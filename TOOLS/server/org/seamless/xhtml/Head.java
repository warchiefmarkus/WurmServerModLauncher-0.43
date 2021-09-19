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
/*    */ public class Head
/*    */   extends XHTMLElement
/*    */ {
/*    */   public Head(XPath xpath, Element element) {
/* 26 */     super(xpath, element);
/*    */   }
/*    */   
/*    */   public XHTMLElement getHeadTitle() {
/* 30 */     return (XHTMLElement)this.CHILD_BUILDER.firstChildOrNull(XHTML.ELEMENT.title.name());
/*    */   }
/*    */   
/*    */   public Link[] getLinks() {
/* 34 */     return (Link[])(new DOMElement<XHTMLElement, XHTMLElement>.ArrayBuilder<Link>(this)
/*    */       {
/*    */         public Link build(Element element) {
/* 37 */           return new Link(Head.this.getXpath(), element);
/*    */         }
/*    */ 
/*    */         
/*    */         public Link[] newChildrenArray(int length) {
/* 42 */           return new Link[length];
/*    */         }
/*    */       }).getChildElements(XHTML.ELEMENT.link.name());
/*    */   }
/*    */   
/*    */   public Meta[] getMetas() {
/* 48 */     return (Meta[])(new DOMElement<XHTMLElement, XHTMLElement>.ArrayBuilder<Meta>(this)
/*    */       {
/*    */         public Meta build(Element element) {
/* 51 */           return new Meta(Head.this.getXpath(), element);
/*    */         }
/*    */ 
/*    */         
/*    */         public Meta[] newChildrenArray(int length) {
/* 56 */           return new Meta[length];
/*    */         }
/*    */       }).getChildElements(XHTML.ELEMENT.meta.name());
/*    */   }
/*    */   
/*    */   public XHTMLElement[] getDocumentStyles() {
/* 62 */     return (XHTMLElement[])this.CHILD_BUILDER.getChildElements(XHTML.ELEMENT.style.name());
/*    */   }
/*    */   
/*    */   public XHTMLElement[] getScripts() {
/* 66 */     return (XHTMLElement[])this.CHILD_BUILDER.getChildElements(XHTML.ELEMENT.script.name());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\xhtml\Head.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */