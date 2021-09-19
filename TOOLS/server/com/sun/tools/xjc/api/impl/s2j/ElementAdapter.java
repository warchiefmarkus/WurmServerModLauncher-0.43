/*     */ package com.sun.tools.xjc.api.impl.s2j;
/*     */ 
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JInvocation;
/*     */ import com.sun.tools.xjc.model.CElementInfo;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.outline.ClassOutline;
/*     */ import com.sun.tools.xjc.outline.FieldAccessor;
/*     */ import com.sun.tools.xjc.outline.FieldOutline;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class ElementAdapter
/*     */   implements FieldOutline
/*     */ {
/*     */   protected final FieldOutline core;
/*     */   protected final CElementInfo ei;
/*     */   
/*     */   public ElementAdapter(FieldOutline core, CElementInfo ei) {
/*  85 */     this.core = core;
/*  86 */     this.ei = ei;
/*     */   }
/*     */   
/*     */   public ClassOutline parent() {
/*  90 */     return this.core.parent();
/*     */   }
/*     */   
/*     */   public CPropertyInfo getPropertyInfo() {
/*  94 */     return this.core.getPropertyInfo();
/*     */   }
/*     */   
/*     */   protected final Outline outline() {
/*  98 */     return this.core.parent().parent();
/*     */   }
/*     */   
/*     */   protected final JCodeModel codeModel() {
/* 102 */     return outline().getCodeModel();
/*     */   }
/*     */   
/*     */   protected abstract class FieldAccessorImpl implements FieldAccessor {
/*     */     final FieldAccessor acc;
/*     */     
/*     */     public FieldAccessorImpl(JExpression target) {
/* 109 */       this.acc = ElementAdapter.this.core.create(target);
/*     */     }
/*     */     
/*     */     public void unsetValues(JBlock body) {
/* 113 */       this.acc.unsetValues(body);
/*     */     }
/*     */     
/*     */     public JExpression hasSetValue() {
/* 117 */       return this.acc.hasSetValue();
/*     */     }
/*     */     
/*     */     public FieldOutline owner() {
/* 121 */       return ElementAdapter.this;
/*     */     }
/*     */     
/*     */     public CPropertyInfo getPropertyInfo() {
/* 125 */       return ElementAdapter.this.core.getPropertyInfo();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected final JInvocation createJAXBElement(JExpression $var) {
/* 132 */       JCodeModel cm = ElementAdapter.this.codeModel();
/*     */       
/* 134 */       return JExpr._new(cm.ref(JAXBElement.class)).arg((JExpression)JExpr._new(cm.ref(QName.class)).arg(ElementAdapter.this.ei.getElementName().getNamespaceURI()).arg(ElementAdapter.this.ei.getElementName().getLocalPart())).arg(ElementAdapter.this.getRawType().boxify().erasure().dotclass()).arg($var);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\impl\s2j\ElementAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */