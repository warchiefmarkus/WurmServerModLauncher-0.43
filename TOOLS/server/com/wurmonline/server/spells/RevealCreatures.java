/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.behaviours.MethodsCreatures;
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.creatures.DbCreatureStatus;
/*    */ import com.wurmonline.server.endgames.EndGameItems;
/*    */ import com.wurmonline.server.skills.Skill;
/*    */ import com.wurmonline.server.zones.Zone;
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
/*    */ public class RevealCreatures
/*    */   extends ReligiousSpell
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   public RevealCreatures() {
/* 36 */     super("Reveal Creatures", 444, 40, 30, 25, 30, 0L);
/* 37 */     this.targetTile = true;
/* 38 */     this.description = "locates creatures nearby";
/* 39 */     this.type = 2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/* 46 */     performer.getCommunicator().sendNormalServerMessage("You receive insights about the area.");
/*    */     
/* 48 */     int sx = Zones.safeTileX(performer.getTileX() - 40 - performer.getNumLinks() * 5);
/* 49 */     int sy = Zones.safeTileY(performer.getTileY() - 40 - performer.getNumLinks() * 5);
/* 50 */     int ex = Zones.safeTileX(performer.getTileX() + 40 + performer.getNumLinks() * 5);
/* 51 */     int ey = Zones.safeTileY(performer.getTileY() + 40 + performer.getNumLinks() * 5);
/*    */     
/* 53 */     Zone[] zones = Zones.getZonesCoveredBy(sx, sy, ex, ey, performer.isOnSurface());
/* 54 */     for (Zone lZone : zones) {
/*    */       
/* 56 */       Creature[] crets = lZone.getAllCreatures();
/* 57 */       for (Creature cret : crets) {
/*    */         
/* 59 */         if (cret.getPower() <= performer.getPower())
/*    */         {
/* 61 */           if (cret != performer)
/*    */           {
/* 63 */             if (cret.getBonusForSpellEffect((byte)29) <= 0.0F) {
/*    */               String toReturn;
/* 65 */               int mindist = Math.max(Math.abs(cret.getTileX() - performer.getTileX()), Math.abs(cret.getTileY() - performer.getTileY()));
/* 66 */               int dir = MethodsCreatures.getDir(performer, cret.getTileX(), cret.getTileY());
/* 67 */               String direction = MethodsCreatures.getLocationStringFor(performer.getStatus().getRotation(), dir, "you");
/*    */               
/* 69 */               if (DbCreatureStatus.getIsLoaded(cret.getWurmId()) == 0) {
/*    */                 
/* 71 */                 toReturn = EndGameItems.getDistanceString(mindist, cret.getName(), direction, false);
/*    */               }
/*    */               else {
/*    */                 
/* 75 */                 toReturn = "";
/*    */               } 
/* 77 */               performer.getCommunicator().sendNormalServerMessage(toReturn);
/*    */             } 
/*    */           }
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\RevealCreatures.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */