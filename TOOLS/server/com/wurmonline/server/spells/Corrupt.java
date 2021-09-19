/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.zones.FaithZone;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
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
/*     */ public class Corrupt
/*     */   extends ReligiousSpell
/*     */ {
/*     */   public static final int RANGE = 4;
/*     */   
/*     */   public Corrupt() {
/*  41 */     super("Corrupt", 446, 30, 26, 30, 33, 0L);
/*  42 */     this.targetTile = true;
/*  43 */     this.description = "corrupts a small area of land with mycelium";
/*  44 */     this.type = 1;
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
/*  55 */     if (!Servers.localServer.PVPSERVER) {
/*     */       
/*  57 */       performer.getCommunicator().sendNormalServerMessage("This spell does not work here.", (byte)3);
/*  58 */       return false;
/*     */     } 
/*  60 */     if (performer.getLayer() < 0) {
/*     */       
/*  62 */       performer.getCommunicator().sendNormalServerMessage("This spell does not work below ground.", (byte)3);
/*  63 */       return false;
/*     */     } 
/*  65 */     if (Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex, tiley)) < 0) {
/*     */       
/*  67 */       performer.getCommunicator().sendNormalServerMessage("This spell does not work below water.", (byte)3);
/*  68 */       return false;
/*     */     } 
/*  70 */     return true;
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
/*  82 */     performer.getCommunicator().sendNormalServerMessage("An invigorating energy flows through you into the ground and reaches the roots of plants and trees.");
/*     */ 
/*     */     
/*  85 */     int sx = Zones.safeTileX(tilex - 1 - performer.getNumLinks());
/*  86 */     int sy = Zones.safeTileY(tiley - 1 - performer.getNumLinks());
/*  87 */     int ex = Zones.safeTileX(tilex + 1 + performer.getNumLinks());
/*  88 */     int ey = Zones.safeTileY(tiley + 1 + performer.getNumLinks());
/*  89 */     boolean blocked = false;
/*  90 */     for (int x = sx; x <= ex; x++) {
/*     */       
/*  92 */       for (int y = sy; y <= ey; y++) {
/*     */ 
/*     */         
/*     */         try {
/*  96 */           FaithZone fz = Zones.getFaithZone(x, y, true);
/*     */           
/*  98 */           boolean ok = false;
/*  99 */           if (fz != null) {
/*     */             
/* 101 */             if (fz.getCurrentRuler() == null || fz.getCurrentRuler() == performer.getDeity() || fz.getCurrentRuler().isHateGod()) {
/* 102 */               ok = true;
/*     */             }
/*     */           } else {
/* 105 */             ok = true;
/*     */           } 
/* 107 */           if (ok) {
/*     */             
/* 109 */             VolaTile t = Zones.getOrCreateTile(x, y, true);
/* 110 */             if (t == null || t.getVillage() == null || (t.getVillage()).kingdom == performer.getKingdomId()) {
/*     */               
/* 112 */               int tile = Server.surfaceMesh.getTile(x, y);
/* 113 */               byte type = Tiles.decodeType(tile);
/* 114 */               Tiles.Tile theTile = Tiles.getTile(type);
/* 115 */               byte data = Tiles.decodeData(tile);
/*     */               
/* 117 */               if (type == Tiles.Tile.TILE_FIELD.id || type == Tiles.Tile.TILE_FIELD2.id || type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_REED.id || type == Tiles.Tile.TILE_DIRT.id || type == Tiles.Tile.TILE_LAWN.id || type == Tiles.Tile.TILE_STEPPE.id || theTile
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 124 */                 .isNormalTree() || theTile
/* 125 */                 .isEnchanted() || theTile
/* 126 */                 .isNormalBush())
/*     */               {
/* 128 */                 if (theTile.isNormalTree()) {
/*     */                   
/* 130 */                   Server.setSurfaceTile(x, y, Tiles.decodeHeight(tile), theTile.getTreeType(data).asMyceliumTree(), data);
/*     */                 }
/* 132 */                 else if (theTile.isEnchantedTree()) {
/*     */                   
/* 134 */                   Server.setSurfaceTile(x, y, 
/* 135 */                       Tiles.decodeHeight(tile), theTile.getTreeType(data).asNormalTree(), data);
/*     */                 }
/* 137 */                 else if (theTile.isNormalBush()) {
/*     */                   
/* 139 */                   Server.setSurfaceTile(x, y, 
/* 140 */                       Tiles.decodeHeight(tile), theTile.getBushType(data).asMyceliumBush(), data);
/*     */                 }
/* 142 */                 else if (theTile.isEnchantedBush()) {
/*     */                   
/* 144 */                   Server.setSurfaceTile(x, y, 
/* 145 */                       Tiles.decodeHeight(tile), theTile.getBushType(data).asNormalBush(), data);
/*     */                 }
/* 147 */                 else if (type == Tiles.Tile.TILE_LAWN.id) {
/*     */                   
/* 149 */                   Server.setSurfaceTile(x, y, 
/* 150 */                       Tiles.decodeHeight(tile), Tiles.Tile.TILE_MYCELIUM_LAWN.id, (byte)0);
/*     */                 }
/*     */                 else {
/*     */                   
/* 154 */                   Server.setSurfaceTile(x, y, 
/* 155 */                       Tiles.decodeHeight(tile), Tiles.Tile.TILE_MYCELIUM.id, (byte)0);
/*     */                 } 
/* 157 */                 Players.getInstance().sendChangedTile(x, y, true, false);
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/* 162 */               blocked = true;
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/* 167 */             blocked = true;
/*     */           }
/*     */         
/* 170 */         } catch (NoSuchZoneException noSuchZoneException) {}
/*     */       } 
/*     */     } 
/* 173 */     if (blocked)
/* 174 */       performer.getCommunicator()
/* 175 */         .sendNormalServerMessage("The domain of another deity or settlement protects this area.", (byte)3); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Corrupt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */