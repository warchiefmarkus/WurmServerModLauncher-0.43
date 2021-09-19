/*    */ package org.kohsuke.rngom.xml.sax;
/*    */ 
/*    */ import javax.xml.parsers.ParserConfigurationException;
/*    */ import javax.xml.parsers.SAXParserFactory;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.XMLReader;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JAXPXMLReaderCreator
/*    */   implements XMLReaderCreator
/*    */ {
/*    */   private final SAXParserFactory spf;
/*    */   
/*    */   public JAXPXMLReaderCreator(SAXParserFactory spf) {
/* 21 */     this.spf = spf;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JAXPXMLReaderCreator() {
/* 29 */     this.spf = SAXParserFactory.newInstance();
/* 30 */     this.spf.setNamespaceAware(true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public XMLReader createXMLReader() throws SAXException {
/*    */     try {
/* 38 */       return this.spf.newSAXParser().getXMLReader();
/* 39 */     } catch (ParserConfigurationException e) {
/* 40 */       throw new SAXException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\xml\sax\JAXPXMLReaderCreator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */