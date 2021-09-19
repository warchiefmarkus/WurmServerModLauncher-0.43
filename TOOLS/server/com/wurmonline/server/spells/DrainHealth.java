/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.bodys.Wound;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.zones.VolaTile;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DrainHealth
/*     */   extends DamageSpell
/*     */ {
/*     */   public static final int RANGE = 50;
/*     */   public static final double BASE_DAMAGE = 2000.0D;
/*     */   public static final double DAMAGE_PER_POWER = 20.0D;
/*     */   
/*     */   DrainHealth() {
/*  46 */     super("Drain Health", 255, 3, 15, 20, 19, 30000L);
/*  47 */     this.targetCreature = true;
/*  48 */     this.offensive = true;
/*  49 */     this.healing = true;
/*  50 */     this.description = "damages a creature internally and heals you";
/*  51 */     this.type = 2;
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
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/*  64 */     double castDamage = power * 20.0D;
/*  65 */     castDamage += 2000.0D;
/*     */ 
/*     */     
/*  68 */     double toHeal = castDamage * 2.0D;
/*  69 */     double healingResistance = SpellResist.getSpellResistance(performer, getNumber());
/*  70 */     toHeal *= healingResistance;
/*  71 */     if (toHeal > 1.0D) {
/*     */       
/*  73 */       if (performer.getBody().getWounds() != null) {
/*     */         
/*  75 */         Wound[] wounds = performer.getBody().getWounds().getWounds();
/*  76 */         if (wounds.length > 0) {
/*     */           
/*  78 */           if (wounds[0].getSeverity() < toHeal)
/*     */           {
/*  80 */             SpellResist.addSpellResistance(performer, getNumber(), wounds[0].getSeverity());
/*  81 */             wounds[0].heal();
/*     */           }
/*     */           else
/*     */           {
/*  85 */             SpellResist.addSpellResistance(performer, getNumber(), toHeal);
/*  86 */             wounds[0].modifySeverity((int)-toHeal);
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/*  91 */           performer.getStatus().modifyWounds(-((int)(0.5D * toHeal)));
/*     */         } 
/*     */       } else {
/*     */         
/*  95 */         performer.getStatus().modifyWounds(-((int)(0.5D * toHeal)));
/*     */       } 
/*     */       
/*  98 */       byte pos = 1;
/*     */       
/*     */       try {
/* 101 */         pos = target.getBody().getRandomWoundPos();
/*     */       }
/* 103 */       catch (Exception ex) {
/*     */         
/* 105 */         pos = 1;
/*     */       } 
/*     */ 
/*     */       
/* 109 */       double toDamage = calculateDamage(target, power, 2000.0D, 20.0D);
/*     */       
/* 111 */       target.addWoundOfType(performer, (byte)9, pos, false, 1.0F, false, toDamage, 0.0F, 0.0F, false, true);
/*     */ 
/*     */       
/* 114 */       performer.getCommunicator().sendNormalServerMessage("You gain some health from " + target.getNameWithGenus() + ".", (byte)4);
/*     */       
/* 116 */       target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " drains you on health.", (byte)4);
/*     */ 
/*     */       
/* 119 */       VolaTile targetVolaTile = Zones.getTileOrNull(target.getTileX(), target
/* 120 */           .getTileY(), target.isOnSurface());
/* 121 */       if (targetVolaTile != null)
/*     */       {
/* 123 */         targetVolaTile.sendAttachCreatureEffect(target, (byte)8, (byte)0, (byte)0, (byte)0, (byte)0);
/*     */       }
/*     */ 
/*     */       
/* 127 */       VolaTile performerVolaTile = Zones.getTileOrNull(performer.getTileX(), performer
/* 128 */           .getTileY(), performer.isOnSurface());
/* 129 */       if (performerVolaTile != null)
/*     */       {
/* 131 */         performerVolaTile.sendAttachCreatureEffect(performer, (byte)9, (byte)0, (byte)0, (byte)0, (byte)0);
/*     */       
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 137 */       performer.getCommunicator().sendNormalServerMessage("You try to drain some health from " + target
/* 138 */           .getNameWithGenus() + " but fail.", (byte)4);
/* 139 */       target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " tries to drain you on health but fails.", (byte)4);
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
/*     */   void doNegativeEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 152 */     performer.getCommunicator().sendNormalServerMessage("You try to drain some health from " + target
/* 153 */         .getNameWithGenus() + " but fail.", (byte)4);
/* 154 */     target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " tries to drain you on health but fails.", (byte)4);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\DrainHealth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */