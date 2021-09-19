/*     */ package com.wurmonline.mesh;
/*     */ 
/*     */ import com.wurmonline.math.TilePos;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.MappedByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public final class MeshIO
/*     */ {
/*  35 */   private static final Logger logger = Logger.getLogger(MeshIO.class.getName());
/*     */   
/*     */   private static final long MAGIC_NUMBER = 5136955264682433437L;
/*     */   private static final int ROW_COUNT = 128;
/*     */   private int size_level;
/*     */   private int size;
/*  41 */   private short maxHeight = 0;
/*  42 */   private short[] maxHeightCoord = new short[] { -1, -1 };
/*     */   public final int[] data;
/*     */   private FileChannel fileChannel;
/*     */   private ByteBuffer tmpBuf;
/*     */   private final IntBuffer tmpBufInt;
/*  47 */   private boolean[] rowDirty = new boolean[128];
/*  48 */   private int rowId = 0;
/*     */ 
/*     */   
/*     */   private final int linesPerRow;
/*     */ 
/*     */   
/*     */   private byte[] distantTerrainTypes;
/*     */ 
/*     */   
/*     */   private static boolean allocateDirectBuffers;
/*     */ 
/*     */   
/*     */   private MeshIO(int size_level, int[] data) {
/*  61 */     if (size_level > 32) {
/*  62 */       throw new IllegalArgumentException("I'm fairly sure you didn't mean to REALLY create a map 2^" + size_level + " units wide.");
/*     */     }
/*  64 */     if (size_level < 4)
/*  65 */       throw new IllegalArgumentException("Maps can't be smaller than 2^4."); 
/*  66 */     this.size_level = size_level;
/*  67 */     this.size = 1 << size_level;
/*  68 */     this.data = data;
/*  69 */     int holes = 0;
/*  70 */     for (int x = 0; x < data.length; x++) {
/*     */       
/*  72 */       if (Tiles.decodeType(data[x]) == Tiles.Tile.TILE_HOLE.getId())
/*  73 */         holes++; 
/*     */     } 
/*  75 */     logger.info("Holes=" + holes);
/*  76 */     if (allocateDirectBuffers) {
/*     */       
/*  78 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/*  80 */         logger.fine("Will allocate a direct byte buffer for writing the Mesh.");
/*     */       }
/*  82 */       this.tmpBuf = ByteBuffer.allocateDirect(this.size * 4);
/*     */     }
/*     */     else {
/*     */       
/*  86 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/*  88 */         logger.fine("Will allocate a heap byte buffer for writing the Mesh.");
/*     */       }
/*  90 */       this.tmpBuf = ByteBuffer.allocate(this.size * 4);
/*     */     } 
/*  92 */     this.tmpBufInt = this.tmpBuf.asIntBuffer();
/*  93 */     this.linesPerRow = this.size / 128;
/*  94 */     logger.info("size_level: " + size_level);
/*  95 */     logger.info("size: " + this.size);
/*  96 */     logger.info("data length: " + data.length);
/*  97 */     logger.info("linesPerRow: " + this.linesPerRow);
/*     */   }
/*     */ 
/*     */   
/*     */   private MeshIO(ByteBuffer header) throws IOException {
/* 102 */     readHeader(header);
/* 103 */     this.data = new int[this.size * this.size + 1];
/* 104 */     if (allocateDirectBuffers) {
/*     */       
/* 106 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/* 108 */         logger.fine("Will allocate a direct byte buffer for writing the Mesh.");
/*     */       }
/* 110 */       this.tmpBuf = ByteBuffer.allocateDirect(this.size * 4);
/*     */     }
/*     */     else {
/*     */       
/* 114 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/* 116 */         logger.fine("Will allocate a heap byte buffer for writing the Mesh.");
/*     */       }
/* 118 */       this.tmpBuf = ByteBuffer.allocate(this.size * 4);
/*     */     } 
/* 120 */     this.tmpBufInt = this.tmpBuf.asIntBuffer();
/* 121 */     this.linesPerRow = this.size / 128;
/*     */   }
/*     */ 
/*     */   
/*     */   private void readHeader(ByteBuffer header) throws IOException {
/* 126 */     long magicNumber = header.getLong();
/* 127 */     if (logger.isLoggable(Level.FINE))
/*     */     {
/* 129 */       logger.fine("Magic Number: " + magicNumber);
/*     */     }
/* 131 */     if (magicNumber != 5136955264682433437L) {
/* 132 */       throw new IOException("Bad magic number! This is not a valid map file.");
/*     */     }
/* 134 */     byte headerVersionNumber = header.get();
/* 135 */     if (logger.isLoggable(Level.FINE))
/*     */     {
/* 137 */       logger.fine("Version Number: " + headerVersionNumber);
/*     */     }
/*     */     
/* 140 */     if (headerVersionNumber == 0) {
/*     */       
/* 142 */       this.size_level = header.get();
/* 143 */       this.size = 1 << this.size_level;
/*     */     } 
/* 145 */     if (logger.isLoggable(Level.FINE)) {
/*     */       
/* 147 */       logger.fine("size level: " + this.size_level);
/* 148 */       logger.fine("size: " + this.size);
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
/*     */   private void writeHeader(ByteBuffer header) throws IOException {
/* 160 */     header.putLong(5136955264682433437L);
/* 161 */     header.put((byte)0);
/* 162 */     header.put((byte)this.size_level);
/*     */   }
/*     */   
/*     */   public static MeshIO createMap(String filename, int level, int[] data) throws IOException {
/*     */     ByteBuffer stripBuffer;
/* 167 */     MeshIO meshIO = new MeshIO(level, data);
/*     */ 
/*     */     
/* 170 */     FileChannel channel = (new RandomAccessFile(filename, "rw")).getChannel();
/* 171 */     logger.info(filename + " size is " + channel.size());
/* 172 */     logger.info("Data array length is " + data.length);
/* 173 */     MappedByteBuffer header = channel.map(FileChannel.MapMode.READ_WRITE, 0L, 1024L);
/* 174 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/* 176 */       logger.finer("Header capacity: " + header.capacity() + ", header.limit: " + header.limit() + ", header.position: " + header
/* 177 */           .position());
/*     */     }
/* 179 */     meshIO.writeHeader(header);
/* 180 */     logger.info("meshIO size is " + meshIO.size);
/*     */ 
/*     */     
/* 183 */     if (allocateDirectBuffers) {
/*     */       
/* 185 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/* 187 */         logger.fine("Will allocate a direct byte buffer for creating the map: " + filename);
/*     */       }
/* 189 */       stripBuffer = ByteBuffer.allocateDirect(meshIO.size * 4);
/*     */     }
/*     */     else {
/*     */       
/* 193 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/* 195 */         logger.fine("Will allocate a heap byte buffer for creating the map: " + filename);
/*     */       }
/* 197 */       stripBuffer = ByteBuffer.allocate(meshIO.size * 4);
/*     */     } 
/*     */     
/* 200 */     for (int i = 0; i < meshIO.size; i++) {
/*     */       
/* 202 */       stripBuffer.clear();
/* 203 */       stripBuffer.asIntBuffer().put(meshIO.data, meshIO.size * i, meshIO.size);
/*     */       
/* 205 */       stripBuffer.flip();
/* 206 */       stripBuffer.limit(meshIO.size * 4);
/* 207 */       stripBuffer.position(0);
/*     */ 
/*     */       
/* 210 */       channel.write(stripBuffer, (1024 + meshIO.size * 4 * i));
/*     */     } 
/*     */     
/* 213 */     meshIO.fileChannel = channel;
/* 214 */     return meshIO;
/*     */   }
/*     */   
/*     */   public static MeshIO open(String filename) throws IOException {
/*     */     ByteBuffer stripBuffer;
/* 219 */     long lStart = System.nanoTime();
/*     */ 
/*     */     
/* 222 */     FileChannel channel = (new RandomAccessFile(filename, "rw")).getChannel();
/* 223 */     logger.info(filename + " size is " + channel.size());
/* 224 */     MappedByteBuffer header = channel.map(FileChannel.MapMode.READ_ONLY, 0L, 1024L);
/* 225 */     if (logger.isLoggable(Level.FINE))
/*     */     {
/* 227 */       logger.fine("Header capacity: " + header.capacity() + ", header.limit: " + header.limit() + ", header.position: " + header
/* 228 */           .position());
/*     */     }
/* 230 */     MeshIO meshIO = new MeshIO(header);
/*     */ 
/*     */     
/* 233 */     if (allocateDirectBuffers) {
/*     */       
/* 235 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/* 237 */         logger.fine("Will allocate a direct byte buffer for reading the mesh: " + filename);
/*     */       }
/* 239 */       stripBuffer = ByteBuffer.allocateDirect(meshIO.size * 4);
/*     */     }
/*     */     else {
/*     */       
/* 243 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/* 245 */         logger.fine("Will allocate a heap byte buffer for reading the mesh: " + filename);
/*     */       }
/* 247 */       stripBuffer = ByteBuffer.allocate(meshIO.size * 4);
/*     */     } 
/*     */     
/* 250 */     for (int i = 0; i < meshIO.size; i++) {
/*     */       
/* 252 */       stripBuffer.clear();
/* 253 */       stripBuffer.limit(meshIO.size * 4);
/* 254 */       stripBuffer.position(0);
/*     */       
/* 256 */       channel.read(stripBuffer, (1024 + meshIO.size * 4 * i));
/* 257 */       stripBuffer.flip();
/* 258 */       stripBuffer.asIntBuffer().get(meshIO.data, meshIO.size * i, meshIO.size);
/* 259 */       stripBuffer.rewind();
/*     */       
/* 261 */       if (filename.contains("top_layer")) {
/*     */         
/* 263 */         IntBuffer tmp = stripBuffer.asIntBuffer();
/* 264 */         for (int x = 0; x < meshIO.size; x++) {
/*     */           
/* 266 */           int tile = tmp.get(x);
/* 267 */           short height = Tiles.decodeHeight(tile);
/* 268 */           if (height > meshIO.getMaxHeight()) {
/*     */             
/* 270 */             meshIO.setMaxHeight(height);
/* 271 */             meshIO.setMaxHeightCoord((short)x, (short)i);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 277 */     meshIO.fileChannel = channel;
/*     */     
/* 279 */     if (logger.isLoggable(Level.FINE)) {
/*     */       
/* 281 */       long lElapsedTime = System.nanoTime() - lStart;
/* 282 */       logger.fine("Loaded Mesh '" + filename + "', that took " + ((float)lElapsedTime / 1000000.0F) + ", millis.");
/*     */     } 
/* 284 */     return meshIO;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 289 */     saveAllDirtyRows();
/* 290 */     this.fileChannel.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSize() {
/* 295 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSizeLevel() {
/* 300 */     return this.size_level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getTile(TilePos tilePos) {
/* 310 */     return getTile(tilePos.x, tilePos.y);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getTile(int x, int y) {
/* 315 */     int tile = 0;
/*     */     
/*     */     try {
/* 318 */       tile = this.data[x | y << this.size_level];
/*     */     }
/* 320 */     catch (ArrayIndexOutOfBoundsException e) {
/*     */       
/* 322 */       e.printStackTrace();
/*     */ 
/*     */ 
/*     */       
/* 326 */       int xx = x;
/* 327 */       int yy = y;
/* 328 */       if (xx < 0) xx = 0; 
/* 329 */       if (yy < 0) yy = 0; 
/* 330 */       int idx = xx | yy << this.size_level;
/* 331 */       if (idx < 0 || idx > this.data.length) idx = this.data.length - 1; 
/* 332 */       logger.log(Level.WARNING, "data: " + this.data.length + ", x: " + x + ", y: " + y + ", size_level: " + this.size_level + ", x | (y << size_level): " + (x | y << this.size_level), e);
/*     */       
/* 334 */       logger.log(Level.WARNING, "Attempting to find closest tile using: x: " + xx + ", y: " + yy + " for an index of: " + idx);
/* 335 */       return this.data[idx];
/*     */     } 
/* 337 */     return tile;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setTile(int x, int y, int value) {
/* 342 */     this.data[x | y << this.size_level] = value;
/* 343 */     this.rowDirty[y / this.linesPerRow] = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final void saveTile(int x, int y) throws IOException {
/* 352 */     this.tmpBuf.clear();
/* 353 */     this.tmpBuf.putInt(this.data[x | y << this.size_level]);
/* 354 */     this.tmpBuf.flip();
/* 355 */     this.fileChannel.write(this.tmpBuf, (1024 + ((x | y << this.size_level) << 2)));
/*     */   }
/*     */ 
/*     */   
/*     */   public final void saveFullRows(int y, int rows) throws IOException {
/* 360 */     this.fileChannel.position((1024 + (y << this.size_level << 2)));
/*     */     
/* 362 */     for (int yy = 0; yy < rows; yy++) {
/*     */       
/* 364 */       this.tmpBuf.clear();
/* 365 */       this.tmpBufInt.clear();
/* 366 */       this.tmpBufInt.put(this.data, y + yy << this.size_level, this.size);
/* 367 */       this.tmpBufInt.flip();
/* 368 */       this.tmpBuf.limit(this.size << 2);
/* 369 */       this.tmpBuf.position(0);
/* 370 */       this.fileChannel.write(this.tmpBuf);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void saveAll() throws IOException {
/* 376 */     long lStart = System.nanoTime();
/*     */     
/* 378 */     saveFullRows(0, this.size);
/*     */     
/* 380 */     if (logger.isLoggable(Level.FINE)) {
/*     */       
/* 382 */       long lElapsedTime = System.nanoTime() - lStart;
/* 383 */       logger.fine("Saved all " + this.size + " rows that took " + ((float)lElapsedTime / 1000000.0F) + ", millis.");
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
/*     */   public final int saveAllDirtyRows() throws IOException {
/* 396 */     long lStart = System.nanoTime();
/*     */     
/* 398 */     int savedRowCount = 0;
/*     */     
/* 400 */     for (int i = 0; i < 128; i++) {
/*     */       
/* 402 */       if (saveNextDirtyRow())
/*     */       {
/* 404 */         savedRowCount++;
/*     */       }
/*     */     } 
/*     */     
/* 408 */     if (logger.isLoggable(Level.FINER)) {
/*     */       
/* 410 */       long lElapsedTime = System.nanoTime() - lStart;
/* 411 */       if (savedRowCount > 0)
/*     */       {
/* 413 */         logger.finer("Saved all " + savedRowCount + " dirty rows that took " + ((float)lElapsedTime / 1000000.0F) + ", millis.");
/*     */       }
/*     */     } 
/*     */     
/* 417 */     return savedRowCount;
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
/*     */   public final boolean saveNextDirtyRow() throws IOException {
/* 429 */     boolean lRowWasDirty = false;
/* 430 */     if (this.rowDirty[this.rowId]) {
/*     */       
/* 432 */       lRowWasDirty = true;
/* 433 */       long lStart = System.nanoTime();
/*     */       
/* 435 */       saveFullRows(this.rowId * this.linesPerRow, this.linesPerRow);
/* 436 */       this.rowDirty[this.rowId] = false;
/*     */       
/* 438 */       if (logger.isLoggable(Level.FINEST)) {
/*     */         
/* 440 */         long lElapsedTime = System.nanoTime() - lStart;
/* 441 */         logger.finest("Saved dirty row " + this.rowId + ", that took " + ((float)lElapsedTime / 1000000.0F) + ", millis.");
/*     */       } 
/*     */     } 
/*     */     
/* 445 */     this.rowId++;
/* 446 */     if (this.rowId >= 128) {
/*     */       
/* 448 */       this.rowId = 0;
/* 449 */       if (logger.isLoggable(Level.FINEST))
/*     */       {
/* 451 */         logger.finest("Resetting dirty row id as it has reached 128");
/*     */       }
/*     */     } 
/*     */     
/* 455 */     return lRowWasDirty;
/*     */   }
/*     */ 
/*     */   
/*     */   int[] cloneData() {
/* 460 */     int[] data2 = new int[this.data.length];
/* 461 */     System.arraycopy(this.data, 0, data2, 0, this.data.length);
/* 462 */     return data2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void calcDistantTerrain() {
/* 467 */     this.distantTerrainTypes = new byte[this.size * this.size / 256];
/* 468 */     for (int xT = 0; xT < this.size / 16; xT++) {
/*     */       
/* 470 */       for (int yT = 0; yT < this.size / 16; yT++) {
/*     */         
/* 472 */         int[] counts = new int[256];
/* 473 */         for (int x = xT * 16; x < xT * 16 + 16; x++) {
/*     */           
/* 475 */           for (int y = yT * 16; y < yT * 16 + 16; y++) {
/*     */             
/* 477 */             int type = Tiles.decodeType(getTile(x, y)) & 0xFF;
/* 478 */             counts[type] = counts[type] + 1;
/*     */           } 
/*     */         } 
/*     */         
/* 482 */         int mostCommon = 0;
/* 483 */         for (int i = 0; i < 256; i++) {
/*     */           
/* 485 */           if (counts[i] > counts[mostCommon])
/*     */           {
/* 487 */             mostCommon = i;
/*     */           }
/*     */         } 
/* 490 */         this.distantTerrainTypes[xT + yT * this.size / 16] = (byte)mostCommon;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getDistantTerrainTypes() {
/* 500 */     return this.distantTerrainTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getData() {
/* 508 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAllRowsDirty() {
/* 513 */     for (int yy = 0; yy < 128; yy++)
/*     */     {
/* 515 */       this.rowDirty[yy] = true;
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
/*     */   public static boolean isAllocateDirectBuffers() {
/* 528 */     return allocateDirectBuffers;
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
/*     */   public static void setAllocateDirectBuffers(boolean newAllocateDirectBuffers) {
/* 541 */     allocateDirectBuffers = newAllocateDirectBuffers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 552 */     return "MeshIO [Size: " + this.size + ", linesPerRow: " + this.linesPerRow + ", rowId: " + this.rowId + ", size_level: " + this.size_level + "]@" + 
/* 553 */       hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public short getMaxHeight() {
/* 558 */     return this.maxHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxHeight(short maxHeight) {
/* 563 */     this.maxHeight = maxHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public short[] getMaxHeightCoord() {
/* 568 */     return this.maxHeightCoord;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxHeightCoord(short x, short y) {
/* 573 */     this.maxHeightCoord[0] = x;
/* 574 */     this.maxHeightCoord[1] = y;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\mesh\MeshIO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */