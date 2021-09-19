/*     */ package org.kohsuke.rngom.digested;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.kohsuke.rngom.ast.builder.Annotations;
/*     */ import org.kohsuke.rngom.ast.builder.BuildException;
/*     */ import org.kohsuke.rngom.ast.builder.CommentList;
/*     */ import org.kohsuke.rngom.ast.builder.DataPatternBuilder;
/*     */ import org.kohsuke.rngom.ast.builder.ElementAnnotationBuilder;
/*     */ import org.kohsuke.rngom.ast.builder.Grammar;
/*     */ import org.kohsuke.rngom.ast.builder.NameClassBuilder;
/*     */ import org.kohsuke.rngom.ast.builder.SchemaBuilder;
/*     */ import org.kohsuke.rngom.ast.builder.Scope;
/*     */ import org.kohsuke.rngom.ast.om.Location;
/*     */ import org.kohsuke.rngom.ast.om.ParsedElementAnnotation;
/*     */ import org.kohsuke.rngom.ast.om.ParsedNameClass;
/*     */ import org.kohsuke.rngom.ast.om.ParsedPattern;
/*     */ import org.kohsuke.rngom.ast.util.LocatorImpl;
/*     */ import org.kohsuke.rngom.nc.NameClass;
/*     */ import org.kohsuke.rngom.nc.NameClassBuilderImpl;
/*     */ import org.kohsuke.rngom.parse.Context;
/*     */ import org.kohsuke.rngom.parse.IllegalSchemaException;
/*     */ import org.kohsuke.rngom.parse.Parseable;
/*     */ import org.w3c.dom.Document;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public class DSchemaBuilderImpl
/*     */   implements SchemaBuilder<NameClass, DPattern, ElementWrapper, LocatorImpl, Annotation, CommentListImpl>
/*     */ {
/*  31 */   private final NameClassBuilder ncb = (NameClassBuilder)new NameClassBuilderImpl();
/*     */ 
/*     */   
/*     */   private final Document dom;
/*     */ 
/*     */ 
/*     */   
/*     */   public DSchemaBuilderImpl() {
/*     */     try {
/*  40 */       DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/*  41 */       dbf.setNamespaceAware(true);
/*  42 */       this.dom = dbf.newDocumentBuilder().newDocument();
/*  43 */     } catch (ParserConfigurationException e) {
/*     */       
/*  45 */       throw new InternalError(e.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public NameClassBuilder getNameClassBuilder() throws BuildException {
/*  50 */     return this.ncb;
/*     */   }
/*     */   
/*     */   static DPattern wrap(DPattern p, LocatorImpl loc, Annotation anno) {
/*  54 */     p.location = (Locator)loc;
/*  55 */     if (anno != null)
/*  56 */       p.annotation = anno.getResult(); 
/*  57 */     return p;
/*     */   }
/*     */   
/*     */   static DContainerPattern addAll(DContainerPattern parent, List<DPattern> children) {
/*  61 */     for (DPattern c : children)
/*  62 */       parent.add(c); 
/*  63 */     return parent;
/*     */   }
/*     */   
/*     */   static DUnaryPattern addBody(DUnaryPattern parent, ParsedPattern _body, LocatorImpl loc) {
/*  67 */     parent.setChild((DPattern)_body);
/*  68 */     return parent;
/*     */   }
/*     */   
/*     */   public DPattern makeChoice(List<DPattern> patterns, LocatorImpl loc, Annotation anno) throws BuildException {
/*  72 */     return wrap(addAll(new DChoicePattern(), patterns), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeInterleave(List<DPattern> patterns, LocatorImpl loc, Annotation anno) throws BuildException {
/*  76 */     return wrap(addAll(new DInterleavePattern(), patterns), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeGroup(List<DPattern> patterns, LocatorImpl loc, Annotation anno) throws BuildException {
/*  80 */     return wrap(addAll(new DGroupPattern(), patterns), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeOneOrMore(DPattern p, LocatorImpl loc, Annotation anno) throws BuildException {
/*  84 */     return wrap(addBody(new DOneOrMorePattern(), p, loc), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeZeroOrMore(DPattern p, LocatorImpl loc, Annotation anno) throws BuildException {
/*  88 */     return wrap(addBody(new DZeroOrMorePattern(), p, loc), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeOptional(DPattern p, LocatorImpl loc, Annotation anno) throws BuildException {
/*  92 */     return wrap(addBody(new DOptionalPattern(), p, loc), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeList(DPattern p, LocatorImpl loc, Annotation anno) throws BuildException {
/*  96 */     return wrap(addBody(new DListPattern(), p, loc), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeMixed(DPattern p, LocatorImpl loc, Annotation anno) throws BuildException {
/* 100 */     return wrap(addBody(new DMixedPattern(), p, loc), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeEmpty(LocatorImpl loc, Annotation anno) {
/* 104 */     return wrap(new DEmptyPattern(), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeNotAllowed(LocatorImpl loc, Annotation anno) {
/* 108 */     return wrap(new DNotAllowedPattern(), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeText(LocatorImpl loc, Annotation anno) {
/* 112 */     return wrap(new DTextPattern(), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeAttribute(NameClass nc, DPattern p, LocatorImpl loc, Annotation anno) throws BuildException {
/* 116 */     return wrap(addBody(new DAttributePattern(nc), p, loc), loc, anno);
/*     */   }
/*     */   
/*     */   public DPattern makeElement(NameClass nc, DPattern p, LocatorImpl loc, Annotation anno) throws BuildException {
/* 120 */     return wrap(addBody(new DElementPattern(nc), p, loc), loc, anno);
/*     */   }
/*     */   
/*     */   public DataPatternBuilder makeDataPatternBuilder(String datatypeLibrary, String type, LocatorImpl loc) throws BuildException {
/* 124 */     return new DataPatternBuilderImpl(datatypeLibrary, type, (Location)loc);
/*     */   }
/*     */   
/*     */   public DPattern makeValue(String datatypeLibrary, String type, String value, Context c, String ns, LocatorImpl loc, Annotation anno) throws BuildException {
/* 128 */     return wrap(new DValuePattern(datatypeLibrary, type, value, c.copy(), ns), loc, anno);
/*     */   }
/*     */   
/*     */   public Grammar makeGrammar(Scope parent) {
/* 132 */     return new GrammarBuilderImpl(new DGrammarPattern(), parent, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public DPattern annotate(DPattern p, Annotation anno) throws BuildException {
/* 137 */     return p;
/*     */   }
/*     */ 
/*     */   
/*     */   public DPattern annotateAfter(DPattern p, ElementWrapper e) throws BuildException {
/* 142 */     return p;
/*     */   }
/*     */ 
/*     */   
/*     */   public DPattern commentAfter(DPattern p, CommentListImpl comments) throws BuildException {
/* 147 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DPattern makeExternalRef(Parseable current, String uri, String ns, Scope<DPattern, ElementWrapper, LocatorImpl, Annotation, CommentListImpl> scope, LocatorImpl loc, Annotation anno) throws BuildException, IllegalSchemaException {
/* 153 */     return null;
/*     */   }
/*     */   
/*     */   public LocatorImpl makeLocation(String systemId, int lineNumber, int columnNumber) {
/* 157 */     return new LocatorImpl(systemId, lineNumber, columnNumber);
/*     */   }
/*     */   
/*     */   public Annotation makeAnnotations(CommentListImpl comments, Context context) {
/* 161 */     return new Annotation();
/*     */   }
/*     */   
/*     */   public ElementAnnotationBuilder makeElementAnnotationBuilder(String ns, String localName, String prefix, LocatorImpl loc, CommentListImpl comments, Context context) {
/*     */     String qname;
/* 166 */     if (prefix == null) {
/* 167 */       qname = localName;
/*     */     } else {
/* 169 */       qname = prefix + ':' + localName;
/* 170 */     }  return new ElementAnnotationBuilderImpl(this.dom.createElementNS(ns, qname));
/*     */   }
/*     */   
/*     */   public CommentListImpl makeCommentList() {
/* 174 */     return null;
/*     */   }
/*     */   
/*     */   public DPattern makeErrorPattern() {
/* 178 */     return new DNotAllowedPattern();
/*     */   }
/*     */   
/*     */   public boolean usesComments() {
/* 182 */     return false;
/*     */   }
/*     */   
/*     */   public DPattern expandPattern(DPattern p) throws BuildException, IllegalSchemaException {
/* 186 */     return p;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DSchemaBuilderImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */