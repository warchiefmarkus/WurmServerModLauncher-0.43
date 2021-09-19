/*      */ package com.wurmonline.server.structures;
/*      */ 
/*      */ import com.wurmonline.math.TilePos;
/*      */ import com.wurmonline.math.Vector3f;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.players.Permissions;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import com.wurmonline.shared.constants.FenceConstants;
/*      */ import com.wurmonline.shared.constants.ItemMaterials;
/*      */ import com.wurmonline.shared.constants.StructureConstantsEnum;
/*      */ import com.wurmonline.shared.constants.StructureStateEnum;
/*      */ import com.wurmonline.shared.constants.WallConstants;
/*      */ import java.io.IOException;
/*      */ import java.util.concurrent.ConcurrentHashMap;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class Fence
/*      */   implements MiscConstants, FenceConstants, TimeConstants, Blocker, ItemMaterials, StructureSupport, Permissions.IAllow, CounterTypes
/*      */ {
/*      */   public static final byte DIR_HORIZ = 0;
/*      */   public static final byte DIR_DOWNRIGHT = 1;
/*      */   public static final byte DIR_DOWN = 2;
/*      */   public static final byte DIR_DOWNLEFT = 3;
/*   94 */   private static final long lowHedgeGrowthInterval = 3L * (Servers.localServer.testServer ? 60000L : 86400000L);
/*   95 */   private static final long mediumHedgeGrowthInterval = 10L * (Servers.localServer.testServer ? 60000L : 86400000L);
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean deityFence;
/*      */ 
/*      */   
/*  102 */   int number = -10;
/*      */   
/*  104 */   private static final Logger logger = Logger.getLogger(Fence.class.getName());
/*      */ 
/*      */   
/*      */   float originalQL;
/*      */ 
/*      */   
/*      */   float currentQL;
/*      */   
/*      */   float damage;
/*      */   
/*  114 */   StructureConstantsEnum type = StructureConstantsEnum.FENCE_PLAN_WOODEN;
/*      */   
/*      */   int tilex;
/*      */   
/*      */   int tiley;
/*      */   
/*      */   long lastUsed;
/*  121 */   StructureStateEnum state = StructureStateEnum.UNINITIALIZED;
/*      */ 
/*      */   
/*      */   byte dir;
/*      */ 
/*      */   
/*      */   int zoneId;
/*      */   
/*      */   private boolean surfaced = true;
/*      */   
/*  131 */   protected int color = -1;
/*      */ 
/*      */   
/*      */   public static final float FENCE_DAMAGE_STATE_DIVIDER = 60.0F;
/*      */   
/*  136 */   int heightOffset = 0;
/*  137 */   int layer = 0;
/*  138 */   private static final Vector3f normalHoriz = new Vector3f(0.0F, 1.0F, 0.0F);
/*  139 */   private static final Vector3f normalVertical = new Vector3f(1.0F, 0.0F, 0.0F);
/*      */   private final Vector3f centerPoint;
/*  141 */   private int floorLevel = 0;
/*  142 */   Permissions permissions = new Permissions();
/*      */   
/*  144 */   private static final ConcurrentHashMap<Long, Fence> rubbleFences = new ConcurrentHashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Fence(StructureConstantsEnum aType, int aTileX, int aTileY, int aHeightOffset, float aQualityLevel, Tiles.TileBorderDirection aDir, int aZoneId, int aLayer) {
/*  150 */     this.tilex = aTileX;
/*  151 */     this.tiley = aTileY;
/*  152 */     this.currentQL = aQualityLevel;
/*  153 */     this.originalQL = aQualityLevel;
/*  154 */     this.lastUsed = System.currentTimeMillis();
/*  155 */     this.type = aType;
/*  156 */     this.zoneId = aZoneId;
/*  157 */     this.dir = aDir.getCode();
/*  158 */     this.heightOffset = aHeightOffset;
/*  159 */     this.layer = aLayer;
/*      */     
/*      */     try {
/*  162 */       Zone zone = Zones.getZone(aZoneId);
/*  163 */       this.surfaced = zone.isOnSurface();
/*      */     }
/*  165 */     catch (NoSuchZoneException nsz) {
/*      */       
/*  167 */       logger.log(Level.WARNING, "ZoneId: " + aZoneId + ", fence: " + this.number + " - " + nsz.getMessage(), (Throwable)nsz);
/*      */     } 
/*  169 */     this.deityFence = (Servers.localServer.entryServer && this.originalQL > 99.0F);
/*  170 */     setFloorLevel();
/*  171 */     this.centerPoint = calculateCenterPoint();
/*  172 */     if (this.type == StructureConstantsEnum.FENCE_RUBBLE) {
/*  173 */       rubbleFences.put(Long.valueOf(getId()), this);
/*      */     }
/*      */   }
/*      */   
/*      */   private final Vector3f calculateCenterPoint() {
/*  178 */     return new Vector3f(isHorizontal() ? (this.tilex * 4 + 2) : (this.tilex * 4), isHorizontal() ? (this.tiley * 4) : (this.tiley * 4 + 2), getMinZ() + 1.5F);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Fence(int aNum, StructureConstantsEnum aType, StructureStateEnum aState, int aColor, int aTileX, int aTileY, int aHeightOffset, float aQualityLevel, float aOrigQl, long aLastUsed, Tiles.TileBorderDirection aDir, int aZoneId, boolean aSurface, float aDamage, int aLayer, int aSettings) {
/*  226 */     this.number = aNum;
/*  227 */     this.state = aState;
/*  228 */     this.tilex = aTileX;
/*  229 */     this.tiley = aTileY;
/*  230 */     this.currentQL = aQualityLevel;
/*  231 */     this.originalQL = aOrigQl;
/*  232 */     this.lastUsed = aLastUsed;
/*  233 */     this.type = aType;
/*  234 */     this.zoneId = aZoneId;
/*  235 */     this.dir = aDir.getCode();
/*  236 */     this.color = aColor;
/*  237 */     this.surfaced = aSurface;
/*  238 */     this.damage = aDamage;
/*  239 */     this.layer = aLayer;
/*  240 */     this.deityFence = (Servers.localServer.entryServer && this.originalQL > 99.0F);
/*  241 */     this.centerPoint = calculateCenterPoint();
/*  242 */     this.heightOffset = aHeightOffset;
/*  243 */     setSettings(aSettings);
/*  244 */     setFloorLevel();
/*  245 */     if (this.type == StructureConstantsEnum.FENCE_RUBBLE) {
/*  246 */       rubbleFences.put(Long.valueOf(getId()), this);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isFence() {
/*  255 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isTile() {
/*  261 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isWall() {
/*  270 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isFloor() {
/*  279 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isRoof() {
/*  285 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isStair() {
/*  291 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Vector3f getNormal() {
/*  297 */     if (isHorizontal())
/*  298 */       return normalHoriz; 
/*  299 */     return normalVertical;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Vector3f getCenterPoint() {
/*  305 */     return this.centerPoint;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Vector3f isBlocking(Creature creature, Vector3f startPos, Vector3f endPos, Vector3f normal, int blockType, long target, boolean followGround) {
/*  312 */     if (target == getId())
/*  313 */       return null; 
/*  314 */     if (!isFinished()) {
/*  315 */       return null;
/*      */     }
/*  317 */     if (isBlocking(blockType, creature))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  328 */       return getIntersectionPoint(startPos, endPos, normal, creature, blockType, followGround);
/*      */     }
/*  330 */     return null;
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
/*      */   public final Vector3f getIntersectionPoint(Vector3f startPos, Vector3f endPos, Vector3f normal, Creature c, int blockType, boolean followGround) {
/*  360 */     Vector3f spcopy = startPos.clone();
/*  361 */     Vector3f epcopy = endPos.clone();
/*  362 */     if (getFloorLevel() == 0)
/*      */     {
/*      */       
/*  365 */       if (followGround || spcopy.z <= getMinZ()) {
/*      */         
/*  367 */         spcopy.z = getMinZ() + 0.5F;
/*  368 */         if (followGround) {
/*  369 */           epcopy.z = getMinZ() + 0.5F;
/*      */         }
/*      */       } 
/*      */     }
/*  373 */     float u = getNormal().dot(getCenterPoint().subtract(spcopy)) / getNormal().dot(epcopy.subtract(spcopy));
/*  374 */     if (u >= 0.0F && u <= 1.0F) {
/*      */       
/*  376 */       Vector3f diff = getCenterPoint().subtract(spcopy);
/*  377 */       if (isHorizontal()) {
/*      */ 
/*      */         
/*  380 */         float steps = diff.y / normal.y;
/*  381 */         Vector3f intersection = spcopy.add(normal.mult(steps));
/*  382 */         if (isWithinBounds(intersection, c, followGround)) {
/*  383 */           return intersection;
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/*  393 */         float steps = diff.x / normal.x;
/*  394 */         Vector3f intersection = spcopy.add(normal.mult(steps));
/*  395 */         if (isWithinBounds(intersection, c, followGround)) {
/*  396 */           return intersection;
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  405 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean isWithinBounds(Vector3f pointToCheck, Creature c, boolean followGround) {
/*  410 */     if (isHorizontal()) {
/*      */       
/*  412 */       if (pointToCheck.getY() >= (this.tiley * 4) - 0.1F && pointToCheck
/*  413 */         .getY() <= (this.tiley * 4) + 0.1F)
/*      */       {
/*  415 */         if (pointToCheck.getX() >= (this.tilex * 4) && pointToCheck
/*  416 */           .getX() <= (this.tilex * 4 + 4))
/*      */         {
/*      */           
/*  419 */           if ((followGround && getFloorLevel() == 0) || (pointToCheck
/*  420 */             .getZ() >= getMinZ() && pointToCheck.getZ() <= getMaxZ())) {
/*  421 */             return true;
/*      */           
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  427 */     else if (pointToCheck.getX() >= (this.tilex * 4) - 0.1F && pointToCheck
/*  428 */       .getX() <= (this.tilex * 4) + 0.1F) {
/*      */ 
/*      */       
/*  431 */       if (pointToCheck.getY() >= (this.tiley * 4) && pointToCheck
/*  432 */         .getY() <= (this.tiley * 4 + 4))
/*      */       {
/*      */ 
/*      */         
/*  436 */         if ((followGround && getFloorLevel() == 0) || (pointToCheck
/*  437 */           .getZ() >= getMinZ() && pointToCheck.getZ() <= getMaxZ())) {
/*  438 */           return true;
/*      */         }
/*      */       }
/*      */     } 
/*  442 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isLowHedge() {
/*  447 */     switch (this.type) {
/*      */       
/*      */       case HEDGE_FLOWER1_LOW:
/*      */       case HEDGE_FLOWER2_LOW:
/*      */       case HEDGE_FLOWER3_LOW:
/*      */       case HEDGE_FLOWER4_LOW:
/*      */       case HEDGE_FLOWER5_LOW:
/*      */       case HEDGE_FLOWER6_LOW:
/*      */       case HEDGE_FLOWER7_LOW:
/*  456 */         return true;
/*      */     } 
/*  458 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWalkthrough() {
/*  463 */     switch (this.type) {
/*      */       
/*      */       case FENCE_CURB:
/*      */       case FENCE_WOVEN:
/*      */       case FENCE_ROPE_LOW:
/*      */       case FENCE_RUBBLE:
/*  469 */         return true;
/*      */     } 
/*      */     
/*  472 */     return isFlowerbed();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFlowerbed() {
/*  477 */     switch (this.type) {
/*      */       
/*      */       case FLOWERBED_BLUE:
/*      */       case FLOWERBED_GREENISH_YELLOW:
/*      */       case FLOWERBED_ORANGE_RED:
/*      */       case FLOWERBED_PURPLE:
/*      */       case FLOWERBED_WHITE:
/*      */       case FLOWERBED_WHITE_DOTTED:
/*      */       case FLOWERBED_YELLOW:
/*  486 */         return true;
/*      */     } 
/*  488 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isLowFence() {
/*  493 */     switch (this.type) {
/*      */       
/*      */       case FENCE_CURB:
/*      */       case FENCE_WOVEN:
/*      */       case FENCE_ROPE_LOW:
/*      */       case FENCE_RUBBLE:
/*      */       case FENCE_GARDESGARD_LOW:
/*      */       case FENCE_IRON:
/*      */       case FENCE_STONEWALL:
/*      */       case FENCE_WOODEN:
/*      */       case FENCE_STONE_PARAPET:
/*      */       case FENCE_WOODEN_PARAPET:
/*      */       case FENCE_STONE_IRON_PARAPET:
/*      */       case FENCE_ROPE_HIGH:
/*      */       case FENCE_PLAN_ROPE_HIGH:
/*      */       case FENCE_GARDESGARD_GATE:
/*      */       case FENCE_WOODEN_GATE:
/*      */       case FENCE_WOODEN_CRUDE_GATE:
/*      */       case FENCE_WOODEN_CRUDE:
/*      */       case FENCE_IRON_GATE:
/*      */       case FENCE_STONE:
/*      */       case FENCE_SANDSTONE_STONE_PARAPET:
/*      */       case FENCE_SLATE_STONE_PARAPET:
/*      */       case FENCE_ROUNDED_STONE_STONE_PARAPET:
/*      */       case FENCE_RENDERED_STONE_PARAPET:
/*      */       case FENCE_POTTERY_STONE_PARAPET:
/*      */       case FENCE_MARBLE_STONE_PARAPET:
/*      */       case FENCE_SLATE_CHAIN_FENCE:
/*      */       case FENCE_ROUNDED_STONE_CHAIN_FENCE:
/*      */       case FENCE_SANDSTONE_CHAIN_FENCE:
/*      */       case FENCE_RENDERED_CHAIN_FENCE:
/*      */       case FENCE_POTTERY_CHAIN_FENCE:
/*      */       case FENCE_MARBLE_CHAIN_FENCE:
/*  526 */         return true;
/*      */     } 
/*  528 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isMediumHedge() {
/*  533 */     switch (this.type) {
/*      */       
/*      */       case HEDGE_FLOWER1_MEDIUM:
/*      */       case HEDGE_FLOWER2_MEDIUM:
/*      */       case HEDGE_FLOWER3_MEDIUM:
/*      */       case HEDGE_FLOWER4_MEDIUM:
/*      */       case HEDGE_FLOWER5_MEDIUM:
/*      */       case HEDGE_FLOWER6_MEDIUM:
/*      */       case HEDGE_FLOWER7_MEDIUM:
/*  542 */         return true;
/*      */     } 
/*  544 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isHighHedge() {
/*  549 */     switch (this.type) {
/*      */       
/*      */       case HEDGE_FLOWER1_HIGH:
/*      */       case HEDGE_FLOWER2_HIGH:
/*      */       case HEDGE_FLOWER3_HIGH:
/*      */       case HEDGE_FLOWER4_HIGH:
/*      */       case HEDGE_FLOWER5_HIGH:
/*      */       case HEDGE_FLOWER6_HIGH:
/*      */       case HEDGE_FLOWER7_HIGH:
/*  558 */         return true;
/*      */     } 
/*  560 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isHedge() {
/*  565 */     return isHedge(this.type);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isHedge(StructureConstantsEnum fenceType) {
/*  570 */     return (fenceType.value >= StructureConstantsEnum.HEDGE_FLOWER1_LOW.value && fenceType.value <= StructureConstantsEnum.HEDGE_FLOWER7_HIGH.value);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isMagic() {
/*  576 */     switch (this.type) {
/*      */       
/*      */       case FENCE_MAGIC_STONE:
/*      */       case FENCE_MAGIC_ICE:
/*      */       case FENCE_MAGIC_FIRE:
/*  581 */         return true;
/*      */     } 
/*  583 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getColor() {
/*  593 */     return this.color;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Tiles.TileBorderDirection getDir() {
/*  602 */     if (this.dir == 0) {
/*  603 */       return Tiles.TileBorderDirection.DIR_HORIZ;
/*      */     }
/*  605 */     return Tiles.TileBorderDirection.DIR_DOWN;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getDirAsByte() {
/*  614 */     return this.dir;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getZoneId() {
/*  623 */     return this.zoneId;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getTileX() {
/*  629 */     return this.tilex;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Village getVillage() {
/*  634 */     VolaTile t = getTile();
/*  635 */     if (t != null)
/*      */     {
/*  637 */       if (t.getVillage() != null)
/*  638 */         return t.getVillage(); 
/*      */     }
/*  640 */     t = getOtherTile();
/*  641 */     if (t != null)
/*      */     {
/*  643 */       if (t.getVillage() != null)
/*  644 */         return t.getVillage(); 
/*      */     }
/*  646 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void pollMagicFences(long currTime) {
/*  651 */     if (isMagic())
/*      */     {
/*  653 */       if (currTime - this.lastUsed > 1000L) {
/*      */         
/*  655 */         if (this.type == StructureConstantsEnum.FENCE_MAGIC_ICE) {
/*      */           
/*  657 */           if (Server.rand.nextInt(20) == 0) {
/*      */             
/*  659 */             VolaTile t = getTile();
/*  660 */             if (t != null) {
/*      */               
/*  662 */               Creature[] crets = t.getCreatures();
/*  663 */               for (Creature defender : crets) {
/*      */                 
/*  665 */                 if (!defender.isUnique()) {
/*      */                   
/*  667 */                   float dam = Math.max(3000.0F, this.damage * 100.0F);
/*  668 */                   defender.addWoundOfType(null, (byte)8, 0, true, 1.0F, true, dam, 0.0F, 0.0F, false, true);
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */             
/*  673 */             t = getOtherTile();
/*  674 */             if (t != null) {
/*      */               
/*  676 */               Creature[] crets = t.getCreatures();
/*  677 */               for (Creature defender : crets)
/*      */               {
/*  679 */                 if (!defender.isUnique())
/*      */                 {
/*  681 */                   float dam = Math.max(3000.0F, this.damage * 100.0F);
/*  682 */                   defender.addWoundOfType(null, (byte)8, 0, true, 1.0F, true, dam, 0.0F, 0.0F, false, true);
/*      */                 }
/*      */               
/*      */               }
/*      */             
/*      */             } 
/*      */           } 
/*  689 */         } else if (this.type == StructureConstantsEnum.FENCE_MAGIC_FIRE) {
/*      */           
/*  691 */           if (Server.rand.nextInt(20) == 0) {
/*      */             
/*  693 */             VolaTile t = getTile();
/*  694 */             if (t != null) {
/*      */               
/*  696 */               Creature[] crets = t.getCreatures();
/*  697 */               for (Creature defender : crets) {
/*      */                 
/*  699 */                 if (!defender.isUnique()) {
/*      */                   
/*  701 */                   float dam = Math.max(3000.0F, this.damage * 100.0F);
/*  702 */                   defender.addWoundOfType(null, (byte)4, 0, true, 1.0F, true, dam, 0.0F, 0.0F, false, true);
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */             
/*  707 */             t = getOtherTile();
/*  708 */             if (t != null) {
/*      */               
/*  710 */               Creature[] crets = t.getCreatures();
/*  711 */               for (Creature defender : crets) {
/*      */                 
/*  713 */                 if (!defender.isUnique()) {
/*      */                   
/*  715 */                   float dam = Math.max(3000.0F, this.damage * 100.0F);
/*  716 */                   defender.addWoundOfType(null, (byte)4, 0, true, 1.0F, true, dam, 0.0F, 0.0F, false, true);
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         
/*  723 */         setLastUsed(currTime);
/*  724 */         if (!setDamage(getDamage() + 2.0F * (100.0F - getQualityLevel()) / 100.0F)) {
/*      */           
/*  726 */           VolaTile tile = Zones.getTileOrNull(getTileX(), getTileY(), this.surfaced);
/*  727 */           if (tile != null)
/*      */           {
/*  729 */             tile.updateMagicalFence(this);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final void poll(long currTime) {
/*  738 */     float mod = 1.0F;
/*  739 */     if (isHedge()) {
/*      */       
/*  741 */       if (getDamage() > 0.0F)
/*  742 */         setDamage(getDamage() - getQualityLevel() / 2.0F); 
/*  743 */       if (currTime - this.lastUsed > 86400000L)
/*      */       {
/*      */         
/*  746 */         if (isLowHedge() && this.type != StructureConstantsEnum.HEDGE_FLOWER1_LOW) {
/*      */           
/*  748 */           if (currTime - this.lastUsed > lowHedgeGrowthInterval)
/*      */           {
/*  750 */             if (Server.rand.nextInt(10) < 7) {
/*      */               
/*  752 */               setDamage(0.0F);
/*  753 */               setType(StructureConstantsEnum.getEnumByValue((short)(this.type.value + 1)));
/*      */               
/*      */               try {
/*  756 */                 save();
/*  757 */                 VolaTile tile = Zones.getTileOrNull(getTileX(), getTileY(), this.surfaced);
/*  758 */                 if (tile != null)
/*      */                 {
/*  760 */                   tile.updateFence(this);
/*      */                 }
/*      */               }
/*  763 */               catch (IOException iox) {
/*      */                 
/*  765 */                 logger.log(Level.WARNING, "Fence: " + this.number + " - " + iox.getMessage(), iox);
/*      */               }
/*      */             
/*      */             } 
/*      */           }
/*  770 */         } else if (isMediumHedge() && this.type != StructureConstantsEnum.HEDGE_FLOWER3_MEDIUM) {
/*      */           
/*  772 */           if (currTime - this.lastUsed > mediumHedgeGrowthInterval)
/*      */           {
/*  774 */             if (Server.rand.nextInt(10) < 7) {
/*      */               
/*  776 */               setType(StructureConstantsEnum.getEnumByValue((short)(this.type.value + 1)));
/*      */               
/*      */               try {
/*  779 */                 save();
/*  780 */                 VolaTile tile = Zones.getTileOrNull(getTileX(), getTileY(), this.surfaced);
/*  781 */                 if (tile != null)
/*      */                 {
/*  783 */                   tile.updateFence(this);
/*      */                 }
/*      */               }
/*  786 */               catch (IOException iox) {
/*      */                 
/*  788 */                 logger.log(Level.WARNING, "Fence: " + this.number + " - " + iox.getMessage(), iox);
/*      */               } 
/*      */             } 
/*      */           }
/*      */         } 
/*      */       }
/*      */       
/*      */       return;
/*      */     } 
/*  797 */     if (getType() == StructureConstantsEnum.FENCE_RUBBLE) {
/*      */       
/*  799 */       setDamage(this.damage + 4.0F);
/*      */       return;
/*      */     } 
/*  802 */     Village v = getVillage();
/*  803 */     if (v != null) {
/*      */       
/*  805 */       if (v.moreThanMonthLeft())
/*      */         return; 
/*  807 */       if (!v.lessThanWeekLeft()) {
/*  808 */         mod = isFlowerbed() ? 2.0F : 10.0F;
/*      */       
/*      */       }
/*      */     }
/*  812 */     else if (Zones.getKingdom(this.tilex, this.tiley) == 0) {
/*  813 */       mod = 0.5F;
/*      */     } 
/*  815 */     if ((float)(currTime - this.lastUsed) > 8.64E7F * mod) {
/*      */       
/*  817 */       setLastUsed(currTime);
/*  818 */       if (!this.deityFence && !hasNoDecay()) {
/*  819 */         setDamage(this.damage + 0.1F * getDamageModifier());
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public final boolean isOnPvPServer() {
/*  825 */     if (isHorizontal()) {
/*      */       
/*  827 */       if (Zones.isOnPvPServer(this.tilex, this.tiley))
/*  828 */         return true; 
/*  829 */       if (Zones.isOnPvPServer(this.tilex, this.tiley - 1)) {
/*  830 */         return true;
/*      */       }
/*      */     } else {
/*      */       
/*  834 */       if (Zones.isOnPvPServer(this.tilex, this.tiley))
/*  835 */         return true; 
/*  836 */       if (Zones.isOnPvPServer(this.tilex - 1, this.tiley))
/*  837 */         return true; 
/*      */     } 
/*  839 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getNumber() {
/*  848 */     return this.number;
/*      */   }
/*      */ 
/*      */   
/*      */   public final float getRepairedDamage() {
/*  853 */     if (this.type == StructureConstantsEnum.FENCE_WOODEN_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_WOODEN_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_WOODEN || this.type == StructureConstantsEnum.FENCE_WOODEN || this.type == StructureConstantsEnum.FENCE_WOODEN_CRUDE_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_WOODEN_GATE_CRUDE)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  859 */       return 40.0F; } 
/*  860 */     return 10.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getTileY() {
/*  866 */     return this.tiley;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getDamageModifier() {
/*  872 */     if (isFlowerbed()) {
/*      */       
/*  874 */       float mod = 5.0F * this.damage / 100.0F;
/*  875 */       return 100.0F / Math.max(1.0F, this.currentQL * (100.0F - this.damage) / 100.0F) + mod;
/*      */     } 
/*      */     
/*  878 */     return 100.0F / Math.max(1.0F, this.currentQL * (100.0F - this.damage) / 100.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isHorizontal() {
/*  884 */     return (this.dir == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getOriginalQualityLevel() {
/*  889 */     return this.originalQL;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final VolaTile getTile() {
/*      */     try {
/*  896 */       Zone zone = Zones.getZone(this.tilex, this.tiley, this.surfaced);
/*  897 */       VolaTile toReturn = zone.getTileOrNull(this.tilex, this.tiley);
/*  898 */       if (toReturn != null) {
/*      */         
/*  900 */         if (toReturn.isTransition())
/*  901 */           return Zones.getZone(this.tilex, this.tiley, false).getOrCreateTile(this.tilex, this.tiley); 
/*  902 */         return toReturn;
/*      */       } 
/*      */       
/*  905 */       logger.log(Level.WARNING, "Tile not in zone, this shouldn't happen " + this.tilex + ", " + this.tiley + ", fence: " + this.number);
/*      */     
/*      */     }
/*  908 */     catch (NoSuchZoneException nsz) {
/*      */       
/*  910 */       logger.log(Level.WARNING, "This shouldn't happen " + this.tilex + ", " + this.tiley + ", fence: " + this.number + " - " + nsz
/*  911 */           .getMessage(), (Throwable)nsz);
/*      */     } 
/*  913 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private VolaTile getOtherTile() {
/*  918 */     if (isHorizontal()) {
/*      */       
/*  920 */       VolaTile volaTile = Zones.getOrCreateTile(this.tilex, this.tiley - 1, this.surfaced);
/*  921 */       return volaTile;
/*      */     } 
/*      */ 
/*      */     
/*  925 */     VolaTile toReturn = Zones.getOrCreateTile(this.tilex - 1, this.tiley, this.surfaced);
/*  926 */     return toReturn;
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
/*      */   public final boolean equals(StructureSupport support) {
/*  944 */     if (this == support)
/*  945 */       return true; 
/*  946 */     if (support == null)
/*  947 */       return false; 
/*  948 */     return (support.getId() == getId());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean equals(Object other) {
/*  954 */     if (this == other)
/*  955 */       return true; 
/*  956 */     if (other == null)
/*  957 */       return false; 
/*  958 */     if (getClass() != other.getClass())
/*  959 */       return false; 
/*  960 */     Fence support = (Fence)other;
/*  961 */     return (support.getId() == getId());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  971 */     int prime = 31;
/*  972 */     int result = 1;
/*  973 */     result = 31 * result + (int)getId();
/*  974 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getId() {
/*  983 */     return Tiles.getBorderObjectId(getTileX(), getTileY(), getHeightOffset(), getLayer(), this.dir, (byte)7);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTemporary() {
/*  989 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getOldId() {
/*  997 */     return (this.dir << 48L) + (this.tilex << 32L) + (this.tiley << 16) + 7L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setType(StructureConstantsEnum aType) {
/* 1008 */     this.type = aType;
/* 1009 */     this.lastUsed = System.currentTimeMillis();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final StructureConstantsEnum getType() {
/* 1020 */     return this.type;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final StructureStateEnum getState() {
/* 1029 */     return this.state;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isFinished() {
/* 1034 */     return (this.state.state >= (getFinishState()).state);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getPositionX() {
/* 1040 */     if (!isHorizontal()) {
/* 1041 */       return (this.tilex * 4);
/*      */     }
/* 1043 */     return (this.tilex * 4 + (this.tilex + 1) * 4) / 2.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getPositionY() {
/* 1049 */     if (isHorizontal()) {
/* 1050 */       return (this.tiley * 4);
/*      */     }
/* 1052 */     return (this.tiley * 4 + (this.tiley + 1) * 4) / 2.0F;
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
/*      */   public final boolean isBlocking(int blockType, Creature creature) {
/* 1064 */     if (blockType == 5 && (isLowHedge() || isLowFence()))
/*      */     {
/* 1066 */       return false;
/*      */     }
/* 1068 */     if (blockType == 4 && (isWalkthrough() || isLowHedge()))
/*      */     {
/* 1070 */       return false;
/*      */     }
/*      */     
/* 1073 */     if (blockType == 6 && 
/* 1074 */       !WallConstants.isBlocking(getType()))
/*      */     {
/* 1076 */       return false;
/*      */     }
/* 1078 */     if (blockType == 6 || blockType == 8)
/*      */     {
/* 1080 */       if (isFinished()) {
/*      */         
/* 1082 */         if (isDoor()) {
/*      */           
/* 1084 */           FenceGate gate = FenceGate.getFenceGate(getId());
/* 1085 */           if (gate != null)
/*      */           {
/* 1087 */             if (gate.canBeOpenedBy(creature, true)) {
/* 1088 */               return false;
/*      */             }
/*      */           }
/*      */         } 
/*      */       } else {
/* 1093 */         return false;
/*      */       }  } 
/* 1095 */     return isFinished();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean canBeOpenedBy(Creature creature, boolean wentThroughDoor) {
/* 1101 */     if (isWalkthrough())
/* 1102 */       return true; 
/* 1103 */     if (isFinished()) {
/*      */       
/* 1105 */       if (isDoor()) {
/*      */         
/* 1107 */         FenceGate gate = FenceGate.getFenceGate(getId());
/* 1108 */         if (gate != null)
/*      */         {
/* 1110 */           if (gate.canBeOpenedBy(creature, true))
/* 1111 */             return true; 
/*      */         }
/*      */       } 
/* 1114 */       return false;
/*      */     } 
/*      */     
/* 1117 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final StructureStateEnum getFinishState(StructureConstantsEnum fenceType) {
/* 1122 */     switch (fenceType) {
/*      */       
/*      */       case FENCE_PLAN_PALISADE:
/*      */       case FENCE_PLAN_PALISADE_GATE:
/*      */       case FENCE_PALISADE:
/*      */       case FENCE_PALISADE_GATE:
/* 1128 */         return StructureStateEnum.STATE_10_NEEDED;
/*      */       case FENCE_PLAN_STONEWALL_HIGH:
/*      */       case FENCE_STONEWALL_HIGH:
/*      */       case FENCE_PLAN_PORTCULLIS:
/*      */       case FENCE_SLATE_TALL_STONE_WALL:
/*      */       case FENCE_ROUNDED_STONE_TALL_STONE_WALL:
/*      */       case FENCE_SANDSTONE_TALL_STONE_WALL:
/*      */       case FENCE_RENDERED_TALL_STONE_WALL:
/*      */       case FENCE_POTTERY_TALL_STONE_WALL:
/*      */       case FENCE_MARBLE_TALL_STONE_WALL:
/*      */       case FENCE_SLATE_PORTCULLIS:
/*      */       case FENCE_ROUNDED_STONE_PORTCULLIS:
/*      */       case FENCE_SANDSTONE_PORTCULLIS:
/*      */       case FENCE_RENDERED_PORTCULLIS:
/*      */       case FENCE_POTTERY_PORTCULLIS:
/*      */       case FENCE_MARBLE_PORTCULLIS:
/*      */       case FENCE_PLAN_SLATE_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_ROUNDED_STONE_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_SANDSTONE_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_RENDERED_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_POTTERY_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_MARBLE_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_SLATE_PORTCULLIS:
/*      */       case FENCE_PLAN_ROUNDED_STONE_PORTCULLIS:
/*      */       case FENCE_PLAN_SANDSTONE_PORTCULLIS:
/*      */       case FENCE_PLAN_RENDERED_PORTCULLIS:
/*      */       case FENCE_PLAN_POTTERY_PORTCULLIS:
/*      */       case FENCE_PLAN_MARBLE_PORTCULLIS:
/* 1156 */         return StructureStateEnum.STATE_20_NEEDED;
/*      */       case FENCE_STONEWALL:
/*      */       case FENCE_STONE:
/*      */       case FENCE_PLAN_STONEWALL:
/*      */       case FENCE_PLAN_STONE:
/*      */       case FENCE_PLAN_SLATE:
/*      */       case FENCE_SLATE:
/*      */       case FENCE_PLAN_ROUNDED_STONE:
/*      */       case FENCE_ROUNDED_STONE:
/*      */       case FENCE_PLAN_POTTERY:
/*      */       case FENCE_POTTERY:
/*      */       case FENCE_PLAN_SANDSTONE:
/*      */       case FENCE_SANDSTONE:
/*      */       case FENCE_PLAN_MARBLE:
/*      */       case FENCE_MARBLE:
/* 1171 */         return StructureStateEnum.STATE_10_NEEDED;
/*      */       case FENCE_WOODEN_PARAPET:
/*      */       case FENCE_PLAN_WOODEN_PARAPET:
/* 1174 */         return StructureStateEnum.STATE_15_NEEDED;
/*      */       case FENCE_STONE_PARAPET:
/*      */       case FENCE_STONE_IRON_PARAPET:
/*      */       case FENCE_SANDSTONE_STONE_PARAPET:
/*      */       case FENCE_SLATE_STONE_PARAPET:
/*      */       case FENCE_ROUNDED_STONE_STONE_PARAPET:
/*      */       case FENCE_RENDERED_STONE_PARAPET:
/*      */       case FENCE_POTTERY_STONE_PARAPET:
/*      */       case FENCE_MARBLE_STONE_PARAPET:
/*      */       case FENCE_PLAN_STONE_PARAPET:
/*      */       case FENCE_PLAN_STONE_IRON_PARAPET:
/*      */       case FENCE_PLAN_SLATE_STONE_PARAPET:
/*      */       case FENCE_PLAN_ROUNDED_STONE_STONE_PARAPET:
/*      */       case FENCE_PLAN_RENDERED_STONE_PARAPET:
/*      */       case FENCE_PLAN_POTTERY_STONE_PARAPET:
/*      */       case FENCE_PLAN_MARBLE_STONE_PARAPET:
/* 1190 */         return StructureStateEnum.STATE_15_NEEDED;
/*      */       case FENCE_SLATE_CHAIN_FENCE:
/*      */       case FENCE_ROUNDED_STONE_CHAIN_FENCE:
/*      */       case FENCE_SANDSTONE_CHAIN_FENCE:
/*      */       case FENCE_RENDERED_CHAIN_FENCE:
/*      */       case FENCE_POTTERY_CHAIN_FENCE:
/*      */       case FENCE_MARBLE_CHAIN_FENCE:
/*      */       case FENCE_PLAN_MEDIUM_CHAIN:
/*      */       case FENCE_PLAN_SLATE_CHAIN_FENCE:
/*      */       case FENCE_PLAN_ROUNDED_STONE_CHAIN_FENCE:
/*      */       case FENCE_PLAN_SANDSTONE_CHAIN_FENCE:
/*      */       case FENCE_PLAN_RENDERED_CHAIN_FENCE:
/*      */       case FENCE_PLAN_POTTERY_CHAIN_FENCE:
/*      */       case FENCE_PLAN_MARBLE_CHAIN_FENCE:
/* 1204 */         return StructureStateEnum.STATE_16_NEEDED;
/*      */       case FENCE_CURB:
/*      */       case FENCE_PLAN_CURB:
/* 1207 */         return StructureStateEnum.STATE_6_NEEDED;
/*      */       case FENCE_PLAN_ROPE_LOW:
/* 1209 */         return StructureStateEnum.STATE_4_NEEDED;
/*      */       case FENCE_PLAN_ROPE_HIGH:
/* 1211 */         return StructureStateEnum.STATE_6_NEEDED;
/*      */       case FENCE_PLAN_IRON_HIGH:
/*      */       case FENCE_PLAN_IRON_GATE_HIGH:
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE:
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE:
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE:
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE:
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE:
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_SLATE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_RENDERED_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_POTTERY_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_MARBLE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_SLATE_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_RENDERED_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_POTTERY_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_MARBLE_HIGH_IRON_FENCE:
/* 1238 */         return StructureStateEnum.STATE_18_NEEDED;
/*      */       case FENCE_PLAN_SANDSTONE_STONE_PARAPET:
/* 1240 */         return StructureStateEnum.STATE_15_NEEDED;
/*      */     } 
/*      */ 
/*      */     
/* 1244 */     if (isIron(fenceType))
/* 1245 */       return StructureStateEnum.STATE_11_NEEDED; 
/* 1246 */     if (isWoven(fenceType)) {
/* 1247 */       return StructureStateEnum.STATE_10_NEEDED;
/*      */     }
/* 1249 */     return StructureStateEnum.STATE_4_NEEDED;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final StructureStateEnum getFinishState() {
/* 1256 */     return getFinishState(this.type);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isPalisadeGate() {
/* 1261 */     return (this.type == StructureConstantsEnum.FENCE_PLAN_PALISADE_GATE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setState(StructureStateEnum newState) {
/* 1270 */     if (this.state.state >= (getFinishState()).state && 
/* 1271 */       newState != StructureStateEnum.INITIALIZED)
/*      */       return; 
/* 1273 */     this.state = newState;
/* 1274 */     if (this.state.state >= (getFinishState()).state)
/* 1275 */       this.state = StructureStateEnum.FINISHED; 
/* 1276 */     if (this.state.state < 0) {
/*      */       
/* 1278 */       this.state = StructureStateEnum.FINISHED;
/* 1279 */       logger.log(Level.WARNING, "Finish state set to " + newState + " at ", new Exception());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isSlate() {
/* 1285 */     return (this.type == StructureConstantsEnum.FENCE_SLATE || this.type == StructureConstantsEnum.FENCE_SLATE_IRON || this.type == StructureConstantsEnum.FENCE_SLATE_IRON_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_SLATE || this.type == StructureConstantsEnum.FENCE_PLAN_SLATE_IRON || this.type == StructureConstantsEnum.FENCE_PLAN_SLATE_IRON_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_SLATE_TALL_STONE_WALL || this.type == StructureConstantsEnum.FENCE_PLAN_SLATE_PORTCULLIS || this.type == StructureConstantsEnum.FENCE_PLAN_SLATE_HIGH_IRON_FENCE || this.type == StructureConstantsEnum.FENCE_PLAN_SLATE_HIGH_IRON_FENCE_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_SLATE_STONE_PARAPET || this.type == StructureConstantsEnum.FENCE_PLAN_SLATE_CHAIN_FENCE || this.type == StructureConstantsEnum.FENCE_SLATE_TALL_STONE_WALL || this.type == StructureConstantsEnum.FENCE_SLATE_PORTCULLIS || this.type == StructureConstantsEnum.FENCE_SLATE_HIGH_IRON_FENCE || this.type == StructureConstantsEnum.FENCE_SLATE_HIGH_IRON_FENCE_GATE || this.type == StructureConstantsEnum.FENCE_SLATE_STONE_PARAPET || this.type == StructureConstantsEnum.FENCE_SLATE_CHAIN_FENCE);
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
/*      */   public final boolean isRoundedStone() {
/* 1307 */     return (this.type == StructureConstantsEnum.FENCE_ROUNDED_STONE || this.type == StructureConstantsEnum.FENCE_ROUNDED_STONE_IRON || this.type == StructureConstantsEnum.FENCE_ROUNDED_STONE_IRON_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE || this.type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_IRON || this.type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_IRON_GATE || this.type == StructureConstantsEnum.FENCE_ROUNDED_STONE_TALL_STONE_WALL || this.type == StructureConstantsEnum.FENCE_ROUNDED_STONE_PORTCULLIS || this.type == StructureConstantsEnum.FENCE_ROUNDED_STONE_HIGH_IRON_FENCE || this.type == StructureConstantsEnum.FENCE_ROUNDED_STONE_HIGH_IRON_FENCE_GATE || this.type == StructureConstantsEnum.FENCE_ROUNDED_STONE_STONE_PARAPET || this.type == StructureConstantsEnum.FENCE_ROUNDED_STONE_CHAIN_FENCE || this.type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_TALL_STONE_WALL || this.type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_PORTCULLIS || this.type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE || this.type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_STONE_PARAPET || this.type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_CHAIN_FENCE);
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
/*      */   public final boolean isPottery() {
/* 1329 */     return (this.type == StructureConstantsEnum.FENCE_POTTERY || this.type == StructureConstantsEnum.FENCE_POTTERY_IRON || this.type == StructureConstantsEnum.FENCE_POTTERY_IRON_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_POTTERY || this.type == StructureConstantsEnum.FENCE_PLAN_POTTERY_IRON || this.type == StructureConstantsEnum.FENCE_PLAN_POTTERY_IRON_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_POTTERY_TALL_STONE_WALL || this.type == StructureConstantsEnum.FENCE_PLAN_POTTERY_PORTCULLIS || this.type == StructureConstantsEnum.FENCE_PLAN_POTTERY_HIGH_IRON_FENCE || this.type == StructureConstantsEnum.FENCE_PLAN_POTTERY_HIGH_IRON_FENCE_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_POTTERY_STONE_PARAPET || this.type == StructureConstantsEnum.FENCE_PLAN_POTTERY_CHAIN_FENCE || this.type == StructureConstantsEnum.FENCE_POTTERY_TALL_STONE_WALL || this.type == StructureConstantsEnum.FENCE_POTTERY_PORTCULLIS || this.type == StructureConstantsEnum.FENCE_POTTERY_HIGH_IRON_FENCE || this.type == StructureConstantsEnum.FENCE_POTTERY_HIGH_IRON_FENCE_GATE || this.type == StructureConstantsEnum.FENCE_POTTERY_STONE_PARAPET || this.type == StructureConstantsEnum.FENCE_POTTERY_CHAIN_FENCE);
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
/*      */   public final boolean isSandstone() {
/* 1351 */     return (this.type == StructureConstantsEnum.FENCE_SANDSTONE || this.type == StructureConstantsEnum.FENCE_SANDSTONE_IRON || this.type == StructureConstantsEnum.FENCE_SANDSTONE_IRON_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE || this.type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_IRON || this.type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_IRON_GATE || this.type == StructureConstantsEnum.FENCE_SANDSTONE_TALL_STONE_WALL || this.type == StructureConstantsEnum.FENCE_SANDSTONE_PORTCULLIS || this.type == StructureConstantsEnum.FENCE_SANDSTONE_HIGH_IRON_FENCE || this.type == StructureConstantsEnum.FENCE_SANDSTONE_HIGH_IRON_FENCE_GATE || this.type == StructureConstantsEnum.FENCE_SANDSTONE_STONE_PARAPET || this.type == StructureConstantsEnum.FENCE_SANDSTONE_CHAIN_FENCE || this.type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_TALL_STONE_WALL || this.type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_PORTCULLIS || this.type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE || this.type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_STONE_PARAPET || this.type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_CHAIN_FENCE);
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
/*      */   public final boolean isPlasteredFence() {
/* 1373 */     return (this.type == StructureConstantsEnum.FENCE_RENDERED || this.type == StructureConstantsEnum.FENCE_RENDERED_IRON || this.type == StructureConstantsEnum.FENCE_RENDERED_IRON_GATE || this.type == StructureConstantsEnum.FENCE_RENDERED_TALL_STONE_WALL || this.type == StructureConstantsEnum.FENCE_RENDERED_PORTCULLIS || this.type == StructureConstantsEnum.FENCE_RENDERED_HIGH_IRON_FENCE || this.type == StructureConstantsEnum.FENCE_RENDERED_HIGH_IRON_FENCE_GATE || this.type == StructureConstantsEnum.FENCE_RENDERED_STONE_PARAPET || this.type == StructureConstantsEnum.FENCE_RENDERED_CHAIN_FENCE);
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
/*      */   public final boolean isPlastered() {
/* 1386 */     return (this.type == StructureConstantsEnum.FENCE_RENDERED || this.type == StructureConstantsEnum.FENCE_RENDERED_IRON || this.type == StructureConstantsEnum.FENCE_RENDERED_IRON_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_RENDERED || this.type == StructureConstantsEnum.FENCE_PLAN_RENDERED_IRON || this.type == StructureConstantsEnum.FENCE_PLAN_RENDERED_IRON_GATE || this.type == StructureConstantsEnum.FENCE_RENDERED_TALL_STONE_WALL || this.type == StructureConstantsEnum.FENCE_RENDERED_PORTCULLIS || this.type == StructureConstantsEnum.FENCE_RENDERED_HIGH_IRON_FENCE || this.type == StructureConstantsEnum.FENCE_RENDERED_HIGH_IRON_FENCE_GATE || this.type == StructureConstantsEnum.FENCE_RENDERED_STONE_PARAPET || this.type == StructureConstantsEnum.FENCE_RENDERED_CHAIN_FENCE || this.type == StructureConstantsEnum.FENCE_PLAN_RENDERED_TALL_STONE_WALL || this.type == StructureConstantsEnum.FENCE_PLAN_RENDERED_PORTCULLIS || this.type == StructureConstantsEnum.FENCE_PLAN_RENDERED_HIGH_IRON_FENCE || this.type == StructureConstantsEnum.FENCE_PLAN_RENDERED_HIGH_IRON_FENCE_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_RENDERED_STONE_PARAPET || this.type == StructureConstantsEnum.FENCE_PLAN_RENDERED_CHAIN_FENCE);
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
/*      */   public final boolean isMarble() {
/* 1408 */     return (this.type == StructureConstantsEnum.FENCE_MARBLE || this.type == StructureConstantsEnum.FENCE_MARBLE_IRON || this.type == StructureConstantsEnum.FENCE_MARBLE_IRON_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_MARBLE || this.type == StructureConstantsEnum.FENCE_PLAN_MARBLE_IRON || this.type == StructureConstantsEnum.FENCE_PLAN_MARBLE_IRON_GATE || this.type == StructureConstantsEnum.FENCE_MARBLE_TALL_STONE_WALL || this.type == StructureConstantsEnum.FENCE_MARBLE_PORTCULLIS || this.type == StructureConstantsEnum.FENCE_MARBLE_HIGH_IRON_FENCE || this.type == StructureConstantsEnum.FENCE_MARBLE_HIGH_IRON_FENCE_GATE || this.type == StructureConstantsEnum.FENCE_MARBLE_STONE_PARAPET || this.type == StructureConstantsEnum.FENCE_MARBLE_CHAIN_FENCE || this.type == StructureConstantsEnum.FENCE_PLAN_MARBLE_TALL_STONE_WALL || this.type == StructureConstantsEnum.FENCE_PLAN_MARBLE_PORTCULLIS || this.type == StructureConstantsEnum.FENCE_PLAN_MARBLE_HIGH_IRON_FENCE || this.type == StructureConstantsEnum.FENCE_PLAN_MARBLE_HIGH_IRON_FENCE_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_MARBLE_STONE_PARAPET || this.type == StructureConstantsEnum.FENCE_PLAN_MARBLE_CHAIN_FENCE);
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
/*      */   public final boolean isStoneFence() {
/* 1430 */     return (this.type == StructureConstantsEnum.FENCE_STONE || this.type == StructureConstantsEnum.FENCE_IRON || this.type == StructureConstantsEnum.FENCE_IRON_GATE || this.type == StructureConstantsEnum.FENCE_STONEWALL_HIGH || this.type == StructureConstantsEnum.FENCE_IRON_HIGH || this.type == StructureConstantsEnum.FENCE_IRON_GATE_HIGH || this.type == StructureConstantsEnum.FENCE_STONE_PARAPET || this.type == StructureConstantsEnum.FENCE_PORTCULLIS || this.type == StructureConstantsEnum.FENCE_MEDIUM_CHAIN);
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
/*      */   public final boolean isStone() {
/* 1444 */     return isStone(this.type);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isRubble() {
/* 1449 */     return (this.type == StructureConstantsEnum.FENCE_RUBBLE);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isStone(StructureConstantsEnum fenceType) {
/* 1454 */     switch (fenceType) {
/*      */       
/*      */       case FENCE_CURB:
/*      */       case FENCE_STONEWALL:
/*      */       case FENCE_STONE_PARAPET:
/*      */       case FENCE_STONE_IRON_PARAPET:
/*      */       case FENCE_STONE:
/*      */       case FENCE_PLAN_STONEWALL_HIGH:
/*      */       case FENCE_STONEWALL_HIGH:
/*      */       case FENCE_PLAN_PORTCULLIS:
/*      */       case FENCE_SLATE_TALL_STONE_WALL:
/*      */       case FENCE_ROUNDED_STONE_TALL_STONE_WALL:
/*      */       case FENCE_SANDSTONE_TALL_STONE_WALL:
/*      */       case FENCE_RENDERED_TALL_STONE_WALL:
/*      */       case FENCE_POTTERY_TALL_STONE_WALL:
/*      */       case FENCE_MARBLE_TALL_STONE_WALL:
/*      */       case FENCE_SLATE_PORTCULLIS:
/*      */       case FENCE_ROUNDED_STONE_PORTCULLIS:
/*      */       case FENCE_SANDSTONE_PORTCULLIS:
/*      */       case FENCE_RENDERED_PORTCULLIS:
/*      */       case FENCE_POTTERY_PORTCULLIS:
/*      */       case FENCE_MARBLE_PORTCULLIS:
/*      */       case FENCE_PLAN_SLATE_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_ROUNDED_STONE_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_SANDSTONE_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_RENDERED_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_POTTERY_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_MARBLE_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_SLATE_PORTCULLIS:
/*      */       case FENCE_PLAN_ROUNDED_STONE_PORTCULLIS:
/*      */       case FENCE_PLAN_SANDSTONE_PORTCULLIS:
/*      */       case FENCE_PLAN_RENDERED_PORTCULLIS:
/*      */       case FENCE_PLAN_POTTERY_PORTCULLIS:
/*      */       case FENCE_PLAN_MARBLE_PORTCULLIS:
/*      */       case FENCE_PLAN_STONEWALL:
/*      */       case FENCE_PLAN_STONE:
/*      */       case FENCE_SLATE:
/*      */       case FENCE_ROUNDED_STONE:
/*      */       case FENCE_POTTERY:
/*      */       case FENCE_SANDSTONE:
/*      */       case FENCE_MARBLE:
/*      */       case FENCE_PLAN_STONE_PARAPET:
/*      */       case FENCE_PLAN_STONE_IRON_PARAPET:
/*      */       case FENCE_PLAN_CURB:
/*      */       case FENCE_PORTCULLIS:
/*      */       case FENCE_RENDERED:
/* 1500 */         return true;
/*      */     } 
/*      */     
/* 1503 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isWood() {
/* 1509 */     return isWood(this.type);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isWood(StructureConstantsEnum fenceType) {
/* 1514 */     switch (fenceType) {
/*      */       
/*      */       case FENCE_WOVEN:
/*      */       case FENCE_ROPE_LOW:
/*      */       case FENCE_GARDESGARD_LOW:
/*      */       case FENCE_WOODEN:
/*      */       case FENCE_WOODEN_PARAPET:
/*      */       case FENCE_ROPE_HIGH:
/*      */       case FENCE_PLAN_ROPE_HIGH:
/*      */       case FENCE_GARDESGARD_GATE:
/*      */       case FENCE_WOODEN_GATE:
/*      */       case FENCE_WOODEN_CRUDE_GATE:
/*      */       case FENCE_WOODEN_CRUDE:
/*      */       case FENCE_PLAN_PALISADE:
/*      */       case FENCE_PLAN_PALISADE_GATE:
/*      */       case FENCE_PALISADE:
/*      */       case FENCE_PALISADE_GATE:
/*      */       case FENCE_PLAN_WOODEN_PARAPET:
/*      */       case FENCE_PLAN_ROPE_LOW:
/*      */       case FENCE_PLAN_WOODEN:
/*      */       case FENCE_PLAN_WOODEN_GATE:
/*      */       case FENCE_PLAN_WOODEN_CRUDE:
/*      */       case FENCE_PLAN_WOODEN_GATE_CRUDE:
/*      */       case FENCE_PLAN_GARDESGARD_GATE:
/*      */       case FENCE_GARDESGARD_HIGH:
/*      */       case FENCE_PLAN_GARDESGARD_HIGH:
/*      */       case FENCE_PLAN_GARDESGARD_LOW:
/*      */       case FENCE_PLAN_WOVEN:
/*      */       case FENCE_SIEGEWALL:
/* 1543 */         return true;
/*      */     } 
/*      */ 
/*      */     
/* 1547 */     return isHedge(fenceType);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isMetal() {
/* 1553 */     return (this.type == StructureConstantsEnum.FENCE_IRON || this.type == StructureConstantsEnum.FENCE_IRON_GATE || this.type == StructureConstantsEnum.FENCE_IRON_HIGH || this.type == StructureConstantsEnum.FENCE_IRON_GATE_HIGH || this.type == StructureConstantsEnum.FENCE_MEDIUM_CHAIN || this.type == StructureConstantsEnum.FENCE_MARBLE_HIGH_IRON_FENCE || this.type == StructureConstantsEnum.FENCE_MARBLE_HIGH_IRON_FENCE_GATE || this.type == StructureConstantsEnum.FENCE_MARBLE_CHAIN_FENCE || this.type == StructureConstantsEnum.FENCE_SLATE_HIGH_IRON_FENCE || this.type == StructureConstantsEnum.FENCE_SLATE_HIGH_IRON_FENCE_GATE || this.type == StructureConstantsEnum.FENCE_SLATE_CHAIN_FENCE || this.type == StructureConstantsEnum.FENCE_POTTERY_HIGH_IRON_FENCE || this.type == StructureConstantsEnum.FENCE_POTTERY_HIGH_IRON_FENCE_GATE || this.type == StructureConstantsEnum.FENCE_POTTERY_CHAIN_FENCE || this.type == StructureConstantsEnum.FENCE_RENDERED_HIGH_IRON_FENCE || this.type == StructureConstantsEnum.FENCE_RENDERED_HIGH_IRON_FENCE_GATE || this.type == StructureConstantsEnum.FENCE_RENDERED_CHAIN_FENCE || this.type == StructureConstantsEnum.FENCE_ROUNDED_STONE_HIGH_IRON_FENCE || this.type == StructureConstantsEnum.FENCE_ROUNDED_STONE_HIGH_IRON_FENCE_GATE || this.type == StructureConstantsEnum.FENCE_ROUNDED_STONE_CHAIN_FENCE || this.type == StructureConstantsEnum.FENCE_SANDSTONE_HIGH_IRON_FENCE || this.type == StructureConstantsEnum.FENCE_SANDSTONE_HIGH_IRON_FENCE_GATE || this.type == StructureConstantsEnum.FENCE_SANDSTONE_CHAIN_FENCE);
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
/*      */   public static final boolean isWoven(StructureConstantsEnum fenceType) {
/* 1580 */     switch (fenceType) {
/*      */       
/*      */       case FENCE_WOVEN:
/*      */       case FENCE_PLAN_WOVEN:
/* 1584 */         return true;
/*      */     } 
/* 1586 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isWoven() {
/* 1592 */     return isWoven(this.type);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final short[] getAllLowHedgeTypes() {
/* 1597 */     return new short[] { StructureConstantsEnum.HEDGE_FLOWER1_LOW.value, StructureConstantsEnum.HEDGE_FLOWER2_LOW.value, StructureConstantsEnum.HEDGE_FLOWER3_LOW.value, StructureConstantsEnum.HEDGE_FLOWER4_LOW.value, StructureConstantsEnum.HEDGE_FLOWER5_LOW.value, StructureConstantsEnum.HEDGE_FLOWER6_LOW.value, StructureConstantsEnum.HEDGE_FLOWER7_LOW.value };
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
/*      */   public static final StructureConstantsEnum getLowHedgeType(byte treeMaterial) {
/* 1611 */     switch (treeMaterial) {
/*      */       
/*      */       case 46:
/* 1614 */         return StructureConstantsEnum.HEDGE_FLOWER1_LOW;
/*      */       case 51:
/* 1616 */         return StructureConstantsEnum.HEDGE_FLOWER2_LOW;
/*      */       case 50:
/* 1618 */         return StructureConstantsEnum.HEDGE_FLOWER3_LOW;
/*      */       case 47:
/* 1620 */         return StructureConstantsEnum.HEDGE_FLOWER4_LOW;
/*      */       case 48:
/* 1622 */         return StructureConstantsEnum.HEDGE_FLOWER5_LOW;
/*      */       case 39:
/* 1624 */         return StructureConstantsEnum.HEDGE_FLOWER6_LOW;
/*      */       case 41:
/* 1626 */         return StructureConstantsEnum.HEDGE_FLOWER7_LOW;
/*      */     } 
/* 1628 */     return StructureConstantsEnum.FENCE_PLAN_WOODEN;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final byte getMaterialForLowHedge(StructureConstantsEnum hedgeType) {
/* 1634 */     switch (hedgeType) {
/*      */       
/*      */       case HEDGE_FLOWER1_LOW:
/* 1637 */         return 46;
/*      */       case HEDGE_FLOWER2_LOW:
/* 1639 */         return 51;
/*      */       case HEDGE_FLOWER3_LOW:
/* 1641 */         return 50;
/*      */       case HEDGE_FLOWER4_LOW:
/* 1643 */         return 47;
/*      */       case HEDGE_FLOWER5_LOW:
/* 1645 */         return 48;
/*      */       case HEDGE_FLOWER6_LOW:
/* 1647 */         return 39;
/*      */       case HEDGE_FLOWER7_LOW:
/* 1649 */         return 41;
/*      */     } 
/* 1651 */     return 46;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final short[] getAllFlowerbeds() {
/* 1657 */     return new short[] { StructureConstantsEnum.FLOWERBED_YELLOW.value, StructureConstantsEnum.FLOWERBED_ORANGE_RED.value, StructureConstantsEnum.FLOWERBED_PURPLE.value, StructureConstantsEnum.FLOWERBED_WHITE.value, StructureConstantsEnum.FLOWERBED_BLUE.value, StructureConstantsEnum.FLOWERBED_GREENISH_YELLOW.value, StructureConstantsEnum.FLOWERBED_WHITE_DOTTED.value };
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
/*      */   public static final int getFlowerTypeByFlowerbedType(StructureConstantsEnum flowerbedType) {
/* 1671 */     switch (flowerbedType) {
/*      */       
/*      */       case FLOWERBED_YELLOW:
/* 1674 */         return 498;
/*      */       case FLOWERBED_ORANGE_RED:
/* 1676 */         return 499;
/*      */       case FLOWERBED_PURPLE:
/* 1678 */         return 500;
/*      */       case FLOWERBED_WHITE:
/* 1680 */         return 501;
/*      */       case FLOWERBED_BLUE:
/* 1682 */         return 502;
/*      */       case FLOWERBED_GREENISH_YELLOW:
/* 1684 */         return 503;
/*      */       case FLOWERBED_WHITE_DOTTED:
/* 1686 */         return 504;
/*      */     } 
/*      */     
/* 1689 */     return 498;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final StructureConstantsEnum getFlowerbedType(int templateId) {
/* 1695 */     switch (templateId) {
/*      */       
/*      */       case 498:
/* 1698 */         return StructureConstantsEnum.FLOWERBED_YELLOW;
/*      */       case 499:
/* 1700 */         return StructureConstantsEnum.FLOWERBED_ORANGE_RED;
/*      */       case 500:
/* 1702 */         return StructureConstantsEnum.FLOWERBED_PURPLE;
/*      */       case 501:
/* 1704 */         return StructureConstantsEnum.FLOWERBED_WHITE;
/*      */       case 502:
/* 1706 */         return StructureConstantsEnum.FLOWERBED_BLUE;
/*      */       case 503:
/* 1708 */         return StructureConstantsEnum.FLOWERBED_GREENISH_YELLOW;
/*      */       case 504:
/* 1710 */         return StructureConstantsEnum.FLOWERBED_WHITE_DOTTED;
/*      */     } 
/*      */     
/* 1713 */     return StructureConstantsEnum.FENCE_PLAN_WOODEN;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final StructureConstantsEnum getFencePlanType(int action) {
/* 1719 */     if (action == 166)
/*      */     {
/* 1721 */       return StructureConstantsEnum.FENCE_PLAN_WOODEN;
/*      */     }
/* 1723 */     if (action == 520)
/*      */     {
/* 1725 */       return StructureConstantsEnum.FENCE_PLAN_WOODEN_CRUDE;
/*      */     }
/* 1727 */     if (action == 528)
/*      */     {
/* 1729 */       return StructureConstantsEnum.FENCE_PLAN_WOODEN_GATE_CRUDE;
/*      */     }
/* 1731 */     if (action == 526)
/*      */     {
/* 1733 */       return StructureConstantsEnum.FENCE_PLAN_GARDESGARD_LOW;
/*      */     }
/* 1735 */     if (action == 527)
/*      */     {
/* 1737 */       return StructureConstantsEnum.FENCE_PLAN_GARDESGARD_HIGH;
/*      */     }
/* 1739 */     if (action == 529)
/*      */     {
/* 1741 */       return StructureConstantsEnum.FENCE_PLAN_GARDESGARD_GATE;
/*      */     }
/* 1743 */     if (action == 165)
/*      */     {
/* 1745 */       return StructureConstantsEnum.FENCE_PLAN_PALISADE;
/*      */     }
/* 1747 */     if (action == 163)
/*      */     {
/* 1749 */       return StructureConstantsEnum.FENCE_PLAN_STONEWALL;
/*      */     }
/* 1751 */     if (action == 167)
/*      */     {
/* 1753 */       return StructureConstantsEnum.FENCE_PLAN_PALISADE_GATE;
/*      */     }
/* 1755 */     if (action == 168)
/*      */     {
/* 1757 */       return StructureConstantsEnum.FENCE_PLAN_WOODEN_GATE;
/*      */     }
/* 1759 */     if (action == 164)
/*      */     {
/* 1761 */       return StructureConstantsEnum.FENCE_PLAN_STONEWALL_HIGH;
/*      */     }
/* 1763 */     if (action == 477)
/*      */     {
/* 1765 */       return StructureConstantsEnum.FENCE_PLAN_IRON;
/*      */     }
/* 1767 */     if (action == 478)
/*      */     {
/* 1769 */       return StructureConstantsEnum.FENCE_PLAN_WOVEN;
/*      */     }
/* 1771 */     if (action == 479)
/*      */     {
/* 1773 */       return StructureConstantsEnum.FENCE_PLAN_IRON_GATE;
/*      */     }
/* 1775 */     if (action == 516)
/*      */     {
/* 1777 */       return StructureConstantsEnum.FENCE_PLAN_WOODEN_PARAPET;
/*      */     }
/* 1779 */     if (action == 517)
/*      */     {
/* 1781 */       return StructureConstantsEnum.FENCE_PLAN_STONE_PARAPET;
/*      */     }
/* 1783 */     if (action == 521)
/*      */     {
/* 1785 */       return StructureConstantsEnum.FENCE_PLAN_STONE_IRON_PARAPET;
/*      */     }
/* 1787 */     if (action == 541)
/*      */     {
/* 1789 */       return StructureConstantsEnum.FENCE_PLAN_STONE;
/*      */     }
/* 1791 */     if (action == 542)
/*      */     {
/* 1793 */       return StructureConstantsEnum.FENCE_PLAN_CURB;
/*      */     }
/* 1795 */     if (action == 543)
/*      */     {
/* 1797 */       return StructureConstantsEnum.FENCE_PLAN_ROPE_LOW;
/*      */     }
/* 1799 */     if (action == 544)
/*      */     {
/* 1801 */       return StructureConstantsEnum.FENCE_PLAN_ROPE_HIGH;
/*      */     }
/* 1803 */     if (action == 545)
/*      */     {
/* 1805 */       return StructureConstantsEnum.FENCE_PLAN_IRON_HIGH;
/*      */     }
/* 1807 */     if (action == 546)
/*      */     {
/* 1809 */       return StructureConstantsEnum.FENCE_PLAN_IRON_GATE_HIGH;
/*      */     }
/* 1811 */     if (action == 611)
/*      */     {
/* 1813 */       return StructureConstantsEnum.FENCE_PLAN_MEDIUM_CHAIN;
/*      */     }
/* 1815 */     if (action == 654)
/*      */     {
/* 1817 */       return StructureConstantsEnum.FENCE_PLAN_PORTCULLIS;
/*      */     }
/* 1819 */     if (action == 832)
/*      */     {
/* 1821 */       return StructureConstantsEnum.FENCE_PLAN_SLATE;
/*      */     }
/* 1823 */     if (action == 835)
/*      */     {
/* 1825 */       return StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE;
/*      */     }
/* 1827 */     if (action == 838)
/*      */     {
/* 1829 */       return StructureConstantsEnum.FENCE_PLAN_POTTERY;
/*      */     }
/* 1831 */     if (action == 841)
/*      */     {
/* 1833 */       return StructureConstantsEnum.FENCE_PLAN_SANDSTONE;
/*      */     }
/* 1835 */     if (action == 844)
/*      */     {
/* 1837 */       return StructureConstantsEnum.FENCE_PLAN_MARBLE;
/*      */     }
/* 1839 */     if (action == 833)
/*      */     {
/* 1841 */       return StructureConstantsEnum.FENCE_PLAN_SLATE_IRON;
/*      */     }
/* 1843 */     if (action == 836)
/*      */     {
/* 1845 */       return StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_IRON;
/*      */     }
/* 1847 */     if (action == 839)
/*      */     {
/* 1849 */       return StructureConstantsEnum.FENCE_PLAN_POTTERY_IRON;
/*      */     }
/* 1851 */     if (action == 842)
/*      */     {
/* 1853 */       return StructureConstantsEnum.FENCE_PLAN_SANDSTONE_IRON;
/*      */     }
/* 1855 */     if (action == 845)
/*      */     {
/* 1857 */       return StructureConstantsEnum.FENCE_PLAN_MARBLE_IRON;
/*      */     }
/* 1859 */     if (action == 834)
/*      */     {
/* 1861 */       return StructureConstantsEnum.FENCE_PLAN_SLATE_IRON_GATE;
/*      */     }
/* 1863 */     if (action == 837)
/*      */     {
/* 1865 */       return StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_IRON_GATE;
/*      */     }
/* 1867 */     if (action == 840)
/*      */     {
/* 1869 */       return StructureConstantsEnum.FENCE_PLAN_POTTERY_IRON_GATE;
/*      */     }
/* 1871 */     if (action == 843)
/*      */     {
/* 1873 */       return StructureConstantsEnum.FENCE_PLAN_SANDSTONE_IRON_GATE;
/*      */     }
/* 1875 */     if (action == 846)
/*      */     {
/* 1877 */       return StructureConstantsEnum.FENCE_PLAN_MARBLE_IRON_GATE;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1882 */     if (action == 870)
/*      */     {
/* 1884 */       return StructureConstantsEnum.FENCE_PLAN_SLATE_TALL_STONE_WALL;
/*      */     }
/* 1886 */     if (action == 871)
/*      */     {
/* 1888 */       return StructureConstantsEnum.FENCE_PLAN_SLATE_PORTCULLIS;
/*      */     }
/* 1890 */     if (action == 872)
/*      */     {
/* 1892 */       return StructureConstantsEnum.FENCE_PLAN_SLATE_HIGH_IRON_FENCE;
/*      */     }
/* 1894 */     if (action == 873)
/*      */     {
/* 1896 */       return StructureConstantsEnum.FENCE_PLAN_SLATE_HIGH_IRON_FENCE_GATE;
/*      */     }
/* 1898 */     if (action == 874)
/*      */     {
/* 1900 */       return StructureConstantsEnum.FENCE_PLAN_SLATE_STONE_PARAPET;
/*      */     }
/* 1902 */     if (action == 875)
/*      */     {
/* 1904 */       return StructureConstantsEnum.FENCE_PLAN_SLATE_CHAIN_FENCE;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1909 */     if (action == 876)
/*      */     {
/* 1911 */       return StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_TALL_STONE_WALL;
/*      */     }
/* 1913 */     if (action == 877)
/*      */     {
/* 1915 */       return StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_PORTCULLIS;
/*      */     }
/* 1917 */     if (action == 878)
/*      */     {
/* 1919 */       return StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE;
/*      */     }
/* 1921 */     if (action == 879)
/*      */     {
/* 1923 */       return StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE_GATE;
/*      */     }
/* 1925 */     if (action == 880)
/*      */     {
/* 1927 */       return StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_STONE_PARAPET;
/*      */     }
/* 1929 */     if (action == 881)
/*      */     {
/* 1931 */       return StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_CHAIN_FENCE;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1936 */     if (action == 882)
/*      */     {
/* 1938 */       return StructureConstantsEnum.FENCE_PLAN_SANDSTONE_TALL_STONE_WALL;
/*      */     }
/* 1940 */     if (action == 883)
/*      */     {
/* 1942 */       return StructureConstantsEnum.FENCE_PLAN_SANDSTONE_PORTCULLIS;
/*      */     }
/* 1944 */     if (action == 884)
/*      */     {
/* 1946 */       return StructureConstantsEnum.FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE;
/*      */     }
/* 1948 */     if (action == 885)
/*      */     {
/* 1950 */       return StructureConstantsEnum.FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE_GATE;
/*      */     }
/* 1952 */     if (action == 886)
/*      */     {
/* 1954 */       return StructureConstantsEnum.FENCE_PLAN_SANDSTONE_STONE_PARAPET;
/*      */     }
/* 1956 */     if (action == 887)
/*      */     {
/* 1958 */       return StructureConstantsEnum.FENCE_PLAN_SANDSTONE_CHAIN_FENCE;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1963 */     if (action == 888)
/*      */     {
/* 1965 */       return StructureConstantsEnum.FENCE_PLAN_RENDERED_TALL_STONE_WALL;
/*      */     }
/* 1967 */     if (action == 889)
/*      */     {
/* 1969 */       return StructureConstantsEnum.FENCE_PLAN_RENDERED_PORTCULLIS;
/*      */     }
/* 1971 */     if (action == 890)
/*      */     {
/* 1973 */       return StructureConstantsEnum.FENCE_PLAN_RENDERED_HIGH_IRON_FENCE;
/*      */     }
/* 1975 */     if (action == 891)
/*      */     {
/* 1977 */       return StructureConstantsEnum.FENCE_PLAN_RENDERED_HIGH_IRON_FENCE_GATE;
/*      */     }
/* 1979 */     if (action == 892)
/*      */     {
/* 1981 */       return StructureConstantsEnum.FENCE_PLAN_RENDERED_STONE_PARAPET;
/*      */     }
/* 1983 */     if (action == 893)
/*      */     {
/* 1985 */       return StructureConstantsEnum.FENCE_PLAN_RENDERED_CHAIN_FENCE;
/*      */     }
/*      */ 
/*      */     
/* 1989 */     if (action == 894)
/*      */     {
/* 1991 */       return StructureConstantsEnum.FENCE_PLAN_POTTERY_TALL_STONE_WALL;
/*      */     }
/* 1993 */     if (action == 895)
/*      */     {
/* 1995 */       return StructureConstantsEnum.FENCE_PLAN_POTTERY_PORTCULLIS;
/*      */     }
/* 1997 */     if (action == 896)
/*      */     {
/* 1999 */       return StructureConstantsEnum.FENCE_PLAN_POTTERY_HIGH_IRON_FENCE;
/*      */     }
/* 2001 */     if (action == 897)
/*      */     {
/* 2003 */       return StructureConstantsEnum.FENCE_PLAN_POTTERY_HIGH_IRON_FENCE_GATE;
/*      */     }
/* 2005 */     if (action == 898)
/*      */     {
/* 2007 */       return StructureConstantsEnum.FENCE_PLAN_POTTERY_STONE_PARAPET;
/*      */     }
/* 2009 */     if (action == 899)
/*      */     {
/* 2011 */       return StructureConstantsEnum.FENCE_PLAN_POTTERY_CHAIN_FENCE;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 2016 */     if (action == 900)
/*      */     {
/* 2018 */       return StructureConstantsEnum.FENCE_PLAN_MARBLE_TALL_STONE_WALL;
/*      */     }
/* 2020 */     if (action == 901)
/*      */     {
/* 2022 */       return StructureConstantsEnum.FENCE_PLAN_MARBLE_PORTCULLIS;
/*      */     }
/* 2024 */     if (action == 902)
/*      */     {
/* 2026 */       return StructureConstantsEnum.FENCE_PLAN_MARBLE_HIGH_IRON_FENCE;
/*      */     }
/* 2028 */     if (action == 903)
/*      */     {
/* 2030 */       return StructureConstantsEnum.FENCE_PLAN_MARBLE_HIGH_IRON_FENCE_GATE;
/*      */     }
/* 2032 */     if (action == 904)
/*      */     {
/* 2034 */       return StructureConstantsEnum.FENCE_PLAN_MARBLE_STONE_PARAPET;
/*      */     }
/* 2036 */     if (action == 905)
/*      */     {
/* 2038 */       return StructureConstantsEnum.FENCE_PLAN_MARBLE_CHAIN_FENCE;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2045 */     logger.log(Level.WARNING, "Fence plan for action " + action + " is not found!", new Exception());
/* 2046 */     return StructureConstantsEnum.FENCE_PLAN_WOODEN;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final StructureConstantsEnum getFencePlanForType(StructureConstantsEnum type) {
/* 2051 */     switch (type) {
/*      */       
/*      */       case FENCE_WOODEN:
/* 2054 */         return StructureConstantsEnum.FENCE_PLAN_WOODEN;
/*      */       case FENCE_WOODEN_CRUDE:
/* 2056 */         return StructureConstantsEnum.FENCE_PLAN_WOODEN_CRUDE;
/*      */       case FENCE_GARDESGARD_LOW:
/* 2058 */         return StructureConstantsEnum.FENCE_PLAN_GARDESGARD_LOW;
/*      */       case FENCE_GARDESGARD_HIGH:
/* 2060 */         return StructureConstantsEnum.FENCE_PLAN_GARDESGARD_HIGH;
/*      */       case FENCE_GARDESGARD_GATE:
/* 2062 */         return StructureConstantsEnum.FENCE_PLAN_GARDESGARD_GATE;
/*      */       case FENCE_PALISADE:
/* 2064 */         return StructureConstantsEnum.FENCE_PLAN_PALISADE;
/*      */       case FENCE_STONEWALL:
/* 2066 */         return StructureConstantsEnum.FENCE_PLAN_STONEWALL;
/*      */       case FENCE_WOODEN_PARAPET:
/* 2068 */         return StructureConstantsEnum.FENCE_PLAN_WOODEN_PARAPET;
/*      */       case FENCE_STONE_PARAPET:
/* 2070 */         return StructureConstantsEnum.FENCE_PLAN_STONE_PARAPET;
/*      */       case FENCE_STONE_IRON_PARAPET:
/* 2072 */         return StructureConstantsEnum.FENCE_PLAN_STONE_IRON_PARAPET;
/*      */       case FENCE_PALISADE_GATE:
/* 2074 */         return StructureConstantsEnum.FENCE_PLAN_PALISADE_GATE;
/*      */       case FENCE_WOODEN_GATE:
/* 2076 */         return StructureConstantsEnum.FENCE_PLAN_WOODEN_GATE;
/*      */       case FENCE_WOODEN_CRUDE_GATE:
/* 2078 */         return StructureConstantsEnum.FENCE_PLAN_WOODEN_GATE_CRUDE;
/*      */       case FENCE_STONEWALL_HIGH:
/* 2080 */         return StructureConstantsEnum.FENCE_PLAN_STONEWALL_HIGH;
/*      */       case FENCE_IRON:
/* 2082 */         return StructureConstantsEnum.FENCE_PLAN_IRON;
/*      */       case FENCE_WOVEN:
/* 2084 */         return StructureConstantsEnum.FENCE_PLAN_WOVEN;
/*      */       case FENCE_IRON_GATE:
/* 2086 */         return StructureConstantsEnum.FENCE_PLAN_IRON_GATE;
/*      */       case FENCE_STONE:
/* 2088 */         return StructureConstantsEnum.FENCE_PLAN_STONE;
/*      */       case FENCE_CURB:
/* 2090 */         return StructureConstantsEnum.FENCE_PLAN_CURB;
/*      */       case FENCE_ROPE_LOW:
/* 2092 */         return StructureConstantsEnum.FENCE_PLAN_ROPE_LOW;
/*      */       case FENCE_ROPE_HIGH:
/* 2094 */         return StructureConstantsEnum.FENCE_PLAN_ROPE_HIGH;
/*      */       case FENCE_IRON_HIGH:
/* 2096 */         return StructureConstantsEnum.FENCE_PLAN_IRON_HIGH;
/*      */       case FENCE_IRON_GATE_HIGH:
/* 2098 */         return StructureConstantsEnum.FENCE_PLAN_IRON_GATE_HIGH;
/*      */       case FENCE_MEDIUM_CHAIN:
/* 2100 */         return StructureConstantsEnum.FENCE_PLAN_MEDIUM_CHAIN;
/*      */       case FENCE_PORTCULLIS:
/* 2102 */         return StructureConstantsEnum.FENCE_PLAN_PORTCULLIS;
/*      */       case FENCE_SLATE:
/* 2104 */         return StructureConstantsEnum.FENCE_PLAN_SLATE;
/*      */       case FENCE_ROUNDED_STONE:
/* 2106 */         return StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE;
/*      */       case FENCE_POTTERY:
/* 2108 */         return StructureConstantsEnum.FENCE_PLAN_POTTERY;
/*      */       case FENCE_SANDSTONE:
/* 2110 */         return StructureConstantsEnum.FENCE_PLAN_SANDSTONE;
/*      */       case FENCE_RENDERED:
/* 2112 */         return StructureConstantsEnum.FENCE_PLAN_RENDERED;
/*      */       case FENCE_MARBLE:
/* 2114 */         return StructureConstantsEnum.FENCE_PLAN_MARBLE;
/*      */       case FENCE_SLATE_IRON:
/* 2116 */         return StructureConstantsEnum.FENCE_PLAN_SLATE_IRON;
/*      */       case FENCE_ROUNDED_STONE_IRON:
/* 2118 */         return StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_IRON;
/*      */       case FENCE_POTTERY_IRON:
/* 2120 */         return StructureConstantsEnum.FENCE_PLAN_POTTERY_IRON;
/*      */       case FENCE_SANDSTONE_IRON:
/* 2122 */         return StructureConstantsEnum.FENCE_PLAN_SANDSTONE_IRON;
/*      */       case FENCE_RENDERED_IRON:
/* 2124 */         return StructureConstantsEnum.FENCE_PLAN_RENDERED_IRON;
/*      */       case FENCE_MARBLE_IRON:
/* 2126 */         return StructureConstantsEnum.FENCE_PLAN_MARBLE_IRON;
/*      */       case FENCE_SLATE_IRON_GATE:
/* 2128 */         return StructureConstantsEnum.FENCE_PLAN_SLATE_IRON_GATE;
/*      */       case FENCE_ROUNDED_STONE_IRON_GATE:
/* 2130 */         return StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_IRON_GATE;
/*      */       case FENCE_POTTERY_IRON_GATE:
/* 2132 */         return StructureConstantsEnum.FENCE_PLAN_POTTERY_IRON_GATE;
/*      */       case FENCE_SANDSTONE_IRON_GATE:
/* 2134 */         return StructureConstantsEnum.FENCE_PLAN_SANDSTONE_IRON_GATE;
/*      */       case FENCE_RENDERED_IRON_GATE:
/* 2136 */         return StructureConstantsEnum.FENCE_PLAN_RENDERED_IRON_GATE;
/*      */       case FENCE_MARBLE_IRON_GATE:
/* 2138 */         return StructureConstantsEnum.FENCE_PLAN_MARBLE_IRON_GATE;
/*      */       
/*      */       case FENCE_SLATE_TALL_STONE_WALL:
/* 2141 */         return StructureConstantsEnum.FENCE_PLAN_SLATE_TALL_STONE_WALL;
/*      */       case FENCE_SLATE_PORTCULLIS:
/* 2143 */         return StructureConstantsEnum.FENCE_PLAN_SLATE_PORTCULLIS;
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE:
/* 2145 */         return StructureConstantsEnum.FENCE_PLAN_SLATE_HIGH_IRON_FENCE;
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE_GATE:
/* 2147 */         return StructureConstantsEnum.FENCE_PLAN_SLATE_HIGH_IRON_FENCE_GATE;
/*      */       case FENCE_SLATE_STONE_PARAPET:
/* 2149 */         return StructureConstantsEnum.FENCE_PLAN_SLATE_STONE_PARAPET;
/*      */       case FENCE_SLATE_CHAIN_FENCE:
/* 2151 */         return StructureConstantsEnum.FENCE_PLAN_SLATE_CHAIN_FENCE;
/*      */       
/*      */       case FENCE_ROUNDED_STONE_TALL_STONE_WALL:
/* 2154 */         return StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_TALL_STONE_WALL;
/*      */       case FENCE_ROUNDED_STONE_PORTCULLIS:
/* 2156 */         return StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_PORTCULLIS;
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE:
/* 2158 */         return StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE;
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/* 2160 */         return StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE_GATE;
/*      */       case FENCE_ROUNDED_STONE_STONE_PARAPET:
/* 2162 */         return StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_STONE_PARAPET;
/*      */       case FENCE_ROUNDED_STONE_CHAIN_FENCE:
/* 2164 */         return StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_CHAIN_FENCE;
/*      */       
/*      */       case FENCE_SANDSTONE_TALL_STONE_WALL:
/* 2167 */         return StructureConstantsEnum.FENCE_PLAN_SANDSTONE_TALL_STONE_WALL;
/*      */       case FENCE_SANDSTONE_PORTCULLIS:
/* 2169 */         return StructureConstantsEnum.FENCE_PLAN_SANDSTONE_PORTCULLIS;
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE:
/* 2171 */         return StructureConstantsEnum.FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE;
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE_GATE:
/* 2173 */         return StructureConstantsEnum.FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE_GATE;
/*      */       case FENCE_SANDSTONE_STONE_PARAPET:
/* 2175 */         return StructureConstantsEnum.FENCE_PLAN_SANDSTONE_STONE_PARAPET;
/*      */       case FENCE_SANDSTONE_CHAIN_FENCE:
/* 2177 */         return StructureConstantsEnum.FENCE_PLAN_SANDSTONE_CHAIN_FENCE;
/*      */       
/*      */       case FENCE_RENDERED_TALL_STONE_WALL:
/* 2180 */         return StructureConstantsEnum.FENCE_PLAN_RENDERED_TALL_STONE_WALL;
/*      */       case FENCE_RENDERED_PORTCULLIS:
/* 2182 */         return StructureConstantsEnum.FENCE_PLAN_RENDERED_PORTCULLIS;
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE:
/* 2184 */         return StructureConstantsEnum.FENCE_PLAN_RENDERED_HIGH_IRON_FENCE;
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE_GATE:
/* 2186 */         return StructureConstantsEnum.FENCE_PLAN_RENDERED_HIGH_IRON_FENCE_GATE;
/*      */       case FENCE_RENDERED_STONE_PARAPET:
/* 2188 */         return StructureConstantsEnum.FENCE_PLAN_RENDERED_STONE_PARAPET;
/*      */       case FENCE_RENDERED_CHAIN_FENCE:
/* 2190 */         return StructureConstantsEnum.FENCE_PLAN_RENDERED_CHAIN_FENCE;
/*      */       
/*      */       case FENCE_POTTERY_TALL_STONE_WALL:
/* 2193 */         return StructureConstantsEnum.FENCE_PLAN_POTTERY_TALL_STONE_WALL;
/*      */       case FENCE_POTTERY_PORTCULLIS:
/* 2195 */         return StructureConstantsEnum.FENCE_PLAN_POTTERY_PORTCULLIS;
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE:
/* 2197 */         return StructureConstantsEnum.FENCE_PLAN_POTTERY_HIGH_IRON_FENCE;
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE_GATE:
/* 2199 */         return StructureConstantsEnum.FENCE_PLAN_POTTERY_HIGH_IRON_FENCE_GATE;
/*      */       case FENCE_POTTERY_STONE_PARAPET:
/* 2201 */         return StructureConstantsEnum.FENCE_PLAN_POTTERY_STONE_PARAPET;
/*      */       case FENCE_POTTERY_CHAIN_FENCE:
/* 2203 */         return StructureConstantsEnum.FENCE_PLAN_POTTERY_CHAIN_FENCE;
/*      */       
/*      */       case FENCE_MARBLE_TALL_STONE_WALL:
/* 2206 */         return StructureConstantsEnum.FENCE_PLAN_MARBLE_TALL_STONE_WALL;
/*      */       case FENCE_MARBLE_PORTCULLIS:
/* 2208 */         return StructureConstantsEnum.FENCE_PLAN_MARBLE_PORTCULLIS;
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE:
/* 2210 */         return StructureConstantsEnum.FENCE_PLAN_MARBLE_HIGH_IRON_FENCE;
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE_GATE:
/* 2212 */         return StructureConstantsEnum.FENCE_PLAN_MARBLE_HIGH_IRON_FENCE_GATE;
/*      */       case FENCE_MARBLE_STONE_PARAPET:
/* 2214 */         return StructureConstantsEnum.FENCE_PLAN_MARBLE_STONE_PARAPET;
/*      */       case FENCE_MARBLE_CHAIN_FENCE:
/* 2216 */         return StructureConstantsEnum.FENCE_PLAN_MARBLE_CHAIN_FENCE;
/*      */     } 
/* 2218 */     logger.log(Level.WARNING, "Fence plan for type " + type + " is not found!", new Exception());
/*      */ 
/*      */     
/* 2221 */     return type;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final StructureConstantsEnum getFenceForPlan(StructureConstantsEnum type) {
/* 2226 */     switch (type) {
/*      */       
/*      */       case FENCE_PLAN_WOODEN:
/* 2229 */         return StructureConstantsEnum.FENCE_WOODEN;
/*      */       case FENCE_PLAN_WOODEN_CRUDE:
/* 2231 */         return StructureConstantsEnum.FENCE_WOODEN_CRUDE;
/*      */       case FENCE_PLAN_GARDESGARD_LOW:
/* 2233 */         return StructureConstantsEnum.FENCE_GARDESGARD_LOW;
/*      */       case FENCE_PLAN_GARDESGARD_HIGH:
/* 2235 */         return StructureConstantsEnum.FENCE_GARDESGARD_HIGH;
/*      */       case FENCE_PLAN_GARDESGARD_GATE:
/* 2237 */         return StructureConstantsEnum.FENCE_GARDESGARD_GATE;
/*      */       case FENCE_PLAN_PALISADE:
/* 2239 */         return StructureConstantsEnum.FENCE_PALISADE;
/*      */       case FENCE_PLAN_STONEWALL:
/* 2241 */         return StructureConstantsEnum.FENCE_STONEWALL;
/*      */       case FENCE_PLAN_WOODEN_PARAPET:
/* 2243 */         return StructureConstantsEnum.FENCE_WOODEN_PARAPET;
/*      */       case FENCE_PLAN_STONE_PARAPET:
/* 2245 */         return StructureConstantsEnum.FENCE_STONE_PARAPET;
/*      */       case FENCE_PLAN_STONE_IRON_PARAPET:
/* 2247 */         return StructureConstantsEnum.FENCE_STONE_IRON_PARAPET;
/*      */       case FENCE_PLAN_PALISADE_GATE:
/* 2249 */         return StructureConstantsEnum.FENCE_PALISADE_GATE;
/*      */       case FENCE_PLAN_WOODEN_GATE:
/* 2251 */         return StructureConstantsEnum.FENCE_WOODEN_GATE;
/*      */       case FENCE_PLAN_WOODEN_GATE_CRUDE:
/* 2253 */         return StructureConstantsEnum.FENCE_WOODEN_CRUDE_GATE;
/*      */       case FENCE_PLAN_STONEWALL_HIGH:
/* 2255 */         return StructureConstantsEnum.FENCE_STONEWALL_HIGH;
/*      */       case FENCE_PLAN_IRON:
/* 2257 */         return StructureConstantsEnum.FENCE_IRON;
/*      */       case FENCE_PLAN_WOVEN:
/* 2259 */         return StructureConstantsEnum.FENCE_WOVEN;
/*      */       case FENCE_PLAN_IRON_GATE:
/* 2261 */         return StructureConstantsEnum.FENCE_IRON_GATE;
/*      */       case FENCE_PLAN_STONE:
/* 2263 */         return StructureConstantsEnum.FENCE_STONE;
/*      */       case FENCE_PLAN_CURB:
/* 2265 */         return StructureConstantsEnum.FENCE_CURB;
/*      */       case FENCE_PLAN_ROPE_LOW:
/* 2267 */         return StructureConstantsEnum.FENCE_ROPE_LOW;
/*      */       case FENCE_PLAN_ROPE_HIGH:
/* 2269 */         return StructureConstantsEnum.FENCE_ROPE_HIGH;
/*      */       case FENCE_PLAN_IRON_GATE_HIGH:
/* 2271 */         return StructureConstantsEnum.FENCE_IRON_GATE_HIGH;
/*      */       case FENCE_PLAN_IRON_HIGH:
/* 2273 */         return StructureConstantsEnum.FENCE_IRON_HIGH;
/*      */       case FENCE_PLAN_MEDIUM_CHAIN:
/* 2275 */         return StructureConstantsEnum.FENCE_MEDIUM_CHAIN;
/*      */       case FENCE_PLAN_PORTCULLIS:
/* 2277 */         return StructureConstantsEnum.FENCE_PORTCULLIS;
/*      */       case FENCE_PLAN_SLATE:
/* 2279 */         return StructureConstantsEnum.FENCE_SLATE;
/*      */       case FENCE_PLAN_ROUNDED_STONE:
/* 2281 */         return StructureConstantsEnum.FENCE_ROUNDED_STONE;
/*      */       case FENCE_PLAN_POTTERY:
/* 2283 */         return StructureConstantsEnum.FENCE_POTTERY;
/*      */       case FENCE_PLAN_SANDSTONE:
/* 2285 */         return StructureConstantsEnum.FENCE_SANDSTONE;
/*      */       case FENCE_PLAN_RENDERED:
/* 2287 */         return StructureConstantsEnum.FENCE_RENDERED;
/*      */       case FENCE_PLAN_MARBLE:
/* 2289 */         return StructureConstantsEnum.FENCE_MARBLE;
/*      */       case FENCE_PLAN_SLATE_IRON:
/* 2291 */         return StructureConstantsEnum.FENCE_SLATE_IRON;
/*      */       case FENCE_PLAN_ROUNDED_STONE_IRON:
/* 2293 */         return StructureConstantsEnum.FENCE_ROUNDED_STONE_IRON;
/*      */       case FENCE_PLAN_POTTERY_IRON:
/* 2295 */         return StructureConstantsEnum.FENCE_POTTERY_IRON;
/*      */       case FENCE_PLAN_SANDSTONE_IRON:
/* 2297 */         return StructureConstantsEnum.FENCE_SANDSTONE_IRON;
/*      */       case FENCE_PLAN_RENDERED_IRON:
/* 2299 */         return StructureConstantsEnum.FENCE_RENDERED_IRON;
/*      */       case FENCE_PLAN_MARBLE_IRON:
/* 2301 */         return StructureConstantsEnum.FENCE_MARBLE_IRON;
/*      */       case FENCE_PLAN_SLATE_IRON_GATE:
/* 2303 */         return StructureConstantsEnum.FENCE_SLATE_IRON_GATE;
/*      */       case FENCE_PLAN_ROUNDED_STONE_IRON_GATE:
/* 2305 */         return StructureConstantsEnum.FENCE_ROUNDED_STONE_IRON_GATE;
/*      */       case FENCE_PLAN_POTTERY_IRON_GATE:
/* 2307 */         return StructureConstantsEnum.FENCE_POTTERY_IRON_GATE;
/*      */       case FENCE_PLAN_SANDSTONE_IRON_GATE:
/* 2309 */         return StructureConstantsEnum.FENCE_SANDSTONE_IRON_GATE;
/*      */       case FENCE_PLAN_RENDERED_IRON_GATE:
/* 2311 */         return StructureConstantsEnum.FENCE_RENDERED_IRON_GATE;
/*      */       case FENCE_PLAN_MARBLE_IRON_GATE:
/* 2313 */         return StructureConstantsEnum.FENCE_MARBLE_IRON_GATE;
/*      */       
/*      */       case FENCE_PLAN_SLATE_TALL_STONE_WALL:
/* 2316 */         return StructureConstantsEnum.FENCE_SLATE_TALL_STONE_WALL;
/*      */       case FENCE_PLAN_SLATE_PORTCULLIS:
/* 2318 */         return StructureConstantsEnum.FENCE_SLATE_PORTCULLIS;
/*      */       case FENCE_PLAN_SLATE_HIGH_IRON_FENCE:
/* 2320 */         return StructureConstantsEnum.FENCE_SLATE_HIGH_IRON_FENCE;
/*      */       case FENCE_PLAN_SLATE_HIGH_IRON_FENCE_GATE:
/* 2322 */         return StructureConstantsEnum.FENCE_SLATE_HIGH_IRON_FENCE_GATE;
/*      */       case FENCE_PLAN_SLATE_STONE_PARAPET:
/* 2324 */         return StructureConstantsEnum.FENCE_SLATE_STONE_PARAPET;
/*      */       case FENCE_PLAN_SLATE_CHAIN_FENCE:
/* 2326 */         return StructureConstantsEnum.FENCE_SLATE_CHAIN_FENCE;
/*      */       
/*      */       case FENCE_PLAN_ROUNDED_STONE_TALL_STONE_WALL:
/* 2329 */         return StructureConstantsEnum.FENCE_ROUNDED_STONE_TALL_STONE_WALL;
/*      */       case FENCE_PLAN_ROUNDED_STONE_PORTCULLIS:
/* 2331 */         return StructureConstantsEnum.FENCE_ROUNDED_STONE_PORTCULLIS;
/*      */       case FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE:
/* 2333 */         return StructureConstantsEnum.FENCE_ROUNDED_STONE_HIGH_IRON_FENCE;
/*      */       case FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/* 2335 */         return StructureConstantsEnum.FENCE_ROUNDED_STONE_HIGH_IRON_FENCE_GATE;
/*      */       case FENCE_PLAN_ROUNDED_STONE_STONE_PARAPET:
/* 2337 */         return StructureConstantsEnum.FENCE_ROUNDED_STONE_STONE_PARAPET;
/*      */       case FENCE_PLAN_ROUNDED_STONE_CHAIN_FENCE:
/* 2339 */         return StructureConstantsEnum.FENCE_ROUNDED_STONE_CHAIN_FENCE;
/*      */       
/*      */       case FENCE_PLAN_SANDSTONE_TALL_STONE_WALL:
/* 2342 */         return StructureConstantsEnum.FENCE_SANDSTONE_TALL_STONE_WALL;
/*      */       case FENCE_PLAN_SANDSTONE_PORTCULLIS:
/* 2344 */         return StructureConstantsEnum.FENCE_SANDSTONE_PORTCULLIS;
/*      */       case FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE:
/* 2346 */         return StructureConstantsEnum.FENCE_SANDSTONE_HIGH_IRON_FENCE;
/*      */       case FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE_GATE:
/* 2348 */         return StructureConstantsEnum.FENCE_SANDSTONE_HIGH_IRON_FENCE_GATE;
/*      */       case FENCE_PLAN_SANDSTONE_STONE_PARAPET:
/* 2350 */         return StructureConstantsEnum.FENCE_SANDSTONE_STONE_PARAPET;
/*      */       case FENCE_PLAN_SANDSTONE_CHAIN_FENCE:
/* 2352 */         return StructureConstantsEnum.FENCE_SANDSTONE_CHAIN_FENCE;
/*      */       
/*      */       case FENCE_PLAN_RENDERED_TALL_STONE_WALL:
/* 2355 */         return StructureConstantsEnum.FENCE_RENDERED_TALL_STONE_WALL;
/*      */       case FENCE_PLAN_RENDERED_PORTCULLIS:
/* 2357 */         return StructureConstantsEnum.FENCE_RENDERED_PORTCULLIS;
/*      */       case FENCE_PLAN_RENDERED_HIGH_IRON_FENCE:
/* 2359 */         return StructureConstantsEnum.FENCE_RENDERED_HIGH_IRON_FENCE;
/*      */       case FENCE_PLAN_RENDERED_HIGH_IRON_FENCE_GATE:
/* 2361 */         return StructureConstantsEnum.FENCE_RENDERED_HIGH_IRON_FENCE_GATE;
/*      */       case FENCE_PLAN_RENDERED_STONE_PARAPET:
/* 2363 */         return StructureConstantsEnum.FENCE_RENDERED_STONE_PARAPET;
/*      */       case FENCE_PLAN_RENDERED_CHAIN_FENCE:
/* 2365 */         return StructureConstantsEnum.FENCE_RENDERED_CHAIN_FENCE;
/*      */       
/*      */       case FENCE_PLAN_POTTERY_TALL_STONE_WALL:
/* 2368 */         return StructureConstantsEnum.FENCE_POTTERY_TALL_STONE_WALL;
/*      */       case FENCE_PLAN_POTTERY_PORTCULLIS:
/* 2370 */         return StructureConstantsEnum.FENCE_POTTERY_PORTCULLIS;
/*      */       case FENCE_PLAN_POTTERY_HIGH_IRON_FENCE:
/* 2372 */         return StructureConstantsEnum.FENCE_POTTERY_HIGH_IRON_FENCE;
/*      */       case FENCE_PLAN_POTTERY_HIGH_IRON_FENCE_GATE:
/* 2374 */         return StructureConstantsEnum.FENCE_POTTERY_HIGH_IRON_FENCE_GATE;
/*      */       case FENCE_PLAN_POTTERY_STONE_PARAPET:
/* 2376 */         return StructureConstantsEnum.FENCE_POTTERY_STONE_PARAPET;
/*      */       case FENCE_PLAN_POTTERY_CHAIN_FENCE:
/* 2378 */         return StructureConstantsEnum.FENCE_POTTERY_CHAIN_FENCE;
/*      */       
/*      */       case FENCE_PLAN_MARBLE_TALL_STONE_WALL:
/* 2381 */         return StructureConstantsEnum.FENCE_MARBLE_TALL_STONE_WALL;
/*      */       case FENCE_PLAN_MARBLE_PORTCULLIS:
/* 2383 */         return StructureConstantsEnum.FENCE_MARBLE_PORTCULLIS;
/*      */       case FENCE_PLAN_MARBLE_HIGH_IRON_FENCE:
/* 2385 */         return StructureConstantsEnum.FENCE_MARBLE_HIGH_IRON_FENCE;
/*      */       case FENCE_PLAN_MARBLE_HIGH_IRON_FENCE_GATE:
/* 2387 */         return StructureConstantsEnum.FENCE_MARBLE_HIGH_IRON_FENCE_GATE;
/*      */       case FENCE_PLAN_MARBLE_STONE_PARAPET:
/* 2389 */         return StructureConstantsEnum.FENCE_MARBLE_STONE_PARAPET;
/*      */       case FENCE_PLAN_MARBLE_CHAIN_FENCE:
/* 2391 */         return StructureConstantsEnum.FENCE_MARBLE_CHAIN_FENCE;
/*      */     } 
/* 2393 */     logger.log(Level.WARNING, "Fence for type " + type + " is not found!", new Exception());
/*      */ 
/*      */     
/* 2396 */     return type;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int[] getItemTemplatesDealtForFence(StructureConstantsEnum aType, StructureStateEnum aState) {
/* 2402 */     if (aState.state >= (getFinishState()).state) {
/*      */       
/* 2404 */       if (aType == StructureConstantsEnum.FENCE_PALISADE || aType == StructureConstantsEnum.FENCE_PALISADE_GATE) {
/*      */         
/* 2406 */         int[] toReturn = new int[10];
/* 2407 */         for (int x = 0; x < toReturn.length; x++)
/* 2408 */           toReturn[x] = 385; 
/* 2409 */         return toReturn;
/*      */       } 
/* 2411 */       if (aType == StructureConstantsEnum.FENCE_STONEWALL) {
/*      */         
/* 2413 */         int[] toReturn = new int[10];
/* 2414 */         for (int x = 0; x < toReturn.length; x++)
/* 2415 */           toReturn[x] = 146; 
/* 2416 */         return toReturn;
/*      */       } 
/* 2418 */       if (aType == StructureConstantsEnum.FENCE_STONE_PARAPET) {
/*      */         
/* 2420 */         int[] toReturn = new int[15];
/* 2421 */         for (int x = 0; x < toReturn.length; x++)
/* 2422 */           toReturn[x] = 132; 
/* 2423 */         return toReturn;
/*      */       } 
/* 2425 */       if (aType == StructureConstantsEnum.FENCE_STONE_IRON_PARAPET) {
/*      */         
/* 2427 */         int[] toReturn = new int[15];
/* 2428 */         for (int x = 0; x < toReturn.length; x++)
/* 2429 */           toReturn[x] = 132; 
/*      */       } else {
/* 2431 */         if (aType == StructureConstantsEnum.FENCE_WOODEN_PARAPET) {
/*      */           
/* 2433 */           int[] toReturn = new int[15];
/* 2434 */           for (int x = 0; x < toReturn.length; x++)
/* 2435 */             toReturn[x] = 22; 
/* 2436 */           return toReturn;
/*      */         } 
/* 2438 */         if (aType == StructureConstantsEnum.FENCE_STONEWALL_HIGH) {
/*      */           
/* 2440 */           int[] toReturn = new int[20];
/* 2441 */           for (int x = 0; x < toReturn.length; x++)
/* 2442 */             toReturn[x] = 132; 
/* 2443 */           return toReturn;
/*      */         } 
/* 2445 */         if (aType == StructureConstantsEnum.FENCE_IRON || aType == StructureConstantsEnum.FENCE_IRON_GATE) {
/*      */           
/* 2447 */           int[] toReturn = new int[1];
/* 2448 */           for (int x = 0; x < toReturn.length; x++)
/* 2449 */             toReturn[x] = 46; 
/* 2450 */           return toReturn;
/*      */         } 
/* 2452 */         if (aType == StructureConstantsEnum.FENCE_WOVEN) {
/*      */           
/* 2454 */           int[] toReturn = new int[2];
/* 2455 */           for (int x = 0; x < toReturn.length; x++)
/* 2456 */             toReturn[x] = 169; 
/* 2457 */           return toReturn;
/*      */         } 
/* 2459 */         if (aType == StructureConstantsEnum.FENCE_RUBBLE)
/*      */         {
/* 2461 */           return new int[] { 169 };
/*      */         }
/*      */ 
/*      */         
/* 2465 */         return new int[] { 23, 22, 22 };
/*      */       } 
/*      */     } 
/* 2468 */     return EMPTY_INT_ARRAY;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int[] getItemTemplatesNeededForFenceTotal(StructureConstantsEnum fenceType) {
/* 2473 */     StructureStateEnum finishState = getFinishState(fenceType);
/* 2474 */     switch (fenceType) {
/*      */       
/*      */       case FENCE_PLAN_PALISADE:
/*      */       case FENCE_PLAN_PALISADE_GATE:
/* 2478 */         return new int[] { 385, finishState.state };
/*      */       
/*      */       case FENCE_PLAN_STONEWALL:
/* 2481 */         return new int[] { 146, finishState.state };
/*      */       
/*      */       case FENCE_PLAN_WOODEN_CRUDE:
/*      */       case FENCE_PLAN_WOODEN_GATE_CRUDE:
/* 2485 */         return new int[] { 23, 2, 22, finishState.state - 2, 218, 1 };
/*      */       
/*      */       case FENCE_PLAN_WOODEN:
/*      */       case FENCE_PLAN_WOODEN_GATE:
/* 2489 */         return new int[] { 23, 2, 22, finishState.state - 2, 218, 1 };
/*      */ 
/*      */ 
/*      */       
/*      */       case FENCE_PLAN_MEDIUM_CHAIN:
/* 2494 */         return new int[] { 132, finishState.state - 6, 859, 6 };
/*      */       
/*      */       case FENCE_PLAN_SLATE_CHAIN_FENCE:
/* 2497 */         return new int[] { 1123, finishState.state - 6, 859, 6 };
/*      */       
/*      */       case FENCE_PLAN_ROUNDED_STONE_CHAIN_FENCE:
/* 2500 */         return new int[] { 1122, finishState.state - 6, 859, 6 };
/*      */       
/*      */       case FENCE_PLAN_SANDSTONE_CHAIN_FENCE:
/* 2503 */         return new int[] { 1121, finishState.state - 6, 859, 6 };
/*      */       
/*      */       case FENCE_PLAN_POTTERY_CHAIN_FENCE:
/* 2506 */         return new int[] { 776, finishState.state - 6, 859, 6 };
/*      */       
/*      */       case FENCE_PLAN_MARBLE_CHAIN_FENCE:
/* 2509 */         return new int[] { 786, finishState.state - 6, 859, 6 };
/*      */ 
/*      */ 
/*      */       
/*      */       case FENCE_PLAN_PORTCULLIS:
/* 2514 */         return new int[] { 132, 15, 681, 2, 187, 2, 559, 1 };
/*      */       
/*      */       case FENCE_PLAN_SLATE_PORTCULLIS:
/* 2517 */         return new int[] { 1123, 15, 681, 2, 187, 2, 559, 1 };
/*      */       
/*      */       case FENCE_PLAN_ROUNDED_STONE_PORTCULLIS:
/* 2520 */         return new int[] { 1122, 15, 681, 2, 187, 2, 559, 1 };
/*      */       
/*      */       case FENCE_PLAN_SANDSTONE_PORTCULLIS:
/* 2523 */         return new int[] { 1121, 15, 681, 2, 187, 2, 559, 1 };
/*      */       
/*      */       case FENCE_PLAN_POTTERY_PORTCULLIS:
/* 2526 */         return new int[] { 776, 15, 681, 2, 187, 2, 559, 1 };
/*      */       
/*      */       case FENCE_PLAN_MARBLE_PORTCULLIS:
/* 2529 */         return new int[] { 786, 15, 681, 2, 187, 2, 559, 1 };
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case FENCE_PLAN_WOODEN_PARAPET:
/* 2535 */         return new int[] { 23, 2, 22, finishState.state - 2, 218, finishState.state - 2 };
/*      */ 
/*      */       
/*      */       case FENCE_PLAN_STONE_PARAPET:
/* 2539 */         return new int[] { 132, finishState.state };
/*      */       
/*      */       case FENCE_PLAN_SLATE_STONE_PARAPET:
/* 2542 */         return new int[] { 1123, finishState.state };
/*      */       
/*      */       case FENCE_PLAN_ROUNDED_STONE_STONE_PARAPET:
/* 2545 */         return new int[] { 1122, finishState.state };
/*      */       
/*      */       case FENCE_PLAN_SANDSTONE_STONE_PARAPET:
/* 2548 */         return new int[] { 1121, finishState.state };
/*      */       
/*      */       case FENCE_PLAN_POTTERY_STONE_PARAPET:
/* 2551 */         return new int[] { 776, finishState.state };
/*      */       
/*      */       case FENCE_PLAN_MARBLE_STONE_PARAPET:
/* 2554 */         return new int[] { 786, finishState.state };
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case FENCE_PLAN_STONE_IRON_PARAPET:
/* 2560 */         return new int[] { 132, finishState.state - 1, 681, 1 };
/*      */       
/*      */       case FENCE_PLAN_GARDESGARD_LOW:
/* 2563 */         return new int[] { 23, finishState.state, 218, 1 };
/*      */       
/*      */       case FENCE_PLAN_GARDESGARD_GATE:
/*      */       case FENCE_PLAN_GARDESGARD_HIGH:
/* 2567 */         return new int[] { 23, finishState.state, 218, 1 };
/*      */ 
/*      */ 
/*      */       
/*      */       case FENCE_PLAN_STONEWALL_HIGH:
/* 2572 */         return new int[] { 132, finishState.state };
/*      */       
/*      */       case FENCE_PLAN_SLATE_TALL_STONE_WALL:
/* 2575 */         return new int[] { 1123, finishState.state };
/*      */       
/*      */       case FENCE_PLAN_ROUNDED_STONE_TALL_STONE_WALL:
/* 2578 */         return new int[] { 1122, finishState.state };
/*      */       
/*      */       case FENCE_PLAN_SANDSTONE_TALL_STONE_WALL:
/* 2581 */         return new int[] { 1121, finishState.state };
/*      */       
/*      */       case FENCE_PLAN_POTTERY_TALL_STONE_WALL:
/* 2584 */         return new int[] { 776, finishState.state };
/*      */       
/*      */       case FENCE_PLAN_MARBLE_TALL_STONE_WALL:
/* 2587 */         return new int[] { 786, finishState.state };
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case FENCE_PLAN_IRON:
/*      */       case FENCE_PLAN_IRON_GATE:
/* 2594 */         return new int[] { 132, finishState.state - 1, 681, 1 };
/*      */       
/*      */       case FENCE_PLAN_SLATE_IRON:
/*      */       case FENCE_PLAN_SLATE_IRON_GATE:
/* 2598 */         return new int[] { 1123, finishState.state - 1, 681, 1 };
/*      */       
/*      */       case FENCE_PLAN_ROUNDED_STONE_IRON:
/*      */       case FENCE_PLAN_ROUNDED_STONE_IRON_GATE:
/* 2602 */         return new int[] { 1122, finishState.state - 1, 681, 1 };
/*      */       
/*      */       case FENCE_PLAN_POTTERY_IRON:
/*      */       case FENCE_PLAN_POTTERY_IRON_GATE:
/* 2606 */         return new int[] { 776, finishState.state - 1, 681, 1 };
/*      */       
/*      */       case FENCE_PLAN_SANDSTONE_IRON:
/*      */       case FENCE_PLAN_SANDSTONE_IRON_GATE:
/* 2610 */         return new int[] { 1121, finishState.state - 1, 681, 1 };
/*      */       
/*      */       case FENCE_PLAN_RENDERED_IRON:
/*      */       case FENCE_PLAN_RENDERED_IRON_GATE:
/* 2614 */         return new int[] { 1121, finishState.state - 11, 130, finishState.state - 1, 681, 1 };
/*      */       
/*      */       case FENCE_PLAN_MARBLE_IRON:
/*      */       case FENCE_PLAN_MARBLE_IRON_GATE:
/* 2618 */         return new int[] { 786, finishState.state - 1, 681, 1 };
/*      */ 
/*      */ 
/*      */       
/*      */       case FENCE_PLAN_IRON_HIGH:
/*      */       case FENCE_PLAN_IRON_GATE_HIGH:
/* 2624 */         return new int[] { 132, finishState.state - 2, 681, 2 };
/*      */       
/*      */       case FENCE_PLAN_SLATE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_SLATE_HIGH_IRON_FENCE:
/* 2628 */         return new int[] { 1123, finishState.state - 2, 681, 2 };
/*      */       
/*      */       case FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE:
/* 2632 */         return new int[] { 1122, finishState.state - 2, 681, 2 };
/*      */       
/*      */       case FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE:
/* 2636 */         return new int[] { 1121, finishState.state - 2, 681, 2 };
/*      */       
/*      */       case FENCE_PLAN_POTTERY_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_POTTERY_HIGH_IRON_FENCE:
/* 2640 */         return new int[] { 776, finishState.state - 2, 681, 2 };
/*      */       
/*      */       case FENCE_PLAN_MARBLE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_MARBLE_HIGH_IRON_FENCE:
/* 2644 */         return new int[] { 786, finishState.state - 2, 681, 2 };
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case FENCE_PLAN_WOVEN:
/* 2650 */         return new int[] { 169, finishState.state };
/*      */ 
/*      */       
/*      */       case FENCE_PLAN_STONE:
/* 2654 */         return new int[] { 132, finishState.state };
/*      */       
/*      */       case FENCE_PLAN_SLATE:
/* 2657 */         return new int[] { 1123, finishState.state };
/*      */       
/*      */       case FENCE_PLAN_ROUNDED_STONE:
/* 2660 */         return new int[] { 1122, finishState.state };
/*      */       
/*      */       case FENCE_PLAN_POTTERY:
/* 2663 */         return new int[] { 776, finishState.state };
/*      */       
/*      */       case FENCE_PLAN_SANDSTONE:
/* 2666 */         return new int[] { 1121, finishState.state };
/*      */       
/*      */       case FENCE_PLAN_RENDERED:
/* 2669 */         return new int[] { 132, finishState.state, 130, finishState.state };
/*      */       
/*      */       case FENCE_PLAN_MARBLE:
/* 2672 */         return new int[] { 786, finishState.state };
/*      */ 
/*      */       
/*      */       case FENCE_PLAN_CURB:
/* 2676 */         return new int[] { 146, finishState.state };
/*      */ 
/*      */       
/*      */       case FENCE_PLAN_ROPE_HIGH:
/*      */       case FENCE_PLAN_ROPE_LOW:
/* 2681 */         return new int[] { 23, 2, 319, finishState.state - 2 };
/*      */     } 
/*      */ 
/*      */     
/* 2685 */     return new int[] { -1 };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int[] getConstructionMaterialsNeededTotal(Fence fence) {
/* 2692 */     if (fence.isFinished()) {
/* 2693 */       return new int[] { -1 };
/*      */     }
/* 2695 */     StructureStateEnum maxState = fence.getFinishState();
/* 2696 */     StructureStateEnum currentState = fence.getState();
/* 2697 */     StructureConstantsEnum type = fence.getType();
/* 2698 */     int needed = maxState.state - currentState.state;
/*      */     
/* 2700 */     if (type == StructureConstantsEnum.FENCE_PLAN_PALISADE || type == StructureConstantsEnum.FENCE_PLAN_PALISADE_GATE) {
/* 2701 */       return new int[] { 385, needed };
/*      */     }
/* 2703 */     if (type == StructureConstantsEnum.FENCE_PLAN_STONEWALL) {
/* 2704 */       return new int[] { 146, needed };
/*      */     }
/* 2706 */     if (type == StructureConstantsEnum.FENCE_PLAN_WOODEN_CRUDE || type == StructureConstantsEnum.FENCE_PLAN_WOODEN_GATE_CRUDE) {
/*      */       
/* 2708 */       if (currentState == StructureStateEnum.UNINITIALIZED) {
/* 2709 */         return new int[] { 23, 1 };
/*      */       }
/* 2711 */       if (currentState == StructureStateEnum.INITIALIZED) {
/* 2712 */         return new int[] { 23, 1, 22, needed - 1, 218, 1 };
/*      */       }
/* 2714 */       if (currentState.state == StructureStateEnum.INITIALIZED.state + 1) {
/* 2715 */         return new int[] { 22, needed, 218, 1 };
/*      */       }
/*      */       
/* 2718 */       return new int[] { 22, needed };
/*      */     } 
/*      */     
/* 2721 */     if (type == StructureConstantsEnum.FENCE_PLAN_WOODEN || type == StructureConstantsEnum.FENCE_PLAN_WOODEN_GATE) {
/*      */       
/* 2723 */       if (currentState == StructureStateEnum.UNINITIALIZED) {
/* 2724 */         return new int[] { 23, 1 };
/*      */       }
/* 2726 */       if (currentState == StructureStateEnum.INITIALIZED) {
/* 2727 */         return new int[] { 23, 1, 22, needed - 1, 218, 1 };
/*      */       }
/* 2729 */       if (currentState.state == StructureStateEnum.INITIALIZED.state + 1) {
/* 2730 */         return new int[] { 22, needed, 218, 1 };
/*      */       }
/*      */       
/* 2733 */       return new int[] { 22, needed };
/*      */     } 
/*      */     
/* 2736 */     if (type == StructureConstantsEnum.FENCE_PLAN_WOODEN_PARAPET) {
/*      */       
/* 2738 */       if (currentState == StructureStateEnum.UNINITIALIZED) {
/* 2739 */         return new int[] { 23, 1 };
/*      */       }
/* 2741 */       if (currentState == StructureStateEnum.INITIALIZED) {
/* 2742 */         return new int[] { 23, 1, 22, needed - 1, 218, needed - 1 };
/*      */       }
/* 2744 */       if (currentState.state >= StructureStateEnum.INITIALIZED.state + 1) {
/* 2745 */         return new int[] { 22, needed, 218, needed };
/*      */       }
/*      */       
/* 2748 */       return new int[] { 22, needed };
/*      */     } 
/*      */ 
/*      */     
/* 2752 */     if (type == StructureConstantsEnum.FENCE_PLAN_STONE_PARAPET)
/*      */     {
/* 2754 */       return new int[] { 132, needed };
/*      */     }
/*      */     
/* 2757 */     if (type == StructureConstantsEnum.FENCE_PLAN_SLATE_STONE_PARAPET)
/*      */     {
/* 2759 */       return new int[] { 1123, needed };
/*      */     }
/*      */     
/* 2762 */     if (type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_STONE_PARAPET)
/*      */     {
/* 2764 */       return new int[] { 1122, needed };
/*      */     }
/*      */     
/* 2767 */     if (type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_STONE_PARAPET)
/*      */     {
/* 2769 */       return new int[] { 1121, needed };
/*      */     }
/*      */     
/* 2772 */     if (type == StructureConstantsEnum.FENCE_PLAN_POTTERY_STONE_PARAPET)
/*      */     {
/* 2774 */       return new int[] { 776, needed };
/*      */     }
/*      */     
/* 2777 */     if (type == StructureConstantsEnum.FENCE_PLAN_MARBLE_STONE_PARAPET)
/*      */     {
/* 2779 */       return new int[] { 786, needed };
/*      */     }
/*      */ 
/*      */     
/* 2783 */     if (type == StructureConstantsEnum.FENCE_PLAN_STONE_IRON_PARAPET) {
/*      */       
/* 2785 */       if (currentState == StructureStateEnum.UNINITIALIZED) {
/* 2786 */         return new int[] { 132, 1 };
/*      */       }
/* 2788 */       if (currentState.state < maxState.state - 1) {
/* 2789 */         return new int[] { 132, maxState.state - 1 - currentState.state, 681, 1 };
/*      */       }
/* 2791 */       if (currentState.state == maxState.state - 1) {
/* 2792 */         return new int[] { 681, 1 };
/*      */       }
/*      */       
/* 2795 */       return new int[] { 132, needed };
/*      */     } 
/*      */     
/* 2798 */     if (type == StructureConstantsEnum.FENCE_PLAN_GARDESGARD_LOW) {
/*      */       
/* 2800 */       if (currentState == StructureStateEnum.UNINITIALIZED || currentState == StructureStateEnum.INITIALIZED) {
/* 2801 */         return new int[] { 23, needed, 218, 1 };
/*      */       }
/* 2803 */       if (currentState.state >= StructureStateEnum.INITIALIZED.state + 1) {
/* 2804 */         return new int[] { 23, needed, 218, 1 };
/*      */       }
/*      */       
/* 2807 */       return new int[] { 23, needed };
/*      */     } 
/*      */     
/* 2810 */     if (type == StructureConstantsEnum.FENCE_PLAN_GARDESGARD_HIGH || type == StructureConstantsEnum.FENCE_PLAN_GARDESGARD_GATE) {
/*      */       
/* 2812 */       if (currentState == StructureStateEnum.UNINITIALIZED || currentState == StructureStateEnum.INITIALIZED) {
/* 2813 */         return new int[] { 23, needed, 218, 1 };
/*      */       }
/* 2815 */       if (currentState.state >= StructureStateEnum.INITIALIZED.state + 1) {
/* 2816 */         return new int[] { 23, needed, 218, 1 };
/*      */       }
/*      */       
/* 2819 */       return new int[] { 23, needed };
/*      */     } 
/*      */ 
/*      */     
/* 2823 */     if (type == StructureConstantsEnum.FENCE_PLAN_STONEWALL_HIGH)
/*      */     {
/* 2825 */       return new int[] { 132, needed };
/*      */     }
/*      */     
/* 2828 */     if (type == StructureConstantsEnum.FENCE_PLAN_SLATE_TALL_STONE_WALL)
/*      */     {
/* 2830 */       return new int[] { 1123, needed };
/*      */     }
/*      */     
/* 2833 */     if (type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_TALL_STONE_WALL)
/*      */     {
/* 2835 */       return new int[] { 1122, needed };
/*      */     }
/*      */     
/* 2838 */     if (type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_TALL_STONE_WALL)
/*      */     {
/* 2840 */       return new int[] { 1121, needed };
/*      */     }
/*      */     
/* 2843 */     if (type == StructureConstantsEnum.FENCE_PLAN_POTTERY_TALL_STONE_WALL)
/*      */     {
/* 2845 */       return new int[] { 776, needed };
/*      */     }
/*      */     
/* 2848 */     if (type == StructureConstantsEnum.FENCE_PLAN_MARBLE_TALL_STONE_WALL)
/*      */     {
/* 2850 */       return new int[] { 786, needed };
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 2855 */     if (type == StructureConstantsEnum.FENCE_PLAN_IRON || type == StructureConstantsEnum.FENCE_PLAN_IRON_GATE) {
/*      */       
/* 2857 */       if (currentState.state < 10) {
/* 2858 */         return new int[] { 132, 10 - currentState.state, 681, 1 };
/*      */       }
/*      */       
/* 2861 */       return new int[] { 681, 1 };
/*      */     } 
/*      */     
/* 2864 */     if (type == StructureConstantsEnum.FENCE_PLAN_SLATE_IRON || type == StructureConstantsEnum.FENCE_PLAN_SLATE_IRON_GATE) {
/*      */       
/* 2866 */       if (currentState.state < 10) {
/* 2867 */         return new int[] { 1123, 10 - currentState.state, 681, 1 };
/*      */       }
/*      */       
/* 2870 */       return new int[] { 681, 1 };
/*      */     } 
/*      */     
/* 2873 */     if (type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_IRON || type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_IRON_GATE) {
/*      */       
/* 2875 */       if (currentState.state < 10) {
/* 2876 */         return new int[] { 1122, 10 - currentState.state, 681, 1 };
/*      */       }
/*      */       
/* 2879 */       return new int[] { 681, 1 };
/*      */     } 
/*      */     
/* 2882 */     if (type == StructureConstantsEnum.FENCE_PLAN_POTTERY_IRON || type == StructureConstantsEnum.FENCE_PLAN_POTTERY_IRON_GATE) {
/*      */       
/* 2884 */       if (currentState.state < 10) {
/* 2885 */         return new int[] { 776, 10 - currentState.state, 681, 1 };
/*      */       }
/*      */       
/* 2888 */       return new int[] { 681, 1 };
/*      */     } 
/*      */     
/* 2891 */     if (type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_IRON || type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_IRON_GATE) {
/*      */       
/* 2893 */       if (currentState.state < 10) {
/* 2894 */         return new int[] { 1121, 10 - currentState.state, 681, 1 };
/*      */       }
/*      */       
/* 2897 */       return new int[] { 681, 1 };
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
/* 2912 */     if (type == StructureConstantsEnum.FENCE_PLAN_MARBLE_IRON || type == StructureConstantsEnum.FENCE_PLAN_MARBLE_IRON_GATE) {
/*      */       
/* 2914 */       if (currentState.state < 10) {
/* 2915 */         return new int[] { 786, 10 - currentState.state, 681, 1 };
/*      */       }
/*      */       
/* 2918 */       return new int[] { 681, 1 };
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2923 */     if (type == StructureConstantsEnum.FENCE_PLAN_IRON_HIGH || type == StructureConstantsEnum.FENCE_PLAN_IRON_GATE_HIGH) {
/*      */       
/* 2925 */       if (currentState.state < (fence.getFinishState()).state - 2) {
/* 2926 */         return new int[] { 132, 
/* 2927 */             (fence.getFinishState()).state - 2 - currentState.state, 681, 2 };
/*      */       }
/* 2929 */       return new int[] { 681, needed };
/*      */     } 
/*      */     
/* 2932 */     if (type == StructureConstantsEnum.FENCE_PLAN_SLATE_HIGH_IRON_FENCE || type == StructureConstantsEnum.FENCE_PLAN_SLATE_HIGH_IRON_FENCE_GATE) {
/*      */       
/* 2934 */       if (currentState.state < (fence.getFinishState()).state - 2) {
/* 2935 */         return new int[] { 1123, 
/* 2936 */             (fence.getFinishState()).state - 2 - currentState.state, 681, 2 };
/*      */       }
/* 2938 */       return new int[] { 681, needed };
/*      */     } 
/*      */     
/* 2941 */     if (type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE || type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE_GATE) {
/*      */       
/* 2943 */       if (currentState.state < (fence.getFinishState()).state - 2) {
/* 2944 */         return new int[] { 1122, 
/* 2945 */             (fence.getFinishState()).state - 2 - currentState.state, 681, 2 };
/*      */       }
/* 2947 */       return new int[] { 681, needed };
/*      */     } 
/*      */     
/* 2950 */     if (type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE || type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE_GATE) {
/*      */       
/* 2952 */       if (currentState.state < (fence.getFinishState()).state - 2) {
/* 2953 */         return new int[] { 1121, 
/* 2954 */             (fence.getFinishState()).state - 2 - currentState.state, 681, 2 };
/*      */       }
/* 2956 */       return new int[] { 681, needed };
/*      */     } 
/*      */     
/* 2959 */     if (type == StructureConstantsEnum.FENCE_PLAN_POTTERY_HIGH_IRON_FENCE || type == StructureConstantsEnum.FENCE_PLAN_POTTERY_HIGH_IRON_FENCE_GATE) {
/*      */       
/* 2961 */       if (currentState.state < (fence.getFinishState()).state - 2) {
/* 2962 */         return new int[] { 776, 
/* 2963 */             (fence.getFinishState()).state - 2 - currentState.state, 681, 2 };
/*      */       }
/* 2965 */       return new int[] { 681, needed };
/*      */     } 
/*      */     
/* 2968 */     if (type == StructureConstantsEnum.FENCE_PLAN_MARBLE_HIGH_IRON_FENCE || type == StructureConstantsEnum.FENCE_PLAN_MARBLE_HIGH_IRON_FENCE_GATE) {
/*      */       
/* 2970 */       if (currentState.state < (fence.getFinishState()).state - 2) {
/* 2971 */         return new int[] { 786, 
/* 2972 */             (fence.getFinishState()).state - 2 - currentState.state, 681, 2 };
/*      */       }
/* 2974 */       return new int[] { 681, needed };
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2979 */     if (type == StructureConstantsEnum.FENCE_PLAN_MEDIUM_CHAIN) {
/*      */       
/* 2981 */       if (currentState.state < (fence.getFinishState()).state - 6) {
/* 2982 */         return new int[] { 132, 
/*      */             
/* 2984 */             (fence.getFinishState()).state - 6 - currentState.state, 859, 6 };
/*      */       }
/* 2986 */       return new int[] { 859, needed };
/*      */     } 
/*      */ 
/*      */     
/* 2990 */     if (type == StructureConstantsEnum.FENCE_PLAN_SLATE_CHAIN_FENCE) {
/*      */       
/* 2992 */       if (currentState.state < (fence.getFinishState()).state - 6) {
/* 2993 */         return new int[] { 1123, 
/*      */             
/* 2995 */             (fence.getFinishState()).state - 6 - currentState.state, 859, 6 };
/*      */       }
/* 2997 */       return new int[] { 859, needed };
/*      */     } 
/*      */ 
/*      */     
/* 3001 */     if (type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_CHAIN_FENCE) {
/*      */       
/* 3003 */       if (currentState.state < (fence.getFinishState()).state - 6) {
/* 3004 */         return new int[] { 1122, 
/*      */             
/* 3006 */             (fence.getFinishState()).state - 6 - currentState.state, 859, 6 };
/*      */       }
/* 3008 */       return new int[] { 859, needed };
/*      */     } 
/*      */ 
/*      */     
/* 3012 */     if (type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_CHAIN_FENCE) {
/*      */       
/* 3014 */       if (currentState.state < (fence.getFinishState()).state - 6) {
/* 3015 */         return new int[] { 1121, 
/*      */             
/* 3017 */             (fence.getFinishState()).state - 6 - currentState.state, 859, 6 };
/*      */       }
/* 3019 */       return new int[] { 859, needed };
/*      */     } 
/*      */ 
/*      */     
/* 3023 */     if (type == StructureConstantsEnum.FENCE_PLAN_POTTERY_CHAIN_FENCE) {
/*      */       
/* 3025 */       if (currentState.state < (fence.getFinishState()).state - 6) {
/* 3026 */         return new int[] { 776, 
/*      */             
/* 3028 */             (fence.getFinishState()).state - 6 - currentState.state, 859, 6 };
/*      */       }
/* 3030 */       return new int[] { 859, needed };
/*      */     } 
/*      */ 
/*      */     
/* 3034 */     if (type == StructureConstantsEnum.FENCE_PLAN_MARBLE_CHAIN_FENCE) {
/*      */       
/* 3036 */       if (currentState.state < (fence.getFinishState()).state - 6) {
/* 3037 */         return new int[] { 786, 
/*      */             
/* 3039 */             (fence.getFinishState()).state - 6 - currentState.state, 859, 6 };
/*      */       }
/* 3041 */       return new int[] { 859, needed };
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3050 */     if (type == StructureConstantsEnum.FENCE_PLAN_PORTCULLIS) {
/*      */       
/* 3052 */       if (currentState.state < (fence.getFinishState()).state - 5)
/*      */       {
/* 3054 */         return new int[] { 132, needed - 5, 681, 2, 187, 2, 559, 1 };
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3062 */       if (currentState.state < (fence.getFinishState()).state - 3)
/*      */       {
/* 3064 */         return new int[] { 681, needed - 3, 187, 2, 559, 1 };
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3071 */       if (currentState.state < (fence.getFinishState()).state - 1)
/*      */       {
/* 3073 */         return new int[] { 187, needed - 1, 559, 1 };
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3081 */       return new int[] { 559, needed };
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3087 */     if (type == StructureConstantsEnum.FENCE_PLAN_SLATE_PORTCULLIS) {
/*      */       
/* 3089 */       if (currentState.state < (fence.getFinishState()).state - 5)
/*      */       {
/* 3091 */         return new int[] { 1123, needed - 5, 681, 2, 187, 2, 559, 1 };
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3099 */       if (currentState.state < (fence.getFinishState()).state - 3)
/*      */       {
/* 3101 */         return new int[] { 681, needed - 3, 187, 2, 559, 1 };
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3108 */       if (currentState.state < (fence.getFinishState()).state - 1)
/*      */       {
/* 3110 */         return new int[] { 187, needed - 1, 559, 1 };
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3118 */       return new int[] { 559, needed };
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3124 */     if (type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_PORTCULLIS) {
/*      */       
/* 3126 */       if (currentState.state < (fence.getFinishState()).state - 5)
/*      */       {
/* 3128 */         return new int[] { 1122, needed - 5, 681, 2, 187, 2, 559, 1 };
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3136 */       if (currentState.state < (fence.getFinishState()).state - 3)
/*      */       {
/* 3138 */         return new int[] { 681, needed - 3, 187, 2, 559, 1 };
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3145 */       if (currentState.state < (fence.getFinishState()).state - 1)
/*      */       {
/* 3147 */         return new int[] { 187, needed - 1, 559, 1 };
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3155 */       return new int[] { 559, needed };
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3161 */     if (type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_PORTCULLIS) {
/*      */       
/* 3163 */       if (currentState.state < (fence.getFinishState()).state - 5)
/*      */       {
/* 3165 */         return new int[] { 1121, needed - 5, 681, 2, 187, 2, 559, 1 };
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3173 */       if (currentState.state < (fence.getFinishState()).state - 3)
/*      */       {
/* 3175 */         return new int[] { 681, needed - 3, 187, 2, 559, 1 };
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3182 */       if (currentState.state < (fence.getFinishState()).state - 1)
/*      */       {
/* 3184 */         return new int[] { 187, needed - 1, 559, 1 };
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3192 */       return new int[] { 559, needed };
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3198 */     if (type == StructureConstantsEnum.FENCE_PLAN_POTTERY_PORTCULLIS) {
/*      */       
/* 3200 */       if (currentState.state < (fence.getFinishState()).state - 5)
/*      */       {
/* 3202 */         return new int[] { 776, needed - 5, 681, 2, 187, 2, 559, 1 };
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3210 */       if (currentState.state < (fence.getFinishState()).state - 3)
/*      */       {
/* 3212 */         return new int[] { 681, needed - 3, 187, 2, 559, 1 };
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3219 */       if (currentState.state < (fence.getFinishState()).state - 1)
/*      */       {
/* 3221 */         return new int[] { 187, needed - 1, 559, 1 };
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3229 */       return new int[] { 559, needed };
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3235 */     if (type == StructureConstantsEnum.FENCE_PLAN_MARBLE_PORTCULLIS) {
/*      */       
/* 3237 */       if (currentState.state < (fence.getFinishState()).state - 5)
/*      */       {
/* 3239 */         return new int[] { 786, needed - 5, 681, 2, 187, 2, 559, 1 };
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3247 */       if (currentState.state < (fence.getFinishState()).state - 3)
/*      */       {
/* 3249 */         return new int[] { 681, needed - 3, 187, 2, 559, 1 };
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3256 */       if (currentState.state < (fence.getFinishState()).state - 1)
/*      */       {
/* 3258 */         return new int[] { 187, needed - 1, 559, 1 };
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3266 */       return new int[] { 559, needed };
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
/* 3279 */     if (type == StructureConstantsEnum.FENCE_PLAN_WOVEN)
/*      */     {
/* 3281 */       return new int[] { 169, needed };
/*      */     }
/*      */     
/* 3284 */     if (type == StructureConstantsEnum.FENCE_PLAN_STONE)
/*      */     {
/* 3286 */       return new int[] { 132, needed };
/*      */     }
/*      */     
/* 3289 */     if (type == StructureConstantsEnum.FENCE_PLAN_SLATE)
/*      */     {
/* 3291 */       return new int[] { 1123, needed };
/*      */     }
/*      */     
/* 3294 */     if (type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE)
/*      */     {
/* 3296 */       return new int[] { 1122, needed };
/*      */     }
/*      */     
/* 3299 */     if (type == StructureConstantsEnum.FENCE_PLAN_POTTERY)
/*      */     {
/* 3301 */       return new int[] { 776, needed };
/*      */     }
/*      */     
/* 3304 */     if (type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE)
/*      */     {
/* 3306 */       return new int[] { 1121, needed };
/*      */     }
/*      */     
/* 3309 */     if (type == StructureConstantsEnum.FENCE_PLAN_RENDERED)
/*      */     {
/* 3311 */       return new int[] { 1122, needed, 130, needed };
/*      */     }
/*      */     
/* 3314 */     if (type == StructureConstantsEnum.FENCE_PLAN_MARBLE)
/*      */     {
/* 3316 */       return new int[] { 786, needed };
/*      */     }
/*      */     
/* 3319 */     if (type == StructureConstantsEnum.FENCE_PLAN_CURB)
/*      */     {
/* 3321 */       return new int[] { 146, needed };
/*      */     }
/*      */     
/* 3324 */     if (type == StructureConstantsEnum.FENCE_PLAN_ROPE_HIGH || type == StructureConstantsEnum.FENCE_PLAN_ROPE_LOW) {
/*      */       
/* 3326 */       if (currentState == StructureStateEnum.UNINITIALIZED) {
/* 3327 */         return new int[] { 23, 1 };
/*      */       }
/* 3329 */       if (currentState == StructureStateEnum.INITIALIZED) {
/* 3330 */         return new int[] { 23, 1, 319, needed - 1 };
/*      */       }
/*      */       
/* 3333 */       return new int[] { 319, needed };
/*      */     } 
/*      */     
/* 3336 */     return new int[] { -1 };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int[] getItemTemplatesNeededForFence(Fence fence) {
/* 3342 */     StructureConstantsEnum type = fence.getType();
/*      */     
/* 3344 */     if (fence.isFinished())
/*      */     {
/* 3346 */       return new int[] { -1 };
/*      */     }
/*      */     
/* 3349 */     if (type == StructureConstantsEnum.FENCE_PLAN_PALISADE || type == StructureConstantsEnum.FENCE_PLAN_PALISADE_GATE)
/*      */     {
/* 3351 */       return new int[] { 385 };
/*      */     }
/*      */     
/* 3354 */     if (type == StructureConstantsEnum.FENCE_PLAN_STONEWALL)
/*      */     {
/* 3356 */       return new int[] { 146 };
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 3361 */     if (type == StructureConstantsEnum.FENCE_PLAN_MEDIUM_CHAIN) {
/*      */       
/* 3363 */       StructureStateEnum state = fence.getState();
/* 3364 */       if (state.state < (fence.getFinishState()).state - 6) {
/* 3365 */         return new int[] { 132 };
/*      */       }
/*      */       
/* 3368 */       return new int[] { 859 };
/*      */     } 
/*      */     
/* 3371 */     if (type == StructureConstantsEnum.FENCE_PLAN_SLATE_CHAIN_FENCE) {
/*      */       
/* 3373 */       StructureStateEnum state = fence.getState();
/* 3374 */       if (state.state < (fence.getFinishState()).state - 6) {
/* 3375 */         return new int[] { 1123 };
/*      */       }
/*      */       
/* 3378 */       return new int[] { 859 };
/*      */     } 
/*      */     
/* 3381 */     if (type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_CHAIN_FENCE) {
/*      */       
/* 3383 */       StructureStateEnum state = fence.getState();
/* 3384 */       if (state.state < (fence.getFinishState()).state - 6) {
/* 3385 */         return new int[] { 1122 };
/*      */       }
/*      */       
/* 3388 */       return new int[] { 859 };
/*      */     } 
/*      */     
/* 3391 */     if (type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_CHAIN_FENCE) {
/*      */       
/* 3393 */       StructureStateEnum state = fence.getState();
/* 3394 */       if (state.state < (fence.getFinishState()).state - 6) {
/* 3395 */         return new int[] { 1121 };
/*      */       }
/*      */       
/* 3398 */       return new int[] { 859 };
/*      */     } 
/*      */     
/* 3401 */     if (type == StructureConstantsEnum.FENCE_PLAN_POTTERY_CHAIN_FENCE) {
/*      */       
/* 3403 */       StructureStateEnum state = fence.getState();
/* 3404 */       if (state.state < (fence.getFinishState()).state - 6) {
/* 3405 */         return new int[] { 776 };
/*      */       }
/*      */       
/* 3408 */       return new int[] { 859 };
/*      */     } 
/*      */     
/* 3411 */     if (type == StructureConstantsEnum.FENCE_PLAN_MARBLE_CHAIN_FENCE) {
/*      */       
/* 3413 */       StructureStateEnum state = fence.getState();
/* 3414 */       if (state.state < (fence.getFinishState()).state - 6) {
/* 3415 */         return new int[] { 786 };
/*      */       }
/*      */       
/* 3418 */       return new int[] { 859 };
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3425 */     if (type == StructureConstantsEnum.FENCE_PLAN_PORTCULLIS) {
/*      */       
/* 3427 */       StructureStateEnum state = fence.getState();
/* 3428 */       int brickStage = (fence.getFinishState()).state - 5;
/* 3429 */       int barStage = (fence.getFinishState()).state - 3;
/* 3430 */       int wheelStage = (fence.getFinishState()).state - 1;
/* 3431 */       if (state.state < brickStage) {
/* 3432 */         return new int[] { 132 };
/*      */       }
/* 3434 */       if (state.state < barStage) {
/* 3435 */         return new int[] { 681 };
/*      */       }
/* 3437 */       if (state.state < wheelStage) {
/* 3438 */         return new int[] { 187 };
/*      */       }
/*      */       
/* 3441 */       return new int[] { 559 };
/*      */     } 
/*      */ 
/*      */     
/* 3445 */     if (type == StructureConstantsEnum.FENCE_PLAN_SLATE_PORTCULLIS) {
/*      */       
/* 3447 */       StructureStateEnum state = fence.getState();
/* 3448 */       int brickStage = (fence.getFinishState()).state - 5;
/* 3449 */       int barStage = (fence.getFinishState()).state - 3;
/* 3450 */       int wheelStage = (fence.getFinishState()).state - 1;
/* 3451 */       if (state.state < brickStage) {
/* 3452 */         return new int[] { 1123 };
/*      */       }
/* 3454 */       if (state.state < barStage) {
/* 3455 */         return new int[] { 681 };
/*      */       }
/* 3457 */       if (state.state < wheelStage) {
/* 3458 */         return new int[] { 187 };
/*      */       }
/*      */       
/* 3461 */       return new int[] { 559 };
/*      */     } 
/*      */ 
/*      */     
/* 3465 */     if (type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_PORTCULLIS) {
/*      */       
/* 3467 */       StructureStateEnum state = fence.getState();
/* 3468 */       int brickStage = (fence.getFinishState()).state - 5;
/* 3469 */       int barStage = (fence.getFinishState()).state - 3;
/* 3470 */       int wheelStage = (fence.getFinishState()).state - 1;
/* 3471 */       if (state.state < brickStage) {
/* 3472 */         return new int[] { 1122 };
/*      */       }
/* 3474 */       if (state.state < barStage) {
/* 3475 */         return new int[] { 681 };
/*      */       }
/* 3477 */       if (state.state < wheelStage) {
/* 3478 */         return new int[] { 187 };
/*      */       }
/*      */       
/* 3481 */       return new int[] { 559 };
/*      */     } 
/*      */ 
/*      */     
/* 3485 */     if (type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_PORTCULLIS) {
/*      */       
/* 3487 */       StructureStateEnum state = fence.getState();
/* 3488 */       int brickStage = (fence.getFinishState()).state - 5;
/* 3489 */       int barStage = (fence.getFinishState()).state - 3;
/* 3490 */       int wheelStage = (fence.getFinishState()).state - 1;
/* 3491 */       if (state.state < brickStage) {
/* 3492 */         return new int[] { 1121 };
/*      */       }
/* 3494 */       if (state.state < barStage) {
/* 3495 */         return new int[] { 681 };
/*      */       }
/* 3497 */       if (state.state < wheelStage) {
/* 3498 */         return new int[] { 187 };
/*      */       }
/*      */       
/* 3501 */       return new int[] { 559 };
/*      */     } 
/*      */ 
/*      */     
/* 3505 */     if (type == StructureConstantsEnum.FENCE_PLAN_POTTERY_PORTCULLIS) {
/*      */       
/* 3507 */       StructureStateEnum state = fence.getState();
/* 3508 */       int brickStage = (fence.getFinishState()).state - 5;
/* 3509 */       int barStage = (fence.getFinishState()).state - 3;
/* 3510 */       int wheelStage = (fence.getFinishState()).state - 1;
/* 3511 */       if (state.state < brickStage) {
/* 3512 */         return new int[] { 776 };
/*      */       }
/* 3514 */       if (state.state < barStage) {
/* 3515 */         return new int[] { 681 };
/*      */       }
/* 3517 */       if (state.state < wheelStage) {
/* 3518 */         return new int[] { 187 };
/*      */       }
/*      */       
/* 3521 */       return new int[] { 559 };
/*      */     } 
/*      */ 
/*      */     
/* 3525 */     if (type == StructureConstantsEnum.FENCE_PLAN_MARBLE_PORTCULLIS) {
/*      */       
/* 3527 */       StructureStateEnum state = fence.getState();
/* 3528 */       int brickStage = (fence.getFinishState()).state - 5;
/* 3529 */       int barStage = (fence.getFinishState()).state - 3;
/* 3530 */       int wheelStage = (fence.getFinishState()).state - 1;
/* 3531 */       if (state.state < brickStage) {
/* 3532 */         return new int[] { 786 };
/*      */       }
/* 3534 */       if (state.state < barStage) {
/* 3535 */         return new int[] { 681 };
/*      */       }
/* 3537 */       if (state.state < wheelStage) {
/* 3538 */         return new int[] { 187 };
/*      */       }
/*      */       
/* 3541 */       return new int[] { 559 };
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3548 */     if (type == StructureConstantsEnum.FENCE_PLAN_WOODEN_CRUDE || type == StructureConstantsEnum.FENCE_PLAN_WOODEN_GATE_CRUDE) {
/*      */       
/* 3550 */       StructureStateEnum state = fence.getState();
/* 3551 */       if (state == StructureStateEnum.UNINITIALIZED || state == StructureStateEnum.INITIALIZED) {
/* 3552 */         return new int[] { 23 };
/*      */       }
/* 3554 */       if (state.state == StructureStateEnum.INITIALIZED.state + 1) {
/* 3555 */         return new int[] { 22, 218 };
/*      */       }
/*      */       
/* 3558 */       return new int[] { 22 };
/*      */     } 
/*      */     
/* 3561 */     if (type == StructureConstantsEnum.FENCE_PLAN_WOODEN || type == StructureConstantsEnum.FENCE_PLAN_WOODEN_GATE) {
/*      */       
/* 3563 */       StructureStateEnum state = fence.getState();
/* 3564 */       if (state == StructureStateEnum.UNINITIALIZED || state == StructureStateEnum.INITIALIZED) {
/* 3565 */         return new int[] { 23 };
/*      */       }
/* 3567 */       if (state.state == StructureStateEnum.INITIALIZED.state + 1) {
/* 3568 */         return new int[] { 22, 218 };
/*      */       }
/*      */       
/* 3571 */       return new int[] { 22 };
/*      */     } 
/*      */     
/* 3574 */     if (type == StructureConstantsEnum.FENCE_PLAN_WOODEN_PARAPET) {
/*      */       
/* 3576 */       StructureStateEnum state = fence.getState();
/* 3577 */       if (state == StructureStateEnum.UNINITIALIZED || state == StructureStateEnum.INITIALIZED) {
/* 3578 */         return new int[] { 23 };
/*      */       }
/* 3580 */       if (state.state >= StructureStateEnum.INITIALIZED.state + 1) {
/* 3581 */         return new int[] { 22, 218 };
/*      */       }
/*      */       
/* 3584 */       return new int[] { 22 };
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 3589 */     if (type == StructureConstantsEnum.FENCE_PLAN_STONE_PARAPET)
/*      */     {
/* 3591 */       return new int[] { 132 };
/*      */     }
/*      */     
/* 3594 */     if (type == StructureConstantsEnum.FENCE_PLAN_SLATE_STONE_PARAPET)
/*      */     {
/* 3596 */       return new int[] { 1123 };
/*      */     }
/*      */     
/* 3599 */     if (type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_STONE_PARAPET)
/*      */     {
/* 3601 */       return new int[] { 1122 };
/*      */     }
/* 3603 */     if (type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_STONE_PARAPET)
/*      */     {
/* 3605 */       return new int[] { 1121 };
/*      */     }
/*      */     
/* 3608 */     if (type == StructureConstantsEnum.FENCE_PLAN_POTTERY_STONE_PARAPET)
/*      */     {
/* 3610 */       return new int[] { 776 };
/*      */     }
/*      */     
/* 3613 */     if (type == StructureConstantsEnum.FENCE_PLAN_MARBLE_STONE_PARAPET)
/*      */     {
/* 3615 */       return new int[] { 786 };
/*      */     }
/*      */ 
/*      */     
/* 3619 */     if (type == StructureConstantsEnum.FENCE_PLAN_STONE_IRON_PARAPET) {
/*      */       
/* 3621 */       StructureStateEnum state = fence.getState();
/* 3622 */       if (state == StructureStateEnum.UNINITIALIZED) {
/* 3623 */         return new int[] { 132 };
/*      */       }
/* 3625 */       if (state.state < (fence.getFinishState()).state - 1) {
/* 3626 */         return new int[] { 132 };
/*      */       }
/* 3628 */       if (state.state == (fence.getFinishState()).state - 1) {
/* 3629 */         return new int[] { 681 };
/*      */       }
/*      */       
/* 3632 */       return new int[] { 132 };
/*      */     } 
/*      */     
/* 3635 */     if (type == StructureConstantsEnum.FENCE_PLAN_GARDESGARD_LOW) {
/*      */       
/* 3637 */       StructureStateEnum state = fence.getState();
/* 3638 */       if (state == StructureStateEnum.UNINITIALIZED || state == StructureStateEnum.INITIALIZED) {
/* 3639 */         return new int[] { 23 };
/*      */       }
/* 3641 */       if (state.state == StructureStateEnum.INITIALIZED.state + 1) {
/* 3642 */         return new int[] { 23, 218 };
/*      */       }
/*      */       
/* 3645 */       return new int[] { 23 };
/*      */     } 
/*      */     
/* 3648 */     if (type == StructureConstantsEnum.FENCE_PLAN_GARDESGARD_HIGH || type == StructureConstantsEnum.FENCE_PLAN_GARDESGARD_GATE) {
/*      */       
/* 3650 */       StructureStateEnum state = fence.getState();
/* 3651 */       if (state == StructureStateEnum.UNINITIALIZED || state == StructureStateEnum.INITIALIZED) {
/* 3652 */         return new int[] { 23 };
/*      */       }
/* 3654 */       if (state.state == StructureStateEnum.INITIALIZED.state + 1) {
/* 3655 */         return new int[] { 23, 218 };
/*      */       }
/*      */       
/* 3658 */       return new int[] { 23 };
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 3663 */     if (type == StructureConstantsEnum.FENCE_PLAN_STONEWALL_HIGH)
/*      */     {
/* 3665 */       return new int[] { 132 };
/*      */     }
/*      */     
/* 3668 */     if (type == StructureConstantsEnum.FENCE_PLAN_SLATE_TALL_STONE_WALL)
/*      */     {
/* 3670 */       return new int[] { 1123 };
/*      */     }
/*      */     
/* 3673 */     if (type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_TALL_STONE_WALL)
/*      */     {
/* 3675 */       return new int[] { 1122 };
/*      */     }
/*      */     
/* 3678 */     if (type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_TALL_STONE_WALL)
/*      */     {
/* 3680 */       return new int[] { 1121 };
/*      */     }
/*      */     
/* 3683 */     if (type == StructureConstantsEnum.FENCE_PLAN_POTTERY_TALL_STONE_WALL)
/*      */     {
/* 3685 */       return new int[] { 776 };
/*      */     }
/*      */     
/* 3688 */     if (type == StructureConstantsEnum.FENCE_PLAN_MARBLE_TALL_STONE_WALL)
/*      */     {
/* 3690 */       return new int[] { 786 };
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3696 */     if (type == StructureConstantsEnum.FENCE_PLAN_IRON || type == StructureConstantsEnum.FENCE_PLAN_IRON_GATE) {
/*      */       
/* 3698 */       StructureStateEnum state = fence.getState();
/* 3699 */       if (state.state < 10) {
/* 3700 */         return new int[] { 132 };
/*      */       }
/*      */       
/* 3703 */       return new int[] { 681 };
/*      */     } 
/*      */     
/* 3706 */     if (type == StructureConstantsEnum.FENCE_PLAN_SLATE_IRON || type == StructureConstantsEnum.FENCE_PLAN_SLATE_IRON_GATE) {
/*      */       
/* 3708 */       StructureStateEnum state = fence.getState();
/* 3709 */       if (state.state < 10) {
/* 3710 */         return new int[] { 1123 };
/*      */       }
/*      */       
/* 3713 */       return new int[] { 681 };
/*      */     } 
/*      */     
/* 3716 */     if (type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_IRON || type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_IRON_GATE) {
/*      */       
/* 3718 */       StructureStateEnum state = fence.getState();
/* 3719 */       if (state.state < 10) {
/* 3720 */         return new int[] { 1122 };
/*      */       }
/*      */       
/* 3723 */       return new int[] { 681 };
/*      */     } 
/*      */     
/* 3726 */     if (type == StructureConstantsEnum.FENCE_PLAN_POTTERY_IRON || type == StructureConstantsEnum.FENCE_PLAN_POTTERY_IRON_GATE) {
/*      */       
/* 3728 */       StructureStateEnum state = fence.getState();
/* 3729 */       if (state.state < 10) {
/* 3730 */         return new int[] { 776 };
/*      */       }
/*      */       
/* 3733 */       return new int[] { 681 };
/*      */     } 
/*      */     
/* 3736 */     if (type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_IRON || type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_IRON_GATE) {
/*      */       
/* 3738 */       StructureStateEnum state = fence.getState();
/* 3739 */       if (state.state < 10) {
/* 3740 */         return new int[] { 1121 };
/*      */       }
/*      */       
/* 3743 */       return new int[] { 681 };
/*      */     } 
/*      */     
/* 3746 */     if (type == StructureConstantsEnum.FENCE_PLAN_RENDERED_IRON || type == StructureConstantsEnum.FENCE_PLAN_RENDERED_IRON_GATE) {
/*      */       
/* 3748 */       StructureStateEnum state = fence.getState();
/* 3749 */       if (state.state < 10) {
/* 3750 */         return new int[] { 132 };
/*      */       }
/* 3752 */       if (state.state < 20) {
/* 3753 */         return new int[] { 130 };
/*      */       }
/*      */       
/* 3756 */       return new int[] { 681 };
/*      */     } 
/*      */     
/* 3759 */     if (type == StructureConstantsEnum.FENCE_PLAN_MARBLE_IRON || type == StructureConstantsEnum.FENCE_PLAN_MARBLE_IRON_GATE) {
/*      */       
/* 3761 */       StructureStateEnum state = fence.getState();
/* 3762 */       if (state.state < 10) {
/* 3763 */         return new int[] { 786 };
/*      */       }
/*      */       
/* 3766 */       return new int[] { 681 };
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 3771 */     if (type == StructureConstantsEnum.FENCE_PLAN_IRON_HIGH || type == StructureConstantsEnum.FENCE_PLAN_IRON_GATE_HIGH) {
/*      */       
/* 3773 */       StructureStateEnum state = fence.getState();
/* 3774 */       if (state.state < (fence.getFinishState()).state - 2) {
/* 3775 */         return new int[] { 132 };
/*      */       }
/*      */       
/* 3778 */       return new int[] { 681 };
/*      */     } 
/*      */     
/* 3781 */     if (type == StructureConstantsEnum.FENCE_PLAN_SLATE_HIGH_IRON_FENCE || type == StructureConstantsEnum.FENCE_PLAN_SLATE_HIGH_IRON_FENCE_GATE) {
/*      */       
/* 3783 */       StructureStateEnum state = fence.getState();
/* 3784 */       if (state.state < (fence.getFinishState()).state - 2) {
/* 3785 */         return new int[] { 1123 };
/*      */       }
/*      */       
/* 3788 */       return new int[] { 681 };
/*      */     } 
/*      */     
/* 3791 */     if (type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE || type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE_GATE) {
/*      */       
/* 3793 */       StructureStateEnum state = fence.getState();
/* 3794 */       if (state.state < (fence.getFinishState()).state - 2) {
/* 3795 */         return new int[] { 1122 };
/*      */       }
/*      */       
/* 3798 */       return new int[] { 681 };
/*      */     } 
/*      */     
/* 3801 */     if (type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE || type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE_GATE) {
/*      */       
/* 3803 */       StructureStateEnum state = fence.getState();
/* 3804 */       if (state.state < (fence.getFinishState()).state - 2) {
/* 3805 */         return new int[] { 1121 };
/*      */       }
/*      */       
/* 3808 */       return new int[] { 681 };
/*      */     } 
/*      */     
/* 3811 */     if (type == StructureConstantsEnum.FENCE_PLAN_POTTERY_HIGH_IRON_FENCE || type == StructureConstantsEnum.FENCE_PLAN_POTTERY_HIGH_IRON_FENCE_GATE) {
/*      */       
/* 3813 */       StructureStateEnum state = fence.getState();
/* 3814 */       if (state.state < (fence.getFinishState()).state - 2) {
/* 3815 */         return new int[] { 776 };
/*      */       }
/*      */       
/* 3818 */       return new int[] { 681 };
/*      */     } 
/*      */     
/* 3821 */     if (type == StructureConstantsEnum.FENCE_PLAN_MARBLE_HIGH_IRON_FENCE || type == StructureConstantsEnum.FENCE_PLAN_MARBLE_HIGH_IRON_FENCE_GATE) {
/*      */       
/* 3823 */       StructureStateEnum state = fence.getState();
/* 3824 */       if (state.state < (fence.getFinishState()).state - 2) {
/* 3825 */         return new int[] { 786 };
/*      */       }
/*      */       
/* 3828 */       return new int[] { 681 };
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3836 */     if (type == StructureConstantsEnum.FENCE_PLAN_WOVEN)
/*      */     {
/* 3838 */       return new int[] { 169 };
/*      */     }
/*      */     
/* 3841 */     if (type == StructureConstantsEnum.FENCE_PLAN_STONE)
/*      */     {
/* 3843 */       return new int[] { 132 };
/*      */     }
/*      */     
/* 3846 */     if (type == StructureConstantsEnum.FENCE_PLAN_SLATE)
/*      */     {
/* 3848 */       return new int[] { 1123 };
/*      */     }
/*      */     
/* 3851 */     if (type == StructureConstantsEnum.FENCE_PLAN_ROUNDED_STONE)
/*      */     {
/* 3853 */       return new int[] { 1122 };
/*      */     }
/*      */     
/* 3856 */     if (type == StructureConstantsEnum.FENCE_PLAN_POTTERY)
/*      */     {
/* 3858 */       return new int[] { 776 };
/*      */     }
/*      */     
/* 3861 */     if (type == StructureConstantsEnum.FENCE_PLAN_SANDSTONE)
/*      */     {
/* 3863 */       return new int[] { 1121 };
/*      */     }
/*      */     
/* 3866 */     if (type == StructureConstantsEnum.FENCE_PLAN_RENDERED)
/*      */     {
/* 3868 */       return new int[] { 132 };
/*      */     }
/*      */     
/* 3871 */     if (type == StructureConstantsEnum.FENCE_PLAN_MARBLE)
/*      */     {
/* 3873 */       return new int[] { 786 };
/*      */     }
/*      */     
/* 3876 */     if (type == StructureConstantsEnum.FENCE_PLAN_CURB)
/*      */     {
/* 3878 */       return new int[] { 146 };
/*      */     }
/*      */     
/* 3881 */     if (type == StructureConstantsEnum.FENCE_PLAN_ROPE_HIGH || type == StructureConstantsEnum.FENCE_PLAN_ROPE_LOW) {
/*      */       
/* 3883 */       StructureStateEnum state = fence.getState();
/* 3884 */       if (state == StructureStateEnum.UNINITIALIZED || state == StructureStateEnum.INITIALIZED) {
/* 3885 */         return new int[] { 23 };
/*      */       }
/*      */       
/* 3888 */       return new int[] { 319 };
/*      */     } 
/*      */ 
/*      */     
/* 3892 */     logger.fine("hit default return");
/* 3893 */     return new int[] { -1 };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isDoor() {
/* 3900 */     switch (this.type) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case FENCE_GARDESGARD_GATE:
/*      */       case FENCE_WOODEN_GATE:
/*      */       case FENCE_WOODEN_CRUDE_GATE:
/*      */       case FENCE_IRON_GATE:
/*      */       case FENCE_PLAN_PALISADE_GATE:
/*      */       case FENCE_PALISADE_GATE:
/*      */       case FENCE_PLAN_PORTCULLIS:
/*      */       case FENCE_SLATE_PORTCULLIS:
/*      */       case FENCE_ROUNDED_STONE_PORTCULLIS:
/*      */       case FENCE_SANDSTONE_PORTCULLIS:
/*      */       case FENCE_RENDERED_PORTCULLIS:
/*      */       case FENCE_POTTERY_PORTCULLIS:
/*      */       case FENCE_MARBLE_PORTCULLIS:
/*      */       case FENCE_PLAN_IRON_GATE_HIGH:
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PORTCULLIS:
/*      */       case FENCE_PLAN_WOODEN_GATE:
/*      */       case FENCE_PLAN_WOODEN_GATE_CRUDE:
/*      */       case FENCE_PLAN_GARDESGARD_GATE:
/*      */       case FENCE_IRON_GATE_HIGH:
/*      */       case FENCE_SLATE_IRON_GATE:
/*      */       case FENCE_ROUNDED_STONE_IRON_GATE:
/*      */       case FENCE_POTTERY_IRON_GATE:
/*      */       case FENCE_SANDSTONE_IRON_GATE:
/*      */       case FENCE_RENDERED_IRON_GATE:
/*      */       case FENCE_MARBLE_IRON_GATE:
/*      */       case FENCE_PLAN_IRON_GATE:
/* 3939 */         return true;
/*      */     } 
/* 3941 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final double getDifficulty() {
/* 3950 */     switch (this.type) {
/*      */       
/*      */       case FENCE_WOVEN:
/*      */       case FENCE_WOODEN:
/*      */       case FENCE_PLAN_WOODEN:
/*      */       case FENCE_PLAN_WOVEN:
/* 3956 */         return 1.0D;
/*      */       case FENCE_WOODEN_CRUDE_GATE:
/*      */       case FENCE_PLAN_PALISADE:
/*      */       case FENCE_PALISADE:
/*      */       case FENCE_PLAN_WOODEN_GATE_CRUDE:
/* 3961 */         return 5.0D;
/*      */       case FENCE_GARDESGARD_LOW:
/*      */       case FENCE_IRON:
/*      */       case FENCE_STONEWALL:
/*      */       case FENCE_WOODEN_GATE:
/*      */       case FENCE_IRON_GATE:
/*      */       case FENCE_STONE:
/*      */       case FENCE_PLAN_PALISADE_GATE:
/*      */       case FENCE_PALISADE_GATE:
/*      */       case FENCE_PLAN_STONEWALL:
/*      */       case FENCE_PLAN_STONE:
/*      */       case FENCE_PLAN_SLATE:
/*      */       case FENCE_SLATE:
/*      */       case FENCE_PLAN_ROUNDED_STONE:
/*      */       case FENCE_ROUNDED_STONE:
/*      */       case FENCE_PLAN_POTTERY:
/*      */       case FENCE_POTTERY:
/*      */       case FENCE_PLAN_SANDSTONE:
/*      */       case FENCE_SANDSTONE:
/*      */       case FENCE_PLAN_MARBLE:
/*      */       case FENCE_MARBLE:
/*      */       case FENCE_RENDERED:
/*      */       case FENCE_PLAN_WOODEN_GATE:
/*      */       case FENCE_PLAN_GARDESGARD_LOW:
/*      */       case FENCE_SLATE_IRON:
/*      */       case FENCE_ROUNDED_STONE_IRON:
/*      */       case FENCE_POTTERY_IRON:
/*      */       case FENCE_SANDSTONE_IRON:
/*      */       case FENCE_RENDERED_IRON:
/*      */       case FENCE_MARBLE_IRON:
/*      */       case FENCE_SLATE_IRON_GATE:
/*      */       case FENCE_ROUNDED_STONE_IRON_GATE:
/*      */       case FENCE_POTTERY_IRON_GATE:
/*      */       case FENCE_SANDSTONE_IRON_GATE:
/*      */       case FENCE_RENDERED_IRON_GATE:
/*      */       case FENCE_MARBLE_IRON_GATE:
/*      */       case FENCE_PLAN_IRON:
/*      */       case FENCE_PLAN_IRON_GATE:
/*      */       case FENCE_PLAN_RENDERED:
/* 4000 */         return 10.0D;
/*      */       case FENCE_GARDESGARD_GATE:
/*      */       case FENCE_PLAN_GARDESGARD_GATE:
/*      */       case FENCE_GARDESGARD_HIGH:
/*      */       case FENCE_PLAN_GARDESGARD_HIGH:
/* 4005 */         return 15.0D;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case FENCE_WOODEN_PARAPET:
/*      */       case FENCE_SANDSTONE_STONE_PARAPET:
/*      */       case FENCE_SLATE_STONE_PARAPET:
/*      */       case FENCE_ROUNDED_STONE_STONE_PARAPET:
/*      */       case FENCE_RENDERED_STONE_PARAPET:
/*      */       case FENCE_POTTERY_STONE_PARAPET:
/*      */       case FENCE_MARBLE_STONE_PARAPET:
/*      */       case FENCE_SLATE_CHAIN_FENCE:
/*      */       case FENCE_ROUNDED_STONE_CHAIN_FENCE:
/*      */       case FENCE_SANDSTONE_CHAIN_FENCE:
/*      */       case FENCE_RENDERED_CHAIN_FENCE:
/*      */       case FENCE_POTTERY_CHAIN_FENCE:
/*      */       case FENCE_MARBLE_CHAIN_FENCE:
/*      */       case FENCE_PLAN_STONEWALL_HIGH:
/*      */       case FENCE_STONEWALL_HIGH:
/*      */       case FENCE_SLATE_TALL_STONE_WALL:
/*      */       case FENCE_ROUNDED_STONE_TALL_STONE_WALL:
/*      */       case FENCE_SANDSTONE_TALL_STONE_WALL:
/*      */       case FENCE_RENDERED_TALL_STONE_WALL:
/*      */       case FENCE_POTTERY_TALL_STONE_WALL:
/*      */       case FENCE_MARBLE_TALL_STONE_WALL:
/*      */       case FENCE_SLATE_PORTCULLIS:
/*      */       case FENCE_ROUNDED_STONE_PORTCULLIS:
/*      */       case FENCE_SANDSTONE_PORTCULLIS:
/*      */       case FENCE_RENDERED_PORTCULLIS:
/*      */       case FENCE_POTTERY_PORTCULLIS:
/*      */       case FENCE_MARBLE_PORTCULLIS:
/*      */       case FENCE_PLAN_SLATE_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_ROUNDED_STONE_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_SANDSTONE_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_RENDERED_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_POTTERY_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_MARBLE_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_SLATE_PORTCULLIS:
/*      */       case FENCE_PLAN_ROUNDED_STONE_PORTCULLIS:
/*      */       case FENCE_PLAN_SANDSTONE_PORTCULLIS:
/*      */       case FENCE_PLAN_RENDERED_PORTCULLIS:
/*      */       case FENCE_PLAN_POTTERY_PORTCULLIS:
/*      */       case FENCE_PLAN_MARBLE_PORTCULLIS:
/*      */       case FENCE_PLAN_WOODEN_PARAPET:
/*      */       case FENCE_PLAN_SLATE_STONE_PARAPET:
/*      */       case FENCE_PLAN_ROUNDED_STONE_STONE_PARAPET:
/*      */       case FENCE_PLAN_RENDERED_STONE_PARAPET:
/*      */       case FENCE_PLAN_POTTERY_STONE_PARAPET:
/*      */       case FENCE_PLAN_MARBLE_STONE_PARAPET:
/*      */       case FENCE_PLAN_SLATE_CHAIN_FENCE:
/*      */       case FENCE_PLAN_ROUNDED_STONE_CHAIN_FENCE:
/*      */       case FENCE_PLAN_SANDSTONE_CHAIN_FENCE:
/*      */       case FENCE_PLAN_RENDERED_CHAIN_FENCE:
/*      */       case FENCE_PLAN_POTTERY_CHAIN_FENCE:
/*      */       case FENCE_PLAN_MARBLE_CHAIN_FENCE:
/*      */       case FENCE_PLAN_IRON_HIGH:
/*      */       case FENCE_PLAN_IRON_GATE_HIGH:
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE:
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE:
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE:
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE:
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE:
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_SLATE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_RENDERED_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_POTTERY_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_MARBLE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_SLATE_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_RENDERED_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_POTTERY_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_MARBLE_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_SANDSTONE_STONE_PARAPET:
/*      */       case FENCE_IRON_HIGH:
/*      */       case FENCE_IRON_GATE_HIGH:
/* 4097 */         return 20.0D;
/*      */       case FENCE_STONE_PARAPET:
/*      */       case FENCE_PLAN_STONE_PARAPET:
/* 4100 */         return 40.0D;
/*      */       case FENCE_STONE_IRON_PARAPET:
/*      */       case FENCE_PLAN_STONE_IRON_PARAPET:
/* 4103 */         return 50.0D;
/*      */     } 
/* 4105 */     if (isLowHedge())
/* 4106 */       return 10.0D; 
/* 4107 */     if (isMediumHedge())
/* 4108 */       return 20.0D; 
/* 4109 */     if (isHighHedge()) {
/* 4110 */       return 40.0D;
/*      */     }
/* 4112 */     return 1.0D;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isIron(StructureConstantsEnum fenceType) {
/* 4117 */     switch (fenceType) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case FENCE_IRON:
/*      */       case FENCE_IRON_GATE:
/*      */       case FENCE_SLATE_CHAIN_FENCE:
/*      */       case FENCE_ROUNDED_STONE_CHAIN_FENCE:
/*      */       case FENCE_SANDSTONE_CHAIN_FENCE:
/*      */       case FENCE_RENDERED_CHAIN_FENCE:
/*      */       case FENCE_POTTERY_CHAIN_FENCE:
/*      */       case FENCE_MARBLE_CHAIN_FENCE:
/*      */       case FENCE_PLAN_MEDIUM_CHAIN:
/*      */       case FENCE_PLAN_SLATE_CHAIN_FENCE:
/*      */       case FENCE_PLAN_ROUNDED_STONE_CHAIN_FENCE:
/*      */       case FENCE_PLAN_SANDSTONE_CHAIN_FENCE:
/*      */       case FENCE_PLAN_RENDERED_CHAIN_FENCE:
/*      */       case FENCE_PLAN_POTTERY_CHAIN_FENCE:
/*      */       case FENCE_PLAN_MARBLE_CHAIN_FENCE:
/*      */       case FENCE_PLAN_IRON_HIGH:
/*      */       case FENCE_PLAN_IRON_GATE_HIGH:
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE:
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE:
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE:
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE:
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE:
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_SLATE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_RENDERED_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_POTTERY_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_MARBLE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_SLATE_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_RENDERED_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_POTTERY_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_MARBLE_HIGH_IRON_FENCE:
/*      */       case FENCE_IRON_HIGH:
/*      */       case FENCE_IRON_GATE_HIGH:
/*      */       case FENCE_MEDIUM_CHAIN:
/*      */       case FENCE_SLATE_IRON:
/*      */       case FENCE_ROUNDED_STONE_IRON:
/*      */       case FENCE_POTTERY_IRON:
/*      */       case FENCE_SANDSTONE_IRON:
/*      */       case FENCE_MARBLE_IRON:
/*      */       case FENCE_SLATE_IRON_GATE:
/*      */       case FENCE_ROUNDED_STONE_IRON_GATE:
/*      */       case FENCE_POTTERY_IRON_GATE:
/*      */       case FENCE_SANDSTONE_IRON_GATE:
/*      */       case FENCE_RENDERED_IRON_GATE:
/*      */       case FENCE_MARBLE_IRON_GATE:
/*      */       case FENCE_PLAN_IRON:
/*      */       case FENCE_PLAN_IRON_GATE:
/*      */       case FENCE_PLAN_SLATE_IRON:
/*      */       case FENCE_PLAN_ROUNDED_STONE_IRON:
/*      */       case FENCE_PLAN_POTTERY_IRON:
/*      */       case FENCE_PLAN_SANDSTONE_IRON:
/*      */       case FENCE_PLAN_MARBLE_IRON:
/*      */       case FENCE_PLAN_SLATE_IRON_GATE:
/*      */       case FENCE_PLAN_ROUNDED_STONE_IRON_GATE:
/*      */       case FENCE_PLAN_POTTERY_IRON_GATE:
/*      */       case FENCE_PLAN_SANDSTONE_IRON_GATE:
/*      */       case FENCE_PLAN_MARBLE_IRON_GATE:
/* 4196 */         return true;
/*      */     } 
/* 4198 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isIron() {
/* 4204 */     return isIron(this.type);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int getSkillNumberNeededForFence(StructureConstantsEnum fenceType) {
/* 4209 */     if (isWood(fenceType)) {
/* 4210 */       return 1005;
/*      */     }
/* 4212 */     return 1013;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Skill getSkillNeededForFence(Creature performer, Fence fence) {
/* 4217 */     Skill toReturn = null;
/* 4218 */     Skills skills = performer.getSkills();
/* 4219 */     if (skills == null)
/* 4220 */       return null; 
/* 4221 */     if (fence.isWood()) {
/*      */ 
/*      */       
/*      */       try {
/* 4225 */         toReturn = performer.getSkills().getSkill(1005);
/*      */       }
/* 4227 */       catch (NoSuchSkillException nss) {
/*      */         
/* 4229 */         toReturn = skills.learn(1005, 1.0F);
/*      */       } 
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/* 4236 */         toReturn = performer.getSkills().getSkill(1013);
/*      */       }
/* 4238 */       catch (NoSuchSkillException nss) {
/*      */         
/* 4240 */         toReturn = skills.learn(1013, 1.0F);
/*      */       } 
/*      */     } 
/*      */     
/* 4244 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Fence[] getRubbleFences() {
/* 4249 */     return (Fence[])rubbleFences.values().toArray((Object[])new Fence[rubbleFences.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void destroy() {
/*      */     try {
/* 4256 */       if (this.type == StructureConstantsEnum.FENCE_RUBBLE)
/* 4257 */         rubbleFences.remove(Long.valueOf(getId())); 
/* 4258 */       Zone zone = Zones.getZone(getZoneId());
/* 4259 */       zone.removeFence(this);
/* 4260 */       delete();
/*      */     }
/* 4262 */     catch (NoSuchZoneException nsz) {
/*      */       
/* 4264 */       logger.log(Level.WARNING, "Fence in nonexistant zone? Fence: " + this.number + " - " + nsz.getMessage(), (Throwable)nsz);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getDamageModifierForItem(Item item, boolean useForFence) {
/* 4271 */     float mod = 0.0F;
/* 4272 */     if (this.type == StructureConstantsEnum.FENCE_PALISADE_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_PALISADE_GATE || this.type == StructureConstantsEnum.FENCE_PALISADE || this.type == StructureConstantsEnum.FENCE_PLAN_PALISADE) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4277 */       if (item.isWeaponAxe()) {
/* 4278 */         mod = 0.02F;
/* 4279 */       } else if (item.isWeaponCrush()) {
/* 4280 */         mod = 0.007F;
/* 4281 */       } else if (item.isWeaponSlash()) {
/* 4282 */         mod = 0.01F;
/* 4283 */       } else if (item.isWeaponPierce()) {
/* 4284 */         mod = 0.005F;
/* 4285 */       } else if (item.isWeaponMisc()) {
/* 4286 */         mod = 0.002F;
/*      */       } 
/* 4288 */     } else if (this.type == StructureConstantsEnum.FENCE_WOODEN_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_WOODEN_GATE || this.type == StructureConstantsEnum.FENCE_WOODEN_CRUDE_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_WOODEN_GATE_CRUDE || this.type == StructureConstantsEnum.FENCE_GARDESGARD_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_GARDESGARD_GATE) {
/*      */ 
/*      */ 
/*      */       
/* 4292 */       if (item.isWeaponAxe()) {
/* 4293 */         mod = 0.03F;
/* 4294 */       } else if (item.isWeaponCrush()) {
/* 4295 */         mod = 0.02F;
/* 4296 */       } else if (item.isWeaponSlash()) {
/* 4297 */         mod = 0.02F;
/* 4298 */       } else if (item.isWeaponPierce()) {
/* 4299 */         mod = 0.01F;
/* 4300 */       } else if (item.isWeaponMisc()) {
/* 4301 */         mod = 0.007F;
/*      */       } 
/* 4303 */     } else if (isMagic()) {
/*      */       
/* 4305 */       if (item.getTemplateId() == 20) {
/* 4306 */         mod = 0.07F;
/* 4307 */       } else if (item.isWeaponCrush()) {
/* 4308 */         mod = 0.03F;
/* 4309 */       } else if (item.isWeaponAxe()) {
/* 4310 */         mod = 0.015F;
/* 4311 */       } else if (item.isWeaponSlash()) {
/* 4312 */         mod = 0.01F;
/* 4313 */       } else if (item.isWeaponPierce()) {
/* 4314 */         mod = 0.01F;
/* 4315 */       } else if (item.isWeaponMisc()) {
/* 4316 */         mod = 0.01F;
/*      */       } 
/* 4318 */     } else if (isHedge() || isFlowerbed()) {
/*      */       
/* 4320 */       if (item.getTemplateId() == 7) {
/* 4321 */         mod = 0.04F;
/* 4322 */       } else if (item.isWeaponCrush()) {
/* 4323 */         mod = 0.01F;
/* 4324 */       } else if (item.isWeaponAxe()) {
/* 4325 */         mod = 0.04F;
/* 4326 */       } else if (item.isWeaponSlash()) {
/* 4327 */         mod = 0.02F;
/* 4328 */       } else if (item.isWeaponPierce()) {
/* 4329 */         mod = 0.01F;
/* 4330 */       } else if (item.isWeaponMisc()) {
/* 4331 */         mod = 0.01F;
/*      */       } 
/* 4333 */       if (mod == 0.0F) {
/* 4334 */         return mod;
/*      */       }
/* 4336 */       if (isLowHedge()) {
/* 4337 */         mod = (float)(mod + 0.01D);
/* 4338 */       } else if (isMediumHedge()) {
/* 4339 */         mod = (float)(mod + 0.005D);
/* 4340 */       } else if (isFlowerbed()) {
/* 4341 */         mod = (float)(mod + 0.01D);
/*      */       } 
/* 4343 */     } else if (isStone()) {
/*      */       
/* 4345 */       if (item.getTemplateId() == 20) {
/* 4346 */         mod = 0.02F;
/* 4347 */       } else if (item.getTemplateId() == 493) {
/* 4348 */         mod = 0.01F;
/* 4349 */       } else if (item.isWeaponCrush()) {
/* 4350 */         mod = 0.01F;
/* 4351 */       } else if (item.isWeaponAxe()) {
/* 4352 */         mod = 0.005F;
/* 4353 */       } else if (item.isWeaponSlash()) {
/* 4354 */         mod = 0.005F;
/* 4355 */       } else if (item.isWeaponPierce()) {
/* 4356 */         mod = 0.002F;
/* 4357 */       } else if (item.isWeaponMisc()) {
/* 4358 */         mod = 0.001F;
/*      */       } 
/* 4360 */       if (mod == 0.0F)
/* 4361 */         return mod; 
/* 4362 */       if (useForFence)
/*      */       {
/* 4364 */         if (this.type == StructureConstantsEnum.FENCE_CURB || this.type == StructureConstantsEnum.FENCE_PLAN_CURB) {
/* 4365 */           mod += 0.01F;
/*      */         }
/*      */       }
/* 4368 */     } else if (isIron()) {
/*      */       
/* 4370 */       if (item.isWeaponCrush()) {
/* 4371 */         mod = 0.02F;
/* 4372 */       } else if (item.isWeaponAxe()) {
/* 4373 */         mod = 0.01F;
/* 4374 */       } else if (item.isWeaponSlash()) {
/* 4375 */         mod = 0.005F;
/* 4376 */       } else if (item.isWeaponPierce()) {
/* 4377 */         mod = 0.002F;
/* 4378 */       } else if (item.isWeaponMisc()) {
/* 4379 */         mod = 0.001F;
/*      */       } 
/* 4381 */     } else if (isWoven()) {
/*      */       
/* 4383 */       if (item.isWeaponCrush()) {
/* 4384 */         mod = 0.01F;
/* 4385 */       } else if (item.isWeaponAxe()) {
/* 4386 */         mod = 0.03F;
/* 4387 */       } else if (item.isWeaponSlash()) {
/* 4388 */         mod = 0.02F;
/* 4389 */       } else if (item.isWeaponPierce()) {
/* 4390 */         mod = 0.01F;
/* 4391 */       } else if (item.isWeaponMisc()) {
/* 4392 */         mod = 0.007F;
/*      */       } 
/* 4394 */     } else if (this.type == StructureConstantsEnum.FENCE_ROPE_HIGH || this.type == StructureConstantsEnum.FENCE_PLAN_ROPE_HIGH || this.type == StructureConstantsEnum.FENCE_ROPE_LOW || this.type == StructureConstantsEnum.FENCE_PLAN_ROPE_LOW) {
/*      */ 
/*      */       
/* 4397 */       if (item.isWeaponCrush()) {
/* 4398 */         mod = 0.01F;
/* 4399 */       } else if (item.isWeaponAxe()) {
/* 4400 */         mod = 0.03F;
/* 4401 */       } else if (item.isWeaponSlash()) {
/* 4402 */         mod = 0.02F;
/* 4403 */       } else if (item.isWeaponPierce()) {
/* 4404 */         mod = 0.01F;
/* 4405 */       } else if (item.isWeaponMisc()) {
/* 4406 */         mod = 0.007F;
/*      */       }
/*      */     
/*      */     }
/* 4410 */     else if (item.isWeaponAxe()) {
/* 4411 */       mod = 0.03F;
/* 4412 */     } else if (item.isWeaponCrush()) {
/* 4413 */       mod = 0.02F;
/* 4414 */     } else if (item.isWeaponSlash()) {
/* 4415 */       mod = 0.015F;
/* 4416 */     } else if (item.isWeaponPierce()) {
/* 4417 */       mod = 0.01F;
/* 4418 */     } else if (item.isWeaponMisc()) {
/* 4419 */       mod = 0.007F;
/*      */     } 
/* 4421 */     return mod;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getRepairItemTemplate() {
/* 4426 */     int templateId = 22;
/* 4427 */     if (this.type == StructureConstantsEnum.FENCE_PLAN_STONEWALL || this.type == StructureConstantsEnum.FENCE_STONEWALL || this.type == StructureConstantsEnum.FENCE_CURB) {
/*      */       
/* 4429 */       templateId = 146;
/* 4430 */     } else if (isMagic()) {
/* 4431 */       templateId = 765;
/* 4432 */     } else if (isSlate()) {
/* 4433 */       templateId = 1123;
/* 4434 */     } else if (isRoundedStone()) {
/* 4435 */       templateId = 1122;
/* 4436 */     } else if (isPottery()) {
/* 4437 */       templateId = 776;
/* 4438 */     } else if (isSandstone()) {
/* 4439 */       templateId = 1121;
/* 4440 */     } else if (isPlastered()) {
/* 4441 */       templateId = 130;
/* 4442 */     } else if (isMarble()) {
/* 4443 */       templateId = 786;
/* 4444 */     } else if (isStone() || isIron()) {
/* 4445 */       templateId = 132;
/* 4446 */     } else if (isWoven()) {
/* 4447 */       templateId = 169;
/* 4448 */     } else if (this.type == StructureConstantsEnum.FENCE_PALISADE || this.type == StructureConstantsEnum.FENCE_PALISADE_GATE || this.type == StructureConstantsEnum.FENCE_PLAN_PALISADE || this.type == StructureConstantsEnum.FENCE_PLAN_PALISADE_GATE) {
/*      */       
/* 4450 */       templateId = 9;
/* 4451 */     } else if (isHedge() || isFlowerbed()) {
/* 4452 */       templateId = -1;
/* 4453 */     }  return templateId;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getCover() {
/* 4458 */     if (isFinished()) {
/*      */       
/* 4460 */       if (this.type == StructureConstantsEnum.FENCE_STONEWALL)
/* 4461 */         return 30; 
/* 4462 */       if (this.type == StructureConstantsEnum.FENCE_STONE_PARAPET)
/* 4463 */         return 50; 
/* 4464 */       if (this.type == StructureConstantsEnum.FENCE_STONE_IRON_PARAPET)
/* 4465 */         return 50; 
/* 4466 */       if (isStone() || this.type == StructureConstantsEnum.FENCE_PALISADE || this.type == StructureConstantsEnum.FENCE_PALISADE_GATE)
/* 4467 */         return 100; 
/* 4468 */       if (this.type == StructureConstantsEnum.FENCE_WOODEN_PARAPET)
/* 4469 */         return 50; 
/* 4470 */       if (this.type == StructureConstantsEnum.FENCE_WOVEN)
/* 4471 */         return 0; 
/* 4472 */       if (isLowHedge())
/* 4473 */         return 0; 
/* 4474 */       if (isMediumHedge())
/* 4475 */         return 30; 
/* 4476 */       if (isHighHedge())
/* 4477 */         return 100; 
/* 4478 */       if (isMagic()) {
/* 4479 */         return 100;
/*      */       }
/* 4481 */       return 20;
/*      */     } 
/* 4483 */     return Math.max(0, (getState()).state);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getBlockPercent(Creature creature) {
/* 4489 */     if (isFinished()) {
/*      */       
/* 4491 */       switch (this.type) {
/*      */         
/*      */         case FENCE_CURB:
/*      */         case FENCE_WOVEN:
/*      */         case FENCE_ROPE_LOW:
/*      */         case FENCE_RUBBLE:
/*      */         case FLOWERBED_BLUE:
/*      */         case FLOWERBED_GREENISH_YELLOW:
/*      */         case FLOWERBED_ORANGE_RED:
/*      */         case FLOWERBED_PURPLE:
/*      */         case FLOWERBED_WHITE:
/*      */         case FLOWERBED_WHITE_DOTTED:
/*      */         case FLOWERBED_YELLOW:
/*      */         case FENCE_SLATE_CHAIN_FENCE:
/*      */         case FENCE_ROUNDED_STONE_CHAIN_FENCE:
/*      */         case FENCE_SANDSTONE_CHAIN_FENCE:
/*      */         case FENCE_RENDERED_CHAIN_FENCE:
/*      */         case FENCE_POTTERY_CHAIN_FENCE:
/*      */         case FENCE_MARBLE_CHAIN_FENCE:
/*      */         case FENCE_MEDIUM_CHAIN:
/* 4511 */           return 0.0F;
/*      */ 
/*      */         
/*      */         case HEDGE_FLOWER1_LOW:
/*      */         case HEDGE_FLOWER2_LOW:
/*      */         case HEDGE_FLOWER3_LOW:
/*      */         case HEDGE_FLOWER4_LOW:
/*      */         case HEDGE_FLOWER5_LOW:
/*      */         case HEDGE_FLOWER6_LOW:
/*      */         case HEDGE_FLOWER7_LOW:
/*      */         case FENCE_GARDESGARD_LOW:
/*      */         case FENCE_IRON:
/*      */         case FENCE_STONEWALL:
/*      */         case FENCE_ROPE_HIGH:
/*      */         case FENCE_GARDESGARD_GATE:
/*      */         case FENCE_IRON_GATE:
/*      */         case FENCE_STONE:
/*      */         case HEDGE_FLOWER1_MEDIUM:
/*      */         case HEDGE_FLOWER2_MEDIUM:
/*      */         case HEDGE_FLOWER3_MEDIUM:
/*      */         case HEDGE_FLOWER4_MEDIUM:
/*      */         case HEDGE_FLOWER5_MEDIUM:
/*      */         case HEDGE_FLOWER6_MEDIUM:
/*      */         case HEDGE_FLOWER7_MEDIUM:
/*      */         case FENCE_SLATE:
/*      */         case FENCE_ROUNDED_STONE:
/*      */         case FENCE_POTTERY:
/*      */         case FENCE_SANDSTONE:
/*      */         case FENCE_MARBLE:
/*      */         case FENCE_RENDERED:
/*      */         case FENCE_SLATE_IRON:
/*      */         case FENCE_ROUNDED_STONE_IRON:
/*      */         case FENCE_POTTERY_IRON:
/*      */         case FENCE_SANDSTONE_IRON:
/*      */         case FENCE_RENDERED_IRON:
/*      */         case FENCE_MARBLE_IRON:
/*      */         case FENCE_SLATE_IRON_GATE:
/*      */         case FENCE_ROUNDED_STONE_IRON_GATE:
/*      */         case FENCE_POTTERY_IRON_GATE:
/*      */         case FENCE_SANDSTONE_IRON_GATE:
/*      */         case FENCE_RENDERED_IRON_GATE:
/*      */         case FENCE_MARBLE_IRON_GATE:
/* 4553 */           return 30.0F;
/*      */         
/*      */         case FENCE_GARDESGARD_HIGH:
/* 4556 */           return 40.0F;
/*      */         
/*      */         case FENCE_STONE_PARAPET:
/*      */         case FENCE_WOODEN_PARAPET:
/*      */         case FENCE_STONE_IRON_PARAPET:
/*      */         case FENCE_SANDSTONE_STONE_PARAPET:
/*      */         case FENCE_SLATE_STONE_PARAPET:
/*      */         case FENCE_ROUNDED_STONE_STONE_PARAPET:
/*      */         case FENCE_RENDERED_STONE_PARAPET:
/*      */         case FENCE_POTTERY_STONE_PARAPET:
/*      */         case FENCE_MARBLE_STONE_PARAPET:
/*      */         case FENCE_SLATE_PORTCULLIS:
/*      */         case FENCE_ROUNDED_STONE_PORTCULLIS:
/*      */         case FENCE_SANDSTONE_PORTCULLIS:
/*      */         case FENCE_RENDERED_PORTCULLIS:
/*      */         case FENCE_POTTERY_PORTCULLIS:
/*      */         case FENCE_MARBLE_PORTCULLIS:
/*      */         case FENCE_SLATE_HIGH_IRON_FENCE_GATE:
/*      */         case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/*      */         case FENCE_SANDSTONE_HIGH_IRON_FENCE_GATE:
/*      */         case FENCE_RENDERED_HIGH_IRON_FENCE_GATE:
/*      */         case FENCE_POTTERY_HIGH_IRON_FENCE_GATE:
/*      */         case FENCE_MARBLE_HIGH_IRON_FENCE_GATE:
/*      */         case FENCE_SLATE_HIGH_IRON_FENCE:
/*      */         case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE:
/*      */         case FENCE_SANDSTONE_HIGH_IRON_FENCE:
/*      */         case FENCE_RENDERED_HIGH_IRON_FENCE:
/*      */         case FENCE_POTTERY_HIGH_IRON_FENCE:
/*      */         case FENCE_MARBLE_HIGH_IRON_FENCE:
/*      */         case FENCE_PORTCULLIS:
/*      */         case FENCE_IRON_HIGH:
/*      */         case FENCE_IRON_GATE_HIGH:
/* 4588 */           return 75.0F;
/*      */         
/*      */         case HEDGE_FLOWER1_HIGH:
/*      */         case HEDGE_FLOWER2_HIGH:
/*      */         case HEDGE_FLOWER3_HIGH:
/*      */         case HEDGE_FLOWER4_HIGH:
/*      */         case HEDGE_FLOWER5_HIGH:
/*      */         case HEDGE_FLOWER6_HIGH:
/*      */         case HEDGE_FLOWER7_HIGH:
/*      */         case FENCE_MAGIC_STONE:
/*      */         case FENCE_MAGIC_ICE:
/*      */         case FENCE_MAGIC_FIRE:
/*      */         case FENCE_PALISADE:
/*      */         case FENCE_PALISADE_GATE:
/*      */         case FENCE_STONEWALL_HIGH:
/*      */         case FENCE_SLATE_TALL_STONE_WALL:
/*      */         case FENCE_ROUNDED_STONE_TALL_STONE_WALL:
/*      */         case FENCE_SANDSTONE_TALL_STONE_WALL:
/*      */         case FENCE_RENDERED_TALL_STONE_WALL:
/*      */         case FENCE_POTTERY_TALL_STONE_WALL:
/*      */         case FENCE_MARBLE_TALL_STONE_WALL:
/* 4609 */           return 100.0F;
/*      */       } 
/* 4611 */       return 20.0F;
/*      */     } 
/*      */ 
/*      */     
/* 4615 */     return Math.max(0, (getState()).state);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isItemRepair(Item item) {
/* 4620 */     return (item.getTemplateId() == getRepairItemTemplate());
/*      */   }
/*      */ 
/*      */   
/*      */   public final float getCurrentQualityLevel() {
/* 4625 */     return this.currentQL * Math.max(1.0F, 100.0F - this.damage) / 100.0F;
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
/*      */   public static final Fence getFence(long fenceId) {
/* 4645 */     int x = Tiles.decodeTileX(fenceId);
/* 4646 */     int y = Tiles.decodeTileY(fenceId);
/* 4647 */     int layer = Tiles.decodeLayer(fenceId);
/*      */ 
/*      */     
/* 4650 */     VolaTile tile = Zones.getTileOrNull(x, y, (layer == 0));
/* 4651 */     if (tile != null)
/*      */     {
/* 4653 */       for (Fence f : tile.getFences()) {
/*      */         
/* 4655 */         if (f != null && f.getId() == fenceId)
/* 4656 */           return f; 
/*      */       } 
/*      */     }
/* 4659 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void setZoneId(int paramInt);
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
/*      */   public abstract void improveOrigQualityLevel(float paramFloat);
/*      */ 
/*      */ 
/*      */   
/*      */   abstract boolean changeColor(int paramInt);
/*      */ 
/*      */   
/*      */   public abstract void delete();
/*      */ 
/*      */   
/*      */   public abstract void setLastUsed(long paramLong);
/*      */ 
/*      */   
/*      */   public final String toString() {
/* 4690 */     return "Fence [Tile: " + this.tilex + ", " + this.tiley + ", dir: " + this.dir + ", surfaced: " + this.surfaced + ", QL: " + this.currentQL + ", DMG: " + this.damage + ", type: " + this.type + ", state: " + this.state + ']';
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
/*      */   public final int getHeightOffset() {
/* 4702 */     return this.heightOffset;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isOnFloorLevel(int level) {
/* 4707 */     return (level == this.floorLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getFloorLevel() {
/* 4713 */     return this.floorLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final void setFloorLevel() {
/* 4719 */     this.floorLevel = this.heightOffset / 30;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte getLayer() {
/* 4729 */     return (byte)this.layer;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isOnSurface() {
/* 4734 */     return (this.layer == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isWithinFloorLevels(int maxFloorLevel, int minFloorLevel) {
/* 4740 */     return (this.floorLevel <= maxFloorLevel && this.floorLevel >= minFloorLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getStartX() {
/* 4746 */     return getTileX();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getStartY() {
/* 4752 */     return getTileY();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMinX() {
/* 4758 */     return getTileX();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMinY() {
/* 4764 */     return getTileY();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getEndX() {
/* 4770 */     if (isHorizontal())
/* 4771 */       return getStartX() + 1; 
/* 4772 */     return getStartX();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getEndY() {
/* 4778 */     if (isHorizontal())
/* 4779 */       return getStartY(); 
/* 4780 */     return getStartY() + 1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supports() {
/* 4786 */     if (isHedge() || isLowFence() || isMagic() || isFlowerbed())
/* 4787 */       return false; 
/* 4788 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFloorZ() {
/* 4794 */     return (this.heightOffset / 10);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getMinZ() {
/* 4800 */     return Math.min(Zones.getHeightForNode(getStartX(), getStartY(), getLayer()), 
/* 4801 */         Zones.getHeightForNode(getEndX(), getEndY(), getLayer())) + 
/* 4802 */       getFloorZ();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getMaxZ() {
/* 4808 */     return Math.max(Zones.getHeightForNode(getStartX(), getStartY(), getLayer()), 
/* 4809 */         Zones.getHeightForNode(getEndX(), getEndY(), getLayer())) + 
/* 4810 */       getFloorZ() + 3.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWithinZ(float maxZ, float minZ, boolean followGround) {
/* 4816 */     return ((getFloorLevel() == 0 && followGround) || (minZ <= getMaxZ() && maxZ >= getMinZ()));
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
/*      */   public final boolean supports(StructureSupport support) {
/* 4828 */     if (!supports())
/* 4829 */       return false; 
/* 4830 */     if (support.isFloor()) {
/*      */       
/* 4832 */       if (getFloorLevel() == support.getFloorLevel() || getFloorLevel() == support.getFloorLevel() - 1)
/*      */       {
/* 4834 */         if (isHorizontal())
/*      */         {
/* 4836 */           if (getStartX() == support.getStartX())
/*      */           {
/* 4838 */             if (getStartY() == support.getStartY() || getStartY() == support.getEndY()) {
/* 4839 */               return true;
/*      */             
/*      */             }
/*      */           }
/*      */         }
/* 4844 */         else if (getStartY() == support.getStartY())
/*      */         {
/* 4846 */           if (getStartX() == support.getStartX() || getStartX() == support.getEndX()) {
/* 4847 */             return true;
/*      */           }
/*      */         }
/*      */       
/*      */       }
/*      */     } else {
/*      */       
/* 4854 */       int levelMod = support.supports() ? -1 : 0;
/* 4855 */       if (support.getFloorLevel() >= getFloorLevel() + levelMod && support.getFloorLevel() <= getFloorLevel() + 1)
/*      */       {
/* 4857 */         if (support.getMinX() == getMinX() && support.getMinY() == getMinY() && 
/* 4858 */           isHorizontal() == support.isHorizontal())
/*      */         {
/* 4860 */           return true;
/*      */         }
/*      */       }
/*      */     } 
/* 4864 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSupportedByGround() {
/* 4870 */     return (getFloorLevel() == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getTempId() {
/* 4876 */     return -10L;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getTypeName() {
/* 4881 */     switch (this.type) {
/*      */       
/*      */       case FENCE_PLAN_PALISADE:
/*      */       case FENCE_PALISADE:
/* 4885 */         return "wooden palisade";
/*      */       case FENCE_PLAN_PALISADE_GATE:
/*      */       case FENCE_PALISADE_GATE:
/* 4888 */         return "wooden palisade gate";
/*      */       case FENCE_STONEWALL:
/*      */       case FENCE_PLAN_STONEWALL:
/* 4891 */         return "low stone wall";
/*      */ 
/*      */       
/*      */       case FENCE_PLAN_STONEWALL_HIGH:
/*      */       case FENCE_STONEWALL_HIGH:
/* 4896 */         return "tall stone wall";
/*      */       case FENCE_SLATE_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_SLATE_TALL_STONE_WALL:
/* 4899 */         return "tall slate wall";
/*      */       case FENCE_ROUNDED_STONE_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_ROUNDED_STONE_TALL_STONE_WALL:
/* 4902 */         return "tall rounded stone wall";
/*      */       case FENCE_SANDSTONE_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_SANDSTONE_TALL_STONE_WALL:
/* 4905 */         return "tall sandstone wall";
/*      */       case FENCE_RENDERED_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_RENDERED_TALL_STONE_WALL:
/* 4908 */         return "tall rendered wall";
/*      */       case FENCE_POTTERY_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_POTTERY_TALL_STONE_WALL:
/* 4911 */         return "tall pottery wall";
/*      */       case FENCE_MARBLE_TALL_STONE_WALL:
/*      */       case FENCE_PLAN_MARBLE_TALL_STONE_WALL:
/* 4914 */         return "tall marble wall";
/*      */ 
/*      */       
/*      */       case FENCE_STONE:
/*      */       case FENCE_PLAN_STONE:
/* 4919 */         return "stone fence";
/*      */       case FENCE_PLAN_SLATE:
/*      */       case FENCE_SLATE:
/* 4922 */         return "slate fence";
/*      */       case FENCE_PLAN_ROUNDED_STONE:
/*      */       case FENCE_ROUNDED_STONE:
/* 4925 */         return "rounded stone fence";
/*      */       case FENCE_PLAN_POTTERY:
/*      */       case FENCE_POTTERY:
/* 4928 */         return "pottery fence";
/*      */       case FENCE_PLAN_SANDSTONE:
/*      */       case FENCE_SANDSTONE:
/* 4931 */         return "sandstone fence";
/*      */       case FENCE_RENDERED:
/*      */       case FENCE_PLAN_RENDERED:
/* 4934 */         return "Rendered fence";
/*      */       case FENCE_PLAN_MARBLE:
/*      */       case FENCE_MARBLE:
/* 4937 */         return "marble fence";
/*      */       case FENCE_CURB:
/*      */       case FENCE_PLAN_CURB:
/* 4940 */         return "curb";
/*      */       case FENCE_WOODEN_GATE:
/*      */       case FENCE_PLAN_WOODEN_GATE:
/* 4943 */         return "wooden fence gate";
/*      */       case FENCE_WOODEN:
/*      */       case FENCE_PLAN_WOODEN:
/* 4946 */         return "wooden fence";
/*      */       case FENCE_WOODEN_CRUDE:
/*      */       case FENCE_PLAN_WOODEN_CRUDE:
/* 4949 */         return "crude wooden fence";
/*      */       case FENCE_WOODEN_CRUDE_GATE:
/*      */       case FENCE_PLAN_WOODEN_GATE_CRUDE:
/* 4952 */         return "crude wooden fence gate";
/*      */       case FENCE_GARDESGARD_GATE:
/*      */       case FENCE_PLAN_GARDESGARD_GATE:
/* 4955 */         return "roundpole fence gate";
/*      */       case FENCE_GARDESGARD_LOW:
/*      */       case FENCE_PLAN_GARDESGARD_LOW:
/* 4958 */         return "low roundpole fence";
/*      */       case FENCE_GARDESGARD_HIGH:
/*      */       case FENCE_PLAN_GARDESGARD_HIGH:
/* 4961 */         return "high roundpole fence";
/*      */       case FENCE_IRON:
/*      */       case FENCE_PLAN_IRON:
/* 4964 */         return "iron fence";
/*      */       case FENCE_SLATE_IRON:
/*      */       case FENCE_PLAN_SLATE_IRON:
/* 4967 */         return "slate iron fence";
/*      */       case FENCE_ROUNDED_STONE_IRON:
/*      */       case FENCE_PLAN_ROUNDED_STONE_IRON:
/* 4970 */         return "rounded stone iron fence";
/*      */       case FENCE_POTTERY_IRON:
/*      */       case FENCE_PLAN_POTTERY_IRON:
/* 4973 */         return "pottery iron fence";
/*      */       case FENCE_SANDSTONE_IRON:
/*      */       case FENCE_PLAN_SANDSTONE_IRON:
/* 4976 */         return "sandstone iron fence";
/*      */       case FENCE_RENDERED_IRON:
/*      */       case FENCE_PLAN_RENDERED_IRON:
/* 4979 */         return "plastered iron fence";
/*      */       case FENCE_MARBLE_IRON:
/*      */       case FENCE_PLAN_MARBLE_IRON:
/* 4982 */         return "marble iron fence";
/*      */ 
/*      */       
/*      */       case FENCE_PLAN_IRON_HIGH:
/*      */       case FENCE_IRON_HIGH:
/* 4987 */         return "high iron fence";
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_SLATE_HIGH_IRON_FENCE:
/* 4990 */         return "slate high iron fence";
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE:
/* 4993 */         return "rounded stone high iron fence";
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE:
/* 4996 */         return "sandstone high iron fence";
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_RENDERED_HIGH_IRON_FENCE:
/* 4999 */         return "rendered high iron fence";
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_POTTERY_HIGH_IRON_FENCE:
/* 5002 */         return "pottery high iron fence";
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE:
/*      */       case FENCE_PLAN_MARBLE_HIGH_IRON_FENCE:
/* 5005 */         return "marble high iron fence";
/*      */ 
/*      */       
/*      */       case FENCE_IRON_GATE:
/*      */       case FENCE_PLAN_IRON_GATE:
/* 5010 */         return "iron fence gate";
/*      */       case FENCE_SLATE_IRON_GATE:
/*      */       case FENCE_PLAN_SLATE_IRON_GATE:
/* 5013 */         return "slate iron gate";
/*      */       case FENCE_ROUNDED_STONE_IRON_GATE:
/*      */       case FENCE_PLAN_ROUNDED_STONE_IRON_GATE:
/* 5016 */         return "rounded stone iron gate";
/*      */       case FENCE_POTTERY_IRON_GATE:
/*      */       case FENCE_PLAN_POTTERY_IRON_GATE:
/* 5019 */         return "pottery iron gate";
/*      */       case FENCE_SANDSTONE_IRON_GATE:
/*      */       case FENCE_PLAN_SANDSTONE_IRON_GATE:
/* 5022 */         return "sandstone iron gate";
/*      */       case FENCE_RENDERED_IRON_GATE:
/*      */       case FENCE_PLAN_RENDERED_IRON_GATE:
/* 5025 */         return "plastered iron gate";
/*      */       case FENCE_MARBLE_IRON_GATE:
/*      */       case FENCE_PLAN_MARBLE_IRON_GATE:
/* 5028 */         return "marble iron gate";
/*      */ 
/*      */       
/*      */       case FENCE_PLAN_IRON_GATE_HIGH:
/*      */       case FENCE_IRON_GATE_HIGH:
/* 5033 */         return "high iron fence gate";
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_SLATE_HIGH_IRON_FENCE_GATE:
/* 5036 */         return "slate high iron fence gate";
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/* 5039 */         return "rounded stone high iron fence gate";
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE_GATE:
/* 5042 */         return "sandstone high iron fence gate";
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_RENDERED_HIGH_IRON_FENCE_GATE:
/* 5045 */         return "rendered high iron fence gate";
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_POTTERY_HIGH_IRON_FENCE_GATE:
/* 5048 */         return "pottery high iron fence gate";
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PLAN_MARBLE_HIGH_IRON_FENCE_GATE:
/* 5051 */         return "marble high iron fence gate";
/*      */       
/*      */       case FENCE_WOVEN:
/*      */       case FENCE_PLAN_WOVEN:
/* 5055 */         return "woven fence";
/*      */       case FENCE_ROPE_LOW:
/*      */       case FENCE_PLAN_ROPE_LOW:
/* 5058 */         return "low rope fence";
/*      */       case FENCE_ROPE_HIGH:
/*      */       case FENCE_PLAN_ROPE_HIGH:
/* 5061 */         return "high rope fence";
/*      */       case FENCE_WOODEN_PARAPET:
/*      */       case FENCE_PLAN_WOODEN_PARAPET:
/* 5064 */         return "wooden parapet";
/*      */ 
/*      */       
/*      */       case FENCE_STONE_PARAPET:
/*      */       case FENCE_PLAN_STONE_PARAPET:
/* 5069 */         return "stone parapet";
/*      */       case FENCE_SLATE_STONE_PARAPET:
/*      */       case FENCE_PLAN_SLATE_STONE_PARAPET:
/* 5072 */         return "slate parapet";
/*      */       case FENCE_ROUNDED_STONE_STONE_PARAPET:
/*      */       case FENCE_PLAN_ROUNDED_STONE_STONE_PARAPET:
/* 5075 */         return "rounded stone parapet";
/*      */       case FENCE_SANDSTONE_STONE_PARAPET:
/*      */       case FENCE_PLAN_SANDSTONE_STONE_PARAPET:
/* 5078 */         return "sandstone parapet";
/*      */       case FENCE_RENDERED_STONE_PARAPET:
/*      */       case FENCE_PLAN_RENDERED_STONE_PARAPET:
/* 5081 */         return "rendered parapet";
/*      */       case FENCE_POTTERY_STONE_PARAPET:
/*      */       case FENCE_PLAN_POTTERY_STONE_PARAPET:
/* 5084 */         return "pottery parapet";
/*      */       case FENCE_MARBLE_STONE_PARAPET:
/*      */       case FENCE_PLAN_MARBLE_STONE_PARAPET:
/* 5087 */         return "marble parapet";
/*      */       
/*      */       case FLOWERBED_BLUE:
/* 5090 */         return "blue flowerbed";
/*      */       case FLOWERBED_YELLOW:
/* 5092 */         return "yellow flowerbed";
/*      */       case FLOWERBED_PURPLE:
/* 5094 */         return "purple flowerbed";
/*      */       case FLOWERBED_WHITE:
/* 5096 */         return "white flowerbed";
/*      */       case FLOWERBED_WHITE_DOTTED:
/* 5098 */         return "white-dotted flowerbed";
/*      */       case FLOWERBED_GREENISH_YELLOW:
/* 5100 */         return "greenish-yellow flowerbed";
/*      */       case FLOWERBED_ORANGE_RED:
/* 5102 */         return "orange-red flowerbed";
/*      */ 
/*      */       
/*      */       case FENCE_PLAN_MEDIUM_CHAIN:
/*      */       case FENCE_MEDIUM_CHAIN:
/* 5107 */         return "chain fence";
/*      */       case FENCE_SLATE_CHAIN_FENCE:
/*      */       case FENCE_PLAN_SLATE_CHAIN_FENCE:
/* 5110 */         return "slate chain fence";
/*      */       case FENCE_ROUNDED_STONE_CHAIN_FENCE:
/*      */       case FENCE_PLAN_ROUNDED_STONE_CHAIN_FENCE:
/* 5113 */         return "rounded stone chain fence";
/*      */       case FENCE_SANDSTONE_CHAIN_FENCE:
/*      */       case FENCE_PLAN_SANDSTONE_CHAIN_FENCE:
/* 5116 */         return "sandstone chain fence";
/*      */       case FENCE_RENDERED_CHAIN_FENCE:
/*      */       case FENCE_PLAN_RENDERED_CHAIN_FENCE:
/* 5119 */         return "rendered chain fence";
/*      */       case FENCE_POTTERY_CHAIN_FENCE:
/*      */       case FENCE_PLAN_POTTERY_CHAIN_FENCE:
/* 5122 */         return "pottery chain fence";
/*      */       case FENCE_MARBLE_CHAIN_FENCE:
/*      */       case FENCE_PLAN_MARBLE_CHAIN_FENCE:
/* 5125 */         return "marble chain fence";
/*      */ 
/*      */       
/*      */       case FENCE_PLAN_PORTCULLIS:
/*      */       case FENCE_PORTCULLIS:
/* 5130 */         return "portcullis";
/*      */       case FENCE_SLATE_PORTCULLIS:
/*      */       case FENCE_PLAN_SLATE_PORTCULLIS:
/* 5133 */         return "slate portcullis";
/*      */       case FENCE_ROUNDED_STONE_PORTCULLIS:
/*      */       case FENCE_PLAN_ROUNDED_STONE_PORTCULLIS:
/* 5136 */         return "rounded stone portcullis";
/*      */       case FENCE_SANDSTONE_PORTCULLIS:
/*      */       case FENCE_PLAN_SANDSTONE_PORTCULLIS:
/* 5139 */         return "sandstone portcullis";
/*      */       case FENCE_RENDERED_PORTCULLIS:
/*      */       case FENCE_PLAN_RENDERED_PORTCULLIS:
/* 5142 */         return "rendered portcullis";
/*      */       case FENCE_POTTERY_PORTCULLIS:
/*      */       case FENCE_PLAN_POTTERY_PORTCULLIS:
/* 5145 */         return "pottery portcullis";
/*      */       case FENCE_MARBLE_PORTCULLIS:
/*      */       case FENCE_PLAN_MARBLE_PORTCULLIS:
/* 5148 */         return "marble portcullis";
/*      */       
/*      */       case FENCE_RUBBLE:
/* 5151 */         return "rubble";
/*      */       case FENCE_SIEGEWALL:
/* 5153 */         return "siege wall";
/*      */     } 
/*      */     
/* 5156 */     if (isHedge())
/* 5157 */       return "hedge"; 
/* 5158 */     return "fence";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setColor(int newcolor) {
/* 5165 */     changeColor(newcolor);
/*      */   }
/*      */ 
/*      */   
/*      */   Permissions getSettings() {
/* 5170 */     return this.permissions;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isGate() {
/* 5175 */     switch (this.type) {
/*      */       case FENCE_GARDESGARD_GATE:
/*      */       case FENCE_WOODEN_GATE:
/*      */       case FENCE_WOODEN_CRUDE_GATE:
/*      */       case FENCE_IRON_GATE:
/*      */       case FENCE_PALISADE_GATE:
/*      */       case FENCE_SLATE_PORTCULLIS:
/*      */       case FENCE_ROUNDED_STONE_PORTCULLIS:
/*      */       case FENCE_SANDSTONE_PORTCULLIS:
/*      */       case FENCE_RENDERED_PORTCULLIS:
/*      */       case FENCE_POTTERY_PORTCULLIS:
/*      */       case FENCE_MARBLE_PORTCULLIS:
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_PORTCULLIS:
/*      */       case FENCE_IRON_GATE_HIGH:
/*      */       case FENCE_SLATE_IRON_GATE:
/*      */       case FENCE_ROUNDED_STONE_IRON_GATE:
/*      */       case FENCE_POTTERY_IRON_GATE:
/*      */       case FENCE_SANDSTONE_IRON_GATE:
/*      */       case FENCE_RENDERED_IRON_GATE:
/*      */       case FENCE_MARBLE_IRON_GATE:
/* 5201 */         return true;
/*      */     } 
/* 5203 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void setSettings(int aSettings) {
/* 5209 */     this.permissions.setPermissionBits(aSettings);
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
/* 5220 */     return false;
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
/* 5231 */     return false;
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
/* 5242 */     return false;
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
/* 5253 */     return false;
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
/* 5264 */     return false;
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
/* 5275 */     return false;
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
/* 5287 */     return false;
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
/* 5298 */     return true;
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
/* 5309 */     return true;
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
/* 5320 */     return false;
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
/* 5331 */     return false;
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
/* 5342 */     return false;
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
/* 5353 */     return false;
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
/* 5364 */     return isGate();
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
/* 5375 */     return isGate();
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
/* 5386 */     return false;
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
/* 5397 */     return false;
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
/* 5408 */     return false;
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
/* 5419 */     return !isHedge();
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
/* 5430 */     return false;
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
/* 5441 */     return true;
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
/* 5452 */     return false;
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
/* 5463 */     return false;
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
/* 5474 */     return false;
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
/* 5485 */     return false;
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
/* 5496 */     return false;
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
/* 5507 */     return false;
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
/* 5519 */     return null;
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
/* 5530 */     return this.damage;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getName() {
/* 5541 */     return getTypeName();
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
/* 5552 */     return this.currentQL;
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
/* 5563 */     return this.permissions.hasPermission(Permissions.Allow.HAS_COURIER.getBit());
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
/* 5574 */     return this.permissions.hasPermission(Permissions.Allow.HAS_DARK_MESSENGER.getBit());
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
/* 5585 */     return this.permissions.hasPermission(Permissions.Allow.DECAY_DISABLED.getBit());
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
/* 5596 */     return this.permissions.hasPermission(Permissions.Allow.ALWAYS_LIT.getBit());
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
/* 5607 */     return this.permissions.hasPermission(Permissions.Allow.AUTO_FILL.getBit());
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
/* 5618 */     return this.permissions.hasPermission(Permissions.Allow.AUTO_LIGHT.getBit());
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
/* 5629 */     return this.permissions.hasPermission(Permissions.Allow.NO_BASH.getBit());
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
/* 5640 */     return this.permissions.hasPermission(Permissions.Allow.NO_DRAG.getBit());
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
/* 5651 */     return this.permissions.hasPermission(Permissions.Allow.NO_DROP.getBit());
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
/* 5662 */     return this.permissions.hasPermission(Permissions.Allow.NO_EAT_OR_DRINK.getBit());
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
/* 5673 */     return this.permissions.hasPermission(Permissions.Allow.NO_IMPROVE.getBit());
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
/* 5684 */     return this.permissions.hasPermission(Permissions.Allow.NOT_MOVEABLE.getBit());
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
/* 5695 */     return this.permissions.hasPermission(Permissions.Allow.NO_PUT.getBit());
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
/* 5706 */     return this.permissions.hasPermission(Permissions.Allow.NO_REPAIR.getBit());
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
/* 5717 */     return this.permissions.hasPermission(Permissions.Allow.NO_TAKE.getBit());
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
/* 5728 */     return this.permissions.hasPermission(Permissions.Allow.NOT_LOCKABLE.getBit());
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
/* 5739 */     return this.permissions.hasPermission(Permissions.Allow.NOT_LOCKPICKABLE.getBit());
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
/* 5750 */     return this.permissions.hasPermission(Permissions.Allow.NOT_PAINTABLE.getBit());
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
/* 5761 */     return true;
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
/* 5772 */     return this.permissions.hasPermission(Permissions.Allow.NO_SPELLS.getBit());
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
/* 5783 */     return this.permissions.hasPermission(Permissions.Allow.NOT_TURNABLE.getBit());
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
/* 5794 */     return this.permissions.hasPermission(Permissions.Allow.OWNER_MOVEABLE.getBit());
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
/* 5805 */     return this.permissions.hasPermission(Permissions.Allow.OWNER_TURNABLE.getBit());
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
/* 5816 */     return this.permissions.hasPermission(Permissions.Allow.PLANTED.getBit());
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
/* 5827 */     if (this.permissions.hasPermission(Permissions.Allow.SEALED_BY_PLAYER.getBit()))
/* 5828 */       return true; 
/* 5829 */     return false;
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
/* 5860 */     this.permissions.setPermissionBit(Permissions.Allow.HAS_COURIER.getBit(), aCourier);
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
/* 5872 */     this.permissions.setPermissionBit(Permissions.Allow.HAS_DARK_MESSENGER.getBit(), aDarkmessenger);
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
/* 5883 */     this.permissions.setPermissionBit(Permissions.Allow.DECAY_DISABLED.getBit(), aNoDecay);
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
/* 5895 */     this.permissions.setPermissionBit(Permissions.Allow.ALWAYS_LIT.getBit(), aAlwaysLit);
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
/* 5907 */     this.permissions.setPermissionBit(Permissions.Allow.AUTO_FILL.getBit(), aAutoFill);
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
/* 5919 */     this.permissions.setPermissionBit(Permissions.Allow.AUTO_LIGHT.getBit(), aAutoLight);
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
/* 5930 */     this.permissions.setPermissionBit(Permissions.Allow.NO_BASH.getBit(), aNoDestroy);
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
/* 5941 */     this.permissions.setPermissionBit(Permissions.Allow.NO_DRAG.getBit(), aNoDrag);
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
/* 5952 */     this.permissions.setPermissionBit(Permissions.Allow.NO_DROP.getBit(), aNoDrop);
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
/* 5963 */     this.permissions.setPermissionBit(Permissions.Allow.NO_EAT_OR_DRINK.getBit(), aNoEatOrDrink);
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
/* 5974 */     this.permissions.setPermissionBit(Permissions.Allow.NO_IMPROVE.getBit(), aNoImprove);
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
/* 5986 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_MOVEABLE.getBit(), aNoMove);
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
/* 5997 */     this.permissions.setPermissionBit(Permissions.Allow.NO_PUT.getBit(), aNoPut);
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
/* 6008 */     this.permissions.setPermissionBit(Permissions.Allow.NO_REPAIR.getBit(), aNoRepair);
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
/* 6020 */     this.permissions.setPermissionBit(Permissions.Allow.NO_TAKE.getBit(), aNoTake);
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
/* 6031 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_LOCKABLE.getBit(), aNoLock);
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
/* 6042 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_LOCKPICKABLE.getBit(), aNoLockpick);
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
/* 6053 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_PAINTABLE.getBit(), aNoPaint);
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
/* 6064 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_RUNEABLE.getBit(), aNoRune);
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
/* 6076 */     this.permissions.setPermissionBit(Permissions.Allow.NO_SPELLS.getBit(), aNoSpells);
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
/* 6087 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_TURNABLE.getBit(), aNoTurn);
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
/* 6098 */     this.permissions.setPermissionBit(Permissions.Allow.OWNER_MOVEABLE.getBit(), aOwnerMove);
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
/* 6109 */     this.permissions.setPermissionBit(Permissions.Allow.OWNER_TURNABLE.getBit(), aOwnerTurn);
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
/* 6121 */     this.permissions.setPermissionBit(Permissions.Allow.PLANTED.getBit(), aPlant);
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
/* 6133 */     this.permissions.setPermissionBit(Permissions.Allow.SEALED_BY_PLAYER.getBit(), aSealed);
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
/* 6173 */     return ((getStartX() == pos.x || getEndX() == pos.x) && getEndY() == pos.y + 1 && getStartY() == pos.y + 1);
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
/*      */   public final boolean isOnNorthBorder(TilePos pos) {
/* 6186 */     return ((getStartX() == pos.x || getEndX() == pos.x) && getEndY() == pos.y && getStartY() == pos.y);
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
/*      */   public final boolean isOnWestBorder(TilePos pos) {
/* 6199 */     return (getStartX() == pos.x && getEndX() == pos.x && (getEndY() == pos.y || getStartY() == pos.y));
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
/*      */   public final boolean isOnEastBorder(TilePos pos) {
/* 6212 */     return (getStartX() == pos.x + 1 && getEndX() == pos.x + 1 && (getEndY() == pos.y || getStartY() == pos.y));
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\Fence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */