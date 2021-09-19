/*     */ package 1.0.com.sun.tools.xjc.generator.field;
/*     */ 
/*     */ import com.sun.codemodel.JAssignmentTarget;
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JFieldVar;
/*     */ import com.sun.codemodel.JInvocation;
/*     */ import com.sun.codemodel.JPrimitiveType;
/*     */ import com.sun.codemodel.JStatement;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.msv.grammar.ExpressionVisitorVoid;
/*     */ import com.sun.tools.xjc.generator.ClassContext;
/*     */ import com.sun.tools.xjc.generator.field.FieldRenderer;
/*     */ import com.sun.tools.xjc.generator.field.FieldRendererFactory;
/*     */ import com.sun.tools.xjc.generator.marshaller.FieldMarshallerGenerator;
/*     */ import com.sun.tools.xjc.grammar.FieldItem;
/*     */ import com.sun.tools.xjc.grammar.FieldUse;
/*     */ import java.util.ArrayList;
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
/*     */ public final class ConstFieldRenderer
/*     */   implements FieldRenderer
/*     */ {
/*     */   private final JCodeModel codeModel;
/*     */   private boolean isCollection = false;
/*     */   private final FieldUse use;
/*     */   private JFieldVar $ref;
/*     */   private int count;
/*  68 */   public static final FieldRendererFactory theFactory = (FieldRendererFactory)new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstFieldRenderer(ClassContext context, FieldUse _use) {
/*  76 */     this.use = _use;
/*  77 */     this.codeModel = this.use.codeModel;
/*     */     
/*  79 */     JExpression initializer = calcInitializer();
/*     */     
/*  81 */     this.$ref = context.ref.field(25, this.isCollection ? (JType)getType().array() : getType(), this.use.name, initializer);
/*     */ 
/*     */     
/*  84 */     this.$ref.javadoc().appendComment(this.use.getJavadoc());
/*     */   }
/*     */ 
/*     */   
/*     */   public void generate() {}
/*     */ 
/*     */   
/*     */   public JBlock getOnSetEventHandler() {
/*  92 */     return JBlock.dummyInstance;
/*     */   }
/*     */ 
/*     */   
/*     */   public void toArray(JBlock block, JExpression $array) {
/*  97 */     if (this.isCollection) {
/*  98 */       block.add((JStatement)this.codeModel.ref(System.class).staticInvoke("arraycopy").arg((JExpression)this.$ref).arg(JExpr.lit(0)).arg($array).arg(JExpr.lit(0)).arg((JExpression)this.$ref.ref("length")));
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 103 */       block.assign((JAssignmentTarget)$array.component(JExpr.lit(0)), (JExpression)this.$ref);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void unsetValues(JBlock body) {}
/*     */   
/*     */   public JExpression hasSetValue() {
/* 110 */     return null;
/*     */   }
/*     */   public JExpression getValue() {
/* 113 */     return (JExpression)this.$ref;
/*     */   }
/*     */   public JClass getValueType() {
/* 116 */     if (this.isCollection) return getType().array(); 
/* 117 */     if (getType().isPrimitive()) return ((JPrimitiveType)getType()).getWrapperClass(); 
/* 118 */     return (JClass)getType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JType getType() {
/* 125 */     return this.use.type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldUse getFieldUse() {
/* 131 */     return this.use;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setter(JBlock body, JExpression newValue) {}
/*     */ 
/*     */   
/*     */   public JExpression ifCountEqual(int i) {
/* 139 */     if (i == this.count) return JExpr.TRUE; 
/* 140 */     return JExpr.FALSE;
/*     */   }
/*     */   public JExpression ifCountGte(int i) {
/* 143 */     if (i <= this.count) return JExpr.TRUE; 
/* 144 */     return JExpr.FALSE;
/*     */   }
/*     */   public JExpression ifCountLte(int i) {
/* 147 */     if (i >= this.count) return JExpr.TRUE; 
/* 148 */     return JExpr.FALSE;
/*     */   }
/*     */   public JExpression count() {
/* 151 */     return JExpr.lit(this.count);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JExpression calcInitializer() {
/* 158 */     FieldItem[] items = this.use.getItems();
/* 159 */     ArrayList result = new ArrayList();
/*     */ 
/*     */ 
/*     */     
/* 163 */     (items[0]).exp.visit((ExpressionVisitorVoid)new Object(this, result));
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
/* 186 */     this.count = result.size();
/*     */     
/* 188 */     if (!this.isCollection)
/*     */     {
/* 190 */       return result.get(0);
/*     */     }
/*     */     
/* 193 */     JInvocation inv = JExpr._new(getType().array());
/* 194 */     for (int i = 0; i < result.size(); i++)
/* 195 */       inv.arg(result.get(i)); 
/* 196 */     return (JExpression)inv;
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
/*     */   public FieldMarshallerGenerator createMarshaller(JBlock block, String uniqueId) {
/* 259 */     if (!this.isCollection) {
/* 260 */       return (FieldMarshallerGenerator)new SingleFMGImpl(this, null);
/*     */     }
/*     */     
/* 263 */     JVar $idx = block.decl((JType)this.codeModel.INT, "idx" + uniqueId, JExpr.lit(0));
/*     */     
/* 265 */     return (FieldMarshallerGenerator)new CollectionFMGImpl(this, $idx);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\field\ConstFieldRenderer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */