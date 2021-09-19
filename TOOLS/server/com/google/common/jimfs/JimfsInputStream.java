/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ 
/*     */ final class JimfsInputStream
/*     */   extends InputStream
/*     */ {
/*     */   @GuardedBy("this")
/*     */   @VisibleForTesting
/*     */   RegularFile file;
/*     */   @GuardedBy("this")
/*     */   private long pos;
/*     */   @GuardedBy("this")
/*     */   private boolean finished;
/*     */   private final FileSystemState fileSystemState;
/*     */   
/*     */   public JimfsInputStream(RegularFile file, FileSystemState fileSystemState) {
/*  50 */     this.file = (RegularFile)Preconditions.checkNotNull(file);
/*  51 */     this.fileSystemState = fileSystemState;
/*  52 */     fileSystemState.register(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int read() throws IOException {
/*  57 */     checkNotClosed();
/*  58 */     if (this.finished) {
/*  59 */       return -1;
/*     */     }
/*     */     
/*  62 */     this.file.readLock().lock();
/*     */     
/*     */     try {
/*  65 */       int b = this.file.read(this.pos++);
/*  66 */       if (b == -1) {
/*  67 */         this.finished = true;
/*     */       } else {
/*  69 */         this.file.updateAccessTime();
/*     */       } 
/*  71 */       return b;
/*     */     } finally {
/*  73 */       this.file.readLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/*  79 */     return readInternal(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*  84 */     Preconditions.checkPositionIndexes(off, off + len, b.length);
/*  85 */     return readInternal(b, off, len);
/*     */   }
/*     */   
/*     */   private synchronized int readInternal(byte[] b, int off, int len) throws IOException {
/*  89 */     checkNotClosed();
/*  90 */     if (this.finished) {
/*  91 */       return -1;
/*     */     }
/*     */     
/*  94 */     this.file.readLock().lock();
/*     */     try {
/*  96 */       int read = this.file.read(this.pos, b, off, len);
/*  97 */       if (read == -1) {
/*  98 */         this.finished = true;
/*     */       } else {
/* 100 */         this.pos += read;
/*     */       } 
/*     */       
/* 103 */       this.file.updateAccessTime();
/* 104 */       return read;
/*     */     } finally {
/* 106 */       this.file.readLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/* 112 */     if (n <= 0L) {
/* 113 */       return 0L;
/*     */     }
/*     */     
/* 116 */     synchronized (this) {
/* 117 */       checkNotClosed();
/* 118 */       if (this.finished) {
/* 119 */         return 0L;
/*     */       }
/*     */ 
/*     */       
/* 123 */       int skip = (int)Math.min(Math.max(this.file.size() - this.pos, 0L), n);
/* 124 */       this.pos += skip;
/* 125 */       return skip;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int available() throws IOException {
/* 131 */     checkNotClosed();
/* 132 */     if (this.finished) {
/* 133 */       return 0;
/*     */     }
/* 135 */     long available = Math.max(this.file.size() - this.pos, 0L);
/* 136 */     return Ints.saturatedCast(available);
/*     */   }
/*     */   
/*     */   @GuardedBy("this")
/*     */   private void checkNotClosed() throws IOException {
/* 141 */     if (this.file == null) {
/* 142 */       throw new IOException("stream is closed");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void close() throws IOException {
/* 148 */     if (isOpen()) {
/* 149 */       this.fileSystemState.unregister(this);
/* 150 */       this.file.closed();
/*     */ 
/*     */       
/* 153 */       this.file = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   @GuardedBy("this")
/*     */   private boolean isOpen() {
/* 159 */     return (this.file != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\JimfsInputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */