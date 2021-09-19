/*     */ package com.wurmonline.server.epic;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import com.wurmonline.server.items.WurmColor;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.statistics.ChallengePointEnum;
/*     */ import com.wurmonline.server.statistics.ChallengeSummary;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.villages.NoSuchVillageException;
/*     */ import com.wurmonline.server.villages.PvPAlliance;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import com.wurmonline.server.zones.FocusZone;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zone;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Hota
/*     */   implements TimeConstants, MiscConstants
/*     */ {
/*  73 */   private static final Logger logger = Logger.getLogger(Hota.class.getName());
/*     */ 
/*     */   
/*     */   private static final String LOAD_ALL_HOTA_ITEMS = "SELECT * FROM HOTA_ITEMS";
/*     */ 
/*     */   
/*     */   private static final String CREATE_HOTA_ITEM = "INSERT INTO HOTA_ITEMS (ITEMID,ITEMTYPE) VALUES (?,?)";
/*     */ 
/*     */   
/*     */   private static final String DELETE_HOTA_ITEMS = "DELETE FROM HOTA_ITEMS";
/*     */   
/*     */   private static final String INSERT_HOTA_HELPER = "INSERT INTO HOTA_HELPERS (CONQUERS,WURMID) VALUES (?,?)";
/*     */   
/*     */   private static final String UPDATE_HOTA_HELPER = "UPDATE HOTA_HELPERS SET CONQUERS=? WHERE WURMID=?";
/*     */   
/*     */   private static final String LOAD_ALL_HOTA_HELPER = "SELECT * FROM HOTA_HELPERS";
/*     */   
/*     */   private static final String DELETE_HOTA_HELPERS = "DELETE FROM HOTA_HELPERS";
/*     */   
/*  92 */   private static final ConcurrentHashMap<Item, Byte> hotaItems = new ConcurrentHashMap<>();
/*     */   
/*  94 */   private static final ConcurrentHashMap<Long, Integer> hotaHelpers = new ConcurrentHashMap<>();
/*     */   
/*     */   public static final byte TYPE_NONE = 0;
/*     */   
/*     */   public static final byte TYPE_PILLAR = 1;
/*     */   
/*     */   public static final byte TYPE_SPEEDSHRINE = 2;
/* 101 */   private static long nextRoundMessage = Long.MAX_VALUE;
/*     */   
/*     */   public static final int VILLAGE_ID_MOD = 2000000;
/* 104 */   public static final LinkedList<Item> pillarsLeft = new LinkedList<>();
/* 105 */   public static final LinkedList<Item> pillarsTouched = new LinkedList<>();
/*     */   
/* 107 */   private static final Set<Long> hotaConquerers = new HashSet<>();
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
/*     */   public static void loadAllHotaItems() {
/* 119 */     long now = System.nanoTime();
/* 120 */     int numberOfItemsLoaded = 0;
/* 121 */     Connection dbcon = null;
/* 122 */     PreparedStatement ps = null;
/* 123 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 126 */       dbcon = DbConnector.getZonesDbCon();
/* 127 */       ps = dbcon.prepareStatement("SELECT * FROM HOTA_ITEMS");
/* 128 */       rs = ps.executeQuery();
/* 129 */       while (rs.next()) {
/*     */         
/* 131 */         byte hotatype = rs.getByte("ITEMTYPE");
/* 132 */         long itemId = rs.getLong("ITEMID");
/*     */         
/*     */         try {
/* 135 */           Item item = Items.getItem(itemId);
/* 136 */           hotaItems.put(item, Byte.valueOf(hotatype));
/* 137 */           numberOfItemsLoaded++;
/*     */         }
/* 139 */         catch (NoSuchItemException nsi) {
/*     */           
/* 141 */           logger.log(Level.WARNING, nsi.getMessage(), (Throwable)nsi);
/*     */         }
/*     */       
/*     */       } 
/* 145 */     } catch (SQLException sqx) {
/*     */       
/* 147 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 151 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 152 */       DbConnector.returnConnection(dbcon);
/* 153 */       float lElapsedTime = (float)(System.nanoTime() - now) / 1000000.0F;
/* 154 */       logger.log(Level.INFO, "Loaded " + numberOfItemsLoaded + " HOTA items. It took " + lElapsedTime + " millis.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void poll() {
/* 161 */     if (Servers.localServer.getNextHota() > 0L)
/*     */     {
/* 163 */       if (System.currentTimeMillis() > Servers.localServer.getNextHota()) {
/*     */         
/* 165 */         FocusZone hotaZone = FocusZone.getHotaZone();
/* 166 */         if (hotaZone != null)
/*     */         {
/* 168 */           if (hotaItems.isEmpty())
/*     */           {
/* 170 */             createHotaItems();
/*     */           }
/* 172 */           putHotaItemsInWorld();
/* 173 */           Servers.localServer.setNextHota(Long.MAX_VALUE);
/* 174 */           String toBroadCast = "The Hunt of the Ancients has begun!";
/* 175 */           switch (Server.rand.nextInt(4)) {
/*     */             
/*     */             case 0:
/* 178 */               toBroadCast = "The Hunt of the Ancients has begun!";
/*     */               break;
/*     */             case 1:
/* 181 */               toBroadCast = "Let The Hunt Begin!";
/*     */               break;
/*     */             case 2:
/* 184 */               toBroadCast = "Hunt! Hunt! Hunt!";
/*     */               break;
/*     */             case 3:
/* 187 */               toBroadCast = "The Hunt of the Ancients is on!";
/*     */               break;
/*     */             case 4:
/* 190 */               if (WurmCalendar.isNight()) {
/* 191 */                 toBroadCast = "It's the night of the Hunter!"; break;
/*     */               } 
/* 193 */               toBroadCast = "It's a glorious day for the Hunt!";
/*     */               break;
/*     */             case 5:
/* 196 */               if (Server.rand.nextInt(100) == 0) {
/*     */                 
/* 198 */                 toBroadCast = "Run, Forrest! Run!";
/*     */                 break;
/*     */               } 
/* 201 */               toBroadCast = "Go conquer those pillars!";
/*     */               break;
/*     */             default:
/* 204 */               toBroadCast = "The Hunt of the Ancients has begun!";
/*     */               break;
/*     */           } 
/* 207 */           Server.getInstance().broadCastSafe(toBroadCast);
/*     */         }
/*     */         else
/*     */         {
/* 211 */           putHotaItemsInVoid();
/* 212 */           Servers.localServer.setNextHota(0L);
/*     */           
/* 214 */           nextRoundMessage = Long.MAX_VALUE;
/*     */         }
/*     */       
/* 217 */       } else if (Servers.localServer.getNextHota() < Long.MAX_VALUE) {
/*     */         
/* 219 */         if (nextRoundMessage == Long.MAX_VALUE)
/*     */         {
/* 221 */           nextRoundMessage = System.currentTimeMillis();
/*     */         }
/* 223 */         long timeLeft = Servers.localServer.getNextHota() - System.currentTimeMillis();
/* 224 */         if (System.currentTimeMillis() >= nextRoundMessage) {
/*     */           
/* 226 */           String one = "The ";
/* 227 */           String two = "Hunt of the Ancients ";
/* 228 */           String three = "begins ";
/* 229 */           if (Server.rand.nextBoolean()) {
/*     */             
/* 231 */             one = "The next ";
/* 232 */             if (Server.rand.nextBoolean())
/*     */             {
/* 234 */               one = "A new ";
/*     */             }
/*     */           } 
/*     */           
/* 238 */           if (Server.rand.nextBoolean()) {
/*     */             
/* 240 */             two = "Hunt ";
/* 241 */             if (Server.rand.nextBoolean())
/*     */             {
/* 243 */               two = "HotA ";
/*     */             }
/*     */           } 
/*     */           
/* 247 */           if (Server.rand.nextBoolean()) {
/*     */             
/* 249 */             three = "starts ";
/* 250 */             if (Server.rand.nextBoolean())
/*     */             {
/* 252 */               three = "will begin ";
/*     */             }
/*     */           } 
/*     */           
/* 256 */           Server.getInstance().broadCastSafe(one + two + three + "in " + Server.getTimeFor(timeLeft) + ".");
/*     */           
/* 258 */           nextRoundMessage = System.currentTimeMillis() + (Servers.localServer.getNextHota() - System.currentTimeMillis()) / 2L;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addPillarConquered(Creature creature, Item pillar) {
/* 266 */     int numsToAdd = 5;
/* 267 */     Integer points = hotaHelpers.get(Long.valueOf(creature.getWurmId()));
/* 268 */     if (points != null)
/* 269 */       numsToAdd = points.intValue() + 5; 
/* 270 */     addHotaHelper(creature, numsToAdd);
/*     */     
/* 272 */     if (!pillarsTouched.contains(pillar)) {
/*     */       
/* 274 */       if (creature.isPlayer())
/*     */       {
/* 276 */         if (!hotaConquerers.contains(Long.valueOf(creature.getWurmId())))
/*     */         {
/* 278 */           hotaConquerers.add(Long.valueOf(creature.getWurmId()));
/*     */         }
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 289 */       if (Servers.localServer.isChallengeServer())
/*     */       {
/* 291 */         if (creature.isPlayer()) {
/*     */           
/* 293 */           ChallengeSummary.addToScore(((Player)creature).getSaveFile(), ChallengePointEnum.ChallengePoint.HOTAPILLARS.getEnumtype(), 1.0F);
/* 294 */           ChallengeSummary.addToScore(((Player)creature).getSaveFile(), ChallengePointEnum.ChallengePoint.OVERALL.getEnumtype(), 5.0F);
/*     */         } 
/*     */       }
/* 297 */       pillarsTouched.add(pillar);
/* 298 */       if (!pillarsLeft.isEmpty()) {
/*     */         
/* 300 */         Item next = pillarsLeft.remove(Server.rand.nextInt(pillarsLeft.size()));
/* 301 */         putPillarInWorld(next);
/*     */       } 
/*     */     } 
/* 304 */     creature.achievement(1);
/* 305 */     Map<Integer, Integer> alliances = new HashMap<>();
/* 306 */     for (Item item : hotaItems.keySet()) {
/*     */       
/* 308 */       if (item.getData1() > 0) {
/*     */         
/* 310 */         Integer nums = alliances.get(Integer.valueOf(item.getData1()));
/* 311 */         if (nums != null) {
/* 312 */           nums = Integer.valueOf(nums.intValue() + 1);
/*     */         } else {
/* 314 */           nums = Integer.valueOf(1);
/* 315 */         }  alliances.put(Integer.valueOf(item.getData1()), nums);
/* 316 */         if (nums.intValue() >= 4)
/*     */         {
/* 318 */           win(item.getData1(), creature);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final LinkedList<Item> getPillarsInWorld() {
/* 326 */     return pillarsTouched;
/*     */   }
/*     */ 
/*     */   
/*     */   static void win(int allianceNumber, Creature winner) {
/* 331 */     PvPAlliance winAlliance = PvPAlliance.getPvPAlliance(allianceNumber);
/*     */     
/* 333 */     if (winAlliance != null) {
/*     */       
/* 335 */       Server.getInstance().broadCastSafe(winner
/* 336 */           .getName() + " has secured victory for " + winAlliance.getName() + "!");
/* 337 */       winAlliance.addHotaWin();
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 344 */         Village v = Villages.getVillage(allianceNumber - 2000000);
/* 345 */         Server.getInstance().broadCastSafe(winner
/* 346 */             .getName() + " has secured victory for " + v.getName() + "!");
/* 347 */         v.addHotaWin();
/* 348 */         v.createHotaPrize(v.getHotaWins());
/*     */       }
/* 350 */       catch (NoSuchVillageException e) {
/*     */ 
/*     */         
/* 353 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*     */         
/* 355 */         Server.getInstance().broadCastSafe(winner
/* 356 */             .getName() + " has secured victory for " + winner.getHimHerItString() + "self!");
/* 357 */         winner.setHotaWins((short)(winner.getHotaWins() + 1));
/*     */       } 
/*     */     } 
/* 360 */     clearHotaHelpers();
/* 361 */     putHotaItemsInVoid();
/* 362 */     Servers.localServer.setNextHota(System.currentTimeMillis() + 129600000L);
/*     */ 
/*     */     
/* 365 */     nextRoundMessage = Servers.isThisATestServer() ? (System.currentTimeMillis() + 60000L) : (System.currentTimeMillis() + 3600000L);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addPillarTouched(Creature creature, Item pillar) {
/* 370 */     int sx = Zones.safeTileX(pillar.getTileX() - 30);
/* 371 */     int sy = Zones.safeTileY(pillar.getTileY() - 30);
/* 372 */     int ex = Zones.safeTileX(pillar.getTileX() + 30);
/* 373 */     int ey = Zones.safeTileY(pillar.getTileY() + 30);
/*     */     
/* 375 */     for (int x = sx; x < ex; x++) {
/*     */       
/* 377 */       for (int y = sy; y < ey; y++) {
/*     */         
/* 379 */         VolaTile t = Zones.getTileOrNull(x, y, creature.isOnSurface());
/* 380 */         if (t != null)
/*     */         {
/* 382 */           for (Creature c : t.getCreatures()) {
/*     */             
/* 384 */             Integer points = hotaHelpers.get(Long.valueOf(c.getWurmId()));
/* 385 */             if (points == null) {
/* 386 */               addHotaHelper(c, 1);
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void putHotaItemsInVoid() {
/* 395 */     pillarsTouched.clear();
/* 396 */     pillarsLeft.clear();
/* 397 */     for (Item item : hotaItems.keySet()) {
/*     */       
/* 399 */       item.deleteAllEffects();
/* 400 */       item.setData1(0);
/* 401 */       if (item.getZoneId() > 0) {
/* 402 */         item.putInVoid();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static final Set<Item> getHotaItems() {
/* 408 */     return hotaItems.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   private static void putHotaItemsInWorld() {
/* 413 */     boolean SaromansEdition = (Server.rand.nextInt(10) > 0);
/* 414 */     FocusZone hotaZone = FocusZone.getHotaZone();
/*     */ 
/*     */     
/* 417 */     if (hotaZone == null) {
/*     */       return;
/*     */     }
/*     */     
/* 421 */     int num = 0;
/* 422 */     int numShrines = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 427 */     for (Item item : hotaItems.keySet()) {
/*     */ 
/*     */       
/* 430 */       item.setData1(0);
/* 431 */       if (item.getZoneId() > 0) {
/*     */ 
/*     */         
/* 434 */         item.deleteAllEffects();
/* 435 */         item.putInVoid();
/*     */       } 
/*     */ 
/*     */       
/* 439 */       int sizeX = hotaZone.getEndX() - hotaZone.getStartX();
/*     */       
/* 441 */       int xborder = sizeX / 10;
/* 442 */       int sizeXSlot = (sizeX - xborder - xborder) / 3;
/* 443 */       logger.log(Level.INFO, "Hota size x " + sizeX + " border=" + xborder + " sizeXSlot=" + sizeXSlot);
/*     */ 
/*     */       
/* 446 */       int sizeY = hotaZone.getEndY() - hotaZone.getStartY();
/*     */       
/* 448 */       int yborder = sizeY / 10;
/* 449 */       int sizeYSlot = (sizeY - yborder - yborder) / 3;
/* 450 */       logger.log(Level.INFO, "Hota size y  " + sizeY + " border=" + yborder + " sizeYSlot=" + sizeYSlot);
/*     */ 
/*     */       
/* 453 */       int tx = Zones.safeTileX(hotaZone.getStartX() + Server.rand
/* 454 */           .nextInt(Math.max(10, sizeX)));
/* 455 */       int ty = Zones.safeTileY(hotaZone.getStartY() + Server.rand
/* 456 */           .nextInt(Math.max(10, sizeY)));
/* 457 */       boolean pillar = false;
/* 458 */       if (item.getTemplateId() == 739) {
/*     */         
/* 460 */         pillar = true;
/*     */         
/* 462 */         tx = Zones.safeTileX(hotaZone.getStartX() + xborder + num % 3 * sizeXSlot + Server.rand
/* 463 */             .nextInt(sizeXSlot));
/* 464 */         if (num < 3) {
/*     */           
/* 466 */           ty = Zones.safeTileY(hotaZone.getStartY() + yborder + Server.rand.nextInt(sizeYSlot));
/*     */         }
/* 468 */         else if (num < 6) {
/*     */           
/* 470 */           ty = Zones.safeTileY(hotaZone.getStartY() + yborder + sizeYSlot + Server.rand.nextInt(sizeYSlot));
/*     */         }
/* 472 */         else if (num < 9) {
/*     */           
/* 474 */           ty = Zones.safeTileY(hotaZone.getStartY() + yborder + 2 * sizeYSlot + Server.rand.nextInt(sizeYSlot));
/*     */         }
/* 476 */         else if (num >= 9) {
/*     */ 
/*     */           
/* 479 */           tx = Zones.safeTileX(hotaZone.getStartX() + xborder + Server.rand
/* 480 */               .nextInt(sizeXSlot * 3));
/* 481 */           ty = Zones.safeTileY(hotaZone.getStartY() + yborder + Server.rand
/* 482 */               .nextInt(sizeYSlot * 3));
/*     */         } 
/* 484 */         num++;
/*     */       }
/* 486 */       else if (item.getTemplateId() == 741) {
/*     */ 
/*     */         
/* 489 */         tx = Math.max(0, 
/* 490 */             Zones.safeTileX(hotaZone.getStartX() + xborder + sizeXSlot / 2 + numShrines % 2 * sizeXSlot + Server.rand
/* 491 */               .nextInt(sizeXSlot)));
/* 492 */         if (numShrines < 2) {
/* 493 */           ty = Zones.safeTileY(hotaZone.getStartY() + yborder + sizeYSlot / 2 + Server.rand
/* 494 */               .nextInt(sizeXSlot));
/*     */         } else {
/* 496 */           ty = Zones.safeTileY(hotaZone.getStartY() + yborder + sizeYSlot / 2 + sizeYSlot + Server.rand
/* 497 */               .nextInt(sizeXSlot));
/* 498 */         }  numShrines++;
/*     */       } 
/* 500 */       float posx = ((tx << 2) + 2);
/* 501 */       float posy = ((ty << 2) + 2);
/*     */       
/*     */       try {
/* 504 */         item.setPosXYZ(posx, posy, Zones.calculateHeight(posx, posy, true));
/*     */         
/* 506 */         if (!pillar || !SaromansEdition) {
/*     */ 
/*     */           
/* 509 */           putPillarInWorld(item);
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 514 */           pillarsLeft.add(item);
/*     */         } 
/*     */         
/* 517 */         logger.log(Level.INFO, item.getName() + " " + num + "(" + numShrines + ") put at " + tx + "," + ty + " num % 3 =" + (num % 3));
/*     */       
/*     */       }
/* 520 */       catch (NoSuchZoneException nsz) {
/*     */         
/* 522 */         logger.log(Level.INFO, "Item " + item
/* 523 */             .getWurmId() + " outside range " + item.getPosX() + " " + item.getPosY());
/*     */       } 
/*     */     } 
/*     */     
/* 527 */     if (!pillarsLeft.isEmpty()) {
/*     */       
/* 529 */       Item next = pillarsLeft.remove(Server.rand.nextInt(pillarsLeft.size()));
/* 530 */       putPillarInWorld(next);
/* 531 */       Item next2 = pillarsLeft.remove(Server.rand.nextInt(pillarsLeft.size()));
/* 532 */       putPillarInWorld(next2);
/* 533 */       Item next3 = pillarsLeft.remove(Server.rand.nextInt(pillarsLeft.size()));
/* 534 */       putPillarInWorld(next3);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void putPillarInWorld(Item pillar) {
/*     */     try {
/* 542 */       Zone z = Zones.getZone((int)pillar.getPosX() >> 2, (int)pillar.getPosY() >> 2, true);
/* 543 */       z.addItem(pillar);
/* 544 */       logger.log(Level.INFO, pillar.getName() + " spawned at " + pillar.getTileX() + "," + pillar.getTileY());
/*     */     }
/* 546 */     catch (NoSuchZoneException nsz) {
/*     */       
/* 548 */       logger.log(Level.INFO, "Pillar " + pillar
/* 549 */           .getWurmId() + " outside range " + pillar.getPosX() + " " + pillar.getPosY());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void forcePillarsToWorld() {
/* 560 */     if (Servers.localServer.isShuttingDownIn < 30 && Servers.localServer.getNextHota() == Long.MAX_VALUE) {
/*     */       
/* 562 */       logger.warning("Forcing all remaining pillars to spawn into the world");
/* 563 */       while (!pillarsLeft.isEmpty())
/*     */       {
/*     */         
/* 566 */         Item next = pillarsLeft.removeLast();
/* 567 */         logger.fine("Putting " + next.getName() + " in the world!");
/* 568 */         putPillarInWorld(next);
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 573 */     else if (!Servers.localServer.maintaining) {
/*     */       
/* 575 */       logger.warning("Something just tried to force all HoTA pillars to spawn when not appropriate");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void createHotaItems() {
/* 585 */     logger.info("Creating Hunt of the Ancients items.");
/*     */     
/*     */     try {
/* 588 */       Item pillarOne = ItemFactory.createItem(739, 90.0F, null);
/* 589 */       pillarOne.setName("Green pillar of the hunt");
/* 590 */       pillarOne.setColor(WurmColor.createColor(0, 255, 0));
/* 591 */       hotaItems.put(pillarOne, Byte.valueOf((byte)1));
/* 592 */       insertHotaItem(pillarOne, (byte)1);
/*     */       
/* 594 */       Item pillarTwo = ItemFactory.createItem(739, 90.0F, null);
/* 595 */       pillarTwo.setName("Blue pillar of the hunt");
/* 596 */       pillarTwo.setColor(WurmColor.createColor(0, 0, 255));
/* 597 */       hotaItems.put(pillarTwo, Byte.valueOf((byte)1));
/* 598 */       insertHotaItem(pillarTwo, (byte)1);
/*     */       
/* 600 */       Item pillarThree = ItemFactory.createItem(739, 90.0F, null);
/* 601 */       pillarThree.setName("Red pillar of the hunt");
/* 602 */       pillarThree.setColor(WurmColor.createColor(255, 0, 0));
/* 603 */       hotaItems.put(pillarThree, Byte.valueOf((byte)1));
/* 604 */       insertHotaItem(pillarThree, (byte)1);
/*     */       
/* 606 */       Item pillarFour = ItemFactory.createItem(739, 90.0F, null);
/* 607 */       pillarFour.setName("Yellow pillar of the hunt");
/* 608 */       pillarFour.setColor(WurmColor.createColor(238, 244, 6));
/* 609 */       hotaItems.put(pillarFour, Byte.valueOf((byte)1));
/* 610 */       insertHotaItem(pillarFour, (byte)1);
/*     */       
/* 612 */       Item pillarFive = ItemFactory.createItem(739, 90.0F, null);
/* 613 */       pillarFive.setName("Sky pillar of the hunt");
/* 614 */       pillarFive.setColor(WurmColor.createColor(33, 208, 218));
/* 615 */       hotaItems.put(pillarFive, Byte.valueOf((byte)1));
/* 616 */       insertHotaItem(pillarFive, (byte)1);
/*     */       
/* 618 */       Item pillarSix = ItemFactory.createItem(739, 90.0F, null);
/* 619 */       pillarSix.setName("Black pillar of the hunt");
/* 620 */       pillarSix.setColor(WurmColor.createColor(0, 0, 0));
/* 621 */       hotaItems.put(pillarSix, Byte.valueOf((byte)1));
/* 622 */       insertHotaItem(pillarSix, (byte)1);
/*     */       
/* 624 */       Item pillarSeven = ItemFactory.createItem(739, 90.0F, null);
/* 625 */       pillarSeven.setName("Clear pillar of the hunt");
/* 626 */       pillarSix.setColor(WurmColor.createColor(255, 255, 255));
/* 627 */       hotaItems.put(pillarSeven, Byte.valueOf((byte)1));
/* 628 */       insertHotaItem(pillarSeven, (byte)1);
/*     */       
/* 630 */       Item pillarEight = ItemFactory.createItem(739, 90.0F, null);
/* 631 */       pillarEight.setName("Brown pillar of the hunt");
/* 632 */       pillarEight.setColor(WurmColor.createColor(154, 88, 22));
/* 633 */       hotaItems.put(pillarEight, Byte.valueOf((byte)1));
/* 634 */       insertHotaItem(pillarEight, (byte)1);
/*     */       
/* 636 */       Item pillarNine = ItemFactory.createItem(739, 90.0F, null);
/* 637 */       pillarNine.setName("Lilac pillar of the hunt");
/* 638 */       pillarNine.setColor(WurmColor.createColor(134, 69, 186));
/* 639 */       hotaItems.put(pillarNine, Byte.valueOf((byte)1));
/* 640 */       insertHotaItem(pillarNine, (byte)1);
/*     */       
/* 642 */       Item pillarTen = ItemFactory.createItem(739, 90.0F, null);
/* 643 */       pillarTen.setName("Orange pillar of the hunt");
/* 644 */       pillarTen.setColor(WurmColor.createColor(255, 128, 186));
/* 645 */       hotaItems.put(pillarTen, Byte.valueOf((byte)1));
/* 646 */       insertHotaItem(pillarTen, (byte)1);
/*     */       
/* 648 */       Item shrineOne = ItemFactory.createItem(741, 90.0F, null);
/* 649 */       hotaItems.put(shrineOne, Byte.valueOf((byte)2));
/* 650 */       insertHotaItem(shrineOne, (byte)2);
/*     */       
/* 652 */       Item shrineTwo = ItemFactory.createItem(741, 90.0F, null);
/* 653 */       hotaItems.put(shrineTwo, Byte.valueOf((byte)2));
/* 654 */       insertHotaItem(shrineTwo, (byte)2);
/*     */       
/* 656 */       Item shrineThree = ItemFactory.createItem(741, 90.0F, null);
/* 657 */       hotaItems.put(shrineThree, Byte.valueOf((byte)2));
/* 658 */       insertHotaItem(shrineThree, (byte)2);
/*     */       
/* 660 */       Item shrineFour = ItemFactory.createItem(741, 90.0F, null);
/* 661 */       hotaItems.put(shrineFour, Byte.valueOf((byte)2));
/* 662 */       insertHotaItem(shrineFour, (byte)2);
/*     */     }
/* 664 */     catch (NoSuchTemplateException nst) {
/*     */       
/* 666 */       logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*     */     }
/* 668 */     catch (FailedException fe) {
/*     */       
/* 670 */       logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*     */     }
/*     */     finally {
/*     */       
/* 674 */       logger.info("Finished creating Hunt of the Ancients items.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void loadAllHelpers() {
/* 680 */     long now = System.nanoTime();
/* 681 */     int numberOfHelpersLoaded = 0;
/* 682 */     Connection dbcon = null;
/* 683 */     PreparedStatement ps = null;
/* 684 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 687 */       dbcon = DbConnector.getZonesDbCon();
/* 688 */       ps = dbcon.prepareStatement("SELECT * FROM HOTA_HELPERS");
/* 689 */       rs = ps.executeQuery();
/* 690 */       while (rs.next())
/*     */       {
/* 692 */         int conquerValue = rs.getInt("CONQUERS");
/* 693 */         long wid = rs.getLong("WURMID");
/* 694 */         hotaHelpers.put(Long.valueOf(wid), Integer.valueOf(conquerValue));
/* 695 */         numberOfHelpersLoaded++;
/*     */       }
/*     */     
/* 698 */     } catch (SQLException sqx) {
/*     */       
/* 700 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 704 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 705 */       DbConnector.returnConnection(dbcon);
/* 706 */       float lElapsedTime = (float)(System.nanoTime() - now) / 1000000.0F;
/* 707 */       logger.log(Level.INFO, "Loaded " + numberOfHelpersLoaded + " HOTA helpers. It took " + lElapsedTime + " millis.");
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
/*     */   public static int getHelpValue(long creatureId) {
/* 721 */     Integer helped = hotaHelpers.get(Long.valueOf(creatureId));
/* 722 */     if (helped != null)
/* 723 */       return helped.intValue(); 
/* 724 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   static void addHotaHelper(Creature creature, int helpValue) {
/* 729 */     Connection dbcon = null;
/* 730 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 733 */       dbcon = DbConnector.getZonesDbCon();
/* 734 */       String updateOrInsert = "INSERT INTO HOTA_HELPERS (CONQUERS,WURMID) VALUES (?,?)";
/* 735 */       Integer helped = hotaHelpers.get(Long.valueOf(creature.getWurmId()));
/* 736 */       if (helped != null)
/* 737 */         updateOrInsert = "UPDATE HOTA_HELPERS SET CONQUERS=? WHERE WURMID=?"; 
/* 738 */       hotaHelpers.put(Long.valueOf(creature.getWurmId()), Integer.valueOf(helpValue));
/* 739 */       ps = dbcon.prepareStatement(updateOrInsert);
/* 740 */       ps.setInt(1, helpValue);
/* 741 */       ps.setLong(2, creature.getWurmId());
/* 742 */       ps.executeUpdate();
/*     */     }
/* 744 */     catch (SQLException sqx) {
/*     */       
/* 746 */       logger.log(Level.WARNING, "Failed to update Hota helper: " + creature.getName() + " with value " + helpValue, sqx);
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 751 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 752 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void destroyHota() {
/* 758 */     for (Item i : hotaItems.keySet())
/*     */     {
/* 760 */       Items.destroyItem(i.getWurmId());
/*     */     }
/* 762 */     hotaItems.clear();
/* 763 */     clearHotaHelpers();
/* 764 */     Connection dbcon = null;
/* 765 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 768 */       dbcon = DbConnector.getZonesDbCon();
/* 769 */       ps = dbcon.prepareStatement("DELETE FROM HOTA_ITEMS");
/* 770 */       ps.executeUpdate();
/*     */     }
/* 772 */     catch (SQLException sqx) {
/*     */       
/* 774 */       logger.log(Level.WARNING, "Failed to delete hota items", sqx);
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 779 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 780 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 782 */     Servers.localServer.setNextHota(0L);
/* 783 */     nextRoundMessage = Long.MAX_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   static void clearHotaHelpers() {
/* 788 */     hotaHelpers.clear();
/* 789 */     hotaConquerers.clear();
/* 790 */     Connection dbcon = null;
/* 791 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 794 */       dbcon = DbConnector.getZonesDbCon();
/*     */       
/* 796 */       ps = dbcon.prepareStatement("DELETE FROM HOTA_HELPERS");
/* 797 */       ps.executeUpdate();
/*     */     }
/* 799 */     catch (SQLException sqx) {
/*     */       
/* 801 */       logger.log(Level.WARNING, "Failed to delete all Hota helpers: ", sqx);
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 806 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 807 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void insertHotaItem(Item item, byte type) {
/* 813 */     Connection dbcon = null;
/* 814 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 817 */       dbcon = DbConnector.getZonesDbCon();
/* 818 */       ps = dbcon.prepareStatement("INSERT INTO HOTA_ITEMS (ITEMID,ITEMTYPE) VALUES (?,?)");
/* 819 */       ps.setLong(1, item.getWurmId());
/* 820 */       ps.setByte(2, type);
/* 821 */       ps.executeUpdate();
/*     */     }
/* 823 */     catch (SQLException sqx) {
/*     */       
/* 825 */       logger.log(Level.WARNING, "Failed to insert hota item id " + item.getWurmId() + " - " + item.getName() + " and type " + type, sqx);
/*     */     
/*     */     }
/*     */     finally {
/*     */ 
/*     */       
/* 831 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 832 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\epic\Hota.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */