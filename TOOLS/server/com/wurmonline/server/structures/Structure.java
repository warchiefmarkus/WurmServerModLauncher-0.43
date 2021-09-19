/*      */ package com.wurmonline.server.structures;
/*      */ 
/*      */ import com.wurmonline.math.TilePos;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.behaviours.Actions;
/*      */ import com.wurmonline.server.behaviours.MethodsStructure;
/*      */ import com.wurmonline.server.creatures.AnimalSettings;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Achievements;
/*      */ import com.wurmonline.server.players.Permissions;
/*      */ import com.wurmonline.server.players.PermissionsHistories;
/*      */ import com.wurmonline.server.players.PermissionsPlayerList;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.tutorial.MissionTargets;
/*      */ import com.wurmonline.server.utils.CoordUtils;
/*      */ import com.wurmonline.server.utils.StringUtil;
/*      */ import com.wurmonline.server.villages.NoSuchVillageException;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.BridgeConstants;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import com.wurmonline.shared.constants.StructureConstants;
/*      */ import com.wurmonline.shared.constants.StructureTypeEnum;
/*      */ import java.io.IOException;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
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
/*      */ public abstract class Structure
/*      */   implements MiscConstants, CounterTypes, TimeConstants, StructureConstants, PermissionsPlayerList.ISettings
/*      */ {
/*   81 */   private static Logger logger = Logger.getLogger(Structure.class.getName());
/*      */ 
/*      */ 
/*      */   
/*      */   private long wurmId;
/*      */ 
/*      */   
/*      */   Set<VolaTile> structureTiles;
/*      */ 
/*      */   
/*   91 */   Set<BuildTile> buildTiles = new HashSet<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   96 */   int minX = 1 << Constants.meshSize;
/*      */ 
/*      */ 
/*      */   
/*  100 */   int maxX = 0;
/*      */ 
/*      */ 
/*      */   
/*  104 */   int minY = 1 << Constants.meshSize;
/*      */ 
/*      */ 
/*      */   
/*  108 */   int maxY = 0;
/*      */ 
/*      */   
/*      */   protected boolean surfaced;
/*      */ 
/*      */   
/*      */   private long creationDate;
/*      */ 
/*      */   
/*      */   private byte roof;
/*      */ 
/*      */   
/*      */   private String name;
/*      */ 
/*      */   
/*      */   private boolean isLoading = false;
/*      */ 
/*      */   
/*      */   private boolean hasLoaded = false;
/*      */   
/*      */   boolean finished = false;
/*      */   
/*      */   Set<Door> doors;
/*      */   
/*  132 */   long writid = -10L;
/*      */   
/*      */   boolean finalfinished = false;
/*      */   boolean allowsVillagers = false;
/*      */   boolean allowsAllies = false;
/*      */   boolean allowsKingdom = false;
/*  138 */   private String planner = "";
/*  139 */   long ownerId = -10L;
/*  140 */   private Permissions permissions = new Permissions();
/*  141 */   int villageId = -1;
/*  142 */   private byte structureType = 0;
/*  143 */   private long lastPolled = System.currentTimeMillis();
/*      */ 
/*      */ 
/*      */   
/*      */   public static final float DAMAGE_STATE_DIVIDER = 60.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Structure(byte theStructureType, String aName, long aId, int aStartX, int aStartY, boolean aSurfaced) {
/*  153 */     this.structureType = theStructureType;
/*  154 */     this.wurmId = aId;
/*  155 */     this.name = aName;
/*  156 */     this.structureTiles = new HashSet<>();
/*      */     
/*  158 */     if (aStartX > this.maxX)
/*  159 */       this.maxX = aStartX; 
/*  160 */     if (aStartX < this.minX)
/*  161 */       this.minX = aStartX; 
/*  162 */     if (aStartY > this.maxY)
/*  163 */       this.maxY = aStartY; 
/*  164 */     if (aStartY < this.minY)
/*  165 */       this.minY = aStartY; 
/*  166 */     this.surfaced = aSurfaced;
/*  167 */     this.creationDate = System.currentTimeMillis();
/*      */     
/*      */     try {
/*  170 */       Zone zone = Zones.getZone(aStartX, aStartY, aSurfaced);
/*  171 */       VolaTile tile = zone.getOrCreateTile(aStartX, aStartY);
/*  172 */       this.structureTiles.add(tile);
/*  173 */       if (theStructureType == 0) {
/*      */         
/*  175 */         tile.addBuildMarker(this);
/*  176 */         clearAllWallsAndMakeWallsForStructureBorder(tile);
/*      */       } else {
/*      */         
/*  179 */         tile.addStructure(this);
/*      */       } 
/*  181 */     } catch (NoSuchZoneException nsz) {
/*      */       
/*  183 */       logger.log(Level.WARNING, "No such zone: " + aStartX + ", " + aStartY + ", StructureId: " + this.wurmId, (Throwable)nsz);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Structure(byte theStructureType, String aName, long aId, boolean aIsSurfaced, byte _roof, boolean _finished, boolean finFinished, long _writid, String aPlanner, long aOwnerId, int aSettings, int aVillageId, boolean allowsCitizens, boolean allowAllies, boolean allowKingdom) {
/*  192 */     this.structureType = theStructureType;
/*  193 */     this.wurmId = aId;
/*  194 */     this.writid = _writid;
/*  195 */     this.name = aName;
/*  196 */     this.structureTiles = new HashSet<>();
/*  197 */     this.surfaced = aIsSurfaced;
/*  198 */     this.roof = _roof;
/*  199 */     this.finished = _finished;
/*  200 */     this.finalfinished = finFinished;
/*  201 */     this.allowsVillagers = allowsCitizens;
/*  202 */     this.allowsAllies = allowAllies;
/*  203 */     this.allowsKingdom = allowKingdom;
/*  204 */     this.planner = aPlanner;
/*  205 */     this.ownerId = aOwnerId;
/*  206 */     setSettings(aSettings);
/*  207 */     this.villageId = aVillageId;
/*  208 */     setMaxAndMin();
/*      */   }
/*      */ 
/*      */   
/*      */   Structure(long id) throws IOException, NoSuchStructureException {
/*  213 */     this.wurmId = id;
/*  214 */     this.structureTiles = new HashSet<>();
/*      */     
/*  216 */     load();
/*  217 */     setMaxAndMin();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void addBuildTile(BuildTile toadd) {
/*  222 */     this.buildTiles.add(toadd);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void clearBuildTiles() {
/*  227 */     this.buildTiles.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getName() {
/*  236 */     return this.name;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setPlanner(String newPlanner) {
/*  241 */     this.planner = newPlanner;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getPlanner() {
/*  246 */     return this.planner;
/*      */   }
/*      */ 
/*      */   
/*      */   final void setSettings(int newSettings) {
/*  251 */     this.permissions.setPermissionBits(newSettings);
/*      */   }
/*      */ 
/*      */   
/*      */   public final Permissions getSettings() {
/*  256 */     return this.permissions;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setName(String aName, boolean saveIt) {
/*  265 */     this.name = aName.substring(0, Math.min(255, aName.length()));
/*  266 */     VolaTile[] vtiles = getStructureTiles();
/*  267 */     for (int x = 0; x < vtiles.length; x++)
/*  268 */       vtiles[x].changeStructureName(aName); 
/*  269 */     if (saveIt) {
/*      */       
/*      */       try {
/*      */         
/*  273 */         saveName();
/*      */       }
/*  275 */       catch (IOException e) {
/*      */ 
/*      */         
/*  278 */         logger.log(Level.WARNING, e.getMessage(), e);
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
/*      */   public final String getTypeName() {
/*  291 */     return "Building";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getObjectName() {
/*  302 */     return this.name;
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
/*      */   
/*      */   public final boolean setObjectName(String newName, Creature creature) {
/*  317 */     if (this.writid != -10L) {
/*      */       
/*      */       try {
/*      */         
/*  321 */         Item writ = Items.getItem(getWritId());
/*  322 */         if (writ.getOwnerId() != creature.getWurmId())
/*      */         {
/*  324 */           return false;
/*      */         }
/*  326 */         writ.setDescription(newName);
/*      */       }
/*  328 */       catch (NoSuchItemException nsi) {
/*      */ 
/*      */ 
/*      */         
/*  332 */         this.writid = -10L;
/*      */         
/*      */         try {
/*  335 */           saveWritId();
/*      */         }
/*  337 */         catch (IOException e) {
/*      */ 
/*      */           
/*  340 */           logger.log(Level.WARNING, e.getMessage(), e);
/*      */         } 
/*      */       } 
/*      */     }
/*  344 */     setName(newName, false);
/*  345 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void setRoof(byte aRoof) {
/*  354 */     this.roof = aRoof;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte getRoof() {
/*  363 */     return this.roof;
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getOwnerId() {
/*  368 */     if (this.writid != -10L) {
/*      */       
/*      */       try {
/*      */         
/*  372 */         Item writ = Items.getItem(this.writid);
/*  373 */         if (this.ownerId != writ.getOwnerId()) {
/*      */           
/*  375 */           this.ownerId = writ.getOwnerId();
/*  376 */           saveOwnerId();
/*      */         } 
/*  378 */         return writ.getOwnerId();
/*      */       }
/*  380 */       catch (NoSuchItemException nsi) {
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
/*  400 */         setWritid(-10L, true);
/*      */       }
/*  402 */       catch (IOException ioe) {
/*      */         
/*  404 */         logger.log(Level.WARNING, ioe.getMessage(), ioe);
/*      */       } 
/*      */     }
/*  407 */     if (this.ownerId == -10L && this.planner.length() > 0) {
/*      */ 
/*      */       
/*  410 */       PlayerInfo pInfo = PlayerInfoFactory.getPlayerInfoWithName(this.planner);
/*  411 */       if (pInfo != null) {
/*      */         
/*  413 */         this.ownerId = pInfo.wurmId;
/*      */         
/*      */         try {
/*  416 */           saveOwnerId();
/*      */         }
/*  418 */         catch (IOException ioe) {
/*      */           
/*  420 */           logger.log(Level.WARNING, ioe.getMessage(), ioe);
/*      */         } 
/*      */       } 
/*      */     } 
/*  424 */     return this.ownerId;
/*      */   }
/*      */ 
/*      */   
/*      */   final void setOwnerId(long newOwnerId) {
/*  429 */     this.ownerId = newOwnerId;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getVillageId() {
/*  434 */     return this.villageId;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setVillageId(int newVillageId) {
/*  439 */     this.villageId = newVillageId;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Village getManagedByVillage() {
/*  444 */     if (this.villageId >= 0) {
/*      */       
/*      */       try {
/*      */         
/*  448 */         return Villages.getVillage(this.villageId);
/*      */       }
/*  450 */       catch (NoSuchVillageException noSuchVillageException) {}
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  456 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getWritId() {
/*  461 */     return this.writid;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEnemy(Creature creature) {
/*  466 */     if (creature.getPower() > 1)
/*  467 */       return false; 
/*  468 */     if (isGuest(creature)) {
/*  469 */       return false;
/*      */     }
/*  471 */     Village vil = getPermissionsVillage();
/*  472 */     if (vil != null) {
/*  473 */       return vil.isEnemy(creature);
/*      */     }
/*      */     
/*  476 */     if (!isSameKingdom(creature)) {
/*  477 */       return true;
/*      */     }
/*  479 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnemyAllowed(Creature creature, short action) {
/*  488 */     Village v = getVillage();
/*  489 */     if (v != null && Actions.actionEntrys[action] != null) {
/*      */       
/*  491 */       if (Actions.actionEntrys[action].isEnemyAllowedWhenNoGuards() && (v.getGuards()).length != 0)
/*  492 */         return false; 
/*  493 */       if (Actions.actionEntrys[action].isEnemyNeverAllowed())
/*  494 */         return false; 
/*  495 */       if (Actions.actionEntrys[action].isEnemyAlwaysAllowed()) {
/*  496 */         return true;
/*      */       }
/*      */     } 
/*  499 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayLockPick(Creature creature) {
/*  506 */     if (Servers.isThisAPvpServer() && isEnemyAllowed(creature, (short)101)) {
/*  507 */       return true;
/*      */     }
/*  509 */     Village v = (getManagedByVillage() == null) ? getVillage() : getManagedByVillage();
/*  510 */     if (v != null)
/*      */     {
/*  512 */       return v.getRoleFor(creature).mayPickLocks();
/*      */     }
/*  514 */     return (mayManage(creature) || Servers.isThisAPvpServer());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCitizen(Creature creature) {
/*  520 */     Village vil = getPermissionsVillage();
/*  521 */     if (vil != null)
/*  522 */       return vil.isCitizen(creature); 
/*  523 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isActionAllowed(Creature performer, short action) {
/*  528 */     if (performer.getPower() > 1) {
/*  529 */       return true;
/*      */     }
/*  531 */     if (isEnemy(performer) && !isEnemyAllowed(performer, action)) {
/*  532 */       return false;
/*      */     }
/*  534 */     if (Actions.isActionAttachLock(action)) {
/*  535 */       return (isEnemy(performer) || mayManage(performer));
/*      */     }
/*  537 */     if (Actions.isActionChangeBuilding(action)) {
/*  538 */       return mayManage(performer);
/*      */     }
/*  540 */     if (Actions.isActionLockPick(action)) {
/*  541 */       return mayLockPick(performer);
/*      */     }
/*  543 */     if (Actions.isActionTake(action) || 
/*  544 */       Actions.isActionPullPushTurn(action) || 671 == action || 672 == action || 
/*      */ 
/*      */       
/*  547 */       Actions.isActionPick(action)) {
/*  548 */       return (isEnemy(performer) || mayPickup(performer));
/*      */     }
/*  550 */     if (Actions.isActionPickupPlanted(action)) {
/*  551 */       return mayPickupPlanted(performer);
/*      */     }
/*  553 */     if (Actions.isActionPlaceMerchants(action)) {
/*  554 */       return mayPlaceMerchants(performer);
/*      */     }
/*  556 */     if (Actions.isActionDestroy(action) || 
/*  557 */       Actions.isActionBuild(action) || 
/*  558 */       Actions.isActionDestroyFence(action) || 
/*  559 */       Actions.isActionPlanBuilding(action) || 
/*  560 */       Actions.isActionPack(action) || 
/*  561 */       Actions.isActionPave(action) || 
/*  562 */       Actions.isActionDestroyItem(action)) {
/*  563 */       return (isEnemy(performer) || mayModify(performer));
/*      */     }
/*  565 */     if (Actions.isActionLoad(action) || Actions.isActionUnload(action)) {
/*  566 */       return mayLoad(performer);
/*      */     }
/*  568 */     if (Actions.isActionDrop(action) && isEnemy(performer)) {
/*  569 */       return true;
/*      */     }
/*  571 */     if (Actions.isActionImproveOrRepair(action) || 
/*  572 */       Actions.isActionDrop(action)) {
/*  573 */       return (isEnemy(performer) || mayPass(performer));
/*      */     }
/*  575 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAllied(Creature creature) {
/*  581 */     Village vil = getPermissionsVillage();
/*  582 */     if (vil != null)
/*  583 */       return vil.isAlly(creature); 
/*  584 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSameKingdom(Creature creature) {
/*  590 */     Village vill = getPermissionsVillage();
/*  591 */     if (vill != null) {
/*  592 */       return (vill.kingdom == creature.getKingdomId());
/*      */     }
/*  594 */     return (Players.getInstance().getKingdomForPlayer(getOwnerId()) == creature.getKingdomId());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Village getVillage() {
/*  601 */     Village village = Villages.getVillage(getMinX(), getMinY(), isSurfaced());
/*  602 */     if (village == null) {
/*  603 */       return null;
/*      */     }
/*  605 */     Village v = Villages.getVillage(getMinX(), getMaxY(), isSurfaced());
/*  606 */     if (v == null || v.getId() != village.getId()) {
/*  607 */       return null;
/*      */     }
/*  609 */     v = Villages.getVillage(getMaxX(), getMaxY(), isSurfaced());
/*  610 */     if (v == null || v.getId() != village.getId()) {
/*  611 */       return null;
/*      */     }
/*  613 */     v = Villages.getVillage(getMinX(), getMinY(), isSurfaced());
/*  614 */     if (v == null || v.getId() != village.getId()) {
/*  615 */       return null;
/*      */     }
/*  617 */     return village;
/*      */   }
/*      */ 
/*      */   
/*      */   private Village getPermissionsVillage() {
/*  622 */     Village vill = getManagedByVillage();
/*  623 */     if (vill != null) {
/*  624 */       return vill;
/*      */     }
/*      */     
/*  627 */     long wid = getOwnerId();
/*  628 */     if (wid != -10L)
/*      */     {
/*  630 */       return Villages.getVillageForCreature(wid);
/*      */     }
/*      */     
/*  633 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private String getVillageName(Player player) {
/*  638 */     String sName = "";
/*  639 */     Village vill = getVillage();
/*  640 */     if (vill != null) {
/*  641 */       sName = vill.getName();
/*      */     } else {
/*  643 */       sName = player.getVillageName();
/*  644 */     }  return sName;
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
/*  655 */     return (creature.getPower() > 1 || isOwner(creature.getWurmId()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canChangeOwner(Creature creature) {
/*  661 */     return ((isActualOwner(creature.getWurmId()) || creature.getPower() > 1) && this.writid == -10L);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getWarning() {
/*  667 */     if (!isFinished()) {
/*  668 */       return "NEEDS TO BE COMPLETE FOR INTERIOR PERMISSIONS TO WORK";
/*      */     }
/*      */     
/*  671 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public PermissionsPlayerList getPermissionsPlayerList() {
/*  677 */     return StructureSettings.getPermissionsPlayerList(getWurmId());
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
/*      */   public Floor[] getFloors() {
/*  690 */     Set<Floor> floors = new HashSet<>();
/*  691 */     for (VolaTile tile : this.structureTiles) {
/*      */       
/*  693 */       Floor[] fArr = tile.getFloors();
/*  694 */       for (int x = 0; x < fArr.length; x++)
/*      */       {
/*  696 */         floors.add(fArr[x]);
/*      */       }
/*      */     } 
/*      */     
/*  700 */     Floor[] toReturn = new Floor[floors.size()];
/*  701 */     return floors.<Floor>toArray(toReturn);
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
/*      */ 
/*      */   
/*      */   public Floor[] getFloorsAtTile(int tilex, int tiley, int offsetHeightStart, int offsetHeightEnd) {
/*  717 */     Set<Floor> floors = new HashSet<>();
/*  718 */     for (VolaTile tile : this.structureTiles) {
/*      */       
/*  720 */       if (tile.getTileX() == tilex && tile.getTileY() == tiley) {
/*      */         
/*  722 */         Floor[] fArr = tile.getFloors(offsetHeightStart, offsetHeightEnd);
/*  723 */         for (int x = 0; x < fArr.length; x++)
/*      */         {
/*  725 */           floors.add(fArr[x]);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  730 */     Floor[] toReturn = new Floor[floors.size()];
/*  731 */     return floors.<Floor>toArray(toReturn);
/*      */   }
/*      */ 
/*      */   
/*      */   public final Wall[] getWalls() {
/*  736 */     Set<Wall> walls = new HashSet<>();
/*  737 */     for (VolaTile tile : this.structureTiles) {
/*      */       
/*  739 */       Wall[] wArr = tile.getWalls();
/*  740 */       for (int x = 0; x < wArr.length; x++)
/*  741 */         walls.add(wArr[x]); 
/*      */     } 
/*  743 */     Wall[] toReturn = new Wall[walls.size()];
/*  744 */     return walls.<Wall>toArray(toReturn);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Wall[] getExteriorWalls() {
/*  752 */     Set<Wall> walls = new HashSet<>();
/*  753 */     for (VolaTile tile : this.structureTiles) {
/*      */       
/*  755 */       Wall[] wArr = tile.getExteriorWalls();
/*  756 */       for (int x = 0; x < wArr.length; x++)
/*  757 */         walls.add(wArr[x]); 
/*      */     } 
/*  759 */     Wall[] toReturn = new Wall[walls.size()];
/*  760 */     return walls.<Wall>toArray(toReturn);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BridgePart[] getBridgeParts() {
/*  770 */     Set<BridgePart> bridgeParts = new HashSet<>();
/*  771 */     for (VolaTile tile : this.structureTiles) {
/*      */       
/*  773 */       BridgePart[] fArr = tile.getBridgeParts();
/*  774 */       for (int x = 0; x < fArr.length; x++)
/*      */       {
/*  776 */         bridgeParts.add(fArr[x]);
/*      */       }
/*      */     } 
/*      */     
/*  780 */     BridgePart[] toReturn = new BridgePart[bridgeParts.size()];
/*  781 */     return bridgeParts.<BridgePart>toArray(toReturn);
/*      */   }
/*      */ 
/*      */   
/*      */   public final VolaTile getTileFor(Wall wall) {
/*  786 */     for (int xx = 1; xx >= -1; xx--) {
/*      */       
/*  788 */       for (int yy = 1; yy >= -1; yy--) {
/*      */ 
/*      */         
/*      */         try {
/*  792 */           Zone zone = Zones.getZone(wall.tilex + xx, wall.tiley + yy, this.surfaced);
/*  793 */           VolaTile tile = zone.getTileOrNull(wall.tilex + xx, wall.tiley + yy);
/*  794 */           if (tile != null) {
/*      */             
/*  796 */             Wall[] walls = tile.getWalls();
/*  797 */             for (int s = 0; s < walls.length; s++)
/*      */             {
/*      */               
/*  800 */               if (walls[s] == wall)
/*      */               {
/*  802 */                 return tile;
/*      */               }
/*      */             }
/*      */           
/*      */           } 
/*  807 */         } catch (NoSuchZoneException noSuchZoneException) {}
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  813 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void poll(long time) {
/*  818 */     if (time - this.lastPolled > 3600000L) {
/*      */       
/*  820 */       this.lastPolled = System.currentTimeMillis();
/*  821 */       if (!isFinalized()) {
/*      */         
/*  823 */         if (time - this.creationDate > 10800000L)
/*      */         {
/*  825 */           logger.log(Level.INFO, "Deleting unfinished structure " + getName());
/*  826 */           totallyDestroy();
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  831 */         boolean destroy = false;
/*  832 */         if (time - this.creationDate > 172800000L) {
/*      */           
/*  834 */           destroy = true;
/*  835 */           if (this.structureType == 0) {
/*      */             
/*  837 */             if (hasWalls()) {
/*  838 */               destroy = false;
/*      */ 
/*      */             
/*      */             }
/*      */           
/*      */           }
/*  844 */           else if ((getBridgeParts()).length != 0) {
/*  845 */             destroy = false;
/*      */           } 
/*      */         } 
/*  848 */         if (destroy)
/*      */         {
/*  850 */           totallyDestroy();
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean hasWalls() {
/*  858 */     for (Iterator<VolaTile> it = this.structureTiles.iterator(); it.hasNext(); ) {
/*      */       
/*  860 */       VolaTile tile = it.next();
/*  861 */       Wall[] wallArr = tile.getWalls();
/*  862 */       for (int x = 0; x < wallArr.length; x++) {
/*      */         
/*  864 */         if (wallArr[x].getType() != StructureTypeEnum.PLAN)
/*      */         {
/*  866 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*  870 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void totallyDestroy() {
/*  876 */     Players.getInstance().setStructureFinished(this.wurmId);
/*  877 */     if (isFinalized()) {
/*      */ 
/*      */       
/*  880 */       if (getWritId() != -10L || this.structureType != 1) {
/*      */         
/*      */         try {
/*      */           
/*  884 */           Item writ = Items.getItem(getWritId());
/*      */ 
/*      */           
/*      */           try {
/*  888 */             Server.getInstance().getCreature(writ.getOwnerId());
/*  889 */             Items.destroyItem(getWritId());
/*      */           }
/*  891 */           catch (NoSuchCreatureException nsc) {
/*      */             
/*  893 */             Items.decay(getWritId(), null);
/*      */           }
/*  895 */           catch (NoSuchPlayerException nsp) {
/*      */             
/*  897 */             Items.decay(getWritId(), null);
/*      */           }
/*      */         
/*  900 */         } catch (NoSuchItemException noSuchItemException) {}
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  907 */       if (this.structureType == 0)
/*      */       {
/*      */ 
/*      */         
/*  911 */         for (VolaTile vt : this.structureTiles) {
/*      */ 
/*      */           
/*  914 */           VolaTile vtNorth = Zones.getTileOrNull(vt.getTileX(), vt.getTileY() - 1, vt.isOnSurface());
/*  915 */           if (vtNorth != null) {
/*      */             
/*  917 */             Structure structNorth = vtNorth.getStructure();
/*  918 */             if (structNorth != null && structNorth.isTypeBridge()) {
/*      */               
/*  920 */               BridgePart[] bps = vtNorth.getBridgeParts();
/*  921 */               if (bps.length == 1 && bps[0].hasHouseSouthExit()) {
/*  922 */                 structNorth.totallyDestroy();
/*      */               }
/*      */             } 
/*      */           } 
/*  926 */           VolaTile vtEast = Zones.getTileOrNull(vt.getTileX() + 1, vt.getTileY(), vt.isOnSurface());
/*  927 */           if (vtEast != null) {
/*      */             
/*  929 */             Structure structEast = vtEast.getStructure();
/*  930 */             if (structEast != null && structEast.isTypeBridge()) {
/*      */               
/*  932 */               BridgePart[] bps = vtEast.getBridgeParts();
/*  933 */               if (bps.length == 1 && bps[0].hasHouseWestExit()) {
/*  934 */                 structEast.totallyDestroy();
/*      */               }
/*      */             } 
/*      */           } 
/*  938 */           VolaTile vtSouth = Zones.getTileOrNull(vt.getTileX(), vt.getTileY() + 1, vt.isOnSurface());
/*  939 */           if (vtSouth != null) {
/*      */             
/*  941 */             Structure structSouth = vtSouth.getStructure();
/*  942 */             if (structSouth != null && structSouth.isTypeBridge()) {
/*      */               
/*  944 */               BridgePart[] bps = vtSouth.getBridgeParts();
/*  945 */               if (bps.length == 1 && bps[0].hasHouseNorthExit()) {
/*  946 */                 structSouth.totallyDestroy();
/*      */               }
/*      */             } 
/*      */           } 
/*  950 */           VolaTile vtWest = Zones.getTileOrNull(vt.getTileX() - 1, vt.getTileY(), vt.isOnSurface());
/*  951 */           if (vtWest != null) {
/*      */             
/*  953 */             Structure structWest = vtWest.getStructure();
/*  954 */             if (structWest != null && structWest.isTypeBridge()) {
/*      */               
/*  956 */               BridgePart[] bps = vtWest.getBridgeParts();
/*  957 */               if (bps.length == 1 && bps[0].hasHouseEastExit())
/*  958 */                 structWest.totallyDestroy(); 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*  963 */       MissionTargets.destroyStructureTargets(getWurmId(), null);
/*      */     } 
/*      */     
/*  966 */     for (Iterator<VolaTile> it = this.structureTiles.iterator(); it.hasNext(); ) {
/*      */       
/*  968 */       VolaTile tile = it.next();
/*      */       
/*  970 */       tile.deleteStructure(getWurmId());
/*      */     } 
/*  972 */     remove();
/*  973 */     delete();
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean hasBridgeEntrance() {
/*  978 */     for (VolaTile vt : this.structureTiles) {
/*      */ 
/*      */       
/*  981 */       if (vt.isOnSurface()) {
/*      */ 
/*      */         
/*  984 */         VolaTile vtNorth = Zones.getTileOrNull(vt.getTileX(), vt.getTileY() - 1, vt.isOnSurface());
/*  985 */         if (vtNorth != null) {
/*      */           
/*  987 */           Structure structNorth = vtNorth.getStructure();
/*  988 */           if (structNorth != null && structNorth.isTypeBridge()) {
/*      */             
/*  990 */             BridgePart[] bps = vtNorth.getBridgeParts();
/*  991 */             if (bps.length == 1 && bps[0].hasHouseSouthExit()) {
/*  992 */               return true;
/*      */             }
/*      */           } 
/*      */         } 
/*  996 */         VolaTile vtEast = Zones.getTileOrNull(vt.getTileX() + 1, vt.getTileY(), vt.isOnSurface());
/*  997 */         if (vtEast != null) {
/*      */           
/*  999 */           Structure structEast = vtEast.getStructure();
/* 1000 */           if (structEast != null && structEast.isTypeBridge()) {
/*      */             
/* 1002 */             BridgePart[] bps = vtEast.getBridgeParts();
/* 1003 */             if (bps.length == 1 && bps[0].hasHouseWestExit()) {
/* 1004 */               return true;
/*      */             }
/*      */           } 
/*      */         } 
/* 1008 */         VolaTile vtSouth = Zones.getTileOrNull(vt.getTileX(), vt.getTileY() + 1, vt.isOnSurface());
/* 1009 */         if (vtSouth != null) {
/*      */           
/* 1011 */           Structure structSouth = vtSouth.getStructure();
/* 1012 */           if (structSouth != null && structSouth.isTypeBridge()) {
/*      */             
/* 1014 */             BridgePart[] bps = vtSouth.getBridgeParts();
/* 1015 */             if (bps.length == 1 && bps[0].hasHouseNorthExit()) {
/* 1016 */               return true;
/*      */             }
/*      */           } 
/*      */         } 
/* 1020 */         VolaTile vtWest = Zones.getTileOrNull(vt.getTileX() - 1, vt.getTileY(), vt.isOnSurface());
/* 1021 */         if (vtWest != null) {
/*      */           
/* 1023 */           Structure structWest = vtWest.getStructure();
/* 1024 */           if (structWest != null && structWest.isTypeBridge()) {
/*      */             
/* 1026 */             BridgePart[] bps = vtWest.getBridgeParts();
/* 1027 */             if (bps.length == 1 && bps[0].hasHouseEastExit())
/* 1028 */               return true; 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1033 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void remove() {
/* 1038 */     if (this.structureTiles.size() > 0) {
/*      */       
/* 1040 */       Zone[] zones = Zones.getZonesCoveredBy(this.minX, this.minY, this.maxX, this.maxY, this.surfaced);
/* 1041 */       for (int x = 0; x < zones.length; x++)
/* 1042 */         zones[x].removeStructure(this); 
/*      */     } 
/* 1044 */     Structures.removeStructure(this.wurmId);
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
/* 1056 */     return true;
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
/* 1068 */     return mayManage(creature);
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
/* 1079 */     if (StructureSettings.isExcluded(this, creature)) {
/* 1080 */       return false;
/*      */     }
/* 1082 */     if (StructureSettings.canManage(this, creature))
/* 1083 */       return true; 
/* 1084 */     if (creature.getCitizenVillage() == null) {
/* 1085 */       return false;
/*      */     }
/* 1087 */     Village vill = getManagedByVillage();
/* 1088 */     if (vill == null)
/* 1089 */       return false; 
/* 1090 */     if (!vill.isCitizen(creature))
/* 1091 */       return false; 
/* 1092 */     return vill.isActionAllowed((short)664, creature);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayManage(Creature creature) {
/* 1102 */     if (creature.getPower() > 1)
/* 1103 */       return true; 
/* 1104 */     return canManage(creature);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean maySeeHistory(Creature creature) {
/* 1114 */     if (creature.getPower() > 1)
/* 1115 */       return true; 
/* 1116 */     return isOwner(creature);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayModify(Creature creature) {
/* 1126 */     if (StructureSettings.isExcluded(this, creature))
/* 1127 */       return false; 
/* 1128 */     return StructureSettings.mayModify(this, creature);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isExcluded(Creature creature) {
/* 1138 */     return StructureSettings.isExcluded(this, creature);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayPass(Creature creature) {
/* 1148 */     if (StructureSettings.isExcluded(this, creature))
/*      */     {
/* 1150 */       return false;
/*      */     }
/* 1152 */     return StructureSettings.mayPass(this, creature);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayPickup(Creature creature) {
/* 1162 */     if (isEnemy(creature))
/* 1163 */       return true; 
/* 1164 */     if (StructureSettings.isExcluded(this, creature))
/* 1165 */       return false; 
/* 1166 */     return StructureSettings.mayPickup(this, creature);
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
/* 1177 */     return isGuest(creature.getWurmId());
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
/* 1189 */     return StructureSettings.isGuest(this, playerId);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void addGuest(long guestId, int aSettings) {
/* 1195 */     StructureSettings.addPlayer(getWurmId(), guestId, aSettings);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void removeGuest(long guestId) {
/* 1201 */     StructureSettings.removePlayer(getWurmId(), guestId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getCreationDate() {
/* 1210 */     return this.creationDate;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getSize() {
/* 1215 */     return this.structureTiles.size();
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getLimit() {
/* 1220 */     return this.structureTiles.size() + (getExteriorWalls()).length;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getLimitFor(int tilex, int tiley, boolean onSurface, boolean adding) {
/* 1225 */     VolaTile newTile = Zones.getOrCreateTile(tilex, tiley, onSurface);
/*      */     
/* 1227 */     int points = getLimit();
/* 1228 */     if (contains(tilex, tiley) && adding)
/*      */     {
/*      */       
/* 1231 */       return points;
/*      */     }
/* 1233 */     int newTilePoints = 5;
/*      */     
/* 1235 */     if (adding) {
/*      */ 
/*      */       
/* 1238 */       Set<VolaTile> neighbors = createNeighbourStructureTiles(this, newTile);
/* 1239 */       newTilePoints -= neighbors.size();
/* 1240 */       points -= neighbors.size();
/* 1241 */       return points + newTilePoints;
/*      */     } 
/*      */     
/* 1244 */     if (contains(tilex, tiley)) {
/*      */       
/* 1246 */       Set<VolaTile> neighbors = createNeighbourStructureTiles(this, newTile);
/*      */       
/* 1248 */       newTilePoints -= neighbors.size();
/* 1249 */       points += neighbors.size();
/* 1250 */       return points - newTilePoints;
/*      */     } 
/*      */     
/* 1253 */     return points;
/*      */   }
/*      */ 
/*      */   
/*      */   private void setMaxAndMin() {
/* 1258 */     this.maxX = 0;
/* 1259 */     this.minX = 1 << Constants.meshSize;
/* 1260 */     this.maxY = 0;
/* 1261 */     this.minY = 1 << Constants.meshSize;
/* 1262 */     if (this.structureTiles != null)
/*      */     {
/* 1264 */       for (Iterator<VolaTile> it = this.structureTiles.iterator(); it.hasNext(); ) {
/*      */         
/* 1266 */         VolaTile tile = it.next();
/* 1267 */         int xx = tile.getTileX();
/* 1268 */         int yy = tile.getTileY();
/* 1269 */         if (xx > this.maxX)
/* 1270 */           this.maxX = xx; 
/* 1271 */         if (xx < this.minX)
/* 1272 */           this.minX = xx; 
/* 1273 */         if (yy > this.maxY)
/* 1274 */           this.maxY = yy; 
/* 1275 */         if (yy < this.minY) {
/* 1276 */           this.minY = yy;
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   static final StructureBounds getStructureBounds(List<Wall> structureWalls) {
/* 1283 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   final StructureBounds secureOuterWalls(List<Wall> structureWalls) {
/* 1288 */     TilePoint max = new TilePoint(0, 0);
/* 1289 */     TilePoint min = new TilePoint(Zones.worldTileSizeX, Zones.worldTileSizeY);
/*      */     
/* 1291 */     StructureBounds structBounds = new StructureBounds(max, min);
/*      */ 
/*      */ 
/*      */     
/* 1295 */     for (Wall wall : structureWalls) {
/*      */       
/* 1297 */       if (wall.getStartX() > structBounds.max.getTileX())
/* 1298 */         structBounds.getMax().setTileX(wall.getStartX()); 
/* 1299 */       if (wall.getStartY() > structBounds.max.getTileY()) {
/* 1300 */         structBounds.getMax().setTileY(wall.getStartY());
/*      */       }
/* 1302 */       if (wall.getStartX() < structBounds.min.getTileX())
/* 1303 */         structBounds.getMin().setTileX(wall.getStartX()); 
/* 1304 */       if (wall.getStartY() < structBounds.min.getTileY()) {
/* 1305 */         structBounds.getMin().setTileY(wall.getStartY());
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1316 */     return structBounds;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fixWalls(VolaTile tile) {
/* 1327 */     for (BuildTile bt : this.buildTiles) {
/*      */       
/* 1329 */       if (bt.getTileX() == tile.getTileX() && bt.getTileY() == tile.getTileY() && tile.isOnSurface() == ((bt.getLayer() == 0))) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */     
/* 1334 */     for (Wall wall : tile.getWalls()) {
/*      */       
/* 1336 */       int x = tile.getTileX();
/* 1337 */       int y = tile.getTileY();
/* 1338 */       int newTileX = 0;
/* 1339 */       int newTileY = 0;
/* 1340 */       boolean found = false;
/* 1341 */       Structure s = null;
/*      */       
/* 1343 */       if (wall.isHorizontal()) {
/*      */ 
/*      */         
/* 1346 */         s = Structures.getStructureForTile(x, y - 1, tile.isOnSurface());
/* 1347 */         if (s != null && s.isTypeHouse()) {
/*      */ 
/*      */           
/* 1350 */           newTileX = x;
/* 1351 */           newTileY = y - 1;
/* 1352 */           found = true;
/*      */         } 
/*      */ 
/*      */         
/* 1356 */         s = Structures.getStructureForTile(x, y + 1, tile.isOnSurface());
/* 1357 */         if (s != null && s.isTypeHouse())
/*      */         {
/*      */           
/* 1360 */           newTileX = x;
/* 1361 */           newTileY = y + 1;
/* 1362 */           found = true;
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1368 */         s = Structures.getStructureForTile(x - 1, y, tile.isOnSurface());
/* 1369 */         if (s != null && s.isTypeHouse()) {
/*      */           
/* 1371 */           newTileX = x - 1;
/* 1372 */           newTileY = y;
/* 1373 */           found = true;
/*      */         } 
/*      */ 
/*      */         
/* 1377 */         s = Structures.getStructureForTile(x + 1, y, tile.isOnSurface());
/* 1378 */         if (s != null && s.isTypeHouse()) {
/*      */           
/* 1380 */           newTileX = x + 1;
/* 1381 */           newTileY = y;
/* 1382 */           found = true;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1387 */       if (!found) {
/*      */ 
/*      */         
/* 1390 */         logger.log(Level.WARNING, StringUtil.format("Wall with WALL.ID = %d is orphan, but belongs to structure %d. Does the structure exist?", new Object[] {
/*      */                 
/* 1392 */                 Integer.valueOf(wall.getNumber()), Long.valueOf(wall.getStructureId())
/*      */               }));
/*      */         
/*      */         return;
/*      */       } 
/* 1397 */       VolaTile t = Zones.getTileOrNull(newTileX, newTileY, tile.isOnSurface());
/*      */ 
/*      */       
/* 1400 */       tile.removeWall(wall, true);
/* 1401 */       wall.setTile(newTileX, newTileY);
/* 1402 */       t.addWall(wall);
/* 1403 */       logger.log(Level.WARNING, StringUtil.format("fixWalls found a wall %d at %d,%d and moved it to %d,%d for structure %d", new Object[] {
/*      */               
/* 1405 */               Integer.valueOf(wall.getNumber()), Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(newTileX), Integer.valueOf(newTileY), Long.valueOf(wall.getStructureId())
/*      */             }));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean loadStructureTiles(List<Wall> structureWalls) {
/* 1415 */     boolean toReturn = true;
/* 1416 */     if (!this.buildTiles.isEmpty()) {
/*      */       
/* 1418 */       toReturn = false;
/*      */       
/* 1420 */       for (BuildTile buildTile : this.buildTiles) {
/*      */ 
/*      */         
/*      */         try {
/* 1424 */           Zone zone = Zones.getZone(buildTile.getTileX(), buildTile.getTileY(), (buildTile.getLayer() == 0));
/*      */           
/* 1426 */           VolaTile tile = zone.getOrCreateTile(buildTile.getTileX(), buildTile.getTileY());
/*      */           
/* 1428 */           addBuildTile(tile, true);
/*      */         }
/* 1430 */         catch (NoSuchZoneException nsz) {
/*      */           
/* 1432 */           logger.log(Level.WARNING, "Structure with id " + this.wurmId + " is built on the edge of the world at " + buildTile
/* 1433 */               .getTileX() + ", " + buildTile.getTileY(), (Throwable)nsz);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1438 */     int tilex = 0;
/* 1439 */     int tiley = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1445 */     for (Iterator<Wall> it = structureWalls.iterator(); it.hasNext(); ) {
/*      */       
/* 1447 */       Wall wall = it.next();
/*      */       
/*      */       try {
/* 1450 */         tilex = wall.getTileX();
/* 1451 */         tiley = wall.getTileY();
/* 1452 */         Zone zone = Zones.getZone(tilex, tiley, isSurfaced());
/* 1453 */         VolaTile tile = zone.getOrCreateTile(tilex, tiley);
/*      */         
/* 1455 */         tile.addWall(wall);
/* 1456 */         if (!this.structureTiles.contains(tile))
/*      */         {
/* 1458 */           logger.log(Level.WARNING, "Wall with  WURMZONES.WALLS.ID =" + wall.getId() + " exists outside a structure! ");
/* 1459 */           fixWalls(tile);
/*      */         }
/*      */       
/*      */       }
/* 1463 */       catch (NoSuchZoneException nsz) {
/*      */         
/* 1465 */         logger.log(Level.WARNING, "Failed to locate zone at " + tilex + ", " + tiley);
/*      */       } 
/*      */       
/* 1468 */       if (wall.getType() == StructureTypeEnum.DOOR || wall.getType() == StructureTypeEnum.DOUBLE_DOOR || wall
/* 1469 */         .getType() == StructureTypeEnum.PORTCULLIS || wall.getType() == StructureTypeEnum.CANOPY_DOOR) {
/*      */         
/* 1471 */         if (this.doors == null)
/* 1472 */           this.doors = new HashSet<>(); 
/* 1473 */         Door door = new DbDoor(wall);
/* 1474 */         addDoor(door);
/* 1475 */         door.addToTiles();
/*      */ 
/*      */         
/*      */         try {
/* 1479 */           door.load();
/*      */         }
/* 1481 */         catch (IOException e) {
/*      */           
/* 1483 */           logger.log(Level.WARNING, "Failed to load a door: " + e.getMessage(), e);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1488 */     this.buildTiles.clear();
/* 1489 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean fillHoles() {
/* 1495 */     int numTiles = this.structureTiles.size() + 3;
/*      */     
/* 1497 */     Set<VolaTile> tilesToAdd = new HashSet<>();
/* 1498 */     Set<VolaTile> tilesChecked = new HashSet<>();
/* 1499 */     Set<VolaTile> tilesRemaining = new HashSet<>();
/* 1500 */     tilesRemaining.addAll(this.structureTiles);
/*      */     
/* 1502 */     int iterations = 0;
/* 1503 */     while (iterations++ < numTiles) {
/*      */       
/* 1505 */       for (VolaTile tile : tilesRemaining) {
/*      */         
/* 1507 */         tilesChecked.add(tile);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1514 */         Wall[] walls = tile.getWalls();
/* 1515 */         boolean checkNorth = true;
/* 1516 */         boolean checkEast = true;
/* 1517 */         boolean checkSouth = true;
/* 1518 */         boolean checkWest = true;
/*      */         
/* 1520 */         for (int x = 0; x < walls.length; x++) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1530 */           if (!walls[x].isIndoor()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1536 */             if (walls[x].getHeight() > 0)
/*      */             {
/* 1538 */               logger.log(Level.INFO, "Wall at " + tile.getTileX() + "," + tile.getTileY() + " not indoor at height " + walls[x]
/*      */                   
/* 1540 */                   .getHeight());
/*      */             }
/*      */             
/* 1543 */             if (walls[x].isHorizontal()) {
/*      */               
/* 1545 */               if (walls[x].getStartY() == tile.getTileY()) {
/* 1546 */                 checkNorth = false;
/*      */               } else {
/* 1548 */                 checkSouth = false;
/*      */               }
/*      */             
/*      */             }
/* 1552 */             else if (walls[x].getStartX() == tile.getTileX()) {
/* 1553 */               checkWest = false;
/*      */             } else {
/* 1555 */               checkEast = false;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 1561 */         if (checkNorth) {
/*      */           
/*      */           try {
/*      */             
/* 1565 */             VolaTile t = Zones.getZone(tile.tilex, tile.tiley - 1, this.surfaced).getOrCreateTile(tile.tilex, tile.tiley - 1);
/*      */             
/* 1567 */             if (!this.structureTiles.contains(t) && 
/* 1568 */               !tilesToAdd.contains(t))
/*      */             {
/* 1570 */               tilesToAdd.add(t);
/*      */             }
/*      */           }
/* 1573 */           catch (NoSuchZoneException nsz) {
/*      */             
/* 1575 */             logger.log(Level.WARNING, "CN Structure with id " + this.wurmId + " is built on the edge of the world at " + tile
/*      */                 
/* 1577 */                 .getTileX() + ", " + tile.getTileY());
/*      */           } 
/*      */         }
/* 1580 */         if (checkEast) {
/*      */           
/*      */           try {
/*      */             
/* 1584 */             VolaTile t = Zones.getZone(tile.tilex + 1, tile.tiley, this.surfaced).getOrCreateTile(tile.tilex + 1, tile.tiley);
/*      */ 
/*      */             
/* 1587 */             if (!this.structureTiles.contains(t) && 
/* 1588 */               !tilesToAdd.contains(t))
/*      */             {
/* 1590 */               tilesToAdd.add(t);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           }
/* 1602 */           catch (NoSuchZoneException nsz) {
/*      */             
/* 1604 */             logger.log(Level.WARNING, "CE Structure with id " + this.wurmId + " is built on the edge of the world at " + tile
/*      */                 
/* 1606 */                 .getTileX() + ", " + tile.getTileY());
/*      */           } 
/*      */         }
/* 1609 */         if (checkWest) {
/*      */           
/*      */           try {
/*      */             
/* 1613 */             VolaTile t = Zones.getZone(tile.tilex - 1, tile.tiley, this.surfaced).getOrCreateTile(tile.tilex - 1, tile.tiley);
/*      */ 
/*      */             
/* 1616 */             if (!this.structureTiles.contains(t) && 
/* 1617 */               !tilesToAdd.contains(t))
/*      */             {
/* 1619 */               tilesToAdd.add(t);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           }
/* 1632 */           catch (NoSuchZoneException nsz) {
/*      */             
/* 1634 */             logger.log(Level.WARNING, "CW Structure with id " + this.wurmId + " is built on the edge of the world at " + tile
/*      */                 
/* 1636 */                 .getTileX() + ", " + tile.getTileY());
/*      */           } 
/*      */         }
/* 1639 */         if (checkSouth) {
/*      */           
/*      */           try {
/*      */             
/* 1643 */             VolaTile t = Zones.getZone(tile.tilex, tile.tiley + 1, this.surfaced).getOrCreateTile(tile.tilex, tile.tiley + 1);
/*      */             
/* 1645 */             if (!this.structureTiles.contains(t) && 
/* 1646 */               !tilesToAdd.contains(t))
/*      */             {
/* 1648 */               tilesToAdd.add(t);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           }
/* 1660 */           catch (NoSuchZoneException nsz) {
/*      */             
/* 1662 */             logger.log(Level.WARNING, "CS Structure with id " + this.wurmId + " is built on the edge of the world at " + tile
/*      */                 
/* 1664 */                 .getTileX() + ", " + tile.getTileY());
/*      */           } 
/*      */         }
/*      */       } 
/* 1668 */       tilesRemaining.removeAll(tilesChecked);
/* 1669 */       if (tilesToAdd.size() > 0) {
/*      */         
/* 1671 */         for (VolaTile tile : tilesToAdd) {
/*      */ 
/*      */           
/*      */           try {
/* 1675 */             if (tile.getTileX() > this.maxX)
/* 1676 */               this.maxX = tile.getTileX(); 
/* 1677 */             if (tile.getTileX() < this.minX)
/* 1678 */               this.minX = tile.getTileX(); 
/* 1679 */             if (tile.getTileY() > this.maxY)
/* 1680 */               this.maxY = tile.getTileY(); 
/* 1681 */             if (tile.getTileY() < this.minY) {
/* 1682 */               this.minY = tile.getTileY();
/*      */             }
/* 1684 */             Zone zone = Zones.getZone(tile.getTileX(), tile.getTileY(), isSurfaced());
/* 1685 */             zone.addStructure(this);
/* 1686 */             this.structureTiles.add(tile);
/* 1687 */             addNewBuildTile(tile.getTileX(), tile.getTileY(), tile.getLayer());
/*      */             
/* 1689 */             tile.setStructureAtLoad(this);
/*      */           }
/* 1691 */           catch (NoSuchZoneException nsz) {
/*      */             
/* 1693 */             logger.log(Level.WARNING, "Structure with id " + this.wurmId + " is built on the edge of the world at " + tile
/* 1694 */                 .getTileX() + ", " + tile.getTileY(), (Throwable)nsz);
/*      */           } 
/*      */         } 
/* 1697 */         tilesRemaining.addAll(tilesToAdd);
/* 1698 */         tilesToAdd.clear();
/*      */         continue;
/*      */       } 
/* 1701 */       return false;
/*      */     } 
/* 1703 */     logger.log(Level.WARNING, "Iterations went over " + numTiles + " for " + getName() + " at " + getCenterX() + ", " + 
/* 1704 */         getCenterY());
/* 1705 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean isEqual(Structure struct1, Structure struct2) {
/* 1711 */     if (struct1 == null)
/* 1712 */       return false; 
/* 1713 */     if (struct2 == null) {
/* 1714 */       return false;
/*      */     }
/* 1716 */     return (struct1.getWurmId() == struct2.getWurmId());
/*      */   }
/*      */ 
/*      */   
/*      */   static final Set<VolaTile> createNeighbourStructureTiles(Structure struct, VolaTile modifiedTile) {
/* 1721 */     Set<VolaTile> toReturn = new HashSet<>();
/* 1722 */     VolaTile t = Zones.getTileOrNull(modifiedTile.getTileX() + 1, modifiedTile.getTileY(), modifiedTile.isOnSurface());
/* 1723 */     if (t != null && isEqual(t.getStructure(), struct))
/* 1724 */       toReturn.add(t); 
/* 1725 */     t = Zones.getTileOrNull(modifiedTile.getTileX(), modifiedTile.getTileY() + 1, modifiedTile.isOnSurface());
/* 1726 */     if (t != null && isEqual(t.getStructure(), struct))
/* 1727 */       toReturn.add(t); 
/* 1728 */     t = Zones.getTileOrNull(modifiedTile.getTileX(), modifiedTile.getTileY() - 1, modifiedTile.isOnSurface());
/* 1729 */     if (t != null && isEqual(t.getStructure(), struct))
/* 1730 */       toReturn.add(t); 
/* 1731 */     t = Zones.getTileOrNull(modifiedTile.getTileX() - 1, modifiedTile.getTileY(), modifiedTile.isOnSurface());
/* 1732 */     if (t != null && isEqual(t.getStructure(), struct))
/* 1733 */       toReturn.add(t); 
/* 1734 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void adjustWallsAroundAddedStructureTile(Structure structure, int tilex, int tiley) {
/* 1739 */     VolaTile newTile = Zones.getOrCreateTile(tilex, tiley, structure.isOnSurface());
/* 1740 */     Set<VolaTile> neighbourTiles = createNeighbourStructureTiles(structure, newTile);
/* 1741 */     structure.adjustSurroundingWallsAddedStructureTile(tilex, tiley, neighbourTiles);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void adjustWallsAroundRemovedStructureTile(Structure structure, int tilex, int tiley) {
/* 1746 */     VolaTile newTile = Zones.getOrCreateTile(tilex, tiley, structure.isOnSurface());
/* 1747 */     Set<VolaTile> neighbourTiles = createNeighbourStructureTiles(structure, newTile);
/* 1748 */     structure.adjustSurroundingWallsRemovedStructureTile(tilex, tiley, neighbourTiles);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateWallIsInner(Structure localStructure, VolaTile volaTile, Wall wall, boolean isInner) {
/* 1756 */     if (localStructure.getWurmId() != getWurmId()) {
/*      */       
/* 1758 */       logger.log(Level.WARNING, "Warning structures too close to eachother: " + localStructure.getWurmId() + " and " + 
/* 1759 */           getWurmId() + " at " + volaTile.getTileX() + "," + volaTile.getTileY());
/*      */       return;
/*      */     } 
/* 1762 */     if (wall.getHeight() > 0) {
/* 1763 */       wall.setIndoor(true);
/*      */     } else {
/* 1765 */       wall.setIndoor(isInner);
/* 1766 */     }  volaTile.updateWall(wall);
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
/*      */   public void adjustSurroundingWallsAddedStructureTile(int tilex, int tiley, Set<VolaTile> neighbourTiles) {
/* 1779 */     VolaTile newTile = Zones.getOrCreateTile(tilex, tiley, isOnSurface());
/*      */     
/* 1781 */     for (VolaTile neighbourTile : neighbourTiles) {
/*      */ 
/*      */       
/* 1784 */       Structure localStructure = neighbourTile.getStructure();
/*      */       
/* 1786 */       Wall[] walls = neighbourTile.getWalls();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1791 */       for (Wall wall : walls) {
/*      */ 
/*      */         
/* 1794 */         if (wall.isHorizontal() && wall
/* 1795 */           .getStartY() == tiley && wall.getEndY() == tiley && wall
/* 1796 */           .getStartX() == tilex && wall.getEndX() == tilex + 1)
/*      */         {
/* 1798 */           if (isFree(tilex, tiley - 1)) {
/*      */             
/* 1800 */             updateWallIsInner(localStructure, newTile, wall, false);
/*      */           }
/*      */           else {
/*      */             
/* 1804 */             updateWallIsInner(localStructure, newTile, wall, true);
/*      */           } 
/*      */         }
/*      */ 
/*      */         
/* 1809 */         if (wall.isHorizontal() && wall
/* 1810 */           .getStartY() == tiley + 1 && wall.getEndY() == tiley + 1 && wall
/* 1811 */           .getStartX() == tilex && wall.getEndX() == tilex + 1)
/*      */         {
/* 1813 */           if (isFree(tilex, tiley + 1)) {
/* 1814 */             updateWallIsInner(localStructure, newTile, wall, false);
/*      */           } else {
/* 1816 */             updateWallIsInner(localStructure, newTile, wall, true);
/*      */           } 
/*      */         }
/*      */         
/* 1820 */         if (!wall.isHorizontal() && wall
/* 1821 */           .getStartX() == tilex + 1 && wall.getEndX() == tilex + 1 && wall
/* 1822 */           .getStartY() == tiley && wall.getEndY() == tiley + 1)
/*      */         {
/* 1824 */           if (isFree(tilex + 1, tiley)) {
/* 1825 */             updateWallIsInner(localStructure, newTile, wall, false);
/*      */           } else {
/* 1827 */             updateWallIsInner(localStructure, newTile, wall, true);
/*      */           } 
/*      */         }
/*      */         
/* 1831 */         if (!wall.isHorizontal() && wall
/* 1832 */           .getStartX() == tilex && wall.getEndX() == tilex && wall
/* 1833 */           .getStartY() == tiley && wall.getEndY() == tiley + 1)
/*      */         {
/* 1835 */           if (isFree(tilex - 1, tiley)) {
/* 1836 */             updateWallIsInner(localStructure, newTile, wall, false);
/*      */           } else {
/* 1838 */             updateWallIsInner(localStructure, newTile, wall, true);
/*      */           } 
/*      */         }
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
/*      */   public void adjustSurroundingWallsRemovedStructureTile(int tilex, int tiley, Set<VolaTile> neighbourTiles) {
/* 1853 */     VolaTile removedTile = Zones.getOrCreateTile(tilex, tiley, isOnSurface());
/*      */     
/* 1855 */     for (VolaTile neighbourTile : neighbourTiles) {
/*      */ 
/*      */       
/* 1858 */       Structure localStructure = neighbourTile.getStructure();
/*      */       
/* 1860 */       Wall[] walls = neighbourTile.getWalls();
/*      */       
/* 1862 */       for (Wall wall : walls) {
/*      */ 
/*      */         
/* 1865 */         if (wall.isHorizontal() && wall
/* 1866 */           .getStartX() == tilex && wall.getEndX() == tilex + 1 && wall
/* 1867 */           .getStartY() == tiley && wall.getEndY() == tiley)
/*      */         {
/* 1869 */           if (isFree(tilex, tiley - 1)) {
/*      */ 
/*      */ 
/*      */             
/* 1873 */             logger.log(Level.WARNING, "Wall exist.");
/*      */           }
/*      */           else {
/*      */             
/* 1877 */             updateWallIsInner(localStructure, removedTile, wall, false);
/*      */           } 
/*      */         }
/*      */ 
/*      */         
/* 1882 */         if (wall.isHorizontal() && wall
/* 1883 */           .getStartX() == tilex && wall.getEndX() == tilex + 1 && wall
/* 1884 */           .getStartY() == tiley + 1 && wall.getEndY() == tiley + 1)
/*      */         {
/* 1886 */           if (isFree(tilex, tiley + 1)) {
/*      */             
/* 1888 */             logger.log(Level.WARNING, "Wall exist.");
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 1893 */             updateWallIsInner(localStructure, removedTile, wall, false);
/*      */           } 
/*      */         }
/*      */ 
/*      */         
/* 1898 */         if (!wall.isHorizontal() && wall
/* 1899 */           .getStartX() == tilex + 1 && wall.getEndX() == tilex + 1 && wall
/* 1900 */           .getStartY() == tiley && wall.getEndY() == tiley + 1)
/*      */         {
/* 1902 */           if (isFree(tilex + 1, tiley)) {
/*      */             
/* 1904 */             logger.log(Level.WARNING, "Walls exist.");
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 1909 */             updateWallIsInner(localStructure, removedTile, wall, false);
/*      */           } 
/*      */         }
/*      */ 
/*      */         
/* 1914 */         if (!wall.isHorizontal() && wall
/* 1915 */           .getStartX() == tilex && wall.getEndX() == tilex && wall
/* 1916 */           .getStartY() == tiley && wall.getEndY() == tiley + 1)
/*      */         {
/* 1918 */           if (isFree(tilex - 1, tiley)) {
/*      */             
/* 1920 */             logger.log(Level.WARNING, "Walls exist.");
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 1925 */             updateWallIsInner(localStructure, removedTile, wall, false);
/*      */           } 
/*      */         }
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
/*      */   
/*      */   public void addMissingWallPlans(VolaTile tile) {
/* 1941 */     boolean lacksNorth = true;
/* 1942 */     boolean lacksSouth = true;
/* 1943 */     boolean lacksWest = true;
/* 1944 */     boolean lacksEast = true;
/* 1945 */     for (Wall w : tile.getWallsForLevel(0)) {
/*      */       
/* 1947 */       if (w.isHorizontal()) {
/*      */ 
/*      */         
/* 1950 */         if (w.getStartY() == tile.tiley) {
/* 1951 */           lacksNorth = false;
/*      */         }
/* 1953 */         if (w.getStartY() == tile.tiley + 1) {
/* 1954 */           lacksSouth = false;
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/* 1959 */         if (w.getStartX() == tile.tilex) {
/* 1960 */           lacksWest = false;
/*      */         }
/* 1962 */         if (w.getStartX() == tile.tilex + 1)
/* 1963 */           lacksEast = false; 
/*      */       } 
/*      */     } 
/* 1966 */     if (lacksWest && isFree(tile.tilex - 1, tile.tiley))
/*      */     {
/*      */       
/* 1969 */       tile.addWall(StructureTypeEnum.PLAN, tile.tilex, tile.tiley, tile.tilex, tile.tiley + 1, 10.0F, this.wurmId, false);
/*      */     }
/*      */     
/* 1972 */     if (lacksEast && isFree(tile.tilex + 1, tile.tiley))
/*      */     {
/*      */       
/* 1975 */       tile.addWall(StructureTypeEnum.PLAN, tile.tilex + 1, tile.tiley, tile.tilex + 1, tile.tiley + 1, 10.0F, this.wurmId, false);
/*      */     }
/*      */     
/* 1978 */     if (lacksNorth && isFree(tile.tilex, tile.tiley - 1))
/*      */     {
/*      */       
/* 1981 */       tile.addWall(StructureTypeEnum.PLAN, tile.tilex, tile.tiley, tile.tilex + 1, tile.tiley, 10.0F, this.wurmId, false);
/*      */     }
/*      */     
/* 1984 */     if (lacksSouth && isFree(tile.tilex, tile.tiley + 1))
/*      */     {
/*      */       
/* 1987 */       tile.addWall(StructureTypeEnum.PLAN, tile.tilex, tile.tiley + 1, tile.tilex + 1, tile.tiley + 1, 10.0F, this.wurmId, false);
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
/*      */ 
/*      */   
/*      */   public static final VolaTile expandStructureToTile(Structure structure, VolaTile toAdd) throws NoSuchZoneException {
/* 2002 */     structure.structureTiles.add(toAdd);
/* 2003 */     toAdd.getZone().addStructure(structure);
/*      */     
/* 2005 */     return toAdd;
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
/*      */   public final void addBuildTile(VolaTile toAdd, boolean loading) throws NoSuchZoneException {
/* 2019 */     if (toAdd.tilex > this.maxX)
/* 2020 */       this.maxX = toAdd.tilex; 
/* 2021 */     if (toAdd.tilex < this.minX)
/* 2022 */       this.minX = toAdd.tilex; 
/* 2023 */     if (toAdd.tiley > this.maxY)
/* 2024 */       this.maxY = toAdd.tiley; 
/* 2025 */     if (toAdd.tiley < this.minY) {
/* 2026 */       this.minY = toAdd.tiley;
/*      */     }
/* 2028 */     if (this.buildTiles.isEmpty() && isFinalized())
/*      */     {
/*      */       
/* 2031 */       addNewBuildTile(toAdd.tilex, toAdd.tiley, toAdd.getLayer());
/*      */     }
/* 2033 */     expandStructureToTile(this, toAdd);
/* 2034 */     if (this.structureType == 0) {
/*      */ 
/*      */ 
/*      */       
/* 2038 */       toAdd.addBuildMarker(this);
/*      */     }
/* 2040 */     else if (loading) {
/* 2041 */       toAdd.setStructureAtLoad(this);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static final VolaTile getFirstNeighbourTileOrNull(VolaTile structureTile) {
/* 2046 */     VolaTile t = Zones.getTileOrNull(structureTile.getTileX() + 1, structureTile.getTileY(), structureTile.isOnSurface());
/* 2047 */     if (t != null && t.getStructure() == structureTile.getStructure())
/* 2048 */       return t; 
/* 2049 */     t = Zones.getTileOrNull(structureTile.getTileX(), structureTile.getTileY() + 1, structureTile.isOnSurface());
/* 2050 */     if (t != null && t.getStructure() == structureTile.getStructure())
/* 2051 */       return t; 
/* 2052 */     t = Zones.getTileOrNull(structureTile.getTileX(), structureTile.getTileY() - 1, structureTile.isOnSurface());
/* 2053 */     if (t != null && t.getStructure() == structureTile.getStructure())
/* 2054 */       return t; 
/* 2055 */     t = Zones.getTileOrNull(structureTile.getTileX() - 1, structureTile.getTileY(), structureTile.isOnSurface());
/* 2056 */     if (t != null && t.getStructure() == structureTile.getStructure())
/* 2057 */       return t; 
/* 2058 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean testRemove(VolaTile tileToCheck) {
/* 2063 */     if (this.structureTiles.size() <= 2)
/* 2064 */       return true; 
/* 2065 */     Set<VolaTile> remainingTiles = new HashSet<>();
/* 2066 */     Set<VolaTile> removedTiles = new HashSet<>();
/* 2067 */     remainingTiles.addAll(this.structureTiles);
/* 2068 */     remainingTiles.remove(tileToCheck);
/*      */     
/* 2070 */     VolaTile firstNeighbour = getFirstNeighbourTileOrNull(tileToCheck);
/*      */     
/* 2072 */     if (firstNeighbour == null)
/*      */     {
/* 2074 */       return true;
/*      */     }
/* 2076 */     removedTiles.add(firstNeighbour);
/*      */ 
/*      */     
/* 2079 */     Set<VolaTile> tilesToRemove = new HashSet<>();
/*      */     
/*      */     while (true) {
/* 2082 */       for (VolaTile removed : removedTiles) {
/*      */         
/* 2084 */         for (VolaTile remaining : remainingTiles) {
/*      */           
/* 2086 */           if (removed.isNextTo(remaining))
/* 2087 */             tilesToRemove.add(remaining); 
/*      */         } 
/*      */       } 
/* 2090 */       if (tilesToRemove.isEmpty())
/*      */       {
/* 2092 */         return remainingTiles.isEmpty();
/*      */       }
/* 2094 */       removedTiles.addAll(tilesToRemove);
/* 2095 */       remainingTiles.removeAll(tilesToRemove);
/* 2096 */       tilesToRemove.clear();
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
/*      */ 
/*      */   
/*      */   public final boolean removeTileFromFinishedStructure(Creature performer, int tilex, int tiley, int layer) {
/* 2111 */     if (this.structureTiles == null)
/* 2112 */       return false; 
/* 2113 */     VolaTile toRemove = null;
/* 2114 */     for (VolaTile tile : this.structureTiles) {
/*      */       
/* 2116 */       int xx = tile.getTileX();
/* 2117 */       int yy = tile.getTileY();
/* 2118 */       if (xx == tilex && yy == tiley) {
/*      */         
/* 2120 */         toRemove = tile;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/* 2125 */     if (!testRemove(toRemove))
/*      */     {
/* 2127 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 2131 */     Wall[] walls = toRemove.getWalls();
/* 2132 */     for (Wall wall : walls) {
/*      */       
/* 2134 */       toRemove.removeWall(wall, false);
/* 2135 */       wall.delete();
/*      */     } 
/*      */ 
/*      */     
/* 2139 */     Floor[] floors = toRemove.getFloors();
/* 2140 */     for (Floor floor : floors) {
/*      */       
/* 2142 */       toRemove.removeFloor(floor);
/* 2143 */       floor.delete();
/*      */     } 
/*      */ 
/*      */     
/* 2147 */     this.structureTiles.remove(toRemove);
/* 2148 */     removeBuildTile(tilex, tiley, layer);
/* 2149 */     MethodsStructure.removeBuildMarker(this, tilex, tiley);
/* 2150 */     setMaxAndMin();
/*      */ 
/*      */     
/* 2153 */     VolaTile westTile = Zones.getTileOrNull(toRemove.getTileX() - 1, toRemove.getTileY(), toRemove.isOnSurface());
/* 2154 */     if (westTile != null && westTile.getStructure() == this)
/* 2155 */       addMissingWallPlans(westTile); 
/* 2156 */     VolaTile eastTile = Zones.getTileOrNull(toRemove.getTileX() + 1, toRemove.getTileY(), toRemove.isOnSurface());
/* 2157 */     if (eastTile != null && eastTile.getStructure() == this) {
/* 2158 */       addMissingWallPlans(eastTile);
/*      */     }
/* 2160 */     VolaTile northTile = Zones.getTileOrNull(toRemove.getTileX(), toRemove.getTileY() - 1, toRemove.isOnSurface());
/* 2161 */     if (northTile != null && northTile.getStructure() == this) {
/* 2162 */       addMissingWallPlans(northTile);
/*      */     }
/* 2164 */     VolaTile southTile = Zones.getTileOrNull(toRemove.getTileX(), toRemove.getTileY() + 1, toRemove.isOnSurface());
/* 2165 */     if (southTile != null && southTile.getStructure() == this) {
/* 2166 */       addMissingWallPlans(southTile);
/*      */     }
/*      */     
/* 2169 */     adjustWallsAroundRemovedStructureTile(this, tilex, tiley);
/*      */     
/* 2171 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean removeTileFromPlannedStructure(Creature aPlanner, int tilex, int tiley) {
/* 2176 */     boolean allowed = false;
/*      */     
/* 2178 */     if (this.structureTiles == null)
/* 2179 */       return false; 
/* 2180 */     VolaTile toRemove = null;
/* 2181 */     for (VolaTile tile : this.structureTiles) {
/*      */       
/* 2183 */       int xx = tile.getTileX();
/* 2184 */       int yy = tile.getTileY();
/* 2185 */       if (xx == tilex && yy == tiley) {
/*      */         
/* 2187 */         toRemove = tile;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/* 2192 */     if (toRemove == null) {
/*      */       
/* 2194 */       logger.warning("Tile " + tilex + "," + tiley + " was not part of structure '" + getWurmId() + "'");
/* 2195 */       return false;
/*      */     } 
/*      */     
/* 2198 */     if (testRemove(toRemove)) {
/*      */       
/* 2200 */       allowed = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2206 */       Wall[] walls = toRemove.getWalls();
/*      */ 
/*      */       
/* 2209 */       for (Wall wall : walls) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2214 */         toRemove.removeWall(wall, false);
/* 2215 */         wall.delete();
/*      */       } 
/*      */ 
/*      */       
/* 2219 */       this.structureTiles.remove(toRemove);
/*      */       
/* 2221 */       MethodsStructure.removeBuildMarker(this, tilex, tiley);
/*      */       
/* 2223 */       setMaxAndMin();
/*      */       
/* 2225 */       VolaTile westTile = Zones.getTileOrNull(toRemove.getTileX() - 1, toRemove.getTileY(), toRemove.isOnSurface());
/* 2226 */       if (westTile != null && westTile.getStructure() == this)
/* 2227 */         addMissingWallPlans(westTile); 
/* 2228 */       VolaTile eastTile = Zones.getTileOrNull(toRemove.getTileX() + 1, toRemove.getTileY(), toRemove.isOnSurface());
/* 2229 */       if (eastTile != null && eastTile.getStructure() == this) {
/* 2230 */         addMissingWallPlans(eastTile);
/*      */       }
/* 2232 */       VolaTile northTile = Zones.getTileOrNull(toRemove.getTileX(), toRemove.getTileY() - 1, toRemove.isOnSurface());
/* 2233 */       if (northTile != null && northTile.getStructure() == this) {
/* 2234 */         addMissingWallPlans(northTile);
/*      */       }
/* 2236 */       VolaTile southTile = Zones.getTileOrNull(toRemove.getTileX(), toRemove.getTileY() + 1, toRemove.isOnSurface());
/* 2237 */       if (southTile != null && southTile.getStructure() == this) {
/* 2238 */         addMissingWallPlans(southTile);
/*      */       }
/*      */     } 
/* 2241 */     if (this.structureTiles.isEmpty()) {
/*      */       
/* 2243 */       aPlanner.setStructure(null);
/*      */       
/*      */       try {
/* 2246 */         aPlanner.save();
/*      */       }
/* 2248 */       catch (Exception iox) {
/*      */         
/* 2250 */         logger.log(Level.WARNING, "Failed to save player " + aPlanner.getName() + ", StructureId: " + this.wurmId, iox);
/*      */       } 
/* 2252 */       Structures.removeStructure(this.wurmId);
/*      */     } 
/*      */     
/* 2255 */     return allowed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void addDoor(Door door) {
/* 2263 */     if (this.doors == null)
/* 2264 */       this.doors = new HashSet<>(); 
/* 2265 */     if (!this.doors.contains(door)) {
/*      */       
/* 2267 */       this.doors.add(door);
/* 2268 */       door.setStructureId(this.wurmId);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final Door[] getAllDoors() {
/* 2274 */     Door[] toReturn = new Door[0];
/* 2275 */     if (this.doors != null && this.doors.size() != 0)
/*      */     {
/* 2277 */       toReturn = this.doors.<Door>toArray(new Door[this.doors.size()]);
/*      */     }
/* 2279 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Door[] getAllDoors(boolean includeAll) {
/* 2284 */     Set<Door> ldoors = new HashSet<>();
/* 2285 */     if (this.doors != null && this.doors.size() != 0)
/*      */     {
/* 2287 */       for (Door door : this.doors) {
/*      */         
/* 2289 */         if (includeAll || door.hasLock())
/* 2290 */           ldoors.add(door); 
/*      */       } 
/*      */     }
/* 2293 */     return ldoors.<Door>toArray(new Door[ldoors.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void removeDoor(Door door) {
/* 2298 */     if (this.doors != null) {
/*      */       
/* 2300 */       this.doors.remove(door);
/* 2301 */       door.delete();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void unlockAllDoors() {
/* 2307 */     Door[] lDoors = getAllDoors();
/* 2308 */     for (int x = 0; x < lDoors.length; x++) {
/* 2309 */       lDoors[x].unlock((x == 0));
/*      */     }
/*      */   }
/*      */   
/*      */   public final void lockAllDoors() {
/* 2314 */     Door[] lDoors = getAllDoors();
/* 2315 */     for (int x = 0; x < lDoors.length; x++) {
/* 2316 */       lDoors[x].lock((x == 0));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isLocked() {
/* 2327 */     Door[] lDoors = getAllDoors();
/* 2328 */     for (int x = 0; x < lDoors.length; x++) {
/*      */       
/* 2330 */       if (!lDoors[x].isLocked())
/* 2331 */         return false; 
/*      */     } 
/* 2333 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isLockable() {
/* 2338 */     Door[] lDoors = getAllDoors();
/* 2339 */     for (int x = 0; x < lDoors.length; x++) {
/*      */ 
/*      */       
/*      */       try {
/* 2343 */         lDoors[x].getLock();
/*      */       }
/* 2345 */       catch (NoSuchLockException nsl) {
/*      */         
/* 2347 */         return false;
/*      */       } 
/*      */     } 
/* 2350 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isTypeBridge() {
/* 2355 */     return (this.structureType == 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isTypeHouse() {
/* 2360 */     return (this.structureType == 0);
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
/*      */   private void finalizeBuildPlanForTiles(long oldStructureId) throws IOException {
/* 2374 */     for (Iterator<VolaTile> it = this.structureTiles.iterator(); it.hasNext(); ) {
/*      */       
/* 2376 */       VolaTile tile = it.next();
/*      */       
/* 2378 */       tile.finalizeBuildPlan(oldStructureId, this.wurmId);
/*      */       
/* 2380 */       addNewBuildTile(tile.tilex, tile.tiley, tile.getLayer());
/* 2381 */       Wall[] walls = tile.getWalls();
/* 2382 */       for (int x = 0; x < walls.length; x++) {
/*      */         
/* 2384 */         walls[x].setStructureId(this.wurmId);
/* 2385 */         walls[x].save();
/*      */       } 
/* 2387 */       Floor[] floors = tile.getFloors();
/* 2388 */       for (int i = 0; i < floors.length; i++) {
/*      */         
/* 2390 */         floors[i].setStructureId(this.wurmId);
/* 2391 */         floors[i].save();
/*      */       } 
/* 2393 */       BridgePart[] bridgeParts = tile.getBridgeParts();
/* 2394 */       for (int j = 0; j < bridgeParts.length; j++) {
/*      */         
/* 2396 */         bridgeParts[j].setStructureId(this.wurmId);
/* 2397 */         bridgeParts[j].save();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean makeFinal(Creature aOwner, String aName) throws IOException, NoSuchZoneException {
/* 2408 */     int size = this.structureTiles.size();
/* 2409 */     if (size > 0) {
/*      */       String sName;
/* 2411 */       if (this.structureType == 1) {
/*      */         
/* 2413 */         sName = aName;
/* 2414 */         Achievements.triggerAchievement(aOwner.getWurmId(), 557);
/*      */       }
/* 2416 */       else if (size <= 2) {
/* 2417 */         sName = aName + "shed";
/* 2418 */       } else if (size <= 3) {
/* 2419 */         sName = aName + "shack";
/* 2420 */       } else if (size <= 5) {
/* 2421 */         sName = aName + "cottage";
/* 2422 */       } else if (size <= 6) {
/* 2423 */         sName = aName + "house";
/* 2424 */       } else if (size <= 10) {
/* 2425 */         sName = aName + "villa";
/* 2426 */       } else if (size <= 20) {
/* 2427 */         sName = aName + "mansion";
/* 2428 */       } else if (size <= 30) {
/* 2429 */         sName = aName + "estate";
/*      */       } else {
/* 2431 */         sName = aName + "stronghold";
/*      */       } 
/* 2433 */       long oldStructureId = this.wurmId;
/* 2434 */       this.wurmId = WurmId.getNextStructureId();
/* 2435 */       Structures.removeStructure(oldStructureId);
/* 2436 */       this.name = sName;
/*      */       
/* 2438 */       Structures.addStructure(this);
/* 2439 */       finalizeBuildPlanForTiles(oldStructureId);
/*      */       
/* 2441 */       Zone northW = null;
/* 2442 */       Zone northE = null;
/* 2443 */       Zone southW = null;
/* 2444 */       Zone southE = null;
/*      */       
/*      */       try {
/* 2447 */         northW = Zones.getZone(this.minX, this.minY, this.surfaced);
/* 2448 */         northW.addStructure(this);
/*      */       }
/* 2450 */       catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 2456 */         northE = Zones.getZone(this.maxX, this.minY, this.surfaced);
/* 2457 */         if (northE != northW) {
/* 2458 */           northE.addStructure(this);
/*      */         }
/* 2460 */       } catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 2466 */         southE = Zones.getZone(this.maxX, this.maxY, this.surfaced);
/* 2467 */         if (southE != northE && southE != northW) {
/* 2468 */           southE.addStructure(this);
/*      */         }
/* 2470 */       } catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 2476 */         southW = Zones.getZone(this.minX, this.maxY, this.surfaced);
/* 2477 */         if (southW != northE && southW != northW && southW != southE) {
/* 2478 */           southW.addStructure(this);
/*      */         }
/* 2480 */       } catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */ 
/*      */       
/* 2484 */       this.writid = -10L;
/*      */       
/* 2486 */       setPlanner(aOwner.getName());
/* 2487 */       setOwnerId(aOwner.getWurmId());
/* 2488 */       save();
/* 2489 */       return true;
/*      */     } 
/*      */     
/* 2492 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void clearAllWallsAndMakeWallsForStructureBorder(VolaTile toAdd) {
/* 2497 */     for (VolaTile tile : createNeighbourStructureTiles(this, toAdd))
/*      */     {
/* 2499 */       destroyWallsBorderingToTile(tile, toAdd);
/*      */     }
/* 2501 */     addMissingWallPlans(toAdd);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void destroyWallsBorderingToTile(VolaTile start, VolaTile target) {
/* 2507 */     boolean destroy = false;
/* 2508 */     for (Wall wall : start.getWalls()) {
/*      */       
/* 2510 */       destroy = false;
/* 2511 */       if (wall.isHorizontal() && wall.getMinX() == target.getTileX())
/*      */       {
/* 2513 */         if (wall.getMinY() == target.getTileY()) {
/*      */ 
/*      */           
/* 2516 */           destroy = true;
/*      */         }
/* 2518 */         else if (wall.getMinY() == target.getTileY() + 1) {
/*      */ 
/*      */           
/* 2521 */           destroy = true;
/*      */         } 
/*      */       }
/* 2524 */       if (!wall.isHorizontal() && wall.getMinY() == target.getTileY())
/*      */       {
/* 2526 */         if (wall.getMinX() == target.getTileX()) {
/*      */ 
/*      */           
/* 2529 */           destroy = true;
/*      */         }
/* 2531 */         else if (wall.getMinX() == target.getTileX() + 1) {
/*      */ 
/*      */           
/* 2534 */           destroy = true;
/*      */         } 
/*      */       }
/* 2537 */       if (destroy) {
/*      */         
/* 2539 */         start.removeWall(wall, false);
/* 2540 */         wall.delete();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isFree(int x, int y) {
/* 2547 */     return !contains(x, y);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isFinished() {
/* 2556 */     return this.finished;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isFinalFinished() {
/* 2567 */     return this.finalfinished;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean needsDoor() {
/* 2573 */     int free = 0;
/* 2574 */     for (Iterator<VolaTile> it = this.structureTiles.iterator(); it.hasNext(); ) {
/*      */       
/* 2576 */       VolaTile tile = it.next();
/* 2577 */       Wall[] wallArr = tile.getWallsForLevel(0);
/* 2578 */       for (int x = 0; x < wallArr.length; x++) {
/*      */         
/* 2580 */         StructureTypeEnum type = wallArr[x].getType();
/* 2581 */         if (type == StructureTypeEnum.DOOR)
/* 2582 */           return false; 
/* 2583 */         if (type == StructureTypeEnum.DOUBLE_DOOR)
/* 2584 */           return false; 
/* 2585 */         if (Wall.isArched(type))
/* 2586 */           return false; 
/* 2587 */         if (type == StructureTypeEnum.PORTCULLIS)
/* 2588 */           return false; 
/* 2589 */         if (type == StructureTypeEnum.CANOPY_DOOR)
/* 2590 */           return false; 
/* 2591 */         if (type == StructureTypeEnum.PLAN)
/* 2592 */           free++; 
/*      */       } 
/*      */     } 
/* 2595 */     if (free < 2)
/* 2596 */       return true; 
/* 2597 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getDoors() {
/* 2606 */     int numdoors = 0;
/* 2607 */     for (Iterator<VolaTile> it = this.structureTiles.iterator(); it.hasNext(); ) {
/*      */       
/* 2609 */       VolaTile tile = it.next();
/* 2610 */       Wall[] wallArr = tile.getWalls();
/* 2611 */       for (int x = 0; x < wallArr.length; x++) {
/*      */         
/* 2613 */         StructureTypeEnum type = wallArr[x].getType();
/* 2614 */         if (type == StructureTypeEnum.DOOR)
/* 2615 */           numdoors++; 
/* 2616 */         if (type == StructureTypeEnum.DOUBLE_DOOR)
/* 2617 */           numdoors++; 
/* 2618 */         if (Wall.isArched(type))
/* 2619 */           numdoors++; 
/* 2620 */         if (type == StructureTypeEnum.PORTCULLIS)
/* 2621 */           numdoors++; 
/* 2622 */         if (type == StructureTypeEnum.CANOPY_DOOR)
/* 2623 */           numdoors++; 
/*      */       } 
/*      */     } 
/* 2626 */     return numdoors;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean updateStructureFinishFlag() {
/* 2637 */     for (Iterator<VolaTile> it = this.structureTiles.iterator(); it.hasNext(); ) {
/*      */       
/* 2639 */       VolaTile tile = it.next();
/* 2640 */       if (this.structureType == 0) {
/*      */         
/* 2642 */         Wall[] wallArr = tile.getWalls();
/* 2643 */         for (int i = 0; i < wallArr.length; i++) {
/*      */           
/* 2645 */           if (!wallArr[i].isIndoor())
/*      */           {
/*      */             
/* 2648 */             if (!wallArr[i].isFinished()) {
/*      */ 
/*      */               
/* 2651 */               setFinished(false);
/* 2652 */               setFinalFinished(false);
/* 2653 */               return false;
/*      */             } 
/*      */           }
/*      */         } 
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/* 2661 */       BridgePart[] bridgeParts = tile.getBridgeParts();
/* 2662 */       for (int x = 0; x < bridgeParts.length; x++) {
/*      */         
/* 2664 */         if (!bridgeParts[x].isFinished()) {
/*      */ 
/*      */           
/* 2667 */           setFinished(false);
/* 2668 */           setFinalFinished(false);
/* 2669 */           return false;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2674 */     setFinished(true);
/* 2675 */     setFinalFinished(true);
/* 2676 */     Players.getInstance().setStructureFinished(this.wurmId);
/* 2677 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isFinalized() {
/* 2682 */     return (WurmId.getType(this.wurmId) == 4);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean contains(int tilex, int tiley) {
/* 2687 */     if (this.structureTiles == null) {
/*      */       
/* 2689 */       logger.log(Level.WARNING, "StructureTiles is null in building with id " + this.wurmId);
/* 2690 */       return true;
/*      */     } 
/* 2692 */     for (VolaTile tile : this.structureTiles) {
/*      */       
/* 2694 */       if (tilex == tile.tilex)
/*      */       {
/* 2696 */         if (tiley == tile.tiley)
/* 2697 */           return true; 
/*      */       }
/*      */     } 
/* 2700 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isOnSurface() {
/* 2705 */     return this.surfaced;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getWurmId() {
/* 2715 */     return this.wurmId;
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
/* 2727 */     return -10;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxAllowed() {
/* 2738 */     return AnimalSettings.getMaxAllowed();
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
/* 2749 */     return (getOwnerId() == playerId);
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
/* 2760 */     return isOwner(creature.getWurmId());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOwner(long playerId) {
/* 2771 */     if (isManaged()) {
/*      */       
/* 2773 */       Village vill = getManagedByVillage();
/* 2774 */       if (vill != null)
/* 2775 */         return vill.isMayor(playerId); 
/*      */     } 
/* 2777 */     return isActualOwner(playerId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayPlaceMerchants(Creature creature) {
/* 2787 */     if (StructureSettings.isExcluded(this, creature))
/* 2788 */       return false; 
/* 2789 */     return StructureSettings.mayPlaceMerchants(this, creature);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayUseBed(Creature creature) {
/* 2800 */     if (isOwner(creature))
/* 2801 */       return true; 
/* 2802 */     if (isGuest(creature))
/* 2803 */       return true; 
/* 2804 */     if (allowsCitizens() && isInOwnerSettlement(creature))
/* 2805 */       return true; 
/* 2806 */     if (allowsAllies() && isInOwnerAlliance(creature))
/* 2807 */       return true; 
/* 2808 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayPickupPlanted(Creature creature) {
/* 2819 */     if (StructureSettings.isExcluded(this, creature))
/* 2820 */       return false; 
/* 2821 */     return StructureSettings.mayPickupPlanted(this, creature);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayLoad(Creature creature) {
/* 2831 */     if (isEnemy(creature))
/* 2832 */       return true; 
/* 2833 */     if (StructureSettings.isExcluded(this, creature))
/* 2834 */       return false; 
/* 2835 */     return StructureSettings.mayLoad(this, creature);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInOwnerSettlement(Creature creature) {
/* 2842 */     if (creature.getCitizenVillage() != null) {
/*      */       
/* 2844 */       long wid = getOwnerId();
/* 2845 */       if (wid != -10L) {
/*      */         
/* 2847 */         Village creatorVillage = Villages.getVillageForCreature(wid);
/* 2848 */         return (creatorVillage != null && creature.getCitizenVillage().getId() == creatorVillage.getId());
/*      */       } 
/*      */     } 
/* 2851 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInOwnerAlliance(Creature creature) {
/* 2859 */     if (creature.getCitizenVillage() != null) {
/*      */       
/* 2861 */       long wid = getOwnerId();
/* 2862 */       if (wid != -10L) {
/*      */         
/* 2864 */         Village creatorVillage = Villages.getVillageForCreature(wid);
/* 2865 */         return (creatorVillage != null && creature.getCitizenVillage().isAlly(creatorVillage));
/*      */       } 
/*      */     } 
/* 2868 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getCenterX() {
/* 2873 */     return this.minX + Math.max(1, this.maxX - this.minX) / 2;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getCenterY() {
/* 2878 */     return this.minY + Math.max(1, this.maxY - this.minY) / 2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxX() {
/* 2887 */     return this.maxX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxY() {
/* 2896 */     return this.maxY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMinX() {
/* 2905 */     return this.minX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMinY() {
/* 2914 */     return this.minY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final VolaTile[] getStructureTiles() {
/* 2923 */     VolaTile[] tiles = new VolaTile[this.structureTiles.size()];
/* 2924 */     return this.structureTiles.<VolaTile>toArray(tiles);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean allowsAllies() {
/* 2929 */     return this.allowsAllies;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean allowsKingdom() {
/* 2934 */     return this.allowsKingdom;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean allowsCitizens() {
/* 2939 */     return this.allowsVillagers;
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
/* 2950 */     return this.permissions.hasPermission(Permissions.Allow.SETTLEMENT_MAY_MANAGE.getBit());
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
/* 2963 */     if (player.getPower() > 1)
/* 2964 */       return true; 
/* 2965 */     if (isManaged()) {
/*      */       
/* 2967 */       Village vil = getPermissionsVillage();
/* 2968 */       if (vil != null)
/* 2969 */         return vil.isMayor((Creature)player); 
/* 2970 */       return false;
/*      */     } 
/*      */     
/* 2973 */     return isOwner((Creature)player);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsManaged(boolean newIsManaged, Player player) {
/* 2984 */     int oldId = this.villageId;
/* 2985 */     if (newIsManaged) {
/*      */ 
/*      */       
/* 2988 */       Village v = getVillage();
/* 2989 */       if (v != null) {
/* 2990 */         this.villageId = v.getId();
/*      */       } else {
/*      */         
/* 2993 */         Village cv = player.getCitizenVillage();
/* 2994 */         if (cv != null) {
/* 2995 */           this.villageId = cv.getId();
/*      */         } else {
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     } else {
/* 3002 */       this.villageId = -1;
/*      */     } 
/* 3004 */     if (oldId != this.villageId && StructureSettings.exists(getWurmId())) {
/*      */       
/* 3006 */       StructureSettings.remove(getWurmId());
/* 3007 */       PermissionsHistories.addHistoryEntry(getWurmId(), System.currentTimeMillis(), -10L, "Auto", "Cleared Permissions");
/*      */     } 
/*      */     
/* 3010 */     this.permissions.setPermissionBit(Permissions.Allow.SETTLEMENT_MAY_MANAGE.getBit(), newIsManaged);
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
/*      */   public String mayManageText(Player player) {
/* 3022 */     String sName = getVillageName(player);
/* 3023 */     if (sName.length() > 0)
/* 3024 */       return "Settlement \"" + sName + "\" may manage"; 
/* 3025 */     return sName;
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
/* 3036 */     return "";
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
/* 3047 */     return "This gives full control to the settlement";
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
/* 3058 */     return "Did you realy mean to do that?";
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
/* 3069 */     return "Doing this reverts the control back to the owner.";
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
/* 3080 */     return "Are you really positive you want to do that?";
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
/* 3091 */     String sName = "";
/* 3092 */     Village vill = getPermissionsVillage();
/* 3093 */     if (vill != null)
/* 3094 */       sName = vill.getName(); 
/* 3095 */     if (sName.length() > 0)
/* 3096 */       return "Citizens of \"" + sName + "\""; 
/* 3097 */     return sName;
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
/* 3108 */     String aName = "";
/* 3109 */     Village vill = getPermissionsVillage();
/* 3110 */     if (vill != null)
/* 3111 */       aName = vill.getAllianceName(); 
/* 3112 */     if (aName.length() > 0)
/* 3113 */       return "Alliance of \"" + aName + "\""; 
/* 3114 */     return "";
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
/* 3125 */     byte kingdom = 0;
/* 3126 */     Village vill = getPermissionsVillage();
/* 3127 */     if (vill != null) {
/* 3128 */       kingdom = vill.kingdom;
/*      */     } else {
/* 3130 */       kingdom = Players.getInstance().getKingdomForPlayer(getOwnerId());
/*      */     } 
/* 3132 */     return "Kingdom of \"" + Kingdoms.getNameFor(kingdom) + "\"";
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
/* 3143 */     return false;
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
/* 3154 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean hasLoaded() {
/* 3164 */     return this.hasLoaded;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void setHasLoaded(boolean aHasLoaded) {
/* 3175 */     this.hasLoaded = aHasLoaded;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isLoading() {
/* 3185 */     return this.isLoading;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void setLoading(boolean aIsLoading) {
/* 3196 */     this.isLoading = aIsLoading;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isSurfaced() {
/* 3206 */     return this.surfaced;
/*      */   }
/*      */ 
/*      */   
/*      */   public final byte getLayer() {
/* 3211 */     if (this.surfaced) {
/* 3212 */       return 0;
/*      */     }
/* 3214 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void setSurfaced(boolean aSurfaced) {
/* 3225 */     this.surfaced = aSurfaced;
/*      */   }
/*      */ 
/*      */   
/*      */   final void setStructureType(byte theStructureType) {
/* 3230 */     this.structureType = theStructureType;
/*      */   }
/*      */ 
/*      */   
/*      */   public final byte getStructureType() {
/* 3235 */     return this.structureType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final long getWritid() {
/* 3245 */     return this.writid;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setWritid(long aWritid, boolean save) {
/* 3256 */     this.writid = aWritid;
/* 3257 */     if (save) {
/*      */       
/*      */       try {
/* 3260 */         saveWritId();
/*      */       }
/* 3262 */       catch (IOException iox) {
/*      */         
/* 3264 */         logger.log(Level.INFO, "Problems saving WritId " + aWritid + ", StructureId: " + this.wurmId + iox.getMessage(), iox);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public final Set<StructureSupport> getAllSupports() {
/* 3270 */     Set<StructureSupport> toReturn = new HashSet<>();
/* 3271 */     if (this.structureTiles == null)
/* 3272 */       return toReturn; 
/* 3273 */     for (VolaTile tile : this.structureTiles)
/*      */     {
/* 3275 */       toReturn.addAll(tile.getAllSupport());
/*      */     }
/* 3277 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void addAllGroundStructureSupportToSet(Set<StructureSupport> supportingSupports, Set<StructureSupport> remainingSupports) {
/* 3283 */     Set<StructureSupport> toMove = new HashSet<>();
/* 3284 */     for (StructureSupport remaining : remainingSupports) {
/*      */       
/* 3286 */       if (remaining.isSupportedByGround())
/*      */       {
/* 3288 */         toMove.add(remaining);
/*      */       }
/*      */     } 
/* 3291 */     supportingSupports.addAll(toMove);
/* 3292 */     remainingSupports.removeAll(toMove);
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
/*      */   public final boolean wouldCreateFlyingStructureIfRemoved(StructureSupport supportToCheck) {
/* 3305 */     Set<StructureSupport> allSupports = getAllSupports();
/*      */     
/* 3307 */     Set<StructureSupport> supportingSupports = new HashSet<>();
/*      */ 
/*      */     
/* 3310 */     allSupports.remove(supportToCheck);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3319 */     StructureSupport match = null;
/* 3320 */     for (StructureSupport csupport : allSupports) {
/*      */       
/* 3322 */       if (csupport.getId() == supportToCheck.getId())
/* 3323 */         match = csupport; 
/*      */     } 
/* 3325 */     if (match != null)
/*      */     {
/* 3327 */       allSupports.remove(match);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3333 */     addAllGroundStructureSupportToSet(supportingSupports, allSupports);
/* 3334 */     Set<StructureSupport> toRemove = new HashSet<>();
/*      */ 
/*      */ 
/*      */     
/* 3338 */     while (!allSupports.isEmpty()) {
/*      */       
/* 3340 */       for (StructureSupport checked : supportingSupports) {
/*      */         
/* 3342 */         for (StructureSupport remaining : allSupports) {
/*      */           
/* 3344 */           if (checked.supports(remaining))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 3352 */             toRemove.add(remaining);
/*      */           }
/*      */         } 
/*      */       } 
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
/* 3366 */       if (toRemove.isEmpty()) {
/*      */         break;
/*      */       }
/*      */       
/* 3370 */       supportingSupports.addAll(toRemove);
/* 3371 */       allSupports.removeAll(toRemove);
/* 3372 */       toRemove.clear();
/*      */     } 
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
/* 3387 */     return !allSupports.isEmpty();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3393 */   public static final int[] noEntrance = new int[] { -1, -1 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int[] getNortEntrance() {
/* 3401 */     return new int[] { this.minX, this.minY - 1 };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int[] getSouthEntrance() {
/* 3410 */     return new int[] { this.maxX, this.maxY + 1 };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int[] getWestEntrance() {
/* 3419 */     return new int[] { this.minX - 1, this.minY };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int[] getEastEntrance() {
/* 3428 */     return new int[] { this.maxX + 1, this.maxY };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isHorizontal() {
/* 3434 */     return (this.minX < this.maxX && this.minY == this.maxY);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean containsSettlement(int[] tileCoords, int layer) {
/* 3439 */     if (tileCoords[0] == -1)
/* 3440 */       return false; 
/* 3441 */     VolaTile t = Zones.getTileOrNull(tileCoords[0], tileCoords[1], (layer == 0));
/* 3442 */     if (t != null)
/* 3443 */       return (t.getVillage() != null); 
/* 3444 */     return false;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int[] findBestBridgeEntrance(Creature creature, int tilex, int tiley, int layer, long bridgeId, int currentPathFindCounter) {
/* 3465 */     int lMaxX = isHorizontal() ? (getMaxX() + 1) : getMaxX();
/* 3466 */     int lMinX = isHorizontal() ? (getMinX() - 1) : getMinX();
/* 3467 */     int lMinY = isHorizontal() ? getMinY() : (getMinY() - 1);
/* 3468 */     int lMaxY = isHorizontal() ? getMaxY() : (getMaxY() + 1);
/* 3469 */     int[] min = { lMinX, lMinY };
/*      */     
/* 3471 */     int[] max = { lMaxX, lMaxY };
/*      */ 
/*      */ 
/*      */     
/* 3475 */     if (!creature.isUnique() && 
/* 3476 */       containsSettlement(min, layer) && containsSettlement(max, layer)) {
/* 3477 */       return noEntrance;
/*      */     }
/* 3479 */     boolean switchEntrance = (currentPathFindCounter > 5 && Server.rand.nextBoolean());
/*      */     
/* 3481 */     if (isHorizontal() ? (creature.getTileX() >= lMaxX) : (creature.getTileY() >= lMaxY))
/* 3482 */       if ((!containsSettlement(max, layer) || creature.isUnique()) && 
/* 3483 */         !switchEntrance)
/*      */       {
/* 3485 */         return max;
/*      */       } 
/* 3487 */     if (isHorizontal() ? (creature.getTileX() <= lMinX) : (creature.getTileY() <= lMinY))
/* 3488 */       if ((!containsSettlement(min, layer) || creature.isUnique()) && 
/* 3489 */         !switchEntrance)
/*      */       {
/* 3491 */         return min;
/*      */       } 
/* 3493 */     int diffMax = Math.abs(isHorizontal() ? (creature.getTileX() - lMaxX) : (creature.getTileY() - lMaxY));
/* 3494 */     int diffMin = isHorizontal() ? (creature.getTileX() - lMinX) : (creature.getTileY() - lMinY);
/* 3495 */     if (diffMax <= diffMin) {
/*      */       
/* 3497 */       if ((!containsSettlement(max, layer) || creature.isUnique()) && 
/* 3498 */         !switchEntrance)
/*      */       {
/* 3500 */         return max;
/*      */       }
/*      */       
/* 3503 */       return min;
/*      */     } 
/* 3505 */     if (diffMin <= diffMax) {
/*      */       
/* 3507 */       if ((!containsSettlement(min, layer) || creature.isUnique()) && 
/* 3508 */         !switchEntrance)
/*      */       {
/* 3510 */         return min;
/*      */       }
/*      */       
/* 3513 */       return max;
/*      */     } 
/*      */     
/* 3516 */     if (Server.rand.nextBoolean()) {
/*      */       
/* 3518 */       if ((!containsSettlement(max, layer) || creature.isUnique()) && 
/* 3519 */         !switchEntrance)
/*      */       {
/* 3521 */         return max;
/*      */       }
/*      */       
/* 3524 */       return min;
/*      */     } 
/*      */ 
/*      */     
/* 3528 */     if ((!containsSettlement(min, layer) || creature.isUnique()) && 
/* 3529 */       !switchEntrance)
/*      */     {
/* 3531 */       return min;
/*      */     }
/*      */     
/* 3534 */     return max;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isBridgeJustPlans() {
/* 3540 */     if (this.structureType != 1)
/* 3541 */       return false; 
/* 3542 */     for (BridgePart bp : getBridgeParts()) {
/*      */       
/* 3544 */       if (bp.getBridgePartState() != BridgeConstants.BridgeState.PLANNED)
/* 3545 */         return false; 
/*      */     } 
/* 3547 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isBridgeGone() {
/* 3558 */     if (isBridgeJustPlans()) {
/*      */ 
/*      */       
/* 3561 */       for (BridgePart bp : getBridgeParts())
/*      */       {
/* 3563 */         bp.destroy();
/*      */       }
/* 3565 */       totallyDestroy();
/* 3566 */       return true;
/*      */     } 
/* 3568 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   void addDefaultAllyPermissions() {
/* 3573 */     if (!getPermissionsPlayerList().exists(-20L)) {
/*      */ 
/*      */       
/* 3576 */       int value = StructureSettings.StructurePermissions.PASS.getValue() + StructureSettings.StructurePermissions.PICKUP.getValue();
/* 3577 */       addNewGuest(-20L, value);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addDefaultCitizenPermissions() {
/* 3584 */     if (!getPermissionsPlayerList().exists(-30L)) {
/*      */ 
/*      */       
/* 3587 */       int value = StructureSettings.StructurePermissions.PASS.getValue() + StructureSettings.StructurePermissions.PICKUP.getValue();
/* 3588 */       addNewGuest(-30L, value);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void addDefaultKingdomPermissions() {
/* 3594 */     if (!getPermissionsPlayerList().exists(-40L)) {
/*      */       
/* 3596 */       int value = StructureSettings.StructurePermissions.PASS.getValue();
/* 3597 */       addNewGuest(-40L, value);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setWalkedOnBridge(long now) {
/* 3603 */     long lastUsed = 0L;
/*      */     
/* 3605 */     for (BridgePart bp : getBridgeParts()) {
/*      */       
/* 3607 */       if (bp.isFinished())
/*      */       {
/* 3609 */         if (lastUsed < bp.getLastUsed()) {
/* 3610 */           lastUsed = bp.getLastUsed();
/*      */         }
/*      */       }
/*      */     } 
/* 3614 */     if (lastUsed < now - 86400000L)
/*      */     {
/*      */       
/* 3617 */       for (BridgePart bp : getBridgeParts()) {
/* 3618 */         bp.setLastUsed(now);
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
/*      */   public boolean setNewOwner(long playerId) {
/* 3630 */     if (this.writid != -10L)
/*      */     {
/*      */       
/* 3633 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 3637 */     if (!isManaged() && StructureSettings.exists(getWurmId())) {
/*      */       
/* 3639 */       StructureSettings.remove(getWurmId());
/* 3640 */       PermissionsHistories.addHistoryEntry(getWurmId(), System.currentTimeMillis(), -10L, "Auto", "Cleared Permissions");
/*      */     } 
/*      */     
/* 3643 */     this.ownerId = playerId;
/*      */     
/*      */     try {
/* 3646 */       saveOwnerId();
/* 3647 */       return true;
/*      */     }
/* 3649 */     catch (IOException iOException) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3654 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getOwnerName() {
/* 3665 */     getOwnerId();
/* 3666 */     if (this.writid != -10L)
/*      */     {
/* 3668 */       return "has writ";
/*      */     }
/* 3670 */     return PlayerInfoFactory.getPlayerName(this.ownerId);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean convertToNewPermissions() {
/* 3675 */     boolean didConvert = false;
/* 3676 */     PermissionsPlayerList ppl = StructureSettings.getPermissionsPlayerList(this.wurmId);
/* 3677 */     if (this.allowsAllies && !ppl.exists(-20L)) {
/*      */       
/* 3679 */       addDefaultAllyPermissions();
/* 3680 */       didConvert = true;
/*      */     } 
/* 3682 */     if (this.allowsVillagers && !ppl.exists(-30L)) {
/*      */       
/* 3684 */       addDefaultCitizenPermissions();
/* 3685 */       didConvert = true;
/*      */     } 
/* 3687 */     if (this.allowsKingdom && !ppl.exists(-40L)) {
/*      */       
/* 3689 */       addDefaultKingdomPermissions();
/* 3690 */       didConvert = true;
/*      */     } 
/* 3692 */     if (didConvert) {
/*      */       
/*      */       try {
/* 3695 */         saveSettings();
/*      */       }
/* 3697 */       catch (IOException e) {
/*      */ 
/*      */         
/* 3700 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     }
/* 3703 */     for (Door d : getAllDoors())
/*      */     {
/* 3705 */       d.setIsManaged(true, null);
/*      */     }
/* 3707 */     return didConvert;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getKingdomId() {
/* 3712 */     byte kingdom = 0;
/* 3713 */     Village vill = getPermissionsVillage();
/* 3714 */     if (vill != null) {
/* 3715 */       kingdom = vill.kingdom;
/*      */     } else {
/* 3717 */       kingdom = Players.getInstance().getKingdomForPlayer(getOwnerId());
/*      */     } 
/* 3719 */     return kingdom;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isGroundFloorAtPosition(float x, float y, boolean isOnSurface) {
/* 3724 */     TilePos tilePos = CoordUtils.WorldToTile(x, y);
/* 3725 */     VolaTile tile = Zones.getOrCreateTile(tilePos, isOnSurface);
/* 3726 */     if (tile != null) {
/*      */       
/* 3728 */       Floor[] floors = tile.getFloors(0, 0);
/* 3729 */       if (floors != null && floors.length > 0 && floors[0].getType() == StructureConstants.FloorType.FLOOR)
/*      */       {
/* 3731 */         return true;
/*      */       }
/*      */     } 
/*      */     
/* 3735 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDestroyed() {
/* 3740 */     if (isTypeBridge()) {
/* 3741 */       return (isBridgeJustPlans() || isBridgeGone());
/*      */     }
/* 3743 */     Wall[] walls = getWalls();
/* 3744 */     Floor[] floors = getFloors();
/* 3745 */     boolean destroyed = true;
/*      */     
/* 3747 */     for (Wall wall : walls) {
/*      */       
/* 3749 */       if (!wall.isWallPlan()) {
/* 3750 */         destroyed = false;
/*      */       }
/*      */     } 
/* 3753 */     for (Floor floor : floors) {
/*      */       
/* 3755 */       if (!floor.isAPlan()) {
/* 3756 */         destroyed = false;
/*      */       }
/*      */     } 
/* 3759 */     return destroyed;
/*      */   }
/*      */ 
/*      */   
/*      */   abstract void setFinalFinished(boolean paramBoolean);
/*      */ 
/*      */   
/*      */   public abstract void setFinished(boolean paramBoolean);
/*      */ 
/*      */   
/*      */   public abstract void endLoading() throws IOException;
/*      */ 
/*      */   
/*      */   abstract void load() throws IOException, NoSuchStructureException;
/*      */ 
/*      */   
/*      */   public abstract void save() throws IOException;
/*      */ 
/*      */   
/*      */   public abstract void saveWritId() throws IOException;
/*      */ 
/*      */   
/*      */   public abstract void saveOwnerId() throws IOException;
/*      */ 
/*      */   
/*      */   public abstract void saveSettings() throws IOException;
/*      */ 
/*      */   
/*      */   public abstract void saveName() throws IOException;
/*      */ 
/*      */   
/*      */   abstract void delete();
/*      */ 
/*      */   
/*      */   abstract void removeStructureGuest(long paramLong);
/*      */ 
/*      */   
/*      */   abstract void addNewGuest(long paramLong, int paramInt);
/*      */   
/*      */   public abstract void setAllowAllies(boolean paramBoolean);
/*      */   
/*      */   public abstract void setAllowVillagers(boolean paramBoolean);
/*      */   
/*      */   public abstract void setAllowKingdom(boolean paramBoolean);
/*      */   
/*      */   public String toString() {
/* 3805 */     return "Structure [wurmId=" + this.wurmId + ", surfaced=" + this.surfaced + ", name=" + this.name + ", writid=" + this.writid + "]";
/*      */   }
/*      */   
/*      */   public abstract void removeBuildTile(int paramInt1, int paramInt2, int paramInt3);
/*      */   
/*      */   public abstract void addNewBuildTile(int paramInt1, int paramInt2, int paramInt3);
/*      */   
/*      */   public abstract void deleteAllBuildTiles();
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\Structure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */