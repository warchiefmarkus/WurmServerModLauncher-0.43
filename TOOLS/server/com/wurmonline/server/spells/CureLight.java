/*     */ package com.wurmonline.server.spells;
/*     */ 
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
/*     */ public final class CureLight
/*     */   extends ReligiousSpell
/*     */ {
/*  40 */   private static final Logger logger = Logger.getLogger(CureLight.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 12;
/*     */ 
/*     */   
/*     */   CureLight() {
/*  47 */     super("Cure Light", 246, 10, 10, 5, 31, 0L);
/*  48 */     this.targetWound = true;
/*  49 */     this.targetCreature = true;
/*  50 */     this.healing = true;
/*  51 */     this.description = "heals a small amount of damage on a single wound";
/*  52 */     this.type = 0;
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
/*     */   boolean precondition(Skill castSkill, Creature performer, Wound target) {
/*  64 */     if (target.getCreature() == null) {
/*     */       
/*  66 */       performer.getCommunicator().sendNormalServerMessage("You cannot heal that wound.", (byte)3);
/*  67 */       return false;
/*     */     } 
/*  69 */     Creature tCret = target.getCreature();
/*     */ 
/*     */     
/*  72 */     if (tCret.isReborn()) {
/*  73 */       return true;
/*     */     }
/*     */     
/*  76 */     if (tCret.isPlayer() && target.getCreature() != performer) {
/*     */ 
/*     */       
/*  79 */       if (!tCret.isFriendlyKingdom(performer.getKingdomId())) {
/*     */ 
/*     */         
/*  82 */         if (performer.faithful) {
/*     */ 
/*     */           
/*  85 */           performer.getCommunicator().sendNormalServerMessage(performer
/*  86 */               .getDeity().getName() + " would never accept that.", (byte)3);
/*     */           
/*  88 */           return false;
/*     */         } 
/*     */         
/*  91 */         return true;
/*     */       } 
/*     */       
/*  94 */       return true;
/*     */     } 
/*     */     
/*  97 */     return true;
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
/* 108 */     Wounds tWounds = target.getBody().getWounds();
/* 109 */     if (tWounds != null && (tWounds.getWounds()).length > 0)
/*     */     {
/* 111 */       return precondition(castSkill, performer, tWounds.getWounds()[0]);
/*     */     }
/* 113 */     performer.getCommunicator().sendNormalServerMessage(target.getName() + " has no wounds to heal.");
/* 114 */     return false;
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
/* 125 */     Wounds tWounds = target.getBody().getWounds();
/* 126 */     if (tWounds != null && (tWounds.getWounds()).length > 0) {
/*     */       
/* 128 */       Wound highestWound = tWounds.getWounds()[0];
/* 129 */       float highestSeverity = highestWound.getSeverity();
/* 130 */       for (Wound w : tWounds.getWounds()) {
/*     */         
/* 132 */         if (w.getSeverity() > highestSeverity) {
/*     */           
/* 134 */           highestWound = w;
/* 135 */           highestSeverity = w.getSeverity();
/*     */         } 
/*     */       } 
/* 138 */       doEffect(castSkill, power, performer, highestWound);
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
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Wound target) {
/* 150 */     boolean doeff = true;
/* 151 */     Creature tCret = target.getCreature();
/* 152 */     if (tCret.isReborn()) {
/*     */ 
/*     */       
/* 155 */       doeff = false;
/* 156 */       performer.getCommunicator().sendNormalServerMessage("The wound grows.", (byte)3);
/*     */       
/* 158 */       target.modifySeverity(1000);
/*     */     }
/* 160 */     else if (tCret.isPlayer() && target.getCreature() != performer) {
/*     */       
/* 162 */       if (performer.getDeity() != null && tCret.getDeity() != null)
/*     */       {
/* 164 */         if (!target.getCreature().isFriendlyKingdom(performer.getKingdomId())) {
/*     */           
/* 166 */           performer.getCommunicator().sendNormalServerMessage(performer
/* 167 */               .getDeity().getName() + " becomes very upset at the way you abuse " + performer
/* 168 */               .getDeity().getHisHerItsString() + " powers!", (byte)3);
/*     */ 
/*     */           
/*     */           try {
/* 172 */             performer.setFaith(performer.getFaith() / 2.0F);
/*     */           }
/* 174 */           catch (Exception ex) {
/*     */             
/* 176 */             logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 181 */     if (doeff) {
/*     */ 
/*     */       
/* 184 */       double resistance = SpellResist.getSpellResistance(tCret, getNumber());
/* 185 */       double toHeal = 9825.0D;
/* 186 */       toHeal += 9825.0D * power / 300.0D;
/* 187 */       if (performer.getCultist() != null && performer.getCultist().healsFaster())
/*     */       {
/* 189 */         toHeal *= 2.0D;
/*     */       }
/* 191 */       toHeal *= resistance;
/*     */ 
/*     */       
/* 194 */       VolaTile t = Zones.getTileOrNull(target.getCreature().getTileX(), target
/* 195 */           .getCreature().getTileY(), target.getCreature().isOnSurface());
/* 196 */       if (t != null)
/*     */       {
/* 198 */         t.sendAttachCreatureEffect(target.getCreature(), (byte)11, (byte)0, (byte)0, (byte)0, (byte)0);
/*     */       }
/*     */ 
/*     */       
/* 202 */       if (target.getSeverity() <= toHeal) {
/*     */ 
/*     */ 
/*     */         
/* 206 */         SpellResist.addSpellResistance(tCret, getNumber(), target.getSeverity());
/* 207 */         target.heal();
/*     */         
/* 209 */         performer.getCommunicator().sendNormalServerMessage("You manage to heal the wound.", (byte)2);
/* 210 */         if (performer != tCret) {
/* 211 */           tCret.getCommunicator().sendNormalServerMessage(performer.getName() + " completely heals your wound.", (byte)2);
/*     */         
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 217 */         SpellResist.addSpellResistance(tCret, getNumber(), toHeal);
/* 218 */         target.modifySeverity((int)-toHeal);
/*     */         
/* 220 */         performer.getCommunicator().sendNormalServerMessage("You cure the wound a bit.", (byte)2);
/* 221 */         if (performer != tCret)
/* 222 */           tCret.getCommunicator().sendNormalServerMessage(performer.getName() + " partially heals your wound.", (byte)2); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\CureLight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */