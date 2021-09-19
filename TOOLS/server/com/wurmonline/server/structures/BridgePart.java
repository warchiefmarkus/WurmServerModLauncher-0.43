/*      */ package com.wurmonline.server.structures;
/*      */ 
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
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.BridgeConstants;
/*      */ import com.wurmonline.shared.constants.SoundNames;
/*      */ import java.awt.geom.Rectangle2D;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.HashSet;
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
/*      */ public abstract class BridgePart
/*      */   implements MiscConstants, TimeConstants, Blocker, IFloor, SoundNames, Permissions.IAllow
/*      */ {
/*   64 */   private static final Logger logger = Logger.getLogger(Wall.class.getName());
/*      */   
/*   66 */   private long structureId = -10L;
/*   67 */   private int number = -10;
/*      */   
/*      */   float originalQL;
/*      */   
/*      */   float currentQL;
/*      */   float damage;
/*      */   private int tilex;
/*      */   private int tiley;
/*      */   private int realHeight;
/*      */   long lastUsed;
/*      */   private BridgeConstants.BridgeType type;
/*      */   private BridgeConstants.BridgeMaterial material;
/*      */   private BridgeConstants.BridgeState bridgePartState;
/*   80 */   protected byte dbState = -1;
/*   81 */   private byte dir = 0;
/*   82 */   private byte slope = 0;
/*      */ 
/*      */   
/*   85 */   private int northExit = -1;
/*   86 */   private int eastExit = -1;
/*   87 */   private int southExit = -1;
/*   88 */   private int westExit = -1;
/*      */   
/*   90 */   byte roadType = 0;
/*   91 */   int layer = 0;
/*      */   
/*   93 */   private int materialCount = -1;
/*      */   
/*   95 */   private static final Set<DbBridgePart> bridgeParts = new HashSet<>();
/*      */   
/*      */   private static final String GETALLBRIDGEPARTS = "SELECT * FROM BRIDGEPARTS";
/*   98 */   private static final Vector3f normal = new Vector3f(0.0F, 0.0F, 1.0F);
/*      */   private static Rectangle2D verticalBlocker;
/*  100 */   Permissions permissions = new Permissions();
/*      */ 
/*      */ 
/*      */   
/*      */   private Vector3f centerPoint;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BridgePart(int id, BridgeConstants.BridgeType floorType, int aTileX, int aTileY, byte aDbState, int aHeightOffset, float ql, long structure, BridgeConstants.BridgeMaterial floorMaterial, float origQl, float dam, int materialcount, long lastmaint, byte aDir, byte aSlope, int aNorthExit, int aEastExit, int aSouthExit, int aWestExit, byte roadType, int layer) {
/*  110 */     setNumber(id);
/*  111 */     this.type = floorType;
/*  112 */     this.tilex = aTileX;
/*  113 */     this.tiley = aTileY;
/*  114 */     this.dbState = aDbState;
/*  115 */     this.bridgePartState = BridgeConstants.BridgeState.fromByte(this.dbState);
/*  116 */     this.realHeight = aHeightOffset;
/*  117 */     this.currentQL = ql;
/*  118 */     this.originalQL = origQl;
/*  119 */     this.damage = dam;
/*  120 */     this.structureId = structure;
/*  121 */     this.material = floorMaterial;
/*  122 */     this.materialCount = materialcount;
/*  123 */     this.lastUsed = lastmaint;
/*  124 */     this.dir = aDir;
/*  125 */     this.slope = aSlope;
/*  126 */     this.northExit = aNorthExit;
/*  127 */     this.eastExit = aEastExit;
/*  128 */     this.southExit = aSouthExit;
/*  129 */     this.westExit = aWestExit;
/*  130 */     this.roadType = roadType;
/*  131 */     this.layer = layer;
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
/*      */   public BridgePart(BridgeConstants.BridgeType floorType, int aTileX, int aTileY, int height, float ql, long structure, BridgeConstants.BridgeMaterial floorMaterial, byte aDir, byte aSlope, int aNorthExit, int aEastExit, int aSouthExit, int aWestExit, byte roadType, int layer) {
/*  158 */     this.type = floorType;
/*  159 */     this.tilex = aTileX;
/*  160 */     this.tiley = aTileY;
/*  161 */     this.bridgePartState = BridgeConstants.BridgeState.PLANNED;
/*  162 */     this.dbState = this.bridgePartState.getCode();
/*  163 */     this.damage = 0.0F;
/*  164 */     this.realHeight = height;
/*  165 */     this.currentQL = ql;
/*  166 */     this.originalQL = ql;
/*  167 */     this.structureId = structure;
/*  168 */     this.material = floorMaterial;
/*  169 */     this.materialCount = 0;
/*  170 */     this.dir = aDir;
/*  171 */     this.slope = aSlope;
/*  172 */     this.northExit = aNorthExit;
/*  173 */     this.eastExit = aEastExit;
/*  174 */     this.southExit = aSouthExit;
/*  175 */     this.westExit = aWestExit;
/*  176 */     this.roadType = roadType;
/*  177 */     this.layer = layer;
/*      */ 
/*      */     
/*      */     try {
/*  181 */       save();
/*      */     }
/*  183 */     catch (IOException e) {
/*      */ 
/*      */       
/*  186 */       logger.log(Level.WARNING, e.getMessage(), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFloor() {
/*  193 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isRoof() {
/*  199 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isStair() {
/*  205 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Vector3f getNormal() {
/*  211 */     return normal;
/*      */   }
/*      */ 
/*      */   
/*      */   private final Vector3f calculateCenterPoint() {
/*  216 */     return new Vector3f((this.tilex * 4 + 2), (this.tiley * 4 + 2), (getRealHeight() + this.slope) / 10.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   private final Rectangle2D getVerticalBlocker() {
/*  221 */     if (this.slope == 0)
/*  222 */       return null; 
/*  223 */     if (verticalBlocker == null) {
/*      */       
/*  225 */       if (this.dir == 0 || this.dir == 4)
/*      */       {
/*      */         
/*  228 */         if (this.slope < 0) {
/*      */           
/*  230 */           verticalBlocker = new Rectangle2D.Float((this.tilex * 4), (this.tiley * 4), 4.0F, Math.abs(this.slope / 10.0F));
/*      */         
/*      */         }
/*      */         else {
/*      */           
/*  235 */           verticalBlocker = new Rectangle2D.Float((this.tilex * 4), ((this.tiley + 1) * 4), 4.0F, Math.abs(this.slope / 10.0F));
/*      */         } 
/*      */       }
/*  238 */       if (this.dir == 6 || this.dir == 2)
/*      */       {
/*      */         
/*  241 */         if (this.slope < 0) {
/*      */           
/*  243 */           verticalBlocker = new Rectangle2D.Float((this.tilex * 4), (this.tiley * 4), 4.0F, Math.abs(this.slope / 10.0F));
/*      */         }
/*      */         else {
/*      */           
/*  247 */           verticalBlocker = new Rectangle2D.Float(((this.tilex + 1) * 4), (this.tiley * 4), 4.0F, Math.abs(this.slope / 10.0F));
/*      */         } 
/*      */       }
/*      */     } 
/*  251 */     return verticalBlocker;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Vector3f getCenterPoint() {
/*  257 */     if (this.centerPoint == null)
/*  258 */       this.centerPoint = calculateCenterPoint(); 
/*  259 */     return this.centerPoint;
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
/*  270 */     return this.tilex;
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
/*  281 */     return this.tiley;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getPositionX() {
/*  287 */     return (this.tilex * 4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getPositionY() {
/*  293 */     return (this.tiley * 4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getHeightOffset() {
/*  302 */     return this.realHeight;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getHeight() {
/*  308 */     if (isOnSurface()) {
/*      */ 
/*      */       
/*  311 */       int i = Tiles.decodeHeight(Server.surfaceMesh.getTile(this.tilex, this.tiley));
/*  312 */       return this.realHeight - i;
/*      */     } 
/*      */     
/*  315 */     int ht = Tiles.decodeHeight(Server.caveMesh.getTile(this.tilex, this.tiley));
/*  316 */     return this.realHeight - ht;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRealHeight() {
/*  322 */     return this.realHeight;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getDir() {
/*  327 */     return this.dir;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getSlope() {
/*  332 */     return this.slope;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasHouseNorthExit() {
/*  341 */     return (this.northExit > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasHouseEastExit() {
/*  350 */     return (this.eastExit > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasHouseSouthExit() {
/*  359 */     return (this.southExit > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasHouseWestExit() {
/*  368 */     return (this.westExit > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasHouseExit() {
/*  377 */     return (hasHouseNorthExit() || hasHouseEastExit() || hasHouseSouthExit() || hasHouseWestExit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasNorthExit() {
/*  386 */     return (this.northExit > -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasEastExit() {
/*  395 */     return (this.eastExit > -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasSouthExit() {
/*  404 */     return (this.southExit > -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasWestExit() {
/*  413 */     return (this.westExit > -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasAnExit() {
/*  422 */     return (hasNorthExit() || hasEastExit() || hasSouthExit() || hasWestExit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFinished() {
/*  431 */     return (this.bridgePartState == BridgeConstants.BridgeState.COMPLETED);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isMetal() {
/*  438 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isWood() {
/*  444 */     return (this.material == BridgeConstants.BridgeMaterial.WOOD);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isStone() {
/*  450 */     return (this.material == BridgeConstants.BridgeMaterial.BRICK);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSlate() {
/*  456 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isThatch() {
/*  462 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isMarble() {
/*  468 */     return (this.material == BridgeConstants.BridgeMaterial.MARBLE);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSandstone() {
/*  474 */     return (this.material == BridgeConstants.BridgeMaterial.SANDSTONE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BridgeConstants.BridgeType getType() {
/*  482 */     return this.type;
/*      */   }
/*      */ 
/*      */   
/*      */   public BridgeConstants.BridgeMaterial getMaterial() {
/*  487 */     return this.material;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int minRequiredSkill() {
/*  493 */     switch (this.material) {
/*      */       
/*      */       case COMPLETED:
/*  496 */         return 10;
/*      */       case PLANNED:
/*  498 */         return 10;
/*      */       case null:
/*  500 */         return 30;
/*      */       case null:
/*      */       case null:
/*      */       case null:
/*      */       case null:
/*      */       case null:
/*      */       case null:
/*  507 */         return 40;
/*      */     } 
/*  509 */     return 99;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void save() throws IOException;
/*      */ 
/*      */   
/*      */   public long getId() {
/*  518 */     return Tiles.getBridgePartId(this.tilex, this.tiley, this.realHeight, (byte)this.layer, (byte)0);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getHeightOffsetFromWurmId(long wurmId) {
/*  523 */     return Tiles.decodeHeightOffset(wurmId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BridgeConstants.BridgeState getBridgePartState() {
/*  531 */     return this.bridgePartState;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getFloorStageAsString() {
/*  536 */     return this.bridgePartState.getDescription();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLastUsed() {
/*  546 */     return this.lastUsed;
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
/*  565 */     return this.number;
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
/*  576 */     this.number = aNumber;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getOriginalQL() {
/*  586 */     return this.originalQL;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getCurrentQL() {
/*  596 */     return this.currentQL;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getState() {
/*  606 */     return this.dbState;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract void setState(byte paramByte);
/*      */ 
/*      */ 
/*      */   
/*      */   public void incBridgePartStage() {
/*  616 */     byte currentStage = this.bridgePartState.getCode();
/*  617 */     byte nextStage = (byte)(currentStage + 1);
/*  618 */     this.bridgePartState = BridgeConstants.BridgeState.fromByte(nextStage);
/*  619 */     setState(nextStage);
/*  620 */     setMaterialCount(0);
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
/*      */   public void setBridgePartState(BridgeConstants.BridgeState aBridgeState) {
/*  634 */     if (aBridgeState.isBeingBuilt() && this.bridgePartState == BridgeConstants.BridgeState.PLANNED) {
/*  635 */       setDamage(0.0F);
/*      */     }
/*  637 */     this.bridgePartState = aBridgeState;
/*  638 */     switch (this.bridgePartState) {
/*      */       
/*      */       case COMPLETED:
/*  641 */         setState(BridgeConstants.BridgeState.COMPLETED.getCode());
/*      */         return;
/*      */       
/*      */       case PLANNED:
/*  645 */         setState(BridgeConstants.BridgeState.PLANNED.getCode());
/*      */         return;
/*      */     } 
/*      */     
/*  649 */     setState(this.bridgePartState.getCode());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaterialCount() {
/*  656 */     return this.materialCount;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getNorthExit() {
/*  661 */     return this.northExit;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getEastExit() {
/*  666 */     return this.eastExit;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getSouthExit() {
/*  671 */     return this.southExit;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getWestExit() {
/*  676 */     return this.westExit;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getNorthExitFloorLevel() {
/*  681 */     if (this.northExit == -1)
/*  682 */       return -1; 
/*  683 */     return this.northExit / 30;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getEastExitFloorLevel() {
/*  688 */     if (this.eastExit == -1)
/*  689 */       return -1; 
/*  690 */     return this.eastExit / 30;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getSouthExitFloorLevel() {
/*  695 */     if (this.southExit == -1)
/*  696 */       return -1; 
/*  697 */     return this.southExit / 30;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getWestExitFloorLevel() {
/*  702 */     if (this.westExit == -1)
/*  703 */       return -1; 
/*  704 */     return this.westExit / 30;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMaterialCount(int count) {
/*  709 */     this.materialCount = count;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getRoadType() {
/*  714 */     return this.roadType;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getLayer() {
/*  719 */     return (byte)this.layer;
/*      */   }
/*      */ 
/*      */   
/*      */   public abstract void saveRoadType(byte paramByte);
/*      */   
/*      */   public abstract void delete();
/*      */   
/*      */   public String getFullName() {
/*  728 */     if (this.bridgePartState == BridgeConstants.BridgeState.PLANNED)
/*      */     {
/*  730 */       return "Planned " + getName();
/*      */     }
/*  732 */     if (this.bridgePartState.isBeingBuilt())
/*      */     {
/*  734 */       return "Unfinished " + getName();
/*      */     }
/*  736 */     return getName();
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
/*  747 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Vector3f isBlocking(Creature creature, Vector3f startPos, Vector3f endPos, Vector3f aNormal, int blockType, long target, boolean followGround) {
/*  757 */     if (target == getId())
/*  758 */       return null; 
/*  759 */     if (isAPlan())
/*      */     {
/*  761 */       return null;
/*      */     }
/*  763 */     Vector3f inter = getIntersectionPoint(startPos, endPos, aNormal, creature, blockType);
/*  764 */     return inter;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isDoor() {
/*  770 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isTile() {
/*  776 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean canBeOpenedBy(Creature creature, boolean wentThroughDoor) {
/*  782 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getBlockPercent(Creature creature) {
/*  789 */     return Math.min(100, Math.max(0, this.bridgePartState.getCode() * 14));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isWithinFloorLevels(int maxFloorLevel, int minFloorLevel) {
/*  796 */     int maxHt = (maxFloorLevel + 1) * 30;
/*  797 */     int minHt = minFloorLevel * 30;
/*      */     
/*  799 */     int ht = getRealHeight();
/*      */     
/*  801 */     if (getType().isSupportType() && ht > maxHt) {
/*  802 */       return true;
/*      */     }
/*  804 */     return (ht > minHt && ht < maxHt);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFloorZ() {
/*  810 */     return 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getMinZ() {
/*  816 */     return getRealHeight() / 10.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getMaxZ() {
/*  822 */     return getMinZ() + 0.25F + this.slope / 10.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWithinZ(float maxZ, float minZ, boolean followGround) {
/*  828 */     return (minZ <= getMaxZ() && maxZ >= getMinZ());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Vector3f getFloorIntersection(Vector3f startPos, Vector3f endPos, Vector3f aNormal, Creature creature, int blockType) {
/*  834 */     Vector3f diff = getCenterPoint().subtract(startPos);
/*  835 */     float steps = diff.z / aNormal.z;
/*  836 */     Vector3f intersection = startPos.add(aNormal.mult(steps));
/*  837 */     Vector3f diffend = endPos.subtract(startPos);
/*  838 */     Vector3f interDiff = intersection.subtract(startPos);
/*  839 */     if (diffend.length() < interDiff.length())
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  844 */       return null;
/*      */     }
/*      */     
/*  847 */     float u = getNormal().dot(getCenterPoint().subtract(startPos)) / getNormal().dot(endPos.subtract(startPos));
/*  848 */     if (isWithinFloorBounds(intersection, creature, blockType)) {
/*      */       
/*  850 */       if (u >= 0.0F && u <= 1.0F)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/*  855 */         return intersection;
/*      */       }
/*      */ 
/*      */       
/*  859 */       return null;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  865 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Vector3f getVerticalIntersection(Vector3f startPos, Vector3f endPos, Vector3f aNormal, Creature creature) {
/*  871 */     if (getFloorLevel() == 0)
/*      */     {
/*      */       
/*  874 */       if (startPos.z <= getMinZ())
/*  875 */         startPos.z = getMinZ() + 0.5F; 
/*      */     }
/*  877 */     Vector3f diff = getCenterPoint().subtract(startPos);
/*      */     
/*  879 */     Vector3f diffend = endPos.subtract(startPos);
/*      */     
/*  881 */     if (isHorizontal()) {
/*      */       
/*  883 */       float steps = diff.y / normal.y;
/*  884 */       Vector3f intersection = startPos.add(normal.mult(steps));
/*  885 */       Vector3f interDiff = intersection.subtract(startPos);
/*      */       
/*  887 */       if (diffend.length() + 0.01F < interDiff.length())
/*      */       {
/*  889 */         return null;
/*      */       }
/*  891 */       if (isWithinVerticalBounds(intersection, creature))
/*      */       {
/*      */         
/*  894 */         float u = getNormal().dot(getCenterPoint().subtract(startPos)) / getNormal().dot(endPos.subtract(startPos));
/*  895 */         if (u >= 0.0F && u <= 1.0F)
/*      */         {
/*  897 */           return intersection;
/*      */         }
/*  899 */         return null;
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  905 */       float steps = diff.x / normal.x;
/*  906 */       Vector3f intersection = startPos.add(normal.mult(steps));
/*  907 */       Vector3f interDiff = intersection.subtract(startPos);
/*  908 */       if (diffend.length() < interDiff.length())
/*      */       {
/*  910 */         return null;
/*      */       }
/*  912 */       if (isWithinVerticalBounds(intersection, creature)) {
/*      */ 
/*      */         
/*  915 */         float u = getNormal().dot(getCenterPoint().subtract(startPos)) / getNormal().dot(endPos.subtract(startPos));
/*  916 */         if (u >= 0.0F && u <= 1.0F)
/*      */         {
/*  918 */           return intersection;
/*      */         }
/*  920 */         return null;
/*      */       } 
/*      */     } 
/*  923 */     return null;
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
/*      */   public final Vector3f getIntersectionPoint(Vector3f startPos, Vector3f endPos, Vector3f aNormal, Creature creature, int blockType) {
/*  946 */     Vector3f intersection = getFloorIntersection(startPos, endPos, aNormal, creature, blockType);
/*  947 */     if (intersection == null) {
/*      */       
/*  949 */       if (blockType == 6)
/*      */       {
/*      */         
/*  952 */         return null;
/*      */       }
/*  954 */       getVerticalIntersection(startPos, endPos, aNormal, creature);
/*      */     } 
/*  956 */     return intersection;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean isWithinFloorBounds(Vector3f pointToCheck, Creature creature, int blockType) {
/*  961 */     if (pointToCheck.getY() >= (this.tiley * 4) && pointToCheck
/*  962 */       .getY() <= ((this.tiley + 1) * 4))
/*      */     {
/*  964 */       if (pointToCheck.getX() >= (this.tilex * 4) && pointToCheck.getX() <= ((this.tilex + 1) * 4)) {
/*      */         
/*  966 */         if (Servers.isThisATestServer())
/*  967 */           logger.info("WithinBounds?:" + getName() + " height checked:" + pointToCheck.getZ() + " against bridge real height:" + (
/*  968 */               getRealHeight() / 10.0F) + " (" + this.tilex + "," + this.tiley + ")"); 
/*  969 */         if (getType().isSupportType() && blockType == 4) {
/*      */ 
/*      */           
/*  972 */           if (pointToCheck.getZ() <= getMinZ() + 0.25F)
/*      */           {
/*  974 */             return true;
/*      */           }
/*      */         } else {
/*      */           
/*  978 */           return (pointToCheck.getZ() >= getMinZ() && pointToCheck.getZ() <= getMinZ() + 0.25F);
/*      */         } 
/*      */       }  } 
/*  981 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean isWithinVerticalBounds(Vector3f pointToCheck, Creature creature) {
/*  986 */     Rectangle2D rect = getVerticalBlocker();
/*  987 */     if (rect == null)
/*  988 */       return false; 
/*  989 */     if (this.dir == 0 || this.dir == 4) {
/*      */       
/*  991 */       if (pointToCheck.getY() >= rect.getY() - 0.10000000149011612D && pointToCheck
/*  992 */         .getY() <= rect.getY() + 0.10000000149011612D)
/*      */       {
/*  994 */         if (pointToCheck.getX() >= rect.getX() && pointToCheck.getX() <= rect.getX() + rect.getWidth() && 
/*  995 */           pointToCheck.getZ() >= getMinZ() && pointToCheck.getZ() <= getMinZ() + rect.getHeight()) {
/*  996 */           return true;
/*      */         
/*      */         }
/*      */       }
/*      */     }
/* 1001 */     else if (pointToCheck.getX() >= rect.getX() - 0.10000000149011612D && pointToCheck
/* 1002 */       .getX() <= rect.getX() + 0.10000000149011612D) {
/*      */       
/* 1004 */       if (pointToCheck.getY() >= rect.getY() && pointToCheck.getY() <= rect.getY() + rect.getWidth() && 
/* 1005 */         pointToCheck.getZ() >= getMinZ() && pointToCheck.getZ() <= getMinZ() + rect.getHeight()) {
/* 1006 */         return true;
/*      */       }
/*      */     } 
/* 1009 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void loadAllBridgeParts() throws IOException {
/* 1014 */     logger.log(Level.INFO, "Loading all bridge parts.");
/* 1015 */     long s = System.nanoTime();
/* 1016 */     Connection dbcon = null;
/* 1017 */     PreparedStatement ps = null;
/* 1018 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1021 */       dbcon = DbConnector.getZonesDbCon();
/* 1022 */       ps = dbcon.prepareStatement("SELECT * FROM BRIDGEPARTS");
/* 1023 */       rs = ps.executeQuery();
/* 1024 */       while (rs.next())
/*      */       {
/* 1026 */         int id = rs.getInt("ID");
/* 1027 */         BridgeConstants.BridgeType floorType = BridgeConstants.BridgeType.fromByte(rs.getByte("TYPE"));
/* 1028 */         BridgeConstants.BridgeMaterial floorMaterial = BridgeConstants.BridgeMaterial.fromByte(rs.getByte("MATERIAL"));
/* 1029 */         byte state = rs.getByte("STATE");
/*      */         
/* 1031 */         int stageCount = rs.getInt("STAGECOUNT");
/* 1032 */         int x = rs.getInt("TILEX");
/* 1033 */         int y = rs.getInt("TILEY");
/* 1034 */         long structureId = rs.getLong("STRUCTURE");
/* 1035 */         int h = rs.getInt("HEIGHTOFFSET");
/* 1036 */         float currentQL = rs.getFloat("CURRENTQL");
/* 1037 */         float origQL = rs.getFloat("ORIGINALQL");
/* 1038 */         float dam = rs.getFloat("DAMAGE");
/* 1039 */         byte dir = rs.getByte("DIR");
/* 1040 */         byte slope = rs.getByte("SLOPE");
/* 1041 */         long last = rs.getLong("LASTMAINTAINED");
/* 1042 */         int northExit = rs.getInt("NORTHEXIT");
/* 1043 */         int eastExit = rs.getInt("EASTEXIT");
/* 1044 */         int southExit = rs.getInt("SOUTHEXIT");
/* 1045 */         int westExit = rs.getInt("WESTEXIT");
/* 1046 */         byte roadType = rs.getByte("ROADTYPE");
/* 1047 */         int layer = rs.getInt("LAYER");
/*      */         
/* 1049 */         bridgeParts.add(new DbBridgePart(id, floorType, x, y, state, h, currentQL, structureId, floorMaterial, origQL, dam, stageCount, last, dir, slope, northExit, eastExit, southExit, westExit, roadType, layer));
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1054 */     catch (SQLException sqx) {
/*      */       
/* 1056 */       logger.log(Level.WARNING, "Failed to load bridge parts!" + sqx.getMessage(), sqx);
/* 1057 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1061 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1062 */       DbConnector.returnConnection(dbcon);
/* 1063 */       long e = System.nanoTime();
/* 1064 */       logger.log(Level.INFO, "Loaded " + bridgeParts.size() + " bridge parts. That took " + ((float)(e - s) / 1000000.0F) + " ms.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Set<BridgePart> getBridgePartsFor(long structureId) {
/* 1072 */     Set<BridgePart> toReturn = new HashSet<>();
/* 1073 */     for (BridgePart bridgePart : bridgeParts) {
/*      */       
/* 1075 */       if (bridgePart.getStructureId() == structureId)
/* 1076 */         toReturn.add(bridgePart); 
/*      */     } 
/* 1078 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getStructureId() {
/* 1087 */     return this.structureId;
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
/* 1098 */     this.structureId = aStructureId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAPlan() {
/* 1107 */     return (this.bridgePartState == BridgeConstants.BridgeState.PLANNED);
/*      */   }
/*      */ 
/*      */   
/*      */   public void revertToPlan() {
/* 1112 */     MethodsHighways.removeNearbyMarkers(this);
/* 1113 */     setBridgePartState(BridgeConstants.BridgeState.PLANNED);
/* 1114 */     setDamage(0.0F);
/* 1115 */     setQualityLevel(1.0F);
/* 1116 */     saveRoadType((byte)0);
/*      */     
/*      */     try {
/* 1119 */       save();
/*      */     }
/* 1121 */     catch (IOException e) {
/*      */       
/* 1123 */       logger.log(Level.WARNING, e.getMessage(), e);
/*      */     } 
/* 1125 */     VolaTile volaTile = Zones.getOrCreateTile(getTileX(), getTileY(), (this.layer == 0));
/* 1126 */     volaTile.updateBridgePart(this);
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
/*      */   public void destroyOrRevertToPlan() {
/* 1139 */     revertToPlan();
/*      */   }
/*      */ 
/*      */   
/*      */   Structure getStructure() {
/* 1144 */     Structure struct = null;
/*      */     
/*      */     try {
/* 1147 */       struct = Structures.getStructure(getStructureId());
/*      */     }
/* 1149 */     catch (NoSuchStructureException e) {
/*      */       
/* 1151 */       logger.log(Level.WARNING, " Failed to find Structures.getStructure(" + getStructureId() + " for a BridgePart about to be deleted: " + e.getMessage(), (Throwable)e);
/*      */     } 
/* 1153 */     return struct;
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
/*      */   public void destroy() {
/* 1165 */     delete();
/*      */     
/* 1167 */     bridgeParts.remove(this);
/*      */     
/* 1169 */     VolaTile volaTile = Zones.getOrCreateTile(getTileX(), getTileY(), (this.layer == 0));
/* 1170 */     volaTile.removeBridgePart(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getDamageModifierForItem(Item item) {
/* 1178 */     switch (this.material)
/*      */     
/*      */     { case COMPLETED:
/* 1181 */         if (item.isWeaponSlash()) {
/* 1182 */           mod = 0.03F;
/*      */         } else {
/* 1184 */           mod = 0.007F;
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
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1217 */         return mod;case null: if (item.isWeaponCrush()) { mod = 0.01F; } else { mod = 0.002F; }  return mod;case null: case null: case null: case null: case null: case null: if (item.isWeaponCrush()) { mod = 0.005F; } else { mod = 0.001F; }  return mod;case PLANNED: if (item.isWeaponAxe()) { mod = 0.03F; } else { mod = 0.007F; }  return mod; }  float mod = 0.0F; return mod;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isOnPvPServer() {
/* 1223 */     if (Zones.isOnPvPServer(this.tilex, this.tiley))
/* 1224 */       return true; 
/* 1225 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getFloorLevel() {
/* 1231 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void buildProgress(int numSteps) {
/* 1241 */     if (getBridgePartState().isBeingBuilt()) {
/*      */       
/* 1243 */       setMaterialCount(getMaterialCount() + numSteps);
/*      */     }
/*      */     else {
/*      */       
/* 1247 */       logger.log(Level.WARNING, "buildProgress method called on bridge part when bridge part was not in buildable state: " + 
/* 1248 */           getId() + " " + this.bridgePartState
/* 1249 */           .toString());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final VolaTile getTile() {
/*      */     try {
/* 1258 */       Zone zone = Zones.getZone(this.tilex, this.tiley, (this.layer == 0));
/* 1259 */       VolaTile toReturn = zone.getTileOrNull(this.tilex, this.tiley);
/* 1260 */       if (toReturn != null) {
/*      */         
/* 1262 */         if (toReturn.isTransition())
/* 1263 */           return Zones.getZone(this.tilex, this.tiley, false).getOrCreateTile(this.tilex, this.tiley); 
/* 1264 */         return toReturn;
/*      */       } 
/*      */       
/* 1267 */       logger.log(Level.WARNING, "Tile not in zone, this shouldn't happen " + this.tilex + ", " + this.tiley);
/*      */     }
/* 1269 */     catch (NoSuchZoneException nsz) {
/*      */       
/* 1271 */       logger.log(Level.WARNING, "This shouldn't happen " + this.tilex + ", " + this.tiley, (Throwable)nsz);
/*      */     } 
/* 1273 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Village getVillage() {
/* 1278 */     VolaTile t = getTile();
/* 1279 */     if (t != null)
/*      */     {
/* 1281 */       if (t.getVillage() != null)
/* 1282 */         return t.getVillage(); 
/*      */     }
/* 1284 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public final float getModByMaterial() {
/* 1289 */     switch (this.material) {
/*      */       
/*      */       case COMPLETED:
/* 1292 */         return 4.0F;
/*      */       
/*      */       case null:
/* 1295 */         return 10.0F;
/*      */       
/*      */       case null:
/*      */       case null:
/*      */       case null:
/*      */       case null:
/*      */       case null:
/*      */       case null:
/* 1303 */         return 12.0F;
/*      */       
/*      */       case PLANNED:
/* 1306 */         return 7.0F;
/*      */     } 
/*      */     
/* 1309 */     return 1.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getDamageModifier() {
/* 1316 */     return 100.0F / Math.max(1.0F, this.currentQL * (100.0F - this.damage) / 100.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean poll(long currTime, Structure struct) {
/* 1321 */     if (struct == null)
/* 1322 */       return true; 
/* 1323 */     if (currTime - struct.getCreationDate() <= (Servers.localServer.testServer ? 3600000L : 86400000L) * 2L) {
/* 1324 */       return false;
/*      */     }
/* 1326 */     if (isAPlan()) {
/* 1327 */       return false;
/*      */     }
/* 1329 */     HighwayPos highwaypos = MethodsHighways.getHighwayPos(this);
/* 1330 */     if (highwaypos != null && MethodsHighways.onHighway(highwaypos))
/*      */     {
/* 1332 */       return false;
/*      */     }
/* 1334 */     float mod = 1.0F;
/* 1335 */     Village v = getVillage();
/* 1336 */     if (v != null) {
/*      */       
/* 1338 */       if (v.moreThanMonthLeft())
/* 1339 */         return false; 
/* 1340 */       if (!v.lessThanWeekLeft()) {
/* 1341 */         mod = 10.0F;
/*      */       
/*      */       }
/*      */     }
/* 1345 */     else if (Zones.getKingdom(this.tilex, this.tiley) == 0) {
/* 1346 */       mod = 0.5F;
/*      */     } 
/* 1348 */     if ((float)(currTime - this.lastUsed) > (Servers.localServer.testServer ? (8.64E7F + 60000.0F * mod) : (6.048E8F + 8.64E7F * mod)) && 
/* 1349 */       !hasNoDecay()) {
/*      */       
/* 1351 */       setLastUsed(currTime);
/* 1352 */       if (setDamage(this.damage + getDamageModifier() * (0.1F + getModByMaterial() / 1000.0F)))
/*      */       {
/* 1354 */         return true;
/*      */       }
/*      */     } 
/* 1357 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getCurrentQualityLevel() {
/* 1363 */     return this.currentQL * Math.max(1.0F, 100.0F - this.damage) / 100.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getRepairItemTemplate() {
/* 1369 */     switch (this.material) {
/*      */       
/*      */       case null:
/* 1372 */         return 132;
/*      */       case null:
/* 1374 */         return 786;
/*      */       case null:
/* 1376 */         return 1123;
/*      */       case null:
/* 1378 */         return 1122;
/*      */       case null:
/* 1380 */         return 776;
/*      */       case null:
/* 1382 */         return 1121;
/*      */       case null:
/* 1384 */         return 132;
/*      */       case COMPLETED:
/* 1386 */         return 22;
/*      */       case PLANNED:
/* 1388 */         return 22;
/*      */     } 
/* 1390 */     return 22;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSoundByMaterial() {
/* 1401 */     switch (getMaterial()) {
/*      */       
/*      */       case null:
/*      */       case null:
/*      */       case null:
/*      */       case null:
/*      */       case null:
/*      */       case null:
/*      */       case null:
/* 1410 */         return "sound.work.masonry";
/*      */       
/*      */       case PLANNED:
/* 1413 */         return (Server.rand.nextInt(2) == 0) ? "sound.work.carpentry.mallet1" : "sound.work.carpentry.mallet2";
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1419 */     return (Server.rand.nextInt(2) == 0) ? "sound.work.carpentry.mallet1" : "sound.work.carpentry.mallet2";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getStartX() {
/* 1426 */     return getTileX();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getStartY() {
/* 1432 */     return getTileY();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMinX() {
/* 1438 */     return getTileX();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMinY() {
/* 1444 */     return getTileY();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supports() {
/* 1450 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getNumberOfExtensions() {
/* 1455 */     int extensions = 0;
/* 1456 */     if (this.type.isSupportType()) {
/*      */ 
/*      */       
/* 1459 */       int htOff = getHeightOffset() + Math.abs(getSlope());
/* 1460 */       int lowestCorner = (int)(Zones.getLowestCorner(getTileX(), getTileY(), 0) * 10.0F);
/*      */       
/* 1462 */       int extensionOffset = (int)getMaterial().getExtensionOffset() * 10;
/* 1463 */       int extensionTop = htOff - extensionOffset;
/*      */       int ht;
/* 1465 */       for (ht = extensionTop; ht > lowestCorner; ht -= 30)
/* 1466 */         extensions++; 
/*      */     } 
/* 1468 */     return extensions;
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
/* 1481 */     if (!supports()) {
/* 1482 */       return false;
/*      */     }
/* 1484 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean equals(StructureSupport support) {
/* 1490 */     return (support.getId() == getId());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getEndX() {
/* 1496 */     return getStartX() + 1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getEndY() {
/* 1502 */     return getStartY() + 1;
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
/*      */   public boolean isSupportedByGround() {
/* 1515 */     return true;
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
/* 1526 */     return "BridgePart [number=" + this.number + ", structureId=" + this.structureId + ", type=" + this.type + "]";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOnSurface() {
/* 1532 */     return (this.layer == 0);
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
/*      */   public boolean canBeAutoFilled() {
/* 1554 */     return false;
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
/* 1565 */     return false;
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
/* 1576 */     return false;
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
/* 1587 */     return false;
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
/* 1598 */     return false;
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
/*      */   public boolean canDisableDecay() {
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
/*      */   public boolean canDisableDestroy() {
/* 1632 */     return true;
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
/*      */   public boolean canDisableDrop() {
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
/*      */   public boolean canDisableEatAndDrink() {
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
/*      */   public boolean canDisableImprove() {
/* 1676 */     return true;
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
/*      */   public boolean canDisableLockpicking() {
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
/*      */   public boolean canDisableMoveable() {
/* 1709 */     return false;
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
/*      */   public final boolean canDisableOwnerTurning() {
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
/*      */   public boolean canDisablePainting() {
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
/*      */   public boolean canDisablePut() {
/* 1753 */     return false;
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
/* 1764 */     return true;
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
/*      */   public boolean canDisableSpellTarget() {
/* 1786 */     return false;
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
/* 1797 */     return false;
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
/* 1808 */     return false;
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
/* 1819 */     return false;
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
/* 1830 */     return false;
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
/* 1842 */     return null;
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
/* 1853 */     return this.damage;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/* 1864 */     return this.material.getName() + " " + this.type.getName();
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
/* 1875 */     return this.currentQL;
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
/* 1886 */     return this.permissions.hasPermission(Permissions.Allow.HAS_COURIER.getBit());
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
/* 1897 */     return this.permissions.hasPermission(Permissions.Allow.HAS_DARK_MESSENGER.getBit());
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
/* 1908 */     return this.permissions.hasPermission(Permissions.Allow.DECAY_DISABLED.getBit());
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
/* 1919 */     return this.permissions.hasPermission(Permissions.Allow.ALWAYS_LIT.getBit());
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
/* 1930 */     return this.permissions.hasPermission(Permissions.Allow.AUTO_FILL.getBit());
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
/* 1941 */     return this.permissions.hasPermission(Permissions.Allow.AUTO_LIGHT.getBit());
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
/* 1952 */     return this.permissions.hasPermission(Permissions.Allow.NO_BASH.getBit());
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
/* 1963 */     return this.permissions.hasPermission(Permissions.Allow.NO_DRAG.getBit());
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
/* 1974 */     return this.permissions.hasPermission(Permissions.Allow.NO_DROP.getBit());
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
/* 1985 */     return this.permissions.hasPermission(Permissions.Allow.NO_EAT_OR_DRINK.getBit());
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
/* 1996 */     return this.permissions.hasPermission(Permissions.Allow.NO_IMPROVE.getBit());
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
/* 2007 */     return this.permissions.hasPermission(Permissions.Allow.NOT_MOVEABLE.getBit());
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
/* 2018 */     return this.permissions.hasPermission(Permissions.Allow.NO_PUT.getBit());
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
/* 2029 */     return this.permissions.hasPermission(Permissions.Allow.NO_REPAIR.getBit());
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
/* 2040 */     return this.permissions.hasPermission(Permissions.Allow.NO_TAKE.getBit());
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
/* 2051 */     return this.permissions.hasPermission(Permissions.Allow.NOT_LOCKABLE.getBit());
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
/* 2062 */     return this.permissions.hasPermission(Permissions.Allow.NOT_LOCKPICKABLE.getBit());
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
/* 2073 */     return this.permissions.hasPermission(Permissions.Allow.NOT_PAINTABLE.getBit());
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
/* 2084 */     return true;
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
/* 2095 */     return this.permissions.hasPermission(Permissions.Allow.NO_SPELLS.getBit());
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
/* 2106 */     return this.permissions.hasPermission(Permissions.Allow.NOT_TURNABLE.getBit());
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
/* 2117 */     return this.permissions.hasPermission(Permissions.Allow.OWNER_MOVEABLE.getBit());
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
/* 2128 */     return this.permissions.hasPermission(Permissions.Allow.OWNER_TURNABLE.getBit());
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
/* 2139 */     return this.permissions.hasPermission(Permissions.Allow.PLANTED.getBit());
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
/* 2150 */     if (this.permissions.hasPermission(Permissions.Allow.SEALED_BY_PLAYER.getBit()))
/* 2151 */       return true; 
/* 2152 */     return false;
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
/* 2183 */     this.permissions.setPermissionBit(Permissions.Allow.HAS_COURIER.getBit(), aCourier);
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
/* 2195 */     this.permissions.setPermissionBit(Permissions.Allow.HAS_DARK_MESSENGER.getBit(), aDarkmessenger);
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
/* 2206 */     this.permissions.setPermissionBit(Permissions.Allow.DECAY_DISABLED.getBit(), aNoDecay);
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
/* 2218 */     this.permissions.setPermissionBit(Permissions.Allow.ALWAYS_LIT.getBit(), aAlwaysLit);
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
/* 2230 */     this.permissions.setPermissionBit(Permissions.Allow.AUTO_FILL.getBit(), aAutoFill);
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
/* 2242 */     this.permissions.setPermissionBit(Permissions.Allow.AUTO_LIGHT.getBit(), aAutoLight);
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
/* 2253 */     this.permissions.setPermissionBit(Permissions.Allow.NO_BASH.getBit(), aNoDestroy);
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
/* 2264 */     this.permissions.setPermissionBit(Permissions.Allow.NO_DRAG.getBit(), aNoDrag);
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
/* 2275 */     this.permissions.setPermissionBit(Permissions.Allow.NO_DROP.getBit(), aNoDrop);
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
/* 2286 */     this.permissions.setPermissionBit(Permissions.Allow.NO_EAT_OR_DRINK.getBit(), aNoEatOrDrink);
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
/* 2297 */     this.permissions.setPermissionBit(Permissions.Allow.NO_IMPROVE.getBit(), aNoImprove);
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
/* 2309 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_MOVEABLE.getBit(), aNoMove);
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
/* 2320 */     this.permissions.setPermissionBit(Permissions.Allow.NO_PUT.getBit(), aNoPut);
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
/* 2331 */     this.permissions.setPermissionBit(Permissions.Allow.NO_REPAIR.getBit(), aNoRepair);
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
/* 2343 */     this.permissions.setPermissionBit(Permissions.Allow.NO_TAKE.getBit(), aNoTake);
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
/* 2354 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_LOCKABLE.getBit(), aNoLock);
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
/* 2365 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_LOCKPICKABLE.getBit(), aNoLockpick);
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
/* 2376 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_PAINTABLE.getBit(), aNoPaint);
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
/* 2387 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_RUNEABLE.getBit(), aNoRune);
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
/* 2399 */     this.permissions.setPermissionBit(Permissions.Allow.NO_SPELLS.getBit(), aNoSpells);
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
/* 2410 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_TURNABLE.getBit(), aNoTurn);
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
/* 2421 */     this.permissions.setPermissionBit(Permissions.Allow.OWNER_MOVEABLE.getBit(), aOwnerMove);
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
/* 2432 */     this.permissions.setPermissionBit(Permissions.Allow.OWNER_TURNABLE.getBit(), aOwnerTurn);
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
/* 2444 */     this.permissions.setPermissionBit(Permissions.Allow.PLANTED.getBit(), aPlant);
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
/* 2456 */     this.permissions.setPermissionBit(Permissions.Allow.SEALED_BY_PLAYER.getBit(), aSealed);
/*      */   }
/*      */   
/*      */   public abstract boolean setQualityLevel(float paramFloat);
/*      */   
/*      */   public void setOriginalQualityLevel(float newQL) {}
/*      */   
/*      */   public abstract void savePermissions();
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\BridgePart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */