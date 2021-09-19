/*     */ package com.sun.tools.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.JAnnotatable;
/*     */ import com.sun.codemodel.JAssignmentTarget;
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JFieldRef;
/*     */ import com.sun.codemodel.JFieldVar;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JOp;
/*     */ import com.sun.codemodel.JPrimitiveType;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.tools.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractListField
/*     */   extends AbstractField
/*     */ {
/*     */   protected JFieldVar field;
/*     */   private JMethod internalGetter;
/*     */   protected final JPrimitiveType primitiveType;
/*  89 */   protected final JClass listT = this.codeModel.ref(List.class).narrow(this.exposedType.boxify());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean eagerInstanciation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractListField(ClassOutlineImpl outline, CPropertyInfo prop, boolean eagerInstanciation) {
/* 105 */     super(outline, prop);
/* 106 */     this.eagerInstanciation = eagerInstanciation;
/*     */     
/* 108 */     if (this.implType instanceof JPrimitiveType) {
/*     */       
/* 110 */       assert this.implType == this.exposedType;
/* 111 */       this.primitiveType = (JPrimitiveType)this.implType;
/*     */     } else {
/* 113 */       this.primitiveType = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void generate() {
/* 120 */     this.field = this.outline.implClass.field(2, (JType)this.listT, this.prop.getName(false));
/* 121 */     if (this.eagerInstanciation) {
/* 122 */       this.field.init(newCoreList());
/*     */     }
/* 124 */     annotate((JAnnotatable)this.field);
/*     */ 
/*     */     
/* 127 */     generateAccessors();
/*     */   }
/*     */   
/*     */   private void generateInternalGetter() {
/* 131 */     this.internalGetter = this.outline.implClass.method(2, (JType)this.listT, "_get" + this.prop.getName(true));
/* 132 */     if (!this.eagerInstanciation)
/*     */     {
/* 134 */       fixNullRef(this.internalGetter.body());
/*     */     }
/* 136 */     this.internalGetter.body()._return((JExpression)this.field);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void fixNullRef(JBlock block) {
/* 146 */     block._if(this.field.eq(JExpr._null()))._then().assign((JAssignmentTarget)this.field, newCoreList());
/*     */   }
/*     */ 
/*     */   
/*     */   public final JType getRawType() {
/* 151 */     return (JType)this.codeModel.ref(List.class).narrow(this.exposedType.boxify());
/*     */   }
/*     */   
/*     */   private JExpression newCoreList() {
/* 155 */     return (JExpression)JExpr._new(getCoreListType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract JClass getCoreListType();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void generateAccessors();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract class Accessor
/*     */     extends AbstractField.Accessor
/*     */   {
/*     */     protected final JFieldRef field;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Accessor(JExpression $target) {
/* 185 */       super($target);
/* 186 */       this.field = $target.ref((JVar)AbstractListField.this.field);
/*     */     }
/*     */ 
/*     */     
/*     */     protected final JExpression unbox(JExpression exp) {
/* 191 */       if (AbstractListField.this.primitiveType == null) return exp; 
/* 192 */       return AbstractListField.this.primitiveType.unwrap(exp);
/*     */     }
/*     */     protected final JExpression box(JExpression exp) {
/* 195 */       if (AbstractListField.this.primitiveType == null) return exp; 
/* 196 */       return AbstractListField.this.primitiveType.wrap(exp);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected final JExpression ref(boolean canBeNull) {
/* 216 */       if (canBeNull)
/* 217 */         return (JExpression)this.field; 
/* 218 */       if (AbstractListField.this.internalGetter == null)
/* 219 */         AbstractListField.this.generateInternalGetter(); 
/* 220 */       return (JExpression)this.$target.invoke(AbstractListField.this.internalGetter);
/*     */     }
/*     */     
/*     */     public JExpression count() {
/* 224 */       return JOp.cond(this.field.eq(JExpr._null()), JExpr.lit(0), (JExpression)this.field.invoke("size"));
/*     */     }
/*     */     
/*     */     public void unsetValues(JBlock body) {
/* 228 */       body.assign((JAssignmentTarget)this.field, JExpr._null());
/*     */     }
/*     */     public JExpression hasSetValue() {
/* 231 */       return this.field.ne(JExpr._null()).cand(this.field.invoke("isEmpty").not());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\field\AbstractListField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */