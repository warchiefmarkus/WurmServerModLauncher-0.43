/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.creatures.SpellEffects;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Purge
/*    */   extends ReligiousSpell
/*    */ {
/*    */   public static final int RANGE = 24;
/*    */   
/*    */   public Purge() {
/* 40 */     super("Purge", 946, 15, 35, 30, 45, 300000L);
/* 41 */     this.targetCreature = true;
/* 42 */     this.offensive = true;
/* 43 */     this.description = "dispels all effects on the target";
/* 44 */     this.type = 0;
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
/* 55 */     if (performer != target) {
/* 56 */       target.getCommunicator().sendCombatNormalMessage(performer.getNameWithGenus() + " purges you!");
/*    */     }
/*    */     
/* 59 */     if (target.getSpellEffects() == null) {
/*    */       
/* 61 */       performer.getCommunicator().sendCombatNormalMessage(String.format("%s has no effects to dispel.", new Object[] { target.getName() }));
/*    */       
/*    */       return;
/*    */     } 
/* 65 */     SpellEffects effs = target.getSpellEffects();
/* 66 */     SpellEffect[] speffs = effs.getEffects();
/* 67 */     for (SpellEffect speff : speffs) {
/*    */ 
/*    */       
/* 70 */       if (speff.type != 64 && speff.type != 74 && speff.type != 73 && speff.type != 75)
/*    */       {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 79 */         if (speff.type != 66 && speff.type != 67 && speff.type != 68 && speff.type != 69 && speff.type != 70)
/*    */         {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 89 */           if (speff.getSpellInfluenceType() == 0)
/* 90 */             effs.removeSpellEffect(speff);  }  } 
/*    */     } 
/* 92 */     performer.getCommunicator().sendCombatNormalMessage(String.format("%s is completely purged of effects!", new Object[] { target.getName() }));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Purge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */