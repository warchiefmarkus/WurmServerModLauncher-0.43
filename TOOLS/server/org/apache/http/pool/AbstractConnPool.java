/*     */ package org.apache.http.pool;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.concurrent.FutureCallback;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class AbstractConnPool<T, C, E extends PoolEntry<T, C>>
/*     */   implements ConnPool<T, E>, ConnPoolControl<T>
/*     */ {
/*     */   private final Lock lock;
/*     */   private final ConnFactory<T, C> connFactory;
/*     */   private final Map<T, RouteSpecificPool<T, C, E>> routeToPool;
/*     */   private final Set<E> leased;
/*     */   private final LinkedList<E> available;
/*     */   private final LinkedList<PoolEntryFuture<E>> pending;
/*     */   private final Map<T, Integer> maxPerRoute;
/*     */   private volatile boolean isShutDown;
/*     */   private volatile int defaultMaxPerRoute;
/*     */   private volatile int maxTotal;
/*     */   
/*     */   public AbstractConnPool(ConnFactory<T, C> connFactory, int defaultMaxPerRoute, int maxTotal) {
/*  82 */     if (connFactory == null) {
/*  83 */       throw new IllegalArgumentException("Connection factory may not null");
/*     */     }
/*  85 */     if (defaultMaxPerRoute <= 0) {
/*  86 */       throw new IllegalArgumentException("Max per route value may not be negative or zero");
/*     */     }
/*  88 */     if (maxTotal <= 0) {
/*  89 */       throw new IllegalArgumentException("Max total value may not be negative or zero");
/*     */     }
/*  91 */     this.lock = new ReentrantLock();
/*  92 */     this.connFactory = connFactory;
/*  93 */     this.routeToPool = new HashMap<T, RouteSpecificPool<T, C, E>>();
/*  94 */     this.leased = new HashSet<E>();
/*  95 */     this.available = new LinkedList<E>();
/*  96 */     this.pending = new LinkedList<PoolEntryFuture<E>>();
/*  97 */     this.maxPerRoute = new HashMap<T, Integer>();
/*  98 */     this.defaultMaxPerRoute = defaultMaxPerRoute;
/*  99 */     this.maxTotal = maxTotal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isShutdown() {
/* 108 */     return this.isShutDown;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() throws IOException {
/* 115 */     if (this.isShutDown) {
/*     */       return;
/*     */     }
/* 118 */     this.isShutDown = true;
/* 119 */     this.lock.lock();
/*     */     try {
/* 121 */       for (PoolEntry poolEntry : this.available) {
/* 122 */         poolEntry.close();
/*     */       }
/* 124 */       for (PoolEntry poolEntry : this.leased) {
/* 125 */         poolEntry.close();
/*     */       }
/* 127 */       for (RouteSpecificPool<T, C, E> pool : this.routeToPool.values()) {
/* 128 */         pool.shutdown();
/*     */       }
/* 130 */       this.routeToPool.clear();
/* 131 */       this.leased.clear();
/* 132 */       this.available.clear();
/*     */     } finally {
/* 134 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private RouteSpecificPool<T, C, E> getPool(final T route) {
/* 139 */     RouteSpecificPool<T, C, E> pool = this.routeToPool.get(route);
/* 140 */     if (pool == null) {
/* 141 */       pool = new RouteSpecificPool<T, C, E>(route)
/*     */         {
/*     */           protected E createEntry(C conn)
/*     */           {
/* 145 */             return AbstractConnPool.this.createEntry(route, conn);
/*     */           }
/*     */         };
/*     */       
/* 149 */       this.routeToPool.put(route, pool);
/*     */     } 
/* 151 */     return pool;
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
/*     */   public Future<E> lease(final T route, final Object state, FutureCallback<E> callback) {
/* 163 */     if (route == null) {
/* 164 */       throw new IllegalArgumentException("Route may not be null");
/*     */     }
/* 166 */     if (this.isShutDown) {
/* 167 */       throw new IllegalStateException("Connection pool shut down");
/*     */     }
/* 169 */     return new PoolEntryFuture<E>(this.lock, callback)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/*     */         public E getPoolEntry(long timeout, TimeUnit tunit) throws InterruptedException, TimeoutException, IOException
/*     */         {
/* 176 */           return AbstractConnPool.this.getPoolEntryBlocking((T)route, state, timeout, tunit, this);
/*     */         }
/*     */       };
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
/*     */   public Future<E> lease(T route, Object state) {
/* 199 */     return lease(route, state, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private E getPoolEntryBlocking(T route, Object state, long timeout, TimeUnit tunit, PoolEntryFuture<E> future) throws IOException, InterruptedException, TimeoutException {
/* 208 */     Date deadline = null;
/* 209 */     if (timeout > 0L) {
/* 210 */       deadline = new Date(System.currentTimeMillis() + tunit.toMillis(timeout));
/*     */     }
/*     */ 
/*     */     
/* 214 */     this.lock.lock();
/*     */     try {
/* 216 */       RouteSpecificPool<T, C, E> pool = getPool(route);
/* 217 */       E entry = null;
/* 218 */       while (entry == null) {
/* 219 */         if (this.isShutDown) {
/* 220 */           throw new IllegalStateException("Connection pool shut down");
/*     */         }
/*     */         while (true) {
/* 223 */           entry = pool.getFree(state);
/* 224 */           if (entry == null) {
/*     */             break;
/*     */           }
/* 227 */           if (entry.isClosed() || entry.isExpired(System.currentTimeMillis())) {
/* 228 */             entry.close();
/* 229 */             this.available.remove(entry);
/* 230 */             pool.free(entry, false);
/*     */             continue;
/*     */           } 
/*     */           break;
/*     */         } 
/* 235 */         if (entry != null) {
/* 236 */           this.available.remove(entry);
/* 237 */           this.leased.add(entry);
/* 238 */           return entry;
/*     */         } 
/*     */ 
/*     */         
/* 242 */         int maxPerRoute = getMax(route);
/*     */         
/* 244 */         int excess = Math.max(0, pool.getAllocatedCount() + 1 - maxPerRoute);
/* 245 */         if (excess > 0) {
/* 246 */           for (int i = 0; i < excess; i++) {
/* 247 */             E lastUsed = pool.getLastUsed();
/* 248 */             if (lastUsed == null) {
/*     */               break;
/*     */             }
/* 251 */             lastUsed.close();
/* 252 */             this.available.remove(lastUsed);
/* 253 */             pool.remove(lastUsed);
/*     */           } 
/*     */         }
/*     */         
/* 257 */         if (pool.getAllocatedCount() < maxPerRoute) {
/* 258 */           int totalUsed = this.leased.size();
/* 259 */           int freeCapacity = Math.max(this.maxTotal - totalUsed, 0);
/* 260 */           if (freeCapacity > 0) {
/* 261 */             int totalAvailable = this.available.size();
/* 262 */             if (totalAvailable > freeCapacity - 1 && 
/* 263 */               !this.available.isEmpty()) {
/* 264 */               PoolEntry poolEntry = (PoolEntry)this.available.removeLast();
/* 265 */               poolEntry.close();
/* 266 */               RouteSpecificPool<T, C, E> otherpool = getPool((T)poolEntry.getRoute());
/* 267 */               otherpool.remove((E)poolEntry);
/*     */             } 
/*     */             
/* 270 */             C conn = this.connFactory.create(route);
/* 271 */             entry = pool.add(conn);
/* 272 */             this.leased.add(entry);
/* 273 */             return entry;
/*     */           } 
/*     */         } 
/*     */         
/* 277 */         boolean success = false;
/*     */         try {
/* 279 */           pool.queue(future);
/* 280 */           this.pending.add(future);
/* 281 */           success = future.await(deadline);
/*     */         
/*     */         }
/*     */         finally {
/*     */ 
/*     */           
/* 287 */           pool.unqueue(future);
/* 288 */           this.pending.remove(future);
/*     */         } 
/*     */         
/* 291 */         if (!success && deadline != null && deadline.getTime() <= System.currentTimeMillis()) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 296 */       throw new TimeoutException("Timeout waiting for connection");
/*     */     } finally {
/* 298 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void notifyPending(RouteSpecificPool<T, C, E> pool) {
/* 303 */     PoolEntryFuture<E> future = pool.nextPending();
/* 304 */     if (future != null) {
/* 305 */       this.pending.remove(future);
/*     */     } else {
/* 307 */       future = this.pending.poll();
/*     */     } 
/* 309 */     if (future != null) {
/* 310 */       future.wakeup();
/*     */     }
/*     */   }
/*     */   
/*     */   public void release(E entry, boolean reusable) {
/* 315 */     this.lock.lock();
/*     */     try {
/* 317 */       if (this.leased.remove(entry)) {
/* 318 */         RouteSpecificPool<T, C, E> pool = getPool((T)entry.getRoute());
/* 319 */         pool.free(entry, reusable);
/* 320 */         if (reusable && !this.isShutDown) {
/* 321 */           this.available.addFirst(entry);
/*     */         } else {
/* 323 */           entry.close();
/*     */         } 
/* 325 */         notifyPending(pool);
/*     */       } 
/*     */     } finally {
/* 328 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getMax(T route) {
/* 333 */     Integer v = this.maxPerRoute.get(route);
/* 334 */     if (v != null) {
/* 335 */       return v.intValue();
/*     */     }
/* 337 */     return this.defaultMaxPerRoute;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxTotal(int max) {
/* 342 */     if (max <= 0) {
/* 343 */       throw new IllegalArgumentException("Max value may not be negative or zero");
/*     */     }
/* 345 */     this.lock.lock();
/*     */     try {
/* 347 */       this.maxTotal = max;
/*     */     } finally {
/* 349 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getMaxTotal() {
/* 354 */     this.lock.lock();
/*     */     try {
/* 356 */       return this.maxTotal;
/*     */     } finally {
/* 358 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDefaultMaxPerRoute(int max) {
/* 363 */     if (max <= 0) {
/* 364 */       throw new IllegalArgumentException("Max value may not be negative or zero");
/*     */     }
/* 366 */     this.lock.lock();
/*     */     try {
/* 368 */       this.defaultMaxPerRoute = max;
/*     */     } finally {
/* 370 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getDefaultMaxPerRoute() {
/* 375 */     this.lock.lock();
/*     */     try {
/* 377 */       return this.defaultMaxPerRoute;
/*     */     } finally {
/* 379 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setMaxPerRoute(T route, int max) {
/* 384 */     if (route == null) {
/* 385 */       throw new IllegalArgumentException("Route may not be null");
/*     */     }
/* 387 */     if (max <= 0) {
/* 388 */       throw new IllegalArgumentException("Max value may not be negative or zero");
/*     */     }
/* 390 */     this.lock.lock();
/*     */     try {
/* 392 */       this.maxPerRoute.put(route, Integer.valueOf(max));
/*     */     } finally {
/* 394 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getMaxPerRoute(T route) {
/* 399 */     if (route == null) {
/* 400 */       throw new IllegalArgumentException("Route may not be null");
/*     */     }
/* 402 */     this.lock.lock();
/*     */     try {
/* 404 */       return getMax(route);
/*     */     } finally {
/* 406 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public PoolStats getTotalStats() {
/* 411 */     this.lock.lock();
/*     */     try {
/* 413 */       return new PoolStats(this.leased.size(), this.pending.size(), this.available.size(), this.maxTotal);
/*     */     
/*     */     }
/*     */     finally {
/*     */ 
/*     */       
/* 419 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public PoolStats getStats(T route) {
/* 424 */     if (route == null) {
/* 425 */       throw new IllegalArgumentException("Route may not be null");
/*     */     }
/* 427 */     this.lock.lock();
/*     */     try {
/* 429 */       RouteSpecificPool<T, C, E> pool = getPool(route);
/* 430 */       return new PoolStats(pool.getLeasedCount(), pool.getPendingCount(), pool.getAvailableCount(), getMax(route));
/*     */     
/*     */     }
/*     */     finally {
/*     */ 
/*     */       
/* 436 */       this.lock.unlock();
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
/*     */   public void closeIdle(long idletime, TimeUnit tunit) {
/* 448 */     if (tunit == null) {
/* 449 */       throw new IllegalArgumentException("Time unit must not be null.");
/*     */     }
/* 451 */     long time = tunit.toMillis(idletime);
/* 452 */     if (time < 0L) {
/* 453 */       time = 0L;
/*     */     }
/* 455 */     long deadline = System.currentTimeMillis() - time;
/* 456 */     this.lock.lock();
/*     */     try {
/* 458 */       Iterator<E> it = this.available.iterator();
/* 459 */       while (it.hasNext()) {
/* 460 */         PoolEntry poolEntry = (PoolEntry)it.next();
/* 461 */         if (poolEntry.getUpdated() <= deadline) {
/* 462 */           poolEntry.close();
/* 463 */           RouteSpecificPool<T, C, E> pool = getPool((T)poolEntry.getRoute());
/* 464 */           pool.remove((E)poolEntry);
/* 465 */           it.remove();
/* 466 */           notifyPending(pool);
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 470 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeExpired() {
/* 478 */     long now = System.currentTimeMillis();
/* 479 */     this.lock.lock();
/*     */     try {
/* 481 */       Iterator<E> it = this.available.iterator();
/* 482 */       while (it.hasNext()) {
/* 483 */         PoolEntry poolEntry = (PoolEntry)it.next();
/* 484 */         if (poolEntry.isExpired(now)) {
/* 485 */           poolEntry.close();
/* 486 */           RouteSpecificPool<T, C, E> pool = getPool((T)poolEntry.getRoute());
/* 487 */           pool.remove((E)poolEntry);
/* 488 */           it.remove();
/* 489 */           notifyPending(pool);
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 493 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 499 */     StringBuilder buffer = new StringBuilder();
/* 500 */     buffer.append("[leased: ");
/* 501 */     buffer.append(this.leased);
/* 502 */     buffer.append("][available: ");
/* 503 */     buffer.append(this.available);
/* 504 */     buffer.append("][pending: ");
/* 505 */     buffer.append(this.pending);
/* 506 */     buffer.append("]");
/* 507 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   protected abstract E createEntry(T paramT, C paramC);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\pool\AbstractConnPool.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */