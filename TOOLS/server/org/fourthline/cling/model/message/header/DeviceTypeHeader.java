/*    */ package org.fourthline.cling.model.message.header;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.fourthline.cling.model.types.DeviceType;
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
/*    */ public class DeviceTypeHeader
/*    */   extends UpnpHeader<DeviceType>
/*    */ {
/*    */   public DeviceTypeHeader() {}
/*    */   
/*    */   public DeviceTypeHeader(URI uri) {
/* 31 */     setString(uri.toString());
/*    */   }
/*    */   
/*    */   public DeviceTypeHeader(DeviceType value) {
/* 35 */     setValue(value);
/*    */   }
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/*    */     try {
/* 40 */       setValue(DeviceType.valueOf(s));
/* 41 */     } catch (RuntimeException ex) {
/* 42 */       throw new InvalidHeaderException("Invalid device type header value, " + ex.getMessage());
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getString() {
/* 47 */     return getValue().toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\DeviceTypeHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */