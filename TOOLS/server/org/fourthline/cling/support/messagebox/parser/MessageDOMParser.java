/*    */ package org.fourthline.cling.support.messagebox.parser;
/*    */ 
/*    */ import javax.xml.namespace.NamespaceContext;
/*    */ import javax.xml.xpath.XPath;
/*    */ import org.seamless.xml.DOM;
/*    */ import org.seamless.xml.DOMParser;
/*    */ import org.seamless.xml.NamespaceContextMap;
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
/*    */ public class MessageDOMParser
/*    */   extends DOMParser<MessageDOM>
/*    */ {
/*    */   protected MessageDOM createDOM(Document document) {
/* 31 */     return new MessageDOM(document);
/*    */   }
/*    */   
/*    */   public NamespaceContextMap createDefaultNamespaceContext(String... optionalPrefixes) {
/* 35 */     NamespaceContextMap ctx = new NamespaceContextMap()
/*    */       {
/*    */         protected String getDefaultNamespaceURI() {
/* 38 */           return "urn:samsung-com:messagebox-1-0";
/*    */         }
/*    */       };
/* 41 */     for (String optionalPrefix : optionalPrefixes) {
/* 42 */       ctx.put(optionalPrefix, "urn:samsung-com:messagebox-1-0");
/*    */     }
/* 44 */     return ctx;
/*    */   }
/*    */   
/*    */   public XPath createXPath() {
/* 48 */     return createXPath((NamespaceContext)createDefaultNamespaceContext(new String[] { "m" }));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\messagebox\parser\MessageDOMParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */