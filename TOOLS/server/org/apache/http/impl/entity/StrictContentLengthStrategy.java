/*     */ package org.apache.http.impl.entity;
/*     */ 
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class StrictContentLengthStrategy
/*     */   implements ContentLengthStrategy
/*     */ {
/*     */   private final int implicitLen;
/*     */   
/*     */   public StrictContentLengthStrategy(int implicitLen) {
/*  64 */     this.implicitLen = implicitLen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StrictContentLengthStrategy() {
/*  72 */     this(-1);
/*     */   }
/*     */   
/*     */   public long determineLength(HttpMessage message) throws HttpException {
/*  76 */     if (message == null) {
/*  77 */       throw new IllegalArgumentException("HTTP message may not be null");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  82 */     Header transferEncodingHeader = message.getFirstHeader("Transfer-Encoding");
/*  83 */     if (transferEncodingHeader != null) {
/*  84 */       String s = transferEncodingHeader.getValue();
/*  85 */       if ("chunked".equalsIgnoreCase(s)) {
/*  86 */         if (message.getProtocolVersion().lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0)) {
/*  87 */           throw new ProtocolException("Chunked transfer encoding not allowed for " + message.getProtocolVersion());
/*     */         }
/*     */ 
/*     */         
/*  91 */         return -2L;
/*  92 */       }  if ("identity".equalsIgnoreCase(s)) {
/*  93 */         return -1L;
/*     */       }
/*  95 */       throw new ProtocolException("Unsupported transfer encoding: " + s);
/*     */     } 
/*     */ 
/*     */     
/*  99 */     Header contentLengthHeader = message.getFirstHeader("Content-Length");
/* 100 */     if (contentLengthHeader != null) {
/* 101 */       String s = contentLengthHeader.getValue();
/*     */       try {
/* 103 */         long len = Long.parseLong(s);
/* 104 */         if (len < 0L) {
/* 105 */           throw new ProtocolException("Negative content length: " + s);
/*     */         }
/* 107 */         return len;
/* 108 */       } catch (NumberFormatException e) {
/* 109 */         throw new ProtocolException("Invalid content length: " + s);
/*     */       } 
/*     */     } 
/* 112 */     return this.implicitLen;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\entity\StrictContentLengthStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */