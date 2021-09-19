/*    */ package org.fourthline.cling.model.message.header;
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
/*    */ 
/*    */ public class UserAgentHeader
/*    */   extends UpnpHeader<String>
/*    */ {
/*    */   public UserAgentHeader() {}
/*    */   
/*    */   public UserAgentHeader(String s) {
/* 30 */     setValue(s);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 35 */     setValue(s);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 40 */     return getValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\UserAgentHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */