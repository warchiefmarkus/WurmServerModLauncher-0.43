/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.sounds.SoundPlayer;
/*     */ import com.wurmonline.server.structures.DbFence;
/*     */ import com.wurmonline.server.structures.Fence;
/*     */ import com.wurmonline.server.structures.Wall;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zone;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.StructureConstantsEnum;
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
/*     */ 
/*     */ 
/*     */ public class WallOfIce
/*     */   extends KarmaSpell
/*     */ {
/*     */   public static final int RANGE = 24;
/*     */   
/*     */   public WallOfIce() {
/*  51 */     super("Wall of Ice", 556, 10, 400, 10, 1, 0L);
/*  52 */     this.targetTileBorder = true;
/*  53 */     this.offensive = true;
/*  54 */     this.description = "creates a magical wall of ice on a tile border";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, int tileBorderx, int tileBordery, int layer, int heightOffset, Tiles.TileBorderDirection dir) {
/*  62 */     VolaTile t = Zones.getTileOrNull(tileBorderx, tileBordery, (layer == 0));
/*  63 */     if (t != null) {
/*     */       
/*  65 */       Wall[] walls = t.getWallsForLevel(heightOffset / 30);
/*  66 */       for (Wall wall : walls) {
/*     */         
/*  68 */         if (wall.isHorizontal() == ((dir == Tiles.TileBorderDirection.DIR_HORIZ)))
/*     */         {
/*  70 */           if (wall.getStartX() == tileBorderx && wall.getStartY() == tileBordery)
/*  71 */             return false; 
/*     */         }
/*     */       } 
/*  74 */       Fence[] fences = t.getFencesForDir(dir);
/*  75 */       for (Fence f : fences) {
/*     */         
/*  77 */         if (f.getHeightOffset() == heightOffset)
/*     */         {
/*  79 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*  83 */     if (dir == Tiles.TileBorderDirection.DIR_DOWN) {
/*     */       
/*  85 */       VolaTile t1 = Zones.getTileOrNull(tileBorderx, tileBordery, (layer == 0));
/*  86 */       if (t1 != null)
/*  87 */         for (Creature c : t1.getCreatures()) {
/*     */           
/*  89 */           if (c.isPlayer())
/*  90 */             return false; 
/*     */         }  
/*  92 */       VolaTile t2 = Zones.getTileOrNull(tileBorderx - 1, tileBordery, (layer == 0));
/*  93 */       if (t2 != null) {
/*  94 */         for (Creature c : t2.getCreatures()) {
/*     */           
/*  96 */           if (c.isPlayer()) {
/*  97 */             return false;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } else {
/* 102 */       VolaTile t1 = Zones.getTileOrNull(tileBorderx, tileBordery, (layer == 0));
/* 103 */       if (t1 != null)
/* 104 */         for (Creature c : t1.getCreatures()) {
/*     */           
/* 106 */           if (c.isPlayer())
/* 107 */             return false; 
/*     */         }  
/* 109 */       VolaTile t2 = Zones.getTileOrNull(tileBorderx, tileBordery - 1, (layer == 0));
/* 110 */       if (t2 != null)
/* 111 */         for (Creature c : t2.getCreatures()) {
/*     */           
/* 113 */           if (c.isPlayer())
/* 114 */             return false; 
/*     */         }  
/*     */     } 
/* 117 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset, Tiles.TileBorderDirection dir) {
/* 124 */     SoundPlayer.playSound("sound.religion.channel", tilex, tiley, performer.isOnSurface(), 0.0F);
/*     */     
/*     */     try {
/* 127 */       Zone zone = Zones.getZone(tilex, tiley, true);
/*     */       
/* 129 */       DbFence dbFence = new DbFence(StructureConstantsEnum.FENCE_MAGIC_ICE, tilex, tiley, heightOffset, (float)(1.0D + power / 5.0D), dir, zone.getId(), layer);
/* 130 */       dbFence.setState(dbFence.getFinishState());
/* 131 */       dbFence.setQualityLevel((float)power);
/* 132 */       dbFence.improveOrigQualityLevel((float)power);
/* 133 */       zone.addFence((Fence)dbFence);
/* 134 */       performer.achievement(320);
/* 135 */       performer.getCommunicator().sendNormalServerMessage("You weave the source and create a wall.");
/* 136 */       Server.getInstance().broadCastAction(performer.getName() + " creates a wall.", performer, 5);
/*     */     }
/* 138 */     catch (NoSuchZoneException noSuchZoneException) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\WallOfIce.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */