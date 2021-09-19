/*    */ package com.wurmonline.shared.util;
/*    */ 
/*    */ import java.util.Random;
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
/*    */ public final class TerrainUtilities
/*    */ {
/* 29 */   private static final Random random = new Random();
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
/*    */   public static float getTreePosX(int xTile, int yTile) {
/* 49 */     random.setSeed(xTile * 31273612L + yTile * 4327864168313L);
/* 50 */     return random.nextFloat() * 0.75F + 0.125F;
/*    */   }
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
/*    */   public static float getTreePosY(int xTile, int yTile) {
/* 63 */     random.setSeed(xTile * 31273612L + yTile * 4327864168314L);
/* 64 */     return random.nextFloat() * 0.75F + 0.125F;
/*    */   }
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
/*    */   public static float getTreeRotation(int xTile, int yTile) {
/* 77 */     random.setSeed(xTile * 31273612L + yTile * 4327864168315L);
/* 78 */     return random.nextFloat() * 360.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\share\\util\TerrainUtilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */