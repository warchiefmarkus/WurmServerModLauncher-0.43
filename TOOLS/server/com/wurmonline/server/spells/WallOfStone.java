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
/*     */ public class WallOfStone
/*     */   extends KarmaSpell
/*     */ {
/*     */   public static final int RANGE = 24;
/*     */   
/*     */   public WallOfStone() {
/*  48 */     super("Wall of Stone", 558, 10, 400, 10, 1, 0L);
/*  49 */     this.targetTileBorder = true;
/*  50 */     this.offensive = true;
/*  51 */     this.description = "creates a temporary wall of stone on a tile border";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, int tileBorderx, int tileBordery, int layer, int heightOffset, Tiles.TileBorderDirection dir) {
/*  58 */     VolaTile t = Zones.getTileOrNull(tileBorderx, tileBordery, (layer == 0));
/*  59 */     if (t != null) {
/*     */       
/*  61 */       Wall[] walls = t.getWallsForLevel(heightOffset / 30);
/*  62 */       for (Wall wall : walls) {
/*     */         
/*  64 */         if (wall.isHorizontal() == ((dir == Tiles.TileBorderDirection.DIR_HORIZ)))
/*     */         {
/*  66 */           if (wall.getStartX() == tileBorderx && wall.getStartY() == tileBordery)
/*  67 */             return false; 
/*     */         }
/*     */       } 
/*  70 */       Fence[] fences = t.getFencesForDir(dir);
/*  71 */       for (Fence f : fences) {
/*     */         
/*  73 */         if (f.getHeightOffset() == heightOffset)
/*     */         {
/*  75 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*  79 */     if (dir == Tiles.TileBorderDirection.DIR_DOWN) {
/*     */       
/*  81 */       VolaTile t1 = Zones.getTileOrNull(tileBorderx, tileBordery, (layer == 0));
/*  82 */       if (t1 != null)
/*  83 */         for (Creature c : t1.getCreatures()) {
/*     */           
/*  85 */           if (c.isPlayer())
/*  86 */             return false; 
/*     */         }  
/*  88 */       VolaTile t2 = Zones.getTileOrNull(tileBorderx - 1, tileBordery, (layer == 0));
/*  89 */       if (t2 != null) {
/*  90 */         for (Creature c : t2.getCreatures()) {
/*     */           
/*  92 */           if (c.isPlayer()) {
/*  93 */             return false;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } else {
/*  98 */       VolaTile t1 = Zones.getTileOrNull(tileBorderx, tileBordery, (layer == 0));
/*  99 */       if (t1 != null)
/* 100 */         for (Creature c : t1.getCreatures()) {
/*     */           
/* 102 */           if (c.isPlayer())
/* 103 */             return false; 
/*     */         }  
/* 105 */       VolaTile t2 = Zones.getTileOrNull(tileBorderx, tileBordery - 1, (layer == 0));
/* 106 */       if (t2 != null)
/* 107 */         for (Creature c : t2.getCreatures()) {
/*     */           
/* 109 */           if (c.isPlayer())
/* 110 */             return false; 
/*     */         }  
/*     */     } 
/* 113 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset, Tiles.TileBorderDirection dir) {
/* 120 */     SoundPlayer.playSound("sound.religion.channel", tilex, tiley, performer.isOnSurface(), 0.0F);
/*     */     
/*     */     try {
/* 123 */       Zone zone = Zones.getZone(tilex, tiley, true);
/*     */       
/* 125 */       DbFence dbFence = new DbFence(StructureConstantsEnum.FENCE_MAGIC_STONE, tilex, tiley, heightOffset, (float)(1.0D + power / 5.0D), dir, zone.getId(), layer);
/* 126 */       dbFence.setState(dbFence.getFinishState());
/* 127 */       dbFence.setQualityLevel((float)power);
/* 128 */       dbFence.improveOrigQualityLevel((float)power);
/* 129 */       zone.addFence((Fence)dbFence);
/* 130 */       performer.achievement(320);
/* 131 */       performer.getCommunicator().sendNormalServerMessage("You weave the source and create a wall.");
/* 132 */       Server.getInstance().broadCastAction(performer.getName() + " creates a wall.", performer, 5);
/*     */     }
/* 134 */     catch (NoSuchZoneException noSuchZoneException) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\WallOfStone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */