/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.http.client.BackoffManager;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.pool.ConnPoolControl;
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
/*     */ public class AIMDBackoffManager
/*     */   implements BackoffManager
/*     */ {
/*     */   private final ConnPoolControl<HttpRoute> connPerRoute;
/*     */   private final Clock clock;
/*     */   private final Map<HttpRoute, Long> lastRouteProbes;
/*     */   private final Map<HttpRoute, Long> lastRouteBackoffs;
/*  61 */   private long coolDown = 5000L;
/*  62 */   private double backoffFactor = 0.5D;
/*  63 */   private int cap = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AIMDBackoffManager(ConnPoolControl<HttpRoute> connPerRoute) {
/*  73 */     this(connPerRoute, new SystemClock());
/*     */   }
/*     */   
/*     */   AIMDBackoffManager(ConnPoolControl<HttpRoute> connPerRoute, Clock clock) {
/*  77 */     this.clock = clock;
/*  78 */     this.connPerRoute = connPerRoute;
/*  79 */     this.lastRouteProbes = new HashMap<HttpRoute, Long>();
/*  80 */     this.lastRouteBackoffs = new HashMap<HttpRoute, Long>();
/*     */   }
/*     */   
/*     */   public void backOff(HttpRoute route) {
/*  84 */     synchronized (this.connPerRoute) {
/*  85 */       int curr = this.connPerRoute.getMaxPerRoute(route);
/*  86 */       Long lastUpdate = getLastUpdate(this.lastRouteBackoffs, route);
/*  87 */       long now = this.clock.getCurrentTime();
/*  88 */       if (now - lastUpdate.longValue() < this.coolDown)
/*  89 */         return;  this.connPerRoute.setMaxPerRoute(route, getBackedOffPoolSize(curr));
/*  90 */       this.lastRouteBackoffs.put(route, Long.valueOf(now));
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getBackedOffPoolSize(int curr) {
/*  95 */     if (curr <= 1) return 1; 
/*  96 */     return (int)Math.floor(this.backoffFactor * curr);
/*     */   }
/*     */   
/*     */   public void probe(HttpRoute route) {
/* 100 */     synchronized (this.connPerRoute) {
/* 101 */       int curr = this.connPerRoute.getMaxPerRoute(route);
/* 102 */       int max = (curr >= this.cap) ? this.cap : (curr + 1);
/* 103 */       Long lastProbe = getLastUpdate(this.lastRouteProbes, route);
/* 104 */       Long lastBackoff = getLastUpdate(this.lastRouteBackoffs, route);
/* 105 */       long now = this.clock.getCurrentTime();
/* 106 */       if (now - lastProbe.longValue() < this.coolDown || now - lastBackoff.longValue() < this.coolDown)
/*     */         return; 
/* 108 */       this.connPerRoute.setMaxPerRoute(route, max);
/* 109 */       this.lastRouteProbes.put(route, Long.valueOf(now));
/*     */     } 
/*     */   }
/*     */   
/*     */   private Long getLastUpdate(Map<HttpRoute, Long> updates, HttpRoute route) {
/* 114 */     Long lastUpdate = updates.get(route);
/* 115 */     if (lastUpdate == null) lastUpdate = Long.valueOf(0L); 
/* 116 */     return lastUpdate;
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
/*     */   public void setBackoffFactor(double d) {
/* 129 */     if (d <= 0.0D || d >= 1.0D) {
/* 130 */       throw new IllegalArgumentException("backoffFactor must be 0.0 < f < 1.0");
/*     */     }
/* 132 */     this.backoffFactor = d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCooldownMillis(long l) {
/* 143 */     if (this.coolDown <= 0L) {
/* 144 */       throw new IllegalArgumentException("cooldownMillis must be positive");
/*     */     }
/* 146 */     this.coolDown = l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPerHostConnectionCap(int cap) {
/* 155 */     if (cap < 1) {
/* 156 */       throw new IllegalArgumentException("perHostConnectionCap must be >= 1");
/*     */     }
/* 158 */     this.cap = cap;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\AIMDBackoffManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */