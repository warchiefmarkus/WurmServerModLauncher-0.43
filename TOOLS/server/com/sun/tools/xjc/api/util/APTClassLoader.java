/*     */ package com.sun.tools.xjc.api.util;
/*     */ 
/*     */ import com.sun.istack.Nullable;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
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
/*     */ public final class APTClassLoader
/*     */   extends URLClassLoader
/*     */ {
/*     */   private final String[] packagePrefixes;
/*     */   
/*     */   public APTClassLoader(@Nullable ClassLoader parent, String[] packagePrefixes) throws ToolsJarNotFoundException {
/*  71 */     super(getToolsJar(parent), parent);
/*  72 */     if ((getURLs()).length == 0) {
/*     */ 
/*     */       
/*  75 */       this.packagePrefixes = new String[0];
/*     */     } else {
/*  77 */       this.packagePrefixes = packagePrefixes;
/*     */     } 
/*     */   }
/*     */   public Class loadClass(String className) throws ClassNotFoundException {
/*  81 */     for (String prefix : this.packagePrefixes) {
/*  82 */       if (className.startsWith(prefix))
/*     */       {
/*     */         
/*  85 */         return findClass(className);
/*     */       }
/*     */     } 
/*     */     
/*  89 */     return super.loadClass(className);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class findClass(String name) throws ClassNotFoundException {
/*  95 */     StringBuilder sb = new StringBuilder(name.length() + 6);
/*  96 */     sb.append(name.replace('.', '/')).append(".class");
/*     */     
/*  98 */     InputStream is = getResourceAsStream(sb.toString());
/*  99 */     if (is == null) {
/* 100 */       throw new ClassNotFoundException("Class not found" + sb);
/*     */     }
/*     */     try {
/* 103 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 104 */       byte[] buf = new byte[1024];
/*     */       int len;
/* 106 */       while ((len = is.read(buf)) >= 0) {
/* 107 */         baos.write(buf, 0, len);
/*     */       }
/* 109 */       buf = baos.toByteArray();
/*     */ 
/*     */       
/* 112 */       int i = name.lastIndexOf('.');
/* 113 */       if (i != -1) {
/* 114 */         String pkgname = name.substring(0, i);
/* 115 */         Package pkg = getPackage(pkgname);
/* 116 */         if (pkg == null) {
/* 117 */           definePackage(pkgname, null, null, null, null, null, null, null);
/*     */         }
/*     */       } 
/* 120 */       return defineClass(name, buf, 0, buf.length);
/* 121 */     } catch (IOException e) {
/* 122 */       throw new ClassNotFoundException(name, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static URL[] getToolsJar(@Nullable ClassLoader parent) throws ToolsJarNotFoundException {
/*     */     try {
/* 133 */       Class.forName("com.sun.tools.javac.Main", false, parent);
/* 134 */       Class.forName("com.sun.tools.apt.Main", false, parent);
/* 135 */       return new URL[0];
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 140 */     catch (ClassNotFoundException e) {
/*     */ 
/*     */ 
/*     */       
/* 144 */       File jreHome = new File(System.getProperty("java.home"));
/* 145 */       File toolsJar = new File(jreHome.getParent(), "lib/tools.jar");
/*     */       
/* 147 */       if (!toolsJar.exists()) {
/* 148 */         throw new ToolsJarNotFoundException(toolsJar);
/*     */       }
/*     */       
/*     */       try {
/* 152 */         return new URL[] { toolsJar.toURL() };
/* 153 */       } catch (MalformedURLException malformedURLException) {
/*     */         
/* 155 */         throw new AssertionError(malformedURLException);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\ap\\util\APTClassLoader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */