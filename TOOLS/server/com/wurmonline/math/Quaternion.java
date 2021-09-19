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
/*     */ 
/*     */ public final class Quaternion
/*     */ {
/*  24 */   private float[] quat = new float[4];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion(float[] angles) {
/*  35 */     fromAngles(angles);
/*     */   }
/*     */ 
/*     */   
/*     */   public Quaternion(Quaternion q1, Quaternion q2, float interp) {
/*  40 */     slerp(q1, q2, interp);
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
/*     */   public Quaternion mult(Quaternion q) {
/*  53 */     float x = this.quat[1] * q.quat[2] - this.quat[2] * q.quat[1] + this.quat[3] * q.quat[0] + this.quat[0] * q.quat[3];
/*  54 */     float y = this.quat[2] * q.quat[0] - this.quat[0] * q.quat[2] + this.quat[3] * q.quat[1] + this.quat[1] * q.quat[3];
/*  55 */     float z = this.quat[0] * q.quat[1] - this.quat[1] * q.quat[0] + this.quat[3] * q.quat[2] + this.quat[2] * q.quat[3];
/*  56 */     float s = this.quat[3] * q.quat[3] - this.quat[0] * q.quat[0] + this.quat[1] * q.quat[1] + this.quat[2] * q.quat[2];
/*     */     
/*  58 */     this.quat[0] = x;
/*  59 */     this.quat[1] = y;
/*  60 */     this.quat[2] = z;
/*  61 */     this.quat[3] = s;
/*     */     
/*  63 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Quaternion fromAngles(float[] angles) {
/*  71 */     return fromAngles(angles[0], angles[1], angles[2]);
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
/*     */   public final Quaternion fromAngles(float x, float y, float z) {
/*  87 */     float angle = z * 0.5F;
/*  88 */     double sy = Math.sin(angle);
/*  89 */     double cy = Math.cos(angle);
/*  90 */     angle = y * 0.5F;
/*  91 */     double sp = Math.sin(angle);
/*  92 */     double cp = Math.cos(angle);
/*  93 */     angle = x * 0.5F;
/*  94 */     double sr = Math.sin(angle);
/*  95 */     double cr = Math.cos(angle);
/*     */     
/*  97 */     double crcp = cr * cp;
/*  98 */     double srsp = sr * sp;
/*     */     
/* 100 */     this.quat[0] = (float)(sr * cp * cy - cr * sp * sy);
/* 101 */     this.quat[1] = (float)(cr * sp * cy + sr * cp * sy);
/* 102 */     this.quat[2] = (float)(crcp * sy - srsp * cy);
/* 103 */     this.quat[3] = (float)(crcp * cy + srsp * sy);
/*     */     
/* 105 */     return this;
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
/*     */   public final Quaternion fromAxisAngle(Vector3f axis, float angle) {
/* 117 */     float halfangle = 0.5F * angle;
/* 118 */     float sinval = (float)Math.sin(halfangle);
/*     */     
/* 120 */     this.quat[0] = sinval * axis.x;
/* 121 */     this.quat[1] = sinval * axis.y;
/* 122 */     this.quat[2] = sinval * axis.z;
/* 123 */     this.quat[3] = (float)Math.cos(halfangle);
/*     */     
/* 125 */     return normalize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void slerp(Quaternion q1, Quaternion q2, float interp) {
/* 132 */     float a = 0.0F;
/* 133 */     float b = 0.0F; int i;
/* 134 */     for (i = 0; i < 4; i++) {
/*     */       
/* 136 */       a += (q1.quat[i] - q2.quat[i]) * (q1.quat[i] - q2.quat[i]);
/* 137 */       b += (q1.quat[i] + q2.quat[i]) * (q1.quat[i] + q2.quat[i]);
/*     */     } 
/* 139 */     if (a > b) {
/* 140 */       q2.negate();
/*     */     }
/* 142 */     float cosom = q1.quat[0] * q2.quat[0] + q1.quat[1] * q2.quat[1] + q1.quat[2] * q2.quat[2] + q1.quat[3] * q2.quat[3];
/*     */ 
/*     */ 
/*     */     
/* 146 */     if (1.0D + cosom > 1.0E-8D) {
/*     */       double sclq1; double sclq2;
/* 148 */       if (1.0D - cosom > 1.0E-8D) {
/*     */         
/* 150 */         double omega = Math.acos(cosom);
/* 151 */         double sinom = Math.sin(omega);
/* 152 */         sclq1 = Math.sin((1.0D - interp) * omega) / sinom;
/* 153 */         sclq2 = Math.sin(interp * omega) / sinom;
/*     */       }
/*     */       else {
/*     */         
/* 157 */         sclq1 = 1.0D - interp;
/* 158 */         sclq2 = interp;
/*     */       } 
/* 160 */       for (i = 0; i < 4; i++) {
/* 161 */         this.quat[i] = (float)(sclq1 * q1.quat[i] + sclq2 * q2.quat[i]);
/*     */       }
/*     */     } else {
/*     */       
/* 165 */       this.quat[0] = -q1.quat[1];
/* 166 */       this.quat[1] = q1.quat[0];
/* 167 */       this.quat[2] = -q1.quat[3];
/* 168 */       this.quat[3] = q1.quat[2];
/*     */       
/* 170 */       double sclq1 = Math.sin((1.0D - interp) * 0.5D * Math.PI);
/* 171 */       double sclq2 = Math.sin(interp * 0.5D * Math.PI);
/* 172 */       for (i = 0; i < 3; i++) {
/* 173 */         this.quat[i] = (float)(sclq1 * q1.quat[i] + sclq2 * this.quat[i]);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void negate() {
/* 179 */     this.quat[0] = -this.quat[0];
/* 180 */     this.quat[1] = -this.quat[1];
/* 181 */     this.quat[2] = -this.quat[2];
/* 182 */     this.quat[3] = -this.quat[3];
/*     */   }
/*     */ 
/*     */   
/*     */   public void conjugate() {
/* 187 */     this.quat[0] = -this.quat[0];
/* 188 */     this.quat[1] = -this.quat[1];
/* 189 */     this.quat[2] = -this.quat[2];
/* 190 */     this.quat[3] = this.quat[3];
/*     */   }
/*     */ 
/*     */   
/*     */   public final void identity() {
/* 195 */     this.quat[0] = 0.0F;
/* 196 */     this.quat[1] = 0.0F;
/* 197 */     this.quat[2] = 0.0F;
/* 198 */     this.quat[3] = 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Quaternion normalize() {
/* 203 */     float norm = this.quat[0] * this.quat[0] + this.quat[1] * this.quat[1] + this.quat[2] * this.quat[2] + this.quat[3] * this.quat[3];
/* 204 */     float invscale = 1.0F / (float)Math.sqrt(norm);
/* 205 */     this.quat[0] = this.quat[0] * invscale;
/* 206 */     this.quat[1] = this.quat[1] * invscale;
/* 207 */     this.quat[2] = this.quat[2] * invscale;
/* 208 */     this.quat[3] = this.quat[3] * invscale;
/* 209 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Quaternion fromMatrix(Matrix m) {
/* 214 */     float[] mat = m.getMatrix();
/* 215 */     float trace = mat[0] + mat[5] + mat[10];
/*     */ 
/*     */     
/* 218 */     if (trace > 0.0F) {
/*     */       
/* 220 */       float root = (float)Math.sqrt((trace + 1.0F));
/*     */       
/* 222 */       this.quat[3] = 0.5F * root;
/*     */       
/* 224 */       root = 0.5F / root;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 229 */       this.quat[0] = (mat[6] - mat[9]) * root;
/* 230 */       this.quat[1] = (mat[8] - mat[2]) * root;
/* 231 */       this.quat[2] = (mat[1] - mat[4]) * root;
/*     */     }
/*     */     else {
/*     */       
/* 235 */       int i = 0;
/* 236 */       int j = 1;
/* 237 */       int k = 2;
/*     */       
/* 239 */       if (mat[5] > mat[0]) {
/*     */         
/* 241 */         i = 1;
/* 242 */         j = 2;
/* 243 */         k = 0;
/*     */       }
/* 245 */       else if (mat[10] > mat[i + 4 * i]) {
/*     */         
/* 247 */         i = 2;
/* 248 */         j = 0;
/* 249 */         k = 1;
/*     */       } 
/*     */       
/* 252 */       float root = (float)Math.sqrt((mat[i + 4 * i] - mat[j + 4 * j] - mat[k + 4 * k] + 1.0F));
/*     */       
/* 254 */       this.quat[i] = 0.5F * root;
/*     */       
/* 256 */       root = 0.5F / root;
/*     */       
/* 258 */       this.quat[j] = (mat[j + 4 * i] + mat[i + 4 * j]) * root;
/* 259 */       this.quat[k] = (mat[k + 4 * i] + mat[i + 4 * k]) * root;
/* 260 */       this.quat[3] = (mat[k + 4 * j] - mat[j + 4 * k]) * root;
/*     */     } 
/*     */ 
/*     */     
/* 264 */     return normalize();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector3f rotate(Vector3f v, Vector3f result) {
/* 269 */     if (result == null) {
/* 270 */       result = new Vector3f();
/*     */     }
/* 272 */     float v1x = this.quat[1] * v.z - this.quat[2] * v.y + this.quat[3] * v.x;
/* 273 */     float v1y = this.quat[2] * v.x - this.quat[0] * v.z + this.quat[3] * v.y;
/* 274 */     float v1z = this.quat[0] * v.y - this.quat[1] * v.x + this.quat[3] * v.z;
/*     */     
/* 276 */     float dotv = this.quat[0] * v.x + this.quat[1] * v.y + this.quat[2] * v.z;
/*     */     
/* 278 */     result.x = this.quat[0] * dotv + this.quat[3] * v1x - v1y * this.quat[2] - v1z * this.quat[1];
/* 279 */     result.y = this.quat[1] * dotv + this.quat[3] * v1y - v1z * this.quat[0] - v1x * this.quat[2];
/* 280 */     result.z = this.quat[2] * dotv + this.quat[3] * v1z - v1x * this.quat[1] - v1y * this.quat[0];
/*     */     
/* 282 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector rotate(Vector v) {
/* 287 */     float vx = v.x();
/* 288 */     float vy = v.y();
/* 289 */     float vz = v.z();
/*     */     
/* 291 */     float v1x = this.quat[1] * vz - this.quat[2] * vy + this.quat[3] * vx;
/* 292 */     float v1y = this.quat[2] * vx - this.quat[0] * vz + this.quat[3] * vy;
/* 293 */     float v1z = this.quat[0] * vy - this.quat[1] * vx + this.quat[3] * vz;
/*     */     
/* 295 */     float dotv = this.quat[0] * vx + this.quat[1] * vy + this.quat[2] * vz;
/*     */     
/* 297 */     return v.set(this.quat[0] * dotv + this.quat[3] * v1x - v1y * this.quat[2] - v1z * this.quat[1], this.quat[1] * dotv + this.quat[3] * v1y - v1z * this.quat[0] - v1x * this.quat[2], this.quat[2] * dotv + this.quat[3] * v1z - v1x * this.quat[1] - v1y * this.quat[0]);
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
/*     */   public final float[] getQuat() {
/* 310 */     return this.quat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setQuat(float[] quat) {
/* 318 */     this.quat = quat;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void set(Quaternion q) {
/* 323 */     this.quat[0] = q.quat[0];
/* 324 */     this.quat[1] = q.quat[1];
/* 325 */     this.quat[2] = q.quat[2];
/* 326 */     this.quat[3] = q.quat[3];
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\math\Quaternion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */