/*    */ package com.wurmonline.server.structures;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BuildTile
/*    */ {
/*    */   private final int tilex;
/*    */   private final int tiley;
/*    */   private final int layer;
/*    */   
/*    */   public BuildTile(int tx, int ty, int tileLayer) {
/* 35 */     this.tilex = tx;
/* 36 */     this.tiley = ty;
/* 37 */     this.layer = tileLayer;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getTileX() {
/* 47 */     return this.tilex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getTileY() {
/* 57 */     return this.tiley;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getLayer() {
/* 67 */     return this.layer;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\BuildTile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */