/*     */ package com.wurmonline.server.creatures;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Servers;
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
/*     */ public class MineDoorSettings
/*     */   implements MiscConstants
/*     */ {
/*     */   public enum MinedoorPermissions
/*     */     implements Permissions.IPermission
/*     */   {
/*  49 */     MANAGE(0, "Manage Item", "Manage", "Item", "Allows managing of these permissions."),
/*  50 */     PASS(1, "Pass Door", "Pass", "Mine Door", "Allows entry through this mine door."),
/*  51 */     EXCLUDE(15, "Deny All", "Deny", "All", "Deny all access.");
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
/*     */     MinedoorPermissions(int aBit, String aDescription, String aHeader1, String aHeader2, String aHover) {
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
/* 138 */     private static final Permissions.Allow[] types = Permissions.Allow.values();
/*     */     public int getValue() { return 1 << this.bit; }
/*     */     public String getDescription() { return this.description; }
/*     */     public String getHeader1() { return this.header1; }
/*     */     public String getHeader2() { return this.header2; } public static Permissions.IPermission[] getPermissions() {
/* 143 */       return (Permissions.IPermission[])types;
/*     */     } public String getHover() {
/*     */       return this.hover;
/*     */     } static {
/*     */     
/* 148 */     } } private static final Logger logger = Logger.getLogger(MineDoorSettings.class.getName());
/*     */   private static final String GET_ALL_SETTINGS = "SELECT * FROM MDPERMS";
/*     */   private static final String ADD_PLAYER = "INSERT INTO MDPERMS (SETTINGS,ID,PERMITTED) VALUES(?,?,?)";
/*     */   private static final String DELETE_SETTINGS = "DELETE FROM MDPERMS WHERE ID=?";
/*     */   private static final String REMOVE_PLAYER = "DELETE FROM MDPERMS WHERE ID=? AND PERMITTED=?";
/*     */   private static final String UPDATE_PLAYER = "UPDATE MDPERMS SET SETTINGS=? WHERE ID=? AND PERMITTED=?";
/* 154 */   private static int MAX_PLAYERS_PER_OBJECT = 1000;
/* 155 */   private static Map<Long, PermissionsPlayerList> objectSettings = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
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
/* 169 */     logger.log(Level.INFO, "Loading all minedoor settings.");
/* 170 */     long start = System.nanoTime();
/* 171 */     long count = 0L;
/* 172 */     Connection dbcon = null;
/* 173 */     PreparedStatement ps = null;
/* 174 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 177 */       dbcon = DbConnector.getZonesDbCon();
/* 178 */       ps = dbcon.prepareStatement("SELECT * FROM MDPERMS");
/* 179 */       rs = ps.executeQuery();
/* 180 */       while (rs.next())
/*     */       {
/* 182 */         int minedoorId = rs.getInt("ID");
/* 183 */         long playerId = rs.getLong("PERMITTED");
/* 184 */         int settings = rs.getInt("SETTINGS");
/* 185 */         if (settings == 0)
/* 186 */           settings = MinedoorPermissions.PASS.getValue(); 
/* 187 */         add(minedoorId, playerId, settings);
/* 188 */         count++;
/*     */       }
/*     */     
/* 191 */     } catch (SQLException ex) {
/*     */       
/* 193 */       logger.log(Level.WARNING, "Failed to load settings for minedoors.", ex);
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 198 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 199 */       DbConnector.returnConnection(dbcon);
/* 200 */       long end = System.nanoTime();
/* 201 */       logger.log(Level.INFO, "Loaded " + count + " minedoor settings. That took " + ((float)(end - start) / 1000000.0F) + " ms.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getMaxAllowed() {
/* 211 */     return Servers.isThisATestServer() ? 10 : MAX_PLAYERS_PER_OBJECT;
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
/*     */   private static PermissionsByPlayer add(int minedoorId, long playerId, int settings) {
/* 223 */     Long id = Long.valueOf(minedoorId);
/* 224 */     if (objectSettings.containsKey(id)) {
/*     */ 
/*     */       
/* 227 */       PermissionsPlayerList permissionsPlayerList = objectSettings.get(id);
/* 228 */       return permissionsPlayerList.add(playerId, settings);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 233 */     PermissionsPlayerList ppl = new PermissionsPlayerList();
/* 234 */     objectSettings.put(id, ppl);
/* 235 */     return ppl.add(playerId, settings);
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
/*     */   public static void addPlayer(int minedoorId, long playerId, int settings) {
/* 248 */     PermissionsByPlayer pbp = add(minedoorId, playerId, settings);
/* 249 */     if (pbp == null) {
/* 250 */       dbAddPlayer(minedoorId, playerId, settings, true);
/* 251 */     } else if (pbp.getSettings() != settings) {
/* 252 */       dbAddPlayer(minedoorId, playerId, settings, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removePlayer(int minedoorId, long playerId) {
/* 263 */     Long id = Long.valueOf(minedoorId);
/* 264 */     if (objectSettings.containsKey(id)) {
/*     */       
/* 266 */       PermissionsPlayerList ppl = objectSettings.get(id);
/* 267 */       ppl.remove(playerId);
/* 268 */       dbRemovePlayer(minedoorId, playerId);
/*     */       
/* 270 */       if (ppl.isEmpty()) {
/* 271 */         objectSettings.remove(id);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 276 */       logger.log(Level.WARNING, "Failed to remove player " + playerId + " from settings for minedoor " + minedoorId + ".");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void dbAddPlayer(int minedoorId, long playerId, int settings, boolean add) {
/* 282 */     Connection dbcon = null;
/* 283 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 286 */       dbcon = DbConnector.getZonesDbCon();
/* 287 */       if (add) {
/* 288 */         ps = dbcon.prepareStatement("INSERT INTO MDPERMS (SETTINGS,ID,PERMITTED) VALUES(?,?,?)");
/*     */       } else {
/* 290 */         ps = dbcon.prepareStatement("UPDATE MDPERMS SET SETTINGS=? WHERE ID=? AND PERMITTED=?");
/*     */       } 
/* 292 */       ps.setInt(1, settings);
/* 293 */       ps.setInt(2, minedoorId);
/* 294 */       ps.setLong(3, playerId);
/* 295 */       ps.executeUpdate();
/*     */     }
/* 297 */     catch (SQLException ex) {
/*     */       
/* 299 */       logger.log(Level.WARNING, "Failed to " + (add ? "add" : "update") + " player (" + playerId + ") for minedoor with id " + minedoorId, ex);
/*     */     }
/*     */     finally {
/*     */       
/* 303 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 304 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void dbRemovePlayer(int minedoorId, long playerId) {
/* 310 */     Connection dbcon = null;
/* 311 */     PreparedStatement ps = null;
/* 312 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 315 */       dbcon = DbConnector.getZonesDbCon();
/* 316 */       ps = dbcon.prepareStatement("DELETE FROM MDPERMS WHERE ID=? AND PERMITTED=?");
/* 317 */       ps.setInt(1, minedoorId);
/* 318 */       ps.setLong(2, playerId);
/* 319 */       ps.executeUpdate();
/*     */     }
/* 321 */     catch (SQLException ex) {
/*     */       
/* 323 */       logger.log(Level.WARNING, "Failed to remove player " + playerId + " from settings for minedoor " + minedoorId + ".", ex);
/*     */     }
/*     */     finally {
/*     */       
/* 327 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 328 */       DbConnector.returnConnection(dbcon);
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
/* 339 */     Long id = Long.valueOf(wurmId);
/* 340 */     return objectSettings.containsKey(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void remove(int minedoorId) {
/* 350 */     Long id = Long.valueOf(minedoorId);
/* 351 */     if (objectSettings.containsKey(id)) {
/*     */       
/* 353 */       dbRemove(minedoorId);
/* 354 */       objectSettings.remove(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void dbRemove(int minedoorId) {
/* 364 */     Connection dbcon = null;
/* 365 */     PreparedStatement ps = null;
/* 366 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 369 */       dbcon = DbConnector.getZonesDbCon();
/* 370 */       ps = dbcon.prepareStatement("DELETE FROM MDPERMS WHERE ID=?");
/* 371 */       ps.setInt(1, minedoorId);
/* 372 */       ps.executeUpdate();
/*     */     }
/* 374 */     catch (SQLException ex) {
/*     */       
/* 376 */       logger.log(Level.WARNING, "Failed to delete settings for minedoor " + minedoorId + ".", ex);
/*     */     }
/*     */     finally {
/*     */       
/* 380 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 381 */       DbConnector.returnConnection(dbcon);
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
/* 393 */     Long id = Long.valueOf(wurmId);
/* 394 */     PermissionsPlayerList ppl = objectSettings.get(id);
/* 395 */     if (ppl == null)
/* 396 */       return new PermissionsPlayerList(); 
/* 397 */     return ppl;
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
/*     */   private static boolean hasPermission(PermissionsPlayerList.ISettings is, long objectId, Creature creature, int bit) {
/* 412 */     if (is.isOwner(creature)) {
/* 413 */       return (bit != MinedoorPermissions.EXCLUDE.getBit());
/*     */     }
/* 415 */     Long id = Long.valueOf(objectId);
/* 416 */     PermissionsPlayerList ppl = objectSettings.get(id);
/* 417 */     if (ppl == null)
/*     */     {
/* 419 */       return false;
/*     */     }
/* 421 */     if (ppl.exists(creature.getWurmId())) {
/* 422 */       return ppl.getPermissionsFor(creature.getWurmId()).hasPermission(bit);
/*     */     }
/* 424 */     if (is.isCitizen(creature) && ppl
/* 425 */       .exists(-30L)) {
/* 426 */       return ppl.getPermissionsFor(-30L).hasPermission(bit);
/*     */     }
/* 428 */     if (is.isAllied(creature) && ppl
/* 429 */       .exists(-20L)) {
/* 430 */       return ppl.getPermissionsFor(-20L).hasPermission(bit);
/*     */     }
/* 432 */     if (is.isSameKingdom(creature) && ppl
/* 433 */       .exists(-40L)) {
/* 434 */       return ppl.getPermissionsFor(-40L).hasPermission(bit);
/*     */     }
/* 436 */     return (ppl.exists(-50L) && ppl
/* 437 */       .getPermissionsFor(-50L).hasPermission(bit));
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
/* 448 */     return isGuest(is, creature.getWurmId());
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
/* 459 */     if (is.isOwner(playerId)) {
/* 460 */       return true;
/*     */     }
/* 462 */     Long id = Long.valueOf(is.getWurmId());
/* 463 */     PermissionsPlayerList ppl = objectSettings.get(id);
/* 464 */     if (ppl == null)
/*     */     {
/* 466 */       return false;
/*     */     }
/*     */     
/* 469 */     return ppl.exists(playerId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean canManage(PermissionsPlayerList.ISettings is, long objectId, Creature creature) {
/* 480 */     return hasPermission(is, objectId, creature, MinedoorPermissions.MANAGE.getBit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean mayPass(PermissionsPlayerList.ISettings is, long objectId, Creature creature) {
/* 490 */     if (creature.getPower() > 1)
/* 491 */       return true; 
/* 492 */     return hasPermission(is, objectId, creature, MinedoorPermissions.PASS.getBit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isExcluded(PermissionsPlayerList.ISettings is, long objectId, Creature creature) {
/* 502 */     if (creature.getPower() > 1)
/* 503 */       return false; 
/* 504 */     return hasPermission(is, objectId, creature, MinedoorPermissions.EXCLUDE.getBit());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\MineDoorSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */