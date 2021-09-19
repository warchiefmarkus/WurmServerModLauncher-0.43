/*     */ package impl.org.controlsfx.tools.rectangle;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import javafx.geometry.Point2D;
/*     */ import javafx.geometry.Rectangle2D;
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
/*     */ public class CoordinatePositions
/*     */ {
/*     */   public static EnumSet<CoordinatePosition> onRectangleAndEdges(Rectangle2D rectangle, Point2D point, double edgeTolerance) {
/*  54 */     EnumSet<CoordinatePosition> positions = EnumSet.noneOf(CoordinatePosition.class);
/*  55 */     positions.add(inRectangle(rectangle, point));
/*  56 */     positions.add(onEdges(rectangle, point, edgeTolerance));
/*  57 */     return positions;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static CoordinatePosition inRectangle(Rectangle2D rectangle, Point2D point) {
/*  75 */     if (rectangle.contains(point)) {
/*  76 */       return CoordinatePosition.IN_RECTANGLE;
/*     */     }
/*  78 */     return CoordinatePosition.OUT_OF_RECTANGLE;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CoordinatePosition onEdges(Rectangle2D rectangle, Point2D point, double edgeTolerance) {
/* 102 */     CoordinatePosition vertical = closeToVertical(rectangle, point, edgeTolerance);
/* 103 */     CoordinatePosition horizontal = closeToHorizontal(rectangle, point, edgeTolerance);
/*     */     
/* 105 */     return extractSingleCardinalDirection(vertical, horizontal);
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
/*     */ 
/*     */   
/*     */   private static CoordinatePosition closeToVertical(Rectangle2D rectangle, Point2D point, double edgeTolerance) {
/* 122 */     double xDistanceToLeft = Math.abs(point.getX() - rectangle.getMinX());
/* 123 */     double xDistanceToRight = Math.abs(point.getX() - rectangle.getMaxX());
/* 124 */     boolean xCloseToLeft = (xDistanceToLeft < edgeTolerance && xDistanceToLeft < xDistanceToRight);
/* 125 */     boolean xCloseToRight = (xDistanceToRight < edgeTolerance && xDistanceToRight < xDistanceToLeft);
/*     */     
/* 127 */     if (!xCloseToLeft && !xCloseToRight) {
/* 128 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 132 */     boolean yCloseToVertical = (rectangle.getMinY() - edgeTolerance < point.getY() && point.getY() < rectangle.getMaxY() + edgeTolerance);
/* 133 */     if (yCloseToVertical) {
/* 134 */       if (xCloseToLeft) {
/* 135 */         return CoordinatePosition.WEST_EDGE;
/*     */       }
/* 137 */       if (xCloseToRight) {
/* 138 */         return CoordinatePosition.EAST_EDGE;
/*     */       }
/*     */     } 
/*     */     
/* 142 */     return null;
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
/*     */ 
/*     */   
/*     */   private static CoordinatePosition closeToHorizontal(Rectangle2D rectangle, Point2D point, double edgeTolerance) {
/* 159 */     double yDistanceToUpper = Math.abs(point.getY() - rectangle.getMinY());
/* 160 */     double yDistanceToLower = Math.abs(point.getY() - rectangle.getMaxY());
/* 161 */     boolean yCloseToUpper = (yDistanceToUpper < edgeTolerance && yDistanceToUpper < yDistanceToLower);
/* 162 */     boolean yCloseToLower = (yDistanceToLower < edgeTolerance && yDistanceToLower < yDistanceToUpper);
/*     */     
/* 164 */     if (!yCloseToUpper && !yCloseToLower) {
/* 165 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 169 */     boolean xCloseToHorizontal = (rectangle.getMinX() - edgeTolerance < point.getX() && point.getX() < rectangle.getMaxX() + edgeTolerance);
/* 170 */     if (xCloseToHorizontal) {
/* 171 */       if (yCloseToUpper) {
/* 172 */         return CoordinatePosition.NORTH_EDGE;
/*     */       }
/* 174 */       if (yCloseToLower) {
/* 175 */         return CoordinatePosition.SOUTH_EDGE;
/*     */       }
/*     */     } 
/*     */     
/* 179 */     return null;
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
/*     */   
/*     */   private static CoordinatePosition extractSingleCardinalDirection(CoordinatePosition vertical, CoordinatePosition horizontal) {
/* 195 */     if (vertical == null) {
/* 196 */       return horizontal;
/*     */     }
/*     */     
/* 199 */     if (horizontal == null) {
/* 200 */       return vertical;
/*     */     }
/*     */ 
/*     */     
/* 204 */     if (horizontal == CoordinatePosition.NORTH_EDGE && vertical == CoordinatePosition.EAST_EDGE) {
/* 205 */       return CoordinatePosition.NORTHEAST_EDGE;
/*     */     }
/* 207 */     if (horizontal == CoordinatePosition.NORTH_EDGE && vertical == CoordinatePosition.WEST_EDGE) {
/* 208 */       return CoordinatePosition.NORTHWEST_EDGE;
/*     */     }
/*     */ 
/*     */     
/* 212 */     if (horizontal == CoordinatePosition.SOUTH_EDGE && vertical == CoordinatePosition.EAST_EDGE) {
/* 213 */       return CoordinatePosition.SOUTHEAST_EDGE;
/*     */     }
/* 215 */     if (horizontal == CoordinatePosition.SOUTH_EDGE && vertical == CoordinatePosition.WEST_EDGE) {
/* 216 */       return CoordinatePosition.SOUTHWEST_EDGE;
/*     */     }
/*     */     
/* 219 */     throw new IllegalArgumentException();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\tools\rectangle\CoordinatePositions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */