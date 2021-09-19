/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.SpellEffects;
/*     */ import com.wurmonline.server.skills.Skill;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class KarmaEnchantment
/*     */   extends KarmaSpell
/*     */ {
/*  35 */   float durationModifier = 20.0F;
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
/*     */   public KarmaEnchantment(String aName, int aNum, int aCastingTime, int aCost, int aDifficulty, int aLevel, long aCooldown) {
/*  49 */     super(aName, aNum, aCastingTime, aCost, aDifficulty, aLevel, aCooldown);
/*  50 */     this.targetCreature = true;
/*  51 */     this.targetTile = true;
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
/*  62 */     if (target != null) {
/*     */       
/*  64 */       if (target.isReborn()) {
/*     */         
/*  66 */         performer.getCommunicator().sendNormalServerMessage(target.getName() + " doesn't seem affected.", (byte)3);
/*     */         
/*  68 */         return true;
/*     */       } 
/*  70 */       if (!target.equals(performer)) {
/*     */         
/*  72 */         if (!this.offensive && target.getKingdomId() != 0 && 
/*  73 */           !performer.isFriendlyKingdom(target.getKingdomId())) {
/*     */           
/*  75 */           performer.getCommunicator().sendNormalServerMessage("Nothing happens as you try to cast this on an enemy.", (byte)3);
/*     */ 
/*     */           
/*  78 */           return false;
/*     */         } 
/*  80 */         if (performer.getDeity() != null) {
/*     */           
/*  82 */           if (target.getDeity() != null) {
/*     */             
/*  84 */             if (!this.offensive && !performer.getDeity().accepts(target.getDeity().getAlignment())) {
/*     */               
/*  86 */               performer.getCommunicator().sendNormalServerMessage(performer
/*  87 */                   .getDeity().getName() + " would never help the infidel " + target.getName() + ".", (byte)3);
/*     */               
/*  89 */               return false;
/*     */             } 
/*     */             
/*  92 */             return true;
/*     */           } 
/*     */           
/*  95 */           return true;
/*     */         } 
/*     */         
/*  98 */         return true;
/*     */       } 
/*     */       
/* 101 */       return true;
/*     */     } 
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void doImmediateEffect(int number, int seconds, double power, Creature target) {
/* 108 */     Spell sp = Spells.getSpell(number);
/* 109 */     SpellEffects effs = target.getSpellEffects();
/* 110 */     if (effs == null)
/*     */     {
/* 112 */       effs = target.createSpellEffects();
/*     */     }
/* 114 */     SpellEffect eff = effs.getSpellEffect(sp.getEnchantment());
/* 115 */     if (eff == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 120 */       eff = new SpellEffect(target.getWurmId(), sp.getEnchantment(), (sp.getEnchantment() == 68) ? 100.0F : (float)power, Math.max(1, seconds), (byte)9, sp.isOffensive() ? 1 : 0, true);
/*     */       
/* 122 */       effs.addSpellEffect(eff);
/*     */ 
/*     */     
/*     */     }
/* 126 */     else if (eff.getPower() < power) {
/*     */       
/* 128 */       eff.setPower((sp.getEnchantment() == 68) ? 100.0F : (float)power);
/*     */       
/* 130 */       eff.setTimeleft(Math.max(eff.timeleft, Math.max(1, seconds)));
/* 131 */       target.sendUpdateSpellEffect(eff);
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
/* 144 */     SpellEffects effs = target.getSpellEffects();
/* 145 */     if (effs == null)
/*     */     {
/* 147 */       effs = target.createSpellEffects();
/*     */     }
/* 149 */     SpellEffect eff = effs.getSpellEffect(this.enchantment);
/* 150 */     if (eff == null) {
/*     */       
/* 152 */       if (target != performer) {
/* 153 */         performer.getCommunicator().sendNormalServerMessage(target.getName() + " now has " + this.effectdesc, (byte)2);
/*     */       }
/* 155 */       target.getCommunicator().sendNormalServerMessage("You now have " + this.effectdesc, (byte)2);
/*     */ 
/*     */ 
/*     */       
/* 159 */       eff = new SpellEffect(target.getWurmId(), this.enchantment, (this.enchantment == 68) ? 100.0F : (float)power, (int)(Math.max(1.0D, power) * (this.durationModifier + performer.getNumLinks())), (byte)9, isOffensive() ? 1 : 0, true);
/*     */       
/* 161 */       effs.addSpellEffect(eff);
/* 162 */       Server.getInstance().broadCastAction(performer.getName() + " looks pleased.", performer, 5);
/*     */ 
/*     */     
/*     */     }
/* 166 */     else if (eff.getPower() > power) {
/*     */       
/* 168 */       performer.getCommunicator().sendNormalServerMessage("You frown as you fail to improve the power.", (byte)3);
/*     */       
/* 170 */       Server.getInstance().broadCastAction(performer.getName() + " frowns.", performer, 5);
/*     */     }
/*     */     else {
/*     */       
/* 174 */       if (target != performer) {
/* 175 */         performer.getCommunicator().sendNormalServerMessage("You succeed in improving the power of the " + this.name + ".", (byte)2);
/*     */       }
/* 177 */       target.getCommunicator().sendNormalServerMessage("You will now receive improved " + this.effectdesc, (byte)2);
/*     */       
/* 179 */       eff.setPower((float)power);
/* 180 */       eff.setTimeleft(Math.max(eff.timeleft, 
/*     */             
/* 182 */             (int)(Math.max(1.0D, (this.enchantment == 68) ? 100.0D : power) * (this.durationModifier + performer.getNumLinks()))));
/* 183 */       target.sendUpdateSpellEffect(eff);
/* 184 */       Server.getInstance().broadCastAction(performer.getName() + " looks pleased.", performer, 5);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\KarmaEnchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */