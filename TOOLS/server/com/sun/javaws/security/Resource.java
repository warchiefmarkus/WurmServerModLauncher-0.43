/*     */ package com.sun.javaws.security;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.security.CodeSigner;
/*     */ import java.security.cert.Certificate;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Resource
/*     */ {
/*     */   public abstract String getName();
/*     */   
/*     */   public abstract URL getURL();
/*     */   
/*     */   public abstract URL getCodeSourceURL();
/*     */   
/*     */   public abstract InputStream getInputStream() throws IOException;
/*     */   
/*     */   public abstract int getContentLength() throws IOException;
/*     */   
/*     */   public byte[] getBytes() throws IOException {
/*     */     byte[] arrayOfByte;
/*  54 */     InputStream inputStream = getInputStream();
/*  55 */     int i = getContentLength();
/*     */     try {
/*  57 */       if (i != -1) {
/*     */         
/*  59 */         arrayOfByte = new byte[i];
/*  60 */         while (i > 0) {
/*  61 */           int j = inputStream.read(arrayOfByte, arrayOfByte.length - i, i);
/*  62 */           if (j == -1) {
/*  63 */             throw new IOException("unexpected EOF");
/*     */           }
/*  65 */           i -= j;
/*     */         } 
/*     */       } else {
/*     */         
/*  69 */         arrayOfByte = new byte[1024];
/*  70 */         int j = 0;
/*  71 */         while ((i = inputStream.read(arrayOfByte, j, arrayOfByte.length - j)) != -1) {
/*  72 */           j += i;
/*  73 */           if (j >= arrayOfByte.length) {
/*  74 */             byte[] arrayOfByte1 = new byte[j * 2];
/*  75 */             System.arraycopy(arrayOfByte, 0, arrayOfByte1, 0, j);
/*  76 */             arrayOfByte = arrayOfByte1;
/*     */           } 
/*     */         } 
/*     */         
/*  80 */         if (j != arrayOfByte.length) {
/*  81 */           byte[] arrayOfByte1 = new byte[j];
/*  82 */           System.arraycopy(arrayOfByte, 0, arrayOfByte1, 0, j);
/*  83 */           arrayOfByte = arrayOfByte1;
/*     */         } 
/*     */       } 
/*     */     } finally {
/*  87 */       inputStream.close();
/*     */     } 
/*  89 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Manifest getManifest() throws IOException {
/*  96 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Certificate[] getCertificates() {
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CodeSigner[] getCodeSigners() {
/* 110 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\security\Resource.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */