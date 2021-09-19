/*     */ package org.flywaydb.core.internal.util;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLDecoder;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.flywaydb.core.api.FlywayException;
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
/*     */ public class ClassUtils
/*     */ {
/*     */   public static synchronized <T> T instantiate(String className, ClassLoader classLoader) throws Exception {
/*  49 */     return (T)Class.forName(className, true, classLoader).newInstance();
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
/*     */   public static <T> List<T> instantiateAll(String[] classes, ClassLoader classLoader) {
/*  61 */     List<T> clazzes = new ArrayList<T>();
/*  62 */     for (String clazz : classes) {
/*  63 */       if (StringUtils.hasLength(clazz)) {
/*     */         try {
/*  65 */           clazzes.add(instantiate(clazz, classLoader));
/*  66 */         } catch (Exception e) {
/*  67 */           throw new FlywayException("Unable to instantiate class: " + clazz, e);
/*     */         } 
/*     */       }
/*     */     } 
/*  71 */     return clazzes;
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
/*     */   public static boolean isPresent(String className, ClassLoader classLoader) {
/*     */     try {
/*  85 */       classLoader.loadClass(className);
/*  86 */       return true;
/*  87 */     } catch (Throwable ex) {
/*     */       
/*  89 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getShortName(Class<?> aClass) {
/* 100 */     String name = aClass.getName();
/* 101 */     return name.substring(name.lastIndexOf(".") + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getLocationOnDisk(Class<?> aClass) {
/*     */     try {
/* 112 */       ProtectionDomain protectionDomain = aClass.getProtectionDomain();
/* 113 */       if (protectionDomain == null)
/*     */       {
/* 115 */         return null;
/*     */       }
/* 117 */       String url = protectionDomain.getCodeSource().getLocation().getPath();
/* 118 */       return URLDecoder.decode(url, "UTF-8");
/* 119 */     } catch (UnsupportedEncodingException e) {
/*     */       
/* 121 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\ClassUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */