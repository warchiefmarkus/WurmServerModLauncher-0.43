/*    */ package org.fourthline.cling.model.message.discovery;
/*    */ 
/*    */ import org.fourthline.cling.model.Location;
/*    */ import org.fourthline.cling.model.message.IncomingDatagramMessage;
/*    */ import org.fourthline.cling.model.message.header.ServiceTypeHeader;
/*    */ import org.fourthline.cling.model.message.header.ServiceUSNHeader;
/*    */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*    */ import org.fourthline.cling.model.meta.LocalDevice;
/*    */ import org.fourthline.cling.model.types.ServiceType;
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
/*    */ public class OutgoingSearchResponseServiceType
/*    */   extends OutgoingSearchResponse
/*    */ {
/*    */   public OutgoingSearchResponseServiceType(IncomingDatagramMessage request, Location location, LocalDevice device, ServiceType serviceType) {
/* 35 */     super(request, location, device);
/*    */     
/* 37 */     getHeaders().add(UpnpHeader.Type.ST, (UpnpHeader)new ServiceTypeHeader(serviceType));
/* 38 */     getHeaders().add(UpnpHeader.Type.USN, (UpnpHeader)new ServiceUSNHeader(device.getIdentity().getUdn(), serviceType));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\discovery\OutgoingSearchResponseServiceType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */