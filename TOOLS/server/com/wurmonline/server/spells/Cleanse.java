/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
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
/*     */ public class Cleanse
/*     */   extends ReligiousSpell
/*     */ {
/*     */   public static final int RANGE = 4;
/*     */   
/*     */   public Cleanse() {
/*  40 */     super("Cleanse", 930, 30, 26, 30, 33, 0L);
/*  41 */     this.targetTile = true;
/*  42 */     this.description = "cleanses a small area of mycelium infected land";
/*  43 */     this.type = 1;
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
/*  54 */     if (performer.getLayer() < 0) {
/*     */       
/*  56 */       performer.getCommunicator().sendNormalServerMessage("This spell does not work below ground.", (byte)3);
/*  57 */       return false;
/*     */     } 
/*  59 */     if (Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex, tiley)) < 0) {
/*     */       
/*  61 */       performer.getCommunicator().sendNormalServerMessage("This spell does not work below water.", (byte)3);
/*  62 */       return false;
/*     */     } 
/*  64 */     return true;
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
/*  76 */     performer.getCommunicator().sendNormalServerMessage("An invigorating energy flows through you into the ground and reaches the roots of plants and trees.");
/*     */ 
/*     */     
/*  79 */     int sx = Zones.safeTileX(tilex - 1 - performer.getNumLinks());
/*  80 */     int sy = Zones.safeTileY(tiley - 1 - performer.getNumLinks());
/*  81 */     int ex = Zones.safeTileX(tilex + 1 + performer.getNumLinks());
/*  82 */     int ey = Zones.safeTileY(tiley + 1 + performer.getNumLinks());
/*  83 */     boolean blocked = false;
/*  84 */     for (int x = sx; x <= ex; x++) {
/*     */       
/*  86 */       for (int y = sy; y <= ey; y++) {
/*     */ 
/*     */         
/*     */         try {
/*  90 */           FaithZone fz = Zones.getFaithZone(x, y, true);
/*     */           
/*  92 */           boolean ok = false;
/*  93 */           if (fz != null) {
/*     */             
/*  95 */             if (fz.getCurrentRuler() == null || fz.getCurrentRuler() == performer.getDeity() || !fz.getCurrentRuler().isHateGod()) {
/*  96 */               ok = true;
/*     */             }
/*     */           } else {
/*  99 */             ok = true;
/*     */           } 
/* 101 */           if (ok) {
/*     */             
/* 103 */             VolaTile t = Zones.getOrCreateTile(x, y, true);
/* 104 */             if (t == null || t.getVillage() == null || (t.getVillage()).kingdom == performer.getKingdomId()) {
/*     */               
/* 106 */               int tile = Server.surfaceMesh.getTile(x, y);
/* 107 */               byte type = Tiles.decodeType(tile);
/* 108 */               Tiles.Tile theTile = Tiles.getTile(type);
/* 109 */               byte data = Tiles.decodeData(tile);
/*     */               
/* 111 */               if (type == Tiles.Tile.TILE_DIRT.id || type == Tiles.Tile.TILE_MYCELIUM_LAWN.id || type == Tiles.Tile.TILE_MYCELIUM.id || theTile
/*     */ 
/*     */                 
/* 114 */                 .isMyceliumTree() || theTile
/* 115 */                 .isMyceliumBush())
/*     */               {
/* 117 */                 if (theTile.isMyceliumTree()) {
/*     */                   
/* 119 */                   Server.setSurfaceTile(x, y, 
/* 120 */                       Tiles.decodeHeight(tile), theTile.getTreeType(data).asNormalTree(), data);
/*     */                 }
/* 122 */                 else if (theTile.isMyceliumBush()) {
/*     */                   
/* 124 */                   Server.setSurfaceTile(x, y, 
/* 125 */                       Tiles.decodeHeight(tile), theTile.getBushType(data).asNormalBush(), data);
/*     */                 }
/* 127 */                 else if (type == Tiles.Tile.TILE_MYCELIUM_LAWN.id) {
/*     */                   
/* 129 */                   Server.setSurfaceTile(x, y, 
/* 130 */                       Tiles.decodeHeight(tile), Tiles.Tile.TILE_LAWN.id, (byte)0);
/*     */                 }
/*     */                 else {
/*     */                   
/* 134 */                   Server.setSurfaceTile(x, y, 
/* 135 */                       Tiles.decodeHeight(tile), Tiles.Tile.TILE_GRASS.id, (byte)0);
/*     */                 } 
/* 137 */                 Players.getInstance().sendChangedTile(x, y, true, false);
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/* 142 */               blocked = true;
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/* 147 */             blocked = true;
/*     */           }
/*     */         
/* 150 */         } catch (NoSuchZoneException noSuchZoneException) {}
/*     */       } 
/*     */     } 
/* 153 */     if (blocked)
/* 154 */       performer.getCommunicator()
/* 155 */         .sendNormalServerMessage("The domain of another deity or settlement protects this area.", (byte)3); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Cleanse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */