/*    */ package impl.org.controlsfx.tools.rectangle.change;
/*    */ 
/*    */ import java.util.Objects;
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
/*    */ abstract class AbstractPreviousRectangleChangeStrategy
/*    */   extends AbstractRatioRespectingChangeStrategy
/*    */ {
/*    */   private final Rectangle2D previous;
/*    */   
/*    */   protected AbstractPreviousRectangleChangeStrategy(Rectangle2D previous, boolean ratioFixed, double ratio) {
/* 60 */     super(ratioFixed, ratio);
/*    */     
/* 62 */     Objects.requireNonNull(previous, "The previous rectangle must not be null.");
/* 63 */     this.previous = previous;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final Rectangle2D getPrevious() {
/* 74 */     return this.previous;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\tools\rectangle\change\AbstractPreviousRectangleChangeStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */