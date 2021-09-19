/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.bodys.Wound;
/*     */ import com.wurmonline.server.bodys.Wounds;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ 
/*     */ public final class Heal
/*     */   extends ReligiousSpell
/*     */ {
/*  41 */   private static Logger logger = Logger.getLogger(Heal.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 12;
/*     */ 
/*     */   
/*     */   Heal() {
/*  48 */     super("Heal", 249, 30, 40, 30, 40, 10000L);
/*  49 */     this.targetCreature = true;
/*  50 */     this.healing = true;
/*  51 */     this.description = "heals an extreme amount of damage";
/*  52 */     this.type = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/*  59 */     if (target.getBody() == null || target.getBody().getWounds() == null) {
/*     */       
/*  61 */       performer.getCommunicator().sendNormalServerMessage(target
/*  62 */           .getNameWithGenus() + " has no wounds to heal.", (byte)3);
/*     */       
/*  64 */       return false;
/*     */     } 
/*     */ 
/*     */     
/*  68 */     if (target.isReborn()) {
/*  69 */       return true;
/*     */     }
/*     */     
/*  72 */     if (!target.equals(performer)) {
/*     */ 
/*     */       
/*  75 */       if (target.isPlayer() && performer.getDeity() != null) {
/*     */ 
/*     */         
/*  78 */         if (!target.isFriendlyKingdom(performer.getKingdomId())) {
/*     */ 
/*     */           
/*  81 */           if (performer.faithful) {
/*     */             
/*  83 */             performer.getCommunicator().sendNormalServerMessage(performer
/*  84 */                 .getDeity().getName() + " would never accept that.", (byte)3);
/*     */             
/*  86 */             return false;
/*     */           } 
/*     */           
/*  89 */           return true;
/*     */         } 
/*     */         
/*  92 */         return true;
/*     */       } 
/*     */       
/*  95 */       return true;
/*     */     } 
/*     */     
/*  98 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 104 */     boolean doeff = true;
/* 105 */     if (target.isReborn()) {
/*     */       
/* 107 */       doeff = false;
/* 108 */       performer.getCommunicator().sendNormalServerMessage("You slay " + target.getNameWithGenus() + ".", (byte)4);
/*     */       
/* 110 */       Server.getInstance().broadCastAction(performer.getName() + " slays " + target.getNameWithGenus() + "!", performer, 5);
/* 111 */       target.addAttacker(performer);
/* 112 */       target.die(false, "Heal cast on Reborn");
/*     */     }
/* 114 */     else if (!target.equals(performer)) {
/*     */       
/* 116 */       if (target.isPlayer() && performer.getDeity() != null)
/*     */       {
/* 118 */         if (!target.isFriendlyKingdom(performer.getKingdomId())) {
/*     */           
/* 120 */           performer.getCommunicator().sendNormalServerMessage(performer
/* 121 */               .getDeity().getName() + " becomes very upset at the way you abuse " + performer
/* 122 */               .getDeity().getHisHerItsString() + " powers!", (byte)3);
/*     */ 
/*     */           
/*     */           try {
/* 126 */             performer.setFaith(performer.getFaith() / 2.0F);
/*     */           }
/* 128 */           catch (Exception ex) {
/*     */             
/* 130 */             logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 135 */     if (doeff) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 140 */       Wounds tWounds = target.getBody().getWounds();
/* 141 */       if (tWounds == null) {
/*     */         
/* 143 */         performer.getCommunicator().sendNormalServerMessage(target
/* 144 */             .getName() + " has no wounds to heal.", (byte)3);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 150 */       double resistance = SpellResist.getSpellResistance(target, getNumber());
/* 151 */       double healingPool = Math.max(20.0D, power) / 100.0D * 65535.0D * 2.0D;
/* 152 */       if (performer.getCultist() != null && performer.getCultist().healsFaster())
/*     */       {
/*     */         
/* 155 */         healingPool *= 2.0D;
/*     */       }
/* 157 */       healingPool *= resistance;
/*     */ 
/*     */       
/* 160 */       for (Wound w : tWounds.getWounds()) {
/* 161 */         if (w.getSeverity() <= healingPool) {
/*     */           
/* 163 */           healingPool -= w.getSeverity();
/* 164 */           SpellResist.addSpellResistance(target, getNumber(), w.getSeverity());
/* 165 */           w.heal();
/*     */         } 
/*     */       } 
/* 168 */       if ((tWounds.getWounds()).length > 0 && healingPool > 0.0D) {
/*     */ 
/*     */         
/* 171 */         SpellResist.addSpellResistance(target, getNumber(), healingPool);
/* 172 */         tWounds.getWounds()[Server.rand.nextInt((tWounds.getWounds()).length)].modifySeverity((int)-healingPool);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 178 */       if ((tWounds.getWounds()).length > 0) {
/*     */ 
/*     */         
/* 181 */         performer.getCommunicator().sendNormalServerMessage("You heal some of " + target.getNameWithGenus() + "'s wounds.", (byte)4);
/*     */         
/* 183 */         target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " heals some of your wounds.", (byte)4);
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 189 */         performer.getCommunicator().sendNormalServerMessage("You fully heal " + target.getNameWithGenus() + ".", (byte)4);
/*     */         
/* 191 */         target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " heals your wounds.", (byte)4);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 196 */       VolaTile t = Zones.getTileOrNull(target.getTileX(), target.getTileY(), target
/* 197 */           .isOnSurface());
/* 198 */       if (t != null)
/*     */       {
/* 200 */         t.sendAttachCreatureEffect(target, (byte)11, (byte)0, (byte)0, (byte)0, (byte)0);
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Heal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */