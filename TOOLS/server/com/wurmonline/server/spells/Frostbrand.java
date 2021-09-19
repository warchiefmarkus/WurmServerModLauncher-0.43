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
/*    */ public class Frostbrand
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 40;
/*    */   
/*    */   public Frostbrand() {
/* 28 */     super("Frostbrand", 417, 20, 45, 60, 40, 0L);
/* 29 */     this.targetWeapon = true;
/* 30 */     this.enchantment = 33;
/* 31 */     this.effectdesc = "will cause frost wounds.";
/* 32 */     this.description = "causes extra frost wounds on an enemy when hit";
/* 33 */     this.type = 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Frostbrand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */