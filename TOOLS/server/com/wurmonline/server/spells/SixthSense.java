/*    */ package com.wurmonline.server.spells;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ public class SixthSense
/*    */   extends CreatureEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   SixthSense() {
/* 33 */     super("Sixth Sense", 376, 10, 15, 20, 6, 0L);
/* 34 */     this.targetCreature = true;
/* 35 */     this.enchantment = 21;
/* 36 */     this.effectdesc = "detect hidden dangers.";
/* 37 */     this.description = "detect hidden creatures and traps";
/* 38 */     this.type = 0;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/* 44 */     if (!target.isPlayer()) {
/*    */       
/* 46 */       performer.getCommunicator().sendNormalServerMessage("You can only cast that on a person.");
/* 47 */       return false;
/*    */     } 
/*    */     
/* 50 */     if (target.isReborn()) {
/* 51 */       return false;
/*    */     }
/* 53 */     if (!target.equals(performer)) {
/*    */       
/* 55 */       if (performer.getDeity() != null) {
/*    */         
/* 57 */         if (target.getDeity() != null) {
/*    */           
/* 59 */           if (target.getDeity().isHateGod())
/*    */           {
/* 61 */             return performer.isFaithful();
/*    */           }
/*    */           
/* 64 */           return true;
/*    */         } 
/*    */         
/* 67 */         return true;
/*    */       } 
/*    */       
/* 70 */       return true;
/*    */     } 
/*    */     
/* 73 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\SixthSense.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */