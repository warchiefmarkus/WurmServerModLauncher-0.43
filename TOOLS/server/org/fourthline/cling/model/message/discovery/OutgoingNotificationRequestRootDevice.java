/*    */ package org.fourthline.cling.model.message.discovery;
/*    */ 
/*    */ import org.fourthline.cling.model.Location;
/*    */ import org.fourthline.cling.model.message.header.InterfaceMacHeader;
/*    */ import org.fourthline.cling.model.message.header.RootDeviceHeader;
/*    */ import org.fourthline.cling.model.message.header.USNRootDeviceHeader;
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
/*    */ 
/*    */ public class OutgoingNotificationRequestRootDevice
/*    */   extends OutgoingNotificationRequest
/*    */ {
/*    */   public OutgoingNotificationRequestRootDevice(Location location, LocalDevice device, NotificationSubtype type) {
/* 33 */     super(location, device, type);
/*    */     
/* 35 */     getHeaders().add(UpnpHeader.Type.NT, (UpnpHeader)new RootDeviceHeader());
/* 36 */     getHeaders().add(UpnpHeader.Type.USN, (UpnpHeader)new USNRootDeviceHeader(device.getIdentity().getUdn()));
/*    */     
/* 38 */     if ("true".equals(System.getProperty("org.fourthline.cling.network.announceMACAddress")) && location
/* 39 */       .getNetworkAddress().getHardwareAddress() != null)
/* 40 */       getHeaders().add(UpnpHeader.Type.EXT_IFACE_MAC, (UpnpHeader)new InterfaceMacHeader(location
/*    */             
/* 42 */             .getNetworkAddress().getHardwareAddress())); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\discovery\OutgoingNotificationRequestRootDevice.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */