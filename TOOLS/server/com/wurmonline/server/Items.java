/*      */ package com.wurmonline.server;
/*      */ 
/*      */ import com.wurmonline.server.behaviours.Vehicles;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.Delivery;
/*      */ import com.wurmonline.server.economy.MonetaryConstants;
/*      */ import com.wurmonline.server.endgames.EndGameItems;
/*      */ import com.wurmonline.server.highways.MethodsHighways;
/*      */ import com.wurmonline.server.highways.Routes;
/*      */ import com.wurmonline.server.items.CoinDbStrings;
/*      */ import com.wurmonline.server.items.DbItem;
/*      */ import com.wurmonline.server.items.DbStrings;
/*      */ import com.wurmonline.server.items.FrozenItemDbStrings;
/*      */ import com.wurmonline.server.items.InitialContainer;
/*      */ import com.wurmonline.server.items.InscriptionData;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemData;
/*      */ import com.wurmonline.server.items.ItemDbStrings;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemMealData;
/*      */ import com.wurmonline.server.items.ItemMetaData;
/*      */ import com.wurmonline.server.items.ItemRequirement;
/*      */ import com.wurmonline.server.items.ItemSettings;
/*      */ import com.wurmonline.server.items.ItemSpellEffects;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.Itempool;
/*      */ import com.wurmonline.server.items.NoSpaceException;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.PermissionsHistories;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.tutorial.MissionTargets;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nullable;
/*      */ import javax.annotation.concurrent.GuardedBy;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Items
/*      */   implements MiscConstants, MonetaryConstants, CounterTypes, TimeConstants
/*      */ {
/*   68 */   private static final ConcurrentHashMap<Long, Item> items = new ConcurrentHashMap<>();
/*      */ 
/*      */ 
/*      */   
/*   72 */   private static Logger logger = Logger.getLogger(Items.class.getName());
/*      */   
/*   74 */   public static final Logger debug = Logger.getLogger("ItemDebug");
/*      */ 
/*      */ 
/*      */   
/*   78 */   private static final ConcurrentHashMap<Item, Creature> draggedItems = new ConcurrentHashMap<>();
/*      */   
/*      */   private static final String GETITEMDATA = "SELECT * FROM ITEMDATA";
/*      */   private static final String GETITEMINSCRIPTIONDATA = "SELECT * FROM INSCRIPTIONS";
/*   82 */   private static final ConcurrentHashMap<Long, Set<Item>> containedItems = new ConcurrentHashMap<>();
/*   83 */   private static final ConcurrentHashMap<Long, Set<Item>> creatureItemsMap = new ConcurrentHashMap<>();
/*   84 */   private static final ConcurrentHashMap<Long, ItemData> itemDataMap = new ConcurrentHashMap<>();
/*   85 */   private static final ConcurrentHashMap<Long, InscriptionData> itemInscriptionDataMap = new ConcurrentHashMap<>();
/*      */   public static final int MAX_COUNT_ITEMS = 100;
/*      */   public static final int MAX_DECO_ITEMS = 15;
/*      */   private static final int MAX_EGGS = 1000;
/*   89 */   private static int currentEggs = 0;
/*      */   @GuardedBy("ITEM_DATA_RW_LOCK")
/*   91 */   private static final Set<Long> protectedCorpses = new HashSet<>();
/*      */   @GuardedBy("HIDDEN_ITEMS_RW_LOCK")
/*   93 */   private static final Set<Item> hiddenItems = new HashSet<>();
/*   94 */   private static final Set<Long> unstableRifts = new HashSet<>();
/*   95 */   private static final LinkedList<Item> spawnPoints = new LinkedList<>();
/*   96 */   private static final Set<Item> tents = new HashSet<>();
/*      */   private static boolean loadedCorpses = false;
/*   98 */   private static final ConcurrentHashMap<Long, Item> gmsigns = new ConcurrentHashMap<>();
/*   99 */   private static final ConcurrentHashMap<Long, Item> markers = new ConcurrentHashMap<>();
/*      */   
/*  101 */   private static final ConcurrentHashMap<Integer, Map<Integer, Set<Item>>> markersXY = new ConcurrentHashMap<>();
/*  102 */   private static final ConcurrentHashMap<Long, Item> waystones = new ConcurrentHashMap<>();
/*  103 */   private static final ConcurrentHashMap<Long, Integer> waystoneContainerCount = new ConcurrentHashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  108 */   private static final ReentrantReadWriteLock HIDDEN_ITEMS_RW_LOCK = new ReentrantReadWriteLock();
/*      */   
/*  110 */   private static final Set<Item> tempHiddenItems = new HashSet<>();
/*      */ 
/*      */   
/*  113 */   private static final Set<Item> warTargetItems = new HashSet<>();
/*      */ 
/*      */   
/*  116 */   private static final Set<Item> sourceSprings = new HashSet<>();
/*      */ 
/*      */   
/*  119 */   private static final Set<Item> supplyDepots = new HashSet<>();
/*      */   
/*  121 */   private static final Set<Item> harvestableItems = new HashSet<>();
/*      */   
/*  123 */   private static final Item[] emptyItems = new Item[0];
/*  124 */   private static final String moveItemsToFreezerForPlayer = DbConnector.isUseSqlite() ? "INSERT OR IGNORE INTO FROZENITEMS SELECT * FROM ITEMS WHERE OWNERID=?" : "INSERT IGNORE INTO FROZENITEMS (SELECT * FROM ITEMS WHERE OWNERID=?)";
/*      */   
/*      */   private static final String deleteInventoryItemsForPlayer = "DELETE FROM ITEMS WHERE OWNERID=?";
/*  127 */   private static final String returnItemsFromFreezerForPlayer = DbConnector.isUseSqlite() ? "INSERT OR IGNORE INTO ITEMS SELECT * FROM FROZENITEMS WHERE OWNERID=?" : "INSERT IGNORE INTO ITEMS (SELECT * FROM FROZENITEMS WHERE OWNERID=?)";
/*      */   
/*      */   private static final String deleteFrozenItemsForPlayer = "DELETE FROM FROZENITEMS WHERE OWNERID=?";
/*  130 */   private static final String returnItemFromFreezer = DbConnector.isUseSqlite() ? "INSERT OR IGNORE INTO ITEMS SELECT * FROM FROZENITEMS WHERE WURMID=?" : "INSERT IGNORE INTO ITEMS (SELECT * FROM FROZENITEMS WHERE WURMID=?)";
/*      */   
/*      */   private static final String deleteFrozenItem = "DELETE FROM FROZENITEMS WHERE WURMID=?";
/*      */   
/*      */   private static final String insertProtectedCorpse = "INSERT INTO PROTECTEDCORPSES(WURMID)VALUES(?)";
/*      */   private static final String deleteProtectedCorpse = "DELETE FROM PROTECTEDCORPSES WHERE WURMID=?";
/*      */   private static final String loadProtectedCorpse = "SELECT * FROM PROTECTEDCORPSES";
/*  137 */   private static final Map<Integer, Set<Item>> zoneItemsAtLoad = new ConcurrentHashMap<>();
/*      */   
/*      */   public static final long riftEndTime = 1482227988600L;
/*      */   
/*  141 */   private static long cpOne = 0L;
/*      */   
/*  143 */   private static long cpTwo = 0L;
/*      */   
/*  145 */   private static long cpThree = 0L;
/*      */   
/*  147 */   private static long cpFour = 0L;
/*      */   
/*  149 */   private static long numCoins = 0L;
/*      */   
/*  151 */   private static long numItems = 0L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getCpOne() {
/*  167 */     return cpOne;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getCpTwo() {
/*  177 */     return cpTwo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getCpThree() {
/*  187 */     return cpThree;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getCpFour() {
/*  197 */     return cpFour;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getNumCoins() {
/*  207 */     return numCoins;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getNumItems() {
/*  217 */     return numItems;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void putItem(Item item) {
/*  223 */     items.put(new Long(item.getWurmId()), item);
/*      */     
/*  225 */     if (item.isItemSpawn())
/*  226 */       addSupplyDepot(item); 
/*  227 */     if (item.isUnstableRift())
/*  228 */       addUnstableRift(item); 
/*  229 */     if (item.getTemplate().isHarvestable()) {
/*  230 */       addHarvestableItem(item);
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean exists(Item item) {
/*  235 */     return (items.get(new Long(item.getWurmId())) != null);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean exists(long id) {
/*  240 */     return (items.get(new Long(id)) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Item getItem(long id) throws NoSuchItemException {
/*  246 */     Item toReturn = items.get(new Long(id));
/*  247 */     if (toReturn == null)
/*      */     {
/*  249 */       throw new NoSuchItemException("No item found with id " + id);
/*      */     }
/*  251 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Optional<Item> getItemOptional(long id) {
/*  256 */     Item item = null;
/*  257 */     item = items.get(new Long(id));
/*  258 */     Optional<Item> toReturn = Optional.ofNullable(item);
/*  259 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Set<Item> getItemsWithDesc(String descpart, boolean boat) throws NoSuchItemException {
/*  264 */     Set<Item> itemsToRet = new HashSet<>();
/*  265 */     String lDescpart = descpart.toLowerCase();
/*  266 */     for (Item ne : items.values()) {
/*      */       
/*  268 */       if ((boat && ne.isBoat()) || !boat)
/*      */       {
/*  270 */         if (ne.getDescription().length() > 0 && 
/*  271 */           ne.getDescription().toLowerCase().contains(lDescpart))
/*  272 */           itemsToRet.add(ne); 
/*      */       }
/*      */     } 
/*  275 */     return itemsToRet;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void countEggs() {
/*  280 */     long start = System.nanoTime();
/*  281 */     currentEggs = 0;
/*  282 */     int fountains = 0;
/*  283 */     int wildhives = 0;
/*  284 */     int hives = 0;
/*  285 */     for (Item nextEgg : items.values()) {
/*      */       
/*  287 */       if (nextEgg.getTemplateId() == 1239) {
/*  288 */         wildhives++; continue;
/*  289 */       }  if (nextEgg.getTemplateId() == 1175) {
/*  290 */         hives++; continue;
/*  291 */       }  if (nextEgg.isEgg() && nextEgg.getData1() > 0) {
/*      */         
/*  293 */         currentEggs++;
/*  294 */         if (logger.isLoggable(Level.FINER))
/*      */         {
/*  296 */           logger.finer("Found egg number: " + currentEggs + ", Item: " + nextEgg); } 
/*      */         continue;
/*      */       } 
/*  299 */       if (nextEgg.getParentId() != -10L && (nextEgg.getTemplateId() == 408 || nextEgg
/*  300 */         .getTemplateId() == 635 || nextEgg
/*  301 */         .getTemplateId() == 405))
/*      */       {
/*  303 */         fountains++;
/*      */       }
/*      */     } 
/*  306 */     float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/*  307 */     logger.log(Level.INFO, "Current number of eggs is " + currentEggs + " (max eggs is " + 'Ϩ' + ") That took " + lElapsedTime + " ms.");
/*      */     
/*  309 */     logger.log(Level.INFO, "Current number of wild hives is " + wildhives + " and domestic hives is " + hives + ".");
/*  310 */     if (Servers.isThisATestServer())
/*  311 */       Players.getInstance().sendGmMessage(null, "System", "Debug: Current number of wild hives is " + wildhives + " and domestic hives is " + hives + ".", false); 
/*  312 */     if (fountains > 0) {
/*  313 */       logger.log(Level.INFO, "Current number of fountains found in containers is " + fountains + ".");
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean mayLayEggs() {
/*  318 */     return (currentEggs < 1000);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Item[] getHiddenItemsAt(int tilex, int tiley, float height, boolean surfaced) {
/*  323 */     HIDDEN_ITEMS_RW_LOCK.readLock().lock();
/*  324 */     tempHiddenItems.clear();
/*      */ 
/*      */     
/*      */     try {
/*  328 */       for (Item i : hiddenItems) {
/*      */ 
/*      */         
/*  331 */         if ((int)i.getPosX() >> 2 == tilex && (int)i.getPosY() >> 2 == tiley && i.getPosZ() >= height && surfaced == i
/*  332 */           .isOnSurface())
/*      */         {
/*  334 */           tempHiddenItems.add(i);
/*      */         }
/*      */       } 
/*  337 */       if (tempHiddenItems.size() > 0) {
/*  338 */         return tempHiddenItems.<Item>toArray(new Item[tempHiddenItems.size()]);
/*      */       }
/*      */     } finally {
/*      */       
/*  342 */       HIDDEN_ITEMS_RW_LOCK.readLock().unlock();
/*      */     } 
/*  344 */     return emptyItems;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void revealItem(Item item) {
/*  349 */     HIDDEN_ITEMS_RW_LOCK.writeLock().lock();
/*      */     
/*      */     try {
/*  352 */       hiddenItems.remove(item);
/*      */     }
/*      */     finally {
/*      */       
/*  356 */       HIDDEN_ITEMS_RW_LOCK.writeLock().unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void hideItem(Creature performer, Item item, float height, boolean putOnSurface) {
/*  362 */     if (putOnSurface) {
/*      */       
/*      */       try
/*      */       {
/*  366 */         item.putInVoid();
/*  367 */         item.setPosX(performer.getPosX());
/*  368 */         item.setPosY(performer.getPosY());
/*  369 */         performer.getCurrentTile().getZone().addItem(item);
/*      */       }
/*  371 */       catch (Exception ex)
/*      */       {
/*  373 */         logger.log(Level.INFO, performer.getName() + " failed to hide item:" + ex.getMessage(), ex);
/*  374 */         performer.getCommunicator().sendNormalServerMessage("Failed to put the item on surface: " + ex.getMessage());
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  379 */       HIDDEN_ITEMS_RW_LOCK.writeLock().lock();
/*      */       
/*      */       try {
/*  382 */         hiddenItems.add(item);
/*      */       }
/*      */       finally {
/*      */         
/*  386 */         HIDDEN_ITEMS_RW_LOCK.writeLock().unlock();
/*      */       } 
/*  388 */       item.setHidden(true);
/*      */       
/*  390 */       int zoneId = item.getZoneId();
/*  391 */       if (zoneId < 0)
/*  392 */         zoneId = performer.getCurrentTile().getZone().getId(); 
/*  393 */       item.putInVoid();
/*  394 */       item.setPosX(performer.getPosX());
/*  395 */       item.setPosY(performer.getPosY());
/*  396 */       item.setPosZ(height);
/*  397 */       item.setZoneId(zoneId, true);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void removeItem(long id) {
/*  403 */     Item i = items.remove(new Long(id));
/*      */     
/*  405 */     if (i != null && i.getTemplate() != null && 
/*  406 */       i.getTemplate().isHarvestable()) {
/*  407 */       removeHarvestableItem(i);
/*      */     }
/*      */   }
/*      */   
/*      */   public static Item[] getAllItems() {
/*  412 */     return (Item[])items.values().toArray((Object[])new Item[items.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Item[] getManagedCartsFor(Player player, boolean includeAll) {
/*  417 */     Set<Item> carts = new HashSet<>();
/*  418 */     for (Item item : items.values()) {
/*      */       
/*  420 */       if (item.isCart() && 
/*  421 */         item.canManage((Creature)player) && (
/*  422 */         includeAll || item.isLocked()))
/*  423 */         carts.add(item); 
/*      */     } 
/*  425 */     return carts.<Item>toArray(new Item[carts.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Item[] getOwnedCartsFor(Player player) {
/*  430 */     Set<Item> carts = new HashSet<>();
/*  431 */     for (Item item : items.values()) {
/*      */       
/*  433 */       if (item.isCart() && 
/*  434 */         item.canManage((Creature)player))
/*  435 */         carts.add(item); 
/*      */     } 
/*  437 */     return carts.<Item>toArray(new Item[carts.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Item[] getManagedShipsFor(Player player, boolean includeAll) {
/*  442 */     Set<Item> ships = new HashSet<>();
/*  443 */     for (Item item : items.values()) {
/*      */       
/*  445 */       if (item.isBoat() && 
/*  446 */         item.canManage((Creature)player) && (
/*  447 */         includeAll || item.isLocked()))
/*  448 */         ships.add(item); 
/*      */     } 
/*  450 */     return ships.<Item>toArray(new Item[ships.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Item[] getOwnedShipsFor(Player player) {
/*  455 */     Set<Item> ships = new HashSet<>();
/*  456 */     for (Item item : items.values()) {
/*      */       
/*  458 */       if (item.isBoat() && 
/*  459 */         item.canManage((Creature)player))
/*  460 */         ships.add(item); 
/*      */     } 
/*  462 */     return ships.<Item>toArray(new Item[ships.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void getOwnedCorpsesCartsShipsFor(Player player, List<Item> corpses, List<Item> carts, List<Item> ships) {
/*  468 */     for (Item item : items.values()) {
/*      */       
/*  470 */       if (item.isCart()) {
/*      */         
/*  472 */         if (item.canManage((Creature)player))
/*  473 */           carts.add(item);  continue;
/*      */       } 
/*  475 */       if (item.isBoat()) {
/*      */         
/*  477 */         if (item.canManage((Creature)player))
/*  478 */           ships.add(item);  continue;
/*      */       } 
/*  480 */       if (item.getTemplateId() == 272)
/*      */       {
/*  482 */         if (item.getName().equals("corpse of " + player.getName()))
/*      */         {
/*  484 */           if (item.getZoneId() > -1)
/*      */           {
/*  486 */             corpses.add(item);
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
/*      */   public static int getNumberOfItems() {
/*  502 */     return items.size();
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
/*      */   public static int getNumberOfNormalItems() {
/*  514 */     int numberOfNormalItems = 0;
/*  515 */     for (Item lItem : items.values()) {
/*      */       
/*  517 */       int templateId = lItem.getTemplateId();
/*  518 */       if (templateId != 0 && templateId != 521 && (templateId < 50 || templateId > 61) && (templateId < 10 || templateId > 19))
/*      */       {
/*      */ 
/*      */         
/*  522 */         numberOfNormalItems++;
/*      */       }
/*      */     } 
/*  525 */     return numberOfNormalItems;
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
/*      */   public static void decay(long id, @Nullable DbStrings dbstrings) {
/*  563 */     if (WurmId.getType(id) != 19)
/*  564 */       ItemFactory.decay(id, dbstrings); 
/*  565 */     removeItem(id);
/*  566 */     setProtected(id, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void destroyItem(long id) {
/*  571 */     destroyItem(id, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void destroyItem(long id, boolean destroyKey) {
/*  576 */     destroyItem(id, destroyKey, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void destroyItem(long id, boolean destroyKey, boolean destroyRecycled) {
/*  581 */     Item dest = null;
/*      */     
/*      */     try {
/*  584 */       dest = getItem(id);
/*  585 */       if (dest.isTraded()) {
/*      */         
/*  587 */         dest.getTradeWindow().removeItem(dest);
/*  588 */         dest.setTradeWindow(null);
/*      */       } 
/*  590 */       if (dest.isTent())
/*  591 */         tents.remove(dest); 
/*  592 */       if (dest.isRoadMarker() && dest.isPlanted()) {
/*      */         
/*  594 */         if (dest.getWhatHappened().length() == 0)
/*      */         {
/*  596 */           dest.setWhatHappened("decayed away"); } 
/*  597 */         removeMarker(dest);
/*      */       } 
/*  599 */       if (dest.getTemplateId() == 677)
/*  600 */         removeGmSign(dest); 
/*  601 */       if (dest.getTemplateId() == 1309)
/*  602 */         removeWagonerContainer(dest); 
/*  603 */       stopDragging(dest);
/*      */       
/*  605 */       if (dest.isVehicle() || dest.isTent()) {
/*      */         
/*  607 */         Vehicles.destroyVehicle(id);
/*      */         
/*  609 */         ItemSettings.remove(id);
/*  610 */         if (dest.isBoat())
/*      */         {
/*  612 */           if (dest.getData() != -1L)
/*      */           {
/*  614 */             destroyItem(dest.getData(), destroyKey, destroyRecycled);
/*      */           }
/*      */         }
/*      */       } 
/*  618 */       MissionTargets.destroyMissionTarget(id, true);
/*  619 */       dest.deleteAllEffects();
/*  620 */       ItemSpellEffects effs = ItemSpellEffects.getSpellEffects(id);
/*  621 */       if (effs != null)
/*  622 */         effs.destroy(); 
/*  623 */       ItemRequirement.deleteRequirements(id);
/*  624 */       if (dest.isHugeAltar() || dest.isArtifact()) {
/*      */         
/*  626 */         if (dest.isArtifact()) {
/*  627 */           EndGameItems.deleteEndGameItem(EndGameItems.getEndGameItem(dest));
/*      */         }
/*  629 */       } else if (dest.isCoin()) {
/*  630 */         Server.getInstance().transaction(id, dest.getOwnerId(), -10L, "Destroyed", dest.getValue());
/*      */       } 
/*  632 */       if (dest.isUnfinished() || dest.isUseOnGroundOnly())
/*      */       {
/*  634 */         if (dest.getWatcherSet() != null) {
/*  635 */           for (Creature cret : dest.getWatcherSet())
/*      */           {
/*  637 */             cret.getCommunicator().sendRemoveFromCreationWindow(dest.getWurmId());
/*      */           }
/*      */         }
/*      */       }
/*  641 */       if (dest.getTemplate().getInitialContainers() != null)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/*  646 */         for (Item i : dest.getItemsAsArray()) {
/*      */ 
/*      */           
/*  649 */           for (InitialContainer ic : dest.getTemplate().getInitialContainers()) {
/*      */             
/*  651 */             if (i.getTemplateId() == ic.getTemplateId()) {
/*      */               
/*  653 */               destroyItem(i.getWurmId(), false, false);
/*      */               
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*  660 */       if (destroyKey && dest.isKey()) {
/*      */         
/*      */         try {
/*      */           
/*  664 */           long lockId = dest.getLockId();
/*  665 */           Item lock = getItem(lockId);
/*  666 */           lock.removeKey(id);
/*      */         }
/*  668 */         catch (NoSuchItemException nsi) {
/*      */           
/*  670 */           logger.log(Level.INFO, "No lock when destroying key " + dest
/*  671 */               .getWurmId() + ", ownerId: " + dest.getOwnerId() + ", lastOwnerId: " + dest
/*  672 */               .getLastOwnerId());
/*      */         } 
/*      */       }
/*  675 */       if (dest.isHollow() && dest.getLockId() != -10L)
/*      */       {
/*  677 */         destroyItem(dest.getLockId(), destroyKey, destroyRecycled);
/*      */       }
/*  679 */       if (dest.isLock() && destroyKey) {
/*      */         
/*  681 */         for (long l : dest.getKeyIds()) {
/*      */ 
/*      */           
/*      */           try {
/*  685 */             Item k = getItem(l);
/*  686 */             if (logger.isLoggable(Level.FINEST))
/*  687 */               logger.finest("Destroying key with name: " + k.getName() + " and template: " + k.getTemplate().getName()); 
/*  688 */             if (k.getTemplateId() == 166 || k.getTemplateId() == 663) {
/*  689 */               dest.removeKey(l);
/*      */             } else {
/*  691 */               destroyItem(l, destroyKey, destroyRecycled);
/*      */             } 
/*  693 */           } catch (NoSuchItemException ni) {
/*      */             
/*  695 */             logger.log(Level.WARNING, "Unable to find item for key: " + l, (Throwable)ni);
/*      */           } 
/*      */         } 
/*  698 */         Connection dbcon = null;
/*  699 */         PreparedStatement ps3 = null;
/*  700 */         PreparedStatement ps4 = null;
/*      */         
/*      */         try {
/*  703 */           dbcon = DbConnector.getItemDbCon();
/*  704 */           ps3 = dbcon.prepareStatement("DELETE FROM ITEMKEYS WHERE LOCKID=?");
/*  705 */           ps3.setLong(1, id);
/*  706 */           ps3.executeUpdate();
/*  707 */           DbUtilities.closeDatabaseObjects(ps3, null);
/*  708 */           ps4 = dbcon.prepareStatement("DELETE FROM LOCKS WHERE WURMID=?");
/*  709 */           ps4.setLong(1, id);
/*  710 */           ps4.executeUpdate();
/*      */         }
/*  712 */         catch (SQLException ex) {
/*      */           
/*  714 */           logger.log(Level.WARNING, "Failed to destroy lock/keys for item with id " + id, ex);
/*      */         }
/*      */         finally {
/*      */           
/*  718 */           DbUtilities.closeDatabaseObjects(ps3, null);
/*  719 */           DbUtilities.closeDatabaseObjects(ps4, null);
/*  720 */           DbConnector.returnConnection(dbcon);
/*      */         } 
/*      */       } 
/*  723 */       if (dest.getTemplateId() == 1127)
/*      */       {
/*      */         
/*  726 */         for (Item i : dest.getItemsAsArray())
/*      */         {
/*      */           
/*  729 */           if (i.getTemplateId() == 1128)
/*      */           {
/*  731 */             destroyItem(i.getWurmId(), false, false);
/*      */           }
/*      */         }
/*      */       
/*      */       }
/*  736 */     } catch (NoSuchItemException nsi) {
/*      */ 
/*      */       
/*  739 */       logger.log(Level.INFO, "Destroying " + id, (Throwable)nsi);
/*      */     } 
/*  741 */     if (dest != null) {
/*      */       
/*  743 */       if (dest.getTemplateId() == 169)
/*  744 */         debug.info("** removeAndEmpty: " + dest.getWurmId()); 
/*  745 */       dest.removeAndEmpty();
/*  746 */       if (!destroyRecycled && dest.isTypeRecycled()) {
/*      */         
/*  748 */         if (dest.getTemplateId() == 169)
/*  749 */           debug.info("** removeItem: " + dest.getWurmId()); 
/*  750 */         removeItem(id);
/*  751 */         Itempool.returnRecycledItem(dest);
/*      */       } else {
/*      */         
/*  754 */         decay(id, dest.getDbStrings());
/*      */       } 
/*      */     } 
/*  757 */     ItemSettings.remove(id);
/*  758 */     PermissionsHistories.remove(id);
/*  759 */     ItemMealData.delete(id);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isItemLoaded(long id) {
/*  765 */     Item item = items.get(new Long(id));
/*  766 */     if (item == null)
/*  767 */       return false; 
/*  768 */     return true;
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
/*      */   public static void startDragging(Creature dragger, Item dragged) {
/*  780 */     draggedItems.put(dragged, dragger);
/*  781 */     if (!dragged.isVehicle()) {
/*      */       
/*  783 */       dragged.setLastOwnerId(dragger.getWurmId());
/*  784 */       Item[] itemarr = dragged.getAllItems(false);
/*  785 */       for (Item lElement : itemarr) {
/*  786 */         lElement.setLastOwnerId(dragger.getWurmId());
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  792 */     dragger.setDraggedItem(dragged);
/*      */     
/*  794 */     if (dragger.getVisionArea() != null) {
/*  795 */       dragger.getVisionArea().broadCastUpdateSelectBar(dragged.getWurmId());
/*      */     }
/*      */   }
/*      */   
/*      */   public static void stopDragging(Item dragged) {
/*  800 */     dragged.savePosition();
/*      */     
/*  802 */     Creature creature = draggedItems.get(dragged);
/*  803 */     if (creature != null) {
/*      */       
/*  805 */       draggedItems.remove(dragged);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  810 */       creature.setDraggedItem(null);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isItemDragged(Item item) {
/*  816 */     return draggedItems.keySet().contains(item);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Creature getDragger(Item item) {
/*  821 */     return draggedItems.get(item);
/*      */   }
/*      */ 
/*      */   
/*      */   static void loadAllItempInscriptionData() {
/*  826 */     long start = System.nanoTime();
/*  827 */     Connection dbcon = null;
/*  828 */     PreparedStatement ps = null;
/*  829 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  832 */       dbcon = DbConnector.getItemDbCon();
/*  833 */       ps = dbcon.prepareStatement("SELECT * FROM INSCRIPTIONS");
/*  834 */       rs = ps.executeQuery();
/*      */       
/*  836 */       String inscription = "";
/*  837 */       String inscriber = "";
/*  838 */       int num = 0;
/*  839 */       while (rs.next()) {
/*      */         
/*  841 */         long iid = rs.getLong("WURMID");
/*  842 */         inscription = rs.getString("INSCRIPTION");
/*  843 */         inscriber = rs.getString("INSCRIBER");
/*  844 */         int penColor = rs.getInt("PENCOLOR");
/*  845 */         new InscriptionData(iid, inscription, inscriber, penColor);
/*  846 */         num++;
/*      */       } 
/*  848 */       float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/*  849 */       logger.log(Level.INFO, "Loaded " + num + " item inscription data entries, that took " + lElapsedTime + " ms");
/*      */     }
/*  851 */     catch (SQLException sqx) {
/*      */       
/*  853 */       logger.log(Level.WARNING, "Failed to load item inscription datas: " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  857 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  858 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static void loadAllItemData() {
/*  864 */     long start = System.nanoTime();
/*  865 */     Connection dbcon = null;
/*  866 */     PreparedStatement ps = null;
/*  867 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  870 */       dbcon = DbConnector.getItemDbCon();
/*  871 */       ps = dbcon.prepareStatement("SELECT * FROM ITEMDATA");
/*  872 */       rs = ps.executeQuery();
/*      */       
/*  874 */       int d1 = 0;
/*  875 */       int d2 = 0;
/*  876 */       int e1 = 0;
/*  877 */       int e2 = 0;
/*  878 */       int num = 0;
/*  879 */       while (rs.next()) {
/*      */         
/*  881 */         long iid = rs.getLong("WURMID");
/*  882 */         d1 = rs.getInt("DATA1");
/*  883 */         d2 = rs.getInt("DATA2");
/*  884 */         e1 = rs.getInt("EXTRA1");
/*  885 */         e2 = rs.getInt("EXTRA2");
/*  886 */         new ItemData(iid, d1, d2, e1, e2);
/*  887 */         num++;
/*      */       } 
/*  889 */       float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/*  890 */       logger.log(Level.INFO, "Loaded " + num + " item data entries, that took " + lElapsedTime + " ms");
/*      */     }
/*  892 */     catch (SQLException sqx) {
/*      */       
/*  894 */       logger.log(Level.WARNING, "Failed to load item datas: " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  898 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  899 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*  901 */     ItemSpellEffects.loadSpellEffectsForItems();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addItemInscriptionData(InscriptionData data) {
/*  911 */     itemInscriptionDataMap.put(new Long(data.getWurmId()), data);
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
/*      */   public static InscriptionData getItemInscriptionData(long itemid) {
/*  923 */     InscriptionData toReturn = null;
/*  924 */     toReturn = itemInscriptionDataMap.get(new Long(itemid));
/*  925 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addData(ItemData data) {
/*  931 */     itemDataMap.put(new Long(data.wurmid), data);
/*      */   }
/*      */ 
/*      */   
/*      */   public static ItemData getItemData(long itemid) {
/*  936 */     ItemData toReturn = null;
/*  937 */     toReturn = itemDataMap.get(new Long(itemid));
/*  938 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Set<Item> getAllItemsForZone(int zid) {
/*  943 */     return zoneItemsAtLoad.get(Integer.valueOf(zid));
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
/*      */   public static final boolean reloadAllSubItems(Player performer, long wurmId) {
/*  956 */     logger.log(Level.INFO, performer.getName() + " forcing a reload of all subitems of " + wurmId);
/*  957 */     long s = System.nanoTime();
/*  958 */     Connection dbcon = null;
/*  959 */     PreparedStatement ps = null;
/*  960 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  963 */       Item parentItem = getItem(wurmId);
/*  964 */       dbcon = DbConnector.getItemDbCon();
/*  965 */       ps = dbcon.prepareStatement("SELECT WURMID FROM ITEMS WHERE PARENTID=?");
/*  966 */       ps.setLong(1, wurmId);
/*  967 */       rs = ps.executeQuery();
/*  968 */       long iid = -10L;
/*      */       
/*  970 */       while (rs.next()) {
/*      */         
/*  972 */         iid = rs.getLong("WURMID");
/*      */ 
/*      */         
/*      */         try {
/*  976 */           Item i = getItem(iid);
/*  977 */           parentItem.insertItem(i, false);
/*      */         }
/*  979 */         catch (NoSuchItemException nsi) {
/*      */           
/*  981 */           logger.log(Level.INFO, "Could not reload subitem:" + iid + " for " + wurmId + " as item could not be found.", (Throwable)nsi);
/*  982 */           performer.getCommunicator().sendNormalServerMessage("Could not reload subitem:" + iid + " for " + wurmId + " as that item could not be found.");
/*      */         } 
/*      */       } 
/*      */       
/*  986 */       return true;
/*      */     }
/*  988 */     catch (NoSuchItemException nsi) {
/*      */       
/*  990 */       logger.log(Level.WARNING, "Could not reload subitems for " + wurmId + " as item could not be found.", (Throwable)nsi);
/*  991 */       performer.getCommunicator().sendNormalServerMessage("Could not reload subitems for " + wurmId + " as that item could not be found.");
/*      */     }
/*  993 */     catch (SQLException sqx) {
/*      */       
/*  995 */       logger.log(Level.WARNING, "Failed to reload subitems: " + sqx.getMessage(), sqx);
/*  996 */       performer.getCommunicator().sendNormalServerMessage("Could not reload subitems for " + wurmId + " due to a database error.");
/*      */     }
/*      */     finally {
/*      */       
/* 1000 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1001 */       DbConnector.returnConnection(dbcon);
/*      */       
/* 1003 */       logger.log(Level.INFO, performer.getName() + " reloaded subitems for " + wurmId + ". That took " + (
/* 1004 */           (float)(System.nanoTime() - s) / 1000000.0F) + " ms.");
/*      */     } 
/*      */     
/* 1007 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static void loadAllZoneItems(DbStrings dbstrings) {
/* 1012 */     if (!loadedCorpses) {
/* 1013 */       loadAllProtectedItems();
/*      */     }
/*      */     try {
/* 1016 */       logger.log(Level.INFO, "Loading all zone items using " + dbstrings);
/* 1017 */       long s = System.nanoTime();
/* 1018 */       Connection dbcon = null;
/* 1019 */       PreparedStatement ps = null;
/* 1020 */       ResultSet rs = null;
/*      */       
/*      */       try {
/* 1023 */         dbcon = DbConnector.getItemDbCon();
/* 1024 */         ps = dbcon.prepareStatement(dbstrings.getZoneItems());
/* 1025 */         rs = ps.executeQuery();
/* 1026 */         long iid = -10L;
/*      */         
/* 1028 */         while (rs.next()) {
/*      */           
/* 1030 */           iid = rs.getLong("WURMID");
/* 1031 */           String name = rs.getString("NAME");
/* 1032 */           float posx = rs.getFloat("POSX");
/* 1033 */           float posy = rs.getFloat("POSY");
/*      */           
/*      */           try {
/* 1036 */             ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(rs.getInt("TEMPLATEID"));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1050 */             DbItem dbItem = new DbItem(iid, temp, name, rs.getLong("LASTMAINTAINED"), rs.getFloat("QUALITYLEVEL"), rs.getFloat("ORIGINALQUALITYLEVEL"), rs.getInt("SIZEX"), rs.getInt("SIZEY"), rs.getInt("SIZEZ"), posx, posy, rs.getFloat("POSZ"), rs.getFloat("ROTATION"), rs.getLong("PARENTID"), -10L, rs.getInt("ZONEID"), rs.getFloat("DAMAGE"), rs.getInt("WEIGHT"), rs.getByte("MATERIAL"), rs.getLong("LOCKID"), rs.getShort("PLACE"), rs.getInt("PRICE"), rs.getShort("TEMPERATURE"), rs.getString("DESCRIPTION"), rs.getByte("BLESS"), rs.getByte("ENCHANT"), rs.getBoolean("BANKED"), rs.getLong("LASTOWNERID"), rs.getByte("AUXDATA"), rs.getLong("CREATIONDATE"), rs.getByte("CREATIONSTATE"), rs.getInt("REALTEMPLATE"), rs.getBoolean("WORNARMOUR"), rs.getInt("COLOR"), rs.getInt("COLOR2"), rs.getBoolean("FEMALE"), rs.getBoolean("MAILED"), rs.getBoolean("TRANSFERRED"), rs.getString("CREATOR"), rs.getBoolean("HIDDEN"), rs.getByte("MAILTIMES"), rs.getByte("RARITY"), rs.getLong("ONBRIDGE"), rs.getInt("SETTINGS"), rs.getBoolean("PLACEDONPARENT"), dbstrings);
/* 1051 */             if (((Item)dbItem).hidden) {
/*      */               
/* 1053 */               HIDDEN_ITEMS_RW_LOCK.writeLock().lock();
/*      */               
/*      */               try {
/* 1056 */                 hiddenItems.add(dbItem);
/*      */               }
/*      */               finally {
/*      */                 
/* 1060 */                 HIDDEN_ITEMS_RW_LOCK.writeLock().unlock();
/*      */               } 
/*      */             } 
/* 1063 */             if (dbItem.isWarTarget())
/* 1064 */               addWarTarget((Item)dbItem); 
/* 1065 */             if (dbItem.isSourceSpring()) {
/* 1066 */               addSourceSpring((Item)dbItem);
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1074 */             long pid = dbItem.getParentId();
/* 1075 */             if (pid != -10L) {
/*      */               
/* 1077 */               Set<Item> contained = containedItems.get(new Long(pid));
/* 1078 */               if (contained == null)
/* 1079 */                 contained = new HashSet<>(); 
/* 1080 */               contained.add(dbItem);
/* 1081 */               containedItems.put(new Long(pid), contained);
/*      */             } 
/* 1083 */             if (dbItem.getParentId() == -10L && dbItem.getZoneId() > 0) {
/*      */               
/* 1085 */               Set<Item> itemset = zoneItemsAtLoad.get(Integer.valueOf(dbItem.getZoneId()));
/* 1086 */               if (itemset == null) {
/*      */                 
/* 1088 */                 itemset = new HashSet<>();
/* 1089 */                 zoneItemsAtLoad.put(Integer.valueOf(dbItem.getZoneId()), itemset);
/*      */               } 
/* 1091 */               itemset.add(dbItem);
/*      */             } 
/* 1093 */             if (dbItem.isEgg() && dbItem.getData1() > 0)
/* 1094 */               currentEggs++; 
/* 1095 */             if (temp.getTemplateId() == 330)
/*      */             {
/* 1097 */               dbItem.setData(-1L);
/*      */             }
/*      */           }
/* 1100 */           catch (NoSuchTemplateException nst) {
/*      */             
/* 1102 */             logger.log(Level.WARNING, "Problem getting Template for item " + name + " (" + iid + ") @" + ((int)posx >> 2) + "," + ((int)posy >> 2) + "- " + nst
/*      */                 
/* 1104 */                 .getMessage(), (Throwable)nst);
/*      */           }
/*      */         
/*      */         } 
/* 1108 */       } catch (SQLException sqx) {
/*      */         
/* 1110 */         logger.log(Level.WARNING, "Failed to load zone items: " + sqx.getMessage(), sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1114 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 1115 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/* 1117 */       for (Item item : items.values())
/*      */       {
/* 1119 */         item.getContainedItems();
/*      */       }
/* 1121 */       long e = System.nanoTime();
/* 1122 */       logger.log(Level.INFO, "Loaded " + items.size() + " zone items. That took " + ((float)(e - s) / 1000000.0F) + " ms.");
/*      */ 
/*      */     
/*      */     }
/* 1126 */     catch (Exception ex) {
/*      */       
/* 1128 */       logger.log(Level.WARNING, "Problem loading zone items due to " + ex.getMessage(), ex);
/*      */     } 
/* 1130 */     Itempool.checkRecycledItems();
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Set<Item> getTents() {
/* 1135 */     return tents;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void addTent(Item tent) {
/* 1140 */     tents.add(tent);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void addGmSign(Item gmSign) {
/* 1145 */     gmsigns.put(Long.valueOf(gmSign.getWurmId()), gmSign);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void removeGmSign(Item gmSign) {
/* 1150 */     gmsigns.remove(Long.valueOf(gmSign.getWurmId()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Item[] getGMSigns() {
/* 1159 */     return (Item[])gmsigns.values().toArray((Object[])new Item[gmsigns.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void addMarker(Item marker) {
/* 1164 */     markers.put(Long.valueOf(marker.getWurmId()), marker);
/* 1165 */     if (marker.getTemplateId() == 1112)
/*      */     {
/* 1167 */       waystones.put(Long.valueOf(marker.getWurmId()), marker);
/*      */     }
/*      */     
/* 1170 */     Map<Integer, Set<Item>> ymap = markersXY.get(Integer.valueOf(marker.getTileX()));
/* 1171 */     if (ymap == null) {
/*      */       
/* 1173 */       ymap = new HashMap<>();
/* 1174 */       HashSet<Item> mset = new HashSet<>();
/* 1175 */       mset.add(marker);
/* 1176 */       ymap.put(Integer.valueOf(marker.getTileY()), mset);
/* 1177 */       markersXY.put(Integer.valueOf(marker.getTileX()), ymap);
/*      */     }
/*      */     else {
/*      */       
/* 1181 */       Set<Item> mset = ymap.get(Integer.valueOf(marker.getTileY()));
/* 1182 */       if (mset == null) {
/*      */         
/* 1184 */         mset = new HashSet<>();
/* 1185 */         ymap.put(Integer.valueOf(marker.getTileY()), mset);
/* 1186 */         mset.add(marker);
/*      */       } else {
/*      */         
/* 1189 */         mset.add(marker);
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
/*      */   public static final Item getMarker(int tilex, int tiley, boolean onSurface, int floorlevel, long bridgeId) {
/* 1205 */     Map<Integer, Set<Item>> ymap = markersXY.get(Integer.valueOf(tilex));
/* 1206 */     if (ymap != null) {
/*      */       
/* 1208 */       Set<Item> mset = ymap.get(Integer.valueOf(tiley));
/* 1209 */       if (mset != null)
/*      */       {
/* 1211 */         for (Item marker : mset) {
/*      */           
/* 1213 */           if (marker.isOnSurface() == onSurface && marker.getFloorLevel() == floorlevel && marker
/* 1214 */             .getBridgeId() == bridgeId)
/* 1215 */             return marker; 
/*      */         } 
/*      */       }
/*      */     } 
/* 1219 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void removeMarker(Item marker) {
/* 1230 */     if (marker.getAuxData() != 0) {
/* 1231 */       MethodsHighways.removeLinksTo(marker);
/*      */     }
/* 1233 */     markers.remove(Long.valueOf(marker.getWurmId()));
/* 1234 */     if (marker.getTemplateId() == 1112)
/*      */     {
/* 1236 */       waystones.remove(Long.valueOf(marker.getWurmId()));
/*      */     }
/*      */     
/* 1239 */     Routes.remove(marker);
/*      */     
/* 1241 */     Map<Integer, Set<Item>> ymap = markersXY.get(Integer.valueOf(marker.getTileX()));
/*      */     
/* 1243 */     if (ymap != null) {
/*      */ 
/*      */       
/* 1246 */       Set<Item> mset = ymap.get(Integer.valueOf(marker.getTileY()));
/* 1247 */       if (mset != null)
/*      */       {
/* 1249 */         for (Item item : mset) {
/*      */           
/* 1251 */           if (item.getWurmId() == marker.getWurmId()) {
/*      */             
/* 1253 */             mset.remove(marker);
/* 1254 */             if (mset.isEmpty()) {
/*      */               
/* 1256 */               ymap.remove(Integer.valueOf(marker.getTileY()));
/* 1257 */               if (ymap.isEmpty())
/*      */               {
/* 1259 */                 markersXY.remove(Integer.valueOf(marker.getTileX()));
/*      */               }
/*      */             } 
/*      */             return;
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
/*      */   public static final Item[] getWaystones() {
/* 1275 */     return (Item[])waystones.values().toArray((Object[])new Item[waystones.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Item[] getMarkers() {
/* 1284 */     return (Item[])markers.values().toArray((Object[])new Item[markers.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void addWagonerContainer(Item wagonerContainer) {
/* 1293 */     long waystoneId = wagonerContainer.getData();
/* 1294 */     Integer count = waystoneContainerCount.get(Long.valueOf(waystoneId));
/* 1295 */     if (count == null) {
/* 1296 */       waystoneContainerCount.put(Long.valueOf(waystoneId), Integer.valueOf(1));
/*      */     } else {
/* 1298 */       waystoneContainerCount.put(Long.valueOf(waystoneId), Integer.valueOf(count.intValue() + 1));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void removeWagonerContainer(Item wagonerContainer) {
/* 1307 */     long waystoneId = wagonerContainer.getData();
/* 1308 */     Integer count = waystoneContainerCount.get(Long.valueOf(waystoneId));
/*      */     
/* 1310 */     if (count != null) {
/*      */       
/* 1312 */       int icount = count.intValue();
/* 1313 */       if (icount > 1) {
/* 1314 */         waystoneContainerCount.put(Long.valueOf(waystoneId), Integer.valueOf(icount - 1));
/*      */       } else {
/*      */         
/* 1317 */         waystoneContainerCount.remove(Long.valueOf(waystoneId));
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
/*      */   public static final boolean isWaystoneInUse(long waystoneId) {
/* 1329 */     Integer count = waystoneContainerCount.get(Long.valueOf(waystoneId));
/* 1330 */     if (count != null) {
/* 1331 */       return true;
/*      */     }
/* 1333 */     return Delivery.isDeliveryPoint(waystoneId);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void addSpawn(Item spawn) {
/* 1338 */     spawnPoints.add(spawn);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void removeSpawn(Item spawn) {
/* 1343 */     spawnPoints.remove(spawn);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Item[] getSpawnPoints() {
/* 1348 */     return spawnPoints.<Item>toArray(new Item[spawnPoints.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void addUnstableRift(Item rift) {
/* 1353 */     unstableRifts.add(Long.valueOf(rift.getWurmId()));
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void pollUnstableRifts() {
/* 1358 */     if (System.currentTimeMillis() > 1482227988600L)
/*      */     {
/* 1360 */       if (!unstableRifts.isEmpty()) {
/*      */         
/* 1362 */         if (unstableRifts.size() >= 15)
/* 1363 */           Server.getInstance().broadCastAlert("A shimmering wave of light runs over all the land as all the source rifts collapse."); 
/* 1364 */         for (Long rift : unstableRifts)
/*      */         {
/* 1366 */           destroyItem(rift.longValue());
/*      */         }
/* 1368 */         unstableRifts.clear();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void removeTent(Item tent) {
/* 1375 */     tents.remove(tent);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void addWarTarget(Item target) {
/* 1380 */     warTargetItems.add(target);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Item[] getWarTargets() {
/* 1385 */     return warTargetItems.<Item>toArray(new Item[warTargetItems.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void addSourceSpring(Item spring) {
/* 1390 */     sourceSprings.add(spring);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Item[] getSourceSprings() {
/* 1395 */     return sourceSprings.<Item>toArray(new Item[sourceSprings.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void addSupplyDepot(Item depot) {
/* 1400 */     supplyDepots.add(depot);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Item[] getSupplyDepots() {
/* 1405 */     return supplyDepots.<Item>toArray(new Item[supplyDepots.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void addHarvestableItem(Item harvestable) {
/* 1410 */     harvestableItems.add(harvestable);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void removeHarvestableItem(Item harvestable) {
/* 1415 */     harvestableItems.remove(harvestable);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Item[] getHarvestableItems() {
/* 1420 */     return harvestableItems.<Item>toArray(new Item[harvestableItems.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isHighestQLForTemplate(int itemTemplate, float itemql, long itemId, boolean before) {
/* 1426 */     if (itemTemplate == 179)
/* 1427 */       return false; 
/* 1428 */     if (itemTemplate == 386)
/* 1429 */       return false; 
/* 1430 */     if (itemql < 80.0F)
/* 1431 */       return false; 
/* 1432 */     boolean searched = false;
/* 1433 */     for (Item i : items.values()) {
/*      */       
/* 1435 */       if (i.getTemplateId() == itemTemplate)
/*      */       {
/* 1437 */         if (!before || itemId != i.getWurmId()) {
/*      */ 
/*      */ 
/*      */           
/* 1441 */           searched = true;
/* 1442 */           if (i.getOriginalQualityLevel() > itemql)
/*      */           {
/* 1444 */             return false;
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/* 1449 */     if (!searched)
/* 1450 */       return false; 
/* 1451 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   static Item createMetaDataItem(ItemMetaData md) {
/* 1456 */     long iid = md.itemId;
/*      */     
/*      */     try {
/* 1459 */       ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(md.itemtemplateId);
/*      */       
/* 1461 */       DbItem dbItem = new DbItem(iid, temp, md.itname, md.lastmaintained, md.ql, md.origQl, md.sizex, md.sizey, md.sizez, md.posx, md.posy, md.posz, 0.0F, md.parentId, md.ownerId, -10, md.itemdam, md.weight, md.material, md.lockid, md.place, md.price, md.temp, md.desc, md.bless, md.enchantment, md.banked, md.lastowner, md.auxbyte, md.creationDate, md.creationState, md.realTemplate, md.wornAsArmour, md.color, md.color2, md.female, md.mailed, false, md.creator, false, (byte)0, md.rarity, md.onBridge, md.settings, false, md.instance);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1467 */       if (((Item)dbItem).hidden) {
/*      */         
/* 1469 */         HIDDEN_ITEMS_RW_LOCK.writeLock().lock();
/*      */         
/*      */         try {
/* 1472 */           hiddenItems.add(dbItem);
/*      */         }
/*      */         finally {
/*      */           
/* 1476 */           HIDDEN_ITEMS_RW_LOCK.writeLock().unlock();
/*      */         } 
/*      */       } 
/* 1479 */       if (Servers.localServer.testServer)
/* 1480 */         logger.log(Level.INFO, "Converting " + dbItem.getName() + ", " + dbItem.getWurmId()); 
/* 1481 */       if (dbItem.isDraggable()) {
/*      */ 
/*      */ 
/*      */         
/* 1485 */         float newPosX = ((int)dbItem.getPosX() >> 2 << 2) + 0.5F + Server.rand.nextFloat() * 2.0F;
/* 1486 */         float newPosY = ((int)dbItem.getPosY() >> 2 << 2) + 0.5F + Server.rand.nextFloat() * 2.0F;
/* 1487 */         dbItem.setTempPositions(newPosX, newPosY, dbItem.getPosZ(), dbItem.getRotation());
/*      */       } 
/* 1489 */       long pid = dbItem.getParentId();
/* 1490 */       if (pid != -10L) {
/*      */         
/* 1492 */         Set<Item> contained = containedItems.get(Long.valueOf(pid));
/* 1493 */         if (contained == null)
/* 1494 */           contained = new HashSet<>(); 
/* 1495 */         contained.add(dbItem);
/* 1496 */         containedItems.put(Long.valueOf(pid), contained);
/*      */       } 
/* 1498 */       if (dbItem.isEgg() && dbItem.getData1() > 0)
/* 1499 */         currentEggs++; 
/* 1500 */       return (Item)dbItem;
/*      */     }
/* 1502 */     catch (NoSuchTemplateException nst) {
/*      */       
/* 1504 */       logger.log(Level.WARNING, "Problem getting Template for item with Wurm ID " + iid + " - " + nst.getMessage(), (Throwable)nst);
/*      */       
/* 1506 */       return null;
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
/*      */   public static void convertItemMetaData(ItemMetaData[] metadatas) {
/* 1518 */     long start = System.nanoTime();
/* 1519 */     Set<Item> mditems = new HashSet<>();
/* 1520 */     for (ItemMetaData lMetadata : metadatas)
/*      */     {
/* 1522 */       mditems.add(createMetaDataItem(lMetadata));
/*      */     }
/* 1524 */     for (Item item : mditems) {
/*      */       
/* 1526 */       if (Servers.localServer.testServer)
/* 1527 */         logger.log(Level.INFO, "Found " + item.getName()); 
/* 1528 */       if (item != null)
/* 1529 */         item.getContainedItems(); 
/*      */     } 
/* 1531 */     float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 1532 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 1534 */       logger.finer("Unpacked " + mditems.size() + " transferred items. That took " + lElapsedTime + " ms.");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void loadAllItemEffects() {
/* 1544 */     logger.info("Loading item effects.");
/* 1545 */     long now = System.nanoTime();
/*      */     
/* 1547 */     int numberOfEffectsLoaded = 0;
/*      */     
/* 1549 */     Item[] itarr = getAllItems();
/* 1550 */     for (Item lElement : itarr) {
/*      */       
/* 1552 */       if (lElement.getTemperature() > 1000 || lElement.isAlwaysLit() || lElement.isItemSpawn()) {
/*      */         
/* 1554 */         lElement.loadEffects();
/* 1555 */         numberOfEffectsLoaded++;
/*      */       } 
/*      */     } 
/*      */     
/* 1559 */     logger.log(Level.INFO, "Loaded " + numberOfEffectsLoaded + " item effects. That took " + ((float)(System.nanoTime() - now) / 1000000.0F) + " ms.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void returnItemFromFreezer(long wurmId) {
/* 1566 */     boolean ok = false;
/* 1567 */     Connection dbcon = null;
/* 1568 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1571 */       dbcon = DbConnector.getItemDbCon();
/* 1572 */       ps = dbcon.prepareStatement(returnItemFromFreezer);
/* 1573 */       ps.setLong(1, wurmId);
/* 1574 */       ps.execute();
/* 1575 */       ok = true;
/*      */     }
/* 1577 */     catch (SQLException sqx) {
/*      */       
/* 1579 */       logger.log(Level.WARNING, "Failed to move item from freezer  " + wurmId + " : " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1583 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1584 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/* 1587 */     if (ok) {
/*      */       
/*      */       try {
/*      */         
/* 1591 */         dbcon = DbConnector.getItemDbCon();
/* 1592 */         ps = dbcon.prepareStatement("DELETE FROM FROZENITEMS WHERE WURMID=?");
/* 1593 */         ps.setLong(1, wurmId);
/* 1594 */         ps.execute();
/*      */       }
/* 1596 */       catch (SQLException sqx) {
/*      */         
/* 1598 */         logger.log(Level.WARNING, "Failed to delete item when moved to freezer " + wurmId + " : " + sqx.getMessage(), sqx);
/*      */       
/*      */       }
/*      */       finally {
/*      */         
/* 1603 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1604 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     }
/* 1607 */     if (ok) {
/*      */       
/*      */       try {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1614 */         Item i = getItem(wurmId);
/* 1615 */         if (i.getDbStrings() == FrozenItemDbStrings.getInstance()) {
/* 1616 */           i.setDbStrings((DbStrings)ItemDbStrings.getInstance());
/*      */         }
/* 1618 */       } catch (NoSuchItemException noSuchItemException) {}
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void returnItemsFromFreezerFor(long playerId) {
/* 1628 */     boolean ok = false;
/* 1629 */     Connection dbcon = null;
/* 1630 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1633 */       dbcon = DbConnector.getItemDbCon();
/* 1634 */       ps = dbcon.prepareStatement(returnItemsFromFreezerForPlayer);
/* 1635 */       ps.setLong(1, playerId);
/* 1636 */       ps.execute();
/* 1637 */       ok = true;
/*      */     }
/* 1639 */     catch (SQLException sqx) {
/*      */       
/* 1641 */       logger.log(Level.WARNING, "Failed to move items from freezer for creature " + playerId + " : " + sqx.getMessage(), sqx);
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/* 1646 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1647 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/* 1650 */     if (ok) {
/*      */       
/*      */       try {
/*      */         
/* 1654 */         dbcon = DbConnector.getItemDbCon();
/* 1655 */         ps = dbcon.prepareStatement("DELETE FROM FROZENITEMS WHERE OWNERID=?");
/* 1656 */         ps.setLong(1, playerId);
/* 1657 */         ps.execute();
/*      */       }
/* 1659 */       catch (SQLException sqx) {
/*      */         
/* 1661 */         logger.log(Level.WARNING, "Failed to delete items when moved to freezer for creature " + playerId + " : " + sqx
/* 1662 */             .getMessage(), sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1666 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1667 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     }
/* 1670 */     if (ok) {
/*      */       
/* 1672 */       PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(playerId);
/* 1673 */       if (pinf != null) {
/*      */ 
/*      */         
/* 1676 */         pinf.setMovedInventory(false);
/*      */         
/*      */         try {
/* 1679 */           pinf.save();
/*      */         }
/* 1681 */         catch (IOException iox) {
/*      */           
/* 1683 */           logger.log(Level.WARNING, iox.getMessage());
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1689 */       for (Item i : items.values()) {
/*      */         
/* 1691 */         if (i.getOwnerId() == playerId)
/*      */         {
/* 1693 */           if (i.getDbStrings() == FrozenItemDbStrings.getInstance()) {
/* 1694 */             i.setDbStrings((DbStrings)ItemDbStrings.getInstance());
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean moveItemsToFreezerFor(long playerId) {
/* 1703 */     boolean ok = false;
/* 1704 */     Connection dbcon = null;
/* 1705 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1708 */       dbcon = DbConnector.getItemDbCon();
/* 1709 */       ps = dbcon.prepareStatement(moveItemsToFreezerForPlayer);
/* 1710 */       ps.setLong(1, playerId);
/* 1711 */       ps.execute();
/* 1712 */       ok = true;
/*      */     }
/* 1714 */     catch (SQLException sqx) {
/*      */       
/* 1716 */       logger.log(Level.WARNING, "Failed to move items to freezer for creature " + playerId + " : " + sqx.getMessage(), sqx);
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/* 1721 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1722 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/* 1725 */     if (ok) {
/*      */       
/*      */       try {
/*      */         
/* 1729 */         dbcon = DbConnector.getItemDbCon();
/* 1730 */         ps = dbcon.prepareStatement("DELETE FROM ITEMS WHERE OWNERID=?");
/* 1731 */         ps.setLong(1, playerId);
/* 1732 */         ps.execute();
/*      */       }
/* 1734 */       catch (SQLException sqx) {
/*      */         
/* 1736 */         logger.log(Level.WARNING, "Failed to delete items when moved to freezer for creature " + playerId + " : " + sqx
/* 1737 */             .getMessage(), sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1741 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1742 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     }
/* 1745 */     if (ok)
/*      */     {
/*      */ 
/*      */       
/* 1749 */       for (Item i : items.values()) {
/*      */         
/* 1751 */         if (i.getOwnerId() == playerId)
/*      */         {
/* 1753 */           if (i.getDbStrings() == ItemDbStrings.getInstance()) {
/*      */             
/* 1755 */             i.setDbStrings((DbStrings)FrozenItemDbStrings.getInstance());
/* 1756 */             logger.log(Level.INFO, "Changed dbstrings for item " + i.getWurmId() + " and player " + playerId + " to frozen");
/*      */           } 
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/* 1762 */     return ok;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Set<Long> loadAllNonTransferredItemsIdsForCreature(long creatureId, PlayerInfo info) {
/* 1767 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 1769 */       logger.finer("Loading items for " + creatureId);
/*      */     }
/* 1771 */     Set<Long> creatureItemIds = new HashSet<>();
/*      */     
/* 1773 */     if (info != null && info.hasMovedInventory()) {
/*      */       
/* 1775 */       returnItemsFromFreezerFor(creatureId);
/* 1776 */       info.setMovedInventory(false);
/* 1777 */       PlayerInfoFactory.getDeleteLogger().log(Level.INFO, "Returned items for " + info.getName() + " after transfer");
/*      */     } 
/*      */ 
/*      */     
/* 1781 */     Connection dbcon = null;
/* 1782 */     PreparedStatement ps = null;
/* 1783 */     ResultSet rs = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1811 */       dbcon = DbConnector.getItemDbCon();
/* 1812 */       ps = dbcon.prepareStatement(ItemDbStrings.getInstance().getCreatureItemsNonTransferred());
/* 1813 */       ps.setLong(1, creatureId);
/* 1814 */       rs = ps.executeQuery();
/* 1815 */       long iid = -10L;
/*      */       
/* 1817 */       while (rs.next())
/*      */       {
/* 1819 */         iid = rs.getLong("WURMID");
/* 1820 */         creatureItemIds.add(new Long(iid));
/*      */       }
/*      */     
/* 1823 */     } catch (SQLException sqx) {
/*      */       
/* 1825 */       logger.log(Level.WARNING, "Failed to load items for creature " + creatureId + " : " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1829 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1830 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/*      */     try {
/* 1834 */       dbcon = DbConnector.getItemDbCon();
/* 1835 */       ps = dbcon.prepareStatement(CoinDbStrings.getInstance().getCreatureItemsNonTransferred());
/* 1836 */       ps.setLong(1, creatureId);
/* 1837 */       rs = ps.executeQuery();
/* 1838 */       long iid = -10L;
/*      */       
/* 1840 */       while (rs.next())
/*      */       {
/* 1842 */         iid = rs.getLong("WURMID");
/* 1843 */         creatureItemIds.add(new Long(iid));
/*      */       }
/*      */     
/* 1846 */     } catch (SQLException sqx) {
/*      */       
/* 1848 */       logger.log(Level.WARNING, "Failed to load coin items for creature " + creatureId + " : " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1852 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1853 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/*      */     try {
/* 1857 */       dbcon = DbConnector.getItemDbCon();
/* 1858 */       ps = dbcon.prepareStatement(FrozenItemDbStrings.getInstance().getCreatureItemsNonTransferred());
/* 1859 */       ps.setLong(1, creatureId);
/* 1860 */       rs = ps.executeQuery();
/* 1861 */       long iid = -10L;
/*      */       
/* 1863 */       while (rs.next())
/*      */       {
/* 1865 */         iid = rs.getLong("WURMID");
/* 1866 */         creatureItemIds.add(new Long(iid));
/*      */       }
/*      */     
/* 1869 */     } catch (SQLException sqx) {
/*      */       
/* 1871 */       logger.log(Level.WARNING, "Failed to load frozen items for creature " + creatureId + " : " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1875 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1876 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 1878 */     return creatureItemIds;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void clearCreatureLoadMap() {
/* 1883 */     creatureItemsMap.clear();
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void loadAllCreatureItems() {
/* 1888 */     Connection dbcon = null;
/* 1889 */     PreparedStatement ps = null;
/* 1890 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1893 */       dbcon = DbConnector.getItemDbCon();
/* 1894 */       ps = dbcon.prepareStatement("SELECT * FROM ITEMS WHERE OWNERID&0xFF=1");
/* 1895 */       rs = ps.executeQuery();
/* 1896 */       long iid = -10L;
/*      */       
/* 1898 */       while (rs.next()) {
/*      */         
/* 1900 */         iid = rs.getLong("WURMID");
/*      */         try {
/*      */           DbItem dbItem;
/* 1903 */           ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(rs.getInt("TEMPLATEID"));
/* 1904 */           Item item = null;
/* 1905 */           boolean load = true;
/* 1906 */           if (temp.alwaysLoaded) {
/*      */             
/* 1908 */             load = false;
/*      */             
/*      */             try {
/* 1911 */               item = getItem(iid);
/* 1912 */               item.setOwnerStuff(temp);
/*      */             }
/* 1914 */             catch (NoSuchItemException nsi) {
/*      */               
/* 1916 */               load = true;
/*      */             } 
/*      */           } 
/* 1919 */           if (load)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1933 */             dbItem = new DbItem(iid, temp, rs.getString("NAME"), rs.getLong("LASTMAINTAINED"), rs.getFloat("QUALITYLEVEL"), rs.getFloat("ORIGINALQUALITYLEVEL"), rs.getInt("SIZEX"), rs.getInt("SIZEY"), rs.getInt("SIZEZ"), rs.getFloat("POSX"), rs.getFloat("POSY"), rs.getFloat("POSZ"), rs.getFloat("ROTATION"), rs.getLong("PARENTID"), rs.getLong("OWNERID"), rs.getInt("ZONEID"), rs.getFloat("DAMAGE"), rs.getInt("WEIGHT"), rs.getByte("MATERIAL"), rs.getLong("LOCKID"), rs.getShort("PLACE"), rs.getInt("PRICE"), rs.getShort("TEMPERATURE"), rs.getString("DESCRIPTION"), rs.getByte("BLESS"), rs.getByte("ENCHANT"), rs.getBoolean("BANKED"), rs.getLong("LASTOWNERID"), rs.getByte("AUXDATA"), rs.getLong("CREATIONDATE"), rs.getByte("CREATIONSTATE"), rs.getInt("REALTEMPLATE"), rs.getBoolean("WORNARMOUR"), rs.getInt("COLOR"), rs.getInt("COLOR2"), rs.getBoolean("FEMALE"), rs.getBoolean("MAILED"), rs.getBoolean("TRANSFERRED"), rs.getString("CREATOR"), rs.getBoolean("HIDDEN"), rs.getByte("MAILTIMES"), rs.getByte("RARITY"), rs.getLong("ONBRIDGE"), rs.getInt("SETTINGS"), rs.getBoolean("PLACEDONPARENT"), (DbStrings)ItemDbStrings.getInstance());
/*      */           }
/* 1935 */           long pid = dbItem.getParentId();
/* 1936 */           if (pid != -10L) {
/*      */             
/* 1938 */             Set<Item> contained = containedItems.get(Long.valueOf(pid));
/* 1939 */             if (contained == null)
/* 1940 */               contained = new HashSet<>(); 
/* 1941 */             contained.add(dbItem);
/* 1942 */             containedItems.put(Long.valueOf(pid), contained);
/*      */           } 
/* 1944 */           if (dbItem.getOwnerId() > 0L) {
/*      */             
/* 1946 */             Set<Item> contained = creatureItemsMap.get(Long.valueOf(dbItem.getOwnerId()));
/* 1947 */             if (contained == null)
/* 1948 */               contained = new HashSet<>(); 
/* 1949 */             contained.add(dbItem);
/* 1950 */             creatureItemsMap.put(Long.valueOf(dbItem.getOwnerId()), contained);
/*      */           } 
/* 1952 */           numItems++;
/*      */         }
/* 1954 */         catch (NoSuchTemplateException nst) {
/*      */           
/* 1956 */           logger.log(Level.WARNING, "Problem getting Template for item", (Throwable)nst);
/*      */         }
/*      */       
/*      */       } 
/* 1960 */     } catch (SQLException sqx) {
/*      */       
/* 1962 */       logger.log(Level.WARNING, "Failed to load items " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1966 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1967 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/*      */     try {
/* 1971 */       dbcon = DbConnector.getItemDbCon();
/* 1972 */       ps = dbcon.prepareStatement("SELECT * FROM COINS WHERE OWNERID&0xFF=1");
/* 1973 */       rs = ps.executeQuery();
/* 1974 */       long iid = -10L;
/*      */       
/* 1976 */       while (rs.next()) {
/*      */         
/* 1978 */         iid = rs.getLong("WURMID");
/*      */         try {
/*      */           DbItem dbItem;
/* 1981 */           ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(rs.getInt("TEMPLATEID"));
/* 1982 */           Item item = null;
/* 1983 */           boolean load = true;
/* 1984 */           if (temp.alwaysLoaded) {
/*      */             
/* 1986 */             load = false;
/*      */             
/*      */             try {
/* 1989 */               item = getItem(iid);
/* 1990 */               item.setOwnerStuff(temp);
/*      */             }
/* 1992 */             catch (NoSuchItemException nsi) {
/*      */               
/* 1994 */               load = true;
/*      */             } 
/*      */           } 
/* 1997 */           if (load)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2011 */             dbItem = new DbItem(iid, temp, rs.getString("NAME"), rs.getLong("LASTMAINTAINED"), rs.getFloat("QUALITYLEVEL"), rs.getFloat("ORIGINALQUALITYLEVEL"), rs.getInt("SIZEX"), rs.getInt("SIZEY"), rs.getInt("SIZEZ"), rs.getFloat("POSX"), rs.getFloat("POSY"), rs.getFloat("POSZ"), rs.getFloat("ROTATION"), rs.getLong("PARENTID"), rs.getLong("OWNERID"), rs.getInt("ZONEID"), rs.getFloat("DAMAGE"), rs.getInt("WEIGHT"), rs.getByte("MATERIAL"), rs.getLong("LOCKID"), rs.getShort("PLACE"), rs.getInt("PRICE"), rs.getShort("TEMPERATURE"), rs.getString("DESCRIPTION"), rs.getByte("BLESS"), rs.getByte("ENCHANT"), rs.getBoolean("BANKED"), rs.getLong("LASTOWNERID"), rs.getByte("AUXDATA"), rs.getLong("CREATIONDATE"), rs.getByte("CREATIONSTATE"), rs.getInt("REALTEMPLATE"), rs.getBoolean("WORNARMOUR"), rs.getInt("COLOR"), rs.getInt("COLOR2"), rs.getBoolean("FEMALE"), rs.getBoolean("MAILED"), rs.getBoolean("TRANSFERRED"), rs.getString("CREATOR"), rs.getBoolean("HIDDEN"), rs.getByte("MAILTIMES"), rs.getByte("RARITY"), rs.getLong("ONBRIDGE"), rs.getInt("SETTINGS"), rs.getBoolean("PLACEDONPARENT"), (DbStrings)CoinDbStrings.getInstance());
/*      */           }
/* 2013 */           long pid = dbItem.getParentId();
/* 2014 */           if (pid != -10L) {
/*      */             
/* 2016 */             Set<Item> contained = containedItems.get(Long.valueOf(pid));
/* 2017 */             if (contained == null)
/* 2018 */               contained = new HashSet<>(); 
/* 2019 */             contained.add(dbItem);
/* 2020 */             containedItems.put(Long.valueOf(pid), contained);
/*      */           } 
/* 2022 */           if (dbItem.getOwnerId() > 0L) {
/*      */             
/* 2024 */             Set<Item> contained = creatureItemsMap.get(Long.valueOf(dbItem.getOwnerId()));
/* 2025 */             if (contained == null)
/* 2026 */               contained = new HashSet<>(); 
/* 2027 */             contained.add(dbItem);
/* 2028 */             creatureItemsMap.put(Long.valueOf(dbItem.getOwnerId()), contained);
/*      */           } 
/* 2030 */           numCoins++;
/*      */         }
/* 2032 */         catch (NoSuchTemplateException nst) {
/*      */           
/* 2034 */           logger.log(Level.WARNING, "Problem getting Template for item", (Throwable)nst);
/*      */         }
/*      */       
/*      */       } 
/* 2038 */     } catch (SQLException sqx) {
/*      */       
/* 2040 */       logger.log(Level.WARNING, "Failed to load coins " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 2044 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 2045 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void loadAllItemsForNonPlayer(Creature creature, long inventoryId) {
/* 2051 */     long cpS = System.nanoTime();
/* 2052 */     if (logger.isLoggable(Level.FINEST))
/*      */     {
/* 2054 */       logger.finest("Loading items for creature " + creature.getWurmId());
/*      */     }
/* 2056 */     Set<Item> creatureItems = creatureItemsMap.get(Long.valueOf(creature.getWurmId()));
/*      */     
/* 2058 */     cpOne += System.nanoTime() - cpS;
/* 2059 */     cpS = System.nanoTime();
/*      */ 
/*      */ 
/*      */     
/* 2063 */     if (creatureItems != null) {
/* 2064 */       for (Item item : creatureItems)
/*      */       {
/* 2066 */         item.getContainedItems();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/* 2072 */       creature.loadPossessions(inventoryId);
/*      */     }
/* 2074 */     catch (Exception ex) {
/*      */       
/* 2076 */       logger.log(Level.WARNING, creature
/* 2077 */           .getName() + " failed to load possessions - inventory not found " + ex.getMessage(), ex);
/*      */     } 
/* 2079 */     cpThree += System.nanoTime() - cpS;
/* 2080 */     cpS = System.nanoTime();
/* 2081 */     if (creatureItems == null || creatureItems.size() == 0) {
/*      */       return;
/*      */     }
/*      */     
/* 2085 */     for (Item item : creatureItems) {
/*      */       
/* 2087 */       if (!item.isInventory() && !item.isBodyPart()) {
/*      */         
/*      */         try {
/*      */           
/* 2091 */           Item parent = item.getParent();
/* 2092 */           if (parent.isBodyPart()) {
/*      */             
/* 2094 */             if (!moveItemFromIncorrectSlot(item, parent, creature))
/*      */             {
/* 2096 */               if (!parent.getItems().contains(item) && 
/* 2097 */                 !parent.insertItem(item, false)) {
/*      */                 
/* 2099 */                 resetParentToInventory(item, creature);
/* 2100 */                 logger.log(Level.INFO, "INSERTED IN INVENTORY " + item
/* 2101 */                     .getName() + " for " + creature.getName() + " wid=" + item
/* 2102 */                     .getWurmId());
/*      */               } 
/*      */             }
/*      */             continue;
/*      */           } 
/* 2107 */           if (parent.isInventory() && !creature.isPlayer())
/*      */           {
/* 2109 */             if (creature.isHorse() || creature.getTemplate().isHellHorse() || creature
/* 2110 */               .getTemplate().isKingdomGuard()) {
/*      */               
/* 2112 */               byte[] spaces = item.getBodySpaces();
/* 2113 */               for (int i = 0; i < spaces.length; i++)
/*      */               {
/*      */                 
/*      */                 try {
/* 2117 */                   Item bp = creature.getBody().getBodyPart(spaces[i]);
/* 2118 */                   if (bp != null)
/*      */                   {
/* 2120 */                     if (bp.testInsertItem(item)) {
/*      */                       
/* 2122 */                       bp.insertItem(item);
/*      */ 
/*      */                       
/*      */                       break;
/*      */                     } 
/*      */                   }
/* 2128 */                 } catch (NoSpaceException nse) {
/*      */                   
/* 2130 */                   logger.log(Level.INFO, "Unable to find body part, inserting in inventory");
/* 2131 */                   resetParentToInventory(item, creature);
/*      */                 }
/*      */               
/*      */               }
/*      */             
/*      */             } 
/*      */           }
/* 2138 */         } catch (NoSuchItemException nsi) {
/*      */ 
/*      */           
/* 2141 */           if (creature.isHorse() || creature.getTemplate().isHellHorse() || creature
/* 2142 */             .getTemplate().isKingdomGuard()) {
/*      */             
/* 2144 */             byte[] spaces = item.getBodySpaces();
/* 2145 */             for (int i = 0; i < spaces.length; i++) {
/*      */ 
/*      */               
/*      */               try {
/* 2149 */                 Item bp = creature.getBody().getBodyPart(spaces[i]);
/* 2150 */                 if (bp != null)
/*      */                 {
/* 2152 */                   if (bp.testInsertItem(item)) {
/*      */                     
/* 2154 */                     bp.insertItem(item);
/*      */ 
/*      */                     
/*      */                     break;
/*      */                   } 
/*      */                 }
/* 2160 */               } catch (NoSpaceException nse) {
/*      */                 
/* 2162 */                 logger.log(Level.INFO, "Unable to find body part, inserting in inventory");
/* 2163 */                 resetParentToInventory(item, creature);
/*      */               } 
/*      */             } 
/*      */             
/*      */             continue;
/*      */           } 
/*      */           
/* 2170 */           logger.log(Level.INFO, "Unable to find parent slot, inserting in inventory");
/* 2171 */           resetParentToInventory(item, creature);
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 2176 */     cpFour += System.nanoTime() - cpS;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Set<Item> loadAllItemsForCreature(Creature creature, long inventoryId) {
/* 2181 */     long cpS = System.nanoTime();
/* 2182 */     if (logger.isLoggable(Level.FINEST))
/*      */     {
/* 2184 */       logger.finest("Loading items for creature " + creature.getWurmId());
/*      */     }
/* 2186 */     Set<Item> creatureItems = new HashSet<>();
/* 2187 */     if (creature.isPlayer())
/*      */     {
/* 2189 */       if (((Player)creature).getSaveFile().hasMovedInventory()) {
/*      */         
/* 2191 */         returnItemsFromFreezerFor(creature.getWurmId());
/* 2192 */         ((Player)creature).getSaveFile().setMovedInventory(false);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2199 */     loadAllItemsForCreatureAndItemtype(creature.getWurmId(), (DbStrings)CoinDbStrings.getInstance(), creatureItems);
/* 2200 */     cpOne += System.nanoTime() - cpS;
/* 2201 */     cpS = System.nanoTime();
/* 2202 */     loadAllItemsForCreatureAndItemtype(creature.getWurmId(), (DbStrings)ItemDbStrings.getInstance(), creatureItems);
/* 2203 */     cpTwo += System.nanoTime() - cpS;
/* 2204 */     cpS = System.nanoTime();
/* 2205 */     for (Item item : creatureItems)
/*      */     {
/* 2207 */       item.getContainedItems();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 2213 */       creature.loadPossessions(inventoryId);
/*      */     }
/* 2215 */     catch (Exception ex) {
/*      */       
/* 2217 */       logger.log(Level.WARNING, creature
/* 2218 */           .getName() + " failed to load possessions - inventory not found " + ex.getMessage(), ex);
/*      */     } 
/* 2220 */     cpThree += System.nanoTime() - cpS;
/* 2221 */     cpS = System.nanoTime();
/* 2222 */     for (Item item : creatureItems) {
/*      */       
/* 2224 */       if (!item.isInventory() && !item.isBodyPart()) {
/*      */         
/*      */         try {
/*      */           
/* 2228 */           Item parent = item.getParent();
/* 2229 */           if (parent.isBodyPart()) {
/*      */             
/* 2231 */             if (!moveItemFromIncorrectSlot(item, parent, creature))
/*      */             {
/* 2233 */               if (!parent.getItems().contains(item) && 
/* 2234 */                 !parent.insertItem(item, false)) {
/*      */                 
/* 2236 */                 resetParentToInventory(item, creature);
/* 2237 */                 logger.log(Level.INFO, "Inserted in inventory " + item
/* 2238 */                     .getName() + " for " + creature.getName() + " wid=" + item
/* 2239 */                     .getWurmId());
/*      */               } 
/*      */             }
/*      */             continue;
/*      */           } 
/* 2244 */           if (parent.isInventory() && !creature.isPlayer())
/*      */           {
/* 2246 */             if (creature.isHorse() || creature.getTemplate().isHellHorse() || creature
/* 2247 */               .getTemplate().isKingdomGuard()) {
/*      */               
/* 2249 */               byte[] spaces = item.getBodySpaces();
/* 2250 */               for (int i = 0; i < spaces.length; i++)
/*      */               {
/*      */                 
/*      */                 try {
/* 2254 */                   Item bp = creature.getBody().getBodyPart(spaces[i]);
/* 2255 */                   if (bp != null)
/*      */                   {
/* 2257 */                     if (bp.testInsertItem(item)) {
/*      */                       
/* 2259 */                       bp.insertItem(item);
/*      */ 
/*      */                       
/*      */                       break;
/*      */                     } 
/*      */                   }
/* 2265 */                 } catch (NoSpaceException nse) {
/*      */                   
/* 2267 */                   logger.log(Level.INFO, "Unable to find body part, inserting in inventory");
/* 2268 */                   resetParentToInventory(item, creature);
/*      */                 }
/*      */               
/*      */               }
/*      */             
/*      */             } 
/*      */           }
/* 2275 */         } catch (NoSuchItemException nsi) {
/*      */ 
/*      */           
/* 2278 */           if (creature.isHorse() || creature.getTemplate().isHellHorse() || creature
/* 2279 */             .getTemplate().isKingdomGuard()) {
/*      */             
/* 2281 */             byte[] spaces = item.getBodySpaces();
/* 2282 */             for (int i = 0; i < spaces.length; i++) {
/*      */ 
/*      */               
/*      */               try {
/* 2286 */                 Item bp = creature.getBody().getBodyPart(spaces[i]);
/* 2287 */                 if (bp != null)
/*      */                 {
/* 2289 */                   if (bp.testInsertItem(item)) {
/*      */                     
/* 2291 */                     bp.insertItem(item);
/*      */ 
/*      */                     
/*      */                     break;
/*      */                   } 
/*      */                 }
/* 2297 */               } catch (NoSpaceException nse) {
/*      */                 
/* 2299 */                 logger.log(Level.INFO, "Unable to find body part, inserting in inventory");
/* 2300 */                 resetParentToInventory(item, creature);
/*      */               } 
/*      */             } 
/*      */             
/*      */             continue;
/*      */           } 
/*      */           
/* 2307 */           logger.log(Level.INFO, "Unable to find parent slot, inserting in inventory");
/* 2308 */           resetParentToInventory(item, creature);
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 2313 */     cpFour += System.nanoTime() - cpS;
/* 2314 */     cpS = System.nanoTime();
/* 2315 */     return creatureItems;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean moveItemFromIncorrectSlot(Item item, Item parent, Creature creature) {
/* 2324 */     if (!creature.isHuman() || item.isBodyPart() || item.isEquipmentSlot())
/* 2325 */       return false; 
/* 2326 */     if (item.isBelt()) {
/*      */       
/* 2328 */       if (parent.isEquipmentSlot())
/*      */       {
/* 2330 */         if (parent.getAuxData() == 22) {
/* 2331 */           return false;
/*      */         }
/*      */       }
/* 2334 */       if (parent.isBodyPart()) {
/*      */         
/* 2336 */         resetParentToInventory(item, creature);
/* 2337 */         return true;
/*      */       } 
/*      */     } else {
/* 2340 */       if (parent.isBodyPart() && item.isInventoryGroup()) {
/*      */         
/* 2342 */         resetParentToInventory(item, creature);
/* 2343 */         return true;
/*      */       } 
/* 2345 */       if (parent.isBodyPart() && !parent.isEquipmentSlot()) {
/*      */         
/* 2347 */         if (item.isArmour())
/*      */         {
/* 2349 */           return false;
/*      */         }
/* 2351 */         if (item.getTemplateId() == 231)
/*      */         {
/* 2353 */           return false;
/*      */         }
/* 2355 */         if (!creature.isPlayer() && item.isWeapon())
/*      */         {
/* 2357 */           return false;
/*      */         }
/*      */ 
/*      */         
/* 2361 */         resetParentToInventory(item, creature);
/* 2362 */         return true;
/*      */       } 
/*      */     } 
/* 2365 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void resetParentToInventory(Item item, Creature creature) {
/* 2370 */     Set<Item> contained = containedItems.get(new Long(item.getParentId()));
/* 2371 */     if (contained != null) {
/*      */       
/* 2373 */       boolean found = contained.remove(item);
/* 2374 */       if (found) {
/* 2375 */         logger.log(Level.INFO, "Success! removed the " + item.getName());
/*      */       }
/*      */     } 
/*      */     try {
/* 2379 */       creature.getInventory().insertItem(item, false);
/* 2380 */       logger.log(Level.INFO, "Inventory id: " + creature
/* 2381 */           .getInventory().getWurmId() + ", item parent now: " + item.getParentId() + " owner id=" + creature
/* 2382 */           .getWurmId() + " item owner id=" + item.getOwnerId());
/*      */       
/*      */       try {
/* 2385 */         Item itemorig = getItem(item.getWurmId());
/* 2386 */         logger.log(Level.INFO, "Inventory id: " + creature.getInventory().getWurmId() + ", item parent now: " + itemorig
/* 2387 */             .getParentId() + " owner id=" + itemorig.getOwnerId());
/*      */       }
/* 2389 */       catch (Exception ex) {
/*      */         
/* 2391 */         logger.log(Level.WARNING, "retrieval failed", ex);
/*      */       }
/*      */     
/* 2394 */     } catch (Exception ex) {
/*      */       
/* 2396 */       logger.log(Level.WARNING, "Inserting " + item + " into inventory instead for creature " + creature.getWurmId() + " failed: " + ex
/* 2397 */           .getMessage(), ex);
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
/*      */   public static Set<Item> loadAllItemsForCreatureWithId(long creatureId, boolean frozen) {
/* 2410 */     Set<Item> creatureItems = new HashSet<>();
/*      */     
/* 2412 */     loadAllItemsForCreatureAndItemtype(creatureId, (DbStrings)CoinDbStrings.getInstance(), creatureItems);
/* 2413 */     if (frozen) {
/* 2414 */       loadAllItemsForCreatureAndItemtype(creatureId, (DbStrings)FrozenItemDbStrings.getInstance(), creatureItems);
/*      */     } else {
/* 2416 */       loadAllItemsForCreatureAndItemtype(creatureId, (DbStrings)ItemDbStrings.getInstance(), creatureItems);
/*      */     } 
/*      */ 
/*      */     
/* 2420 */     return creatureItems;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void loadAllItemsForCreatureAndItemtype(long creatureId, DbStrings dbstrings, Set<Item> creatureItems) {
/* 2429 */     Connection dbcon = null;
/* 2430 */     PreparedStatement ps = null;
/* 2431 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 2434 */       dbcon = DbConnector.getItemDbCon();
/* 2435 */       ps = dbcon.prepareStatement(dbstrings.getCreatureItems());
/* 2436 */       ps.setLong(1, creatureId);
/* 2437 */       rs = ps.executeQuery();
/* 2438 */       long iid = -10L;
/*      */       
/* 2440 */       while (rs.next()) {
/*      */         
/* 2442 */         iid = rs.getLong("WURMID");
/*      */         try {
/*      */           DbItem dbItem;
/* 2445 */           ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(rs.getInt("TEMPLATEID"));
/* 2446 */           Item item = null;
/* 2447 */           boolean load = true;
/* 2448 */           if (temp.alwaysLoaded) {
/*      */             
/* 2450 */             load = false;
/*      */             
/*      */             try {
/* 2453 */               item = getItem(iid);
/* 2454 */               item.setOwnerStuff(temp);
/*      */             }
/* 2456 */             catch (NoSuchItemException nsi) {
/*      */               
/* 2458 */               load = true;
/*      */             } 
/*      */           } 
/* 2461 */           if (load)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2475 */             dbItem = new DbItem(iid, temp, rs.getString("NAME"), rs.getLong("LASTMAINTAINED"), rs.getFloat("QUALITYLEVEL"), rs.getFloat("ORIGINALQUALITYLEVEL"), rs.getInt("SIZEX"), rs.getInt("SIZEY"), rs.getInt("SIZEZ"), rs.getFloat("POSX"), rs.getFloat("POSY"), rs.getFloat("POSZ"), rs.getFloat("ROTATION"), rs.getLong("PARENTID"), rs.getLong("OWNERID"), rs.getInt("ZONEID"), rs.getFloat("DAMAGE"), rs.getInt("WEIGHT"), rs.getByte("MATERIAL"), rs.getLong("LOCKID"), rs.getShort("PLACE"), rs.getInt("PRICE"), rs.getShort("TEMPERATURE"), rs.getString("DESCRIPTION"), rs.getByte("BLESS"), rs.getByte("ENCHANT"), rs.getBoolean("BANKED"), rs.getLong("LASTOWNERID"), rs.getByte("AUXDATA"), rs.getLong("CREATIONDATE"), rs.getByte("CREATIONSTATE"), rs.getInt("REALTEMPLATE"), rs.getBoolean("WORNARMOUR"), rs.getInt("COLOR"), rs.getInt("COLOR2"), rs.getBoolean("FEMALE"), rs.getBoolean("MAILED"), rs.getBoolean("TRANSFERRED"), rs.getString("CREATOR"), rs.getBoolean("HIDDEN"), rs.getByte("MAILTIMES"), rs.getByte("RARITY"), rs.getLong("ONBRIDGE"), rs.getInt("SETTINGS"), rs.getBoolean("PLACEDONPARENT"), dbstrings);
/*      */           }
/* 2477 */           long pid = dbItem.getParentId();
/* 2478 */           if (pid != -10L) {
/*      */             
/* 2480 */             Set<Item> contained = containedItems.get(new Long(pid));
/* 2481 */             if (contained == null)
/* 2482 */               contained = new HashSet<>(); 
/* 2483 */             contained.add(dbItem);
/* 2484 */             containedItems.put(new Long(pid), contained);
/*      */           } 
/* 2486 */           creatureItems.add(dbItem);
/*      */         }
/* 2488 */         catch (NoSuchTemplateException nst) {
/*      */           
/* 2490 */           logger.log(Level.WARNING, "Problem getting Template for item with Wurm ID " + iid + "  for creature " + creatureId + " - " + nst
/* 2491 */               .getMessage(), (Throwable)nst);
/*      */         }
/*      */       
/*      */       } 
/* 2495 */     } catch (SQLException sqx) {
/*      */       
/* 2497 */       logger.log(Level.WARNING, "Failed to load items for creature " + creatureId + ": " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 2501 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 2502 */       DbConnector.returnConnection(dbcon);
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
/*      */   public static Set<Item> getContainedItems(long id) {
/* 2515 */     Long lid = new Long(id);
/* 2516 */     return containedItems.remove(lid);
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
/*      */   static void loadAllStaticItems() {
/* 2528 */     logger.log(Level.INFO, "Loading all static preloaded items");
/* 2529 */     long start = System.nanoTime();
/* 2530 */     ItemTemplate[] templates = ItemTemplateFactory.getInstance().getTemplates();
/* 2531 */     for (ItemTemplate lTemplate : templates) {
/*      */       
/* 2533 */       if (lTemplate.alwaysLoaded) {
/* 2534 */         loadAllStaticItems(lTemplate.getTemplateId());
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2542 */     float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 2543 */     logger.log(Level.INFO, "Loaded all static preloaded items, that took " + lElapsedTime + " ms");
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
/*      */   private static void loadAllStaticItems(int templateId) {
/* 2555 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 2557 */       logger.finer("Loading all static items for template ID " + templateId);
/*      */     }
/* 2559 */     DbStrings dbstrings = Item.getDbStrings(templateId);
/*      */ 
/*      */     
/* 2562 */     long iid = -10L;
/* 2563 */     Connection dbcon = null;
/* 2564 */     PreparedStatement ps = null;
/* 2565 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 2568 */       dbcon = DbConnector.getItemDbCon();
/* 2569 */       ps = dbcon.prepareStatement(dbstrings.getPreloadedItems());
/* 2570 */       ps.setInt(1, templateId);
/* 2571 */       rs = ps.executeQuery();
/* 2572 */       ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(templateId);
/*      */       
/* 2574 */       while (rs.next())
/*      */       {
/* 2576 */         iid = rs.getLong("WURMID");
/* 2577 */         new DbItem(iid, temp, rs.getString("NAME"), rs.getLong("LASTMAINTAINED"), rs
/* 2578 */             .getFloat("QUALITYLEVEL"), rs
/* 2579 */             .getFloat("ORIGINALQUALITYLEVEL"), rs.getInt("SIZEX"), rs.getInt("SIZEY"), rs.getInt("SIZEZ"), rs
/* 2580 */             .getFloat("POSX"), rs.getFloat("POSY"), rs.getFloat("POSZ"), rs.getFloat("ROTATION"), rs
/* 2581 */             .getLong("PARENTID"), rs.getLong("OWNERID"), rs.getInt("ZONEID"), rs.getFloat("DAMAGE"), rs
/* 2582 */             .getInt("WEIGHT"), rs.getByte("MATERIAL"), rs.getLong("LOCKID"), rs.getShort("PLACE"), rs
/* 2583 */             .getInt("PRICE"), rs.getShort("TEMPERATURE"), rs.getString("DESCRIPTION"), rs.getByte("BLESS"), rs
/* 2584 */             .getByte("ENCHANT"), rs.getBoolean("BANKED"), rs.getLong("LASTOWNERID"), rs.getByte("AUXDATA"), rs
/* 2585 */             .getLong("CREATIONDATE"), rs.getByte("CREATIONSTATE"), rs.getInt("REALTEMPLATE"), rs
/* 2586 */             .getBoolean("WORNARMOUR"), rs.getInt("COLOR"), rs.getInt("COLOR2"), rs.getBoolean("FEMALE"), rs
/* 2587 */             .getBoolean("MAILED"), rs.getBoolean("TRANSFERRED"), rs.getString("CREATOR"), rs.getBoolean("HIDDEN"), rs
/* 2588 */             .getByte("MAILTIMES"), rs.getByte("RARITY"), rs.getLong("ONBRIDGE"), rs.getInt("SETTINGS"), rs
/* 2589 */             .getBoolean("PLACEDONPARENT"), dbstrings);
/*      */       }
/*      */     
/*      */     }
/* 2593 */     catch (NoSuchTemplateException nst) {
/*      */       
/* 2595 */       logger.log(Level.WARNING, "Problem getting Template ID " + templateId + " - " + nst.getMessage(), (Throwable)nst);
/*      */     }
/* 2597 */     catch (SQLException sqx) {
/*      */       
/* 2599 */       logger.log(Level.WARNING, "Failed to load items for template " + templateId + " : " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 2603 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 2604 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 2606 */     if (dbstrings == ItemDbStrings.getInstance()) {
/*      */ 
/*      */       
/* 2609 */       FrozenItemDbStrings frozenItemDbStrings = FrozenItemDbStrings.getInstance();
/* 2610 */       iid = -10L;
/*      */       
/*      */       try {
/* 2613 */         dbcon = DbConnector.getItemDbCon();
/* 2614 */         ps = dbcon.prepareStatement(frozenItemDbStrings.getPreloadedItems());
/* 2615 */         ps.setInt(1, templateId);
/* 2616 */         rs = ps.executeQuery();
/* 2617 */         ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(templateId);
/*      */         
/* 2619 */         while (rs.next())
/*      */         {
/* 2621 */           iid = rs.getLong("WURMID");
/* 2622 */           new DbItem(iid, temp, rs.getString("NAME"), rs.getLong("LASTMAINTAINED"), rs.getFloat("QUALITYLEVEL"), rs
/* 2623 */               .getFloat("ORIGINALQUALITYLEVEL"), rs.getInt("SIZEX"), rs.getInt("SIZEY"), rs.getInt("SIZEZ"), rs
/* 2624 */               .getFloat("POSX"), rs.getFloat("POSY"), rs.getFloat("POSZ"), rs.getFloat("ROTATION"), rs
/* 2625 */               .getLong("PARENTID"), rs.getLong("OWNERID"), rs.getInt("ZONEID"), rs.getFloat("DAMAGE"), rs
/* 2626 */               .getInt("WEIGHT"), rs.getByte("MATERIAL"), rs.getLong("LOCKID"), rs.getShort("PLACE"), rs
/* 2627 */               .getInt("PRICE"), rs.getShort("TEMPERATURE"), rs.getString("DESCRIPTION"), rs.getByte("BLESS"), rs
/* 2628 */               .getByte("ENCHANT"), rs.getBoolean("BANKED"), rs.getLong("LASTOWNERID"), rs.getByte("AUXDATA"), rs
/* 2629 */               .getLong("CREATIONDATE"), rs.getByte("CREATIONSTATE"), rs.getInt("REALTEMPLATE"), rs
/* 2630 */               .getBoolean("WORNARMOUR"), rs.getInt("COLOR"), rs.getInt("COLOR2"), rs.getBoolean("FEMALE"), rs
/* 2631 */               .getBoolean("MAILED"), rs.getBoolean("TRANSFERRED"), rs.getString("CREATOR"), rs.getBoolean("HIDDEN"), rs
/* 2632 */               .getByte("MAILTIMES"), rs.getByte("RARITY"), rs.getLong("ONBRIDGE"), rs.getInt("SETTINGS"), rs
/* 2633 */               .getBoolean("PLACEDONPARENT"), (DbStrings)frozenItemDbStrings);
/*      */         }
/*      */       
/* 2636 */       } catch (NoSuchTemplateException nst) {
/*      */         
/* 2638 */         logger.log(Level.WARNING, "Problem getting Template ID " + templateId + " - " + nst.getMessage(), (Throwable)nst);
/*      */       }
/* 2640 */       catch (SQLException sqx) {
/*      */         
/* 2642 */         logger.log(Level.WARNING, "Failed to load frozen items for template " + templateId + " : " + sqx.getMessage(), sqx);
/*      */       
/*      */       }
/*      */       finally {
/*      */         
/* 2647 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 2648 */         DbConnector.returnConnection(dbcon);
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
/*      */   static void deleteSpawnPoints() {
/* 2662 */     Item[] _items = getAllItems();
/* 2663 */     int dist = 0;
/* 2664 */     for (Item lItem : _items) {
/*      */       
/* 2666 */       if (lItem.getTemplateId() == 521) {
/*      */         
/* 2668 */         dist = ((int)lItem.getPosX() >> 2) - ((int)lItem.getPosY() >> 2);
/* 2669 */         if (dist < 2 && dist > -2) {
/* 2670 */           destroyItem(lItem.getWurmId());
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void transferRegaliaForKingdom(byte kingdom, long newOwnerId) {
/* 2677 */     if (Kingdoms.getKingdom(kingdom) != null && Kingdoms.getKingdom(kingdom).isCustomKingdom()) {
/*      */       
/* 2679 */       Item[] _items = getAllItems();
/* 2680 */       for (Item lItem : _items) {
/*      */         
/* 2682 */         if (lItem.isRoyal())
/*      */         {
/* 2684 */           if (lItem.getOwnerId() != -10L)
/*      */           {
/* 2686 */             if (lItem.getKingdom() == kingdom)
/*      */             {
/* 2688 */               if (lItem.getOwnerId() != newOwnerId) {
/*      */                 
/* 2690 */                 lItem.putInVoid();
/* 2691 */                 lItem.setTransferred(false);
/* 2692 */                 lItem.setBanked(false);
/*      */                 
/*      */                 try {
/* 2695 */                   Creature c = Server.getInstance().getCreature(newOwnerId);
/* 2696 */                   c.getInventory().insertItem(lItem);
/*      */                 }
/* 2698 */                 catch (Exception ex) {
/*      */                   
/* 2700 */                   logger.log(Level.WARNING, ex.getMessage());
/*      */                 } 
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
/*      */   public static void deleteRoyalItemForKingdom(byte kingdom, boolean onSurface, boolean destroy) {
/* 2720 */     boolean putOnGround = Kingdoms.getKingdom(kingdom).isCustomKingdom();
/* 2721 */     Item[] _items = getAllItems();
/* 2722 */     for (Item lItem : _items) {
/*      */       
/* 2724 */       if (lItem.isRoyal())
/*      */       {
/* 2726 */         if (kingdom == 2 && lItem.getTemplateId() == 538) {
/* 2727 */           lItem.updatePos();
/* 2728 */         } else if (lItem.getKingdom() == kingdom) {
/*      */           
/* 2730 */           if (!destroy && putOnGround) {
/*      */             
/* 2732 */             int tilex = lItem.getTileX();
/* 2733 */             int tiley = lItem.getTileY();
/*      */             
/*      */             try {
/* 2736 */               Creature owner = Server.getInstance().getCreature(lItem.getOwnerId());
/* 2737 */               tilex = owner.getTileX();
/* 2738 */               tiley = owner.getTileY();
/* 2739 */               lItem.setPosXY(owner.getPosX(), owner.getPosY());
/* 2740 */               owner.getCommunicator().sendAlertServerMessage("As a sign of abdication, you put the " + lItem.getName() + " at your feet.");
/*      */             }
/* 2742 */             catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             try {
/* 2748 */               Zone z = Zones.getZone(tilex, tiley, onSurface);
/* 2749 */               lItem.putInVoid();
/* 2750 */               lItem.setTransferred(false);
/* 2751 */               lItem.setBanked(false);
/* 2752 */               z.addItem(lItem);
/*      */             }
/* 2754 */             catch (NoSuchZoneException nsz) {
/*      */               
/* 2756 */               logger.log(Level.WARNING, nsz.getMessage(), (Throwable)nsz);
/*      */             } 
/*      */           } else {
/*      */             
/* 2760 */             destroyItem(lItem.getWurmId());
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
/*      */   public static void deleteChristmasItems() {
/* 2774 */     Item[] lItems = getAllItems();
/* 2775 */     for (Item lLItem : lItems) {
/*      */       
/* 2777 */       if (lLItem.getTemplateId() == 442) {
/* 2778 */         destroyItem(lLItem.getWurmId());
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public static final void loadAllProtectedItems() {
/* 2784 */     long start = System.nanoTime();
/* 2785 */     Connection dbcon = null;
/* 2786 */     PreparedStatement ps = null;
/* 2787 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 2790 */       dbcon = DbConnector.getItemDbCon();
/* 2791 */       ps = dbcon.prepareStatement("SELECT * FROM PROTECTEDCORPSES");
/* 2792 */       rs = ps.executeQuery();
/* 2793 */       int num = 0;
/* 2794 */       while (rs.next()) {
/*      */         
/* 2796 */         protectedCorpses.add(Long.valueOf(rs.getLong("WURMID")));
/* 2797 */         num++;
/*      */       } 
/* 2799 */       float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 2800 */       logger.log(Level.INFO, "Loaded " + num + " protected corpse entries, that took " + lElapsedTime + " ms");
/*      */     }
/* 2802 */     catch (SQLException sqx) {
/*      */       
/* 2804 */       logger.log(Level.WARNING, "Failed to load protected corpses: " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 2808 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 2809 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 2811 */     loadedCorpses = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isProtected(Item item) {
/* 2816 */     return protectedCorpses.contains(Long.valueOf(item.getWurmId()));
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void setProtected(long wurmid, boolean isProtected) {
/* 2821 */     if (!isProtected) {
/*      */       
/* 2823 */       if (protectedCorpses.remove(Long.valueOf(wurmid))) {
/*      */         
/* 2825 */         Connection dbcon = null;
/* 2826 */         PreparedStatement ps = null;
/*      */         
/*      */         try {
/* 2829 */           dbcon = DbConnector.getItemDbCon();
/* 2830 */           ps = dbcon.prepareStatement("DELETE FROM PROTECTEDCORPSES WHERE WURMID=?");
/* 2831 */           ps.setLong(1, wurmid);
/* 2832 */           ps.execute();
/*      */         }
/* 2834 */         catch (SQLException sqx) {
/*      */           
/* 2836 */           logger.log(Level.WARNING, "Failed to set protected false " + wurmid + " : " + sqx.getMessage(), sqx);
/*      */         }
/*      */         finally {
/*      */           
/* 2840 */           DbUtilities.closeDatabaseObjects(ps, null);
/* 2841 */           DbConnector.returnConnection(dbcon);
/*      */         }
/*      */       
/*      */       } 
/* 2845 */     } else if (!protectedCorpses.contains(Long.valueOf(wurmid))) {
/*      */       
/* 2847 */       protectedCorpses.add(Long.valueOf(wurmid));
/* 2848 */       Connection dbcon = null;
/* 2849 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2852 */         dbcon = DbConnector.getItemDbCon();
/* 2853 */         ps = dbcon.prepareStatement("INSERT INTO PROTECTEDCORPSES(WURMID)VALUES(?)");
/* 2854 */         ps.setLong(1, wurmid);
/* 2855 */         ps.execute();
/*      */       }
/* 2857 */       catch (SQLException sqx) {
/*      */         
/* 2859 */         logger.log(Level.WARNING, "Failed to add protected " + wurmid + " : " + sqx.getMessage(), sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 2863 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2864 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int getBattleCampControl(byte kingdom) {
/* 2871 */     int nums = 0;
/* 2872 */     for (Item item : getWarTargets()) {
/*      */       
/* 2874 */       if (item.getKingdom() == kingdom)
/* 2875 */         nums++; 
/*      */     } 
/* 2877 */     return nums;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Item findMerchantContractFromId(long contractId) {
/* 2882 */     for (Item item : getAllItems()) {
/*      */       
/* 2884 */       if (item.getTemplateId() == 300)
/*      */       {
/* 2886 */         if (item.getData() == contractId)
/*      */         {
/* 2888 */           return item;
/*      */         }
/*      */       }
/*      */     } 
/* 2892 */     return null;
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
/*      */   static class EggCounter
/*      */     implements Runnable
/*      */   {
/*      */     public void run() {
/* 2924 */       if (Items.logger.isLoggable(Level.FINE))
/*      */       {
/* 2926 */         Items.logger.fine("Running newSingleThreadScheduledExecutor for calling Items.countEggs()");
/*      */       }
/*      */       
/*      */       try {
/* 2930 */         long start = System.nanoTime();
/*      */         
/* 2932 */         Items.countEggs();
/*      */         
/* 2934 */         float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 2935 */         if (lElapsedTime > (float)Constants.lagThreshold)
/*      */         {
/* 2937 */           Items.logger.info("Finished calling Items.countEggs(), which took " + lElapsedTime + " millis.");
/*      */         
/*      */         }
/*      */       }
/* 2941 */       catch (RuntimeException e) {
/*      */         
/* 2943 */         Items.logger.log(Level.WARNING, "Caught exception in ScheduledExecutorService while calling Items.countEggs()", e);
/* 2944 */         throw e;
/*      */       } 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\Items.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */