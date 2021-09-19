/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.net.ProxySelector;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.routing.HttpRoutePlanner;
/*     */ import org.apache.http.impl.DefaultConnectionReuseStrategy;
/*     */ import org.apache.http.impl.NoConnectionReuseStrategy;
/*     */ import org.apache.http.impl.conn.PoolingClientConnectionManager;
/*     */ import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
/*     */ import org.apache.http.impl.conn.SchemeRegistryFactory;
/*     */ import org.apache.http.params.HttpParams;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ public class SystemDefaultHttpClient
/*     */   extends DefaultHttpClient
/*     */ {
/*     */   public SystemDefaultHttpClient(HttpParams params) {
/* 109 */     super((ClientConnectionManager)null, params);
/*     */   }
/*     */   
/*     */   public SystemDefaultHttpClient() {
/* 113 */     super((ClientConnectionManager)null, (HttpParams)null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClientConnectionManager createClientConnectionManager() {
/* 118 */     PoolingClientConnectionManager connmgr = new PoolingClientConnectionManager(SchemeRegistryFactory.createSystemDefault());
/*     */     
/* 120 */     String s = System.getProperty("http.keepAlive", "true");
/* 121 */     if ("true".equalsIgnoreCase(s)) {
/* 122 */       s = System.getProperty("http.maxConnections", "5");
/* 123 */       int max = Integer.parseInt(s);
/* 124 */       connmgr.setDefaultMaxPerRoute(max);
/* 125 */       connmgr.setMaxTotal(2 * max);
/*     */     } 
/* 127 */     return (ClientConnectionManager)connmgr;
/*     */   }
/*     */ 
/*     */   
/*     */   protected HttpRoutePlanner createHttpRoutePlanner() {
/* 132 */     return (HttpRoutePlanner)new ProxySelectorRoutePlanner(getConnectionManager().getSchemeRegistry(), ProxySelector.getDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConnectionReuseStrategy createConnectionReuseStrategy() {
/* 138 */     String s = System.getProperty("http.keepAlive", "true");
/* 139 */     if ("true".equalsIgnoreCase(s)) {
/* 140 */       return (ConnectionReuseStrategy)new DefaultConnectionReuseStrategy();
/*     */     }
/* 142 */     return (ConnectionReuseStrategy)new NoConnectionReuseStrategy();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\SystemDefaultHttpClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */