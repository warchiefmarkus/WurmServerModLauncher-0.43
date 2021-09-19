/*    */ package org.fourthline.cling.model.message.discovery;
/*    */ 
/*    */ import org.fourthline.cling.model.Location;
/*    */ import org.fourthline.cling.model.message.IncomingDatagramMessage;
/*    */ import org.fourthline.cling.model.message.header.RootDeviceHeader;
/*    */ import org.fourthline.cling.model.message.header.USNRootDeviceHeader;
/*    */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*    */ import org.fourthline.cling.model.meta.LocalDevice;
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
/*    */ public class OutgoingSearchResponseRootDevice
/*    */   extends OutgoingSearchResponse
/*    */ {
/*    */   public OutgoingSearchResponseRootDevice(IncomingDatagramMessage request, Location location, LocalDevice device) {
/* 33 */     super(request, location, device);
/*    */     
/* 35 */     getHeaders().add(UpnpHeader.Type.ST, (UpnpHeader)new RootDeviceHeader());
/* 36 */     getHeaders().add(UpnpHeader.Type.USN, (UpnpHeader)new USNRootDeviceHeader(device.getIdentity().getUdn()));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\discovery\OutgoingSearchResponseRootDevice.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */