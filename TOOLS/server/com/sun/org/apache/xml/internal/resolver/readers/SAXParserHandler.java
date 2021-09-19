/*     */ package com.sun.org.apache.xml.internal.resolver.readers;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SAXParserHandler
/*     */   extends DefaultHandler
/*     */ {
/*  39 */   private EntityResolver er = null;
/*  40 */   private ContentHandler ch = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityResolver(EntityResolver er) {
/*  47 */     this.er = er;
/*     */   }
/*     */   
/*     */   public void setContentHandler(ContentHandler ch) {
/*  51 */     this.ch = ch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
/*  58 */     if (this.er != null) {
/*     */       try {
/*  60 */         return this.er.resolveEntity(publicId, systemId);
/*  61 */       } catch (IOException e) {
/*  62 */         System.out.println("resolveEntity threw IOException!");
/*  63 */         return null;
/*     */       } 
/*     */     }
/*  66 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void characters(char[] ch, int start, int length) throws SAXException {
/*  73 */     if (this.ch != null) {
/*  74 */       this.ch.characters(ch, start, length);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void endDocument() throws SAXException {
/*  80 */     if (this.ch != null) {
/*  81 */       this.ch.endDocument();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
/*  87 */     if (this.ch != null) {
/*  88 */       this.ch.endElement(namespaceURI, localName, qName);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/*  94 */     if (this.ch != null) {
/*  95 */       this.ch.endPrefixMapping(prefix);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
/* 101 */     if (this.ch != null) {
/* 102 */       this.ch.ignorableWhitespace(ch, start, length);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void processingInstruction(String target, String data) throws SAXException {
/* 108 */     if (this.ch != null) {
/* 109 */       this.ch.processingInstruction(target, data);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/* 114 */     if (this.ch != null) {
/* 115 */       this.ch.setDocumentLocator(locator);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void skippedEntity(String name) throws SAXException {
/* 121 */     if (this.ch != null) {
/* 122 */       this.ch.skippedEntity(name);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void startDocument() throws SAXException {
/* 128 */     if (this.ch != null) {
/* 129 */       this.ch.startDocument();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/* 136 */     if (this.ch != null) {
/* 137 */       this.ch.startElement(namespaceURI, localName, qName, atts);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException {
/* 143 */     if (this.ch != null)
/* 144 */       this.ch.startPrefixMapping(prefix, uri); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\org\apache\xml\internal\resolver\readers\SAXParserHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */