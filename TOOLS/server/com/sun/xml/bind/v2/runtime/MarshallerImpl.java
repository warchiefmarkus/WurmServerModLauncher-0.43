/*     */ package com.sun.xml.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.xml.bind.DatatypeConverterImpl;
/*     */ import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
/*     */ import com.sun.xml.bind.marshaller.DataWriter;
/*     */ import com.sun.xml.bind.marshaller.DumbEscapeHandler;
/*     */ import com.sun.xml.bind.marshaller.MinimumEscapeHandler;
/*     */ import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
/*     */ import com.sun.xml.bind.marshaller.NioEscapeHandler;
/*     */ import com.sun.xml.bind.marshaller.SAX2DOMEx;
/*     */ import com.sun.xml.bind.marshaller.XMLWriter;
/*     */ import com.sun.xml.bind.v2.runtime.output.C14nXmlOutput;
/*     */ import com.sun.xml.bind.v2.runtime.output.Encoded;
/*     */ import com.sun.xml.bind.v2.runtime.output.ForkXmlOutput;
/*     */ import com.sun.xml.bind.v2.runtime.output.IndentingUTF8XmlOutput;
/*     */ import com.sun.xml.bind.v2.runtime.output.NamespaceContextImpl;
/*     */ import com.sun.xml.bind.v2.runtime.output.SAXOutput;
/*     */ import com.sun.xml.bind.v2.runtime.output.UTF8XmlOutput;
/*     */ import com.sun.xml.bind.v2.runtime.output.XMLEventWriterOutput;
/*     */ import com.sun.xml.bind.v2.runtime.output.XMLStreamWriterOutput;
/*     */ import com.sun.xml.bind.v2.runtime.output.XmlOutput;
/*     */ import com.sun.xml.bind.v2.util.FatalAdapter;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.Closeable;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.Flushable;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import javax.xml.bind.DatatypeConverter;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.MarshalException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.PropertyException;
/*     */ import javax.xml.bind.ValidationEvent;
/*     */ import javax.xml.bind.ValidationEventHandler;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import javax.xml.bind.attachment.AttachmentMarshaller;
/*     */ import javax.xml.bind.helpers.AbstractMarshallerImpl;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import javax.xml.transform.sax.SAXResult;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.validation.Schema;
/*     */ import javax.xml.validation.ValidatorHandler;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
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
/*     */ public final class MarshallerImpl
/*     */   extends AbstractMarshallerImpl
/*     */   implements ValidationEventHandler
/*     */ {
/* 109 */   private String indent = "    ";
/*     */ 
/*     */   
/* 112 */   private NamespacePrefixMapper prefixMapper = null;
/*     */ 
/*     */   
/* 115 */   private CharacterEscapeHandler escapeHandler = null;
/*     */ 
/*     */   
/* 118 */   private String header = null;
/*     */ 
/*     */ 
/*     */   
/*     */   final JAXBContextImpl context;
/*     */ 
/*     */   
/*     */   protected final XMLSerializer serializer;
/*     */ 
/*     */   
/*     */   private Schema schema;
/*     */ 
/*     */   
/* 131 */   private Marshaller.Listener externalListener = null;
/*     */   
/*     */   private boolean c14nSupport;
/*     */   
/*     */   private Flushable toBeFlushed;
/*     */   private Closeable toBeClosed;
/*     */   protected static final String INDENT_STRING = "com.sun.xml.bind.indentString";
/*     */   protected static final String PREFIX_MAPPER = "com.sun.xml.bind.namespacePrefixMapper";
/*     */   protected static final String ENCODING_HANDLER = "com.sun.xml.bind.characterEscapeHandler";
/*     */   protected static final String ENCODING_HANDLER2 = "com.sun.xml.bind.marshaller.CharacterEscapeHandler";
/*     */   protected static final String XMLDECLARATION = "com.sun.xml.bind.xmlDeclaration";
/*     */   protected static final String XML_HEADERS = "com.sun.xml.bind.xmlHeaders";
/*     */   protected static final String C14N = "com.sun.xml.bind.c14n";
/*     */   protected static final String OBJECT_IDENTITY_CYCLE_DETECTION = "com.sun.xml.bind.objectIdentitityCycleDetection";
/*     */   
/*     */   public MarshallerImpl(JAXBContextImpl c, AssociationMap assoc) {
/* 147 */     DatatypeConverter.setDatatypeConverter(DatatypeConverterImpl.theInstance);
/*     */     
/* 149 */     this.context = c;
/* 150 */     this.serializer = new XMLSerializer(this);
/* 151 */     this.c14nSupport = this.context.c14nSupport;
/*     */     
/*     */     try {
/* 154 */       setEventHandler(this);
/* 155 */     } catch (JAXBException e) {
/* 156 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public JAXBContextImpl getContext() {
/* 161 */     return this.context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void marshal(Object obj, OutputStream out, NamespaceContext inscopeNamespace) throws JAXBException {
/* 171 */     write(obj, createWriter(out), new StAXPostInitAction(inscopeNamespace, this.serializer));
/*     */   }
/*     */   
/*     */   public void marshal(Object obj, XMLStreamWriter writer) throws JAXBException {
/* 175 */     write(obj, XMLStreamWriterOutput.create(writer, this.context), new StAXPostInitAction(writer, this.serializer));
/*     */   }
/*     */   
/*     */   public void marshal(Object obj, XMLEventWriter writer) throws JAXBException {
/* 179 */     write(obj, (XmlOutput)new XMLEventWriterOutput(writer), new StAXPostInitAction(writer, this.serializer));
/*     */   }
/*     */   
/*     */   public void marshal(Object obj, XmlOutput output) throws JAXBException {
/* 183 */     write(obj, output, (Runnable)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final XmlOutput createXmlOutput(Result result) throws JAXBException {
/* 190 */     if (result instanceof SAXResult) {
/* 191 */       return (XmlOutput)new SAXOutput(((SAXResult)result).getHandler());
/*     */     }
/* 193 */     if (result instanceof DOMResult) {
/* 194 */       Node node = ((DOMResult)result).getNode();
/*     */       
/* 196 */       if (node == null) {
/* 197 */         Document doc = JAXBContextImpl.createDom();
/* 198 */         ((DOMResult)result).setNode(doc);
/* 199 */         return (XmlOutput)new SAXOutput((ContentHandler)new SAX2DOMEx(doc));
/*     */       } 
/* 201 */       return (XmlOutput)new SAXOutput((ContentHandler)new SAX2DOMEx(node));
/*     */     } 
/*     */     
/* 204 */     if (result instanceof StreamResult) {
/* 205 */       StreamResult sr = (StreamResult)result;
/*     */       
/* 207 */       if (sr.getWriter() != null)
/* 208 */         return createWriter(sr.getWriter()); 
/* 209 */       if (sr.getOutputStream() != null)
/* 210 */         return createWriter(sr.getOutputStream()); 
/* 211 */       if (sr.getSystemId() != null) {
/* 212 */         String fileURL = sr.getSystemId();
/*     */         
/* 214 */         if (fileURL.startsWith("file:///"))
/* 215 */           if (fileURL.substring(8).indexOf(":") > 0) {
/* 216 */             fileURL = fileURL.substring(8);
/*     */           } else {
/* 218 */             fileURL = fileURL.substring(7);
/*     */           }  
/* 220 */         if (fileURL.startsWith("file:/"))
/*     */         {
/*     */           
/* 223 */           if (fileURL.substring(6).indexOf(":") > 0) {
/* 224 */             fileURL = fileURL.substring(6);
/*     */           } else {
/* 226 */             fileURL = fileURL.substring(5);
/*     */           } 
/*     */         }
/*     */         
/*     */         try {
/* 231 */           FileOutputStream fos = new FileOutputStream(fileURL);
/* 232 */           assert this.toBeClosed == null;
/* 233 */           this.toBeClosed = fos;
/* 234 */           return createWriter(fos);
/* 235 */         } catch (IOException e) {
/* 236 */           throw new MarshalException(e);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 242 */     throw new MarshalException(Messages.UNSUPPORTED_RESULT.format(new Object[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final Runnable createPostInitAction(Result result) {
/* 249 */     if (result instanceof DOMResult) {
/* 250 */       Node node = ((DOMResult)result).getNode();
/* 251 */       return new DomPostInitAction(node, this.serializer);
/*     */     } 
/* 253 */     return null;
/*     */   }
/*     */   
/*     */   public void marshal(Object target, Result result) throws JAXBException {
/* 257 */     write(target, createXmlOutput(result), createPostInitAction(result));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final <T> void write(Name rootTagName, JaxBeanInfo<T> bi, T obj, XmlOutput out, Runnable postInitAction) throws JAXBException {
/*     */     try {
/*     */       try {
/* 267 */         prewrite(out, true, postInitAction);
/* 268 */         this.serializer.startElement(rootTagName, (Object)null);
/* 269 */         if (bi.jaxbType == Void.class || bi.jaxbType == void.class) {
/*     */           
/* 271 */           this.serializer.endNamespaceDecls((Object)null);
/* 272 */           this.serializer.endAttributes();
/*     */         }
/* 274 */         else if (obj == null) {
/* 275 */           this.serializer.writeXsiNilTrue();
/*     */         } else {
/* 277 */           this.serializer.childAsXsiType(obj, "root", bi);
/*     */         } 
/* 279 */         this.serializer.endElement();
/* 280 */         postwrite();
/* 281 */       } catch (SAXException e) {
/* 282 */         throw new MarshalException(e);
/* 283 */       } catch (IOException e) {
/* 284 */         throw new MarshalException(e);
/* 285 */       } catch (XMLStreamException e) {
/* 286 */         throw new MarshalException(e);
/*     */       } finally {
/* 288 */         this.serializer.close();
/*     */       } 
/*     */     } finally {
/* 291 */       cleanUp();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void write(Object obj, XmlOutput out, Runnable postInitAction) throws JAXBException {
/*     */     try {
/*     */       ForkXmlOutput forkXmlOutput;
/* 300 */       if (obj == null) {
/* 301 */         throw new IllegalArgumentException(Messages.NOT_MARSHALLABLE.format(new Object[0]));
/*     */       }
/* 303 */       if (this.schema != null) {
/*     */         
/* 305 */         ValidatorHandler validator = this.schema.newValidatorHandler();
/* 306 */         validator.setErrorHandler((ErrorHandler)new FatalAdapter(this.serializer));
/*     */         
/* 308 */         XMLFilterImpl f = new XMLFilterImpl() {
/*     */             public void startPrefixMapping(String prefix, String uri) throws SAXException {
/* 310 */               super.startPrefixMapping(prefix.intern(), uri.intern());
/*     */             }
/*     */           };
/* 313 */         f.setContentHandler(validator);
/* 314 */         forkXmlOutput = new ForkXmlOutput((XmlOutput)new SAXOutput(f)
/*     */             {
/*     */               public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws SAXException, IOException, XMLStreamException {
/* 317 */                 super.startDocument(serializer, false, nsUriIndex2prefixIndex, nsContext);
/*     */               }
/*     */               
/*     */               public void endDocument(boolean fragment) throws SAXException, IOException, XMLStreamException {
/* 321 */                 super.endDocument(false);
/*     */               }
/*     */             }out);
/*     */       } 
/*     */       
/*     */       try {
/* 327 */         prewrite((XmlOutput)forkXmlOutput, isFragment(), postInitAction);
/* 328 */         this.serializer.childAsRoot(obj);
/* 329 */         postwrite();
/* 330 */       } catch (SAXException e) {
/* 331 */         throw new MarshalException(e);
/* 332 */       } catch (IOException e) {
/* 333 */         throw new MarshalException(e);
/* 334 */       } catch (XMLStreamException e) {
/* 335 */         throw new MarshalException(e);
/*     */       } finally {
/* 337 */         this.serializer.close();
/*     */       } 
/*     */     } finally {
/* 340 */       cleanUp();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void cleanUp() {
/* 345 */     if (this.toBeFlushed != null) {
/*     */       try {
/* 347 */         this.toBeFlushed.flush();
/* 348 */       } catch (IOException e) {}
/*     */     }
/*     */     
/* 351 */     if (this.toBeClosed != null) {
/*     */       try {
/* 353 */         this.toBeClosed.close();
/* 354 */       } catch (IOException e) {}
/*     */     }
/*     */     
/* 357 */     this.toBeFlushed = null;
/* 358 */     this.toBeClosed = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void prewrite(XmlOutput out, boolean fragment, Runnable postInitAction) throws IOException, SAXException, XMLStreamException {
/* 364 */     this.serializer.startDocument(out, fragment, getSchemaLocation(), getNoNSSchemaLocation());
/* 365 */     if (postInitAction != null) postInitAction.run(); 
/* 366 */     if (this.prefixMapper != null) {
/*     */       
/* 368 */       String[] decls = this.prefixMapper.getContextualNamespaceDecls();
/* 369 */       if (decls != null)
/* 370 */         for (int i = 0; i < decls.length; i += 2) {
/* 371 */           String prefix = decls[i];
/* 372 */           String nsUri = decls[i + 1];
/* 373 */           if (nsUri != null && prefix != null) {
/* 374 */             this.serializer.addInscopeBinding(nsUri, prefix);
/*     */           }
/*     */         }  
/*     */     } 
/* 378 */     this.serializer.setPrefixMapper(this.prefixMapper);
/*     */   }
/*     */   
/*     */   private void postwrite() throws IOException, SAXException, XMLStreamException {
/* 382 */     this.serializer.endDocument();
/* 383 */     this.serializer.reconcileID();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CharacterEscapeHandler createEscapeHandler(String encoding) {
/* 394 */     if (this.escapeHandler != null)
/*     */     {
/* 396 */       return this.escapeHandler;
/*     */     }
/* 398 */     if (encoding.startsWith("UTF"))
/*     */     {
/*     */       
/* 401 */       return MinimumEscapeHandler.theInstance;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 406 */       return (CharacterEscapeHandler)new NioEscapeHandler(getJavaEncoding(encoding));
/* 407 */     } catch (Throwable e) {
/*     */       
/* 409 */       return DumbEscapeHandler.theInstance;
/*     */     } 
/*     */   }
/*     */   
/*     */   public XmlOutput createWriter(Writer w, String encoding) {
/*     */     XMLWriter xw;
/* 415 */     if (!(w instanceof BufferedWriter)) {
/* 416 */       w = new BufferedWriter(w);
/*     */     }
/* 418 */     assert this.toBeFlushed == null;
/* 419 */     this.toBeFlushed = w;
/*     */     
/* 421 */     CharacterEscapeHandler ceh = createEscapeHandler(encoding);
/*     */ 
/*     */     
/* 424 */     if (isFormattedOutput()) {
/* 425 */       DataWriter d = new DataWriter(w, encoding, ceh);
/* 426 */       d.setIndentStep(this.indent);
/* 427 */       DataWriter dataWriter1 = d;
/*     */     } else {
/* 429 */       xw = new XMLWriter(w, encoding, ceh);
/*     */     } 
/* 431 */     xw.setXmlDecl(!isFragment());
/* 432 */     xw.setHeader(this.header);
/* 433 */     return (XmlOutput)new SAXOutput((ContentHandler)xw);
/*     */   }
/*     */   
/*     */   public XmlOutput createWriter(Writer w) {
/* 437 */     return createWriter(w, getEncoding());
/*     */   }
/*     */   
/*     */   public XmlOutput createWriter(OutputStream os) throws JAXBException {
/* 441 */     return createWriter(os, getEncoding());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlOutput createWriter(OutputStream os, String encoding) throws JAXBException {
/* 449 */     if (encoding.equals("UTF-8")) {
/* 450 */       UTF8XmlOutput out; Encoded[] table = this.context.getUTF8NameTable();
/*     */       
/* 452 */       if (isFormattedOutput()) {
/* 453 */         IndentingUTF8XmlOutput indentingUTF8XmlOutput = new IndentingUTF8XmlOutput(os, this.indent, table);
/*     */       }
/* 455 */       else if (this.c14nSupport) {
/* 456 */         C14nXmlOutput c14nXmlOutput = new C14nXmlOutput(os, table, this.context.c14nSupport);
/*     */       } else {
/* 458 */         out = new UTF8XmlOutput(os, table);
/*     */       } 
/* 460 */       if (this.header != null)
/* 461 */         out.setHeader(this.header); 
/* 462 */       return (XmlOutput)out;
/*     */     } 
/*     */     
/*     */     try {
/* 466 */       return createWriter(new OutputStreamWriter(os, getJavaEncoding(encoding)), encoding);
/*     */     
/*     */     }
/* 469 */     catch (UnsupportedEncodingException e) {
/* 470 */       throw new MarshalException(Messages.UNSUPPORTED_ENCODING.format(new Object[] { encoding }, ), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getProperty(String name) throws PropertyException {
/* 478 */     if ("com.sun.xml.bind.indentString".equals(name))
/* 479 */       return this.indent; 
/* 480 */     if ("com.sun.xml.bind.characterEscapeHandler".equals(name) || "com.sun.xml.bind.marshaller.CharacterEscapeHandler".equals(name))
/* 481 */       return this.escapeHandler; 
/* 482 */     if ("com.sun.xml.bind.namespacePrefixMapper".equals(name))
/* 483 */       return this.prefixMapper; 
/* 484 */     if ("com.sun.xml.bind.xmlDeclaration".equals(name))
/* 485 */       return Boolean.valueOf(!isFragment()); 
/* 486 */     if ("com.sun.xml.bind.xmlHeaders".equals(name))
/* 487 */       return this.header; 
/* 488 */     if ("com.sun.xml.bind.c14n".equals(name))
/* 489 */       return Boolean.valueOf(this.c14nSupport); 
/* 490 */     if ("com.sun.xml.bind.objectIdentitityCycleDetection".equals(name)) {
/* 491 */       return Boolean.valueOf(this.serializer.getObjectIdentityCycleDetection());
/*     */     }
/*     */     
/* 494 */     return super.getProperty(name);
/*     */   }
/*     */   
/*     */   public void setProperty(String name, Object value) throws PropertyException {
/* 498 */     if ("com.sun.xml.bind.indentString".equals(name)) {
/* 499 */       checkString(name, value);
/* 500 */       this.indent = (String)value;
/*     */       return;
/*     */     } 
/* 503 */     if ("com.sun.xml.bind.characterEscapeHandler".equals(name) || "com.sun.xml.bind.marshaller.CharacterEscapeHandler".equals(name)) {
/* 504 */       if (!(value instanceof CharacterEscapeHandler)) {
/* 505 */         throw new PropertyException(Messages.MUST_BE_X.format(new Object[] { name, CharacterEscapeHandler.class.getName(), value.getClass().getName() }));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 510 */       this.escapeHandler = (CharacterEscapeHandler)value;
/*     */       return;
/*     */     } 
/* 513 */     if ("com.sun.xml.bind.namespacePrefixMapper".equals(name)) {
/* 514 */       if (!(value instanceof NamespacePrefixMapper)) {
/* 515 */         throw new PropertyException(Messages.MUST_BE_X.format(new Object[] { name, NamespacePrefixMapper.class.getName(), value.getClass().getName() }));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 520 */       this.prefixMapper = (NamespacePrefixMapper)value;
/*     */       return;
/*     */     } 
/* 523 */     if ("com.sun.xml.bind.xmlDeclaration".equals(name)) {
/* 524 */       checkBoolean(name, value);
/*     */ 
/*     */       
/* 527 */       super.setProperty("jaxb.fragment", Boolean.valueOf(!((Boolean)value).booleanValue()));
/*     */       return;
/*     */     } 
/* 530 */     if ("com.sun.xml.bind.xmlHeaders".equals(name)) {
/* 531 */       checkString(name, value);
/* 532 */       this.header = (String)value;
/*     */       return;
/*     */     } 
/* 535 */     if ("com.sun.xml.bind.c14n".equals(name)) {
/* 536 */       checkBoolean(name, value);
/* 537 */       this.c14nSupport = ((Boolean)value).booleanValue();
/*     */       return;
/*     */     } 
/* 540 */     if ("com.sun.xml.bind.objectIdentitityCycleDetection".equals(name)) {
/* 541 */       checkBoolean(name, value);
/* 542 */       this.serializer.setObjectIdentityCycleDetection(((Boolean)value).booleanValue());
/*     */       
/*     */       return;
/*     */     } 
/* 546 */     super.setProperty(name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkBoolean(String name, Object value) throws PropertyException {
/* 553 */     if (!(value instanceof Boolean)) {
/* 554 */       throw new PropertyException(Messages.MUST_BE_X.format(new Object[] { name, Boolean.class.getName(), value.getClass().getName() }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkString(String name, Object value) throws PropertyException {
/* 565 */     if (!(value instanceof String)) {
/* 566 */       throw new PropertyException(Messages.MUST_BE_X.format(new Object[] { name, String.class.getName(), value.getClass().getName() }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends XmlAdapter> void setAdapter(Class<A> type, A adapter) {
/* 575 */     if (type == null)
/* 576 */       throw new IllegalArgumentException(); 
/* 577 */     this.serializer.putAdapter(type, (XmlAdapter)adapter);
/*     */   }
/*     */ 
/*     */   
/*     */   public <A extends XmlAdapter> A getAdapter(Class<A> type) {
/* 582 */     if (type == null)
/* 583 */       throw new IllegalArgumentException(); 
/* 584 */     if (this.serializer.containsAdapter(type))
/*     */     {
/* 586 */       return this.serializer.getAdapter(type);
/*     */     }
/* 588 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAttachmentMarshaller(AttachmentMarshaller am) {
/* 593 */     this.serializer.attachmentMarshaller = am;
/*     */   }
/*     */ 
/*     */   
/*     */   public AttachmentMarshaller getAttachmentMarshaller() {
/* 598 */     return this.serializer.attachmentMarshaller;
/*     */   }
/*     */   
/*     */   public Schema getSchema() {
/* 602 */     return this.schema;
/*     */   }
/*     */   
/*     */   public void setSchema(Schema s) {
/* 606 */     this.schema = s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleEvent(ValidationEvent event) {
/* 614 */     return false;
/*     */   }
/*     */   
/*     */   public Marshaller.Listener getListener() {
/* 618 */     return this.externalListener;
/*     */   }
/*     */   
/*     */   public void setListener(Marshaller.Listener listener) {
/* 622 */     this.externalListener = listener;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\MarshallerImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */