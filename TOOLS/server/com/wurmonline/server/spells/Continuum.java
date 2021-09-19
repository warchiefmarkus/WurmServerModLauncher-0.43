/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.Server;
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
/*    */ public class Continuum
/*    */   extends KarmaEnchantment
/*    */ {
/*    */   public static final int RANGE = 24;
/*    */   
/*    */   public Continuum() {
/* 39 */     super("Continuum", 552, 20, 650, 20, 1, 240000L);
/* 40 */     this.targetCreature = true;
/* 41 */     this.enchantment = 69;
/* 42 */     this.effectdesc = "20% general damage reduction.";
/* 43 */     this.description = "gives you 20% protection against most damage";
/* 44 */     this.durationModifier = 3.0F;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 50 */     SpellEffects effs = target.getSpellEffects();
/* 51 */     if (effs == null)
/*    */     {
/* 53 */       effs = target.createSpellEffects();
/*    */     }
/* 55 */     SpellEffect eff = effs.getSpellEffect(this.enchantment);
/*    */     
/* 57 */     int duration = (int)(900.0D + 1800.0D * power / 100.0D);
/* 58 */     if (eff == null) {
/*    */ 
/*    */ 
/*    */       
/* 62 */       performer.getCommunicator().sendNormalServerMessage("Duration: " + duration, (byte)2);
/* 63 */       if (target != performer)
/* 64 */         performer.getCommunicator().sendNormalServerMessage(target.getNameWithGenus() + " now has " + this.effectdesc); 
/* 65 */       target.getCommunicator().sendNormalServerMessage("You now have " + this.effectdesc);
/* 66 */       eff = new SpellEffect(target.getWurmId(), this.enchantment, (float)power, duration, (byte)9, (byte)0, true);
/*    */ 
/*    */       
/* 69 */       effs.addSpellEffect(eff);
/* 70 */       Server.getInstance().broadCastAction(performer.getName() + " looks pleased.", performer, 5);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     }
/* 76 */     else if (eff.getPower() > power) {
/*    */       
/* 78 */       performer.getCommunicator().sendNormalServerMessage("You frown as you fail to improve the power.", (byte)3);
/* 79 */       Server.getInstance().broadCastAction(performer.getName() + " frowns.", performer, 5);
/*    */     }
/*    */     else {
/*    */       
/* 83 */       if (target != performer) {
/* 84 */         performer.getCommunicator().sendNormalServerMessage("You succeed in improving the power of the " + this.name + ".", (byte)2);
/*    */       }
/* 86 */       target.getCommunicator().sendNormalServerMessage("You will now receive improved " + this.effectdesc);
/* 87 */       eff.setPower((float)power);
/* 88 */       eff.setTimeleft(Math.max(eff.timeleft, duration));
/* 89 */       target.sendUpdateSpellEffect(eff);
/* 90 */       Server.getInstance().broadCastAction(performer.getName() + " looks pleased.", performer, 5);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Continuum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */