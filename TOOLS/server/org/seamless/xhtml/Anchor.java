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
/*    */ public class Anchor
/*    */   extends XHTMLElement
/*    */ {
/*    */   public Anchor(XPath xpath, Element element) {
/* 26 */     super(xpath, element);
/*    */   }
/*    */   
/*    */   public String getType() {
/* 30 */     return getAttribute(XHTML.ATTR.type);
/*    */   }
/*    */   
/*    */   public Anchor setType(String type) {
/* 34 */     setAttribute(XHTML.ATTR.type, type);
/* 35 */     return this;
/*    */   }
/*    */   
/*    */   public Href getHref() {
/* 39 */     return Href.fromString(getAttribute(XHTML.ATTR.href));
/*    */   }
/*    */   
/*    */   public Anchor setHref(String href) {
/* 43 */     setAttribute(XHTML.ATTR.href, href);
/* 44 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 49 */     return "(Anchor) " + getAttribute(XHTML.ATTR.href);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\xhtml\Anchor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */