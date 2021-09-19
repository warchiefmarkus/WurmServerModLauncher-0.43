/*      */ package com.wurmonline.server.structures;
/*      */ 
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.creatures.AnimalSettings;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.players.Permissions;
/*      */ import com.wurmonline.server.players.PermissionsHistories;
/*      */ import com.wurmonline.server.players.PermissionsPlayerList;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.tutorial.MissionTriggers;
/*      */ import com.wurmonline.server.zones.NoSuchTileException;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VirtualZone;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.SoundNames;
/*      */ import java.io.IOException;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
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
/*      */ public abstract class Door
/*      */   implements MiscConstants, SoundNames, TimeConstants, PermissionsPlayerList.ISettings
/*      */ {
/*      */   Wall wall;
/*   55 */   private static Logger logger = Logger.getLogger(Door.class.getName());
/*      */ 
/*      */ 
/*      */   
/*   59 */   long lock = -10L;
/*   60 */   long structure = -10L;
/*      */   
/*      */   boolean open = false;
/*      */   
/*      */   VolaTile outerTile;
/*      */   
/*      */   VolaTile innerTile;
/*      */   protected int startx;
/*      */   protected int starty;
/*      */   protected int endx;
/*      */   protected int endy;
/*      */   Set<Creature> creatures;
/*      */   Set<VirtualZone> watchers;
/*   73 */   short lockCounter = 0;
/*      */ 
/*      */   
/*      */   boolean preAlertLockedStatus = false;
/*      */   
/*   78 */   String name = "";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Door() {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Door(Wall _wall) {
/*   89 */     this.wall = _wall;
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
/*      */   public final void setLockCounter(short newcounter) {
/*  103 */     if (this.lockCounter <= 0 || newcounter <= 0)
/*  104 */       playLockSound(); 
/*  105 */     if (newcounter > this.lockCounter) {
/*  106 */       this.lockCounter = newcounter;
/*      */     }
/*  108 */     if (this.lockCounter > 0)
/*      */     {
/*  110 */       if (getInnerTile() != null) {
/*  111 */         getInnerTile().addUnlockedDoor(this);
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
/*      */   public short getLockCounter() {
/*  124 */     return this.lockCounter;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getLockCounterTime() {
/*  129 */     int m = this.lockCounter / 120;
/*  130 */     int s = this.lockCounter % 120 / 2;
/*  131 */     if (m > 0)
/*  132 */       return m + " minutes and " + s + " seconds."; 
/*  133 */     return s + " seconds.";
/*      */   }
/*      */ 
/*      */   
/*      */   private void playLockSound() {
/*  138 */     if (this.innerTile != null) {
/*      */       
/*  140 */       SoundPlayer.playSound("sound.object.lockunlock", this.innerTile.tilex, this.innerTile.tiley, this.innerTile.isOnSurface(), 1.0F);
/*  141 */       Server.getInstance().broadCastMessage("A loud *click* is heard.", this.innerTile.tilex, this.innerTile.tiley, this.innerTile
/*  142 */           .isOnSurface(), 5);
/*      */     }
/*  144 */     else if (this.outerTile != null) {
/*      */       
/*  146 */       SoundPlayer.playSound("sound.object.lockunlock", this.outerTile.tilex, this.outerTile.tiley, this.outerTile.isOnSurface(), 1.0F);
/*  147 */       Server.getInstance().broadCastMessage("A loud *click* is heard.", this.outerTile.tilex, this.outerTile.tiley, this.outerTile
/*  148 */           .isOnSurface(), 5);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setStructureId(long structureId) {
/*  154 */     this.structure = structureId;
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getStructureId() {
/*  159 */     return this.structure;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void addWatcher(VirtualZone watcher) {
/*  164 */     if (this.watchers == null)
/*  165 */       this.watchers = new HashSet<>(); 
/*  166 */     if (!this.watchers.contains(watcher)) {
/*  167 */       this.watchers.add(watcher);
/*      */     }
/*      */   }
/*      */   
/*      */   public final void removeWatcher(VirtualZone watcher) {
/*  172 */     if (this.watchers != null)
/*      */     {
/*  174 */       if (this.watchers.contains(watcher)) {
/*  175 */         this.watchers.remove(watcher);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getName() {
/*  185 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final VolaTile getOuterTile() {
/*  194 */     return this.outerTile;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final VolaTile getInnerTile() {
/*  203 */     return this.innerTile;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTileX() {
/*  208 */     if (this.outerTile != null)
/*  209 */       return this.outerTile.tilex; 
/*  210 */     if (this.innerTile != null)
/*  211 */       return this.innerTile.tilex; 
/*  212 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTileY() {
/*  217 */     if (this.outerTile != null)
/*  218 */       return this.outerTile.tiley; 
/*  219 */     if (this.innerTile != null)
/*  220 */       return this.innerTile.tiley; 
/*  221 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Wall getWall() throws NoSuchWallException {
/*  231 */     if (this.wall == null)
/*  232 */       throw new NoSuchWallException("null inner wall for tilex=" + getTileX() + ", tiley=" + getTileY() + " structure=" + 
/*  233 */           getStructureId()); 
/*  234 */     return this.wall;
/*      */   }
/*      */ 
/*      */   
/*      */   final void calculateArea() {
/*  239 */     int innerTileStartX = this.innerTile.getTileX();
/*  240 */     int outerTileStartX = this.outerTile.getTileX();
/*  241 */     int innerTileStartY = this.innerTile.getTileY();
/*  242 */     int outerTileStartY = this.outerTile.getTileY();
/*      */     
/*  244 */     if (innerTileStartX == outerTileStartX) {
/*      */       
/*  246 */       this.starty = (Math.min(innerTileStartY, outerTileStartY) << 2) + 2;
/*  247 */       this.endy = (Math.max(innerTileStartY, outerTileStartY) << 2) + 2;
/*  248 */       this.startx = innerTileStartX << 2;
/*  249 */       this.endx = innerTileStartX + 1 << 2;
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  254 */       this.starty = innerTileStartY << 2;
/*  255 */       this.endy = innerTileStartY + 1 << 2;
/*  256 */       this.startx = (Math.min(innerTileStartX, outerTileStartX) << 2) + 2;
/*  257 */       this.endx = (Math.max(innerTileStartX, outerTileStartX) << 2) + 2;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isTransition() {
/*  263 */     return (this.innerTile.isTransition() || this.outerTile.isTransition());
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean covers(float x, float y, float posz, int floorLevel, boolean followGround) {
/*  268 */     return (((this.wall != null && this.wall.isWithinZ(posz + 1.0F, posz, followGround)) || (isTransition() && floorLevel <= 0)) && x >= this.startx && x <= this.endx && y >= this.starty && y <= this.endy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addToTiles() {
/*      */     try {
/*  278 */       Structure struct = Structures.getStructure(this.structure);
/*  279 */       this.outerTile = this.wall.getOrCreateOuterTile(struct.isSurfaced());
/*  280 */       this.outerTile.addDoor(this);
/*  281 */       this.innerTile = this.wall.getOrCreateInnerTile(struct.isSurfaced());
/*  282 */       this.innerTile.addDoor(this);
/*  283 */       calculateArea();
/*      */     }
/*  285 */     catch (NoSuchStructureException nss) {
/*      */       
/*  287 */       logger.log(Level.WARNING, "No such structure? structure: " + this.structure, (Throwable)nss);
/*      */     }
/*  289 */     catch (NoSuchZoneException nsz) {
/*      */       
/*  291 */       logger.log(Level.WARNING, "No such zone - wall: " + this.wall + " - " + nsz.getMessage(), (Throwable)nsz);
/*      */     }
/*  293 */     catch (NoSuchTileException nst) {
/*      */       
/*  295 */       logger.log(Level.WARNING, "No such tile - wall: " + this.wall + " - " + nst.getMessage(), (Throwable)nst);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void removeFromTiles() {
/*  301 */     if (this.outerTile != null)
/*  302 */       this.outerTile.removeDoor(this); 
/*  303 */     if (this.innerTile != null) {
/*  304 */       this.innerTile.removeDoor(this);
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
/*      */   public boolean canBeOpenedBy(Creature creature, boolean wentThroughDoor) {
/*  316 */     if (creature == null) {
/*  317 */       return false;
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
/*  329 */     if (creature.isKingdomGuard() || creature.isGhost())
/*  330 */       return true; 
/*  331 */     if (MissionTriggers.isDoorOpen(creature, this.wall.getId(), 1))
/*  332 */       return true; 
/*  333 */     if (creature.getPower() > 0)
/*  334 */       return true; 
/*  335 */     if (creature.getLeader() != null && 
/*  336 */       canBeOpenedBy(creature.getLeader(), false)) {
/*  337 */       return true;
/*      */     }
/*  339 */     if (!creature.canOpenDoors())
/*      */     {
/*  341 */       return false;
/*      */     }
/*  343 */     return canBeUnlockedBy(creature);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBeUnlockedByKey(Item key) {
/*  348 */     Item doorlock = null;
/*      */     
/*      */     try {
/*  351 */       doorlock = Items.getItem(this.lock);
/*      */     }
/*  353 */     catch (NoSuchItemException nsi) {
/*      */       
/*  355 */       return false;
/*      */     } 
/*  357 */     if (doorlock.isLocked()) {
/*      */       
/*  359 */       if (doorlock.isUnlockedBy(key.getWurmId()))
/*  360 */         return true; 
/*  361 */       return false;
/*      */     } 
/*      */     
/*  364 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBeUnlockedBy(Creature creature) {
/*  369 */     return mayPass(creature);
/*      */   }
/*      */ 
/*      */   
/*      */   public void creatureMoved(Creature creature, int diffTileX, int diffTileY) {
/*  374 */     if (covers(creature.getStatus().getPositionX(), creature.getStatus().getPositionY(), creature.getPositionZ(), creature
/*  375 */         .getFloorLevel(), creature.followsGround())) {
/*      */       
/*  377 */       if (!addCreature(creature))
/*      */       {
/*  379 */         if (diffTileX != 0 || diffTileY != 0)
/*      */         {
/*  381 */           if (!canBeOpenedBy(creature, true)) {
/*      */ 
/*      */             
/*      */             try {
/*  385 */               int tilex = creature.getTileX();
/*  386 */               int tiley = creature.getTileY();
/*      */               
/*  388 */               VolaTile tile = Zones.getZone(tilex, tiley, creature.isOnSurface()).getTileOrNull(tilex, tiley);
/*  389 */               if (tile != null) {
/*      */                 
/*  391 */                 if (tile == this.innerTile)
/*      */                 {
/*  393 */                   int oldTileX = tilex - diffTileX;
/*  394 */                   int oldTileY = tiley - diffTileY;
/*      */ 
/*      */                   
/*  397 */                   if (creature instanceof Player)
/*      */                   {
/*  399 */                     creature.getCommunicator().sendAlertServerMessage("You cannot enter that building.");
/*  400 */                     logger.log(Level.WARNING, creature.getName() + " a cheater? Passed through door at " + creature
/*  401 */                         .getStatus().getPositionX() + ", " + creature
/*  402 */                         .getStatus().getPositionY() + ", z=" + creature.getPositionZ() + ", minZ=" + this.wall
/*  403 */                         .getMinZ());
/*  404 */                     creature.setTeleportPoints((short)oldTileX, (short)oldTileY, creature
/*  405 */                         .getLayer(), creature.getFloorLevel());
/*  406 */                     creature.startTeleporting();
/*  407 */                     creature.getCommunicator().sendTeleport(false);
/*      */                   }
/*      */                 
/*      */                 }
/*      */               
/*      */               }
/*      */               else {
/*      */                 
/*  415 */                 logger.log(Level.WARNING, "A door on no tile at " + creature.getStatus().getPositionX() + ", " + creature
/*  416 */                     .getStatus().getPositionY() + ", structure: " + this.structure);
/*      */               } 
/*  418 */             } catch (NoSuchZoneException nsz) {
/*      */               
/*  420 */               logger.log(Level.WARNING, "A door in no zone at " + creature.getStatus().getPositionX() + ", " + creature
/*  421 */                   .getStatus().getPositionY() + ", structure: " + this.structure + " - " + nsz);
/*      */             }
/*      */           
/*  424 */           } else if (this.structure != -10L) {
/*      */             
/*  426 */             if (creature.isPlayer()) {
/*      */               
/*  428 */               int tilex = creature.getTileX();
/*  429 */               int tiley = creature.getTileY();
/*  430 */               VolaTile tile = Zones.getTileOrNull(tilex, tiley, creature.isOnSurface());
/*  431 */               if (tile == this.innerTile) {
/*      */ 
/*      */                 
/*  434 */                 if (creature.getEnemyPresense() > 0)
/*      */                 {
/*  436 */                   if (tile.getVillage() == null) {
/*  437 */                     setLockCounter((short)120);
/*      */                   }
/*      */                 }
/*  440 */               } else if (tile == this.outerTile) {
/*      */                 
/*  442 */                 int oldTileX = tilex - diffTileX;
/*  443 */                 int oldTileY = tiley - diffTileY;
/*      */ 
/*      */                 
/*      */                 try {
/*  447 */                   VolaTile oldtile = Zones.getZone(oldTileX, oldTileY, creature.isOnSurface()).getTileOrNull(oldTileX, oldTileY);
/*  448 */                   if (oldtile != null && oldtile == this.innerTile)
/*      */                   {
/*      */                     
/*  451 */                     if (creature.getEnemyPresense() > 0)
/*      */                     {
/*  453 */                       if (oldtile.getVillage() == null) {
/*  454 */                         setLockCounter((short)120);
/*      */                       }
/*      */                     }
/*      */                   }
/*  458 */                 } catch (NoSuchZoneException nsz) {
/*      */                   
/*  460 */                   logger.log(Level.WARNING, "A door in no zone at " + creature.getStatus().getPositionX() + ", " + creature
/*      */                       
/*  462 */                       .getStatus().getPositionY() + ", structure: " + this.structure + " - " + nsz);
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         }
/*      */       }
/*      */     } else {
/*      */       
/*  471 */       removeCreature(creature);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void updateDoor(Creature creature, Item key, boolean removedKey) {
/*  476 */     boolean isOpenToCreature = canBeOpenedBy(creature, false);
/*  477 */     if (removedKey) {
/*      */       
/*  479 */       if (this.creatures != null)
/*      */       {
/*  481 */         if (this.creatures.contains(creature))
/*      */         {
/*  483 */           if (!isOpenToCreature)
/*      */           {
/*  485 */             if (canBeUnlockedByKey(key))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  491 */               if (isOpen()) {
/*      */                 
/*  493 */                 boolean close = true;
/*  494 */                 for (Creature checked : this.creatures) {
/*      */                   
/*  496 */                   if (canBeOpenedBy(checked, false))
/*  497 */                     close = false; 
/*      */                 } 
/*  499 */                 if (close && creature.isVisible() && !creature.isGhost())
/*  500 */                   close(); 
/*      */               } 
/*      */             }
/*      */           }
/*      */         }
/*  505 */         if (this.creatures.size() == 0) {
/*  506 */           this.creatures = null;
/*      */         }
/*      */       }
/*      */     
/*      */     }
/*  511 */     else if (this.creatures != null) {
/*      */       
/*  513 */       if (this.creatures.contains(creature))
/*      */       {
/*  515 */         if (isOpenToCreature)
/*      */         {
/*  517 */           if (canBeUnlockedByKey(key)) {
/*      */             
/*  519 */             if (!isOpen() && creature.isVisible() && !creature.isGhost())
/*  520 */               open(); 
/*  521 */             creature.getCommunicator().sendPassable(true, this);
/*      */           } 
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeCreature(Creature creature) {
/*  531 */     if (this.creatures != null) {
/*      */       
/*  533 */       if (this.creatures.contains(creature)) {
/*      */ 
/*      */ 
/*      */         
/*  537 */         this.creatures.remove(creature);
/*  538 */         creature.setCurrentDoor(null);
/*  539 */         creature.getCommunicator().sendPassable(false, this);
/*  540 */         if (isOpen()) {
/*      */           
/*  542 */           boolean close = true;
/*  543 */           for (Creature checked : this.creatures) {
/*      */             
/*  545 */             if (canBeOpenedBy(checked, false))
/*  546 */               close = false; 
/*      */           } 
/*  548 */           if (close && creature.isVisible() && !creature.isGhost())
/*  549 */             close(); 
/*      */         } 
/*      */       } 
/*  552 */       if (this.creatures.size() == 0) {
/*  553 */         this.creatures = null;
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean addCreature(Creature creature) {
/*  559 */     if (this.creatures == null)
/*  560 */       this.creatures = new HashSet<>(); 
/*  561 */     if (!this.creatures.contains(creature)) {
/*      */       
/*  563 */       this.creatures.add(creature);
/*  564 */       creature.setCurrentDoor(this);
/*  565 */       if (canBeOpenedBy(creature, false)) {
/*      */         
/*  567 */         if (!isOpen() && creature.isVisible() && !creature.isGhost()) {
/*  568 */           open();
/*      */         }
/*  570 */         creature.getCommunicator().sendPassable(true, this);
/*      */       } 
/*  572 */       return true;
/*      */     } 
/*  574 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLock(long lockid) {
/*  583 */     this.lock = lockid;
/*      */     
/*      */     try {
/*  586 */       save();
/*      */     }
/*  588 */     catch (IOException iox) {
/*      */       
/*  590 */       logger.log(Level.WARNING, "Failed to save door for structure with id " + this.structure);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getLockId() throws NoSuchLockException {
/*  596 */     if (this.lock == -10L)
/*  597 */       throw new NoSuchLockException("No ID"); 
/*  598 */     return this.lock;
/*      */   }
/*      */ 
/*      */   
/*      */   boolean keyFits(long keyId) throws NoSuchLockException {
/*  603 */     if (this.lock == -10L) {
/*  604 */       throw new NoSuchLockException("No ID");
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/*  609 */       Structure struct = Structures.getStructure(this.structure);
/*  610 */       if (struct.getWritId() == keyId) {
/*  611 */         return true;
/*      */       }
/*  613 */     } catch (NoSuchStructureException nss) {
/*      */       
/*  615 */       logger.log(Level.WARNING, "This door's structure does not exist! " + this.startx + ", " + this.starty + "-" + this.endx + ", " + this.endy + ", structure: " + this.structure + " - " + nss, (Throwable)nss);
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/*  620 */       Item doorlock = Items.getItem(this.lock);
/*  621 */       return doorlock.isUnlockedBy(keyId);
/*      */     }
/*  623 */     catch (NoSuchItemException nsi) {
/*      */       
/*  625 */       logger.log(Level.INFO, "Lock has decayed? Id was " + this.lock + ", structure: " + this.structure + " - " + nsi);
/*      */ 
/*      */       
/*  628 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isOpen() {
/*  637 */     return this.open;
/*      */   }
/*      */ 
/*      */   
/*      */   void close() {
/*  642 */     if (this.wall != null && this.wall.isFinished()) {
/*      */       
/*  644 */       if (this.wall.isAlwaysOpen() == true) {
/*      */         return;
/*      */       }
/*  647 */       if (this.innerTile != null) {
/*      */         
/*  649 */         SoundPlayer.playSound("sound.door.close", this.innerTile.tilex, this.innerTile.tiley, this.innerTile.isOnSurface(), 1.0F);
/*      */       }
/*  651 */       else if (this.outerTile != null) {
/*      */         
/*  653 */         SoundPlayer.playSound("sound.door.close", this.outerTile.tilex, this.outerTile.tiley, this.outerTile.isOnSurface(), 1.0F);
/*      */       } 
/*      */     } 
/*  656 */     this.open = false;
/*  657 */     if (this.watchers != null)
/*      */     {
/*  659 */       for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext(); ) {
/*      */         
/*  661 */         VirtualZone z = it.next();
/*  662 */         z.closeDoor(this);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   void open() {
/*  669 */     if (this.wall != null && this.wall.isFinished()) {
/*      */       
/*  671 */       if (this.wall.isAlwaysOpen() == true) {
/*      */         return;
/*      */       }
/*  674 */       if (this.innerTile != null) {
/*      */         
/*  676 */         SoundPlayer.playSound("sound.door.open", this.innerTile.tilex, this.innerTile.tiley, this.innerTile.isOnSurface(), 1.0F);
/*      */       }
/*  678 */       else if (this.outerTile != null) {
/*      */         
/*  680 */         SoundPlayer.playSound("sound.door.open", this.outerTile.tilex, this.outerTile.tiley, this.outerTile.isOnSurface(), 1.0F);
/*      */       } 
/*      */     } 
/*  683 */     this.open = true;
/*  684 */     if (this.watchers != null)
/*      */     {
/*  686 */       for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext(); ) {
/*      */         
/*  688 */         VirtualZone z = it.next();
/*  689 */         z.openDoor(this);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public float getQualityLevel() {
/*  696 */     return this.wall.getCurrentQualityLevel();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Item getLock() throws NoSuchLockException {
/*  706 */     if (this.lock == -10L) {
/*  707 */       throw new NoSuchLockException("No ID");
/*      */     }
/*      */     try {
/*  710 */       Item toReturn = Items.getItem(this.lock);
/*  711 */       return toReturn;
/*      */     }
/*  713 */     catch (NoSuchItemException nsi) {
/*      */       
/*  715 */       throw new NoSuchLockException(nsi);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean pollUnlocked() {
/*  721 */     if (this.lockCounter > 0) {
/*      */       
/*  723 */       this.lockCounter = (short)(this.lockCounter - 1);
/*  724 */       if (this.lockCounter == 0) {
/*      */         
/*  726 */         playLockSound();
/*      */         
/*  728 */         return true;
/*      */       } 
/*      */     } 
/*  731 */     return false;
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
/*      */   public final boolean startAlert(boolean playSound) {
/*  743 */     this.preAlertLockedStatus = isLocked();
/*  744 */     if (!this.preAlertLockedStatus) {
/*      */       
/*  746 */       lock(playSound);
/*      */       
/*  748 */       return true;
/*      */     } 
/*  750 */     return false;
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
/*      */   public final boolean endAlert(boolean playSound) {
/*  762 */     if (!this.preAlertLockedStatus) {
/*      */       
/*  764 */       unlock(playSound);
/*      */       
/*  766 */       return true;
/*      */     } 
/*  768 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void lock(boolean playSound) {
/*      */     try {
/*  776 */       Item lLock = getLock();
/*  777 */       lLock.lock();
/*  778 */       if (playSound) {
/*  779 */         playLockSound();
/*      */       }
/*  781 */     } catch (NoSuchLockException noSuchLockException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void unlock(boolean playSound) {
/*      */     try {
/*  790 */       Item lLock = getLock();
/*  791 */       lLock.unlock();
/*  792 */       if (playSound) {
/*  793 */         playLockSound();
/*      */       }
/*  795 */     } catch (NoSuchLockException noSuchLockException) {}
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
/*      */   public final boolean isUnlocked() {
/*  807 */     return !isLocked();
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
/*      */   public final boolean isLocked() {
/*  819 */     if (this.lockCounter > 0) {
/*  820 */       return false;
/*      */     }
/*      */     try {
/*  823 */       Item lLock = getLock();
/*  824 */       return lLock.isLocked();
/*      */     }
/*  826 */     catch (NoSuchLockException nsi) {
/*      */       
/*  828 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNewName(String newname) {
/*  834 */     this.name = newname;
/*      */     
/*  836 */     this.innerTile.updateWall(this.wall);
/*  837 */     this.outerTile.updateWall(this.wall);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void save() throws IOException;
/*      */ 
/*      */   
/*      */   abstract void load() throws IOException;
/*      */ 
/*      */   
/*      */   public abstract void delete();
/*      */ 
/*      */   
/*      */   public abstract void setName(String paramString);
/*      */ 
/*      */   
/*      */   public int getFloorLevel() {
/*  855 */     return this.wall.getFloorLevel();
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
/*  866 */     return AnimalSettings.getMaxAllowed();
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
/*  877 */     return this.wall.getId();
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
/*  889 */     return -10;
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
/*  900 */     return getName();
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
/*  911 */     setName(aNewName);
/*  912 */     return true;
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
/*  923 */     return isOwner(playerId);
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
/*  934 */     return isOwner(creature.getWurmId());
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
/*      */   public boolean isOwner(long playerId) {
/*      */     try {
/*  948 */       Structure struct = Structures.getStructure(this.structure);
/*  949 */       return struct.isOwner(playerId);
/*      */     }
/*  951 */     catch (NoSuchStructureException e) {
/*      */       
/*  953 */       return false;
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
/*      */   public boolean canChangeName(Creature creature) {
/*  965 */     return (isOwner(creature) || creature.getPower() > 1);
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
/*  976 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean showWarning() {
/*      */     try {
/*  983 */       getLock();
/*  984 */       return false;
/*      */     }
/*  986 */     catch (NoSuchLockException e) {
/*      */       
/*  988 */       return true;
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
/*      */   public String getWarning() {
/* 1000 */     if (showWarning())
/* 1001 */       return "NEEDS TO HAVE A LOCK!"; 
/* 1002 */     return "";
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
/* 1013 */     return DoorSettings.getPermissionsPlayerList(getWurmId());
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
/*      */   public boolean isManaged() {
/* 1025 */     if (this.wall == null)
/* 1026 */       return false; 
/* 1027 */     return this.wall.getSettings().hasPermission(Permissions.Allow.SETTLEMENT_MAY_MANAGE.getBit());
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
/*      */   public boolean isManageEnabled(Player player) {
/* 1039 */     return (mayManage((Creature)player) || player.getPower() > 1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsManaged(boolean newIsManaged, Player player) {
/* 1045 */     if (this.wall != null) {
/*      */       
/* 1047 */       if (newIsManaged && DoorSettings.exists(getWurmId())) {
/*      */         
/* 1049 */         DoorSettings.remove(getWurmId());
/* 1050 */         if (player != null)
/* 1051 */           PermissionsHistories.addHistoryEntry(getWurmId(), System.currentTimeMillis(), player
/* 1052 */               .getWurmId(), player.getName(), "Removed all permissions"); 
/*      */       } 
/* 1054 */       this.wall.getSettings().setPermissionBit(Permissions.Allow.SETTLEMENT_MAY_MANAGE.getBit(), newIsManaged);
/* 1055 */       this.wall.savePermissions();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addDefaultCitizenPermissions() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCitizen(Creature aCreature) {
/*      */     try {
/* 1075 */       Structure struct = Structures.getStructure(this.structure);
/* 1076 */       return struct.isCitizen(aCreature);
/*      */     }
/* 1078 */     catch (NoSuchStructureException e) {
/*      */       
/* 1080 */       return false;
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
/*      */   public boolean isAllied(Creature aCreature) {
/*      */     try {
/* 1094 */       Structure struct = Structures.getStructure(this.structure);
/* 1095 */       return struct.isAllied(aCreature);
/*      */     }
/* 1097 */     catch (NoSuchStructureException e) {
/*      */       
/* 1099 */       return false;
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
/*      */   public boolean isSameKingdom(Creature aCreature) {
/*      */     try {
/* 1113 */       Structure struct = Structures.getStructure(this.structure);
/* 1114 */       return struct.isSameKingdom(aCreature);
/*      */     }
/* 1116 */     catch (NoSuchStructureException e) {
/*      */       
/* 1118 */       return false;
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
/*      */   public boolean isGuest(Creature creature) {
/* 1130 */     return isGuest(creature.getWurmId());
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
/* 1142 */     return DoorSettings.isGuest(this, playerId);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addGuest(long guestId, int aSettings) {
/* 1148 */     DoorSettings.addPlayer(getWurmId(), guestId, aSettings);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeGuest(long guestId) {
/* 1154 */     DoorSettings.removePlayer(getWurmId(), guestId);
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
/*      */   public boolean canHavePermissions() {
/* 1166 */     return isLocked();
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
/*      */   public boolean mayShowPermissions(Creature creature) {
/* 1178 */     return (!isManaged() && hasLock() && mayManage(creature));
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
/* 1189 */     if (this.wall == null)
/* 1190 */       return false; 
/* 1191 */     Structure structure = Structures.getStructureOrNull(this.wall.getStructureId());
/* 1192 */     if (structure == null) {
/* 1193 */       return false;
/*      */     }
/* 1195 */     return structure.canManage(creature);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayManage(Creature creature) {
/* 1206 */     if (creature.getPower() > 1)
/* 1207 */       return true; 
/* 1208 */     return canManage(creature);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayPass(Creature creature) {
/* 1217 */     if (creature.getPower() > 1)
/* 1218 */       return true; 
/* 1219 */     if (this.wall == null)
/* 1220 */       return true; 
/* 1221 */     Structure structure = Structures.getStructureOrNull(this.wall.getStructureId());
/* 1222 */     if (structure == null)
/* 1223 */       return true; 
/* 1224 */     if (!isLocked())
/* 1225 */       return true; 
/* 1226 */     if (structure.isExcluded(creature))
/* 1227 */       return false; 
/* 1228 */     if (isManaged()) {
/* 1229 */       return structure.mayPass(creature);
/*      */     }
/* 1231 */     if (DoorSettings.isExcluded(this, creature))
/* 1232 */       return false; 
/* 1233 */     return DoorSettings.mayPass(this, creature);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayLock(Creature creature) {
/* 1243 */     if (creature.getPower() > 1)
/* 1244 */       return true; 
/* 1245 */     if (this.wall == null)
/* 1246 */       return true; 
/* 1247 */     Structure structure = Structures.getStructureOrNull(this.wall.getStructureId());
/* 1248 */     if (structure == null)
/* 1249 */       return true; 
/* 1250 */     if (structure.isExcluded(creature))
/* 1251 */       return false; 
/* 1252 */     if (isManaged())
/* 1253 */       return structure.mayModify(creature); 
/* 1254 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasLock() {
/*      */     try {
/* 1261 */       getLock();
/*      */     }
/* 1263 */     catch (NoSuchLockException e) {
/*      */       
/* 1265 */       this.lock = -10L;
/*      */     } 
/* 1267 */     return (this.lock != -10L);
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
/* 1278 */     if (this.wall == null)
/* 1279 */       return "No Wall!"; 
/* 1280 */     return this.wall.getTypeName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String mayManageText(Player aPlayer) {
/* 1291 */     return "Controlled By Building";
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
/* 1302 */     return "If ticked, then building controls entry.";
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
/* 1313 */     return "This will allow the building to Control this door.";
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
/* 1324 */     return "Are you sure?";
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
/* 1335 */     return "This will allow the door to be independant of the building 'May Enter' setting.";
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
/* 1346 */     return "Are you sure?";
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
/*      */   public String getSettlementName() {
/*      */     try {
/* 1359 */       Structure struct = Structures.getStructure(this.structure);
/* 1360 */       return struct.getSettlementName();
/*      */     }
/* 1362 */     catch (NoSuchStructureException e) {
/*      */       
/* 1364 */       return "";
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
/*      */   public String getAllianceName() {
/*      */     try {
/* 1378 */       Structure struct = Structures.getStructure(this.structure);
/* 1379 */       return struct.getAllianceName();
/*      */     }
/* 1381 */     catch (NoSuchStructureException e) {
/*      */       
/* 1383 */       return "";
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
/*      */   public String getKingdomName() {
/*      */     try {
/* 1397 */       Structure struct = Structures.getStructure(this.structure);
/* 1398 */       return struct.getKingdomName();
/*      */     }
/* 1400 */     catch (NoSuchStructureException e) {
/*      */       
/* 1402 */       return "";
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
/*      */   public boolean canAllowEveryone() {
/* 1414 */     return false;
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
/* 1425 */     return "";
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
/* 1437 */     return false;
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
/* 1448 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isNotLockable() {
/* 1453 */     return this.wall.isNotLockable();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isNotLockpickable() {
/* 1458 */     return this.wall.isNotLockpickable();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getLayer() {
/* 1463 */     return this.wall.getLayer();
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\Door.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */