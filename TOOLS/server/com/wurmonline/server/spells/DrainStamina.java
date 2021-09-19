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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class DrainStamina
/*    */   extends ReligiousSpell
/*    */ {
/*    */   public static final int RANGE = 12;
/*    */   
/*    */   DrainStamina() {
/* 36 */     super("Drain Stamina", 254, 9, 20, 20, 10, 0L);
/* 37 */     this.targetCreature = true;
/* 38 */     this.offensive = true;
/* 39 */     this.description = "drains stamina from a creature and returns it to you";
/* 40 */     this.type = 2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/* 51 */     if (target.isReborn()) {
/*    */       
/* 53 */       performer.getCommunicator().sendNormalServerMessage("You can not drain stamina from the " + target
/* 54 */           .getNameWithGenus() + ".", (byte)3);
/*    */       
/* 56 */       return false;
/*    */     } 
/* 58 */     if (target.equals(performer))
/* 59 */       return false; 
/* 60 */     if (target.getStatus().getStamina() < 200) {
/*    */       
/* 62 */       performer.getCommunicator().sendNormalServerMessage(target
/* 63 */           .getNameWithGenus() + " does not have enough stamina to drain.", (byte)3);
/*    */       
/* 65 */       return false;
/*    */     } 
/* 67 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 78 */     int stam = target.getStatus().getStamina();
/*    */     
/* 80 */     int staminaGained = (int)(Math.max(power, 20.0D) / 200.0D * stam * target.addSpellResistance((short)254));
/* 81 */     if (staminaGained > 1) {
/*    */       
/* 83 */       performer.getStatus().modifyStamina((int)(0.75D * staminaGained));
/* 84 */       target.getStatus().modifyStamina(-staminaGained);
/* 85 */       performer.getCommunicator().sendNormalServerMessage("You drain some stamina from " + target.getNameWithGenus() + ".", (byte)4);
/*    */       
/* 87 */       target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " drains you on stamina.", (byte)4);
/*    */     
/*    */     }
/*    */     else {
/*    */       
/* 92 */       performer.getCommunicator().sendNormalServerMessage("You try to drain some stamina from " + target
/* 93 */           .getNameWithGenus() + " but fail.", (byte)3);
/*    */       
/* 95 */       target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " tries to drain you on stamina but fails.", (byte)4);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\DrainStamina.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */