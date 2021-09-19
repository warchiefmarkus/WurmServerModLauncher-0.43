/*    */ package org.fourthline.cling.model.message.discovery;
/*    */ 
/*    */ import org.fourthline.cling.model.Location;
/*    */ import org.fourthline.cling.model.message.IncomingDatagramMessage;
/*    */ import org.fourthline.cling.model.message.OutgoingDatagramMessage;
/*    */ import org.fourthline.cling.model.message.UpnpOperation;
/*    */ import org.fourthline.cling.model.message.UpnpResponse;
/*    */ import org.fourthline.cling.model.message.header.EXTHeader;
/*    */ import org.fourthline.cling.model.message.header.InterfaceMacHeader;
/*    */ import org.fourthline.cling.model.message.header.LocationHeader;
/*    */ import org.fourthline.cling.model.message.header.MaxAgeHeader;
/*    */ import org.fourthline.cling.model.message.header.ServerHeader;
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
/*    */ 
/*    */ public class OutgoingSearchResponse
/*    */   extends OutgoingDatagramMessage<UpnpResponse>
/*    */ {
/*    */   public OutgoingSearchResponse(IncomingDatagramMessage request, Location location, LocalDevice device) {
/* 40 */     super((UpnpOperation)new UpnpResponse(UpnpResponse.Status.OK), request.getSourceAddress(), request.getSourcePort());
/*    */     
/* 42 */     getHeaders().add(UpnpHeader.Type.MAX_AGE, (UpnpHeader)new MaxAgeHeader(device.getIdentity().getMaxAgeSeconds()));
/* 43 */     getHeaders().add(UpnpHeader.Type.LOCATION, (UpnpHeader)new LocationHeader(location.getURL()));
/* 44 */     getHeaders().add(UpnpHeader.Type.SERVER, (UpnpHeader)new ServerHeader());
/* 45 */     getHeaders().add(UpnpHeader.Type.EXT, (UpnpHeader)new EXTHeader());
/*    */     
/* 47 */     if ("true".equals(System.getProperty("org.fourthline.cling.network.announceMACAddress")) && location
/* 48 */       .getNetworkAddress().getHardwareAddress() != null)
/* 49 */       getHeaders().add(UpnpHeader.Type.EXT_IFACE_MAC, (UpnpHeader)new InterfaceMacHeader(location
/*    */             
/* 51 */             .getNetworkAddress().getHardwareAddress())); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\discovery\OutgoingSearchResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */