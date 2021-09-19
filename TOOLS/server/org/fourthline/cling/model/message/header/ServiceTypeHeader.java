/*    */ package org.fourthline.cling.model.message.header;
/*    */ 
/*    */ import java.net.URI;
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
/*    */ public class ServiceTypeHeader
/*    */   extends UpnpHeader<ServiceType>
/*    */ {
/*    */   public ServiceTypeHeader() {}
/*    */   
/*    */   public ServiceTypeHeader(URI uri) {
/* 31 */     setString(uri.toString());
/*    */   }
/*    */   
/*    */   public ServiceTypeHeader(ServiceType value) {
/* 35 */     setValue(value);
/*    */   }
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/*    */     try {
/* 40 */       setValue(ServiceType.valueOf(s));
/* 41 */     } catch (RuntimeException ex) {
/* 42 */       throw new InvalidHeaderException("Invalid service type header value, " + ex.getMessage());
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getString() {
/* 47 */     return getValue().toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\ServiceTypeHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */