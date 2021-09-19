/*     */ package org.apache.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.io.BufferInfo;
/*     */ import org.apache.http.io.HttpTransportMetrics;
/*     */ import org.apache.http.io.SessionInputBuffer;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.params.HttpProtocolParams;
/*     */ import org.apache.http.util.ByteArrayBuffer;
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
/*     */ @NotThreadSafe
/*     */ public abstract class AbstractSessionInputBuffer
/*     */   implements SessionInputBuffer, BufferInfo
/*     */ {
/*  72 */   private static final Charset ASCII = Charset.forName("US-ASCII");
/*     */   
/*     */   private InputStream instream;
/*     */   
/*     */   private byte[] buffer;
/*     */   private int bufferpos;
/*     */   private int bufferlen;
/*  79 */   private ByteArrayBuffer linebuffer = null;
/*     */   
/*     */   private Charset charset;
/*     */   private CharsetDecoder decoder;
/*     */   private CharBuffer cbuf;
/*     */   private boolean ascii = true;
/*  85 */   private int maxLineLen = -1;
/*  86 */   private int minChunkLimit = 512;
/*     */ 
/*     */ 
/*     */   
/*     */   private HttpTransportMetricsImpl metrics;
/*     */ 
/*     */   
/*     */   private CodingErrorAction onMalformedInputAction;
/*     */ 
/*     */   
/*     */   private CodingErrorAction onUnMappableInputAction;
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init(InputStream instream, int buffersize, HttpParams params) {
/* 101 */     if (instream == null) {
/* 102 */       throw new IllegalArgumentException("Input stream may not be null");
/*     */     }
/* 104 */     if (buffersize <= 0) {
/* 105 */       throw new IllegalArgumentException("Buffer size may not be negative or zero");
/*     */     }
/* 107 */     if (params == null) {
/* 108 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 110 */     this.instream = instream;
/* 111 */     this.buffer = new byte[buffersize];
/* 112 */     this.bufferpos = 0;
/* 113 */     this.bufferlen = 0;
/* 114 */     this.linebuffer = new ByteArrayBuffer(buffersize);
/* 115 */     this.charset = Charset.forName(HttpProtocolParams.getHttpElementCharset(params));
/* 116 */     this.ascii = this.charset.equals(ASCII);
/* 117 */     this.decoder = null;
/* 118 */     this.maxLineLen = params.getIntParameter("http.connection.max-line-length", -1);
/* 119 */     this.minChunkLimit = params.getIntParameter("http.connection.min-chunk-limit", 512);
/* 120 */     this.metrics = createTransportMetrics();
/* 121 */     this.onMalformedInputAction = HttpProtocolParams.getMalformedInputAction(params);
/* 122 */     this.onUnMappableInputAction = HttpProtocolParams.getUnmappableInputAction(params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpTransportMetricsImpl createTransportMetrics() {
/* 129 */     return new HttpTransportMetricsImpl();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 136 */     return this.buffer.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 143 */     return this.bufferlen - this.bufferpos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int available() {
/* 150 */     return capacity() - length();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int fillBuffer() throws IOException {
/* 155 */     if (this.bufferpos > 0) {
/* 156 */       int i = this.bufferlen - this.bufferpos;
/* 157 */       if (i > 0) {
/* 158 */         System.arraycopy(this.buffer, this.bufferpos, this.buffer, 0, i);
/*     */       }
/* 160 */       this.bufferpos = 0;
/* 161 */       this.bufferlen = i;
/*     */     } 
/*     */     
/* 164 */     int off = this.bufferlen;
/* 165 */     int len = this.buffer.length - off;
/* 166 */     int l = this.instream.read(this.buffer, off, len);
/* 167 */     if (l == -1) {
/* 168 */       return -1;
/*     */     }
/* 170 */     this.bufferlen = off + l;
/* 171 */     this.metrics.incrementBytesTransferred(l);
/* 172 */     return l;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasBufferedData() {
/* 177 */     return (this.bufferpos < this.bufferlen);
/*     */   }
/*     */   
/*     */   public int read() throws IOException {
/* 181 */     int noRead = 0;
/* 182 */     while (!hasBufferedData()) {
/* 183 */       noRead = fillBuffer();
/* 184 */       if (noRead == -1) {
/* 185 */         return -1;
/*     */       }
/*     */     } 
/* 188 */     return this.buffer[this.bufferpos++] & 0xFF;
/*     */   }
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 192 */     if (b == null) {
/* 193 */       return 0;
/*     */     }
/* 195 */     if (hasBufferedData()) {
/* 196 */       int i = Math.min(len, this.bufferlen - this.bufferpos);
/* 197 */       System.arraycopy(this.buffer, this.bufferpos, b, off, i);
/* 198 */       this.bufferpos += i;
/* 199 */       return i;
/*     */     } 
/*     */ 
/*     */     
/* 203 */     if (len > this.minChunkLimit) {
/* 204 */       int read = this.instream.read(b, off, len);
/* 205 */       if (read > 0) {
/* 206 */         this.metrics.incrementBytesTransferred(read);
/*     */       }
/* 208 */       return read;
/*     */     } 
/*     */     
/* 211 */     while (!hasBufferedData()) {
/* 212 */       int noRead = fillBuffer();
/* 213 */       if (noRead == -1) {
/* 214 */         return -1;
/*     */       }
/*     */     } 
/* 217 */     int chunk = Math.min(len, this.bufferlen - this.bufferpos);
/* 218 */     System.arraycopy(this.buffer, this.bufferpos, b, off, chunk);
/* 219 */     this.bufferpos += chunk;
/* 220 */     return chunk;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/* 225 */     if (b == null) {
/* 226 */       return 0;
/*     */     }
/* 228 */     return read(b, 0, b.length);
/*     */   }
/*     */   
/*     */   private int locateLF() {
/* 232 */     for (int i = this.bufferpos; i < this.bufferlen; i++) {
/* 233 */       if (this.buffer[i] == 10) {
/* 234 */         return i;
/*     */       }
/*     */     } 
/* 237 */     return -1;
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
/*     */   public int readLine(CharArrayBuffer charbuffer) throws IOException {
/* 256 */     if (charbuffer == null) {
/* 257 */       throw new IllegalArgumentException("Char array buffer may not be null");
/*     */     }
/* 259 */     int noRead = 0;
/* 260 */     boolean retry = true;
/* 261 */     while (retry) {
/*     */       
/* 263 */       int i = locateLF();
/* 264 */       if (i != -1) {
/*     */         
/* 266 */         if (this.linebuffer.isEmpty())
/*     */         {
/* 268 */           return lineFromReadBuffer(charbuffer, i);
/*     */         }
/* 270 */         retry = false;
/* 271 */         int len = i + 1 - this.bufferpos;
/* 272 */         this.linebuffer.append(this.buffer, this.bufferpos, len);
/* 273 */         this.bufferpos = i + 1;
/*     */       } else {
/*     */         
/* 276 */         if (hasBufferedData()) {
/* 277 */           int len = this.bufferlen - this.bufferpos;
/* 278 */           this.linebuffer.append(this.buffer, this.bufferpos, len);
/* 279 */           this.bufferpos = this.bufferlen;
/*     */         } 
/* 281 */         noRead = fillBuffer();
/* 282 */         if (noRead == -1) {
/* 283 */           retry = false;
/*     */         }
/*     */       } 
/* 286 */       if (this.maxLineLen > 0 && this.linebuffer.length() >= this.maxLineLen) {
/* 287 */         throw new IOException("Maximum line length limit exceeded");
/*     */       }
/*     */     } 
/* 290 */     if (noRead == -1 && this.linebuffer.isEmpty())
/*     */     {
/* 292 */       return -1;
/*     */     }
/* 294 */     return lineFromLineBuffer(charbuffer);
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
/*     */   private int lineFromLineBuffer(CharArrayBuffer charbuffer) throws IOException {
/* 313 */     int len = this.linebuffer.length();
/* 314 */     if (len > 0) {
/* 315 */       if (this.linebuffer.byteAt(len - 1) == 10) {
/* 316 */         len--;
/*     */       }
/*     */       
/* 319 */       if (len > 0 && 
/* 320 */         this.linebuffer.byteAt(len - 1) == 13) {
/* 321 */         len--;
/*     */       }
/*     */     } 
/*     */     
/* 325 */     if (this.ascii) {
/* 326 */       charbuffer.append(this.linebuffer, 0, len);
/*     */     } else {
/* 328 */       ByteBuffer bbuf = ByteBuffer.wrap(this.linebuffer.buffer(), 0, len);
/* 329 */       len = appendDecoded(charbuffer, bbuf);
/*     */     } 
/* 331 */     this.linebuffer.clear();
/* 332 */     return len;
/*     */   }
/*     */ 
/*     */   
/*     */   private int lineFromReadBuffer(CharArrayBuffer charbuffer, int pos) throws IOException {
/* 337 */     int off = this.bufferpos;
/*     */     
/* 339 */     this.bufferpos = pos + 1;
/* 340 */     if (pos > off && this.buffer[pos - 1] == 13)
/*     */     {
/* 342 */       pos--;
/*     */     }
/* 344 */     int len = pos - off;
/* 345 */     if (this.ascii) {
/* 346 */       charbuffer.append(this.buffer, off, len);
/*     */     } else {
/* 348 */       ByteBuffer bbuf = ByteBuffer.wrap(this.buffer, off, len);
/* 349 */       len = appendDecoded(charbuffer, bbuf);
/*     */     } 
/* 351 */     return len;
/*     */   }
/*     */ 
/*     */   
/*     */   private int appendDecoded(CharArrayBuffer charbuffer, ByteBuffer bbuf) throws IOException {
/* 356 */     if (!bbuf.hasRemaining()) {
/* 357 */       return 0;
/*     */     }
/* 359 */     if (this.decoder == null) {
/* 360 */       this.decoder = this.charset.newDecoder();
/* 361 */       this.decoder.onMalformedInput(this.onMalformedInputAction);
/* 362 */       this.decoder.onUnmappableCharacter(this.onUnMappableInputAction);
/*     */     } 
/* 364 */     if (this.cbuf == null) {
/* 365 */       this.cbuf = CharBuffer.allocate(1024);
/*     */     }
/* 367 */     this.decoder.reset();
/* 368 */     int len = 0;
/* 369 */     while (bbuf.hasRemaining()) {
/* 370 */       CoderResult coderResult = this.decoder.decode(bbuf, this.cbuf, true);
/* 371 */       len += handleDecodingResult(coderResult, charbuffer, bbuf);
/*     */     } 
/* 373 */     CoderResult result = this.decoder.flush(this.cbuf);
/* 374 */     len += handleDecodingResult(result, charbuffer, bbuf);
/* 375 */     this.cbuf.clear();
/* 376 */     return len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int handleDecodingResult(CoderResult result, CharArrayBuffer charbuffer, ByteBuffer bbuf) throws IOException {
/* 383 */     if (result.isError()) {
/* 384 */       result.throwException();
/*     */     }
/* 386 */     this.cbuf.flip();
/* 387 */     int len = this.cbuf.remaining();
/* 388 */     while (this.cbuf.hasRemaining()) {
/* 389 */       charbuffer.append(this.cbuf.get());
/*     */     }
/* 391 */     this.cbuf.compact();
/* 392 */     return len;
/*     */   }
/*     */   
/*     */   public String readLine() throws IOException {
/* 396 */     CharArrayBuffer charbuffer = new CharArrayBuffer(64);
/* 397 */     int l = readLine(charbuffer);
/* 398 */     if (l != -1) {
/* 399 */       return charbuffer.toString();
/*     */     }
/* 401 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpTransportMetrics getMetrics() {
/* 406 */     return this.metrics;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\io\AbstractSessionInputBuffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */