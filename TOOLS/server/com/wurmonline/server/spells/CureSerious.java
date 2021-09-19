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
/*     */ public class CureSerious
/*     */   extends ReligiousSpell
/*     */ {
/*  41 */   private static final Logger logger = Logger.getLogger(CureSerious.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 12;
/*     */ 
/*     */   
/*     */   CureSerious() {
/*  48 */     super("Cure Serious", 248, 15, 17, 15, 35, 0L);
/*  49 */     this.targetWound = true;
/*  50 */     this.targetCreature = true;
/*  51 */     this.healing = true;
/*  52 */     this.description = "heals an extreme amount of damage on a single wound";
/*  53 */     this.type = 0;
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
/*  64 */     Wounds tWounds = target.getBody().getWounds();
/*  65 */     if (tWounds != null && (tWounds.getWounds()).length > 0)
/*     */     {
/*  67 */       return precondition(castSkill, performer, tWounds.getWounds()[0]);
/*     */     }
/*  69 */     performer.getCommunicator().sendNormalServerMessage(target.getName() + " has no wounds to heal.");
/*  70 */     return false;
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
/*  81 */     Wounds tWounds = target.getBody().getWounds();
/*  82 */     if (tWounds != null && (tWounds.getWounds()).length > 0) {
/*     */       
/*  84 */       Wound highestWound = tWounds.getWounds()[0];
/*  85 */       float highestSeverity = highestWound.getSeverity();
/*  86 */       for (Wound w : tWounds.getWounds()) {
/*     */         
/*  88 */         if (w.getSeverity() > highestSeverity) {
/*     */           
/*  90 */           highestWound = w;
/*  91 */           highestSeverity = w.getSeverity();
/*     */         } 
/*     */       } 
/*  94 */       doEffect(castSkill, power, performer, highestWound);
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
/*     */   boolean precondition(Skill castSkill, Creature performer, Wound target) {
/* 107 */     if (target.getCreature() == null) {
/*     */       
/* 109 */       performer.getCommunicator().sendNormalServerMessage("You cannot heal that wound.", (byte)3);
/* 110 */       return false;
/*     */     } 
/* 112 */     Creature tCret = target.getCreature();
/*     */ 
/*     */     
/* 115 */     if (tCret.isReborn()) {
/* 116 */       return true;
/*     */     }
/*     */     
/* 119 */     if (tCret.isPlayer() && target.getCreature() != performer) {
/*     */ 
/*     */       
/* 122 */       if (!tCret.isFriendlyKingdom(performer.getKingdomId())) {
/*     */ 
/*     */         
/* 125 */         if (performer.faithful) {
/*     */ 
/*     */           
/* 128 */           performer.getCommunicator().sendNormalServerMessage(performer
/* 129 */               .getDeity().getName() + " would never accept that.", (byte)3);
/*     */           
/* 131 */           return false;
/*     */         } 
/*     */         
/* 134 */         return true;
/*     */       } 
/*     */       
/* 137 */       return true;
/*     */     } 
/*     */     
/* 140 */     return true;
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
/* 151 */     boolean doeff = true;
/* 152 */     Creature tCret = target.getCreature();
/* 153 */     if (tCret.isReborn()) {
/*     */ 
/*     */       
/* 156 */       doeff = false;
/* 157 */       performer.getCommunicator().sendNormalServerMessage("The wound grows.", (byte)3);
/* 158 */       target.modifySeverity(6000);
/*     */     }
/* 160 */     else if (tCret.isPlayer() && target.getCreature() != performer) {
/*     */       
/* 162 */       if (performer.getDeity() != null)
/*     */       {
/* 164 */         if (!tCret.isFriendlyKingdom(performer.getKingdomId())) {
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
/*     */ 
/*     */     
/* 183 */     if (doeff) {
/*     */ 
/*     */       
/* 186 */       double resistance = SpellResist.getSpellResistance(tCret, getNumber());
/* 187 */       double toHeal = 58950.0D;
/* 188 */       toHeal += 58950.0D * power / 300.0D;
/* 189 */       if (performer.getCultist() != null && performer.getCultist().healsFaster())
/*     */       {
/* 191 */         toHeal *= 2.0D;
/*     */       }
/* 193 */       toHeal *= resistance;
/*     */ 
/*     */       
/* 196 */       VolaTile t = Zones.getTileOrNull(target.getCreature().getTileX(), target
/* 197 */           .getCreature().getTileY(), target.getCreature().isOnSurface());
/* 198 */       if (t != null)
/*     */       {
/* 200 */         t.sendAttachCreatureEffect(target.getCreature(), (byte)11, (byte)0, (byte)0, (byte)0, (byte)0);
/*     */       }
/*     */       
/* 203 */       if (target.getSeverity() <= toHeal) {
/*     */ 
/*     */ 
/*     */         
/* 207 */         SpellResist.addSpellResistance(tCret, getNumber(), target.getSeverity());
/* 208 */         target.heal();
/*     */         
/* 210 */         performer.getCommunicator().sendNormalServerMessage("You heal the wound.", (byte)2);
/* 211 */         if (performer != tCret) {
/* 212 */           tCret.getCommunicator().sendNormalServerMessage(performer.getName() + " completely heals your wound.", (byte)2);
/*     */         
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 218 */         SpellResist.addSpellResistance(tCret, getNumber(), toHeal);
/* 219 */         target.modifySeverity((int)-toHeal);
/*     */         
/* 221 */         performer.getCommunicator().sendNormalServerMessage("You cure the wound a bit.", (byte)2);
/* 222 */         if (performer != tCret)
/* 223 */           tCret.getCommunicator().sendNormalServerMessage(performer.getName() + " partially heals your wound.", (byte)2); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\CureSerious.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */