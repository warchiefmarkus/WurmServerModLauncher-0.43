/*     */ package org.apache.http.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
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
/*     */ @NotThreadSafe
/*     */ public class BasicHttpEntity
/*     */   extends AbstractHttpEntity
/*     */ {
/*     */   private InputStream content;
/*  55 */   private long length = -1L;
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/*  59 */     return this.length;
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
/*     */   public InputStream getContent() throws IllegalStateException {
/*  72 */     if (this.content == null) {
/*  73 */       throw new IllegalStateException("Content has not been provided");
/*     */     }
/*  75 */     return this.content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  85 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentLength(long len) {
/*  95 */     this.length = len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContent(InputStream instream) {
/* 105 */     this.content = instream;
/*     */   }
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/* 109 */     if (outstream == null) {
/* 110 */       throw new IllegalArgumentException("Output stream may not be null");
/*     */     }
/* 112 */     InputStream instream = getContent();
/*     */     
/*     */     try {
/* 115 */       byte[] tmp = new byte[2048]; int l;
/* 116 */       while ((l = instream.read(tmp)) != -1) {
/* 117 */         outstream.write(tmp, 0, l);
/*     */       }
/*     */     } finally {
/* 120 */       instream.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isStreaming() {
/* 125 */     return (this.content != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void consumeContent() throws IOException {
/* 137 */     if (this.content != null)
/* 138 */       this.content.close(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\entity\BasicHttpEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */