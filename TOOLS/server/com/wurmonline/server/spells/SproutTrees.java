/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.mesh.GrassData;
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.mesh.TreeData;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zone;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class SproutTrees
/*     */   extends KarmaSpell
/*     */ {
/*  44 */   private static final Logger logger = Logger.getLogger(SproutTrees.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 24;
/*     */ 
/*     */   
/*     */   public SproutTrees() {
/*  51 */     super("Sprout trees", 634, 30, 400, 32, 1, 300000L);
/*  52 */     this.description = "sprouts trees on grass or dirt tiles in the area.";
/*  53 */     this.offensive = false;
/*  54 */     this.targetTile = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, int tilex, int tiley, int layer) {
/*  61 */     if (layer < 0) {
/*     */       
/*  63 */       performer.getCommunicator().sendNormalServerMessage("You need to be on the surface to cast this spell");
/*  64 */       return false;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/*  69 */       Zone zone = Zones.getZone(tilex, tiley, true);
/*  70 */       VolaTile tile = zone.getOrCreateTile(tilex, tiley);
/*     */ 
/*     */       
/*  73 */       if (tile.getVillage() != null)
/*     */       {
/*     */         
/*  76 */         if (performer.getCitizenVillage() != null) {
/*     */           
/*  78 */           if (performer.getCitizenVillage().getId() != tile.getVillage().getId())
/*     */           {
/*  80 */             performer.getCommunicator().sendNormalServerMessage("You may not cast that spell on someone elses deed.");
/*     */             
/*  82 */             return false;
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/*  87 */           performer.getCommunicator().sendNormalServerMessage("You may not cast that spell on someone elses deed.");
/*  88 */           return false;
/*     */         }
/*     */       
/*     */       }
/*  92 */     } catch (NoSuchZoneException nsz) {
/*     */ 
/*     */       
/*  95 */       performer.getCommunicator().sendNormalServerMessage("You fail to focus the spell on that area.");
/*  96 */       return false;
/*     */     } 
/*  98 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/* 105 */     List<VolaTile> tiles = null;
/*     */     
/*     */     try {
/* 108 */       Zone zone = Zones.getZone(tilex, tiley, true);
/* 109 */       tiles = new ArrayList<>();
/*     */ 
/*     */       
/* 112 */       for (int x = tilex - 2; x < tilex + 2; x++) {
/*     */         
/* 114 */         for (int y = tiley - 2; y < tiley + 2; y++) {
/*     */           
/* 116 */           VolaTile tile = zone.getOrCreateTile(x, y);
/* 117 */           if (tile.getVillage() != null)
/*     */           {
/*     */             
/* 120 */             if (performer.getCitizenVillage() != null) {
/*     */               
/* 122 */               if (performer.getCitizenVillage().getId() != tile.getVillage().getId())
/*     */                 continue; 
/*     */             } else {
/*     */               continue;
/*     */             } 
/*     */           }
/* 128 */           tiles.add(tile);
/*     */           continue;
/*     */         } 
/*     */       } 
/* 132 */     } catch (NoSuchZoneException nz) {
/*     */       
/* 134 */       logger.log(Level.WARNING, "Unable to find zone for sprout trees.", (Throwable)nz);
/*     */     } 
/*     */ 
/*     */     
/* 138 */     if (tiles == null || tiles.size() == 0) {
/*     */       
/* 140 */       performer.getCommunicator().sendNormalServerMessage("You fail to focus the spell on that area.");
/*     */       
/*     */       return;
/*     */     } 
/* 144 */     int treeType = Math.min(13, (int)(power / 7.5D));
/*     */     
/* 146 */     for (int i = 0; i < tiles.size(); i++) {
/*     */       
/* 148 */       VolaTile currTile = tiles.get(i);
/*     */       
/* 150 */       if (needsToCheckSurrounding(treeType))
/*     */       {
/*     */         
/* 153 */         if (isTreeNearby(currTile.tilex, currTile.tiley))
/*     */           continue; 
/*     */       }
/* 156 */       int tileId = Server.surfaceMesh.getTile(currTile.tilex, currTile.tiley);
/* 157 */       byte type = Tiles.decodeType(tileId);
/*     */ 
/*     */       
/* 160 */       if (type == 5 || type == 2 || type == 10) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 165 */         byte ttype, age = (byte)(7 + Server.rand.nextInt(8));
/* 166 */         if (type == 10) {
/* 167 */           ttype = TreeData.TreeType.fromInt(type).asMyceliumTree();
/*     */         } else {
/* 169 */           ttype = TreeData.TreeType.fromInt(type).asNormalTree();
/* 170 */         }  byte newData = Tiles.encodeTreeData(age, false, false, GrassData.GrowthTreeStage.SHORT);
/*     */         
/* 172 */         Server.surfaceMesh.setTile(currTile.tilex, currTile.tiley, 
/* 173 */             Tiles.encode(Tiles.decodeHeight(tileId), ttype, newData));
/* 174 */         Players.getInstance().sendChangedTile(currTile.tilex, currTile.tiley, true, false);
/*     */       } 
/*     */       continue;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean needsToCheckSurrounding(int type) {
/* 187 */     return (type == TreeData.TreeType.OAK.getTypeId() || type == TreeData.TreeType.WILLOW.getTypeId());
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
/*     */   private boolean isTreeNearby(int tx, int ty) {
/* 199 */     for (int x = tx - 1; x < tx + 1; x++) {
/*     */       
/* 201 */       for (int y = ty - 1; y < ty + 1; y++) {
/*     */         
/* 203 */         if (x != tx || y != ty)
/*     */           
/*     */           try {
/*     */             
/* 207 */             int tileId = Server.surfaceMesh.getTile(x, y);
/* 208 */             byte type = Tiles.decodeType(tileId);
/* 209 */             Tiles.Tile tile = Tiles.getTile(type);
/*     */             
/* 211 */             if (tile.isTree()) {
/* 212 */               return true;
/*     */             }
/* 214 */           } catch (Exception ex) {
/*     */             
/* 216 */             logger.log(Level.FINEST, ex.getMessage(), ex);
/*     */           }  
/*     */       } 
/*     */     } 
/* 220 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\SproutTrees.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */