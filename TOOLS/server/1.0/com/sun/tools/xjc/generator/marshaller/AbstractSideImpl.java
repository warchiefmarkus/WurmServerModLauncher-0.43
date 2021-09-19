/*    */ package 1.0.com.sun.tools.xjc.generator.marshaller;
/*    */ 
/*    */ import com.sun.codemodel.JBlock;
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JExpr;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JPrimitiveType;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.tools.xjc.generator.marshaller.Context;
/*    */ import com.sun.tools.xjc.generator.marshaller.Side;
/*    */ import com.sun.tools.xjc.generator.util.BlockReference;
/*    */ import com.sun.xml.bind.JAXBAssertionError;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class AbstractSideImpl
/*    */   implements Side
/*    */ {
/*    */   protected final Context context;
/*    */   
/*    */   protected AbstractSideImpl(Context _context) {
/* 28 */     this.context = _context;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final JBlock getBlock(boolean create) {
/* 35 */     return this.context.getCurrentBlock().get(create);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final BlockReference createWhileBlock(BlockReference parent, JExpression expr) {
/* 42 */     return (BlockReference)new Object(this, parent, expr);
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
/*    */   protected final JExpression instanceOf(JExpression obj, JType type) {
/*    */     JClass jClass;
/* 55 */     if (this.context.codeModel.NULL == type) {
/* 56 */       return obj.eq(JExpr._null());
/*    */     }
/* 58 */     if (type instanceof JPrimitiveType) {
/* 59 */       jClass = ((JPrimitiveType)type).getWrapperClass();
/*    */     }
/* 61 */     return obj._instanceof((JType)jClass);
/*    */   }
/*    */ 
/*    */   
/*    */   protected static Object _assert(boolean b) {
/* 66 */     if (!b)
/* 67 */       throw new JAXBAssertionError(); 
/* 68 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\marshaller\AbstractSideImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */