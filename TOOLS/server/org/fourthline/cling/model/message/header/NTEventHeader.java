/*    */ package org.fourthline.cling.model.message.header;
/*    */ 
/*    */ import java.util.Locale;
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
/*    */ public class NTEventHeader
/*    */   extends UpnpHeader<String>
/*    */ {
/*    */   public NTEventHeader() {
/* 26 */     setValue("upnp:event");
/*    */   }
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 30 */     if (!s.toLowerCase(Locale.ROOT).equals(getValue())) {
/* 31 */       throw new InvalidHeaderException("Invalid event NT header value: " + s);
/*    */     }
/*    */   }
/*    */   
/*    */   public String getString() {
/* 36 */     return getValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\NTEventHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */