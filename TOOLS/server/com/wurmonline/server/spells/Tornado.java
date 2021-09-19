/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.mesh.FoliageAge;
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.mesh.TreeData;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.combat.CombatEngine;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.AttitudeConstants;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.Ellipse2D;
/*     */ import java.util.logging.Level;
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
/*     */ public class Tornado
/*     */   extends DamageSpell
/*     */   implements AttitudeConstants
/*     */ {
/*  47 */   private static Logger logger = Logger.getLogger(Tornado.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 24;
/*     */   
/*     */   public static final double BASE_DAMAGE = 4000.0D;
/*     */   
/*     */   public static final double DAMAGE_PER_POWER = 80.0D;
/*     */   
/*     */   public static final int RADIUS = 2;
/*     */ 
/*     */   
/*     */   public Tornado() {
/*  60 */     super("Tornado", 413, 15, 50, 30, 40, 120000L);
/*  61 */     this.targetTile = true;
/*  62 */     this.offensive = true;
/*  63 */     this.description = "covers an area with extreme winds that can damage enemies and trees";
/*  64 */     this.type = 2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, int tilex, int tiley, int layer) {
/*  70 */     if (performer.getLayer() < 0) {
/*     */       
/*  72 */       performer.getCommunicator().sendNormalServerMessage("You must be above ground to cast this spell.", (byte)3);
/*     */       
/*  74 */       return false;
/*     */     } 
/*  76 */     VolaTile t = Zones.getTileOrNull(tilex, tiley, performer.isOnSurface());
/*  77 */     if (t != null && t.getStructure() != null) {
/*     */       
/*  79 */       performer.getCommunicator().sendNormalServerMessage("You can't cast this inside.", (byte)3);
/*  80 */       return false;
/*     */     } 
/*  82 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/*  89 */     performer.getCommunicator().sendNormalServerMessage("You call upon the wind of Vynora.");
/*     */     
/*  91 */     int radiusBonus = (int)(power / 40.0D);
/*  92 */     int sx = Zones.safeTileX(tilex - 2 - radiusBonus - performer.getNumLinks());
/*  93 */     int sy = Zones.safeTileY(tiley - 2 - radiusBonus - performer.getNumLinks());
/*  94 */     int ex = Zones.safeTileX(tilex + 2 + radiusBonus + performer.getNumLinks());
/*  95 */     int ey = Zones.safeTileY(tiley + 2 + radiusBonus + performer.getNumLinks());
/*     */     
/*  97 */     Shape circle = new Ellipse2D.Float(sx, sy, (ex - sx), (ey - sy));
/*  98 */     for (int x = sx; x < ex; x++) {
/*     */       
/* 100 */       for (int y = sy; y < ey; y++) {
/*     */         
/* 102 */         if (circle.contains(x, y)) {
/*     */           
/* 104 */           int tile = Server.surfaceMesh.getTile(x, y);
/* 105 */           byte type = Tiles.decodeType(tile);
/* 106 */           Tiles.Tile theTile = Tiles.getTile(type);
/* 107 */           byte data = Tiles.decodeData(tile);
/* 108 */           if (type == Tiles.Tile.TILE_FIELD.id || type == Tiles.Tile.TILE_FIELD2.id) {
/*     */             
/* 110 */             int worldResource = Server.getWorldResource(x, y);
/* 111 */             int farmedCount = worldResource >>> 11;
/* 112 */             int farmedChance = worldResource & 0x7FF;
/* 113 */             farmedChance = (int)Math.min(farmedChance - power / 7.0D, 2047.0D);
/* 114 */             Server.setWorldResource(x, y, (farmedCount << 11) + farmedChance);
/*     */           }
/* 116 */           else if (theTile.isNormalTree() || theTile.isMyceliumTree()) {
/*     */             
/* 118 */             byte treeAge = FoliageAge.getAgeAsByte(data);
/* 119 */             TreeData.TreeType treeType = theTile.getTreeType(data);
/* 120 */             if (treeAge == 15 || Server.rand.nextInt(16 - treeAge) != 0) {
/*     */               
/* 122 */               byte newt = Tiles.Tile.TILE_GRASS.id;
/* 123 */               if (theTile.isMyceliumTree())
/* 124 */                 newt = Tiles.Tile.TILE_MYCELIUM.id; 
/* 125 */               Server.setSurfaceTile(x, y, Tiles.decodeHeight(tile), newt, (byte)0);
/* 126 */               Server.setWorldResource(x, y, 0);
/*     */ 
/*     */ 
/*     */               
/* 130 */               int templateId = 9;
/* 131 */               if (treeAge >= FoliageAge.OLD_ONE.getAgeId() && treeAge < FoliageAge.SHRIVELLED.getAgeId() && !treeType.isFruitTree())
/* 132 */                 templateId = 385; 
/* 133 */               double sizeMod = treeAge / 15.0D;
/* 134 */               if (!treeType.isFruitTree())
/* 135 */                 sizeMod *= 0.25D; 
/* 136 */               double lNewRotation = Math.atan2(((y << 2) + 2 - (y << 2) + 2), ((x << 2) + 2 - (x << 2) + 2));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 143 */               float rot = (float)(lNewRotation * 57.29577951308232D);
/*     */               
/*     */               try {
/* 146 */                 Item newItem = ItemFactory.createItem(templateId, (float)power / 5.0F, (x * 4 + Server.rand
/* 147 */                     .nextInt(4)), (y * 4 + Server.rand.nextInt(4)), rot, performer
/* 148 */                     .isOnSurface(), treeType.getMaterial(), (byte)0, -10L, null, treeAge);
/* 149 */                 newItem.setWeight((int)Math.max(1000.0D, sizeMod * newItem.getWeightGrams()), true);
/* 150 */                 newItem.setLastOwnerId(performer.getWurmId());
/*     */               }
/* 152 */               catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */               
/* 156 */               Players.getInstance().sendChangedTile(x, y, true, false);
/*     */             } 
/*     */           } 
/* 159 */           VolaTile t = Zones.getTileOrNull(x, y, performer.isOnSurface());
/* 160 */           if (t != null && t.getStructure() == null)
/*     */           {
/* 162 */             if ((t.getFences()).length <= 0) {
/*     */               
/* 164 */               Creature[] crets = t.getCreatures();
/* 165 */               int affected = 0;
/* 166 */               for (int c = 0; c < crets.length; c++) {
/*     */                 
/* 168 */                 if (!crets[c].isGhost() && !crets[c].isDead())
/*     */                 {
/* 170 */                   if (crets[c].getAttitude(performer) == 2) {
/*     */                     
/*     */                     try {
/*     */                       
/* 174 */                       byte pos = crets[c].getBody().getRandomWoundPos();
/*     */ 
/*     */                       
/* 177 */                       double damage = calculateDamage(crets[c], power, 4000.0D, 80.0D);
/*     */                       
/* 179 */                       CombatEngine.addWound(performer, crets[c], (byte)0, pos, damage, 1.0F, "assault", performer
/* 180 */                           .getBattle(), 0.0F, 0.0F, false, false, false, true);
/* 181 */                       performer.getStatus().setStunned(5.0F);
/* 182 */                       affected++;
/*     */                     }
/* 184 */                     catch (Exception exe) {
/*     */                       
/* 186 */                       logger.log(Level.WARNING, exe.getMessage(), exe);
/*     */                     } 
/*     */                   }
/*     */                 }
/* 190 */                 if (affected > power / 10.0D + performer.getNumLinks())
/*     */                   break; 
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Tornado.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */