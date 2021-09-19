/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.Random;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
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
/*     */ public class WurmHarvestables
/*     */   implements TimeConstants, MiscConstants
/*     */ {
/*  45 */   private static final Logger logger = Logger.getLogger(WurmHarvestables.class.getName());
/*     */   
/*     */   public static final int NONE_ID = 0;
/*     */   
/*     */   public static final int OLIVE_ID = 1;
/*     */   
/*     */   public static final int GRAPE_ID = 2;
/*     */   
/*     */   public static final int CHERRY_ID = 3;
/*     */   
/*     */   public static final int APPLE_ID = 4;
/*     */   
/*     */   public static final int LEMON_ID = 5;
/*     */   public static final int OLEANDER_ID = 6;
/*     */   public static final int CAMELLIA_ID = 7;
/*     */   public static final int LAVENDER_ID = 8;
/*     */   public static final int MAPLE_ID = 9;
/*     */   public static final int ROSE_ID = 10;
/*     */   public static final int CHESTNUT_ID = 11;
/*     */   public static final int WALNUT_ID = 12;
/*     */   public static final int PINE_ID = 13;
/*     */   public static final int HAZEL_ID = 14;
/*     */   public static final int HOPS_ID = 15;
/*     */   public static final int OAK_ID = 16;
/*     */   public static final int ORANGE_ID = 17;
/*     */   public static final int RASPBERRY_ID = 18;
/*     */   public static final int BLUEBERRY_ID = 19;
/*     */   public static final int LINGONBERRY_ID = 20;
/*     */   public static final int MAX_HARVEST_ID = 20;
/*     */   private static final String GET_CALENDAR_HARVEST_EVENTS = "SELECT * FROM CALENDAR WHERE type = 1";
/*     */   private static final String INSERT_CALENDAR_HARVEST_EVENT = "INSERT INTO CALENDAR (eventid, starttime, type) VALUES (?,?,1)";
/*     */   private static final String UPDATE_CALENDAR_HARVEST_EVENT = "UPDATE CALENDAR SET starttime = ? where eventid = ? and type = 1";
/*  77 */   private static final Harvestable[] harvestables = new Harvestable[21];
/*     */   
/*  79 */   public static long lastHarvestableCheck = 0L;
/*  80 */   public static final Random endRand = new Random();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Harvestable
/*     */   {
/*  91 */     NONE(0, "none", 0, 0, (byte)-1, (byte)-1, -1, "", ""),
/*  92 */     OLIVE(1, "olive", 7, 0, (byte)108, (byte)122, -1, "olive", "an olive"),
/*     */     
/*  94 */     GRAPE(2, "grape", 8, 0, (byte)-111, (byte)-105, 920, "grape", "a grape"),
/*     */     
/*  96 */     CHERRY(3, "cherry", 6, 0, (byte)109, (byte)123, -1, "cherry", "a cherry"),
/*     */     
/*  98 */     APPLE(4, "apple", 8, 2, (byte)106, (byte)120, -1, "apple", "an apple"),
/*     */     
/* 100 */     LEMON(5, "lemon", 8, 1, (byte)107, (byte)121, -1, "lemon", "a lemon"),
/*     */     
/* 102 */     OLEANDER(6, "oleander", 3, 1, (byte)-109, (byte)-103, -1, "oleander leaf", "an oleander leaf"),
/*     */     
/* 104 */     CAMELLIA(7, "camellia", 3, 3, (byte)-110, (byte)-104, -1, "camellia leaf", "a camellia leaf"),
/*     */     
/* 106 */     LAVENDER(8, "lavender", 4, 1, (byte)-114, (byte)-108, -1, "lavender flower", "a lavender flower"),
/*     */     
/* 108 */     MAPLE(9, "maple", 4, 3, (byte)105, (byte)119, -1, "maple sap", "some maple sap"),
/*     */     
/* 110 */     ROSE(10, "rose", 4, 2, (byte)-113, (byte)-107, 1018, "rose flower", "a rose flower"),
/*     */     
/* 112 */     CHESTNUT(11, "chestnut", 8, 3, (byte)110, (byte)124, -1, "chestnut", "a chestnut"),
/*     */     
/* 114 */     WALNUT(12, "walnut", 9, 1, (byte)111, (byte)125, -1, "walnut", "a walnut"),
/*     */     
/* 116 */     PINE(13, "pine", 0, 0, (byte)101, (byte)115, -1, "pinenut", "a pinenut"),
/*     */     
/* 118 */     HAZEL(14, "hazel", 9, 2, (byte)-96, (byte)-95, -1, "hazelnut", "a hazelnut"),
/*     */     
/* 120 */     HOPS(15, "hops", 7, 2, (byte)-1, (byte)-1, 1274, "hops", "some hops"),
/* 121 */     OAK(16, "oak", 5, 1, (byte)102, (byte)116, -1, "acorn", "an acorn"),
/*     */     
/* 123 */     ORANGE(17, "orange", 7, 3, (byte)-93, (byte)-92, -1, "orange", "an orange"),
/*     */     
/* 125 */     RASPBERRY(18, "raspberry", 9, 0, (byte)-90, (byte)-89, -1, "raspberry", "a raspberry"),
/*     */     
/* 127 */     BLUEBERRY(19, "blueberry", 7, 1, (byte)-87, (byte)-86, -1, "blueberry", "a blueberry"),
/*     */     
/* 129 */     LINGONBERRY(20, "lingonberry", 9, 3, (byte)-84, (byte)-84, -1, "lingonberry", "a lingonberry");
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
/* 143 */     private long seasonStart = Long.MAX_VALUE;
/* 144 */     private long seasonEnd = Long.MAX_VALUE; private boolean isHarvestable = false; private final int harvestableId;
/*     */     private final String name;
/*     */     private final int month;
/*     */     private final int week;
/*     */     
/*     */     Harvestable(int id, String name, int month, int week, byte tileNormal, byte tileMycelium, int trellis, String fruit, String fruitWithGenus) {
/* 150 */       this.harvestableId = id;
/* 151 */       this.name = name;
/* 152 */       this.month = month;
/* 153 */       this.week = week;
/* 154 */       this.tileNormal = tileNormal;
/* 155 */       this.tileMycelium = tileMycelium;
/* 156 */       this.trellis = trellis;
/* 157 */       this.fruit = fruit;
/* 158 */       this.fruitWithGenus = fruitWithGenus;
/* 159 */       if (tileNormal > 0) {
/*     */ 
/*     */         
/* 162 */         Tiles.Tile tile = Tiles.getTile(tileNormal);
/* 163 */         if (tile != null) {
/* 164 */           this.reportDifficulty = tile.getWoodDificulity();
/*     */         } else {
/* 166 */           this.reportDifficulty = 2;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 171 */         this.reportDifficulty = 2;
/*     */       } 
/*     */       
/* 174 */       if (id < 0 || id > 20) {
/* 175 */         WurmHarvestables.logger.severe("Invalid Harvest Id " + id);
/*     */       } else {
/* 177 */         WurmHarvestables.harvestables[id] = this;
/*     */       } 
/*     */     }
/*     */     private final byte tileNormal; private final byte tileMycelium; private final int trellis; private final int reportDifficulty; private final String fruit; private final String fruitWithGenus;
/*     */     public int getHarvestableId() {
/* 182 */       return this.harvestableId;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 187 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getHarvestableWithDates() {
/* 192 */       if (this.isHarvestable)
/*     */       {
/* 194 */         return this.name + ", season ends in " + WurmCalendar.getDaysFrom(getSeasonEnd());
/*     */       }
/* 196 */       return "";
/*     */     }
/*     */ 
/*     */     
/*     */     public String getState() {
/* 201 */       if (isHarvestable())
/* 202 */         return "Harvestable"; 
/* 203 */       if (isAlmostRipe())
/* 204 */         return "Almost Ripe"; 
/* 205 */       return "";
/*     */     }
/*     */ 
/*     */     
/*     */     public String getFruit() {
/* 210 */       return this.fruit;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getFruitWithGenus() {
/* 215 */       return this.fruitWithGenus;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getSeasonStart() {
/* 220 */       return this.seasonStart;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getSeasonEnd() {
/* 225 */       return this.seasonEnd;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSeasonStart(long newGrowth, boolean updateHarvestables) {
/* 230 */       this.seasonStart = newGrowth;
/* 231 */       boolean harvestable = (WurmCalendar.getCurrentTime() > this.seasonStart);
/* 232 */       if (harvestable != this.isHarvestable) {
/*     */         
/* 234 */         this.isHarvestable = harvestable;
/* 235 */         if (updateHarvestables)
/*     */         {
/*     */           
/* 238 */           updateHarvestables(harvestable);
/*     */         }
/*     */       } 
/*     */       
/* 242 */       WurmHarvestables.endRand.setSeed(this.seasonStart);
/*     */       
/* 244 */       int diff = Math.min(7, this.reportDifficulty);
/* 245 */       int adjust = WurmHarvestables.endRand.nextInt((int)(diff * 86400L));
/* 246 */       this.seasonEnd = getSeasonStart() + 2419200L - adjust;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isHarvestable() {
/* 251 */       return this.isHarvestable;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void calcHarvestStart(int yearOffset) {
/* 258 */       long rDay = 259200L + Server.rand.nextInt(604800) + Server.rand.nextInt(604800) - Server.rand.nextInt(604800) - Server.rand.nextInt(604800);
/* 259 */       long startWeek = yearOffset * 29030400L + this.month * 2419200L + this.week * 604800L;
/*     */       
/* 261 */       setSeasonStart(startWeek + rDay, true);
/* 262 */       WurmHarvestables.dbUpdateHarvestEvent(this.harvestableId, this.seasonStart);
/*     */     }
/*     */ 
/*     */     
/*     */     public long getDefaultSeasonStart() {
/* 267 */       int yearOffset = (int)(this.seasonStart / 29030400L);
/* 268 */       return yearOffset * 29030400L + this.month * 2419200L + this.week * 604800L;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getDefaultSeasonEnd() {
/* 273 */       return getDefaultSeasonStart() + 2419200L;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean isHarvestOver() {
/* 278 */       if (this.harvestableId == 0)
/* 279 */         return false; 
/* 280 */       if (WurmHarvestables.lastHarvestableCheck > getSeasonEnd()) {
/*     */         
/* 282 */         calcHarvestStart(WurmCalendar.getYearOffset() + 1);
/*     */         
/* 284 */         updateHarvestables(false);
/* 285 */         return true;
/*     */       } 
/* 287 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean hasSeasonStarted() {
/* 292 */       if (this.harvestableId == 0)
/* 293 */         return false; 
/* 294 */       if (!this.isHarvestable && WurmHarvestables.lastHarvestableCheck > this.seasonStart) {
/*     */         
/* 296 */         updateHarvestables(true);
/* 297 */         return true;
/*     */       } 
/* 299 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     private void updateHarvestables(boolean harvestable) {
/* 304 */       this.isHarvestable = harvestable;
/* 305 */       if (this.tileNormal != -1)
/* 306 */         WurmHarvestables.setHarvestable(this.tileNormal, this.tileMycelium, this.isHarvestable); 
/* 307 */       if (this.trellis > 0) {
/* 308 */         WurmHarvestables.setHarvestable(this.trellis, this.isHarvestable);
/*     */       }
/*     */     }
/*     */     
/*     */     public String getHarvestEvent() {
/* 313 */       return "start " + this.name + " season: " + (this.isHarvestable ? "(harvestable) " : "") + this.seasonStart + " - " + WurmCalendar.getTimeFor(this.seasonStart);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isAlmostRipe() {
/* 318 */       return (WurmCalendar.currentTime >= getSeasonStart() - 2419200L && WurmCalendar.currentTime < 
/* 319 */         getSeasonStart());
/*     */     }
/*     */ 
/*     */     
/*     */     public int getReportDifficulty() {
/* 324 */       return this.reportDifficulty;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSap() {
/* 329 */       return (this.harvestableId == 9);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isFlower() {
/* 334 */       return (this.harvestableId == 8 || this.harvestableId == 10);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isLeaf() {
/* 339 */       return (this.harvestableId == 7 || this.harvestableId == 6);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isBerry() {
/* 344 */       return (this.harvestableId == 18 || this.harvestableId == 19 || this.harvestableId == 20);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isNut() {
/* 349 */       return (this.harvestableId == 11 || this.harvestableId == 12 || this.harvestableId == 13 || this.harvestableId == 14);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isFruit() {
/* 355 */       return (this.harvestableId == 1 || this.harvestableId == 2 || this.harvestableId == 3 || this.harvestableId == 4 || this.harvestableId == 5 || this.harvestableId == 17);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isHops() {
/* 362 */       return (this.harvestableId == 15);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isAcorn() {
/* 367 */       return (this.harvestableId == 16);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Harvestable getHarvestable(int id) {
/* 377 */     if (id == 0)
/* 378 */       return null; 
/* 379 */     if (id < 1 || id > 20) {
/*     */       
/* 381 */       logger.severe("Invalid Harvest Id " + id);
/* 382 */       return null;
/*     */     } 
/* 384 */     return harvestables[id];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getMaxHarvestId() {
/* 394 */     return 20;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static long getLastHarvestableCheck() {
/* 404 */     return lastHarvestableCheck;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setHarvestStart(int eventId, long newDate) {
/* 409 */     Harvestable harvestable = getHarvestable(eventId);
/* 410 */     if (harvestable != null) {
/*     */       
/* 412 */       harvestable.setSeasonStart(newDate, true);
/* 413 */       dbUpdateHarvestEvent(eventId, newDate);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void dbLoadHarvestStartTimes() {
/* 422 */     Connection dbcon = null;
/* 423 */     Statement stmt = null;
/* 424 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 427 */       dbcon = DbConnector.getItemDbCon();
/* 428 */       stmt = dbcon.createStatement();
/* 429 */       rs = stmt.executeQuery("SELECT * FROM CALENDAR WHERE type = 1");
/* 430 */       while (rs.next())
/*     */       {
/* 432 */         int lEventId = rs.getInt("eventid");
/*     */ 
/*     */         
/* 435 */         long tStartTime = rs.getLong("starttime");
/*     */ 
/*     */         
/* 438 */         boolean recalc = (tStartTime > (WurmCalendar.getYearOffset() + 1) * 29030400L + (WurmCalendar.getStarfall() + 1) * 2419200L);
/*     */ 
/*     */         
/* 441 */         long lStartTime = Math.max(WurmCalendar.getYearOffset() * 29030400L, tStartTime);
/*     */         
/* 443 */         if (logger.isLoggable(Level.FINEST))
/*     */         {
/* 445 */           logger.finest("Loading harvest calendar event - Id: " + lEventId + ", start: " + lStartTime);
/*     */         }
/* 447 */         Harvestable harvestable = getHarvestable(lEventId);
/* 448 */         if (harvestable != null) {
/*     */           
/* 450 */           if (recalc) {
/* 451 */             harvestable.calcHarvestStart(WurmCalendar.getYearOffset()); continue;
/*     */           } 
/* 453 */           harvestable.setSeasonStart(lStartTime, false); continue;
/*     */         } 
/* 455 */         if (lEventId == 0) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 460 */         logger.warning("Unknown harvest event in the Calendar: " + lEventId + ", start: " + lStartTime);
/*     */       }
/*     */     
/* 463 */     } catch (SQLException ex) {
/*     */       
/* 465 */       logger.log(Level.WARNING, "Failed to load harvest events from the calendar", ex);
/*     */     }
/*     */     finally {
/*     */       
/* 469 */       DbUtilities.closeDatabaseObjects(stmt, rs);
/* 470 */       DbConnector.returnConnection(dbcon);
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
/*     */   private static void dbUpdateHarvestEvent(int aEventId, long aStartTime) {
/* 482 */     Connection dbcon = null;
/* 483 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 486 */       dbcon = DbConnector.getItemDbCon();
/* 487 */       ps = dbcon.prepareStatement("UPDATE CALENDAR SET starttime = ? where eventid = ? and type = 1");
/* 488 */       ps.setLong(1, Math.max(0L, aStartTime));
/* 489 */       ps.setLong(2, aEventId);
/*     */       
/* 491 */       if (ps.executeUpdate() == 0)
/*     */       {
/*     */         
/* 494 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 495 */         ps = dbcon.prepareStatement("INSERT INTO CALENDAR (eventid, starttime, type) VALUES (?,?,1)");
/* 496 */         ps.setLong(1, aEventId);
/* 497 */         ps.setLong(2, aStartTime);
/* 498 */         ps.executeUpdate();
/*     */       }
/*     */     
/* 501 */     } catch (SQLException ex) {
/*     */       
/* 503 */       logger.log(Level.WARNING, "Failed to update harvest event to calendar with event id " + aEventId + ", startTime: " + aStartTime, ex);
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 508 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 509 */       DbConnector.returnConnection(dbcon);
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
/*     */   public static final void setStartTimes() {
/* 523 */     long start = System.nanoTime();
/* 524 */     boolean forceEnumLoad = Harvestable.APPLE.isHarvestable();
/* 525 */     dbLoadHarvestStartTimes();
/*     */ 
/*     */     
/* 528 */     for (Harvestable harvestable : Harvestable.values()) {
/*     */       
/* 530 */       if (harvestable != Harvestable.NONE && harvestable.getSeasonStart() > WurmCalendar.currentTime + 29030400L + 2419200L)
/*     */       {
/*     */         
/* 533 */         harvestable.calcHarvestStart(WurmCalendar.getYearOffset());
/*     */       }
/*     */     } 
/* 536 */     logGrowthStartDates();
/*     */     
/* 538 */     float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 539 */     logger.log(Level.INFO, "Set harvest start dates. It took " + lElapsedTime + " millis.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void logGrowthStartDates() {
/* 547 */     StringBuilder buf = new StringBuilder();
/* 548 */     buf.append("Current wurm time: ").append(WurmCalendar.currentTime).append(" - ").append(WurmCalendar.getTime());
/* 549 */     buf.append("\n" + Harvestable.APPLE.getHarvestEvent());
/* 550 */     buf.append("\n" + Harvestable.BLUEBERRY.getHarvestEvent());
/* 551 */     buf.append("\n" + Harvestable.CAMELLIA.getHarvestEvent());
/* 552 */     buf.append("\n" + Harvestable.CHERRY.getHarvestEvent());
/* 553 */     buf.append("\n" + Harvestable.CHESTNUT.getHarvestEvent());
/* 554 */     buf.append("\n" + Harvestable.GRAPE.getHarvestEvent());
/* 555 */     buf.append("\n" + Harvestable.HAZEL.getHarvestEvent());
/* 556 */     buf.append("\n" + Harvestable.HOPS.getHarvestEvent());
/* 557 */     buf.append("\n" + Harvestable.LAVENDER.getHarvestEvent());
/* 558 */     buf.append("\n" + Harvestable.LEMON.getHarvestEvent());
/* 559 */     buf.append("\n" + Harvestable.LINGONBERRY.getHarvestEvent());
/* 560 */     buf.append("\n" + Harvestable.MAPLE.getHarvestEvent());
/* 561 */     buf.append("\n" + Harvestable.OAK.getHarvestEvent());
/* 562 */     buf.append("\n" + Harvestable.OLEANDER.getHarvestEvent());
/* 563 */     buf.append("\n" + Harvestable.OLIVE.getHarvestEvent());
/* 564 */     buf.append("\n" + Harvestable.ORANGE.getHarvestEvent());
/* 565 */     buf.append("\n" + Harvestable.PINE.getHarvestEvent());
/* 566 */     buf.append("\n" + Harvestable.RASPBERRY.getHarvestEvent());
/* 567 */     buf.append("\n" + Harvestable.ROSE.getHarvestEvent());
/* 568 */     buf.append("\n" + Harvestable.WALNUT.getHarvestEvent());
/* 569 */     logger.log(Level.INFO, buf.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkHarvestables(long currentTime) {
/* 577 */     boolean haveDatesChanged = false;
/*     */     
/* 579 */     if (currentTime < lastHarvestableCheck + 3600L) {
/*     */       return;
/*     */     }
/* 582 */     lastHarvestableCheck = WurmCalendar.currentTime;
/*     */     
/* 584 */     for (Harvestable harvestable : harvestables) {
/*     */       
/* 586 */       haveDatesChanged = (haveDatesChanged || harvestable.isHarvestOver());
/* 587 */       haveDatesChanged = (haveDatesChanged || harvestable.hasSeasonStarted());
/*     */     } 
/* 589 */     if (haveDatesChanged)
/*     */     {
/* 591 */       logGrowthStartDates();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void setHarvestable(byte normalType, byte myceliumType, boolean harvestable) {
/* 597 */     int min = 1;
/* 598 */     int ms = Constants.meshSize;
/* 599 */     int max = (1 << ms) - 1;
/*     */     
/* 601 */     for (int x = 1; x < max; x++) {
/*     */       
/* 603 */       for (int y = 1; y < max; y++) {
/*     */         
/* 605 */         int encodedTile = Server.surfaceMesh.getTile(x, y);
/* 606 */         byte tileType = Tiles.decodeType(encodedTile);
/* 607 */         if (tileType == normalType || tileType == myceliumType) {
/*     */ 
/*     */           
/* 610 */           short newHeight = Tiles.decodeHeight(encodedTile);
/* 611 */           byte tileData = Tiles.decodeData(encodedTile);
/* 612 */           if (harvestable) {
/* 613 */             tileData = (byte)(tileData | 0x8);
/*     */           } else {
/* 615 */             tileData = (byte)(tileData & 0xF7);
/* 616 */           }  Server.setSurfaceTile(x, y, newHeight, tileType, tileData);
/* 617 */           Players.getInstance().sendChangedTile(x, y, true, false);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void setHarvestable(int itemType, boolean harvestable) {
/* 626 */     for (Item item : Items.getHarvestableItems()) {
/*     */       
/* 628 */       if (item.getTemplateId() == itemType) {
/* 629 */         item.setHarvestable(harvestable);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Harvestable[] getHarvestables() {
/* 635 */     return harvestables;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getHarvestableIdFromTile(byte tileType) {
/* 640 */     for (Harvestable harvestable : harvestables) {
/*     */       
/* 642 */       if (harvestable.tileNormal == tileType || harvestable.tileMycelium == tileType)
/*     */       {
/* 644 */         return harvestable.harvestableId;
/*     */       }
/*     */     } 
/* 647 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getHarvestableIdFromTrellis(int trellis) {
/* 652 */     for (Harvestable harvestable : harvestables) {
/*     */       
/* 654 */       if (harvestable.trellis == trellis)
/*     */       {
/* 656 */         return harvestable.harvestableId;
/*     */       }
/*     */     } 
/* 659 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\WurmHarvestables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */