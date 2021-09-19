/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.Servers;
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.skills.Skill;
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
/*    */ public class FranticCharge
/*    */   extends CreatureEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   public FranticCharge() {
/* 32 */     super("Frantic Charge", 423, 5, 20, 30, 30, 0L);
/* 33 */     this.targetCreature = true;
/* 34 */     this.enchantment = 39;
/* 35 */     this.effectdesc = "faster attack and movement speed.";
/* 36 */     this.description = "increases attack and movement speed of a player";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/* 42 */     if (super.precondition(castSkill, performer, target)) {
/*    */       
/* 44 */       if (Servers.isThisAPvpServer() && !target.isPlayer()) {
/*    */         
/* 46 */         performer.getCommunicator().sendNormalServerMessage("You cannot cast " + getName() + " on " + target.getNameWithGenus());
/* 47 */         return false;
/*    */       } 
/*    */     } else {
/*    */       
/* 51 */       return false;
/*    */     } 
/* 53 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\FranticCharge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */