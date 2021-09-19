/*    */ package org.fourthline.cling.model.message.discovery;
/*    */ 
/*    */ import org.fourthline.cling.model.Location;
/*    */ import org.fourthline.cling.model.message.header.DeviceTypeHeader;
/*    */ import org.fourthline.cling.model.message.header.DeviceUSNHeader;
/*    */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*    */ import org.fourthline.cling.model.meta.LocalDevice;
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
/*    */ public class OutgoingNotificationRequestDeviceType
/*    */   extends OutgoingNotificationRequest
/*    */ {
/*    */   public OutgoingNotificationRequestDeviceType(Location location, LocalDevice device, NotificationSubtype type) {
/* 31 */     super(location, device, type);
/*    */     
/* 33 */     getHeaders().add(UpnpHeader.Type.NT, (UpnpHeader)new DeviceTypeHeader(device.getType()));
/* 34 */     getHeaders().add(UpnpHeader.Type.USN, (UpnpHeader)new DeviceUSNHeader(device.getIdentity().getUdn(), device.getType()));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\discovery\OutgoingNotificationRequestDeviceType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */