/*    */ package org.apache.http.client;
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
/*    */ @Immutable
/*    */ public class CircularRedirectException
/*    */   extends RedirectException
/*    */ {
/*    */   private static final long serialVersionUID = 6830063487001091803L;
/*    */   
/*    */   public CircularRedirectException() {}
/*    */   
/*    */   public CircularRedirectException(String message) {
/* 55 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CircularRedirectException(String message, Throwable cause) {
/* 66 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\client\CircularRedirectException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */