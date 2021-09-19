/*    */ package org.fourthline.cling.model.message.discovery;
/*    */ 
/*    */ import java.net.URL;
/*    */ import org.fourthline.cling.model.message.IncomingDatagramMessage;
/*    */ import org.fourthline.cling.model.message.UpnpRequest;
/*    */ import org.fourthline.cling.model.message.header.DeviceUSNHeader;
/*    */ import org.fourthline.cling.model.message.header.InterfaceMacHeader;
/*    */ import org.fourthline.cling.model.message.header.LocationHeader;
/*    */ import org.fourthline.cling.model.message.header.MaxAgeHeader;
/*    */ import org.fourthline.cling.model.message.header.NTSHeader;
/*    */ import org.fourthline.cling.model.message.header.ServiceUSNHeader;
/*    */ import org.fourthline.cling.model.message.header.UDNHeader;
/*    */ import org.fourthline.cling.model.message.header.USNRootDeviceHeader;
/*    */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*    */ import org.fourthline.cling.model.types.NamedDeviceType;
/*    */ import org.fourthline.cling.model.types.NamedServiceType;
/*    */ import org.fourthline.cling.model.types.NotificationSubtype;
/*    */ import org.fourthline.cling.model.types.UDN;
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
/*    */ public class IncomingNotificationRequest
/*    */   extends IncomingDatagramMessage<UpnpRequest>
/*    */ {
/*    */   public IncomingNotificationRequest(IncomingDatagramMessage<UpnpRequest> source) {
/* 42 */     super(source);
/*    */   }
/*    */   
/*    */   public boolean isAliveMessage() {
/* 46 */     NTSHeader nts = (NTSHeader)getHeaders().getFirstHeader(UpnpHeader.Type.NTS, NTSHeader.class);
/* 47 */     return (nts != null && ((NotificationSubtype)nts.getValue()).equals(NotificationSubtype.ALIVE));
/*    */   }
/*    */   
/*    */   public boolean isByeByeMessage() {
/* 51 */     NTSHeader nts = (NTSHeader)getHeaders().getFirstHeader(UpnpHeader.Type.NTS, NTSHeader.class);
/* 52 */     return (nts != null && ((NotificationSubtype)nts.getValue()).equals(NotificationSubtype.BYEBYE));
/*    */   }
/*    */   
/*    */   public URL getLocationURL() {
/* 56 */     LocationHeader header = (LocationHeader)getHeaders().getFirstHeader(UpnpHeader.Type.LOCATION, LocationHeader.class);
/* 57 */     if (header != null) {
/* 58 */       return (URL)header.getValue();
/*    */     }
/* 60 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UDN getUDN() {
/* 69 */     UpnpHeader<UDN> udnHeader = getHeaders().getFirstHeader(UpnpHeader.Type.USN, USNRootDeviceHeader.class);
/* 70 */     if (udnHeader != null) return (UDN)udnHeader.getValue();
/*    */     
/* 72 */     udnHeader = getHeaders().getFirstHeader(UpnpHeader.Type.USN, UDNHeader.class);
/* 73 */     if (udnHeader != null) return (UDN)udnHeader.getValue();
/*    */     
/* 75 */     UpnpHeader<NamedDeviceType> deviceTypeHeader = getHeaders().getFirstHeader(UpnpHeader.Type.USN, DeviceUSNHeader.class);
/* 76 */     if (deviceTypeHeader != null) return ((NamedDeviceType)deviceTypeHeader.getValue()).getUdn();
/*    */     
/* 78 */     UpnpHeader<NamedServiceType> serviceTypeHeader = getHeaders().getFirstHeader(UpnpHeader.Type.USN, ServiceUSNHeader.class);
/* 79 */     if (serviceTypeHeader != null) return ((NamedServiceType)serviceTypeHeader.getValue()).getUdn();
/*    */     
/* 81 */     return null;
/*    */   }
/*    */   
/*    */   public Integer getMaxAge() {
/* 85 */     MaxAgeHeader header = (MaxAgeHeader)getHeaders().getFirstHeader(UpnpHeader.Type.MAX_AGE, MaxAgeHeader.class);
/* 86 */     if (header != null) {
/* 87 */       return (Integer)header.getValue();
/*    */     }
/* 89 */     return null;
/*    */   }
/*    */   
/*    */   public byte[] getInterfaceMacHeader() {
/* 93 */     InterfaceMacHeader header = (InterfaceMacHeader)getHeaders().getFirstHeader(UpnpHeader.Type.EXT_IFACE_MAC, InterfaceMacHeader.class);
/* 94 */     if (header != null) {
/* 95 */       return (byte[])header.getValue();
/*    */     }
/* 97 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\discovery\IncomingNotificationRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */