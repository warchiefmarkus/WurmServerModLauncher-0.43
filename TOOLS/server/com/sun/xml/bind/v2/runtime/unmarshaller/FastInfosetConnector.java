/*     */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.bind.WhiteSpaceProcessor;
/*     */ import com.sun.xml.fastinfoset.stax.StAXDocumentParser;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.Attributes;
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
/*     */ 
/*     */ final class FastInfosetConnector
/*     */   extends StAXConnector
/*     */ {
/*     */   private final StAXDocumentParser fastInfosetStreamReader;
/*     */   private boolean textReported;
/*  65 */   private final Base64Data base64Data = new Base64Data();
/*     */ 
/*     */   
/*  68 */   private final StringBuilder buffer = new StringBuilder();
/*     */   private final CharSequenceImpl charArray;
/*     */   
/*     */   public FastInfosetConnector(StAXDocumentParser fastInfosetStreamReader, XmlVisitor visitor) {
/*  72 */     super(visitor);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 249 */     this.charArray = new CharSequenceImpl(); fastInfosetStreamReader.setStringInterning(true); this.fastInfosetStreamReader = fastInfosetStreamReader;
/*     */   }
/*     */   public void bridge() throws XMLStreamException { try { int depth = 0; int event = this.fastInfosetStreamReader.getEventType(); if (event == 7) while (!this.fastInfosetStreamReader.isStartElement()) event = this.fastInfosetStreamReader.next();   if (event != 1) throw new IllegalStateException("The current event is not START_ELEMENT\n but " + event);  handleStartDocument(this.fastInfosetStreamReader.getNamespaceContext()); while (true) { switch (event) { case 1: handleStartElement(); depth++; break;case 2: depth--; handleEndElement(); if (depth == 0) break;  break;case 4: case 6: case 12: if (this.predictor.expectText()) { event = this.fastInfosetStreamReader.peekNext(); if (event == 2) { processNonIgnorableText(); break; }  if (event == 1) { processIgnorableText(); break; }  handleFragmentedCharacters(); }  break; }  event = this.fastInfosetStreamReader.next(); }  this.fastInfosetStreamReader.next(); handleEndDocument(); } catch (SAXException e) { throw new XMLStreamException(e); }  }
/* 252 */   protected Location getCurrentLocation() { return this.fastInfosetStreamReader.getLocation(); } protected String getCurrentQName() { return this.fastInfosetStreamReader.getNameString(); } private void processNonIgnorableText() throws SAXException { this.textReported = true;
/* 253 */     boolean isTextAlgorithmAplied = (this.fastInfosetStreamReader.getTextAlgorithmBytes() != null);
/*     */ 
/*     */     
/* 256 */     if (isTextAlgorithmAplied && this.fastInfosetStreamReader.getTextAlgorithmIndex() == 1)
/*     */     
/* 258 */     { this.base64Data.set(this.fastInfosetStreamReader.getTextAlgorithmBytesClone(), null);
/* 259 */       this.visitor.text((CharSequence)this.base64Data); }
/*     */     else
/* 261 */     { if (isTextAlgorithmAplied) {
/* 262 */         this.fastInfosetStreamReader.getText();
/*     */       }
/*     */       
/* 265 */       this.charArray.set();
/* 266 */       this.visitor.text(this.charArray); }  }
/*     */   private void handleStartElement() throws SAXException { processUnreportedText(); for (int i = 0; i < this.fastInfosetStreamReader.accessNamespaceCount(); i++) this.visitor.startPrefixMapping(this.fastInfosetStreamReader.getNamespacePrefix(i), this.fastInfosetStreamReader.getNamespaceURI(i));  this.tagName.uri = this.fastInfosetStreamReader.accessNamespaceURI(); this.tagName.local = this.fastInfosetStreamReader.accessLocalName(); this.tagName.atts = (Attributes)this.fastInfosetStreamReader.getAttributesHolder(); this.visitor.startElement(this.tagName); }
/*     */   private void handleFragmentedCharacters() throws XMLStreamException, SAXException { this.buffer.setLength(0); this.buffer.append(this.fastInfosetStreamReader.accessTextCharacters(), this.fastInfosetStreamReader.accessTextStart(), this.fastInfosetStreamReader.accessTextLength()); while (true) { switch (this.fastInfosetStreamReader.peekNext()) { case 1: processBufferedText(true); return;case 2: processBufferedText(false); return;case 4: case 6: case 12: this.fastInfosetStreamReader.next(); this.buffer.append(this.fastInfosetStreamReader.accessTextCharacters(), this.fastInfosetStreamReader.accessTextStart(), this.fastInfosetStreamReader.accessTextLength()); continue; }  this.fastInfosetStreamReader.next(); }  }
/*     */   private void handleEndElement() throws SAXException { processUnreportedText(); this.tagName.uri = this.fastInfosetStreamReader.accessNamespaceURI(); this.tagName.local = this.fastInfosetStreamReader.accessLocalName(); this.visitor.endElement(this.tagName); for (int i = this.fastInfosetStreamReader.accessNamespaceCount() - 1; i >= 0; i--) this.visitor.endPrefixMapping(this.fastInfosetStreamReader.getNamespacePrefix(i));  }
/*     */   private final class CharSequenceImpl implements CharSequence {
/* 271 */     char[] ch; int start; int length; CharSequenceImpl() {} CharSequenceImpl(char[] ch, int start, int length) { this.ch = ch; this.start = start; this.length = length; } public void set() { this.ch = FastInfosetConnector.this.fastInfosetStreamReader.accessTextCharacters(); this.start = FastInfosetConnector.this.fastInfosetStreamReader.accessTextStart(); this.length = FastInfosetConnector.this.fastInfosetStreamReader.accessTextLength(); } public final int length() { return this.length; } public final char charAt(int index) { return this.ch[this.start + index]; } public final CharSequence subSequence(int start, int end) { return new CharSequenceImpl(this.ch, this.start + start, end - start); } public String toString() { return new String(this.ch, this.start, this.length); } } private void processIgnorableText() throws SAXException { boolean isTextAlgorithmAplied = (this.fastInfosetStreamReader.getTextAlgorithmBytes() != null);
/*     */ 
/*     */     
/* 274 */     if (isTextAlgorithmAplied && this.fastInfosetStreamReader.getTextAlgorithmIndex() == 1) {
/*     */       
/* 276 */       this.base64Data.set(this.fastInfosetStreamReader.getTextAlgorithmBytesClone(), null);
/* 277 */       this.visitor.text((CharSequence)this.base64Data);
/* 278 */       this.textReported = true;
/*     */     } else {
/* 280 */       if (isTextAlgorithmAplied) {
/* 281 */         this.fastInfosetStreamReader.getText();
/*     */       }
/*     */       
/* 284 */       this.charArray.set();
/* 285 */       if (!WhiteSpaceProcessor.isWhiteSpace(this.charArray)) {
/* 286 */         this.visitor.text(this.charArray);
/* 287 */         this.textReported = true;
/*     */       } 
/*     */     }  }
/*     */ 
/*     */   
/*     */   private void processBufferedText(boolean ignorable) throws SAXException {
/* 293 */     if (!ignorable || !WhiteSpaceProcessor.isWhiteSpace(this.buffer)) {
/* 294 */       this.visitor.text(this.buffer);
/* 295 */       this.textReported = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processUnreportedText() throws SAXException {
/* 300 */     if (!this.textReported && this.predictor.expectText()) {
/* 301 */       this.visitor.text("");
/*     */     }
/* 303 */     this.textReported = false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\FastInfosetConnector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */