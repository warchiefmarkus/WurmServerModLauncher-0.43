/*    */ package 1.0.com.sun.tools.xjc.generator.marshaller;
/*    */ 
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.tools.xjc.generator.marshaller.Context;
/*    */ import com.sun.tools.xjc.generator.marshaller.IfThenElseBlockReference;
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
/*    */ class NestedIfBlockProvider
/*    */ {
/*    */   private final Context context;
/*    */   private int nestLevel;
/*    */   private IfThenElseBlockReference previous;
/*    */   
/*    */   NestedIfBlockProvider(Context _context) {
/* 33 */     this.nestLevel = 0;
/* 34 */     this.previous = null;
/*    */     this.context = _context;
/*    */   }
/*    */   public void startBlock(JExpression testExp) {
/* 38 */     startElse();
/*    */     
/* 40 */     this.nestLevel++;
/* 41 */     this.previous = new IfThenElseBlockReference(this.context, testExp);
/* 42 */     this.context.pushNewBlock(this.previous.createThenProvider());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void startElse() {
/* 50 */     if (this.previous != null) {
/* 51 */       this.context.popBlock();
/* 52 */       this.context.pushNewBlock(this.previous.createElseProvider());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void end() {
/* 58 */     while (this.nestLevel-- > 0)
/* 59 */       this.context.popBlock(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\marshaller\NestedIfBlockProvider.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */