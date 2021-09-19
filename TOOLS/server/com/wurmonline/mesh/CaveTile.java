/*    */ package com.wurmonline.mesh;
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
/*    */ public final class CaveTile
/*    */   implements Cloneable
/*    */ {
/* 22 */   private static final CaveTile[] tiles = new CaveTile[256];
/*    */   
/* 24 */   static final CaveTile TILE_HOLE = new CaveTile("hole", 0);
/* 25 */   static final CaveTile TILE_ROCK = new CaveTile("Rock", 1);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   final byte id;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private CaveTile(String name, int id) {
/* 37 */     this.id = (byte)id;
/* 38 */     tiles[id] = this;
/*    */   }
/*    */ 
/*    */   
/*    */   static CaveTile getTile(int id) {
/* 43 */     return tiles[id];
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   byte getId() {
/* 51 */     return this.id;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static float decodeHeightAsFloat(int encodedTile) {
/* 62 */     return Tiles.decodeHeightAsFloat(encodedTile);
/*    */   }
/*    */ 
/*    */   
/*    */   public static byte decodeCeilingTexture(int encodedTile) {
/* 67 */     return (byte)(encodedTile >> 28 & 0xF);
/*    */   }
/*    */ 
/*    */   
/*    */   public static byte decodeFloorTexture(int encodedTile) {
/* 72 */     return (byte)(encodedTile >> 24 & 0xF);
/*    */   }
/*    */ 
/*    */   
/*    */   public static int decodeCeilingHeight(int encodedTile) {
/* 77 */     return encodedTile >> 16 & 0xFF;
/*    */   }
/*    */ 
/*    */   
/*    */   public static float decodeCeilingHeightAsFloat(int encodedTile) {
/* 82 */     return (encodedTile >> 16 & 0xFF) / 10.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public static int decodeCaveTileDir(long wurmId) {
/* 87 */     return (int)(wurmId >> 48L) & 0xFF;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\mesh\CaveTile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */