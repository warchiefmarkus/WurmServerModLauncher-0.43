/*    */ package org.fourthline.cling.support.model.dlna.types;
/*    */ 
/*    */ import org.fourthline.cling.model.types.BytesRange;
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
/*    */ public class AvailableSeekRangeType
/*    */ {
/*    */   private Mode modeFlag;
/*    */   private NormalPlayTimeRange normalPlayTimeRange;
/*    */   private BytesRange bytesRange;
/*    */   
/*    */   public enum Mode
/*    */   {
/* 27 */     MODE_0,
/* 28 */     MODE_1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AvailableSeekRangeType(Mode modeFlag, NormalPlayTimeRange nptRange) {
/* 37 */     this.modeFlag = modeFlag;
/* 38 */     this.normalPlayTimeRange = nptRange;
/*    */   }
/*    */   
/*    */   public AvailableSeekRangeType(Mode modeFlag, BytesRange byteRange) {
/* 42 */     this.modeFlag = modeFlag;
/* 43 */     this.bytesRange = byteRange;
/*    */   }
/*    */   
/*    */   public AvailableSeekRangeType(Mode modeFlag, NormalPlayTimeRange nptRange, BytesRange byteRange) {
/* 47 */     this.modeFlag = modeFlag;
/* 48 */     this.normalPlayTimeRange = nptRange;
/* 49 */     this.bytesRange = byteRange;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NormalPlayTimeRange getNormalPlayTimeRange() {
/* 56 */     return this.normalPlayTimeRange;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BytesRange getBytesRange() {
/* 63 */     return this.bytesRange;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Mode getModeFlag() {
/* 70 */     return this.modeFlag;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\types\AvailableSeekRangeType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */