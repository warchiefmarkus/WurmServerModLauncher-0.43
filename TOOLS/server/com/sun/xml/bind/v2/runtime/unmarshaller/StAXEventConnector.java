/*     */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ import javax.xml.stream.events.Characters;
/*     */ import javax.xml.stream.events.EndElement;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ import javax.xml.stream.events.StartElement;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ import org.xml.sax.Attributes;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class StAXEventConnector
/*     */   extends StAXConnector
/*     */ {
/*     */   private final XMLEventReader staxEventReader;
/*     */   private XMLEvent event;
/* 110 */   private final AttributesImpl attrs = new AttributesImpl();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   private final StringBuilder buffer = new StringBuilder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean seenText;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StAXEventConnector(XMLEventReader staxCore, XmlVisitor visitor) {
/* 130 */     super(visitor);
/* 131 */     this.staxEventReader = staxCore;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void bridge() throws XMLStreamException {
/*     */     try {
/* 138 */       int depth = 0;
/*     */       
/* 140 */       this.event = this.staxEventReader.peek();
/*     */       
/* 142 */       if (!this.event.isStartDocument() && !this.event.isStartElement()) {
/* 143 */         throw new IllegalStateException();
/*     */       }
/*     */       
/*     */       do {
/* 147 */         this.event = this.staxEventReader.nextEvent();
/* 148 */       } while (!this.event.isStartElement());
/*     */       
/* 150 */       handleStartDocument(this.event.asStartElement().getNamespaceContext());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 157 */         switch (this.event.getEventType()) {
/*     */           case 1:
/* 159 */             handleStartElement(this.event.asStartElement());
/* 160 */             depth++;
/*     */             break;
/*     */           case 2:
/* 163 */             depth--;
/* 164 */             handleEndElement(this.event.asEndElement());
/* 165 */             if (depth == 0)
/*     */               break;  break;
/*     */           case 4:
/*     */           case 6:
/*     */           case 12:
/* 170 */             handleCharacters(this.event.asCharacters());
/*     */             break;
/*     */         } 
/*     */ 
/*     */         
/* 175 */         this.event = this.staxEventReader.nextEvent();
/*     */       } 
/*     */       
/* 178 */       handleEndDocument();
/* 179 */       this.event = null;
/* 180 */     } catch (SAXException e) {
/* 181 */       throw new XMLStreamException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Location getCurrentLocation() {
/* 186 */     return this.event.getLocation();
/*     */   }
/*     */   
/*     */   protected String getCurrentQName() {
/*     */     QName qName;
/* 191 */     if (this.event.isEndElement()) {
/* 192 */       qName = this.event.asEndElement().getName();
/*     */     } else {
/* 194 */       qName = this.event.asStartElement().getName();
/* 195 */     }  return getQName(qName.getPrefix(), qName.getLocalPart());
/*     */   }
/*     */   
/*     */   private void handleCharacters(Characters event) throws SAXException, XMLStreamException {
/*     */     XMLEvent next;
/* 200 */     if (!this.predictor.expectText()) {
/*     */       return;
/*     */     }
/* 203 */     this.seenText = true;
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 208 */       next = this.staxEventReader.peek();
/* 209 */       if (!isIgnorable(next))
/*     */         break; 
/* 211 */       this.staxEventReader.nextEvent();
/*     */     } 
/*     */     
/* 214 */     if (isTag(next)) {
/*     */       
/* 216 */       this.visitor.text(event.getData());
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 222 */     this.buffer.append(event.getData());
/*     */ 
/*     */     
/*     */     while (true) {
/* 226 */       next = this.staxEventReader.peek();
/* 227 */       if (!isIgnorable(next)) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 232 */         if (isTag(next)) {
/*     */           
/* 234 */           this.visitor.text(this.buffer);
/* 235 */           this.buffer.setLength(0);
/*     */           
/*     */           return;
/*     */         } 
/* 239 */         this.buffer.append(next.asCharacters().getData());
/* 240 */         this.staxEventReader.nextEvent();
/*     */         continue;
/*     */       } 
/*     */       this.staxEventReader.nextEvent();
/*     */     }  } private boolean isTag(XMLEvent event) {
/* 245 */     int eventType = event.getEventType();
/* 246 */     return (eventType == 1 || eventType == 2);
/*     */   }
/*     */   
/*     */   private boolean isIgnorable(XMLEvent event) {
/* 250 */     int eventType = event.getEventType();
/* 251 */     return (eventType == 5 || eventType == 3);
/*     */   }
/*     */   
/*     */   private void handleEndElement(EndElement event) throws SAXException {
/* 255 */     if (!this.seenText && this.predictor.expectText()) {
/* 256 */       this.visitor.text("");
/*     */     }
/*     */ 
/*     */     
/* 260 */     QName qName = event.getName();
/* 261 */     this.tagName.uri = fixNull(qName.getNamespaceURI());
/* 262 */     this.tagName.local = qName.getLocalPart();
/* 263 */     this.visitor.endElement(this.tagName);
/*     */ 
/*     */     
/* 266 */     for (Iterator<Namespace> i = event.getNamespaces(); i.hasNext(); ) {
/* 267 */       String prefix = fixNull(((Namespace)i.next()).getPrefix());
/* 268 */       this.visitor.endPrefixMapping(prefix);
/*     */     } 
/*     */     
/* 271 */     this.seenText = false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleStartElement(StartElement event) throws SAXException {
/* 276 */     for (Iterator<Namespace> i = event.getNamespaces(); i.hasNext(); ) {
/* 277 */       Namespace ns = i.next();
/* 278 */       this.visitor.startPrefixMapping(fixNull(ns.getPrefix()), fixNull(ns.getNamespaceURI()));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 284 */     QName qName = event.getName();
/* 285 */     this.tagName.uri = fixNull(qName.getNamespaceURI());
/* 286 */     String localName = qName.getLocalPart();
/* 287 */     this.tagName.uri = fixNull(qName.getNamespaceURI());
/* 288 */     this.tagName.local = localName;
/* 289 */     this.tagName.atts = getAttributes(event);
/* 290 */     this.visitor.startElement(this.tagName);
/*     */     
/* 292 */     this.seenText = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Attributes getAttributes(StartElement event) {
/* 303 */     this.attrs.clear();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 310 */     for (Iterator<Attribute> i = event.getAttributes(); i.hasNext(); ) {
/* 311 */       String qName; Attribute staxAttr = i.next();
/*     */       
/* 313 */       QName name = staxAttr.getName();
/* 314 */       String uri = fixNull(name.getNamespaceURI());
/* 315 */       String localName = name.getLocalPart();
/* 316 */       String prefix = name.getPrefix();
/*     */       
/* 318 */       if (prefix == null || prefix.length() == 0) {
/* 319 */         qName = localName;
/*     */       } else {
/* 321 */         qName = prefix + ':' + localName;
/* 322 */       }  String type = staxAttr.getDTDType();
/* 323 */       String value = staxAttr.getValue();
/*     */       
/* 325 */       this.attrs.addAttribute(uri, localName, qName, type, value);
/*     */     } 
/*     */     
/* 328 */     return this.attrs;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\StAXEventConnector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */