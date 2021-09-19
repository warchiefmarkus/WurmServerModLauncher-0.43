/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.Server;
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
/*    */ public class Ward
/*    */   extends ReligiousSpell
/*    */ {
/*    */   public static final int RANGE = 40;
/*    */   
/*    */   public Ward() {
/* 33 */     super("Ward", 437, 20, 20, 20, 43, 0L);
/* 34 */     this.targetTile = true;
/* 35 */     this.description = "drives away enemy creatures";
/* 36 */     this.type = 2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/* 43 */     performer.getCommunicator().sendNormalServerMessage("You place the Mark of Fo in the area, declaring a sanctuary.");
/*    */     
/* 45 */     int sx = Zones.safeTileX(tilex - (int)Math.max(5.0D, power / 10.0D) + performer.getNumLinks());
/* 46 */     int sy = Zones.safeTileY(tiley - (int)Math.max(5.0D, power / 10.0D) + performer.getNumLinks());
/* 47 */     int ex = Zones.safeTileX(tilex + (int)Math.max(5.0D, power / 10.0D) + performer.getNumLinks());
/* 48 */     int ey = Zones.safeTileY(tiley + (int)Math.max(5.0D, power / 10.0D) + performer.getNumLinks());
/* 49 */     for (int x = sx; x < ex; x++) {
/*    */       
/* 51 */       for (int y = sy; y < ey; y++) {
/*    */         
/* 53 */         VolaTile t = Zones.getOrCreateTile(x, y, performer.isOnSurface());
/* 54 */         if (t != null) {
/*    */           
/* 56 */           Creature[] crets = t.getCreatures();
/* 57 */           for (Creature cret : crets) {
/*    */             
/* 59 */             if (!cret.isPlayer() && !cret.isHuman() && !cret.isSpiritGuard() && 
/* 60 */               !cret.isUnique() && cret.getLoyalty() <= 0.0F && !cret.isRidden()) {
/*    */               
/* 62 */               cret.setTarget(-10L, true);
/* 63 */               if (cret.opponent != null) {
/*    */                 
/* 65 */                 cret.opponent.setTarget(-10L, true);
/* 66 */                 if (cret.opponent != null)
/* 67 */                   cret.opponent.setOpponent(null); 
/*    */               } 
/* 69 */               cret.setOpponent(null);
/* 70 */               cret.setFleeCounter(20, true);
/* 71 */               Server.getInstance().broadCastAction(cret.getName() + " panics.", cret, 10);
/*    */             } 
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Ward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */