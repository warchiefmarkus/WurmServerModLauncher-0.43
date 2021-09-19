/*    */ package com.sun.tools.xjc.generator.bean;
/*    */ 
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JDefinedClass;
/*    */ import com.sun.tools.xjc.model.CClassInfo;
/*    */ import com.sun.tools.xjc.outline.ClassOutline;
/*    */ import com.sun.tools.xjc.outline.Outline;
/*    */ import com.sun.tools.xjc.outline.PackageOutline;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ClassOutlineImpl
/*    */   extends ClassOutline
/*    */ {
/*    */   private final BeanGenerator _parent;
/*    */   
/*    */   public MethodWriter createMethodWriter() {
/* 55 */     return (this._parent.getModel()).strategy.createMethodWriter(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PackageOutlineImpl _package() {
/* 63 */     return (PackageOutlineImpl)super._package();
/*    */   }
/*    */ 
/*    */   
/*    */   ClassOutlineImpl(BeanGenerator _parent, CClassInfo _target, JDefinedClass exposedClass, JDefinedClass _implClass, JClass _implRef) {
/* 68 */     super(_target, exposedClass, _implRef, _implClass);
/* 69 */     this._parent = _parent;
/* 70 */     (_package()).classes.add(this);
/*    */   }
/*    */   
/*    */   public BeanGenerator parent() {
/* 74 */     return this._parent;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\ClassOutlineImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */