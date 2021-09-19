/*     */ package javax.xml.bind.helpers;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.PropertyException;
/*     */ import javax.xml.bind.ValidationEventHandler;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import javax.xml.bind.attachment.AttachmentMarshaller;
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import javax.xml.transform.sax.SAXResult;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.validation.Schema;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.ContentHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractMarshallerImpl
/*     */   implements Marshaller
/*     */ {
/*  50 */   private ValidationEventHandler eventHandler = new DefaultValidationEventHandler();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private String encoding = "UTF-8";
/*     */ 
/*     */   
/*  60 */   private String schemaLocation = null;
/*     */ 
/*     */   
/*  63 */   private String noNSSchemaLocation = null;
/*     */ 
/*     */   
/*     */   private boolean formattedOutput = false;
/*     */ 
/*     */   
/*     */   private boolean fragment = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public final void marshal(Object obj, OutputStream os) throws JAXBException {
/*  74 */     checkNotNull(obj, "obj", os, "os");
/*  75 */     marshal(obj, new StreamResult(os));
/*     */   }
/*     */   
/*     */   public void marshal(Object jaxbElement, File output) throws JAXBException {
/*  79 */     checkNotNull(jaxbElement, "jaxbElement", output, "output");
/*     */     try {
/*  81 */       OutputStream os = new BufferedOutputStream(new FileOutputStream(output));
/*     */       try {
/*  83 */         marshal(jaxbElement, new StreamResult(os));
/*     */       } finally {
/*  85 */         os.close();
/*     */       } 
/*  87 */     } catch (IOException e) {
/*  88 */       throw new JAXBException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void marshal(Object obj, Writer w) throws JAXBException {
/*  95 */     checkNotNull(obj, "obj", w, "writer");
/*  96 */     marshal(obj, new StreamResult(w));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void marshal(Object obj, ContentHandler handler) throws JAXBException {
/* 102 */     checkNotNull(obj, "obj", handler, "handler");
/* 103 */     marshal(obj, new SAXResult(handler));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void marshal(Object obj, Node node) throws JAXBException {
/* 109 */     checkNotNull(obj, "obj", node, "node");
/* 110 */     marshal(obj, new DOMResult(node));
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
/*     */   public Node getNode(Object obj) throws JAXBException {
/* 122 */     checkNotNull(obj, "obj", Boolean.TRUE, "foo");
/*     */     
/* 124 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getEncoding() {
/* 133 */     return this.encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setEncoding(String encoding) {
/* 143 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getSchemaLocation() {
/* 152 */     return this.schemaLocation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setSchemaLocation(String location) {
/* 161 */     this.schemaLocation = location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getNoNSSchemaLocation() {
/* 171 */     return this.noNSSchemaLocation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setNoNSSchemaLocation(String location) {
/* 180 */     this.noNSSchemaLocation = location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isFormattedOutput() {
/* 190 */     return this.formattedOutput;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setFormattedOutput(boolean v) {
/* 199 */     this.formattedOutput = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isFragment() {
/* 210 */     return this.fragment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setFragment(boolean v) {
/* 219 */     this.fragment = v;
/*     */   }
/*     */ 
/*     */   
/* 223 */   static String[] aliases = new String[] { "UTF-8", "UTF8", "UTF-16", "Unicode", "UTF-16BE", "UnicodeBigUnmarked", "UTF-16LE", "UnicodeLittleUnmarked", "US-ASCII", "ASCII", "TIS-620", "TIS620", "ISO-10646-UCS-2", "Unicode", "EBCDIC-CP-US", "cp037", "EBCDIC-CP-CA", "cp037", "EBCDIC-CP-NL", "cp037", "EBCDIC-CP-WT", "cp037", "EBCDIC-CP-DK", "cp277", "EBCDIC-CP-NO", "cp277", "EBCDIC-CP-FI", "cp278", "EBCDIC-CP-SE", "cp278", "EBCDIC-CP-IT", "cp280", "EBCDIC-CP-ES", "cp284", "EBCDIC-CP-GB", "cp285", "EBCDIC-CP-FR", "cp297", "EBCDIC-CP-AR1", "cp420", "EBCDIC-CP-HE", "cp424", "EBCDIC-CP-BE", "cp500", "EBCDIC-CP-CH", "cp500", "EBCDIC-CP-ROECE", "cp870", "EBCDIC-CP-YU", "cp870", "EBCDIC-CP-IS", "cp871", "EBCDIC-CP-AR2", "cp918" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getJavaEncoding(String encoding) throws UnsupportedEncodingException {
/*     */     try {
/* 275 */       "1".getBytes(encoding);
/* 276 */       return encoding;
/* 277 */     } catch (UnsupportedEncodingException e) {
/*     */       
/* 279 */       for (int i = 0; i < aliases.length; i += 2) {
/* 280 */         if (encoding.equals(aliases[i])) {
/* 281 */           "1".getBytes(aliases[i + 1]);
/* 282 */           return aliases[i + 1];
/*     */         } 
/*     */       } 
/*     */       
/* 286 */       throw new UnsupportedEncodingException(encoding);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String name, Object value) throws PropertyException {
/* 306 */     if (name == null) {
/* 307 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "name"));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 312 */     if ("jaxb.encoding".equals(name)) {
/* 313 */       checkString(name, value);
/* 314 */       setEncoding((String)value);
/*     */       return;
/*     */     } 
/* 317 */     if ("jaxb.formatted.output".equals(name)) {
/* 318 */       checkBoolean(name, value);
/* 319 */       setFormattedOutput(((Boolean)value).booleanValue());
/*     */       return;
/*     */     } 
/* 322 */     if ("jaxb.noNamespaceSchemaLocation".equals(name)) {
/* 323 */       checkString(name, value);
/* 324 */       setNoNSSchemaLocation((String)value);
/*     */       return;
/*     */     } 
/* 327 */     if ("jaxb.schemaLocation".equals(name)) {
/* 328 */       checkString(name, value);
/* 329 */       setSchemaLocation((String)value);
/*     */       return;
/*     */     } 
/* 332 */     if ("jaxb.fragment".equals(name)) {
/* 333 */       checkBoolean(name, value);
/* 334 */       setFragment(((Boolean)value).booleanValue());
/*     */       
/*     */       return;
/*     */     } 
/* 338 */     throw new PropertyException(name, value);
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
/*     */   public Object getProperty(String name) throws PropertyException {
/* 350 */     if (name == null) {
/* 351 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "name"));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 356 */     if ("jaxb.encoding".equals(name))
/* 357 */       return getEncoding(); 
/* 358 */     if ("jaxb.formatted.output".equals(name))
/* 359 */       return isFormattedOutput() ? Boolean.TRUE : Boolean.FALSE; 
/* 360 */     if ("jaxb.noNamespaceSchemaLocation".equals(name))
/* 361 */       return getNoNSSchemaLocation(); 
/* 362 */     if ("jaxb.schemaLocation".equals(name))
/* 363 */       return getSchemaLocation(); 
/* 364 */     if ("jaxb.fragment".equals(name)) {
/* 365 */       return isFragment() ? Boolean.TRUE : Boolean.FALSE;
/*     */     }
/* 367 */     throw new PropertyException(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidationEventHandler getEventHandler() throws JAXBException {
/* 373 */     return this.eventHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEventHandler(ValidationEventHandler handler) throws JAXBException {
/* 382 */     if (handler == null) {
/* 383 */       this.eventHandler = new DefaultValidationEventHandler();
/*     */     } else {
/* 385 */       this.eventHandler = handler;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkBoolean(String name, Object value) throws PropertyException {
/* 396 */     if (!(value instanceof Boolean)) {
/* 397 */       throw new PropertyException(Messages.format("AbstractMarshallerImpl.MustBeBoolean", name));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkString(String name, Object value) throws PropertyException {
/* 405 */     if (!(value instanceof String)) {
/* 406 */       throw new PropertyException(Messages.format("AbstractMarshallerImpl.MustBeString", name));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkNotNull(Object o1, String o1Name, Object o2, String o2Name) {
/* 416 */     if (o1 == null) {
/* 417 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", o1Name));
/*     */     }
/*     */     
/* 420 */     if (o2 == null) {
/* 421 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", o2Name));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void marshal(Object obj, XMLEventWriter writer) throws JAXBException {
/* 429 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void marshal(Object obj, XMLStreamWriter writer) throws JAXBException {
/* 435 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void setSchema(Schema schema) {
/* 439 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Schema getSchema() {
/* 443 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void setAdapter(XmlAdapter adapter) {
/* 447 */     if (adapter == null)
/* 448 */       throw new IllegalArgumentException(); 
/* 449 */     setAdapter(adapter.getClass(), adapter);
/*     */   }
/*     */   
/*     */   public <A extends XmlAdapter> void setAdapter(Class<A> type, A adapter) {
/* 453 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public <A extends XmlAdapter> A getAdapter(Class<A> type) {
/* 457 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void setAttachmentMarshaller(AttachmentMarshaller am) {
/* 461 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public AttachmentMarshaller getAttachmentMarshaller() {
/* 465 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void setListener(Marshaller.Listener listener) {
/* 469 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Marshaller.Listener getListener() {
/* 473 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\helpers\AbstractMarshallerImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */