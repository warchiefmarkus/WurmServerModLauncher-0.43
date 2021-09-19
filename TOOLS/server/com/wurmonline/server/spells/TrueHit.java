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
/*    */ public class TrueHit
/*    */   extends CreatureEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   public TrueHit() {
/* 26 */     super("Truehit", 447, 10, 10, 15, 30, 0L);
/* 27 */     this.targetCreature = true;
/* 28 */     this.enchantment = 30;
/* 29 */     this.effectdesc = "combat vision and aiming.";
/* 30 */     this.description = "increases offensive combat rating";
/* 31 */     this.type = 2;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\TrueHit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */