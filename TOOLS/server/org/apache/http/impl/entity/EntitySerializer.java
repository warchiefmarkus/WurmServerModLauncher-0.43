/*     */ package org.apache.http.impl.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ import org.apache.http.impl.io.ChunkedOutputStream;
/*     */ import org.apache.http.impl.io.ContentLengthOutputStream;
/*     */ import org.apache.http.impl.io.IdentityOutputStream;
/*     */ import org.apache.http.io.SessionOutputBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class EntitySerializer
/*     */ {
/*     */   private final ContentLengthStrategy lenStrategy;
/*     */   
/*     */   public EntitySerializer(ContentLengthStrategy lenStrategy) {
/*  65 */     if (lenStrategy == null) {
/*  66 */       throw new IllegalArgumentException("Content length strategy may not be null");
/*     */     }
/*  68 */     this.lenStrategy = lenStrategy;
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
/*     */   protected OutputStream doSerialize(SessionOutputBuffer outbuffer, HttpMessage message) throws HttpException, IOException {
/*  88 */     long len = this.lenStrategy.determineLength(message);
/*  89 */     if (len == -2L)
/*  90 */       return (OutputStream)new ChunkedOutputStream(outbuffer); 
/*  91 */     if (len == -1L) {
/*  92 */       return (OutputStream)new IdentityOutputStream(outbuffer);
/*     */     }
/*  94 */     return (OutputStream)new ContentLengthOutputStream(outbuffer, len);
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
/*     */   public void serialize(SessionOutputBuffer outbuffer, HttpMessage message, HttpEntity entity) throws HttpException, IOException {
/* 112 */     if (outbuffer == null) {
/* 113 */       throw new IllegalArgumentException("Session output buffer may not be null");
/*     */     }
/* 115 */     if (message == null) {
/* 116 */       throw new IllegalArgumentException("HTTP message may not be null");
/*     */     }
/* 118 */     if (entity == null) {
/* 119 */       throw new IllegalArgumentException("HTTP entity may not be null");
/*     */     }
/* 121 */     OutputStream outstream = doSerialize(outbuffer, message);
/* 122 */     entity.writeTo(outstream);
/* 123 */     outstream.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\entity\EntitySerializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */