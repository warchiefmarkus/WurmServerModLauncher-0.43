/*     */ package org.apache.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.MalformedChunkCodingException;
/*     */ import org.apache.http.TruncatedChunkException;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.io.BufferInfo;
/*     */ import org.apache.http.io.SessionInputBuffer;
/*     */ import org.apache.http.util.CharArrayBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class ChunkedInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private static final int CHUNK_LEN = 1;
/*     */   private static final int CHUNK_DATA = 2;
/*     */   private static final int CHUNK_CRLF = 3;
/*     */   private static final int BUFFER_SIZE = 2048;
/*     */   private final SessionInputBuffer in;
/*     */   private final CharArrayBuffer buffer;
/*     */   private int state;
/*     */   private int chunkSize;
/*     */   private int pos;
/*     */   private boolean eof = false;
/*     */   private boolean closed = false;
/*  86 */   private Header[] footers = new Header[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkedInputStream(SessionInputBuffer in) {
/*  95 */     if (in == null) {
/*  96 */       throw new IllegalArgumentException("Session input buffer may not be null");
/*     */     }
/*  98 */     this.in = in;
/*  99 */     this.pos = 0;
/* 100 */     this.buffer = new CharArrayBuffer(16);
/* 101 */     this.state = 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/* 106 */     if (this.in instanceof BufferInfo) {
/* 107 */       int len = ((BufferInfo)this.in).length();
/* 108 */       return Math.min(len, this.chunkSize - this.pos);
/*     */     } 
/* 110 */     return 0;
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
/*     */   public int read() throws IOException {
/* 128 */     if (this.closed) {
/* 129 */       throw new IOException("Attempted read from closed stream.");
/*     */     }
/* 131 */     if (this.eof) {
/* 132 */       return -1;
/*     */     }
/* 134 */     if (this.state != 2) {
/* 135 */       nextChunk();
/* 136 */       if (this.eof) {
/* 137 */         return -1;
/*     */       }
/*     */     } 
/* 140 */     int b = this.in.read();
/* 141 */     if (b != -1) {
/* 142 */       this.pos++;
/* 143 */       if (this.pos >= this.chunkSize) {
/* 144 */         this.state = 3;
/*     */       }
/*     */     } 
/* 147 */     return b;
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
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 163 */     if (this.closed) {
/* 164 */       throw new IOException("Attempted read from closed stream.");
/*     */     }
/*     */     
/* 167 */     if (this.eof) {
/* 168 */       return -1;
/*     */     }
/* 170 */     if (this.state != 2) {
/* 171 */       nextChunk();
/* 172 */       if (this.eof) {
/* 173 */         return -1;
/*     */       }
/*     */     } 
/* 176 */     len = Math.min(len, this.chunkSize - this.pos);
/* 177 */     int bytesRead = this.in.read(b, off, len);
/* 178 */     if (bytesRead != -1) {
/* 179 */       this.pos += bytesRead;
/* 180 */       if (this.pos >= this.chunkSize) {
/* 181 */         this.state = 3;
/*     */       }
/* 183 */       return bytesRead;
/*     */     } 
/* 185 */     this.eof = true;
/* 186 */     throw new TruncatedChunkException("Truncated chunk ( expected size: " + this.chunkSize + "; actual size: " + this.pos + ")");
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
/*     */   public int read(byte[] b) throws IOException {
/* 201 */     return read(b, 0, b.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void nextChunk() throws IOException {
/* 209 */     this.chunkSize = getChunkSize();
/* 210 */     if (this.chunkSize < 0) {
/* 211 */       throw new MalformedChunkCodingException("Negative chunk size");
/*     */     }
/* 213 */     this.state = 2;
/* 214 */     this.pos = 0;
/* 215 */     if (this.chunkSize == 0) {
/* 216 */       this.eof = true;
/* 217 */       parseTrailerHeaders();
/*     */     } 
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
/*     */   private int getChunkSize() throws IOException {
/* 235 */     int i, separator, st = this.state;
/* 236 */     switch (st) {
/*     */       case 3:
/* 238 */         this.buffer.clear();
/* 239 */         i = this.in.readLine(this.buffer);
/* 240 */         if (i == -1) {
/* 241 */           return 0;
/*     */         }
/* 243 */         if (!this.buffer.isEmpty()) {
/* 244 */           throw new MalformedChunkCodingException("Unexpected content at the end of chunk");
/*     */         }
/*     */         
/* 247 */         this.state = 1;
/*     */       
/*     */       case 1:
/* 250 */         this.buffer.clear();
/* 251 */         i = this.in.readLine(this.buffer);
/* 252 */         if (i == -1) {
/* 253 */           return 0;
/*     */         }
/* 255 */         separator = this.buffer.indexOf(59);
/* 256 */         if (separator < 0) {
/* 257 */           separator = this.buffer.length();
/*     */         }
/*     */         try {
/* 260 */           return Integer.parseInt(this.buffer.substringTrimmed(0, separator), 16);
/* 261 */         } catch (NumberFormatException e) {
/* 262 */           throw new MalformedChunkCodingException("Bad chunk header");
/*     */         } 
/*     */     } 
/* 265 */     throw new IllegalStateException("Inconsistent codec state");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseTrailerHeaders() throws IOException {
/*     */     try {
/* 275 */       this.footers = AbstractMessageParser.parseHeaders(this.in, -1, -1, null);
/*     */     }
/* 277 */     catch (HttpException ex) {
/* 278 */       MalformedChunkCodingException malformedChunkCodingException = new MalformedChunkCodingException("Invalid footer: " + ex.getMessage());
/*     */       
/* 280 */       malformedChunkCodingException.initCause((Throwable)ex);
/* 281 */       throw malformedChunkCodingException;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 293 */     if (!this.closed) {
/*     */       try {
/* 295 */         if (!this.eof) {
/*     */           
/* 297 */           byte[] buffer = new byte[2048];
/* 298 */           while (read(buffer) >= 0);
/*     */         } 
/*     */       } finally {
/*     */         
/* 302 */         this.eof = true;
/* 303 */         this.closed = true;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public Header[] getFooters() {
/* 309 */     return (Header[])this.footers.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\io\ChunkedInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */