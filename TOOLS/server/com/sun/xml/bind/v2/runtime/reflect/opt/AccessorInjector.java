/*     */ package com.sun.xml.bind.v2.runtime.reflect.opt;
/*     */ 
/*     */ import com.sun.xml.bind.Util;
/*     */ import com.sun.xml.bind.v2.bytecode.ClassTailor;
/*     */ import java.io.InputStream;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ class AccessorInjector
/*     */ {
/*  51 */   private static final Logger logger = Util.getClassLogger();
/*     */   
/*  53 */   protected static final boolean noOptimize = (Util.getSystemProperty(ClassTailor.class.getName() + ".noOptimize") != null);
/*     */ 
/*     */   
/*     */   static {
/*  57 */     if (noOptimize) {
/*  58 */       logger.info("The optimized code generation is disabled");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> prepare(Class beanClass, String templateClassName, String newClassName, String... replacements) {
/*  70 */     if (noOptimize) {
/*  71 */       return null;
/*     */     }
/*     */     try {
/*  74 */       ClassLoader cl = beanClass.getClassLoader();
/*  75 */       if (cl == null) return null;
/*     */       
/*  77 */       Class<?> c = Injector.find(cl, newClassName);
/*  78 */       if (c == null) {
/*  79 */         byte[] image = tailor(templateClassName, newClassName, replacements);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  85 */         if (image == null)
/*  86 */           return null; 
/*  87 */         c = Injector.inject(cl, newClassName, image);
/*     */       } 
/*  89 */       return c;
/*  90 */     } catch (SecurityException e) {
/*     */       
/*  92 */       logger.log(Level.INFO, "Unable to create an optimized TransducedAccessor ", e);
/*  93 */       return null;
/*     */     } 
/*     */   }
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
/*     */   private static byte[] tailor(String templateClassName, String newClassName, String... replacements) {
/*     */     InputStream resource;
/* 112 */     if (CLASS_LOADER != null) {
/* 113 */       resource = CLASS_LOADER.getResourceAsStream(templateClassName + ".class");
/*     */     } else {
/* 115 */       resource = ClassLoader.getSystemResourceAsStream(templateClassName + ".class");
/* 116 */     }  if (resource == null) {
/* 117 */       return null;
/*     */     }
/* 119 */     return ClassTailor.tailor(resource, templateClassName, newClassName, replacements);
/*     */   }
/*     */   
/* 122 */   private static final ClassLoader CLASS_LOADER = AccessorInjector.class.getClassLoader();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\opt\AccessorInjector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */