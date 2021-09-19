/*     */ package com.wurmonline.server.effects;
/*     */ 
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zone;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
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
/*     */ public abstract class Effect
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7768268294902679751L;
/*     */   private long owner;
/*     */   private short type;
/*     */   private float posX;
/*     */   private float posY;
/*     */   private float posZ;
/*     */   private boolean surfaced;
/*  65 */   private int id = 0;
/*     */   
/*  67 */   private static final Logger logger = Logger.getLogger(Effect.class.getName());
/*     */   
/*     */   private long startTime;
/*     */   private String effectString;
/*  71 */   private float timeout = -1.0F;
/*  72 */   private float rotationOffset = 0.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Effect(long aOwner, short aType, float aPosX, float aPosY, float aPosZ, boolean aSurfaced) {
/*  80 */     this.owner = aOwner;
/*  81 */     this.type = aType;
/*  82 */     this.posX = aPosX;
/*  83 */     this.posY = aPosY;
/*  84 */     this.posZ = aPosZ;
/*  85 */     this.surfaced = aSurfaced;
/*  86 */     this.startTime = System.currentTimeMillis();
/*     */     
/*     */     try {
/*  89 */       save();
/*     */     }
/*  91 */     catch (IOException iox) {
/*     */       
/*  93 */       logger.log(Level.WARNING, "Failed to save effect", iox);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Effect(int num, long ownerid, short typ, float posx, float posy, float posz, long stime) {
/* 100 */     this.id = num;
/* 101 */     this.owner = ownerid;
/* 102 */     this.type = typ;
/* 103 */     this.posX = posx;
/* 104 */     this.posY = posy;
/* 105 */     this.posZ = posz;
/* 106 */     this.startTime = stime;
/*     */   }
/*     */ 
/*     */   
/*     */   Effect(long aOwner, int aNumber) throws IOException {
/* 111 */     this.owner = aOwner;
/* 112 */     this.id = aNumber;
/* 113 */     load();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getStartTime() {
/* 123 */     return this.startTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void setStartTime(long aStartTime) {
/* 134 */     this.startTime = aStartTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getId() {
/* 143 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void setId(int aId) {
/* 154 */     this.id = aId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getTileX() {
/* 164 */     return (int)this.posX >> 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getTileY() {
/* 174 */     return (int)this.posY >> 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final float getPosX() {
/* 183 */     return this.posX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setPosX(float positionX) {
/* 192 */     this.posX = positionX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setPosY(float positionY) {
/* 201 */     this.posY = positionY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final float getPosY() {
/* 210 */     return this.posY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final float getPosZ() {
/* 219 */     return this.posZ;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setPosZ(float aPosZ) {
/* 228 */     this.posZ = aPosZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public final float calculatePosZ(VolaTile tile) {
/* 233 */     return Zones.calculatePosZ(getPosX(), getPosY(), tile, isOnSurface(), false, getPosZ(), null, -10L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getOwner() {
/* 242 */     return this.owner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void setOwner(long aOwner) {
/* 253 */     this.owner = aOwner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final short getType() {
/* 262 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isGlobal() {
/* 267 */     return (this.type == 2 || this.type == 3 || this.type == 16 || this.type == 19 || this.type == 4 || this.type == 25);
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
/*     */   final void setType(short aType) {
/* 280 */     this.type = aType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setSurfaced(boolean aSurfaced) {
/* 289 */     this.surfaced = aSurfaced;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isOnSurface() {
/* 298 */     return this.surfaced;
/*     */   }
/*     */ 
/*     */   
/*     */   public final byte getLayer() {
/* 303 */     return (byte)(this.surfaced ? 0 : -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setEffectString(String effString) {
/* 308 */     this.effectString = effString;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getEffectString() {
/* 313 */     return this.effectString;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setTimeout(float timeout) {
/* 318 */     this.timeout = timeout;
/*     */   }
/*     */ 
/*     */   
/*     */   public final float getTimeout() {
/* 323 */     return this.timeout;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setRotationOffset(float rotationOffset) {
/* 328 */     this.rotationOffset = rotationOffset;
/*     */   }
/*     */ 
/*     */   
/*     */   public final float getRotationOffset() {
/* 333 */     return this.rotationOffset;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPosXYZ(float posX, float posY, float posZ, boolean sendUpdate) {
/* 338 */     if (posX != getPosX() || posY != getPosY() || posZ != getPosZ())
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 343 */         if (sendUpdate) {
/*     */           
/* 345 */           Zone zone = Zones.getZone((int)(getPosX() / 4.0F), (int)(getPosY() / 4.0F), isOnSurface());
/* 346 */           zone.removeEffect(this);
/*     */         } 
/*     */         
/* 349 */         setPosX(posX);
/* 350 */         setPosY(posY);
/* 351 */         setPosZ(posZ);
/*     */         
/* 353 */         if (sendUpdate)
/*     */         {
/* 355 */           Zone zone = Zones.getZone((int)(getPosX() / 4.0F), (int)(getPosY() / 4.0F), isOnSurface());
/* 356 */           zone.addEffect(this, false);
/*     */         }
/*     */       
/* 359 */       } catch (NoSuchZoneException noSuchZoneException) {} 
/*     */   }
/*     */   
/*     */   public abstract void save() throws IOException;
/*     */   
/*     */   abstract void load() throws IOException;
/*     */   
/*     */   abstract void delete();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\effects\Effect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */