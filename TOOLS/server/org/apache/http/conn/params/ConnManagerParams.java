/*     */ package org.apache.http.conn.params;
/*     */ 
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.conn.routing.HttpRoute;
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
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public final class ConnManagerParams
/*     */   implements ConnManagerPNames
/*     */ {
/*     */   public static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 20;
/*     */   
/*     */   public static long getTimeout(HttpParams params) {
/*  62 */     if (params == null) {
/*  63 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*  65 */     return params.getLongParameter("http.conn-manager.timeout", 0L);
/*     */   }
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
/*     */   public static void setTimeout(HttpParams params, long timeout) {
/*  78 */     if (params == null) {
/*  79 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*  81 */     params.setLongParameter("http.conn-manager.timeout", timeout);
/*     */   }
/*     */ 
/*     */   
/*  85 */   private static final ConnPerRoute DEFAULT_CONN_PER_ROUTE = new ConnPerRoute()
/*     */     {
/*     */       public int getMaxForRoute(HttpRoute route) {
/*  88 */         return 2;
/*     */       }
/*     */     };
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
/*     */   public static void setMaxConnectionsPerRoute(HttpParams params, ConnPerRoute connPerRoute) {
/* 102 */     if (params == null) {
/* 103 */       throw new IllegalArgumentException("HTTP parameters must not be null.");
/*     */     }
/*     */     
/* 106 */     params.setParameter("http.conn-manager.max-per-route", connPerRoute);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ConnPerRoute getMaxConnectionsPerRoute(HttpParams params) {
/* 117 */     if (params == null) {
/* 118 */       throw new IllegalArgumentException("HTTP parameters must not be null.");
/*     */     }
/*     */     
/* 121 */     ConnPerRoute connPerRoute = (ConnPerRoute)params.getParameter("http.conn-manager.max-per-route");
/* 122 */     if (connPerRoute == null) {
/* 123 */       connPerRoute = DEFAULT_CONN_PER_ROUTE;
/*     */     }
/* 125 */     return connPerRoute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setMaxTotalConnections(HttpParams params, int maxTotalConnections) {
/* 137 */     if (params == null) {
/* 138 */       throw new IllegalArgumentException("HTTP parameters must not be null.");
/*     */     }
/*     */     
/* 141 */     params.setIntParameter("http.conn-manager.max-total", maxTotalConnections);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getMaxTotalConnections(HttpParams params) {
/* 153 */     if (params == null) {
/* 154 */       throw new IllegalArgumentException("HTTP parameters must not be null.");
/*     */     }
/*     */     
/* 157 */     return params.getIntParameter("http.conn-manager.max-total", 20);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\conn\params\ConnManagerParams.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */