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
/*    */ public class EssenceDrain
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   EssenceDrain() {
/* 35 */     super("Essence Drain", 933, 20, 100, 60, 61, 0L);
/* 36 */     this.targetWeapon = true;
/* 37 */     this.enchantment = 63;
/* 38 */     this.effectdesc = "will cause extra internal wounds and heal you.";
/* 39 */     this.description = "causes extra internal wounds and heals the wielder slightly";
/* 40 */     this.type = 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\EssenceDrain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */