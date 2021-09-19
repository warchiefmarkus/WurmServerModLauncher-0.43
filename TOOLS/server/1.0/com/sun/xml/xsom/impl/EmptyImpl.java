/*    */ package 1.0.com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSContentType;
/*    */ import com.sun.xml.xsom.XSParticle;
/*    */ import com.sun.xml.xsom.XSSimpleType;
/*    */ import com.sun.xml.xsom.impl.ComponentImpl;
/*    */ import com.sun.xml.xsom.impl.ContentTypeImpl;
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
/*    */ public class EmptyImpl
/*    */   extends ComponentImpl
/*    */   implements ContentTypeImpl
/*    */ {
/*    */   public EmptyImpl() {
/* 26 */     super(null, null, null);
/*    */   }
/* 28 */   public XSSimpleType asSimpleType() { return null; }
/* 29 */   public XSParticle asParticle() { return null; } public XSContentType asEmpty() {
/* 30 */     return (XSContentType)this;
/*    */   }
/*    */   public Object apply(XSContentTypeFunction function) {
/* 33 */     return function.empty((XSContentType)this);
/*    */   }
/*    */   public Object apply(XSFunction function) {
/* 36 */     return function.empty((XSContentType)this);
/*    */   }
/*    */   public void visit(XSVisitor visitor) {
/* 39 */     visitor.empty((XSContentType)this);
/*    */   }
/*    */   public void visit(XSContentTypeVisitor visitor) {
/* 42 */     visitor.empty((XSContentType)this);
/*    */   }
/*    */   public XSContentType getContentType() {
/* 45 */     return (XSContentType)this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\EmptyImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */