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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class DOMOutput
/*    */   extends SAXOutput
/*    */ {
/*    */   private final AssociationMap assoc;
/*    */   
/*    */   public DOMOutput(Node node, AssociationMap assoc) {
/* 58 */     super((ContentHandler)new SAX2DOMEx(node));
/* 59 */     this.assoc = assoc;
/* 60 */     assert assoc != null;
/*    */   }
/*    */   
/*    */   private SAX2DOMEx getBuilder() {
/* 64 */     return (SAX2DOMEx)this.out;
/*    */   }
/*    */   
/*    */   public void endStartTag() throws SAXException {
/* 68 */     super.endStartTag();
/*    */     
/* 70 */     Object op = this.nsContext.getCurrent().getOuterPeer();
/* 71 */     if (op != null) {
/* 72 */       this.assoc.addOuter(getBuilder().getCurrentElement(), op);
/*    */     }
/* 74 */     Object ip = this.nsContext.getCurrent().getInnerPeer();
/* 75 */     if (ip != null)
/* 76 */       this.assoc.addInner(getBuilder().getCurrentElement(), ip); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\output\DOMOutput.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */