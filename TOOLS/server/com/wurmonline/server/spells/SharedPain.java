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
/*    */ public final class SharedPain
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   SharedPain() {
/* 30 */     super("Aura of Shared Pain", 278, 20, 35, 60, 25, 0L);
/* 31 */     this.targetArmour = true;
/* 32 */     this.enchantment = 17;
/* 33 */     this.effectdesc = "may damage creatures when they hit this armour.";
/* 34 */     this.description = "causes damage to creatures when they hit armour enchanted with this";
/* 35 */     this.type = 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\SharedPain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */