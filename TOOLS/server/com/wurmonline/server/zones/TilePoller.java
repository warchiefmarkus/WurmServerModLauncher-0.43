/*      */ package com.wurmonline.server.zones;
/*      */ 
/*      */ import com.wurmonline.mesh.BushData;
/*      */ import com.wurmonline.mesh.FieldData;
/*      */ import com.wurmonline.mesh.FoliageAge;
/*      */ import com.wurmonline.mesh.GrassData;
/*      */ import com.wurmonline.mesh.MeshIO;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.mesh.TreeData;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.GeneralUtilities;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Point4f;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.behaviours.Crops;
/*      */ import com.wurmonline.server.behaviours.MethodsItems;
/*      */ import com.wurmonline.server.behaviours.Terraforming;
/*      */ import com.wurmonline.server.creatures.MineDoorPermission;
/*      */ import com.wurmonline.server.highways.HighwayPos;
/*      */ import com.wurmonline.server.highways.MethodsHighways;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.shared.constants.SoundNames;
/*      */ import java.util.Random;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class TilePoller
/*      */   implements TimeConstants, SoundNames, MiscConstants
/*      */ {
/*      */   public static boolean logTilePolling = false;
/*   73 */   private static int MINIMUM_REED_HEIGHT = 0;
/*      */ 
/*      */ 
/*      */   
/*   77 */   private static int MINIMUM_KELP_HEIGHT = -30;
/*      */   
/*   79 */   private static int MAX_KELP_HEIGHT = -400;
/*   80 */   public static MeshIO currentMesh = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   86 */   public static int currentPollTile = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   94 */   public static int pollModifier = 7;
/*      */   
/*   96 */   private static final Logger logger = Logger.getLogger(TilePoller.class.getName());
/*      */   
/*      */   public static boolean pollingSurface = true;
/*      */   
/*      */   private static int nthTick;
/*      */   
/*      */   private static int currTick;
/*      */   
/*      */   public static int rest;
/*      */   
/*      */   private static boolean manyPerTick = false;
/*      */   
/*      */   private static final long FORAGE_PRIME = 101531L;
/*      */   
/*      */   private static final long IRON_PRIME = 103591L;
/*      */   
/*      */   private static final long HERB_PRIME = 102563L;
/*      */   private static final long INVESTIGATE_PRIME = 104149L;
/*      */   private static final long SEARCH_PRIME = 103025L;
/*  115 */   private static int forageChance = 2;
/*  116 */   private static int herbChance = 2;
/*  117 */   private static int investigateChance = 2;
/*  118 */   private static int searchChance = 2;
/*  119 */   private static final Random r = new Random();
/*  120 */   private static final long HIVE_PRIME = 102700L + WurmCalendar.getYear();
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int hiveFactor = 500;
/*      */ 
/*      */   
/*  127 */   public static int pollround = 0;
/*      */   private static boolean pollEruption = false;
/*      */   private static boolean createFlowers = false;
/*  130 */   public static int treeGrowth = 20;
/*  131 */   private static int flowerCounter = 0;
/*      */   public static boolean entryServer = false;
/*  133 */   private static final Random kelpRandom = new Random();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  139 */   public static int mask = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void calcRest() {
/*  152 */     int ticksPerPeriod = 3456000;
/*      */     
/*  154 */     int tiles = (1 << Constants.meshSize) * (1 << Constants.meshSize);
/*  155 */     logger.log(Level.INFO, "Current polltile=" + currentPollTile + ", rest=" + rest + " pollmodifier=" + pollModifier + ", pollround=" + pollround);
/*      */     
/*  157 */     if (3456000 >= tiles) {
/*      */       
/*  159 */       nthTick = 3456000 / tiles;
/*  160 */       if (currentPollTile == 0) {
/*  161 */         rest = 3456000 % tiles;
/*      */       }
/*      */     } else {
/*      */       
/*  165 */       nthTick = tiles / 3456000;
/*  166 */       if (currentPollTile == 0)
/*  167 */         rest = tiles % 3456000; 
/*  168 */       manyPerTick = true;
/*      */     } 
/*  170 */     logger.log(Level.INFO, "tiles=" + tiles + ", mask=" + mask + " ticksperday=" + 3456000 + ", Nthick=" + nthTick + ", rest=" + rest + ", manypertick=" + manyPerTick);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void pollNext() {
/*  176 */     if (Constants.isGameServer)
/*      */     {
/*  178 */       if (manyPerTick) {
/*      */         int x;
/*  180 */         for (x = 0; x < nthTick; x++) {
/*      */           
/*  182 */           pollingSurface = true;
/*  183 */           pollNextTile();
/*  184 */           pollingSurface = false;
/*  185 */           pollNextTile();
/*  186 */           checkPolltile();
/*      */         } 
/*  188 */         if (rest > 0)
/*      */         {
/*  190 */           for (x = 0; x < Math.min(rest, nthTick); x++)
/*      */           {
/*      */             
/*  193 */             pollingSurface = true;
/*  194 */             pollNextTile();
/*  195 */             pollingSurface = false;
/*  196 */             pollNextTile();
/*  197 */             checkPolltile();
/*  198 */             rest--;
/*  199 */             if (rest == 0)
/*      */             {
/*  201 */               logger.log(Level.INFO, "...and THERE all rest-tiles are gone.");
/*      */             }
/*      */           }
/*      */         
/*      */         }
/*      */       } else {
/*      */         
/*  208 */         currTick++;
/*  209 */         if (currTick == nthTick) {
/*      */           
/*  211 */           pollingSurface = true;
/*  212 */           pollNextTile();
/*  213 */           pollingSurface = false;
/*  214 */           pollNextTile();
/*  215 */           checkPolltile();
/*  216 */           currTick = 0;
/*  217 */           if (rest > 0)
/*      */           {
/*  219 */             for (int x = 0; x < Math.min(rest, nthTick); x++) {
/*      */               
/*  221 */               pollingSurface = true;
/*  222 */               pollNextTile();
/*  223 */               pollingSurface = false;
/*  224 */               pollNextTile();
/*  225 */               checkPolltile();
/*  226 */               rest--;
/*  227 */               if (rest == 0)
/*      */               {
/*  229 */                 logger.log(Level.INFO, "...and THERE all rest-tiles are gone.");
/*      */               }
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void pollNextTile() {
/*  242 */     if (pollingSurface) {
/*  243 */       currentMesh = Server.surfaceMesh;
/*      */     } else {
/*  245 */       currentMesh = Server.caveMesh;
/*  246 */     }  if (pollingSurface) {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/*  251 */         int temptile1 = currentMesh.data[currentPollTile];
/*  252 */         int temptilex = currentPollTile & (1 << Constants.meshSize) - 1;
/*  253 */         int temptiley = currentPollTile >> Constants.meshSize;
/*  254 */         byte tempint1 = Tiles.decodeType(temptile1);
/*  255 */         byte temptile2 = Tiles.decodeData(temptile1);
/*  256 */         checkEffects(temptile1, temptilex, temptiley, tempint1, temptile2);
/*  257 */         VolaTile tempvtile1 = Zones.getTileOrNull(temptilex, temptiley, pollingSurface);
/*  258 */         if (tempvtile1 != null)
/*      */         {
/*  260 */           tempvtile1.pollStructures(System.currentTimeMillis());
/*      */         }
/*  262 */       } catch (Exception e) {
/*  263 */         logger.severe("Indexoutofbounds: data array size: " + (currentMesh.getData()).length + " polltile: " + currentPollTile);
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  269 */       int temptile1 = currentMesh.data[currentPollTile];
/*      */       
/*  271 */       byte tempbyte1 = Tiles.decodeType(temptile1);
/*  272 */       if (tempbyte1 == Tiles.Tile.TILE_CAVE.id || Tiles.isReinforcedFloor(tempbyte1) || tempbyte1 == Tiles.Tile.TILE_CAVE_EXIT.id) {
/*      */         
/*  274 */         int temptilex = currentPollTile & (1 << Constants.meshSize) - 1;
/*  275 */         int temptiley = currentPollTile >> Constants.meshSize;
/*  276 */         int data = Tiles.decodeData(temptile1) & 0xFF;
/*  277 */         checkCaveDecay(temptile1, temptilex, temptiley, tempbyte1, data);
/*  278 */         VolaTile tempvtile1 = Zones.getTileOrNull(temptilex, temptiley, pollingSurface);
/*  279 */         if (tempvtile1 != null)
/*      */         {
/*  281 */           tempvtile1.pollStructures(System.currentTimeMillis());
/*      */         }
/*      */       }
/*  284 */       else if (tempbyte1 == Tiles.Tile.TILE_CAVE_WALL.id) {
/*      */ 
/*      */         
/*  287 */         int temptilex = currentPollTile & (1 << Constants.meshSize) - 1;
/*  288 */         int temptiley = currentPollTile >> Constants.meshSize;
/*      */         
/*  290 */         byte state = Zones.getMiningState(temptilex, temptiley);
/*  291 */         if (state == 0) {
/*      */           
/*  293 */           r.setSeed((temptilex + temptiley * Zones.worldTileSizeY) * 102533L);
/*      */           
/*  295 */           if (r.nextInt(100) == 0) {
/*      */             
/*  297 */             Server.caveMesh.setTile(temptilex, temptiley, 
/*      */                 
/*  299 */                 Tiles.encode(Tiles.decodeHeight(temptile1), Tiles.Tile.TILE_CAVE_WALL_ROCKSALT.id, 
/*  300 */                   Tiles.decodeData(temptile1)));
/*  301 */             Players.getInstance().sendChangedTile(temptilex, temptiley, false, true);
/*      */           } 
/*  303 */           r.setSeed((temptilex + temptiley * Zones.worldTileSizeY) * 123307L);
/*      */           
/*  305 */           if (r.nextInt(64) == 0) {
/*      */             
/*  307 */             Server.caveMesh.setTile(temptilex, temptiley, 
/*      */                 
/*  309 */                 Tiles.encode(Tiles.decodeHeight(temptile1), Tiles.Tile.TILE_CAVE_WALL_SANDSTONE.id, 
/*  310 */                   Tiles.decodeData(temptile1)));
/*  311 */             Players.getInstance().sendChangedTile(temptilex, temptiley, false, true);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void checkCaveDecay(int tile, int tilex, int tiley, byte type, int _data) {
/*  360 */     if (Zones.protectedTiles[tilex][tiley]) {
/*      */       return;
/*      */     }
/*  363 */     Village village = Villages.getVillage(tilex, tiley, true);
/*  364 */     if (village != null && village.isPermanent) {
/*      */       return;
/*      */     }
/*      */     
/*  368 */     HighwayPos highwayPos = MethodsHighways.getHighwayPos(tilex, tiley, false);
/*  369 */     if (highwayPos != null && MethodsHighways.onHighway(highwayPos)) {
/*      */       return;
/*      */     }
/*  372 */     boolean decay = false;
/*  373 */     if (Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE_EXIT.id) {
/*      */ 
/*      */       
/*  376 */       if (Server.rand.nextInt(60) == 0)
/*      */       {
/*      */         
/*  379 */         if (!Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile(tilex, tiley)))) {
/*      */           
/*  381 */           decay = true;
/*  382 */           if (Tiles.decodeType(currentMesh.getTile(tilex, tiley - 1)) != Tiles.Tile.TILE_CAVE_WALL.id && 
/*  383 */             Tiles.decodeType(currentMesh.getTile(tilex, tiley - 1)) != Tiles.Tile.TILE_CAVE.id)
/*  384 */             decay = false; 
/*  385 */           if (decay && Tiles.decodeType(currentMesh.getTile(tilex, tiley + 1)) != Tiles.Tile.TILE_CAVE_WALL.id && 
/*  386 */             Tiles.decodeType(currentMesh.getTile(tilex, tiley + 1)) != Tiles.Tile.TILE_CAVE.id)
/*  387 */             decay = false; 
/*  388 */           if (decay && Tiles.decodeType(currentMesh.getTile(tilex + 1, tiley)) != Tiles.Tile.TILE_CAVE_WALL.id && 
/*  389 */             Tiles.decodeType(currentMesh.getTile(tilex + 1, tiley)) != Tiles.Tile.TILE_CAVE.id)
/*  390 */             decay = false; 
/*  391 */           if (decay && Tiles.decodeType(currentMesh.getTile(tilex - 1, tiley)) != Tiles.Tile.TILE_CAVE_WALL.id && 
/*  392 */             Tiles.decodeType(currentMesh.getTile(tilex - 1, tiley)) != Tiles.Tile.TILE_CAVE.id) {
/*  393 */             decay = false;
/*      */           }
/*      */         } 
/*      */       }
/*      */     } else {
/*      */       
/*  399 */       int tempint1 = Server.rand.nextInt(4);
/*  400 */       int tx = 0;
/*  401 */       int ty = -1;
/*  402 */       if (tempint1 == 1) {
/*      */         
/*  404 */         tx = -1;
/*  405 */         ty = 0;
/*      */       }
/*  407 */       else if (tempint1 == 2) {
/*      */         
/*  409 */         tx = 1;
/*  410 */         ty = 0;
/*      */       }
/*  412 */       else if (tempint1 == 3) {
/*      */         
/*  414 */         tx = 0;
/*  415 */         ty = 1;
/*      */       } 
/*  417 */       int temptile1 = currentMesh.getTile(tilex + tx, tiley + ty);
/*      */ 
/*      */       
/*  420 */       if (Tiles.decodeType(temptile1) == Tiles.Tile.TILE_CAVE_WALL.id)
/*      */       {
/*  422 */         if (Server.rand.nextFloat() <= 0.002F)
/*      */         {
/*  424 */           decay = true;
/*      */         }
/*      */       }
/*  427 */       if (decay && Tiles.isReinforcedFloor(Tiles.decodeType(tile)))
/*      */       {
/*      */         
/*  430 */         if (Server.rand.nextInt(10) < 8)
/*      */         {
/*  432 */           decay = false;
/*      */         }
/*      */       }
/*      */     } 
/*  436 */     if (decay) {
/*      */ 
/*      */       
/*  439 */       for (int x = -1; x <= 1; x++) {
/*      */         
/*  441 */         for (int y = -1; y <= 1; y++) {
/*      */           
/*  443 */           VolaTile tempvtile2 = Zones.getTileOrNull(tilex + x, tiley + y, false);
/*  444 */           if (tempvtile2 != null && tempvtile2.getStructure() != null && tempvtile2.getStructure().isFinished()) {
/*      */             return;
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  451 */       VolaTile tempvtile1 = Zones.getTileOrNull(tilex, tiley, false);
/*  452 */       if (tempvtile1 != null) {
/*      */         
/*  454 */         if ((tempvtile1.getCreatures()).length > 0) {
/*      */           return;
/*      */         }
/*  457 */         tempvtile1.destroyEverything();
/*      */       } 
/*  459 */       byte state = Zones.getMiningState(tilex, tiley);
/*  460 */       if (type == Tiles.Tile.TILE_CAVE_EXIT.id) {
/*      */         
/*  462 */         Terraforming.setAsRock(tilex, tiley, true);
/*  463 */         if (state != 0) {
/*      */           
/*  465 */           Zones.setMiningState(tilex, tiley, (byte)0, false);
/*  466 */           Zones.deleteMiningTile(tilex, tiley);
/*      */         } 
/*  468 */         if (logger.isLoggable(Level.FINER))
/*      */         {
/*  470 */           logger.finer("Caved in EXIT at " + tilex + ", " + tiley);
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/*  475 */         Terraforming.caveIn(tilex, tiley);
/*  476 */         if (state != 0) {
/*      */           
/*  478 */           Zones.setMiningState(tilex, tiley, (byte)0, false);
/*  479 */           Zones.deleteMiningTile(tilex, tiley);
/*      */         } 
/*  481 */         if (logger.isLoggable(Level.FINER))
/*      */         {
/*  483 */           logger.finer("Caved in " + tilex + ", " + tiley);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*  489 */   public static int sentTilePollMessages = 0;
/*      */ 
/*      */   
/*      */   public static void checkEffects(int tile, int tilex, int tiley, byte type, byte aData) {
/*  493 */     if (logTilePolling && sentTilePollMessages < 10) {
/*      */       
/*  495 */       logger.log(Level.INFO, "Tile Polling is working for " + tilex + "," + tiley);
/*  496 */       sentTilePollMessages++;
/*      */     } 
/*      */     
/*  499 */     Tiles.Tile theTile = Tiles.getTile(type);
/*      */     
/*  501 */     if (!WurmCalendar.isSeasonWinter()) {
/*      */       
/*  503 */       Server.setGatherable(tilex, tiley, false);
/*  504 */       HiveZone hz = Zones.getHiveZoneAt(tilex, tiley, true);
/*  505 */       if (hz != null) {
/*  506 */         checkHoneyProduction(hz, tilex, tiley, type, aData, theTile);
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/*  511 */       if (!containsStructure(tilex, tiley)) {
/*  512 */         Server.setGatherable(tilex, tiley, true);
/*      */       }
/*  514 */       Item[] hives = Zones.getActiveDomesticHives(tilex, tiley);
/*  515 */       if (hives != null)
/*      */       {
/*  517 */         for (Item hive : hives) {
/*      */ 
/*      */           
/*  520 */           if (hive.hasTwoQueens())
/*      */           {
/*  522 */             if (hive.removeQueen() && Servers.isThisATestServer())
/*  523 */               Players.getInstance().sendGmMessage(null, "System", "Debug: Removed queen @ " + tilex + "," + tiley + " Two:" + hive.hasTwoQueens(), false); 
/*      */           }
/*  525 */           if (Server.rand.nextInt(10) == 0) {
/*      */             
/*  527 */             Item sugar = hive.getSugar();
/*  528 */             if (sugar != null) {
/*      */ 
/*      */               
/*  531 */               Items.destroyItem(sugar.getWurmId());
/*      */             }
/*      */             else {
/*      */               
/*  535 */               Item honey = hive.getHoney();
/*  536 */               if (honey != null) {
/*      */                 
/*  538 */                 if (honey.getWeightGrams() < 1000)
/*      */                 {
/*  540 */                   if (Server.rand.nextInt(10) == 0)
/*      */                   {
/*  542 */                     if (hive.removeQueen() && Servers.isThisATestServer()) {
/*  543 */                       Players.getInstance().sendGmMessage(null, "System", "Debug: Removed queen @ " + tilex + "," + tiley + " Two:" + hive.hasTwoQueens(), false);
/*      */                     }
/*      */                   }
/*      */                 }
/*  547 */                 honey.setWeight(Math.max(0, honey.getWeightGrams() - 10), true);
/*      */               }
/*  549 */               else if (Server.rand.nextInt(3) == 0) {
/*      */                 
/*  551 */                 if (hive.removeQueen() && Servers.isThisATestServer()) {
/*  552 */                   Players.getInstance().sendGmMessage(null, "System", "Debug: Removed queen @ " + tilex + "," + tiley + " No Honey!", false);
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*  560 */     if (Zones.protectedTiles[tilex][tiley]) {
/*      */       return;
/*      */     }
/*      */     
/*  564 */     if (type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_KELP.id || type == Tiles.Tile.TILE_REED.id) {
/*      */       
/*  566 */       if (Server.rand.nextInt(10) < 1)
/*      */       {
/*  568 */         checkForMycelGrowth(tile, tilex, tiley, type, aData);
/*      */       }
/*  570 */       else if (Tiles.decodeHeight(tile) > 0)
/*      */       {
/*  572 */         checkForSeedGrowth(tile, tilex, tiley);
/*  573 */         checkForGrassGrowth(tile, tilex, tiley, type, aData, true);
/*      */       }
/*      */       else
/*      */       {
/*  577 */         checkForGrassGrowth(tile, tilex, tiley, type, aData, true);
/*      */       }
/*      */     
/*  580 */     } else if (type == Tiles.Tile.TILE_DIRT.id) {
/*      */       
/*  582 */       if (Server.rand.nextInt(10) < 1)
/*      */       {
/*  584 */         checkForMycelGrowth(tile, tilex, tiley, type, aData);
/*      */       }
/*  586 */       else if (Server.rand.nextInt(5) < 1)
/*      */       {
/*  588 */         checkForGrassSpread(tile, tilex, tiley, type, aData);
/*      */       }
/*  590 */       else if (Server.rand.nextInt(3) < 1)
/*      */       {
/*      */         
/*  593 */         checkGrubGrowth(tile, tilex, tiley);
/*      */       }
/*      */     
/*  596 */     } else if (type == Tiles.Tile.TILE_SAND.id) {
/*      */       
/*  598 */       if (Server.rand.nextInt(10) < 1)
/*      */       {
/*      */         
/*  601 */         checkGrubGrowth(tile, tilex, tiley);
/*      */       }
/*      */     }
/*  604 */     else if (type == Tiles.Tile.TILE_DIRT_PACKED.id) {
/*      */       
/*  606 */       if (Server.rand.nextInt(20) == 1)
/*      */       {
/*  608 */         checkForGrassSpread(tile, tilex, tiley, type, aData);
/*      */       }
/*      */     }
/*  611 */     else if (type == Tiles.Tile.TILE_LAWN.id) {
/*      */       
/*  613 */       if (Server.rand.nextInt(50) == 1) {
/*      */ 
/*      */         
/*  616 */         Village v = Villages.getVillage(tilex, tiley, true);
/*  617 */         if (v == null && Server.rand.nextInt(100) > 75)
/*      */         {
/*      */           
/*  620 */           currentMesh.setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_GRASS.id, (byte)0));
/*  621 */           Server.modifyFlagsByTileType(tilex, tiley, Tiles.Tile.TILE_GRASS.id);
/*  622 */           Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/*      */         }
/*      */       
/*  625 */       } else if (Server.rand.nextInt(10) < 1) {
/*      */         
/*  627 */         checkForMycelGrowth(tile, tilex, tiley, type, aData);
/*      */       }
/*      */     
/*  630 */     } else if (type == Tiles.Tile.TILE_STEPPE.id) {
/*      */       
/*  632 */       if (Server.rand.nextInt(10) < 1)
/*      */       {
/*  634 */         checkForMycelGrowth(tile, tilex, tiley, type, aData);
/*      */       }
/*  636 */       else if (Tiles.decodeHeight(tile) > 0)
/*      */       {
/*  638 */         checkForSeedGrowth(tile, tilex, tiley);
/*      */       }
/*      */     
/*  641 */     } else if (type == Tiles.Tile.TILE_MOSS.id) {
/*      */       
/*  643 */       if (Server.rand.nextInt(10) < 1)
/*      */       {
/*  645 */         checkForMycelGrowth(tile, tilex, tiley, type, aData);
/*      */       }
/*  647 */       else if (Server.rand.nextInt(3) == 1 && Tiles.decodeHeight(tile) > 0)
/*      */       {
/*  649 */         checkForSeedGrowth(tile, tilex, tiley);
/*      */       }
/*      */     
/*  652 */     } else if (type == Tiles.Tile.TILE_PEAT.id) {
/*      */       
/*  654 */       if (Server.rand.nextInt(5) == 1 && Tiles.decodeHeight(tile) > 0)
/*      */       {
/*  656 */         checkForSeedGrowth(tile, tilex, tiley);
/*      */       }
/*      */     }
/*  659 */     else if (type == Tiles.Tile.TILE_TUNDRA.id) {
/*      */       
/*  661 */       if (Tiles.decodeHeight(tile) > 0) {
/*      */         
/*  663 */         if (Server.rand.nextInt(7) == 1)
/*  664 */           checkForSeedGrowth(tile, tilex, tiley); 
/*  665 */         if (Server.rand.nextInt(100) == 1) {
/*  666 */           checkForLingonberryStart(tile, tilex, tiley);
/*      */         }
/*      */       } 
/*  669 */     } else if (type == Tiles.Tile.TILE_MARSH.id) {
/*      */       
/*  671 */       if (Server.rand.nextInt(9) == 1 && Tiles.decodeHeight(tile) > -20)
/*      */       {
/*  673 */         checkForSeedGrowth(tile, tilex, tiley);
/*      */       }
/*      */     }
/*  676 */     else if (theTile.isNormalTree() || theTile.isEnchantedTree()) {
/*      */       
/*  678 */       checkForTreeGrowth(tile, tilex, tiley, type, aData);
/*  679 */       if (Server.rand.nextInt(10) < 1) {
/*      */         
/*  681 */         checkForMycelGrowth(tile, tilex, tiley, type, aData);
/*      */       }
/*      */       else {
/*      */         
/*  685 */         checkForSeedGrowth(tile, tilex, tiley);
/*  686 */         checkForTreeGrassGrowth(tile, tilex, tiley, type, aData);
/*      */       } 
/*      */ 
/*      */       
/*  690 */       if (Server.isCheckHive(tilex, tiley) && WurmCalendar.isSeasonAutumn() && Server.rand.nextInt(30) == 0) {
/*      */         
/*  692 */         Item wildHive = Zones.getWildHive(tilex, tiley);
/*  693 */         if (wildHive != null) {
/*      */           
/*  695 */           Item honey = wildHive.getHoney();
/*  696 */           if (honey == null) {
/*      */             
/*  698 */             Server.setCheckHive(tilex, tiley, false);
/*  699 */             if (Zones.removeWildHive(tilex, tiley) && Servers.isThisATestServer()) {
/*  700 */               Players.getInstance().sendGmMessage(null, "System", "Debug: Removed wild hive due to no food in autumn @ " + tilex + "," + tiley, false);
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*  705 */       if (Server.isCheckHive(tilex, tiley) && WurmCalendar.isWinter()) {
/*      */         
/*  707 */         Server.setCheckHive(tilex, tiley, false);
/*  708 */         if (Zones.removeWildHive(tilex, tiley) && Servers.isThisATestServer()) {
/*  709 */           Players.getInstance().sendGmMessage(null, "System", "Debug: Removed wild hive due to winter @ " + tilex + "," + tiley, false);
/*      */         }
/*      */       } 
/*  712 */       if (!Server.isCheckHive(tilex, tiley) && WurmCalendar.isSpring() && Server.rand.nextInt(5) == 0) {
/*      */         
/*  714 */         Server.setCheckHive(tilex, tiley, true);
/*  715 */         addWildBeeHives(tilex, tiley, theTile, aData);
/*      */       } 
/*      */       
/*  718 */       if (!Server.isCheckHive(tilex, tiley) && WurmCalendar.isSummer() && Server.rand.nextInt(15) == 0)
/*      */       {
/*  720 */         Server.setCheckHive(tilex, tiley, true);
/*  721 */         addWildBeeHives(tilex, tiley, theTile, aData);
/*      */       }
/*      */     
/*  724 */     } else if (theTile.isNormalBush() || theTile.isEnchantedBush()) {
/*      */       
/*  726 */       checkForTreeGrowth(tile, tilex, tiley, type, aData);
/*  727 */       if (Server.rand.nextInt(10) < 1)
/*      */       {
/*  729 */         checkForMycelGrowth(tile, tilex, tiley, type, aData);
/*      */       }
/*      */       else
/*      */       {
/*  733 */         checkForSeedGrowth(tile, tilex, tiley);
/*  734 */         checkForTreeGrassGrowth(tile, tilex, tiley, type, aData);
/*      */       }
/*      */     
/*  737 */     } else if (type == Tiles.Tile.TILE_FIELD.id || type == Tiles.Tile.TILE_FIELD2.id) {
/*      */ 
/*      */       
/*  740 */       if (!Features.Feature.CROP_POLLER.isEnabled() && !Zones.protectedTiles[tilex][tiley])
/*      */       {
/*  742 */         checkForFarmGrowth(tile, tilex, tiley, type, aData);
/*      */       }
/*      */     }
/*  745 */     else if (type == Tiles.Tile.TILE_MYCELIUM.id) {
/*      */       
/*  747 */       if (!Servers.isThisAPvpServer())
/*      */       {
/*      */ 
/*      */         
/*  751 */         Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), Tiles.Tile.TILE_GRASS.id, aData);
/*  752 */         Players.getInstance().sendChangedTile(tilex, tiley, true, false);
/*      */       }
/*  754 */       else if (Server.rand.nextInt(3) < 1)
/*      */       {
/*  756 */         checkForGrassSpread(tile, tilex, tiley, type, aData);
/*  757 */         checkForSeedGrowth(tile, tilex, tiley);
/*      */       }
/*      */       else
/*      */       {
/*  761 */         checkForGrassGrowth(tile, tilex, tiley, type, aData, false);
/*  762 */         checkGrubGrowth(tile, tilex, tiley);
/*      */       }
/*      */     
/*  765 */     } else if (type == Tiles.Tile.TILE_MYCELIUM_LAWN.id) {
/*      */       
/*  767 */       if (!Servers.isThisAPvpServer()) {
/*      */ 
/*      */         
/*  770 */         Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), Tiles.Tile.TILE_LAWN.id, aData);
/*  771 */         Players.getInstance().sendChangedTile(tilex, tiley, true, false);
/*      */       }
/*  773 */       else if (Server.rand.nextInt(25) == 1) {
/*      */ 
/*      */         
/*  776 */         Village v = Villages.getVillage(tilex, tiley, pollingSurface);
/*  777 */         if (v == null && Server.rand.nextInt(100) > 75)
/*      */         {
/*      */           
/*  780 */           currentMesh.setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_MYCELIUM.id, (byte)0));
/*  781 */           Server.modifyFlagsByTileType(tilex, tiley, Tiles.Tile.TILE_MYCELIUM.id);
/*  782 */           Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/*      */         }
/*      */       
/*  785 */       } else if (Server.rand.nextInt(10) < 1) {
/*      */         
/*  787 */         checkForGrassSpread(tile, tilex, tiley, type, aData);
/*      */       }
/*      */     
/*  790 */     } else if (theTile.isMyceliumTree() || theTile.isMyceliumBush()) {
/*      */       
/*  792 */       if (!Servers.isThisAPvpServer()) {
/*      */ 
/*      */ 
/*      */         
/*  796 */         byte newType = (byte)Tiles.toNormal(type);
/*  797 */         currentMesh.setTile(tilex, tiley, 
/*  798 */             Tiles.encode(Tiles.decodeHeight(tile), newType, aData));
/*  799 */         Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/*      */       }
/*      */       else {
/*      */         
/*  803 */         checkForTreeGrowth(tile, tilex, tiley, type, aData);
/*  804 */         if (Server.rand.nextInt(3) < 1) {
/*  805 */           checkForGrassSpread(tile, tilex, tiley, type, aData);
/*      */         } else {
/*      */           
/*  808 */           checkForGrassGrowth(tile, tilex, tiley, type, aData, false);
/*  809 */           checkForSeedGrowth(tile, tilex, tiley);
/*      */         }
/*      */       
/*      */       } 
/*  813 */     } else if (type == Tiles.Tile.TILE_LAVA.id) {
/*      */       
/*  815 */       checkForLavaFlow(tile, tilex, tiley, type, aData);
/*      */     }
/*  817 */     else if (type == Tiles.Tile.TILE_PLANKS.id) {
/*      */       
/*  819 */       if (!Zones.walkedTiles[tilex][tiley]) {
/*      */         
/*  821 */         if (aData == 0 || Server.rand.nextInt(10) == 0) {
/*  822 */           checkForDecayToDirt(tile, tilex, tiley, type, aData);
/*      */         }
/*      */       } else {
/*  825 */         Zones.walkedTiles[tilex][tiley] = false;
/*      */       }
/*      */     
/*  828 */     } else if (pollingSurface && (type == Tiles.Tile.TILE_ROCK.id || type == Tiles.Tile.TILE_CLIFF.id)) {
/*      */       
/*  830 */       if (pollEruption) {
/*  831 */         checkForEruption(tile, tilex, tiley, type, aData);
/*      */       } else {
/*  833 */         checkCreateIronRock(tilex, tiley);
/*      */       }
/*      */     
/*  836 */     } else if (Tiles.isMineDoor(type)) {
/*      */       
/*  838 */       decayMineDoor(tile, tilex, tiley);
/*      */     } 
/*      */     
/*  841 */     if (Tiles.isRoadType(type) || Tiles.isEnchanted(type) || type == Tiles.Tile.TILE_DIRT_PACKED.id || type == Tiles.Tile.TILE_DIRT.id || type == Tiles.Tile.TILE_LAWN.id || type == Tiles.Tile.TILE_MYCELIUM_LAWN.id)
/*      */     {
/*      */ 
/*      */       
/*  845 */       checkInvestigateGrowth(tile, tilex, tiley);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void decayMineDoor(int tile, int tilex, int tiley) {
/*  875 */     if (pollingSurface && Server.rand.nextInt(3) == 0) {
/*      */       
/*  877 */       Village v = Villages.getVillage(tilex, tiley, true);
/*  878 */       if (v == null || v.lessThanWeekLeft()) {
/*      */         
/*  880 */         int currQl = Server.getWorldResource(tilex, tiley);
/*  881 */         currQl = Math.max(0, currQl - 100);
/*  882 */         Server.setWorldResource(tilex, tiley, currQl);
/*  883 */         if (currQl == 0) {
/*      */           
/*  885 */           Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), Tiles.Tile.TILE_HOLE.id, (byte)0);
/*  886 */           Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, true);
/*      */           
/*  888 */           MineDoorPermission perm = MineDoorPermission.getPermission(tilex, tiley);
/*  889 */           if (perm != null) {
/*  890 */             MineDoorPermission.removePermission(perm);
/*      */           }
/*  892 */           MineDoorPermission.deleteMineDoor(tilex, tiley);
/*  893 */           Server.getInstance().broadCastMessage("A mine door crumbles and falls down with a crash.", tilex, tiley, true, 5);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void checkPolltile() {
/*  903 */     if (currentPollTile == 0) {
/*      */       
/*  905 */       calcRest();
/*  906 */       WurmCalendar.checkSpring();
/*      */ 
/*      */       
/*  909 */       createFlowers = (Server.rand.nextInt(5) == 0);
/*      */ 
/*      */       
/*  912 */       pollround++;
/*  913 */       pollModifier = (Server.rand.nextInt(Math.min(30000, currentMesh.data.length)) + 1) * 2 - 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  921 */       logger.log(Level.INFO, "New pollModifier: " + pollModifier + " eruptions=" + pollEruption);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  928 */     currentPollTile = currentPollTile + pollModifier & mask;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean checkForGrassGrowth(int tile, int tilex, int tiley, byte type, byte aData, boolean andFlowers) {
/*      */     int growthRate;
/*  961 */     GrassData.GrowthStage growthStage = GrassData.GrowthStage.decodeTileData(aData);
/*  962 */     GrassData.FlowerType flowerType = GrassData.FlowerType.decodeTileData(aData);
/*      */     
/*  964 */     boolean dataHasChanged = false;
/*  965 */     short height = Tiles.decodeHeight(tile);
/*  966 */     int seed = 100;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  971 */     if (WurmCalendar.isWinter()) {
/*  972 */       growthRate = 15;
/*  973 */     } else if (WurmCalendar.isSummer()) {
/*  974 */       growthRate = 40;
/*  975 */     } else if (WurmCalendar.isAutumn()) {
/*  976 */       growthRate = 20;
/*      */     } else {
/*      */       
/*  979 */       growthRate = 30;
/*      */     } 
/*  981 */     if (type == Tiles.Tile.TILE_MYCELIUM.id) {
/*  982 */       growthRate = 50 - growthRate;
/*      */     }
/*  984 */     int rnd = Server.rand.nextInt(100);
/*      */     
/*  986 */     if (growthRate >= rnd)
/*      */     {
/*  988 */       if (!growthStage.isMax()) {
/*      */         
/*  990 */         growthStage = growthStage.getNextStage();
/*  991 */         dataHasChanged = true;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  996 */     if (andFlowers && createFlowers) {
/*      */       
/*  998 */       GrassData.FlowerType newFlowerType = Terraforming.getRandomFlower(flowerType, false);
/*  999 */       if (flowerType != newFlowerType) {
/*      */         
/* 1001 */         flowerType = newFlowerType;
/* 1002 */         dataHasChanged = true;
/*      */       } 
/*      */     } 
/*      */     
/* 1006 */     if (dataHasChanged) {
/*      */       
/* 1008 */       if (logger.isLoggable(Level.FINER))
/*      */       {
/* 1010 */         logger.log(Level.FINER, "tile [" + tilex + "," + tiley + "] changed: " + height + " type=" + 
/* 1011 */             Tiles.getTile(type).getName() + " stage=" + growthStage
/*      */             
/* 1013 */             .toString() + " flower=" + flowerType.toString().toLowerCase() + ".");
/*      */       }
/* 1015 */       byte tileData = GrassData.encodeGrassTileData(growthStage, flowerType);
/* 1016 */       currentMesh.setTile(tilex, tiley, Tiles.encode(height, type, tileData));
/* 1017 */       Server.modifyFlagsByTileType(tilex, tiley, type);
/* 1018 */       Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/* 1019 */       return true;
/*      */     } 
/* 1021 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean checkForTreeGrassGrowth(int tile, int tilex, int tiley, byte type, byte aData) {
/*      */     int growthRate;
/* 1042 */     GrassData.GrowthTreeStage growthStage = GrassData.GrowthTreeStage.decodeTileData(aData);
/*      */     
/* 1044 */     boolean dataHasChanged = false;
/* 1045 */     short height = Tiles.decodeHeight(tile);
/* 1046 */     int seed = 100;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1051 */     if (WurmCalendar.isWinter()) {
/* 1052 */       growthRate = 5;
/* 1053 */     } else if (WurmCalendar.isSummer()) {
/* 1054 */       growthRate = 15;
/* 1055 */     } else if (WurmCalendar.isAutumn()) {
/* 1056 */       growthRate = 10;
/*      */     } else {
/*      */       
/* 1059 */       growthRate = 20;
/*      */     } 
/* 1061 */     if (Tiles.getTile(type).isMycelium()) {
/* 1062 */       growthRate = 25 - growthRate;
/*      */     }
/* 1064 */     int rnd = Server.rand.nextInt(100);
/*      */     
/* 1066 */     if (growthRate >= rnd)
/*      */     {
/* 1068 */       if (!growthStage.isMax())
/*      */       {
/*      */         
/* 1071 */         if (growthStage == GrassData.GrowthTreeStage.LAWN) {
/*      */ 
/*      */           
/* 1074 */           Village v = Villages.getVillage(tilex, tiley, true);
/* 1075 */           if (v == null && Server.rand.nextInt(100) > 75)
/*      */           {
/* 1077 */             growthStage = GrassData.GrowthTreeStage.SHORT;
/* 1078 */             dataHasChanged = true;
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1083 */           growthStage = growthStage.getNextStage();
/* 1084 */           dataHasChanged = true;
/*      */         } 
/*      */       }
/*      */     }
/*      */     
/* 1089 */     if (dataHasChanged) {
/*      */       
/* 1091 */       if (logger.isLoggable(Level.FINER))
/*      */       {
/* 1093 */         logger.log(Level.FINER, "tile [" + tilex + "," + tiley + "] changed: " + height + " type=" + 
/* 1094 */             Tiles.getTile(type).getName() + " stage=" + growthStage
/* 1095 */             .toString() + ".");
/*      */       }
/* 1097 */       FoliageAge tage = FoliageAge.getFoliageAge(aData);
/* 1098 */       boolean hasFruit = TreeData.hasFruit(aData);
/* 1099 */       boolean incentre = TreeData.isCentre(aData);
/*      */       
/* 1101 */       byte tileData = Tiles.encodeTreeData(tage, hasFruit, incentre, growthStage);
/* 1102 */       currentMesh.setTile(tilex, tiley, Tiles.encode(height, type, tileData));
/* 1103 */       Server.modifyFlagsByTileType(tilex, tiley, type);
/* 1104 */       Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/* 1105 */       return true;
/*      */     } 
/* 1107 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean checkForSeedGrowth(int tile, int tilex, int tiley) {
/* 1127 */     byte type = Tiles.decodeType(tile);
/* 1128 */     Tiles.Tile theTile = Tiles.getTile(type);
/*      */ 
/*      */     
/* 1131 */     r.setSeed((tilex + tiley * Zones.worldTileSizeY) * 102563L);
/*      */ 
/*      */     
/* 1134 */     boolean canHaveHerb = (r.nextInt(herbChance) == 0 && theTile.canBotanize());
/*      */     
/* 1136 */     r.setSeed((tilex + tiley * Zones.worldTileSizeY) * 101531L);
/*      */ 
/*      */     
/* 1139 */     boolean canHaveForage = (r.nextInt(forageChance) == 0 && theTile.canForage());
/*      */     
/* 1141 */     checkInvestigateGrowth(tile, tilex, tiley);
/*      */     
/* 1143 */     if (containsStructure(tilex, tiley)) {
/* 1144 */       return false;
/*      */     }
/* 1146 */     if (canHaveForage || canHaveHerb) {
/*      */       
/* 1148 */       boolean containsForage = Server.isForagable(tilex, tiley);
/* 1149 */       boolean containsHerb = Server.isBotanizable(tilex, tiley);
/* 1150 */       boolean changed = false;
/*      */ 
/*      */       
/* 1153 */       if (canHaveForage && !containsForage) {
/*      */         
/* 1155 */         changed = true;
/* 1156 */         containsForage = true;
/*      */       } 
/* 1158 */       if (canHaveHerb && !containsHerb) {
/*      */         
/* 1160 */         changed = true;
/* 1161 */         containsHerb = true;
/*      */       } 
/* 1163 */       if (changed) {
/*      */         
/* 1165 */         setGrassHasSeeds(tilex, tiley, containsForage, containsHerb);
/* 1166 */         return true;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1173 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static void checkInvestigateGrowth(int tile, int tilex, int tiley) {
/* 1178 */     r.setSeed((tilex + tiley * Zones.worldTileSizeY) * 104149L);
/* 1179 */     boolean canHaveInvestigate = (r.nextInt(investigateChance) == 0);
/*      */     
/* 1181 */     if (containsStructure(tilex, tiley)) {
/*      */       return;
/*      */     }
/* 1184 */     if (canHaveInvestigate)
/*      */     {
/* 1186 */       if (!Server.isInvestigatable(tilex, tiley)) {
/* 1187 */         Server.setInvestigatable(tilex, tiley, true);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static void checkGrubGrowth(int tile, int tilex, int tiley) {
/* 1194 */     r.setSeed((tilex + tiley * Zones.worldTileSizeY) * 103025L);
/* 1195 */     boolean canHaveGrub = (r.nextInt(searchChance) == 0);
/*      */     
/* 1197 */     if (containsStructure(tilex, tiley)) {
/*      */       return;
/*      */     }
/* 1200 */     if (canHaveGrub)
/*      */     {
/* 1202 */       Server.setGrubs(tilex, tiley, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setGrassHasSeeds(int tilex, int tiley, boolean forageable, boolean botanizeable) {
/* 1208 */     if (logger.isLoggable(Level.FINEST)) {
/*      */       
/* 1210 */       if (forageable)
/* 1211 */         logger.finest(tilex + ", " + tiley + " setting forageable."); 
/* 1212 */       if (botanizeable)
/* 1213 */         logger.finest(tilex + ", " + tiley + " setting botanizable."); 
/*      */     } 
/* 1215 */     Server.setForagable(tilex, tiley, forageable);
/* 1216 */     Server.setBotanizable(tilex, tiley, botanizeable);
/* 1217 */     Players.getInstance().sendChangedTile(tilex, tiley, true, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void checkForFarmGrowth(int tile, int tilex, int tiley, byte type, byte aData) {
/* 1222 */     int tileAge = Crops.decodeFieldAge(aData);
/* 1223 */     int crop = Crops.getCropNumber(type, aData);
/* 1224 */     boolean farmed = Crops.decodeFieldState(aData);
/* 1225 */     if (logTilePolling) {
/* 1226 */       logger.log(Level.INFO, "Polling farm at " + tilex + "," + tiley + ", age=" + tileAge + ", crop=" + crop + ", farmed=" + farmed);
/*      */     }
/* 1228 */     if (tileAge == 7) {
/*      */       
/* 1230 */       if (Server.rand.nextInt(100) <= 10) {
/*      */         
/* 1232 */         currentMesh.setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_DIRT.id, (byte)0));
/* 1233 */         Server.modifyFlagsByTileType(tilex, tiley, Tiles.Tile.TILE_DIRT.id);
/* 1234 */         Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/* 1235 */         if (logTilePolling)
/* 1236 */           logger.log(Level.INFO, "Set to dirt"); 
/*      */       } 
/* 1238 */       if (WurmCalendar.isNight())
/*      */       {
/* 1240 */         SoundPlayer.playSound("sound.birdsong.bird1", tilex, tiley, pollingSurface, 2.0F);
/*      */       }
/*      */       else
/*      */       {
/* 1244 */         SoundPlayer.playSound("sound.birdsong.bird3", tilex, tiley, pollingSurface, 2.0F);
/*      */       }
/*      */     
/* 1247 */     } else if (tileAge < 7) {
/*      */       
/* 1249 */       if ((tileAge == 5 || tileAge == 6) && 
/* 1250 */         Server.rand.nextInt(tileAge) != 0)
/*      */         return; 
/* 1252 */       tileAge++;
/* 1253 */       VolaTile tempvtile1 = Zones.getOrCreateTile(tilex, tiley, pollingSurface);
/* 1254 */       if (tempvtile1.getStructure() != null) {
/*      */         
/* 1256 */         currentMesh.setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_DIRT.id, (byte)0));
/* 1257 */         Server.modifyFlagsByTileType(tilex, tiley, Tiles.Tile.TILE_DIRT.id);
/*      */       }
/*      */       else {
/*      */         
/* 1261 */         currentMesh.setTile(tilex, tiley, 
/* 1262 */             Tiles.encode(Tiles.decodeHeight(tile), type, Crops.encodeFieldData(false, tileAge, crop)));
/* 1263 */         Server.modifyFlagsByTileType(tilex, tiley, type);
/* 1264 */         if (logTilePolling)
/* 1265 */           logger.log(Level.INFO, "Changed the tile"); 
/*      */       } 
/* 1267 */       if (WurmCalendar.isNight()) {
/*      */         
/* 1269 */         SoundPlayer.playSound("sound.ambient.night.crickets", tilex, tiley, pollingSurface, 0.0F);
/*      */       }
/*      */       else {
/*      */         
/* 1273 */         SoundPlayer.playSound("sound.birdsong.bird2", tilex, tiley, pollingSurface, 1.0F);
/*      */       } 
/* 1275 */       Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/*      */     } else {
/*      */       
/* 1278 */       logger.log(Level.WARNING, "Strange, tile " + tilex + ", " + tiley + " is field but has age above 7:" + tileAge + " crop is " + crop + " farmed is " + farmed);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean containsStructure(int tilex, int tiley) {
/*      */     try {
/* 1286 */       Zone zone = Zones.getZone(tilex, tiley, pollingSurface);
/* 1287 */       VolaTile tempvtile1 = zone.getTileOrNull(tilex, tiley);
/*      */       
/* 1289 */       if (tempvtile1 != null) {
/*      */         
/* 1291 */         if (containsHouse(tempvtile1)) {
/* 1292 */           return true;
/*      */         }
/* 1294 */         if (containsFences(tempvtile1)) {
/* 1295 */           return true;
/*      */         }
/*      */       } 
/* 1298 */     } catch (NoSuchZoneException nsz) {
/*      */       
/* 1300 */       logger.log(Level.WARNING, "Weird, no zone for " + tilex + ", " + tiley + " surfaced=" + pollingSurface, (Throwable)nsz);
/*      */     } 
/* 1302 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean containsFences(VolaTile vtile) {
/* 1307 */     if (vtile == null) {
/* 1308 */       return false;
/*      */     }
/*      */     
/* 1311 */     if ((vtile.getFences()).length > 0) {
/* 1312 */       return true;
/*      */     }
/* 1314 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean containsHouse(VolaTile vtile) {
/* 1319 */     if (vtile == null) {
/* 1320 */       return false;
/*      */     }
/*      */     
/* 1323 */     if (vtile.getStructure() != null) {
/* 1324 */       return true;
/*      */     }
/* 1326 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean containsTracks(int tilex, int tiley) {
/*      */     try {
/* 1333 */       Zone zone = Zones.getZone(tilex, tiley, pollingSurface);
/* 1334 */       Track[] tracks = zone.getTracksFor(tilex, tiley);
/* 1335 */       if (tracks.length > 0) {
/* 1336 */         return true;
/*      */       }
/* 1338 */     } catch (NoSuchZoneException nsz) {
/*      */       
/* 1340 */       logger.log(Level.WARNING, "Weird, no zone for " + tilex + ", " + tiley + " surfaced=" + pollingSurface, (Throwable)nsz);
/*      */     } 
/* 1342 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void checkForEruption(int tile, int tilex, int tiley, byte type, byte aData) {
/* 1347 */     if (pollingSurface && pollEruption && Tiles.decodeHeight(tile) > 100) {
/*      */       
/* 1349 */       for (int xx = -1; xx <= 1; xx++) {
/*      */         
/* 1351 */         for (int yy = -1; yy <= 1; yy++) {
/*      */           
/* 1353 */           if (tilex + xx >= 0 && tiley + yy >= 0 && tilex + xx < 1 << Constants.meshSize && tiley + yy < 1 << Constants.meshSize) {
/*      */ 
/*      */             
/* 1356 */             int temptile1 = Server.surfaceMesh.getTile(tilex + xx, tiley + yy);
/* 1357 */             byte tempbyte1 = Tiles.decodeType(temptile1);
/* 1358 */             if (tempbyte1 != Tiles.Tile.TILE_LAVA.id && tempbyte1 != Tiles.Tile.TILE_HOLE.id && 
/* 1359 */               !Tiles.isMineDoor(tempbyte1)) {
/*      */               
/* 1361 */               int temptile2 = Server.caveMesh.getTile(tilex + xx, tiley + yy);
/* 1362 */               byte tempbyte2 = Tiles.decodeType(temptile2);
/* 1363 */               if (Tiles.isSolidCave(tempbyte2)) {
/*      */                 
/* 1365 */                 int tempint1 = Tiles.decodeHeight(temptile1);
/*      */                 
/* 1367 */                 tempint1 += 4;
/* 1368 */                 int tempint2 = Tiles.encode((short)tempint1, Tiles.Tile.TILE_LAVA.id, (byte)0);
/* 1369 */                 for (int xn = 0; xn <= 1; xn++) {
/*      */                   
/* 1371 */                   for (int yn = 0; yn <= 1; yn++) {
/*      */ 
/*      */                     
/*      */                     try {
/* 1375 */                       int tempint3 = Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex + xx + xn, tiley + yy + yn));
/*      */                       
/* 1377 */                       Server.rockMesh.setTile(tilex + xx + xn, tiley + yy + yn, 
/* 1378 */                           Tiles.encode((short)tempint3, Tiles.Tile.TILE_ROCK.id, (byte)0));
/*      */                     }
/* 1380 */                     catch (Exception exception) {}
/*      */                   } 
/*      */                 } 
/*      */ 
/*      */ 
/*      */                 
/* 1386 */                 Server.surfaceMesh.setTile(tilex + xx, tiley + yy, tempint2);
/* 1387 */                 Server.modifyFlagsByTileType(tilex + xx, tiley + yy, Tiles.Tile.TILE_LAVA.id);
/*      */                 
/* 1389 */                 Server.caveMesh.setTile(tilex + xx, tiley + yy, 
/* 1390 */                     Tiles.encode(Tiles.decodeHeight(temptile2), Tiles.Tile.TILE_CAVE_WALL_LAVA.id, 
/* 1391 */                       Tiles.decodeData(temptile2)));
/* 1392 */                 Players.getInstance().sendChangedTile(tilex + xx, tiley + yy, false, true);
/* 1393 */                 Players.getInstance().sendChangedTile(tilex + xx, tiley + yy, pollingSurface, true);
/* 1394 */                 pollEruption = false;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 1400 */       if (!pollEruption)
/*      */       {
/* 1402 */         logger.log(Level.INFO, "Eruption at " + tilex + ", " + tiley + "!");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean checkCreateIronRock(int tilex, int tiley) {
/* 1409 */     r.setSeed((tilex + tiley * Zones.worldTileSizeY) * 103591L);
/* 1410 */     boolean canHaveRock = (r.nextInt(forageChance) == 0);
/*      */     
/* 1412 */     if (canHaveRock) {
/*      */       
/* 1414 */       if (containsStructure(tilex, tiley)) {
/* 1415 */         return false;
/*      */       }
/* 1417 */       boolean containsRock = Server.isForagable(tilex, tiley);
/*      */       
/* 1419 */       if (!containsRock) {
/*      */         
/* 1421 */         setGrassHasSeeds(tilex, tiley, true, false);
/* 1422 */         return true;
/*      */       } 
/*      */     } 
/* 1425 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void checkForLavaFlow(int tile, int tilex, int tiley, byte type, byte aData) {
/* 1440 */     if (Server.rand.nextInt(30) == 0) {
/*      */ 
/*      */       
/* 1443 */       if ((Tiles.decodeData(tile) & 0xFF) != 255)
/*      */       {
/* 1445 */         int tempint2 = Tiles.decodeHeight(tile);
/* 1446 */         int temptile1 = Tiles.encode((short)tempint2, Tiles.Tile.TILE_ROCK.id, (byte)0);
/* 1447 */         checkCreateIronRock(tilex, tiley);
/* 1448 */         currentMesh.setTile(tilex, tiley, temptile1);
/*      */         
/* 1450 */         for (int xx = 0; xx <= 1; xx++) {
/*      */           
/* 1452 */           for (int yy = 0; yy <= 1; yy++) {
/*      */ 
/*      */             
/*      */             try {
/* 1456 */               int tempint3 = Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex + xx, tiley + yy));
/* 1457 */               Server.rockMesh.setTile(tilex + xx, tiley + yy, 
/* 1458 */                   Tiles.encode((short)tempint3, Tiles.Tile.TILE_ROCK.id, (byte)0));
/*      */             }
/* 1460 */             catch (Exception exception) {}
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1466 */         Terraforming.caveIn(tilex, tiley);
/*      */       }
/*      */     
/* 1469 */     } else if (Server.rand.nextInt(40) == 0) {
/*      */       
/* 1471 */       boolean foundHole = false;
/* 1472 */       if (Tiles.decodeHeight(tile) > 0 && pollingSurface) {
/*      */         
/* 1474 */         int currHeight = Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex, tiley));
/* 1475 */         for (int xx = -1; xx <= 1; xx++) {
/*      */           
/* 1477 */           for (int yy = -1; yy <= 1; yy++) {
/*      */ 
/*      */             
/* 1480 */             if ((xx == 0 && yy != 0) || (yy == 0 && xx != 0)) {
/*      */               
/* 1482 */               if (foundHole) {
/*      */                 break;
/*      */               }
/* 1485 */               if (tilex + xx >= 0 && tiley + yy >= 0 && tilex + xx < 1 << Constants.meshSize && tiley + yy < 1 << Constants.meshSize)
/*      */               {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 1493 */                 int tempint1 = Tiles.decodeHeight(currentMesh.getTile(tilex + xx, tiley + yy));
/* 1494 */                 if (tempint1 < currHeight)
/*      */                 {
/* 1496 */                   int t = currentMesh.getTile(tilex + xx, tiley + yy);
/* 1497 */                   byte type2 = Tiles.decodeType(t);
/* 1498 */                   if (Server.rand.nextInt(2) == 0)
/*      */                   {
/* 1500 */                     if (type2 != Tiles.Tile.TILE_LAVA.id && type2 != Tiles.Tile.TILE_HOLE.id && 
/* 1501 */                       !Tiles.isMineDoor(type2))
/*      */                     {
/*      */ 
/*      */ 
/*      */ 
/*      */                       
/* 1507 */                       int tempint2 = tempint1 + 4;
/* 1508 */                       int temptile1 = Tiles.encode((short)tempint2, Tiles.Tile.TILE_LAVA.id, (byte)0);
/*      */                       
/* 1510 */                       for (int xn = 0; xn <= 1; xn++) {
/*      */                         
/* 1512 */                         for (int yn = 0; yn <= 1; yn++) {
/*      */ 
/*      */                           
/*      */                           try {
/* 1516 */                             int tempint3 = Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex + xx + xn, tiley + yy + yn));
/*      */ 
/*      */                             
/* 1519 */                             Server.rockMesh.setTile(tilex + xx + xn, tiley + yy + yn, 
/* 1520 */                                 Tiles.encode((short)tempint3, Tiles.Tile.TILE_ROCK.id, (byte)0));
/*      */                           }
/* 1522 */                           catch (Exception exception) {}
/*      */                         } 
/*      */                       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                       
/* 1534 */                       currentMesh.setTile(tilex + xx, tiley + yy, temptile1);
/* 1535 */                       Server.modifyFlagsByTileType(tilex + xx, tiley + yy, Tiles.Tile.TILE_LAVA.id);
/* 1536 */                       Players.getInstance().sendChangedTile(tilex + xx, tiley + yy, pollingSurface, true);
/*      */                     }
/*      */                   
/*      */                   }
/*      */                 }
/* 1541 */                 else if ((Tiles.decodeData(tile) & 0xFF) == 255)
/*      */                 {
/* 1543 */                   int t = currentMesh.getTile(tilex + xx, tiley + yy);
/* 1544 */                   byte type2 = Tiles.decodeType(t);
/* 1545 */                   if (type2 == Tiles.Tile.TILE_ROCK.id)
/*      */                   {
/* 1547 */                     int temptile2 = Server.caveMesh.getTile(tilex + xx, tiley + yy);
/* 1548 */                     byte tempbyte2 = Tiles.decodeType(temptile2);
/* 1549 */                     if (Tiles.isSolidCave(tempbyte2))
/*      */                     {
/* 1551 */                       int tempint2 = tempint1 + 4;
/* 1552 */                       int temptile1 = Tiles.encode((short)tempint2, Tiles.Tile.TILE_LAVA.id, (byte)0);
/*      */                       
/* 1554 */                       currentMesh.setTile(tilex + xx, tiley + yy, temptile1);
/* 1555 */                       Server.rockMesh.setTile(tilex + xx, tiley + yy, 
/* 1556 */                           Tiles.encode((short)tempint2, Tiles.Tile.TILE_ROCK.id, (byte)0));
/*      */ 
/*      */ 
/*      */                       
/* 1560 */                       Server.caveMesh.setTile(tilex + xx, tiley + yy, 
/*      */ 
/*      */                           
/* 1563 */                           Tiles.encode(Tiles.decodeHeight(temptile2), Tiles.Tile.TILE_CAVE_WALL_LAVA.id, 
/* 1564 */                             Tiles.decodeData(Server.caveMesh.getTile(tilex + yy, tiley + yy))));
/* 1565 */                       Players.getInstance().sendChangedTile(tilex + xx, tiley + yy, false, true);
/* 1566 */                       Players.getInstance().sendChangedTile(tilex + xx, tiley + yy, true, true);
/*      */                     }
/*      */                   
/*      */                   }
/*      */                 
/*      */                 }
/*      */               
/*      */               }
/*      */             
/*      */             }
/* 1576 */             else if (tilex + xx >= 0 && tiley + yy >= 0 && tilex + xx < 1 << Constants.meshSize && tiley + yy < 1 << Constants.meshSize) {
/*      */ 
/*      */               
/* 1579 */               int t = currentMesh.getTile(tilex + xx, tiley + yy);
/* 1580 */               byte type2 = Tiles.decodeType(t);
/* 1581 */               if (type2 == Tiles.Tile.TILE_HOLE.id || Tiles.isMineDoor(type2)) {
/*      */                 
/* 1583 */                 foundHole = true;
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean checkForGrassSpread(int tile, int tilex, int tiley, byte type, byte aData) {
/* 1597 */     short theight = Tiles.decodeHeight(tile);
/* 1598 */     byte tileData = 0;
/* 1599 */     boolean isATree = (Tiles.isTree(type) || Tiles.isBush(type));
/* 1600 */     if (isATree)
/* 1601 */       tileData = aData; 
/* 1602 */     if (theight > MAX_KELP_HEIGHT && pollingSurface) {
/*      */       
/* 1604 */       if (theight < 0) {
/*      */         
/* 1606 */         kelpRandom.setSeed((Servers.localServer.id * 25000 + tilex / 12 * tiley / 12));
/* 1607 */         if (kelpRandom.nextInt(20) == 0) {
/*      */           
/* 1609 */           kelpRandom.setSeed((tilex * tiley));
/* 1610 */           if (kelpRandom.nextBoolean())
/*      */           {
/* 1612 */             if (theight <= MINIMUM_REED_HEIGHT)
/*      */             {
/* 1614 */               byte newType = Tiles.Tile.TILE_REED.id;
/* 1615 */               if (theight < MINIMUM_KELP_HEIGHT)
/*      */               {
/* 1617 */                 newType = Tiles.Tile.TILE_KELP.id;
/*      */               }
/* 1619 */               currentMesh.setTile(tilex, tiley, Tiles.encode(theight, newType, (byte)0));
/* 1620 */               Server.modifyFlagsByTileType(tilex, tiley, newType);
/* 1621 */               Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/*      */               
/* 1623 */               return true;
/*      */             }
/*      */           
/*      */           }
/* 1627 */         } else if (Server.rand.nextInt(10) == 1) {
/*      */           
/* 1629 */           if (Tiles.isMycelium(type)) {
/*      */             
/* 1631 */             if (Kingdoms.getKingdomTemplateFor(Zones.getKingdom(tilex, tiley)) == 3) {
/* 1632 */               return false;
/*      */             }
/* 1634 */             byte newType = (byte)Tiles.toNormal(type);
/*      */             
/* 1636 */             if (newType == Tiles.Tile.TILE_GRASS.id) {
/*      */               
/* 1638 */               newType = Tiles.Tile.TILE_DIRT.id;
/* 1639 */               if (Server.rand.nextInt(7) == 1)
/*      */               {
/* 1641 */                 if (theight < MINIMUM_KELP_HEIGHT) {
/* 1642 */                   newType = Tiles.Tile.TILE_KELP.id;
/*      */                 } else {
/* 1644 */                   newType = Tiles.Tile.TILE_REED.id;
/*      */                 } 
/*      */               }
/*      */             } 
/* 1648 */             currentMesh.setTile(tilex, tiley, Tiles.encode(theight, newType, tileData));
/* 1649 */             Server.modifyFlagsByTileType(tilex, tiley, newType);
/* 1650 */             Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/*      */           } 
/*      */         } 
/* 1653 */         return false;
/*      */       } 
/*      */       
/* 1656 */       boolean checkMycel = (Servers.isThisAPvpServer() && Kingdoms.getKingdomTemplateFor(Zones.getKingdom(tilex, tiley)) == 3);
/*      */       
/* 1658 */       if (Tiles.isMycelium(type))
/*      */       {
/* 1660 */         if (checkMycel) {
/* 1661 */           return false;
/*      */         }
/*      */       }
/*      */       try {
/* 1665 */         Zone zone = Zones.getZone(tilex, tiley, pollingSurface);
/* 1666 */         VolaTile tempvtile1 = zone.getTileOrNull(tilex, tiley);
/* 1667 */         if (tempvtile1 != null && containsHouse(tempvtile1)) {
/* 1668 */           return false;
/*      */         }
/* 1670 */       } catch (NoSuchZoneException nsz) {
/*      */         
/* 1672 */         logger.log(Level.WARNING, "Weird, no zone for " + tilex + ", " + tiley + " surfaced=" + pollingSurface, (Throwable)nsz);
/*      */       } 
/* 1674 */       if (containsTracks(tilex, tiley))
/* 1675 */         return false; 
/* 1676 */       if (type == Tiles.Tile.TILE_DIRT_PACKED.id && Villages.getVillage(tilex, tiley, pollingSurface) != null)
/* 1677 */         return false; 
/* 1678 */       boolean foundGrass = false;
/* 1679 */       boolean foundMycel = false;
/* 1680 */       boolean foundSteppe = false;
/* 1681 */       boolean foundMoss = false;
/* 1682 */       int tundraCount = 0;
/*      */       
/* 1684 */       for (int xx = -1; xx <= 1; xx++) {
/*      */         
/* 1686 */         for (int yy = -1; yy <= 1; yy++) {
/*      */           
/* 1688 */           if ((xx == 0 && yy != 0) || (yy == 0 && xx != 0))
/*      */           {
/*      */             
/* 1691 */             if (tilex + xx >= 0 && tiley + yy >= 0 && tilex + xx < Zones.worldTileSizeX && tiley + yy < Zones.worldTileSizeY) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1699 */               if (xx >= 0 && yy >= 0) {
/*      */                 
/* 1701 */                 float height = Tiles.decodeHeight(currentMesh.getTile(tilex + xx, tiley + yy));
/* 1702 */                 if (height < 0.0F)
/* 1703 */                   return false; 
/*      */               } 
/* 1705 */               byte tempbyte2 = Tiles.decodeType(currentMesh.getTile(tilex + xx, tiley + yy));
/*      */ 
/*      */               
/* 1708 */               if (tempbyte2 == Tiles.Tile.TILE_GRASS.id && !isATree) {
/*      */                 
/* 1710 */                 foundGrass = true;
/* 1711 */                 if (flowerCounter++ == 10)
/*      */                 {
/*      */                   
/* 1714 */                   tileData = (byte)(Tiles.decodeData(currentMesh.getTile(tilex + xx, tiley + yy)) & 0xF);
/* 1715 */                   flowerCounter = 0;
/*      */                 }
/*      */               
/* 1718 */               } else if (Tiles.isNormal(tempbyte2)) {
/* 1719 */                 foundGrass = true;
/* 1720 */               } else if (Tiles.isMycelium(tempbyte2) && checkMycel) {
/* 1721 */                 foundMycel = true;
/* 1722 */               } else if (tempbyte2 == Tiles.Tile.TILE_STEPPE.id) {
/* 1723 */                 foundSteppe = true;
/* 1724 */               } else if (tempbyte2 == Tiles.Tile.TILE_MOSS.id) {
/* 1725 */                 foundMoss = true;
/*      */               } 
/*      */             }  } 
/* 1728 */           if (xx != 0 || yy != 0)
/*      */           {
/*      */             
/* 1731 */             if (tilex + xx >= 0 && tiley + yy >= 0 && tilex + xx < Zones.worldTileSizeX && tiley + yy < Zones.worldTileSizeY) {
/*      */ 
/*      */               
/* 1734 */               byte tempbyte2 = Tiles.decodeType(currentMesh.getTile(tilex + xx, tiley + yy));
/* 1735 */               if (Tiles.isTundra(tempbyte2)) {
/*      */                 
/* 1737 */                 tundraCount++;
/* 1738 */                 if (xx == 0 || yy == 0) {
/* 1739 */                   tundraCount++;
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/* 1746 */       if (!Tiles.isMycelium(type)) {
/*      */         
/* 1748 */         if (foundMoss && Server.rand.nextInt(10) == 1) {
/*      */           
/* 1750 */           currentMesh.setTile(tilex, tiley, Tiles.encode(theight, Tiles.Tile.TILE_MOSS.id, (byte)0));
/* 1751 */           Server.modifyFlagsByTileType(tilex, tiley, Tiles.Tile.TILE_MOSS.id);
/* 1752 */           Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/* 1753 */           return true;
/*      */         } 
/* 1755 */         if (foundSteppe && Server.rand.nextInt(4) == 1) {
/*      */           
/* 1757 */           currentMesh.setTile(tilex, tiley, Tiles.encode(theight, Tiles.Tile.TILE_STEPPE.id, (byte)0));
/* 1758 */           Server.modifyFlagsByTileType(tilex, tiley, Tiles.Tile.TILE_STEPPE.id);
/* 1759 */           Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/* 1760 */           return true;
/*      */         } 
/* 1762 */         if (tundraCount > 1) {
/*      */           
/* 1764 */           if (Server.rand.nextInt(15) == 1) {
/*      */             
/* 1766 */             currentMesh.setTile(tilex, tiley, Tiles.encode(theight, Tiles.Tile.TILE_TUNDRA.id, (byte)0));
/* 1767 */             Server.modifyFlagsByTileType(tilex, tiley, Tiles.Tile.TILE_TUNDRA.id);
/* 1768 */             Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/* 1769 */             return true;
/*      */           } 
/*      */           
/* 1772 */           if (foundGrass && Server.rand.nextInt(5) != 0)
/* 1773 */             return true; 
/*      */         } 
/*      */       } 
/* 1776 */       if (foundGrass || foundMycel)
/*      */       {
/* 1778 */         if (Tiles.isMycelium(type)) {
/*      */ 
/*      */           
/* 1781 */           byte newTileType = (byte)Tiles.toNormal(type);
/*      */           
/* 1783 */           if (type == Tiles.Tile.TILE_MYCELIUM_LAWN.id) {
/* 1784 */             newTileType = Tiles.Tile.TILE_LAWN.id;
/*      */           }
/* 1786 */           currentMesh.setTile(tilex, tiley, Tiles.encode(theight, newTileType, tileData));
/* 1787 */           Server.modifyFlagsByTileType(tilex, tiley, newTileType);
/*      */         } else {
/*      */           byte newTileType;
/*      */ 
/*      */ 
/*      */           
/* 1793 */           if (theight < MINIMUM_KELP_HEIGHT) {
/* 1794 */             newTileType = Tiles.Tile.TILE_KELP.id;
/* 1795 */           } else if (theight < 0) {
/* 1796 */             newTileType = Tiles.Tile.TILE_REED.id;
/*      */           } else {
/* 1798 */             newTileType = Tiles.Tile.TILE_GRASS.id;
/* 1799 */           }  currentMesh.setTile(tilex, tiley, Tiles.encode(theight, newTileType, tileData));
/* 1800 */           Server.modifyFlagsByTileType(tilex, tiley, newTileType);
/*      */         } 
/* 1802 */         Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/* 1803 */         return true;
/*      */       }
/*      */     
/* 1806 */     } else if (pollingSurface) {
/*      */       
/* 1808 */       if (Server.rand.nextInt(10) == 1)
/*      */       {
/* 1810 */         if (WurmCalendar.isMorning())
/*      */         {
/* 1812 */           SoundPlayer.playSound("sound.fish.splash", tilex, tiley, pollingSurface, 0.0F);
/*      */         }
/*      */       }
/*      */     } 
/* 1816 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean checkForDecayToDirt(int tile, int tilex, int tiley, byte type, byte aData) {
/* 1822 */     if (pollingSurface)
/*      */     {
/* 1824 */       if (Server.rand.nextInt(10) == 0)
/*      */         
/*      */         try {
/* 1827 */           Zone zone = Zones.getZone(tilex, tiley, pollingSurface);
/* 1828 */           VolaTile tempvtile1 = zone.getTileOrNull(tilex, tiley);
/* 1829 */           if (tempvtile1 != null)
/*      */           {
/* 1831 */             if (!containsHouse(tempvtile1) && tempvtile1.getVillage() == null)
/*      */             {
/* 1833 */               boolean foundMarsh = false;
/* 1834 */               for (int xx = -1; xx <= 1; xx++) {
/* 1835 */                 for (int yy = -1; yy <= 1; yy++) {
/*      */                   
/* 1837 */                   if (tilex + xx >= 0 && tiley + yy >= 0 && tilex + xx < 1 << Constants.meshSize && tiley + yy < 1 << Constants.meshSize)
/*      */                   {
/*      */                     
/* 1840 */                     if (Tiles.decodeType(currentMesh.getTile(tilex + xx, tiley + yy)) == Tiles.Tile.TILE_MARSH.id) {
/*      */                       
/* 1842 */                       foundMarsh = true;
/*      */                       break;
/*      */                     } 
/*      */                   }
/*      */                 } 
/*      */               } 
/* 1848 */               if (Tiles.decodeHeight(tile) > 0 && foundMarsh && Server.rand.nextInt(3) == 0) {
/*      */                 
/* 1850 */                 currentMesh.setTile(tilex, tiley, 
/* 1851 */                     Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_MARSH.id, (byte)0));
/* 1852 */                 Server.modifyFlagsByTileType(tilex, tiley, Tiles.Tile.TILE_MARSH.id);
/*      */               }
/*      */               else {
/*      */                 
/* 1856 */                 currentMesh
/* 1857 */                   .setTile(tilex, tiley, 
/* 1858 */                     Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_DIRT.id, (byte)0));
/* 1859 */                 Server.modifyFlagsByTileType(tilex, tiley, Tiles.Tile.TILE_DIRT.id);
/*      */               } 
/* 1861 */               Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/*      */             }
/*      */           
/*      */           }
/* 1865 */         } catch (NoSuchZoneException nsz) {
/*      */           
/* 1867 */           logger.log(Level.WARNING, "Weird, no zone for " + tilex + ", " + tiley + " surfaced=" + pollingSurface, (Throwable)nsz);
/*      */         }  
/*      */     }
/* 1870 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean checkForMycelGrowth(int tile, int tilex, int tiley, byte type, byte aData) {
/* 1876 */     if (Tiles.decodeHeight(tile) > 0 && pollingSurface) {
/*      */       
/* 1878 */       if (containsStructure(tilex, tiley)) {
/* 1879 */         return true;
/*      */       }
/* 1881 */       boolean checkMycel = (Servers.isThisAPvpServer() && Kingdoms.getKingdomTemplateFor(Zones.getKingdom(tilex, tiley)) == 3);
/* 1882 */       boolean foundMycel = false;
/* 1883 */       boolean foundMoss = false;
/* 1884 */       boolean foundSteppe = false;
/*      */       
/* 1886 */       for (int xx = -1; xx <= 1; xx++) {
/*      */         
/* 1888 */         for (int yy = -1; yy <= 1; yy++) {
/*      */           
/* 1890 */           if ((xx == 0 && yy != 0) || (yy == 0 && xx != 0))
/*      */           {
/* 1892 */             if (tilex + xx >= 0 && tiley + yy >= 0 && tilex + xx < 1 << Constants.meshSize && tiley + yy < 1 << Constants.meshSize) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1900 */               if (xx >= 0 && yy >= 0) {
/*      */                 
/* 1902 */                 float height = Tiles.decodeHeight(currentMesh.getTile(tilex + xx, tiley + yy));
/* 1903 */                 if (height < 0.0F)
/* 1904 */                   return false; 
/*      */               } 
/* 1906 */               byte type2 = Tiles.decodeType(currentMesh.getTile(tilex + xx, tiley + yy));
/*      */               
/* 1908 */               if (Tiles.isMycelium(type2) && checkMycel) {
/* 1909 */                 foundMycel = true;
/* 1910 */               } else if (type2 == Tiles.Tile.TILE_MOSS.id) {
/* 1911 */                 foundMoss = true;
/* 1912 */               } else if (type2 == Tiles.Tile.TILE_STEPPE.id) {
/* 1913 */                 foundSteppe = true;
/*      */               } 
/*      */             }  } 
/*      */         } 
/*      */       } 
/* 1918 */       if (foundMycel) {
/*      */ 
/*      */ 
/*      */         
/* 1922 */         if (type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_STEPPE.id || type == Tiles.Tile.TILE_DIRT.id || type == Tiles.Tile.TILE_LAWN.id || type == Tiles.Tile.TILE_MOSS.id) {
/*      */ 
/*      */           
/* 1925 */           byte newType = Tiles.Tile.TILE_MYCELIUM.id;
/* 1926 */           if (type == Tiles.Tile.TILE_LAWN.id) {
/* 1927 */             newType = Tiles.Tile.TILE_MYCELIUM_LAWN.id;
/*      */           }
/* 1929 */           currentMesh.setTile(tilex, tiley, 
/* 1930 */               Tiles.encode(Tiles.decodeHeight(tile), newType, (byte)0));
/* 1931 */           Server.modifyFlagsByTileType(tilex, tiley, newType);
/* 1932 */           Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/*      */         }
/* 1934 */         else if (Tiles.isNormalTree(type) || Tiles.isNormalBush(type)) {
/*      */           
/* 1936 */           byte newType = (byte)Tiles.toMycelium(type);
/* 1937 */           currentMesh.setTile(tilex, tiley, 
/* 1938 */               Tiles.encode(Tiles.decodeHeight(tile), newType, aData));
/* 1939 */           Server.modifyFlagsByTileType(tilex, tiley, newType);
/* 1940 */           Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/*      */         } 
/* 1942 */         return foundMycel;
/*      */       } 
/* 1944 */       if (type == Tiles.Tile.TILE_GRASS.id) {
/*      */         
/* 1946 */         byte newType = 0;
/* 1947 */         if (foundMoss && Server.rand.nextInt(300) == 0) {
/* 1948 */           newType = Tiles.Tile.TILE_MOSS.id;
/* 1949 */         } else if (foundSteppe && Server.rand.nextInt(10) == 0) {
/* 1950 */           newType = Tiles.Tile.TILE_STEPPE.id;
/*      */         } 
/* 1952 */         if (newType != 0)
/*      */         {
/* 1954 */           currentMesh.setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), newType, (byte)0));
/* 1955 */           Server.modifyFlagsByTileType(tilex, tiley, newType);
/* 1956 */           Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/* 1957 */           return true;
/*      */         }
/*      */       
/*      */       } 
/* 1961 */     } else if (pollingSurface) {
/*      */       
/* 1963 */       if (Server.rand.nextInt(10) == 1)
/*      */       {
/* 1965 */         if (WurmCalendar.isMorning())
/*      */         {
/* 1967 */           SoundPlayer.playSound("sound.fish.splash", tilex, tiley, pollingSurface, 0.0F);
/*      */         }
/*      */       }
/*      */     } 
/* 1971 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean checkForTreeSprout(int tilex, int tiley, int origtype, byte origdata) {
/* 1978 */     int newtilex = tilex - 10 + Server.rand.nextInt(21);
/* 1979 */     int newtiley = tiley - 10 + Server.rand.nextInt(21);
/*      */     
/* 1981 */     if (!GeneralUtilities.isValidTileLocation(newtilex, newtiley))
/* 1982 */       return true; 
/* 1983 */     int newtile = currentMesh.getTile(newtilex, newtiley);
/*      */     
/* 1985 */     if (Tiles.decodeHeight(newtile) > 0 && pollingSurface) {
/*      */ 
/*      */       
/* 1988 */       if (newtilex == tilex && newtiley == tiley) {
/* 1989 */         return true;
/*      */       }
/* 1991 */       if (containsStructure(newtilex, newtiley) || containsTracks(newtilex, newtiley))
/* 1992 */         return true; 
/* 1993 */       byte newtype = Tiles.decodeType(newtile);
/* 1994 */       Tiles.Tile theNewTile = Tiles.getTile(newtype);
/* 1995 */       Tiles.Tile theOrigTile = Tiles.getTile(origtype);
/* 1996 */       short newHeight = Tiles.decodeHeight(newtile);
/*      */ 
/*      */       
/* 1999 */       if (theNewTile == Tiles.Tile.TILE_GRASS || theNewTile == Tiles.Tile.TILE_MYCELIUM) {
/*      */ 
/*      */         
/* 2002 */         int foundTrees = 0;
/* 2003 */         for (int xx = -3; xx <= 3; xx++) {
/*      */           
/* 2005 */           for (int yy = -3; yy <= 3; yy++) {
/*      */ 
/*      */             
/* 2008 */             if ((xx == 0 && yy != 0) || (xx != 0 && yy == 0))
/*      */             {
/*      */               
/* 2011 */               if (GeneralUtilities.isValidTileLocation(newtilex + xx, newtiley + yy)) {
/*      */                 
/* 2013 */                 int checkTile = currentMesh.getTile(newtilex + xx, newtiley + yy);
/* 2014 */                 byte checktype = Tiles.decodeType(checkTile);
/* 2015 */                 byte checkdata = Tiles.decodeData(checkTile);
/* 2016 */                 Tiles.Tile theCheckTile = Tiles.getTile(checktype);
/*      */ 
/*      */                 
/* 2019 */                 if (theCheckTile.isTree()) {
/*      */ 
/*      */                   
/* 2022 */                   if (theCheckTile.isOak(checkdata) || theOrigTile.isOak(origdata)) {
/*      */                     
/* 2024 */                     foundTrees++;
/*      */                     break;
/*      */                   } 
/* 2027 */                   if (theCheckTile.isWillow(checkdata) || theOrigTile.isWillow(origdata)) {
/*      */                     
/* 2029 */                     if (xx > -3 && xx < 3 && yy > -3 && yy < 3) {
/*      */                       
/* 2031 */                       foundTrees++;
/*      */                       
/*      */                       break;
/*      */                     } 
/* 2035 */                   } else if (xx > -2 && xx < 2 && yy > -2 && yy < 2) {
/*      */                     
/* 2037 */                     foundTrees++;
/*      */                     
/*      */                     break;
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*      */         
/* 2047 */         if (foundTrees < 1)
/*      */         {
/*      */           
/* 2050 */           byte newType, newdata = 0;
/*      */           
/* 2052 */           boolean evil = false;
/* 2053 */           if (Kingdoms.getKingdomTemplateFor(Zones.getKingdom(tilex, tiley)) == 3)
/* 2054 */             evil = true; 
/* 2055 */           if (theOrigTile.isTree()) {
/*      */             
/* 2057 */             TreeData.TreeType treetype = theOrigTile.getTreeType(origdata);
/*      */             
/* 2059 */             if (evil && theOrigTile.isMycelium()) {
/* 2060 */               newType = treetype.asMyceliumTree();
/*      */             } else {
/* 2062 */               newType = treetype.asNormalTree();
/*      */             } 
/*      */           } else {
/*      */             
/* 2066 */             BushData.BushType bushtype = theOrigTile.getBushType(origdata);
/*      */             
/* 2068 */             if (evil && theOrigTile.isMycelium()) {
/* 2069 */               newType = bushtype.asMyceliumBush();
/*      */             } else {
/* 2071 */               newType = bushtype.asNormalBush();
/*      */             } 
/* 2073 */           }  newdata = Tiles.encodeTreeData(FoliageAge.YOUNG_ONE, false, false, GrassData.GrowthTreeStage.SHORT);
/* 2074 */           currentMesh.setTile(newtilex, newtiley, Tiles.encode(newHeight, newType, newdata));
/* 2075 */           Server.modifyFlagsByTileType(newtilex, newtiley, newType);
/* 2076 */           Server.setWorldResource(newtilex, newtiley, 0);
/*      */           
/* 2078 */           if (WurmCalendar.isNight()) {
/* 2079 */             SoundPlayer.playSound("sound.birdsong.bird1", newtilex, newtiley, pollingSurface, 3.0F);
/*      */           } else {
/* 2081 */             SoundPlayer.playSound("sound.birdsong.bird4", newtilex, newtiley, pollingSurface, 0.3F);
/*      */           } 
/* 2083 */           Players.getInstance().sendChangedTile(newtilex, newtiley, pollingSurface, false);
/* 2084 */           return true;
/*      */         }
/*      */       
/*      */       } 
/* 2088 */     } else if (pollingSurface) {
/*      */       
/* 2090 */       if (Server.rand.nextInt(10) == 1)
/*      */       {
/* 2092 */         if (WurmCalendar.isMorning())
/*      */         {
/* 2094 */           SoundPlayer.playSound("sound.fish.splash", newtilex, newtiley, pollingSurface, 0.0F);
/*      */         }
/*      */       }
/*      */     } 
/* 2098 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void checkForTreeGrowth(int tile, int tilex, int tiley, byte type, byte aData) {
/* 2103 */     if (pollingSurface) {
/*      */       
/* 2105 */       Tiles.Tile theTile = Tiles.getTile(type);
/* 2106 */       boolean underwater = (Tiles.decodeHeight(tile) <= -5);
/* 2107 */       int age = aData >> 4 & 0xF;
/*      */       
/* 2109 */       if (age != 15) {
/*      */ 
/*      */         
/* 2112 */         if (treeGrowth == 0 && age <= 1) {
/*      */           
/* 2114 */           currentMesh.setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_GRASS.id, (byte)0));
/* 2115 */           Server.modifyFlagsByTileType(tilex, tiley, Tiles.Tile.TILE_GRASS.id);
/* 2116 */           Server.setWorldResource(tilex, tiley, 0);
/* 2117 */           Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/*      */ 
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/* 2123 */         int chance = entryServer ? Server.rand.nextInt(20) : Server.rand.nextInt(225);
/* 2124 */         if (chance <= (16 - age) * (16 - age)) {
/*      */           
/* 2126 */           byte partdata = (byte)(aData & 0xF);
/* 2127 */           boolean isOak = theTile.isOak(partdata);
/* 2128 */           boolean isWillow = theTile.isWillow(partdata);
/*      */           
/* 2130 */           if (!isOak || Server.rand.nextInt(5) == 0) {
/*      */             
/* 2132 */             if (theTile.isMycelium())
/*      */             {
/* 2134 */               if (Kingdoms.getKingdomTemplateFor(Zones.getKingdom(tilex, tiley)) != 3)
/*      */               {
/* 2136 */                 if (Server.rand.nextInt(3) == 0) {
/*      */ 
/*      */                   
/* 2139 */                   byte b1, b2 = (byte)(aData & 0xF7);
/* 2140 */                   if (theTile.isTree()) {
/* 2141 */                     b1 = theTile.getTreeType(partdata).asNormalTree();
/*      */                   } else {
/* 2143 */                     b1 = theTile.getBushType(partdata).asNormalBush();
/*      */                   } 
/* 2145 */                   currentMesh.setTile(tilex, tiley, 
/* 2146 */                       Tiles.encode(Tiles.decodeHeight(tile), b1, b2));
/* 2147 */                   Server.modifyFlagsByTileType(tilex, tiley, b1);
/* 2148 */                   Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/*      */                   return;
/*      */                 } 
/*      */               }
/*      */             }
/* 2153 */             if (underwater) {
/* 2154 */               age = FoliageAge.SHRIVELLED.getAgeId();
/*      */             } else {
/* 2156 */               age++;
/* 2157 */             }  if (chance > 8)
/*      */             {
/* 2159 */               if (WurmCalendar.isNight()) {
/*      */                 
/* 2161 */                 SoundPlayer.playSound("sound.birdsong.owl.short", tilex, tiley, pollingSurface, 4.0F);
/*      */               }
/*      */               else {
/*      */                 
/* 2165 */                 SoundPlayer.playSound("sound.ambient.day.crickets", tilex, tiley, pollingSurface, 0.0F);
/*      */               } 
/*      */             }
/*      */             
/* 2169 */             Server.setWorldResource(tilex, tiley, 0);
/*      */             
/* 2171 */             byte newData = (byte)((age << 4) + partdata & 0xFF);
/* 2172 */             byte newType = convertToNewType(theTile, newData);
/*      */             
/* 2174 */             currentMesh
/* 2175 */               .setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), newType, newData));
/* 2176 */             Server.modifyFlagsByTileType(tilex, tiley, newType);
/* 2177 */             Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/* 2178 */             if (age >= 15) {
/* 2179 */               Zones.removeWildHive(tilex, tiley);
/*      */             }
/*      */           } 
/*      */           
/* 2183 */           if ((isOak || isWillow) && age >= 7) {
/*      */             
/* 2185 */             int rad = 1;
/* 2186 */             if (age >= 10)
/* 2187 */               rad = 2; 
/* 2188 */             int maxX = Math.min(tilex + rad, currentMesh.getSize() - 1);
/* 2189 */             int maxY = Math.min(tiley + rad, currentMesh.getSize() - 1);
/* 2190 */             for (int x = Math.max(tilex - rad, 0); x <= maxX; x++) {
/*      */               
/* 2192 */               for (int y = Math.max(tiley - rad, 0); y <= maxY; y++) {
/*      */                 
/* 2194 */                 if (x != tilex || y != tiley) {
/*      */                   
/* 2196 */                   int tt = currentMesh.getTile(x, y);
/* 2197 */                   byte ttyp = Tiles.decodeType(tt);
/* 2198 */                   Tiles.Tile ttile = Tiles.getTile(ttyp);
/*      */                   
/* 2200 */                   byte newType = Tiles.Tile.TILE_GRASS.id;
/* 2201 */                   if (ttile.isMyceliumTree() || ttile.isMyceliumBush()) {
/* 2202 */                     newType = Tiles.Tile.TILE_MYCELIUM.id;
/*      */                   }
/* 2204 */                   if (ttile.isTree() || ttile.isBush()) {
/*      */                     
/* 2206 */                     byte newData = 0;
/* 2207 */                     Server.setWorldResource(x, y, 0);
/* 2208 */                     currentMesh.setTile(x, y, 
/* 2209 */                         Tiles.encode(Tiles.decodeHeight(tt), newType, (byte)0));
/* 2210 */                     Server.modifyFlagsByTileType(x, y, newType);
/* 2211 */                     Players.getInstance().sendChangedTile(x, y, pollingSurface, false);
/*      */                     
/* 2213 */                     Zones.removeWildHive(x, y);
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/* 2220 */           Zones.reposWildHive(tilex, tiley, theTile, aData);
/*      */         } 
/*      */         
/* 2223 */         if (age < 15 && age > 8 && treeGrowth > 0 && !underwater && theTile != Tiles.Tile.TILE_BUSH_LINGONBERRY) {
/*      */           
/* 2225 */           int growthChance = Server.rand.nextInt(treeGrowth);
/* 2226 */           if (growthChance < 1) {
/* 2227 */             checkForTreeSprout(tilex, tiley, type, aData);
/* 2228 */           } else if (growthChance == 2) {
/* 2229 */             growMushroom(tilex, tiley);
/*      */           } 
/* 2231 */         }  if (theTile.isTree() && age == 15)
/*      */         {
/*      */           
/* 2234 */           Server.setGrubs(tilex, tiley, true);
/*      */         }
/* 2236 */         if (theTile.isTree() && age == 14 && theTile == Tiles.Tile.TILE_TREE_BIRCH)
/*      */         {
/*      */           
/* 2239 */           checkGrubGrowth(tile, tilex, tiley);
/*      */         }
/* 2241 */         if (theTile.isBush())
/*      */         {
/*      */           
/* 2244 */           checkGrubGrowth(tile, tilex, tiley);
/*      */         }
/*      */       }
/* 2247 */       else if (theTile == Tiles.Tile.TILE_BUSH_LINGONBERRY) {
/*      */ 
/*      */         
/* 2250 */         currentMesh.setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_TUNDRA.id, (byte)0));
/* 2251 */         Server.modifyFlagsByTileType(tilex, tiley, Tiles.Tile.TILE_TUNDRA.id);
/* 2252 */         Server.setWorldResource(tilex, tiley, 0);
/* 2253 */         Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 2258 */         int chance = Server.rand.nextInt(15);
/* 2259 */         if (chance == 1) {
/*      */ 
/*      */           
/* 2262 */           Zones.removeWildHive(tilex, tiley);
/* 2263 */           if (underwater) {
/*      */ 
/*      */             
/* 2266 */             currentMesh.setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_DIRT.id, (byte)0));
/* 2267 */             Server.modifyFlagsByTileType(tilex, tiley, Tiles.Tile.TILE_DIRT.id);
/* 2268 */             Server.setWorldResource(tilex, tiley, 0);
/* 2269 */             Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 2274 */             byte newType = type;
/* 2275 */             byte newData = (byte)(aData & 0xF);
/*      */             
/* 2277 */             newType = convertToNewType(theTile, aData);
/*      */             
/* 2279 */             boolean noChange = true;
/* 2280 */             Village v = Villages.getVillage(tilex, tiley, true);
/* 2281 */             if (v == null)
/* 2282 */               noChange = (Server.rand.nextInt(100) < 75); 
/* 2283 */             boolean inCenter = (TreeData.isCentre(newData) && noChange);
/* 2284 */             GrassData.GrowthTreeStage stage = TreeData.getGrassLength(newData);
/* 2285 */             newData = Tiles.encodeTreeData((byte)0, false, inCenter, stage);
/*      */             
/* 2287 */             Server.setWorldResource(tilex, tiley, 0);
/* 2288 */             currentMesh.setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), newType, newData));
/*      */ 
/*      */             
/* 2291 */             Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/*      */           } 
/*      */         } else {
/*      */           
/* 2295 */           checkGrubGrowth(tile, tilex, tiley);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void checkForLingonberryStart(int tile, int tilex, int tiley) {
/* 2309 */     for (int x = tilex - 2; x < tilex + 2; x++) {
/*      */       
/* 2311 */       for (int y = tiley - 2; y < tiley + 2; y++) {
/*      */         
/* 2313 */         int tt = currentMesh.getTile(x, y);
/* 2314 */         byte ttyp = Tiles.decodeType(tt);
/* 2315 */         Tiles.Tile ttile = Tiles.getTile(ttyp);
/* 2316 */         if (ttile != Tiles.Tile.TILE_TUNDRA && ttile != Tiles.Tile.TILE_GRASS && ttile != Tiles.Tile.TILE_DIRT) {
/*      */           return;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 2322 */     currentMesh.setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_BUSH_LINGONBERRY.id, (byte)0));
/* 2323 */     Server.modifyFlagsByTileType(tilex, tiley, Tiles.Tile.TILE_BUSH_LINGONBERRY.id);
/* 2324 */     Server.setWorldResource(tilex, tiley, 0);
/* 2325 */     Players.getInstance().sendChangedTile(tilex, tiley, pollingSurface, false);
/*      */   }
/*      */ 
/*      */   
/*      */   private static byte convertToNewType(Tiles.Tile theTile, byte data) {
/* 2330 */     if (theTile.isNormalTree())
/* 2331 */       return theTile.getTreeType(data).asNormalTree(); 
/* 2332 */     if (theTile.isMyceliumTree())
/* 2333 */       return theTile.getTreeType(data).asMyceliumTree(); 
/* 2334 */     if (theTile.isEnchantedTree())
/* 2335 */       return theTile.getTreeType(data).asEnchantedTree(); 
/* 2336 */     if (theTile.isNormalBush())
/* 2337 */       return theTile.getBushType(data).asNormalBush(); 
/* 2338 */     if (theTile.isMyceliumBush()) {
/* 2339 */       return theTile.getBushType(data).asMyceliumBush();
/*      */     }
/*      */     
/* 2342 */     return theTile.getBushType(data).asEnchantedBush();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void growMushroom(int tilex, int tiley) {
/* 2347 */     int num = tilex + tiley;
/* 2348 */     if (num % 128 == 0) {
/*      */       
/* 2350 */       if (logger.isLoggable(Level.FINEST))
/*      */       {
/* 2352 */         logger.finest("Creating mushrrom at tile " + tilex + ", " + tiley);
/*      */       }
/* 2354 */       int chance = Server.rand.nextInt(100);
/*      */       
/* 2356 */       int templ = 247;
/* 2357 */       if (chance > 40)
/*      */       {
/* 2359 */         if (chance < 60) {
/* 2360 */           templ = 246;
/* 2361 */         } else if (chance < 80) {
/* 2362 */           templ = 248;
/* 2363 */         } else if (chance < 90) {
/* 2364 */           templ = 249;
/* 2365 */         } else if (chance < 99) {
/* 2366 */           templ = 251;
/*      */         } else {
/* 2368 */           templ = 250;
/*      */         } 
/*      */       }
/* 2371 */       float posx = (tilex << 2) + Server.rand.nextFloat() * 4.0F;
/* 2372 */       float posy = (tiley << 2) + Server.rand.nextFloat() * 4.0F;
/*      */ 
/*      */       
/*      */       try {
/* 2376 */         ItemFactory.createItem(templ, 80.0F + Server.rand.nextInt(20), posx, posy, Server.rand.nextInt(180), pollingSurface, (byte)0, -10L, null);
/*      */       
/*      */       }
/* 2379 */       catch (FailedException fe) {
/*      */         
/* 2381 */         logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*      */       }
/* 2383 */       catch (NoSuchTemplateException nst) {
/*      */         
/* 2385 */         logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addWildBeeHives(int tilex, int tiley, Tiles.Tile theTile, byte aData) {
/* 2402 */     if (Zones.isFarFromAnyHive(tilex, tiley, true)) {
/*      */ 
/*      */       
/* 2405 */       byte age = FoliageAge.getAgeAsByte(aData);
/* 2406 */       TreeData.TreeType treeType = theTile.getTreeType(aData);
/*      */       
/* 2408 */       if (age > 7 && age < 13) {
/*      */ 
/*      */         
/* 2411 */         boolean canHaveHive = false;
/* 2412 */         switch (treeType) {
/*      */ 
/*      */ 
/*      */           
/*      */           case BIRCH:
/*      */           case PINE:
/*      */           case CEDAR:
/*      */           case WILLOW:
/*      */           case MAPLE:
/*      */           case FIR:
/*      */           case LINDEN:
/* 2423 */             canHaveHive = true;
/*      */             break;
/*      */         } 
/*      */         
/* 2427 */         if (canHaveHive) {
/*      */ 
/*      */           
/* 2430 */           r.setSeed((tilex + tiley * Zones.worldTileSizeY) * HIVE_PRIME);
/* 2431 */           if (r.nextInt(500) == 0) {
/*      */ 
/*      */             
/* 2434 */             boolean ok = true;
/* 2435 */             for (int x = -1; x <= 1; x++) {
/*      */               
/* 2437 */               for (int y = -1; y <= 1; y++) {
/*      */                 
/* 2439 */                 VolaTile vt = Zones.getTileOrNull(x, y, true);
/* 2440 */                 if (vt != null && vt.getStructure() != null) {
/*      */                   
/* 2442 */                   ok = false;
/*      */                   break;
/*      */                 } 
/*      */               } 
/*      */             } 
/* 2447 */             if (ok) {
/*      */               
/* 2449 */               Point4f p = MethodsItems.getHivePos(tilex, tiley, theTile, aData);
/* 2450 */               if (p.getPosZ() == 0.0F) {
/*      */                 return;
/*      */               }
/*      */               
/*      */               try {
/* 2455 */                 int ql = 30 + Server.rand.nextInt(41);
/*      */                 
/* 2457 */                 Item wildHive = ItemFactory.createItem(1239, ql, treeType
/* 2458 */                     .getMaterial(), (byte)0, null);
/* 2459 */                 wildHive.setPos(p.getPosX(), p.getPosY(), p.getPosZ(), p.getRot(), -10L);
/* 2460 */                 wildHive.setLastOwnerId(-10L);
/*      */                 
/* 2462 */                 wildHive.setAuxData((byte)1);
/* 2463 */                 Zone zone = Zones.getZone(Zones.safeTileX(tilex), Zones.safeTileY(tiley), true);
/* 2464 */                 zone.addItem(wildHive);
/* 2465 */                 if (Servers.isThisATestServer()) {
/* 2466 */                   Players.getInstance().sendGmMessage(null, "System", "Debug: Adding Hive (" + wildHive.getWurmId() + ") @ " + tilex + "," + tiley + " ql:" + ql + " rot:" + p
/* 2467 */                       .getRot() + " ht:" + p.getPosZ() + " material:" + treeType.getName(), false);
/*      */                 }
/* 2469 */               } catch (FailedException fe) {
/*      */                 
/* 2471 */                 logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*      */               }
/* 2473 */               catch (NoSuchTemplateException nst) {
/*      */                 
/* 2475 */                 logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */               }
/* 2477 */               catch (NoSuchZoneException e) {
/*      */                 
/* 2479 */                 logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void checkHoneyProduction(HiveZone hiveZone, int tilex, int tiley, byte type, byte data, Tiles.Tile theTile) {
/* 2500 */     Item hive = hiveZone.getCurrentHive();
/* 2501 */     if (!hive.isOnSurface()) {
/*      */       
/* 2503 */       if (Servers.isThisATestServer())
/* 2504 */         Players.getInstance().sendGmMessage(null, "System", "Debug: Queen bee died as hive is underground @ " + hive.getTileX() + "," + hive.getTileY() + " from " + hive
/* 2505 */             .getWurmId() + " to " + hive.getWurmId(), false); 
/* 2506 */       hive.removeQueen();
/*      */       return;
/*      */     } 
/* 2509 */     if (hiveZone.hasHive(tilex, tiley))
/*      */     {
/* 2511 */       if (Server.rand.nextInt(5) == 0) {
/*      */ 
/*      */         
/* 2514 */         Item sugar = hive.getSugar();
/* 2515 */         if (sugar != null) {
/*      */ 
/*      */           
/* 2518 */           Items.destroyItem(sugar.getWurmId());
/*      */         }
/*      */         else {
/*      */           
/* 2522 */           Item honey = hive.getHoney();
/* 2523 */           if (honey != null) {
/*      */             
/* 2525 */             honey.setWeight(Math.max(0, honey.getWeightGrams() - 10), true);
/* 2526 */             honey.setLastMaintained(WurmCalendar.currentTime);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/* 2532 */     if (hive.hasQueen()) {
/*      */ 
/*      */       
/* 2535 */       if (Server.rand.nextInt(3) == 0) {
/*      */         
/* 2537 */         VolaTile vt = Zones.getTileOrNull(tilex, tiley, true);
/* 2538 */         Item emptyHive = (vt != null) ? vt.findHive(1175, false) : null;
/*      */ 
/*      */         
/* 2541 */         if (emptyHive != null) {
/*      */           
/* 2543 */           boolean canMove = false;
/* 2544 */           if (hive.hasTwoQueens()) {
/* 2545 */             canMove = true;
/* 2546 */           } else if (hive.getTemplateId() != 1175 && emptyHive.getCurrentQualityLevel() > hive.getCurrentQualityLevel()) {
/*      */ 
/*      */             
/* 2549 */             float distX = Math.abs(hive.getPosX() - emptyHive.getPosX());
/* 2550 */             float distY = Math.abs(hive.getPosY() - emptyHive.getPosY());
/* 2551 */             float dist = Math.max(distX, distY);
/* 2552 */             if (dist == 0.0F)
/* 2553 */               logger.info("More than one hive on same tile " + hive.getPosX() + "," + hive.getPosY()); 
/* 2554 */             canMove = (Server.rand.nextInt(Math.max(1, (int)dist)) == 0);
/*      */           } 
/* 2556 */           if (canMove) {
/*      */             
/* 2558 */             if (Servers.isThisATestServer())
/* 2559 */               Players.getInstance().sendGmMessage(null, "System", "Debug: Queen bee migrated @ " + hive.getTileX() + "," + hive.getTileY() + " from " + hive
/* 2560 */                   .getWurmId() + " to " + emptyHive.getWurmId(), false); 
/* 2561 */             hive.removeQueen();
/* 2562 */             emptyHive.addQueen();
/* 2563 */             if (!hive.hasQueen()) {
/*      */ 
/*      */ 
/*      */               
/* 2567 */               if (hive.getTemplateId() == 1239) {
/*      */                 
/* 2569 */                 for (Item item : hive.getItemsAsArray())
/*      */                 {
/* 2571 */                   Items.destroyItem(item.getWurmId());
/*      */                 }
/* 2573 */                 Items.destroyItem(hive.getWurmId());
/*      */                 
/*      */                 return;
/*      */               } 
/* 2577 */               Zones.removeHive(hive, false);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 2582 */       if (Server.rand.nextInt(3) == 0)
/*      */       {
/* 2584 */         Item honey = addHoney(tilex, tiley, hive, type, data, theTile);
/*      */         
/* 2586 */         if (hive.hasQueen() && !hive.hasTwoQueens())
/*      */         {
/* 2588 */           if ((WurmCalendar.isSeasonSpring() || WurmCalendar.isSeasonSummer()) && hiveZone.hasHive(tilex, tiley))
/*      */           {
/* 2590 */             if (honey != null)
/*      */             {
/* 2592 */               if (honey.getWeightGrams() > 1000 && Server.rand.nextInt(5) == 0)
/*      */               {
/*      */                 
/* 2595 */                 if (Servers.isThisATestServer())
/* 2596 */                   Players.getInstance().sendGmMessage(null, "System", "Debug: Queen bee added @ " + hive.getTileX() + "," + hive.getTileY(), false); 
/* 2597 */                 hive.addQueen();
/*      */               }
/*      */             
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 2607 */       Zones.removeHive(hive, true);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private static Item addHoney(int tilex, int tiley, Item hive, byte type, byte data, Tiles.Tile theTile) {
/* 2616 */     float nectarProduced = 0.0F;
/* 2617 */     int starfall = WurmCalendar.getStarfall();
/* 2618 */     if (type == 7 || type == 43) {
/*      */ 
/*      */       
/* 2621 */       switch (FieldData.getAge(data)) {
/*      */         
/*      */         case 0:
/* 2624 */           nectarProduced += 0.0F;
/*      */           break;
/*      */         case 1:
/* 2627 */           nectarProduced += 4.0F;
/*      */           break;
/*      */         case 2:
/* 2630 */           nectarProduced += 10.0F;
/*      */           break;
/*      */         case 3:
/* 2633 */           nectarProduced += 15.0F;
/*      */           break;
/*      */         case 4:
/* 2636 */           nectarProduced += 17.0F;
/*      */           break;
/*      */         case 5:
/* 2639 */           nectarProduced += 8.0F;
/*      */           break;
/*      */         case 6:
/* 2642 */           nectarProduced += 6.0F;
/*      */           break;
/*      */         case 7:
/* 2645 */           nectarProduced += 0.0F;
/*      */           break;
/*      */       } 
/*      */       
/* 2649 */       int worldResource = Server.getWorldResource(tilex, tiley);
/* 2650 */       int farmedCount = worldResource >>> 11;
/* 2651 */       int farmedChance = worldResource & 0x7FF;
/* 2652 */       int newfarmedChance = Math.min(farmedChance + (int)(nectarProduced * 10.0F), 2047);
/* 2653 */       Server.setWorldResource(tilex, tiley, (farmedCount << 11) + newfarmedChance);
/* 2654 */       Players.getInstance().sendChangedTile(tilex, tiley, true, false);
/*      */     }
/* 2656 */     else if (type == 2) {
/*      */ 
/*      */       
/* 2659 */       if (starfall < 1) {
/* 2660 */         nectarProduced += 0.0F;
/* 2661 */       } else if (starfall < 4) {
/* 2662 */         nectarProduced += 5.0F;
/* 2663 */       } else if (starfall < 9) {
/* 2664 */         nectarProduced += 5.0F;
/*      */       } else {
/* 2666 */         nectarProduced += 2.0F;
/*      */       } 
/* 2668 */       GrassData.FlowerType flowerType = GrassData.FlowerType.decodeTileData(data);
/* 2669 */       if (flowerType != GrassData.FlowerType.NONE) {
/* 2670 */         nectarProduced *= 1.0F + flowerType.getEncodedData() / 2.0F;
/*      */       }
/* 2672 */     } else if (theTile.isEnchanted()) {
/*      */ 
/*      */       
/* 2675 */       if (starfall < 1) {
/* 2676 */         nectarProduced++;
/* 2677 */       } else if (starfall < 4) {
/* 2678 */         nectarProduced += 6.0F;
/* 2679 */       } else if (starfall < 9) {
/* 2680 */         nectarProduced += 6.0F;
/*      */       } else {
/* 2682 */         nectarProduced += 3.0F;
/*      */       } 
/* 2684 */     } else if (type == 22) {
/*      */ 
/*      */       
/* 2687 */       if (starfall < 1) {
/* 2688 */         nectarProduced++;
/* 2689 */       } else if (starfall < 4) {
/* 2690 */         nectarProduced += 9.0F;
/* 2691 */       } else if (starfall < 9) {
/* 2692 */         nectarProduced += 11.0F;
/*      */       } else {
/* 2694 */         nectarProduced += 7.0F;
/*      */       } 
/* 2696 */     } else if (theTile.isNormalBush() || theTile.isNormalTree()) {
/*      */ 
/*      */       
/* 2699 */       int treeAge = FoliageAge.getAgeAsByte(data);
/* 2700 */       if (treeAge < 4) {
/* 2701 */         nectarProduced++;
/* 2702 */       } else if (treeAge < 8) {
/* 2703 */         nectarProduced += 4.0F;
/* 2704 */       } else if (treeAge < 12) {
/* 2705 */         nectarProduced += 8.0F;
/* 2706 */       } else if (treeAge < 14) {
/* 2707 */         nectarProduced += 7.0F;
/* 2708 */       } else if (treeAge < 15) {
/* 2709 */         nectarProduced += 5.0F;
/*      */       } 
/* 2711 */       if (starfall < 1) {
/* 2712 */         nectarProduced += 2.0F;
/* 2713 */       } else if (starfall < 4) {
/* 2714 */         nectarProduced += 8.0F;
/* 2715 */       } else if (starfall < 9) {
/* 2716 */         nectarProduced += 6.0F;
/*      */       } else {
/* 2718 */         nectarProduced += 4.0F;
/*      */       } 
/*      */     } 
/* 2721 */     if (starfall < 1) {
/* 2722 */       nectarProduced *= 0.1F;
/* 2723 */     } else if (starfall < 4) {
/* 2724 */       nectarProduced *= 1.5F;
/* 2725 */     } else if (starfall < 9) {
/* 2726 */       nectarProduced *= 1.1F;
/*      */     } else {
/* 2728 */       nectarProduced *= 0.7F;
/*      */     } 
/* 2730 */     Item honey = hive.getHoney();
/*      */     
/* 2732 */     if (nectarProduced < 5.0F)
/* 2733 */       return honey; 
/* 2734 */     int addedHoneyWeight = (int)nectarProduced - 4;
/* 2735 */     int newHoneyWeight = addedHoneyWeight;
/* 2736 */     if (addedHoneyWeight > 0 && hive.getFreeVolume() > addedHoneyWeight) {
/*      */ 
/*      */       
/* 2739 */       int waxcount = hive.getWaxCount();
/* 2740 */       if (honey != null) {
/*      */ 
/*      */ 
/*      */         
/* 2744 */         float totalQL = honey.getWeightGrams() * honey.getCurrentQualityLevel() + addedHoneyWeight * hive.getCurrentQualityLevel();
/* 2745 */         newHoneyWeight = honey.getWeightGrams() + addedHoneyWeight;
/* 2746 */         float newQL = totalQL / newHoneyWeight;
/* 2747 */         honey.setWeight(newHoneyWeight, true);
/* 2748 */         honey.setQualityLevel(newQL);
/* 2749 */         honey.setDamage(0.0F);
/* 2750 */         honey.setLastOwnerId(0L);
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*      */ 
/*      */           
/* 2757 */           Item newhoney = ItemFactory.createItem(70, hive.getCurrentQualityLevel(), (byte)29, (byte)0, null);
/*      */           
/* 2759 */           newhoney.setWeight(newHoneyWeight, true);
/* 2760 */           newhoney.setLastOwnerId(0L);
/* 2761 */           hive.insertItem(newhoney);
/*      */         }
/* 2763 */         catch (FailedException e) {
/*      */ 
/*      */           
/* 2766 */           logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */         }
/* 2768 */         catch (NoSuchTemplateException e) {
/*      */ 
/*      */           
/* 2771 */           logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 2776 */       if (nectarProduced > 10.0F)
/*      */       {
/*      */ 
/*      */         
/* 2780 */         if (waxcount < 20 && Server.rand.nextInt(40) == 0) {
/*      */           
/*      */           try {
/*      */             
/* 2784 */             Item newwax = ItemFactory.createItem(1254, hive.getCurrentQualityLevel(), (byte)29, (byte)0, null);
/*      */             
/* 2786 */             newwax.setLastOwnerId(0L);
/* 2787 */             if (hive.testInsertItem(newwax))
/*      */             {
/*      */ 
/*      */               
/* 2791 */               hive.insertItem(newwax);
/*      */             
/*      */             }
/*      */             else
/*      */             {
/* 2796 */               Items.destroyItem(newwax.getWurmId());
/*      */             }
/*      */           
/* 2799 */           } catch (FailedException e) {
/*      */ 
/*      */             
/* 2802 */             logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */           }
/* 2804 */           catch (NoSuchTemplateException e) {
/*      */ 
/*      */             
/* 2807 */             logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */           } 
/*      */         }
/*      */       }
/*      */     } 
/* 2812 */     return honey;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\TilePoller.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */