/*      */ package com.wurmonline.server.creatures;
/*      */ 
/*      */ import com.wurmonline.mesh.FoliageAge;
/*      */ import com.wurmonline.mesh.MeshIO;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.PlonkData;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.behaviours.Seat;
/*      */ import com.wurmonline.server.behaviours.Terraforming;
/*      */ import com.wurmonline.server.behaviours.Vehicle;
/*      */ import com.wurmonline.server.behaviours.Vehicles;
/*      */ import com.wurmonline.server.bodys.Wound;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.Trade;
/*      */ import com.wurmonline.server.modifiers.DoubleValueModifier;
/*      */ import com.wurmonline.server.modifiers.FixedDoubleValueModifier;
/*      */ import com.wurmonline.server.modifiers.ModifierTypes;
/*      */ import com.wurmonline.server.modifiers.ValueModifiedListener;
/*      */ import com.wurmonline.server.players.ItemBonus;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.structures.Blocker;
/*      */ import com.wurmonline.server.structures.Blocking;
/*      */ import com.wurmonline.server.structures.BlockingResult;
/*      */ import com.wurmonline.server.structures.BridgePart;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.BridgeConstants;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import com.wurmonline.shared.constants.Enchants;
/*      */ import com.wurmonline.shared.constants.ProtoConstants;
/*      */ import com.wurmonline.shared.util.MovementChecker;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.geom.AffineTransform;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
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
/*      */ public final class MovementScheme
/*      */   extends MovementChecker
/*      */   implements ModifierTypes, ValueModifiedListener, ProtoConstants, CounterTypes, TimeConstants, Enchants
/*      */ {
/*      */   private final Creature creature;
/*      */   boolean halted = false;
/*      */   private boolean encumbered = false;
/*      */   public Item draggedItem;
/*   76 */   private static final Logger logger = Logger.getLogger(MovementScheme.class.getName());
/*      */   private Set<DoubleValueModifier> modifiers;
/*   78 */   private float baseModifier = 1.0F;
/*   79 */   private static final DoubleValueModifier dragMod = (DoubleValueModifier)new FixedDoubleValueModifier(-0.5D);
/*   80 */   private static final DoubleValueModifier ramDragMod = (DoubleValueModifier)new FixedDoubleValueModifier(-0.75D);
/*   81 */   private static final DoubleValueModifier combatMod = (DoubleValueModifier)new FixedDoubleValueModifier(-0.7D);
/*   82 */   private static final DoubleValueModifier drunkMod = (DoubleValueModifier)new FixedDoubleValueModifier(-0.6D);
/*      */   
/*   84 */   private static final DoubleValueModifier mooreMod = (DoubleValueModifier)new FixedDoubleValueModifier(-5.0D);
/*   85 */   private static final DoubleValueModifier farwalkerMod = (DoubleValueModifier)new FixedDoubleValueModifier(0.5D);
/*      */   
/*      */   private boolean webArmoured = false;
/*      */   private boolean hasSpiritSpeed = false;
/*   89 */   private final DoubleValueModifier webArmourMod = new DoubleValueModifier(7, 0.0D);
/*      */   private boolean justWebSlowArmour = false;
/*   91 */   private static final DoubleValueModifier chargeMod = (DoubleValueModifier)new FixedDoubleValueModifier(0.10000000149011612D);
/*      */   
/*      */   DoubleValueModifier stealthMod;
/*   94 */   private static final DoubleValueModifier freezeMod = (DoubleValueModifier)new FixedDoubleValueModifier(-5.0D);
/*      */   private static final long NOID = -10L;
/*   96 */   public final DoubleValueModifier armourMod = new DoubleValueModifier(0.0D);
/*      */   private final List<Float> movementSpeeds;
/*      */   private final List<Byte> windImpacts;
/*      */   private final List<Short> mountSpeeds;
/*  100 */   private final Set<Integer> intraports = new HashSet<>();
/*      */   
/*  102 */   public int samePosCounts = 0;
/*      */   private static Vehicle vehic;
/*      */   private static Creature cretVehicle;
/*      */   public static Item itemVehicle;
/*      */   private static Player passenger;
/*  107 */   private int climbSkill = 10;
/*  108 */   Map<Long, Float> oldmoves = new HashMap<>();
/*  109 */   private int changedTileCounter = 0;
/*  110 */   private int errors = 0;
/*      */   
/*      */   private boolean hasWetFeet = false;
/*      */   
/*      */   private boolean outAtSea = false;
/*      */   
/*      */   private boolean m300m = false;
/*      */   
/*      */   private boolean m700m = false;
/*      */   
/*      */   private boolean m1400m = false;
/*      */   
/*      */   private boolean m2180m = false;
/*      */   
/*      */   MovementScheme(Creature _creature) {
/*  125 */     this.creature = _creature;
/*  126 */     this.movementSpeeds = new ArrayList<>();
/*  127 */     this.windImpacts = new ArrayList<>();
/*  128 */     this.mountSpeeds = new ArrayList<>();
/*  129 */     if (!this.creature.isPlayer()) {
/*  130 */       this.onGround = true;
/*      */     } else {
/*  132 */       this.halted = true;
/*  133 */     }  setLog(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void initalizeModifiersWithTemplate() {
/*  142 */     addModifier(this.armourMod);
/*  143 */     this.armourMod.addListener(this);
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
/*      */   public float getTileSteepness(int tilex, int tiley, int clayer) {
/*  157 */     if (this.creature != null && this.creature.getBridgeId() > 0L)
/*      */     {
/*  159 */       return 0.0F;
/*      */     }
/*  161 */     float highest = -100.0F;
/*  162 */     float lowest = 32000.0F;
/*  163 */     for (int x = 0; x <= 1; x++) {
/*      */       
/*  165 */       for (int y = 0; y <= 1; y++) {
/*      */         
/*  167 */         if (tilex + x < Zones.worldTileSizeX && tiley + y < Zones.worldTileSizeY)
/*      */         {
/*  169 */           if (clayer >= 0) {
/*      */             
/*  171 */             float height = Tiles.decodeHeightAsFloat(Server.surfaceMesh.getTile(tilex + x, tiley + y));
/*  172 */             if (height > highest)
/*  173 */               highest = height; 
/*  174 */             if (height < lowest) {
/*  175 */               lowest = height;
/*      */             }
/*      */           } else {
/*      */             
/*  179 */             float height = Tiles.decodeHeightAsFloat(Server.caveMesh.getTile(tilex + x, tiley + y));
/*  180 */             if (height > highest)
/*  181 */               highest = height; 
/*  182 */             if (height < lowest)
/*  183 */               lowest = height; 
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*  188 */     return highest - lowest;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected byte getTextureForTile(int xTile, int yTile, int layer, long bridgeId) {
/*  195 */     if (bridgeId > 0L) {
/*      */       
/*  197 */       VolaTile vt = Zones.getTileOrNull(xTile, yTile, (layer == 0));
/*  198 */       if (vt != null)
/*      */       {
/*  200 */         for (BridgePart bp : vt.getBridgeParts()) {
/*      */           
/*  202 */           if (bp.getStructureId() == bridgeId) {
/*      */             
/*  204 */             if (bp.getMaterial() == BridgeConstants.BridgeMaterial.WOOD || bp.getMaterial() == BridgeConstants.BridgeMaterial.ROPE)
/*  205 */               return Tiles.Tile.TILE_PLANKS.id; 
/*  206 */             if (bp.getMaterial() == BridgeConstants.BridgeMaterial.BRICK) {
/*  207 */               return Tiles.Tile.TILE_COBBLESTONE.id;
/*      */             }
/*      */             
/*  210 */             return Tiles.Tile.TILE_STONE_SLABS.id;
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*  215 */     MeshIO mesh = Server.surfaceMesh;
/*  216 */     if (layer < 0) {
/*      */       
/*  218 */       if (xTile < 0 || xTile > Zones.worldTileSizeX || yTile < 0 || yTile > Zones.worldTileSizeY)
/*  219 */         return Tiles.Tile.TILE_ROCK.id; 
/*  220 */       mesh = Server.caveMesh;
/*      */     } else {
/*  222 */       if (this.creature.hasOpenedMineDoor(xTile, yTile)) {
/*  223 */         return Tiles.Tile.TILE_HOLE.id;
/*      */       }
/*      */       
/*  226 */       if (xTile < 0 || xTile > Zones.worldTileSizeX || yTile < 0 || yTile > Zones.worldTileSizeY)
/*  227 */         return Tiles.Tile.TILE_DIRT.id; 
/*      */     } 
/*  229 */     return Tiles.decodeType(mesh.getTile(xTile, yTile));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getHeightOfBridge(int xTile, int yTile, int layer) {
/*  235 */     VolaTile vt = Zones.getTileOrNull(xTile, yTile, (layer == 0));
/*  236 */     if (vt != null) {
/*      */       
/*  238 */       BridgePart[] arrayOfBridgePart = vt.getBridgeParts(); int i = arrayOfBridgePart.length; byte b = 0; if (b < i) { BridgePart bp = arrayOfBridgePart[b];
/*      */         
/*  240 */         return bp.getHeight() / 10.0F; }
/*      */     
/*      */     } 
/*  243 */     return -1000.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isIntraTeleporting() {
/*  248 */     return !this.intraports.isEmpty();
/*      */   }
/*      */ 
/*      */   
/*      */   public void addIntraTeleport(int teleportNumber) {
/*  253 */     this.intraports.add(Integer.valueOf(teleportNumber));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean removeIntraTeleport(int teleportNumber) {
/*  258 */     this.intraports.remove(Integer.valueOf(teleportNumber));
/*  259 */     return this.intraports.isEmpty();
/*      */   }
/*      */ 
/*      */   
/*      */   public void clearIntraports() {
/*  264 */     this.intraports.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected float getCeilingForNode(int xTile, int yTile) {
/*  270 */     return Tiles.decodeData(Server.caveMesh.getTile(xTile, yTile) & 0xFF) / 10.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean removeWindMod(byte impact) {
/*  275 */     for (ListIterator<Byte> it = this.windImpacts.listIterator(); it.hasNext(); ) {
/*      */       
/*  277 */       Byte b = it.next();
/*  278 */       it.remove();
/*      */ 
/*      */       
/*  281 */       if (b.byteValue() == impact) {
/*      */         
/*  283 */         if (this.creature.isPlayer()) {
/*  284 */           ((Player)this.creature).sentWind = 0L;
/*      */         }
/*      */         
/*  287 */         return true;
/*      */       } 
/*      */     } 
/*  290 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setWindMod(byte impact) {
/*  295 */     setWindImpact(impact / 200.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean addWindImpact(byte impact) {
/*  300 */     if (this.windImpacts.isEmpty() || ((Byte)this.windImpacts.get(this.windImpacts.size() - 1)).byteValue() != impact) {
/*      */       
/*  302 */       this.windImpacts.add(Byte.valueOf(impact));
/*  303 */       this.creature.getCommunicator().sendWindImpact(impact);
/*  304 */       return true;
/*      */     } 
/*  306 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void resendMountSpeed() {
/*  311 */     if (this.mountSpeeds.size() > 0) {
/*  312 */       this.creature.getCommunicator().sendMountSpeed(((Short)this.mountSpeeds.get(0)).shortValue());
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean removeMountSpeed(short speed) {
/*  317 */     for (ListIterator<Short> it = this.mountSpeeds.listIterator(); it.hasNext(); ) {
/*      */       
/*  319 */       Short b = it.next();
/*  320 */       it.remove();
/*  321 */       if (b.shortValue() == speed) {
/*      */         
/*  323 */         if (this.creature.isPlayer())
/*  324 */           ((Player)this.creature).sentMountSpeed = 0L; 
/*  325 */         return true;
/*      */       } 
/*      */     } 
/*  328 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMountSpeed(short newMountSpeed) {
/*  333 */     if (this.commandingBoat) {
/*  334 */       setMountSpeed(newMountSpeed / 1000.0F);
/*      */     } else {
/*  336 */       setMountSpeed(newMountSpeed / 200.0F);
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean addMountSpeed(short speed) {
/*  341 */     if (this.mountSpeeds.isEmpty() || ((Short)this.mountSpeeds.get(this.mountSpeeds.size() - 1)).shortValue() != speed) {
/*      */ 
/*      */       
/*  344 */       this.mountSpeeds.add(Short.valueOf(speed));
/*  345 */       this.creature.getCommunicator().sendMountSpeed(speed);
/*  346 */       return true;
/*      */     } 
/*  348 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   boolean removeSpeedMod(float speedmod) {
/*  353 */     for (ListIterator<Float> it = this.movementSpeeds.listIterator(); it.hasNext(); ) {
/*      */       
/*  355 */       Float f = it.next();
/*  356 */       it.remove();
/*      */ 
/*      */ 
/*      */       
/*  360 */       if (f.floatValue() == speedmod)
/*      */       {
/*      */ 
/*      */         
/*  364 */         return true;
/*      */       }
/*      */     } 
/*  367 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendSpeedModifier() {
/*  372 */     if (addSpeedMod(Math.max(0.0F, getSpeedModifier())))
/*      */     {
/*  374 */       this.creature.getCommunicator().sendSpeedModifier(Math.max(0.0F, getSpeedModifier()));
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
/*      */   private boolean addSpeedMod(float speedmod) {
/*  387 */     this.oldmoves.put(new Long(System.currentTimeMillis()), new Float(speedmod));
/*  388 */     if (this.oldmoves.size() > 20) {
/*      */       
/*  390 */       Long[] longs = (Long[])this.oldmoves.keySet().toArray((Object[])new Long[this.oldmoves.size()]);
/*  391 */       for (int x = 0; x < 10; x++)
/*  392 */         this.oldmoves.remove(longs[x]); 
/*      */     } 
/*  394 */     if (this.movementSpeeds.isEmpty() || ((Float)this.movementSpeeds.get(this.movementSpeeds.size() - 1)).floatValue() != speedmod) {
/*      */       
/*  396 */       this.movementSpeeds.add(Float.valueOf(speedmod));
/*  397 */       return true;
/*      */     } 
/*  399 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void hitGround(float speed) {
/*  406 */     if (this.creature instanceof Player && !this.creature.isDead() && this.creature.isOnSurface())
/*      */     {
/*  408 */       if (this.creature.getVisionArea() != null && this.creature.getVisionArea().isInitialized() && 
/*  409 */         !this.creature.getCommunicator().isInvulnerable() && 
/*  410 */         !this.creature.getCommunicator().stillLoggingIn())
/*      */       {
/*  412 */         if (this.creature.getFarwalkerSeconds() <= 0)
/*      */         {
/*      */ 
/*      */ 
/*      */           
/*  417 */           if ((this.creature.getPower() < 1 || this.creature.loggerCreature1 > 0L) && speed > 0.5F) {
/*      */             
/*  419 */             if (this.creature.getLayer() >= 0) {
/*      */               
/*  421 */               float distFromBorder = 0.09F;
/*  422 */               float tilex = ((int)getX() / 4);
/*  423 */               float tiley = ((int)getY() / 4);
/*  424 */               if (getX() / 4.0F - tilex < distFromBorder) {
/*      */                 
/*  426 */                 VolaTile current = Zones.getTileOrNull((int)tilex, (int)tiley, true);
/*  427 */                 if (current != null) {
/*      */ 
/*      */ 
/*      */                   
/*  431 */                   Wall[] walls = current.getWalls();
/*  432 */                   for (Wall w : walls) {
/*      */                     
/*  434 */                     if (w.getFloorLevel() == this.creature.getFloorLevel() && w.getTileX() == tilex && 
/*  435 */                       !w.isHorizontal()) {
/*      */                       return;
/*      */                     }
/*      */                   } 
/*      */                   
/*  440 */                   Fence[] fences = current.getFences();
/*  441 */                   for (Fence f : fences) {
/*      */                     
/*  443 */                     if (f.getTileX() == tilex && !f.isHorizontal() && f
/*  444 */                       .getFloorLevel() == this.creature.getFloorLevel()) {
/*      */                       return;
/*      */                     }
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */               
/*  451 */               if (getY() / 4.0F - tiley < distFromBorder) {
/*      */                 
/*  453 */                 VolaTile current = Zones.getTileOrNull((int)tilex, (int)tiley, true);
/*  454 */                 if (current != null) {
/*      */ 
/*      */ 
/*      */                   
/*  458 */                   Wall[] walls = current.getWalls();
/*  459 */                   for (Wall w : walls) {
/*      */                     
/*  461 */                     if (w.getTileY() == tiley && w.isHorizontal() && w
/*  462 */                       .getFloorLevel() == this.creature.getFloorLevel()) {
/*      */                       return;
/*      */                     }
/*      */                   } 
/*      */                   
/*  467 */                   Fence[] fences = current.getFences();
/*  468 */                   for (Fence f : fences) {
/*      */                     
/*  470 */                     if (f.getTileY() == tiley && f.isHorizontal() && f
/*  471 */                       .getFloorLevel() == this.creature.getFloorLevel()) {
/*      */                       return;
/*      */                     }
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */               
/*  478 */               if (tilex + 1.0F - getX() / 4.0F < distFromBorder) {
/*      */ 
/*      */                 
/*  481 */                 VolaTile current = Zones.getTileOrNull((int)tilex + 1, (int)tiley, true);
/*  482 */                 if (current != null) {
/*      */ 
/*      */ 
/*      */                   
/*  486 */                   Wall[] walls = current.getWalls();
/*  487 */                   for (Wall w : walls) {
/*      */                     
/*  489 */                     if (w.getTileX() == tilex + 1.0F && !w.isHorizontal() && w
/*  490 */                       .getFloorLevel() == this.creature.getFloorLevel()) {
/*      */                       return;
/*      */                     }
/*      */                   } 
/*      */                   
/*  495 */                   Fence[] fences = current.getFences();
/*  496 */                   for (Fence f : fences) {
/*      */                     
/*  498 */                     if (f.getTileX() == tilex + 1.0F && !f.isHorizontal() && f
/*  499 */                       .getFloorLevel() == this.creature.getFloorLevel()) {
/*      */                       return;
/*      */                     }
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */               
/*  506 */               if (tiley + 1.0F - getY() / 4.0F < distFromBorder) {
/*      */ 
/*      */                 
/*  509 */                 VolaTile current = Zones.getTileOrNull((int)tilex, (int)tiley + 1, true);
/*  510 */                 if (current != null) {
/*      */ 
/*      */ 
/*      */                   
/*  514 */                   Wall[] walls = current.getWalls();
/*  515 */                   for (Wall w : walls) {
/*      */                     
/*  517 */                     if (w.getTileY() == tiley + 1.0F && w.isHorizontal() && w
/*  518 */                       .getFloorLevel() == this.creature.getFloorLevel()) {
/*      */                       return;
/*      */                     }
/*      */                   } 
/*      */                   
/*  523 */                   Fence[] fences = current.getFences();
/*  524 */                   for (Fence f : fences) {
/*      */                     
/*  526 */                     if (f.getTileY() == tiley + 1.0F && f.isHorizontal() && f
/*  527 */                       .getFloorLevel() == this.creature.getFloorLevel()) {
/*      */                       return;
/*      */                     }
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */             
/*  535 */             float baseDam = 1.0F + speed;
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             try {
/*  541 */               float damMod = 20.0F;
/*      */               
/*  543 */               float dam = baseDam * baseDam * baseDam * 24.0F * 60.0F * 20.0F / 15.0F;
/*      */               
/*  545 */               dam = Math.max(dam, 300.0F);
/*  546 */               this.creature.getCommunicator().sendNormalServerMessage("Ouch! That hurt!");
/*  547 */               this.creature.sendToLoggers("Speed=" + speed + ", baseDam=" + baseDam + " damMod=" + 20.0F + " weightCarried=" + this.creature
/*  548 */                   .getCarriedWeight() + " dam=" + dam);
/*      */               
/*  550 */               this.creature.achievement(88);
/*  551 */               if (!PlonkData.FALL_DAMAGE.hasSeenThis(this.creature)) {
/*  552 */                 PlonkData.FALL_DAMAGE.trigger(this.creature);
/*      */               }
/*  554 */               this.creature.addWoundOfType(null, (byte)0, 1, true, 1.0F, false, dam, 0.0F, 0.0F, false, false);
/*      */             }
/*  556 */             catch (Exception exception) {}
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
/*      */   
/*      */   protected float getHeightForNode(int xNode, int yNode, int layer) {
/*  576 */     return Zones.getHeightForNode(xNode, yNode, layer);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected float[] getNodeHeights(int xNode, int yNode, int layer, long bridgeId) {
/*  582 */     return Zones.getNodeHeights(xNode, yNode, layer, bridgeId);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean handleWrongLayer(int clientInputLayer, int expectedLayer) {
/*  588 */     if (this.creature.getVehicle() != -10L)
/*      */     {
/*      */ 
/*      */       
/*  592 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  598 */     if (this.creature.getPower() >= 2)
/*      */     {
/*  600 */       if (Tiles.decodeType(Server.caveMesh.getTile(this.creature.getTileX(), this.creature.getTileY())) != Tiles.Tile.TILE_CAVE_EXIT.id)
/*      */       {
/*  602 */         this.creature.getCommunicator().sendAlertServerMessage("You were detected to be on a different layer from what is shown in your client, setting layer to the one in your client.");
/*      */       }
/*      */     }
/*  605 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handlePlayerInRock() {
/*  612 */     if (!this.creature.isDead()) {
/*      */       
/*  614 */       int tilex = this.creature.getTileX();
/*  615 */       int tiley = this.creature.getTileY();
/*      */       
/*  617 */       if (Tiles.decodeType(Server.caveMesh.getTile(tilex, tiley)) != Tiles.Tile.TILE_CAVE.id && 
/*  618 */         Tiles.decodeType(Server.caveMesh.getTile(tilex, tiley)) != Tiles.Tile.TILE_CAVE_EXIT.id) {
/*      */         
/*  620 */         if (this.creature.getVehicle() == -10L)
/*      */         {
/*  622 */           for (int x = -1; x <= 1; x++) {
/*      */             
/*  624 */             for (int y = -1; y <= 1; y++) {
/*      */ 
/*      */               
/*  627 */               byte type = Tiles.decodeType(Server.caveMesh.getTile(Zones.safeTileX(x + tilex), 
/*  628 */                     Zones.safeTileY(y + tiley)));
/*  629 */               Tiles.Tile tempTile = Tiles.getTile(type);
/*      */               
/*  631 */               if (type == Tiles.Tile.TILE_CAVE_EXIT.id) {
/*      */                 
/*  633 */                 this.creature.setTeleportPoints((short)Zones.safeTileX(x + tilex), 
/*  634 */                     (short)Zones.safeTileY(y + tiley), 0, 0);
/*  635 */                 this.creature.startTeleporting();
/*  636 */                 this.creature.getCommunicator().sendTeleport(false, false, (byte)0);
/*      */                 return;
/*      */               } 
/*  639 */               if (!tempTile.isSolidCave() && tempTile != null) {
/*      */                 
/*  641 */                 this.creature.setTeleportPoints((short)Zones.safeTileX(x + tilex), 
/*  642 */                     (short)Zones.safeTileY(y + tiley), -1, 0);
/*  643 */                 this.creature.startTeleporting();
/*  644 */                 this.creature.getCommunicator().sendTeleport(false, false, (byte)0);
/*      */                 return;
/*      */               } 
/*      */             } 
/*      */           } 
/*  649 */           this.creature.getCommunicator().sendAlertServerMessage("You manage to become stuck in the rock, and quickly suffocate.");
/*      */ 
/*      */           
/*  652 */           this.creature.die(false, "Suffocated in Rock (2)");
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  658 */         ((Player)this.creature).intraTeleport(this.creature.getPosX(), this.creature.getPosY(), this.creature.getPositionZ(), this.creature
/*  659 */             .getStatus().getRotation(), this.creature.getLayer(), "in rock commanding=" + this.creature.isVehicleCommander() + " height=" + this.creature
/*  660 */             .getPositionZ());
/*  661 */         if (this.creature.isVehicleCommander()) {
/*      */           
/*  663 */           Vehicle creatureVehicle = Vehicles.getVehicleForId(this.creature.getVehicle());
/*  664 */           if (creatureVehicle != null)
/*      */           {
/*  666 */             if (!creatureVehicle.isCreature()) {
/*      */ 
/*      */               
/*      */               try {
/*  670 */                 Item ivehicle = Items.getItem(this.creature.getVehicle());
/*  671 */                 if (!ivehicle.isOnSurface())
/*      */                 {
/*  673 */                   itemVehicle.newLayer = (byte)this.creature.getLayer();
/*      */                   
/*      */                   try {
/*  676 */                     Zone z1 = Zones.getZone(ivehicle.getTileX(), ivehicle.getTileY(), false);
/*  677 */                     z1.removeItem(ivehicle);
/*  678 */                     ivehicle.setPosXY(this.creature.getPosX(), this.creature.getPosY());
/*  679 */                     Zone z2 = Zones.getZone(ivehicle.getTileX(), ivehicle.getTileY(), false);
/*  680 */                     z2.addItem(ivehicle);
/*      */                   }
/*  682 */                   catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */                   
/*  685 */                   itemVehicle.newLayer = Byte.MIN_VALUE;
/*      */                 }
/*      */               
/*  688 */               } catch (NoSuchItemException noSuchItemException) {}
/*      */             } else {
/*      */ 
/*      */               
/*      */               try {
/*      */ 
/*      */ 
/*      */                 
/*  696 */                 Creature cvehicle = Creatures.getInstance().getCreature(this.creature.getVehicle());
/*  697 */                 if (!cvehicle.isOnSurface()) {
/*      */                   
/*      */                   try {
/*      */                     
/*  701 */                     Zone z1 = Zones.getZone(cvehicle.getTileX(), cvehicle.getTileY(), false);
/*  702 */                     z1.removeCreature(cvehicle, true, false);
/*  703 */                     cvehicle.setPositionX(this.creature.getPosX());
/*  704 */                     cvehicle.setPositionY(this.creature.getPosY());
/*  705 */                     Zone z2 = Zones.getZone(cvehicle.getTileX(), cvehicle.getTileY(), false);
/*  706 */                     z2.addCreature(cvehicle.getWurmId());
/*      */                   }
/*  708 */                   catch (NoSuchZoneException noSuchZoneException) {
/*      */ 
/*      */                   
/*  711 */                   } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */                 
/*      */                 }
/*      */               
/*      */               }
/*  716 */               catch (NoSuchCreatureException noSuchCreatureException) {}
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
/*      */   protected void setLayer(int layer) {
/*  731 */     this.creature.setLayer(layer, false);
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
/*      */   protected boolean handleMoveTooFar(float clientInput, float expectedDistance) {
/*  744 */     if (!this.creature.isDead() && (this.creature.getPower() < 1 || this.creature.loggerCreature1 != -10L) && 
/*  745 */       clientInput - expectedDistance * 1.1F > 0.0F)
/*      */     {
/*      */       
/*  748 */       if (this.changedTileCounter == 0) {
/*      */         
/*  750 */         setErrors(getErrors() + 1);
/*  751 */         if (getErrors() > 4) {
/*      */           
/*  753 */           this.creature.getStatus().setNormalRegen(false);
/*      */ 
/*      */           
/*  756 */           logger.log(Level.WARNING, this.creature.getName() + " TOO FAR, input=" + clientInput + ", expected=" + expectedDistance + " :  " + 
/*  757 */               getCurrx() + "(" + getXold() + "); " + getCurry() + "(" + 
/*  758 */               getYold() + ") bridge=" + this.creature.getBridgeId());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  767 */           ((Player)this.creature).intraTeleport(this.creature.getPosX(), this.creature.getPosY(), this.creature.getPositionZ(), this.creature
/*  768 */               .getStatus().getRotation(), this.creature.getLayer(), "Moved too far");
/*  769 */           setAbort(true);
/*  770 */           if (getErrors() > 10)
/*      */           {
/*  772 */             Players.getInstance().sendGlobalGMMessage(this.creature, " movement too far (" + (clientInput - expectedDistance) + ") at " + (
/*      */                 
/*  774 */                 getCurrx() / 4.0F) + "," + (
/*  775 */                 getCurry() / 4.0F));
/*      */           }
/*  777 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  788 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setServerClimbing(boolean climb) {
/*  793 */     setClimbing(climb);
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
/*      */   protected boolean handleMoveTooShort(float clientInput, float expectedDistance) {
/*  809 */     if (!this.creature.isTeleporting() && !isIntraTeleporting() && !this.creature.isDead() && (this.creature
/*  810 */       .getCommunicator().hasReceivedTicks() || !isClimbing()) && this.creature
/*  811 */       .getPower() < 2)
/*      */     {
/*  813 */       if (this.changedTileCounter == 0)
/*      */       {
/*  815 */         if (this.creature.getCurrentTile() != null);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  822 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean movedOnStair() {
/*  828 */     VolaTile t = this.creature.getCurrentTile();
/*  829 */     if (t != null)
/*      */     {
/*  831 */       if (t.hasStair(this.creature.getFloorLevel() + 1) || t.hasStair(this.creature.getFloorLevel())) {
/*      */         
/*  833 */         setBridgeCounter(10);
/*  834 */         this.wasOnStair = true;
/*      */       } 
/*      */     }
/*      */     
/*  838 */     return this.wasOnStair;
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
/*      */   protected boolean handleZError(float clientInput, float expectedPosition) {
/*  851 */     if (this.changedTileCounter == 0 && this.creature.getPower() < 2 && !this.creature.isTeleporting() && 
/*  852 */       !this.creature.isDead() && (this.creature.getCommunicator().hasReceivedTicks() || !isClimbing())) {
/*      */       
/*  854 */       if (getTargetGroundOffset() != getGroundOffset()) {
/*  855 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  859 */       if (this.creature.getVisionArea() != null && this.creature.getVisionArea().isInitialized()) {
/*      */         
/*  861 */         setErrors(getErrors() + 1);
/*  862 */         if (getErrors() > 4) {
/*      */           
/*  864 */           ((Player)this.creature).intraTeleport(this.creature.getPosX(), this.creature.getPosY(), this.creature.getPositionZ(), this.creature
/*  865 */               .getStatus().getRotation(), this.creature.getLayer(), "Error in z=" + clientInput + ", expected=" + expectedPosition);
/*      */           
/*  867 */           setAbort(true);
/*  868 */           setErrors(getErrors() + 1);
/*  869 */           if (getErrors() > 10)
/*      */           {
/*  871 */             Players.getInstance().sendGlobalGMMessage(this.creature, " movement too high (" + (clientInput - expectedPosition) + ") at " + (
/*      */                 
/*  873 */                 getCurrx() / 4.0F) + "," + (
/*  874 */                 getCurry() / 4.0F));
/*      */           }
/*  876 */           return true;
/*      */         } 
/*      */       } 
/*      */     } 
/*  880 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void movePassengers(Vehicle aVehicle, @Nullable Creature driver, boolean isCreature) {
/*  886 */     if (isCreature) {
/*      */       
/*  888 */       for (int x = 0; x < aVehicle.seats.length; x++)
/*      */       {
/*  890 */         if ((aVehicle.seats[x]).type == 1 && aVehicle.seats[x].isOccupied()) {
/*      */           
/*      */           try {
/*      */             
/*  894 */             passenger = Players.getInstance().getPlayer((aVehicle.seats[x]).occupant);
/*  895 */             float r = (-cretVehicle.getStatus().getRotation() + 180.0F) * 3.1415927F / 180.0F;
/*  896 */             float s = (float)Math.sin(r);
/*  897 */             float c = (float)Math.cos(r);
/*  898 */             float xo = s * -(aVehicle.seats[x]).offx - c * -(aVehicle.seats[x]).offy;
/*  899 */             float yo = c * -(aVehicle.seats[x]).offx + s * -(aVehicle.seats[x]).offy;
/*  900 */             float newposx = cretVehicle.getPosX() + xo;
/*  901 */             float newposy = cretVehicle.getPosY() + yo;
/*  902 */             newposx = Math.max(3.0F, newposx);
/*  903 */             newposx = Math.min(Zones.worldMeterSizeX - 3.0F, newposx);
/*  904 */             newposy = Math.max(3.0F, newposy);
/*  905 */             newposy = Math.min(Zones.worldMeterSizeY - 3.0F, newposy);
/*  906 */             int diffx = ((int)newposx >> 2) - passenger.getTileX();
/*  907 */             int diffy = ((int)newposy >> 2) - passenger.getTileY();
/*  908 */             boolean move = true;
/*  909 */             if (diffy != 0 || diffx != 0) {
/*      */               
/*  911 */               BlockingResult result = Blocking.getBlockerBetween((Creature)passenger, passenger.getStatus()
/*  912 */                   .getPositionX(), passenger.getStatus().getPositionY(), newposx, newposy, passenger
/*  913 */                   .getPositionZ(), passenger.getPositionZ(), passenger.isOnSurface(), passenger
/*  914 */                   .isOnSurface(), false, 6, -1L, passenger.getBridgeId(), passenger
/*  915 */                   .getBridgeId(), cretVehicle.followsGround());
/*      */               
/*  917 */               if (result != null) {
/*      */                 
/*  919 */                 Blocker first = result.getFirstBlocker();
/*  920 */                 if (!first.isDoor() || (!first.canBeOpenedBy((Creature)passenger, false) && driver != null && 
/*  921 */                   !first.canBeOpenedBy(driver, false)))
/*      */                 {
/*  923 */                   if (!(first instanceof BridgePart))
/*      */                   {
/*  925 */                     if (driver != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */                       
/*  930 */                       newposx = driver.getPosX();
/*  931 */                       newposy = driver.getPosY();
/*  932 */                       diffx = ((int)newposx >> 2) - passenger.getTileX();
/*  933 */                       diffy = ((int)newposy >> 2) - passenger.getTileY();
/*      */                     }
/*      */                     else {
/*      */                       
/*  937 */                       move = false;
/*  938 */                       passenger.disembark(false);
/*      */                     } 
/*      */                   }
/*      */                 }
/*      */               } 
/*  943 */               if (move)
/*      */               {
/*  945 */                 if (passenger.getLayer() < 0)
/*      */                 {
/*  947 */                   if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile((int)newposx >> 2, (int)newposy >> 2))) || 
/*      */                     
/*  949 */                     Blocking.isDiagonalRockBetween((Creature)passenger, passenger.getTileX(), passenger
/*  950 */                       .getTileY(), (int)newposx >> 2, (int)newposy >> 2) != null)
/*      */                   {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                     
/*  959 */                     if (driver != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */                       
/*  964 */                       newposx = driver.getPosX();
/*  965 */                       newposy = driver.getPosY();
/*  966 */                       diffx = ((int)newposx >> 2) - passenger.getTileX();
/*  967 */                       diffy = ((int)newposy >> 2) - passenger.getTileY();
/*      */                     } 
/*      */                   }
/*      */                 }
/*      */               }
/*  972 */               if (passenger.getStatus().isTrading()) {
/*      */                 
/*  974 */                 Trade trade = passenger.getStatus().getTrade();
/*  975 */                 Creature lOpponent = null;
/*  976 */                 if (trade.creatureOne == passenger) {
/*  977 */                   lOpponent = trade.creatureTwo;
/*      */                 } else {
/*  979 */                   lOpponent = trade.creatureOne;
/*  980 */                 }  if (Creature.rangeTo((Creature)passenger, lOpponent) > 6)
/*  981 */                   trade.end((Creature)passenger, false); 
/*      */               } 
/*      */             } 
/*  984 */             if (move)
/*      */             {
/*      */ 
/*      */               
/*  988 */               passenger.getStatus().setPositionXYZ(newposx, newposy, cretVehicle
/*  989 */                   .getPositionZ() + (aVehicle.seats[x]).offz);
/*  990 */               passenger.getMovementScheme().setPosition(passenger.getStatus().getPositionX(), passenger
/*  991 */                   .getStatus().getPositionY(), passenger.getStatus().getPositionZ(), passenger
/*  992 */                   .getStatus().getRotation(), passenger.getLayer());
/*      */ 
/*      */               
/*  995 */               if (diffy != 0 || diffx != 0) {
/*      */                 
/*      */                 try {
/*      */                   
/*  999 */                   if (passenger.hasLink() && passenger.getVisionArea() != null) {
/*      */                     
/* 1001 */                     passenger.getVisionArea().move(diffx, diffy);
/* 1002 */                     passenger.getVisionArea().linkZones(diffy, diffx);
/*      */                   } 
/* 1004 */                   Zone z = Zones.getZone(passenger.getTileX(), passenger.getTileY(), passenger
/* 1005 */                       .isOnSurface());
/* 1006 */                   passenger.getStatus().savePosition(passenger.getWurmId(), true, z.getId(), false);
/*      */                 }
/* 1008 */                 catch (IOException iox) {
/*      */                   
/* 1010 */                   logger.log(Level.WARNING, iox.getMessage(), iox);
/* 1011 */                   passenger.setLink(false);
/*      */                 }
/* 1013 */                 catch (NoSuchZoneException nsz) {
/*      */                   
/* 1015 */                   logger.log(Level.WARNING, nsz.getMessage(), (Throwable)nsz);
/* 1016 */                   passenger.setLink(false);
/*      */                 }
/* 1018 */                 catch (Exception exception) {}
/*      */               }
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1024 */               diffx = ((int)newposx >> 2) - (passenger.getCurrentTile()).tilex;
/* 1025 */               diffy = ((int)newposy >> 2) - (passenger.getCurrentTile()).tiley;
/* 1026 */               if (diffy != 0 || diffx != 0) {
/*      */                 
/*      */                 try {
/*      */                   
/* 1030 */                   passenger.getCurrentTile()
/* 1031 */                     .creatureMoved(passenger.getWurmId(), 0.0F, 0.0F, 0.0F, diffx, diffy, true);
/*      */                 }
/* 1033 */                 catch (NoSuchPlayerException noSuchPlayerException) {
/*      */ 
/*      */                 
/* 1036 */                 } catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */               
/*      */               }
/*      */             }
/*      */           
/*      */           }
/* 1042 */           catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */         
/*      */         }
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1050 */       for (int x = 0; x < aVehicle.seats.length; x++) {
/*      */         
/* 1052 */         if ((aVehicle.seats[x]).type == 1 && aVehicle.seats[x].isOccupied()) {
/*      */           
/*      */           try {
/*      */             
/* 1056 */             passenger = Players.getInstance().getPlayer((aVehicle.seats[x]).occupant);
/* 1057 */             float r = (-itemVehicle.getRotation() + 180.0F) * 3.1415927F / 180.0F;
/* 1058 */             float s = (float)Math.sin(r);
/* 1059 */             float c = (float)Math.cos(r);
/* 1060 */             float xo = s * -(aVehicle.seats[x]).offx - c * -(aVehicle.seats[x]).offy;
/* 1061 */             float yo = c * -(aVehicle.seats[x]).offx + s * -(aVehicle.seats[x]).offy;
/* 1062 */             float newposx = itemVehicle.getPosX() + xo;
/* 1063 */             float newposy = itemVehicle.getPosY() + yo;
/* 1064 */             newposx = Math.max(3.0F, newposx);
/* 1065 */             newposx = Math.min(Zones.worldMeterSizeX - 3.0F, newposx);
/* 1066 */             newposy = Math.max(3.0F, newposy);
/* 1067 */             newposy = Math.min(Zones.worldMeterSizeY - 3.0F, newposy);
/* 1068 */             int diffx = ((int)newposx >> 2) - passenger.getTileX();
/* 1069 */             int diffy = ((int)newposy >> 2) - passenger.getTileY();
/* 1070 */             boolean move = true;
/* 1071 */             if (diffy != 0 || diffx != 0) {
/*      */ 
/*      */               
/* 1074 */               if (passenger.isOnSurface()) {
/*      */                 
/* 1076 */                 BlockingResult result = Blocking.getBlockerBetween((Creature)passenger, passenger.getStatus()
/* 1077 */                     .getPositionX(), passenger.getStatus().getPositionY(), newposx, newposy, passenger
/* 1078 */                     .getPositionZ(), passenger.getPositionZ(), passenger.isOnSurface(), passenger
/* 1079 */                     .isOnSurface(), false, 6, -1L, passenger.getBridgeId(), passenger
/* 1080 */                     .getBridgeId(), (itemVehicle.getFloorLevel() == 0 && itemVehicle
/* 1081 */                     .getBridgeId() <= 0L));
/*      */                 
/* 1083 */                 if (result != null) {
/*      */                   
/* 1085 */                   Blocker first = result.getFirstBlocker();
/* 1086 */                   if (!first.isDoor() || (!first.canBeOpenedBy((Creature)passenger, false) && driver != null && 
/* 1087 */                     !first.canBeOpenedBy(driver, false)))
/*      */                   {
/* 1089 */                     if (!(first instanceof BridgePart))
/*      */                     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                       
/* 1097 */                       if (driver != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */                         
/* 1102 */                         newposx = driver.getPosX();
/* 1103 */                         newposy = driver.getPosY();
/* 1104 */                         diffx = ((int)newposx >> 2) - passenger.getTileX();
/* 1105 */                         diffy = ((int)newposy >> 2) - passenger.getTileY();
/*      */                       }
/*      */                       else {
/*      */                         
/* 1109 */                         move = false;
/* 1110 */                         passenger.disembark(false);
/*      */                       } 
/*      */                     }
/*      */                   }
/*      */                 } 
/*      */               } 
/*      */               
/* 1117 */               if (move)
/*      */               {
/* 1119 */                 if (passenger.getLayer() < 0)
/*      */                 {
/* 1121 */                   if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile((int)newposx >> 2, (int)newposy >> 2))) || 
/*      */                     
/* 1123 */                     Blocking.isDiagonalRockBetween((Creature)passenger, passenger.getTileX(), passenger
/* 1124 */                       .getTileY(), (int)newposx >> 2, (int)newposy >> 2) != null)
/*      */                   {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                     
/* 1132 */                     if (driver != null) {
/*      */                       
/* 1134 */                       newposx = driver.getPosX();
/* 1135 */                       newposy = driver.getPosY();
/* 1136 */                       diffx = ((int)newposx >> 2) - passenger.getTileX();
/* 1137 */                       diffy = ((int)newposy >> 2) - passenger.getTileY();
/*      */                     } 
/*      */                   }
/*      */                 }
/*      */               }
/* 1142 */               if (passenger.getStatus().isTrading()) {
/*      */                 
/* 1144 */                 Trade trade = passenger.getStatus().getTrade();
/* 1145 */                 Creature lOpponent = null;
/* 1146 */                 if (trade.creatureOne == passenger) {
/* 1147 */                   lOpponent = trade.creatureTwo;
/*      */                 } else {
/* 1149 */                   lOpponent = trade.creatureOne;
/* 1150 */                 }  if (Creature.rangeTo((Creature)passenger, lOpponent) > 6)
/* 1151 */                   trade.end((Creature)passenger, false); 
/*      */               } 
/*      */             } 
/* 1154 */             if (move)
/*      */             {
/* 1156 */               passenger.getStatus().setPositionXYZ(newposx, newposy, itemVehicle
/* 1157 */                   .getPosZ() + (aVehicle.seats[x]).offz);
/*      */ 
/*      */               
/* 1160 */               passenger.getMovementScheme().setPosition(passenger.getStatus().getPositionX(), passenger
/* 1161 */                   .getStatus().getPositionY(), passenger.getStatus().getPositionZ(), passenger
/* 1162 */                   .getStatus().getRotation(), passenger.getLayer());
/*      */ 
/*      */               
/* 1165 */               if (diffy != 0 || diffx != 0) {
/*      */                 
/*      */                 try {
/*      */                   
/* 1169 */                   if (passenger.hasLink() && passenger.getVisionArea() != null) {
/*      */                     
/* 1171 */                     passenger.getVisionArea().move(diffx, diffy);
/* 1172 */                     passenger.getVisionArea().linkZones(diffy, diffx);
/*      */                   } 
/* 1174 */                   Zone z = Zones.getZone(passenger.getTileX(), passenger.getTileY(), passenger
/* 1175 */                       .isOnSurface());
/* 1176 */                   passenger.getStatus().savePosition(passenger.getWurmId(), true, z.getId(), false);
/*      */                 }
/* 1178 */                 catch (IOException iox) {
/*      */                   
/* 1180 */                   passenger.setLink(false);
/*      */                 }
/* 1182 */                 catch (NoSuchZoneException nsz) {
/*      */                   
/* 1184 */                   passenger.setLink(false);
/*      */                 }
/* 1186 */                 catch (Exception exception) {}
/*      */               }
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1192 */               diffx = ((int)newposx >> 2) - (passenger.getCurrentTile()).tilex;
/* 1193 */               diffy = ((int)newposy >> 2) - (passenger.getCurrentTile()).tiley;
/* 1194 */               if (diffy != 0 || diffx != 0) {
/*      */                 
/*      */                 try {
/*      */                   
/* 1198 */                   passenger.getCurrentTile()
/* 1199 */                     .creatureMoved(passenger.getWurmId(), 0.0F, 0.0F, 0.0F, diffx, diffy, true);
/*      */                 }
/* 1201 */                 catch (NoSuchPlayerException noSuchPlayerException) {
/*      */ 
/*      */                 
/* 1204 */                 } catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */               
/*      */               }
/*      */             }
/*      */           
/*      */           }
/* 1210 */           catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void moveVehicle(float diffX, float diffY, float diffZ) {
/* 1220 */     if (vehic.isChair()) {
/*      */       return;
/*      */     }
/* 1223 */     if (vehic.creature) {
/*      */ 
/*      */       
/*      */       try {
/* 1227 */         cretVehicle = Creatures.getInstance().getCreature(vehic.wurmid);
/* 1228 */         if (this.creature.isOnSurface() == cretVehicle.isOnSurface()) {
/*      */           
/* 1230 */           VolaTile t = Zones.getTileOrNull(cretVehicle.getTileX(), cretVehicle.getTileY(), cretVehicle
/* 1231 */               .isOnSurface());
/* 1232 */           if (t == null) {
/*      */ 
/*      */             
/*      */             try {
/*      */ 
/*      */ 
/*      */               
/* 1239 */               Zone z = Zones.getZone(cretVehicle.getTileX(), cretVehicle.getTileY(), cretVehicle
/* 1240 */                   .isOnSurface());
/* 1241 */               z.removeCreature(cretVehicle, false, false);
/* 1242 */               z.addCreature(vehic.wurmid);
/*      */             }
/* 1244 */             catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */             
/* 1247 */             this.creature.disembark(true);
/*      */             
/*      */             return;
/*      */           } 
/*      */           
/* 1252 */           Seat driverseat = vehic.getPilotSeat();
/*      */           
/* 1254 */           float diffrot = Creature.normalizeAngle(getVehicleRotation()) - cretVehicle.getStatus().getRotation();
/* 1255 */           cretVehicle.setRotation(Creature.normalizeAngle(getVehicleRotation()));
/* 1256 */           float _r = (-cretVehicle.getStatus().getRotation() + 180.0F) * 3.1415927F / 180.0F;
/* 1257 */           float _s = (float)Math.sin(_r);
/* 1258 */           float _c = (float)Math.cos(_r);
/* 1259 */           float xo = _s * -driverseat.offx - _c * -driverseat.offy;
/* 1260 */           float yo = _c * -driverseat.offx + _s * -driverseat.offy;
/* 1261 */           float nPosX = this.creature.getPosX() - xo;
/* 1262 */           float nPosY = this.creature.getPosY() - yo;
/* 1263 */           float nPosZ = this.creature.getPositionZ() - driverseat.offz;
/*      */           
/* 1265 */           nPosX = Math.max(3.0F, nPosX);
/* 1266 */           nPosX = Math.min(Zones.worldMeterSizeX - 3.0F, nPosX);
/* 1267 */           nPosY = Math.max(3.0F, nPosY);
/* 1268 */           nPosY = Math.min(Zones.worldMeterSizeY - 3.0F, nPosY);
/*      */           
/* 1270 */           if (!cretVehicle.isOnSurface())
/*      */           {
/* 1272 */             if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh
/* 1273 */                   .getTile((int)nPosX >> 2, (int)nPosY >> 2))))
/*      */             {
/* 1275 */               if (Tiles.decodeType(Server.caveMesh.getTile(cretVehicle.getTileX(), cretVehicle
/* 1276 */                     .getTileY())) != 201) {
/*      */                 return;
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1285 */           diffX = nPosX - cretVehicle.getPosX();
/* 1286 */           diffY = nPosY - cretVehicle.getPosY();
/* 1287 */           if (diffX != 0.0F || diffY != 0.0F) {
/*      */             
/* 1289 */             int dtx = ((int)nPosX >> 2) - cretVehicle.getTileX();
/* 1290 */             int dty = ((int)nPosY >> 2) - cretVehicle.getTileY();
/* 1291 */             cretVehicle.setPositionX(nPosX);
/* 1292 */             cretVehicle.setPositionY(nPosY);
/* 1293 */             cretVehicle.setPositionZ(nPosZ);
/*      */             
/*      */             try {
/* 1296 */               cretVehicle.getVisionArea().move(dtx, dty);
/* 1297 */               cretVehicle.getVisionArea().linkZones(dtx, dty);
/*      */             }
/* 1299 */             catch (IOException iOException) {}
/*      */ 
/*      */ 
/*      */             
/* 1303 */             t.creatureMoved(vehic.wurmid, diffX, diffY, diffZ, dtx, dty);
/*      */           }
/* 1305 */           else if (diffrot != 0.0F) {
/* 1306 */             t.creatureMoved(vehic.wurmid, 0.0F, 0.0F, diffZ, 0, 0);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1311 */           Zone zone = null;
/*      */           
/*      */           try {
/* 1314 */             zone = Zones.getZone(cretVehicle.getTileX(), cretVehicle.getTileY(), cretVehicle.isOnSurface());
/* 1315 */             zone.removeCreature(cretVehicle, false, false);
/*      */           }
/* 1317 */           catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */           
/* 1320 */           Seat driverseat = vehic.getPilotSeat();
/*      */           
/* 1322 */           cretVehicle.setRotation(Creature.normalizeAngle(getVehicleRotation()));
/* 1323 */           float _r = (-cretVehicle.getStatus().getRotation() + 180.0F) * 3.1415927F / 180.0F;
/* 1324 */           float _s = (float)Math.sin(_r);
/* 1325 */           float _c = (float)Math.cos(_r);
/* 1326 */           float xo = _s * -driverseat.offx - _c * -driverseat.offy;
/* 1327 */           float yo = _c * -driverseat.offx + _s * -driverseat.offy;
/* 1328 */           float nPosX = cretVehicle.getPosX() - xo;
/* 1329 */           float nPosY = cretVehicle.getPosY() - yo;
/* 1330 */           nPosX = Math.max(3.0F, nPosX);
/* 1331 */           nPosX = Math.min(Zones.worldMeterSizeX - 3.0F, nPosX);
/* 1332 */           nPosY = Math.max(3.0F, nPosY);
/* 1333 */           nPosY = Math.min(Zones.worldMeterSizeY - 3.0F, nPosY);
/* 1334 */           cretVehicle.setPositionX(nPosX);
/* 1335 */           cretVehicle.setPositionY(nPosY);
/*      */           
/* 1337 */           cretVehicle.setLayer(this.creature.getLayer(), false);
/*      */           
/*      */           try {
/* 1340 */             zone = Zones.getZone(cretVehicle.getTileX(), cretVehicle.getTileY(), this.creature.isOnSurface());
/* 1341 */             zone.addCreature(cretVehicle.getWurmId());
/*      */           }
/* 1343 */           catch (NoSuchZoneException noSuchZoneException) {}
/*      */         } 
/*      */ 
/*      */         
/* 1347 */         movePassengers(vehic, this.creature, true);
/*      */       }
/* 1349 */       catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */       
/* 1352 */       } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1361 */         itemVehicle = Items.getItem(vehic.wurmid);
/* 1362 */         if (this.creature.isOnSurface() == itemVehicle.isOnSurface()) {
/*      */           
/* 1364 */           moveItemVehicleSameLevel();
/*      */         }
/*      */         else {
/*      */           
/* 1368 */           moveItemVehicleOtherLevel();
/*      */         } 
/* 1370 */         movePassengers(vehic, this.creature, false);
/*      */       }
/* 1372 */       catch (NoSuchItemException noSuchItemException) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void moveItemVehicleOtherLevel() {
/* 1380 */     Seat driverseat = vehic.getPilotSeat();
/* 1381 */     float _r = (-itemVehicle.getRotation() + 180.0F) * 3.1415927F / 180.0F;
/* 1382 */     float _s = (float)Math.sin(_r);
/* 1383 */     float _c = (float)Math.cos(_r);
/* 1384 */     float xo = _s * -driverseat.offx - _c * -driverseat.offy;
/* 1385 */     float yo = _c * -driverseat.offx + _s * -driverseat.offy;
/* 1386 */     float nPosX = itemVehicle.getPosX() - xo;
/* 1387 */     float nPosY = itemVehicle.getPosY() - yo;
/* 1388 */     nPosX = Math.max(3.0F, nPosX);
/* 1389 */     nPosX = Math.min(Zones.worldMeterSizeX - 3.0F, nPosX);
/* 1390 */     nPosY = Math.max(3.0F, nPosY);
/* 1391 */     nPosY = Math.min(Zones.worldMeterSizeY - 3.0F, nPosY);
/*      */ 
/*      */     
/* 1394 */     if (!this.creature.isOnSurface()) {
/*      */       
/* 1396 */       int caveTile = Server.caveMesh.getTile((int)nPosX >> 2, (int)nPosY >> 2);
/* 1397 */       if (Tiles.isSolidCave(Tiles.decodeType(caveTile))) {
/*      */         
/* 1399 */         moveItemVehicleSameLevel();
/*      */         return;
/*      */       } 
/*      */     } 
/* 1403 */     Zone zone = null;
/* 1404 */     itemVehicle.newLayer = (byte)this.creature.getLayer();
/*      */ 
/*      */     
/*      */     try {
/* 1408 */       zone = Zones.getZone((int)itemVehicle.getPosX() >> 2, (int)itemVehicle.getPosY() >> 2, itemVehicle.isOnSurface());
/* 1409 */       zone.removeItem(itemVehicle, true, true);
/*      */     }
/* 1411 */     catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */     
/* 1414 */     itemVehicle.setPosXY(nPosX, nPosY);
/*      */     
/*      */     try {
/* 1417 */       zone = Zones.getZone((int)itemVehicle.getPosX() >> 2, (int)itemVehicle.getPosY() >> 2, (itemVehicle.newLayer >= 0));
/*      */       
/* 1419 */       zone.addItem(itemVehicle, false, false, false);
/*      */ 
/*      */     
/*      */     }
/* 1423 */     catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */     
/* 1426 */     itemVehicle.newLayer = Byte.MIN_VALUE;
/* 1427 */     Seat[] seats = vehic.hitched;
/* 1428 */     if (seats != null)
/*      */     {
/* 1430 */       for (int x = 0; x < seats.length; x++) {
/*      */         
/* 1432 */         if (seats[x] != null)
/*      */         {
/* 1434 */           if ((seats[x]).occupant != -10L) {
/*      */             
/*      */             try {
/*      */               
/* 1438 */               Creature c = Server.getInstance().getCreature((seats[x]).occupant);
/* 1439 */               c.getStatus().setLayer(itemVehicle.isOnSurface() ? 0 : -1);
/* 1440 */               c.getCurrentTile().newLayer(c);
/*      */             }
/* 1442 */             catch (NoSuchPlayerException noSuchPlayerException) {
/*      */ 
/*      */             
/* 1445 */             } catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */           }
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 1452 */     Seat[] pseats = vehic.seats;
/* 1453 */     if (pseats != null)
/*      */     {
/* 1455 */       for (int x = 0; x < pseats.length; x++) {
/*      */         
/* 1457 */         if (x > 0)
/*      */         {
/*      */           
/* 1460 */           if (pseats[x] != null)
/*      */           {
/* 1462 */             if ((pseats[x]).occupant != -10L) {
/*      */               
/*      */               try {
/*      */ 
/*      */                 
/* 1467 */                 Creature c = Server.getInstance().getCreature((pseats[x]).occupant);
/* 1468 */                 logger.log(Level.INFO, c.getName() + " Setting to new layer " + (
/* 1469 */                     itemVehicle.isOnSurface() ? 0 : -1));
/* 1470 */                 c.getStatus().setLayer(itemVehicle.isOnSurface() ? 0 : -1);
/* 1471 */                 c.getCurrentTile().newLayer(c);
/* 1472 */                 if (c.isPlayer())
/*      */                 {
/* 1474 */                   if (itemVehicle.isOnSurface()) {
/*      */ 
/*      */                     
/* 1477 */                     c.getCommunicator().sendNormalServerMessage("You leave the cave.");
/*      */                   }
/*      */                   else {
/*      */                     
/* 1481 */                     c.getCommunicator().sendNormalServerMessage("You enter the cave.");
/* 1482 */                     if (c.getVisionArea() != null) {
/* 1483 */                       c.getVisionArea().initializeCaves();
/*      */                     }
/*      */                   } 
/*      */                 }
/* 1487 */               } catch (NoSuchPlayerException noSuchPlayerException) {
/*      */ 
/*      */               
/* 1490 */               } catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */             }
/*      */           }
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1500 */   static Creature toRemove = null;
/*      */   
/*      */   public void moveItemVehicleSameLevel() {
/* 1503 */     VolaTile t = Zones.getTileOrNull(itemVehicle.getTileX(), itemVehicle.getTileY(), itemVehicle.isOnSurface());
/* 1504 */     if (t == null) {
/*      */ 
/*      */       
/*      */       try {
/* 1508 */         Zone z = Zones.getZone(itemVehicle.getTileX(), itemVehicle.getTileY(), itemVehicle.isOnSurface());
/* 1509 */         z.removeItem(itemVehicle);
/* 1510 */         z.addItem(itemVehicle);
/*      */       }
/* 1512 */       catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */       
/* 1515 */       this.creature.disembark(true);
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1520 */     Seat driverseat = vehic.getPilotSeat();
/* 1521 */     if (driverseat == null) {
/*      */       
/* 1523 */       logger.warning("Driverseat null for " + this.creature.getName());
/* 1524 */       this.creature.disembark(true);
/*      */       
/*      */       return;
/*      */     } 
/* 1528 */     float _r = (-itemVehicle.getRotation() + 180.0F) * 3.1415927F / 180.0F;
/* 1529 */     float _s = (float)Math.sin(_r);
/* 1530 */     float _c = (float)Math.cos(_r);
/* 1531 */     float xo = _s * -driverseat.offx - _c * -driverseat.offy;
/* 1532 */     float yo = _c * -driverseat.offx + _s * -driverseat.offy;
/* 1533 */     float nPosX = this.creature.getPosX() - xo;
/* 1534 */     float nPosY = this.creature.getPosY() - yo;
/* 1535 */     float nPosZ = this.creature.getPositionZ() - driverseat.offz;
/*      */     
/* 1537 */     nPosX = Math.max(3.0F, nPosX);
/* 1538 */     nPosX = Math.min(Zones.worldMeterSizeX - 3.0F, nPosX);
/* 1539 */     nPosY = Math.max(3.0F, nPosY);
/* 1540 */     nPosY = Math.min(Zones.worldMeterSizeY - 3.0F, nPosY);
/* 1541 */     int diffdecx = (int)(nPosX * 100.0F - itemVehicle.getPosX() * 100.0F);
/* 1542 */     int diffdecy = (int)(nPosY * 100.0F - itemVehicle.getPosY() * 100.0F);
/* 1543 */     if (diffdecx != 0 || diffdecy != 0) {
/*      */       
/* 1545 */       nPosX = itemVehicle.getPosX() + diffdecx * 0.01F;
/* 1546 */       nPosY = itemVehicle.getPosY() + diffdecy * 0.01F;
/*      */     } 
/* 1548 */     if (!itemVehicle.isOnSurface())
/*      */     {
/* 1550 */       if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile((int)nPosX >> 2, (int)nPosY >> 2))))
/*      */       {
/* 1552 */         if (Tiles.decodeType(Server.caveMesh.getTile((int)itemVehicle.getPosX() >> 2, 
/* 1553 */               (int)itemVehicle.getPosY() >> 2)) != 201) {
/*      */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1559 */     t.moveItem(itemVehicle, nPosX, nPosY, nPosZ, Creature.normalizeAngle(getVehicleRotation()), itemVehicle
/* 1560 */         .isOnSurface(), itemVehicle.getPosZ());
/*      */     
/* 1562 */     if (vehic.draggers != null) {
/*      */ 
/*      */       
/* 1565 */       for (Creature c : vehic.draggers) {
/*      */         
/* 1567 */         if (c.isDead()) {
/* 1568 */           toRemove = c; continue;
/*      */         } 
/* 1570 */         moveDragger(c);
/*      */       } 
/*      */       
/* 1573 */       if (toRemove != null) {
/*      */         
/* 1575 */         vehic.removeDragger(toRemove);
/* 1576 */         toRemove = null;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void moveDragger(Creature c) {
/* 1584 */     Vehicle v = c.getHitched();
/* 1585 */     Seat seat = v.getHitchSeatFor(c.getWurmId());
/* 1586 */     float _r = (-itemVehicle.getRotation() + 180.0F) * 3.1415927F / 180.0F;
/* 1587 */     float _s = (float)Math.sin(_r);
/* 1588 */     float _c = (float)Math.cos(_r);
/* 1589 */     float xo = _s * -seat.offx - _c * -seat.offy;
/* 1590 */     float yo = _c * -seat.offx + _s * -seat.offy;
/* 1591 */     float nPosX = this.creature.getPosX() + xo;
/* 1592 */     float nPosY = this.creature.getPosY() + yo;
/*      */     
/* 1594 */     nPosX = Math.max(3.0F, nPosX);
/* 1595 */     nPosX = Math.min(Zones.worldMeterSizeX - 3.0F, nPosX);
/* 1596 */     nPosY = Math.max(3.0F, nPosY);
/* 1597 */     nPosY = Math.min(Zones.worldMeterSizeY - 3.0F, nPosY);
/* 1598 */     int diffdecx = (int)(nPosX * 100.0F - itemVehicle.getPosX() * 100.0F);
/* 1599 */     int diffdecy = (int)(nPosY * 100.0F - itemVehicle.getPosY() * 100.0F);
/* 1600 */     if (diffdecx != 0 || diffdecy != 0) {
/*      */       
/* 1602 */       nPosX = itemVehicle.getPosX() + diffdecx * 0.01F;
/* 1603 */       nPosY = itemVehicle.getPosY() + diffdecy * 0.01F;
/*      */     } 
/*      */     
/* 1606 */     moveDragger(c, nPosX, nPosY, itemVehicle.getPosZ(), Creature.normalizeAngle(getVehicleRotation()), false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void moveDragger(Creature c, float nPosX, float nPosY, float nPosZ, float newRot, boolean addRemove) {
/* 1612 */     int diffx = ((int)nPosX >> 2) - (c.getCurrentTile()).tilex;
/* 1613 */     int diffy = ((int)nPosY >> 2) - (c.getCurrentTile()).tiley;
/* 1614 */     if (c.getLayer() < 0 && itemVehicle.isOnSurface())
/*      */     {
/* 1616 */       if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile((int)nPosX >> 2, (int)nPosY >> 2)))) {
/*      */         
/* 1618 */         c.setLayer(0, false);
/*      */       }
/*      */       else {
/*      */         
/* 1622 */         byte typeSurf = Tiles.decodeType(Server.surfaceMesh.getTile((int)nPosX >> 2, (int)nPosY >> 2));
/* 1623 */         if (typeSurf != 0)
/*      */         {
/* 1625 */           c.setLayer(0, false);
/*      */         }
/*      */       } 
/*      */     }
/* 1629 */     c.getStatus().setPositionXYZ(nPosX, nPosY, nPosZ);
/*      */     
/* 1631 */     c.getMovementScheme().setPosition(c.getStatus().getPositionX(), c.getStatus().getPositionY(), c
/* 1632 */         .getStatus().getPositionZ(), newRot, c.getLayer());
/* 1633 */     if (Math.abs(c.getStatus().getRotation() - newRot) > 10.0F) {
/*      */       
/* 1635 */       c.setRotation(newRot);
/* 1636 */       c.moved(0.0F, 0.0F, 0.0F, 0, 0);
/*      */     } 
/* 1638 */     if (diffy != 0 || diffx != 0) {
/*      */       
/*      */       try {
/*      */         
/* 1642 */         if (c.getVisionArea() != null) {
/*      */           
/* 1644 */           c.getVisionArea().move(diffx, diffy);
/* 1645 */           c.getVisionArea().linkZones(diffy, diffx);
/*      */         } 
/* 1647 */         Zone z = Zones.getZone(c.getTileX(), c.getTileY(), c.isOnSurface());
/* 1648 */         c.getStatus().savePosition(c.getWurmId(), c.isPlayer(), z.getId(), true);
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/* 1653 */           c.getCurrentTile().creatureMoved(c.getWurmId(), 0.0F, 0.0F, 0.0F, diffx, diffy, true);
/*      */         }
/* 1655 */         catch (NoSuchPlayerException noSuchPlayerException) {
/*      */ 
/*      */         
/*      */         }
/* 1659 */         catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 1665 */       catch (IOException iOException) {
/*      */ 
/*      */       
/* 1668 */       } catch (NoSuchZoneException noSuchZoneException) {}
/*      */     }
/*      */ 
/*      */     
/* 1672 */     if (addRemove) {
/*      */ 
/*      */       
/* 1675 */       diffx = ((int)nPosX >> 2) - (passenger.getCurrentTile()).tilex;
/* 1676 */       diffy = ((int)nPosY >> 2) - (passenger.getCurrentTile()).tiley;
/* 1677 */       if (diffy != 0 || diffx != 0) {
/*      */         
/*      */         try {
/*      */           
/* 1681 */           passenger.getCurrentTile().creatureMoved(passenger.getWurmId(), 0.0F, 0.0F, 0.0F, diffx, diffy, true);
/*      */         }
/* 1683 */         catch (NoSuchPlayerException noSuchPlayerException) {
/*      */ 
/*      */         
/* 1686 */         } catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void move(float diffX, float diffY, float diffZ) {
/* 1695 */     vehic = null;
/* 1696 */     if (!isIntraTeleporting()) {
/*      */       
/* 1698 */       int weight = this.creature.getCarriedWeight();
/* 1699 */       if (this.draggedItem != null) {
/* 1700 */         weight += this.draggedItem.getWeightGrams() / 100;
/*      */       }
/* 1702 */       double moveDiff = Math.sqrt((diffX * diffX + diffY * diffY)) * 15.0D;
/* 1703 */       double beforeZMod = moveDiff;
/* 1704 */       if (isClimbing() && this.creature.getVehicle() == -10L && !this.creature.isUsingLastGasp()) {
/*      */ 
/*      */         
/* 1707 */         if (weight <= 10000)
/* 1708 */           weight = 10000; 
/* 1709 */         if (this.creature.getLayer() <= 0) {
/*      */           
/* 1711 */           short[] steepness = Creature.getTileSteepness(this.creature.getTileX(), this.creature.getTileY(), this.creature
/* 1712 */               .isOnSurface());
/* 1713 */           if (diffZ != 0.0F && (steepness[1] > 23 || steepness[1] < -23)) {
/*      */             
/* 1715 */             float gsbon = this.creature.getBonusForSpellEffect((byte)38) / 20.0F;
/* 1716 */             if (gsbon > 0.0F) {
/* 1717 */               moveDiff += Math.max(1.0F, Math.abs(diffZ) * 500.0F / Math.max(1.0F, gsbon));
/*      */             } else {
/* 1719 */               moveDiff += Math.max(1.0F, Math.abs(diffZ) * 500.0F) / Math.max(1.0D, Math.pow((this.climbSkill / 10), 0.3D));
/* 1720 */             }  if (Server.rand.nextInt(this.climbSkill * 2) == 0) {
/*      */               
/* 1722 */               this.climbSkill = Math.max(10, (int)this.creature.getClimbingSkill().getKnowledge(0.0D));
/*      */               
/* 1724 */               if (this.creature.getStatus().getStamina() < 10000) {
/*      */                 
/* 1726 */                 int stam = (int)(101.0F - Math.max(10.0F, this.creature.getStatus().getStamina() / 100.0F));
/* 1727 */                 if (gsbon <= 0.0F || (this.creature.getStatus().getStamina() < 25 && Server.rand.nextInt(this.climbSkill) == 0)) if (this.creature
/* 1728 */                     .getClimbingSkill().skillCheck(stam, 0.0D, 
/* 1729 */                       (this.creature.getStatus().getStamina() < 25), 
/* 1730 */                       Math.max(1, this.climbSkill / 20)) < 0.0D) {
/*      */                     
/*      */                     try {
/*      */                       
/* 1734 */                       this.creature.getCommunicator().sendNormalServerMessage("You need to catch your breath, and stop climbing.");
/*      */                       
/* 1736 */                       this.creature.setClimbing(false);
/* 1737 */                       this.creature.getCommunicator().sendToggle(0, this.creature.isClimbing());
/*      */                     }
/* 1739 */                     catch (IOException iox) {
/*      */                       
/* 1741 */                       logger.log(Level.WARNING, this.creature.getName() + ' ' + iox.getMessage(), iox);
/*      */                     } 
/*      */                   }
/*      */               
/*      */               } else {
/*      */                 
/* 1747 */                 this.creature.getClimbingSkill().skillCheck(Math.max((-this.climbSkill / 2), Math.min(this.climbSkill * 1.25F, (steepness[1] - this.climbSkill * 2))), 0.0D, (gsbon > 0.0F), 
/* 1748 */                     Math.max(1, this.climbSkill / 20));
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/* 1753 */       } else if (diffZ < 0.0F) {
/* 1754 */         moveDiff += Math.max(-moveDiff, Math.min(-1.0F, diffZ * 100.0F));
/* 1755 */       } else if (diffZ != 0.0F && moveDiff > 0.0D) {
/* 1756 */         moveDiff += Math.max(0.5D, (diffZ * 50.0F));
/*      */       } 
/* 1758 */       if (this.creature.isStealth()) {
/*      */         
/* 1760 */         if (weight <= 10000)
/* 1761 */           weight = 10000; 
/* 1762 */         weight += 5000;
/*      */       } 
/*      */       
/* 1765 */       if (diffX != 0.0F || diffY != 0.0F || diffZ != 0.0F) {
/*      */         
/* 1767 */         this.creature.getStatus().setMoving(true);
/*      */         
/* 1769 */         MeshIO mesh = Server.surfaceMesh;
/* 1770 */         if (this.creature.getLayer() < 0)
/* 1771 */           mesh = Server.caveMesh; 
/* 1772 */         int tile = mesh.getTile((this.creature.getCurrentTile()).tilex, (this.creature.getCurrentTile()).tiley);
/* 1773 */         if (this.creature.isPlayer()) {
/*      */ 
/*      */           
/* 1776 */           short height = Tiles.decodeHeight(tile);
/* 1777 */           if (height > 21800) {
/*      */             
/* 1779 */             if (!this.m2180m)
/*      */             {
/* 1781 */               this.m2180m = true;
/* 1782 */               this.creature.achievement(81);
/*      */             }
/*      */           
/* 1785 */           } else if (height > 14000) {
/*      */             
/* 1787 */             if (!this.m1400m)
/*      */             {
/* 1789 */               this.m1400m = true;
/* 1790 */               this.creature.achievement(80);
/*      */             }
/*      */           
/* 1793 */           } else if (height > 7000) {
/*      */             
/* 1795 */             if (!this.m700m)
/*      */             {
/* 1797 */               this.m700m = true;
/* 1798 */               this.creature.achievement(79);
/*      */             }
/*      */           
/* 1801 */           } else if (height > 3000) {
/*      */             
/* 1803 */             if (!this.m300m)
/*      */             {
/* 1805 */               this.m300m = true;
/* 1806 */               this.creature.achievement(78);
/*      */             }
/*      */           
/* 1809 */           } else if (height < -300) {
/*      */             
/* 1811 */             if (!this.outAtSea) {
/*      */               
/* 1813 */               this.outAtSea = true;
/* 1814 */               this.creature.achievement(77);
/*      */             } 
/*      */           } 
/*      */         } 
/* 1818 */         if (this.creature.isStealth())
/*      */         {
/* 1820 */           if (Terraforming.isRoad(Tiles.decodeType(tile)))
/*      */           {
/* 1822 */             if (Server.rand.nextInt(20) == 0)
/* 1823 */               this.creature.setStealth(false); 
/*      */           }
/*      */         }
/* 1826 */         VolaTile vtile = Zones.getTileOrNull((this.creature.getCurrentTile()).tilex, (this.creature.getCurrentTile()).tiley, this.creature
/* 1827 */             .isOnSurface());
/* 1828 */         if (vtile != null)
/*      */         {
/* 1830 */           if (this.creature.getPower() < 2 || this.creature.loggerCreature1 > 0L)
/*      */           {
/* 1832 */             if (vtile.hasOnePerTileItem(this.creature.getFloorLevel()))
/*      */             {
/* 1834 */               if (Servers.localServer.PVPSERVER) {
/*      */ 
/*      */                 
/* 1837 */                 Item barrier = vtile.getOnePerTileItem(this.creature.getFloorLevel());
/* 1838 */                 if (barrier != null)
/*      */                 {
/* 1840 */                   if (barrier.getTemplateId() == 938)
/*      */                   {
/* 1842 */                     if (this.creature.getFarwalkerSeconds() <= 0) {
/*      */ 
/*      */                       
/* 1845 */                       Rectangle rect = new Rectangle((this.creature.getCurrentTile()).tilex * 4, (this.creature.getCurrentTile()).tiley * 4 + 1, 4, 1);
/* 1846 */                       AffineTransform transform = new AffineTransform();
/* 1847 */                       transform.rotate((barrier.getRotation() - 90.0F), rect.getX() + (rect.width / 2), rect
/* 1848 */                           .getY() + (rect.height / 2));
/*      */                       
/* 1850 */                       Shape transformed = transform.createTransformedShape(rect);
/*      */ 
/*      */                       
/* 1853 */                       if (transformed.contains(this.creature.getPosX(), this.creature.getPosY())) {
/*      */ 
/*      */                         
/* 1856 */                         Wound wound = null;
/* 1857 */                         boolean dead = false;
/*      */                         
/*      */                         try {
/* 1860 */                           byte pos = this.creature.getBody().getRandomWoundPos();
/* 1861 */                           if (Server.rand.nextInt(10) <= 6)
/*      */                           {
/* 1863 */                             if (this.creature.getBody().getWounds() != null) {
/*      */                               
/* 1865 */                               wound = this.creature.getBody().getWounds().getWoundAtLocation(pos);
/* 1866 */                               if (wound != null) {
/*      */ 
/*      */                                 
/* 1869 */                                 dead = wound.modifySeverity(
/* 1870 */                                     (int)(1000.0F + Server.rand.nextInt(4000) * (100.0F - this.creature.getSpellDamageProtectBonus()) / 100.0F));
/*      */                                 
/* 1872 */                                 wound.setBandaged(false);
/* 1873 */                                 this.creature.setWounded();
/*      */                               } 
/* 1875 */                               barrier.setDamage(barrier.getDamage() + Server.rand.nextFloat() * 5.0F);
/*      */                             } 
/*      */                           }
/* 1878 */                           if (wound == null)
/*      */                           {
/* 1880 */                             dead = this.creature.addWoundOfType(null, (byte)2, pos, false, 1.0F, true, (500.0F + Server.rand
/*      */                                 
/* 1882 */                                 .nextInt(6000) * (100.0F - this.creature
/* 1883 */                                 .getSpellDamageProtectBonus()) / 100.0F), 0.0F, 0.0F, false, false);
/*      */                           }
/* 1885 */                           Server.getInstance().broadCastAction(this.creature
/* 1886 */                               .getNameWithGenus() + " is pierced by the barrier.", this.creature, 2);
/* 1887 */                           this.creature.getCommunicator().sendAlertServerMessage("You are pierced by the barrier!");
/*      */                           
/* 1889 */                           if (dead) {
/*      */                             
/* 1891 */                             this.creature.achievement(143);
/*      */                             
/*      */                             return;
/*      */                           } 
/* 1895 */                         } catch (Exception exception) {}
/*      */                       } 
/*      */                     } 
/*      */                   }
/*      */                 }
/*      */               } 
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */         
/* 1906 */         if (Tiles.decodeType(tile) == Tiles.Tile.TILE_LAVA.id) {
/*      */ 
/*      */           
/* 1909 */           if (this.creature.getPower() < 1)
/*      */           {
/* 1911 */             if (this.creature.getDeity() == null || !this.creature.getDeity().isMountainGod() || this.creature.getFaith() < 35.0F)
/*      */             {
/* 1913 */               if (this.creature.getFarwalkerSeconds() <= 0)
/*      */               {
/* 1915 */                 if (Server.rand.nextInt(10) == 0)
/*      */                 {
/* 1917 */                   Wound wound = null;
/* 1918 */                   boolean dead = false;
/*      */                   
/*      */                   try {
/* 1921 */                     byte pos = 15;
/* 1922 */                     if (Server.rand.nextBoolean() == true)
/* 1923 */                       pos = 16; 
/* 1924 */                     if (Server.rand.nextInt(10) <= 6)
/*      */                     {
/* 1926 */                       if (this.creature.getBody().getWounds() != null) {
/*      */                         
/* 1928 */                         wound = this.creature.getBody().getWounds().getWoundAtLocation(pos);
/* 1929 */                         if (wound != null) {
/*      */                           
/* 1931 */                           dead = wound.modifySeverity(
/* 1932 */                               (int)(1000.0F + Server.rand.nextInt(4000) * (100.0F - this.creature.getSpellDamageProtectBonus()) / 100.0F));
/* 1933 */                           wound.setBandaged(false);
/* 1934 */                           this.creature.setWounded();
/*      */                         } 
/*      */                       } 
/*      */                     }
/* 1938 */                     if (wound == null)
/*      */                     {
/* 1940 */                       if (this.creature.isPlayer())
/*      */                       {
/* 1942 */                         dead = this.creature.addWoundOfType(null, (byte)4, pos, false, 1.0F, true, (1000.0F + Server.rand
/* 1943 */                             .nextInt(4000) * (100.0F - this.creature.getSpellDamageProtectBonus()) / 100.0F), 0.0F, 0.0F, false, false);
/*      */                       }
/*      */                     }
/*      */                     
/* 1947 */                     this.creature.getCommunicator().sendAlertServerMessage("You are burnt by lava!");
/* 1948 */                     if (dead) {
/*      */                       
/* 1950 */                       this.creature.achievement(142);
/*      */                       
/*      */                       return;
/*      */                     } 
/* 1954 */                   } catch (Exception exception) {}
/*      */                 
/*      */                 }
/*      */               
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/* 1962 */         else if (this.creature.getDeity() == null || !this.creature.getDeity().isForestGod() || this.creature.getFaith() < 35.0F) {
/*      */ 
/*      */           
/* 1965 */           if (this.creature.getPower() < 1)
/*      */           {
/* 1967 */             Tiles.Tile theTile = Tiles.getTile(Tiles.decodeType(tile));
/* 1968 */             if (theTile.isNormalBush() || theTile.isMyceliumBush())
/*      */             {
/*      */               
/* 1971 */               byte data = Tiles.decodeData(tile);
/* 1972 */               byte age = FoliageAge.getAgeAsByte(data);
/* 1973 */               if (theTile.isThorn(data) && age > FoliageAge.OLD_TWO.getAgeId())
/*      */               {
/* 1975 */                 if (this.creature.getFarwalkerSeconds() <= 0)
/*      */                 {
/* 1977 */                   if (Server.rand.nextInt(10) == 0)
/*      */                   {
/* 1979 */                     Wound wound = null;
/* 1980 */                     boolean dead = false;
/*      */                     
/*      */                     try {
/* 1983 */                       byte pos = this.creature.getBody().getRandomWoundPos();
/* 1984 */                       if (Server.rand.nextInt(10) <= 6)
/*      */                       {
/* 1986 */                         if (this.creature.getBody().getWounds() != null) {
/*      */                           
/* 1988 */                           wound = this.creature.getBody().getWounds().getWoundAtLocation(pos);
/* 1989 */                           if (wound != null) {
/*      */                             
/* 1991 */                             if (Tiles.getTile(Tiles.decodeType(tile)).isMyceliumBush() && this.creature
/* 1992 */                               .getKingdomTemplateId() == 3) {
/* 1993 */                               dead = wound.modifySeverity(
/* 1994 */                                   (int)(500.0F + Server.rand.nextInt(2000) * (100.0F - this.creature.getSpellDamageProtectBonus()) / 100.0F));
/*      */                             } else {
/* 1996 */                               dead = wound.modifySeverity(
/* 1997 */                                   (int)(1000.0F + Server.rand.nextInt(4000) * (100.0F - this.creature.getSpellDamageProtectBonus()) / 100.0F));
/* 1998 */                             }  wound.setBandaged(false);
/* 1999 */                             this.creature.setWounded();
/*      */                           } 
/*      */                         } 
/*      */                       }
/* 2003 */                       if (wound == null)
/*      */                       {
/* 2005 */                         if (WurmId.getType(this.creature.getWurmId()) == 0)
/*      */                         {
/* 2007 */                           if (Tiles.getTile(Tiles.decodeType(tile)).isMyceliumBush() && this.creature
/* 2008 */                             .getKingdomId() == 3) {
/*      */                             
/* 2010 */                             dead = this.creature.addWoundOfType(null, (byte)2, pos, false, 1.0F, true, (500.0F + Server.rand
/* 2011 */                                 .nextInt(4000) * (100.0F - this.creature.getSpellDamageProtectBonus()) / 100.0F), 0.0F, 0.0F, false, false);
/*      */                           
/*      */                           }
/*      */                           else {
/*      */                             
/* 2016 */                             dead = this.creature.addWoundOfType(null, (byte)2, pos, false, 1.0F, true, (500.0F + Server.rand
/* 2017 */                                 .nextInt(6000) * (100.0F - this.creature.getSpellDamageProtectBonus()) / 100.0F), 0.0F, 0.0F, false, false);
/*      */                           } 
/*      */                         }
/*      */                       }
/*      */                       
/* 2022 */                       this.creature.getCommunicator().sendAlertServerMessage("You are pierced by the sharp thorns!");
/*      */                       
/* 2024 */                       if (dead) {
/*      */                         
/* 2026 */                         this.creature.achievement(143);
/*      */                         
/*      */                         return;
/*      */                       } 
/* 2030 */                     } catch (Exception exception) {}
/*      */                   }
/*      */                 
/*      */                 }
/*      */               }
/*      */             }
/*      */           
/*      */           }
/*      */         
/*      */         }
/* 2040 */         else if (this.creature.getDeity() != null) {
/*      */           
/* 2042 */           if (this.creature.getDeity().isForestGod() && this.creature.getFaith() >= 70.0F) {
/*      */             
/* 2044 */             byte type = Tiles.decodeType(tile);
/* 2045 */             Tiles.Tile theTile = Tiles.getTile(type);
/* 2046 */             if (theTile.isNormalTree() || theTile.isMyceliumTree() || type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_FIELD.id || type == Tiles.Tile.TILE_FIELD2.id || type == Tiles.Tile.TILE_DIRT.id || type == Tiles.Tile.TILE_TUNDRA.id)
/*      */             {
/*      */ 
/*      */               
/* 2050 */               weight = (int)(weight * 0.5D);
/*      */             }
/*      */           }
/* 2053 */           else if (this.creature.getDeity().isRoadProtector() && this.creature.getFaith() >= 60.0F && this.creature.getFavor() > 30.0F) {
/*      */             
/* 2055 */             byte type = Tiles.decodeType(tile);
/* 2056 */             if (Terraforming.isRoad(type))
/*      */             {
/* 2058 */               weight = (int)(weight * 0.5D);
/*      */             }
/*      */           }
/* 2061 */           else if (this.creature.getDeity().isMountainGod() && this.creature.getFaith() >= 60.0F && this.creature.getFavor() > 30.0F) {
/*      */             
/* 2063 */             byte type = Tiles.decodeType(tile);
/* 2064 */             if (type == Tiles.Tile.TILE_ROCK.id || (type == Tiles.Tile.TILE_SAND.id && this.creature
/* 2065 */               .getKingdomId() == 2))
/*      */             {
/* 2067 */               weight = (int)(weight * 0.5D);
/*      */             }
/*      */           }
/* 2070 */           else if (this.creature.getDeity().isHateGod() && this.creature.getFaith() >= 60.0F && this.creature.getFavor() > 30.0F) {
/*      */             
/* 2072 */             byte type = Tiles.decodeType(tile);
/* 2073 */             Tiles.Tile theTile = Tiles.getTile(type);
/* 2074 */             if (type == Tiles.Tile.TILE_MYCELIUM.id || theTile.isMyceliumTree())
/*      */             {
/* 2076 */               weight = (int)(weight * 0.5D);
/*      */             }
/*      */           } 
/*      */         } 
/*      */         
/* 2081 */         if (this.draggedItem != null) {
/*      */           
/*      */           try {
/*      */             
/* 2085 */             if (moveDraggedItem())
/*      */             {
/* 2087 */               if (this.draggedItem != null) {
/*      */                 
/* 2089 */                 vehic = Vehicles.getVehicleForId(this.draggedItem.getWurmId());
/* 2090 */                 if (vehic != null) {
/*      */                   
/* 2092 */                   if (vehic.creature) {
/*      */ 
/*      */                     
/*      */                     try {
/* 2096 */                       cretVehicle = Server.getInstance().getCreature(vehic.wurmid);
/*      */                     }
/* 2098 */                     catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */                     
/* 2101 */                     } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */                   } else {
/*      */ 
/*      */                     
/*      */                     try {
/*      */ 
/*      */ 
/*      */                       
/* 2109 */                       itemVehicle = Items.getItem(vehic.wurmid);
/*      */                     }
/* 2111 */                     catch (NoSuchItemException noSuchItemException) {}
/*      */                   } 
/*      */ 
/*      */                   
/* 2115 */                   movePassengers(vehic, this.creature, vehic.creature);
/*      */                 } 
/* 2117 */                 weight += this.draggedItem.getFullWeight() / 10;
/* 2118 */                 if (moveDiff > 0.0D) {
/* 2119 */                   weight *= 2;
/*      */                 }
/*      */               } 
/*      */             }
/* 2123 */           } catch (NoSuchZoneException nsz) {
/*      */             
/* 2125 */             Items.stopDragging(this.draggedItem);
/*      */           } 
/*      */         }
/*      */         
/* 2129 */         if (this.creature.isVehicleCommander()) {
/*      */           
/* 2131 */           vehic = Vehicles.getVehicleForId(this.creature.getVehicle());
/* 2132 */           if (vehic != null && !vehic.isChair()) {
/* 2133 */             moveVehicle(diffX, diffY, diffZ);
/*      */           }
/*      */           try {
/* 2136 */             weight += Items.getItem(this.creature.getVehicle()).getWeightGrams() / 100;
/*      */           }
/* 2138 */           catch (NoSuchItemException noSuchItemException) {}
/*      */         } 
/*      */         
/* 2141 */         if (this.creature.getPower() < 2 && this.creature.getPositionZ() < -1.3D && this.creature.getVehicle() == -10L) {
/*      */           
/* 2143 */           if (!this.hasWetFeet) {
/*      */             
/* 2145 */             this.hasWetFeet = true;
/* 2146 */             this.creature.achievement(70);
/*      */           } 
/*      */           
/* 2149 */           if (!PlonkData.SWIMMING.hasSeenThis(this.creature)) {
/* 2150 */             PlonkData.SWIMMING.trigger(this.creature);
/*      */           }
/* 2152 */           moveDiff++;
/* 2153 */           weight += 20000;
/* 2154 */           if (this.creature.getStatus().getStamina() < 50 && !this.creature.isSubmerged())
/*      */           {
/* 2156 */             if (!this.creature.isUndead() && Server.rand.nextInt(100) == 0)
/*      */             {
/* 2158 */               this.creature.addWoundOfType(null, (byte)7, 2, false, 1.0F, false, ((4000.0F + Server.rand
/* 2159 */                   .nextFloat() * 3000.0F) * ItemBonus.getDrownDamReduction(this.creature)), 0.0F, 0.0F, false, false);
/* 2160 */               this.creature.getCommunicator().sendAlertServerMessage("You are drowning!");
/*      */             }
/*      */           
/*      */           }
/*      */         } 
/*      */       } else {
/*      */         
/* 2167 */         if (getBitMask() == 0)
/* 2168 */           this.creature.getStatus().setMoving(false); 
/* 2169 */         if (this.creature.isVehicleCommander()) {
/*      */           
/* 2171 */           vehic = Vehicles.getVehicleForId(this.creature.getVehicle());
/* 2172 */           if (vehic != null)
/*      */           {
/* 2174 */             moveVehicle(diffX, diffY, diffZ);
/*      */           }
/*      */         } 
/*      */       } 
/* 2178 */       if ((beforeZMod > 0.0D || moveDiff > 0.0D) && (this.creature.getVehicle() == -10L || isMovingVehicle() || this.draggedItem != null)) {
/*      */         
/* 2180 */         this.creature.getStatus().setNormalRegen(false);
/* 2181 */         if (moveDiff > 0.0D) {
/* 2182 */           this.creature.getStatus().modifyStamina((int)(-moveDiff * weight / 5000.0D));
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void setHasSpiritSpeed(boolean hasSpeed) {
/* 2189 */     this.hasSpiritSpeed = hasSpeed;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getSpeedModifier() {
/* 2194 */     if (this.halted)
/* 2195 */       return 0.0F; 
/* 2196 */     double bonus = 0.0D;
/* 2197 */     if (this.modifiers != null) {
/*      */       
/* 2199 */       double webHurtModifier = 0.0D;
/* 2200 */       for (DoubleValueModifier lDoubleValueModifier : this.modifiers) {
/*      */         
/* 2202 */         if (lDoubleValueModifier.getType() == 7) {
/*      */           
/* 2204 */           if (lDoubleValueModifier.getModifier() < webHurtModifier)
/* 2205 */             webHurtModifier = lDoubleValueModifier.getModifier(); 
/*      */           continue;
/*      */         } 
/* 2208 */         bonus += lDoubleValueModifier.getModifier();
/*      */       } 
/* 2210 */       bonus += webHurtModifier;
/*      */     } 
/* 2212 */     if (bonus < -4.0D)
/* 2213 */       return 0.0F; 
/* 2214 */     if (this.encumbered)
/* 2215 */       return 0.05F; 
/* 2216 */     if (this.hasSpiritSpeed)
/* 2217 */       bonus *= 1.0499999523162842D; 
/* 2218 */     return (float)Math.max(0.05000000074505806D, this.baseModifier + bonus);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setEncumbered(boolean enc) {
/* 2223 */     if (this.creature.getVehicle() != -10L) {
/* 2224 */       this.encumbered = false;
/*      */     } else {
/* 2226 */       this.encumbered = enc;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBaseModifier(float base) {
/* 2235 */     if (this.creature.getVehicle() != -10L) {
/* 2236 */       this.baseModifier = 1.0F;
/*      */     } else {
/* 2238 */       this.baseModifier = base;
/* 2239 */     }  update();
/*      */   }
/*      */ 
/*      */   
/*      */   public void update() {
/* 2244 */     if (!this.creature.isPlayer()) {
/*      */       
/* 2246 */       if (this.creature.isRidden())
/*      */       {
/* 2248 */         this.creature.forceMountSpeedChange();
/*      */       }
/*      */       return;
/*      */     } 
/* 2252 */     if (!this.halted) {
/* 2253 */       sendSpeedModifier();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void haltSpeedModifier() {
/* 2259 */     addSpeedMod(0.0F);
/*      */     
/* 2261 */     this.creature.getCommunicator().sendSpeedModifier(0.0F);
/*      */ 
/*      */ 
/*      */     
/* 2265 */     this.halted = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void resumeSpeedModifier() {
/* 2270 */     this.halted = false;
/* 2271 */     sendSpeedModifier();
/*      */   }
/*      */ 
/*      */   
/*      */   public void stopSendingSpeedModifier() {
/* 2276 */     this.halted = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Item getDraggedItem() {
/* 2285 */     return this.draggedItem;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDraggedItem(@Nullable Item dragged) {
/* 2295 */     if (this.draggedItem != null && !this.draggedItem.equals(dragged))
/*      */     {
/* 2297 */       if (dragged != null) {
/* 2298 */         Items.stopDragging(this.draggedItem);
/*      */       } else {
/*      */         
/* 2301 */         this.creature.getCommunicator().sendNormalServerMessage("You stop dragging " + this.draggedItem
/* 2302 */             .getNameWithGenus() + '.');
/* 2303 */         if (this.draggedItem.getTemplateId() == 1125) {
/* 2304 */           removeModifier(ramDragMod);
/*      */         } else {
/* 2306 */           removeModifier(dragMod);
/*      */         } 
/*      */       }  } 
/* 2309 */     this.draggedItem = dragged;
/* 2310 */     if (this.draggedItem != null) {
/*      */       
/* 2312 */       this.creature.getCommunicator().sendNormalServerMessage("You start dragging " + this.draggedItem
/* 2313 */           .getNameWithGenus() + '.');
/* 2314 */       if (this.draggedItem.getTemplateId() == 1125) {
/* 2315 */         addModifier(ramDragMod);
/*      */       } else {
/* 2317 */         addModifier(dragMod);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setFightMoveMod(boolean fighting) {
/* 2323 */     if (fighting) {
/* 2324 */       addModifier(combatMod);
/*      */     } else {
/* 2326 */       removeModifier(combatMod);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setFarwalkerMoveMod(boolean add) {
/* 2331 */     if (add) {
/* 2332 */       addModifier(farwalkerMod);
/*      */     } else {
/* 2334 */       removeModifier(farwalkerMod);
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
/*      */   public boolean setWebArmourMod(boolean add, float power) {
/* 2348 */     if (add) {
/*      */       
/* 2350 */       if (!this.webArmoured)
/*      */       {
/* 2352 */         this.webArmoured = true;
/* 2353 */         this.justWebSlowArmour = true;
/* 2354 */         this.webArmourMod.setModifier((-power / 200.0F));
/* 2355 */         addModifier(this.webArmourMod);
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/* 2362 */     else if (this.webArmoured) {
/*      */ 
/*      */       
/* 2365 */       if (this.justWebSlowArmour) {
/* 2366 */         this.justWebSlowArmour = false;
/*      */       } else {
/*      */         
/* 2369 */         this.webArmoured = false;
/* 2370 */         removeModifier(this.webArmourMod);
/* 2371 */         this.webArmourMod.setModifier(0.0D);
/*      */       } 
/*      */     } 
/*      */     
/* 2375 */     return this.webArmoured;
/*      */   }
/*      */ 
/*      */   
/*      */   public double getWebArmourMod() {
/* 2380 */     if (this.webArmoured)
/* 2381 */       return this.webArmourMod.getModifier(); 
/* 2382 */     return 0.0D;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setChargeMoveMod(boolean add) {
/* 2387 */     if (add) {
/* 2388 */       addModifier(chargeMod);
/*      */     } else {
/* 2390 */       removeModifier(chargeMod);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setDrunkMod(boolean drunk) {
/* 2395 */     if (drunk) {
/* 2396 */       addModifier(drunkMod);
/*      */     } else {
/* 2398 */       removeModifier(drunkMod);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setMooredMod(boolean moored) {
/* 2403 */     if (moored) {
/* 2404 */       addModifier(mooreMod);
/*      */     } else {
/* 2406 */       removeModifier(mooreMod);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setStealthMod(boolean stealth) {
/* 2411 */     if (stealth) {
/* 2412 */       addModifier(this.stealthMod);
/*      */     } else {
/* 2414 */       removeModifier(this.stealthMod);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setFreezeMod(boolean frozen) {
/* 2419 */     if (frozen) {
/* 2420 */       addModifier(freezeMod);
/*      */     } else {
/* 2422 */       removeModifier(freezeMod);
/*      */     } 
/*      */   }
/*      */   
/*      */   private final float getDragDistanceMod(int templateId) {
/* 2427 */     switch (templateId) {
/*      */       
/*      */       case 539:
/* 2430 */         return -2.0F;
/*      */       case 853:
/*      */       case 1410:
/* 2433 */         return -3.0F;
/*      */     } 
/* 2435 */     return -1.5F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean moveDraggedItem() throws NoSuchZoneException {
/* 2441 */     int weight = this.draggedItem.getFullWeight(true);
/* 2442 */     int left = this.creature.getCarryingCapacityLeft();
/* 2443 */     if ((this.draggedItem.getTemplateId() == 539 && weight < left) || weight < left * 10) {
/*      */       
/* 2445 */       float iposx = this.creature.getStatus().getPositionX();
/* 2446 */       float iposy = this.creature.getStatus().getPositionY();
/* 2447 */       float rot = this.creature.getStatus().getRotation();
/* 2448 */       float oldPosZ = this.creature.getPositionZ();
/*      */       
/* 2450 */       float distMod = getDragDistanceMod(this.draggedItem.getTemplateId());
/* 2451 */       float xPosMod = (float)Math.sin((rot * 0.017453292F)) * distMod;
/* 2452 */       float yPosMod = -((float)Math.cos((rot * 0.017453292F))) * distMod;
/*      */       
/* 2454 */       float newPosX = iposx + xPosMod;
/* 2455 */       float newPosY = iposy + yPosMod;
/*      */       
/* 2457 */       if (!this.creature.isOnSurface())
/*      */       {
/* 2459 */         if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile((int)newPosX >> 2, (int)newPosY >> 2))) || 
/* 2460 */           Blocking.isDiagonalRockBetween(this.creature, (int)iposx >> 2, (int)iposy >> 2, (int)newPosX >> 2, (int)newPosY >> 2) != null)
/*      */         {
/*      */ 
/*      */           
/* 2464 */           if (!Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile((int)iposx >> 2, (int)iposy >> 2)))) {
/*      */             
/* 2466 */             newPosX = iposx;
/* 2467 */             newPosY = iposy;
/*      */           }
/*      */           else {
/*      */             
/* 2471 */             Items.stopDragging(this.draggedItem);
/* 2472 */             return false;
/*      */           } 
/*      */         }
/*      */       }
/*      */       
/* 2477 */       if (this.draggedItem.onBridge() > 0L) {
/*      */         
/* 2479 */         int ntx = (int)newPosX >> 2;
/* 2480 */         int nty = (int)newPosY >> 2;
/* 2481 */         if (this.draggedItem.getTileX() != ntx || this.draggedItem.getTileY() != nty) {
/*      */           
/* 2483 */           VolaTile newTile = Zones.getOrCreateTile(ntx, nty, this.draggedItem.isOnSurface());
/*      */           
/* 2485 */           if (newTile == null || newTile.getStructure() == null || newTile
/* 2486 */             .getStructure().getWurmId() != this.draggedItem.onBridge()) {
/*      */             
/* 2488 */             VolaTile oldTile = Zones.getOrCreateTile(this.draggedItem.getTileX(), this.draggedItem.getTileY(), this.draggedItem
/* 2489 */                 .isOnSurface());
/* 2490 */             boolean leavingOnSide = false;
/* 2491 */             if (oldTile != null) {
/*      */               
/* 2493 */               BridgePart[] bridgeParts = oldTile.getBridgeParts();
/* 2494 */               if (bridgeParts != null) {
/*      */                 
/* 2496 */                 BridgePart[] arrayOfBridgePart = bridgeParts; int i = arrayOfBridgePart.length; byte b = 0; if (b < i) { BridgePart bp = arrayOfBridgePart[b];
/*      */ 
/*      */                   
/* 2499 */                   if ((bp.getDir() == 0 || bp.getDir() == 4) && this.draggedItem.getTileX() != ntx) {
/* 2500 */                     leavingOnSide = true;
/* 2501 */                   } else if ((bp.getDir() == 2 || bp.getDir() == 6) && this.draggedItem.getTileY() != nty) {
/* 2502 */                     leavingOnSide = true;
/*      */                   }  }
/*      */               
/*      */               } 
/*      */             } 
/* 2507 */             if (leavingOnSide) {
/*      */               
/* 2509 */               newPosX = iposx;
/* 2510 */               newPosY = iposy;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 2515 */       float newPosZ = Zones.calculatePosZ(newPosX, newPosY, null, this.draggedItem.isOnSurface(), false, oldPosZ, null, this.draggedItem
/* 2516 */           .onBridge());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2523 */       float maxDepth = -6.0F;
/* 2524 */       if (this.draggedItem.isVehicle() && !this.draggedItem.isBoat()) {
/*      */         
/* 2526 */         Vehicle lVehicle = Vehicles.getVehicle(this.draggedItem);
/* 2527 */         maxDepth = lVehicle.getMaxDepth();
/*      */       } 
/* 2529 */       if (this.draggedItem.isFloating() && newPosZ > 0.3D) {
/*      */         
/* 2531 */         this.creature.getCommunicator()
/* 2532 */           .sendAlertServerMessage("The " + this.draggedItem.getName() + " gets stuck in the ground.", (byte)3);
/* 2533 */         Items.stopDragging(this.draggedItem);
/* 2534 */         return false;
/*      */       } 
/* 2536 */       if (!this.draggedItem.isFloating() && newPosZ < maxDepth && this.draggedItem.onBridge() <= 0L) {
/*      */         
/* 2538 */         this.creature.getCommunicator()
/* 2539 */           .sendAlertServerMessage("The " + this.draggedItem.getName() + " gets stuck on the bottom.", (byte)3);
/* 2540 */         Items.stopDragging(this.draggedItem);
/* 2541 */         return false;
/*      */       } 
/*      */ 
/*      */       
/* 2545 */       if (this.creature.isOnSurface() == this.draggedItem.isOnSurface()) {
/*      */         
/* 2547 */         VolaTile t = Zones.getTileOrNull(this.draggedItem.getTileX(), this.draggedItem.getTileY(), this.draggedItem
/* 2548 */             .isOnSurface());
/* 2549 */         if (t == null) {
/*      */           
/* 2551 */           Items.stopDragging(this.draggedItem);
/* 2552 */           return false;
/*      */         } 
/*      */ 
/*      */         
/* 2556 */         if (!this.draggedItem.isOnSurface())
/*      */         {
/* 2558 */           if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh
/* 2559 */                 .getTile((int)newPosX >> 2, (int)newPosY >> 2))))
/*      */           {
/* 2561 */             if (Tiles.decodeType(Server.caveMesh.getTile(this.draggedItem.getTileX(), this.draggedItem
/* 2562 */                   .getTileY())) != 201)
/*      */             {
/* 2564 */               return false;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */         
/* 2570 */         t.moveItem(this.draggedItem, newPosX, newPosY, newPosZ, Creature.normalizeAngle(rot), this.creature
/* 2571 */             .isOnSurface(), oldPosZ);
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 2576 */         Zone zone = null;
/* 2577 */         if (this.creature.isOnSurface() && 
/* 2578 */           Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile((int)newPosX >> 2, (int)newPosY >> 2)))) {
/*      */ 
/*      */           
/* 2581 */           newPosX = iposx;
/* 2582 */           newPosY = iposy;
/*      */         } 
/*      */         
/*      */         try {
/* 2586 */           zone = Zones.getZone(this.draggedItem.getTileX(), this.draggedItem.getTileY(), this.draggedItem.isOnSurface());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2594 */           zone.removeItem(this.draggedItem);
/*      */         }
/* 2596 */         catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */         
/* 2599 */         this.draggedItem.setPosXYZ(newPosX, newPosY, newPosZ);
/* 2600 */         this.draggedItem.newLayer = (byte)(this.creature.isOnSurface() ? 0 : -1);
/* 2601 */         zone = Zones.getZone((int)newPosX >> 2, (int)newPosY >> 2, this.creature.isOnSurface());
/* 2602 */         zone.addItem(this.draggedItem, true, this.creature.isOnSurface(), false);
/*      */         
/* 2604 */         this.draggedItem.newLayer = Byte.MIN_VALUE;
/* 2605 */         if (this.draggedItem.isVehicle()) {
/*      */           
/* 2607 */           Vehicle vehicle = Vehicles.getVehicleForId(this.draggedItem.getWurmId());
/* 2608 */           if (vehicle != null) {
/*      */             
/* 2610 */             Seat[] seats = vehicle.getSeats();
/* 2611 */             if (seats != null)
/*      */             {
/* 2613 */               for (int x = 0; x < seats.length; x++) {
/*      */                 
/* 2615 */                 if (seats[x] != null)
/*      */                 {
/* 2617 */                   if ((seats[x]).occupant != -10L) {
/*      */                     
/*      */                     try {
/*      */                       
/* 2621 */                       Creature c = Server.getInstance().getCreature((seats[x]).occupant);
/* 2622 */                       c.setLayer(this.creature.getLayer(), false);
/* 2623 */                       c.refreshVisible();
/* 2624 */                       c.getCommunicator().attachCreature(-1L, this.draggedItem.getWurmId(), (seats[x]).offx, (seats[x]).offy, (seats[x]).offz, x);
/*      */                     
/*      */                     }
/* 2627 */                     catch (NoSuchPlayerException noSuchPlayerException) {
/*      */ 
/*      */                     
/* 2630 */                     } catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */                   }
/*      */                 }
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2641 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 2645 */     this.creature.getCommunicator().sendNormalServerMessage("The " + this.draggedItem.getName() + " is too heavy.");
/* 2646 */     Items.stopDragging(this.draggedItem);
/* 2647 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addModifier(DoubleValueModifier modifier) {
/* 2653 */     if (!this.creature.isPlayer() && !this.creature.isVehicle()) {
/*      */       return;
/*      */     }
/*      */     
/* 2657 */     if (this.modifiers == null)
/* 2658 */       this.modifiers = new HashSet<>(); 
/* 2659 */     if (!this.modifiers.contains(modifier))
/*      */     {
/* 2661 */       this.modifiers.add(modifier);
/*      */     }
/*      */ 
/*      */     
/* 2665 */     update();
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeModifier(DoubleValueModifier modifier) {
/* 2670 */     if (this.modifiers != null) {
/* 2671 */       this.modifiers.remove(modifier);
/*      */     }
/*      */ 
/*      */     
/* 2675 */     update();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void valueChanged(double oldValue, double newValue) {
/* 2681 */     update();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void touchFreeMoveCounter() {
/* 2686 */     this.changedTileCounter = 5;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void decreaseFreeMoveCounter() {
/* 2691 */     if (this.changedTileCounter > 0)
/*      */     {
/*      */       
/* 2694 */       this.changedTileCounter--;
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
/*      */   public int getMaxTargetGroundOffset(int suggestedOffset) {
/* 2706 */     if (this.creature.getPower() > 0) {
/* 2707 */       return suggestedOffset;
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
/* 2719 */     float xPos = getCurrx();
/* 2720 */     float yPos = getCurry();
/* 2721 */     if (xPos == 0.0F && yPos == 0.0F) {
/*      */ 
/*      */ 
/*      */       
/* 2725 */       xPos = getX();
/* 2726 */       yPos = getY();
/*      */     } 
/* 2728 */     VolaTile t = Zones.getOrCreateTile((int)xPos / 4, (int)yPos / 4, (getLayer() >= 0));
/* 2729 */     if (t == null) {
/* 2730 */       return 0;
/*      */     }
/* 2732 */     int max = t.getMaxFloorLevel() * 30 + 30;
/*      */     
/* 2734 */     return max;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getErrors() {
/* 2739 */     return this.errors;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setErrors(int errors) {
/* 2744 */     this.errors = errors;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\MovementScheme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */