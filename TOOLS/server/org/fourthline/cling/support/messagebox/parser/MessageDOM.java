/*    */ package org.fourthline.cling.support.messagebox.parser;
/*    */ 
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
/*    */ public class MessageDOM
/*    */   extends DOM
/*    */ {
/*    */   public static final String NAMESPACE_URI = "urn:samsung-com:messagebox-1-0";
/*    */   
/*    */   public MessageDOM(Document dom) {
/* 31 */     super(dom);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRootElementNamespace() {
/* 36 */     return "urn:samsung-com:messagebox-1-0";
/*    */   }
/*    */ 
/*    */   
/*    */   public MessageElement getRoot(XPath xPath) {
/* 41 */     return new MessageElement(xPath, getW3CDocument().getDocumentElement());
/*    */   }
/*    */ 
/*    */   
/*    */   public MessageDOM copy() {
/* 46 */     return new MessageDOM((Document)getW3CDocument().cloneNode(true));
/*    */   }
/*    */   
/*    */   public MessageElement createRoot(XPath xpath, String element) {
/* 50 */     createRoot(element);
/* 51 */     return getRoot(xpath);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\messagebox\parser\MessageDOM.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */