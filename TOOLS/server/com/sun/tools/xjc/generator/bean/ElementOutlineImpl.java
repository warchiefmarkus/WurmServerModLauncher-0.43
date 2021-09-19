/*     */ package com.sun.tools.xjc.generator.bean;
/*     */ 
/*     */ import com.sun.codemodel.JCast;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JInvocation;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.tools.xjc.model.CElementInfo;
/*     */ import com.sun.tools.xjc.outline.Aspect;
/*     */ import com.sun.tools.xjc.outline.ElementOutline;
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
/*     */ final class ElementOutlineImpl
/*     */   extends ElementOutline
/*     */ {
/*     */   private final BeanGenerator parent;
/*     */   
/*     */   public BeanGenerator parent() {
/*  63 */     return this.parent;
/*     */   }
/*     */   
/*     */   ElementOutlineImpl(BeanGenerator parent, CElementInfo ei) {
/*  67 */     super(ei, parent.getClassFactory().createClass(parent.getContainer(ei.parent, Aspect.EXPOSED), ei.shortName(), ei.getLocator()));
/*     */ 
/*     */     
/*  70 */     this.parent = parent;
/*  71 */     parent.elements.put(ei, this);
/*     */     
/*  73 */     JCodeModel cm = parent.getCodeModel();
/*     */     
/*  75 */     this.implClass._extends(cm.ref(JAXBElement.class).narrow(this.target.getContentInMemoryType().toType(parent, Aspect.EXPOSED).boxify()));
/*     */ 
/*     */ 
/*     */     
/*  79 */     if (ei.hasClass()) {
/*  80 */       JType implType = ei.getContentInMemoryType().toType(parent, Aspect.IMPLEMENTATION);
/*  81 */       JCast jCast = JExpr.cast((JType)cm.ref(Class.class), implType.boxify().dotclass());
/*  82 */       JClass scope = null;
/*  83 */       if (ei.getScope() != null)
/*  84 */         scope = (parent.getClazz(ei.getScope())).implRef; 
/*  85 */       JExpression scopeClass = (scope == null) ? JExpr._null() : scope.dotclass();
/*     */ 
/*     */       
/*  88 */       JMethod cons = this.implClass.constructor(1);
/*  89 */       cons.body().invoke("super").arg((JExpression)this.implClass.field(26, QName.class, "NAME", (JExpression)createQName(cm, ei.getElementName()))).arg((JExpression)jCast).arg(scopeClass).arg((JExpression)cons.param(implType, "value"));
/*     */     } 
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
/*     */   private JInvocation createQName(JCodeModel codeModel, QName name) {
/* 102 */     return JExpr._new(codeModel.ref(QName.class)).arg(name.getNamespaceURI()).arg(name.getLocalPart());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\ElementOutlineImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */