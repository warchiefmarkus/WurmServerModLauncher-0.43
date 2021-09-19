/*    */ package com.wurmonline.server.creatures;
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
/*    */ public final class CreatureMove
/*    */ {
/* 25 */   public long timestamp = 0L;
/*    */ 
/*    */   
/*    */   public float diffX;
/*    */ 
/*    */   
/*    */   public float diffY;
/*    */ 
/*    */   
/*    */   public float diffZ;
/*    */ 
/*    */   
/*    */   public int rotation;
/*    */ 
/*    */   
/*    */   public void resetXYZ() {
/* 41 */     this.diffX = 0.0F;
/* 42 */     this.diffY = 0.0F;
/* 43 */     this.diffZ = 0.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\CreatureMove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */