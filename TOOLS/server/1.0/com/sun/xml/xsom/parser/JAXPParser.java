/*    */ package 1.0.com.sun.xml.xsom.parser;
/*    */ 
/*    */ import com.sun.xml.xsom.parser.XMLParser;
/*    */ import java.io.IOException;
/*    */ import javax.xml.parsers.ParserConfigurationException;
/*    */ import javax.xml.parsers.SAXParserFactory;
/*    */ import org.xml.sax.ContentHandler;
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
/*    */ import org.xml.sax.XMLReader;
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
/*    */ public class JAXPParser
/*    */   implements XMLParser
/*    */ {
/*    */   private final SAXParserFactory factory;
/*    */   
/*    */   public JAXPParser(SAXParserFactory factory) {
/* 41 */     factory.setNamespaceAware(true);
/* 42 */     this.factory = factory;
/*    */   }
/*    */   
/*    */   public JAXPParser() {
/* 46 */     this(SAXParserFactory.newInstance());
/*    */   }
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
/*    */   public void parse(InputSource source, ContentHandler handler, ErrorHandler errorHandler, EntityResolver entityResolver) throws SAXException, IOException {
/*    */     try {
/* 61 */       XMLReader reader = this.factory.newSAXParser().getXMLReader();
/* 62 */       XMLReaderEx xMLReaderEx = new XMLReaderEx(reader);
/*    */       
/* 64 */       xMLReaderEx.setContentHandler(handler);
/* 65 */       if (errorHandler != null)
/* 66 */         xMLReaderEx.setErrorHandler(errorHandler); 
/* 67 */       if (entityResolver != null)
/* 68 */         xMLReaderEx.setEntityResolver(entityResolver); 
/* 69 */       xMLReaderEx.parse(source);
/* 70 */     } catch (ParserConfigurationException e) {
/*    */       
/* 72 */       SAXParseException spe = new SAXParseException(e.getMessage(), null, e);
/* 73 */       errorHandler.fatalError(spe);
/* 74 */       throw spe;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\parser\JAXPParser.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */