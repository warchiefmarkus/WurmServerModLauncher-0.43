/*     */ package com.wurmonline.server.sounds;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
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
/*     */ public final class SoundPlayer
/*     */ {
/*  34 */   private static final Logger logger = Logger.getLogger(SoundPlayer.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void playSound(String soundName, Creature creature, float height) {
/*  45 */     if (soundName.length() > 0) {
/*     */       
/*     */       try {
/*  48 */         VolaTile vtile = Zones.getOrCreateTile(creature.getTileX(), creature.getTileY(), creature.isOnSurface());
/*     */         
/*  50 */         float offsetx = 4.0F * Server.rand.nextFloat();
/*     */         
/*  52 */         float offsety = 4.0F * Server.rand.nextFloat();
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
/*  66 */         Sound so = new Sound(soundName, creature.getPosX() - 2.0F + offsetx, creature.getPosY() - 2.0F + offsety, Zones.calculateHeight(creature.getPosX(), creature.getPosY(), creature
/*  67 */               .isOnSurface()) + height, 1.0F, 1.0F, 5.0F);
/*     */         
/*  69 */         vtile.addSound(so);
/*     */       }
/*  71 */       catch (NoSuchZoneException nsz) {
/*     */         
/*  73 */         logger.log(Level.WARNING, "Can't play sound at " + creature.getPosX() + ", " + creature.getPosY() + " surfaced=" + creature
/*     */             
/*  75 */             .isOnSurface(), (Throwable)nsz);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void playSound(String soundName, Item item, float height) {
/*     */     try {
/*  83 */       VolaTile vtile = Zones.getOrCreateTile((int)item.getPosX() >> 2, (int)item.getPosY() >> 2, item
/*  84 */           .isOnSurface());
/*  85 */       Sound so = new Sound(soundName, item.getPosX(), item.getPosY(), Zones.calculateHeight(item.getPosX(), item
/*  86 */             .getPosY(), item.isOnSurface()) + height, 1.0F, 1.0F, 5.0F);
/*     */       
/*  88 */       vtile.addSound(so);
/*     */     }
/*  90 */     catch (NoSuchZoneException nsz) {
/*     */       
/*  92 */       logger.log(Level.WARNING, "Can't play sound at " + item
/*  93 */           .getPosX() + ", " + item.getPosY() + " surfaced=" + item.isOnSurface(), (Throwable)nsz);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void playSound(String soundName, int tilex, int tiley, boolean surfaced, float height) {
/*     */     try {
/* 102 */       VolaTile vtile = Zones.getOrCreateTile(tilex, tiley, surfaced);
/* 103 */       Sound so = new Sound(soundName, ((tilex << 2) + 2), ((tiley << 2) + 2), Zones.calculateHeight(((tilex << 2) + 2), ((tiley << 2) + 2), surfaced) + height, 1.0F, 1.0F, 5.0F);
/*     */       
/* 105 */       vtile.addSound(so);
/*     */     }
/* 107 */     catch (NoSuchZoneException noSuchZoneException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void playSong(String songName, Creature creature) {
/* 115 */     playSong(songName, creature, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void playSong(String songName, Creature creature, float pitch) {
/*     */     try {
/* 126 */       Sound so = new Sound(songName, creature.getPosX(), creature.getPosY(), Zones.calculateHeight(creature
/* 127 */             .getPosX(), creature.getPosY(), creature.isOnSurface()), 1.0F, pitch, 5.0F);
/*     */       
/* 129 */       creature.getCommunicator().sendMusic(so);
/*     */     }
/* 131 */     catch (NoSuchZoneException nsz) {
/*     */       
/* 133 */       logger.log(Level.WARNING, "Can't play sound at " + creature.getPosX() + ", " + creature.getPosY() + " surfaced=" + creature
/* 134 */           .isOnSurface(), (Throwable)nsz);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\sounds\SoundPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */