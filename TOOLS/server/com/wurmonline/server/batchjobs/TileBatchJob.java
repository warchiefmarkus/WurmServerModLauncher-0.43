/*     */ package com.wurmonline.server.batchjobs;
/*     */ 
/*     */ import com.wurmonline.mesh.BushData;
/*     */ import com.wurmonline.mesh.GrassData;
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Constants;
/*     */ import com.wurmonline.server.Server;
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
/*     */ public final class TileBatchJob
/*     */ {
/*  35 */   private static Logger logger = Logger.getLogger(TileBatchJob.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void convertTreeTilesToBushTiles() {
/*  45 */     int min = 0;
/*  46 */     int ms = Constants.meshSize;
/*  47 */     int max = 1 << ms;
/*  48 */     int tile = 0;
/*  49 */     int fixed = 0;
/*  50 */     for (int x = 0; x < max; x++) {
/*     */       
/*  52 */       for (int y = 0; y < max; y++) {
/*     */         
/*  54 */         tile = Server.surfaceMesh.getTile(x, y);
/*  55 */         byte tileType = Tiles.decodeType(tile);
/*     */         
/*  57 */         if (tileType == Tiles.Tile.TILE_TREE.id || tileType == Tiles.Tile.TILE_ENCHANTED_TREE.id || tileType == Tiles.Tile.TILE_MYCELIUM_TREE.id) {
/*     */ 
/*     */ 
/*     */           
/*  61 */           byte data = Tiles.decodeData(tile);
/*  62 */           byte treeType = (byte)(data & 0xF);
/*  63 */           byte treeAge = (byte)(data >>> 4 & 0xF);
/*  64 */           if ((treeType < 0 || treeType >= 10) && treeType != 16 && treeType != 17)
/*     */           {
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
/*  82 */             if (treeType >= 10 && treeType <= 15) {
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
/*  93 */               switch (treeType) {
/*     */                 
/*     */                 case 10:
/*  96 */                   treeType = (byte)BushData.BushType.LAVENDER.getTypeId();
/*     */                   break;
/*     */                 case 11:
/*  99 */                   treeType = (byte)BushData.BushType.ROSE.getTypeId();
/*     */                   break;
/*     */                 case 12:
/* 102 */                   treeType = (byte)BushData.BushType.THORN.getTypeId();
/*     */                   break;
/*     */                 case 13:
/* 105 */                   treeType = (byte)BushData.BushType.GRAPE.getTypeId();
/*     */                   break;
/*     */                 case 14:
/* 108 */                   treeType = (byte)BushData.BushType.CAMELLIA.getTypeId();
/*     */                   break;
/*     */                 case 15:
/* 111 */                   treeType = (byte)BushData.BushType.OLEANDER.getTypeId();
/*     */                   break;
/*     */               } 
/*     */ 
/*     */               
/* 116 */               byte newTileType = Tiles.Tile.TILE_BUSH.id;
/* 117 */               if (tileType == Tiles.Tile.TILE_ENCHANTED_TREE.id) {
/* 118 */                 newTileType = Tiles.Tile.TILE_ENCHANTED_BUSH.id;
/* 119 */               } else if (tileType == Tiles.Tile.TILE_MYCELIUM_TREE.id) {
/* 120 */                 newTileType = Tiles.Tile.TILE_MYCELIUM_BUSH.id;
/* 121 */               }  data = (byte)(treeAge << 4 | treeType);
/* 122 */               Server.surfaceMesh.setTile(x, y, Tiles.encode(Tiles.decodeHeight(tile), newTileType, data));
/* 123 */               fixed++;
/*     */             }  } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 128 */     logger.log(Level.INFO, "Fixed " + fixed + " tiles to bush.");
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
/*     */   public static final void convertKelpReedToNewTileTypes() {
/* 140 */     int min = 0;
/* 141 */     int ms = Constants.meshSize;
/* 142 */     int max = 1 << ms;
/* 143 */     int tile = 0;
/* 144 */     int fixedReed = 0;
/* 145 */     int fixedKelp = 0;
/* 146 */     for (int x = 0; x < max; x++) {
/*     */       
/* 148 */       for (int y = 0; y < max; y++) {
/*     */         
/* 150 */         tile = Server.surfaceMesh.getTile(x, y);
/*     */         
/* 152 */         if (Tiles.decodeType(tile) == Tiles.Tile.TILE_GRASS.id) {
/*     */           
/* 154 */           byte data = Tiles.decodeData(tile);
/* 155 */           GrassData.GrassType grassType = GrassData.GrassType.decodeTileData(data);
/* 156 */           GrassData.GrowthStage growthStage = GrassData.GrowthStage.decodeTileData(data);
/* 157 */           GrassData.FlowerType flowerType = GrassData.FlowerType.decodeTileData(data);
/*     */           
/* 159 */           if (grassType == GrassData.GrassType.KELP) {
/*     */             
/* 161 */             fixedKelp++;
/* 162 */             Server.surfaceMesh.setTile(x, y, Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_KELP.id, GrassData.encodeGrassTileData(growthStage, grassType, flowerType)));
/*     */           }
/* 164 */           else if (grassType == GrassData.GrassType.REED) {
/*     */             
/* 166 */             fixedReed++;
/* 167 */             Server.surfaceMesh.setTile(x, y, Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_REED.id, GrassData.encodeGrassTileData(growthStage, grassType, flowerType)));
/*     */           }
/* 169 */           else if (grassType == GrassData.GrassType.GRASS) {
/*     */             
/* 171 */             Server.surfaceMesh.setTile(x, y, Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_GRASS.id, GrassData.encodeGrassTileData(growthStage, grassType, flowerType)));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 176 */     logger.log(Level.INFO, "Fixed " + fixedReed + " reed tiles and " + fixedKelp + " kelp tiles.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void changeTarredPlanksToNewType() {
/* 184 */     int min = 0;
/* 185 */     int ms = Constants.meshSize;
/* 186 */     int max = 1 << ms;
/* 187 */     int fixed = 0;
/*     */     
/* 189 */     for (int x = 0; x < max; x++) {
/*     */       
/* 191 */       for (int y = 0; y < max; y++) {
/*     */         
/* 193 */         int tile = Server.surfaceMesh.getTile(x, y);
/*     */         
/* 195 */         if (Tiles.decodeType(tile) == Tiles.Tile.TILE_PLANKS.id) {
/*     */           
/* 197 */           byte data = Tiles.decodeData(tile);
/* 198 */           if (data == 1) {
/*     */             
/* 200 */             short h = Tiles.decodeHeight(tile);
/* 201 */             Server.setSurfaceTile(x, y, h, Tiles.Tile.TILE_PLANKS_TARRED.id, (byte)0);
/* 202 */             fixed++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 207 */     logger.log(Level.INFO, "Fixed " + fixed + " tarred floorboards tiles.");
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void clearFlowers() {
/* 212 */     int min = 0;
/* 213 */     int ms = Constants.meshSize;
/* 214 */     int max = 1 << ms;
/* 215 */     int tile = 0;
/* 216 */     int fixed = 0;
/* 217 */     for (int x = 0; x < max; x++) {
/*     */       
/* 219 */       for (int y = 0; y < max; y++) {
/*     */         
/* 221 */         tile = Server.surfaceMesh.getTile(x, y);
/*     */         
/* 223 */         if (Tiles.decodeType(tile) == Tiles.Tile.TILE_GRASS.id)
/*     */         {
/* 225 */           if (Tiles.decodeData(tile & 0xF) > 0) {
/*     */             
/* 227 */             fixed++;
/* 228 */             Server.surfaceMesh.setTile(x, y, Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_GRASS.id, (byte)0));
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 233 */     logger.log(Level.INFO, "Fixed " + fixed + " flowers.");
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void fixCorruptBushData() {
/* 238 */     int min = 0;
/* 239 */     int ms = Constants.meshSize;
/* 240 */     int max = 1 << ms;
/* 241 */     int tile = 0;
/* 242 */     int fixed = 0;
/* 243 */     for (int x = 0; x < max; x++) {
/*     */       
/* 245 */       for (int y = 0; y < max; y++) {
/*     */         
/* 247 */         tile = Server.surfaceMesh.getTile(x, y);
/* 248 */         byte type = Tiles.decodeType(tile);
/* 249 */         if (type == 31)
/*     */         {
/* 251 */           if ((Tiles.decodeData(tile) & 0xF) == 15) {
/*     */             
/* 253 */             fixed++;
/* 254 */             int bushType = Server.rand.nextInt(BushData.BushType.getLength());
/* 255 */             byte newData = (byte)((Tiles.decodeData(tile) & 0xF0) + bushType);
/* 256 */             Server.surfaceMesh.setTile(x, y, Tiles.encode(Tiles.decodeHeight(tile), type, newData));
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 261 */     logger.log(Level.INFO, "Fixed " + fixed + " bushes.");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void splitTreeAndBushes() {
/* 267 */     int min = 0;
/* 268 */     int ms = Constants.meshSize;
/* 269 */     int max = 1 << ms;
/* 270 */     int treefixed = 0;
/* 271 */     int bushfixed = 0;
/* 272 */     for (int x = 0; x < max; x++) {
/*     */       
/* 274 */       for (int y = 0; y < max; y++) {
/*     */         
/* 276 */         int tile = Server.surfaceMesh.getTile(x, y);
/* 277 */         byte tileType = Tiles.decodeType(tile);
/* 278 */         byte tileData = Tiles.decodeData(tile);
/* 279 */         byte newType = tileType;
/* 280 */         byte newData = tileData;
/*     */ 
/*     */         
/* 283 */         if (tileType == 3) {
/*     */           
/* 285 */           newType = getNewNormalTreeType(tileType, tileData);
/* 286 */           treefixed++;
/*     */         }
/* 288 */         else if (tileType == 11) {
/*     */           
/* 290 */           newType = getNewMyceliumTreeType(tileType, tileData);
/* 291 */           treefixed++;
/*     */         }
/* 293 */         else if (tileType == 14) {
/*     */           
/* 295 */           newType = getNewEnchantedTreeType(tileType, tileData);
/* 296 */           treefixed++;
/*     */         } 
/* 298 */         if (tileType == 31) {
/*     */           
/* 300 */           newType = getNewNormalBushType(tileType, tileData);
/* 301 */           bushfixed++;
/*     */         }
/* 303 */         else if (tileType == 35) {
/*     */           
/* 305 */           newType = getNewMyceliumBushType(tileType, tileData);
/* 306 */           bushfixed++;
/*     */         }
/* 308 */         else if (tileType == 34) {
/*     */           
/* 310 */           newType = getNewEnchantedBushType(tileType, tileData);
/* 311 */           bushfixed++;
/*     */         } 
/*     */         
/* 314 */         if (newType != tileType) {
/*     */           
/* 316 */           newData = (byte)((tileData & 0xF0) + 1);
/* 317 */           Server.surfaceMesh.setTile(x, y, Tiles.encode(Tiles.decodeHeight(tile), newType, newData));
/*     */         } 
/*     */       } 
/*     */     } 
/* 321 */     logger.log(Level.INFO, "Converted " + treefixed + " trees and " + bushfixed + " bushes.");
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte getNewNormalTreeType(byte tileType, byte tileData) {
/* 326 */     switch (tileData & 0xF) {
/*     */       case 0:
/* 328 */         return 100;
/* 329 */       case 1: return 101;
/* 330 */       case 2: return 102;
/* 331 */       case 3: return 103;
/* 332 */       case 4: return 104;
/* 333 */       case 5: return 105;
/* 334 */       case 6: return 106;
/* 335 */       case 7: return 107;
/* 336 */       case 8: return 108;
/* 337 */       case 9: return 109;
/* 338 */       case 10: return 110;
/* 339 */       case 11: return 111;
/* 340 */       case 12: return 112;
/* 341 */       case 13: return 113;
/* 342 */     }  return tileType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte getNewEnchantedTreeType(byte tileType, byte tileData) {
/* 348 */     switch (tileData & 0xF) {
/*     */       case 0:
/* 350 */         return Byte.MIN_VALUE;
/* 351 */       case 1: return -127;
/* 352 */       case 2: return -126;
/* 353 */       case 3: return -125;
/* 354 */       case 4: return -124;
/* 355 */       case 5: return -123;
/* 356 */       case 6: return -122;
/* 357 */       case 7: return -121;
/* 358 */       case 8: return -120;
/* 359 */       case 9: return -119;
/* 360 */       case 10: return -118;
/* 361 */       case 11: return -117;
/* 362 */       case 12: return -116;
/* 363 */       case 13: return -115;
/* 364 */     }  return tileType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte getNewMyceliumTreeType(byte tileType, byte tileData) {
/* 370 */     switch (tileData & 0xF) {
/*     */       case 0:
/* 372 */         return 114;
/* 373 */       case 1: return 115;
/* 374 */       case 2: return 116;
/* 375 */       case 3: return 117;
/* 376 */       case 4: return 118;
/* 377 */       case 5: return 119;
/* 378 */       case 6: return 120;
/* 379 */       case 7: return 121;
/* 380 */       case 8: return 122;
/* 381 */       case 9: return 123;
/* 382 */       case 10: return 124;
/* 383 */       case 11: return 125;
/* 384 */       case 12: return 126;
/* 385 */       case 13: return Byte.MAX_VALUE;
/* 386 */     }  return tileType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte getNewNormalBushType(byte tileType, byte tileData) {
/* 392 */     switch (tileData & 0xF) {
/*     */       case 0:
/* 394 */         return -114;
/* 395 */       case 1: return -113;
/* 396 */       case 2: return -112;
/* 397 */       case 3: return -111;
/* 398 */       case 4: return -110;
/* 399 */       case 5: return -109;
/* 400 */     }  return tileType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte getNewEnchantedBushType(byte tileType, byte tileData) {
/* 406 */     switch (tileData & 0xF) {
/*     */       case 0:
/* 408 */         return -102;
/* 409 */       case 1: return -101;
/* 410 */       case 2: return -100;
/* 411 */       case 3: return -99;
/* 412 */       case 4: return -98;
/* 413 */       case 5: return -97;
/* 414 */     }  return tileType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte getNewMyceliumBushType(byte tileType, byte tileData) {
/* 420 */     switch (tileData & 0xF) {
/*     */       case 0:
/* 422 */         return -108;
/* 423 */       case 1: return -107;
/* 424 */       case 2: return -106;
/* 425 */       case 3: return -105;
/* 426 */       case 4: return -104;
/* 427 */       case 5: return -103;
/* 428 */     }  return tileType;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\batchjobs\TileBatchJob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */