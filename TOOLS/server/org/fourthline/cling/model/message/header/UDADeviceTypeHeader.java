/*    */ package org.fourthline.cling.model.message.header;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.fourthline.cling.model.types.DeviceType;
/*    */ import org.fourthline.cling.model.types.UDADeviceType;
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
/*    */ public class UDADeviceTypeHeader
/*    */   extends DeviceTypeHeader
/*    */ {
/*    */   public UDADeviceTypeHeader() {}
/*    */   
/*    */   public UDADeviceTypeHeader(URI uri) {
/* 32 */     super(uri);
/*    */   }
/*    */   
/*    */   public UDADeviceTypeHeader(DeviceType value) {
/* 36 */     super(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/*    */     try {
/* 42 */       setValue((DeviceType)UDADeviceType.valueOf(s));
/* 43 */     } catch (Exception ex) {
/* 44 */       throw new InvalidHeaderException("Invalid UDA device type header value, " + ex.getMessage());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\UDADeviceTypeHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */