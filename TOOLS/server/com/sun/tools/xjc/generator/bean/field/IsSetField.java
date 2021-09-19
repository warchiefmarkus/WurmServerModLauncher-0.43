/*     */ package com.sun.tools.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.tools.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.xjc.generator.bean.MethodWriter;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.outline.FieldAccessor;
/*     */ import com.sun.tools.xjc.outline.FieldOutline;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IsSetField
/*     */   extends AbstractField
/*     */ {
/*     */   private final FieldOutline core;
/*     */   private final boolean generateUnSetMethod;
/*     */   private final boolean generateIsSetMethod;
/*     */   
/*     */   protected IsSetField(ClassOutlineImpl outline, CPropertyInfo prop, FieldOutline core, boolean unsetMethod, boolean issetMethod) {
/*  65 */     super(outline, prop);
/*  66 */     this.core = core;
/*  67 */     this.generateIsSetMethod = issetMethod;
/*  68 */     this.generateUnSetMethod = unsetMethod;
/*     */     
/*  70 */     generate(outline, prop);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void generate(ClassOutlineImpl outline, CPropertyInfo prop) {
/*  76 */     MethodWriter writer = outline.createMethodWriter();
/*     */     
/*  78 */     JCodeModel codeModel = outline.parent().getCodeModel();
/*     */     
/*  80 */     FieldAccessor acc = this.core.create(JExpr._this());
/*     */     
/*  82 */     if (this.generateIsSetMethod) {
/*     */       
/*  84 */       JExpression hasSetValue = acc.hasSetValue();
/*  85 */       if (hasSetValue == null)
/*     */       {
/*     */         
/*  88 */         throw new UnsupportedOperationException();
/*     */       }
/*  90 */       writer.declareMethod((JType)codeModel.BOOLEAN, "isSet" + this.prop.getName(true)).body()._return(hasSetValue);
/*     */     } 
/*     */ 
/*     */     
/*  94 */     if (this.generateUnSetMethod)
/*     */     {
/*  96 */       acc.unsetValues(writer.declareMethod((JType)codeModel.VOID, "unset" + this.prop.getName(true)).body());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public JType getRawType() {
/* 102 */     return this.core.getRawType();
/*     */   }
/*     */   
/*     */   public FieldAccessor create(JExpression targetObject) {
/* 106 */     return new Accessor(targetObject);
/*     */   }
/*     */   
/*     */   private class Accessor
/*     */     extends AbstractField.Accessor {
/*     */     private final FieldAccessor core;
/*     */     
/*     */     Accessor(JExpression $target) {
/* 114 */       super($target);
/* 115 */       this.core = IsSetField.this.core.create($target);
/*     */     }
/*     */ 
/*     */     
/*     */     public void unsetValues(JBlock body) {
/* 120 */       this.core.unsetValues(body);
/*     */     }
/*     */     public JExpression hasSetValue() {
/* 123 */       return this.core.hasSetValue();
/*     */     }
/*     */     public void toRawValue(JBlock block, JVar $var) {
/* 126 */       this.core.toRawValue(block, $var);
/*     */     }
/*     */     
/*     */     public void fromRawValue(JBlock block, String uniqueName, JExpression $var) {
/* 130 */       this.core.fromRawValue(block, uniqueName, $var);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\field\IsSetField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */