/*    */ package org.fourthline.cling.support.model.dlna.types;
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
/*    */ public class CodedDataBuffer
/*    */ {
/*    */   private Long size;
/*    */   private TransferMechanism tranfer;
/*    */   
/*    */   public enum TransferMechanism
/*    */   {
/* 24 */     IMMEDIATELY,
/* 25 */     TIMESTAMP,
/* 26 */     OTHER;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CodedDataBuffer(Long size, TransferMechanism transfer) {
/* 33 */     this.size = size;
/* 34 */     this.tranfer = transfer;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Long getSize() {
/* 41 */     return this.size;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TransferMechanism getTranfer() {
/* 48 */     return this.tranfer;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\types\CodedDataBuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */