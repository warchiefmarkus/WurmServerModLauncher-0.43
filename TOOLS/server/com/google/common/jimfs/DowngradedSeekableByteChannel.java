/*    */ package com.google.common.jimfs;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.channels.FileChannel;
/*    */ import java.nio.channels.SeekableByteChannel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class DowngradedSeekableByteChannel
/*    */   implements SeekableByteChannel
/*    */ {
/*    */   private final FileChannel channel;
/*    */   
/*    */   DowngradedSeekableByteChannel(FileChannel channel) {
/* 37 */     this.channel = (FileChannel)Preconditions.checkNotNull(channel);
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(ByteBuffer dst) throws IOException {
/* 42 */     return this.channel.read(dst);
/*    */   }
/*    */ 
/*    */   
/*    */   public int write(ByteBuffer src) throws IOException {
/* 47 */     return this.channel.write(src);
/*    */   }
/*    */ 
/*    */   
/*    */   public long position() throws IOException {
/* 52 */     return this.channel.position();
/*    */   }
/*    */ 
/*    */   
/*    */   public SeekableByteChannel position(long newPosition) throws IOException {
/* 57 */     this.channel.position(newPosition);
/* 58 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public long size() throws IOException {
/* 63 */     return this.channel.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public SeekableByteChannel truncate(long size) throws IOException {
/* 68 */     this.channel.truncate(size);
/* 69 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isOpen() {
/* 74 */     return this.channel.isOpen();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 79 */     this.channel.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\DowngradedSeekableByteChannel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */