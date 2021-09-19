/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.behaviours.Action;
/*    */ import com.wurmonline.server.behaviours.NoSuchActionException;
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
/*    */ public class TangleWeave
/*    */   extends CreatureEnchantment
/*    */ {
/*    */   public static final int RANGE = 50;
/*    */   
/*    */   public TangleWeave() {
/* 38 */     super("Tangleweave", 641, 3, 15, 30, 10, 30000L);
/* 39 */     this.enchantment = 93;
/* 40 */     this.offensive = true;
/* 41 */     this.effectdesc = "interrupts and slow casting.";
/* 42 */     this.description = "interrupts an enemy spell caster and slows future spells";
/* 43 */     this.durationModifier = 0.5F;
/* 44 */     this.type = 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean precondition(Skill castSkill, Creature performer, Creature target) {
/* 51 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 57 */     super.doEffect(castSkill, power, performer, target);
/*    */     
/*    */     try {
/* 60 */       Action act = target.getCurrentAction();
/* 61 */       if (act.isSpell()) {
/*    */         
/* 63 */         performer.getCommunicator().sendCombatNormalMessage(String.format("You interrupt %s from %s.", new Object[] { target.getName(), act.getActionString() }));
/* 64 */         String toSend = target.getActions().stopCurrentAction(false);
/* 65 */         if (toSend.length() > 0)
/* 66 */           target.getCommunicator().sendNormalServerMessage(toSend); 
/* 67 */         target.sendActionControl("", false, 0);
/*    */         
/*    */         return;
/*    */       } 
/* 71 */     } catch (NoSuchActionException noSuchActionException) {}
/* 72 */     performer.getCommunicator().sendCombatNormalMessage("You failed to interrupt " + target.getName() + ".");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\TangleWeave.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */