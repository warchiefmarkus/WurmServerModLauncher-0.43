/*     */ package com.wurmonline.server.creatures.ai;
/*     */ 
/*     */ import com.wurmonline.math.TilePos;
/*     */ import com.wurmonline.math.Vector3f;
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.structures.Blocker;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathTile
/*     */   implements Blocker, MiscConstants
/*     */ {
/*     */   protected int tilex;
/*     */   protected int tiley;
/*     */   private float posX;
/*     */   private float posY;
/*     */   private final int tile;
/*     */   private int floorLevel;
/*     */   private boolean used = false;
/*     */   private final boolean surfaced;
/*     */   private boolean door = false;
/*     */   private final float moveCost;
/*  61 */   private float distFromStart = Float.MAX_VALUE;
/*     */   
/*     */   private PathTile linkedFrom;
/*  64 */   private static final Vector3f normal = new Vector3f(0.0F, 0.0F, 1.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathTile(int tx, int ty, int t, boolean surf, int aFloorLevel) {
/*  80 */     this(tx, ty, t, surf, PathFinder.getCost(t), aFloorLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public PathTile(float posX, float posY, int tile, boolean surface, int floorLevel) {
/*  85 */     this((int)posX >> 2, (int)posY >> 2, tile, surface, PathFinder.getCost(tile), floorLevel, posX, posY);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PathTile(int aTileX, int aTileY, int aTile, boolean aSurface, float aMoveCost, int aFloorLevel) {
/*  91 */     this(aTileX, aTileY, aTile, aSurface, aMoveCost, aFloorLevel, -1.0F, -1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PathTile(int aTileX, int aTileY, int aTile, boolean aSurface, float aMoveCost, int aFloorLevel, float posX, float posY) {
/* 112 */     this.tilex = aTileX;
/* 113 */     this.tiley = aTileY;
/* 114 */     this.tile = aTile;
/* 115 */     this.surfaced = aSurface;
/* 116 */     this.moveCost = aMoveCost;
/* 117 */     this.floorLevel = aFloorLevel;
/* 118 */     this.posX = posX;
/* 119 */     this.posY = posY;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setFloorLevel(int aFloorLevel) {
/* 124 */     this.floorLevel = aFloorLevel;
/*     */   }
/*     */   
/*     */   float getDistanceFromStart() {
/* 128 */     return this.distFromStart;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   PathTile getLink() {
/* 134 */     return this.linkedFrom;
/*     */   }
/*     */ 
/*     */   
/*     */   void setDistanceFromStart(PathTile link, float val) {
/* 139 */     if (val < this.distFromStart) {
/*     */       
/* 141 */       this.linkedFrom = link;
/* 142 */       this.distFromStart = val;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getMoveCost() {
/* 152 */     return this.moveCost;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTileX() {
/* 158 */     return this.tilex;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTileY() {
/* 164 */     return this.tiley;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPosX() {
/* 169 */     return this.posX;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPosY() {
/* 174 */     return this.posY;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSpecificPos() {
/* 179 */     return (this.posX > 0.0F && this.posY > 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSpecificPos(float posX, float posY) {
/* 184 */     this.posX = posX;
/* 185 */     this.posY = posY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSurfaced() {
/* 194 */     return this.surfaced;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOnSurface() {
/* 200 */     return this.surfaced;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTile() {
/* 209 */     return this.tile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 223 */     int prime = 31;
/* 224 */     int result = 1;
/* 225 */     result = 31 * result + (this.surfaced ? 1231 : 1237);
/* 226 */     result = 31 * result + this.tilex;
/* 227 */     result = 31 * result + this.tiley;
/* 228 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 242 */     if (this == obj)
/* 243 */       return true; 
/* 244 */     if (obj == null)
/* 245 */       return false; 
/* 246 */     if (!(obj instanceof PathTile))
/* 247 */       return false; 
/* 248 */     PathTile other = (PathTile)obj;
/* 249 */     if (this.surfaced != other.surfaced)
/* 250 */       return false; 
/* 251 */     if (this.tilex != other.tilex)
/* 252 */       return false; 
/* 253 */     if (this.tiley != other.tiley)
/* 254 */       return false; 
/* 255 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 261 */     return "x=" + this.tilex + ", y=" + this.tiley + ", surf=" + this.surfaced + " fl=" + this.floorLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDoor() {
/* 271 */     return this.door;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setDoor(boolean isdoor) {
/* 280 */     this.door = isdoor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isUsed() {
/* 290 */     return this.used;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isNotUsed() {
/* 300 */     return !this.used;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setUsed() {
/* 308 */     this.used = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFloorLevel() {
/* 314 */     return this.floorLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 320 */     return Tiles.getTile(Tiles.decodeType(this.tile)).getName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final float getPositionX() {
/* 326 */     if (hasSpecificPos()) {
/* 327 */       return getPosX();
/*     */     }
/* 329 */     return (2 + this.tilex * 4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final float getPositionY() {
/* 335 */     if (hasSpecificPos()) {
/* 336 */       return getPosY();
/*     */     }
/* 338 */     return (2 + this.tiley * 4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isWithinFloorLevels(int maxFloorLevel, int minFloorLevel) {
/* 344 */     int lFloorLevel = this.surfaced ? 0 : -1;
/* 345 */     return (lFloorLevel >= minFloorLevel && lFloorLevel <= maxFloorLevel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Vector3f isBlocking(Creature creature, Vector3f startPos, Vector3f endPos, Vector3f aNormal, int blockType, long target, boolean followGround) {
/* 353 */     if (this.surfaced)
/* 354 */       return null; 
/* 355 */     if (Tiles.isSolidCave(Tiles.decodeType(this.tile)))
/*     */     {
/* 357 */       return new Vector3f(getPositionX(), getPositionY(), Tiles.decodeHeightAsFloat(this.tile));
/*     */     }
/* 359 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final float getBlockPercent(Creature creature) {
/* 365 */     if (this.surfaced)
/* 366 */       return 0.0F; 
/* 367 */     if (Tiles.isSolidCave(Tiles.decodeType(this.tile)))
/*     */     {
/* 369 */       return 100.0F;
/*     */     }
/* 371 */     return 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFence() {
/* 382 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWall() {
/* 393 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFloor() {
/* 404 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRoof() {
/* 410 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3f getNormal() {
/* 420 */     return normal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3f getCenterPoint() {
/* 431 */     return new Vector3f(getPositionX(), getPositionY(), Tiles.decodeHeightAsFloat(this.tile));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isHorizontal() {
/* 442 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean canBeOpenedBy(Creature creature, boolean wentThroughDoor) {
/* 448 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isStone() {
/* 454 */     return (Tiles.isSolidCave(Tiles.decodeType(this.tile)) || Tiles.decodeType(this.tile) == Tiles.Tile.TILE_ROCK.id || 
/* 455 */       Tiles.decodeType(this.tile) == Tiles.Tile.TILE_STONE_SLABS.id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isWood() {
/* 461 */     return Tiles.isTree(Tiles.decodeType(this.tile));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isMetal() {
/* 467 */     return Tiles.isOreCave(Tiles.decodeType(this.tile));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isTile() {
/* 473 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getFloorZ() {
/* 479 */     return (this.floorLevel * 3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getMinZ() {
/* 485 */     return Zones.getHeightForNode(this.tilex, this.tiley, isOnSurface() ? 0 : -1) + getFloorZ();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getMaxZ() {
/* 496 */     return getMinZ() + 4.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWithinZ(float maxZ, float minZ, boolean followGround) {
/* 502 */     return ((getFloorLevel() <= 0 && followGround) || (minZ <= getMaxZ() && maxZ >= getMinZ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean setDamage(float newDamage) {
/* 513 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final float getDamage() {
/* 519 */     return 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final float getDamageModifier() {
/* 525 */     return 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getId() {
/* 531 */     return Tiles.getTileId(getTileX(), getTileY(), getFloorLevel() * 30);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTempId() {
/* 537 */     return -10L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isStair() {
/* 543 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isOnSouthBorder(TilePos pos) {
/* 555 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isOnNorthBorder(TilePos pos) {
/* 567 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isOnWestBorder(TilePos pos) {
/* 579 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isOnEastBorder(TilePos pos) {
/* 591 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\PathTile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */