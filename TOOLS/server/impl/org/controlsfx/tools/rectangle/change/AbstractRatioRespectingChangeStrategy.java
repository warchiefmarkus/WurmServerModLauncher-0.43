/*    */ package impl.org.controlsfx.tools.rectangle.change;
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
/*    */ abstract class AbstractRatioRespectingChangeStrategy
/*    */   extends AbstractBeginEndCheckingChangeStrategy
/*    */ {
/*    */   private final boolean ratioFixed;
/*    */   private final double ratio;
/*    */   
/*    */   protected AbstractRatioRespectingChangeStrategy(boolean ratioFixed, double ratio) {
/* 60 */     this.ratioFixed = ratioFixed;
/* 61 */     this.ratio = ratio;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final boolean isRatioFixed() {
/* 72 */     return this.ratioFixed;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final double getRatio() {
/* 81 */     if (!this.ratioFixed)
/* 82 */       throw new IllegalStateException("The ratio is not fixed."); 
/* 83 */     return this.ratio;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\tools\rectangle\change\AbstractRatioRespectingChangeStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */