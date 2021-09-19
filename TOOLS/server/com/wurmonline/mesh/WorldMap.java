/*     */ package com.wurmonline.mesh;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.ShortBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.util.Arrays;
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
/*     */ public final class WorldMap
/*     */ {
/*  40 */   private static final byte[] HEADER_PREFIX = "WURMMAP".getBytes();
/*     */   
/*     */   private static final byte FILE_FORMAT_VERSION = 0;
/*     */   private static final int HEADER_SIZE = 12;
/*     */   private final int width;
/*     */   private Chunk[][] chunks;
/*     */   private final FileChannel channel;
/*  47 */   private final ByteBuffer byteBuffer = ByteBuffer.allocate(16384);
/*     */ 
/*     */   
/*     */   private WorldMap(FileChannel channel, int width, int height) {
/*  51 */     this.width = width;
/*  52 */     this.chunks = new Chunk[width][height];
/*  53 */     this.channel = channel;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void freeChunk(int x, int y) throws IOException {
/*  58 */     if (this.chunks[x][y] != null) {
/*     */       
/*  60 */       if (this.chunks[x][y].isDirty()) {
/*     */         
/*  62 */         long pos = (x + y * this.width) * 16384L + 12L;
/*  63 */         this.channel.position(pos);
/*     */         
/*  65 */         this.byteBuffer.clear();
/*  66 */         IntBuffer ib = this.byteBuffer.asIntBuffer();
/*  67 */         this.chunks[x][y].encode(ib);
/*  68 */         this.byteBuffer.position(ib.position() * 4);
/*  69 */         this.byteBuffer.flip();
/*     */         
/*  71 */         this.channel.write(this.byteBuffer);
/*     */       } 
/*     */       
/*  74 */       this.chunks[x][y] = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadChunk(int x, int y) throws IOException {
/*  80 */     if (this.chunks[x][y] == null) {
/*     */       
/*  82 */       long pos = (x + y * this.width) * 16384L + 12L;
/*  83 */       this.channel.position(pos);
/*     */       
/*  85 */       this.byteBuffer.clear();
/*  86 */       this.channel.read(this.byteBuffer);
/*  87 */       this.byteBuffer.flip();
/*     */       
/*  89 */       IntBuffer ib = this.byteBuffer.asIntBuffer();
/*  90 */       this.chunks[x][y] = Chunk.decode(ib);
/*  91 */       this.byteBuffer.position(ib.position() * 4);
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
/*     */   public static WorldMap getWorldMap(File file) throws IOException {
/* 175 */     if (file.exists()) {
/*     */ 
/*     */ 
/*     */       
/* 179 */       RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
/* 180 */       FileChannel channel = randomAccessFile.getChannel();
/*     */       
/* 182 */       if (file.length() < 12L)
/*     */       {
/* 184 */         throw new IOException("Map file too small to even contain a header.");
/*     */       }
/*     */       
/* 187 */       channel.position(0L);
/* 188 */       ByteBuffer header = ByteBuffer.allocate(12);
/* 189 */       channel.read(header);
/*     */       
/* 191 */       header.flip();
/* 192 */       byte[] prefix = new byte[7];
/* 193 */       header.get(prefix);
/* 194 */       byte version = header.get();
/* 195 */       ShortBuffer headerShort = header.asShortBuffer();
/* 196 */       short width = headerShort.get();
/* 197 */       short height = headerShort.get();
/*     */       
/* 199 */       if (!Arrays.equals(prefix, HEADER_PREFIX))
/*     */       {
/* 201 */         throw new IOException("Bad map file header: " + new String(prefix) + ".");
/*     */       }
/* 203 */       if (version != 0)
/*     */       {
/* 205 */         throw new IOException("Bad map file format version number.");
/*     */       }
/* 207 */       if (file.length() != (width * height * 16384 + 12))
/*     */       {
/* 209 */         throw new IOException("Found the map file, but it was the wrong size. (found " + file.length() + ", expected " + (width * height * 16384 + 12) + ")");
/*     */       }
/*     */ 
/*     */       
/* 213 */       return new WorldMap(channel, width, height);
/*     */     } 
/*     */ 
/*     */     
/* 217 */     throw new IOException("Failed to locate mapfile");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\mesh\WorldMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */