/*    */ package 1.0.com.sun.tools.xjc.grammar.xducer;
/*    */ 
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.msv.datatype.DatabindableDatatype;
/*    */ import com.sun.msv.grammar.ValueExp;
/*    */ import com.sun.tools.xjc.grammar.xducer.DeserializerContext;
/*    */ import com.sun.tools.xjc.grammar.xducer.SerializerContext;
/*    */ import com.sun.tools.xjc.grammar.xducer.TransducerImpl;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DatabindableXducer
/*    */   extends TransducerImpl
/*    */ {
/*    */   private final DatabindableDatatype dt;
/*    */   private final JClass returnType;
/*    */   
/*    */   public DatabindableXducer(JCodeModel writer, DatabindableDatatype _dt) {
/* 28 */     this.dt = _dt;
/*    */     
/* 30 */     String name = this.dt.getJavaObjectType().getName();
/* 31 */     int idx = name.lastIndexOf(".");
/* 32 */     if (idx < 0) {
/* 33 */       this.returnType = writer._package("").ref(name);
/*    */     } else {
/* 35 */       this.returnType = writer._package(name.substring(0, idx)).ref(name.substring(idx + 1));
/*    */     } 
/*    */   }
/*    */   
/*    */   public JType getReturnType() {
/* 40 */     return (JType)this.returnType;
/*    */   }
/*    */   
/*    */   public JExpression generateSerializer(JExpression value, SerializerContext context) {
/* 44 */     throw new UnsupportedOperationException("TODO");
/*    */   }
/*    */ 
/*    */   
/*    */   public JExpression generateDeserializer(JExpression value, DeserializerContext context) {
/* 49 */     throw new UnsupportedOperationException("TODO");
/*    */   }
/*    */ 
/*    */   
/*    */   public JExpression generateConstant(ValueExp exp) {
/* 54 */     throw new UnsupportedOperationException("TODO");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\xducer\DatabindableXducer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */