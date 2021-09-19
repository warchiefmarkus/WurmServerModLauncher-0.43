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
/*     */ public final class Courier
/*     */   extends ReligiousSpell
/*     */ {
/*     */   public static final int RANGE = 4;
/*     */   
/*     */   Courier() {
/*  37 */     super("Courier", 338, 30, 30, 20, 30, 0L);
/*  38 */     this.targetItem = true;
/*  39 */     this.enchantment = 20;
/*  40 */     this.effectdesc = "is possessed by some messenger spirits.";
/*  41 */     this.description = "tempts messenger spirits to inhabit the target and work for you";
/*  42 */     this.type = 1;
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
/*  53 */     if (!target.isMailBox() && !target.isSpringFilled() && !target.isUnenchantedTurret() && 
/*  54 */       !target.isPuppet() && !target.isEnchantedTurret()) {
/*     */       
/*  56 */       performer.getCommunicator().sendNormalServerMessage("The spell will not work on that.", (byte)3);
/*  57 */       return false;
/*     */     } 
/*     */ 
/*     */     
/*  61 */     SpellEffect negatingEffect = EnchantUtil.hasNegatingEffect(target, getEnchantment());
/*  62 */     if (negatingEffect != null) {
/*  63 */       EnchantUtil.sendNegatingEffectMessage(getName(), performer, target, negatingEffect);
/*  64 */       return false;
/*     */     } 
/*  66 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/*  76 */     return false;
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
/*  87 */     if ((!target.isMailBox() && !target.isSpringFilled() && !target.isPuppet() && 
/*  88 */       !target.isUnenchantedTurret() && !target.isEnchantedTurret()) || (target
/*  89 */       .hasDarkMessenger() && !target.isEnchantedTurret())) {
/*     */       
/*  91 */       performer.getCommunicator().sendNormalServerMessage("The spell fizzles.", (byte)3);
/*     */       return;
/*     */     } 
/*  94 */     if (target.isUnenchantedTurret() || target.isEnchantedTurret()) {
/*     */       
/*  96 */       int spirit = Zones.getSpiritsForTile(performer.getTileX(), performer.getTileY(), performer.isOnSurface());
/*  97 */       String sname = "no spirits";
/*  98 */       int templateId = 934;
/*  99 */       if (spirit == 4) {
/*     */         
/* 101 */         templateId = 942;
/* 102 */         sname = "There are plenty of air spirits at this height.";
/*     */       } 
/* 104 */       if (spirit == 2) {
/*     */         
/* 106 */         templateId = 968;
/* 107 */         sname = "Some water spirits were closeby.";
/*     */       } 
/* 109 */       if (spirit == 3) {
/*     */         
/* 111 */         templateId = 940;
/* 112 */         sname = "Earth spirits are everywhere below ground.";
/*     */       } 
/* 114 */       if (spirit == 1) {
/*     */         
/* 116 */         sname = "Some nearby fire spirits are drawn to your contraption.";
/* 117 */         templateId = 941;
/*     */       } 
/* 119 */       if (templateId == 934) {
/*     */         
/* 121 */         performer.getCommunicator().sendAlertServerMessage("There are no spirits nearby. Nothing happens.", (byte)3);
/*     */         
/*     */         return;
/*     */       } 
/* 125 */       if (target.isUnenchantedTurret()) {
/*     */         
/* 127 */         performer.getCommunicator().sendSafeServerMessage(sname);
/* 128 */         target.setTemplateId(templateId);
/* 129 */         target.setAuxData(performer.getKingdomId());
/*     */       }
/* 131 */       else if (target.isEnchantedTurret()) {
/*     */         
/* 133 */         if (target.getTemplateId() != templateId) {
/*     */           
/* 135 */           performer.getCommunicator().sendAlertServerMessage("The nearby spirits ignore your contraption. Nothing happens.", (byte)3);
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 140 */         performer.getCommunicator().sendSafeServerMessage(sname);
/*     */       } 
/*     */     } 
/*     */     
/* 144 */     ItemSpellEffects effs = target.getSpellEffects();
/* 145 */     if (effs == null)
/* 146 */       effs = new ItemSpellEffects(target.getWurmId()); 
/* 147 */     SpellEffect eff = effs.getSpellEffect(this.enchantment);
/* 148 */     if (eff == null) {
/*     */       
/* 150 */       performer.getCommunicator().sendNormalServerMessage("You summon nearby spirits into the " + target
/* 151 */           .getName() + ".", (byte)2);
/*     */       
/* 153 */       eff = new SpellEffect(target.getWurmId(), this.enchantment, (float)power, 20000000);
/* 154 */       effs.addSpellEffect(eff);
/* 155 */       Server.getInstance().broadCastAction(performer
/* 156 */           .getName() + " looks pleased as " + performer.getHeSheItString() + " summons some spirits into the " + target
/* 157 */           .getName() + ".", performer, 5);
/* 158 */       if (!target.isEnchantedTurret()) {
/* 159 */         target.setHasCourier(true);
/*     */       
/*     */       }
/*     */     }
/* 163 */     else if (eff.getPower() > power) {
/*     */       
/* 165 */       performer.getCommunicator().sendNormalServerMessage("You frown as you fail to summon more spirits into the " + target
/* 166 */           .getName() + ".", (byte)3);
/*     */       
/* 168 */       Server.getInstance().broadCastAction(performer.getName() + " frowns.", performer, 5);
/*     */     }
/*     */     else {
/*     */       
/* 172 */       performer.getCommunicator().sendNormalServerMessage("You succeed in summoning more spirits into the " + this.name + ".", (byte)2);
/*     */ 
/*     */       
/* 175 */       eff.improvePower(performer, (float)power);
/* 176 */       if (!target.isEnchantedTurret())
/* 177 */         target.setHasCourier(true); 
/* 178 */       Server.getInstance().broadCastAction(performer
/* 179 */           .getName() + " looks pleased as " + performer.getHeSheItString() + " summons some spirits into the " + target
/* 180 */           .getName() + ".", performer, 5);
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
/*     */   void doNegativeEffect(Skill castSkill, double power, Creature performer, Item target) {
/* 200 */     performer.getCommunicator().sendNormalServerMessage("The " + target
/* 201 */         .getName() + " emits a deep worrying sound of resonance, but stays intact.", (byte)3);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Courier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */