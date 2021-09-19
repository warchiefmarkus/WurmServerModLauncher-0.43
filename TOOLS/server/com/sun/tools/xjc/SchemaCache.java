/*     */ package com.sun.tools.xjc;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import javax.xml.validation.Schema;
/*     */ import javax.xml.validation.SchemaFactory;
/*     */ import javax.xml.validation.ValidatorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SchemaCache
/*     */ {
/*     */   private Schema schema;
/*     */   private final URL source;
/*     */   
/*     */   public SchemaCache(URL source) {
/*  68 */     this.source = source;
/*     */   }
/*     */   
/*     */   public ValidatorHandler newValidator() {
/*  72 */     synchronized (this) {
/*  73 */       if (this.schema == null) {
/*     */         try {
/*  75 */           this.schema = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema").newSchema(this.source);
/*  76 */         } catch (SAXException e) {
/*     */           
/*  78 */           throw new AssertionError(e);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/*  83 */     ValidatorHandler handler = this.schema.newValidatorHandler();
/*  84 */     fixValidatorBug6246922(handler);
/*     */     
/*  86 */     return handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fixValidatorBug6246922(ValidatorHandler handler) {
/*     */     try {
/*     */       Class<?> xsformatter;
/*  95 */       Field f = handler.getClass().getDeclaredField("errorReporter");
/*  96 */       f.setAccessible(true);
/*  97 */       Object errorReporter = f.get(handler);
/*     */       
/*  99 */       Method get = errorReporter.getClass().getDeclaredMethod("getMessageFormatter", new Class[] { String.class });
/* 100 */       Object currentFormatter = get.invoke(errorReporter, new Object[] { "http://www.w3.org/TR/xml-schema-1" });
/* 101 */       if (currentFormatter != null) {
/*     */         return;
/*     */       }
/*     */       
/* 105 */       Method put = null;
/* 106 */       for (Method m : errorReporter.getClass().getDeclaredMethods()) {
/* 107 */         if (m.getName().equals("putMessageFormatter")) {
/* 108 */           put = m;
/*     */           break;
/*     */         } 
/*     */       } 
/* 112 */       if (put == null)
/*     */         return; 
/* 114 */       ClassLoader cl = errorReporter.getClass().getClassLoader();
/* 115 */       String className = "com.sun.org.apache.xerces.internal.impl.xs.XSMessageFormatter";
/*     */       
/* 117 */       if (cl == null) {
/* 118 */         xsformatter = Class.forName(className);
/*     */       } else {
/* 120 */         xsformatter = cl.loadClass(className);
/*     */       } 
/*     */       
/* 123 */       put.invoke(errorReporter, new Object[] { "http://www.w3.org/TR/xml-schema-1", xsformatter.newInstance() });
/* 124 */     } catch (Throwable t) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\SchemaCache.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */