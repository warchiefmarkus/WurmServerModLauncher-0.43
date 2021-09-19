/*    */ package 1.0.com.sun.tools.xjc.generator.marshaller;
/*    */ 
/*    */ import com.sun.codemodel.JConditional;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.tools.xjc.generator.marshaller.Context;
/*    */ import com.sun.tools.xjc.generator.util.BlockReference;
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
/*    */ class IfThenElseBlockReference
/*    */ {
/*    */   private final JExpression testExp;
/*    */   private final BlockReference parent;
/*    */   private JConditional cond;
/*    */   private boolean swapped;
/*    */   
/*    */   IfThenElseBlockReference(Context _context, JExpression exp) {
/* 38 */     this.swapped = false;
/*    */     this.testExp = exp;
/*    */     this.parent = _context.getCurrentBlock();
/*    */   } public BlockReference createThenProvider() {
/* 42 */     return (BlockReference)new Object(this);
/*    */   }
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
/*    */   public BlockReference createElseProvider() {
/* 55 */     return (BlockReference)new Object(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\marshaller\IfThenElseBlockReference.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */