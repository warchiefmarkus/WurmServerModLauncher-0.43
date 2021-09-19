/*    */ package org.fourthline.cling.support.model.dlna.message.header;
/*    */ 
/*    */ import org.fourthline.cling.model.message.header.InvalidHeaderException;
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
/*    */ public class MaxPrateHeader
/*    */   extends DLNAHeader<Long>
/*    */ {
/*    */   public MaxPrateHeader() {
/* 25 */     setValue(Long.valueOf(0L));
/*    */   }
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/*    */     try {
/* 31 */       setValue(Long.valueOf(Long.parseLong(s)));
/*    */       return;
/* 33 */     } catch (NumberFormatException numberFormatException) {
/*    */       
/* 35 */       throw new InvalidHeaderException("Invalid SCID header value: " + s);
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getString() {
/* 40 */     return ((Long)getValue()).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\message\header\MaxPrateHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */