/*    */ package org.apache.http.cookie;
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
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class CookieRestrictionViolationException
/*    */   extends MalformedCookieException
/*    */ {
/*    */   private static final long serialVersionUID = 7371235577078589013L;
/*    */   
/*    */   public CookieRestrictionViolationException() {}
/*    */   
/*    */   public CookieRestrictionViolationException(String message) {
/* 58 */     super(message);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\cookie\CookieRestrictionViolationException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */