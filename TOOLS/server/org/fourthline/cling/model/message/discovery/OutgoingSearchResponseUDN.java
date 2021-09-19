/*    */ package org.fourthline.cling.model.message.discovery;
/*    */ 
/*    */ import org.fourthline.cling.model.Location;
/*    */ import org.fourthline.cling.model.message.IncomingDatagramMessage;
/*    */ import org.fourthline.cling.model.message.header.UDNHeader;
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
/*    */ public class OutgoingSearchResponseUDN
/*    */   extends OutgoingSearchResponse
/*    */ {
/*    */   public OutgoingSearchResponseUDN(IncomingDatagramMessage request, Location location, LocalDevice device) {
/* 32 */     super(request, location, device);
/*    */     
/* 34 */     getHeaders().add(UpnpHeader.Type.ST, (UpnpHeader)new UDNHeader(device.getIdentity().getUdn()));
/* 35 */     getHeaders().add(UpnpHeader.Type.USN, (UpnpHeader)new UDNHeader(device.getIdentity().getUdn()));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\discovery\OutgoingSearchResponseUDN.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */