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
/*    */ public class AVClientInfoHeader
/*    */   extends UpnpHeader<String>
/*    */ {
/*    */   public AVClientInfoHeader() {}
/*    */   
/*    */   public AVClientInfoHeader(String s) {
/* 29 */     setValue(s);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 34 */     setValue(s);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 39 */     return getValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\AVClientInfoHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */