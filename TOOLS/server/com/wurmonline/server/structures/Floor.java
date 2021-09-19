/*      */ package com.wurmonline.server.structures;
/*      */ 
/*      */ import com.wurmonline.math.TilePos;
/*      */ import com.wurmonline.math.Vector3f;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.highways.HighwayPos;
/*      */ import com.wurmonline.server.highways.MethodsHighways;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.players.Permissions;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.StructureConstants;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class Floor
/*      */   implements MiscConstants, TimeConstants, Blocker, IFloor, Permissions.IAllow
/*      */ {
/*   74 */   private static final Logger logger = Logger.getLogger(Wall.class.getName());
/*      */   
/*   76 */   private long structureId = -10L;
/*   77 */   private int number = -10;
/*      */   
/*      */   float originalQL;
/*      */   float currentQL;
/*      */   float damage;
/*      */   private int tilex;
/*      */   private int tiley;
/*      */   private int heightOffset;
/*      */   long lastUsed;
/*   86 */   private StructureConstants.FloorType type = StructureConstants.FloorType.FLOOR;
/*   87 */   private StructureConstants.FloorMaterial material = StructureConstants.FloorMaterial.WOOD;
/*   88 */   private StructureConstants.FloorState floorState = StructureConstants.FloorState.PLANNING;
/*      */   
/*   90 */   protected byte dbState = -1;
/*      */   
/*   92 */   private byte layer = 0;
/*   93 */   private int floorLevel = 0;
/*      */   
/*   95 */   private int color = -1;
/*   96 */   private byte direction = 0;
/*      */ 
/*      */   
/*      */   private static final byte FLOOR_DBSTATE_PLANNED = -1;
/*      */ 
/*      */   
/*      */   private static final byte FLOOR_DBSTATE_UNINITIALIZED = 0;
/*      */ 
/*      */   
/*  105 */   private static final Map<Long, Set<Floor>> floors = new HashMap<>();
/*      */   
/*      */   private static final String GETALLFLOORS = "SELECT * FROM FLOORS";
/*  108 */   private static final Vector3f normal = new Vector3f(0.0F, 0.0F, 1.0F);
/*  109 */   Permissions permissions = new Permissions();
/*      */ 
/*      */   
/*      */   private Vector3f centerPoint;
/*      */ 
/*      */ 
/*      */   
/*      */   public Floor(int id, StructureConstants.FloorType floorType, int aTileX, int aTileY, byte adbState, int aheightOffset, float ql, long structure, StructureConstants.FloorMaterial floorMaterial, int aLayer, float origQl, float dam, long lastmaint, byte dir) {
/*  117 */     setNumber(id);
/*  118 */     this.type = floorType;
/*  119 */     this.tilex = aTileX;
/*  120 */     this.tiley = aTileY;
/*  121 */     this.dbState = adbState;
/*  122 */     this.floorState = StructureConstants.FloorState.fromByte(this.dbState);
/*  123 */     this.heightOffset = aheightOffset;
/*  124 */     this.currentQL = ql;
/*  125 */     this.originalQL = origQl;
/*  126 */     this.damage = dam;
/*  127 */     this.structureId = structure;
/*  128 */     this.material = floorMaterial;
/*  129 */     this.layer = (byte)(aLayer & 0xFF);
/*  130 */     this.lastUsed = lastmaint;
/*  131 */     this.direction = dir;
/*  132 */     setFloorLevel();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Floor(StructureConstants.FloorType floorType, int aTileX, int aTileY, int height, float ql, long structure, StructureConstants.FloorMaterial floorMaterial, int aLayer) {
/*  138 */     this.type = floorType;
/*  139 */     this.tilex = aTileX;
/*  140 */     this.tiley = aTileY;
/*  141 */     this.heightOffset = height;
/*  142 */     this.currentQL = ql;
/*  143 */     this.structureId = structure;
/*  144 */     this.material = floorMaterial;
/*  145 */     this.layer = (byte)(aLayer & 0xFF);
/*  146 */     setFloorLevel();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFloor() {
/*  152 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isStair() {
/*  158 */     return (isFinished() && this.type.isStair());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Vector3f getNormal() {
/*  164 */     return normal;
/*      */   }
/*      */ 
/*      */   
/*      */   private final Vector3f calculateCenterPoint() {
/*  169 */     return new Vector3f((this.tilex * 4 + 2), (this.tiley * 4 + 2), getMinZ() + (
/*  170 */         isRoof() ? 1.0F : 0.125F));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Vector3f getCenterPoint() {
/*  176 */     if (this.centerPoint == null)
/*  177 */       this.centerPoint = calculateCenterPoint(); 
/*  178 */     return this.centerPoint;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTileX() {
/*  189 */     return this.tilex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setTilex(int aTilex) {
/*  200 */     this.tilex = aTilex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTileY() {
/*  211 */     return this.tiley;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setTiley(int aTiley) {
/*  222 */     this.tiley = aTiley;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getPositionX() {
/*  228 */     return (this.tilex * 4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getPositionY() {
/*  234 */     return (this.tiley * 4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getHeightOffset() {
/*  242 */     return this.heightOffset;
/*      */   }
/*      */ 
/*      */   
/*      */   void setHeightOffset(int aHeightOffset) {
/*  247 */     this.heightOffset = aHeightOffset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getLayer() {
/*  255 */     return this.layer;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getDir() {
/*  260 */     return this.direction;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean leavingStairOnTop(int tilexDiff, int tileyDiff) {
/*  265 */     if (isStair()) {
/*      */       
/*  267 */       if (getDir() == 0)
/*      */       {
/*  269 */         return (tileyDiff < 0);
/*      */       }
/*  271 */       if (getDir() == 2)
/*      */       {
/*  273 */         return (tilexDiff > 0);
/*      */       }
/*  275 */       if (getDir() == 6)
/*      */       {
/*  277 */         return (tilexDiff < 0);
/*      */       }
/*  279 */       if (getDir() == 4)
/*      */       {
/*  281 */         return (tileyDiff > 0);
/*      */       }
/*      */     } 
/*  284 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLayer(byte newLayer) {
/*  294 */     this.layer = newLayer;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOnSurface() {
/*  300 */     return (this.layer == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void rotate(int change) {
/*  305 */     this.direction = (byte)((this.direction + 8 + change) % 8);
/*      */     
/*      */     try {
/*  308 */       save();
/*      */     }
/*  310 */     catch (IOException e) {
/*      */ 
/*      */       
/*  313 */       logger.log(Level.WARNING, e.getMessage(), e);
/*      */     } 
/*  315 */     VolaTile volaTile = Zones.getOrCreateTile(getTileX(), getTileY(), (getLayer() >= 0));
/*  316 */     volaTile.updateFloor(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFinished() {
/*  325 */     return (this.floorState == StructureConstants.FloorState.COMPLETED);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isMetal() {
/*  331 */     return (this.material == StructureConstants.FloorMaterial.METAL_COPPER || this.material == StructureConstants.FloorMaterial.METAL_GOLD || this.material == StructureConstants.FloorMaterial.METAL_IRON || this.material == StructureConstants.FloorMaterial.METAL_SILVER || this.material == StructureConstants.FloorMaterial.METAL_STEEL);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isWood() {
/*  339 */     return (this.material == StructureConstants.FloorMaterial.WOOD || this.material == StructureConstants.FloorMaterial.THATCH || this.material == StructureConstants.FloorMaterial.STANDALONE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isStone() {
/*  346 */     return (this.material == StructureConstants.FloorMaterial.STONE_BRICK || this.material == StructureConstants.FloorMaterial.STONE_SLAB);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSlate() {
/*  352 */     return (this.material == StructureConstants.FloorMaterial.SLATE_SLAB);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isMarble() {
/*  358 */     return (this.material == StructureConstants.FloorMaterial.MARBLE_SLAB);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSandstone() {
/*  364 */     return (this.material == StructureConstants.FloorMaterial.SANDSTONE_SLAB);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isGold() {
/*  369 */     return (this.material == StructureConstants.FloorMaterial.METAL_GOLD);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isSilver() {
/*  374 */     return (this.material == StructureConstants.FloorMaterial.METAL_SILVER);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isIron() {
/*  379 */     return (this.material == StructureConstants.FloorMaterial.METAL_IRON);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isSteel() {
/*  384 */     return (this.material == StructureConstants.FloorMaterial.METAL_STEEL);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isCopper() {
/*  389 */     return (this.material == StructureConstants.FloorMaterial.METAL_COPPER);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isThatch() {
/*  395 */     return (this.material == StructureConstants.FloorMaterial.THATCH);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isClay() {
/*  400 */     return (this.material == StructureConstants.FloorMaterial.CLAY_BRICK);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isSolid() {
/*  405 */     return (isFinished() && !isStair());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StructureConstants.FloorType getType() {
/*  413 */     return this.type;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setType(StructureConstants.FloorType newType) {
/*  418 */     this.type = newType;
/*      */   }
/*      */ 
/*      */   
/*      */   public StructureConstants.FloorMaterial getMaterial() {
/*  423 */     return this.material;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMaterial(StructureConstants.FloorMaterial newMaterial) {
/*  428 */     this.material = newMaterial;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void save() throws IOException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getId() {
/*  442 */     return Tiles.getFloorId(this.tilex, this.tiley, this.heightOffset, getLayer());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getHeightOffsetFromWurmId(long wurmId) {
/*  448 */     return (int)(wurmId >> 48L) & 0xFFFF;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StructureConstants.FloorState getFloorState() {
/*  456 */     return this.floorState;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   long getLastUsed() {
/*  466 */     return this.lastUsed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void setLastUsed(long paramLong);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int getNumber() {
/*  485 */     return this.number;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setNumber(int aNumber) {
/*  496 */     this.number = aNumber;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getOriginalQL() {
/*  506 */     return this.originalQL;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getCurrentQL() {
/*  516 */     return this.currentQL;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getState() {
/*  526 */     return this.dbState;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract void setState(byte paramByte);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFloorState(StructureConstants.FloorState aFloorState) {
/*  541 */     this.floorState = aFloorState;
/*  542 */     switch (this.floorState) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case METAL_COPPER:
/*  548 */         if (getState() <= 0)
/*      */         {
/*  550 */           setState((byte)0);
/*      */         }
/*      */         break;
/*      */       
/*      */       case METAL_GOLD:
/*  555 */         setState(StructureConstants.FloorState.COMPLETED.getCode());
/*      */         break;
/*      */       
/*      */       case METAL_SILVER:
/*  559 */         setState((byte)-1);
/*      */         break;
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
/*      */   int getColor() {
/*  573 */     return this.color;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setColor(int aColor) {
/*  584 */     this.color = aColor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void delete();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  597 */     switch (this.type) {
/*      */       
/*      */       case METAL_COPPER:
/*  600 */         return "hatch";
/*      */       case METAL_GOLD:
/*  602 */         return "opening";
/*      */       case METAL_SILVER:
/*  604 */         return "roof";
/*      */       case METAL_IRON:
/*  606 */         return "floor";
/*      */       case METAL_STEEL:
/*  608 */         return "staircase";
/*      */       case CLAY_BRICK:
/*  610 */         return "wide staircase";
/*      */       case SLATE_SLAB:
/*  612 */         return "wide staircase with banisters on right";
/*      */       case STONE_BRICK:
/*  614 */         return "wide staircase with banisters on left";
/*      */       case STONE_SLAB:
/*  616 */         return "wide staircase with banisiters on both sides";
/*      */       case MARBLE_SLAB:
/*  618 */         return "right staircase";
/*      */       case SANDSTONE_SLAB:
/*  620 */         return "left staircase";
/*      */       case THATCH:
/*  622 */         return "clockwise spiral staircase";
/*      */       case WOOD:
/*  624 */         return "clockwise spiral staircase with banisters";
/*      */       case STANDALONE:
/*  626 */         return "counter clockwise spiral staircase";
/*      */       case null:
/*  628 */         return "counter clockwise spiral staircase with banisters";
/*      */     } 
/*  630 */     return "unknown";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isOpening() {
/*  636 */     return (this.type == StructureConstants.FloorType.OPENING);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isHorizontal() {
/*  647 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Vector3f isBlocking(Creature creature, Vector3f startPos, Vector3f endPos, Vector3f aNormal, int blockType, long target, boolean followGround) {
/*  657 */     if (target == getId())
/*  658 */       return null; 
/*  659 */     if (isAPlan())
/*      */     {
/*  661 */       return null;
/*      */     }
/*  663 */     if (isOpening())
/*      */     {
/*  665 */       return null;
/*      */     }
/*  667 */     Vector3f inter = getIntersectionPoint(startPos, endPos, aNormal, creature);
/*  668 */     return inter;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isDoor() {
/*  674 */     return (this.type == StructureConstants.FloorType.DOOR);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isRoof() {
/*  680 */     return (this.type == StructureConstants.FloorType.ROOF);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isTile() {
/*  686 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean canBeOpenedBy(Creature creature, boolean wentThroughDoor) {
/*  692 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getBlockPercent(Creature creature) {
/*  698 */     if (isFinished())
/*      */     {
/*  700 */       return 100.0F;
/*      */     }
/*  702 */     return Math.max(0, getState());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isWithinFloorLevels(int maxFloorLevel, int minFloorLevel) {
/*  708 */     return (this.floorLevel <= maxFloorLevel && this.floorLevel >= minFloorLevel);
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
/*      */   public final Vector3f getIntersectionPoint(Vector3f startPos, Vector3f endPos, Vector3f aNormal, Creature creature) {
/*  733 */     if (isWithinBounds(startPos, creature)) {
/*  734 */       return startPos.clone();
/*      */     }
/*      */     
/*  737 */     float zPlane = getMinZ();
/*  738 */     if (Math.abs(startPos.z - getMinZ()) > Math.abs(startPos.z - getMaxZ()))
/*  739 */       zPlane = getMaxZ(); 
/*  740 */     float xPlane = (getMinX() * 4);
/*  741 */     if (Math.abs(startPos.x - (getMinX() * 4)) > Math.abs(startPos.x - (getMinX() * 4 + 4)))
/*  742 */       xPlane += 4.0F; 
/*  743 */     float yPlane = (getMinY() * 4);
/*  744 */     if (Math.abs(startPos.y - (getMinY() * 4)) > Math.abs(startPos.y - (getMinY() * 4 + 4))) {
/*  745 */       yPlane += 4.0F;
/*      */     }
/*      */     
/*  748 */     for (int i = 0; i < 3; i++) {
/*      */       
/*  750 */       float planeVal = (i == 0) ? zPlane : ((i == 1) ? xPlane : yPlane);
/*      */       
/*  752 */       Vector3f centerPoint = getCenterPoint().clone();
/*  753 */       switch (i) {
/*      */         
/*      */         case 0:
/*  756 */           centerPoint.setZ(planeVal);
/*      */           break;
/*      */         case 1:
/*  759 */           centerPoint.setX(planeVal);
/*      */           break;
/*      */         case 2:
/*  762 */           centerPoint.setY(planeVal);
/*      */           break;
/*      */       } 
/*      */       
/*  766 */       Vector3f diff = startPos.subtract(centerPoint);
/*  767 */       float diffVal = (i == 0) ? diff.z : ((i == 1) ? diff.x : diff.y);
/*  768 */       float normalVal = (i == 0) ? aNormal.z : ((i == 1) ? aNormal.x : aNormal.y);
/*  769 */       if (normalVal != 0.0F) {
/*      */ 
/*      */         
/*  772 */         float steps = diffVal / normalVal;
/*  773 */         Vector3f intersection = startPos.add(aNormal.mult(-steps));
/*  774 */         Vector3f diffend = endPos.subtract(startPos);
/*  775 */         Vector3f interDiff = intersection.subtract(startPos);
/*      */ 
/*      */ 
/*      */         
/*  779 */         if (interDiff.length() < diffend.length())
/*      */         {
/*  781 */           if (isWithinBounds(intersection, creature)) {
/*      */ 
/*      */             
/*  784 */             float u = aNormal.dot(centerPoint.subtract(startPos)) / aNormal.dot(endPos.subtract(startPos));
/*  785 */             if (u >= 0.0F && u <= 1.0F) {
/*  786 */               return intersection;
/*      */             }
/*      */           } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  831 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean isWithinBounds(Vector3f pointToCheck, Creature creature) {
/*  842 */     if (pointToCheck.getY() >= (this.tiley * 4) && pointToCheck
/*  843 */       .getY() <= ((this.tiley + 1) * 4))
/*      */     {
/*  845 */       if (pointToCheck.getX() >= (this.tilex * 4) && pointToCheck.getX() <= ((this.tilex + 1) * 4))
/*      */       {
/*  847 */         if (isWithinZ(pointToCheck.getZ(), pointToCheck.getZ(), (creature != null && creature.followsGround())))
/*      */         {
/*  849 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  856 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void loadAllFloors() throws IOException {
/*  861 */     logger.log(Level.INFO, "Loading all floors.");
/*  862 */     long s = System.nanoTime();
/*  863 */     Connection dbcon = null;
/*  864 */     PreparedStatement ps = null;
/*  865 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  868 */       dbcon = DbConnector.getZonesDbCon();
/*  869 */       ps = dbcon.prepareStatement("SELECT * FROM FLOORS");
/*  870 */       rs = ps.executeQuery();
/*  871 */       while (rs.next())
/*      */       {
/*  873 */         StructureConstants.FloorType floorType = StructureConstants.FloorType.fromByte(rs.getByte("TYPE"));
/*  874 */         StructureConstants.FloorMaterial floorMaterial = StructureConstants.FloorMaterial.fromByte(rs.getByte("MATERIAL"));
/*      */ 
/*      */ 
/*      */         
/*  878 */         long sid = rs.getLong("STRUCTURE");
/*  879 */         Set<Floor> flset = floors.get(Long.valueOf(sid));
/*  880 */         if (flset == null) {
/*      */           
/*  882 */           flset = new HashSet<>();
/*  883 */           floors.put(Long.valueOf(sid), flset);
/*      */         } 
/*  885 */         flset.add(new DbFloor(rs.getInt("ID"), floorType, rs.getInt("TILEX"), rs.getInt("TILEY"), rs.getByte("STATE"), rs
/*      */               
/*  887 */               .getInt("HEIGHTOFFSET"), rs
/*  888 */               .getFloat("CURRENTQL"), sid, floorMaterial, rs.getInt("LAYER"), rs
/*  889 */               .getFloat("ORIGINALQL"), rs.getFloat("DAMAGE"), rs.getLong("LASTMAINTAINED"), rs.getByte("DIR")));
/*      */       }
/*      */     
/*  892 */     } catch (SQLException sqx) {
/*      */       
/*  894 */       logger.log(Level.WARNING, "Failed to load walls!" + sqx.getMessage(), sqx);
/*  895 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  899 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  900 */       DbConnector.returnConnection(dbcon);
/*  901 */       long e = System.nanoTime();
/*  902 */       logger.log(Level.INFO, "Loaded " + floors.size() + " floors. That took " + ((float)(e - s) / 1000000.0F) + " ms.");
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
/*      */   public static final Set<Floor> getFloorsFor(long structureId) {
/*  916 */     return floors.get(Long.valueOf(structureId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getStructureId() {
/*  925 */     return this.structureId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setStructureId(long aStructureId) {
/*  936 */     this.structureId = aStructureId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAPlan() {
/*  945 */     return (this.floorState == StructureConstants.FloorState.PLANNING);
/*      */   }
/*      */ 
/*      */   
/*      */   public void revertToPlan() {
/*  950 */     MethodsHighways.removeNearbyMarkers(this);
/*  951 */     setFloorState(StructureConstants.FloorState.PLANNING);
/*      */     
/*      */     try {
/*  954 */       save();
/*      */     }
/*  956 */     catch (IOException e) {
/*      */       
/*  958 */       logger.log(Level.WARNING, e.getMessage(), e);
/*      */     } 
/*  960 */     VolaTile volaTile = Zones.getOrCreateTile(getTileX(), getTileY(), (getLayer() >= 0));
/*  961 */     volaTile.updateFloor(this);
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
/*      */   public void destroyOrRevertToPlan() {
/*  973 */     Structure struct = null;
/*      */     
/*      */     try {
/*  976 */       struct = Structures.getStructure(getStructureId());
/*      */     }
/*  978 */     catch (NoSuchStructureException e) {
/*      */       
/*  980 */       logger.log(Level.WARNING, " Failed to find Structures.getStructure(" + getStructureId() + " for a Floor about to be deleted: " + e
/*  981 */           .getMessage(), (Throwable)e);
/*      */     } 
/*      */     
/*  984 */     if (struct != null && struct.wouldCreateFlyingStructureIfRemoved(this)) {
/*      */       
/*  986 */       revertToPlan();
/*      */     }
/*      */     else {
/*      */       
/*  990 */       destroy();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void destroy() {
/* 1000 */     delete();
/*      */     
/* 1002 */     Set<Floor> flset = floors.get(Long.valueOf(getStructureId()));
/* 1003 */     if (flset != null)
/*      */     {
/* 1005 */       flset.remove(this);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1010 */     VolaTile volaTile = Zones.getOrCreateTile(getTileX(), getTileY(), (getLayer() >= 0));
/* 1011 */     volaTile.removeFloor(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getDamageModifierForItem(Item item) {
/* 1019 */     switch (this.material)
/*      */     
/*      */     { case METAL_COPPER:
/*      */       case METAL_GOLD:
/*      */       case METAL_SILVER:
/* 1024 */         if (item.isWeaponCrush()) {
/* 1025 */           mod = 0.03F;
/*      */         } else {
/* 1027 */           mod = 0.007F;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1056 */         return mod;case METAL_IRON: case METAL_STEEL: if (item.isWeaponCrush()) { mod = 0.02F; } else { mod = 0.007F; }  return mod;case CLAY_BRICK: case SLATE_SLAB: case STONE_BRICK: case STONE_SLAB: case MARBLE_SLAB: case SANDSTONE_SLAB: if (item.isWeaponCrush()) { mod = 0.03F; } else { mod = 0.007F; }  return mod;case THATCH: case WOOD: case STANDALONE: mod = 0.03F; return mod; }  float mod = 0.0F; return mod;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isOnPvPServer() {
/* 1062 */     if (Zones.isOnPvPServer(this.tilex, this.tiley))
/* 1063 */       return true; 
/* 1064 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getFloorLevel() {
/* 1070 */     return this.floorLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final void setFloorLevel() {
/* 1076 */     this.floorLevel = this.heightOffset / 30;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void buildProgress(int numSteps) {
/* 1086 */     if (numSteps > 127)
/*      */     {
/* 1088 */       numSteps = 127;
/*      */     }
/*      */     
/* 1091 */     if (getFloorState() == StructureConstants.FloorState.BUILDING) {
/*      */       
/* 1093 */       setState((byte)(getState() + numSteps));
/*      */     }
/*      */     else {
/*      */       
/* 1097 */       logger.log(Level.WARNING, "buildProgress method called on floor when floor was not in buildable state: " + 
/* 1098 */           getId() + " " + this.floorState
/* 1099 */           .toString());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final VolaTile getTile() {
/*      */     try {
/* 1108 */       Zone zone = Zones.getZone(this.tilex, this.tiley, (getLayer() == 0));
/* 1109 */       VolaTile toReturn = zone.getTileOrNull(this.tilex, this.tiley);
/* 1110 */       if (toReturn != null) {
/*      */         
/* 1112 */         if (toReturn.isTransition())
/* 1113 */           return Zones.getZone(this.tilex, this.tiley, false).getOrCreateTile(this.tilex, this.tiley); 
/* 1114 */         return toReturn;
/*      */       } 
/*      */       
/* 1117 */       logger.log(Level.WARNING, "Tile not in zone, this shouldn't happen " + this.tilex + ", " + this.tiley);
/*      */     }
/* 1119 */     catch (NoSuchZoneException nsz) {
/*      */       
/* 1121 */       logger.log(Level.WARNING, "This shouldn't happen " + this.tilex + ", " + this.tiley, (Throwable)nsz);
/*      */     } 
/* 1123 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Village getVillage() {
/* 1128 */     VolaTile t = getTile();
/* 1129 */     if (t != null)
/*      */     {
/* 1131 */       if (t.getVillage() != null)
/* 1132 */         return t.getVillage(); 
/*      */     }
/* 1134 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getDamageModifier() {
/* 1140 */     return 100.0F / Math.max(1.0F, this.currentQL * (100.0F - this.damage) / 100.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean poll(long currTime, VolaTile t, Structure struct) {
/* 1145 */     if (struct == null)
/*      */     {
/* 1147 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1154 */     HighwayPos highwaypos = MethodsHighways.getHighwayPos(this);
/* 1155 */     if (highwaypos != null && MethodsHighways.onHighway(highwaypos))
/*      */     {
/* 1157 */       return false;
/*      */     }
/* 1159 */     if (currTime - struct.getCreationDate() <= 172800000L)
/*      */     {
/* 1161 */       return false;
/*      */     }
/* 1163 */     float mod = 1.0F;
/* 1164 */     Village village = getVillage();
/* 1165 */     if (village != null) {
/*      */ 
/*      */       
/* 1168 */       if (village.moreThanMonthLeft()) {
/* 1169 */         return false;
/*      */       }
/* 1171 */       if (!village.lessThanWeekLeft()) {
/* 1172 */         mod *= 10.0F;
/*      */       
/*      */       }
/*      */     }
/* 1176 */     else if (Zones.getKingdom(this.tilex, this.tiley) == 0) {
/* 1177 */       mod *= 0.5F;
/*      */     } 
/*      */     
/* 1180 */     if (t != null && !t.isOnSurface())
/* 1181 */       mod *= 0.75F; 
/* 1182 */     if ((float)(currTime - this.lastUsed) > (Servers.localServer.testServer ? (60000.0F * mod) : (8.64E7F * mod)) && !hasNoDecay()) {
/*      */ 
/*      */ 
/*      */       
/* 1186 */       long ownerId = struct.getOwnerId();
/* 1187 */       if (ownerId == -10L) {
/*      */ 
/*      */         
/* 1190 */         this.damage += 20.0F + Server.rand.nextFloat() * 10.0F;
/*      */       }
/*      */       else {
/*      */         
/* 1194 */         boolean ownerIsInactive = false;
/* 1195 */         long aMonth = Servers.isThisATestServer() ? 86400000L : 2419200000L;
/* 1196 */         PlayerInfo pInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(ownerId);
/* 1197 */         if (pInfo == null) {
/*      */           
/* 1199 */           ownerIsInactive = true;
/* 1200 */         } else if (pInfo.lastLogin == 0L && pInfo.lastLogout < System.currentTimeMillis() - 3L * aMonth) {
/*      */           
/* 1202 */           ownerIsInactive = true;
/*      */         } 
/* 1204 */         if (ownerIsInactive) {
/* 1205 */           this.damage += 3.0F;
/*      */         }
/* 1207 */         if (village == null && t != null) {
/*      */           
/* 1209 */           Village v = Villages.getVillageWithPerimeterAt(t.tilex, t.tiley, t.isOnSurface());
/* 1210 */           if (v != null)
/*      */           {
/* 1212 */             if (!v.isCitizen(ownerId))
/*      */             {
/* 1214 */               if (ownerIsInactive)
/* 1215 */                 this.damage += 3.0F; 
/*      */             }
/*      */           }
/*      */         } 
/*      */       } 
/* 1220 */       setLastUsed(currTime);
/* 1221 */       if (setDamage(this.damage + 0.1F * getDamageModifier()))
/*      */       {
/* 1223 */         return true;
/*      */       }
/*      */     } 
/* 1226 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getCurrentQualityLevel() {
/* 1232 */     return this.currentQL * Math.max(1.0F, 100.0F - this.damage) / 100.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getRepairItemTemplate() {
/* 1238 */     if (isWood())
/* 1239 */       return 22; 
/* 1240 */     if (isStone())
/* 1241 */       return 132; 
/* 1242 */     if (isSlate())
/*      */     {
/* 1244 */       return 770;
/*      */     }
/* 1246 */     if (isMarble())
/*      */     {
/* 1248 */       return 786;
/*      */     }
/* 1250 */     if (isSandstone())
/*      */     {
/* 1252 */       return 1121;
/*      */     }
/* 1254 */     if (isGold())
/*      */     {
/* 1256 */       return 44;
/*      */     }
/* 1258 */     if (isSilver())
/*      */     {
/* 1260 */       return 45;
/*      */     }
/* 1262 */     if (isIron())
/*      */     {
/* 1264 */       return 46;
/*      */     }
/* 1266 */     if (isSteel())
/*      */     {
/* 1268 */       return 205;
/*      */     }
/* 1270 */     if (isCopper())
/*      */     {
/* 1272 */       return 47;
/*      */     }
/* 1274 */     if (isThatch())
/*      */     {
/* 1276 */       return 756;
/*      */     }
/* 1278 */     if (isClay())
/*      */     {
/* 1280 */       return 130;
/*      */     }
/* 1282 */     return 22;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getStartX() {
/* 1288 */     return getTileX();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getStartY() {
/* 1294 */     return getTileY();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMinX() {
/* 1300 */     return getTileX();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMinY() {
/* 1306 */     return getTileY();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supports() {
/* 1312 */     return true;
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
/*      */   public final boolean supports(StructureSupport support) {
/* 1325 */     if (!supports())
/* 1326 */       return false; 
/* 1327 */     if (support.isFloor()) {
/*      */       
/* 1329 */       if (getFloorLevel() == support.getFloorLevel())
/*      */       {
/* 1331 */         if (getStartX() == support.getStartX())
/*      */         {
/* 1333 */           if (getEndY() == support.getStartY() || getStartY() == support.getEndY()) {
/* 1334 */             return true;
/*      */           
/*      */           }
/*      */         }
/* 1338 */         else if (getStartY() == support.getStartY())
/*      */         {
/* 1340 */           if (getEndX() == support.getStartX() || getStartX() == support.getEndX()) {
/* 1341 */             return true;
/*      */           
/*      */           }
/*      */         }
/*      */       
/*      */       }
/*      */     }
/* 1348 */     else if (!support.supports()) {
/*      */       
/* 1350 */       if (support.getFloorLevel() == getFloorLevel())
/*      */       {
/* 1352 */         return isOnSideOfThis(support);
/*      */       }
/*      */     }
/* 1355 */     else if (support.getFloorLevel() >= getFloorLevel() - 1 && support.getFloorLevel() <= getFloorLevel()) {
/*      */       
/* 1357 */       return isOnSideOfThis(support);
/*      */     } 
/*      */     
/* 1360 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFloorZ() {
/* 1366 */     return this.heightOffset / 10.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getMinZ() {
/* 1372 */     return Zones.getHeightForNode(this.tilex, this.tiley, getLayer()) + getFloorZ();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getMaxZ() {
/* 1378 */     return getMinZ() + (isRoof() ? 2.0F : 0.25F);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWithinZ(float maxZ, float minZ, boolean followGround) {
/* 1384 */     return (getFloorLevel() > 0 && minZ <= getMaxZ() && maxZ >= getMinZ());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean equals(StructureSupport support) {
/* 1390 */     if (this == support)
/* 1391 */       return true; 
/* 1392 */     if (support == null)
/* 1393 */       return false; 
/* 1394 */     return (support.getId() == getId());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean equals(Object other) {
/* 1400 */     if (this == other)
/* 1401 */       return true; 
/* 1402 */     if (other == null)
/* 1403 */       return false; 
/* 1404 */     if (getClass() != other.getClass())
/* 1405 */       return false; 
/* 1406 */     Floor support = (Floor)other;
/* 1407 */     return (support.getId() == getId());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1418 */     int prime = 31;
/* 1419 */     int result = 1;
/* 1420 */     result = 31 * result + (int)getId();
/* 1421 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean isOnSideOfThis(StructureSupport support) {
/* 1426 */     if (support.isHorizontal()) {
/*      */       
/* 1428 */       if (support.getMinX() == getMinX() && (support
/* 1429 */         .getMinY() == getMinY() || support.getMinY() == getMinY() + 1))
/*      */       {
/* 1431 */         return true;
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1436 */     else if (support.getMinY() == getMinY() && (support
/* 1437 */       .getMinX() == getMinX() || support.getMinX() == getMinX() + 1)) {
/*      */       
/* 1439 */       return true;
/*      */     } 
/*      */     
/* 1442 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getEndX() {
/* 1448 */     return getStartX() + 1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getEndY() {
/* 1454 */     return getStartY() + 1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSupportedByGround() {
/* 1460 */     return (getFloorLevel() == 0);
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
/* 1471 */     return "Floor [number=" + this.number + ", structureId=" + this.structureId + ", type=" + this.type + "]";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getTempId() {
/* 1477 */     return -10L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canBeAlwaysLit() {
/* 1488 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canBeAutoFilled() {
/* 1499 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canBeAutoLit() {
/* 1510 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean canBePeggedByPlayer() {
/* 1521 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canBePlanted() {
/* 1532 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean canBeSealedByPlayer() {
/* 1543 */     return false;
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
/*      */   public boolean canChangeCreator() {
/* 1555 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableDecay() {
/* 1566 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableDestroy() {
/* 1577 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableDrag() {
/* 1588 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableDrop() {
/* 1599 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableEatAndDrink() {
/* 1610 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableImprove() {
/* 1621 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableLocking() {
/* 1632 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableLockpicking() {
/* 1643 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableMoveable() {
/* 1654 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean canDisableOwnerMoveing() {
/* 1665 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean canDisableOwnerTurning() {
/* 1676 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisablePainting() {
/* 1687 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisablePut() {
/* 1698 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableRepair() {
/* 1709 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableRuneing() {
/* 1720 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableSpellTarget() {
/* 1731 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableTake() {
/* 1742 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableTurning() {
/* 1753 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canHaveCourier() {
/* 1764 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canHaveDakrMessenger() {
/* 1775 */     return false;
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
/*      */   public String getCreatorName() {
/* 1787 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getDamage() {
/* 1798 */     return this.damage;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getQualityLevel() {
/* 1809 */     return this.currentQL;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasCourier() {
/* 1820 */     return this.permissions.hasPermission(Permissions.Allow.HAS_COURIER.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasDarkMessenger() {
/* 1831 */     return this.permissions.hasPermission(Permissions.Allow.HAS_DARK_MESSENGER.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasNoDecay() {
/* 1842 */     return this.permissions.hasPermission(Permissions.Allow.DECAY_DISABLED.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAlwaysLit() {
/* 1853 */     return this.permissions.hasPermission(Permissions.Allow.ALWAYS_LIT.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAutoFilled() {
/* 1864 */     return this.permissions.hasPermission(Permissions.Allow.AUTO_FILL.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAutoLit() {
/* 1875 */     return this.permissions.hasPermission(Permissions.Allow.AUTO_LIGHT.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isIndestructible() {
/* 1886 */     return this.permissions.hasPermission(Permissions.Allow.NO_BASH.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoDrag() {
/* 1897 */     return this.permissions.hasPermission(Permissions.Allow.NO_DRAG.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoDrop() {
/* 1908 */     return this.permissions.hasPermission(Permissions.Allow.NO_DROP.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoEatOrDrink() {
/* 1919 */     return this.permissions.hasPermission(Permissions.Allow.NO_EAT_OR_DRINK.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoImprove() {
/* 1930 */     return this.permissions.hasPermission(Permissions.Allow.NO_IMPROVE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoMove() {
/* 1941 */     return this.permissions.hasPermission(Permissions.Allow.NOT_MOVEABLE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoPut() {
/* 1952 */     return this.permissions.hasPermission(Permissions.Allow.NO_PUT.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoRepair() {
/* 1963 */     return this.permissions.hasPermission(Permissions.Allow.NO_REPAIR.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoTake() {
/* 1974 */     return this.permissions.hasPermission(Permissions.Allow.NO_TAKE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNotLockable() {
/* 1985 */     return this.permissions.hasPermission(Permissions.Allow.NOT_LOCKABLE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNotLockpickable() {
/* 1996 */     return this.permissions.hasPermission(Permissions.Allow.NOT_LOCKPICKABLE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNotPaintable() {
/* 2007 */     return this.permissions.hasPermission(Permissions.Allow.NOT_PAINTABLE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNotRuneable() {
/* 2018 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNotSpellTarget() {
/* 2029 */     return this.permissions.hasPermission(Permissions.Allow.NO_SPELLS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNotTurnable() {
/* 2040 */     return this.permissions.hasPermission(Permissions.Allow.NOT_TURNABLE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOwnerMoveable() {
/* 2051 */     return this.permissions.hasPermission(Permissions.Allow.OWNER_MOVEABLE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOwnerTurnable() {
/* 2062 */     return this.permissions.hasPermission(Permissions.Allow.OWNER_TURNABLE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPlanted() {
/* 2073 */     return this.permissions.hasPermission(Permissions.Allow.PLANTED.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSealedByPlayer() {
/* 2084 */     if (this.permissions.hasPermission(Permissions.Allow.SEALED_BY_PLAYER.getBit()))
/* 2085 */       return true; 
/* 2086 */     return false;
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
/*      */   public void setCreator(String aNewCreator) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract boolean setDamage(float paramFloat);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHasCourier(boolean aCourier) {
/* 2117 */     this.permissions.setPermissionBit(Permissions.Allow.HAS_COURIER.getBit(), aCourier);
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
/*      */   public void setHasDarkMessenger(boolean aDarkmessenger) {
/* 2129 */     this.permissions.setPermissionBit(Permissions.Allow.HAS_DARK_MESSENGER.getBit(), aDarkmessenger);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHasNoDecay(boolean aNoDecay) {
/* 2140 */     this.permissions.setPermissionBit(Permissions.Allow.DECAY_DISABLED.getBit(), aNoDecay);
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
/*      */   public void setIsAlwaysLit(boolean aAlwaysLit) {
/* 2152 */     this.permissions.setPermissionBit(Permissions.Allow.ALWAYS_LIT.getBit(), aAlwaysLit);
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
/*      */   public void setIsAutoFilled(boolean aAutoFill) {
/* 2164 */     this.permissions.setPermissionBit(Permissions.Allow.AUTO_FILL.getBit(), aAutoFill);
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
/*      */   public void setIsAutoLit(boolean aAutoLight) {
/* 2176 */     this.permissions.setPermissionBit(Permissions.Allow.AUTO_LIGHT.getBit(), aAutoLight);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsIndestructible(boolean aNoDestroy) {
/* 2187 */     this.permissions.setPermissionBit(Permissions.Allow.NO_BASH.getBit(), aNoDestroy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNoDrag(boolean aNoDrag) {
/* 2198 */     this.permissions.setPermissionBit(Permissions.Allow.NO_DRAG.getBit(), aNoDrag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNoDrop(boolean aNoDrop) {
/* 2209 */     this.permissions.setPermissionBit(Permissions.Allow.NO_DROP.getBit(), aNoDrop);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNoEatOrDrink(boolean aNoEatOrDrink) {
/* 2220 */     this.permissions.setPermissionBit(Permissions.Allow.NO_EAT_OR_DRINK.getBit(), aNoEatOrDrink);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNoImprove(boolean aNoImprove) {
/* 2231 */     this.permissions.setPermissionBit(Permissions.Allow.NO_IMPROVE.getBit(), aNoImprove);
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
/*      */   public void setIsNoMove(boolean aNoMove) {
/* 2243 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_MOVEABLE.getBit(), aNoMove);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNoPut(boolean aNoPut) {
/* 2254 */     this.permissions.setPermissionBit(Permissions.Allow.NO_PUT.getBit(), aNoPut);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNoRepair(boolean aNoRepair) {
/* 2265 */     this.permissions.setPermissionBit(Permissions.Allow.NO_REPAIR.getBit(), aNoRepair);
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
/*      */   public void setIsNoTake(boolean aNoTake) {
/* 2277 */     this.permissions.setPermissionBit(Permissions.Allow.NO_TAKE.getBit(), aNoTake);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNotLockable(boolean aNoLock) {
/* 2288 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_LOCKABLE.getBit(), aNoLock);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNotLockpickable(boolean aNoLockpick) {
/* 2299 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_LOCKPICKABLE.getBit(), aNoLockpick);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNotPaintable(boolean aNoPaint) {
/* 2310 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_PAINTABLE.getBit(), aNoPaint);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNotRuneable(boolean aNoRune) {
/* 2321 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_RUNEABLE.getBit(), aNoRune);
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
/*      */   public void setIsNotSpellTarget(boolean aNoSpells) {
/* 2333 */     this.permissions.setPermissionBit(Permissions.Allow.NO_SPELLS.getBit(), aNoSpells);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNotTurnable(boolean aNoTurn) {
/* 2344 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_TURNABLE.getBit(), aNoTurn);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsOwnerMoveable(boolean aOwnerMove) {
/* 2355 */     this.permissions.setPermissionBit(Permissions.Allow.OWNER_MOVEABLE.getBit(), aOwnerMove);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsOwnerTurnable(boolean aOwnerTurn) {
/* 2366 */     this.permissions.setPermissionBit(Permissions.Allow.OWNER_TURNABLE.getBit(), aOwnerTurn);
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
/*      */   public void setIsPlanted(boolean aPlant) {
/* 2378 */     this.permissions.setPermissionBit(Permissions.Allow.PLANTED.getBit(), aPlant);
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
/*      */   public void setIsSealedByPlayer(boolean aSealed) {
/* 2390 */     this.permissions.setPermissionBit(Permissions.Allow.SEALED_BY_PLAYER.getBit(), aSealed);
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
/*      */   public abstract boolean setQualityLevel(float paramFloat);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOriginalQualityLevel(float newQL) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void savePermissions();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isOnSouthBorder(TilePos pos) {
/* 2429 */     return false;
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
/*      */   public final boolean isOnNorthBorder(TilePos pos) {
/* 2441 */     return false;
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
/*      */   public final boolean isOnWestBorder(TilePos pos) {
/* 2453 */     return false;
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
/*      */   public final boolean isOnEastBorder(TilePos pos) {
/* 2465 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\Floor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */