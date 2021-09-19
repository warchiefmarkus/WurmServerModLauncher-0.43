/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.behaviours.Terraforming;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.highways.HighwayPos;
/*     */ import com.wurmonline.server.highways.MethodsHighways;
/*     */ import com.wurmonline.server.skills.Skill;
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
/*     */ public class StrongWall
/*     */   extends ReligiousSpell
/*     */ {
/*     */   public static final int RANGE = 40;
/*     */   
/*     */   public StrongWall() {
/*  41 */     super("Strongwall", 440, 180, 70, 70, 70, 0L);
/*  42 */     this.targetTile = true;
/*  43 */     this.description = "causes an open tile underground to collapse, or reinforces an existing wall";
/*  44 */     this.type = 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, int tilex, int tiley, int layer) {
/*  50 */     int tile = Server.caveMesh.getTile(tilex, tiley);
/*  51 */     if (layer < 0 || Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE_EXIT.id) {
/*     */       
/*  53 */       if (Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE_EXIT.id || Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE.id || 
/*  54 */         Tiles.isReinforcedFloor(Tiles.decodeType(tile))) {
/*     */ 
/*     */         
/*  57 */         HighwayPos highwayPos = MethodsHighways.getHighwayPos(tilex, tiley, false);
/*  58 */         if (highwayPos != null && MethodsHighways.onHighway(highwayPos)) {
/*  59 */           return false;
/*     */         }
/*  61 */         VolaTile t = Zones.getOrCreateTile(tilex, tiley, false);
/*  62 */         if ((t.getCreatures()).length > 0) {
/*     */           
/*  64 */           performer.getCommunicator().sendNormalServerMessage("That tile is occupied by creatures.", (byte)3);
/*     */           
/*  66 */           return false;
/*     */         } 
/*  68 */         if (t.getStructure() != null) {
/*     */           
/*  70 */           performer.getCommunicator().sendNormalServerMessage("The structure gets in the way.", (byte)3);
/*     */           
/*  72 */           return false;
/*     */         } 
/*  74 */         if ((t.getItems()).length > 0) {
/*     */           
/*  76 */           performer.getCommunicator().sendNormalServerMessage("You should remove the items first.", (byte)3);
/*     */           
/*  78 */           return false;
/*     */         } 
/*  80 */         if (t.getVillage() == null)
/*     */         {
/*     */           
/*  83 */           for (int x = -1; x <= 1; x++) {
/*     */             
/*  85 */             for (int y = -1; y <= 1; y++) {
/*     */               
/*  87 */               if (x != 0 || y != 0) {
/*     */                 
/*  89 */                 VolaTile vt = Zones.getTileOrNull(tilex + x, tiley + y, false);
/*  90 */                 if (vt != null && vt.getStructure() != null) {
/*     */                   
/*  92 */                   performer.getCommunicator().sendNormalServerMessage("The nearby structure gets in the way.", (byte)3);
/*     */                   
/*  94 */                   return false;
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         }
/* 100 */         int ts = Server.surfaceMesh.getTile(tilex, tiley);
/* 101 */         byte type = Tiles.decodeType(ts);
/* 102 */         if (Tiles.isMineDoor(type)) {
/*     */           
/* 104 */           performer.getCommunicator().sendNormalServerMessage("You need to destroy the mine door first.", (byte)3);
/*     */           
/* 106 */           return false;
/*     */         } 
/*     */       } else {
/* 109 */         if (Tiles.isOreCave(Tiles.decodeType(tile))) {
/*     */           
/* 111 */           performer.getCommunicator().sendNormalServerMessage("Nothing happens on the ore.", (byte)3);
/*     */           
/* 113 */           return false;
/*     */         } 
/* 115 */         if (Tiles.isReinforcedCave(Tiles.decodeType(tile))) {
/*     */           
/* 117 */           performer.getCommunicator().sendNormalServerMessage("Nothing happens on the reinforced rock.", (byte)3);
/*     */           
/* 119 */           return false;
/*     */         } 
/* 121 */       }  return true;
/*     */     } 
/* 123 */     performer.getCommunicator().sendNormalServerMessage("This spell works on rock below ground.", (byte)3);
/* 124 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/* 131 */     int tile = Server.caveMesh.getTile(tilex, tiley);
/* 132 */     byte type = Tiles.decodeType(tile);
/* 133 */     if (Tiles.isSolidCave(type)) {
/*     */       
/* 135 */       if (type == Tiles.Tile.TILE_CAVE_WALL.id) {
/*     */         
/* 137 */         Server.caveMesh.setTile(tilex, tiley, 
/* 138 */             Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id, Tiles.decodeData(tile)));
/* 139 */         Players.getInstance().sendChangedTile(tilex, tiley, false, true);
/* 140 */         performer.getCommunicator().sendNormalServerMessage("You reinforce the rock.", (byte)2);
/*     */       } else {
/*     */         
/* 143 */         performer.getCommunicator().sendNormalServerMessage("Nothing happens to the " + 
/* 144 */             (Tiles.getTile(type)).tiledesc.toLowerCase() + ".", (byte)3);
/*     */       } 
/*     */       return;
/*     */     } 
/* 148 */     if (Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE_EXIT.id || Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE.id || 
/* 149 */       Tiles.isReinforcedFloor(Tiles.decodeType(tile))) {
/*     */ 
/*     */       
/* 152 */       HighwayPos highwayPos = MethodsHighways.getHighwayPos(tilex, tiley, false);
/* 153 */       if (highwayPos != null && MethodsHighways.onHighway(highwayPos)) {
/*     */         
/* 155 */         performer.getCommunicator().sendNormalServerMessage("That highway gets in the way.", (byte)3);
/*     */         
/*     */         return;
/*     */       } 
/* 159 */       VolaTile t = Zones.getOrCreateTile(tilex, tiley, false);
/* 160 */       if (t.getStructure() != null) {
/*     */         
/* 162 */         performer.getCommunicator().sendNormalServerMessage("The structure gets in the way.", (byte)3);
/*     */         
/*     */         return;
/*     */       } 
/* 166 */       if ((t.getCreatures()).length > 0) {
/*     */         
/* 168 */         performer.getCommunicator().sendNormalServerMessage("That tile is occupied by creatures.", (byte)3);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 173 */       if ((t.getItems()).length > 0) {
/*     */         
/* 175 */         performer.getCommunicator().sendNormalServerMessage("You should remove the items first.", (byte)3);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 180 */       if (t.getVillage() == null)
/*     */       {
/*     */         
/* 183 */         for (int x = -1; x <= 1; x++) {
/*     */           
/* 185 */           for (int y = -1; y <= 1; y++) {
/*     */             
/* 187 */             if (x != 0 || y != 0) {
/*     */               
/* 189 */               VolaTile vt = Zones.getTileOrNull(tilex + x, tiley + y, false);
/* 190 */               if (vt != null && vt.getStructure() != null) {
/*     */                 
/* 192 */                 performer.getCommunicator().sendNormalServerMessage("The nearby structure gets in the way.", (byte)3);
/*     */                 
/*     */                 return;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/* 200 */       int ts = Server.surfaceMesh.getTile(tilex, tiley);
/* 201 */       byte stype = Tiles.decodeType(ts);
/* 202 */       if (Tiles.isMineDoor(stype)) {
/*     */         
/* 204 */         performer.getCommunicator().sendNormalServerMessage("You need to destroy the mine door first.", (byte)3);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 209 */     Terraforming.setAsRock(tilex, tiley, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\StrongWall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */