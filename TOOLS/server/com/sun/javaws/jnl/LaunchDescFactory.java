/*     */ package com.sun.javaws.jnl;
/*     */ 
/*     */ import com.sun.deploy.resources.ResourceManager;
/*     */ import com.sun.javaws.JavawsFactory;
/*     */ import com.sun.javaws.cache.Cache;
/*     */ import com.sun.javaws.exceptions.BadFieldException;
/*     */ import com.sun.javaws.exceptions.JNLParseException;
/*     */ import com.sun.javaws.exceptions.MissingFieldException;
/*     */ import com.sun.javaws.net.HttpRequest;
/*     */ import com.sun.javaws.net.HttpResponse;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LaunchDescFactory
/*     */ {
/*     */   public static LaunchDesc buildDescriptor(byte[] paramArrayOfbyte) throws IOException, BadFieldException, MissingFieldException, JNLParseException {
/*  46 */     return XMLFormat.parse(paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static LaunchDesc buildDescriptor(InputStream paramInputStream) throws IOException, BadFieldException, MissingFieldException, JNLParseException {
/*  52 */     return buildDescriptor(readBytes(paramInputStream, -1L));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static LaunchDesc buildDescriptor(InputStream paramInputStream, long paramLong) throws IOException, BadFieldException, MissingFieldException, JNLParseException {
/*  58 */     return buildDescriptor(readBytes(paramInputStream, paramLong));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LaunchDesc buildDescriptor(File paramFile) throws IOException, BadFieldException, MissingFieldException, JNLParseException {
/*  66 */     return buildDescriptor(new FileInputStream(paramFile), paramFile.length());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static LaunchDesc buildDescriptor(URL paramURL) throws IOException, BadFieldException, MissingFieldException, JNLParseException {
/*  72 */     File file = Cache.getCachedLaunchedFile(paramURL);
/*  73 */     if (file != null) {
/*  74 */       return buildDescriptor(file);
/*     */     }
/*  76 */     HttpRequest httpRequest = JavawsFactory.getHttpRequestImpl();
/*  77 */     HttpResponse httpResponse = httpRequest.doGetRequest(paramURL);
/*  78 */     BufferedInputStream bufferedInputStream = httpResponse.getInputStream();
/*  79 */     int i = httpResponse.getContentLength();
/*     */     
/*  81 */     LaunchDesc launchDesc = buildDescriptor(bufferedInputStream, i);
/*     */     
/*  83 */     bufferedInputStream.close();
/*     */     
/*  85 */     return launchDesc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LaunchDesc buildDescriptor(String paramString) throws IOException, BadFieldException, MissingFieldException, JNLParseException {
/*  93 */     FileInputStream fileInputStream = null;
/*  94 */     int i = -1;
/*     */     try {
/*  96 */       URL uRL = new URL(paramString);
/*     */       
/*  98 */       return buildDescriptor(uRL);
/*     */     }
/* 100 */     catch (MalformedURLException malformedURLException) {
/*     */       
/* 102 */       if (malformedURLException.getMessage().indexOf("https") != -1) {
/* 103 */         throw new BadFieldException(ResourceManager.getString("launch.error.badfield.download.https"), "<jnlp>", "https");
/*     */       }
/*     */ 
/*     */       
/* 107 */       fileInputStream = new FileInputStream(paramString);
/* 108 */       long l = (new File(paramString)).length();
/* 109 */       if (l > 1048576L) throw new IOException("File too large"); 
/* 110 */       i = (int)l;
/*     */       
/* 112 */       return buildDescriptor(fileInputStream, i);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static LaunchDesc buildInternalLaunchDesc(String paramString1, String paramString2, String paramString3) {
/* 118 */     return new LaunchDesc("0.1", null, null, null, null, 1, null, 5, null, null, null, null, (paramString3 == null) ? paramString1 : paramString3, paramString2, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] readBytes(InputStream paramInputStream, long paramLong) throws IOException {
/* 139 */     if (paramLong > 1048576L) throw new IOException("File too large");
/*     */     
/* 141 */     BufferedInputStream bufferedInputStream = null;
/* 142 */     if (paramInputStream instanceof BufferedInputStream) {
/* 143 */       bufferedInputStream = (BufferedInputStream)paramInputStream;
/*     */     } else {
/* 145 */       bufferedInputStream = new BufferedInputStream(paramInputStream);
/*     */     } 
/*     */     
/* 148 */     if (paramLong <= 0L) paramLong = 10240L; 
/* 149 */     byte[] arrayOfByte = new byte[(int)paramLong];
/*     */     
/* 151 */     int j = 0;
/* 152 */     int i = bufferedInputStream.read(arrayOfByte, j, arrayOfByte.length - j);
/* 153 */     while (i != -1) {
/* 154 */       j += i;
/*     */       
/* 156 */       if (arrayOfByte.length == j) {
/* 157 */         byte[] arrayOfByte1 = new byte[arrayOfByte.length * 2];
/* 158 */         System.arraycopy(arrayOfByte, 0, arrayOfByte1, 0, arrayOfByte.length);
/* 159 */         arrayOfByte = arrayOfByte1;
/*     */       } 
/*     */       
/* 162 */       i = bufferedInputStream.read(arrayOfByte, j, arrayOfByte.length - j);
/*     */     } 
/* 164 */     bufferedInputStream.close();
/* 165 */     paramInputStream.close();
/*     */     
/* 167 */     if (j != arrayOfByte.length) {
/* 168 */       byte[] arrayOfByte1 = new byte[j];
/* 169 */       System.arraycopy(arrayOfByte, 0, arrayOfByte1, 0, j);
/* 170 */       arrayOfByte = arrayOfByte1;
/*     */     } 
/* 172 */     return arrayOfByte;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jnl\LaunchDescFactory.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */