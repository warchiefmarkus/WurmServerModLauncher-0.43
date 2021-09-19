/*      */ package com.wurmonline.server.zones;
/*      */ 
/*      */ import com.wurmonline.math.TilePos;
/*      */ import com.wurmonline.mesh.FoliageAge;
/*      */ import com.wurmonline.mesh.MeshIO;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Point4f;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.ServerDirInfo;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.behaviours.MethodsCreatures;
/*      */ import com.wurmonline.server.behaviours.MethodsItems;
/*      */ import com.wurmonline.server.behaviours.Terraforming;
/*      */ import com.wurmonline.server.combat.CombatConstants;
/*      */ import com.wurmonline.server.creatures.CombatHandler;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.ai.NoPathException;
/*      */ import com.wurmonline.server.creatures.ai.Path;
/*      */ import com.wurmonline.server.creatures.ai.PathFinder;
/*      */ import com.wurmonline.server.creatures.ai.PathTile;
/*      */ import com.wurmonline.server.deities.Deity;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.kingdom.Kingdom;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.structures.Blocking;
/*      */ import com.wurmonline.server.structures.BlockingResult;
/*      */ import com.wurmonline.server.structures.BridgePart;
/*      */ import com.wurmonline.server.structures.DbStructure;
/*      */ import com.wurmonline.server.structures.Floor;
/*      */ import com.wurmonline.server.structures.NoSuchStructureException;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.Structures;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.shared.constants.BridgeConstants;
/*      */ import com.wurmonline.shared.constants.EffectConstants;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FileWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nonnull;
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
/*      */ public final class Zones
/*      */   implements MiscConstants, EffectConstants, CombatConstants, TimeConstants
/*      */ {
/*      */   private static Zone[][] surfaceZones;
/*      */   private static Zone[][] caveZones;
/*  128 */   private static final Map<Integer, VirtualZone> virtualZones = new HashMap<>();
/*      */   
/*  130 */   private static final Map<Integer, Map<Integer, Byte>> miningTiles = new Hashtable<>();
/*      */ 
/*      */   
/*      */   public static final int zoneSize = 64;
/*      */   
/*      */   public static final int zoneShifter = 6;
/*      */   
/*  137 */   private static final Set<Item> duelRings = new HashSet<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  143 */   public static final int worldTileSizeX = 1 << Constants.meshSize;
/*      */ 
/*      */ 
/*      */   
/*  147 */   public static final int worldTileSizeY = 1 << Constants.meshSize;
/*      */ 
/*      */ 
/*      */   
/*  151 */   public static final float worldMeterSizeX = ((worldTileSizeX - 1) * 4);
/*      */   
/*  153 */   public static final float worldMeterSizeY = ((worldTileSizeY - 1) * 4);
/*      */   
/*  155 */   public static boolean[][] protectedTiles = new boolean[worldTileSizeX][worldTileSizeY];
/*      */   
/*  157 */   static boolean[][] walkedTiles = new boolean[worldTileSizeX][worldTileSizeY];
/*      */   
/*  159 */   private static final byte[][] kingdoms = new byte[worldTileSizeX][worldTileSizeY];
/*      */   
/*  161 */   public static final int faithSizeX = worldTileSizeX >> 3;
/*      */   
/*  163 */   public static final int faithSizeY = worldTileSizeY >> 3;
/*      */   
/*      */   public static final int DOMAIN_DIVISION = 64;
/*      */   
/*  167 */   public static final int domainSizeX = worldTileSizeX / 64;
/*      */   
/*  169 */   public static final int domainSizeY = worldTileSizeY / 64;
/*      */   
/*      */   public static final int INFLUENCE_DIVISION = 256;
/*      */   
/*  173 */   public static final int influenceSizeX = worldTileSizeX / 256;
/*      */   
/*  175 */   public static final int influenceSizeY = worldTileSizeY / 256;
/*      */   
/*      */   public static final int HIVE_DIVISION = 32;
/*      */   
/*  179 */   public static final int hiveZoneSizeX = worldTileSizeX / 32;
/*      */   
/*  181 */   public static final int hiveZoneSizeY = worldTileSizeY / 32;
/*      */ 
/*      */   
/*      */   private static boolean hasLoadedChristmas = false;
/*      */   
/*  186 */   private static final Logger logger = Logger.getLogger(Zones.class.getName());
/*      */   
/*  188 */   private static int currentSaveZoneX = 0;
/*      */   
/*  190 */   private static int currentSaveZoneY = 0;
/*      */   
/*      */   private static boolean loading = false;
/*      */   
/*  194 */   public static int numberOfZones = 0;
/*      */   
/*      */   private static int rest;
/*      */   
/*      */   private static int maxRest;
/*      */   
/*      */   private static int zonesPerRun;
/*      */   
/*      */   private static boolean haslogged = false;
/*      */   
/*  204 */   private static int coverHolder = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  210 */   private static final FaithZone[][] surfaceDomains = new FaithZone[faithSizeX][faithSizeY];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  216 */   private static final FaithZone[][] caveDomains = new FaithZone[faithSizeX][faithSizeY];
/*      */   
/*  218 */   private static final LinkedList<Item> altars = new LinkedList<>();
/*      */   
/*  220 */   private static final ArrayList<HashMap<Item, FaithZone>> altarZones = new ArrayList<>();
/*      */   
/*  222 */   private static final ArrayList<HashMap<Item, InfluenceZone>> influenceZones = new ArrayList<>();
/*      */   
/*  224 */   private static final ArrayList<ConcurrentHashMap<Item, HiveZone>> hiveZones = new ArrayList<>();
/*      */   
/*  226 */   private static final ConcurrentHashMap<Item, TurretZone> turretZones = new ConcurrentHashMap<>();
/*      */   
/*  228 */   private static final byte[][] influenceCache = new byte[worldTileSizeX][worldTileSizeY];
/*      */   
/*  230 */   private static int pollnum = 0;
/*      */   
/*      */   private static int MESHSIZE;
/*      */   
/*      */   private static final String UPDATE_MININGTILE = "UPDATE MINING SET STATE=? WHERE TILEX=? AND TILEY=?";
/*      */   
/*      */   private static final String INSERT_MININGTILE = "INSERT INTO MINING (STATE,TILEX,TILEY) VALUES(?,?,?)";
/*      */   
/*      */   private static final String DELETE_MININGTILE = "DELETE FROM MINING WHERE TILEX=? AND TILEY=?";
/*      */   
/*      */   private static final String GET_ALL_MININGTILES = "SELECT * FROM MINING";
/*      */   
/*      */   private static final String GET_MININGTILE = "SELECT STATE FROM MINING WHERE TILEX=? AND TILEY=?";
/*      */   
/*  244 */   private static final LinkedList<Item> guardTowers = new LinkedList<>();
/*      */   
/*  246 */   private static final String protectedTileFile = ServerDirInfo.getFileDBPath() + File.separator + "protectedTiles.bmap";
/*      */ 
/*      */   
/*      */   public static boolean shouldCreateWarTargets = false;
/*      */ 
/*      */   
/*      */   public static boolean shouldSourceSprings = false;
/*      */ 
/*      */   
/*  255 */   private static Map<Byte, Float> landPercent = new HashMap<>();
/*      */   
/*  257 */   public static Creature evilsanta = null;
/*      */   
/*  259 */   public static Creature santa = null;
/*      */   
/*  261 */   public static Creature santaMolRehan = null;
/*      */ 
/*      */   
/*  264 */   public static final ConcurrentHashMap<Long, Creature> santas = new ConcurrentHashMap<>();
/*      */   
/*  266 */   static final Random zrand = new Random();
/*  267 */   private static int currentPollZoneX = zrand.nextInt(worldTileSizeX);
/*      */   
/*  269 */   private static int currentPollZoneY = zrand.nextInt(worldTileSizeY);
/*      */ 
/*      */   
/*      */   private static boolean devlog = false;
/*      */   
/*  274 */   private static final Object ZONE_SYNC_LOCK = new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  280 */   private static LinkedList<LongPosition> posmap = new LinkedList<>();
/*      */ 
/*      */   
/*      */   private static boolean hasStartedYet = false;
/*      */ 
/*      */   
/*      */   static {
/*      */     try {
/*  288 */       createZones();
/*      */       
/*  290 */       loadAllMiningTiles();
/*  291 */       SpawnTable.createEncounters();
/*  292 */       initializeWalkTiles();
/*  293 */       loadProtectedTiles();
/*      */       
/*  295 */       (Creatures.getInstance()).numberOfZonesX = worldTileSizeX >> 6;
/*      */       
/*  297 */       for (int i = 0; i < worldTileSizeX; i++) {
/*  298 */         for (int j = 0; j < worldTileSizeY; j++)
/*  299 */           influenceCache[i][j] = -1; 
/*      */       } 
/*  301 */     } catch (IOException ex) {
/*      */       
/*  303 */       logger.log(Level.SEVERE, "Failed to load zones!", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static void setLandPercent(byte kingdom, float percent) {
/*  309 */     landPercent.put(Byte.valueOf(kingdom), Float.valueOf(percent));
/*      */   }
/*      */ 
/*      */   
/*      */   public static float getPercentLandForKingdom(byte kingdom) {
/*  314 */     Float f = landPercent.get(Byte.valueOf(kingdom));
/*  315 */     if (f != null)
/*  316 */       return f.floatValue(); 
/*  317 */     return 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void saveProtectedTiles() {
/*  322 */     File f = new File(protectedTileFile);
/*      */     
/*      */     try {
/*  325 */       f.createNewFile();
/*      */     }
/*  327 */     catch (IOException iox) {
/*      */       
/*  329 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */     } 
/*      */     
/*      */     try {
/*  333 */       DataOutputStream ds = new DataOutputStream(new FileOutputStream(f));
/*  334 */       ObjectOutputStream oos = new ObjectOutputStream(ds);
/*  335 */       oos.writeObject(protectedTiles);
/*  336 */       oos.flush();
/*  337 */       oos.close();
/*  338 */       ds.close();
/*      */     }
/*  340 */     catch (IOException iox) {
/*      */       
/*  342 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void addGuardTowerInfluence(Item tower, boolean silent) {
/*  348 */     if (Features.Feature.NEW_KINGDOM_INF.isEnabled()) {
/*      */       
/*  350 */       if (influenceZones.isEmpty())
/*      */       {
/*  352 */         initInfluenceZones();
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  357 */       int actualZone = Math.max(0, tower.getTileY() / 256) * influenceSizeX + Math.max(0, tower.getTileX() / 256);
/*  358 */       HashMap<Item, InfluenceZone> thisZone = influenceZones.get(actualZone);
/*  359 */       if (thisZone == null) {
/*  360 */         thisZone = new HashMap<>();
/*      */       }
/*  362 */       InfluenceZone newZone = new InfluenceZone(tower);
/*      */       
/*  364 */       thisZone.put(tower, newZone);
/*  365 */       influenceZones.set(actualZone, thisZone);
/*      */       
/*  367 */       for (int i = (int)(tower.getTileX() - tower.getCurrentQualityLevel()); i < tower.getTileX() + tower.getCurrentQualityLevel(); i++) {
/*  368 */         for (int j = (int)(tower.getTileY() - tower.getCurrentQualityLevel()); j < tower.getTileY() + tower.getCurrentQualityLevel(); j++)
/*  369 */           influenceCache[i][j] = -1; 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static final void removeGuardTowerInfluence(Item tower, boolean silent) {
/*  375 */     if (Features.Feature.NEW_KINGDOM_INF.isEnabled()) {
/*      */       
/*  377 */       if (influenceZones.isEmpty())
/*      */       {
/*  379 */         initInfluenceZones();
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  384 */       int actualZone = Math.max(0, tower.getTileY() / 256) * influenceSizeX + Math.max(0, tower.getTileX() / 256);
/*  385 */       HashMap<Item, InfluenceZone> thisZone = influenceZones.get(actualZone);
/*  386 */       if (thisZone == null) {
/*      */         return;
/*      */       }
/*  389 */       thisZone.remove(tower);
/*      */       
/*  391 */       for (int i = (int)(tower.getTileX() - tower.getCurrentQualityLevel()); i < tower.getTileX() + tower.getCurrentQualityLevel(); i++) {
/*  392 */         for (int j = (int)(tower.getTileY() - tower.getCurrentQualityLevel()); j < tower.getTileY() + tower.getCurrentQualityLevel(); j++)
/*  393 */           influenceCache[i][j] = -1; 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static final byte getKingdom(int tilex, int tiley) {
/*  399 */     if (Servers.localServer.HOMESERVER) {
/*  400 */       return Servers.localServer.KINGDOM;
/*      */     }
/*  402 */     if (Features.Feature.NEW_KINGDOM_INF.isEnabled()) {
/*      */       
/*  404 */       if (influenceZones.isEmpty()) {
/*      */         
/*  406 */         initInfluenceZones();
/*      */         
/*  408 */         if (!guardTowers.isEmpty())
/*      */         {
/*  410 */           for (Item item : guardTowers)
/*      */           {
/*  412 */             addGuardTowerInfluence(item, true);
/*      */           }
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  418 */       if (influenceCache[safeTileX(tilex)][safeTileY(tiley)] != -1) {
/*  419 */         return influenceCache[safeTileX(tilex)][safeTileY(tiley)];
/*      */       }
/*  421 */       VolaTile t = getTileOrNull(tilex, tiley, true);
/*  422 */       if (t != null && t.getVillage() != null) {
/*  423 */         return (t.getVillage()).kingdom;
/*      */       }
/*  425 */       InfluenceZone toReturn = null;
/*  426 */       HashMap<Item, InfluenceZone> thisZone = null;
/*  427 */       for (int i = -1; i <= 1; i++) {
/*  428 */         for (int j = -1; j <= 1; j++) {
/*      */ 
/*      */           
/*  431 */           int actualZone = Math.max(0, Math.min(tiley / 256 + j, influenceSizeY - 1)) * influenceSizeX + Math.max(0, Math.min(tilex / 256 + i, influenceSizeX - 1));
/*  432 */           if (actualZone < influenceZones.size()) {
/*      */             
/*  434 */             thisZone = influenceZones.get(actualZone);
/*  435 */             if (thisZone != null)
/*      */             {
/*      */               
/*  438 */               for (InfluenceZone inf : thisZone.values()) {
/*      */                 
/*  440 */                 if (inf.containsTile(tilex, tiley)) {
/*      */                   
/*  442 */                   if (inf.getStrengthForTile(tilex, tiley, true) <= 0.0F) {
/*      */                     continue;
/*      */                   }
/*  445 */                   if (toReturn == null) {
/*  446 */                     toReturn = inf; continue;
/*  447 */                   }  if (toReturn.getStrengthForTile(tilex, tiley, true) <= inf.getStrengthForTile(tilex, tiley, true))
/*  448 */                     toReturn = inf; 
/*      */                 } 
/*      */               }  } 
/*      */           } 
/*      */         } 
/*  453 */       }  if (toReturn == null) {
/*  454 */         return 0;
/*      */       }
/*  456 */       influenceCache[tilex][tiley] = toReturn.getZoneItem().getKingdom();
/*  457 */       return toReturn.getZoneItem().getKingdom();
/*      */     } 
/*      */     
/*  460 */     return kingdoms[safeTileX(tilex)][safeTileY(tiley)];
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isKingdomBlocking(int tilex, int tiley, int endx, int endy, byte founderKingdom, int exclusionZoneX, int exclusionZoneY, int exclusionZoneEndX, int exclusionZoneEndY) {
/*  466 */     int startx = safeTileX(tilex);
/*  467 */     int starty = safeTileY(tiley);
/*  468 */     int ex = safeTileX(endx);
/*  469 */     int ey = safeTileY(endy);
/*      */     
/*  471 */     boolean hasExclusionZone = (exclusionZoneX != -1);
/*      */     
/*  473 */     int exclusionX = safeTileX(exclusionZoneX);
/*  474 */     int exclusionY = safeTileY(exclusionZoneY);
/*  475 */     int exclusionEndX = safeTileX(exclusionZoneEndX);
/*  476 */     int exclusionEndY = safeTileY(exclusionZoneEndY);
/*      */     
/*  478 */     for (int x = startx; x < ex; x++) {
/*      */       
/*  480 */       if (!hasExclusionZone || x < exclusionX || x > exclusionEndX)
/*      */       {
/*  482 */         for (int y = starty; y < ey; y++) {
/*      */           
/*  484 */           if (!hasExclusionZone || y < exclusionY || y > exclusionEndY)
/*      */           {
/*  486 */             if (getKingdom(x, y) != 0 && getKingdom(x, y) != founderKingdom)
/*      */             {
/*  488 */               return false; } 
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*  493 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isKingdomBlocking(int tilex, int tiley, int endx, int endy, byte founderKingdom) {
/*  499 */     return isKingdomBlocking(tilex, tiley, endx, endy, founderKingdom, -1, -1, -1, -1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isWithinDuelRing(int tilex, int tiley, int endx, int endy) {
/*  504 */     int startx = safeTileX(tilex);
/*  505 */     int starty = safeTileY(tiley);
/*  506 */     int ex = safeTileX(endx);
/*  507 */     int ey = safeTileY(endy);
/*      */     
/*  509 */     for (int x = startx; x < ex; x++) {
/*      */       
/*  511 */       for (int y = starty; y < ey; y++) {
/*      */         
/*  513 */         Item ring = isWithinDuelRing(x, y, true);
/*  514 */         if (ring != null)
/*      */         {
/*  516 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*  520 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void setKingdom(int tilex, int tiley, byte kingdom) {
/*  526 */     kingdoms[safeTileX(tilex)][safeTileY(tiley)] = kingdom;
/*  527 */     if (Server.getSecondsUptime() > 10) {
/*      */       
/*  529 */       setKingdomOn(tilex, tiley, kingdom, true);
/*  530 */       setKingdomOn(tilex, tiley, kingdom, false);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void setKingdomOn(int tilex, int tiley, byte kingdom, boolean onSurface) {
/*  536 */     VolaTile t = getTileOrNull(tilex, tiley, onSurface);
/*  537 */     if (t != null) {
/*      */       
/*  539 */       Creature[] crets = t.getCreatures();
/*  540 */       for (Creature c : crets) {
/*  541 */         c.setCurrentKingdom(kingdom);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public static final void setKingdom(int tilex, int tiley, int sizex, int sizey, byte kingdom) {
/*  547 */     for (int x = tilex; x < tilex + sizex; x++) {
/*      */       
/*  549 */       for (int y = tiley; y < tiley + sizey; y++) {
/*  550 */         kingdoms[safeTileX(x)][safeTileY(y)] = kingdom;
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void loadProtectedTiles() {
/*  556 */     logger.info("Loading protected tiles from file: " + protectedTileFile);
/*  557 */     long start = System.nanoTime();
/*      */     
/*  559 */     File f = new File(protectedTileFile);
/*      */     
/*      */     try {
/*  562 */       if (f.createNewFile())
/*      */       {
/*  564 */         saveProtectedTiles();
/*  565 */         logger.log(Level.INFO, "Created first instance of protected tiles file.");
/*      */       }
/*      */     
/*  568 */     } catch (IOException iox) {
/*      */       
/*  570 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */     } 
/*      */     
/*      */     try {
/*  574 */       DataInputStream ds = new DataInputStream(new FileInputStream(f));
/*  575 */       ObjectInputStream oos = new ObjectInputStream(ds);
/*  576 */       boolean[][] protectedTileArr = (boolean[][])oos.readObject();
/*  577 */       oos.close();
/*  578 */       ds.close();
/*  579 */       boolean[][] tmpTiles = new boolean[worldTileSizeX][worldTileSizeY];
/*  580 */       int protectedt = 0;
/*  581 */       int wtx = worldTileSizeX;
/*  582 */       int wty = worldTileSizeY;
/*      */ 
/*      */       
/*      */       try {
/*  586 */         for (int x = 0; x < wtx; x++) {
/*      */           
/*  588 */           for (int y = 0; y < wty; y++) {
/*      */             
/*  590 */             tmpTiles[x][y] = protectedTileArr[x][y];
/*  591 */             if (tmpTiles[x][y])
/*  592 */               protectedt++; 
/*      */           } 
/*      */         } 
/*  595 */         protectedTiles = tmpTiles;
/*      */       }
/*  597 */       catch (Exception ex) {
/*      */ 
/*      */         
/*  600 */         for (int x = 0; x < wtx; x++) {
/*      */           
/*  602 */           for (int y = 0; y < wty; y++)
/*      */           {
/*  604 */             protectedTiles[x][y] = false;
/*      */           }
/*      */         } 
/*  607 */         f.delete();
/*      */       } 
/*  609 */       logger.log(Level.INFO, "Loaded " + protectedt + " protected tiles. It took " + ((float)(System.nanoTime() - start) / 1000000.0F) + " ms.");
/*      */     
/*      */     }
/*  612 */     catch (IOException iox) {
/*      */       
/*  614 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */     }
/*  616 */     catch (ClassNotFoundException nsc) {
/*      */       
/*  618 */       logger.log(Level.WARNING, nsc.getMessage(), nsc);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isTileProtected(int tilex, int tiley) {
/*  624 */     return protectedTiles[tilex][tiley];
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isTileCornerProtected(int tilex, int tiley) {
/*  629 */     for (int x = 0; x <= 1; x++) {
/*      */       
/*  631 */       for (int y = 0; y <= 1; y++) {
/*      */         
/*  633 */         if (isTileProtected(tilex - x, tiley - y))
/*      */         {
/*  635 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*  639 */     return false;
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
/*      */   static void initializeWalkTiles() {
/*  651 */     logger.info("Initialising walked tiles");
/*  652 */     long start = System.nanoTime();
/*  653 */     int wsx = worldTileSizeX;
/*  654 */     int wsy = worldTileSizeY;
/*  655 */     boolean[][] tmptiles = new boolean[wsx][wsy];
/*  656 */     for (int x = 0; x < wsx; x++) {
/*      */       
/*  658 */       for (int y = 0; y < wsy; y++)
/*      */       {
/*  660 */         tmptiles[x][y] = true;
/*      */       }
/*      */     } 
/*  663 */     walkedTiles = tmptiles;
/*  664 */     logger.log(Level.INFO, "Initialised walked tiles. It took " + ((float)(System.nanoTime() - start) / 1000000.0F) + " ms.");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void addGuardTower(Item tower) {
/*  670 */     if (loading) {
/*  671 */       guardTowers.add(tower);
/*      */     } else {
/*  673 */       Kingdoms.addTower(tower);
/*      */     } 
/*  675 */     addGuardTowerInfluence(tower, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void removeGuardTower(Item tower) {
/*  680 */     guardTowers.remove(tower);
/*  681 */     removeGuardTowerInfluence(tower, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public static LinkedList<Item> getGuardTowers() {
/*  686 */     return guardTowers;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void initInfluenceZones() {
/*  691 */     if (!influenceZones.isEmpty()) {
/*      */       return;
/*      */     }
/*  694 */     for (int y = 0; y < influenceSizeY; y++) {
/*  695 */       for (int x = 0; x < influenceSizeX; x++)
/*  696 */         influenceZones.add(null); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void loadTowers() {
/*  701 */     logger.info("Loading guard towers.");
/*  702 */     long now = System.nanoTime();
/*      */     
/*  704 */     if (Features.Feature.NEW_KINGDOM_INF.isEnabled())
/*      */     {
/*  706 */       if (influenceZones.isEmpty()) {
/*  707 */         initInfluenceZones();
/*      */       }
/*      */     }
/*  710 */     for (ListIterator<Item> it = guardTowers.listIterator(); it.hasNext(); ) {
/*      */       
/*  712 */       Item gt = it.next();
/*  713 */       Kingdom k = Kingdoms.getKingdom(gt.getAuxData());
/*  714 */       it.remove();
/*  715 */       if (!k.existsHere()) {
/*      */         
/*  717 */         logger.log(Level.INFO, "Removing tower for non-existent kingdom of " + k.getName());
/*  718 */         Kingdoms.destroyTower(gt, true);
/*      */         continue;
/*      */       } 
/*  721 */       Kingdoms.addTower(gt);
/*      */     } 
/*      */     
/*  724 */     if (Features.Feature.NEW_KINGDOM_INF.isEnabled())
/*      */     {
/*      */ 
/*      */       
/*  728 */       for (int i = 0; i < worldTileSizeX; i++) {
/*  729 */         for (int j = 0; j < worldTileSizeY; j++)
/*  730 */           getKingdom(i, j); 
/*      */       } 
/*      */     }
/*  733 */     logger.log(Level.INFO, "Loaded " + Kingdoms.getNumberOfGuardTowers() + " Guard towers. That took " + (
/*  734 */         (float)(System.nanoTime() - now) / 1000000.0F) + " ms.");
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int getCoverHolder() {
/*  739 */     return coverHolder;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void resetCoverHolder() {
/*  744 */     coverHolder = 0;
/*      */   }
/*      */   
/*  747 */   private static long lastCounted = 0L;
/*      */ 
/*      */   
/*      */   public static void calculateZones(boolean overRide) {
/*  751 */     if (System.currentTimeMillis() - lastCounted > 60000L || overRide) {
/*      */       
/*  753 */       landPercent.clear();
/*  754 */       long now = System.currentTimeMillis();
/*  755 */       int[] zoneControl = new int[255];
/*  756 */       for (int x = 0; x < worldTileSizeX; x++) {
/*      */         
/*  758 */         for (int y = 0; y < worldTileSizeY; y++) {
/*      */           
/*  760 */           byte kingdom = getKingdom(x, y);
/*  761 */           zoneControl[kingdom & 0xFF] = zoneControl[kingdom & 0xFF] + 1;
/*      */         } 
/*      */       } 
/*  764 */       lastCounted = System.currentTimeMillis();
/*  765 */       long numberOfTiles = (worldTileSizeX * worldTileSizeY);
/*  766 */       for (int i = 0; i < 255; i++) {
/*      */         
/*  768 */         if (zoneControl[i] > 0)
/*      */         {
/*  770 */           setLandPercent((byte)i, zoneControl[i] * 100.0F / (float)numberOfTiles);
/*      */         }
/*      */       } 
/*  773 */       if (System.currentTimeMillis() - now > 1000L)
/*      */       {
/*  775 */         logger.log(Level.INFO, "Calculating zones took " + (System.currentTimeMillis() - now) + " millis");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static void addAltar(Item altar, boolean silent) {
/*  782 */     if (Features.Feature.NEWDOMAINS.isEnabled()) {
/*      */ 
/*      */ 
/*      */       
/*  786 */       int actualZone = Math.max(0, altar.getTileY() / 64) * domainSizeX + Math.max(0, altar.getTileX() / 64);
/*  787 */       HashMap<Item, FaithZone> thisZone = altarZones.get(actualZone);
/*  788 */       if (thisZone == null) {
/*  789 */         thisZone = new HashMap<>();
/*      */       }
/*  791 */       FaithZone newZone = new FaithZone(altar);
/*  792 */       FaithZone oldZone = null;
/*  793 */       VolaTile tempTile = null;
/*      */       
/*  795 */       thisZone.put(altar, newZone);
/*  796 */       altarZones.set(actualZone, thisZone);
/*      */       
/*  798 */       if (!silent && newZone.getCurrentRuler() != null) {
/*      */         
/*      */         try {
/*  801 */           for (int i = newZone.getStartX(); i < newZone.getEndX(); i++) {
/*  802 */             for (int j = newZone.getStartY(); j < newZone.getEndY(); j++) {
/*      */               
/*  804 */               tempTile = getTileOrNull(i, j, altar.isOnSurface());
/*  805 */               if (tempTile != null) {
/*      */                 
/*  807 */                 oldZone = getFaithZone(i, j, altar.isOnSurface());
/*      */                 
/*  809 */                 if (newZone.getStrengthForTile(i, j, altar.isOnSurface()) > 0)
/*      */                 {
/*      */                   
/*  812 */                   if (oldZone == null)
/*      */                   
/*  814 */                   { tempTile.broadCast("The domain of " + newZone.getCurrentRuler().getName() + " has now reached this place.");
/*      */                      }
/*      */                   
/*  817 */                   else if (oldZone.getStrengthForTile(i, j, altar.isOnSurface()) < newZone.getStrengthForTile(i, j, altar.isOnSurface()))
/*      */                   
/*  819 */                   { tempTile.broadCast(newZone.getCurrentRuler().getName() + "'s domain is now the strongest here!"); }  } 
/*      */               } 
/*      */             } 
/*      */           } 
/*  823 */         } catch (NoSuchZoneException e) {
/*  824 */           logger.log(Level.WARNING, "Error getting existing zones when adding new altar.");
/*      */         } 
/*      */       }
/*      */     } else {
/*      */       
/*  829 */       altars.add(altar);
/*      */     } 
/*      */   }
/*      */   
/*      */   static void removeAltar(Item altar, boolean silent) {
/*  834 */     if (Features.Feature.NEWDOMAINS.isEnabled()) {
/*      */ 
/*      */ 
/*      */       
/*  838 */       int actualZone = Math.max(0, altar.getTileY() / 64) * domainSizeX + Math.max(0, altar.getTileX() / 64);
/*  839 */       HashMap<Item, FaithZone> thisZone = altarZones.get(actualZone);
/*  840 */       if (thisZone == null) {
/*      */         
/*  842 */         logger.log(Level.WARNING, "AltarZone was NULL when it should not have been: " + actualZone);
/*      */         
/*      */         return;
/*      */       } 
/*  846 */       FaithZone oldZone = thisZone.remove(altar);
/*  847 */       FaithZone newZone = null;
/*  848 */       VolaTile tempTile = null;
/*      */       
/*  850 */       if (!silent && oldZone != null && oldZone.getCurrentRuler() != null) {
/*      */         
/*      */         try {
/*  853 */           for (int i = oldZone.getStartX(); i < oldZone.getEndX(); i++) {
/*  854 */             for (int j = oldZone.getStartY(); j < oldZone.getEndY(); j++) {
/*      */               
/*  856 */               tempTile = getTileOrNull(i, j, altar.isOnSurface());
/*  857 */               if (tempTile != null) {
/*      */                 
/*  859 */                 newZone = getFaithZone(i, j, altar.isOnSurface());
/*  860 */                 if (newZone == null)
/*      */                 
/*  862 */                 { tempTile.broadCast(oldZone.getCurrentRuler().getName() + " has had to lose " + oldZone
/*  863 */                       .getCurrentRuler().getHisHerItsString() + " hold over this area!"); }
/*      */                 
/*      */                 else
/*      */                 
/*  867 */                 { tempTile.broadCast(newZone.getCurrentRuler().getName() + "'s domain is now the strongest here!"); } 
/*      */               } 
/*      */             } 
/*      */           } 
/*  871 */         } catch (NoSuchZoneException e) {
/*  872 */           logger.log(Level.WARNING, "Error getting existing zones when adding new altar.");
/*      */         } 
/*      */       }
/*      */     } else {
/*      */       
/*  877 */       altars.remove(altar);
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void calcCreatures(Creature responder) {
/*  882 */     int visible = 0;
/*  883 */     int offline = 0;
/*  884 */     int total = 0;
/*  885 */     int submerged = 0;
/*  886 */     int surfbelowsurface = 0;
/*  887 */     int cavebelowsurface = 0;
/*      */     int x;
/*  889 */     for (x = 0; x < worldTileSizeX >> 6; x++) {
/*      */       
/*  891 */       for (int y = 0; y < worldTileSizeY >> 6; y++) {
/*      */         
/*  893 */         Creature[] crets = surfaceZones[x][y].getAllCreatures();
/*  894 */         for (int c = 0; c < crets.length; c++) {
/*      */           
/*  896 */           total++;
/*  897 */           if (crets[c].isVisible())
/*  898 */             visible++; 
/*  899 */           if (crets[c].isOffline()) {
/*  900 */             offline++;
/*      */           }
/*  902 */           if (crets[c].getPositionZ() < -10.0F)
/*  903 */             surfbelowsurface++; 
/*      */         } 
/*      */       } 
/*      */     } 
/*  907 */     for (x = 0; x < worldTileSizeX >> 6; x++) {
/*      */       
/*  909 */       for (int y = 0; y < worldTileSizeY >> 6; y++) {
/*      */         
/*  911 */         Creature[] crets = caveZones[x][y].getAllCreatures();
/*  912 */         for (int c = 0; c < crets.length; c++) {
/*      */           
/*  914 */           total++;
/*  915 */           submerged++;
/*  916 */           if (crets[c].isVisible())
/*  917 */             visible++; 
/*  918 */           if (crets[c].isOffline())
/*  919 */             offline++; 
/*  920 */           if (crets[c].getPositionZ() < -10.0F)
/*  921 */             cavebelowsurface++; 
/*      */         } 
/*      */       } 
/*      */     } 
/*  925 */     responder.getCommunicator().sendNormalServerMessage("Creatures total:" + total + ", On surface=" + (total - submerged) + " (of which " + surfbelowsurface + " are below -10 meters), in Caves=" + submerged + " (of which " + cavebelowsurface + " are below -10 meters), visible=" + visible + ", offline=" + offline + ".");
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
/*      */   public static Item[] getAltars() {
/*  937 */     return altars.<Item>toArray(new Item[altars.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Item[] getAltars(int deityId) {
/*  943 */     Set<Item> lAltars = new HashSet<>();
/*  944 */     if (Features.Feature.NEWDOMAINS.isEnabled()) {
/*      */       
/*  946 */       for (HashMap<Item, FaithZone> m : altarZones) {
/*  947 */         if (m != null)
/*      */         {
/*  949 */           for (Item altar : m.keySet()) {
/*      */             
/*  951 */             if (altar == null) {
/*      */               continue;
/*      */             }
/*  954 */             Deity deity = altar.getBless();
/*  955 */             if ((deity == null && deityId == 0) || (deity != null && deity
/*  956 */               .getNumber() == deityId))
/*      */             {
/*  958 */               lAltars.add(altar);
/*      */             }
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } else {
/*      */       
/*  965 */       for (Item altar : altars) {
/*      */         
/*  967 */         Deity deity = altar.getBless();
/*  968 */         if ((deity == null && deityId == 0) || (deity != null && deity
/*  969 */           .getNumber() == deityId))
/*      */         {
/*  971 */           lAltars.add(altar);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  976 */     return lAltars.<Item>toArray(new Item[lAltars.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void checkAltars() {
/*  981 */     long now = 0L;
/*  982 */     if (logger.isLoggable(Level.FINER)) {
/*      */       
/*  984 */       logger.finer("Checking altars.");
/*  985 */       now = System.nanoTime();
/*      */     } 
/*      */     
/*  988 */     for (Item altar : altars)
/*      */     {
/*  990 */       addToDomains(altar);
/*      */     }
/*      */     
/*  993 */     if (logger.isLoggable(Level.FINEST)) {
/*      */       
/*  995 */       int numberOfAltars = altars.size();
/*  996 */       logger.log(Level.FINEST, "Checked " + numberOfAltars + " altars. That took " + ((float)(System.nanoTime() - now) / 1000000.0F) + " ms.");
/*      */     } 
/*      */     
/*  999 */     if (shouldCreateWarTargets) {
/*      */ 
/*      */       
/* 1002 */       shouldCreateWarTargets = false;
/* 1003 */       int x = worldTileSizeX / 6;
/* 1004 */       int y = worldTileSizeY / 6;
/* 1005 */       createCampsOnLine(x, y);
/* 1006 */       y = worldTileSizeY / 2;
/* 1007 */       createCampsOnLine(x, y);
/* 1008 */       y += worldTileSizeY / 6;
/* 1009 */       createCampsOnLine(x, y);
/*      */     } 
/*      */     
/* 1012 */     if (shouldSourceSprings) {
/*      */       
/* 1014 */       shouldSourceSprings = false;
/* 1015 */       createSprings();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void addWarDomains() {
/* 1021 */     Item[] targs = Items.getWarTargets();
/* 1022 */     if (targs != null && targs.length > 0)
/*      */     {
/* 1024 */       for (Item target : targs)
/*      */       {
/* 1026 */         Kingdoms.addWarTargetKingdom(target);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void createSprings() {
/* 1034 */     int springs = worldTileSizeX / 50;
/* 1035 */     for (int a = 0; a < springs; a++) {
/*      */       
/* 1037 */       boolean found = false;
/* 1038 */       int tries = 0;
/* 1039 */       logger.log(Level.INFO, "Trying to create spring " + a);
/* 1040 */       while (!found && tries < 1000) {
/*      */         
/* 1042 */         tries++;
/* 1043 */         int tx = worldTileSizeX / 3 + Server.rand.nextInt(worldTileSizeY / 3);
/* 1044 */         int ty = worldTileSizeX / 3 + Server.rand.nextInt(worldTileSizeY / 3);
/*      */         
/* 1046 */         int tile = Server.surfaceMesh.getTile(tx, ty);
/* 1047 */         if (Tiles.decodeHeight(tile) > 5) {
/*      */           
/*      */           try {
/*      */             
/* 1051 */             int type = 766;
/* 1052 */             if (Server.rand.nextBoolean()) {
/* 1053 */               type = 767;
/*      */             }
/* 1055 */             Item target1 = ItemFactory.createItem(type, 100.0F, (tx * 4 + 2), (ty * 4 + 2), Server.rand
/* 1056 */                 .nextInt(360), true, (byte)0, -10L, "");
/*      */             
/* 1058 */             target1.setSizes(target1.getSizeX() + Server.rand.nextInt(1), target1
/* 1059 */                 .getSizeY() + Server.rand.nextInt(2), target1.getSizeZ() + Server.rand.nextInt(3));
/* 1060 */             logger.log(Level.INFO, "Created " + target1.getName() + " at " + target1.getTileX() + " " + target1
/* 1061 */                 .getTileY() + " sizes " + target1.getSizeX() + "," + target1.getSizeY() + "," + target1
/* 1062 */                 .getSizeZ() + ")");
/* 1063 */             Items.addSourceSpring(target1);
/* 1064 */             found = true;
/*      */           }
/* 1066 */           catch (FailedException fe) {
/*      */             
/* 1068 */             logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*      */           }
/* 1070 */           catch (NoSuchTemplateException nst) {
/*      */             
/* 1072 */             logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void createCampsOnLine(int x, int y) {
/* 1081 */     Village[] villages = Villages.getVillages();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1086 */     boolean found = false;
/* 1087 */     int tries = 0;
/* 1088 */     logger.log(Level.INFO, "Trying to create camp at " + x + "," + y);
/* 1089 */     while (!found && tries < 1000) {
/*      */       
/* 1091 */       tries++;
/* 1092 */       int tx = safeTileY(x + Server.rand.nextInt(worldTileSizeX / 3));
/* 1093 */       int ty = safeTileY(y + Server.rand.nextInt(worldTileSizeY / 3));
/* 1094 */       boolean inVillage = false;
/* 1095 */       for (Village v : villages) {
/*      */         
/* 1097 */         if (v.coversWithPerimeterAndBuffer(tx, ty, 60)) {
/*      */           
/* 1099 */           inVillage = true;
/*      */           break;
/*      */         } 
/*      */       } 
/* 1103 */       if (!inVillage) {
/*      */         
/* 1105 */         int tile = Server.surfaceMesh.getTile(tx, ty);
/* 1106 */         if (Tiles.decodeHeight(tile) > 2)
/*      */         {
/* 1108 */           if (Terraforming.isAllCornersInsideHeightRange(tx, ty, true, (short)(Tiles.decodeHeight(tile) + 5), 
/* 1109 */               (short)(Tiles.decodeHeight(tile) - 5))) {
/*      */             
/*      */             try {
/*      */               
/* 1113 */               int type = Server.rand.nextInt(3);
/* 1114 */               if (type == 0) {
/* 1115 */                 type = 760;
/* 1116 */               } else if (type == 1) {
/* 1117 */                 type = 762;
/* 1118 */               } else if (type == 2) {
/* 1119 */                 type = 761;
/* 1120 */               }  Item target1 = ItemFactory.createItem(type, 100.0F, (tx * 4 + 2), (ty * 4 + 2), Server.rand
/* 1121 */                   .nextInt(360), true, (byte)0, -10L, "");
/* 1122 */               target1.setName(createTargName(target1.getName()));
/* 1123 */               logger.log(Level.INFO, "Created " + target1.getName() + " at " + target1.getTileX() + " " + target1
/* 1124 */                   .getTileY());
/* 1125 */               Items.addWarTarget(target1);
/* 1126 */               new FocusZone(tx - 60, tx + 60, ty - 60, ty + 60, (byte)7, target1
/*      */ 
/*      */                   
/* 1129 */                   .getName(), "", true);
/* 1130 */               found = true;
/*      */             
/*      */             }
/* 1133 */             catch (FailedException fe) {
/*      */               
/* 1135 */               logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*      */             }
/* 1137 */             catch (NoSuchTemplateException nst) {
/*      */               
/* 1139 */               logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */             } 
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/* 1145 */     if (x == 0) {
/* 1146 */       x = worldTileSizeX / 2 - worldTileSizeX / 6;
/*      */     } else {
/* 1148 */       x = worldTileSizeX / 2 + worldTileSizeX / 6;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void createBattleCamp(int tx, int ty) {
/*      */     try {
/* 1156 */       int type = Server.rand.nextInt(3);
/* 1157 */       if (type == 0) {
/* 1158 */         type = 760;
/* 1159 */       } else if (type == 1) {
/* 1160 */         type = 762;
/* 1161 */       } else if (type == 2) {
/* 1162 */         type = 761;
/* 1163 */       }  Item target1 = ItemFactory.createItem(type, 100.0F, (tx * 4 + 2), (ty * 4 + 2), Server.rand
/* 1164 */           .nextInt(360), true, (byte)0, -10L, "");
/* 1165 */       target1.setName(createTargName(target1.getName()));
/* 1166 */       logger.log(Level.INFO, "Created " + target1.getName() + " at " + target1.getTileX() + " " + target1
/* 1167 */           .getTileY());
/* 1168 */       Items.addWarTarget(target1);
/* 1169 */       new FocusZone(tx - 60, tx + 60, ty - 60, ty + 60, (byte)7, target1
/*      */ 
/*      */           
/* 1172 */           .getName(), "", true);
/*      */     }
/* 1174 */     catch (FailedException fe) {
/*      */       
/* 1176 */       logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*      */     }
/* 1178 */     catch (NoSuchTemplateException nst) {
/*      */       
/* 1180 */       logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static final String createTargName(String origName) {
/* 1186 */     switch (Server.rand.nextInt(30)) {
/*      */       
/*      */       case 0:
/* 1189 */         return origName + " Unicorn One";
/*      */       case 1:
/* 1191 */         return origName + " Deepwoods";
/*      */       case 2:
/* 1193 */         return origName + " Goldbar";
/*      */       case 3:
/* 1195 */         return origName + " Stinger";
/*      */       case 4:
/* 1197 */         return origName + " Forefront";
/*      */       case 5:
/* 1199 */         return origName + " Pike Hill";
/*      */       case 6:
/* 1201 */         return origName + " Glory Day";
/*      */       case 7:
/* 1203 */         return origName + " Silver Anchor";
/*      */       case 8:
/* 1205 */         return origName + " Bloody Tip";
/*      */       case 9:
/* 1207 */         return origName + " Goreplain";
/*      */       case 10:
/* 1209 */         return origName + " Of The Bull";
/*      */       case 11:
/* 1211 */         return origName + " Muddyknee";
/*      */       case 12:
/* 1213 */         return origName + " First Fist";
/*      */       case 13:
/* 1215 */         return origName + " Golden Day";
/*      */       case 14:
/* 1217 */         return origName + " Stone Valley";
/*      */       case 15:
/* 1219 */         return origName + " New Day";
/*      */       case 16:
/* 1221 */         return origName + " Ramona Hill";
/*      */       case 17:
/* 1223 */         return "The High " + origName;
/*      */       case 18:
/* 1225 */         return "The " + origName + " of Spite";
/*      */       case 19:
/* 1227 */         return "The Trolls " + origName;
/*      */       case 20:
/* 1229 */         return "Diamond " + origName;
/*      */       case 21:
/* 1231 */         return "Silver " + origName;
/*      */       case 22:
/* 1233 */         return "Jackal's " + origName;
/*      */       case 23:
/* 1235 */         return "Stonefort " + origName;
/*      */       case 24:
/* 1237 */         return "Three rings " + origName;
/*      */       case 25:
/* 1239 */         return "Fifteen Tears " + origName;
/*      */       case 26:
/* 1241 */         return "Final Days " + origName;
/*      */       case 27:
/* 1243 */         return "Victory " + origName;
/*      */       case 28:
/* 1245 */         return "Cappa Cat " + origName;
/*      */       case 29:
/* 1247 */         return "Headstrong " + origName;
/*      */       case 30:
/* 1249 */         return "No Surrender " + origName;
/*      */     } 
/* 1251 */     return "No Way Back " + origName;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final FaithZone[][] getFaithZones(boolean surfaced) {
/* 1257 */     if (surfaced) {
/* 1258 */       return surfaceDomains;
/*      */     }
/* 1260 */     return caveDomains;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void addToDomains(Item item) {
/* 1265 */     if (item.getData1() != 0) {
/*      */       
/* 1267 */       Deity deity = item.getBless();
/* 1268 */       if (deity != null) {
/*      */         
/* 1270 */         int tilex = item.getTileX();
/* 1271 */         int tiley = item.getTileY();
/*      */ 
/*      */         
/* 1274 */         int ql = (int)(Servers.localServer.isChallengeServer() ? (item.getCurrentQualityLevel() / 3.0F) : item.getCurrentQualityLevel());
/* 1275 */         int minx = Math.max(0, tilex - ql);
/* 1276 */         int miny = Math.max(0, tiley - ql);
/* 1277 */         int maxx = Math.min(worldTileSizeX - 1, tilex + ql);
/* 1278 */         int maxy = Math.min(worldTileSizeY - 1, tiley + ql);
/* 1279 */         FaithZone[] lCoveredFaithZones = getFaithZonesCoveredBy(minx, miny, maxx, maxy, item.isOnSurface());
/*      */         
/* 1281 */         if (lCoveredFaithZones != null)
/*      */         {
/* 1283 */           for (int x = 0; x < lCoveredFaithZones.length; x++) {
/*      */             
/* 1285 */             int dist = Math.max(Math.abs(tilex - lCoveredFaithZones[x].getCenterX()), 
/* 1286 */                 Math.abs(tiley - lCoveredFaithZones[x].getCenterY()));
/* 1287 */             if (100 - dist > 0)
/*      */             {
/* 1289 */               lCoveredFaithZones[x].addToFaith(deity, Math.min(ql, 100 - dist));
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
/*      */   public static void fixTrees() {
/* 1302 */     logger.log(Level.INFO, "Fixing trees.");
/* 1303 */     int found = 0;
/*      */     
/* 1305 */     MeshIO mesh = Server.surfaceMesh;
/* 1306 */     Random random = new Random();
/* 1307 */     int ms = Constants.meshSize;
/* 1308 */     int max = 1 << ms;
/* 1309 */     for (int x = 0; x < max; x++) {
/*      */       
/* 1311 */       for (int y = 0; y < max; y++) {
/*      */         
/* 1313 */         int tile = mesh.getTile(x, y);
/* 1314 */         byte type = Tiles.decodeType(tile);
/* 1315 */         byte data = Tiles.decodeData(tile);
/* 1316 */         Tiles.Tile theTile = Tiles.getTile(type);
/*      */ 
/*      */         
/* 1319 */         if (type == Tiles.Tile.TILE_TREE.id || type == Tiles.Tile.TILE_BUSH.id || type == Tiles.Tile.TILE_ENCHANTED_TREE.id || type == Tiles.Tile.TILE_ENCHANTED_BUSH.id || type == Tiles.Tile.TILE_MYCELIUM_TREE.id || type == Tiles.Tile.TILE_MYCELIUM_BUSH.id) {
/*      */           byte newType;
/*      */ 
/*      */           
/* 1323 */           found++;
/*      */           
/* 1325 */           byte newLen = (byte)(1 + random.nextInt(3));
/* 1326 */           byte age = FoliageAge.getAgeAsByte(data);
/* 1327 */           byte newData = Tiles.encodeTreeData(age, false, false, newLen);
/*      */           
/* 1329 */           if (type == Tiles.Tile.TILE_TREE.id) {
/* 1330 */             newType = theTile.getTreeType(data).asNormalTree();
/* 1331 */           } else if (type == Tiles.Tile.TILE_ENCHANTED_TREE.id) {
/* 1332 */             newType = theTile.getTreeType(data).asEnchantedTree();
/* 1333 */           } else if (type == Tiles.Tile.TILE_MYCELIUM_TREE.id) {
/* 1334 */             newType = theTile.getTreeType(data).asMyceliumTree();
/* 1335 */           } else if (type == Tiles.Tile.TILE_BUSH.id) {
/* 1336 */             newType = theTile.getBushType(data).asNormalBush();
/* 1337 */           } else if (type == Tiles.Tile.TILE_ENCHANTED_BUSH.id) {
/* 1338 */             newType = theTile.getBushType(data).asEnchantedBush();
/*      */           } else {
/*      */             
/* 1341 */             newType = theTile.getBushType(data).asMyceliumBush();
/* 1342 */           }  mesh.setTile(x, y, Tiles.encode(Tiles.decodeHeight(tile), newType, newData));
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 1349 */       mesh.saveAll();
/* 1350 */       logger.log(Level.INFO, "Set " + found + " trees to new type.");
/*      */     }
/* 1352 */     catch (IOException iox) {
/*      */       
/* 1354 */       logger.log(Level.WARNING, "Failed to fix trees", iox);
/*      */     } 
/*      */     
/* 1357 */     Constants.RUNBATCH = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void flash() {
/* 1367 */     int tilex = Server.rand.nextInt(worldTileSizeX);
/* 1368 */     int tiley = Server.rand.nextInt(worldTileSizeY);
/* 1369 */     flash(tilex, tiley, true);
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
/*      */   public static void flash(int tilex, int tiley, boolean doDamage) {
/* 1388 */     if (logger.isLoggable(Level.FINEST))
/*      */     {
/* 1390 */       logger.finest("Flashing tile at " + tilex + ", " + tiley + ", damage: " + doDamage);
/*      */     }
/* 1392 */     int tile = Server.surfaceMesh.getTile(tilex, tiley);
/* 1393 */     float height = Math.max(0.0F, Tiles.decodeHeightAsFloat(tile));
/*      */     
/* 1395 */     Players.getInstance().weatherFlash(tilex, tiley, height);
/* 1396 */     if (doDamage) {
/*      */       
/* 1398 */       VolaTile t = getTileOrNull(tilex, tiley, true);
/* 1399 */       if (t != null)
/*      */       {
/*      */ 
/*      */         
/* 1403 */         t.flashStrike();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void flashSpell(int tilex, int tiley, float baseDamage, Creature caster) {
/* 1410 */     if (logger.isLoggable(Level.FINEST))
/*      */     {
/* 1412 */       logger.finest("Flashing tile at " + tilex + ", " + tiley);
/*      */     }
/* 1414 */     int tile = Server.surfaceMesh.getTile(tilex, tiley);
/* 1415 */     float height = Math.max(0.0F, Tiles.decodeHeightAsFloat(tile));
/*      */     
/* 1417 */     Players.getInstance().weatherFlash(tilex, tiley, height);
/*      */     
/* 1419 */     VolaTile t = getTileOrNull(tilex, tiley, true);
/* 1420 */     if (t != null)
/*      */     {
/*      */ 
/*      */       
/* 1424 */       t.lightningStrikeSpell(baseDamage, caster);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void fixCaveResources() {
/* 1434 */     int ms = Constants.meshSize;
/* 1435 */     int min = 0;
/* 1436 */     int max = 1 << ms;
/* 1437 */     int count = 0;
/* 1438 */     long now = System.nanoTime();
/* 1439 */     for (int x = 0; x < max; x++) {
/*      */       
/* 1441 */       for (int y = 0; y < max; y++) {
/*      */         
/* 1443 */         if (Server.getCaveResource(x, y) == 0) {
/*      */           
/* 1445 */           count++;
/* 1446 */           Server.setCaveResource(x, y, 65535);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*      */     try {
/* 1452 */       Server.resourceMesh.saveAll();
/*      */     }
/* 1454 */     catch (IOException iox) {
/*      */       
/* 1456 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */     } 
/* 1458 */     logger.log(Level.INFO, "Fixed " + count + " cave resources. It took " + ((float)(System.nanoTime() - now) / 1000000.0F) + " ms.");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void createInvestigatables() {
/* 1464 */     int ms = Constants.meshSize;
/* 1465 */     int min = 0;
/* 1466 */     int max = 1 << ms;
/* 1467 */     for (int x = 0; x < max; x++) {
/*      */       
/* 1469 */       for (int y = 0; y < max; y++) {
/*      */         
/* 1471 */         if (Server.rand.nextFloat() < 0.6F) {
/* 1472 */           Server.setInvestigatable(x, y, true);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static final void createSeeds() {
/* 1479 */     MeshIO mesh = Server.surfaceMesh;
/* 1480 */     int ms = Constants.meshSize;
/* 1481 */     int min = 0;
/* 1482 */     int max = 1 << ms;
/* 1483 */     int count = 0;
/* 1484 */     long now = System.nanoTime();
/* 1485 */     for (int x = 0; x < max; x++) {
/*      */       
/* 1487 */       for (int y = 0; y < max; y++) {
/*      */         
/* 1489 */         int tile = mesh.getTile(x, y);
/* 1490 */         byte tileType = Tiles.decodeType(tile);
/* 1491 */         if (tileType == Tiles.Tile.TILE_GRASS.id || tileType == Tiles.Tile.TILE_MARSH.id || tileType == Tiles.Tile.TILE_MYCELIUM.id) {
/*      */ 
/*      */           
/* 1494 */           if (Tiles.decodeHeight(tile) > -20)
/*      */           {
/* 1496 */             if (TilePoller.checkForSeedGrowth(tile, x, y)) {
/* 1497 */               count++;
/*      */             }
/*      */           }
/* 1500 */         } else if (tileType == Tiles.Tile.TILE_STEPPE.id || tileType == Tiles.Tile.TILE_TUNDRA.id || tileType == Tiles.Tile.TILE_MOSS.id || tileType == Tiles.Tile.TILE_PEAT.id) {
/*      */ 
/*      */           
/* 1503 */           if (Tiles.decodeHeight(tile) > 0)
/*      */           {
/* 1505 */             if (TilePoller.checkForSeedGrowth(tile, x, y)) {
/* 1506 */               count++;
/*      */             }
/*      */           }
/* 1509 */         } else if (tileType == Tiles.Tile.TILE_TREE.id || tileType == Tiles.Tile.TILE_BUSH.id || tileType == Tiles.Tile.TILE_MYCELIUM_TREE.id || tileType == Tiles.Tile.TILE_MYCELIUM_BUSH.id) {
/*      */ 
/*      */           
/* 1512 */           if (TilePoller.checkForSeedGrowth(tile, x, y)) {
/* 1513 */             count++;
/*      */           }
/* 1515 */         } else if (tileType == Tiles.Tile.TILE_ROCK.id || tileType == Tiles.Tile.TILE_CLIFF.id) {
/*      */           
/* 1517 */           if (TilePoller.checkCreateIronRock(x, y)) {
/* 1518 */             count++;
/*      */           }
/* 1520 */         } else if (Tiles.isRoadType(tileType) || Tiles.isEnchanted(tileType) || tileType == Tiles.Tile.TILE_DIRT_PACKED.id || tileType == Tiles.Tile.TILE_DIRT.id || tileType == Tiles.Tile.TILE_LAWN.id || tileType == Tiles.Tile.TILE_MYCELIUM_LAWN.id) {
/*      */ 
/*      */ 
/*      */           
/* 1524 */           TilePoller.checkInvestigateGrowth(tile, x, y);
/*      */         } 
/*      */       } 
/*      */     } 
/* 1528 */     float lElapsedTime = (float)(System.nanoTime() - now) / 1000000.0F;
/* 1529 */     logger.log(Level.INFO, "Created " + count + " seeds. It took " + lElapsedTime + " millis.");
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void addDuelRing(Item item) {
/* 1534 */     duelRings.add(item);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void removeDuelRing(Item item) {
/* 1539 */     duelRings.remove(item);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Item isWithinDuelRing(int tileX, int tileY, boolean surfaced) {
/* 1544 */     if (!surfaced)
/* 1545 */       return null; 
/* 1546 */     int maxDist = 20;
/* 1547 */     for (Item ring : duelRings) {
/*      */       
/* 1549 */       if (ring.getZoneId() > 0 && !ring.deleted)
/*      */       {
/* 1551 */         if (ring.getTileX() < tileX + 20 && ring.getTileX() > tileX - 20 && ring.getTileY() < tileY + 20 && ring
/* 1552 */           .getTileY() > tileY - 20)
/*      */         {
/* 1554 */           return ring;
/*      */         }
/*      */       }
/*      */     } 
/* 1558 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isTreeCapable(int x, int y, MeshIO mesh, int width, int tile) {
/* 1563 */     if (Tiles.decodeHeight(tile) < 0)
/* 1564 */       return false; 
/* 1565 */     if (Tiles.decodeType(tile) != Tiles.Tile.TILE_DIRT.id)
/* 1566 */       return false; 
/* 1567 */     int neighborTrees = 0;
/*      */     
/* 1569 */     for (int xx = x - 1; xx <= x + 1; xx++) {
/*      */       
/* 1571 */       for (int yy = y - 1; yy <= y + 1; yy++) {
/*      */         
/* 1573 */         int xxx = xx & width - 1;
/* 1574 */         int yyy = yy & width - 1;
/* 1575 */         int t = mesh.getTile(xxx, yyy);
/* 1576 */         if (Tiles.decodeType(t) == Tiles.Tile.TILE_TREE.id)
/* 1577 */           neighborTrees++; 
/*      */       } 
/*      */     } 
/* 1580 */     return (neighborTrees < 5);
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void initializeDomains() {
/* 1585 */     if (Features.Feature.NEWDOMAINS.isEnabled()) {
/*      */       
/* 1587 */       for (int y = 0; y < domainSizeY; y++) {
/* 1588 */         for (int x = 0; x < domainSizeX; x++) {
/* 1589 */           altarZones.add(null);
/*      */         }
/*      */       } 
/*      */     } else {
/* 1593 */       for (int x = 0; x < faithSizeX; x++) {
/*      */         
/* 1595 */         for (int y = 0; y < faithSizeY; y++) {
/*      */           
/* 1597 */           surfaceDomains[x][y] = new FaithZone((short)(x * 8), (short)(y * 8), (short)(x * 8 + 7), (short)(y * 8 + 7));
/*      */           
/* 1599 */           caveDomains[x][y] = new FaithZone((short)(x * 8), (short)(y * 8), (short)(x * 8 + 7), (short)(y * 8 + 7));
/*      */         } 
/*      */       } 
/*      */       
/* 1603 */       logger.log(Level.INFO, "Number of faithzones=" + (faithSizeX * faithSizeX) + " surfaced as well as underground.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final FaithZone getFaithZone(int tilex, int tiley, boolean surfaced) throws NoSuchZoneException {
/* 1610 */     if (Features.Feature.NEWDOMAINS.isEnabled()) {
/*      */       
/* 1612 */       FaithZone toReturn = null;
/* 1613 */       HashMap<Item, FaithZone> thisZone = null;
/* 1614 */       for (int i = -1; i <= 1; i++) {
/* 1615 */         for (int j = -1; j <= 1; j++) {
/*      */ 
/*      */           
/* 1618 */           int actualZone = Math.max(0, Math.min(tiley / 64 + j, domainSizeY - 1)) * domainSizeX + Math.max(0, Math.min(tilex / 64 + i, domainSizeX - 1));
/* 1619 */           if (actualZone < altarZones.size()) {
/*      */             
/* 1621 */             thisZone = altarZones.get(actualZone);
/* 1622 */             if (thisZone != null)
/*      */             {
/*      */               
/* 1625 */               for (FaithZone f : thisZone.values()) {
/*      */                 
/* 1627 */                 if (f.getCurrentRuler() == null) {
/*      */                   continue;
/*      */                 }
/* 1630 */                 if (f.containsTile(tilex, tiley)) {
/*      */                   
/* 1632 */                   if (f.getStrengthForTile(tilex, tiley, surfaced) <= 0) {
/*      */                     continue;
/*      */                   }
/* 1635 */                   if (toReturn == null) {
/* 1636 */                     toReturn = f; continue;
/* 1637 */                   }  if (toReturn.getStrengthForTile(tilex, tiley, surfaced) < f.getStrengthForTile(tilex, tiley, surfaced)) {
/* 1638 */                     toReturn = f; continue;
/* 1639 */                   }  if (toReturn.getStrengthForTile(tilex, tiley, surfaced) == f.getStrengthForTile(tilex, tiley, surfaced)) {
/*      */ 
/*      */                     
/* 1642 */                     int fDist = Math.min(Math.abs(f.getCenterX() - tilex), Math.abs(f.getCenterY() - tiley));
/* 1643 */                     int rDist = Math.min(Math.abs(toReturn.getCenterX() - tilex), Math.abs(toReturn.getCenterY() - tiley));
/*      */                     
/* 1645 */                     if (fDist < rDist)
/* 1646 */                       toReturn = f; 
/*      */                   } 
/*      */                 } 
/*      */               }  } 
/*      */           } 
/*      */         } 
/* 1652 */       }  return toReturn;
/*      */     } 
/*      */ 
/*      */     
/* 1656 */     if (tilex < 0 || tilex >= worldTileSizeX || tiley < 0 || tiley >= worldTileSizeY)
/* 1657 */       throw new NoSuchZoneException("No faith zone at " + tilex + ", " + tiley); 
/* 1658 */     if (surfaced) {
/* 1659 */       return surfaceDomains[tilex >> 3][tiley >> 3];
/*      */     }
/* 1661 */     return caveDomains[tilex >> 3][tiley >> 3];
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean areaOverlapsFaithZone(FaithZone f, int startx, int starty, int endx, int endy) {
/* 1667 */     for (int i = startx; i < endx; i++) {
/* 1668 */       for (int j = starty; j < endy; j++) {
/* 1669 */         if (f.containsTile(i, j))
/* 1670 */           return true; 
/*      */       } 
/* 1672 */     }  return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final ArrayList<HashMap<Item, FaithZone>> getCoveredZones(int startx, int starty, int endx, int endy) {
/* 1677 */     ArrayList<HashMap<Item, FaithZone>> returnList = new ArrayList<>();
/*      */     
/* 1679 */     for (int y = Math.min(0, starty / 64); y <= Math.min(0, endy / 64); y++) {
/* 1680 */       for (int x = Math.min(0, startx / 64); x <= Math.min(0, endx / 64); x++) {
/*      */         
/* 1682 */         HashMap<Item, FaithZone> thisZone = altarZones.get(y * domainSizeX + x);
/* 1683 */         if (thisZone != null && !returnList.contains(thisZone))
/* 1684 */           returnList.add(thisZone); 
/*      */       } 
/*      */     } 
/* 1687 */     return returnList;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final FaithZone[] getFaithZonesCoveredBy(int startx, int starty, int endx, int endy, boolean surfaced) {
/* 1693 */     Set<FaithZone> zoneList = new HashSet<>();
/*      */     
/* 1695 */     if (Features.Feature.NEWDOMAINS.isEnabled()) {
/*      */       
/* 1697 */       ArrayList<HashMap<Item, FaithZone>> coveredZones = getCoveredZones(startx, starty, endx, endy);
/* 1698 */       for (HashMap<Item, FaithZone> z : coveredZones) {
/* 1699 */         for (FaithZone f : z.values()) {
/* 1700 */           if (f.getCurrentRuler() != null && 
/* 1701 */             areaOverlapsFaithZone(f, startx, starty, endx, endy)) {
/* 1702 */             zoneList.add(f);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } else {
/* 1707 */       for (int x = startx >> 3; x <= endx >> 3; x++) {
/*      */         
/* 1709 */         for (int y = starty >> 3; y <= endy >> 3; y++) {
/*      */           
/* 1711 */           FaithZone zone2 = surfaceDomains[x][y];
/* 1712 */           if (!surfaced)
/* 1713 */             zone2 = caveDomains[x][y]; 
/* 1714 */           zoneList.add(zone2);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1719 */     FaithZone[] toReturn = new FaithZone[zoneList.size()];
/* 1720 */     return zoneList.<FaithZone>toArray(toReturn);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final FaithZone[] getFaithZones() {
/* 1725 */     ArrayList<FaithZone> allZones = new ArrayList<>();
/*      */     
/* 1727 */     for (HashMap<Item, FaithZone> z : altarZones) {
/* 1728 */       if (z != null)
/* 1729 */         allZones.addAll(z.values()); 
/*      */     } 
/* 1731 */     FaithZone[] toReturn = new FaithZone[allZones.size()];
/* 1732 */     return allZones.<FaithZone>toArray(toReturn);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void createZones() throws IOException {
/* 1737 */     logger.log(Level.INFO, "Creating zones: size is " + worldTileSizeX + ", " + worldTileSizeY);
/*      */ 
/*      */ 
/*      */     
/* 1741 */     loading = true;
/* 1742 */     long now = System.nanoTime();
/*      */     
/* 1744 */     MESHSIZE = 1 << Constants.meshSize;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1749 */     initializeDomains();
/* 1750 */     initializeHiveZones();
/* 1751 */     int numz = 0;
/* 1752 */     int ms = MESHSIZE >> 6;
/* 1753 */     int zs = 6;
/* 1754 */     int zsize = 64;
/* 1755 */     boolean useDB = true;
/* 1756 */     Zone[][] szones = new Zone[worldTileSizeX >> 6][worldTileSizeY >> 6];
/*      */     
/* 1758 */     numberOfZones = szones.length * (szones[0]).length;
/* 1759 */     if (logger.isLoggable(Level.FINE)) {
/*      */       
/* 1761 */       logger.fine("Number of zones x=" + (worldTileSizeX >> 6) + ", total=" + numberOfZones);
/* 1762 */       logger.fine("This should equal zones x=" + (MESHSIZE >> 6));
/* 1763 */       logger.finer("Zone 54, 54=3456, 3519,3456,3519");
/*      */     } 
/*      */     
/* 1766 */     for (int x = 0; x < ms; x++) {
/*      */       
/* 1768 */       for (int y = 0; y < ms; y++) {
/*      */ 
/*      */         
/* 1771 */         szones[x][y] = new DbZone(x << 6, (x << 6) + 64 - 1, y << 6, (y << 6) + 64 - 1, true);
/*      */         
/* 1773 */         numz++;
/*      */       } 
/*      */     } 
/* 1776 */     logger.info("Initialised surface zones - array size: [" + ms + "][" + ms + "];");
/* 1777 */     if (logger.isLoggable(Level.FINEST))
/*      */     {
/* 1779 */       logger.finest("This should equal number of zones: " + numz);
/*      */     }
/* 1781 */     Zone[][] cZones = new Zone[worldTileSizeX >> 6][worldTileSizeY >> 6];
/*      */     int i;
/* 1783 */     for (i = 0; i < ms; i++) {
/*      */       
/* 1785 */       for (int y = 0; y < ms; y++)
/*      */       {
/*      */         
/* 1788 */         cZones[i][y] = new DbZone(i << 6, (i << 6) + 64 - 1, y << 6, (y << 6) + 64 - 1, false);
/*      */       }
/*      */     } 
/*      */     
/* 1792 */     logger.info("Initialised cave zones - array size: [" + (worldTileSizeX >> 6) + "][" + (worldTileSizeY >> 6) + "];");
/*      */ 
/*      */     
/* 1795 */     logger.log(Level.INFO, "Seconds between polls=800, zonespolled=" + Zone.zonesPolled + ", maxnumberofzonespolled=" + Zone.maxZonesPolled);
/*      */ 
/*      */     
/* 1798 */     surfaceZones = szones;
/* 1799 */     caveZones = cZones;
/* 1800 */     logger.info("Loading surface and cave zone structures");
/* 1801 */     Structures.loadAllStructures();
/* 1802 */     numz = 0;
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
/* 1819 */     for (i = 0; i < ms; i++) {
/*      */       
/* 1821 */       for (int y = 0; y < ms; y++) {
/*      */         
/* 1823 */         surfaceZones[i][y].loadFences();
/* 1824 */         caveZones[i][y].loadFences();
/*      */       } 
/*      */     } 
/* 1827 */     logger.info("Loaded fences");
/* 1828 */     DbStructure.loadBuildTiles();
/* 1829 */     logger.info("Loaded build tiles");
/* 1830 */     Structures.endLoadAll();
/* 1831 */     logger.info("Ended loading of structures");
/* 1832 */     for (i = 0; i < ms; i++) {
/*      */       
/* 1834 */       for (int y = 0; y < ms; y++) {
/*      */         
/* 1836 */         numz++;
/* 1837 */         surfaceZones[i][y].loadAllItemsForZone();
/* 1838 */         caveZones[i][y].loadAllItemsForZone();
/*      */       } 
/*      */     } 
/* 1841 */     logger.info("Loaded zone items");
/*      */     
/* 1843 */     logger.info("Loaded " + numz + " surface and cave zones");
/* 1844 */     float mod = 40.0F;
/* 1845 */     zonesPerRun = (int)(numberOfZones / 40.0F);
/* 1846 */     maxRest = (int)(numberOfZones - zonesPerRun * 40.0F);
/* 1847 */     rest = maxRest;
/* 1848 */     loading = false;
/* 1849 */     float lElapsedTime = (float)(System.nanoTime() - now) / 1000000.0F;
/* 1850 */     logger.log(Level.INFO, "Zones created and loaded. Number of zones=" + numberOfZones + ". PollFraction is " + rest + ". That took " + lElapsedTime + " millis.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeZones() {
/* 1859 */     if (!Servers.localServer.LOGINSERVER) {
/*      */       
/* 1861 */       File f = new File("zones" + Server.rand.nextInt(10000) + ".txt");
/* 1862 */       BufferedWriter output = null;
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 1867 */         output = new BufferedWriter(new FileWriter(f));
/*      */         
/* 1869 */         output.write("Legend:\n");
/* 1870 */         output.write("No kingdom: 0\n");
/* 1871 */         output.write("J/K: =\n");
/* 1872 */         output.write("J/K village: v\n");
/* 1873 */         output.write("J/K tower: t\n");
/* 1874 */         output.write("J/K village+tower: V\n");
/* 1875 */         output.write("HOTS: #\n");
/* 1876 */         output.write("HOTS village: w\n");
/* 1877 */         output.write("HOTS tower: g\n");
/* 1878 */         output.write("HOTS village+tower: W\n\n");
/* 1879 */         for (int x = 0; x < MESHSIZE >> 6; x++)
/*      */         {
/* 1881 */           for (int y = 0; y < MESHSIZE >> 6; y++)
/*      */           {
/* 1883 */             surfaceZones[x][y].write(output);
/*      */           }
/* 1885 */           output.write("\n");
/* 1886 */           output.flush();
/*      */         }
/*      */       
/* 1889 */       } catch (IOException iox) {
/*      */         
/* 1891 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */       } finally {
/*      */ 
/*      */         
/*      */         try {
/*      */ 
/*      */           
/* 1898 */           if (output != null) {
/* 1899 */             output.close();
/*      */           }
/* 1901 */         } catch (IOException iox) {
/*      */           
/* 1903 */           logger.log(Level.WARNING, iox.getMessage(), iox);
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
/*      */   public static boolean containsVillage(int x, int y, boolean surfaced) {
/*      */     try {
/* 1921 */       Zone zone = getZone(x, y, surfaced);
/* 1922 */       return zone.containsVillage(x, y);
/*      */     }
/* 1924 */     catch (NoSuchZoneException noSuchZoneException) {
/*      */ 
/*      */ 
/*      */       
/* 1928 */       return false;
/*      */     } 
/*      */   }
/*      */   
/*      */   public static Village getVillage(@Nonnull TilePos tilePos, boolean surfaced) {
/* 1933 */     return getVillage(tilePos.x, tilePos.y, surfaced);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Village getVillage(int x, int y, boolean surfaced) {
/*      */     try {
/* 1940 */       Zone zone = getZone(x, y, surfaced);
/* 1941 */       return zone.getVillage(x, y);
/*      */     }
/* 1943 */     catch (NoSuchZoneException noSuchZoneException) {
/*      */ 
/*      */ 
/*      */       
/* 1947 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void pollNextZones(long sleepTime) throws IOException {
/*      */     int bonusZone;
/* 1958 */     if (rest > 0) {
/*      */       
/* 1960 */       bonusZone = 1;
/* 1961 */       rest--;
/*      */     }
/*      */     else {
/*      */       
/* 1965 */       bonusZone = 0;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1970 */     synchronized (ZONE_SYNC_LOCK) {
/*      */       
/* 1972 */       for (int x = 0; x < zonesPerRun + bonusZone; x++) {
/*      */         
/* 1974 */         if (currentPollZoneY == 1)
/* 1975 */           Creatures.getInstance().pollAllCreatures(currentPollZoneX); 
/* 1976 */         if (currentPollZoneX >= worldTileSizeX >> 6) {
/*      */           
/* 1978 */           currentPollZoneX = 0;
/* 1979 */           currentPollZoneY++;
/*      */         } 
/* 1981 */         if (currentPollZoneY >= worldTileSizeY >> 6) {
/*      */           
/* 1983 */           currentPollZoneY = 0;
/* 1984 */           rest = maxRest;
/*      */           
/* 1986 */           if (pollnum > numberOfZones)
/*      */           {
/* 1988 */             pollnum = 0;
/*      */           }
/* 1990 */           pollnum += 16;
/*      */           
/* 1992 */           Server.incrementCombatCounter();
/* 1993 */           Server.incrementSecondsUptime();
/* 1994 */           PlayerInfoFactory.pollPremiumPlayers();
/* 1995 */           FocusZone.pollAll();
/* 1996 */           if (Server.getCombatCounter() % 10 == 0)
/*      */           {
/* 1998 */             CombatHandler.resolveRound();
/*      */           }
/*      */           
/* 2001 */           Players.getInstance().pollDeadPlayers();
/*      */           
/* 2003 */           Players.getInstance().pollKosWarnings();
/*      */           
/* 2005 */           if (logger.isLoggable(Level.FINEST))
/*      */           {
/* 2007 */             logger.finest("Pollnum=" + pollnum + ", max=" + numberOfZones + " cpzy=" + currentPollZoneY + " cpzx=" + currentPollZoneX);
/*      */           }
/*      */         } 
/*      */ 
/*      */         
/* 2012 */         Zone lSurfaceZone = surfaceZones[currentPollZoneX][currentPollZoneY];
/* 2013 */         if (lSurfaceZone != null && lSurfaceZone.isLoaded())
/*      */         {
/* 2015 */           lSurfaceZone.poll(pollnum);
/*      */         }
/* 2017 */         Zone lCaveZone = caveZones[currentPollZoneX][currentPollZoneY];
/* 2018 */         if (lCaveZone != null && lCaveZone.isLoaded())
/*      */         {
/* 2020 */           lCaveZone.poll(numberOfZones + pollnum);
/*      */         }
/* 2022 */         currentPollZoneX++;
/*      */       } 
/*      */     } 
/* 2025 */     if (logger.isLoggable(Level.FINEST))
/*      */     {
/* 2027 */       logger.finest("Polled " + (zonesPerRun + bonusZone) + " zones to " + currentPollZoneX + ", " + currentPollZoneY);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void saveAllZones() {
/* 2033 */     int worldSize = 1 << Constants.meshSize;
/* 2034 */     for (int x = 0; x < worldSize >> 6; x++) {
/*      */       
/* 2036 */       for (int y = 0; y < worldSize >> 6; y++) {
/*      */ 
/*      */         
/*      */         try {
/* 2040 */           if (surfaceZones[x][y].isLoaded()) {
/* 2041 */             surfaceZones[x][y].save();
/*      */           }
/* 2043 */         } catch (IOException iox) {
/*      */           
/* 2045 */           logger.log(Level.WARNING, "Failed to save surface zone " + x + ", " + y);
/*      */         } 
/*      */         
/*      */         try {
/* 2049 */           if (caveZones[x][y].isLoaded()) {
/* 2050 */             caveZones[x][y].save();
/*      */           }
/* 2052 */         } catch (IOException iox) {
/*      */           
/* 2054 */           logger.log(Level.WARNING, "Failed to save cave zone " + x + ", " + y);
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
/*      */   public static void saveNextZone() throws IOException {
/* 2067 */     if (currentSaveZoneX >= worldTileSizeX >> 6) {
/*      */       
/* 2069 */       currentSaveZoneX = 0;
/* 2070 */       currentSaveZoneY++;
/*      */     } 
/* 2072 */     if (currentSaveZoneY >= worldTileSizeY >> 6) {
/* 2073 */       currentSaveZoneY = 0;
/*      */     }
/* 2075 */     Zone lSurfaceSaveZone = surfaceZones[currentSaveZoneX][currentSaveZoneY];
/* 2076 */     if (lSurfaceSaveZone != null && lSurfaceSaveZone.isLoaded())
/*      */     {
/* 2078 */       lSurfaceSaveZone.save();
/*      */     }
/*      */     
/* 2081 */     Zone lCaveSaveZone = caveZones[currentSaveZoneX][currentSaveZoneY];
/* 2082 */     if (lCaveSaveZone != null && lCaveSaveZone.isLoaded())
/*      */     {
/* 2084 */       lCaveSaveZone.save();
/*      */     }
/* 2086 */     currentSaveZoneX++;
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
/*      */   public static VirtualZone createZone(Creature watcher, int startX, int startY, int centerX, int centerY, int size, boolean surface) {
/* 2112 */     if (logger.isLoggable(Level.FINEST))
/*      */     {
/* 2114 */       logger.finest("Creating virtual zone " + startX + ", " + startY + ", size: " + size + ", surface: " + surface + " for " + watcher
/* 2115 */           .getName());
/*      */     }
/* 2117 */     VirtualZone zone = new VirtualZone(watcher, startX, startY, centerX, centerY, size, surface);
/* 2118 */     virtualZones.put(Integer.valueOf(zone.getId()), zone);
/*      */     
/* 2120 */     return zone;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void removeZone(int id) {
/* 2125 */     virtualZones.remove(Integer.valueOf(id));
/*      */   }
/*      */ 
/*      */   
/*      */   static VirtualZone getVirtualZone(int number) throws NoSuchZoneException {
/* 2130 */     VirtualZone toReturn = virtualZones.get(Integer.valueOf(number));
/* 2131 */     if (toReturn == null)
/* 2132 */       throw new NoSuchZoneException("No zone with number " + number); 
/* 2133 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Zone getZone(int number) throws NoSuchZoneException {
/* 2138 */     Zone toReturn = null;
/*      */     
/* 2140 */     if (toReturn == null)
/*      */     {
/* 2142 */       for (int x = 0; x < worldTileSizeX >> 6; x++) {
/*      */         
/* 2144 */         for (int y = 0; y < worldTileSizeY >> 6; y++) {
/*      */           
/* 2146 */           if (surfaceZones[x][y].getId() == number)
/* 2147 */             return surfaceZones[x][y]; 
/*      */         } 
/*      */       } 
/*      */     }
/* 2151 */     if (toReturn == null)
/*      */     {
/* 2153 */       for (int x = 0; x < worldTileSizeX >> 6; x++) {
/*      */         
/* 2155 */         for (int y = 0; y < worldTileSizeY >> 6; y++) {
/*      */           
/* 2157 */           if (caveZones[x][y].getId() == number)
/* 2158 */             return caveZones[x][y]; 
/*      */         } 
/*      */       } 
/*      */     }
/* 2162 */     if (toReturn == null)
/* 2163 */       throw new NoSuchZoneException("No zone with number " + number); 
/* 2164 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getZoneIdFor(int x, int y, boolean surfaced) throws NoSuchZoneException {
/* 2170 */     Zone toReturn = getZone(x, y, surfaced);
/* 2171 */     return toReturn.getId();
/*      */   }
/*      */ 
/*      */   
/*      */   public static Zone getZone(@Nonnull TilePos tilePos, boolean surfaced) throws NoSuchZoneException {
/* 2176 */     return getZone(tilePos.x, tilePos.y, surfaced);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Zone getZone(int tilex, int tiley, boolean surfaced) throws NoSuchZoneException {
/* 2181 */     Zone toReturn = null;
/* 2182 */     if (surfaced) {
/*      */ 
/*      */       
/*      */       try {
/* 2186 */         toReturn = surfaceZones[tilex >> 6][tiley >> 6];
/* 2187 */         if (!toReturn.covers(tilex, tiley)) {
/* 2188 */           logger.log(Level.WARNING, "Error in the way zones are fetched. Doesn't work for " + (tilex >> 6) + " to cover " + tilex + " or " + (tiley >> 6) + " to cover " + tiley);
/*      */         }
/*      */       }
/* 2191 */       catch (ArrayIndexOutOfBoundsException ex) {
/*      */         
/* 2193 */         throw new NoSuchZoneException("No such zone: x=" + (tilex >> 6) + ", y=" + (tiley >> 6), ex);
/*      */       } 
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/* 2200 */         toReturn = caveZones[tilex >> 6][tiley >> 6];
/*      */       }
/* 2202 */       catch (Exception ex) {
/*      */         
/* 2204 */         throw new NoSuchZoneException("No such cave zone: x=" + (tilex >> 6) + ", y=" + (tiley >> 6), ex);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 2210 */       if (!toReturn.isLoaded())
/*      */       {
/* 2212 */         logger.log(Level.WARNING, "THIS SHOULD NOT HAPPEN - zone: x=" + (tilex >> 6) + ", y=" + (tiley >> 6) + " - surfaced: " + surfaced, new Exception());
/*      */         
/* 2214 */         toReturn.load();
/* 2215 */         toReturn.loadFences();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 2228 */     catch (IOException ex) {
/*      */       
/* 2230 */       logger.log(Level.WARNING, "Failed to load zone " + (tilex >> 6) + ", " + (tiley >> 6) + ". Zone will be empty.", ex);
/*      */     } 
/*      */     
/* 2233 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Zone[] getZonesCoveredBy(VirtualZone zone) {
/* 2238 */     Set<Zone> zoneList = new HashSet<>();
/* 2239 */     if (logger.isLoggable(Level.FINEST))
/*      */     {
/* 2241 */       logger.finest("Getting zones covered by " + zone.getId() + ": " + zone.getStartX() + "," + zone.getEndX() + ",y:" + zone
/* 2242 */           .getStartY() + "," + zone.getEndY() + " which starts at " + (zone.getStartX() >> 6) + "," + (zone
/* 2243 */           .getStartY() >> 6) + " and ends at " + (zone.getEndX() >> 6) + "," + (zone
/* 2244 */           .getEndY() >> 6));
/*      */     }
/* 2246 */     for (int x = zone.getStartX() >> 6; x <= zone.getEndX() >> 6; x++) {
/*      */       
/* 2248 */       for (int y = zone.getStartY() >> 6; y <= zone.getEndY() >> 6; y++) {
/*      */ 
/*      */         
/*      */         try {
/* 2252 */           Zone zone2 = getZone(x << 6, y << 6, zone.isOnSurface());
/* 2253 */           if (logger.isLoggable(Level.FINEST))
/*      */           {
/* 2255 */             logger.finest("Adding " + zone2.getId() + ": " + zone2.getStartX() + "," + zone2.getStartY() + "-" + zone2
/* 2256 */                 .getEndX() + "," + zone2.getEndY() + " which is at " + x + "," + y);
/*      */           }
/* 2258 */           zoneList.add(zone2);
/*      */         }
/* 2260 */         catch (NoSuchZoneException noSuchZoneException) {}
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2267 */     Zone[] toReturn = new Zone[zoneList.size()];
/* 2268 */     return zoneList.<Zone>toArray(toReturn);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void checkAllSurfaceZones(Creature checker) {
/* 2273 */     for (int x = 0; x < worldTileSizeX >> 6; x++) {
/*      */       
/* 2275 */       for (int y = 0; y < worldTileSizeY >> 6; y++)
/*      */       {
/* 2277 */         surfaceZones[x][y].checkIntegrity(checker);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void checkAllCaveZones(Creature checker) {
/* 2284 */     for (int x = 0; x < worldTileSizeX >> 6; x++) {
/*      */       
/* 2286 */       for (int y = 0; y < worldTileSizeY >> 6; y++)
/*      */       {
/* 2288 */         caveZones[x][y].checkIntegrity(checker);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Zone[] getZonesCoveredBy(int startx, int starty, int endx, int endy, boolean surfaced) {
/* 2296 */     Set<Zone> zoneList = new HashSet<>();
/* 2297 */     if (logger.isLoggable(Level.FINEST))
/*      */     {
/* 2299 */       logger.finest("Getting zones covered by x: " + startx + "," + endx + ",y:" + starty + "," + endy + " which starts at " + (startx >> 6) + "," + (starty >> 6) + " and ends at " + (endx >> 6) + "," + (endy >> 6));
/*      */     }
/*      */ 
/*      */     
/* 2303 */     for (int x = startx >> 6; x <= endx >> 6; x++) {
/*      */       
/* 2305 */       for (int y = starty >> 6; y <= endy >> 6; y++) {
/*      */ 
/*      */         
/*      */         try {
/* 2309 */           Zone zone2 = getZone(x << 6, y << 6, surfaced);
/* 2310 */           if (logger.isLoggable(Level.FINEST))
/*      */           {
/* 2312 */             logger.finest("Adding " + zone2.getId() + ": " + zone2.getStartX() + "," + zone2.getStartY() + "-" + zone2
/* 2313 */                 .getEndX() + "," + zone2.getEndY() + " which is at " + x + "," + y);
/*      */           }
/* 2315 */           zoneList.add(zone2);
/*      */         }
/* 2317 */         catch (NoSuchZoneException noSuchZoneException) {}
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2324 */     Zone[] toReturn = new Zone[zoneList.size()];
/* 2325 */     return zoneList.<Zone>toArray(toReturn);
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
/*      */   public static boolean isStructureFinished(Creature creature, Wall wall) {
/*      */     try {
/* 2339 */       Structure struct = Structures.getStructure(wall.getStructureId());
/* 2340 */       return struct.isFinished();
/*      */     }
/* 2342 */     catch (NoSuchStructureException nss) {
/*      */       
/* 2344 */       logger.log(Level.WARNING, "No structure for wall with id " + wall.getId());
/* 2345 */       return false;
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
/*      */   public static int getTileIntForTile(int xTile, int yTile, int layer) {
/* 2754 */     if (layer < 0)
/*      */     {
/* 2756 */       return Server.caveMesh.getTile(safeTileX(xTile), safeTileY(yTile));
/*      */     }
/*      */ 
/*      */     
/* 2760 */     return Server.surfaceMesh.getTile(safeTileX(xTile), safeTileY(yTile));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte getTextureForTile(int xTile, int yTile, int layer) {
/* 2766 */     if (layer < 0) {
/*      */       
/* 2768 */       if (xTile < 0 || xTile > worldTileSizeX || yTile < 0 || yTile > worldTileSizeY)
/* 2769 */         return Tiles.Tile.TILE_ROCK.id; 
/* 2770 */       return Tiles.decodeType(Server.caveMesh.getTile(xTile, yTile));
/*      */     } 
/*      */ 
/*      */     
/* 2774 */     if (xTile < 0 || xTile > worldTileSizeX || yTile < 0 || yTile > worldTileSizeY)
/* 2775 */       return Tiles.Tile.TILE_DIRT.id; 
/* 2776 */     return Tiles.decodeType(Server.surfaceMesh.getTile(xTile, yTile));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final double getDir(float x, float y) {
/* 2782 */     double degree = 1.0D;
/* 2783 */     if (x != 0.0F) {
/*      */       
/* 2785 */       if (y != 0.0F) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2791 */         degree = Math.atan2(y, x);
/* 2792 */         degree = degree * 57.29577951308232D + 90.0D;
/* 2793 */         while (degree < 0.0D)
/* 2794 */           degree += 360.0D; 
/* 2795 */         while (degree >= 360.0D) {
/* 2796 */           degree -= 360.0D;
/*      */ 
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 2802 */       else if (x < 0.0F) {
/* 2803 */         degree = 270.0D;
/*      */       } else {
/* 2805 */         degree = 90.0D;
/*      */       } 
/* 2807 */     } else if (y != 0.0F) {
/*      */       
/* 2809 */       if (y > 0.0F) {
/* 2810 */         degree = 180.0D;
/*      */       } else {
/* 2812 */         degree = 360.0D;
/*      */       } 
/* 2814 */     }  return degree;
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
/*      */   public static int getTileZoneFor(float posX, float posY, int tilex, int tiley) {
/* 2829 */     float xa = posX - (tilex * 4);
/* 2830 */     float ya = posY - (tiley * 4);
/*      */ 
/*      */     
/* 2833 */     xa -= 2.0F;
/* 2834 */     ya -= 2.0F;
/* 2835 */     double rot = getDir(xa, ya);
/* 2836 */     if (logger.isLoggable(Level.FINEST))
/*      */     {
/* 2838 */       logger.finest("Rot to " + xa + ", " + ya + " is " + rot);
/*      */     }
/* 2840 */     if (rot <= 45.0D)
/* 2841 */       return 0; 
/* 2842 */     if (rot <= 90.0D)
/* 2843 */       return 1; 
/* 2844 */     if (rot <= 135.0D)
/* 2845 */       return 2; 
/* 2846 */     if (rot <= 180.0D)
/* 2847 */       return 3; 
/* 2848 */     if (rot <= 225.0D)
/* 2849 */       return 4; 
/* 2850 */     if (rot <= 270.0D)
/* 2851 */       return 5; 
/* 2852 */     if (rot <= 315.0D) {
/* 2853 */       return 6;
/*      */     }
/* 2855 */     return 7;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final float calculateRockHeight(float posX, float posY) throws NoSuchZoneException {
/* 2860 */     float height = 0.0F;
/*      */     
/*      */     try {
/* 2863 */       int tilex = (int)posX >> 2;
/* 2864 */       int tiley = (int)posY >> 2;
/*      */       
/* 2866 */       MeshIO mesh = Server.rockMesh;
/* 2867 */       int[] meshData = mesh.data;
/*      */       
/* 2869 */       float xa = posX / 4.0F - tilex;
/* 2870 */       float ya = posY / 4.0F - tiley;
/* 2871 */       if (xa > ya)
/*      */       {
/* 2873 */         if (ya >= 0.999F)
/* 2874 */           ya = 0.999F; 
/* 2875 */         xa -= ya;
/* 2876 */         xa /= 1.0F - ya;
/*      */         
/* 2878 */         if (xa < 0.0F)
/* 2879 */           xa = 0.0F; 
/* 2880 */         if (xa > 1.0F) {
/* 2881 */           xa = 1.0F;
/*      */         }
/* 2883 */         float xheight1 = Tiles.decodeHeightAsFloat(meshData[tilex | tiley << Constants.meshSize]) * (1.0F - xa) + Tiles.decodeHeightAsFloat(meshData[tilex + 1 | tiley << Constants.meshSize]) * xa;
/* 2884 */         float xheight2 = Tiles.decodeHeightAsFloat(meshData[tilex + 1 | tiley + 1 << Constants.meshSize]);
/* 2885 */         height = xheight1 * (1.0F - ya) + xheight2 * ya;
/*      */       }
/*      */       else
/*      */       {
/* 2889 */         if (ya <= 0.001F)
/* 2890 */           ya = 0.001F; 
/* 2891 */         xa /= ya;
/*      */         
/* 2893 */         if (xa < 0.0F)
/* 2894 */           xa = 0.0F; 
/* 2895 */         if (xa > 1.0F)
/* 2896 */           xa = 1.0F; 
/* 2897 */         float xheight1 = Tiles.decodeHeightAsFloat(meshData[tilex | tiley << Constants.meshSize]);
/*      */         
/* 2899 */         float xheight2 = Tiles.decodeHeightAsFloat(meshData[tilex | tiley + 1 << Constants.meshSize]) * (1.0F - xa) + Tiles.decodeHeightAsFloat(meshData[tilex + 1 | tiley + 1 << Constants.meshSize]) * xa;
/*      */         
/* 2901 */         height = xheight1 * (1.0F - ya) + xheight2 * ya;
/*      */       }
/*      */     
/* 2904 */     } catch (Exception ex) {
/*      */       
/* 2906 */       if (logger.isLoggable(Level.FINER))
/*      */       {
/* 2908 */         logger.log(Level.FINER, "No such zone at " + posX + ", " + posY, ex);
/*      */       }
/* 2910 */       throw new NoSuchZoneException("No such zone", ex);
/*      */     } 
/* 2912 */     return height;
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
/*      */   public static final float calculateHeight(float posX, float posY, boolean surfaced) throws NoSuchZoneException {
/* 2950 */     float height = 0.0F;
/*      */     
/*      */     try {
/* 2953 */       int tilex = (int)posX >> 2;
/* 2954 */       int tiley = (int)posY >> 2;
/*      */       
/* 2956 */       MeshIO mesh = Server.surfaceMesh;
/* 2957 */       if (!surfaced) {
/* 2958 */         mesh = Server.caveMesh;
/* 2959 */       } else if (Tiles.decodeType(mesh.getTile(tilex, tiley)) == Tiles.Tile.TILE_HOLE.id) {
/* 2960 */         mesh = Server.caveMesh;
/* 2961 */       }  int[] meshData = mesh.data;
/*      */       
/* 2963 */       float xa = posX / 4.0F - tilex;
/* 2964 */       float ya = posY / 4.0F - tiley;
/* 2965 */       if (xa > ya)
/*      */       {
/* 2967 */         if (ya >= 0.999F)
/* 2968 */           ya = 0.999F; 
/* 2969 */         xa -= ya;
/* 2970 */         xa /= 1.0F - ya;
/*      */         
/* 2972 */         if (xa < 0.0F)
/* 2973 */           xa = 0.0F; 
/* 2974 */         if (xa > 1.0F) {
/* 2975 */           xa = 1.0F;
/*      */         }
/* 2977 */         float xheight1 = Tiles.decodeHeightAsFloat(meshData[tilex | tiley << Constants.meshSize]) * (1.0F - xa) + Tiles.decodeHeightAsFloat(meshData[tilex + 1 | tiley << Constants.meshSize]) * xa;
/* 2978 */         float xheight2 = Tiles.decodeHeightAsFloat(meshData[tilex + 1 | tiley + 1 << Constants.meshSize]);
/* 2979 */         height = xheight1 * (1.0F - ya) + xheight2 * ya;
/*      */       }
/*      */       else
/*      */       {
/* 2983 */         if (ya <= 0.001F)
/* 2984 */           ya = 0.001F; 
/* 2985 */         xa /= ya;
/*      */         
/* 2987 */         if (xa < 0.0F)
/* 2988 */           xa = 0.0F; 
/* 2989 */         if (xa > 1.0F)
/* 2990 */           xa = 1.0F; 
/* 2991 */         float xheight1 = Tiles.decodeHeightAsFloat(meshData[tilex | tiley << Constants.meshSize]);
/*      */         
/* 2993 */         float xheight2 = Tiles.decodeHeightAsFloat(meshData[tilex | tiley + 1 << Constants.meshSize]) * (1.0F - xa) + Tiles.decodeHeightAsFloat(meshData[tilex + 1 | tiley + 1 << Constants.meshSize]) * xa;
/*      */         
/* 2995 */         height = xheight1 * (1.0F - ya) + xheight2 * ya;
/*      */       }
/*      */     
/* 2998 */     } catch (Exception ex) {
/*      */       
/* 3000 */       if (logger.isLoggable(Level.FINER))
/*      */       {
/* 3002 */         logger.log(Level.FINER, "No such zone at " + posX + ", " + posY, ex);
/*      */       }
/* 3004 */       throw new NoSuchZoneException("No such zone", ex);
/*      */     } 
/* 3006 */     return height;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final float calculatePosZ(float posx, float posy, VolaTile tile, boolean isOnSurface, boolean floating, float currentPosZ, @Nullable Creature creature, long bridgeId) {
/*      */     try {
/* 3015 */       float basePosZ = calculateHeight(posx, posy, isOnSurface);
/* 3016 */       if (bridgeId > 0L) {
/*      */         
/* 3018 */         int xx = (int)StrictMath.floor((posx / 4.0F));
/* 3019 */         int yy = (int)StrictMath.floor((posy / 4.0F));
/*      */         
/* 3021 */         float xa = posx / 4.0F - xx;
/* 3022 */         float ya = posy / 4.0F - yy;
/*      */         
/* 3024 */         float[] hts = getNodeHeights(xx, yy, isOnSurface ? 0 : -1, bridgeId);
/*      */         
/* 3026 */         return hts[0] * (1.0F - xa) * (1.0F - ya) + hts[1] * xa * (1.0F - ya) + hts[2] * (1.0F - xa) * ya + hts[3] * xa * ya;
/*      */       } 
/*      */       
/* 3029 */       int currentFloorLevel = 0;
/*      */       
/* 3031 */       if (creature != null) {
/*      */         
/* 3033 */         currentFloorLevel = creature.getFloorLevel();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 3040 */       else if (currentPosZ > basePosZ) {
/*      */         
/* 3042 */         currentFloorLevel = (int)(currentPosZ - basePosZ) / 3;
/*      */       } 
/*      */       
/* 3045 */       if (tile != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3050 */         int dfl = tile.getDropFloorLevel(currentFloorLevel);
/* 3051 */         Floor[] f = tile.getFloors(dfl * 30, dfl * 30);
/*      */         
/* 3053 */         float h = basePosZ + Math.max(0, dfl * 3) + ((f.length > 0) ? 0.25F : 0.0F);
/*      */ 
/*      */ 
/*      */         
/* 3057 */         return floating ? Math.max(0.0F + ((creature != null) ? (creature.getTemplate()).offZ : 0.0F), h) : h;
/*      */       } 
/*      */ 
/*      */       
/* 3061 */       VolaTile ttile = getTileOrNull((int)posx / 4, (int)posy / 4, isOnSurface);
/* 3062 */       if (ttile != null) {
/*      */ 
/*      */ 
/*      */         
/* 3066 */         int dfl = ttile.getDropFloorLevel(currentFloorLevel);
/* 3067 */         Floor[] f = ttile.getFloors(dfl * 30, dfl * 30);
/*      */         
/* 3069 */         float h = basePosZ + Math.max(0, dfl * 3) + ((f.length > 0) ? 0.25F : 0.0F);
/*      */ 
/*      */ 
/*      */         
/* 3073 */         return floating ? Math.max(0.0F + ((creature != null) ? (creature.getTemplate()).offZ : 0.0F), h) : h;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3079 */       if (floating) {
/* 3080 */         return Math.max(0.0F + ((creature != null) ? (creature.getTemplate()).offZ : 0.0F), basePosZ);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 3085 */       return basePosZ;
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 3090 */     catch (NoSuchZoneException nsz) {
/*      */       
/* 3092 */       logger.log(Level.WARNING, "No Zone for tile " + posx + ", " + posy + " " + isOnSurface, new Exception());
/*      */ 
/*      */       
/* 3095 */       return currentPosZ;
/*      */     } 
/*      */   }
/*      */   
/*      */   public static final float[] getNodeHeights(int xNode, int yNode, int layer, long bridgeId) {
/* 3100 */     float NW = 0.0F;
/* 3101 */     float NE = 0.0F;
/* 3102 */     float SW = 0.0F;
/* 3103 */     float SE = 0.0F;
/*      */     
/* 3105 */     if (bridgeId > 0L) {
/*      */ 
/*      */       
/* 3108 */       VolaTile vt = getTileOrNull(xNode, yNode, (layer == 0));
/* 3109 */       if (vt != null)
/*      */       {
/*      */ 
/*      */         
/* 3113 */         for (BridgePart bp : vt.getBridgeParts()) {
/*      */           
/* 3115 */           if (bp.getStructureId() == bridgeId)
/*      */           {
/* 3117 */             if (bp.getBridgePartState() == BridgeConstants.BridgeState.COMPLETED) {
/*      */               
/* 3119 */               NW = bp.getHeightOffset() / 10.0F;
/* 3120 */               SE = (bp.getHeightOffset() + bp.getSlope()) / 10.0F;
/* 3121 */               if (bp.getDir() == 0 || bp.getDir() == 4) {
/*      */                 
/* 3123 */                 NE = NW;
/* 3124 */                 SW = SE;
/*      */               
/*      */               }
/*      */               else {
/*      */                 
/* 3129 */                 NE = SE;
/* 3130 */                 SW = NW;
/*      */               } 
/*      */               
/* 3133 */               return new float[] { NW, NE, SW, SE };
/*      */             } 
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 3141 */     NW = getHeightForNode(xNode, yNode, layer);
/* 3142 */     NE = getHeightForNode(xNode + 1, yNode, layer);
/* 3143 */     SW = getHeightForNode(xNode, yNode + 1, layer);
/* 3144 */     SE = getHeightForNode(xNode + 1, yNode + 1, layer);
/*      */     
/* 3146 */     return new float[] { NW, NE, SW, SE };
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
/*      */   public static final float getHeightForNode(int xNode, int yNode, int layer) {
/* 3159 */     MeshIO mesh = Server.surfaceMesh;
/* 3160 */     if (layer < 0)
/* 3161 */       mesh = Server.caveMesh; 
/* 3162 */     if (xNode < 0 || xNode >= 1 << Constants.meshSize)
/* 3163 */       xNode = 0; 
/* 3164 */     if (yNode < 0 || yNode >= 1 << Constants.meshSize)
/* 3165 */       yNode = 0; 
/* 3166 */     return Tiles.decodeHeightAsFloat(mesh.getTile(xNode, yNode));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static VolaTile getTileOrNull(@Nonnull TilePos tilePos, boolean surfaced) {
/* 3174 */     return getTileOrNull(tilePos.x, tilePos.y, surfaced);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static VolaTile getTileOrNull(int tilex, int tiley, boolean surfaced) {
/* 3182 */     VolaTile tile = null;
/*      */     
/*      */     try {
/* 3185 */       Zone zone = getZone(tilex, tiley, surfaced);
/* 3186 */       tile = zone.getTileOrNull(tilex, tiley);
/*      */     }
/* 3188 */     catch (NoSuchZoneException nsz) {
/*      */       
/* 3190 */       if (!haslogged) {
/*      */         
/* 3192 */         if (logger.isLoggable(Level.FINEST))
/*      */         {
/* 3194 */           logger.log(Level.FINEST, "HERE _ No such zone: " + tilex + ", " + tiley + " surf=" + surfaced, (Throwable)nsz);
/*      */         }
/* 3196 */         haslogged = true;
/*      */       } 
/*      */     } 
/* 3199 */     return tile;
/*      */   }
/*      */ 
/*      */   
/*      */   public static VolaTile getOrCreateTile(@Nonnull TilePos tilePos, boolean surfaced) {
/* 3204 */     return getOrCreateTile(tilePos.x, tilePos.y, surfaced);
/*      */   }
/*      */ 
/*      */   
/*      */   public static VolaTile getOrCreateTile(int tilex, int tiley, boolean surfaced) {
/* 3209 */     VolaTile tile = null;
/*      */     
/*      */     try {
/* 3212 */       Zone zone = getZone(tilex, tiley, surfaced);
/* 3213 */       tile = zone.getOrCreateTile(tilex, tiley);
/*      */     }
/* 3215 */     catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3225 */     return tile;
/*      */   }
/*      */ 
/*      */   
/*      */   public static VolaTile[] getTilesSurrounding(int tilex, int tiley, boolean surfaced, int distance) {
/* 3230 */     Set<VolaTile> tiles = new HashSet<>();
/* 3231 */     for (int x = -distance; x <= distance; x++) {
/*      */       
/* 3233 */       for (int y = -distance; y <= distance; y++) {
/*      */         
/* 3235 */         VolaTile tile = getTileOrNull(tilex + x, tiley + y, surfaced);
/* 3236 */         if (tile != null)
/* 3237 */           tiles.add(tile); 
/*      */       } 
/*      */     } 
/* 3240 */     return tiles.<VolaTile>toArray(new VolaTile[tiles.size()]);
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
/*      */   public static final boolean isNoBuildZone(int tilex, int tiley) {
/* 3525 */     return FocusZone.isNoBuildZoneAt(tilex, tiley);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isPremSpawnZoneAt(int tilex, int tiley) {
/* 3530 */     if (Servers.localServer.isChaosServer()) {
/*      */       
/* 3532 */       Village v = Villages.getVillage(tilex, tiley, true);
/* 3533 */       if (v != null)
/*      */       {
/* 3535 */         if (v.isPermanent)
/* 3536 */           return false; 
/*      */       }
/* 3538 */       return true;
/*      */     } 
/* 3540 */     return FocusZone.isPremSpawnOnlyZoneAt(tilex, tiley);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isInPvPZone(int tilex, int tiley) {
/* 3545 */     if (FocusZone.isNonPvPZoneAt(tilex, tiley))
/* 3546 */       return false; 
/* 3547 */     if (FocusZone.isPvPZoneAt(tilex, tiley))
/* 3548 */       return true; 
/* 3549 */     return (isWithinDuelRing(tilex, tiley, true) != null);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isOnPvPServer(@Nonnull TilePos tilePos) {
/* 3554 */     return isOnPvPServer(tilePos.x, tilePos.y);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isOnPvPServer(int tilex, int tiley) {
/* 3559 */     if (Servers.localServer.PVPSERVER)
/*      */     {
/* 3561 */       return !FocusZone.isNonPvPZoneAt(tilex, tiley);
/*      */     }
/* 3563 */     if (FocusZone.isPvPZoneAt(tilex, tiley))
/* 3564 */       return true; 
/* 3565 */     return (isWithinDuelRing(tilex, tiley, true) != null);
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
/*      */   public static final boolean willEnterStructure(Creature creature, float startx, float starty, float endx, float endy, boolean surfaced) {
/* 3642 */     int starttilex = (int)startx >> 2;
/* 3643 */     int starttiley = (int)starty >> 2;
/*      */     
/* 3645 */     int endtilex = (int)endx >> 2;
/* 3646 */     int endtiley = (int)endy >> 2;
/* 3647 */     int max = 100;
/* 3648 */     if (creature != null)
/* 3649 */       max = creature.getMaxHuntDistance() + 5; 
/* 3650 */     if (Math.abs(endtilex - starttilex) > max || Math.abs(endtiley - starttiley) > max) {
/*      */       
/* 3652 */       if (creature != null) {
/* 3653 */         logger.log(Level.WARNING, creature
/* 3654 */             .getName() + " checking more than his maxdist of " + creature.getMaxHuntDistance(), new Exception());
/*      */       } else {
/*      */         
/* 3657 */         logger.log(Level.WARNING, "Too far: " + starttilex + "," + starttiley + "->" + endtilex + ", " + endtiley, new Exception());
/*      */       } 
/* 3659 */       return true;
/*      */     } 
/* 3661 */     if (starttilex == endtilex && starttiley == endtiley)
/* 3662 */       return false; 
/* 3663 */     double rot = getRotation(startx, starty, endx, endy);
/*      */     
/* 3665 */     double xPosMod = Math.sin(rot * 0.01745329238474369D) * 2.0D;
/* 3666 */     double yPosMod = -Math.cos(rot * 0.01745329238474369D) * 2.0D;
/*      */     
/* 3668 */     double currX = startx;
/* 3669 */     double currY = starty;
/* 3670 */     int currTileX = starttilex;
/* 3671 */     int currTileY = starttiley;
/* 3672 */     int lastTileX = starttilex;
/* 3673 */     int lastTileY = starttiley;
/*      */     
/* 3675 */     boolean found = false;
/*      */     
/*      */     while (true) {
/* 3678 */       if (Math.abs(endtilex - currTileX) <= 1 && Math.abs(endtiley - currTileY) <= 1) {
/*      */         
/* 3680 */         VolaTile startTile = getTileOrNull(currTileX, currTileY, surfaced);
/* 3681 */         VolaTile endTile = getTileOrNull(endtilex, endtiley, surfaced);
/* 3682 */         if (startTile != null) {
/*      */           
/* 3684 */           if (endTile != null)
/*      */           {
/* 3686 */             if (startTile.getStructure() == null && endTile.getStructure() != null && endTile
/* 3687 */               .getStructure().isFinished()) {
/* 3688 */               return true;
/*      */             }
/*      */           }
/* 3691 */         } else if (endTile != null && endTile.getStructure() != null && endTile.getStructure().isFinished()) {
/* 3692 */           return true;
/* 3693 */         }  return false;
/*      */       } 
/* 3695 */       currX += xPosMod;
/* 3696 */       currY += yPosMod;
/*      */       
/* 3698 */       currTileX = (int)currX >> 2;
/* 3699 */       currTileY = (int)currY >> 2;
/* 3700 */       if (Math.abs(currTileX - starttilex) > max || Math.abs(currTileY - starttiley) > max) {
/*      */         
/* 3702 */         if (creature != null) {
/* 3703 */           logger.log(Level.WARNING, creature.getName() + " missed target " + creature.getMaxHuntDistance(), new Exception());
/*      */         } else {
/*      */           
/* 3706 */           logger.log(Level.WARNING, "missed target : " + starttilex + "," + starttiley + "->" + endtilex + ", " + endtiley, new Exception());
/*      */         } 
/* 3708 */         return true;
/*      */       } 
/* 3710 */       int diffX = currTileX - lastTileX;
/* 3711 */       int diffY = currTileY - lastTileY;
/*      */       
/* 3713 */       if (diffX != 0 || diffY != 0) {
/*      */         
/* 3715 */         VolaTile startTile = getTileOrNull(lastTileX, lastTileY, surfaced);
/* 3716 */         VolaTile endTile = getTileOrNull(currTileX, currTileY, surfaced);
/* 3717 */         if (startTile != null) {
/*      */           
/* 3719 */           if (endTile != null)
/*      */           {
/* 3721 */             if (startTile.getStructure() == null && endTile.getStructure() != null && endTile
/* 3722 */               .getStructure().isFinished()) {
/* 3723 */               return true;
/*      */             }
/*      */           }
/* 3726 */         } else if (endTile != null && endTile.getStructure() != null && endTile.getStructure().isFinished()) {
/* 3727 */           return true;
/* 3728 */         }  lastTileY = currTileY;
/* 3729 */         lastTileX = currTileX;
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
/*      */   @Nullable
/*      */   public static final Floor[] getFloorsAtTile(int tilex, int tiley, int startHeightOffset, int endHeightOffset, int layer) {
/* 4001 */     return getFloorsAtTile(tilex, tiley, startHeightOffset, endHeightOffset, (layer != -1));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static final Floor[] getFloorsAtTile(int tilex, int tiley, int startHeightOffset, int endHeightOffset, boolean onSurface) {
/* 4008 */     VolaTile tile = getTileOrNull(tilex, tiley, onSurface);
/* 4009 */     if (tile != null) {
/*      */       
/* 4011 */       Floor[] floors = tile.getFloors(startHeightOffset, endHeightOffset);
/* 4012 */       if (floors != null)
/*      */       {
/* 4014 */         if (floors.length > 0)
/* 4015 */           return floors; 
/*      */       }
/*      */     } 
/* 4018 */     return null;
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
/*      */   public static final Floor getFloor(int tilex, int tiley, boolean onSurface, int floorLevel) {
/* 4031 */     VolaTile tile = getTileOrNull(tilex, tiley, onSurface);
/* 4032 */     if (tile != null)
/*      */     {
/* 4034 */       return tile.getFloor(floorLevel);
/*      */     }
/* 4036 */     return null;
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
/*      */   public static final BridgePart[] getBridgePartsAtTile(int tilex, int tiley, boolean onSurface) {
/* 4049 */     VolaTile tile = getTileOrNull(tilex, tiley, onSurface);
/* 4050 */     if (tile != null) {
/*      */       
/* 4052 */       BridgePart[] bridgeParts = tile.getBridgeParts();
/* 4053 */       if (bridgeParts != null)
/*      */       {
/* 4055 */         if (bridgeParts.length > 0) {
/* 4056 */           return bridgeParts;
/*      */         }
/*      */       }
/*      */     } 
/* 4060 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static BridgePart getBridgePartFor(int tilex, int tiley, boolean onSurface) {
/* 4072 */     BridgePart[] bps = getBridgePartsAtTile(tilex, tiley, onSurface);
/*      */     
/* 4074 */     if (bps != null && bps.length > 0)
/* 4075 */       return bps[0]; 
/* 4076 */     return null;
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static Structure[] getStructuresInArea(int startX, int startY, int endX, int endY, boolean surfaced) {
/*      */     Structure[] toReturn;
/* 5133 */     Set<Structure> set = new HashSet<>();
/* 5134 */     for (int x = startX; x <= endX; x++) {
/*      */       
/* 5136 */       for (int y = startY; y <= endY; y++) {
/*      */         
/* 5138 */         VolaTile tile = getTileOrNull(x, y, surfaced);
/* 5139 */         if (tile != null) {
/*      */           
/* 5141 */           Structure structure = tile.getStructure();
/* 5142 */           if (structure != null && !set.contains(structure) && structure.isTypeHouse()) {
/* 5143 */             set.add(structure);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 5148 */     if (set.size() > 0) {
/* 5149 */       toReturn = set.<Structure>toArray(new Structure[set.size()]);
/*      */     } else {
/* 5151 */       toReturn = new Structure[0];
/* 5152 */     }  return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final double getRotation(float startx, float starty, float endx, float endy) {
/* 5157 */     double newrot = Math.atan2((endy - starty), (endx - startx));
/* 5158 */     return newrot * 57.29577951308232D + 90.0D;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final LongPosition getEndTile(float startPosX, float startPosY, float rot, int tiledist) {
/* 5164 */     float xPosMod = (float)Math.sin((rot * 0.017453292F)) * (4 * tiledist);
/* 5165 */     float yPosMod = -((float)Math.cos((rot * 0.017453292F))) * (4 * tiledist);
/* 5166 */     float newPosX = startPosX + xPosMod;
/* 5167 */     float newPosY = startPosY + yPosMod;
/* 5168 */     return new LongPosition(0L, (int)newPosX >> 2, (int)newPosY >> 2);
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
/*      */   static final Den getNorthTop(int templateId) {
/* 5194 */     for (int tries = 0; tries < 1000; tries++) {
/*      */       
/* 5196 */       int startX = 0;
/* 5197 */       int startY = 0;
/* 5198 */       int endX = worldTileSizeX - 10;
/* 5199 */       int endY = Math.min(worldTileSizeY / 10, 500);
/*      */       
/* 5201 */       Zone[] zones = getZonesCoveredBy(0, 0, endX, endY, true);
/* 5202 */       Zone highest = null;
/* 5203 */       float top = 0.0F;
/* 5204 */       for (int x = 0; x < zones.length; x++) {
/*      */         
/* 5206 */         if ((zones[x]).highest > top)
/*      */         {
/* 5208 */           if (top == 0.0F || Server.rand.nextInt(2) == 0) {
/*      */             
/* 5210 */             top = (zones[x]).highest;
/* 5211 */             highest = zones[x];
/*      */           } 
/*      */         }
/*      */       } 
/* 5215 */       if (highest != null) {
/*      */         
/* 5217 */         int tx = 0;
/* 5218 */         int ty = 0;
/* 5219 */         short h = 0;
/* 5220 */         MeshIO mesh = Server.surfaceMesh;
/* 5221 */         for (int i = highest.startX; i <= highest.endX; i++) {
/*      */           
/* 5223 */           for (int y = highest.startY; y < highest.endY; y++) {
/*      */             
/* 5225 */             int tile = mesh.getTile(i, y);
/* 5226 */             short hi = Tiles.decodeHeight(tile);
/* 5227 */             if (hi > h) {
/*      */               
/* 5229 */               h = hi;
/* 5230 */               tx = i;
/* 5231 */               ty = y;
/*      */             } 
/*      */           } 
/*      */         } 
/* 5235 */         if (h > 0) {
/*      */           
/* 5237 */           VolaTile t = getTileOrNull(safeTileX(tx), safeTileY(ty), true);
/* 5238 */           if (t == null)
/*      */           {
/* 5240 */             if (Villages.getVillageWithPerimeterAt(safeTileX(tx), safeTileY(ty), true) == null && 
/* 5241 */               Villages.getVillageWithPerimeterAt(safeTileX(tx - 20), safeTileY(ty - 20), true) == null && 
/* 5242 */               Villages.getVillageWithPerimeterAt(safeTileX(tx - 20), safeTileY(ty), true) == null && 
/* 5243 */               Villages.getVillageWithPerimeterAt(safeTileX(tx + 20), safeTileY(ty), true) == null && 
/* 5244 */               Villages.getVillageWithPerimeterAt(safeTileX(tx), safeTileY(ty), true) == null && 
/* 5245 */               Villages.getVillageWithPerimeterAt(safeTileX(tx + 20), 
/* 5246 */                 safeTileY(ty - 20), true) == null)
/* 5247 */               if (Villages.getVillageWithPerimeterAt(safeTileX(tx + 20), 
/* 5248 */                   safeTileY(ty + 20), true) == null)
/* 5249 */                 if (Villages.getVillageWithPerimeterAt(safeTileX(tx), 
/* 5250 */                     safeTileY(ty + 20), true) == null)
/* 5251 */                   if (Villages.getVillageWithPerimeterAt(safeTileX(tx - 20), 
/* 5252 */                       safeTileY(ty + 20), true) == null) {
/*      */                     
/* 5254 */                     logger.log(Level.INFO, "Created den for " + templateId + " after " + tries + " tries.");
/*      */                     
/* 5256 */                     return new Den(templateId, tx, ty, true);
/*      */                   }    
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 5262 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   static final Den getWestTop(int templateId) {
/* 5267 */     for (int a = 0; a < 100; a++) {
/*      */       
/* 5269 */       int startX = 0;
/* 5270 */       int startY = Math.min(worldTileSizeY / 10, 500);
/* 5271 */       int endX = (worldTileSizeX - 10) / 2;
/* 5272 */       int endY = worldTileSizeY - Math.min(worldTileSizeY / 10, 500);
/* 5273 */       Zone[] zones = getZonesCoveredBy(0, startY, endX, endY, true);
/* 5274 */       Zone highest = null;
/* 5275 */       float top = 0.0F;
/* 5276 */       for (int x = 0; x < zones.length; x++) {
/*      */         
/* 5278 */         if ((zones[x]).highest > top)
/*      */         {
/* 5280 */           if (top == 0.0F || Server.rand.nextInt(2) == 0) {
/*      */             
/* 5282 */             top = (zones[x]).highest;
/* 5283 */             highest = zones[x];
/*      */           } 
/*      */         }
/*      */       } 
/* 5287 */       if (highest != null) {
/*      */         
/* 5289 */         int tx = 0;
/* 5290 */         int ty = 0;
/* 5291 */         short h = 0;
/* 5292 */         MeshIO mesh = Server.surfaceMesh;
/* 5293 */         for (int i = highest.startX; i <= highest.endX; i++) {
/*      */           
/* 5295 */           for (int y = highest.startY; y < highest.endY; y++) {
/*      */             
/* 5297 */             Village v = Villages.getVillageWithPerimeterAt(i, y, true);
/* 5298 */             if (v == null) {
/*      */               
/* 5300 */               int tile = mesh.getTile(i, y);
/* 5301 */               short hi = Tiles.decodeHeight(tile);
/* 5302 */               if (hi > h) {
/*      */                 
/* 5304 */                 h = hi;
/* 5305 */                 tx = i;
/* 5306 */                 ty = y;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/* 5311 */         if (h > 0)
/* 5312 */           return new Den(templateId, tx, ty, true); 
/*      */       } 
/*      */     } 
/* 5315 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   static final Den getSouthTop(int templateId) {
/* 5320 */     for (int a = 0; a < 100; a++) {
/*      */       
/* 5322 */       int startX = 0;
/* 5323 */       int startY = worldTileSizeY - Math.min(worldTileSizeY / 10, 500);
/* 5324 */       int endX = worldTileSizeX - 10;
/* 5325 */       int endY = worldTileSizeY - 10;
/* 5326 */       Zone[] zones = getZonesCoveredBy(0, startY, endX, endY, true);
/* 5327 */       Zone highest = null;
/* 5328 */       float top = 0.0F;
/* 5329 */       for (int x = 0; x < zones.length; x++) {
/*      */         
/* 5331 */         if ((zones[x]).highest > top)
/*      */         {
/* 5333 */           if (top == 0.0F || Server.rand.nextInt(2) == 0) {
/*      */             
/* 5335 */             top = (zones[x]).highest;
/* 5336 */             highest = zones[x];
/*      */           } 
/*      */         }
/*      */       } 
/* 5340 */       if (highest != null) {
/*      */         
/* 5342 */         int tx = 0;
/* 5343 */         int ty = 0;
/* 5344 */         short h = 0;
/* 5345 */         MeshIO mesh = Server.surfaceMesh;
/* 5346 */         for (int i = highest.startX; i <= highest.endX; i++) {
/*      */           
/* 5348 */           for (int y = highest.startY; y < highest.endY; y++) {
/*      */             
/* 5350 */             Village v = Villages.getVillageWithPerimeterAt(i, y, true);
/* 5351 */             if (v == null) {
/*      */               
/* 5353 */               int tile = mesh.getTile(i, y);
/* 5354 */               short hi = Tiles.decodeHeight(tile);
/* 5355 */               if (hi > h) {
/*      */                 
/* 5357 */                 h = hi;
/* 5358 */                 tx = i;
/* 5359 */                 ty = y;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/* 5364 */         if (h > 0)
/* 5365 */           return new Den(templateId, tx, ty, true); 
/*      */       } 
/*      */     } 
/* 5368 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final TilePos getRandomCenterLand() {
/* 5373 */     return TilePos.fromXY(worldTileSizeX / 4 + Server.rand.nextInt(worldTileSizeX / 2), worldTileSizeY / 4 + Server.rand.nextInt(worldTileSizeY / 2));
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Den getRandomTop() {
/* 5378 */     for (int a = 0; a < 100; a++) {
/*      */       
/* 5380 */       int minHeight = 200;
/* 5381 */       int startx = Server.rand.nextInt(worldTileSizeX >> 6);
/* 5382 */       int starty = Server.rand.nextInt(worldTileSizeY >> 6);
/* 5383 */       int endx = Math.min(startx + 2, worldTileSizeX >> 6);
/* 5384 */       int endy = Math.min(starty + 2, worldTileSizeY >> 6);
/*      */       
/* 5386 */       Zone highest = null;
/* 5387 */       float top = 0.0F;
/* 5388 */       for (int x = startx; x < endx; x++) {
/*      */         
/* 5390 */         for (int y = starty; y < endy; y++) {
/*      */           
/* 5392 */           if ((surfaceZones[x][y]).highest > top) {
/*      */             
/* 5394 */             logger.info("Zone " + x + "," + y + " now highest for top.");
/* 5395 */             top = (surfaceZones[x][y]).highest;
/* 5396 */             if (top > 200.0F)
/* 5397 */               highest = surfaceZones[x][y]; 
/*      */           } 
/*      */         } 
/*      */       } 
/* 5401 */       if (highest != null) {
/*      */         
/* 5403 */         int tx = 0;
/* 5404 */         int ty = 0;
/* 5405 */         short h = 0;
/* 5406 */         MeshIO mesh = Server.surfaceMesh;
/* 5407 */         for (int i = highest.startX; i <= highest.endX; i++) {
/*      */           
/* 5409 */           for (int y = highest.startY; y < highest.endY; y++) {
/*      */             
/* 5411 */             Village v = Villages.getVillageWithPerimeterAt(i, y, true);
/* 5412 */             if (v == null) {
/*      */               
/* 5414 */               int tile = mesh.getTile(i, y);
/* 5415 */               short hi = Tiles.decodeHeight(tile);
/* 5416 */               if (hi > h) {
/*      */                 
/* 5418 */                 h = hi;
/* 5419 */                 tx = i;
/* 5420 */                 ty = y;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/* 5425 */         if (h > 200)
/* 5426 */           return new Den(0, tx, ty, true); 
/*      */       } 
/*      */     } 
/* 5429 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   static final Den getEastTop(int templateId) {
/* 5434 */     for (int a = 0; a < 100; a++) {
/*      */       
/* 5436 */       int startX = (worldTileSizeX - 5) / 2;
/* 5437 */       int startY = Math.min(worldTileSizeY / 10, 500);
/* 5438 */       int endX = worldTileSizeX - 10;
/* 5439 */       int endY = worldTileSizeY - Math.min(worldTileSizeY / 10, 500);
/* 5440 */       Zone[] zones = getZonesCoveredBy(startX, startY, endX, endY, true);
/* 5441 */       Zone highest = null;
/* 5442 */       float top = 0.0F;
/* 5443 */       for (int x = 0; x < zones.length; x++) {
/*      */         
/* 5445 */         if ((zones[x]).highest > top)
/*      */         {
/* 5447 */           if (top == 0.0F || Server.rand.nextInt(2) == 0) {
/*      */             
/* 5449 */             top = (zones[x]).highest;
/* 5450 */             highest = zones[x];
/*      */           } 
/*      */         }
/*      */       } 
/* 5454 */       if (highest != null) {
/*      */         
/* 5456 */         int tx = 0;
/* 5457 */         int ty = 0;
/* 5458 */         short h = 0;
/* 5459 */         MeshIO mesh = Server.surfaceMesh;
/* 5460 */         for (int i = highest.startX; i <= highest.endX; i++) {
/*      */           
/* 5462 */           for (int y = highest.startY; y < highest.endY; y++) {
/*      */             
/* 5464 */             Village v = Villages.getVillageWithPerimeterAt(i, y, true);
/* 5465 */             if (v == null) {
/*      */               
/* 5467 */               int tile = mesh.getTile(i, y);
/* 5468 */               short hi = Tiles.decodeHeight(tile);
/* 5469 */               if (hi > h) {
/*      */                 
/* 5471 */                 h = hi;
/* 5472 */                 tx = i;
/* 5473 */                 ty = y;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/* 5478 */         if (h > 0)
/* 5479 */           return new Den(templateId, tx, ty, true); 
/*      */       } 
/*      */     } 
/* 5482 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   static final Den getRandomForest(int templateId) {
/* 5487 */     for (int a = 0; a < 100; a++) {
/*      */       
/* 5489 */       int num1 = Server.rand.nextInt(surfaceZones.length);
/* 5490 */       int num2 = Server.rand.nextInt(surfaceZones.length);
/* 5491 */       Zone zone = surfaceZones[num1][num2];
/* 5492 */       if (zone.isForest) {
/*      */         
/* 5494 */         int tx = 0;
/* 5495 */         int ty = 0;
/* 5496 */         short h = 0;
/* 5497 */         MeshIO mesh = Server.surfaceMesh;
/* 5498 */         for (int x = zone.startX; x <= zone.endX; x++) {
/*      */           
/* 5500 */           for (int y = zone.startY; y < zone.endY; y++) {
/*      */             
/* 5502 */             Village v = Villages.getVillageWithPerimeterAt(x, y, true);
/* 5503 */             if (v == null) {
/*      */               
/* 5505 */               int tile = mesh.getTile(x, y);
/* 5506 */               short hi = Tiles.decodeHeight(tile);
/* 5507 */               if (hi > h) {
/*      */                 
/* 5509 */                 h = hi;
/* 5510 */                 tx = x;
/* 5511 */                 ty = y;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/* 5516 */         if (h > 0)
/* 5517 */           return new Den(templateId, tx, ty, true); 
/*      */       } 
/*      */     } 
/* 5520 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void releaseAllCorpsesFor(Creature creature) {
/* 5525 */     int worldSize = 1 << Constants.meshSize; int x;
/* 5526 */     for (x = 0; x < worldSize >> 6; x++) {
/*      */       
/* 5528 */       for (int y = 0; y < worldSize >> 6; y++) {
/*      */         
/* 5530 */         Item[] its = surfaceZones[x][y].getAllItems();
/* 5531 */         for (int itx = 0; itx < its.length; itx++) {
/*      */           
/* 5533 */           if (its[itx].getTemplateId() == 272)
/*      */           {
/* 5535 */             if (its[itx].getName().equals("corpse of " + creature.getName()))
/*      */             {
/* 5537 */               its[itx].setProtected(false);
/*      */             }
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 5543 */     for (x = 0; x < worldSize >> 6; x++) {
/*      */       
/* 5545 */       for (int y = 0; y < worldSize >> 6; y++) {
/*      */         
/* 5547 */         Item[] its = caveZones[x][y].getAllItems();
/* 5548 */         for (int itx = 0; itx < its.length; itx++) {
/*      */           
/* 5550 */           if (its[itx].getTemplateId() == 272)
/*      */           {
/* 5552 */             if (its[itx].getName().equals("corpse of " + creature.getName()))
/*      */             {
/* 5554 */               its[itx].setProtected(false);
/*      */             }
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final byte getMiningState(int x, int y) {
/* 5564 */     Map<Integer, Byte> tab = miningTiles.get(Integer.valueOf(x));
/* 5565 */     if (tab == null) {
/* 5566 */       return 0;
/*      */     }
/*      */     
/* 5569 */     Byte b = tab.get(Integer.valueOf(y));
/* 5570 */     if (b == null) {
/* 5571 */       return 0;
/*      */     }
/* 5573 */     return b.byteValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void setMiningState(int x, int y, byte state, boolean load) {
/* 5579 */     Map<Integer, Byte> tab = miningTiles.get(Integer.valueOf(x));
/* 5580 */     boolean save = false;
/* 5581 */     if (tab == null) {
/*      */       
/* 5583 */       if (state > 0 || state == -1)
/*      */       {
/* 5585 */         tab = new Hashtable<>();
/* 5586 */         tab.put(Integer.valueOf(y), Byte.valueOf(state));
/* 5587 */         miningTiles.put(Integer.valueOf(x), tab);
/* 5588 */         save = true;
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/* 5594 */     else if (state > 0 || state == -1 || tab.get(Integer.valueOf(y)) != null) {
/*      */       
/* 5596 */       tab.put(Integer.valueOf(y), Byte.valueOf(state));
/* 5597 */       save = true;
/*      */     } 
/*      */     
/* 5600 */     if (!load && save)
/*      */     {
/* 5602 */       saveMiningTile(x, y, state);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void loadAllMiningTiles() {
/* 5608 */     Connection dbcon = null;
/* 5609 */     PreparedStatement ps = null;
/* 5610 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 5613 */       long start = System.nanoTime();
/*      */       
/* 5615 */       dbcon = DbConnector.getZonesDbCon();
/* 5616 */       ps = dbcon.prepareStatement("SELECT * FROM MINING");
/*      */       
/* 5618 */       rs = ps.executeQuery();
/* 5619 */       int a = 0;
/* 5620 */       while (rs.next()) {
/*      */         
/* 5622 */         setMiningState(rs.getInt("TILEX"), rs.getInt("TILEY"), rs.getByte("STATE"), true);
/* 5623 */         a++;
/*      */       } 
/* 5625 */       logger.log(Level.INFO, "Loaded " + a + " mining tiles, that took " + ((float)(System.nanoTime() - start) / 1000000.0F) + " ms");
/*      */     
/*      */     }
/* 5628 */     catch (SQLException sqx) {
/*      */       
/* 5630 */       logger.log(Level.WARNING, "Failed to load miningtiles", sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 5634 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 5635 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void saveMiningTile(int tilex, int tiley, byte state) {
/* 5641 */     Connection dbcon = null;
/* 5642 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 5645 */       dbcon = DbConnector.getZonesDbCon();
/* 5646 */       if (!exists(dbcon, tilex, tiley)) {
/*      */         
/* 5648 */         createMiningTile(dbcon, tilex, tiley, state);
/*      */         return;
/*      */       } 
/* 5651 */       ps = dbcon.prepareStatement("UPDATE MINING SET STATE=? WHERE TILEX=? AND TILEY=?");
/* 5652 */       ps.setByte(1, state);
/* 5653 */       ps.setInt(2, tilex);
/* 5654 */       ps.setInt(3, tiley);
/*      */       
/* 5656 */       ps.executeUpdate();
/*      */     }
/* 5658 */     catch (SQLException sqex) {
/*      */       
/* 5660 */       logger.log(Level.WARNING, "Failed to save miningtile " + tilex + ", " + tiley + ", " + state + ":" + sqex
/* 5661 */           .getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 5665 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 5666 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void createMiningTile(Connection dbcon, int tilex, int tiley, byte state) {
/* 5672 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 5675 */       ps = dbcon.prepareStatement("INSERT INTO MINING (STATE,TILEX,TILEY) VALUES(?,?,?)");
/* 5676 */       ps.setByte(1, state);
/* 5677 */       ps.setInt(2, tilex);
/* 5678 */       ps.setInt(3, tiley);
/*      */       
/* 5680 */       ps.executeUpdate();
/*      */     }
/* 5682 */     catch (SQLException sqex) {
/*      */       
/* 5684 */       logger.log(Level.WARNING, "Failed to save miningtile " + tilex + ", " + tiley + ", " + state + ":" + sqex
/* 5685 */           .getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 5689 */       DbUtilities.closeDatabaseObjects(ps, null);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void deleteMiningTile(int tilex, int tiley) {
/* 5695 */     Connection dbcon = null;
/* 5696 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 5699 */       dbcon = DbConnector.getZonesDbCon();
/* 5700 */       ps = dbcon.prepareStatement("DELETE FROM MINING WHERE TILEX=? AND TILEY=?");
/* 5701 */       ps.setInt(1, tilex);
/* 5702 */       ps.setInt(2, tiley);
/* 5703 */       ps.executeUpdate();
/*      */     }
/* 5705 */     catch (SQLException sqex) {
/*      */       
/* 5707 */       logger.log(Level.WARNING, "Failed to delete miningtile " + tilex + ", " + tiley + ":" + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 5711 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 5712 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean exists(Connection dbcon, int tilex, int tiley) throws SQLException {
/* 5718 */     PreparedStatement ps = null;
/* 5719 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 5722 */       ps = dbcon.prepareStatement("SELECT STATE FROM MINING WHERE TILEX=? AND TILEY=?");
/* 5723 */       ps.setInt(1, tilex);
/* 5724 */       ps.setInt(2, tiley);
/* 5725 */       rs = ps.executeQuery();
/* 5726 */       return rs.next();
/*      */     }
/*      */     finally {
/*      */       
/* 5730 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void spawnSanta(byte kingdom) {
/* 5741 */     Village v = Villages.getFirstPermanentVillageForKingdom(kingdom);
/* 5742 */     if (v == null)
/* 5743 */       v = Villages.getCapital(kingdom); 
/* 5744 */     if (v == null)
/* 5745 */       v = Villages.getFirstVillageForKingdom(kingdom); 
/* 5746 */     int tilex = v.startx + (v.endx - v.startx) / 2;
/* 5747 */     int tiley = v.starty + (v.endy - v.starty) / 2;
/* 5748 */     if (v != null) {
/*      */       
/*      */       try {
/*      */         
/* 5752 */         tilex = v.getToken().getTileX();
/* 5753 */         tiley = v.getToken().getTileY();
/*      */       }
/* 5755 */       catch (NoSuchItemException noSuchItemException) {}
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 5760 */     int temp = 46;
/* 5761 */     String name = "Santa Claus";
/* 5762 */     Kingdom k = Kingdoms.getKingdom(kingdom);
/* 5763 */     if (k != null && k.getTemplate() == 3) {
/*      */       
/* 5765 */       temp = 47;
/* 5766 */       name = "Evil Santa";
/*      */     } 
/*      */     
/*      */     try {
/* 5770 */       evilsanta = Creature.doNew(temp, (tilex * 4 + 1), (tiley * 4 + 1), 154.0F, 0, name, (byte)0, kingdom);
/*      */     
/*      */     }
/* 5773 */     catch (Exception ex) {
/*      */       
/* 5775 */       logger.log(Level.WARNING, "Failed to create santa! " + ex.getMessage(), ex);
/*      */     } 
/*      */     
/*      */     try {
/* 5779 */       ItemFactory.createItem(442, 90.0F, (tilex * 4), ((tiley + 1) * 4), 154.0F, true, (byte)0, -10L, null);
/*      */ 
/*      */       
/* 5782 */       sendChristmasEffect(evilsanta, null);
/*      */     }
/* 5784 */     catch (Exception ex) {
/*      */       
/* 5786 */       logger.log(Level.WARNING, "Failed to create evil julbord " + ex.getMessage(), ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void loadChristmas() {
/* 5792 */     if (!isHasLoadedChristmas()) {
/*      */ 
/*      */ 
/*      */       
/* 5796 */       Server.getInstance().broadCastSafe("Merry Christmas!");
/* 5797 */       if (!Servers.localServer.HOMESERVER) {
/*      */ 
/*      */         
/* 5800 */         int tilex = Servers.localServer.SPAWNPOINTLIBX - 2;
/* 5801 */         int tiley = Servers.localServer.SPAWNPOINTLIBY - 2;
/* 5802 */         Village v = Villages.getFirstPermanentVillageForKingdom((byte)3);
/* 5803 */         if (v == null)
/* 5804 */           v = Villages.getFirstVillageForKingdom((byte)3); 
/* 5805 */         evilsanta = addSanta(v, tilex, tiley, 154, 47, "Evil Santa", (byte)3);
/*      */         
/* 5807 */         tilex = Servers.localServer.SPAWNPOINTJENNX - 2;
/* 5808 */         tiley = Servers.localServer.SPAWNPOINTJENNY - 2;
/*      */         
/* 5810 */         v = Villages.getFirstPermanentVillageForKingdom((byte)1);
/* 5811 */         if (v == null)
/* 5812 */           v = Villages.getFirstVillageForKingdom((byte)1); 
/* 5813 */         Zones.santa = addSanta(v, tilex, tiley, 154, 46, "Santa Claus", (byte)1);
/*      */         
/* 5815 */         tilex = Servers.localServer.SPAWNPOINTMOLX - 2;
/* 5816 */         tiley = Servers.localServer.SPAWNPOINTMOLY - 2;
/* 5817 */         v = Villages.getFirstPermanentVillageForKingdom((byte)2);
/* 5818 */         if (v == null)
/* 5819 */           v = Villages.getFirstVillageForKingdom((byte)2); 
/* 5820 */         santaMolRehan = addSanta(v, tilex, tiley, 94, 46, "Twin Santa", (byte)2);
/*      */         
/* 5822 */         v = Villages.getFirstPermanentVillageForKingdom((byte)4);
/* 5823 */         if (v == null)
/* 5824 */           v = Villages.getFirstVillageForKingdom((byte)4); 
/* 5825 */         if (v != null)
/*      */         {
/* 5827 */           Creature santa = addSanta(v, tilex, tiley, 94, 46, "Santa Claus", (byte)4);
/* 5828 */           santas.put(Long.valueOf(santa.getWurmId()), santa);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 5833 */         int tilex = Servers.localServer.SPAWNPOINTJENNX - 2;
/* 5834 */         int tiley = Servers.localServer.SPAWNPOINTJENNY - 2;
/* 5835 */         Village v = Villages.getFirstPermanentVillageForKingdom(Servers.localServer.KINGDOM);
/* 5836 */         if (v == null)
/* 5837 */           v = Villages.getFirstVillageForKingdom(Servers.localServer.KINGDOM); 
/* 5838 */         if (v != null) {
/*      */           
/*      */           try {
/*      */             
/* 5842 */             tilex = v.getToken().getTileX();
/* 5843 */             tiley = v.getToken().getTileY();
/*      */           }
/* 5845 */           catch (NoSuchItemException noSuchItemException) {}
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/* 5853 */           Zones.santa = Creature.doNew(46, (tilex * 4 + 1), (tiley * 4 + 1), 90.0F, 0, "Santa Claus", (byte)0, Servers.localServer.KINGDOM);
/*      */         
/*      */         }
/* 5856 */         catch (Exception ex) {
/*      */           
/* 5858 */           logger.log(Level.WARNING, "Failed to create santa! " + ex.getMessage(), ex);
/*      */         } 
/*      */ 
/*      */         
/*      */         try {
/* 5863 */           Item julbord = ItemFactory.createItem(442, 90.0F, ((tilex + 1) * 4), (tiley * 4), 96.0F, true, (byte)0, -10L, null);
/*      */ 
/*      */           
/* 5866 */           sendChristmasEffect(Zones.santa, null);
/*      */         }
/* 5868 */         catch (Exception ex) {
/*      */           
/* 5870 */           logger.log(Level.WARNING, "Failed to create julbord " + ex.getMessage(), ex);
/*      */         } 
/*      */       } 
/* 5873 */       setHasLoadedChristmas(true);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private static Creature addSanta(@Nullable Village v, int tilex, int tiley, int rot, int santaTemplate, String santaName, byte kingdom) {
/* 5881 */     Creature santa = null;
/* 5882 */     if (v != null) {
/*      */       
/*      */       try {
/*      */ 
/*      */         
/* 5887 */         tilex = v.getToken().getTileX();
/* 5888 */         tiley = v.getToken().getTileY();
/*      */       }
/* 5890 */       catch (NoSuchItemException noSuchItemException) {}
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 5897 */       santa = Creature.doNew(santaTemplate, (tilex * 4 + 2), (tiley * 4) + 0.75F, 90.0F, 0, santaName, (byte)0, kingdom);
/*      */       
/* 5899 */       sendChristmasEffect(santa, null);
/*      */       
/*      */       try {
/* 5902 */         ItemFactory.createItem(442, 90.0F, (tilex * 4 + 1), (tiley * 4 + 2), 90.0F, true, (byte)0, -10L, null);
/*      */       
/*      */       }
/* 5905 */       catch (Exception ex) {
/*      */         
/* 5907 */         logger.log(Level.WARNING, "Failed to create julbord " + ex.getMessage(), ex);
/*      */       }
/*      */     
/* 5910 */     } catch (Exception ex) {
/*      */       
/* 5912 */       logger.log(Level.WARNING, "Failed to create " + santaName + "! " + ex.getMessage(), ex);
/*      */     } 
/* 5914 */     return santa;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void sendChristmasEffect(Creature creature, Item item) {
/* 5919 */     Player[] players = Players.getInstance().getPlayers();
/* 5920 */     for (int x = 0; x < players.length; x++) {
/*      */       
/* 5922 */       if (item != null) {
/* 5923 */         players[x].getCommunicator().sendAddEffect(creature.getWurmId(), (short)4, item.getPosX(), item
/* 5924 */             .getPosY(), item.getPosZ(), (byte)0);
/*      */       } else {
/* 5926 */         players[x].getCommunicator().sendAddEffect(creature.getWurmId(), (short)4, creature.getPosX(), creature
/* 5927 */             .getPosY(), creature.getPositionZ(), (byte)0);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void removeChristmasEffect(Creature idcreature) {
/* 5933 */     if (idcreature != null) {
/*      */       
/* 5935 */       Player[] players = Players.getInstance().getPlayers();
/* 5936 */       for (int x = 0; x < players.length; x++)
/*      */       {
/* 5938 */         players[x].getCommunicator().sendRemoveEffect(idcreature.getWurmId());
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void deleteChristmas() {
/* 5945 */     if (hasLoadedChristmas) {
/*      */       
/* 5947 */       logger.log(Level.INFO, "Starting christmas deletion.");
/* 5948 */       setHasLoadedChristmas(false);
/* 5949 */       if (evilsanta != null) {
/*      */         
/* 5951 */         removeChristmasEffect(evilsanta);
/* 5952 */         MethodsCreatures.destroyCreature(evilsanta);
/* 5953 */         evilsanta = null;
/*      */       } 
/* 5955 */       if (Zones.santa != null) {
/*      */         
/* 5957 */         removeChristmasEffect(Zones.santa);
/* 5958 */         MethodsCreatures.destroyCreature(Zones.santa);
/* 5959 */         Zones.santa = null;
/*      */       } 
/* 5961 */       if (santaMolRehan != null) {
/*      */         
/* 5963 */         removeChristmasEffect(santaMolRehan);
/* 5964 */         MethodsCreatures.destroyCreature(santaMolRehan);
/* 5965 */         santaMolRehan = null;
/*      */       } 
/* 5967 */       if (!santas.isEmpty()) {
/*      */         
/* 5969 */         for (Creature santa : santas.values()) {
/*      */           
/* 5971 */           removeChristmasEffect(santa);
/* 5972 */           MethodsCreatures.destroyCreature(santa);
/*      */         } 
/* 5974 */         santas.clear();
/*      */       } 
/* 5976 */       Items.deleteChristmasItems();
/* 5977 */       logger.log(Level.INFO, "Christmas deletion done.");
/* 5978 */       Server.getInstance().broadCastSafe("Christmas is over!");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static TilePos safeTile(@Nonnull TilePos tilePos) {
/* 5989 */     tilePos.set(safeTileX(tilePos.x), safeTileY(tilePos.y));
/* 5990 */     return tilePos;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int safeTileX(int tilex) {
/* 5995 */     return Math.max(0, Math.min(tilex, worldTileSizeX - 1));
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int safeTileY(int tiley) {
/* 6000 */     return Math.max(0, Math.min(tiley, worldTileSizeY - 1));
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] getClosestSpring(int tilex, int tiley, int dist) {
/* 6005 */     tilex = safeTileX(tilex);
/* 6006 */     tiley = safeTileY(tiley);
/* 6007 */     int closestX = -1;
/* 6008 */     int closestY = -1;
/*      */     
/* 6010 */     for (int x = safeTileX(tilex - dist); x < safeTileX(tilex + 1 + dist); x++) {
/*      */       
/* 6012 */       for (int y = safeTileY(tiley - dist); y < safeTileY(tiley + 1 + dist); y++) {
/*      */         
/* 6014 */         if (Zone.hasSpring(x, y))
/*      */         {
/* 6016 */           if (closestX < 0 || closestY < 0) {
/*      */             
/* 6018 */             closestX = Math.abs(tilex - x);
/* 6019 */             closestY = Math.abs(tiley - y);
/*      */           }
/*      */           else {
/*      */             
/* 6023 */             int dx1 = tilex - x;
/* 6024 */             int dy1 = tiley - y;
/* 6025 */             int dx2 = closestX;
/* 6026 */             int dy2 = closestY;
/* 6027 */             if (Math.sqrt((dx1 * dx1 + dy1 * dy1)) < Math.sqrt((dx2 * dx2 + dy2 * dy2))) {
/*      */               
/* 6029 */               closestX = Math.abs(dx1);
/* 6030 */               closestY = Math.abs(dy1);
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/* 6036 */     return new int[] { closestX, closestY };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setHasLoadedChristmas(boolean aHasLoadedChristmas) {
/* 6042 */     hasLoadedChristmas = aHasLoadedChristmas;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isHasLoadedChristmas() {
/* 6047 */     return hasLoadedChristmas;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean sendNewYear() {
/* 6056 */     for (int a = 0; a < Math.min(500, worldTileSizeX / 4); a++) {
/*      */       
/* 6058 */       short effectType = (short)(5 + Server.rand.nextInt(5));
/* 6059 */       float x = (Server.rand.nextInt(worldTileSizeX) * 4);
/* 6060 */       float y = (Server.rand.nextInt(worldTileSizeY) * 4);
/*      */       
/*      */       try {
/* 6063 */         float h = calculateHeight(x, y, true);
/* 6064 */         Players.getInstance().sendEffect(effectType, x, y, Math.max(0.0F, h) + 10.0F + Server.rand.nextInt(30), true, 500.0F);
/*      */       }
/* 6066 */       catch (NoSuchZoneException noSuchZoneException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 6071 */     return true;
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
/*      */   public static void removeNewYear() {
/* 6107 */     if (hasStartedYet) {
/*      */       
/* 6109 */       Player[] players = Players.getInstance().getPlayers();
/* 6110 */       LongPosition l = posmap.removeFirst();
/* 6111 */       for (int p = 0; p < players.length; p++)
/*      */       {
/* 6113 */         players[p].getCommunicator().sendRemoveEffect(l.getId());
/*      */       }
/* 6115 */       if (posmap.isEmpty())
/*      */       {
/* 6117 */         hasStartedYet = false;
/*      */       }
/*      */       return;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void sendNewYearsEffectsToPlayer(Player p) {
/* 6129 */     if (posmap.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/* 6133 */     for (Iterator<LongPosition> it = posmap.iterator(); it.hasNext();) {
/*      */ 
/*      */       
/*      */       try {
/* 6137 */         LongPosition l = it.next();
/* 6138 */         p.getCommunicator().sendAddEffect(l.getId(), l.getEffectType(), (l.getTilex() << 2), (l.getTiley() << 2), 
/* 6139 */             calculateHeight((l.getTilex() << 2), (l.getTiley() << 2), true), (byte)0);
/*      */       }
/* 6141 */       catch (NoSuchZoneException noSuchZoneException) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean interruptedRange(Creature performer, Creature defender) {
/* 6151 */     if (performer == null || defender == null)
/* 6152 */       return true; 
/* 6153 */     if (performer.equals(defender))
/* 6154 */       return false; 
/* 6155 */     if (performer.isOnSurface() != defender.isOnSurface()) {
/*      */       
/* 6157 */       performer.getCommunicator().sendCombatNormalMessage("You fail to get a clear view of " + defender.getName() + ".");
/* 6158 */       return true;
/*      */     } 
/* 6160 */     resetCoverHolder();
/* 6161 */     if (performer.isOnSurface()) {
/*      */       
/* 6163 */       BlockingResult blockers = Blocking.getBlockerBetween(performer, performer.getPosX(), performer.getPosY(), defender
/* 6164 */           .getPosX(), defender.getPosY(), performer.getPositionZ(), defender.getPositionZ(), performer
/* 6165 */           .isOnSurface(), defender.isOnSurface(), true, 4, -1L, performer.getBridgeId(), performer
/* 6166 */           .getBridgeId(), false);
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
/* 6180 */       if (blockers != null && (
/* 6181 */         blockers.getTotalCover() >= 100.0F || ((
/* 6182 */         !performer.isOnPvPServer() || !defender.isOnPvPServer()) && blockers
/* 6183 */         .getFirstBlocker() != null)))
/*      */       {
/* 6185 */         if (performer.getCitizenVillage() == null || !performer.getCitizenVillage().isEnemy(defender)) {
/*      */           
/* 6187 */           performer.getCommunicator().sendCombatNormalMessage("You fail to get a clear view of " + defender
/* 6188 */               .getName() + ".");
/* 6189 */           return true;
/*      */         } 
/*      */       }
/*      */     } 
/* 6193 */     if (!defender.isWithinDistanceTo(performer, 4.0F))
/*      */     {
/* 6195 */       if (!VirtualZone.isCreatureTurnedTowardsTarget(defender, performer, 60.0F, false)) {
/*      */         
/* 6197 */         performer.getCommunicator().sendCombatNormalMessage("You must turn towards " + defender
/* 6198 */             .getName() + " in order to see it.");
/* 6199 */         return true;
/*      */       } 
/*      */     }
/* 6202 */     PathFinder pf = new PathFinder(true);
/*      */     
/*      */     try {
/* 6205 */       Path path = pf.rayCast((performer.getCurrentTile()).tilex, (performer.getCurrentTile()).tiley, 
/* 6206 */           (defender.getCurrentTile()).tilex, (defender.getCurrentTile()).tiley, performer.isOnSurface(), (
/* 6207 */           (int)Creature.getRange(performer, defender.getPosX(), defender.getPosY()) >> 2) + 5);
/* 6208 */       float initialHeight = Math.max(0.0F, performer.getPositionZ() + performer.getAltOffZ() + 1.4F);
/* 6209 */       float targetHeight = Math.max(0.0F, defender.getPositionZ() + defender.getAltOffZ() + 1.4F);
/* 6210 */       double distx = Math.pow(((performer.getCurrentTile()).tilex - (defender.getCurrentTile()).tilex), 2.0D);
/* 6211 */       double disty = Math.pow(((performer.getCurrentTile()).tiley - (defender.getCurrentTile()).tiley), 2.0D);
/* 6212 */       double dist = Math.sqrt(distx + disty);
/* 6213 */       double dx = (targetHeight - initialHeight) / dist;
/* 6214 */       while (!path.isEmpty())
/*      */       {
/* 6216 */         PathTile p = path.getFirst();
/* 6217 */         distx = Math.pow((p.getTileX() - (defender.getCurrentTile()).tilex), 2.0D);
/* 6218 */         disty = Math.pow((p.getTileY() - (defender.getCurrentTile()).tiley), 2.0D);
/* 6219 */         double currdist = Math.sqrt(distx + disty);
/*      */ 
/*      */ 
/*      */         
/* 6223 */         float currHeight = Math.max(0.0F, getLowestCorner(p.getTileX(), p.getTileY(), performer.getLayer()));
/*      */         
/* 6225 */         double distmod = currdist * dx;
/* 6226 */         if (dx < 0.0D) {
/*      */ 
/*      */           
/* 6229 */           if (currHeight > targetHeight - distmod)
/*      */           {
/* 6231 */             performer.getCommunicator().sendCombatNormalMessage("You fail to get a clear view.");
/*      */             
/* 6233 */             return true;
/*      */           
/*      */           }
/*      */         
/*      */         }
/* 6238 */         else if (currHeight > targetHeight - distmod) {
/*      */           
/* 6240 */           performer.getCommunicator().sendCombatNormalMessage("You fail to get a clear view.");
/*      */           
/* 6242 */           return true;
/*      */         } 
/*      */         
/* 6245 */         path.removeFirst();
/*      */       }
/*      */     
/* 6248 */     } catch (NoPathException np) {
/*      */       
/* 6250 */       performer.getCommunicator().sendCombatNormalMessage("You fail to get a clear view.");
/* 6251 */       return true;
/*      */     } 
/* 6253 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Set<PathTile> explode(int tilex, int tiley, int floorLevel, boolean followStructure, int diameter) {
/* 6259 */     Set<PathTile> toRet = new HashSet<>();
/* 6260 */     VolaTile current = getTileOrNull(tilex, tiley, (floorLevel >= 0));
/*      */     
/* 6262 */     if (current != null && current.getStructure() != null) {
/*      */       
/* 6264 */       Structure structure = current.getStructure();
/* 6265 */       if (followStructure)
/*      */       {
/* 6267 */         for (int x = -diameter; x <= diameter; x++) {
/* 6268 */           for (int y = -diameter; y <= diameter; y++)
/*      */           {
/* 6270 */             toRet = checkStructureTile(structure, tilex, tiley, x, y, floorLevel, toRet);
/*      */           }
/*      */         }
/*      */       
/*      */       }
/*      */     } else {
/*      */       
/* 6277 */       for (int x = -diameter; x <= diameter; x++) {
/* 6278 */         for (int y = -diameter; y <= diameter; y++)
/*      */         {
/* 6280 */           toRet = checkStructureTile(null, tilex, tiley, x, y, floorLevel, toRet);
/*      */         }
/*      */       } 
/*      */     } 
/* 6284 */     return toRet;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final Set<PathTile> checkStructureTile(Structure structure, int tilex, int tiley, int xmod, int ymod, int floorLevel, Set<PathTile> toRet) {
/* 6290 */     int tx = tilex + xmod;
/* 6291 */     int ty = tiley + ymod;
/* 6292 */     if (containsStructure(structure, tx, ty, floorLevel))
/*      */     {
/* 6294 */       toRet.add(getPathTile(tx, ty, (floorLevel >= 0), floorLevel));
/*      */     }
/* 6296 */     return toRet;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean containsStructure(Structure structure, int tx, int ty, int floorLevel) {
/* 6301 */     VolaTile current = getTileOrNull(tx, ty, (floorLevel >= 0));
/* 6302 */     if (structure == null && (current == null || current.getStructure() == null))
/* 6303 */       return true; 
/* 6304 */     return (current != null && current.getStructure() == structure);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final PathTile getPathTile(int tx, int ty, boolean surfaced, int floorLevel) {
/* 6309 */     boolean surface = (floorLevel >= 0);
/* 6310 */     int tileNum2 = getMesh(surface).getTile(tx, ty);
/* 6311 */     return new PathTile(tx, ty, tileNum2, surface, (byte)floorLevel);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final float getLowestCorner(int tilex, int tiley, int layer) {
/* 6316 */     tilex = safeTileX(tilex);
/* 6317 */     tiley = safeTileY(tiley);
/* 6318 */     if (layer >= 0) {
/*      */       
/* 6320 */       float f = Tiles.decodeHeightAsFloat(Server.surfaceMesh.getTile(tilex, tiley));
/* 6321 */       for (int i = 0; i <= 1; i++) {
/*      */         
/* 6323 */         for (int y = 0; y <= 1; y++)
/*      */         {
/* 6325 */           f = Math.min(Tiles.decodeHeightAsFloat(Server.surfaceMesh.getTile(tilex + y, tiley + y)), f);
/*      */         }
/*      */       } 
/* 6328 */       return f;
/*      */     } 
/*      */ 
/*      */     
/* 6332 */     float lowest = Tiles.decodeHeightAsFloat(Server.caveMesh.getTile(tilex, tiley));
/* 6333 */     for (int x = 0; x <= 1; x++) {
/*      */       
/* 6335 */       for (int y = 0; y <= 1; y++)
/*      */       {
/* 6337 */         lowest = Math.min(Tiles.decodeHeightAsFloat(Server.caveMesh.getTile(tilex + y, tiley + y)), lowest);
/*      */       }
/*      */     } 
/* 6340 */     return lowest;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int getSpiritsForTile(int tilex, int tiley, boolean surfaced) {
/* 6346 */     if (!surfaced)
/* 6347 */       return 3; 
/*      */     int x;
/* 6349 */     for (x = safeTileX(tilex - 2); x <= safeTileX(tilex + 2); x++) {
/* 6350 */       for (int y = safeTileY(tiley - 2); y <= safeTileY(tiley + 2); y++) {
/*      */         
/* 6352 */         if (Features.Feature.SURFACEWATER.isEnabled())
/*      */         {
/* 6354 */           if (Water.getSurfaceWater(x, y) > 0)
/* 6355 */             return 2; 
/*      */         }
/* 6357 */         if (Tiles.decodeHeight(Server.surfaceMesh.getTile(x, y)) <= 0)
/* 6358 */           return 2; 
/*      */       } 
/* 6360 */     }  for (x = safeTileX(tilex - 2); x <= safeTileX(tilex + 2); x++) {
/* 6361 */       for (int y = safeTileX(tiley - 2); y <= safeTileY(tiley + 2); y++) {
/*      */         
/* 6363 */         if (Tiles.decodeType(Server.surfaceMesh.getTile(x, y)) == Tiles.Tile.TILE_LAVA.id)
/* 6364 */           return 1; 
/* 6365 */         VolaTile t = getTileOrNull(x, y, surfaced);
/* 6366 */         if (t != null)
/* 6367 */           for (Item i : t.getItems()) {
/*      */             
/* 6369 */             if (i.getTemperature() > 1000)
/* 6370 */               return 1; 
/*      */           }  
/*      */       } 
/* 6373 */     }  if (Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex, tiley)) > 1000)
/* 6374 */       return 4; 
/* 6375 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MeshIO getMesh(boolean surface) {
/* 6386 */     return surface ? Server.surfaceMesh : Server.caveMesh;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void initializeHiveZones() {
/* 6391 */     for (int y = 0; y < hiveZoneSizeY; y++) {
/* 6392 */       for (int x = 0; x < hiveZoneSizeX; x++) {
/* 6393 */         hiveZones.add(null);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void addHive(Item hive, boolean silent) {
/* 6400 */     int actualZone = Math.max(0, hive.getTileY() / 32) * hiveZoneSizeX + Math.max(0, hive.getTileX() / 32);
/* 6401 */     ConcurrentHashMap<Item, HiveZone> thisZone = hiveZones.get(actualZone);
/* 6402 */     if (thisZone == null) {
/* 6403 */       thisZone = new ConcurrentHashMap<>();
/*      */     }
/* 6405 */     thisZone.put(hive, new HiveZone(hive));
/* 6406 */     hiveZones.set(actualZone, thisZone);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void removeHive(Item hive, boolean silent) {
/* 6413 */     int actualZone = Math.max(0, hive.getTileY() / 32) * hiveZoneSizeX + Math.max(0, hive.getTileX() / 32);
/* 6414 */     ConcurrentHashMap<Item, HiveZone> thisZone = hiveZones.get(actualZone);
/* 6415 */     if (thisZone == null) {
/*      */       
/* 6417 */       logger.log(Level.WARNING, "HiveZone was NULL when it should not have been: " + actualZone);
/*      */       
/*      */       return;
/*      */     } 
/* 6421 */     thisZone.remove(hive);
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
/*      */   public static final HiveZone getHiveZoneAt(int tilex, int tiley, boolean surfaced) {
/* 6433 */     return getHiveZone(tilex, tiley, surfaced);
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
/*      */   public static final boolean isFarFromAnyHive(int tilex, int tiley, boolean surfaced) {
/* 6445 */     if (!surfaced) {
/* 6446 */       return false;
/*      */     }
/* 6448 */     ConcurrentHashMap<Item, HiveZone> thisZone = null;
/* 6449 */     for (int i = -1; i <= 1; i++) {
/*      */       
/* 6451 */       for (int j = -1; j <= 1; j++) {
/*      */ 
/*      */         
/* 6454 */         int actualZone = Math.max(0, Math.min(tiley / 32 + j, hiveZoneSizeY - 1)) * hiveZoneSizeX + Math.max(0, Math.min(tilex / 32 + i, hiveZoneSizeX - 1));
/* 6455 */         if (actualZone < hiveZones.size()) {
/*      */           
/* 6457 */           thisZone = hiveZones.get(actualZone);
/* 6458 */           if (thisZone != null)
/*      */           {
/*      */             
/* 6461 */             for (HiveZone hz : thisZone.values()) {
/*      */               
/* 6463 */               if (hz.isCloseToTile(tilex, tiley))
/* 6464 */                 return false; 
/*      */             }  } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 6469 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static final HiveZone getHiveZone(int tilex, int tiley, boolean surfaced) {
/* 6475 */     if (!surfaced) {
/* 6476 */       return null;
/*      */     }
/* 6478 */     HiveZone toReturn = null;
/* 6479 */     ConcurrentHashMap<Item, HiveZone> thisZone = null;
/* 6480 */     for (int i = -1; i <= 1; i++) {
/*      */       
/* 6482 */       for (int j = -1; j <= 1; j++) {
/*      */ 
/*      */         
/* 6485 */         int actualZone = Math.max(0, Math.min(tiley / 32 + j, hiveZoneSizeY - 1)) * hiveZoneSizeX + Math.max(0, Math.min(tilex / 32 + i, hiveZoneSizeX - 1));
/* 6486 */         if (actualZone < hiveZones.size()) {
/*      */           
/* 6488 */           thisZone = hiveZones.get(actualZone);
/* 6489 */           if (thisZone != null)
/*      */           {
/*      */             
/* 6492 */             for (HiveZone hz : thisZone.values()) {
/*      */               
/* 6494 */               if (hz.containsTile(tilex, tiley)) {
/*      */                 
/* 6496 */                 if (hz.getStrengthForTile(tilex, tiley, surfaced) <= 0) {
/*      */                   continue;
/*      */                 }
/* 6499 */                 if (toReturn == null) {
/* 6500 */                   toReturn = hz; continue;
/* 6501 */                 }  if (toReturn.getStrengthForTile(tilex, tiley, surfaced) < hz.getStrengthForTile(tilex, tiley, surfaced)) {
/* 6502 */                   toReturn = hz; continue;
/* 6503 */                 }  if (toReturn.getStrengthForTile(tilex, tiley, surfaced) == hz.getStrengthForTile(tilex, tiley, surfaced))
/*      */                 {
/* 6505 */                   if (hz.getCurrentHive().getCurrentQualityLevel() > toReturn.getCurrentHive().getCurrentQualityLevel())
/* 6506 */                     toReturn = hz;  } 
/*      */               } 
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 6513 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Item[] getHives(int hiveType) {
/* 6523 */     Set<Item> hiveZoneSet = new HashSet<>();
/* 6524 */     for (ConcurrentHashMap<Item, HiveZone> thisZone : hiveZones) {
/*      */       
/* 6526 */       if (thisZone == null) {
/*      */         continue;
/*      */       }
/* 6529 */       for (HiveZone hz : thisZone.values()) {
/*      */         
/* 6531 */         if (hz.getCurrentHive().getTemplateId() == hiveType) {
/* 6532 */           hiveZoneSet.add(hz.getCurrentHive());
/*      */         }
/*      */       } 
/*      */     } 
/* 6536 */     return hiveZoneSet.<Item>toArray(new Item[hiveZoneSet.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static final Item getCurrentHive(int tilex, int tiley) {
/* 6547 */     HiveZone z = getHiveZone(tilex, tiley, true);
/* 6548 */     if (z != null && z.getCurrentHive() != null) {
/* 6549 */       return z.getCurrentHive();
/*      */     }
/* 6551 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static final Item getWildHive(int tilex, int tiley) {
/* 6562 */     ConcurrentHashMap<Item, HiveZone> thisZone = null;
/*      */     
/* 6564 */     int actualZone = Math.max(0, Math.min(tiley / 32, hiveZoneSizeY - 1)) * hiveZoneSizeX + Math.max(0, Math.min(tilex / 32, hiveZoneSizeX - 1));
/* 6565 */     if (actualZone >= hiveZones.size())
/* 6566 */       return null; 
/* 6567 */     thisZone = hiveZones.get(actualZone);
/* 6568 */     if (thisZone == null) {
/* 6569 */       return null;
/*      */     }
/* 6571 */     for (HiveZone hz : thisZone.values()) {
/*      */       
/* 6573 */       if (hz.hasHive(tilex, tiley)) {
/*      */         
/* 6575 */         Item hive = hz.getCurrentHive();
/* 6576 */         if (hive.getTemplateId() == 1239) {
/* 6577 */           return hive;
/*      */         }
/*      */       } 
/*      */     } 
/* 6581 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Item[] getActiveDomesticHives(int tilex, int tiley) {
/* 6592 */     Set<Item> hiveZoneSet = new HashSet<>();
/* 6593 */     ConcurrentHashMap<Item, HiveZone> thisZone = null;
/*      */     
/* 6595 */     int actualZone = Math.max(0, Math.min(tiley / 32, hiveZoneSizeY - 1)) * hiveZoneSizeX + Math.max(0, Math.min(tilex / 32, hiveZoneSizeX - 1));
/* 6596 */     if (actualZone >= hiveZones.size())
/* 6597 */       return null; 
/* 6598 */     thisZone = hiveZones.get(actualZone);
/* 6599 */     if (thisZone == null) {
/* 6600 */       return null;
/*      */     }
/* 6602 */     for (HiveZone hz : thisZone.values()) {
/*      */       
/* 6604 */       if (hz.hasHive(tilex, tiley)) {
/*      */         
/* 6606 */         Item hive = hz.getCurrentHive();
/* 6607 */         if (hive.getTemplateId() == 1175) {
/* 6608 */           hiveZoneSet.add(hive);
/*      */         }
/*      */       } 
/*      */     } 
/* 6612 */     return hiveZoneSet.<Item>toArray(new Item[hiveZoneSet.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean removeWildHive(int tilex, int tiley) {
/* 6622 */     Item hive = getWildHive(tilex, tiley);
/* 6623 */     if (hive != null) {
/*      */       
/* 6625 */       for (Item item : hive.getItemsAsArray())
/*      */       {
/* 6627 */         Items.destroyItem(item.getWurmId());
/*      */       }
/* 6629 */       Items.destroyItem(hive.getWurmId());
/* 6630 */       return true;
/*      */     } 
/* 6632 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isGoodTileForSpawn(int tilex, int tiley, boolean surfaced) {
/* 6637 */     return isGoodTileForSpawn(tilex, tiley, surfaced, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isGoodTileForSpawn(int tilex, int tiley, boolean surfaced, boolean canBeOccupied) {
/* 6642 */     if (tilex < 0 || tiley < 0 || tilex > worldTileSizeX || tiley > worldTileSizeY) {
/* 6643 */       return false;
/*      */     }
/* 6645 */     VolaTile t = getTileOrNull(tilex, tiley, surfaced);
/* 6646 */     short[] steepness = Creature.getTileSteepness(tilex, tiley, surfaced);
/* 6647 */     return ((canBeOccupied || t == null) && 
/* 6648 */       Tiles.decodeHeight(getTileIntForTile(tilex, tiley, surfaced ? 0 : -1)) > 0 && (steepness[0] < 23 || steepness[1] < 23));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isVillagePremSpawn(Village village) {
/* 6654 */     return (Servers.localServer.testServer || isPremSpawnZoneAt(village.getStartX(), village.getStartY()) || 
/* 6655 */       isPremSpawnZoneAt(village.getStartX(), village.getEndY()) || 
/* 6656 */       isPremSpawnZoneAt(village.getEndX(), village.getStartY()) || 
/* 6657 */       isPremSpawnZoneAt(village.getEndX(), village.getEndY()));
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
/*      */   public static void reposWildHive(int tilex, int tiley, Tiles.Tile theTile, byte aData) {
/* 6669 */     Item hive = getWildHive(tilex, tiley);
/* 6670 */     if (hive == null)
/*      */       return; 
/* 6672 */     Point4f p = MethodsItems.getHivePos(tilex, tiley, theTile, aData);
/*      */     
/* 6674 */     if (p.getPosZ() > 0.0F) {
/*      */       
/* 6676 */       hive.setPosZ(p.getPosZ());
/* 6677 */       hive.updatePos();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addTurret(Item turret, boolean silent) {
/* 6685 */     turretZones.put(turret, new TurretZone(turret));
/*      */     
/* 6687 */     if (Servers.localServer.testServer) {
/* 6688 */       Players.getInstance().sendGmMessage(null, "System", "Turret added to " + turret.getTileX() + "," + turret.getTileY(), false);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void removeTurret(Item turret, boolean silent) {
/* 6693 */     turretZones.remove(turret);
/*      */     
/* 6695 */     if (Servers.localServer.testServer) {
/* 6696 */       Players.getInstance().sendGmMessage(null, "System", "Turret removed from " + turret.getTileX() + "," + turret.getTileY(), false);
/*      */     }
/*      */   }
/*      */   
/*      */   public static TurretZone getTurretZone(int tileX, int tileY, boolean surfaced) {
/* 6701 */     TurretZone bestTurret = null;
/* 6702 */     for (TurretZone tz : turretZones.values()) {
/*      */       
/* 6704 */       if (!tz.containsTile(tileX, tileY)) {
/*      */         continue;
/*      */       }
/* 6707 */       if (bestTurret == null) {
/* 6708 */         bestTurret = tz; continue;
/* 6709 */       }  if (bestTurret.getStrengthForTile(tileX, tileY, surfaced) < tz.getStrengthForTile(tileX, tileY, surfaced)) {
/* 6710 */         bestTurret = tz;
/*      */       }
/*      */     } 
/* 6713 */     if (bestTurret == null) {
/* 6714 */       return null;
/*      */     }
/* 6716 */     return bestTurret;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Item getCurrentTurret(int tileX, int tileY, boolean surfaced) {
/* 6721 */     TurretZone tz = getTurretZone(tileX, tileY, surfaced);
/* 6722 */     if (tz == null) {
/* 6723 */       return null;
/*      */     }
/* 6725 */     return tz.getZoneItem();
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\Zones.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */