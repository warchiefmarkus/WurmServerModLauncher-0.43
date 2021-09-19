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
/*    */ public class Truestrike
/*    */   extends KarmaEnchantment
/*    */ {
/*    */   public static final int RANGE = 24;
/*    */   
/*    */   public Truestrike() {
/* 33 */     super("True Strike", 555, 5, 500, 20, 1, 180000L);
/* 34 */     this.targetCreature = true;
/*    */     
/* 36 */     this.enchantment = 67;
/* 37 */     this.effectdesc = "one critical hit coming up.";
/* 38 */     this.description = "your next hit will be a critical strike";
/* 39 */     this.durationModifier = 3.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Truestrike.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */