/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
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
/*     */ public class Lightning
/*     */   extends KarmaSpell
/*     */ {
/*  39 */   private static final Logger logger = Logger.getLogger(Lightning.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 24;
/*     */ 
/*     */   
/*     */   public Lightning() {
/*  46 */     super("Lightning", 561, 20, 500, 20, 1, 180000L);
/*  47 */     this.targetCreature = true;
/*  48 */     this.targetTile = true;
/*  49 */     this.offensive = true;
/*     */     
/*  51 */     this.description = "creates and calls lightning down on your foes";
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
/*  62 */     if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2 && 
/*  63 */       !performer.getDeity().isHateGod())
/*     */     {
/*  65 */       if (performer.faithful)
/*     */       {
/*  67 */         if (!performer.isDuelOrSpar(target)) {
/*     */           
/*  69 */           performer.getCommunicator().sendNormalServerMessage(performer
/*  70 */               .getDeity().getName() + " would never accept your attack on " + target.getName() + ".", (byte)3);
/*     */           
/*  72 */           return false;
/*     */         } 
/*     */       }
/*     */     }
/*  76 */     if (!performer.isOnSurface()) {
/*     */       
/*  78 */       performer.getCommunicator().sendNormalServerMessage("You need to be outside to call lightning.", (byte)3);
/*  79 */       return false;
/*     */     } 
/*  81 */     if (performer.getCurrentTile().getStructure() != null) {
/*     */       
/*  83 */       performer.getCommunicator().sendNormalServerMessage("You need to be outside to call lightning.", (byte)3);
/*  84 */       return false;
/*     */     } 
/*  86 */     if (target.isOnSurface()) {
/*     */       
/*  88 */       if (Server.getInstance().hasThunderMode())
/*     */       {
/*  90 */         VolaTile t = Zones.getOrCreateTile(target.getTileX(), target.getTileY(), true);
/*  91 */         if (t.getStructure() == null || t.getStructure().isTypeBridge())
/*     */         {
/*  93 */           return true;
/*     */         }
/*     */         
/*  96 */         performer.getCommunicator().sendNormalServerMessage(target
/*  97 */             .getName() + " is hiding in the structure.", (byte)3);
/*     */       }
/*     */       else
/*     */       {
/* 101 */         performer.getCommunicator().sendNormalServerMessage("There is no lightning in the sky.", (byte)3);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 106 */       performer.getCommunicator().sendNormalServerMessage("You need to be outside to call lightning.", (byte)3);
/* 107 */     }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, int tilex, int tiley, int layer) {
/* 113 */     if (!performer.isOnSurface()) {
/*     */       
/* 115 */       performer.getCommunicator().sendNormalServerMessage("You need to be outside to call lightning.", (byte)3);
/* 116 */       return false;
/*     */     } 
/* 118 */     if (performer.getCurrentTile().getStructure() != null) {
/*     */       
/* 120 */       performer.getCommunicator().sendNormalServerMessage("You need to be outside to call lightning.", (byte)3);
/* 121 */       return false;
/*     */     } 
/* 123 */     if (layer < 0) {
/*     */       
/* 125 */       performer.getCommunicator().sendNormalServerMessage("You need to be outside to call lightning.", (byte)3);
/* 126 */       return false;
/*     */     } 
/* 128 */     VolaTile t = Zones.getOrCreateTile(tilex, tiley, true);
/* 129 */     if (t.getStructure() == null || t.getStructure().isTypeBridge())
/*     */     {
/* 131 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 135 */     performer.getCommunicator().sendNormalServerMessage("The structure is in the way.", (byte)3);
/* 136 */     return false;
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
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 148 */     if (target.isOnSurface()) {
/*     */       
/* 150 */       if (Server.getInstance().hasThunderMode()) {
/*     */         
/* 152 */         VolaTile t = Zones.getOrCreateTile(target.getTileX(), target.getTileY(), true);
/* 153 */         if (t.getStructure() == null || t.getStructure().isTypeBridge()) {
/*     */           
/* 155 */           float damage = 5000.0F + 5000.0F * (float)power / 100.0F;
/* 156 */           performer.getCommunicator().sendNormalServerMessage("You call down lightning on " + target.getName() + "!", (byte)2);
/*     */           
/* 158 */           target.getCommunicator().sendAlertServerMessage(performer.getName() + " calls down lightning on you!", (byte)4);
/*     */           
/* 160 */           Zones.flashSpell(target.getTileX(), target.getTileY(), damage, performer);
/*     */         } else {
/*     */           
/* 163 */           performer.getCommunicator().sendNormalServerMessage(target
/* 164 */               .getName() + " is hiding in the structure.", (byte)3);
/*     */         } 
/*     */       } else {
/*     */         
/* 168 */         performer.getCommunicator().sendNormalServerMessage("There is no lightning in the sky.", (byte)3);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 173 */       performer.getCommunicator().sendNormalServerMessage("There is no lightning there.", (byte)3);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/* 181 */     if (performer.isOnSurface()) {
/*     */       
/* 183 */       VolaTile t = Zones.getOrCreateTile(tilex, tiley, true);
/* 184 */       if (t.getStructure() == null || t.getStructure().isTypeBridge()) {
/*     */         
/* 186 */         float damage = 5000.0F + 5000.0F * (float)power / 100.0F;
/* 187 */         if (performer.getPower() > 1 && Servers.isThisATestServer())
/* 188 */           performer.getCommunicator().sendNormalServerMessage("Base damage: " + damage); 
/* 189 */         Server.getWeather().setRainAdd(40.0F);
/* 190 */         Server.getWeather().setCloudTarget(40.0F);
/* 191 */         performer.getCommunicator().sendNormalServerMessage("You call down lightning in the area!");
/* 192 */         t.broadCastAction(performer.getName() + " calls down lightning on you!", performer, true);
/* 193 */         Zones.flashSpell(tilex, tiley, damage, performer);
/*     */       } else {
/*     */         
/* 196 */         performer.getCommunicator().sendNormalServerMessage("Contains a structure blocking your efforts.", (byte)3);
/*     */       } 
/*     */     } else {
/*     */       
/* 200 */       performer.getCommunicator().sendNormalServerMessage("You need to be above ground to call lightning.", (byte)3);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Lightning.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */