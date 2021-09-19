/*    */ package com.wurmonline.server;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Point
/*    */ {
/*    */   private int px;
/*    */   private int py;
/*    */   private int ph;
/*    */   
/*    */   public Point(int x, int y) {
/* 37 */     this.px = x;
/* 38 */     this.py = y;
/* 39 */     this.ph = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public Point(int x, int y, int h) {
/* 44 */     this.px = x;
/* 45 */     this.py = y;
/* 46 */     this.ph = h;
/*    */   }
/*    */ 
/*    */   
/*    */   public Point(Point point) {
/* 51 */     this.px = point.px;
/* 52 */     this.py = point.py;
/* 53 */     this.ph = point.ph;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getX() {
/* 58 */     return this.px;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setX(int x) {
/* 63 */     this.px = x;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getY() {
/* 68 */     return this.py;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setY(int y) {
/* 73 */     this.py = y;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getH() {
/* 78 */     return this.ph;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setH(int h) {
/* 83 */     this.ph = h;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setXY(int x, int y) {
/* 88 */     this.px = x;
/* 89 */     this.py = y;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setXYH(int x, int y, int h) {
/* 94 */     this.px = x;
/* 95 */     this.py = y;
/* 96 */     this.ph = h;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\Point.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */