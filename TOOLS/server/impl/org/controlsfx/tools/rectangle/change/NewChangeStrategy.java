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
/*    */ public class NewChangeStrategy
/*    */   extends AbstractFixedPointChangeStrategy
/*    */ {
/*    */   private Point2D startingPoint;
/*    */   
/*    */   public NewChangeStrategy(boolean ratioFixed, double ratio, Rectangle2D bounds) {
/* 63 */     super(ratioFixed, ratio, bounds);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void beforeBeginHook(Point2D point) {
/* 71 */     this.startingPoint = point;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Point2D getFixedCorner() {
/* 79 */     return this.startingPoint;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\tools\rectangle\change\NewChangeStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */