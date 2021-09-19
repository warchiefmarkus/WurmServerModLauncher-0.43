/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.routing.RouteTracker;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ @Deprecated
/*     */ public abstract class AbstractPoolEntry
/*     */ {
/*     */   protected final ClientConnectionOperator connOperator;
/*     */   protected final OperatedClientConnection connection;
/*     */   protected volatile HttpRoute route;
/*     */   protected volatile Object state;
/*     */   protected volatile RouteTracker tracker;
/*     */   
/*     */   protected AbstractPoolEntry(ClientConnectionOperator connOperator, HttpRoute route) {
/*  90 */     if (connOperator == null) {
/*  91 */       throw new IllegalArgumentException("Connection operator may not be null");
/*     */     }
/*  93 */     this.connOperator = connOperator;
/*  94 */     this.connection = connOperator.createConnection();
/*  95 */     this.route = route;
/*  96 */     this.tracker = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getState() {
/* 105 */     return this.state;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setState(Object state) {
/* 114 */     this.state = state;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void open(HttpRoute route, HttpContext context, HttpParams params) throws IOException {
/* 130 */     if (route == null) {
/* 131 */       throw new IllegalArgumentException("Route must not be null.");
/*     */     }
/*     */     
/* 134 */     if (params == null) {
/* 135 */       throw new IllegalArgumentException("Parameters must not be null.");
/*     */     }
/*     */     
/* 138 */     if (this.tracker != null && this.tracker.isConnected()) {
/* 139 */       throw new IllegalStateException("Connection already open.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 148 */     this.tracker = new RouteTracker(route);
/* 149 */     HttpHost proxy = route.getProxyHost();
/*     */     
/* 151 */     this.connOperator.openConnection(this.connection, (proxy != null) ? proxy : route.getTargetHost(), route.getLocalAddress(), context, params);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 157 */     RouteTracker localTracker = this.tracker;
/*     */ 
/*     */ 
/*     */     
/* 161 */     if (localTracker == null) {
/* 162 */       throw new InterruptedIOException("Request aborted");
/*     */     }
/*     */     
/* 165 */     if (proxy == null) {
/* 166 */       localTracker.connectTarget(this.connection.isSecure());
/*     */     } else {
/* 168 */       localTracker.connectProxy(proxy, this.connection.isSecure());
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void tunnelTarget(boolean secure, HttpParams params) throws IOException {
/* 187 */     if (params == null) {
/* 188 */       throw new IllegalArgumentException("Parameters must not be null.");
/*     */     }
/*     */ 
/*     */     
/* 192 */     if (this.tracker == null || !this.tracker.isConnected()) {
/* 193 */       throw new IllegalStateException("Connection not open.");
/*     */     }
/* 195 */     if (this.tracker.isTunnelled()) {
/* 196 */       throw new IllegalStateException("Connection is already tunnelled.");
/*     */     }
/*     */ 
/*     */     
/* 200 */     this.connection.update(null, this.tracker.getTargetHost(), secure, params);
/*     */     
/* 202 */     this.tracker.tunnelTarget(secure);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void tunnelProxy(HttpHost next, boolean secure, HttpParams params) throws IOException {
/* 223 */     if (next == null) {
/* 224 */       throw new IllegalArgumentException("Next proxy must not be null.");
/*     */     }
/*     */     
/* 227 */     if (params == null) {
/* 228 */       throw new IllegalArgumentException("Parameters must not be null.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 233 */     if (this.tracker == null || !this.tracker.isConnected()) {
/* 234 */       throw new IllegalStateException("Connection not open.");
/*     */     }
/*     */     
/* 237 */     this.connection.update(null, next, secure, params);
/* 238 */     this.tracker.tunnelProxy(next, secure);
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
/*     */ 
/*     */   
/*     */   public void layerProtocol(HttpContext context, HttpParams params) throws IOException {
/* 253 */     if (params == null) {
/* 254 */       throw new IllegalArgumentException("Parameters must not be null.");
/*     */     }
/*     */ 
/*     */     
/* 258 */     if (this.tracker == null || !this.tracker.isConnected()) {
/* 259 */       throw new IllegalStateException("Connection not open.");
/*     */     }
/* 261 */     if (!this.tracker.isTunnelled())
/*     */     {
/* 263 */       throw new IllegalStateException("Protocol layering without a tunnel not supported.");
/*     */     }
/*     */     
/* 266 */     if (this.tracker.isLayered()) {
/* 267 */       throw new IllegalStateException("Multiple protocol layering not supported.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 277 */     HttpHost target = this.tracker.getTargetHost();
/*     */     
/* 279 */     this.connOperator.updateSecureConnection(this.connection, target, context, params);
/*     */ 
/*     */     
/* 282 */     this.tracker.layerProtocol(this.connection.isSecure());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void shutdownEntry() {
/* 293 */     this.tracker = null;
/* 294 */     this.state = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\AbstractPoolEntry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */