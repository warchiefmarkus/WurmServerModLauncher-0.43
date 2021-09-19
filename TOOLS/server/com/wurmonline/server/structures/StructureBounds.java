/*    */ package com.wurmonline.server.structures;
/*    */ 
/*    */ import com.wurmonline.server.zones.Zones;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StructureBounds
/*    */ {
/*    */   TilePoint max;
/*    */   TilePoint min;
/*    */   
/*    */   StructureBounds(int minTileX, int minTileY, int maxTileX, int maxTileY) {
/* 32 */     this.max = new TilePoint(maxTileX, maxTileY);
/* 33 */     this.min = new TilePoint(minTileX, minTileY);
/*    */   }
/*    */ 
/*    */   
/*    */   StructureBounds(TilePoint pMax, TilePoint pMin) {
/* 38 */     if (pMax.getTileX() > Zones.worldTileSizeX)
/*    */     {
/* 40 */       pMax.setTileX(Zones.worldTileSizeX);
/*    */     }
/*    */     
/* 43 */     if (pMax.getTileY() > Zones.worldTileSizeY)
/*    */     {
/* 45 */       pMax.setTileY(Zones.worldTileSizeY);
/*    */     }
/*    */     
/* 48 */     this.max = pMax;
/* 49 */     this.min = pMin;
/*    */   }
/*    */ 
/*    */   
/*    */   public TilePoint getMax() {
/* 54 */     return this.max;
/*    */   }
/*    */ 
/*    */   
/*    */   public TilePoint getMin() {
/* 59 */     return this.min;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\StructureBounds.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */