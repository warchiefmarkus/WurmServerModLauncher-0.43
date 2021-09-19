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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class CaveNode
/*    */   extends Node
/*    */ {
/*    */   private int special;
/*    */   private float[] normals2;
/*    */   private int ceilingTexture;
/*    */   private float data;
/*    */   
/*    */   int getSpecial() {
/* 35 */     return this.special;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void setSpecial(int special) {
/* 44 */     this.special = special;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   float[] getNormals2() {
/* 52 */     return this.normals2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void setNormals2(float[] normals2) {
/* 61 */     this.normals2 = normals2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   int getCeilingTexture() {
/* 69 */     return this.ceilingTexture;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void setCeilingTexture(int ceilingTexture) {
/* 78 */     this.ceilingTexture = ceilingTexture;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   float getData() {
/* 86 */     return this.data;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void setData(float data) {
/* 95 */     this.data = data;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\mesh\CaveNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */