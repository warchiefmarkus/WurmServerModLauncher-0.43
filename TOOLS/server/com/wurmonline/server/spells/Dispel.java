/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.SpellEffects;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemSpellEffects;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.zones.AreaSpellEffect;
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
/*     */ public class Dispel
/*     */   extends ReligiousSpell
/*     */ {
/*     */   public static final int RANGE = 24;
/*     */   
/*     */   public Dispel() {
/*  39 */     super("Dispel", 450, 5, 10, 20, 10, 0L);
/*  40 */     this.targetTile = true;
/*  41 */     this.targetItem = true;
/*  42 */     this.targetCreature = true;
/*  43 */     this.description = "dispels an effect on the target";
/*  44 */     this.type = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/*  55 */     if (performer != target) {
/*  56 */       target.getCommunicator().sendCombatNormalMessage(performer.getNameWithGenus() + " dispels you!");
/*     */     }
/*     */     
/*  59 */     if (target.getSpellEffects() == null) {
/*     */       
/*  61 */       performer.getCommunicator().sendCombatNormalMessage(String.format("%s has no effects to dispel.", new Object[] { target.getName() }));
/*     */       
/*     */       return;
/*     */     } 
/*  65 */     SpellEffects effs = target.getSpellEffects();
/*  66 */     SpellEffect[] speffs = effs.getEffects();
/*  67 */     for (SpellEffect speff : speffs) {
/*     */ 
/*     */       
/*  70 */       if (speff.type != 64 && speff.type != 74 && speff.type != 73 && speff.type != 75) {
/*     */ 
/*     */ 
/*     */         
/*  74 */         double resistance = SpellResist.getSpellResistance(target, getNumber());
/*  75 */         double powerToRemove = power * resistance;
/*  76 */         SpellResist.addSpellResistance(target, getNumber(), powerToRemove);
/*     */ 
/*     */         
/*  79 */         if (powerToRemove < 1.0D) {
/*     */           
/*  81 */           performer.getCommunicator().sendCombatNormalMessage(String.format("%s resists your dispel!", new Object[] { target.getName() }));
/*     */           
/*     */           return;
/*     */         } 
/*  85 */         if (powerToRemove >= speff.getPower()) {
/*     */           
/*  87 */           effs.removeSpellEffect(speff);
/*  88 */           if (speff.type == 22 && 
/*  89 */             target.getCurrentTile() != null)
/*  90 */             target.getCurrentTile().setNewRarityShader(target); 
/*  91 */           performer.getCommunicator().sendCombatNormalMessage(String.format("You completely dispel %s from %s!", new Object[] { speff.getName(), target.getName() }));
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/*  96 */         speff.setPower((float)(speff.getPower() - powerToRemove));
/*  97 */         performer.getCommunicator().sendCombatNormalMessage(String.format("You partially dispel the %s from %s!", new Object[] { speff.getName(), target.getName() }));
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 102 */     performer.getCommunicator().sendCombatNormalMessage(String.format("%s has no effects to dispel.", new Object[] { target.getName() }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Item target) {
/* 113 */     if (!mayBeEnchanted(target)) {
/*     */       
/* 115 */       performer.getCommunicator().sendNormalServerMessage("The protection force is too high. You fail to dispel the " + target
/* 116 */           .getName() + ".", (byte)3);
/*     */       
/*     */       return;
/*     */     } 
/* 120 */     if (target.getOwnerId() != performer.getWurmId() && (target
/* 121 */       .getOwnerId() != -10L || target.getLastOwnerId() != performer.getWurmId()))
/*     */     {
/* 123 */       if (!performer.mayDestroy(target)) {
/*     */         
/* 125 */         performer.getCommunicator().sendNormalServerMessage("The protection force is too high. You fail to dispel the " + target
/* 126 */             .getName() + ".", (byte)3);
/*     */         
/*     */         return;
/*     */       } 
/*     */     }
/* 131 */     ItemSpellEffects effs = target.getSpellEffects();
/*     */     
/* 133 */     if (effs == null || (effs.getEffects()).length == 0)
/*     */     {
/* 135 */       if (target.enchantment == 0) {
/*     */         
/* 137 */         performer.getCommunicator().sendNormalServerMessage("Nothing seems to happen as you dispel " + target
/* 138 */             .getName() + ".", (byte)3);
/*     */         
/*     */         return;
/*     */       } 
/*     */     }
/* 143 */     if (target.enchantment != 0) {
/*     */       
/* 145 */       String enchName = "";
/* 146 */       int enchDifficulty = 60;
/* 147 */       Spell ench = Spells.getEnchantment(target.enchantment);
/* 148 */       if (ench != null) {
/* 149 */         enchName = ench.getName();
/* 150 */         enchDifficulty = ench.getDifficulty(true);
/*     */       } else {
/* 152 */         switch (target.enchantment) {
/*     */           case 90:
/* 154 */             enchName = "acid damage";
/*     */             break;
/*     */           case 91:
/* 157 */             enchName = "fire damage";
/*     */             break;
/*     */           case 92:
/* 160 */             enchName = "frost damage";
/*     */             break;
/*     */         } 
/*     */       } 
/* 164 */       if (enchName != "") {
/*     */         
/* 166 */         if (Server.rand.nextInt(enchDifficulty + 100) < power) {
/*     */           
/* 168 */           target.enchant((byte)0);
/* 169 */           performer.getCommunicator().sendNormalServerMessage("You remove the " + enchName + " enchantment.", (byte)2);
/*     */         }
/*     */         else {
/*     */           
/* 173 */           performer.getCommunicator().sendNormalServerMessage("Nothing seems to happen as you dispel " + target
/* 174 */               .getName() + ".", (byte)3);
/*     */         } 
/*     */         return;
/*     */       } 
/*     */     } 
/* 179 */     if (effs != null) {
/*     */       
/* 181 */       SpellEffect[] speffs = effs.getEffects();
/* 182 */       if (speffs.length > 0) {
/*     */         
/* 184 */         Spell ench = Spells.getEnchantment((speffs[0]).type);
/* 185 */         if (ench == null && Server.rand.nextInt(20 + (int)speffs[0].getPower()) < power) {
/*     */           
/* 187 */           performer.getCommunicator().sendNormalServerMessage("You remove the " + speffs[0].getName() + " imbuement.", (byte)2);
/*     */           
/* 189 */           effs.removeSpellEffect((speffs[0]).type);
/*     */         }
/* 191 */         else if (ench != null && Server.rand.nextInt(ench.getDifficulty(true) + (int)speffs[0].getPower()) < power) {
/*     */           
/* 193 */           performer.getCommunicator().sendNormalServerMessage("You remove the " + ench.getName() + " enchantment.", (byte)2);
/*     */           
/* 195 */           effs.removeSpellEffect((speffs[0]).type);
/* 196 */           if (target.isEnchantedTurret())
/*     */           {
/* 198 */             if ((speffs[0]).type == 20 || (speffs[0]).type == 44) {
/* 199 */               target.setTemplateId(934);
/*     */             }
/*     */           }
/*     */         } else {
/* 203 */           performer.getCommunicator().sendNormalServerMessage("Nothing seems to happen as you dispel " + target
/* 204 */               .getName() + ".", (byte)3);
/*     */         } 
/*     */         return;
/*     */       } 
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
/*     */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/* 220 */     AreaSpellEffect.removeAreaEffect(tilex, tiley, layer);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Dispel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */