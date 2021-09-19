/*    */ package org.fourthline.cling.support.model.dlna;
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
/*    */ public enum DLNAConversionIndicator
/*    */ {
/* 27 */   NONE(0),
/* 28 */   TRANSCODED(1);
/*    */   
/*    */   private int code;
/*    */   
/*    */   DLNAConversionIndicator(int code) {
/* 33 */     this.code = code;
/*    */   }
/*    */   
/*    */   public int getCode() {
/* 37 */     return this.code;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\DLNAConversionIndicator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */