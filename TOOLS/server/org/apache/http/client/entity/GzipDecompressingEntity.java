/*    */ package org.apache.http.client.entity;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.util.zip.GZIPInputStream;
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HttpEntity;
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
/*    */ public class GzipDecompressingEntity
/*    */   extends DecompressingEntity
/*    */ {
/*    */   public GzipDecompressingEntity(HttpEntity entity) {
/* 51 */     super(entity);
/*    */   }
/*    */ 
/*    */   
/*    */   InputStream decorate(InputStream wrapped) throws IOException {
/* 56 */     return new GZIPInputStream(wrapped);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Header getContentEncoding() {
/* 66 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getContentLength() {
/* 76 */     return -1L;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\client\entity\GzipDecompressingEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */