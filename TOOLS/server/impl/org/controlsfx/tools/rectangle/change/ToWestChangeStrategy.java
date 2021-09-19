/*     */ package impl.org.controlsfx.tools.rectangle.change;
/*     */ 
/*     */ import impl.org.controlsfx.tools.rectangle.Edge2D;
/*     */ import javafx.geometry.Orientation;
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
/*     */ public class ToWestChangeStrategy
/*     */   extends AbstractFixedEdgeChangeStrategy
/*     */ {
/*     */   private final Edge2D easternEdge;
/*     */   
/*     */   public ToWestChangeStrategy(Rectangle2D original, boolean ratioFixed, double ratio, Rectangle2D bounds) {
/*  68 */     super(ratioFixed, ratio, bounds);
/*  69 */     Point2D edgeCenterPoint = new Point2D(original.getMaxX(), (original.getMinY() + original.getMaxY()) / 2.0D);
/*  70 */     this.easternEdge = new Edge2D(edgeCenterPoint, Orientation.VERTICAL, original.getMaxY() - original.getMinY());
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
/*     */   public ToWestChangeStrategy(Rectangle2D original, boolean ratioFixed, double ratio, double maxX, double maxY) {
/*  91 */     this(original, ratioFixed, ratio, new Rectangle2D(0.0D, 0.0D, maxX, maxY));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Edge2D getFixedEdge() {
/* 101 */     return this.easternEdge;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\tools\rectangle\change\ToWestChangeStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */