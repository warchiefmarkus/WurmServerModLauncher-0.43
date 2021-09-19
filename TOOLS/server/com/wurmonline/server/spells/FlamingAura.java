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
/*    */ public final class FlamingAura
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   FlamingAura() {
/* 30 */     super("Flaming Aura", 277, 20, 45, 60, 39, 0L);
/* 31 */     this.targetWeapon = true;
/* 32 */     this.enchantment = 14;
/* 33 */     this.effectdesc = "will burn targets hit with it.";
/* 34 */     this.description = "causes extra fire wounds on an enemy when hit";
/* 35 */     this.type = 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\FlamingAura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */