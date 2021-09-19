/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.bodys.TempWound;
/*     */ import com.wurmonline.server.bodys.Wound;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.AttitudeConstants;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Smite
/*     */   extends ReligiousSpell
/*     */   implements AttitudeConstants
/*     */ {
/*     */   public static final int RANGE = 12;
/*     */   
/*     */   Smite() {
/*  47 */     super("Smite", 252, 30, 50, 70, 70, 30000L);
/*  48 */     this.targetCreature = true;
/*  49 */     this.offensive = true;
/*  50 */     this.description = "damages the targets body with extreme fire damage depending on how healthy they are";
/*  51 */     this.type = 2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/*  57 */     if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2 && 
/*  58 */       !performer.getDeity().isLibila())
/*     */     {
/*  60 */       if (performer.faithful) {
/*     */         
/*  62 */         performer.getCommunicator().sendNormalServerMessage(performer
/*  63 */             .getDeity().getName() + " would never accept your smiting " + target.getName() + ".", (byte)3);
/*     */         
/*  65 */         return false;
/*     */       } 
/*     */     }
/*  68 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/*  74 */     if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2 && 
/*  75 */       !performer.getDeity().isLibila())
/*     */     {
/*  77 */       performer.modifyFaith(-5.0F);
/*     */     }
/*     */     
/*  80 */     if (Server.rand.nextFloat() > target.addSpellResistance((short)252)) {
/*     */       
/*  82 */       performer.getCommunicator().sendNormalServerMessage(target
/*  83 */           .getName() + " resists your attempt to smite " + target.getHimHerItString() + ".", (byte)3);
/*     */       
/*  85 */       target.getCommunicator().sendSafeServerMessage(performer.getName() + " tries to smite you but you resist.", (byte)4);
/*     */       
/*     */       return;
/*     */     } 
/*  89 */     int damage = (target.getStatus()).damage;
/*  90 */     int minhealth = 65435;
/*  91 */     if (target.isUnique())
/*     */     {
/*  93 */       minhealth = 15535;
/*     */     }
/*     */     
/*  96 */     double maxdam = Math.max(0, minhealth - damage);
/*  97 */     maxdam *= 0.5D + 0.5D * power / 100.0D;
/*  98 */     float resistance = (float)SpellResist.getSpellResistance(target, getNumber());
/*  99 */     maxdam *= resistance;
/*     */     
/* 101 */     SpellResist.addSpellResistance(target, getNumber(), maxdam);
/*     */     
/* 103 */     maxdam = Spell.modifyDamage(target, maxdam);
/*     */     
/* 105 */     if (maxdam > 500.0D) {
/*     */       
/* 107 */       performer.getCommunicator().sendNormalServerMessage("You smite " + target.getName() + ".", (byte)2);
/* 108 */       target.getCommunicator().sendAlertServerMessage(performer.getName() + " smites you.", (byte)4);
/* 109 */       Wound wound = null;
/* 110 */       if (target instanceof com.wurmonline.server.players.Player) {
/*     */         
/* 112 */         target.addWoundOfType(null, (byte)4, 0, false, 1.0F, true, maxdam, 0.0F, 0.0F, false, true);
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 117 */         TempWound tempWound = new TempWound((byte)4, (byte)0, (float)maxdam, target.getWurmId(), 0.0F, 0.0F, true);
/* 118 */         target.getBody().addWound((Wound)tempWound);
/*     */       } 
/*     */       
/* 121 */       VolaTile t = Zones.getTileOrNull(target.getTileX(), target.getTileY(), target
/* 122 */           .isOnSurface());
/* 123 */       if (t != null)
/*     */       {
/* 125 */         t.sendAttachCreatureEffect(target, (byte)10, (byte)0, (byte)0, (byte)0, (byte)0);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 130 */       performer.getCommunicator().sendNormalServerMessage("You try to smite " + target
/* 131 */           .getName() + " but there seems to be no effect.", (byte)3);
/* 132 */       target.getCommunicator().sendNormalServerMessage(performer.getName() + " tries to smite you but to no avail.", (byte)4);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Smite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */