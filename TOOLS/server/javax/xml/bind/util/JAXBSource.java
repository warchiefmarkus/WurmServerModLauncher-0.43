/*     */ package javax.xml.bind.util;
/*     */ 
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.DTDHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.ext.LexicalHandler;
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
/*     */ 
/*     */ 
/*     */ public class JAXBSource
/*     */   extends SAXSource
/*     */ {
/*     */   private final Marshaller marshaller;
/*     */   private final Object contentObject;
/*     */   
/*     */   public JAXBSource(JAXBContext context, Object contentObject) throws JAXBException {
/*  88 */     this((context == null) ? assertionFailed(Messages.format("JAXBSource.NullContext")) : context.createMarshaller(), (contentObject == null) ? assertionFailed(Messages.format("JAXBSource.NullContent")) : contentObject);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public JAXBSource(Marshaller marshaller, Object contentObject) throws JAXBException {
/* 116 */     if (marshaller == null) {
/* 117 */       throw new JAXBException(Messages.format("JAXBSource.NullMarshaller"));
/*     */     }
/*     */     
/* 120 */     if (contentObject == null) {
/* 121 */       throw new JAXBException(Messages.format("JAXBSource.NullContent"));
/*     */     }
/*     */     
/* 124 */     this.marshaller = marshaller;
/* 125 */     this.contentObject = contentObject;
/*     */     
/* 127 */     setXMLReader(this.pseudoParser);
/*     */     
/* 129 */     setInputSource(new InputSource());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   private final XMLReader pseudoParser = new XMLReader() { private LexicalHandler lexicalHandler;
/*     */       public boolean getFeature(String name) throws SAXNotRecognizedException {
/* 140 */         if (name.equals("http://xml.org/sax/features/namespaces"))
/* 141 */           return true; 
/* 142 */         if (name.equals("http://xml.org/sax/features/namespace-prefixes"))
/* 143 */           return false; 
/* 144 */         throw new SAXNotRecognizedException(name);
/*     */       }
/*     */       private EntityResolver entityResolver; private DTDHandler dtdHandler;
/*     */       public void setFeature(String name, boolean value) throws SAXNotRecognizedException {
/* 148 */         if (name.equals("http://xml.org/sax/features/namespaces") && value)
/*     */           return; 
/* 150 */         if (name.equals("http://xml.org/sax/features/namespace-prefixes") && !value)
/*     */           return; 
/* 152 */         throw new SAXNotRecognizedException(name);
/*     */       }
/*     */       
/*     */       public Object getProperty(String name) throws SAXNotRecognizedException {
/* 156 */         if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
/* 157 */           return this.lexicalHandler;
/*     */         }
/* 159 */         throw new SAXNotRecognizedException(name);
/*     */       }
/*     */       
/*     */       public void setProperty(String name, Object value) throws SAXNotRecognizedException {
/* 163 */         if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
/* 164 */           this.lexicalHandler = (LexicalHandler)value;
/*     */           return;
/*     */         } 
/* 167 */         throw new SAXNotRecognizedException(name);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void setEntityResolver(EntityResolver resolver) {
/* 175 */         this.entityResolver = resolver;
/*     */       }
/*     */       public EntityResolver getEntityResolver() {
/* 178 */         return this.entityResolver;
/*     */       }
/*     */ 
/*     */       
/*     */       public void setDTDHandler(DTDHandler handler) {
/* 183 */         this.dtdHandler = handler;
/*     */       }
/*     */       public DTDHandler getDTDHandler() {
/* 186 */         return this.dtdHandler;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 192 */       private XMLFilterImpl repeater = new XMLFilterImpl(); private ErrorHandler errorHandler;
/*     */       
/*     */       public void setContentHandler(ContentHandler handler) {
/* 195 */         this.repeater.setContentHandler(handler);
/*     */       }
/*     */       public ContentHandler getContentHandler() {
/* 198 */         return this.repeater.getContentHandler();
/*     */       }
/*     */ 
/*     */       
/*     */       public void setErrorHandler(ErrorHandler handler) {
/* 203 */         this.errorHandler = handler;
/*     */       }
/*     */       public ErrorHandler getErrorHandler() {
/* 206 */         return this.errorHandler;
/*     */       }
/*     */       
/*     */       public void parse(InputSource input) throws SAXException {
/* 210 */         parse();
/*     */       }
/*     */       
/*     */       public void parse(String systemId) throws SAXException {
/* 214 */         parse();
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void parse() throws SAXException {
/*     */         try {
/* 222 */           JAXBSource.this.marshaller.marshal(JAXBSource.this.contentObject, this.repeater);
/* 223 */         } catch (JAXBException e) {
/*     */           
/* 225 */           SAXParseException se = new SAXParseException(e.getMessage(), null, null, -1, -1, (Exception)e);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 231 */           if (this.errorHandler != null) {
/* 232 */             this.errorHandler.fatalError(se);
/*     */           }
/*     */ 
/*     */           
/* 236 */           throw se;
/*     */         } 
/*     */       } }
/*     */   ;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Marshaller assertionFailed(String message) throws JAXBException {
/* 248 */     throw new JAXBException(message);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bin\\util\JAXBSource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */