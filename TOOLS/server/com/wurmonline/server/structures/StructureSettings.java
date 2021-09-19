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
/*     */ public final class StructureSettings
/*     */   implements MiscConstants
/*     */ {
/*     */   public enum StructurePermissions
/*     */     implements Permissions.IPermission
/*     */   {
/*  50 */     MANAGE(0, "Manage Item", "Manage", "Item", "Allows managing of these permissions."),
/*  51 */     PASS(1, "May Enter", "May", "Enter", "Allows entry even through locked doors (if door is Controlled By Building) and the abililty to improve and repair the building and items inside."),
/*  52 */     MODIFY(2, "Modify Building", "Modify", "Building", "Allows destroying, building and rotating of floors, roofs and walls - needs deed permissions to add or remove tiles."),
/*  53 */     PICKUP(3, "Pickup Items", "Pickup", "Items", "Allows picking up of items and Pull/Push/Turn (overrides deed setting), also allows Hauling Up and Down of items."),
/*  54 */     PICKUP_PLANTED(4, "Pickup Planted", "Pickup", "Planted", "Allows picking up of planted items (overrides deed setting). Requires 'Pickup Items' as well."),
/*  55 */     PLACE_MERCHANTS(5, "Place Merchants", "Place", "Merchants", "Allows planting of merchants and traders (overrides deed setting)."),
/*  56 */     LOAD(6, "May Load", "May", "(Un)Load", "Allows (Un)loading of items (overrides deed setting). Requires 'Pickup Items' to load items they dont own, will also requires 'Pickup Planted' if item is planted."),
/*  57 */     EXCLUDE(15, "Deny All", "Deny", "All", "Deny all access.");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final byte bit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String description;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String header1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String header2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String hover;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     StructurePermissions(int aBit, String aDescription, String aHeader1, String aHeader2, String aHover) {
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
/* 144 */     private static final Permissions.IPermission[] types = (Permissions.IPermission[])values();
/*     */     public int getValue() { return 1 << this.bit; }
/*     */     public String getDescription() { return this.description; }
/*     */     public String getHeader1() { return this.header1; }
/*     */     public String getHeader2() { return this.header2; } public static Permissions.IPermission[] getPermissions() {
/* 149 */       return types;
/*     */     } public String getHover() {
/*     */       return this.hover;
/*     */     } static {
/*     */     
/* 154 */     } } private static final Logger logger = Logger.getLogger(StructureSettings.class.getName());
/*     */   private static final String GET_ALL_SETTINGS = "SELECT * FROM STRUCTUREGUESTS";
/*     */   private static final String ADD_PLAYER = "INSERT INTO STRUCTUREGUESTS (SETTINGS,STRUCTUREID,GUESTID) VALUES(?,?,?)";
/*     */   private static final String DELETE_SETTINGS = "DELETE FROM STRUCTUREGUESTS WHERE STRUCTUREID=?";
/*     */   private static final String REMOVE_PLAYER = "DELETE FROM STRUCTUREGUESTS WHERE STRUCTUREID=? AND GUESTID=?";
/*     */   private static final String UPDATE_PLAYER = "UPDATE STRUCTUREGUESTS SET SETTINGS=? WHERE STRUCTUREID=? AND GUESTID=?";
/* 160 */   private static int MAX_PLAYERS_PER_OBJECT = 1000;
/* 161 */   private static Map<Long, PermissionsPlayerList> objectSettings = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
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
/* 175 */     logger.log(Level.INFO, "Loading all structure settings.");
/* 176 */     long start = System.nanoTime();
/* 177 */     long count = 0L;
/* 178 */     Connection dbcon = null;
/* 179 */     PreparedStatement ps = null;
/* 180 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 183 */       dbcon = DbConnector.getZonesDbCon();
/* 184 */       ps = dbcon.prepareStatement("SELECT * FROM STRUCTUREGUESTS");
/* 185 */       rs = ps.executeQuery();
/* 186 */       while (rs.next())
/*     */       {
/* 188 */         long wurmId = rs.getLong("STRUCTUREID");
/* 189 */         long guestId = rs.getLong("GUESTID");
/* 190 */         int settings = rs.getInt("SETTINGS");
/* 191 */         if (settings == 0)
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 196 */           settings = StructurePermissions.PASS.getValue() + StructurePermissions.LOAD.getValue() + StructurePermissions.MODIFY.getValue() + StructurePermissions.PICKUP.getValue() + StructurePermissions.PLACE_MERCHANTS.getValue(); } 
/* 197 */         add(wurmId, guestId, settings);
/* 198 */         count++;
/*     */       }
/*     */     
/* 201 */     } catch (SQLException ex) {
/*     */       
/* 203 */       logger.log(Level.WARNING, "Failed to load settings for structures.", ex);
/*     */     }
/*     */     finally {
/*     */       
/* 207 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 208 */       DbConnector.returnConnection(dbcon);
/* 209 */       long end = System.nanoTime();
/* 210 */       logger.log(Level.INFO, "Loaded " + count + " structure settings (guests). That took " + ((float)(end - start) / 1000000.0F) + " ms.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getMaxAllowed() {
/* 220 */     return Servers.isThisATestServer() ? 10 : MAX_PLAYERS_PER_OBJECT;
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
/* 232 */     Long id = Long.valueOf(wurmId);
/* 233 */     if (objectSettings.containsKey(id)) {
/*     */ 
/*     */       
/* 236 */       PermissionsPlayerList permissionsPlayerList = objectSettings.get(id);
/* 237 */       return permissionsPlayerList.add(playerId, settings);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 242 */     PermissionsPlayerList ppl = new PermissionsPlayerList();
/* 243 */     objectSettings.put(id, ppl);
/* 244 */     return ppl.add(playerId, settings);
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
/* 257 */     PermissionsByPlayer pbp = add(wurmId, playerId, settings);
/* 258 */     if (pbp == null) {
/* 259 */       dbAddPlayer(wurmId, playerId, settings, true);
/* 260 */     } else if (pbp.getSettings() != settings) {
/* 261 */       dbAddPlayer(wurmId, playerId, settings, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removePlayer(long wurmId, long playerId) {
/* 271 */     Long id = Long.valueOf(wurmId);
/* 272 */     if (objectSettings.containsKey(id)) {
/*     */       
/* 274 */       PermissionsPlayerList ppl = objectSettings.get(id);
/* 275 */       ppl.remove(playerId);
/* 276 */       dbRemovePlayer(wurmId, playerId);
/*     */       
/* 278 */       if (ppl.isEmpty()) {
/* 279 */         objectSettings.remove(id);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 284 */       logger.log(Level.WARNING, "Failed to remove player " + playerId + " from settings for structure " + wurmId + ".");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void dbAddPlayer(long wurmId, long playerId, int settings, boolean add) {
/* 290 */     Connection dbcon = null;
/* 291 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 294 */       dbcon = DbConnector.getZonesDbCon();
/* 295 */       if (add) {
/* 296 */         ps = dbcon.prepareStatement("INSERT INTO STRUCTUREGUESTS (SETTINGS,STRUCTUREID,GUESTID) VALUES(?,?,?)");
/*     */       } else {
/* 298 */         ps = dbcon.prepareStatement("UPDATE STRUCTUREGUESTS SET SETTINGS=? WHERE STRUCTUREID=? AND GUESTID=?");
/*     */       } 
/* 300 */       ps.setInt(1, settings);
/* 301 */       ps.setLong(2, wurmId);
/* 302 */       ps.setLong(3, playerId);
/* 303 */       ps.executeUpdate();
/*     */     }
/* 305 */     catch (SQLException ex) {
/*     */       
/* 307 */       logger.log(Level.WARNING, "Failed to " + (add ? "add" : "update") + " player (" + playerId + ") for structure with id " + wurmId, ex);
/*     */     }
/*     */     finally {
/*     */       
/* 311 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 312 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void dbRemovePlayer(long wurmId, long playerId) {
/* 318 */     Connection dbcon = null;
/* 319 */     PreparedStatement ps = null;
/* 320 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 323 */       dbcon = DbConnector.getZonesDbCon();
/* 324 */       ps = dbcon.prepareStatement("DELETE FROM STRUCTUREGUESTS WHERE STRUCTUREID=? AND GUESTID=?");
/* 325 */       ps.setLong(1, wurmId);
/* 326 */       ps.setLong(2, playerId);
/* 327 */       ps.executeUpdate();
/*     */     }
/* 329 */     catch (SQLException ex) {
/*     */       
/* 331 */       logger.log(Level.WARNING, "Failed to remove player " + playerId + " from settings for structure " + wurmId + ".", ex);
/*     */     }
/*     */     finally {
/*     */       
/* 335 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 336 */       DbConnector.returnConnection(dbcon);
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
/* 347 */     Long id = Long.valueOf(wurmId);
/* 348 */     return objectSettings.containsKey(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void remove(long wurmId) {
/* 358 */     Long id = Long.valueOf(wurmId);
/* 359 */     if (objectSettings.containsKey(id)) {
/*     */       
/* 361 */       dbRemove(wurmId);
/* 362 */       objectSettings.remove(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void dbRemove(long wurmId) {
/* 372 */     Connection dbcon = null;
/* 373 */     PreparedStatement ps = null;
/* 374 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 377 */       dbcon = DbConnector.getZonesDbCon();
/* 378 */       ps = dbcon.prepareStatement("DELETE FROM STRUCTUREGUESTS WHERE STRUCTUREID=?");
/* 379 */       ps.setLong(1, wurmId);
/* 380 */       ps.executeUpdate();
/*     */     }
/* 382 */     catch (SQLException ex) {
/*     */       
/* 384 */       logger.log(Level.WARNING, "Failed to delete settings for structure " + wurmId + ".", ex);
/*     */     }
/*     */     finally {
/*     */       
/* 388 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 389 */       DbConnector.returnConnection(dbcon);
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
/* 401 */     Long id = Long.valueOf(wurmId);
/* 402 */     PermissionsPlayerList ppl = objectSettings.get(id);
/* 403 */     if (ppl == null)
/* 404 */       return new PermissionsPlayerList(); 
/* 405 */     return ppl;
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
/* 418 */     if (is.isOwner(creature)) {
/* 419 */       return (bit != MineDoorSettings.MinedoorPermissions.EXCLUDE.getBit());
/*     */     }
/* 421 */     Long id = Long.valueOf(is.getWurmId());
/* 422 */     PermissionsPlayerList ppl = objectSettings.get(id);
/* 423 */     if (ppl == null)
/*     */     {
/* 425 */       return false;
/*     */     }
/* 427 */     if (ppl.exists(creature.getWurmId())) {
/* 428 */       return ppl.getPermissionsFor(creature.getWurmId()).hasPermission(bit);
/*     */     }
/* 430 */     if (is.isCitizen(creature) && ppl
/* 431 */       .exists(-30L)) {
/* 432 */       return ppl.getPermissionsFor(-30L).hasPermission(bit);
/*     */     }
/* 434 */     if (is.isAllied(creature) && ppl
/* 435 */       .exists(-20L)) {
/* 436 */       return ppl.getPermissionsFor(-20L).hasPermission(bit);
/*     */     }
/* 438 */     if (is.isSameKingdom(creature) && ppl
/* 439 */       .exists(-40L)) {
/* 440 */       return ppl.getPermissionsFor(-40L).hasPermission(bit);
/*     */     }
/* 442 */     return (ppl.exists(-50L) && ppl
/* 443 */       .getPermissionsFor(-50L).hasPermission(bit));
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
/* 454 */     return isGuest(is, creature.getWurmId());
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
/* 465 */     if (is.isOwner(playerId)) {
/* 466 */       return true;
/*     */     }
/* 468 */     Long id = Long.valueOf(is.getWurmId());
/* 469 */     PermissionsPlayerList ppl = objectSettings.get(id);
/* 470 */     if (ppl == null)
/*     */     {
/* 472 */       return false;
/*     */     }
/* 474 */     if (ppl.exists(playerId))
/*     */     {
/* 476 */       return !ppl.getPermissionsFor(playerId).hasPermission(StructurePermissions.EXCLUDE.getBit()); } 
/* 477 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean canManage(PermissionsPlayerList.ISettings is, Creature creature) {
/* 488 */     return hasPermission(is, creature, StructurePermissions.MANAGE.getBit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean mayModify(PermissionsPlayerList.ISettings is, Creature creature) {
/* 498 */     if (creature.getPower() > 1)
/* 499 */       return true; 
/* 500 */     return hasPermission(is, creature, StructurePermissions.MODIFY.getBit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean mayPass(PermissionsPlayerList.ISettings is, Creature creature) {
/* 510 */     if (creature.getPower() > 1)
/* 511 */       return true; 
/* 512 */     return hasPermission(is, creature, StructurePermissions.PASS.getBit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean mayPickup(PermissionsPlayerList.ISettings is, Creature creature) {
/* 522 */     if (creature.getPower() > 1)
/* 523 */       return true; 
/* 524 */     return hasPermission(is, creature, StructurePermissions.PICKUP.getBit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean mayPickupPlanted(PermissionsPlayerList.ISettings is, Creature creature) {
/* 534 */     if (creature.getPower() > 1)
/* 535 */       return true; 
/* 536 */     return hasPermission(is, creature, StructurePermissions.PICKUP_PLANTED.getBit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean mayPlaceMerchants(PermissionsPlayerList.ISettings is, Creature creature) {
/* 546 */     if (creature.getPower() > 1)
/* 547 */       return true; 
/* 548 */     return hasPermission(is, creature, StructurePermissions.PLACE_MERCHANTS.getBit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean mayLoad(PermissionsPlayerList.ISettings is, Creature creature) {
/* 558 */     if (creature.getPower() > 1)
/* 559 */       return true; 
/* 560 */     return hasPermission(is, creature, StructurePermissions.LOAD.getBit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isExcluded(PermissionsPlayerList.ISettings is, Creature creature) {
/* 570 */     if (creature.getPower() > 1)
/* 571 */       return false; 
/* 572 */     return hasPermission(is, creature, StructurePermissions.EXCLUDE.getBit());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\StructureSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */