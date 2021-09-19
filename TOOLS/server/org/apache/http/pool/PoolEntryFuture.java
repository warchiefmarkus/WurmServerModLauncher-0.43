/*     */ package org.apache.http.pool;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Date;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.Lock;
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
/*     */ @ThreadSafe
/*     */ abstract class PoolEntryFuture<T>
/*     */   implements Future<T>
/*     */ {
/*     */   private final Lock lock;
/*     */   private final FutureCallback<T> callback;
/*     */   private final Condition condition;
/*     */   private volatile boolean cancelled;
/*     */   private volatile boolean completed;
/*     */   private T result;
/*     */   
/*     */   PoolEntryFuture(Lock lock, FutureCallback<T> callback) {
/*  53 */     this.lock = lock;
/*  54 */     this.condition = lock.newCondition();
/*  55 */     this.callback = callback;
/*     */   }
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  59 */     this.lock.lock();
/*     */     try {
/*  61 */       if (this.completed) {
/*  62 */         return false;
/*     */       }
/*  64 */       this.completed = true;
/*  65 */       this.cancelled = true;
/*  66 */       if (this.callback != null) {
/*  67 */         this.callback.cancelled();
/*     */       }
/*  69 */       this.condition.signalAll();
/*  70 */       return true;
/*     */     } finally {
/*  72 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isCancelled() {
/*  77 */     return this.cancelled;
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/*  81 */     return this.completed;
/*     */   }
/*     */   
/*     */   public T get() throws InterruptedException, ExecutionException {
/*     */     try {
/*  86 */       return get(0L, TimeUnit.MILLISECONDS);
/*  87 */     } catch (TimeoutException ex) {
/*  88 */       throw new ExecutionException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*  95 */     this.lock.lock();
/*     */     try {
/*  97 */       if (this.completed) {
/*  98 */         return this.result;
/*     */       }
/* 100 */       this.result = getPoolEntry(timeout, unit);
/* 101 */       this.completed = true;
/* 102 */       if (this.callback != null) {
/* 103 */         this.callback.completed(this.result);
/*     */       }
/* 105 */       return this.result;
/* 106 */     } catch (IOException ex) {
/* 107 */       this.completed = true;
/* 108 */       this.result = null;
/* 109 */       if (this.callback != null) {
/* 110 */         this.callback.failed(ex);
/*     */       }
/* 112 */       throw new ExecutionException(ex);
/*     */     } finally {
/* 114 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract T getPoolEntry(long paramLong, TimeUnit paramTimeUnit) throws IOException, InterruptedException, TimeoutException;
/*     */   
/*     */   public boolean await(Date deadline) throws InterruptedException {
/* 122 */     this.lock.lock();
/*     */     try {
/* 124 */       if (this.cancelled) {
/* 125 */         throw new InterruptedException("Operation interrupted");
/*     */       }
/* 127 */       boolean success = false;
/* 128 */       if (deadline != null) {
/* 129 */         success = this.condition.awaitUntil(deadline);
/*     */       } else {
/* 131 */         this.condition.await();
/* 132 */         success = true;
/*     */       } 
/* 134 */       if (this.cancelled) {
/* 135 */         throw new InterruptedException("Operation interrupted");
/*     */       }
/* 137 */       return success;
/*     */     } finally {
/* 139 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void wakeup() {
/* 145 */     this.lock.lock();
/*     */     try {
/* 147 */       this.condition.signalAll();
/*     */     } finally {
/* 149 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\pool\PoolEntryFuture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */