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
/*    */ public class SCIDHeader
/*    */   extends DLNAHeader<String>
/*    */ {
/*    */   public SCIDHeader() {
/* 25 */     setValue("");
/*    */   }
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 30 */     if (s.length() != 0) {
/* 31 */       setValue(s);
/*    */       return;
/*    */     } 
/* 34 */     throw new InvalidHeaderException("Invalid SCID header value: " + s);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 39 */     return ((String)getValue()).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\message\header\SCIDHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */