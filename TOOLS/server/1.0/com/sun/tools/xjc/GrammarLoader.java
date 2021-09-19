/*     */ package 1.0.com.sun.tools.xjc;
/*     */ 
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.org.apache.xerces.internal.impl.Version;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.Messages;
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.reader.dtd.TDTDReader;
/*     */ import com.sun.tools.xjc.reader.internalizer.DOMForest;
/*     */ import com.sun.tools.xjc.reader.internalizer.DOMForestScanner;
/*     */ import com.sun.tools.xjc.reader.internalizer.InternalizationLogic;
/*     */ import com.sun.tools.xjc.reader.relaxng.CustomizationConverter;
/*     */ import com.sun.tools.xjc.reader.relaxng.RELAXNGInternalizationLogic;
/*     */ import com.sun.tools.xjc.reader.relaxng.TRELAXNGReader;
/*     */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.AnnotationParserFactoryImpl;
/*     */ import com.sun.tools.xjc.reader.xmlschema.parser.SchemaConstraintChecker;
/*     */ import com.sun.tools.xjc.reader.xmlschema.parser.XMLSchemaInternalizationLogic;
/*     */ import com.sun.tools.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import com.sun.xml.bind.util.Which;
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import com.sun.xml.xsom.impl.parser.SAXParserFactoryAdaptor;
/*     */ import com.sun.xml.xsom.parser.AnnotationParserFactory;
/*     */ import com.sun.xml.xsom.parser.XMLParser;
/*     */ import com.sun.xml.xsom.parser.XSOMParser;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.dom4j.DocumentFactory;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GrammarLoader
/*     */ {
/*     */   private final Options opt;
/*     */   private final ErrorReceiverFilter errorReceiver;
/*     */   
/*     */   public static AnnotatedGrammar load(Options opt, ErrorReceiver er) throws SAXException, IOException {
/*  76 */     return (new com.sun.tools.xjc.GrammarLoader(opt, er)).load();
/*     */   }
/*     */ 
/*     */   
/*     */   public GrammarLoader(Options _opt, ErrorReceiver er) {
/*  81 */     this.opt = _opt;
/*  82 */     this.errorReceiver = new ErrorReceiverFilter(er);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private AnnotatedGrammar load() throws IOException {
/*  88 */     if (!sanityCheck())
/*  89 */       return null; 
/*     */     try {
/*     */       AnnotatedGrammar grammar;
/*     */       InputSource bindFile;
/*  93 */       JCodeModel codeModel = new JCodeModel();
/*     */       
/*  95 */       switch (this.opt.getSchemaLanguage()) {
/*     */         
/*     */         case 0:
/*  98 */           bindFile = null;
/*  99 */           if ((this.opt.getBindFiles()).length > 0) {
/* 100 */             bindFile = this.opt.getBindFiles()[0];
/*     */           }
/* 102 */           if (bindFile == null)
/*     */           {
/* 104 */             bindFile = new InputSource(new StringReader("<?xml version='1.0'?><xml-java-binding-schema><options package='" + ((this.opt.defaultPackage == null) ? "generated" : this.opt.defaultPackage) + "'/></xml-java-binding-schema>"));
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 112 */           checkTooManySchemaErrors();
/* 113 */           grammar = loadDTD(this.opt.getGrammars()[0], bindFile);
/*     */           break;
/*     */         
/*     */         case 2:
/* 117 */           checkTooManySchemaErrors();
/* 118 */           grammar = loadRELAXNG();
/*     */           break;
/*     */         
/*     */         case 3:
/* 122 */           checkTooManySchemaErrors();
/* 123 */           grammar = annotateXMLSchema(loadWSDL(codeModel), codeModel);
/*     */           break;
/*     */         
/*     */         case 1:
/* 127 */           grammar = annotateXMLSchema(loadXMLSchema(codeModel), codeModel);
/*     */           break;
/*     */         
/*     */         default:
/* 131 */           throw new JAXBAssertionError();
/*     */       } 
/*     */       
/* 134 */       if (this.errorReceiver.hadError())
/* 135 */         grammar = null; 
/* 136 */       return grammar;
/*     */     }
/* 138 */     catch (SAXException e) {
/*     */ 
/*     */ 
/*     */       
/* 142 */       if (this.opt.debugMode)
/*     */       {
/*     */ 
/*     */         
/* 146 */         if (e.getException() != null) {
/* 147 */           e.getException().printStackTrace();
/*     */         } else {
/* 149 */           e.printStackTrace();
/*     */         }  } 
/* 151 */       return null;
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
/* 162 */     if (this.opt.getSchemaLanguage() == 0) {
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 167 */         new DocumentFactory();
/* 168 */       } catch (NoClassDefFoundError e) {
/* 169 */         this.errorReceiver.error(null, Messages.format("Driver.MissingDOM4J"));
/* 170 */         return false;
/*     */       } 
/*     */     }
/* 173 */     if (this.opt.getSchemaLanguage() == 1) {
/* 174 */       int guess = this.opt.guessSchemaLanguage();
/*     */       
/* 176 */       String[] msg = null;
/* 177 */       switch (guess) {
/*     */         case 0:
/* 179 */           msg = new String[] { "DTD", "-dtd" };
/*     */           break;
/*     */         case 2:
/* 182 */           msg = new String[] { "RELAX NG", "-relaxng" };
/*     */           break;
/*     */       } 
/* 185 */       if (msg != null) {
/* 186 */         this.errorReceiver.warning(null, Messages.format("Driver.ExperimentalLanguageWarning", msg[0], msg[1]));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 191 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkTooManySchemaErrors() {
/* 245 */     if ((this.opt.getGrammars()).length != 1) {
/* 246 */       this.errorReceiver.error(null, Messages.format("GrammarLoader.TooManySchema"));
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
/*     */   private AnnotatedGrammar loadDTD(InputSource source, InputSource bindFile) {
/* 260 */     return TDTDReader.parse(source, bindFile, (ErrorReceiver)this.errorReceiver, this.opt, new ExpressionPool());
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
/*     */   public DOMForest buildDOMForest(InternalizationLogic logic) throws SAXException, IOException {
/*     */     DOMForest forest;
/*     */     try {
/* 278 */       forest = new DOMForest(logic);
/* 279 */     } catch (ParserConfigurationException e) {
/*     */       
/* 281 */       throw new SAXException(e);
/*     */     } 
/*     */     
/* 284 */     forest.setErrorHandler((ErrorHandler)this.errorReceiver);
/* 285 */     if (this.opt.entityResolver != null) {
/* 286 */       forest.setEntityResolver(this.opt.entityResolver);
/*     */     }
/*     */     
/* 289 */     InputSource[] sources = this.opt.getGrammars();
/* 290 */     for (int i = 0; i < sources.length; i++) {
/* 291 */       forest.parse(sources[i]);
/*     */     }
/*     */     
/* 294 */     InputSource[] externalBindingFiles = this.opt.getBindFiles();
/* 295 */     for (int j = 0; j < externalBindingFiles.length; j++) {
/* 296 */       Element root = forest.parse(externalBindingFiles[j]).getDocumentElement();
/*     */ 
/*     */       
/* 299 */       if (!root.getNamespaceURI().equals("http://java.sun.com/xml/ns/jaxb") || !root.getLocalName().equals("bindings"))
/*     */       {
/* 301 */         this.errorReceiver.error(new SAXParseException(Messages.format("Driver.NotABindingFile", root.getNamespaceURI(), root.getLocalName()), null, externalBindingFiles[j].getSystemId(), -1, -1));
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 311 */     forest.transform();
/*     */     
/* 313 */     return forest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private XSSchemaSet loadXMLSchema(JCodeModel codeModel) throws SAXException, IOException {
/*     */     try {
/* 323 */       this.errorReceiver.info(new SAXParseException("Using Xerces from " + Which.which(Version.class), null));
/*     */     }
/* 325 */     catch (Throwable t) {}
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 330 */       if (this.opt.strictCheck && !SchemaConstraintChecker.check(this.opt.getGrammars(), (ErrorReceiver)this.errorReceiver, this.opt.entityResolver))
/*     */       {
/* 332 */         return null; } 
/* 333 */     } catch (LinkageError e) {
/*     */ 
/*     */ 
/*     */       
/* 337 */       this.errorReceiver.warning(new SAXParseException(Messages.format("GrammarLoader.IncompatibleXerces", e.toString()), null));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 344 */         this.errorReceiver.warning(new SAXParseException("Using Xerces from " + Which.which(Version.class), null));
/*     */       }
/* 346 */       catch (Throwable t) {}
/*     */ 
/*     */ 
/*     */       
/* 350 */       if (this.opt.debugMode)
/*     */       {
/* 352 */         throw e;
/*     */       }
/*     */     } 
/*     */     
/* 356 */     DOMForest forest = buildDOMForest((InternalizationLogic)new XMLSchemaInternalizationLogic());
/*     */ 
/*     */ 
/*     */     
/* 360 */     XSOMParser xsomParser = createXSOMParser(forest, codeModel);
/*     */ 
/*     */     
/* 363 */     InputSource[] grammars = this.opt.getGrammars();
/* 364 */     for (int i = 0; i < grammars.length; i++) {
/* 365 */       xsomParser.parse(grammars[i]);
/*     */     }
/* 367 */     return xsomParser.getResult();
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
/*     */   private XSSchemaSet loadWSDL(JCodeModel codeModel) throws SAXException, IOException {
/* 380 */     DOMForest forest = buildDOMForest((InternalizationLogic)new XMLSchemaInternalizationLogic());
/*     */     
/* 382 */     DOMForestScanner scanner = new DOMForestScanner(forest);
/*     */     
/* 384 */     XSOMParser xsomParser = createXSOMParser(forest, codeModel);
/*     */ 
/*     */     
/* 387 */     InputSource[] grammars = this.opt.getGrammars();
/* 388 */     Document wsdlDom = forest.get(grammars[0].getSystemId());
/*     */     
/* 390 */     NodeList schemas = wsdlDom.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "schema");
/* 391 */     for (int i = 0; i < schemas.getLength(); i++) {
/* 392 */       scanner.scan((Element)schemas.item(i), xsomParser.getParserHandler());
/*     */     }
/* 394 */     return xsomParser.getResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedGrammar annotateXMLSchema(XSSchemaSet xs, JCodeModel codeModel) throws SAXException {
/* 405 */     if (xs == null) {
/* 406 */       return null;
/*     */     }
/* 408 */     return BGMBuilder.build(xs, codeModel, (ErrorReceiver)this.errorReceiver, this.opt.defaultPackage, (this.opt.compatibilityMode == 2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XSOMParser createXSOMParser(DOMForest forest, JCodeModel codeModel) {
/* 417 */     XSOMParser reader = new XSOMParser((XMLParser)new XMLSchemaForestParser(this, forest, null));
/* 418 */     reader.setAnnotationParser((AnnotationParserFactory)new AnnotationParserFactoryImpl(codeModel, this.opt));
/* 419 */     reader.setErrorHandler((ErrorHandler)this.errorReceiver);
/* 420 */     return reader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AnnotatedGrammar loadRELAXNG() throws IOException, SAXException {
/* 429 */     DOMForest forest = buildDOMForest((InternalizationLogic)new RELAXNGInternalizationLogic());
/*     */ 
/*     */     
/* 432 */     (new CustomizationConverter(this.opt)).fixup(forest);
/*     */ 
/*     */     
/* 435 */     Object object = new Object(this, forest);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 472 */     SAXParserFactoryAdaptor sAXParserFactoryAdaptor = new SAXParserFactoryAdaptor((XMLParser)object);
/* 473 */     sAXParserFactoryAdaptor.setNamespaceAware(true);
/*     */     
/* 475 */     TRELAXNGReader reader = new TRELAXNGReader((ErrorReceiver)this.errorReceiver, this.opt.entityResolver, (SAXParserFactory)sAXParserFactoryAdaptor, this.opt.defaultPackage);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 485 */     reader.parse(this.opt.getGrammars()[0]);
/*     */     
/* 487 */     return reader.getAnnotatedResult();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\GrammarLoader.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */