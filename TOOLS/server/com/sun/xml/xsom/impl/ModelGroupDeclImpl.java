/*    */ package com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSElementDecl;
/*    */ import com.sun.xml.xsom.XSModelGroup;
/*    */ import com.sun.xml.xsom.XSModelGroupDecl;
/*    */ import com.sun.xml.xsom.XSTerm;
/*    */ import com.sun.xml.xsom.XSWildcard;
/*    */ import com.sun.xml.xsom.impl.parser.SchemaDocumentImpl;
/*    */ import com.sun.xml.xsom.visitor.XSFunction;
/*    */ import com.sun.xml.xsom.visitor.XSTermFunction;
/*    */ import com.sun.xml.xsom.visitor.XSTermFunctionWithParam;
/*    */ import com.sun.xml.xsom.visitor.XSTermVisitor;
/*    */ import com.sun.xml.xsom.visitor.XSVisitor;
/*    */ import org.xml.sax.Locator;
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
/*    */ public class ModelGroupDeclImpl
/*    */   extends DeclarationImpl
/*    */   implements XSModelGroupDecl, Ref.Term
/*    */ {
/*    */   private final ModelGroupImpl modelGroup;
/*    */   
/*    */   public ModelGroupDeclImpl(SchemaDocumentImpl owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _targetNamespace, String _name, ModelGroupImpl _modelGroup) {
/* 42 */     super(owner, _annon, _loc, _fa, _targetNamespace, _name, false);
/* 43 */     this.modelGroup = _modelGroup;
/*    */     
/* 45 */     if (this.modelGroup == null)
/* 46 */       throw new IllegalArgumentException(); 
/*    */   }
/*    */   
/*    */   public XSModelGroup getModelGroup() {
/* 50 */     return this.modelGroup;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void redefine(ModelGroupDeclImpl oldMG) {
/* 57 */     this.modelGroup.redefine(oldMG);
/*    */   }
/*    */ 
/*    */   
/*    */   public void visit(XSVisitor visitor) {
/* 62 */     visitor.modelGroupDecl(this);
/*    */   }
/*    */   public void visit(XSTermVisitor visitor) {
/* 65 */     visitor.modelGroupDecl(this);
/*    */   }
/*    */   public Object apply(XSTermFunction function) {
/* 68 */     return function.modelGroupDecl(this);
/*    */   }
/*    */   
/*    */   public <T, P> T apply(XSTermFunctionWithParam<T, P> function, P param) {
/* 72 */     return (T)function.modelGroupDecl(this, param);
/*    */   }
/*    */   
/*    */   public Object apply(XSFunction function) {
/* 76 */     return function.modelGroupDecl(this);
/*    */   }
/*    */   
/*    */   public boolean isWildcard() {
/* 80 */     return false;
/* 81 */   } public boolean isModelGroupDecl() { return true; }
/* 82 */   public boolean isModelGroup() { return false; } public boolean isElementDecl() {
/* 83 */     return false;
/*    */   }
/* 85 */   public XSWildcard asWildcard() { return null; }
/* 86 */   public XSModelGroupDecl asModelGroupDecl() { return this; }
/* 87 */   public XSModelGroup asModelGroup() { return null; } public XSElementDecl asElementDecl() {
/* 88 */     return null;
/*    */   }
/*    */   
/*    */   public XSTerm getTerm() {
/* 92 */     return (XSTerm)this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\ModelGroupDeclImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */