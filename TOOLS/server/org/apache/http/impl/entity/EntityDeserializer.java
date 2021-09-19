/*     */ package org.apache.http.impl.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.entity.BasicHttpEntity;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ import org.apache.http.impl.io.ChunkedInputStream;
/*     */ import org.apache.http.impl.io.ContentLengthInputStream;
/*     */ import org.apache.http.impl.io.IdentityInputStream;
/*     */ import org.apache.http.io.SessionInputBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class EntityDeserializer
/*     */ {
/*     */   private final ContentLengthStrategy lenStrategy;
/*     */   
/*     */   public EntityDeserializer(ContentLengthStrategy lenStrategy) {
/*  68 */     if (lenStrategy == null) {
/*  69 */       throw new IllegalArgumentException("Content length strategy may not be null");
/*     */     }
/*  71 */     this.lenStrategy = lenStrategy;
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
/*     */   protected BasicHttpEntity doDeserialize(SessionInputBuffer inbuffer, HttpMessage message) throws HttpException, IOException {
/*  92 */     BasicHttpEntity entity = new BasicHttpEntity();
/*     */     
/*  94 */     long len = this.lenStrategy.determineLength(message);
/*  95 */     if (len == -2L) {
/*  96 */       entity.setChunked(true);
/*  97 */       entity.setContentLength(-1L);
/*  98 */       entity.setContent((InputStream)new ChunkedInputStream(inbuffer));
/*  99 */     } else if (len == -1L) {
/* 100 */       entity.setChunked(false);
/* 101 */       entity.setContentLength(-1L);
/* 102 */       entity.setContent((InputStream)new IdentityInputStream(inbuffer));
/*     */     } else {
/* 104 */       entity.setChunked(false);
/* 105 */       entity.setContentLength(len);
/* 106 */       entity.setContent((InputStream)new ContentLengthInputStream(inbuffer, len));
/*     */     } 
/*     */     
/* 109 */     Header contentTypeHeader = message.getFirstHeader("Content-Type");
/* 110 */     if (contentTypeHeader != null) {
/* 111 */       entity.setContentType(contentTypeHeader);
/*     */     }
/* 113 */     Header contentEncodingHeader = message.getFirstHeader("Content-Encoding");
/* 114 */     if (contentEncodingHeader != null) {
/* 115 */       entity.setContentEncoding(contentEncodingHeader);
/*     */     }
/* 117 */     return entity;
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
/*     */   public HttpEntity deserialize(SessionInputBuffer inbuffer, HttpMessage message) throws HttpException, IOException {
/* 137 */     if (inbuffer == null) {
/* 138 */       throw new IllegalArgumentException("Session input buffer may not be null");
/*     */     }
/* 140 */     if (message == null) {
/* 141 */       throw new IllegalArgumentException("HTTP message may not be null");
/*     */     }
/* 143 */     return (HttpEntity)doDeserialize(inbuffer, message);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\entity\EntityDeserializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */