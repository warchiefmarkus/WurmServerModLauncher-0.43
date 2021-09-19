package com.wurmonline.server.structures;

public interface StructureSupport {
  boolean supports(StructureSupport paramStructureSupport);
  
  int getFloorLevel();
  
  int getStartX();
  
  int getStartY();
  
  int getMinX();
  
  int getMinY();
  
  boolean isHorizontal();
  
  boolean isFloor();
  
  boolean isFence();
  
  boolean isWall();
  
  int getEndX();
  
  int getEndY();
  
  boolean isSupportedByGround();
  
  boolean supports();
  
  String getName();
  
  long getId();
  
  boolean equals(StructureSupport paramStructureSupport);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\StructureSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */