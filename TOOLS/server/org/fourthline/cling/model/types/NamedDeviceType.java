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
/*    */ public class NamedDeviceType
/*    */ {
/*    */   private UDN udn;
/*    */   private DeviceType deviceType;
/*    */   
/*    */   public NamedDeviceType(UDN udn, DeviceType deviceType) {
/* 30 */     this.udn = udn;
/* 31 */     this.deviceType = deviceType;
/*    */   }
/*    */   
/*    */   public UDN getUdn() {
/* 35 */     return this.udn;
/*    */   }
/*    */   
/*    */   public DeviceType getDeviceType() {
/* 39 */     return this.deviceType;
/*    */   }
/*    */   public static NamedDeviceType valueOf(String s) throws InvalidValueException {
/*    */     UDN udn;
/* 43 */     String[] strings = s.split("::");
/* 44 */     if (strings.length != 2) {
/* 45 */       throw new InvalidValueException("Can't parse UDN::DeviceType from: " + s);
/*    */     }
/*    */ 
/*    */     
/*    */     try {
/* 50 */       udn = UDN.valueOf(strings[0]);
/* 51 */     } catch (Exception ex) {
/* 52 */       throw new InvalidValueException("Can't parse UDN: " + strings[0]);
/*    */     } 
/*    */     
/* 55 */     DeviceType deviceType = DeviceType.valueOf(strings[1]);
/* 56 */     return new NamedDeviceType(udn, deviceType);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     return getUdn().toString() + "::" + getDeviceType().toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 66 */     if (this == o) return true; 
/* 67 */     if (o == null || !(o instanceof NamedDeviceType)) return false;
/*    */     
/* 69 */     NamedDeviceType that = (NamedDeviceType)o;
/*    */     
/* 71 */     if (!this.deviceType.equals(that.deviceType)) return false; 
/* 72 */     if (!this.udn.equals(that.udn)) return false;
/*    */     
/* 74 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 79 */     int result = this.udn.hashCode();
/* 80 */     result = 31 * result + this.deviceType.hashCode();
/* 81 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\NamedDeviceType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */