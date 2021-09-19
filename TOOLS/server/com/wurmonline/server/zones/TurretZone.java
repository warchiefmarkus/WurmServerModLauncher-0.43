/*    */ package com.wurmonline.server.zones;
/*    */ 
/*    */ import com.wurmonline.server.items.Item;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TurretZone
/*    */   extends GenericZone
/*    */ {
/*    */   public static final float DISTMOD_QLMULTIPLIER = 5.0F;
/*    */   public static final int DISTMOD_TURRET = 3;
/*    */   public static final int DISTMOD_ARCHERYTOWER = 5;
/*    */   
/*    */   public TurretZone(Item i) {
/* 16 */     super(i);
/* 17 */     updateZone();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public float getStrengthForTile(int tileX, int tileY, boolean surfaced) {
/* 23 */     if (getZoneItem() == null) {
/* 24 */       return 0.0F;
/*    */     }
/* 26 */     if (getZoneItem().getTemplateId() == 934) {
/* 27 */       return 0.0F;
/*    */     }
/* 29 */     if (getZoneItem().isOnSurface() != surfaced) {
/* 30 */       return 0.0F;
/*    */     }
/* 32 */     if (!containsTile(tileX, tileY)) {
/* 33 */       return 0.0F;
/*    */     }
/* 35 */     int xDiff = Math.abs(tileX - getZoneItem().getTileX()) * 4;
/* 36 */     int yDiff = Math.abs(tileY - getZoneItem().getTileY()) * 4;
/*    */     
/* 38 */     float actDist = (float)Math.sqrt((xDiff * xDiff + yDiff * yDiff));
/*    */     
/* 40 */     return getCurrentQL() - actDist;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void updateZone() {
/* 46 */     if (getZoneItem() == null) {
/*    */       return;
/*    */     }
/* 49 */     float ql = getCurrentQL();
/* 50 */     float distanceModifier = ql / 100.0F * 5.0F;
/* 51 */     int dist = (int)((getZoneItem().isEnchantedTurret() ? 3 : 5) * distanceModifier);
/*    */     
/* 53 */     setBounds(getZoneItem().getTileX() - dist, getZoneItem().getTileY() - dist, 
/* 54 */         getZoneItem().getTileX() + dist, getZoneItem().getTileY() + dist);
/*    */     
/* 56 */     setCachedQL(ql);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected float getCurrentQL() {
/* 62 */     if (getZoneItem() == null) {
/* 63 */       return 0.0F;
/*    */     }
/* 65 */     if (getZoneItem().isEnchantedTurret() && !getZoneItem().isPlanted())
/*    */     {
/* 67 */       return 0.0F;
/*    */     }
/*    */     
/* 70 */     return getZoneItem().getCurrentQualityLevel();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\TurretZone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */