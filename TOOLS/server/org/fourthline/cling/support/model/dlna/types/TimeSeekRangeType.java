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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TimeSeekRangeType
/*    */ {
/*    */   private NormalPlayTimeRange normalPlayTimeRange;
/*    */   private BytesRange bytesRange;
/*    */   
/*    */   public TimeSeekRangeType(NormalPlayTimeRange nptRange) {
/* 30 */     this.normalPlayTimeRange = nptRange;
/*    */   }
/*    */   
/*    */   public TimeSeekRangeType(NormalPlayTimeRange nptRange, BytesRange byteRange) {
/* 34 */     this.normalPlayTimeRange = nptRange;
/* 35 */     this.bytesRange = byteRange;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NormalPlayTimeRange getNormalPlayTimeRange() {
/* 42 */     return this.normalPlayTimeRange;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BytesRange getBytesRange() {
/* 49 */     return this.bytesRange;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setBytesRange(BytesRange bytesRange) {
/* 56 */     this.bytesRange = bytesRange;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\types\TimeSeekRangeType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */