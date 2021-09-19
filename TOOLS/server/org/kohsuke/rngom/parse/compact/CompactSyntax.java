/*      */ package org.kohsuke.rngom.parse.compact;
/*      */ 
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.List;
/*      */ import java.util.Vector;
/*      */ import org.kohsuke.rngom.ast.builder.Annotations;
/*      */ import org.kohsuke.rngom.ast.builder.BuildException;
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
/*      */ import org.xml.sax.ErrorHandler;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.SAXParseException;
/*      */ import org.xml.sax.helpers.LocatorImpl;
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CompactSyntax
/*      */   implements Context, CompactSyntaxConstants
/*      */ {
/*      */   private static final int IN_ELEMENT = 0;
/*      */   private static final int IN_ATTRIBUTE = 1;
/*      */   private static final int IN_ANY_NAME = 2;
/*      */   private static final int IN_NS_NAME = 4;
/*      */   private String defaultNamespace;
/*   50 */   private String compatibilityPrefix = null;
/*      */   
/*      */   private SchemaBuilder sb;
/*      */   
/*      */   private NameClassBuilder ncb;
/*      */   
/*      */   private String sourceUri;
/*      */   private CompactParseable parseable;
/*      */   private ErrorHandler eh;
/*   59 */   private final Hashtable namespaceTable = new Hashtable<Object, Object>();
/*   60 */   private final Hashtable datatypesTable = new Hashtable<Object, Object>();
/*      */   private boolean hadError = false;
/*   62 */   private static final Localizer localizer = new Localizer(new Localizer(Parseable.class), CompactSyntax.class);
/*   63 */   private final Hashtable attributeNameTable = new Hashtable<Object, Object>();
/*      */ 
/*      */   
/*      */   private boolean annotationsIncludeElements = false;
/*      */   
/*      */   private String inheritedNs;
/*      */   
/*      */   private CommentList topLevelComments;
/*      */ 
/*      */   
/*      */   final class LocatedString
/*      */   {
/*      */     private final String str;
/*      */     
/*      */     private final Token tok;
/*      */ 
/*      */     
/*      */     LocatedString(String str, Token tok) {
/*   81 */       this.str = str;
/*   82 */       this.tok = tok;
/*      */     }
/*      */     
/*      */     String getString() {
/*   86 */       return this.str;
/*      */     }
/*      */     
/*      */     Location getLocation() {
/*   90 */       return CompactSyntax.this.makeLocation(this.tok);
/*      */     }
/*      */     
/*      */     Token getToken() {
/*   94 */       return this.tok;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public CompactSyntax(CompactParseable parseable, Reader r, String sourceUri, SchemaBuilder sb, ErrorHandler eh, String inheritedNs) {
/*  100 */     this(r);
/*  101 */     this.sourceUri = sourceUri;
/*  102 */     this.parseable = parseable;
/*  103 */     this.sb = sb;
/*  104 */     this.ncb = sb.getNameClassBuilder();
/*  105 */     this.eh = eh;
/*      */ 
/*      */     
/*  108 */     this.topLevelComments = sb.makeCommentList();
/*  109 */     this.inheritedNs = this.defaultNamespace = new String(inheritedNs);
/*      */   }
/*      */   
/*      */   ParsedPattern parse(Scope scope) throws IllegalSchemaException {
/*      */     try {
/*  114 */       ParsedPattern p = Input(scope);
/*  115 */       if (!this.hadError) {
/*  116 */         return p;
/*      */       }
/*  118 */     } catch (ParseException e) {
/*  119 */       error("syntax_error", e.getMessage(), e.currentToken.next);
/*      */     }
/*  121 */     catch (EscapeSyntaxException e) {
/*  122 */       reportEscapeSyntaxException(e);
/*      */     } 
/*  124 */     throw new IllegalSchemaException();
/*      */   }
/*      */   
/*      */   ParsedPattern parseInclude(IncludedGrammar g) throws IllegalSchemaException {
/*      */     try {
/*  129 */       ParsedPattern p = IncludedGrammar(g);
/*  130 */       if (!this.hadError) {
/*  131 */         return p;
/*      */       }
/*  133 */     } catch (ParseException e) {
/*  134 */       error("syntax_error", e.getMessage(), e.currentToken.next);
/*      */     }
/*  136 */     catch (EscapeSyntaxException e) {
/*  137 */       reportEscapeSyntaxException(e);
/*      */     } 
/*  139 */     throw new IllegalSchemaException();
/*      */   }
/*      */   
/*      */   private void checkNsName(int context, LocatedString ns) {
/*  143 */     if ((context & 0x4) != 0)
/*  144 */       error("ns_name_except_contains_ns_name", ns.getToken()); 
/*      */   }
/*      */   
/*      */   private void checkAnyName(int context, Token t) {
/*  148 */     if ((context & 0x4) != 0)
/*  149 */       error("ns_name_except_contains_any_name", t); 
/*  150 */     if ((context & 0x2) != 0)
/*  151 */       error("any_name_except_contains_any_name", t); 
/*      */   }
/*      */   
/*      */   private void error(String key, Token tok) {
/*  155 */     doError(localizer.message(key), tok);
/*      */   }
/*      */   
/*      */   private void error(String key, String arg, Token tok) {
/*  159 */     doError(localizer.message(key, arg), tok);
/*      */   }
/*      */   
/*      */   private void error(String key, String arg1, String arg2, Token tok) {
/*  163 */     doError(localizer.message(key, arg1, arg2), tok);
/*      */   }
/*      */   
/*      */   private void doError(String message, Token tok) {
/*  167 */     this.hadError = true;
/*  168 */     if (this.eh != null) {
/*  169 */       LocatorImpl loc = new LocatorImpl();
/*  170 */       loc.setLineNumber(tok.beginLine);
/*  171 */       loc.setColumnNumber(tok.beginColumn);
/*  172 */       loc.setSystemId(this.sourceUri);
/*      */       try {
/*  174 */         this.eh.error(new SAXParseException(message, loc));
/*      */       }
/*  176 */       catch (SAXException se) {
/*  177 */         throw new BuildException(se);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void reportEscapeSyntaxException(EscapeSyntaxException e) {
/*  183 */     if (this.eh != null) {
/*  184 */       LocatorImpl loc = new LocatorImpl();
/*  185 */       loc.setLineNumber(e.getLineNumber());
/*  186 */       loc.setColumnNumber(e.getColumnNumber());
/*  187 */       loc.setSystemId(this.sourceUri);
/*      */       try {
/*  189 */         this.eh.error(new SAXParseException(localizer.message(e.getKey()), loc));
/*      */       }
/*  191 */       catch (SAXException se) {
/*  192 */         throw new BuildException(se);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static String unquote(String s) {
/*  198 */     if (s.length() >= 6 && s.charAt(0) == s.charAt(1)) {
/*  199 */       s = s.replace(false, '\n');
/*  200 */       return s.substring(3, s.length() - 3);
/*      */     } 
/*      */     
/*  203 */     return s.substring(1, s.length() - 1);
/*      */   }
/*      */   
/*      */   Location makeLocation(Token t) {
/*  207 */     return this.sb.makeLocation(this.sourceUri, t.beginLine, t.beginColumn);
/*      */   }
/*      */   
/*      */   private static ParsedPattern[] addPattern(ParsedPattern[] patterns, int i, ParsedPattern p) {
/*  211 */     if (i >= patterns.length) {
/*  212 */       ParsedPattern[] oldPatterns = patterns;
/*  213 */       patterns = new ParsedPattern[oldPatterns.length * 2];
/*  214 */       System.arraycopy(oldPatterns, 0, patterns, 0, oldPatterns.length);
/*      */     } 
/*  216 */     patterns[i] = p;
/*  217 */     return patterns;
/*      */   }
/*      */   
/*      */   String getCompatibilityPrefix() {
/*  221 */     if (this.compatibilityPrefix == null) {
/*  222 */       this.compatibilityPrefix = "a";
/*  223 */       while (this.namespaceTable.get(this.compatibilityPrefix) != null)
/*  224 */         this.compatibilityPrefix += "a"; 
/*      */     } 
/*  226 */     return this.compatibilityPrefix;
/*      */   }
/*      */   
/*      */   public String resolveNamespacePrefix(String prefix) {
/*  230 */     String result = (String)this.namespaceTable.get(prefix);
/*  231 */     if (result.length() == 0)
/*  232 */       return null; 
/*  233 */     return result;
/*      */   }
/*      */   
/*      */   public Enumeration prefixes() {
/*  237 */     return this.namespaceTable.keys();
/*      */   }
/*      */   
/*      */   public String getBaseUri() {
/*  241 */     return this.sourceUri;
/*      */   }
/*      */   
/*      */   public boolean isUnparsedEntity(String entityName) {
/*  245 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isNotation(String notationName) {
/*  249 */     return false;
/*      */   }
/*      */   
/*      */   public Context copy() {
/*  253 */     return this;
/*      */   }
/*      */   
/*      */   private Context getContext() {
/*  257 */     return this;
/*      */   }
/*      */   
/*      */   private CommentList getComments() {
/*  261 */     return getComments(getTopLevelComments());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private CommentList getTopLevelComments() {
/*  267 */     CommentList tem = this.topLevelComments;
/*  268 */     this.topLevelComments = null;
/*  269 */     return tem;
/*      */   }
/*      */   
/*      */   private void noteTopLevelComments() {
/*  273 */     this.topLevelComments = getComments(this.topLevelComments);
/*      */   }
/*      */   
/*      */   private void topLevelComments(GrammarSection section) {
/*  277 */     section.topLevelComment(getComments(null));
/*      */   }
/*      */   
/*  280 */   private Token lastCommentSourceToken = null; public CompactSyntaxTokenManager token_source; JavaCharStream jj_input_stream; public Token token; public Token jj_nt; private int jj_ntk; private Token jj_scanpos; private Token jj_lastpos; private int jj_la;
/*      */   
/*      */   private CommentList getComments(CommentList comments) {
/*  283 */     Token nextToken = getToken(1);
/*  284 */     if (this.lastCommentSourceToken != nextToken) {
/*  285 */       if (this.lastCommentSourceToken == null)
/*  286 */         this.lastCommentSourceToken = this.token; 
/*      */       do {
/*  288 */         this.lastCommentSourceToken = this.lastCommentSourceToken.next;
/*  289 */         Token t = this.lastCommentSourceToken.specialToken;
/*  290 */         if (t == null)
/*  291 */           continue;  while (t.specialToken != null)
/*  292 */           t = t.specialToken; 
/*  293 */         if (comments == null)
/*  294 */           comments = this.sb.makeCommentList(); 
/*  295 */         for (; t != null; t = t.next) {
/*  296 */           String s = mungeComment(t.image);
/*  297 */           Location loc = makeLocation(t);
/*  298 */           if (t.next != null && t.next.kind == 44) {
/*      */             
/*  300 */             StringBuffer buf = new StringBuffer(s);
/*      */             do {
/*  302 */               t = t.next;
/*  303 */               buf.append('\n');
/*  304 */               buf.append(mungeComment(t.image));
/*      */             }
/*  306 */             while (t.next != null && t.next.kind == 44);
/*  307 */             s = buf.toString();
/*      */           } 
/*  309 */           comments.addComment(s, loc);
/*      */         }
/*      */       
/*  312 */       } while (this.lastCommentSourceToken != nextToken);
/*      */     } 
/*  314 */     return comments;
/*      */   }
/*      */   
/*      */   private ParsedPattern afterComments(ParsedPattern p) {
/*  318 */     CommentList comments = getComments(null);
/*  319 */     if (comments == null)
/*  320 */       return p; 
/*  321 */     return this.sb.commentAfter(p, comments);
/*      */   }
/*      */   
/*      */   private ParsedNameClass afterComments(ParsedNameClass nc) {
/*  325 */     CommentList comments = getComments(null);
/*  326 */     if (comments == null)
/*  327 */       return nc; 
/*  328 */     return this.ncb.commentAfter(nc, comments);
/*      */   }
/*      */   
/*      */   private static String mungeComment(String image) {
/*  332 */     int i = image.indexOf('#') + 1;
/*  333 */     while (i < image.length() && image.charAt(i) == '#')
/*  334 */       i++; 
/*  335 */     if (i < image.length() && image.charAt(i) == ' ')
/*  336 */       i++; 
/*  337 */     return image.substring(i);
/*      */   }
/*      */   
/*      */   private Annotations getCommentsAsAnnotations() {
/*  341 */     CommentList comments = getComments();
/*  342 */     if (comments == null)
/*  343 */       return null; 
/*  344 */     return this.sb.makeAnnotations(comments, getContext());
/*      */   }
/*      */   
/*      */   private Annotations addCommentsToChildAnnotations(Annotations a) {
/*  348 */     CommentList comments = getComments();
/*  349 */     if (comments == null)
/*  350 */       return a; 
/*  351 */     if (a == null)
/*  352 */       a = this.sb.makeAnnotations(null, getContext()); 
/*  353 */     a.addComment(comments);
/*  354 */     return a;
/*      */   }
/*      */   
/*      */   private Annotations addCommentsToLeadingAnnotations(Annotations a) {
/*  358 */     CommentList comments = getComments();
/*  359 */     if (comments == null)
/*  360 */       return a; 
/*  361 */     if (a == null)
/*  362 */       return this.sb.makeAnnotations(comments, getContext()); 
/*  363 */     a.addLeadingComment(comments);
/*  364 */     return a;
/*      */   }
/*      */   
/*      */   private Annotations getTopLevelCommentsAsAnnotations() {
/*  368 */     CommentList comments = getTopLevelComments();
/*  369 */     if (comments == null)
/*  370 */       return null; 
/*  371 */     return this.sb.makeAnnotations(comments, getContext());
/*      */   }
/*      */   
/*      */   private void clearAttributeList() {
/*  375 */     this.attributeNameTable.clear();
/*      */   }
/*      */   
/*      */   private void addAttribute(Annotations a, String ns, String localName, String prefix, String value, Token tok) {
/*  379 */     String key = ns + "#" + localName;
/*  380 */     if (this.attributeNameTable.get(key) != null) {
/*  381 */       error("duplicate_attribute", ns, localName, tok);
/*      */     } else {
/*  383 */       this.attributeNameTable.put(key, key);
/*  384 */       a.addAttribute(ns, localName, prefix, value, makeLocation(tok));
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkExcept(Token[] except) {
/*  389 */     if (except[0] != null)
/*  390 */       error("except_missing_parentheses", except[0]); 
/*      */   }
/*      */   
/*      */   private String lookupPrefix(String prefix, Token t) {
/*  394 */     String ns = (String)this.namespaceTable.get(prefix);
/*  395 */     if (ns == null) {
/*  396 */       error("undeclared_prefix", prefix, t);
/*  397 */       return "#error";
/*      */     } 
/*  399 */     return ns;
/*      */   }
/*      */   private String lookupDatatype(String prefix, Token t) {
/*  402 */     String ns = (String)this.datatypesTable.get(prefix);
/*  403 */     if (ns == null) {
/*  404 */       error("undeclared_prefix", prefix, t);
/*  405 */       return "";
/*      */     } 
/*  407 */     return ns;
/*      */   }
/*      */   private String resolve(String str) {
/*      */     try {
/*  411 */       return (new URL(new URL(this.sourceUri), str)).toString();
/*      */     }
/*  413 */     catch (MalformedURLException e) {
/*  414 */       return str;
/*      */     } 
/*      */   }
/*      */   public final ParsedPattern Input(Scope scope) throws ParseException {
/*      */     ParsedPattern p;
/*  419 */     Preamble();
/*  420 */     if (jj_2_1(2147483647))
/*  421 */     { p = TopLevelGrammar(scope); }
/*      */     else
/*  423 */     { switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk)
/*      */       { case 1:
/*      */         case 10:
/*      */         case 17:
/*      */         case 18:
/*      */         case 19:
/*      */         case 26:
/*      */         case 27:
/*      */         case 28:
/*      */         case 31:
/*      */         case 32:
/*      */         case 33:
/*      */         case 34:
/*      */         case 35:
/*      */         case 36:
/*      */         case 40:
/*      */         case 43:
/*      */         case 54:
/*      */         case 55:
/*      */         case 57:
/*      */         case 58:
/*  444 */           p = Expr(true, scope, null, null);
/*  445 */           p = afterComments(p);
/*  446 */           jj_consume_token(0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  454 */           return p; }  this.jj_la1[0] = this.jj_gen; jj_consume_token(-1); throw new ParseException(); }  return p;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void TopLevelLookahead() throws ParseException {
/*  459 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 57:
/*  461 */         jj_consume_token(57);
/*  462 */         jj_consume_token(1);
/*      */         return;
/*      */       case 54:
/*      */       case 55:
/*  466 */         Identifier();
/*  467 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 1:
/*  469 */             jj_consume_token(1);
/*      */             return;
/*      */           case 2:
/*  472 */             jj_consume_token(2);
/*      */             return;
/*      */           case 3:
/*  475 */             jj_consume_token(3);
/*      */             return;
/*      */           case 4:
/*  478 */             jj_consume_token(4);
/*      */             return;
/*      */         } 
/*  481 */         this.jj_la1[1] = this.jj_gen;
/*  482 */         jj_consume_token(-1);
/*  483 */         throw new ParseException();
/*      */ 
/*      */       
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*  489 */         LookaheadGrammarKeyword();
/*      */         return;
/*      */       case 1:
/*  492 */         LookaheadBody();
/*  493 */         LookaheadAfterAnnotations();
/*      */         return;
/*      */       case 40:
/*      */       case 43:
/*  497 */         LookaheadDocumentation();
/*  498 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 1:
/*  500 */             LookaheadBody();
/*      */             break;
/*      */           default:
/*  503 */             this.jj_la1[2] = this.jj_gen;
/*      */             break;
/*      */         } 
/*  506 */         LookaheadAfterAnnotations();
/*      */         return;
/*      */     } 
/*  509 */     this.jj_la1[3] = this.jj_gen;
/*  510 */     jj_consume_token(-1);
/*  511 */     throw new ParseException();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void LookaheadAfterAnnotations() throws ParseException {
/*  516 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 54:
/*      */       case 55:
/*  519 */         Identifier();
/*  520 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 2:
/*  522 */             jj_consume_token(2);
/*      */             return;
/*      */           case 3:
/*  525 */             jj_consume_token(3);
/*      */             return;
/*      */           case 4:
/*  528 */             jj_consume_token(4);
/*      */             return;
/*      */         } 
/*  531 */         this.jj_la1[4] = this.jj_gen;
/*  532 */         jj_consume_token(-1);
/*  533 */         throw new ParseException();
/*      */ 
/*      */       
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*  539 */         LookaheadGrammarKeyword();
/*      */         return;
/*      */     } 
/*  542 */     this.jj_la1[5] = this.jj_gen;
/*  543 */     jj_consume_token(-1);
/*  544 */     throw new ParseException();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void LookaheadGrammarKeyword() throws ParseException {
/*  549 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 5:
/*  551 */         jj_consume_token(5);
/*      */         return;
/*      */       case 6:
/*  554 */         jj_consume_token(6);
/*      */         return;
/*      */       case 7:
/*  557 */         jj_consume_token(7);
/*      */         return;
/*      */     } 
/*  560 */     this.jj_la1[6] = this.jj_gen;
/*  561 */     jj_consume_token(-1);
/*  562 */     throw new ParseException();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void LookaheadDocumentation() throws ParseException {
/*      */     while (true) {
/*  569 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 40:
/*  571 */           jj_consume_token(40);
/*      */           break;
/*      */         case 43:
/*  574 */           jj_consume_token(43);
/*      */           break;
/*      */         default:
/*  577 */           this.jj_la1[7] = this.jj_gen;
/*  578 */           jj_consume_token(-1);
/*  579 */           throw new ParseException();
/*      */       } 
/*      */       
/*      */       while (true) {
/*  583 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 41:
/*      */             break;
/*      */           
/*      */           default:
/*  588 */             this.jj_la1[8] = this.jj_gen;
/*      */             break;
/*      */         } 
/*  591 */         jj_consume_token(41);
/*      */       } 
/*  593 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 40:
/*      */         case 43:
/*      */           continue;
/*      */       }  break;
/*      */     } 
/*  599 */     this.jj_la1[9] = this.jj_gen;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void LookaheadBody() throws ParseException {
/*  606 */     jj_consume_token(1);
/*      */     
/*      */     while (true) {
/*  609 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 1:
/*      */         case 2:
/*      */         case 5:
/*      */         case 6:
/*      */         case 7:
/*      */         case 8:
/*      */         case 10:
/*      */         case 13:
/*      */         case 14:
/*      */         case 15:
/*      */         case 16:
/*      */         case 17:
/*      */         case 18:
/*      */         case 19:
/*      */         case 26:
/*      */         case 27:
/*      */         case 31:
/*      */         case 32:
/*      */         case 33:
/*      */         case 34:
/*      */         case 35:
/*      */         case 36:
/*      */         case 54:
/*      */         case 55:
/*      */         case 57:
/*      */         case 58:
/*      */           break;
/*      */         
/*      */         default:
/*  639 */           this.jj_la1[10] = this.jj_gen;
/*      */           break;
/*      */       } 
/*  642 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 57:
/*  644 */           jj_consume_token(57);
/*      */           continue;
/*      */         case 5:
/*      */         case 6:
/*      */         case 7:
/*      */         case 10:
/*      */         case 13:
/*      */         case 14:
/*      */         case 15:
/*      */         case 16:
/*      */         case 17:
/*      */         case 18:
/*      */         case 19:
/*      */         case 26:
/*      */         case 27:
/*      */         case 31:
/*      */         case 32:
/*      */         case 33:
/*      */         case 34:
/*      */         case 35:
/*      */         case 36:
/*      */         case 54:
/*      */         case 55:
/*  667 */           UnprefixedName();
/*      */           continue;
/*      */         case 2:
/*  670 */           jj_consume_token(2);
/*      */           continue;
/*      */         case 58:
/*  673 */           jj_consume_token(58);
/*      */           continue;
/*      */         case 8:
/*  676 */           jj_consume_token(8);
/*      */           continue;
/*      */         case 1:
/*  679 */           LookaheadBody();
/*      */           continue;
/*      */       } 
/*  682 */       this.jj_la1[11] = this.jj_gen;
/*  683 */       jj_consume_token(-1);
/*  684 */       throw new ParseException();
/*      */     } 
/*      */     
/*  687 */     jj_consume_token(9);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ParsedPattern IncludedGrammar(IncludedGrammar g) throws ParseException {
/*      */     Annotations a;
/*  693 */     Preamble();
/*  694 */     if (jj_2_2(2147483647))
/*  695 */     { a = GrammarBody((GrammarSection)g, (Scope)g, getTopLevelCommentsAsAnnotations()); }
/*      */     else
/*  697 */     { ParsedPattern p; switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk)
/*      */       { case 1:
/*      */         case 10:
/*      */         case 40:
/*      */         case 43:
/*  702 */           a = Annotations();
/*  703 */           jj_consume_token(10);
/*  704 */           jj_consume_token(11);
/*  705 */           a = GrammarBody((GrammarSection)g, (Scope)g, a);
/*  706 */           topLevelComments((GrammarSection)g);
/*  707 */           jj_consume_token(12);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  715 */           p = afterComments(g.endIncludedGrammar(this.sb.makeLocation(this.sourceUri, 1, 1), a));
/*  716 */           jj_consume_token(0);
/*  717 */           return p; }  this.jj_la1[12] = this.jj_gen; jj_consume_token(-1); throw new ParseException(); }  ParsedPattern parsedPattern = afterComments(g.endIncludedGrammar(this.sb.makeLocation(this.sourceUri, 1, 1), a)); jj_consume_token(0); return parsedPattern;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ParsedPattern TopLevelGrammar(Scope scope) throws ParseException {
/*  722 */     Annotations a = getTopLevelCommentsAsAnnotations();
/*      */ 
/*      */     
/*  725 */     Grammar g = this.sb.makeGrammar(scope);
/*  726 */     a = GrammarBody((GrammarSection)g, (Scope)g, a);
/*  727 */     ParsedPattern p = afterComments(g.endGrammar(this.sb.makeLocation(this.sourceUri, 1, 1), a));
/*  728 */     jj_consume_token(0);
/*  729 */     return p;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void Preamble() throws ParseException {
/*      */     while (true) {
/*  736 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 13:
/*      */         case 14:
/*      */         case 16:
/*      */           break;
/*      */         
/*      */         default:
/*  743 */           this.jj_la1[13] = this.jj_gen;
/*      */           break;
/*      */       } 
/*  746 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 13:
/*      */         case 14:
/*  749 */           NamespaceDecl();
/*      */           continue;
/*      */         case 16:
/*  752 */           DatatypesDecl();
/*      */           continue;
/*      */       } 
/*  755 */       this.jj_la1[14] = this.jj_gen;
/*  756 */       jj_consume_token(-1);
/*  757 */       throw new ParseException();
/*      */     } 
/*      */     
/*  760 */     this.namespaceTable.put("xml", "http://www.w3.org/XML/1998/namespace");
/*  761 */     if (this.datatypesTable.get("xsd") == null)
/*  762 */       this.datatypesTable.put("xsd", "http://www.w3.org/2001/XMLSchema-datatypes"); 
/*      */   }
/*      */   
/*      */   public final void NamespaceDecl() throws ParseException {
/*  766 */     LocatedString prefix = null;
/*  767 */     boolean isDefault = false;
/*      */     
/*  769 */     noteTopLevelComments();
/*  770 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 13:
/*  772 */         jj_consume_token(13);
/*  773 */         prefix = UnprefixedName();
/*      */         break;
/*      */       case 14:
/*  776 */         jj_consume_token(14);
/*  777 */         isDefault = true;
/*  778 */         jj_consume_token(13);
/*  779 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 5:
/*      */           case 6:
/*      */           case 7:
/*      */           case 10:
/*      */           case 13:
/*      */           case 14:
/*      */           case 15:
/*      */           case 16:
/*      */           case 17:
/*      */           case 18:
/*      */           case 19:
/*      */           case 26:
/*      */           case 27:
/*      */           case 31:
/*      */           case 32:
/*      */           case 33:
/*      */           case 34:
/*      */           case 35:
/*      */           case 36:
/*      */           case 54:
/*      */           case 55:
/*  801 */             prefix = UnprefixedName();
/*      */             break;
/*      */         } 
/*  804 */         this.jj_la1[15] = this.jj_gen;
/*      */         break;
/*      */ 
/*      */       
/*      */       default:
/*  809 */         this.jj_la1[16] = this.jj_gen;
/*  810 */         jj_consume_token(-1);
/*  811 */         throw new ParseException();
/*      */     } 
/*  813 */     jj_consume_token(2);
/*  814 */     String namespaceName = NamespaceName();
/*  815 */     if (isDefault)
/*  816 */       this.defaultNamespace = namespaceName; 
/*  817 */     if (prefix != null) {
/*  818 */       if (prefix.getString().equals("xmlns")) {
/*  819 */         error("xmlns_prefix", prefix.getToken());
/*  820 */       } else if (prefix.getString().equals("xml")) {
/*  821 */         if (!namespaceName.equals("http://www.w3.org/XML/1998/namespace")) {
/*  822 */           error("xml_prefix_bad_uri", prefix.getToken());
/*      */         }
/*  824 */       } else if (namespaceName.equals("http://www.w3.org/XML/1998/namespace")) {
/*  825 */         error("xml_uri_bad_prefix", prefix.getToken());
/*      */       } else {
/*  827 */         if (namespaceName.equals("http://relaxng.org/ns/compatibility/annotations/1.0"))
/*  828 */           this.compatibilityPrefix = prefix.getString(); 
/*  829 */         this.namespaceTable.put(prefix.getString(), namespaceName);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public final String NamespaceName() throws ParseException {
/*      */     String r;
/*  836 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 58:
/*  838 */         r = Literal();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  849 */         return r;case 15: jj_consume_token(15); r = this.inheritedNs; return r;
/*      */     } 
/*      */     this.jj_la1[17] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException();
/*      */   }
/*      */   public final void DatatypesDecl() throws ParseException {
/*  856 */     noteTopLevelComments();
/*  857 */     jj_consume_token(16);
/*  858 */     LocatedString prefix = UnprefixedName();
/*  859 */     jj_consume_token(2);
/*  860 */     String uri = Literal();
/*  861 */     this.datatypesTable.put(prefix.getString(), uri);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern AnnotatedPrimaryExpr(boolean topLevel, Scope scope, Token[] except) throws ParseException {
/*  869 */     Annotations a = Annotations();
/*  870 */     ParsedPattern p = PrimaryExpr(topLevel, scope, a, except);
/*      */     
/*      */     while (true) {
/*  873 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 59:
/*      */           break;
/*      */         
/*      */         default:
/*  878 */           this.jj_la1[18] = this.jj_gen;
/*      */           break;
/*      */       } 
/*  881 */       Token t = jj_consume_token(59);
/*  882 */       ParsedElementAnnotation e = AnnotationElement(false);
/*  883 */       if (topLevel) {
/*  884 */         error("top_level_follow_annotation", t); continue;
/*      */       } 
/*  886 */       p = this.sb.annotateAfter(p, e);
/*      */     } 
/*  888 */     return p;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ParsedPattern PrimaryExpr(boolean topLevel, Scope scope, Annotations a, Token[] except) throws ParseException {
/*      */     ParsedPattern p;
/*  894 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 26:
/*  896 */         p = ElementExpr(scope, a);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  945 */         return p;case 27: p = AttributeExpr(scope, a); return p;case 10: p = GrammarExpr(scope, a); return p;case 33: p = ExternalRefExpr(scope, a); return p;case 31: p = ListExpr(scope, a); return p;case 32: p = MixedExpr(scope, a); return p;case 28: p = ParenExpr(topLevel, scope, a); return p;case 54: case 55: p = IdentifierExpr(scope, a); return p;case 34: p = ParentExpr(scope, a); return p;case 35: case 36: case 57: p = DataExpr(topLevel, scope, a, except); return p;case 58: p = ValueExpr(topLevel, a); return p;case 18: p = TextExpr(a); return p;case 17: p = EmptyExpr(a); return p;case 19: p = NotAllowedExpr(a); return p;
/*      */     } 
/*      */     this.jj_la1[19] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException();
/*      */   } public final ParsedPattern EmptyExpr(Annotations a) throws ParseException {
/*  951 */     Token t = jj_consume_token(17);
/*  952 */     return this.sb.makeEmpty(makeLocation(t), a);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern TextExpr(Annotations a) throws ParseException {
/*  958 */     Token t = jj_consume_token(18);
/*  959 */     return this.sb.makeText(makeLocation(t), a);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern NotAllowedExpr(Annotations a) throws ParseException {
/*  965 */     Token t = jj_consume_token(19);
/*  966 */     return this.sb.makeNotAllowed(makeLocation(t), a);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ParsedPattern Expr(boolean topLevel, Scope scope, Token t, Annotations a) throws ParseException {
/*  971 */     List<ParsedPattern> patterns = new ArrayList();
/*      */     
/*  973 */     boolean[] hadOccur = new boolean[1];
/*  974 */     Token[] except = new Token[1];
/*  975 */     ParsedPattern p = UnaryExpr(topLevel, scope, hadOccur, except);
/*  976 */     patterns.add(p);
/*  977 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 20:
/*      */       case 21:
/*      */       case 22:
/*  981 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 20:
/*  983 */             checkExcept(except);
/*      */             
/*      */             while (true) {
/*  986 */               t = jj_consume_token(20);
/*  987 */               p = UnaryExpr(topLevel, scope, null, except);
/*  988 */               patterns.add(p); checkExcept(except);
/*  989 */               switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */                 case 20:
/*      */                   continue;
/*      */               }  break;
/*      */             } 
/*  994 */             this.jj_la1[20] = this.jj_gen;
/*      */ 
/*      */ 
/*      */             
/*  998 */             p = this.sb.makeChoice(patterns, makeLocation(t), a);
/*      */             break;
/*      */           
/*      */           case 21:
/*      */             while (true) {
/* 1003 */               t = jj_consume_token(21);
/* 1004 */               p = UnaryExpr(topLevel, scope, null, except);
/* 1005 */               patterns.add(p); checkExcept(except);
/* 1006 */               switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */                 case 21:
/*      */                   continue;
/*      */               }  break;
/*      */             } 
/* 1011 */             this.jj_la1[21] = this.jj_gen;
/*      */ 
/*      */ 
/*      */             
/* 1015 */             p = this.sb.makeInterleave(patterns, makeLocation(t), a);
/*      */             break;
/*      */           
/*      */           case 22:
/*      */             while (true) {
/* 1020 */               t = jj_consume_token(22);
/* 1021 */               p = UnaryExpr(topLevel, scope, null, except);
/* 1022 */               patterns.add(p); checkExcept(except);
/* 1023 */               switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */                 case 22:
/*      */                   continue;
/*      */               }  break;
/*      */             } 
/* 1028 */             this.jj_la1[22] = this.jj_gen;
/*      */ 
/*      */ 
/*      */             
/* 1032 */             p = this.sb.makeGroup(patterns, makeLocation(t), a);
/*      */             break;
/*      */         } 
/* 1035 */         this.jj_la1[23] = this.jj_gen;
/* 1036 */         jj_consume_token(-1);
/* 1037 */         throw new ParseException();
/*      */ 
/*      */       
/*      */       default:
/* 1041 */         this.jj_la1[24] = this.jj_gen;
/*      */         break;
/*      */     } 
/* 1044 */     if (patterns.size() == 1 && a != null)
/* 1045 */       if (hadOccur[0]) {
/* 1046 */         p = this.sb.annotate(p, a);
/*      */       } else {
/* 1048 */         p = this.sb.makeGroup(patterns, makeLocation(t), a);
/*      */       }  
/* 1050 */     return p;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern UnaryExpr(boolean topLevel, Scope scope, boolean[] hadOccur, Token[] except) throws ParseException {
/*      */     Token t;
/* 1058 */     ParsedPattern p = AnnotatedPrimaryExpr(topLevel, scope, except);
/* 1059 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk)
/*      */     { case 23:
/*      */       case 24:
/*      */       case 25:
/* 1063 */         if (hadOccur != null) hadOccur[0] = true; 
/* 1064 */         p = afterComments(p);
/* 1065 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 23:
/* 1067 */             t = jj_consume_token(23);
/* 1068 */             checkExcept(except); p = this.sb.makeOneOrMore(p, makeLocation(t), null);
/*      */             break;
/*      */           case 24:
/* 1071 */             t = jj_consume_token(24);
/* 1072 */             checkExcept(except); p = this.sb.makeOptional(p, makeLocation(t), null);
/*      */             break;
/*      */           case 25:
/* 1075 */             t = jj_consume_token(25);
/* 1076 */             checkExcept(except); p = this.sb.makeZeroOrMore(p, makeLocation(t), null);
/*      */             break;
/*      */           default:
/* 1079 */             this.jj_la1[25] = this.jj_gen;
/* 1080 */             jj_consume_token(-1);
/* 1081 */             throw new ParseException();
/*      */         } 
/*      */         
/*      */         while (true) {
/* 1085 */           switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */             case 59:
/*      */               break;
/*      */             
/*      */             default:
/* 1090 */               this.jj_la1[26] = this.jj_gen;
/*      */               break;
/*      */           } 
/* 1093 */           t = jj_consume_token(59);
/* 1094 */           ParsedElementAnnotation e = AnnotationElement(false);
/* 1095 */           if (topLevel) {
/* 1096 */             error("top_level_follow_annotation", t); continue;
/*      */           } 
/* 1098 */           p = this.sb.annotateAfter(p, e);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1105 */         return p; }  this.jj_la1[27] = this.jj_gen; return p;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern ElementExpr(Scope scope, Annotations a) throws ParseException {
/* 1113 */     Token t = jj_consume_token(26);
/* 1114 */     ParsedNameClass nc = NameClass(0, null);
/* 1115 */     jj_consume_token(11);
/* 1116 */     ParsedPattern p = Expr(false, scope, null, null);
/* 1117 */     p = afterComments(p);
/* 1118 */     jj_consume_token(12);
/* 1119 */     return this.sb.makeElement(nc, p, makeLocation(t), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern AttributeExpr(Scope scope, Annotations a) throws ParseException {
/* 1127 */     Token t = jj_consume_token(27);
/* 1128 */     ParsedNameClass nc = NameClass(1, null);
/* 1129 */     jj_consume_token(11);
/* 1130 */     ParsedPattern p = Expr(false, scope, null, null);
/* 1131 */     p = afterComments(p);
/* 1132 */     jj_consume_token(12);
/* 1133 */     return this.sb.makeAttribute(nc, p, makeLocation(t), a);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedNameClass NameClass(int context, Annotations[] pa) throws ParseException {
/*      */     ParsedNameClass nc;
/* 1140 */     Annotations a = Annotations();
/* 1141 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 10:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 19:
/*      */       case 26:
/*      */       case 27:
/*      */       case 28:
/*      */       case 31:
/*      */       case 32:
/*      */       case 33:
/*      */       case 34:
/*      */       case 35:
/*      */       case 36:
/*      */       case 54:
/*      */       case 55:
/*      */       case 57:
/* 1165 */         nc = PrimaryNameClass(context, a);
/* 1166 */         nc = AnnotateAfter(nc);
/* 1167 */         nc = NameClassAlternatives(context, nc, pa);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1180 */         return nc;case 25: nc = AnyNameExceptClass(context, a, pa); return nc;case 56: nc = NsNameExceptClass(context, a, pa); return nc;
/*      */     } 
/*      */     this.jj_la1[28] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException();
/*      */   }
/*      */   public final ParsedNameClass AnnotateAfter(ParsedNameClass nc) throws ParseException {
/*      */     while (true) {
/* 1188 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 59:
/*      */           break;
/*      */         
/*      */         default:
/* 1193 */           this.jj_la1[29] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 1196 */       jj_consume_token(59);
/* 1197 */       ParsedElementAnnotation e = AnnotationElement(false);
/* 1198 */       nc = this.ncb.annotateAfter(nc, e);
/*      */     } 
/* 1200 */     return nc;
/*      */   }
/*      */   
/*      */   public final ParsedNameClass NameClassAlternatives(int context, ParsedNameClass nc, Annotations[] pa) throws ParseException {
/*      */     Token t;
/*      */     ParsedNameClass[] nameClasses;
/*      */     int nNameClasses;
/*      */     Annotations a;
/* 1208 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk)
/*      */     { case 20:
/* 1210 */         nameClasses = new ParsedNameClass[2];
/* 1211 */         nameClasses[0] = nc;
/* 1212 */         nNameClasses = 1;
/*      */         
/*      */         while (true) {
/* 1215 */           t = jj_consume_token(20);
/* 1216 */           nc = BasicNameClass(context);
/* 1217 */           nc = AnnotateAfter(nc);
/* 1218 */           if (nNameClasses >= nameClasses.length) {
/* 1219 */             ParsedNameClass[] oldNameClasses = nameClasses;
/* 1220 */             nameClasses = new ParsedNameClass[oldNameClasses.length * 2];
/* 1221 */             System.arraycopy(oldNameClasses, 0, nameClasses, 0, oldNameClasses.length);
/*      */           } 
/* 1223 */           nameClasses[nNameClasses++] = nc;
/* 1224 */           switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */             case 20:
/*      */               continue;
/*      */           }  break;
/*      */         } 
/* 1229 */         this.jj_la1[30] = this.jj_gen;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1234 */         if (pa == null) {
/* 1235 */           a = null;
/*      */         } else {
/* 1237 */           a = pa[0];
/* 1238 */           pa[0] = null;
/*      */         } 
/* 1240 */         nc = this.ncb.makeChoice(Arrays.<ParsedNameClass>asList(nameClasses).subList(0, nNameClasses), makeLocation(t), a);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1246 */         return nc; }  this.jj_la1[31] = this.jj_gen; return nc;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedNameClass BasicNameClass(int context) throws ParseException {
/*      */     ParsedNameClass nc;
/* 1253 */     Annotations a = Annotations();
/* 1254 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 10:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 19:
/*      */       case 26:
/*      */       case 27:
/*      */       case 28:
/*      */       case 31:
/*      */       case 32:
/*      */       case 33:
/*      */       case 34:
/*      */       case 35:
/*      */       case 36:
/*      */       case 54:
/*      */       case 55:
/*      */       case 57:
/* 1278 */         nc = PrimaryNameClass(context, a);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1289 */         return nc;case 25: case 56: nc = OpenNameClass(context, a); return nc;
/*      */     } 
/*      */     this.jj_la1[32] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException();
/*      */   } public final ParsedNameClass PrimaryNameClass(int context, Annotations a) throws ParseException { ParsedNameClass nc;
/* 1295 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 10:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 19:
/*      */       case 26:
/*      */       case 27:
/*      */       case 31:
/*      */       case 32:
/*      */       case 33:
/*      */       case 34:
/*      */       case 35:
/*      */       case 36:
/*      */       case 54:
/*      */       case 55:
/* 1317 */         nc = UnprefixedNameClass(context, a);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1330 */         return nc;case 57: nc = PrefixedNameClass(a); return nc;case 28: nc = ParenNameClass(context, a); return nc;
/*      */     } 
/*      */     this.jj_la1[33] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException(); } public final ParsedNameClass OpenNameClass(int context, Annotations a) throws ParseException {
/*      */     Token t;
/*      */     LocatedString ns;
/* 1337 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 56:
/* 1339 */         ns = NsName();
/* 1340 */         checkNsName(context, ns); return this.ncb.makeNsName(ns.getString(), ns.getLocation(), a);
/*      */       
/*      */       case 25:
/* 1343 */         t = jj_consume_token(25);
/* 1344 */         checkAnyName(context, t); return this.ncb.makeAnyName(makeLocation(t), a);
/*      */     } 
/*      */     
/* 1347 */     this.jj_la1[34] = this.jj_gen;
/* 1348 */     jj_consume_token(-1);
/* 1349 */     throw new ParseException();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedNameClass UnprefixedNameClass(int context, Annotations a) throws ParseException {
/*      */     String ns;
/* 1356 */     LocatedString name = UnprefixedName();
/*      */     
/* 1358 */     if ((context & 0x1) == 1) {
/* 1359 */       ns = "";
/*      */     } else {
/* 1361 */       ns = this.defaultNamespace;
/* 1362 */     }  return this.ncb.makeName(ns, name.getString(), null, name.getLocation(), a);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedNameClass PrefixedNameClass(Annotations a) throws ParseException {
/* 1368 */     Token t = jj_consume_token(57);
/* 1369 */     String qn = t.image;
/* 1370 */     int colon = qn.indexOf(':');
/* 1371 */     String prefix = qn.substring(0, colon);
/* 1372 */     return this.ncb.makeName(lookupPrefix(prefix, t), qn.substring(colon + 1), prefix, makeLocation(t), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedNameClass NsNameExceptClass(int context, Annotations a, Annotations[] pa) throws ParseException {
/* 1379 */     LocatedString ns = NsName();
/* 1380 */     checkNsName(context, ns);
/* 1381 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk)
/*      */     { case 30:
/* 1383 */         nc = ExceptNameClass(context | 0x4);
/* 1384 */         nc = this.ncb.makeNsName(ns.getString(), nc, ns.getLocation(), a);
/* 1385 */         nc = AnnotateAfter(nc);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1393 */         return nc; }  this.jj_la1[35] = this.jj_gen; ParsedNameClass nc = this.ncb.makeNsName(ns.getString(), ns.getLocation(), a); nc = AnnotateAfter(nc); nc = NameClassAlternatives(context, nc, pa); return nc;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final LocatedString NsName() throws ParseException {
/* 1399 */     Token t = jj_consume_token(56);
/* 1400 */     String qn = t.image;
/* 1401 */     String prefix = qn.substring(0, qn.length() - 2);
/* 1402 */     return new LocatedString(lookupPrefix(prefix, t), t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedNameClass AnyNameExceptClass(int context, Annotations a, Annotations[] pa) throws ParseException {
/* 1409 */     Token t = jj_consume_token(25);
/* 1410 */     checkAnyName(context, t);
/* 1411 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk)
/*      */     { case 30:
/* 1413 */         nc = ExceptNameClass(context | 0x2);
/* 1414 */         nc = this.ncb.makeAnyName(nc, makeLocation(t), a);
/* 1415 */         nc = AnnotateAfter(nc);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1423 */         return nc; }  this.jj_la1[36] = this.jj_gen; ParsedNameClass nc = this.ncb.makeAnyName(makeLocation(t), a); nc = AnnotateAfter(nc); nc = NameClassAlternatives(context, nc, pa); return nc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedNameClass ParenNameClass(int context, Annotations a) throws ParseException {
/* 1430 */     Annotations[] pa = { a };
/* 1431 */     Token t = jj_consume_token(28);
/* 1432 */     ParsedNameClass nc = NameClass(context, pa);
/* 1433 */     nc = afterComments(nc);
/* 1434 */     jj_consume_token(29);
/* 1435 */     if (pa[0] != null)
/* 1436 */       nc = this.ncb.makeChoice(Collections.singletonList(nc), makeLocation(t), pa[0]); 
/* 1437 */     return nc;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedNameClass ExceptNameClass(int context) throws ParseException {
/* 1443 */     jj_consume_token(30);
/* 1444 */     ParsedNameClass nc = BasicNameClass(context);
/* 1445 */     return nc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern ListExpr(Scope scope, Annotations a) throws ParseException {
/* 1452 */     Token t = jj_consume_token(31);
/* 1453 */     jj_consume_token(11);
/* 1454 */     ParsedPattern p = Expr(false, scope, null, null);
/* 1455 */     p = afterComments(p);
/* 1456 */     jj_consume_token(12);
/* 1457 */     return this.sb.makeList(p, makeLocation(t), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern MixedExpr(Scope scope, Annotations a) throws ParseException {
/* 1464 */     Token t = jj_consume_token(32);
/* 1465 */     jj_consume_token(11);
/* 1466 */     ParsedPattern p = Expr(false, scope, null, null);
/* 1467 */     p = afterComments(p);
/* 1468 */     jj_consume_token(12);
/* 1469 */     return this.sb.makeMixed(p, makeLocation(t), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern GrammarExpr(Scope scope, Annotations a) throws ParseException {
/* 1476 */     Token t = jj_consume_token(10);
/* 1477 */     Grammar g = this.sb.makeGrammar(scope);
/* 1478 */     jj_consume_token(11);
/* 1479 */     a = GrammarBody((GrammarSection)g, (Scope)g, a);
/* 1480 */     topLevelComments((GrammarSection)g);
/* 1481 */     jj_consume_token(12);
/* 1482 */     return g.endGrammar(makeLocation(t), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern ParenExpr(boolean topLevel, Scope scope, Annotations a) throws ParseException {
/* 1489 */     Token t = jj_consume_token(28);
/* 1490 */     ParsedPattern p = Expr(topLevel, scope, t, a);
/* 1491 */     p = afterComments(p);
/* 1492 */     jj_consume_token(29);
/* 1493 */     return p;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Annotations GrammarBody(GrammarSection section, Scope scope, Annotations a) throws ParseException {
/* 1501 */     while (jj_2_3(2)) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1506 */       ParsedElementAnnotation e = AnnotationElementNotKeyword();
/* 1507 */       if (a == null) a = this.sb.makeAnnotations(null, getContext());  a.addElement(e);
/*      */     } 
/*      */     
/*      */     while (true) {
/* 1511 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 1:
/*      */         case 5:
/*      */         case 6:
/*      */         case 7:
/*      */         case 40:
/*      */         case 43:
/*      */         case 54:
/*      */         case 55:
/*      */           break;
/*      */         
/*      */         default:
/* 1523 */           this.jj_la1[37] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 1526 */       GrammarComponent(section, scope);
/*      */     } 
/* 1528 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void GrammarComponent(GrammarSection section, Scope scope) throws ParseException {
/* 1535 */     Annotations a = Annotations();
/* 1536 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 5:
/*      */       case 54:
/*      */       case 55:
/* 1540 */         Definition(section, scope, a);
/*      */         break;
/*      */       case 7:
/* 1543 */         Include(section, scope, a);
/*      */         break;
/*      */       case 6:
/* 1546 */         Div(section, scope, a);
/*      */         break;
/*      */       default:
/* 1549 */         this.jj_la1[38] = this.jj_gen;
/* 1550 */         jj_consume_token(-1);
/* 1551 */         throw new ParseException();
/*      */     } 
/*      */ 
/*      */     
/* 1555 */     while (jj_2_4(2)) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1560 */       ParsedElementAnnotation e = AnnotationElementNotKeyword();
/* 1561 */       section.topLevelAnnotation(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void Definition(GrammarSection section, Scope scope, Annotations a) throws ParseException {
/* 1566 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 54:
/*      */       case 55:
/* 1569 */         Define(section, scope, a);
/*      */         return;
/*      */       case 5:
/* 1572 */         Start(section, scope, a);
/*      */         return;
/*      */     } 
/* 1575 */     this.jj_la1[39] = this.jj_gen;
/* 1576 */     jj_consume_token(-1);
/* 1577 */     throw new ParseException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void Start(GrammarSection section, Scope scope, Annotations a) throws ParseException {
/* 1585 */     Token t = jj_consume_token(5);
/* 1586 */     GrammarSection.Combine combine = AssignOp();
/* 1587 */     ParsedPattern p = Expr(false, scope, null, null);
/* 1588 */     section.define("\000#start\000", combine, p, makeLocation(t), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void Define(GrammarSection section, Scope scope, Annotations a) throws ParseException {
/* 1595 */     LocatedString name = Identifier();
/* 1596 */     GrammarSection.Combine combine = AssignOp();
/* 1597 */     ParsedPattern p = Expr(false, scope, null, null);
/* 1598 */     section.define(name.getString(), combine, p, name.getLocation(), a);
/*      */   }
/*      */   
/*      */   public final GrammarSection.Combine AssignOp() throws ParseException {
/* 1602 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 2:
/* 1604 */         jj_consume_token(2);
/* 1605 */         return null;
/*      */       
/*      */       case 4:
/* 1608 */         jj_consume_token(4);
/* 1609 */         return GrammarSection.COMBINE_CHOICE;
/*      */       
/*      */       case 3:
/* 1612 */         jj_consume_token(3);
/* 1613 */         return GrammarSection.COMBINE_INTERLEAVE;
/*      */     } 
/*      */     
/* 1616 */     this.jj_la1[40] = this.jj_gen;
/* 1617 */     jj_consume_token(-1);
/* 1618 */     throw new ParseException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void Include(GrammarSection section, Scope scope, Annotations a) throws ParseException {
/* 1627 */     Include include = section.makeInclude();
/* 1628 */     Token t = jj_consume_token(7);
/* 1629 */     String href = Literal();
/* 1630 */     String ns = Inherit();
/* 1631 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 11:
/* 1633 */         jj_consume_token(11);
/* 1634 */         a = IncludeBody((GrammarSection)include, scope, a);
/* 1635 */         topLevelComments((GrammarSection)include);
/* 1636 */         jj_consume_token(12);
/*      */         break;
/*      */       default:
/* 1639 */         this.jj_la1[41] = this.jj_gen;
/*      */         break;
/*      */     } 
/*      */     try {
/* 1643 */       include.endInclude(this.parseable, resolve(href), ns, makeLocation(t), a);
/*      */     }
/* 1645 */     catch (IllegalSchemaException e) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Annotations IncludeBody(GrammarSection section, Scope scope, Annotations a) throws ParseException {
/* 1652 */     while (jj_2_5(2)) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1657 */       ParsedElementAnnotation e = AnnotationElementNotKeyword();
/* 1658 */       if (a == null) a = this.sb.makeAnnotations(null, getContext());  a.addElement(e);
/*      */     } 
/*      */     
/*      */     while (true) {
/* 1662 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 1:
/*      */         case 5:
/*      */         case 6:
/*      */         case 40:
/*      */         case 43:
/*      */         case 54:
/*      */         case 55:
/*      */           break;
/*      */         
/*      */         default:
/* 1673 */           this.jj_la1[42] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 1676 */       IncludeComponent(section, scope);
/*      */     } 
/* 1678 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void IncludeComponent(GrammarSection section, Scope scope) throws ParseException {
/* 1685 */     Annotations a = Annotations();
/* 1686 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 5:
/*      */       case 54:
/*      */       case 55:
/* 1690 */         Definition(section, scope, a);
/*      */         break;
/*      */       case 6:
/* 1693 */         IncludeDiv(section, scope, a);
/*      */         break;
/*      */       default:
/* 1696 */         this.jj_la1[43] = this.jj_gen;
/* 1697 */         jj_consume_token(-1);
/* 1698 */         throw new ParseException();
/*      */     } 
/*      */ 
/*      */     
/* 1702 */     while (jj_2_6(2)) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1707 */       ParsedElementAnnotation e = AnnotationElementNotKeyword();
/* 1708 */       section.topLevelAnnotation(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void Div(GrammarSection section, Scope scope, Annotations a) throws ParseException {
/* 1714 */     Div div = section.makeDiv();
/* 1715 */     Token t = jj_consume_token(6);
/* 1716 */     jj_consume_token(11);
/* 1717 */     a = GrammarBody((GrammarSection)div, scope, a);
/* 1718 */     topLevelComments((GrammarSection)div);
/* 1719 */     jj_consume_token(12);
/* 1720 */     div.endDiv(makeLocation(t), a);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void IncludeDiv(GrammarSection section, Scope scope, Annotations a) throws ParseException {
/* 1725 */     Div div = section.makeDiv();
/* 1726 */     Token t = jj_consume_token(6);
/* 1727 */     jj_consume_token(11);
/* 1728 */     a = IncludeBody((GrammarSection)div, scope, a);
/* 1729 */     topLevelComments((GrammarSection)div);
/* 1730 */     jj_consume_token(12);
/* 1731 */     div.endDiv(makeLocation(t), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern ExternalRefExpr(Scope scope, Annotations a) throws ParseException {
/* 1738 */     Token t = jj_consume_token(33);
/* 1739 */     String href = Literal();
/* 1740 */     String ns = Inherit();
/*      */     try {
/* 1742 */       return this.sb.makeExternalRef(this.parseable, resolve(href), ns, scope, makeLocation(t), a);
/*      */     }
/* 1744 */     catch (IllegalSchemaException e) {
/* 1745 */       return this.sb.makeErrorPattern();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final String Inherit() throws ParseException {
/* 1751 */     String ns = null;
/* 1752 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 15:
/* 1754 */         jj_consume_token(15);
/* 1755 */         jj_consume_token(2);
/* 1756 */         ns = Prefix();
/*      */         break;
/*      */       default:
/* 1759 */         this.jj_la1[44] = this.jj_gen;
/*      */         break;
/*      */     } 
/* 1762 */     if (ns == null)
/* 1763 */       ns = this.defaultNamespace; 
/* 1764 */     return ns;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern ParentExpr(Scope scope, Annotations a) throws ParseException {
/* 1770 */     jj_consume_token(34);
/* 1771 */     a = addCommentsToChildAnnotations(a);
/* 1772 */     LocatedString name = Identifier();
/* 1773 */     if (scope == null) {
/* 1774 */       error("parent_ref_outside_grammar", name.getToken());
/* 1775 */       return this.sb.makeErrorPattern();
/*      */     } 
/* 1777 */     return scope.makeParentRef(name.getString(), name.getLocation(), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern IdentifierExpr(Scope scope, Annotations a) throws ParseException {
/* 1784 */     LocatedString name = Identifier();
/* 1785 */     if (scope == null) {
/* 1786 */       error("ref_outside_grammar", name.getToken());
/* 1787 */       return this.sb.makeErrorPattern();
/*      */     } 
/* 1789 */     return scope.makeRef(name.getString(), name.getLocation(), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern ValueExpr(boolean topLevel, Annotations a) throws ParseException {
/* 1796 */     LocatedString s = LocatedLiteral();
/* 1797 */     if (topLevel && this.annotationsIncludeElements) {
/* 1798 */       error("top_level_follow_annotation", s.getToken());
/* 1799 */       a = null;
/*      */     } 
/* 1801 */     return this.sb.makeValue("", "token", s.getString(), getContext(), this.defaultNamespace, s.getLocation(), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern DataExpr(boolean topLevel, Scope scope, Annotations a, Token[] except) throws ParseException {
/* 1809 */     String datatypeUri = null;
/* 1810 */     String s = null;
/* 1811 */     ParsedPattern e = null;
/*      */     
/* 1813 */     Token datatypeToken = DatatypeName();
/* 1814 */     String datatype = datatypeToken.image;
/* 1815 */     Location loc = makeLocation(datatypeToken);
/* 1816 */     int colon = datatype.indexOf(':');
/* 1817 */     if (colon < 0) {
/* 1818 */       datatypeUri = "";
/*      */     } else {
/* 1820 */       String prefix = datatype.substring(0, colon);
/* 1821 */       datatypeUri = lookupDatatype(prefix, datatypeToken);
/* 1822 */       datatype = datatype.substring(colon + 1);
/*      */     } 
/* 1824 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 58:
/* 1826 */         s = Literal();
/* 1827 */         if (topLevel && this.annotationsIncludeElements) {
/* 1828 */           error("top_level_follow_annotation", datatypeToken);
/* 1829 */           a = null;
/*      */         } 
/* 1831 */         return this.sb.makeValue(datatypeUri, datatype, s, getContext(), this.defaultNamespace, loc, a);
/*      */     } 
/*      */     
/* 1834 */     this.jj_la1[48] = this.jj_gen;
/* 1835 */     DataPatternBuilder dpb = this.sb.makeDataPatternBuilder(datatypeUri, datatype, loc);
/* 1836 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 11:
/* 1838 */         Params(dpb);
/* 1839 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 30:
/* 1841 */             e = Except(scope, except);
/*      */             break;
/*      */         } 
/* 1844 */         this.jj_la1[45] = this.jj_gen;
/*      */         break;
/*      */ 
/*      */       
/*      */       default:
/* 1849 */         this.jj_la1[47] = this.jj_gen;
/* 1850 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 30:
/* 1852 */             e = Except(scope, except);
/*      */             break;
/*      */         } 
/* 1855 */         this.jj_la1[46] = this.jj_gen;
/*      */         break;
/*      */     } 
/*      */     
/* 1859 */     return (e == null) ? dpb.makePattern(loc, a) : dpb.makePattern(e, loc, a);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Token DatatypeName() throws ParseException {
/*      */     Token t;
/* 1866 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 35:
/* 1868 */         t = jj_consume_token(35);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1881 */         return t;case 36: t = jj_consume_token(36); return t;case 57: t = jj_consume_token(57); return t;
/*      */     } 
/*      */     this.jj_la1[49] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException();
/*      */   } public final LocatedString Identifier() throws ParseException { LocatedString s;
/*      */     Token t;
/* 1888 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 54:
/* 1890 */         t = jj_consume_token(54);
/* 1891 */         s = new LocatedString(t.image, t);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1902 */         return s;case 55: t = jj_consume_token(55); s = new LocatedString(t.image.substring(1), t); return s;
/*      */     } 
/*      */     this.jj_la1[50] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException(); }
/*      */   public final String Prefix() throws ParseException { Token t;
/*      */     String prefix;
/* 1909 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 54:
/* 1911 */         t = jj_consume_token(54);
/* 1912 */         prefix = t.image;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1945 */         return lookupPrefix(prefix, t);case 55: t = jj_consume_token(55); prefix = t.image.substring(1); return lookupPrefix(prefix, t);case 5: case 6: case 7: case 10: case 13: case 14: case 15: case 16: case 17: case 18: case 19: case 26: case 27: case 31: case 32: case 33: case 34: case 35: case 36: t = Keyword(); prefix = t.image; return lookupPrefix(prefix, t);
/*      */     } 
/*      */     this.jj_la1[51] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException(); }
/*      */   public final LocatedString UnprefixedName() throws ParseException { LocatedString s;
/*      */     Token t;
/* 1952 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 54:
/*      */       case 55:
/* 1955 */         s = Identifier();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1984 */         return s;case 5: case 6: case 7: case 10: case 13: case 14: case 15: case 16: case 17: case 18: case 19: case 26: case 27: case 31: case 32: case 33: case 34: case 35: case 36: t = Keyword(); s = new LocatedString(t.image, t); return s;
/*      */     } 
/*      */     this.jj_la1[52] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException(); } public final void Params(DataPatternBuilder dpb) throws ParseException {
/* 1989 */     jj_consume_token(11);
/*      */     
/*      */     while (true) {
/* 1992 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 1:
/*      */         case 5:
/*      */         case 6:
/*      */         case 7:
/*      */         case 10:
/*      */         case 13:
/*      */         case 14:
/*      */         case 15:
/*      */         case 16:
/*      */         case 17:
/*      */         case 18:
/*      */         case 19:
/*      */         case 26:
/*      */         case 27:
/*      */         case 31:
/*      */         case 32:
/*      */         case 33:
/*      */         case 34:
/*      */         case 35:
/*      */         case 36:
/*      */         case 40:
/*      */         case 43:
/*      */         case 54:
/*      */         case 55:
/*      */           break;
/*      */         
/*      */         default:
/* 2020 */           this.jj_la1[53] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 2023 */       Param(dpb);
/*      */     } 
/* 2025 */     jj_consume_token(12);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void Param(DataPatternBuilder dpb) throws ParseException {
/* 2032 */     Annotations a = Annotations();
/* 2033 */     LocatedString name = UnprefixedName();
/* 2034 */     jj_consume_token(2);
/* 2035 */     a = addCommentsToLeadingAnnotations(a);
/* 2036 */     String value = Literal();
/* 2037 */     dpb.addParam(name.getString(), value, getContext(), this.defaultNamespace, name.getLocation(), a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParsedPattern Except(Scope scope, Token[] except) throws ParseException {
/* 2044 */     Token[] innerExcept = new Token[1];
/* 2045 */     Token t = jj_consume_token(30);
/* 2046 */     Annotations a = Annotations();
/* 2047 */     ParsedPattern p = PrimaryExpr(false, scope, a, innerExcept);
/* 2048 */     checkExcept(innerExcept);
/* 2049 */     except[0] = t;
/* 2050 */     return p;
/*      */   }
/*      */   
/*      */   public final ParsedElementAnnotation Documentation() throws ParseException {
/*      */     Token t;
/* 2055 */     CommentList comments = getComments();
/*      */ 
/*      */     
/* 2058 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 40:
/* 2060 */         t = jj_consume_token(40);
/*      */         break;
/*      */       case 43:
/* 2063 */         t = jj_consume_token(43);
/*      */         break;
/*      */       default:
/* 2066 */         this.jj_la1[54] = this.jj_gen;
/* 2067 */         jj_consume_token(-1);
/* 2068 */         throw new ParseException();
/*      */     } 
/* 2070 */     ElementAnnotationBuilder eab = this.sb.makeElementAnnotationBuilder("http://relaxng.org/ns/compatibility/annotations/1.0", "documentation", getCompatibilityPrefix(), makeLocation(t), comments, getContext());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2076 */     eab.addText(mungeComment(t.image), makeLocation(t), null);
/*      */     
/*      */     while (true) {
/* 2079 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 41:
/*      */           break;
/*      */         
/*      */         default:
/* 2084 */           this.jj_la1[55] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 2087 */       t = jj_consume_token(41);
/* 2088 */       eab.addText("\n" + mungeComment(t.image), makeLocation(t), null);
/*      */     } 
/* 2090 */     return eab.makeElementAnnotation();
/*      */   }
/*      */ 
/*      */   
/*      */   public final Annotations Annotations() throws ParseException {
/* 2095 */     CommentList comments = getComments();
/* 2096 */     Annotations a = null;
/*      */     
/* 2098 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 40:
/*      */       case 43:
/* 2101 */         a = this.sb.makeAnnotations(comments, getContext());
/*      */         
/*      */         while (true) {
/* 2104 */           ParsedElementAnnotation e = Documentation();
/* 2105 */           a.addElement(e);
/* 2106 */           switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */             case 40:
/*      */             case 43:
/*      */               continue;
/*      */           }  break;
/*      */         } 
/* 2112 */         this.jj_la1[56] = this.jj_gen;
/*      */ 
/*      */ 
/*      */         
/* 2116 */         comments = getComments();
/* 2117 */         if (comments != null)
/* 2118 */           a.addLeadingComment(comments); 
/*      */         break;
/*      */       default:
/* 2121 */         this.jj_la1[57] = this.jj_gen;
/*      */         break;
/*      */     } 
/* 2124 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 1:
/* 2126 */         jj_consume_token(1);
/* 2127 */         if (a == null) a = this.sb.makeAnnotations(comments, getContext());  clearAttributeList(); this.annotationsIncludeElements = false;
/*      */ 
/*      */         
/* 2130 */         while (jj_2_7(2))
/*      */         {
/*      */ 
/*      */ 
/*      */           
/* 2135 */           PrefixedAnnotationAttribute(a, false);
/*      */         }
/*      */         
/*      */         while (true) {
/* 2139 */           switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */             case 5:
/*      */             case 6:
/*      */             case 7:
/*      */             case 10:
/*      */             case 13:
/*      */             case 14:
/*      */             case 15:
/*      */             case 16:
/*      */             case 17:
/*      */             case 18:
/*      */             case 19:
/*      */             case 26:
/*      */             case 27:
/*      */             case 31:
/*      */             case 32:
/*      */             case 33:
/*      */             case 34:
/*      */             case 35:
/*      */             case 36:
/*      */             case 54:
/*      */             case 55:
/*      */             case 57:
/*      */               break;
/*      */             
/*      */             default:
/* 2165 */               this.jj_la1[58] = this.jj_gen;
/*      */               break;
/*      */           } 
/* 2168 */           ParsedElementAnnotation parsedElementAnnotation = AnnotationElement(false);
/* 2169 */           a.addElement(parsedElementAnnotation); this.annotationsIncludeElements = true;
/*      */         } 
/* 2171 */         a.addComment(getComments());
/* 2172 */         jj_consume_token(9);
/*      */         break;
/*      */       default:
/* 2175 */         this.jj_la1[59] = this.jj_gen;
/*      */         break;
/*      */     } 
/* 2178 */     if (a == null && comments != null)
/* 2179 */       a = this.sb.makeAnnotations(comments, getContext()); 
/* 2180 */     return a;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void AnnotationAttribute(Annotations a) throws ParseException {
/* 2185 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 57:
/* 2187 */         PrefixedAnnotationAttribute(a, true);
/*      */         return;
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 10:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 19:
/*      */       case 26:
/*      */       case 27:
/*      */       case 31:
/*      */       case 32:
/*      */       case 33:
/*      */       case 34:
/*      */       case 35:
/*      */       case 36:
/*      */       case 54:
/*      */       case 55:
/* 2210 */         UnprefixedAnnotationAttribute(a);
/*      */         return;
/*      */     } 
/* 2213 */     this.jj_la1[60] = this.jj_gen;
/* 2214 */     jj_consume_token(-1);
/* 2215 */     throw new ParseException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void PrefixedAnnotationAttribute(Annotations a, boolean nested) throws ParseException {
/* 2222 */     Token t = jj_consume_token(57);
/* 2223 */     jj_consume_token(2);
/* 2224 */     String value = Literal();
/* 2225 */     String qn = t.image;
/* 2226 */     int colon = qn.indexOf(':');
/* 2227 */     String prefix = qn.substring(0, colon);
/* 2228 */     String ns = lookupPrefix(prefix, t);
/* 2229 */     if (ns == this.inheritedNs) {
/* 2230 */       error("inherited_annotation_namespace", t);
/* 2231 */     } else if (ns.length() == 0 && !nested) {
/* 2232 */       error("unqualified_annotation_attribute", t);
/* 2233 */     } else if (ns.equals("http://relaxng.org/ns/structure/1.0") && !nested) {
/* 2234 */       error("relax_ng_namespace", t);
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 2239 */     else if (ns.equals("http://www.w3.org/2000/xmlns")) {
/* 2240 */       error("xmlns_annotation_attribute_uri", t);
/*      */     } else {
/* 2242 */       if (ns.length() == 0)
/* 2243 */         prefix = null; 
/* 2244 */       addAttribute(a, ns, qn.substring(colon + 1), prefix, value, t);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void UnprefixedAnnotationAttribute(Annotations a) throws ParseException {
/* 2251 */     LocatedString name = UnprefixedName();
/* 2252 */     jj_consume_token(2);
/* 2253 */     String value = Literal();
/* 2254 */     if (name.getString().equals("xmlns")) {
/* 2255 */       error("xmlns_annotation_attribute", name.getToken());
/*      */     } else {
/* 2257 */       addAttribute(a, "", name.getString(), null, value, name.getToken());
/*      */     } 
/*      */   }
/*      */   
/*      */   public final ParsedElementAnnotation AnnotationElement(boolean nested) throws ParseException { ParsedElementAnnotation a;
/* 2262 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 57:
/* 2264 */         a = PrefixedAnnotationElement(nested);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2294 */         return a;case 5: case 6: case 7: case 10: case 13: case 14: case 15: case 16: case 17: case 18: case 19: case 26: case 27: case 31: case 32: case 33: case 34: case 35: case 36: case 54: case 55: a = UnprefixedAnnotationElement(); return a;
/*      */     } 
/*      */     this.jj_la1[61] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException(); }
/*      */   public final ParsedElementAnnotation AnnotationElementNotKeyword() throws ParseException { ParsedElementAnnotation a;
/* 2300 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 57:
/* 2302 */         a = PrefixedAnnotationElement(false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2313 */         return a;case 54: case 55: a = IdentifierAnnotationElement(); return a;
/*      */     } 
/*      */     this.jj_la1[62] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException(); } public final ParsedElementAnnotation PrefixedAnnotationElement(boolean nested) throws ParseException {
/* 2318 */     CommentList comments = getComments();
/*      */ 
/*      */     
/* 2321 */     Token t = jj_consume_token(57);
/* 2322 */     String qn = t.image;
/* 2323 */     int colon = qn.indexOf(':');
/* 2324 */     String prefix = qn.substring(0, colon);
/* 2325 */     String ns = lookupPrefix(prefix, t);
/* 2326 */     if (ns == this.inheritedNs) {
/* 2327 */       error("inherited_annotation_namespace", t);
/* 2328 */       ns = "";
/*      */     }
/* 2330 */     else if (!nested && ns.equals("http://relaxng.org/ns/structure/1.0")) {
/* 2331 */       error("relax_ng_namespace", t);
/* 2332 */       ns = "";
/*      */     
/*      */     }
/* 2335 */     else if (ns.length() == 0) {
/* 2336 */       prefix = null;
/*      */     } 
/* 2338 */     ElementAnnotationBuilder eab = this.sb.makeElementAnnotationBuilder(ns, qn.substring(colon + 1), prefix, makeLocation(t), comments, getContext());
/*      */     
/* 2340 */     AnnotationElementContent(eab);
/* 2341 */     return eab.makeElementAnnotation();
/*      */   }
/*      */ 
/*      */   
/*      */   public final ParsedElementAnnotation UnprefixedAnnotationElement() throws ParseException {
/* 2346 */     CommentList comments = getComments();
/*      */ 
/*      */     
/* 2349 */     LocatedString name = UnprefixedName();
/* 2350 */     ElementAnnotationBuilder eab = this.sb.makeElementAnnotationBuilder("", name.getString(), null, name.getLocation(), comments, getContext());
/*      */     
/* 2352 */     AnnotationElementContent(eab);
/* 2353 */     return eab.makeElementAnnotation();
/*      */   }
/*      */ 
/*      */   
/*      */   public final ParsedElementAnnotation IdentifierAnnotationElement() throws ParseException {
/* 2358 */     CommentList comments = getComments();
/*      */ 
/*      */     
/* 2361 */     LocatedString name = Identifier();
/* 2362 */     ElementAnnotationBuilder eab = this.sb.makeElementAnnotationBuilder("", name.getString(), null, name.getLocation(), comments, getContext());
/*      */     
/* 2364 */     AnnotationElementContent(eab);
/* 2365 */     return eab.makeElementAnnotation();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void AnnotationElementContent(ElementAnnotationBuilder eab) throws ParseException {
/* 2371 */     jj_consume_token(1);
/* 2372 */     clearAttributeList();
/*      */ 
/*      */     
/* 2375 */     while (jj_2_8(2))
/*      */     {
/*      */ 
/*      */ 
/*      */       
/* 2380 */       AnnotationAttribute((Annotations)eab);
/*      */     }
/*      */     label34: while (true) {
/*      */       ParsedElementAnnotation e;
/* 2384 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 5:
/*      */         case 6:
/*      */         case 7:
/*      */         case 10:
/*      */         case 13:
/*      */         case 14:
/*      */         case 15:
/*      */         case 16:
/*      */         case 17:
/*      */         case 18:
/*      */         case 19:
/*      */         case 26:
/*      */         case 27:
/*      */         case 31:
/*      */         case 32:
/*      */         case 33:
/*      */         case 34:
/*      */         case 35:
/*      */         case 36:
/*      */         case 54:
/*      */         case 55:
/*      */         case 57:
/*      */         case 58:
/*      */           break;
/*      */         
/*      */         default:
/* 2411 */           this.jj_la1[63] = this.jj_gen;
/*      */           break;
/*      */       } 
/* 2414 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 58:
/* 2416 */           AnnotationElementLiteral(eab);
/*      */           
/*      */           while (true) {
/* 2419 */             switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */               case 8:
/*      */                 break;
/*      */               
/*      */               default:
/* 2424 */                 this.jj_la1[64] = this.jj_gen;
/*      */                 continue label34;
/*      */             } 
/* 2427 */             jj_consume_token(8);
/* 2428 */             AnnotationElementLiteral(eab);
/*      */           } 
/*      */         
/*      */         case 5:
/*      */         case 6:
/*      */         case 7:
/*      */         case 10:
/*      */         case 13:
/*      */         case 14:
/*      */         case 15:
/*      */         case 16:
/*      */         case 17:
/*      */         case 18:
/*      */         case 19:
/*      */         case 26:
/*      */         case 27:
/*      */         case 31:
/*      */         case 32:
/*      */         case 33:
/*      */         case 34:
/*      */         case 35:
/*      */         case 36:
/*      */         case 54:
/*      */         case 55:
/*      */         case 57:
/* 2453 */           e = AnnotationElement(true);
/* 2454 */           eab.addElement(e);
/*      */           continue;
/*      */       } 
/* 2457 */       this.jj_la1[65] = this.jj_gen;
/* 2458 */       jj_consume_token(-1);
/* 2459 */       throw new ParseException();
/*      */     } 
/*      */     
/* 2462 */     eab.addComment(getComments());
/* 2463 */     jj_consume_token(9);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void AnnotationElementLiteral(ElementAnnotationBuilder eab) throws ParseException {
/* 2468 */     CommentList comments = getComments();
/* 2469 */     Token t = jj_consume_token(58);
/* 2470 */     eab.addText(unquote(t.image), makeLocation(t), comments);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String Literal() throws ParseException {
/*      */     StringBuffer buf;
/* 2477 */     Token t = jj_consume_token(58);
/* 2478 */     String s = unquote(t.image);
/* 2479 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk)
/*      */     { case 8:
/* 2481 */         buf = new StringBuffer(s);
/*      */         
/*      */         while (true) {
/* 2484 */           jj_consume_token(8);
/* 2485 */           t = jj_consume_token(58);
/* 2486 */           buf.append(unquote(t.image));
/* 2487 */           switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */             case 8:
/*      */               continue;
/*      */           }  break;
/*      */         } 
/* 2492 */         this.jj_la1[66] = this.jj_gen;
/*      */ 
/*      */ 
/*      */         
/* 2496 */         s = buf.toString();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2502 */         return s; }  this.jj_la1[67] = this.jj_gen; return s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final LocatedString LocatedLiteral() throws ParseException {
/*      */     StringBuffer buf;
/* 2511 */     Token t = jj_consume_token(58);
/* 2512 */     String s = unquote(t.image);
/* 2513 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk)
/*      */     { case 8:
/* 2515 */         buf = new StringBuffer(s);
/*      */         
/*      */         while (true) {
/* 2518 */           jj_consume_token(8);
/* 2519 */           Token t2 = jj_consume_token(58);
/* 2520 */           buf.append(unquote(t2.image));
/* 2521 */           switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */             case 8:
/*      */               continue;
/*      */           }  break;
/*      */         } 
/* 2526 */         this.jj_la1[68] = this.jj_gen;
/*      */ 
/*      */ 
/*      */         
/* 2530 */         s = buf.toString();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2536 */         return new LocatedString(s, t); }  this.jj_la1[69] = this.jj_gen; return new LocatedString(s, t);
/*      */   }
/*      */   
/*      */   public final Token Keyword() throws ParseException
/*      */   {
/*      */     Token t;
/* 2542 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 26:
/* 2544 */         t = jj_consume_token(26);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2605 */         return t;case 27: t = jj_consume_token(27); return t;case 13: t = jj_consume_token(13); return t;case 31: t = jj_consume_token(31); return t;case 32: t = jj_consume_token(32); return t;case 10: t = jj_consume_token(10); return t;case 17: t = jj_consume_token(17); return t;case 18: t = jj_consume_token(18); return t;case 34: t = jj_consume_token(34); return t;case 33: t = jj_consume_token(33); return t;case 19: t = jj_consume_token(19); return t;case 5: t = jj_consume_token(5); return t;case 7: t = jj_consume_token(7); return t;case 14: t = jj_consume_token(14); return t;case 15: t = jj_consume_token(15); return t;case 35: t = jj_consume_token(35); return t;case 36: t = jj_consume_token(36); return t;case 16: t = jj_consume_token(16); return t;case 6: t = jj_consume_token(6); return t;
/*      */     } 
/*      */     this.jj_la1[70] = this.jj_gen;
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException(); } private final boolean jj_2_1(int xla) {
/* 2610 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 2611 */     try { return !jj_3_1(); }
/* 2612 */     catch (LookaheadSuccess ls) { return true; }
/* 2613 */     finally { jj_save(0, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_2(int xla) {
/* 2617 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 2618 */     try { return !jj_3_2(); }
/* 2619 */     catch (LookaheadSuccess ls) { return true; }
/* 2620 */     finally { jj_save(1, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_3(int xla) {
/* 2624 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 2625 */     try { return !jj_3_3(); }
/* 2626 */     catch (LookaheadSuccess ls) { return true; }
/* 2627 */     finally { jj_save(2, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_4(int xla) {
/* 2631 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 2632 */     try { return !jj_3_4(); }
/* 2633 */     catch (LookaheadSuccess ls) { return true; }
/* 2634 */     finally { jj_save(3, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_5(int xla) {
/* 2638 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 2639 */     try { return !jj_3_5(); }
/* 2640 */     catch (LookaheadSuccess ls) { return true; }
/* 2641 */     finally { jj_save(4, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_6(int xla) {
/* 2645 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 2646 */     try { return !jj_3_6(); }
/* 2647 */     catch (LookaheadSuccess ls) { return true; }
/* 2648 */     finally { jj_save(5, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_7(int xla) {
/* 2652 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 2653 */     try { return !jj_3_7(); }
/* 2654 */     catch (LookaheadSuccess ls) { return true; }
/* 2655 */     finally { jj_save(6, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_2_8(int xla) {
/* 2659 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token; 
/* 2660 */     try { return !jj_3_8(); }
/* 2661 */     catch (LookaheadSuccess ls) { return true; }
/* 2662 */     finally { jj_save(7, xla); }
/*      */   
/*      */   }
/*      */   private final boolean jj_3R_43() {
/* 2666 */     if (jj_scan_token(1)) return true;
/*      */     
/*      */     while (true) {
/* 2669 */       Token xsp = this.jj_scanpos;
/* 2670 */       if (jj_3R_52()) { this.jj_scanpos = xsp;
/*      */         
/* 2672 */         if (jj_scan_token(9)) return true; 
/* 2673 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3R_51() {
/* 2677 */     if (jj_scan_token(55)) return true; 
/* 2678 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_50() {
/* 2682 */     if (jj_scan_token(54)) return true; 
/* 2683 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_41() {
/* 2688 */     Token xsp = this.jj_scanpos;
/* 2689 */     if (jj_3R_50()) {
/* 2690 */       this.jj_scanpos = xsp;
/* 2691 */       if (jj_3R_51()) return true; 
/*      */     } 
/* 2693 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_47() {
/* 2697 */     if (jj_scan_token(57)) return true; 
/* 2698 */     if (jj_3R_56()) return true; 
/* 2699 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_55() {
/* 2704 */     Token xsp = this.jj_scanpos;
/* 2705 */     if (jj_scan_token(40)) {
/* 2706 */       this.jj_scanpos = xsp;
/* 2707 */       if (jj_scan_token(43)) return true; 
/*      */     } 
/*      */     while (true) {
/* 2710 */       xsp = this.jj_scanpos;
/* 2711 */       if (jj_scan_token(41)) { this.jj_scanpos = xsp;
/*      */         
/* 2713 */         return false; }
/*      */     
/*      */     } 
/*      */   }
/*      */   private final boolean jj_3R_45() {
/* 2718 */     if (jj_3R_55()) return true; 
/*      */     while (true) {
/* 2720 */       Token xsp = this.jj_scanpos;
/* 2721 */       if (jj_3R_55()) { this.jj_scanpos = xsp;
/*      */         
/* 2723 */         return false; }
/*      */     
/*      */     } 
/*      */   } private final boolean jj_3R_38() {
/* 2727 */     if (jj_3R_48()) return true; 
/* 2728 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_42() {
/* 2733 */     Token xsp = this.jj_scanpos;
/* 2734 */     if (jj_scan_token(5)) {
/* 2735 */       this.jj_scanpos = xsp;
/* 2736 */       if (jj_scan_token(6)) {
/* 2737 */         this.jj_scanpos = xsp;
/* 2738 */         if (jj_scan_token(7)) return true; 
/*      */       } 
/*      */     } 
/* 2741 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_37() {
/* 2745 */     if (jj_3R_47()) return true; 
/* 2746 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_54() {
/* 2750 */     if (jj_3R_42()) return true; 
/* 2751 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_29() {
/* 2756 */     Token xsp = this.jj_scanpos;
/* 2757 */     if (jj_3R_37()) {
/* 2758 */       this.jj_scanpos = xsp;
/* 2759 */       if (jj_3R_38()) return true; 
/*      */     } 
/* 2761 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_44() {
/* 2766 */     Token xsp = this.jj_scanpos;
/* 2767 */     if (jj_3R_53()) {
/* 2768 */       this.jj_scanpos = xsp;
/* 2769 */       if (jj_3R_54()) return true; 
/*      */     } 
/* 2771 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_53() {
/* 2775 */     if (jj_3R_41()) return true;
/*      */     
/* 2777 */     Token xsp = this.jj_scanpos;
/* 2778 */     if (jj_scan_token(2)) {
/* 2779 */       this.jj_scanpos = xsp;
/* 2780 */       if (jj_scan_token(3)) {
/* 2781 */         this.jj_scanpos = xsp;
/* 2782 */         if (jj_scan_token(4)) return true; 
/*      */       } 
/*      */     } 
/* 2785 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_36() {
/* 2789 */     if (jj_3R_45()) return true;
/*      */     
/* 2791 */     Token xsp = this.jj_scanpos;
/* 2792 */     if (jj_3R_46()) this.jj_scanpos = xsp; 
/* 2793 */     if (jj_3R_44()) return true; 
/* 2794 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_35() {
/* 2798 */     if (jj_3R_43()) return true; 
/* 2799 */     if (jj_3R_44()) return true; 
/* 2800 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_34() {
/* 2804 */     if (jj_3R_42()) return true; 
/* 2805 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_33() {
/* 2809 */     if (jj_3R_41()) return true;
/*      */     
/* 2811 */     Token xsp = this.jj_scanpos;
/* 2812 */     if (jj_scan_token(1)) {
/* 2813 */       this.jj_scanpos = xsp;
/* 2814 */       if (jj_scan_token(2)) {
/* 2815 */         this.jj_scanpos = xsp;
/* 2816 */         if (jj_scan_token(3)) {
/* 2817 */           this.jj_scanpos = xsp;
/* 2818 */           if (jj_scan_token(4)) return true; 
/*      */         } 
/*      */       } 
/*      */     } 
/* 2822 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_1() {
/* 2826 */     if (jj_3R_28()) return true; 
/* 2827 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_32() {
/* 2831 */     if (jj_scan_token(57)) return true; 
/* 2832 */     if (jj_scan_token(1)) return true; 
/* 2833 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_28() {
/* 2838 */     Token xsp = this.jj_scanpos;
/* 2839 */     if (jj_3R_32()) {
/* 2840 */       this.jj_scanpos = xsp;
/* 2841 */       if (jj_3R_33()) {
/* 2842 */         this.jj_scanpos = xsp;
/* 2843 */         if (jj_3R_34()) {
/* 2844 */           this.jj_scanpos = xsp;
/* 2845 */           if (jj_3R_35()) {
/* 2846 */             this.jj_scanpos = xsp;
/* 2847 */             if (jj_3R_36()) return true; 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 2852 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_59() {
/* 2856 */     if (jj_3R_43()) return true; 
/* 2857 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_8() {
/* 2861 */     if (jj_3R_31()) return true; 
/* 2862 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_56() {
/* 2866 */     if (jj_scan_token(1)) return true; 
/* 2867 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_49() {
/* 2871 */     if (jj_3R_57()) return true; 
/* 2872 */     if (jj_scan_token(2)) return true; 
/* 2873 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_40() {
/* 2877 */     if (jj_3R_49()) return true; 
/* 2878 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_4() {
/* 2882 */     if (jj_3R_29()) return true; 
/* 2883 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_48() {
/* 2887 */     if (jj_3R_41()) return true; 
/* 2888 */     if (jj_3R_56()) return true; 
/* 2889 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_3() {
/* 2893 */     if (jj_3R_29()) return true; 
/* 2894 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_6() {
/* 2898 */     if (jj_3R_29()) return true; 
/* 2899 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_62() {
/* 2904 */     Token xsp = this.jj_scanpos;
/* 2905 */     if (jj_scan_token(26)) {
/* 2906 */       this.jj_scanpos = xsp;
/* 2907 */       if (jj_scan_token(27)) {
/* 2908 */         this.jj_scanpos = xsp;
/* 2909 */         if (jj_scan_token(13)) {
/* 2910 */           this.jj_scanpos = xsp;
/* 2911 */           if (jj_scan_token(31)) {
/* 2912 */             this.jj_scanpos = xsp;
/* 2913 */             if (jj_scan_token(32)) {
/* 2914 */               this.jj_scanpos = xsp;
/* 2915 */               if (jj_scan_token(10)) {
/* 2916 */                 this.jj_scanpos = xsp;
/* 2917 */                 if (jj_scan_token(17)) {
/* 2918 */                   this.jj_scanpos = xsp;
/* 2919 */                   if (jj_scan_token(18)) {
/* 2920 */                     this.jj_scanpos = xsp;
/* 2921 */                     if (jj_scan_token(34)) {
/* 2922 */                       this.jj_scanpos = xsp;
/* 2923 */                       if (jj_scan_token(33)) {
/* 2924 */                         this.jj_scanpos = xsp;
/* 2925 */                         if (jj_scan_token(19)) {
/* 2926 */                           this.jj_scanpos = xsp;
/* 2927 */                           if (jj_scan_token(5)) {
/* 2928 */                             this.jj_scanpos = xsp;
/* 2929 */                             if (jj_scan_token(7)) {
/* 2930 */                               this.jj_scanpos = xsp;
/* 2931 */                               if (jj_scan_token(14)) {
/* 2932 */                                 this.jj_scanpos = xsp;
/* 2933 */                                 if (jj_scan_token(15)) {
/* 2934 */                                   this.jj_scanpos = xsp;
/* 2935 */                                   if (jj_scan_token(35)) {
/* 2936 */                                     this.jj_scanpos = xsp;
/* 2937 */                                     if (jj_scan_token(36)) {
/* 2938 */                                       this.jj_scanpos = xsp;
/* 2939 */                                       if (jj_scan_token(16)) {
/* 2940 */                                         this.jj_scanpos = xsp;
/* 2941 */                                         if (jj_scan_token(6)) return true; 
/*      */                                       } 
/*      */                                     } 
/*      */                                   } 
/*      */                                 } 
/*      */                               } 
/*      */                             } 
/*      */                           } 
/*      */                         } 
/*      */                       } 
/*      */                     } 
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 2960 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_61() {
/* 2964 */     if (jj_3R_62()) return true; 
/* 2965 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_2() {
/* 2969 */     if (jj_3R_28()) return true; 
/* 2970 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_30() {
/* 2974 */     if (jj_scan_token(57)) return true; 
/* 2975 */     if (jj_scan_token(2)) return true; 
/* 2976 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_60() {
/* 2980 */     if (jj_3R_41()) return true; 
/* 2981 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_58() {
/* 2985 */     if (jj_3R_57()) return true; 
/* 2986 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_57() {
/* 2991 */     Token xsp = this.jj_scanpos;
/* 2992 */     if (jj_3R_60()) {
/* 2993 */       this.jj_scanpos = xsp;
/* 2994 */       if (jj_3R_61()) return true; 
/*      */     } 
/* 2996 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_5() {
/* 3000 */     if (jj_3R_29()) return true; 
/* 3001 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_31() {
/* 3006 */     Token xsp = this.jj_scanpos;
/* 3007 */     if (jj_3R_39()) {
/* 3008 */       this.jj_scanpos = xsp;
/* 3009 */       if (jj_3R_40()) return true; 
/*      */     } 
/* 3011 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_39() {
/* 3015 */     if (jj_3R_30()) return true; 
/* 3016 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_7() {
/* 3020 */     if (jj_3R_30()) return true; 
/* 3021 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_46() {
/* 3025 */     if (jj_3R_43()) return true; 
/* 3026 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_52() {
/* 3031 */     Token xsp = this.jj_scanpos;
/* 3032 */     if (jj_scan_token(57)) {
/* 3033 */       this.jj_scanpos = xsp;
/* 3034 */       if (jj_3R_58()) {
/* 3035 */         this.jj_scanpos = xsp;
/* 3036 */         if (jj_scan_token(2)) {
/* 3037 */           this.jj_scanpos = xsp;
/* 3038 */           if (jj_scan_token(58)) {
/* 3039 */             this.jj_scanpos = xsp;
/* 3040 */             if (jj_scan_token(8)) {
/* 3041 */               this.jj_scanpos = xsp;
/* 3042 */               if (jj_3R_59()) return true; 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 3048 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean lookingAhead = false;
/*      */ 
/*      */   
/*      */   private boolean jj_semLA;
/*      */   
/*      */   private int jj_gen;
/*      */   
/* 3060 */   private final int[] jj_la1 = new int[71]; private static int[] jj_la1_0;
/*      */   private static int[] jj_la1_1;
/*      */   
/*      */   static {
/* 3064 */     jj_la1_0();
/* 3065 */     jj_la1_1();
/*      */   }
/*      */   private static void jj_la1_0() {
/* 3068 */     jj_la1_0 = new int[] { -1676803070, 30, 2, 226, 28, 224, 224, 0, 0, 0, -1945115162, -1945115162, 1026, 90112, 90112, -1945115424, 24576, 32768, 0, -1676803072, 1048576, 2097152, 4194304, 7340032, 7340032, 58720256, 0, 58720256, -1643125536, 0, 1048576, 1048576, -1643125536, -1676679968, 33554432, 1073741824, 1073741824, 226, 224, 32, 28, 2048, 98, 96, 32768, 1073741824, 1073741824, 2048, 0, 0, 0, -1945115424, -1945115424, -1945115422, 0, 0, 0, 0, -1945115424, 2, -1945115424, -1945115424, 0, -1945115424, 256, -1945115424, 256, 256, 256, 256, -1945115424 };
/*      */   }
/*      */   private static void jj_la1_1() {
/* 3071 */     jj_la1_1 = new int[] { 113248543, 0, 0, 46139648, 0, 12582912, 0, 2304, 512, 2304, 113246239, 113246239, 2304, 0, 0, 12582943, 0, 67108864, 134217728, 113246239, 0, 0, 0, 0, 0, 0, 134217728, 0, 62914591, 134217728, 0, 0, 62914591, 46137375, 16777216, 0, 0, 12585216, 12582912, 12582912, 0, 0, 12585216, 12582912, 0, 0, 0, 0, 67108864, 33554456, 12582912, 12582943, 12582943, 12585247, 2304, 512, 2304, 2304, 46137375, 0, 46137375, 46137375, 46137344, 113246239, 0, 113246239, 0, 0, 0, 0, 31 };
/*      */   }
/* 3073 */   private final JJCalls[] jj_2_rtns = new JJCalls[8];
/*      */   private boolean jj_rescan = false;
/* 3075 */   private int jj_gc = 0;
/*      */   
/*      */   private final LookaheadSuccess jj_ls;
/*      */   
/*      */   private Vector jj_expentries;
/*      */   
/*      */   private int[] jj_expentry;
/*      */   
/*      */   private int jj_kind;
/*      */   private int[] jj_lasttokens;
/*      */   private int jj_endpos;
/*      */   
/*      */   public void ReInit(InputStream stream) {
/* 3088 */     this.jj_input_stream.ReInit(stream, 1, 1);
/* 3089 */     this.token_source.ReInit(this.jj_input_stream);
/* 3090 */     this.token = new Token();
/* 3091 */     this.jj_ntk = -1;
/* 3092 */     this.jj_gen = 0; int i;
/* 3093 */     for (i = 0; i < 71; ) { this.jj_la1[i] = -1; i++; }
/* 3094 */      for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void ReInit(Reader stream) {
/* 3108 */     this.jj_input_stream.ReInit(stream, 1, 1);
/* 3109 */     this.token_source.ReInit(this.jj_input_stream);
/* 3110 */     this.token = new Token();
/* 3111 */     this.jj_ntk = -1;
/* 3112 */     this.jj_gen = 0; int i;
/* 3113 */     for (i = 0; i < 71; ) { this.jj_la1[i] = -1; i++; }
/* 3114 */      for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void ReInit(CompactSyntaxTokenManager tm) {
/* 3127 */     this.token_source = tm;
/* 3128 */     this.token = new Token();
/* 3129 */     this.jj_ntk = -1;
/* 3130 */     this.jj_gen = 0; int i;
/* 3131 */     for (i = 0; i < 71; ) { this.jj_la1[i] = -1; i++; }
/* 3132 */      for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }
/*      */   
/*      */   }
/*      */   private final Token jj_consume_token(int kind) throws ParseException {
/*      */     Token oldToken;
/* 3137 */     if ((oldToken = this.token).next != null) { this.token = this.token.next; }
/* 3138 */     else { this.token = this.token.next = this.token_source.getNextToken(); }
/* 3139 */      this.jj_ntk = -1;
/* 3140 */     if (this.token.kind == kind) {
/* 3141 */       this.jj_gen++;
/* 3142 */       if (++this.jj_gc > 100) {
/* 3143 */         this.jj_gc = 0;
/* 3144 */         for (int i = 0; i < this.jj_2_rtns.length; i++) {
/* 3145 */           JJCalls c = this.jj_2_rtns[i];
/* 3146 */           while (c != null) {
/* 3147 */             if (c.gen < this.jj_gen) c.first = null; 
/* 3148 */             c = c.next;
/*      */           } 
/*      */         } 
/*      */       } 
/* 3152 */       return this.token;
/*      */     } 
/* 3154 */     this.token = oldToken;
/* 3155 */     this.jj_kind = kind;
/* 3156 */     throw generateParseException();
/*      */   }
/*      */   private static final class LookaheadSuccess extends Error {
/*      */     private LookaheadSuccess() {} }
/* 3160 */   public CompactSyntax(InputStream stream) { this.jj_ls = new LookaheadSuccess();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3206 */     this.jj_expentries = new Vector();
/*      */     
/* 3208 */     this.jj_kind = -1;
/* 3209 */     this.jj_lasttokens = new int[100]; this.jj_input_stream = new JavaCharStream(stream, 1, 1); this.token_source = new CompactSyntaxTokenManager(this.jj_input_stream); this.token = new Token(); this.jj_ntk = -1; this.jj_gen = 0; int i; for (i = 0; i < 71; ) { this.jj_la1[i] = -1; i++; }  for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }  } public CompactSyntax(Reader stream) { this.jj_ls = new LookaheadSuccess(); this.jj_expentries = new Vector(); this.jj_kind = -1; this.jj_lasttokens = new int[100]; this.jj_input_stream = new JavaCharStream(stream, 1, 1); this.token_source = new CompactSyntaxTokenManager(this.jj_input_stream); this.token = new Token(); this.jj_ntk = -1; this.jj_gen = 0; int i; for (i = 0; i < 71; ) { this.jj_la1[i] = -1; i++; }  for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }  } public CompactSyntax(CompactSyntaxTokenManager tm) { this.jj_ls = new LookaheadSuccess(); this.jj_expentries = new Vector(); this.jj_kind = -1; this.jj_lasttokens = new int[100]; this.token_source = tm; this.token = new Token(); this.jj_ntk = -1; this.jj_gen = 0; int i; for (i = 0; i < 71; ) { this.jj_la1[i] = -1; i++; }  for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }  }
/*      */   private final boolean jj_scan_token(int kind) { if (this.jj_scanpos == this.jj_lastpos) { this.jj_la--; if (this.jj_scanpos.next == null) { this.jj_lastpos = this.jj_scanpos = this.jj_scanpos.next = this.token_source.getNextToken(); } else { this.jj_lastpos = this.jj_scanpos = this.jj_scanpos.next; }  } else { this.jj_scanpos = this.jj_scanpos.next; }  if (this.jj_rescan) { int i = 0; Token tok = this.token; while (tok != null && tok != this.jj_scanpos) { i++; tok = tok.next; }  if (tok != null) jj_add_error_token(kind, i);  }  if (this.jj_scanpos.kind != kind)
/*      */       return true;  if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos)
/*      */       throw this.jj_ls;  return false; }
/* 3213 */   public final Token getNextToken() { if (this.token.next != null) { this.token = this.token.next; } else { this.token = this.token.next = this.token_source.getNextToken(); }  this.jj_ntk = -1; this.jj_gen++; return this.token; } private void jj_add_error_token(int kind, int pos) { if (pos >= 100)
/* 3214 */       return;  if (pos == this.jj_endpos + 1)
/* 3215 */     { this.jj_lasttokens[this.jj_endpos++] = kind; }
/* 3216 */     else if (this.jj_endpos != 0)
/* 3217 */     { this.jj_expentry = new int[this.jj_endpos];
/* 3218 */       for (int i = 0; i < this.jj_endpos; i++) {
/* 3219 */         this.jj_expentry[i] = this.jj_lasttokens[i];
/*      */       }
/* 3221 */       boolean exists = false;
/* 3222 */       for (Enumeration<int[]> e = this.jj_expentries.elements(); e.hasMoreElements(); ) {
/* 3223 */         int[] oldentry = e.nextElement();
/* 3224 */         if (oldentry.length == this.jj_expentry.length) {
/* 3225 */           exists = true;
/* 3226 */           for (int j = 0; j < this.jj_expentry.length; j++) {
/* 3227 */             if (oldentry[j] != this.jj_expentry[j]) {
/* 3228 */               exists = false;
/*      */               break;
/*      */             } 
/*      */           } 
/* 3232 */           if (exists)
/*      */             break; 
/*      */         } 
/* 3235 */       }  if (!exists) this.jj_expentries.addElement(this.jj_expentry); 
/* 3236 */       if (pos != 0) this.jj_lasttokens[(this.jj_endpos = pos) - 1] = kind;  }  }
/*      */   public final Token getToken(int index) { Token t = this.lookingAhead ? this.jj_scanpos : this.token; for (int i = 0; i < index; i++) { if (t.next != null) { t = t.next; } else { t = t.next = this.token_source.getNextToken(); }
/*      */        }
/*      */      return t; }
/*      */   private final int jj_ntk() { if ((this.jj_nt = this.token.next) == null)
/* 3241 */       return this.jj_ntk = (this.token.next = this.token_source.getNextToken()).kind;  return this.jj_ntk = this.jj_nt.kind; } public ParseException generateParseException() { this.jj_expentries.removeAllElements();
/* 3242 */     boolean[] la1tokens = new boolean[61]; int i;
/* 3243 */     for (i = 0; i < 61; i++) {
/* 3244 */       la1tokens[i] = false;
/*      */     }
/* 3246 */     if (this.jj_kind >= 0) {
/* 3247 */       la1tokens[this.jj_kind] = true;
/* 3248 */       this.jj_kind = -1;
/*      */     } 
/* 3250 */     for (i = 0; i < 71; i++) {
/* 3251 */       if (this.jj_la1[i] == this.jj_gen) {
/* 3252 */         for (int k = 0; k < 32; k++) {
/* 3253 */           if ((jj_la1_0[i] & 1 << k) != 0) {
/* 3254 */             la1tokens[k] = true;
/*      */           }
/* 3256 */           if ((jj_la1_1[i] & 1 << k) != 0) {
/* 3257 */             la1tokens[32 + k] = true;
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/* 3262 */     for (i = 0; i < 61; i++) {
/* 3263 */       if (la1tokens[i]) {
/* 3264 */         this.jj_expentry = new int[1];
/* 3265 */         this.jj_expentry[0] = i;
/* 3266 */         this.jj_expentries.addElement(this.jj_expentry);
/*      */       } 
/*      */     } 
/* 3269 */     this.jj_endpos = 0;
/* 3270 */     jj_rescan_token();
/* 3271 */     jj_add_error_token(0, 0);
/* 3272 */     int[][] exptokseq = new int[this.jj_expentries.size()][];
/* 3273 */     for (int j = 0; j < this.jj_expentries.size(); j++) {
/* 3274 */       exptokseq[j] = this.jj_expentries.elementAt(j);
/*      */     }
/* 3276 */     return new ParseException(this.token, exptokseq, tokenImage); }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void enable_tracing() {}
/*      */ 
/*      */   
/*      */   public final void disable_tracing() {}
/*      */   
/*      */   private final void jj_rescan_token() {
/* 3286 */     this.jj_rescan = true;
/* 3287 */     for (int i = 0; i < 8; ) {
/* 3288 */       JJCalls p = this.jj_2_rtns[i];
/*      */       while (true)
/* 3290 */       { if (p.gen > this.jj_gen) {
/* 3291 */           this.jj_la = p.arg; this.jj_lastpos = this.jj_scanpos = p.first;
/* 3292 */           switch (i) { case 0:
/* 3293 */               jj_3_1(); break;
/* 3294 */             case 1: jj_3_2(); break;
/* 3295 */             case 2: jj_3_3(); break;
/* 3296 */             case 3: jj_3_4(); break;
/* 3297 */             case 4: jj_3_5(); break;
/* 3298 */             case 5: jj_3_6(); break;
/* 3299 */             case 6: jj_3_7(); break;
/* 3300 */             case 7: jj_3_8(); break; }
/*      */         
/*      */         } 
/* 3303 */         p = p.next;
/* 3304 */         if (p == null)
/*      */           i++;  } 
/* 3306 */     }  this.jj_rescan = false;
/*      */   }
/*      */   
/*      */   private final void jj_save(int index, int xla) {
/* 3310 */     JJCalls p = this.jj_2_rtns[index];
/* 3311 */     while (p.gen > this.jj_gen) {
/* 3312 */       if (p.next == null) { p = p.next = new JJCalls(); break; }
/* 3313 */        p = p.next;
/*      */     } 
/* 3315 */     p.gen = this.jj_gen + xla - this.jj_la; p.first = this.token; p.arg = xla;
/*      */   }
/*      */   
/*      */   static final class JJCalls {
/*      */     int gen;
/*      */     Token first;
/*      */     int arg;
/*      */     JJCalls next;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\compact\CompactSyntax.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */