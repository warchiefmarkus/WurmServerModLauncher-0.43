/*    */ package com.wurmonline.server.zones;
/*    */ 
/*    */ import com.wurmonline.server.items.Item;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class GenericZone
/*    */ {
/*    */   private Item zoneOwner;
/*    */   private float cachedQL;
/*    */   private int startX;
/*    */   private int startY;
/*    */   private int endX;
/*    */   private int endY;
/*    */   
/*    */   public GenericZone(Item i) {
/* 22 */     this.zoneOwner = i;
/* 23 */     updateZone();
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract float getStrengthForTile(int paramInt1, int paramInt2, boolean paramBoolean);
/*    */   
/*    */   public boolean containsTile(int tileX, int tileY) {
/* 30 */     if (this.zoneOwner == null) {
/* 31 */       return false;
/*    */     }
/* 33 */     if (this.cachedQL != getCurrentQL()) {
/* 34 */       updateZone();
/*    */     }
/* 36 */     if (tileX >= this.startX && tileX <= this.endX && 
/* 37 */       tileY >= this.startY && tileY <= this.endY) {
/* 38 */       return true;
/*    */     }
/* 40 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract void updateZone();
/*    */   
/*    */   public Item getZoneItem() {
/* 47 */     return this.zoneOwner;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setZoneItem(Item i) {
/* 52 */     this.zoneOwner = i;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCachedQL(float ql) {
/* 57 */     this.cachedQL = ql;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setBounds(int sx, int sy, int ex, int ey) {
/* 62 */     this.startX = sx;
/* 63 */     this.startY = sy;
/* 64 */     this.endX = ex;
/* 65 */     this.endY = ey;
/*    */   }
/*    */   
/*    */   protected abstract float getCurrentQL();
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\GenericZone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */