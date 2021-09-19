/*     */ package org.apache.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.io.BufferInfo;
/*     */ import org.apache.http.io.HttpTransportMetrics;
/*     */ import org.apache.http.io.SessionOutputBuffer;
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
/*     */ @NotThreadSafe
/*     */ public abstract class AbstractSessionOutputBuffer
/*     */   implements SessionOutputBuffer, BufferInfo
/*     */ {
/*  71 */   private static final Charset ASCII = Charset.forName("US-ASCII");
/*  72 */   private static final byte[] CRLF = new byte[] { 13, 10 };
/*     */   
/*     */   private OutputStream outstream;
/*     */   
/*     */   private ByteArrayBuffer buffer;
/*     */   private Charset charset;
/*     */   private CharsetEncoder encoder;
/*     */   private ByteBuffer bbuf;
/*     */   private boolean ascii = true;
/*  81 */   private int minChunkLimit = 512;
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
/*     */   protected void init(OutputStream outstream, int buffersize, HttpParams params) {
/*  96 */     if (outstream == null) {
/*  97 */       throw new IllegalArgumentException("Input stream may not be null");
/*     */     }
/*  99 */     if (buffersize <= 0) {
/* 100 */       throw new IllegalArgumentException("Buffer size may not be negative or zero");
/*     */     }
/* 102 */     if (params == null) {
/* 103 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 105 */     this.outstream = outstream;
/* 106 */     this.buffer = new ByteArrayBuffer(buffersize);
/* 107 */     this.charset = Charset.forName(HttpProtocolParams.getHttpElementCharset(params));
/* 108 */     this.ascii = this.charset.equals(ASCII);
/* 109 */     this.encoder = null;
/* 110 */     this.minChunkLimit = params.getIntParameter("http.connection.min-chunk-limit", 512);
/* 111 */     this.metrics = createTransportMetrics();
/* 112 */     this.onMalformedInputAction = HttpProtocolParams.getMalformedInputAction(params);
/* 113 */     this.onUnMappableInputAction = HttpProtocolParams.getUnmappableInputAction(params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpTransportMetricsImpl createTransportMetrics() {
/* 120 */     return new HttpTransportMetricsImpl();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 127 */     return this.buffer.capacity();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 134 */     return this.buffer.length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int available() {
/* 141 */     return capacity() - length();
/*     */   }
/*     */   
/*     */   protected void flushBuffer() throws IOException {
/* 145 */     int len = this.buffer.length();
/* 146 */     if (len > 0) {
/* 147 */       this.outstream.write(this.buffer.buffer(), 0, len);
/* 148 */       this.buffer.clear();
/* 149 */       this.metrics.incrementBytesTransferred(len);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void flush() throws IOException {
/* 154 */     flushBuffer();
/* 155 */     this.outstream.flush();
/*     */   }
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/* 159 */     if (b == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 165 */     if (len > this.minChunkLimit || len > this.buffer.capacity()) {
/*     */       
/* 167 */       flushBuffer();
/*     */       
/* 169 */       this.outstream.write(b, off, len);
/* 170 */       this.metrics.incrementBytesTransferred(len);
/*     */     } else {
/*     */       
/* 173 */       int freecapacity = this.buffer.capacity() - this.buffer.length();
/* 174 */       if (len > freecapacity)
/*     */       {
/* 176 */         flushBuffer();
/*     */       }
/*     */       
/* 179 */       this.buffer.append(b, off, len);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/* 184 */     if (b == null) {
/*     */       return;
/*     */     }
/* 187 */     write(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public void write(int b) throws IOException {
/* 191 */     if (this.buffer.isFull()) {
/* 192 */       flushBuffer();
/*     */     }
/* 194 */     this.buffer.append(b);
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
/*     */   public void writeLine(String s) throws IOException {
/* 207 */     if (s == null) {
/*     */       return;
/*     */     }
/* 210 */     if (s.length() > 0) {
/* 211 */       if (this.ascii) {
/* 212 */         for (int i = 0; i < s.length(); i++) {
/* 213 */           write(s.charAt(i));
/*     */         }
/*     */       } else {
/* 216 */         CharBuffer cbuf = CharBuffer.wrap(s);
/* 217 */         writeEncoded(cbuf);
/*     */       } 
/*     */     }
/* 220 */     write(CRLF);
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
/*     */   public void writeLine(CharArrayBuffer charbuffer) throws IOException {
/* 233 */     if (charbuffer == null) {
/*     */       return;
/*     */     }
/* 236 */     if (this.ascii) {
/* 237 */       int off = 0;
/* 238 */       int remaining = charbuffer.length();
/* 239 */       while (remaining > 0) {
/* 240 */         int chunk = this.buffer.capacity() - this.buffer.length();
/* 241 */         chunk = Math.min(chunk, remaining);
/* 242 */         if (chunk > 0) {
/* 243 */           this.buffer.append(charbuffer, off, chunk);
/*     */         }
/* 245 */         if (this.buffer.isFull()) {
/* 246 */           flushBuffer();
/*     */         }
/* 248 */         off += chunk;
/* 249 */         remaining -= chunk;
/*     */       } 
/*     */     } else {
/* 252 */       CharBuffer cbuf = CharBuffer.wrap(charbuffer.buffer(), 0, charbuffer.length());
/* 253 */       writeEncoded(cbuf);
/*     */     } 
/* 255 */     write(CRLF);
/*     */   }
/*     */   
/*     */   private void writeEncoded(CharBuffer cbuf) throws IOException {
/* 259 */     if (!cbuf.hasRemaining()) {
/*     */       return;
/*     */     }
/* 262 */     if (this.encoder == null) {
/* 263 */       this.encoder = this.charset.newEncoder();
/* 264 */       this.encoder.onMalformedInput(this.onMalformedInputAction);
/* 265 */       this.encoder.onUnmappableCharacter(this.onUnMappableInputAction);
/*     */     } 
/* 267 */     if (this.bbuf == null) {
/* 268 */       this.bbuf = ByteBuffer.allocate(1024);
/*     */     }
/* 270 */     this.encoder.reset();
/* 271 */     while (cbuf.hasRemaining()) {
/* 272 */       CoderResult coderResult = this.encoder.encode(cbuf, this.bbuf, true);
/* 273 */       handleEncodingResult(coderResult);
/*     */     } 
/* 275 */     CoderResult result = this.encoder.flush(this.bbuf);
/* 276 */     handleEncodingResult(result);
/* 277 */     this.bbuf.clear();
/*     */   }
/*     */   
/*     */   private void handleEncodingResult(CoderResult result) throws IOException {
/* 281 */     if (result.isError()) {
/* 282 */       result.throwException();
/*     */     }
/* 284 */     this.bbuf.flip();
/* 285 */     while (this.bbuf.hasRemaining()) {
/* 286 */       write(this.bbuf.get());
/*     */     }
/* 288 */     this.bbuf.compact();
/*     */   }
/*     */   
/*     */   public HttpTransportMetrics getMetrics() {
/* 292 */     return this.metrics;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\io\AbstractSessionOutputBuffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */