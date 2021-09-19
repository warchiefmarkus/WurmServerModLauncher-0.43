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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Toxin
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   Toxin() {
/* 40 */     super("Toxin", 259, 30, 40, 20, 45, 0L);
/* 41 */     this.targetJewelry = true;
/* 42 */     this.enchantment = 1;
/* 43 */     this.effectdesc = "will increase any poison damage you cause.";
/* 44 */     this.description = "increases any poison damage you cause";
/* 45 */     this.type = 2;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Toxin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */