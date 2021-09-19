/*    */ package impl.org.controlsfx.tools.rectangle.change;
/*    */ 
/*    */ import javafx.geometry.Point2D;
/*    */ import javafx.geometry.Rectangle2D;
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
/*    */ public class ToNorthwestChangeStrategy
/*    */   extends AbstractFixedPointChangeStrategy
/*    */ {
/*    */   private final Point2D southeasternCorner;
/*    */   
/*    */   public ToNorthwestChangeStrategy(Rectangle2D original, boolean ratioFixed, double ratio, Rectangle2D bounds) {
/* 66 */     super(ratioFixed, ratio, bounds);
/* 67 */     this.southeasternCorner = new Point2D(original.getMaxX(), original.getMaxY());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Point2D getFixedCorner() {
/* 77 */     return this.southeasternCorner;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\tools\rectangle\change\ToNorthwestChangeStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */