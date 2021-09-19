/*     */ package 1.0.com.sun.tools.xjc.generator.field;
/*     */ 
/*     */ import com.sun.codemodel.JAssignmentTarget;
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JConditional;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JFieldVar;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JOp;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.tools.xjc.generator.ClassContext;
/*     */ import com.sun.tools.xjc.generator.JavadocBuilder;
/*     */ import com.sun.tools.xjc.generator.field.AbstractFieldRendererWithVar;
/*     */ import com.sun.tools.xjc.generator.field.Messages;
/*     */ import com.sun.tools.xjc.generator.marshaller.FieldMarshallerGenerator;
/*     */ import com.sun.tools.xjc.grammar.DefaultValue;
/*     */ import com.sun.tools.xjc.grammar.FieldUse;
/*     */ import com.sun.tools.xjc.reader.NameConverter;
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
/*     */ public class SingleFieldRenderer
/*     */   extends AbstractFieldRendererWithVar
/*     */ {
/*     */   private JBlock onSetEvent;
/*     */   
/*     */   public SingleFieldRenderer(ClassContext context, FieldUse fu) {
/*  48 */     super(context, fu);
/*  49 */     _assert(!fu.type.isPrimitive());
/*     */   }
/*     */   
/*     */   protected JFieldVar generateField() {
/*  53 */     return generateField(this.fu.type);
/*     */   }
/*     */   
/*     */   public JClass getValueType() {
/*  57 */     return (JClass)this.fu.type;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generateAccessors() {
/*  76 */     JMethod $get = this.writer.declareMethod(this.fu.type, ((this.fu.type == this.codeModel.BOOLEAN) ? "is" : "get") + this.fu.name);
/*     */     
/*  78 */     String javadoc = this.fu.getJavadoc();
/*  79 */     if (javadoc.length() == 0) {
/*  80 */       javadoc = Messages.format("SingleFieldRenderer.DefaultGetterJavadoc", NameConverter.standard.toVariableName(this.fu.name));
/*     */     }
/*     */     
/*  83 */     this.writer.javadoc().appendComment(javadoc);
/*     */ 
/*     */     
/*  86 */     DefaultValue[] defaultValues = this.fu.getDefaultValues();
/*  87 */     if (defaultValues == null) {
/*  88 */       $get.body()._return((JExpression)ref());
/*     */     } else {
/*     */       
/*  91 */       _assert((defaultValues.length == 1));
/*     */       
/*  93 */       JConditional cond = $get.body()._if(ref().eq(JExpr._null()));
/*  94 */       cond._then()._return(defaultValues[0].generateConstant());
/*  95 */       cond._else()._return((JExpression)ref());
/*     */     } 
/*     */     
/*  98 */     this.writer.javadoc().addReturn("possible object is\n" + JavadocBuilder.listPossibleTypes(this.fu));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     JMethod $set = this.writer.declareMethod((JType)this.codeModel.VOID, "set" + this.fu.name);
/* 108 */     JVar $value = this.writer.addParameter(this.fu.type, "value");
/* 109 */     JBlock body = $set.body();
/* 110 */     body.assign((JAssignmentTarget)ref(), (JExpression)$value);
/* 111 */     this.onSetEvent = body;
/*     */     
/* 113 */     javadoc = this.fu.getJavadoc();
/* 114 */     if (javadoc.length() == 0) {
/* 115 */       javadoc = Messages.format("SingleFieldRenderer.DefaultSetterJavadoc", NameConverter.standard.toVariableName(this.fu.name));
/*     */     }
/*     */ 
/*     */     
/* 119 */     this.writer.javadoc().appendComment(javadoc);
/* 120 */     this.writer.javadoc().addParam($value, "allowed object is\n" + JavadocBuilder.listPossibleTypes(this.fu));
/*     */   }
/*     */ 
/*     */   
/*     */   public JBlock getOnSetEventHandler() {
/* 125 */     return this.onSetEvent;
/*     */   }
/*     */   
/*     */   public void setter(JBlock block, JExpression newValue) {
/* 129 */     block.assign((JAssignmentTarget)ref(), newValue);
/*     */   }
/*     */   
/*     */   public void toArray(JBlock block, JExpression $array) {
/* 133 */     block.assign((JAssignmentTarget)$array.component(JExpr.lit(0)), (JExpression)ref());
/*     */   }
/*     */   
/*     */   public void unsetValues(JBlock body) {
/* 137 */     body.assign((JAssignmentTarget)ref(), JExpr._null());
/*     */   }
/*     */   public JExpression hasSetValue() {
/* 140 */     return ref().ne(JExpr._null());
/*     */   }
/*     */   public JExpression getValue() {
/* 143 */     return (JExpression)ref();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JExpression ifCountEqual(int i) {
/* 149 */     switch (i) { case 0:
/* 150 */         return ref().eq(JExpr._null());
/* 151 */       case 1: return ref().ne(JExpr._null()); }
/* 152 */      return JExpr.FALSE;
/*     */   }
/*     */   
/*     */   public JExpression ifCountGte(int i) {
/* 156 */     if (i == 1) return ref().ne(JExpr._null()); 
/* 157 */     return JExpr.FALSE;
/*     */   }
/*     */   
/*     */   public JExpression ifCountLte(int i) {
/* 161 */     if (i == 0) return ref().eq(JExpr._null()); 
/* 162 */     return JExpr.TRUE;
/*     */   }
/*     */   
/*     */   public JExpression count() {
/* 166 */     return JOp.cond(ref().ne(JExpr._null()), JExpr.lit(1), JExpr.lit(0));
/*     */   }
/*     */ 
/*     */   
/*     */   public FieldMarshallerGenerator createMarshaller(JBlock block, String uniqueId) {
/* 171 */     return (FieldMarshallerGenerator)new Object(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\field\SingleFieldRenderer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */