/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.ai.PathTile;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.structures.Floor;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.Set;
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
/*     */ public class Fireball
/*     */   extends KarmaSpell
/*     */ {
/*  41 */   private static final Logger logger = Logger.getLogger(Fireball.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 24;
/*     */ 
/*     */   
/*     */   public Fireball() {
/*  48 */     super("Fireball", 549, 15, 1000, 30, 1, 180000L);
/*  49 */     this.targetCreature = true;
/*  50 */     this.offensive = true;
/*     */     
/*  52 */     this.description = "sends an exploding ball of fire towards the target";
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
/*  63 */     if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2 && 
/*  64 */       !performer.getDeity().isHateGod())
/*     */     {
/*  66 */       if (performer.faithful)
/*     */       {
/*  68 */         if (!performer.isDuelOrSpar(target)) {
/*     */           
/*  70 */           performer.getCommunicator().sendNormalServerMessage(performer
/*  71 */               .getDeity().getName() + " would never accept your attack on " + target.getNameWithGenus() + ".", (byte)3);
/*     */           
/*  73 */           return false;
/*     */         } 
/*     */       }
/*     */     }
/*  77 */     return true;
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
/*  88 */     if (!target.isUnique() || power > 99.0D) {
/*     */       
/*  90 */       int diameter = (int)Math.max(power / 30.0D, 1.0D);
/*  91 */       int tfloorlevel = target.getFloorLevel();
/*  92 */       Set<PathTile> tiles = Zones.explode(target.getTileX(), target.getTileY(), tfloorlevel, true, diameter);
/*     */       
/*  94 */       boolean insideStructure = false;
/*  95 */       target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " burns you.", (byte)4);
/*     */ 
/*     */       
/*  98 */       if (target.getCurrentTile() != null) {
/*     */         
/* 100 */         target.getCurrentTile().sendAttachCreatureEffect(target, (byte)4, (byte)0, (byte)0, (byte)0, (byte)0);
/*     */         
/* 102 */         insideStructure = true;
/*     */       } 
/*     */       
/* 105 */       int maxLevel = (tfloorlevel >= 0) ? 2 : tfloorlevel;
/* 106 */       for (int fLevel = target.getFloorLevel(); fLevel <= maxLevel; fLevel++) {
/*     */         
/* 108 */         if (fLevel <= 0 || insideStructure)
/*     */         {
/* 110 */           for (PathTile pathtile : tiles) {
/*     */             
/* 112 */             VolaTile targetVolaTile = Zones.getOrCreateTile(pathtile.getTileX(), pathtile
/* 113 */                 .getTileY(), (pathtile.getFloorLevel() >= 0));
/*     */             
/* 115 */             if (targetVolaTile != null)
/*     */             {
/* 117 */               Creature[] crets = targetVolaTile.getCreatures();
/* 118 */               for (Creature creature : crets) {
/*     */                 
/* 120 */                 if (creature.getWurmId() != performer.getWurmId() && creature
/* 121 */                   .getKingdomId() != performer.getKingdomId() && creature
/* 122 */                   .getPower() < 2)
/*     */                 {
/* 124 */                   if (creature.getFloorLevel() == fLevel)
/*     */                     
/*     */                     try {
/*     */                       
/* 128 */                       double damage = 9000.0D + 6000.0D * power / 100.0D;
/* 129 */                       byte pos = creature.getBody().getRandomWoundPos();
/*     */                       
/* 131 */                       creature.addWoundOfType(performer, (byte)4, pos, false, 1.0F, true, damage, 0.0F, 0.0F, false, true);
/*     */                     
/*     */                     }
/* 134 */                     catch (Exception ex) {
/*     */                       
/* 136 */                       logger.log(Level.INFO, ex.getMessage(), ex);
/*     */                     }  
/*     */                 }
/*     */               } 
/* 140 */               if (targetVolaTile.getStructure() != null) {
/*     */                 
/* 142 */                 insideStructure = true;
/*     */                 
/* 144 */                 Floor f = targetVolaTile.getTopFloor();
/* 145 */                 if (f == null || f.getFloorLevel() > fLevel)
/* 146 */                   targetVolaTile.sendAddQuickTileEffect((byte)65, fLevel); 
/*     */                 continue;
/*     */               } 
/* 149 */               targetVolaTile.sendAddQuickTileEffect((byte)65, fLevel);
/*     */             }
/*     */           
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/* 157 */       performer.getCommunicator().sendNormalServerMessage("You try to fireball " + target
/* 158 */           .getNameWithGenus() + " but fail.");
/* 159 */       target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " tries to fireball you but fails.");
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
/*     */   void doNegativeEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 171 */     performer.getCommunicator().sendNormalServerMessage("You try to set " + target
/* 172 */         .getNameWithGenus() + " on fire but fail.");
/* 173 */     target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " tries to set you on fire but fails.");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Fireball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */