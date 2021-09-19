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
/*    */ public class Venom
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 40;
/*    */   
/*    */   Venom() {
/* 28 */     super("Venom", 412, 20, 100, 60, 62, 0L);
/* 29 */     this.targetWeapon = true;
/* 30 */     this.enchantment = 27;
/* 31 */     this.effectdesc = "will deal only poison damage wounds.";
/* 32 */     this.description = "causes a weapon to deal poison wounds instead of normal damage, but may reduce damage";
/* 33 */     this.type = 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Venom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */