/*     */ package com.wurmonline.server.creatures;
/*     */ 
/*     */ import com.wurmonline.math.Vector2f;
/*     */ import com.wurmonline.math.Vector3f;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class BoundBox
/*     */ {
/*     */   public BoxMatrix M;
/*     */   public Vector3f extent;
/*     */   
/*     */   public BoundBox() {
/*  36 */     this.M = new BoxMatrix(true);
/*  37 */     this.extent = new Vector3f(0.0F, 0.0F, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public BoundBox(BoxMatrix m, Vector3f e) {
/*  42 */     set(m, e);
/*     */   }
/*     */ 
/*     */   
/*     */   public BoundBox(BoxMatrix m, Vector3f bl, Vector3f bh) {
/*  47 */     set(m, bl, bh);
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(BoxMatrix m, Vector3f e) {
/*  52 */     this.M = m;
/*  53 */     this.extent = e;
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(BoxMatrix m, Vector3f bl, Vector3f bh) {
/*  58 */     this.M = m;
/*  59 */     this.M.translate(bh.add(bl).mult(0.5F));
/*  60 */     this.extent = bh.subtract(bl).divide(2.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector3f getSize() {
/*  65 */     return this.extent.mult(2.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector3f getCenterPoint() {
/*  70 */     return this.M.getTranslate();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isPointInBox(Vector3f inP) {
/*  75 */     Vector3f P = this.M.InvertSimple().multiply(inP);
/*     */     
/*  77 */     if (Math.abs(P.x) < this.extent.x && Math.abs(P.y) < this.extent.y)
/*  78 */       return true; 
/*  79 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private final Vector2f getIntersection(Vector3f pp, Vector3f cp, Vector3f s1, Vector3f s2) {
/*  84 */     Vector2f L = new Vector2f(cp.x - pp.x, cp.y - pp.y);
/*  85 */     Vector2f S = new Vector2f(s2.x - s1.x, s2.y - s1.y);
/*     */     
/*  87 */     float dot = L.x * S.y - L.y * S.x;
/*     */     
/*  89 */     if (dot == 0.0F) {
/*  90 */       return null;
/*     */     }
/*  92 */     Vector2f c = new Vector2f(s1.x - pp.x, s1.y - pp.y);
/*  93 */     float t = (c.x * S.y - c.y * S.x) / dot;
/*  94 */     if (t < 0.0F || t > 1.0F) {
/*  95 */       return null;
/*     */     }
/*  97 */     float u = (c.x * L.y - c.y * L.x) / dot;
/*  98 */     if (u < 0.0F || u > 1.0F)
/*  99 */       return null; 
/* 100 */     Vector2f inter = null;
/* 101 */     Vector2f LP = new Vector2f(pp.x, pp.y);
/* 102 */     inter = LP.add(L.mult(t));
/* 103 */     return inter;
/*     */   }
/*     */ 
/*     */   
/*     */   public final float distOutside(Vector3f inP, Vector3f cpoint) {
/* 108 */     BoxMatrix MInv = this.M.InvertSimple();
/* 109 */     Vector3f LB1 = MInv.multiply(inP);
/* 110 */     Vector3f LB2 = MInv.multiply(cpoint);
/*     */     
/* 112 */     List<Vector2f> inters = new ArrayList<>();
/* 113 */     Vector2f ii = getIntersection(LB1, LB2, new Vector3f(-this.extent.x, -this.extent.y, 1.0F), new Vector3f(-this.extent.x, this.extent.y, 1.0F));
/* 114 */     if (ii != null)
/* 115 */       inters.add(ii); 
/* 116 */     ii = getIntersection(LB1, LB2, new Vector3f(-this.extent.x, this.extent.y, 1.0F), new Vector3f(this.extent.x, this.extent.y, 1.0F));
/* 117 */     if (ii != null)
/* 118 */       inters.add(ii); 
/* 119 */     ii = getIntersection(LB1, LB2, new Vector3f(this.extent.x, this.extent.y, 1.0F), new Vector3f(this.extent.x, -this.extent.y, 1.0F));
/* 120 */     if (ii != null)
/* 121 */       inters.add(ii); 
/* 122 */     ii = getIntersection(LB1, LB2, new Vector3f(this.extent.x, -this.extent.y, 1.0F), new Vector3f(-this.extent.x, -this.extent.y, 1.0F));
/* 123 */     if (ii != null) {
/* 124 */       inters.add(ii);
/*     */     }
/* 126 */     if (inters.size() > 0) {
/*     */       
/* 128 */       Vector2f p2 = new Vector2f(LB1.x, LB1.y);
/* 129 */       float minLen = 0.0F;
/* 130 */       for (int i = 0; i < inters.size(); i++) {
/*     */         
/* 132 */         float len = p2.subtract(inters.get(i)).length();
/* 133 */         if (i == 0) {
/*     */           
/* 135 */           minLen = len;
/*     */         }
/* 137 */         else if (len < minLen) {
/*     */           
/* 139 */           minLen = len;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 144 */       return minLen;
/*     */     } 
/*     */ 
/*     */     
/* 148 */     return -1.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Vector3f[] getInvRot() {
/* 154 */     Vector3f[] result = new Vector3f[3];
/* 155 */     result[0] = new Vector3f(this.M.mf[0], this.M.mf[1], this.M.mf[2]);
/* 156 */     result[1] = new Vector3f(this.M.mf[4], this.M.mf[5], this.M.mf[6]);
/* 157 */     result[2] = new Vector3f(this.M.mf[8], this.M.mf[9], this.M.mf[10]);
/* 158 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\BoundBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */