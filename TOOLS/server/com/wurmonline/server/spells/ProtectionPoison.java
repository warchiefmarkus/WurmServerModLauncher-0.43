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
/*    */ public final class ProtectionPoison
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   ProtectionPoison() {
/* 40 */     super("Poison Protection", 266, 30, 30, 30, 27, 0L);
/* 41 */     this.targetJewelry = true;
/* 42 */     this.enchantment = 8;
/* 43 */     this.effectdesc = "will reduce any poison damage you take.";
/* 44 */     this.description = "reduces any poison damage you take";
/* 45 */     this.type = 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\ProtectionPoison.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */