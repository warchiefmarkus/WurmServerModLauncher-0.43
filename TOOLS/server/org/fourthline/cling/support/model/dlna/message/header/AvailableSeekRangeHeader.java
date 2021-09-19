/*    */ package org.fourthline.cling.support.model.dlna.message.header;
/*    */ 
/*    */ import org.fourthline.cling.model.message.header.InvalidHeaderException;
/*    */ import org.fourthline.cling.model.types.BytesRange;
/*    */ import org.fourthline.cling.model.types.InvalidValueException;
/*    */ import org.fourthline.cling.support.model.dlna.types.AvailableSeekRangeType;
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
/*    */ public class AvailableSeekRangeHeader
/*    */   extends DLNAHeader<AvailableSeekRangeType>
/*    */ {
/*    */   public AvailableSeekRangeHeader() {}
/*    */   
/*    */   public AvailableSeekRangeHeader(AvailableSeekRangeType timeSeekRange) {
/* 32 */     setValue(timeSeekRange);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 37 */     if (s.length() != 0) {
/* 38 */       String[] params = s.split(" ");
/* 39 */       if (params.length > 1) {
/*    */         try {
/* 41 */           AvailableSeekRangeType.Mode mode = null;
/* 42 */           NormalPlayTimeRange timeRange = null;
/* 43 */           BytesRange byteRange = null;
/*    */ 
/*    */           
/*    */           try {
/* 47 */             mode = AvailableSeekRangeType.Mode.valueOf("MODE_" + params[0]);
/* 48 */           } catch (IllegalArgumentException e) {
/* 49 */             throw new InvalidValueException("Invalid AvailableSeekRange Mode");
/*    */           } 
/*    */           
/* 52 */           boolean useTime = true;
/*    */           
/*    */           try {
/* 55 */             timeRange = NormalPlayTimeRange.valueOf(params[1], true);
/* 56 */           } catch (InvalidValueException timeInvalidValueException) {
/*    */             try {
/* 58 */               byteRange = BytesRange.valueOf(params[1]);
/* 59 */               useTime = false;
/* 60 */             } catch (InvalidValueException bytesInvalidValueException) {
/* 61 */               throw new InvalidValueException("Invalid AvailableSeekRange Range");
/*    */             } 
/*    */           } 
/* 64 */           if (useTime) {
/* 65 */             if (params.length > 2) {
/*    */               
/* 67 */               byteRange = BytesRange.valueOf(params[2]);
/* 68 */               setValue(new AvailableSeekRangeType(mode, timeRange, byteRange));
/*    */             } else {
/*    */               
/* 71 */               setValue(new AvailableSeekRangeType(mode, timeRange));
/*    */             } 
/*    */           } else {
/* 74 */             setValue(new AvailableSeekRangeType(mode, byteRange));
/*    */           } 
/*    */           return;
/* 77 */         } catch (InvalidValueException invalidValueException) {
/* 78 */           throw new InvalidHeaderException("Invalid AvailableSeekRange header value: " + s + "; " + invalidValueException.getMessage());
/*    */         } 
/*    */       }
/*    */     } 
/* 82 */     throw new InvalidHeaderException("Invalid AvailableSeekRange header value: " + s);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 87 */     AvailableSeekRangeType t = (AvailableSeekRangeType)getValue();
/* 88 */     String s = Integer.toString(t.getModeFlag().ordinal());
/* 89 */     if (t.getNormalPlayTimeRange() != null) {
/* 90 */       s = s + " " + t.getNormalPlayTimeRange().getString(false);
/*    */     }
/* 92 */     if (t.getBytesRange() != null) {
/* 93 */       s = s + " " + t.getBytesRange().getString(false);
/*    */     }
/* 95 */     return s;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\message\header\AvailableSeekRangeHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */