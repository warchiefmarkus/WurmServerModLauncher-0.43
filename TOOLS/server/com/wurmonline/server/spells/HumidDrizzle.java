/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.skills.Skill;
/*    */ import com.wurmonline.server.zones.VolaTile;
/*    */ import com.wurmonline.server.zones.Zones;
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
/*    */ public class HumidDrizzle
/*    */   extends ReligiousSpell
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   public HumidDrizzle() {
/* 33 */     super("Humid Drizzle", 407, 30, 30, 20, 21, 30000L);
/* 34 */     this.targetTile = true;
/* 35 */     this.description = "tends to animals in area";
/* 36 */     this.type = 1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/* 43 */     performer.getCommunicator().sendNormalServerMessage("You tend to the animals here.", (byte)2);
/*    */     
/* 45 */     int sx = Zones.safeTileX(tilex - 5 - performer.getNumLinks());
/* 46 */     int sy = Zones.safeTileY(tiley - 5 - performer.getNumLinks());
/* 47 */     int ex = Zones.safeTileX(tilex + 5 + performer.getNumLinks());
/* 48 */     int ey = Zones.safeTileY(tiley + 5 + performer.getNumLinks());
/* 49 */     for (int x = sx; x < ex; x++) {
/*    */       
/* 51 */       for (int y = sy; y < ey; y++) {
/*    */         
/* 53 */         VolaTile t = Zones.getTileOrNull(x, y, performer.isOnSurface());
/* 54 */         if (t != null) {
/*    */           
/* 56 */           Creature[] crets = t.getCreatures();
/* 57 */           for (Creature lCret : crets) {
/*    */             
/* 59 */             if (!lCret.isMonster() && !lCret.isPlayer())
/*    */             {
/*    */               
/* 62 */               if (SpellResist.getSpellResistance(lCret, getNumber()) >= 1.0D) {
/*    */                 
/* 64 */                 lCret.setMilked(false);
/* 65 */                 lCret.setLastGroomed(System.currentTimeMillis());
/* 66 */                 lCret.getBody().healFully();
/* 67 */                 performer.getCommunicator().sendNormalServerMessage(lCret.getNameWithGenus() + " now shines with health.");
/*    */                 
/* 69 */                 SpellResist.addSpellResistance(lCret, getNumber(), power);
/*    */               } 
/*    */             }
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\HumidDrizzle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */