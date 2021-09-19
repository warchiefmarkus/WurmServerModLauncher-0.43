/*     */ package impl.org.controlsfx.tools.rectangle.change;
/*     */ 
/*     */ import impl.org.controlsfx.tools.rectangle.Rectangles2D;
/*     */ import java.util.Objects;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractFixedPointChangeStrategy
/*     */   extends AbstractRatioRespectingChangeStrategy
/*     */ {
/*     */   private final Rectangle2D bounds;
/*     */   private Point2D fixedCorner;
/*     */   
/*     */   protected AbstractFixedPointChangeStrategy(boolean ratioFixed, double ratio, Rectangle2D bounds) {
/*  72 */     super(ratioFixed, ratio);
/*  73 */     Objects.requireNonNull(bounds, "The argument 'bounds' must not be null.");
/*     */     
/*  75 */     this.bounds = bounds;
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
/*     */   protected abstract Point2D getFixedCorner();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Rectangle2D createFromCorners(Point2D point) {
/*  97 */     Point2D pointInBounds = Rectangles2D.inRectangle(this.bounds, point);
/*     */     
/*  99 */     if (isRatioFixed()) {
/* 100 */       return Rectangles2D.forDiagonalCornersAndRatio(this.fixedCorner, pointInBounds, getRatio());
/*     */     }
/* 102 */     return Rectangles2D.forDiagonalCorners(this.fixedCorner, pointInBounds);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Rectangle2D doBegin(Point2D point) {
/* 111 */     boolean startPointNotInBounds = !this.bounds.contains(point);
/* 112 */     if (startPointNotInBounds) {
/* 113 */       throw new IllegalArgumentException("The change's start point (" + point + ") must lie within the bounds (" + this.bounds + ").");
/*     */     }
/*     */ 
/*     */     
/* 117 */     this.fixedCorner = getFixedCorner();
/* 118 */     return createFromCorners(point);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Rectangle2D doContinue(Point2D point) {
/* 126 */     return createFromCorners(point);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Rectangle2D doEnd(Point2D point) {
/* 134 */     Rectangle2D newRectangle = createFromCorners(point);
/* 135 */     this.fixedCorner = null;
/* 136 */     return newRectangle;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\tools\rectangle\change\AbstractFixedPointChangeStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */