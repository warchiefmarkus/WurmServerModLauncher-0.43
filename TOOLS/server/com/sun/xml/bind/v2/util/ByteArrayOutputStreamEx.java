/*    */ package com.sun.xml.bind.v2.util;
/*    */ 
/*    */ import com.sun.xml.bind.v2.runtime.unmarshaller.Base64Data;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
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
/*    */ public final class ByteArrayOutputStreamEx
/*    */   extends ByteArrayOutputStream
/*    */ {
/*    */   public ByteArrayOutputStreamEx() {}
/*    */   
/*    */   public ByteArrayOutputStreamEx(int size) {
/* 53 */     super(size);
/*    */   }
/*    */   
/*    */   public void set(Base64Data dt, String mimeType) {
/* 57 */     dt.set(this.buf, this.count, mimeType);
/*    */   }
/*    */   
/*    */   public byte[] getBuffer() {
/* 61 */     return this.buf;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void readFrom(InputStream is) throws IOException {
/*    */     while (true) {
/* 69 */       if (this.count == this.buf.length) {
/*    */         
/* 71 */         byte[] data = new byte[this.buf.length * 2];
/* 72 */         System.arraycopy(this.buf, 0, data, 0, this.buf.length);
/* 73 */         this.buf = data;
/*    */       } 
/*    */       
/* 76 */       int sz = is.read(this.buf, this.count, this.buf.length - this.count);
/* 77 */       if (sz < 0)
/* 78 */         return;  this.count += sz;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v\\util\ByteArrayOutputStreamEx.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */