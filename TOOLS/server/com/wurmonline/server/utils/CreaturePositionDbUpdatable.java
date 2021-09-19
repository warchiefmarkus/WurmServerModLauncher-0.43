/*     */ package com.wurmonline.server.utils;
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
/*     */ public final class CreaturePositionDbUpdatable
/*     */   implements WurmDbUpdatable
/*     */ {
/*     */   static final String SAVE_CREATURE_POSITION = "update POSITION set POSX=?, POSY=?, POSZ=?, ROTATION=?, ZONEID=?, LAYER=?,ONBRIDGE=? where WURMID=?";
/*     */   private final long id;
/*     */   private final float positionX;
/*     */   private final float positionY;
/*     */   private final float positionZ;
/*     */   private final float rotation;
/*     */   private final int zoneid;
/*     */   private final int layer;
/*     */   private final long bridgeId;
/*     */   
/*     */   public CreaturePositionDbUpdatable(long aId, float aPositionX, float aPositionY, float aPositionZ, float aRotation, int aZoneid, int aLayer, long bridgeid) {
/*  64 */     this.id = aId;
/*  65 */     this.positionX = aPositionX;
/*  66 */     this.positionY = aPositionY;
/*  67 */     this.positionZ = aPositionZ;
/*  68 */     this.rotation = aRotation;
/*  69 */     this.zoneid = aZoneid;
/*  70 */     this.layer = aLayer;
/*  71 */     this.bridgeId = bridgeid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDatabaseUpdateStatement() {
/*  82 */     return "update POSITION set POSX=?, POSY=?, POSZ=?, ROTATION=?, ZONEID=?, LAYER=?,ONBRIDGE=? where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getId() {
/*  92 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getPositionX() {
/* 102 */     return this.positionX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getPositionY() {
/* 112 */     return this.positionY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getPositionZ() {
/* 122 */     return this.positionZ;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getRotation() {
/* 132 */     return this.rotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getZoneid() {
/* 142 */     return this.zoneid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getLayer() {
/* 152 */     return this.layer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getBridgeId() {
/* 162 */     return this.bridgeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 173 */     return "CreaturePositionDbUpdatable [id=" + this.id + ", positionX=" + this.positionX + ", positionY=" + this.positionY + ", positionZ=" + this.positionZ + ", rotation=" + this.rotation + ", zoneid=" + this.zoneid + ", layer=" + this.layer + ", bridge=" + this.bridgeId + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\CreaturePositionDbUpdatable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */