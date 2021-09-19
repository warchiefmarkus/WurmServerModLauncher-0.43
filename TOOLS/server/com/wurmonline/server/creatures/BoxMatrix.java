/*     */ package com.wurmonline.server.creatures;
/*     */ 
/*     */ import com.wurmonline.math.Vector3f;
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
/*     */ public class BoxMatrix
/*     */ {
/*  27 */   public float[] mf = new float[16];
/*     */ 
/*     */   
/*     */   public BoxMatrix(boolean identity) {
/*  31 */     if (identity) {
/*  32 */       identity();
/*     */     }
/*     */   }
/*     */   
/*     */   public final BoxMatrix multiply(BoxMatrix inM) {
/*  37 */     BoxMatrix result = new BoxMatrix(false);
/*  38 */     for (int i = 0; i < 16; i += 4) {
/*     */       
/*  40 */       for (int j = 0; j < 4; j++)
/*     */       {
/*  42 */         result.mf[i + j] = this.mf[i + 0] * inM.mf[0 + j] + this.mf[i + 1] * inM.mf[4 + j] + this.mf[i + 2] * inM.mf[8 + j] + this.mf[i + 3] * inM.mf[12 + j];
/*     */       }
/*     */     } 
/*     */     
/*  46 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector3f multiply(Vector3f Point) {
/*  51 */     float x = Point.x * this.mf[0] + Point.y * this.mf[4] + Point.z * this.mf[8] + this.mf[12];
/*  52 */     float y = Point.x * this.mf[1] + Point.y * this.mf[5] + Point.z * this.mf[9] + this.mf[13];
/*  53 */     float z = Point.x * this.mf[2] + Point.y * this.mf[6] + Point.z * this.mf[10] + this.mf[14];
/*  54 */     return new Vector3f(x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rotate(float degrees, boolean xRot, boolean yRot, boolean zRot) {
/*  59 */     BoxMatrix temp = new BoxMatrix(true);
/*  60 */     if (xRot)
/*  61 */       temp.rotX(-degrees); 
/*  62 */     if (yRot)
/*  63 */       temp.rotY(-degrees); 
/*  64 */     if (zRot)
/*  65 */       temp.rotZ(-degrees); 
/*  66 */     this.mf = (temp.multiply(this)).mf;
/*     */   }
/*     */ 
/*     */   
/*     */   public void scale(float sx, float sy, float sz) {
/*     */     int x;
/*  72 */     for (x = 0; x < 4; x++)
/*  73 */       this.mf[x] = this.mf[x] * sx; 
/*  74 */     for (x = 4; x < 8; x++)
/*  75 */       this.mf[x] = this.mf[x] * sy; 
/*  76 */     for (x = 8; x < 12; x++) {
/*  77 */       this.mf[x] = this.mf[x] * sz;
/*     */     }
/*     */   }
/*     */   
/*     */   public void translate(Vector3f test) {
/*  82 */     for (int j = 0; j < 4; j++)
/*     */     {
/*  84 */       this.mf[12 + j] = this.mf[12 + j] + test.x * this.mf[j] + test.y * this.mf[4 + j] + test.z * this.mf[8 + j];
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final BoxMatrix rotationOnly() {
/*  90 */     BoxMatrix Temp = new BoxMatrix(true);
/*  91 */     Temp.mf = this.mf;
/*  92 */     Temp.mf[12] = 0.0F;
/*  93 */     Temp.mf[13] = 0.0F;
/*  94 */     Temp.mf[14] = 0.0F;
/*  95 */     return Temp;
/*     */   }
/*     */ 
/*     */   
/*     */   private void rotX(float angle) {
/* 100 */     this.mf[5] = (float)Math.cos(Math.toRadians(angle));
/* 101 */     this.mf[6] = (float)Math.sin(Math.toRadians(angle));
/* 102 */     this.mf[9] = (float)-Math.sin(Math.toRadians(angle));
/* 103 */     this.mf[10] = (float)Math.cos(Math.toRadians(angle));
/*     */   }
/*     */ 
/*     */   
/*     */   private void rotY(float angle) {
/* 108 */     this.mf[0] = (float)Math.cos(Math.toRadians(angle));
/* 109 */     this.mf[2] = (float)-Math.sin(Math.toRadians(angle));
/* 110 */     this.mf[8] = (float)Math.sin(Math.toRadians(angle));
/* 111 */     this.mf[10] = (float)Math.cos(Math.toRadians(angle));
/*     */   }
/*     */ 
/*     */   
/*     */   private void rotZ(float angle) {
/* 116 */     this.mf[0] = (float)Math.cos(Math.toRadians(angle));
/* 117 */     this.mf[1] = (float)Math.sin(Math.toRadians(angle));
/* 118 */     this.mf[4] = (float)-Math.sin(Math.toRadians(angle));
/* 119 */     this.mf[5] = (float)Math.cos(Math.toRadians(angle));
/*     */   }
/*     */ 
/*     */   
/*     */   public final BoxMatrix InvertSimple() {
/* 124 */     BoxMatrix R = new BoxMatrix(false);
/* 125 */     R.mf[0] = this.mf[0];
/* 126 */     R.mf[1] = this.mf[4];
/* 127 */     R.mf[2] = this.mf[8];
/* 128 */     R.mf[3] = 0.0F;
/* 129 */     R.mf[4] = this.mf[1];
/* 130 */     R.mf[5] = this.mf[5];
/* 131 */     R.mf[6] = this.mf[9];
/* 132 */     R.mf[7] = 0.0F;
/* 133 */     R.mf[8] = this.mf[2];
/* 134 */     R.mf[9] = this.mf[6];
/* 135 */     R.mf[10] = this.mf[10];
/* 136 */     R.mf[11] = 0.0F;
/* 137 */     R.mf[12] = -(this.mf[12] * this.mf[0]) - this.mf[13] * this.mf[1] - this.mf[14] * this.mf[2];
/* 138 */     R.mf[13] = -(this.mf[12] * this.mf[4]) - this.mf[13] * this.mf[5] - this.mf[14] * this.mf[6];
/* 139 */     R.mf[14] = -(this.mf[12] * this.mf[8]) - this.mf[13] * this.mf[9] - this.mf[14] * this.mf[10];
/* 140 */     R.mf[15] = 1.0F;
/* 141 */     return R;
/*     */   }
/*     */ 
/*     */   
/*     */   public final BoxMatrix InvertRot() {
/* 146 */     BoxMatrix R = new BoxMatrix(false);
/* 147 */     R.mf[0] = this.mf[0];
/* 148 */     R.mf[1] = this.mf[4];
/* 149 */     R.mf[2] = this.mf[8];
/* 150 */     R.mf[3] = 0.0F;
/* 151 */     R.mf[4] = this.mf[1];
/* 152 */     R.mf[5] = this.mf[5];
/* 153 */     R.mf[6] = this.mf[9];
/* 154 */     R.mf[7] = 0.0F;
/* 155 */     R.mf[8] = this.mf[2];
/* 156 */     R.mf[9] = this.mf[6];
/* 157 */     R.mf[10] = this.mf[10];
/* 158 */     R.mf[11] = 0.0F;
/* 159 */     R.mf[12] = 0.0F;
/* 160 */     R.mf[13] = 0.0F;
/* 161 */     R.mf[14] = 0.0F;
/* 162 */     R.mf[15] = 1.0F;
/* 163 */     return R;
/*     */   }
/*     */ 
/*     */   
/*     */   public void RotateMatrix(float fDegrees, float x, float y, float z) {
/* 168 */     identity();
/* 169 */     float cosA = (float)Math.cos(Math.toRadians(fDegrees));
/* 170 */     float sinA = (float)Math.sin(Math.toRadians(fDegrees));
/* 171 */     float m = 1.0F - cosA;
/* 172 */     this.mf[0] = cosA + x * x * m;
/* 173 */     this.mf[5] = cosA + y * y * m;
/* 174 */     this.mf[10] = cosA + z * z * m;
/*     */     
/* 176 */     float tmp1 = x * y * m;
/* 177 */     float tmp2 = z * sinA;
/* 178 */     this.mf[4] = tmp1 + tmp2;
/* 179 */     this.mf[1] = tmp1 - tmp2;
/*     */     
/* 181 */     tmp1 = x * z * m;
/* 182 */     tmp2 = y * sinA;
/* 183 */     this.mf[8] = tmp1 - tmp2;
/* 184 */     this.mf[2] = tmp1 + tmp2;
/*     */     
/* 186 */     tmp1 = y * z * m;
/* 187 */     tmp2 = x * sinA;
/* 188 */     this.mf[9] = tmp1 + tmp2;
/* 189 */     this.mf[6] = tmp1 - tmp2;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector3f getTranslate() {
/* 194 */     return new Vector3f(this.mf[12], this.mf[13], this.mf[14]);
/*     */   }
/*     */ 
/*     */   
/*     */   void identity() {
/* 199 */     this.mf[0] = 1.0F;
/* 200 */     this.mf[1] = 0.0F;
/* 201 */     this.mf[2] = 0.0F;
/* 202 */     this.mf[3] = 0.0F;
/* 203 */     this.mf[4] = 0.0F;
/* 204 */     this.mf[5] = 1.0F;
/* 205 */     this.mf[6] = 0.0F;
/* 206 */     this.mf[7] = 0.0F;
/* 207 */     this.mf[8] = 0.0F;
/* 208 */     this.mf[9] = 0.0F;
/* 209 */     this.mf[10] = 1.0F;
/* 210 */     this.mf[11] = 0.0F;
/* 211 */     this.mf[12] = 0.0F;
/* 212 */     this.mf[13] = 0.0F;
/* 213 */     this.mf[14] = 0.0F;
/* 214 */     this.mf[15] = 1.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\BoxMatrix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */