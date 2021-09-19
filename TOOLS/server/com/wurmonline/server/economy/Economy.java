/*     */ package com.wurmonline.server.economy;
/*     */ 
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.concurrent.GuardedBy;
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
/*     */ public abstract class Economy
/*     */   implements MonetaryConstants, MiscConstants, TimeConstants
/*     */ {
/*     */   static long goldCoins;
/*     */   static long lastPolledTraders;
/*     */   static long copperCoins;
/*     */   static long silverCoins;
/*     */   static long ironCoins;
/*  55 */   private static final LinkedList<Item> goldOnes = new LinkedList<>();
/*  56 */   private static final LinkedList<Item> goldFives = new LinkedList<>();
/*  57 */   private static final LinkedList<Item> goldTwentys = new LinkedList<>();
/*  58 */   private static final LinkedList<Item> silverOnes = new LinkedList<>();
/*  59 */   private static final LinkedList<Item> silverFives = new LinkedList<>();
/*  60 */   private static final LinkedList<Item> silverTwentys = new LinkedList<>();
/*  61 */   private static final LinkedList<Item> copperOnes = new LinkedList<>();
/*  62 */   private static final LinkedList<Item> copperFives = new LinkedList<>();
/*  63 */   private static final LinkedList<Item> copperTwentys = new LinkedList<>();
/*  64 */   private static final LinkedList<Item> ironOnes = new LinkedList<>();
/*  65 */   private static final LinkedList<Item> ironFives = new LinkedList<>();
/*  66 */   private static final LinkedList<Item> ironTwentys = new LinkedList<>();
/*     */   
/*  68 */   private static final Logger logger = Logger.getLogger(Economy.class.getName());
/*     */   
/*  70 */   private static final Logger moneylogger = Logger.getLogger("Money");
/*     */   
/*     */   final int id;
/*  73 */   private static final Map<Integer, SupplyDemand> supplyDemand = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("SHOPS_RW_LOCK")
/*  78 */   private static final Map<Long, Shop> shops = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*  82 */   private static final ReentrantReadWriteLock SHOPS_RW_LOCK = new ReentrantReadWriteLock();
/*     */ 
/*     */   
/*     */   private static Economy economy;
/*     */ 
/*     */   
/*     */   private static final int minTraderDistance = 64;
/*     */ 
/*     */   
/*     */   public static Economy getEconomy() {
/*  92 */     if (economy == null) {
/*     */       
/*  94 */       long start = System.nanoTime();
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  99 */         economy = new DbEconomy(Servers.localServer.id);
/*     */       }
/* 101 */       catch (IOException iox) {
/*     */         
/* 103 */         logger.log(Level.WARNING, "Failed to create economy: " + iox.getMessage(), iox);
/* 104 */         Server.getInstance().shutDown();
/*     */       } 
/*     */       
/* 107 */       logger.log(Level.INFO, "Loading economy took " + ((float)(System.nanoTime() - start) / 1000000.0F) + " ms.");
/*     */     } 
/* 109 */     return economy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Economy(int aEconomy) throws IOException {
/* 117 */     goldCoins = 0L;
/* 118 */     copperCoins = 0L;
/* 119 */     silverCoins = 0L;
/* 120 */     ironCoins = 0L;
/* 121 */     this.id = aEconomy;
/* 122 */     initialize();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getGold() {
/* 127 */     return goldCoins;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getSilver() {
/* 132 */     return silverCoins;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getCopper() {
/* 137 */     return copperCoins;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getIron() {
/* 142 */     return ironCoins;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int getValueFor(int coinType) {
/* 147 */     switch (coinType) {
/*     */       
/*     */       case 50:
/* 150 */         return 100;
/*     */       case 54:
/* 152 */         return 500;
/*     */       case 58:
/* 154 */         return 2000;
/*     */       case 51:
/* 156 */         return 1;
/*     */       case 55:
/* 158 */         return 5;
/*     */       case 59:
/* 160 */         return 20;
/*     */       case 52:
/* 162 */         return 10000;
/*     */       case 56:
/* 164 */         return 50000;
/*     */       case 60:
/* 166 */         return 200000;
/*     */       case 53:
/* 168 */         return 1000000;
/*     */       case 57:
/* 170 */         return 5000000;
/*     */       case 61:
/* 172 */         return 20000000;
/*     */     } 
/* 174 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   LinkedList<Item> getListForCointype(int type) {
/* 180 */     switch (type) {
/*     */       
/*     */       case 50:
/* 183 */         return copperOnes;
/*     */       case 54:
/* 185 */         return copperFives;
/*     */       case 58:
/* 187 */         return copperTwentys;
/*     */       case 51:
/* 189 */         return ironOnes;
/*     */       case 55:
/* 191 */         return ironFives;
/*     */       case 59:
/* 193 */         return ironTwentys;
/*     */       case 52:
/* 195 */         return silverOnes;
/*     */       case 56:
/* 197 */         return silverFives;
/*     */       case 60:
/* 199 */         return silverTwentys;
/*     */       case 53:
/* 201 */         return goldOnes;
/*     */       case 57:
/* 203 */         return goldFives;
/*     */       case 61:
/* 205 */         return goldTwentys;
/*     */     } 
/* 207 */     logger.log(Level.WARNING, "Found no list for type " + type);
/* 208 */     return new LinkedList<>();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void returnCoin(Item coin, String message) {
/* 214 */     returnCoin(coin, message, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void returnCoin(Item coin, String message, boolean dontLog) {
/* 219 */     if (!dontLog) {
/* 220 */       transaction(coin.getWurmId(), coin.getOwnerId(), this.id, message, coin.getValue());
/*     */     }
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
/* 234 */     coin.setTradeWindow(null);
/* 235 */     coin.setOwner(-10L, false);
/* 236 */     coin.setLastOwnerId(-10L);
/* 237 */     coin.setZoneId(-10, true);
/* 238 */     coin.setParentId(-10L, true);
/* 239 */     coin.setRarity((byte)0);
/* 240 */     coin.setBanked(true);
/*     */     
/* 242 */     int templateid = coin.getTemplateId();
/* 243 */     List<Item> toAdd = getListForCointype(templateid);
/* 244 */     toAdd.add(coin);
/*     */   }
/*     */ 
/*     */   
/*     */   public Item[] getCoinsFor(long value) {
/* 249 */     if (value > 0L) {
/*     */       
/*     */       try {
/*     */         
/* 253 */         if (value >= 1000000L)
/* 254 */           return getGoldTwentyCoinsFor(value, new HashSet<>()); 
/* 255 */         if (value >= 10000L)
/* 256 */           return getSilverTwentyCoinsFor(value, new HashSet<>()); 
/* 257 */         if (value >= 100L) {
/* 258 */           return getCopperTwentyCoinsFor(value, new HashSet<>());
/*     */         }
/* 260 */         return getIronTwentyCoinsFor(value, new HashSet<>());
/*     */       }
/* 262 */       catch (FailedException fe) {
/*     */         
/* 264 */         logger.log(Level.WARNING, "Failed to create coins: " + fe.getMessage(), (Throwable)fe);
/*     */       }
/* 266 */       catch (NoSuchTemplateException nst) {
/*     */         
/* 268 */         logger.log(Level.WARNING, "Failed to create coins: " + nst.getMessage(), (Throwable)nst);
/*     */       } 
/*     */     }
/* 271 */     return new Item[0];
/*     */   }
/*     */ 
/*     */   
/*     */   private Item[] getGoldTwentyCoinsFor(long value, Set<Item> items) throws FailedException, NoSuchTemplateException {
/* 276 */     if (value > 0L) {
/*     */       
/* 278 */       long num = value / 20000000L;
/* 279 */       if (items == null)
/* 280 */         items = new HashSet<>();  long x;
/* 281 */       for (x = 0L; x < num; x++) {
/*     */         
/* 283 */         Item coin = null;
/* 284 */         if (goldTwentys.size() > 0) {
/* 285 */           coin = goldTwentys.removeFirst();
/*     */         } else {
/*     */           
/* 288 */           coin = ItemFactory.createItem(61, Server.rand.nextFloat() * 100.0F, null);
/* 289 */           goldCoins += 20L;
/* 290 */           updateCreatedGold(goldCoins);
/* 291 */           logger.log(Level.INFO, "CREATING COIN GOLD20 " + coin.getWurmId(), new Exception());
/*     */         } 
/* 293 */         items.add(coin);
/* 294 */         coin.setBanked(false);
/* 295 */         value -= 20000000L;
/*     */       } 
/*     */     } 
/* 298 */     return getGoldFiveCoinsFor(value, items);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Shop[] getShops() {
/* 307 */     SHOPS_RW_LOCK.readLock().lock();
/*     */     
/*     */     try {
/* 310 */       return (Shop[])shops.values().toArray((Object[])new Shop[shops.size()]);
/*     */     }
/*     */     finally {
/*     */       
/* 314 */       SHOPS_RW_LOCK.readLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static Shop[] getTraders() {
/* 320 */     Map<Long, Shop> traders = new HashMap<>();
/* 321 */     SHOPS_RW_LOCK.readLock().lock();
/*     */     
/*     */     try {
/* 324 */       for (Shop s : shops.values()) {
/*     */         
/* 326 */         if (!s.isPersonal() && s.getWurmId() != 0L)
/* 327 */           traders.put(Long.valueOf(s.getWurmId()), s); 
/*     */       } 
/* 329 */       return (Shop[])traders.values().toArray((Object[])new Shop[traders.size()]);
/*     */     }
/*     */     finally {
/*     */       
/* 333 */       SHOPS_RW_LOCK.readLock().unlock();
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
/*     */   public long getShopMoney() {
/* 345 */     long toRet = 0L;
/* 346 */     Shop[] lShops = getShops();
/* 347 */     for (Shop lLShop : lShops) {
/*     */       
/* 349 */       if (lLShop.getMoney() > 0L)
/* 350 */         toRet += lLShop.getMoney(); 
/*     */     } 
/* 352 */     return toRet;
/*     */   }
/*     */ 
/*     */   
/*     */   public void pollTraderEarnings() {
/* 357 */     if (System.currentTimeMillis() - lastPolledTraders > 2419200000L) {
/*     */       
/* 359 */       resetEarnings();
/* 360 */       updateLastPolled();
/* 361 */       logger.log(Level.INFO, "Economy reset earnings.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Item[] getGoldFiveCoinsFor(long value, Set<Item> items) throws FailedException, NoSuchTemplateException {
/* 367 */     if (value > 0L) {
/*     */       
/* 369 */       long num = value / 5000000L;
/* 370 */       if (items == null)
/* 371 */         items = new HashSet<>();  long x;
/* 372 */       for (x = 0L; x < num; x++) {
/*     */         
/* 374 */         Item coin = null;
/* 375 */         if (goldFives.size() > 0) {
/* 376 */           coin = goldFives.removeFirst();
/*     */         } else {
/*     */           
/* 379 */           coin = ItemFactory.createItem(57, Server.rand.nextFloat() * 100.0F, null);
/* 380 */           goldCoins += 5L;
/* 381 */           updateCreatedGold(goldCoins);
/* 382 */           logger.log(Level.INFO, "CREATING COIN GOLD5 " + coin.getWurmId(), new Exception());
/*     */         } 
/* 384 */         items.add(coin);
/* 385 */         coin.setBanked(false);
/* 386 */         value -= 5000000L;
/*     */       } 
/*     */     } 
/* 389 */     return getGoldOneCoinsFor(value, items);
/*     */   }
/*     */ 
/*     */   
/*     */   private Item[] getGoldOneCoinsFor(long value, Set<Item> items) throws FailedException, NoSuchTemplateException {
/* 394 */     if (value > 0L) {
/*     */       
/* 396 */       long num = value / 1000000L;
/* 397 */       if (items == null)
/* 398 */         items = new HashSet<>();  long x;
/* 399 */       for (x = 0L; x < num; x++) {
/*     */         
/* 401 */         Item coin = null;
/* 402 */         if (goldOnes.size() > 0) {
/* 403 */           coin = goldOnes.removeFirst();
/*     */         } else {
/*     */           
/* 406 */           coin = ItemFactory.createItem(53, Server.rand.nextFloat() * 100.0F, null);
/* 407 */           goldCoins++;
/* 408 */           updateCreatedGold(goldCoins);
/*     */           
/* 410 */           logger.log(Level.INFO, "CREATING COIN GOLD1 " + coin.getWurmId(), new Exception());
/*     */         } 
/* 412 */         items.add(coin);
/* 413 */         coin.setBanked(false);
/* 414 */         value -= 1000000L;
/*     */       } 
/*     */     } 
/* 417 */     return getSilverTwentyCoinsFor(value, items);
/*     */   }
/*     */ 
/*     */   
/*     */   private Item[] getSilverTwentyCoinsFor(long value, Set<Item> items) throws FailedException, NoSuchTemplateException {
/* 422 */     if (value > 0L) {
/*     */       
/* 424 */       long num = value / 200000L;
/* 425 */       if (items == null)
/* 426 */         items = new HashSet<>();  long x;
/* 427 */       for (x = 0L; x < num; x++) {
/*     */         
/* 429 */         Item coin = null;
/* 430 */         if (silverTwentys.size() > 0) {
/* 431 */           coin = silverTwentys.removeFirst();
/*     */         } else {
/*     */           
/* 434 */           coin = ItemFactory.createItem(60, Server.rand.nextFloat() * 100.0F, null);
/* 435 */           silverCoins += 20L;
/* 436 */           updateCreatedSilver(silverCoins);
/*     */         } 
/* 438 */         items.add(coin);
/* 439 */         coin.setBanked(false);
/* 440 */         value -= 200000L;
/*     */       } 
/*     */     } 
/* 443 */     return getSilverFiveCoinsFor(value, items);
/*     */   }
/*     */ 
/*     */   
/*     */   private Item[] getSilverFiveCoinsFor(long value, Set<Item> items) throws FailedException, NoSuchTemplateException {
/* 448 */     if (value > 0L) {
/*     */       
/* 450 */       long num = value / 50000L;
/* 451 */       if (items == null)
/* 452 */         items = new HashSet<>();  long x;
/* 453 */       for (x = 0L; x < num; x++) {
/*     */         
/* 455 */         Item coin = null;
/* 456 */         if (silverFives.size() > 0) {
/* 457 */           coin = silverFives.removeFirst();
/*     */         } else {
/*     */           
/* 460 */           coin = ItemFactory.createItem(56, Server.rand.nextFloat() * 100.0F, null);
/* 461 */           silverCoins += 5L;
/* 462 */           updateCreatedSilver(silverCoins);
/*     */         } 
/* 464 */         items.add(coin);
/* 465 */         coin.setBanked(false);
/* 466 */         value -= 50000L;
/*     */       } 
/*     */     } 
/* 469 */     return getSilverOneCoinsFor(value, items);
/*     */   }
/*     */ 
/*     */   
/*     */   private Item[] getSilverOneCoinsFor(long value, Set<Item> items) throws FailedException, NoSuchTemplateException {
/* 474 */     if (value > 0L) {
/*     */       
/* 476 */       long num = value / 10000L;
/* 477 */       if (items == null)
/* 478 */         items = new HashSet<>();  long x;
/* 479 */       for (x = 0L; x < num; x++) {
/*     */         
/* 481 */         Item coin = null;
/* 482 */         if (silverOnes.size() > 0) {
/* 483 */           coin = silverOnes.removeFirst();
/*     */         } else {
/*     */           
/* 486 */           coin = ItemFactory.createItem(52, Server.rand.nextFloat() * 100.0F, null);
/* 487 */           silverCoins++;
/* 488 */           updateCreatedSilver(silverCoins);
/*     */         } 
/* 490 */         items.add(coin);
/* 491 */         coin.setBanked(false);
/* 492 */         value -= 10000L;
/*     */       } 
/*     */     } 
/* 495 */     return getCopperTwentyCoinsFor(value, items);
/*     */   }
/*     */ 
/*     */   
/*     */   private Item[] getCopperTwentyCoinsFor(long value, Set<Item> items) throws FailedException, NoSuchTemplateException {
/* 500 */     if (value > 0L) {
/*     */       
/* 502 */       long num = value / 2000L;
/* 503 */       if (items == null)
/* 504 */         items = new HashSet<>();  long x;
/* 505 */       for (x = 0L; x < num; x++) {
/*     */         
/* 507 */         Item coin = null;
/* 508 */         if (copperTwentys.size() > 0) {
/* 509 */           coin = copperTwentys.removeFirst();
/*     */         } else {
/*     */           
/* 512 */           coin = ItemFactory.createItem(58, Server.rand.nextFloat() * 100.0F, null);
/* 513 */           copperCoins += 20L;
/* 514 */           updateCreatedCopper(copperCoins);
/*     */         } 
/* 516 */         items.add(coin);
/* 517 */         coin.setBanked(false);
/* 518 */         value -= 2000L;
/*     */       } 
/*     */     } 
/* 521 */     return getCopperFiveCoinsFor(value, items);
/*     */   }
/*     */ 
/*     */   
/*     */   private Item[] getCopperFiveCoinsFor(long value, Set<Item> items) throws FailedException, NoSuchTemplateException {
/* 526 */     if (value > 0L) {
/*     */       
/* 528 */       long num = value / 500L;
/* 529 */       if (items == null)
/* 530 */         items = new HashSet<>();  long x;
/* 531 */       for (x = 0L; x < num; x++) {
/*     */         
/* 533 */         Item coin = null;
/* 534 */         if (copperFives.size() > 0) {
/*     */           
/* 536 */           coin = copperFives.removeFirst();
/*     */         }
/*     */         else {
/*     */           
/* 540 */           coin = ItemFactory.createItem(54, Server.rand.nextFloat() * 100.0F, null);
/* 541 */           copperCoins += 5L;
/* 542 */           updateCreatedCopper(copperCoins);
/*     */         } 
/* 544 */         items.add(coin);
/* 545 */         coin.setBanked(false);
/* 546 */         value -= 500L;
/*     */       } 
/*     */     } 
/* 549 */     return getCopperOneCoinsFor(value, items);
/*     */   }
/*     */ 
/*     */   
/*     */   private Item[] getCopperOneCoinsFor(long value, Set<Item> items) throws FailedException, NoSuchTemplateException {
/* 554 */     if (value > 0L) {
/*     */       
/* 556 */       long num = value / 100L;
/* 557 */       if (items == null)
/* 558 */         items = new HashSet<>();  long x;
/* 559 */       for (x = 0L; x < num; x++) {
/*     */         
/* 561 */         Item coin = null;
/* 562 */         if (copperOnes.size() > 0) {
/*     */           
/* 564 */           coin = copperOnes.removeFirst();
/*     */         }
/*     */         else {
/*     */           
/* 568 */           coin = ItemFactory.createItem(50, Server.rand.nextFloat() * 100.0F, null);
/* 569 */           copperCoins++;
/* 570 */           updateCreatedCopper(copperCoins);
/*     */         } 
/* 572 */         items.add(coin);
/* 573 */         coin.setBanked(false);
/* 574 */         value -= 100L;
/*     */       } 
/*     */     } 
/* 577 */     return getIronTwentyCoinsFor(value, items);
/*     */   }
/*     */ 
/*     */   
/*     */   private Item[] getIronTwentyCoinsFor(long value, Set<Item> items) throws FailedException, NoSuchTemplateException {
/* 582 */     if (value > 0L) {
/*     */       
/* 584 */       long num = value / 20L;
/* 585 */       if (items == null)
/* 586 */         items = new HashSet<>();  long x;
/* 587 */       for (x = 0L; x < num; x++) {
/*     */         
/* 589 */         Item coin = null;
/* 590 */         if (ironTwentys.size() > 0) {
/* 591 */           coin = ironTwentys.removeFirst();
/*     */         } else {
/*     */           
/* 594 */           coin = ItemFactory.createItem(59, Server.rand.nextFloat() * 100.0F, null);
/* 595 */           ironCoins += 20L;
/* 596 */           updateCreatedIron(ironCoins);
/*     */         } 
/* 598 */         items.add(coin);
/* 599 */         coin.setBanked(false);
/* 600 */         value -= 20L;
/*     */       } 
/*     */     } 
/* 603 */     return getIronFiveCoinsFor(value, items);
/*     */   }
/*     */ 
/*     */   
/*     */   private Item[] getIronFiveCoinsFor(long value, Set<Item> items) throws FailedException, NoSuchTemplateException {
/* 608 */     if (value > 0L) {
/*     */       
/* 610 */       long num = value / 5L;
/* 611 */       if (items == null)
/* 612 */         items = new HashSet<>();  long x;
/* 613 */       for (x = 0L; x < num; x++) {
/*     */         
/* 615 */         Item coin = null;
/* 616 */         if (ironFives.size() > 0) {
/* 617 */           coin = ironFives.removeFirst();
/*     */         } else {
/*     */           
/* 620 */           coin = ItemFactory.createItem(55, Server.rand.nextFloat() * 100.0F, null);
/* 621 */           ironCoins += 5L;
/* 622 */           updateCreatedIron(ironCoins);
/*     */         } 
/* 624 */         items.add(coin);
/* 625 */         coin.setBanked(false);
/* 626 */         value -= 5L;
/*     */       } 
/*     */     } 
/* 629 */     return getIronOneCoinsFor(value, items);
/*     */   }
/*     */ 
/*     */   
/*     */   private Item[] getIronOneCoinsFor(long value, Set<Item> items) throws FailedException, NoSuchTemplateException {
/* 634 */     if (value > 0L) {
/*     */       
/* 636 */       long num = value;
/* 637 */       if (items == null)
/* 638 */         items = new HashSet<>(); 
/* 639 */       for (int x = 0; x < num; x++) {
/*     */         
/* 641 */         Item coin = null;
/* 642 */         if (ironOnes.size() > 0) {
/* 643 */           coin = ironOnes.removeFirst();
/*     */         } else {
/*     */           
/* 646 */           coin = ItemFactory.createItem(51, Server.rand.nextFloat() * 100.0F, null);
/* 647 */           ironCoins++;
/* 648 */           updateCreatedIron(ironCoins);
/*     */         } 
/* 650 */         items.add(coin);
/* 651 */         coin.setBanked(false);
/* 652 */         value--;
/*     */       } 
/*     */     } 
/* 655 */     return items.<Item>toArray(new Item[items.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SupplyDemand getSupplyDemand(int itemTemplateId) {
/* 665 */     SupplyDemand sd = supplyDemand.get(Integer.valueOf(itemTemplateId));
/* 666 */     if (sd == null)
/* 667 */       sd = createSupplyDemand(itemTemplateId); 
/* 668 */     return sd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPool(int itemTemplateId) {
/* 679 */     SupplyDemand sd = getSupplyDemand(itemTemplateId);
/* 680 */     return sd.getPool();
/*     */   }
/*     */ 
/*     */   
/*     */   public Shop getShop(Creature creature) {
/* 685 */     return getShop(creature, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public Shop getShop(Creature creature, boolean destroying) {
/* 690 */     Shop tm = null;
/*     */     
/* 692 */     if (creature.isNpcTrader()) {
/*     */       
/* 694 */       SHOPS_RW_LOCK.readLock().lock();
/*     */       
/*     */       try {
/* 697 */         tm = shops.get(new Long(creature.getWurmId()));
/*     */       }
/*     */       finally {
/*     */         
/* 701 */         SHOPS_RW_LOCK.readLock().unlock();
/*     */       } 
/* 703 */       if (!destroying && tm == null)
/* 704 */         tm = createShop(creature.getWurmId()); 
/*     */     } 
/* 706 */     return tm;
/*     */   }
/*     */ 
/*     */   
/*     */   public Shop[] getShopsForOwner(long owner) {
/* 711 */     Set<Shop> sh = new HashSet<>();
/* 712 */     SHOPS_RW_LOCK.readLock().lock();
/*     */     
/*     */     try {
/* 715 */       for (Shop shop : shops.values()) {
/*     */         
/* 717 */         if (shop.getOwnerId() == owner) {
/* 718 */           sh.add(shop);
/*     */         }
/*     */       } 
/*     */     } finally {
/*     */       
/* 723 */       SHOPS_RW_LOCK.readLock().unlock();
/*     */     } 
/* 725 */     return sh.<Shop>toArray(new Shop[sh.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public Shop getKingsShop() {
/*     */     Shop tm;
/* 731 */     SHOPS_RW_LOCK.readLock().lock();
/*     */     
/*     */     try {
/* 734 */       tm = shops.get(Long.valueOf(0L));
/*     */     }
/*     */     finally {
/*     */       
/* 738 */       SHOPS_RW_LOCK.readLock().unlock();
/*     */     } 
/* 740 */     if (tm == null)
/* 741 */       tm = createShop(0L); 
/* 742 */     return tm;
/*     */   }
/*     */ 
/*     */   
/*     */   static void addShop(Shop tm) {
/* 747 */     SHOPS_RW_LOCK.writeLock().lock();
/*     */     
/*     */     try {
/* 750 */       shops.put(new Long(tm.getWurmId()), tm);
/*     */     }
/*     */     finally {
/*     */       
/* 754 */       SHOPS_RW_LOCK.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void deleteShop(long wurmid) {
/* 760 */     SHOPS_RW_LOCK.writeLock().lock();
/*     */     
/*     */     try {
/* 763 */       Shop shop = shops.get(new Long(wurmid));
/* 764 */       if (shop != null)
/* 765 */         shop.delete(); 
/* 766 */       shops.remove(new Long(wurmid));
/*     */     }
/*     */     finally {
/*     */       
/* 770 */       SHOPS_RW_LOCK.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void addSupplyDemand(SupplyDemand sd) {
/* 776 */     supplyDemand.put(Integer.valueOf(sd.getId()), sd);
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
/*     */   public void addItemSoldByTraders(int templateId) {
/* 795 */     getSupplyDemand(templateId).addItemSoldByTrader();
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void addItemSoldByTraders(String paramString1, long paramLong, String paramString2, String paramString3, int paramInt);
/*     */ 
/*     */   
/*     */   public void addItemBoughtByTraders(int templateId) {
/* 803 */     getSupplyDemand(templateId).addItemBoughtByTrader();
/*     */   }
/*     */ 
/*     */   
/*     */   public Change getChangeFor(long value) {
/* 808 */     return new Change(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Creature getRandomTrader() {
/* 813 */     Creature toReturn = null;
/* 814 */     SHOPS_RW_LOCK.readLock().lock();
/*     */     
/*     */     try {
/* 817 */       int size = shops.size();
/* 818 */       for (Shop shop : shops.values()) {
/*     */         
/* 820 */         if (!shop.isPersonal() && shop.getWurmId() > 0L)
/*     */         {
/* 822 */           if (Server.rand.nextInt(Math.max(2, size / 2)) == 0) {
/*     */             
/*     */             try {
/* 825 */               toReturn = Creatures.getInstance().getCreature(shop.getWurmId());
/* 826 */               return toReturn;
/*     */             }
/* 828 */             catch (NoSuchCreatureException nsc) {
/*     */               
/* 830 */               logger.log(Level.WARNING, "Weird, shop with id " + shop.getWurmId() + " has no creature.");
/*     */             } 
/*     */           }
/*     */         }
/*     */       } 
/*     */     } finally {
/*     */       
/* 837 */       SHOPS_RW_LOCK.readLock().unlock();
/*     */     } 
/* 839 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public Creature getTraderForZone(int x, int y, boolean surfaced) {
/* 844 */     int sx = 0;
/* 845 */     int sy = 0;
/* 846 */     int ex = 64;
/* 847 */     int ey = 64;
/* 848 */     Creature toReturn = null;
/* 849 */     SHOPS_RW_LOCK.readLock().lock();
/*     */     
/*     */     try {
/* 852 */       for (Shop shop : shops.values()) {
/*     */         
/* 854 */         if (!shop.isPersonal() && shop.getWurmId() > 0L) {
/*     */           
/* 856 */           VolaTile tile = shop.getPos();
/* 857 */           if (tile != null) {
/*     */             
/* 859 */             sx = tile.getTileX() - 64;
/* 860 */             sy = tile.getTileY() - 64;
/* 861 */             ex = tile.getTileX() + 64;
/* 862 */             ey = tile.getTileY() + 64;
/* 863 */             if (x < ex && x > sx && y < ey && y > sy && tile.isOnSurface() == surfaced) {
/*     */               try
/*     */               {
/*     */                 
/* 867 */                 toReturn = Creatures.getInstance().getCreature(shop.getWurmId());
/* 868 */                 return toReturn;
/*     */               }
/* 870 */               catch (NoSuchCreatureException nsc)
/*     */               {
/* 872 */                 logger.log(Level.WARNING, "Weird, shop with id " + shop.getWurmId() + " has no creature.");
/*     */               }
/*     */             
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } finally {
/*     */       
/* 881 */       SHOPS_RW_LOCK.readLock().unlock();
/*     */     } 
/* 883 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void updateCreatedIron(long paramLong);
/*     */   
/*     */   public abstract void updateCreatedSilver(long paramLong);
/*     */   
/*     */   public abstract void updateCreatedCopper(long paramLong);
/*     */   
/*     */   public abstract void updateCreatedGold(long paramLong);
/*     */   
/*     */   abstract void loadSupplyDemand();
/*     */   
/*     */   abstract void loadShopMoney();
/*     */   
/*     */   abstract void initialize() throws IOException;
/*     */   
/*     */   abstract SupplyDemand createSupplyDemand(int paramInt);
/*     */   
/*     */   public abstract Shop createShop(long paramLong);
/*     */   
/*     */   public abstract Shop createShop(long paramLong1, long paramLong2);
/*     */   
/*     */   public abstract void transaction(long paramLong1, long paramLong2, long paramLong3, String paramString, long paramLong4);
/*     */   
/*     */   public abstract void updateLastPolled();
/*     */   
/*     */   public final void resetEarnings() {
/* 912 */     for (Shop s : shops.values())
/*     */     {
/* 914 */       s.resetEarnings();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\economy\Economy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */