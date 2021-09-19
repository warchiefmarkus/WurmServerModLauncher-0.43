/*     */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.bind.IDResolver;
/*     */ import com.sun.xml.bind.api.ClassResolver;
/*     */ import com.sun.xml.bind.unmarshaller.DOMScanner;
/*     */ import com.sun.xml.bind.unmarshaller.InfosetScanner;
/*     */ import com.sun.xml.bind.unmarshaller.Messages;
/*     */ import com.sun.xml.bind.v2.runtime.AssociationMap;
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.bind.v2.runtime.JaxBeanInfo;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.PropertyException;
/*     */ import javax.xml.bind.UnmarshalException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.UnmarshallerHandler;
/*     */ import javax.xml.bind.ValidationEvent;
/*     */ import javax.xml.bind.ValidationEventHandler;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import javax.xml.bind.attachment.AttachmentUnmarshaller;
/*     */ import javax.xml.bind.helpers.AbstractUnmarshallerImpl;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.validation.Schema;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UnmarshallerImpl
/*     */   extends AbstractUnmarshallerImpl
/*     */   implements ValidationEventHandler
/*     */ {
/*     */   protected final JAXBContextImpl context;
/*     */   private Schema schema;
/*     */   public final UnmarshallingContext coordinator;
/*     */   private Unmarshaller.Listener externalListener;
/*     */   private AttachmentUnmarshaller attachmentUnmarshaller;
/* 111 */   private IDResolver idResolver = new DefaultIDResolver();
/*     */   
/*     */   public UnmarshallerImpl(JAXBContextImpl context, AssociationMap assoc) {
/* 114 */     this.context = context;
/* 115 */     this.coordinator = new UnmarshallingContext(this, assoc);
/*     */     
/*     */     try {
/* 118 */       setEventHandler(this);
/* 119 */     } catch (JAXBException e) {
/* 120 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public UnmarshallerHandler getUnmarshallerHandler() {
/* 125 */     return getUnmarshallerHandler(true, (JaxBeanInfo)null);
/*     */   }
/*     */   
/*     */   private SAXConnector getUnmarshallerHandler(boolean intern, JaxBeanInfo expectedType) {
/* 129 */     XmlVisitor h = createUnmarshallerHandler((InfosetScanner)null, false, expectedType);
/* 130 */     if (intern)
/* 131 */       h = new InterningXmlVisitor(h); 
/* 132 */     return new SAXConnector(h, null);
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
/*     */   public final XmlVisitor createUnmarshallerHandler(InfosetScanner scanner, boolean inplace, JaxBeanInfo expectedType) {
/* 150 */     this.coordinator.reset(scanner, inplace, expectedType, this.idResolver);
/* 151 */     XmlVisitor unmarshaller = this.coordinator;
/*     */ 
/*     */     
/* 154 */     if (this.schema != null) {
/* 155 */       unmarshaller = new ValidatingUnmarshaller(this.schema, unmarshaller);
/*     */     }
/* 157 */     if (this.attachmentUnmarshaller != null && this.attachmentUnmarshaller.isXOPPackage()) {
/* 158 */       unmarshaller = new MTOMDecorator(this, unmarshaller, this.attachmentUnmarshaller);
/*     */     }
/* 160 */     return unmarshaller;
/*     */   }
/*     */   
/* 163 */   private static final DefaultHandler dummyHandler = new DefaultHandler();
/*     */   public static final String FACTORY = "com.sun.xml.bind.ObjectFactory";
/*     */   
/*     */   public static boolean needsInterning(XMLReader reader) {
/*     */     try {
/* 168 */       reader.setFeature("http://xml.org/sax/features/string-interning", true);
/* 169 */     } catch (SAXException e) {}
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 174 */       if (reader.getFeature("http://xml.org/sax/features/string-interning"))
/* 175 */         return false; 
/* 176 */     } catch (SAXException e) {}
/*     */ 
/*     */ 
/*     */     
/* 180 */     return true;
/*     */   }
/*     */   
/*     */   protected Object unmarshal(XMLReader reader, InputSource source) throws JAXBException {
/* 184 */     return unmarshal0(reader, source, (JaxBeanInfo)null);
/*     */   }
/*     */   
/*     */   protected <T> JAXBElement<T> unmarshal(XMLReader reader, InputSource source, Class<T> expectedType) throws JAXBException {
/* 188 */     if (expectedType == null)
/* 189 */       throw new IllegalArgumentException(); 
/* 190 */     return (JAXBElement<T>)unmarshal0(reader, source, getBeanInfo(expectedType));
/*     */   }
/*     */   
/*     */   private Object unmarshal0(XMLReader reader, InputSource source, JaxBeanInfo expectedType) throws JAXBException {
/* 194 */     SAXConnector connector = getUnmarshallerHandler(needsInterning(reader), expectedType);
/*     */     
/* 196 */     reader.setContentHandler((ContentHandler)connector);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 208 */     reader.setErrorHandler(this.coordinator);
/*     */     
/*     */     try {
/* 211 */       reader.parse(source);
/* 212 */     } catch (IOException e) {
/* 213 */       throw new UnmarshalException(e);
/* 214 */     } catch (SAXException e) {
/* 215 */       throw createUnmarshalException(e);
/*     */     } 
/*     */     
/* 218 */     Object result = connector.getResult();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 223 */     reader.setContentHandler(dummyHandler);
/* 224 */     reader.setErrorHandler(dummyHandler);
/*     */     
/* 226 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> JAXBElement<T> unmarshal(Source source, Class<T> expectedType) throws JAXBException {
/* 231 */     if (source instanceof SAXSource) {
/* 232 */       SAXSource ss = (SAXSource)source;
/*     */       
/* 234 */       XMLReader reader = ss.getXMLReader();
/* 235 */       if (reader == null) {
/* 236 */         reader = getXMLReader();
/*     */       }
/* 238 */       return unmarshal(reader, ss.getInputSource(), expectedType);
/*     */     } 
/* 240 */     if (source instanceof StreamSource) {
/* 241 */       return unmarshal(getXMLReader(), streamSourceToInputSource((StreamSource)source), expectedType);
/*     */     }
/* 243 */     if (source instanceof DOMSource) {
/* 244 */       return unmarshal(((DOMSource)source).getNode(), expectedType);
/*     */     }
/*     */     
/* 247 */     throw new IllegalArgumentException();
/*     */   }
/*     */   
/*     */   public Object unmarshal0(Source source, JaxBeanInfo expectedType) throws JAXBException {
/* 251 */     if (source instanceof SAXSource) {
/* 252 */       SAXSource ss = (SAXSource)source;
/*     */       
/* 254 */       XMLReader reader = ss.getXMLReader();
/* 255 */       if (reader == null) {
/* 256 */         reader = getXMLReader();
/*     */       }
/* 258 */       return unmarshal0(reader, ss.getInputSource(), expectedType);
/*     */     } 
/* 260 */     if (source instanceof StreamSource) {
/* 261 */       return unmarshal0(getXMLReader(), streamSourceToInputSource((StreamSource)source), expectedType);
/*     */     }
/* 263 */     if (source instanceof DOMSource) {
/* 264 */       return unmarshal0(((DOMSource)source).getNode(), expectedType);
/*     */     }
/*     */     
/* 267 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   
/*     */   public final ValidationEventHandler getEventHandler() {
/*     */     try {
/* 273 */       return super.getEventHandler();
/* 274 */     } catch (JAXBException e) {
/*     */       
/* 276 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasEventHandler() {
/* 286 */     return (getEventHandler() != this);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> JAXBElement<T> unmarshal(Node node, Class<T> expectedType) throws JAXBException {
/* 291 */     if (expectedType == null)
/* 292 */       throw new IllegalArgumentException(); 
/* 293 */     return (JAXBElement<T>)unmarshal0(node, getBeanInfo(expectedType));
/*     */   }
/*     */   
/*     */   public final Object unmarshal(Node node) throws JAXBException {
/* 297 */     return unmarshal0(node, (JaxBeanInfo)null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final Object unmarshal(SAXSource source) throws JAXBException {
/* 303 */     return unmarshal(source);
/*     */   }
/*     */   
/*     */   public final Object unmarshal0(Node node, JaxBeanInfo expectedType) throws JAXBException {
/*     */     try {
/* 308 */       DOMScanner scanner = new DOMScanner();
/*     */       
/* 310 */       InterningXmlVisitor handler = new InterningXmlVisitor(createUnmarshallerHandler((InfosetScanner)null, false, expectedType));
/* 311 */       scanner.setContentHandler((ContentHandler)new SAXConnector(handler, (LocatorEx)scanner));
/*     */       
/* 313 */       if (node instanceof Element) {
/* 314 */         scanner.scan((Element)node);
/*     */       }
/* 316 */       else if (node instanceof Document) {
/* 317 */         scanner.scan((Document)node);
/*     */       } else {
/*     */         
/* 320 */         throw new IllegalArgumentException("Unexpected node type: " + node);
/*     */       } 
/* 322 */       return handler.getContext().getResult();
/* 323 */     } catch (SAXException e) {
/* 324 */       throw createUnmarshalException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object unmarshal(XMLStreamReader reader) throws JAXBException {
/* 330 */     return unmarshal0(reader, (JaxBeanInfo)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> JAXBElement<T> unmarshal(XMLStreamReader reader, Class<T> expectedType) throws JAXBException {
/* 335 */     if (expectedType == null)
/* 336 */       throw new IllegalArgumentException(); 
/* 337 */     return (JAXBElement<T>)unmarshal0(reader, getBeanInfo(expectedType));
/*     */   }
/*     */   
/*     */   public Object unmarshal0(XMLStreamReader reader, JaxBeanInfo expectedType) throws JAXBException {
/* 341 */     if (reader == null) {
/* 342 */       throw new IllegalArgumentException(Messages.format("Unmarshaller.NullReader"));
/*     */     }
/*     */ 
/*     */     
/* 346 */     int eventType = reader.getEventType();
/* 347 */     if (eventType != 1 && eventType != 7)
/*     */     {
/*     */       
/* 350 */       throw new IllegalStateException(Messages.format("Unmarshaller.IllegalReaderState", Integer.valueOf(eventType)));
/*     */     }
/*     */ 
/*     */     
/* 354 */     XmlVisitor h = createUnmarshallerHandler((InfosetScanner)null, false, expectedType);
/* 355 */     StAXConnector connector = StAXStreamConnector.create(reader, h);
/*     */     
/*     */     try {
/* 358 */       connector.bridge();
/* 359 */     } catch (XMLStreamException e) {
/* 360 */       throw handleStreamException(e);
/*     */     } 
/*     */     
/* 363 */     return h.getContext().getResult();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> JAXBElement<T> unmarshal(XMLEventReader reader, Class<T> expectedType) throws JAXBException {
/* 368 */     if (expectedType == null)
/* 369 */       throw new IllegalArgumentException(); 
/* 370 */     return (JAXBElement<T>)unmarshal0(reader, getBeanInfo(expectedType));
/*     */   }
/*     */ 
/*     */   
/*     */   public Object unmarshal(XMLEventReader reader) throws JAXBException {
/* 375 */     return unmarshal0(reader, (JaxBeanInfo)null);
/*     */   }
/*     */   
/*     */   private Object unmarshal0(XMLEventReader reader, JaxBeanInfo expectedType) throws JAXBException {
/* 379 */     if (reader == null) {
/* 380 */       throw new IllegalArgumentException(Messages.format("Unmarshaller.NullReader"));
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 385 */       XMLEvent event = reader.peek();
/*     */       
/* 387 */       if (!event.isStartElement() && !event.isStartDocument())
/*     */       {
/* 389 */         throw new IllegalStateException(Messages.format("Unmarshaller.IllegalReaderState", Integer.valueOf(event.getEventType())));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 395 */       boolean isZephyr = reader.getClass().getName().equals("com.sun.xml.stream.XMLReaderImpl");
/* 396 */       XmlVisitor h = createUnmarshallerHandler((InfosetScanner)null, false, expectedType);
/* 397 */       if (!isZephyr)
/* 398 */         h = new InterningXmlVisitor(h); 
/* 399 */       (new StAXEventConnector(reader, h)).bridge();
/* 400 */       return h.getContext().getResult();
/* 401 */     } catch (XMLStreamException e) {
/* 402 */       throw handleStreamException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object unmarshal0(InputStream input, JaxBeanInfo expectedType) throws JAXBException {
/* 407 */     return unmarshal0(getXMLReader(), new InputSource(input), expectedType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static JAXBException handleStreamException(XMLStreamException e) {
/* 417 */     Throwable ne = e.getNestedException();
/* 418 */     if (ne instanceof JAXBException)
/* 419 */       return (JAXBException)ne; 
/* 420 */     if (ne instanceof SAXException)
/* 421 */       return (JAXBException)new UnmarshalException(ne); 
/* 422 */     return (JAXBException)new UnmarshalException(e);
/*     */   }
/*     */   
/*     */   public Object getProperty(String name) throws PropertyException {
/* 426 */     if (name.equals(IDResolver.class.getName())) {
/* 427 */       return this.idResolver;
/*     */     }
/* 429 */     return super.getProperty(name);
/*     */   }
/*     */   
/*     */   public void setProperty(String name, Object value) throws PropertyException {
/* 433 */     if (name.equals("com.sun.xml.bind.ObjectFactory")) {
/* 434 */       this.coordinator.setFactories(value);
/*     */       return;
/*     */     } 
/* 437 */     if (name.equals(IDResolver.class.getName())) {
/* 438 */       this.idResolver = (IDResolver)value;
/*     */       return;
/*     */     } 
/* 441 */     if (name.equals(ClassResolver.class.getName())) {
/* 442 */       this.coordinator.classResolver = (ClassResolver)value;
/*     */       return;
/*     */     } 
/* 445 */     super.setProperty(name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSchema(Schema schema) {
/* 452 */     this.schema = schema;
/*     */   }
/*     */ 
/*     */   
/*     */   public Schema getSchema() {
/* 457 */     return this.schema;
/*     */   }
/*     */ 
/*     */   
/*     */   public AttachmentUnmarshaller getAttachmentUnmarshaller() {
/* 462 */     return this.attachmentUnmarshaller;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAttachmentUnmarshaller(AttachmentUnmarshaller au) {
/* 467 */     this.attachmentUnmarshaller = au;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValidating() {
/* 475 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidating(boolean validating) {
/* 483 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public <A extends XmlAdapter> void setAdapter(Class<A> type, A adapter) {
/* 488 */     if (type == null)
/* 489 */       throw new IllegalArgumentException(); 
/* 490 */     this.coordinator.putAdapter(type, (XmlAdapter)adapter);
/*     */   }
/*     */ 
/*     */   
/*     */   public <A extends XmlAdapter> A getAdapter(Class<A> type) {
/* 495 */     if (type == null)
/* 496 */       throw new IllegalArgumentException(); 
/* 497 */     if (this.coordinator.containsAdapter(type))
/*     */     {
/* 499 */       return (A)this.coordinator.getAdapter(type);
/*     */     }
/* 501 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmarshalException createUnmarshalException(SAXException e) {
/* 506 */     return super.createUnmarshalException(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleEvent(ValidationEvent event) {
/* 514 */     return (event.getSeverity() != 2);
/*     */   }
/*     */   
/*     */   private static InputSource streamSourceToInputSource(StreamSource ss) {
/* 518 */     InputSource is = new InputSource();
/* 519 */     is.setSystemId(ss.getSystemId());
/* 520 */     is.setByteStream(ss.getInputStream());
/* 521 */     is.setCharacterStream(ss.getReader());
/*     */     
/* 523 */     return is;
/*     */   }
/*     */   
/*     */   public <T> JaxBeanInfo<T> getBeanInfo(Class<T> clazz) throws JAXBException {
/* 527 */     return this.context.getBeanInfo(clazz, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public Unmarshaller.Listener getListener() {
/* 532 */     return this.externalListener;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setListener(Unmarshaller.Listener listener) {
/* 537 */     this.externalListener = listener;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\UnmarshallerImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */