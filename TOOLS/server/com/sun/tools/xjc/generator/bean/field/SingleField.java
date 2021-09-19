/*     */ package com.sun.tools.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.JAssignmentTarget;
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JConditional;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.tools.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.xjc.generator.bean.MethodWriter;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.outline.FieldAccessor;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import com.sun.xml.bind.api.impl.NameConverter;
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
/*     */ public class SingleField
/*     */   extends AbstractFieldWithVar
/*     */ {
/*     */   protected SingleField(ClassOutlineImpl context, CPropertyInfo prop) {
/*  76 */     this(context, prop, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SingleField(ClassOutlineImpl context, CPropertyInfo prop, boolean forcePrimitiveAccess) {
/*  86 */     super(context, prop); JType getterType;
/*  87 */     assert !this.exposedType.isPrimitive() && !this.implType.isPrimitive();
/*     */     
/*  89 */     createField();
/*     */     
/*  91 */     MethodWriter writer = context.createMethodWriter();
/*  92 */     NameConverter nc = context.parent().getModel().getNameConverter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     JExpression defaultValue = null;
/* 103 */     if (prop.defaultValue != null) {
/* 104 */       defaultValue = prop.defaultValue.compute((Outline)this.outline.parent());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 109 */     if (defaultValue != null || forcePrimitiveAccess) {
/* 110 */       getterType = this.exposedType.unboxify();
/*     */     } else {
/* 112 */       getterType = this.exposedType;
/*     */     } 
/* 114 */     JMethod $get = writer.declareMethod(getterType, getGetterMethod());
/* 115 */     String javadoc = prop.javadoc;
/* 116 */     if (javadoc.length() == 0)
/* 117 */       javadoc = Messages.DEFAULT_GETTER_JAVADOC.format(new Object[] { nc.toVariableName(prop.getName(true)) }); 
/* 118 */     writer.javadoc().append(javadoc);
/*     */ 
/*     */     
/* 121 */     if (defaultValue == null) {
/* 122 */       $get.body()._return((JExpression)ref());
/*     */     } else {
/* 124 */       JConditional cond = $get.body()._if(ref().eq(JExpr._null()));
/* 125 */       cond._then()._return(defaultValue);
/* 126 */       cond._else()._return((JExpression)ref());
/*     */     } 
/*     */     
/* 129 */     List<Object> possibleTypes = listPossibleTypes(prop);
/* 130 */     writer.javadoc().addReturn().append("possible object is\n").append(possibleTypes);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     JMethod $set = writer.declareMethod((JType)this.codeModel.VOID, "set" + prop.getName(true));
/* 139 */     JType setterType = this.exposedType;
/* 140 */     if (forcePrimitiveAccess) setterType = setterType.unboxify(); 
/* 141 */     JVar $value = writer.addParameter(setterType, "value");
/* 142 */     JBlock body = $set.body();
/* 143 */     body.assign((JAssignmentTarget)JExpr._this().ref((JVar)ref()), castToImplType((JExpression)$value));
/*     */ 
/*     */     
/* 146 */     writer.javadoc().append(Messages.DEFAULT_SETTER_JAVADOC.format(new Object[] { nc.toVariableName(prop.getName(true)) }));
/* 147 */     writer.javadoc().addParam($value).append("allowed object is\n").append(possibleTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final JType getFieldType() {
/* 153 */     return this.implType;
/*     */   }
/*     */   
/*     */   public FieldAccessor create(JExpression targetObject) {
/* 157 */     return new Accessor(targetObject);
/*     */   }
/*     */   
/*     */   protected class Accessor extends AbstractFieldWithVar.Accessor {
/*     */     protected Accessor(JExpression $target) {
/* 162 */       super($target);
/*     */     }
/*     */     
/*     */     public void unsetValues(JBlock body) {
/* 166 */       body.assign((JAssignmentTarget)this.$ref, JExpr._null());
/*     */     }
/*     */     public JExpression hasSetValue() {
/* 169 */       return this.$ref.ne(JExpr._null());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\field\SingleField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */