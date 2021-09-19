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
/*    */ public enum DLNAFlags
/*    */ {
/* 41 */   SENDER_PACED(-2147483648),
/* 42 */   TIME_BASED_SEEK(1073741824),
/* 43 */   BYTE_BASED_SEEK(536870912),
/* 44 */   FLAG_PLAY_CONTAINER(268435456),
/* 45 */   S0_INCREASE(134217728),
/* 46 */   SN_INCREASE(67108864),
/* 47 */   RTSP_PAUSE(33554432),
/* 48 */   STREAMING_TRANSFER_MODE(16777216),
/* 49 */   INTERACTIVE_TRANSFERT_MODE(8388608),
/* 50 */   BACKGROUND_TRANSFERT_MODE(4194304),
/* 51 */   CONNECTION_STALL(2097152),
/* 52 */   DLNA_V15(1048576);
/*    */   
/*    */   private int code;
/*    */   
/*    */   DLNAFlags(int code) {
/* 57 */     this.code = code;
/*    */   }
/*    */   
/*    */   public int getCode() {
/* 61 */     return this.code;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\DLNAFlags.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */