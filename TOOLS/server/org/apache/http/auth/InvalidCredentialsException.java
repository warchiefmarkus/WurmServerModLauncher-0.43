/*    */ package org.apache.http.auth;
/*    */ 
/*    */ import org.apache.http.annotation.Immutable;
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
/*    */ @Immutable
/*    */ public class InvalidCredentialsException
/*    */   extends AuthenticationException
/*    */ {
/*    */   private static final long serialVersionUID = -4834003835215460648L;
/*    */   
/*    */   public InvalidCredentialsException() {}
/*    */   
/*    */   public InvalidCredentialsException(String message) {
/* 56 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InvalidCredentialsException(String message, Throwable cause) {
/* 67 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\auth\InvalidCredentialsException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */