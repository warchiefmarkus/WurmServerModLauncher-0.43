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
/*    */ public class TransportSettings
/*    */ {
/* 23 */   private PlayMode playMode = PlayMode.NORMAL;
/* 24 */   private RecordQualityMode recQualityMode = RecordQualityMode.NOT_IMPLEMENTED;
/*    */ 
/*    */   
/*    */   public TransportSettings() {}
/*    */   
/*    */   public TransportSettings(PlayMode playMode) {
/* 30 */     this.playMode = playMode;
/*    */   }
/*    */   
/*    */   public TransportSettings(PlayMode playMode, RecordQualityMode recQualityMode) {
/* 34 */     this.playMode = playMode;
/* 35 */     this.recQualityMode = recQualityMode;
/*    */   }
/*    */   
/*    */   public PlayMode getPlayMode() {
/* 39 */     return this.playMode;
/*    */   }
/*    */   
/*    */   public RecordQualityMode getRecQualityMode() {
/* 43 */     return this.recQualityMode;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\TransportSettings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */