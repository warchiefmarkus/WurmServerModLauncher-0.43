/*     */ package org.seamless.xml;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
/*     */ import org.w3c.dom.ls.LSInput;
/*     */ import org.w3c.dom.ls.LSResourceResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CatalogResourceResolver
/*     */   implements LSResourceResolver
/*     */ {
/*  40 */   private static Logger log = Logger.getLogger(CatalogResourceResolver.class.getName());
/*     */   
/*     */   private final Map<URI, URL> catalog;
/*     */   
/*     */   public CatalogResourceResolver(Map<URI, URL> catalog) {
/*  45 */     this.catalog = catalog;
/*     */   }
/*     */   
/*     */   public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
/*  49 */     log.finest("Trying to resolve system identifier URI in catalog: " + systemId);
/*     */     URL systemURL;
/*  51 */     if ((systemURL = this.catalog.get(URI.create(systemId))) != null) {
/*  52 */       log.finest("Loading catalog resource: " + systemURL);
/*     */       try {
/*  54 */         Input i = new Input(systemURL.openStream());
/*  55 */         i.setBaseURI(baseURI);
/*  56 */         i.setSystemId(systemId);
/*  57 */         i.setPublicId(publicId);
/*  58 */         return i;
/*  59 */       } catch (Exception ex) {
/*  60 */         throw new RuntimeException(ex);
/*     */       } 
/*     */     } 
/*  63 */     log.info("System identifier not found in catalog, continuing with default resolution (this most likely means remote HTTP request!): " + systemId);
/*     */ 
/*     */ 
/*     */     
/*  67 */     return null;
/*     */   }
/*     */   
/*     */   private static final class Input
/*     */     implements LSInput
/*     */   {
/*     */     InputStream in;
/*     */     
/*     */     public Input(InputStream in) {
/*  76 */       this.in = in;
/*     */     }
/*     */     
/*     */     public Reader getCharacterStream() {
/*  80 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setCharacterStream(Reader characterStream) {}
/*     */     
/*     */     public InputStream getByteStream() {
/*  87 */       return this.in;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setByteStream(InputStream byteStream) {}
/*     */     
/*     */     public String getStringData() {
/*  94 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setStringData(String stringData) {}
/*     */     
/*     */     public String getSystemId() {
/* 101 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSystemId(String systemId) {}
/*     */     
/*     */     public String getPublicId() {
/* 108 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setPublicId(String publicId) {}
/*     */     
/*     */     public String getBaseURI() {
/* 115 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setBaseURI(String baseURI) {}
/*     */     
/*     */     public String getEncoding() {
/* 122 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setEncoding(String encoding) {}
/*     */     
/*     */     public boolean getCertifiedText() {
/* 129 */       return false;
/*     */     }
/*     */     
/*     */     public void setCertifiedText(boolean certifiedText) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\xml\CatalogResourceResolver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */