/*     */ package impl.org.controlsfx.tools.rectangle.change;
/*     */ 
/*     */ import impl.org.controlsfx.tools.rectangle.Edge2D;
/*     */ import impl.org.controlsfx.tools.rectangle.Rectangles2D;
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
/*     */ abstract class AbstractFixedEdgeChangeStrategy
/*     */   extends AbstractRatioRespectingChangeStrategy
/*     */ {
/*     */   private final Rectangle2D bounds;
/*     */   private Edge2D fixedEdge;
/*     */   
/*     */   protected AbstractFixedEdgeChangeStrategy(boolean ratioFixed, double ratio, Rectangle2D bounds) {
/*  70 */     super(ratioFixed, ratio);
/*  71 */     this.bounds = bounds;
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
/*     */   protected abstract Edge2D getFixedEdge();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Rectangle2D createFromEdges(Point2D point) {
/*  94 */     Point2D pointInBounds = Rectangles2D.inRectangle(this.bounds, point);
/*     */     
/*  96 */     if (isRatioFixed()) {
/*  97 */       return Rectangles2D.forEdgeAndOpposingPointAndRatioWithinBounds(this.fixedEdge, pointInBounds, 
/*  98 */           getRatio(), this.bounds);
/*     */     }
/* 100 */     return Rectangles2D.forEdgeAndOpposingPoint(this.fixedEdge, pointInBounds);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Rectangle2D doBegin(Point2D point) {
/* 109 */     boolean startPointNotInBounds = !this.bounds.contains(point);
/* 110 */     if (startPointNotInBounds) {
/* 111 */       throw new IllegalArgumentException("The change's start point (" + point + ") must lie within the bounds (" + this.bounds + ").");
/*     */     }
/*     */ 
/*     */     
/* 115 */     this.fixedEdge = getFixedEdge();
/* 116 */     return createFromEdges(point);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Rectangle2D doContinue(Point2D point) {
/* 124 */     return createFromEdges(point);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Rectangle2D doEnd(Point2D point) {
/* 132 */     Rectangle2D newRectangle = createFromEdges(point);
/* 133 */     this.fixedEdge = null;
/* 134 */     return newRectangle;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\tools\rectangle\change\AbstractFixedEdgeChangeStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */