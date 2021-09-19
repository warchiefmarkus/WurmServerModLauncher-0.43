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
/*    */ public final class Corrosion
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   Corrosion() {
/* 39 */     super("Corrosion", 262, 30, 40, 50, 44, 0L);
/* 40 */     this.targetJewelry = true;
/* 41 */     this.enchantment = 4;
/* 42 */     this.effectdesc = "will increase any acid damage you cause.";
/* 43 */     this.description = "increases any acid damage you cause";
/* 44 */     this.type = 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Corrosion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */