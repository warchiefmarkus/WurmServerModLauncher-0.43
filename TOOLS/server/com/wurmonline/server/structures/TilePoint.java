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
/*    */ public class TilePoint
/*    */ {
/*    */   private int tileX;
/*    */   private int tileY;
/*    */   
/*    */   public TilePoint(int pTileX, int pTileY) {
/* 29 */     this.tileX = pTileX;
/* 30 */     this.tileY = pTileY;
/*    */   }
/*    */   
/*    */   public void setTileX(int val) {
/* 34 */     this.tileX = val;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getTileX() {
/* 39 */     return this.tileX;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setTileY(int val) {
/* 44 */     this.tileY = val;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getTileY() {
/* 49 */     return this.tileY;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 58 */     return "[" + this.tileX + "," + this.tileY + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\TilePoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */