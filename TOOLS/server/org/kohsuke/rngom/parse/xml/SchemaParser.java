/*      */ package org.kohsuke.rngom.parse.xml;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.List;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ import org.kohsuke.rngom.ast.builder.Annotations;
/*      */ import org.kohsuke.rngom.ast.builder.CommentList;
/*      */ import org.kohsuke.rngom.ast.builder.DataPatternBuilder;
/*      */ import org.kohsuke.rngom.ast.builder.Div;
/*      */ import org.kohsuke.rngom.ast.builder.ElementAnnotationBuilder;
/*      */ import org.kohsuke.rngom.ast.builder.Grammar;
/*      */ import org.kohsuke.rngom.ast.builder.GrammarSection;
/*      */ import org.kohsuke.rngom.ast.builder.Include;
/*      */ import org.kohsuke.rngom.ast.builder.IncludedGrammar;
/*      */ import org.kohsuke.rngom.ast.builder.NameClassBuilder;
/*      */ import org.kohsuke.rngom.ast.builder.SchemaBuilder;
/*      */ import org.kohsuke.rngom.ast.builder.Scope;
/*      */ import org.kohsuke.rngom.ast.om.Location;
/*      */ import org.kohsuke.rngom.ast.om.ParsedElementAnnotation;
/*      */ import org.kohsuke.rngom.ast.om.ParsedNameClass;
/*      */ import org.kohsuke.rngom.ast.om.ParsedPattern;
/*      */ import org.kohsuke.rngom.parse.Context;
/*      */ import org.kohsuke.rngom.parse.IllegalSchemaException;
/*      */ import org.kohsuke.rngom.parse.Parseable;
/*      */ import org.kohsuke.rngom.util.Localizer;
/*      */ import org.kohsuke.rngom.util.Uri;
/*      */ import org.kohsuke.rngom.xml.sax.AbstractLexicalHandler;
/*      */ import org.kohsuke.rngom.xml.sax.XmlBaseHandler;
/*      */ import org.kohsuke.rngom.xml.util.Naming;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.ErrorHandler;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.SAXNotRecognizedException;
/*      */ import org.xml.sax.SAXNotSupportedException;
/*      */ import org.xml.sax.SAXParseException;
/*      */ import org.xml.sax.XMLReader;
/*      */ import org.xml.sax.helpers.DefaultHandler;
/*      */ 
/*      */ 
/*      */ 
/*      */ class SchemaParser
/*      */ {
/*   49 */   private static final String relaxngURIPrefix = "http://relaxng.org/ns/structure/1.0".substring(0, "http://relaxng.org/ns/structure/1.0".lastIndexOf('/') + 1);
/*      */   
/*      */   static final String relaxng10URI = "http://relaxng.org/ns/structure/1.0";
/*   52 */   private static final Localizer localizer = new Localizer(new Localizer(Parseable.class), SchemaParser.class);
/*      */   
/*      */   private String relaxngURI;
/*      */   
/*      */   private final XMLReader xr;
/*      */   
/*      */   private final ErrorHandler eh;
/*      */   
/*      */   private final SchemaBuilder schemaBuilder;
/*      */   
/*      */   private final NameClassBuilder nameClassBuilder;
/*      */   private ParsedPattern startPattern;
/*      */   private Locator locator;
/*   65 */   private final XmlBaseHandler xmlBaseHandler = new XmlBaseHandler();
/*   66 */   private final ContextImpl context = new ContextImpl(); private boolean hadError = false; private Hashtable patternTable;
/*      */   private Hashtable nameClassTable;
/*      */   private static final int INIT_CHILD_ALLOC = 5;
/*      */   private static final int PATTERN_CONTEXT = 0;
/*      */   private static final int ANY_NAME_CONTEXT = 1;
/*      */   private static final int NS_NAME_CONTEXT = 2;
/*      */   private SAXParseable parseable;
/*      */   
/*      */   static class PrefixMapping { final String prefix;
/*      */     final String uri;
/*      */     final PrefixMapping next;
/*      */     
/*      */     PrefixMapping(String prefix, String uri, PrefixMapping next) {
/*   79 */       this.prefix = prefix;
/*   80 */       this.uri = uri;
/*   81 */       this.next = next;
/*      */     } }
/*      */ 
/*      */   
/*      */   static abstract class AbstractContext extends DtdContext implements Context {
/*      */     SchemaParser.PrefixMapping prefixMapping;
/*      */     
/*      */     AbstractContext() {
/*   89 */       this.prefixMapping = new SchemaParser.PrefixMapping("xml", "http://www.w3.org/XML/1998/namespace", null);
/*      */     }
/*      */     
/*      */     AbstractContext(AbstractContext context) {
/*   93 */       super(context);
/*   94 */       this.prefixMapping = context.prefixMapping;
/*      */     }
/*      */     
/*      */     public String resolveNamespacePrefix(String prefix) {
/*   98 */       for (SchemaParser.PrefixMapping p = this.prefixMapping; p != null; p = p.next) {
/*   99 */         if (p.prefix.equals(prefix))
/*  100 */           return p.uri; 
/*  101 */       }  return null;
/*      */     }
/*      */     
/*      */     public Enumeration prefixes() {
/*  105 */       Vector<String> v = new Vector();
/*  106 */       for (SchemaParser.PrefixMapping p = this.prefixMapping; p != null; p = p.next) {
/*  107 */         if (!v.contains(p.prefix))
/*  108 */           v.addElement(p.prefix); 
/*      */       } 
/*  110 */       return v.elements();
/*      */     }
/*      */     
/*      */     public Context copy() {
/*  114 */       return new SchemaParser.SavedContext(this);
/*      */     } }
/*      */   
/*      */   static class SavedContext extends AbstractContext {
/*      */     private final String baseUri;
/*      */     
/*      */     SavedContext(SchemaParser.AbstractContext context) {
/*  121 */       super(context);
/*  122 */       this.baseUri = context.getBaseUri();
/*      */     }
/*      */     
/*      */     public String getBaseUri() {
/*  126 */       return this.baseUri;
/*      */     }
/*      */   }
/*      */   
/*      */   class ContextImpl extends AbstractContext {
/*      */     public String getBaseUri() {
/*  132 */       return SchemaParser.this.xmlBaseHandler.getBaseUri();
/*      */     }
/*      */   }
/*      */   
/*      */   static interface CommentHandler {
/*      */     void comment(String param1String);
/*      */   }
/*      */   
/*      */   abstract class Handler implements ContentHandler, CommentHandler {
/*      */     CommentList comments;
/*      */     
/*      */     CommentList getComments() {
/*  144 */       CommentList tem = this.comments;
/*  145 */       this.comments = null;
/*  146 */       return tem;
/*      */     }
/*      */     
/*      */     public void comment(String value) {
/*  150 */       if (this.comments == null)
/*  151 */         this.comments = SchemaParser.this.schemaBuilder.makeCommentList(); 
/*  152 */       this.comments.addComment(value, SchemaParser.this.makeLocation());
/*      */     }
/*      */     
/*      */     public void processingInstruction(String target, String date) {}
/*      */     
/*      */     public void skippedEntity(String name) {}
/*      */     
/*      */     public void startPrefixMapping(String prefix, String uri) {
/*  160 */       SchemaParser.this.context.prefixMapping = new SchemaParser.PrefixMapping(prefix, uri, SchemaParser.this.context.prefixMapping);
/*      */     } public void ignorableWhitespace(char[] ch, int start, int len) {} public void startDocument() {}
/*      */     public void endDocument() {}
/*      */     public void endPrefixMapping(String prefix) {
/*  164 */       SchemaParser.this.context.prefixMapping = SchemaParser.this.context.prefixMapping.next;
/*      */     }
/*      */     
/*      */     public void setDocumentLocator(Locator loc) {
/*  168 */       SchemaParser.this.locator = loc;
/*  169 */       SchemaParser.this.xmlBaseHandler.setLocator(loc);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   abstract class State
/*      */     extends Handler
/*      */   {
/*      */     State parent;
/*      */     String nsInherit;
/*      */     String ns;
/*      */     String datatypeLibrary;
/*      */     Scope scope;
/*      */     Location startLocation;
/*      */     Annotations annotations;
/*      */     
/*      */     void set() {
/*  186 */       SchemaParser.this.xr.setContentHandler(this);
/*      */     }
/*      */     
/*      */     abstract State create();
/*      */     
/*      */     abstract State createChildState(String param1String) throws SAXException;
/*      */     
/*      */     void setParent(State parent) {
/*  194 */       this.parent = parent;
/*  195 */       this.nsInherit = parent.getNs();
/*  196 */       this.datatypeLibrary = parent.datatypeLibrary;
/*  197 */       this.scope = parent.scope;
/*  198 */       this.startLocation = SchemaParser.this.makeLocation();
/*  199 */       if (parent.comments != null) {
/*  200 */         this.annotations = SchemaParser.this.schemaBuilder.makeAnnotations(parent.comments, SchemaParser.this.getContext());
/*  201 */         parent.comments = null;
/*      */       }
/*  203 */       else if (parent instanceof SchemaParser.RootState) {
/*  204 */         this.annotations = SchemaParser.this.schemaBuilder.makeAnnotations(null, SchemaParser.this.getContext());
/*      */       } 
/*      */     }
/*      */     String getNs() {
/*  208 */       return (this.ns == null) ? this.nsInherit : this.ns;
/*      */     }
/*      */     
/*      */     boolean isRelaxNGElement(String uri) throws SAXException {
/*  212 */       return uri.equals(SchemaParser.this.relaxngURI);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/*  219 */       SchemaParser.this.xmlBaseHandler.startElement();
/*  220 */       if (isRelaxNGElement(namespaceURI)) {
/*  221 */         State state = createChildState(localName);
/*  222 */         if (state == null) {
/*  223 */           SchemaParser.this.xr.setContentHandler(new SchemaParser.Skipper(this));
/*      */           return;
/*      */         } 
/*  226 */         state.setParent(this);
/*  227 */         state.set();
/*  228 */         state.attributes(atts);
/*      */       } else {
/*      */         
/*  231 */         checkForeignElement();
/*  232 */         SchemaParser.ForeignElementHandler feh = new SchemaParser.ForeignElementHandler(this, getComments());
/*  233 */         feh.startElement(namespaceURI, localName, qName, atts);
/*  234 */         SchemaParser.this.xr.setContentHandler(feh);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
/*  241 */       SchemaParser.this.xmlBaseHandler.endElement();
/*  242 */       this.parent.set();
/*  243 */       end();
/*      */     }
/*      */     
/*      */     void setName(String name) throws SAXException {
/*  247 */       SchemaParser.this.error("illegal_name_attribute");
/*      */     }
/*      */     
/*      */     void setOtherAttribute(String name, String value) throws SAXException {
/*  251 */       SchemaParser.this.error("illegal_attribute_ignored", name);
/*      */     }
/*      */ 
/*      */     
/*      */     void endAttributes() throws SAXException {}
/*      */ 
/*      */     
/*      */     void checkForeignElement() throws SAXException {}
/*      */     
/*      */     void attributes(Attributes atts) throws SAXException {
/*  261 */       int len = atts.getLength();
/*  262 */       for (int i = 0; i < len; i++) {
/*  263 */         String uri = atts.getURI(i);
/*  264 */         if (uri.length() == 0) {
/*  265 */           String name = atts.getLocalName(i);
/*  266 */           if (name.equals("name")) {
/*  267 */             setName(atts.getValue(i).trim());
/*  268 */           } else if (name.equals("ns")) {
/*  269 */             this.ns = atts.getValue(i);
/*  270 */           } else if (name.equals("datatypeLibrary")) {
/*  271 */             this.datatypeLibrary = atts.getValue(i);
/*  272 */             SchemaParser.this.checkUri(this.datatypeLibrary);
/*  273 */             if (!this.datatypeLibrary.equals("") && !Uri.isAbsolute(this.datatypeLibrary))
/*      */             {
/*  275 */               SchemaParser.this.error("relative_datatype_library"); } 
/*  276 */             if (Uri.hasFragmentId(this.datatypeLibrary))
/*  277 */               SchemaParser.this.error("fragment_identifier_datatype_library"); 
/*  278 */             this.datatypeLibrary = Uri.escapeDisallowedChars(this.datatypeLibrary);
/*      */           } else {
/*      */             
/*  281 */             setOtherAttribute(name, atts.getValue(i));
/*      */           } 
/*  283 */         } else if (uri.equals(SchemaParser.this.relaxngURI)) {
/*  284 */           SchemaParser.this.error("qualified_attribute", atts.getLocalName(i));
/*  285 */         } else if (uri.equals("http://www.w3.org/XML/1998/namespace") && atts.getLocalName(i).equals("base")) {
/*      */           
/*  287 */           SchemaParser.this.xmlBaseHandler.xmlBaseAttribute(atts.getValue(i));
/*      */         } else {
/*  289 */           if (this.annotations == null)
/*  290 */             this.annotations = SchemaParser.this.schemaBuilder.makeAnnotations(null, SchemaParser.this.getContext()); 
/*  291 */           this.annotations.addAttribute(uri, atts.getLocalName(i), SchemaParser.this.findPrefix(atts.getQName(i), uri), atts.getValue(i), this.startLocation);
/*      */         } 
/*      */       } 
/*      */       
/*  295 */       endAttributes();
/*      */     }
/*      */ 
/*      */     
/*      */     abstract void end() throws SAXException;
/*      */ 
/*      */     
/*      */     void endChild(ParsedPattern pattern) {}
/*      */ 
/*      */     
/*      */     void endChild(ParsedNameClass nc) {}
/*      */     
/*      */     public void startDocument() {}
/*      */     
/*      */     public void endDocument() {
/*  310 */       if (this.comments != null && SchemaParser.this.startPattern != null) {
/*  311 */         SchemaParser.this.startPattern = SchemaParser.this.schemaBuilder.commentAfter(SchemaParser.this.startPattern, this.comments);
/*  312 */         this.comments = null;
/*      */       } 
/*      */     }
/*      */     
/*      */     public void characters(char[] ch, int start, int len) throws SAXException {
/*  317 */       for (int i = 0; i < len; i++) {
/*  318 */         switch (ch[start + i]) {
/*      */           case '\t':
/*      */           case '\n':
/*      */           case '\r':
/*      */           case ' ':
/*      */             break;
/*      */           default:
/*  325 */             SchemaParser.this.error("illegal_characters_ignored");
/*      */             break;
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     boolean isPatternNamespaceURI(String s) {
/*  332 */       return s.equals(SchemaParser.this.relaxngURI);
/*      */     }
/*      */     
/*      */     void endForeignChild(ParsedElementAnnotation ea) {
/*  336 */       if (this.annotations == null)
/*  337 */         this.annotations = SchemaParser.this.schemaBuilder.makeAnnotations(null, SchemaParser.this.getContext()); 
/*  338 */       this.annotations.addElement(ea);
/*      */     }
/*      */     
/*      */     void mergeLeadingComments() {
/*  342 */       if (this.comments != null) {
/*  343 */         if (this.annotations == null) {
/*  344 */           this.annotations = SchemaParser.this.schemaBuilder.makeAnnotations(this.comments, SchemaParser.this.getContext());
/*      */         } else {
/*  346 */           this.annotations.addLeadingComment(this.comments);
/*  347 */         }  this.comments = null;
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   class ForeignElementHandler extends Handler {
/*      */     final SchemaParser.State nextState;
/*      */     ElementAnnotationBuilder builder;
/*  355 */     final Stack builderStack = new Stack();
/*      */     StringBuffer textBuf;
/*      */     Location textLoc;
/*      */     
/*      */     ForeignElementHandler(SchemaParser.State nextState, CommentList comments) {
/*  360 */       this.nextState = nextState;
/*  361 */       this.comments = comments;
/*      */     }
/*      */ 
/*      */     
/*      */     public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
/*  366 */       flushText();
/*  367 */       if (this.builder != null)
/*  368 */         this.builderStack.push(this.builder); 
/*  369 */       Location loc = SchemaParser.this.makeLocation();
/*  370 */       this.builder = SchemaParser.this.schemaBuilder.makeElementAnnotationBuilder(namespaceURI, localName, SchemaParser.this.findPrefix(qName, namespaceURI), loc, getComments(), SchemaParser.this.getContext());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  376 */       int len = atts.getLength();
/*  377 */       for (int i = 0; i < len; i++) {
/*  378 */         String uri = atts.getURI(i);
/*  379 */         this.builder.addAttribute(uri, atts.getLocalName(i), SchemaParser.this.findPrefix(atts.getQName(i), uri), atts.getValue(i), loc);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void endElement(String namespaceURI, String localName, String qName) {
/*  386 */       flushText();
/*  387 */       if (this.comments != null)
/*  388 */         this.builder.addComment(getComments()); 
/*  389 */       ParsedElementAnnotation ea = this.builder.makeElementAnnotation();
/*  390 */       if (this.builderStack.empty()) {
/*  391 */         this.nextState.endForeignChild(ea);
/*  392 */         this.nextState.set();
/*      */       } else {
/*      */         
/*  395 */         this.builder = this.builderStack.pop();
/*  396 */         this.builder.addElement(ea);
/*      */       } 
/*      */     }
/*      */     
/*      */     public void characters(char[] ch, int start, int length) {
/*  401 */       if (this.textBuf == null)
/*  402 */         this.textBuf = new StringBuffer(); 
/*  403 */       this.textBuf.append(ch, start, length);
/*  404 */       if (this.textLoc == null)
/*  405 */         this.textLoc = SchemaParser.this.makeLocation(); 
/*      */     }
/*      */     
/*      */     public void comment(String value) {
/*  409 */       flushText();
/*  410 */       super.comment(value);
/*      */     }
/*      */     
/*      */     void flushText() {
/*  414 */       if (this.textBuf != null && this.textBuf.length() != 0) {
/*  415 */         this.builder.addText(this.textBuf.toString(), this.textLoc, getComments());
/*  416 */         this.textBuf.setLength(0);
/*      */       } 
/*  418 */       this.textLoc = null;
/*      */     }
/*      */   }
/*      */   
/*      */   class Skipper extends DefaultHandler implements CommentHandler {
/*  423 */     int level = 1;
/*      */     final SchemaParser.State nextState;
/*      */     
/*      */     Skipper(SchemaParser.State nextState) {
/*  427 */       this.nextState = nextState;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/*  434 */       this.level++;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
/*  440 */       if (--this.level == 0)
/*  441 */         this.nextState.set(); 
/*      */     }
/*      */     
/*      */     public void comment(String value) {}
/*      */   }
/*      */   
/*      */   abstract class EmptyContentState
/*      */     extends State
/*      */   {
/*      */     SchemaParser.State createChildState(String localName) throws SAXException {
/*  451 */       SchemaParser.this.error("expected_empty", localName);
/*  452 */       return null;
/*      */     }
/*      */     
/*      */     abstract ParsedPattern makePattern() throws SAXException;
/*      */     
/*      */     void end() throws SAXException {
/*  458 */       if (this.comments != null) {
/*  459 */         if (this.annotations == null)
/*  460 */           this.annotations = SchemaParser.this.schemaBuilder.makeAnnotations(null, SchemaParser.this.getContext()); 
/*  461 */         this.annotations.addComment(this.comments);
/*  462 */         this.comments = null;
/*      */       } 
/*  464 */       this.parent.endChild(makePattern());
/*      */     }
/*      */   }
/*      */   
/*      */   abstract class PatternContainerState
/*      */     extends State
/*      */   {
/*      */     List<ParsedPattern> childPatterns;
/*      */     
/*      */     SchemaParser.State createChildState(String localName) throws SAXException {
/*  474 */       SchemaParser.State state = (SchemaParser.State)SchemaParser.this.patternTable.get(localName);
/*  475 */       if (state == null) {
/*  476 */         SchemaParser.this.error("expected_pattern", localName);
/*  477 */         return null;
/*      */       } 
/*  479 */       return state.create();
/*      */     }
/*      */     
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) throws SAXException {
/*  483 */       if (patterns.size() == 1 && anno == null)
/*  484 */         return patterns.get(0); 
/*  485 */       return SchemaParser.this.schemaBuilder.makeGroup(patterns, loc, anno);
/*      */     }
/*      */     
/*      */     void endChild(ParsedPattern pattern) {
/*  489 */       if (this.childPatterns == null)
/*  490 */         this.childPatterns = new ArrayList<ParsedPattern>(5); 
/*  491 */       this.childPatterns.add(pattern);
/*      */     }
/*      */     
/*      */     void endForeignChild(ParsedElementAnnotation ea) {
/*  495 */       if (this.childPatterns == null) {
/*  496 */         super.endForeignChild(ea);
/*      */       } else {
/*  498 */         int idx = this.childPatterns.size() - 1;
/*  499 */         this.childPatterns.set(idx, SchemaParser.this.schemaBuilder.annotateAfter(this.childPatterns.get(idx), ea));
/*      */       } 
/*      */     }
/*      */     
/*      */     void end() throws SAXException {
/*  504 */       if (this.childPatterns == null) {
/*  505 */         SchemaParser.this.error("missing_children");
/*  506 */         endChild(SchemaParser.this.schemaBuilder.makeErrorPattern());
/*      */       } 
/*  508 */       if (this.comments != null) {
/*  509 */         int idx = this.childPatterns.size() - 1;
/*  510 */         this.childPatterns.set(idx, SchemaParser.this.schemaBuilder.commentAfter(this.childPatterns.get(idx), this.comments));
/*  511 */         this.comments = null;
/*      */       } 
/*  513 */       sendPatternToParent(buildPattern(this.childPatterns, this.startLocation, this.annotations));
/*      */     }
/*      */     
/*      */     void sendPatternToParent(ParsedPattern p) {
/*  517 */       this.parent.endChild(p);
/*      */     }
/*      */   }
/*      */   
/*      */   class GroupState extends PatternContainerState {
/*      */     SchemaParser.State create() {
/*  523 */       return new GroupState();
/*      */     }
/*      */   }
/*      */   
/*      */   class ZeroOrMoreState extends PatternContainerState {
/*      */     SchemaParser.State create() {
/*  529 */       return new ZeroOrMoreState();
/*      */     }
/*      */     
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) throws SAXException {
/*  533 */       return SchemaParser.this.schemaBuilder.makeZeroOrMore(super.buildPattern(patterns, loc, (Annotations)null), loc, anno);
/*      */     }
/*      */   }
/*      */   
/*      */   class OneOrMoreState extends PatternContainerState {
/*      */     SchemaParser.State create() {
/*  539 */       return new OneOrMoreState();
/*      */     }
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) throws SAXException {
/*  542 */       return SchemaParser.this.schemaBuilder.makeOneOrMore(super.buildPattern(patterns, loc, (Annotations)null), loc, anno);
/*      */     }
/*      */   }
/*      */   
/*      */   class OptionalState extends PatternContainerState {
/*      */     SchemaParser.State create() {
/*  548 */       return new OptionalState();
/*      */     }
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) throws SAXException {
/*  551 */       return SchemaParser.this.schemaBuilder.makeOptional(super.buildPattern(patterns, loc, (Annotations)null), loc, anno);
/*      */     }
/*      */   }
/*      */   
/*      */   class ListState extends PatternContainerState {
/*      */     SchemaParser.State create() {
/*  557 */       return new ListState();
/*      */     }
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) throws SAXException {
/*  560 */       return SchemaParser.this.schemaBuilder.makeList(super.buildPattern(patterns, loc, (Annotations)null), loc, anno);
/*      */     }
/*      */   }
/*      */   
/*      */   class ChoiceState extends PatternContainerState {
/*      */     SchemaParser.State create() {
/*  566 */       return new ChoiceState();
/*      */     }
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) throws SAXException {
/*  569 */       return SchemaParser.this.schemaBuilder.makeChoice(patterns, loc, anno);
/*      */     }
/*      */   }
/*      */   
/*      */   class InterleaveState extends PatternContainerState {
/*      */     SchemaParser.State create() {
/*  575 */       return new InterleaveState();
/*      */     }
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) {
/*  578 */       return SchemaParser.this.schemaBuilder.makeInterleave(patterns, loc, anno);
/*      */     }
/*      */   }
/*      */   
/*      */   class MixedState extends PatternContainerState {
/*      */     SchemaParser.State create() {
/*  584 */       return new MixedState();
/*      */     }
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) throws SAXException {
/*  587 */       return SchemaParser.this.schemaBuilder.makeMixed(super.buildPattern(patterns, loc, (Annotations)null), loc, anno);
/*      */     }
/*      */   }
/*      */   
/*      */   static interface NameClassRef {
/*      */     void setNameClass(ParsedNameClass param1ParsedNameClass);
/*      */   }
/*      */   
/*      */   class ElementState extends PatternContainerState implements NameClassRef {
/*      */     ParsedNameClass nameClass;
/*      */     boolean nameClassWasAttribute;
/*      */     String name;
/*      */     
/*      */     void setName(String name) {
/*  601 */       this.name = name;
/*      */     }
/*      */     
/*      */     public void setNameClass(ParsedNameClass nc) {
/*  605 */       this.nameClass = nc;
/*      */     }
/*      */     
/*      */     void endAttributes() throws SAXException {
/*  609 */       if (this.name != null) {
/*  610 */         this.nameClass = SchemaParser.this.expandName(this.name, getNs(), null);
/*  611 */         this.nameClassWasAttribute = true;
/*      */       } else {
/*      */         
/*  614 */         (new SchemaParser.NameClassChildState(this, this)).set();
/*      */       } 
/*      */     }
/*      */     SchemaParser.State create() {
/*  618 */       return new ElementState();
/*      */     }
/*      */     
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) throws SAXException {
/*  622 */       return SchemaParser.this.schemaBuilder.makeElement(this.nameClass, super.buildPattern(patterns, loc, (Annotations)null), loc, anno);
/*      */     }
/*      */     
/*      */     void endForeignChild(ParsedElementAnnotation ea) {
/*  626 */       if (this.nameClassWasAttribute || this.childPatterns != null || this.nameClass == null) {
/*  627 */         super.endForeignChild(ea);
/*      */       } else {
/*  629 */         this.nameClass = SchemaParser.this.nameClassBuilder.annotateAfter(this.nameClass, ea);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   class RootState extends PatternContainerState {
/*      */     IncludedGrammar grammar;
/*      */     
/*      */     RootState() {}
/*      */     
/*      */     RootState(IncludedGrammar grammar, Scope scope, String ns) {
/*  640 */       this.grammar = grammar;
/*  641 */       this.scope = scope;
/*  642 */       this.nsInherit = ns;
/*  643 */       this.datatypeLibrary = "";
/*      */     }
/*      */     
/*      */     SchemaParser.State create() {
/*  647 */       return new RootState();
/*      */     }
/*      */     
/*      */     SchemaParser.State createChildState(String localName) throws SAXException {
/*  651 */       if (this.grammar == null)
/*  652 */         return super.createChildState(localName); 
/*  653 */       if (localName.equals("grammar"))
/*  654 */         return new SchemaParser.MergeGrammarState(this.grammar); 
/*  655 */       SchemaParser.this.error("expected_grammar", localName);
/*  656 */       return null;
/*      */     }
/*      */     
/*      */     void checkForeignElement() throws SAXException {
/*  660 */       SchemaParser.this.error("root_bad_namespace_uri", "http://relaxng.org/ns/structure/1.0");
/*      */     }
/*      */     
/*      */     void endChild(ParsedPattern pattern) {
/*  664 */       SchemaParser.this.startPattern = pattern;
/*      */     }
/*      */     
/*      */     boolean isRelaxNGElement(String uri) throws SAXException {
/*  668 */       if (!uri.startsWith(SchemaParser.relaxngURIPrefix))
/*  669 */         return false; 
/*  670 */       if (!uri.equals("http://relaxng.org/ns/structure/1.0")) {
/*  671 */         SchemaParser.this.warning("wrong_uri_version", "http://relaxng.org/ns/structure/1.0".substring(SchemaParser.relaxngURIPrefix.length()), uri.substring(SchemaParser.relaxngURIPrefix.length()));
/*      */       }
/*      */       
/*  674 */       SchemaParser.this.relaxngURI = uri;
/*  675 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   class NotAllowedState
/*      */     extends EmptyContentState {
/*      */     SchemaParser.State create() {
/*  682 */       return new NotAllowedState();
/*      */     }
/*      */     
/*      */     ParsedPattern makePattern() {
/*  686 */       return SchemaParser.this.schemaBuilder.makeNotAllowed(this.startLocation, this.annotations);
/*      */     }
/*      */   }
/*      */   
/*      */   class EmptyState extends EmptyContentState {
/*      */     SchemaParser.State create() {
/*  692 */       return new EmptyState();
/*      */     }
/*      */     
/*      */     ParsedPattern makePattern() {
/*  696 */       return SchemaParser.this.schemaBuilder.makeEmpty(this.startLocation, this.annotations);
/*      */     }
/*      */   }
/*      */   
/*      */   class TextState extends EmptyContentState {
/*      */     SchemaParser.State create() {
/*  702 */       return new TextState();
/*      */     }
/*      */     
/*      */     ParsedPattern makePattern() {
/*  706 */       return SchemaParser.this.schemaBuilder.makeText(this.startLocation, this.annotations);
/*      */     } }
/*      */   class ValueState extends EmptyContentState { final StringBuffer buf; String type;
/*      */     
/*      */     ValueState() {
/*  711 */       this.buf = new StringBuffer();
/*      */     }
/*      */     
/*      */     SchemaParser.State create() {
/*  715 */       return new ValueState();
/*      */     }
/*      */     
/*      */     void setOtherAttribute(String name, String value) throws SAXException {
/*  719 */       if (name.equals("type")) {
/*  720 */         this.type = SchemaParser.this.checkNCName(value.trim());
/*      */       } else {
/*  722 */         super.setOtherAttribute(name, value);
/*      */       } 
/*      */     }
/*      */     public void characters(char[] ch, int start, int len) {
/*  726 */       this.buf.append(ch, start, len);
/*      */     }
/*      */     
/*      */     void checkForeignElement() throws SAXException {
/*  730 */       SchemaParser.this.error("value_contains_foreign_element");
/*      */     }
/*      */     
/*      */     ParsedPattern makePattern() throws SAXException {
/*  734 */       if (this.type == null) {
/*  735 */         return makePattern("", "token");
/*      */       }
/*  737 */       return makePattern(this.datatypeLibrary, this.type);
/*      */     }
/*      */     
/*      */     void end() throws SAXException {
/*  741 */       mergeLeadingComments();
/*  742 */       super.end();
/*      */     }
/*      */     
/*      */     ParsedPattern makePattern(String datatypeLibrary, String type) {
/*  746 */       return SchemaParser.this.schemaBuilder.makeValue(datatypeLibrary, type, this.buf.toString(), SchemaParser.this.getContext(), getNs(), this.startLocation, this.annotations);
/*      */     } }
/*      */ 
/*      */ 
/*      */   
/*      */   class DataState
/*      */     extends State
/*      */   {
/*      */     String type;
/*      */     ParsedPattern except;
/*      */     DataPatternBuilder dpb;
/*      */     
/*      */     DataState() {
/*  759 */       this.except = null;
/*  760 */       this.dpb = null;
/*      */     }
/*      */     SchemaParser.State create() {
/*  763 */       return new DataState();
/*      */     }
/*      */     
/*      */     SchemaParser.State createChildState(String localName) throws SAXException {
/*  767 */       if (localName.equals("param")) {
/*  768 */         if (this.except != null)
/*  769 */           SchemaParser.this.error("param_after_except"); 
/*  770 */         return new SchemaParser.ParamState(this.dpb);
/*      */       } 
/*  772 */       if (localName.equals("except")) {
/*  773 */         if (this.except != null)
/*  774 */           SchemaParser.this.error("multiple_except"); 
/*  775 */         return new SchemaParser.ChoiceState();
/*      */       } 
/*  777 */       SchemaParser.this.error("expected_param_except", localName);
/*  778 */       return null;
/*      */     }
/*      */     
/*      */     void setOtherAttribute(String name, String value) throws SAXException {
/*  782 */       if (name.equals("type")) {
/*  783 */         this.type = SchemaParser.this.checkNCName(value.trim());
/*      */       } else {
/*  785 */         super.setOtherAttribute(name, value);
/*      */       } 
/*      */     }
/*      */     void endAttributes() throws SAXException {
/*  789 */       if (this.type == null) {
/*  790 */         SchemaParser.this.error("missing_type_attribute");
/*      */       } else {
/*  792 */         this.dpb = SchemaParser.this.schemaBuilder.makeDataPatternBuilder(this.datatypeLibrary, this.type, this.startLocation);
/*      */       } 
/*      */     }
/*      */     void endForeignChild(ParsedElementAnnotation ea) {
/*  796 */       this.dpb.annotation(ea);
/*      */     }
/*      */     
/*      */     void end() throws SAXException {
/*      */       ParsedPattern p;
/*  801 */       if (this.dpb != null) {
/*  802 */         if (this.except != null) {
/*  803 */           p = this.dpb.makePattern(this.except, this.startLocation, this.annotations);
/*      */         } else {
/*  805 */           p = this.dpb.makePattern(this.startLocation, this.annotations);
/*      */         } 
/*      */       } else {
/*  808 */         p = SchemaParser.this.schemaBuilder.makeErrorPattern();
/*      */       } 
/*  810 */       this.parent.endChild(p);
/*      */     }
/*      */     
/*      */     void endChild(ParsedPattern pattern) {
/*  814 */       this.except = pattern;
/*      */     }
/*      */   }
/*      */   
/*      */   class ParamState
/*      */     extends State {
/*  820 */     private final StringBuffer buf = new StringBuffer();
/*      */     private final DataPatternBuilder dpb;
/*      */     private String name;
/*      */     
/*      */     ParamState(DataPatternBuilder dpb) {
/*  825 */       this.dpb = dpb;
/*      */     }
/*      */     
/*      */     SchemaParser.State create() {
/*  829 */       return new ParamState(null);
/*      */     }
/*      */     
/*      */     void setName(String name) throws SAXException {
/*  833 */       this.name = SchemaParser.this.checkNCName(name);
/*      */     }
/*      */     
/*      */     void endAttributes() throws SAXException {
/*  837 */       if (this.name == null)
/*  838 */         SchemaParser.this.error("missing_name_attribute"); 
/*      */     }
/*      */     
/*      */     SchemaParser.State createChildState(String localName) throws SAXException {
/*  842 */       SchemaParser.this.error("expected_empty", localName);
/*  843 */       return null;
/*      */     }
/*      */     
/*      */     public void characters(char[] ch, int start, int len) {
/*  847 */       this.buf.append(ch, start, len);
/*      */     }
/*      */     
/*      */     void checkForeignElement() throws SAXException {
/*  851 */       SchemaParser.this.error("param_contains_foreign_element");
/*      */     }
/*      */     
/*      */     void end() throws SAXException {
/*  855 */       if (this.name == null)
/*      */         return; 
/*  857 */       if (this.dpb == null)
/*      */         return; 
/*  859 */       mergeLeadingComments();
/*  860 */       this.dpb.addParam(this.name, this.buf.toString(), SchemaParser.this.getContext(), getNs(), this.startLocation, this.annotations);
/*      */     }
/*      */   }
/*      */   
/*      */   class AttributeState extends PatternContainerState implements NameClassRef {
/*      */     ParsedNameClass nameClass;
/*      */     boolean nameClassWasAttribute;
/*      */     String name;
/*      */     
/*      */     SchemaParser.State create() {
/*  870 */       return new AttributeState();
/*      */     }
/*      */     
/*      */     void setName(String name) {
/*  874 */       this.name = name;
/*      */     }
/*      */     
/*      */     public void setNameClass(ParsedNameClass nc) {
/*  878 */       this.nameClass = nc;
/*      */     }
/*      */     
/*      */     void endAttributes() throws SAXException {
/*  882 */       if (this.name != null) {
/*      */         String nsUse;
/*  884 */         if (this.ns != null) {
/*  885 */           nsUse = this.ns;
/*      */         } else {
/*  887 */           nsUse = "";
/*  888 */         }  this.nameClass = SchemaParser.this.expandName(this.name, nsUse, null);
/*  889 */         this.nameClassWasAttribute = true;
/*      */       } else {
/*      */         
/*  892 */         (new SchemaParser.NameClassChildState(this, this)).set();
/*      */       } 
/*      */     }
/*      */     void endForeignChild(ParsedElementAnnotation ea) {
/*  896 */       if (this.nameClassWasAttribute || this.childPatterns != null || this.nameClass == null) {
/*  897 */         super.endForeignChild(ea);
/*      */       } else {
/*  899 */         this.nameClass = SchemaParser.this.nameClassBuilder.annotateAfter(this.nameClass, ea);
/*      */       } 
/*      */     }
/*      */     void end() throws SAXException {
/*  903 */       if (this.childPatterns == null)
/*  904 */         endChild(SchemaParser.this.schemaBuilder.makeText(this.startLocation, null)); 
/*  905 */       super.end();
/*      */     }
/*      */     
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) throws SAXException {
/*  909 */       return SchemaParser.this.schemaBuilder.makeAttribute(this.nameClass, super.buildPattern(patterns, loc, (Annotations)null), loc, anno);
/*      */     }
/*      */     
/*      */     SchemaParser.State createChildState(String localName) throws SAXException {
/*  913 */       SchemaParser.State tem = super.createChildState(localName);
/*  914 */       if (tem != null && this.childPatterns != null)
/*  915 */         SchemaParser.this.error("attribute_multi_pattern"); 
/*  916 */       return tem;
/*      */     }
/*      */   }
/*      */   
/*      */   abstract class SinglePatternContainerState
/*      */     extends PatternContainerState {
/*      */     SchemaParser.State createChildState(String localName) throws SAXException {
/*  923 */       if (this.childPatterns == null)
/*  924 */         return super.createChildState(localName); 
/*  925 */       SchemaParser.this.error("too_many_children");
/*  926 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */   class GrammarSectionState extends State {
/*      */     GrammarSection section;
/*      */     
/*      */     GrammarSectionState() {}
/*      */     
/*      */     GrammarSectionState(GrammarSection section) {
/*  936 */       this.section = section;
/*      */     }
/*      */     
/*      */     SchemaParser.State create() {
/*  940 */       return new GrammarSectionState(null);
/*      */     }
/*      */     
/*      */     SchemaParser.State createChildState(String localName) throws SAXException {
/*  944 */       if (localName.equals("define"))
/*  945 */         return new SchemaParser.DefineState(this.section); 
/*  946 */       if (localName.equals("start"))
/*  947 */         return new SchemaParser.StartState(this.section); 
/*  948 */       if (localName.equals("include")) {
/*  949 */         Include include = this.section.makeInclude();
/*  950 */         if (include != null)
/*  951 */           return new SchemaParser.IncludeState(include); 
/*      */       } 
/*  953 */       if (localName.equals("div"))
/*  954 */         return new SchemaParser.DivState(this.section.makeDiv()); 
/*  955 */       SchemaParser.this.error("expected_define", localName);
/*      */       
/*  957 */       return null;
/*      */     }
/*      */     
/*      */     void end() throws SAXException {
/*  961 */       if (this.comments != null) {
/*  962 */         this.section.topLevelComment(this.comments);
/*  963 */         this.comments = null;
/*      */       } 
/*      */     }
/*      */     
/*      */     void endForeignChild(ParsedElementAnnotation ea) {
/*  968 */       this.section.topLevelAnnotation(ea);
/*      */     } }
/*      */   
/*      */   class DivState extends GrammarSectionState {
/*      */     final Div div;
/*      */     
/*      */     DivState(Div div) {
/*  975 */       super((GrammarSection)div);
/*  976 */       this.div = div;
/*      */     }
/*      */     
/*      */     void end() throws SAXException {
/*  980 */       super.end();
/*  981 */       this.div.endDiv(this.startLocation, this.annotations);
/*      */     }
/*      */   }
/*      */   
/*      */   class IncludeState extends GrammarSectionState {
/*      */     String href;
/*      */     final Include include;
/*      */     
/*      */     IncludeState(Include include) {
/*  990 */       super((GrammarSection)include);
/*  991 */       this.include = include;
/*      */     }
/*      */     
/*      */     void setOtherAttribute(String name, String value) throws SAXException {
/*  995 */       if (name.equals("href")) {
/*  996 */         this.href = value;
/*  997 */         SchemaParser.this.checkUri(this.href);
/*      */       } else {
/*      */         
/* 1000 */         super.setOtherAttribute(name, value);
/*      */       } 
/*      */     }
/*      */     void endAttributes() throws SAXException {
/* 1004 */       if (this.href == null) {
/* 1005 */         SchemaParser.this.error("missing_href_attribute");
/*      */       } else {
/* 1007 */         this.href = SchemaParser.this.resolve(this.href);
/*      */       } 
/*      */     }
/*      */     void end() throws SAXException {
/* 1011 */       super.end();
/* 1012 */       if (this.href != null)
/*      */         try {
/* 1014 */           this.include.endInclude(SchemaParser.this.parseable, this.href, getNs(), this.startLocation, this.annotations);
/*      */         }
/* 1016 */         catch (IllegalSchemaException e) {} 
/*      */     }
/*      */   }
/*      */   
/*      */   class MergeGrammarState
/*      */     extends GrammarSectionState {
/*      */     final IncludedGrammar grammar;
/*      */     
/*      */     MergeGrammarState(IncludedGrammar grammar) {
/* 1025 */       super((GrammarSection)grammar);
/* 1026 */       this.grammar = grammar;
/*      */     }
/*      */     
/*      */     void end() throws SAXException {
/* 1030 */       super.end();
/* 1031 */       this.parent.endChild(this.grammar.endIncludedGrammar(this.startLocation, this.annotations));
/*      */     }
/*      */   }
/*      */   
/*      */   class GrammarState extends GrammarSectionState {
/*      */     Grammar grammar;
/*      */     
/*      */     void setParent(SchemaParser.State parent) {
/* 1039 */       super.setParent(parent);
/* 1040 */       this.grammar = SchemaParser.this.schemaBuilder.makeGrammar(this.scope);
/* 1041 */       this.section = (GrammarSection)this.grammar;
/* 1042 */       this.scope = (Scope)this.grammar;
/*      */     }
/*      */     
/*      */     SchemaParser.State create() {
/* 1046 */       return new GrammarState();
/*      */     }
/*      */     
/*      */     void end() throws SAXException {
/* 1050 */       super.end();
/* 1051 */       this.parent.endChild(this.grammar.endGrammar(this.startLocation, this.annotations));
/*      */     }
/*      */   }
/*      */   
/*      */   class RefState extends EmptyContentState {
/*      */     String name;
/*      */     
/*      */     SchemaParser.State create() {
/* 1059 */       return new RefState();
/*      */     }
/*      */ 
/*      */     
/*      */     void endAttributes() throws SAXException {
/* 1064 */       if (this.name == null)
/* 1065 */         SchemaParser.this.error("missing_name_attribute"); 
/*      */     }
/*      */     
/*      */     void setName(String name) throws SAXException {
/* 1069 */       this.name = SchemaParser.this.checkNCName(name);
/*      */     }
/*      */     
/*      */     ParsedPattern makePattern() throws SAXException {
/* 1073 */       if (this.name == null)
/* 1074 */         return SchemaParser.this.schemaBuilder.makeErrorPattern(); 
/* 1075 */       if (this.scope == null) {
/* 1076 */         SchemaParser.this.error("ref_outside_grammar", this.name);
/* 1077 */         return SchemaParser.this.schemaBuilder.makeErrorPattern();
/*      */       } 
/* 1079 */       return this.scope.makeRef(this.name, this.startLocation, this.annotations);
/*      */     }
/*      */   }
/*      */   
/*      */   class ParentRefState extends RefState {
/*      */     SchemaParser.State create() {
/* 1085 */       return new ParentRefState();
/*      */     }
/*      */     
/*      */     ParsedPattern makePattern() throws SAXException {
/* 1089 */       if (this.name == null)
/* 1090 */         return SchemaParser.this.schemaBuilder.makeErrorPattern(); 
/* 1091 */       if (this.scope == null) {
/* 1092 */         SchemaParser.this.error("parent_ref_outside_grammar", this.name);
/* 1093 */         return SchemaParser.this.schemaBuilder.makeErrorPattern();
/*      */       } 
/* 1095 */       return this.scope.makeParentRef(this.name, this.startLocation, this.annotations);
/*      */     }
/*      */   }
/*      */   
/*      */   class ExternalRefState extends EmptyContentState {
/*      */     String href;
/*      */     ParsedPattern includedPattern;
/*      */     
/*      */     SchemaParser.State create() {
/* 1104 */       return new ExternalRefState();
/*      */     }
/*      */     
/*      */     void setOtherAttribute(String name, String value) throws SAXException {
/* 1108 */       if (name.equals("href")) {
/* 1109 */         this.href = value;
/* 1110 */         SchemaParser.this.checkUri(this.href);
/*      */       } else {
/*      */         
/* 1113 */         super.setOtherAttribute(name, value);
/*      */       } 
/*      */     }
/*      */     void endAttributes() throws SAXException {
/* 1117 */       if (this.href == null) {
/* 1118 */         SchemaParser.this.error("missing_href_attribute");
/*      */       } else {
/* 1120 */         this.href = SchemaParser.this.resolve(this.href);
/*      */       } 
/*      */     }
/*      */     ParsedPattern makePattern() {
/* 1124 */       if (this.href != null) {
/*      */         try {
/* 1126 */           return SchemaParser.this.schemaBuilder.makeExternalRef(SchemaParser.this.parseable, this.href, getNs(), this.scope, this.startLocation, this.annotations);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         }
/* 1133 */         catch (IllegalSchemaException e) {}
/*      */       }
/* 1135 */       return SchemaParser.this.schemaBuilder.makeErrorPattern();
/*      */     }
/*      */   }
/*      */   
/*      */   abstract class DefinitionState extends PatternContainerState {
/* 1140 */     GrammarSection.Combine combine = null;
/*      */     final GrammarSection section;
/*      */     
/*      */     DefinitionState(GrammarSection section) {
/* 1144 */       this.section = section;
/*      */     }
/*      */     
/*      */     void setOtherAttribute(String name, String value) throws SAXException {
/* 1148 */       if (name.equals("combine")) {
/* 1149 */         value = value.trim();
/* 1150 */         if (value.equals("choice")) {
/* 1151 */           this.combine = GrammarSection.COMBINE_CHOICE;
/* 1152 */         } else if (value.equals("interleave")) {
/* 1153 */           this.combine = GrammarSection.COMBINE_INTERLEAVE;
/*      */         } else {
/* 1155 */           SchemaParser.this.error("combine_attribute_bad_value", value);
/*      */         } 
/*      */       } else {
/* 1158 */         super.setOtherAttribute(name, value);
/*      */       } 
/*      */     }
/*      */     ParsedPattern buildPattern(List<ParsedPattern> patterns, Location loc, Annotations anno) throws SAXException {
/* 1162 */       return super.buildPattern(patterns, loc, (Annotations)null);
/*      */     }
/*      */   }
/*      */   
/*      */   class DefineState extends DefinitionState {
/*      */     String name;
/*      */     
/*      */     DefineState(GrammarSection section) {
/* 1170 */       super(section);
/*      */     }
/*      */     
/*      */     SchemaParser.State create() {
/* 1174 */       return new DefineState(null);
/*      */     }
/*      */     
/*      */     void setName(String name) throws SAXException {
/* 1178 */       this.name = SchemaParser.this.checkNCName(name);
/*      */     }
/*      */     
/*      */     void endAttributes() throws SAXException {
/* 1182 */       if (this.name == null)
/* 1183 */         SchemaParser.this.error("missing_name_attribute"); 
/*      */     }
/*      */     
/*      */     void sendPatternToParent(ParsedPattern p) {
/* 1187 */       if (this.name != null)
/* 1188 */         this.section.define(this.name, this.combine, p, this.startLocation, this.annotations); 
/*      */     }
/*      */   }
/*      */   
/*      */   class StartState
/*      */     extends DefinitionState
/*      */   {
/*      */     StartState(GrammarSection section) {
/* 1196 */       super(section);
/*      */     }
/*      */     
/*      */     SchemaParser.State create() {
/* 1200 */       return new StartState(null);
/*      */     }
/*      */     
/*      */     void sendPatternToParent(ParsedPattern p) {
/* 1204 */       this.section.define("\000#start\000", this.combine, p, this.startLocation, this.annotations);
/*      */     }
/*      */     
/*      */     SchemaParser.State createChildState(String localName) throws SAXException {
/* 1208 */       SchemaParser.State tem = super.createChildState(localName);
/* 1209 */       if (tem != null && this.childPatterns != null)
/* 1210 */         SchemaParser.this.error("start_multi_pattern"); 
/* 1211 */       return tem;
/*      */     }
/*      */   }
/*      */   
/*      */   abstract class NameClassContainerState
/*      */     extends State {
/*      */     SchemaParser.State createChildState(String localName) throws SAXException {
/* 1218 */       SchemaParser.State state = (SchemaParser.State)SchemaParser.this.nameClassTable.get(localName);
/* 1219 */       if (state == null) {
/* 1220 */         SchemaParser.this.error("expected_name_class", localName);
/* 1221 */         return null;
/*      */       } 
/* 1223 */       return state.create();
/*      */     }
/*      */   }
/*      */   
/*      */   class NameClassChildState extends NameClassContainerState {
/*      */     final SchemaParser.State prevState;
/*      */     final SchemaParser.NameClassRef nameClassRef;
/*      */     
/*      */     SchemaParser.State create() {
/* 1232 */       return null;
/*      */     }
/*      */     
/*      */     NameClassChildState(SchemaParser.State prevState, SchemaParser.NameClassRef nameClassRef) {
/* 1236 */       this.prevState = prevState;
/* 1237 */       this.nameClassRef = nameClassRef;
/* 1238 */       setParent(prevState.parent);
/* 1239 */       this.ns = prevState.ns;
/*      */     }
/*      */     
/*      */     void endChild(ParsedNameClass nameClass) {
/* 1243 */       this.nameClassRef.setNameClass(nameClass);
/* 1244 */       this.prevState.set();
/*      */     }
/*      */     
/*      */     void endForeignChild(ParsedElementAnnotation ea) {
/* 1248 */       this.prevState.endForeignChild(ea);
/*      */     }
/*      */     
/*      */     void end() throws SAXException {
/* 1252 */       this.nameClassRef.setNameClass(SchemaParser.this.nameClassBuilder.makeErrorNameClass());
/* 1253 */       SchemaParser.this.error("missing_name_class");
/* 1254 */       this.prevState.set();
/* 1255 */       this.prevState.end();
/*      */     }
/*      */   }
/*      */   
/*      */   abstract class NameClassBaseState
/*      */     extends State {
/*      */     abstract ParsedNameClass makeNameClass() throws SAXException;
/*      */     
/*      */     void end() throws SAXException {
/* 1264 */       this.parent.endChild(makeNameClass());
/*      */     } }
/*      */   class NameState extends NameClassBaseState { final StringBuffer buf;
/*      */     
/*      */     NameState() {
/* 1269 */       this.buf = new StringBuffer();
/*      */     }
/*      */     SchemaParser.State createChildState(String localName) throws SAXException {
/* 1272 */       SchemaParser.this.error("expected_name", localName);
/* 1273 */       return null;
/*      */     }
/*      */     
/*      */     SchemaParser.State create() {
/* 1277 */       return new NameState();
/*      */     }
/*      */     
/*      */     public void characters(char[] ch, int start, int len) {
/* 1281 */       this.buf.append(ch, start, len);
/*      */     }
/*      */     
/*      */     void checkForeignElement() throws SAXException {
/* 1285 */       SchemaParser.this.error("name_contains_foreign_element");
/*      */     }
/*      */     
/*      */     ParsedNameClass makeNameClass() throws SAXException {
/* 1289 */       mergeLeadingComments();
/* 1290 */       return SchemaParser.this.expandName(this.buf.toString().trim(), getNs(), this.annotations);
/*      */     } }
/*      */ 
/*      */ 
/*      */   
/*      */   class AnyNameState
/*      */     extends NameClassBaseState
/*      */   {
/*      */     ParsedNameClass except;
/*      */     
/*      */     AnyNameState() {
/* 1301 */       this.except = null;
/*      */     }
/*      */     SchemaParser.State create() {
/* 1304 */       return new AnyNameState();
/*      */     }
/*      */     
/*      */     SchemaParser.State createChildState(String localName) throws SAXException {
/* 1308 */       if (localName.equals("except")) {
/* 1309 */         if (this.except != null)
/* 1310 */           SchemaParser.this.error("multiple_except"); 
/* 1311 */         return new SchemaParser.NameClassChoiceState(getContext());
/*      */       } 
/* 1313 */       SchemaParser.this.error("expected_except", localName);
/* 1314 */       return null;
/*      */     }
/*      */     
/*      */     int getContext() {
/* 1318 */       return 1;
/*      */     }
/*      */     
/*      */     ParsedNameClass makeNameClass() {
/* 1322 */       if (this.except == null) {
/* 1323 */         return makeNameClassNoExcept();
/*      */       }
/* 1325 */       return makeNameClassExcept(this.except);
/*      */     }
/*      */     
/*      */     ParsedNameClass makeNameClassNoExcept() {
/* 1329 */       return SchemaParser.this.nameClassBuilder.makeAnyName(this.startLocation, this.annotations);
/*      */     }
/*      */     
/*      */     ParsedNameClass makeNameClassExcept(ParsedNameClass except) {
/* 1333 */       return SchemaParser.this.nameClassBuilder.makeAnyName(except, this.startLocation, this.annotations);
/*      */     }
/*      */     
/*      */     void endChild(ParsedNameClass nameClass) {
/* 1337 */       this.except = nameClass;
/*      */     }
/*      */   }
/*      */   
/*      */   class NsNameState
/*      */     extends AnyNameState {
/*      */     SchemaParser.State create() {
/* 1344 */       return new NsNameState();
/*      */     }
/*      */     
/*      */     ParsedNameClass makeNameClassNoExcept() {
/* 1348 */       return SchemaParser.this.nameClassBuilder.makeNsName(getNs(), null, null);
/*      */     }
/*      */     
/*      */     ParsedNameClass makeNameClassExcept(ParsedNameClass except) {
/* 1352 */       return SchemaParser.this.nameClassBuilder.makeNsName(getNs(), except, null, null);
/*      */     }
/*      */     
/*      */     int getContext() {
/* 1356 */       return 2;
/*      */     }
/*      */   }
/*      */   
/*      */   class NameClassChoiceState
/*      */     extends NameClassContainerState {
/*      */     private ParsedNameClass[] nameClasses;
/*      */     private int nNameClasses;
/*      */     private int context;
/*      */     
/*      */     NameClassChoiceState() {
/* 1367 */       this.context = 0;
/*      */     }
/*      */     
/*      */     NameClassChoiceState(int context) {
/* 1371 */       this.context = context;
/*      */     }
/*      */     
/*      */     void setParent(SchemaParser.State parent) {
/* 1375 */       super.setParent(parent);
/* 1376 */       if (parent instanceof NameClassChoiceState)
/* 1377 */         this.context = ((NameClassChoiceState)parent).context; 
/*      */     }
/*      */     
/*      */     SchemaParser.State create() {
/* 1381 */       return new NameClassChoiceState();
/*      */     }
/*      */     
/*      */     SchemaParser.State createChildState(String localName) throws SAXException {
/* 1385 */       if (localName.equals("anyName")) {
/* 1386 */         if (this.context >= 1) {
/* 1387 */           SchemaParser.this.error((this.context == 1) ? "any_name_except_contains_any_name" : "ns_name_except_contains_any_name");
/*      */ 
/*      */           
/* 1390 */           return null;
/*      */         }
/*      */       
/* 1393 */       } else if (localName.equals("nsName") && 
/* 1394 */         this.context == 2) {
/* 1395 */         SchemaParser.this.error("ns_name_except_contains_ns_name");
/* 1396 */         return null;
/*      */       } 
/*      */       
/* 1399 */       return super.createChildState(localName);
/*      */     }
/*      */     
/*      */     void endChild(ParsedNameClass nc) {
/* 1403 */       if (this.nameClasses == null) {
/* 1404 */         this.nameClasses = new ParsedNameClass[5];
/* 1405 */       } else if (this.nNameClasses >= this.nameClasses.length) {
/* 1406 */         ParsedNameClass[] newNameClasses = new ParsedNameClass[this.nameClasses.length * 2];
/* 1407 */         System.arraycopy(this.nameClasses, 0, newNameClasses, 0, this.nameClasses.length);
/* 1408 */         this.nameClasses = newNameClasses;
/*      */       } 
/* 1410 */       this.nameClasses[this.nNameClasses++] = nc;
/*      */     }
/*      */     
/*      */     void endForeignChild(ParsedElementAnnotation ea) {
/* 1414 */       if (this.nNameClasses == 0) {
/* 1415 */         super.endForeignChild(ea);
/*      */       } else {
/* 1417 */         this.nameClasses[this.nNameClasses - 1] = SchemaParser.this.nameClassBuilder.annotateAfter(this.nameClasses[this.nNameClasses - 1], ea);
/*      */       } 
/*      */     }
/*      */     void end() throws SAXException {
/* 1421 */       if (this.nNameClasses == 0) {
/* 1422 */         SchemaParser.this.error("missing_name_class");
/* 1423 */         this.parent.endChild(SchemaParser.this.nameClassBuilder.makeErrorNameClass());
/*      */         return;
/*      */       } 
/* 1426 */       if (this.comments != null) {
/* 1427 */         this.nameClasses[this.nNameClasses - 1] = SchemaParser.this.nameClassBuilder.commentAfter(this.nameClasses[this.nNameClasses - 1], this.comments);
/* 1428 */         this.comments = null;
/*      */       } 
/* 1430 */       this.parent.endChild(SchemaParser.this.nameClassBuilder.makeChoice(Arrays.asList(this.nameClasses).subList(0, this.nNameClasses), this.startLocation, this.annotations));
/*      */     }
/*      */   }
/*      */   
/*      */   private void initPatternTable() {
/* 1435 */     this.patternTable = new Hashtable<Object, Object>();
/* 1436 */     this.patternTable.put("zeroOrMore", new ZeroOrMoreState());
/* 1437 */     this.patternTable.put("oneOrMore", new OneOrMoreState());
/* 1438 */     this.patternTable.put("optional", new OptionalState());
/* 1439 */     this.patternTable.put("list", new ListState());
/* 1440 */     this.patternTable.put("choice", new ChoiceState());
/* 1441 */     this.patternTable.put("interleave", new InterleaveState());
/* 1442 */     this.patternTable.put("group", new GroupState());
/* 1443 */     this.patternTable.put("mixed", new MixedState());
/* 1444 */     this.patternTable.put("element", new ElementState());
/* 1445 */     this.patternTable.put("attribute", new AttributeState());
/* 1446 */     this.patternTable.put("empty", new EmptyState());
/* 1447 */     this.patternTable.put("text", new TextState());
/* 1448 */     this.patternTable.put("value", new ValueState());
/* 1449 */     this.patternTable.put("data", new DataState());
/* 1450 */     this.patternTable.put("notAllowed", new NotAllowedState());
/* 1451 */     this.patternTable.put("grammar", new GrammarState());
/* 1452 */     this.patternTable.put("ref", new RefState());
/* 1453 */     this.patternTable.put("parentRef", new ParentRefState());
/* 1454 */     this.patternTable.put("externalRef", new ExternalRefState());
/*      */   }
/*      */   
/*      */   private void initNameClassTable() {
/* 1458 */     this.nameClassTable = new Hashtable<Object, Object>();
/* 1459 */     this.nameClassTable.put("name", new NameState());
/* 1460 */     this.nameClassTable.put("anyName", new AnyNameState());
/* 1461 */     this.nameClassTable.put("nsName", new NsNameState());
/* 1462 */     this.nameClassTable.put("choice", new NameClassChoiceState());
/*      */   }
/*      */   
/*      */   public ParsedPattern getParsedPattern() throws IllegalSchemaException {
/* 1466 */     if (this.hadError)
/* 1467 */       throw new IllegalSchemaException(); 
/* 1468 */     return this.startPattern;
/*      */   }
/*      */   
/*      */   private void error(String key) throws SAXException {
/* 1472 */     error(key, this.locator);
/*      */   }
/*      */   
/*      */   private void error(String key, String arg) throws SAXException {
/* 1476 */     error(key, arg, this.locator);
/*      */   }
/*      */   
/*      */   void error(String key, String arg1, String arg2) throws SAXException {
/* 1480 */     error(key, arg1, arg2, this.locator);
/*      */   }
/*      */   
/*      */   private void error(String key, Locator loc) throws SAXException {
/* 1484 */     error(new SAXParseException(localizer.message(key), loc));
/*      */   }
/*      */   
/*      */   private void error(String key, String arg, Locator loc) throws SAXException {
/* 1488 */     error(new SAXParseException(localizer.message(key, arg), loc));
/*      */   }
/*      */ 
/*      */   
/*      */   private void error(String key, String arg1, String arg2, Locator loc) throws SAXException {
/* 1493 */     error(new SAXParseException(localizer.message(key, arg1, arg2), loc));
/*      */   }
/*      */   
/*      */   private void error(SAXParseException e) throws SAXException {
/* 1497 */     this.hadError = true;
/* 1498 */     if (this.eh != null)
/* 1499 */       this.eh.error(e); 
/*      */   }
/*      */   
/*      */   void warning(String key) throws SAXException {
/* 1503 */     warning(key, this.locator);
/*      */   }
/*      */   
/*      */   private void warning(String key, String arg) throws SAXException {
/* 1507 */     warning(key, arg, this.locator);
/*      */   }
/*      */   
/*      */   private void warning(String key, String arg1, String arg2) throws SAXException {
/* 1511 */     warning(key, arg1, arg2, this.locator);
/*      */   }
/*      */   
/*      */   private void warning(String key, Locator loc) throws SAXException {
/* 1515 */     warning(new SAXParseException(localizer.message(key), loc));
/*      */   }
/*      */   
/*      */   private void warning(String key, String arg, Locator loc) throws SAXException {
/* 1519 */     warning(new SAXParseException(localizer.message(key, arg), loc));
/*      */   }
/*      */ 
/*      */   
/*      */   private void warning(String key, String arg1, String arg2, Locator loc) throws SAXException {
/* 1524 */     warning(new SAXParseException(localizer.message(key, arg1, arg2), loc));
/*      */   }
/*      */   
/*      */   private void warning(SAXParseException e) throws SAXException {
/* 1528 */     if (this.eh != null) {
/* 1529 */       this.eh.warning(e);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   SchemaParser(SAXParseable parseable, XMLReader xr, ErrorHandler eh, SchemaBuilder schemaBuilder, IncludedGrammar grammar, Scope scope, String inheritedNs) throws SAXException {
/* 1539 */     this.parseable = parseable;
/* 1540 */     this.xr = xr;
/* 1541 */     this.eh = eh;
/* 1542 */     this.schemaBuilder = schemaBuilder;
/* 1543 */     this.nameClassBuilder = schemaBuilder.getNameClassBuilder();
/* 1544 */     if (eh != null)
/* 1545 */       xr.setErrorHandler(eh); 
/* 1546 */     xr.setDTDHandler(this.context);
/* 1547 */     if (schemaBuilder.usesComments()) {
/*      */       try {
/* 1549 */         xr.setProperty("http://xml.org/sax/properties/lexical-handler", new LexicalHandlerImpl());
/*      */       }
/* 1551 */       catch (SAXNotRecognizedException e) {
/* 1552 */         warning("no_comment_support", xr.getClass().getName());
/*      */       }
/* 1554 */       catch (SAXNotSupportedException e) {
/* 1555 */         warning("no_comment_support", xr.getClass().getName());
/*      */       } 
/*      */     }
/* 1558 */     initPatternTable();
/* 1559 */     initNameClassTable();
/* 1560 */     (new RootState(grammar, scope, inheritedNs)).set();
/*      */   }
/*      */ 
/*      */   
/*      */   private Context getContext() {
/* 1565 */     return this.context;
/*      */   }
/*      */   
/*      */   class LexicalHandlerImpl extends AbstractLexicalHandler {
/*      */     private boolean inDtd = false;
/*      */     
/*      */     public void startDTD(String s, String s1, String s2) throws SAXException {
/* 1572 */       this.inDtd = true;
/*      */     }
/*      */     
/*      */     public void endDTD() throws SAXException {
/* 1576 */       this.inDtd = false;
/*      */     }
/*      */     
/*      */     public void comment(char[] chars, int start, int length) throws SAXException {
/* 1580 */       if (!this.inDtd)
/* 1581 */         ((SchemaParser.CommentHandler)SchemaParser.this.xr.getContentHandler()).comment(new String(chars, start, length)); 
/*      */     }
/*      */   }
/*      */   
/*      */   private ParsedNameClass expandName(String name, String ns, Annotations anno) throws SAXException {
/* 1586 */     int ic = name.indexOf(':');
/* 1587 */     if (ic == -1)
/* 1588 */       return this.nameClassBuilder.makeName(ns, checkNCName(name), null, null, anno); 
/* 1589 */     String prefix = checkNCName(name.substring(0, ic));
/* 1590 */     String localName = checkNCName(name.substring(ic + 1));
/* 1591 */     for (PrefixMapping tem = this.context.prefixMapping; tem != null; tem = tem.next) {
/* 1592 */       if (tem.prefix.equals(prefix))
/* 1593 */         return this.nameClassBuilder.makeName(tem.uri, localName, prefix, null, anno); 
/* 1594 */     }  error("undefined_prefix", prefix);
/* 1595 */     return this.nameClassBuilder.makeName("", localName, null, null, anno);
/*      */   }
/*      */   
/*      */   private String findPrefix(String qName, String uri) {
/* 1599 */     String prefix = null;
/* 1600 */     if (qName == null || qName.equals("")) {
/* 1601 */       for (PrefixMapping p = this.context.prefixMapping; p != null; p = p.next) {
/* 1602 */         if (p.uri.equals(uri)) {
/* 1603 */           prefix = p.prefix;
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } else {
/* 1608 */       int off = qName.indexOf(':');
/* 1609 */       if (off > 0)
/* 1610 */         prefix = qName.substring(0, off); 
/*      */     } 
/* 1612 */     return prefix;
/*      */   }
/*      */   private String checkNCName(String str) throws SAXException {
/* 1615 */     if (!Naming.isNcname(str))
/* 1616 */       error("invalid_ncname", str); 
/* 1617 */     return str;
/*      */   }
/*      */   
/*      */   private String resolve(String systemId) throws SAXException {
/* 1621 */     if (Uri.hasFragmentId(systemId))
/* 1622 */       error("href_fragment_id"); 
/* 1623 */     systemId = Uri.escapeDisallowedChars(systemId);
/* 1624 */     return Uri.resolve(this.xmlBaseHandler.getBaseUri(), systemId);
/*      */   }
/*      */   
/*      */   private Location makeLocation() {
/* 1628 */     if (this.locator == null)
/* 1629 */       return null; 
/* 1630 */     return this.schemaBuilder.makeLocation(this.locator.getSystemId(), this.locator.getLineNumber(), this.locator.getColumnNumber());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkUri(String s) throws SAXException {
/* 1636 */     if (!Uri.isValid(s))
/* 1637 */       error("invalid_uri", s); 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\xml\SchemaParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */