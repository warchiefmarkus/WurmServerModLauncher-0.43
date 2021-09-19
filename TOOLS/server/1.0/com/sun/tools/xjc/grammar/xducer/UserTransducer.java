/*     */ package 1.0.com.sun.tools.xjc.grammar.xducer;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JInvocation;
/*     */ import com.sun.codemodel.JStatement;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.msv.grammar.ValueExp;
/*     */ import com.sun.tools.xjc.generator.util.BlockReference;
/*     */ import com.sun.tools.xjc.grammar.xducer.DeserializerContext;
/*     */ import com.sun.tools.xjc.grammar.xducer.Messages;
/*     */ import com.sun.tools.xjc.grammar.xducer.SerializerContext;
/*     */ import com.sun.tools.xjc.grammar.xducer.TransducerImpl;
/*     */ import com.sun.xml.bind.DatatypeConverterImpl;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UserTransducer
/*     */   extends TransducerImpl
/*     */ {
/*     */   private final JType type;
/*     */   private final JCodeModel codeModel;
/*     */   private final String parseMethod;
/*     */   private final String printMethod;
/*     */   private final boolean enableNamespaceContext;
/*     */   private static final String ERR_EXTERNAL_PARSE_METHOD_REQUIRED = "UserTransducer.ExternalParseMethodRequired";
/*     */   private static final String ERR_EXTERNAL_PRINT_METHOD_REQUIRED = "UserTransducer.ExternalPrintMethodRequired";
/*     */   
/*     */   public UserTransducer(JType _type, String _parseMethod, String _printMethod, boolean _enableNamespaceContext) {
/*  81 */     this.type = _type;
/*  82 */     this.codeModel = _type.owner();
/*     */     
/*  84 */     this.parseMethod = _parseMethod;
/*  85 */     this.printMethod = _printMethod;
/*     */     
/*  87 */     this.enableNamespaceContext = _enableNamespaceContext;
/*     */     
/*  89 */     if (this.type.isPrimitive()) {
/*     */ 
/*     */       
/*  92 */       if (this.parseMethod.indexOf('.') == -1) {
/*  93 */         throw new IllegalArgumentException(Messages.format("UserTransducer.ExternalParseMethodRequired", _type.name()));
/*     */       }
/*  95 */       if (this.printMethod.indexOf('.') == -1) {
/*  96 */         throw new IllegalArgumentException(Messages.format("UserTransducer.ExternalPrintMethodRequired", _type.name()));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public UserTransducer(JType _type, String _parseMethod, String _printMethod) {
/* 102 */     this(_type, _parseMethod, _printMethod, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public JType getReturnType() {
/* 107 */     return this.type;
/*     */   }
/*     */   
/*     */   public JExpression generateSerializer(JExpression value, SerializerContext context) {
/* 111 */     return (JExpression)_generateSerializer(value, context);
/*     */   }
/*     */ 
/*     */   
/*     */   private JInvocation _generateSerializer(JExpression value, SerializerContext context) {
/*     */     JInvocation inv;
/* 117 */     int idx = this.printMethod.lastIndexOf('.');
/* 118 */     if (idx < 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 123 */       inv = value.invoke(this.printMethod);
/*     */     } else {
/*     */       
/*     */       try {
/* 127 */         inv = this.codeModel.ref(this.printMethod.substring(0, idx)).staticInvoke(this.printMethod.substring(idx + 1)).arg(value);
/*     */       }
/* 129 */       catch (ClassNotFoundException e) {
/* 130 */         throw new NoClassDefFoundError(e.getMessage());
/*     */       } 
/*     */     } 
/*     */     
/* 134 */     if (this.enableNamespaceContext) {
/* 135 */       inv.arg(context.getNamespaceContext());
/*     */     }
/* 137 */     return inv;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JExpression generateDeserializer(JExpression literal, DeserializerContext context) {
/*     */     JInvocation inv;
/* 144 */     if (this.parseMethod.equals("new")) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 149 */       inv = JExpr._new(this.type);
/*     */     } else {
/* 151 */       int idx = this.parseMethod.lastIndexOf('.');
/* 152 */       if (idx < 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 158 */         inv = ((JClass)this.type).staticInvoke(this.parseMethod);
/*     */       } else {
/*     */         
/*     */         try {
/* 162 */           inv = this.codeModel.ref(this.parseMethod.substring(0, idx)).staticInvoke(this.parseMethod.substring(idx + 1));
/*     */         }
/* 164 */         catch (ClassNotFoundException e) {
/* 165 */           throw new NoClassDefFoundError(e.getMessage());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 171 */     inv.arg(literal);
/* 172 */     if (this.enableNamespaceContext) {
/* 173 */       inv.arg(context.getNamespaceContext());
/*     */     }
/* 175 */     return (JExpression)inv;
/*     */   }
/*     */   
/*     */   public void declareNamespace(BlockReference body, JExpression value, SerializerContext context) {
/* 179 */     if (this.enableNamespaceContext)
/*     */     {
/*     */       
/* 182 */       body.get(true).add((JStatement)_generateSerializer(value, context)); } 
/*     */   }
/*     */   
/*     */   public JExpression generateConstant(ValueExp exp) {
/* 186 */     return generateDeserializer((JExpression)this.codeModel.ref(DatatypeConverterImpl.class).staticInvoke("installHook").arg(JExpr.lit(obtainString(exp))), null);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\xducer\UserTransducer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */