/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.bodys.Wound;
/*     */ import com.wurmonline.server.bodys.Wounds;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.utils.CreatureLineSegment;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.util.MulticolorLineSegment;
/*     */ import java.util.ArrayList;
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
/*     */ public class LightOfFo
/*     */   extends ReligiousSpell
/*     */ {
/*     */   public static final int RANGE = 4;
/*     */   
/*     */   public LightOfFo() {
/*  44 */     super("Light of Fo", 438, 15, 60, 40, 33, 120000L);
/*  45 */     this.targetTile = true;
/*  46 */     this.healing = true;
/*  47 */     this.description = "covers an area with healing energy, healing multiple wounds from allies";
/*  48 */     this.type = 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/*  55 */     performer.getCommunicator().sendNormalServerMessage("You place the Mark of Fo in the area, declaring a sanctuary.");
/*     */     
/*  57 */     int sx = Zones.safeTileX(tilex - (int)Math.max(1.0D, power / 10.0D + performer.getNumLinks()));
/*  58 */     int sy = Zones.safeTileY(tiley - (int)Math.max(1.0D, power / 10.0D + performer.getNumLinks()));
/*  59 */     int ex = Zones.safeTileX(tilex + (int)Math.max(1.0D, power / 10.0D + performer.getNumLinks()));
/*  60 */     int ey = Zones.safeTileY(tiley + (int)Math.max(1.0D, power / 10.0D + performer.getNumLinks()));
/*  61 */     int totalHealed = 0;
/*  62 */     for (int x = sx; x <= ex; x++) {
/*     */       
/*  64 */       for (int y = sy; y <= ey; y++) {
/*     */         
/*  66 */         VolaTile t = Zones.getTileOrNull(x, y, performer.isOnSurface());
/*     */         
/*  68 */         if (t != null)
/*     */         {
/*     */           
/*  71 */           for (Creature lCret : t.getCreatures()) {
/*     */             
/*  73 */             boolean doHeal = false;
/*     */             
/*  75 */             if (lCret.getKingdomId() == performer.getKingdomId() || lCret
/*  76 */               .getAttitude(performer) == 1) {
/*  77 */               doHeal = true;
/*     */             }
/*     */             
/*  80 */             Village lVill = lCret.getCitizenVillage();
/*  81 */             if (lVill != null)
/*     */             {
/*  83 */               if (lVill.isEnemy(performer))
/*     */               {
/*  85 */                 doHeal = false;
/*     */               }
/*     */             }
/*     */ 
/*     */             
/*  90 */             Village pVill = performer.getCitizenVillage();
/*  91 */             if (pVill != null)
/*     */             {
/*  93 */               if (pVill.isEnemy(lCret))
/*     */               {
/*  95 */                 doHeal = false;
/*     */               }
/*     */             }
/*     */ 
/*     */             
/* 100 */             if (doHeal)
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 108 */               if (lCret.getBody() != null && lCret.getBody().getWounds() != null) {
/*     */ 
/*     */ 
/*     */                 
/* 112 */                 Wounds tWounds = lCret.getBody().getWounds();
/*     */ 
/*     */ 
/*     */                 
/* 116 */                 double healingPool = 16375.0D;
/* 117 */                 healingPool += 98250.0D * power / 100.0D;
/* 118 */                 if (performer.getCultist() != null && performer.getCultist().healsFaster())
/*     */                 {
/* 120 */                   healingPool *= 2.0D;
/*     */                 }
/* 122 */                 double resistance = SpellResist.getSpellResistance(lCret, getNumber());
/* 123 */                 healingPool *= resistance;
/*     */                 
/* 125 */                 int woundsHealed = 0;
/* 126 */                 int maxWoundHeal = (int)(healingPool * 0.2D);
/* 127 */                 for (Wound w : tWounds.getWounds()) {
/*     */ 
/*     */                   
/* 130 */                   if (woundsHealed >= 5) {
/*     */                     break;
/*     */                   }
/* 133 */                   if (w.getSeverity() >= maxWoundHeal) {
/*     */ 
/*     */                     
/* 136 */                     healingPool -= maxWoundHeal;
/* 137 */                     SpellResist.addSpellResistance(lCret, getNumber(), maxWoundHeal);
/* 138 */                     w.modifySeverity(-maxWoundHeal);
/* 139 */                     woundsHealed++;
/*     */                   } 
/*     */                 } 
/*     */ 
/*     */                 
/* 144 */                 while (woundsHealed < 5 && (tWounds.getWounds()).length > 0) {
/*     */                   
/* 146 */                   Wound targetWound = tWounds.getWounds()[0];
/*     */ 
/*     */                   
/* 149 */                   for (Wound w : tWounds.getWounds()) {
/*     */                     
/* 151 */                     if (w.getSeverity() > targetWound.getSeverity()) {
/* 152 */                       targetWound = w;
/*     */                     }
/*     */                   } 
/*     */                   
/* 156 */                   SpellResist.addSpellResistance(lCret, 249, targetWound.getSeverity());
/* 157 */                   targetWound.heal();
/* 158 */                   woundsHealed++;
/*     */                 } 
/*     */ 
/*     */                 
/* 162 */                 if (woundsHealed < 5)
/*     */                 {
/* 164 */                   for (Wound w : tWounds.getWounds()) {
/*     */ 
/*     */                     
/* 167 */                     if (woundsHealed >= 5) {
/*     */                       break;
/*     */                     }
/* 170 */                     if (w.getSeverity() <= maxWoundHeal) {
/*     */ 
/*     */                       
/* 173 */                       SpellResist.addSpellResistance(lCret, getNumber(), w.getSeverity());
/* 174 */                       w.heal();
/* 175 */                       woundsHealed++;
/*     */                     
/*     */                     }
/*     */                     else {
/*     */                       
/* 180 */                       SpellResist.addSpellResistance(lCret, getNumber(), maxWoundHeal);
/* 181 */                       w.modifySeverity(-maxWoundHeal);
/* 182 */                       woundsHealed++;
/*     */                     } 
/*     */                   } 
/*     */                 }
/*     */ 
/*     */                 
/* 188 */                 VolaTile tt = Zones.getTileOrNull(lCret.getTileX(), lCret
/* 189 */                     .getTileY(), lCret
/* 190 */                     .isOnSurface());
/* 191 */                 if (tt != null)
/*     */                 {
/* 193 */                   tt.sendAttachCreatureEffect(lCret, (byte)11, (byte)0, (byte)0, (byte)0, (byte)0);
/*     */                 }
/*     */ 
/*     */                 
/* 197 */                 totalHealed++;
/*     */                 
/* 199 */                 String heal = (performer == lCret) ? "heal" : "heals";
/* 200 */                 ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 201 */                 segments.add(new CreatureLineSegment(performer));
/* 202 */                 segments.add(new MulticolorLineSegment(" " + heal + " some of your wounds with " + getName() + ".", (byte)0));
/* 203 */                 lCret.getCommunicator().sendColoredMessageCombat(segments);
/*     */               } 
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\LightOfFo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */