/*     */ package 1.0.com.sun.xml.xsom.parser;
/*     */ 
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.ParserContext;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCHandler;
/*     */ import com.sun.xml.xsom.impl.parser.state.Schema;
/*     */ import com.sun.xml.xsom.parser.AnnotationParserFactory;
/*     */ import com.sun.xml.xsom.parser.JAXPParser;
/*     */ import com.sun.xml.xsom.parser.XMLParser;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
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
/*     */ public final class XSOMParser
/*     */ {
/*     */   private EntityResolver entityResolver;
/*     */   private ErrorHandler userErrorHandler;
/*     */   private AnnotationParserFactory apFactory;
/*     */   private final ParserContext context;
/*     */   
/*     */   public XSOMParser() {
/*  50 */     this((XMLParser)new JAXPParser());
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
/*  62 */     this((XMLParser)new JAXPParser(factory));
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
/*  76 */     this.context = new ParserContext(this, parser);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(InputStream is) throws SAXException {
/*  83 */     parse(new InputSource(is));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(File schema) throws SAXException, IOException {
/*  90 */     parse(schema.toURL());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(URL url) throws SAXException {
/*  97 */     parse(url.toExternalForm());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(String systemId) throws SAXException {
/* 104 */     parse(new InputSource(systemId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(InputSource source) throws SAXException {
/* 111 */     this.context.parse(source);
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
/* 135 */     NGCCRuntimeEx runtime = this.context.newNGCCRuntime();
/* 136 */     Schema s = new Schema(runtime, false, null);
/* 137 */     runtime.setRootHandler((NGCCHandler)s);
/* 138 */     return (ContentHandler)runtime;
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
/* 154 */     return this.context.getResult();
/*     */   }
/*     */   
/*     */   public EntityResolver getEntityResolver() {
/* 158 */     return this.entityResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityResolver(EntityResolver resolver) {
/* 165 */     this.entityResolver = resolver;
/*     */   }
/*     */   public ErrorHandler getErrorHandler() {
/* 168 */     return this.userErrorHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorHandler(ErrorHandler errorHandler) {
/* 175 */     this.userErrorHandler = errorHandler;
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
/*     */   public void setAnnotationParser(Class annParser) {
/* 189 */     setAnnotationParser((AnnotationParserFactory)new Object(this, annParser));
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
/*     */   public void setAnnotationParser(AnnotationParserFactory factory) {
/* 209 */     this.apFactory = factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationParserFactory getAnnotationParserFactory() {
/* 216 */     return this.apFactory;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\parser\XSOMParser.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */