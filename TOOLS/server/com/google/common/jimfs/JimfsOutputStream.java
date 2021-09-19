/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import javax.annotation.concurrent.GuardedBy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class JimfsOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   @GuardedBy("this")
/*     */   @VisibleForTesting
/*     */   RegularFile file;
/*     */   @GuardedBy("this")
/*     */   private long pos;
/*     */   private final boolean append;
/*     */   private final FileSystemState fileSystemState;
/*     */   
/*     */   JimfsOutputStream(RegularFile file, boolean append, FileSystemState fileSystemState) {
/*  47 */     this.file = (RegularFile)Preconditions.checkNotNull(file);
/*  48 */     this.append = append;
/*  49 */     this.fileSystemState = fileSystemState;
/*  50 */     fileSystemState.register(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void write(int b) throws IOException {
/*  55 */     checkNotClosed();
/*     */     
/*  57 */     this.file.writeLock().lock();
/*     */     try {
/*  59 */       if (this.append) {
/*  60 */         this.pos = this.file.sizeWithoutLocking();
/*     */       }
/*  62 */       this.file.write(this.pos++, (byte)b);
/*     */       
/*  64 */       this.file.updateModifiedTime();
/*     */     } finally {
/*  66 */       this.file.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/*  72 */     writeInternal(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/*  77 */     Preconditions.checkPositionIndexes(off, off + len, b.length);
/*  78 */     writeInternal(b, off, len);
/*     */   }
/*     */   
/*     */   private synchronized void writeInternal(byte[] b, int off, int len) throws IOException {
/*  82 */     checkNotClosed();
/*     */     
/*  84 */     this.file.writeLock().lock();
/*     */     try {
/*  86 */       if (this.append) {
/*  87 */         this.pos = this.file.sizeWithoutLocking();
/*     */       }
/*  89 */       this.pos += this.file.write(this.pos, b, off, len);
/*     */       
/*  91 */       this.file.updateModifiedTime();
/*     */     } finally {
/*  93 */       this.file.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   @GuardedBy("this")
/*     */   private void checkNotClosed() throws IOException {
/*  99 */     if (this.file == null) {
/* 100 */       throw new IOException("stream is closed");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void close() throws IOException {
/* 106 */     if (isOpen()) {
/* 107 */       this.fileSystemState.unregister(this);
/* 108 */       this.file.closed();
/*     */ 
/*     */       
/* 111 */       this.file = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   @GuardedBy("this")
/*     */   private boolean isOpen() {
/* 117 */     return (this.file != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\JimfsOutputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */