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
/*    */ public class SubscriptionIdHeader
/*    */   extends UpnpHeader<String>
/*    */ {
/*    */   public static final String PREFIX = "uuid:";
/*    */   
/*    */   public SubscriptionIdHeader() {}
/*    */   
/*    */   public SubscriptionIdHeader(String value) {
/* 29 */     setValue(value);
/*    */   }
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 33 */     if (!s.startsWith("uuid:")) {
/* 34 */       throw new InvalidHeaderException("Invalid subscription ID header value, must start with 'uuid:': " + s);
/*    */     }
/* 36 */     setValue(s);
/*    */   }
/*    */   
/*    */   public String getString() {
/* 40 */     return getValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\SubscriptionIdHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */