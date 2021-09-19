/*    */ package org.fourthline.cling.model.message.discovery;
/*    */ 
/*    */ import org.fourthline.cling.model.Location;
/*    */ import org.fourthline.cling.model.message.header.UDNHeader;
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
/*    */ public class OutgoingNotificationRequestUDN
/*    */   extends OutgoingNotificationRequest
/*    */ {
/*    */   public OutgoingNotificationRequestUDN(Location location, LocalDevice device, NotificationSubtype type) {
/* 30 */     super(location, device, type);
/*    */     
/* 32 */     getHeaders().add(UpnpHeader.Type.NT, (UpnpHeader)new UDNHeader(device.getIdentity().getUdn()));
/* 33 */     getHeaders().add(UpnpHeader.Type.USN, (UpnpHeader)new UDNHeader(device.getIdentity().getUdn()));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\discovery\OutgoingNotificationRequestUDN.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */