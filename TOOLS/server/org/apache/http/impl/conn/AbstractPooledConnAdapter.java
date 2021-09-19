/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
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
/*     */ @Deprecated
/*     */ public abstract class AbstractPooledConnAdapter
/*     */   extends AbstractClientConnAdapter
/*     */ {
/*     */   protected volatile AbstractPoolEntry poolEntry;
/*     */   
/*     */   protected AbstractPooledConnAdapter(ClientConnectionManager manager, AbstractPoolEntry entry) {
/*  66 */     super(manager, entry.connection);
/*  67 */     this.poolEntry = entry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractPoolEntry getPoolEntry() {
/*  78 */     return this.poolEntry;
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
/*     */   protected void assertValid(AbstractPoolEntry entry) {
/*  90 */     if (isReleased() || entry == null) {
/*  91 */       throw new ConnectionShutdownException();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void assertAttached() {
/*  99 */     if (this.poolEntry == null) {
/* 100 */       throw new ConnectionShutdownException();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void detach() {
/* 110 */     this.poolEntry = null;
/* 111 */     super.detach();
/*     */   }
/*     */   
/*     */   public HttpRoute getRoute() {
/* 115 */     AbstractPoolEntry entry = getPoolEntry();
/* 116 */     assertValid(entry);
/* 117 */     return (entry.tracker == null) ? null : entry.tracker.toRoute();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void open(HttpRoute route, HttpContext context, HttpParams params) throws IOException {
/* 123 */     AbstractPoolEntry entry = getPoolEntry();
/* 124 */     assertValid(entry);
/* 125 */     entry.open(route, context, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tunnelTarget(boolean secure, HttpParams params) throws IOException {
/* 130 */     AbstractPoolEntry entry = getPoolEntry();
/* 131 */     assertValid(entry);
/* 132 */     entry.tunnelTarget(secure, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tunnelProxy(HttpHost next, boolean secure, HttpParams params) throws IOException {
/* 137 */     AbstractPoolEntry entry = getPoolEntry();
/* 138 */     assertValid(entry);
/* 139 */     entry.tunnelProxy(next, secure, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public void layerProtocol(HttpContext context, HttpParams params) throws IOException {
/* 144 */     AbstractPoolEntry entry = getPoolEntry();
/* 145 */     assertValid(entry);
/* 146 */     entry.layerProtocol(context, params);
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 150 */     AbstractPoolEntry entry = getPoolEntry();
/* 151 */     if (entry != null) {
/* 152 */       entry.shutdownEntry();
/*     */     }
/* 154 */     OperatedClientConnection conn = getWrappedConnection();
/* 155 */     if (conn != null) {
/* 156 */       conn.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void shutdown() throws IOException {
/* 161 */     AbstractPoolEntry entry = getPoolEntry();
/* 162 */     if (entry != null) {
/* 163 */       entry.shutdownEntry();
/*     */     }
/* 165 */     OperatedClientConnection conn = getWrappedConnection();
/* 166 */     if (conn != null) {
/* 167 */       conn.shutdown();
/*     */     }
/*     */   }
/*     */   
/*     */   public Object getState() {
/* 172 */     AbstractPoolEntry entry = getPoolEntry();
/* 173 */     assertValid(entry);
/* 174 */     return entry.getState();
/*     */   }
/*     */   
/*     */   public void setState(Object state) {
/* 178 */     AbstractPoolEntry entry = getPoolEntry();
/* 179 */     assertValid(entry);
/* 180 */     entry.setState(state);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\AbstractPooledConnAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */