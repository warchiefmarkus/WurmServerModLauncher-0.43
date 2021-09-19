/*     */ package com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.reader.xmlschema.Messages;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import com.sun.xml.xsom.parser.AnnotationParser;
/*     */ import com.sun.xml.xsom.parser.AnnotationParserFactory;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.UnmarshallerHandler;
/*     */ import javax.xml.bind.ValidationEventHandler;
/*     */ import javax.xml.bind.helpers.DefaultValidationEventHandler;
/*     */ import javax.xml.validation.ValidatorHandler;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
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
/*     */ public class AnnotationParserFactoryImpl
/*     */   implements AnnotationParserFactory
/*     */ {
/*     */   private final Options options;
/*     */   private ValidatorHandler validator;
/*     */   
/*     */   public AnnotationParserFactoryImpl(Options opts) {
/*  68 */     this.options = opts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationParser create() {
/*  79 */     return new AnnotationParser() {
/*  80 */         private Unmarshaller u = (Unmarshaller)BindInfo.getJAXBContext().createUnmarshaller();
/*     */ 
/*     */ 
/*     */         
/*     */         private UnmarshallerHandler handler;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public ContentHandler getContentHandler(AnnotationContext context, String parentElementName, final ErrorHandler errorHandler, EntityResolver entityResolver) {
/*  90 */           if (this.handler != null)
/*     */           {
/*     */             
/*  93 */             throw new AssertionError();
/*     */           }
/*  95 */           if (AnnotationParserFactoryImpl.this.options.debugMode) {
/*     */             try {
/*  97 */               this.u.setEventHandler((ValidationEventHandler)new DefaultValidationEventHandler());
/*  98 */             } catch (JAXBException e) {
/*  99 */               throw new AssertionError(e);
/*     */             } 
/*     */           }
/* 102 */           this.handler = this.u.getUnmarshallerHandler();
/*     */ 
/*     */           
/* 105 */           return new ForkingFilter((ContentHandler)this.handler)
/*     */             {
/*     */               public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
/* 108 */                 super.startElement(uri, localName, qName, atts);
/* 109 */                 if ((uri.equals("http://java.sun.com/xml/ns/jaxb") || uri.equals("http://java.sun.com/xml/ns/jaxb/xjc")) && getSideHandler() == null) {
/*     */ 
/*     */                   
/* 112 */                   if (AnnotationParserFactoryImpl.this.validator == null)
/* 113 */                     AnnotationParserFactoryImpl.this.validator = BindInfo.bindingFileSchema.newValidator(); 
/* 114 */                   AnnotationParserFactoryImpl.this.validator.setErrorHandler(errorHandler);
/* 115 */                   startForking(uri, localName, qName, atts, new AnnotationParserFactoryImpl.ValidatorProtecter(AnnotationParserFactoryImpl.this.validator));
/*     */                 } 
/*     */ 
/*     */                 
/* 119 */                 for (int i = atts.getLength() - 1; i >= 0; i--) {
/* 120 */                   if (atts.getURI(i).equals("http://www.w3.org/2005/05/xmlmime") && atts.getLocalName(i).equals("expectedContentTypes"))
/*     */                   {
/* 122 */                     errorHandler.warning(new SAXParseException(Messages.format("UnusedCustomizationChecker.WarnUnusedExpectedContentTypes", new Object[0]), getDocumentLocator()));
/*     */                   }
/*     */                 } 
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public BindInfo getResult(Object existing) {
/* 133 */           if (this.handler == null)
/*     */           {
/*     */             
/* 136 */             throw new AssertionError();
/*     */           }
/*     */           try {
/* 139 */             BindInfo result = (BindInfo)this.handler.getResult();
/*     */             
/* 141 */             if (existing != null) {
/* 142 */               BindInfo bie = (BindInfo)existing;
/* 143 */               bie.absorb(result);
/* 144 */               return bie;
/*     */             } 
/* 146 */             if (!result.isPointless()) {
/* 147 */               return result;
/*     */             }
/* 149 */             return null;
/*     */           }
/* 151 */           catch (JAXBException e) {
/* 152 */             throw new AssertionError(e);
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static final class ValidatorProtecter extends XMLFilterImpl {
/*     */     public ValidatorProtecter(ContentHandler h) {
/* 160 */       setContentHandler(h);
/*     */     }
/*     */ 
/*     */     
/*     */     public void startPrefixMapping(String prefix, String uri) throws SAXException {
/* 165 */       super.startPrefixMapping(prefix.intern(), uri);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\bindinfo\AnnotationParserFactoryImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */