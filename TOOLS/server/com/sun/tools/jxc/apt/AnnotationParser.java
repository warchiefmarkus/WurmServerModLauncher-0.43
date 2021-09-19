/*     */ package com.sun.tools.jxc.apt;
/*     */ 
/*     */ import com.sun.mirror.apt.AnnotationProcessor;
/*     */ import com.sun.mirror.apt.AnnotationProcessorEnvironment;
/*     */ import com.sun.mirror.declaration.AnnotationTypeDeclaration;
/*     */ import com.sun.tools.jxc.ConfigReader;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.api.ErrorListener;
/*     */ import com.sun.tools.xjc.api.J2SJAXBModel;
/*     */ import com.sun.tools.xjc.api.Reference;
/*     */ import com.sun.tools.xjc.api.XJC;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.xml.bind.SchemaOutputResolver;
/*     */ import org.xml.sax.ErrorHandler;
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
/*     */ final class AnnotationParser
/*     */   implements AnnotationProcessor
/*     */ {
/*     */   private final AnnotationProcessorEnvironment env;
/*     */   private ErrorReceiver errorListener;
/*     */   
/*     */   public AnnotationProcessorEnvironment getEnv() {
/*  81 */     return this.env;
/*     */   }
/*     */ 
/*     */   
/*     */   AnnotationParser(Set<AnnotationTypeDeclaration> atds, AnnotationProcessorEnvironment env) {
/*  86 */     this.env = env;
/*  87 */     this.errorListener = new ErrorReceiverImpl(env.getMessager(), env.getOptions().containsKey("-Ajaxb.debug"));
/*     */   }
/*     */   
/*     */   public void process() {
/*  91 */     for (Map.Entry<String, String> me : (Iterable<Map.Entry<String, String>>)this.env.getOptions().entrySet()) {
/*  92 */       String key = me.getKey();
/*  93 */       if (key.startsWith("-Ajaxb.config=")) {
/*     */ 
/*     */         
/*  96 */         String value = key.substring("-Ajaxb.config".length() + 1);
/*     */ 
/*     */ 
/*     */         
/* 100 */         StringTokenizer st = new StringTokenizer(value, File.pathSeparator);
/* 101 */         if (!st.hasMoreTokens()) {
/* 102 */           this.errorListener.error(null, Messages.OPERAND_MISSING.format(new Object[] { "-Ajaxb.config" }));
/*     */           
/*     */           continue;
/*     */         } 
/* 106 */         while (st.hasMoreTokens()) {
/* 107 */           File configFile = new File(st.nextToken());
/* 108 */           if (!configFile.exists()) {
/* 109 */             this.errorListener.error(null, Messages.NON_EXISTENT_FILE.format(new Object[0]));
/*     */             
/*     */             continue;
/*     */           } 
/*     */           try {
/* 114 */             ConfigReader configReader = new ConfigReader(this.env, this.env.getTypeDeclarations(), configFile, (ErrorHandler)this.errorListener);
/*     */             
/* 116 */             Collection<Reference> classesToBeIncluded = configReader.getClassesToBeIncluded();
/* 117 */             J2SJAXBModel model = XJC.createJavaCompiler().bind(classesToBeIncluded, Collections.emptyMap(), null, this.env);
/*     */ 
/*     */             
/* 120 */             SchemaOutputResolver schemaOutputResolver = configReader.getSchemaOutputResolver();
/*     */             
/* 122 */             model.generateSchema(schemaOutputResolver, (ErrorListener)this.errorListener);
/* 123 */           } catch (IOException e) {
/* 124 */             this.errorListener.error(e.getMessage(), e);
/* 125 */           } catch (SAXException e) {}
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\jxc\apt\AnnotationParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */