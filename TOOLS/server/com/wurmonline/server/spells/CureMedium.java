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
/*     */ public final class CureMedium
/*     */   extends ReligiousSpell
/*     */ {
/*  41 */   private static final Logger logger = Logger.getLogger(CureMedium.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 12;
/*     */ 
/*     */   
/*     */   CureMedium() {
/*  48 */     super("Cure Medium", 247, 12, 13, 10, 32, 0L);
/*  49 */     this.targetWound = true;
/*  50 */     this.targetCreature = true;
/*  51 */     this.healing = true;
/*  52 */     this.description = "heals a large amount of damage on a single wound";
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
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, Wound target) {
/*  65 */     if (target.getCreature() == null) {
/*     */       
/*  67 */       performer.getCommunicator().sendNormalServerMessage("You cannot heal that wound.", (byte)3);
/*  68 */       return false;
/*     */     } 
/*  70 */     Creature tCret = target.getCreature();
/*     */ 
/*     */     
/*  73 */     if (tCret.isReborn()) {
/*  74 */       return true;
/*     */     }
/*     */     
/*  77 */     if (tCret.isPlayer() && target.getCreature() != performer) {
/*     */ 
/*     */       
/*  80 */       if (!tCret.isFriendlyKingdom(performer.getKingdomId())) {
/*     */ 
/*     */         
/*  83 */         if (performer.faithful) {
/*     */ 
/*     */           
/*  86 */           performer.getCommunicator().sendNormalServerMessage(performer
/*  87 */               .getDeity().getName() + " would never accept that.", (byte)3);
/*     */           
/*  89 */           return false;
/*     */         } 
/*     */         
/*  92 */         return true;
/*     */       } 
/*     */       
/*  95 */       return true;
/*     */     } 
/*     */     
/*  98 */     return true;
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
/* 109 */     Wounds tWounds = target.getBody().getWounds();
/* 110 */     if (tWounds != null && (tWounds.getWounds()).length > 0)
/*     */     {
/* 112 */       return precondition(castSkill, performer, tWounds.getWounds()[0]);
/*     */     }
/* 114 */     performer.getCommunicator().sendNormalServerMessage(target.getName() + " has no wounds to heal.");
/* 115 */     return false;
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
/* 126 */     Wounds tWounds = target.getBody().getWounds();
/* 127 */     if (tWounds != null && (tWounds.getWounds()).length > 0) {
/*     */       
/* 129 */       Wound highestWound = tWounds.getWounds()[0];
/* 130 */       float highestSeverity = highestWound.getSeverity();
/* 131 */       for (Wound w : tWounds.getWounds()) {
/*     */         
/* 133 */         if (w.getSeverity() > highestSeverity) {
/*     */           
/* 135 */           highestWound = w;
/* 136 */           highestSeverity = w.getSeverity();
/*     */         } 
/*     */       } 
/* 139 */       doEffect(castSkill, power, performer, highestWound);
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
/* 151 */     boolean doeff = true;
/* 152 */     Creature tCret = target.getCreature();
/* 153 */     if (tCret.isReborn()) {
/*     */ 
/*     */       
/* 156 */       doeff = false;
/* 157 */       performer.getCommunicator().sendNormalServerMessage("The wound grows.", (byte)3);
/* 158 */       target.modifySeverity(2000);
/*     */     }
/* 160 */     else if (tCret.isPlayer() && tCret != performer) {
/*     */       
/* 162 */       if (performer.getDeity() != null)
/*     */       {
/* 164 */         if (!tCret.isFriendlyKingdom(performer.getKingdomId())) {
/*     */           
/* 166 */           performer.getCommunicator().sendNormalServerMessage(performer
/* 167 */               .getDeity().getName() + " becomes very upset at the way you abuse " + performer
/* 168 */               .getDeity().getHisHerItsString() + " powers!", (byte)3);
/*     */           
/*     */           try {
/* 171 */             performer.setFaith(performer.getFaith() / 2.0F);
/*     */           }
/* 173 */           catch (Exception ex) {
/*     */             
/* 175 */             logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 181 */     if (doeff) {
/*     */ 
/*     */       
/* 184 */       double resistance = SpellResist.getSpellResistance(tCret, getNumber());
/* 185 */       double toHeal = 19650.0D;
/* 186 */       toHeal += 19650.0D * power / 300.0D;
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


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\CureMedium.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */