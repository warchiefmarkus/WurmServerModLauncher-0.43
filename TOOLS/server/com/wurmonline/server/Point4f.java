/*     */ package com.wurmonline.server;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Point4f
/*     */ {
/*     */   private float posx;
/*     */   private float posy;
/*     */   private float posz;
/*     */   private float rot;
/*     */   
/*     */   public Point4f() {
/*  38 */     this.posx = 0.0F;
/*  39 */     this.posy = 0.0F;
/*  40 */     this.posz = 0.0F;
/*  41 */     this.rot = 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public Point4f(float posx, float posy) {
/*  46 */     this.posx = posx;
/*  47 */     this.posy = posy;
/*  48 */     this.posz = 0.0F;
/*  49 */     this.rot = 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public Point4f(float posx, float posy, float posz) {
/*  54 */     this.posx = posx;
/*  55 */     this.posy = posy;
/*  56 */     this.posz = posz;
/*  57 */     this.rot = 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public Point4f(float posx, float posy, float posz, float rot) {
/*  62 */     this.posx = posx;
/*  63 */     this.posy = posy;
/*  64 */     this.posz = posz;
/*  65 */     this.rot = rot;
/*     */   }
/*     */ 
/*     */   
/*     */   public Point4f(Point4f point) {
/*  70 */     this.posx = point.posx;
/*  71 */     this.posy = point.posy;
/*  72 */     this.posz = point.posz;
/*  73 */     this.rot = point.rot;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPosX() {
/*  78 */     return this.posx;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPosX(float posx) {
/*  83 */     this.posx = posx;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPosY() {
/*  88 */     return this.posy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPosY(float posy) {
/*  93 */     this.posy = posy;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPosZ() {
/*  98 */     return this.posz;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPosZ(float posz) {
/* 103 */     this.posz = posz;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getRot() {
/* 108 */     return this.rot;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRot(float rot) {
/* 113 */     this.rot = rot;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setXY(float posx, float posy) {
/* 118 */     this.posx = posx;
/* 119 */     this.posy = posy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setXYZ(float posx, float posy, float posz) {
/* 124 */     this.posx = posx;
/* 125 */     this.posy = posy;
/* 126 */     this.posz = posz;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setXYR(float posx, float posy, float rot) {
/* 131 */     this.posx = posx;
/* 132 */     this.posy = posy;
/* 133 */     this.rot = rot;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setXYZR(float posx, float posy, float posz, float rot) {
/* 138 */     this.posx = posx;
/* 139 */     this.posy = posy;
/* 140 */     this.posz = posz;
/* 141 */     this.rot = rot;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTileX() {
/* 146 */     return (int)this.posx >> 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTileY() {
/* 151 */     return (int)this.posy >> 2;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\Point4f.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */