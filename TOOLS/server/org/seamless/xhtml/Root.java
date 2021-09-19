/*    */ package org.seamless.xhtml;
/*    */ 
/*    */ import javax.xml.xpath.XPath;
/*    */ import org.seamless.xml.DOMElement;
/*    */ import org.w3c.dom.Element;
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
/*    */ public class Root
/*    */   extends XHTMLElement
/*    */ {
/*    */   public Root(XPath xpath, Element element) {
/* 26 */     super(xpath, element);
/*    */   }
/*    */   
/*    */   public Head getHead() {
/* 30 */     return (Head)(new DOMElement<XHTMLElement, XHTMLElement>.Builder<Head>(this)
/*    */       {
/*    */         public Head build(Element element) {
/* 33 */           return new Head(Root.this.getXpath(), element);
/*    */         }
/*    */       }).firstChildOrNull(XHTML.ELEMENT.head.name());
/*    */   }
/*    */   
/*    */   public Body getBody() {
/* 39 */     return (Body)(new DOMElement<XHTMLElement, XHTMLElement>.Builder<Body>(this)
/*    */       {
/*    */         public Body build(Element element) {
/* 42 */           return new Body(Root.this.getXpath(), element);
/*    */         }
/*    */       }).firstChildOrNull(XHTML.ELEMENT.body.name());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\xhtml\Root.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */