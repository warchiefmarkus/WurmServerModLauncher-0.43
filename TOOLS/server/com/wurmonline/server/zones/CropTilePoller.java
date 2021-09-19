/*     */ package com.wurmonline.server.zones;
/*     */ 
/*     */ import com.wurmonline.mesh.MeshIO;
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.behaviours.Crops;
/*     */ import com.wurmonline.server.sounds.SoundPlayer;
/*     */ import com.wurmonline.shared.constants.SoundNames;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ public class CropTilePoller
/*     */   implements TimeConstants, SoundNames
/*     */ {
/*  48 */   private static final Logger logger = Logger.getLogger(CropTilePoller.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean logTilePolling = false;
/*     */ 
/*     */ 
/*     */   
/*  56 */   private static volatile long lastPolledTiles = 0L;
/*  57 */   private static final Map<Long, CropTile> tiles = new HashMap<>();
/*     */   
/*     */   private static boolean isInitialized = false;
/*     */   
/*     */   static {
/*  62 */     lastPolledTiles = System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Long cropTileIndexFor(CropTile cropTile) {
/*  71 */     return Long.valueOf(Tiles.getTileId(cropTile.getX(), cropTile.getY(), 0, cropTile.isOnSurface()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addCropTile(int tileData, int x, int y, int cropType, boolean surface) {
/*  76 */     synchronized (tiles) {
/*     */       
/*  78 */       CropTile cTile = new CropTile(tileData, x, y, cropType, surface);
/*  79 */       tiles.put(cropTileIndexFor(cTile), cTile);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void initializeFields() {
/*  85 */     logger.log(Level.INFO, "CROPS_POLLER: Collecting tile data.");
/*  86 */     addAllFieldsInMesh(Server.surfaceMesh, true);
/*  87 */     addAllFieldsInMesh(Server.caveMesh, false);
/*     */     
/*  89 */     logCropFields();
/*  90 */     isInitialized = true;
/*  91 */     logger.log(Level.INFO, "CROPS_POLLER: Collecting tile Finished.");
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addAllFieldsInMesh(MeshIO mesh, boolean surface) {
/*  96 */     int mapSize = mesh.getSize();
/*  97 */     Tiles.Tile tileToLookFor = Tiles.Tile.TILE_FIELD;
/*  98 */     Tiles.Tile tile2ToLookFor = Tiles.Tile.TILE_FIELD2;
/*     */     
/* 100 */     for (int x = 0; x < mapSize; x++) {
/*     */       
/* 102 */       for (int y = 0; y < mapSize; y++) {
/*     */         
/* 104 */         int tileId = mesh.getTile(x, y);
/* 105 */         byte type = Tiles.decodeType(tileId);
/* 106 */         Tiles.Tile tileEnum = Tiles.getTile(type);
/*     */         
/* 108 */         if (tileEnum != null && (tileEnum.id == tileToLookFor.id || tileEnum.id == tile2ToLookFor.id)) {
/*     */           
/* 110 */           byte data = Tiles.decodeData(tileId);
/* 111 */           int crop = Crops.getCropNumber(type, data);
/*     */           
/* 113 */           CropTile cropTile = new CropTile(tileId, x, y, crop, surface);
/* 114 */           tiles.put(cropTileIndexFor(cropTile), cropTile);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void logCropFields() {
/* 122 */     Map<Integer, Integer> map = new HashMap<>();
/* 123 */     for (Iterator<CropTile> it = tiles.values().iterator(); it.hasNext(); ) {
/*     */       
/* 125 */       CropTile tile = it.next();
/* 126 */       Integer crop = Integer.valueOf(tile.getCropType());
/* 127 */       Integer count = map.get(crop);
/* 128 */       if (count == null) {
/*     */         
/* 130 */         map.put(crop, Integer.valueOf(1));
/*     */         
/*     */         continue;
/*     */       } 
/* 134 */       count = Integer.valueOf(count.intValue() + 1);
/* 135 */       map.put(crop, count);
/*     */     } 
/*     */ 
/*     */     
/* 139 */     String text = "\n";
/* 140 */     for (Integer crop : map.keySet()) {
/*     */       
/* 142 */       Integer count = map.get(crop);
/* 143 */       String cropName = Crops.getCropName(crop.intValue());
/* 144 */       text = text + cropName + " fields: " + count.toString() + "\n";
/*     */     } 
/* 146 */     logger.log(Level.INFO, text);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void pollCropTiles() {
/* 151 */     if (!isInitialized) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 157 */     long now = System.currentTimeMillis();
/* 158 */     long fieldGrowthTime = Servers.localServer.getFieldGrowthTime();
/*     */     
/* 160 */     long elapsedSinceLastPoll = now - lastPolledTiles;
/* 161 */     boolean timeToPoll = (elapsedSinceLastPoll >= fieldGrowthTime);
/*     */     
/* 163 */     if (!timeToPoll) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 171 */     List<CropTile> toRemove = new ArrayList<>();
/* 172 */     synchronized (tiles) {
/*     */ 
/*     */       
/* 175 */       if (now - lastPolledTiles < fieldGrowthTime) {
/*     */         return;
/*     */       }
/* 178 */       lastPolledTiles = System.currentTimeMillis();
/* 179 */       for (CropTile cTile : tiles.values()) {
/*     */         
/* 181 */         MeshIO meshToUse = Server.surfaceMesh;
/* 182 */         if (!cTile.isOnSurface()) {
/* 183 */           meshToUse = Server.caveMesh;
/*     */         }
/* 185 */         int currTileId = meshToUse.getTile(cTile.getX(), cTile.getY());
/* 186 */         byte type = Tiles.decodeType(currTileId);
/* 187 */         Tiles.Tile tileEnum = Tiles.getTile(type);
/* 188 */         if (tileEnum == null || (tileEnum.id != Tiles.Tile.TILE_FIELD.id && tileEnum.id != Tiles.Tile.TILE_FIELD2.id)) {
/*     */ 
/*     */ 
/*     */           
/* 192 */           toRemove.add(cTile);
/*     */           
/*     */           continue;
/*     */         } 
/* 196 */         byte data = Tiles.decodeData(currTileId);
/*     */ 
/*     */ 
/*     */         
/* 200 */         checkForFarmGrowth(currTileId, cTile.getX(), cTile.getY(), type, data, meshToUse, cTile.isOnSurface());
/*     */       } 
/*     */       
/* 203 */       for (CropTile t : toRemove)
/*     */       {
/* 205 */         tiles.remove(cropTileIndexFor(t));
/*     */       }
/*     */     } 
/* 208 */     logger.fine("Completed poll of crop tiles.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkForFarmGrowth(int tile, int tilex, int tiley, byte type, byte aData, MeshIO currentMesh, boolean pollingSurface) {
/* 215 */     if (Zones.protectedTiles[tilex][tiley])
/*     */       return; 
/* 217 */     int tileAge = Crops.decodeFieldAge(aData);
/* 218 */     int crop = Crops.getCropNumber(type, aData);
/* 219 */     boolean farmed = Crops.decodeFieldState(aData);
/* 220 */     if (logTilePolling) {
/* 221 */       logger.log(Level.INFO, "Polling farm at " + tilex + "," + tiley + ", age=" + tileAge + ", crop=" + crop + ", farmed=" + farmed);
/*     */     }
/* 223 */     if (tileAge == 7) {
/*     */       
/* 225 */       if (Server.rand.nextInt(100) <= 10) {
/*     */         
/* 227 */         currentMesh.setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_DIRT.id, (byte)0));
/* 228 */         Server.modifyFlagsByTileType(tilex, tiley, Tiles.Tile.TILE_DIRT.id);
/* 229 */         Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/* 230 */         if (logTilePolling)
/* 231 */           logger.log(Level.INFO, "Set to dirt"); 
/*     */       } 
/* 233 */       if (WurmCalendar.isNight())
/*     */       {
/* 235 */         SoundPlayer.playSound("sound.birdsong.bird1", tilex, tiley, pollingSurface, 2.0F);
/*     */       }
/*     */       else
/*     */       {
/* 239 */         SoundPlayer.playSound("sound.birdsong.bird3", tilex, tiley, pollingSurface, 2.0F);
/*     */       }
/*     */     
/* 242 */     } else if (tileAge < 7) {
/*     */       
/* 244 */       if ((tileAge == 5 || tileAge == 6) && 
/* 245 */         Server.rand.nextInt(3) < 2)
/*     */         return; 
/* 247 */       tileAge++;
/* 248 */       VolaTile tempvtile1 = Zones.getOrCreateTile(tilex, tiley, pollingSurface);
/* 249 */       if (tempvtile1.getStructure() != null) {
/*     */         
/* 251 */         currentMesh.setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_DIRT.id, (byte)0));
/* 252 */         Server.modifyFlagsByTileType(tilex, tiley, Tiles.Tile.TILE_DIRT.id);
/*     */       }
/*     */       else {
/*     */         
/* 256 */         currentMesh.setTile(tilex, tiley, 
/* 257 */             Tiles.encode(Tiles.decodeHeight(tile), type, Crops.encodeFieldData(false, tileAge, crop)));
/* 258 */         Server.modifyFlagsByTileType(tilex, tiley, type);
/* 259 */         if (logTilePolling)
/* 260 */           logger.log(Level.INFO, "Changed the tile"); 
/*     */       } 
/* 262 */       if (WurmCalendar.isNight()) {
/*     */         
/* 264 */         SoundPlayer.playSound("sound.ambient.night.crickets", tilex, tiley, pollingSurface, 0.0F);
/*     */       }
/*     */       else {
/*     */         
/* 268 */         SoundPlayer.playSound("sound.birdsong.bird2", tilex, tiley, pollingSurface, 1.0F);
/*     */       } 
/* 270 */       Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/*     */     } else {
/*     */       
/* 273 */       logger.log(Level.WARNING, "Strange, tile " + tilex + ", " + tiley + " is field but has age above 7:" + tileAge + " crop is " + crop + " farmed is " + farmed);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\CropTilePoller.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */