/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.behaviours.Actions;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.zones.VolaTile;
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
/*     */ public class KarmaMissile
/*     */   extends KarmaSpell
/*     */ {
/*  40 */   private static final Logger logger = Logger.getLogger(KarmaMissile.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 24;
/*     */ 
/*     */   
/*     */   public KarmaMissile() {
/*  47 */     super("Karma Missile", 551, 15, 300, 15, 1, 60000L);
/*  48 */     this.targetCreature = true;
/*     */     
/*  50 */     this.description = "sends a flurry of negative energy missiles towards the target";
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
/*  61 */     if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2 && 
/*  62 */       !performer.getDeity().isHateGod())
/*     */     {
/*  64 */       if (performer.faithful)
/*     */       {
/*  66 */         if (!performer.isDuelOrSpar(target)) {
/*     */           
/*  68 */           performer.getCommunicator().sendNormalServerMessage(performer
/*  69 */               .getDeity().getName() + " would never accept your attack on " + target.getName() + ".", (byte)3);
/*     */           
/*  71 */           return false;
/*     */         } 
/*     */       }
/*     */     }
/*  75 */     return true;
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
/*  86 */     if (!target.isUnique() || power > 99.0D) {
/*     */       
/*  88 */       if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2 && 
/*  89 */         !performer.getDeity().isHateGod())
/*     */       {
/*  91 */         if (!performer.isDuelOrSpar(target))
/*     */         {
/*  93 */           performer.modifyFaith(-(100.0F - performer.getFaith()) / 50.0F);
/*     */         }
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/*  99 */         sendMissile(performer, target, 0.0F, 0.0F, 0.0F, power);
/* 100 */         double attPower = rollAttack(performer, castSkill, target);
/* 101 */         if (attPower > 0.0D)
/* 102 */           sendMissile(performer, target, 0.0F, 0.0F, -0.5F, attPower); 
/* 103 */         attPower = rollAttack(performer, castSkill, target);
/* 104 */         if (attPower > 0.0D)
/* 105 */           sendMissile(performer, target, 0.0F, 0.0F, 0.5F, attPower); 
/* 106 */         attPower = rollAttack(performer, castSkill, target);
/* 107 */         if (attPower > 0.0D)
/* 108 */           sendMissile(performer, target, 0.5F, 0.5F, 0.5F, attPower); 
/* 109 */         attPower = rollAttack(performer, castSkill, target);
/* 110 */         if (attPower > 0.0D) {
/* 111 */           sendMissile(performer, target, -0.5F, -0.5F, 0.5F, attPower);
/*     */         }
/* 113 */       } catch (Exception exe) {
/*     */         
/* 115 */         logger.log(Level.WARNING, exe.getMessage(), exe);
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 121 */       performer.getCommunicator().sendNormalServerMessage("You try to missile " + target
/* 122 */           .getName() + " but fail.", (byte)3);
/* 123 */       target.getCommunicator().sendNormalServerMessage(performer.getName() + " tries to missile you but fails.", (byte)4);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final double rollAttack(Creature performer, Skill castSkill, Creature target) {
/* 130 */     double distDiff = 0.0D;
/* 131 */     double dist = Creature.getRange(performer, target.getPosX(), target.getPosY());
/*     */     
/*     */     try {
/* 134 */       distDiff = dist - (Actions.actionEntrys[this.number].getRange() / 2.0F);
/*     */ 
/*     */       
/* 137 */       if (distDiff > 0.0D) {
/* 138 */         distDiff *= 2.0D;
/*     */       }
/* 140 */     } catch (Exception ex) {
/*     */       
/* 142 */       logger.log(Level.WARNING, getName() + " error: " + ex.getMessage());
/*     */     } 
/* 144 */     return trimPower(performer, Math.max((Server.rand
/* 145 */           .nextFloat() * 10.0F), castSkill
/* 146 */           .skillCheck(distDiff + this.difficulty, performer.zoneBonus, true, 1.0F)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void sendMissile(Creature performer, Creature target, float offx, float offy, float offz, double power) throws Exception {
/* 153 */     VolaTile t = performer.getCurrentTile();
/* 154 */     long shardId = WurmId.getNextTempItemId();
/* 155 */     if (t != null)
/*     */     {
/* 157 */       t.sendProjectile(shardId, (byte)4, "model.spell.ShardOfIce", "Karma Missile", (byte)0, performer
/* 158 */           .getPosX() + offx, performer.getPosY() + offy, performer.getPositionZ() + performer
/* 159 */           .getAltOffZ() + offz, performer
/*     */           
/* 161 */           .getStatus().getRotation(), (byte)performer.getLayer(), (int)target.getPosX(), 
/* 162 */           (int)target.getPosY(), target.getPositionZ() + target.getAltOffZ(), performer.getWurmId(), target
/* 163 */           .getWurmId(), 0.0F, 0.0F);
/*     */     }
/*     */     
/* 166 */     t = target.getCurrentTile();
/* 167 */     if (t != null)
/*     */     {
/* 169 */       t.sendProjectile(shardId, (byte)4, "model.spell.ShardOfIce", "Karma Missile", (byte)0, performer
/* 170 */           .getPosX() + offx, performer.getPosY() + offy, performer.getPositionZ() + performer
/* 171 */           .getAltOffZ() + offz, performer
/*     */           
/* 173 */           .getStatus().getRotation(), (byte)performer.getLayer(), (int)target.getPosX(), 
/* 174 */           (int)target.getPosY(), target.getPositionZ() + target.getAltOffZ(), performer.getWurmId(), target
/* 175 */           .getWurmId(), 0.0F, 0.0F);
/*     */     }
/*     */     
/* 178 */     byte pos = target.getBody().getRandomWoundPos();
/* 179 */     double damage = 2500.0D + 3500.0D * power / 100.0D;
/* 180 */     if (performer.getPower() > 1 && Servers.isThisATestServer())
/* 181 */       performer.getCommunicator().sendNormalServerMessage("Damage: " + damage); 
/* 182 */     target.addWoundOfType(performer, (byte)10, pos, false, 1.0F, true, damage, 0.0F, 0.0F, false, true);
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
/*     */   void doNegativeEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 194 */     performer.getCommunicator().sendNormalServerMessage("You try to send negative karma to " + target
/* 195 */         .getName() + " but fail.", (byte)3);
/* 196 */     target.getCommunicator().sendNormalServerMessage(performer.getName() + " tries to give you negative karma but fails.", (byte)4);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\KarmaMissile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */