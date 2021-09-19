/*    */ package com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSFacet;
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
/*    */ public class FacetImpl
/*    */   extends ComponentImpl
/*    */   implements XSFacet
/*    */ {
/*    */   private final String name;
/*    */   private final XmlString value;
/*    */   private boolean fixed;
/*    */   
/*    */   public FacetImpl(SchemaDocumentImpl owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _name, XmlString _value, boolean _fixed) {
/* 33 */     super(owner, _annon, _loc, _fa);
/*    */     
/* 35 */     this.name = _name;
/* 36 */     this.value = _value;
/* 37 */     this.fixed = _fixed;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 41 */     return this.name;
/*    */   }
/*    */   public XmlString getValue() {
/* 44 */     return this.value;
/*    */   }
/*    */   public boolean isFixed() {
/* 47 */     return this.fixed;
/*    */   }
/*    */   
/*    */   public void visit(XSVisitor visitor) {
/* 51 */     visitor.facet(this);
/*    */   }
/*    */   public Object apply(XSFunction function) {
/* 54 */     return function.facet(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\FacetImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */