/*    */ package impl.org.controlsfx.tools.rectangle.change;
/*    */ 
/*    */ import java.util.Objects;
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
/*    */ abstract class AbstractBeginEndCheckingChangeStrategy
/*    */   implements Rectangle2DChangeStrategy
/*    */ {
/*    */   private boolean beforeBegin = true;
/*    */   
/*    */   public final Rectangle2D beginChange(Point2D point) {
/* 63 */     Objects.requireNonNull(point, "The specified point must not be null.");
/* 64 */     if (!this.beforeBegin) {
/* 65 */       throw new IllegalStateException("The change already began, so 'beginChange' must not be called again before 'endChange' was called.");
/*    */     }
/* 67 */     this.beforeBegin = false;
/*    */     
/* 69 */     beforeBeginHook(point);
/* 70 */     return doBegin(point);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Rectangle2D continueChange(Point2D point) {
/* 78 */     Objects.requireNonNull(point, "The specified point must not be null.");
/* 79 */     if (this.beforeBegin) {
/* 80 */       throw new IllegalStateException("The change did not begin. Call 'beginChange' before 'continueChange'.");
/*    */     }
/* 82 */     return doContinue(point);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Rectangle2D endChange(Point2D point) {
/* 90 */     Objects.requireNonNull(point, "The specified point must not be null.");
/* 91 */     if (this.beforeBegin) {
/* 92 */       throw new IllegalStateException("The change did not begin. Call 'beginChange' before 'endChange'.");
/*    */     }
/* 94 */     Rectangle2D finalRectangle = doEnd(point);
/* 95 */     afterEndHook(point);
/* 96 */     this.beforeBegin = true;
/* 97 */     return finalRectangle;
/*    */   }
/*    */   
/*    */   protected void beforeBeginHook(Point2D point) {}
/*    */   
/*    */   protected abstract Rectangle2D doBegin(Point2D paramPoint2D);
/*    */   
/*    */   protected abstract Rectangle2D doContinue(Point2D paramPoint2D);
/*    */   
/*    */   protected abstract Rectangle2D doEnd(Point2D paramPoint2D);
/*    */   
/*    */   protected void afterEndHook(Point2D point) {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\tools\rectangle\change\AbstractBeginEndCheckingChangeStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */