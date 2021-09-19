/*     */ package org.apache.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.http.ConnectionClosedException;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.io.BufferInfo;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class ContentLengthInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private static final int BUFFER_SIZE = 2048;
/*     */   private long contentLength;
/*  65 */   private long pos = 0L;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean closed = false;
/*     */ 
/*     */ 
/*     */   
/*  73 */   private SessionInputBuffer in = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentLengthInputStream(SessionInputBuffer in, long contentLength) {
/*  85 */     if (in == null) {
/*  86 */       throw new IllegalArgumentException("Input stream may not be null");
/*     */     }
/*  88 */     if (contentLength < 0L) {
/*  89 */       throw new IllegalArgumentException("Content length may not be negative");
/*     */     }
/*  91 */     this.in = in;
/*  92 */     this.contentLength = contentLength;
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
/*     */   public void close() throws IOException {
/* 104 */     if (!this.closed) {
/*     */       try {
/* 106 */         if (this.pos < this.contentLength) {
/* 107 */           byte[] buffer = new byte[2048];
/* 108 */           while (read(buffer) >= 0);
/*     */         }
/*     */       
/*     */       }
/*     */       finally {
/*     */         
/* 114 */         this.closed = true;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/* 121 */     if (this.in instanceof BufferInfo) {
/* 122 */       int len = ((BufferInfo)this.in).length();
/* 123 */       return Math.min(len, (int)(this.contentLength - this.pos));
/*     */     } 
/* 125 */     return 0;
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
/*     */   public int read() throws IOException {
/* 137 */     if (this.closed) {
/* 138 */       throw new IOException("Attempted read from closed stream.");
/*     */     }
/*     */     
/* 141 */     if (this.pos >= this.contentLength) {
/* 142 */       return -1;
/*     */     }
/* 144 */     int b = this.in.read();
/* 145 */     if (b == -1) {
/* 146 */       if (this.pos < this.contentLength) {
/* 147 */         throw new ConnectionClosedException("Premature end of Content-Length delimited message body (expected: " + this.contentLength + "; received: " + this.pos);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 152 */       this.pos++;
/*     */     } 
/* 154 */     return b;
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
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 171 */     if (this.closed) {
/* 172 */       throw new IOException("Attempted read from closed stream.");
/*     */     }
/*     */     
/* 175 */     if (this.pos >= this.contentLength) {
/* 176 */       return -1;
/*     */     }
/*     */     
/* 179 */     if (this.pos + len > this.contentLength) {
/* 180 */       len = (int)(this.contentLength - this.pos);
/*     */     }
/* 182 */     int count = this.in.read(b, off, len);
/* 183 */     if (count == -1 && this.pos < this.contentLength) {
/* 184 */       throw new ConnectionClosedException("Premature end of Content-Length delimited message body (expected: " + this.contentLength + "; received: " + this.pos);
/*     */     }
/*     */ 
/*     */     
/* 188 */     if (count > 0) {
/* 189 */       this.pos += count;
/*     */     }
/* 191 */     return count;
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
/*     */   public int read(byte[] b) throws IOException {
/* 204 */     return read(b, 0, b.length);
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
/*     */   public long skip(long n) throws IOException {
/* 217 */     if (n <= 0L) {
/* 218 */       return 0L;
/*     */     }
/* 220 */     byte[] buffer = new byte[2048];
/*     */ 
/*     */     
/* 223 */     long remaining = Math.min(n, this.contentLength - this.pos);
/*     */     
/* 225 */     long count = 0L;
/* 226 */     while (remaining > 0L) {
/* 227 */       int l = read(buffer, 0, (int)Math.min(2048L, remaining));
/* 228 */       if (l == -1) {
/*     */         break;
/*     */       }
/* 231 */       count += l;
/* 232 */       remaining -= l;
/*     */     } 
/* 234 */     return count;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\io\ContentLengthInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */