/*     */ package org.apache.http.util;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import org.apache.http.Consts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class EncodingUtils
/*     */ {
/*     */   public static String getString(byte[] data, int offset, int length, String charset) {
/*  59 */     if (data == null) {
/*  60 */       throw new IllegalArgumentException("Parameter may not be null");
/*     */     }
/*     */     
/*  63 */     if (charset == null || charset.length() == 0) {
/*  64 */       throw new IllegalArgumentException("charset may not be null or empty");
/*     */     }
/*     */     
/*     */     try {
/*  68 */       return new String(data, offset, length, charset);
/*  69 */     } catch (UnsupportedEncodingException e) {
/*  70 */       return new String(data, offset, length);
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
/*     */   public static String getString(byte[] data, String charset) {
/*  85 */     if (data == null) {
/*  86 */       throw new IllegalArgumentException("Parameter may not be null");
/*     */     }
/*  88 */     return getString(data, 0, data.length, charset);
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
/*     */   public static byte[] getBytes(String data, String charset) {
/* 101 */     if (data == null) {
/* 102 */       throw new IllegalArgumentException("data may not be null");
/*     */     }
/*     */     
/* 105 */     if (charset == null || charset.length() == 0) {
/* 106 */       throw new IllegalArgumentException("charset may not be null or empty");
/*     */     }
/*     */     
/*     */     try {
/* 110 */       return data.getBytes(charset);
/* 111 */     } catch (UnsupportedEncodingException e) {
/* 112 */       return data.getBytes();
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
/*     */   public static byte[] getAsciiBytes(String data) {
/* 124 */     if (data == null) {
/* 125 */       throw new IllegalArgumentException("Parameter may not be null");
/*     */     }
/*     */     
/*     */     try {
/* 129 */       return data.getBytes(Consts.ASCII.name());
/* 130 */     } catch (UnsupportedEncodingException e) {
/* 131 */       throw new Error("HttpClient requires ASCII support");
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
/*     */   public static String getAsciiString(byte[] data, int offset, int length) {
/* 147 */     if (data == null) {
/* 148 */       throw new IllegalArgumentException("Parameter may not be null");
/*     */     }
/*     */     
/*     */     try {
/* 152 */       return new String(data, offset, length, Consts.ASCII.name());
/* 153 */     } catch (UnsupportedEncodingException e) {
/* 154 */       throw new Error("HttpClient requires ASCII support");
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
/*     */   public static String getAsciiString(byte[] data) {
/* 167 */     if (data == null) {
/* 168 */       throw new IllegalArgumentException("Parameter may not be null");
/*     */     }
/* 170 */     return getAsciiString(data, 0, data.length);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\htt\\util\EncodingUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */