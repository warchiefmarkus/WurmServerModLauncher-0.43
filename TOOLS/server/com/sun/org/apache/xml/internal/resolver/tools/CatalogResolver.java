/*     */ package com.sun.org.apache.xml.internal.resolver.tools;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.resolver.Catalog;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogManager;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.FileURL;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.URIResolver;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
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
/*     */ public class CatalogResolver
/*     */   implements EntityResolver, URIResolver
/*     */ {
/*     */   public boolean namespaceAware = true;
/*     */   public boolean validating = false;
/*  70 */   private Catalog catalog = null;
/*     */ 
/*     */   
/*  73 */   private CatalogManager catalogManager = CatalogManager.getStaticManager();
/*     */ 
/*     */   
/*     */   public CatalogResolver() {
/*  77 */     initializeCatalogs(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public CatalogResolver(boolean privateCatalog) {
/*  82 */     initializeCatalogs(privateCatalog);
/*     */   }
/*     */ 
/*     */   
/*     */   public CatalogResolver(CatalogManager manager) {
/*  87 */     this.catalogManager = manager;
/*  88 */     initializeCatalogs(!this.catalogManager.getUseStaticCatalog());
/*     */   }
/*     */ 
/*     */   
/*     */   private void initializeCatalogs(boolean privateCatalog) {
/*  93 */     this.catalog = this.catalogManager.getCatalog();
/*     */   }
/*     */ 
/*     */   
/*     */   public Catalog getCatalog() {
/*  98 */     return this.catalog;
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
/*     */   public String getResolvedEntity(String publicId, String systemId) {
/* 122 */     String resolved = null;
/*     */     
/* 124 */     if (this.catalog == null) {
/* 125 */       this.catalogManager.debug.message(1, "Catalog resolution attempted with null catalog; ignored");
/* 126 */       return null;
/*     */     } 
/*     */     
/* 129 */     if (systemId != null) {
/*     */       try {
/* 131 */         resolved = this.catalog.resolveSystem(systemId);
/* 132 */       } catch (MalformedURLException me) {
/* 133 */         this.catalogManager.debug.message(1, "Malformed URL exception trying to resolve", publicId);
/*     */         
/* 135 */         resolved = null;
/* 136 */       } catch (IOException ie) {
/* 137 */         this.catalogManager.debug.message(1, "I/O exception trying to resolve", publicId);
/* 138 */         resolved = null;
/*     */       } 
/*     */     }
/*     */     
/* 142 */     if (resolved == null) {
/* 143 */       if (publicId != null) {
/*     */         try {
/* 145 */           resolved = this.catalog.resolvePublic(publicId, systemId);
/* 146 */         } catch (MalformedURLException me) {
/* 147 */           this.catalogManager.debug.message(1, "Malformed URL exception trying to resolve", publicId);
/*     */         }
/* 149 */         catch (IOException ie) {
/* 150 */           this.catalogManager.debug.message(1, "I/O exception trying to resolve", publicId);
/*     */         } 
/*     */       }
/*     */       
/* 154 */       if (resolved != null) {
/* 155 */         this.catalogManager.debug.message(2, "Resolved public", publicId, resolved);
/*     */       }
/*     */     } else {
/* 158 */       this.catalogManager.debug.message(2, "Resolved system", systemId, resolved);
/*     */     } 
/*     */     
/* 161 */     return resolved;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputSource resolveEntity(String publicId, String systemId) {
/* 192 */     String resolved = getResolvedEntity(publicId, systemId);
/*     */     
/* 194 */     if (resolved != null) {
/*     */       try {
/* 196 */         InputSource iSource = new InputSource(resolved);
/* 197 */         iSource.setPublicId(publicId);
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
/* 210 */         URL url = new URL(resolved);
/* 211 */         InputStream iStream = url.openStream();
/* 212 */         iSource.setByteStream(iStream);
/*     */         
/* 214 */         return iSource;
/* 215 */       } catch (Exception e) {
/* 216 */         this.catalogManager.debug.message(1, "Failed to create InputSource", resolved);
/* 217 */         return null;
/*     */       } 
/*     */     }
/*     */     
/* 221 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Source resolve(String href, String base) throws TransformerException {
/* 228 */     String uri = href;
/* 229 */     String fragment = null;
/* 230 */     int hashPos = href.indexOf("#");
/* 231 */     if (hashPos >= 0) {
/* 232 */       uri = href.substring(0, hashPos);
/* 233 */       fragment = href.substring(hashPos + 1);
/*     */     } 
/*     */     
/* 236 */     String result = null;
/*     */     
/*     */     try {
/* 239 */       result = this.catalog.resolveURI(href);
/* 240 */     } catch (Exception e) {}
/*     */ 
/*     */ 
/*     */     
/* 244 */     if (result == null) {
/*     */       try {
/* 246 */         URL url = null;
/*     */         
/* 248 */         if (base == null) {
/* 249 */           url = new URL(uri);
/* 250 */           result = url.toString();
/*     */         } else {
/* 252 */           URL baseURL = new URL(base);
/* 253 */           url = (href.length() == 0) ? baseURL : new URL(baseURL, uri);
/* 254 */           result = url.toString();
/*     */         } 
/* 256 */       } catch (MalformedURLException mue) {
/*     */         
/* 258 */         String absBase = makeAbsolute(base);
/* 259 */         if (!absBase.equals(base))
/*     */         {
/* 261 */           return resolve(href, absBase);
/*     */         }
/* 263 */         throw new TransformerException("Malformed URL " + href + "(base " + base + ")", mue);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 270 */     this.catalogManager.debug.message(2, "Resolved URI", href, result);
/*     */     
/* 272 */     SAXSource source = new SAXSource();
/* 273 */     source.setInputSource(new InputSource(result));
/* 274 */     setEntityResolver(source);
/* 275 */     return source;
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
/*     */   private void setEntityResolver(SAXSource source) throws TransformerException {
/* 300 */     XMLReader reader = source.getXMLReader();
/* 301 */     if (reader == null) {
/* 302 */       SAXParserFactory spFactory = SAXParserFactory.newInstance();
/* 303 */       spFactory.setNamespaceAware(true);
/*     */       try {
/* 305 */         reader = spFactory.newSAXParser().getXMLReader();
/*     */       }
/* 307 */       catch (ParserConfigurationException ex) {
/* 308 */         throw new TransformerException(ex);
/*     */       }
/* 310 */       catch (SAXException ex) {
/* 311 */         throw new TransformerException(ex);
/*     */       } 
/*     */     } 
/* 314 */     reader.setEntityResolver(this);
/* 315 */     source.setXMLReader(reader);
/*     */   }
/*     */ 
/*     */   
/*     */   private String makeAbsolute(String uri) {
/* 320 */     if (uri == null) {
/* 321 */       uri = "";
/*     */     }
/*     */     
/*     */     try {
/* 325 */       URL url = new URL(uri);
/* 326 */       return url.toString();
/* 327 */     } catch (MalformedURLException mue) {
/*     */       try {
/* 329 */         URL fileURL = FileURL.makeURL(uri);
/* 330 */         return fileURL.toString();
/* 331 */       } catch (MalformedURLException mue2) {
/*     */         
/* 333 */         return uri;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\org\apache\xml\internal\resolver\tools\CatalogResolver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */