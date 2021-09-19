/*    */ package org.seamless.xhtml;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import javax.xml.namespace.NamespaceContext;
/*    */ import javax.xml.xpath.XPath;
/*    */ import org.seamless.xml.DOM;
/*    */ import org.seamless.xml.DOMParser;
/*    */ import org.seamless.xml.NamespaceContextMap;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.Node;
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
/*    */ public class XHTMLParser
/*    */   extends DOMParser<XHTML>
/*    */ {
/*    */   public XHTMLParser() {
/* 33 */     super(XHTML.createSchemaSources());
/*    */   }
/*    */ 
/*    */   
/*    */   protected XHTML createDOM(Document document) {
/* 38 */     return (document != null) ? new XHTML(document) : null;
/*    */   }
/*    */   
/*    */   public void checkDuplicateIdentifiers(XHTML document) throws IllegalStateException {
/* 42 */     final Set<String> identifiers = new HashSet<String>();
/* 43 */     accept(document.getW3CDocument().getDocumentElement(), new DOMParser.NodeVisitor((short)1)
/*    */         {
/*    */           public void visit(Node node) {
/* 46 */             Element element = (Element)node;
/*    */             
/* 48 */             String id = element.getAttribute(XHTML.ATTR.id.name());
/* 49 */             if (!"".equals(id)) {
/* 50 */               if (identifiers.contains(id)) {
/* 51 */                 throw new IllegalStateException("Duplicate identifier, override/change value: " + id);
/*    */               }
/* 53 */               identifiers.add(id);
/*    */             } 
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public NamespaceContextMap createDefaultNamespaceContext(String... optionalPrefixes) {
/* 61 */     NamespaceContextMap ctx = new NamespaceContextMap()
/*    */       {
/*    */         protected String getDefaultNamespaceURI() {
/* 64 */           return "http://www.w3.org/1999/xhtml";
/*    */         }
/*    */       };
/* 67 */     for (String optionalPrefix : optionalPrefixes) {
/* 68 */       ctx.put(optionalPrefix, "http://www.w3.org/1999/xhtml");
/*    */     }
/* 70 */     return ctx;
/*    */   }
/*    */   
/*    */   public XPath createXPath() {
/* 74 */     return createXPath((NamespaceContext)createDefaultNamespaceContext(new String[] { "h" }));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\xhtml\XHTMLParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */