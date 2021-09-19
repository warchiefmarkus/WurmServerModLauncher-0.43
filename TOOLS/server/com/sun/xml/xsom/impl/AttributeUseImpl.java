/*    */ package com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSAttributeDecl;
/*    */ import com.sun.xml.xsom.XSAttributeUse;
/*    */ import com.sun.xml.xsom.XmlString;
/*    */ import com.sun.xml.xsom.impl.parser.SchemaDocumentImpl;
/*    */ import com.sun.xml.xsom.visitor.XSFunction;
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
/*    */ public class AttributeUseImpl
/*    */   extends ComponentImpl
/*    */   implements XSAttributeUse
/*    */ {
/*    */   private final Ref.Attribute att;
/*    */   private final XmlString defaultValue;
/*    */   private final XmlString fixedValue;
/*    */   private final boolean required;
/*    */   
/*    */   public AttributeUseImpl(SchemaDocumentImpl owner, AnnotationImpl ann, Locator loc, ForeignAttributesImpl fa, Ref.Attribute _decl, XmlString def, XmlString fixed, boolean req) {
/* 35 */     super(owner, ann, loc, fa);
/*    */     
/* 37 */     this.att = _decl;
/* 38 */     this.defaultValue = def;
/* 39 */     this.fixedValue = fixed;
/* 40 */     this.required = req;
/*    */   }
/*    */   
/*    */   public XSAttributeDecl getDecl() {
/* 44 */     return this.att.getAttribute();
/*    */   }
/*    */   
/*    */   public XmlString getDefaultValue() {
/* 48 */     if (this.defaultValue != null) return this.defaultValue; 
/* 49 */     return getDecl().getDefaultValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public XmlString getFixedValue() {
/* 54 */     if (this.fixedValue != null) return this.fixedValue; 
/* 55 */     return getDecl().getFixedValue();
/*    */   }
/*    */   
/*    */   public boolean isRequired() {
/* 59 */     return this.required;
/*    */   }
/*    */   public Object apply(XSFunction f) {
/* 62 */     return f.attributeUse(this);
/*    */   }
/*    */   public void visit(XSVisitor v) {
/* 65 */     v.attributeUse(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\AttributeUseImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */