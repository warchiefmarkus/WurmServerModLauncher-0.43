/*    */ package org.apache.http.impl.client;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Queue;
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HttpHost;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.auth.AuthScheme;
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
/*    */ @Immutable
/*    */ public class ProxyAuthenticationStrategy
/*    */   extends AuthenticationStrategyImpl
/*    */ {
/*    */   public ProxyAuthenticationStrategy() {
/* 45 */     super(407, "Proxy-Authenticate", "http.auth.proxy-scheme-pref");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\ProxyAuthenticationStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */