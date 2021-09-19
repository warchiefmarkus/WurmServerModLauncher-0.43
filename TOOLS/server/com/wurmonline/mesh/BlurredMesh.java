/*    */ package com.wurmonline.mesh;
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
/*    */ public final class BlurredMesh
/*    */   extends Mesh
/*    */ {
/*    */   private final Mesh mesh;
/*    */   private final int factor;
/*    */   private Mesh parent;
/*    */   private int factorPow;
/*    */   
/*    */   public BlurredMesh(Mesh mesh, int factor) {
/* 29 */     super(mesh.getWidth() / factor, mesh.getHeight() / factor, mesh.getMeshWidth() * factor);
/* 30 */     this.factor = factor;
/*    */     
/* 32 */     if (factor == 1) {
/* 33 */       this.factorPow = 0;
/* 34 */     } else if (factor == 2) {
/* 35 */       this.factorPow = 1;
/* 36 */     } else if (factor == 4) {
/* 37 */       this.factorPow = 2;
/* 38 */     } else if (factor == 8) {
/* 39 */       this.factorPow = 3;
/* 40 */     } else if (factor == 16) {
/* 41 */       this.factorPow = 4;
/* 42 */     } else if (factor == 32) {
/* 43 */       this.factorPow = 5;
/* 44 */     } else if (factor == 64) {
/* 45 */       this.factorPow = 6;
/* 46 */     } else if (factor == 128) {
/* 47 */       this.factorPow = 7;
/* 48 */     } else if (factor == 256) {
/* 49 */       this.factorPow = 8;
/*    */     } else {
/* 51 */       throw new IllegalArgumentException("Factor has to be 2^n");
/* 52 */     }  this.mesh = mesh;
/* 53 */     this.parent = mesh;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setParent(Mesh parent) {
/* 58 */     this.parent = parent;
/*    */   }
/*    */ 
/*    */   
/*    */   public Mesh getParent() {
/* 63 */     return this.parent;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public float getTextureScale() {
/* 69 */     return this.mesh.getTextureScale() * this.factor;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Node getNode(int x, int y) {
/* 75 */     return this.mesh.getNode(x << this.factorPow, y << this.factorPow);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBlurFactor() {
/* 80 */     return this.factor;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\mesh\BlurredMesh.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */