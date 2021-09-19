/*     */ package org.apache.http.impl.conn.tsccm;
/*     */ 
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.impl.conn.AbstractPoolEntry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class BasicPoolEntry
/*     */   extends AbstractPoolEntry
/*     */ {
/*     */   private final long created;
/*     */   private long updated;
/*     */   private long validUntil;
/*     */   private long expiry;
/*     */   
/*     */   public BasicPoolEntry(ClientConnectionOperator op, HttpRoute route, ReferenceQueue<Object> queue) {
/*  56 */     super(op, route);
/*  57 */     if (route == null) {
/*  58 */       throw new IllegalArgumentException("HTTP route may not be null");
/*     */     }
/*  60 */     this.created = System.currentTimeMillis();
/*  61 */     this.validUntil = Long.MAX_VALUE;
/*  62 */     this.expiry = this.validUntil;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicPoolEntry(ClientConnectionOperator op, HttpRoute route) {
/*  73 */     this(op, route, -1L, TimeUnit.MILLISECONDS);
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
/*     */   public BasicPoolEntry(ClientConnectionOperator op, HttpRoute route, long connTTL, TimeUnit timeunit) {
/*  88 */     super(op, route);
/*  89 */     if (route == null) {
/*  90 */       throw new IllegalArgumentException("HTTP route may not be null");
/*     */     }
/*  92 */     this.created = System.currentTimeMillis();
/*  93 */     if (connTTL > 0L) {
/*  94 */       this.validUntil = this.created + timeunit.toMillis(connTTL);
/*     */     } else {
/*  96 */       this.validUntil = Long.MAX_VALUE;
/*     */     } 
/*  98 */     this.expiry = this.validUntil;
/*     */   }
/*     */   
/*     */   protected final OperatedClientConnection getConnection() {
/* 102 */     return this.connection;
/*     */   }
/*     */   
/*     */   protected final HttpRoute getPlannedRoute() {
/* 106 */     return this.route;
/*     */   }
/*     */   
/*     */   protected final BasicPoolEntryRef getWeakRef() {
/* 110 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void shutdownEntry() {
/* 115 */     super.shutdownEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCreated() {
/* 122 */     return this.created;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getUpdated() {
/* 129 */     return this.updated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getExpiry() {
/* 136 */     return this.expiry;
/*     */   }
/*     */   
/*     */   public long getValidUntil() {
/* 140 */     return this.validUntil;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateExpiry(long time, TimeUnit timeunit) {
/*     */     long newExpiry;
/* 147 */     this.updated = System.currentTimeMillis();
/*     */     
/* 149 */     if (time > 0L) {
/* 150 */       newExpiry = this.updated + timeunit.toMillis(time);
/*     */     } else {
/* 152 */       newExpiry = Long.MAX_VALUE;
/*     */     } 
/* 154 */     this.expiry = Math.min(this.validUntil, newExpiry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExpired(long now) {
/* 161 */     return (now >= this.expiry);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\tsccm\BasicPoolEntry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */