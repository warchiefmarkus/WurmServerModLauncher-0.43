/*     */ package 1.0.com.sun.tools.xjc.generator.marshaller;
/*     */ 
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.msv.datatype.xsd.QnameValueType;
/*     */ import com.sun.msv.datatype.xsd.XSDatatype;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ValueExp;
/*     */ import com.sun.tools.xjc.generator.marshaller.Context;
/*     */ import com.sun.tools.xjc.generator.marshaller.Pass;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import javax.xml.bind.DatatypeConverter;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractPassImpl
/*     */   implements Pass
/*     */ {
/*     */   private final String name;
/*     */   protected final Context context;
/*     */   
/*     */   AbstractPassImpl(Context _context, String _name) {
/*  38 */     this.context = _context;
/*  39 */     this.name = _name;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/*  44 */     return this.name;
/*     */   }
/*     */   
/*     */   public final void build(Expression exp) {
/*  48 */     Pass old = this.context.currentPass;
/*  49 */     this.context.currentPass = this;
/*  50 */     this.context.build(exp);
/*  51 */     this.context.currentPass = old;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void marshalValue(ValueExp exp) {
/*  64 */     if (!exp.dt.isContextDependent()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  72 */       String literal = null;
/*  73 */       if (exp.dt instanceof XSDatatype)
/*     */       {
/*  75 */         literal = ((XSDatatype)exp.dt).convertToLexicalValue(exp.value, null);
/*     */       }
/*     */ 
/*     */       
/*  79 */       if (literal == null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  85 */         throw new JAXBAssertionError();
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  92 */       getBlock(true).invoke((JExpression)this.context.$serializer, "text").arg(JExpr.lit(literal)).arg(JExpr._null());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 100 */     else if (exp.dt instanceof com.sun.msv.datatype.xsd.QnameType) {
/*     */       
/* 102 */       QnameValueType qn = (QnameValueType)exp.value;
/*     */       
/* 104 */       getBlock(true).invoke((JExpression)this.context.$serializer, "text").arg((JExpression)this.context.codeModel.ref(DatatypeConverter.class).staticInvoke("printQName").arg((JExpression)JExpr._new(this.context.codeModel.ref(QName.class)).arg(JExpr.lit(qn.namespaceURI)).arg(JExpr.lit(qn.localPart))).arg((JExpression)this.context.$serializer.invoke("getNamespaceContext"))).arg(JExpr._null());
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */       
/* 112 */       throw new JAXBAssertionError("unsupported datatype " + exp.name);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JBlock getBlock(boolean create) {
/* 121 */     return this.context.getCurrentBlock().get(create);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\marshaller\AbstractPassImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */