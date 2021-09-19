/*      */ package com.wurmonline.server.zones;
/*      */ 
/*      */ import com.wurmonline.math.Vector3f;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.HistoryManager;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.Message;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.MovementListener;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.behaviours.MethodsCreatures;
/*      */ import com.wurmonline.server.behaviours.Seat;
/*      */ import com.wurmonline.server.behaviours.Vehicle;
/*      */ import com.wurmonline.server.behaviours.Vehicles;
/*      */ import com.wurmonline.server.bodys.TempWound;
/*      */ import com.wurmonline.server.bodys.Wound;
/*      */ import com.wurmonline.server.combat.CombatEngine;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.MineDoorPermission;
/*      */ import com.wurmonline.server.creatures.NoArmourException;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.creatures.Wagoner;
/*      */ import com.wurmonline.server.economy.Economy;
/*      */ import com.wurmonline.server.economy.Shop;
/*      */ import com.wurmonline.server.effects.Effect;
/*      */ import com.wurmonline.server.epic.EpicTargetItems;
/*      */ import com.wurmonline.server.highways.HighwayFinder;
/*      */ import com.wurmonline.server.highways.HighwayPos;
/*      */ import com.wurmonline.server.highways.MethodsHighways;
/*      */ import com.wurmonline.server.highways.Node;
/*      */ import com.wurmonline.server.highways.Routes;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.ItemTypes;
/*      */ import com.wurmonline.server.items.NoSpaceException;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.items.WurmColor;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.sounds.Sound;
/*      */ import com.wurmonline.server.spells.Spell;
/*      */ import com.wurmonline.server.spells.SpellResist;
/*      */ import com.wurmonline.server.structures.Blocker;
/*      */ import com.wurmonline.server.structures.BridgePart;
/*      */ import com.wurmonline.server.structures.DbWall;
/*      */ import com.wurmonline.server.structures.Door;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.FenceGate;
/*      */ import com.wurmonline.server.structures.Floor;
/*      */ import com.wurmonline.server.structures.NoSuchStructureException;
/*      */ import com.wurmonline.server.structures.NoSuchWallException;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.StructureSupport;
/*      */ import com.wurmonline.server.structures.Structures;
/*      */ import com.wurmonline.server.structures.TempFence;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.villages.NoSuchRoleException;
/*      */ import com.wurmonline.server.villages.NoSuchVillageException;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.shared.constants.BridgeConstants;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import com.wurmonline.shared.constants.ItemMaterials;
/*      */ import com.wurmonline.shared.constants.StructureConstantsEnum;
/*      */ import com.wurmonline.shared.constants.StructureMaterialEnum;
/*      */ import com.wurmonline.shared.constants.StructureTypeEnum;
/*      */ import com.wurmonline.shared.util.MaterialUtilities;
/*      */ import com.wurmonline.shared.util.MulticolorLineSegment;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nonnull;
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
/*      */ public final class VolaTile
/*      */   implements MovementListener, ItemTypes, MiscConstants, CounterTypes, ItemMaterials
/*      */ {
/*  208 */   private static final Logger logger = Logger.getLogger(VolaTile.class.getName());
/*  209 */   private VolaTileItems vitems = null;
/*      */   
/*      */   private Structure structure;
/*      */   
/*      */   private Set<Effect> effects;
/*      */   private Set<Creature> creatures;
/*      */   private Set<Wall> walls;
/*      */   private Set<Floor> floors;
/*      */   private Set<BridgePart> bridgeParts;
/*      */   private Set<MineDoorPermission> mineDoors;
/*      */   public final int tilex;
/*      */   public final int tiley;
/*      */   private final boolean surfaced;
/*      */   private Set<VirtualZone> watchers;
/*      */   private final Zone zone;
/*      */   private Set<Door> doors;
/*      */   private Set<Door> unlockedDoors;
/*      */   private boolean inactive = false;
/*      */   private boolean isLava = false;
/*  228 */   private static final Set<StructureSupport> emptySupports = new HashSet<>();
/*      */ 
/*      */   
/*      */   private Map<Long, Fence> fences;
/*      */ 
/*      */   
/*      */   private Map<Long, Fence> magicFences;
/*      */ 
/*      */   
/*      */   private Village village;
/*      */   
/*      */   public boolean isTransition;
/*      */   
/*  241 */   private static final Creature[] emptyCreatures = new Creature[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  246 */   private static final Item[] emptyItems = new Item[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  251 */   private static final Wall[] emptyWalls = new Wall[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  256 */   private static final Fence[] emptyFences = new Fence[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  261 */   private static final Floor[] emptyFloors = new Floor[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  266 */   private static final BridgePart[] emptyBridgeParts = new BridgePart[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  271 */   private static final VirtualZone[] emptyWatchers = new VirtualZone[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  276 */   private static final Effect[] emptyEffects = new Effect[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  281 */   private static final Door[] emptyDoors = new Door[0];
/*      */   
/*  283 */   static final Set<Wall> toRemove = new HashSet<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   VolaTile(int x, int y, boolean isSurfaced, Set<VirtualZone> aWatchers, Zone zon) {
/*  291 */     this.tilex = x;
/*  292 */     this.tiley = y;
/*  293 */     this.surfaced = isSurfaced;
/*  294 */     this.zone = zon;
/*  295 */     this.watchers = aWatchers;
/*  296 */     checkTransition();
/*  297 */     checkIsLava();
/*      */   }
/*      */ 
/*      */   
/*      */   private final void checkTransition() {
/*  302 */     this.isTransition = (Tiles.decodeType(Server.caveMesh.getTile(this.tilex, this.tiley)) == Tiles.Tile.TILE_CAVE_EXIT.id);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isOnSurface() {
/*  307 */     return this.surfaced;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isLava() {
/*  317 */     return this.isLava;
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkIsLava() {
/*  322 */     this
/*  323 */       .isLava = ((isOnSurface() && Tiles.decodeType(Server.surfaceMesh.getTile(this.tilex, this.tiley)) == Tiles.Tile.TILE_LAVA.id) || (!isOnSurface() && Tiles.decodeType(Server.caveMesh.getTile(this.tilex, this.tiley)) == Tiles.Tile.TILE_CAVE_WALL_LAVA.id));
/*      */   }
/*      */ 
/*      */   
/*      */   public int getNumberOfItems(int floorLevel) {
/*  328 */     if (this.vitems == null)
/*  329 */       return 0; 
/*  330 */     return this.vitems.getNumberOfItems(floorLevel);
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getNumberOfDecorations(int floorLevel) {
/*  335 */     if (this.vitems == null)
/*  336 */       return 0; 
/*  337 */     return this.vitems.getNumberOfDecorations(floorLevel);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addFence(Fence fence) {
/*  342 */     if (this.fences == null)
/*  343 */       this.fences = new ConcurrentHashMap<>(); 
/*  344 */     if (fence.isMagic()) {
/*      */       
/*  346 */       if (this.magicFences == null)
/*  347 */         this.magicFences = new ConcurrentHashMap<>(); 
/*  348 */       this.magicFences.put(Long.valueOf(fence.getId()), fence);
/*      */     } 
/*  350 */     if (fence.isTemporary()) {
/*      */       
/*  352 */       Fence f = this.fences.get(Long.valueOf(fence.getId()));
/*  353 */       if (f != null && !f.isTemporary())
/*      */         return; 
/*      */     } 
/*  356 */     this.fences.put(Long.valueOf(fence.getId()), fence);
/*  357 */     if (fence.getZoneId() != this.zone.getId())
/*      */     {
/*  359 */       fence.setZoneId(this.zone.getId());
/*      */     }
/*  361 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/*  365 */         vz.addFence(fence);
/*      */       }
/*  367 */       catch (Exception e) {
/*      */         
/*  369 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setVillage(Village aVillage) {
/*  380 */     MineDoorPermission md = MineDoorPermission.getPermission(this.tilex, this.tiley);
/*  381 */     if (this.village == null || !this.village.equals(aVillage)) {
/*      */       
/*  383 */       if (this.doors != null)
/*      */       {
/*  385 */         for (Door door : this.doors) {
/*      */           
/*  387 */           if (door instanceof FenceGate) {
/*      */             
/*  389 */             if (aVillage != null) {
/*  390 */               aVillage.addGate((FenceGate)door); continue;
/*  391 */             }  if (this.village != null)
/*  392 */               this.village.removeGate((FenceGate)door); 
/*      */           } 
/*      */         } 
/*      */       }
/*  396 */       if (md != null)
/*      */       {
/*  398 */         if (aVillage != null) {
/*  399 */           aVillage.addMineDoor(md);
/*  400 */         } else if (this.village != null) {
/*  401 */           this.village.removeMineDoor(md);
/*      */         }  } 
/*  403 */       if (this.creatures != null)
/*      */       {
/*  405 */         for (Creature c : this.creatures) {
/*      */           
/*  407 */           c.setCurrentVillage(aVillage);
/*  408 */           if (c.isWagoner() && aVillage == null) {
/*      */             
/*  410 */             Wagoner wagoner = c.getWagoner();
/*  411 */             if (wagoner != null)
/*  412 */               wagoner.clrVillage(); 
/*      */           } 
/*  414 */           if (c.isNpcTrader() && c.getCitizenVillage() == null) {
/*      */             
/*  416 */             Shop s = Economy.getEconomy().getShop(c);
/*  417 */             if (s.getOwnerId() == -10L) {
/*      */               
/*  419 */               if (aVillage != null) {
/*      */ 
/*      */                 
/*      */                 try {
/*  423 */                   logger.log(Level.INFO, "Adding " + c.getName() + " as citizen to " + aVillage.getName());
/*  424 */                   aVillage.addCitizen(c, aVillage.getRoleForStatus((byte)3));
/*      */                 }
/*  426 */                 catch (IOException iox) {
/*      */                   
/*  428 */                   logger.log(Level.INFO, iox.getMessage());
/*      */                 }
/*  430 */                 catch (NoSuchRoleException nsx) {
/*      */                   
/*  432 */                   logger.log(Level.INFO, nsx.getMessage());
/*      */                 } 
/*      */ 
/*      */ 
/*      */                 
/*      */                 continue;
/*      */               } 
/*      */ 
/*      */               
/*  441 */               c.setCitizenVillage(null);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*      */       
/*  447 */       if (this.vitems != null)
/*      */       {
/*  449 */         for (Item i : this.vitems.getAllItemsAsSet()) {
/*      */           
/*  451 */           if (i.getTemplateId() == 757) {
/*      */             
/*  453 */             if (aVillage != null) {
/*  454 */               aVillage.addBarrel(i); continue;
/*  455 */             }  if (this.village != null)
/*  456 */               this.village.removeBarrel(i);  continue;
/*      */           } 
/*  458 */           if (i.getTemplateId() == 1112) {
/*      */             
/*  460 */             if (aVillage != null) {
/*      */               
/*  462 */               Node node = Routes.getNode(i.getWurmId());
/*  463 */               if (node != null)
/*  464 */                 node.setVillage(aVillage);  continue;
/*      */             } 
/*  466 */             if (this.village != null) {
/*      */               
/*  468 */               Node node = Routes.getNode(i.getWurmId());
/*  469 */               if (node != null)
/*  470 */                 node.setVillage(null); 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*  475 */       this.village = aVillage;
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  480 */       if (this.doors != null)
/*      */       {
/*  482 */         for (Door door : this.doors) {
/*      */           
/*  484 */           if (door instanceof FenceGate)
/*  485 */             aVillage.addGate((FenceGate)door); 
/*      */         } 
/*      */       }
/*  488 */       if (md != null)
/*      */       {
/*  490 */         aVillage.addMineDoor(md);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Village getVillage() {
/*  501 */     return this.village;
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeFence(Fence fence) {
/*  506 */     if (this.fences != null) {
/*      */       
/*  508 */       Fence f = this.fences.remove(Long.valueOf(fence.getId()));
/*      */       
/*  510 */       if (f != null) {
/*      */         
/*  512 */         if (f.isMagic())
/*      */         {
/*  514 */           if (this.magicFences != null) {
/*      */             
/*  516 */             this.magicFences.remove(Long.valueOf(fence.getId()));
/*  517 */             if (this.magicFences.isEmpty())
/*  518 */               this.magicFences = null; 
/*      */           } 
/*      */         }
/*  521 */         if (fence.isTemporary() && !f.isTemporary()) {
/*      */           
/*  523 */           this.fences.put(Long.valueOf(f.getId()), f);
/*      */         }
/*      */         else {
/*      */           
/*  527 */           for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */             
/*      */             try {
/*  531 */               vz.removeFence(f);
/*      */             }
/*  533 */             catch (Exception e) {
/*      */               
/*  535 */               logger.log(Level.WARNING, e.getMessage(), e);
/*      */             } 
/*      */           } 
/*  538 */           if (this.fences.isEmpty()) {
/*  539 */             this.fences = null;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void addSound(Sound sound) {
/*  548 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/*  552 */         vz.playSound(sound);
/*      */       }
/*  554 */       catch (Exception e) {
/*      */         
/*  556 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateFence(Fence fence) {
/*  563 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/*  567 */         vz.removeFence(fence);
/*  568 */         vz.addFence(fence);
/*      */       }
/*  570 */       catch (Exception e) {
/*      */         
/*  572 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateMagicalFence(Fence fence) {
/*  579 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/*      */ 
/*      */ 
/*      */         
/*  586 */         vz.addFence(fence);
/*      */       }
/*  588 */       catch (Exception e) {
/*      */         
/*  590 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Fence[] getFences() {
/*  601 */     if (this.fences != null)
/*  602 */       return (Fence[])this.fences.values().toArray((Object[])new Fence[this.fences.size()]); 
/*  603 */     return emptyFences;
/*      */   }
/*      */ 
/*      */   
/*      */   public Collection<Fence> getFencesList() {
/*  608 */     if (this.fences != null) {
/*  609 */       return this.fences.values();
/*      */     }
/*  611 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Fence[] getAllFences() {
/*  622 */     Set<Fence> fenceSet = new HashSet<>();
/*  623 */     if (this.fences != null)
/*      */     {
/*  625 */       for (Fence f : this.fences.values())
/*      */       {
/*  627 */         fenceSet.add(f);
/*      */       }
/*      */     }
/*      */     
/*  631 */     VolaTile eastTile = this.zone.getTileOrNull(this.tilex + 1, this.tiley);
/*  632 */     if (eastTile != null) {
/*      */       
/*  634 */       Fence[] eastFences = eastTile.getFencesForDir(Tiles.TileBorderDirection.DIR_DOWN);
/*  635 */       for (int x = 0; x < eastFences.length; x++)
/*      */       {
/*  637 */         fenceSet.add(eastFences[x]);
/*      */       }
/*      */     } 
/*      */     
/*  641 */     VolaTile southTile = this.zone.getTileOrNull(this.tilex, this.tiley + 1);
/*  642 */     if (southTile != null) {
/*      */       
/*  644 */       Fence[] southFences = southTile.getFencesForDir(Tiles.TileBorderDirection.DIR_HORIZ);
/*  645 */       for (int x = 0; x < southFences.length; x++)
/*      */       {
/*  647 */         fenceSet.add(southFences[x]);
/*      */       }
/*      */     } 
/*      */     
/*  651 */     if (fenceSet.size() == 0) {
/*  652 */       return emptyFences;
/*      */     }
/*  654 */     return fenceSet.<Fence>toArray(new Fence[fenceSet.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasFenceOnCorner(int floorLevel) {
/*  665 */     if (this.fences != null)
/*      */     {
/*  667 */       if ((getFencesForLevel(floorLevel)).length > 0)
/*  668 */         return true; 
/*      */     }
/*  670 */     VolaTile westTile = this.zone.getTileOrNull(this.tilex - 1, this.tiley);
/*  671 */     if (westTile != null) {
/*      */       
/*  673 */       Fence[] westFences = westTile.getFencesForDirAndLevel(Tiles.TileBorderDirection.DIR_HORIZ, floorLevel);
/*  674 */       if (westFences.length > 0)
/*  675 */         return true; 
/*      */     } 
/*  677 */     VolaTile northTile = this.zone.getTileOrNull(this.tilex, this.tiley - 1);
/*  678 */     if (northTile != null) {
/*      */       
/*  680 */       Fence[] northFences = northTile.getFencesForDirAndLevel(Tiles.TileBorderDirection.DIR_DOWN, floorLevel);
/*  681 */       if (northFences.length > 0)
/*  682 */         return true; 
/*      */     } 
/*  684 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Fence[] getFencesForDirAndLevel(Tiles.TileBorderDirection dir, int floorLevel) {
/*  693 */     if (this.fences != null) {
/*      */       
/*  695 */       Set<Fence> fenceSet = new HashSet<>();
/*  696 */       for (Fence f : this.fences.values()) {
/*      */         
/*  698 */         if (f.getDir() == dir && f.getFloorLevel() == floorLevel)
/*  699 */           fenceSet.add(f); 
/*      */       } 
/*  701 */       return fenceSet.<Fence>toArray(new Fence[fenceSet.size()]);
/*      */     } 
/*      */     
/*  704 */     return emptyFences;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Fence[] getFencesForDir(Tiles.TileBorderDirection dir) {
/*  713 */     if (this.fences != null) {
/*      */       
/*  715 */       Set<Fence> fenceSet = new HashSet<>();
/*  716 */       for (Fence f : this.fences.values()) {
/*      */         
/*  718 */         if (f.getDir() == dir)
/*  719 */           fenceSet.add(f); 
/*      */       } 
/*  721 */       return fenceSet.<Fence>toArray(new Fence[fenceSet.size()]);
/*      */     } 
/*      */     
/*  724 */     return emptyFences;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Fence[] getFencesForLevel(int floorLevel) {
/*  733 */     if (this.fences != null) {
/*      */       
/*  735 */       Set<Fence> fenceSet = new HashSet<>();
/*  736 */       for (Fence f : this.fences.values()) {
/*      */         
/*  738 */         if (f.getFloorLevel() == floorLevel)
/*  739 */           fenceSet.add(f); 
/*      */       } 
/*  741 */       return fenceSet.<Fence>toArray(new Fence[fenceSet.size()]);
/*      */     } 
/*      */     
/*  744 */     return emptyFences;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Fence getFence(long id) {
/*  753 */     if (this.fences != null)
/*  754 */       return this.fences.get(Long.valueOf(id)); 
/*  755 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addDoor(Door door) {
/*  760 */     if (this.doors == null)
/*  761 */       this.doors = new HashSet<>(); 
/*  762 */     if (!this.doors.contains(door)) {
/*      */       
/*  764 */       this.doors.add(door);
/*      */       
/*  766 */       if (this.watchers != null)
/*      */       {
/*  768 */         for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */           
/*      */           try {
/*  772 */             door.addWatcher(vz);
/*      */           }
/*  774 */           catch (Exception e) {
/*      */             
/*  776 */             logger.log(Level.WARNING, e.getMessage(), e);
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeDoor(Door door) {
/*  785 */     if (this.doors != null)
/*      */     {
/*  787 */       if (this.doors.contains(door)) {
/*      */         
/*  789 */         if (this.watchers != null)
/*      */         {
/*  791 */           for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */             
/*      */             try {
/*  795 */               door.removeWatcher(vz);
/*      */             }
/*  797 */             catch (Exception e) {
/*      */               
/*  799 */               logger.log(Level.WARNING, e.getMessage(), e);
/*      */             } 
/*      */           } 
/*      */         }
/*  803 */         this.doors.remove(door);
/*      */         
/*  805 */         if (this.unlockedDoors != null)
/*  806 */           this.unlockedDoors.remove(door); 
/*  807 */         if (this.doors.isEmpty()) {
/*  808 */           this.doors = null;
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public void addUnlockedDoor(Door door) {
/*  815 */     if (this.unlockedDoors == null)
/*  816 */       this.unlockedDoors = new HashSet<>(); 
/*  817 */     if (!this.unlockedDoors.contains(door))
/*      */     {
/*  819 */       this.unlockedDoors.add(door);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeUnlockedDoor(Door door) {
/*  825 */     if (this.unlockedDoors == null) {
/*      */       return;
/*      */     }
/*  828 */     this.unlockedDoors.remove(door);
/*      */     
/*  830 */     if (this.unlockedDoors.isEmpty()) {
/*  831 */       this.unlockedDoors = null;
/*      */     }
/*      */   }
/*      */   
/*      */   public void addMineDoor(MineDoorPermission door) {
/*  836 */     if (this.mineDoors == null)
/*  837 */       this.mineDoors = new HashSet<>(); 
/*  838 */     if (this.mineDoors != null)
/*      */     {
/*  840 */       if (!this.mineDoors.contains(door)) {
/*      */         
/*  842 */         this.mineDoors.add(door);
/*      */         
/*  844 */         if (this.watchers != null)
/*      */         {
/*  846 */           for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */             
/*      */             try {
/*  850 */               door.addWatcher(vz);
/*  851 */               vz.addMineDoor(door);
/*      */             }
/*  853 */             catch (Exception e) {
/*      */               
/*  855 */               logger.log(Level.WARNING, e.getMessage(), e);
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeMineDoor(MineDoorPermission door) {
/*  865 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/*  869 */         door.removeWatcher(vz);
/*  870 */         vz.removeMineDoor(door);
/*      */       }
/*  872 */       catch (Exception e) {
/*      */         
/*  874 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*  877 */     if (this.mineDoors == null) {
/*      */       return;
/*      */     }
/*  880 */     this.mineDoors.remove(door);
/*  881 */     if (this.mineDoors.isEmpty()) {
/*  882 */       this.mineDoors = null;
/*      */     }
/*      */   }
/*      */   
/*      */   public void checkChangedAttitude(Creature creature) {
/*  887 */     if (this.watchers != null)
/*      */     {
/*  889 */       for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */         
/*      */         try {
/*  893 */           vz.sendAttitude(creature);
/*      */         }
/*  895 */         catch (Exception e) {
/*      */           
/*  897 */           logger.log(Level.WARNING, e.getMessage(), e);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendUpdateTarget(Creature creature) {
/*  905 */     if (this.watchers != null)
/*      */     {
/*  907 */       for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */         
/*      */         try {
/*  911 */           vz.sendUpdateHasTarget(creature);
/*      */         }
/*  913 */         catch (Exception e) {
/*      */           
/*  915 */           logger.log(Level.WARNING, e.getMessage(), e);
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
/*      */   public Door[] getDoors() {
/*  927 */     if (this.doors != null && this.doors.size() > 0)
/*  928 */       return this.doors.<Door>toArray(new Door[this.doors.size()]); 
/*  929 */     return emptyDoors;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getTileX() {
/*  934 */     return this.tilex;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getTileY() {
/*  939 */     return this.tiley;
/*      */   }
/*      */ 
/*      */   
/*      */   public final float getPosX() {
/*  944 */     return ((this.tilex << 2) + 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public final float getPosY() {
/*  949 */     return ((this.tiley << 2) + 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void pollMagicFences(long time) {
/*  954 */     if (this.magicFences != null)
/*      */     {
/*  956 */       for (Fence f : this.magicFences.values())
/*      */       {
/*  958 */         f.pollMagicFences(time);
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
/*      */   
/*      */   public void pollStructures(long time) {
/*  973 */     if (this.floors != null)
/*      */     {
/*  975 */       for (Floor floor : getFloors()) {
/*  976 */         if (floor.poll(time, this, this.structure))
/*  977 */           removeFloor(floor); 
/*      */       }  } 
/*  979 */     if (this.walls != null) {
/*      */       
/*  981 */       Wall[] lTempWalls = getWalls();
/*  982 */       for (int x = 0; x < lTempWalls.length; x++)
/*  983 */         lTempWalls[x].poll(time, this, this.structure); 
/*      */     } 
/*  985 */     if (this.fences != null)
/*      */     {
/*  987 */       for (Fence f : getFences())
/*  988 */         f.poll(time); 
/*      */     }
/*  990 */     if (this.bridgeParts != null)
/*      */     {
/*  992 */       for (BridgePart bridgePart : getBridgeParts()) {
/*  993 */         if (bridgePart.poll(time, this.structure))
/*  994 */           removeBridgePart(bridgePart); 
/*      */       } 
/*      */     }
/*  997 */     if (this.structure != null) {
/*  998 */       this.structure.poll(time);
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
/*      */ 
/*      */ 
/*      */   
/*      */   public void poll(boolean pollItems, int seed, boolean setAreaEffectFlag) {
/* 1016 */     boolean lava = isLava();
/*      */     
/* 1018 */     long now = System.nanoTime();
/* 1019 */     if (this.vitems != null)
/* 1020 */       this.vitems.poll(pollItems, seed, lava, this.structure, isOnSurface(), this.village, now); 
/* 1021 */     pollMagicFences(now);
/* 1022 */     if (lava)
/* 1023 */       for (Creature c : getCreatures())
/*      */       {
/* 1025 */         c.setDoLavaDamage(true);
/*      */       } 
/* 1027 */     if (setAreaEffectFlag)
/*      */     {
/* 1029 */       if (getAreaEffect() != null)
/*      */       {
/* 1031 */         for (Creature c : getCreatures())
/*      */         {
/* 1033 */           c.setDoAreaEffect(true);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1043 */     pollAllUnlockedDoorsOnThisTile();
/*      */     
/* 1045 */     applyLavaDamageToWallsAndFences();
/*      */     
/* 1047 */     checkDeletion();
/*      */     
/* 1049 */     if (Servers.isThisAPvpServer())
/*      */     {
/* 1051 */       pollOnDeedEnemys();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void pollOnDeedEnemys() {
/* 1057 */     if (getVillage() != null)
/*      */     {
/* 1059 */       for (Creature c : getCreatures()) {
/*      */         
/* 1061 */         if (c.getPower() < 1 && c.isPlayer() && (getVillage()).kingdom != c.getKingdomId()) {
/*      */           
/*      */           try {
/*      */             
/* 1065 */             c.currentVillage.getToken().setLastOwnerId(System.currentTimeMillis());
/*      */           }
/* 1067 */           catch (NoSuchItemException e) {
/*      */             
/* 1069 */             e.printStackTrace();
/*      */           } 
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean doAreaDamage(Creature aCreature) {
/* 1079 */     boolean dead = false;
/* 1080 */     if (!aCreature.isInvulnerable() && !aCreature.isGhost() && !aCreature.isUnique()) {
/*      */       
/* 1082 */       AreaSpellEffect aes = getAreaEffect();
/*      */       
/* 1084 */       if (aes != null) {
/*      */         
/* 1086 */         System.out.println("AREA DAMAGE " + aCreature.getName());
/*      */         
/* 1088 */         if (aes.getFloorLevel() != aCreature.getFloorLevel()) {
/*      */           
/* 1090 */           int heightOffset = aes.getHeightOffset();
/* 1091 */           if (heightOffset != 0) {
/*      */             
/* 1093 */             int pz = aCreature.getPosZDirts();
/* 1094 */             if (Math.abs(pz - heightOffset) > 10) {
/*      */               
/* 1096 */               System.out.println("AREA DAMAGE FAILED");
/* 1097 */               return false;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1104 */         byte type = getAreaEffect().getType();
/* 1105 */         Creature caster = null;
/*      */         
/*      */         try {
/* 1108 */           caster = Server.getInstance().getCreature(aes.getCreator());
/*      */         }
/* 1110 */         catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */         
/*      */         }
/* 1114 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */ 
/*      */         
/* 1118 */         if (caster != null) {
/*      */           
/*      */           try {
/*      */             
/* 1122 */             if (aCreature.getAttitude(caster) == 2 || (caster
/* 1123 */               .getCitizenVillage() != null && caster.getCitizenVillage().isEnemy(aCreature)))
/*      */             {
/* 1125 */               boolean ok = true;
/* 1126 */               if (!caster.isOnPvPServer() || !aCreature.isOnPvPServer()) {
/*      */                 
/* 1128 */                 Village v = aCreature.getCurrentVillage();
/* 1129 */                 if (v != null && !v.mayAttack(caster, aCreature))
/*      */                 {
/* 1131 */                   ok = false;
/*      */                 }
/*      */               } 
/* 1134 */               if (ok)
/*      */               {
/* 1136 */                 aCreature.addAttacker(caster);
/* 1137 */                 if (type == 36 || type == 53)
/*      */                 {
/* 1139 */                   byte pos = aCreature.getBody().getRandomWoundPos();
/* 1140 */                   sendAttachCreatureEffect(aCreature, (byte)6, (byte)0, (byte)0, (byte)0, (byte)0);
/*      */ 
/*      */ 
/*      */                   
/* 1144 */                   double damage = getAreaEffect().getPower() * 4.0D;
/* 1145 */                   damage += 150.0D;
/* 1146 */                   double resistance = SpellResist.getSpellResistance(aCreature, 414);
/* 1147 */                   damage *= resistance;
/*      */                   
/* 1149 */                   SpellResist.addSpellResistance(aCreature, 414, damage);
/*      */                   
/* 1151 */                   damage = Spell.modifyDamage(aCreature, damage);
/*      */                   
/* 1153 */                   dead = CombatEngine.addWound(caster, aCreature, (byte)8, pos, damage, 1.0F, "", null, 0.0F, 0.0F, false, false, true, true);
/*      */ 
/*      */ 
/*      */                 
/*      */                 }
/* 1158 */                 else if (type == 35 || type == 51)
/*      */                 {
/* 1160 */                   byte pos = aCreature.getBody().getRandomWoundPos();
/*      */                   
/* 1162 */                   double damage = getAreaEffect().getPower() * 2.75D;
/* 1163 */                   damage += 300.0D;
/* 1164 */                   double resistance = SpellResist.getSpellResistance(aCreature, 420);
/* 1165 */                   damage *= resistance;
/*      */                   
/* 1167 */                   SpellResist.addSpellResistance(aCreature, 420, damage);
/*      */                   
/* 1169 */                   damage = Spell.modifyDamage(aCreature, damage);
/* 1170 */                   dead = CombatEngine.addWound(caster, aCreature, (byte)4, pos, damage, 1.0F, "", null, 0.0F, 0.0F, false, false, true, true);
/*      */ 
/*      */ 
/*      */                 
/*      */                 }
/* 1175 */                 else if (type == 34)
/*      */                 {
/* 1177 */                   byte pos = aCreature.getBody().getRandomWoundPos();
/*      */                   
/* 1179 */                   double damage = getAreaEffect().getPower() * 1.0D;
/* 1180 */                   damage += 400.0D;
/* 1181 */                   double resistance = SpellResist.getSpellResistance(aCreature, 418);
/* 1182 */                   damage *= resistance;
/*      */                   
/* 1184 */                   SpellResist.addSpellResistance(aCreature, 418, damage);
/*      */                   
/* 1186 */                   damage = Spell.modifyDamage(aCreature, damage);
/*      */                   
/* 1188 */                   dead = CombatEngine.addWound(caster, aCreature, (byte)0, pos, damage, 1.0F, "", null, 1.0F, 0.0F, false, false, true, true);
/*      */ 
/*      */ 
/*      */                 
/*      */                 }
/* 1193 */                 else if (type == 37)
/*      */                 {
/* 1195 */                   sendAttachCreatureEffect(aCreature, (byte)7, (byte)0, (byte)0, (byte)0, (byte)0);
/*      */ 
/*      */                   
/* 1198 */                   byte pos = aCreature.getBody().getRandomWoundPos();
/*      */ 
/*      */                   
/* 1201 */                   double damage = getAreaEffect().getPower() * 2.0D;
/* 1202 */                   damage += 350.0D;
/* 1203 */                   double resistance = SpellResist.getSpellResistance(aCreature, 433);
/* 1204 */                   damage *= resistance;
/*      */                   
/* 1206 */                   SpellResist.addSpellResistance(aCreature, 433, damage);
/*      */                   
/* 1208 */                   damage = Spell.modifyDamage(aCreature, damage);
/*      */                   
/* 1210 */                   dead = CombatEngine.addWound(caster, aCreature, (byte)5, pos, damage, 1.0F, "", null, 0.0F, 3.0F, false, false, true, true);
/*      */                 }
/*      */               
/*      */               }
/*      */             
/*      */             }
/*      */           
/* 1217 */           } catch (Exception exe) {
/*      */             
/* 1219 */             logger.log(Level.WARNING, exe.getMessage(), exe);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/* 1224 */     return dead;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void pollAllUnlockedDoorsOnThisTile() {
/* 1234 */     if (this.unlockedDoors != null && this.unlockedDoors.size() > 0)
/*      */     {
/* 1236 */       for (Iterator<Door> it = this.unlockedDoors.iterator(); it.hasNext();) {
/*      */         
/* 1238 */         if (((Door)it.next()).pollUnlocked()) {
/* 1239 */           it.remove();
/*      */         }
/*      */       } 
/*      */     }
/* 1243 */     if (this.unlockedDoors != null && this.unlockedDoors.isEmpty()) {
/* 1244 */       this.unlockedDoors = null;
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
/*      */   private void applyLavaDamageToWallsAndFences() {
/* 1257 */     if (isLava()) {
/*      */       
/* 1259 */       if (this.walls != null) {
/*      */         
/* 1261 */         Wall[] lTempWalls = getWalls();
/* 1262 */         for (int x = 0; x < lTempWalls.length; x++)
/*      */         {
/* 1264 */           lTempWalls[x].setDamage(lTempWalls[x].getDamage() + 1.0F);
/*      */         }
/*      */       } 
/* 1267 */       if (this.fences != null)
/*      */       {
/* 1269 */         for (Fence f : getFences()) {
/* 1270 */           f.setDamage(f.getDamage() + 1.0F);
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
/*      */ 
/*      */   
/*      */   private void pollAllWatchersOfThisTile() {
/* 1287 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 1291 */         if (vz.getWatcher() instanceof Player)
/*      */         {
/* 1293 */           if (!vz.getWatcher().hasLink())
/*      */           {
/*      */ 
/*      */             
/* 1297 */             removeWatcher(vz);
/*      */           }
/*      */         }
/*      */       }
/* 1301 */       catch (Exception e) {
/*      */         
/* 1303 */         logger.log(Level.WARNING, e.getMessage(), e);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void pollAllCreaturesOnThisTile(boolean lava, boolean areaEffect) {
/* 1322 */     long lStart = System.nanoTime();
/*      */     
/* 1324 */     Creature[] lTempCreatures = getCreatures();
/* 1325 */     for (int x = 0; x < lTempCreatures.length; x++)
/*      */     {
/* 1327 */       pollOneCreatureOnThisTile(lava, lTempCreatures[x], areaEffect);
/*      */     }
/*      */     
/* 1330 */     if ((float)(System.nanoTime() - lStart) / 1000000.0F > 300.0F && !Servers.localServer.testServer) {
/*      */       
/* 1332 */       int destroyed = 0;
/* 1333 */       for (int y = 0; y < lTempCreatures.length; y++) {
/*      */         
/* 1335 */         if (lTempCreatures[y].isDead())
/* 1336 */           destroyed++; 
/*      */       } 
/* 1338 */       logger.log(Level.INFO, "Tile at " + this.tilex + ", " + this.tiley + " polled " + lTempCreatures.length + " creatures. Of those were " + destroyed + " destroyed. It took " + (
/*      */           
/* 1340 */           (float)(System.nanoTime() - lStart) / 1000000.0F) + " ms");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isVisibleToPlayers() {
/* 1346 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 1350 */         if (vz.getWatcher() != null && vz.getWatcher().isPlayer()) {
/* 1351 */           return true;
/*      */         }
/* 1353 */       } catch (Exception e) {
/*      */         
/* 1355 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/* 1358 */     return false;
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
/*      */   private void pollOneCreatureOnThisTile(boolean lava, Creature aCreature, boolean areaEffect) {
/*      */     try {
/* 1373 */       boolean dead = false;
/* 1374 */       if (aCreature.poll()) {
/* 1375 */         deleteCreature(aCreature);
/* 1376 */       } else if (lava) {
/*      */         
/* 1378 */         if (!aCreature.isInvulnerable() && !aCreature.isGhost() && !aCreature.isUnique())
/*      */         {
/* 1380 */           if (aCreature.getDeity() == null || !aCreature.getDeity().isMountainGod() || aCreature.getFaith() < 35.0F)
/*      */           {
/* 1382 */             if (aCreature.getFarwalkerSeconds() <= 0) {
/*      */               
/* 1384 */               Wound wound = null;
/*      */ 
/*      */               
/*      */               try {
/* 1388 */                 byte pos = aCreature.getBody().getRandomWoundPos((byte)10);
/* 1389 */                 if (Server.rand.nextInt(10) <= 6)
/*      */                 {
/* 1391 */                   if (aCreature.getBody().getWounds() != null) {
/*      */                     
/* 1393 */                     wound = aCreature.getBody().getWounds().getWoundAtLocation(pos);
/* 1394 */                     if (wound != null) {
/*      */                       
/* 1396 */                       dead = wound.modifySeverity(
/* 1397 */                           (int)(5000.0F + Server.rand.nextInt(5000) * (100.0F - aCreature.getSpellDamageProtectBonus()) / 100.0F));
/* 1398 */                       wound.setBandaged(false);
/* 1399 */                       aCreature.setWounded();
/*      */                     } 
/*      */                   } 
/*      */                 }
/* 1403 */                 if (wound == null)
/*      */                 {
/* 1405 */                   if (!aCreature.isGhost() && !aCreature.isUnique() && 
/* 1406 */                     !aCreature.isKingdomGuard())
/*      */                   {
/* 1408 */                     dead = aCreature.addWoundOfType(null, (byte)4, pos, false, 1.0F, true, (5000.0F + Server.rand
/* 1409 */                         .nextInt(5000) * (100.0F - aCreature.getSpellDamageProtectBonus()) / 100.0F), 0.0F, 0.0F, false, false);
/*      */                   }
/*      */                 }
/* 1412 */                 aCreature.getCommunicator().sendAlertServerMessage("You are burnt by lava!");
/* 1413 */                 if (dead)
/*      */                 {
/* 1415 */                   aCreature.achievement(142);
/* 1416 */                   deleteCreature(aCreature);
/*      */                 }
/*      */               
/* 1419 */               } catch (Exception ex) {
/*      */                 
/* 1421 */                 logger.log(Level.WARNING, aCreature.getName() + " " + ex.getMessage(), ex);
/*      */               }
/*      */             
/*      */             } 
/*      */           }
/*      */         }
/* 1427 */       } else if (!dead && areaEffect) {
/*      */         
/* 1429 */         if (!aCreature.isInvulnerable() && !aCreature.isGhost() && !aCreature.isUnique()) {
/*      */           
/* 1431 */           AreaSpellEffect aes = getAreaEffect();
/* 1432 */           if (aes != null && aes.getFloorLevel() == aCreature.getFloorLevel()) {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1437 */             byte type = aes.getType();
/* 1438 */             Creature caster = null;
/*      */             
/*      */             try {
/* 1441 */               caster = Server.getInstance().getCreature(aes.getCreator());
/*      */             }
/* 1443 */             catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */             
/*      */             }
/* 1447 */             catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */ 
/*      */             
/* 1451 */             if (caster != null) {
/*      */               
/*      */               try {
/*      */                 
/* 1455 */                 if (aCreature.getAttitude(caster) == 2 || (caster
/* 1456 */                   .getCitizenVillage() != null && caster.getCitizenVillage().isEnemy(aCreature)))
/*      */                 {
/* 1458 */                   boolean ok = true;
/* 1459 */                   if (!caster.isOnPvPServer() || !aCreature.isOnPvPServer()) {
/*      */                     
/* 1461 */                     Village v = aCreature.getCurrentVillage();
/* 1462 */                     if (v != null && !v.mayAttack(caster, aCreature))
/*      */                     {
/* 1464 */                       ok = false;
/*      */                     }
/*      */                   } 
/* 1467 */                   if (ok)
/*      */                   {
/* 1469 */                     aCreature.addAttacker(caster);
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
/*      */                   }
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
/*      */                 }
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
/*      */               }
/* 1532 */               catch (Exception exe) {
/*      */                 
/* 1534 */                 logger.log(Level.WARNING, exe.getMessage(), exe);
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/* 1540 */     } catch (Exception ex) {
/*      */       
/* 1542 */       logger.log(Level.WARNING, "Failed to poll creature " + aCreature.getWurmId() + " " + ex.getMessage(), ex);
/*      */       
/*      */       try {
/* 1545 */         Server.getInstance().getCreature(aCreature.getWurmId());
/*      */       }
/* 1547 */       catch (Exception nsc) {
/*      */         
/* 1549 */         logger.log(Level.INFO, "Failed to locate creature. Removing from tile. Creature: " + aCreature);
/* 1550 */         if (this.creatures != null) {
/* 1551 */           this.creatures.remove(aCreature);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void deleteCreature(Creature creature) {
/* 1558 */     creature.setNewTile(null, 0.0F, false);
/* 1559 */     removeCreature(creature);
/*      */     
/*      */     try {
/* 1562 */       this.zone.deleteCreature(creature, false);
/*      */     }
/* 1564 */     catch (NoSuchCreatureException nsc) {
/*      */       
/* 1566 */       logger.log(Level.WARNING, nsc.getMessage(), (Throwable)nsc);
/*      */     }
/* 1568 */     catch (NoSuchPlayerException nsp) {
/*      */       
/* 1570 */       logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp);
/*      */     } 
/* 1572 */     Door[] _doors = getDoors();
/* 1573 */     for (int d = 0; d < _doors.length; d++) {
/* 1574 */       _doors[d].removeCreature(creature);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean containsCreature(Creature creature) {
/* 1585 */     return (this.creatures != null && this.creatures.contains(creature));
/*      */   }
/*      */ 
/*      */   
/*      */   public void deleteCreatureQuick(Creature creature) {
/* 1590 */     creature.setNewTile(null, 0.0F, false);
/* 1591 */     this.zone.removeCreature(creature, true, false);
/* 1592 */     Door[] lDoors = getDoors();
/* 1593 */     for (int d = 0; d < lDoors.length; d++) {
/* 1594 */       lDoors[d].removeCreature(creature);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void broadCastMulticolored(List<MulticolorLineSegment> segments, Creature performer, @Nullable Creature receiver, boolean combat, byte onScreenMessage) {
/* 1600 */     if (this.creatures != null)
/*      */     {
/* 1602 */       for (Iterator<Creature> it = this.creatures.iterator(); it.hasNext(); ) {
/*      */         
/* 1604 */         Creature creature = it.next();
/* 1605 */         if (!creature.equals(performer) && (receiver == null || !creature.equals(receiver)))
/*      */         {
/* 1607 */           if (performer.isVisibleTo(creature))
/*      */           {
/* 1609 */             if (!creature.getCommunicator().isInvulnerable()) {
/*      */               
/* 1611 */               if (combat) {
/*      */                 
/* 1613 */                 creature.getCommunicator().sendColoredMessageCombat(segments, onScreenMessage);
/*      */                 
/*      */                 continue;
/*      */               } 
/* 1617 */               creature.getCommunicator().sendColoredMessageEvent(segments, onScreenMessage);
/*      */             } 
/*      */           }
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void broadCastAction(String message, Creature performer, boolean combat) {
/* 1628 */     broadCastAction(message, performer, null, combat);
/*      */   }
/*      */ 
/*      */   
/*      */   public void broadCastAction(String message, Creature performer, @Nullable Creature receiver, boolean combat) {
/* 1633 */     if (this.creatures != null)
/*      */     {
/* 1635 */       for (Iterator<Creature> it = this.creatures.iterator(); it.hasNext(); ) {
/*      */         
/* 1637 */         Creature creature = it.next();
/* 1638 */         if (!creature.equals(performer) && (receiver == null || !creature.equals(receiver)))
/*      */         {
/* 1640 */           if (performer.isVisibleTo(creature))
/*      */           {
/* 1642 */             if (!creature.getCommunicator().isInvulnerable()) {
/*      */               
/* 1644 */               if (combat) {
/*      */                 
/* 1646 */                 creature.getCommunicator().sendCombatNormalMessage(message);
/*      */                 
/*      */                 continue;
/*      */               } 
/* 1650 */               creature.getCommunicator().sendNormalServerMessage(message);
/*      */             } 
/*      */           }
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void broadCast(String message) {
/* 1661 */     if (this.creatures != null)
/*      */     {
/* 1663 */       for (Iterator<Creature> it = this.creatures.iterator(); it.hasNext(); ) {
/*      */         
/* 1665 */         Creature creature = it.next();
/* 1666 */         if (!creature.getCommunicator().isInvulnerable()) {
/* 1667 */           creature.getCommunicator().sendNormalServerMessage(message);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public void broadCastMessage(Message message) {
/* 1674 */     if (this.watchers != null)
/*      */     {
/* 1676 */       for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext(); ) {
/*      */         
/* 1678 */         VirtualZone z = it.next();
/* 1679 */         z.broadCastMessage(message);
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
/*      */   void broadCastMessageLocal(Message message) {
/* 1691 */     if (this.creatures != null)
/* 1692 */       for (Iterator<Creature> it = this.creatures.iterator(); it.hasNext(); ) {
/*      */         
/* 1694 */         Creature creature = it.next();
/* 1695 */         if (!creature.getCommunicator().isInvulnerable()) {
/* 1696 */           creature.getCommunicator().sendMessage(message);
/*      */         }
/*      */       }  
/*      */   }
/*      */   
/*      */   void addWatcher(VirtualZone watcher) {
/* 1702 */     if (this.watchers == null)
/* 1703 */       this.watchers = new HashSet<>(); 
/* 1704 */     if (!this.watchers.contains(watcher)) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1709 */       this.watchers.add(watcher);
/* 1710 */       linkTo(watcher, false);
/* 1711 */       if (this.doors != null)
/*      */       {
/* 1713 */         for (Iterator<Door> dit = this.doors.iterator(); dit.hasNext(); ) {
/*      */           
/* 1715 */           Door door = dit.next();
/* 1716 */           door.addWatcher(watcher);
/*      */         } 
/*      */       }
/* 1719 */       if (this.mineDoors != null)
/*      */       {
/* 1721 */         for (Iterator<MineDoorPermission> dit = this.mineDoors.iterator(); dit.hasNext(); ) {
/*      */           
/* 1723 */           MineDoorPermission door = dit.next();
/* 1724 */           door.addWatcher(watcher);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void removeWatcher(VirtualZone watcher) {
/* 1732 */     if (this.watchers != null) {
/*      */       
/* 1734 */       if (this.watchers.contains(watcher)) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1739 */         this.watchers.remove(watcher);
/* 1740 */         linkTo(watcher, true);
/* 1741 */         if (this.doors != null)
/*      */         {
/* 1743 */           for (Iterator<Door> dit = this.doors.iterator(); dit.hasNext(); ) {
/*      */             
/* 1745 */             Door door = dit.next();
/* 1746 */             door.removeWatcher(watcher);
/*      */           } 
/*      */         }
/* 1749 */         if (this.mineDoors != null)
/*      */         {
/* 1751 */           for (Iterator<MineDoorPermission> dit = this.mineDoors.iterator(); dit.hasNext(); ) {
/*      */             
/* 1753 */             MineDoorPermission door = dit.next();
/* 1754 */             door.removeWatcher(watcher);
/*      */           } 
/*      */         }
/* 1757 */         if (!isVisibleToPlayers())
/*      */         {
/* 1759 */           for (Creature c : getCreatures())
/*      */           {
/* 1761 */             c.setVisibleToPlayers(false);
/*      */           }
/*      */         }
/*      */       } 
/* 1765 */       if (logger.isLoggable(Level.FINEST))
/*      */       {
/* 1767 */         logger.finest("Tile: " + this.tilex + ", " + this.tiley + "removing watcher " + watcher.getId());
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1772 */     else if (logger.isLoggable(Level.FINEST)) {
/*      */       
/* 1774 */       logger.finest("Tile: " + this.tilex + ", " + this.tiley + " tried to remove but watchers is null though.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void addEffect(Effect effect, boolean temp) {
/* 1782 */     if (this.isTransition && this.surfaced) {
/*      */       
/* 1784 */       getCaveTile().addEffect(effect, temp);
/*      */       
/*      */       return;
/*      */     } 
/* 1788 */     if (!temp) {
/*      */       
/* 1790 */       if (this.effects == null)
/* 1791 */         this.effects = new HashSet<>(); 
/* 1792 */       this.effects.add(effect);
/*      */     } 
/*      */     
/* 1795 */     if (this.watchers != null)
/*      */     {
/* 1797 */       for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext();) {
/* 1798 */         ((VirtualZone)it.next()).addEffect(effect, temp);
/*      */       }
/*      */     }
/* 1801 */     effect.setSurfaced(this.surfaced);
/*      */     
/*      */     try {
/* 1804 */       effect.save();
/*      */     }
/* 1806 */     catch (IOException iox) {
/*      */       
/* 1808 */       logger.log(Level.INFO, iox.getMessage(), iox);
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
/*      */   int addCreature(Creature creature, float diffZ) throws NoSuchCreatureException, NoSuchPlayerException {
/* 1840 */     if (this.inactive) {
/*      */       
/* 1842 */       logger.log(Level.WARNING, "AT 1 adding " + creature.getName() + " who is at " + creature.getTileX() + ", " + creature
/* 1843 */           .getTileY() + " to inactive tile " + this.tilex + "," + this.tiley, new Exception());
/* 1844 */       logger.log(Level.WARNING, "The zone " + this.zone.id + " covers " + this.zone.startX + ", " + this.zone.startY + " to " + this.zone.endX + "," + this.zone.endY);
/*      */     } 
/*      */     
/* 1847 */     if (!creature.setNewTile(this, diffZ, false))
/*      */     {
/* 1849 */       return 0;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1875 */     if (this.creatures == null)
/* 1876 */       this.creatures = new HashSet<>(); 
/* 1877 */     for (Creature c : this.creatures) {
/*      */       
/* 1879 */       if (!c.isFriendlyKingdom(creature.getKingdomId()))
/* 1880 */         c.setStealth(false); 
/*      */     } 
/* 1882 */     this.creatures.add(creature);
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
/* 1905 */     creature.setCurrentVillage(this.village);
/*      */     
/* 1907 */     creature.calculateZoneBonus(this.tilex, this.tiley, this.surfaced);
/*      */     
/* 1909 */     if (creature.isPlayer()) {
/*      */       
/*      */       try {
/*      */         
/* 1913 */         FaithZone z = Zones.getFaithZone(this.tilex, this.tiley, this.surfaced);
/* 1914 */         if (z != null) {
/* 1915 */           creature.setCurrentDeity(z.getCurrentRuler());
/*      */         } else {
/* 1917 */           creature.setCurrentDeity(null);
/*      */         } 
/* 1919 */       } catch (NoSuchZoneException nsz) {
/*      */         
/* 1921 */         logger.log(Level.WARNING, "No faith zone here? " + this.tilex + ", " + this.tiley + ", surf=" + this.surfaced);
/*      */       } 
/*      */     }
/* 1924 */     if (creature.getHighwayPathDestination().length() > 0 || creature.isWagoner()) {
/*      */ 
/*      */       
/* 1927 */       HighwayPos currentHighwayPos = null;
/* 1928 */       if (creature.getBridgeId() != -10L) {
/*      */         
/* 1930 */         BridgePart bridgePart = Zones.getBridgePartFor(this.tilex, this.tiley, this.surfaced);
/*      */         
/* 1932 */         if (bridgePart != null)
/* 1933 */           currentHighwayPos = MethodsHighways.getHighwayPos(bridgePart); 
/*      */       } 
/* 1935 */       if (currentHighwayPos == null && creature.getFloorLevel() > 0) {
/*      */         
/* 1937 */         Floor floor = Zones.getFloor(this.tilex, this.tiley, this.surfaced, creature.getFloorLevel());
/*      */         
/* 1939 */         if (floor != null)
/* 1940 */           currentHighwayPos = MethodsHighways.getHighwayPos(floor); 
/*      */       } 
/* 1942 */       if (currentHighwayPos == null)
/* 1943 */         currentHighwayPos = MethodsHighways.getHighwayPos(this.tilex, this.tiley, this.surfaced); 
/* 1944 */       if (currentHighwayPos != null) {
/*      */ 
/*      */ 
/*      */         
/* 1948 */         Item waystone = getWaystone(currentHighwayPos);
/* 1949 */         if (waystone == null)
/* 1950 */           waystone = getWaystone(MethodsHighways.getNewHighwayPosLinked(currentHighwayPos, (byte)1)); 
/* 1951 */         if (waystone == null)
/* 1952 */           waystone = getWaystone(MethodsHighways.getNewHighwayPosLinked(currentHighwayPos, -128)); 
/* 1953 */         if (waystone == null)
/* 1954 */           waystone = getWaystone(MethodsHighways.getNewHighwayPosLinked(currentHighwayPos, (byte)64)); 
/* 1955 */         if (waystone == null)
/* 1956 */           waystone = getWaystone(MethodsHighways.getNewHighwayPosLinked(currentHighwayPos, (byte)32)); 
/* 1957 */         if (waystone == null)
/* 1958 */           waystone = getWaystone(MethodsHighways.getNewHighwayPosLinked(currentHighwayPos, (byte)2)); 
/* 1959 */         if (waystone == null)
/* 1960 */           waystone = getWaystone(MethodsHighways.getNewHighwayPosLinked(currentHighwayPos, (byte)4)); 
/* 1961 */         if (waystone == null)
/* 1962 */           waystone = getWaystone(MethodsHighways.getNewHighwayPosLinked(currentHighwayPos, (byte)16)); 
/* 1963 */         if (waystone == null) {
/* 1964 */           waystone = getWaystone(MethodsHighways.getNewHighwayPosLinked(currentHighwayPos, (byte)8));
/*      */         }
/* 1966 */         if (waystone != null && creature.getLastWaystoneChecked() != waystone.getWurmId())
/*      */         {
/* 1968 */           if (creature.isWagoner()) {
/*      */ 
/*      */             
/* 1971 */             creature.setLastWaystoneChecked(waystone.getWurmId());
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 1976 */             Node startNode = Routes.getNode(waystone.getWurmId());
/* 1977 */             String goingto = creature.getHighwayPathDestination();
/* 1978 */             if (startNode.getVillage() == null || creature.currentVillage != null || !startNode.getVillage().getName().equalsIgnoreCase(goingto)) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1985 */               creature.setLastWaystoneChecked(waystone.getWurmId());
/*      */               
/*      */               try {
/* 1988 */                 Village destinationVillage = Villages.getVillage(goingto);
/* 1989 */                 HighwayFinder.queueHighwayFinding(creature, startNode, destinationVillage, (byte)0);
/*      */               }
/* 1991 */               catch (NoSuchVillageException e) {
/*      */ 
/*      */                 
/* 1994 */                 creature.getCommunicator().sendNormalServerMessage("Destination village (" + goingto + ") cannot be found.");
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/* 2001 */     return this.creatures.size();
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private Item getWaystone(@Nullable HighwayPos highwayPos) {
/* 2007 */     if (highwayPos == null)
/* 2008 */       return null; 
/* 2009 */     Item marker = MethodsHighways.getMarker(highwayPos);
/* 2010 */     if (marker != null && marker.getTemplateId() == 1112)
/*      */     {
/* 2012 */       return marker;
/*      */     }
/* 2014 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean removeCreature(Creature creature) {
/* 2024 */     if (this.creatures != null) {
/*      */       
/* 2026 */       boolean removed = this.creatures.remove(creature);
/* 2027 */       if (this.creatures.isEmpty())
/* 2028 */         this.creatures = null; 
/* 2029 */       if (!removed)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/* 2034 */         return false;
/*      */       }
/* 2036 */       Door[] doorArr = getDoors();
/* 2037 */       for (int d = 0; d < doorArr.length; d++) {
/*      */         
/* 2039 */         if (!doorArr[d].covers(creature.getPosX(), creature.getPosY(), creature.getPositionZ(), creature.getFloorLevel(), creature
/* 2040 */             .followsGround()))
/* 2041 */           doorArr[d].removeCreature(creature); 
/* 2042 */       }  if (this.watchers != null)
/*      */       {
/* 2044 */         for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext(); ) {
/*      */           
/* 2046 */           VirtualZone watchingZone = it.next();
/*      */           
/* 2048 */           watchingZone.removeCreature(creature);
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
/*      */       
/* 2062 */       return true;
/*      */     } 
/* 2064 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean checkOpportunityAttacks(Creature creature) {
/* 2073 */     Creature[] lTempCreatures = getCreatures();
/* 2074 */     for (int x = 0; x < lTempCreatures.length; x++) {
/*      */       
/* 2076 */       if (lTempCreatures[x] != creature && !lTempCreatures[x].isMoving())
/*      */       {
/* 2078 */         if (lTempCreatures[x].getAttitude(creature) == 2)
/*      */         {
/*      */           
/* 2081 */           if (VirtualZone.isCreatureTurnedTowardsTarget(creature, lTempCreatures[x]))
/* 2082 */             return lTempCreatures[x].opportunityAttack(creature); 
/*      */         }
/*      */       }
/*      */     } 
/* 2086 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void makeInvisible(Creature creature) {
/* 2091 */     if (this.watchers != null)
/*      */     {
/* 2093 */       for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext(); ) {
/*      */         
/* 2095 */         VirtualZone watchingZone = it.next();
/* 2096 */         watchingZone.makeInvisible(creature);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void makeVisible(Creature creature) throws NoSuchCreatureException, NoSuchPlayerException {
/* 2103 */     if (this.watchers != null)
/*      */     {
/* 2105 */       for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */         
/*      */         try {
/* 2109 */           vz.addCreature(creature.getWurmId(), false);
/*      */         }
/* 2111 */         catch (Exception e) {
/*      */           
/* 2113 */           logger.log(Level.WARNING, e.getMessage(), e);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void makeInvisible(Item item) {
/* 2121 */     if (this.watchers != null)
/*      */     {
/* 2123 */       for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext(); ) {
/*      */         
/* 2125 */         VirtualZone watchingZone = it.next();
/* 2126 */         watchingZone.removeItem(item);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void makeVisible(Item item) {
/* 2133 */     if (this.watchers != null)
/*      */     {
/* 2135 */       for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext(); ) {
/*      */         
/* 2137 */         VirtualZone watchingZone = it.next();
/* 2138 */         watchingZone.addItem(item, this, -10L, true);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private VolaTile getCaveTile() {
/*      */     try {
/* 2147 */       Zone z = Zones.getZone(this.tilex, this.tiley, false);
/* 2148 */       return z.getOrCreateTile(this.tilex, this.tiley);
/*      */     }
/* 2150 */     catch (NoSuchZoneException noSuchZoneException) {
/*      */ 
/*      */       
/* 2153 */       logger.log(Level.WARNING, "No cave tile for " + this.tilex + ", " + this.tiley);
/* 2154 */       return this;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private VolaTile getSurfaceTile() {
/*      */     try {
/* 2161 */       Zone z = Zones.getZone(this.tilex, this.tiley, true);
/* 2162 */       return z.getOrCreateTile(this.tilex, this.tiley);
/*      */     }
/* 2164 */     catch (NoSuchZoneException noSuchZoneException) {
/*      */ 
/*      */       
/* 2167 */       logger.log(Level.WARNING, "No surface tile for " + this.tilex + ", " + this.tiley);
/* 2168 */       return this;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void addItem(Item item, boolean moving, boolean starting) {
/* 2173 */     addItem(item, moving, -10L, starting);
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
/*      */   public void addItem(Item item, boolean moving, long creatureId, boolean starting) {
/* 2186 */     if (this.inactive) {
/*      */       
/* 2188 */       logger.log(Level.WARNING, "adding " + item.getName() + " to inactive tile " + this.tilex + "," + this.tiley + " surf=" + this.surfaced + " itemsurf=" + item
/* 2189 */           .isOnSurface(), new Exception());
/* 2190 */       logger.log(Level.WARNING, "The zone " + this.zone.id + " covers " + this.zone.startX + ", " + this.zone.startY + " to " + this.zone.endX + "," + this.zone.endY);
/*      */     } 
/*      */ 
/*      */     
/* 2194 */     if (item.hidden) {
/*      */       return;
/*      */     }
/* 2197 */     if (this.isTransition && this.surfaced && !item.isVehicle()) {
/*      */       
/* 2199 */       if (logger.isLoggable(Level.FINEST))
/*      */       {
/* 2201 */         logger.finest("Adding " + item.getName() + " to cave level instead.");
/*      */       }
/* 2203 */       boolean stayOnSurface = false;
/* 2204 */       if (Zones.getTextureForTile(this.tilex, this.tiley, 0) != Tiles.Tile.TILE_HOLE.id)
/* 2205 */         stayOnSurface = true; 
/* 2206 */       if (!stayOnSurface) {
/*      */         
/* 2208 */         getCaveTile().addItem(item, moving, starting);
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/* 2213 */     if (item.isTileAligned()) {
/*      */       
/* 2215 */       item.setPosXY(((this.tilex << 2) + 2), ((this.tiley << 2) + 2));
/* 2216 */       item.setOwnerId(-10L);
/* 2217 */       if (item.isFence())
/*      */       {
/* 2219 */         if (isOnSurface()) {
/*      */           
/* 2221 */           int offz = 0;
/*      */           
/*      */           try {
/* 2224 */             offz = (int)((item.getPosZ() - Zones.calculateHeight(item.getPosX(), item.getPosY(), this.surfaced)) / 10.0F);
/*      */           }
/* 2226 */           catch (NoSuchZoneException nsz) {
/*      */             
/* 2228 */             logger.log(Level.WARNING, "Dropping fence item outside zones.");
/*      */           } 
/* 2230 */           float rot = Creature.normalizeAngle(item.getRotation());
/* 2231 */           if (rot >= 45.0F && rot < 135.0F)
/*      */           {
/* 2233 */             VolaTile next = Zones.getOrCreateTile(this.tilex + 1, this.tiley, this.surfaced);
/* 2234 */             next.addFence((Fence)new TempFence(StructureConstantsEnum.FENCE_SIEGEWALL, this.tilex + 1, this.tiley, offz, item, Tiles.TileBorderDirection.DIR_DOWN, next
/* 2235 */                   .getZone().getId(), getLayer()));
/*      */           }
/* 2237 */           else if (rot >= 135.0F && rot < 225.0F)
/*      */           {
/* 2239 */             VolaTile next = Zones.getOrCreateTile(this.tilex, this.tiley + 1, this.surfaced);
/* 2240 */             next.addFence((Fence)new TempFence(StructureConstantsEnum.FENCE_SIEGEWALL, this.tilex, this.tiley + 1, offz, item, Tiles.TileBorderDirection.DIR_HORIZ, next
/* 2241 */                   .getZone().getId(), getLayer()));
/*      */           }
/* 2243 */           else if (rot >= 225.0F && rot < 315.0F)
/*      */           {
/* 2245 */             addFence((Fence)new TempFence(StructureConstantsEnum.FENCE_SIEGEWALL, this.tilex, this.tiley, offz, item, Tiles.TileBorderDirection.DIR_DOWN, 
/* 2246 */                   getZone().getId(), getLayer()));
/*      */           }
/*      */           else
/*      */           {
/* 2250 */             addFence((Fence)new TempFence(StructureConstantsEnum.FENCE_SIEGEWALL, this.tilex, this.tiley, offz, item, Tiles.TileBorderDirection.DIR_HORIZ, 
/* 2251 */                   getZone().getId(), getLayer()));
/*      */           }
/*      */         
/*      */         } 
/*      */       }
/* 2256 */     } else if (item.getTileX() != this.tilex || item.getTileY() != this.tiley) {
/*      */       
/* 2258 */       putRandomOnTile(item);
/* 2259 */       item.setOwnerId(-10L);
/*      */     } 
/* 2261 */     if (!this.surfaced)
/*      */     {
/* 2263 */       if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(this.tilex, this.tiley)))) {
/*      */         
/* 2265 */         if (!(getSurfaceTile()).isTransition) {
/*      */           
/* 2267 */           getSurfaceTile().addItem(item, moving, creatureId, starting);
/* 2268 */           logger.log(Level.INFO, "adding " + item.getName() + " in rock at " + this.tilex + ", " + this.tiley + " ");
/*      */         } 
/*      */         
/*      */         return;
/*      */       } 
/*      */     }
/* 2274 */     item.setZoneId(this.zone.getId(), this.surfaced);
/* 2275 */     if (!starting && !item.getTemplate().hovers()) {
/* 2276 */       item.updatePosZ(this);
/*      */     }
/* 2278 */     if (this.vitems == null)
/* 2279 */       this.vitems = new VolaTileItems(); 
/* 2280 */     if (this.vitems.addItem(item, starting)) {
/*      */       
/* 2282 */       if (item.getTemplateId() == 726) {
/* 2283 */         Zones.addDuelRing(item);
/*      */       }
/* 2285 */       if (!item.isDecoration()) {
/*      */         
/* 2287 */         Item pile = this.vitems.getPileItem(item.getFloorLevel());
/* 2288 */         if (this.vitems.checkIfCreatePileItem(item.getFloorLevel()) || pile != null) {
/*      */           
/* 2290 */           if (pile == null) {
/*      */             
/* 2292 */             pile = createPileItem(item, starting);
/* 2293 */             this.vitems.addPileItem(pile);
/*      */           } 
/* 2295 */           pile.insertItem(item, true);
/* 2296 */           int data = pile.getData1();
/* 2297 */           if (data != -1 && item.getTemplateId() != data) {
/*      */             
/* 2299 */             pile.setData1(-1);
/* 2300 */             pile.setName(pile.getTemplate().getName());
/* 2301 */             String modelname = pile.getTemplate().getModelName().replaceAll(" ", "") + "unknown.";
/*      */             
/* 2303 */             if (this.watchers != null)
/*      */             {
/* 2305 */               for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext();) {
/* 2306 */                 ((VirtualZone)it.next()).renameItem(pile, pile.getName(), modelname);
/*      */               }
/*      */             }
/*      */           } 
/* 2310 */         } else if (this.watchers != null) {
/*      */           
/* 2312 */           boolean onGroundLevel = true;
/* 2313 */           if (item.getFloorLevel() > 0) {
/* 2314 */             onGroundLevel = false;
/*      */           
/*      */           }
/* 2317 */           else if ((getFloors(0, 0)).length > 0) {
/* 2318 */             onGroundLevel = false;
/*      */           } 
/* 2320 */           for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */             
/*      */             try {
/* 2324 */               if (vz.isVisible(item, this)) {
/* 2325 */                 vz.addItem(item, this, creatureId, onGroundLevel);
/*      */               }
/* 2327 */             } catch (Exception e) {
/*      */               
/* 2329 */               logger.log(Level.WARNING, e.getMessage(), e);
/*      */             }
/*      */           
/*      */           } 
/*      */         } 
/* 2334 */       } else if (this.watchers != null) {
/*      */         
/* 2336 */         boolean onGroundLevel = true;
/* 2337 */         if (item.getFloorLevel() > 0) {
/* 2338 */           onGroundLevel = false;
/*      */         
/*      */         }
/* 2341 */         else if ((getFloors(0, 0)).length > 0) {
/* 2342 */           onGroundLevel = false;
/*      */         } 
/* 2344 */         for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */           
/*      */           try {
/* 2348 */             if (vz.isVisible(item, this)) {
/* 2349 */               vz.addItem(item, this, creatureId, onGroundLevel);
/*      */             }
/* 2351 */           } catch (Exception e) {
/*      */             
/* 2353 */             logger.log(Level.WARNING, e.getMessage(), e);
/*      */           } 
/*      */         } 
/*      */       } 
/* 2357 */       if (item.isDomainItem()) {
/* 2358 */         Zones.addAltar(item, moving);
/*      */       }
/* 2360 */       if ((item.getTemplateId() == 1175 || item.getTemplateId() == 1239) && item
/* 2361 */         .getAuxData() > 0)
/* 2362 */         Zones.addHive(item, moving); 
/* 2363 */       if (item.getTemplateId() == 939 || item.isEnchantedTurret())
/* 2364 */         Zones.addTurret(item, moving); 
/* 2365 */       if (item.isEpicTargetItem()) {
/* 2366 */         EpicTargetItems.addRitualTargetItem(item);
/*      */       }
/* 2368 */       if (this.village != null && item.getTemplateId() == 757)
/*      */       {
/* 2370 */         this.village.addBarrel(item);
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 2375 */       item.setZoneId(this.zone.getId(), this.surfaced);
/* 2376 */       if (!item.deleted) {
/* 2377 */         logger.log(Level.WARNING, "tile already contained item " + item.getName() + " (ID: " + item.getWurmId() + ") at " + this.tilex + ", " + this.tiley, new Exception());
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void updatePile(Item pile) {
/* 2385 */     checkIfRenamePileItem(pile);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void removeItem(Item item, boolean moving) {
/* 2393 */     if (this.vitems != null)
/*      */     {
/* 2395 */       if (!this.vitems.isEmpty()) {
/*      */         
/* 2397 */         if (item.getTemplateId() == 726)
/* 2398 */           Zones.removeDuelRing(item); 
/* 2399 */         Item pileItem = this.vitems.getPileItem(item.getFloorLevel());
/* 2400 */         if (pileItem != null && item.getWurmId() == pileItem.getWurmId()) {
/*      */           
/* 2402 */           this.vitems.removePileItem(item.getFloorLevel());
/* 2403 */           if (this.vitems.isEmpty())
/*      */           {
/*      */             
/* 2406 */             this.vitems.destroy(this);
/* 2407 */             this.vitems = null;
/*      */           }
/*      */         
/* 2410 */         } else if (this.vitems.removeItem(item)) {
/*      */ 
/*      */           
/* 2413 */           this.vitems.destroy(this);
/*      */           
/* 2415 */           this.vitems = null;
/* 2416 */           if (pileItem != null)
/*      */           {
/* 2418 */             destroyPileItem(pileItem.getFloorLevel());
/*      */           }
/*      */         }
/* 2421 */         else if (pileItem != null && this.vitems.checkIfRemovePileItem(pileItem.getFloorLevel())) {
/*      */           
/* 2423 */           if (!moving) {
/* 2424 */             destroyPileItem(pileItem.getFloorLevel());
/*      */           }
/* 2426 */         } else if (!item.isDecoration() && pileItem != null) {
/*      */           
/* 2428 */           checkIfRenamePileItem(pileItem);
/*      */         } 
/* 2430 */         if (item.isDomainItem())
/* 2431 */           Zones.removeAltar(item, moving); 
/* 2432 */         if (item.getTemplateId() == 1175 || item.getTemplateId() == 1239)
/* 2433 */           Zones.removeHive(item, moving); 
/* 2434 */         if (item.getTemplateId() == 939 || item.isEnchantedTurret())
/* 2435 */           Zones.removeTurret(item, moving); 
/* 2436 */         if (item.isEpicTargetItem())
/* 2437 */           EpicTargetItems.removeRitualTargetItem(item); 
/* 2438 */         if (item.isKingdomMarker())
/*      */         {
/* 2440 */           if (item.getTemplateId() != 328)
/* 2441 */             Kingdoms.destroyTower(item); 
/*      */         }
/* 2443 */         if (item.getTemplateId() == 521) {
/*      */           
/* 2445 */           this.zone.creatureSpawn = null;
/* 2446 */           Zone.spawnPoints--;
/*      */         } 
/* 2448 */         if (this.village != null && item.getTemplateId() == 757)
/*      */         {
/* 2450 */           this.village.removeBarrel(item);
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/* 2455 */         Item pileItem = this.vitems.getPileItem(item.getFloorLevel());
/*      */         
/* 2457 */         if (pileItem != null && item.getWurmId() == pileItem.getWurmId()) {
/*      */           
/* 2459 */           this.vitems.removePileItem(item.getFloorLevel());
/*      */         }
/* 2461 */         else if (pileItem != null && this.vitems.checkIfRemovePileItem(item.getFloorLevel())) {
/*      */           
/* 2463 */           destroyPileItem(item.getFloorLevel());
/*      */         } 
/*      */       } 
/*      */     }
/* 2467 */     if (item.isFence()) {
/*      */       
/* 2469 */       int offz = 0;
/*      */       
/*      */       try {
/* 2472 */         offz = (int)((item.getPosZ() - Zones.calculateHeight(item.getPosX(), item.getPosY(), item
/* 2473 */             .isOnSurface())) / 10.0F);
/*      */       }
/* 2475 */       catch (NoSuchZoneException nsz) {
/*      */         
/* 2477 */         logger.log(Level.WARNING, "Dropping fence item outside zones.");
/*      */       } 
/* 2479 */       float rot = Creature.normalizeAngle(item.getRotation());
/* 2480 */       if (rot >= 45.0F && rot < 135.0F) {
/*      */         
/* 2482 */         VolaTile next = Zones.getOrCreateTile(this.tilex + 1, this.tiley, (item
/* 2483 */             .isOnSurface() || this.isTransition));
/* 2484 */         next.removeFence((Fence)new TempFence(StructureConstantsEnum.FENCE_SIEGEWALL, this.tilex + 1, this.tiley, offz, item, Tiles.TileBorderDirection.DIR_DOWN, next
/* 2485 */               .getZone().getId(), Math.max(0, next.getLayer())));
/*      */       }
/* 2487 */       else if (rot >= 135.0F && rot < 225.0F) {
/*      */         
/* 2489 */         VolaTile next = Zones.getOrCreateTile(this.tilex, this.tiley + 1, (item
/* 2490 */             .isOnSurface() || this.isTransition));
/* 2491 */         next.removeFence((Fence)new TempFence(StructureConstantsEnum.FENCE_SIEGEWALL, this.tilex, this.tiley + 1, offz, item, Tiles.TileBorderDirection.DIR_HORIZ, next
/* 2492 */               .getZone().getId(), Math.max(0, next.getLayer())));
/*      */       }
/* 2494 */       else if (rot >= 225.0F && rot < 315.0F) {
/*      */         
/* 2496 */         removeFence((Fence)new TempFence(StructureConstantsEnum.FENCE_SIEGEWALL, this.tilex, this.tiley, offz, item, Tiles.TileBorderDirection.DIR_DOWN, 
/* 2497 */               getZone().getId(), Math.max(0, getLayer())));
/*      */       }
/*      */       else {
/*      */         
/* 2501 */         removeFence((Fence)new TempFence(StructureConstantsEnum.FENCE_SIEGEWALL, this.tilex, this.tiley, offz, item, Tiles.TileBorderDirection.DIR_HORIZ, 
/* 2502 */               getZone().getId(), Math.max(0, getLayer())));
/*      */       } 
/*      */     } 
/*      */     
/* 2506 */     sendRemoveItem(item, moving);
/* 2507 */     if (!moving) {
/* 2508 */       item.setZoneId(-10, this.surfaced);
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkIfRenamePileItem(Item pileItem) {
/* 2513 */     if (pileItem.getData1() == -1) {
/*      */       
/* 2515 */       int itid = -1;
/*      */       
/* 2517 */       byte material = 0;
/* 2518 */       boolean multipleMaterials = false;
/*      */       
/* 2520 */       for (Item item : pileItem.getItems()) {
/*      */         
/* 2522 */         if (!multipleMaterials)
/*      */         {
/*      */ 
/*      */           
/* 2526 */           if (item.getMaterial() != material)
/*      */           {
/* 2528 */             if (material == 0) {
/*      */ 
/*      */ 
/*      */               
/* 2532 */               material = item.getMaterial();
/*      */ 
/*      */             
/*      */             }
/*      */             else {
/*      */ 
/*      */ 
/*      */               
/* 2540 */               material = 0;
/* 2541 */               multipleMaterials = true;
/*      */             } 
/*      */           }
/*      */         }
/* 2545 */         if (itid == -1) {
/* 2546 */           itid = item.getTemplateId();
/*      */           continue;
/*      */         } 
/* 2549 */         if (itid != item.getTemplateId()) {
/*      */           
/* 2551 */           itid = -1;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/* 2556 */       if (itid != -1) {
/*      */         
/*      */         try {
/*      */           
/* 2560 */           String name = pileItem.getTemplate().getName();
/* 2561 */           pileItem.setData1(itid);
/* 2562 */           ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(itid);
/* 2563 */           String tname = template.getName();
/* 2564 */           name = "Pile of " + template.sizeString + tname;
/*      */           
/* 2566 */           pileItem.setMaterial(material);
/* 2567 */           StringBuilder build = new StringBuilder();
/* 2568 */           build.append(pileItem.getTemplate().getModelName());
/* 2569 */           build.append(tname);
/* 2570 */           build.append(".");
/* 2571 */           build.append(MaterialUtilities.getMaterialString(pileItem.getMaterial()));
/* 2572 */           String modelname = build.toString().replaceAll(" ", "").trim();
/* 2573 */           pileItem.setName(name);
/*      */           
/* 2575 */           if (this.watchers != null)
/*      */           {
/* 2577 */             for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext();) {
/* 2578 */               ((VirtualZone)it.next()).renameItem(pileItem, name, modelname);
/*      */             }
/*      */           }
/* 2581 */         } catch (NoSuchTemplateException noSuchTemplateException) {}
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void sendRemoveItem(Item item, boolean moving) {
/* 2590 */     if (this.watchers != null)
/*      */     {
/* 2592 */       for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */         
/*      */         try {
/* 2596 */           if (!moving || !vz.isVisible(item, this)) {
/* 2597 */             vz.removeItem(item);
/*      */           }
/* 2599 */         } catch (Exception e) {
/*      */           
/* 2601 */           logger.log(Level.WARNING, e.getMessage(), e);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final void sendSetBridgeId(Creature creature, long bridgeId, boolean sendToSelf) {
/* 2609 */     if (this.watchers != null)
/*      */     {
/* 2611 */       for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */         
/*      */         try {
/* 2615 */           if (sendToSelf)
/*      */           {
/* 2617 */             if (creature.getWurmId() == vz.getWatcher().getWurmId())
/*      */             {
/* 2619 */               vz.sendBridgeId(-1L, bridgeId);
/*      */             }
/* 2621 */             else if (creature.isVisibleTo(vz.getWatcher()))
/*      */             {
/* 2623 */               vz.sendBridgeId(creature.getWurmId(), bridgeId);
/*      */             
/*      */             }
/*      */           
/*      */           }
/* 2628 */           else if (creature.getWurmId() != vz.getWatcher().getWurmId())
/*      */           {
/* 2630 */             if (creature.isVisibleTo(vz.getWatcher()))
/*      */             {
/* 2632 */               vz.sendBridgeId(creature.getWurmId(), bridgeId);
/*      */             }
/*      */           }
/*      */         
/*      */         }
/* 2637 */         catch (Exception e) {
/*      */           
/* 2639 */           logger.log(Level.WARNING, e.getMessage(), e);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final void sendSetBridgeId(Item item, long bridgeId) {
/* 2647 */     if (this.watchers != null)
/*      */     {
/* 2649 */       for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */         
/*      */         try {
/* 2653 */           if (vz.isVisible(item, this)) {
/* 2654 */             vz.sendBridgeId(item.getWurmId(), bridgeId);
/*      */           }
/* 2656 */         } catch (Exception e) {
/*      */           
/* 2658 */           logger.log(Level.WARNING, e.getMessage(), e);
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
/*      */   public void removeWall(Wall wall, boolean silent) {
/* 2671 */     if (wall != null) {
/*      */       
/* 2673 */       if (this.walls != null) {
/*      */         
/* 2675 */         this.walls.remove(wall);
/* 2676 */         if (this.walls.size() == 0)
/* 2677 */           this.walls = null; 
/*      */       } 
/* 2679 */       if (this.watchers != null && !silent)
/*      */       {
/* 2681 */         for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */           
/*      */           try {
/* 2685 */             vz.removeWall(this.structure.getWurmId(), wall);
/*      */           }
/* 2687 */           catch (Exception e) {
/*      */             
/* 2689 */             logger.log(Level.WARNING, e.getMessage(), e);
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   boolean removeEffect(Effect effect) {
/* 2699 */     boolean removed = false;
/* 2700 */     if (this.effects != null && this.effects.contains(effect)) {
/*      */       
/* 2702 */       this.effects.remove(effect);
/* 2703 */       if (this.watchers != null)
/*      */       {
/* 2705 */         for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext();)
/* 2706 */           ((VirtualZone)it.next()).removeEffect(effect); 
/*      */       }
/* 2708 */       if (this.effects.size() == 0)
/* 2709 */         this.effects = null; 
/* 2710 */       removed = true;
/*      */     } 
/*      */     
/* 2713 */     return removed;
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
/*      */   public void creatureMoved(long creatureId, float diffX, float diffY, float diffZ, int diffTileX, int diffTileY) throws NoSuchCreatureException, NoSuchPlayerException {
/* 2725 */     creatureMoved(creatureId, diffX, diffY, diffZ, diffTileX, diffTileY, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void creatureMoved(long creatureId, float diffX, float diffY, float diffZ, int diffTileX, int diffTileY, boolean passenger) throws NoSuchCreatureException, NoSuchPlayerException {
/* 2734 */     Creature creature = Server.getInstance().getCreature(creatureId);
/*      */     
/* 2736 */     int tileX = this.tilex + diffTileX;
/* 2737 */     int tileY = this.tiley + diffTileY;
/* 2738 */     boolean changedLevel = false;
/* 2739 */     if (diffTileX != 0 || diffTileY != 0) {
/*      */       
/* 2741 */       if (!creature.isPlayer()) {
/*      */         
/* 2743 */         boolean following = (creature.getLeader() != null || creature.isRidden() || creature.getHitched() != null);
/* 2744 */         boolean godown = false;
/* 2745 */         if (this.surfaced && following)
/*      */         {
/* 2747 */           if (creature.isRidden() || creature.getHitched() != null) {
/*      */             
/* 2749 */             if (creature.getHitched() != null) {
/*      */               
/* 2751 */               Creature rider = Server.getInstance().getCreature((creature.getHitched()).pilotId);
/* 2752 */               if (!rider.isOnSurface())
/*      */               {
/* 2754 */                 godown = true;
/*      */               }
/*      */             } else {
/*      */ 
/*      */               
/*      */               try {
/*      */                 
/* 2761 */                 if (creature.getMountVehicle() != null) {
/*      */                   
/* 2763 */                   Creature rider = Server.getInstance().getCreature((creature.getMountVehicle()).pilotId);
/* 2764 */                   if (!rider.isOnSurface())
/*      */                   {
/* 2766 */                     godown = true;
/*      */                   }
/*      */                 } else {
/*      */                   
/* 2770 */                   logger.log(Level.WARNING, "Mount Vehicle is null for ridden " + creature.getWurmId());
/*      */                 } 
/* 2772 */               } catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */               
/*      */               }
/* 2776 */               catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */             
/*      */             }
/*      */ 
/*      */           
/*      */           }
/* 2782 */           else if (creature.getLeader() != null) {
/*      */ 
/*      */             
/* 2785 */             if (!creature.getLeader().isOnSurface()) {
/* 2786 */               godown = true;
/*      */             }
/*      */           } 
/*      */         }
/* 2790 */         if (!this.surfaced) {
/*      */           
/* 2792 */           if (this.isTransition && Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(tileX, tileY)))) {
/*      */             
/* 2794 */             changedLevel = true;
/* 2795 */             creature.getStatus().setLayer(0);
/*      */           }
/* 2797 */           else if (creature.getLeader() != null) {
/*      */             
/* 2799 */             if (creature.getLeader().isOnSurface())
/*      */             {
/* 2801 */               if (!this.isTransition)
/*      */               {
/*      */                 
/* 2804 */                 changedLevel = true;
/* 2805 */                 creature.getStatus().setLayer(0);
/*      */               }
/*      */             
/*      */             }
/*      */           } 
/* 2810 */         } else if (Tiles.decodeType(Server.surfaceMesh.getTile(tileX, tileY)) == Tiles.Tile.TILE_HOLE.id || (godown && 
/* 2811 */           Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile(tileX, tileY))))) {
/*      */           
/* 2813 */           changedLevel = true;
/* 2814 */           creature.getStatus().setLayer(-1);
/*      */         } 
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
/* 2828 */         VolaTile nextTile = Zones.getTileOrNull(tileX, tileY, isOnSurface());
/* 2829 */         if (nextTile != null)
/*      */         {
/* 2831 */           if (nextTile.getStructure() != null && creature.getBridgeId() != nextTile.getStructure().getWurmId()) {
/*      */             
/* 2833 */             if ((nextTile.getBridgeParts()).length > 0)
/*      */             {
/* 2835 */               for (BridgePart bp : nextTile.getBridgeParts()) {
/*      */ 
/*      */ 
/*      */                 
/* 2839 */                 int ht = Math.max(0, creature.getPosZDirts());
/*      */ 
/*      */                 
/* 2842 */                 if (Math.abs(ht - bp.getHeightOffset()) < 25)
/*      */                 {
/* 2844 */                   if (bp.hasAnExit()) {
/* 2845 */                     creature.setBridgeId(nextTile.structure.getWurmId());
/*      */                   }
/*      */                 }
/*      */               } 
/*      */             }
/* 2850 */           } else if (creature.getBridgeId() > 0L && (nextTile
/* 2851 */             .getStructure() == null || nextTile.getStructure().getWurmId() != creature
/* 2852 */             .getBridgeId())) {
/*      */             
/* 2854 */             boolean leave = true;
/* 2855 */             BridgePart[] parts = getBridgeParts();
/* 2856 */             if (parts != null)
/*      */             {
/* 2858 */               for (BridgePart bp : parts) {
/*      */                 
/* 2860 */                 if (bp.isFinished())
/*      */                 {
/*      */                   
/* 2863 */                   if (bp.getDir() == 0 || bp.getDir() == 4) {
/*      */                     
/* 2865 */                     if (getTileY() == nextTile.getTileY())
/*      */                     {
/* 2867 */                       leave = false;
/*      */                     
/*      */                     }
/*      */                   
/*      */                   }
/* 2872 */                   else if (getTileX() == nextTile.getTileX()) {
/*      */                     
/* 2874 */                     leave = false;
/*      */                   } 
/*      */                 }
/*      */               } 
/*      */             }
/*      */ 
/*      */             
/* 2881 */             if (leave)
/* 2882 */               creature.setBridgeId(-10L); 
/*      */           } 
/*      */         }
/*      */       } 
/* 2886 */       if (!changedLevel && this.zone.covers(tileX, tileY)) {
/*      */         
/* 2888 */         VolaTile newTile = this.zone.getOrCreateTile(tileX, tileY);
/* 2889 */         newTile.addCreature(creature, diffZ);
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/* 2895 */           this.zone.removeCreature(creature, changedLevel, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2902 */           Zone newZone = Zones.getZone(tileX, tileY, creature.isOnSurface());
/* 2903 */           newZone.addCreature(creature.getWurmId());
/*      */ 
/*      */         
/*      */         }
/* 2907 */         catch (NoSuchZoneException sex) {
/*      */           
/* 2909 */           logger.log(Level.INFO, sex.getMessage() + " this tile at " + this.tilex + "," + this.tiley + ", diff=" + diffTileX + ", " + diffTileY, (Throwable)sex);
/*      */         } 
/*      */       } 
/*      */       
/* 2913 */       if (!passenger)
/* 2914 */         this.zone.createTrack(creature, this.tilex, this.tiley, diffTileX, diffTileY); 
/*      */     } 
/* 2916 */     if (this.isTransition) {
/*      */       
/* 2918 */       if (!passenger)
/*      */       {
/*      */ 
/*      */         
/* 2922 */         updateNeighbourTileDoors(creature, this.tilex, this.tiley);
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 2927 */     else if (!passenger) {
/* 2928 */       doorCreatureMoved(creature, diffTileX, diffTileY);
/*      */     } 
/* 2930 */     if (!changedLevel && !passenger)
/*      */     {
/* 2932 */       for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */         
/*      */         try {
/* 2936 */           if (diffZ * 10.0F <= 127.0F && diffZ * 10.0F >= -128.0F) {
/*      */ 
/*      */             
/* 2939 */             if (vz.creatureMoved(creatureId, diffX, diffY, diffZ, diffTileX, diffTileY))
/*      */             {
/* 2941 */               logger.log(Level.INFO, "Forcibly removing watcher " + vz);
/* 2942 */               removeWatcher(vz);
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 2947 */             if (logger.isLoggable(Level.FINEST))
/*      */             {
/* 2949 */               logger.finest(creature.getName() + " moved more than byte max (" + '' + ") or min (" + -128 + ") in z: " + diffZ + " at " + this.tilex + ", " + this.tiley + " surfaced=" + 
/*      */                   
/* 2951 */                   isOnSurface());
/*      */             }
/* 2953 */             makeInvisible(creature);
/* 2954 */             makeVisible(creature);
/*      */           }
/*      */         
/* 2957 */         } catch (Exception ex) {
/*      */           
/* 2959 */           logger.log(Level.WARNING, "Exception when " + creature.getName() + " moved at " + this.tilex + ", " + this.tiley + " tile surf=" + 
/* 2960 */               isOnSurface() + " cret onsurf=" + creature.isOnSurface() + ": ", ex);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/* 2965 */     if (creature instanceof Player && !passenger && creature.getBridgeId() != -10L)
/*      */     {
/*      */       
/* 2968 */       if (getStructure() != null && getStructure().isTypeBridge()) {
/* 2969 */         getStructure().setWalkedOnBridge(System.currentTimeMillis());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void updateNeighbourTileDoors(Creature creature, int tilex, int tiley) {
/* 2989 */     if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(tilex, tiley - 1)))) {
/*      */       
/* 2991 */       VolaTile newTile = Zones.getOrCreateTile(tilex, tiley - 1, true);
/* 2992 */       newTile.getSurfaceTile().doorCreatureMoved(creature, 0, 0);
/*      */     }
/*      */     else {
/*      */       
/* 2996 */       VolaTile newTile = Zones.getOrCreateTile(tilex, tiley - 1, false);
/* 2997 */       newTile.getCaveTile().doorCreatureMoved(creature, 0, 0);
/*      */     } 
/*      */ 
/*      */     
/* 3001 */     if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(tilex + 1, tiley)))) {
/*      */       
/* 3003 */       VolaTile newTile = Zones.getOrCreateTile(tilex + 1, tiley, true);
/* 3004 */       newTile.getSurfaceTile().doorCreatureMoved(creature, 0, 0);
/*      */     }
/*      */     else {
/*      */       
/* 3008 */       VolaTile newTile = Zones.getOrCreateTile(tilex + 1, tiley, false);
/* 3009 */       newTile.getCaveTile().doorCreatureMoved(creature, 0, 0);
/*      */     } 
/*      */ 
/*      */     
/* 3013 */     if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(tilex, tiley + 1)))) {
/*      */       
/* 3015 */       VolaTile newTile = Zones.getOrCreateTile(tilex, tiley + 1, true);
/* 3016 */       newTile.getSurfaceTile().doorCreatureMoved(creature, 0, 0);
/*      */     }
/*      */     else {
/*      */       
/* 3020 */       VolaTile newTile = Zones.getOrCreateTile(tilex, tiley + 1, false);
/* 3021 */       newTile.getCaveTile().doorCreatureMoved(creature, 0, 0);
/*      */     } 
/*      */ 
/*      */     
/* 3025 */     if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(tilex - 1, tiley)))) {
/*      */       
/* 3027 */       VolaTile newTile = Zones.getOrCreateTile(tilex - 1, tiley, true);
/* 3028 */       newTile.getSurfaceTile().doorCreatureMoved(creature, 0, 0);
/*      */     }
/*      */     else {
/*      */       
/* 3032 */       VolaTile newTile = Zones.getOrCreateTile(tilex - 1, tiley, false);
/* 3033 */       newTile.getCaveTile().doorCreatureMoved(creature, 0, 0);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void doorCreatureMoved(Creature creature, int diffTileX, int diffTileY) {
/* 3039 */     if (this.doors != null)
/*      */     {
/* 3041 */       for (Iterator<Door> it = this.doors.iterator(); it.hasNext(); ) {
/*      */         
/* 3043 */         Door door = it.next();
/* 3044 */         door.creatureMoved(creature, diffTileX, diffTileY);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public Creature[] getCreatures() {
/* 3056 */     if (this.creatures != null) {
/* 3057 */       return this.creatures.<Creature>toArray(new Creature[this.creatures.size()]);
/*      */     }
/* 3059 */     return emptyCreatures;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Item[] getItems() {
/* 3068 */     if (this.vitems != null) {
/* 3069 */       return this.vitems.getAllItemsAsArray();
/*      */     }
/* 3071 */     return emptyItems;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final Effect[] getEffects() {
/* 3082 */     if (this.effects != null) {
/* 3083 */       return this.effects.<Effect>toArray(new Effect[this.effects.size()]);
/*      */     }
/* 3085 */     return emptyEffects;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public VirtualZone[] getWatchers() {
/* 3094 */     if (this.watchers != null) {
/* 3095 */       return this.watchers.<VirtualZone>toArray(new VirtualZone[this.watchers.size()]);
/*      */     }
/* 3097 */     return emptyWatchers;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getMaxFloorLevel() {
/* 3102 */     if (!this.surfaced || this.isTransition)
/* 3103 */       return 3; 
/* 3104 */     int toRet = 0;
/* 3105 */     if (this.floors != null) {
/*      */       
/* 3107 */       toRet = 1;
/* 3108 */       for (Floor f : this.floors) {
/*      */ 
/*      */ 
/*      */         
/* 3112 */         if (f.getFloorLevel() > toRet)
/*      */         {
/* 3114 */           toRet = f.getFloorLevel();
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 3119 */     if (this.bridgeParts != null) {
/*      */       
/* 3121 */       toRet = 1;
/* 3122 */       for (BridgePart b : this.bridgeParts) {
/*      */         
/* 3124 */         if (b.getFloorLevel() > toRet)
/*      */         {
/* 3126 */           toRet = b.getFloorLevel();
/*      */         }
/*      */       } 
/*      */     } 
/* 3130 */     return toRet;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getDropFloorLevel(int maxFloorLevel) {
/* 3138 */     int toRet = 0;
/* 3139 */     if (this.floors != null)
/*      */     {
/* 3141 */       for (Floor f : this.floors) {
/*      */         
/* 3143 */         if (f.isSolid()) {
/*      */           
/* 3145 */           if (f.getFloorLevel() == maxFloorLevel)
/*      */           {
/* 3147 */             return maxFloorLevel;
/*      */           }
/* 3149 */           if (f.getFloorLevel() < maxFloorLevel && f.getFloorLevel() > toRet)
/*      */           {
/* 3151 */             toRet = f.getFloorLevel();
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/* 3156 */     return toRet;
/*      */   }
/*      */ 
/*      */   
/*      */   void sendRemoveItem(Item item) {
/* 3161 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 3165 */         vz.removeItem(item);
/*      */       }
/* 3167 */       catch (Exception e) {
/*      */         
/* 3169 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void change() {
/* 3176 */     checkTransition();
/* 3177 */     Item stumpToDestroy = null;
/* 3178 */     if (this.vitems != null)
/*      */     {
/* 3180 */       for (Item item : this.vitems.getAllItemsAsArray()) {
/*      */         
/* 3182 */         if (item.getTileX() == this.tilex && item.getTileY() == this.tiley && item.getBridgeId() == -10L) {
/*      */           
/* 3184 */           boolean ok = true;
/* 3185 */           if (item.isVehicle()) {
/*      */             
/* 3187 */             Vehicle vehic = Vehicles.getVehicle(item);
/* 3188 */             if ((vehic.getHitched()).length > 0) {
/*      */               
/* 3190 */               ok = false;
/*      */             } else {
/*      */               
/* 3193 */               for (Seat seat : vehic.getSeats()) {
/*      */                 
/* 3195 */                 if (seat.occupant > 0L)
/*      */                 {
/* 3197 */                   ok = false; } 
/*      */               } 
/*      */             } 
/*      */           } 
/* 3201 */           if (item.getTemplateId() == 731) {
/*      */             
/* 3203 */             stumpToDestroy = item;
/*      */           }
/*      */           else {
/*      */             
/* 3207 */             int oldFloorLevel = item.getFloorLevel();
/* 3208 */             item.updatePosZ(this);
/* 3209 */             Item pileItem = this.vitems.getPileItem(item.getFloorLevel());
/* 3210 */             if (oldFloorLevel != item.getFloorLevel()) {
/*      */               
/* 3212 */               this.vitems.moveToNewFloorLevel(item, oldFloorLevel);
/* 3213 */               logger.log(Level.INFO, item
/* 3214 */                   .getName() + " moving from " + oldFloorLevel + " fl=" + item.getFloorLevel());
/*      */             } 
/* 3216 */             if (ok)
/*      */             {
/* 3218 */               if (pileItem == null || item.isDecoration()) {
/*      */                 
/* 3220 */                 boolean onGroundLevel = true;
/* 3221 */                 if (item.getFloorLevel() > 0) {
/* 3222 */                   onGroundLevel = false;
/*      */                 
/*      */                 }
/* 3225 */                 else if ((getFloors(0, 0)).length > 0) {
/* 3226 */                   onGroundLevel = false;
/*      */                 } 
/* 3228 */                 for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */                   
/*      */                   try {
/* 3232 */                     vz.removeItem(item);
/* 3233 */                     if (vz.isVisible(item, this)) {
/* 3234 */                       vz.addItem(item, this, onGroundLevel);
/*      */                     }
/* 3236 */                   } catch (Exception e) {
/*      */                     
/* 3238 */                     logger.log(Level.WARNING, e.getMessage(), e);
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/* 3247 */     if (this.vitems != null)
/*      */     {
/* 3249 */       for (Item pileItem : this.vitems.getPileItems()) {
/*      */         
/* 3251 */         if (pileItem.getBridgeId() == -10L) {
/*      */           
/* 3253 */           int oldFloorLevel = pileItem.getFloorLevel();
/* 3254 */           pileItem.updatePosZ(this);
/* 3255 */           boolean destroy = false;
/* 3256 */           if (pileItem.getFloorLevel() != oldFloorLevel)
/*      */           {
/* 3258 */             destroy = this.vitems.movePileItemToNewFloorLevel(pileItem, oldFloorLevel);
/*      */           }
/* 3260 */           if (!destroy) {
/*      */             
/* 3262 */             boolean onGroundLevel = true;
/* 3263 */             if (pileItem.getFloorLevel() > 0) {
/* 3264 */               onGroundLevel = false;
/*      */             
/*      */             }
/* 3267 */             else if ((getFloors(0, 0)).length > 0) {
/* 3268 */               onGroundLevel = false;
/*      */             } 
/* 3270 */             for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */               
/*      */               try {
/* 3274 */                 vz.removeItem(pileItem);
/* 3275 */                 if (vz.isVisible(pileItem, this)) {
/* 3276 */                   vz.addItem(pileItem, this, onGroundLevel);
/*      */                 }
/* 3278 */               } catch (Exception e) {
/*      */                 
/* 3280 */                 logger.log(Level.WARNING, e.getMessage(), e);
/*      */               }
/*      */             
/*      */             } 
/*      */           } else {
/*      */             
/* 3286 */             destroyPileItem(pileItem);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/* 3291 */     if (this.effects != null)
/*      */     {
/* 3293 */       for (Iterator<Effect> it = this.effects.iterator(); it.hasNext(); ) {
/*      */         
/* 3295 */         Effect effect = it.next();
/* 3296 */         if (effect.getTileX() == this.tilex && effect.getTileY() == this.tiley) {
/*      */ 
/*      */           
/*      */           try {
/* 3300 */             if (this.structure == null)
/*      */             {
/* 3302 */               if (this.isTransition) {
/* 3303 */                 effect.setPosZ(Zones.calculateHeight(effect.getPosX(), effect.getPosY(), false));
/*      */               } else {
/*      */                 
/* 3306 */                 long owner = effect.getOwner();
/* 3307 */                 long bridgeId = -10L;
/* 3308 */                 if (WurmId.getType(owner) == 2) {
/*      */ 
/*      */                   
/*      */                   try {
/* 3312 */                     Item i = Items.getItem(owner);
/* 3313 */                     bridgeId = i.onBridge();
/*      */                   }
/* 3315 */                   catch (NoSuchItemException noSuchItemException) {}
/*      */ 
/*      */ 
/*      */                 
/*      */                 }
/* 3320 */                 else if (WurmId.getType(owner) == 1) {
/*      */ 
/*      */                   
/*      */                   try {
/* 3324 */                     Creature c = Creatures.getInstance().getCreature(owner);
/* 3325 */                     bridgeId = c.getBridgeId();
/*      */                   }
/* 3327 */                   catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */ 
/*      */ 
/*      */                 
/*      */                 }
/* 3332 */                 else if (WurmId.getType(owner) == 0) {
/*      */ 
/*      */                   
/*      */                   try {
/* 3336 */                     Player p = Players.getInstance().getPlayer(owner);
/* 3337 */                     bridgeId = p.getBridgeId();
/*      */                   }
/* 3339 */                   catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */                 } 
/*      */ 
/*      */ 
/*      */                 
/* 3344 */                 float height = Zones.calculatePosZ(effect.getPosX(), effect.getPosY(), this, this.surfaced, false, effect
/* 3345 */                     .getPosZ(), null, bridgeId);
/* 3346 */                 effect.setPosZ(height);
/*      */               }
/*      */             
/*      */             }
/* 3350 */           } catch (NoSuchZoneException nsz) {
/*      */             
/* 3352 */             logger.log(Level.WARNING, effect.getId() + " moved out of zone.");
/*      */           } 
/* 3354 */           for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */             
/*      */             try {
/* 3358 */               vz.removeEffect(effect);
/* 3359 */               vz.addEffect(effect, false);
/*      */             }
/* 3361 */             catch (Exception e) {
/*      */               
/* 3363 */               logger.log(Level.WARNING, e.getMessage(), e);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/* 3369 */     if (this.creatures != null)
/*      */     {
/* 3371 */       for (Iterator<Creature> it = this.creatures.iterator(); it.hasNext(); ) {
/*      */         
/* 3373 */         Creature creature = it.next();
/* 3374 */         if (creature.getBridgeId() == -10L)
/*      */         {
/* 3376 */           if (!(creature instanceof Player)) {
/*      */             
/* 3378 */             if (creature.isSubmerged()) {
/* 3379 */               creature.submerge(); continue;
/* 3380 */             }  if (creature.getVehicle() < 0L) {
/*      */               
/* 3382 */               float oldPosZ = creature.getStatus().getPositionZ();
/*      */               
/* 3384 */               float newPosZ = 1.0F;
/* 3385 */               boolean surf = this.surfaced;
/* 3386 */               if (this.isTransition)
/* 3387 */                 surf = false; 
/* 3388 */               newPosZ = Zones.calculatePosZ(creature.getPosX(), creature.getPosY(), this, surf, false, creature
/* 3389 */                   .getPositionZ(), null, creature.getBridgeId());
/* 3390 */               creature.setPositionZ(Math.max(-1.25F, newPosZ));
/*      */               
/*      */               try {
/* 3393 */                 creature.savePosition(this.zone.id);
/*      */               }
/* 3395 */               catch (Exception iox) {
/*      */                 
/* 3397 */                 logger.log(Level.WARNING, creature.getName() + ": " + iox.getMessage(), iox);
/*      */               } 
/* 3399 */               for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */                 
/*      */                 try {
/* 3403 */                   vz.creatureMoved(creature.getWurmId(), 0.0F, 0.0F, newPosZ - oldPosZ, 0, 0);
/*      */                 }
/* 3405 */                 catch (NoSuchCreatureException nsc) {
/*      */                   
/* 3407 */                   logger.log(Level.INFO, "Creature not found when changing height of tile.", (Throwable)nsc);
/*      */                 }
/* 3409 */                 catch (NoSuchPlayerException nsp) {
/*      */                   
/* 3411 */                   logger.log(Level.INFO, "Player not found when changing height of tile.", (Throwable)nsp);
/*      */                 }
/* 3413 */                 catch (Exception e) {
/*      */                   
/* 3415 */                   logger.log(Level.WARNING, e.getMessage(), e);
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/* 3424 */     if (stumpToDestroy != null)
/*      */     {
/* 3426 */       Items.destroyItem(stumpToDestroy.getWurmId());
/*      */     }
/*      */     
/* 3429 */     checkIsLava();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Wall[] getWalls() {
/* 3438 */     if (this.walls != null) {
/* 3439 */       return this.walls.<Wall>toArray(new Wall[this.walls.size()]);
/*      */     }
/* 3441 */     return emptyWalls;
/*      */   }
/*      */ 
/*      */   
/*      */   public BridgePart[] getBridgeParts() {
/* 3446 */     if (this.bridgeParts != null) {
/* 3447 */       return this.bridgeParts.<BridgePart>toArray(new BridgePart[this.bridgeParts.size()]);
/*      */     }
/* 3449 */     return emptyBridgeParts;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Set<StructureSupport> getAllSupport() {
/* 3454 */     if (this.walls == null && this.fences == null && this.floors == null)
/* 3455 */       return emptySupports; 
/* 3456 */     Set<StructureSupport> toReturn = new HashSet<>();
/* 3457 */     if (this.walls != null)
/*      */     {
/*      */       
/* 3460 */       for (Wall w : this.walls)
/*      */       {
/* 3462 */         toReturn.add(w);
/*      */       }
/*      */     }
/* 3465 */     if (this.fences != null)
/* 3466 */       toReturn.addAll((Collection)this.fences.values()); 
/* 3467 */     if (this.floors != null)
/* 3468 */       toReturn.addAll(this.floors); 
/* 3469 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Wall[] getWallsForLevel(int floorLevel) {
/* 3478 */     if (this.walls != null) {
/*      */       
/* 3480 */       Set<Wall> wallsSet = new HashSet<>();
/* 3481 */       for (Wall w : this.walls) {
/*      */         
/* 3483 */         if (w.getFloorLevel() == floorLevel)
/* 3484 */           wallsSet.add(w); 
/*      */       } 
/* 3486 */       return wallsSet.<Wall>toArray(new Wall[wallsSet.size()]);
/*      */     } 
/*      */     
/* 3489 */     return emptyWalls;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Wall[] getExteriorWalls() {
/* 3497 */     if (this.walls != null) {
/*      */       
/* 3499 */       Set<Wall> wallsSet = new HashSet<>();
/* 3500 */       for (Wall w : this.walls) {
/*      */         
/* 3502 */         if (!w.isIndoor())
/* 3503 */           wallsSet.add(w); 
/*      */       } 
/* 3505 */       return wallsSet.<Wall>toArray(new Wall[wallsSet.size()]);
/*      */     } 
/*      */     
/* 3508 */     return emptyWalls;
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
/*      */   Wall getWall(long wallId) throws NoSuchWallException {
/* 3520 */     if (this.walls != null)
/*      */     {
/* 3522 */       for (Iterator<Wall> it = this.walls.iterator(); it.hasNext(); ) {
/*      */         
/* 3524 */         Wall wall = it.next();
/* 3525 */         if (wall.getId() == wallId)
/* 3526 */           return wall; 
/*      */       } 
/*      */     }
/* 3529 */     throw new NoSuchWallException("There are no walls on this tile so cannot find wallid: " + wallId);
/*      */   }
/*      */ 
/*      */   
/*      */   Wall getWall(int startX, int startY, int endX, int endY, boolean horizontal) {
/* 3534 */     if (this.walls != null)
/*      */     {
/* 3536 */       for (Iterator<Wall> it = this.walls.iterator(); it.hasNext(); ) {
/*      */         
/* 3538 */         Wall wall = it.next();
/*      */         
/* 3540 */         if (wall.getStartX() == startX && wall.getStartY() == startY && wall.getEndX() == endX && wall
/* 3541 */           .getEndY() == endY)
/*      */         {
/* 3543 */           if (wall.isHorizontal() == horizontal)
/* 3544 */             return wall; 
/*      */         }
/*      */       } 
/*      */     }
/* 3548 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Structure getStructure() {
/* 3557 */     return this.structure;
/*      */   }
/*      */ 
/*      */   
/*      */   public void deleteStructure(long wurmStructureId) {
/* 3562 */     if (this.structure == null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 3567 */     if (this.structure.getWurmId() != wurmStructureId) {
/*      */       
/* 3569 */       logger.log(Level.WARNING, "Tried to delete structure " + wurmStructureId + " from VolaTile [" + this.tilex + "," + this.tiley + "] but it was structure " + this.structure
/* 3570 */           .getWurmId() + " so nothing was deleted.");
/*      */       
/*      */       return;
/*      */     } 
/* 3574 */     if (this.walls != null)
/*      */     {
/* 3576 */       for (Iterator<Wall> it = this.walls.iterator(); it.hasNext(); ) {
/*      */         
/* 3578 */         Wall wall = it.next();
/* 3579 */         if (wall.getStructureId() == wurmStructureId) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3584 */           if (wall.getType() == StructureTypeEnum.DOOR || wall.getType() == StructureTypeEnum.DOUBLE_DOOR || wall
/* 3585 */             .getType() == StructureTypeEnum.PORTCULLIS || wall.getType() == StructureTypeEnum.CANOPY_DOOR || wall
/* 3586 */             .isArched()) {
/*      */             
/* 3588 */             Door[] alld = getDoors();
/* 3589 */             for (int x = 0; x < alld.length; x++) {
/*      */ 
/*      */               
/*      */               try {
/* 3593 */                 if (alld[x].getWall() == wall)
/*      */                 {
/* 3595 */                   alld[x].removeFromTiles();
/*      */                   
/* 3597 */                   alld[x].delete();
/*      */                 }
/*      */               
/* 3600 */               } catch (NoSuchWallException nsw) {
/*      */                 
/* 3602 */                 logger.log(Level.WARNING, nsw.getMessage(), (Throwable)nsw);
/*      */               } 
/*      */             } 
/*      */           } 
/* 3606 */           wall.delete();
/* 3607 */           it.remove();
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3617 */     if (this.floors != null)
/*      */     {
/* 3619 */       for (Iterator<Floor> it = this.floors.iterator(); it.hasNext(); ) {
/*      */         
/* 3621 */         Floor floor = it.next();
/* 3622 */         if (floor.getStructureId() == wurmStructureId) {
/*      */           
/* 3624 */           floor.delete();
/* 3625 */           it.remove();
/* 3626 */           if (floor.isStair()) {
/* 3627 */             Stairs.removeStair(hashCode(), floor.getFloorLevel());
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 3635 */     if (this.bridgeParts != null)
/*      */     {
/* 3637 */       for (Iterator<BridgePart> it = this.bridgeParts.iterator(); it.hasNext(); ) {
/*      */         
/* 3639 */         BridgePart bridgepart = it.next();
/* 3640 */         if (bridgepart.getStructureId() == wurmStructureId) {
/*      */           
/* 3642 */           bridgepart.delete();
/* 3643 */           it.remove();
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/* 3648 */     if (this.fences != null)
/*      */     {
/* 3650 */       for (Fence fence : getFences()) {
/*      */         
/* 3652 */         if (fence.getFloorLevel() > 0) {
/* 3653 */           fence.destroy();
/*      */         }
/*      */       } 
/*      */     }
/* 3657 */     VolaTile tw = Zones.getTileOrNull(getTileX() + 1, getTileY(), isOnSurface());
/* 3658 */     if (tw != null)
/*      */     {
/*      */       
/* 3661 */       if (tw.getStructure() == null)
/*      */       {
/* 3663 */         for (Fence fence : tw.getFences()) {
/*      */           
/* 3665 */           if (fence.getFloorLevel() > 0)
/* 3666 */             fence.destroy(); 
/*      */         } 
/*      */       }
/*      */     }
/* 3670 */     VolaTile ts = Zones.getTileOrNull(getTileX(), getTileY() + 1, isOnSurface());
/* 3671 */     if (ts != null)
/*      */     {
/*      */       
/* 3674 */       if (ts.getStructure() == null)
/*      */       {
/* 3676 */         for (Fence fence : ts.getFences()) {
/*      */           
/* 3678 */           if (fence.getFloorLevel() > 0)
/* 3679 */             fence.destroy(); 
/*      */         } 
/*      */       }
/*      */     }
/* 3683 */     if (this.watchers != null) {
/*      */       
/* 3685 */       logger.log(Level.INFO, "deleteStructure " + wurmStructureId + " (Watchers  " + this.watchers.size() + ")");
/*      */       
/* 3687 */       for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext(); ) {
/*      */         
/* 3689 */         VirtualZone lZone = it.next();
/* 3690 */         lZone.deleteStructure(this.structure);
/*      */       } 
/*      */     } 
/*      */     
/* 3694 */     if (this.vitems != null) {
/*      */       
/* 3696 */       for (Item pileItem : this.vitems.getPileItems()) {
/*      */         
/* 3698 */         if (pileItem.getFloorLevel() > 0) {
/*      */           
/* 3700 */           float pileHeight = pileItem.getPosZ();
/* 3701 */           float tileHeight = 0.0F;
/*      */ 
/*      */           
/*      */           try {
/* 3705 */             tileHeight = Zones.calculateHeight(getPosX(), getPosY(), isOnSurface());
/*      */           }
/* 3707 */           catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */           
/* 3710 */           if (tileHeight != pileHeight)
/*      */           {
/* 3712 */             destroyPileItem(pileItem.getFloorLevel());
/*      */           }
/*      */         }
/*      */         else {
/*      */           
/* 3717 */           pileItem.updatePosZ(this);
/*      */           
/* 3719 */           for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */             
/*      */             try {
/* 3723 */               if (vz.isVisible(pileItem, this))
/*      */               {
/* 3725 */                 vz.removeItem(pileItem);
/* 3726 */                 vz.addItem(pileItem, this, true);
/*      */               }
/*      */             
/* 3729 */             } catch (Exception e) {
/*      */               
/* 3731 */               logger.log(Level.WARNING, e.getMessage(), e);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 3736 */       for (Item item : this.vitems.getAllItemsAsArray()) {
/*      */         
/* 3738 */         if (item.getParentId() == -10L) {
/*      */           
/* 3740 */           item.updatePosZ(this);
/* 3741 */           item.updateIfGroundItem();
/* 3742 */           item.setOnBridge(-10L);
/*      */         } 
/*      */       } 
/*      */     } 
/* 3746 */     if (this.creatures != null)
/*      */     {
/* 3748 */       for (Creature c : this.creatures) {
/*      */         
/* 3750 */         if (!c.isPlayer()) {
/*      */           
/* 3752 */           float oldposz = c.getPositionZ();
/* 3753 */           float newPosz = c.calculatePosZ();
/* 3754 */           float diffz = newPosz - oldposz;
/* 3755 */           c.setPositionZ(newPosz);
/* 3756 */           c.moved(0.0F, 0.0F, diffz, 0, 0);
/*      */         }
/*      */         else {
/*      */           
/* 3760 */           c.getCommunicator().setGroundOffset(0, true);
/*      */         } 
/* 3762 */         c.setBridgeId(-10L);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 3767 */     this.structure = null;
/* 3768 */     if (this.walls != null)
/*      */     {
/* 3770 */       for (Iterator<Wall> it = this.walls.iterator(); it.hasNext(); ) {
/*      */         
/* 3772 */         Wall wall = it.next();
/* 3773 */         long sid = wall.getStructureId();
/*      */         
/*      */         try {
/* 3776 */           Structure struct = Structures.getStructure(sid);
/*      */           
/* 3778 */           this.structure = struct;
/* 3779 */           struct.addBuildTile(this, false);
/*      */           
/*      */           break;
/* 3782 */         } catch (NoSuchStructureException sns) {
/*      */           
/* 3784 */           logger.log(Level.WARNING, sns.getMessage(), " for wall " + wall);
/*      */         }
/* 3786 */         catch (NoSuchZoneException nsz) {
/*      */           
/* 3788 */           logger.log(Level.INFO, "Out of bounds?: " + nsz.getMessage(), (Throwable)nsz);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateStructureForZone(VirtualZone vzone, Structure _structure, int aTilex, int aTiley) {
/* 3798 */     vzone.sendStructureWalls(_structure);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addStructure(Structure _structure) {
/* 3803 */     if (this.structure == null)
/*      */     {
/* 3805 */       if (this.watchers != null)
/*      */       {
/* 3807 */         for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext(); ) {
/*      */           
/* 3809 */           VirtualZone vzone = it.next();
/* 3810 */           updateStructureForZone(vzone, _structure, this.tilex, this.tiley);
/*      */         } 
/*      */       }
/*      */     }
/* 3814 */     this.structure = _structure;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addBridge(Structure bridge) {
/* 3819 */     if (this.structure == null)
/*      */     {
/* 3821 */       if (this.watchers != null)
/*      */       {
/* 3823 */         for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext(); ) {
/*      */           
/* 3825 */           VirtualZone vzone = it.next();
/* 3826 */           vzone.addStructure(bridge);
/*      */         } 
/*      */       }
/*      */     }
/* 3830 */     this.structure = bridge;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addBuildMarker(Structure _structure) {
/* 3836 */     if (this.structure == null)
/*      */     {
/* 3838 */       if (this.watchers != null)
/*      */       {
/* 3840 */         for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext(); ) {
/*      */           
/* 3842 */           VirtualZone vzone = it.next();
/* 3843 */           vzone.addBuildMarker(_structure, this.tilex, this.tiley);
/*      */         } 
/*      */       }
/*      */     }
/* 3847 */     this.structure = _structure;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setStructureAtLoad(Structure _structure) {
/* 3852 */     this.structure = _structure;
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeBuildMarker(Structure _structure, int _tilex, int _tiley) {
/* 3857 */     if (this.structure != null) {
/*      */       
/* 3859 */       if (this.watchers != null)
/*      */       {
/* 3861 */         for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext(); ) {
/*      */           
/* 3863 */           VirtualZone vzone = it.next();
/* 3864 */           vzone.removeBuildMarker(_structure, _tilex, _tiley);
/*      */         } 
/*      */       }
/* 3867 */       this.structure = null;
/*      */     } else {
/*      */       
/* 3870 */       logger.log(Level.INFO, "Hmm tried to remove buildmarker from a tile that didn't contain it.");
/*      */     } 
/*      */   }
/*      */   
/*      */   public void finalizeBuildPlan(long oldStructureId, long newStructureId) {
/* 3875 */     if (this.structure != null)
/*      */     {
/* 3877 */       if (this.watchers != null)
/*      */       {
/* 3879 */         for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext(); ) {
/*      */           
/* 3881 */           VirtualZone lZone = it.next();
/* 3882 */           lZone.finalizeBuildPlan(oldStructureId, newStructureId);
/*      */         } 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addWall(StructureTypeEnum type, int x1, int y1, int x2, int y2, float qualityLevel, long structureId, boolean isIndoor) {
/* 3891 */     if (logger.isLoggable(Level.FINEST))
/*      */     {
/* 3893 */       logger.finest("StructureID: " + structureId + " adding wall at " + x1 + "-" + y1 + "," + x2 + "-" + y2 + ", QL: " + qualityLevel);
/*      */     }
/*      */ 
/*      */     
/* 3897 */     DbWall dbWall = new DbWall(type, this.tilex, this.tiley, x1, y1, x2, y2, qualityLevel, structureId, StructureMaterialEnum.WOOD, isIndoor, 0, getLayer());
/* 3898 */     addWall((Wall)dbWall);
/* 3899 */     updateWall((Wall)dbWall);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addWall(Wall wall) {
/* 3904 */     if (this.walls == null) {
/* 3905 */       this.walls = new HashSet<>();
/*      */     }
/*      */ 
/*      */     
/* 3909 */     boolean removedOneWall = false;
/* 3910 */     for (Wall w : this.walls) {
/*      */       
/* 3912 */       removedOneWall = false;
/* 3913 */       if (wall.heightOffset == 0 && w.heightOffset == 0)
/*      */       {
/* 3915 */         if (wall.x1 == w.x1 && wall.x2 == w.x2 && wall.y1 == w.y1 && wall.y2 == w.y2) {
/*      */           
/* 3917 */           if ((wall.getType()).value <= (w.getType()).value) {
/*      */ 
/*      */             
/* 3920 */             removedOneWall = true;
/*      */             break;
/*      */           } 
/* 3923 */           if ((w.getType()).value < (wall.getType()).value)
/*      */           {
/*      */             
/* 3926 */             toRemove.add(w);
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/* 3931 */     if (!removedOneWall)
/* 3932 */       this.walls.add(wall); 
/* 3933 */     if (removedOneWall) {
/* 3934 */       logger.log(Level.INFO, "Not adding wall at " + wall.getTileX() + ", " + wall.getTileY() + ", structure: " + this.structure);
/*      */     }
/* 3936 */     for (Wall torem : toRemove) {
/*      */       
/* 3938 */       logger.log(Level.INFO, "Deleting wall at " + torem.getTileX() + ", " + torem.getTileY() + ", structure: " + this.structure);
/*      */       
/* 3940 */       torem.delete();
/*      */     } 
/* 3942 */     toRemove.clear();
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateFloor(Floor floor) {
/* 3947 */     if (this.structure != null) {
/*      */       
/* 3949 */       if (this.watchers != null)
/*      */       {
/* 3951 */         for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */           
/*      */           try {
/* 3955 */             vz.updateFloor(this.structure.getWurmId(), floor);
/*      */           }
/* 3957 */           catch (Exception e) {
/*      */             
/* 3959 */             logger.log(Level.WARNING, e.getMessage(), e);
/*      */           } 
/*      */         } 
/*      */       }
/*      */       
/*      */       try {
/* 3965 */         floor.save();
/*      */       }
/* 3967 */       catch (IOException iox) {
/*      */         
/* 3969 */         logger.log(Level.WARNING, "Failed to save structure floor: " + floor.getId() + '.', iox);
/*      */       } 
/*      */       
/* 3972 */       if (floor.isFinished())
/*      */       {
/* 3974 */         if (this.vitems != null)
/*      */         {
/* 3976 */           for (Item item : this.vitems.getAllItemsAsArray()) {
/*      */             
/* 3978 */             item.updatePosZ(this);
/* 3979 */             item.updateIfGroundItem();
/*      */           } 
/*      */         }
/*      */       }
/* 3983 */       if (this.vitems != null)
/*      */       {
/* 3985 */         for (Item pile : this.vitems.getPileItems()) {
/*      */           
/* 3987 */           pile.updatePosZ(this);
/* 3988 */           for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */             
/*      */             try {
/* 3992 */               if (vz.isVisible(pile, this))
/*      */               {
/* 3994 */                 vz.removeItem(pile);
/* 3995 */                 vz.addItem(pile, this, true);
/*      */               }
/*      */             
/* 3998 */             } catch (Exception e) {
/*      */               
/* 4000 */               logger.log(Level.WARNING, e.getMessage(), e);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateBridgePart(BridgePart bridgePart) {
/* 4011 */     if (!isOnSurface() && !Features.Feature.CAVE_BRIDGES.isEnabled()) {
/*      */       
/* 4013 */       getSurfaceTile().updateBridgePart(bridgePart);
/*      */ 
/*      */     
/*      */     }
/* 4017 */     else if (this.structure != null) {
/*      */       
/* 4019 */       if (this.watchers != null)
/*      */       {
/* 4021 */         for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */           
/*      */           try {
/* 4025 */             vz.updateBridgePart(this.structure.getWurmId(), bridgePart);
/*      */           }
/* 4027 */           catch (Exception e) {
/*      */             
/* 4029 */             logger.log(Level.WARNING, e.getMessage(), e);
/*      */           } 
/*      */         } 
/*      */       }
/*      */       
/*      */       try {
/* 4035 */         bridgePart.save();
/*      */       }
/* 4037 */       catch (IOException iox) {
/*      */         
/* 4039 */         logger.log(Level.WARNING, "Failed to save structure bridge part: " + bridgePart.getId() + '.', iox);
/*      */       } 
/* 4041 */       if (bridgePart.getState() != BridgeConstants.BridgeState.COMPLETED.getCode()) {
/*      */ 
/*      */         
/* 4044 */         if (this.vitems != null) {
/*      */           
/* 4046 */           for (Item item : this.vitems.getAllItemsAsArray()) {
/*      */             
/* 4048 */             if (item.onBridge() == this.structure.getWurmId()) {
/*      */               
/* 4050 */               item.setOnBridge(-10L);
/* 4051 */               for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */                 
/*      */                 try {
/* 4055 */                   if (item.getParentId() == -10L && vz.isVisible(item, this))
/*      */                   {
/* 4057 */                     vz.removeItem(item);
/* 4058 */                     item.setPosZ(-3000.0F);
/* 4059 */                     vz.addItem(item, this, true);
/*      */                   }
/*      */                 
/* 4062 */                 } catch (Exception e) {
/*      */                   
/* 4064 */                   logger.log(Level.WARNING, e.getMessage(), e);
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/* 4069 */           for (Item pile : this.vitems.getPileItems()) {
/*      */             
/* 4071 */             if (pile.onBridge() == this.structure.getWurmId()) {
/*      */               
/* 4073 */               pile.setOnBridge(-10L);
/* 4074 */               for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */                 
/*      */                 try {
/* 4078 */                   if (vz.isVisible(pile, this))
/*      */                   {
/* 4080 */                     pile.setPosZ(-3000.0F);
/* 4081 */                     vz.removeItem(pile);
/* 4082 */                     vz.addItem(pile, this, true);
/*      */                   }
/*      */                 
/* 4085 */                 } catch (Exception e) {
/*      */                   
/* 4087 */                   logger.log(Level.WARNING, e.getMessage(), e);
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         
/* 4094 */         if (this.creatures != null)
/*      */         {
/* 4096 */           for (Creature c : this.creatures) {
/*      */             
/* 4098 */             if (c.getBridgeId() == this.structure.getWurmId()) {
/*      */               
/* 4100 */               c.setBridgeId(-10L);
/* 4101 */               if (!c.isPlayer()) {
/*      */                 
/* 4103 */                 float oldposz = c.getPositionZ();
/* 4104 */                 float newPosz = c.calculatePosZ();
/* 4105 */                 float diffz = newPosz - oldposz;
/* 4106 */                 c.setPositionZ(newPosz);
/* 4107 */                 c.moved(0.0F, 0.0F, diffz, 0, 0);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateWall(Wall wall) {
/* 4128 */     if (this.structure != null) {
/*      */       
/* 4130 */       if (this.watchers != null)
/*      */       {
/* 4132 */         for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */           
/*      */           try {
/* 4136 */             vz.updateWall(this.structure.getWurmId(), wall);
/*      */           }
/* 4138 */           catch (Exception e) {
/*      */             
/* 4140 */             logger.log(Level.WARNING, e.getMessage(), e);
/*      */           } 
/*      */         } 
/*      */       }
/* 4144 */       if (this.structure.isFinalized()) {
/*      */         
/*      */         try {
/*      */           
/* 4148 */           wall.save();
/*      */         }
/* 4150 */         catch (IOException iox) {
/*      */           
/* 4152 */           logger.log(Level.WARNING, "Failed to save structure wall: " + wall.getId() + '.', iox);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void linkTo(VirtualZone aZone, boolean aRemove) {
/* 4161 */     linkStructureToZone(aZone, aRemove);
/* 4162 */     linkFencesToZone(aZone, aRemove);
/* 4163 */     linkDoorsToZone(aZone, aRemove);
/* 4164 */     linkMineDoorsToZone(aZone, aRemove);
/* 4165 */     linkCreaturesToZone(aZone, aRemove);
/* 4166 */     linkItemsToZone(aZone, aRemove);
/* 4167 */     linkPileToZone(aZone, aRemove);
/* 4168 */     linkEffectsToZone(aZone, aRemove);
/* 4169 */     linkAreaEffectsToZone(aZone, aRemove);
/*      */   }
/*      */ 
/*      */   
/*      */   private void linkAreaEffectsToZone(VirtualZone aZone, boolean aRemove) {
/* 4174 */     if (aZone.getWatcher().isPlayer()) {
/*      */       
/* 4176 */       AreaSpellEffect ae = getAreaEffect();
/* 4177 */       if (ae != null && !aRemove && aZone.covers(this.tilex, this.tiley)) {
/*      */         
/* 4179 */         aZone.addAreaSpellEffect(ae, true);
/*      */       }
/* 4181 */       else if (ae != null) {
/*      */         
/* 4183 */         aZone.removeAreaSpellEffect(ae);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void linkDoorsToZone(VirtualZone aZone, boolean aRemove) {
/* 4190 */     if (this.doors != null)
/*      */     {
/* 4192 */       if (!aRemove && aZone.covers(this.tilex, this.tiley)) {
/*      */         
/* 4194 */         for (Iterator<Door> it = this.doors.iterator(); it.hasNext(); )
/*      */         {
/* 4196 */           Door door = it.next();
/* 4197 */           aZone.addDoor(door);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 4202 */         for (Iterator<Door> it = this.doors.iterator(); it.hasNext(); ) {
/*      */           
/* 4204 */           Door door = it.next();
/* 4205 */           aZone.removeDoor(door);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void linkMineDoorsToZone(VirtualZone aZone, boolean aRemove) {
/* 4213 */     if (this.mineDoors != null)
/*      */     {
/* 4215 */       if (!aRemove && aZone.covers(this.tilex, this.tiley)) {
/*      */         
/* 4217 */         for (Iterator<MineDoorPermission> it = this.mineDoors.iterator(); it.hasNext(); )
/*      */         {
/* 4219 */           MineDoorPermission door = it.next();
/* 4220 */           aZone.addMineDoor(door);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 4225 */         for (Iterator<MineDoorPermission> it = this.mineDoors.iterator(); it.hasNext(); ) {
/*      */           
/* 4227 */           MineDoorPermission door = it.next();
/* 4228 */           aZone.removeMineDoor(door);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void linkFencesToZone(VirtualZone aZone, boolean aRemove) {
/* 4236 */     if (this.fences != null)
/*      */     {
/* 4238 */       if (!aRemove && aZone.covers(this.tilex, this.tiley)) {
/*      */         
/* 4240 */         for (Fence f : getFences()) {
/* 4241 */           aZone.addFence(f);
/*      */         }
/*      */       } else {
/*      */         
/* 4245 */         for (Fence f : getFences()) {
/* 4246 */           aZone.removeFence(f);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   protected void linkCreaturesToZone(VirtualZone aZone, boolean aRemove) {
/* 4253 */     if (this.creatures != null) {
/*      */       
/* 4255 */       Creature[] crets = getCreatures();
/* 4256 */       if (!aRemove && aZone.covers(this.tilex, this.tiley)) {
/*      */         
/* 4258 */         for (int x = 0; x < crets.length; x++) {
/*      */           
/* 4260 */           if (!crets[x].isDead()) {
/*      */             try
/*      */             {
/*      */               
/* 4264 */               aZone.addCreature(crets[x].getWurmId(), false);
/*      */             }
/* 4266 */             catch (NoSuchCreatureException cnf)
/*      */             {
/* 4268 */               this.creatures.remove(crets[x]);
/* 4269 */               logger.log(Level.INFO, crets[x].getName() + "," + cnf.getMessage(), (Throwable)cnf);
/*      */             }
/* 4271 */             catch (NoSuchPlayerException nsp)
/*      */             {
/* 4273 */               this.creatures.remove(crets[x]);
/* 4274 */               logger.log(Level.INFO, crets[x].getName() + "," + nsp.getMessage(), (Throwable)nsp);
/*      */             }
/*      */           
/*      */           }
/*      */         } 
/*      */       } else {
/*      */         
/* 4281 */         for (int x = 0; x < crets.length; x++) {
/*      */ 
/*      */           
/*      */           try {
/* 4285 */             aZone.deleteCreature(crets[x], true);
/*      */           }
/* 4287 */           catch (NoSuchPlayerException nsp) {
/*      */             
/* 4289 */             logger.log(Level.INFO, crets[x].getName() + "," + nsp.getMessage(), (Throwable)nsp);
/*      */           }
/* 4291 */           catch (NoSuchCreatureException nsc) {
/*      */             
/* 4293 */             logger.log(Level.INFO, crets[x].getName() + "," + nsc.getMessage(), (Throwable)nsc);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void linkPileToZone(VirtualZone aZone, boolean aRemove) {
/* 4302 */     if (this.vitems != null)
/*      */     {
/* 4304 */       for (Item pileItem : this.vitems.getPileItems()) {
/*      */         
/* 4306 */         if (pileItem != null)
/*      */         {
/* 4308 */           if (!aRemove && aZone.covers(this.tilex, this.tiley)) {
/*      */             
/* 4310 */             if (aZone.isVisible(pileItem, this)) {
/*      */               
/* 4312 */               boolean onGroundLevel = true;
/* 4313 */               if (pileItem.getFloorLevel() > 0) {
/* 4314 */                 onGroundLevel = false;
/*      */               
/*      */               }
/* 4317 */               else if ((getFloors(0, 0)).length > 0) {
/* 4318 */                 onGroundLevel = false;
/*      */               } 
/* 4320 */               aZone.addItem(pileItem, this, onGroundLevel);
/*      */             } else {
/*      */               
/* 4323 */               aZone.removeItem(pileItem);
/*      */             } 
/*      */           } else {
/* 4326 */             aZone.removeItem(pileItem);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public byte getKingdom() {
/* 4334 */     return Zones.getKingdom(this.tilex, this.tiley);
/*      */   }
/*      */ 
/*      */   
/*      */   private void linkItemsToZone(VirtualZone aZone, boolean aRemove) {
/* 4339 */     if (this.vitems != null)
/*      */     {
/* 4341 */       if (!aRemove && aZone.covers(this.tilex, this.tiley)) {
/*      */         
/* 4343 */         Item[] lTempItemsLink = this.vitems.getAllItemsAsArray();
/* 4344 */         for (int x = 0; x < lTempItemsLink.length; x++) {
/*      */           
/* 4346 */           Item pileItem = this.vitems.getPileItem(lTempItemsLink[x].getFloorLevel());
/* 4347 */           if (pileItem == null || lTempItemsLink[x].isDecoration())
/*      */           {
/* 4349 */             if (aZone.isVisible(lTempItemsLink[x], this)) {
/*      */               
/* 4351 */               boolean onGroundLevel = true;
/* 4352 */               if (lTempItemsLink[x].getFloorLevel() > 0) {
/* 4353 */                 onGroundLevel = false;
/*      */               
/*      */               }
/* 4356 */               else if ((getFloors(0, 0)).length > 0) {
/* 4357 */                 onGroundLevel = false;
/*      */               } 
/* 4359 */               if (!aZone.addItem(lTempItemsLink[x], this, onGroundLevel)) {
/*      */                 
/*      */                 try {
/*      */                   
/* 4363 */                   Items.getItem(lTempItemsLink[x].getWurmId());
/* 4364 */                   removeItem(lTempItemsLink[x], false);
/* 4365 */                   Zone z = Zones.getZone(lTempItemsLink[x].getTileX(), lTempItemsLink[x].getTileY(), 
/* 4366 */                       isOnSurface());
/* 4367 */                   z.addItem(lTempItemsLink[x]);
/* 4368 */                   logger.log(Level.INFO, this.tilex + ", " + this.tiley + " removing " + lTempItemsLink[x].getName() + " with id " + lTempItemsLink[x]
/* 4369 */                       .getWurmId() + " and added it to " + lTempItemsLink[x]
/* 4370 */                       .getTileX() + "," + lTempItemsLink[x].getTileY() + " where it belongs.");
/*      */                 
/*      */                 }
/* 4373 */                 catch (NoSuchItemException nsi) {
/*      */                   
/* 4375 */                   logger.log(Level.INFO, this.tilex + ", " + this.tiley + " removing " + lTempItemsLink[x].getName() + " with id " + lTempItemsLink[x]
/* 4376 */                       .getWurmId() + " since it doesn't belong here.");
/* 4377 */                   removeItem(lTempItemsLink[x], false);
/*      */                 }
/* 4379 */                 catch (NoSuchZoneException nsz) {
/*      */                   
/* 4381 */                   logger.log(Level.INFO, this.tilex + ", " + this.tiley + " removed " + lTempItemsLink[x].getName() + " with id " + lTempItemsLink[x]
/* 4382 */                       .getWurmId() + ". It is in no valid zone.");
/*      */                 } 
/*      */               }
/*      */             } else {
/*      */               
/* 4387 */               aZone.removeItem(lTempItemsLink[x]);
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } else {
/*      */         
/* 4393 */         for (Item item : this.vitems.getAllItemsAsSet()) {
/*      */           
/* 4395 */           if (item.getSizeZ() < 500) {
/* 4396 */             aZone.removeItem(item);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private void linkEffectsToZone(VirtualZone aZone, boolean aRemove) {
/* 4404 */     if (this.effects != null)
/*      */     {
/* 4406 */       if (!aRemove && aZone.covers(this.tilex, this.tiley)) {
/*      */         
/* 4408 */         for (Iterator<Effect> it = this.effects.iterator(); it.hasNext(); )
/*      */         {
/* 4410 */           Effect effect = it.next();
/* 4411 */           aZone.addEffect(effect, false);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 4416 */         for (Iterator<Effect> it = this.effects.iterator(); it.hasNext(); ) {
/*      */           
/* 4418 */           Effect effect = it.next();
/* 4419 */           aZone.removeEffect(effect);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void linkStructureToZone(VirtualZone aZone, boolean aRemove) {
/* 4427 */     if (this.structure != null) {
/*      */       
/* 4429 */       if (logger.isLoggable(Level.FINEST))
/*      */       {
/* 4431 */         logger.log(Level.INFO, "linkStructureToZone: " + this.structure.getWurmId() + " " + aZone.getId());
/*      */       }
/* 4433 */       if (!aRemove) {
/* 4434 */         aZone.addStructure(this.structure);
/*      */       } else {
/* 4436 */         aZone.removeStructure(this.structure);
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
/*      */   private boolean checkDeletion() {
/* 4471 */     if (this.creatures == null || this.creatures.size() == 0)
/*      */     {
/* 4473 */       if (this.vitems == null || this.vitems.isEmpty())
/*      */       {
/* 4475 */         if (this.walls == null || this.walls.size() == 0)
/*      */         {
/* 4477 */           if (this.structure == null)
/*      */           {
/* 4479 */             if (this.fences == null)
/*      */             {
/* 4481 */               if (this.doors == null || this.doors.size() == 0)
/*      */               {
/* 4483 */                 if (this.effects == null || this.effects.size() == 0)
/*      */                 {
/* 4485 */                   if (this.floors == null || this.floors.size() == 0)
/*      */                   {
/* 4487 */                     if (this.mineDoors == null || this.mineDoors.size() == 0) {
/*      */                       
/* 4489 */                       this.zone.removeTile(this);
/* 4490 */                       return true;
/*      */                     } 
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 4500 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void changeStructureName(String newName) {
/* 4505 */     if (this.watchers != null && this.structure != null)
/*      */     {
/* 4507 */       for (Iterator<VirtualZone> it = this.watchers.iterator(); it.hasNext(); ) {
/*      */         
/* 4509 */         VirtualZone vzone = it.next();
/* 4510 */         vzone.changeStructureName(this.structure.getWurmId(), newName);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final Item createPileItem(Item posItem, boolean starting) {
/*      */     try {
/* 4519 */       Item pileItem = ItemFactory.createItem(177, 60.0F, null);
/*      */       
/* 4521 */       float newXPos = ((this.tilex << 2) + 1) + Server.rand.nextFloat() * 2.0F;
/* 4522 */       float newYPos = ((this.tiley << 2) + 1) + Server.rand.nextFloat() * 2.0F;
/*      */       
/* 4524 */       float height = posItem.getPosZ();
/* 4525 */       if (Server.getSecondsUptime() > 0) {
/* 4526 */         height = Zones.calculatePosZ(newXPos, newYPos, this, isOnSurface(), false, posItem
/* 4527 */             .getPosZ(), null, posItem.onBridge());
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 4532 */       pileItem.setPos(newXPos, newYPos, height, posItem
/* 4533 */           .getRotation(), posItem.getBridgeId());
/*      */       
/* 4535 */       pileItem.setZoneId(this.zone.getId(), this.surfaced);
/* 4536 */       int data = posItem.getTemplateId();
/* 4537 */       pileItem.setData1(data);
/* 4538 */       byte material = 0;
/* 4539 */       boolean multipleMaterials = false;
/*      */ 
/*      */ 
/*      */       
/* 4543 */       if (this.vitems != null)
/*      */       {
/* 4545 */         for (Item item : this.vitems.getAllItemsAsArray()) {
/*      */           
/* 4547 */           if (!item.isDecoration() && item.getFloorLevel() == pileItem.getFloorLevel()) {
/*      */             
/* 4549 */             if (!starting)
/* 4550 */               sendRemoveItem(item, false); 
/* 4551 */             if (!multipleMaterials)
/*      */             {
/*      */ 
/*      */               
/* 4555 */               if (item.getMaterial() != material)
/*      */               {
/* 4557 */                 if (material == 0) {
/*      */ 
/*      */ 
/*      */                   
/* 4561 */                   material = item.getMaterial();
/*      */ 
/*      */ 
/*      */                 
/*      */                 }
/*      */                 else {
/*      */ 
/*      */ 
/*      */                   
/* 4570 */                   material = 0;
/* 4571 */                   multipleMaterials = true;
/*      */                 } 
/*      */               }
/*      */             }
/* 4575 */             if (!item.equals(posItem))
/* 4576 */               pileItem.insertItem(item, true); 
/* 4577 */             if (data != -1 && item.getTemplateId() != data) {
/*      */               
/* 4579 */               pileItem.setData1(-1);
/* 4580 */               data = -1;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/* 4585 */       String name = pileItem.getName();
/* 4586 */       String modelname = pileItem.getModelName();
/* 4587 */       if (data != -1) {
/*      */         
/* 4589 */         ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(data);
/* 4590 */         String tname = template.getName();
/* 4591 */         name = "Pile of " + template.sizeString + tname;
/* 4592 */         if (material == 0) {
/* 4593 */           pileItem.setMaterial(template.getMaterial());
/*      */         } else {
/* 4595 */           pileItem.setMaterial(material);
/* 4596 */         }  StringBuilder build = new StringBuilder();
/* 4597 */         build.append(pileItem.getTemplate().getModelName());
/* 4598 */         build.append(tname);
/* 4599 */         build.append(".");
/* 4600 */         build.append(MaterialUtilities.getMaterialString(material));
/* 4601 */         modelname = build.toString().replaceAll(" ", "").trim();
/* 4602 */         pileItem.setName(name);
/*      */       } 
/*      */ 
/*      */       
/* 4606 */       if (!starting && this.watchers != null)
/*      */       {
/* 4608 */         for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */           
/*      */           try {
/* 4612 */             if (vz.isVisible(pileItem, this)) {
/*      */               
/* 4614 */               boolean onGroundLevel = true;
/* 4615 */               if (pileItem.getFloorLevel() > 0) {
/* 4616 */                 onGroundLevel = false;
/*      */               
/*      */               }
/* 4619 */               else if ((getFloors(0, 0)).length > 0) {
/* 4620 */                 onGroundLevel = false;
/*      */               } 
/* 4622 */               vz.addItem(pileItem, this, onGroundLevel);
/* 4623 */               if (data != -1) {
/* 4624 */                 vz.renameItem(pileItem, name, modelname);
/*      */               }
/*      */             } 
/* 4627 */           } catch (Exception e) {
/*      */             
/* 4629 */             logger.log(Level.WARNING, e.getMessage(), e);
/*      */           } 
/*      */         } 
/*      */       }
/* 4633 */       return pileItem;
/*      */     }
/* 4635 */     catch (FailedException fe) {
/*      */       
/* 4637 */       logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*      */     }
/* 4639 */     catch (NoSuchTemplateException nst) {
/*      */       
/* 4641 */       logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */     } 
/* 4643 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public void renameItem(Item item) {
/* 4648 */     if (this.watchers != null)
/*      */     {
/* 4650 */       for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */         
/*      */         try {
/* 4654 */           if (vz.isVisible(item, this))
/*      */           {
/* 4656 */             vz.renameItem(item, item.getName(), item.getModelName());
/*      */           }
/*      */         }
/* 4659 */         catch (Exception e) {
/*      */           
/* 4661 */           logger.log(Level.WARNING, e.getMessage(), e);
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
/*      */   
/*      */   public void putRandomOnTile(Item item) {
/* 4677 */     float newPosX = (this.tilex << 2) + 0.5F + Server.rand.nextFloat() * 3.0F;
/* 4678 */     float newPosY = (this.tiley << 2) + 0.5F + Server.rand.nextFloat() * 3.0F;
/* 4679 */     item.setPosXY(newPosX, newPosY);
/*      */   }
/*      */ 
/*      */   
/*      */   private void destroyPileItem(int floorLevel) {
/* 4684 */     if (this.vitems != null) {
/*      */       
/* 4686 */       Item pileItem = this.vitems.getPileItem(floorLevel);
/* 4687 */       destroyPileItem(pileItem);
/* 4688 */       if (floorLevel == 0) {
/* 4689 */         this.vitems.removePileItem(floorLevel);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private final void destroyPileItem(Item pileItem) {
/* 4695 */     if (pileItem != null) {
/*      */ 
/*      */       
/*      */       try {
/* 4699 */         Creature[] iwatchers = pileItem.getWatchers();
/* 4700 */         for (int x = 0; x < iwatchers.length; x++) {
/* 4701 */           iwatchers[x].getCommunicator().sendCloseInventoryWindow(pileItem.getWurmId());
/*      */         }
/* 4703 */       } catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4709 */       if (this.vitems != null) {
/*      */         
/* 4711 */         Item[] itemarra = this.vitems.getAllItemsAsArray();
/* 4712 */         for (int x = 0; x < itemarra.length; x++) {
/*      */           
/* 4714 */           if (!itemarra[x].isDecoration() && 
/* 4715 */             itemarra[x].getFloorLevel() == pileItem.getFloorLevel()) {
/* 4716 */             this.vitems.removeItem(itemarra[x]);
/*      */           }
/*      */         } 
/* 4719 */         Item p = this.vitems.getPileItem(pileItem.getFloorLevel());
/* 4720 */         if (p != null && p != pileItem)
/*      */         {
/* 4722 */           Items.destroyItem(p.getWurmId());
/*      */         }
/*      */       } 
/* 4725 */       Items.destroyItem(pileItem.getWurmId());
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
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 4742 */     int result = this.tilex + 1;
/* 4743 */     result += Zones.worldTileSizeY * (this.tiley + 1);
/* 4744 */     return result * (this.surfaced ? 1 : 2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int generateHashCode(int _tilex, int _tiley, boolean _surfaced) {
/* 4755 */     int result = _tilex + 1;
/* 4756 */     result += (_tiley + 1) * Zones.worldTileSizeY;
/* 4757 */     return result * (_surfaced ? 1 : 2);
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
/*      */   public boolean equals(Object object) {
/* 4770 */     if (this == object)
/*      */     {
/* 4772 */       return true;
/*      */     }
/* 4774 */     if (object != null && object.getClass() == getClass()) {
/*      */       
/* 4776 */       VolaTile tile = (VolaTile)object;
/* 4777 */       return (tile.getTileX() == this.tilex && tile.getTileY() == this.tiley && tile.surfaced == this.surfaced);
/*      */     } 
/* 4779 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isGuarded() {
/* 4784 */     if (this.village != null)
/* 4785 */       return (this.village.guards.size() > 0); 
/* 4786 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasFire() {
/* 4796 */     if (this.vitems == null)
/* 4797 */       return false; 
/* 4798 */     return this.vitems.hasFire();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Zone getZone() {
/* 4808 */     return this.zone;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isInactive() {
/* 4818 */     return this.inactive;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setInactive(boolean aInactive) {
/* 4829 */     this.inactive = aInactive;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTransition() {
/* 4839 */     return this.isTransition;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean hasOnePerTileItem(int floorLevel) {
/* 4849 */     return (this.vitems != null && this.vitems.hasOnePerTileItem(floorLevel));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getFourPerTileCount(int floorLevel) {
/* 4859 */     if (this.vitems == null)
/* 4860 */       return 0; 
/* 4861 */     return this.vitems.getFourPerTileCount(floorLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Item getOnePerTileItem(int floorLevel) {
/* 4872 */     if (this.vitems == null)
/* 4873 */       return null; 
/* 4874 */     return this.vitems.getOnePerTileItem(floorLevel);
/*      */   }
/*      */ 
/*      */   
/*      */   void lightningStrikeSpell(float baseDamage, Creature caster) {
/* 4879 */     if (this.structure == null || !this.structure.isFinished()) {
/*      */       
/* 4881 */       if (this.creatures != null) {
/*      */         
/* 4883 */         Creature[] crets = getCreatures();
/* 4884 */         for (int c = 0; c < crets.length; c++) {
/*      */ 
/*      */ 
/*      */           
/* 4888 */           Wound wound = null;
/* 4889 */           if (!crets[c].isPlayer()) {
/* 4890 */             TempWound tempWound = new TempWound((byte)4, (byte)1, baseDamage, crets[c].getWurmId(), 0.0F, 0.0F, true);
/*      */           } else {
/*      */             
/* 4893 */             if (Servers.localServer.PVPSERVER) {
/*      */               
/* 4895 */               float mod = 1.0F;
/*      */               
/*      */               try {
/* 4898 */                 Item armour = crets[c].getArmour((byte)1);
/* 4899 */                 if (armour != null)
/*      */                 {
/* 4901 */                   if (armour.isMetal()) {
/* 4902 */                     mod = 2.0F;
/* 4903 */                   } else if (armour.isLeather() || armour.isCloth()) {
/* 4904 */                     mod = 0.5F;
/* 4905 */                   }  armour.setDamage(armour.getDamage() + armour.getDamageModifier());
/*      */                 }
/*      */               
/* 4908 */               } catch (NoArmourException noArmourException) {
/*      */ 
/*      */               
/*      */               }
/* 4912 */               catch (NoSpaceException nsp) {
/*      */                 
/* 4914 */                 logger.log(Level.WARNING, crets[c].getName() + " no armour space on loc " + '\001');
/*      */               } 
/*      */               
/* 4917 */               crets[c].getCommunicator().sendAlertServerMessage("YOU ARE HIT BY LIGHTNING! OUCH!");
/* 4918 */               if (Servers.isThisATestServer())
/* 4919 */                 crets[c].getCommunicator().sendNormalServerMessage("Lightning damage mod: " + mod); 
/* 4920 */               crets[c].addWoundOfType(null, (byte)4, 1, false, 1.0F, false, (baseDamage * mod), 0.0F, 0.0F, false, true);
/*      */             } 
/*      */             
/* 4923 */             crets[c].addAttacker(caster);
/*      */           } 
/*      */         } 
/*      */       } 
/* 4927 */       if (Servers.localServer.PVPSERVER)
/*      */       {
/* 4929 */         if (this.vitems != null) {
/*      */           
/* 4931 */           Item[] ttempItems = this.vitems.getAllItemsAsArray();
/* 4932 */           for (int x = 0; x < ttempItems.length; x++) {
/*      */             
/* 4934 */             if (!ttempItems[x].isIndestructible() && !ttempItems[x].isHugeAltar()) {
/* 4935 */               ttempItems[x]
/* 4936 */                 .setDamage(ttempItems[x].getDamage() + ttempItems[x]
/* 4937 */                   .getDamageModifier() * ((ttempItems[x]
/* 4938 */                   .isLocked() || ttempItems[x].isDecoration() || ttempItems[x]
/* 4939 */                   .isMetal() || ttempItems[x]
/* 4940 */                   .isStone()) ? 0.1F : 10.0F));
/*      */             }
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   void flashStrike() {
/* 4949 */     if (this.structure == null || !this.structure.isFinished()) {
/*      */       
/* 4951 */       if (this.creatures != null) {
/*      */         
/* 4953 */         Creature[] crets = getCreatures();
/* 4954 */         for (int c = 0; c < crets.length; c++) {
/*      */ 
/*      */ 
/*      */           
/* 4958 */           Wound wound = null;
/* 4959 */           if (!crets[c].isPlayer()) {
/* 4960 */             TempWound tempWound = new TempWound((byte)4, (byte)1, 10000.0F, crets[c].getWurmId(), 0.0F, 0.0F, false);
/*      */           } else {
/*      */             
/* 4963 */             float mod = 1.0F;
/*      */             
/*      */             try {
/* 4966 */               Item armour = crets[c].getArmour((byte)1);
/* 4967 */               if (armour != null) {
/*      */                 
/* 4969 */                 if (armour.isMetal()) {
/* 4970 */                   mod = 2.0F;
/* 4971 */                 } else if (armour.isLeather() || armour.isCloth()) {
/* 4972 */                   mod = 0.5F;
/* 4973 */                 }  armour.setDamage(armour.getDamage() + armour.getDamageModifier() * 10.0F);
/*      */               } 
/* 4975 */               Item[] lItems = crets[c].getBody().getContainersAndWornItems();
/* 4976 */               for (int x = 0; x < lItems.length; x++) {
/*      */                 
/* 4978 */                 if ((lItems[x].isArmour() || lItems[x].isWeapon()) && lItems[x].isMetal())
/*      */                 {
/* 4980 */                   mod += 0.1F;
/* 4981 */                   lItems[x].setDamage(lItems[x].getDamage() + lItems[x].getDamageModifier() * 10.0F);
/*      */                 }
/*      */               
/*      */               } 
/* 4985 */             } catch (NoArmourException noArmourException) {
/*      */ 
/*      */             
/*      */             }
/* 4989 */             catch (NoSpaceException nsp) {
/*      */               
/* 4991 */               logger.log(Level.WARNING, crets[c].getName() + " no armour space on loc " + '\001');
/*      */             } 
/*      */             
/* 4994 */             crets[c].getCommunicator().sendAlertServerMessage("YOU ARE HIT BY LIGHTNING! OUCH!");
/*      */             
/* 4996 */             crets[c].addWoundOfType(null, (byte)4, 1, false, 1.0F, false, (3000.0F * mod), 0.0F, 0.0F, false, false);
/* 4997 */             HistoryManager.addHistory(crets[c].getName(), "was hit by lightning!");
/* 4998 */             if (logger.isLoggable(Level.FINER))
/*      */             {
/* 5000 */               logger.finer(crets[c].getName() + " was hit by lightning!");
/*      */             }
/* 5002 */             Skills skills = crets[c].getSkills();
/* 5003 */             Skill mindspeed = null;
/*      */             
/*      */             try {
/* 5006 */               mindspeed = skills.getSkill(101);
/* 5007 */               double knowl = mindspeed.getKnowledge();
/* 5008 */               mindspeed.setKnowledge(knowl + (1.0F * mod), false);
/*      */             }
/* 5010 */             catch (NoSuchSkillException nss) {
/*      */               
/* 5012 */               mindspeed = skills.learn(101, 21.0F);
/*      */             } 
/* 5014 */             crets[c].getCommunicator().sendNormalServerMessage("A strange dizziness runs through your head, eventually sharpening your senses.");
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 5019 */       if (this.vitems != null) {
/*      */         
/* 5021 */         Item[] ttempItems = this.vitems.getAllItemsAsArray();
/* 5022 */         for (int x = 0; x < ttempItems.length; x++)
/*      */         {
/* 5024 */           ttempItems[x].setDamage(ttempItems[x].getDamage() + ttempItems[x].getDamageModifier() * 10.0F);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void moveItem(Item item, float newPosX, float newPosY, float newPosZ, float newRot, boolean surf, float oldPosZ) {
/* 5033 */     float diffX = newPosX - item.getPosX();
/* 5034 */     float diffY = newPosY - item.getPosY();
/* 5035 */     if (diffX != 0.0F || diffY != 0.0F) {
/*      */       
/* 5037 */       int newTileX = (int)newPosX >> 2;
/* 5038 */       int newTileY = (int)newPosY >> 2;
/* 5039 */       long newBridgeId = item.getBridgeId();
/* 5040 */       long oldBridgeId = item.getBridgeId();
/*      */       
/* 5042 */       if (newTileX != this.tilex || newTileY != this.tiley || surf != isOnSurface()) {
/*      */         
/* 5044 */         VolaTile dt = Zones.getTileOrNull(Zones.safeTileX(newTileX), Zones.safeTileY(newTileY), surf);
/* 5045 */         if (item.onBridge() == -10L && dt != null && dt.getStructure() != null && dt.getStructure().isTypeBridge()) {
/*      */ 
/*      */           
/* 5048 */           if (item.getBridgeId() == -10L) {
/*      */ 
/*      */             
/* 5051 */             BridgePart bp = Zones.getBridgePartFor(newTileX, newTileY, surf);
/* 5052 */             if (bp != null && bp.isFinished() && bp.hasAnExit())
/*      */             {
/* 5054 */               if (Servers.isThisATestServer() && item.isWagonerWagon())
/*      */               {
/*      */                 
/* 5057 */                 Players.getInstance().sendGmMessage(null, "System", "Debug: Wagon " + item.getName() + " bid:" + oldBridgeId + " z:" + item
/* 5058 */                     .getPosZ() + " fl:" + item.getFloorLevel() + " bp:" + bp
/* 5059 */                     .getStructureId() + " N:" + bp.getNorthExit() + " E:" + bp.getEastExit() + " S:" + bp
/* 5060 */                     .getSouthExit() + " W:" + bp.getWestExit() + " @" + item
/* 5061 */                     .getTileX() + "," + item.getTileY() + " to " + newTileX + "," + newTileY + "," + surf, false);
/*      */               }
/*      */ 
/*      */ 
/*      */               
/* 5066 */               if (newTileY < item.getTileY() && bp.getSouthExitFloorLevel() == item.getFloorLevel()) {
/* 5067 */                 newBridgeId = bp.getStructureId();
/*      */               }
/* 5069 */               else if (newTileX > item.getTileX() && bp.getWestExitFloorLevel() == item.getFloorLevel()) {
/* 5070 */                 newBridgeId = bp.getStructureId();
/*      */               }
/* 5072 */               else if (newTileY > item.getTileY() && bp.getNorthExitFloorLevel() == item.getFloorLevel()) {
/* 5073 */                 newBridgeId = bp.getStructureId();
/*      */               }
/* 5075 */               else if (newTileX < item.getTileX() && bp.getEastExitFloorLevel() == item.getFloorLevel()) {
/* 5076 */                 newBridgeId = bp.getStructureId();
/*      */               } 
/* 5078 */               if (Servers.isThisATestServer() && newBridgeId != oldBridgeId)
/*      */               {
/* 5080 */                 Players.getInstance().sendGmMessage(null, "System", "Debug: Wagon " + item.getName() + " obid:" + oldBridgeId + " z:" + item
/* 5081 */                     .getPosZ() + " fl:" + item.getFloorLevel() + " nbid:" + newBridgeId + " N:" + bp
/* 5082 */                     .getNorthExit() + " E:" + bp.getEastExit() + " S:" + bp.getSouthExit() + " W:" + bp.getWestExit() + " @" + item
/* 5083 */                     .getTileX() + "," + item.getTileY() + " to " + newTileX + "," + newTileY + "," + surf, false);
/*      */               
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/* 5089 */               newBridgeId = -10L;
/* 5090 */               item.setOnBridge(-10L);
/* 5091 */               sendSetBridgeId(item, -10L);
/* 5092 */               item.calculatePosZ(dt, null);
/*      */             }
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 5098 */             BridgePart bp = Zones.getBridgePartFor(newTileX, newTileY, surf);
/* 5099 */             if (bp == null) {
/*      */ 
/*      */               
/* 5102 */               newBridgeId = -10L;
/* 5103 */               item.setOnBridge(-10L);
/* 5104 */               sendSetBridgeId(item, -10L);
/* 5105 */               item.calculatePosZ(dt, null);
/*      */             } 
/*      */           } 
/*      */           
/* 5109 */           if (item.onBridge() != newBridgeId) {
/*      */             
/* 5111 */             float nz = Zones.calculatePosZ(newPosX, newPosY, dt, isOnSurface(), false, oldPosZ, null, newBridgeId);
/*      */ 
/*      */             
/* 5114 */             if (Servers.isThisATestServer() && item.isWagonerWagon()) {
/* 5115 */               Players.getInstance().sendGmMessage(null, "System", "Debug: Wagon " + item.getName() + " moving onto, or off, a bridge from bid:" + oldBridgeId + " z:" + item
/*      */                   
/* 5117 */                   .getPosZ() + " fl:" + item.getFloorLevel() + " to bp:" + newBridgeId + " newZ:" + nz + " @" + item
/*      */                   
/* 5119 */                   .getTileX() + "," + item.getTileY() + " to " + newTileX + "," + newTileY + "," + surf, false);
/*      */             }
/*      */             
/* 5122 */             if (Math.abs(oldPosZ - nz) < 10.0F)
/*      */             {
/*      */               
/* 5125 */               if (!item.isBoat())
/*      */               {
/* 5127 */                 item.setOnBridge(newBridgeId);
/*      */                 
/* 5129 */                 newPosZ = nz;
/*      */                 
/* 5131 */                 sendSetBridgeId(item, newBridgeId);
/*      */               }
/*      */             
/*      */             }
/*      */           } 
/* 5136 */         } else if (item.onBridge() > 0L && (dt == null || dt
/* 5137 */           .getStructure() == null || dt.getStructure().getWurmId() != item.onBridge())) {
/*      */           
/* 5139 */           boolean leave = true;
/* 5140 */           BridgePart bp = Zones.getBridgePartFor(newTileX, newTileY, surf);
/* 5141 */           if (bp != null && bp.isFinished())
/*      */           {
/*      */             
/* 5144 */             if (bp.getDir() == 0 || bp.getDir() == 4) {
/*      */               
/* 5146 */               if (getTileX() != newTileX)
/*      */               {
/* 5148 */                 leave = false;
/*      */               
/*      */               }
/*      */             
/*      */             }
/* 5153 */             else if (getTileY() != newTileY) {
/*      */               
/* 5155 */               leave = false;
/*      */             } 
/*      */           }
/*      */           
/* 5159 */           if (leave) {
/*      */             
/* 5161 */             newBridgeId = -10L;
/* 5162 */             item.setOnBridge(-10L);
/* 5163 */             sendSetBridgeId(item, -10L);
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 5174 */         if (surf != isOnSurface())
/*      */         {
/* 5176 */           item.newLayer = (byte)(isOnSurface() ? -1 : 0);
/*      */         }
/* 5178 */         removeItem(item, true);
/*      */         
/* 5180 */         if (diffX != 0.0F && diffY != 0.0F) {
/*      */           
/* 5182 */           item.setPosXYZRotation(newPosX, newPosY, newPosZ, newRot);
/*      */         }
/*      */         else {
/*      */           
/* 5186 */           item.setRotation(newRot);
/* 5187 */           if (diffX != 0.0F)
/* 5188 */             item.setPosX(newPosX); 
/* 5189 */           if (diffY != 0.0F)
/* 5190 */             item.setPosY(newPosY); 
/* 5191 */           item.setPosZ(newPosZ);
/*      */         } 
/*      */         
/*      */         try {
/* 5195 */           Zone _zone = Zones.getZone((int)newPosX >> 2, (int)newPosY >> 2, surf);
/* 5196 */           _zone.addItem(item, true, (surf != isOnSurface()), false);
/*      */         }
/* 5198 */         catch (NoSuchZoneException nsz) {
/*      */           
/* 5200 */           logger.log(Level.WARNING, item.getName() + ", " + nsz.getMessage(), (Throwable)nsz);
/*      */         } 
/* 5202 */         if (surf != isOnSurface()) {
/* 5203 */           item.newLayer = Byte.MIN_VALUE;
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */ 
/*      */         
/* 5216 */         if (diffX != 0.0F)
/* 5217 */           item.setTempXPosition(newPosX); 
/* 5218 */         if (diffY != 0.0F)
/* 5219 */           item.setTempYPosition(newPosY); 
/* 5220 */         item.setTempZandRot(newPosZ, newRot);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 5225 */       for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */         
/*      */         try {
/* 5229 */           if (vz.isVisible(item, this)) {
/*      */             
/* 5231 */             if (item.getFloorLevel() <= 0 && item.onBridge() <= 0L) {
/*      */               
/* 5233 */               if (Structure.isGroundFloorAtPosition(newPosX, newPosY, item.isOnSurface()))
/*      */               {
/* 5235 */                 vz.sendMoveMovingItemAndSetZ(item.getWurmId(), item.getPosX(), item.getPosY(), item
/* 5236 */                     .getPosZ(), (int)(newRot * 256.0F / 360.0F));
/*      */               }
/*      */               else
/*      */               {
/* 5240 */                 vz.sendMoveMovingItem(item.getWurmId(), item.getPosX(), item.getPosY(), (int)(newRot * 256.0F / 360.0F));
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/* 5245 */               vz.sendMoveMovingItemAndSetZ(item.getWurmId(), item.getPosX(), item.getPosY(), item
/* 5246 */                   .getPosZ(), (int)(newRot * 256.0F / 360.0F));
/*      */             } 
/*      */           } else {
/* 5249 */             vz.removeItem(item);
/*      */           } 
/* 5251 */         } catch (Exception e) {
/*      */           
/* 5253 */           logger.log(Level.WARNING, e.getMessage(), e);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void destroyEverything() {
/* 5261 */     Creature[] crets = getCreatures();
/* 5262 */     for (int x = 0; x < crets.length; x++) {
/*      */       
/* 5264 */       crets[x].getCommunicator().sendNormalServerMessage("The rock suddenly caves in! You are crushed!");
/* 5265 */       crets[x].die(true, "Cave collapse");
/*      */     } 
/* 5267 */     Fence[] fenceArr = getFences();
/* 5268 */     for (int i = 0; i < fenceArr.length; i++) {
/*      */       
/* 5270 */       if (fenceArr[i] != null)
/* 5271 */         fenceArr[i].destroy(); 
/*      */     } 
/* 5273 */     Wall[] wallArr = getWalls();
/* 5274 */     for (int j = 0; j < wallArr.length; j++) {
/*      */       
/* 5276 */       if (wallArr[j] != null)
/* 5277 */         wallArr[j].destroy(); 
/*      */     } 
/* 5279 */     Floor[] floorArr = getFloors();
/* 5280 */     for (int k = 0; k < floorArr.length; k++) {
/*      */       
/* 5282 */       if (floorArr[k] != null)
/* 5283 */         floorArr[k].destroy(); 
/*      */     } 
/* 5285 */     Item[] ttempItems = getItems();
/* 5286 */     for (int m = 0; m < ttempItems.length; m++) {
/* 5287 */       Items.destroyItem(ttempItems[m].getWurmId());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void sendNewLayerToWatchers(Item item) {
/* 5295 */     logger.log(Level.INFO, "Tile at " + this.tilex + ", " + this.tiley + " sending secondary");
/* 5296 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5300 */         vz.justSendNewLayer(item);
/*      */       }
/* 5302 */       catch (Exception e) {
/*      */         
/* 5304 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void newLayer(Item item) {
/* 5311 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5315 */         vz.newLayer(item);
/*      */       }
/* 5317 */       catch (Exception e) {
/*      */         
/* 5319 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/* 5322 */     if (!isOnSurface())
/*      */     {
/* 5324 */       for (VirtualZone vz : getSurfaceTile().getWatchers()) {
/*      */ 
/*      */         
/*      */         try {
/* 5328 */           vz.addItem(item, getSurfaceTile(), true);
/*      */         }
/* 5330 */         catch (Exception e) {
/*      */           
/* 5332 */           logger.log(Level.WARNING, e.getMessage(), e);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void newLayer(Creature creature) {
/* 5340 */     if (creature.isOnSurface() != isOnSurface())
/*      */     {
/* 5342 */       for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */         
/*      */         try {
/* 5346 */           vz.newLayer(creature, isOnSurface());
/*      */         }
/* 5348 */         catch (Exception e) {
/*      */           
/* 5350 */           logger.log(Level.WARNING, e.getMessage(), e);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     try {
/* 5356 */       Zone newzone = Zones.getZone(this.tilex, this.tiley, (creature.getLayer() >= 0));
/* 5357 */       VolaTile currentTile = newzone.getOrCreateTile(this.tilex, this.tiley);
/* 5358 */       removeCreature(creature);
/* 5359 */       currentTile.addCreature(creature, 0.0F);
/*      */     }
/* 5361 */     catch (NoSuchZoneException noSuchZoneException) {
/*      */ 
/*      */     
/* 5364 */     } catch (NoSuchPlayerException noSuchPlayerException) {
/*      */ 
/*      */     
/* 5367 */     } catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addLightSource(Item lightSource) {
/* 5374 */     if (lightSource.getTemplateId() == 1243)
/*      */       return; 
/* 5376 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5380 */         if (lightSource.getColor() != -1)
/*      */         {
/* 5382 */           int lightStrength = Math.max(WurmColor.getColorRed(lightSource.getColor()), 
/* 5383 */               WurmColor.getColorGreen(lightSource.getColor()));
/* 5384 */           lightStrength = Math.max(lightStrength, WurmColor.getColorBlue(lightSource.getColor()));
/* 5385 */           if (lightStrength == 0)
/* 5386 */             lightStrength = 1; 
/* 5387 */           byte r = (byte)(WurmColor.getColorRed(lightSource.getColor()) * 128 / lightStrength);
/* 5388 */           byte g = (byte)(WurmColor.getColorGreen(lightSource.getColor()) * 128 / lightStrength);
/* 5389 */           byte b = (byte)(WurmColor.getColorBlue(lightSource.getColor()) * 128 / lightStrength);
/*      */           
/* 5391 */           vz.sendAttachItemEffect(lightSource.getWurmId(), (byte)4, r, g, b, lightSource
/* 5392 */               .getRadius());
/*      */ 
/*      */         
/*      */         }
/* 5396 */         else if (lightSource.isLightBright())
/*      */         {
/* 5398 */           int lightStrength = (int)(80.0F + lightSource.getCurrentQualityLevel() / 100.0F * 40.0F);
/* 5399 */           vz.sendAttachItemEffect(lightSource.getWurmId(), (byte)4, 
/* 5400 */               Item.getRLight(lightStrength), Item.getGLight(lightStrength), Item.getBLight(lightStrength), lightSource
/* 5401 */               .getRadius());
/*      */         }
/*      */         else
/*      */         {
/* 5405 */           vz.sendAttachItemEffect(lightSource.getWurmId(), (byte)4, (byte)80, (byte)80, (byte)80, lightSource
/* 5406 */               .getRadius());
/*      */         }
/*      */       
/*      */       }
/* 5410 */       catch (Exception e) {
/*      */         
/* 5412 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeLightSource(Item lightSource) {
/* 5419 */     if (lightSource.getTemplateId() == 1243)
/*      */       return; 
/* 5421 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5425 */         vz.sendRemoveEffect(lightSource.getWurmId(), (byte)0);
/* 5426 */         vz.sendRemoveEffect(lightSource.getWurmId(), (byte)4);
/*      */       }
/* 5428 */       catch (Exception e) {
/*      */         
/* 5430 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHasLightSource(Creature creature, @Nullable Item lightSource) {
/* 5438 */     if (lightSource != null && lightSource.getTemplateId() == 1243)
/*      */       return; 
/* 5440 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5444 */         if (lightSource == null) {
/*      */           
/* 5446 */           if (vz.getWatcher().getWurmId() == creature.getWurmId()) {
/* 5447 */             vz.sendRemoveEffect(-1L, (byte)0);
/*      */           } else {
/* 5449 */             vz.sendRemoveEffect(creature.getWurmId(), (byte)0);
/*      */           }
/*      */         
/*      */         }
/* 5453 */         else if (vz.getWatcher().getWurmId() == creature.getWurmId()) {
/*      */           
/* 5455 */           if (lightSource.getColor() != -1) {
/*      */             
/* 5457 */             int lightStrength = Math.max(WurmColor.getColorRed(lightSource.color), 
/* 5458 */                 WurmColor.getColorGreen(lightSource.color));
/* 5459 */             lightStrength = Math.max(lightStrength, WurmColor.getColorBlue(lightSource.color));
/* 5460 */             byte r = (byte)(WurmColor.getColorRed(lightSource.color) * 128 / lightStrength);
/* 5461 */             byte g = (byte)(WurmColor.getColorGreen(lightSource.color) * 128 / lightStrength);
/* 5462 */             byte b = (byte)(WurmColor.getColorBlue(lightSource.color) * 128 / lightStrength);
/*      */             
/* 5464 */             vz.sendAttachCreatureEffect(null, (byte)0, r, g, b, lightSource
/* 5465 */                 .getRadius());
/*      */ 
/*      */           
/*      */           }
/* 5469 */           else if (lightSource.isLightBright()) {
/*      */             
/* 5471 */             int lightStrength = (int)(80.0F + lightSource.getCurrentQualityLevel() / 100.0F * 40.0F);
/* 5472 */             vz.sendAttachCreatureEffect(null, (byte)0, 
/* 5473 */                 Item.getRLight(lightStrength), Item.getGLight(lightStrength), 
/* 5474 */                 Item.getBLight(lightStrength), lightSource.getRadius());
/*      */           } else {
/*      */             
/* 5477 */             vz.sendAttachCreatureEffect(null, (byte)0, 
/* 5478 */                 Item.getRLight(80), Item.getGLight(80), Item.getBLight(80), lightSource.getRadius());
/*      */           
/*      */           }
/*      */         
/*      */         }
/* 5483 */         else if (lightSource.getColor() != -1) {
/*      */           
/* 5485 */           int lightStrength = Math.max(WurmColor.getColorRed(lightSource.color), 
/* 5486 */               WurmColor.getColorGreen(lightSource.color));
/* 5487 */           lightStrength = Math.max(lightStrength, WurmColor.getColorBlue(lightSource.color));
/* 5488 */           byte r = (byte)(WurmColor.getColorRed(lightSource.color) * 128 / lightStrength);
/* 5489 */           byte g = (byte)(WurmColor.getColorGreen(lightSource.color) * 128 / lightStrength);
/* 5490 */           byte b = (byte)(WurmColor.getColorBlue(lightSource.color) * 128 / lightStrength);
/*      */           
/* 5492 */           vz.sendAttachCreatureEffect(creature, (byte)0, r, g, b, lightSource
/* 5493 */               .getRadius());
/*      */ 
/*      */         
/*      */         }
/* 5497 */         else if (lightSource.isLightBright()) {
/*      */           
/* 5499 */           int lightStrength = (int)(80.0F + lightSource.getCurrentQualityLevel() / 100.0F * 40.0F);
/* 5500 */           vz.sendAttachCreatureEffect(creature, (byte)0, 
/* 5501 */               Item.getRLight(lightStrength), Item.getGLight(lightStrength), 
/* 5502 */               Item.getBLight(lightStrength), lightSource.getRadius());
/*      */         } else {
/*      */           
/* 5505 */           vz.sendAttachCreatureEffect(creature, (byte)0, 
/* 5506 */               Item.getRLight(80), Item.getGLight(80), Item.getBLight(80), lightSource.getRadius());
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 5511 */       catch (Exception e) {
/*      */         
/* 5513 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHasLightSource(Creature creature, byte colorRed, byte colorGreen, byte colorBlue, byte radius) {
/* 5521 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5525 */         if (vz.getWatcher().getWurmId() == creature.getWurmId())
/*      */         {
/* 5527 */           vz.sendAttachCreatureEffect(null, (byte)0, colorRed, colorGreen, colorBlue, radius);
/*      */         
/*      */         }
/*      */         else
/*      */         {
/* 5532 */           vz.sendAttachCreatureEffect(creature, (byte)0, colorRed, colorGreen, colorBlue, radius);
/*      */         }
/*      */       
/*      */       }
/* 5536 */       catch (Exception e) {
/*      */         
/* 5538 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendAttachCreatureEffect(Creature creature, byte effectType, byte data0, byte data1, byte data2, byte radius) {
/* 5546 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5550 */         if (vz.getWatcher().getWurmId() == creature.getWurmId())
/*      */         {
/* 5552 */           vz.sendAttachCreatureEffect(null, effectType, data0, data1, data2, radius);
/*      */         }
/*      */         else
/*      */         {
/* 5556 */           vz.sendAttachCreatureEffect(creature, effectType, data0, data1, data2, radius);
/*      */         }
/*      */       
/* 5559 */       } catch (Exception e) {
/*      */         
/* 5561 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendRemoveCreatureEffect(Creature creature, byte effectType) {
/* 5568 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5572 */         if (vz.getWatcher().getWurmId() == creature.getWurmId())
/*      */         {
/* 5574 */           vz.sendRemoveEffect(-1L, effectType);
/*      */         }
/*      */         else
/*      */         {
/* 5578 */           vz.sendRemoveEffect(creature.getWurmId(), effectType);
/*      */         }
/*      */       
/* 5581 */       } catch (Exception e) {
/*      */         
/* 5583 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendProjectile(long itemid, byte type, String modelName, String name, byte material, float startX, float startY, float startH, float rot, byte layer, float endX, float endY, float endH, long sourceId, long targetId, float projectedSecondsInAir, float actualSecondsInAir) {
/* 5593 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5597 */         if (vz.getWatcher().getWurmId() == targetId) {
/*      */           
/* 5599 */           if (vz.getWatcher().getWurmId() == sourceId) {
/* 5600 */             vz.sendProjectile(itemid, type, modelName, name, material, startX, startY, startH, rot, layer, endX, endY, endH, -1L, -1L, projectedSecondsInAir, actualSecondsInAir);
/*      */           } else {
/*      */             
/* 5603 */             vz.sendProjectile(itemid, type, modelName, name, material, startX, startY, startH, rot, layer, endX, endY, endH, sourceId, -1L, projectedSecondsInAir, actualSecondsInAir);
/*      */           }
/*      */         
/* 5606 */         } else if (vz.getWatcher().getWurmId() == sourceId) {
/*      */           
/* 5608 */           if (vz.getWatcher().getWurmId() == targetId) {
/* 5609 */             vz.sendProjectile(itemid, type, modelName, name, material, startX, startY, startH, rot, layer, endX, endY, endH, -1L, -1L, projectedSecondsInAir, actualSecondsInAir);
/*      */           } else {
/*      */             
/* 5612 */             vz.sendProjectile(itemid, type, modelName, name, material, startX, startY, startH, rot, layer, endX, endY, endH, -1L, targetId, projectedSecondsInAir, actualSecondsInAir);
/*      */           } 
/*      */         } else {
/*      */           
/* 5616 */           vz.sendProjectile(itemid, type, modelName, name, material, startX, startY, startH, rot, layer, endX, endY, endH, sourceId, targetId, projectedSecondsInAir, actualSecondsInAir);
/*      */         }
/*      */       
/* 5619 */       } catch (Exception e) {
/*      */         
/* 5621 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendNewProjectile(long itemid, byte type, String modelName, String name, byte material, Vector3f startingPosition, Vector3f startingVelocity, Vector3f endingPosition, float rotation, boolean surface) {
/* 5629 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5633 */         vz.sendNewProjectile(itemid, type, modelName, name, material, startingPosition, startingVelocity, endingPosition, rotation, surface);
/*      */       }
/* 5635 */       catch (Exception e) {
/*      */         
/* 5637 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendHorseWear(long creatureId, int itemId, byte material, byte slot, byte aux_data) {
/* 5644 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5648 */         vz.sendHorseWear(creatureId, itemId, material, slot, aux_data);
/*      */       }
/* 5650 */       catch (Exception e) {
/*      */         
/* 5652 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendRemoveHorseWear(long creatureId, int itemId, byte slot) {
/* 5659 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5663 */         vz.sendRemoveHorseWear(creatureId, itemId, slot);
/*      */       }
/* 5665 */       catch (Exception e) {
/*      */         
/* 5667 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendBoatAttachment(long itemId, int templateId, byte material, byte slot, byte aux) {
/* 5674 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5678 */         vz.sendBoatAttachment(itemId, templateId, material, slot, aux);
/*      */       }
/* 5680 */       catch (Exception e) {
/*      */         
/* 5682 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendBoatDetachment(long itemId, int templateId, byte slot) {
/* 5689 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5693 */         vz.sendBoatDetachment(itemId, templateId, slot);
/*      */       }
/* 5695 */       catch (Exception e) {
/*      */         
/* 5697 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendWearItem(long creatureId, int itemId, byte bodyPart, int colorRed, int colorGreen, int colorBlue, int secondaryColorRed, int secondaryColorGreen, int secondaryColorBlue, byte material, byte rarity) {
/* 5706 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5710 */         if (vz.getWatcher().getWurmId() == creatureId)
/*      */         {
/* 5712 */           vz.sendWearItem(-1L, itemId, bodyPart, colorRed, colorGreen, colorBlue, secondaryColorRed, secondaryColorGreen, secondaryColorBlue, material, rarity);
/*      */         }
/*      */         else
/*      */         {
/* 5716 */           vz.sendWearItem(creatureId, itemId, bodyPart, colorRed, colorGreen, colorBlue, secondaryColorRed, secondaryColorGreen, secondaryColorBlue, material, rarity);
/*      */         }
/*      */       
/* 5719 */       } catch (Exception e) {
/*      */         
/* 5721 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendRemoveWearItem(long creatureId, byte bodyPart) {
/* 5728 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5732 */         if (vz.getWatcher().getWurmId() == creatureId)
/*      */         {
/* 5734 */           vz.sendRemoveWearItem(-1L, bodyPart);
/*      */         }
/*      */         else
/*      */         {
/* 5738 */           vz.sendRemoveWearItem(creatureId, bodyPart);
/*      */         }
/*      */       
/* 5741 */       } catch (Exception e) {
/*      */         
/* 5743 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendWieldItem(long creatureId, byte slot, String modelname, byte rarity, int colorRed, int colorGreen, int colorBlue, int secondaryColorRed, int secondaryColorGreen, int secondaryColorBlue) {
/* 5751 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5755 */         if (vz.getWatcher().getWurmId() == creatureId)
/*      */         {
/* 5757 */           vz.sendWieldItem(-1L, slot, modelname, rarity, colorRed, colorGreen, colorBlue, secondaryColorRed, secondaryColorGreen, secondaryColorBlue);
/*      */         }
/*      */         else
/*      */         {
/* 5761 */           vz.sendWieldItem(creatureId, slot, modelname, rarity, colorRed, colorGreen, colorBlue, secondaryColorRed, secondaryColorGreen, secondaryColorBlue);
/*      */         }
/*      */       
/* 5764 */       } catch (Exception e) {
/*      */         
/* 5766 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendUseItem(Creature creature, String modelname, byte rarity, int colorRed, int colorGreen, int colorBlue, int secondaryColorRed, int secondaryColorGreen, int secondaryColorBlue) {
/* 5774 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5778 */         if (vz.getWatcher().getWurmId() == creature.getWurmId()) {
/*      */           
/* 5780 */           vz.sendUseItem(null, modelname, rarity, colorRed, colorGreen, colorBlue, secondaryColorRed, secondaryColorGreen, secondaryColorBlue);
/*      */ 
/*      */         
/*      */         }
/* 5784 */         else if (creature.isVisibleTo(vz.getWatcher())) {
/* 5785 */           vz.sendUseItem(creature, modelname, rarity, colorRed, colorGreen, colorBlue, secondaryColorRed, secondaryColorGreen, secondaryColorBlue);
/*      */         }
/*      */       
/* 5788 */       } catch (Exception e) {
/*      */         
/* 5790 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendStopUseItem(Creature creature) {
/* 5797 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5801 */         if (vz.getWatcher().getWurmId() == creature.getWurmId()) {
/*      */           
/* 5803 */           vz.sendStopUseItem(null);
/*      */ 
/*      */         
/*      */         }
/* 5807 */         else if (creature.isVisibleTo(vz.getWatcher())) {
/* 5808 */           vz.sendStopUseItem(creature);
/*      */         }
/*      */       
/* 5811 */       } catch (Exception e) {
/*      */         
/* 5813 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendAnimation(Creature creature, String animationName, boolean looping, long target) {
/* 5820 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5824 */         if (vz.getWatcher().getWurmId() == creature.getWurmId()) {
/*      */           
/* 5826 */           vz.sendAnimation(null, animationName, looping, target);
/*      */ 
/*      */         
/*      */         }
/* 5830 */         else if (creature.isVisibleTo(vz.getWatcher())) {
/* 5831 */           vz.sendAnimation(creature, animationName, looping, target);
/*      */         }
/*      */       
/* 5834 */       } catch (Exception e) {
/*      */         
/* 5836 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendStance(Creature creature, byte stance) {
/* 5843 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5847 */         if (vz.getWatcher().getWurmId() == creature.getWurmId()) {
/*      */           
/* 5849 */           vz.sendStance(null, stance);
/*      */ 
/*      */         
/*      */         }
/* 5853 */         else if (creature.isVisibleTo(vz.getWatcher())) {
/* 5854 */           vz.sendStance(creature, stance);
/*      */         }
/*      */       
/* 5857 */       } catch (Exception e) {
/*      */         
/* 5859 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendAnimation(Creature initiator, Item item, String animationName, boolean looping, boolean freeze) {
/* 5866 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5870 */         Creature watcher = vz.getWatcher();
/* 5871 */         if (watcher != null)
/*      */         {
/*      */           
/* 5874 */           if (vz.isVisible(item, this) && (initiator == null || initiator.isVisibleTo(watcher))) {
/* 5875 */             watcher.getCommunicator()
/* 5876 */               .sendAnimation(item.getWurmId(), animationName, looping, freeze);
/*      */           }
/*      */         }
/* 5879 */       } catch (Exception e) {
/*      */         
/* 5881 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendCreatureDamage(Creature creature, float damPercent) {
/* 5888 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5892 */         if (creature.isVisibleTo(vz.getWatcher())) {
/* 5893 */           vz.sendCreatureDamage(creature, damPercent);
/*      */         }
/* 5895 */       } catch (Exception e) {
/*      */         
/* 5897 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendFishingLine(Creature creature, float posX, float posY, byte floatType) {
/* 5904 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5908 */         if (creature.isVisibleTo(vz.getWatcher())) {
/* 5909 */           vz.sendFishingLine(creature, posX, posY, floatType);
/*      */         }
/* 5911 */       } catch (Exception e) {
/*      */         
/* 5913 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendFishHooked(Creature creature, byte fishType, long fishId) {
/* 5920 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5924 */         if (creature.isVisibleTo(vz.getWatcher())) {
/* 5925 */           vz.sendFishHooked(creature, fishType, fishId);
/*      */         }
/* 5927 */       } catch (Exception e) {
/*      */         
/* 5929 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendFishingStopped(Creature creature) {
/* 5936 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5940 */         if (creature.isVisibleTo(vz.getWatcher())) {
/* 5941 */           vz.sendFishingStopped(creature);
/*      */         }
/* 5943 */       } catch (Exception e) {
/*      */         
/* 5945 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendSpearStrike(Creature creature, float posX, float posY) {
/* 5952 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5956 */         if (creature.isVisibleTo(vz.getWatcher())) {
/* 5957 */           vz.sendSpearStrike(creature, posX, posY);
/*      */         }
/* 5959 */       } catch (Exception e) {
/*      */         
/* 5961 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendRepaint(Item item) {
/* 5968 */     boolean noPaint = (item.color == -1);
/* 5969 */     boolean noPaint2 = (item.color2 == -1);
/* 5970 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5974 */         vz.sendRepaint(item.getWurmId(), (byte)WurmColor.getColorRed(item.getColor()), 
/* 5975 */             (byte)WurmColor.getColorGreen(item.getColor()), (byte)WurmColor.getColorBlue(item.getColor()), noPaint ? 0 : -1, (byte)0);
/*      */         
/* 5977 */         if (item.supportsSecondryColor()) {
/* 5978 */           vz.sendRepaint(item.getWurmId(), (byte)WurmColor.getColorRed(item.getColor2()), 
/* 5979 */               (byte)WurmColor.getColorGreen(item.getColor2()), (byte)WurmColor.getColorBlue(item.getColor2()), noPaint2 ? 0 : -1, (byte)1);
/*      */         }
/* 5981 */       } catch (Exception e) {
/*      */         
/* 5983 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendAttachCreature(long creatureId, long targetId, float offx, float offy, float offz, int seatId) {
/* 5991 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 5995 */         if (vz.getWatcher().getWurmId() == creatureId) {
/*      */           
/* 5997 */           vz.sendAttachCreature(-1L, targetId, offx, offy, offz, seatId);
/*      */         }
/* 5999 */         else if (vz.getWatcher().getWurmId() == targetId) {
/*      */           
/* 6001 */           vz.sendAttachCreature(creatureId, -1L, offx, offy, offz, seatId);
/*      */         } else {
/*      */           
/* 6004 */           vz.sendAttachCreature(creatureId, targetId, offx, offy, offz, seatId);
/*      */         } 
/* 6006 */       } catch (Exception e) {
/*      */         
/* 6008 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendAttachCreature(long creatureId, long targetId, float offx, float offy, float offz, int seatId, boolean ignoreOrigin) {
/* 6016 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 6020 */         if (vz.getWatcher().getWurmId() == creatureId) {
/*      */           
/* 6022 */           if (!ignoreOrigin) {
/* 6023 */             vz.sendAttachCreature(-1L, targetId, offx, offy, offz, seatId);
/*      */           }
/* 6025 */         } else if (vz.getWatcher().getWurmId() == targetId) {
/*      */           
/* 6027 */           vz.sendAttachCreature(creatureId, -1L, offx, offy, offz, seatId);
/*      */         } else {
/*      */           
/* 6030 */           vz.sendAttachCreature(creatureId, targetId, offx, offy, offz, seatId);
/*      */         } 
/* 6032 */       } catch (Exception e) {
/*      */         
/* 6034 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<VolaTile> getThisAndSurroundingTiles(int dist) {
/* 6041 */     Set<VolaTile> surr = new HashSet<>();
/* 6042 */     VolaTile t = null;
/* 6043 */     for (int x = -dist; x <= dist; x++) {
/*      */       
/* 6045 */       for (int y = -dist; y <= dist; y++) {
/*      */         
/* 6047 */         t = Zones.getTileOrNull(Zones.safeTileX(this.tilex + x), Zones.safeTileY(this.tiley + y), this.surfaced);
/* 6048 */         if (t != null)
/*      */         {
/* 6050 */           surr.add(t);
/*      */         }
/*      */       } 
/*      */     } 
/* 6054 */     return surr;
/*      */   }
/*      */ 
/*      */   
/*      */   public void checkDiseaseSpread() {
/* 6059 */     int dist = 1;
/* 6060 */     if (this.village != null && 
/* 6061 */       this.village.getCreatureRatio() < Village.OPTIMUMCRETRATIO)
/* 6062 */       dist = 2; 
/* 6063 */     Set<VolaTile> set = getThisAndSurroundingTiles(dist);
/* 6064 */     for (VolaTile t : set) {
/*      */       
/* 6066 */       Creature[] crets = t.getCreatures();
/* 6067 */       for (Creature c : crets) {
/*      */         
/* 6069 */         if (!c.isPlayer() && !c.isKingdomGuard() && !c.isSpiritGuard() && !c.isUnique())
/*      */         {
/* 6071 */           if (Server.rand.nextInt(100) == 0)
/*      */           {
/* 6073 */             if (c.getDisease() == 0) {
/*      */               
/* 6075 */               logger.log(Level.INFO, "Disease spreads to " + c.getName() + " at " + t);
/* 6076 */               c.setDisease((byte)1);
/*      */             } 
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void checkVisibility(Creature watched, boolean makeInvis) {
/*      */     float lStealthMod;
/* 6087 */     if (makeInvis) {
/* 6088 */       lStealthMod = MethodsCreatures.getStealthTerrainModifier(watched, this.tilex, this.tiley, this.surfaced);
/*      */     } else {
/* 6090 */       lStealthMod = 0.0F;
/* 6091 */     }  for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 6095 */         if (vz.getWatcher().getWurmId() != watched.getWurmId())
/*      */         {
/* 6097 */           if (makeInvis && !watched.visibilityCheck(vz.getWatcher(), lStealthMod)) {
/*      */             
/* 6099 */             vz.makeInvisible(watched);
/*      */           } else {
/*      */ 
/*      */             
/*      */             try {
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
/* 6116 */               vz.addCreature(watched.getWurmId(), false);
/*      */             }
/* 6118 */             catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */             
/* 6121 */             } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */       }
/* 6127 */       catch (Exception e) {
/*      */         
/* 6129 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void checkCaveOpening() {
/* 6136 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 6140 */         vz.getWatcher().getVisionArea().checkCaves(false);
/*      */       }
/* 6142 */       catch (Exception e) {
/*      */         
/* 6144 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNewFace(Creature c) {
/* 6151 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 6155 */         vz.setNewFace(c);
/*      */       }
/* 6157 */       catch (Exception e) {
/*      */         
/* 6159 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNewRarityShader(Creature c) {
/* 6166 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 6170 */         vz.setNewRarityShader(c);
/*      */       }
/* 6172 */       catch (Exception e) {
/*      */         
/* 6174 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendActionControl(Creature c, String actionString, boolean start, int timeLeft) {
/* 6181 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 6185 */         vz.sendActionControl(c.getWurmId(), actionString, start, timeLeft);
/*      */       }
/* 6187 */       catch (Exception e) {
/*      */         
/* 6189 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendActionControl(Item item, String actionString, boolean start, int timeLeft) {
/* 6196 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 6200 */         vz.sendActionControl(item.getWurmId(), actionString, start, timeLeft);
/*      */       }
/* 6202 */       catch (Exception e) {
/*      */         
/* 6204 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendRotate(Item item, float rotation) {
/* 6211 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 6215 */         vz.sendRotate(item, rotation);
/*      */       }
/* 6217 */       catch (Exception e) {
/*      */         
/* 6219 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int getLayer() {
/* 6226 */     if (this.surfaced)
/* 6227 */       return 0; 
/* 6228 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendAddTileEffect(AreaSpellEffect effect, boolean loop) {
/* 6233 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 6237 */         vz.addAreaSpellEffect(effect, loop);
/*      */       }
/* 6239 */       catch (Exception e) {
/*      */         
/* 6241 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendAddQuickTileEffect(byte effect, int floorOffset) {
/* 6248 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 6252 */         vz.sendAddTileEffect(this.tilex, this.tiley, getLayer(), effect, floorOffset, false);
/*      */       }
/* 6254 */       catch (Exception e) {
/*      */         
/* 6256 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendRemoveTileEffect(AreaSpellEffect effect) {
/* 6263 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 6267 */         vz.removeAreaSpellEffect(effect);
/*      */       }
/* 6269 */       catch (Exception e) {
/*      */         
/* 6271 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateFenceState(Fence fence) {
/* 6278 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 6282 */         vz.updateFenceDamageState(fence);
/*      */       }
/* 6284 */       catch (Exception e) {
/*      */         
/* 6286 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateTargetStatus(long targetId, byte type, float status) {
/* 6293 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 6297 */         vz.updateTargetStatus(targetId, type, status);
/*      */       }
/* 6299 */       catch (Exception e) {
/*      */         
/* 6301 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateWallDamageState(Wall wall) {
/* 6308 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 6312 */         vz.updateWallDamageState(wall);
/*      */       }
/* 6314 */       catch (Exception e) {
/*      */         
/* 6316 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateFloorDamageState(Floor floor) {
/* 6323 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 6327 */         vz.updateFloorDamageState(floor);
/*      */       }
/* 6329 */       catch (Exception e) {
/*      */         
/* 6331 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateBridgePartDamageState(BridgePart bridgePart) {
/* 6338 */     for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */       
/*      */       try {
/* 6342 */         vz.updateBridgePartDamageState(bridgePart);
/*      */       }
/* 6344 */       catch (Exception e) {
/*      */         
/* 6346 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private AreaSpellEffect getAreaEffect() {
/* 6353 */     return AreaSpellEffect.getEffect(this.tilex, this.tiley, getLayer());
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isInPvPZone() {
/* 6358 */     return Zones.isOnPvPServer(this.tilex, this.tiley);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void lightLamps() {
/* 6363 */     if (this.vitems != null)
/*      */     {
/* 6365 */       for (Item i : this.vitems.getAllItemsAsSet()) {
/*      */         
/* 6367 */         if (i.isStreetLamp())
/*      */         {
/* 6369 */           if (i.isPlanted()) {
/*      */             
/* 6371 */             i.setAuxData((byte)120);
/* 6372 */             i.setTemperature((short)10000);
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
/*      */   public String toString() {
/* 6387 */     return "VolaTile [X: " + this.tilex + ", Y: " + this.tiley + ", surf=" + this.surfaced + "]";
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
/*      */   public Floor[] getFloors(int startHeightOffset, int endHeightOffset) {
/* 6401 */     if (this.floors == null) {
/* 6402 */       return emptyFloors;
/*      */     }
/* 6404 */     List<Floor> toReturn = new ArrayList<>();
/* 6405 */     for (Floor floor : this.floors) {
/*      */ 
/*      */ 
/*      */       
/* 6409 */       if (floor.getHeightOffset() >= startHeightOffset && floor.getHeightOffset() <= endHeightOffset)
/* 6410 */         toReturn.add(floor); 
/*      */     } 
/* 6412 */     return toReturn.<Floor>toArray(new Floor[toReturn.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Floor getFloor(int floorLevel) {
/* 6423 */     if (this.floors != null)
/*      */     {
/* 6425 */       for (Floor floor : this.floors) {
/*      */         
/* 6427 */         if (floor.getFloorLevel() == floorLevel)
/* 6428 */           return floor; 
/*      */       } 
/*      */     }
/* 6431 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Floor[] getFloors() {
/* 6439 */     if (this.floors == null) {
/* 6440 */       return emptyFloors;
/*      */     }
/* 6442 */     return this.floors.<Floor>toArray(new Floor[this.floors.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void addFloor(Floor floor) {
/* 6453 */     if (this.floors == null)
/* 6454 */       this.floors = new HashSet<>(); 
/* 6455 */     this.floors.add(floor);
/* 6456 */     if (floor.isStair())
/*      */     {
/* 6458 */       Stairs.addStair(hashCode(), floor.getFloorLevel());
/*      */     }
/* 6460 */     if (this.vitems != null)
/*      */     {
/* 6462 */       for (Item pile : this.vitems.getPileItems())
/*      */       {
/* 6464 */         pile.updatePosZ(this);
/*      */       }
/*      */     }
/* 6467 */     if (this.watchers != null)
/*      */     {
/* 6469 */       for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */         
/*      */         try {
/* 6473 */           vz.updateFloor(this.structure.getWurmId(), floor);
/*      */         }
/* 6475 */         catch (Exception e) {
/*      */           
/* 6477 */           logger.log(Level.WARNING, e.getMessage(), e);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final void removeFloor(Blocker floor) {
/* 6485 */     if (this.floors != null) {
/*      */       
/* 6487 */       Floor toRem = null;
/* 6488 */       for (Floor fl : this.floors) {
/*      */         
/* 6490 */         if (fl.getId() == floor.getId()) {
/*      */           
/* 6492 */           toRem = fl;
/*      */           break;
/*      */         } 
/*      */       } 
/* 6496 */       if (toRem != null) {
/* 6497 */         removeFloor(toRem);
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
/*      */   public final void removeFloor(Floor floor) {
/* 6509 */     int floorLevel = floor.getFloorLevel();
/* 6510 */     if (this.floors != null) {
/*      */       
/* 6512 */       this.floors.remove(floor);
/* 6513 */       if (floor.isStair())
/* 6514 */         Stairs.removeStair(hashCode(), floorLevel); 
/* 6515 */       if (this.floors.size() == 0)
/* 6516 */         this.floors = null; 
/*      */     } 
/* 6518 */     if (this.structure == null)
/*      */       return; 
/* 6520 */     if (this.watchers != null)
/*      */     {
/* 6522 */       for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */         
/*      */         try {
/* 6526 */           vz.removeFloor(this.structure.getWurmId(), floor);
/*      */         }
/* 6528 */         catch (Exception e) {
/*      */           
/* 6530 */           logger.log(Level.WARNING, e.getMessage(), e);
/*      */         } 
/*      */       } 
/*      */     }
/* 6534 */     if (floorLevel > 0) {
/* 6535 */       destroyPileItem(floorLevel);
/*      */     
/*      */     }
/* 6538 */     else if (this.vitems != null) {
/*      */       
/* 6540 */       Item pileItem = this.vitems.getPileItem(floorLevel);
/* 6541 */       if (pileItem != null) {
/*      */         
/* 6543 */         pileItem.updatePosZ(this);
/*      */         
/* 6545 */         for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */           
/*      */           try {
/* 6549 */             if (vz.isVisible(pileItem, this))
/*      */             {
/* 6551 */               vz.removeItem(pileItem);
/* 6552 */               vz.addItem(pileItem, this, true);
/*      */             }
/*      */           
/* 6555 */           } catch (Exception e) {
/*      */             
/* 6557 */             logger.log(Level.WARNING, e.getMessage(), e);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 6564 */     if (this.vitems != null)
/*      */     {
/* 6566 */       for (Item item : this.vitems.getAllItemsAsArray()) {
/*      */         
/* 6568 */         if (item.isDecoration() && item.getFloorLevel() == floorLevel) {
/*      */           
/* 6570 */           item.updatePosZ(this);
/* 6571 */           item.updateIfGroundItem();
/*      */         } 
/*      */       } 
/*      */     }
/* 6575 */     if (this.creatures != null)
/*      */     {
/* 6577 */       for (Creature c : this.creatures) {
/*      */         
/* 6579 */         if (c.getFloorLevel() == floorLevel)
/*      */         {
/* 6581 */           if (!c.isPlayer()) {
/*      */             
/* 6583 */             float oldposz = c.getPositionZ();
/* 6584 */             float newPosz = c.calculatePosZ();
/* 6585 */             float diffz = newPosz - oldposz;
/* 6586 */             c.setPositionZ(newPosz);
/* 6587 */             c.moved(0.0F, 0.0F, diffz, 0, 0);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6596 */     checkDeletion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void addBridgePart(BridgePart bridgePart) {
/* 6607 */     if (this.bridgeParts == null)
/* 6608 */       this.bridgeParts = new HashSet<>(); 
/* 6609 */     this.bridgeParts.add(bridgePart);
/*      */     
/* 6611 */     if (this.vitems != null)
/*      */     {
/* 6613 */       for (Item pile : this.vitems.getPileItems())
/*      */       {
/* 6615 */         pile.updatePosZ(this);
/*      */       }
/*      */     }
/* 6618 */     if (this.watchers != null)
/*      */     {
/* 6620 */       for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */         
/*      */         try {
/* 6624 */           vz.updateBridgePart(this.structure.getWurmId(), bridgePart);
/*      */         }
/* 6626 */         catch (Exception e) {
/*      */           
/* 6628 */           logger.log(Level.WARNING, e.getMessage(), e);
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
/*      */   public final void removeBridgePart(BridgePart bridgePart) {
/* 6642 */     if (this.bridgeParts != null) {
/*      */       
/* 6644 */       this.bridgeParts.remove(bridgePart);
/* 6645 */       if (this.bridgeParts.size() == 0)
/* 6646 */         this.bridgeParts = null; 
/*      */     } 
/* 6648 */     if (this.structure == null)
/*      */       return; 
/* 6650 */     if (this.watchers != null)
/*      */     {
/* 6652 */       for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */         
/*      */         try {
/* 6656 */           vz.removeBridgePart(this.structure.getWurmId(), bridgePart);
/*      */         }
/* 6658 */         catch (Exception e) {
/*      */           
/* 6660 */           logger.log(Level.WARNING, e.getMessage(), e);
/*      */         } 
/*      */       } 
/*      */     }
/* 6664 */     if (this.vitems != null)
/*      */     {
/* 6666 */       for (Item pile : this.vitems.getPileItems()) {
/*      */         
/* 6668 */         pile.setOnBridge(-10L);
/* 6669 */         pile.updatePosZ(this);
/*      */         
/* 6671 */         for (VirtualZone vz : getWatchers()) {
/*      */ 
/*      */           
/*      */           try {
/* 6675 */             if (vz.isVisible(pile, this))
/*      */             {
/* 6677 */               vz.removeItem(pile);
/* 6678 */               vz.addItem(pile, this, true);
/*      */             }
/*      */           
/* 6681 */           } catch (Exception e) {
/*      */             
/* 6683 */             logger.log(Level.WARNING, e.getMessage(), e);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/* 6689 */     if (this.vitems != null)
/*      */     {
/* 6691 */       for (Item item : this.vitems.getAllItemsAsArray()) {
/*      */         
/* 6693 */         if (item.getBridgeId() == this.structure.getWurmId()) {
/*      */           
/* 6695 */           item.setOnBridge(-10L);
/* 6696 */           item.updatePosZ(this);
/* 6697 */           item.updateIfGroundItem();
/*      */         } 
/*      */       } 
/*      */     }
/* 6701 */     if (this.creatures != null)
/*      */     {
/* 6703 */       for (Creature c : this.creatures) {
/*      */         
/* 6705 */         if (c.getBridgeId() == this.structure.getWurmId()) {
/*      */           
/* 6707 */           c.setBridgeId(-10L);
/* 6708 */           if (!c.isPlayer()) {
/*      */             
/* 6710 */             float oldposz = c.getPositionZ();
/* 6711 */             float newPosz = c.calculatePosZ();
/* 6712 */             float diffz = newPosz - oldposz;
/* 6713 */             c.setPositionZ(newPosz);
/* 6714 */             c.moved(0.0F, 0.0F, diffz, 0, 0);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6723 */     checkDeletion();
/*      */   }
/*      */ 
/*      */   
/*      */   public final Floor getTopFloor() {
/* 6728 */     if (this.floors != null) {
/*      */       
/* 6730 */       Floor toret = null;
/* 6731 */       for (Floor floor : this.floors) {
/*      */         
/* 6733 */         if (toret == null || floor.getFloorLevel() > toret.getFloorLevel())
/*      */         {
/* 6735 */           toret = floor;
/*      */         }
/*      */       } 
/* 6738 */       return toret;
/*      */     } 
/* 6740 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Fence getTopFence() {
/* 6745 */     if (this.fences != null) {
/*      */       
/* 6747 */       Fence toret = null;
/* 6748 */       for (Fence f : this.fences.values()) {
/*      */         
/* 6750 */         if (toret == null || f.getFloorLevel() > toret.getFloorLevel())
/* 6751 */           toret = f; 
/*      */       } 
/* 6753 */       return toret;
/*      */     } 
/* 6755 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Wall getTopWall() {
/* 6760 */     if (this.walls != null) {
/*      */       
/* 6762 */       Wall toret = null;
/* 6763 */       for (Wall f : this.walls) {
/*      */         
/* 6765 */         if ((toret == null || f.getFloorLevel() > toret.getFloorLevel()) && 
/* 6766 */           f.isFinished())
/* 6767 */           toret = f; 
/*      */       } 
/* 6769 */       return toret;
/*      */     } 
/* 6771 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isNextTo(VolaTile t) {
/* 6776 */     if (t == null || t.getLayer() != getLayer())
/* 6777 */       return false; 
/* 6778 */     if ((t.getTileX() == getTileX() - 1 || t.getTileX() == getTileX() + 1) && t.getTileY() == getTileY())
/* 6779 */       return true; 
/* 6780 */     if (t.getTileX() == getTileX() && (t.getTileY() == getTileY() - 1 || t.getTileY() == getTileY() + 1))
/* 6781 */       return true; 
/* 6782 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void damageFloors(int minFloorLevel, int maxFloorLevel, float addedDamage) {
/* 6787 */     Floor[] floorArr = getFloors(minFloorLevel * 30, maxFloorLevel * 30);
/* 6788 */     for (Floor floor : floorArr) {
/*      */       
/* 6790 */       floor.setDamage(floor.getDamage() + addedDamage);
/* 6791 */       if (floor.getDamage() >= 100.0F) {
/* 6792 */         removeFloor(floor);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public final boolean hasStair(int floorLevel) {
/* 6798 */     return Stairs.hasStair(hashCode(), floorLevel);
/*      */   }
/*      */ 
/*      */   
/*      */   public Item findHive(int hiveType) {
/* 6803 */     if (this.vitems != null)
/*      */     {
/* 6805 */       for (Item item : this.vitems.getAllItemsAsArray()) {
/*      */         
/* 6807 */         if (item.getTemplateId() == hiveType)
/*      */         {
/* 6809 */           return item;
/*      */         }
/*      */       } 
/*      */     }
/* 6813 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public Item findHive(int hiveType, boolean withQueen) {
/* 6818 */     if (this.vitems != null)
/*      */     {
/* 6820 */       for (Item item : this.vitems.getAllItemsAsArray()) {
/*      */         
/* 6822 */         if (item.getTemplateId() == hiveType) {
/*      */           
/* 6824 */           if (withQueen && item.getAuxData() > 0)
/* 6825 */             return item; 
/* 6826 */           if (!withQueen && item.getAuxData() == 0)
/* 6827 */             return item; 
/*      */         } 
/*      */       } 
/*      */     }
/* 6831 */     return null;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\VolaTile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */