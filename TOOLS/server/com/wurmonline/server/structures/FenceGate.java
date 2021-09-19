/*      */ package com.wurmonline.server.structures;
/*      */ 
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Permissions;
/*      */ import com.wurmonline.server.players.PermissionsHistories;
/*      */ import com.wurmonline.server.players.PermissionsPlayerList;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.tutorial.MissionTriggers;
/*      */ import com.wurmonline.server.villages.NoSuchVillageException;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.VillageRole;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.VirtualZone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import java.io.IOException;
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
/*      */ public abstract class FenceGate
/*      */   extends Door
/*      */   implements MiscConstants
/*      */ {
/*   58 */   private static final Logger logger = Logger.getLogger(FenceGate.class.getName());
/*      */   final Fence fence;
/*   60 */   private Village village = null;
/*   61 */   int villageId = -1;
/*      */ 
/*      */ 
/*      */   
/*   65 */   int openTime = 0;
/*      */ 
/*      */ 
/*      */   
/*   69 */   int closeTime = 0;
/*   70 */   static final Map<Long, FenceGate> gates = new ConcurrentHashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   FenceGate(Fence aFence) {
/*   78 */     this.fence = aFence;
/*   79 */     gates.put(new Long(aFence.getId()), this);
/*      */     
/*      */     try {
/*   82 */       load();
/*      */     }
/*   84 */     catch (IOException iox) {
/*      */       
/*   86 */       logger.log(Level.WARNING, "Failed to load/save " + this.name + "," + aFence.getId(), iox);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getQualityLevel() {
/*   93 */     return this.fence.getCurrentQualityLevel();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setVillage(@Nullable Village vill) {
/*  102 */     this.village = vill;
/*      */     
/*  104 */     if (vill != null) {
/*  105 */       setIsManaged(true, (Player)null);
/*      */     
/*      */     }
/*  108 */     else if (this.villageId != -1) {
/*  109 */       setIsManaged(false, (Player)null);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Village getVillage() {
/*  119 */     return this.village;
/*      */   }
/*      */ 
/*      */   
/*      */   private Village getPermissionsVillage() {
/*  124 */     Village vill = getVillage();
/*  125 */     if (vill != null) {
/*  126 */       return vill;
/*      */     }
/*      */     
/*  129 */     long wid = getOwnerId();
/*  130 */     if (wid != -10L) {
/*  131 */       return Villages.getVillageForCreature(wid);
/*      */     }
/*  133 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getVillageId() {
/*  140 */     return this.villageId;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Village getManagedByVillage() {
/*  145 */     if (this.villageId >= 0) {
/*      */       
/*      */       try {
/*      */         
/*  149 */         return Villages.getVillage(this.villageId);
/*      */       }
/*  151 */       catch (NoSuchVillageException noSuchVillageException) {}
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  157 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Fence getFence() {
/*  166 */     return this.fence;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getOpenTime() {
/*  175 */     return this.openTime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getCloseTime() {
/*  184 */     return this.closeTime;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void addToTiles() {
/*  190 */     this.innerTile = this.fence.getTile();
/*  191 */     int tilex = this.innerTile.getTileX();
/*  192 */     int tiley = this.innerTile.getTileY();
/*  193 */     this.innerTile.addDoor(this);
/*  194 */     if (this.fence.isHorizontal()) {
/*      */       
/*  196 */       this.outerTile = Zones.getOrCreateTile(tilex, tiley - 1, this.fence.isOnSurface());
/*  197 */       this.outerTile.addDoor(this);
/*      */     }
/*      */     else {
/*      */       
/*  201 */       this.outerTile = Zones.getOrCreateTile(tilex - 1, tiley, this.fence.isOnSurface());
/*  202 */       this.outerTile.addDoor(this);
/*      */     } 
/*  204 */     calculateArea();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean canBeOpenedBy(Creature creature, boolean passedThroughDoor) {
/*  210 */     if (creature == null)
/*  211 */       return false; 
/*  212 */     if (MissionTriggers.isDoorOpen(creature, getWurmId(), 1))
/*  213 */       return true; 
/*  214 */     if (creature.getPower() > 1)
/*  215 */       return true; 
/*  216 */     if (creature.isKingdomGuard() || creature.isGhost())
/*  217 */       return true; 
/*  218 */     if (creature.getLeader() != null && 
/*  219 */       canBeOpenedBy(creature.getLeader(), false)) {
/*  220 */       return true;
/*      */     }
/*  222 */     if (!creature.canOpenDoors()) {
/*  223 */       return false;
/*      */     }
/*  225 */     if (this.village != null && this.village.isEnemy(creature)) {
/*  226 */       return canBeUnlockedBy(creature);
/*      */     }
/*  228 */     if (creature.isPlayer() && mayPass(creature)) {
/*  229 */       return true;
/*      */     }
/*  231 */     return canBeUnlockedBy(creature);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean canBeUnlockedBy(Creature creature) {
/*  237 */     if (creature.getPower() > 1) {
/*  238 */       return true;
/*      */     }
/*  240 */     if (this.lockCounter > 0) {
/*      */       
/*  242 */       creature.sendToLoggers("Lock counter=" + this.lockCounter);
/*  243 */       return true;
/*      */     } 
/*  245 */     if (this.lock == -10L) {
/*      */       
/*  247 */       creature.sendToLoggers("No lock ");
/*  248 */       return true;
/*      */     } 
/*  250 */     Item doorlock = null;
/*      */     
/*      */     try {
/*  253 */       doorlock = Items.getItem(this.lock);
/*      */     }
/*  255 */     catch (NoSuchItemException nsi) {
/*      */       
/*  257 */       logger.log(Level.INFO, "Lock has decayed? Id was " + this.lock);
/*      */       
/*  259 */       creature.sendToLoggers("Lock id " + this.lock + " has decayed?");
/*  260 */       return true;
/*      */     } 
/*      */     
/*  263 */     if (doorlock.isLocked()) {
/*      */       
/*  265 */       Item[] items = creature.getKeys();
/*  266 */       for (int x = 0; x < items.length; x++) {
/*      */         
/*  268 */         if (doorlock.isUnlockedBy(items[x].getWurmId())) {
/*      */           
/*  270 */           creature.sendToLoggers("I have key");
/*  271 */           return true;
/*      */         } 
/*      */       } 
/*  274 */       if (mayLock(creature))
/*      */       {
/*  276 */         creature.sendToLoggers("I have Lock Permission");
/*  277 */         return true;
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  282 */       creature.sendToLoggers("It's not locked");
/*  283 */       return true;
/*      */     } 
/*  285 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void creatureMoved(Creature creature, int diffTileX, int diffTileY) {
/*  291 */     if (covers(creature.getStatus().getPositionX(), creature.getStatus().getPositionY(), creature.getPositionZ(), creature
/*  292 */         .getFloorLevel(), creature.followsGround())) {
/*      */       
/*  294 */       addCreature(creature);
/*      */     }
/*      */     else {
/*      */       
/*  298 */       removeCreature(creature);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void removeCreature(Creature creature) {
/*  307 */     if (this.creatures != null) {
/*      */       
/*  309 */       if (this.creatures.contains(creature)) {
/*      */         
/*  311 */         this.creatures.remove(creature);
/*  312 */         if (isOpen() && !creature.isGhost()) {
/*      */           
/*  314 */           creature.getCommunicator().sendCloseFence(this.fence, false, true);
/*  315 */           boolean close = true;
/*  316 */           for (Iterator<Creature> it = this.creatures.iterator(); it.hasNext(); ) {
/*      */             
/*  318 */             Creature checked = it.next();
/*  319 */             if (canBeOpenedBy(checked, false))
/*  320 */               close = false; 
/*      */           } 
/*  322 */           if (close) {
/*      */             
/*  324 */             close();
/*  325 */             if (this.watchers != null && creature.isVisible())
/*      */             {
/*  327 */               for (Iterator<VirtualZone> iterator = this.watchers.iterator(); iterator.hasNext(); ) {
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*  332 */                 VirtualZone z = iterator.next();
/*  333 */                 if (z.getWatcher() != creature)
/*      */                 {
/*  335 */                   z.closeFence(this.fence, false, false);
/*      */                 }
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*  342 */       if (this.creatures.size() == 0) {
/*  343 */         this.creatures = null;
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public final boolean containsCreature(Creature creature) {
/*  349 */     if (this.creatures == null) {
/*  350 */       return false;
/*      */     }
/*  352 */     return this.creatures.contains(creature);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateDoor(Creature creature, Item key, boolean removedKey) {
/*  358 */     boolean isOpenToCreature = canBeOpenedBy(creature, false);
/*  359 */     if (removedKey) {
/*      */       
/*  361 */       if (this.creatures != null)
/*      */       {
/*  363 */         if (this.creatures.contains(creature))
/*      */         {
/*  365 */           if (!isOpenToCreature)
/*      */           {
/*  367 */             if (canBeUnlockedByKey(key)) {
/*      */               
/*  369 */               creature.getCommunicator().sendCloseFence(this.fence, false, true);
/*  370 */               if (isOpen()) {
/*      */                 
/*  372 */                 boolean close = true;
/*  373 */                 for (Iterator<Creature> it = this.creatures.iterator(); it.hasNext(); ) {
/*      */                   
/*  375 */                   Creature checked = it.next();
/*  376 */                   if (canBeOpenedBy(checked, false))
/*  377 */                     close = false; 
/*      */                 } 
/*  379 */                 if (close && creature.isVisible() && !creature.isGhost()) {
/*      */ 
/*      */                   
/*  382 */                   close();
/*  383 */                   if (this.watchers != null && creature.isVisible())
/*      */                   {
/*  385 */                     for (Iterator<VirtualZone> iterator = this.watchers.iterator(); iterator.hasNext(); ) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                       
/*  392 */                       VirtualZone z = iterator.next();
/*  393 */                       if (z.getWatcher() != creature)
/*      */                       {
/*  395 */                         z.closeFence(this.fence, false, false);
/*      */                       }
/*      */                     } 
/*      */                   }
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           }
/*      */         }
/*  404 */         if (this.creatures.size() == 0) {
/*  405 */           this.creatures = null;
/*      */         }
/*      */       }
/*      */     
/*      */     }
/*  410 */     else if (this.creatures != null) {
/*      */       
/*  412 */       if (this.creatures.contains(creature))
/*      */       {
/*  414 */         if (isOpenToCreature)
/*      */         {
/*  416 */           if (canBeUnlockedByKey(key))
/*      */           {
/*  418 */             if (!isOpen() && creature.isVisible() && !creature.isGhost()) {
/*      */               
/*  420 */               if (this.watchers != null && creature.isVisible())
/*      */               {
/*  422 */                 for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext(); ) {
/*      */                   
/*  424 */                   VirtualZone z = it.next();
/*  425 */                   if (z.getWatcher() != creature)
/*      */                   {
/*  427 */                     z.openFence(this.fence, false, false);
/*      */                   }
/*      */                 } 
/*      */               }
/*  431 */               open();
/*  432 */               creature.getCommunicator().sendOpenFence(this.fence, true, true);
/*      */             } 
/*      */           }
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean addCreature(Creature creature) {
/*  444 */     if (this.creatures == null)
/*  445 */       this.creatures = new HashSet<>(); 
/*  446 */     if (!this.creatures.contains(creature)) {
/*      */       
/*  448 */       this.creatures.add(creature);
/*  449 */       if (canBeOpenedBy(creature, false) && !creature.isGhost())
/*      */       {
/*  451 */         if (!isOpen()) {
/*      */           
/*  453 */           if (this.watchers != null && creature.isVisible())
/*      */           {
/*  455 */             for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext(); ) {
/*      */               
/*  457 */               VirtualZone z = it.next();
/*  458 */               if (z.getWatcher() != creature)
/*      */               {
/*  460 */                 z.openFence(this.fence, false, false);
/*      */               }
/*      */             } 
/*      */           }
/*  464 */           open();
/*      */           
/*  466 */           if (creature.getEnemyPresense() > 0)
/*      */           {
/*  468 */             if (getVillage() == null)
/*  469 */               setLockCounter((short)120); 
/*      */           }
/*  471 */           creature.getCommunicator().sendOpenFence(this.fence, true, true);
/*      */         }
/*      */         else {
/*      */           
/*  475 */           creature.getCommunicator().sendOpenFence(this.fence, true, true);
/*      */         } 
/*      */       }
/*  478 */       return true;
/*      */     } 
/*  480 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean keyFits(long keyId) throws NoSuchLockException {
/*  486 */     if (this.lock == -10L) {
/*  487 */       throw new NoSuchLockException("No ID");
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/*  492 */       Item doorlock = Items.getItem(this.lock);
/*  493 */       return doorlock.isUnlockedBy(keyId);
/*      */     }
/*  495 */     catch (NoSuchItemException nsi) {
/*      */       
/*  497 */       logger.log(Level.INFO, "Lock has decayed? Id was " + this.lock);
/*      */ 
/*      */       
/*  500 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isOpenTime() {
/*  506 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final void close() {
/*  512 */     this.open = false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final void open() {
/*  518 */     this.open = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void removeFromVillage() {
/*  523 */     if (this.village != null) {
/*  524 */       this.village.removeGate(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public static final FenceGate getFenceGate(long id) {
/*  529 */     Long lid = new Long(id);
/*  530 */     FenceGate toReturn = gates.get(lid);
/*  531 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final FenceGate[] getAllGates() {
/*  536 */     return (FenceGate[])gates.values().toArray((Object[])new FenceGate[gates.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final FenceGate[] getManagedGatesFor(Player player, int villageId, boolean includeAll) {
/*  547 */     Set<FenceGate> fenceGates = new HashSet<>();
/*  548 */     for (FenceGate gate : gates.values()) {
/*      */       
/*  550 */       if ((gate.canManage((Creature)player) || (villageId >= 0 && gate.getVillageId() == villageId)) && (
/*  551 */         includeAll || gate.hasLock()))
/*  552 */         fenceGates.add(gate); 
/*      */     } 
/*  554 */     return fenceGates.<FenceGate>toArray(new FenceGate[fenceGates.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final FenceGate[] getOwnedGatesFor(Player player) {
/*  559 */     Set<FenceGate> fenceGates = new HashSet<>();
/*  560 */     for (FenceGate gate : gates.values()) {
/*      */       
/*  562 */       if (gate.isOwner((Creature)player) || gate.isActualOwner(player.getWurmId()))
/*  563 */         fenceGates.add(gate); 
/*      */     } 
/*  565 */     return fenceGates.<FenceGate>toArray(new FenceGate[fenceGates.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void unManageGatesFor(int villageId) {
/*  575 */     for (FenceGate gate : getAllGates()) {
/*      */       
/*  577 */       if (gate.getVillageId() == villageId)
/*      */       {
/*  579 */         gate.setIsManaged(false, (Player)null);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final Village getOwnerVillage() {
/*  586 */     return Villages.getVillageForCreature(getOwnerId());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean covers(float x, float y, float posz, int floorLevel, boolean followGround) {
/*  595 */     return (((this.fence != null && this.fence.isWithinZ(posz + 1.0F, posz, followGround)) || (isTransition() && floorLevel <= 0)) && x >= this.startx && x <= this.endx && y >= this.starty && y <= this.endy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getFloorLevel() {
/*  604 */     return this.fence.getFloorLevel();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void setOpenTime(int paramInt);
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void setCloseTime(int paramInt);
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void setLock(long paramLong);
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void save() throws IOException;
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void load() throws IOException;
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void delete();
/*      */ 
/*      */   
/*      */   public final long getOwnerId() {
/*  633 */     if (this.lock != -10L) {
/*      */       
/*      */       try {
/*      */         
/*  637 */         Item doorlock = Items.getItem(this.lock);
/*      */         
/*  639 */         return doorlock.getLastOwnerId();
/*      */       }
/*  641 */       catch (NoSuchItemException nsi) {
/*      */         
/*  643 */         return -10L;
/*      */       } 
/*      */     }
/*  646 */     return -10L;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getWurmId() {
/*  652 */     return this.fence.getId();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setObjectName(String aNewName, Creature aCreature) {
/*  663 */     setName(aNewName);
/*      */     
/*  665 */     this.outerTile.updateFence(getFence());
/*  666 */     return true;
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
/*  677 */     long wid = getOwnerId();
/*  678 */     if (this.lock != -10L)
/*      */     {
/*  680 */       return (wid == playerId);
/*      */     }
/*  682 */     return false;
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
/*  693 */     return isOwner(creature.getWurmId());
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
/*  705 */     if (isManaged()) {
/*      */       
/*  707 */       Village vill = getManagedByVillage();
/*  708 */       if (vill != null)
/*  709 */         return vill.isMayor(playerId); 
/*      */     } 
/*  711 */     return isActualOwner(playerId);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canChangeOwner(Creature creature) {
/*  717 */     return (hasLock() && (creature.getPower() > 1 || isActualOwner(creature.getWurmId())));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getWarning() {
/*  723 */     if (this.lock == -10L) {
/*  724 */       return "NEEDS TO HAVE A LOCK FOR PERMISSIONS TO WORK";
/*      */     }
/*      */     
/*  727 */     if (!isLocked())
/*  728 */       return "NEEDS TO BE LOCKED OTHERWISE EVERYONE CAN PASS"; 
/*  729 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public PermissionsPlayerList getPermissionsPlayerList() {
/*  735 */     return DoorSettings.getPermissionsPlayerList(getWurmId());
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
/*  747 */     return isLocked();
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
/*  759 */     return (hasLock() && mayManage(creature));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canManage(Creature creature) {
/*  770 */     if (DoorSettings.isExcluded(this, creature)) {
/*  771 */       return false;
/*      */     }
/*  773 */     if (DoorSettings.canManage(this, creature))
/*  774 */       return true; 
/*  775 */     if (creature.getCitizenVillage() == null) {
/*  776 */       return false;
/*      */     }
/*  778 */     Village vill = getManagedByVillage();
/*  779 */     if (vill == null)
/*  780 */       return false; 
/*  781 */     if (!vill.isCitizen(creature))
/*  782 */       return false; 
/*  783 */     return vill.isActionAllowed((short)667, creature);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayManage(Creature creature) {
/*  793 */     if (creature.getPower() > 1)
/*  794 */       return true; 
/*  795 */     return canManage(creature);
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
/*  806 */     if (this.fence == null)
/*  807 */       return false; 
/*  808 */     return this.fence.getSettings().hasPermission(Permissions.Allow.SETTLEMENT_MAY_MANAGE.getBit());
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
/*  821 */     if (player.getPower() > 1)
/*  822 */       return true; 
/*  823 */     if (isManaged()) {
/*      */       
/*  825 */       Village vil = getVillage();
/*  826 */       if (vil != null)
/*  827 */         return false; 
/*      */     } 
/*  829 */     return isOwner((Creature)player);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsManaged(boolean newIsManaged, @Nullable Player player) {
/*  840 */     if (this.fence != null) {
/*      */       
/*  842 */       int oldId = this.villageId;
/*  843 */       if (newIsManaged) {
/*      */ 
/*      */         
/*  846 */         Village v = getVillage();
/*  847 */         if (v != null) {
/*      */           
/*  849 */           setVillageId(v.getId());
/*      */         
/*      */         }
/*      */         else {
/*      */           
/*  854 */           Village cv = getOwnerVillage();
/*  855 */           if (cv != null) {
/*  856 */             setVillageId(cv.getId());
/*      */           } else {
/*      */             
/*      */             return;
/*      */           } 
/*      */         } 
/*      */       } else {
/*  863 */         setVillageId(-1);
/*      */       } 
/*  865 */       if (oldId != this.villageId && DoorSettings.exists(getWurmId())) {
/*      */         
/*  867 */         DoorSettings.remove(getWurmId());
/*  868 */         PermissionsHistories.addHistoryEntry(getWurmId(), System.currentTimeMillis(), -10L, "Auto", "Cleared Permissions");
/*      */       } 
/*      */ 
/*      */       
/*  872 */       this.fence.getSettings().setPermissionBit(Permissions.Allow.SETTLEMENT_MAY_MANAGE.getBit(), newIsManaged);
/*  873 */       this.fence.savePermissions();
/*      */       
/*      */       try {
/*  876 */         save();
/*      */       }
/*  878 */       catch (IOException e) {
/*      */ 
/*      */         
/*  881 */         logger.log(Level.WARNING, e.getMessage(), e);
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
/*      */   public String mayManageText(Player aPlayer) {
/*  895 */     String vName = "";
/*  896 */     Village vill = getManagedByVillage();
/*  897 */     if (vill != null) {
/*  898 */       vName = vill.getName();
/*      */     }
/*      */     else {
/*      */       
/*  902 */       vill = getVillage();
/*  903 */       if (vill != null) {
/*  904 */         vName = vill.getName();
/*      */       }
/*      */       else {
/*      */         
/*  908 */         vill = Villages.getVillageForCreature(getOwnerId());
/*  909 */         if (vill != null)
/*  910 */           vName = vill.getName(); 
/*      */       } 
/*      */     } 
/*  913 */     if (vName.length() > 0)
/*  914 */       return "Settlement \"" + vName + "\" may manage"; 
/*  915 */     return vName;
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
/*  926 */     return "";
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
/*  937 */     return "By selecting this you are giving full control to settlement.";
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
/*  948 */     return "Are you positive you want to give your control away?";
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
/*  959 */     return "By doing this you are reverting the control to owner";
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
/*  970 */     return "Are you sure you want them to have control?";
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
/*  981 */     String sName = "";
/*  982 */     Village vill = getPermissionsVillage();
/*  983 */     if (vill != null)
/*  984 */       sName = vill.getName(); 
/*  985 */     if (sName.length() == 0)
/*  986 */       return sName; 
/*  987 */     return "Citizens of \"" + sName + "\"";
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
/*  998 */     String aName = "";
/*  999 */     Village vill = getPermissionsVillage();
/* 1000 */     if (vill != null)
/* 1001 */       aName = vill.getAllianceName(); 
/* 1002 */     if (aName.length() == 0)
/* 1003 */       return aName; 
/* 1004 */     return "Alliance of \"" + aName + "\"";
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
/* 1015 */     byte kingdom = 0;
/* 1016 */     Village vill = getPermissionsVillage();
/* 1017 */     if (vill != null) {
/* 1018 */       kingdom = vill.kingdom;
/*      */     } else {
/* 1020 */       kingdom = Players.getInstance().getKingdomForPlayer(getOwnerId());
/*      */     } 
/* 1022 */     return "Kingdom of \"" + Kingdoms.getNameFor(kingdom) + "\"";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addDefaultCitizenPermissions() {
/* 1028 */     if (!getPermissionsPlayerList().exists(-30L)) {
/*      */ 
/*      */       
/* 1031 */       int value = DoorSettings.DoorPermissions.PASS.getValue();
/* 1032 */       addGuest(-30L, value);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCitizen(Creature creature) {
/* 1039 */     Village vill = getManagedByVillage();
/* 1040 */     if (vill == null) {
/* 1041 */       vill = getOwnerVillage();
/*      */     }
/* 1043 */     if (vill != null)
/* 1044 */       return vill.isCitizen(creature); 
/* 1045 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAllied(Creature creature) {
/* 1051 */     Village vill = getManagedByVillage();
/* 1052 */     if (vill == null) {
/* 1053 */       vill = getOwnerVillage();
/*      */     }
/* 1055 */     if (vill != null)
/* 1056 */       return vill.isAlly(creature); 
/* 1057 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSameKingdom(Creature creature) {
/* 1065 */     Village vill = getPermissionsVillage();
/* 1066 */     if (vill != null) {
/* 1067 */       return (vill.kingdom == creature.getKingdomId());
/*      */     }
/* 1069 */     return (Players.getInstance().getKingdomForPlayer(getOwnerId()) == creature.getKingdomId());
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
/* 1080 */     return isGuest(creature.getWurmId());
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
/* 1092 */     return DoorSettings.isGuest(this, playerId);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addGuest(long guestId, int aSettings) {
/* 1098 */     DoorSettings.addPlayer(getWurmId(), guestId, aSettings);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeGuest(long guestId) {
/* 1104 */     DoorSettings.removePlayer(getWurmId(), guestId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayPass(Creature creature) {
/* 1115 */     if (!isLocked())
/* 1116 */       return true; 
/* 1117 */     if (DoorSettings.exists(getWurmId())) {
/*      */       
/* 1119 */       if (DoorSettings.isExcluded(this, creature))
/* 1120 */         return false; 
/* 1121 */       if (DoorSettings.mayPass(this, creature)) {
/* 1122 */         return true;
/*      */       }
/*      */     } 
/* 1125 */     if (isManaged()) {
/*      */       
/* 1127 */       Village vill = getManagedByVillage();
/* 1128 */       VillageRole vr = (vill == null) ? null : vill.getRoleFor(creature);
/* 1129 */       return (vr != null && vr.mayPassGates());
/*      */     } 
/* 1131 */     return isOwner(creature);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayAttachLock(Creature creature) {
/* 1141 */     if (hasLock()) {
/*      */ 
/*      */       
/* 1144 */       if (this.village != null) {
/*      */         
/* 1146 */         VillageRole vr = this.village.getRoleFor(creature);
/* 1147 */         return (vr != null && vr.mayAttachLock());
/*      */       } 
/* 1149 */       return isOwner(creature);
/*      */     } 
/* 1151 */     return true;
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
/*      */   public final boolean mayLock(Creature creature) {
/* 1164 */     if (DoorSettings.exists(getWurmId())) {
/*      */       
/* 1166 */       if (DoorSettings.isExcluded(this, creature))
/* 1167 */         return false; 
/* 1168 */       if (DoorSettings.mayLock(this, creature)) {
/* 1169 */         return true;
/*      */       }
/*      */     } 
/* 1172 */     if (isManaged()) {
/*      */       
/* 1174 */       Village vill = getManagedByVillage();
/* 1175 */       VillageRole vr = (vill == null) ? null : vill.getRoleFor(creature);
/* 1176 */       return (vr != null && vr.mayAttachLock());
/*      */     } 
/* 1178 */     return isOwner(creature);
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
/* 1189 */     if (this.fence == null)
/* 1190 */       return "No Fence!"; 
/* 1191 */     return this.fence.getTypeName();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNotLockpickable() {
/* 1197 */     return this.fence.isNotLockpickable();
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
/*      */   public boolean setNewOwner(long playerId) {
/*      */     try {
/* 1210 */       if (!isManaged() && DoorSettings.exists(getWurmId())) {
/*      */         
/* 1212 */         DoorSettings.remove(getWurmId());
/* 1213 */         PermissionsHistories.addHistoryEntry(getWurmId(), System.currentTimeMillis(), -10L, "Auto", "Cleared Permissions");
/*      */       } 
/*      */       
/* 1216 */       Item theLock = getLock();
/* 1217 */       logger.info("Overwritting owner (" + theLock.getLastOwnerId() + ") of lock " + theLock.getWurmId() + " to " + playerId);
/*      */       
/* 1219 */       theLock.setLastOwnerId(playerId);
/* 1220 */       return true;
/*      */     }
/* 1222 */     catch (NoSuchLockException noSuchLockException) {
/*      */ 
/*      */ 
/*      */       
/* 1226 */       return false;
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
/*      */   public String getOwnerName() {
/*      */     try {
/* 1239 */       Item theLock = getLock();
/* 1240 */       return PlayerInfoFactory.getPlayerName(theLock.getLastOwnerId());
/*      */     }
/* 1242 */     catch (NoSuchLockException noSuchLockException) {
/*      */ 
/*      */ 
/*      */       
/* 1246 */       return "";
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean maySeeHistory(Creature creature) {
/* 1256 */     if (creature.getPower() > 1)
/* 1257 */       return true; 
/* 1258 */     return isOwner(creature);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean convertToNewPermissions() {
/* 1263 */     boolean didConvert = false;
/* 1264 */     if (this.village != null) {
/*      */       
/* 1266 */       setIsManaged(true, (Player)null);
/* 1267 */       didConvert = true;
/*      */     } 
/* 1269 */     if (didConvert)
/* 1270 */       this.fence.savePermissions(); 
/* 1271 */     return didConvert;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean fixForNewPermissions() {
/* 1276 */     boolean didConvert = false;
/* 1277 */     if (this.village != null) {
/*      */       
/* 1279 */       addDefaultCitizenPermissions();
/* 1280 */       didConvert = true;
/*      */     } 
/* 1282 */     return didConvert;
/*      */   }
/*      */   
/*      */   public abstract void setVillageId(int paramInt);
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\FenceGate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */