/*     */ package com.wurmonline.server.sounds;
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
/*     */ public final class Sound
/*     */ {
/*     */   private final float posx;
/*     */   private final float posy;
/*     */   private final float posz;
/*     */   private final String name;
/*     */   private final float volume;
/*     */   private final float pitch;
/*     */   private final float priority;
/*     */   
/*     */   public Sound(String aResourceName, float aPosx, float aPosy, float aPosz, float aVolume, float aPitch, float aPriority) {
/*  57 */     if (aResourceName.startsWith("sound")) {
/*  58 */       this.name = aResourceName;
/*     */     } else {
/*  60 */       this.name = "sound." + aResourceName;
/*  61 */     }  this.posx = aPosx;
/*  62 */     this.posy = aPosy;
/*  63 */     this.posz = aPosz;
/*  64 */     this.volume = aVolume;
/*  65 */     this.pitch = aPitch;
/*  66 */     this.priority = aPriority;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPosX() {
/*  87 */     return this.posx;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPosY() {
/*  92 */     return this.posy;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPosZ() {
/*  97 */     return this.posz;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPitch() {
/* 106 */     return this.pitch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getVolume() {
/* 115 */     return this.volume;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 124 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPriority() {
/* 133 */     return this.priority;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\sounds\Sound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */