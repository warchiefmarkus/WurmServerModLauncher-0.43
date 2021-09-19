/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.io.EofSensor;
/*     */ import org.apache.http.io.HttpTransportMetrics;
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
/*     */ @Immutable
/*     */ public class LoggingSessionInputBuffer
/*     */   implements SessionInputBuffer, EofSensor
/*     */ {
/*     */   private final SessionInputBuffer in;
/*     */   private final EofSensor eofSensor;
/*     */   private final Wire wire;
/*     */   private final String charset;
/*     */   
/*     */   public LoggingSessionInputBuffer(SessionInputBuffer in, Wire wire, String charset) {
/*  67 */     this.in = in;
/*  68 */     this.eofSensor = (in instanceof EofSensor) ? (EofSensor)in : null;
/*  69 */     this.wire = wire;
/*  70 */     this.charset = (charset != null) ? charset : Consts.ASCII.name();
/*     */   }
/*     */   
/*     */   public LoggingSessionInputBuffer(SessionInputBuffer in, Wire wire) {
/*  74 */     this(in, wire, null);
/*     */   }
/*     */   
/*     */   public boolean isDataAvailable(int timeout) throws IOException {
/*  78 */     return this.in.isDataAvailable(timeout);
/*     */   }
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*  82 */     int l = this.in.read(b, off, len);
/*  83 */     if (this.wire.enabled() && l > 0) {
/*  84 */       this.wire.input(b, off, l);
/*     */     }
/*  86 */     return l;
/*     */   }
/*     */   
/*     */   public int read() throws IOException {
/*  90 */     int l = this.in.read();
/*  91 */     if (this.wire.enabled() && l != -1) {
/*  92 */       this.wire.input(l);
/*     */     }
/*  94 */     return l;
/*     */   }
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/*  98 */     int l = this.in.read(b);
/*  99 */     if (this.wire.enabled() && l > 0) {
/* 100 */       this.wire.input(b, 0, l);
/*     */     }
/* 102 */     return l;
/*     */   }
/*     */   
/*     */   public String readLine() throws IOException {
/* 106 */     String s = this.in.readLine();
/* 107 */     if (this.wire.enabled() && s != null) {
/* 108 */       String tmp = s + "\r\n";
/* 109 */       this.wire.input(tmp.getBytes(this.charset));
/*     */     } 
/* 111 */     return s;
/*     */   }
/*     */   
/*     */   public int readLine(CharArrayBuffer buffer) throws IOException {
/* 115 */     int l = this.in.readLine(buffer);
/* 116 */     if (this.wire.enabled() && l >= 0) {
/* 117 */       int pos = buffer.length() - l;
/* 118 */       String s = new String(buffer.buffer(), pos, l);
/* 119 */       String tmp = s + "\r\n";
/* 120 */       this.wire.input(tmp.getBytes(this.charset));
/*     */     } 
/* 122 */     return l;
/*     */   }
/*     */   
/*     */   public HttpTransportMetrics getMetrics() {
/* 126 */     return this.in.getMetrics();
/*     */   }
/*     */   
/*     */   public boolean isEof() {
/* 130 */     if (this.eofSensor != null) {
/* 131 */       return this.eofSensor.isEof();
/*     */     }
/* 133 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\LoggingSessionInputBuffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */