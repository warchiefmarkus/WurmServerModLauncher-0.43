/*     */ package com.wurmonline.math;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Vector
/*     */ {
/*  13 */   public float[] vector = new float[4];
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector() {}
/*     */ 
/*     */   
/*     */   public Vector(float[] vector) {
/*  21 */     for (int i = 0; i < vector.length; i++)
/*     */     {
/*  23 */       this.vector[i] = vector[i];
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector(float v1, float v2, float v3) {
/*  29 */     this.vector[0] = v1;
/*  30 */     this.vector[1] = v2;
/*  31 */     this.vector[2] = v3;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector sub(Position3D p) {
/*  36 */     this.vector[0] = this.vector[0] - p.x;
/*  37 */     this.vector[1] = this.vector[1] - p.y;
/*  38 */     this.vector[2] = this.vector[2] - p.z;
/*     */     
/*  40 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final float[] getVector() {
/*  45 */     return this.vector;
/*     */   }
/*     */ 
/*     */   
/*     */   public final float[] getVector3() {
/*  50 */     float[] v = new float[3];
/*  51 */     v[0] = this.vector[0];
/*  52 */     v[1] = this.vector[1];
/*  53 */     v[2] = this.vector[2];
/*  54 */     return v;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector transform(Matrix m) {
/*  59 */     float[] matrix = m.getMatrix();
/*     */     
/*  61 */     float vx = this.vector[0] * matrix[0] + this.vector[1] * matrix[4] + this.vector[2] * matrix[8] + matrix[12];
/*  62 */     float vy = this.vector[0] * matrix[1] + this.vector[1] * matrix[5] + this.vector[2] * matrix[9] + matrix[13];
/*  63 */     float vz = this.vector[0] * matrix[2] + this.vector[1] * matrix[6] + this.vector[2] * matrix[10] + matrix[14];
/*  64 */     float vw = this.vector[0] * matrix[3] + this.vector[1] * matrix[7] + this.vector[2] * matrix[11] + matrix[15];
/*     */     
/*  66 */     this.vector[0] = vx;
/*  67 */     this.vector[1] = vy;
/*  68 */     this.vector[2] = vz;
/*  69 */     this.vector[3] = vw;
/*     */     
/*  71 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector transform3(Matrix m) {
/*  76 */     double[] v = new double[3];
/*  77 */     float[] matrix = m.getMatrix();
/*     */     
/*  79 */     v[0] = (this.vector[0] * matrix[0] + this.vector[1] * matrix[4] + this.vector[2] * matrix[8]);
/*  80 */     v[1] = (this.vector[0] * matrix[1] + this.vector[1] * matrix[5] + this.vector[2] * matrix[9]);
/*  81 */     v[2] = (this.vector[0] * matrix[2] + this.vector[1] * matrix[6] + this.vector[2] * matrix[10]);
/*     */     
/*  83 */     this.vector[0] = (float)v[0];
/*  84 */     this.vector[1] = (float)v[1];
/*  85 */     this.vector[2] = (float)v[2];
/*  86 */     this.vector[3] = 1.0F;
/*     */     
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector reset() {
/*  93 */     this.vector[0] = 0.0F;
/*  94 */     this.vector[1] = 0.0F;
/*  95 */     this.vector[2] = 0.0F;
/*  96 */     this.vector[3] = 1.0F;
/*  97 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector set(float x, float y, float z) {
/* 102 */     this.vector[0] = x;
/* 103 */     this.vector[1] = y;
/* 104 */     this.vector[2] = z;
/* 105 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector set(float x, float y, float z, float w) {
/* 110 */     this.vector[0] = x;
/* 111 */     this.vector[1] = y;
/* 112 */     this.vector[2] = z;
/* 113 */     this.vector[3] = w;
/* 114 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector set(float[] values) {
/* 119 */     this.vector[0] = values[0];
/* 120 */     this.vector[1] = values[1];
/* 121 */     this.vector[2] = values[2];
/* 122 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector set(Vector v) {
/* 127 */     this.vector[0] = v.vector[0];
/* 128 */     this.vector[1] = v.vector[1];
/* 129 */     this.vector[2] = v.vector[2];
/* 130 */     this.vector[3] = v.vector[3];
/* 131 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector add(Vector v) {
/* 136 */     this.vector[0] = this.vector[0] + v.vector[0];
/* 137 */     this.vector[1] = this.vector[1] + v.vector[1];
/* 138 */     this.vector[2] = this.vector[2] + v.vector[2];
/* 139 */     this.vector[3] = this.vector[3] + v.vector[3];
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector add(Vector v1, Vector v2) {
/* 145 */     add(v1);
/* 146 */     return add(v2);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector scale(float scale) {
/* 151 */     this.vector[0] = this.vector[0] * scale;
/* 152 */     this.vector[1] = this.vector[1] * scale;
/* 153 */     this.vector[2] = this.vector[2] * scale;
/* 154 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector normalize() {
/* 159 */     float len = length();
/*     */     
/* 161 */     this.vector[0] = this.vector[0] / len;
/* 162 */     this.vector[1] = this.vector[1] / len;
/* 163 */     this.vector[2] = this.vector[2] / len;
/*     */     
/* 165 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final float x() {
/* 170 */     return this.vector[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public final float y() {
/* 175 */     return this.vector[1];
/*     */   }
/*     */ 
/*     */   
/*     */   public final float z() {
/* 180 */     return this.vector[2];
/*     */   }
/*     */ 
/*     */   
/*     */   public final float w() {
/* 185 */     return this.vector[3];
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector negate() {
/* 190 */     this.vector[0] = -this.vector[0];
/* 191 */     this.vector[1] = -this.vector[1];
/* 192 */     this.vector[2] = -this.vector[2];
/* 193 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector cross(Vector v) {
/* 198 */     return 
/* 199 */       set(this.vector[1] * v.vector[2] - this.vector[2] * v.vector[1], this.vector[2] * v.vector[0] - this.vector[0] * v.vector[2], this.vector[0] * v.vector[1] - this.vector[1] * v.vector[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final float length() {
/* 206 */     return (float)Math.sqrt((this.vector[0] * this.vector[0] + this.vector[1] * this.vector[1] + this.vector[2] * this.vector[2]));
/*     */   }
/*     */ 
/*     */   
/*     */   public final float dot(Vector v) {
/* 211 */     return this.vector[0] * v.vector[0] + this.vector[1] * v.vector[1] + this.vector[2] * v.vector[2];
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\math\Vector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */