/*    */ package org.apache.http.entity;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
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
/*    */ public class EntityTemplate
/*    */   extends AbstractHttpEntity
/*    */ {
/*    */   private final ContentProducer contentproducer;
/*    */   
/*    */   public EntityTemplate(ContentProducer contentproducer) {
/* 48 */     if (contentproducer == null) {
/* 49 */       throw new IllegalArgumentException("Content producer may not be null");
/*    */     }
/* 51 */     this.contentproducer = contentproducer;
/*    */   }
/*    */   
/*    */   public long getContentLength() {
/* 55 */     return -1L;
/*    */   }
/*    */   
/*    */   public InputStream getContent() throws IOException {
/* 59 */     ByteArrayOutputStream buf = new ByteArrayOutputStream();
/* 60 */     writeTo(buf);
/* 61 */     return new ByteArrayInputStream(buf.toByteArray());
/*    */   }
/*    */   
/*    */   public boolean isRepeatable() {
/* 65 */     return true;
/*    */   }
/*    */   
/*    */   public void writeTo(OutputStream outstream) throws IOException {
/* 69 */     if (outstream == null) {
/* 70 */       throw new IllegalArgumentException("Output stream may not be null");
/*    */     }
/* 72 */     this.contentproducer.writeTo(outstream);
/*    */   }
/*    */   
/*    */   public boolean isStreaming() {
/* 76 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\entity\EntityTemplate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */