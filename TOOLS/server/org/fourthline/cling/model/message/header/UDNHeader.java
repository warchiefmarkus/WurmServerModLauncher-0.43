/*    */ package org.fourthline.cling.model.message.header;
/*    */ 
/*    */ import org.fourthline.cling.model.types.UDN;
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
/*    */ public class UDNHeader
/*    */   extends UpnpHeader<UDN>
/*    */ {
/*    */   public UDNHeader() {}
/*    */   
/*    */   public UDNHeader(UDN udn) {
/* 29 */     setValue(udn);
/*    */   }
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 33 */     if (!s.startsWith("uuid:")) {
/* 34 */       throw new InvalidHeaderException("Invalid UDA header value, must start with 'uuid:': " + s);
/*    */     }
/*    */     
/* 37 */     if (s.contains("::urn")) {
/* 38 */       throw new InvalidHeaderException("Invalid UDA header value, must not contain '::urn': " + s);
/*    */     }
/*    */     
/* 41 */     UDN udn = new UDN(s.substring("uuid:".length()));
/* 42 */     setValue(udn);
/*    */   }
/*    */   
/*    */   public String getString() {
/* 46 */     return getValue().toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\UDNHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */