/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.util.concurrent.ListenableFuture;
/*     */ import com.google.common.util.concurrent.ListeningExecutorService;
/*     */ import com.google.common.util.concurrent.MoreExecutors;
/*     */ import com.google.common.util.concurrent.SettableFuture;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.AsynchronousFileChannel;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.CompletionHandler;
/*     */ import java.nio.channels.FileLock;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
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
/*     */ final class JimfsAsynchronousFileChannel
/*     */   extends AsynchronousFileChannel
/*     */ {
/*     */   private final JimfsFileChannel channel;
/*     */   private final ListeningExecutorService executor;
/*     */   
/*     */   public JimfsAsynchronousFileChannel(JimfsFileChannel channel, ExecutorService executor) {
/*  50 */     this.channel = (JimfsFileChannel)Preconditions.checkNotNull(channel);
/*  51 */     this.executor = MoreExecutors.listeningDecorator(executor);
/*     */   }
/*     */ 
/*     */   
/*     */   public long size() throws IOException {
/*  56 */     return this.channel.size();
/*     */   }
/*     */ 
/*     */   
/*     */   private <R, A> void addCallback(ListenableFuture<R> future, CompletionHandler<R, ? super A> handler, @Nullable A attachment) {
/*  61 */     future.addListener(new CompletionHandlerCallback<>(future, handler, attachment), (Executor)this.executor);
/*     */   }
/*     */ 
/*     */   
/*     */   public AsynchronousFileChannel truncate(long size) throws IOException {
/*  66 */     this.channel.truncate(size);
/*  67 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void force(boolean metaData) throws IOException {
/*  72 */     this.channel.force(metaData);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A> void lock(long position, long size, boolean shared, @Nullable A attachment, CompletionHandler<FileLock, ? super A> handler) {
/*  82 */     Preconditions.checkNotNull(handler);
/*  83 */     addCallback(lock(position, size, shared), handler, attachment);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFuture<FileLock> lock(final long position, final long size, final boolean shared) {
/*  89 */     Util.checkNotNegative(position, "position");
/*  90 */     Util.checkNotNegative(size, "size");
/*  91 */     if (!isOpen()) {
/*  92 */       return closedChannelFuture();
/*     */     }
/*  94 */     if (shared) {
/*  95 */       this.channel.checkReadable();
/*     */     } else {
/*  97 */       this.channel.checkWritable();
/*     */     } 
/*  99 */     return this.executor.submit(new Callable<FileLock>()
/*     */         {
/*     */           public FileLock call() throws IOException
/*     */           {
/* 103 */             return JimfsAsynchronousFileChannel.this.tryLock(position, size, shared);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public FileLock tryLock(long position, long size, boolean shared) throws IOException {
/* 110 */     Util.checkNotNegative(position, "position");
/* 111 */     Util.checkNotNegative(size, "size");
/* 112 */     this.channel.checkOpen();
/* 113 */     if (shared) {
/* 114 */       this.channel.checkReadable();
/*     */     } else {
/* 116 */       this.channel.checkWritable();
/*     */     } 
/* 118 */     return new JimfsFileChannel.FakeFileLock(this, position, size, shared);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A> void read(ByteBuffer dst, long position, @Nullable A attachment, CompletionHandler<Integer, ? super A> handler) {
/* 127 */     addCallback(read(dst, position), handler, attachment);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<Integer> read(final ByteBuffer dst, final long position) {
/* 132 */     Preconditions.checkArgument(!dst.isReadOnly(), "dst may not be read-only");
/* 133 */     Util.checkNotNegative(position, "position");
/* 134 */     if (!isOpen()) {
/* 135 */       return closedChannelFuture();
/*     */     }
/* 137 */     this.channel.checkReadable();
/* 138 */     return this.executor.submit(new Callable<Integer>()
/*     */         {
/*     */           public Integer call() throws IOException
/*     */           {
/* 142 */             return Integer.valueOf(JimfsAsynchronousFileChannel.this.channel.read(dst, position));
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A> void write(ByteBuffer src, long position, @Nullable A attachment, CompletionHandler<Integer, ? super A> handler) {
/* 153 */     addCallback(write(src, position), handler, attachment);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<Integer> write(final ByteBuffer src, final long position) {
/* 158 */     Util.checkNotNegative(position, "position");
/* 159 */     if (!isOpen()) {
/* 160 */       return closedChannelFuture();
/*     */     }
/* 162 */     this.channel.checkWritable();
/* 163 */     return this.executor.submit(new Callable<Integer>()
/*     */         {
/*     */           public Integer call() throws IOException
/*     */           {
/* 167 */             return Integer.valueOf(JimfsAsynchronousFileChannel.this.channel.write(src, position));
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 174 */     return this.channel.isOpen();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 179 */     this.channel.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <V> ListenableFuture<V> closedChannelFuture() {
/* 186 */     SettableFuture<V> future = SettableFuture.create();
/* 187 */     future.setException(new ClosedChannelException());
/* 188 */     return (ListenableFuture<V>)future;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class CompletionHandlerCallback<R, A>
/*     */     implements Runnable
/*     */   {
/*     */     private final ListenableFuture<R> future;
/*     */     
/*     */     private final CompletionHandler<R, ? super A> completionHandler;
/*     */     
/*     */     @Nullable
/*     */     private final A attachment;
/*     */ 
/*     */     
/*     */     private CompletionHandlerCallback(ListenableFuture<R> future, CompletionHandler<R, ? super A> completionHandler, @Nullable A attachment) {
/* 204 */       this.future = (ListenableFuture<R>)Preconditions.checkNotNull(future);
/* 205 */       this.completionHandler = (CompletionHandler<R, ? super A>)Preconditions.checkNotNull(completionHandler);
/* 206 */       this.attachment = attachment;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       R result;
/*     */       try {
/* 213 */         result = (R)this.future.get();
/* 214 */       } catch (ExecutionException e) {
/* 215 */         onFailure(e.getCause());
/*     */         return;
/* 217 */       } catch (InterruptedException|RuntimeException|Error e) {
/*     */ 
/*     */         
/* 220 */         onFailure(e);
/*     */         
/*     */         return;
/*     */       } 
/* 224 */       onSuccess(result);
/*     */     }
/*     */     
/*     */     private void onSuccess(R result) {
/* 228 */       this.completionHandler.completed(result, this.attachment);
/*     */     }
/*     */     
/*     */     private void onFailure(Throwable t) {
/* 232 */       this.completionHandler.failed(t, this.attachment);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\JimfsAsynchronousFileChannel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */