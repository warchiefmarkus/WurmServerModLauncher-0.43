/*    */ package org.fourthline.cling.support.model;
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
/*    */ public enum TransportStatus
/*    */ {
/* 23 */   OK,
/* 24 */   ERROR_OCCURRED,
/* 25 */   CUSTOM;
/*    */   
/*    */   String value;
/*    */   
/*    */   TransportStatus() {
/* 30 */     this.value = name();
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 34 */     return this.value;
/*    */   }
/*    */   
/*    */   public TransportStatus setValue(String value) {
/* 38 */     this.value = value;
/* 39 */     return this;
/*    */   }
/*    */   
/*    */   public static TransportStatus valueOrCustomOf(String s) {
/*    */     try {
/* 44 */       return valueOf(s);
/* 45 */     } catch (IllegalArgumentException ex) {
/* 46 */       return CUSTOM.setValue(s);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\TransportStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */