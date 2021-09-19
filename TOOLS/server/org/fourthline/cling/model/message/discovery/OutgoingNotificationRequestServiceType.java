/*    */ package org.fourthline.cling.model.message.discovery;
/*    */ 
/*    */ import org.fourthline.cling.model.Location;
/*    */ import org.fourthline.cling.model.message.header.ServiceTypeHeader;
/*    */ import org.fourthline.cling.model.message.header.ServiceUSNHeader;
/*    */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*    */ import org.fourthline.cling.model.meta.LocalDevice;
/*    */ import org.fourthline.cling.model.types.NotificationSubtype;
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
/*    */ public class OutgoingNotificationRequestServiceType
/*    */   extends OutgoingNotificationRequest
/*    */ {
/*    */   public OutgoingNotificationRequestServiceType(Location location, LocalDevice device, NotificationSubtype type, ServiceType serviceType) {
/* 35 */     super(location, device, type);
/*    */     
/* 37 */     getHeaders().add(UpnpHeader.Type.NT, (UpnpHeader)new ServiceTypeHeader(serviceType));
/* 38 */     getHeaders().add(UpnpHeader.Type.USN, (UpnpHeader)new ServiceUSNHeader(device.getIdentity().getUdn(), serviceType));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\discovery\OutgoingNotificationRequestServiceType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */