/*    */ package com.wurmonline.math;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Position3D
/*    */ {
/*    */   float x;
/*    */   float y;
/*    */   float z;
/*    */   
/*    */   public Position3D() {}
/*    */   
/*    */   public Position3D(Vertex v) {
/* 33 */     this.x = v.vertex[0];
/* 34 */     this.y = v.vertex[1];
/* 35 */     this.z = v.vertex[2];
/*    */   }
/*    */ 
/*    */   
/*    */   public Position3D(Position3D p) {
/* 40 */     this.x = p.x;
/* 41 */     this.y = p.y;
/* 42 */     this.z = p.z;
/*    */   }
/*    */ 
/*    */   
/*    */   public Position3D(float x, float y, float z) {
/* 47 */     this.x = x;
/* 48 */     this.y = y;
/* 49 */     this.z = z;
/*    */   }
/*    */ 
/*    */   
/*    */   public void sub(Position3D p) {
/* 54 */     this.x -= p.x;
/* 55 */     this.y -= p.y;
/* 56 */     this.z -= p.z;
/*    */   }
/*    */ 
/*    */   
/*    */   public void sub(Vector v) {
/* 61 */     this.x -= v.vector[0];
/* 62 */     this.y -= v.vector[1];
/* 63 */     this.z -= v.vector[2];
/*    */   }
/*    */ 
/*    */   
/*    */   public void sub(Vertex v1, Vector v2) {
/* 68 */     this.x -= v1.point[0];
/* 69 */     this.y -= v1.point[1];
/* 70 */     this.z -= v1.point[2];
/* 71 */     sub(v2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void set(Position3D p) {
/* 76 */     this.x = p.x;
/* 77 */     this.y = p.y;
/* 78 */     this.z = p.z;
/*    */   }
/*    */ 
/*    */   
/*    */   public void scale(int i) {
/* 83 */     this.x *= i;
/* 84 */     this.y *= i;
/* 85 */     this.z *= i;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\math\Position3D.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */