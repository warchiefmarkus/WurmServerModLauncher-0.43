/*    */ package org.seamless.util.math;
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
/*    */ public class Point
/*    */ {
/*    */   private int x;
/*    */   private int y;
/*    */   
/*    */   public Point(int x, int y) {
/* 25 */     this.x = x;
/* 26 */     this.y = y;
/*    */   }
/*    */   
/*    */   public int getX() {
/* 30 */     return this.x;
/*    */   }
/*    */   
/*    */   public int getY() {
/* 34 */     return this.y;
/*    */   }
/*    */   
/*    */   public Point multiply(double by) {
/* 38 */     return new Point((this.x != 0) ? (int)(this.x * by) : 0, (this.y != 0) ? (int)(this.y * by) : 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Point divide(double by) {
/* 45 */     return new Point((this.x != 0) ? (int)(this.x / by) : 0, (this.y != 0) ? (int)(this.y / by) : 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 53 */     if (this == o) return true; 
/* 54 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 56 */     Point point = (Point)o;
/*    */     
/* 58 */     if (this.x != point.x) return false; 
/* 59 */     if (this.y != point.y) return false;
/*    */     
/* 61 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 66 */     int result = this.x;
/* 67 */     result = 31 * result + this.y;
/* 68 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 73 */     return "Point(" + this.x + "/" + this.y + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\math\Point.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */