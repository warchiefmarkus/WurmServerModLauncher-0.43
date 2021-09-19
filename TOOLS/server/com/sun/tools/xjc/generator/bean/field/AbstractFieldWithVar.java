/*     */ package com.sun.tools.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.JAnnotatable;
/*     */ import com.sun.codemodel.JAssignmentTarget;
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JFieldRef;
/*     */ import com.sun.codemodel.JFieldVar;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.tools.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
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
/*     */ abstract class AbstractFieldWithVar
/*     */   extends AbstractField
/*     */ {
/*     */   private JFieldVar field;
/*     */   
/*     */   AbstractFieldWithVar(ClassOutlineImpl outline, CPropertyInfo prop) {
/*  67 */     super(outline, prop);
/*     */   }
/*     */   
/*     */   protected final void createField() {
/*  71 */     this.field = this.outline.implClass.field(2, getFieldType(), this.prop.getName(false));
/*     */ 
/*     */     
/*  74 */     annotate((JAnnotatable)this.field);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getGetterMethod() {
/*  85 */     return ((getFieldType().boxify().getPrimitiveType() == this.codeModel.BOOLEAN) ? "is" : "get") + this.prop.getName(true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract JType getFieldType();
/*     */ 
/*     */   
/*     */   protected JFieldVar ref() {
/*  93 */     return this.field;
/*     */   }
/*     */   public final JType getRawType() {
/*  96 */     return this.exposedType;
/*     */   }
/*     */   
/*     */   protected abstract class Accessor extends AbstractField.Accessor { protected final JFieldRef $ref;
/*     */     
/*     */     protected Accessor(JExpression $target) {
/* 102 */       super($target);
/* 103 */       this.$ref = $target.ref((JVar)AbstractFieldWithVar.this.ref());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void toRawValue(JBlock block, JVar $var) {
/* 112 */       block.assign((JAssignmentTarget)$var, (JExpression)this.$target.invoke(AbstractFieldWithVar.this.getGetterMethod()));
/*     */     }
/*     */     
/*     */     public final void fromRawValue(JBlock block, String uniqueName, JExpression $var) {
/* 116 */       block.invoke(this.$target, "set" + AbstractFieldWithVar.this.prop.getName(true)).arg($var);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\field\AbstractFieldWithVar.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */