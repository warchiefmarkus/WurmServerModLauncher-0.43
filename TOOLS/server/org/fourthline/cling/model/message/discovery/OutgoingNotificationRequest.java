/*    */ package org.fourthline.cling.model.message.discovery;
/*    */ 
/*    */ import org.fourthline.cling.model.Location;
/*    */ import org.fourthline.cling.model.ModelUtil;
/*    */ import org.fourthline.cling.model.message.OutgoingDatagramMessage;
/*    */ import org.fourthline.cling.model.message.UpnpOperation;
/*    */ import org.fourthline.cling.model.message.UpnpRequest;
/*    */ import org.fourthline.cling.model.message.header.HostHeader;
/*    */ import org.fourthline.cling.model.message.header.LocationHeader;
/*    */ import org.fourthline.cling.model.message.header.MaxAgeHeader;
/*    */ import org.fourthline.cling.model.message.header.NTSHeader;
/*    */ import org.fourthline.cling.model.message.header.ServerHeader;
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
/*    */ public abstract class OutgoingNotificationRequest
/*    */   extends OutgoingDatagramMessage<UpnpRequest>
/*    */ {
/*    */   private NotificationSubtype type;
/*    */   
/*    */   protected OutgoingNotificationRequest(Location location, LocalDevice device, NotificationSubtype type) {
/* 40 */     super((UpnpOperation)new UpnpRequest(UpnpRequest.Method.NOTIFY), 
/*    */         
/* 42 */         ModelUtil.getInetAddressByName("239.255.255.250"), 1900);
/*    */ 
/*    */ 
/*    */     
/* 46 */     this.type = type;
/*    */     
/* 48 */     getHeaders().add(UpnpHeader.Type.MAX_AGE, (UpnpHeader)new MaxAgeHeader(device.getIdentity().getMaxAgeSeconds()));
/* 49 */     getHeaders().add(UpnpHeader.Type.LOCATION, (UpnpHeader)new LocationHeader(location.getURL()));
/*    */     
/* 51 */     getHeaders().add(UpnpHeader.Type.SERVER, (UpnpHeader)new ServerHeader());
/* 52 */     getHeaders().add(UpnpHeader.Type.HOST, (UpnpHeader)new HostHeader());
/* 53 */     getHeaders().add(UpnpHeader.Type.NTS, (UpnpHeader)new NTSHeader(type));
/*    */   }
/*    */   
/*    */   public NotificationSubtype getType() {
/* 57 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\discovery\OutgoingNotificationRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */