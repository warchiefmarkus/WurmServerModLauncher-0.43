package com.wurmonline.server.structures;

import com.wurmonline.server.items.Item;
import com.wurmonline.server.zones.VolaTile;

public interface IFloor extends StructureSupport {
  float getDamageModifierForItem(Item paramItem);
  
  long getStructureId();
  
  VolaTile getTile();
  
  boolean isOnPvPServer();
  
  int getTileX();
  
  int getTileY();
  
  float getCurrentQualityLevel();
  
  float getDamage();
  
  boolean setDamage(float paramFloat);
  
  float getQualityLevel();
  
  void destroyOrRevertToPlan();
  
  boolean isAPlan();
  
  boolean isThatch();
  
  boolean isStone();
  
  boolean isSandstone();
  
  boolean isSlate();
  
  boolean isMarble();
  
  boolean isMetal();
  
  boolean isWood();
  
  boolean isFinished();
  
  int getRepairItemTemplate();
  
  boolean setQualityLevel(float paramFloat);
  
  void setLastUsed(long paramLong);
  
  boolean isOnSurface();
  
  boolean equals(StructureSupport paramStructureSupport);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\IFloor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */