/*     */ package javax.xml.bind.helpers;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.PropertyException;
/*     */ import javax.xml.bind.UnmarshalException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.ValidationEventHandler;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import javax.xml.bind.attachment.AttachmentUnmarshaller;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.validation.Schema;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractUnmarshallerImpl
/*     */   implements Unmarshaller
/*     */ {
/*  56 */   private ValidationEventHandler eventHandler = new DefaultValidationEventHandler();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean validating = false;
/*     */ 
/*     */ 
/*     */   
/*  65 */   private XMLReader reader = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected XMLReader getXMLReader() throws JAXBException {
/*  77 */     if (this.reader == null) {
/*     */       
/*     */       try {
/*  80 */         SAXParserFactory parserFactory = SAXParserFactory.newInstance();
/*  81 */         parserFactory.setNamespaceAware(true);
/*     */ 
/*     */ 
/*     */         
/*  85 */         parserFactory.setValidating(false);
/*  86 */         this.reader = parserFactory.newSAXParser().getXMLReader();
/*  87 */       } catch (ParserConfigurationException e) {
/*  88 */         throw new JAXBException(e);
/*  89 */       } catch (SAXException e) {
/*  90 */         throw new JAXBException(e);
/*     */       } 
/*     */     }
/*  93 */     return this.reader;
/*     */   }
/*     */   
/*     */   public Object unmarshal(Source source) throws JAXBException {
/*  97 */     if (source == null) {
/*  98 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "source"));
/*     */     }
/*     */ 
/*     */     
/* 102 */     if (source instanceof SAXSource)
/* 103 */       return unmarshal((SAXSource)source); 
/* 104 */     if (source instanceof StreamSource)
/* 105 */       return unmarshal(streamSourceToInputSource((StreamSource)source)); 
/* 106 */     if (source instanceof DOMSource) {
/* 107 */       return unmarshal(((DOMSource)source).getNode());
/*     */     }
/*     */     
/* 110 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object unmarshal(SAXSource source) throws JAXBException {
/* 116 */     XMLReader reader = source.getXMLReader();
/* 117 */     if (reader == null) {
/* 118 */       reader = getXMLReader();
/*     */     }
/* 120 */     return unmarshal(reader, source.getInputSource());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Object unmarshal(XMLReader paramXMLReader, InputSource paramInputSource) throws JAXBException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object unmarshal(InputSource source) throws JAXBException {
/* 132 */     if (source == null) {
/* 133 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "source"));
/*     */     }
/*     */ 
/*     */     
/* 137 */     return unmarshal(getXMLReader(), source);
/*     */   }
/*     */ 
/*     */   
/*     */   private Object unmarshal(String url) throws JAXBException {
/* 142 */     return unmarshal(new InputSource(url));
/*     */   }
/*     */   
/*     */   public final Object unmarshal(URL url) throws JAXBException {
/* 146 */     if (url == null) {
/* 147 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "url"));
/*     */     }
/*     */ 
/*     */     
/* 151 */     return unmarshal(url.toExternalForm());
/*     */   }
/*     */   
/*     */   public final Object unmarshal(File f) throws JAXBException {
/* 155 */     if (f == null) {
/* 156 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "file"));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 162 */       String path = f.getAbsolutePath();
/* 163 */       if (File.separatorChar != '/')
/* 164 */         path = path.replace(File.separatorChar, '/'); 
/* 165 */       if (!path.startsWith("/"))
/* 166 */         path = "/" + path; 
/* 167 */       if (!path.endsWith("/") && f.isDirectory())
/* 168 */         path = path + "/"; 
/* 169 */       return unmarshal(new URL("file", "", path));
/* 170 */     } catch (MalformedURLException e) {
/* 171 */       throw new IllegalArgumentException(e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object unmarshal(InputStream is) throws JAXBException {
/* 178 */     if (is == null) {
/* 179 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "is"));
/*     */     }
/*     */ 
/*     */     
/* 183 */     InputSource isrc = new InputSource(is);
/* 184 */     return unmarshal(isrc);
/*     */   }
/*     */   
/*     */   public final Object unmarshal(Reader reader) throws JAXBException {
/* 188 */     if (reader == null) {
/* 189 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "reader"));
/*     */     }
/*     */ 
/*     */     
/* 193 */     InputSource isrc = new InputSource(reader);
/* 194 */     return unmarshal(isrc);
/*     */   }
/*     */ 
/*     */   
/*     */   private static InputSource streamSourceToInputSource(StreamSource ss) {
/* 199 */     InputSource is = new InputSource();
/* 200 */     is.setSystemId(ss.getSystemId());
/* 201 */     is.setByteStream(ss.getInputStream());
/* 202 */     is.setCharacterStream(ss.getReader());
/*     */     
/* 204 */     return is;
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
/*     */   public boolean isValidating() throws JAXBException {
/* 221 */     return this.validating;
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
/*     */   public void setEventHandler(ValidationEventHandler handler) throws JAXBException {
/* 241 */     if (handler == null) {
/* 242 */       this.eventHandler = new DefaultValidationEventHandler();
/*     */     } else {
/* 244 */       this.eventHandler = handler;
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
/*     */   public void setValidating(boolean validating) throws JAXBException {
/* 262 */     this.validating = validating;
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
/*     */   public ValidationEventHandler getEventHandler() throws JAXBException {
/* 275 */     return this.eventHandler;
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
/*     */ 
/*     */   
/*     */   protected UnmarshalException createUnmarshalException(SAXException e) {
/* 300 */     Exception nested = e.getException();
/* 301 */     if (nested instanceof UnmarshalException) {
/* 302 */       return (UnmarshalException)nested;
/*     */     }
/* 304 */     if (nested instanceof RuntimeException)
/*     */     {
/*     */ 
/*     */       
/* 308 */       throw (RuntimeException)nested;
/*     */     }
/*     */ 
/*     */     
/* 312 */     if (nested != null) {
/* 313 */       return new UnmarshalException(nested);
/*     */     }
/* 315 */     return new UnmarshalException(e);
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
/*     */   public void setProperty(String name, Object value) throws PropertyException {
/* 327 */     if (name == null) {
/* 328 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "name"));
/*     */     }
/*     */ 
/*     */     
/* 332 */     throw new PropertyException(name, value);
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
/* 344 */     if (name == null) {
/* 345 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "name"));
/*     */     }
/*     */ 
/*     */     
/* 349 */     throw new PropertyException(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object unmarshal(XMLEventReader reader) throws JAXBException {
/* 354 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object unmarshal(XMLStreamReader reader) throws JAXBException {
/* 359 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public <T> JAXBElement<T> unmarshal(Node node, Class<T> expectedType) throws JAXBException {
/* 363 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public <T> JAXBElement<T> unmarshal(Source source, Class<T> expectedType) throws JAXBException {
/* 367 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public <T> JAXBElement<T> unmarshal(XMLStreamReader reader, Class<T> expectedType) throws JAXBException {
/* 371 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public <T> JAXBElement<T> unmarshal(XMLEventReader reader, Class<T> expectedType) throws JAXBException {
/* 375 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void setSchema(Schema schema) {
/* 379 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Schema getSchema() {
/* 383 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void setAdapter(XmlAdapter adapter) {
/* 387 */     if (adapter == null)
/* 388 */       throw new IllegalArgumentException(); 
/* 389 */     setAdapter(adapter.getClass(), adapter);
/*     */   }
/*     */   
/*     */   public <A extends XmlAdapter> void setAdapter(Class<A> type, A adapter) {
/* 393 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public <A extends XmlAdapter> A getAdapter(Class<A> type) {
/* 397 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void setAttachmentUnmarshaller(AttachmentUnmarshaller au) {
/* 401 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public AttachmentUnmarshaller getAttachmentUnmarshaller() {
/* 405 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void setListener(Unmarshaller.Listener listener) {
/* 409 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Unmarshaller.Listener getListener() {
/* 413 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\helpers\AbstractUnmarshallerImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */