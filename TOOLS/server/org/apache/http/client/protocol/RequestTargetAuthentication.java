/*    */ package org.apache.http.client.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.auth.AuthState;
/*    */ import org.apache.http.protocol.HttpContext;
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
/*    */ public class RequestTargetAuthentication
/*    */   extends RequestAuthenticationBase
/*    */ {
/*    */   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
/* 54 */     if (request == null) {
/* 55 */       throw new IllegalArgumentException("HTTP request may not be null");
/*    */     }
/* 57 */     if (context == null) {
/* 58 */       throw new IllegalArgumentException("HTTP context may not be null");
/*    */     }
/*    */     
/* 61 */     String method = request.getRequestLine().getMethod();
/* 62 */     if (method.equalsIgnoreCase("CONNECT")) {
/*    */       return;
/*    */     }
/*    */     
/* 66 */     if (request.containsHeader("Authorization")) {
/*    */       return;
/*    */     }
/*    */ 
/*    */     
/* 71 */     AuthState authState = (AuthState)context.getAttribute("http.auth.target-scope");
/*    */     
/* 73 */     if (authState == null) {
/* 74 */       this.log.debug("Target auth state not set in the context");
/*    */       return;
/*    */     } 
/* 77 */     if (this.log.isDebugEnabled()) {
/* 78 */       this.log.debug("Target auth state: " + authState.getState());
/*    */     }
/* 80 */     process(authState, request, context);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\client\protocol\RequestTargetAuthentication.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */