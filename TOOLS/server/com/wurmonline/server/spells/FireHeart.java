/*     */ package com.wurmonline.server.spells;
/*     */ 
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
/*     */ public class FireHeart
/*     */   extends DamageSpell
/*     */   implements AttitudeConstants
/*     */ {
/*  34 */   private static Logger logger = Logger.getLogger(FireHeart.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 50;
/*     */   
/*     */   public static final double BASE_DAMAGE = 9000.0D;
/*     */   
/*     */   public static final double DAMAGE_PER_POWER = 80.0D;
/*     */ 
/*     */   
/*     */   public FireHeart() {
/*  45 */     super("Fireheart", 424, 7, 20, 20, 35, 30000L);
/*  46 */     this.targetCreature = true;
/*  47 */     this.offensive = true;
/*  48 */     this.description = "damages the targets heart with superheated fire";
/*  49 */     this.type = 2;
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
/*  60 */     if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2)
/*     */     {
/*  62 */       if (performer.faithful)
/*     */       {
/*  64 */         if (!performer.isDuelOrSpar(target)) {
/*     */           
/*  66 */           performer.getCommunicator().sendNormalServerMessage(performer
/*  67 */               .getDeity().getName() + " would never accept your attack on " + target.getNameWithGenus() + ".", (byte)3);
/*     */           
/*  69 */           return false;
/*     */         } 
/*     */       }
/*     */     }
/*  73 */     return true;
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
/*  84 */     if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2)
/*     */     {
/*  86 */       if (!performer.isDuelOrSpar(target))
/*     */       {
/*  88 */         performer.modifyFaith(-5.0F);
/*     */       }
/*     */     }
/*  91 */     VolaTile t = target.getCurrentTile();
/*  92 */     if (t != null) {
/*     */       
/*  94 */       t.sendAddQuickTileEffect((byte)35, target.getFloorLevel());
/*  95 */       t.sendAttachCreatureEffect(target, (byte)5, (byte)0, (byte)0, (byte)0, (byte)0);
/*     */     } 
/*     */     
/*     */     try {
/*  99 */       byte pos = target.getBody().getCenterWoundPos();
/*     */       
/* 101 */       double damage = calculateDamage(target, power, 9000.0D, 80.0D);
/*     */       
/* 103 */       target.addWoundOfType(performer, (byte)4, pos, false, 1.0F, false, damage, 0.0F, 0.0F, false, true);
/*     */     
/*     */     }
/* 106 */     catch (Exception exe) {
/*     */       
/* 108 */       logger.log(Level.WARNING, exe.getMessage(), exe);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\FireHeart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */