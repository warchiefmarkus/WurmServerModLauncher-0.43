/*    */ package org.apache.http.impl.client;
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
/*    */ @Immutable
/*    */ public class LaxRedirectStrategy
/*    */   extends DefaultRedirectStrategy
/*    */ {
/* 49 */   private static final String[] REDIRECT_METHODS = new String[] { "GET", "POST", "HEAD" };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean isRedirectable(String method) {
/* 57 */     for (String m : REDIRECT_METHODS) {
/* 58 */       if (m.equalsIgnoreCase(method)) {
/* 59 */         return true;
/*    */       }
/*    */     } 
/* 62 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\LaxRedirectStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */