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
/*    */ public final class WindOfAges
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   WindOfAges() {
/* 31 */     super("Wind of Ages", 279, 20, 50, 60, 50, 0L);
/* 32 */     this.targetItem = true;
/* 33 */     this.enchantment = 16;
/* 34 */     this.effectdesc = "will be quicker to use.";
/* 35 */     this.description = "increases usage speed";
/* 36 */     this.type = 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\WindOfAges.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */