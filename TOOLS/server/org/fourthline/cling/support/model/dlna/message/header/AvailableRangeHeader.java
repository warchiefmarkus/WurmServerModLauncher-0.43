/*    */ package org.fourthline.cling.support.model.dlna.message.header;
/*    */ 
/*    */ import org.fourthline.cling.model.message.header.InvalidHeaderException;
/*    */ import org.fourthline.cling.support.model.dlna.types.NormalPlayTimeRange;
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
/*    */ public class AvailableRangeHeader
/*    */   extends DLNAHeader<NormalPlayTimeRange>
/*    */ {
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 30 */     if (s.length() != 0) {
/*    */       try {
/* 32 */         setValue(NormalPlayTimeRange.valueOf(s, true));
/*    */         return;
/* 34 */       } catch (Exception exception) {}
/*    */     }
/* 36 */     throw new InvalidHeaderException("Invalid AvailableRange header value: " + s);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 41 */     return ((NormalPlayTimeRange)getValue()).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\message\header\AvailableRangeHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */