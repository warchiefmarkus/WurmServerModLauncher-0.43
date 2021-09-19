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
/*    */ public class GetAvailableSeekRangeHeader
/*    */   extends DLNAHeader<Integer>
/*    */ {
/*    */   public GetAvailableSeekRangeHeader() {
/* 25 */     setValue(Integer.valueOf(1));
/*    */   }
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 30 */     if (s.length() != 0) {
/*    */       try {
/* 32 */         int t = Integer.parseInt(s);
/* 33 */         if (t == 1)
/*    */           return; 
/* 35 */       } catch (Exception exception) {}
/*    */     }
/* 37 */     throw new InvalidHeaderException("Invalid GetAvailableSeekRange header value: " + s);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 42 */     return ((Integer)getValue()).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\message\header\GetAvailableSeekRangeHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */