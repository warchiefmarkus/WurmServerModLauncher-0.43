/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.structures.Structure;
/*     */ import com.wurmonline.server.zones.AreaSpellEffect;
/*     */ import com.wurmonline.server.zones.VolaTile;
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
/*     */ public class FungusTrap
/*     */   extends ReligiousSpell
/*     */ {
/*     */   public static final int RANGE = 24;
/*     */   public static final double BASE_DAMAGE = 350.0D;
/*     */   public static final double DAMAGE_PER_SECOND = 2.0D;
/*     */   public static final int RADIUS = 2;
/*     */   
/*     */   public FungusTrap() {
/*  47 */     super("Fungus Trap", 433, 10, 23, 20, 33, 120000L);
/*  48 */     this.targetTile = true;
/*  49 */     this.offensive = true;
/*  50 */     this.description = "covers an area with fungus that deals damage to enemies over time";
/*  51 */     this.type = 1;
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
/*  85 */     if (layer < 0)
/*     */     {
/*  87 */       tile = Server.caveMesh.getTile(tilex, tiley);
/*     */     }
/*  89 */     byte type = Tiles.decodeType(tile);
/*  90 */     if (Tiles.isSolidCave(type)) {
/*     */       
/*  92 */       performer.getCommunicator().sendNormalServerMessage("You fail to find a spot to direct the power to.", (byte)3);
/*     */       
/*     */       return;
/*     */     } 
/*  96 */     performer.getCommunicator().sendNormalServerMessage("You call upon the mycelium!", (byte)2);
/*     */     
/*  98 */     Structure currstr = performer.getCurrentTile().getStructure();
/*  99 */     int sx = Zones.safeTileX(tilex - 2 - performer.getNumLinks());
/* 100 */     int ex = Zones.safeTileX(tilex + 2 + performer.getNumLinks());
/* 101 */     int sy = Zones.safeTileY(tiley - 2 - performer.getNumLinks());
/* 102 */     int ey = Zones.safeTileY(tiley + 2 + performer.getNumLinks());
/*     */     
/* 104 */     VolaTile tileTarget = Zones.getOrCreateTile(tilex, tiley, (layer >= 0));
/* 105 */     Structure targetStructure = null;
/* 106 */     if (heightOffset != -1)
/*     */     {
/* 108 */       targetStructure = tileTarget.getStructure();
/*     */     }
/*     */     
/* 111 */     calculateAOE(sx, sy, ex, ey, tilex, tiley, layer, currstr, targetStructure, heightOffset);
/*     */     
/* 113 */     for (int x = sx; x <= ex; x++) {
/*     */       
/* 115 */       for (int y = sy; y <= ey; y++) {
/*     */         
/* 117 */         int currAreaX = x - sx;
/* 118 */         int currAreaY = y - sy;
/* 119 */         if (!this.area[currAreaX][currAreaY])
/*     */         {
/* 121 */           new AreaSpellEffect(performer.getWurmId(), x, y, layer, (byte)37, System.currentTimeMillis() + 1000L * (30 + (int)power / 10), (float)power * 1.5F, layer, this.offsets[currAreaX][currAreaY], true);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\FungusTrap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */