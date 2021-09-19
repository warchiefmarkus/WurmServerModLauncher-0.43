/*     */ package com.sun.tools.xjc.runtime;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
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
/*     */ public class JAXBContextFactory
/*     */ {
/*     */   private static final String DOT_OBJECT_FACTORY = ".ObjectFactory";
/*     */   private static final String IMPL_DOT_OBJECT_FACTORY = ".impl.ObjectFactory";
/*     */   
/*     */   public static JAXBContext createContext(Class[] classes, Map properties) throws JAXBException {
/*  93 */     Class[] r = new Class[classes.length];
/*  94 */     boolean modified = false;
/*     */ 
/*     */ 
/*     */     
/*  98 */     for (int i = 0; i < r.length; i++) {
/*  99 */       Class<?> c = classes[i];
/* 100 */       String name = c.getName();
/* 101 */       if (name.endsWith(".ObjectFactory") && !name.endsWith(".impl.ObjectFactory")) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 107 */         name = name.substring(0, name.length() - ".ObjectFactory".length()) + ".impl.ObjectFactory";
/*     */         
/*     */         try {
/* 110 */           c = c.getClassLoader().loadClass(name);
/* 111 */         } catch (ClassNotFoundException e) {
/* 112 */           throw new JAXBException(e);
/*     */         } 
/*     */         
/* 115 */         modified = true;
/*     */       } 
/*     */       
/* 118 */       r[i] = c;
/*     */     } 
/*     */     
/* 121 */     if (!modified)
/*     */     {
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
/* 134 */       throw new JAXBException("Unable to find a JAXB implementation to delegate");
/*     */     }
/*     */ 
/*     */     
/* 138 */     return JAXBContext.newInstance(r, properties);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JAXBContext createContext(String contextPath, ClassLoader classLoader, Map properties) throws JAXBException {
/* 148 */     List<Class<?>> classes = new ArrayList<Class<?>>();
/* 149 */     StringTokenizer tokens = new StringTokenizer(contextPath, ":");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 156 */       while (tokens.hasMoreTokens()) {
/* 157 */         String pkg = tokens.nextToken();
/* 158 */         classes.add(classLoader.loadClass(pkg + ".impl.ObjectFactory"));
/*     */       } 
/* 160 */     } catch (ClassNotFoundException e) {
/* 161 */       throw new JAXBException(e);
/*     */     } 
/*     */ 
/*     */     
/* 165 */     return JAXBContext.newInstance((Class[])classes.<Class<?>[]>toArray((Class<?>[][])new Class[classes.size()]), properties);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\runtime\JAXBContextFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */