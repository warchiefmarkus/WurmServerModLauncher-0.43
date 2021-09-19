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
/*     */ @NotThreadSafe
/*     */ public class IdentityOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private final SessionOutputBuffer out;
/*     */   private boolean closed = false;
/*     */   
/*     */   public IdentityOutputStream(SessionOutputBuffer out) {
/*  61 */     if (out == null) {
/*  62 */       throw new IllegalArgumentException("Session output buffer may not be null");
/*     */     }
/*  64 */     this.out = out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  74 */     if (!this.closed) {
/*  75 */       this.closed = true;
/*  76 */       this.out.flush();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/*  82 */     this.out.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/*  87 */     if (this.closed) {
/*  88 */       throw new IOException("Attempted write to closed stream.");
/*     */     }
/*  90 */     this.out.write(b, off, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/*  95 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/* 100 */     if (this.closed) {
/* 101 */       throw new IOException("Attempted write to closed stream.");
/*     */     }
/* 103 */     this.out.write(b);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\io\IdentityOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */