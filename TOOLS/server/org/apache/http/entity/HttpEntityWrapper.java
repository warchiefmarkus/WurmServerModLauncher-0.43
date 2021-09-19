/*     */ package org.apache.http.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class HttpEntityWrapper
/*     */   implements HttpEntity
/*     */ {
/*     */   protected HttpEntity wrappedEntity;
/*     */   
/*     */   public HttpEntityWrapper(HttpEntity wrapped) {
/*  62 */     if (wrapped == null) {
/*  63 */       throw new IllegalArgumentException("wrapped entity must not be null");
/*     */     }
/*     */     
/*  66 */     this.wrappedEntity = wrapped;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  72 */     return this.wrappedEntity.isRepeatable();
/*     */   }
/*     */   
/*     */   public boolean isChunked() {
/*  76 */     return this.wrappedEntity.isChunked();
/*     */   }
/*     */   
/*     */   public long getContentLength() {
/*  80 */     return this.wrappedEntity.getContentLength();
/*     */   }
/*     */   
/*     */   public Header getContentType() {
/*  84 */     return this.wrappedEntity.getContentType();
/*     */   }
/*     */   
/*     */   public Header getContentEncoding() {
/*  88 */     return this.wrappedEntity.getContentEncoding();
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException {
/*  93 */     return this.wrappedEntity.getContent();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/*  98 */     this.wrappedEntity.writeTo(outstream);
/*     */   }
/*     */   
/*     */   public boolean isStreaming() {
/* 102 */     return this.wrappedEntity.isStreaming();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void consumeContent() throws IOException {
/* 111 */     this.wrappedEntity.consumeContent();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\entity\HttpEntityWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */