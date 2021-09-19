/*    */ package org.fourthline.cling.model.meta;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.fourthline.cling.model.Validatable;
/*    */ import org.fourthline.cling.model.ValidationError;
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
/*    */ public class DeviceIdentity
/*    */   implements Validatable
/*    */ {
/*    */   private final UDN udn;
/*    */   private final Integer maxAgeSeconds;
/*    */   
/*    */   public DeviceIdentity(UDN udn, DeviceIdentity template) {
/* 37 */     this.udn = udn;
/* 38 */     this.maxAgeSeconds = template.getMaxAgeSeconds();
/*    */   }
/*    */   
/*    */   public DeviceIdentity(UDN udn) {
/* 42 */     this.udn = udn;
/* 43 */     this.maxAgeSeconds = Integer.valueOf(1800);
/*    */   }
/*    */   
/*    */   public DeviceIdentity(UDN udn, Integer maxAgeSeconds) {
/* 47 */     this.udn = udn;
/* 48 */     this.maxAgeSeconds = maxAgeSeconds;
/*    */   }
/*    */   
/*    */   public UDN getUdn() {
/* 52 */     return this.udn;
/*    */   }
/*    */   
/*    */   public Integer getMaxAgeSeconds() {
/* 56 */     return this.maxAgeSeconds;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 61 */     if (this == o) return true; 
/* 62 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 64 */     DeviceIdentity that = (DeviceIdentity)o;
/*    */     
/* 66 */     if (!this.udn.equals(that.udn)) return false;
/*    */     
/* 68 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 73 */     return this.udn.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 78 */     return "(" + getClass().getSimpleName() + ") UDN: " + getUdn();
/*    */   }
/*    */ 
/*    */   
/*    */   public List<ValidationError> validate() {
/* 83 */     List<ValidationError> errors = new ArrayList<>();
/*    */     
/* 85 */     if (getUdn() == null) {
/* 86 */       errors.add(new ValidationError(
/* 87 */             getClass(), "major", "Device has no UDN"));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 93 */     return errors;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\meta\DeviceIdentity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */