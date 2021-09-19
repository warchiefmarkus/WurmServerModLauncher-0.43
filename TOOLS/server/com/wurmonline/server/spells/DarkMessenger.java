/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemSpellEffects;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.zones.Zones;
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
/*     */ public final class DarkMessenger
/*     */   extends ReligiousSpell
/*     */ {
/*     */   public static final int RANGE = 4;
/*     */   
/*     */   DarkMessenger() {
/*  38 */     super("Dark Messenger", 339, 30, 30, 40, 30, 0L);
/*  39 */     this.targetItem = true;
/*  40 */     this.enchantment = 44;
/*  41 */     this.effectdesc = "is possessed by some evil messenger spirits.";
/*  42 */     this.description = "tricks evil messenger spirits to inhabit the target and work for you";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, Item target) {
/*  53 */     if (!target.isMailBox() && !target.isSpringFilled() && !target.isPuppet() && 
/*  54 */       !target.isUnenchantedTurret() && !target.isEnchantedTurret()) {
/*     */       
/*  56 */       performer.getCommunicator().sendNormalServerMessage("The spell will not work on that.", (byte)3);
/*     */       
/*  58 */       return false;
/*     */     } 
/*     */ 
/*     */     
/*  62 */     SpellEffect negatingEffect = EnchantUtil.hasNegatingEffect(target, getEnchantment());
/*  63 */     if (negatingEffect != null) {
/*  64 */       EnchantUtil.sendNegatingEffectMessage(getName(), performer, target, negatingEffect);
/*  65 */       return false;
/*     */     } 
/*  67 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/*  77 */     return false;
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
/*  88 */     if ((!target.isMailBox() && !target.isSpringFilled() && !target.isPuppet() && 
/*  89 */       !target.isUnenchantedTurret() && !target.isEnchantedTurret()) || (target
/*  90 */       .hasCourier() && !target.isEnchantedTurret())) {
/*     */       
/*  92 */       performer.getCommunicator().sendNormalServerMessage("The spell fizzles.", (byte)3);
/*     */       return;
/*     */     } 
/*  95 */     if (target.isUnenchantedTurret() || target.isEnchantedTurret()) {
/*     */       
/*  97 */       int spirit = Zones.getSpiritsForTile(performer.getTileX(), performer.getTileY(), performer.isOnSurface());
/*  98 */       String sname = "no demoniacs";
/*  99 */       int templateId = 934;
/* 100 */       if (spirit == 4) {
/*     */         
/* 102 */         templateId = 942;
/* 103 */         sname = "There are plenty of air demoniacs at this height.";
/*     */       } 
/* 105 */       if (spirit == 2) {
/*     */         
/* 107 */         templateId = 968;
/* 108 */         sname = "Some water demoniacs were closeby.";
/*     */       } 
/* 110 */       if (spirit == 3) {
/*     */         
/* 112 */         templateId = 940;
/* 113 */         sname = "Earth demoniacs are everywhere below ground.";
/*     */       } 
/* 115 */       if (spirit == 1) {
/*     */         
/* 117 */         sname = "Some nearby fire demoniacs are drawn to your contraption.";
/* 118 */         templateId = 941;
/*     */       } 
/* 120 */       if (templateId == 934) {
/*     */         
/* 122 */         performer.getCommunicator().sendAlertServerMessage("There are no demoniacs nearby. Nothing happens.", (byte)3);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 127 */       if (target.isUnenchantedTurret()) {
/*     */         
/* 129 */         performer.getCommunicator().sendSafeServerMessage(sname);
/* 130 */         target.setTemplateId(templateId);
/* 131 */         target.setAuxData(performer.getKingdomId());
/*     */       }
/* 133 */       else if (target.isEnchantedTurret()) {
/*     */         
/* 135 */         if (target.getTemplateId() != templateId) {
/*     */           
/* 137 */           performer.getCommunicator().sendAlertServerMessage("The nearby demoniacs ignore your contraption. Nothing happens.", (byte)3);
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 142 */         performer.getCommunicator().sendSafeServerMessage(sname);
/*     */       } 
/*     */     } 
/*     */     
/* 146 */     ItemSpellEffects effs = target.getSpellEffects();
/* 147 */     if (effs == null)
/* 148 */       effs = new ItemSpellEffects(target.getWurmId()); 
/* 149 */     SpellEffect eff = effs.getSpellEffect(this.enchantment);
/* 150 */     if (eff == null) {
/*     */       
/* 152 */       performer.getCommunicator().sendNormalServerMessage("You summon some small demoniacs into the " + target
/* 153 */           .getName() + ".", (byte)2);
/*     */       
/* 155 */       eff = new SpellEffect(target.getWurmId(), this.enchantment, (float)power, 20000000);
/* 156 */       effs.addSpellEffect(eff);
/* 157 */       Server.getInstance().broadCastAction(performer
/* 158 */           .getName() + " looks pleased as " + performer.getHeSheItString() + " summons small demoniacs that disappear into the " + target
/* 159 */           .getName() + ".", performer, 5);
/*     */       
/* 161 */       if (!target.isEnchantedTurret()) {
/* 162 */         target.setHasDarkMessenger(true);
/*     */       
/*     */       }
/*     */     }
/* 166 */     else if (eff.getPower() > power) {
/*     */       
/* 168 */       performer.getCommunicator().sendNormalServerMessage("You frown as you fail to summon more demoniacs into the " + target
/* 169 */           .getName() + ".", (byte)3);
/*     */       
/* 171 */       Server.getInstance().broadCastAction(performer.getName() + " frowns.", performer, 5);
/*     */     }
/*     */     else {
/*     */       
/* 175 */       performer.getCommunicator().sendNormalServerMessage("You succeed in summoning more demoniacs into the " + this.name + ".", (byte)2);
/*     */ 
/*     */       
/* 178 */       eff.improvePower(performer, (float)power);
/* 179 */       if (!target.isEnchantedTurret())
/* 180 */         target.setHasDarkMessenger(true); 
/* 181 */       Server.getInstance().broadCastAction(performer
/* 182 */           .getName() + " looks pleased as " + performer.getHeSheItString() + " summons small demoniacs that disappear into the " + target
/* 183 */           .getName() + ".", performer, 5);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doNegativeEffect(Skill castSkill, double power, Creature performer, Item target) {
/* 204 */     performer.getCommunicator().sendNormalServerMessage("The " + target
/* 205 */         .getName() + " emits a deep worrying sound of resonance, but stays intact.", (byte)3);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\DarkMessenger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */