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
/*     */ @NotThreadSafe
/*     */ public class ChunkedOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private final SessionOutputBuffer out;
/*     */   private byte[] cache;
/*  56 */   private int cachePosition = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean wroteLastChunk = false;
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
/*     */   public ChunkedOutputStream(SessionOutputBuffer out, int bufferSize) throws IOException {
/*  74 */     this.cache = new byte[bufferSize];
/*  75 */     this.out = out;
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
/*     */   public ChunkedOutputStream(SessionOutputBuffer out) throws IOException {
/*  87 */     this(out, 2048);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void flushCache() throws IOException {
/*  95 */     if (this.cachePosition > 0) {
/*  96 */       this.out.writeLine(Integer.toHexString(this.cachePosition));
/*  97 */       this.out.write(this.cache, 0, this.cachePosition);
/*  98 */       this.out.writeLine("");
/*  99 */       this.cachePosition = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void flushCacheWithAppend(byte[] bufferToAppend, int off, int len) throws IOException {
/* 108 */     this.out.writeLine(Integer.toHexString(this.cachePosition + len));
/* 109 */     this.out.write(this.cache, 0, this.cachePosition);
/* 110 */     this.out.write(bufferToAppend, off, len);
/* 111 */     this.out.writeLine("");
/* 112 */     this.cachePosition = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeClosingChunk() throws IOException {
/* 117 */     this.out.writeLine("0");
/* 118 */     this.out.writeLine("");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void finish() throws IOException {
/* 128 */     if (!this.wroteLastChunk) {
/* 129 */       flushCache();
/* 130 */       writeClosingChunk();
/* 131 */       this.wroteLastChunk = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/* 138 */     if (this.closed) {
/* 139 */       throw new IOException("Attempted write to closed stream.");
/*     */     }
/* 141 */     this.cache[this.cachePosition] = (byte)b;
/* 142 */     this.cachePosition++;
/* 143 */     if (this.cachePosition == this.cache.length) flushCache();
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/* 152 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(byte[] src, int off, int len) throws IOException {
/* 161 */     if (this.closed) {
/* 162 */       throw new IOException("Attempted write to closed stream.");
/*     */     }
/* 164 */     if (len >= this.cache.length - this.cachePosition) {
/* 165 */       flushCacheWithAppend(src, off, len);
/*     */     } else {
/* 167 */       System.arraycopy(src, off, this.cache, this.cachePosition, len);
/* 168 */       this.cachePosition += len;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 177 */     flushCache();
/* 178 */     this.out.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 186 */     if (!this.closed) {
/* 187 */       this.closed = true;
/* 188 */       finish();
/* 189 */       this.out.flush();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\io\ChunkedOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */