/*     */ package com.sun.org.apache.xml.internal.resolver.tools;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.resolver.Catalog;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogManager;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.FileURL;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Locale;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.xml.sax.AttributeList;
/*     */ import org.xml.sax.DTDHandler;
/*     */ import org.xml.sax.DocumentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.Parser;
/*     */ import org.xml.sax.SAXException;
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
/*     */ public class ResolvingParser
/*     */   implements Parser, DTDHandler, DocumentHandler, EntityResolver
/*     */ {
/*     */   public static boolean namespaceAware = true;
/*     */   public static boolean validating = false;
/*     */   public static boolean suppressExplanation = false;
/*  78 */   private SAXParser saxParser = null;
/*     */ 
/*     */   
/*  81 */   private Parser parser = null;
/*     */ 
/*     */   
/*  84 */   private DocumentHandler documentHandler = null;
/*     */ 
/*     */   
/*  87 */   private DTDHandler dtdHandler = null;
/*     */ 
/*     */   
/*  90 */   private CatalogManager catalogManager = CatalogManager.getStaticManager();
/*     */ 
/*     */   
/*  93 */   private CatalogResolver catalogResolver = null;
/*     */ 
/*     */   
/*  96 */   private CatalogResolver piCatalogResolver = null;
/*     */ 
/*     */   
/*     */   private boolean allowXMLCatalogPI = false;
/*     */ 
/*     */   
/*     */   private boolean oasisXMLCatalogPI = false;
/*     */ 
/*     */   
/* 105 */   private URL baseURL = null;
/*     */ 
/*     */   
/*     */   public ResolvingParser() {
/* 109 */     initParser();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResolvingParser(CatalogManager manager) {
/* 114 */     this.catalogManager = manager;
/* 115 */     initParser();
/*     */   }
/*     */ 
/*     */   
/*     */   private void initParser() {
/* 120 */     this.catalogResolver = new CatalogResolver(this.catalogManager);
/*     */     
/* 122 */     SAXParserFactory spf = SAXParserFactory.newInstance();
/* 123 */     spf.setNamespaceAware(namespaceAware);
/* 124 */     spf.setValidating(validating);
/*     */     
/*     */     try {
/* 127 */       this.saxParser = spf.newSAXParser();
/* 128 */       this.parser = this.saxParser.getParser();
/* 129 */       this.documentHandler = null;
/* 130 */       this.dtdHandler = null;
/* 131 */     } catch (Exception ex) {
/* 132 */       ex.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Catalog getCatalog() {
/* 138 */     return this.catalogResolver.getCatalog();
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
/*     */   public void parse(InputSource input) throws IOException, SAXException {
/* 165 */     setupParse(input.getSystemId());
/*     */     try {
/* 167 */       this.parser.parse(input);
/* 168 */     } catch (InternalError ie) {
/* 169 */       explain(input.getSystemId());
/* 170 */       throw ie;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(String systemId) throws IOException, SAXException {
/* 181 */     setupParse(systemId);
/*     */     try {
/* 183 */       this.parser.parse(systemId);
/* 184 */     } catch (InternalError ie) {
/* 185 */       explain(systemId);
/* 186 */       throw ie;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDocumentHandler(DocumentHandler handler) {
/* 192 */     this.documentHandler = handler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDTDHandler(DTDHandler handler) {
/* 197 */     this.dtdHandler = handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityResolver(EntityResolver resolver) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorHandler(ErrorHandler handler) {
/* 212 */     this.parser.setErrorHandler(handler);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLocale(Locale locale) throws SAXException {
/* 217 */     this.parser.setLocale(locale);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void characters(char[] ch, int start, int length) throws SAXException {
/* 223 */     if (this.documentHandler != null) {
/* 224 */       this.documentHandler.characters(ch, start, length);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void endDocument() throws SAXException {
/* 230 */     if (this.documentHandler != null) {
/* 231 */       this.documentHandler.endDocument();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void endElement(String name) throws SAXException {
/* 237 */     if (this.documentHandler != null) {
/* 238 */       this.documentHandler.endElement(name);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
/* 245 */     if (this.documentHandler != null) {
/* 246 */       this.documentHandler.ignorableWhitespace(ch, start, length);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processingInstruction(String target, String pidata) throws SAXException {
/* 254 */     if (target.equals("oasis-xml-catalog")) {
/* 255 */       URL catalog = null;
/* 256 */       String data = pidata;
/*     */       
/* 258 */       int pos = data.indexOf("catalog=");
/* 259 */       if (pos >= 0) {
/* 260 */         data = data.substring(pos + 8);
/* 261 */         if (data.length() > 1) {
/* 262 */           String quote = data.substring(0, 1);
/* 263 */           data = data.substring(1);
/* 264 */           pos = data.indexOf(quote);
/* 265 */           if (pos >= 0) {
/* 266 */             data = data.substring(0, pos);
/*     */             try {
/* 268 */               if (this.baseURL != null) {
/* 269 */                 catalog = new URL(this.baseURL, data);
/*     */               } else {
/* 271 */                 catalog = new URL(data);
/*     */               } 
/* 273 */             } catch (MalformedURLException mue) {}
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 280 */       if (this.allowXMLCatalogPI) {
/* 281 */         if (this.catalogManager.getAllowOasisXMLCatalogPI()) {
/* 282 */           this.catalogManager.debug.message(4, "oasis-xml-catalog PI", pidata);
/*     */           
/* 284 */           if (catalog != null) {
/*     */             try {
/* 286 */               this.catalogManager.debug.message(4, "oasis-xml-catalog", catalog.toString());
/* 287 */               this.oasisXMLCatalogPI = true;
/*     */               
/* 289 */               if (this.piCatalogResolver == null) {
/* 290 */                 this.piCatalogResolver = new CatalogResolver(true);
/*     */               }
/*     */               
/* 293 */               this.piCatalogResolver.getCatalog().parseCatalog(catalog.toString());
/* 294 */             } catch (Exception e) {
/* 295 */               this.catalogManager.debug.message(3, "Exception parsing oasis-xml-catalog: " + catalog.toString());
/*     */             } 
/*     */           } else {
/*     */             
/* 299 */             this.catalogManager.debug.message(3, "PI oasis-xml-catalog unparseable: " + pidata);
/*     */           } 
/*     */         } else {
/* 302 */           this.catalogManager.debug.message(4, "PI oasis-xml-catalog ignored: " + pidata);
/*     */         } 
/*     */       } else {
/* 305 */         this.catalogManager.debug.message(3, "PI oasis-xml-catalog occurred in an invalid place: " + pidata);
/*     */       }
/*     */     
/*     */     }
/* 309 */     else if (this.documentHandler != null) {
/* 310 */       this.documentHandler.processingInstruction(target, pidata);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/* 317 */     if (this.documentHandler != null) {
/* 318 */       this.documentHandler.setDocumentLocator(locator);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void startDocument() throws SAXException {
/* 324 */     if (this.documentHandler != null) {
/* 325 */       this.documentHandler.startDocument();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startElement(String name, AttributeList atts) throws SAXException {
/* 332 */     this.allowXMLCatalogPI = false;
/* 333 */     if (this.documentHandler != null) {
/* 334 */       this.documentHandler.startElement(name, atts);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void notationDecl(String name, String publicId, String systemId) throws SAXException {
/* 341 */     this.allowXMLCatalogPI = false;
/* 342 */     if (this.dtdHandler != null) {
/* 343 */       this.dtdHandler.notationDecl(name, publicId, systemId);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
/* 353 */     this.allowXMLCatalogPI = false;
/* 354 */     if (this.dtdHandler != null) {
/* 355 */       this.dtdHandler.unparsedEntityDecl(name, publicId, systemId, notationName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputSource resolveEntity(String publicId, String systemId) {
/* 365 */     this.allowXMLCatalogPI = false;
/* 366 */     String resolved = this.catalogResolver.getResolvedEntity(publicId, systemId);
/*     */     
/* 368 */     if (resolved == null && this.piCatalogResolver != null) {
/* 369 */       resolved = this.piCatalogResolver.getResolvedEntity(publicId, systemId);
/*     */     }
/*     */     
/* 372 */     if (resolved != null) {
/*     */       try {
/* 374 */         InputSource iSource = new InputSource(resolved);
/* 375 */         iSource.setPublicId(publicId);
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
/* 388 */         URL url = new URL(resolved);
/* 389 */         InputStream iStream = url.openStream();
/* 390 */         iSource.setByteStream(iStream);
/*     */         
/* 392 */         return iSource;
/* 393 */       } catch (Exception e) {
/* 394 */         this.catalogManager.debug.message(1, "Failed to create InputSource", resolved);
/* 395 */         return null;
/*     */       } 
/*     */     }
/* 398 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void setupParse(String systemId) {
/* 404 */     this.allowXMLCatalogPI = true;
/* 405 */     this.parser.setEntityResolver(this);
/* 406 */     this.parser.setDocumentHandler(this);
/* 407 */     this.parser.setDTDHandler(this);
/*     */     
/* 409 */     URL cwd = null;
/*     */     
/*     */     try {
/* 412 */       cwd = FileURL.makeURL("basename");
/* 413 */     } catch (MalformedURLException mue) {
/* 414 */       cwd = null;
/*     */     } 
/*     */     
/*     */     try {
/* 418 */       this.baseURL = new URL(systemId);
/* 419 */     } catch (MalformedURLException mue) {
/* 420 */       if (cwd != null) {
/*     */         try {
/* 422 */           this.baseURL = new URL(cwd, systemId);
/* 423 */         } catch (MalformedURLException mue2) {
/*     */           
/* 425 */           this.baseURL = null;
/*     */         } 
/*     */       } else {
/*     */         
/* 429 */         this.baseURL = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void explain(String systemId) {
/* 436 */     if (!suppressExplanation) {
/* 437 */       System.out.println("Parser probably encountered bad URI in " + systemId);
/* 438 */       System.out.println("For example, replace '/some/uri' with 'file:/some/uri'.");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\org\apache\xml\internal\resolver\tools\ResolvingParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */