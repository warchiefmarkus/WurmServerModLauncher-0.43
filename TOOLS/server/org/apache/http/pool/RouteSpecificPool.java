/*     */ package org.apache.http.pool;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Set;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ abstract class RouteSpecificPool<T, C, E extends PoolEntry<T, C>>
/*     */ {
/*     */   private final T route;
/*     */   private final Set<E> leased;
/*     */   private final LinkedList<E> available;
/*     */   private final LinkedList<PoolEntryFuture<E>> pending;
/*     */   
/*     */   RouteSpecificPool(T route) {
/*  46 */     this.route = route;
/*  47 */     this.leased = new HashSet<E>();
/*  48 */     this.available = new LinkedList<E>();
/*  49 */     this.pending = new LinkedList<PoolEntryFuture<E>>();
/*     */   }
/*     */   
/*     */   protected abstract E createEntry(C paramC);
/*     */   
/*     */   public final T getRoute() {
/*  55 */     return this.route;
/*     */   }
/*     */   
/*     */   public int getLeasedCount() {
/*  59 */     return this.leased.size();
/*     */   }
/*     */   
/*     */   public int getPendingCount() {
/*  63 */     return this.pending.size();
/*     */   }
/*     */   
/*     */   public int getAvailableCount() {
/*  67 */     return this.available.size();
/*     */   }
/*     */   
/*     */   public int getAllocatedCount() {
/*  71 */     return this.available.size() + this.leased.size();
/*     */   }
/*     */   
/*     */   public E getFree(Object state) {
/*  75 */     if (!this.available.isEmpty()) {
/*  76 */       if (state != null) {
/*  77 */         Iterator<E> iterator = this.available.iterator();
/*  78 */         while (iterator.hasNext()) {
/*  79 */           PoolEntry poolEntry = (PoolEntry)iterator.next();
/*  80 */           if (state.equals(poolEntry.getState())) {
/*  81 */             iterator.remove();
/*  82 */             this.leased.add((E)poolEntry);
/*  83 */             return (E)poolEntry;
/*     */           } 
/*     */         } 
/*     */       } 
/*  87 */       Iterator<E> it = this.available.iterator();
/*  88 */       while (it.hasNext()) {
/*  89 */         PoolEntry poolEntry = (PoolEntry)it.next();
/*  90 */         if (poolEntry.getState() == null) {
/*  91 */           it.remove();
/*  92 */           this.leased.add((E)poolEntry);
/*  93 */           return (E)poolEntry;
/*     */         } 
/*     */       } 
/*     */     } 
/*  97 */     return null;
/*     */   }
/*     */   
/*     */   public E getLastUsed() {
/* 101 */     if (!this.available.isEmpty()) {
/* 102 */       return this.available.getLast();
/*     */     }
/* 104 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(E entry) {
/* 109 */     if (entry == null) {
/* 110 */       throw new IllegalArgumentException("Pool entry may not be null");
/*     */     }
/* 112 */     if (!this.available.remove(entry) && 
/* 113 */       !this.leased.remove(entry)) {
/* 114 */       return false;
/*     */     }
/*     */     
/* 117 */     return true;
/*     */   }
/*     */   
/*     */   public void free(E entry, boolean reusable) {
/* 121 */     if (entry == null) {
/* 122 */       throw new IllegalArgumentException("Pool entry may not be null");
/*     */     }
/* 124 */     boolean found = this.leased.remove(entry);
/* 125 */     if (!found) {
/* 126 */       throw new IllegalStateException("Entry " + entry + " has not been leased from this pool");
/*     */     }
/*     */     
/* 129 */     if (reusable) {
/* 130 */       this.available.addFirst(entry);
/*     */     }
/*     */   }
/*     */   
/*     */   public E add(C conn) {
/* 135 */     E entry = createEntry(conn);
/* 136 */     this.leased.add(entry);
/* 137 */     return entry;
/*     */   }
/*     */   
/*     */   public void queue(PoolEntryFuture<E> future) {
/* 141 */     if (future == null) {
/*     */       return;
/*     */     }
/* 144 */     this.pending.add(future);
/*     */   }
/*     */   
/*     */   public PoolEntryFuture<E> nextPending() {
/* 148 */     return this.pending.poll();
/*     */   }
/*     */   
/*     */   public void unqueue(PoolEntryFuture<E> future) {
/* 152 */     if (future == null) {
/*     */       return;
/*     */     }
/* 155 */     this.pending.remove(future);
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 159 */     for (PoolEntryFuture<E> future : this.pending) {
/* 160 */       future.cancel(true);
/*     */     }
/* 162 */     this.pending.clear();
/* 163 */     for (PoolEntry poolEntry : this.available) {
/* 164 */       poolEntry.close();
/*     */     }
/* 166 */     this.available.clear();
/* 167 */     for (PoolEntry poolEntry : this.leased) {
/* 168 */       poolEntry.close();
/*     */     }
/* 170 */     this.leased.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 175 */     StringBuilder buffer = new StringBuilder();
/* 176 */     buffer.append("[route: ");
/* 177 */     buffer.append(this.route);
/* 178 */     buffer.append("][leased: ");
/* 179 */     buffer.append(this.leased.size());
/* 180 */     buffer.append("][available: ");
/* 181 */     buffer.append(this.available.size());
/* 182 */     buffer.append("][pending: ");
/* 183 */     buffer.append(this.pending.size());
/* 184 */     buffer.append("]");
/* 185 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\pool\RouteSpecificPool.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */