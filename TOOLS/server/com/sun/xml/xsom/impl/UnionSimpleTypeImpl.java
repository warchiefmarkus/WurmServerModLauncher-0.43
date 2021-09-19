/*    */ package com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSFacet;
/*    */ import com.sun.xml.xsom.XSSimpleType;
/*    */ import com.sun.xml.xsom.XSUnionSimpleType;
/*    */ import com.sun.xml.xsom.XSVariety;
/*    */ import com.sun.xml.xsom.impl.parser.SchemaDocumentImpl;
/*    */ import com.sun.xml.xsom.visitor.XSSimpleTypeFunction;
/*    */ import com.sun.xml.xsom.visitor.XSSimpleTypeVisitor;
/*    */ import java.util.Iterator;
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
/*    */ public class UnionSimpleTypeImpl
/*    */   extends SimpleTypeImpl
/*    */   implements XSUnionSimpleType
/*    */ {
/*    */   private final Ref.SimpleType[] memberTypes;
/*    */   
/*    */   public UnionSimpleTypeImpl(SchemaDocumentImpl _parent, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _name, boolean _anonymous, Set<XSVariety> finalSet, Ref.SimpleType[] _members) {
/* 41 */     super(_parent, _annon, _loc, _fa, _name, _anonymous, finalSet, (_parent.getSchema()).parent.anySimpleType);
/*    */ 
/*    */     
/* 44 */     this.memberTypes = _members;
/*    */   }
/*    */   
/*    */   public XSSimpleType getMember(int idx) {
/* 48 */     return this.memberTypes[idx].getType(); } public int getMemberSize() {
/* 49 */     return this.memberTypes.length;
/*    */   }
/*    */   public Iterator<XSSimpleType> iterator() {
/* 52 */     return new Iterator<XSSimpleType>() {
/* 53 */         int idx = 0;
/*    */         public boolean hasNext() {
/* 55 */           return (this.idx < UnionSimpleTypeImpl.this.memberTypes.length);
/*    */         }
/*    */         
/*    */         public XSSimpleType next() {
/* 59 */           return UnionSimpleTypeImpl.this.memberTypes[this.idx++].getType();
/*    */         }
/*    */         
/*    */         public void remove() {
/* 63 */           throw new UnsupportedOperationException();
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public void visit(XSSimpleTypeVisitor visitor) {
/* 69 */     visitor.unionSimpleType(this);
/*    */   }
/*    */   public Object apply(XSSimpleTypeFunction function) {
/* 72 */     return function.unionSimpleType(this);
/*    */   }
/*    */   
/*    */   public XSUnionSimpleType getBaseUnionType() {
/* 76 */     return this;
/*    */   }
/*    */   
/*    */   public XSFacet getFacet(String name) {
/* 80 */     return null;
/*    */   } public XSVariety getVariety() {
/* 82 */     return XSVariety.LIST;
/*    */   } public XSSimpleType getPrimitiveType() {
/* 84 */     return null;
/*    */   }
/* 86 */   public boolean isUnion() { return true; } public XSUnionSimpleType asUnion() {
/* 87 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\UnionSimpleTypeImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */