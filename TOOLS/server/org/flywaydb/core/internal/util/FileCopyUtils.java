/*     */ package org.flywaydb.core.internal.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileCopyUtils
/*     */ {
/*     */   public static String copyToString(Reader in) throws IOException {
/*  46 */     StringWriter out = new StringWriter();
/*  47 */     copy(in, out);
/*  48 */     String str = out.toString();
/*     */ 
/*     */     
/*  51 */     if (str.startsWith("﻿")) {
/*  52 */       return str.substring(1);
/*     */     }
/*     */     
/*  55 */     return str;
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
/*     */   public static byte[] copyToByteArray(InputStream in) throws IOException {
/*  67 */     ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
/*  68 */     copy(in, out);
/*  69 */     return out.toByteArray();
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
/*     */   private static void copy(Reader in, Writer out) throws IOException {
/*     */     try {
/*  82 */       char[] buffer = new char[4096];
/*     */       int bytesRead;
/*  84 */       while ((bytesRead = in.read(buffer)) != -1) {
/*  85 */         out.write(buffer, 0, bytesRead);
/*     */       }
/*  87 */       out.flush();
/*     */     } finally {
/*     */       try {
/*  90 */         in.close();
/*  91 */       } catch (IOException iOException) {}
/*     */ 
/*     */       
/*     */       try {
/*  95 */         out.close();
/*  96 */       } catch (IOException iOException) {}
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
/*     */   private static int copy(InputStream in, OutputStream out) throws IOException {
/*     */     try {
/* 113 */       int byteCount = 0;
/* 114 */       byte[] buffer = new byte[4096];
/*     */       int bytesRead;
/* 116 */       while ((bytesRead = in.read(buffer)) != -1) {
/* 117 */         out.write(buffer, 0, bytesRead);
/* 118 */         byteCount += bytesRead;
/*     */       } 
/* 120 */       out.flush();
/* 121 */       return byteCount;
/*     */     } finally {
/*     */       try {
/* 124 */         in.close();
/* 125 */       } catch (IOException iOException) {}
/*     */ 
/*     */       
/*     */       try {
/* 129 */         out.close();
/* 130 */       } catch (IOException iOException) {}
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\FileCopyUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */