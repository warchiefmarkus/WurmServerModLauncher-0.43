/*     */ package 1.0.com.sun.xml.xsom.impl.parser;
/*     */ 
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.impl.SchemaImpl;
/*     */ import com.sun.xml.xsom.impl.UName;
/*     */ import com.sun.xml.xsom.impl.parser.DefaultAnnotationParser;
/*     */ import com.sun.xml.xsom.impl.parser.Messages;
/*     */ import com.sun.xml.xsom.impl.parser.ParserContext;
/*     */ import com.sun.xml.xsom.impl.parser.Patch;
/*     */ import com.sun.xml.xsom.impl.parser.PatcherManager;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCHandler;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCRuntime;
/*     */ import com.sun.xml.xsom.impl.parser.state.Schema;
/*     */ import com.sun.xml.xsom.impl.util.Uri;
/*     */ import com.sun.xml.xsom.parser.AnnotationParser;
/*     */ import java.io.IOException;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Stack;
/*     */ import org.relaxng.datatype.ValidationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NGCCRuntimeEx
/*     */   extends NGCCRuntime
/*     */   implements PatcherManager
/*     */ {
/*     */   public final ParserContext parser;
/*     */   public SchemaImpl currentSchema;
/*  39 */   public int finalDefault = 0;
/*     */   
/*  41 */   public int blockDefault = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean elementFormDefault = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean attributeFormDefault = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean chameleonMode = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String documentSystemId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   private final Stack elementNames = new Stack(); private Context currentContext; public static final String XMLSchemaNSURI = "http://www.w3.org/2001/XMLSchema";
/*     */   
/*     */   NGCCRuntimeEx(ParserContext _parser) {
/*  76 */     this(_parser, false);
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
/*     */   public void checkDoubleDefError(XSDeclaration c) throws SAXException {
/*  89 */     if (c == null)
/*  90 */       return;  reportError(Messages.format("DoubleDefinition", c.getName()), getLocator());
/*  91 */     reportError(Messages.format("DoubleDefinition.Original"), c.getLocator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPatcher(Patch patcher) {
/*  98 */     this.parser.patcherManager.addPatcher(patcher);
/*     */   }
/*     */   public void reportError(String msg, Locator loc) throws SAXException {
/* 101 */     this.parser.patcherManager.reportError(msg, loc);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private InputSource resolveRelativeURL(String relativeUri) throws SAXException {
/* 107 */     String baseUri = getLocator().getSystemId();
/* 108 */     if (baseUri == null)
/*     */     {
/*     */       
/* 111 */       baseUri = this.documentSystemId;
/*     */     }
/* 113 */     String systemId = Uri.resolve(baseUri, relativeUri);
/* 114 */     return new InputSource(systemId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void includeSchema(String schemaLocation) throws SAXException {
/* 119 */     com.sun.xml.xsom.impl.parser.NGCCRuntimeEx runtime = new com.sun.xml.xsom.impl.parser.NGCCRuntimeEx(this.parser, this.chameleonMode);
/* 120 */     runtime.currentSchema = this.currentSchema;
/* 121 */     runtime.blockDefault = this.blockDefault;
/* 122 */     runtime.finalDefault = this.finalDefault;
/*     */     
/* 124 */     if (schemaLocation == null) {
/* 125 */       SAXParseException e = new SAXParseException(Messages.format("MissingSchemaLocation"), getLocator());
/*     */       
/* 127 */       this.parser.errorHandler.fatalError(e);
/* 128 */       throw e;
/*     */     } 
/*     */     
/* 131 */     runtime.parseEntity(resolveRelativeURL(schemaLocation), true, this.currentSchema.getTargetNamespace(), getLocator());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void importSchema(String ns, String schemaLocation) throws SAXException {
/* 137 */     if (schemaLocation == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 143 */     com.sun.xml.xsom.impl.parser.NGCCRuntimeEx newRuntime = new com.sun.xml.xsom.impl.parser.NGCCRuntimeEx(this.parser);
/* 144 */     newRuntime.parseEntity(resolveRelativeURL(schemaLocation), false, ns, getLocator());
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
/*     */   public boolean hasAlreadyBeenRead(String targetNamespace) {
/* 180 */     if (this.documentSystemId == null)
/*     */     {
/*     */       
/* 183 */       return false;
/*     */     }
/* 185 */     if (this.documentSystemId.startsWith("file:///"))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 191 */       this.documentSystemId = "file:/" + this.documentSystemId.substring(8);
/*     */     }
/* 193 */     ParserContext.DocumentIdentity docIdentity = new ParserContext.DocumentIdentity(targetNamespace, this.documentSystemId);
/*     */ 
/*     */ 
/*     */     
/* 197 */     return !this.parser.parsedDocuments.add(docIdentity);
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
/*     */   public void parseEntity(InputSource source, boolean includeMode, String expectedNamespace, Locator importLocation) throws SAXException {
/* 210 */     this.documentSystemId = source.getSystemId();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 216 */       Schema s = new Schema(this, includeMode, expectedNamespace);
/* 217 */       setRootHandler((NGCCHandler)s);
/*     */       
/*     */       try {
/* 220 */         this.parser.parser.parse(source, (ContentHandler)this, getErrorHandler(), this.parser.getEntityResolver());
/*     */       
/*     */       }
/* 223 */       catch (IOException e) {
/* 224 */         SAXParseException se = new SAXParseException(e.toString(), importLocation, e);
/*     */         
/* 226 */         this.parser.errorHandler.fatalError(se);
/* 227 */         throw se;
/*     */       } 
/* 229 */     } catch (SAXException e) {
/* 230 */       this.parser.setErrorFlag();
/* 231 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationParser createAnnotationParser() {
/* 239 */     if (this.parser.getAnnotationParserFactory() == null) {
/* 240 */       return DefaultAnnotationParser.theInstance;
/*     */     }
/* 242 */     return this.parser.getAnnotationParserFactory().create();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAnnotationContextElementName() {
/* 250 */     return this.elementNames.get(this.elementNames.size() - 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Locator copyLocator() {
/* 255 */     return new LocatorImpl(getLocator());
/*     */   }
/*     */   
/*     */   public ErrorHandler getErrorHandler() {
/* 259 */     return this.parser.errorHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnterElementConsumed(String uri, String localName, String qname, Attributes atts) throws SAXException {
/* 264 */     super.onEnterElementConsumed(uri, localName, qname, atts);
/* 265 */     this.elementNames.push(localName);
/*     */   }
/*     */   
/*     */   public void onLeaveElementConsumed(String uri, String localName, String qname) throws SAXException {
/* 269 */     super.onLeaveElementConsumed(uri, localName, qname);
/* 270 */     this.elementNames.pop();
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
/*     */   private NGCCRuntimeEx(ParserContext _parser, boolean chameleonMode) {
/* 306 */     this.currentContext = null;
/*     */     this.parser = _parser;
/*     */     this.chameleonMode = chameleonMode;
/*     */     this.currentContext = new Context("", "", null);
/* 310 */     this.currentContext = new Context("xml", "http://www.w3.org/XML/1998/namespace", this.currentContext); } public ValidationContext createValidationContext() { return (ValidationContext)this.currentContext; }
/*     */ 
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException {
/* 314 */     super.startPrefixMapping(prefix, uri);
/* 315 */     this.currentContext = new Context(prefix, uri, this.currentContext);
/*     */   }
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/* 318 */     super.endPrefixMapping(prefix);
/* 319 */     this.currentContext = Context.access$000(this.currentContext);
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
/*     */   public UName parseUName(String qname) throws SAXException {
/* 335 */     int idx = qname.indexOf(':');
/* 336 */     if (idx < 0) {
/* 337 */       String str = resolveNamespacePrefix("");
/*     */ 
/*     */       
/* 340 */       if (str.equals("") && this.chameleonMode) {
/* 341 */         str = this.currentSchema.getTargetNamespace();
/*     */       }
/*     */       
/* 344 */       return new UName(str, qname, qname);
/*     */     } 
/* 346 */     String prefix = qname.substring(0, idx);
/* 347 */     String uri = this.currentContext.resolveNamespacePrefix(prefix);
/* 348 */     if (uri == null) {
/*     */       
/* 350 */       reportError(Messages.format("UndefinedPrefix", prefix), getLocator());
/*     */       
/* 352 */       uri = "undefined";
/*     */     } 
/* 354 */     return new UName(uri, qname.substring(idx + 1), qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean parseBoolean(String v) {
/* 359 */     if (v == null) return false; 
/* 360 */     v = v.trim();
/* 361 */     return (v.equals("true") || v.equals("1"));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void unexpectedX(String token) throws SAXException {
/* 366 */     SAXParseException e = new SAXParseException(MessageFormat.format("Unexpected {0} appears at line {1} column {2}", new Object[] { token, new Integer(getLocator().getLineNumber()), new Integer(getLocator().getColumnNumber()) }), getLocator());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 374 */     this.parser.errorHandler.fatalError(e);
/* 375 */     throw e;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\NGCCRuntimeEx.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */