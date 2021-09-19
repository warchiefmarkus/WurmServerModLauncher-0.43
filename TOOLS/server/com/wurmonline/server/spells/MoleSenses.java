/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.behaviours.ShardBehaviour;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.zones.LongPosition;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
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
/*     */ public class MoleSenses
/*     */   extends ReligiousSpell
/*     */ {
/*  39 */   private static Logger logger = Logger.getLogger(MoleSenses.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 4;
/*     */ 
/*     */   
/*     */   public MoleSenses() {
/*  46 */     super("Mole Senses", 439, 30, 60, 40, 65, 0L);
/*  47 */     this.targetTile = true;
/*  48 */     this.description = "smell ores and rock depth";
/*  49 */     this.type = 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, int tilex, int tiley, int layer) {
/*  59 */     if (performer.getLayer() < 0) {
/*     */       
/*  61 */       performer.getCommunicator().sendNormalServerMessage("This spell does not work below ground. Your senses would become overwhelmed.", (byte)3);
/*     */       
/*  63 */       return false;
/*     */     } 
/*  65 */     return true;
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
/*     */ 
/*     */   
/*     */   private void sendOres(Creature performer, double power, int tilex, int tiley) {
/*  79 */     int maxRadius = Math.max(2, 1 + ShardBehaviour.calcMaxRadius(power) / 2);
/*     */ 
/*     */ 
/*     */     
/*  83 */     Set<String> ores = new HashSet<>();
/*  84 */     for (int dx = maxRadius * -1; dx < maxRadius; dx++) {
/*  85 */       for (int dy = maxRadius * -1; dy < maxRadius; dy++) {
/*  86 */         int type = Tiles.decodeType(Server.caveMesh.getTile(tilex + dx, tiley + dy)) & 0xFF;
/*     */         
/*  88 */         if (type == 205 && power > 40.0D) {
/*  89 */           ores.add("slate");
/*  90 */         } else if (type == 206 && power > 40.0D) {
/*  91 */           ores.add("marble");
/*  92 */         } else if (type == 220 && power > 60.0D) {
/*  93 */           ores.add("gold");
/*  94 */         } else if (type == 221 && power > 50.0D) {
/*  95 */           ores.add("silver");
/*  96 */         } else if (type == 227 && power > 70.0D) {
/*  97 */           ores.add("adamantine");
/*  98 */         } else if (type == 228 && power > 80.0D) {
/*  99 */           ores.add("glimmersteel");
/* 100 */         } else if (type == 222) {
/* 101 */           ores.add("iron");
/* 102 */         } else if (type == 223) {
/* 103 */           ores.add("copper");
/* 104 */         } else if (type == 224) {
/* 105 */           ores.add("lead");
/* 106 */         } else if (type == 225) {
/* 107 */           ores.add("zinc");
/* 108 */         } else if (type == 226) {
/* 109 */           ores.add("tin");
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 115 */     Iterator<String> it = ores.iterator();
/* 116 */     if (ores.size() == 1) {
/* 117 */       performer.getCommunicator().sendNormalServerMessage("You smell traces of " + (String)it.next() + " in the dirt.");
/* 118 */     } else if (ores.size() > 1) {
/* 119 */       String s = "You smell traces of ";
/* 120 */       for (int i = 0; i < ores.size() - 1; i++) {
/* 121 */         if (i == ores.size() - 2) {
/* 122 */           s = s + (String)it.next();
/*     */         } else {
/* 124 */           s = s + (String)it.next() + ", ";
/*     */         } 
/*     */       } 
/* 127 */       s = s + " and " + (String)it.next() + " in the dirt.";
/*     */       
/* 129 */       performer.getCommunicator().sendNormalServerMessage(s);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/* 141 */     performer.getCommunicator().sendNormalServerMessage("For a short while you go almost blind, but you smell the earth with incredible accuracy.");
/*     */ 
/*     */     
/*     */     try {
/* 145 */       float rockheight = Zones.calculateRockHeight(performer.getPosX(), performer.getPosY());
/* 146 */       float surfheight = Zones.calculateHeight(performer.getPosX(), performer.getPosY(), true);
/* 147 */       float diff = surfheight - rockheight;
/* 148 */       if (diff >= 0.0F && diff < 20.0F) {
/*     */         
/* 150 */         if (diff < 1.0F) {
/* 151 */           performer.getCommunicator().sendNormalServerMessage("You smell the rock less than a meter below you.");
/*     */         } else {
/* 153 */           performer.getCommunicator().sendNormalServerMessage("You smell the rock less than " + (int)diff + " meters below you.");
/* 154 */         }  Tiles.Tile tile = Tiles.getTile(Tiles.decodeType(Server.caveMesh.getTile(performer.getTileX(), performer.getTileY())));
/* 155 */         String tname = tile.tiledesc.toLowerCase();
/* 156 */         if (tile.id == Tiles.Tile.TILE_CAVE_WALL.id) {
/* 157 */           tname = "rock";
/*     */         }
/*     */         
/* 160 */         if (diff > 0.0F) {
/* 161 */           sendOres(performer, power, tilex, tiley);
/*     */         }
/*     */       }
/* 164 */       else if (diff > 20.0F) {
/* 165 */         performer.getCommunicator().sendNormalServerMessage("You fail to smell the rock here. It is probably too deep down.", (byte)3);
/*     */       }
/*     */     
/* 168 */     } catch (NoSuchZoneException nsz) {
/*     */       
/* 170 */       performer.getCommunicator().sendNormalServerMessage("You fail to smell the rock here. It is probably too deep down.", (byte)3);
/*     */     } 
/*     */     
/* 173 */     float rot = Creature.normalizeAngle(performer.getStatus().getRotation());
/* 174 */     int numtiles = (int)Math.max(1.0D, power / 10.0D) + performer.getNumLinks() * 2;
/* 175 */     for (int nt = 1; nt <= numtiles; nt++) {
/*     */       
/* 177 */       LongPosition lp = Zones.getEndTile(performer.getPosX(), performer.getPosY(), rot, nt);
/*     */       
/* 179 */       int tile = Server.caveMesh.getTile(lp.getTilex(), lp.getTiley());
/* 180 */       logger.info("Checking tile " + lp.getTilex() + ", " + lp.getTiley() + ", cave is " + (Tiles.getTile(Tiles.decodeType(tile))).tiledesc.toLowerCase());
/*     */       
/* 182 */       Tiles.Tile t = Tiles.getTile(Tiles.decodeType(tile));
/* 183 */       if (Tiles.isOreCave(t.id))
/* 184 */         performer.getCommunicator().sendNormalServerMessage("You sniff " + t.tiledesc
/* 185 */             .toLowerCase() + " " + nt + " tiles away in your facing direction."); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\MoleSenses.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */