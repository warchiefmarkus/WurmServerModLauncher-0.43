/*     */ package com.sun.javaws.security;
/*     */ 
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.TraceLevel;
/*     */ import com.sun.javaws.Globals;
/*     */ import com.sun.javaws.cache.DownloadProtocol;
/*     */ import com.sun.javaws.exceptions.JARSigningException;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.security.CodeSource;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SigningInfo
/*     */ {
/*     */   public static Certificate[] checkSigning(URL paramURL, String paramString, JarFile paramJarFile, DownloadProtocol.DownloadDelegate paramDownloadDelegate, File paramFile) throws JARSigningException {
/*  46 */     Certificate[] arrayOfCertificate = null;
/*  47 */     boolean bool1 = false;
/*  48 */     boolean bool2 = false;
/*  49 */     Object object = null;
/*  50 */     int i = paramJarFile.size();
/*  51 */     byte b = 0;
/*     */ 
/*     */     
/*  54 */     if (paramDownloadDelegate != null) paramDownloadDelegate.validating(paramURL, 0, i); 
/*  55 */     BufferedOutputStream bufferedOutputStream = null;
/*  56 */     InputStream inputStream = null;
/*     */     try {
/*  58 */       byte[] arrayOfByte = new byte[32768];
/*  59 */       Enumeration enumeration = paramJarFile.entries();
/*  60 */       while (enumeration.hasMoreElements()) {
/*  61 */         JarEntry jarEntry = enumeration.nextElement();
/*  62 */         String str = jarEntry.getName();
/*     */ 
/*     */         
/*  65 */         if (!str.startsWith("META-INF/") && !str.endsWith("/") && jarEntry.getSize() != 0L) {
/*  66 */           inputStream = paramJarFile.getInputStream(jarEntry);
/*     */ 
/*     */           
/*  69 */           if (paramFile != null && str.indexOf("/") == -1) {
/*  70 */             File file = new File(paramFile, jarEntry.getName());
/*  71 */             bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
/*     */           } 
/*     */           int j;
/*  74 */           while ((j = inputStream.read(arrayOfByte, 0, arrayOfByte.length)) != -1) {
/*     */ 
/*     */             
/*  77 */             if (bufferedOutputStream != null) bufferedOutputStream.write(arrayOfByte, 0, j); 
/*     */           } 
/*  79 */           if (bufferedOutputStream != null) { bufferedOutputStream.close(); bufferedOutputStream = null; }
/*  80 */            inputStream.close(); inputStream = null;
/*     */           
/*  82 */           Certificate[] arrayOfCertificate1 = jarEntry.getCertificates();
/*  83 */           if (arrayOfCertificate1 != null && arrayOfCertificate1.length == 0) arrayOfCertificate1 = null;
/*     */           
/*  85 */           boolean bool = false;
/*  86 */           if (arrayOfCertificate1 != null) {
/*  87 */             bool = true;
/*     */             
/*  89 */             if (arrayOfCertificate == null) {
/*  90 */               arrayOfCertificate = arrayOfCertificate1;
/*  91 */             } else if (!equalChains(arrayOfCertificate, arrayOfCertificate1)) {
/*     */               
/*  93 */               throw new JARSigningException(paramURL, paramString, 1);
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/*  98 */           bool1 = (bool1 || bool) ? true : false;
/*  99 */           bool2 = (bool2 || !bool) ? true : false;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 104 */         if (paramDownloadDelegate != null) paramDownloadDelegate.validating(paramURL, ++b, i); 
/*     */       } 
/* 106 */     } catch (SecurityException securityException) {
/* 107 */       throw new JARSigningException(paramURL, paramString, 2, securityException);
/* 108 */     } catch (IOException iOException) {
/* 109 */       throw new JARSigningException(paramURL, paramString, 2, iOException);
/*     */     } finally {
/*     */       
/* 112 */       try { if (bufferedOutputStream != null) bufferedOutputStream.close(); 
/* 113 */         if (inputStream != null) inputStream.close();  }
/* 114 */       catch (IOException iOException) { Trace.ignoredException(iOException); }
/*     */     
/*     */     } 
/* 117 */     if (bool1)
/*     */     {
/* 119 */       if (bool2) {
/* 120 */         throw new JARSigningException(paramURL, paramString, 3);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 126 */     if (arrayOfCertificate != null) {
/*     */       try {
/* 128 */         Manifest manifest = paramJarFile.getManifest();
/* 129 */         Set set = manifest.getEntries().entrySet();
/* 130 */         Iterator iterator = set.iterator();
/* 131 */         while (iterator.hasNext()) {
/* 132 */           Map.Entry entry = iterator.next();
/* 133 */           String str = (String)entry.getKey();
/*     */ 
/*     */           
/* 136 */           if (isSignedManifestEntry(manifest, str) && paramJarFile.getEntry(str) == null)
/*     */           {
/* 138 */             throw new JARSigningException(paramURL, paramString, 4, str);
/*     */           }
/*     */         } 
/* 141 */       } catch (IOException iOException) {
/* 142 */         throw new JARSigningException(paramURL, paramString, 2, iOException);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 147 */     return arrayOfCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CodeSource getCodeSource(URL paramURL, JarFile paramJarFile) {
/* 154 */     Enumeration enumeration = paramJarFile.entries();
/* 155 */     byte[] arrayOfByte = new byte[32768];
/* 156 */     while (enumeration.hasMoreElements()) {
/* 157 */       JarEntry jarEntry = enumeration.nextElement();
/* 158 */       String str = jarEntry.getName();
/* 159 */       Trace.println(" ... name=" + str, TraceLevel.SECURITY);
/*     */ 
/*     */       
/* 162 */       if (!str.startsWith("META-INF/") && !str.endsWith("/") && jarEntry.getSize() != 0L) {
/*     */         
/*     */         try {
/* 165 */           InputStream inputStream = paramJarFile.getInputStream(jarEntry);
/*     */           int i;
/* 167 */           while ((i = inputStream.read(arrayOfByte, 0, arrayOfByte.length)) != -1);
/* 168 */           inputStream.close();
/* 169 */         } catch (IOException iOException) {
/* 170 */           Trace.ignoredException(iOException);
/*     */         } 
/* 172 */         if (Globals.isJavaVersionAtLeast15())
/*     */         {
/* 174 */           return new CodeSource(paramURL, jarEntry.getCodeSigners());
/*     */         }
/*     */         
/* 177 */         return new CodeSource(paramURL, jarEntry.getCertificates());
/*     */       } 
/*     */     } 
/*     */     
/* 181 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean equalChains(Certificate[] paramArrayOfCertificate1, Certificate[] paramArrayOfCertificate2) {
/* 186 */     if (paramArrayOfCertificate1.length != paramArrayOfCertificate2.length) {
/* 187 */       return false;
/*     */     }
/* 189 */     for (byte b = 0; b < paramArrayOfCertificate1.length; b++) {
/* 190 */       if (!paramArrayOfCertificate1[b].equals(paramArrayOfCertificate2[b])) return false; 
/*     */     } 
/* 192 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isSignedManifestEntry(Manifest paramManifest, String paramString) {
/* 197 */     Attributes attributes = paramManifest.getAttributes(paramString);
/* 198 */     if (attributes != null) {
/* 199 */       Iterator iterator = attributes.keySet().iterator();
/* 200 */       while (iterator.hasNext()) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 205 */         String str = iterator.next().toString();
/* 206 */         str = str.toUpperCase(Locale.ENGLISH);
/* 207 */         if (str.endsWith("-DIGEST") || str.indexOf("-DIGEST-") != -1)
/*     */         {
/* 209 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 213 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\security\SigningInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */