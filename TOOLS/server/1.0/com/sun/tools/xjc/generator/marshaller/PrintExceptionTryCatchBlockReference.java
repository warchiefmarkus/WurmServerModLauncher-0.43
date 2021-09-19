/*    */ package 1.0.com.sun.tools.xjc.generator.marshaller;
/*    */ 
/*    */ import com.sun.codemodel.JBlock;
/*    */ import com.sun.codemodel.JCatchBlock;
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JExpr;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JTryBlock;
/*    */ import com.sun.tools.xjc.generator.marshaller.Context;
/*    */ import com.sun.tools.xjc.generator.util.BlockReference;
/*    */ import com.sun.tools.xjc.runtime.Util;
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
/*    */ class PrintExceptionTryCatchBlockReference
/*    */   implements BlockReference
/*    */ {
/*    */   private final BlockReference parent;
/*    */   private final Context context;
/* 29 */   private JTryBlock block = null;
/*    */ 
/*    */   
/*    */   PrintExceptionTryCatchBlockReference(Context _context) {
/* 33 */     this.context = _context;
/* 34 */     this.parent = this.context.getCurrentBlock();
/*    */   }
/*    */   
/*    */   public JBlock get(boolean create) {
/* 38 */     if (!create && this.block == null) return null;
/*    */     
/* 40 */     if (this.block == null) {
/*    */       
/* 42 */       this.block = this.parent.get(true)._try();
/*    */       
/* 44 */       JCodeModel codeModel = this.context.codeModel;
/*    */       
/* 46 */       JCatchBlock $catch = this.block._catch(codeModel.ref(Exception.class));
/* 47 */       $catch.body().staticInvoke(this.context.getRuntime(Util.class), "handlePrintConversionException").arg(JExpr._this()).arg((JExpression)$catch.param("e")).arg((JExpression)this.context.$serializer);
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 54 */     return this.block.body();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\marshaller\PrintExceptionTryCatchBlockReference.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */