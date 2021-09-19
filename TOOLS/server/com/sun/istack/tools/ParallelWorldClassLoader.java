/*     */ package com.sun.istack.tools;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParallelWorldClassLoader
/*     */   extends ClassLoader
/*     */ {
/*     */   private final String prefix;
/*     */   
/*     */   public ParallelWorldClassLoader(ClassLoader parent, String prefix) {
/*  61 */     super(parent);
/*  62 */     this.prefix = prefix;
/*     */   }
/*     */   
/*     */   protected Class findClass(String name) throws ClassNotFoundException {
/*  66 */     StringBuffer sb = new StringBuffer(name.length() + this.prefix.length() + 6);
/*  67 */     sb.append(this.prefix).append(name.replace('.', '/')).append(".class");
/*     */     
/*  69 */     InputStream is = getParent().getResourceAsStream(sb.toString());
/*  70 */     if (is == null) {
/*  71 */       throw new ClassNotFoundException(name);
/*     */     }
/*     */     try {
/*  74 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*  75 */       byte[] buf = new byte[1024];
/*     */       int len;
/*  77 */       while ((len = is.read(buf)) >= 0) {
/*  78 */         baos.write(buf, 0, len);
/*     */       }
/*  80 */       buf = baos.toByteArray();
/*  81 */       int packIndex = name.lastIndexOf('.');
/*  82 */       if (packIndex != -1) {
/*  83 */         String pkgname = name.substring(0, packIndex);
/*     */         
/*  85 */         Package pkg = getPackage(pkgname);
/*  86 */         if (pkg == null) {
/*  87 */           definePackage(pkgname, null, null, null, null, null, null, null);
/*     */         }
/*     */       } 
/*  90 */       return defineClass(name, buf, 0, buf.length);
/*  91 */     } catch (IOException e) {
/*  92 */       throw new ClassNotFoundException(name, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected URL findResource(String name) {
/*  97 */     return getParent().getResource(this.prefix + name);
/*     */   }
/*     */   
/*     */   protected Enumeration<URL> findResources(String name) throws IOException {
/* 101 */     return getParent().getResources(this.prefix + name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL toJarUrl(URL res) throws ClassNotFoundException, MalformedURLException {
/* 108 */     String url = res.toExternalForm();
/* 109 */     if (!url.startsWith("jar:"))
/* 110 */       throw new ClassNotFoundException("Loaded outside a jar " + url); 
/* 111 */     url = url.substring(4);
/* 112 */     url = url.substring(0, url.lastIndexOf('!'));
/* 113 */     return new URL(url);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\istack\tools\ParallelWorldClassLoader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */