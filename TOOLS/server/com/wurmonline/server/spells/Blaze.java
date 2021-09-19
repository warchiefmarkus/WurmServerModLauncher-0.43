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
/*    */ public final class Blaze
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   Blaze() {
/* 40 */     super("Blaze", 260, 30, 40, 50, 46, 0L);
/* 41 */     this.targetJewelry = true;
/* 42 */     this.enchantment = 2;
/* 43 */     this.effectdesc = "will increase any fire damage you cause.";
/* 44 */     this.description = "increases any fire damage you cause";
/* 45 */     this.type = 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Blaze.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */