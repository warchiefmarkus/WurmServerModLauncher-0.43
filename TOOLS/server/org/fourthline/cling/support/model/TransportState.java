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
/*    */ public enum TransportState
/*    */ {
/* 23 */   STOPPED,
/* 24 */   PLAYING,
/* 25 */   TRANSITIONING,
/* 26 */   PAUSED_PLAYBACK,
/* 27 */   PAUSED_RECORDING,
/* 28 */   RECORDING,
/* 29 */   NO_MEDIA_PRESENT,
/* 30 */   CUSTOM;
/*    */   
/*    */   String value;
/*    */   
/*    */   TransportState() {
/* 35 */     this.value = name();
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 39 */     return this.value;
/*    */   }
/*    */   
/*    */   public TransportState setValue(String value) {
/* 43 */     this.value = value;
/* 44 */     return this;
/*    */   }
/*    */   
/*    */   public static TransportState valueOrCustomOf(String s) {
/*    */     try {
/* 49 */       return valueOf(s);
/* 50 */     } catch (IllegalArgumentException ex) {
/* 51 */       return CUSTOM.setValue(s);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\TransportState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */