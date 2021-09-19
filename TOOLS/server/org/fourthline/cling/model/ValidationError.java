/*    */ package org.fourthline.cling.model;
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
/*    */ public class ValidationError
/*    */ {
/*    */   private Class clazz;
/*    */   private String propertyName;
/*    */   private String message;
/*    */   
/*    */   public ValidationError(Class clazz, String message) {
/* 29 */     this.clazz = clazz;
/* 30 */     this.message = message;
/*    */   }
/*    */   
/*    */   public ValidationError(Class clazz, String propertyName, String message) {
/* 34 */     this.clazz = clazz;
/* 35 */     this.propertyName = propertyName;
/* 36 */     this.message = message;
/*    */   }
/*    */   
/*    */   public Class getClazz() {
/* 40 */     return this.clazz;
/*    */   }
/*    */   
/*    */   public String getPropertyName() {
/* 44 */     return this.propertyName;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 48 */     return this.message;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 53 */     return getClass().getSimpleName() + " (Class: " + 
/* 54 */       getClazz().getSimpleName() + ", propertyName: " + 
/* 55 */       getPropertyName() + "): " + this.message;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\ValidationError.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */