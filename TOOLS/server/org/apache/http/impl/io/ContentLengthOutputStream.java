/*     */ package org.apache.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.http.annotation.NotThreadSafe;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class ContentLengthOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private final SessionOutputBuffer out;
/*     */   private final long contentLength;
/*  64 */   private long total = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean closed = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentLengthOutputStream(SessionOutputBuffer out, long contentLength) {
/*  81 */     if (out == null) {
/*  82 */       throw new IllegalArgumentException("Session output buffer may not be null");
/*     */     }
/*  84 */     if (contentLength < 0L) {
/*  85 */       throw new IllegalArgumentException("Content length may not be negative");
/*     */     }
/*  87 */     this.out = out;
/*  88 */     this.contentLength = contentLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  98 */     if (!this.closed) {
/*  99 */       this.closed = true;
/* 100 */       this.out.flush();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 106 */     this.out.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/* 111 */     if (this.closed) {
/* 112 */       throw new IOException("Attempted write to closed stream.");
/*     */     }
/* 114 */     if (this.total < this.contentLength) {
/* 115 */       long max = this.contentLength - this.total;
/* 116 */       if (len > max) {
/* 117 */         len = (int)max;
/*     */       }
/* 119 */       this.out.write(b, off, len);
/* 120 */       this.total += len;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/* 126 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/* 131 */     if (this.closed) {
/* 132 */       throw new IOException("Attempted write to closed stream.");
/*     */     }
/* 134 */     if (this.total < this.contentLength) {
/* 135 */       this.out.write(b);
/* 136 */       this.total++;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\io\ContentLengthOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */