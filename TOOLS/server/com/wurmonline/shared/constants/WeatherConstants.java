/*    */ package com.wurmonline.shared.constants;
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
/*    */ 
/*    */ 
/*    */ public final class WeatherConstants
/*    */ {
/*    */   private static final double DEGS_TO_RADS = 0.017453292519943295D;
/*    */   
/*    */   public static final float getNormalizedWindX(float windRotation) {
/* 35 */     return -((float)Math.sin(windRotation * 0.017453292519943295D));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final float getNormalizedWindY(float windRotation) {
/* 44 */     return (float)Math.cos(windRotation * 0.017453292519943295D);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final float getWindX(float windRotation, float windPower) {
/* 54 */     return -((float)Math.sin(windRotation * 0.017453292519943295D)) * Math.abs(windPower);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final float getWindY(float windRotation, float windPower) {
/* 64 */     return (float)Math.cos(windRotation * 0.017453292519943295D) * Math.abs(windPower);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\shared\constants\WeatherConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */