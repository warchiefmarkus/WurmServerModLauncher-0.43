/*     */ package com.wurmonline.server.zones;
/*     */ 
/*     */ import com.wurmonline.mesh.MeshIO;
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Constants;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.ServerDirInfo;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import java.io.IOException;
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ 
/*     */ 
/*     */ public final class Water
/*     */   extends Thread
/*     */   implements MiscConstants
/*     */ {
/*     */   public boolean shouldStop = false;
/*     */   private static final int ROW_COUNT = 64;
/*  46 */   private static final Logger logger = Logger.getLogger(Water.class.getName());
/*     */ 
/*     */   
/*     */   private static MeshIO waterMesh;
/*     */ 
/*     */   
/*  52 */   static final int[][] heightsArr = new int[1 << Constants.meshSize][1 << Constants.meshSize];
/*  53 */   static final float[][] addedWaterArr = new float[1 << Constants.meshSize][1 << Constants.meshSize];
/*  54 */   static final float[][] currentWaterArr = new float[1 << Constants.meshSize][1 << Constants.meshSize];
/*  55 */   static final float[][] reservoirArr = new float[1 << Constants.meshSize][1 << Constants.meshSize];
/*     */   private static final int INIT = 0;
/*     */   private static final int FLOW = 1;
/*     */   private static final int ADD_WATER = 2;
/*     */   private static final int EVAPORATE = 3;
/*     */   private static final int UPDATE = 4;
/*  61 */   private static int phase = 0;
/*     */   
/*  63 */   private static final long SPRINGRAND = Servers.localServer.id + 127312634L;
/*  64 */   private final Random waterRand = new Random();
/*  65 */   private final ConcurrentHashMap<Long, WaterGenerator> springs = new ConcurrentHashMap<>();
/*     */   
/*     */   public static final int MAX_WATERLEVEL_DECI = 10;
/*     */   public static final int MAX_WATERLEVEL_CM = 100;
/*  69 */   private final ConcurrentHashMap<Long, WaterGenerator> changedTileCorners = new ConcurrentHashMap<>();
/*  70 */   private int rowsPerRun = Math.max(1, Zones.worldTileSizeY / 64);
/*     */   
/*  72 */   private final int sleepTime = 15;
/*     */   
/*  74 */   private float nowRain = 0.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Water() {
/*  81 */     super("Water-Thread");
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void loadWaterMesh() {
/*  86 */     long start = System.nanoTime();
/*     */     
/*     */     try {
/*  89 */       waterMesh = MeshIO.open(ServerDirInfo.getFileDBPath() + "water.map");
/*     */     }
/*  91 */     catch (IOException iex) {
/*     */       
/*  93 */       logger.log(Level.INFO, "water mesh doesn't exist.. creating..");
/*  94 */       int[] waterArr = new int[(1 << Constants.meshSize) * (1 << Constants.meshSize)];
/*     */       
/*  96 */       for (int x = 0; x < (1 << Constants.meshSize) * (1 << Constants.meshSize); x++)
/*     */       {
/*  98 */         waterArr[x] = 0;
/*     */       }
/*     */       
/*     */       try {
/* 102 */         waterMesh = MeshIO.createMap(ServerDirInfo.getFileDBPath() + "water.map", Constants.meshSize, waterArr);
/*     */       }
/* 104 */       catch (IOException iox) {
/*     */         
/* 106 */         logger.log(Level.SEVERE, "Failed to create water mesh. Exiting.", iox);
/* 107 */         System.exit(0);
/*     */       }
/*     */     
/*     */     } finally {
/*     */       
/* 112 */       float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 113 */       logger.info("Loading water mesh, size: " + waterMesh.getSize() + " took " + lElapsedTime + " ms");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void loadSprings() {
/* 119 */     int numSprings = (1 << Constants.meshSize) / 64;
/* 120 */     logger.log(Level.INFO, "NUMBER OF SPRINGS=" + numSprings);
/* 121 */     Random wrand = new Random(SPRINGRAND);
/* 122 */     for (int x = 0; x < numSprings; x++) {
/*     */       
/* 124 */       int tx = wrand.nextInt(Zones.worldTileSizeX);
/* 125 */       int ty = wrand.nextInt(Zones.worldTileSizeY);
/* 126 */       if (isAboveWater(tx, ty)) {
/*     */ 
/*     */         
/* 129 */         int th = 25 + wrand.nextInt(25);
/* 130 */         WaterGenerator wg = new WaterGenerator(tx, ty, true, 0, th);
/* 131 */         this.springs.put(Long.valueOf(wg.getTileId()), wg);
/* 132 */         logger.log(Level.INFO, "Spring at " + tx + "," + ty + " =" + th);
/*     */       } 
/*     */     } 
/* 135 */     logger.log(Level.INFO, "NUMBER OF ACTIVE SPRINGS=" + this.springs.size());
/*     */     
/* 137 */     for (WaterGenerator generator : this.springs.values()) {
/* 138 */       generator.updateItem();
/*     */     }
/*     */   }
/*     */   
/*     */   public static int getCaveWater(int tilex, int tiley) {
/* 143 */     int value = waterMesh.getTile(tilex, tiley);
/* 144 */     int toReturn = value >> 16 & 0xFFFF;
/* 145 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setCaveWater(int tilex, int tiley, int newValue) {
/* 150 */     int value = waterMesh.getTile(tilex, tiley);
/* 151 */     if ((value >> 16 & 0xFFFF) != newValue) {
/* 152 */       waterMesh.setTile(tilex, tiley, ((Math.max(0, newValue) & 0xFFFF) << 16) + (value & 0xFFFF));
/*     */     }
/*     */   }
/*     */   
/*     */   public static int getSurfaceWater(int tilex, int tiley) {
/* 157 */     int value = waterMesh.getTile(tilex, tiley);
/* 158 */     int toReturn = value & 0xFFFF;
/* 159 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setSurfaceWater(int tilex, int tiley, int newValue) {
/* 164 */     int value = waterMesh.getTile(tilex, tiley);
/* 165 */     if ((value & 0xFFFF) != newValue) {
/* 166 */       waterMesh.setTile(tilex, tiley, (value & 0xFFFF0000) + (
/* 167 */           Math.max(0, newValue) & 0xFFFF));
/*     */     }
/*     */   }
/*     */   
/*     */   private static float getWaterLevel(int tilex, int tiley) {
/* 172 */     return currentWaterArr[tilex][tiley];
/*     */   }
/*     */ 
/*     */   
/*     */   private static void setWaterLevel(int tilex, int tiley, float newValue) {
/* 177 */     currentWaterArr[tilex][tiley] = newValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static float getAddedWater(int tilex, int tiley) {
/* 183 */     return addedWaterArr[tilex][tiley];
/*     */   }
/*     */ 
/*     */   
/*     */   private static void clrAddedWater(int tilex, int tiley) {
/* 188 */     addedWaterArr[tilex][tiley] = 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void incAddedWater(int tilex, int tiley, float newValue) {
/* 193 */     addedWaterArr[tilex][tiley] = addedWaterArr[tilex][tiley] + newValue;
/*     */   }
/*     */ 
/*     */   
/*     */   private static float getReservoir(int tilex, int tiley) {
/* 198 */     return reservoirArr[tilex][tiley];
/*     */   }
/*     */ 
/*     */   
/*     */   private static void setReservoir(int tilex, int tiley, float newValue) {
/* 203 */     reservoirArr[tilex][tiley] = newValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getHeightCm(int tilex, int tiley) {
/* 209 */     return heightsArr[tilex][tiley];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void setHeightDm(int tilex, int tiley, int newValue) {
/* 215 */     heightsArr[tilex][tiley] = newValue * 10;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isAboveWater(int x, int y) {
/* 220 */     return (Tiles.decodeHeight(Server.surfaceMesh.getTile(x, y)) > 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 227 */     logger.log(Level.INFO, "WATER ROWS PER RUN=" + this.rowsPerRun + " SLEEPTIME=" + '\017' + " SHOULD STOP=" + this.shouldStop);
/*     */     
/* 229 */     for (int y = 0; y < 1 << Constants.meshSize; y++) {
/*     */       
/* 231 */       for (int x = 0; x < 1 << Constants.meshSize; x++)
/*     */       {
/* 233 */         setWaterLevel(x, y, (getSurfaceWater(x, y) * 10));
/*     */       }
/*     */     } 
/* 236 */     int currY = 0;
/* 237 */     float maxRain = 0.0F;
/* 238 */     while (!this.shouldStop) {
/*     */       
/* 240 */       int xTiles = Zones.worldTileSizeX;
/*     */       
/*     */       try {
/* 243 */         sleep(15L);
/* 244 */         float rain = Server.getWeather().getRain();
/* 245 */         float evaporation = Server.getWeather().getEvaporationRate();
/* 246 */         for (int i = 0; i < this.rowsPerRun; i++) {
/*     */           
/* 248 */           for (int x = 0; x < xTiles; x++) {
/*     */             
/* 250 */             if (isAboveWater(x, currY))
/*     */             {
/*     */               
/* 253 */               if (x > 0 && x < Zones.worldTileSizeX && currY > 0 && currY < Zones.worldTileSizeY) {
/*     */                 int encodedTile; float newWaterLevel; int waterLevel;
/*     */                 WaterGenerator wg;
/* 256 */                 switch (phase) {
/*     */                   
/*     */                   case 0:
/* 259 */                     encodedTile = Server.surfaceMesh.getTile(x, currY);
/* 260 */                     setHeightDm(x, currY, Tiles.decodeHeight(encodedTile));
/* 261 */                     clrAddedWater(x, currY);
/*     */                     break;
/*     */                   case 1:
/* 264 */                     doFlow(x, currY);
/*     */                     break;
/*     */                   case 2:
/* 267 */                     addWater(x, currY, rain);
/*     */                     break;
/*     */                   case 3:
/* 270 */                     doEvaporation(x, currY, evaporation);
/*     */                     break;
/*     */ 
/*     */                   
/*     */                   case 4:
/* 275 */                     newWaterLevel = getWaterLevel(x, currY);
/*     */                     
/* 277 */                     waterLevel = (int)(newWaterLevel / 10.0F);
/* 278 */                     setSurfaceWater(x, currY, waterLevel);
/* 279 */                     wg = WaterGenerator.getWG(x, currY);
/* 280 */                     if (wg != null) {
/* 281 */                       wg.setHeight(waterLevel);
/*     */                     } else {
/* 283 */                       wg = new WaterGenerator(x, currY, 0, waterLevel);
/* 284 */                     }  addWater(wg);
/*     */                     break;
/*     */                 } 
/*     */               } 
/*     */             }
/*     */           } 
/* 290 */           currY++;
/* 291 */           if (currY >= Zones.worldTileSizeY)
/*     */           {
/* 293 */             currY = 0;
/*     */           }
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 299 */         if (currY == 0) {
/*     */ 
/*     */           
/* 302 */           phase++;
/* 303 */           if (phase > 4) {
/* 304 */             phase = 0;
/*     */           }
/*     */         } 
/* 307 */       } catch (InterruptedException interruptedException) {}
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 312 */         waterMesh.saveNextDirtyRow();
/*     */       }
/* 314 */       catch (IOException ex) {
/*     */         
/* 316 */         this.shouldStop = true;
/*     */       } 
/* 318 */       if ((int)(this.nowRain * 100.0F) > (int)(maxRain * 100.0F))
/*     */       {
/* 320 */         maxRain = this.nowRain;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 325 */     logger.info("Water mesh thread has finished");
/*     */     
/*     */     try {
/* 328 */       waterMesh.saveAll();
/* 329 */       waterMesh.close();
/*     */     }
/* 331 */     catch (IOException iox) {
/*     */       
/* 333 */       logger.log(Level.WARNING, "Failed to save watermesh!", iox);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private final void doFlow(int x, int y) {
/* 339 */     float[][] moveArr = new float[3][3];
/* 340 */     for (int xx = -1; xx <= 1; xx++) {
/* 341 */       for (int yy = -1; yy <= 1; yy++)
/* 342 */         moveArr[xx + 1][yy + 1] = 0.0F; 
/*     */     } 
/* 344 */     float amountToFlow = 1.0F;
/* 345 */     int lowerCount = 1;
/* 346 */     while (lowerCount > 0) {
/*     */       
/* 348 */       lowerCount = 0;
/* 349 */       float lowDiffs = 0.0F;
/* 350 */       float nwl = getWaterLevel(x, y) + moveArr[1][1];
/* 351 */       float wht = getHeightCm(x, y) + nwl;
/* 352 */       boolean ok = (nwl > 0.1F);
/* 353 */       if (ok)
/*     */       {
/*     */         
/* 356 */         for (int i = -1; i <= 1; i++) {
/* 357 */           for (int yy = -1; yy <= 1; yy++) {
/* 358 */             if (i != 0 || yy != 0) {
/*     */               
/* 360 */               float cht = getHeightCm(x + i, y + yy) + getWaterLevel(x + i, y + yy) + moveArr[i + 1][yy + 1];
/*     */               
/* 362 */               float diff = wht - cht;
/* 363 */               if (diff > 0.0F) {
/*     */                 
/* 365 */                 lowerCount++;
/* 366 */                 lowDiffs += diff;
/*     */               } 
/*     */             } 
/*     */           } 
/* 370 */         }  }  float toMovePerCm = 0.0F;
/*     */       
/* 372 */       if (lowerCount > 0 && lowDiffs > 0.0F) {
/*     */         
/* 374 */         toMovePerCm = amountToFlow / lowDiffs;
/* 375 */         for (int i = -1; i <= 1; i++) {
/* 376 */           for (int yy = -1; yy <= 1; yy++) {
/* 377 */             if (i != 0 || yy != 0) {
/*     */               
/* 379 */               float cht = getHeightCm(x + i, y + yy) + getWaterLevel(x + i, y + yy) + moveArr[i + 1][yy + 1];
/*     */               
/* 381 */               float diff = wht - cht;
/* 382 */               if (ok && diff > 0.0F) {
/*     */ 
/*     */                 
/* 385 */                 float toMove = toMovePerCm * diff;
/* 386 */                 moveArr[i + 1][yy + 1] = moveArr[i + 1][yy + 1] + toMove;
/* 387 */                 moveArr[1][1] = moveArr[1][1] - toMove;
/* 388 */                 incAddedWater(x + i, y + yy, toMove);
/* 389 */                 incAddedWater(x, y, -toMove);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void addWater(int x, int y, float rain) {
/* 402 */     if (rain > 0.0F) {
/*     */       
/* 404 */       if (rain > this.nowRain)
/*     */       {
/* 406 */         this.nowRain = rain;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 412 */       incAddedWater(x, y, rain);
/*     */     } 
/* 414 */     WaterGenerator sp = this.springs.get(Long.valueOf(Tiles.getTileId(x, y, 0, true)));
/* 415 */     if (sp != null)
/*     */     {
/*     */       
/* 418 */       incAddedWater(x, y, (sp.getHeight() + this.waterRand.nextInt(25)));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float getMaxWaterLeakage(byte code) {
/* 429 */     switch (code) {
/*     */ 
/*     */       
/*     */       case 0:
/* 433 */         return 0.1F;
/*     */       
/*     */       case 1:
/* 436 */         return 0.2F;
/*     */       
/*     */       case 2:
/* 439 */         return 0.5F;
/*     */     } 
/* 441 */     return 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float getMaxWaterInfiltration(byte code) {
/* 451 */     switch (code) {
/*     */ 
/*     */       
/*     */       case 0:
/* 455 */         return 0.0F;
/*     */       
/*     */       case 1:
/* 458 */         return 0.25F;
/*     */       
/*     */       case 2:
/* 461 */         return 0.5F;
/*     */       
/*     */       case 3:
/* 464 */         return 1.0F;
/*     */     } 
/* 466 */     return 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float getMaxWaterReservoir(byte code) {
/* 476 */     switch (code) {
/*     */ 
/*     */       
/*     */       case 0:
/* 480 */         return 3.0F;
/*     */       
/*     */       case 1:
/* 483 */         return 7.0F;
/*     */       
/*     */       case 2:
/* 486 */         return 15.0F;
/*     */     } 
/* 488 */     return 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void doEvaporation(int x, int y, float evaporation) {
/* 494 */     float newWaterLevel = Math.max(0.0F, getWaterLevel(x, y) + getAddedWater(x, y));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 499 */     int encodedTile = Server.surfaceMesh.getTile(x, y);
/* 500 */     byte type = Tiles.decodeType(encodedTile);
/* 501 */     Tiles.Tile tile = Tiles.getTile(type);
/* 502 */     float maxWaterLeakage = getMaxWaterLeakage(tile.getWaterLeakageCode());
/* 503 */     float maxWaterInfiltration = getMaxWaterInfiltration(tile.getWaterInfiltrationCode());
/* 504 */     float maxWaterReservoir = getMaxWaterReservoir(tile.getWaterReservoirCode());
/*     */     
/* 506 */     float currentReservoir = getReservoir(x, y);
/* 507 */     if (maxWaterLeakage > 0.0F && currentReservoir > 0.0F)
/*     */     {
/* 509 */       currentReservoir = Math.max(0.0F, currentReservoir - maxWaterLeakage);
/*     */     }
/*     */     
/* 512 */     float tempNewWaterLevel = getWaterLevel(x, y) + getAddedWater(x, y);
/* 513 */     if (maxWaterInfiltration > 0.0F && tempNewWaterLevel > 0.0F) {
/*     */ 
/*     */       
/* 516 */       float room = maxWaterReservoir - currentReservoir;
/* 517 */       float toMove = Math.min(Math.min(room, maxWaterInfiltration), tempNewWaterLevel);
/* 518 */       currentReservoir += toMove;
/* 519 */       incAddedWater(x, y, -toMove);
/*     */     } 
/* 521 */     setReservoir(x, y, currentReservoir);
/* 522 */     setWaterLevel(x, y, newWaterLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void addWater(WaterGenerator addedChange) {
/* 527 */     if (addedChange.changed())
/*     */     {
/*     */ 
/*     */       
/* 531 */       this.changedTileCorners.put(Long.valueOf(addedChange.getTileId()), addedChange);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void propagateChanges() {
/* 537 */     for (WaterGenerator generator : this.changedTileCorners.values()) {
/*     */       
/* 539 */       if (generator.changedSinceReset())
/*     */       {
/* 541 */         for (Player player : Players.getInstance().getPlayers()) {
/*     */           
/* 543 */           if (player.isWithinTileDistanceTo(generator.x, generator.y, 0, 20))
/*     */           {
/* 545 */             player.getCommunicator().sendWater(generator.x, generator.y, generator.layer, generator.getHeight());
/*     */           }
/*     */         } 
/*     */       }
/*     */       
/* 550 */       generator.setReset(true);
/*     */     } 
/* 552 */     this.changedTileCorners.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void tickGenerators() {
/* 559 */     for (WaterGenerator spring : this.springs.values()) {
/*     */ 
/*     */       
/* 562 */       int oldSurfaceVal = getSurfaceWater(spring.x, spring.y);
/*     */       
/* 564 */       if (oldSurfaceVal < 200) {
/*     */         
/* 566 */         this.waterRand.setSeed((spring.x + spring.y));
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 571 */         setSurfaceWater(spring.x, spring.y, oldSurfaceVal + 1 + this.waterRand.nextInt(3));
/* 572 */         spring.setHeight(getSurfaceWater(spring.x, spring.y));
/*     */ 
/*     */         
/* 575 */         addWater(spring);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\Water.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */