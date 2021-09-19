/*     */ package com.sun.xml.xsom.impl.parser;
/*     */ 
/*     */ import com.sun.xml.xsom.parser.XMLParser;
/*     */ import java.io.IOException;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Parser;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ import org.xml.sax.helpers.XMLReaderAdapter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SAXParserFactoryAdaptor
/*     */   extends SAXParserFactory
/*     */ {
/*     */   private final XMLParser parser;
/*     */   
/*     */   public SAXParserFactoryAdaptor(XMLParser _parser) {
/*  47 */     this.parser = _parser;
/*     */   }
/*     */   
/*     */   public SAXParser newSAXParser() throws ParserConfigurationException, SAXException {
/*  51 */     return new SAXParserImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFeature(String name, boolean value) {}
/*     */ 
/*     */   
/*     */   public boolean getFeature(String name) {
/*  59 */     return false;
/*     */   }
/*     */   
/*     */   private class SAXParserImpl
/*     */     extends SAXParser {
/*  64 */     private final SAXParserFactoryAdaptor.XMLReaderImpl reader = new SAXParserFactoryAdaptor.XMLReaderImpl();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Parser getParser() throws SAXException {
/*  70 */       return new XMLReaderAdapter(this.reader);
/*     */     }
/*     */     
/*     */     public XMLReader getXMLReader() throws SAXException {
/*  74 */       return this.reader;
/*     */     }
/*     */     
/*     */     public boolean isNamespaceAware() {
/*  78 */       return true;
/*     */     }
/*     */     
/*     */     public boolean isValidating() {
/*  82 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setProperty(String name, Object value) {}
/*     */     
/*     */     public Object getProperty(String name) {
/*  89 */       return null;
/*     */     }
/*     */     
/*     */     private SAXParserImpl() {} }
/*     */   
/*     */   private class XMLReaderImpl extends XMLFilterImpl {
/*     */     public void parse(InputSource input) throws IOException, SAXException {
/*  96 */       SAXParserFactoryAdaptor.this.parser.parse(input, this, this, this);
/*     */     }
/*     */     private XMLReaderImpl() {}
/*     */     public void parse(String systemId) throws IOException, SAXException {
/* 100 */       SAXParserFactoryAdaptor.this.parser.parse(new InputSource(systemId), this, this, this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\SAXParserFactoryAdaptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */