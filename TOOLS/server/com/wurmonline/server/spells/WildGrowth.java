/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.behaviours.Crops;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.structures.Fence;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.StructureConstantsEnum;
/*     */ import java.io.IOException;
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
/*     */ public class WildGrowth
/*     */   extends ReligiousSpell
/*     */ {
/*  42 */   private static final Logger logger = Logger.getLogger(WildGrowth.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 40;
/*     */ 
/*     */   
/*     */   public WildGrowth() {
/*  49 */     super("Wild Growth", 436, 30, 40, 40, 41, 0L);
/*  50 */     this.targetTile = true;
/*  51 */     this.description = "fields and trees are nurtured";
/*  52 */     this.type = 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, int tilex, int tiley, int layer) {
/*  58 */     if (performer.getLayer() < 0) {
/*     */       
/*  60 */       performer.getCommunicator().sendNormalServerMessage("This spell does not work below ground.", (byte)3);
/*     */       
/*  62 */       return false;
/*     */     } 
/*  64 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/*  71 */     performer.getCommunicator().sendNormalServerMessage("An invigorating energy flows through you into the ground and reaches the roots of plants and trees.");
/*     */ 
/*     */     
/*  74 */     int sx = Zones.safeTileX(tilex - (int)Math.max(1.0D, power / 20.0D) - performer.getNumLinks());
/*  75 */     int sy = Zones.safeTileY(tiley - (int)Math.max(1.0D, power / 20.0D) - performer.getNumLinks());
/*  76 */     int ex = Zones.safeTileX(tilex + (int)Math.max(1.0D, power / 20.0D) + performer.getNumLinks());
/*  77 */     int ey = Zones.safeTileY(tiley + (int)Math.max(1.0D, power / 20.0D) + performer.getNumLinks());
/*  78 */     boolean sentMessage = false;
/*  79 */     for (int x = sx; x <= ex; x++) {
/*     */       
/*  81 */       for (int y = sy; y <= ey; y++) {
/*     */         
/*  83 */         int tile = Server.surfaceMesh.getTile(x, y);
/*  84 */         byte type = Tiles.decodeType(tile);
/*  85 */         Tiles.Tile theTile = Tiles.getTile(type);
/*  86 */         if (performer.isOnSurface()) {
/*     */           
/*  88 */           VolaTile t = Zones.getTileOrNull(x, y, true);
/*  89 */           if (t != null)
/*     */           {
/*  91 */             if (t.getVillage() == null || t
/*  92 */               .getVillage().isActionAllowed((short)468, performer, false, 0, 0)) {
/*     */               
/*  94 */               for (Fence fence : t.getFences()) {
/*  95 */                 if (fence.isHedge())
/*     */                 {
/*  97 */                   if (!fence.isHighHedge() && fence.getType() != StructureConstantsEnum.HEDGE_FLOWER1_LOW && fence
/*  98 */                     .getType() != StructureConstantsEnum.HEDGE_FLOWER3_MEDIUM)
/*     */                   {
/* 100 */                     fence.setDamage(0.0F);
/* 101 */                     fence.setType(StructureConstantsEnum.getEnumByValue((short)((fence.getType()).value + 1)));
/*     */                     
/*     */                     try {
/* 104 */                       fence.save();
/* 105 */                       t.updateFence(fence);
/*     */                     }
/* 107 */                     catch (IOException iox) {
/*     */                       
/* 109 */                       logger.log(Level.WARNING, x + "," + y + " " + iox.getMessage(), iox);
/*     */                     }
/*     */                   
/*     */                   }
/*     */                 
/*     */                 }
/*     */               } 
/* 116 */             } else if (!sentMessage) {
/*     */               
/* 118 */               performer.getCommunicator().sendNormalServerMessage("You are not allowed to affect the hedges in " + t
/* 119 */                   .getVillage().getName() + ".", (byte)3);
/*     */               
/* 121 */               sentMessage = true;
/*     */             } 
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 127 */         if (type == Tiles.Tile.TILE_FIELD.id || type == Tiles.Tile.TILE_FIELD2.id) {
/*     */           
/* 129 */           int worldResource = Server.getWorldResource(x, y);
/* 130 */           int farmedCount = worldResource >>> 11;
/* 131 */           int farmedChance = worldResource & 0x7FF;
/* 132 */           farmedChance = (int)Math.min(farmedChance + power * 2.0D + 75.0D, 2047.0D);
/* 133 */           Server.setWorldResource(x, y, (farmedCount << 11) + farmedChance);
/*     */           
/* 135 */           byte data = Tiles.decodeData(tile);
/* 136 */           int tileAge = Crops.decodeFieldAge(data);
/* 137 */           int crop = Crops.getCropNumber(type, data);
/* 138 */           if (tileAge < 7)
/*     */           {
/* 140 */             Server.setSurfaceTile(x, y, Tiles.decodeHeight(tile), type, 
/* 141 */                 Crops.encodeFieldData(true, tileAge, crop));
/* 142 */             Players.getInstance().sendChangedTile(x, y, true, false);
/*     */           }
/*     */         
/* 145 */         } else if (theTile.isNormalTree() || theTile.isNormalBush()) {
/*     */           
/* 147 */           int age = Tiles.decodeData(tile) >> 4 & 0xF;
/* 148 */           int halfdata = Tiles.decodeData(tile) & 0xF;
/*     */           
/* 150 */           Server.setWorldResource(x, y, 0);
/* 151 */           if (age < 15) {
/*     */             
/* 153 */             int newData = (age + 1 << 4) + halfdata & 0xFF;
/* 154 */             Server.setSurfaceTile(x, y, Tiles.decodeHeight(tile), type, (byte)newData);
/*     */           } else {
/*     */             
/* 157 */             Server.setSurfaceTile(x, y, Tiles.decodeHeight(tile), Tiles.Tile.TILE_GRASS.id, (byte)0);
/* 158 */           }  Players.getInstance().sendChangedTile(x, y, true, false);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\WildGrowth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */