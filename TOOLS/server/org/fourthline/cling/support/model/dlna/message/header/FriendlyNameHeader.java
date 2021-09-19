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
/*    */ public class FriendlyNameHeader
/*    */   extends DLNAHeader<String>
/*    */ {
/*    */   public FriendlyNameHeader() {
/* 25 */     setValue("");
/*    */   }
/*    */   
/*    */   public FriendlyNameHeader(String name) {
/* 29 */     setValue(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 34 */     if (s.length() != 0) {
/* 35 */       setValue(s);
/*    */       return;
/*    */     } 
/* 38 */     throw new InvalidHeaderException("Invalid GetAvailableSeekRange header value: " + s);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 43 */     return (String)getValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\message\header\FriendlyNameHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */