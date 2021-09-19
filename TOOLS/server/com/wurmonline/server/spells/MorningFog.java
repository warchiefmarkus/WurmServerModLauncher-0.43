/*    */ package com.wurmonline.server.spells;
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
/*    */ public final class MorningFog
/*    */   extends CreatureEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   MorningFog() {
/* 30 */     super("Morning Fog", 282, 10, 5, 10, 7, 0L);
/* 31 */     this.targetCreature = true;
/* 32 */     this.enchantment = 19;
/* 33 */     this.effectdesc = "protection from thorns and lava.";
/* 34 */     this.description = "protection from thorns and lava";
/* 35 */     this.type = 2;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\MorningFog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */