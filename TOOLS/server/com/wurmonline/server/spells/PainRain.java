/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.items.Item;
/*    */ import com.wurmonline.server.skills.Skill;
/*    */ import com.wurmonline.server.structures.Structure;
/*    */ import com.wurmonline.server.zones.VolaTile;
/*    */ import com.wurmonline.server.zones.Zones;
/*    */ import com.wurmonline.shared.constants.AttitudeConstants;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PainRain
/*    */   extends DamageSpell
/*    */   implements AttitudeConstants
/*    */ {
/*    */   public static final int RANGE = 24;
/*    */   public static final double BASE_DAMAGE = 6000.0D;
/*    */   public static final double DAMAGE_PER_POWER = 40.0D;
/*    */   public static final int RADIUS = 2;
/*    */   
/*    */   public PainRain() {
/* 43 */     super("Pain Rain", 432, 10, 40, 20, 40, 120000L);
/* 44 */     this.targetTile = true;
/* 45 */     this.offensive = true;
/* 46 */     this.description = "covers an area with damaging energy causing infection wounds on enemies";
/* 47 */     this.type = 2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/* 54 */     Structure currstr = performer.getCurrentTile().getStructure();
/*    */ 
/*    */     
/* 57 */     int radiusBonus = (int)(power / 40.0D);
/* 58 */     int sx = Zones.safeTileX(tilex - 2 - radiusBonus - performer.getNumLinks());
/* 59 */     int sy = Zones.safeTileY(tiley - 2 - radiusBonus - performer.getNumLinks());
/* 60 */     int ex = Zones.safeTileX(tilex + 2 + radiusBonus + performer.getNumLinks());
/* 61 */     int ey = Zones.safeTileY(tiley + 2 + radiusBonus + performer.getNumLinks());
/* 62 */     for (int x = sx; x < ex; x++) {
/*    */       
/* 64 */       for (int y = sy; y < ey; y++) {
/*    */         
/* 66 */         VolaTile t = Zones.getTileOrNull(x, y, (layer == 0));
/*    */         
/* 68 */         if (t != null) {
/*    */ 
/*    */ 
/*    */           
/* 72 */           Structure toCheck = t.getStructure();
/* 73 */           if (currstr == toCheck) {
/*    */ 
/*    */ 
/*    */             
/* 77 */             Item ring = Zones.isWithinDuelRing(x, y, (layer >= 0));
/* 78 */             if (ring == null) {
/*    */ 
/*    */               
/* 81 */               Creature[] crets = t.getCreatures();
/* 82 */               int affected = 0;
/* 83 */               for (Creature lCret : crets) {
/*    */                 
/* 85 */                 if (!lCret.isInvulnerable() && lCret.getAttitude(performer) == 2) {
/*    */                   
/* 87 */                   lCret.addAttacker(performer);
/*    */                   
/* 89 */                   double damage = calculateDamage(lCret, power, 6000.0D, 40.0D);
/*    */                   
/* 91 */                   lCret.addWoundOfType(performer, (byte)6, 1, true, 1.0F, false, damage, (float)power / 5.0F, 0.0F, false, true);
/*    */                   
/* 93 */                   affected++;
/*    */                 } 
/* 95 */                 if (affected > power / 10.0D + performer.getNumLinks())
/*    */                   break; 
/*    */               } 
/*    */             } 
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\PainRain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */