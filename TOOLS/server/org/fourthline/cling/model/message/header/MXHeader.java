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
/*    */ public class MXHeader
/*    */   extends UpnpHeader<Integer>
/*    */ {
/* 24 */   public static final Integer DEFAULT_VALUE = Integer.valueOf(3);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MXHeader() {
/* 30 */     setValue(DEFAULT_VALUE);
/*    */   }
/*    */   
/*    */   public MXHeader(Integer delayInSeconds) {
/* 34 */     setValue(delayInSeconds);
/*    */   }
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/*    */     Integer value;
/*    */     try {
/* 40 */       value = Integer.valueOf(Integer.parseInt(s));
/* 41 */     } catch (Exception ex) {
/* 42 */       throw new InvalidHeaderException("Can't parse MX seconds integer from: " + s);
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 47 */     if (value.intValue() < 0 || value.intValue() > 120) {
/* 48 */       setValue(DEFAULT_VALUE);
/*    */     } else {
/* 50 */       setValue(value);
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getString() {
/* 55 */     return getValue().toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\MXHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */