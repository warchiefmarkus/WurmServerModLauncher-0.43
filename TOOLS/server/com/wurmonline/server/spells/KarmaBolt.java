/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.WurmId;
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
/*     */ 
/*     */ public class KarmaBolt
/*     */   extends KarmaSpell
/*     */ {
/*  38 */   private static final Logger logger = Logger.getLogger(KarmaMissile.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 24;
/*     */ 
/*     */   
/*     */   public KarmaBolt() {
/*  45 */     super("Karma Bolt", 550, 10, 200, 10, 1, 180000L);
/*  46 */     this.targetCreature = true;
/*  47 */     this.offensive = true;
/*     */     
/*  49 */     this.description = "sends a thick bolt of negative Karma towards the target";
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
/*  60 */     if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2 && 
/*  61 */       !performer.getDeity().isHateGod())
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
/*  85 */     if (!target.isUnique() || power > 99.0D) {
/*     */       
/*  87 */       if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2 && 
/*  88 */         !performer.getDeity().isHateGod())
/*     */       {
/*  90 */         if (!performer.isDuelOrSpar(target))
/*     */         {
/*  92 */           performer.modifyFaith(-(100.0F - performer.getFaith()) / 50.0F);
/*     */         }
/*     */       }
/*     */       
/*     */       try {
/*  97 */         sendBolt(performer, target, 0.0F, 0.0F, 0.0F, power);
/*     */       }
/*  99 */       catch (Exception exe) {
/*     */         
/* 101 */         logger.log(Level.WARNING, exe.getMessage(), exe);
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 107 */       performer.getCommunicator().sendNormalServerMessage("You try to bolt " + target
/* 108 */           .getName() + " but fail.", (byte)3);
/* 109 */       target.getCommunicator().sendNormalServerMessage(performer.getName() + " tries to bolt you but fails.", (byte)4);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void sendBolt(Creature performer, Creature target, float offx, float offy, float offz, double power) throws Exception {
/* 118 */     VolaTile t = performer.getCurrentTile();
/* 119 */     long shardId = WurmId.getNextTempItemId();
/* 120 */     if (t != null)
/*     */     {
/* 122 */       t.sendProjectile(shardId, (byte)4, "model.spell.ShardOfIce", "Karma Bolt", (byte)0, performer
/* 123 */           .getPosX() + offx, performer.getPosY() + offy, performer.getPositionZ() + performer
/* 124 */           .getAltOffZ() + offz, performer
/*     */           
/* 126 */           .getStatus().getRotation(), (byte)performer.getLayer(), (int)target.getPosX(), 
/* 127 */           (int)target.getPosY(), target.getPositionZ() + target.getAltOffZ(), performer.getWurmId(), target
/* 128 */           .getWurmId(), 0.0F, 0.0F);
/*     */     }
/*     */     
/* 131 */     t = target.getCurrentTile();
/* 132 */     if (t != null)
/*     */     {
/* 134 */       t.sendProjectile(shardId, (byte)4, "model.spell.ShardOfIce", "Karma Bolt", (byte)0, performer
/* 135 */           .getPosX() + offx, performer.getPosY() + offy, performer.getPositionZ() + performer
/* 136 */           .getAltOffZ() + offz, performer
/*     */           
/* 138 */           .getStatus().getRotation(), (byte)performer.getLayer(), (int)target.getPosX(), 
/* 139 */           (int)target.getPosY(), target.getPositionZ() + target.getAltOffZ(), performer.getWurmId(), target
/* 140 */           .getWurmId(), 0.0F, 0.0F);
/*     */     }
/*     */     
/* 143 */     double damage = 5000.0D + 5000.0D * power / 100.0D;
/* 144 */     target.addWoundOfType(performer, (byte)6, 1, true, 1.0F, true, damage, 20.0F, 0.0F, false, true);
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
/* 156 */     performer.getCommunicator().sendNormalServerMessage("You try to send negative karma to " + target
/* 157 */         .getName() + " but fail.", (byte)3);
/*     */     
/* 159 */     target.getCommunicator().sendNormalServerMessage(performer.getName() + " tries to give you negative karma but fails.", (byte)4);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\KarmaBolt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */