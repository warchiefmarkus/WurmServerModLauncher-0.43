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
/*    */ 
/*    */ 
/*    */ public enum SeekMode
/*    */ {
/* 25 */   TRACK_NR("TRACK_NR"),
/* 26 */   ABS_TIME("ABS_TIME"),
/* 27 */   REL_TIME("REL_TIME"),
/* 28 */   ABS_COUNT("ABS_COUNT"),
/* 29 */   REL_COUNT("REL_COUNT"),
/* 30 */   CHANNEL_FREQ("CHANNEL_FREQ"),
/* 31 */   TAPE_INDEX("TAPE-INDEX"),
/* 32 */   FRAME("FRAME");
/*    */   
/*    */   private String protocolString;
/*    */   
/*    */   SeekMode(String protocolString) {
/* 37 */     this.protocolString = protocolString;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 42 */     return this.protocolString;
/*    */   }
/*    */   
/*    */   public static SeekMode valueOrExceptionOf(String s) throws IllegalArgumentException {
/* 46 */     for (SeekMode seekMode : values()) {
/* 47 */       if (seekMode.protocolString.equals(s)) {
/* 48 */         return seekMode;
/*    */       }
/*    */     } 
/* 51 */     throw new IllegalArgumentException("Invalid seek mode string: " + s);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\SeekMode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */