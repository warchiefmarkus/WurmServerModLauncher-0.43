/*    */ package org.seamless.xhtml;
/*    */ 
/*    */ import javax.xml.xpath.XPath;
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
/*    */ 
/*    */ public class Link
/*    */   extends XHTMLElement
/*    */ {
/*    */   public Link(XPath xpath, Element element) {
/* 26 */     super(xpath, element);
/*    */   }
/*    */   
/*    */   public Href getHref() {
/* 30 */     return Href.fromString(getAttribute(XHTML.ATTR.href));
/*    */   }
/*    */   
/*    */   public String getRel() {
/* 34 */     return getAttribute(XHTML.ATTR.rel);
/*    */   }
/*    */   
/*    */   public String getRev() {
/* 38 */     return getAttribute(XHTML.ATTR.rev);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\xhtml\Link.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */