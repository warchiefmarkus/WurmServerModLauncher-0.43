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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StoneSkin
/*    */   extends KarmaEnchantment
/*    */ {
/*    */   public StoneSkin() {
/* 34 */     super("Stoneskin", 553, 20, 500, 20, 1, 240000L);
/* 35 */     this.targetCreature = true;
/* 36 */     this.enchantment = 68;
/* 37 */     this.effectdesc = "3 wounds ignored.";
/* 38 */     this.description = "makes you ignore 3 wounds";
/* 39 */     this.durationModifier = 100000.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\StoneSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */