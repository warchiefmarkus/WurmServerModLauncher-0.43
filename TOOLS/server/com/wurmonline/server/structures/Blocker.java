package com.wurmonline.server.structures;

import com.wurmonline.math.TilePos;
import com.wurmonline.math.Vector3f;
import com.wurmonline.server.creatures.Creature;

public interface Blocker {
  public static final int TYPE_NONE = 0;
  
  public static final int TYPE_FENCE = 1;
  
  public static final int TYPE_WALL = 2;
  
  public static final int TYPE_FLOOR = 3;
  
  public static final int TYPE_ALL = 4;
  
  public static final int TYPE_ALL_BUT_OPEN = 5;
  
  public static final int TYPE_MOVEMENT = 6;
  
  public static final int TYPE_TARGET_TILE = 7;
  
  public static final int TYPE_NOT_DOOR = 8;
  
  boolean isFence();
  
  boolean isStone();
  
  boolean isWood();
  
  boolean isMetal();
  
  boolean isWall();
  
  boolean isDoor();
  
  boolean isFloor();
  
  boolean isRoof();
  
  boolean isTile();
  
  boolean isStair();
  
  boolean canBeOpenedBy(Creature paramCreature, boolean paramBoolean);
  
  int getFloorLevel();
  
  String getName();
  
  Vector3f getNormal();
  
  Vector3f getCenterPoint();
  
  int getTileX();
  
  int getTileY();
  
  boolean isOnSurface();
  
  float getPositionX();
  
  float getPositionY();
  
  boolean isHorizontal();
  
  boolean isWithinFloorLevels(int paramInt1, int paramInt2);
  
  Vector3f isBlocking(Creature paramCreature, Vector3f paramVector3f1, Vector3f paramVector3f2, Vector3f paramVector3f3, int paramInt, long paramLong, boolean paramBoolean);
  
  float getBlockPercent(Creature paramCreature);
  
  float getDamageModifier();
  
  boolean setDamage(float paramFloat);
  
  float getDamage();
  
  long getId();
  
  long getTempId();
  
  float getMinZ();
  
  float getMaxZ();
  
  float getFloorZ();
  
  boolean isWithinZ(float paramFloat1, float paramFloat2, boolean paramBoolean);
  
  boolean isOnSouthBorder(TilePos paramTilePos);
  
  boolean isOnNorthBorder(TilePos paramTilePos);
  
  boolean isOnWestBorder(TilePos paramTilePos);
  
  boolean isOnEastBorder(TilePos paramTilePos);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\Blocker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */