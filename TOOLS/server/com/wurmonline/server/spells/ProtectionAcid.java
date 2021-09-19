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
/*    */ public final class ProtectionAcid
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   ProtectionAcid() {
/* 40 */     super("Acid Protection", 263, 30, 30, 30, 32, 0L);
/* 41 */     this.targetJewelry = true;
/* 42 */     this.enchantment = 5;
/* 43 */     this.effectdesc = "will reduce any acid damage you take.";
/* 44 */     this.description = "reduces any acid damage you take";
/* 45 */     this.type = 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\ProtectionAcid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */