/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.behaviours.MethodsCreatures;
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.endgames.EndGameItems;
/*    */ import com.wurmonline.server.skills.Skill;
/*    */ import com.wurmonline.server.villages.Village;
/*    */ import com.wurmonline.server.villages.Villages;
/*    */ import com.wurmonline.server.zones.Zones;
/*    */ import java.awt.Rectangle;
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
/*    */ public class RevealSettlements
/*    */   extends ReligiousSpell
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   public RevealSettlements() {
/* 37 */     super("Reveal Settlements", 443, 20, 30, 25, 30, 0L);
/* 38 */     this.targetTile = true;
/* 39 */     this.description = "locates nearby settlements";
/* 40 */     this.type = 2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/* 47 */     performer.getCommunicator().sendNormalServerMessage("You receive insights about the area.");
/*    */     
/* 49 */     int sx = Zones.safeTileX(performer.getTileX() - 100 - performer.getNumLinks() * 20);
/* 50 */     int sy = Zones.safeTileY(performer.getTileY() - 100 - performer.getNumLinks() * 20);
/* 51 */     int ex = Zones.safeTileX(performer.getTileX() + 100 + performer.getNumLinks() * 20);
/* 52 */     int ey = Zones.safeTileY(performer.getTileY() + 100 + performer.getNumLinks() * 20);
/* 53 */     Rectangle zoneRect = new Rectangle(sx, sy, ex - sx, ey - sy);
/* 54 */     Village[] vills = Villages.getVillages();
/* 55 */     for (Village vill : vills) {
/*    */       
/* 57 */       if (vill != performer.getCurrentVillage()) {
/*    */         
/* 59 */         Rectangle villageRect = new Rectangle(vill.startx, vill.starty, vill.endx - vill.startx + 1, vill.endy - vill.starty + 1);
/* 60 */         if (villageRect.intersects(zoneRect)) {
/*    */           
/* 62 */           int centerx = (int)villageRect.getCenterX();
/* 63 */           int centery = (int)villageRect.getCenterY();
/* 64 */           int mindist = Math.max(Math.abs(centerx - performer.getTileX()), Math.abs(centery - performer.getTileY()));
/* 65 */           int dir = MethodsCreatures.getDir(performer, centerx, centery);
/* 66 */           String direction = MethodsCreatures.getLocationStringFor(performer.getStatus().getRotation(), dir, "you");
/* 67 */           String toReturn = EndGameItems.getDistanceString(mindist, vill.getName(), direction, true);
/* 68 */           performer.getCommunicator().sendNormalServerMessage(toReturn);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\RevealSettlements.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */