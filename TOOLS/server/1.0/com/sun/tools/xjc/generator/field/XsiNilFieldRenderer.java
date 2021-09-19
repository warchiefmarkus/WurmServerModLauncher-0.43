/*     */ package 1.0.com.sun.tools.xjc.generator.field;
/*     */ 
/*     */ import com.sun.codemodel.JAssignmentTarget;
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JFieldVar;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JOp;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.tools.xjc.generator.ClassContext;
/*     */ import com.sun.tools.xjc.generator.field.AbstractFieldRendererWithVar;
/*     */ import com.sun.tools.xjc.generator.field.FieldRendererFactory;
/*     */ import com.sun.tools.xjc.generator.marshaller.FieldMarshallerGenerator;
/*     */ import com.sun.tools.xjc.generator.util.BlockReference;
/*     */ import com.sun.tools.xjc.grammar.FieldUse;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
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
/*     */ public class XsiNilFieldRenderer
/*     */   extends AbstractFieldRendererWithVar
/*     */ {
/*  36 */   public static final FieldRendererFactory theFactory = (FieldRendererFactory)new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BlockReference onSetEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XsiNilFieldRenderer(ClassContext context, FieldUse fu) {
/*  52 */     super(context, fu);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected JFieldVar generateField() {
/*  58 */     return generateField((JType)this.codeModel.BOOLEAN);
/*     */   }
/*     */   
/*     */   public void generateAccessors() {
/*  62 */     JMethod $get = this.writer.declareMethod((JType)this.codeModel.BOOLEAN, "is" + this.fu.name);
/*  63 */     this.writer.javadoc().appendComment(this.fu.getJavadoc());
/*     */     
/*  65 */     $get.body()._return((JExpression)ref());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     JMethod $set = this.writer.declareMethod((JType)this.codeModel.VOID, "set" + this.fu.name);
/*  75 */     JVar $value = this.writer.addParameter((JType)this.codeModel.BOOLEAN, "value");
/*  76 */     JBlock body = $set.body();
/*  77 */     body.assign((JAssignmentTarget)ref(), (JExpression)$value);
/*     */     
/*  79 */     this.writer.javadoc().appendComment("Passing <code>true</code> will generate xsi:nil in the XML output");
/*     */ 
/*     */     
/*  82 */     this.writer.javadoc().appendComment(this.fu.getJavadoc());
/*     */ 
/*     */     
/*  85 */     this.onSetEvent = (BlockReference)new Object(this, body, $value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JBlock getOnSetEventHandler() {
/*  93 */     return this.onSetEvent.get(true);
/*     */   }
/*     */   
/*     */   public void setter(JBlock block, JExpression newValue) {
/*  97 */     block.assign((JAssignmentTarget)ref(), newValue);
/*     */   }
/*     */   
/*     */   public void toArray(JBlock block, JExpression $array) {
/* 101 */     block.assign((JAssignmentTarget)$array.component(JExpr.lit(0)), (JExpression)ref());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void unsetValues(JBlock body) {
/* 107 */     throw new JAXBAssertionError();
/*     */   }
/*     */   public JExpression hasSetValue() {
/* 110 */     return null;
/*     */   }
/*     */   public JExpression getValue() {
/* 113 */     return this.codeModel.BOOLEAN.wrap((JExpression)ref());
/*     */   }
/*     */   public JClass getValueType() {
/* 116 */     return this.codeModel.BOOLEAN.getWrapperClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public JExpression ifCountEqual(int i) {
/* 121 */     switch (i) { case 0:
/* 122 */         return ref().not();
/* 123 */       case 1: return (JExpression)ref(); }
/* 124 */      return JExpr.FALSE;
/*     */   }
/*     */ 
/*     */   
/*     */   public JExpression ifCountGte(int i) {
/* 129 */     if (i == 1) return (JExpression)ref(); 
/* 130 */     return JExpr.FALSE;
/*     */   }
/*     */   
/*     */   public JExpression ifCountLte(int i) {
/* 134 */     if (i == 0) return ref().not(); 
/* 135 */     return JExpr.TRUE;
/*     */   }
/*     */   
/*     */   public JExpression count() {
/* 139 */     return JOp.cond((JExpression)ref(), JExpr.lit(1), JExpr.lit(0));
/*     */   }
/*     */   
/*     */   public FieldMarshallerGenerator createMarshaller(JBlock block, String uniqueId) {
/* 143 */     return (FieldMarshallerGenerator)new Object(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\field\XsiNilFieldRenderer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */