/*     */ package org.apache.http.conn.params;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.routing.HttpRoute;
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
/*     */ @Deprecated
/*     */ @ThreadSafe
/*     */ public final class ConnPerRouteBean
/*     */   implements ConnPerRoute
/*     */ {
/*     */   public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 2;
/*     */   private final ConcurrentHashMap<HttpRoute, Integer> maxPerHostMap;
/*     */   private volatile int defaultMax;
/*     */   
/*     */   public ConnPerRouteBean(int defaultMax) {
/*  60 */     this.maxPerHostMap = new ConcurrentHashMap<HttpRoute, Integer>();
/*  61 */     setDefaultMaxPerRoute(defaultMax);
/*     */   }
/*     */   
/*     */   public ConnPerRouteBean() {
/*  65 */     this(2);
/*     */   }
/*     */   
/*     */   public int getDefaultMax() {
/*  69 */     return this.defaultMax;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDefaultMaxPerRoute() {
/*  76 */     return this.defaultMax;
/*     */   }
/*     */   
/*     */   public void setDefaultMaxPerRoute(int max) {
/*  80 */     if (max < 1) {
/*  81 */       throw new IllegalArgumentException("The maximum must be greater than 0.");
/*     */     }
/*     */     
/*  84 */     this.defaultMax = max;
/*     */   }
/*     */   
/*     */   public void setMaxForRoute(HttpRoute route, int max) {
/*  88 */     if (route == null) {
/*  89 */       throw new IllegalArgumentException("HTTP route may not be null.");
/*     */     }
/*     */     
/*  92 */     if (max < 1) {
/*  93 */       throw new IllegalArgumentException("The maximum must be greater than 0.");
/*     */     }
/*     */     
/*  96 */     this.maxPerHostMap.put(route, Integer.valueOf(max));
/*     */   }
/*     */   
/*     */   public int getMaxForRoute(HttpRoute route) {
/* 100 */     if (route == null) {
/* 101 */       throw new IllegalArgumentException("HTTP route may not be null.");
/*     */     }
/*     */     
/* 104 */     Integer max = this.maxPerHostMap.get(route);
/* 105 */     if (max != null) {
/* 106 */       return max.intValue();
/*     */     }
/* 108 */     return this.defaultMax;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxForRoutes(Map<HttpRoute, Integer> map) {
/* 113 */     if (map == null) {
/*     */       return;
/*     */     }
/* 116 */     this.maxPerHostMap.clear();
/* 117 */     this.maxPerHostMap.putAll(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 122 */     return this.maxPerHostMap.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\conn\params\ConnPerRouteBean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */