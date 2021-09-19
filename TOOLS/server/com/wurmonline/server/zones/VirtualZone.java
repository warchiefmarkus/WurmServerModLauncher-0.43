/*      */ package com.wurmonline.server.zones;
/*      */ 
/*      */ import com.wurmonline.math.Vector3f;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.Message;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.behaviours.MethodsCreatures;
/*      */ import com.wurmonline.server.behaviours.Seat;
/*      */ import com.wurmonline.server.behaviours.Vehicle;
/*      */ import com.wurmonline.server.behaviours.Vehicles;
/*      */ import com.wurmonline.server.bodys.BodyTemplate;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.CreatureMove;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.MineDoorPermission;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.creatures.Npc;
/*      */ import com.wurmonline.server.effects.Effect;
/*      */ import com.wurmonline.server.endgames.EndGameItems;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.NoSpaceException;
/*      */ import com.wurmonline.server.items.WurmColor;
/*      */ import com.wurmonline.server.kingdom.GuardTower;
/*      */ import com.wurmonline.server.kingdom.King;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.MovementEntity;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.sounds.Sound;
/*      */ import com.wurmonline.server.structures.BridgePart;
/*      */ import com.wurmonline.server.structures.Door;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.FenceGate;
/*      */ import com.wurmonline.server.structures.Floor;
/*      */ import com.wurmonline.server.structures.NoSuchStructureException;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.Structures;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.shared.constants.AttitudeConstants;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import com.wurmonline.shared.constants.ProtoConstants;
/*      */ import com.wurmonline.shared.constants.StructureTypeEnum;
/*      */ import com.wurmonline.shared.util.StringUtilities;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
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
/*      */ public final class VirtualZone
/*      */   implements MiscConstants, CounterTypes, AttitudeConstants, ProtoConstants, TimeConstants
/*      */ {
/*      */   private final Creature watcher;
/*      */   private Zone[] watchedZones;
/*      */   private Set<Item> items;
/*      */   private Set<Effect> effects;
/*      */   private Set<AreaSpellEffect> areaEffects;
/*      */   private Set<Long> finalizedBuildings;
/*      */   private Set<Door> doors;
/*      */   private Set<Fence> fences;
/*      */   private Set<MineDoorPermission> mineDoors;
/*      */   private int centerx;
/*      */   private int centery;
/*  101 */   private int startX = 0;
/*  102 */   private int endX = 0;
/*  103 */   private int startY = 0;
/*  104 */   private int endY = 0;
/*      */   private Set<Structure> structures;
/*  106 */   private final Map<Long, CreatureMove> creatures = new HashMap<>();
/*      */   
/*  108 */   private static final Logger logger = Logger.getLogger(VirtualZone.class.getName());
/*      */   private int size;
/*      */   private final int id;
/*  111 */   private static int ids = 0;
/*  112 */   private static final int worldTileSizeX = 1 << Constants.meshSize;
/*  113 */   private static final int worldTileSizeY = 1 << Constants.meshSize;
/*      */   private final boolean isOnSurface;
/*  115 */   private static final Long[] emptyLongArray = new Long[0];
/*  116 */   private float MOVELIMIT = 0.05F;
/*      */   
/*  118 */   private ArrayList<Structure> nearbyStructureList = new ArrayList<>();
/*      */   
/*  120 */   private static final Set<VirtualZone> allZones = new HashSet<>();
/*      */   
/*      */   private static final int surfaceToSurfaceLocalDistance = 80;
/*  123 */   private static final int caveToSurfaceLocalDistance = Servers.localServer.EPIC ? 20 : 80;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int surfaceToCaveLocalDistance = 20;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int caveToCaveLocalDistance = 20;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int ITEM_INSIDE_RENDERDIST = 15;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int HOUSEITEMS_RENDERDIST = 5;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean hasReceivedLocalMessageOnChaos;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean covers(int x, int y) {
/*  165 */     return (x >= this.startX && x <= this.endX && y >= this.startY && y <= this.endY);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Creature getWatcher() {
/*  174 */     return this.watcher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getId() {
/*  183 */     return this.id;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOnSurface() {
/*  192 */     return this.isOnSurface;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getStartX() {
/*  201 */     return this.startX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getStartY() {
/*  210 */     return this.startY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getEndX() {
/*  219 */     return this.endX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getEndY() {
/*  228 */     return this.endY;
/*      */   }
/*      */ 
/*      */   
/*      */   public void initialize() {
/*  233 */     if (!this.watcher.isDead()) {
/*      */       
/*  235 */       this.watchedZones = Zones.getZonesCoveredBy(this);
/*  236 */       for (int i = 0; i < this.watchedZones.length; i++) {
/*      */ 
/*      */         
/*      */         try {
/*  240 */           this.watchedZones[i].addWatcher(this.id);
/*      */         }
/*  242 */         catch (NoSuchZoneException nze) {
/*      */           
/*  244 */           logger.log(Level.INFO, nze.getMessage(), (Throwable)nze);
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
/*      */   void addVillage(Village newVillage) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void broadCastMessage(Message message) {
/*  263 */     if (!this.watcher.isIgnored(message.getSender().getWurmId())) {
/*      */       
/*  265 */       if (this.watcher.isNpc())
/*      */       {
/*  267 */         if (message.getSender().getWurmId() != this.watcher.getWurmId())
/*  268 */           ((Npc)this.watcher).getChatManager().addLocalChat(message); 
/*      */       }
/*  270 */       if (!this.watcher.getCommunicator().isInvulnerable())
/*      */       {
/*  272 */         this.watcher.getCommunicator().sendMessage(message);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void callGuards() {
/*  279 */     boolean found = false;
/*  280 */     if (this.items != null)
/*      */     {
/*  282 */       for (Item i : this.items) {
/*      */         
/*  284 */         if (i.isKingdomMarker())
/*      */         {
/*  286 */           if (i.getKingdom() == this.watcher.getKingdomId()) {
/*      */             
/*  288 */             GuardTower tower = Kingdoms.getTower(i);
/*  289 */             if (tower != null && tower.alertGuards(this.watcher)) {
/*      */               
/*  291 */               this.watcher.getCommunicator().sendSafeServerMessage("Guards from " + i.getName() + " runs to the rescue!");
/*  292 */               found = true;
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/*      */     }
/*  298 */     if (!found) {
/*  299 */       this.watcher.getCommunicator().sendSafeServerMessage("No guards seem to respond to your call.");
/*      */     }
/*      */   }
/*      */   
/*      */   public int getCenterX() {
/*  304 */     return this.centerx;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getCenterY() {
/*  309 */     return this.centery;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int getSize() {
/*  319 */     return this.size;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean shouldSeeCaves() {
/*  324 */     if (this.watcher.isPlayer())
/*      */     {
/*  326 */       if (this.watcher.isOnSurface()) {
/*      */         
/*  328 */         for (int x = this.startX + 10; x <= this.endX - 10; x++) {
/*      */           
/*  330 */           for (int y = this.startY + 10; y <= this.endY - 10; y++) {
/*      */             
/*  332 */             if (Tiles.decodeType(Server.caveMesh.data[x | y << Constants.meshSize]) == Tiles.Tile.TILE_CAVE_EXIT.id)
/*      */             {
/*  334 */               return true;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } else {
/*      */         
/*  340 */         return true;
/*      */       }  } 
/*  342 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void move(int xChange, int yChange) {
/*  347 */     this.centerx = Math.max(0, this.centerx + xChange);
/*  348 */     this.centery = Math.max(0, this.centery + yChange);
/*  349 */     this.centerx = Math.min(worldTileSizeX - 1, this.centerx);
/*  350 */     this.centery = Math.min(worldTileSizeY - 1, this.centery);
/*  351 */     this.startX = Math.max(0, this.centerx - this.size);
/*  352 */     this.startY = Math.max(0, this.centery - this.size);
/*  353 */     this.endX = Math.min(worldTileSizeX - 1, this.centerx + this.size);
/*  354 */     this.endY = Math.min(worldTileSizeY - 1, this.centery + this.size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int getSizeX() {
/*  362 */     return this.endX - this.startX;
/*      */   }
/*      */ 
/*      */   
/*      */   private final int getSizeY() {
/*  367 */     return this.endY - this.startY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void stopWatching() {
/*  375 */     Zone[] checkedZones = Zones.getZonesCoveredBy(Math.max(0, this.startX - 100), Math.max(0, this.startY - 100), 
/*  376 */         Math.min(Zones.worldTileSizeX - 1, this.endX + 100), Math.min(Zones.worldTileSizeY - 1, this.endY + 100), this.isOnSurface);
/*  377 */     for (int x = 0; x < checkedZones.length; x++) {
/*      */ 
/*      */       
/*      */       try {
/*  381 */         checkedZones[x].removeWatcher(this);
/*      */       }
/*  383 */       catch (NoSuchZoneException sex) {
/*      */         
/*  385 */         logger.log(Level.WARNING, sex.getMessage(), (Throwable)sex);
/*      */       } 
/*      */     } 
/*  388 */     this.watchedZones = null;
/*  389 */     pruneDestroy();
/*  390 */     this.size = 0;
/*      */ 
/*      */ 
/*      */     
/*  394 */     allZones.remove(this);
/*  395 */     if (Server.rand.nextInt(1000) == 0) {
/*      */       
/*  397 */       int cs = Creatures.getInstance().getNumberOfCreatures();
/*  398 */       int ps = Players.getInstance().getNumberOfPlayers();
/*  399 */       if (allZones.size() > ps * 2 + cs * 2 + 100) {
/*  400 */         logger.log(Level.INFO, "Number of virtual zones now: " + allZones.size() + ". Creatures*2=" + (cs * 2) + ", players*2=" + (ps * 2));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Long[] getCreatures() {
/*  411 */     if (this.creatures != null)
/*  412 */       return (Long[])this.creatures.keySet().toArray((Object[])new Long[this.creatures.size()]); 
/*  413 */     return emptyLongArray;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsCreature(Creature creature) {
/*  418 */     if (this.creatures != null)
/*  419 */       return this.creatures.keySet().contains(Long.valueOf(creature.getWurmId())); 
/*  420 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void refreshAttitudes() {
/*  425 */     for (Long l : this.creatures.keySet()) {
/*      */       
/*      */       try {
/*  428 */         Creature cret = Creatures.getInstance().getCreature(l.longValue());
/*  429 */         sendAttitude(cret);
/*      */       }
/*  431 */       catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void pruneDestroy() {
/*  439 */     removeAllStructures();
/*  440 */     this.finalizedBuildings = null;
/*      */     
/*  442 */     if (this.doors != null) {
/*      */       
/*  444 */       for (Iterator<Door> it = this.doors.iterator(); it.hasNext(); ) {
/*      */         
/*  446 */         Door door = it.next();
/*  447 */         door.removeWatcher(this);
/*      */       } 
/*  449 */       this.doors = null;
/*      */     } 
/*  451 */     if (this.creatures != null) {
/*      */       
/*  453 */       for (Long l : this.creatures.keySet())
/*      */       {
/*  455 */         this.watcher.getCommunicator().sendDeleteCreature(l.longValue());
/*      */       }
/*  457 */       this.creatures.clear();
/*      */     } 
/*  459 */     if (this.fences != null) {
/*      */       
/*  461 */       for (Iterator<Fence> it = this.fences.iterator(); it.hasNext(); ) {
/*      */         
/*  463 */         Fence fence = it.next();
/*  464 */         this.watcher.getCommunicator().sendRemoveFence(fence);
/*      */       } 
/*  466 */       this.fences = null;
/*      */     } 
/*  468 */     if (this.items != null) {
/*      */       
/*  470 */       for (Iterator<Item> it = this.items.iterator(); it.hasNext(); ) {
/*      */         
/*  472 */         Item item = it.next();
/*  473 */         if (item.isMovingItem()) {
/*  474 */           this.watcher.getCommunicator().sendDeleteMovingItem(item.getWurmId()); continue;
/*      */         } 
/*  476 */         this.watcher.getCommunicator().sendRemoveItem(item);
/*      */       } 
/*  478 */       this.items = null;
/*      */     } 
/*  480 */     if (this.effects != null) {
/*      */       
/*  482 */       for (Iterator<Effect> it = this.effects.iterator(); it.hasNext(); ) {
/*      */         
/*  484 */         Effect effect = it.next();
/*  485 */         this.watcher.getCommunicator().sendRemoveEffect(effect.getOwner());
/*      */       } 
/*  487 */       this.effects = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void addFence(Fence fence) {
/*  493 */     if (this.fences == null)
/*  494 */       this.fences = new HashSet<>(); 
/*  495 */     if (!this.fences.contains(fence))
/*      */     {
/*  497 */       if (covers(fence.getTileX(), fence.getTileY())) {
/*      */         
/*  499 */         this.fences.add(fence);
/*  500 */         this.watcher.getCommunicator().sendAddFence(fence);
/*  501 */         if (fence.getDamage() >= 60.0F) {
/*  502 */           this.watcher.getCommunicator().sendDamageState(fence.getId(), (byte)(int)fence.getDamage());
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   void removeFence(Fence fence) {
/*  509 */     if (this.fences != null) {
/*      */       
/*  511 */       if (this.fences.contains(fence))
/*      */       {
/*  513 */         if (this.watcher != null)
/*      */         {
/*  515 */           this.watcher.getCommunicator().sendRemoveFence(fence);
/*      */         }
/*      */       }
/*  518 */       this.fences.remove(fence);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void addMineDoor(MineDoorPermission door) {
/*  524 */     if (this.mineDoors == null)
/*  525 */       this.mineDoors = new HashSet<>(); 
/*  526 */     if (!this.mineDoors.contains(door)) {
/*      */       
/*  528 */       this.mineDoors.add(door);
/*  529 */       this.watcher.getCommunicator().sendAddMineDoor(door);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeMineDoor(MineDoorPermission door) {
/*  535 */     if (this.mineDoors != null)
/*      */     {
/*  537 */       if (this.mineDoors.contains(door)) {
/*      */         
/*  539 */         if (this.watcher != null)
/*      */         {
/*  541 */           this.watcher.getCommunicator().sendRemoveMineDoor(door);
/*      */         }
/*  543 */         this.mineDoors.remove(door);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   void renameItem(Item item, String newName, String newModelName) {
/*  550 */     if (this.items != null && this.items.contains(item))
/*      */     {
/*  552 */       if (this.watcher != null) {
/*  553 */         this.watcher.getCommunicator().sendRename(item, newName, newModelName);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   void sendAttitude(Creature creature) {
/*  559 */     if (this.creatures != null && this.creatures.keySet().contains(new Long(creature.getWurmId())))
/*      */     {
/*  561 */       if (this.watcher instanceof Player) {
/*  562 */         this.watcher.getCommunicator().changeAttitude(creature.getWurmId(), creature.getAttitude(this.watcher));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   void sendUpdateHasTarget(Creature creature) {
/*  568 */     if (this.creatures != null && this.creatures.keySet().contains(new Long(creature.getWurmId())))
/*      */     {
/*  570 */       if (this.watcher instanceof Player)
/*      */       {
/*  572 */         if (creature.getTarget() != null) {
/*      */           
/*  574 */           this.watcher.getCommunicator().sendHasTarget(creature.getWurmId(), true);
/*      */         }
/*      */         else {
/*      */           
/*  578 */           this.watcher.getCommunicator().sendHasTarget(creature.getWurmId(), false);
/*      */         } 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private byte getLayer() {
/*  586 */     if (isOnSurface())
/*  587 */       return 0; 
/*  588 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   boolean addCreature(long creatureId, boolean overRideRange) throws NoSuchCreatureException, NoSuchPlayerException {
/*  594 */     return addCreature(creatureId, overRideRange, -10L, 0.0F, 0.0F, 0.0F);
/*      */   }
/*      */   
/*  597 */   public VirtualZone(Creature aWatcher, int aStartX, int aStartY, int centerX, int centerY, int aSz, boolean aIsOnSurface) { this.hasReceivedLocalMessageOnChaos = false; this.isOnSurface = aIsOnSurface;
/*      */     this.startX = Math.max(0, aStartX);
/*      */     this.startY = Math.max(0, aStartY);
/*      */     this.centerx = Math.max(0, centerX);
/*      */     this.centery = Math.max(0, centerY);
/*      */     this.centerx = Math.min(worldTileSizeX - 1, centerX);
/*      */     this.centery = Math.min(worldTileSizeY - 1, centerY);
/*      */     this.endX = Math.min(worldTileSizeX - 1, this.centerx + aSz);
/*      */     this.endY = Math.min(worldTileSizeY - 1, this.centery + aSz);
/*      */     this.id = ids++;
/*      */     this.size = aSz;
/*      */     this.watcher = aWatcher;
/*  609 */     allZones.add(this); } public final boolean addCreature(long creatureId, boolean overRideRange, long copyId, float offx, float offy, float offz) throws NoSuchCreatureException, NoSuchPlayerException { Creature creature = Server.getInstance().getCreature(creatureId);
/*      */     
/*  611 */     if (coversCreature(creature) || (this.watcher != null && this.watcher.isPlayer() && overRideRange)) {
/*      */       
/*  613 */       if (this.watcher != null && this.watcher.getWurmId() != creatureId)
/*      */       {
/*  615 */         if (creature.isVisibleTo(this.watcher))
/*      */         {
/*  617 */           if (!this.watcher.isPlayer() || this.watcher.hasLink())
/*      */           {
/*  619 */             if (this.creatures.keySet().contains(Long.valueOf(creatureId)) && copyId == -10L) {
/*  620 */               return false;
/*      */             }
/*  622 */             if (!this.creatures.keySet().contains(Long.valueOf(creatureId)))
/*      */             {
/*  624 */               if (this.watcher.isPlayer()) {
/*      */                 
/*  626 */                 this.creatures.put(Long.valueOf(creatureId), new CreatureMove());
/*  627 */                 creature.setVisibleToPlayers(true);
/*      */               } else {
/*      */                 
/*  630 */                 this.creatures.put(Long.valueOf(creatureId), null);
/*      */               }  } 
/*  632 */             if (this.watcher.hasLink()) {
/*      */               
/*  634 */               String suff = "";
/*  635 */               String pre = "";
/*      */               
/*  637 */               if (!this.watcher.hasFlag(56)) {
/*      */                 
/*  639 */                 if (!creature.hasFlag(24))
/*  640 */                   pre = creature.getAbilityTitle(); 
/*  641 */                 if (creature.getCultist() != null && !creature.hasFlag(25)) {
/*  642 */                   suff = suff + " " + creature.getCultist().getCultistTitleShort();
/*      */                 }
/*      */               } 
/*  645 */               boolean enemy = false;
/*  646 */               if (creature.getPower() > 0 && !Servers.localServer.testServer) {
/*      */                 
/*  648 */                 if (creature.getPower() == 1) {
/*  649 */                   suff = " (HERO)";
/*  650 */                 } else if (creature.getPower() == 2) {
/*  651 */                   suff = " (GM)";
/*  652 */                 } else if (creature.getPower() == 3) {
/*  653 */                   suff = " (GOD)";
/*  654 */                 } else if (creature.getPower() == 4) {
/*  655 */                   suff = " (ARCH)";
/*  656 */                 } else if (creature.getPower() == 5) {
/*  657 */                   suff = " (ADMIN)";
/*      */                 } 
/*      */               } else {
/*      */                 
/*  661 */                 if (creature.isKing()) {
/*  662 */                   suff = suff + " [" + King.getRulerTitle((creature.getSex() == 0), creature.getKingdomId()) + "]";
/*      */                 }
/*      */                 
/*  665 */                 if (this.watcher.getKingdomId() != 0 && creature.getKingdomId() != 0 && 
/*  666 */                   !creature.isFriendlyKingdom(this.watcher.getKingdomId())) {
/*      */                   
/*  668 */                   if (creature.getPower() < 2 && this.watcher.getPower() < 2 && creature
/*  669 */                     .isPlayer())
/*      */                   {
/*  671 */                     if (this.watcher.getCultist() != null && this.watcher.getCultist().getLevel() > 8 && this.watcher
/*  672 */                       .getCultist().getPath() == 3) {
/*      */ 
/*      */ 
/*      */                       
/*  676 */                       suff = suff + " (ENEMY)";
/*      */                     } else {
/*      */                       
/*  679 */                       suff = " (ENEMY)";
/*  680 */                     }  enemy = true;
/*      */                   }
/*      */                 
/*  683 */                 } else if (creature.getKingdomTemplateId() != 3 && creature.getReputation() < 0) {
/*      */                   
/*  685 */                   suff = suff + " (OUTLAW)";
/*  686 */                   enemy = true;
/*      */                 }
/*  688 */                 else if (this.watcher.getCitizenVillage() != null && creature.isPlayer() && this.watcher
/*  689 */                   .getCitizenVillage().isEnemy(creature)) {
/*      */                   
/*  691 */                   suff = " (ENEMY)";
/*  692 */                   enemy = true;
/*      */                 }
/*  694 */                 else if (creature.hasAttackedUnmotivated()) {
/*      */                   
/*  696 */                   suff = " (HUNTED)";
/*      */ 
/*      */ 
/*      */                 
/*      */                 }
/*  701 */                 else if (!this.watcher.isPlayer() || !this.watcher.hasFlag(56)) {
/*      */                   
/*  703 */                   if (creature.getTitle() != null || (Features.Feature.COMPOUND_TITLES
/*  704 */                     .isEnabled() && creature.getSecondTitle() != null))
/*      */                   {
/*  706 */                     if (!creature.getTitleString().isEmpty()) {
/*      */                       
/*  708 */                       suff = suff + " [";
/*  709 */                       suff = suff + creature.getTitleString();
/*  710 */                       suff = suff + "]";
/*      */                     } 
/*      */                   }
/*      */                 } 
/*  714 */                 if (creature.isChampion() && creature.getDeity() != null) {
/*  715 */                   suff = suff + " [Champion of " + (creature.getDeity()).name + "]";
/*      */                 }
/*      */               } 
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
/*  732 */               if (enemy && creature.getPower() < 2 && this.watcher.getPower() < 2 && creature
/*  733 */                 .isPlayer())
/*      */               {
/*  735 */                 if ((creature.getFightingSkill().getRealKnowledge() > 20.0D || creature.getFaith() > 25.0F) && 
/*  736 */                   Servers.isThisAPvpServer())
/*  737 */                   this.watcher.addEnemyPresense(); 
/*      */               }
/*  739 */               byte layer = (byte)creature.getLayer();
/*      */ 
/*      */               
/*  742 */               if (overRideRange)
/*  743 */                 layer = getLayer(); 
/*  744 */               String hoverText = creature.getHoverText(this.watcher);
/*  745 */               this.watcher.getCommunicator().sendNewCreature((copyId != -10L) ? copyId : creatureId, pre + 
/*  746 */                   StringUtilities.raiseFirstLetterOnly(creature.getName()) + suff, hoverText, 
/*  747 */                   creature.isUndead() ? creature.getUndeadModelName() : creature.getModelName(), creature
/*  748 */                   .getStatus().getPositionX() + offx, creature
/*  749 */                   .getStatus().getPositionY() + offy, creature
/*  750 */                   .getStatus().getPositionZ() + offz, creature
/*  751 */                   .getStatus().getBridgeId(), creature
/*  752 */                   .getStatus().getRotation(), layer, (creature
/*      */                   
/*  754 */                   .getBridgeId() <= 0L && !creature.isSubmerged() && (creature
/*  755 */                   .getPower() == 0 || (creature.getMovementScheme()).onGround) && creature
/*  756 */                   .getFloorLevel() <= 0 && creature
/*  757 */                   .getMovementScheme().getGroundOffset() <= 0.0F), false, 
/*  758 */                   (creature.getTemplate().getTemplateId() != 119), creature
/*  759 */                   .getKingdomId(), creature.getFace(), creature.getBlood(), creature.isUndead(), (copyId != -10L || creature
/*  760 */                   .isNpc()), creature.getStatus().getModType());
/*      */               
/*  762 */               if (creature.getRarityShader() != 0)
/*  763 */                 setNewRarityShader(creature); 
/*  764 */               if (copyId != -10L) {
/*      */                 
/*  766 */                 this.watcher.getCommunicator().setCreatureDamage(copyId, creature.getStatus().calcDamPercent());
/*  767 */                 if (creature.getRarityShader() != 0)
/*  768 */                   this.watcher.getCommunicator().updateCreatureRarity(copyId, creature.getRarityShader()); 
/*      */               } 
/*  770 */               for (Item item : creature.getBody().getContainersAndWornItems()) {
/*      */                 
/*  772 */                 if (item != null) {
/*      */                   
/*      */                   try {
/*      */ 
/*      */ 
/*      */ 
/*      */                     
/*  779 */                     byte armorSlot = item.isArmour() ? BodyTemplate.convertToArmorEquipementSlot((byte)item.getParent().getPlace()) : BodyTemplate.convertToItemEquipementSlot((byte)item.getParent().getPlace());
/*      */                     
/*  781 */                     if (creature.isAnimal() && creature.isVehicle())
/*      */                     {
/*  783 */                       this.watcher.getCommunicator().sendHorseWear(creature.getWurmId(), item
/*  784 */                           .getTemplateId(), item
/*  785 */                           .getMaterial(), armorSlot, item.getAuxData());
/*      */                     }
/*      */                     else
/*      */                     {
/*  789 */                       this.watcher.getCommunicator().sendWearItem((copyId != -10L) ? copyId : creature
/*  790 */                           .getWurmId(), item
/*  791 */                           .getTemplateId(), armorSlot, 
/*  792 */                           WurmColor.getColorRed(item.getColor()), 
/*  793 */                           WurmColor.getColorGreen(item.getColor()), 
/*  794 */                           WurmColor.getColorBlue(item.getColor()), 
/*  795 */                           WurmColor.getColorRed(item.getColor2()), 
/*  796 */                           WurmColor.getColorGreen(item.getColor2()), 
/*  797 */                           WurmColor.getColorBlue(item.getColor2()), item
/*  798 */                           .getMaterial(), item.getRarity());
/*      */                     }
/*      */                   
/*      */                   }
/*  802 */                   catch (Exception exception) {}
/*      */                 }
/*      */               } 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  809 */               if (creature.hasCustomColor())
/*  810 */                 sendRepaint((copyId != -10L) ? copyId : creatureId, creature.getColorRed(), creature.getColorGreen(), creature
/*  811 */                     .getColorBlue(), (byte)-1, (byte)creature.getPaintMode()); 
/*  812 */               if (creature.hasCustomSize() || creature.isFish())
/*  813 */                 sendResizeCreature((copyId != -10L) ? copyId : creatureId, creature.getSizeModX(), creature.getSizeModY(), creature
/*  814 */                     .getSizeModZ()); 
/*  815 */               if (creature.getBestLightsource() != null) {
/*      */                 
/*  817 */                 addLightSource(creature, creature.getBestLightsource());
/*      */               }
/*  819 */               else if (creature.isPlayer()) {
/*  820 */                 ((Player)creature).sendLantern(this);
/*  821 */               }  if (this.watcher.isPlayer())
/*  822 */                 sendCreatureDamage(creature, creature.getStatus().calcDamPercent()); 
/*  823 */               if (creature.isOnFire()) {
/*  824 */                 sendAttachCreatureEffect(creature, (byte)1, creature.getFireRadius(), (byte)-1, (byte)-1, (byte)1);
/*      */               }
/*  826 */               if (creature.isGhost()) {
/*      */                 
/*  828 */                 this.watcher.getCommunicator().sendAttachEffect(creature.getWurmId(), (byte)2, 
/*      */                     
/*  830 */                     creature.isSpiritGuard() ? -56 : 100, 
/*  831 */                     creature.isSpiritGuard() ? 1 : 1, (byte)0, (byte)1);
/*  832 */                 this.watcher.getCommunicator().sendAttachEffect(creature.getWurmId(), (byte)3, (byte)50, 
/*      */                     
/*  834 */                     creature.isSpiritGuard() ? 50 : 50, (byte)50, (byte)1);
/*      */               }
/*  836 */               else if (creature.hasGlow()) {
/*      */                 
/*  838 */                 if (creature.hasCustomColor()) {
/*  839 */                   this.watcher.getCommunicator().sendAttachEffect(creature.getWurmId(), (byte)3, (byte)1, (byte)1, (byte)1, (byte)1);
/*      */                 } else {
/*      */                   
/*  842 */                   this.watcher.getCommunicator().sendAttachEffect(creature.getWurmId(), (byte)3, creature
/*  843 */                       .getColorRed(), creature
/*  844 */                       .getColorGreen(), creature.getColorGreen(), (byte)1);
/*      */                 } 
/*  846 */               }  sendCreatureItems(creature);
/*  847 */               if (creature.isPlayer() || creature.isNpc())
/*      */               {
/*  849 */                 if (!Servers.localServer.PVPSERVER || this.watcher
/*  850 */                   .isPaying())
/*      */                 {
/*  852 */                   this.watcher.getCommunicator().sendAddLocal(creature.getName(), creatureId);
/*      */                 }
/*      */               }
/*      */             } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  861 */             if (this.watcher.isTypeFleeing())
/*      */             {
/*  863 */               if (creature.isPlayer() || creature.isAggHuman() || creature.isHuman() || creature.isCarnivore() || creature
/*  864 */                 .isMonster()) {
/*      */                 
/*  866 */                 float newDistance = creature.getPos2f().distance(this.watcher.getPos2f());
/*  867 */                 if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) {
/*      */                   
/*  869 */                   int baseCounter = (int)(Math.max(1.0F, creature.getBaseCombatRating() - this.watcher.getBaseCombatRating()) * 5.0F);
/*  870 */                   if (baseCounter - newDistance > 0.0F) {
/*  871 */                     this.watcher.setFleeCounter((int)Math.min(60.0F, Math.max(3.0F, baseCounter - newDistance)));
/*      */                   }
/*      */                 } else {
/*      */                   
/*  875 */                   this.watcher.setFleeCounter(60);
/*      */                 } 
/*      */               } 
/*      */             }
/*  879 */             checkIfAttack(creature, creatureId);
/*  880 */             byte att = creature.getAttitude(this.watcher);
/*  881 */             if (att != 0)
/*  882 */               this.watcher.getCommunicator().changeAttitude((copyId != -10L) ? copyId : creatureId, att); 
/*  883 */             if (creature.getVehicle() != -10L) {
/*      */               
/*  885 */               Vehicle vehic = Vehicles.getVehicleForId(creature.getVehicle());
/*  886 */               if (vehic != null) {
/*      */                 
/*  888 */                 Seat s = vehic.getSeatFor(creature.getWurmId());
/*  889 */                 if (s != null)
/*  890 */                   sendAttachCreature(creatureId, creature.getVehicle(), s.offx, s.offy, s.offz, vehic
/*  891 */                       .getSeatNumberFor(s)); 
/*      */               } 
/*      */             } 
/*  894 */             if (creature.getHitched() != null) {
/*      */               
/*  896 */               Seat s = creature.getHitched().getHitchSeatFor(creature.getWurmId());
/*  897 */               if (s != null) {
/*  898 */                 sendAttachCreature(creatureId, (creature.getHitched()).wurmid, s.offx, s.offy, s.offz, 0);
/*      */               }
/*      */             } 
/*  901 */             if (creature.getTarget() != null)
/*      */             {
/*  903 */               this.watcher.getCommunicator().sendHasTarget(creature.getWurmId(), true);
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  911 */             if (creature.isRidden()) {
/*      */               
/*  913 */               Vehicle vehic = Vehicles.getVehicleForId(creatureId);
/*  914 */               if (vehic != null) {
/*      */                 
/*  916 */                 Seat[] seats = vehic.getSeats();
/*  917 */                 for (int x = 0; x < seats.length; x++) {
/*      */                   
/*  919 */                   if (seats[x].isOccupied()) {
/*      */                     
/*  921 */                     if (!this.creatures.containsKey(Long.valueOf((seats[x]).occupant))) {
/*      */                       
/*      */                       try {
/*      */                         
/*  925 */                         addCreature((seats[x]).occupant, true);
/*      */                       }
/*  927 */                       catch (NoSuchCreatureException nsc) {
/*      */                         
/*  929 */                         logger.log(Level.INFO, nsc.getMessage(), (Throwable)nsc);
/*      */                       }
/*  931 */                       catch (NoSuchPlayerException nsp) {
/*      */                         
/*  933 */                         logger.log(Level.INFO, nsp.getMessage(), (Throwable)nsp);
/*      */                       } 
/*      */                     }
/*  936 */                     sendAttachCreature((seats[x]).occupant, creatureId, (seats[x]).offx, (seats[x]).offy, (seats[x]).offz, x);
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */             
/*  942 */             return true;
/*      */           }
/*      */         
/*      */         }
/*      */       }
/*      */     } else {
/*      */       
/*  949 */       removeCreature(creature);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  970 */     return false; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendCreatureItems(Creature creature) {
/*  975 */     if (creature.isPlayer()) {
/*      */ 
/*      */       
/*      */       try {
/*  979 */         Item lTempItem = creature.getEquippedWeapon((byte)37);
/*  980 */         if (lTempItem != null && !lTempItem.isBodyPartAttached())
/*      */         {
/*  982 */           sendWieldItem((creature.getWurmId() == this.watcher.getWurmId()) ? -1L : creature.getWurmId(), (byte)0, lTempItem
/*  983 */               .getModelName(), lTempItem.getRarity(), 
/*  984 */               WurmColor.getColorRed(lTempItem.getColor()), WurmColor.getColorGreen(lTempItem.getColor()), WurmColor.getColorBlue(lTempItem.getColor()), 
/*  985 */               WurmColor.getColorRed(lTempItem.getColor2()), WurmColor.getColorGreen(lTempItem.getColor2()), WurmColor.getColorBlue(lTempItem.getColor2()));
/*      */         }
/*      */       }
/*  988 */       catch (NoSpaceException nsp) {
/*      */         
/*  990 */         logger.log(Level.WARNING, creature
/*  991 */             .getName() + " could not get equipped weapon for left hand due to " + nsp.getMessage(), (Throwable)nsp);
/*      */       } 
/*      */       
/*      */       try {
/*  995 */         Item lTempItem = creature.getEquippedWeapon((byte)38);
/*  996 */         if (lTempItem != null && !lTempItem.isBodyPartAttached())
/*      */         {
/*  998 */           sendWieldItem((creature.getWurmId() == this.watcher.getWurmId()) ? -1L : creature.getWurmId(), (byte)1, lTempItem
/*  999 */               .getModelName(), lTempItem.getRarity(), 
/* 1000 */               WurmColor.getColorRed(lTempItem.getColor()), WurmColor.getColorGreen(lTempItem.getColor()), WurmColor.getColorBlue(lTempItem.getColor()), 
/* 1001 */               WurmColor.getColorRed(lTempItem.getColor2()), WurmColor.getColorGreen(lTempItem.getColor2()), WurmColor.getColorBlue(lTempItem.getColor2()));
/*      */         }
/*      */       }
/* 1004 */       catch (NoSpaceException nsp) {
/*      */         
/* 1006 */         logger.log(Level.WARNING, creature
/* 1007 */             .getName() + " could not get equipped weapon for right hand due to " + nsp.getMessage(), (Throwable)nsp);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void newLayer(Creature creature, boolean tileIsSurfaced) {
/* 1014 */     if (creature != null && this.watcher.getWurmId() != creature.getWurmId()) {
/*      */       
/* 1016 */       if (this.creatures.containsKey(Long.valueOf(creature.getWurmId())))
/*      */       {
/* 1018 */         if (this.watcher.hasLink())
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1024 */           this.watcher.getCommunicator().sendCreatureChangedLayer(creature.getWurmId(), (byte)creature.getLayer());
/*      */         }
/* 1026 */         if (isOnSurface())
/*      */         {
/* 1028 */           if (this.watcher.getVisionArea().getUnderGround() != null && this.watcher.getVisionArea().getUnderGround().coversCreature(creature)) {
/*      */ 
/*      */             
/* 1031 */             CreatureMove cm = this.creatures.remove(Long.valueOf(creature.getWurmId()));
/*      */             
/* 1033 */             addToVisionArea(creature, cm, this.watcher.getVisionArea().getUnderGround());
/*      */           } else {
/*      */ 
/*      */             
/*      */             try {
/* 1038 */               deleteCreature(creature, true);
/*      */             }
/* 1040 */             catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */             
/*      */             }
/* 1044 */             catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/*      */         else
/*      */         {
/* 1052 */           CreatureMove cm = this.creatures.remove(Long.valueOf(creature.getWurmId()));
/* 1053 */           addToVisionArea(creature, cm, this.watcher.getVisionArea().getSurface());
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1061 */     else if (creature != null && this.watcher.getWurmId() == creature.getWurmId()) {
/*      */ 
/*      */       
/* 1064 */       if (this.watcher.getVehicle() != -10L && !this.watcher.isVehicleCommander())
/*      */       {
/* 1066 */         this.watcher.getCommunicator().sendCreatureChangedLayer(-1L, (byte)creature.getLayer());
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
/*      */   public void justSendNewLayer(Item item) {
/* 1079 */     if (this.watcher.getVehicle() != item.getWurmId())
/*      */     {
/* 1081 */       this.watcher.getCommunicator().sendCreatureChangedLayer(item.getWurmId(), item.newLayer);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void addToVisionArea(Creature creature, CreatureMove cm, VirtualZone newzone) {
/* 1087 */     newzone.addCreatureToMap(creature, cm);
/* 1088 */     if (creature.isRidden()) {
/*      */       
/* 1090 */       Set<Long> riders = creature.getRiders();
/* 1091 */       for (Long rider : riders) {
/*      */         
/* 1093 */         cm = this.creatures.remove(Long.valueOf(rider.longValue()));
/*      */         
/*      */         try {
/* 1096 */           newzone.addCreature(rider.longValue(), true);
/*      */         }
/* 1098 */         catch (Exception nex) {
/*      */           
/* 1100 */           logger.log(Level.WARNING, nex.getMessage(), nex);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void newLayer(Item vehicle) {
/* 1108 */     if (vehicle != null)
/*      */     {
/* 1110 */       if (this.items != null && this.items.contains(vehicle)) {
/*      */         
/* 1112 */         byte newlayer = vehicle.isOnSurface() ? 0 : -1;
/* 1113 */         if (vehicle.newLayer != Byte.MIN_VALUE)
/* 1114 */           newlayer = vehicle.newLayer; 
/* 1115 */         if (this.watcher.hasLink()) {
/* 1116 */           this.watcher.getCommunicator().sendCreatureChangedLayer(vehicle.getWurmId(), newlayer);
/*      */         }
/* 1118 */         if (newlayer < 0) {
/*      */           
/* 1120 */           if (this.watcher.getVisionArea().getUnderGround() != null && this.watcher.getVisionArea().getUnderGround().covers(vehicle.getTileX(), vehicle.getTileY()))
/*      */           {
/* 1122 */             this.watcher.getVisionArea().getUnderGround().addItem(vehicle, null, true);
/*      */ 
/*      */ 
/*      */           
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/*      */ 
/*      */             
/* 1132 */             removeItem(vehicle);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1137 */           this.watcher.getVisionArea().getSurface().addItem(vehicle, null, true);
/*      */         } 
/* 1139 */         this.items.remove(vehicle);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void addCreatureToMap(Creature creature, CreatureMove cm) {
/* 1146 */     if (cm != null) {
/* 1147 */       this.creatures.put(Long.valueOf(creature.getWurmId()), cm);
/*      */     }
/*      */   }
/*      */   
/*      */   public void checkForEnemies() {
/* 1152 */     for (Long cid : this.creatures.keySet()) {
/*      */       
/* 1154 */       if (this.watcher.target == -10L) {
/*      */ 
/*      */         
/*      */         try {
/* 1158 */           Creature creature = Server.getInstance().getCreature(cid.longValue());
/* 1159 */           checkIfAttack(creature, cid.longValue());
/*      */         }
/* 1161 */         catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */         
/* 1164 */         } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */         continue;
/*      */       } 
/*      */       return;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkIfAttack(Creature creature, long creatureId) {
/* 1175 */     if (this.watcher.getTemplate().getCreatureAI() != null)
/*      */     {
/* 1177 */       if (this.watcher.getTemplate().getCreatureAI().maybeAttackCreature(this.watcher, this, creature)) {
/*      */         return;
/*      */       }
/*      */     }
/* 1181 */     if (creature.isTransferring())
/*      */       return; 
/* 1183 */     if (this.watcher.isPlayer()) {
/*      */       
/* 1185 */       if (creature.addingAfterTeleport && this.watcher.lastOpponent == creature)
/*      */       {
/* 1187 */         if (creature.isWithinDistanceTo(this.watcher.getPosX(), this.watcher.getPosY(), this.watcher
/* 1188 */             .getPositionZ() + this.watcher.getAltOffZ(), 12.0F))
/*      */         {
/* 1190 */           this.watcher.setTarget(creatureId, false);
/*      */         }
/*      */       }
/*      */       
/*      */       return;
/*      */     } 
/* 1196 */     if (creature.isNpc() && this.watcher.isNpc() && 
/* 1197 */       creature.getAttitude(this.watcher) != 2)
/*      */       return; 
/* 1199 */     if (creature.getLayer() == this.watcher.getLayer() || this.watcher.isKingdomGuard() || this.watcher.isUnique() || this.watcher.isWarGuard()) {
/*      */       
/* 1201 */       if (creature.fleeCounter > 0)
/*      */         return; 
/* 1203 */       if (creature.getVehicle() > -10L && this.watcher.isNoAttackVehicles())
/*      */         return; 
/* 1205 */       if (creature.getCultist() != null)
/*      */       {
/* 1207 */         if (creature.getCultist().hasFearEffect() || creature.getCultist().hasLoveEffect())
/*      */           return; 
/*      */       }
/* 1210 */       if (!creature.isWithinDistanceTo(this.watcher.getPosX(), this.watcher.getPosY(), this.watcher
/* 1211 */           .getPositionZ() + this.watcher.getAltOffZ(), (this.watcher
/* 1212 */           .isSpiritGuard() || this.watcher.isKingdomGuard() || this.watcher.isWarGuard() || this.watcher.isUnique()) ? 30.0F : 12.0F))
/*      */       {
/* 1214 */         if (!isCreatureTurnedTowardsTarget(creature, this.watcher) && !this.watcher.isKingdomGuard() && 
/* 1215 */           !this.watcher.isWarGuard())
/*      */           return; 
/*      */       }
/* 1218 */       if (creature.isBridgeBlockingAttack(this.watcher, true))
/*      */         return; 
/* 1220 */       if (this.watcher.getAttitude(creature) == 2) {
/*      */         
/* 1222 */         if (this.watcher.target == -10L) {
/*      */           
/* 1224 */           if (this.watcher.isKingdomGuard()) {
/*      */ 
/*      */             
/* 1227 */             if (creature.getCurrentTile().getKingdom() == this.watcher.getKingdomId() || this.watcher
/* 1228 */               .getKingdomId() == 0) {
/*      */               
/* 1230 */               GuardTower gt = Kingdoms.getTower(this.watcher);
/* 1231 */               if (gt != null) {
/*      */                 
/* 1233 */                 int tpx = gt.getTower().getTileX();
/* 1234 */                 int tpy = gt.getTower().getTileY();
/* 1235 */                 if (creature.isWithinTileDistanceTo(tpx, tpy, (int)gt.getTower().getPosZ(), 50))
/*      */                 {
/* 1237 */                   if (creature.isRidden()) {
/*      */                     
/* 1239 */                     if (Server.rand.nextInt(50) == 0) {
/* 1240 */                       this.watcher.setTarget(creatureId, false);
/*      */                     }
/* 1242 */                   } else if (creature.isPlayer() || creature.isDominated()) {
/*      */                     
/* 1244 */                     this.watcher.setTarget(creatureId, false);
/*      */                   }
/* 1246 */                   else if (this.watcher.getAlertSeconds() > 0) {
/*      */                     
/* 1248 */                     if (creature.isAggHuman()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                       
/* 1255 */                       this.watcher.setTarget(creatureId, false);
/* 1256 */                       if (this.watcher.target == creatureId)
/* 1257 */                         GuardTower.yellHunt(this.watcher, creature, false); 
/*      */                     } 
/*      */                   } 
/*      */                 }
/*      */               } 
/*      */             } 
/*      */             return;
/*      */           } 
/* 1265 */           if (this.watcher.isWarGuard()) {
/*      */             
/* 1267 */             Item target = Kingdoms.getClosestWarTarget(this.watcher.getTileX(), this.watcher.getTileY(), this.watcher);
/* 1268 */             if (target != null)
/*      */             {
/* 1270 */               if (this.watcher.isWithinTileDistanceTo(target.getTileX(), target.getTileY(), 0, 15))
/*      */               {
/* 1272 */                 if (creature.isWithinTileDistanceTo(target.getTileX(), target.getTileY(), 0, 5)) {
/*      */                   
/* 1274 */                   this.watcher.setTarget(creatureId, false);
/*      */                   
/*      */                   return;
/*      */                 } 
/*      */               }
/*      */             }
/* 1280 */           } else if (this.watcher.isDominated()) {
/*      */             
/* 1282 */             this.watcher.setTarget(creatureId, false);
/*      */           } 
/* 1284 */           if (!this.watcher.isSpiritGuard())
/*      */           {
/* 1286 */             if (creature.isRidden() && Server.rand.nextInt(10) == 0) {
/*      */               
/* 1288 */               this.watcher.setTarget(creatureId, false);
/*      */             }
/* 1290 */             else if (creature.isDominated() && Server.rand.nextInt(10) == 0) {
/*      */               
/* 1292 */               this.watcher.setTarget(creatureId, false);
/*      */             } 
/*      */           }
/* 1295 */           if (creature instanceof Player && this.watcher.isAggHuman()) {
/*      */             
/* 1297 */             if (!creature.hasLink())
/*      */               return; 
/* 1299 */             if (creature.getSpellEffects() != null && 
/* 1300 */               creature.getSpellEffects().getSpellEffect((byte)73) != null)
/*      */             {
/*      */               
/* 1303 */               if (!creature.isWithinDistanceTo(this.watcher, 7.0F))
/*      */                 return; 
/*      */             }
/* 1306 */             if (creature.addingAfterTeleport || Server.rand
/* 1307 */               .nextInt(100) <= this.watcher.getAggressivity() * this.watcher
/* 1308 */               .getStatus().getAggTypeModifier())
/*      */             {
/* 1310 */               this.watcher.setTarget(creatureId, false);
/*      */             }
/*      */           }
/* 1313 */           else if (this.watcher.isAggHuman() && creature.isKingdomGuard()) {
/*      */             
/* 1315 */             if (creature.addingAfterTeleport || (creature
/* 1316 */               .getAlertSeconds() > 0 && Server.rand.nextInt((int)Math.max(1.0F, this.watcher
/* 1317 */                   .getAggressivity() * this.watcher.getStatus().getAggTypeModifier())) == 0))
/*      */             {
/* 1319 */               this.watcher.setTarget(creatureId, false);
/*      */             }
/*      */           }
/* 1322 */           else if (this.watcher.isAggHuman() && creature.isSpiritGuard()) {
/*      */             
/* 1324 */             if (creature.getCitizenVillage() != null)
/*      */             {
/* 1326 */               if (!creature.getCitizenVillage().isEnemy(this.watcher))
/*      */                 return; 
/* 1328 */               if (creature.addingAfterTeleport || Server.rand
/* 1329 */                 .nextInt((int)Math.max(1.0F, this.watcher.getAggressivity() * this.watcher
/* 1330 */                     .getStatus().getAggTypeModifier())) == 0)
/*      */               {
/* 1332 */                 this.watcher.setTarget(creatureId, false);
/*      */               }
/*      */             }
/*      */           
/* 1336 */           } else if (this.watcher.isSpiritGuard()) {
/*      */             
/* 1338 */             if (this.watcher.getCitizenVillage() == null) {
/*      */ 
/*      */               
/* 1341 */               if (creature.addingAfterTeleport || Server.rand.nextInt(100) <= 80)
/*      */               {
/* 1343 */                 this.watcher.setTarget(creatureId, false);
/*      */               
/*      */               }
/*      */             
/*      */             }
/* 1348 */             else if (creature.isRidden()) {
/*      */               
/* 1350 */               if (!this.watcher.getCitizenVillage().isEnemy(creature))
/*      */                 return; 
/* 1352 */               if (Server.rand.nextInt(100) == 0) {
/* 1353 */                 this.watcher.setTarget(creatureId, false);
/*      */               }
/* 1355 */             } else if ((creature.isPlayer() || creature.isBreakFence() || creature.isDominated()) && 
/* 1356 */               this.watcher.getCitizenVillage().isWithinAttackPerimeter(creature.getTileX(), creature
/* 1357 */                 .getTileY())) {
/*      */               
/* 1359 */               if (!this.watcher.getCitizenVillage().isEnemy(creature)) {
/*      */                 return;
/*      */               }
/*      */               
/* 1363 */               this.watcher.setTarget(creatureId, false);
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 1368 */             this.watcher.setTarget(creatureId, false);
/*      */           } 
/*      */         } 
/* 1371 */       } else if (this.watcher.getTemplate().getLeaderTemplateId() > 0) {
/*      */         
/* 1373 */         if (this.watcher.leader == null && !this.watcher.isDominated())
/*      */         {
/* 1375 */           if (creature.getTemplate().getTemplateId() == this.watcher.getTemplate().getLeaderTemplateId())
/*      */           {
/* 1377 */             if (!this.watcher.isHerbivore() || !this.watcher.isHungry())
/*      */             {
/* 1379 */               if (creature.getPositionZ() >= -0.71D || (creature
/* 1380 */                 .isSwimming() && this.watcher.isSwimming()))
/*      */               {
/* 1382 */                 if (creature.mayLeadMoreCreatures()) {
/*      */                   
/* 1384 */                   creature.addFollower(this.watcher, null);
/* 1385 */                   this.watcher.setLeader(creature);
/*      */                 } 
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void addAreaSpellEffect(AreaSpellEffect effect, boolean loop) {
/* 1397 */     if (effect != null) {
/*      */       
/* 1399 */       if (this.areaEffects == null)
/* 1400 */         this.areaEffects = new HashSet<>(); 
/* 1401 */       if (!this.areaEffects.contains(effect)) {
/*      */         
/* 1403 */         this.areaEffects.add(effect);
/* 1404 */         if (this.watcher.hasLink())
/*      */         {
/* 1406 */           this.watcher.getCommunicator().sendAddAreaSpellEffect(effect.getTilex(), effect.getTiley(), effect.getLayer(), effect
/* 1407 */               .getType(), effect.getFloorLevel(), effect.getHeightOffset(), loop);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void removeAreaSpellEffect(AreaSpellEffect effect) {
/* 1415 */     if (this.areaEffects != null && this.areaEffects.contains(effect)) {
/*      */       
/* 1417 */       this.areaEffects.remove(effect);
/* 1418 */       if (effect != null && this.watcher.hasLink()) {
/* 1419 */         this.watcher.getCommunicator().sendRemoveAreaSpellEffect(effect.getTilex(), effect.getTiley(), effect.getLayer());
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void addEffect(Effect effect, boolean temp) {
/* 1425 */     if (this.effects == null)
/* 1426 */       this.effects = new HashSet<>(); 
/* 1427 */     if (!this.effects.contains(effect) || temp) {
/*      */ 
/*      */ 
/*      */       
/* 1431 */       if (!temp && (WurmId.getType(effect.getOwner()) == 2 || 
/* 1432 */         WurmId.getType(effect.getOwner()) == 6)) {
/*      */         
/*      */         try {
/*      */           
/* 1436 */           Item effectHolder = Items.getItem(effect.getOwner());
/* 1437 */           if (this.items == null || !this.items.contains(effectHolder)) {
/*      */             return;
/*      */           }
/* 1440 */         } catch (NoSuchItemException nsi) {
/*      */           return;
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/* 1446 */       if (!temp) {
/* 1447 */         this.effects.add(effect);
/*      */       }
/* 1449 */       if (this.watcher.hasLink()) {
/* 1450 */         this.watcher.getCommunicator().sendAddEffect(effect.getOwner(), effect.getType(), effect.getPosX(), effect
/* 1451 */             .getPosY(), effect.getPosZ(), effect.getLayer(), effect.getEffectString(), effect
/* 1452 */             .getTimeout(), effect.getRotationOffset());
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void removeEffect(Effect effect) {
/* 1458 */     if (this.effects != null && this.effects.contains(effect)) {
/*      */       
/* 1460 */       this.effects.remove(effect);
/* 1461 */       if (this.watcher.hasLink()) {
/* 1462 */         this.watcher.getCommunicator().sendRemoveEffect(effect.getOwner());
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void removeCreature(Creature creature) {
/* 1470 */     if (!coversCreature(creature) || creature.isLoggedOut()) {
/*      */       
/* 1472 */       if (!this.watcher.isSpiritGuard() && creature.getWurmId() == this.watcher.target)
/* 1473 */         this.watcher.setTarget(-10L, true); 
/* 1474 */       if (this.creatures != null && this.creatures.keySet().contains(Long.valueOf(creature.getWurmId()))) {
/*      */         
/* 1476 */         this.creatures.remove(Long.valueOf(creature.getWurmId()));
/* 1477 */         checkIfEnemyIsPresent(false);
/*      */         
/* 1479 */         if (creature.getCurrentTile() == null || !creature.getCurrentTile().isVisibleToPlayers())
/*      */         {
/* 1481 */           creature.setVisibleToPlayers(false);
/*      */         }
/* 1483 */         if (this.watcher.hasLink()) {
/*      */           
/* 1485 */           this.watcher.getCommunicator().sendDeleteCreature(creature.getWurmId());
/* 1486 */           if (creature instanceof Player || creature.isNpc()) {
/* 1487 */             this.watcher.getCommunicator().sendRemoveLocal(creature.getName());
/*      */           }
/* 1489 */           if (this.watcher.isPlayer()) {
/*      */             
/* 1491 */             Vehicle vehic = Vehicles.getVehicleForId(creature.getWurmId());
/* 1492 */             if (vehic != null) {
/*      */               
/* 1494 */               Seat[] seats = vehic.getSeats();
/* 1495 */               for (int x = 0; x < seats.length; x++) {
/*      */                 
/* 1497 */                 if (seats[x].isOccupied()) {
/*      */                   
/*      */                   try {
/*      */                     
/* 1501 */                     Creature occ = Server.getInstance().getCreature((seats[x]).occupant);
/* 1502 */                     this.watcher.getCommunicator().sendRemoveLocal(occ.getName());
/*      */                   }
/* 1504 */                   catch (NoSuchCreatureException nsc) {
/*      */                     
/* 1506 */                     logger.log(Level.WARNING, nsc.getMessage(), (Throwable)nsc);
/*      */                   }
/* 1508 */                   catch (NoSuchPlayerException nsp) {
/*      */                     
/* 1510 */                     logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp);
/*      */                   } 
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean checkIfEnemyIsPresent(boolean checkedFromOtherVirtualZone) {
/* 1523 */     if (this.watcher.isPlayer() && !this.watcher.isTeleporting())
/*      */     {
/* 1525 */       if (this.watcher.hasLink() && this.watcher.getVisionArea() != null && this.watcher.getVisionArea().isInitialized()) {
/*      */         
/* 1527 */         boolean foundEnemy = false;
/* 1528 */         if (this.creatures != null) {
/*      */           
/* 1530 */           Long[] crets = getCreatures();
/* 1531 */           for (int x = 0; x < crets.length; x++) {
/*      */             
/* 1533 */             if (WurmId.getType(crets[x].longValue()) == 0) {
/*      */               
/*      */               try {
/*      */                 
/* 1537 */                 Creature creature = Server.getInstance().getCreature(crets[x].longValue());
/* 1538 */                 if (this.watcher.getKingdomId() != 0 && creature.getKingdomId() != 0 && 
/* 1539 */                   !this.watcher.isFriendlyKingdom(creature.getKingdomId())) {
/*      */                   
/* 1541 */                   if (creature.getPower() < 2 && this.watcher.getPower() < 2 && 
/* 1542 */                     creature.getFightingSkill().getRealKnowledge() > 20.0D) {
/* 1543 */                     foundEnemy = true;
/*      */                   }
/* 1545 */                 } else if (this.watcher.getCitizenVillage() != null && this.watcher.getCitizenVillage().isEnemy(creature)) {
/*      */                   
/* 1547 */                   if (creature.getPower() < 2 && this.watcher.getPower() < 2 && 
/* 1548 */                     creature.getFightingSkill().getRealKnowledge() > 20.0D) {
/* 1549 */                     foundEnemy = true;
/*      */                   }
/*      */                 } 
/* 1552 */               } catch (Exception exception) {}
/*      */             }
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 1558 */         if (!foundEnemy) {
/*      */ 
/*      */ 
/*      */           
/* 1562 */           if (!checkedFromOtherVirtualZone) {
/*      */             
/* 1564 */             boolean found = false;
/* 1565 */             if (this.watcher.getVisionArea() != null)
/*      */             {
/* 1567 */               if (this.isOnSurface) {
/* 1568 */                 found = this.watcher.getVisionArea().getUnderGround().checkIfEnemyIsPresent(true);
/*      */               } else {
/* 1570 */                 found = this.watcher.getVisionArea().getSurface().checkIfEnemyIsPresent(true);
/*      */               }  } 
/* 1572 */             if (!found)
/* 1573 */               this.watcher.removeEnemyPresense(); 
/* 1574 */             return found;
/*      */           } 
/* 1576 */           return false;
/*      */         } 
/*      */       } 
/*      */     }
/* 1580 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   void makeInvisible(Creature creature) {
/* 1585 */     if (this.creatures != null && this.creatures.keySet().contains(new Long(creature.getWurmId()))) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1590 */       this.creatures.remove(new Long(creature.getWurmId()));
/* 1591 */       checkIfEnemyIsPresent(false);
/* 1592 */       if (this.watcher.hasLink())
/* 1593 */         this.watcher.getCommunicator().sendDeleteCreature(creature.getWurmId()); 
/* 1594 */       if (creature instanceof Player || creature.isNpc()) {
/* 1595 */         this.watcher.getCommunicator().sendRemoveLocal(creature.getName());
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
/*      */   private boolean coversCreature(Creature creature) {
/* 1609 */     if (creature.isDead())
/* 1610 */       return false; 
/* 1611 */     if (creature == this.watcher) {
/* 1612 */       return true;
/*      */     }
/* 1614 */     if (creature.isPlayer() && Servers.localServer.PVPSERVER) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1620 */       if (this.watcher.isOnSurface() && creature.isOnSurface() && this.watcher
/* 1621 */         .isWithinDistanceTo(creature.getTileX(), creature.getTileY(), 80))
/*      */       {
/* 1623 */         return true;
/*      */       }
/*      */       
/* 1626 */       if (!this.watcher.isOnSurface() && creature.isOnSurface() && this.watcher
/* 1627 */         .isWithinDistanceTo(creature.getTileX(), creature.getTileY(), caveToSurfaceLocalDistance))
/*      */       {
/* 1629 */         return true;
/*      */       }
/*      */       
/* 1632 */       if (this.watcher.isOnSurface() && !creature.isOnSurface() && this.watcher
/* 1633 */         .isWithinDistanceTo(creature.getTileX(), creature.getTileY(), 20))
/*      */       {
/* 1635 */         return true;
/*      */       }
/*      */       
/* 1638 */       if (!this.watcher.isOnSurface() && !creature.isOnSurface() && this.watcher
/* 1639 */         .isWithinDistanceTo(creature.getTileX(), creature.getTileY(), 20))
/*      */       {
/* 1641 */         return true;
/*      */       }
/*      */       
/* 1644 */       return false;
/*      */     } 
/* 1646 */     return covers(creature.getTileX(), creature.getTileY());
/*      */   }
/*      */ 
/*      */   
/*      */   public void moveAllCreatures() {
/* 1651 */     Map.Entry[] arrayOfEntry = (Map.Entry[])this.creatures.entrySet().toArray((Object[])new Map.Entry[this.creatures.size()]);
/*      */     
/* 1653 */     for (int x = 0; x < arrayOfEntry.length; x++) {
/*      */       
/* 1655 */       if (((CreatureMove)arrayOfEntry[x].getValue()).timestamp != 0L) {
/*      */         
/*      */         try {
/*      */           
/* 1659 */           Creature creature = Server.getInstance().getCreature(((Long)arrayOfEntry[x].getKey()).longValue());
/* 1660 */           if (!isMovingSameLevel(creature) || Structure.isGroundFloorAtPosition(creature.getPosX(), creature.getPosY(), creature.isOnSurface())) {
/* 1661 */             this.watcher.getCommunicator().sendMoveCreatureAndSetZ(((Long)arrayOfEntry[x].getKey()).longValue(), creature.getPosX(), creature
/* 1662 */                 .getPosY(), creature.getPositionZ(), ((CreatureMove)arrayOfEntry[x].getValue()).rotation);
/*      */           } else {
/* 1664 */             this.watcher.getCommunicator().sendMoveCreature(((Long)arrayOfEntry[x].getKey()).longValue(), creature.getPosX(), creature
/* 1665 */                 .getPosY(), ((CreatureMove)arrayOfEntry[x].getValue()).rotation, creature.isMoving());
/* 1666 */           }  clearCreatureMove(creature, arrayOfEntry[x].getValue());
/*      */         }
/* 1668 */         catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */         
/* 1671 */         } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean isMovingSameLevel(Creature creature) {
/* 1680 */     return (creature.getBridgeId() <= 0L && ((creature
/* 1681 */       .isPlayer() && (creature.getMovementScheme()).onGround) || !creature.isSubmerged()) && creature
/* 1682 */       .getFloorLevel() <= 0 && creature.getMovementScheme().getGroundOffset() == 0.0F);
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
/*      */   boolean creatureMoved(long creatureId, float diffX, float diffY, float diffZ, int diffTileX, int diffTileY) throws NoSuchCreatureException, NoSuchPlayerException {
/* 1700 */     if (this.watcher == null || (this.watcher.isPlayer() && !this.watcher.hasLink())) {
/* 1701 */       return true;
/*      */     }
/* 1703 */     Creature creature = Server.getInstance().getCreature(creatureId);
/* 1704 */     if (this.watcher.equals(creature)) {
/*      */       
/* 1706 */       if (this.watcher.getPower() > 2 && this.watcher.loggerCreature1 != -10L) {
/* 1707 */         this.watcher.getCommunicator().sendAck(this.watcher.getPosX(), this.watcher.getPosY());
/*      */       }
/*      */       
/* 1710 */       if (this.watcher.isPlayer() && (
/* 1711 */         diffTileX != 0 || diffTileY != 0)) {
/* 1712 */         getStructuresWithinDistance(5);
/*      */       }
/* 1714 */       return false;
/*      */     } 
/* 1716 */     if (!coversCreature(creature)) {
/*      */       
/* 1718 */       removeCreature(creature);
/* 1719 */       return false;
/*      */     } 
/* 1721 */     if (!creature.isVisibleTo(this.watcher)) {
/* 1722 */       return false;
/*      */     }
/* 1724 */     if (!addCreature(creatureId, false))
/*      */     {
/* 1726 */       if (!this.watcher.isPlayer()) {
/*      */         
/* 1728 */         this.watcher.creatureMoved(creature, diffX, diffY, diffZ);
/*      */       }
/* 1730 */       else if (this.watcher.hasLink()) {
/*      */         
/* 1732 */         if (creature.isPlayer()) {
/*      */           
/* 1734 */           Set<MovementEntity> illusions = Creature.getIllusionsFor(creatureId);
/* 1735 */           if (illusions != null)
/*      */           {
/* 1737 */             for (MovementEntity e : illusions) {
/*      */               
/* 1739 */               this.watcher.getCommunicator().sendMoveCreature(e.getWurmid(), creature
/* 1740 */                   .getPosX() + (e.getMovePosition()).diffX, creature.getPosY() + (e.getMovePosition()).diffY, 
/* 1741 */                   (int)(creature.getStatus().getRotation() * 256.0F / 360.0F), true);
/*      */               
/* 1743 */               if (e.shouldExpire()) {
/* 1744 */                 this.watcher.getCommunicator().sendDeleteCreature(e.getWurmid());
/*      */               }
/*      */             } 
/*      */           }
/*      */         } 
/* 1749 */         CreatureMove cmove = this.creatures.get(new Long(creatureId));
/* 1750 */         boolean moveSameLevel = isMovingSameLevel(creature);
/* 1751 */         if (diffX != 0.0F || diffY != 0.0F || diffZ != 0.0F) {
/*      */ 
/*      */           
/* 1754 */           this.MOVELIMIT = Math.max(0.05F, Math.min(0.7F, Creature.rangeTo(creature, this.watcher) / 100.0F));
/* 1755 */           if (Math.abs(cmove.diffX + diffX) > this.MOVELIMIT || Math.abs(cmove.diffY + diffY) > this.MOVELIMIT || 
/* 1756 */             Math.abs(cmove.diffZ + diffZ) > this.MOVELIMIT) {
/*      */             
/* 1758 */             if (!moveSameLevel || Structure.isGroundFloorAtPosition(creature.getPosX(), creature.getPosY(), creature.isOnSurface())) {
/* 1759 */               this.watcher.getCommunicator().sendMoveCreatureAndSetZ(creatureId, creature.getPosX(), creature.getPosY(), creature.getPositionZ(), cmove.rotation);
/*      */             } else {
/* 1761 */               this.watcher.getCommunicator().sendMoveCreature(creatureId, creature.getPosX(), creature.getPosY(), cmove.rotation, creature.isMoving());
/*      */             } 
/* 1763 */             cmove.resetXYZ();
/* 1764 */             cmove.timestamp = System.currentTimeMillis();
/* 1765 */             cmove.rotation = (int)(creature.getStatus().getRotation() * 256.0F / 360.0F);
/*      */           }
/* 1767 */           else if (creature.getAttitude(this.watcher) == 2 || Math.abs(diffZ) > 0.3F) {
/*      */             
/* 1769 */             if (creature.isSubmerged() && creature.getPositionZ() < 0.0F)
/*      */             {
/* 1771 */               this.watcher.getCommunicator().sendMoveCreatureAndSetZ(creatureId, creature.getPosX(), creature.getPosY(), creature.getPositionZ(), 
/* 1772 */                   (int)(creature.getStatus().getRotation() * 256.0F / 360.0F));
/* 1773 */               clearCreatureMove(creature, cmove);
/*      */             }
/*      */             else
/*      */             {
/* 1777 */               if (!moveSameLevel || Structure.isGroundFloorAtPosition(creature.getPosX(), creature.getPosY(), creature.isOnSurface())) {
/* 1778 */                 this.watcher.getCommunicator().sendMoveCreatureAndSetZ(creatureId, creature.getPosX(), creature.getPosY(), creature.getPositionZ(), 
/* 1779 */                     (int)(creature.getStatus().getRotation() * 256.0F / 360.0F));
/*      */               } else {
/* 1781 */                 this.watcher.getCommunicator().sendMoveCreature(creatureId, creature.getPosX(), creature.getPosY(), 
/* 1782 */                     (int)(creature.getStatus().getRotation() * 256.0F / 360.0F), creature.isMoving());
/*      */               } 
/* 1784 */               clearCreatureMove(creature, cmove);
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 1789 */             cmove.timestamp = System.currentTimeMillis();
/* 1790 */             cmove.diffX += diffX;
/* 1791 */             cmove.diffY += diffY;
/* 1792 */             cmove.diffZ += diffZ;
/* 1793 */             cmove.rotation = (int)(creature.getStatus().getRotation() * 256.0F / 360.0F);
/*      */           
/*      */           }
/*      */         
/*      */         }
/* 1798 */         else if (creature.getAttitude(this.watcher) == 2) {
/*      */           
/* 1800 */           if (!moveSameLevel || Structure.isGroundFloorAtPosition(creature.getPosX(), creature.getPosY(), creature.isOnSurface())) {
/* 1801 */             this.watcher.getCommunicator().sendMoveCreatureAndSetZ(creatureId, creature.getPosX(), creature.getPosY(), creature.getPositionZ(), 
/* 1802 */                 (int)(creature.getStatus().getRotation() * 256.0F / 360.0F));
/*      */           } else {
/* 1804 */             this.watcher.getCommunicator().sendMoveCreature(creatureId, creature.getPosX(), creature.getPosY(), 
/* 1805 */                 (int)(creature.getStatus().getRotation() * 256.0F / 360.0F), creature.isMoving());
/*      */           } 
/* 1807 */           clearCreatureMove(creature, cmove);
/*      */         }
/*      */         else {
/*      */           
/* 1811 */           cmove.timestamp = System.currentTimeMillis();
/* 1812 */           cmove.rotation = (int)(creature.getStatus().getRotation() * 256.0F / 360.0F);
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 1818 */     if (diffTileX != 0 || diffTileY != 0) {
/*      */       
/* 1820 */       checkIfAttack(creature, creatureId);
/* 1821 */       if (creature.getVehicle() != -10L) {
/*      */         
/*      */         try {
/*      */           
/* 1825 */           Item itemVehicle = Items.getItem(creature.getVehicle());
/* 1826 */           Vehicle vehicle = Vehicles.getVehicle(itemVehicle);
/* 1827 */           for (Seat seat : vehicle.getSeats())
/*      */           {
/* 1829 */             PlayerInfo oInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(seat.getOccupant());
/* 1830 */             if (oInfo != null) {
/*      */               
/*      */               try {
/*      */                 
/* 1834 */                 Player oPlayer = Players.getInstance().getPlayer(oInfo.wurmId);
/* 1835 */                 if (oPlayer.hasLink())
/*      */                 {
/* 1837 */                   checkIfAttack((Creature)oPlayer, oPlayer.getWurmId());
/*      */                 }
/*      */               }
/* 1840 */               catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */             
/*      */             }
/*      */           }
/*      */         
/*      */         }
/* 1846 */         catch (NoSuchItemException noSuchItemException) {}
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1852 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void clearCreatureMove(Creature creature, CreatureMove cmove) {
/* 1857 */     cmove.timestamp = 0L;
/* 1858 */     cmove.diffX = 0.0F;
/* 1859 */     cmove.diffY = 0.0F;
/* 1860 */     cmove.diffZ = 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public void clearMovementForCreature(long creatureId) {
/* 1865 */     CreatureMove cmove = this.creatures.get(Long.valueOf(creatureId));
/* 1866 */     if (cmove != null) {
/*      */       
/* 1868 */       cmove.timestamp = 0L;
/* 1869 */       cmove.diffX = 0.0F;
/* 1870 */       cmove.diffY = 0.0F;
/* 1871 */       cmove.diffZ = 0.0F;
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
/*      */   public void linkVisionArea() {
/* 1883 */     checkNewZone();
/* 1884 */     if (this.size > 0) {
/*      */       
/* 1886 */       for (int x = 0; x < this.watchedZones.length; x++)
/*      */       {
/* 1888 */         this.watchedZones[x].linkTo(this, this.startX, this.startY, this.endX, this.endY);
/*      */       }
/*      */     } else {
/*      */       
/* 1892 */       logger.log(Level.WARNING, "Size is 0 for creature " + this.watcher.getName());
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkNewZone() {
/* 1897 */     if (this.size <= 0) {
/*      */       return;
/*      */     }
/* 1900 */     Zone[] checkedZones = Zones.getZonesCoveredBy(this);
/* 1901 */     LinkedList<Zone> newZones = new LinkedList<>(Arrays.asList(checkedZones));
/* 1902 */     if (this.watchedZones == null)
/* 1903 */       this.watchedZones = new Zone[0]; 
/* 1904 */     LinkedList<Zone> oldZones = new LinkedList<>(Arrays.asList(this.watchedZones));
/* 1905 */     for (ListIterator<Zone> listIterator = newZones.listIterator(); listIterator.hasNext(); ) {
/*      */       
/* 1907 */       Zone newZ = listIterator.next();
/* 1908 */       if (logger.isLoggable(Level.FINEST))
/*      */       {
/* 1910 */         logger.finest("new zone is " + newZ.getStartX() + "," + newZ.getEndX() + "," + newZ.getStartY() + "," + newZ
/* 1911 */             .getEndY());
/*      */       }
/* 1913 */       for (ListIterator<Zone> it2 = oldZones.listIterator(); it2.hasNext(); ) {
/*      */         
/* 1915 */         Zone oldZ = it2.next();
/* 1916 */         if (logger.isLoggable(Level.FINEST))
/*      */         {
/* 1918 */           logger.finest("old zone is " + oldZ.getStartX() + "," + oldZ.getEndX() + "," + oldZ.getStartY() + "," + oldZ
/* 1919 */               .getEndY());
/*      */         }
/* 1921 */         if (newZ.equals(oldZ)) {
/*      */           
/* 1923 */           listIterator.remove();
/* 1924 */           it2.remove();
/*      */         } 
/*      */       } 
/*      */     }  Iterator<Zone> it;
/* 1928 */     for (it = newZones.iterator(); it.hasNext(); ) {
/*      */       
/* 1930 */       Zone toAdd = it.next();
/* 1931 */       if (logger.isLoggable(Level.FINEST))
/*      */       {
/* 1933 */         logger.finest("Adding zone " + getId() + " as watcher to " + toAdd.getId());
/*      */       }
/*      */       
/*      */       try {
/* 1937 */         toAdd.addWatcher(this.id);
/*      */       }
/* 1939 */       catch (NoSuchZoneException nze) {
/*      */         
/* 1941 */         logger.log(Level.INFO, nze.getMessage(), (Throwable)nze);
/*      */       } 
/*      */     } 
/*      */     
/* 1945 */     for (it = oldZones.iterator(); it.hasNext(); ) {
/*      */       
/* 1947 */       Zone toRemove = it.next();
/*      */       
/*      */       try {
/* 1950 */         if (logger.isLoggable(Level.FINEST))
/*      */         {
/* 1952 */           logger.finest("Removing zone " + getId() + " as watcher to " + toRemove.getId());
/*      */         }
/* 1954 */         toRemove.removeWatcher(this);
/*      */       }
/* 1956 */       catch (NoSuchZoneException sex) {
/*      */         
/* 1958 */         logger.log(Level.WARNING, "Zone with id does not exist!", (Throwable)sex);
/*      */       } 
/*      */     } 
/* 1961 */     this.watchedZones = checkedZones;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void deleteCreature(Creature creature, boolean removeAsTarget) throws NoSuchCreatureException, NoSuchPlayerException {
/* 1967 */     if (this.watcher == null) {
/*      */       
/* 1969 */       logger.log(Level.WARNING, "Watcher is null when linking: " + creature.getName(), new Exception());
/*      */       return;
/*      */     } 
/* 1972 */     if (removeAsTarget) {
/*      */ 
/*      */ 
/*      */       
/* 1976 */       boolean removeTarget = true;
/*      */       
/* 1978 */       if (creature.isTeleporting()) {
/*      */         
/* 1980 */         removeTarget = (Math.abs(this.watcher.getPosX() - creature.getTeleportX()) > 20.0F || Math.abs(this.watcher.getPosY() - creature
/* 1981 */             .getTeleportY()) > 20.0F);
/*      */       }
/* 1983 */       else if (this.watcher.isTeleporting()) {
/*      */         
/* 1985 */         removeTarget = (Math.abs(creature.getPosX() - this.watcher.getTeleportX()) > 20.0F || Math.abs(creature.getPosY() - this.watcher
/* 1986 */             .getTeleportY()) > 20.0F);
/*      */       } 
/* 1988 */       if (creature.isDead())
/* 1989 */         removeTarget = true; 
/* 1990 */       if (this.watcher.isDead())
/* 1991 */         removeTarget = true; 
/* 1992 */       if (removeTarget) {
/*      */         
/* 1994 */         if (creature.getWurmId() == this.watcher.target)
/*      */         {
/* 1996 */           if (this.watcher.getVisionArea() == null || this.watcher.getVisionArea().getSurface() == null || 
/* 1997 */             !this.watcher.getVisionArea().getSurface().containsCreature(creature))
/* 1998 */             this.watcher.setTarget(-10L, true); 
/*      */         }
/* 2000 */         if (creature.target == this.watcher.getWurmId())
/*      */         {
/* 2002 */           if (creature.getVisionArea() == null || creature.getVisionArea().getSurface() == null || 
/* 2003 */             !creature.getVisionArea().getSurface().containsCreature(this.watcher))
/* 2004 */             creature.setTarget(-10L, true); 
/*      */         }
/*      */       } 
/*      */     } 
/* 2008 */     if (this.creatures != null)
/*      */     {
/* 2010 */       if (this.creatures.keySet().contains(new Long(creature.getWurmId()))) {
/*      */         
/* 2012 */         this.creatures.remove(new Long(creature.getWurmId()));
/* 2013 */         if (removeAsTarget)
/* 2014 */           checkIfEnemyIsPresent(false); 
/* 2015 */         if (this.watcher.hasLink()) {
/*      */           
/* 2017 */           if (this.watcher != null && !this.watcher.equals(creature))
/* 2018 */             this.watcher.getCommunicator().sendDeleteCreature(creature.getWurmId()); 
/* 2019 */           if (creature instanceof Player || creature.isNpc())
/*      */           {
/* 2021 */             this.watcher.getCommunicator().sendRemoveLocal(creature.getName());
/*      */           }
/*      */         } 
/*      */ 
/*      */         
/* 2026 */         if (creature.getVehicle() != -10L)
/*      */         {
/* 2028 */           if (WurmId.getType(creature.getVehicle()) == 2) {
/*      */ 
/*      */ 
/*      */             
/* 2032 */             Vehicle vehic = Vehicles.getVehicleForId(creature.getVehicle());
/* 2033 */             if (vehic != null) {
/*      */               
/* 2035 */               boolean shouldRemove = true;
/* 2036 */               Seat[] seats = vehic.getSeats();
/* 2037 */               for (int x = 0; x < seats.length; x++) {
/*      */                 
/* 2039 */                 if (seats[x].isOccupied())
/*      */                 {
/* 2041 */                   if (this.creatures.containsKey(Long.valueOf((seats[x]).occupant))) {
/*      */ 
/*      */                     
/* 2044 */                     shouldRemove = false;
/*      */ 
/*      */                     
/*      */                     break;
/*      */                   } 
/*      */                 }
/*      */               } 
/*      */               
/* 2052 */               if (shouldRemove) {
/*      */                 
/*      */                 try {
/*      */                   
/* 2056 */                   Item vc = Items.getItem(creature.getVehicle());
/* 2057 */                   VolaTile tile = Zones.getOrCreateTile(vc.getTileX(), vc.getTileY(), vc.isOnSurface());
/* 2058 */                   if (!isVisible(vc, tile))
/*      */                   {
/*      */ 
/*      */ 
/*      */                     
/* 2063 */                     if (vc.isMovingItem()) {
/* 2064 */                       this.watcher.getCommunicator().sendDeleteMovingItem(vc.getWurmId());
/*      */                     } else {
/*      */                       
/* 2067 */                       if (vc.isWarTarget())
/*      */                       {
/* 2069 */                         this.watcher.getCommunicator().sendRemoveEffect(vc.getWurmId());
/*      */                       }
/* 2071 */                       this.watcher.getCommunicator().sendRemoveItem(vc);
/*      */                     }
/*      */                   
/*      */                   }
/* 2075 */                 } catch (NoSuchItemException noSuchItemException) {}
/*      */               }
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
/*      */   
/*      */   public final void pollVisibleVehicles() {
/* 2089 */     if (this.items != null) {
/* 2090 */       for (Iterator<Item> it = this.items.iterator(); it.hasNext(); ) {
/*      */         
/* 2092 */         Item i = it.next();
/* 2093 */         if (i.isVehicle()) {
/*      */           
/* 2095 */           if (i.deleted) {
/*      */             
/* 2097 */             it.remove();
/* 2098 */             sendRemoveItem(i);
/*      */             
/*      */             continue;
/*      */           } 
/* 2102 */           VolaTile t = Zones.getTileOrNull(i.getTileX(), i.getTileY(), i.isOnSurface());
/* 2103 */           if (!isVisible(i, t)) {
/*      */             
/* 2105 */             it.remove();
/* 2106 */             sendRemoveItem(i);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isVisible(Item item, VolaTile tile) {
/* 2115 */     if (item.getTemplateId() == 344)
/*      */     {
/* 2117 */       return (this.watcher.getPower() > 0);
/*      */     }
/*      */     
/* 2120 */     if (tile == null) {
/* 2121 */       return false;
/*      */     }
/* 2123 */     int distancex = Math.abs(tile.getTileX() - this.centerx);
/* 2124 */     int distancey = Math.abs(tile.getTileY() - this.centery);
/* 2125 */     int distance = Math.max(distancex, distancey);
/*      */ 
/*      */     
/* 2128 */     if (item.isVehicle()) {
/*      */       
/* 2130 */       Vehicle vehic = Vehicles.getVehicleForId(item.getWurmId());
/* 2131 */       if (vehic != null)
/*      */       {
/* 2133 */         Seat[] seats = vehic.getSeats();
/* 2134 */         for (int x = 0; x < seats.length; x++) {
/*      */           
/* 2136 */           if (seats[x].isOccupied())
/*      */           {
/* 2138 */             if (this.creatures.containsKey(Long.valueOf((seats[x]).occupant)))
/* 2139 */               return true; 
/*      */           }
/*      */         } 
/* 2142 */         if (this.watcher.isPlayer())
/*      */         {
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
/* 2162 */           return (Math.max(Math.abs(this.centerx - item.getTileX()), Math.abs(this.centery - item.getTileY())) <= this.size);
/*      */         }
/*      */       }
/*      */     
/* 2166 */     } else if (this.watcher.isPlayer()) {
/*      */       
/* 2168 */       if (item.getSizeZ() >= 500)
/* 2169 */         return true; 
/*      */     } 
/* 2171 */     if (item.isLight())
/*      */     {
/* 2173 */       return true;
/*      */     }
/* 2175 */     if (distance > this.size)
/*      */     {
/* 2177 */       return false;
/*      */     }
/*      */     
/* 2180 */     int isize = item.getSizeZ();
/* 2181 */     int mod = 3;
/* 2182 */     if (isize >= 300) {
/* 2183 */       mod = 128;
/* 2184 */     } else if (isize >= 200) {
/* 2185 */       mod = 64;
/* 2186 */     } else if (isize >= 100) {
/* 2187 */       mod = 32;
/* 2188 */     } else if (isize >= 50) {
/* 2189 */       mod = 16;
/* 2190 */     } else if (isize >= 10) {
/* 2191 */       mod = 8;
/* 2192 */     }  if (item.isBrazier())
/*      */     {
/* 2194 */       return (distance <= Math.max(mod, 16));
/*      */     }
/* 2196 */     if (item.isCarpet())
/*      */     {
/* 2198 */       mod = (distance <= Math.max(mod, 10)) ? Math.max(mod, 10) : mod;
/*      */     }
/*      */     
/* 2201 */     Structure itemStructure = tile.getStructure();
/* 2202 */     if (itemStructure != null && itemStructure.isTypeHouse()) {
/*      */       
/* 2204 */       if (this.watcher.isPlayer())
/*      */       {
/*      */         
/* 2207 */         if (this.nearbyStructureList != null && this.nearbyStructureList.contains(itemStructure)) {
/* 2208 */           return (distance <= mod);
/*      */         }
/*      */       }
/*      */ 
/*      */       
/* 2213 */       if (distance > 15) {
/* 2214 */         return false;
/*      */       }
/*      */     } 
/* 2217 */     if (logger.isLoggable(Level.FINEST))
/*      */     {
/* 2219 */       logger.finest(item.getName() + " distance=" + distance + ", size=" + (item.getSizeZ() / 10));
/*      */     }
/*      */     
/* 2222 */     return (distance <= mod);
/*      */   }
/*      */ 
/*      */   
/*      */   private final ArrayList<Structure> getStructuresWithinDistance(int tileDistance) {
/* 2227 */     if (this.nearbyStructureList == null)
/* 2228 */       this.nearbyStructureList = new ArrayList<>(); 
/* 2229 */     this.nearbyStructureList.clear();
/*      */     
/* 2231 */     for (int i = this.watcher.getTileX() - tileDistance; i < this.watcher.getTileX() + tileDistance; i++) {
/*      */       
/* 2233 */       for (int j = this.watcher.getTileY() - tileDistance; j < this.watcher.getTileY() + tileDistance; j++) {
/*      */         
/* 2235 */         VolaTile tile = Zones.getTileOrNull(Zones.safeTileX(i), Zones.safeTileY(j), this.watcher.isOnSurface());
/* 2236 */         if (tile != null && tile.getStructure() != null && tile.getStructure().isTypeHouse() && 
/* 2237 */           !this.nearbyStructureList.contains(tile.getStructure())) {
/* 2238 */           this.nearbyStructureList.add(tile.getStructure());
/*      */         }
/*      */       } 
/*      */     } 
/* 2242 */     return this.nearbyStructureList;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final Structure getStructureAtWatcherPosition() {
/*      */     try {
/* 2250 */       Zone zone = Zones.getZone(this.watcher.getTileX(), this.watcher.getTileY(), this.watcher.isOnSurface());
/*      */ 
/*      */       
/* 2253 */       VolaTile tile = zone.getOrCreateTile(this.watcher.getTileX(), this.watcher.getTileY());
/*      */ 
/*      */       
/* 2256 */       return tile.getStructure();
/*      */     }
/* 2258 */     catch (NoSuchZoneException e) {
/*      */       
/* 2260 */       logger.log(Level.WARNING, "Unable to find the zone at the watchers tile position.", (Throwable)e);
/* 2261 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void sendMoveMovingItem(long aId, float x, float y, int rot) {
/* 2267 */     if (this.watcher.hasLink())
/*      */     {
/* 2269 */       this.watcher.getCommunicator().sendMoveMovingItem(aId, x, y, rot);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   void sendMoveMovingItemAndSetZ(long aId, float x, float y, float z, int rot) {
/* 2275 */     if (this.watcher.hasLink())
/*      */     {
/* 2277 */       this.watcher.getCommunicator().sendMoveMovingItemAndSetZ(aId, x, y, z, rot);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   boolean addItem(Item item, VolaTile tile, boolean onGroundLevel) {
/* 2283 */     return addItem(item, tile, -10L, onGroundLevel);
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
/*      */   boolean addItem(Item item, VolaTile tile, long creatureId, boolean onGroundLevel) {
/* 2297 */     if (this.items == null)
/* 2298 */       this.items = new HashSet<>(); 
/* 2299 */     if (item.isMovingItem() || covers(item.getTileX(), item.getTileY())) {
/*      */       
/* 2301 */       if (!this.items.contains(item))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2309 */         this.items.add(item);
/*      */         
/* 2311 */         if (this.watcher.hasLink()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2317 */           if (item.isMovingItem()) {
/*      */             
/* 2319 */             byte newlayer = item.isOnSurface() ? 0 : -1;
/* 2320 */             if (item.newLayer != Byte.MIN_VALUE)
/* 2321 */               newlayer = item.newLayer; 
/* 2322 */             this.watcher.getCommunicator().sendNewMovingItem(item.getWurmId(), item.getName(), item.getModelName(), item
/* 2323 */                 .getPosX(), item.getPosY(), item.getPosZ(), item.onBridge(), item.getRotation(), newlayer, 
/* 2324 */                 (item.getFloorLevel() <= 0), (item
/* 2325 */                 .isFloating() && item.getCurrentQualityLevel() >= 10.0F), true, item.getMaterial(), item
/* 2326 */                 .getRarity());
/*      */             
/* 2328 */             Vehicle vehic = Vehicles.getVehicleForId(item.getWurmId());
/* 2329 */             if (vehic != null) {
/*      */               
/* 2331 */               Seat[] seats = vehic.getSeats();
/* 2332 */               for (int x = 0; x < seats.length; x++) {
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 2337 */                 if (seats[x].isOccupied() && this.watcher.getWurmId() != (seats[x]).occupant) {
/*      */                   
/* 2339 */                   Creature occ = Server.getInstance().getCreatureOrNull((seats[x]).occupant);
/* 2340 */                   if (occ != null && !occ.equals(this.watcher))
/*      */                   {
/* 2342 */                     if (occ.isVisibleTo(this.watcher)) {
/*      */                       
/* 2344 */                       if ((!Servers.localServer.PVPSERVER || this.watcher.isPaying()) && occ.isPlayer())
/*      */                       {
/* 2346 */                         this.watcher.getCommunicator().sendAddLocal(occ.getName(), (seats[x]).occupant);
/*      */                       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                       
/* 2353 */                       if (!this.creatures.containsKey(Long.valueOf((seats[x]).occupant)))
/*      */                       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                         
/* 2363 */                         if (this.watcher.isPlayer()) {
/*      */                           
/* 2365 */                           this.creatures.put(Long.valueOf(creatureId), new CreatureMove());
/*      */                         } else {
/*      */                           
/* 2368 */                           this.creatures.put(Long.valueOf(creatureId), null);
/*      */                         }  } 
/* 2370 */                       sendAttachCreature((seats[x]).occupant, item.getWurmId(), (seats[x]).offx, (seats[x]).offy, (seats[x]).offz, x);
/*      */                     } 
/*      */                   }
/*      */                 } 
/*      */               } 
/*      */ 
/*      */ 
/*      */               
/* 2378 */               Seat[] hitched = vehic.hitched;
/* 2379 */               for (int i = 0; i < hitched.length; i++)
/*      */               {
/*      */ 
/*      */ 
/*      */                 
/* 2384 */                 if (hitched[i].isOccupied() && this.creatures.containsKey(Long.valueOf((hitched[i]).occupant)) && this.watcher
/* 2385 */                   .getWurmId() != (hitched[i]).occupant) {
/* 2386 */                   sendAttachCreature((hitched[i]).occupant, item.getWurmId(), (hitched[i]).offx, (hitched[i]).offy, (hitched[i]).offz, i);
/*      */ 
/*      */ 
/*      */                 
/*      */                 }
/* 2391 */                 else if (this.watcher.getWurmId() == (hitched[i]).occupant) {
/*      */                   
/* 2393 */                   logger.log(Level.WARNING, "This should be unused code.");
/* 2394 */                   sendAttachCreature(-1L, item.getWurmId(), (hitched[i]).offx, (hitched[i]).offy, (hitched[i]).offz, i);
/*      */                 }
/*      */               
/*      */               }
/*      */             
/*      */             } 
/*      */           } else {
/*      */             
/* 2402 */             this.watcher.getCommunicator().sendItem(item, creatureId, onGroundLevel);
/* 2403 */             if (item.isWarTarget()) {
/*      */               
/* 2405 */               this.watcher.getCommunicator().sendAddEffect(item.getWurmId(), (short)24, item
/* 2406 */                   .getPosX(), item.getPosY(), item.getData1(), (byte)(item.isOnSurface() ? 0 : -1));
/* 2407 */               this.watcher.getCommunicator().sendTargetStatus(item.getWurmId(), (byte)item.getData2(), item
/* 2408 */                   .getData1());
/*      */             } 
/* 2410 */             if (item.getTemplate().hasViewableSubItems())
/*      */             {
/* 2412 */               if (item.getItemCount() > 0) {
/*      */                 
/* 2414 */                 boolean normalContainer = item.getTemplate().isContainerWithSubItems();
/* 2415 */                 for (Item i : item.getItems()) {
/*      */                   
/* 2417 */                   if (normalContainer && !i.isPlacedOnParent()) {
/*      */                     continue;
/*      */                   }
/* 2420 */                   this.watcher.getCommunicator().sendItem(i, -10L, false);
/* 2421 */                   if (i.isLight() && i.isOnFire())
/* 2422 */                     addLightSource(i); 
/* 2423 */                   if ((i.getEffects()).length > 0)
/* 2424 */                     for (Effect e : i.getEffects())
/* 2425 */                       addEffect(e, false);  
/* 2426 */                   if (i.getColor() != -1) {
/* 2427 */                     sendRepaint(i.getWurmId(), (byte)WurmColor.getColorRed(i.getColor()), 
/* 2428 */                         (byte)WurmColor.getColorGreen(i.getColor()), (byte)WurmColor.getColorBlue(i.getColor()), (byte)-1, (byte)0);
/*      */                   }
/* 2430 */                   if (i.getColor2() != -1) {
/* 2431 */                     sendRepaint(i.getWurmId(), (byte)WurmColor.getColorRed(i.getColor2()), 
/* 2432 */                         (byte)WurmColor.getColorGreen(i.getColor2()), (byte)WurmColor.getColorBlue(i.getColor2()), (byte)-1, (byte)1);
/*      */                   }
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           } 
/*      */           
/* 2439 */           if (item.isLight())
/*      */           {
/* 2441 */             if (item.isOnFire()) {
/*      */               
/* 2443 */               addLightSource(item);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2452 */               if ((item.getEffects()).length > 0) {
/*      */                 
/* 2454 */                 Effect[] effs = item.getEffects();
/* 2455 */                 for (int x = 0; x < effs.length; x++)
/* 2456 */                   addEffect(effs[x], false); 
/*      */               } 
/*      */             } 
/*      */           }
/* 2460 */           if (!item.isLight() || item.getTemplateId() == 1396) {
/*      */             
/* 2462 */             if (item.getColor() != -1)
/*      */             {
/* 2464 */               sendRepaint(item.getWurmId(), (byte)WurmColor.getColorRed(item.getColor()), 
/* 2465 */                   (byte)WurmColor.getColorGreen(item.getColor()), (byte)WurmColor.getColorBlue(item.getColor()), (byte)-1, (byte)0);
/*      */             }
/*      */             
/* 2468 */             if (item.supportsSecondryColor() && item.getColor2() != -1)
/*      */             {
/* 2470 */               sendRepaint(item.getWurmId(), (byte)WurmColor.getColorRed(item.getColor2()), 
/* 2471 */                   (byte)WurmColor.getColorGreen(item.getColor2()), (byte)WurmColor.getColorBlue(item.getColor2()), (byte)-1, (byte)1);
/*      */             }
/*      */           } 
/*      */           
/* 2475 */           if (item.getExtra() != -1L && (item.getTemplateId() == 491 || item.getTemplateId() == 490)) {
/*      */             
/* 2477 */             Optional<Item> extraItem = Items.getItemOptional(item.getExtra());
/* 2478 */             if (extraItem.isPresent())
/*      */             {
/* 2480 */               sendBoatAttachment(item.getWurmId(), ((Item)extraItem.get()).getTemplateId(), ((Item)extraItem.get()).getMaterial(), (byte)1, ((Item)extraItem
/* 2481 */                   .get()).getAuxData());
/*      */             }
/*      */           } 
/*      */         } 
/* 2485 */         if (item.isHugeAltar()) {
/*      */           
/* 2487 */           if (this.watcher.getMusicPlayer() != null)
/*      */           {
/* 2489 */             if (this.watcher.getMusicPlayer().isItOkToPlaySong(true))
/*      */             {
/* 2491 */               if (item.getTemplateId() == 327) {
/* 2492 */                 this.watcher.getMusicPlayer().checkMUSIC_WHITELIGHT_SND();
/*      */               } else {
/* 2494 */                 this.watcher.getMusicPlayer().checkMUSIC_BLACKLIGHT_SND();
/*      */               } 
/*      */             }
/*      */           }
/* 2498 */         } else if (item.getTemplateId() == 518) {
/*      */           
/* 2500 */           if (this.watcher.getMusicPlayer() != null)
/*      */           {
/* 2502 */             if (this.watcher.getMusicPlayer().isItOkToPlaySong(true))
/*      */             {
/* 2504 */               this.watcher.getMusicPlayer().checkMUSIC_COLOSSUS_SND();
/*      */             
/*      */             }
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 2517 */       int tilex = item.getTileX();
/* 2518 */       int tiley = item.getTileY();
/*      */       
/*      */       try {
/* 2521 */         Item item1 = item.getParent();
/*      */       }
/* 2523 */       catch (NoSuchItemException noSuchItemException) {}
/*      */ 
/*      */ 
/*      */       
/* 2527 */       if (item.getContainerSizeZ() < 500 && !item.isVehicle()) {
/*      */         
/* 2529 */         VolaTile vtile = Zones.getTileOrNull(tilex, tiley, this.isOnSurface);
/* 2530 */         if (vtile != null) {
/*      */           
/* 2532 */           if (!vtile.equals(tile)) {
/* 2533 */             return false;
/*      */           }
/*      */           
/* 2536 */           if (!covers(tile.getTileX(), tile.getTileY()))
/*      */           {
/* 2538 */             vtile.removeWatcher(this);
/*      */           
/*      */           }
/*      */         }
/*      */         else {
/*      */           
/* 2544 */           return false;
/*      */         } 
/*      */       } 
/*      */     } 
/* 2548 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private void sendRemoveItem(Item item) {
/* 2553 */     if (this.watcher.hasLink())
/*      */     {
/* 2555 */       if (item.isMovingItem()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2563 */         this.watcher.getCommunicator().sendDeleteMovingItem(item.getWurmId());
/* 2564 */         if (this.watcher.isPlayer())
/*      */         {
/* 2566 */           Vehicle vehic = Vehicles.getVehicleForId(item.getWurmId());
/* 2567 */           if (vehic != null)
/*      */           {
/* 2569 */             Seat[] seats = vehic.getSeats();
/* 2570 */             for (int x = 0; x < seats.length; x++)
/*      */             {
/* 2572 */               if (seats[x].isOccupied()) {
/*      */                 
/*      */                 try {
/*      */                   
/* 2576 */                   Creature occ = Server.getInstance().getCreature((seats[x]).occupant);
/* 2577 */                   if (occ != null && !occ.equals(this.watcher))
/*      */                   {
/* 2579 */                     if (this.creatures != null)
/* 2580 */                       this.creatures.remove(Long.valueOf((seats[x]).occupant)); 
/* 2581 */                     this.watcher.getCommunicator().sendRemoveLocal(occ.getName());
/*      */                   }
/*      */                 
/* 2584 */                 } catch (NoSuchCreatureException nsc) {
/*      */                   
/* 2586 */                   logger.log(Level.WARNING, nsc.getMessage(), (Throwable)nsc);
/*      */                 }
/* 2588 */                 catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */               
/*      */               }
/*      */             }
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 2600 */         if (item.isWarTarget())
/*      */         {
/* 2602 */           this.watcher.getCommunicator().sendRemoveEffect(item.getWurmId());
/*      */         }
/* 2604 */         if (item.getTemplate().hasViewableSubItems())
/*      */         {
/* 2606 */           if (item.getItemCount() > 0) {
/*      */             
/* 2608 */             boolean normalContainer = item.getTemplate().isContainerWithSubItems();
/* 2609 */             for (Item i : item.getAllItems(false)) {
/*      */               
/* 2611 */               if (!normalContainer || i.isPlacedOnParent())
/*      */               {
/*      */                 
/* 2614 */                 this.watcher.getCommunicator().sendRemoveItem(i); } 
/*      */             } 
/*      */           } 
/*      */         }
/* 2618 */         this.watcher.getCommunicator().sendRemoveItem(item);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   void removeItem(Item item) {
/* 2625 */     if (this.items != null && this.items.contains(item)) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2632 */       this.items.remove(item);
/* 2633 */       sendRemoveItem(item);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void removeStructure(Structure structure) {
/* 2639 */     if (logger.isLoggable(Level.FINEST))
/*      */     {
/* 2641 */       logger.finest(this.watcher.getName() + " removing structure " + structure);
/*      */     }
/* 2643 */     if (this.structures != null && this.structures.contains(structure)) {
/*      */       
/* 2645 */       boolean stillHere = false;
/* 2646 */       VolaTile[] tiles = structure.getStructureTiles();
/* 2647 */       for (int x = 0; x < tiles.length; x++) {
/*      */         
/* 2649 */         if (covers(tiles[x].getTileX(), tiles[x].getTileY())) {
/*      */           
/* 2651 */           stillHere = true;
/*      */           break;
/*      */         } 
/*      */       } 
/* 2655 */       if (!stillHere) {
/*      */         
/* 2657 */         this.structures.remove(structure);
/* 2658 */         this.watcher.getCommunicator().sendRemoveStructure(structure.getWurmId());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void deleteStructure(Structure structure) {
/* 2665 */     if (this.structures != null)
/*      */     {
/* 2667 */       if (this.structures.contains(structure)) {
/*      */         
/* 2669 */         this.structures.remove(structure);
/* 2670 */         this.watcher.getCommunicator().sendRemoveStructure(structure.getWurmId());
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void removeAllStructures() {
/* 2677 */     if (this.structures != null)
/*      */     {
/* 2679 */       for (Iterator<Structure> it = this.structures.iterator(); it.hasNext(); ) {
/*      */         
/* 2681 */         Structure structure = it.next();
/* 2682 */         this.watcher.getCommunicator().sendRemoveStructure(structure.getWurmId());
/*      */       } 
/*      */     }
/* 2685 */     this.structures = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void sendStructureWalls(Structure structure) {
/* 2695 */     Wall[] wallArr = structure.getWalls();
/* 2696 */     for (int x = 0; x < wallArr.length; x++)
/*      */     {
/* 2698 */       updateWall(structure.getWurmId(), wallArr[x]);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   void addStructure(Structure structure) {
/* 2704 */     if (logger.isLoggable(Level.FINEST))
/*      */     {
/* 2706 */       logger.finest(this.watcher.getName() + " adding structure " + structure);
/*      */     }
/* 2708 */     if (this.structures == null)
/* 2709 */       this.structures = new HashSet<>(); 
/* 2710 */     if (!this.structures.contains(structure)) {
/*      */       
/* 2712 */       this.structures.add(structure);
/* 2713 */       this.watcher.getCommunicator().sendAddStructure(structure.getName(), (short)structure.getCenterX(), 
/* 2714 */           (short)structure.getCenterY(), structure.getWurmId(), structure.getStructureType(), structure
/* 2715 */           .getLayer());
/* 2716 */       if (structure.isTypeHouse()) {
/*      */         
/* 2718 */         this.watcher.getCommunicator().sendMultipleBuildMarkers(structure.getWurmId(), structure.getStructureTiles(), structure
/* 2719 */             .getLayer());
/* 2720 */         sendStructureWalls(structure);
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
/* 2744 */       Floor[] floorArr = structure.getFloors();
/* 2745 */       if (floorArr != null)
/*      */       {
/* 2747 */         for (int x = 0; x < floorArr.length; x++)
/*      */         {
/* 2749 */           updateFloor(structure.getWurmId(), floorArr[x]);
/*      */         }
/*      */       }
/*      */       
/* 2753 */       BridgePart[] bridgePartArr = structure.getBridgeParts();
/* 2754 */       if (bridgePartArr != null)
/*      */       {
/* 2756 */         for (int x = 0; x < bridgePartArr.length; x++)
/*      */         {
/* 2758 */           updateBridgePart(structure.getWurmId(), bridgePartArr[x]);
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void addBuildMarker(Structure structure, int tilex, int tiley) {
/* 2766 */     if (this.structures == null)
/* 2767 */       this.structures = new HashSet<>(); 
/* 2768 */     if (!this.structures.contains(structure)) {
/* 2769 */       addStructure(structure);
/*      */     } else {
/* 2771 */       this.watcher.getCommunicator().sendSingleBuildMarker(structure.getWurmId(), tilex, tiley, getLayer());
/*      */     } 
/*      */   }
/*      */   
/*      */   void removeBuildMarker(Structure structure, int tilex, int tiley) {
/* 2776 */     if (this.structures != null && this.structures.contains(structure)) {
/*      */       
/* 2778 */       boolean stillHere = false;
/* 2779 */       VolaTile[] tiles = structure.getStructureTiles();
/* 2780 */       for (int x = 0; x < tiles.length; x++) {
/*      */         
/* 2782 */         if (covers(tiles[x].getTileX(), tiles[x].getTileY())) {
/*      */           
/* 2784 */           stillHere = true;
/*      */           break;
/*      */         } 
/*      */       } 
/* 2788 */       if (stillHere) {
/*      */         
/* 2790 */         if (logger.isLoggable(Level.FINEST))
/*      */         {
/* 2792 */           logger.finest(this.watcher.getName() + " removing build marker for structure " + structure.getWurmId());
/*      */         }
/* 2794 */         this.watcher.getCommunicator().sendSingleBuildMarker(structure.getWurmId(), tilex, tiley, getLayer());
/*      */       }
/*      */       else {
/*      */         
/* 2798 */         removeStructure(structure);
/*      */       } 
/*      */     } else {
/*      */       
/* 2802 */       logger.log(Level.INFO, "Hmm tried to remove buildmarker from a zone that didn't contain it.");
/*      */     } 
/*      */   }
/*      */   
/*      */   void finalizeBuildPlan(long oldStructureId, long newStructureId) {
/* 2807 */     if (this.finalizedBuildings == null)
/* 2808 */       this.finalizedBuildings = new HashSet<>(); 
/* 2809 */     if (!this.finalizedBuildings.contains(new Long(newStructureId))) {
/*      */ 
/*      */       
/*      */       try {
/* 2813 */         Structure structure = Structures.getStructure(newStructureId);
/*      */         
/* 2815 */         if (structure.isTypeHouse()) {
/* 2816 */           this.watcher.getCommunicator().sendRemoveStructure(oldStructureId);
/*      */         }
/* 2818 */         this.watcher.getCommunicator().sendAddStructure(structure.getName(), (short)structure.getCenterX(), 
/* 2819 */             (short)structure.getCenterY(), structure.getWurmId(), structure.getStructureType(), structure
/* 2820 */             .getLayer());
/*      */         
/* 2822 */         if (structure.isTypeHouse())
/* 2823 */           this.watcher.getCommunicator().sendMultipleBuildMarkers(structure.getWurmId(), structure.getStructureTiles(), structure
/* 2824 */               .getLayer()); 
/* 2825 */         Wall[] wallArr = structure.getWalls();
/*      */         
/* 2827 */         for (int x = 0; x < wallArr.length; x++) {
/*      */           
/* 2829 */           if (wallArr[x].getType() != StructureTypeEnum.PLAN) {
/*      */             
/* 2831 */             this.watcher.getCommunicator().sendAddWall(structure.getWurmId(), wallArr[x]);
/* 2832 */             if (wallArr[x].getDamage() >= 60.0F) {
/* 2833 */               this.watcher.getCommunicator().sendWallDamageState(structure.getWurmId(), wallArr[x].getId(), 
/* 2834 */                   (byte)(int)wallArr[x].getDamage());
/*      */             }
/*      */           } 
/*      */         } 
/* 2838 */       } catch (NoSuchStructureException nss) {
/*      */         
/* 2840 */         logger.log(Level.WARNING, "The new building doesn't exist.", (Throwable)nss);
/*      */       } 
/* 2842 */       this.finalizedBuildings.add(new Long(newStructureId));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void addDoor(Door door) {
/* 2848 */     if (this.doors == null)
/* 2849 */       this.doors = new HashSet<>(); 
/* 2850 */     if (!this.doors.contains(door)) {
/*      */       
/* 2852 */       this.doors.add(door);
/* 2853 */       if (door.isOpen())
/*      */       {
/* 2855 */         if (door instanceof FenceGate) {
/* 2856 */           openFence(((FenceGate)door).getFence(), false, true);
/*      */         } else {
/* 2858 */           openDoor(door);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   void removeDoor(Door door) {
/* 2865 */     if (this.doors != null) {
/* 2866 */       this.doors.remove(door);
/*      */     }
/*      */   }
/*      */   
/*      */   public void openDoor(Door door) {
/* 2871 */     this.watcher.getCommunicator().sendOpenDoor(door);
/*      */   }
/*      */ 
/*      */   
/*      */   public void closeDoor(Door door) {
/* 2876 */     this.watcher.getCommunicator().sendCloseDoor(door);
/*      */   }
/*      */ 
/*      */   
/*      */   public void openFence(Fence fence, boolean passable, boolean changedPassable) {
/* 2881 */     this.watcher.getCommunicator().sendOpenFence(fence, passable, changedPassable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void closeFence(Fence fence, boolean passable, boolean changedPassable) {
/* 2886 */     this.watcher.getCommunicator().sendCloseFence(fence, passable, changedPassable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void openMineDoor(MineDoorPermission door) {
/* 2891 */     this.watcher.getCommunicator().sendOpenMineDoor(door);
/*      */   }
/*      */ 
/*      */   
/*      */   public void closeMineDoor(MineDoorPermission door) {
/* 2896 */     this.watcher.getCommunicator().sendCloseMineDoor(door);
/*      */   }
/*      */ 
/*      */   
/*      */   void updateFloor(long structureId, Floor floor) {
/* 2901 */     this.watcher.getCommunicator().sendAddFloor(structureId, floor);
/* 2902 */     if (floor.getDamage() >= 60.0F) {
/* 2903 */       this.watcher.getCommunicator().sendWallDamageState(floor.getStructureId(), floor.getId(), (byte)(int)floor.getDamage());
/*      */     }
/*      */   }
/*      */   
/*      */   void updateBridgePart(long structureId, BridgePart bridgePart) {
/* 2908 */     this.watcher.getCommunicator().sendAddBridgePart(structureId, bridgePart);
/* 2909 */     if (bridgePart.getDamage() >= 60.0F) {
/* 2910 */       this.watcher.getCommunicator().sendWallDamageState(bridgePart.getStructureId(), bridgePart.getId(), (byte)(int)bridgePart.getDamage());
/*      */     }
/*      */   }
/*      */   
/*      */   void updateWall(long structureId, Wall wall) {
/* 2915 */     this.watcher.getCommunicator().sendAddWall(structureId, wall);
/* 2916 */     if (wall.getDamage() >= 60.0F) {
/* 2917 */       this.watcher.getCommunicator().sendWallDamageState(wall.getStructureId(), wall.getId(), (byte)(int)wall.getDamage());
/*      */     }
/*      */   }
/*      */   
/*      */   void removeWall(long structureId, Wall wall) {
/* 2922 */     this.watcher.getCommunicator().sendRemoveWall(structureId, wall);
/*      */   }
/*      */ 
/*      */   
/*      */   void removeFloor(long structureId, Floor floor) {
/* 2927 */     this.watcher.getCommunicator().sendRemoveFloor(structureId, floor);
/*      */   }
/*      */ 
/*      */   
/*      */   void removeBridgePart(long structureId, BridgePart bridgePart) {
/* 2932 */     this.watcher.getCommunicator().sendRemoveBridgePart(structureId, bridgePart);
/*      */   }
/*      */ 
/*      */   
/*      */   void changeStructureName(long structureId, String newName) {
/* 2937 */     this.watcher.getCommunicator().sendChangeStructureName(structureId, newName);
/*      */   }
/*      */ 
/*      */   
/*      */   void playSound(Sound sound) {
/* 2942 */     this.watcher.getCommunicator().sendSound(sound);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isCreatureTurnedTowardsTarget(Creature target, Creature performer) {
/* 2947 */     return isCreatureTurnedTowardsTarget(target, performer, 180.0F, false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isCreatureShieldedVersusTarget(Creature target, Creature performer) {
/* 2953 */     if (performer.isWithinDistanceTo(target, 1.5F)) {
/*      */       
/* 2955 */       if (Servers.localServer.testServer && target.isPlayer() && performer.isPlayer())
/* 2956 */         target.getCommunicator().sendNormalServerMessage(performer.getName() + " is so close he auto blocks you."); 
/* 2957 */       return true;
/*      */     } 
/* 2959 */     return isCreatureTurnedTowardsTarget(target, performer, 135.0F, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isCreatureTurnedTowardsItem(Item target, Creature performer, float angle) {
/* 2964 */     double newrot = Math.atan2((target.getPosY() - (int)performer.getStatus().getPositionY()), (target.getPosX() - 
/* 2965 */         (int)performer.getStatus().getPositionX()));
/*      */     
/* 2967 */     float attAngle = (float)(newrot * 57.29577951308232D) + 90.0F;
/* 2968 */     attAngle = Creature.normalizeAngle(attAngle);
/* 2969 */     float prot = Creature.normalizeAngle(performer.getStatus().getRotation() - attAngle);
/* 2970 */     if (prot > angle / 2.0F && prot < 360.0F - angle / 2.0F)
/*      */     {
/* 2972 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 2976 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isItemTurnedTowardsCreature(Creature target, Item performer, float angle) {
/* 2982 */     double newrot = Math.atan2((target.getPosY() - (int)performer.getPosY()), (target
/* 2983 */         .getPosX() - (int)performer.getPosX()));
/*      */     
/* 2985 */     float attAngle = (float)(newrot * 57.29577951308232D) - 90.0F;
/* 2986 */     attAngle = Creature.normalizeAngle(attAngle);
/* 2987 */     float prot = Creature.normalizeAngle(performer.getRotation() - attAngle);
/* 2988 */     if (prot > angle / 2.0F && prot < 360.0F - angle / 2.0F)
/*      */     {
/* 2990 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 2994 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isCreatureTurnedTowardsTarget(Creature target, Creature performer, float angle, boolean leftWinged) {
/* 3001 */     boolean log = (leftWinged && Servers.localServer.testServer && target.isPlayer() && performer.isPlayer());
/* 3002 */     double newrot = Math.atan2((target.getPosY() - (int)performer.getStatus().getPositionY()), (target.getPosX() - 
/* 3003 */         (int)performer.getStatus().getPositionX()));
/*      */     
/* 3005 */     float attAngle = (float)(newrot * 57.29577951308232D) + 90.0F;
/* 3006 */     attAngle = Creature.normalizeAngle(attAngle);
/* 3007 */     float crot = Creature.normalizeAngle(performer.getStatus().getRotation());
/* 3008 */     float prot = Creature.normalizeAngle(attAngle - crot);
/*      */     
/* 3010 */     float rightAngle = angle / 2.0F;
/* 3011 */     float leftAngle = 360.0F - angle / 2.0F;
/* 3012 */     if (leftWinged) {
/*      */       
/* 3014 */       leftAngle -= 45.0F;
/* 3015 */       rightAngle -= 45.0F;
/*      */     } 
/* 3017 */     leftAngle = Creature.normalizeAngle(leftAngle);
/* 3018 */     rightAngle = Creature.normalizeAngle(rightAngle);
/* 3019 */     if (log)
/* 3020 */       target.getCommunicator().sendNormalServerMessage(attAngle + ", " + crot + ", prot=" + prot); 
/* 3021 */     if (prot > rightAngle && prot < leftAngle) {
/*      */       
/* 3023 */       if (log) {
/* 3024 */         target.getCommunicator().sendNormalServerMessage("1.5 " + performer
/* 3025 */             .getName() + " will not block you. Angle to me= " + attAngle + ", creature angle=" + crot + ", difference=" + prot + ". Max left=" + leftAngle + ", right=" + rightAngle);
/*      */       }
/* 3027 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 3031 */     if (log) {
/* 3032 */       target.getCommunicator().sendNormalServerMessage("1.5 " + performer
/* 3033 */           .getName() + " will block you. Angle to me= " + attAngle + ", creature angle=" + crot + ", difference=" + prot + ". Max left=" + leftAngle + ", right=" + rightAngle);
/*      */     }
/* 3035 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void addLightSource(Item lightSource) {
/* 3041 */     int colorToUse = lightSource.getColor();
/* 3042 */     if (lightSource.getTemplateId() == 1396)
/* 3043 */       colorToUse = lightSource.getColor2(); 
/* 3044 */     if (colorToUse != -1) {
/*      */       
/* 3046 */       int lightStrength = Math.max(WurmColor.getColorRed(colorToUse), WurmColor.getColorGreen(colorToUse));
/* 3047 */       lightStrength = Math.max(1, Math.max(lightStrength, WurmColor.getColorBlue(colorToUse)));
/* 3048 */       byte r = (byte)(WurmColor.getColorRed(colorToUse) * 128 / lightStrength);
/* 3049 */       byte g = (byte)(WurmColor.getColorGreen(colorToUse) * 128 / lightStrength);
/* 3050 */       byte b = (byte)(WurmColor.getColorBlue(colorToUse) * 128 / lightStrength);
/*      */ 
/*      */ 
/*      */       
/* 3054 */       sendAttachItemEffect(lightSource.getWurmId(), (byte)4, r, g, b, lightSource.getRadius());
/*      */ 
/*      */     
/*      */     }
/* 3058 */     else if (lightSource.isLightBright()) {
/*      */       
/* 3060 */       int lightStrength = (int)(80.0F + lightSource.getCurrentQualityLevel() / 100.0F * 40.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3065 */       sendAttachItemEffect(lightSource.getWurmId(), (byte)4, 
/* 3066 */           Item.getRLight(lightStrength), Item.getGLight(lightStrength), Item.getBLight(lightStrength), lightSource
/* 3067 */           .getRadius());
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 3074 */       sendAttachItemEffect(lightSource.getWurmId(), (byte)4, Item.getRLight(80), 
/* 3075 */           Item.getGLight(80), Item.getBLight(80), lightSource.getRadius());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void addLightSource(Creature creature, Item lightSource) {
/* 3082 */     if (lightSource.getColor() != -1) {
/*      */       
/* 3084 */       int lightStrength = Math.max(WurmColor.getColorRed(lightSource.getColor()), WurmColor.getColorGreen(lightSource.getColor()));
/* 3085 */       lightStrength = Math.max(1, Math.max(lightStrength, WurmColor.getColorBlue(lightSource.getColor())));
/* 3086 */       byte r = (byte)(WurmColor.getColorRed(lightSource.getColor()) * 128 / lightStrength);
/* 3087 */       byte g = (byte)(WurmColor.getColorGreen(lightSource.getColor()) * 128 / lightStrength);
/* 3088 */       byte b = (byte)(WurmColor.getColorBlue(lightSource.getColor()) * 128 / lightStrength);
/*      */       
/* 3090 */       sendAttachCreatureEffect(creature, (byte)0, r, g, b, lightSource.getRadius());
/*      */ 
/*      */     
/*      */     }
/* 3094 */     else if (lightSource.isLightBright()) {
/*      */       
/* 3096 */       int lightStrength = (int)(80.0F + lightSource.getCurrentQualityLevel() / 100.0F * 40.0F);
/* 3097 */       sendAttachCreatureEffect(creature, (byte)0, Item.getRLight(lightStrength), 
/* 3098 */           Item.getGLight(lightStrength), Item.getBLight(lightStrength), lightSource.getRadius());
/*      */     } else {
/*      */       
/* 3101 */       sendAttachCreatureEffect(creature, (byte)0, Item.getRLight(80), Item.getGLight(80), 
/* 3102 */           Item.getBLight(80), lightSource.getRadius());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendAttachCreatureEffect(Creature creature, byte effectType, byte data0, byte data1, byte data2, byte radius) {
/* 3109 */     if (creature == null) {
/* 3110 */       this.watcher.getCommunicator().sendAttachEffect(-1L, effectType, data0, data1, data2, radius);
/* 3111 */     } else if (this.creatures.containsKey(Long.valueOf(creature.getWurmId()))) {
/* 3112 */       this.watcher.getCommunicator().sendAttachEffect(creature.getWurmId(), effectType, data0, data1, data2, radius);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void sendAttachItemEffect(long targetId, byte effectType, byte data0, byte data1, byte data2, byte radius) {
/* 3118 */     this.watcher.getCommunicator().sendAttachEffect(targetId, effectType, data0, data1, data2, radius);
/*      */   }
/*      */ 
/*      */   
/*      */   void sendRemoveEffect(long targetId, byte effectType) {
/* 3123 */     if (WurmId.getType(targetId) == 2) {
/* 3124 */       this.watcher.getCommunicator().sendRemoveEffect(targetId, effectType);
/* 3125 */     } else if (targetId == -1L || this.creatures.containsKey(Long.valueOf(targetId))) {
/* 3126 */       this.watcher.getCommunicator().sendRemoveEffect(targetId, effectType);
/*      */     } 
/*      */   }
/*      */   
/*      */   void sendHorseWear(long creatureId, int itemId, byte material, byte slot, byte aux_data) {
/* 3131 */     if (this.creatures.containsKey(Long.valueOf(creatureId)))
/*      */     {
/* 3133 */       this.watcher.getCommunicator().sendHorseWear(creatureId, itemId, material, slot, aux_data);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   void sendRemoveHorseWear(long creatureId, int itemId, byte slot) {
/* 3139 */     if (this.creatures.containsKey(Long.valueOf(creatureId)))
/*      */     {
/* 3141 */       this.watcher.getCommunicator().sendRemoveHorseWear(creatureId, itemId, slot);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   void sendBoatAttachment(long itemId, int templateId, byte material, byte slot, byte aux) {
/* 3147 */     this.watcher.getCommunicator().sendHorseWear(itemId, templateId, material, slot, aux);
/*      */   }
/*      */ 
/*      */   
/*      */   void sendBoatDetachment(long itemId, int templateId, byte slot) {
/* 3152 */     this.watcher.getCommunicator().sendRemoveHorseWear(itemId, templateId, slot);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void sendWearItem(long creatureId, int itemId, byte bodyPart, int colorRed, int colorGreen, int colorBlue, int secondaryColorRed, int secondaryColorGreen, int secondaryColorBlue, byte material, byte rarity) {
/* 3159 */     if (creatureId == -1L || this.creatures.containsKey(Long.valueOf(creatureId)))
/*      */     {
/* 3161 */       this.watcher.getCommunicator().sendWearItem(creatureId, itemId, bodyPart, colorRed, colorGreen, colorBlue, secondaryColorRed, secondaryColorGreen, secondaryColorBlue, material, rarity);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void sendRemoveWearItem(long creatureId, byte bodyPart) {
/* 3170 */     if (creatureId == -1L || this.creatures.containsKey(Long.valueOf(creatureId)))
/*      */     {
/* 3172 */       this.watcher.getCommunicator().sendRemoveWearItem(creatureId, bodyPart);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void sendWieldItem(long creatureId, byte slot, String modelname, byte rarity, int colorRed, int colorGreen, int colorBlue, int secondaryColorRed, int secondaryColorGreen, int secondaryColorBlue) {
/* 3179 */     if (creatureId == -1L || this.creatures.containsKey(Long.valueOf(creatureId))) {
/* 3180 */       this.watcher.getCommunicator().sendWieldItem(creatureId, slot, modelname, rarity, colorRed, colorGreen, colorBlue, secondaryColorRed, secondaryColorGreen, secondaryColorBlue);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void sendUseItem(Creature creature, String modelname, byte rarity, int colorRed, int colorGreen, int colorBlue, int secondaryColorRed, int secondaryColorGreen, int secondaryColorBlue) {
/* 3187 */     if (creature == null) {
/* 3188 */       this.watcher.getCommunicator().sendUseItem(-1L, modelname, rarity, colorRed, colorGreen, colorBlue, secondaryColorRed, secondaryColorGreen, secondaryColorBlue);
/*      */     }
/* 3190 */     else if (!creature.isTeleporting() && this.creatures.containsKey(Long.valueOf(creature.getWurmId()))) {
/* 3191 */       this.watcher.getCommunicator().sendUseItem(creature.getWurmId(), modelname, rarity, colorRed, colorGreen, colorBlue, secondaryColorRed, secondaryColorGreen, secondaryColorBlue);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void sendStopUseItem(Creature creature) {
/* 3197 */     if (creature == null) {
/* 3198 */       this.watcher.getCommunicator().sendStopUseItem(-1L);
/* 3199 */     } else if (!creature.isTeleporting() && this.creatures.containsKey(Long.valueOf(creature.getWurmId()))) {
/* 3200 */       this.watcher.getCommunicator().sendStopUseItem(creature.getWurmId());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendRepaint(long wurmid, byte red, byte green, byte blue, byte alpha, byte paintType) {
/* 3207 */     this.watcher.getCommunicator().sendRepaint(wurmid, red, green, blue, alpha, paintType);
/*      */   }
/*      */ 
/*      */   
/*      */   private void sendResizeCreature(long wurmid, byte xscaleMod, byte yscaleMod, byte zscaleMod) {
/* 3212 */     this.watcher.getCommunicator().sendResize(wurmid, xscaleMod, yscaleMod, zscaleMod);
/*      */   }
/*      */ 
/*      */   
/*      */   void sendAnimation(Creature creature, String animationName, boolean looping, long target) {
/* 3217 */     if (creature == null) {
/*      */       
/* 3219 */       if (target <= 0L) {
/* 3220 */         this.watcher.getCommunicator().sendAnimation(-1L, animationName, looping, animationName.equals("die"));
/*      */       } else {
/* 3222 */         this.watcher.getCommunicator().sendAnimation(-1L, animationName, looping, false, target);
/*      */       } 
/* 3224 */     } else if (this.creatures.containsKey(Long.valueOf(creature.getWurmId()))) {
/*      */       
/* 3226 */       if (target <= 0L) {
/* 3227 */         this.watcher.getCommunicator().sendAnimation(creature.getWurmId(), animationName, looping, animationName.equals("die"));
/*      */       } else {
/* 3229 */         this.watcher.getCommunicator().sendAnimation(creature.getWurmId(), animationName, looping, false, target);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   void sendStance(Creature creature, byte stance) {
/* 3235 */     if (creature == null) {
/* 3236 */       this.watcher.getCommunicator().sendStance(-1L, stance);
/* 3237 */     } else if (this.creatures.containsKey(Long.valueOf(creature.getWurmId()))) {
/* 3238 */       this.watcher.getCommunicator().sendStance(creature.getWurmId(), stance);
/*      */     } 
/*      */   }
/*      */   
/*      */   void sendCreatureDamage(Creature creature, float currentDamage) {
/* 3243 */     if (creature != null && this.watcher != null && !creature.equals(this.watcher) && this.creatures
/* 3244 */       .containsKey(Long.valueOf(creature.getWurmId()))) {
/* 3245 */       this.watcher.getCommunicator().setCreatureDamage(creature.getWurmId(), currentDamage);
/*      */     }
/*      */   }
/*      */   
/*      */   void sendFishingLine(Creature creature, float posX, float posY, byte floatType) {
/* 3250 */     if (creature != null && this.watcher != null && !creature.equals(this.watcher) && this.creatures
/* 3251 */       .containsKey(Long.valueOf(creature.getWurmId()))) {
/* 3252 */       this.watcher.getCommunicator().sendFishCasted(creature.getWurmId(), posX, posY, floatType);
/*      */     }
/*      */   }
/*      */   
/*      */   void sendFishHooked(Creature creature, byte fishType, long fishId) {
/* 3257 */     if (creature != null && this.watcher != null && !creature.equals(this.watcher) && this.creatures
/* 3258 */       .containsKey(Long.valueOf(creature.getWurmId()))) {
/* 3259 */       this.watcher.getCommunicator().sendFishBite(fishType, fishId, creature.getWurmId());
/*      */     }
/*      */   }
/*      */   
/*      */   void sendFishingStopped(Creature creature) {
/* 3264 */     if (creature != null && this.watcher != null && !creature.equals(this.watcher) && this.creatures
/* 3265 */       .containsKey(Long.valueOf(creature.getWurmId()))) {
/* 3266 */       this.watcher.getCommunicator().sendFishSubCommand((byte)15, creature.getWurmId());
/*      */     }
/*      */   }
/*      */   
/*      */   void sendSpearStrike(Creature creature, float posX, float posY) {
/* 3271 */     if (creature != null && this.watcher != null && !creature.equals(this.watcher) && this.creatures
/* 3272 */       .containsKey(Long.valueOf(creature.getWurmId()))) {
/* 3273 */       this.watcher.getCommunicator().sendSpearStrike(creature.getWurmId(), posX, posY);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   void sendAttachCreature(long creatureId, long targetId, float offx, float offy, float offz, int seatId) {
/* 3279 */     boolean send = true;
/* 3280 */     if (targetId != -1L)
/*      */     {
/* 3282 */       if (WurmId.getType(targetId) == 1 || WurmId.getType(targetId) == 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3287 */         if (!this.creatures.containsKey(Long.valueOf(targetId))) {
/*      */           
/*      */           try {
/*      */             
/* 3291 */             addCreature(targetId, true);
/* 3292 */             send = false;
/*      */           }
/* 3294 */           catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */           
/* 3297 */           } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 3303 */       else if (WurmId.getType(targetId) == 2 || 
/* 3304 */         WurmId.getType(targetId) == 19 || 
/* 3305 */         WurmId.getType(targetId) == 20) {
/*      */ 
/*      */         
/*      */         try {
/* 3309 */           Item item = Items.getItem(targetId);
/*      */ 
/*      */ 
/*      */           
/* 3313 */           if (this.items == null || !this.items.contains(item))
/*      */           {
/*      */             
/* 3316 */             if (this.watcher.getVisionArea() != null) {
/*      */               
/* 3318 */               if (isOnSurface())
/*      */               {
/* 3320 */                 if (!item.isOnSurface())
/*      */                 {
/* 3322 */                   if (this.watcher.getVisionArea().getUnderGround() == null || 
/* 3323 */                     (this.watcher.getVisionArea().getUnderGround()).items == null || 
/* 3324 */                     !(this.watcher.getVisionArea().getUnderGround()).items.contains(item))
/*      */                   {
/* 3326 */                     if (this.watcher.getVisionArea().getUnderGround() != null) {
/*      */                       
/* 3328 */                       if (this.watcher.getVisionArea().getUnderGround().covers(item.getTileX(), item.getTileY()))
/*      */                       {
/* 3330 */                         this.watcher.getVisionArea().getUnderGround().addItem(item, null, true);
/*      */                       }
/*      */                     } else {
/*      */                       
/* 3334 */                       addItem(item, null, true);
/* 3335 */                     }  send = false;
/*      */                   
/*      */                   }
/*      */                 
/*      */                 }
/*      */               }
/* 3341 */               else if (item.isOnSurface())
/*      */               {
/* 3343 */                 if (this.watcher.getVisionArea().getSurface() == null || 
/* 3344 */                   (this.watcher.getVisionArea().getSurface()).items == null || 
/* 3345 */                   !(this.watcher.getVisionArea().getSurface()).items.contains(item))
/*      */                 {
/* 3347 */                   if (this.watcher.getVisionArea().getSurface() != null) {
/*      */                     
/* 3349 */                     this.watcher.getVisionArea().getSurface().addItem(item, null, true);
/*      */                   } else {
/*      */                     
/* 3352 */                     addItem(item, null, true);
/* 3353 */                   }  send = false;
/*      */                 }
/*      */               
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/* 3360 */               addItem(item, null, true);
/* 3361 */               send = false;
/*      */             }
/*      */           
/*      */           }
/* 3365 */         } catch (NoSuchItemException noSuchItemException) {}
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 3370 */     if (creatureId != -1L)
/*      */     {
/* 3372 */       if (WurmId.getType(creatureId) == 1 || WurmId.getType(creatureId) == 0) {
/*      */         
/* 3374 */         if (this.watcher.getWurmId() != creatureId && !this.creatures.containsKey(Long.valueOf(creatureId)))
/*      */         {
/* 3376 */           if (targetId != -1L) {
/*      */             
/*      */             try {
/* 3379 */               addCreature(creatureId, true);
/* 3380 */               send = false;
/*      */             }
/* 3382 */             catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */             
/* 3385 */             } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */           
/*      */           }
/*      */         }
/*      */       }
/* 3390 */       else if (WurmId.getType(creatureId) == 2 || 
/* 3391 */         WurmId.getType(creatureId) == 19 || 
/* 3392 */         WurmId.getType(creatureId) == 20) {
/*      */ 
/*      */         
/*      */         try {
/* 3396 */           Item item = Items.getItem(creatureId);
/* 3397 */           if (!this.items.contains(item)) {
/* 3398 */             addItem(item, null, true);
/*      */           }
/* 3400 */         } catch (NoSuchItemException noSuchItemException) {}
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 3406 */     if (send)
/*      */     {
/* 3408 */       if (creatureId == this.watcher.getWurmId()) {
/*      */         
/* 3410 */         this.watcher.getCommunicator().attachCreature(-1L, targetId, offx, offy, offz, seatId);
/*      */       }
/*      */       else {
/*      */         
/* 3414 */         this.watcher.getCommunicator().attachCreature(creatureId, targetId, offx, offy, offz, seatId);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendRotate(Item item, float rotation) {
/* 3421 */     if (this.items != null && this.items.contains(item))
/*      */     {
/* 3423 */       this.watcher.getCommunicator().sendRotate(item.getWurmId(), rotation);
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
/*      */   public String toString() {
/* 3435 */     return "VirtualZone [ID: " + this.id + ", Watcher: " + this.watcher.getWurmId() + ']';
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendHostileCreatures() {
/* 3440 */     int nums = 0;
/* 3441 */     String layer = "Above ground";
/* 3442 */     if (!this.isOnSurface)
/* 3443 */       layer = "Below ground"; 
/* 3444 */     for (Long c : this.creatures.keySet()) {
/*      */ 
/*      */       
/*      */       try {
/* 3448 */         Creature creat = Server.getInstance().getCreature(c.longValue());
/* 3449 */         if (creat.getAttitude(this.watcher) == 2) {
/*      */           
/* 3451 */           int tilex = creat.getTileX();
/* 3452 */           int tiley = creat.getTileY();
/* 3453 */           if (this.watcher.getCurrentTile() != null)
/*      */           {
/* 3455 */             nums++;
/* 3456 */             int ctx = (this.watcher.getCurrentTile()).tilex;
/* 3457 */             int cty = (this.watcher.getCurrentTile()).tiley;
/* 3458 */             int mindist = Math.max(Math.abs(tilex - ctx), Math.abs(tiley - cty));
/* 3459 */             int dir = MethodsCreatures.getDir(this.watcher, tilex, tiley);
/* 3460 */             String direction = MethodsCreatures.getLocationStringFor(this.watcher.getStatus().getRotation(), dir, "you");
/*      */ 
/*      */             
/* 3463 */             this.watcher.getCommunicator().sendNormalServerMessage(
/* 3464 */                 EndGameItems.getDistanceString(mindist, creat.getName(), direction, false) + layer);
/*      */           }
/*      */         
/*      */         } 
/* 3468 */       } catch (NoSuchCreatureException nsc) {
/*      */         
/* 3470 */         logger.log(Level.WARNING, nsc.getMessage(), (Throwable)nsc);
/*      */       }
/* 3472 */       catch (NoSuchPlayerException nsp) {
/*      */         
/* 3474 */         logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp);
/*      */       } 
/*      */     } 
/* 3477 */     if (nums == 0) {
/* 3478 */       this.watcher.getCommunicator().sendNormalServerMessage("No hostile creatures found " + layer.toLowerCase() + ".");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendAddTileEffect(int tilex, int tiley, int layer, byte effect, int floorLevel, boolean loop) {
/* 3484 */     this.watcher.getCommunicator().sendAddAreaSpellEffect(tilex, tiley, layer, effect, floorLevel, 0, loop);
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendRemoveTileEffect(int tilex, int tiley, int layer) {
/* 3489 */     this.watcher.getCommunicator().sendRemoveAreaSpellEffect(tilex, tiley, layer);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateWallDamageState(Wall wall) {
/* 3494 */     this.watcher.getCommunicator().sendWallDamageState(wall.getStructureId(), wall.getId(), (byte)(int)wall.getDamage());
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateFloorDamageState(Floor floor) {
/* 3499 */     this.watcher.getCommunicator().sendWallDamageState(floor.getStructureId(), floor.getId(), (byte)(int)floor.getDamage());
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateBridgePartDamageState(BridgePart bridgePart) {
/* 3504 */     this.watcher.getCommunicator().sendWallDamageState(bridgePart.getStructureId(), bridgePart.getId(), (byte)(int)bridgePart.getDamage());
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateFenceDamageState(Fence fence) {
/* 3509 */     this.watcher.getCommunicator().sendDamageState(fence.getId(), (byte)(int)fence.getDamage());
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateTargetStatus(long targetId, byte statusType, float status) {
/* 3514 */     this.watcher.getCommunicator().sendTargetStatus(targetId, statusType, status);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNewFace(Creature c) {
/* 3519 */     if (c.getWurmId() == this.watcher.getWurmId()) {
/* 3520 */       this.watcher.getCommunicator().sendNewFace(-10L, c.getFace());
/* 3521 */     } else if (containsCreature(c)) {
/* 3522 */       this.watcher.getCommunicator().sendNewFace(c.getWurmId(), c.getFace());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setNewRarityShader(Creature c) {
/* 3527 */     if (c.getWurmId() == this.watcher.getWurmId()) {
/* 3528 */       this.watcher.getCommunicator().updateCreatureRarity(-10L, c.getRarityShader());
/* 3529 */     } else if (containsCreature(c)) {
/* 3530 */       this.watcher.getCommunicator().updateCreatureRarity(c.getWurmId(), c.getRarityShader());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void sendActionControl(long creatureId, String actionString, boolean start, int timeLeft) {
/* 3535 */     if (creatureId == this.watcher.getWurmId()) {
/* 3536 */       this.watcher.getCommunicator().sendActionControl(-1L, actionString, start, timeLeft);
/*      */     } else {
/* 3538 */       this.watcher.getCommunicator().sendActionControl(creatureId, actionString, start, timeLeft);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendProjectile(long itemid, byte type, String modelName, String name, byte material, float _startX, float _startY, float startH, float rot, byte layer, float _endX, float _endY, float endH, long sourceId, long targetId, float projectedSecondsInAir, float actualSecondsInAir) {
/* 3546 */     this.watcher.getCommunicator().sendProjectile(itemid, type, modelName, name, material, _startX, _startY, startH, rot, layer, _endX, _endY, endH, sourceId, targetId, projectedSecondsInAir, actualSecondsInAir);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendNewProjectile(long itemid, byte type, String modelName, String name, byte material, Vector3f startingPosition, Vector3f startingVelocity, Vector3f endingPosition, float rotation, boolean surface) {
/* 3554 */     this.watcher.getCommunicator().sendNewProjectile(itemid, type, modelName, name, material, startingPosition, startingVelocity, endingPosition, rotation, surface);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void sendBridgeId(long creatureId, long bridgeId) {
/* 3559 */     this.watcher.getCommunicator().sendBridgeId(creatureId, bridgeId);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\VirtualZone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */