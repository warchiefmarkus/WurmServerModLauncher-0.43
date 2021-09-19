/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.bodys.Wound;
/*     */ import com.wurmonline.server.bodys.Wounds;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.structures.Structure;
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
/*     */ 
/*     */ public class ScornOfLibila
/*     */   extends DamageSpell
/*     */ {
/*     */   public static final int RANGE = 4;
/*     */   public static final double BASE_DAMAGE = 4000.0D;
/*     */   public static final double DAMAGE_PER_POWER = 40.0D;
/*     */   public static final int RADIUS = 3;
/*     */   
/*     */   public ScornOfLibila() {
/*  50 */     super("Scorn of Libila", 448, 15, 40, 50, 40, 120000L);
/*     */     
/*  52 */     this.targetTile = true;
/*  53 */     this.offensive = true;
/*  54 */     this.healing = true;
/*  55 */     this.description = "covers an area with draining energy, causing internal wounds on enemies and healing allies";
/*  56 */     this.type = 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/*  63 */     performer.getCommunicator().sendNormalServerMessage("You place the Mark of Libila where you stand, declaring a sanctuary.");
/*     */ 
/*     */     
/*  66 */     Structure currstr = performer.getCurrentTile().getStructure();
/*     */     
/*  68 */     int radiusBonus = (int)(power / 40.0D);
/*  69 */     int sx = Zones.safeTileX(performer.getTileX() - 3 - radiusBonus - performer.getNumLinks());
/*  70 */     int sy = Zones.safeTileY(performer.getTileY() - 3 - radiusBonus - performer.getNumLinks());
/*  71 */     int ex = Zones.safeTileX(performer.getTileX() + 3 + radiusBonus + performer.getNumLinks());
/*  72 */     int ey = Zones.safeTileY(performer.getTileY() + 3 + radiusBonus + performer.getNumLinks());
/*     */     
/*  74 */     calculateArea(sx, sy, ex, ey, tilex, tiley, layer, currstr);
/*  75 */     int damdealt = 3;
/*  76 */     int maxRiftPart = 5; int x;
/*  77 */     for (x = sx; x <= ex; x++) {
/*     */       
/*  79 */       for (int y = sy; y <= ey; y++) {
/*     */         
/*  81 */         boolean isValidTargetTile = false;
/*  82 */         if (tilex == x && tiley == y) {
/*     */           
/*  84 */           isValidTargetTile = true;
/*     */         }
/*     */         else {
/*     */           
/*  88 */           int currAreaX = x - sx;
/*  89 */           int currAreaY = y - sy;
/*  90 */           if (!this.area[currAreaX][currAreaY])
/*     */           {
/*  92 */             isValidTargetTile = true;
/*     */           }
/*     */         } 
/*  95 */         if (isValidTargetTile) {
/*     */           
/*  97 */           VolaTile t = Zones.getTileOrNull(x, y, performer.isOnSurface());
/*  98 */           if (t != null) {
/*     */             
/* 100 */             Creature[] crets = t.getCreatures();
/* 101 */             for (Creature lCret : crets) {
/*     */               
/* 103 */               if (!lCret.isInvulnerable())
/*     */               {
/* 105 */                 if (lCret.getAttitude(performer) == 2) {
/*     */                   
/* 107 */                   t.sendAttachCreatureEffect(lCret, (byte)8, (byte)0, (byte)0, (byte)0, (byte)0);
/*     */                   
/* 109 */                   damdealt += 3;
/*     */                   
/* 111 */                   double damage = calculateDamage(lCret, power, 4000.0D, 40.0D);
/*     */                   
/* 113 */                   if (!lCret.addWoundOfType(performer, (byte)9, 1, false, 1.0F, false, damage, 0.0F, 0.0F, false, true))
/*     */                   {
/*     */ 
/*     */ 
/*     */                     
/* 118 */                     lCret.setTarget(performer.getWurmId(), false);
/*     */                   }
/*     */                 } 
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 127 */     for (x = sx; x <= ex && damdealt > 0; x++) {
/*     */       
/* 129 */       for (int y = sy; y <= ey && damdealt > 0; y++) {
/*     */         
/* 131 */         VolaTile t = Zones.getTileOrNull(x, y, performer.isOnSurface());
/* 132 */         if (t != null) {
/*     */           
/* 134 */           Creature[] crets = t.getCreatures();
/* 135 */           for (Creature lCret : crets) {
/*     */             
/* 137 */             if (lCret.getAttitude(performer) == 1 || (lCret
/* 138 */               .getAttitude(performer) == 0 && !lCret.isAggHuman()) || lCret
/* 139 */               .getKingdomId() == performer.getKingdomId())
/*     */             {
/*     */               
/* 142 */               if (lCret.getBody() != null && lCret.getBody().getWounds() != null) {
/*     */ 
/*     */ 
/*     */                 
/* 146 */                 Wounds tWounds = lCret.getBody().getWounds();
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 151 */                 double healingPool = 58950.0D;
/* 152 */                 healingPool += 58950.0D * power / 100.0D;
/* 153 */                 if (performer.getCultist() != null && performer.getCultist().healsFaster())
/*     */                 {
/* 155 */                   healingPool *= 2.0D;
/*     */                 }
/*     */ 
/*     */                 
/* 159 */                 double resistance = SpellResist.getSpellResistance(lCret, 249);
/* 160 */                 healingPool *= resistance;
/*     */ 
/*     */                 
/* 163 */                 int woundsHealed = 0;
/* 164 */                 int maxWoundHeal = (int)(healingPool * 0.33D);
/* 165 */                 for (Wound w : tWounds.getWounds()) {
/*     */ 
/*     */                   
/* 168 */                   if (woundsHealed >= 3 || damdealt <= 0) {
/*     */                     break;
/*     */                   }
/* 171 */                   if (w.getSeverity() >= maxWoundHeal) {
/*     */ 
/*     */ 
/*     */ 
/*     */                     
/* 176 */                     healingPool -= maxWoundHeal;
/* 177 */                     SpellResist.addSpellResistance(lCret, 249, maxWoundHeal);
/* 178 */                     w.modifySeverity(-maxWoundHeal);
/* 179 */                     woundsHealed++;
/* 180 */                     damdealt--;
/*     */                   } 
/*     */                 } 
/*     */ 
/*     */                 
/* 185 */                 while (woundsHealed < 3 && damdealt > 0 && (tWounds.getWounds()).length > 0) {
/*     */                   
/* 187 */                   Wound targetWound = tWounds.getWounds()[0];
/*     */ 
/*     */                   
/* 190 */                   for (Wound w : tWounds.getWounds()) {
/*     */                     
/* 192 */                     if (w.getSeverity() > targetWound.getSeverity()) {
/* 193 */                       targetWound = w;
/*     */                     }
/*     */                   } 
/*     */                   
/* 197 */                   SpellResist.addSpellResistance(lCret, 249, targetWound.getSeverity());
/* 198 */                   targetWound.heal();
/* 199 */                   woundsHealed++;
/* 200 */                   damdealt--;
/*     */                 } 
/*     */ 
/*     */                 
/* 204 */                 if (woundsHealed < 3 && damdealt > 0 && (tWounds.getWounds()).length > 0)
/*     */                 {
/* 206 */                   for (Wound w : tWounds.getWounds()) {
/*     */ 
/*     */                     
/* 209 */                     if (woundsHealed >= 3 || damdealt <= 0) {
/*     */                       break;
/*     */                     }
/* 212 */                     if (w.getSeverity() <= maxWoundHeal) {
/*     */ 
/*     */                       
/* 215 */                       SpellResist.addSpellResistance(lCret, 249, w.getSeverity());
/* 216 */                       w.heal();
/* 217 */                       woundsHealed++;
/* 218 */                       damdealt--;
/*     */                     
/*     */                     }
/*     */                     else {
/*     */                       
/* 223 */                       SpellResist.addSpellResistance(lCret, getNumber(), maxWoundHeal);
/* 224 */                       w.modifySeverity(-maxWoundHeal);
/* 225 */                       woundsHealed++;
/*     */                     } 
/*     */                   } 
/*     */                 }
/*     */               } 
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\ScornOfLibila.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */