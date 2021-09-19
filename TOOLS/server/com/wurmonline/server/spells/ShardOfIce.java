/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.shared.constants.AttitudeConstants;
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
/*     */ public class ShardOfIce
/*     */   extends DamageSpell
/*     */   implements AttitudeConstants
/*     */ {
/*  35 */   private static Logger logger = Logger.getLogger(FireHeart.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 50;
/*     */   
/*     */   public static final double BASE_DAMAGE = 5000.0D;
/*     */   
/*     */   public static final double DAMAGE_PER_POWER = 120.0D;
/*     */ 
/*     */   
/*     */   public ShardOfIce() {
/*  46 */     super("Shard of Ice", 485, 7, 20, 30, 35, 30000L);
/*  47 */     this.targetCreature = true;
/*  48 */     this.offensive = true;
/*  49 */     this.description = "damages the targets body with a spear of ice causing frost damage";
/*  50 */     this.type = 2;
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
/*  61 */     if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2)
/*     */     {
/*  63 */       if (performer.faithful)
/*     */       {
/*  65 */         if (!performer.isDuelOrSpar(target)) {
/*     */           
/*  67 */           performer.getCommunicator().sendNormalServerMessage(performer
/*  68 */               .getDeity().getName() + " would never accept your attack on " + target.getName() + ".", (byte)3);
/*     */           
/*  70 */           return false;
/*     */         } 
/*     */       }
/*     */     }
/*  74 */     return true;
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
/*  85 */     if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2)
/*     */     {
/*  87 */       if (!performer.isDuelOrSpar(target))
/*     */       {
/*  89 */         performer.modifyFaith(-(100.0F - performer.getFaith()) / 50.0F);
/*     */       }
/*     */     }
/*     */     
/*     */     try {
/*  94 */       VolaTile t = performer.getCurrentTile();
/*  95 */       long shardId = WurmId.getNextTempItemId();
/*  96 */       if (t != null)
/*     */       {
/*  98 */         t.sendProjectile(shardId, (byte)4, "model.spell.ShardOfIce", "Shard Of Ice", (byte)0, performer
/*  99 */             .getPosX(), performer.getPosY(), performer.getPositionZ() + performer
/* 100 */             .getAltOffZ(), performer
/* 101 */             .getStatus().getRotation(), (byte)performer.getLayer(), (int)target.getPosX(), 
/* 102 */             (int)target.getPosY(), target.getPositionZ() + target.getAltOffZ(), performer.getWurmId(), target
/* 103 */             .getWurmId(), 0.0F, 0.0F);
/*     */       }
/*     */       
/* 106 */       t = target.getCurrentTile();
/* 107 */       if (t != null)
/*     */       {
/* 109 */         t.sendProjectile(shardId, (byte)4, "model.spell.ShardOfIce", "Shard Of Ice", (byte)0, performer
/* 110 */             .getPosX(), performer.getPosY(), performer.getPositionZ() + performer
/* 111 */             .getAltOffZ(), performer
/* 112 */             .getStatus().getRotation(), (byte)performer.getLayer(), (int)target.getPosX(), 
/* 113 */             (int)target.getPosY(), target.getPositionZ() + target.getAltOffZ(), performer.getWurmId(), target
/* 114 */             .getWurmId(), 0.0F, 0.0F);
/*     */       }
/*     */       
/* 117 */       byte pos = target.getBody().getCenterWoundPos();
/*     */       
/* 119 */       double damage = calculateDamage(target, power, 5000.0D, 120.0D);
/*     */       
/* 121 */       target.addWoundOfType(performer, (byte)8, pos, false, 1.0F, false, damage, 0.0F, 0.0F, false, true);
/*     */     
/*     */     }
/* 124 */     catch (Exception exe) {
/*     */       
/* 126 */       logger.log(Level.WARNING, exe.getMessage(), exe);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\ShardOfIce.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */