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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MyVector
/*     */ {
/*  27 */   private float[] mVector = new float[4];
/*     */ 
/*     */   
/*     */   MyVector() {
/*  31 */     reset();
/*     */   }
/*     */ 
/*     */   
/*     */   float[] getVector() {
/*  36 */     return this.mVector;
/*     */   }
/*     */ 
/*     */   
/*     */   MyVector(float[] values) {
/*  41 */     set(values);
/*  42 */     this.mVector[3] = 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   void reset() {
/*  47 */     this.mVector[2] = 0.0F; this.mVector[1] = 0.0F; this.mVector[0] = 0.0F;
/*  48 */     this.mVector[3] = 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   void set(float[] values) {
/*  53 */     this.mVector[0] = values[0];
/*  54 */     this.mVector[1] = values[1];
/*  55 */     this.mVector[2] = values[2];
/*     */   }
/*     */ 
/*     */   
/*     */   void add(MyVector v) {
/*  60 */     this.mVector[0] = this.mVector[0] + v.mVector[0];
/*  61 */     this.mVector[1] = this.mVector[1] + v.mVector[1];
/*  62 */     this.mVector[2] = this.mVector[2] + v.mVector[2];
/*  63 */     this.mVector[3] = this.mVector[3] + v.mVector[3];
/*     */   }
/*     */ 
/*     */   
/*     */   void normalize() {
/*  68 */     float len = length();
/*     */     
/*  70 */     this.mVector[0] = this.mVector[0] / len;
/*  71 */     this.mVector[1] = this.mVector[1] / len;
/*  72 */     this.mVector[2] = this.mVector[2] / len;
/*     */   }
/*     */ 
/*     */   
/*     */   float length() {
/*  77 */     return (float)Math.sqrt((this.mVector[0] * this.mVector[0] + this.mVector[1] * this.mVector[1] + this.mVector[2] * this.mVector[2]));
/*     */   }
/*     */ 
/*     */   
/*     */   void transform(Matrix m) {
/*  82 */     float[] vector = new float[4];
/*     */     
/*  84 */     float[] matrix = m.getMatrix();
/*     */     
/*  86 */     vector[0] = this.mVector[0] * matrix[0] + this.mVector[1] * matrix[4] + this.mVector[2] * matrix[8] + matrix[12];
/*  87 */     vector[1] = this.mVector[0] * matrix[1] + this.mVector[1] * matrix[5] + this.mVector[2] * matrix[9] + matrix[13];
/*  88 */     vector[2] = this.mVector[0] * matrix[2] + this.mVector[1] * matrix[6] + this.mVector[2] * matrix[10] + matrix[14];
/*  89 */     vector[3] = this.mVector[0] * matrix[3] + this.mVector[1] * matrix[7] + this.mVector[2] * matrix[11] + matrix[15];
/*     */     
/*  91 */     this.mVector[0] = vector[0];
/*  92 */     this.mVector[1] = vector[1];
/*  93 */     this.mVector[2] = vector[2];
/*  94 */     this.mVector[3] = vector[3];
/*     */   }
/*     */ 
/*     */   
/*     */   void transform3(Matrix m) {
/*  99 */     float[] vector = new float[3];
/* 100 */     float[] matrix = m.getMatrix();
/*     */     
/* 102 */     vector[0] = this.mVector[0] * matrix[0] + this.mVector[1] * matrix[4] + this.mVector[2] * matrix[8];
/* 103 */     vector[1] = this.mVector[0] * matrix[1] + this.mVector[1] * matrix[5] + this.mVector[2] * matrix[9];
/* 104 */     vector[2] = this.mVector[0] * matrix[2] + this.mVector[1] * matrix[6] + this.mVector[2] * matrix[10];
/*     */     
/* 106 */     this.mVector[0] = vector[0];
/* 107 */     this.mVector[1] = vector[1];
/* 108 */     this.mVector[2] = vector[2];
/* 109 */     this.mVector[3] = 1.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void transform(float[] vector, float[] m_vector, float[] matrix) {
/* 115 */     vector[0] = m_vector[0] * matrix[0] + m_vector[1] * matrix[4] + m_vector[2] * matrix[8] + matrix[12];
/* 116 */     vector[1] = m_vector[0] * matrix[1] + m_vector[1] * matrix[5] + m_vector[2] * matrix[9] + matrix[13];
/* 117 */     vector[2] = m_vector[0] * matrix[2] + m_vector[1] * matrix[6] + m_vector[2] * matrix[10] + matrix[14];
/*     */   }
/*     */ 
/*     */   
/*     */   public static void transform3(float[] vector, float[] m_vector, float[] matrix) {
/* 122 */     vector[0] = m_vector[0] * matrix[0] + m_vector[1] * matrix[4] + m_vector[2] * matrix[8];
/* 123 */     vector[1] = m_vector[0] * matrix[1] + m_vector[1] * matrix[5] + m_vector[2] * matrix[9];
/* 124 */     vector[2] = m_vector[0] * matrix[2] + m_vector[1] * matrix[6] + m_vector[2] * matrix[10];
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\math\MyVector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */