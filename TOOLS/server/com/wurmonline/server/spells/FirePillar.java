/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.structures.Structure;
/*     */ import com.wurmonline.server.zones.AreaSpellEffect;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.AttitudeConstants;
/*     */ import java.util.logging.Logger;
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
/*     */ public class FirePillar
/*     */   extends ReligiousSpell
/*     */   implements AttitudeConstants
/*     */ {
/*  41 */   private static Logger logger = Logger.getLogger(FirePillar.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 24;
/*     */ 
/*     */   
/*     */   public static final double BASE_DAMAGE = 300.0D;
/*     */ 
/*     */   
/*     */   public static final double DAMAGE_PER_SECOND = 2.75D;
/*     */ 
/*     */   
/*     */   public static final int RADIUS = 2;
/*     */ 
/*     */   
/*     */   public FirePillar() {
/*  57 */     super("Fire Pillar", 420, 10, 30, 10, 37, 120000L);
/*     */     
/*  59 */     this.targetTile = true;
/*  60 */     this.offensive = true;
/*  61 */     this.description = "covers an area with fire dealing damage to enemies over time";
/*  62 */     this.type = 2;
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
/*  73 */     if (layer < 0) {
/*     */       
/*  75 */       int tile = Server.caveMesh.getTile(tilex, tiley);
/*  76 */       if (Tiles.isSolidCave(Tiles.decodeType(tile))) {
/*     */         
/*  78 */         performer.getCommunicator().sendNormalServerMessage("The spell doesn't work there.", (byte)3);
/*  79 */         return false;
/*     */       } 
/*     */     } 
/*  82 */     return true;
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
/*     */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/*  94 */     int tile = Server.surfaceMesh.getTile(tilex, tiley);
/*  95 */     if (layer < 0)
/*     */     {
/*  97 */       tile = Server.caveMesh.getTile(tilex, tiley);
/*     */     }
/*  99 */     byte type = Tiles.decodeType(tile);
/* 100 */     if (Tiles.isSolidCave(type)) {
/*     */       
/* 102 */       performer.getCommunicator().sendNormalServerMessage("You fail to find a spot to direct the power to.", (byte)3);
/*     */       
/*     */       return;
/*     */     } 
/* 106 */     performer.getCommunicator().sendNormalServerMessage("You heat the air around the " + 
/* 107 */         (Tiles.getTile(Tiles.decodeType(tile))).tiledesc.toLowerCase() + ".");
/* 108 */     Structure currstr = performer.getCurrentTile().getStructure();
/* 109 */     int sx = Zones.safeTileX(tilex - 2 - performer.getNumLinks());
/* 110 */     int ex = Zones.safeTileX(tilex + 2 + performer.getNumLinks());
/* 111 */     int sy = Zones.safeTileY(tiley - 2 - performer.getNumLinks());
/* 112 */     int ey = Zones.safeTileY(tiley + 2 + performer.getNumLinks());
/*     */     
/* 114 */     VolaTile tileTarget = Zones.getOrCreateTile(tilex, tiley, (layer >= 0));
/* 115 */     Structure targetStructure = null;
/* 116 */     if (heightOffset != -1)
/*     */     {
/* 118 */       targetStructure = tileTarget.getStructure();
/*     */     }
/*     */     
/* 121 */     calculateAOE(sx, sy, ex, ey, tilex, tiley, layer, currstr, targetStructure, heightOffset);
/*     */     
/* 123 */     for (int x = sx; x <= ex; x++) {
/*     */       
/* 125 */       for (int y = sy; y <= ey; y++) {
/*     */         
/* 127 */         if (!((tilex == x && y == tiley) ? 1 : 0)) {
/*     */           
/* 129 */           int currAreaX = x - sx;
/* 130 */           int currAreaY = y - sy;
/* 131 */           if (!this.area[currAreaX][currAreaY])
/*     */           {
/* 133 */             new AreaSpellEffect(performer.getWurmId(), x, y, layer, (byte)51, System.currentTimeMillis() + 1000L * (30 + (int)power / 10), (float)power * 2.0F, layer, this.offsets[currAreaX][currAreaY], true);
/*     */           
/*     */           }
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 140 */           int groundHeight = 0;
/* 141 */           if (targetStructure == null || targetStructure.isTypeHouse()) {
/*     */             
/* 143 */             float[] hts = Zones.getNodeHeights(tilex, tiley, layer, -10L);
/*     */             
/* 145 */             float h = hts[0] * 0.5F * 0.5F + hts[1] * 0.5F * 0.5F + hts[2] * 0.5F * 0.5F + hts[3] * 0.5F * 0.5F;
/*     */             
/* 147 */             groundHeight = (int)(h * 10.0F);
/*     */           } 
/* 149 */           new AreaSpellEffect(performer.getWurmId(), x, y, layer, (byte)35, System.currentTimeMillis() + 1000L * (30 + (int)power / 10), (float)power * 2.0F, layer, heightOffset + groundHeight, true);
/*     */ 
/*     */ 
/*     */           
/* 153 */           if (Server.rand.nextInt(1000) < power)
/*     */           {
/* 155 */             if (layer >= 0)
/*     */             {
/* 157 */               if (Tiles.canSpawnTree(type) || Tiles.isTree(type) || Tiles.isEnchanted(type)) {
/*     */                 
/* 159 */                 Server.setSurfaceTile(x, y, Tiles.decodeHeight(tile), Tiles.Tile.TILE_SAND.id, (byte)0);
/*     */                 
/* 161 */                 Players.getInstance().sendChangedTile(x, y, true, true);
/*     */               } 
/*     */             }
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\FirePillar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */