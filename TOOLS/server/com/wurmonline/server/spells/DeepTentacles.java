/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.structures.Structure;
/*     */ import com.wurmonline.server.zones.AreaSpellEffect;
/*     */ import com.wurmonline.server.zones.Zones;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeepTentacles
/*     */   extends ReligiousSpell
/*     */ {
/*     */   public static final int RANGE = 24;
/*     */   public static final double BASE_DAMAGE = 400.0D;
/*     */   public static final double DAMAGE_PER_SECOND = 1.0D;
/*     */   public static final int RADIUS = 1;
/*     */   
/*     */   public DeepTentacles() {
/*  47 */     super("Tentacles", 418, 10, 30, 20, 33, 120000L);
/*  48 */     this.targetTile = true;
/*  49 */     this.offensive = true;
/*  50 */     this.description = "covers an area with bludgeoning tentacles that damage enemies over time";
/*  51 */     this.type = 2;
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
/*  62 */     if (layer < 0) {
/*     */       
/*  64 */       int tile = Server.caveMesh.getTile(tilex, tiley);
/*  65 */       if (Tiles.isSolidCave(Tiles.decodeType(tile))) {
/*     */         
/*  67 */         performer.getCommunicator().sendNormalServerMessage("The spell doesn't work there.", (byte)3);
/*     */         
/*  69 */         return false;
/*     */       } 
/*     */     } 
/*  72 */     return true;
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
/*  84 */     int tile = Server.surfaceMesh.getTile(tilex, tiley);
/*  85 */     if (layer < 0) {
/*     */       
/*  87 */       tile = Server.caveMesh.getTile(tilex, tiley);
/*     */       
/*  89 */       byte type = Tiles.decodeType(tile);
/*  90 */       if (Tiles.isSolidCave(type)) {
/*     */         
/*  92 */         performer.getCommunicator().sendNormalServerMessage("You fail to find a spot to direct the power to.", (byte)3);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*  97 */     Structure currstr = performer.getCurrentTile().getStructure();
/*  98 */     performer.getCommunicator().sendNormalServerMessage("Waving tentacles appear around the " + 
/*  99 */         (Tiles.getTile(Tiles.decodeType(tile))).tiledesc.toLowerCase() + ".");
/* 100 */     int sx = Zones.safeTileX(tilex - 1 - performer.getNumLinks());
/* 101 */     int ex = Zones.safeTileX(tilex + 1 + performer.getNumLinks());
/* 102 */     int sy = Zones.safeTileY(tiley - 1 - performer.getNumLinks());
/* 103 */     int ey = Zones.safeTileY(tiley + 1 + performer.getNumLinks());
/*     */     
/* 105 */     calculateArea(sx, sy, ex, ey, tilex, tiley, layer, currstr);
/* 106 */     for (int x = sx; x <= ex; x++) {
/*     */       
/* 108 */       for (int y = sy; y <= ey; y++) {
/*     */         
/* 110 */         int currAreaX = x - sx;
/* 111 */         int currAreaY = y - sy;
/* 112 */         if (!this.area[currAreaX][currAreaY])
/* 113 */           new AreaSpellEffect(performer.getWurmId(), x, y, layer, (byte)34, System.currentTimeMillis() + 1000L * (30 + (int)power / 10), (float)power * 1.5F, layer, 0, true); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\DeepTentacles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */