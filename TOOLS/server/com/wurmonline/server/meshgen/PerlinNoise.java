/*     */ package com.wurmonline.server.meshgen;
/*     */ 
/*     */ import java.util.Random;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class PerlinNoise
/*     */ {
/*  23 */   static float[] f_lut = new float[4096];
/*     */   static {
/*  25 */     for (int i = 0; i < f_lut.length; i++) {
/*     */       
/*  27 */       double ft = i / f_lut.length * Math.PI;
/*  28 */       f_lut[i] = (float)((1.0D - Math.cos(ft)) * 0.5D);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   float[][] noise;
/*     */   private float[][] noiseValues;
/*     */   private int level;
/*     */   private int width;
/*     */   private Random random;
/*     */   
/*     */   private final class NoiseMap
/*     */   {
/*     */     private int lWidth;
/*     */     
/*     */     private NoiseMap(Random aRandom, int aWidth, int aMode) {
/*  44 */       this.lWidth = aWidth;
/*     */       
/*  46 */       if (aMode == 0) {
/*     */         
/*  48 */         for (int x = 0; x < aWidth; x++) {
/*  49 */           for (int y = 0; y < aWidth; y++) {
/*     */             
/*  51 */             if ((x == 0 || y == 0) && aMode < 3) {
/*  52 */               PerlinNoise.this.noise[x][y] = 0.0F;
/*     */             } else {
/*  54 */               PerlinNoise.this.noise[x][y] = aRandom.nextFloat();
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } else {
/*  59 */         for (int x = 0; x < aWidth; x++) {
/*  60 */           for (int y = 0; y < aWidth; y++) {
/*     */             
/*  62 */             if ((x == 0 || y == 0) && aMode < 3) {
/*  63 */               PerlinNoise.this.noise[x][y] = 0.0F;
/*     */             } else {
/*  65 */               PerlinNoise.this.noise[x][y] = (aRandom.nextFloat() + aRandom.nextFloat()) / 2.0F;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     private float getNoise(int x, int y) {
/*  72 */       return PerlinNoise.this.noise[x & this.lWidth - 1][y & this.lWidth - 1];
/*     */     }
/*     */ 
/*     */     
/*     */     private float getInterpolatedNoise(int x, int y, int xFraction, int yFraction) {
/*  77 */       float v1 = getNoise(x, y);
/*  78 */       float v2 = getNoise(x + 1, y);
/*  79 */       float v3 = getNoise(x, y + 1);
/*  80 */       float v4 = getNoise(x + 1, y + 1);
/*     */       
/*  82 */       float i1 = interpolate(v1, v2, xFraction);
/*  83 */       float i2 = interpolate(v3, v4, xFraction);
/*     */       
/*  85 */       return interpolate(i1, i2, yFraction);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private final float interpolate(float a, float b, int x) {
/*  91 */       float f = PerlinNoise.f_lut[x];
/*  92 */       return a * (1.0F - f) + b * f;
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
/*     */   PerlinNoise(Random aRandom, int aLevel) {
/* 104 */     this.random = aRandom;
/* 105 */     this.width = 2 << aLevel;
/* 106 */     this.level = aLevel;
/* 107 */     if (this.width > 4096) {
/* 108 */       throw new IllegalArgumentException("Max size is 4096");
/*     */     }
/* 110 */     this.noise = new float[this.width][this.width];
/* 111 */     this.noiseValues = new float[this.width][this.width];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   float[][] generatePerlinNoise(float persistence, int mode, MeshGenGui.Task task, int progressStart, int progressRange) {
/* 117 */     int highnoisesteps = 1;
/* 118 */     int start = 0;
/*     */     
/* 120 */     for (int x = 0; x < this.width; x++) {
/*     */       
/* 122 */       for (int y = 0; y < this.width; y++)
/*     */       {
/* 124 */         this.noiseValues[x][y] = 0.0F;
/*     */       }
/*     */     } 
/*     */     
/* 128 */     for (int i = 0; i < this.level + 2; i++) {
/*     */       
/* 130 */       int w = 1 << i;
/*     */       
/* 132 */       float perst = (float)Math.pow(0.9990000128746033D, (i - 0 + 1)) * persistence;
/* 133 */       float amplitude = (float)Math.pow(perst, (i - 0 + 1));
/* 134 */       if (i <= 1)
/*     */       {
/* 136 */         amplitude *= (i * i) / 1.0F;
/*     */       }
/*     */ 
/*     */       
/* 140 */       NoiseMap noiseMap = new NoiseMap(this.random, w, mode);
/*     */ 
/*     */       
/* 143 */       for (int j = 0; j < this.width; j++) {
/*     */         
/* 145 */         task.setNote(progressStart + (j + (i - 0) * this.width) / (this.level - 0 + 2));
/* 146 */         int xx = j * w / this.width;
/* 147 */         int xx2 = j * w % this.width * 4096 / this.width;
/*     */         
/* 149 */         for (int y = 0; y < this.width; y++) {
/*     */           
/* 151 */           int yy = y * w / this.width;
/*     */           
/* 153 */           int yy2 = y * w % this.width * 4096 / this.width;
/*     */           
/* 155 */           this.noiseValues[j][y] = this.noiseValues[j][y] + noiseMap.getInterpolatedNoise(xx, yy, xx2, yy2) * amplitude;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 162 */     return this.noiseValues;
/*     */   }
/*     */ 
/*     */   
/*     */   void setRandom(Random aRandom) {
/* 167 */     this.random = aRandom;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\meshgen\PerlinNoise.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */