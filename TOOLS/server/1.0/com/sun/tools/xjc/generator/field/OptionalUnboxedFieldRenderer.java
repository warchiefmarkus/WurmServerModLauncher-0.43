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
/*     */ import com.sun.codemodel.JPrimitiveType;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.tools.xjc.generator.ClassContext;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OptionalUnboxedFieldRenderer
/*     */   extends AbstractFieldRendererWithVar
/*     */ {
/*     */   private JVar $has_flag;
/*     */   private JBlock onSetEvent;
/*     */   
/*     */   public OptionalUnboxedFieldRenderer(ClassContext context, FieldUse fu) {
/*  57 */     super(context, fu);
/*     */   }
/*     */   
/*     */   protected JFieldVar generateField() {
/*  61 */     this.$has_flag = (JVar)this.context.implClass.field(2, (JType)this.codeModel.BOOLEAN, "has_" + this.fu.name);
/*  62 */     return generateField(this.fu.type);
/*     */   }
/*     */   
/*     */   public JClass getValueType() {
/*  66 */     return ((JPrimitiveType)this.fu.type).getWrapperClass();
/*     */   }
/*     */   
/*     */   public JExpression getValue() {
/*  70 */     return ((JPrimitiveType)this.fu.type).wrap((JExpression)ref());
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
/*     */   public void generateAccessors() {
/*  84 */     JMethod $get = this.writer.declareMethod(this.fu.type, ((this.fu.type == this.codeModel.BOOLEAN) ? "is" : "get") + this.fu.name);
/*     */ 
/*     */     
/*  87 */     String javadoc = this.fu.getJavadoc();
/*  88 */     if (javadoc.length() == 0) {
/*  89 */       javadoc = Messages.format("SingleFieldRenderer.DefaultGetterJavadoc", NameConverter.standard.toVariableName(this.fu.name));
/*     */     }
/*     */     
/*  92 */     this.writer.javadoc().appendComment(javadoc);
/*     */     
/*  94 */     DefaultValue[] defaultValues = this.fu.getDefaultValues();
/*  95 */     if (defaultValues == null) {
/*  96 */       $get.body()._return((JExpression)ref());
/*     */     } else {
/*     */       
/*  99 */       _assert((defaultValues.length == 1));
/*     */       
/* 101 */       JConditional cond = $get.body()._if(this.$has_flag.not());
/*     */       
/* 103 */       cond._then()._return(defaultValues[0].generateConstant());
/* 104 */       cond._else()._return((JExpression)ref());
/*     */     } 
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
/* 116 */     JMethod $set = this.writer.declareMethod((JType)this.codeModel.VOID, "set" + this.fu.name);
/* 117 */     JVar $value = this.writer.addParameter(this.fu.type, "value");
/* 118 */     JBlock body = $set.body();
/* 119 */     body.assign((JAssignmentTarget)ref(), (JExpression)$value);
/* 120 */     body.assign((JAssignmentTarget)this.$has_flag, JExpr.TRUE);
/* 121 */     this.onSetEvent = body;
/* 122 */     javadoc = this.fu.getJavadoc();
/* 123 */     if (javadoc.length() == 0) {
/* 124 */       javadoc = Messages.format("SingleFieldRenderer.DefaultSetterJavadoc", NameConverter.standard.toVariableName(this.fu.name));
/*     */     }
/*     */     
/* 127 */     this.writer.javadoc().appendComment(javadoc);
/*     */   }
/*     */   
/*     */   public void toArray(JBlock block, JExpression $array) {
/* 131 */     block.assign((JAssignmentTarget)$array.component(JExpr.lit(0)), (JExpression)ref());
/*     */   }
/*     */ 
/*     */   
/*     */   public void unsetValues(JBlock body) {
/* 136 */     body.assign((JAssignmentTarget)this.$has_flag, JExpr.FALSE);
/*     */   }
/*     */   
/*     */   public JExpression hasSetValue() {
/* 140 */     return (JExpression)this.$has_flag;
/*     */   }
/*     */   
/*     */   public JBlock getOnSetEventHandler() {
/* 144 */     return this.onSetEvent;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JExpression ifCountEqual(int i) {
/* 150 */     switch (i) { case 0:
/* 151 */         return this.$has_flag.not();
/* 152 */       case 1: return (JExpression)this.$has_flag; }
/* 153 */      return JExpr.FALSE;
/*     */   }
/*     */   
/*     */   public JExpression ifCountGte(int i) {
/* 157 */     if (i == 1) return (JExpression)this.$has_flag; 
/* 158 */     return JExpr.FALSE;
/*     */   }
/*     */   
/*     */   public JExpression ifCountLte(int i) {
/* 162 */     if (i == 0) return this.$has_flag.not(); 
/* 163 */     return JExpr.TRUE;
/*     */   }
/*     */   
/*     */   public JExpression count() {
/* 167 */     return JOp.cond((JExpression)this.$has_flag, JExpr.lit(1), JExpr.lit(0));
/*     */   }
/*     */   
/*     */   public void setter(JBlock block, JExpression newValue) {
/* 171 */     block.assign((JAssignmentTarget)ref(), newValue);
/* 172 */     block.assign((JAssignmentTarget)this.$has_flag, JExpr.TRUE);
/*     */   }
/*     */   
/*     */   public FieldMarshallerGenerator createMarshaller(JBlock block, String uniqueId) {
/* 176 */     return (FieldMarshallerGenerator)new Object(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\field\OptionalUnboxedFieldRenderer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */