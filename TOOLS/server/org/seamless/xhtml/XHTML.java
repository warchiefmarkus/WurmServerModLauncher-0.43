/*    */ package org.seamless.xhtml;
/*    */ 
/*    */ import javax.xml.transform.Source;
/*    */ import javax.xml.transform.stream.StreamSource;
/*    */ import javax.xml.xpath.XPath;
/*    */ import org.seamless.xml.DOM;
/*    */ import org.seamless.xml.DOMElement;
/*    */ import org.w3c.dom.Document;
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
/*    */ public class XHTML
/*    */   extends DOM
/*    */ {
/*    */   public static final String NAMESPACE_URI = "http://www.w3.org/1999/xhtml";
/*    */   public static final String SCHEMA_RESOURCE = "org/seamless/schemas/xhtml1-strict.xsd";
/*    */   
/*    */   public static Source[] createSchemaSources() {
/* 35 */     return new Source[] { new StreamSource(XHTML.class.getClassLoader().getResourceAsStream("org/seamless/schemas/xhtml1-strict.xsd")) };
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public enum ELEMENT
/*    */   {
/* 42 */     html, head, title, meta, link, script, style,
/* 43 */     body, div, span, p, object, a, img, pre,
/* 44 */     h1, h2, h3, h4, h5, h6,
/* 45 */     table, thead, tfoot, tbody, tr, th, td,
/* 46 */     ul, ol, li, dl, dt, dd,
/* 47 */     form, input, select, option;
/*    */   }
/*    */   
/*    */   public enum ATTR {
/* 51 */     id, style, title,
/* 52 */     type, href, name, content, scheme, rel, rev,
/* 53 */     colspan, rowspan, src, alt,
/* 54 */     action, method;
/*    */     
/*    */     public static final String CLASS = "class";
/*    */   }
/*    */   
/*    */   public XHTML(Document dom) {
/* 60 */     super(dom);
/*    */   }
/*    */   
/*    */   public Root createRoot(XPath xpath, ELEMENT elememt) {
/* 64 */     createRoot(elememt.name());
/* 65 */     return getRoot(xpath);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRootElementNamespace() {
/* 70 */     return "http://www.w3.org/1999/xhtml";
/*    */   }
/*    */ 
/*    */   
/*    */   public Root getRoot(XPath xpath) {
/* 75 */     return new Root(xpath, getW3CDocument().getDocumentElement());
/*    */   }
/*    */ 
/*    */   
/*    */   public XHTML copy() {
/* 80 */     return new XHTML((Document)getW3CDocument().cloneNode(true));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\xhtml\XHTML.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */