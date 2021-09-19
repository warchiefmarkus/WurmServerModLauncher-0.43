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
/*    */ public class Weakness
/*    */   extends CreatureEnchantment
/*    */ {
/*    */   public static final int RANGE = 50;
/*    */   
/*    */   public Weakness() {
/* 29 */     super("Weakness", 429, 20, 50, 40, 40, 30000L);
/* 30 */     this.enchantment = 41;
/* 31 */     this.offensive = true;
/* 32 */     this.effectdesc = "reduced body strength.";
/* 33 */     this.description = "reduces body strength by one fifth";
/* 34 */     this.type = 2;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Weakness.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */