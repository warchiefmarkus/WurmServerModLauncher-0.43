/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.structures.Structure;
/*     */ import com.wurmonline.server.zones.AreaSpellEffect;
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
/*     */ public class IcePillar
/*     */   extends ReligiousSpell
/*     */   implements AttitudeConstants
/*     */ {
/*  40 */   private static Logger logger = Logger.getLogger(IcePillar.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 24;
/*     */ 
/*     */   
/*     */   public static final double BASE_DAMAGE = 150.0D;
/*     */ 
/*     */   
/*     */   public static final double DAMAGE_PER_SECOND = 4.0D;
/*     */ 
/*     */   
/*     */   public static final int RADIUS = 2;
/*     */ 
/*     */   
/*     */   public IcePillar() {
/*  56 */     super("Ice Pillar", 414, 10, 30, 10, 35, 120000L);
/*     */     
/*  58 */     this.targetTile = true;
/*  59 */     this.offensive = true;
/*  60 */     this.description = "covers an area with frost dealing damage to enemies over time";
/*  61 */     this.type = 2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, int tilex, int tiley, int layer) {
/*  67 */     if (layer < 0) {
/*     */       
/*  69 */       int tile = Server.caveMesh.getTile(tilex, tiley);
/*  70 */       if (Tiles.isSolidCave(Tiles.decodeType(tile))) {
/*     */         
/*  72 */         performer.getCommunicator().sendNormalServerMessage("The spell doesn't work there.", (byte)3);
/*  73 */         return false;
/*     */       } 
/*     */     } 
/*  76 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/*  83 */     int tile = Server.surfaceMesh.getTile(tilex, tiley);
/*  84 */     if (layer < 0)
/*     */     {
/*  86 */       tile = Server.caveMesh.getTile(tilex, tiley);
/*     */     }
/*  88 */     byte type = Tiles.decodeType(tile);
/*  89 */     if (Tiles.isSolidCave(type)) {
/*     */       
/*  91 */       performer.getCommunicator().sendNormalServerMessage("You fail to find a spot to direct the power to.", (byte)3);
/*     */       
/*     */       return;
/*     */     } 
/*  95 */     performer.getCommunicator().sendNormalServerMessage("You freeze the air around the " + 
/*  96 */         (Tiles.getTile(Tiles.decodeType(tile))).tiledesc.toLowerCase() + ".");
/*  97 */     Structure currstr = performer.getCurrentTile().getStructure();
/*     */     
/*  99 */     int sx = Zones.safeTileX(tilex - 2 - performer.getNumLinks());
/* 100 */     int ex = Zones.safeTileX(tilex + 2 + performer.getNumLinks());
/* 101 */     int sy = Zones.safeTileY(tiley - 2 - performer.getNumLinks());
/* 102 */     int ey = Zones.safeTileY(tiley + 2 + performer.getNumLinks());
/*     */     
/* 104 */     calculateArea(sx, sy, ex, ey, tilex, tiley, layer, currstr);
/*     */     
/* 106 */     for (int x = sx; x <= ex; x++) {
/*     */       
/* 108 */       for (int y = sy; y <= ey; y++) {
/*     */         
/* 110 */         if (tilex == x && y == tiley) {
/*     */           
/* 112 */           new AreaSpellEffect(performer.getWurmId(), x, y, layer, (byte)36, System.currentTimeMillis() + 1000L * (30 + (int)power / 10), (float)power * 2.0F, layer, 0, true);
/*     */           
/* 114 */           if (Server.rand.nextInt(1000) < power)
/*     */           {
/* 116 */             if (layer >= 0)
/*     */             {
/* 118 */               if (Tiles.canSpawnTree(type) || Tiles.isEnchanted(type))
/*     */               {
/* 120 */                 Server.setSurfaceTile(x, y, 
/* 121 */                     Tiles.decodeHeight(tile), Tiles.Tile.TILE_TUNDRA.id, (byte)0);
/* 122 */                 Players.getInstance().sendChangedTile(x, y, true, true);
/*     */               }
/*     */             
/*     */             }
/*     */           }
/*     */         } else {
/*     */           
/* 129 */           int currAreaX = x - sx;
/* 130 */           int currAreaY = y - sy;
/* 131 */           if (!this.area[currAreaX][currAreaY])
/*     */           {
/* 133 */             new AreaSpellEffect(performer.getWurmId(), x, y, layer, (byte)53, System.currentTimeMillis() + 1000L * (30 + (int)power / 10), (float)power * 2.0F, 0, 0, true);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\IcePillar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */