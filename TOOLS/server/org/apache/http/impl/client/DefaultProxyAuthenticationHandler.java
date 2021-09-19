/*    */ package org.apache.http.impl.client;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.auth.MalformedChallengeException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ @Immutable
/*    */ public class DefaultProxyAuthenticationHandler
/*    */   extends AbstractAuthenticationHandler
/*    */ {
/*    */   public boolean isAuthenticationRequested(HttpResponse response, HttpContext context) {
/* 63 */     if (response == null) {
/* 64 */       throw new IllegalArgumentException("HTTP response may not be null");
/*    */     }
/* 66 */     int status = response.getStatusLine().getStatusCode();
/* 67 */     return (status == 407);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Header> getChallenges(HttpResponse response, HttpContext context) throws MalformedChallengeException {
/* 73 */     if (response == null) {
/* 74 */       throw new IllegalArgumentException("HTTP response may not be null");
/*    */     }
/* 76 */     Header[] headers = response.getHeaders("Proxy-Authenticate");
/* 77 */     return parseChallenges(headers);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected List<String> getAuthPreferences(HttpResponse response, HttpContext context) {
/* 85 */     List<String> authpref = (List<String>)response.getParams().getParameter("http.auth.proxy-scheme-pref");
/*    */     
/* 87 */     if (authpref != null) {
/* 88 */       return authpref;
/*    */     }
/* 90 */     return super.getAuthPreferences(response, context);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\DefaultProxyAuthenticationHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */