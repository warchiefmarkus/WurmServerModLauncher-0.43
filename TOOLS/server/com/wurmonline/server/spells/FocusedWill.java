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
/*     */ 
/*     */ 
/*     */ public final class FocusedWill
/*     */   extends ReligiousSpell
/*     */ {
/*  42 */   private static final Logger logger = Logger.getLogger(FocusedWill.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 12;
/*     */ 
/*     */   
/*     */   FocusedWill() {
/*  49 */     super("Focused Will", 929, 10, 10, 5, 31, 0L);
/*  50 */     this.targetWound = true;
/*  51 */     this.targetCreature = true;
/*  52 */     this.healing = true;
/*  53 */     this.description = "heals a small amount of damage on a single wound in exchange for your stamina";
/*  54 */     this.type = 0;
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
/*  66 */     if (target.getCreature() == null) {
/*     */       
/*  68 */       performer.getCommunicator().sendNormalServerMessage("You cannot heal that wound.", (byte)3);
/*     */       
/*  70 */       return false;
/*     */     } 
/*  72 */     Creature tCret = target.getCreature();
/*     */ 
/*     */     
/*  75 */     if (tCret.isReborn()) {
/*     */       
/*  77 */       performer.getCommunicator().sendNormalServerMessage("You cannot heal the undead.", (byte)3);
/*     */       
/*  79 */       return false;
/*     */     } 
/*     */ 
/*     */     
/*  83 */     if (tCret.isPlayer() && target.getCreature() != performer) {
/*     */ 
/*     */       
/*  86 */       if (!tCret.isFriendlyKingdom(performer.getKingdomId())) {
/*     */ 
/*     */         
/*  89 */         if (performer.faithful) {
/*     */ 
/*     */           
/*  92 */           performer.getCommunicator().sendNormalServerMessage(performer
/*  93 */               .getDeity().getName() + " would never accept that.", (byte)3);
/*     */           
/*  95 */           return false;
/*     */         } 
/*     */         
/*  98 */         return true;
/*     */       } 
/*     */       
/* 101 */       return true;
/*     */     } 
/*     */     
/* 104 */     return true;
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
/* 115 */     Wounds tWounds = target.getBody().getWounds();
/* 116 */     if (tWounds != null && (tWounds.getWounds()).length > 0)
/*     */     {
/* 118 */       return precondition(castSkill, performer, tWounds.getWounds()[0]);
/*     */     }
/* 120 */     performer.getCommunicator().sendNormalServerMessage(target.getName() + " has no wounds to heal.");
/* 121 */     return false;
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
/* 132 */     Wounds tWounds = target.getBody().getWounds();
/* 133 */     if (tWounds != null && (tWounds.getWounds()).length > 0) {
/*     */       
/* 135 */       Wound highestWound = tWounds.getWounds()[0];
/* 136 */       float highestSeverity = highestWound.getSeverity();
/* 137 */       for (Wound w : tWounds.getWounds()) {
/*     */         
/* 139 */         if (w.getSeverity() > highestSeverity) {
/*     */           
/* 141 */           highestWound = w;
/* 142 */           highestSeverity = w.getSeverity();
/*     */         } 
/*     */       } 
/* 145 */       doEffect(castSkill, power, performer, highestWound);
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
/* 157 */     Creature tCret = target.getCreature();
/* 158 */     if (tCret.isPlayer() && target.getCreature() != performer)
/*     */     {
/* 160 */       if (performer.getDeity() != null && tCret.getDeity() != null)
/*     */       {
/* 162 */         if (!target.getCreature().isFriendlyKingdom(performer.getKingdomId())) {
/*     */           
/* 164 */           performer.getCommunicator().sendNormalServerMessage(performer
/* 165 */               .getDeity().getName() + " becomes very upset at the way you abuse " + performer
/* 166 */               .getDeity().getHisHerItsString() + " powers!", (byte)3);
/*     */ 
/*     */           
/*     */           try {
/* 170 */             performer.setFaith(performer.getFaith() / 2.0F);
/*     */           }
/* 172 */           catch (Exception ex) {
/*     */             
/* 174 */             logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/* 180 */     double resistance = SpellResist.getSpellResistance(tCret, getNumber());
/* 181 */     double toHeal = 3275.0D;
/* 182 */     toHeal += 9825.0D * power / 100.0D;
/* 183 */     if (performer.getCultist() != null && performer.getCultist().healsFaster())
/*     */     {
/* 185 */       toHeal *= 2.0D;
/*     */     }
/* 187 */     toHeal *= resistance;
/*     */ 
/*     */     
/* 190 */     VolaTile t = Zones.getTileOrNull(target.getCreature().getTileX(), target
/* 191 */         .getCreature().getTileY(), target.getCreature().isOnSurface());
/* 192 */     if (t != null)
/*     */     {
/* 194 */       t.sendAttachCreatureEffect(target.getCreature(), (byte)11, (byte)0, (byte)0, (byte)0, (byte)0);
/*     */     }
/*     */ 
/*     */     
/* 198 */     if (target.getSeverity() <= toHeal) {
/*     */ 
/*     */ 
/*     */       
/* 202 */       SpellResist.addSpellResistance(tCret, getNumber(), target.getSeverity());
/* 203 */       performer.getStatus().modifyStamina(-target.getSeverity());
/* 204 */       target.heal();
/*     */       
/* 206 */       performer.getCommunicator().sendNormalServerMessage("You manage to heal the wound.", (byte)2);
/* 207 */       if (performer != tCret) {
/* 208 */         tCret.getCommunicator().sendNormalServerMessage(performer.getName() + " completely heals your wound.", (byte)2);
/*     */       
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 214 */       SpellResist.addSpellResistance(tCret, getNumber(), toHeal);
/* 215 */       performer.getStatus().modifyStamina((float)-toHeal);
/* 216 */       target.modifySeverity((int)-toHeal);
/*     */       
/* 218 */       performer.getCommunicator().sendNormalServerMessage("You cure the wound a bit.", (byte)2);
/* 219 */       if (performer != tCret)
/* 220 */         tCret.getCommunicator().sendNormalServerMessage(performer.getName() + " partially heals your wound.", (byte)2); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\FocusedWill.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */