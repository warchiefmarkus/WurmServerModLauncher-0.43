/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardWatchEventKinds;
/*     */ import java.nio.file.WatchEvent;
/*     */ import java.nio.file.Watchable;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class PollingWatchService
/*     */   extends AbstractWatchService
/*     */ {
/*  56 */   private static final ThreadFactory THREAD_FACTORY = (new ThreadFactoryBuilder()).setNameFormat("com.google.common.jimfs.PollingWatchService-thread-%d").setDaemon(true).build();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   private final ScheduledExecutorService pollingService = Executors.newSingleThreadScheduledExecutor(THREAD_FACTORY);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   private final ConcurrentMap<AbstractWatchService.Key, Snapshot> snapshots = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private final FileSystemView view;
/*     */ 
/*     */ 
/*     */   
/*     */   private final PathService pathService;
/*     */ 
/*     */ 
/*     */   
/*     */   private final FileSystemState fileSystemState;
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   final long interval;
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   final TimeUnit timeUnit;
/*     */ 
/*     */   
/*     */   private ScheduledFuture<?> pollingFuture;
/*     */ 
/*     */   
/*     */   private final Runnable pollingTask;
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractWatchService.Key register(Watchable watchable, Iterable<? extends WatchEvent.Kind<?>> eventTypes) throws IOException {
/* 101 */     JimfsPath path = checkWatchable(watchable);
/*     */     
/* 103 */     AbstractWatchService.Key key = super.register(path, eventTypes);
/*     */     
/* 105 */     Snapshot snapshot = takeSnapshot(path);
/*     */     
/* 107 */     synchronized (this) {
/* 108 */       this.snapshots.put(key, snapshot);
/* 109 */       if (this.pollingFuture == null) {
/* 110 */         startPolling();
/*     */       }
/*     */     } 
/*     */     
/* 114 */     return key;
/*     */   }
/*     */   
/*     */   private JimfsPath checkWatchable(Watchable watchable) {
/* 118 */     if (!(watchable instanceof JimfsPath) || !isSameFileSystem((Path)watchable)) {
/* 119 */       throw new IllegalArgumentException("watchable (" + watchable + ") must be a Path " + "associated with the same file system as this watch service");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     return (JimfsPath)watchable;
/*     */   }
/*     */   
/*     */   private boolean isSameFileSystem(Path path) {
/* 130 */     return (((JimfsFileSystem)path.getFileSystem()).getDefaultView() == this.view);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   synchronized boolean isPolling() {
/* 135 */     return (this.pollingFuture != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void cancelled(AbstractWatchService.Key key) {
/* 140 */     this.snapshots.remove(key);
/*     */     
/* 142 */     if (this.snapshots.isEmpty()) {
/* 143 */       stopPolling();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 149 */     super.close();
/*     */     
/* 151 */     synchronized (this) {
/*     */       
/* 153 */       for (AbstractWatchService.Key key : this.snapshots.keySet()) {
/* 154 */         key.cancel();
/*     */       }
/*     */       
/* 157 */       this.pollingService.shutdown();
/* 158 */       this.fileSystemState.unregister(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void startPolling() {
/* 163 */     this.pollingFuture = this.pollingService.scheduleAtFixedRate(this.pollingTask, this.interval, this.interval, this.timeUnit);
/*     */   }
/*     */ 
/*     */   
/*     */   private void stopPolling() {
/* 168 */     this.pollingFuture.cancel(false);
/* 169 */     this.pollingFuture = null;
/*     */   }
/*     */   
/* 172 */   PollingWatchService(FileSystemView view, PathService pathService, FileSystemState fileSystemState, long interval, TimeUnit timeUnit) { this.pollingTask = new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 176 */           synchronized (PollingWatchService.this) {
/* 177 */             for (Map.Entry<AbstractWatchService.Key, PollingWatchService.Snapshot> entry : (Iterable<Map.Entry<AbstractWatchService.Key, PollingWatchService.Snapshot>>)PollingWatchService.this.snapshots.entrySet()) {
/* 178 */               AbstractWatchService.Key key = entry.getKey();
/* 179 */               PollingWatchService.Snapshot previousSnapshot = entry.getValue();
/*     */               
/* 181 */               JimfsPath path = (JimfsPath)key.watchable();
/*     */               try {
/* 183 */                 PollingWatchService.Snapshot newSnapshot = PollingWatchService.this.takeSnapshot(path);
/* 184 */                 boolean posted = previousSnapshot.postChanges(newSnapshot, key);
/* 185 */                 entry.setValue(newSnapshot);
/* 186 */                 if (posted) {
/* 187 */                   key.signal();
/*     */                 }
/* 189 */               } catch (IOException e) {
/*     */ 
/*     */                 
/* 192 */                 key.cancel();
/*     */               } 
/*     */             } 
/*     */           }  } }; this.view = (FileSystemView)Preconditions.checkNotNull(view); this.pathService = (PathService)Preconditions.checkNotNull(pathService);
/*     */     this.fileSystemState = (FileSystemState)Preconditions.checkNotNull(fileSystemState);
/*     */     Preconditions.checkArgument((interval >= 0L), "interval (%s) may not be negative", new Object[] { Long.valueOf(interval) });
/*     */     this.interval = interval;
/*     */     this.timeUnit = (TimeUnit)Preconditions.checkNotNull(timeUnit);
/* 200 */     fileSystemState.register(this); } private Snapshot takeSnapshot(JimfsPath path) throws IOException { return new Snapshot((Map<Name, Long>)this.view.snapshotModifiedTimes(path)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class Snapshot
/*     */   {
/*     */     private final ImmutableMap<Name, Long> modifiedTimes;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Snapshot(Map<Name, Long> modifiedTimes) {
/* 214 */       this.modifiedTimes = ImmutableMap.copyOf(modifiedTimes);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean postChanges(Snapshot newState, AbstractWatchService.Key key) {
/* 222 */       boolean changesPosted = false;
/*     */       
/* 224 */       if (key.subscribesTo(StandardWatchEventKinds.ENTRY_CREATE)) {
/* 225 */         Sets.SetView setView = Sets.difference((Set)newState.modifiedTimes.keySet(), (Set)this.modifiedTimes.keySet());
/*     */ 
/*     */         
/* 228 */         for (Name name : setView) {
/* 229 */           key.post(new AbstractWatchService.Event(StandardWatchEventKinds.ENTRY_CREATE, 1, PollingWatchService.this.pathService.createFileName(name)));
/* 230 */           changesPosted = true;
/*     */         } 
/*     */       } 
/*     */       
/* 234 */       if (key.subscribesTo(StandardWatchEventKinds.ENTRY_DELETE)) {
/* 235 */         Sets.SetView setView = Sets.difference((Set)this.modifiedTimes.keySet(), (Set)newState.modifiedTimes.keySet());
/*     */ 
/*     */         
/* 238 */         for (Name name : setView) {
/* 239 */           key.post(new AbstractWatchService.Event(StandardWatchEventKinds.ENTRY_DELETE, 1, PollingWatchService.this.pathService.createFileName(name)));
/* 240 */           changesPosted = true;
/*     */         } 
/*     */       } 
/*     */       
/* 244 */       if (key.subscribesTo(StandardWatchEventKinds.ENTRY_MODIFY)) {
/* 245 */         for (Map.Entry<Name, Long> entry : (Iterable<Map.Entry<Name, Long>>)this.modifiedTimes.entrySet()) {
/* 246 */           Name name = entry.getKey();
/* 247 */           Long modifiedTime = entry.getValue();
/*     */           
/* 249 */           Long newModifiedTime = (Long)newState.modifiedTimes.get(name);
/* 250 */           if (newModifiedTime != null && !modifiedTime.equals(newModifiedTime)) {
/* 251 */             key.post(new AbstractWatchService.Event(StandardWatchEventKinds.ENTRY_MODIFY, 1, PollingWatchService.this.pathService.createFileName(name)));
/* 252 */             changesPosted = true;
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 257 */       return changesPosted;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\PollingWatchService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */