/*     */ package com.wurmonline.server.zones;
/*     */ 
/*     */ import com.wurmonline.server.Constants;
/*     */ import com.wurmonline.server.MeshTile;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
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
/*     */ 
/*     */ 
/*     */ public class WaterType
/*     */   implements MiscConstants
/*     */ {
/*  36 */   private static final Logger logger = Logger.getLogger(WaterType.class.getName());
/*     */   
/*     */   public static final byte NOT_WATER = 0;
/*     */   
/*     */   public static final byte WATER = 1;
/*     */   
/*     */   public static final byte POND = 2;
/*     */   public static final byte LAKE = 3;
/*     */   public static final byte SEA = 4;
/*     */   public static final byte SEA_SHALLOWS = 5;
/*     */   public static final byte LAKE_SHALLOWS = 6;
/*     */   public static final byte SHALLOWS = 7;
/*     */   static final byte LAKE_RADIUS = 7;
/*     */   static final byte POND_RADIUS = 2;
/*     */   static final byte SHALLOWS_DEPTH = -25;
/*  51 */   static final byte[][] waterSurface = new byte[1 << Constants.meshSize][1 << Constants.meshSize];
/*  52 */   static final byte[][] waterCave = new byte[1 << Constants.meshSize][1 << Constants.meshSize];
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
/*     */   public static final void calcWaterTypes() {
/*  64 */     long start = System.nanoTime();
/*  65 */     int max = Server.surfaceMesh.getSize() - 1;
/*     */ 
/*     */     
/*  68 */     surfaceByDepth(waterSurface, (byte)0, 0, true, (byte)1);
/*     */     
/*  70 */     setBorder(waterSurface, (byte)1, (byte)2);
/*     */     
/*  72 */     fill(waterSurface, (byte)1, (byte)2);
/*     */     
/*  74 */     surfaceByDepth(waterSurface, (byte)2, -25, false, (byte)5);
/*     */     
/*  76 */     setBorder(waterSurface, (byte)2, (byte)4);
/*     */     
/*  78 */     fill(waterSurface, (byte)2, (byte)4);
/*     */ 
/*     */     
/*  81 */     setTypeByArea(waterSurface, 7, (byte)1, (byte)3);
/*     */     
/*  83 */     fill(waterSurface, (byte)1, (byte)3);
/*     */     
/*  85 */     setTypeByArea(waterSurface, 7, (byte)2, (byte)3);
/*     */     
/*  87 */     fill(waterSurface, (byte)2, (byte)3);
/*     */     
/*  89 */     surfaceByDepth(waterSurface, (byte)3, -25, false, (byte)6);
/*     */ 
/*     */     
/*  92 */     postProcessShallows(waterSurface);
/*     */ 
/*     */     
/*  95 */     setTypeByArea(waterSurface, 2, (byte)1, (byte)2);
/*     */     
/*  97 */     fill(waterSurface, (byte)1, (byte)2);
/*     */ 
/*     */     
/* 100 */     caveByDepth(waterCave, (byte)0, 0, true, (byte)1);
/*     */     
/* 102 */     setTypeByArea(waterCave, 7, (byte)1, (byte)3);
/*     */     
/* 104 */     fill(waterCave, (byte)1, (byte)3);
/*     */ 
/*     */     
/* 107 */     setTypeByArea(waterCave, 2, (byte)1, (byte)2);
/*     */     
/* 109 */     fill(waterCave, (byte)1, (byte)2);
/*     */     
/* 111 */     float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 112 */     logger.info("Calculated water types, size: " + max + " took " + lElapsedTime + " ms");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void fill(byte[][] map, byte checkType, byte setType) {
/* 123 */     int max = Server.surfaceMesh.getSize() - 1;
/* 124 */     boolean looping = true;
/* 125 */     while (looping) {
/*     */       
/* 127 */       looping = false;
/*     */       int x;
/* 129 */       for (x = 1; x < max - 1; x++) {
/*     */         
/* 131 */         for (int y = 1; y < max - 1; y++)
/*     */         {
/* 133 */           looping = fillTile(map, x, y, checkType, setType, looping);
/*     */         }
/*     */       } 
/*     */       
/* 137 */       for (x = max - 1; x > 1; x--) {
/*     */         
/* 139 */         for (int y = 1; y < max - 1; y++)
/*     */         {
/* 141 */           looping = fillTile(map, x, y, checkType, setType, looping);
/*     */         }
/*     */       } 
/*     */       
/* 145 */       for (x = 1; x < max - 1; x++) {
/*     */         
/* 147 */         for (int y = max - 1; y > 1; y--)
/*     */         {
/* 149 */           looping = fillTile(map, x, y, checkType, setType, looping);
/*     */         }
/*     */       } 
/*     */       
/* 153 */       for (x = max - 1; x > 1; x--) {
/*     */         
/* 155 */         for (int y = max - 1; y > 1; y--)
/*     */         {
/* 157 */           looping = fillTile(map, x, y, checkType, setType, looping);
/*     */         }
/*     */       } 
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
/*     */   private static final boolean fillTile(byte[][] map, int x, int y, byte checkType, byte setType, boolean looping) {
/* 175 */     if (map[x][y] == checkType)
/*     */     {
/* 177 */       if (map[x - 1][y] == setType || map[x + 1][y] == setType || map[x][y - 1] == setType || map[x][y + 1] == setType) {
/*     */         
/* 179 */         map[x][y] = setType;
/* 180 */         return true;
/*     */       } 
/*     */     }
/* 183 */     return looping;
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
/*     */   private static final void surfaceByDepth(byte[][] map, byte checkType, int depth, boolean under, byte setType) {
/* 196 */     int max = Server.surfaceMesh.getSize() - 1;
/* 197 */     for (int x = 0; x <= max; x++) {
/*     */       
/* 199 */       for (int y = 0; y <= max; y++) {
/*     */         
/* 201 */         if (map[x][y] == checkType) {
/*     */           
/* 203 */           MeshTile mt = new MeshTile(Server.surfaceMesh, x, y);
/* 204 */           if (mt.isUnder(depth) == under)
/*     */           {
/* 206 */             map[x][y] = setType;
/*     */           }
/*     */         } 
/*     */       } 
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
/*     */   private static final void caveByDepth(byte[][] map, byte checkType, int depth, boolean under, byte setType) {
/* 223 */     int max = Server.caveMesh.getSize() - 1;
/* 224 */     for (int x = 0; x <= max; x++) {
/*     */       
/* 226 */       for (int y = 0; y <= max; y++) {
/*     */         
/* 228 */         MeshTile mt = new MeshTile(Server.caveMesh, x, y);
/* 229 */         if (map[x][y] == checkType)
/*     */         {
/* 231 */           if (mt.isUnder(depth) == under)
/*     */           {
/* 233 */             map[x][y] = setType;
/*     */           }
/*     */         }
/*     */       } 
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
/*     */   private static final void setBorder(byte[][] map, byte checkType, byte setType) {
/* 248 */     int max = Server.surfaceMesh.getSize() - 1;
/* 249 */     for (int i = 0; i <= max; i++) {
/*     */ 
/*     */       
/* 252 */       if (map[i][0] == checkType) {
/* 253 */         map[i][0] = setType;
/*     */       }
/* 255 */       else if (map[i][max] == checkType) {
/* 256 */         map[i][max] = setType;
/*     */       }
/* 258 */       else if (map[0][i] == checkType) {
/* 259 */         map[0][i] = setType;
/*     */       }
/* 261 */       else if (map[max][i] == checkType) {
/* 262 */         map[max][i] = setType;
/*     */       } 
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
/*     */   private static final void setTypeByArea(byte[][] map, int radius, byte checkType, byte setType) {
/* 275 */     int max = Server.surfaceMesh.getSize() - 1;
/* 276 */     for (int x = radius; x <= max - radius; x++) {
/*     */       
/* 278 */       for (int y = radius; y <= max - radius; y++) {
/*     */         
/* 280 */         boolean ok = (map[x][y] == checkType);
/* 281 */         for (int i = 1; i < radius && ok; i++) {
/*     */           
/* 283 */           if (map[x - i][y] != checkType) {
/* 284 */             ok = false;
/* 285 */           } else if (map[x + i][y] != checkType) {
/* 286 */             ok = false;
/* 287 */           } else if (map[x][y - i] != checkType) {
/* 288 */             ok = false;
/* 289 */           } else if (map[x][y + i] != checkType) {
/* 290 */             ok = false;
/*     */           } 
/* 292 */         }  if (ok)
/*     */         {
/* 294 */           map[x][y] = setType;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void postProcessShallows(byte[][] map) {
/* 305 */     int max = Server.surfaceMesh.getSize() - 1;
/*     */     
/* 307 */     for (int x = 0; x <= max; x++) {
/*     */       
/* 309 */       for (int y = 0; y <= max; y++) {
/*     */         
/* 311 */         if (map[x][y] == 5) {
/*     */           
/* 313 */           byte closest = 5;
/*     */ 
/*     */           
/* 316 */           for (int ii = 1; ii < max && closest == 5; ii++) {
/*     */             
/* 318 */             closest = checkShallows(map, x, y, ii, closest, (byte)4);
/* 319 */             closest = checkShallows(map, x, y, ii, closest, (byte)3);
/*     */           } 
/* 321 */           if (closest == 3) {
/* 322 */             map[x][y] = 6;
/*     */           }
/*     */         } 
/*     */       } 
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
/*     */   private static final byte checkShallows(byte[][] map, int x, int y, int ii, byte closest, byte wType) {
/* 340 */     for (int jj = -ii; jj < ii && closest == 5; jj++) {
/*     */       
/* 342 */       int mapSize = Server.surfaceMesh.getSize() - 1;
/* 343 */       if (x + jj < mapSize && x - jj >= 1 && y + jj < mapSize && y - jj >= 1)
/*     */       {
/*     */         
/* 346 */         if (x + ii < mapSize && x - ii >= 1 && y + ii < mapSize && y - ii >= 1) {
/*     */ 
/*     */ 
/*     */           
/* 350 */           if (map[x + jj][y - ii] == wType) {
/* 351 */             closest = wType;
/*     */           }
/* 353 */           if (map[x + ii][y + jj] == wType) {
/* 354 */             closest = wType;
/*     */           }
/* 356 */           if (map[x + jj][y + ii] == wType) {
/* 357 */             closest = wType;
/*     */           }
/* 359 */           if (map[x - ii][y + jj] == wType)
/* 360 */             closest = wType; 
/*     */         }  } 
/* 362 */     }  return closest;
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
/*     */   public static final byte getWaterType(int tilex, int tiley, boolean onSurface) {
/* 374 */     if (onSurface)
/* 375 */       return waterSurface[tilex][tiley]; 
/* 376 */     return waterCave[tilex][tiley];
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
/*     */   public static final String getWaterTypeString(int tilex, int tiley, boolean onSurface) {
/* 388 */     byte waterType = getWaterType(tilex, tiley, onSurface);
/* 389 */     return getWaterTypeString(waterType);
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
/*     */   public static final String getWaterTypeString(byte waterType) {
/* 401 */     switch (waterType) {
/*     */       
/*     */       case 0:
/* 404 */         return "Not Water";
/*     */       case 1:
/* 406 */         return "Water";
/*     */       case 2:
/* 408 */         return "Pond";
/*     */       case 3:
/* 410 */         return "Lake";
/*     */       case 4:
/* 412 */         return "Sea";
/*     */       case 5:
/* 414 */         return "Sea Shallows";
/*     */       case 6:
/* 416 */         return "Lake Shallows";
/*     */       case 7:
/* 418 */         return "Shallows";
/*     */     } 
/* 420 */     return "Unknown (" + waterType + ")";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean isBrackish(int tilex, int tiley, boolean onSurface) {
/* 426 */     byte waterType = getWaterType(tilex, tiley, onSurface);
/* 427 */     return (waterType == 4 || waterType == 5);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\WaterType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */