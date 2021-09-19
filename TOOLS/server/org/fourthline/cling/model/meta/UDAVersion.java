/*    */ package org.fourthline.cling.model.meta;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.fourthline.cling.model.Validatable;
/*    */ import org.fourthline.cling.model.ValidationError;
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
/*    */ public class UDAVersion
/*    */   implements Validatable
/*    */ {
/* 34 */   private int major = 1;
/* 35 */   private int minor = 0;
/*    */ 
/*    */   
/*    */   public UDAVersion() {}
/*    */   
/*    */   public UDAVersion(int major, int minor) {
/* 41 */     this.major = major;
/* 42 */     this.minor = minor;
/*    */   }
/*    */   
/*    */   public int getMajor() {
/* 46 */     return this.major;
/*    */   }
/*    */   
/*    */   public int getMinor() {
/* 50 */     return this.minor;
/*    */   }
/*    */   
/*    */   public List<ValidationError> validate() {
/* 54 */     List<ValidationError> errors = new ArrayList<>();
/*    */     
/* 56 */     if (getMajor() != 1) {
/* 57 */       errors.add(new ValidationError(
/* 58 */             getClass(), "major", "UDA major spec version must be 1"));
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 63 */     if (getMajor() < 0) {
/* 64 */       errors.add(new ValidationError(
/* 65 */             getClass(), "minor", "UDA minor spec version must be equal or greater 0"));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 71 */     return errors;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\meta\UDAVersion.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */