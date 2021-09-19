/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.MappedByteBuffer;
/*     */ import java.nio.channels.AsynchronousCloseException;
/*     */ import java.nio.channels.AsynchronousFileChannel;
/*     */ import java.nio.channels.ClosedByInterruptException;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.FileLock;
/*     */ import java.nio.channels.FileLockInterruptionException;
/*     */ import java.nio.channels.NonReadableChannelException;
/*     */ import java.nio.channels.NonWritableChannelException;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class JimfsFileChannel
/*     */   extends FileChannel
/*     */ {
/*     */   @GuardedBy("blockingThreads")
/*  66 */   private final Set<Thread> blockingThreads = new HashSet<>();
/*     */   
/*     */   private final RegularFile file;
/*     */   
/*     */   private final FileSystemState fileSystemState;
/*     */   
/*     */   private final boolean read;
/*     */   
/*     */   private final boolean write;
/*     */   
/*     */   private final boolean append;
/*     */   @GuardedBy("this")
/*     */   private long position;
/*     */   
/*     */   public JimfsFileChannel(RegularFile file, Set<OpenOption> options, FileSystemState fileSystemState) {
/*  81 */     this.file = file;
/*  82 */     this.fileSystemState = fileSystemState;
/*  83 */     this.read = options.contains(StandardOpenOption.READ);
/*  84 */     this.write = options.contains(StandardOpenOption.WRITE);
/*  85 */     this.append = options.contains(StandardOpenOption.APPEND);
/*     */     
/*  87 */     fileSystemState.register(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsynchronousFileChannel asAsynchronousFileChannel(ExecutorService executor) {
/*  95 */     return new JimfsAsynchronousFileChannel(this, executor);
/*     */   }
/*     */   
/*     */   void checkReadable() {
/*  99 */     if (!this.read) {
/* 100 */       throw new NonReadableChannelException();
/*     */     }
/*     */   }
/*     */   
/*     */   void checkWritable() {
/* 105 */     if (!this.write) {
/* 106 */       throw new NonWritableChannelException();
/*     */     }
/*     */   }
/*     */   
/*     */   void checkOpen() throws ClosedChannelException {
/* 111 */     if (!isOpen()) {
/* 112 */       throw new ClosedChannelException();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean beginBlocking() {
/* 122 */     begin();
/* 123 */     synchronized (this.blockingThreads) {
/* 124 */       if (isOpen()) {
/* 125 */         this.blockingThreads.add(Thread.currentThread());
/* 126 */         return true;
/*     */       } 
/*     */       
/* 129 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void endBlocking(boolean completed) throws AsynchronousCloseException {
/* 138 */     synchronized (this.blockingThreads) {
/* 139 */       this.blockingThreads.remove(Thread.currentThread());
/*     */     } 
/* 141 */     end(completed);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(ByteBuffer dst) throws IOException {
/* 146 */     Preconditions.checkNotNull(dst);
/* 147 */     checkOpen();
/* 148 */     checkReadable();
/*     */     
/* 150 */     int read = 0;
/*     */     
/* 152 */     synchronized (this) {
/* 153 */       boolean completed = false;
/*     */       try {
/* 155 */         if (!beginBlocking()) {
/* 156 */           return 0;
/*     */         }
/* 158 */         this.file.readLock().lockInterruptibly();
/*     */         try {
/* 160 */           read = this.file.read(this.position, dst);
/* 161 */           if (read != -1) {
/* 162 */             this.position += read;
/*     */           }
/* 164 */           this.file.updateAccessTime();
/* 165 */           completed = true;
/*     */         } finally {
/* 167 */           this.file.readLock().unlock();
/*     */         } 
/* 169 */       } catch (InterruptedException e) {
/* 170 */         Thread.currentThread().interrupt();
/*     */       } finally {
/* 172 */         endBlocking(completed);
/*     */       } 
/*     */     } 
/*     */     
/* 176 */     return read;
/*     */   }
/*     */ 
/*     */   
/*     */   public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
/* 181 */     Preconditions.checkPositionIndexes(offset, offset + length, dsts.length);
/* 182 */     List<ByteBuffer> buffers = Arrays.<ByteBuffer>asList(dsts).subList(offset, offset + length);
/* 183 */     Util.checkNoneNull(buffers);
/* 184 */     checkOpen();
/* 185 */     checkReadable();
/*     */     
/* 187 */     long read = 0L;
/*     */     
/* 189 */     synchronized (this) {
/* 190 */       boolean completed = false;
/*     */       try {
/* 192 */         if (!beginBlocking()) {
/* 193 */           return 0L;
/*     */         }
/* 195 */         this.file.readLock().lockInterruptibly();
/*     */         try {
/* 197 */           read = this.file.read(this.position, buffers);
/* 198 */           if (read != -1L) {
/* 199 */             this.position += read;
/*     */           }
/* 201 */           this.file.updateAccessTime();
/* 202 */           completed = true;
/*     */         } finally {
/* 204 */           this.file.readLock().unlock();
/*     */         } 
/* 206 */       } catch (InterruptedException e) {
/* 207 */         Thread.currentThread().interrupt();
/*     */       } finally {
/* 209 */         endBlocking(completed);
/*     */       } 
/*     */     } 
/*     */     
/* 213 */     return read;
/*     */   }
/*     */ 
/*     */   
/*     */   public int write(ByteBuffer src) throws IOException {
/* 218 */     Preconditions.checkNotNull(src);
/* 219 */     checkOpen();
/* 220 */     checkWritable();
/*     */     
/* 222 */     int written = 0;
/*     */     
/* 224 */     synchronized (this) {
/* 225 */       boolean completed = false;
/*     */       try {
/* 227 */         if (!beginBlocking()) {
/* 228 */           return 0;
/*     */         }
/* 230 */         this.file.writeLock().lockInterruptibly();
/*     */         try {
/* 232 */           if (this.append) {
/* 233 */             this.position = this.file.size();
/*     */           }
/* 235 */           written = this.file.write(this.position, src);
/* 236 */           this.position += written;
/* 237 */           this.file.updateModifiedTime();
/* 238 */           completed = true;
/*     */         } finally {
/* 240 */           this.file.writeLock().unlock();
/*     */         } 
/* 242 */       } catch (InterruptedException e) {
/* 243 */         Thread.currentThread().interrupt();
/*     */       } finally {
/* 245 */         endBlocking(completed);
/*     */       } 
/*     */     } 
/*     */     
/* 249 */     return written;
/*     */   }
/*     */ 
/*     */   
/*     */   public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
/* 254 */     Preconditions.checkPositionIndexes(offset, offset + length, srcs.length);
/* 255 */     List<ByteBuffer> buffers = Arrays.<ByteBuffer>asList(srcs).subList(offset, offset + length);
/* 256 */     Util.checkNoneNull(buffers);
/* 257 */     checkOpen();
/* 258 */     checkWritable();
/*     */     
/* 260 */     long written = 0L;
/*     */     
/* 262 */     synchronized (this) {
/* 263 */       boolean completed = false;
/*     */       try {
/* 265 */         if (!beginBlocking()) {
/* 266 */           return 0L;
/*     */         }
/* 268 */         this.file.writeLock().lockInterruptibly();
/*     */         try {
/* 270 */           if (this.append) {
/* 271 */             this.position = this.file.size();
/*     */           }
/* 273 */           written = this.file.write(this.position, buffers);
/* 274 */           this.position += written;
/* 275 */           this.file.updateModifiedTime();
/* 276 */           completed = true;
/*     */         } finally {
/* 278 */           this.file.writeLock().unlock();
/*     */         } 
/* 280 */       } catch (InterruptedException e) {
/* 281 */         Thread.currentThread().interrupt();
/*     */       } finally {
/* 283 */         endBlocking(completed);
/*     */       } 
/*     */     } 
/*     */     
/* 287 */     return written;
/*     */   }
/*     */   
/*     */   public long position() throws IOException {
/*     */     long pos;
/* 292 */     checkOpen();
/*     */ 
/*     */ 
/*     */     
/* 296 */     synchronized (this) {
/* 297 */       boolean completed = false;
/*     */       try {
/* 299 */         begin();
/* 300 */         if (!isOpen()) {
/* 301 */           return 0L;
/*     */         }
/* 303 */         pos = this.position;
/* 304 */         completed = true;
/*     */       } finally {
/* 306 */         end(completed);
/*     */       } 
/*     */     } 
/*     */     
/* 310 */     return pos;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileChannel position(long newPosition) throws IOException {
/* 315 */     Util.checkNotNegative(newPosition, "newPosition");
/* 316 */     checkOpen();
/*     */     
/* 318 */     synchronized (this) {
/* 319 */       boolean completed = false;
/*     */       try {
/* 321 */         begin();
/* 322 */         if (!isOpen()) {
/* 323 */           return this;
/*     */         }
/* 325 */         this.position = newPosition;
/* 326 */         completed = true;
/*     */       } finally {
/* 328 */         end(completed);
/*     */       } 
/*     */     } 
/*     */     
/* 332 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public long size() throws IOException {
/* 337 */     checkOpen();
/*     */     
/* 339 */     long size = 0L;
/*     */     
/* 341 */     boolean completed = false;
/*     */     try {
/* 343 */       if (!beginBlocking()) {
/* 344 */         return 0L;
/*     */       }
/* 346 */       this.file.readLock().lockInterruptibly();
/*     */       try {
/* 348 */         size = this.file.sizeWithoutLocking();
/* 349 */         completed = true;
/*     */       } finally {
/* 351 */         this.file.readLock().unlock();
/*     */       } 
/* 353 */     } catch (InterruptedException e) {
/* 354 */       Thread.currentThread().interrupt();
/*     */     } finally {
/* 356 */       endBlocking(completed);
/*     */     } 
/*     */     
/* 359 */     return size;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileChannel truncate(long size) throws IOException {
/* 364 */     Util.checkNotNegative(size, "size");
/* 365 */     checkOpen();
/* 366 */     checkWritable();
/*     */     
/* 368 */     synchronized (this) {
/* 369 */       boolean completed = false;
/*     */       try {
/* 371 */         if (!beginBlocking()) {
/* 372 */           return this;
/*     */         }
/* 374 */         this.file.writeLock().lockInterruptibly();
/*     */         try {
/* 376 */           this.file.truncate(size);
/* 377 */           if (this.position > size) {
/* 378 */             this.position = size;
/*     */           }
/* 380 */           this.file.updateModifiedTime();
/* 381 */           completed = true;
/*     */         } finally {
/* 383 */           this.file.writeLock().unlock();
/*     */         } 
/* 385 */       } catch (InterruptedException e) {
/* 386 */         Thread.currentThread().interrupt();
/*     */       } finally {
/* 388 */         endBlocking(completed);
/*     */       } 
/*     */     } 
/*     */     
/* 392 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void force(boolean metaData) throws IOException {
/* 397 */     checkOpen();
/*     */ 
/*     */ 
/*     */     
/* 401 */     boolean completed = false;
/*     */     try {
/* 403 */       begin();
/* 404 */       completed = true;
/*     */     } finally {
/* 406 */       end(completed);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long transferTo(long position, long count, WritableByteChannel target) throws IOException {
/* 412 */     Preconditions.checkNotNull(target);
/* 413 */     Util.checkNotNegative(position, "position");
/* 414 */     Util.checkNotNegative(count, "count");
/* 415 */     checkOpen();
/* 416 */     checkReadable();
/*     */     
/* 418 */     long transferred = 0L;
/*     */ 
/*     */     
/* 421 */     boolean completed = false;
/*     */     try {
/* 423 */       if (!beginBlocking()) {
/* 424 */         return 0L;
/*     */       }
/* 426 */       this.file.readLock().lockInterruptibly();
/*     */       try {
/* 428 */         transferred = this.file.transferTo(position, count, target);
/* 429 */         this.file.updateAccessTime();
/* 430 */         completed = true;
/*     */       } finally {
/* 432 */         this.file.readLock().unlock();
/*     */       } 
/* 434 */     } catch (InterruptedException e) {
/* 435 */       Thread.currentThread().interrupt();
/*     */     } finally {
/* 437 */       endBlocking(completed);
/*     */     } 
/*     */     
/* 440 */     return transferred;
/*     */   }
/*     */ 
/*     */   
/*     */   public long transferFrom(ReadableByteChannel src, long position, long count) throws IOException {
/* 445 */     Preconditions.checkNotNull(src);
/* 446 */     Util.checkNotNegative(position, "position");
/* 447 */     Util.checkNotNegative(count, "count");
/* 448 */     checkOpen();
/* 449 */     checkWritable();
/*     */     
/* 451 */     long transferred = 0L;
/*     */     
/* 453 */     if (this.append) {
/*     */       
/* 455 */       synchronized (this) {
/* 456 */         boolean completed = false;
/*     */         try {
/* 458 */           if (!beginBlocking()) {
/* 459 */             return 0L;
/*     */           }
/*     */           
/* 462 */           this.file.writeLock().lockInterruptibly();
/*     */           try {
/* 464 */             position = this.file.sizeWithoutLocking();
/* 465 */             transferred = this.file.transferFrom(src, position, count);
/* 466 */             this.position = position + transferred;
/* 467 */             this.file.updateModifiedTime();
/* 468 */             completed = true;
/*     */           } finally {
/* 470 */             this.file.writeLock().unlock();
/*     */           } 
/* 472 */         } catch (InterruptedException e) {
/* 473 */           Thread.currentThread().interrupt();
/*     */         } finally {
/* 475 */           endBlocking(completed);
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 480 */       boolean completed = false;
/*     */       try {
/* 482 */         if (!beginBlocking()) {
/* 483 */           return 0L;
/*     */         }
/* 485 */         this.file.writeLock().lockInterruptibly();
/*     */         try {
/* 487 */           transferred = this.file.transferFrom(src, position, count);
/* 488 */           this.file.updateModifiedTime();
/* 489 */           completed = true;
/*     */         } finally {
/* 491 */           this.file.writeLock().unlock();
/*     */         } 
/* 493 */       } catch (InterruptedException e) {
/* 494 */         Thread.currentThread().interrupt();
/*     */       } finally {
/* 496 */         endBlocking(completed);
/*     */       } 
/*     */     } 
/*     */     
/* 500 */     return transferred;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(ByteBuffer dst, long position) throws IOException {
/* 505 */     Preconditions.checkNotNull(dst);
/* 506 */     Util.checkNotNegative(position, "position");
/* 507 */     checkOpen();
/* 508 */     checkReadable();
/*     */     
/* 510 */     int read = 0;
/*     */ 
/*     */     
/* 513 */     boolean completed = false;
/*     */     try {
/* 515 */       if (!beginBlocking()) {
/* 516 */         return 0;
/*     */       }
/* 518 */       this.file.readLock().lockInterruptibly();
/*     */       try {
/* 520 */         read = this.file.read(position, dst);
/* 521 */         this.file.updateAccessTime();
/* 522 */         completed = true;
/*     */       } finally {
/* 524 */         this.file.readLock().unlock();
/*     */       } 
/* 526 */     } catch (InterruptedException e) {
/* 527 */       Thread.currentThread().interrupt();
/*     */     } finally {
/* 529 */       endBlocking(completed);
/*     */     } 
/*     */     
/* 532 */     return read;
/*     */   }
/*     */ 
/*     */   
/*     */   public int write(ByteBuffer src, long position) throws IOException {
/* 537 */     Preconditions.checkNotNull(src);
/* 538 */     Util.checkNotNegative(position, "position");
/* 539 */     checkOpen();
/* 540 */     checkWritable();
/*     */     
/* 542 */     int written = 0;
/*     */     
/* 544 */     if (this.append) {
/*     */       
/* 546 */       synchronized (this) {
/* 547 */         boolean completed = false;
/*     */         try {
/* 549 */           if (!beginBlocking()) {
/* 550 */             return 0;
/*     */           }
/*     */           
/* 553 */           this.file.writeLock().lockInterruptibly();
/*     */           try {
/* 555 */             position = this.file.sizeWithoutLocking();
/* 556 */             written = this.file.write(position, src);
/* 557 */             this.position = position + written;
/* 558 */             this.file.updateModifiedTime();
/* 559 */             completed = true;
/*     */           } finally {
/* 561 */             this.file.writeLock().unlock();
/*     */           } 
/* 563 */         } catch (InterruptedException e) {
/* 564 */           Thread.currentThread().interrupt();
/*     */         } finally {
/* 566 */           endBlocking(completed);
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 571 */       boolean completed = false;
/*     */       try {
/* 573 */         if (!beginBlocking()) {
/* 574 */           return 0;
/*     */         }
/* 576 */         this.file.writeLock().lockInterruptibly();
/*     */         try {
/* 578 */           written = this.file.write(position, src);
/* 579 */           this.file.updateModifiedTime();
/* 580 */           completed = true;
/*     */         } finally {
/* 582 */           this.file.writeLock().unlock();
/*     */         } 
/* 584 */       } catch (InterruptedException e) {
/* 585 */         Thread.currentThread().interrupt();
/*     */       } finally {
/* 587 */         endBlocking(completed);
/*     */       } 
/*     */     } 
/*     */     
/* 591 */     return written;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MappedByteBuffer map(FileChannel.MapMode mode, long position, long size) throws IOException {
/* 598 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public FileLock lock(long position, long size, boolean shared) throws IOException {
/* 603 */     checkLockArguments(position, size, shared);
/*     */ 
/*     */     
/* 606 */     boolean completed = false;
/*     */     try {
/* 608 */       begin();
/* 609 */       completed = true;
/* 610 */       return new FakeFileLock(this, position, size, shared);
/*     */     } finally {
/*     */       try {
/* 613 */         end(completed);
/* 614 */       } catch (ClosedByInterruptException e) {
/* 615 */         throw new FileLockInterruptionException();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public FileLock tryLock(long position, long size, boolean shared) throws IOException {
/* 622 */     checkLockArguments(position, size, shared);
/*     */ 
/*     */     
/* 625 */     return new FakeFileLock(this, position, size, shared);
/*     */   }
/*     */   
/*     */   private void checkLockArguments(long position, long size, boolean shared) throws IOException {
/* 629 */     Util.checkNotNegative(position, "position");
/* 630 */     Util.checkNotNegative(size, "size");
/* 631 */     checkOpen();
/* 632 */     if (shared) {
/* 633 */       checkReadable();
/*     */     } else {
/* 635 */       checkWritable();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void implCloseChannel() {
/*     */     try {
/* 644 */       synchronized (this.blockingThreads) {
/* 645 */         for (Thread thread : this.blockingThreads) {
/* 646 */           thread.interrupt();
/*     */         }
/*     */       } 
/*     */     } finally {
/* 650 */       this.fileSystemState.unregister(this);
/* 651 */       this.file.closed();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class FakeFileLock
/*     */     extends FileLock
/*     */   {
/* 660 */     private final AtomicBoolean valid = new AtomicBoolean(true);
/*     */     
/*     */     public FakeFileLock(FileChannel channel, long position, long size, boolean shared) {
/* 663 */       super(channel, position, size, shared);
/*     */     }
/*     */     
/*     */     public FakeFileLock(AsynchronousFileChannel channel, long position, long size, boolean shared) {
/* 667 */       super(channel, position, size, shared);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isValid() {
/* 672 */       return this.valid.get();
/*     */     }
/*     */ 
/*     */     
/*     */     public void release() throws IOException {
/* 677 */       this.valid.set(false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\JimfsFileChannel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */