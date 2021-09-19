/*     */ package com.sun.tools.xjc;
/*     */ 
/*     */ import com.sun.istack.tools.MaskingClassLoader;
/*     */ import com.sun.istack.tools.ParallelWorldClassLoader;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ClassLoaderBuilder
/*     */ {
/*     */   protected static ClassLoader createProtectiveClassLoader(ClassLoader cl, String v) throws ClassNotFoundException, MalformedURLException {
/*     */     URLClassLoader uRLClassLoader;
/*     */     ParallelWorldClassLoader parallelWorldClassLoader;
/*  67 */     if (noHack) return cl;
/*     */     
/*  69 */     boolean mustang = false;
/*     */     
/*  71 */     if (JAXBContext.class.getClassLoader() == null) {
/*     */       
/*  73 */       mustang = true;
/*     */       
/*  75 */       List mask = new ArrayList(Arrays.asList((Object[])maskedPackages));
/*  76 */       mask.add("javax.xml.bind.");
/*     */       
/*  78 */       MaskingClassLoader maskingClassLoader = new MaskingClassLoader(cl, mask);
/*     */       
/*  80 */       URL apiUrl = maskingClassLoader.getResource("javax/xml/bind/annotation/XmlSeeAlso.class");
/*  81 */       if (apiUrl == null) {
/*  82 */         throw new ClassNotFoundException("There's no JAXB 2.1 API in the classpath");
/*     */       }
/*  84 */       uRLClassLoader = new URLClassLoader(new URL[] { ParallelWorldClassLoader.toJarUrl(apiUrl) }, (ClassLoader)maskingClassLoader);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     if (v.equals("1.0")) {
/*  94 */       MaskingClassLoader maskingClassLoader; if (!mustang)
/*     */       {
/*  96 */         maskingClassLoader = new MaskingClassLoader(uRLClassLoader, toolPackages); } 
/*  97 */       parallelWorldClassLoader = new ParallelWorldClassLoader((ClassLoader)maskingClassLoader, "1.0/");
/*     */     }
/*  99 */     else if (mustang) {
/*     */       
/* 101 */       parallelWorldClassLoader = new ParallelWorldClassLoader((ClassLoader)parallelWorldClassLoader, "");
/*     */     } 
/*     */     
/* 104 */     return (ClassLoader)parallelWorldClassLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   private static String[] maskedPackages = new String[] { "com.sun.tools.", "com.sun.codemodel.", "com.sun.relaxng.", "com.sun.xml.xsom.", "com.sun.xml.bind." };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   private static String[] toolPackages = new String[] { "com.sun.tools.", "com.sun.codemodel.", "com.sun.relaxng.", "com.sun.xml.xsom." };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 132 */   public static final boolean noHack = Boolean.getBoolean(XJCFacade.class.getName() + ".nohack");
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\ClassLoaderBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */