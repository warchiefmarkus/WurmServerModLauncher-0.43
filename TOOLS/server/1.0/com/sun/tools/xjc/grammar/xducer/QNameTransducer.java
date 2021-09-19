/*    */ package 1.0.com.sun.tools.xjc.grammar.xducer;
/*    */ 
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JExpr;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.msv.datatype.xsd.QnameValueType;
/*    */ import com.sun.msv.grammar.ValueExp;
/*    */ import com.sun.tools.xjc.generator.util.BlockReference;
/*    */ import com.sun.tools.xjc.generator.util.WhitespaceNormalizer;
/*    */ import com.sun.tools.xjc.grammar.xducer.DeserializerContext;
/*    */ import com.sun.tools.xjc.grammar.xducer.SerializerContext;
/*    */ import com.sun.tools.xjc.grammar.xducer.TransducerImpl;
/*    */ import javax.xml.bind.DatatypeConverter;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class QNameTransducer
/*    */   extends TransducerImpl
/*    */ {
/*    */   private final JCodeModel codeModel;
/*    */   
/*    */   public QNameTransducer(JCodeModel cm) {
/* 23 */     this.codeModel = cm;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void declareNamespace(BlockReference body, JExpression value, SerializerContext context) {
/* 29 */     context.declareNamespace(body.get(true), (JExpression)value.invoke("getNamespaceURI"), (JExpression)value.invoke("getPrefix"), JExpr.FALSE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JExpression generateSerializer(JExpression value, SerializerContext context) {
/* 39 */     return (JExpression)this.codeModel.ref(DatatypeConverter.class).staticInvoke("printQName").arg(value).arg(context.getNamespaceContext());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JExpression generateDeserializer(JExpression lexical, DeserializerContext context) {
/* 46 */     return (JExpression)this.codeModel.ref(DatatypeConverter.class).staticInvoke("parseQName").arg(WhitespaceNormalizer.COLLAPSE.generate(this.codeModel, lexical)).arg(context.getNamespaceContext());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JType getReturnType() {
/* 53 */     return (JType)this.codeModel.ref(QName.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JExpression generateConstant(ValueExp exp) {
/* 59 */     QnameValueType data = (QnameValueType)exp.value;
/*    */     
/* 61 */     return (JExpression)JExpr._new(this.codeModel.ref(QName.class)).arg(JExpr.lit(data.namespaceURI)).arg(JExpr.lit(data.localPart));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\xducer\QNameTransducer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */