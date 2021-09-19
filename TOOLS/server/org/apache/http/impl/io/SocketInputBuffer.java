/*     */ package org.apache.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.io.EofSensor;
/*     */ import org.apache.http.params.HttpParams;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class SocketInputBuffer
/*     */   extends AbstractSessionInputBuffer
/*     */   implements EofSensor
/*     */ {
/*     */   private final Socket socket;
/*     */   private boolean eof;
/*     */   
/*     */   public SocketInputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
/*  74 */     if (socket == null) {
/*  75 */       throw new IllegalArgumentException("Socket may not be null");
/*     */     }
/*  77 */     this.socket = socket;
/*  78 */     this.eof = false;
/*  79 */     if (buffersize < 0) {
/*  80 */       buffersize = socket.getReceiveBufferSize();
/*     */     }
/*  82 */     if (buffersize < 1024) {
/*  83 */       buffersize = 1024;
/*     */     }
/*  85 */     init(socket.getInputStream(), buffersize, params);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int fillBuffer() throws IOException {
/*  90 */     int i = super.fillBuffer();
/*  91 */     this.eof = (i == -1);
/*  92 */     return i;
/*     */   }
/*     */   
/*     */   public boolean isDataAvailable(int timeout) throws IOException {
/*  96 */     boolean result = hasBufferedData();
/*  97 */     if (!result) {
/*  98 */       int oldtimeout = this.socket.getSoTimeout();
/*     */       try {
/* 100 */         this.socket.setSoTimeout(timeout);
/* 101 */         fillBuffer();
/* 102 */         result = hasBufferedData();
/* 103 */       } catch (SocketTimeoutException ex) {
/* 104 */         throw ex;
/*     */       } finally {
/* 106 */         this.socket.setSoTimeout(oldtimeout);
/*     */       } 
/*     */     } 
/* 109 */     return result;
/*     */   }
/*     */   
/*     */   public boolean isEof() {
/* 113 */     return this.eof;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\io\SocketInputBuffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */