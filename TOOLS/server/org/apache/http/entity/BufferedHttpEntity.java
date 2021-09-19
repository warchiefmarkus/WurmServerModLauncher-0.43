/*     */ package org.apache.http.entity;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.util.EntityUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class BufferedHttpEntity
/*     */   extends HttpEntityWrapper
/*     */ {
/*     */   private final byte[] buffer;
/*     */   
/*     */   public BufferedHttpEntity(HttpEntity entity) throws IOException {
/*  60 */     super(entity);
/*  61 */     if (!entity.isRepeatable() || entity.getContentLength() < 0L) {
/*  62 */       this.buffer = EntityUtils.toByteArray(entity);
/*     */     } else {
/*  64 */       this.buffer = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/*  70 */     if (this.buffer != null) {
/*  71 */       return this.buffer.length;
/*     */     }
/*  73 */     return this.wrappedEntity.getContentLength();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException {
/*  79 */     if (this.buffer != null) {
/*  80 */       return new ByteArrayInputStream(this.buffer);
/*     */     }
/*  82 */     return this.wrappedEntity.getContent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChunked() {
/*  93 */     return (this.buffer == null && this.wrappedEntity.isChunked());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/* 103 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/* 109 */     if (outstream == null) {
/* 110 */       throw new IllegalArgumentException("Output stream may not be null");
/*     */     }
/* 112 */     if (this.buffer != null) {
/* 113 */       outstream.write(this.buffer);
/*     */     } else {
/* 115 */       this.wrappedEntity.writeTo(outstream);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStreaming() {
/* 123 */     return (this.buffer == null && this.wrappedEntity.isStreaming());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\entity\BufferedHttpEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */