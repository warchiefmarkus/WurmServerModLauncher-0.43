/*     */ package com.sun.tools.xjc.reader.xmlschema.parser;
/*     */ 
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IncorrectNamespaceURIChecker
/*     */   extends XMLFilterImpl
/*     */ {
/*     */   private ErrorHandler errorHandler;
/*     */   private Locator locator;
/*     */   private boolean isJAXBPrefixUsed;
/*     */   private boolean isCustomizationUsed;
/*     */   
/*     */   public IncorrectNamespaceURIChecker(ErrorHandler handler) {
/*  84 */     this.locator = null;
/*     */ 
/*     */     
/*  87 */     this.isJAXBPrefixUsed = false;
/*     */     
/*  89 */     this.isCustomizationUsed = false;
/*     */     this.errorHandler = handler;
/*     */   } public void endDocument() throws SAXException {
/*  92 */     if (this.isJAXBPrefixUsed && !this.isCustomizationUsed) {
/*  93 */       SAXParseException e = new SAXParseException(Messages.format("IncorrectNamespaceURIChecker.WarnIncorrectURI", new Object[] { "http://java.sun.com/xml/ns/jaxb" }), this.locator);
/*     */ 
/*     */       
/*  96 */       this.errorHandler.warning(e);
/*     */     } 
/*     */     
/*  99 */     super.endDocument();
/*     */   }
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException {
/* 103 */     if (prefix.equals("jaxb"))
/* 104 */       this.isJAXBPrefixUsed = true; 
/* 105 */     if (uri.equals("http://java.sun.com/xml/ns/jaxb")) {
/* 106 */       this.isCustomizationUsed = true;
/*     */     }
/* 108 */     super.startPrefixMapping(prefix, uri);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/* 113 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     if (namespaceURI.equals("http://java.sun.com/xml/ns/jaxb"))
/* 121 */       this.isCustomizationUsed = true; 
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/* 125 */     super.setDocumentLocator(locator);
/* 126 */     this.locator = locator;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\parser\IncorrectNamespaceURIChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */