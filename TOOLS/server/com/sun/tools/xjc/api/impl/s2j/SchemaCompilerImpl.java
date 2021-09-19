/*     */ package com.sun.tools.xjc.api.impl.s2j;
/*     */ 
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.istack.NotNull;
/*     */ import com.sun.istack.SAXParseException2;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.ModelLoader;
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.api.ClassNameAllocator;
/*     */ import com.sun.tools.xjc.api.ErrorListener;
/*     */ import com.sun.tools.xjc.api.S2JJAXBModel;
/*     */ import com.sun.tools.xjc.api.SchemaCompiler;
/*     */ import com.sun.tools.xjc.api.SpecVersion;
/*     */ import com.sun.tools.xjc.model.Model;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import com.sun.tools.xjc.reader.internalizer.DOMForest;
/*     */ import com.sun.tools.xjc.reader.internalizer.InternalizationLogic;
/*     */ import com.sun.tools.xjc.reader.internalizer.SCDBasedBindingSet;
/*     */ import com.sun.tools.xjc.reader.xmlschema.parser.XMLSchemaInternalizationLogic;
/*     */ import com.sun.xml.bind.unmarshaller.DOMScanner;
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.validation.SchemaFactory;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SchemaCompilerImpl
/*     */   extends ErrorReceiver
/*     */   implements SchemaCompiler
/*     */ {
/*     */   private ErrorListener errorListener;
/*  93 */   protected final Options opts = new Options();
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   protected DOMForest forest;
/*     */   
/*     */   private boolean hadError;
/*     */ 
/*     */   
/*     */   public SchemaCompilerImpl() {
/* 103 */     this.opts.compatibilityMode = 2;
/* 104 */     resetSchema();
/*     */     
/* 106 */     if (System.getProperty("xjc-api.test") != null) {
/* 107 */       this.opts.debugMode = true;
/* 108 */       this.opts.verbose = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public Options getOptions() {
/* 114 */     return this.opts;
/*     */   }
/*     */   
/*     */   public ContentHandler getParserHandler(String systemId) {
/* 118 */     return (ContentHandler)this.forest.getParserHandler(systemId, true);
/*     */   }
/*     */   
/*     */   public void parseSchema(String systemId, Element element) {
/* 122 */     checkAbsoluteness(systemId);
/*     */     try {
/* 124 */       DOMScanner scanner = new DOMScanner();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 130 */       LocatorImpl loc = new LocatorImpl();
/* 131 */       loc.setSystemId(systemId);
/* 132 */       scanner.setLocator(loc);
/*     */       
/* 134 */       scanner.setContentHandler(getParserHandler(systemId));
/* 135 */       scanner.scan(element);
/* 136 */     } catch (SAXException e) {
/*     */ 
/*     */ 
/*     */       
/* 140 */       fatalError((SAXParseException)new SAXParseException2(e.getMessage(), null, systemId, -1, -1, e));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void parseSchema(InputSource source) {
/* 146 */     checkAbsoluteness(source.getSystemId());
/*     */     try {
/* 148 */       this.forest.parse(source, true);
/* 149 */     } catch (SAXException e) {
/*     */ 
/*     */       
/* 152 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setTargetVersion(SpecVersion version) {
/* 157 */     if (version == null)
/* 158 */       version = SpecVersion.LATEST; 
/* 159 */     this.opts.target = version;
/*     */   }
/*     */   
/*     */   public void parseSchema(String systemId, XMLStreamReader reader) throws XMLStreamException {
/* 163 */     checkAbsoluteness(systemId);
/* 164 */     this.forest.parse(systemId, reader, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkAbsoluteness(String systemId) {
/*     */     try {
/* 176 */       new URL(systemId);
/* 177 */     } catch (MalformedURLException _) {
/*     */       try {
/* 179 */         new URI(systemId);
/* 180 */       } catch (URISyntaxException e) {
/* 181 */         throw new IllegalArgumentException("system ID '" + systemId + "' isn't absolute", e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setEntityResolver(EntityResolver entityResolver) {
/* 187 */     this.forest.setEntityResolver(entityResolver);
/* 188 */     this.opts.entityResolver = entityResolver;
/*     */   }
/*     */   
/*     */   public void setDefaultPackageName(String packageName) {
/* 192 */     this.opts.defaultPackage2 = packageName;
/*     */   }
/*     */   
/*     */   public void forcePackageName(String packageName) {
/* 196 */     this.opts.defaultPackage = packageName;
/*     */   }
/*     */   
/*     */   public void setClassNameAllocator(ClassNameAllocator allocator) {
/* 200 */     this.opts.classNameAllocator = allocator;
/*     */   }
/*     */   
/*     */   public void resetSchema() {
/* 204 */     this.forest = new DOMForest((InternalizationLogic)new XMLSchemaInternalizationLogic());
/* 205 */     this.forest.setErrorHandler(this);
/* 206 */     this.forest.setEntityResolver(this.opts.entityResolver);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAXBModelImpl bind() {
/* 216 */     SCDBasedBindingSet scdBasedBindingSet = this.forest.transform(this.opts.isExtensionMode());
/*     */     
/* 218 */     if (!NO_CORRECTNESS_CHECK) {
/*     */       
/* 220 */       SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
/* 221 */       sf.setErrorHandler(new DowngradingErrorHandler((ErrorHandler)this));
/* 222 */       this.forest.weakSchemaCorrectnessCheck(sf);
/* 223 */       if (this.hadError) {
/* 224 */         return null;
/*     */       }
/*     */     } 
/* 227 */     JCodeModel codeModel = new JCodeModel();
/*     */     
/* 229 */     ModelLoader gl = new ModelLoader(this.opts, codeModel, this);
/*     */     
/*     */     try {
/* 232 */       XSSchemaSet result = gl.createXSOM(this.forest, scdBasedBindingSet);
/* 233 */       if (result == null) {
/* 234 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 241 */       Model model = gl.annotateXMLSchema(result);
/* 242 */       if (model == null) return null;
/*     */       
/* 244 */       if (this.hadError) return null;
/*     */       
/* 246 */       Outline context = model.generateCode(this.opts, this);
/* 247 */       if (context == null) return null;
/*     */       
/* 249 */       if (this.hadError) return null;
/*     */       
/* 251 */       return new JAXBModelImpl(context);
/* 252 */     } catch (SAXException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 260 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setErrorListener(ErrorListener errorListener) {
/* 265 */     this.errorListener = errorListener;
/*     */   }
/*     */ 
/*     */   
/*     */   public void info(SAXParseException exception) {
/* 270 */     if (this.errorListener != null)
/* 271 */       this.errorListener.info(exception); 
/*     */   }
/*     */   public void warning(SAXParseException exception) {
/* 274 */     if (this.errorListener != null)
/* 275 */       this.errorListener.warning(exception); 
/*     */   }
/*     */   public void error(SAXParseException exception) {
/* 278 */     this.hadError = true;
/* 279 */     if (this.errorListener != null)
/* 280 */       this.errorListener.error(exception); 
/*     */   }
/*     */   public void fatalError(SAXParseException exception) {
/* 283 */     this.hadError = true;
/* 284 */     if (this.errorListener != null) {
/* 285 */       this.errorListener.fatalError(exception);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean NO_CORRECTNESS_CHECK = false;
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/* 297 */       NO_CORRECTNESS_CHECK = Boolean.getBoolean(SchemaCompilerImpl.class + ".noCorrectnessCheck");
/* 298 */     } catch (Throwable t) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\impl\s2j\SchemaCompilerImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */