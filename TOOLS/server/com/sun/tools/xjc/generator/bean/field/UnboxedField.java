/*     */ package com.sun.tools.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.JAssignmentTarget;
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JPrimitiveType;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.tools.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.xjc.generator.bean.MethodWriter;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.outline.Aspect;
/*     */ import com.sun.tools.xjc.outline.FieldAccessor;
/*     */ import com.sun.xml.bind.api.impl.NameConverter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnboxedField
/*     */   extends AbstractFieldWithVar
/*     */ {
/*     */   private final JPrimitiveType ptype;
/*     */   
/*     */   protected UnboxedField(ClassOutlineImpl outline, CPropertyInfo prop) {
/*  67 */     super(outline, prop);
/*     */     
/*  69 */     assert this.implType == this.exposedType;
/*     */     
/*  71 */     this.ptype = (JPrimitiveType)this.implType;
/*  72 */     assert this.ptype != null;
/*     */     
/*  74 */     createField();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  80 */     MethodWriter writer = outline.createMethodWriter();
/*  81 */     NameConverter nc = outline.parent().getModel().getNameConverter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     JMethod $get = writer.declareMethod((JType)this.ptype, getGetterMethod());
/*  90 */     String javadoc = prop.javadoc;
/*  91 */     if (javadoc.length() == 0)
/*  92 */       javadoc = Messages.DEFAULT_GETTER_JAVADOC.format(new Object[] { nc.toVariableName(prop.getName(true)) }); 
/*  93 */     writer.javadoc().append(javadoc);
/*     */     
/*  95 */     $get.body()._return((JExpression)ref());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     JMethod $set = writer.declareMethod((JType)this.codeModel.VOID, "set" + prop.getName(true));
/* 103 */     JVar $value = writer.addParameter((JType)this.ptype, "value");
/* 104 */     JBlock body = $set.body();
/* 105 */     body.assign((JAssignmentTarget)JExpr._this().ref((JVar)ref()), (JExpression)$value);
/*     */     
/* 107 */     writer.javadoc().append(Messages.DEFAULT_SETTER_JAVADOC.format(new Object[] { nc.toVariableName(prop.getName(true)) }));
/*     */   }
/*     */ 
/*     */   
/*     */   protected JType getType(Aspect aspect) {
/* 112 */     return (JType)super.getType(aspect).boxify().getPrimitiveType();
/*     */   }
/*     */   
/*     */   protected JType getFieldType() {
/* 116 */     return (JType)this.ptype;
/*     */   }
/*     */   
/*     */   public FieldAccessor create(JExpression targetObject) {
/* 120 */     return new AbstractFieldWithVar.Accessor(targetObject)
/*     */       {
/*     */         public void unsetValues(JBlock body) {}
/*     */ 
/*     */ 
/*     */         
/*     */         public JExpression hasSetValue() {
/* 127 */           return JExpr.TRUE;
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\field\UnboxedField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */