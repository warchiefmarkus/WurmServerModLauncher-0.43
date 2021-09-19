/*     */ package com.wurmonline.mesh;
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
/*     */ public class Node
/*     */ {
/*     */   private boolean render;
/*  23 */   float[] normals = new float[3];
/*     */ 
/*     */   
/*     */   private float height;
/*     */ 
/*     */   
/*     */   private float x;
/*     */ 
/*     */   
/*     */   private float y;
/*     */   
/*     */   private float bbBottom;
/*     */   
/*     */   private float bbHeight;
/*     */   
/*     */   private boolean visible;
/*     */   
/*     */   private byte texture;
/*     */   
/*     */   byte data;
/*     */   
/*     */   private Object object;
/*     */ 
/*     */   
/*     */   boolean isRender() {
/*  48 */     return this.render;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setRender(boolean render) {
/*  56 */     this.render = render;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float[] getNormals() {
/*  64 */     return this.normals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setNormals(float[] normals) {
/*  72 */     this.normals = normals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getHeight() {
/*  80 */     return this.height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setHeight(float height) {
/*  88 */     this.height = height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getX() {
/*  96 */     return this.x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setX(float x) {
/* 104 */     this.x = x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getY() {
/* 112 */     return this.y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setY(float y) {
/* 120 */     this.y = y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getBbBottom() {
/* 128 */     return this.bbBottom;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setBbBottom(float bbBottom) {
/* 136 */     this.bbBottom = bbBottom;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getBbHeight() {
/* 144 */     return this.bbHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setBbHeight(float bbHeight) {
/* 152 */     this.bbHeight = bbHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isVisible() {
/* 160 */     return this.visible;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setVisible(boolean visible) {
/* 168 */     this.visible = visible;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte getTexture() {
/* 176 */     return this.texture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setTexture(byte texture) {
/* 184 */     this.texture = texture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setData(byte data) {
/* 192 */     this.data = data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object getObject() {
/* 200 */     return this.object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setObject(Object object) {
/* 208 */     this.object = object;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\mesh\Node.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */