/*     */ package com.wurmonline.server.structures;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.MineDoorSettings;
/*     */ import com.wurmonline.server.players.Permissions;
/*     */ import com.wurmonline.server.players.PermissionsByPlayer;
/*     */ import com.wurmonline.server.players.PermissionsPlayerList;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Map;
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
/*     */ public final class DoorSettings
/*     */   implements MiscConstants
/*     */ {
/*     */   public enum DoorPermissions
/*     */     implements Permissions.IPermission
/*     */   {
/*  51 */     PASS(1, "Pass Door", "Pass", "Door", "Allows entry through this door even when its locked."),
/*     */ 
/*     */     
/*  54 */     EXCLUDE(15, "Deny All", "Deny", "All", "Deny all access.");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final byte bit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final String description;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final String header1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final String header2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final String hover;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     DoorPermissions(int aBit, String aDescription, String aHeader1, String aHeader2, String aHover) {
/*     */       this.bit = (byte)aBit;
/*     */       this.description = aDescription;
/*     */       this.header1 = aHeader1;
/*     */       this.header2 = aHeader2;
/*     */       this.hover = aHover;
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
/*     */     public byte getBit() {
/*     */       return this.bit;
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
/* 141 */     private static final Permissions.IPermission[] types = (Permissions.IPermission[])values();
/*     */     public int getValue() { return 1 << this.bit; }
/*     */     public String getDescription() { return this.description; }
/*     */     public String getHeader1() { return this.header1; }
/*     */     public String getHeader2() { return this.header2; } public static Permissions.IPermission[] getPermissions() {
/* 146 */       return types;
/*     */     } public String getHover() {
/*     */       return this.hover;
/*     */     } static {
/*     */     
/*     */     } }
/* 152 */   public enum GatePermissions implements Permissions.IPermission { MANAGE(0, "Manage Item", "Manage", "Item", "Allows managing of these permissions."),
/* 153 */     PASS(1, "Pass Gate", "Pass", "Gate", "Allows entry through this gate even when its locked."),
/* 154 */     LOCK(2, "(Un)Lock Gate", "(Un)Lock", "Gate", "Allows locking (and unlocking) of this gate."),
/* 155 */     EXCLUDE(15, "Deny All", "Deny", "All", "Deny all access.");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final byte bit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final String description;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final String header1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final String header2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final String hover;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     GatePermissions(int aBit, String aDescription, String aHeader1, String aHeader2, String aHover) {
/*     */       this.bit = (byte)aBit;
/*     */       this.description = aDescription;
/*     */       this.header1 = aHeader1;
/*     */       this.header2 = aHeader2;
/*     */       this.hover = aHover;
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
/*     */     public byte getBit() {
/*     */       return this.bit;
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
/* 242 */     private static final Permissions.IPermission[] types = (Permissions.IPermission[])values(); public int getValue() {
/*     */       return 1 << this.bit;
/*     */     } public String getDescription() {
/*     */       return this.description;
/*     */     } public static Permissions.IPermission[] getPermissions() {
/* 247 */       return types; } public String getHeader1() { return this.header1; }
/*     */     public String getHeader2() { return this.header2; }
/*     */     public String getHover() { return this.hover; }
/*     */     static {
/*     */     
/* 252 */     } } private static final Logger logger = Logger.getLogger(DoorSettings.class.getName());
/*     */   private static final String GET_ALL_SETTINGS = "SELECT * FROM DOORSETTINGS";
/*     */   private static final String ADD_PLAYER = "INSERT INTO DOORSETTINGS (SETTINGS,WURMID,PLAYERID) VALUES(?,?,?)";
/*     */   private static final String DELETE_SETTINGS = "DELETE FROM DOORSETTINGS WHERE WURMID=?";
/*     */   private static final String REMOVE_PLAYER = "DELETE FROM DOORSETTINGS WHERE WURMID=? AND PLAYERID=?";
/*     */   private static final String UPDATE_PLAYER = "UPDATE DOORSETTINGS SET SETTINGS=? WHERE WURMID=? AND PLAYERID=?";
/*     */   private static final int MAX_PLAYERS_PER_OBJECT = 1000;
/* 259 */   private static Map<Long, PermissionsPlayerList> objectSettings = new ConcurrentHashMap<>();
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
/*     */   public static void loadAll() throws IOException {
/* 273 */     logger.log(Level.INFO, "Loading all door (and gate) settings.");
/* 274 */     long start = System.nanoTime();
/* 275 */     long count = 0L;
/* 276 */     Connection dbcon = null;
/* 277 */     PreparedStatement ps = null;
/* 278 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 281 */       dbcon = DbConnector.getZonesDbCon();
/* 282 */       ps = dbcon.prepareStatement("SELECT * FROM DOORSETTINGS");
/* 283 */       rs = ps.executeQuery();
/* 284 */       while (rs.next())
/*     */       {
/* 286 */         long wurmId = rs.getLong("WURMID");
/* 287 */         long playerId = rs.getLong("PLAYERID");
/* 288 */         int settings = rs.getInt("SETTINGS");
/* 289 */         add(wurmId, playerId, settings);
/* 290 */         count++;
/*     */       }
/*     */     
/* 293 */     } catch (SQLException ex) {
/*     */       
/* 295 */       logger.log(Level.WARNING, "Failed to load settings for doors (and gates).", ex);
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 300 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 301 */       DbConnector.returnConnection(dbcon);
/* 302 */       long end = System.nanoTime();
/* 303 */       logger.log(Level.INFO, "Loaded " + count + " door (and gate) settings. That took " + ((float)(end - start) / 1000000.0F) + " ms.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getMaxAllowed() {
/* 313 */     return Servers.isThisATestServer() ? 10 : 1000;
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
/*     */   private static PermissionsByPlayer add(long wurmId, long playerId, int settings) {
/* 325 */     Long id = Long.valueOf(wurmId);
/* 326 */     if (objectSettings.containsKey(id)) {
/*     */ 
/*     */       
/* 329 */       PermissionsPlayerList permissionsPlayerList = objectSettings.get(id);
/* 330 */       return permissionsPlayerList.add(playerId, settings);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 335 */     PermissionsPlayerList ppl = new PermissionsPlayerList();
/* 336 */     objectSettings.put(id, ppl);
/* 337 */     return ppl.add(playerId, settings);
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
/*     */   public static void addPlayer(long wurmId, long playerId, int settings) {
/* 350 */     PermissionsByPlayer pbp = add(wurmId, playerId, settings);
/* 351 */     if (pbp == null) {
/* 352 */       dbAddPlayer(wurmId, playerId, settings, true);
/* 353 */     } else if (pbp.getSettings() != settings) {
/* 354 */       dbAddPlayer(wurmId, playerId, settings, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removePlayer(long wurmId, long playerId) {
/* 364 */     Long id = Long.valueOf(wurmId);
/* 365 */     if (objectSettings.containsKey(id)) {
/*     */       
/* 367 */       PermissionsPlayerList ppl = objectSettings.get(id);
/* 368 */       ppl.remove(playerId);
/* 369 */       dbRemovePlayer(wurmId, playerId);
/*     */       
/* 371 */       if (ppl.isEmpty()) {
/* 372 */         objectSettings.remove(id);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void dbAddPlayer(long wurmId, long playerId, int settings, boolean add) {
/* 383 */     Connection dbcon = null;
/* 384 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 387 */       dbcon = DbConnector.getZonesDbCon();
/* 388 */       if (add) {
/* 389 */         ps = dbcon.prepareStatement("INSERT INTO DOORSETTINGS (SETTINGS,WURMID,PLAYERID) VALUES(?,?,?)");
/*     */       } else {
/* 391 */         ps = dbcon.prepareStatement("UPDATE DOORSETTINGS SET SETTINGS=? WHERE WURMID=? AND PLAYERID=?");
/*     */       } 
/* 393 */       ps.setInt(1, settings);
/* 394 */       ps.setLong(2, wurmId);
/* 395 */       ps.setLong(3, playerId);
/* 396 */       ps.executeUpdate();
/*     */     }
/* 398 */     catch (SQLException ex) {
/*     */       
/* 400 */       logger.log(Level.WARNING, "Failed to " + (add ? "add" : "update") + " player (" + playerId + ") for door with id " + wurmId, ex);
/*     */     }
/*     */     finally {
/*     */       
/* 404 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 405 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void dbRemovePlayer(long wurmId, long playerId) {
/* 411 */     Connection dbcon = null;
/* 412 */     PreparedStatement ps = null;
/* 413 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 416 */       dbcon = DbConnector.getZonesDbCon();
/* 417 */       ps = dbcon.prepareStatement("DELETE FROM DOORSETTINGS WHERE WURMID=? AND PLAYERID=?");
/* 418 */       ps.setLong(1, wurmId);
/* 419 */       ps.setLong(2, playerId);
/* 420 */       ps.executeUpdate();
/*     */     }
/* 422 */     catch (SQLException ex) {
/*     */       
/* 424 */       logger.log(Level.WARNING, "Failed to remove player " + playerId + " from settings for door " + wurmId + ".", ex);
/*     */     }
/*     */     finally {
/*     */       
/* 428 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 429 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean exists(long wurmId) {
/* 440 */     Long id = Long.valueOf(wurmId);
/* 441 */     return objectSettings.containsKey(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void remove(long wurmId) {
/* 451 */     Long id = Long.valueOf(wurmId);
/* 452 */     if (objectSettings.containsKey(id)) {
/*     */       
/* 454 */       dbRemove(wurmId);
/* 455 */       objectSettings.remove(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void dbRemove(long wurmId) {
/* 465 */     Connection dbcon = null;
/* 466 */     PreparedStatement ps = null;
/* 467 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 470 */       dbcon = DbConnector.getZonesDbCon();
/* 471 */       ps = dbcon.prepareStatement("DELETE FROM DOORSETTINGS WHERE WURMID=?");
/* 472 */       ps.setLong(1, wurmId);
/* 473 */       ps.executeUpdate();
/*     */     }
/* 475 */     catch (SQLException ex) {
/*     */       
/* 477 */       logger.log(Level.WARNING, "Failed to delete settings for door " + wurmId + ".", ex);
/*     */     }
/*     */     finally {
/*     */       
/* 481 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 482 */       DbConnector.returnConnection(dbcon);
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
/*     */   public static PermissionsPlayerList getPermissionsPlayerList(long wurmId) {
/* 494 */     Long id = Long.valueOf(wurmId);
/* 495 */     PermissionsPlayerList ppl = objectSettings.get(id);
/* 496 */     if (ppl == null)
/* 497 */       return new PermissionsPlayerList(); 
/* 498 */     return ppl;
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
/*     */   private static boolean hasPermission(PermissionsPlayerList.ISettings is, Creature creature, int bit) {
/* 511 */     if (is.isOwner(creature)) {
/* 512 */       return (bit != MineDoorSettings.MinedoorPermissions.EXCLUDE.getBit());
/*     */     }
/* 514 */     Long id = Long.valueOf(is.getWurmId());
/* 515 */     PermissionsPlayerList ppl = objectSettings.get(id);
/* 516 */     if (ppl == null)
/*     */     {
/* 518 */       return false;
/*     */     }
/* 520 */     if (ppl.exists(creature.getWurmId())) {
/* 521 */       return ppl.getPermissionsFor(creature.getWurmId()).hasPermission(bit);
/*     */     }
/* 523 */     if (is.isCitizen(creature) && ppl
/* 524 */       .exists(-30L)) {
/* 525 */       return ppl.getPermissionsFor(-30L).hasPermission(bit);
/*     */     }
/* 527 */     if (is.isAllied(creature) && ppl
/* 528 */       .exists(-20L)) {
/* 529 */       return ppl.getPermissionsFor(-20L).hasPermission(bit);
/*     */     }
/* 531 */     if (is.isSameKingdom(creature) && ppl
/* 532 */       .exists(-40L)) {
/* 533 */       return ppl.getPermissionsFor(-40L).hasPermission(bit);
/*     */     }
/* 535 */     return (ppl.exists(-50L) && ppl
/* 536 */       .getPermissionsFor(-50L).hasPermission(bit));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isGuest(PermissionsPlayerList.ISettings is, Creature creature) {
/* 547 */     return isGuest(is, creature.getWurmId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isGuest(PermissionsPlayerList.ISettings is, long playerId) {
/* 558 */     if (is.isOwner(playerId)) {
/* 559 */       return true;
/*     */     }
/* 561 */     Long id = Long.valueOf(is.getWurmId());
/* 562 */     PermissionsPlayerList ppl = objectSettings.get(id);
/* 563 */     if (ppl == null)
/*     */     {
/* 565 */       return false;
/*     */     }
/*     */     
/* 568 */     return ppl.exists(playerId);
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
/*     */   public static boolean canManage(PermissionsPlayerList.ISettings is, Creature creature) {
/* 580 */     return hasPermission(is, creature, GatePermissions.MANAGE.getBit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean mayPass(PermissionsPlayerList.ISettings is, Creature creature) {
/* 591 */     if (creature.getPower() > 1)
/* 592 */       return true; 
/* 593 */     return hasPermission(is, creature, DoorPermissions.PASS.getBit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean mayLock(PermissionsPlayerList.ISettings is, Creature creature) {
/* 604 */     if (creature.getPower() > 1)
/* 605 */       return true; 
/* 606 */     return hasPermission(is, creature, GatePermissions.LOCK.getBit());
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
/*     */   public static boolean isExcluded(PermissionsPlayerList.ISettings is, Creature creature) {
/* 630 */     if (creature.getPower() > 1)
/* 631 */       return false; 
/* 632 */     return hasPermission(is, creature, DoorPermissions.EXCLUDE.getBit());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\DoorSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */