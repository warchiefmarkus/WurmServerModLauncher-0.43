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
/*    */ public class ForestGiant
/*    */   extends CreatureEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   ForestGiant() {
/* 28 */     super("Forest Giant Strength", 410, 10, 50, 49, 55, 0L);
/* 29 */     this.enchantment = 25;
/* 30 */     this.effectdesc = "increased body strength.";
/* 31 */     this.description = "increases body strength";
/* 32 */     this.type = 2;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\ForestGiant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */