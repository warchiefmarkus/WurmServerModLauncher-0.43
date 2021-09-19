/*     */ package com.wurmonline.server.creatures;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.players.Permissions;
/*     */ import com.wurmonline.server.players.PermissionsByPlayer;
/*     */ import com.wurmonline.server.players.PermissionsPlayerList;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public class AnimalSettings
/*     */   implements MiscConstants
/*     */ {
/*     */   public enum Animal0Permissions
/*     */     implements Permissions.IPermission
/*     */   {
/*  53 */     MANAGE(0, "Manage Item", "Manage", "Item", "Allows managing of these permissions."),
/*  54 */     COMMANDER(1, "Commander", "Can", "Lead", "Allows leading of this animal."),
/*     */     
/*  56 */     ACCESS_HOLD(3, "Manage Equipment", "Manage", "Equipment", "Allows adding or removing equipement from the animal without taming it."),
/*     */ 
/*     */     
/*  59 */     EXCLUDE(15, "Deny All", "Deny", "All", "Deny all access.");
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 146 */     private static final Permissions.IPermission[] types = (Permissions.IPermission[])values(); Animal0Permissions(int aBit, String aDescription, String aHeader1, String aHeader2, String aHover) { this.bit = (byte)aBit; this.description = aDescription; this.header1 = aHeader1; this.header2 = aHeader2;
/*     */       this.hover = aHover; }
/*     */     public byte getBit() { return this.bit; }
/*     */     public int getValue() { return 1 << this.bit; }
/*     */     public String getDescription() { return this.description; }
/* 151 */     public static Permissions.IPermission[] getPermissions() { return types; } public String getHeader1() { return this.header1; } public String getHeader2() { return this.header2; } public String getHover() {
/*     */       return this.hover;
/*     */     }
/*     */     static {
/*     */     
/*     */     } }
/* 157 */   public enum Animal1Permissions implements Permissions.IPermission { MANAGE(0, "Manage Item", "Manage", "Item", "Allows managing of these permissions."),
/* 158 */     COMMANDER(1, "Commander", "Can", "Ride", "Allows leading and riding of this animal."),
/*     */     
/* 160 */     ACCESS_HOLD(3, "Manage Equipment", "Manage", "Equipment", "Allows adding or removing equipement from the animal without taming it."),
/*     */ 
/*     */     
/* 163 */     EXCLUDE(15, "Deny All", "Deny", "All", "Deny all access.");
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 250 */     private static final Permissions.IPermission[] types = (Permissions.IPermission[])values(); Animal1Permissions(int aBit, String aDescription, String aHeader1, String aHeader2, String aHover) { this.bit = (byte)aBit; this.description = aDescription; this.header1 = aHeader1; this.header2 = aHeader2;
/*     */       this.hover = aHover; }
/*     */     public byte getBit() { return this.bit; }
/*     */     public int getValue() { return 1 << this.bit; }
/*     */     public String getDescription() { return this.description; }
/* 255 */     public static Permissions.IPermission[] getPermissions() { return types; } public String getHeader1() { return this.header1; } public String getHeader2() { return this.header2; } public String getHover() {
/*     */       return this.hover;
/*     */     }
/*     */     static {
/*     */     
/*     */     } }
/* 261 */   public enum Animal2Permissions implements Permissions.IPermission { MANAGE(0, "Manage Item", "Manage", "Item", "Allows managing of these permissions."),
/* 262 */     COMMANDER(1, "Commander", "Can", "Ride", "Allows leading and riding of this animal."),
/* 263 */     PASSENGER(2, "Passenger", "Can be", "Passenger", "Allows being a passenger on this animal."),
/* 264 */     ACCESS_HOLD(3, "Manage Equipment", "Manage", "Equipment", "Allows adding or removing equipement from the animal without taming it."),
/*     */ 
/*     */     
/* 267 */     EXCLUDE(15, "Deny All", "Deny", "All", "Deny all access.");
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 354 */     private static final Permissions.IPermission[] types = (Permissions.IPermission[])values();
/*     */     Animal2Permissions(int aBit, String aDescription, String aHeader1, String aHeader2, String aHover) { this.bit = (byte)aBit; this.description = aDescription; this.header1 = aHeader1; this.header2 = aHeader2;
/*     */       this.hover = aHover; }
/*     */     public byte getBit() { return this.bit; }
/*     */     public int getValue() { return 1 << this.bit; }
/* 359 */     public String getDescription() { return this.description; } public static Permissions.IPermission[] getPermissions() { return types; } public String getHeader1() { return this.header1; } public String getHeader2() { return this.header2; } public String getHover() {
/*     */       return this.hover;
/*     */     } static {
/*     */     
/*     */     } }
/*     */    public enum WagonerPermissions implements Permissions.IPermission {
/* 365 */     MANAGE(0, "Manage Item", "Manage", "Item", "Allows managing of these permissions."),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 371 */     CANUSE(6, "Can Use", "Can", "Use", "Allows sending of bulk items using this NPC."),
/*     */     
/* 373 */     EXCLUDE(15, "Deny All", "Deny", "All", "Deny all access.");
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 460 */     private static final Permissions.IPermission[] types = (Permissions.IPermission[])values(); WagonerPermissions(int aBit, String aDescription, String aHeader1, String aHeader2, String aHover) { this.bit = (byte)aBit; this.description = aDescription; this.header1 = aHeader1; this.header2 = aHeader2; this.hover = aHover; } public byte getBit() { return this.bit; } public int getValue() { return 1 << this.bit; } public String getDescription() { return this.description; }
/*     */     public String getHeader1() { return this.header1; }
/*     */     public String getHeader2() { return this.header2; }
/*     */     public String getHover() { return this.hover; }
/*     */     static {  }
/* 465 */     public static Permissions.IPermission[] getPermissions() { return types; }
/*     */   
/*     */   }
/*     */ 
/*     */   
/* 470 */   private static final Logger logger = Logger.getLogger(AnimalSettings.class.getName());
/*     */   private static final String GET_ALL_SETTINGS = "SELECT * FROM ANIMALSETTINGS";
/*     */   private static final String ADD_PLAYER = "INSERT INTO ANIMALSETTINGS (SETTINGS,WURMID,PLAYERID) VALUES(?,?,?)";
/*     */   private static final String DELETE_SETTINGS = "DELETE FROM ANIMALSETTINGS WHERE WURMID=?";
/*     */   private static final String REMOVE_PLAYER = "DELETE FROM ANIMALSETTINGS WHERE WURMID=? AND PLAYERID=?";
/*     */   private static final String UPDATE_PLAYER = "UPDATE ANIMALSETTINGS SET SETTINGS=? WHERE WURMID=? AND PLAYERID=?";
/* 476 */   private static int MAX_PLAYERS_PER_OBJECT = 1000;
/* 477 */   private static Map<Long, PermissionsPlayerList> objectSettings = new ConcurrentHashMap<>();
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
/* 491 */     logger.log(Level.INFO, "Loading all animal settings.");
/* 492 */     long start = System.nanoTime();
/* 493 */     long count = 0L;
/* 494 */     Connection dbcon = null;
/* 495 */     PreparedStatement ps = null;
/* 496 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 499 */       dbcon = DbConnector.getCreatureDbCon();
/* 500 */       ps = dbcon.prepareStatement("SELECT * FROM ANIMALSETTINGS");
/* 501 */       rs = ps.executeQuery();
/* 502 */       while (rs.next())
/*     */       {
/* 504 */         long wurmId = rs.getLong("WURMID");
/* 505 */         long playerId = rs.getLong("PLAYERID");
/* 506 */         int settings = rs.getInt("SETTINGS");
/* 507 */         add(wurmId, playerId, settings);
/* 508 */         count++;
/*     */       }
/*     */     
/* 511 */     } catch (SQLException ex) {
/*     */       
/* 513 */       logger.log(Level.WARNING, "Failed to load settings for animals.", ex);
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 518 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 519 */       DbConnector.returnConnection(dbcon);
/* 520 */       long end = System.nanoTime();
/* 521 */       logger.log(Level.INFO, "Loaded " + count + " animal settings. That took " + ((float)(end - start) / 1000000.0F) + " ms.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getMaxAllowed() {
/* 531 */     return Servers.isThisATestServer() ? 10 : MAX_PLAYERS_PER_OBJECT;
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
/* 543 */     Long id = Long.valueOf(wurmId);
/* 544 */     if (objectSettings.containsKey(id)) {
/*     */ 
/*     */       
/* 547 */       PermissionsPlayerList permissionsPlayerList = objectSettings.get(id);
/* 548 */       return permissionsPlayerList.add(playerId, settings);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 553 */     PermissionsPlayerList ppl = new PermissionsPlayerList();
/* 554 */     objectSettings.put(id, ppl);
/* 555 */     return ppl.add(playerId, settings);
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
/* 568 */     PermissionsByPlayer pbp = add(wurmId, playerId, settings);
/* 569 */     if (pbp == null) {
/* 570 */       dbAddPlayer(wurmId, playerId, settings, true);
/* 571 */     } else if (pbp.getSettings() != settings) {
/* 572 */       dbAddPlayer(wurmId, playerId, settings, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removePlayer(long wurmId, long playerId) {
/* 582 */     Long id = Long.valueOf(wurmId);
/* 583 */     if (objectSettings.containsKey(id)) {
/*     */       
/* 585 */       PermissionsPlayerList ppl = objectSettings.get(id);
/* 586 */       ppl.remove(playerId);
/* 587 */       dbRemovePlayer(wurmId, playerId);
/*     */       
/* 589 */       if (ppl.isEmpty()) {
/* 590 */         objectSettings.remove(id);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 595 */       logger.log(Level.WARNING, "Failed to remove player " + playerId + " from settings for animal " + wurmId + ".");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void dbAddPlayer(long wurmId, long playerId, int settings, boolean add) {
/* 601 */     Connection dbcon = null;
/* 602 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 605 */       dbcon = DbConnector.getCreatureDbCon();
/* 606 */       if (add) {
/* 607 */         ps = dbcon.prepareStatement("INSERT INTO ANIMALSETTINGS (SETTINGS,WURMID,PLAYERID) VALUES(?,?,?)");
/*     */       } else {
/* 609 */         ps = dbcon.prepareStatement("UPDATE ANIMALSETTINGS SET SETTINGS=? WHERE WURMID=? AND PLAYERID=?");
/*     */       } 
/* 611 */       ps.setInt(1, settings);
/* 612 */       ps.setLong(2, wurmId);
/* 613 */       ps.setLong(3, playerId);
/* 614 */       ps.executeUpdate();
/*     */     }
/* 616 */     catch (SQLException ex) {
/*     */       
/* 618 */       logger.log(Level.WARNING, "Failed to " + (add ? "add" : "update") + " player (" + playerId + ") for animal with id " + wurmId, ex);
/*     */     }
/*     */     finally {
/*     */       
/* 622 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 623 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void dbRemovePlayer(long wurmId, long playerId) {
/* 629 */     Connection dbcon = null;
/* 630 */     PreparedStatement ps = null;
/* 631 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 634 */       dbcon = DbConnector.getCreatureDbCon();
/* 635 */       ps = dbcon.prepareStatement("DELETE FROM ANIMALSETTINGS WHERE WURMID=? AND PLAYERID=?");
/* 636 */       ps.setLong(1, wurmId);
/* 637 */       ps.setLong(2, playerId);
/* 638 */       ps.executeUpdate();
/*     */     }
/* 640 */     catch (SQLException ex) {
/*     */       
/* 642 */       logger.log(Level.WARNING, "Failed to remove player " + playerId + " from settings for animal " + wurmId + ".", ex);
/*     */     }
/*     */     finally {
/*     */       
/* 646 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 647 */       DbConnector.returnConnection(dbcon);
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
/* 658 */     Long id = Long.valueOf(wurmId);
/* 659 */     return objectSettings.containsKey(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void remove(long wurmId) {
/* 669 */     Long id = Long.valueOf(wurmId);
/* 670 */     if (objectSettings.containsKey(id)) {
/*     */       
/* 672 */       dbRemove(wurmId);
/* 673 */       objectSettings.remove(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void dbRemove(long wurmId) {
/* 683 */     Connection dbcon = null;
/* 684 */     PreparedStatement ps = null;
/* 685 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 688 */       dbcon = DbConnector.getCreatureDbCon();
/* 689 */       ps = dbcon.prepareStatement("DELETE FROM ANIMALSETTINGS WHERE WURMID=?");
/* 690 */       ps.setLong(1, wurmId);
/* 691 */       ps.executeUpdate();
/*     */     }
/* 693 */     catch (SQLException ex) {
/*     */       
/* 695 */       logger.log(Level.WARNING, "Failed to delete settings for animal " + wurmId + ".", ex);
/*     */     }
/*     */     finally {
/*     */       
/* 699 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 700 */       DbConnector.returnConnection(dbcon);
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
/* 712 */     Long id = Long.valueOf(wurmId);
/* 713 */     PermissionsPlayerList ppl = objectSettings.get(id);
/* 714 */     if (ppl == null)
/* 715 */       return new PermissionsPlayerList(); 
/* 716 */     return ppl;
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
/*     */   private static boolean hasPermission(PermissionsPlayerList.ISettings is, Creature creature, @Nullable Village brandVillage, int bit) {
/* 729 */     if (is.isOwner(creature)) {
/* 730 */       return (bit != MineDoorSettings.MinedoorPermissions.EXCLUDE.getBit());
/*     */     }
/* 732 */     Long id = Long.valueOf(is.getWurmId());
/* 733 */     PermissionsPlayerList ppl = objectSettings.get(id);
/* 734 */     if (ppl == null)
/*     */     {
/* 736 */       return false;
/*     */     }
/* 738 */     if (ppl.exists(creature.getWurmId())) {
/* 739 */       return ppl.getPermissionsFor(creature.getWurmId()).hasPermission(bit);
/*     */     }
/* 741 */     if (is.isCitizen(creature) && ppl
/* 742 */       .exists(-30L)) {
/* 743 */       return ppl.getPermissionsFor(-30L).hasPermission(bit);
/*     */     }
/* 745 */     if (is.isAllied(creature) && ppl
/* 746 */       .exists(-20L)) {
/* 747 */       return ppl.getPermissionsFor(-20L).hasPermission(bit);
/*     */     }
/* 749 */     if (is.isSameKingdom(creature) && ppl
/* 750 */       .exists(-40L)) {
/* 751 */       return ppl.getPermissionsFor(-40L).hasPermission(bit);
/*     */     }
/* 753 */     if (brandVillage != null && brandVillage.isActionAllowed((short)484, creature) && ppl
/* 754 */       .exists(-60L)) {
/* 755 */       return ppl.getPermissionsFor(-60L).hasPermission(bit);
/*     */     }
/* 757 */     return (ppl.exists(-50L) && ppl
/* 758 */       .getPermissionsFor(-50L).hasPermission(bit));
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
/* 769 */     return isGuest(is, creature.getWurmId());
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
/* 780 */     if (is.isOwner(playerId)) {
/* 781 */       return true;
/*     */     }
/* 783 */     Long id = Long.valueOf(is.getWurmId());
/* 784 */     PermissionsPlayerList ppl = objectSettings.get(id);
/* 785 */     if (ppl == null)
/*     */     {
/* 787 */       return false;
/*     */     }
/*     */     
/* 790 */     return ppl.exists(playerId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean canManage(PermissionsPlayerList.ISettings is, Creature creature, @Nullable Village brandVillage) {
/* 801 */     return hasPermission(is, creature, brandVillage, Animal2Permissions.MANAGE.getBit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean mayCommand(PermissionsPlayerList.ISettings is, Creature creature, @Nullable Village brandVillage) {
/* 811 */     if (creature.getPower() > 1)
/* 812 */       return true; 
/* 813 */     return hasPermission(is, creature, brandVillage, Animal2Permissions.COMMANDER.getBit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean mayPassenger(PermissionsPlayerList.ISettings is, Creature creature, @Nullable Village brandVillage) {
/* 823 */     if (creature.getPower() > 1)
/* 824 */       return true; 
/* 825 */     return hasPermission(is, creature, brandVillage, Animal2Permissions.PASSENGER.getBit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean mayAccessHold(PermissionsPlayerList.ISettings is, Creature creature, @Nullable Village brandVillage) {
/* 835 */     if (creature.getPower() > 1)
/* 836 */       return true; 
/* 837 */     return hasPermission(is, creature, brandVillage, Animal2Permissions.ACCESS_HOLD.getBit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean mayUse(PermissionsPlayerList.ISettings is, Creature creature, @Nullable Village brandVillage) {
/* 847 */     if (creature.getPower() > 1)
/* 848 */       return true; 
/* 849 */     return hasPermission(is, creature, brandVillage, WagonerPermissions.CANUSE.getBit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean publicMayUse(PermissionsPlayerList.ISettings is) {
/* 859 */     Long id = Long.valueOf(is.getWurmId());
/* 860 */     PermissionsPlayerList ppl = objectSettings.get(id);
/* 861 */     if (ppl == null)
/*     */     {
/* 863 */       return false; } 
/* 864 */     return (ppl.exists(-50L) && ppl
/* 865 */       .getPermissionsFor(-50L).hasPermission(WagonerPermissions.CANUSE.getBit()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isExcluded(PermissionsPlayerList.ISettings is, Creature creature) {
/* 875 */     if (creature.getPower() > 1)
/* 876 */       return false; 
/* 877 */     return hasPermission(is, creature, null, Animal2Permissions.EXCLUDE.getBit());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\AnimalSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */