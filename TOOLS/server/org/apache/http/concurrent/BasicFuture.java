/*     */ package org.apache.http.concurrent;
/*     */ 
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicFuture<T>
/*     */   implements Future<T>, Cancellable
/*     */ {
/*     */   private final FutureCallback<T> callback;
/*     */   private volatile boolean completed;
/*     */   private volatile boolean cancelled;
/*     */   private volatile T result;
/*     */   private volatile Exception ex;
/*     */   
/*     */   public BasicFuture(FutureCallback<T> callback) {
/*  53 */     this.callback = callback;
/*     */   }
/*     */   
/*     */   public boolean isCancelled() {
/*  57 */     return this.cancelled;
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/*  61 */     return this.completed;
/*     */   }
/*     */   
/*     */   private T getResult() throws ExecutionException {
/*  65 */     if (this.ex != null) {
/*  66 */       throw new ExecutionException(this.ex);
/*     */     }
/*  68 */     return this.result;
/*     */   }
/*     */   
/*     */   public synchronized T get() throws InterruptedException, ExecutionException {
/*  72 */     while (!this.completed) {
/*  73 */       wait();
/*     */     }
/*  75 */     return getResult();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*  80 */     long msecs = unit.toMillis(timeout);
/*  81 */     long startTime = (msecs <= 0L) ? 0L : System.currentTimeMillis();
/*  82 */     long waitTime = msecs;
/*  83 */     if (this.completed)
/*  84 */       return getResult(); 
/*  85 */     if (waitTime <= 0L) {
/*  86 */       throw new TimeoutException();
/*     */     }
/*     */     while (true) {
/*  89 */       wait(waitTime);
/*  90 */       if (this.completed) {
/*  91 */         return getResult();
/*     */       }
/*  93 */       waitTime = msecs - System.currentTimeMillis() - startTime;
/*  94 */       if (waitTime <= 0L) {
/*  95 */         throw new TimeoutException();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean completed(T result) {
/* 103 */     synchronized (this) {
/* 104 */       if (this.completed) {
/* 105 */         return false;
/*     */       }
/* 107 */       this.completed = true;
/* 108 */       this.result = result;
/* 109 */       notifyAll();
/*     */     } 
/* 111 */     if (this.callback != null) {
/* 112 */       this.callback.completed(result);
/*     */     }
/* 114 */     return true;
/*     */   }
/*     */   
/*     */   public boolean failed(Exception exception) {
/* 118 */     synchronized (this) {
/* 119 */       if (this.completed) {
/* 120 */         return false;
/*     */       }
/* 122 */       this.completed = true;
/* 123 */       this.ex = exception;
/* 124 */       notifyAll();
/*     */     } 
/* 126 */     if (this.callback != null) {
/* 127 */       this.callback.failed(exception);
/*     */     }
/* 129 */     return true;
/*     */   }
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 133 */     synchronized (this) {
/* 134 */       if (this.completed) {
/* 135 */         return false;
/*     */       }
/* 137 */       this.completed = true;
/* 138 */       this.cancelled = true;
/* 139 */       notifyAll();
/*     */     } 
/* 141 */     if (this.callback != null) {
/* 142 */       this.callback.cancelled();
/*     */     }
/* 144 */     return true;
/*     */   }
/*     */   
/*     */   public boolean cancel() {
/* 148 */     return cancel(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\concurrent\BasicFuture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */