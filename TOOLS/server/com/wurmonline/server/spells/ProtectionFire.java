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
/*    */ public final class ProtectionFire
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   ProtectionFire() {
/* 40 */     super("Fire Protection", 265, 30, 30, 30, 28, 0L);
/* 41 */     this.targetJewelry = true;
/* 42 */     this.enchantment = 7;
/* 43 */     this.effectdesc = "will reduce any fire damage you take.";
/* 44 */     this.description = "reduces any fire damage you take";
/* 45 */     this.type = 2;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\ProtectionFire.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */