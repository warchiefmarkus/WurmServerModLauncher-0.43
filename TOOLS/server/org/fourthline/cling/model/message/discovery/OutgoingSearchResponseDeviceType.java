/*    */ package org.fourthline.cling.model.message.discovery;
/*    */ 
/*    */ import org.fourthline.cling.model.Location;
/*    */ import org.fourthline.cling.model.message.IncomingDatagramMessage;
/*    */ import org.fourthline.cling.model.message.header.DeviceTypeHeader;
/*    */ import org.fourthline.cling.model.message.header.DeviceUSNHeader;
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
/*    */ public class OutgoingSearchResponseDeviceType
/*    */   extends OutgoingSearchResponse
/*    */ {
/*    */   public OutgoingSearchResponseDeviceType(IncomingDatagramMessage request, Location location, LocalDevice device) {
/* 33 */     super(request, location, device);
/*    */     
/* 35 */     getHeaders().add(UpnpHeader.Type.ST, (UpnpHeader)new DeviceTypeHeader(device.getType()));
/* 36 */     getHeaders().add(UpnpHeader.Type.USN, (UpnpHeader)new DeviceUSNHeader(device.getIdentity().getUdn(), device.getType()));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\discovery\OutgoingSearchResponseDeviceType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */