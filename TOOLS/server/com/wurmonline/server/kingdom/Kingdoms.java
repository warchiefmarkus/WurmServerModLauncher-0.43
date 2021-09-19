/*     */ package com.wurmonline.server.kingdom;
/*     */ 
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ 
/*     */ 
/*     */ public final class Kingdoms
/*     */   implements MiscConstants
/*     */ {
/*     */   public static final String KINGDOM_NAME_JENN = "Jenn-Kellon";
/*     */   public static final String KINGDOM_CHAT_JENN = "Jenn-Kellon";
/*     */   public static final String KINGDOM_NAME_MOLREHAN = "Mol Rehan";
/*     */   public static final String KINGDOM_CHAT_MOLREHAN = "Mol Rehan";
/*     */   public static final String KINGDOM_NAME_LIBILA = "Horde of the Summoned";
/*     */   public static final String KINGDOM_CHAT_HOTS = "HOTS";
/*     */   public static final String KINGDOM_NAME_FREEDOM = "Freedom Isles";
/*     */   public static final String KINGDOM_CHAT_FREEDOM = "Freedom";
/*     */   public static final String KINGDOM_NAME_NONE = "no known kingdom";
/*     */   public static final String KINGDOM_SUFFIX_JENN = "jenn.";
/*     */   public static final String KINGDOM_SUFFIX_MOLREHAN = "molr.";
/*     */   public static final String KINGDOM_SUFFIX_HOTS = "hots.";
/*     */   public static final String KINGDOM_SUFFIX_FREEDOM = "free.";
/*     */   public static final String KINGDOM_SUFFIX_NONE = "";
/*  66 */   public static int activePremiumJenn = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   public static int activePremiumMolr = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   public static int activePremiumHots = 0;
/*     */   
/*     */   public static final int TOWER_INFLUENCE = 60;
/*     */   public static final int CHALLENGE_ITEM_INFLUENCE = 20;
/*  84 */   public static final int minKingdomDist = Servers.localServer.isChallengeServer() ? 60 : ((Servers.localServer.id == 3) ? 100 : 150);
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int maxTowerDistance = 100;
/*     */ 
/*     */ 
/*     */   
/*  92 */   private static final Map<Byte, Kingdom> kingdoms = new HashMap<>();
/*     */   
/*  94 */   private static Logger logger = Logger.getLogger(Kingdoms.class.getName());
/*  95 */   private static final ConcurrentHashMap<Item, GuardTower> towers = new ConcurrentHashMap<>();
/*     */   
/*     */   public static final int minOwnTowerDistance = 50;
/*     */   public static final int minArcheryTowerDistance = 20;
/*     */   
/*     */   public static final void createBasicKingdoms() {
/* 101 */     addKingdom(new Kingdom((byte)0, (byte)0, "no known kingdom", "abofk7ba", "none", "", "Unknown", "Unknown", false));
/*     */     
/* 103 */     addKingdom(new Kingdom((byte)1, (byte)1, "Jenn-Kellon", "abosdsd", "Jenn-Kellon", "jenn.", "Noble", "Protectors", true));
/*     */     
/* 105 */     addKingdom(new Kingdom((byte)2, (byte)2, "Mol Rehan", "ajajkjh3d", "Mol Rehan", "molr.", "Fire", "Gold", true));
/*     */     
/* 107 */     addKingdom(new Kingdom((byte)3, (byte)3, "Horde of the Summoned", "11dfkjutyd", "HOTS", "hots.", "Hate", "Vengeance", true));
/*     */     
/* 109 */     addKingdom(new Kingdom((byte)4, (byte)4, "Freedom Isles", "asiuytsr", "Freedom", "free.", "Peaceful", "Friendly", true));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final int numKingdoms() {
/* 115 */     return kingdoms.size();
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
/*     */ 
/*     */ 
/*     */   
/*     */   static int getActivePremiumJenn() {
/* 133 */     return activePremiumJenn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void setActivePremiumJenn(int aActivePremiumJenn) {
/* 144 */     activePremiumJenn = aActivePremiumJenn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int getActivePremiumMolr() {
/* 154 */     return activePremiumMolr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void setActivePremiumMolr(int aActivePremiumMolr) {
/* 165 */     activePremiumMolr = aActivePremiumMolr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int getActivePremiumHots() {
/* 175 */     return activePremiumHots;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void setActivePremiumHots(int aActivePremiumHots) {
/* 186 */     activePremiumHots = aActivePremiumHots;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getNameFor(byte kingdom) {
/* 196 */     Kingdom k = getKingdomOrNull(kingdom);
/* 197 */     if (k != null)
/* 198 */       return k.getName(); 
/* 199 */     return "no known kingdom";
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean isKingdomChat(String chatTitle) {
/* 204 */     for (Kingdom k : kingdoms.values()) {
/*     */       
/* 206 */       if (k.getChatName().equals(chatTitle))
/* 207 */         return true; 
/*     */     } 
/* 209 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean isGlobalKingdomChat(String chatTitle) {
/* 214 */     for (Kingdom k : kingdoms.values()) {
/*     */       
/* 216 */       if (("GL-" + k.getChatName()).equals(chatTitle))
/* 217 */         return true; 
/*     */     } 
/* 219 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean mayCreateKingdom() {
/* 224 */     return (kingdoms.size() < 255);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Kingdom getKingdomWithName(String kname) {
/* 229 */     for (Kingdom k : kingdoms.values()) {
/*     */       
/* 231 */       if (k.getName().equalsIgnoreCase(kname))
/* 232 */         return k; 
/*     */     } 
/* 234 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Kingdom getKingdomWithChatTitle(String chatTitle) {
/* 239 */     for (Kingdom k : kingdoms.values()) {
/*     */       
/* 241 */       if (k.getChatName().equals(chatTitle))
/* 242 */         return k; 
/*     */     } 
/* 244 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Kingdom getKingdomWithSuffix(String suffix) {
/* 249 */     for (Kingdom k : kingdoms.values()) {
/*     */       
/* 251 */       if (k.getSuffix().equals(suffix))
/* 252 */         return k; 
/*     */     } 
/* 254 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void loadKingdom(Kingdom kingdom) {
/* 259 */     Kingdom oldk = kingdoms.get(Byte.valueOf(kingdom.kingdomId));
/* 260 */     if (oldk != null) {
/* 261 */       kingdom.setAlliances(oldk.getAllianceMap());
/*     */     }
/* 263 */     kingdoms.put(Byte.valueOf(kingdom.kingdomId), kingdom);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean addKingdom(Kingdom kingdom) {
/* 268 */     boolean isNew = false;
/* 269 */     boolean exists = false;
/* 270 */     Kingdom oldk = kingdoms.get(Byte.valueOf(kingdom.kingdomId));
/*     */     
/* 272 */     if (oldk != null) {
/*     */       
/* 274 */       exists = true;
/* 275 */       kingdom.setAlliances(oldk.getAllianceMap());
/* 276 */       kingdom.setExistsHere(oldk.existsHere());
/* 277 */       kingdom.activePremiums = oldk.activePremiums;
/* 278 */       if (oldk.acceptsTransfers() != kingdom.acceptsTransfers() || 
/* 279 */         !oldk.getFirstMotto().equals(kingdom.getFirstMotto()) || 
/* 280 */         !oldk.getSecondMotto().equals(kingdom.getSecondMotto()) || 
/* 281 */         !oldk.getPassword().equals(kingdom.getPassword())) {
/* 282 */         kingdom.update();
/*     */       }
/*     */     } else {
/* 285 */       isNew = true;
/* 286 */     }  kingdoms.put(Byte.valueOf(kingdom.kingdomId), kingdom);
/* 287 */     if (isNew)
/*     */     {
/* 289 */       Players.getInstance().sendKingdomToPlayers(kingdom);
/*     */     }
/* 291 */     kingdom.setShouldBeDeleted(false);
/* 292 */     if (!exists)
/* 293 */       kingdom.saveToDisk(); 
/* 294 */     return isNew;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void markAllKingdomsForDeletion() {
/* 299 */     Kingdom[] allKingdoms = getAllKingdoms();
/* 300 */     for (Kingdom k : allKingdoms)
/*     */     {
/* 302 */       k.setShouldBeDeleted(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void trimKingdoms() {
/* 308 */     Kingdom[] allKingdoms = getAllKingdoms();
/* 309 */     for (Kingdom k : allKingdoms) {
/*     */       
/* 311 */       if (k.isShouldBeDeleted()) {
/*     */         
/* 313 */         k.delete();
/* 314 */         removeKingdom(k.getId());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void removeKingdom(byte id) {
/* 321 */     King.purgeKing(id);
/* 322 */     kingdoms.remove(Byte.valueOf(id));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Kingdom getKingdomOrNull(byte id) {
/* 327 */     return kingdoms.get(Byte.valueOf(id));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Kingdom getKingdom(byte id) {
/* 332 */     Kingdom toret = kingdoms.get(Byte.valueOf(id));
/* 333 */     if (toret == null)
/* 334 */       return kingdoms.get(Byte.valueOf((byte)0)); 
/* 335 */     return toret;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final byte getKingdomTemplateFor(byte id) {
/* 340 */     Kingdom toret = kingdoms.get(Byte.valueOf(id));
/* 341 */     if (toret == null)
/* 342 */       return 0; 
/* 343 */     return toret.getTemplate();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Kingdom[] getAllKingdoms() {
/* 348 */     return (Kingdom[])kingdoms.values().toArray((Object[])new Kingdom[kingdoms.values().size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ConcurrentHashMap<Item, GuardTower> getTowers() {
/* 353 */     return towers;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final byte getNextAvailableKingdomId() {
/* 358 */     for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b = (byte)(b + 1)) {
/*     */       
/* 360 */       if (b < 0 || b > 4)
/*     */       {
/* 362 */         if (kingdoms.get(Byte.valueOf(b)) == null)
/* 363 */           return b; 
/*     */       }
/*     */     } 
/* 366 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getSuffixFor(byte kingdom) {
/* 376 */     Kingdom k = getKingdomOrNull(kingdom);
/* 377 */     if (k != null)
/* 378 */       return k.getSuffix(); 
/* 379 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getChatNameFor(byte kingdom) {
/* 390 */     Kingdom k = getKingdomOrNull(kingdom);
/* 391 */     if (k != null)
/* 392 */       return k.getChatName(); 
/* 393 */     return "no known kingdom";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void addTower(Item tower) {
/* 402 */     if (!towers.keySet().contains(tower)) {
/*     */       
/* 404 */       towers.put(tower, new GuardTower(tower));
/* 405 */       addTowerKingdom(tower);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void reAddKingdomInfluences(int startx, int starty, int endx, int endy) {
/* 411 */     for (Village v : Villages.getVillagesWithin(startx, starty, endx, endy))
/*     */     {
/* 413 */       v.setKingdomInfluence();
/*     */     }
/* 415 */     Zones.addWarDomains();
/* 416 */     for (Iterator<Item> i = towers.keySet().iterator(); i.hasNext(); ) {
/*     */       
/* 418 */       Item it = i.next();
/* 419 */       if (it.getTileX() >= startx && it.getTileX() <= endx && it.getTileY() >= starty && it.getTileY() < endy)
/*     */       {
/* 421 */         addTowerKingdom(it);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addTowerKingdom(Item tower) {
/* 429 */     if (tower.getKingdom() != 0 && tower.getTemplateId() != 996) {
/*     */ 
/*     */       
/* 432 */       Kingdom k = getKingdom(tower.getKingdom());
/* 433 */       if (k.getId() != 0) {
/*     */ 
/*     */         
/* 436 */         for (int x = tower.getTileX() - 60; x < tower.getTileX() + 60; x++) {
/*     */           
/* 438 */           for (int y = tower.getTileY() - 60; y < tower.getTileY() + 60; y++) {
/*     */             
/* 440 */             if (Zones.getKingdom(x, y) == 0)
/* 441 */               Zones.setKingdom(x, y, tower.getKingdom()); 
/*     */           } 
/*     */         } 
/* 444 */         if (Features.Feature.TOWER_CHAINING.isEnabled()) {
/* 445 */           InfluenceChain.addTowerToChain(k.getId(), tower);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static final void removeInfluenceForTower(Item item) {
/* 452 */     int extraCheckedTiles = 1;
/* 453 */     for (int x = item.getTileX() - 60 - 1; x < item.getTileX() + 60 + 1; x++) {
/*     */       
/* 455 */       for (int y = item.getTileY() - 60 - 1; y < item.getTileY() + 60 + 1; y++) {
/*     */         
/* 457 */         if (Zones.getKingdom(x, y) == item.getKingdom())
/*     */         {
/* 459 */           if (Villages.getVillageWithPerimeterAt(x, y, true) == null)
/* 460 */             Zones.setKingdom(x, y, (byte)0); 
/*     */         }
/*     */       } 
/*     */     } 
/* 464 */     if (Features.Feature.TOWER_CHAINING.isEnabled()) {
/* 465 */       InfluenceChain.removeTowerFromChain(item.getKingdom(), item);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final void addWarTargetKingdom(Item target) {
/* 470 */     if (target.getKingdom() != 0) {
/*     */       
/* 472 */       Kingdom k = getKingdom(target.getKingdom());
/* 473 */       if (k.getId() != 0) {
/*     */         
/* 475 */         int sx = Zones.safeTileX(target.getTileX() - 60);
/* 476 */         int ex = Zones.safeTileX(target.getTileX() + 60);
/* 477 */         int sy = Zones.safeTileY(target.getTileY() - 60);
/* 478 */         int ey = Zones.safeTileY(target.getTileY() + 60);
/* 479 */         for (int x = sx; x <= ex; x++) {
/* 480 */           for (int y = sy; y <= ey; y++) {
/*     */             
/* 482 */             if (Villages.getVillageWithPerimeterAt(x, y, true) == null)
/* 483 */               Zones.setKingdom(x, y, target.getKingdom()); 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public static final void destroyTower(Item item) {
/* 490 */     destroyTower(item, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void destroyTower(Item item, boolean destroyItem) {
/* 499 */     if (towers == null || towers.size() == 0) {
/*     */       
/* 501 */       GuardTower t = new GuardTower(item);
/* 502 */       t.destroy();
/* 503 */       Items.destroyItem(item.getWurmId());
/*     */     }
/*     */     else {
/*     */       
/* 507 */       GuardTower t = towers.get(item);
/* 508 */       if (t != null)
/* 509 */         t.destroy(); 
/* 510 */       towers.remove(item);
/* 511 */       if (destroyItem)
/* 512 */         Items.destroyItem(item.getWurmId()); 
/* 513 */       removeInfluenceForTower(item);
/* 514 */       Zones.removeGuardTower(item);
/* 515 */       reAddKingdomInfluences(item.getTileX() - 200, item.getTileY() - 200, item.getTileX() + 200, item
/* 516 */           .getTileY() + 200);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final GuardTower getTower(Item tower) {
/* 527 */     return towers.get(tower);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final GuardTower getClosestTower(int tilex, int tiley, boolean surfaced) {
/* 537 */     GuardTower closest = null;
/* 538 */     int minDist = 2000;
/*     */     
/* 540 */     for (Iterator<GuardTower> it = towers.values().iterator(); it.hasNext(); ) {
/*     */       
/* 542 */       GuardTower tower = it.next();
/* 543 */       if (tower.getTower().isOnSurface() == surfaced) {
/*     */         
/* 545 */         int distx = Math.abs(tower.getTower().getTileX() - tilex);
/* 546 */         int disty = Math.abs(tower.getTower().getTileY() - tiley);
/* 547 */         if (distx < 50 && disty < 50 && (distx <= minDist || disty <= minDist)) {
/*     */           
/* 549 */           minDist = Math.min(distx, disty);
/* 550 */           closest = tower;
/*     */         } 
/*     */       } 
/*     */     } 
/* 554 */     return closest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final GuardTower getClosestEnemyTower(int tilex, int tiley, boolean surfaced, Creature searcher) {
/* 565 */     GuardTower closest = null;
/* 566 */     if (searcher.getKingdomId() != 0) {
/*     */       
/* 568 */       int minDist = 2000;
/*     */       
/* 570 */       for (Iterator<GuardTower> it = towers.values().iterator(); it.hasNext(); ) {
/*     */         
/* 572 */         GuardTower tower = it.next();
/* 573 */         if (tower.getTower().isOnSurface() == surfaced) {
/*     */           
/* 575 */           int distx = Math.abs(tower.getTower().getTileX() - tilex);
/* 576 */           int disty = Math.abs(tower.getTower().getTileY() - tiley);
/* 577 */           if (distx <= minDist || disty <= minDist)
/*     */           {
/* 579 */             if (!searcher.isFriendlyKingdom(tower.getKingdom())) {
/*     */               
/* 581 */               minDist = Math.min(distx, disty);
/* 582 */               closest = tower;
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 588 */     return closest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Item getClosestWarTarget(int tilex, int tiley, Creature searcher) {
/* 598 */     Item closest = null;
/* 599 */     if (searcher.getKingdomId() != 0) {
/*     */       
/* 601 */       int minDist = 200;
/*     */       
/* 603 */       for (Item target : Items.getWarTargets()) {
/*     */         
/* 605 */         int distx = Math.abs(target.getTileX() - tilex);
/* 606 */         int disty = Math.abs(target.getTileY() - tiley);
/* 607 */         if (distx <= minDist || disty <= minDist)
/*     */         {
/* 609 */           if (searcher.isFriendlyKingdom(target.getKingdom())) {
/*     */             
/* 611 */             minDist = Math.min(distx, disty);
/* 612 */             closest = target;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 617 */     return closest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final GuardTower getTower(Creature guard) {
/* 627 */     return guard.getGuardTower();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final GuardTower getRandomTowerForKingdom(byte kingdom) {
/* 638 */     LinkedList<GuardTower> tows = new LinkedList<>();
/* 639 */     for (Iterator<GuardTower> it = towers.values().iterator(); it.hasNext(); ) {
/*     */       
/* 641 */       GuardTower tower = it.next();
/* 642 */       if (tower.getKingdom() == kingdom)
/* 643 */         tows.add(tower); 
/*     */     } 
/* 645 */     if (tows.size() > 0)
/* 646 */       return tows.get(Server.rand.nextInt(tows.size())); 
/* 647 */     return null;
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
/*     */   public static final boolean isTowerTooNear(int tilex, int tiley, boolean surfaced, boolean archery) {
/* 659 */     if (archery) {
/*     */       
/* 661 */       for (Item gt : Items.getAllItems()) {
/*     */         
/* 663 */         if (gt.isProtectionTower() && gt.isOnSurface() == surfaced)
/*     */         {
/* 665 */           if (Math.abs(((int)gt.getPosX() >> 2) - tilex) < 20 && 
/* 666 */             Math.abs(((int)gt.getPosY() >> 2) - tiley) < 20) {
/* 667 */             return true;
/*     */           }
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/* 673 */       for (Iterator<Item> it = towers.keySet().iterator(); it.hasNext(); ) {
/*     */         
/* 675 */         Item gt = it.next();
/* 676 */         if (gt.isOnSurface() == surfaced)
/*     */         {
/* 678 */           if (Math.abs(((int)gt.getPosX() >> 2) - tilex) < 50 && 
/* 679 */             Math.abs(((int)gt.getPosY() >> 2) - tiley) < 50)
/* 680 */             return true; 
/*     */         }
/*     */       } 
/*     */     } 
/* 684 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void convertTowersWithin(int startx, int starty, int endx, int endy, byte newKingdom) {
/* 690 */     for (Iterator<Item> i = towers.keySet().iterator(); i.hasNext(); ) {
/*     */       
/* 692 */       Item it = i.next();
/* 693 */       if (it.getTileX() >= startx && it.getTileX() <= endx && it.getTileY() >= starty && it.getTileY() < endy) {
/*     */         
/* 695 */         removeInfluenceForTower(it);
/* 696 */         it.setAuxData(newKingdom);
/* 697 */         addTowerKingdom(it);
/* 698 */         Kingdom k = getKingdom(newKingdom);
/* 699 */         boolean changed = false;
/* 700 */         if (k != null) {
/*     */           
/* 702 */           String aName = k.getName() + " guard tower";
/*     */           
/* 704 */           it.setName(aName);
/* 705 */           int templateId = 384;
/* 706 */           if (k.getTemplate() == 2) {
/* 707 */             templateId = 528;
/* 708 */           } else if (k.getTemplate() == 3) {
/* 709 */             templateId = 430;
/* 710 */           }  if (k.getTemplate() == 4)
/* 711 */             templateId = 638; 
/* 712 */           if (it.getTemplateId() != templateId) {
/*     */             
/* 714 */             it.setTemplateId(templateId);
/* 715 */             changed = true;
/*     */           } 
/*     */         } 
/* 718 */         if (!changed)
/* 719 */           it.updateIfGroundItem(); 
/* 720 */         ((GuardTower)towers.get(it)).destroyGuards();
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
/*     */   public static final void poll() {
/* 752 */     for (Iterator<GuardTower> it = towers.values().iterator(); it.hasNext();)
/*     */     {
/* 754 */       ((GuardTower)it.next()).poll();
/*     */     }
/* 756 */     King[] kings = King.getKings();
/* 757 */     if (kings != null)
/*     */     {
/* 759 */       for (King king : kings) {
/*     */ 
/*     */         
/*     */         try {
/* 763 */           Player player = Players.getInstance().getPlayer(king.kingid);
/* 764 */           if (player.getKingdomId() != king.kingdom)
/*     */           {
/* 766 */             king.abdicate(player.isOnSurface(), false);
/*     */           }
/*     */         }
/* 769 */         catch (NoSuchPlayerException noSuchPlayerException) {}
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
/*     */   public static int getNumberOfGuardTowers() {
/*     */     int numberOfTowers;
/* 785 */     if (towers != null) {
/*     */       
/* 787 */       numberOfTowers = towers.size();
/*     */     }
/*     */     else {
/*     */       
/* 791 */       numberOfTowers = 0;
/*     */     } 
/* 793 */     return numberOfTowers;
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
/*     */ 
/*     */   
/*     */   public static void checkIfDisbandKingdom() {}
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
/*     */   public static final void destroyTowersWithKingdom(byte deletedKingdom) {
/* 824 */     if (towers != null)
/*     */     {
/* 826 */       for (GuardTower tower : towers.values()) {
/*     */         
/* 828 */         if (tower.getKingdom() == deletedKingdom)
/*     */         {
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
/* 841 */           destroyTower(tower.getTower(), true);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean isCustomKingdom(byte kingdomId) {
/* 849 */     return (kingdomId < 0 || kingdomId > 4);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\kingdom\Kingdoms.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */