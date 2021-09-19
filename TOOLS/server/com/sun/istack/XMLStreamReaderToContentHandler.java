/*     */ package com.sun.istack;
/*     */ 
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMLStreamReaderToContentHandler
/*     */ {
/*     */   private final XMLStreamReader staxStreamReader;
/*     */   private final ContentHandler saxHandler;
/*     */   private final boolean eagerQuit;
/*     */   private final boolean fragment;
/*     */   private final String[] inscopeNamespaces;
/*     */   
/*     */   public XMLStreamReaderToContentHandler(XMLStreamReader staxCore, ContentHandler saxCore, boolean eagerQuit, boolean fragment) {
/*  47 */     this(staxCore, saxCore, eagerQuit, fragment, new String[0]);
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
/*     */   public XMLStreamReaderToContentHandler(XMLStreamReader staxCore, ContentHandler saxCore, boolean eagerQuit, boolean fragment, String[] inscopeNamespaces) {
/*  65 */     this.staxStreamReader = staxCore;
/*  66 */     this.saxHandler = saxCore;
/*  67 */     this.eagerQuit = eagerQuit;
/*  68 */     this.fragment = fragment;
/*  69 */     this.inscopeNamespaces = inscopeNamespaces;
/*  70 */     assert inscopeNamespaces.length % 2 == 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bridge() throws XMLStreamException {
/*     */     try {
/*  81 */       int depth = 0;
/*     */ 
/*     */       
/*  84 */       int event = this.staxStreamReader.getEventType();
/*  85 */       if (event == 7)
/*     */       {
/*  87 */         while (!this.staxStreamReader.isStartElement()) {
/*  88 */           event = this.staxStreamReader.next();
/*     */         }
/*     */       }
/*     */       
/*  92 */       if (event != 1) {
/*  93 */         throw new IllegalStateException("The current event is not START_ELEMENT\n but " + event);
/*     */       }
/*  95 */       handleStartDocument();
/*     */       int i;
/*  97 */       for (i = 0; i < this.inscopeNamespaces.length; i += 2) {
/*  98 */         this.saxHandler.startPrefixMapping(this.inscopeNamespaces[i], this.inscopeNamespaces[i + 1]);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       do {
/* 106 */         switch (event) {
/*     */           case 1:
/* 108 */             depth++;
/* 109 */             handleStartElement();
/*     */             break;
/*     */           case 2:
/* 112 */             handleEndElement();
/* 113 */             depth--;
/* 114 */             if (depth == 0 && this.eagerQuit)
/*     */               break; 
/*     */             break;
/*     */           case 4:
/* 118 */             handleCharacters();
/*     */             break;
/*     */           case 9:
/* 121 */             handleEntityReference();
/*     */             break;
/*     */           case 3:
/* 124 */             handlePI();
/*     */             break;
/*     */           case 5:
/* 127 */             handleComment();
/*     */             break;
/*     */           case 11:
/* 130 */             handleDTD();
/*     */             break;
/*     */           case 10:
/* 133 */             handleAttribute();
/*     */             break;
/*     */           case 13:
/* 136 */             handleNamespace();
/*     */             break;
/*     */           case 12:
/* 139 */             handleCDATA();
/*     */             break;
/*     */           case 15:
/* 142 */             handleEntityDecl();
/*     */             break;
/*     */           case 14:
/* 145 */             handleNotationDecl();
/*     */             break;
/*     */           case 6:
/* 148 */             handleSpace();
/*     */             break;
/*     */           default:
/* 151 */             throw new InternalError("processing event: " + event);
/*     */         } 
/*     */         
/* 154 */         event = this.staxStreamReader.next();
/* 155 */       } while (depth != 0);
/*     */       
/* 157 */       for (i = 0; i < this.inscopeNamespaces.length; i += 2) {
/* 158 */         this.saxHandler.endPrefixMapping(this.inscopeNamespaces[i]);
/*     */       }
/*     */       
/* 161 */       handleEndDocument();
/* 162 */     } catch (SAXException e) {
/* 163 */       throw new XMLStreamException2(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleEndDocument() throws SAXException {
/* 168 */     if (this.fragment) {
/*     */       return;
/*     */     }
/* 171 */     this.saxHandler.endDocument();
/*     */   }
/*     */   
/*     */   private void handleStartDocument() throws SAXException {
/* 175 */     if (this.fragment) {
/*     */       return;
/*     */     }
/* 178 */     this.saxHandler.setDocumentLocator(new Locator() {
/*     */           public int getColumnNumber() {
/* 180 */             return XMLStreamReaderToContentHandler.this.staxStreamReader.getLocation().getColumnNumber();
/*     */           }
/*     */           public int getLineNumber() {
/* 183 */             return XMLStreamReaderToContentHandler.this.staxStreamReader.getLocation().getLineNumber();
/*     */           }
/*     */           public String getPublicId() {
/* 186 */             return XMLStreamReaderToContentHandler.this.staxStreamReader.getLocation().getPublicId();
/*     */           }
/*     */           public String getSystemId() {
/* 189 */             return XMLStreamReaderToContentHandler.this.staxStreamReader.getLocation().getSystemId();
/*     */           }
/*     */         });
/* 192 */     this.saxHandler.startDocument();
/*     */   }
/*     */   
/*     */   private void handlePI() throws XMLStreamException {
/*     */     try {
/* 197 */       this.saxHandler.processingInstruction(this.staxStreamReader.getPITarget(), this.staxStreamReader.getPIData());
/*     */     
/*     */     }
/* 200 */     catch (SAXException e) {
/* 201 */       throw new XMLStreamException2(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleCharacters() throws XMLStreamException {
/*     */     try {
/* 207 */       this.saxHandler.characters(this.staxStreamReader.getTextCharacters(), this.staxStreamReader.getTextStart(), this.staxStreamReader.getTextLength());
/*     */ 
/*     */     
/*     */     }
/* 211 */     catch (SAXException e) {
/* 212 */       throw new XMLStreamException2(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleEndElement() throws XMLStreamException {
/* 217 */     QName qName = this.staxStreamReader.getName();
/*     */     
/*     */     try {
/* 220 */       String pfix = qName.getPrefix();
/* 221 */       String rawname = (pfix == null || pfix.length() == 0) ? qName.getLocalPart() : (pfix + ':' + qName.getLocalPart());
/*     */ 
/*     */ 
/*     */       
/* 225 */       this.saxHandler.endElement(qName.getNamespaceURI(), qName.getLocalPart(), rawname);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 231 */       int nsCount = this.staxStreamReader.getNamespaceCount();
/* 232 */       for (int i = nsCount - 1; i >= 0; i--) {
/* 233 */         String prefix = this.staxStreamReader.getNamespacePrefix(i);
/* 234 */         if (prefix == null) {
/* 235 */           prefix = "";
/*     */         }
/* 237 */         this.saxHandler.endPrefixMapping(prefix);
/*     */       } 
/* 239 */     } catch (SAXException e) {
/* 240 */       throw new XMLStreamException2(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleStartElement() throws XMLStreamException {
/*     */     try {
/*     */       String rawname;
/* 248 */       int nsCount = this.staxStreamReader.getNamespaceCount();
/* 249 */       for (int i = 0; i < nsCount; i++) {
/* 250 */         this.saxHandler.startPrefixMapping(fixNull(this.staxStreamReader.getNamespacePrefix(i)), fixNull(this.staxStreamReader.getNamespaceURI(i)));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 256 */       QName qName = this.staxStreamReader.getName();
/* 257 */       String prefix = qName.getPrefix();
/*     */       
/* 259 */       if (prefix == null || prefix.length() == 0) {
/* 260 */         rawname = qName.getLocalPart();
/*     */       } else {
/* 262 */         rawname = prefix + ':' + qName.getLocalPart();
/* 263 */       }  Attributes attrs = getAttributes();
/* 264 */       this.saxHandler.startElement(qName.getNamespaceURI(), qName.getLocalPart(), rawname, attrs);
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 269 */     catch (SAXException e) {
/* 270 */       throw new XMLStreamException2(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String fixNull(String s) {
/* 275 */     if (s == null) return ""; 
/* 276 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Attributes getAttributes() {
/* 286 */     AttributesImpl attrs = new AttributesImpl();
/*     */     
/* 288 */     int eventType = this.staxStreamReader.getEventType();
/* 289 */     if (eventType != 10 && eventType != 1)
/*     */     {
/* 291 */       throw new InternalError("getAttributes() attempting to process: " + eventType);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 300 */     for (int i = 0; i < this.staxStreamReader.getAttributeCount(); i++) {
/* 301 */       String qName, uri = this.staxStreamReader.getAttributeNamespace(i);
/* 302 */       if (uri == null) uri = ""; 
/* 303 */       String localName = this.staxStreamReader.getAttributeLocalName(i);
/* 304 */       String prefix = this.staxStreamReader.getAttributePrefix(i);
/*     */       
/* 306 */       if (prefix == null || prefix.length() == 0) {
/* 307 */         qName = localName;
/*     */       } else {
/* 309 */         qName = prefix + ':' + localName;
/* 310 */       }  String type = this.staxStreamReader.getAttributeType(i);
/* 311 */       String value = this.staxStreamReader.getAttributeValue(i);
/*     */       
/* 313 */       attrs.addAttribute(uri, localName, qName, type, value);
/*     */     } 
/*     */     
/* 316 */     return attrs;
/*     */   }
/*     */   
/*     */   private void handleNamespace() {}
/*     */   
/*     */   private void handleAttribute() {}
/*     */   
/*     */   private void handleDTD() {}
/*     */   
/*     */   private void handleComment() {}
/*     */   
/*     */   private void handleEntityReference() {}
/*     */   
/*     */   private void handleSpace() {}
/*     */   
/*     */   private void handleNotationDecl() {}
/*     */   
/*     */   private void handleEntityDecl() {}
/*     */   
/*     */   private void handleCDATA() {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\istack\XMLStreamReaderToContentHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */