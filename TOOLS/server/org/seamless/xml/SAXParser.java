/*     */ package org.seamless.xml;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.validation.Schema;
/*     */ import javax.xml.validation.SchemaFactory;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ import org.xml.sax.helpers.XMLReaderFactory;
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
/*     */ public class SAXParser
/*     */ {
/*  43 */   private static final Logger log = Logger.getLogger(SAXParser.class.getName());
/*     */   
/*  45 */   public static final URI XML_SCHEMA_NAMESPACE = URI.create("http://www.w3.org/2001/xml.xsd");
/*     */   
/*  47 */   public static final URL XML_SCHEMA_RESOURCE = Thread.currentThread().getContextClassLoader().getResource("org/seamless/schemas/xml.xsd");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SAXParser() {
/*  53 */     this(null);
/*     */   }
/*     */ 
/*     */   
/*  57 */   private final XMLReader xr = create(); public SAXParser(DefaultHandler handler) {
/*  58 */     if (handler != null)
/*  59 */       this.xr.setContentHandler(handler); 
/*     */   }
/*     */   
/*     */   public void setContentHandler(ContentHandler handler) {
/*  63 */     this.xr.setContentHandler(handler);
/*     */   }
/*     */   
/*     */   protected XMLReader create() {
/*     */     try {
/*  68 */       if (getSchemaSources() != null) {
/*     */         
/*  70 */         SAXParserFactory factory = SAXParserFactory.newInstance();
/*  71 */         factory.setNamespaceAware(true);
/*  72 */         factory.setSchema(createSchema(getSchemaSources()));
/*  73 */         XMLReader xmlReader = factory.newSAXParser().getXMLReader();
/*  74 */         xmlReader.setErrorHandler(getErrorHandler());
/*  75 */         return xmlReader;
/*     */       } 
/*  77 */       return XMLReaderFactory.createXMLReader();
/*  78 */     } catch (Exception ex) {
/*  79 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Schema createSchema(Source[] schemaSources) {
/*     */     try {
/*  85 */       SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
/*  86 */       schemaFactory.setResourceResolver(new CatalogResourceResolver(new HashMap<URI, URL>()
/*     */             {
/*     */             
/*     */             }));
/*     */       
/*  91 */       return schemaFactory.newSchema(schemaSources);
/*  92 */     } catch (Exception ex) {
/*  93 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Source[] getSchemaSources() {
/*  98 */     return null;
/*     */   }
/*     */   
/*     */   protected ErrorHandler getErrorHandler() {
/* 102 */     return new SimpleErrorHandler();
/*     */   }
/*     */   
/*     */   public void parse(InputSource source) throws ParserException {
/*     */     try {
/* 107 */       this.xr.parse(source);
/* 108 */     } catch (Exception ex) {
/* 109 */       throw new ParserException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public class SimpleErrorHandler
/*     */     implements ErrorHandler
/*     */   {
/*     */     public void warning(SAXParseException e) throws SAXException {
/* 118 */       throw new SAXException(e);
/*     */     }
/*     */     
/*     */     public void error(SAXParseException e) throws SAXException {
/* 122 */       throw new SAXException(e);
/*     */     }
/*     */     
/*     */     public void fatalError(SAXParseException e) throws SAXException {
/* 126 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Handler<I>
/*     */     extends DefaultHandler
/*     */   {
/*     */     protected SAXParser parser;
/*     */     protected I instance;
/*     */     protected Handler parent;
/* 136 */     protected StringBuilder characters = new StringBuilder();
/*     */     protected Attributes attributes;
/*     */     
/*     */     public Handler(I instance) {
/* 140 */       this(instance, null, null);
/*     */     }
/*     */     
/*     */     public Handler(I instance, SAXParser parser) {
/* 144 */       this(instance, parser, null);
/*     */     }
/*     */     
/*     */     public Handler(I instance, Handler parent) {
/* 148 */       this(instance, parent.getParser(), parent);
/*     */     }
/*     */     
/*     */     public Handler(I instance, SAXParser parser, Handler parent) {
/* 152 */       this.instance = instance;
/* 153 */       this.parser = parser;
/* 154 */       this.parent = parent;
/* 155 */       if (parser != null) {
/* 156 */         parser.setContentHandler(this);
/*     */       }
/*     */     }
/*     */     
/*     */     public I getInstance() {
/* 161 */       return this.instance;
/*     */     }
/*     */     
/*     */     public SAXParser getParser() {
/* 165 */       return this.parser;
/*     */     }
/*     */     
/*     */     public Handler getParent() {
/* 169 */       return this.parent;
/*     */     }
/*     */     
/*     */     protected void switchToParent() {
/* 173 */       if (this.parser != null && this.parent != null) {
/* 174 */         this.parser.setContentHandler(this.parent);
/* 175 */         this.attributes = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     public String getCharacters() {
/* 180 */       return this.characters.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
/* 186 */       this.characters = new StringBuilder();
/* 187 */       this.attributes = new AttributesImpl(attributes);
/* 188 */       SAXParser.log.finer(getClass().getSimpleName() + " starting: " + localName);
/*     */     }
/*     */ 
/*     */     
/*     */     public void characters(char[] ch, int start, int length) throws SAXException {
/* 193 */       this.characters.append(ch, start, length);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void endElement(String uri, String localName, String qName) throws SAXException {
/* 200 */       if (isLastElement(uri, localName, qName)) {
/* 201 */         SAXParser.log.finer(getClass().getSimpleName() + ": last element, switching to parent: " + localName);
/* 202 */         switchToParent();
/*     */         
/*     */         return;
/*     */       } 
/* 206 */       SAXParser.log.finer(getClass().getSimpleName() + " ending: " + localName);
/*     */     }
/*     */     
/*     */     protected boolean isLastElement(String uri, String localName, String qName) {
/* 210 */       return false;
/*     */     }
/*     */     
/*     */     protected Attributes getAttributes() {
/* 214 */       return this.attributes;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\xml\SAXParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */