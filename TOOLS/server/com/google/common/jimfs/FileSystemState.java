/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.ClosedFileSystemException;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class FileSystemState
/*     */   implements Closeable
/*     */ {
/*  40 */   private final Set<Closeable> resources = Sets.newConcurrentHashSet();
/*     */   
/*     */   private final Runnable onClose;
/*  43 */   private final AtomicBoolean open = new AtomicBoolean(true);
/*     */ 
/*     */   
/*  46 */   private final AtomicInteger registering = new AtomicInteger();
/*     */   
/*     */   FileSystemState(Runnable onClose) {
/*  49 */     this.onClose = (Runnable)Preconditions.checkNotNull(onClose);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/*  56 */     return this.open.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkOpen() {
/*  63 */     if (!this.open.get()) {
/*  64 */       throw new ClosedFileSystemException();
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
/*     */   public <C extends Closeable> C register(C resource) {
/*  76 */     checkOpen();
/*     */     
/*  78 */     this.registering.incrementAndGet();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  83 */       checkOpen();
/*  84 */       this.resources.add((Closeable)resource);
/*  85 */       return resource;
/*     */     } finally {
/*  87 */       this.registering.decrementAndGet();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregister(Closeable resource) {
/*  95 */     this.resources.remove(resource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 103 */     if (this.open.compareAndSet(true, false)) {
/* 104 */       this.onClose.run();
/*     */       
/* 106 */       Throwable thrown = null;
/*     */       while (true) {
/* 108 */         for (Closeable resource : this.resources) {
/*     */           try {
/* 110 */             resource.close();
/* 111 */           } catch (Throwable e) {
/* 112 */             if (thrown == null) {
/* 113 */               thrown = e;
/*     */             } else {
/* 115 */               thrown.addSuppressed(e);
/*     */             } 
/*     */           } finally {
/*     */             
/* 119 */             this.resources.remove(resource);
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 130 */         if (this.registering.get() <= 0 && this.resources.isEmpty()) {
/* 131 */           Throwables.propagateIfPossible(thrown, IOException.class);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\FileSystemState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */