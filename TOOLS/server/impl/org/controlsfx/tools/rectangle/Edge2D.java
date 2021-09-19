/*     */ package impl.org.controlsfx.tools.rectangle;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import javafx.geometry.Orientation;
/*     */ import javafx.geometry.Point2D;
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
/*     */ public class Edge2D
/*     */ {
/*     */   private final Point2D centerPoint;
/*     */   private final Orientation orientation;
/*     */   private final double length;
/*     */   
/*     */   public Edge2D(Point2D centerPoint, Orientation orientation, double length) {
/*  73 */     Objects.requireNonNull(centerPoint, "The specified center point must not be null.");
/*  74 */     Objects.requireNonNull(orientation, "The specified orientation must not be null.");
/*  75 */     if (length < 0.0D) {
/*  76 */       throw new IllegalArgumentException("The length must not be negative, i.e. zero or a positive value is alowed.");
/*     */     }
/*     */     
/*  79 */     this.centerPoint = centerPoint;
/*  80 */     this.orientation = orientation;
/*  81 */     this.length = length;
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
/*     */   public Point2D getUpperLeft() {
/*  96 */     if (isHorizontal()) {
/*     */       
/*  98 */       double cornersX = this.centerPoint.getX() - this.length / 2.0D;
/*  99 */       double edgesY = this.centerPoint.getY();
/* 100 */       return new Point2D(cornersX, edgesY);
/*     */     } 
/*     */     
/* 103 */     double edgesX = this.centerPoint.getX();
/* 104 */     double cornersY = this.centerPoint.getY() - this.length / 2.0D;
/* 105 */     return new Point2D(edgesX, cornersY);
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
/*     */   public Point2D getLowerRight() {
/* 117 */     if (isHorizontal()) {
/*     */       
/* 119 */       double cornersX = this.centerPoint.getX() + this.length / 2.0D;
/* 120 */       double edgesY = this.centerPoint.getY();
/* 121 */       return new Point2D(cornersX, edgesY);
/*     */     } 
/*     */     
/* 124 */     double edgesX = this.centerPoint.getX();
/* 125 */     double cornersY = this.centerPoint.getY() + this.length / 2.0D;
/* 126 */     return new Point2D(edgesX, cornersY);
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
/*     */   public double getOrthogonalDifference(Point2D otherPoint) {
/* 141 */     Objects.requireNonNull(otherPoint, "The other point must nt be null.");
/*     */     
/* 143 */     if (isHorizontal())
/*     */     {
/* 145 */       return otherPoint.getY() - this.centerPoint.getY();
/*     */     }
/*     */     
/* 148 */     return otherPoint.getX() - this.centerPoint.getX();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Point2D getCenterPoint() {
/* 159 */     return this.centerPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Orientation getOrientation() {
/* 169 */     return this.orientation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isHorizontal() {
/* 178 */     return (this.orientation == Orientation.HORIZONTAL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isVertical() {
/* 187 */     return (this.orientation == Orientation.VERTICAL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getLength() {
/* 194 */     return this.length;
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
/*     */   public int hashCode() {
/* 206 */     int prime = 31;
/* 207 */     int result = 1;
/* 208 */     result = 31 * result + ((this.centerPoint == null) ? 0 : this.centerPoint.hashCode());
/*     */     
/* 210 */     long temp = Double.doubleToLongBits(this.length);
/* 211 */     result = 31 * result + (int)(temp ^ temp >>> 32L);
/* 212 */     result = 31 * result + ((this.orientation == null) ? 0 : this.orientation.hashCode());
/* 213 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 221 */     if (this == obj)
/* 222 */       return true; 
/* 223 */     if (obj == null)
/* 224 */       return false; 
/* 225 */     if (getClass() != obj.getClass())
/* 226 */       return false; 
/* 227 */     Edge2D other = (Edge2D)obj;
/* 228 */     if (this.centerPoint == null) {
/* 229 */       if (other.centerPoint != null)
/* 230 */         return false; 
/* 231 */     } else if (!this.centerPoint.equals(other.centerPoint)) {
/* 232 */       return false;
/* 233 */     }  if (Double.doubleToLongBits(this.length) != Double.doubleToLongBits(other.length))
/* 234 */       return false; 
/* 235 */     if (this.orientation != other.orientation)
/* 236 */       return false; 
/* 237 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 245 */     return "Edge2D [centerX = " + this.centerPoint.getX() + ", centerY = " + this.centerPoint.getY() + ", orientation = " + this.orientation + ", length = " + this.length + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\tools\rectangle\Edge2D.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */