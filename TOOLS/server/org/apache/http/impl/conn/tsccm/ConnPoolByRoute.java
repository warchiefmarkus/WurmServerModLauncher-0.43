/*     */ package org.apache.http.impl.conn.tsccm;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.ConnectionPoolTimeoutException;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.conn.params.ConnManagerParams;
/*     */ import org.apache.http.conn.params.ConnPerRoute;
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
/*     */ @Deprecated
/*     */ public class ConnPoolByRoute
/*     */   extends AbstractConnPool
/*     */ {
/*  71 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private final Lock poolLock;
/*     */ 
/*     */   
/*     */   protected final ClientConnectionOperator operator;
/*     */ 
/*     */   
/*     */   protected final ConnPerRoute connPerRoute;
/*     */ 
/*     */   
/*     */   protected final Set<BasicPoolEntry> leasedConnections;
/*     */ 
/*     */   
/*     */   protected final Queue<BasicPoolEntry> freeConnections;
/*     */ 
/*     */   
/*     */   protected final Queue<WaitingThread> waitingThreads;
/*     */ 
/*     */   
/*     */   protected final Map<HttpRoute, RouteSpecificPool> routeToPool;
/*     */ 
/*     */   
/*     */   private final long connTTL;
/*     */ 
/*     */   
/*     */   private final TimeUnit connTTLTimeUnit;
/*     */ 
/*     */   
/*     */   protected volatile boolean shutdown;
/*     */ 
/*     */   
/*     */   protected volatile int maxTotalConnections;
/*     */ 
/*     */   
/*     */   protected volatile int numConnections;
/*     */ 
/*     */ 
/*     */   
/*     */   public ConnPoolByRoute(ClientConnectionOperator operator, ConnPerRoute connPerRoute, int maxTotalConnections) {
/* 112 */     this(operator, connPerRoute, maxTotalConnections, -1L, TimeUnit.MILLISECONDS);
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
/*     */   public ConnPoolByRoute(ClientConnectionOperator operator, ConnPerRoute connPerRoute, int maxTotalConnections, long connTTL, TimeUnit connTTLTimeUnit) {
/* 125 */     if (operator == null) {
/* 126 */       throw new IllegalArgumentException("Connection operator may not be null");
/*     */     }
/* 128 */     if (connPerRoute == null) {
/* 129 */       throw new IllegalArgumentException("Connections per route may not be null");
/*     */     }
/* 131 */     this.poolLock = super.poolLock;
/* 132 */     this.leasedConnections = super.leasedConnections;
/* 133 */     this.operator = operator;
/* 134 */     this.connPerRoute = connPerRoute;
/* 135 */     this.maxTotalConnections = maxTotalConnections;
/* 136 */     this.freeConnections = createFreeConnQueue();
/* 137 */     this.waitingThreads = createWaitingThreadQueue();
/* 138 */     this.routeToPool = createRouteToPoolMap();
/* 139 */     this.connTTL = connTTL;
/* 140 */     this.connTTLTimeUnit = connTTLTimeUnit;
/*     */   }
/*     */   
/*     */   protected Lock getLock() {
/* 144 */     return this.poolLock;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConnPoolByRoute(ClientConnectionOperator operator, HttpParams params) {
/* 153 */     this(operator, ConnManagerParams.getMaxConnectionsPerRoute(params), ConnManagerParams.getMaxTotalConnections(params));
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
/*     */   protected Queue<BasicPoolEntry> createFreeConnQueue() {
/* 165 */     return new LinkedList<BasicPoolEntry>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Queue<WaitingThread> createWaitingThreadQueue() {
/* 175 */     return new LinkedList<WaitingThread>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<HttpRoute, RouteSpecificPool> createRouteToPoolMap() {
/* 185 */     return new HashMap<HttpRoute, RouteSpecificPool>();
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
/*     */   protected RouteSpecificPool newRouteSpecificPool(HttpRoute route) {
/* 198 */     return new RouteSpecificPool(route, this.connPerRoute);
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
/*     */   protected WaitingThread newWaitingThread(Condition cond, RouteSpecificPool rospl) {
/* 213 */     return new WaitingThread(cond, rospl);
/*     */   }
/*     */   
/*     */   private void closeConnection(BasicPoolEntry entry) {
/* 217 */     OperatedClientConnection conn = entry.getConnection();
/* 218 */     if (conn != null) {
/*     */       try {
/* 220 */         conn.close();
/* 221 */       } catch (IOException ex) {
/* 222 */         this.log.debug("I/O error closing connection", ex);
/*     */       } 
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
/*     */   protected RouteSpecificPool getRoutePool(HttpRoute route, boolean create) {
/* 238 */     RouteSpecificPool rospl = null;
/* 239 */     this.poolLock.lock();
/*     */     
/*     */     try {
/* 242 */       rospl = this.routeToPool.get(route);
/* 243 */       if (rospl == null && create) {
/*     */         
/* 245 */         rospl = newRouteSpecificPool(route);
/* 246 */         this.routeToPool.put(route, rospl);
/*     */       } 
/*     */     } finally {
/*     */       
/* 250 */       this.poolLock.unlock();
/*     */     } 
/*     */     
/* 253 */     return rospl;
/*     */   }
/*     */   
/*     */   public int getConnectionsInPool(HttpRoute route) {
/* 257 */     this.poolLock.lock();
/*     */     
/*     */     try {
/* 260 */       RouteSpecificPool rospl = getRoutePool(route, false);
/* 261 */       return (rospl != null) ? rospl.getEntryCount() : 0;
/*     */     } finally {
/*     */       
/* 264 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getConnectionsInPool() {
/* 269 */     this.poolLock.lock();
/*     */     try {
/* 271 */       return this.numConnections;
/*     */     } finally {
/* 273 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolEntryRequest requestPoolEntry(final HttpRoute route, final Object state) {
/* 282 */     final WaitingThreadAborter aborter = new WaitingThreadAborter();
/*     */     
/* 284 */     return new PoolEntryRequest()
/*     */       {
/*     */         public void abortRequest() {
/* 287 */           ConnPoolByRoute.this.poolLock.lock();
/*     */           try {
/* 289 */             aborter.abort();
/*     */           } finally {
/* 291 */             ConnPoolByRoute.this.poolLock.unlock();
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public BasicPoolEntry getPoolEntry(long timeout, TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
/* 299 */           return ConnPoolByRoute.this.getEntryBlocking(route, state, timeout, tunit, aborter);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BasicPoolEntry getEntryBlocking(HttpRoute route, Object state, long timeout, TimeUnit tunit, WaitingThreadAborter aborter) throws ConnectionPoolTimeoutException, InterruptedException {
/* 329 */     Date deadline = null;
/* 330 */     if (timeout > 0L) {
/* 331 */       deadline = new Date(System.currentTimeMillis() + tunit.toMillis(timeout));
/*     */     }
/*     */ 
/*     */     
/* 335 */     BasicPoolEntry entry = null;
/* 336 */     this.poolLock.lock();
/*     */     
/*     */     try {
/* 339 */       RouteSpecificPool rospl = getRoutePool(route, true);
/* 340 */       WaitingThread waitingThread = null;
/*     */       
/* 342 */       while (entry == null)
/*     */       {
/* 344 */         if (this.shutdown) {
/* 345 */           throw new IllegalStateException("Connection pool shut down");
/*     */         }
/*     */         
/* 348 */         if (this.log.isDebugEnabled()) {
/* 349 */           this.log.debug("[" + route + "] total kept alive: " + this.freeConnections.size() + ", total issued: " + this.leasedConnections.size() + ", total allocated: " + this.numConnections + " out of " + this.maxTotalConnections);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 360 */         entry = getFreeEntry(rospl, state);
/* 361 */         if (entry != null) {
/*     */           break;
/*     */         }
/*     */         
/* 365 */         boolean hasCapacity = (rospl.getCapacity() > 0);
/*     */         
/* 367 */         if (this.log.isDebugEnabled()) {
/* 368 */           this.log.debug("Available capacity: " + rospl.getCapacity() + " out of " + rospl.getMaxEntries() + " [" + route + "][" + state + "]");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 373 */         if (hasCapacity && this.numConnections < this.maxTotalConnections) {
/*     */           
/* 375 */           entry = createEntry(rospl, this.operator); continue;
/*     */         } 
/* 377 */         if (hasCapacity && !this.freeConnections.isEmpty()) {
/*     */           
/* 379 */           deleteLeastUsedEntry();
/*     */ 
/*     */           
/* 382 */           rospl = getRoutePool(route, true);
/* 383 */           entry = createEntry(rospl, this.operator);
/*     */           
/*     */           continue;
/*     */         } 
/* 387 */         if (this.log.isDebugEnabled()) {
/* 388 */           this.log.debug("Need to wait for connection [" + route + "][" + state + "]");
/*     */         }
/*     */ 
/*     */         
/* 392 */         if (waitingThread == null) {
/* 393 */           waitingThread = newWaitingThread(this.poolLock.newCondition(), rospl);
/*     */           
/* 395 */           aborter.setWaitingThread(waitingThread);
/*     */         } 
/*     */         
/* 398 */         boolean success = false;
/*     */         try {
/* 400 */           rospl.queueThread(waitingThread);
/* 401 */           this.waitingThreads.add(waitingThread);
/* 402 */           success = waitingThread.await(deadline);
/*     */ 
/*     */         
/*     */         }
/*     */         finally {
/*     */ 
/*     */           
/* 409 */           rospl.removeThread(waitingThread);
/* 410 */           this.waitingThreads.remove(waitingThread);
/*     */         } 
/*     */ 
/*     */         
/* 414 */         if (!success && deadline != null && deadline.getTime() <= System.currentTimeMillis())
/*     */         {
/* 416 */           throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
/*     */         }
/*     */       }
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 423 */       this.poolLock.unlock();
/*     */     } 
/* 425 */     return entry;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void freeEntry(BasicPoolEntry entry, boolean reusable, long validDuration, TimeUnit timeUnit) {
/* 431 */     HttpRoute route = entry.getPlannedRoute();
/* 432 */     if (this.log.isDebugEnabled()) {
/* 433 */       this.log.debug("Releasing connection [" + route + "][" + entry.getState() + "]");
/*     */     }
/*     */ 
/*     */     
/* 437 */     this.poolLock.lock();
/*     */     try {
/* 439 */       if (this.shutdown) {
/*     */ 
/*     */         
/* 442 */         closeConnection(entry);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 447 */       this.leasedConnections.remove(entry);
/*     */       
/* 449 */       RouteSpecificPool rospl = getRoutePool(route, true);
/*     */       
/* 451 */       if (reusable && rospl.getCapacity() >= 0) {
/* 452 */         if (this.log.isDebugEnabled()) {
/*     */           String s;
/* 454 */           if (validDuration > 0L) {
/* 455 */             s = "for " + validDuration + " " + timeUnit;
/*     */           } else {
/* 457 */             s = "indefinitely";
/*     */           } 
/* 459 */           this.log.debug("Pooling connection [" + route + "][" + entry.getState() + "]; keep alive " + s);
/*     */         } 
/*     */         
/* 462 */         rospl.freeEntry(entry);
/* 463 */         entry.updateExpiry(validDuration, timeUnit);
/* 464 */         this.freeConnections.add(entry);
/*     */       } else {
/* 466 */         closeConnection(entry);
/* 467 */         rospl.dropEntry();
/* 468 */         this.numConnections--;
/*     */       } 
/*     */       
/* 471 */       notifyWaitingThread(rospl);
/*     */     } finally {
/*     */       
/* 474 */       this.poolLock.unlock();
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
/*     */   protected BasicPoolEntry getFreeEntry(RouteSpecificPool rospl, Object state) {
/* 488 */     BasicPoolEntry entry = null;
/* 489 */     this.poolLock.lock();
/*     */     try {
/* 491 */       boolean done = false;
/* 492 */       while (!done)
/*     */       {
/* 494 */         entry = rospl.allocEntry(state);
/*     */         
/* 496 */         if (entry != null) {
/* 497 */           if (this.log.isDebugEnabled()) {
/* 498 */             this.log.debug("Getting free connection [" + rospl.getRoute() + "][" + state + "]");
/*     */           }
/*     */ 
/*     */           
/* 502 */           this.freeConnections.remove(entry);
/* 503 */           if (entry.isExpired(System.currentTimeMillis())) {
/*     */ 
/*     */             
/* 506 */             if (this.log.isDebugEnabled()) {
/* 507 */               this.log.debug("Closing expired free connection [" + rospl.getRoute() + "][" + state + "]");
/*     */             }
/* 509 */             closeConnection(entry);
/*     */ 
/*     */ 
/*     */             
/* 513 */             rospl.dropEntry();
/* 514 */             this.numConnections--; continue;
/*     */           } 
/* 516 */           this.leasedConnections.add(entry);
/* 517 */           done = true;
/*     */           
/*     */           continue;
/*     */         } 
/* 521 */         done = true;
/* 522 */         if (this.log.isDebugEnabled()) {
/* 523 */           this.log.debug("No free connections [" + rospl.getRoute() + "][" + state + "]");
/*     */         }
/*     */       }
/*     */     
/*     */     } finally {
/*     */       
/* 529 */       this.poolLock.unlock();
/*     */     } 
/* 531 */     return entry;
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
/*     */   protected BasicPoolEntry createEntry(RouteSpecificPool rospl, ClientConnectionOperator op) {
/* 548 */     if (this.log.isDebugEnabled()) {
/* 549 */       this.log.debug("Creating new connection [" + rospl.getRoute() + "]");
/*     */     }
/*     */ 
/*     */     
/* 553 */     BasicPoolEntry entry = new BasicPoolEntry(op, rospl.getRoute(), this.connTTL, this.connTTLTimeUnit);
/*     */     
/* 555 */     this.poolLock.lock();
/*     */     try {
/* 557 */       rospl.createdEntry(entry);
/* 558 */       this.numConnections++;
/* 559 */       this.leasedConnections.add(entry);
/*     */     } finally {
/* 561 */       this.poolLock.unlock();
/*     */     } 
/*     */     
/* 564 */     return entry;
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
/*     */   protected void deleteEntry(BasicPoolEntry entry) {
/* 581 */     HttpRoute route = entry.getPlannedRoute();
/*     */     
/* 583 */     if (this.log.isDebugEnabled()) {
/* 584 */       this.log.debug("Deleting connection [" + route + "][" + entry.getState() + "]");
/*     */     }
/*     */ 
/*     */     
/* 588 */     this.poolLock.lock();
/*     */     
/*     */     try {
/* 591 */       closeConnection(entry);
/*     */       
/* 593 */       RouteSpecificPool rospl = getRoutePool(route, true);
/* 594 */       rospl.deleteEntry(entry);
/* 595 */       this.numConnections--;
/* 596 */       if (rospl.isUnused()) {
/* 597 */         this.routeToPool.remove(route);
/*     */       }
/*     */     } finally {
/*     */       
/* 601 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void deleteLeastUsedEntry() {
/* 611 */     this.poolLock.lock();
/*     */     
/*     */     try {
/* 614 */       BasicPoolEntry entry = this.freeConnections.remove();
/*     */       
/* 616 */       if (entry != null) {
/* 617 */         deleteEntry(entry);
/* 618 */       } else if (this.log.isDebugEnabled()) {
/* 619 */         this.log.debug("No free connection to delete");
/*     */       } 
/*     */     } finally {
/*     */       
/* 623 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleLostEntry(HttpRoute route) {
/* 630 */     this.poolLock.lock();
/*     */     
/*     */     try {
/* 633 */       RouteSpecificPool rospl = getRoutePool(route, true);
/* 634 */       rospl.dropEntry();
/* 635 */       if (rospl.isUnused()) {
/* 636 */         this.routeToPool.remove(route);
/*     */       }
/*     */       
/* 639 */       this.numConnections--;
/* 640 */       notifyWaitingThread(rospl);
/*     */     } finally {
/*     */       
/* 643 */       this.poolLock.unlock();
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
/*     */   protected void notifyWaitingThread(RouteSpecificPool rospl) {
/* 662 */     WaitingThread waitingThread = null;
/*     */     
/* 664 */     this.poolLock.lock();
/*     */     
/*     */     try {
/* 667 */       if (rospl != null && rospl.hasThread()) {
/* 668 */         if (this.log.isDebugEnabled()) {
/* 669 */           this.log.debug("Notifying thread waiting on pool [" + rospl.getRoute() + "]");
/*     */         }
/*     */         
/* 672 */         waitingThread = rospl.nextThread();
/* 673 */       } else if (!this.waitingThreads.isEmpty()) {
/* 674 */         if (this.log.isDebugEnabled()) {
/* 675 */           this.log.debug("Notifying thread waiting on any pool");
/*     */         }
/* 677 */         waitingThread = this.waitingThreads.remove();
/* 678 */       } else if (this.log.isDebugEnabled()) {
/* 679 */         this.log.debug("Notifying no-one, there are no waiting threads");
/*     */       } 
/*     */       
/* 682 */       if (waitingThread != null) {
/* 683 */         waitingThread.wakeup();
/*     */       }
/*     */     } finally {
/*     */       
/* 687 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteClosedConnections() {
/* 694 */     this.poolLock.lock();
/*     */     try {
/* 696 */       Iterator<BasicPoolEntry> iter = this.freeConnections.iterator();
/* 697 */       while (iter.hasNext()) {
/* 698 */         BasicPoolEntry entry = iter.next();
/* 699 */         if (!entry.getConnection().isOpen()) {
/* 700 */           iter.remove();
/* 701 */           deleteEntry(entry);
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 705 */       this.poolLock.unlock();
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
/*     */   public void closeIdleConnections(long idletime, TimeUnit tunit) {
/* 718 */     if (tunit == null) {
/* 719 */       throw new IllegalArgumentException("Time unit must not be null.");
/*     */     }
/* 721 */     if (idletime < 0L) {
/* 722 */       idletime = 0L;
/*     */     }
/* 724 */     if (this.log.isDebugEnabled()) {
/* 725 */       this.log.debug("Closing connections idle longer than " + idletime + " " + tunit);
/*     */     }
/*     */     
/* 728 */     long deadline = System.currentTimeMillis() - tunit.toMillis(idletime);
/* 729 */     this.poolLock.lock();
/*     */     try {
/* 731 */       Iterator<BasicPoolEntry> iter = this.freeConnections.iterator();
/* 732 */       while (iter.hasNext()) {
/* 733 */         BasicPoolEntry entry = iter.next();
/* 734 */         if (entry.getUpdated() <= deadline) {
/* 735 */           if (this.log.isDebugEnabled()) {
/* 736 */             this.log.debug("Closing connection last used @ " + new Date(entry.getUpdated()));
/*     */           }
/* 738 */           iter.remove();
/* 739 */           deleteEntry(entry);
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 743 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeExpiredConnections() {
/* 749 */     this.log.debug("Closing expired connections");
/* 750 */     long now = System.currentTimeMillis();
/*     */     
/* 752 */     this.poolLock.lock();
/*     */     try {
/* 754 */       Iterator<BasicPoolEntry> iter = this.freeConnections.iterator();
/* 755 */       while (iter.hasNext()) {
/* 756 */         BasicPoolEntry entry = iter.next();
/* 757 */         if (entry.isExpired(now)) {
/* 758 */           if (this.log.isDebugEnabled()) {
/* 759 */             this.log.debug("Closing connection expired @ " + new Date(entry.getExpiry()));
/*     */           }
/* 761 */           iter.remove();
/* 762 */           deleteEntry(entry);
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 766 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 772 */     this.poolLock.lock();
/*     */     try {
/* 774 */       if (this.shutdown) {
/*     */         return;
/*     */       }
/* 777 */       this.shutdown = true;
/*     */ 
/*     */       
/* 780 */       Iterator<BasicPoolEntry> iter1 = this.leasedConnections.iterator();
/* 781 */       while (iter1.hasNext()) {
/* 782 */         BasicPoolEntry entry = iter1.next();
/* 783 */         iter1.remove();
/* 784 */         closeConnection(entry);
/*     */       } 
/*     */ 
/*     */       
/* 788 */       Iterator<BasicPoolEntry> iter2 = this.freeConnections.iterator();
/* 789 */       while (iter2.hasNext()) {
/* 790 */         BasicPoolEntry entry = iter2.next();
/* 791 */         iter2.remove();
/*     */         
/* 793 */         if (this.log.isDebugEnabled()) {
/* 794 */           this.log.debug("Closing connection [" + entry.getPlannedRoute() + "][" + entry.getState() + "]");
/*     */         }
/*     */         
/* 797 */         closeConnection(entry);
/*     */       } 
/*     */ 
/*     */       
/* 801 */       Iterator<WaitingThread> iwth = this.waitingThreads.iterator();
/* 802 */       while (iwth.hasNext()) {
/* 803 */         WaitingThread waiter = iwth.next();
/* 804 */         iwth.remove();
/* 805 */         waiter.wakeup();
/*     */       } 
/*     */       
/* 808 */       this.routeToPool.clear();
/*     */     } finally {
/*     */       
/* 811 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxTotalConnections(int max) {
/* 819 */     this.poolLock.lock();
/*     */     try {
/* 821 */       this.maxTotalConnections = max;
/*     */     } finally {
/* 823 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxTotalConnections() {
/* 832 */     return this.maxTotalConnections;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\tsccm\ConnPoolByRoute.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */