/*     */ package org.seamless.util.math;
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
/*     */ public class Rectangle
/*     */ {
/*     */   private Point position;
/*     */   private int width;
/*     */   private int height;
/*     */   
/*     */   public Rectangle() {}
/*     */   
/*     */   public Rectangle(Point position, int width, int height) {
/*  30 */     this.position = position;
/*  31 */     this.width = width;
/*  32 */     this.height = height;
/*     */   }
/*     */   
/*     */   public void reset() {
/*  36 */     this.position = new Point(0, 0);
/*  37 */     this.width = 0;
/*  38 */     this.height = 0;
/*     */   }
/*     */   
/*     */   public Point getPosition() {
/*  42 */     return this.position;
/*     */   }
/*     */   
/*     */   public void setPosition(Point position) {
/*  46 */     this.position = position;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/*  50 */     return this.width;
/*     */   }
/*     */   
/*     */   public void setWidth(int width) {
/*  54 */     this.width = width;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/*  58 */     return this.height;
/*     */   }
/*     */   
/*     */   public void setHeight(int height) {
/*  62 */     this.height = height;
/*     */   }
/*     */   
/*     */   public Rectangle intersection(Rectangle that) {
/*  66 */     int tx1 = this.position.getX();
/*  67 */     int ty1 = this.position.getY();
/*  68 */     int rx1 = that.position.getX();
/*  69 */     int ry1 = that.position.getY();
/*  70 */     long tx2 = tx1;
/*  71 */     tx2 += this.width;
/*  72 */     long ty2 = ty1;
/*  73 */     ty2 += this.height;
/*  74 */     long rx2 = rx1;
/*  75 */     rx2 += that.width;
/*  76 */     long ry2 = ry1;
/*  77 */     ry2 += that.height;
/*  78 */     if (tx1 < rx1) tx1 = rx1; 
/*  79 */     if (ty1 < ry1) ty1 = ry1; 
/*  80 */     if (tx2 > rx2) tx2 = rx2; 
/*  81 */     if (ty2 > ry2) ty2 = ry2; 
/*  82 */     tx2 -= tx1;
/*  83 */     ty2 -= ty1;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     if (tx2 < -2147483648L) tx2 = -2147483648L; 
/*  89 */     if (ty2 < -2147483648L) ty2 = -2147483648L; 
/*  90 */     return new Rectangle(new Point(tx1, ty1), (int)tx2, (int)ty2);
/*     */   }
/*     */   
/*     */   public boolean isOverlapping(Rectangle that) {
/*  94 */     Rectangle intersection = intersection(that);
/*  95 */     return (intersection.getWidth() > 0 && intersection.getHeight() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 100 */     return "Rectangle(" + this.position + " - " + this.width + "x" + this.height + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\math\Rectangle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */