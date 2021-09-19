/*    */ package org.apache.http.impl.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.io.BufferInfo;
/*    */ import org.apache.http.io.SessionInputBuffer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @NotThreadSafe
/*    */ public class IdentityInputStream
/*    */   extends InputStream
/*    */ {
/*    */   private final SessionInputBuffer in;
/*    */   private boolean closed = false;
/*    */   
/*    */   public IdentityInputStream(SessionInputBuffer in) {
/* 63 */     if (in == null) {
/* 64 */       throw new IllegalArgumentException("Session input buffer may not be null");
/*    */     }
/* 66 */     this.in = in;
/*    */   }
/*    */ 
/*    */   
/*    */   public int available() throws IOException {
/* 71 */     if (this.in instanceof BufferInfo) {
/* 72 */       return ((BufferInfo)this.in).length();
/*    */     }
/* 74 */     return 0;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 80 */     this.closed = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int read() throws IOException {
/* 85 */     if (this.closed) {
/* 86 */       return -1;
/*    */     }
/* 88 */     return this.in.read();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int read(byte[] b, int off, int len) throws IOException {
/* 94 */     if (this.closed) {
/* 95 */       return -1;
/*    */     }
/* 97 */     return this.in.read(b, off, len);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\io\IdentityInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */