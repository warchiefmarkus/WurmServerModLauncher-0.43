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
/*    */ public class MindStealer
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   public MindStealer() {
/* 28 */     super("Mind Stealer", 415, 20, 100, 60, 50, 0L);
/* 29 */     this.targetWeapon = true;
/* 30 */     this.enchantment = 31;
/* 31 */     this.effectdesc = "will steal skill knowledge from some creatures.";
/* 32 */     this.description = "may steal some higher skills from enemies on hits";
/* 33 */     this.type = 2;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\MindStealer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */