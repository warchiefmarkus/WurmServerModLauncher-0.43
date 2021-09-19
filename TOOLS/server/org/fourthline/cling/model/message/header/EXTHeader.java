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
/*    */ public class EXTHeader
/*    */   extends UpnpHeader<String>
/*    */ {
/*    */   public static final String DEFAULT_VALUE = "";
/*    */   
/*    */   public EXTHeader() {
/* 27 */     setValue("");
/*    */   }
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 31 */     if (s != null && s.length() > 0) {
/* 32 */       throw new InvalidHeaderException("Invalid EXT header, it has no value: " + s);
/*    */     }
/*    */   }
/*    */   
/*    */   public String getString() {
/* 37 */     return getValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\EXTHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */