/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.zones.Trap;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.io.IOException;
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
/*     */ public class Forecast
/*     */   extends KarmaSpell
/*     */ {
/*     */   public static final int RANGE = 24;
/*     */   
/*     */   public Forecast() {
/*  43 */     super("Forecast", 560, 60, 300, 30, 1, 180000L);
/*  44 */     this.offensive = true;
/*  45 */     this.targetCreature = true;
/*  46 */     this.targetTile = true;
/*  47 */     this.description = "predicts the future for an area by placing deadly traps around it";
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
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/*  59 */     int villid = 0;
/*  60 */     if (performer.getCitizenVillage() != null)
/*  61 */       villid = (performer.getCitizenVillage()).id; 
/*  62 */     int layer = target.getLayer(); int x;
/*  63 */     for (x = Zones.safeTileX(target.getTileX() - 5); x < Zones.safeTileX(target.getTileX() + 5); x++) {
/*     */       
/*  65 */       int i = Zones.safeTileY(target.getTileY() - 5);
/*  66 */       createTrapAt(x, i, layer, villid, performer.getKingdomId());
/*     */     } 
/*  68 */     for (x = Zones.safeTileX(target.getTileX() - 5); x < Zones.safeTileX(target.getTileX() + 5); x++) {
/*     */       
/*  70 */       int i = Zones.safeTileY(target.getTileY() + 5);
/*  71 */       createTrapAt(x, i, layer, villid, performer.getKingdomId());
/*     */     }  int y;
/*  73 */     for (y = Zones.safeTileY(target.getTileY() - 5); y < Zones.safeTileY(target.getTileY() + 5); y++) {
/*     */       
/*  75 */       int i = Zones.safeTileX(target.getTileX() - 5);
/*  76 */       createTrapAt(i, y, layer, villid, performer.getKingdomId());
/*     */     } 
/*  78 */     for (y = Zones.safeTileY(target.getTileY() - 5); y < Zones.safeTileY(target.getTileY() + 5); y++) {
/*     */       
/*  80 */       int i = Zones.safeTileX(target.getTileX() + 5);
/*  81 */       createTrapAt(i, y, layer, villid, performer.getKingdomId());
/*     */     } 
/*  83 */     performer.getCommunicator().sendNormalServerMessage("You predict a grim future for " + target
/*  84 */         .getNameWithGenus() + " by placing deadly traps around " + target
/*  85 */         .getHimHerItString() + ".");
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
/*  97 */     int villid = 0;
/*  98 */     if (performer.getCitizenVillage() != null)
/*  99 */       villid = (performer.getCitizenVillage()).id;  int x;
/* 100 */     for (x = Zones.safeTileX(tilex - 5); x < Zones.safeTileX(tilex + 5); x++) {
/*     */       
/* 102 */       int i = Zones.safeTileY(tiley - 5);
/* 103 */       createTrapAt(x, i, layer, villid, performer.getKingdomId());
/*     */     } 
/* 105 */     for (x = Zones.safeTileX(tilex - 5); x < Zones.safeTileX(tilex + 5); x++) {
/*     */       
/* 107 */       int i = Zones.safeTileY(tiley + 5);
/* 108 */       createTrapAt(x, i, layer, villid, performer.getKingdomId());
/*     */     }  int y;
/* 110 */     for (y = Zones.safeTileY(tiley - 4); y < Zones.safeTileY(tiley + 4); y++) {
/*     */       
/* 112 */       int i = Zones.safeTileX(tilex - 5);
/* 113 */       createTrapAt(i, y, layer, villid, performer.getKingdomId());
/*     */     } 
/* 115 */     for (y = Zones.safeTileY(tiley - 4); y < Zones.safeTileY(tiley + 4); y++) {
/*     */       
/* 117 */       int i = Zones.safeTileX(tilex + 5);
/* 118 */       createTrapAt(i, y, layer, villid, performer.getKingdomId());
/*     */     } 
/*     */     
/* 121 */     performer.getCommunicator().sendNormalServerMessage("You predict a grim future for enemies near the area by placing deadly traps around it.");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final void createTrapAt(int x, int y, int layer, int villid, byte creatorKingdom) {
/* 127 */     boolean ok = true;
/* 128 */     if (layer < 0) {
/*     */       
/* 130 */       int t = Server.caveMesh.getTile(x, y);
/* 131 */       if (Tiles.isSolidCave(Tiles.decodeType(t)))
/* 132 */         ok = false; 
/* 133 */       if (Tiles.decodeHeight(t) < 0) {
/* 134 */         ok = false;
/*     */       }
/*     */     } else {
/*     */       
/* 138 */       int t = Server.surfaceMesh.getTile(x, y);
/* 139 */       if (Tiles.decodeHeight(t) < 0)
/* 140 */         ok = false; 
/*     */     } 
/* 142 */     if (ok)
/*     */       
/*     */       try {
/* 145 */         VolaTile ttile = Zones.getOrCreateTile(x, y, (layer >= 0));
/* 146 */         if (ttile != null) {
/*     */ 
/*     */           
/* 149 */           int fl = ttile.getDropFloorLevel(layer * 30);
/* 150 */           layer = fl / 30;
/* 151 */           ttile.sendAddQuickTileEffect((byte)71, layer * 30);
/*     */         } 
/*     */ 
/*     */         
/* 155 */         Trap t = new Trap((byte)10, (byte)99, creatorKingdom, villid, Trap.createId(x, y, layer), (byte)100, (byte)100, (byte)100);
/* 156 */         t.create();
/*     */       }
/* 158 */       catch (IOException iox) {
/*     */         return;
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Forecast.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */