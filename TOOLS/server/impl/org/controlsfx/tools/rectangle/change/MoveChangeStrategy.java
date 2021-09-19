/*     */ package impl.org.controlsfx.tools.rectangle.change;
/*     */ 
/*     */ import impl.org.controlsfx.tools.MathTools;
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
/*     */ public class MoveChangeStrategy
/*     */   extends AbstractPreviousRectangleChangeStrategy
/*     */ {
/*     */   private final Rectangle2D bounds;
/*     */   private Point2D startingPoint;
/*     */   
/*     */   public MoveChangeStrategy(Rectangle2D previous, Rectangle2D bounds) {
/*  69 */     super(previous, false, 0.0D);
/*  70 */     Objects.requireNonNull(bounds, "The specified bounds must not be null.");
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MoveChangeStrategy(Rectangle2D previous, double maxX, double maxY) {
/*  88 */     super(previous, false, 0.0D);
/*  89 */     if (maxX < previous.getWidth()) {
/*  90 */       throw new IllegalArgumentException("The specified maximal x-coordinate must be greater than or equal to the previous rectangle's width.");
/*     */     }
/*     */     
/*  93 */     if (maxY < previous.getHeight()) {
/*  94 */       throw new IllegalArgumentException("The specified maximal y-coordinate must be greater than or equal to the previous rectangle's height.");
/*     */     }
/*     */ 
/*     */     
/*  98 */     this.bounds = new Rectangle2D(0.0D, 0.0D, maxX, maxY);
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
/*     */   private final Rectangle2D moveRectangleToPoint(Point2D point) {
/* 120 */     double xMove = point.getX() - this.startingPoint.getX();
/* 121 */     double yMove = point.getY() - this.startingPoint.getY();
/*     */ 
/*     */     
/* 124 */     double upperLeftX = getPrevious().getMinX() + xMove;
/* 125 */     double upperLeftY = getPrevious().getMinY() + yMove;
/*     */ 
/*     */     
/* 128 */     double maxX = this.bounds.getMaxX() - getPrevious().getWidth();
/* 129 */     double maxY = this.bounds.getMaxY() - getPrevious().getHeight();
/*     */ 
/*     */     
/* 132 */     double correctedUpperLeftX = MathTools.inInterval(this.bounds.getMinX(), upperLeftX, maxX);
/* 133 */     double correctedUpperLeftY = MathTools.inInterval(this.bounds.getMinY(), upperLeftY, maxY);
/*     */ 
/*     */     
/* 136 */     return new Rectangle2D(correctedUpperLeftX, correctedUpperLeftY, 
/*     */         
/* 138 */         getPrevious().getWidth(), getPrevious().getHeight());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Rectangle2D doBegin(Point2D point) {
/* 146 */     this.startingPoint = point;
/* 147 */     return getPrevious();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Rectangle2D doContinue(Point2D point) {
/* 155 */     return moveRectangleToPoint(point);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Rectangle2D doEnd(Point2D point) {
/* 163 */     return moveRectangleToPoint(point);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\tools\rectangle\change\MoveChangeStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */