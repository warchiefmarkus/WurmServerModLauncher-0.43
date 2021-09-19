/*    */ package 1.0.com.sun.tools.xjc.reader.internalizer;
/*    */ 
/*    */ import com.sun.tools.xjc.reader.internalizer.DOMForest;
/*    */ import com.sun.tools.xjc.reader.internalizer.DOMForestScanner;
/*    */ import com.sun.xml.xsom.parser.XMLParser;
/*    */ import java.io.IOException;
/*    */ import org.w3c.dom.Document;
/*    */ import org.xml.sax.ContentHandler;
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.InputSource;
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
/*    */ class DOMForestParser
/*    */   implements XMLParser
/*    */ {
/*    */   private final DOMForest forest;
/*    */   private final DOMForestScanner scanner;
/*    */   private final XMLParser fallbackParser;
/*    */   
/*    */   DOMForestParser(DOMForest forest, XMLParser fallbackParser) {
/* 43 */     this.forest = forest;
/* 44 */     this.scanner = new DOMForestScanner(forest);
/* 45 */     this.fallbackParser = fallbackParser;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void parse(InputSource source, ContentHandler contentHandler, ErrorHandler errorHandler, EntityResolver entityResolver) throws SAXException, IOException {
/* 55 */     String systemId = source.getSystemId();
/* 56 */     Document dom = this.forest.get(systemId);
/*    */     
/* 58 */     if (dom == null) {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 64 */       this.fallbackParser.parse(source, contentHandler, errorHandler, entityResolver);
/*    */       
/*    */       return;
/*    */     } 
/* 68 */     this.scanner.scan(dom, contentHandler);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\internalizer\DOMForestParser.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */