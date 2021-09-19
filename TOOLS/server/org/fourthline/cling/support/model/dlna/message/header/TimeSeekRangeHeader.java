/*    */ package org.fourthline.cling.support.model.dlna.message.header;
/*    */ 
/*    */ import org.fourthline.cling.model.message.header.InvalidHeaderException;
/*    */ import org.fourthline.cling.model.types.BytesRange;
/*    */ import org.fourthline.cling.model.types.InvalidValueException;
/*    */ import org.fourthline.cling.support.model.dlna.types.NormalPlayTimeRange;
/*    */ import org.fourthline.cling.support.model.dlna.types.TimeSeekRangeType;
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
/*    */ public class TimeSeekRangeHeader
/*    */   extends DLNAHeader<TimeSeekRangeType>
/*    */ {
/*    */   public TimeSeekRangeHeader() {}
/*    */   
/*    */   public TimeSeekRangeHeader(TimeSeekRangeType timeSeekRange) {
/* 32 */     setValue(timeSeekRange);
/*    */   }
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 36 */     if (s.length() != 0) {
/* 37 */       String[] params = s.split(" ");
/* 38 */       if (params.length > 0) {
/*    */         try {
/* 40 */           TimeSeekRangeType t = new TimeSeekRangeType(NormalPlayTimeRange.valueOf(params[0]));
/* 41 */           if (params.length > 1) {
/* 42 */             t.setBytesRange(BytesRange.valueOf(params[1]));
/*    */           }
/* 44 */           setValue(t);
/*    */           return;
/* 46 */         } catch (InvalidValueException invalidValueException) {
/* 47 */           throw new InvalidHeaderException("Invalid TimeSeekRange header value: " + s + "; " + invalidValueException.getMessage());
/*    */         } 
/*    */       }
/*    */     } 
/* 51 */     throw new InvalidHeaderException("Invalid TimeSeekRange header value: " + s);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 56 */     TimeSeekRangeType t = (TimeSeekRangeType)getValue();
/* 57 */     String s = t.getNormalPlayTimeRange().getString();
/* 58 */     if (t.getBytesRange() != null) s = s + " " + t.getBytesRange().getString(true); 
/* 59 */     return s;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\message\header\TimeSeekRangeHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */