/*     */ package com.sun.xml.xsom.parser;
/*     */ 
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.ParserContext;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCHandler;
/*     */ import com.sun.xml.xsom.impl.parser.state.Schema;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.net.URL;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
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
/*     */ 
/*     */ public final class XSOMParser
/*     */ {
/*     */   private EntityResolver entityResolver;
/*     */   private ErrorHandler userErrorHandler;
/*     */   private AnnotationParserFactory apFactory;
/*     */   private final ParserContext context;
/*     */   
/*     */   public XSOMParser() {
/*  63 */     this(new JAXPParser());
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
/*     */   public XSOMParser(SAXParserFactory factory) {
/*  75 */     this(new JAXPParser(factory));
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
/*     */   public XSOMParser(XMLParser parser) {
/*  89 */     this.context = new ParserContext(this, parser);
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
/*     */   public void parse(InputStream is) throws SAXException {
/* 102 */     parse(new InputSource(is));
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
/*     */   public void parse(Reader reader) throws SAXException {
/* 115 */     parse(new InputSource(reader));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(File schema) throws SAXException, IOException {
/* 122 */     parse(schema.toURL());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(URL url) throws SAXException {
/* 129 */     parse(url.toExternalForm());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(String systemId) throws SAXException {
/* 136 */     parse(new InputSource(systemId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(InputSource source) throws SAXException {
/* 147 */     this.context.parse(source);
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
/*     */   public ContentHandler getParserHandler() {
/* 171 */     NGCCRuntimeEx runtime = this.context.newNGCCRuntime();
/* 172 */     Schema s = new Schema(runtime, false, null);
/* 173 */     runtime.setRootHandler((NGCCHandler)s);
/* 174 */     return (ContentHandler)runtime;
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
/*     */   public XSSchemaSet getResult() throws SAXException {
/* 190 */     return this.context.getResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<SchemaDocument> getDocuments() {
/* 201 */     return new HashSet<SchemaDocument>(this.context.parsedDocuments.keySet());
/*     */   }
/*     */   
/*     */   public EntityResolver getEntityResolver() {
/* 205 */     return this.entityResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityResolver(EntityResolver resolver) {
/* 212 */     this.entityResolver = resolver;
/*     */   }
/*     */   public ErrorHandler getErrorHandler() {
/* 215 */     return this.userErrorHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorHandler(ErrorHandler errorHandler) {
/* 222 */     this.userErrorHandler = errorHandler;
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
/*     */   public void setAnnotationParser(final Class annParser) {
/* 236 */     setAnnotationParser(new AnnotationParserFactory() {
/*     */           public AnnotationParser create() {
/*     */             try {
/* 239 */               return annParser.newInstance();
/* 240 */             } catch (InstantiationException e) {
/* 241 */               throw new InstantiationError(e.getMessage());
/* 242 */             } catch (IllegalAccessException e) {
/* 243 */               throw new IllegalAccessError(e.getMessage());
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAnnotationParser(AnnotationParserFactory factory) {
/* 256 */     this.apFactory = factory;
/*     */   }
/*     */   
/*     */   public AnnotationParserFactory getAnnotationParserFactory() {
/* 260 */     return this.apFactory;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\parser\XSOMParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */