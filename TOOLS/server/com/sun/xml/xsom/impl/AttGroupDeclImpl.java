/*    */ package com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSAttGroupDecl;
/*    */ import com.sun.xml.xsom.XSAttributeUse;
/*    */ import com.sun.xml.xsom.XSWildcard;
/*    */ import com.sun.xml.xsom.impl.parser.DelayedRef;
/*    */ import com.sun.xml.xsom.impl.parser.SchemaDocumentImpl;
/*    */ import com.sun.xml.xsom.visitor.XSFunction;
/*    */ import com.sun.xml.xsom.visitor.XSVisitor;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
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
/*    */ public class AttGroupDeclImpl
/*    */   extends AttributesHolder
/*    */   implements XSAttGroupDecl
/*    */ {
/*    */   private WildcardImpl wildcard;
/*    */   
/*    */   public AttGroupDeclImpl(SchemaDocumentImpl _parent, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _name, WildcardImpl _wildcard) {
/* 38 */     this(_parent, _annon, _loc, _fa, _name);
/* 39 */     setWildcard(_wildcard);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public AttGroupDeclImpl(SchemaDocumentImpl _parent, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _name) {
/* 45 */     super(_parent, _annon, _loc, _fa, _name, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setWildcard(WildcardImpl wc) {
/* 50 */     this.wildcard = wc; } public XSWildcard getAttributeWildcard() {
/* 51 */     return this.wildcard;
/*    */   }
/*    */   public XSAttributeUse getAttributeUse(String nsURI, String localName) {
/* 54 */     UName name = new UName(nsURI, localName);
/* 55 */     XSAttributeUse o = null;
/*    */     
/* 57 */     Iterator<XSAttGroupDecl> itr = iterateAttGroups();
/* 58 */     while (itr.hasNext() && o == null) {
/* 59 */       o = ((XSAttGroupDecl)itr.next()).getAttributeUse(nsURI, localName);
/*    */     }
/* 61 */     if (o == null) o = this.attributes.get(name);
/*    */     
/* 63 */     return o;
/*    */   }
/*    */   
/*    */   public void redefine(AttGroupDeclImpl ag) {
/* 67 */     for (Iterator<Ref.AttGroup> itr = this.attGroups.iterator(); itr.hasNext(); ) {
/* 68 */       DelayedRef.AttGroup r = (DelayedRef.AttGroup)itr.next();
/* 69 */       r.redefine(ag);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void visit(XSVisitor visitor) {
/* 74 */     visitor.attGroupDecl(this);
/*    */   }
/*    */   public Object apply(XSFunction function) {
/* 77 */     return function.attGroupDecl(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\AttGroupDeclImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */