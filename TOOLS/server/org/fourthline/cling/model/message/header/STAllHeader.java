/*    */ package org.fourthline.cling.model.message.header;
/*    */ 
/*    */ import org.fourthline.cling.model.types.NotificationSubtype;
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
/*    */ public class STAllHeader
/*    */   extends UpnpHeader<NotificationSubtype>
/*    */ {
/*    */   public STAllHeader() {
/* 26 */     setValue(NotificationSubtype.ALL);
/*    */   }
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 30 */     if (!s.equals(NotificationSubtype.ALL.getHeaderString())) {
/* 31 */       throw new InvalidHeaderException("Invalid ST header value (not " + NotificationSubtype.ALL + "): " + s);
/*    */     }
/*    */   }
/*    */   
/*    */   public String getString() {
/* 36 */     return getValue().getHeaderString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\STAllHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */