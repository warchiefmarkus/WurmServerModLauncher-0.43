/*     */ package com.sun.xml.xsom.parser;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.parser.Messages;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
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
/*     */ 
/*     */ public class JAXPParser
/*     */   implements XMLParser
/*     */ {
/*     */   private final SAXParserFactory factory;
/*     */   
/*     */   public JAXPParser(SAXParserFactory factory) {
/*  51 */     factory.setNamespaceAware(true);
/*  52 */     this.factory = factory;
/*     */   }
/*     */   
/*     */   public JAXPParser() {
/*  56 */     this(SAXParserFactory.newInstance());
/*     */   }
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
/*     */   public void parse(InputSource source, ContentHandler handler, ErrorHandler errorHandler, EntityResolver entityResolver) throws SAXException, IOException {
/*     */     try {
/*  71 */       XMLReader reader = this.factory.newSAXParser().getXMLReader();
/*  72 */       reader = new XMLReaderEx(reader);
/*     */       
/*  74 */       reader.setContentHandler(handler);
/*  75 */       if (errorHandler != null)
/*  76 */         reader.setErrorHandler(errorHandler); 
/*  77 */       if (entityResolver != null)
/*  78 */         reader.setEntityResolver(entityResolver); 
/*  79 */       reader.parse(source);
/*  80 */     } catch (ParserConfigurationException e) {
/*     */       
/*  82 */       SAXParseException spe = new SAXParseException(e.getMessage(), null, e);
/*  83 */       errorHandler.fatalError(spe);
/*  84 */       throw spe;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class XMLReaderEx
/*     */     extends XMLFilterImpl
/*     */   {
/*     */     private Locator locator;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     XMLReaderEx(XMLReader parent) {
/* 101 */       setParent(parent);
/*     */     }
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
/*     */     public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
/*     */       try {
/* 128 */         InputSource is = null;
/*     */ 
/*     */         
/* 131 */         if (getEntityResolver() != null)
/* 132 */           is = getEntityResolver().resolveEntity(publicId, systemId); 
/* 133 */         if (is != null) return is;
/*     */ 
/*     */ 
/*     */         
/* 137 */         is = new InputSource((new URL(systemId)).openStream());
/* 138 */         is.setSystemId(systemId);
/* 139 */         is.setPublicId(publicId);
/* 140 */         return is;
/* 141 */       } catch (IOException e) {
/*     */ 
/*     */         
/* 144 */         SAXParseException spe = new SAXParseException(Messages.format("EntityResolutionFailure", new Object[] { systemId, e.toString() }), this.locator, e);
/*     */ 
/*     */ 
/*     */         
/* 148 */         if (getErrorHandler() != null)
/* 149 */           getErrorHandler().fatalError(spe); 
/* 150 */         throw spe;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void setDocumentLocator(Locator locator) {
/* 155 */       super.setDocumentLocator(locator);
/* 156 */       this.locator = locator;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\parser\JAXPParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */