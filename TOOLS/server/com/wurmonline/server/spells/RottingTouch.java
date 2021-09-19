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
/*    */ public final class RottingTouch
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   RottingTouch() {
/* 26 */     super("Rotting Touch", 281, 20, 40, 60, 33, 0L);
/* 27 */     this.targetWeapon = true;
/* 28 */     this.enchantment = 18;
/* 29 */     this.effectdesc = "will hurt more, but be more brittle.";
/* 30 */     this.description = "causes extra damage on wounds, but takes more damage";
/* 31 */     this.type = 2;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\RottingTouch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */