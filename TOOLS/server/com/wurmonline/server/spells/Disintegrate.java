/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.behaviours.Action;
/*     */ import com.wurmonline.server.behaviours.NoSuchActionException;
/*     */ import com.wurmonline.server.behaviours.TileRockBehaviour;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.utils.logging.TileEvent;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.VillageRole;
/*     */ import com.wurmonline.server.villages.Villages;
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
/*     */ public class Disintegrate
/*     */   extends ReligiousSpell
/*     */ {
/*     */   public static final int RANGE = 4;
/*     */   
/*     */   public Disintegrate() {
/*  44 */     super("Disintegrate", 449, 60, 80, 70, 70, 0L);
/*  45 */     this.targetTile = true;
/*  46 */     this.description = "destroys cave walls and reinforcements";
/*  47 */     this.type = 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, int tilex, int tiley, int layer) {
/*  58 */     if (layer < 0) {
/*     */       
/*  60 */       int tile = Server.caveMesh.getTile(tilex, tiley);
/*  61 */       if (Tiles.isSolidCave(Tiles.decodeType(tile))) {
/*     */         
/*  63 */         if (Tiles.isReinforcedCave(Tiles.decodeType(tile)))
/*     */         {
/*  65 */           if (Servers.localServer.PVPSERVER) {
/*     */             
/*  67 */             Village v = Villages.getVillage(tilex, tiley, true);
/*  68 */             if (v != null)
/*     */             {
/*  70 */               boolean ok = false;
/*     */               
/*  72 */               VillageRole r = v.getRoleFor(performer);
/*  73 */               if (r != null && r.mayMineRock() && r.mayReinforce()) {
/*  74 */                 ok = true;
/*     */               }
/*  76 */               if (!ok && System.currentTimeMillis() - v.plan.getLastDrained() > 7200000L)
/*     */               {
/*  78 */                 performer
/*  79 */                   .getCommunicator()
/*  80 */                   .sendNormalServerMessage("The settlement has not been drained during the last two hours and the wall still stands this time.", (byte)3);
/*     */ 
/*     */                 
/*  83 */                 return false;
/*     */               }
/*     */             
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/*  90 */             Village v = Villages.getVillage(tilex, tiley, true);
/*  91 */             if (v != null) {
/*     */               
/*  93 */               VillageRole r = v.getRoleFor(performer);
/*  94 */               return (r != null && r.mayMineRock() && r.mayReinforce());
/*     */             } 
/*     */           } 
/*     */         }
/*  98 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 102 */     performer.getCommunicator().sendNormalServerMessage("This spell works on rock below ground.");
/* 103 */     return false;
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
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/*     */     try {
/* 117 */       Action act = performer.getCurrentAction();
/* 118 */       int dir = (int)(act.getTarget() >> 48L) & 0xFF;
/* 119 */       if (dir == 1) {
/*     */         
/* 121 */         performer.getCommunicator().sendNormalServerMessage("The roof just resounds hollowly.", (byte)3);
/*     */         return;
/*     */       } 
/* 124 */       if (dir == 0) {
/*     */         
/* 126 */         performer.getCommunicator().sendNormalServerMessage("The floor just resounds hollowly.", (byte)3);
/*     */         return;
/*     */       } 
/* 129 */       int tile = Server.caveMesh.getTile(tilex, tiley);
/*     */       
/* 131 */       byte type = Tiles.decodeType(tile);
/* 132 */       boolean dis = true;
/*     */       
/* 134 */       if (Tiles.isReinforcedCave(type)) {
/*     */         int num;
/*     */         
/* 137 */         if (performer.getDeity().isWarrior()) {
/* 138 */           num = Math.max(1, 100 - (int)power);
/*     */         } else {
/* 140 */           num = Math.max(1, 200 - 2 * (int)power);
/*     */         } 
/* 142 */         if (Server.rand.nextInt(Servers.localServer.testServer ? 1 : num) != 0) {
/*     */           
/* 144 */           performer.getCommunicator().sendNormalServerMessage("You fail to find a weak spot to direct the power to. The wall still stands this time.", (byte)3);
/*     */ 
/*     */           
/* 147 */           dis = false;
/*     */         }
/*     */         else {
/*     */           
/* 151 */           Server.setCaveResource(tilex, tiley, Server.rand.nextInt(100) + 50);
/* 152 */           Server.caveMesh.setTile(tilex, tiley, 
/* 153 */               Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_CAVE_WALL.id, Tiles.decodeData(tile)));
/* 154 */           Players.getInstance().sendChangedTile(tilex, tiley, false, false);
/* 155 */           TileEvent.log(performer.getTileX(), performer.getTileY(), performer.getLayer(), performer
/* 156 */               .getWurmId(), 449);
/* 157 */           Tiles.Tile t = Tiles.getTile(type);
/* 158 */           if (t != null) {
/* 159 */             logger.info(performer.getName() + " (" + performer.getWurmId() + ") disintegrated a " + t
/* 160 */                 .getName() + " at [" + tilex + "," + tiley + ",false]");
/*     */           }
/* 162 */           performer.getCommunicator().sendNormalServerMessage("You disintegrate the reinforcement.", (byte)2);
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */       
/* 168 */       if (dis && TileRockBehaviour.createInsideTunnel(tilex, tiley, tile, performer, 145, dir, true, act))
/*     */       {
/* 170 */         performer.getCommunicator().sendNormalServerMessage("You disintegrate the " + 
/* 171 */             (Tiles.getTile(Tiles.decodeType(tile))).tiledesc.toLowerCase() + ".", (byte)2);
/*     */       
/*     */       }
/*     */     }
/* 175 */     catch (NoSuchActionException nsa) {
/*     */       
/* 177 */       performer.getCommunicator().sendNormalServerMessage("You fail to channel the spell. If this happens regurarly, talk to the gods.");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Disintegrate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */