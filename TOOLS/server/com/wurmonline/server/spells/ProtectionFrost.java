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
/*    */ public final class ProtectionFrost
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   ProtectionFrost() {
/* 40 */     super("Frost Protection", 264, 30, 30, 30, 30, 0L);
/* 41 */     this.targetJewelry = true;
/* 42 */     this.enchantment = 6;
/* 43 */     this.effectdesc = "will reduce any frost damage you take.";
/* 44 */     this.description = "reduces any frost damage you take";
/* 45 */     this.type = 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\ProtectionFrost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */