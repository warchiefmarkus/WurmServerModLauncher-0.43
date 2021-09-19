/*    */ package com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSDeclaration;
/*    */ import com.sun.xml.xsom.impl.parser.SchemaDocumentImpl;
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
/*    */ abstract class DeclarationImpl
/*    */   extends ComponentImpl
/*    */   implements XSDeclaration
/*    */ {
/*    */   private final String name;
/*    */   private final String targetNamespace;
/*    */   private final boolean anonymous;
/*    */   
/*    */   DeclarationImpl(SchemaDocumentImpl owner, AnnotationImpl _annon, Locator loc, ForeignAttributesImpl fa, String _targetNamespace, String _name, boolean _anonymous) {
/* 33 */     super(owner, _annon, loc, fa);
/* 34 */     this.targetNamespace = _targetNamespace;
/* 35 */     this.name = _name;
/* 36 */     this.anonymous = _anonymous;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 40 */     return this.name;
/*    */   }
/*    */   public String getTargetNamespace() {
/* 43 */     return this.targetNamespace;
/*    */   }
/*    */   
/*    */   public boolean isAnonymous() {
/* 47 */     return this.anonymous;
/*    */   }
/* 49 */   public final boolean isGlobal() { return !isAnonymous(); } public final boolean isLocal() {
/* 50 */     return isAnonymous();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\DeclarationImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */