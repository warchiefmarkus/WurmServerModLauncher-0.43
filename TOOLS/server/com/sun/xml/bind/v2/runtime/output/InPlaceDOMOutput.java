/*    */ package com.sun.xml.bind.v2.runtime.output;
/*    */ 
/*    */ import com.sun.xml.bind.marshaller.SAX2DOMEx;
/*    */ import com.sun.xml.bind.v2.runtime.AssociationMap;
/*    */ import org.w3c.dom.Node;
/*    */ import org.xml.sax.ContentHandler;
/*    */ import org.xml.sax.SAXException;
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
/*    */ public final class InPlaceDOMOutput
/*    */   extends SAXOutput
/*    */ {
/*    */   private final AssociationMap assoc;
/*    */   
/*    */   public InPlaceDOMOutput(Node node, AssociationMap assoc) {
/* 53 */     super((ContentHandler)new SAX2DOMEx(node));
/* 54 */     this.assoc = assoc;
/* 55 */     assert assoc != null;
/*    */   }
/*    */   
/*    */   private SAX2DOMEx getBuilder() {
/* 59 */     return (SAX2DOMEx)this.out;
/*    */   }
/*    */   
/*    */   public void endStartTag() throws SAXException {
/* 63 */     super.endStartTag();
/*    */     
/* 65 */     Object op = this.nsContext.getCurrent().getOuterPeer();
/* 66 */     if (op != null) {
/* 67 */       this.assoc.addOuter(getBuilder().getCurrentElement(), op);
/*    */     }
/* 69 */     Object ip = this.nsContext.getCurrent().getInnerPeer();
/* 70 */     if (ip != null)
/* 71 */       this.assoc.addInner(getBuilder().getCurrentElement(), ip); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\output\InPlaceDOMOutput.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */