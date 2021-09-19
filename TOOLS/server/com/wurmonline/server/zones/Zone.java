/*      */ package com.wurmonline.server.zones;
/*      */ 
/*      */ import com.wurmonline.math.TilePos;
/*      */ import com.wurmonline.mesh.MeshIO;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.CreatureTemplate;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateIds;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.MineDoorPermission;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.deities.Deity;
/*      */ import com.wurmonline.server.effects.Effect;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemTypes;
/*      */ import com.wurmonline.server.kingdom.Kingdom;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.structures.Door;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.FenceGate;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import com.wurmonline.shared.constants.CreatureTypes;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nonnull;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class Zone
/*      */   implements CounterTypes, MiscConstants, ItemTypes, CreatureTemplateIds, CreatureTypes
/*      */ {
/*      */   public static final String cvsversion = "$Id: Zone.java,v 1.55 2007-04-09 13:40:23 root Exp $";
/*  147 */   private static int ids = 0;
/*      */   
/*  149 */   private static final Logger logger = Logger.getLogger(Zone.class.getName());
/*      */   
/*      */   private Set<Village> villages;
/*      */   
/*      */   final int startX;
/*      */   
/*      */   final int endX;
/*      */   final int startY;
/*      */   final int endY;
/*      */   private final ConcurrentHashMap<Integer, VolaTile> tiles;
/*      */   private final ArrayList<VolaTile> deletionQueue;
/*      */   Set<VirtualZone> zoneWatchers;
/*      */   Set<Structure> structures;
/*      */   private Tracks tracks;
/*      */   int id;
/*      */   boolean isLoaded = true;
/*      */   final boolean isOnSurface;
/*      */   boolean loading = false;
/*      */   private final int size;
/*  168 */   private int creatures = 0;
/*  169 */   private int kingdomCreatures = 0;
/*  170 */   int highest = 0;
/*      */   
/*      */   private boolean allWater = false;
/*      */   
/*      */   private boolean allLand = false;
/*      */   
/*      */   boolean isForest = false;
/*  177 */   Den den = null;
/*  178 */   Item creatureSpawn = null;
/*  179 */   Item treasureChest = null;
/*  180 */   static int spawnPoints = 0;
/*  181 */   static int treasureChests = 0;
/*  182 */   private static final Random r = new Random();
/*      */ 
/*      */ 
/*      */   
/*      */   private static final long SPRING_PRIME = 7919L;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hasRift = false;
/*      */ 
/*      */   
/*      */   private final int spawnSeed;
/*      */ 
/*      */   
/*  196 */   private static final VolaTile[] emptyTiles = new VolaTile[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  201 */   private static final VirtualZone[] emptyWatchers = new VirtualZone[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  206 */   private static final Structure[] emptyStructures = new Structure[0];
/*  207 */   private short pollTicker = 0;
/*      */   public static final int secondsBetweenPolls = 800;
/*  209 */   static final int zonesPolled = Math.max(2, Zones.numberOfZones * 2 / 800);
/*  210 */   public static final int maxZonesPolled = Zones.numberOfZones * 2 / zonesPolled;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  215 */   private static final long LOG_ELAPSED_TIME_THRESHOLD = Constants.lagThreshold;
/*      */   
/*  217 */   private static final int breedingLimit = Servers.localServer.maxCreatures / 25;
/*  218 */   private static final LinkedList<Long> fogSpiders = new LinkedList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Zone(int aStartX, int aEndX, int aStartY, int aEndY, boolean aIsOnSurface) {
/*  224 */     this.id = ids++;
/*  225 */     this.pollTicker = (short)(this.id / zonesPolled);
/*  226 */     this.startX = Zones.safeTileX(aStartX);
/*  227 */     this.startY = Zones.safeTileY(aStartY);
/*      */     
/*  229 */     this.endX = Zones.safeTileX(aEndX);
/*  230 */     this.endY = Zones.safeTileY(aEndY);
/*      */     
/*  232 */     this.size = aEndX - aStartX + 1;
/*  233 */     this.isOnSurface = aIsOnSurface;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  238 */     this.tiles = new ConcurrentHashMap<>();
/*  239 */     this.deletionQueue = new ArrayList<>();
/*  240 */     setTypes();
/*  241 */     this.spawnSeed = Zones.worldTileSizeX / 200;
/*      */   }
/*      */ 
/*      */   
/*      */   final Item[] getAllItems() {
/*  246 */     if (this.tiles != null) {
/*      */       
/*  248 */       Set<Item> items = new HashSet<>();
/*  249 */       for (VolaTile tile : this.tiles.values()) {
/*      */         
/*  251 */         Item[] its = tile.getItems();
/*  252 */         for (Item lIt : its)
/*  253 */           items.add(lIt); 
/*      */       } 
/*  255 */       return items.<Item>toArray(new Item[items.size()]);
/*      */     } 
/*  257 */     return new Item[0];
/*      */   }
/*      */ 
/*      */   
/*      */   public final Creature[] getAllCreatures() {
/*  262 */     if (this.tiles != null) {
/*      */       
/*  264 */       Set<Creature> crets = new HashSet<>();
/*  265 */       for (VolaTile tile : this.tiles.values()) {
/*      */         
/*  267 */         Creature[] its = tile.getCreatures();
/*  268 */         for (Creature lIt : its)
/*  269 */           crets.add(lIt); 
/*      */       } 
/*  271 */       return crets.<Creature>toArray(new Creature[crets.size()]);
/*      */     } 
/*  273 */     return new Creature[0];
/*      */   }
/*      */ 
/*      */   
/*      */   private void setTypes() {
/*  278 */     if (this.isOnSurface) {
/*      */       
/*  280 */       int forest = 0;
/*  281 */       MeshIO mesh = Server.surfaceMesh;
/*  282 */       for (int x = this.startX; x <= this.endX; x++) {
/*      */         
/*  284 */         for (int y = this.startY; y < this.endY; y++) {
/*      */           
/*  286 */           int tile = mesh.getTile(x, y);
/*  287 */           int h = Tiles.decodeHeight(tile);
/*  288 */           if (h > this.highest) {
/*      */             
/*  290 */             this.highest = h;
/*  291 */             this.allWater = false;
/*      */           }
/*  293 */           else if (h < 0) {
/*      */             
/*  295 */             this.allLand = false;
/*      */           } 
/*      */           
/*  298 */           if (Tiles.isTree(Tiles.decodeType(tile)))
/*  299 */             forest++; 
/*      */         } 
/*      */       } 
/*  302 */       if (forest > this.size * this.size / 6) {
/*      */         
/*  304 */         if (logger.isLoggable(Level.FINEST))
/*      */         {
/*  306 */           logger.finest("Zone at " + this.startX + "," + this.startY + "-" + this.endX + "," + this.endY + " is forest.");
/*      */         }
/*  308 */         this.isForest = true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getSize() {
/*  319 */     return this.size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isOnSurface() {
/*  328 */     return this.isOnSurface;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void addVillage(Village village) {
/*  333 */     if (this.villages == null)
/*  334 */       this.villages = new HashSet<>(); 
/*  335 */     if (!this.villages.contains(village)) {
/*      */       
/*  337 */       this.villages.add(village);
/*  338 */       if (this.tiles != null)
/*      */       {
/*  340 */         for (VolaTile tile : this.tiles.values()) {
/*      */           
/*  342 */           if (village.covers(tile.getTileX(), tile.getTileY())) {
/*  343 */             tile.setVillage(village);
/*      */           }
/*      */         } 
/*      */       }
/*  347 */       addMineDoors(village);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void removeVillage(Village village) {
/*  353 */     if (this.villages == null)
/*  354 */       this.villages = new HashSet<>(); 
/*  355 */     if (this.villages.contains(village)) {
/*      */       
/*  357 */       this.villages.remove(village);
/*  358 */       if (this.tiles != null)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/*  363 */         for (VolaTile tile : this.tiles.values()) {
/*      */           
/*  365 */           if (village.covers(tile.getTileX(), tile.getTileY())) {
/*  366 */             tile.setVillage(null);
/*      */           }
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  374 */       for (int x = this.startX; x < this.endX; x++) {
/*      */         
/*  376 */         for (int y = this.startY; y < this.endY; y++) {
/*      */           
/*  378 */           MineDoorPermission md = MineDoorPermission.getPermission(x, y);
/*  379 */           if (md != null)
/*      */           {
/*  381 */             if (village.covers(x, y)) {
/*  382 */               village.removeMineDoor(md);
/*      */             }
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void updateVillage(Village village, boolean shouldStay) {
/*  391 */     if (this.villages == null)
/*  392 */       this.villages = new HashSet<>(); 
/*  393 */     if (this.villages.contains(village)) {
/*      */       
/*  395 */       if (!shouldStay)
/*  396 */         this.villages.remove(village); 
/*  397 */       if (this.tiles != null) {
/*      */         
/*  399 */         for (VolaTile tile : this.tiles.values()) {
/*      */           
/*  401 */           if (!village.covers(tile.getTileX(), tile.getTileY()) && tile.getVillage() == village) {
/*  402 */             tile.setVillage(null);
/*      */           }
/*      */         } 
/*  405 */         for (VolaTile tile : this.tiles.values()) {
/*      */           
/*  407 */           if (village.covers(tile.getTileX(), tile.getTileY()))
/*  408 */             tile.setVillage(village); 
/*      */         } 
/*      */       } 
/*  411 */       for (int x = this.startX; x < this.endX; x++) {
/*      */         
/*  413 */         for (int y = this.startY; y < this.endY; y++) {
/*      */           
/*  415 */           MineDoorPermission md = MineDoorPermission.getPermission(x, y);
/*  416 */           if (md != null) {
/*      */             
/*  418 */             if (!village.covers(x, y) && md.getVillage() == village)
/*  419 */               village.removeMineDoor(md); 
/*  420 */             if (village.covers(x, y)) {
/*  421 */               village.addMineDoor(md);
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*  426 */       addMineDoors(village);
/*      */     }
/*  428 */     else if (shouldStay) {
/*  429 */       addVillage(village);
/*      */     } 
/*      */   }
/*      */   
/*      */   final boolean containsVillage(int x, int y) {
/*  434 */     if (this.villages != null)
/*      */     {
/*  436 */       for (Village village : this.villages) {
/*      */         
/*  438 */         if (village.covers(x, y))
/*  439 */           return true; 
/*      */       } 
/*      */     }
/*  442 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   final Village getVillage(int x, int y) {
/*  447 */     if (this.villages != null)
/*      */     {
/*  449 */       for (Village village : this.villages) {
/*      */         
/*  451 */         if (village.covers(x, y))
/*  452 */           return village; 
/*      */       } 
/*      */     }
/*  455 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Village[] getVillages() {
/*  464 */     if (this.villages != null) {
/*  465 */       return this.villages.<Village>toArray(new Village[this.villages.size()]);
/*      */     }
/*  467 */     return new Village[0];
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
/*      */   public final void poll(int nums) {
/*  502 */     this.pollTicker = (short)(this.pollTicker + 1);
/*  503 */     boolean lPollStuff = (this.pollTicker >= maxZonesPolled);
/*      */     
/*  505 */     boolean spawnCreatures = (lPollStuff || Creatures.getInstance().getNumberOfCreatures() < Servers.localServer.maxCreatures - 1000);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  514 */     boolean checkAreaEffect = (Server.rand.nextInt(5) == 0);
/*  515 */     long now = System.nanoTime();
/*  516 */     for (VolaTile lElement : this.tiles.values()) {
/*  517 */       lElement.poll(lPollStuff, this.pollTicker, checkAreaEffect);
/*      */     }
/*  519 */     for (VolaTile toDelete : this.deletionQueue)
/*  520 */       this.tiles.remove(Integer.valueOf(toDelete.hashCode())); 
/*  521 */     this.deletionQueue.clear();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  527 */     float lElapsedTime = (float)(System.nanoTime() - now) / 1000000.0F;
/*  528 */     if (logger.isLoggable(Level.FINE) && lElapsedTime > 200.0F) {
/*      */       
/*  530 */       logger.fine("Zone at " + this.startX + ", " + this.startY + " polled " + this.tiles.size() + " tiles. That took " + lElapsedTime + " millis.");
/*      */     
/*      */     }
/*  533 */     else if (!Servers.localServer.testServer && lElapsedTime > 300.0F) {
/*  534 */       logger.log(Level.INFO, "Zone at " + this.startX + ", " + this.startY + " polled " + this.tiles.size() + " tiles. That took " + lElapsedTime + " millis.");
/*      */     } 
/*      */     
/*  537 */     if (isOnSurface())
/*      */     {
/*  539 */       if (Server.getWeather().getFog() > 0.5F && fogSpiders.size() < Zones.worldTileSizeX / 10) {
/*      */ 
/*      */         
/*      */         try {
/*  543 */           TilePos tp = TilePos.fromXY(getStartX() + Server.rand.nextInt(this.size), getStartY() + Server.rand.nextInt(this.size));
/*      */           
/*  545 */           if (Tiles.decodeHeight(Server.surfaceMesh.getTile(tp)) > 0) {
/*      */             
/*  547 */             VolaTile t = Zones.getTileOrNull(tp, true);
/*  548 */             if ((t == null || (t.getStructure() == null && t.getVillage() == null)) && 
/*  549 */               Villages.getVillage(tp, true) == null) {
/*      */               
/*  551 */               CreatureTemplate ctemplate = CreatureTemplateFactory.getInstance().getTemplate(105);
/*  552 */               Creature cret = Creature.doNew(105, (tp.x << 2) + 2.0F, (tp.y << 2) + 2.0F, Server.rand
/*  553 */                   .nextInt(360), 0, "", ctemplate.getSex(), (byte)0);
/*      */               
/*  555 */               fogSpiders.add(Long.valueOf(cret.getWurmId()));
/*  556 */               if (fogSpiders.size() % 100 == 0) {
/*  557 */                 logger.log(Level.INFO, "Now " + fogSpiders.size() + " fog spiders.");
/*      */               }
/*      */             } 
/*      */           } 
/*  561 */         } catch (Exception ex) {
/*      */           
/*  563 */           logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */         }
/*      */       
/*  566 */       } else if (Server.getWeather().getFog() <= 0.0F && fogSpiders.size() > 0) {
/*      */         
/*  568 */         long toDestroy = ((Long)fogSpiders.removeFirst()).longValue();
/*      */         
/*      */         try {
/*  571 */           Creature spider = Creatures.getInstance().getCreature(toDestroy);
/*  572 */           spider.destroy();
/*  573 */           if (fogSpiders.size() % 100 == 0) {
/*  574 */             logger.log(Level.INFO, "Now " + fogSpiders.size() + " fog spiders.");
/*      */           }
/*  576 */         } catch (Exception exception) {}
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  586 */     if (lPollStuff) {
/*      */       
/*  588 */       if (logger.isLoggable(Level.FINEST))
/*      */       {
/*  590 */         logger.finest(this.id + " polling. Ticker=" + this.pollTicker + " max=" + maxZonesPolled);
/*      */       }
/*  592 */       this.pollTicker = 0;
/*      */       
/*  594 */       if (this.tracks != null) {
/*  595 */         this.tracks.decay();
/*      */       }
/*  597 */       if (Features.Feature.NEWDOMAINS.isEnabled()) {
/*      */         
/*  599 */         FaithZone[] lFaithZonesCovered = Zones.getFaithZonesCoveredBy(this.startX, this.startY, this.endX, this.endY, this.isOnSurface);
/*  600 */         for (FaithZone lElement : lFaithZonesCovered) {
/*  601 */           lElement.pollMycelium();
/*      */         }
/*      */       } else {
/*      */         
/*  605 */         FaithZone[] lFaithZonesCovered = Zones.getFaithZonesCoveredBy(this.startX, this.startY, this.endX, this.endY, this.isOnSurface);
/*      */         
/*  607 */         Deity old = null;
/*  608 */         for (FaithZone lElement : lFaithZonesCovered) {
/*      */           
/*  610 */           old = lElement.getCurrentRuler();
/*  611 */           if (lElement.poll())
/*      */           {
/*  613 */             for (int x = lElement.getStartX(); x < lElement.getEndX(); x++) {
/*      */               
/*  615 */               for (int y = lElement.getStartY(); y < lElement.getEndY(); y++) {
/*      */                 
/*  617 */                 VolaTile tile = getTileOrNull(x, y);
/*  618 */                 if (tile != null)
/*      */                 {
/*  620 */                   if (old == null) {
/*  621 */                     tile.broadCast("The domain of " + lElement
/*  622 */                         .getCurrentRuler().getName() + " now has reached this place.");
/*      */ 
/*      */                   
/*      */                   }
/*  626 */                   else if (lElement.getCurrentRuler() != null) {
/*  627 */                     tile.broadCast(lElement
/*  628 */                         .getCurrentRuler().getName() + "'s domain now is the strongest here!");
/*      */                   } else {
/*  630 */                     tile.broadCast(old
/*  631 */                         .getName() + " has had to lose " + old.getHisHerItsString() + " hold over this area!");
/*      */                   } 
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  641 */     if (spawnCreatures && !isHasRift()) {
/*      */       
/*  643 */       boolean lSpawnKingdom = false;
/*      */ 
/*      */       
/*  646 */       if (this.kingdomCreatures <= 0 && isOnSurface())
/*      */       {
/*  648 */         if (!Servers.localServer.PVPSERVER) {
/*  649 */           lSpawnKingdom = (Server.rand.nextInt(50) == 0);
/*      */         } else {
/*  651 */           lSpawnKingdom = (Server.rand.nextInt(20) == 0);
/*      */         }  } 
/*  653 */       boolean spawnSeaHunter = false;
/*      */       
/*  655 */       boolean spawnSeaCreature = false;
/*  656 */       if (this.isOnSurface)
/*      */       {
/*  658 */         if (this.creatures < 20 && Servers.localServer.maxCreatures > 100) {
/*      */           
/*  660 */           if (!lSpawnKingdom && Creatures.getInstance().getNumberOfSeaHunters() < 500)
/*      */           {
/*  662 */             spawnSeaHunter = true;
/*      */           }
/*  664 */           if (!spawnSeaHunter && !lSpawnKingdom)
/*      */           {
/*  666 */             spawnSeaCreature = true;
/*      */           }
/*      */         } 
/*      */       }
/*  670 */       if (logger.isLoggable(Level.FINEST))
/*      */       {
/*  672 */         logger.finest(this.id + " " + ((this.den != null && this.creatures < 20) ? 1 : 0) + " || (" + 
/*  673 */             Creatures.getInstance().getNumberOfCreatures() + "<" + Servers.localServer.maxCreatures + " && (" + ((this.creatures < 5) ? 1 : 0) + " || " + lSpawnKingdom + "))");
/*      */       }
/*      */ 
/*      */       
/*  677 */       boolean doSpawn = (this.den != null || this.creatureSpawn != null);
/*      */ 
/*      */       
/*  680 */       if (doSpawn) {
/*  681 */         doSpawn = (this.creatures < 60 && Creatures.getInstance().getNumberOfTyped() < Servers.localServer.maxTypedCreatures);
/*      */       }
/*      */       
/*  684 */       if (!doSpawn) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  689 */         if (this.creatures < 40 || lSpawnKingdom)
/*      */         {
/*  691 */           doSpawn = true; } 
/*  692 */         if (Creatures.getInstance().getNumberOfCreatures() > Servers.localServer.maxCreatures + Servers.localServer.maxTypedCreatures) {
/*      */ 
/*      */           
/*  695 */           lSpawnKingdom = false;
/*  696 */           doSpawn = false;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  701 */       boolean createDen = false;
/*  702 */       if (spawnPoints < Servers.localServer.maxTypedCreatures / 40)
/*      */       {
/*  704 */         if (this.den == null && this.creatureSpawn == null)
/*      */         {
/*  706 */           if (Server.rand.nextInt(10) == 0)
/*      */           {
/*  708 */             createDen = true;
/*      */           }
/*      */         }
/*      */       }
/*  712 */       if (doSpawn || createDen) {
/*      */         
/*  714 */         int lSeed = Server.rand.nextInt(this.spawnSeed);
/*  715 */         if (lSeed == 0) {
/*      */           
/*  717 */           int sx = Server.rand.nextInt(this.endX - this.startX);
/*  718 */           int sy = Server.rand.nextInt(this.endY - this.startY);
/*  719 */           int tx = this.startX + sx;
/*  720 */           int ty = this.startY + sy;
/*  721 */           for (int xa = -10; xa < 10; xa++) {
/*      */             
/*  723 */             for (int ya = -10; ya < 10; ya++) {
/*      */               
/*  725 */               VolaTile volaTile = Zones.getTileOrNull(tx + xa, ty + ya, this.isOnSurface);
/*  726 */               if (volaTile != null) {
/*      */                 
/*  728 */                 Creature[] crets = volaTile.getCreatures();
/*  729 */                 for (Creature lCret : crets) {
/*      */                   
/*  731 */                   if (lCret.isPlayer())
/*      */                     return; 
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*  737 */           VolaTile t = getTileOrNull(tx, ty);
/*  738 */           if (t != null) {
/*      */             
/*  740 */             if (lSpawnKingdom)
/*      */             {
/*  742 */               if ((t.getWalls()).length == 0 && (t.getFences()).length == 0 && t.getStructure() == null && (t
/*  743 */                 .getCreatures()).length == 0) {
/*  744 */                 spawnCreature(tx, ty, lSpawnKingdom);
/*      */               }
/*      */             }
/*      */           } else {
/*      */             
/*  749 */             Village v = Villages.getVillage(tx, ty, this.isOnSurface);
/*  750 */             if (v == null)
/*      */             {
/*      */               
/*  753 */               if (createDen) {
/*  754 */                 createDen(tx, ty);
/*      */               } else {
/*      */                 
/*  757 */                 spawnCreature(tx, ty, lSpawnKingdom);
/*  758 */                 if (Server.rand.nextInt(300) == 0) {
/*  759 */                   createTreasureChest(tx, ty);
/*      */                 }
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*  765 */       } else if (spawnSeaCreature || spawnSeaHunter) {
/*      */         
/*  767 */         spawnSeaCreature(spawnSeaHunter);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private final int getRandomSeaCreatureId() {
/*  774 */     if (Creatures.getInstance().getNumberOfSeaMonsters() < 4 && Server.rand.nextInt(86400) == 0) {
/*  775 */       return 70;
/*      */     }
/*  777 */     Map<Integer, Integer> slotsForCreature = new HashMap<>();
/*  778 */     slotsForCreature.put(Integer.valueOf(100), 
/*  779 */         Integer.valueOf(Creatures.getInstance().getOpenSpawnSlotsForCreatureType(100)));
/*  780 */     slotsForCreature.put(Integer.valueOf(97), 
/*  781 */         Integer.valueOf(Creatures.getInstance().getOpenSpawnSlotsForCreatureType(97)));
/*  782 */     slotsForCreature.put(Integer.valueOf(99), 
/*  783 */         Integer.valueOf(Creatures.getInstance().getOpenSpawnSlotsForCreatureType(99)));
/*      */     
/*  785 */     Integer[] crets = new Integer[slotsForCreature.keySet().size()];
/*  786 */     slotsForCreature.keySet().toArray((Object[])crets);
/*  787 */     for (int i = crets.length - 1; i >= 0; i--) {
/*      */       
/*  789 */       Integer key = crets[i];
/*  790 */       if (((Integer)slotsForCreature.get(key)).intValue() == 0)
/*  791 */         slotsForCreature.remove(key); 
/*      */     } 
/*  793 */     int validCount = slotsForCreature.keySet().size();
/*  794 */     if (validCount == 0) {
/*  795 */       return 0;
/*      */     }
/*  797 */     int val = Server.rand.nextInt(validCount);
/*  798 */     if (crets.length != slotsForCreature.keySet().size()) {
/*      */       
/*  800 */       crets = new Integer[slotsForCreature.keySet().size()];
/*  801 */       slotsForCreature.keySet().toArray((Object[])crets);
/*      */     } 
/*  803 */     return crets[val].intValue();
/*      */   }
/*      */ 
/*      */   
/*      */   private final void spawnSeaCreature(boolean spawnSeaHunter) {
/*  808 */     int template = spawnSeaHunter ? 71 : getRandomSeaCreatureId();
/*  809 */     if (template == 0) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  814 */     int sx = Server.rand.nextInt(this.endX - this.startX);
/*  815 */     int sy = Server.rand.nextInt(this.endY - this.startY);
/*  816 */     int tx = this.startX + sx;
/*  817 */     int ty = this.startY + sy;
/*  818 */     for (int xa = -10; xa < 10; xa++) {
/*      */       
/*  820 */       for (int ya = -10; ya < 10; ya++) {
/*      */         
/*  822 */         VolaTile t = Zones.getTileOrNull(tx + xa, ty + ya, this.isOnSurface);
/*  823 */         if (t != null) {
/*      */           
/*  825 */           Creature[] crets = t.getCreatures();
/*  826 */           for (Creature lCret : crets) {
/*      */             
/*  828 */             if (lCret.isPlayer())
/*      */               return; 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*  834 */     short[] tsteep = Creature.getTileSteepness(tx, ty, true);
/*      */     
/*  836 */     if (tsteep[0] > -200) {
/*      */       return;
/*      */     }
/*      */     
/*      */     try {
/*  841 */       CreatureTemplate ctemplate = CreatureTemplateFactory.getInstance().getTemplate(template);
/*      */ 
/*      */       
/*  844 */       if (!spawnSeaHunter)
/*      */       {
/*  846 */         if (!maySpawnCreatureTemplate(ctemplate, false, false)) {
/*      */           return;
/*      */         }
/*      */       }
/*  850 */       byte sex = ctemplate.getSex();
/*  851 */       if (sex == 0 && !ctemplate.keepSex)
/*      */       {
/*  853 */         if (Server.rand.nextInt(2) == 0) {
/*  854 */           sex = 1;
/*      */         }
/*      */       }
/*  857 */       Creature.doNew(template, (tx << 2) + 2.0F, (ty << 2) + 2.0F, Server.rand
/*  858 */           .nextInt(360), 0, "", sex);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  864 */     catch (Exception ex) {
/*      */       
/*  866 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final void createTreasureChest(int tx, int ty) {
/*  873 */     if (Features.Feature.TREASURE_CHESTS.isEnabled()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  884 */       if (treasureChests > Zones.worldTileSizeX / 70) {
/*      */         return;
/*      */       }
/*  887 */       if (!this.allWater && (this.isForest == true || Server.rand.nextInt(5) == 0)) {
/*      */         
/*  889 */         int tile = Server.caveMesh.getTile(tx, ty);
/*  890 */         if (this.isOnSurface) {
/*  891 */           tile = Server.surfaceMesh.getTile(tx, ty);
/*      */         }
/*  893 */         if (Tiles.decodeHeight(tile) > 0) {
/*      */           
/*  895 */           boolean ok = !Tiles.isSolidCave(Tiles.decodeType(tile));
/*  896 */           if (this.isOnSurface) {
/*      */             
/*  898 */             ok = false;
/*  899 */             if (!Tiles.isMineDoor(Tiles.decodeType(tile)) && Tiles.decodeType(tile) != Tiles.Tile.TILE_HOLE.id) {
/*  900 */               ok = true;
/*      */             }
/*      */           } 
/*  903 */           if (ok) {
/*      */             
/*  905 */             short[] tsteep = Creature.getTileSteepness(tx, ty, true);
/*      */             
/*  907 */             if (tsteep[1] >= 20) {
/*      */               return;
/*      */             }
/*      */             try {
/*  911 */               Item i = ItemFactory.createItem(995, (50 + Server.rand.nextInt(30)), (tx << 2) + 2.0F, (ty << 2) + 2.0F, Server.rand
/*  912 */                   .nextFloat() * 360.0F, this.isOnSurface, (byte)1, -10L, null);
/*      */               
/*  914 */               i.setAuxData((byte)Server.rand.nextInt(8));
/*  915 */               if (i.getAuxData() > 4) {
/*  916 */                 i.setRarity((byte)2);
/*      */               }
/*  918 */               if (Server.rand.nextBoolean()) {
/*      */ 
/*      */                 
/*  921 */                 Item lock = ItemFactory.createItem(194, 30.0F + Server.rand.nextFloat() * 70.0F, null);
/*  922 */                 i.setLockId(lock.getWurmId());
/*  923 */                 int tilex = i.getTileX();
/*  924 */                 int tiley = i.getTileY();
/*  925 */                 SoundPlayer.playSound("sound.object.lockunlock", tilex, tiley, this.isOnSurface, 1.0F);
/*  926 */                 i.setLocked(true);
/*      */               } 
/*  928 */               i.fillTreasureChest();
/*      */             }
/*  930 */             catch (Exception fe) {
/*      */               
/*  932 */               logger.log(Level.WARNING, "Failed to create treasure chest: " + fe.getMessage(), fe);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private final void createDen(int tx, int ty) {
/*  942 */     byte type = (byte)Math.max(0, Server.rand.nextInt(22) - 10);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  950 */     CreatureTemplate[] ctemps = CreatureTemplateFactory.getInstance().getTemplates();
/*  951 */     CreatureTemplate selected = ctemps[Server.rand.nextInt(ctemps.length)];
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  956 */     if (selected.hasDen() && (selected.isSubterranean() || this.isOnSurface)) {
/*      */       
/*  958 */       int tile = Server.caveMesh.getTile(tx, ty);
/*  959 */       if (this.isOnSurface) {
/*  960 */         tile = Server.surfaceMesh.getTile(tx, ty);
/*      */       }
/*  962 */       if (Tiles.decodeHeight(tile) > 0) {
/*      */         
/*  964 */         boolean ok = !Tiles.isSolidCave(Tiles.decodeType(tile));
/*  965 */         if (this.isOnSurface) {
/*      */           
/*  967 */           ok = false;
/*  968 */           if (!Tiles.isMineDoor(Tiles.decodeType(tile)) && Tiles.decodeType(tile) != Tiles.Tile.TILE_HOLE.id) {
/*  969 */             ok = true;
/*      */           }
/*      */         } 
/*  972 */         if (ok) {
/*      */           
/*  974 */           short[] tsteep = Creature.getTileSteepness(tx, ty, true);
/*      */           
/*  976 */           if (tsteep[1] >= 20)
/*      */             return; 
/*  978 */           if (tsteep[0] > 3000) {
/*      */             return;
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*      */           try {
/*  985 */             Item i = ItemFactory.createItem(521, (50 + Server.rand.nextInt(30)), (tx << 2) + 2.0F, (ty << 2) + 2.0F, Server.rand
/*  986 */                 .nextFloat() * 360.0F, this.isOnSurface, (byte)0, -10L, null);
/*      */             
/*  988 */             i.setAuxData(type);
/*  989 */             i.setData1(selected.getTemplateId());
/*  990 */             i.setName(selected.getDenName());
/*      */           }
/*  992 */           catch (Exception fe) {
/*      */             
/*  994 */             logger.log(Level.WARNING, "Failed to create den: " + fe.getMessage(), fe);
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
/*      */   private final void spawnCreature(int tx, int ty, boolean _spawnKingdom) {
/* 1013 */     int tile = 0;
/* 1014 */     if (this.isOnSurface) {
/*      */       
/* 1016 */       tile = Server.surfaceMesh.getTile(tx, ty);
/*      */       
/* 1018 */       if (Tiles.isMineDoor(Tiles.decodeType(tile)) || Tiles.decodeType(tile) == Tiles.Tile.TILE_HOLE.id)
/*      */         return; 
/* 1020 */       byte kingdom = Zones.getKingdom(tx, ty);
/* 1021 */       byte kingdomTemplate = kingdom;
/* 1022 */       if (kingdom == 0)
/* 1023 */         kingdom = Zones.getKingdom(tx + 50, ty + 50); 
/* 1024 */       if (kingdom == 0)
/* 1025 */         kingdom = Zones.getKingdom(tx + 50, ty - 50); 
/* 1026 */       if (kingdom == 0)
/* 1027 */         kingdom = Zones.getKingdom(tx - 50, ty + 50); 
/* 1028 */       if (kingdom == 0)
/* 1029 */         kingdom = Zones.getKingdom(tx - 50, ty - 50); 
/* 1030 */       if (kingdom == 0)
/* 1031 */         kingdom = Zones.getKingdom(tx + 50, ty); 
/* 1032 */       if (kingdom == 0)
/* 1033 */         kingdom = Zones.getKingdom(tx - 50, ty); 
/* 1034 */       if (kingdom == 0)
/* 1035 */         kingdom = Zones.getKingdom(tx, ty + 50); 
/* 1036 */       if (kingdom == 0)
/* 1037 */         kingdom = Zones.getKingdom(tx, ty - 50); 
/* 1038 */       if (kingdom == 0) {
/* 1039 */         _spawnKingdom = false;
/*      */       } else {
/*      */         
/* 1042 */         Kingdom k = Kingdoms.getKingdom(kingdom);
/* 1043 */         if (k != null)
/* 1044 */           kingdomTemplate = k.getTemplate(); 
/*      */       } 
/* 1046 */       float height = Tiles.decodeHeightAsFloat(tile);
/* 1047 */       if (height > 0.0F) {
/*      */         
/* 1049 */         if (_spawnKingdom) {
/*      */           
/* 1051 */           short[] tsteep = Creature.getTileSteepness(tx, ty, this.isOnSurface);
/* 1052 */           if (tsteep[1] >= 40) {
/*      */             return;
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 1058 */           byte deity = 1;
/* 1059 */           int creatureTemplate = 37;
/* 1060 */           if (kingdomTemplate == 3) {
/*      */             
/* 1062 */             deity = 4;
/* 1063 */             creatureTemplate = 40;
/*      */           }
/* 1065 */           else if (kingdomTemplate == 2) {
/*      */             
/* 1067 */             creatureTemplate = 39;
/* 1068 */             deity = 2;
/*      */           }
/* 1070 */           else if (height < 1.0F) {
/*      */             
/* 1072 */             creatureTemplate = 38;
/* 1073 */             deity = 3;
/*      */           } 
/*      */           
/*      */           try {
/* 1077 */             CreatureTemplate ctemplate = CreatureTemplateFactory.getInstance().getTemplate(creatureTemplate);
/* 1078 */             if (!maySpawnCreatureTemplate(ctemplate, false, _spawnKingdom))
/*      */               return; 
/* 1080 */             Creature cret = Creature.doNew(creatureTemplate, (tx << 2) + 2.0F, (ty << 2) + 2.0F, Server.rand
/* 1081 */                 .nextInt(360), this.isOnSurface ? 0 : -1, "", ctemplate.getSex(), kingdom);
/*      */             
/* 1083 */             cret.setDeity(Deities.getDeity(deity));
/*      */           }
/* 1085 */           catch (Exception ex) {
/*      */             
/* 1087 */             logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */           } 
/*      */           return;
/*      */         } 
/* 1091 */         if (this.den != null) {
/*      */           
/*      */           try
/*      */           {
/* 1095 */             CreatureTemplate ctemplate = CreatureTemplateFactory.getInstance().getTemplate(this.den
/* 1096 */                 .getTemplateId());
/* 1097 */             if (!maySpawnCreatureTemplate(ctemplate, true, false)) {
/*      */               return;
/*      */             }
/*      */             
/* 1101 */             short[] tsteep = Creature.getTileSteepness(tx, ty, this.isOnSurface);
/* 1102 */             if (tsteep[1] >= 40) {
/*      */               return;
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1109 */             byte sex = ctemplate.getSex();
/* 1110 */             if (sex == 0 && !ctemplate.keepSex)
/*      */             {
/* 1112 */               if (Server.rand.nextInt(2) == 0) {
/* 1113 */                 sex = 1;
/*      */               }
/*      */             }
/* 1116 */             Creature.doNew(this.den.getTemplateId(), (tx << 2) + 2.0F, (ty << 2) + 2.0F, Server.rand
/* 1117 */                 .nextInt(360), this.isOnSurface ? 0 : -1, "", sex);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           }
/* 1123 */           catch (Exception ex)
/*      */           {
/* 1125 */             logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1130 */           short[] tsteep = Creature.getTileSteepness(tx, ty, this.isOnSurface);
/* 1131 */           if (tsteep[1] >= 40) {
/*      */             return;
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 1137 */           if (this.creatureSpawn != null && Server.rand.nextInt(10) != 0) {
/*      */             
/* 1139 */             if (this.creatureSpawn.getData1() > 0) {
/*      */               
/*      */               try {
/* 1142 */                 CreatureTemplate ctemplate = CreatureTemplateFactory.getInstance().getTemplate(this.creatureSpawn
/* 1143 */                     .getData1());
/*      */ 
/*      */ 
/*      */                 
/* 1147 */                 if (!maySpawnCreatureTemplate(ctemplate, true, false))
/*      */                   return; 
/* 1149 */                 byte sex = ctemplate.getSex();
/* 1150 */                 if (sex == 0 && !ctemplate.keepSex)
/*      */                 {
/* 1152 */                   if (Server.rand.nextInt(2) == 0)
/* 1153 */                     sex = 1; 
/*      */                 }
/* 1155 */                 byte ctype = this.creatureSpawn.getAuxData();
/* 1156 */                 if (Server.rand.nextInt(40) == 0)
/* 1157 */                   ctype = 99; 
/* 1158 */                 Creature.doNew(ctemplate.getTemplateId(), ctype, (tx << 2) + 2.0F, (ty << 2) + 2.0F, Server.rand
/* 1159 */                     .nextInt(360), this.isOnSurface ? 0 : -1, "", sex);
/*      */ 
/*      */                 
/* 1162 */                 if (this.creatureSpawn.getDamage() < 99.0F) {
/* 1163 */                   this.creatureSpawn.setDamage(this.creatureSpawn.getDamage() + Server.rand.nextFloat() * 0.5F);
/*      */                 } else {
/*      */                   
/* 1166 */                   Items.destroyItem(this.creatureSpawn.getWurmId());
/*      */                 
/*      */                 }
/*      */ 
/*      */               
/*      */               }
/* 1172 */               catch (Exception ex) {
/*      */                 
/* 1174 */                 logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */               } 
/*      */             }
/*      */           } else {
/*      */             
/* 1179 */             byte type = Tiles.decodeType(tile);
/* 1180 */             byte elev = 0;
/* 1181 */             if (type == Tiles.Tile.TILE_LAVA.id) {
/*      */               
/* 1183 */               if ((Tiles.decodeData(tile) & 0xFF) != 255) {
/* 1184 */                 elev = -1;
/*      */               }
/*      */             } else {
/*      */               
/* 1188 */               for (int x = 0; x <= 1; x++) {
/*      */                 
/* 1190 */                 for (int y = 0; y <= 1; y++) {
/*      */                   
/* 1192 */                   int ntile = Server.surfaceMesh.getTile(tx + x, ty + y);
/* 1193 */                   byte ntype = Tiles.decodeType(ntile);
/* 1194 */                   if (Tiles.getTile(ntype).isNormalTree()) {
/*      */                     
/* 1196 */                     type = Tiles.Tile.TILE_TREE.id;
/*      */                     break;
/*      */                   } 
/* 1199 */                   if (ntype == Tiles.Tile.TILE_LAVA.id) {
/*      */                     
/* 1201 */                     if ((Tiles.decodeData(ntile) & 0xFF) != 255)
/* 1202 */                       elev = -1; 
/* 1203 */                     type = Tiles.Tile.TILE_LAVA.id;
/*      */                     break;
/*      */                   } 
/* 1206 */                   if (Tiles.decodeHeight(ntile) < 0) {
/*      */                     
/* 1208 */                     if (ntype == Tiles.Tile.TILE_ROCK.id) {
/*      */                       
/* 1210 */                       elev = 1;
/* 1211 */                       type = Tiles.Tile.TILE_ROCK.id; break;
/*      */                     } 
/* 1213 */                     if (ntype == Tiles.Tile.TILE_SAND.id) {
/*      */                       
/* 1215 */                       elev = 5;
/* 1216 */                       type = Tiles.Tile.TILE_SAND.id;
/*      */                     } 
/*      */                     break;
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */             } 
/* 1223 */             Encounter enc = SpawnTable.getRandomEncounter(type, elev);
/* 1224 */             spawnEncounter(tx, ty, enc);
/*      */           }
/*      */         
/*      */         } 
/*      */       } else {
/*      */         
/* 1230 */         boolean spawnSeaHunter = false;
/* 1231 */         if (Creatures.getInstance().getNumberOfSeaHunters() < 500) {
/*      */           
/* 1233 */           spawnSeaHunter = true;
/* 1234 */           spawnSeaCreature(true);
/*      */         } 
/* 1236 */         if (!spawnSeaHunter)
/*      */         {
/* 1238 */           spawnSeaCreature(false);
/*      */         }
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1244 */       if (this.creatureSpawn != null) {
/*      */         
/* 1246 */         tx = this.creatureSpawn.getTileX();
/* 1247 */         ty = this.creatureSpawn.getTileY();
/*      */       } 
/* 1249 */       tile = Server.caveMesh.getTile(tx, ty);
/* 1250 */       byte type = Tiles.decodeType(tile);
/* 1251 */       if (!Tiles.isSolidCave(type))
/*      */       {
/* 1253 */         if (this.creatureSpawn != null) {
/*      */           
/*      */           try
/*      */           {
/* 1257 */             CreatureTemplate ctemplate = CreatureTemplateFactory.getInstance().getTemplate(this.creatureSpawn
/* 1258 */                 .getData1());
/*      */ 
/*      */ 
/*      */             
/* 1262 */             if (!maySpawnCreatureTemplate(ctemplate, true, false))
/*      */               return; 
/* 1264 */             byte sex = ctemplate.getSex();
/* 1265 */             if (sex == 0 && !ctemplate.keepSex)
/*      */             {
/* 1267 */               if (Server.rand.nextInt(2) == 0)
/* 1268 */                 sex = 1; 
/*      */             }
/* 1270 */             byte ctype = this.creatureSpawn.getAuxData();
/* 1271 */             if (Server.rand.nextInt(40) == 0) {
/* 1272 */               ctype = 99;
/*      */             }
/* 1274 */             Creature cret = Creature.doNew(ctemplate.getTemplateId(), ctype, (tx << 2) + 2.0F, (ty << 2) + 2.0F, Server.rand
/* 1275 */                 .nextInt(360), this.isOnSurface ? 0 : -1, "", sex);
/*      */             
/* 1277 */             if (this.creatureSpawn.getDamage() < 99.0F) {
/* 1278 */               this.creatureSpawn.setDamage(this.creatureSpawn.getDamage() + Server.rand.nextFloat());
/*      */             } else {
/* 1280 */               Items.destroyItem(this.creatureSpawn.getWurmId());
/*      */             
/*      */             }
/*      */ 
/*      */           
/*      */           }
/* 1286 */           catch (Exception ex)
/*      */           {
/* 1288 */             logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */           }
/*      */         
/* 1291 */         } else if (Tiles.decodeHeight(tile) > 0) {
/*      */           
/* 1293 */           Encounter enc = SpawnTable.getRandomEncounter(Tiles.Tile.TILE_CAVE.id, (byte)-1);
/* 1294 */           spawnEncounter(tx, ty, enc);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean maySpawnCreatureTemplate(CreatureTemplate ctemplate, boolean typed, boolean kingdomCreature) {
/* 1303 */     return maySpawnCreatureTemplate(ctemplate, typed, false, kingdomCreature);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean maySpawnCreatureTemplate(CreatureTemplate ctemplate, boolean typed, boolean breeding, boolean kingdomCreature) {
/* 1311 */     if (ctemplate.isAggHuman() || ctemplate.isMonster())
/*      */     {
/* 1313 */       if (Creatures.getInstance().getNumberOfAgg() / Creatures.getInstance().getNumberOfCreatures() > Servers.localServer.percentAggCreatures / 100.0F)
/*      */       {
/* 1315 */         return false;
/*      */       }
/*      */     }
/*      */ 
/*      */     
/* 1320 */     if (typed)
/*      */     {
/* 1322 */       if (!((Creatures.getInstance().getNumberOfTyped() < Servers.localServer.maxTypedCreatures) ? 1 : 0))
/* 1323 */         return false; 
/*      */     }
/* 1325 */     if (kingdomCreature)
/*      */     {
/* 1327 */       return (Creatures.getInstance().getNumberOfKingdomCreatures() < Servers.localServer.maxCreatures / (Servers.localServer.PVPSERVER ? 50 : 200));
/*      */     }
/*      */     
/* 1330 */     if (Creatures.getInstance().getNumberOfNice() > Servers.localServer.maxCreatures / 2 - (breeding ? breedingLimit : 0))
/*      */     {
/* 1332 */       return false;
/*      */     }
/* 1334 */     int nums = Creatures.getInstance().getCreatureByType(ctemplate.getTemplateId());
/* 1335 */     if (nums > Servers.localServer.maxCreatures * ctemplate.getMaxPercentOfCreatures() || (ctemplate
/* 1336 */       .usesMaxPopulation() && nums >= ctemplate.getMaxPopulationOfCreatures()))
/* 1337 */       return false; 
/* 1338 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean hasSpring(int tilex, int tiley) {
/* 1343 */     r.setSeed((tilex + tiley * Zones.worldTileSizeY) * 7919L);
/* 1344 */     return (r.nextInt(128) == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   private void spawnEncounter(int tx, int ty, Encounter enc) {
/* 1349 */     if (enc != null) {
/*      */       
/* 1351 */       Map<Integer, Integer> encTypes = enc.getTypes();
/* 1352 */       if (encTypes != null)
/*      */       {
/* 1354 */         for (Integer templateId : encTypes.keySet()) {
/*      */           
/* 1356 */           boolean create = true;
/*      */ 
/*      */ 
/*      */           
/* 1360 */           Integer nums = encTypes.get(templateId);
/* 1361 */           int n = nums.intValue();
/* 1362 */           if (n > 1) {
/* 1363 */             n = Math.max(1, Server.rand.nextInt(n));
/*      */           }
/*      */           try {
/* 1366 */             CreatureTemplate ctemplate = CreatureTemplateFactory.getInstance().getTemplate(templateId
/* 1367 */                 .intValue());
/*      */             
/* 1369 */             if (!ctemplate.nonNewbie || !Constants.isNewbieFriendly)
/*      */             {
/* 1371 */               if (!maySpawnCreatureTemplate(ctemplate, false, false))
/*      */                 return; 
/* 1373 */               for (int x = 0; x < n; x++)
/*      */               {
/* 1375 */                 byte rType, sex = ctemplate.getSex();
/* 1376 */                 if (sex == 0 && !ctemplate.keepSex)
/*      */                 {
/* 1378 */                   if (Server.rand.nextInt(2) == 0) {
/* 1379 */                     sex = 1;
/*      */                   }
/*      */                 }
/*      */                 
/* 1383 */                 int tChance = Server.rand.nextInt(5);
/* 1384 */                 if (ctemplate.hasDen()) {
/*      */                   
/* 1386 */                   if (tChance == 1) {
/*      */ 
/*      */                     
/* 1389 */                     int cChance = Server.rand.nextInt(20);
/* 1390 */                     if (cChance == 1)
/*      */                     {
/* 1392 */                       rType = 99;
/*      */                     }
/*      */                     else
/*      */                     {
/* 1396 */                       rType = (byte)Server.rand.nextInt(11);
/*      */                     }
/*      */                   
/*      */                   } else {
/*      */                     
/* 1401 */                     rType = 0;
/*      */                   }
/*      */                 
/*      */                 } else {
/*      */                   
/* 1406 */                   rType = 0;
/*      */                 } 
/* 1408 */                 Creature cret = Creature.doNew(templateId.intValue(), rType, (tx << 2) + 1.0F + Server.rand
/* 1409 */                     .nextFloat() * 2.0F, (ty << 2) + 1.0F + Server.rand.nextFloat() * 2.0F, Server.rand
/* 1410 */                     .nextInt(360), this.isOnSurface ? 0 : -1, "", sex);
/*      */                 
/* 1412 */                 if (Servers.isThisATestServer())
/*      */                 {
/* 1414 */                   if (ctemplate.hasDen())
/*      */                   {
/* 1416 */                     Players.getInstance().sendGmMessage(null, "System", "Debug: " + cret
/* 1417 */                         .getNameWithGenus() + " was spawned @ " + tx + ", " + ty + ", type chance roll was " + tChance + ".", false);
/*      */                   }
/*      */                 }
/*      */               }
/*      */             
/*      */             }
/*      */           
/* 1424 */           } catch (Exception ex) {
/*      */             
/* 1426 */             logger.log(Level.WARNING, ex.getMessage(), ex);
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
/*      */   private VolaTile[] getTiles() {
/* 1440 */     if (this.tiles != null)
/*      */     {
/* 1442 */       return (VolaTile[])this.tiles.values().toArray((Object[])new VolaTile[this.tiles.size()]);
/*      */     }
/* 1444 */     return emptyTiles;
/*      */   }
/*      */ 
/*      */   
/*      */   private VirtualZone[] getWatchers() {
/* 1449 */     if (this.zoneWatchers != null)
/*      */     {
/* 1451 */       return this.zoneWatchers.<VirtualZone>toArray(new VirtualZone[this.zoneWatchers.size()]);
/*      */     }
/* 1453 */     return emptyWatchers;
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Creature> getPlayerWatchers() {
/* 1458 */     List<Creature> playerWatchers = new ArrayList<>();
/* 1459 */     if (this.zoneWatchers != null)
/*      */     {
/* 1461 */       for (VirtualZone vz : this.zoneWatchers) {
/*      */         
/* 1463 */         Creature cWatcher = vz.getWatcher();
/* 1464 */         if (cWatcher.isPlayer() && !playerWatchers.contains(cWatcher)) {
/* 1465 */           playerWatchers.add(vz.getWatcher());
/*      */         }
/*      */       } 
/*      */     }
/* 1469 */     return playerWatchers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getId() {
/* 1478 */     return this.id;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getStartX() {
/* 1487 */     return this.startX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getStartY() {
/* 1496 */     return this.startY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getEndX() {
/* 1505 */     return this.endX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getEndY() {
/* 1514 */     return this.endY;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean covers(int x, int y) {
/* 1519 */     return (x >= this.startX && x <= this.endX && y >= this.startY && y <= this.endY);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isLoaded() {
/* 1528 */     return this.isLoaded;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final VolaTile getOrCreateTile(@Nonnull TilePos tilePos) {
/* 1539 */     return getOrCreateTile(tilePos.x, tilePos.y);
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
/*      */   public final VolaTile getOrCreateTile(int x, int y) {
/* 1556 */     if (!covers(x, y)) {
/*      */       
/* 1558 */       logger.log(Level.WARNING, "Zone " + this.id + " at " + this.startX + ", " + this.endX + "-" + this.startY + "," + this.endY + " doesn't cover " + x + "," + y, new Exception());
/*      */ 
/*      */       
/*      */       try {
/* 1562 */         Zone z = Zones.getZone(x, y, isOnSurface());
/* 1563 */         logger.log(Level.INFO, "Adding to " + z.getId());
/* 1564 */         return z.getOrCreateTile(x, y);
/*      */       }
/* 1566 */       catch (NoSuchZoneException nsz) {
/*      */         
/* 1568 */         logger.log(Level.WARNING, "No such zone: " + x + ", " + y + " at ", (Throwable)nsz);
/*      */       } 
/*      */     } 
/* 1571 */     VolaTile t = this.tiles.get(Integer.valueOf(VolaTile.generateHashCode(x, y, this.isOnSurface)));
/* 1572 */     if (t != null)
/* 1573 */       return t; 
/* 1574 */     Set<VirtualZone> tileWatchers = new HashSet<>();
/* 1575 */     if (this.zoneWatchers != null)
/*      */     {
/* 1577 */       for (VirtualZone watcher : this.zoneWatchers) {
/*      */         
/* 1579 */         if (watcher.covers(x, y))
/* 1580 */           tileWatchers.add(watcher); 
/*      */       } 
/*      */     }
/* 1583 */     VolaTile toReturn = new VolaTile(x, y, this.isOnSurface, tileWatchers, this);
/* 1584 */     if (this.villages != null)
/*      */     {
/* 1586 */       for (Village village : this.villages) {
/*      */         
/* 1588 */         if (village.covers(x, y)) {
/*      */           
/* 1590 */           toReturn.setVillage(village);
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     }
/* 1595 */     this.tiles.put(Integer.valueOf(VolaTile.generateHashCode(x, y, this.isOnSurface)), toReturn);
/* 1596 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   final void removeTile(VolaTile tile) {
/* 1601 */     this.deletionQueue.add(tile);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1615 */     tile.setInactive(true);
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
/*      */   @Deprecated
/*      */   public final VolaTile getTile(int x, int y) throws NoSuchTileException {
/* 1631 */     VolaTile t = this.tiles.get(Integer.valueOf(VolaTile.generateHashCode(x, y, this.isOnSurface)));
/* 1632 */     if (t != null) {
/* 1633 */       return t;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1641 */     throw new NoSuchTileException(x + ", " + y);
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
/*      */   public final VolaTile getTileOrNull(@Nonnull TilePos tilePos) {
/* 1653 */     return getTileOrNull(tilePos.x, tilePos.y);
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
/*      */   public final VolaTile getTileOrNull(int x, int y) {
/* 1668 */     VolaTile t = this.tiles.get(Integer.valueOf(VolaTile.generateHashCode(x, y, this.isOnSurface)));
/* 1669 */     return t;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void addEffect(Effect effect, boolean temp) {
/* 1679 */     int x = effect.getTileX();
/* 1680 */     int y = effect.getTileY();
/* 1681 */     VolaTile tile = getOrCreateTile(x, y);
/* 1682 */     tile.addEffect(effect, temp);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void removeEffect(Effect effect) {
/* 1687 */     int x = effect.getTileX();
/* 1688 */     int y = effect.getTileY();
/* 1689 */     VolaTile tile = getTileOrNull(x, y);
/* 1690 */     if (tile != null) {
/*      */       
/* 1692 */       if (!tile.removeEffect(effect))
/*      */       {
/*      */ 
/*      */ 
/*      */         
/* 1697 */         for (VolaTile t : this.tiles.values()) {
/*      */           
/* 1699 */           if (t.removeEffect(effect)) {
/*      */             
/* 1701 */             logger.log(Level.WARNING, "Aimed to delete effect at " + x + "," + y + " but found it at " + t
/* 1702 */                 .getTileX() + ", " + t
/* 1703 */                 .getTileY() + " instead.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             return;
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } else {
/* 1716 */       logger.log(Level.WARNING, "Tile at " + x + "," + y + " failed to remove effect: No Tile Found");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int addCreature(long creatureId) throws NoSuchCreatureException, NoSuchPlayerException {
/* 1722 */     Creature creature = null;
/* 1723 */     creature = Server.getInstance().getCreature(creatureId);
/* 1724 */     this.creatures++;
/* 1725 */     if (creature.isDefendKingdom() || creature.isAggWhitie()) {
/* 1726 */       this.kingdomCreatures++;
/*      */     }
/* 1728 */     if (creature.getTemplate().getTemplateId() == 105 && 
/* 1729 */       !fogSpiders.contains(Long.valueOf(creatureId))) {
/* 1730 */       fogSpiders.add(Long.valueOf(creatureId));
/*      */     }
/* 1732 */     int x = creature.getTileX();
/* 1733 */     int y = creature.getTileY();
/* 1734 */     VolaTile tile = getOrCreateTile(x, y);
/* 1735 */     return tile.addCreature(creature, 0.0F);
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
/*      */   final void write(BufferedWriter writer) throws IOException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void removeCreature(Creature creature, boolean delete, boolean removeAsTarget) {
/* 1782 */     this.creatures--;
/* 1783 */     if (creature.isDefendKingdom() || creature.isAggWhitie())
/* 1784 */       this.kingdomCreatures--; 
/* 1785 */     if (delete && this.zoneWatchers != null) {
/*      */       
/* 1787 */       VirtualZone[] watchers = getWatchers();
/* 1788 */       for (VirtualZone lWatcher : watchers) {
/*      */ 
/*      */         
/*      */         try {
/* 1792 */           lWatcher.deleteCreature(creature, removeAsTarget);
/*      */         }
/* 1794 */         catch (NoSuchPlayerException nsp) {
/*      */           
/* 1796 */           logger.log(Level.WARNING, creature.getName() + ": " + nsp.getMessage(), (Throwable)nsp);
/*      */         }
/* 1798 */         catch (NoSuchCreatureException nsc) {
/*      */           
/* 1800 */           logger.log(Level.WARNING, creature.getName() + ": " + nsc.getMessage(), (Throwable)nsc);
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
/*      */   public final void deleteCreature(Creature creature, boolean deleteFromTile) throws NoSuchCreatureException, NoSuchPlayerException {
/* 1814 */     this.creatures--;
/* 1815 */     if (creature.isDefendKingdom() || creature.isAggWhitie())
/* 1816 */       this.kingdomCreatures--; 
/* 1817 */     if (deleteFromTile) {
/*      */       
/* 1819 */       int x = creature.getTileX();
/* 1820 */       int y = creature.getTileY();
/*      */       
/* 1822 */       VolaTile tile = getTileOrNull(x, y);
/* 1823 */       if (tile != null) {
/*      */         
/* 1825 */         if (!tile.removeCreature(creature))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1837 */           boolean ok = false;
/* 1838 */           if (creature.getCurrentTile() != null)
/*      */           {
/* 1840 */             if (creature.getCurrentTile().removeCreature(creature))
/*      */             {
/* 1842 */               ok = true;
/*      */             }
/*      */           }
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 1849 */         boolean ok = false;
/* 1850 */         if (creature.getCurrentTile() != null)
/*      */         {
/* 1852 */           if (creature.getCurrentTile().removeCreature(creature))
/* 1853 */             ok = true; 
/*      */         }
/* 1855 */         logger.log(Level.WARNING, this.id + " tile " + x + "," + y + " where " + creature.getName() + " should be didn't contain it. The creature.currentTile removed it=" + ok, new Exception());
/*      */       } 
/*      */     } 
/*      */     
/* 1859 */     if (this.zoneWatchers != null) {
/*      */       
/* 1861 */       for (VirtualZone zone : this.zoneWatchers)
/*      */       {
/* 1863 */         zone.deleteCreature(creature, true);
/*      */       }
/*      */       
/* 1866 */       if (this.isOnSurface) {
/*      */ 
/*      */         
/* 1869 */         if (creature.getVisionArea() != null)
/*      */         {
/* 1871 */           VirtualZone vz = creature.getVisionArea().getSurface();
/* 1872 */           this.zoneWatchers.remove(vz);
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 1877 */       else if (creature.getVisionArea() != null) {
/*      */         
/* 1879 */         VirtualZone vz = creature.getVisionArea().getUnderGround();
/* 1880 */         this.zoneWatchers.remove(vz);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void addItem(Item item) {
/* 1889 */     addItem(item, false, false, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void addItem(Item item, boolean moving, boolean newLayer, boolean starting) {
/* 1894 */     int x = item.getTileX();
/* 1895 */     int y = item.getTileY();
/* 1896 */     if (!covers(x, y)) {
/*      */       
/* 1898 */       logger.log(Level.WARNING, this.id + " zone at " + this.startX + ", " + this.endX + "-" + this.startY + "," + this.endY + " surf=" + this.isOnSurface + " doesn't cover " + x + " (" + item
/* 1899 */           .getPosX() + ") ," + y + " (" + item.getPosY() + "), a " + item
/* 1900 */           .getName() + " id " + item.getWurmId(), new Exception());
/*      */       
/*      */       try {
/* 1903 */         Zone z = Zones.getZone(x, y, isOnSurface());
/* 1904 */         logger.log(Level.INFO, "Adding to " + z.getId());
/* 1905 */         z.addItem(item, moving, newLayer, starting);
/*      */       }
/* 1907 */       catch (NoSuchZoneException nsz) {
/*      */         
/* 1909 */         logger.log(Level.WARNING, "No such zone: " + x + ", " + y + " at ", (Throwable)nsz);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1914 */       if (item.isKingdomMarker() || item.getTemplateId() == 996) {
/*      */         
/* 1916 */         if (!isOnSurface()) {
/*      */           
/* 1918 */           Kingdoms.destroyTower(item);
/* 1919 */           Items.decay(item.getWurmId(), item.getDbStrings());
/*      */           
/*      */           return;
/*      */         } 
/* 1923 */         if (item.isGuardTower() && !moving) {
/* 1924 */           Zones.addGuardTower(item);
/*      */         
/*      */         }
/*      */       }
/* 1928 */       else if (item.getTemplateId() == 521) {
/*      */         
/* 1930 */         this.creatureSpawn = item;
/* 1931 */         spawnPoints++;
/*      */       }
/* 1933 */       else if (item.getTemplateId() == 995) {
/*      */         
/* 1935 */         this.treasureChest = item;
/* 1936 */         treasureChests++;
/*      */       } 
/*      */ 
/*      */       
/* 1940 */       VolaTile tile = getOrCreateTile(x, y);
/* 1941 */       tile.addItem(item, moving, starting);
/* 1942 */       if (newLayer) {
/* 1943 */         tile.newLayer(item);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void removeItem(Item item) {
/* 1949 */     removeItem(item, false, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateModelName(Item item) {
/* 1954 */     int x = item.getTileX();
/* 1955 */     int y = item.getTileY();
/* 1956 */     VolaTile tile = getTileOrNull(x, y);
/* 1957 */     if (tile != null) {
/*      */       
/* 1959 */       for (VirtualZone vz : getWatchers()) {
/*      */         
/*      */         try
/*      */         {
/* 1963 */           vz.getWatcher().getCommunicator().sendChangeModelName(item);
/*      */         }
/* 1965 */         catch (Exception e)
/*      */         {
/* 1967 */           logger.log(Level.WARNING, e.getMessage(), e);
/*      */         }
/*      */       
/*      */       } 
/*      */     } else {
/*      */       
/* 1973 */       logger.log(Level.WARNING, "Failed to remove " + item.getName() + " at " + x + ", " + y + ". Duplicate methods calling?");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void updatePile(Item pile) {
/* 1980 */     int x = pile.getTileX();
/* 1981 */     int y = pile.getTileY();
/*      */     
/* 1983 */     VolaTile tile = getTileOrNull(x, y);
/* 1984 */     if (tile != null) {
/* 1985 */       tile.updatePile(pile);
/*      */     } else {
/*      */       
/* 1988 */       logger.log(Level.WARNING, "Failed to update pile at x: " + x + " y: " + y);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeItem(Item item, boolean moving, boolean newLayer) {
/* 1994 */     item.setZoneId(-10, this.isOnSurface);
/* 1995 */     int x = item.getTileX();
/* 1996 */     int y = item.getTileY();
/* 1997 */     VolaTile tile = getTileOrNull(x, y);
/* 1998 */     if (tile != null) {
/*      */       
/* 2000 */       tile.removeItem(item, moving);
/* 2001 */       if (newLayer)
/* 2002 */         tile.newLayer(item); 
/* 2003 */       if ((item.isUnfinished() || item.isUseOnGroundOnly()) && 
/* 2004 */         item.getWatcherSet() != null) {
/* 2005 */         for (Creature cret : item.getWatcherSet()) {
/* 2006 */           cret.getCommunicator().sendRemoveFromCreationWindow(item.getWurmId());
/*      */         }
/*      */       }
/*      */     } else {
/* 2010 */       logger.log(Level.WARNING, "Failed to remove " + item.getName() + " at " + x + ", " + y + ". Duplicate methods calling?");
/*      */     } 
/*      */     
/* 2013 */     if (item.getTemplateId() == 521) {
/*      */       
/* 2015 */       this.creatureSpawn = null;
/* 2016 */       spawnPoints--;
/*      */     }
/* 2018 */     else if (item.getTemplateId() == 995) {
/*      */       
/* 2020 */       this.treasureChest = null;
/* 2021 */       treasureChests--;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void removeStructure(Structure structure) {
/* 2027 */     if (this.structures != null) {
/* 2028 */       this.structures.remove(structure);
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
/*      */   public final void addStructure(Structure structure) {
/* 2045 */     if (this.structures == null)
/* 2046 */       this.structures = new HashSet<>(); 
/* 2047 */     if (!this.structures.contains(structure)) {
/* 2048 */       this.structures.add(structure);
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
/*      */   public final void addFence(Fence fence) {
/* 2065 */     int tilex = fence.getTileX();
/* 2066 */     int tiley = fence.getTileY();
/* 2067 */     VolaTile tile = getOrCreateTile(tilex, tiley);
/* 2068 */     tile.addFence(fence);
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
/*      */   public final void removeFence(Fence fence) {
/* 2083 */     int tilex = fence.getTileX();
/* 2084 */     int tiley = fence.getTileY();
/* 2085 */     VolaTile tile = getOrCreateTile(tilex, tiley);
/* 2086 */     tile.removeFence(fence);
/*      */ 
/*      */     
/* 2089 */     if (fence.isDoor() && fence.isFinished()) {
/*      */       
/* 2091 */       FenceGate gate = FenceGate.getFenceGate(fence.getId());
/* 2092 */       if (gate != null) {
/*      */ 
/*      */ 
/*      */         
/* 2096 */         gate.removeFromVillage();
/* 2097 */         gate.removeFromTiles();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2109 */         gate.delete();
/*      */       } else {
/*      */         
/* 2112 */         logger.log(Level.WARNING, "fencegate did not exist for fence " + this.id, new Exception());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Structure[] getStructures() {
/* 2122 */     if (this.structures != null)
/* 2123 */       return this.structures.<Structure>toArray(new Structure[this.structures.size()]); 
/* 2124 */     return emptyStructures;
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
/*      */   final void linkTo(VirtualZone virtualZone, int aStartX, int aStartY, int aEndX, int aEndY) {
/* 2138 */     long now = System.nanoTime();
/* 2139 */     VolaTile[] lTileArray = getTiles();
/* 2140 */     for (VolaTile lElement : lTileArray) {
/*      */ 
/*      */ 
/*      */       
/* 2144 */       int centerX = virtualZone.getCenterX();
/* 2145 */       int centerY = virtualZone.getCenterY();
/*      */       
/* 2147 */       if (lElement.tilex < aStartX || lElement.tilex > aEndX || lElement.tiley < aStartY || lElement.tiley > aEndY) {
/* 2148 */         lElement.removeWatcher(virtualZone);
/* 2149 */       } else if (lElement.tilex == aStartX || lElement.tilex == aEndX) {
/*      */         
/* 2151 */         if (lElement.tiley >= aStartY || lElement.tiley <= aEndY) {
/* 2152 */           lElement.addWatcher(virtualZone);
/*      */         }
/* 2154 */       } else if (lElement.tiley == aStartY || lElement.tiley == aEndY) {
/*      */         
/* 2156 */         if (lElement.tilex >= aStartX || lElement.tilex <= aEndX) {
/* 2157 */           lElement.addWatcher(virtualZone);
/*      */         
/*      */         }
/*      */       }
/* 2161 */       else if (virtualZone.getWatcher().isPlayer()) {
/*      */         
/* 2163 */         lElement.linkTo(virtualZone, false);
/*      */       }
/*      */       else {
/*      */         
/* 2167 */         int distancex = Math.abs(lElement.tilex - centerX);
/* 2168 */         int distancey = Math.abs(lElement.tiley - centerY);
/* 2169 */         int distance = Math.max(distancex, distancey);
/* 2170 */         if (distance < Math.min(virtualZone.getSize() / 2, 7)) {
/* 2171 */           lElement.linkTo(virtualZone, false);
/* 2172 */         } else if (distance == 10 && this.size >= 10) {
/* 2173 */           lElement.linkTo(virtualZone, false);
/* 2174 */         } else if (distance == 20 && this.size >= 20) {
/* 2175 */           lElement.linkTo(virtualZone, false);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2180 */     float lElapsedTime = (float)(System.nanoTime() - now) / 1000000.0F;
/* 2181 */     if (lElapsedTime > (float)LOG_ELAPSED_TIME_THRESHOLD)
/*      */     {
/* 2183 */       logger.info("linkTo in zone: " + virtualZone + ", which took " + lElapsedTime + " millis.");
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
/*      */   final void addWatcher(int zoneNum) throws NoSuchZoneException {
/* 2195 */     VirtualZone zone = Zones.getVirtualZone(zoneNum);
/* 2196 */     if (this.zoneWatchers == null)
/* 2197 */       this.zoneWatchers = new HashSet<>(); 
/* 2198 */     VirtualZone[] warr = getWatchers();
/* 2199 */     for (VirtualZone lElement : warr) {
/*      */       
/* 2201 */       if (lElement.getWatcher() == null || lElement.getWatcher().getWurmId() == zone.getWatcher().getWurmId()) {
/*      */         
/* 2203 */         if (lElement.getWatcher() != null) {
/* 2204 */           logger.log(Level.WARNING, "Old virtualzone being removed:" + lElement.getWatcher().getName(), new Exception());
/*      */         } else {
/*      */           
/* 2207 */           logger.log(Level.WARNING, "Old virtualzone being removed: watcher=null", new Exception());
/* 2208 */         }  removeWatcher(lElement);
/*      */       } 
/*      */     } 
/* 2211 */     if (!this.zoneWatchers.contains(zone)) {
/*      */       
/* 2213 */       this.zoneWatchers.add(zone);
/* 2214 */       VolaTile[] lTileArray = getTiles();
/* 2215 */       for (VolaTile lElement : lTileArray) {
/*      */         
/* 2217 */         if (zone.covers(lElement.getTileX(), lElement.getTileY())) {
/* 2218 */           lElement.addWatcher(zone);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void removeWatcher(VirtualZone zone) throws NoSuchZoneException {
/* 2228 */     for (VolaTile tile : this.tiles.values())
/*      */     {
/* 2230 */       tile.removeWatcher(zone);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2237 */     if (this.zoneWatchers != null)
/*      */     {
/* 2239 */       this.zoneWatchers.remove(zone);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   final Track[] getTracksFor(int tilex, int tiley) {
/* 2245 */     if (this.tracks == null)
/* 2246 */       return new Track[0]; 
/* 2247 */     return this.tracks.getTracksFor(tilex, tiley);
/*      */   }
/*      */ 
/*      */   
/*      */   public final Track[] getTracksFor(int tilex, int tiley, int dist) {
/* 2252 */     if (this.tracks == null)
/* 2253 */       return new Track[0]; 
/* 2254 */     return this.tracks.getTracksFor(tilex, tiley, dist);
/*      */   }
/*      */ 
/*      */   
/*      */   private void addTrack(Track track) {
/* 2259 */     if (this.tracks == null)
/* 2260 */       this.tracks = new Tracks(); 
/* 2261 */     this.tracks.addTrack(track);
/*      */   }
/*      */ 
/*      */   
/*      */   final void createTrack(Creature creature, int tileX, int tileY, int diffTileX, int diffTileY) {
/* 2266 */     if (!creature.isGhost() && creature.getPower() <= 0 && creature.getFloorLevel() <= 0 && 
/* 2267 */       !creature.isFish()) {
/*      */       
/* 2269 */       long now = System.nanoTime();
/*      */       
/* 2271 */       Track toAdd = null;
/* 2272 */       if (tileX + diffTileX >= 0 && tileX + diffTileX < 1 << Constants.meshSize && tileY + diffTileY >= 0 && tileX + diffTileX < 1 << Constants.meshSize) {
/*      */ 
/*      */         
/* 2275 */         int tilenum = Server.surfaceMesh.getTile(tileX + diffTileX, tileY + diffTileY);
/* 2276 */         if (!this.isOnSurface) {
/*      */           
/* 2278 */           tilenum = Server.caveMesh.getTile(tileX + diffTileX, tileY + diffTileY);
/*      */         } else {
/*      */           
/* 2281 */           Zones.walkedTiles[tileX + diffTileX][tileY + diffTileY] = true;
/* 2282 */         }  if (diffTileX < 0) {
/*      */           
/* 2284 */           if (diffTileY == 0) {
/*      */ 
/*      */             
/* 2287 */             toAdd = new Track(creature.getWurmId(), creature.getName(), tileX - diffTileX, tileY - diffTileY, tilenum, System.currentTimeMillis(), (byte)6);
/* 2288 */           } else if (diffTileY < 0) {
/*      */ 
/*      */             
/* 2291 */             toAdd = new Track(creature.getWurmId(), creature.getName(), tileX - diffTileX, tileY - diffTileY, tilenum, System.currentTimeMillis(), (byte)7);
/* 2292 */           } else if (diffTileY > 0) {
/*      */ 
/*      */             
/* 2295 */             toAdd = new Track(creature.getWurmId(), creature.getName(), tileX - diffTileX, tileY - diffTileY, tilenum, System.currentTimeMillis(), (byte)5);
/*      */           } 
/* 2297 */         } else if (diffTileX > 0) {
/*      */           
/* 2299 */           if (diffTileY == 0) {
/*      */ 
/*      */             
/* 2302 */             toAdd = new Track(creature.getWurmId(), creature.getName(), tileX - diffTileX, tileY - diffTileY, tilenum, System.currentTimeMillis(), (byte)2);
/* 2303 */           } else if (diffTileY < 0) {
/*      */ 
/*      */             
/* 2306 */             toAdd = new Track(creature.getWurmId(), creature.getName(), tileX - diffTileX, tileY - diffTileY, tilenum, System.currentTimeMillis(), (byte)1);
/* 2307 */           } else if (diffTileY > 0) {
/*      */ 
/*      */             
/* 2310 */             toAdd = new Track(creature.getWurmId(), creature.getName(), tileX - diffTileX, tileY - diffTileY, tilenum, System.currentTimeMillis(), (byte)3);
/*      */           } 
/* 2312 */         } else if (diffTileY > 0) {
/*      */           
/* 2314 */           if (diffTileX == 0)
/*      */           {
/*      */             
/* 2317 */             toAdd = new Track(creature.getWurmId(), creature.getName(), tileX - diffTileX, tileY - diffTileY, tilenum, System.currentTimeMillis(), (byte)4);
/*      */           }
/* 2319 */         } else if (diffTileY < 0) {
/*      */           
/* 2321 */           if (diffTileX == 0)
/*      */           {
/*      */             
/* 2324 */             toAdd = new Track(creature.getWurmId(), creature.getName(), tileX - diffTileX, tileY - diffTileY, tilenum, System.currentTimeMillis(), (byte)0); } 
/*      */         } 
/* 2326 */         if (toAdd != null)
/* 2327 */           addTrack(toAdd); 
/* 2328 */         if (Server.rand.nextInt(100) == 0)
/*      */         {
/* 2330 */           if ((this.tracks.getTracksFor(tileX - diffTileX, tileY - diffTileY)).length > 20)
/*      */           {
/* 2332 */             if (creature.isOnSurface())
/*      */             {
/* 2334 */               if (!creature.isTypeFleeing() || (creature
/* 2335 */                 .getCurrentVillage() == null && Server.rand.nextInt(20) == 0) || (creature
/* 2336 */                 .getCurrentVillage() != null && Server.rand.nextInt(50) == 0)) {
/*      */                 
/* 2338 */                 MeshIO mesh = Server.surfaceMesh;
/* 2339 */                 byte type = Tiles.decodeType(tilenum);
/* 2340 */                 if (type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_LAWN.id || type == Tiles.Tile.TILE_REED.id || type == Tiles.Tile.TILE_DIRT.id || type == Tiles.Tile.TILE_MYCELIUM.id) {
/*      */ 
/*      */                   
/* 2343 */                   mesh.setTile(tileX + diffTileX, tileY + diffTileY, Tiles.encode(Tiles.decodeHeight(tilenum), Tiles.Tile.TILE_DIRT_PACKED.id, 
/* 2344 */                         Tiles.decodeData(tilenum)));
/* 2345 */                   Players.getInstance().sendChangedTile(tileX + diffTileX, tileY + diffTileY, creature
/* 2346 */                       .isOnSurface(), true);
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           }
/*      */         }
/*      */       } 
/* 2353 */       float lElapsedTime = (float)(System.nanoTime() - now) / 1000000.0F;
/* 2354 */       if (lElapsedTime > (float)LOG_ELAPSED_TIME_THRESHOLD)
/*      */       {
/* 2356 */         logger.info("createTrack, Creature id, " + creature.getWurmId() + ", which took " + lElapsedTime + " millis. - " + creature);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void changeTile(int x, int y) {
/* 2364 */     VolaTile tile1 = getOrCreateTile(x, y);
/*      */     
/* 2366 */     Creature[] crets = tile1.getCreatures();
/* 2367 */     for (Creature lCret2 : crets)
/* 2368 */       lCret2.setChangedTileCounter(); 
/* 2369 */     VolaTile tile = Zones.getTileOrNull(x - 1, y, this.isOnSurface);
/* 2370 */     if (tile != null) {
/*      */       
/* 2372 */       crets = tile.getCreatures();
/* 2373 */       for (Creature lCret2 : crets)
/* 2374 */         lCret2.setChangedTileCounter(); 
/*      */     } 
/* 2376 */     tile = Zones.getTileOrNull(x - 1, y - 1, this.isOnSurface);
/* 2377 */     if (tile != null) {
/*      */       
/* 2379 */       crets = tile.getCreatures();
/* 2380 */       for (Creature lCret2 : crets)
/* 2381 */         lCret2.setChangedTileCounter(); 
/*      */     } 
/* 2383 */     tile = Zones.getTileOrNull(x, y - 1, this.isOnSurface);
/* 2384 */     if (tile != null) {
/*      */       
/* 2386 */       crets = tile.getCreatures();
/* 2387 */       for (Creature lCret2 : crets)
/* 2388 */         lCret2.setChangedTileCounter(); 
/*      */     } 
/* 2390 */     tile = Zones.getTileOrNull(x + 1, y - 1, this.isOnSurface);
/* 2391 */     if (tile != null) {
/*      */       
/* 2393 */       crets = tile.getCreatures();
/* 2394 */       for (Creature lCret2 : crets)
/* 2395 */         lCret2.setChangedTileCounter(); 
/*      */     } 
/* 2397 */     tile = Zones.getTileOrNull(x + 1, y, this.isOnSurface);
/* 2398 */     if (tile != null) {
/*      */       
/* 2400 */       crets = tile.getCreatures();
/* 2401 */       for (Creature lCret2 : crets)
/* 2402 */         lCret2.setChangedTileCounter(); 
/*      */     } 
/* 2404 */     tile = Zones.getTileOrNull(x - 1, y + 1, this.isOnSurface);
/* 2405 */     if (tile != null) {
/*      */       
/* 2407 */       crets = tile.getCreatures();
/* 2408 */       for (Creature lCret2 : crets)
/* 2409 */         lCret2.setChangedTileCounter(); 
/*      */     } 
/* 2411 */     tile = Zones.getTileOrNull(x, y + 1, this.isOnSurface);
/* 2412 */     if (tile != null) {
/*      */       
/* 2414 */       crets = tile.getCreatures();
/* 2415 */       for (Creature lCret2 : crets)
/* 2416 */         lCret2.setChangedTileCounter(); 
/*      */     } 
/* 2418 */     tile = Zones.getTileOrNull(x + 1, y + 1, this.isOnSurface);
/* 2419 */     if (tile != null) {
/*      */       
/* 2421 */       crets = tile.getCreatures();
/* 2422 */       for (Creature lCret2 : crets)
/* 2423 */         lCret2.setChangedTileCounter(); 
/*      */     } 
/* 2425 */     tile1.change();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void addGates(Village village) {
/* 2430 */     for (VolaTile tile : this.tiles.values()) {
/*      */       
/* 2432 */       Door[] doors = tile.getDoors();
/* 2433 */       if (doors != null)
/*      */       {
/* 2435 */         for (Door lDoor : doors) {
/*      */           
/* 2437 */           if (lDoor instanceof FenceGate)
/*      */           {
/* 2439 */             if (village.covers(tile.getTileX(), tile.getTileY())) {
/* 2440 */               village.addGate((FenceGate)lDoor);
/*      */             }
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void addMineDoors(Village village) {
/* 2451 */     for (int x = this.startX; x < this.endX; x++) {
/*      */       
/* 2453 */       for (int y = this.startY; y < this.endY; y++) {
/*      */         
/* 2455 */         MineDoorPermission md = MineDoorPermission.getPermission(x, y);
/* 2456 */         if (md != null)
/*      */         {
/* 2458 */           if (village.covers(x, y)) {
/* 2459 */             village.addMineDoor(md);
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/* 2466 */   static int totalItems = 0;
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void loadAllItemsForZone() {
/* 2471 */     if (Items.getAllItemsForZone(this.id) != null)
/*      */     {
/* 2473 */       for (Item item : Items.getAllItemsForZone(this.id))
/*      */       {
/* 2475 */         addItem(item, false, false, true);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void getItemsByZoneId() {}
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void save() throws IOException;
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void load() throws IOException;
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void loadFences() throws IOException;
/*      */ 
/*      */ 
/*      */   
/*      */   final void checkIntegrity(Creature checker) {
/* 2500 */     for (VolaTile t : this.tiles.values()) {
/*      */       
/* 2502 */       for (VolaTile t2 : this.tiles.values()) {
/*      */         
/* 2504 */         if (t != t2 && t.tilex == t2.tilex && t.tiley == t2.tiley)
/*      */         {
/* 2506 */           checker.getCommunicator().sendNormalServerMessage("Z " + 
/* 2507 */               getId() + " multiple tiles:" + t.tilex + ", " + t.tiley);
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
/*      */   public final String toString() {
/* 2521 */     StringBuilder lBuilder = new StringBuilder(200);
/* 2522 */     lBuilder.append("Zone [id: ").append(this.id);
/* 2523 */     lBuilder.append(", startXY: ").append(this.startX).append(',').append(this.startY);
/* 2524 */     lBuilder.append(", endXY: ").append(this.endX).append(',').append(this.endY);
/* 2525 */     lBuilder.append(", size: ").append(this.size);
/* 2526 */     lBuilder.append(", highest: ").append(this.highest);
/* 2527 */     lBuilder.append(", isForest: ").append(this.isForest);
/* 2528 */     lBuilder.append(", isLoaded: ").append(this.isLoaded);
/* 2529 */     lBuilder.append(", isOnSurface: ").append(this.isOnSurface);
/* 2530 */     lBuilder.append(']');
/* 2531 */     return super.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isHasRift() {
/* 2536 */     return this.hasRift;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setHasRift(boolean hasRift) {
/* 2541 */     this.hasRift = hasRift;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\Zone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */