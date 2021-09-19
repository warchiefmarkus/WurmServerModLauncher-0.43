/*     */ package 1.0.com.sun.tools.xjc.grammar.xducer;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JPrimitiveType;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.msv.grammar.ValueExp;
/*     */ import com.sun.tools.xjc.generator.field.FieldRenderer;
/*     */ import com.sun.tools.xjc.generator.util.BlockReference;
/*     */ import com.sun.tools.xjc.grammar.xducer.DeserializerContext;
/*     */ import com.sun.tools.xjc.grammar.xducer.SerializerContext;
/*     */ import com.sun.tools.xjc.grammar.xducer.Transducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.TransducerDecorator;
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
/*     */ public final class TypeAdaptedTransducer
/*     */   extends TransducerDecorator
/*     */ {
/*     */   private final JType expectedType;
/*     */   private final boolean boxing;
/*     */   
/*     */   public static Transducer adapt(Transducer xducer, FieldRenderer fieldRenderer) {
/*  37 */     return adapt(xducer, (fieldRenderer.getFieldUse()).type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Transducer adapt(Transducer xducer, JType expectedType) {
/*     */     JClass jClass;
/*  45 */     JType t = xducer.getReturnType();
/*  46 */     if (t instanceof JPrimitiveType && expectedType instanceof JClass) {
/*  47 */       jClass = ((JPrimitiveType)t).getWrapperClass();
/*  48 */       return (Transducer)new com.sun.tools.xjc.grammar.xducer.TypeAdaptedTransducer(xducer, (JType)jClass);
/*     */     } 
/*     */ 
/*     */     
/*  52 */     if (t instanceof JClass && jClass instanceof JPrimitiveType) {
/*  53 */       return (Transducer)new com.sun.tools.xjc.grammar.xducer.TypeAdaptedTransducer(xducer, (JType)jClass);
/*     */     }
/*     */     
/*  56 */     return xducer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TypeAdaptedTransducer(Transducer _xducer, JType _expectedType) {
/*  66 */     super(_xducer);
/*  67 */     this.expectedType = _expectedType;
/*  68 */     this.boxing = this.expectedType instanceof JClass;
/*     */   }
/*     */ 
/*     */   
/*     */   public JType getReturnType() {
/*  73 */     return this.expectedType;
/*     */   }
/*     */ 
/*     */   
/*     */   public JExpression generateSerializer(JExpression value, SerializerContext context) {
/*  78 */     if (this.boxing) {
/*  79 */       return super.generateSerializer(((JPrimitiveType)super.getReturnType()).unwrap(value), context);
/*     */     }
/*     */     
/*  82 */     return super.generateSerializer(((JPrimitiveType)this.expectedType).wrap(value), context);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JExpression generateDeserializer(JExpression literal, DeserializerContext context) {
/*  88 */     if (this.boxing) {
/*  89 */       return ((JPrimitiveType)super.getReturnType()).wrap(super.generateDeserializer(literal, context));
/*     */     }
/*     */     
/*  92 */     return ((JPrimitiveType)this.expectedType).unwrap(super.generateDeserializer(literal, context));
/*     */   }
/*     */ 
/*     */   
/*     */   public JExpression generateConstant(ValueExp exp) {
/*  97 */     if (this.boxing) {
/*  98 */       return ((JPrimitiveType)super.getReturnType()).wrap(super.generateConstant(exp));
/*     */     }
/*     */     
/* 101 */     return ((JPrimitiveType)this.expectedType).unwrap(super.generateConstant(exp));
/*     */   }
/*     */ 
/*     */   
/*     */   public void declareNamespace(BlockReference body, JExpression value, SerializerContext context) {
/* 106 */     if (this.boxing) {
/* 107 */       super.declareNamespace(body, ((JPrimitiveType)super.getReturnType()).unwrap(value), context);
/*     */     } else {
/*     */       
/* 110 */       super.declareNamespace(body, ((JPrimitiveType)this.expectedType).wrap(value), context);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\xducer\TypeAdaptedTransducer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */