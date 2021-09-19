/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.UnsignedBytes;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
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
/*     */ final class RegularFile
/*     */   extends File
/*     */ {
/*  45 */   private final ReadWriteLock lock = new ReentrantReadWriteLock();
/*     */   
/*     */   private final HeapDisk disk;
/*     */   
/*     */   private byte[][] blocks;
/*     */   
/*     */   private int blockCount;
/*     */   
/*     */   private long size;
/*     */   
/*     */   private int openCount;
/*     */   
/*     */   private boolean deleted;
/*     */   
/*     */   public static RegularFile create(int id, HeapDisk disk) {
/*  60 */     return new RegularFile(id, disk, new byte[32][], 0, 0L);
/*     */   }
/*     */   
/*     */   RegularFile(int id, HeapDisk disk, byte[][] blocks, int blockCount, long size) {
/*  64 */     super(id);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  73 */     this.openCount = 0;
/*  74 */     this.deleted = false;
/*     */     this.disk = (HeapDisk)Preconditions.checkNotNull(disk);
/*     */     this.blocks = (byte[][])Preconditions.checkNotNull(blocks);
/*     */     this.blockCount = blockCount;
/*     */     Preconditions.checkArgument((size >= 0L));
/*     */     this.size = size; } public Lock readLock() {
/*  80 */     return this.lock.readLock();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Lock writeLock() {
/*  87 */     return this.lock.writeLock();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void expandIfNecessary(int minBlockCount) {
/*  93 */     if (minBlockCount > this.blocks.length) {
/*  94 */       this.blocks = Arrays.<byte[]>copyOf(this.blocks, Util.nextPowerOf2(minBlockCount));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int blockCount() {
/* 102 */     return this.blockCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void copyBlocksTo(RegularFile target, int count) {
/* 109 */     int start = this.blockCount - count;
/* 110 */     int targetEnd = target.blockCount + count;
/* 111 */     target.expandIfNecessary(targetEnd);
/*     */     
/* 113 */     System.arraycopy(this.blocks, start, target.blocks, target.blockCount, count);
/* 114 */     target.blockCount = targetEnd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void transferBlocksTo(RegularFile target, int count) {
/* 121 */     copyBlocksTo(target, count);
/* 122 */     truncateBlocks(this.blockCount - count);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void truncateBlocks(int count) {
/* 129 */     Util.clear(this.blocks, count, this.blockCount - count);
/* 130 */     this.blockCount = count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void addBlock(byte[] block) {
/* 137 */     expandIfNecessary(this.blockCount + 1);
/* 138 */     this.blocks[this.blockCount++] = block;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   byte[] getBlock(int index) {
/* 146 */     return this.blocks[index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long sizeWithoutLocking() {
/* 156 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long size() {
/* 163 */     readLock().lock();
/*     */     try {
/* 165 */       return this.size;
/*     */     } finally {
/* 167 */       readLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   RegularFile copyWithoutContent(int id) {
/* 173 */     byte[][] copyBlocks = new byte[Math.max(this.blockCount * 2, 32)][];
/* 174 */     return new RegularFile(id, this.disk, copyBlocks, 0, this.size);
/*     */   }
/*     */ 
/*     */   
/*     */   void copyContentTo(File file) throws IOException {
/* 179 */     RegularFile copy = (RegularFile)file;
/* 180 */     this.disk.allocate(copy, this.blockCount);
/*     */     
/* 182 */     for (int i = 0; i < this.blockCount; i++) {
/* 183 */       byte[] block = this.blocks[i];
/* 184 */       byte[] copyBlock = copy.blocks[i];
/* 185 */       System.arraycopy(block, 0, copyBlock, 0, block.length);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   ReadWriteLock contentLock() {
/* 191 */     return this.lock;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void opened() {
/* 199 */     this.openCount++;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void closed() {
/* 204 */     if (--this.openCount == 0 && this.deleted) {
/* 205 */       deleteContents();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void deleted() {
/* 215 */     if (links() == 0) {
/* 216 */       this.deleted = true;
/* 217 */       if (this.openCount == 0) {
/* 218 */         deleteContents();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void deleteContents() {
/* 228 */     this.disk.free(this);
/* 229 */     this.size = 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean truncate(long size) {
/* 240 */     if (size >= this.size) {
/* 241 */       return false;
/*     */     }
/*     */     
/* 244 */     long lastPosition = size - 1L;
/* 245 */     this.size = size;
/*     */     
/* 247 */     int newBlockCount = blockIndex(lastPosition) + 1;
/* 248 */     int blocksToRemove = this.blockCount - newBlockCount;
/* 249 */     if (blocksToRemove > 0) {
/* 250 */       this.disk.free(this, blocksToRemove);
/*     */     }
/*     */     
/* 253 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void prepareForWrite(long pos, long len) throws IOException {
/* 260 */     long end = pos + len;
/*     */ 
/*     */     
/* 263 */     int lastBlockIndex = this.blockCount - 1;
/* 264 */     int endBlockIndex = blockIndex(end - 1L);
/*     */     
/* 266 */     if (endBlockIndex > lastBlockIndex) {
/* 267 */       int additionalBlocksNeeded = endBlockIndex - lastBlockIndex;
/* 268 */       this.disk.allocate(this, additionalBlocksNeeded);
/*     */     } 
/*     */ 
/*     */     
/* 272 */     if (pos > this.size) {
/* 273 */       long remaining = pos - this.size;
/*     */       
/* 275 */       int blockIndex = blockIndex(this.size);
/* 276 */       byte[] block = this.blocks[blockIndex];
/* 277 */       int off = offsetInBlock(this.size);
/*     */       
/* 279 */       remaining -= zero(block, off, length(off, remaining));
/*     */       
/* 281 */       while (remaining > 0L) {
/* 282 */         block = this.blocks[++blockIndex];
/*     */         
/* 284 */         remaining -= zero(block, 0, length(remaining));
/*     */       } 
/*     */       
/* 287 */       this.size = pos;
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
/*     */   public int write(long pos, byte b) throws IOException {
/* 299 */     prepareForWrite(pos, 1L);
/*     */     
/* 301 */     byte[] block = this.blocks[blockIndex(pos)];
/* 302 */     int off = offsetInBlock(pos);
/* 303 */     block[off] = b;
/*     */     
/* 305 */     if (pos >= this.size) {
/* 306 */       this.size = pos + 1L;
/*     */     }
/*     */     
/* 309 */     return 1;
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
/*     */   public int write(long pos, byte[] b, int off, int len) throws IOException {
/* 321 */     prepareForWrite(pos, len);
/*     */     
/* 323 */     if (len == 0) {
/* 324 */       return 0;
/*     */     }
/*     */     
/* 327 */     int remaining = len;
/*     */     
/* 329 */     int blockIndex = blockIndex(pos);
/* 330 */     byte[] block = this.blocks[blockIndex];
/* 331 */     int offInBlock = offsetInBlock(pos);
/*     */     
/* 333 */     int written = put(block, offInBlock, b, off, length(offInBlock, remaining));
/* 334 */     remaining -= written;
/* 335 */     off += written;
/*     */     
/* 337 */     while (remaining > 0) {
/* 338 */       block = this.blocks[++blockIndex];
/*     */       
/* 340 */       written = put(block, 0, b, off, length(remaining));
/* 341 */       remaining -= written;
/* 342 */       off += written;
/*     */     } 
/*     */     
/* 345 */     long endPos = pos + len;
/* 346 */     if (endPos > this.size) {
/* 347 */       this.size = endPos;
/*     */     }
/*     */     
/* 350 */     return len;
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
/*     */   public int write(long pos, ByteBuffer buf) throws IOException {
/* 362 */     int len = buf.remaining();
/*     */     
/* 364 */     prepareForWrite(pos, len);
/*     */     
/* 366 */     if (len == 0) {
/* 367 */       return 0;
/*     */     }
/*     */     
/* 370 */     int blockIndex = blockIndex(pos);
/* 371 */     byte[] block = this.blocks[blockIndex];
/* 372 */     int off = offsetInBlock(pos);
/*     */     
/* 374 */     put(block, off, buf);
/*     */     
/* 376 */     while (buf.hasRemaining()) {
/* 377 */       block = this.blocks[++blockIndex];
/*     */       
/* 379 */       put(block, 0, buf);
/*     */     } 
/*     */     
/* 382 */     long endPos = pos + len;
/* 383 */     if (endPos > this.size) {
/* 384 */       this.size = endPos;
/*     */     }
/*     */     
/* 387 */     return len;
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
/*     */   public long write(long pos, Iterable<ByteBuffer> bufs) throws IOException {
/* 399 */     long start = pos;
/* 400 */     for (ByteBuffer buf : bufs) {
/* 401 */       pos += write(pos, buf);
/*     */     }
/* 403 */     return pos - start;
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
/*     */   public long transferFrom(ReadableByteChannel src, long pos, long count) throws IOException {
/* 415 */     prepareForWrite(pos, 0L);
/*     */     
/* 417 */     if (count == 0L) {
/* 418 */       return 0L;
/*     */     }
/*     */     
/* 421 */     long remaining = count;
/*     */     
/* 423 */     int blockIndex = blockIndex(pos);
/* 424 */     byte[] block = blockForWrite(blockIndex);
/* 425 */     int off = offsetInBlock(pos);
/*     */     
/* 427 */     ByteBuffer buf = ByteBuffer.wrap(block, off, length(off, remaining));
/*     */     
/* 429 */     long currentPos = pos;
/* 430 */     int read = 0;
/* 431 */     while (buf.hasRemaining()) {
/* 432 */       read = src.read(buf);
/* 433 */       if (read == -1) {
/*     */         break;
/*     */       }
/*     */       
/* 437 */       currentPos += read;
/* 438 */       remaining -= read;
/*     */     } 
/*     */ 
/*     */     
/* 442 */     if (currentPos > this.size) {
/* 443 */       this.size = currentPos;
/*     */     }
/*     */     
/* 446 */     if (read != -1)
/*     */     {
/* 448 */       while (remaining > 0L) {
/* 449 */         block = blockForWrite(++blockIndex);
/*     */         
/* 451 */         buf = ByteBuffer.wrap(block, 0, length(remaining));
/* 452 */         while (buf.hasRemaining()) {
/* 453 */           read = src.read(buf);
/* 454 */           if (read == -1) {
/*     */             // Byte code: goto -> 229
/*     */           }
/*     */           
/* 458 */           currentPos += read;
/* 459 */           remaining -= read;
/*     */         } 
/*     */         
/* 462 */         if (currentPos > this.size) {
/* 463 */           this.size = currentPos;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 468 */     if (currentPos > this.size) {
/* 469 */       this.size = currentPos;
/*     */     }
/*     */     
/* 472 */     return currentPos - pos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(long pos) {
/* 480 */     if (pos >= this.size) {
/* 481 */       return -1;
/*     */     }
/*     */     
/* 484 */     byte[] block = this.blocks[blockIndex(pos)];
/* 485 */     int off = offsetInBlock(pos);
/* 486 */     return UnsignedBytes.toInt(block[off]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(long pos, byte[] b, int off, int len) {
/* 496 */     int bytesToRead = (int)bytesToRead(pos, len);
/*     */     
/* 498 */     if (bytesToRead > 0) {
/* 499 */       int remaining = bytesToRead;
/*     */       
/* 501 */       int blockIndex = blockIndex(pos);
/* 502 */       byte[] block = this.blocks[blockIndex];
/* 503 */       int offsetInBlock = offsetInBlock(pos);
/*     */       
/* 505 */       int read = get(block, offsetInBlock, b, off, length(offsetInBlock, remaining));
/* 506 */       remaining -= read;
/* 507 */       off += read;
/*     */       
/* 509 */       while (remaining > 0) {
/* 510 */         int index = ++blockIndex;
/* 511 */         block = this.blocks[index];
/*     */         
/* 513 */         read = get(block, 0, b, off, length(remaining));
/* 514 */         remaining -= read;
/* 515 */         off += read;
/*     */       } 
/*     */     } 
/*     */     
/* 519 */     return bytesToRead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(long pos, ByteBuffer buf) {
/* 529 */     int bytesToRead = (int)bytesToRead(pos, buf.remaining());
/*     */     
/* 531 */     if (bytesToRead > 0) {
/* 532 */       int remaining = bytesToRead;
/*     */       
/* 534 */       int blockIndex = blockIndex(pos);
/* 535 */       byte[] block = this.blocks[blockIndex];
/* 536 */       int off = offsetInBlock(pos);
/*     */       
/* 538 */       remaining -= get(block, off, buf, length(off, remaining));
/*     */       
/* 540 */       while (remaining > 0) {
/* 541 */         int index = ++blockIndex;
/* 542 */         block = this.blocks[index];
/* 543 */         remaining -= get(block, 0, buf, length(remaining));
/*     */       } 
/*     */     } 
/*     */     
/* 547 */     return bytesToRead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long read(long pos, Iterable<ByteBuffer> bufs) {
/* 556 */     if (pos >= size()) {
/* 557 */       return -1L;
/*     */     }
/*     */     
/* 560 */     long start = pos;
/* 561 */     for (ByteBuffer buf : bufs) {
/* 562 */       int read = read(pos, buf);
/* 563 */       if (read == -1) {
/*     */         break;
/*     */       }
/* 566 */       pos += read;
/*     */     } 
/*     */ 
/*     */     
/* 570 */     return pos - start;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long transferTo(long pos, long count, WritableByteChannel dest) throws IOException {
/* 581 */     long bytesToRead = bytesToRead(pos, count);
/*     */     
/* 583 */     if (bytesToRead > 0L) {
/* 584 */       long remaining = bytesToRead;
/*     */       
/* 586 */       int blockIndex = blockIndex(pos);
/* 587 */       byte[] block = this.blocks[blockIndex];
/* 588 */       int off = offsetInBlock(pos);
/*     */       
/* 590 */       ByteBuffer buf = ByteBuffer.wrap(block, off, length(off, remaining));
/* 591 */       while (buf.hasRemaining()) {
/* 592 */         remaining -= dest.write(buf);
/*     */       }
/* 594 */       buf.clear();
/*     */       
/* 596 */       while (remaining > 0L) {
/* 597 */         int index = ++blockIndex;
/* 598 */         block = this.blocks[index];
/*     */         
/* 600 */         buf = ByteBuffer.wrap(block, 0, length(remaining));
/* 601 */         while (buf.hasRemaining()) {
/* 602 */           remaining -= dest.write(buf);
/*     */         }
/* 604 */         buf.clear();
/*     */       } 
/*     */     } 
/*     */     
/* 608 */     return Math.max(bytesToRead, 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] blockForWrite(int index) throws IOException {
/* 615 */     if (index >= this.blockCount) {
/* 616 */       int additionalBlocksNeeded = index - this.blockCount + 1;
/* 617 */       this.disk.allocate(this, additionalBlocksNeeded);
/*     */     } 
/*     */     
/* 620 */     return this.blocks[index];
/*     */   }
/*     */   
/*     */   private int blockIndex(long position) {
/* 624 */     return (int)(position / this.disk.blockSize());
/*     */   }
/*     */   
/*     */   private int offsetInBlock(long position) {
/* 628 */     return (int)(position % this.disk.blockSize());
/*     */   }
/*     */   
/*     */   private int length(long max) {
/* 632 */     return (int)Math.min(this.disk.blockSize(), max);
/*     */   }
/*     */   
/*     */   private int length(int off, long max) {
/* 636 */     return (int)Math.min((this.disk.blockSize() - off), max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long bytesToRead(long pos, long max) {
/* 644 */     long available = this.size - pos;
/* 645 */     if (available <= 0L) {
/* 646 */       return -1L;
/*     */     }
/* 648 */     return Math.min(available, max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int zero(byte[] block, int offset, int len) {
/* 655 */     Util.zero(block, offset, len);
/* 656 */     return len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int put(byte[] block, int offset, byte[] b, int off, int len) {
/* 663 */     System.arraycopy(b, off, block, offset, len);
/* 664 */     return len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int put(byte[] block, int offset, ByteBuffer buf) {
/* 671 */     int len = Math.min(block.length - offset, buf.remaining());
/* 672 */     buf.get(block, offset, len);
/* 673 */     return len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int get(byte[] block, int offset, byte[] b, int off, int len) {
/* 681 */     System.arraycopy(block, offset, b, off, len);
/* 682 */     return len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int get(byte[] block, int offset, ByteBuffer buf, int len) {
/* 689 */     buf.put(block, offset, len);
/* 690 */     return len;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\RegularFile.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */