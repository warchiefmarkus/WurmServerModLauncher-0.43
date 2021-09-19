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
/*    */ public class Hellstrength
/*    */   extends CreatureEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   public Hellstrength() {
/* 28 */     super("Hell Strength", 427, 10, 60, 40, 45, 30000L);
/*    */     
/* 30 */     this.enchantment = 40;
/* 31 */     this.effectdesc = "increased body strength and soul strength.";
/* 32 */     this.description = "increases body strength and soul strength";
/* 33 */     this.type = 2;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Hellstrength.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */