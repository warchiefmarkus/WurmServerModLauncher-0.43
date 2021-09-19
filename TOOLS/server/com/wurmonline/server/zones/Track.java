/*    */ package com.wurmonline.server.zones;
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
/*    */ public final class Track
/*    */ {
/*    */   private final int tilex;
/*    */   private final int tiley;
/*    */   private final int tile;
/*    */   private final long time;
/*    */   private final String creatureName;
/*    */   private final byte direction;
/*    */   private final long id;
/*    */   
/*    */   Track(long aId, String aCreatureName, int x, int y, int aTile, long aTime, byte aDirection) {
/* 33 */     this.tilex = x;
/* 34 */     this.tiley = y;
/* 35 */     this.id = aId;
/* 36 */     this.creatureName = aCreatureName;
/* 37 */     this.tile = aTile;
/* 38 */     this.time = aTime;
/* 39 */     this.direction = aDirection;
/*    */   }
/*    */ 
/*    */   
/*    */   int getTileX() {
/* 44 */     return this.tilex;
/*    */   }
/*    */ 
/*    */   
/*    */   int getTileY() {
/* 49 */     return this.tiley;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getTime() {
/* 58 */     return this.time;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   int getTile() {
/* 67 */     return this.tile;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCreatureName() {
/* 76 */     return this.creatureName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getDirection() {
/* 85 */     return this.direction & 0xFF;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getId() {
/* 90 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\Track.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */