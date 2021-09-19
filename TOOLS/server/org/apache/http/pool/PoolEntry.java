/*     */ package org.apache.http.pool;
/*     */ 
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.http.annotation.GuardedBy;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class PoolEntry<T, C>
/*     */ {
/*     */   private final String id;
/*     */   private final T route;
/*     */   private final C conn;
/*     */   private final long created;
/*     */   private final long validUnit;
/*     */   @GuardedBy("this")
/*     */   private long updated;
/*     */   @GuardedBy("this")
/*     */   private long expiry;
/*     */   private volatile Object state;
/*     */   
/*     */   public PoolEntry(String id, T route, C conn, long timeToLive, TimeUnit tunit) {
/*  80 */     if (route == null) {
/*  81 */       throw new IllegalArgumentException("Route may not be null");
/*     */     }
/*  83 */     if (conn == null) {
/*  84 */       throw new IllegalArgumentException("Connection may not be null");
/*     */     }
/*  86 */     if (tunit == null) {
/*  87 */       throw new IllegalArgumentException("Time unit may not be null");
/*     */     }
/*  89 */     this.id = id;
/*  90 */     this.route = route;
/*  91 */     this.conn = conn;
/*  92 */     this.created = System.currentTimeMillis();
/*  93 */     if (timeToLive > 0L) {
/*  94 */       this.validUnit = this.created + tunit.toMillis(timeToLive);
/*     */     } else {
/*  96 */       this.validUnit = Long.MAX_VALUE;
/*     */     } 
/*  98 */     this.expiry = this.validUnit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolEntry(String id, T route, C conn) {
/* 109 */     this(id, route, conn, 0L, TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */   public String getId() {
/* 113 */     return this.id;
/*     */   }
/*     */   
/*     */   public T getRoute() {
/* 117 */     return this.route;
/*     */   }
/*     */   
/*     */   public C getConnection() {
/* 121 */     return this.conn;
/*     */   }
/*     */   
/*     */   public long getCreated() {
/* 125 */     return this.created;
/*     */   }
/*     */   
/*     */   public long getValidUnit() {
/* 129 */     return this.validUnit;
/*     */   }
/*     */   
/*     */   public Object getState() {
/* 133 */     return this.state;
/*     */   }
/*     */   
/*     */   public void setState(Object state) {
/* 137 */     this.state = state;
/*     */   }
/*     */   
/*     */   public synchronized long getUpdated() {
/* 141 */     return this.updated;
/*     */   }
/*     */   
/*     */   public synchronized long getExpiry() {
/* 145 */     return this.expiry;
/*     */   }
/*     */   public synchronized void updateExpiry(long time, TimeUnit tunit) {
/*     */     long newExpiry;
/* 149 */     if (tunit == null) {
/* 150 */       throw new IllegalArgumentException("Time unit may not be null");
/*     */     }
/* 152 */     this.updated = System.currentTimeMillis();
/*     */     
/* 154 */     if (time > 0L) {
/* 155 */       newExpiry = this.updated + tunit.toMillis(time);
/*     */     } else {
/* 157 */       newExpiry = Long.MAX_VALUE;
/*     */     } 
/* 159 */     this.expiry = Math.min(newExpiry, this.validUnit);
/*     */   }
/*     */   
/*     */   public synchronized boolean isExpired(long now) {
/* 163 */     return (now >= this.expiry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void close();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isClosed();
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 179 */     StringBuilder buffer = new StringBuilder();
/* 180 */     buffer.append("[id:");
/* 181 */     buffer.append(this.id);
/* 182 */     buffer.append("][route:");
/* 183 */     buffer.append(this.route);
/* 184 */     buffer.append("][state:");
/* 185 */     buffer.append(this.state);
/* 186 */     buffer.append("]");
/* 187 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\pool\PoolEntry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */