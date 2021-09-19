/*     */ package com.sun.org.apache.xml.internal.resolver.tools;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.resolver.Catalog;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogManager;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.FileURL;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
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
/*     */ public class ResolvingXMLFilter
/*     */   extends XMLFilterImpl
/*     */ {
/*     */   public static boolean suppressExplanation = false;
/*  63 */   private CatalogManager catalogManager = CatalogManager.getStaticManager();
/*     */ 
/*     */   
/*  66 */   private CatalogResolver catalogResolver = null;
/*     */ 
/*     */   
/*  69 */   private CatalogResolver piCatalogResolver = null;
/*     */ 
/*     */   
/*     */   private boolean allowXMLCatalogPI = false;
/*     */ 
/*     */   
/*     */   private boolean oasisXMLCatalogPI = false;
/*     */ 
/*     */   
/*  78 */   private URL baseURL = null;
/*     */ 
/*     */ 
/*     */   
/*     */   public ResolvingXMLFilter() {
/*  83 */     this.catalogResolver = new CatalogResolver(this.catalogManager);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResolvingXMLFilter(XMLReader parent) {
/*  88 */     super(parent);
/*  89 */     this.catalogResolver = new CatalogResolver(this.catalogManager);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ResolvingXMLFilter(CatalogManager manager) {
/*  95 */     this.catalogManager = manager;
/*  96 */     this.catalogResolver = new CatalogResolver(this.catalogManager);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResolvingXMLFilter(XMLReader parent, CatalogManager manager) {
/* 101 */     super(parent);
/* 102 */     this.catalogManager = manager;
/* 103 */     this.catalogResolver = new CatalogResolver(this.catalogManager);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Catalog getCatalog() {
/* 110 */     return this.catalogResolver.getCatalog();
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
/*     */   public void parse(InputSource input) throws IOException, SAXException {
/* 136 */     this.allowXMLCatalogPI = true;
/*     */     
/* 138 */     setupBaseURI(input.getSystemId());
/*     */     
/*     */     try {
/* 141 */       super.parse(input);
/* 142 */     } catch (InternalError ie) {
/* 143 */       explain(input.getSystemId());
/* 144 */       throw ie;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(String systemId) throws IOException, SAXException {
/* 154 */     this.allowXMLCatalogPI = true;
/*     */     
/* 156 */     setupBaseURI(systemId);
/*     */     
/*     */     try {
/* 159 */       super.parse(systemId);
/* 160 */     } catch (InternalError ie) {
/* 161 */       explain(systemId);
/* 162 */       throw ie;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputSource resolveEntity(String publicId, String systemId) {
/* 172 */     this.allowXMLCatalogPI = false;
/* 173 */     String resolved = this.catalogResolver.getResolvedEntity(publicId, systemId);
/*     */     
/* 175 */     if (resolved == null && this.piCatalogResolver != null) {
/* 176 */       resolved = this.piCatalogResolver.getResolvedEntity(publicId, systemId);
/*     */     }
/*     */     
/* 179 */     if (resolved != null) {
/*     */       try {
/* 181 */         InputSource iSource = new InputSource(resolved);
/* 182 */         iSource.setPublicId(publicId);
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
/* 195 */         URL url = new URL(resolved);
/* 196 */         InputStream iStream = url.openStream();
/* 197 */         iSource.setByteStream(iStream);
/*     */         
/* 199 */         return iSource;
/* 200 */       } catch (Exception e) {
/* 201 */         this.catalogManager.debug.message(1, "Failed to create InputSource", resolved);
/* 202 */         return null;
/*     */       } 
/*     */     }
/* 205 */     return null;
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
/*     */   public void notationDecl(String name, String publicId, String systemId) throws SAXException {
/* 217 */     this.allowXMLCatalogPI = false;
/* 218 */     super.notationDecl(name, publicId, systemId);
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
/*     */   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
/* 232 */     this.allowXMLCatalogPI = false;
/* 233 */     super.unparsedEntityDecl(name, publicId, systemId, notationName);
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
/*     */   public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
/* 245 */     this.allowXMLCatalogPI = false;
/* 246 */     super.startElement(uri, localName, qName, atts);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processingInstruction(String target, String pidata) throws SAXException {
/* 255 */     if (target.equals("oasis-xml-catalog")) {
/* 256 */       URL catalog = null;
/* 257 */       String data = pidata;
/*     */       
/* 259 */       int pos = data.indexOf("catalog=");
/* 260 */       if (pos >= 0) {
/* 261 */         data = data.substring(pos + 8);
/* 262 */         if (data.length() > 1) {
/* 263 */           String quote = data.substring(0, 1);
/* 264 */           data = data.substring(1);
/* 265 */           pos = data.indexOf(quote);
/* 266 */           if (pos >= 0) {
/* 267 */             data = data.substring(0, pos);
/*     */             try {
/* 269 */               if (this.baseURL != null) {
/* 270 */                 catalog = new URL(this.baseURL, data);
/*     */               } else {
/* 272 */                 catalog = new URL(data);
/*     */               } 
/* 274 */             } catch (MalformedURLException mue) {}
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 281 */       if (this.allowXMLCatalogPI) {
/* 282 */         if (this.catalogManager.getAllowOasisXMLCatalogPI()) {
/* 283 */           this.catalogManager.debug.message(4, "oasis-xml-catalog PI", pidata);
/*     */           
/* 285 */           if (catalog != null) {
/*     */             try {
/* 287 */               this.catalogManager.debug.message(4, "oasis-xml-catalog", catalog.toString());
/* 288 */               this.oasisXMLCatalogPI = true;
/*     */               
/* 290 */               if (this.piCatalogResolver == null) {
/* 291 */                 this.piCatalogResolver = new CatalogResolver(true);
/*     */               }
/*     */               
/* 294 */               this.piCatalogResolver.getCatalog().parseCatalog(catalog.toString());
/* 295 */             } catch (Exception e) {
/* 296 */               this.catalogManager.debug.message(3, "Exception parsing oasis-xml-catalog: " + catalog.toString());
/*     */             } 
/*     */           } else {
/*     */             
/* 300 */             this.catalogManager.debug.message(3, "PI oasis-xml-catalog unparseable: " + pidata);
/*     */           } 
/*     */         } else {
/* 303 */           this.catalogManager.debug.message(4, "PI oasis-xml-catalog ignored: " + pidata);
/*     */         } 
/*     */       } else {
/* 306 */         this.catalogManager.debug.message(3, "PI oasis-xml-catalog occurred in an invalid place: " + pidata);
/*     */       } 
/*     */     } else {
/*     */       
/* 310 */       super.processingInstruction(target, pidata);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void setupBaseURI(String systemId) {
/* 316 */     URL cwd = null;
/*     */     
/*     */     try {
/* 319 */       cwd = FileURL.makeURL("basename");
/* 320 */     } catch (MalformedURLException mue) {
/* 321 */       cwd = null;
/*     */     } 
/*     */     
/*     */     try {
/* 325 */       this.baseURL = new URL(systemId);
/* 326 */     } catch (MalformedURLException mue) {
/* 327 */       if (cwd != null) {
/*     */         try {
/* 329 */           this.baseURL = new URL(cwd, systemId);
/* 330 */         } catch (MalformedURLException mue2) {
/*     */           
/* 332 */           this.baseURL = null;
/*     */         } 
/*     */       } else {
/*     */         
/* 336 */         this.baseURL = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void explain(String systemId) {
/* 343 */     if (!suppressExplanation) {
/* 344 */       System.out.println("XMLReader probably encountered bad URI in " + systemId);
/* 345 */       System.out.println("For example, replace '/some/uri' with 'file:/some/uri'.");
/*     */     } 
/* 347 */     suppressExplanation = true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\org\apache\xml\internal\resolver\tools\ResolvingXMLFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */