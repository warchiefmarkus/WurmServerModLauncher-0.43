/*    */ package org.fourthline.cling.model.types;
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
/*    */ public class NamedServiceType
/*    */ {
/*    */   private UDN udn;
/*    */   private ServiceType serviceType;
/*    */   
/*    */   public NamedServiceType(UDN udn, ServiceType serviceType) {
/* 30 */     this.udn = udn;
/* 31 */     this.serviceType = serviceType;
/*    */   }
/*    */   
/*    */   public UDN getUdn() {
/* 35 */     return this.udn;
/*    */   }
/*    */   
/*    */   public ServiceType getServiceType() {
/* 39 */     return this.serviceType;
/*    */   }
/*    */   public static NamedServiceType valueOf(String s) throws InvalidValueException {
/*    */     UDN udn;
/* 43 */     String[] strings = s.split("::");
/* 44 */     if (strings.length != 2) {
/* 45 */       throw new InvalidValueException("Can't parse UDN::ServiceType from: " + s);
/*    */     }
/*    */ 
/*    */     
/*    */     try {
/* 50 */       udn = UDN.valueOf(strings[0]);
/* 51 */     } catch (Exception ex) {
/* 52 */       throw new InvalidValueException("Can't parse UDN: " + strings[0]);
/*    */     } 
/*    */     
/* 55 */     ServiceType serviceType = ServiceType.valueOf(strings[1]);
/* 56 */     return new NamedServiceType(udn, serviceType);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     return getUdn().toString() + "::" + getServiceType().toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 66 */     if (this == o) return true; 
/* 67 */     if (o == null || !(o instanceof NamedServiceType)) return false;
/*    */     
/* 69 */     NamedServiceType that = (NamedServiceType)o;
/*    */     
/* 71 */     if (!this.serviceType.equals(that.serviceType)) return false; 
/* 72 */     if (!this.udn.equals(that.udn)) return false;
/*    */     
/* 74 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 79 */     int result = this.udn.hashCode();
/* 80 */     result = 31 * result + this.serviceType.hashCode();
/* 81 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\NamedServiceType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */