/*     */ package org.apache.http.impl.conn.tsccm;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.LinkedList;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Queue;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.conn.params.ConnPerRoute;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.util.LangUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class RouteSpecificPool
/*     */ {
/*  54 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   protected final HttpRoute route;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int maxEntries;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ConnPerRoute connPerRoute;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final LinkedList<BasicPoolEntry> freeEntries;
/*     */ 
/*     */   
/*     */   protected final Queue<WaitingThread> waitingThreads;
/*     */ 
/*     */   
/*     */   protected int numEntries;
/*     */ 
/*     */ 
/*     */   
/*     */   public RouteSpecificPool(HttpRoute route, int maxEntries) {
/*  81 */     this.route = route;
/*  82 */     this.maxEntries = maxEntries;
/*  83 */     this.connPerRoute = new ConnPerRoute() {
/*     */         public int getMaxForRoute(HttpRoute route) {
/*  85 */           return RouteSpecificPool.this.maxEntries;
/*     */         }
/*     */       };
/*  88 */     this.freeEntries = new LinkedList<BasicPoolEntry>();
/*  89 */     this.waitingThreads = new LinkedList<WaitingThread>();
/*  90 */     this.numEntries = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RouteSpecificPool(HttpRoute route, ConnPerRoute connPerRoute) {
/* 101 */     this.route = route;
/* 102 */     this.connPerRoute = connPerRoute;
/* 103 */     this.maxEntries = connPerRoute.getMaxForRoute(route);
/* 104 */     this.freeEntries = new LinkedList<BasicPoolEntry>();
/* 105 */     this.waitingThreads = new LinkedList<WaitingThread>();
/* 106 */     this.numEntries = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpRoute getRoute() {
/* 116 */     return this.route;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getMaxEntries() {
/* 126 */     return this.maxEntries;
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
/*     */   public boolean isUnused() {
/* 139 */     return (this.numEntries < 1 && this.waitingThreads.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCapacity() {
/* 149 */     return this.connPerRoute.getMaxForRoute(this.route) - this.numEntries;
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
/*     */   public final int getEntryCount() {
/* 161 */     return this.numEntries;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicPoolEntry allocEntry(Object state) {
/* 171 */     if (!this.freeEntries.isEmpty()) {
/* 172 */       ListIterator<BasicPoolEntry> it = this.freeEntries.listIterator(this.freeEntries.size());
/* 173 */       while (it.hasPrevious()) {
/* 174 */         BasicPoolEntry entry = it.previous();
/* 175 */         if (entry.getState() == null || LangUtils.equals(state, entry.getState())) {
/* 176 */           it.remove();
/* 177 */           return entry;
/*     */         } 
/*     */       } 
/*     */     } 
/* 181 */     if (getCapacity() == 0 && !this.freeEntries.isEmpty()) {
/* 182 */       BasicPoolEntry entry = this.freeEntries.remove();
/* 183 */       entry.shutdownEntry();
/* 184 */       OperatedClientConnection conn = entry.getConnection();
/*     */       try {
/* 186 */         conn.close();
/* 187 */       } catch (IOException ex) {
/* 188 */         this.log.debug("I/O error closing connection", ex);
/*     */       } 
/* 190 */       return entry;
/*     */     } 
/* 192 */     return null;
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
/*     */   public void freeEntry(BasicPoolEntry entry) {
/* 204 */     if (this.numEntries < 1) {
/* 205 */       throw new IllegalStateException("No entry created for this pool. " + this.route);
/*     */     }
/*     */     
/* 208 */     if (this.numEntries <= this.freeEntries.size()) {
/* 209 */       throw new IllegalStateException("No entry allocated from this pool. " + this.route);
/*     */     }
/*     */     
/* 212 */     this.freeEntries.add(entry);
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
/*     */   public void createdEntry(BasicPoolEntry entry) {
/* 226 */     if (!this.route.equals(entry.getPlannedRoute())) {
/* 227 */       throw new IllegalArgumentException("Entry not planned for this pool.\npool: " + this.route + "\nplan: " + entry.getPlannedRoute());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     this.numEntries++;
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
/*     */   public boolean deleteEntry(BasicPoolEntry entry) {
/* 249 */     boolean found = this.freeEntries.remove(entry);
/* 250 */     if (found)
/* 251 */       this.numEntries--; 
/* 252 */     return found;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dropEntry() {
/* 263 */     if (this.numEntries < 1) {
/* 264 */       throw new IllegalStateException("There is no entry that could be dropped.");
/*     */     }
/*     */     
/* 267 */     this.numEntries--;
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
/*     */   public void queueThread(WaitingThread wt) {
/* 280 */     if (wt == null) {
/* 281 */       throw new IllegalArgumentException("Waiting thread must not be null.");
/*     */     }
/*     */     
/* 284 */     this.waitingThreads.add(wt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasThread() {
/* 295 */     return !this.waitingThreads.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WaitingThread nextThread() {
/* 305 */     return this.waitingThreads.peek();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeThread(WaitingThread wt) {
/* 315 */     if (wt == null) {
/*     */       return;
/*     */     }
/* 318 */     this.waitingThreads.remove(wt);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\tsccm\RouteSpecificPool.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */