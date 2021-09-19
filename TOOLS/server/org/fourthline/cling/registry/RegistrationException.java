/*    */ package org.fourthline.cling.registry;
/*    */ 
/*    */ import java.util.List;
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
/*    */ public class RegistrationException
/*    */   extends RuntimeException
/*    */ {
/*    */   public List<ValidationError> errors;
/*    */   
/*    */   public RegistrationException(String s) {
/* 32 */     super(s);
/*    */   }
/*    */   
/*    */   public RegistrationException(String s, Throwable throwable) {
/* 36 */     super(s, throwable);
/*    */   }
/*    */   
/*    */   public RegistrationException(String s, List<ValidationError> errors) {
/* 40 */     super(s);
/* 41 */     this.errors = errors;
/*    */   }
/*    */   
/*    */   public List<ValidationError> getErrors() {
/* 45 */     return this.errors;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\registry\RegistrationException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */