/*    */ package com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSFacet;
/*    */ import com.sun.xml.xsom.XSListSimpleType;
/*    */ import com.sun.xml.xsom.XSSimpleType;
/*    */ import com.sun.xml.xsom.XSVariety;
/*    */ import com.sun.xml.xsom.impl.parser.SchemaDocumentImpl;
/*    */ import com.sun.xml.xsom.visitor.XSSimpleTypeFunction;
/*    */ import com.sun.xml.xsom.visitor.XSSimpleTypeVisitor;
/*    */ import java.util.Set;
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
/*    */ 
/*    */ public class ListSimpleTypeImpl
/*    */   extends SimpleTypeImpl
/*    */   implements XSListSimpleType
/*    */ {
/*    */   private final Ref.SimpleType itemType;
/*    */   
/*    */   public ListSimpleTypeImpl(SchemaDocumentImpl _parent, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _name, boolean _anonymous, Set<XSVariety> finalSet, Ref.SimpleType _itemType) {
/* 40 */     super(_parent, _annon, _loc, _fa, _name, _anonymous, finalSet, (_parent.getSchema()).parent.anySimpleType);
/*    */ 
/*    */     
/* 43 */     this.itemType = _itemType;
/*    */   }
/*    */   
/*    */   public XSSimpleType getItemType() {
/* 47 */     return this.itemType.getType();
/*    */   }
/*    */   public void visit(XSSimpleTypeVisitor visitor) {
/* 50 */     visitor.listSimpleType(this);
/*    */   }
/*    */   public Object apply(XSSimpleTypeFunction function) {
/* 53 */     return function.listSimpleType(this);
/*    */   }
/*    */   
/*    */   public XSFacet getFacet(String name) {
/* 57 */     return null;
/*    */   } public XSVariety getVariety() {
/* 59 */     return XSVariety.LIST;
/*    */   } public XSSimpleType getPrimitiveType() {
/* 61 */     return null;
/*    */   } public XSListSimpleType getBaseListType() {
/* 63 */     return this;
/*    */   }
/* 65 */   public boolean isList() { return true; } public XSListSimpleType asList() {
/* 66 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\ListSimpleTypeImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */