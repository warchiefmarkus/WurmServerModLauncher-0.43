/*    */ package org.fourthline.cling.model.message.header;
/*    */ 
/*    */ import org.fourthline.cling.model.types.NamedServiceType;
/*    */ import org.fourthline.cling.model.types.ServiceType;
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
/*    */ public class ServiceUSNHeader
/*    */   extends UpnpHeader<NamedServiceType>
/*    */ {
/*    */   public ServiceUSNHeader() {}
/*    */   
/*    */   public ServiceUSNHeader(UDN udn, ServiceType serviceType) {
/* 31 */     setValue(new NamedServiceType(udn, serviceType));
/*    */   }
/*    */   
/*    */   public ServiceUSNHeader(NamedServiceType value) {
/* 35 */     setValue(value);
/*    */   }
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/*    */     try {
/* 40 */       setValue(NamedServiceType.valueOf(s));
/* 41 */     } catch (Exception ex) {
/* 42 */       throw new InvalidHeaderException("Invalid service USN header value, " + ex.getMessage());
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getString() {
/* 47 */     return getValue().toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\ServiceUSNHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */