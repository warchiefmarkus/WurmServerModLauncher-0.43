/*     */ package com.sun.tools.xjc.util;
/*     */ 
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
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
/*     */ public class ForkContentHandler
/*     */   implements ContentHandler
/*     */ {
/*     */   private final ContentHandler lhs;
/*     */   private final ContentHandler rhs;
/*     */   
/*     */   public ForkContentHandler(ContentHandler first, ContentHandler second) {
/*  64 */     this.lhs = first;
/*  65 */     this.rhs = second;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContentHandler create(ContentHandler[] handlers) {
/*  73 */     if (handlers.length == 0) {
/*  74 */       throw new IllegalArgumentException();
/*     */     }
/*  76 */     ContentHandler result = handlers[0];
/*  77 */     for (int i = 1; i < handlers.length; i++)
/*  78 */       result = new ForkContentHandler(result, handlers[i]); 
/*  79 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/*  86 */     this.lhs.setDocumentLocator(locator);
/*  87 */     this.rhs.setDocumentLocator(locator);
/*     */   }
/*     */   
/*     */   public void startDocument() throws SAXException {
/*  91 */     this.lhs.startDocument();
/*  92 */     this.rhs.startDocument();
/*     */   }
/*     */   
/*     */   public void endDocument() throws SAXException {
/*  96 */     this.lhs.endDocument();
/*  97 */     this.rhs.endDocument();
/*     */   }
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException {
/* 101 */     this.lhs.startPrefixMapping(prefix, uri);
/* 102 */     this.rhs.startPrefixMapping(prefix, uri);
/*     */   }
/*     */   
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/* 106 */     this.lhs.endPrefixMapping(prefix);
/* 107 */     this.rhs.endPrefixMapping(prefix);
/*     */   }
/*     */   
/*     */   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
/* 111 */     this.lhs.startElement(uri, localName, qName, attributes);
/* 112 */     this.rhs.startElement(uri, localName, qName, attributes);
/*     */   }
/*     */   
/*     */   public void endElement(String uri, String localName, String qName) throws SAXException {
/* 116 */     this.lhs.endElement(uri, localName, qName);
/* 117 */     this.rhs.endElement(uri, localName, qName);
/*     */   }
/*     */   
/*     */   public void characters(char[] ch, int start, int length) throws SAXException {
/* 121 */     this.lhs.characters(ch, start, length);
/* 122 */     this.rhs.characters(ch, start, length);
/*     */   }
/*     */   
/*     */   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
/* 126 */     this.lhs.ignorableWhitespace(ch, start, length);
/* 127 */     this.rhs.ignorableWhitespace(ch, start, length);
/*     */   }
/*     */   
/*     */   public void processingInstruction(String target, String data) throws SAXException {
/* 131 */     this.lhs.processingInstruction(target, data);
/* 132 */     this.rhs.processingInstruction(target, data);
/*     */   }
/*     */   
/*     */   public void skippedEntity(String name) throws SAXException {
/* 136 */     this.lhs.skippedEntity(name);
/* 137 */     this.rhs.skippedEntity(name);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xj\\util\ForkContentHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */