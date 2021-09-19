/*    */ package com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSContentType;
/*    */ import com.sun.xml.xsom.XSParticle;
/*    */ import com.sun.xml.xsom.XSSimpleType;
/*    */ import com.sun.xml.xsom.visitor.XSContentTypeFunction;
/*    */ import com.sun.xml.xsom.visitor.XSContentTypeVisitor;
/*    */ import com.sun.xml.xsom.visitor.XSFunction;
/*    */ import com.sun.xml.xsom.visitor.XSVisitor;
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
/*    */ public class EmptyImpl
/*    */   extends ComponentImpl
/*    */   implements ContentTypeImpl
/*    */ {
/*    */   public EmptyImpl() {
/* 36 */     super(null, null, null, null);
/*    */   }
/* 38 */   public XSSimpleType asSimpleType() { return null; }
/* 39 */   public XSParticle asParticle() { return null; } public XSContentType asEmpty() {
/* 40 */     return this;
/*    */   }
/*    */   public Object apply(XSContentTypeFunction function) {
/* 43 */     return function.empty(this);
/*    */   }
/*    */   public Object apply(XSFunction function) {
/* 46 */     return function.empty(this);
/*    */   }
/*    */   public void visit(XSVisitor visitor) {
/* 49 */     visitor.empty(this);
/*    */   }
/*    */   public void visit(XSContentTypeVisitor visitor) {
/* 52 */     visitor.empty(this);
/*    */   }
/*    */   public XSContentType getContentType() {
/* 55 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\EmptyImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */