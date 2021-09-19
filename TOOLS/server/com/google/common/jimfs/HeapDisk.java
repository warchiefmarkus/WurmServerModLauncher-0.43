/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.LongMath;
/*     */ import java.io.IOException;
/*     */ import java.math.RoundingMode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class HeapDisk
/*     */ {
/*     */   private final int blockSize;
/*     */   private final int maxBlockCount;
/*     */   private final int maxCachedBlockCount;
/*     */   @VisibleForTesting
/*     */   final RegularFile blockCache;
/*     */   private int allocatedBlockCount;
/*     */   
/*     */   public HeapDisk(Configuration config) {
/*  60 */     this.blockSize = config.blockSize;
/*  61 */     this.maxBlockCount = toBlockCount(config.maxSize, this.blockSize);
/*  62 */     this.maxCachedBlockCount = (config.maxCacheSize == -1L) ? this.maxBlockCount : toBlockCount(config.maxCacheSize, this.blockSize);
/*     */     
/*  64 */     this.blockCache = createBlockCache(this.maxCachedBlockCount);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int toBlockCount(long size, int blockSize) {
/*  69 */     return (int)LongMath.divide(size, blockSize, RoundingMode.FLOOR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HeapDisk(int blockSize, int maxBlockCount, int maxCachedBlockCount) {
/*  77 */     Preconditions.checkArgument((blockSize > 0), "blockSize (%s) must be positive", new Object[] { Integer.valueOf(blockSize) });
/*  78 */     Preconditions.checkArgument((maxBlockCount > 0), "maxBlockCount (%s) must be positive", new Object[] { Integer.valueOf(maxBlockCount) });
/*  79 */     Preconditions.checkArgument((maxCachedBlockCount >= 0), "maxCachedBlockCount must be non-negative", new Object[] { Integer.valueOf(maxCachedBlockCount) });
/*     */     
/*  81 */     this.blockSize = blockSize;
/*  82 */     this.maxBlockCount = maxBlockCount;
/*  83 */     this.maxCachedBlockCount = maxCachedBlockCount;
/*  84 */     this.blockCache = createBlockCache(maxCachedBlockCount);
/*     */   }
/*     */   
/*     */   private RegularFile createBlockCache(int maxCachedBlockCount) {
/*  88 */     return new RegularFile(-1, this, new byte[Math.min(maxCachedBlockCount, 8192)][], 0, 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int blockSize() {
/*  95 */     return this.blockSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized long getTotalSpace() {
/* 103 */     return this.maxBlockCount * this.blockSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized long getUnallocatedSpace() {
/* 112 */     return (this.maxBlockCount - this.allocatedBlockCount) * this.blockSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void allocate(RegularFile file, int count) throws IOException {
/* 119 */     int newAllocatedBlockCount = this.allocatedBlockCount + count;
/* 120 */     if (newAllocatedBlockCount > this.maxBlockCount) {
/* 121 */       throw new IOException("out of disk space");
/*     */     }
/*     */     
/* 124 */     int newBlocksNeeded = Math.max(count - this.blockCache.blockCount(), 0);
/*     */     
/* 126 */     for (int i = 0; i < newBlocksNeeded; i++) {
/* 127 */       file.addBlock(new byte[this.blockSize]);
/*     */     }
/*     */     
/* 130 */     if (newBlocksNeeded != count) {
/* 131 */       this.blockCache.transferBlocksTo(file, count - newBlocksNeeded);
/*     */     }
/*     */     
/* 134 */     this.allocatedBlockCount = newAllocatedBlockCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void free(RegularFile file) {
/* 141 */     free(file, file.blockCount());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void free(RegularFile file, int count) {
/* 148 */     int remainingCacheSpace = this.maxCachedBlockCount - this.blockCache.blockCount();
/* 149 */     if (remainingCacheSpace > 0) {
/* 150 */       file.copyBlocksTo(this.blockCache, Math.min(count, remainingCacheSpace));
/*     */     }
/* 152 */     file.truncateBlocks(file.blockCount() - count);
/*     */     
/* 154 */     this.allocatedBlockCount -= count;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\HeapDisk.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */