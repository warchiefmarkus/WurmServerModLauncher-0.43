/*     */ package org.fourthline.cling.support.lastchange;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.StringReader;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.fourthline.cling.model.XMLUtil;
/*     */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
/*     */ import org.fourthline.cling.support.shared.AbstractMap;
/*     */ import org.seamless.util.Exceptions;
/*     */ import org.seamless.util.io.IO;
/*     */ import org.seamless.xml.DOMParser;
/*     */ import org.seamless.xml.SAXParser;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.InputSource;
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
/*     */ public abstract class LastChangeParser
/*     */   extends SAXParser
/*     */ {
/*  60 */   private static final Logger log = Logger.getLogger(LastChangeParser.class.getName());
/*     */   
/*     */   public enum CONSTANTS {
/*  63 */     Event,
/*  64 */     InstanceID,
/*  65 */     val;
/*     */     
/*     */     public boolean equals(String s) {
/*  68 */       return name().equals(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<Class<? extends EventedValue>> getEventedVariables() {
/*  75 */     return Collections.EMPTY_SET;
/*     */   }
/*     */   
/*     */   protected EventedValue createValue(String name, Map.Entry<String, String>[] attributes) throws Exception {
/*  79 */     for (Class<? extends EventedValue> evType : getEventedVariables()) {
/*  80 */       if (evType.getSimpleName().equals(name)) {
/*  81 */         Constructor<? extends EventedValue> ctor = evType.getConstructor(new Class[] { Map.Entry[].class });
/*  82 */         return ctor.newInstance(new Object[] { attributes });
/*     */       } 
/*     */     } 
/*  85 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Event parseResource(String resource) throws Exception {
/*  96 */     InputStream is = null;
/*     */     try {
/*  98 */       is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
/*  99 */       return parse(IO.readLines(is));
/*     */     } finally {
/* 101 */       if (is != null) is.close();
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   public Event parse(String xml) throws Exception {
/* 107 */     if (xml == null || xml.length() == 0) {
/* 108 */       throw new RuntimeException("Null or empty XML");
/*     */     }
/*     */     
/* 111 */     Event event = new Event();
/* 112 */     new RootHandler(event, this);
/*     */     
/* 114 */     if (log.isLoggable(Level.FINE)) {
/* 115 */       log.fine("Parsing 'LastChange' event XML content");
/* 116 */       log.fine("===================================== 'LastChange' BEGIN ============================================");
/* 117 */       log.fine(xml);
/* 118 */       log.fine("====================================== 'LastChange' END  ============================================");
/*     */     } 
/* 120 */     parse(new InputSource(new StringReader(xml)));
/*     */     
/* 122 */     log.fine("Parsed event with instances IDs: " + event.getInstanceIDs().size());
/* 123 */     if (log.isLoggable(Level.FINEST)) {
/* 124 */       for (InstanceID instanceID : event.getInstanceIDs()) {
/* 125 */         log.finest("InstanceID '" + instanceID.getId() + "' has values: " + instanceID.getValues().size());
/* 126 */         for (EventedValue<String> eventedValue : instanceID.getValues()) {
/* 127 */           log.finest(eventedValue.getName() + " => " + eventedValue.getValue());
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 132 */     return event;
/*     */   }
/*     */   
/*     */   class RootHandler
/*     */     extends SAXParser.Handler<Event> {
/*     */     RootHandler(Event instance, SAXParser parser) {
/* 138 */       super(instance, parser);
/*     */     }
/*     */     
/*     */     RootHandler(Event instance) {
/* 142 */       super(instance);
/*     */     }
/*     */ 
/*     */     
/*     */     public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
/* 147 */       super.startElement(uri, localName, qName, attributes);
/* 148 */       if (LastChangeParser.CONSTANTS.InstanceID.equals(localName)) {
/* 149 */         String valAttr = attributes.getValue(LastChangeParser.CONSTANTS.val.name());
/* 150 */         if (valAttr != null) {
/* 151 */           InstanceID instanceID = new InstanceID(new UnsignedIntegerFourBytes(valAttr));
/* 152 */           ((Event)getInstance()).getInstanceIDs().add(instanceID);
/* 153 */           new LastChangeParser.InstanceIDHandler(instanceID, this);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   class InstanceIDHandler
/*     */     extends SAXParser.Handler<InstanceID> {
/*     */     InstanceIDHandler(InstanceID instance, SAXParser.Handler parent) {
/* 162 */       super(instance, parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
/* 167 */       super.startElement(uri, localName, qName, attributes);
/* 168 */       Map.Entry[] attributeMap = new Map.Entry[attributes.getLength()];
/* 169 */       for (int i = 0; i < attributeMap.length; i++) {
/* 170 */         attributeMap[i] = (Map.Entry)new AbstractMap.SimpleEntry(attributes
/*     */             
/* 172 */             .getLocalName(i), attributes
/* 173 */             .getValue(i));
/*     */       }
/*     */       
/*     */       try {
/* 177 */         EventedValue esv = LastChangeParser.this.createValue(localName, (Map.Entry<String, String>[])attributeMap);
/* 178 */         if (esv != null)
/* 179 */           ((InstanceID)getInstance()).getValues().add(esv); 
/* 180 */       } catch (Exception ex) {
/*     */         
/* 182 */         LastChangeParser.log.warning("Error reading event XML, ignoring value: " + Exceptions.unwrap(ex));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isLastElement(String uri, String localName, String qName) {
/* 188 */       return LastChangeParser.CONSTANTS.InstanceID.equals(localName);
/*     */     }
/*     */   }
/*     */   
/*     */   public String generate(Event event) throws Exception {
/* 193 */     return XMLUtil.documentToFragmentString(buildDOM(event));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Document buildDOM(Event event) throws Exception {
/* 198 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/* 199 */     factory.setNamespaceAware(true);
/*     */     
/* 201 */     Document d = factory.newDocumentBuilder().newDocument();
/* 202 */     generateRoot(event, d);
/*     */     
/* 204 */     return d;
/*     */   }
/*     */   
/*     */   protected void generateRoot(Event event, Document descriptor) {
/* 208 */     Element eventElement = descriptor.createElementNS(getNamespace(), CONSTANTS.Event.name());
/* 209 */     descriptor.appendChild(eventElement);
/* 210 */     generateInstanceIDs(event, descriptor, eventElement);
/*     */   }
/*     */   
/*     */   protected void generateInstanceIDs(Event event, Document descriptor, Element rootElement) {
/* 214 */     for (InstanceID instanceID : event.getInstanceIDs()) {
/* 215 */       if (instanceID.getId() == null)
/* 216 */         continue;  Element instanceIDElement = XMLUtil.appendNewElement(descriptor, rootElement, CONSTANTS.InstanceID.name());
/* 217 */       instanceIDElement.setAttribute(CONSTANTS.val.name(), instanceID.getId().toString());
/*     */       
/* 219 */       for (EventedValue eventedValue : instanceID.getValues()) {
/* 220 */         generateEventedValue(eventedValue, descriptor, instanceIDElement);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void generateEventedValue(EventedValue eventedValue, Document descriptor, Element parentElement) {
/* 226 */     String name = eventedValue.getName();
/* 227 */     Map.Entry[] arrayOfEntry = (Map.Entry[])eventedValue.getAttributes();
/* 228 */     if (arrayOfEntry != null && arrayOfEntry.length > 0) {
/* 229 */       Element evElement = XMLUtil.appendNewElement(descriptor, parentElement, name);
/* 230 */       for (Map.Entry<String, String> attr : arrayOfEntry)
/* 231 */         evElement.setAttribute(attr.getKey(), DOMParser.escape(attr.getValue())); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract String getNamespace();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\lastchange\LastChangeParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */