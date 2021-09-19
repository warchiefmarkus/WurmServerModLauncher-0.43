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
/*    */ public class Nimbleness
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   public Nimbleness() {
/* 28 */     super("Nimbleness", 416, 20, 60, 60, 30, 0L);
/* 29 */     this.targetWeapon = true;
/* 30 */     this.enchantment = 32;
/* 31 */     this.effectdesc = "increase the chance to hit.";
/* 32 */     this.description = "increases chance to hit";
/* 33 */     this.type = 2;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Nimbleness.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */