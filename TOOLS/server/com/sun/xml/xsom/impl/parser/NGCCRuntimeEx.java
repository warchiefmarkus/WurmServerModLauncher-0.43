/*     */ package com.sun.xml.xsom.impl.parser;
/*     */ 
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XmlString;
/*     */ import com.sun.xml.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.xsom.impl.SchemaImpl;
/*     */ import com.sun.xml.xsom.impl.UName;
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
/*     */ import org.xml.sax.EntityResolver;
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
/*     */ 
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
/*  43 */   public int finalDefault = 0;
/*     */   
/*  45 */   public int blockDefault = 0;
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
/*  78 */   private final Stack<String> elementNames = new Stack<String>();
/*     */ 
/*     */   
/*     */   private final NGCCRuntimeEx referer;
/*     */ 
/*     */   
/*     */   public SchemaDocumentImpl document;
/*     */ 
/*     */   
/*     */   private Context currentContext;
/*     */   
/*     */   public static final String XMLSchemaNSURI = "http://www.w3.org/2001/XMLSchema";
/*     */ 
/*     */   
/*     */   NGCCRuntimeEx(ParserContext _parser) {
/*  93 */     this(_parser, false, (NGCCRuntimeEx)null);
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
/*     */   public void checkDoubleDefError(XSDeclaration c) throws SAXException {
/* 107 */     if (c == null || ignorableDuplicateComponent(c))
/*     */       return; 
/* 109 */     reportError(Messages.format("DoubleDefinition", new Object[] { c.getName() }));
/* 110 */     reportError(Messages.format("DoubleDefinition.Original", new Object[0]), c.getLocator());
/*     */   }
/*     */   
/*     */   public static boolean ignorableDuplicateComponent(XSDeclaration c) {
/* 114 */     if (c.getTargetNamespace().equals("http://www.w3.org/2001/XMLSchema")) {
/* 115 */       if (c instanceof com.sun.xml.xsom.XSSimpleType)
/*     */       {
/* 117 */         return true; } 
/* 118 */       if (c.isGlobal() && c.getName().equals("anyType"))
/* 119 */         return true; 
/*     */     } 
/* 121 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPatcher(Patch patcher) {
/* 128 */     this.parser.patcherManager.addPatcher(patcher);
/*     */   }
/*     */   public void addErrorChecker(Patch patcher) {
/* 131 */     this.parser.patcherManager.addErrorChecker(patcher);
/*     */   }
/*     */   public void reportError(String msg, Locator loc) throws SAXException {
/* 134 */     this.parser.patcherManager.reportError(msg, loc);
/*     */   }
/*     */   public void reportError(String msg) throws SAXException {
/* 137 */     reportError(msg, getLocator());
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
/*     */   private InputSource resolveRelativeURL(String namespaceURI, String relativeUri) throws SAXException {
/*     */     try {
/* 156 */       String baseUri = getLocator().getSystemId();
/* 157 */       if (baseUri == null)
/*     */       {
/*     */         
/* 160 */         baseUri = this.documentSystemId;
/*     */       }
/* 162 */       String systemId = null;
/* 163 */       if (relativeUri != null) {
/* 164 */         systemId = Uri.resolve(baseUri, relativeUri);
/*     */       }
/* 166 */       EntityResolver er = this.parser.getEntityResolver();
/* 167 */       if (er != null) {
/* 168 */         InputSource is = er.resolveEntity(namespaceURI, systemId);
/* 169 */         if (is != null) {
/* 170 */           return is;
/*     */         }
/*     */       } 
/* 173 */       if (systemId != null) {
/* 174 */         return new InputSource(systemId);
/*     */       }
/* 176 */       return null;
/* 177 */     } catch (IOException e) {
/* 178 */       SAXParseException se = new SAXParseException(e.getMessage(), getLocator(), e);
/* 179 */       this.parser.errorHandler.error(se);
/* 180 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void includeSchema(String schemaLocation) throws SAXException {
/* 186 */     NGCCRuntimeEx runtime = new NGCCRuntimeEx(this.parser, this.chameleonMode, this);
/* 187 */     runtime.currentSchema = this.currentSchema;
/* 188 */     runtime.blockDefault = this.blockDefault;
/* 189 */     runtime.finalDefault = this.finalDefault;
/*     */     
/* 191 */     if (schemaLocation == null) {
/* 192 */       SAXParseException e = new SAXParseException(Messages.format("MissingSchemaLocation", new Object[0]), getLocator());
/*     */       
/* 194 */       this.parser.errorHandler.fatalError(e);
/* 195 */       throw e;
/*     */     } 
/*     */     
/* 198 */     runtime.parseEntity(resolveRelativeURL((String)null, schemaLocation), true, this.currentSchema.getTargetNamespace(), getLocator());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void importSchema(String ns, String schemaLocation) throws SAXException {
/* 204 */     NGCCRuntimeEx newRuntime = new NGCCRuntimeEx(this.parser, false, this);
/* 205 */     InputSource source = resolveRelativeURL(ns, schemaLocation);
/* 206 */     if (source != null) {
/* 207 */       newRuntime.parseEntity(source, false, ns, getLocator());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAlreadyBeenRead() {
/* 246 */     if (this.documentSystemId != null && 
/* 247 */       this.documentSystemId.startsWith("file:///"))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 253 */       this.documentSystemId = "file:/" + this.documentSystemId.substring(8);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 260 */     assert this.document == null;
/* 261 */     this.document = new SchemaDocumentImpl(this.currentSchema, this.documentSystemId);
/*     */     
/* 263 */     SchemaDocumentImpl existing = this.parser.parsedDocuments.get(this.document);
/* 264 */     if (existing == null) {
/* 265 */       this.parser.parsedDocuments.put(this.document, this.document);
/*     */     } else {
/* 267 */       this.document = existing;
/*     */     } 
/*     */     
/* 270 */     assert this.document != null;
/*     */     
/* 272 */     if (this.referer != null) {
/* 273 */       assert this.referer.document != null : "referer " + this.referer.documentSystemId + " has docIdentity==null";
/* 274 */       this.referer.document.references.add(this.document);
/* 275 */       this.document.referers.add(this.referer.document);
/*     */     } 
/*     */     
/* 278 */     return (existing != null);
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
/* 291 */     this.documentSystemId = source.getSystemId();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 297 */       Schema s = new Schema(this, includeMode, expectedNamespace);
/* 298 */       setRootHandler((NGCCHandler)s);
/*     */       
/*     */       try {
/* 301 */         this.parser.parser.parse(source, (ContentHandler)this, getErrorHandler(), this.parser.getEntityResolver());
/*     */       
/*     */       }
/* 304 */       catch (IOException e) {
/* 305 */         SAXParseException se = new SAXParseException(e.toString(), importLocation, e);
/*     */         
/* 307 */         this.parser.errorHandler.fatalError(se);
/* 308 */         throw se;
/*     */       } 
/* 310 */     } catch (SAXException e) {
/* 311 */       this.parser.setErrorFlag();
/* 312 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationParser createAnnotationParser() {
/* 320 */     if (this.parser.getAnnotationParserFactory() == null) {
/* 321 */       return DefaultAnnotationParser.theInstance;
/*     */     }
/* 323 */     return this.parser.getAnnotationParserFactory().create();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAnnotationContextElementName() {
/* 331 */     return this.elementNames.get(this.elementNames.size() - 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Locator copyLocator() {
/* 336 */     return new LocatorImpl(getLocator());
/*     */   }
/*     */   
/*     */   public ErrorHandler getErrorHandler() {
/* 340 */     return this.parser.errorHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnterElementConsumed(String uri, String localName, String qname, Attributes atts) throws SAXException {
/* 345 */     super.onEnterElementConsumed(uri, localName, qname, atts);
/* 346 */     this.elementNames.push(localName);
/*     */   }
/*     */   
/*     */   public void onLeaveElementConsumed(String uri, String localName, String qname) throws SAXException {
/* 350 */     super.onLeaveElementConsumed(uri, localName, qname);
/* 351 */     this.elementNames.pop();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Context
/*     */     implements ValidationContext
/*     */   {
/*     */     private final String prefix;
/*     */     
/*     */     private final String uri;
/*     */     
/*     */     private final Context previous;
/*     */ 
/*     */     
/*     */     Context(String _prefix, String _uri, Context _context) {
/* 366 */       this.previous = _context;
/* 367 */       this.prefix = _prefix;
/* 368 */       this.uri = _uri;
/*     */     }
/*     */     
/*     */     public String resolveNamespacePrefix(String p) {
/* 372 */       if (p.equals(this.prefix)) return this.uri; 
/* 373 */       if (this.previous == null) return null; 
/* 374 */       return this.previous.resolveNamespacePrefix(p);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getBaseUri() {
/* 382 */       return null;
/* 383 */     } public boolean isNotation(String arg0) { return false; } public boolean isUnparsedEntity(String arg0) {
/* 384 */       return false;
/*     */     }
/*     */   }
/* 387 */   private NGCCRuntimeEx(ParserContext _parser, boolean chameleonMode, NGCCRuntimeEx referer) { this.currentContext = null; this.parser = _parser;
/*     */     this.chameleonMode = chameleonMode;
/*     */     this.referer = referer;
/*     */     this.currentContext = new Context("", "", null);
/* 391 */     this.currentContext = new Context("xml", "http://www.w3.org/XML/1998/namespace", this.currentContext); } public ValidationContext createValidationContext() { return this.currentContext; }
/*     */ 
/*     */   
/*     */   public XmlString createXmlString(String value) {
/* 395 */     if (value == null) return null; 
/* 396 */     return new XmlString(value, createValidationContext());
/*     */   }
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException {
/* 400 */     super.startPrefixMapping(prefix, uri);
/* 401 */     this.currentContext = new Context(prefix, uri, this.currentContext);
/*     */   }
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/* 404 */     super.endPrefixMapping(prefix);
/* 405 */     this.currentContext = this.currentContext.previous;
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
/* 421 */     int idx = qname.indexOf(':');
/* 422 */     if (idx < 0) {
/* 423 */       String str = resolveNamespacePrefix("");
/*     */ 
/*     */       
/* 426 */       if (str.equals("") && this.chameleonMode) {
/* 427 */         str = this.currentSchema.getTargetNamespace();
/*     */       }
/*     */       
/* 430 */       return new UName(str, qname, qname);
/*     */     } 
/* 432 */     String prefix = qname.substring(0, idx);
/* 433 */     String uri = this.currentContext.resolveNamespacePrefix(prefix);
/* 434 */     if (uri == null) {
/*     */       
/* 436 */       reportError(Messages.format("UndefinedPrefix", new Object[] { prefix }));
/*     */       
/* 438 */       uri = "undefined";
/*     */     } 
/* 440 */     return new UName(uri, qname.substring(idx + 1), qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean parseBoolean(String v) {
/* 445 */     if (v == null) return false; 
/* 446 */     v = v.trim();
/* 447 */     return (v.equals("true") || v.equals("1"));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void unexpectedX(String token) throws SAXException {
/* 452 */     SAXParseException e = new SAXParseException(MessageFormat.format("Unexpected {0} appears at line {1} column {2}", new Object[] { token, Integer.valueOf(getLocator().getLineNumber()), Integer.valueOf(getLocator().getColumnNumber()) }), getLocator());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 459 */     this.parser.errorHandler.fatalError(e);
/* 460 */     throw e;
/*     */   }
/*     */   
/*     */   public ForeignAttributesImpl parseForeignAttributes(ForeignAttributesImpl next) {
/* 464 */     ForeignAttributesImpl impl = new ForeignAttributesImpl(createValidationContext(), copyLocator(), next);
/*     */     
/* 466 */     Attributes atts = getCurrentAttributes();
/* 467 */     for (int i = 0; i < atts.getLength(); i++) {
/* 468 */       if (atts.getURI(i).length() > 0) {
/* 469 */         impl.addAttribute(atts.getURI(i), atts.getLocalName(i), atts.getQName(i), atts.getType(i), atts.getValue(i));
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 479 */     return impl;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\NGCCRuntimeEx.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */