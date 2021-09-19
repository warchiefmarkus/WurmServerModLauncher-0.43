/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.parser;
/*    */ 
/*    */ import com.sun.tools.xjc.reader.xmlschema.parser.Messages;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
/*    */ import org.xml.sax.helpers.XMLFilterImpl;
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
/*    */ public class IncorrectNamespaceURIChecker
/*    */   extends XMLFilterImpl
/*    */ {
/*    */   private ErrorHandler errorHandler;
/*    */   private Locator locator;
/*    */   private boolean isJAXBPrefixUsed;
/*    */   private boolean isCustomizationUsed;
/*    */   
/*    */   public IncorrectNamespaceURIChecker(ErrorHandler handler) {
/* 53 */     this.locator = null;
/*    */ 
/*    */     
/* 56 */     this.isJAXBPrefixUsed = false;
/*    */     
/* 58 */     this.isCustomizationUsed = false;
/*    */     this.errorHandler = handler;
/*    */   } public void endDocument() throws SAXException {
/* 61 */     if (this.isJAXBPrefixUsed && !this.isCustomizationUsed) {
/* 62 */       SAXParseException e = new SAXParseException(Messages.format("IncorrectNamespaceURIChecker.WarnIncorrectURI", "http://java.sun.com/xml/ns/jaxb"), this.locator);
/*    */ 
/*    */       
/* 65 */       this.errorHandler.warning(e);
/*    */     } 
/*    */     
/* 68 */     super.endDocument();
/*    */   }
/*    */   
/*    */   public void startPrefixMapping(String prefix, String uri) throws SAXException {
/* 72 */     if (prefix.equals("jaxb"))
/* 73 */       this.isJAXBPrefixUsed = true; 
/* 74 */     if (uri.equals("http://java.sun.com/xml/ns/jaxb")) {
/* 75 */       this.isCustomizationUsed = true;
/*    */     }
/* 77 */     super.startPrefixMapping(prefix, uri);
/*    */   }
/*    */ 
/*    */   
/*    */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/* 82 */     super.startElement(namespaceURI, localName, qName, atts);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 89 */     if (namespaceURI.equals("http://java.sun.com/xml/ns/jaxb"))
/* 90 */       this.isCustomizationUsed = true; 
/*    */   }
/*    */   
/*    */   public void setDocumentLocator(Locator locator) {
/* 94 */     super.setDocumentLocator(locator);
/* 95 */     this.locator = locator;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\parser\IncorrectNamespaceURIChecker.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */