/*     */ package com.sun.xml.bind.v2.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import javax.activation.DataHandler;
/*     */ import javax.activation.DataSource;
/*     */ import javax.activation.MimeType;
/*     */ import javax.activation.MimeTypeParseException;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DataSourceSource
/*     */   extends StreamSource
/*     */ {
/*     */   private final DataSource source;
/*     */   private final String charset;
/*     */   private Reader r;
/*     */   private InputStream is;
/*     */   
/*     */   public DataSourceSource(DataHandler dh) throws MimeTypeParseException {
/*  80 */     this(dh.getDataSource());
/*     */   }
/*     */   
/*     */   public DataSourceSource(DataSource source) throws MimeTypeParseException {
/*  84 */     this.source = source;
/*     */     
/*  86 */     String ct = source.getContentType();
/*  87 */     if (ct == null) {
/*  88 */       this.charset = null;
/*     */     } else {
/*  90 */       MimeType mimeType = new MimeType(ct);
/*  91 */       this.charset = mimeType.getParameter("charset");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setReader(Reader reader) {
/*  97 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInputStream(InputStream inputStream) {
/* 102 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Reader getReader() {
/*     */     try {
/* 108 */       if (this.charset == null) return null; 
/* 109 */       if (this.r == null)
/* 110 */         this.r = new InputStreamReader(this.source.getInputStream(), this.charset); 
/* 111 */       return this.r;
/* 112 */     } catch (IOException e) {
/*     */       
/* 114 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() {
/*     */     try {
/* 121 */       if (this.charset != null) return null; 
/* 122 */       if (this.is == null)
/* 123 */         this.is = this.source.getInputStream(); 
/* 124 */       return this.is;
/* 125 */     } catch (IOException e) {
/*     */       
/* 127 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public DataSource getDataSource() {
/* 132 */     return this.source;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v\\util\DataSourceSource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */