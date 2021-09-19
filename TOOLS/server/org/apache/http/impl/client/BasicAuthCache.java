/*    */ package org.apache.http.impl.client;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.apache.http.HttpHost;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.auth.AuthScheme;
/*    */ import org.apache.http.client.AuthCache;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @NotThreadSafe
/*    */ public class BasicAuthCache
/*    */   implements AuthCache
/*    */ {
/* 51 */   private final HashMap<HttpHost, AuthScheme> map = new HashMap<HttpHost, AuthScheme>();
/*    */ 
/*    */   
/*    */   protected HttpHost getKey(HttpHost host) {
/* 55 */     if (host.getPort() <= 0) {
/* 56 */       int port = host.getSchemeName().equalsIgnoreCase("https") ? 443 : 80;
/* 57 */       return new HttpHost(host.getHostName(), port, host.getSchemeName());
/*    */     } 
/* 59 */     return host;
/*    */   }
/*    */ 
/*    */   
/*    */   public void put(HttpHost host, AuthScheme authScheme) {
/* 64 */     if (host == null) {
/* 65 */       throw new IllegalArgumentException("HTTP host may not be null");
/*    */     }
/* 67 */     this.map.put(getKey(host), authScheme);
/*    */   }
/*    */   
/*    */   public AuthScheme get(HttpHost host) {
/* 71 */     if (host == null) {
/* 72 */       throw new IllegalArgumentException("HTTP host may not be null");
/*    */     }
/* 74 */     return this.map.get(getKey(host));
/*    */   }
/*    */   
/*    */   public void remove(HttpHost host) {
/* 78 */     if (host == null) {
/* 79 */       throw new IllegalArgumentException("HTTP host may not be null");
/*    */     }
/* 81 */     this.map.remove(getKey(host));
/*    */   }
/*    */   
/*    */   public void clear() {
/* 85 */     this.map.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 90 */     return this.map.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\BasicAuthCache.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */