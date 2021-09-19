/*    */ package com.wurmonline.server.structures;
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
/*    */ public class PlanBridgeCheckResult
/*    */ {
/*    */   final boolean failed;
/* 29 */   String pMsg = "";
/* 30 */   String qMsg = "";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PlanBridgeCheckResult(boolean fail, String PMsg, String QMsg) {
/* 37 */     this.failed = fail;
/* 38 */     this.pMsg = PMsg;
/* 39 */     this.qMsg = QMsg;
/*    */   }
/*    */ 
/*    */   
/*    */   public PlanBridgeCheckResult(boolean fail) {
/* 44 */     this.failed = fail;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean failed() {
/* 49 */     return this.failed;
/*    */   }
/*    */ 
/*    */   
/*    */   public String pMsg() {
/* 54 */     return this.pMsg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String qMsg() {
/* 59 */     return this.qMsg;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\PlanBridgeCheckResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */