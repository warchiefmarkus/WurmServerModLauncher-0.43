/*     */ package com.sun.tools.xjc;
/*     */ 
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.tools.xjc.model.Model;
/*     */ import com.sun.tools.xjc.reader.ExtensionBindingChecker;
/*     */ import com.sun.tools.xjc.reader.dtd.TDTDReader;
/*     */ import com.sun.tools.xjc.reader.internalizer.DOMForest;
/*     */ import com.sun.tools.xjc.reader.internalizer.DOMForestScanner;
/*     */ import com.sun.tools.xjc.reader.internalizer.InternalizationLogic;
/*     */ import com.sun.tools.xjc.reader.internalizer.SCDBasedBindingSet;
/*     */ import com.sun.tools.xjc.reader.internalizer.VersionChecker;
/*     */ import com.sun.tools.xjc.reader.relaxng.RELAXNGCompiler;
/*     */ import com.sun.tools.xjc.reader.relaxng.RELAXNGInternalizationLogic;
/*     */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.AnnotationParserFactoryImpl;
/*     */ import com.sun.tools.xjc.reader.xmlschema.parser.CustomizationContextChecker;
/*     */ import com.sun.tools.xjc.reader.xmlschema.parser.IncorrectNamespaceURIChecker;
/*     */ import com.sun.tools.xjc.reader.xmlschema.parser.SchemaConstraintChecker;
/*     */ import com.sun.tools.xjc.reader.xmlschema.parser.XMLSchemaInternalizationLogic;
/*     */ import com.sun.tools.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import com.sun.xml.xsom.parser.AnnotationParserFactory;
/*     */ import com.sun.xml.xsom.parser.JAXPParser;
/*     */ import com.sun.xml.xsom.parser.XMLParser;
/*     */ import com.sun.xml.xsom.parser.XSOMParser;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import org.kohsuke.rngom.ast.builder.SchemaBuilder;
/*     */ import org.kohsuke.rngom.ast.util.CheckingSchemaBuilder;
/*     */ import org.kohsuke.rngom.digested.DPattern;
/*     */ import org.kohsuke.rngom.digested.DSchemaBuilderImpl;
/*     */ import org.kohsuke.rngom.parse.IllegalSchemaException;
/*     */ import org.kohsuke.rngom.parse.Parseable;
/*     */ import org.kohsuke.rngom.parse.compact.CompactParseable;
/*     */ import org.kohsuke.rngom.parse.xml.SAXParseable;
/*     */ import org.kohsuke.rngom.xml.sax.XMLReaderCreator;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLFilter;
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
/*     */ public final class ModelLoader
/*     */ {
/*     */   private final Options opt;
/*     */   private final ErrorReceiverFilter errorReceiver;
/*     */   private final JCodeModel codeModel;
/*     */   private SCDBasedBindingSet scdBasedBindingSet;
/*     */   
/*     */   public static Model load(Options opt, JCodeModel codeModel, ErrorReceiver er) {
/* 113 */     return (new ModelLoader(opt, codeModel, er)).load();
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelLoader(Options _opt, JCodeModel _codeModel, ErrorReceiver er) {
/* 118 */     this.opt = _opt;
/* 119 */     this.codeModel = _codeModel;
/* 120 */     this.errorReceiver = new ErrorReceiverFilter(er);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Model load() {
/* 126 */     if (!sanityCheck())
/* 127 */       return null; 
/*     */     try {
/*     */       Model grammar;
/*     */       InputSource bindFile;
/* 131 */       switch (this.opt.getSchemaLanguage()) {
/*     */         
/*     */         case DTD:
/* 134 */           bindFile = null;
/* 135 */           if ((this.opt.getBindFiles()).length > 0) {
/* 136 */             bindFile = this.opt.getBindFiles()[0];
/*     */           }
/* 138 */           if (bindFile == null)
/*     */           {
/* 140 */             bindFile = new InputSource(new StringReader("<?xml version='1.0'?><xml-java-binding-schema><options package='" + ((this.opt.defaultPackage == null) ? "generated" : this.opt.defaultPackage) + "'/></xml-java-binding-schema>"));
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 148 */           checkTooManySchemaErrors();
/* 149 */           grammar = loadDTD(this.opt.getGrammars()[0], bindFile);
/*     */           break;
/*     */         
/*     */         case RELAXNG:
/* 153 */           checkTooManySchemaErrors();
/* 154 */           grammar = loadRELAXNG();
/*     */           break;
/*     */         
/*     */         case RELAXNG_COMPACT:
/* 158 */           checkTooManySchemaErrors();
/* 159 */           grammar = loadRELAXNGCompact();
/*     */           break;
/*     */         
/*     */         case WSDL:
/* 163 */           grammar = annotateXMLSchema(loadWSDL());
/*     */           break;
/*     */         
/*     */         case XMLSCHEMA:
/* 167 */           grammar = annotateXMLSchema(loadXMLSchema());
/*     */           break;
/*     */         
/*     */         default:
/* 171 */           throw new AssertionError();
/*     */       } 
/*     */       
/* 174 */       if (this.errorReceiver.hadError()) {
/* 175 */         grammar = null;
/*     */       } else {
/* 177 */         grammar.setPackageLevelAnnotations(this.opt.packageLevelAnnotations);
/*     */       } 
/*     */       
/* 180 */       return grammar;
/*     */     }
/* 182 */     catch (SAXException e) {
/*     */ 
/*     */ 
/*     */       
/* 186 */       if (this.opt.verbose)
/*     */       {
/*     */ 
/*     */         
/* 190 */         if (e.getException() != null) {
/* 191 */           e.getException().printStackTrace();
/*     */         } else {
/* 193 */           e.printStackTrace();
/*     */         }  } 
/* 195 */       return null;
/* 196 */     } catch (AbortException e) {
/*     */       
/* 198 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean sanityCheck() {
/* 209 */     if (this.opt.getSchemaLanguage() == Language.XMLSCHEMA) {
/* 210 */       Language guess = this.opt.guessSchemaLanguage();
/*     */       
/* 212 */       String[] msg = null;
/* 213 */       switch (guess) {
/*     */         case DTD:
/* 215 */           msg = new String[] { "DTD", "-dtd" };
/*     */           break;
/*     */         case RELAXNG:
/* 218 */           msg = new String[] { "RELAX NG", "-relaxng" };
/*     */           break;
/*     */         case RELAXNG_COMPACT:
/* 221 */           msg = new String[] { "RELAX NG compact syntax", "-relaxng-compact" };
/*     */           break;
/*     */         case WSDL:
/* 224 */           msg = new String[] { "WSDL", "-wsdl" };
/*     */           break;
/*     */       } 
/* 227 */       if (msg != null) {
/* 228 */         this.errorReceiver.warning(null, Messages.format("Driver.ExperimentalLanguageWarning", new Object[] { msg[0], msg[1] }));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 233 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class XMLSchemaParser
/*     */     implements XMLParser
/*     */   {
/*     */     private final XMLParser baseParser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private XMLSchemaParser(XMLParser baseParser) {
/* 251 */       this.baseParser = baseParser;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void parse(InputSource source, ContentHandler handler, ErrorHandler errorHandler, EntityResolver entityResolver) throws SAXException, IOException {
/* 257 */       handler = wrapBy((XMLFilterImpl)new ExtensionBindingChecker("http://www.w3.org/2001/XMLSchema", ModelLoader.this.opt, (ErrorHandler)ModelLoader.this.errorReceiver), handler);
/* 258 */       handler = wrapBy((XMLFilterImpl)new IncorrectNamespaceURIChecker((ErrorHandler)ModelLoader.this.errorReceiver), handler);
/* 259 */       handler = wrapBy((XMLFilterImpl)new CustomizationContextChecker((ErrorHandler)ModelLoader.this.errorReceiver), handler);
/*     */ 
/*     */       
/* 262 */       this.baseParser.parse(source, handler, errorHandler, entityResolver);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private ContentHandler wrapBy(XMLFilterImpl filter, ContentHandler handler) {
/* 270 */       filter.setContentHandler(handler);
/* 271 */       return filter;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkTooManySchemaErrors() {
/* 280 */     if ((this.opt.getGrammars()).length != 1) {
/* 281 */       this.errorReceiver.error(null, Messages.format("ModelLoader.TooManySchema", new Object[0]));
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
/*     */   private Model loadDTD(InputSource source, InputSource bindFile) {
/* 295 */     return TDTDReader.parse(source, bindFile, (ErrorReceiver)this.errorReceiver, this.opt);
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
/*     */   public DOMForest buildDOMForest(InternalizationLogic logic) throws SAXException {
/* 312 */     DOMForest forest = new DOMForest(logic);
/*     */     
/* 314 */     forest.setErrorHandler((ErrorReceiver)this.errorReceiver);
/* 315 */     if (this.opt.entityResolver != null) {
/* 316 */       forest.setEntityResolver(this.opt.entityResolver);
/*     */     }
/*     */     
/* 319 */     for (InputSource value : this.opt.getGrammars()) {
/* 320 */       this.errorReceiver.pollAbort();
/* 321 */       forest.parse(value, true);
/*     */     } 
/*     */ 
/*     */     
/* 325 */     for (InputSource value : this.opt.getBindFiles()) {
/* 326 */       this.errorReceiver.pollAbort();
/* 327 */       Document dom = forest.parse(value, true);
/* 328 */       if (dom != null) {
/* 329 */         Element root = dom.getDocumentElement();
/*     */ 
/*     */         
/* 332 */         if (!fixNull(root.getNamespaceURI()).equals("http://java.sun.com/xml/ns/jaxb") || !root.getLocalName().equals("bindings"))
/*     */         {
/* 334 */           this.errorReceiver.error(new SAXParseException(Messages.format("Driver.NotABindingFile", new Object[] { root.getNamespaceURI(), root.getLocalName() }), null, value.getSystemId(), -1, -1));
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 342 */     this.scdBasedBindingSet = forest.transform(this.opt.isExtensionMode());
/*     */     
/* 344 */     return forest;
/*     */   }
/*     */   
/*     */   private String fixNull(String s) {
/* 348 */     if (s == null) return ""; 
/* 349 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XSSchemaSet loadXMLSchema() throws SAXException {
/* 357 */     if (this.opt.strictCheck && !SchemaConstraintChecker.check(this.opt.getGrammars(), (ErrorReceiver)this.errorReceiver, this.opt.entityResolver))
/*     */     {
/* 359 */       return null;
/*     */     }
/*     */     
/* 362 */     if ((this.opt.getBindFiles()).length == 0) {
/*     */       
/*     */       try {
/*     */         
/* 366 */         return createXSOMSpeculative();
/* 367 */       } catch (SpeculationFailure _) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 374 */     DOMForest forest = buildDOMForest((InternalizationLogic)new XMLSchemaInternalizationLogic());
/* 375 */     return createXSOM(forest, this.scdBasedBindingSet);
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
/*     */   private XSSchemaSet loadWSDL() throws SAXException {
/* 388 */     DOMForest forest = buildDOMForest((InternalizationLogic)new XMLSchemaInternalizationLogic());
/*     */     
/* 390 */     DOMForestScanner scanner = new DOMForestScanner(forest);
/*     */     
/* 392 */     XSOMParser xsomParser = createXSOMParser(forest);
/*     */ 
/*     */     
/* 395 */     for (InputSource grammar : this.opt.getGrammars()) {
/* 396 */       Document wsdlDom = forest.get(grammar.getSystemId());
/*     */       
/* 398 */       NodeList schemas = wsdlDom.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "schema");
/* 399 */       for (int i = 0; i < schemas.getLength(); i++)
/* 400 */         scanner.scan((Element)schemas.item(i), xsomParser.getParserHandler()); 
/*     */     } 
/* 402 */     return xsomParser.getResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model annotateXMLSchema(XSSchemaSet xs) {
/* 413 */     if (xs == null)
/* 414 */       return null; 
/* 415 */     return BGMBuilder.build(xs, this.codeModel, (ErrorReceiver)this.errorReceiver, this.opt);
/*     */   }
/*     */ 
/*     */   
/*     */   public XSOMParser createXSOMParser(XMLParser parser) {
/* 420 */     XSOMParser reader = new XSOMParser(new XMLSchemaParser(parser));
/* 421 */     reader.setAnnotationParser((AnnotationParserFactory)new AnnotationParserFactoryImpl(this.opt));
/* 422 */     reader.setErrorHandler((ErrorHandler)this.errorReceiver);
/* 423 */     reader.setEntityResolver(this.opt.entityResolver);
/* 424 */     return reader;
/*     */   }
/*     */   
/*     */   public XSOMParser createXSOMParser(final DOMForest forest) {
/* 428 */     XSOMParser p = createXSOMParser(forest.createParser());
/* 429 */     p.setEntityResolver(new EntityResolver()
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
/*     */           {
/* 437 */             if (systemId != null && forest.get(systemId) != null)
/* 438 */               return new InputSource(systemId); 
/* 439 */             if (ModelLoader.this.opt.entityResolver != null) {
/* 440 */               return ModelLoader.this.opt.entityResolver.resolveEntity(publicId, systemId);
/*     */             }
/* 442 */             return null;
/*     */           }
/*     */         });
/* 445 */     return p;
/*     */   }
/*     */   
/*     */   private static final class SpeculationFailure extends Error {
/*     */     private SpeculationFailure() {} }
/*     */   
/*     */   private static final class SpeculationChecker extends XMLFilterImpl {
/*     */     public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
/* 453 */       if (localName.equals("bindings") && uri.equals("http://java.sun.com/xml/ns/jaxb"))
/* 454 */         throw new ModelLoader.SpeculationFailure(); 
/* 455 */       super.startElement(uri, localName, qName, attributes);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private SpeculationChecker() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private XSSchemaSet createXSOMSpeculative() throws SAXException, SpeculationFailure {
/* 470 */     XMLParser parser = new XMLParser() {
/* 471 */         private final JAXPParser base = new JAXPParser();
/*     */ 
/*     */ 
/*     */         
/*     */         public void parse(InputSource source, ContentHandler handler, ErrorHandler errorHandler, EntityResolver entityResolver) throws SAXException, IOException {
/* 476 */           handler = wrapBy(new ModelLoader.SpeculationChecker(), handler);
/* 477 */           handler = wrapBy((XMLFilterImpl)new VersionChecker(null, (ErrorHandler)ModelLoader.this.errorReceiver, entityResolver), handler);
/*     */           
/* 479 */           this.base.parse(source, handler, errorHandler, entityResolver);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         private ContentHandler wrapBy(XMLFilterImpl filter, ContentHandler handler) {
/* 487 */           filter.setContentHandler(handler);
/* 488 */           return filter;
/*     */         }
/*     */       };
/*     */     
/* 492 */     XSOMParser reader = createXSOMParser(parser);
/*     */ 
/*     */     
/* 495 */     for (InputSource value : this.opt.getGrammars()) {
/* 496 */       reader.parse(value);
/*     */     }
/* 498 */     return reader.getResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XSSchemaSet createXSOM(DOMForest forest, SCDBasedBindingSet scdBasedBindingSet) throws SAXException {
/* 509 */     XSOMParser reader = createXSOMParser(forest);
/*     */ 
/*     */     
/* 512 */     for (String systemId : forest.getRootDocuments()) {
/* 513 */       this.errorReceiver.pollAbort();
/* 514 */       Document dom = forest.get(systemId);
/* 515 */       if (!dom.getDocumentElement().getNamespaceURI().equals("http://java.sun.com/xml/ns/jaxb")) {
/* 516 */         reader.parse(systemId);
/*     */       }
/*     */     } 
/* 519 */     XSSchemaSet result = reader.getResult();
/*     */     
/* 521 */     if (result != null) {
/* 522 */       scdBasedBindingSet.apply(result, (ErrorReceiver)this.errorReceiver);
/*     */     }
/* 524 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Model loadRELAXNG() throws SAXException {
/* 533 */     final DOMForest forest = buildDOMForest((InternalizationLogic)new RELAXNGInternalizationLogic());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 538 */     XMLReaderCreator xrc = new XMLReaderCreator()
/*     */       {
/*     */         
/*     */         public XMLReader createXMLReader()
/*     */         {
/* 543 */           XMLFilter buffer = new XMLFilterImpl() {
/*     */               public void parse(InputSource source) throws IOException, SAXException {
/* 545 */                 forest.createParser().parse(source, this, this, this);
/*     */               }
/*     */             };
/*     */           
/* 549 */           ExtensionBindingChecker extensionBindingChecker = new ExtensionBindingChecker("http://relaxng.org/ns/structure/1.0", ModelLoader.this.opt, (ErrorHandler)ModelLoader.this.errorReceiver);
/* 550 */           extensionBindingChecker.setParent(buffer);
/*     */           
/* 552 */           extensionBindingChecker.setEntityResolver(ModelLoader.this.opt.entityResolver);
/*     */           
/* 554 */           return (XMLReader)extensionBindingChecker;
/*     */         }
/*     */       };
/*     */     
/* 558 */     SAXParseable sAXParseable = new SAXParseable(this.opt.getGrammars()[0], (ErrorHandler)this.errorReceiver, xrc);
/*     */     
/* 560 */     return loadRELAXNG((Parseable)sAXParseable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Model loadRELAXNGCompact() {
/* 568 */     if ((this.opt.getBindFiles()).length > 0) {
/* 569 */       this.errorReceiver.error(new SAXParseException(Messages.format("ModelLoader.BindingFileNotSupportedForRNC", new Object[0]), null));
/*     */     }
/*     */ 
/*     */     
/* 573 */     CompactParseable compactParseable = new CompactParseable(this.opt.getGrammars()[0], (ErrorHandler)this.errorReceiver);
/*     */     
/* 575 */     return loadRELAXNG((Parseable)compactParseable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Model loadRELAXNG(Parseable p) {
/* 583 */     CheckingSchemaBuilder checkingSchemaBuilder = new CheckingSchemaBuilder((SchemaBuilder)new DSchemaBuilderImpl(), (ErrorHandler)this.errorReceiver);
/*     */     
/*     */     try {
/* 586 */       DPattern out = (DPattern)p.parse((SchemaBuilder)checkingSchemaBuilder);
/* 587 */       return RELAXNGCompiler.build(out, this.codeModel, this.opt);
/* 588 */     } catch (IllegalSchemaException e) {
/* 589 */       this.errorReceiver.error(e.getMessage(), (Exception)e);
/* 590 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\ModelLoader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */