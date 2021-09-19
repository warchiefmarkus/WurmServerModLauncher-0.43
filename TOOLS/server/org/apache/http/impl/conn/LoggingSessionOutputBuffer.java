/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.io.HttpTransportMetrics;
/*     */ import org.apache.http.io.SessionOutputBuffer;
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
/*     */ @Immutable
/*     */ public class LoggingSessionOutputBuffer
/*     */   implements SessionOutputBuffer
/*     */ {
/*     */   private final SessionOutputBuffer out;
/*     */   private final Wire wire;
/*     */   private final String charset;
/*     */   
/*     */   public LoggingSessionOutputBuffer(SessionOutputBuffer out, Wire wire, String charset) {
/*  64 */     this.out = out;
/*  65 */     this.wire = wire;
/*  66 */     this.charset = (charset != null) ? charset : Consts.ASCII.name();
/*     */   }
/*     */   
/*     */   public LoggingSessionOutputBuffer(SessionOutputBuffer out, Wire wire) {
/*  70 */     this(out, wire, null);
/*     */   }
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/*  74 */     this.out.write(b, off, len);
/*  75 */     if (this.wire.enabled()) {
/*  76 */       this.wire.output(b, off, len);
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(int b) throws IOException {
/*  81 */     this.out.write(b);
/*  82 */     if (this.wire.enabled()) {
/*  83 */       this.wire.output(b);
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/*  88 */     this.out.write(b);
/*  89 */     if (this.wire.enabled()) {
/*  90 */       this.wire.output(b);
/*     */     }
/*     */   }
/*     */   
/*     */   public void flush() throws IOException {
/*  95 */     this.out.flush();
/*     */   }
/*     */   
/*     */   public void writeLine(CharArrayBuffer buffer) throws IOException {
/*  99 */     this.out.writeLine(buffer);
/* 100 */     if (this.wire.enabled()) {
/* 101 */       String s = new String(buffer.buffer(), 0, buffer.length());
/* 102 */       String tmp = s + "\r\n";
/* 103 */       this.wire.output(tmp.getBytes(this.charset));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void writeLine(String s) throws IOException {
/* 108 */     this.out.writeLine(s);
/* 109 */     if (this.wire.enabled()) {
/* 110 */       String tmp = s + "\r\n";
/* 111 */       this.wire.output(tmp.getBytes(this.charset));
/*     */     } 
/*     */   }
/*     */   
/*     */   public HttpTransportMetrics getMetrics() {
/* 116 */     return this.out.getMetrics();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\LoggingSessionOutputBuffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */