/*     */ package com.sun.tools.xjc.util;
/*     */ 
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
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
/*     */ public abstract class SubtreeCutter
/*     */   extends XMLFilterImpl
/*     */ {
/*  58 */   private int cutDepth = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   private static final ContentHandler stub = new DefaultHandler();
/*     */ 
/*     */ 
/*     */   
/*     */   private ContentHandler next;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startDocument() throws SAXException {
/*  75 */     this.cutDepth = 0;
/*  76 */     super.startDocument();
/*     */   }
/*     */   
/*     */   public boolean isCutting() {
/*  80 */     return (this.cutDepth > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startCutting() {
/*  90 */     super.setContentHandler(stub);
/*  91 */     this.cutDepth = 1;
/*     */   }
/*     */   
/*     */   public void setContentHandler(ContentHandler handler) {
/*  95 */     this.next = handler;
/*     */     
/*  97 */     if (getContentHandler() != stub)
/*  98 */       super.setContentHandler(handler); 
/*     */   }
/*     */   
/*     */   public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
/* 102 */     if (this.cutDepth > 0)
/* 103 */       this.cutDepth++; 
/* 104 */     super.startElement(uri, localName, qName, atts);
/*     */   }
/*     */   
/*     */   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
/* 108 */     super.endElement(namespaceURI, localName, qName);
/*     */     
/* 110 */     if (this.cutDepth != 0) {
/* 111 */       this.cutDepth--;
/* 112 */       if (this.cutDepth == 1) {
/*     */         
/* 114 */         super.setContentHandler(this.next);
/* 115 */         this.cutDepth = 0;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xj\\util\SubtreeCutter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */