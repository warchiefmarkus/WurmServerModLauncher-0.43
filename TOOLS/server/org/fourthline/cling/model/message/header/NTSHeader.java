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
/*    */ 
/*    */ public class NTSHeader
/*    */   extends UpnpHeader<NotificationSubtype>
/*    */ {
/*    */   public NTSHeader() {}
/*    */   
/*    */   public NTSHeader(NotificationSubtype type) {
/* 29 */     setValue(type);
/*    */   }
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 33 */     for (NotificationSubtype type : NotificationSubtype.values()) {
/* 34 */       if (s.equals(type.getHeaderString())) {
/* 35 */         setValue(type);
/*    */         break;
/*    */       } 
/*    */     } 
/* 39 */     if (getValue() == null) {
/* 40 */       throw new InvalidHeaderException("Invalid NTS header value: " + s);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 46 */     return getValue().getHeaderString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\NTSHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */