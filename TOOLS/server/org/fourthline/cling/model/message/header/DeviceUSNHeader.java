/*    */ package org.fourthline.cling.model.message.header;
/*    */ 
/*    */ import org.fourthline.cling.model.types.DeviceType;
/*    */ import org.fourthline.cling.model.types.NamedDeviceType;
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
/*    */ public class DeviceUSNHeader
/*    */   extends UpnpHeader<NamedDeviceType>
/*    */ {
/*    */   public DeviceUSNHeader() {}
/*    */   
/*    */   public DeviceUSNHeader(UDN udn, DeviceType deviceType) {
/* 31 */     setValue(new NamedDeviceType(udn, deviceType));
/*    */   }
/*    */   
/*    */   public DeviceUSNHeader(NamedDeviceType value) {
/* 35 */     setValue(value);
/*    */   }
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/*    */     try {
/* 40 */       setValue(NamedDeviceType.valueOf(s));
/* 41 */     } catch (Exception ex) {
/* 42 */       throw new InvalidHeaderException("Invalid device USN header value, " + ex.getMessage());
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getString() {
/* 47 */     return getValue().toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\DeviceUSNHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */