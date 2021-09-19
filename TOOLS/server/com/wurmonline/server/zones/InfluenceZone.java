/*    */ package com.wurmonline.server.zones;
/*    */ 
/*    */ import com.wurmonline.server.items.Item;
/*    */ 
/*    */ 
/*    */ public class InfluenceZone
/*    */   extends GenericZone
/*    */ {
/*    */   public InfluenceZone(Item i) {
/* 10 */     super(i);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public float getStrengthForTile(int tileX, int tileY, boolean surfaced) {
/* 16 */     if (getZoneItem() == null) {
/* 17 */       return 0.0F;
/*    */     }
/* 19 */     int xDiff = Math.abs(getZoneItem().getTileX() - tileX);
/* 20 */     int yDiff = Math.abs(getZoneItem().getTileY() - tileY);
/*    */ 
/*    */ 
/*    */     
/* 24 */     return getCurrentQL() - Math.max(xDiff, yDiff);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void updateZone() {
/* 30 */     if (getZoneItem() == null) {
/*    */       
/* 32 */       setCachedQL(0.0F);
/*    */       
/*    */       return;
/*    */     } 
/* 36 */     int dist = (int)getZoneItem().getCurrentQualityLevel();
/* 37 */     setBounds(getZoneItem().getTileX() - dist, getZoneItem().getTileY() - dist, 
/* 38 */         getZoneItem().getTileX() + dist, getZoneItem().getTileY() + dist);
/*    */     
/* 40 */     setCachedQL(getZoneItem().getCurrentQualityLevel());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected float getCurrentQL() {
/* 46 */     if (getZoneItem() == null) {
/* 47 */       return 0.0F;
/*    */     }
/* 49 */     return getZoneItem().getCurrentQualityLevel();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\InfluenceZone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */