/*     */ package impl.org.controlsfx.tools.rectangle;
/*     */ 
/*     */ import impl.org.controlsfx.tools.MathTools;
/*     */ import java.util.Objects;
/*     */ import javafx.geometry.Bounds;
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
/*     */ 
/*     */ 
/*     */ public class Rectangles2D
/*     */ {
/*     */   public static boolean contains(Rectangle2D rectangle, Edge2D edge) {
/*  58 */     Objects.requireNonNull(rectangle, "The argument 'rectangle' must not be null.");
/*  59 */     Objects.requireNonNull(edge, "The argument 'edge' must not be null.");
/*     */     
/*  61 */     boolean edgeInBounds = (rectangle.contains(edge.getUpperLeft()) && rectangle.contains(edge.getLowerRight()));
/*  62 */     return edgeInBounds;
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
/*     */   public static Point2D inRectangle(Rectangle2D rectangle, Point2D point) {
/*  81 */     Objects.requireNonNull(rectangle, "The argument 'rectangle' must not be null.");
/*  82 */     Objects.requireNonNull(point, "The argument 'point' must not be null.");
/*     */     
/*  84 */     if (rectangle.contains(point)) {
/*  85 */       return point;
/*     */     }
/*     */ 
/*     */     
/*  89 */     double newX = MathTools.inInterval(rectangle.getMinX(), point.getX(), rectangle.getMaxX());
/*  90 */     double newY = MathTools.inInterval(rectangle.getMinY(), point.getY(), rectangle.getMaxY());
/*  91 */     return new Point2D(newX, newY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Point2D getCenterPoint(Rectangle2D rectangle) {
/* 102 */     Objects.requireNonNull(rectangle, "The argument 'rectangle' must not be null.");
/*     */     
/* 104 */     double centerX = (rectangle.getMinX() + rectangle.getMaxX()) / 2.0D;
/* 105 */     double centerY = (rectangle.getMinY() + rectangle.getMaxY()) / 2.0D;
/* 106 */     return new Point2D(centerX, centerY);
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
/*     */   public static Rectangle2D intersection(Rectangle2D a, Rectangle2D b) {
/* 124 */     Objects.requireNonNull(a, "The argument 'a' must not be null.");
/* 125 */     Objects.requireNonNull(b, "The argument 'b' must not be null.");
/*     */     
/* 127 */     if (a.intersects(b)) {
/* 128 */       double intersectionMinX = Math.max(a.getMinX(), b.getMinX());
/* 129 */       double intersectionMaxX = Math.min(a.getMaxX(), b.getMaxX());
/* 130 */       double intersectionWidth = intersectionMaxX - intersectionMinX;
/* 131 */       double intersectionMinY = Math.max(a.getMinY(), b.getMinY());
/* 132 */       double intersectionMaxY = Math.min(a.getMaxY(), b.getMaxY());
/* 133 */       double intersectionHeight = intersectionMaxY - intersectionMinY;
/* 134 */       return new Rectangle2D(intersectionMinX, intersectionMinY, intersectionWidth, intersectionHeight);
/*     */     } 
/* 136 */     return Rectangle2D.EMPTY;
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
/*     */   public static Rectangle2D forDiagonalCorners(Point2D oneCorner, Point2D diagonalCorner) {
/* 155 */     Objects.requireNonNull(oneCorner, "The specified corner must not be null.");
/* 156 */     Objects.requireNonNull(diagonalCorner, "The specified diagonal corner must not be null.");
/*     */     
/* 158 */     double minX = Math.min(oneCorner.getX(), diagonalCorner.getX());
/* 159 */     double minY = Math.min(oneCorner.getY(), diagonalCorner.getY());
/* 160 */     double width = Math.abs(oneCorner.getX() - diagonalCorner.getX());
/* 161 */     double height = Math.abs(oneCorner.getY() - diagonalCorner.getY());
/*     */     
/* 163 */     return new Rectangle2D(minX, minY, width, height);
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
/*     */   public static Rectangle2D forUpperLeftCornerAndSize(Point2D upperLeft, double width, double height) {
/* 183 */     return new Rectangle2D(upperLeft.getX(), upperLeft.getY(), width, height);
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
/*     */   public static Rectangle2D forDiagonalCornersAndRatio(Point2D fixedCorner, Point2D diagonalCorner, double ratio) {
/* 206 */     Objects.requireNonNull(fixedCorner, "The specified fixed corner must not be null.");
/* 207 */     Objects.requireNonNull(diagonalCorner, "The specified diagonal corner must not be null.");
/* 208 */     if (ratio < 0.0D) {
/* 209 */       throw new IllegalArgumentException("The specified ratio " + ratio + " must be larger than zero.");
/*     */     }
/*     */ 
/*     */     
/* 213 */     double xDifference = diagonalCorner.getX() - fixedCorner.getX();
/* 214 */     double yDifference = diagonalCorner.getY() - fixedCorner.getY();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 219 */     double xDifferenceByRatio = correctCoordinateDifferenceByRatio(xDifference, yDifference, ratio);
/* 220 */     double yDifferenceByRatio = correctCoordinateDifferenceByRatio(yDifference, xDifference, 1.0D / ratio);
/*     */ 
/*     */     
/* 223 */     double minX = getMinCoordinate(fixedCorner.getX(), xDifferenceByRatio);
/* 224 */     double minY = getMinCoordinate(fixedCorner.getY(), yDifferenceByRatio);
/*     */     
/* 226 */     double width = Math.abs(xDifferenceByRatio);
/* 227 */     double height = Math.abs(yDifferenceByRatio);
/*     */     
/* 229 */     return new Rectangle2D(minX, minY, width, height);
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
/*     */   private static double correctCoordinateDifferenceByRatio(double difference, double otherDifference, double ratioAsMultiplier) {
/* 249 */     double differenceByRatio = otherDifference * ratioAsMultiplier;
/* 250 */     double correctedDistance = Math.min(Math.abs(difference), Math.abs(differenceByRatio));
/*     */     
/* 252 */     return correctedDistance * Math.signum(difference);
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
/*     */   private static double getMinCoordinate(double fixedCoordinate, double difference) {
/* 267 */     if (difference < 0.0D) {
/* 268 */       return fixedCoordinate + difference;
/*     */     }
/*     */     
/* 271 */     return fixedCoordinate;
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
/*     */   public static Rectangle2D forCenterAndSize(Point2D centerPoint, double width, double height) {
/* 290 */     Objects.requireNonNull(centerPoint, "The specified center point must not be null.");
/*     */     
/* 292 */     double absoluteWidth = Math.abs(width);
/* 293 */     double absoluteHeight = Math.abs(height);
/* 294 */     double minX = centerPoint.getX() - absoluteWidth / 2.0D;
/* 295 */     double minY = centerPoint.getY() - absoluteHeight / 2.0D;
/*     */     
/* 297 */     return new Rectangle2D(minX, minY, width, height);
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
/*     */   public static Rectangle2D fixRatio(Rectangle2D original, double ratio) {
/* 318 */     Objects.requireNonNull(original, "The specified original rectangle must not be null.");
/* 319 */     if (ratio < 0.0D) {
/* 320 */       throw new IllegalArgumentException("The specified ratio " + ratio + " must be larger than zero.");
/*     */     }
/*     */     
/* 323 */     return createWithFixedRatioWithinBounds(original, ratio, null);
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
/*     */   public static Rectangle2D fixRatioWithinBounds(Rectangle2D original, double ratio, Rectangle2D bounds) {
/* 345 */     Objects.requireNonNull(original, "The specified original rectangle must not be null.");
/* 346 */     Objects.requireNonNull(bounds, "The specified bounds for the new rectangle must not be null.");
/* 347 */     if (ratio < 0.0D) {
/* 348 */       throw new IllegalArgumentException("The specified ratio " + ratio + " must be larger than zero.");
/*     */     }
/*     */     
/* 351 */     return createWithFixedRatioWithinBounds(original, ratio, bounds);
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
/*     */   private static Rectangle2D createWithFixedRatioWithinBounds(Rectangle2D original, double ratio, Rectangle2D bounds) {
/* 371 */     Point2D centerPoint = getCenterPoint(original);
/*     */     
/* 373 */     boolean centerPointInBounds = (bounds == null || bounds.contains(centerPoint));
/* 374 */     if (!centerPointInBounds) {
/* 375 */       throw new IllegalArgumentException("The center point " + centerPoint + " of the original rectangle is out of the specified bounds.");
/*     */     }
/*     */ 
/*     */     
/* 379 */     double area = original.getWidth() * original.getHeight();
/*     */     
/* 381 */     return createForCenterAreaAndRatioWithinBounds(centerPoint, area, ratio, bounds);
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
/*     */   public static Rectangle2D forCenterAndAreaAndRatio(Point2D centerPoint, double area, double ratio) {
/* 402 */     Objects.requireNonNull(centerPoint, "The specified center point of the new rectangle must not be null.");
/* 403 */     if (area < 0.0D) {
/* 404 */       throw new IllegalArgumentException("The specified area " + area + " must be larger than zero.");
/*     */     }
/* 406 */     if (ratio < 0.0D) {
/* 407 */       throw new IllegalArgumentException("The specified ratio " + ratio + " must be larger than zero.");
/*     */     }
/*     */     
/* 410 */     return createForCenterAreaAndRatioWithinBounds(centerPoint, area, ratio, null);
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
/*     */   public static Rectangle2D forCenterAndAreaAndRatioWithinBounds(Point2D centerPoint, double area, double ratio, Rectangle2D bounds) {
/* 434 */     Objects.requireNonNull(centerPoint, "The specified center point of the new rectangle must not be null.");
/* 435 */     Objects.requireNonNull(bounds, "The specified bounds for the new rectangle must not be null.");
/* 436 */     boolean centerPointInBounds = bounds.contains(centerPoint);
/* 437 */     if (!centerPointInBounds) {
/* 438 */       throw new IllegalArgumentException("The center point " + centerPoint + " of the original rectangle is out of the specified bounds.");
/*     */     }
/*     */     
/* 441 */     if (area < 0.0D) {
/* 442 */       throw new IllegalArgumentException("The specified area " + area + " must be larger than zero.");
/*     */     }
/* 444 */     if (ratio < 0.0D) {
/* 445 */       throw new IllegalArgumentException("The specified ratio " + ratio + " must be larger than zero.");
/*     */     }
/*     */     
/* 448 */     return createForCenterAreaAndRatioWithinBounds(centerPoint, area, ratio, bounds);
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
/*     */   private static Rectangle2D createForCenterAreaAndRatioWithinBounds(Point2D centerPoint, double area, double ratio, Rectangle2D bounds) {
/* 472 */     double newWidth = Math.sqrt(area * ratio);
/* 473 */     double newHeight = area / newWidth;
/*     */     
/* 475 */     boolean boundsSpecified = (bounds != null);
/* 476 */     if (boundsSpecified) {
/* 477 */       double reductionFactor = lengthReductionToStayWithinBounds(centerPoint, newWidth, newHeight, bounds);
/* 478 */       newWidth *= reductionFactor;
/* 479 */       newHeight *= reductionFactor;
/*     */     } 
/*     */     
/* 482 */     return forCenterAndSize(centerPoint, newWidth, newHeight);
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
/*     */   private static double lengthReductionToStayWithinBounds(Point2D centerPoint, double width, double height, Rectangle2D bounds) {
/* 506 */     Objects.requireNonNull(centerPoint, "The specified center point of the new rectangle must not be null.");
/* 507 */     Objects.requireNonNull(bounds, "The specified bounds for the new rectangle must not be null.");
/* 508 */     boolean centerPointInBounds = bounds.contains(centerPoint);
/* 509 */     if (!centerPointInBounds) {
/* 510 */       throw new IllegalArgumentException("The center point " + centerPoint + " of the original rectangle is out of the specified bounds.");
/*     */     }
/*     */     
/* 513 */     if (width < 0.0D) {
/* 514 */       throw new IllegalArgumentException("The specified width " + width + " must be larger than zero.");
/*     */     }
/* 516 */     if (height < 0.0D) {
/* 517 */       throw new IllegalArgumentException("The specified height " + height + " must be larger than zero.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 526 */     double distanceToEast = Math.abs(centerPoint.getX() - bounds.getMinX());
/* 527 */     double distanceToWest = Math.abs(centerPoint.getX() - bounds.getMaxX());
/* 528 */     double distanceToNorth = Math.abs(centerPoint.getY() - bounds.getMinY());
/* 529 */     double distanceToSouth = Math.abs(centerPoint.getY() - bounds.getMaxY());
/*     */ 
/*     */     
/* 532 */     return MathTools.min(new double[] { 1.0D, distanceToEast / width * 2.0D, distanceToWest / width * 2.0D, distanceToNorth / height * 2.0D, distanceToSouth / height * 2.0D });
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
/*     */   public static Rectangle2D forEdgeAndOpposingPoint(Edge2D edge, Point2D point) {
/* 552 */     double otherDimension = edge.getOrthogonalDifference(point);
/* 553 */     return createForEdgeAndOtherDimension(edge, otherDimension);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Rectangle2D forEdgeAndOpposingPointAndRatioWithinBounds(Edge2D edge, Point2D point, double ratio, Rectangle2D bounds) {
/* 583 */     Objects.requireNonNull(edge, "The specified edge must not be null.");
/* 584 */     Objects.requireNonNull(point, "The specified point must not be null.");
/* 585 */     Objects.requireNonNull(bounds, "The specified bounds must not be null.");
/*     */     
/* 587 */     boolean edgeInBounds = contains(bounds, edge);
/* 588 */     if (!edgeInBounds) {
/* 589 */       throw new IllegalArgumentException("The specified edge " + edge + " is not entirely contained on the specified bounds.");
/*     */     }
/*     */     
/* 592 */     if (ratio < 0.0D) {
/* 593 */       throw new IllegalArgumentException("The specified ratio " + ratio + " must be larger than zero.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 603 */     Point2D boundedPoint = movePointIntoBounds(point, bounds);
/* 604 */     Edge2D unboundedEdge = resizeEdgeForDistanceAndRatio(edge, boundedPoint, ratio);
/* 605 */     Edge2D boundedEdge = resizeEdgeForBounds(unboundedEdge, bounds);
/*     */ 
/*     */ 
/*     */     
/* 609 */     double otherDimension = Math.signum(boundedEdge.getOrthogonalDifference(boundedPoint));
/* 610 */     if (boundedEdge.isHorizontal()) {
/*     */       
/* 612 */       otherDimension *= boundedEdge.getLength() / ratio;
/*     */     } else {
/*     */       
/* 615 */       otherDimension *= boundedEdge.getLength() * ratio;
/*     */     } 
/*     */     
/* 618 */     return createForEdgeAndOtherDimension(boundedEdge, otherDimension);
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
/*     */   private static Point2D movePointIntoBounds(Point2D point, Rectangle2D bounds) {
/* 633 */     if (bounds.contains(point)) {
/* 634 */       return point;
/*     */     }
/* 636 */     double boundedPointX = MathTools.inInterval(bounds.getMinX(), point.getX(), bounds.getMaxX());
/* 637 */     double boundedPointY = MathTools.inInterval(bounds.getMinY(), point.getY(), bounds.getMaxY());
/* 638 */     return new Point2D(boundedPointX, boundedPointY);
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
/*     */   private static Edge2D resizeEdgeForDistanceAndRatio(Edge2D edge, Point2D point, double ratio) {
/* 655 */     double distance = Math.abs(edge.getOrthogonalDifference(point));
/* 656 */     if (edge.isHorizontal()) {
/*     */       
/* 658 */       double xLength = distance * ratio;
/* 659 */       return new Edge2D(edge.getCenterPoint(), edge.getOrientation(), xLength);
/*     */     } 
/*     */     
/* 662 */     double yLength = distance / ratio;
/* 663 */     return new Edge2D(edge.getCenterPoint(), edge.getOrientation(), yLength);
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
/*     */   private static Edge2D resizeEdgeForBounds(Edge2D edge, Rectangle2D bounds) {
/* 682 */     boolean edgeInBounds = contains(bounds, edge);
/* 683 */     if (edgeInBounds) {
/* 684 */       return edge;
/*     */     }
/*     */ 
/*     */     
/* 688 */     boolean centerPointInBounds = bounds.contains(edge.getCenterPoint());
/* 689 */     if (!centerPointInBounds) {
/* 690 */       throw new IllegalArgumentException("The specified edge's center point (" + edge + ") is out of the specified bounds (" + bounds + ").");
/*     */     }
/*     */ 
/*     */     
/* 694 */     if (edge.isHorizontal()) {
/*     */       
/* 696 */       double leftPartLengthBound = Math.abs(bounds.getMinX() - edge.getCenterPoint().getX());
/* 697 */       double rightPartLengthBound = Math.abs(bounds.getMaxX() - edge.getCenterPoint().getX());
/*     */       
/* 699 */       double leftPartLength = MathTools.inInterval(0.0D, edge.getLength() / 2.0D, leftPartLengthBound);
/* 700 */       double rightPartLength = MathTools.inInterval(0.0D, edge.getLength() / 2.0D, rightPartLengthBound);
/*     */       
/* 702 */       double horizontalLength = Math.min(leftPartLength, rightPartLength) * 2.0D;
/* 703 */       return new Edge2D(edge.getCenterPoint(), edge.getOrientation(), horizontalLength);
/*     */     } 
/*     */     
/* 706 */     double lowerPartLengthBound = Math.abs(bounds.getMinY() - edge.getCenterPoint().getY());
/* 707 */     double upperPartLengthBound = Math.abs(bounds.getMaxY() - edge.getCenterPoint().getY());
/*     */     
/* 709 */     double lowerPartLength = MathTools.inInterval(0.0D, edge.getLength() / 2.0D, lowerPartLengthBound);
/* 710 */     double upperPartLength = MathTools.inInterval(0.0D, edge.getLength() / 2.0D, upperPartLengthBound);
/*     */     
/* 712 */     double verticalLength = Math.min(lowerPartLength, upperPartLength) * 2.0D;
/* 713 */     return new Edge2D(edge.getCenterPoint(), edge.getOrientation(), verticalLength);
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
/*     */   private static Rectangle2D createForEdgeAndOtherDimension(Edge2D edge, double otherDimension) {
/* 730 */     if (edge.isHorizontal()) {
/* 731 */       return createForHorizontalEdgeAndHeight(edge, otherDimension);
/*     */     }
/* 733 */     return createForVerticalEdgeAndWidth(edge, otherDimension);
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
/*     */   private static Rectangle2D createForHorizontalEdgeAndHeight(Edge2D horizontalEdge, double height) {
/* 748 */     Point2D leftEdgeEndPoint = horizontalEdge.getUpperLeft();
/* 749 */     double upperLeftX = leftEdgeEndPoint.getX();
/*     */     
/* 751 */     double upperLeftY = leftEdgeEndPoint.getY() + Math.min(0.0D, height);
/*     */     
/* 753 */     double absoluteWidth = Math.abs(horizontalEdge.getLength());
/* 754 */     double absoluteHeight = Math.abs(height);
/*     */     
/* 756 */     return new Rectangle2D(upperLeftX, upperLeftY, absoluteWidth, absoluteHeight);
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
/*     */   private static Rectangle2D createForVerticalEdgeAndWidth(Edge2D verticalEdge, double width) {
/* 770 */     Point2D upperEdgeEndPoint = verticalEdge.getUpperLeft();
/*     */     
/* 772 */     double upperLeftX = upperEdgeEndPoint.getX() + Math.min(0.0D, width);
/* 773 */     double upperLeftY = upperEdgeEndPoint.getY();
/*     */     
/* 775 */     double absoluteWidth = Math.abs(width);
/* 776 */     double absoluteHeight = Math.abs(verticalEdge.getLength());
/*     */     
/* 778 */     return new Rectangle2D(upperLeftX, upperLeftY, absoluteWidth, absoluteHeight);
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
/*     */   public static Rectangle2D fromBounds(Bounds bounds) {
/* 793 */     return new Rectangle2D(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\tools\rectangle\Rectangles2D.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */