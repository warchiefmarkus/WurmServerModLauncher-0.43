/*    */ package com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSAttributeDecl;
/*    */ import com.sun.xml.xsom.XSSimpleType;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AttributeDeclImpl
/*    */   extends DeclarationImpl
/*    */   implements XSAttributeDecl, Ref.Attribute
/*    */ {
/*    */   private final Ref.SimpleType type;
/*    */   private final XmlString defaultValue;
/*    */   private final XmlString fixedValue;
/*    */   
/*    */   public AttributeDeclImpl(SchemaDocumentImpl owner, String _targetNamespace, String _name, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, boolean _anonymous, XmlString _defValue, XmlString _fixedValue, Ref.SimpleType _type) {
/* 38 */     super(owner, _annon, _loc, _fa, _targetNamespace, _name, _anonymous);
/*    */     
/* 40 */     if (_name == null) {
/* 41 */       throw new IllegalArgumentException();
/*    */     }
/* 43 */     this.defaultValue = _defValue;
/* 44 */     this.fixedValue = _fixedValue;
/* 45 */     this.type = _type;
/*    */   }
/*    */   
/*    */   public XSSimpleType getType() {
/* 49 */     return this.type.getType();
/*    */   }
/*    */   public XmlString getDefaultValue() {
/* 52 */     return this.defaultValue;
/*    */   }
/*    */   public XmlString getFixedValue() {
/* 55 */     return this.fixedValue;
/*    */   }
/*    */   public void visit(XSVisitor visitor) {
/* 58 */     visitor.attributeDecl(this);
/*    */   }
/*    */   public Object apply(XSFunction function) {
/* 61 */     return function.attributeDecl(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public XSAttributeDecl getAttribute() {
/* 66 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\AttributeDeclImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */