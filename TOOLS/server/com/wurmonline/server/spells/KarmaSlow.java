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
/*    */ public class KarmaSlow
/*    */   extends KarmaEnchantment
/*    */ {
/*    */   public static final int RANGE = 24;
/*    */   
/*    */   public KarmaSlow() {
/* 39 */     super("Karma Slow", 554, 10, 500, 20, 1, 120000L);
/* 40 */     this.targetCreature = true;
/* 41 */     this.offensive = true;
/* 42 */     this.enchantment = 66;
/* 43 */     this.effectdesc = "slower attack speed.";
/* 44 */     this.description = "slows down attacks";
/* 45 */     this.durationModifier = 1.0F;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 52 */     SpellEffects effs = target.getSpellEffects();
/* 53 */     if (effs == null)
/*    */     {
/* 55 */       effs = target.createSpellEffects();
/*    */     }
/* 57 */     SpellEffect eff = effs.getSpellEffect(this.enchantment);
/*    */     
/* 59 */     int duration = (int)(30.0D + 270.0D * power / 100.0D);
/* 60 */     if (eff == null) {
/*    */       
/* 62 */       if (target != performer) {
/* 63 */         performer.getCommunicator().sendNormalServerMessage(target.getName() + " now has " + this.effectdesc, (byte)2);
/*    */       }
/* 65 */       target.getCommunicator().sendNormalServerMessage("You now have " + this.effectdesc, (byte)2);
/* 66 */       eff = new SpellEffect(target.getWurmId(), this.enchantment, (float)power, duration, (byte)9, (byte)1, true);
/*    */ 
/*    */       
/* 69 */       effs.addSpellEffect(eff);
/* 70 */       Server.getInstance().broadCastAction(performer.getName() + " looks pleased.", performer, 5);
/*    */ 
/*    */     
/*    */     }
/* 74 */     else if (eff.getPower() > power) {
/*    */       
/* 76 */       performer.getCommunicator().sendNormalServerMessage("You frown as you fail to improve the power.", (byte)3);
/*    */       
/* 78 */       Server.getInstance().broadCastAction(performer.getName() + " frowns.", performer, 5);
/*    */     }
/*    */     else {
/*    */       
/* 82 */       if (target != performer) {
/* 83 */         performer.getCommunicator().sendNormalServerMessage("You succeed in improving the power of the " + this.name + ".", (byte)2);
/*    */       }
/* 85 */       target.getCommunicator().sendNormalServerMessage("You will now receive improved " + this.effectdesc, (byte)2);
/*    */       
/* 87 */       eff.setPower((float)power);
/* 88 */       eff.setTimeleft(Math.max(eff.timeleft, duration));
/*    */       
/* 90 */       target.sendUpdateSpellEffect(eff);
/* 91 */       Server.getInstance().broadCastAction(performer.getName() + " looks pleased.", performer, 5);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\KarmaSlow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */