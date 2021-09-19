/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.ClosedWatchServiceException;
/*     */ import java.nio.file.StandardWatchEventKinds;
/*     */ import java.nio.file.WatchEvent;
/*     */ import java.nio.file.WatchKey;
/*     */ import java.nio.file.WatchService;
/*     */ import java.nio.file.Watchable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractWatchService
/*     */   implements WatchService
/*     */ {
/*  58 */   private final BlockingQueue<WatchKey> queue = new LinkedBlockingQueue<>();
/*  59 */   private final WatchKey poison = new Key(this, null, (Iterable<? extends WatchEvent.Kind<?>>)ImmutableSet.of());
/*     */   
/*  61 */   private final AtomicBoolean open = new AtomicBoolean(true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Key register(Watchable watchable, Iterable<? extends WatchEvent.Kind<?>> eventTypes) throws IOException {
/*  70 */     checkOpen();
/*  71 */     return new Key(this, watchable, eventTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   public boolean isOpen() {
/*  79 */     return this.open.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void enqueue(Key key) {
/*  86 */     if (isOpen()) {
/*  87 */       this.queue.add(key);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancelled(Key key) {}
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   ImmutableList<WatchKey> queuedKeys() {
/*  98 */     return ImmutableList.copyOf(this.queue);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public WatchKey poll() {
/* 104 */     checkOpen();
/* 105 */     return check(this.queue.poll());
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public WatchKey poll(long timeout, TimeUnit unit) throws InterruptedException {
/* 111 */     checkOpen();
/* 112 */     return check(this.queue.poll(timeout, unit));
/*     */   }
/*     */ 
/*     */   
/*     */   public WatchKey take() throws InterruptedException {
/* 117 */     checkOpen();
/* 118 */     return check(this.queue.take());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private WatchKey check(@Nullable WatchKey key) {
/* 126 */     if (key == this.poison) {
/*     */       
/* 128 */       this.queue.offer(this.poison);
/* 129 */       throw new ClosedWatchServiceException();
/*     */     } 
/* 131 */     return key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void checkOpen() {
/* 138 */     if (!this.open.get()) {
/* 139 */       throw new ClosedWatchServiceException();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 145 */     if (this.open.compareAndSet(true, false)) {
/* 146 */       this.queue.clear();
/* 147 */       this.queue.offer(this.poison);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class Event<T>
/*     */     implements WatchEvent<T>
/*     */   {
/*     */     private final WatchEvent.Kind<T> kind;
/*     */     
/*     */     private final int count;
/*     */     @Nullable
/*     */     private final T context;
/*     */     
/*     */     public Event(WatchEvent.Kind<T> kind, int count, @Nullable T context) {
/* 162 */       this.kind = (WatchEvent.Kind<T>)Preconditions.checkNotNull(kind);
/* 163 */       Preconditions.checkArgument((count >= 0), "count (%s) must be non-negative", new Object[] { Integer.valueOf(count) });
/* 164 */       this.count = count;
/* 165 */       this.context = context;
/*     */     }
/*     */ 
/*     */     
/*     */     public WatchEvent.Kind<T> kind() {
/* 170 */       return this.kind;
/*     */     }
/*     */ 
/*     */     
/*     */     public int count() {
/* 175 */       return this.count;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T context() {
/* 181 */       return this.context;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 186 */       if (obj instanceof Event) {
/* 187 */         Event<?> other = (Event)obj;
/* 188 */         return (kind().equals(other.kind()) && count() == other.count() && Objects.equals(context(), other.context()));
/*     */       } 
/*     */ 
/*     */       
/* 192 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 197 */       return Objects.hash(new Object[] { kind(), Integer.valueOf(count()), context() });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 202 */       return MoreObjects.toStringHelper(this).add("kind", kind()).add("count", count()).add("context", context()).toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class Key
/*     */     implements WatchKey
/*     */   {
/*     */     @VisibleForTesting
/*     */     static final int MAX_QUEUE_SIZE = 256;
/*     */     
/*     */     private final AbstractWatchService watcher;
/*     */     private final Watchable watchable;
/*     */     private final ImmutableSet<WatchEvent.Kind<?>> subscribedTypes;
/*     */     
/*     */     private static WatchEvent<Object> overflowEvent(int count) {
/* 218 */       return new AbstractWatchService.Event(StandardWatchEventKinds.OVERFLOW, count, null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 225 */     private final AtomicReference<State> state = new AtomicReference<>(State.READY);
/* 226 */     private final AtomicBoolean valid = new AtomicBoolean(true);
/* 227 */     private final AtomicInteger overflow = new AtomicInteger();
/*     */     
/* 229 */     private final BlockingQueue<WatchEvent<?>> events = new ArrayBlockingQueue<>(256);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Key(AbstractWatchService watcher, @Nullable Watchable watchable, Iterable<? extends WatchEvent.Kind<?>> subscribedTypes) {
/* 235 */       this.watcher = (AbstractWatchService)Preconditions.checkNotNull(watcher);
/* 236 */       this.watchable = watchable;
/* 237 */       this.subscribedTypes = ImmutableSet.copyOf(subscribedTypes);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @VisibleForTesting
/*     */     State state() {
/* 245 */       return this.state.get();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean subscribesTo(WatchEvent.Kind<?> eventType) {
/* 252 */       return this.subscribedTypes.contains(eventType);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void post(WatchEvent<?> event) {
/* 260 */       if (!this.events.offer(event)) {
/* 261 */         this.overflow.incrementAndGet();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void signal() {
/* 270 */       if (this.state.getAndSet(State.SIGNALLED) == State.READY) {
/* 271 */         this.watcher.enqueue(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isValid() {
/* 277 */       return (this.watcher.isOpen() && this.valid.get());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public List<WatchEvent<?>> pollEvents() {
/* 285 */       List<WatchEvent<?>> result = new ArrayList<>(this.events.size());
/* 286 */       this.events.drainTo(result);
/* 287 */       int overflowCount = this.overflow.getAndSet(0);
/* 288 */       if (overflowCount != 0) {
/* 289 */         result.add(overflowEvent(overflowCount));
/*     */       }
/* 291 */       return Collections.unmodifiableList(result);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean reset() {
/* 298 */       if (isValid() && this.state.compareAndSet(State.SIGNALLED, State.READY))
/*     */       {
/* 300 */         if (!this.events.isEmpty()) {
/* 301 */           signal();
/*     */         }
/*     */       }
/*     */       
/* 305 */       return isValid();
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 310 */       this.valid.set(false);
/* 311 */       this.watcher.cancelled(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public Watchable watchable() {
/* 316 */       return this.watchable;
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     enum State {
/* 321 */       READY,
/* 322 */       SIGNALLED;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\AbstractWatchService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */