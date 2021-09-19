/*    */ package org.fourthline.cling.support.model.dlna.message.header;
/*    */ 
/*    */ import org.fourthline.cling.model.ServiceReference;
/*    */ import org.fourthline.cling.model.message.header.InvalidHeaderException;
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
/*    */ public class PeerManagerHeader
/*    */   extends DLNAHeader<ServiceReference>
/*    */ {
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 30 */     if (s.length() != 0) {
/*    */       try {
/* 32 */         ServiceReference serviceReference = new ServiceReference(s);
/* 33 */         if (serviceReference.getUdn() != null && serviceReference.getServiceId() != null) {
/* 34 */           setValue(serviceReference);
/*    */           return;
/*    */         } 
/* 37 */       } catch (Exception exception) {}
/*    */     }
/*    */     
/* 40 */     throw new InvalidHeaderException("Invalid PeerManager header value: " + s);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 45 */     return ((ServiceReference)getValue()).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\message\header\PeerManagerHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */