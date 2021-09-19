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
/*    */ public class LifeTransfer
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   LifeTransfer() {
/* 35 */     super("Life Transfer", 409, 20, 100, 60, 61, 0L);
/* 36 */     this.targetWeapon = true;
/* 37 */     this.enchantment = 26;
/* 38 */     this.effectdesc = "will transfer life to you when harming enemies.";
/* 39 */     this.description = "heals the wielder when causing damage to an enemy";
/* 40 */     this.type = 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\LifeTransfer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */