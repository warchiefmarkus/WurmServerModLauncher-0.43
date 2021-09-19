/*     */ package com.wurmonline.javaone;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.MappedByteBuffer;
/*     */ import java.nio.channels.FileChannel;
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
/*     */ public final class MeshIO
/*     */ {
/*     */   public static final long MAGIC_NUMBER = 5136955264682433437L;
/*     */   private static final int ROW_COUNT = 512;
/*     */   private int sizeLevel;
/*     */   private int size;
/*     */   private final int[] data;
/*     */   private FileChannel fileChannel;
/*     */   private ByteBuffer tmpBuf;
/*     */   private IntBuffer tmpBufInt;
/*  39 */   private boolean[] rowDirty = new boolean[512];
/*  40 */   private int rowId = 0;
/*     */   
/*     */   private int linesPerRow;
/*     */   
/*     */   private MeshIO(int aSizeLevel, int[] aData) {
/*  45 */     if (aSizeLevel > 32)
/*  46 */       throw new IllegalArgumentException("I'm fairly sure you didn't mean to REALLY create a map 2^" + aSizeLevel + " units wide."); 
/*  47 */     if (aSizeLevel < 4)
/*  48 */       throw new IllegalArgumentException("Maps can't be smaller than 2^4."); 
/*  49 */     this.sizeLevel = aSizeLevel;
/*  50 */     this.size = 1 << aSizeLevel;
/*  51 */     this.data = aData;
/*  52 */     this.tmpBuf = ByteBuffer.allocate(this.size * 4);
/*  53 */     this.tmpBufInt = this.tmpBuf.asIntBuffer();
/*  54 */     this.linesPerRow = this.size / 512;
/*     */   }
/*     */ 
/*     */   
/*     */   private MeshIO(ByteBuffer header) throws IOException {
/*  59 */     readHeader(header);
/*  60 */     this.data = new int[this.size * this.size];
/*  61 */     this.tmpBuf = ByteBuffer.allocate(this.size * 4);
/*  62 */     this.tmpBufInt = this.tmpBuf.asIntBuffer();
/*  63 */     this.linesPerRow = this.size / 512;
/*     */   }
/*     */ 
/*     */   
/*     */   private void readHeader(ByteBuffer header) throws IOException {
/*  68 */     long magicNumber = header.getLong();
/*  69 */     if (magicNumber != 5136955264682433437L)
/*  70 */       throw new IOException("Bad magic number! This is not a valid map file."); 
/*  71 */     byte headerVersionNumber = header.get();
/*     */     
/*  73 */     if (headerVersionNumber == 0) {
/*     */       
/*  75 */       this.sizeLevel = header.get();
/*  76 */       this.size = 1 << this.sizeLevel;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeHeader(ByteBuffer header) throws IOException {
/*  82 */     header.putLong(5136955264682433437L);
/*  83 */     header.put((byte)0);
/*  84 */     header.put((byte)this.sizeLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public static MeshIO createMap(String filename, int level, int[] data) throws IOException {
/*  89 */     MeshIO meshIO = new MeshIO(level, data);
/*  90 */     FileChannel channel = (new RandomAccessFile(filename, "rw")).getChannel();
/*  91 */     MappedByteBuffer header = channel.map(FileChannel.MapMode.READ_WRITE, 0L, 1024L);
/*  92 */     meshIO.writeHeader(header);
/*     */     
/*  94 */     ByteBuffer stripBuffer = ByteBuffer.allocate(meshIO.size * 4);
/*     */     
/*  96 */     for (int i = 0; i < meshIO.size; i++) {
/*     */       
/*  98 */       stripBuffer.clear();
/*  99 */       stripBuffer.asIntBuffer().put(meshIO.data, meshIO.size * i, meshIO.size);
/* 100 */       stripBuffer.flip();
/* 101 */       stripBuffer.limit(meshIO.size * 4);
/* 102 */       stripBuffer.position(0);
/* 103 */       channel.write(stripBuffer, (1024 + meshIO.size * 4 * i));
/*     */     } 
/*     */     
/* 106 */     meshIO.fileChannel = channel;
/* 107 */     return meshIO;
/*     */   }
/*     */ 
/*     */   
/*     */   public static MeshIO open(String filename) throws IOException {
/* 112 */     FileChannel channel = (new RandomAccessFile(filename, "rw")).getChannel();
/* 113 */     MappedByteBuffer header = channel.map(FileChannel.MapMode.READ_ONLY, 0L, 1024L);
/* 114 */     MeshIO meshIO = new MeshIO(header);
/*     */     
/* 116 */     ByteBuffer stripBuffer = ByteBuffer.allocate(meshIO.size * 4);
/*     */     
/* 118 */     for (int i = 0; i < meshIO.size; i++) {
/*     */       
/* 120 */       stripBuffer.clear();
/* 121 */       stripBuffer.limit(meshIO.size * 4);
/* 122 */       stripBuffer.position(0);
/* 123 */       channel.read(stripBuffer, (1024 + meshIO.size * 4 * i));
/* 124 */       stripBuffer.flip();
/* 125 */       stripBuffer.asIntBuffer().get(meshIO.data, meshIO.size * i, meshIO.size);
/*     */     } 
/*     */     
/* 128 */     meshIO.fileChannel = channel;
/* 129 */     return meshIO;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 134 */     saveAllDirtyRows();
/* 135 */     this.fileChannel.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSize() {
/* 144 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getTile(int x, int y) {
/* 149 */     return this.data[x | y << this.sizeLevel];
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setTile(int x, int y, int value) {
/* 154 */     this.data[x | y << this.sizeLevel] = value;
/* 155 */     this.rowDirty[y / this.linesPerRow] = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final void saveTile(int x, int y) throws IOException {
/* 164 */     this.tmpBuf.clear();
/* 165 */     this.tmpBuf.putInt(this.data[x | y << this.sizeLevel]);
/* 166 */     this.tmpBuf.flip();
/* 167 */     this.fileChannel.write(this.tmpBuf, (1024 + ((x | y << this.sizeLevel) << 2)));
/*     */   }
/*     */ 
/*     */   
/*     */   public final void saveFullRows(int y, int rows) throws IOException {
/* 172 */     this.fileChannel.position((1024 + (y << this.sizeLevel << 2)));
/*     */     
/* 174 */     for (int yy = 0; yy < rows; yy++) {
/*     */       
/* 176 */       this.tmpBuf.clear();
/* 177 */       this.tmpBufInt.clear();
/* 178 */       this.tmpBufInt.put(this.data, y + yy << this.sizeLevel, this.size);
/* 179 */       this.tmpBufInt.flip();
/* 180 */       this.tmpBuf.limit(this.size << 2);
/* 181 */       this.tmpBuf.position(0);
/* 182 */       this.fileChannel.write(this.tmpBuf);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void saveAll() throws IOException {
/* 188 */     saveFullRows(0, this.size);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void saveAllDirtyRows() throws IOException {
/* 193 */     for (int i = 0; i < 512; i++) {
/* 194 */       saveNextDirtyRow();
/*     */     }
/*     */   }
/*     */   
/*     */   public final void saveNextDirtyRow() throws IOException {
/* 199 */     if (this.rowDirty[this.rowId]) {
/*     */       
/* 201 */       saveFullRows(this.rowId * this.linesPerRow, this.linesPerRow);
/* 202 */       this.rowDirty[this.rowId] = false;
/*     */     } 
/*     */     
/* 205 */     this.rowId++;
/* 206 */     if (this.rowId >= 512) {
/* 207 */       this.rowId = 0;
/*     */     }
/*     */   }
/*     */   
/*     */   public int[] cloneData() {
/* 212 */     int[] data2 = new int[this.data.length];
/* 213 */     System.arraycopy(this.data, 0, data2, 0, this.data.length);
/* 214 */     return data2;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\javaone\MeshIO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */