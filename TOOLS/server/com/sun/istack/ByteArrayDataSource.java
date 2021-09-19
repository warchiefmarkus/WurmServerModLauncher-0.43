/*    */ package com.sun.istack;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import javax.activation.DataSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ByteArrayDataSource
/*    */   implements DataSource
/*    */ {
/*    */   private final String contentType;
/*    */   private final byte[] buf;
/*    */   private final int len;
/*    */   
/*    */   public ByteArrayDataSource(byte[] buf, String contentType) {
/* 20 */     this(buf, buf.length, contentType);
/*    */   }
/*    */   public ByteArrayDataSource(byte[] buf, int length, String contentType) {
/* 23 */     this.buf = buf;
/* 24 */     this.len = length;
/* 25 */     this.contentType = contentType;
/*    */   }
/*    */   
/*    */   public String getContentType() {
/* 29 */     if (this.contentType == null)
/* 30 */       return "application/octet-stream"; 
/* 31 */     return this.contentType;
/*    */   }
/*    */   
/*    */   public InputStream getInputStream() {
/* 35 */     return new ByteArrayInputStream(this.buf, 0, this.len);
/*    */   }
/*    */   
/*    */   public String getName() {
/* 39 */     return null;
/*    */   }
/*    */   
/*    */   public OutputStream getOutputStream() {
/* 43 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\istack\ByteArrayDataSource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */