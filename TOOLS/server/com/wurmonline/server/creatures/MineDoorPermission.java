/*      */ package com.wurmonline.server.creatures;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Point;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.behaviours.TileRockBehaviour;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Permissions;
/*      */ import com.wurmonline.server.players.PermissionsHistories;
/*      */ import com.wurmonline.server.players.PermissionsPlayerList;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.structures.StructureSettings;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.villages.NoSuchVillageException;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VirtualZone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nullable;
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
/*      */ public final class MineDoorPermission
/*      */   implements MiscConstants, PermissionsPlayerList.ISettings
/*      */ {
/*   72 */   private static final Logger logger = Logger.getLogger(MineDoorPermission.class.getName());
/*      */   
/*      */   private static final String CREATE_MINEDOOR = "INSERT INTO MINEDOOR (CREATOR,VILLAGE,ALLOWALL,ALLOWALLIES,NAME,SETTINGS,ID) VALUES (?,?,?,?,?,?,?)";
/*      */   
/*      */   private static final String UPDATE_MINEDOOR = "UPDATE MINEDOOR SET CREATOR=?,VILLAGE=?,ALLOWALL=?,ALLOWALLIES=?,NAME=?,SETTINGS=? WHERE ID=?";
/*      */   
/*      */   private static final String DELETE_MINEDOOR = "DELETE FROM MINEDOOR WHERE ID=?";
/*      */   
/*      */   private static final String GET_ALL_MINEDOORS = "SELECT * FROM MINEDOOR";
/*      */   
/*   82 */   private static final Map<Integer, MineDoorPermission> mineDoorPermissions = new ConcurrentHashMap<>();
/*      */   
/*      */   private final int id;
/*      */   private final long wurmId;
/*   86 */   private long creator = -10L;
/*   87 */   private int villageId = -1;
/*   88 */   private String name = "";
/*      */   
/*      */   private boolean allowAll = false;
/*      */   private boolean allowAllies = false;
/*   92 */   private Village village = null;
/*      */   
/*   94 */   private Permissions permissions = new Permissions();
/*      */   
/*   96 */   private long closingTime = 0L;
/*      */   
/*   98 */   Set<Creature> creaturesOpened = new HashSet<>();
/*   99 */   Set<VirtualZone> watchers = new HashSet<>();
/*      */ 
/*      */   
/*      */   boolean open = false;
/*      */ 
/*      */ 
/*      */   
/*      */   public MineDoorPermission(int tilex, int tiley, long _creator, Village currentvillage, boolean _allowAll, boolean _allowAllies, String _name, int settings) {
/*  107 */     this.id = makeId(tilex, tiley);
/*  108 */     this.wurmId = Tiles.getTileId(tilex, tiley, 0);
/*  109 */     this.creator = _creator;
/*  110 */     this.allowAllies = _allowAllies;
/*  111 */     this.name = _name;
/*  112 */     this.permissions.setPermissionBits(settings);
/*  113 */     this.village = currentvillage;
/*  114 */     if (this.village != null) {
/*      */ 
/*      */       
/*  117 */       setIsManaged(true, null);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  122 */       setIsManaged(false, null);
/*      */     } 
/*      */     
/*      */     try {
/*  126 */       save();
/*      */     }
/*  128 */     catch (IOException e) {
/*      */       
/*  130 */       logger.log(Level.WARNING, e.getMessage(), e);
/*      */     } 
/*  132 */     addPermission(this);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private MineDoorPermission(int _id, long _creator, int _villageid, boolean _allowall, boolean _allowAllies, String _name, int settings) {
/*  138 */     this.id = _id;
/*  139 */     this.wurmId = Tiles.getTileId(decodeTileX(this.id), decodeTileY(this.id), 0);
/*  140 */     this.creator = _creator;
/*  141 */     this.villageId = _villageid;
/*  142 */     this.allowAll = _allowall;
/*  143 */     this.allowAllies = _allowAllies;
/*  144 */     this.name = _name;
/*  145 */     this.permissions.setPermissionBits(settings);
/*  146 */     addPermission(this);
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
/*      */   public int getMaxAllowed() {
/*  160 */     return MineDoorSettings.getMaxAllowed();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setVillage(@Nullable Village vill) {
/*  169 */     this.village = vill;
/*      */     
/*  171 */     if (vill != null) {
/*      */       
/*  173 */       setIsManaged(true, null);
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  178 */     else if (this.villageId != -1) {
/*  179 */       setIsManaged(false, null);
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
/*      */   public boolean setVillageId(int newid) {
/*  192 */     if (this.villageId == newid)
/*  193 */       return false; 
/*  194 */     this.villageId = newid;
/*  195 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getVillageId() {
/*  205 */     return this.villageId;
/*      */   }
/*      */ 
/*      */   
/*      */   public Village getVillage() {
/*  210 */     return this.village;
/*      */   }
/*      */ 
/*      */   
/*      */   private Village getPermissionsVillage() {
/*  215 */     Village vill = getManagedByVillage();
/*  216 */     if (vill != null) {
/*  217 */       return vill;
/*      */     }
/*  219 */     return getOwnerVillage();
/*      */   }
/*      */ 
/*      */   
/*      */   public final Village getOwnerVillage() {
/*  224 */     return Villages.getVillageForCreature(this.creator);
/*      */   }
/*      */ 
/*      */   
/*      */   public final Village getManagedByVillage() {
/*  229 */     if (this.villageId >= 0) {
/*      */       
/*      */       try {
/*      */         
/*  233 */         return Villages.getVillage(this.villageId);
/*      */       }
/*  235 */       catch (NoSuchVillageException e) {
/*      */ 
/*      */         
/*  238 */         setVillageId(-1);
/*      */       } 
/*      */     }
/*  241 */     return null;
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
/*      */   public boolean setAllowAll(boolean allow) {
/*  254 */     if (this.allowAll == allow)
/*  255 */       return false; 
/*  256 */     this.allowAll = allow;
/*  257 */     return true;
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
/*      */   public boolean isAllowAll() {
/*  269 */     return this.allowAll;
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
/*      */   public boolean setAllowAllies(boolean allow) {
/*  282 */     if (this.allowAllies == allow)
/*  283 */       return false; 
/*  284 */     this.allowAllies = allow;
/*  285 */     return true;
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
/*      */   public boolean isAllowAllies() {
/*  297 */     return this.allowAllies;
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
/*      */   public boolean setController(long newid) {
/*  309 */     if (this.creator == newid)
/*  310 */       return false; 
/*  311 */     this.creator = newid;
/*  312 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getController() {
/*  322 */     return this.creator;
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeMDPerm(long creatureId) {
/*  327 */     MineDoorSettings.removePlayer(this.id, creatureId);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addMDPerm(long creatureId, int settings) {
/*  332 */     MineDoorSettings.addPlayer(this.id, creatureId, settings);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void removePermission(MineDoorPermission perm) {
/*  337 */     mineDoorPermissions.remove(Integer.valueOf(perm.id));
/*  338 */     int x = perm.getTileX();
/*  339 */     int y = perm.getTileY();
/*      */     
/*      */     try {
/*  342 */       Zones.getZone(perm.getTileX(), perm.getTileY(), true).getOrCreateTile(x, y).removeMineDoor(perm);
/*      */     }
/*  344 */     catch (NoSuchZoneException e) {
/*      */       
/*  346 */       logger.log(Level.SEVERE, "Could not find zone for removing a mine door at " + x + ":" + y + "!");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void addPermission(MineDoorPermission perm) {
/*  352 */     mineDoorPermissions.put(Integer.valueOf(perm.id), perm);
/*      */     
/*  354 */     int x = perm.getTileX();
/*  355 */     int y = perm.getTileY();
/*      */     
/*      */     try {
/*  358 */       if (isVisibleDoorTile(Tiles.decodeType(Server.surfaceMesh.getTile(x, y)))) {
/*      */         
/*  360 */         Point highestCorner = TileRockBehaviour.findHighestCorner(x, y);
/*  361 */         Point nextHighestCorner = TileRockBehaviour.findNextHighestCorner(x, y, highestCorner);
/*      */         
/*  363 */         if (highestCorner.getH() == nextHighestCorner.getH()) {
/*  364 */           Zones.getZone(perm.getTileX(), perm.getTileY(), true).getOrCreateTile(x, y).addMineDoor(perm);
/*      */         }
/*      */       } 
/*  367 */     } catch (NoSuchZoneException e) {
/*      */       
/*  369 */       logger.log(Level.SEVERE, "Could not find zone for adding a mine door at " + x + ":" + y + "!");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean isVisibleDoorTile(int tile) {
/*  375 */     return (tile == 27 || tile == 25 || tile == 28 || tile == 29);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final MineDoorPermission getPermission(long wurmId) {
/*  382 */     int tilex = Tiles.decodeTileX(wurmId);
/*  383 */     int tiley = Tiles.decodeTileY(wurmId);
/*  384 */     return getPermission(tilex, tiley);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final MineDoorPermission getPermission(int tilex, int tiley) {
/*  389 */     return getPermission(makeId(tilex, tiley));
/*      */   }
/*      */ 
/*      */   
/*      */   public static final MineDoorPermission getPermission(int mineDoorId) {
/*  394 */     return mineDoorPermissions.get(Integer.valueOf(mineDoorId));
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
/*      */   public final boolean canHavePermissions() {
/*  406 */     return true;
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
/*      */   public final boolean mayShowPermissions(Creature creature) {
/*  418 */     return mayManage(creature);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean canManage(Creature creature) {
/*  429 */     if (MineDoorSettings.isExcluded(this, this.id, creature)) {
/*  430 */       return false;
/*      */     }
/*  432 */     if (MineDoorSettings.canManage(this, this.id, creature))
/*  433 */       return true; 
/*  434 */     if (creature.getCitizenVillage() == null) {
/*  435 */       return false;
/*      */     }
/*  437 */     Village vill = getManagedByVillage();
/*  438 */     if (vill == null)
/*  439 */       return false; 
/*  440 */     if (!vill.isCitizen(creature))
/*  441 */       return false; 
/*  442 */     return vill.isActionAllowed((short)364, creature);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayManage(Creature creature) {
/*  452 */     if (creature.getPower() > 1)
/*  453 */       return true; 
/*  454 */     return canManage(creature);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean maySeeHistory(Creature creature) {
/*  464 */     if (creature.getPower() > 1)
/*  465 */       return true; 
/*  466 */     return isOwner(creature);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayPass(Creature creature) {
/*  476 */     return (!MineDoorSettings.isExcluded(this, this.id, creature) && 
/*  477 */       MineDoorSettings.mayPass(this, this.id, creature));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void save() throws IOException {
/*  484 */     Connection dbcon = null;
/*  485 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  488 */       dbcon = DbConnector.getZonesDbCon();
/*  489 */       ps = dbcon.prepareStatement("UPDATE MINEDOOR SET CREATOR=?,VILLAGE=?,ALLOWALL=?,ALLOWALLIES=?,NAME=?,SETTINGS=? WHERE ID=?");
/*  490 */       ps.setLong(1, this.creator);
/*  491 */       ps.setInt(2, this.villageId);
/*  492 */       ps.setBoolean(3, this.allowAll);
/*  493 */       ps.setBoolean(4, this.allowAllies);
/*  494 */       ps.setString(5, this.name);
/*  495 */       ps.setInt(6, this.permissions.getPermissions());
/*  496 */       ps.setInt(7, this.id);
/*  497 */       if (ps.executeUpdate() == 0)
/*      */       {
/*  499 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  500 */         ps = dbcon.prepareStatement("INSERT INTO MINEDOOR (CREATOR,VILLAGE,ALLOWALL,ALLOWALLIES,NAME,SETTINGS,ID) VALUES (?,?,?,?,?,?,?)");
/*  501 */         ps.setLong(1, this.creator);
/*  502 */         ps.setInt(2, this.villageId);
/*  503 */         ps.setBoolean(3, this.allowAll);
/*  504 */         ps.setBoolean(4, this.allowAllies);
/*  505 */         ps.setString(5, this.name);
/*  506 */         ps.setInt(6, this.permissions.getPermissions());
/*  507 */         ps.setInt(7, this.id);
/*  508 */         ps.executeUpdate();
/*      */       }
/*      */     
/*  511 */     } catch (SQLException sqex) {
/*      */       
/*  513 */       logger.log(Level.WARNING, "Failed to create mine door: " + this.id + ", " + this.creator + ", " + this.villageId + ":" + sqex
/*  514 */           .getMessage(), sqex);
/*  515 */       throw new IOException(sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  519 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  520 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void deleteMineDoor(int tilex, int tiley) {
/*  526 */     int mdId = makeId(tilex, tiley);
/*  527 */     Connection dbcon = null;
/*  528 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  531 */       dbcon = DbConnector.getZonesDbCon();
/*  532 */       ps = dbcon.prepareStatement("DELETE FROM MINEDOOR WHERE ID=?");
/*  533 */       ps.setInt(1, mdId);
/*  534 */       ps.executeUpdate();
/*      */     }
/*  536 */     catch (SQLException sqex) {
/*      */       
/*  538 */       logger.log(Level.WARNING, "Failed to delete mine door: " + mdId + ":" + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  542 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  543 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/*  546 */     MineDoorSettings.remove(mdId);
/*  547 */     PermissionsHistories.remove(mdId);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void loadAllMineDoors() {
/*  552 */     Connection dbcon = null;
/*  553 */     PreparedStatement ps = null;
/*  554 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  557 */       dbcon = DbConnector.getZonesDbCon();
/*  558 */       ps = dbcon.prepareStatement("SELECT * FROM MINEDOOR");
/*  559 */       rs = ps.executeQuery();
/*  560 */       while (rs.next())
/*      */       {
/*  562 */         new MineDoorPermission(rs.getInt("ID"), rs.getLong("CREATOR"), rs.getInt("VILLAGE"), rs
/*  563 */             .getBoolean("ALLOWALL"), rs.getBoolean("ALLOWALLIES"), rs
/*  564 */             .getString("NAME"), rs.getInt("SETTINGS"));
/*      */       }
/*      */     }
/*  567 */     catch (SQLException sqex) {
/*      */       
/*  569 */       logger.log(Level.WARNING, "Failed to load all mine doors: " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  573 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  574 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void addWatcher(VirtualZone watcher) {
/*  580 */     if (this.watchers == null)
/*  581 */       this.watchers = new HashSet<>(); 
/*  582 */     if (!this.watchers.contains(watcher)) {
/*  583 */       this.watchers.add(watcher);
/*      */     }
/*      */   }
/*      */   
/*      */   public final void removeWatcher(VirtualZone watcher) {
/*  588 */     if (this.watchers != null)
/*      */     {
/*  590 */       if (this.watchers.contains(watcher)) {
/*  591 */         this.watchers.remove(watcher);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void open(Creature creature) {
/*  598 */     this.open = true;
/*      */     
/*  600 */     if (this.creaturesOpened.isEmpty())
/*      */     {
/*  602 */       for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext(); ) {
/*      */         
/*  604 */         VirtualZone z = it.next();
/*  605 */         if (creature.isVisibleTo(z.getWatcher())) {
/*  606 */           z.openMineDoor(this);
/*      */         }
/*      */       } 
/*      */     }
/*  610 */     this.creaturesOpened.add(creature);
/*      */   }
/*      */ 
/*      */   
/*      */   public void close(Creature creature) {
/*  615 */     this.open = true;
/*      */     
/*  617 */     this.creaturesOpened.remove(creature);
/*  618 */     if (this.creaturesOpened.isEmpty()) {
/*  619 */       for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext(); ) {
/*      */         
/*  621 */         VirtualZone z = it.next();
/*  622 */         z.closeMineDoor(this);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getClosingTime() {
/*  632 */     return this.closingTime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClosingTime(long aClosingTime) {
/*  643 */     this.closingTime = aClosingTime;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isWideOpen() {
/*  648 */     return (getClosingTime() > System.currentTimeMillis());
/*      */   }
/*      */ 
/*      */   
/*      */   public static int makeId(int tilex, int tiley) {
/*  653 */     return (tilex << 16) + tiley;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int decodeTileX(int mdId) {
/*  658 */     return mdId >> 16 & 0xFFFF;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int decodeTileY(int mdId) {
/*  663 */     return mdId & 0xFFFF;
/*      */   }
/*      */ 
/*      */   
/*      */   public static MineDoorPermission[] getAllMineDoors() {
/*  668 */     return (MineDoorPermission[])mineDoorPermissions.values().toArray((Object[])new MineDoorPermission[mineDoorPermissions.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final MineDoorPermission[] getManagedMineDoorsFor(Player player, int villageId, boolean includeAll) {
/*  679 */     Set<MineDoorPermission> mineDoors = new HashSet<>();
/*  680 */     for (MineDoorPermission mineDoor : mineDoorPermissions.values()) {
/*      */ 
/*      */       
/*  683 */       int encodedTile = Server.surfaceMesh.getTile(mineDoor.getTileX(), mineDoor.getTileY());
/*  684 */       byte type = Tiles.decodeType(encodedTile);
/*  685 */       if (!Tiles.isMineDoor(type)) {
/*      */         
/*  687 */         removePermission(mineDoor);
/*      */         continue;
/*      */       } 
/*  690 */       if (mineDoor.canManage((Creature)player)) {
/*  691 */         mineDoors.add(mineDoor);
/*      */       }
/*  693 */       if (includeAll)
/*      */       {
/*  695 */         if ((villageId >= 0 && mineDoor.getVillageId() == villageId) || (mineDoor
/*  696 */           .getVillage() != null && mineDoor.getVillage().isMayor(player.getWurmId()))) {
/*  697 */           mineDoors.add(mineDoor);
/*      */         }
/*      */       }
/*      */     } 
/*  701 */     return mineDoors.<MineDoorPermission>toArray(new MineDoorPermission[mineDoors.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final MineDoorPermission[] getOwnedMinedoorsFor(Player player) {
/*  706 */     Set<MineDoorPermission> mineDoors = new HashSet<>();
/*  707 */     for (MineDoorPermission mineDoor : mineDoorPermissions.values()) {
/*      */ 
/*      */       
/*  710 */       int encodedTile = Server.surfaceMesh.getTile(mineDoor.getTileX(), mineDoor.getTileY());
/*  711 */       byte type = Tiles.decodeType(encodedTile);
/*  712 */       if (!Tiles.isMineDoor(type)) {
/*      */         
/*  714 */         removePermission(mineDoor);
/*      */         continue;
/*      */       } 
/*  717 */       if (mineDoor.isOwner((Creature)player) || mineDoor.isActualOwner(player.getWurmId())) {
/*  718 */         mineDoors.add(mineDoor);
/*      */       }
/*      */     } 
/*  721 */     return mineDoors.<MineDoorPermission>toArray(new MineDoorPermission[mineDoors.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void unManageMineDoorsFor(int villageId) {
/*  731 */     for (MineDoorPermission md : getAllMineDoors()) {
/*      */       
/*  733 */       if (md.getVillageId() == villageId)
/*      */       {
/*      */         
/*  736 */         md.setVillage(null);
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
/*      */   public long getWurmId() {
/*  749 */     return this.wurmId;
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
/*      */   public int getTemplateId() {
/*  761 */     return -10;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTileX() {
/*  766 */     return decodeTileX(this.id);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTileY() {
/*  771 */     return decodeTileY(this.id);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getObjectName() {
/*  782 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTypeName() {
/*  793 */     int encodedTile = Server.surfaceMesh.getTile(getTileX(), getTileY());
/*  794 */     byte type = Tiles.decodeType(encodedTile);
/*  795 */     Tiles.Tile tile = Tiles.getTile(type);
/*  796 */     return tile.getDesc();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setObjectName(String newName, Creature creature) {
/*  807 */     if (this.name.equals(newName))
/*  808 */       return true; 
/*  809 */     this.name = newName;
/*  810 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isActualOwner(long playerId) {
/*  821 */     return (this.creator == playerId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOwner(Creature creature) {
/*  832 */     return isOwner(creature.getWurmId());
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
/*      */   public boolean isOwner(long playerId) {
/*  844 */     if (isManaged()) {
/*      */       
/*  846 */       Village vill = getManagedByVillage();
/*  847 */       if (vill != null) {
/*  848 */         return vill.isMayor(playerId);
/*      */       }
/*      */     } 
/*  851 */     return isActualOwner(playerId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canChangeName(Creature creature) {
/*  862 */     return (creature.getPower() > 1 || isOwner(creature.getWurmId()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canChangeOwner(Creature creature) {
/*  873 */     return (creature.getPower() > 1 || isActualOwner(creature.getWurmId()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean showWarning() {
/*  879 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getWarning() {
/*  890 */     if (showWarning())
/*  891 */       return "NEEDS A LOCK"; 
/*  892 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PermissionsPlayerList getPermissionsPlayerList() {
/*  903 */     return MineDoorSettings.getPermissionsPlayerList(this.id);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isManaged() {
/*  914 */     return this.permissions.hasPermission(Permissions.Allow.SETTLEMENT_MAY_MANAGE.getBit());
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
/*      */   public boolean isManageEnabled(Player player) {
/*  927 */     if (player.getPower() > 1)
/*  928 */       return true; 
/*  929 */     if (isManaged()) {
/*      */       
/*  931 */       Village vil = Villages.getVillage(getTileX(), getTileY(), true);
/*      */       
/*  933 */       if (vil != null)
/*  934 */         return false; 
/*      */     } 
/*  936 */     return isOwner((Creature)player);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsManaged(boolean newIsManaged, Player player) {
/*  942 */     int oldId = this.villageId;
/*  943 */     if (newIsManaged) {
/*      */ 
/*      */       
/*  946 */       if (this.village != null) {
/*      */         
/*  948 */         setVillageId(this.village.getId());
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  953 */         Village cv = getOwnerVillage();
/*  954 */         if (cv != null) {
/*  955 */           setVillageId(cv.getId());
/*      */         } else {
/*  957 */           setVillageId(-1);
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/*  962 */       setVillageId(-1);
/*      */     } 
/*  964 */     if (oldId != this.villageId && MineDoorSettings.exists(this.id)) {
/*      */       
/*  966 */       MineDoorSettings.remove(this.id);
/*  967 */       PermissionsHistories.addHistoryEntry(getWurmId(), System.currentTimeMillis(), -10L, "Auto", "Cleared Permissions");
/*      */     } 
/*      */     
/*  970 */     this.permissions.setPermissionBit(Permissions.Allow.SETTLEMENT_MAY_MANAGE.getBit(), (this.villageId != -1));
/*      */     
/*      */     try {
/*  973 */       save();
/*      */     }
/*  975 */     catch (IOException e) {
/*      */ 
/*      */       
/*  978 */       logger.log(Level.WARNING, e.getMessage(), e);
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
/*      */   public boolean isCitizen(Creature creature) {
/*  990 */     Village vill = getManagedByVillage();
/*  991 */     if (vill == null) {
/*  992 */       vill = getOwnerVillage();
/*      */     }
/*  994 */     if (vill != null)
/*  995 */       return vill.isCitizen(creature); 
/*  996 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAllied(Creature creature) {
/* 1007 */     Village vill = getManagedByVillage();
/* 1008 */     if (vill == null) {
/* 1009 */       vill = getOwnerVillage();
/*      */     }
/* 1011 */     if (vill != null)
/* 1012 */       return vill.isAlly(creature); 
/* 1013 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSameKingdom(Creature creature) {
/* 1024 */     Village vill = getPermissionsVillage();
/* 1025 */     if (vill != null) {
/* 1026 */       return (vill.kingdom == creature.getKingdomId());
/*      */     }
/* 1028 */     return (Players.getInstance().getKingdomForPlayer(this.creator) == creature.getKingdomId());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addGuest(long guestId, int settings) {
/* 1039 */     MineDoorSettings.addPlayer(this.id, guestId, settings);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeGuest(long guestId) {
/* 1050 */     MineDoorSettings.removePlayer(this.id, guestId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isGuest(Creature creature) {
/* 1061 */     return isGuest(creature.getWurmId());
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
/*      */   public boolean isGuest(long playerId) {
/* 1073 */     return MineDoorSettings.isGuest(this, playerId);
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getOwnerId() {
/* 1078 */     return this.creator;
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
/*      */   public String mayManageText(Player aPlayer) {
/* 1090 */     String vName = "";
/* 1091 */     Village vill = getManagedByVillage();
/* 1092 */     if (vill != null) {
/* 1093 */       vName = "Settlement \"" + vill.getName() + "\" may manage";
/*      */     }
/*      */     else {
/*      */       
/* 1097 */       vill = getVillage();
/* 1098 */       if (vill != null) {
/* 1099 */         vName = "Settlement \"" + vill.getName() + "\" may manage";
/*      */       }
/*      */       else {
/*      */         
/* 1103 */         vill = Villages.getVillageForCreature(getOwnerId());
/* 1104 */         if (vill != null)
/* 1105 */           vName = "Settlement \"" + vill.getName() + "\" may manage"; 
/*      */       } 
/*      */     } 
/* 1108 */     return vName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String mayManageHover(Player aPlayer) {
/* 1119 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String messageOnTick() {
/* 1130 */     return "By selecting this you are giving full control to settlement.";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String questionOnTick() {
/* 1141 */     return "Are you positive you want to give your control away?";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String messageUnTick() {
/* 1152 */     return "By doing this you are reverting the control to owner";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String questionUnTick() {
/* 1163 */     return "Are you sure you want them to have control?";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSettlementName() {
/* 1174 */     String sName = "";
/* 1175 */     Village vill = getPermissionsVillage();
/* 1176 */     if (vill != null) {
/* 1177 */       sName = vill.getName();
/*      */     }
/* 1179 */     if (sName.length() == 0)
/* 1180 */       return sName; 
/* 1181 */     return "Citizens of \"" + sName + "\"";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getAllianceName() {
/* 1192 */     String aName = "";
/* 1193 */     Village vill = getPermissionsVillage();
/* 1194 */     if (vill != null)
/* 1195 */       aName = vill.getAllianceName(); 
/* 1196 */     if (aName.length() == 0)
/* 1197 */       return aName; 
/* 1198 */     return "Alliance of \"" + aName + "\"";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getKingdomName() {
/* 1209 */     byte kingdom = 0;
/* 1210 */     Village vill = getPermissionsVillage();
/* 1211 */     if (vill != null) {
/* 1212 */       kingdom = vill.kingdom;
/*      */     } else {
/* 1214 */       kingdom = Players.getInstance().getKingdomForPlayer(this.creator);
/*      */     } 
/* 1216 */     return "Kingdom of \"" + Kingdoms.getNameFor(kingdom) + "\"";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getRolePermissionName() {
/* 1227 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canAllowEveryone() {
/* 1238 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setNewOwner(long playerId) {
/* 1249 */     if (!isManaged() && MineDoorSettings.exists(this.id)) {
/*      */       
/* 1251 */       MineDoorSettings.remove(this.id);
/* 1252 */       PermissionsHistories.addHistoryEntry(getWurmId(), System.currentTimeMillis(), playerId, "Auto", "Cleared Permissions");
/*      */     } 
/*      */ 
/*      */     
/* 1256 */     return setController(playerId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getOwnerName() {
/* 1267 */     return PlayerInfoFactory.getPlayerName(this.creator);
/*      */   }
/*      */ 
/*      */   
/*      */   void addDefaultAllyPermissions() {
/* 1272 */     if (!getPermissionsPlayerList().exists(-20L)) {
/*      */       
/* 1274 */       int value = MineDoorSettings.MinedoorPermissions.PASS.getValue();
/* 1275 */       addGuest(-20L, value);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addDefaultCitizenPermissions() {
/* 1283 */     if (!getPermissionsPlayerList().exists(-30L)) {
/*      */ 
/*      */       
/* 1286 */       int value = MineDoorSettings.MinedoorPermissions.PASS.getValue();
/*      */       
/* 1288 */       addGuest(-30L, value);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void addDefaultKingdomPermission() {
/* 1294 */     if (!getPermissionsPlayerList().exists(-40L)) {
/*      */       
/* 1296 */       int value = MineDoorSettings.MinedoorPermissions.PASS.getValue();
/* 1297 */       addGuest(-40L, value);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean convertToNewPermissions() {
/* 1303 */     boolean didConvert = false;
/* 1304 */     PermissionsPlayerList ppl = StructureSettings.getPermissionsPlayerList(getWurmId());
/* 1305 */     if (this.allowAllies && !ppl.exists(-20L)) {
/*      */       
/* 1307 */       addDefaultAllyPermissions();
/* 1308 */       didConvert = true;
/*      */     } 
/* 1310 */     if (getVillageId() >= 0) {
/*      */       
/* 1312 */       setIsManaged(true, null);
/* 1313 */       didConvert = true;
/*      */     } 
/* 1315 */     if (getVillageId() >= 0 && !ppl.exists(-30L)) {
/*      */       
/* 1317 */       addDefaultCitizenPermissions();
/* 1318 */       didConvert = true;
/*      */     } 
/* 1320 */     if (this.allowAll && !ppl.exists(-40L)) {
/*      */       
/* 1322 */       addDefaultKingdomPermission();
/* 1323 */       didConvert = true;
/*      */     } 
/* 1325 */     if (didConvert)
/*      */       
/*      */       try {
/* 1328 */         save();
/*      */       }
/* 1330 */       catch (IOException e) {
/*      */ 
/*      */         
/* 1333 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       }  
/* 1335 */     return didConvert;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isItem() {
/* 1341 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\MineDoorPermission.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */