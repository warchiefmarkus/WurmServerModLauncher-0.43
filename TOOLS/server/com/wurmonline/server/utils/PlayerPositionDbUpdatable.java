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
/*     */ public final class PlayerPositionDbUpdatable
/*     */   implements WurmDbUpdatable
/*     */ {
/*     */   static final String SAVE_PLAYER_POSITION = "update POSITION set POSX=?, POSY=?, POSZ=?, ROTATION=?,ZONEID=?,LAYER=?,ONBRIDGE=? where WURMID=?";
/*     */   private final long id;
/*     */   private final float positionX;
/*     */   private final float positionY;
/*     */   private final float positionZ;
/*     */   private final float rotation;
/*     */   private final int zoneid;
/*     */   private final int layer;
/*     */   private final long bridgeId;
/*     */   
/*     */   public PlayerPositionDbUpdatable(long aId, float aPositionX, float aPositionY, float aPositionZ, float aRotation, int aZoneid, int aLayer, long bridgeid) {
/*  63 */     this.id = aId;
/*  64 */     this.positionX = aPositionX;
/*  65 */     this.positionY = aPositionY;
/*  66 */     this.positionZ = aPositionZ;
/*  67 */     this.rotation = aRotation;
/*  68 */     this.zoneid = aZoneid;
/*  69 */     this.layer = aLayer;
/*  70 */     this.bridgeId = bridgeid;
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
/*  81 */     return "update POSITION set POSX=?, POSY=?, POSZ=?, ROTATION=?,ZONEID=?,LAYER=?,ONBRIDGE=? where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getId() {
/*  91 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getPositionX() {
/* 101 */     return this.positionX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getPositionY() {
/* 111 */     return this.positionY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getPositionZ() {
/* 121 */     return this.positionZ;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getRotation() {
/* 131 */     return this.rotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getZoneid() {
/* 141 */     return this.zoneid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getLayer() {
/* 151 */     return this.layer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getBridgeId() {
/* 161 */     return this.bridgeId;
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
/* 172 */     return "PlayerPositionDbUpdatable [id=" + this.id + ", positionX=" + this.positionX + ", positionY=" + this.positionY + ", positionZ=" + this.positionZ + ", rotation=" + this.rotation + ", zoneid=" + this.zoneid + ", layer=" + this.layer + ", bridge=" + this.bridgeId + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\PlayerPositionDbUpdatable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */