/*    */ package org.fourthline.cling.model;
/*    */ 
/*    */ import org.fourthline.cling.model.types.ServiceId;
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
/*    */ public class ServiceReference
/*    */ {
/*    */   public static final String DELIMITER = "/";
/*    */   private final UDN udn;
/*    */   private final ServiceId serviceId;
/*    */   
/*    */   public ServiceReference(String s) {
/* 44 */     String[] split = s.split("/");
/* 45 */     if (split.length == 2) {
/* 46 */       this.udn = UDN.valueOf(split[0]);
/* 47 */       this.serviceId = ServiceId.valueOf(split[1]);
/*    */     } else {
/* 49 */       this.udn = null;
/* 50 */       this.serviceId = null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public ServiceReference(UDN udn, ServiceId serviceId) {
/* 55 */     this.udn = udn;
/* 56 */     this.serviceId = serviceId;
/*    */   }
/*    */   
/*    */   public UDN getUdn() {
/* 60 */     return this.udn;
/*    */   }
/*    */   
/*    */   public ServiceId getServiceId() {
/* 64 */     return this.serviceId;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 69 */     if (this == o) return true; 
/* 70 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 72 */     ServiceReference that = (ServiceReference)o;
/*    */     
/* 74 */     if (!this.serviceId.equals(that.serviceId)) return false; 
/* 75 */     if (!this.udn.equals(that.udn)) return false;
/*    */     
/* 77 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 82 */     int result = this.udn.hashCode();
/* 83 */     result = 31 * result + this.serviceId.hashCode();
/* 84 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 89 */     return (this.udn == null || this.serviceId == null) ? "" : (this.udn.toString() + "/" + this.serviceId.toString());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\ServiceReference.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */