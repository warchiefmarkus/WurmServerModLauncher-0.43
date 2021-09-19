/*    */ package org.fourthline.cling.model;
/*    */ 
/*    */ import java.util.List;
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
/*    */ public class ValidationException
/*    */   extends Exception
/*    */ {
/*    */   public List<ValidationError> errors;
/*    */   
/*    */   public ValidationException(String s) {
/* 31 */     super(s);
/*    */   }
/*    */   
/*    */   public ValidationException(String s, Throwable throwable) {
/* 35 */     super(s, throwable);
/*    */   }
/*    */   
/*    */   public ValidationException(String s, List<ValidationError> errors) {
/* 39 */     super(s);
/* 40 */     this.errors = errors;
/*    */   }
/*    */   
/*    */   public List<ValidationError> getErrors() {
/* 44 */     return this.errors;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\ValidationException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */