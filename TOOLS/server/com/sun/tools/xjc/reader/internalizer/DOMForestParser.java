/*    */ package com.sun.tools.xjc.reader.internalizer;
/*    */ 
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
/*    */ class DOMForestParser
/*    */   implements XMLParser
/*    */ {
/*    */   private final DOMForest forest;
/*    */   private final DOMForestScanner scanner;
/*    */   private final XMLParser fallbackParser;
/*    */   
/*    */   DOMForestParser(DOMForest forest, XMLParser fallbackParser) {
/* 74 */     this.forest = forest;
/* 75 */     this.scanner = new DOMForestScanner(forest);
/* 76 */     this.fallbackParser = fallbackParser;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void parse(InputSource source, ContentHandler contentHandler, ErrorHandler errorHandler, EntityResolver entityResolver) throws SAXException, IOException {
/* 86 */     String systemId = source.getSystemId();
/* 87 */     Document dom = this.forest.get(systemId);
/*    */     
/* 89 */     if (dom == null) {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 95 */       this.fallbackParser.parse(source, contentHandler, errorHandler, entityResolver);
/*    */       
/*    */       return;
/*    */     } 
/* 99 */     this.scanner.scan(dom, contentHandler);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\internalizer\DOMForestParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */