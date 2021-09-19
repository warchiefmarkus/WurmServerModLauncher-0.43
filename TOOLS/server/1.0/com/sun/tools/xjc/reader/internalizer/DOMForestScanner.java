/*    */ package 1.0.com.sun.tools.xjc.reader.internalizer;
/*    */ 
/*    */ import com.sun.tools.xjc.reader.internalizer.DOMForest;
/*    */ import com.sun.xml.bind.unmarshaller.DOMScanner;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Element;
/*    */ import org.xml.sax.ContentHandler;
/*    */ import org.xml.sax.SAXException;
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
/*    */ public class DOMForestScanner
/*    */ {
/*    */   private final DOMForest forest;
/*    */   
/*    */   public DOMForestScanner(DOMForest _forest) {
/* 42 */     this.forest = _forest;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void scan(Element e, ContentHandler contentHandler) throws SAXException {
/* 50 */     DOMScanner scanner = new DOMScanner();
/*    */ 
/*    */     
/* 53 */     LocationResolver resolver = new LocationResolver(this, scanner);
/* 54 */     resolver.setContentHandler(contentHandler);
/*    */ 
/*    */     
/* 57 */     scanner.parseWithContext(e, (ContentHandler)resolver);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void scan(Document d, ContentHandler contentHandler) throws SAXException {
/* 65 */     scan(d.getDocumentElement(), contentHandler);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\internalizer\DOMForestScanner.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */