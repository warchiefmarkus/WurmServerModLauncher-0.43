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
/*     */ public final class ImprovedNoise
/*     */ {
/*  33 */   private final int[] p = new int[512];
/*     */ 
/*     */   
/*     */   public ImprovedNoise(long seed) {
/*  37 */     shuffle(seed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double noise(double x, double y, double z) {
/*  43 */     int X = (int)Math.floor(x) & 0xFF;
/*  44 */     int Y = (int)Math.floor(y) & 0xFF;
/*  45 */     int Z = (int)Math.floor(z) & 0xFF;
/*     */     
/*  47 */     x -= Math.floor(x);
/*  48 */     y -= Math.floor(y);
/*  49 */     z -= Math.floor(z);
/*     */     
/*  51 */     double u = fade(x);
/*  52 */     double v = fade(y);
/*  53 */     double w = fade(z);
/*     */ 
/*     */     
/*  56 */     int A = this.p[X] + Y;
/*  57 */     int AA = this.p[A] + Z;
/*  58 */     int AB = this.p[A + 1] + Z;
/*  59 */     int B = this.p[X + 1] + Y;
/*  60 */     int BA = this.p[B] + Z;
/*  61 */     int BB = this.p[B + 1] + Z;
/*     */     
/*  63 */     return lerp(w, lerp(v, lerp(u, grad(this.p[AA], x, y, z), 
/*  64 */             grad(this.p[BA], x - 1.0D, y, z)), 
/*  65 */           lerp(u, grad(this.p[AB], x, y - 1.0D, z), 
/*  66 */             grad(this.p[BB], x - 1.0D, y - 1.0D, z))), 
/*  67 */         lerp(v, lerp(u, grad(this.p[AA + 1], x, y, z - 1.0D), 
/*  68 */             grad(this.p[BA + 1], x - 1.0D, y, z - 1.0D)), 
/*  69 */           lerp(u, grad(this.p[AB + 1], x, y - 1.0D, z - 1.0D), grad(this.p[BB + 1], x - 1.0D, y - 1.0D, z - 1.0D))));
/*     */   }
/*     */ 
/*     */   
/*     */   double fade(double t) {
/*  74 */     return t * t * t * (t * (t * 6.0D - 15.0D) + 10.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   double lerp(double t, double a, double b) {
/*  79 */     return a + t * (b - a);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   double grad(int hash, double x, double y, double z) {
/*  85 */     int h = hash & 0xF;
/*  86 */     double u = (h < 8) ? x : y;
/*  87 */     double v = (h < 4) ? y : ((h == 12 || h == 14) ? x : z);
/*  88 */     return (((h & 0x1) == 0) ? u : -u) + (((h & 0x2) == 0) ? v : -v);
/*     */   }
/*     */ 
/*     */   
/*     */   public double perlinNoise(double x, double y) {
/*  93 */     double n = 0.0D;
/*     */     
/*  95 */     for (int i = 0; i < 8; i++) {
/*     */       
/*  97 */       double stepSize = 64.0D / (1 << i);
/*  98 */       n += noise(x / stepSize, y / stepSize, 128.0D) * 1.0D / (1 << i);
/*     */     } 
/*     */     
/* 101 */     return n;
/*     */   }
/*     */ 
/*     */   
/*     */   public void shuffle(long seed) {
/* 106 */     Random random = new Random(seed);
/* 107 */     int[] permutation = new int[256]; int i;
/* 108 */     for (i = 0; i < 256; i++)
/*     */     {
/* 110 */       permutation[i] = i;
/*     */     }
/*     */     
/* 113 */     for (i = 0; i < 256; i++) {
/*     */       
/* 115 */       int j = random.nextInt(256 - i) + i;
/* 116 */       int tmp = permutation[i];
/* 117 */       permutation[i] = permutation[j];
/* 118 */       permutation[j] = tmp;
/* 119 */       this.p[i] = permutation[i];
/* 120 */       this.p[i + 256] = permutation[i];
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\meshgen\ImprovedNoise.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */