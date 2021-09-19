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
/*    */ public class USNRootDeviceHeader
/*    */   extends UpnpHeader<UDN>
/*    */ {
/*    */   public static final String ROOT_DEVICE_SUFFIX = "::upnp:rootdevice";
/*    */   
/*    */   public USNRootDeviceHeader() {}
/*    */   
/*    */   public USNRootDeviceHeader(UDN udn) {
/* 31 */     setValue(udn);
/*    */   }
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 35 */     if (!s.startsWith("uuid:") || !s.endsWith("::upnp:rootdevice")) {
/* 36 */       throw new InvalidHeaderException("Invalid root device USN header value, must start with 'uuid:' and end with '::upnp:rootdevice' but is '" + s + "'");
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 42 */     UDN udn = new UDN(s.substring("uuid:".length(), s.length() - "::upnp:rootdevice".length()));
/* 43 */     setValue(udn);
/*    */   }
/*    */   
/*    */   public String getString() {
/* 47 */     return getValue().toString() + "::upnp:rootdevice";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\USNRootDeviceHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */