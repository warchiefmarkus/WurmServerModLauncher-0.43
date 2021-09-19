/*     */ package com.sun.tools.xjc.reader.internalizer;
/*     */ 
/*     */ import com.sun.xml.bind.WhiteSpaceProcessor;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class WhitespaceStripper
/*     */   extends XMLFilterImpl
/*     */ {
/*  64 */   private int state = 0;
/*     */   
/*  66 */   private char[] buf = new char[1024];
/*  67 */   private int bufLen = 0;
/*     */   
/*     */   private static final int AFTER_START_ELEMENT = 1;
/*     */   private static final int AFTER_END_ELEMENT = 2;
/*     */   
/*     */   public WhitespaceStripper(XMLReader reader) {
/*  73 */     setParent(reader);
/*     */   }
/*     */   
/*     */   public WhitespaceStripper(ContentHandler handler, ErrorHandler eh, EntityResolver er) {
/*  77 */     setContentHandler(handler);
/*  78 */     if (eh != null) setErrorHandler(eh); 
/*  79 */     if (er != null) setEntityResolver(er); 
/*     */   } public void characters(char[] ch, int start, int length) throws SAXException {
/*     */     int len;
/*     */     int i;
/*  83 */     switch (this.state) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 1:
/*  89 */         if (this.bufLen + length > this.buf.length) {
/*     */           
/*  91 */           char[] newBuf = new char[Math.max(this.bufLen + length, this.buf.length * 2)];
/*  92 */           System.arraycopy(this.buf, 0, newBuf, 0, this.bufLen);
/*  93 */           this.buf = newBuf;
/*     */         } 
/*  95 */         System.arraycopy(ch, start, this.buf, this.bufLen, length);
/*  96 */         this.bufLen += length;
/*     */         break;
/*     */       
/*     */       case 2:
/* 100 */         len = start + length;
/* 101 */         for (i = start; i < len; i++) {
/* 102 */           if (!WhiteSpaceProcessor.isWhiteSpace(ch[i])) {
/* 103 */             super.characters(ch, start, length);
/*     */             return;
/*     */           } 
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
/* 112 */     processPendingText();
/* 113 */     super.startElement(uri, localName, qName, atts);
/* 114 */     this.state = 1;
/* 115 */     this.bufLen = 0;
/*     */   }
/*     */   
/*     */   public void endElement(String uri, String localName, String qName) throws SAXException {
/* 119 */     processPendingText();
/* 120 */     super.endElement(uri, localName, qName);
/* 121 */     this.state = 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processPendingText() throws SAXException {
/* 129 */     if (this.state == 1)
/* 130 */       for (int i = this.bufLen - 1; i >= 0; i--) {
/* 131 */         if (!WhiteSpaceProcessor.isWhiteSpace(this.buf[i])) {
/* 132 */           super.characters(this.buf, 0, this.bufLen);
/*     */           return;
/*     */         } 
/*     */       }  
/*     */   }
/*     */   
/*     */   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\internalizer\WhitespaceStripper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */