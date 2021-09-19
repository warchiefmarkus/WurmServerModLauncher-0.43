/*    */ package 1.0.com.sun.xml.xsom.impl.parser;
/*    */ 
/*    */ import com.sun.xml.xsom.parser.XMLParser;
/*    */ import javax.xml.parsers.ParserConfigurationException;
/*    */ import javax.xml.parsers.SAXParser;
/*    */ import javax.xml.parsers.SAXParserFactory;
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
/*    */ public class SAXParserFactoryAdaptor
/*    */   extends SAXParserFactory
/*    */ {
/*    */   private final XMLParser parser;
/*    */   
/*    */   public SAXParserFactoryAdaptor(XMLParser _parser) {
/* 39 */     this.parser = _parser;
/*    */   }
/*    */   
/*    */   public SAXParser newSAXParser() throws ParserConfigurationException, SAXException {
/* 43 */     return (SAXParser)new SAXParserImpl(this, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setFeature(String name, boolean value) {}
/*    */ 
/*    */   
/*    */   public boolean getFeature(String name) {
/* 51 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\SAXParserFactoryAdaptor.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */