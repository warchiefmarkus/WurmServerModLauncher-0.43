/*     */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.bind.WhiteSpaceProcessor;
/*     */ import java.lang.reflect.Constructor;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class StAXStreamConnector
/*     */   extends StAXConnector
/*     */ {
/*     */   private final XMLStreamReader staxStreamReader;
/*     */   
/*     */   public static StAXConnector create(XMLStreamReader reader, XmlVisitor visitor) {
/* 106 */     Class<?> readerClass = reader.getClass();
/* 107 */     if (FI_STAX_READER_CLASS != null && FI_STAX_READER_CLASS.isAssignableFrom(readerClass) && FI_CONNECTOR_CTOR != null) {
/*     */       try {
/* 109 */         return FI_CONNECTOR_CTOR.newInstance(new Object[] { reader, visitor });
/* 110 */       } catch (Exception t) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 115 */     boolean isZephyr = readerClass.getName().equals("com.sun.xml.stream.XMLReaderImpl");
/* 116 */     if (!isZephyr)
/*     */     {
/*     */       
/* 119 */       if (!checkImplementaionNameOfSjsxp(reader))
/*     */       {
/*     */         
/* 122 */         if (!getBoolProp(reader, "org.codehaus.stax2.internNames") || !getBoolProp(reader, "org.codehaus.stax2.internNsUris"))
/*     */         {
/*     */ 
/*     */           
/* 126 */           visitor = new InterningXmlVisitor(visitor); }  } 
/*     */     }
/* 128 */     if (STAX_EX_READER_CLASS != null && STAX_EX_READER_CLASS.isAssignableFrom(readerClass)) {
/*     */       try {
/* 130 */         return STAX_EX_CONNECTOR_CTOR.newInstance(new Object[] { reader, visitor });
/* 131 */       } catch (Exception t) {}
/*     */     }
/*     */ 
/*     */     
/* 135 */     return new StAXStreamConnector(reader, visitor);
/*     */   }
/*     */   
/*     */   private static boolean checkImplementaionNameOfSjsxp(XMLStreamReader reader) {
/*     */     try {
/* 140 */       Object name = reader.getProperty("http://java.sun.com/xml/stream/properties/implementation-name");
/* 141 */       return (name != null && name.equals("sjsxp"));
/* 142 */     } catch (Exception e) {
/*     */ 
/*     */       
/* 145 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean getBoolProp(XMLStreamReader r, String n) {
/*     */     try {
/* 151 */       Object o = r.getProperty(n);
/* 152 */       if (o instanceof Boolean) return ((Boolean)o).booleanValue(); 
/* 153 */       return false;
/* 154 */     } catch (Exception e) {
/*     */ 
/*     */       
/* 157 */       return false;
/*     */     } 
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
/* 169 */   protected final StringBuilder buffer = new StringBuilder();
/*     */ 
/*     */   
/*     */   protected boolean textReported = false;
/*     */   
/*     */   private final Attributes attributes;
/*     */ 
/*     */   
/*     */   protected StAXStreamConnector(XMLStreamReader staxStreamReader, XmlVisitor visitor) {
/* 178 */     super(visitor);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 281 */     this.attributes = new Attributes() {
/*     */         public int getLength() {
/* 283 */           return StAXStreamConnector.this.staxStreamReader.getAttributeCount();
/*     */         }
/*     */         
/*     */         public String getURI(int index) {
/* 287 */           String uri = StAXStreamConnector.this.staxStreamReader.getAttributeNamespace(index);
/* 288 */           if (uri == null) return ""; 
/* 289 */           return uri;
/*     */         }
/*     */         
/*     */         public String getLocalName(int index) {
/* 293 */           return StAXStreamConnector.this.staxStreamReader.getAttributeLocalName(index);
/*     */         }
/*     */         
/*     */         public String getQName(int index) {
/* 297 */           String prefix = StAXStreamConnector.this.staxStreamReader.getAttributePrefix(index);
/* 298 */           if (prefix == null || prefix.length() == 0) {
/* 299 */             return getLocalName(index);
/*     */           }
/* 301 */           return prefix + ':' + getLocalName(index);
/*     */         }
/*     */         
/*     */         public String getType(int index) {
/* 305 */           return StAXStreamConnector.this.staxStreamReader.getAttributeType(index);
/*     */         }
/*     */         
/*     */         public String getValue(int index) {
/* 309 */           return StAXStreamConnector.this.staxStreamReader.getAttributeValue(index);
/*     */         }
/*     */         
/*     */         public int getIndex(String uri, String localName) {
/* 313 */           for (int i = getLength() - 1; i >= 0; i--) {
/* 314 */             if (localName.equals(getLocalName(i)) && uri.equals(getURI(i)))
/* 315 */               return i; 
/* 316 */           }  return -1;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public int getIndex(String qName) {
/* 322 */           for (int i = getLength() - 1; i >= 0; i--) {
/* 323 */             if (qName.equals(getQName(i)))
/* 324 */               return i; 
/*     */           } 
/* 326 */           return -1;
/*     */         }
/*     */         
/*     */         public String getType(String uri, String localName) {
/* 330 */           int index = getIndex(uri, localName);
/* 331 */           if (index < 0) return null; 
/* 332 */           return getType(index);
/*     */         }
/*     */         
/*     */         public String getType(String qName) {
/* 336 */           int index = getIndex(qName);
/* 337 */           if (index < 0) return null; 
/* 338 */           return getType(index);
/*     */         }
/*     */         
/*     */         public String getValue(String uri, String localName) {
/* 342 */           int index = getIndex(uri, localName);
/* 343 */           if (index < 0) return null; 
/* 344 */           return getValue(index);
/*     */         }
/*     */         
/*     */         public String getValue(String qName) {
/* 348 */           int index = getIndex(qName);
/* 349 */           if (index < 0) return null; 
/* 350 */           return getValue(index); }
/*     */       }; this.staxStreamReader = staxStreamReader; } public void bridge() throws XMLStreamException { try { int depth = 0; int event = this.staxStreamReader.getEventType(); if (event == 7)
/*     */         while (!this.staxStreamReader.isStartElement())
/*     */           event = this.staxStreamReader.next();   if (event != 1)
/*     */         throw new IllegalStateException("The current event is not START_ELEMENT\n but " + event);  handleStartDocument(this.staxStreamReader.getNamespaceContext()); while (true) { switch (event) { case 1: handleStartElement(); depth++; break;case 2: depth--; handleEndElement(); if (depth == 0)
/* 355 */               break;  break;case 4: case 6: case 12: handleCharacters(); break; }  event = this.staxStreamReader.next(); }  this.staxStreamReader.next(); handleEndDocument(); } catch (SAXException e) { throw new XMLStreamException(e); }  } protected void handleCharacters() throws XMLStreamException, SAXException { if (this.predictor.expectText())
/* 356 */       this.buffer.append(this.staxStreamReader.getTextCharacters(), this.staxStreamReader.getTextStart(), this.staxStreamReader.getTextLength());  }
/*     */   protected Location getCurrentLocation() { return this.staxStreamReader.getLocation(); }
/*     */   protected String getCurrentQName() { return getQName(this.staxStreamReader.getPrefix(), this.staxStreamReader.getLocalName()); }
/*     */   private void handleEndElement() throws SAXException { processText(false); this.tagName.uri = fixNull(this.staxStreamReader.getNamespaceURI()); this.tagName.local = this.staxStreamReader.getLocalName(); this.visitor.endElement(this.tagName); int nsCount = this.staxStreamReader.getNamespaceCount(); for (int i = nsCount - 1; i >= 0; i--)
/*     */       this.visitor.endPrefixMapping(fixNull(this.staxStreamReader.getNamespacePrefix(i)));  }
/*     */   private void handleStartElement() throws SAXException { processText(true); int nsCount = this.staxStreamReader.getNamespaceCount(); for (int i = 0; i < nsCount; i++)
/*     */       this.visitor.startPrefixMapping(fixNull(this.staxStreamReader.getNamespacePrefix(i)), fixNull(this.staxStreamReader.getNamespaceURI(i)));  this.tagName.uri = fixNull(this.staxStreamReader.getNamespaceURI()); this.tagName.local = this.staxStreamReader.getLocalName(); this.tagName.atts = this.attributes;
/* 363 */     this.visitor.startElement(this.tagName); } private void processText(boolean ignorable) throws SAXException { if (this.predictor.expectText() && (!ignorable || !WhiteSpaceProcessor.isWhiteSpace(this.buffer))) {
/* 364 */       if (this.textReported) {
/* 365 */         this.textReported = false;
/*     */       } else {
/* 367 */         this.visitor.text(this.buffer);
/*     */       } 
/*     */     }
/* 370 */     this.buffer.setLength(0); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 378 */   private static final Class FI_STAX_READER_CLASS = initFIStAXReaderClass();
/* 379 */   private static final Constructor<? extends StAXConnector> FI_CONNECTOR_CTOR = initFastInfosetConnectorClass();
/*     */   
/*     */   private static Class initFIStAXReaderClass() {
/*     */     try {
/* 383 */       Class<?> fisr = UnmarshallerImpl.class.getClassLoader().loadClass("org.jvnet.fastinfoset.stax.FastInfosetStreamReader");
/*     */       
/* 385 */       Class<?> sdp = UnmarshallerImpl.class.getClassLoader().loadClass("com.sun.xml.fastinfoset.stax.StAXDocumentParser");
/*     */ 
/*     */       
/* 388 */       if (fisr.isAssignableFrom(sdp)) {
/* 389 */         return sdp;
/*     */       }
/* 391 */       return null;
/* 392 */     } catch (Throwable e) {
/* 393 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Constructor<? extends StAXConnector> initFastInfosetConnectorClass() {
/*     */     try {
/* 399 */       if (FI_STAX_READER_CLASS == null) {
/* 400 */         return null;
/*     */       }
/* 402 */       Class<?> c = UnmarshallerImpl.class.getClassLoader().loadClass("com.sun.xml.bind.v2.runtime.unmarshaller.FastInfosetConnector");
/*     */       
/* 404 */       return (Constructor)c.getConstructor(new Class[] { FI_STAX_READER_CLASS, XmlVisitor.class });
/* 405 */     } catch (Throwable e) {
/* 406 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 413 */   private static final Class STAX_EX_READER_CLASS = initStAXExReader();
/* 414 */   private static final Constructor<? extends StAXConnector> STAX_EX_CONNECTOR_CTOR = initStAXExConnector();
/*     */   
/*     */   private static Class initStAXExReader() {
/*     */     try {
/* 418 */       return UnmarshallerImpl.class.getClassLoader().loadClass("org.jvnet.staxex.XMLStreamReaderEx");
/* 419 */     } catch (Throwable e) {
/* 420 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Constructor<? extends StAXConnector> initStAXExConnector() {
/*     */     try {
/* 426 */       Class<?> c = UnmarshallerImpl.class.getClassLoader().loadClass("com.sun.xml.bind.v2.runtime.unmarshaller.StAXExConnector");
/* 427 */       return (Constructor)c.getConstructor(new Class[] { STAX_EX_READER_CLASS, XmlVisitor.class });
/* 428 */     } catch (Throwable e) {
/* 429 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\StAXStreamConnector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */