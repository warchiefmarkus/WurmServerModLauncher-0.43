/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.SpellEffects;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.shared.constants.AttitudeConstants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CreatureEnchantment
/*     */   extends ReligiousSpell
/*     */   implements AttitudeConstants
/*     */ {
/*  30 */   float durationModifier = 20.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CreatureEnchantment(String aName, int aNum, int aCastingTime, int aCost, int aDifficulty, int aLevel, long aCooldown) {
/*  40 */     super(aName, aNum, aCastingTime, aCost, aDifficulty, aLevel, aCooldown);
/*  41 */     this.targetCreature = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/*  52 */     if (target.isReborn()) {
/*     */       
/*  54 */       performer.getCommunicator().sendNormalServerMessage(target.getNameWithGenus() + " doesn't seem affected.", (byte)3);
/*     */       
/*  56 */       return false;
/*     */     } 
/*  58 */     if (!target.equals(performer)) {
/*     */       
/*  60 */       if (!isOffensive() && target.getKingdomId() != 0 && 
/*  61 */         !performer.isFriendlyKingdom(target.getKingdomId())) {
/*     */         
/*  63 */         performer.getCommunicator().sendNormalServerMessage("Nothing happens as you try to cast this on an enemy.", (byte)3);
/*     */ 
/*     */         
/*  66 */         return false;
/*     */       } 
/*     */       
/*  69 */       if (performer.getDeity() != null && !isOffensive()) {
/*     */         
/*  71 */         if (target.getAttitude(performer) != 2)
/*     */         {
/*  73 */           return true;
/*     */         }
/*  75 */         performer.getCommunicator().sendNormalServerMessage(performer
/*  76 */             .getDeity().getName() + " would never help the infidel " + target.getName() + ".", (byte)3);
/*     */         
/*  78 */         return false;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  95 */       return true;
/*     */     } 
/*     */     
/*  98 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void doImmediateEffect(int number, int seconds, double power, Creature target) {
/* 103 */     Spell sp = Spells.getSpell(number);
/* 104 */     SpellEffects effs = target.getSpellEffects();
/* 105 */     if (effs == null)
/*     */     {
/* 107 */       effs = target.createSpellEffects();
/*     */     }
/* 109 */     SpellEffect eff = effs.getSpellEffect(sp.getEnchantment());
/* 110 */     if (eff == null) {
/*     */ 
/*     */       
/* 113 */       eff = new SpellEffect(target.getWurmId(), sp.getEnchantment(), (float)power, Math.max(1, seconds), (byte)9, sp.isOffensive() ? 1 : 0, true);
/*     */       
/* 115 */       effs.addSpellEffect(eff);
/*     */ 
/*     */     
/*     */     }
/* 119 */     else if (eff.getPower() < power) {
/*     */       
/* 121 */       eff.setPower((float)power);
/* 122 */       eff.setTimeleft(Math.max(eff.timeleft, Math.max(1, seconds)));
/* 123 */       target.sendUpdateSpellEffect(eff);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 136 */     SpellEffects effs = target.getSpellEffects();
/* 137 */     if (effs == null)
/*     */     {
/* 139 */       effs = target.createSpellEffects();
/*     */     }
/* 141 */     SpellEffect eff = effs.getSpellEffect(this.enchantment);
/* 142 */     if (eff == null) {
/*     */       
/* 144 */       if (target != performer) {
/* 145 */         performer.getCommunicator().sendNormalServerMessage(target.getNameWithGenus() + " now has " + this.effectdesc, (byte)2);
/*     */       }
/* 147 */       target.getCommunicator().sendNormalServerMessage("You now have " + this.effectdesc);
/*     */ 
/*     */       
/* 150 */       eff = new SpellEffect(target.getWurmId(), this.enchantment, (float)power, (int)Math.max(1.0D, Math.max(1.0D, power) * (this.durationModifier + performer.getNumLinks())), (byte)9, isOffensive() ? 1 : 0, true);
/*     */       
/* 152 */       effs.addSpellEffect(eff);
/* 153 */       Server.getInstance().broadCastAction(performer.getName() + " looks pleased.", performer, 5);
/*     */ 
/*     */     
/*     */     }
/* 157 */     else if (eff.getPower() > power) {
/*     */       
/* 159 */       performer.getCommunicator().sendNormalServerMessage("You frown as you fail to improve the power.", (byte)3);
/*     */       
/* 161 */       Server.getInstance().broadCastAction(performer.getName() + " frowns.", performer, 5);
/*     */     }
/*     */     else {
/*     */       
/* 165 */       if (target != performer) {
/* 166 */         performer.getCommunicator().sendNormalServerMessage("You succeed in improving the power of the " + this.name + ".", (byte)2);
/*     */       }
/*     */       
/* 169 */       target.getCommunicator().sendNormalServerMessage("You will now receive " + this.effectdesc);
/* 170 */       eff.setPower((float)power);
/* 171 */       eff.setTimeleft(Math.max(eff.timeleft, 
/* 172 */             (int)Math.max(1.0D, Math.max(1.0D, power) * (this.durationModifier + performer.getNumLinks()))));
/* 173 */       target.sendUpdateSpellEffect(eff);
/* 174 */       Server.getInstance().broadCastAction(performer.getName() + " looks pleased.", performer, 5);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\CreatureEnchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */