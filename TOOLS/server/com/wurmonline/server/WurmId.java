/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
/*     */ import java.io.Serializable;
/*     */ import java.math.BigInteger;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
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
/*     */ public final class WurmId
/*     */   implements Serializable, CounterTypes
/*     */ {
/*     */   private static final long serialVersionUID = -1805883548433788244L;
/*  44 */   private static long playerIdCounter = 0L;
/*  45 */   private static long creatureIdCounter = 0L;
/*  46 */   private static long itemIdCounter = 0L;
/*  47 */   private static long structureIdCounter = 0L;
/*  48 */   private static long tempIdCounter = 0L;
/*  49 */   private static long illusionIdCounter = 0L;
/*  50 */   private static long woundIdCounter = 0L;
/*  51 */   private static long temporaryWoundIdCounter = 0L;
/*  52 */   private static long spellIdCounter = 0L;
/*  53 */   private static long creatureSkillsIdCounter = 0L;
/*     */   
/*  55 */   private static long templateSkillsIdCounter = 0L;
/*  56 */   private static long playerSkillsIdCounter = 0L;
/*  57 */   private static long temporarySkillsIdCounter = 0L;
/*  58 */   private static long planIdCounter = 0L;
/*  59 */   private static long bankIdCounter = 0L;
/*  60 */   private static long bodyIdCounter = 0L;
/*  61 */   private static long coinIdCounter = 0L;
/*  62 */   private static long poiIdCounter = 0L;
/*  63 */   private static long couponIdCounter = 0L;
/*  64 */   private static long wccommandCounter = 0L;
/*  65 */   private static int savecounter = 0;
/*     */ 
/*     */   
/*  68 */   private static final Logger logger = Logger.getLogger(WurmId.class.getName());
/*     */   
/*     */   private static final String getMaxPlayerId = "SELECT MAX(WURMID) FROM PLAYERS";
/*     */   
/*     */   private static final String getMaxCreatureId = "SELECT MAX(WURMID) FROM CREATURES";
/*     */   private static final String getMaxItemId = "SELECT MAX(WURMID) FROM ITEMS";
/*     */   private static final String getMaxStructureId = "SELECT MAX(WURMID) FROM STRUCTURES";
/*     */   private static final String getMaxWoundId = "SELECT MAX(ID) FROM WOUNDS";
/*     */   private static final String getMaxSkillId = "SELECT MAX(ID) FROM SKILLS";
/*     */   private static final String getMaxBankId = "SELECT MAX(WURMID) FROM BANKS";
/*     */   private static final String getMaxSpellId = "SELECT MAX(WURMID) FROM SPELLEFFECTS";
/*     */   private static final String getMaxBodyId = "SELECT MAX(WURMID) FROM BODYPARTS";
/*     */   private static final String getMaxCoinId = "SELECT MAX(WURMID) FROM COINS";
/*     */   private static final String getMaxPoiId = "SELECT MAX(ID) FROM MAP_ANNOTATIONS";
/*     */   private static final String getMaxCouponId = "SELECT MAX(CODEID) FROM REDEEMCODE";
/*     */   private static final String getIds = "SELECT * FROM IDS WHERE SERVER=?";
/*     */   private static final String createIds = "INSERT INTO IDS (SERVER,PLAYERIDS,CREATUREIDS,ITEMIDS,STRUCTUREIDS,WOUNDIDS,PLAYERSKILLIDS,CREATURESKILLIDS,BANKIDS,SPELLIDS,PLANIDS,BODYIDS,COINIDS,WCCOMMANDS, POIIDS, REDEEMIDS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*     */   private static final String updateIds = "UPDATE IDS SET PLAYERIDS=?,CREATUREIDS=?,ITEMIDS=?,STRUCTUREIDS=?,WOUNDIDS=?,PLAYERSKILLIDS=?,CREATURESKILLIDS=?,BANKIDS=?,SPELLIDS=?,PLANIDS=?,BODYIDS=?,COINIDS=?,WCCOMMANDS=?, POIIDS=?, REDEEMIDS=? WHERE SERVER=?";
/*     */   
/*     */   static {
/*  88 */     loadIdNumbers();
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
/*     */   public static final int getType(long id) {
/* 108 */     return (int)(id & 0xFFL);
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
/*     */   public static final int getOrigin(long id) {
/* 120 */     return (int)(id >> 8L) & 0xFFFF;
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
/*     */   public static final long getNumber(long id) {
/* 132 */     return id >> 24L;
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
/*     */   public static final long getId(long id) {
/* 144 */     return id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long getNextItemId() {
/* 152 */     itemIdCounter++;
/* 153 */     savecounter++;
/* 154 */     checkSave();
/* 155 */     return BigInteger.valueOf(itemIdCounter).shiftLeft(24).longValue() + (Servers.localServer.id << 8) + 2L;
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
/*     */   public static final long getNextPlayerId() {
/* 167 */     playerIdCounter++;
/* 168 */     savecounter++;
/* 169 */     checkSave();
/* 170 */     return BigInteger.valueOf(playerIdCounter).shiftLeft(24).longValue() + (Servers.localServer.id << 8) + 0L;
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
/*     */   public static final long getNextBodyPartId(long creatureId, byte bodyplace, boolean isPlayer) {
/* 184 */     return BigInteger.valueOf(BigInteger.valueOf(creatureId >> 8L).shiftLeft(1).longValue() + (isPlayer ? 1L : 0L))
/* 185 */       .shiftLeft(16).longValue() + (bodyplace << 8) + 19L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long getCreatureIdForBodyPart(long bodypartId) {
/* 194 */     boolean isPlayer = ((BigInteger.valueOf(bodypartId).shiftRight(16).longValue() & 0x1L) == 1L);
/*     */     
/* 196 */     return (bodypartId >> 17L) + (isPlayer ? 0L : 1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int getBodyPlaceForBodyPart(long bodypartId) {
/* 205 */     return (int)(bodypartId >> 8L & 0xFFL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long getNextCreatureId() {
/* 213 */     creatureIdCounter++;
/*     */     
/* 215 */     savecounter++;
/* 216 */     checkSave();
/* 217 */     return BigInteger.valueOf(creatureIdCounter).shiftLeft(24).longValue() + (Servers.localServer.id << 8) + 1L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long getNextStructureId() {
/* 226 */     structureIdCounter++;
/* 227 */     savecounter++;
/* 228 */     checkSave();
/* 229 */     return BigInteger.valueOf(structureIdCounter).shiftLeft(24).longValue() + (Servers.localServer.id << 8) + 4L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long getNextTempItemId() {
/* 238 */     tempIdCounter++;
/* 239 */     return BigInteger.valueOf(tempIdCounter).shiftLeft(24).longValue() + (Servers.localServer.id << 8) + 6L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long getNextIllusionId() {
/* 248 */     illusionIdCounter++;
/* 249 */     return BigInteger.valueOf(illusionIdCounter).shiftLeft(24).longValue() + 24L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long getNextTemporaryWoundId() {
/* 257 */     temporaryWoundIdCounter++;
/* 258 */     return BigInteger.valueOf(temporaryWoundIdCounter).shiftLeft(24).longValue() + (Servers.localServer.id << 8) + 32L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long getNextWoundId() {
/* 267 */     woundIdCounter++;
/* 268 */     savecounter++;
/* 269 */     checkSave();
/* 270 */     return BigInteger.valueOf(woundIdCounter).shiftLeft(24).longValue() + (Servers.localServer.id << 8) + 8L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long getNextTemporarySkillId() {
/* 279 */     temporarySkillsIdCounter++;
/* 280 */     return BigInteger.valueOf(temporarySkillsIdCounter).shiftLeft(24).longValue() + (Servers.localServer.id << 8) + 31L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long getNextPlayerSkillId() {
/* 289 */     playerSkillsIdCounter++;
/* 290 */     savecounter++;
/* 291 */     checkSave();
/* 292 */     return BigInteger.valueOf(playerSkillsIdCounter).shiftLeft(24).longValue() + (Servers.localServer.id << 8) + 10L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long getNextCreatureSkillId() {
/* 301 */     creatureSkillsIdCounter++;
/* 302 */     savecounter++;
/* 303 */     checkSave();
/* 304 */     return BigInteger.valueOf(creatureSkillsIdCounter).shiftLeft(24).longValue() + (Servers.localServer.id << 8) + 9L;
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
/*     */   public static final long getNextBankId() {
/* 318 */     bankIdCounter++;
/* 319 */     savecounter++;
/* 320 */     checkSave();
/* 321 */     return BigInteger.valueOf(bankIdCounter).shiftLeft(24).longValue() + (Servers.localServer.id << 8) + 13L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long getNextSpellId() {
/* 329 */     spellIdCounter++;
/* 330 */     savecounter++;
/* 331 */     checkSave();
/* 332 */     return BigInteger.valueOf(spellIdCounter).shiftLeft(24).longValue() + (Servers.localServer.id << 8) + 15L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long getNextWCCommandId() {
/* 341 */     wccommandCounter++;
/* 342 */     savecounter++;
/* 343 */     checkSave();
/* 344 */     return BigInteger.valueOf(wccommandCounter).shiftLeft(24).longValue() + (Servers.localServer.id << 8) + 21L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long getNextPlanId() {
/* 353 */     planIdCounter++;
/* 354 */     savecounter++;
/* 355 */     checkSave();
/* 356 */     return BigInteger.valueOf(planIdCounter).shiftLeft(24).longValue() + (Servers.localServer.id << 8) + 16L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long getNextBodyId() {
/* 364 */     bodyIdCounter++;
/* 365 */     savecounter++;
/* 366 */     checkSave();
/* 367 */     return BigInteger.valueOf(bodyIdCounter).shiftLeft(24).longValue() + (Servers.localServer.id << 8) + 19L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long getNextCoinId() {
/* 376 */     coinIdCounter++;
/* 377 */     savecounter++;
/* 378 */     checkSave();
/* 379 */     return BigInteger.valueOf(coinIdCounter).shiftLeft(24).longValue() + (Servers.localServer.id << 8) + 20L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long getNextPoiId() {
/* 388 */     poiIdCounter++;
/* 389 */     savecounter++;
/* 390 */     checkSave();
/* 391 */     return BigInteger.valueOf(poiIdCounter).shiftLeft(24).longValue() + (Servers.localServer.id << 8) + 26L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long getNextCouponId() {
/* 397 */     couponIdCounter++;
/* 398 */     savecounter++;
/* 399 */     checkSave();
/* 400 */     return BigInteger.valueOf(couponIdCounter).shiftLeft(24).longValue() + (Servers.localServer.id << 8) + 29L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void loadIdNumbers() {
/* 406 */     long start = System.nanoTime();
/* 407 */     Connection dbcon = null;
/* 408 */     PreparedStatement ps = null;
/* 409 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 412 */       dbcon = DbConnector.getLoginDbCon();
/* 413 */       ps = dbcon.prepareStatement("SELECT * FROM IDS WHERE SERVER=?");
/* 414 */       ps.setInt(1, Servers.localServer.id);
/* 415 */       rs = ps.executeQuery();
/* 416 */       if (rs.next()) {
/*     */         
/* 418 */         logger.log(Level.INFO, "Loading ids.");
/* 419 */         playerIdCounter = rs.getLong("PLAYERIDS");
/* 420 */         woundIdCounter = rs.getLong("WOUNDIDS");
/* 421 */         playerSkillsIdCounter = rs.getLong("PLAYERSKILLIDS");
/* 422 */         creatureSkillsIdCounter = rs.getLong("CREATURESKILLIDS");
/* 423 */         creatureIdCounter = rs.getLong("CREATUREIDS");
/* 424 */         structureIdCounter = rs.getLong("STRUCTUREIDS");
/* 425 */         itemIdCounter = rs.getLong("ITEMIDS");
/* 426 */         bankIdCounter = rs.getLong("BANKIDS");
/* 427 */         spellIdCounter = rs.getLong("SPELLIDS");
/* 428 */         wccommandCounter = rs.getLong("WCCOMMANDS");
/* 429 */         planIdCounter = rs.getLong("PLANIDS");
/* 430 */         bodyIdCounter = rs.getLong("BODYIDS");
/* 431 */         coinIdCounter = rs.getLong("COINIDS");
/* 432 */         poiIdCounter = rs.getLong("POIIDS");
/* 433 */         couponIdCounter = rs.getLong("REDEEMIDS");
/*     */       } 
/*     */ 
/*     */       
/* 437 */       rs.close();
/* 438 */       ps.close();
/* 439 */       if (itemIdCounter == 0L)
/*     */       {
/*     */ 
/*     */         
/* 443 */         loadIdNumbers(true);
/*     */       
/*     */       }
/*     */       else
/*     */       {
/* 448 */         itemIdCounter += 3000L;
/* 449 */         playerIdCounter += 3000L;
/* 450 */         woundIdCounter += 3000L;
/* 451 */         playerSkillsIdCounter += 3000L;
/* 452 */         creatureSkillsIdCounter += 3000L;
/* 453 */         creatureIdCounter += 3000L;
/* 454 */         structureIdCounter += 3000L;
/* 455 */         itemIdCounter += 3000L;
/* 456 */         bankIdCounter += 3000L;
/* 457 */         spellIdCounter += 3000L;
/* 458 */         wccommandCounter += 3000L;
/* 459 */         planIdCounter += 3000L;
/* 460 */         bodyIdCounter += 3000L;
/* 461 */         coinIdCounter += 3000L;
/* 462 */         poiIdCounter += 1000L;
/* 463 */         couponIdCounter += 100L;
/* 464 */         updateNumbers();
/* 465 */         logger.log(Level.INFO, "Added to ids, creatrureIdcounter is now " + creatureIdCounter);
/*     */       }
/*     */     
/*     */     }
/* 469 */     catch (SQLException sqx) {
/*     */       
/* 471 */       logger.log(Level.WARNING, "Failed to load max playerid: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 475 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 476 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 478 */     logger.info("Finished loading Wurm IDs, that took " + ((float)(System.nanoTime() - start) / 1000000.0F) + " millis.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void checkSave() {
/* 489 */     if (savecounter >= 1000) {
/*     */       
/* 491 */       updateNumbers();
/* 492 */       savecounter = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void loadIdNumbers(boolean create) {
/* 498 */     logger.log(Level.WARNING, "LOADING WURMIDS 'MANUALLY'. This should only happen at convert or on a new server.");
/* 499 */     Connection dbcon = null;
/* 500 */     PreparedStatement ps = null;
/* 501 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 504 */       dbcon = DbConnector.getPlayerDbCon();
/* 505 */       ps = dbcon.prepareStatement("SELECT MAX(WURMID) FROM PLAYERS");
/* 506 */       rs = ps.executeQuery();
/* 507 */       if (rs.next())
/* 508 */         playerIdCounter = rs.getLong("MAX(WURMID)") >> 24L; 
/* 509 */       rs.close();
/* 510 */       ps.close();
/*     */     
/*     */     }
/* 513 */     catch (SQLException sqx) {
/*     */       
/* 515 */       logger.log(Level.WARNING, "Failed to load max playerid: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 519 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 520 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 525 */       dbcon = DbConnector.getPlayerDbCon();
/* 526 */       ps = dbcon.prepareStatement("SELECT MAX(ID) FROM WOUNDS");
/* 527 */       rs = ps.executeQuery();
/* 528 */       if (rs.next())
/* 529 */         woundIdCounter = rs.getLong("MAX(ID)") >> 24L; 
/* 530 */       rs.close();
/* 531 */       ps.close();
/*     */     }
/* 533 */     catch (SQLException sqx) {
/*     */       
/* 535 */       logger.log(Level.WARNING, "Failed to load max woundid: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 539 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 540 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 545 */       dbcon = DbConnector.getPlayerDbCon();
/* 546 */       ps = dbcon.prepareStatement("SELECT MAX(ID) FROM SKILLS");
/* 547 */       rs = ps.executeQuery();
/* 548 */       if (rs.next())
/* 549 */         playerSkillsIdCounter = rs.getLong("MAX(ID)") >> 24L; 
/* 550 */       rs.close();
/* 551 */       ps.close();
/*     */     }
/* 553 */     catch (SQLException sqx) {
/*     */       
/* 555 */       logger.log(Level.WARNING, "Failed to load max player skill id: " + sqx.getMessage(), sqx);
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 560 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 561 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */     
/*     */     try {
/* 565 */       dbcon = DbConnector.getCreatureDbCon();
/* 566 */       ps = dbcon.prepareStatement("SELECT MAX(ID) FROM SKILLS");
/* 567 */       rs = ps.executeQuery();
/* 568 */       if (rs.next())
/* 569 */         creatureSkillsIdCounter = rs.getLong("MAX(ID)") >> 24L; 
/* 570 */       rs.close();
/* 571 */       ps.close();
/*     */     }
/* 573 */     catch (SQLException sqx) {
/*     */       
/* 575 */       logger.log(Level.WARNING, "Failed to load max creature skill id: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 579 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 580 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 585 */       dbcon = DbConnector.getTemplateDbCon();
/* 586 */       ps = dbcon.prepareStatement("SELECT MAX(ID) FROM SKILLS");
/* 587 */       rs = ps.executeQuery();
/* 588 */       if (rs.next())
/* 589 */         templateSkillsIdCounter = rs.getLong("MAX(ID)") >> 24L; 
/* 590 */       rs.close();
/* 591 */       ps.close();
/*     */     }
/* 593 */     catch (SQLException sqx) {
/*     */       
/* 595 */       logger.log(Level.WARNING, "Failed to load max templateid: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 599 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 600 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 605 */       dbcon = DbConnector.getCreatureDbCon();
/* 606 */       ps = dbcon.prepareStatement("SELECT MAX(WURMID) FROM CREATURES");
/*     */       
/* 608 */       rs = ps.executeQuery();
/* 609 */       if (rs.next())
/* 610 */         creatureIdCounter = rs.getLong("MAX(WURMID)") >> 24L; 
/* 611 */       logger.log(Level.WARNING, "Max creatureid: " + creatureIdCounter + " when loading manually");
/* 612 */       rs.close();
/* 613 */       ps.close();
/*     */     }
/* 615 */     catch (SQLException sqx) {
/*     */       
/* 617 */       logger.log(Level.WARNING, "Failed to load max creatureid: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 621 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 622 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 627 */       dbcon = DbConnector.getZonesDbCon();
/* 628 */       ps = dbcon.prepareStatement("SELECT MAX(WURMID) FROM STRUCTURES");
/*     */       
/* 630 */       rs = ps.executeQuery();
/* 631 */       if (rs.next())
/* 632 */         structureIdCounter = rs.getLong("MAX(WURMID)") >> 24L; 
/* 633 */       rs.close();
/* 634 */       ps.close();
/*     */     }
/* 636 */     catch (SQLException sqx) {
/*     */       
/* 638 */       logger.log(Level.WARNING, "Failed to load max structureid: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 642 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 643 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 648 */       dbcon = DbConnector.getItemDbCon();
/* 649 */       ps = dbcon.prepareStatement("SELECT MAX(WURMID) FROM ITEMS");
/*     */       
/* 651 */       rs = ps.executeQuery();
/* 652 */       if (rs.next())
/* 653 */         itemIdCounter = rs.getLong("MAX(WURMID)") >> 24L; 
/* 654 */       rs.close();
/* 655 */       ps.close();
/*     */     }
/* 657 */     catch (SQLException sqx) {
/*     */       
/* 659 */       logger.log(Level.WARNING, "Failed to load max itemid: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 663 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 664 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 669 */       dbcon = DbConnector.getItemDbCon();
/* 670 */       ps = dbcon.prepareStatement("SELECT MAX(WURMID) FROM BODYPARTS");
/*     */       
/* 672 */       rs = ps.executeQuery();
/* 673 */       if (rs.next())
/* 674 */         bodyIdCounter = rs.getLong("MAX(WURMID)") >> 24L; 
/* 675 */       rs.close();
/* 676 */       ps.close();
/*     */     }
/* 678 */     catch (SQLException sqx) {
/*     */       
/* 680 */       logger.log(Level.WARNING, "Failed to load max body id: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 684 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 685 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 690 */       dbcon = DbConnector.getItemDbCon();
/* 691 */       ps = dbcon.prepareStatement("SELECT MAX(WURMID) FROM COINS");
/*     */       
/* 693 */       rs = ps.executeQuery();
/* 694 */       if (rs.next())
/* 695 */         coinIdCounter = rs.getLong("MAX(WURMID)") >> 24L; 
/* 696 */       rs.close();
/* 697 */       ps.close();
/*     */     }
/* 699 */     catch (SQLException sqx) {
/*     */       
/* 701 */       logger.log(Level.WARNING, "Failed to load max coin id: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 705 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 706 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 711 */       dbcon = DbConnector.getPlayerDbCon();
/* 712 */       ps = dbcon.prepareStatement("SELECT MAX(ID) FROM MAP_ANNOTATIONS");
/*     */       
/* 714 */       rs = ps.executeQuery();
/* 715 */       if (rs.next())
/* 716 */         poiIdCounter = rs.getLong("MAX(ID)") >> 24L; 
/* 717 */       rs.close();
/* 718 */       ps.close();
/*     */     }
/* 720 */     catch (SQLException sqx) {
/*     */       
/* 722 */       logger.log(Level.WARNING, "Failed to load max poi id: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 726 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 727 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 732 */       dbcon = DbConnector.getEconomyDbCon();
/* 733 */       ps = dbcon.prepareStatement("SELECT MAX(WURMID) FROM BANKS");
/*     */       
/* 735 */       rs = ps.executeQuery();
/* 736 */       if (rs.next())
/* 737 */         bankIdCounter = rs.getLong("MAX(WURMID)") >> 24L; 
/* 738 */       rs.close();
/* 739 */       ps.close();
/*     */     }
/* 741 */     catch (SQLException sqx) {
/*     */       
/* 743 */       logger.log(Level.WARNING, "Failed to load max bank id: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 747 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 748 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 753 */       dbcon = DbConnector.getPlayerDbCon();
/* 754 */       ps = dbcon.prepareStatement("SELECT MAX(WURMID) FROM SPELLEFFECTS");
/*     */       
/* 756 */       rs = ps.executeQuery();
/* 757 */       if (rs.next())
/* 758 */         spellIdCounter = rs.getLong("MAX(WURMID)") >> 24L; 
/* 759 */       rs.close();
/* 760 */       ps.close();
/*     */     }
/* 762 */     catch (SQLException sqx) {
/*     */       
/* 764 */       logger.log(Level.WARNING, "Failed to load max spell id: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 768 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 769 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */     
/* 772 */     if (create)
/* 773 */       saveNumbers(); 
/* 774 */     logger.info("Loaded id numbers from database, playerids:" + playerIdCounter + ", creatureids:" + creatureIdCounter + ", itemids:" + itemIdCounter + ", structureIds:" + structureIdCounter + ", woundids:" + woundIdCounter + ", playerSkillIds: " + playerSkillsIdCounter + ", creatureSkillIds: " + creatureSkillsIdCounter + ", templateSkillIds: " + templateSkillsIdCounter + ", bankIds: " + bankIdCounter + ", spellIds: " + spellIdCounter + ", planIds: " + planIdCounter + ", bodyIds: " + bodyIdCounter + ", coinIds: " + coinIdCounter + ", wccommandCounter: " + wccommandCounter + ", poiIdCounter: " + poiIdCounter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void updateNumbers() {
/* 784 */     long start = System.nanoTime();
/* 785 */     Connection dbcon = null;
/* 786 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 789 */       dbcon = DbConnector.getLoginDbCon();
/* 790 */       ps = dbcon.prepareStatement("UPDATE IDS SET PLAYERIDS=?,CREATUREIDS=?,ITEMIDS=?,STRUCTUREIDS=?,WOUNDIDS=?,PLAYERSKILLIDS=?,CREATURESKILLIDS=?,BANKIDS=?,SPELLIDS=?,PLANIDS=?,BODYIDS=?,COINIDS=?,WCCOMMANDS=?, POIIDS=?, REDEEMIDS=? WHERE SERVER=?");
/* 791 */       ps.setLong(1, playerIdCounter);
/* 792 */       ps.setLong(2, creatureIdCounter);
/* 793 */       ps.setLong(3, itemIdCounter);
/* 794 */       ps.setLong(4, structureIdCounter);
/* 795 */       ps.setLong(5, woundIdCounter);
/* 796 */       ps.setLong(6, playerSkillsIdCounter);
/* 797 */       ps.setLong(7, creatureSkillsIdCounter);
/* 798 */       ps.setLong(8, bankIdCounter);
/* 799 */       ps.setLong(9, spellIdCounter);
/* 800 */       ps.setLong(10, planIdCounter);
/* 801 */       ps.setLong(11, bodyIdCounter);
/* 802 */       ps.setLong(12, coinIdCounter);
/* 803 */       ps.setLong(13, wccommandCounter);
/* 804 */       ps.setLong(14, poiIdCounter);
/* 805 */       ps.setLong(15, couponIdCounter);
/* 806 */       ps.setInt(16, Servers.localServer.id);
/* 807 */       ps.executeUpdate();
/* 808 */       ps.close();
/*     */     }
/* 810 */     catch (SQLException sqex) {
/*     */       
/* 812 */       logger.log(Level.WARNING, "Failed to update idnums into logindb! " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 816 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 817 */       DbConnector.returnConnection(dbcon);
/* 818 */       if (logger.isLoggable(Level.FINE)) {
/*     */         
/* 820 */         logger.fine("Finished updating Wurm IDs, that took " + ((float)(System.nanoTime() - start) / 1000000.0F) + " millis.");
/*     */ 
/*     */         
/* 823 */         logger.fine("Saved id numbers to database, playerids:" + playerIdCounter + ", creatureids:" + creatureIdCounter + ", itemids:" + itemIdCounter + ", structureIds:" + structureIdCounter + ", woundids:" + woundIdCounter + ", playerSkillIds: " + playerSkillsIdCounter + ", creatureSkillIds: " + creatureSkillsIdCounter + ", bankIds: " + bankIdCounter + ", spellIds: " + spellIdCounter + ", planIds: " + planIdCounter + ", bodyIds: " + bodyIdCounter + ", coinIds: " + coinIdCounter + ", wccommandCounter: " + wccommandCounter + ", poiIdCounter: " + poiIdCounter);
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
/*     */   private static final void saveNumbers() {
/* 839 */     long start = System.nanoTime();
/* 840 */     PreparedStatement ps = null;
/* 841 */     Connection dbcon = null;
/*     */     
/*     */     try {
/* 844 */       dbcon = DbConnector.getLoginDbCon();
/* 845 */       ps = dbcon.prepareStatement("INSERT INTO IDS (SERVER,PLAYERIDS,CREATUREIDS,ITEMIDS,STRUCTUREIDS,WOUNDIDS,PLAYERSKILLIDS,CREATURESKILLIDS,BANKIDS,SPELLIDS,PLANIDS,BODYIDS,COINIDS,WCCOMMANDS, POIIDS, REDEEMIDS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
/* 846 */       ps.setInt(1, Servers.localServer.id);
/* 847 */       ps.setLong(2, playerIdCounter);
/* 848 */       ps.setLong(3, creatureIdCounter);
/* 849 */       ps.setLong(4, itemIdCounter);
/* 850 */       ps.setLong(5, structureIdCounter);
/* 851 */       ps.setLong(6, woundIdCounter);
/* 852 */       ps.setLong(7, playerSkillsIdCounter);
/* 853 */       ps.setLong(8, creatureSkillsIdCounter);
/* 854 */       ps.setLong(9, bankIdCounter);
/* 855 */       ps.setLong(10, spellIdCounter);
/* 856 */       ps.setLong(11, planIdCounter);
/* 857 */       ps.setLong(12, bodyIdCounter);
/* 858 */       ps.setLong(13, coinIdCounter);
/* 859 */       ps.setLong(14, wccommandCounter);
/* 860 */       ps.setLong(15, poiIdCounter);
/* 861 */       ps.setLong(16, couponIdCounter);
/* 862 */       ps.executeUpdate();
/* 863 */       ps.close();
/*     */     }
/* 865 */     catch (SQLException sqex) {
/*     */       
/* 867 */       logger.log(Level.WARNING, "Failed to insert idnums into logindb! Trying update instead." + sqex.getMessage(), sqex);
/* 868 */       updateNumbers();
/*     */     }
/*     */     finally {
/*     */       
/* 872 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 873 */       DbConnector.returnConnection(dbcon);
/* 874 */       logger.info("Finished saving Wurm IDs, that took " + ((float)(System.nanoTime() - start) / 1000000.0F) + " millis.");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\WurmId.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */