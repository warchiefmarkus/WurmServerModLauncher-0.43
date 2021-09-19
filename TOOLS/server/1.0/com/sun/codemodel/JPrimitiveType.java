/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JArrayClass;
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JExpr;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JFormatter;
/*    */ import com.sun.codemodel.JType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class JPrimitiveType
/*    */   extends JType
/*    */ {
/*    */   private final String typeName;
/*    */   private final JCodeModel owner;
/*    */   private final JClass wrapperClass;
/*    */   
/*    */   JPrimitiveType(JCodeModel owner, String typeName, Class wrapper) {
/* 23 */     this.owner = owner;
/* 24 */     this.typeName = typeName;
/* 25 */     this.wrapperClass = owner.ref(wrapper);
/*    */   }
/*    */   public JCodeModel owner() {
/* 28 */     return this.owner;
/*    */   }
/*    */   public String fullName() {
/* 31 */     return this.typeName;
/*    */   }
/*    */   
/*    */   public String name() {
/* 35 */     return fullName();
/*    */   }
/*    */   
/*    */   public boolean isPrimitive() {
/* 39 */     return true;
/*    */   }
/*    */   
/*    */   public JClass array() {
/* 43 */     return (JClass)new JArrayClass(this.owner, this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JClass getWrapperClass() {
/* 52 */     return this.wrapperClass;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JExpression wrap(JExpression exp) {
/* 63 */     return (JExpression)JExpr._new(getWrapperClass()).arg(exp);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JExpression unwrap(JExpression exp) {
/* 74 */     return (JExpression)exp.invoke(this.typeName + "Value");
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 78 */     f.p(this.typeName);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JPrimitiveType.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */