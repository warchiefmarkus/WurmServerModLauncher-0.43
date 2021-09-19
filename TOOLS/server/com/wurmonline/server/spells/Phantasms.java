/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.MessageServer;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.SpellEffects;
/*     */ import com.wurmonline.server.creatures.ai.scripts.UtilitiesAOE;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.utils.CreatureLineSegment;
/*     */ import com.wurmonline.shared.util.MulticolorLineSegment;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
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
/*     */ public class Phantasms
/*     */   extends ReligiousSpell
/*     */ {
/*     */   public static final int RANGE = 50;
/*     */   
/*     */   public Phantasms() {
/*  46 */     super("Phantasms", 426, 10, 10, 10, 20, 0L);
/*  47 */     this.targetCreature = true;
/*  48 */     this.enchantment = 43;
/*  49 */     this.offensive = true;
/*  50 */     this.effectdesc = "confusion and muddled thoughts.";
/*  51 */     this.description = "confuses the target and may make them attack something else";
/*  52 */     this.type = 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void doImmediateEffect(double power, Creature target) {
/*  57 */     Spell ph = Spells.getSpell(426);
/*  58 */     SpellEffects effs = target.getSpellEffects();
/*  59 */     if (effs == null)
/*     */     {
/*  61 */       effs = target.createSpellEffects();
/*     */     }
/*  63 */     SpellEffect eff = effs.getSpellEffect(ph.getEnchantment());
/*  64 */     if (eff == null) {
/*     */       
/*  66 */       eff = new SpellEffect(target.getWurmId(), ph.getEnchantment(), (float)power, 310, (byte)9, (byte)1, true);
/*     */       
/*  68 */       effs.addSpellEffect(eff);
/*     */ 
/*     */     
/*     */     }
/*  72 */     else if (eff.getPower() < power) {
/*     */       
/*  74 */       eff.setPower((float)power);
/*  75 */       eff.setTimeleft(310);
/*  76 */       target.sendUpdateSpellEffect(eff);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/*  84 */     if (target.isPlayer()) {
/*     */       
/*  86 */       SpellEffects effs = target.getSpellEffects();
/*  87 */       if (effs == null)
/*     */       {
/*  89 */         effs = target.createSpellEffects();
/*     */       }
/*  91 */       SpellEffect eff = effs.getSpellEffect(this.enchantment);
/*  92 */       if (eff == null)
/*     */       {
/*  94 */         performer.getCommunicator().sendNormalServerMessage(target.getName() + " will now receive " + this.effectdesc, (byte)2);
/*     */         
/*  96 */         eff = new SpellEffect(target.getWurmId(), this.enchantment, (float)power, 300 + performer.getNumLinks() * 10, (byte)9, (byte)1, true);
/*     */         
/*  98 */         effs.addSpellEffect(eff);
/*     */ 
/*     */       
/*     */       }
/* 102 */       else if (eff.getPower() > power)
/*     */       {
/* 104 */         performer.getCommunicator().sendNormalServerMessage("You frown as you fail to improve the power.", (byte)3);
/*     */       
/*     */       }
/*     */       else
/*     */       {
/* 109 */         performer.getCommunicator().sendNormalServerMessage("You succeed in improving the power of the " + this.name + ".", (byte)2);
/*     */         
/* 111 */         eff.setPower((float)power);
/* 112 */         eff.setTimeleft(Math.max(eff.timeleft, 300 + performer.getNumLinks() * 10));
/* 113 */         target.sendUpdateSpellEffect(eff);
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 120 */       HashSet<Creature> nearbyCreatures = UtilitiesAOE.getRadialAreaCreatures(target, (target.getTemplate().getVision() * 4));
/*     */       
/* 122 */       Creature testCret = null;
/* 123 */       for (Iterator<Creature> it = nearbyCreatures.iterator(); it.hasNext(); testCret = it.next()) {
/*     */         
/* 125 */         if (testCret != null)
/*     */         {
/*     */           
/* 128 */           if (testCret.getPower() > 0 || testCret.isUnique()) {
/*     */             
/* 130 */             it.remove();
/*     */ 
/*     */           
/*     */           }
/* 134 */           else if (testCret == target.getTarget() || testCret == performer || testCret.isRidden()) {
/*     */             
/* 136 */             it.remove();
/*     */           }
/*     */           else {
/*     */             
/* 140 */             byte att = target.getAttitude(testCret);
/* 141 */             if (att == 1 || att == 7 || att == 5) {
/*     */ 
/*     */               
/* 144 */               it.remove();
/*     */             } else {
/*     */               
/* 147 */               byte casterAtt = performer.getAttitude(testCret);
/* 148 */               if (casterAtt == 1 || casterAtt == 7 || casterAtt == 5)
/*     */               {
/*     */                 
/* 151 */                 it.remove(); } 
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/* 156 */       if (nearbyCreatures.size() > 0) {
/*     */         
/* 158 */         int currentCret = 0;
/* 159 */         int targetCret = Server.rand.nextInt(nearbyCreatures.size());
/* 160 */         for (Creature c : nearbyCreatures)
/*     */         {
/* 162 */           if (currentCret == targetCret) {
/*     */             
/* 164 */             target.removeTarget(target.target);
/* 165 */             target.setTarget(c.getWurmId(), true);
/* 166 */             target.setOpponent(c);
/*     */             
/* 168 */             performer.getCommunicator().sendNormalServerMessage(target.getName() + " starts to see phantasms  and turns towards " + c
/* 169 */                 .getName() + " in anger.", (byte)2);
/*     */             
/* 171 */             ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 172 */             segments.add(new CreatureLineSegment(target));
/* 173 */             segments.add(new MulticolorLineSegment(" starts to see phantasms and turns to attack ", (byte)0));
/* 174 */             segments.add(new CreatureLineSegment(c));
/* 175 */             segments.add(new MulticolorLineSegment(" in anger.", (byte)0));
/*     */             
/* 177 */             MessageServer.broadcastColoredAction(segments, performer, target, 5, true);
/*     */             continue;
/*     */           } 
/* 180 */           currentCret++;
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 185 */       else if (target.getTarget() == null) {
/* 186 */         performer.getCommunicator().sendNormalServerMessage(target.getName() + " starts to see phantasms  but cannot find the source of them.", (byte)2);
/*     */       }
/* 188 */       else if (target.getTarget() == performer) {
/* 189 */         performer.getCommunicator().sendNormalServerMessage(target.getName() + " starts to see phantasms  but can only see you as being the cause.", (byte)2);
/*     */       } else {
/*     */         
/* 192 */         performer.getCommunicator().sendNormalServerMessage(target.getName() + " starts to see phantasms  but can only see " + target
/* 193 */             .getTarget().getName() + " as being the cause.", (byte)2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Phantasms.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */