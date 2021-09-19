/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.Server;
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.creatures.CreatureMove;
/*    */ import com.wurmonline.server.items.Item;
/*    */ import com.wurmonline.server.players.MovementEntity;
/*    */ import com.wurmonline.server.skills.Skill;
/*    */ import com.wurmonline.server.zones.VirtualZone;
/*    */ import com.wurmonline.server.zones.VolaTile;
/*    */ import com.wurmonline.server.zones.Zones;
/*    */ import java.util.logging.Level;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MirroredSelf
/*    */   extends KarmaSpell
/*    */ {
/*    */   public MirroredSelf() {
/* 45 */     super("Mirrored Self", 562, 5, 500, 20, 1, 900000L);
/* 46 */     this.targetTile = true;
/* 47 */     this.targetItem = true;
/* 48 */     this.description = "creates deceptive illusions of yourself around you";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/* 55 */     castMirroredSelf(performer, Math.max(10.0D, power));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, Item target) {
/* 61 */     castMirroredSelf(performer, Math.max(10.0D, power));
/*    */   }
/*    */ 
/*    */   
/*    */   private void castMirroredSelf(Creature performer, double power) {
/* 66 */     int nums = 2 + (int)power / 10;
/* 67 */     int x = 0;
/* 68 */     int y = 0;
/* 69 */     for (int n = 0; n < nums; n++) {
/*    */ 
/*    */       
/* 72 */       MovementEntity entity = new MovementEntity(performer.getWurmId(), System.currentTimeMillis() + 1000L * Math.max(20L, (long)power));
/* 73 */       CreatureMove startPos = new CreatureMove();
/* 74 */       startPos.diffX = (byte)(-1 + Server.rand.nextInt(2));
/* 75 */       startPos.diffY = (byte)(-1 + Server.rand.nextInt(2));
/* 76 */       startPos.diffZ = 0.0F;
/* 77 */       entity.setMovePosition(startPos);
/* 78 */       performer.addIllusion(entity);
/* 79 */       VolaTile tile = Zones.getOrCreateTile(performer.getTileX() + 0, performer.getTileY() + 0, performer
/* 80 */           .isOnSurface());
/* 81 */       for (VirtualZone vz : tile.getWatchers()) {
/*    */ 
/*    */         
/*    */         try {
/* 85 */           float posZ = Zones.calculatePosZ(((performer.getTileX() + 0) * 4), ((performer.getTileY() + 0) * 4), tile, performer
/* 86 */               .isOnSurface(), false, performer.getPositionZ(), performer, -10L);
/* 87 */           float diffZ = performer.getPositionZ() - posZ;
/*    */           
/*    */           try {
/* 90 */             vz.addCreature(performer.getWurmId(), false, entity.getWurmid(), 0.0F, 0.0F, diffZ);
/*    */           }
/* 92 */           catch (Exception exception) {}
/*    */ 
/*    */ 
/*    */         
/*    */         }
/* 97 */         catch (Exception e) {
/*    */           
/* 99 */           logger.log(Level.WARNING, e.getMessage(), e);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\MirroredSelf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */