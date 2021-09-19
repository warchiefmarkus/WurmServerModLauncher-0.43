/*    */ package 1.0.com.sun.tools.xjc.reader.internalizer;
/*    */ 
/*    */ import com.sun.tools.xjc.reader.internalizer.LocatorTable;
/*    */ import com.sun.xml.bind.marshaller.SAX2DOMEx;
/*    */ import java.util.Set;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.Node;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.Locator;
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
/*    */ class DOMBuilder
/*    */   extends SAX2DOMEx
/*    */ {
/*    */   private final LocatorTable locatorTable;
/*    */   private final Set outerMostBindings;
/*    */   private Locator locator;
/*    */   
/*    */   public DOMBuilder(Document dom, LocatorTable ltable, Set outerMostBindings) {
/* 38 */     super(dom);
/* 39 */     this.locatorTable = ltable;
/* 40 */     this.outerMostBindings = outerMostBindings;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDocumentLocator(Locator locator) {
/* 51 */     this.locator = locator;
/* 52 */     super.setDocumentLocator(locator);
/*    */   }
/*    */ 
/*    */   
/*    */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
/* 57 */     super.startElement(namespaceURI, localName, qName, atts);
/*    */     
/* 59 */     Element e = getCurrentElement();
/* 60 */     this.locatorTable.storeStartLocation(e, this.locator);
/*    */ 
/*    */     
/* 63 */     if ("http://java.sun.com/xml/ns/jaxb".equals(e.getNamespaceURI()) && "bindings".equals(e.getLocalName())) {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 69 */       Node p = e.getParentNode();
/* 70 */       if (p instanceof Document || (p instanceof Element && !e.getNamespaceURI().equals(p.getNamespaceURI())))
/*    */       {
/* 72 */         this.outerMostBindings.add(e);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public void endElement(String namespaceURI, String localName, String qName) {
/* 78 */     this.locatorTable.storeEndLocation(getCurrentElement(), this.locator);
/* 79 */     super.endElement(namespaceURI, localName, qName);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\internalizer\DOMBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */