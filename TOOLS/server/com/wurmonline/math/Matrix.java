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
/*     */ public final class Matrix
/*     */ {
/*  23 */   private float[] matrix = new float[16];
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix() {}
/*     */ 
/*     */   
/*     */   public Matrix(Matrix matrix) {
/*  31 */     this.matrix = matrix.matrix;
/*     */   }
/*     */ 
/*     */   
/*     */   public float[] inverseRotateVect(float[] pVect) {
/*  36 */     float[] vec = new float[3];
/*     */     
/*  38 */     vec[0] = pVect[0] * this.matrix[0] + pVect[1] * this.matrix[1] + pVect[2] * this.matrix[2];
/*  39 */     vec[1] = pVect[0] * this.matrix[4] + pVect[1] * this.matrix[5] + pVect[2] * this.matrix[6];
/*  40 */     vec[2] = pVect[0] * this.matrix[8] + pVect[1] * this.matrix[9] + pVect[2] * this.matrix[10];
/*     */     
/*  42 */     return vec;
/*     */   }
/*     */ 
/*     */   
/*     */   public float[] inverseTranslateVect(float[] pVect) {
/*  47 */     pVect[0] = pVect[0] - this.matrix[12];
/*  48 */     pVect[1] = pVect[1] - this.matrix[13];
/*  49 */     pVect[2] = pVect[2] - this.matrix[14];
/*     */     
/*  51 */     return pVect;
/*     */   }
/*     */ 
/*     */   
/*     */   public void postMultiply(Matrix m2) {
/*  56 */     float[] newMatrix = new float[16];
/*     */     
/*  58 */     newMatrix[0] = this.matrix[0] * m2.matrix[0] + this.matrix[4] * m2.matrix[1] + this.matrix[8] * m2.matrix[2];
/*  59 */     newMatrix[1] = this.matrix[1] * m2.matrix[0] + this.matrix[5] * m2.matrix[1] + this.matrix[9] * m2.matrix[2];
/*  60 */     newMatrix[2] = this.matrix[2] * m2.matrix[0] + this.matrix[6] * m2.matrix[1] + this.matrix[10] * m2.matrix[2];
/*  61 */     newMatrix[3] = 0.0F;
/*     */     
/*  63 */     newMatrix[4] = this.matrix[0] * m2.matrix[4] + this.matrix[4] * m2.matrix[5] + this.matrix[8] * m2.matrix[6];
/*  64 */     newMatrix[5] = this.matrix[1] * m2.matrix[4] + this.matrix[5] * m2.matrix[5] + this.matrix[9] * m2.matrix[6];
/*  65 */     newMatrix[6] = this.matrix[2] * m2.matrix[4] + this.matrix[6] * m2.matrix[5] + this.matrix[10] * m2.matrix[6];
/*  66 */     newMatrix[7] = 0.0F;
/*     */     
/*  68 */     newMatrix[8] = this.matrix[0] * m2.matrix[8] + this.matrix[4] * m2.matrix[9] + this.matrix[8] * m2.matrix[10];
/*  69 */     newMatrix[9] = this.matrix[1] * m2.matrix[8] + this.matrix[5] * m2.matrix[9] + this.matrix[9] * m2.matrix[10];
/*  70 */     newMatrix[10] = this.matrix[2] * m2.matrix[8] + this.matrix[6] * m2.matrix[9] + this.matrix[10] * m2.matrix[10];
/*  71 */     newMatrix[11] = 0.0F;
/*     */     
/*  73 */     newMatrix[12] = this.matrix[0] * m2.matrix[12] + this.matrix[4] * m2.matrix[13] + this.matrix[8] * m2.matrix[14] + this.matrix[12];
/*  74 */     newMatrix[13] = this.matrix[1] * m2.matrix[12] + this.matrix[5] * m2.matrix[13] + this.matrix[9] * m2.matrix[14] + this.matrix[13];
/*  75 */     newMatrix[14] = this.matrix[2] * m2.matrix[12] + this.matrix[6] * m2.matrix[13] + this.matrix[10] * m2.matrix[14] + this.matrix[14];
/*  76 */     newMatrix[15] = 1.0F;
/*     */     
/*  78 */     set(newMatrix);
/*     */   }
/*     */ 
/*     */   
/*     */   public void postMultiplyFull(Matrix m2) {
/*  83 */     float[] newMatrix = new float[16];
/*     */     
/*  85 */     newMatrix[0] = this.matrix[0] * m2.matrix[0] + this.matrix[4] * m2.matrix[1] + this.matrix[8] * m2.matrix[2] + this.matrix[12] * m2.matrix[3];
/*  86 */     newMatrix[1] = this.matrix[1] * m2.matrix[0] + this.matrix[5] * m2.matrix[1] + this.matrix[9] * m2.matrix[2] + this.matrix[13] * m2.matrix[3];
/*  87 */     newMatrix[2] = this.matrix[2] * m2.matrix[0] + this.matrix[6] * m2.matrix[1] + this.matrix[10] * m2.matrix[2] + this.matrix[14] * m2.matrix[3];
/*  88 */     newMatrix[3] = this.matrix[3] * m2.matrix[0] + this.matrix[7] * m2.matrix[1] + this.matrix[11] * m2.matrix[2] + this.matrix[15] * m2.matrix[3];
/*     */     
/*  90 */     newMatrix[4] = this.matrix[0] * m2.matrix[4] + this.matrix[4] * m2.matrix[5] + this.matrix[8] * m2.matrix[6] + this.matrix[12] * m2.matrix[7];
/*  91 */     newMatrix[5] = this.matrix[1] * m2.matrix[4] + this.matrix[5] * m2.matrix[5] + this.matrix[9] * m2.matrix[6] + this.matrix[13] * m2.matrix[7];
/*  92 */     newMatrix[6] = this.matrix[2] * m2.matrix[4] + this.matrix[6] * m2.matrix[5] + this.matrix[10] * m2.matrix[6] + this.matrix[14] * m2.matrix[7];
/*  93 */     newMatrix[7] = this.matrix[3] * m2.matrix[4] + this.matrix[7] * m2.matrix[5] + this.matrix[11] * m2.matrix[6] + this.matrix[15] * m2.matrix[7];
/*     */     
/*  95 */     newMatrix[8] = this.matrix[0] * m2.matrix[8] + this.matrix[4] * m2.matrix[9] + this.matrix[8] * m2.matrix[10] + this.matrix[12] * m2.matrix[11];
/*  96 */     newMatrix[9] = this.matrix[1] * m2.matrix[8] + this.matrix[5] * m2.matrix[9] + this.matrix[9] * m2.matrix[10] + this.matrix[13] * m2.matrix[11];
/*  97 */     newMatrix[10] = this.matrix[2] * m2.matrix[8] + this.matrix[6] * m2.matrix[9] + this.matrix[10] * m2.matrix[10] + this.matrix[14] * m2.matrix[11];
/*  98 */     newMatrix[11] = this.matrix[3] * m2.matrix[8] + this.matrix[7] * m2.matrix[9] + this.matrix[11] * m2.matrix[10] + this.matrix[15] * m2.matrix[11];
/*     */     
/* 100 */     newMatrix[12] = this.matrix[0] * m2.matrix[12] + this.matrix[4] * m2.matrix[13] + this.matrix[8] * m2.matrix[14] + this.matrix[12] * m2.matrix[15];
/* 101 */     newMatrix[13] = this.matrix[1] * m2.matrix[12] + this.matrix[5] * m2.matrix[13] + this.matrix[9] * m2.matrix[14] + this.matrix[13] * m2.matrix[15];
/* 102 */     newMatrix[14] = this.matrix[2] * m2.matrix[12] + this.matrix[6] * m2.matrix[13] + this.matrix[10] * m2.matrix[14] + this.matrix[14] * m2.matrix[15];
/* 103 */     newMatrix[15] = this.matrix[3] * m2.matrix[12] + this.matrix[7] * m2.matrix[13] + this.matrix[11] * m2.matrix[14] + this.matrix[15] * m2.matrix[15];
/*     */     
/* 105 */     set(newMatrix);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Matrix setTranslation(float[] translation) {
/* 110 */     this.matrix[12] = translation[0];
/* 111 */     this.matrix[13] = translation[1];
/* 112 */     this.matrix[14] = translation[2];
/* 113 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Matrix setTranslation(float x, float y, float z) {
/* 118 */     this.matrix[12] = x;
/* 119 */     this.matrix[13] = y;
/* 120 */     this.matrix[14] = z;
/* 121 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Matrix setTranslation(Vector translation) {
/* 126 */     this.matrix[12] = translation.x();
/* 127 */     this.matrix[13] = translation.y();
/* 128 */     this.matrix[14] = translation.z();
/* 129 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInverseTranslation(float[] translation) {
/* 134 */     this.matrix[12] = -translation[0];
/* 135 */     this.matrix[13] = -translation[1];
/* 136 */     this.matrix[14] = -translation[2];
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRotationDegrees(float[] angles) {
/* 141 */     float[] vec = new float[3];
/* 142 */     vec[0] = (float)(angles[0] * 180.0D / Math.PI);
/* 143 */     vec[1] = (float)(angles[1] * 180.0D / Math.PI);
/* 144 */     vec[2] = (float)(angles[2] * 180.0D / Math.PI);
/* 145 */     setRotationRadians(vec);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInverseRotationDegrees(float[] angles) {
/* 150 */     float[] vec = new float[3];
/* 151 */     vec[0] = (float)(angles[0] * 180.0D / Math.PI);
/* 152 */     vec[1] = (float)(angles[1] * 180.0D / Math.PI);
/* 153 */     vec[2] = (float)(angles[2] * 180.0D / Math.PI);
/* 154 */     setInverseRotationRadians(vec);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRotationRadians(float[] angles) {
/* 159 */     float cr = (float)Math.cos(angles[0]);
/* 160 */     float sr = (float)Math.sin(angles[0]);
/* 161 */     float cp = (float)Math.cos(angles[1]);
/* 162 */     float sp = (float)Math.sin(angles[1]);
/* 163 */     float cy = (float)Math.cos(angles[2]);
/* 164 */     float sy = (float)Math.sin(angles[2]);
/*     */     
/* 166 */     this.matrix[0] = cp * cy;
/* 167 */     this.matrix[1] = cp * sy;
/* 168 */     this.matrix[2] = -sp;
/*     */     
/* 170 */     float srsp = sr * sp;
/* 171 */     float crsp = cr * sp;
/*     */     
/* 173 */     this.matrix[4] = srsp * cy - cr * sy;
/* 174 */     this.matrix[5] = srsp * sy + cr * cy;
/* 175 */     this.matrix[6] = sr * cp;
/*     */     
/* 177 */     this.matrix[8] = crsp * cy + sr * sy;
/* 178 */     this.matrix[9] = crsp * sy - sr * cy;
/* 179 */     this.matrix[10] = cr * cp;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInverseRotationRadians(float[] angles) {
/* 184 */     float cr = (float)Math.cos(angles[0]);
/* 185 */     float sr = (float)Math.sin(angles[0]);
/* 186 */     float cp = (float)Math.cos(angles[1]);
/* 187 */     float sp = (float)Math.sin(angles[1]);
/* 188 */     float cy = (float)Math.cos(angles[2]);
/* 189 */     float sy = (float)Math.sin(angles[2]);
/*     */     
/* 191 */     this.matrix[0] = cp * cy;
/* 192 */     this.matrix[4] = cp * sy;
/* 193 */     this.matrix[8] = -sp;
/*     */     
/* 195 */     float srsp = sr * sp;
/* 196 */     float crsp = cr * sp;
/*     */     
/* 198 */     this.matrix[1] = srsp * cy - cr * sy;
/* 199 */     this.matrix[5] = srsp * sy + cr * cy;
/* 200 */     this.matrix[9] = sr * cp;
/*     */     
/* 202 */     this.matrix[2] = crsp * cy + sr * sy;
/* 203 */     this.matrix[6] = crsp * sy - sr * cy;
/* 204 */     this.matrix[10] = cr * cp;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Matrix setRotationQuaternion(Quaternion quaternion) {
/* 209 */     float[] quat = quaternion.getQuat();
/* 210 */     this.matrix[0] = (float)(1.0D - 2.0D * quat[1] * quat[1] - 2.0D * quat[2] * quat[2]);
/* 211 */     this.matrix[1] = (float)(2.0D * quat[0] * quat[1] + 2.0D * quat[3] * quat[2]);
/* 212 */     this.matrix[2] = (float)(2.0D * quat[0] * quat[2] - 2.0D * quat[3] * quat[1]);
/*     */     
/* 214 */     this.matrix[4] = (float)(2.0D * quat[0] * quat[1] - 2.0D * quat[3] * quat[2]);
/* 215 */     this.matrix[5] = (float)(1.0D - 2.0D * quat[0] * quat[0] - 2.0D * quat[2] * quat[2]);
/* 216 */     this.matrix[6] = (float)(2.0D * quat[1] * quat[2] + 2.0D * quat[3] * quat[0]);
/*     */     
/* 218 */     this.matrix[8] = (float)(2.0D * quat[0] * quat[2] + 2.0D * quat[3] * quat[1]);
/* 219 */     this.matrix[9] = (float)(2.0D * quat[1] * quat[2] - 2.0D * quat[3] * quat[0]);
/* 220 */     this.matrix[10] = (float)(1.0D - 2.0D * quat[0] * quat[0] - 2.0D * quat[1] * quat[1]);
/*     */     
/* 222 */     this.matrix[14] = 0.0F; this.matrix[13] = 0.0F; this.matrix[12] = 0.0F; this.matrix[11] = 0.0F; this.matrix[7] = 0.0F; this.matrix[3] = 0.0F;
/* 223 */     this.matrix[15] = 1.0F;
/*     */     
/* 225 */     return this;
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
/*     */   public void set(float[] matrix) {
/* 261 */     this.matrix = matrix;
/*     */   }
/*     */ 
/*     */   
/*     */   public float get(int i, int j) {
/* 266 */     return this.matrix[4 * i + j];
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(int i, int j, float val) {
/* 271 */     this.matrix[4 * i + j] = val;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Matrix loadIdentity() {
/* 276 */     for (int i = 0; i < 16; i++)
/* 277 */       this.matrix[i] = 0.0F; 
/* 278 */     this.matrix[15] = 1.0F; this.matrix[10] = 1.0F; this.matrix[5] = 1.0F; this.matrix[0] = 1.0F;
/*     */ 
/*     */ 
/*     */     
/* 282 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScale(float scalX, float scalY, float scalZ) {
/* 287 */     this.matrix[0] = this.matrix[0] * scalX;
/* 288 */     this.matrix[5] = this.matrix[5] * scalY;
/* 289 */     this.matrix[10] = this.matrix[10] * scalZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public Matrix inverse() {
/* 294 */     this.matrix[0] = -this.matrix[0];
/* 295 */     this.matrix[1] = -this.matrix[1];
/* 296 */     this.matrix[2] = -this.matrix[2];
/* 297 */     this.matrix[4] = -this.matrix[4];
/* 298 */     this.matrix[5] = -this.matrix[5];
/* 299 */     this.matrix[6] = -this.matrix[6];
/* 300 */     this.matrix[8] = -this.matrix[8];
/* 301 */     this.matrix[9] = -this.matrix[9];
/* 302 */     this.matrix[10] = -this.matrix[10];
/*     */     
/* 304 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final float[] getMatrix() {
/* 314 */     return this.matrix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setMatrix(float[] matrix) {
/* 323 */     this.matrix = matrix;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Matrix setAxisX(float x, float y, float z) {
/* 328 */     this.matrix[0] = x;
/* 329 */     this.matrix[1] = y;
/* 330 */     this.matrix[2] = z;
/* 331 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Matrix setAxisY(float x, float y, float z) {
/* 336 */     this.matrix[4] = x;
/* 337 */     this.matrix[5] = y;
/* 338 */     this.matrix[6] = z;
/* 339 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Matrix setAxisZ(float x, float y, float z) {
/* 344 */     this.matrix[8] = x;
/* 345 */     this.matrix[9] = y;
/* 346 */     this.matrix[10] = z;
/* 347 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\math\Matrix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */