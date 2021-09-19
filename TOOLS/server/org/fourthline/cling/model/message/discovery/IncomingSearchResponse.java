/*    */ package org.fourthline.cling.model.message.discovery;
/*    */ 
/*    */ import java.net.URL;
/*    */ import org.fourthline.cling.model.message.IncomingDatagramMessage;
/*    */ import org.fourthline.cling.model.message.UpnpResponse;
/*    */ import org.fourthline.cling.model.message.header.DeviceUSNHeader;
/*    */ import org.fourthline.cling.model.message.header.InterfaceMacHeader;
/*    */ import org.fourthline.cling.model.message.header.LocationHeader;
/*    */ import org.fourthline.cling.model.message.header.MaxAgeHeader;
/*    */ import org.fourthline.cling.model.message.header.ServiceUSNHeader;
/*    */ import org.fourthline.cling.model.message.header.UDNHeader;
/*    */ import org.fourthline.cling.model.message.header.USNRootDeviceHeader;
/*    */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*    */ import org.fourthline.cling.model.types.NamedDeviceType;
/*    */ import org.fourthline.cling.model.types.NamedServiceType;
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
/*    */ public class IncomingSearchResponse
/*    */   extends IncomingDatagramMessage<UpnpResponse>
/*    */ {
/*    */   public IncomingSearchResponse(IncomingDatagramMessage<UpnpResponse> source) {
/* 40 */     super(source);
/*    */   }
/*    */   
/*    */   public boolean isSearchResponseMessage() {
/* 44 */     UpnpHeader st = getHeaders().getFirstHeader(UpnpHeader.Type.ST);
/* 45 */     UpnpHeader usn = getHeaders().getFirstHeader(UpnpHeader.Type.USN);
/* 46 */     UpnpHeader ext = getHeaders().getFirstHeader(UpnpHeader.Type.EXT);
/* 47 */     return (st != null && st.getValue() != null && usn != null && usn.getValue() != null && ext != null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public UDN getRootDeviceUDN() {
/* 53 */     UpnpHeader<UDN> udnHeader = getHeaders().getFirstHeader(UpnpHeader.Type.USN, USNRootDeviceHeader.class);
/* 54 */     if (udnHeader != null) return (UDN)udnHeader.getValue();
/*    */     
/* 56 */     udnHeader = getHeaders().getFirstHeader(UpnpHeader.Type.USN, UDNHeader.class);
/* 57 */     if (udnHeader != null) return (UDN)udnHeader.getValue();
/*    */     
/* 59 */     UpnpHeader<NamedDeviceType> deviceTypeHeader = getHeaders().getFirstHeader(UpnpHeader.Type.USN, DeviceUSNHeader.class);
/* 60 */     if (deviceTypeHeader != null) return ((NamedDeviceType)deviceTypeHeader.getValue()).getUdn();
/*    */     
/* 62 */     UpnpHeader<NamedServiceType> serviceTypeHeader = getHeaders().getFirstHeader(UpnpHeader.Type.USN, ServiceUSNHeader.class);
/* 63 */     if (serviceTypeHeader != null) return ((NamedServiceType)serviceTypeHeader.getValue()).getUdn();
/*    */     
/* 65 */     return null;
/*    */   }
/*    */   
/*    */   public URL getLocationURL() {
/* 69 */     LocationHeader header = (LocationHeader)getHeaders().getFirstHeader(UpnpHeader.Type.LOCATION, LocationHeader.class);
/* 70 */     if (header != null) {
/* 71 */       return (URL)header.getValue();
/*    */     }
/* 73 */     return null;
/*    */   }
/*    */   
/*    */   public Integer getMaxAge() {
/* 77 */     MaxAgeHeader header = (MaxAgeHeader)getHeaders().getFirstHeader(UpnpHeader.Type.MAX_AGE, MaxAgeHeader.class);
/* 78 */     if (header != null) {
/* 79 */       return (Integer)header.getValue();
/*    */     }
/* 81 */     return null;
/*    */   }
/*    */   
/*    */   public byte[] getInterfaceMacHeader() {
/* 85 */     InterfaceMacHeader header = (InterfaceMacHeader)getHeaders().getFirstHeader(UpnpHeader.Type.EXT_IFACE_MAC, InterfaceMacHeader.class);
/* 86 */     if (header != null) {
/* 87 */       return (byte[])header.getValue();
/*    */     }
/* 89 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\discovery\IncomingSearchResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */