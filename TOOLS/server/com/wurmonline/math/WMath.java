/*    */ package com.wurmonline.math;
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
/*    */ public final class WMath
/*    */ {
/*    */   private static final float pi = 3.1415927F;
/*    */   public static final float pi2 = 6.2831855F;
/*    */   public static final float DEG_TO_RAD = 0.017453292F;
/*    */   public static final float RAD_TO_DEG = 57.295776F;
/*    */   public static final float FAR_AWAY = 3.4028235E38F;
/*    */   
/*    */   public static float atan2(float y, float x) {
/* 37 */     if (y == 0.0F)
/* 38 */       return 0.0F; 
/* 39 */     float coeff_1 = 0.7853982F;
/* 40 */     float coeff_2 = 2.3561945F;
/* 41 */     float abs_y = Math.abs(y);
/* 42 */     float angle = 0.0F;
/* 43 */     if (x >= 0.0F) {
/*    */       
/* 45 */       float r = (x - abs_y) / (x + abs_y);
/* 46 */       angle = 0.7853982F - 0.7853982F * r;
/*    */     }
/*    */     else {
/*    */       
/* 50 */       float r = (x + abs_y) / (abs_y - x);
/* 51 */       angle = 2.3561945F - 0.7853982F * r;
/*    */     } 
/* 53 */     if (y < 0.0F) {
/* 54 */       return -angle;
/*    */     }
/* 56 */     return angle;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int floor(float f) {
/* 65 */     return (f > 0.0F) ? (int)f : -((int)-f);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static float abs(float f) {
/* 74 */     return (f >= 0.0F) ? f : -f;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static float getRadFromDeg(float deg) {
/* 83 */     return 0.017453292F * deg;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static float getDegFromRad(float rad) {
/* 92 */     return 57.295776F * rad;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\math\WMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */