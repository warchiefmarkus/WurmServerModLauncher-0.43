/*     */ package com.sun.tools.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.JAnnotatable;
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JFieldVar;
/*     */ import com.sun.codemodel.JPrimitiveType;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.tools.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.outline.FieldAccessor;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ConstField
/*     */   extends AbstractField
/*     */ {
/*     */   private final JFieldVar $ref;
/*     */   
/*     */   ConstField(ClassOutlineImpl outline, CPropertyInfo prop) {
/*  69 */     super(outline, prop);
/*     */ 
/*     */     
/*  72 */     assert !prop.isCollection();
/*     */     
/*  74 */     JPrimitiveType ptype = this.implType.boxify().getPrimitiveType();
/*     */ 
/*     */     
/*  77 */     JExpression defaultValue = null;
/*  78 */     if (prop.defaultValue != null) {
/*  79 */       defaultValue = prop.defaultValue.compute((Outline)outline.parent());
/*     */     }
/*  81 */     this.$ref = outline.ref.field(25, (ptype != null) ? (JType)ptype : this.implType, prop.getName(true), defaultValue);
/*     */     
/*  83 */     this.$ref.javadoc().append(prop.javadoc);
/*     */     
/*  85 */     annotate((JAnnotatable)this.$ref);
/*     */   }
/*     */ 
/*     */   
/*     */   public JType getRawType() {
/*  90 */     return this.exposedType;
/*     */   }
/*     */ 
/*     */   
/*     */   public FieldAccessor create(JExpression target) {
/*  95 */     return new Accessor(target);
/*     */   }
/*     */   
/*     */   private class Accessor
/*     */     extends AbstractField.Accessor {
/*     */     Accessor(JExpression $target) {
/* 101 */       super($target);
/*     */     }
/*     */ 
/*     */     
/*     */     public void unsetValues(JBlock body) {}
/*     */     
/*     */     public JExpression hasSetValue() {
/* 108 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void toRawValue(JBlock block, JVar $var) {
/* 113 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void fromRawValue(JBlock block, String uniqueName, JExpression $var) {
/* 117 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\field\ConstField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */