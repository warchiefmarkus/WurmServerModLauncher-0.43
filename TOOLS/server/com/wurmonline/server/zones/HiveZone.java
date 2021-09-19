/*     */ package com.wurmonline.server.zones;
/*     */ 
/*     */ import com.wurmonline.server.items.Item;
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
/*     */ 
/*     */ 
/*     */ public final class HiveZone
/*     */ {
/*  32 */   private static final Logger logger = Logger.getLogger(HiveZone.class.getName());
/*     */   
/*     */   private Item hive;
/*     */   
/*     */   private final int areaRadius;
/*     */   
/*     */   public HiveZone(Item hive) {
/*  39 */     this.hive = hive;
/*  40 */     this.areaRadius = 1 + (int)Math.sqrt(hive.getCurrentQualityLevel());
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
/*     */   public int getStrengthForTile(int tileX, int tileY, boolean surfaced) {
/*  52 */     return this.areaRadius - 
/*  53 */       Math.max(Math.abs(this.hive.getTileX() - tileX), Math.abs(this.hive.getTileY() - tileY));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStartX() {
/*  62 */     return this.hive.getTileX() - this.areaRadius;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStartY() {
/*  71 */     return this.hive.getTileY() - this.areaRadius;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEndX() {
/*  80 */     return this.hive.getTileX() + this.areaRadius;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEndY() {
/*  89 */     return this.hive.getTileY() + this.areaRadius;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsTile(int tileX, int tileY) {
/* 100 */     return (tileX > getStartX() && tileX < getEndX() && tileY > 
/* 101 */       getStartY() && tileY < getEndY());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCloseToTile(int tileX, int tileY) {
/* 112 */     return (getDistanceFrom(tileX, tileY) < 10 + this.areaRadius);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Item getCurrentHive() {
/* 122 */     return this.hive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasHive(int tilex, int tiley) {
/* 133 */     return (this.hive.getTileX() == tilex && this.hive.getTileY() == tiley);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDistanceFrom(int tilex, int tiley) {
/* 144 */     return Math.max(Math.abs(this.hive.getTileX() - tilex), Math.abs(this.hive.getTileY() - tiley));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClose(int tilex, int tiley) {
/* 155 */     return (getDistanceFrom(tilex, tiley) < 2);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\HiveZone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */