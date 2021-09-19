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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum DLNAOperations
/*    */ {
/* 31 */   NONE(0),
/* 32 */   RANGE(1),
/* 33 */   TIMESEEK(16);
/*    */   
/*    */   private int code;
/*    */   
/*    */   DLNAOperations(int code) {
/* 38 */     this.code = code;
/*    */   }
/*    */   
/*    */   public int getCode() {
/* 42 */     return this.code;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\DLNAOperations.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */