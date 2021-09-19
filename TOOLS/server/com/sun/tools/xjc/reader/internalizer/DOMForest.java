/*     */ package com.sun.tools.xjc.reader.internalizer;
/*     */ 
/*     */ import com.sun.istack.NotNull;
/*     */ import com.sun.istack.XMLStreamReaderToContentHandler;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.api.ErrorListener;
/*     */ import com.sun.tools.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.xml.bind.marshaller.DataWriter;
/*     */ import com.sun.xml.xsom.parser.JAXPParser;
/*     */ import com.sun.xml.xsom.parser.XMLParser;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.sax.SAXResult;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.validation.SchemaFactory;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DOMForest
/*     */ {
/* 110 */   private final Map<String, Document> core = new HashMap<String, Document>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   private final Set<String> rootDocuments = new HashSet<String>();
/*     */ 
/*     */   
/* 124 */   public final LocatorTable locatorTable = new LocatorTable();
/*     */ 
/*     */   
/* 127 */   public final Set<Element> outerMostBindings = new HashSet<Element>();
/*     */ 
/*     */   
/* 130 */   private EntityResolver entityResolver = null;
/*     */ 
/*     */   
/* 133 */   private ErrorReceiver errorReceiver = null;
/*     */ 
/*     */   
/*     */   protected final InternalizationLogic logic;
/*     */ 
/*     */   
/*     */   private final SAXParserFactory parserFactory;
/*     */ 
/*     */   
/*     */   private final DocumentBuilder documentBuilder;
/*     */ 
/*     */   
/*     */   public DOMForest(SAXParserFactory parserFactory, DocumentBuilder documentBuilder, InternalizationLogic logic) {
/* 146 */     this.parserFactory = parserFactory;
/* 147 */     this.documentBuilder = documentBuilder;
/* 148 */     this.logic = logic;
/*     */   }
/*     */   
/*     */   public DOMForest(InternalizationLogic logic) {
/*     */     try {
/* 153 */       DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/* 154 */       dbf.setNamespaceAware(true);
/* 155 */       this.documentBuilder = dbf.newDocumentBuilder();
/*     */       
/* 157 */       this.parserFactory = SAXParserFactory.newInstance();
/* 158 */       this.parserFactory.setNamespaceAware(true);
/* 159 */     } catch (ParserConfigurationException e) {
/* 160 */       throw new AssertionError(e);
/*     */     } 
/*     */     
/* 163 */     this.logic = logic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Document get(String systemId) {
/* 171 */     Document doc = this.core.get(systemId);
/*     */     
/* 173 */     if (doc == null && systemId.startsWith("file:/") && !systemId.startsWith("file://"))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 180 */       doc = this.core.get("file://" + systemId.substring(5));
/*     */     }
/*     */     
/* 183 */     if (doc == null && systemId.startsWith("file:")) {
/*     */ 
/*     */       
/* 186 */       String systemPath = getPath(systemId);
/* 187 */       for (String key : this.core.keySet()) {
/* 188 */         if (key.startsWith("file:") && getPath(key).equalsIgnoreCase(systemPath)) {
/* 189 */           doc = this.core.get(key);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 195 */     return doc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getPath(String key) {
/* 202 */     key = key.substring(5);
/* 203 */     while (key.length() > 0 && key.charAt(0) == '/')
/* 204 */       key = key.substring(1); 
/* 205 */     return key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getRootDocuments() {
/* 212 */     return Collections.unmodifiableSet(this.rootDocuments);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Document getOneDocument() {
/* 219 */     for (Document dom : this.core.values()) {
/* 220 */       if (!dom.getDocumentElement().getNamespaceURI().equals("http://java.sun.com/xml/ns/jaxb")) {
/* 221 */         return dom;
/*     */       }
/*     */     } 
/* 224 */     throw new AssertionError();
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
/*     */   public boolean checkSchemaCorrectness(ErrorReceiver errorHandler) {
/*     */     try {
/* 240 */       SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
/* 241 */       ErrorReceiverFilter filter = new ErrorReceiverFilter((ErrorListener)errorHandler);
/* 242 */       sf.setErrorHandler((ErrorHandler)filter);
/* 243 */       Set<String> roots = getRootDocuments();
/* 244 */       Source[] sources = new Source[roots.size()];
/* 245 */       int i = 0;
/* 246 */       for (String root : roots) {
/* 247 */         sources[i++] = new DOMSource(get(root), root);
/*     */       }
/* 249 */       sf.newSchema(sources);
/* 250 */       return !filter.hadError();
/* 251 */     } catch (SAXException e) {
/*     */       
/* 253 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSystemId(Document dom) {
/* 263 */     for (Map.Entry<String, Document> e : this.core.entrySet()) {
/* 264 */       if (e.getValue() == dom)
/* 265 */         return e.getKey(); 
/*     */     } 
/* 267 */     return null;
/*     */   }
/*     */   
/*     */   public Document parse(InputSource source, boolean root) throws SAXException {
/* 271 */     if (source.getSystemId() == null) {
/* 272 */       throw new IllegalArgumentException();
/*     */     }
/* 274 */     return parse(source.getSystemId(), source, root);
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
/*     */   public Document parse(String systemId, boolean root) throws SAXException, IOException {
/* 286 */     systemId = normalizeSystemId(systemId);
/*     */     
/* 288 */     if (this.core.containsKey(systemId))
/*     */     {
/* 290 */       return this.core.get(systemId);
/*     */     }
/* 292 */     InputSource is = null;
/*     */ 
/*     */     
/* 295 */     if (this.entityResolver != null)
/* 296 */       is = this.entityResolver.resolveEntity(null, systemId); 
/* 297 */     if (is == null) {
/* 298 */       is = new InputSource(systemId);
/*     */     }
/*     */     
/* 301 */     return parse(systemId, is, root);
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
/*     */   private ContentHandler getParserHandler(Document dom) {
/* 315 */     DOMBuilder dOMBuilder = new DOMBuilder(dom, this.locatorTable, this.outerMostBindings);
/* 316 */     WhitespaceStripper whitespaceStripper = new WhitespaceStripper((ContentHandler)dOMBuilder, (ErrorHandler)this.errorReceiver, this.entityResolver);
/* 317 */     VersionChecker versionChecker = new VersionChecker(whitespaceStripper, (ErrorHandler)this.errorReceiver, this.entityResolver);
/*     */ 
/*     */ 
/*     */     
/* 321 */     XMLFilterImpl f = this.logic.createExternalReferenceFinder(this);
/* 322 */     f.setContentHandler(versionChecker);
/*     */     
/* 324 */     if (this.errorReceiver != null)
/* 325 */       f.setErrorHandler((ErrorHandler)this.errorReceiver); 
/* 326 */     if (this.entityResolver != null) {
/* 327 */       f.setEntityResolver(this.entityResolver);
/*     */     }
/* 329 */     return f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class HandlerImpl
/*     */     extends XMLFilterImpl
/*     */     implements Handler
/*     */   {
/*     */     private HandlerImpl() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Handler getParserHandler(String systemId, boolean root) {
/* 350 */     final Document dom = this.documentBuilder.newDocument();
/* 351 */     this.core.put(systemId, dom);
/* 352 */     if (root) {
/* 353 */       this.rootDocuments.add(systemId);
/*     */     }
/* 355 */     ContentHandler handler = getParserHandler(dom);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 360 */     HandlerImpl x = new HandlerImpl() {
/*     */         public Document getDocument() {
/* 362 */           return dom;
/*     */         }
/*     */       };
/* 365 */     x.setContentHandler(handler);
/*     */     
/* 367 */     return x;
/*     */   }
/*     */   
/*     */   public static interface Handler
/*     */     extends ContentHandler
/*     */   {
/*     */     Document getDocument();
/*     */   }
/*     */   
/*     */   public Document parse(String systemId, InputSource inputSource, boolean root) throws SAXException {
/* 377 */     Document dom = this.documentBuilder.newDocument();
/*     */     
/* 379 */     systemId = normalizeSystemId(systemId);
/*     */ 
/*     */ 
/*     */     
/* 383 */     this.core.put(systemId, dom);
/* 384 */     if (root) {
/* 385 */       this.rootDocuments.add(systemId);
/*     */     }
/*     */     try {
/* 388 */       XMLReader reader = this.parserFactory.newSAXParser().getXMLReader();
/* 389 */       reader.setContentHandler(getParserHandler(dom));
/* 390 */       if (this.errorReceiver != null)
/* 391 */         reader.setErrorHandler((ErrorHandler)this.errorReceiver); 
/* 392 */       if (this.entityResolver != null)
/* 393 */         reader.setEntityResolver(this.entityResolver); 
/* 394 */       reader.parse(inputSource);
/* 395 */     } catch (ParserConfigurationException e) {
/*     */       
/* 397 */       this.errorReceiver.error(e.getMessage(), e);
/* 398 */       this.core.remove(systemId);
/* 399 */       this.rootDocuments.remove(systemId);
/* 400 */       return null;
/* 401 */     } catch (IOException e) {
/* 402 */       this.errorReceiver.error(e.getMessage(), e);
/* 403 */       this.core.remove(systemId);
/* 404 */       this.rootDocuments.remove(systemId);
/* 405 */       return null;
/*     */     } 
/*     */     
/* 408 */     return dom;
/*     */   }
/*     */   
/*     */   private String normalizeSystemId(String systemId) {
/*     */     try {
/* 413 */       systemId = (new URI(systemId)).normalize().toString();
/* 414 */     } catch (URISyntaxException e) {}
/*     */ 
/*     */     
/* 417 */     return systemId;
/*     */   }
/*     */   
/*     */   public Document parse(String systemId, XMLStreamReader parser, boolean root) throws XMLStreamException {
/* 421 */     Document dom = this.documentBuilder.newDocument();
/*     */     
/* 423 */     systemId = normalizeSystemId(systemId);
/*     */     
/* 425 */     if (root) {
/* 426 */       this.rootDocuments.add(systemId);
/*     */     }
/* 428 */     if (systemId == null)
/* 429 */       throw new IllegalArgumentException("system id cannot be null"); 
/* 430 */     this.core.put(systemId, dom);
/*     */     
/* 432 */     (new XMLStreamReaderToContentHandler(parser, getParserHandler(dom), false, false)).bridge();
/*     */     
/* 434 */     return dom;
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
/*     */   public SCDBasedBindingSet transform(boolean enableSCD) {
/* 448 */     return Internalizer.transform(this, enableSCD);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void weakSchemaCorrectnessCheck(SchemaFactory sf) {
/* 471 */     List<SAXSource> sources = new ArrayList<SAXSource>();
/* 472 */     for (String systemId : getRootDocuments()) {
/* 473 */       Document dom = get(systemId);
/* 474 */       if (dom.getDocumentElement().getNamespaceURI().equals("http://java.sun.com/xml/ns/jaxb")) {
/*     */         continue;
/*     */       }
/* 477 */       SAXSource ss = createSAXSource(systemId);
/*     */       try {
/* 479 */         ss.getXMLReader().setFeature("http://xml.org/sax/features/namespace-prefixes", true);
/* 480 */       } catch (SAXException e) {
/* 481 */         throw new AssertionError(e);
/*     */       } 
/* 483 */       sources.add(ss);
/*     */     } 
/*     */     
/*     */     try {
/* 487 */       sf.newSchema(sources.<Source>toArray((Source[])new SAXSource[0]));
/* 488 */     } catch (SAXException e) {
/*     */     
/* 490 */     } catch (RuntimeException e) {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 495 */         sf.getErrorHandler().warning(new SAXParseException(Messages.format("ERR_GENERAL_SCHEMA_CORRECTNESS_ERROR", new Object[] { e.getMessage() }), null, null, -1, -1, e));
/*     */ 
/*     */       
/*     */       }
/* 499 */       catch (SAXException _) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public SAXSource createSAXSource(String systemId) {
/* 510 */     ContentHandlerNamespacePrefixAdapter reader = new ContentHandlerNamespacePrefixAdapter(new XMLFilterImpl()
/*     */         {
/*     */           public void parse(InputSource input) throws SAXException, IOException
/*     */           {
/* 514 */             DOMForest.this.createParser().parse(input, this, this, this);
/*     */           }
/*     */           
/*     */           public void parse(String systemId) throws SAXException, IOException {
/* 518 */             parse(new InputSource(systemId));
/*     */           }
/*     */         });
/*     */     
/* 522 */     return new SAXSource(reader, new InputSource(systemId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLParser createParser() {
/* 533 */     return new DOMForestParser(this, (XMLParser)new JAXPParser());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityResolver getEntityResolver() {
/* 539 */     return this.entityResolver;
/*     */   }
/*     */   
/*     */   public void setEntityResolver(EntityResolver entityResolver) {
/* 543 */     this.entityResolver = entityResolver;
/*     */   }
/*     */   
/*     */   public ErrorReceiver getErrorHandler() {
/* 547 */     return this.errorReceiver;
/*     */   }
/*     */   
/*     */   public void setErrorHandler(ErrorReceiver errorHandler) {
/* 551 */     this.errorReceiver = errorHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Document[] listDocuments() {
/* 558 */     return (Document[])this.core.values().toArray((Object[])new Document[this.core.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] listSystemIDs() {
/* 565 */     return (String[])this.core.keySet().toArray((Object[])new String[this.core.keySet().size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dump(OutputStream out) throws IOException {
/*     */     try {
/* 576 */       Transformer it = TransformerFactory.newInstance().newTransformer();
/*     */       
/* 578 */       for (Map.Entry<String, Document> e : this.core.entrySet()) {
/* 579 */         out.write(("---<< " + (String)e.getKey() + '\n').getBytes());
/*     */         
/* 581 */         DataWriter dw = new DataWriter(new OutputStreamWriter(out), null);
/* 582 */         dw.setIndentStep("  ");
/* 583 */         it.transform(new DOMSource(e.getValue()), new SAXResult((ContentHandler)dw));
/*     */ 
/*     */         
/* 586 */         out.write("\n\n\n".getBytes());
/*     */       } 
/* 588 */     } catch (TransformerException e) {
/* 589 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\internalizer\DOMForest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */