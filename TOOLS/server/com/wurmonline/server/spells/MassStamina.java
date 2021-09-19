/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.skills.Skill;
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
/*    */ public class MassStamina
/*    */   extends ReligiousSpell
/*    */   implements AttitudeConstants
/*    */ {
/*    */   public static final int RANGE = 12;
/*    */   
/*    */   public MassStamina() {
/* 36 */     super("Mass Stamina", 425, 15, 50, 20, 40, 900000L);
/* 37 */     this.targetTile = true;
/* 38 */     this.description = "covers an area with revitalising energy, refreshing stamina for allies";
/* 39 */     this.type = 2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/* 46 */     int sx = Zones.safeTileX(tilex - 5 - performer.getNumLinks());
/* 47 */     int sy = Zones.safeTileY(tiley - 5 - performer.getNumLinks());
/* 48 */     int ex = Zones.safeTileX(tilex + 5 + performer.getNumLinks());
/* 49 */     int ey = Zones.safeTileY(tiley + 5 + performer.getNumLinks());
/* 50 */     for (int x = sx; x < ex; x++) {
/*    */       
/* 52 */       for (int y = sy; y < ey; y++) {
/*    */         
/* 54 */         VolaTile t = Zones.getTileOrNull(x, y, performer.isOnSurface());
/* 55 */         if (t != null) {
/*    */           
/* 57 */           Creature[] crets = t.getCreatures();
/* 58 */           for (Creature lCret : crets) {
/*    */             
/* 60 */             if (lCret.getAttitude(performer) != 2)
/*    */             {
/* 62 */               lCret.getStatus().modifyStamina2(100.0F);
/*    */             }
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\MassStamina.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */