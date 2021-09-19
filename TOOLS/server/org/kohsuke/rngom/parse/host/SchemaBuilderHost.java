/*     */ package org.kohsuke.rngom.parse.host;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ import org.kohsuke.rngom.parse.Context;
/*     */ import org.kohsuke.rngom.parse.IllegalSchemaException;
/*     */ import org.kohsuke.rngom.parse.Parseable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SchemaBuilderHost
/*     */   extends Base
/*     */   implements SchemaBuilder
/*     */ {
/*     */   final SchemaBuilder lhs;
/*     */   final SchemaBuilder rhs;
/*     */   
/*     */   public SchemaBuilderHost(SchemaBuilder lhs, SchemaBuilder rhs) {
/*  33 */     this.lhs = lhs;
/*  34 */     this.rhs = rhs;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern annotate(ParsedPattern _p, Annotations _anno) throws BuildException {
/*  40 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/*  41 */     AnnotationsHost a = cast(_anno);
/*     */     
/*  43 */     return new ParsedPatternHost(this.lhs.annotate(p.lhs, a.lhs), this.rhs.annotate(p.lhs, a.lhs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern annotateAfter(ParsedPattern _p, ParsedElementAnnotation _e) throws BuildException {
/*  51 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/*  52 */     ParsedElementAnnotationHost e = (ParsedElementAnnotationHost)_e;
/*  53 */     return new ParsedPatternHost(this.lhs.annotateAfter(p.lhs, e.lhs), this.rhs.annotateAfter(p.rhs, e.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern commentAfter(ParsedPattern _p, CommentList _comments) throws BuildException {
/*  61 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/*  62 */     CommentListHost comments = (CommentListHost)_comments;
/*     */     
/*  64 */     return new ParsedPatternHost(this.lhs.commentAfter(p.lhs, (comments == null) ? null : comments.lhs), this.rhs.commentAfter(p.rhs, (comments == null) ? null : comments.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern expandPattern(ParsedPattern _p) throws BuildException, IllegalSchemaException {
/*  70 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/*  71 */     return new ParsedPatternHost(this.lhs.expandPattern(p.lhs), this.rhs.expandPattern(p.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NameClassBuilder getNameClassBuilder() throws BuildException {
/*  77 */     return new NameClassBuilderHost(this.lhs.getNameClassBuilder(), this.rhs.getNameClassBuilder());
/*     */   }
/*     */   
/*     */   public Annotations makeAnnotations(CommentList _comments, Context context) {
/*  81 */     CommentListHost comments = (CommentListHost)_comments;
/*  82 */     Annotations l = this.lhs.makeAnnotations((comments != null) ? comments.lhs : null, context);
/*  83 */     Annotations r = this.rhs.makeAnnotations((comments != null) ? comments.rhs : null, context);
/*  84 */     if (l == null || r == null)
/*  85 */       throw new IllegalArgumentException("annotations cannot be null"); 
/*  86 */     return new AnnotationsHost(l, r);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeAttribute(ParsedNameClass _nc, ParsedPattern _p, Location _loc, Annotations _anno) throws BuildException {
/*  92 */     ParsedNameClassHost nc = (ParsedNameClassHost)_nc;
/*  93 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/*  94 */     LocationHost loc = cast(_loc);
/*  95 */     AnnotationsHost anno = cast(_anno);
/*     */     
/*  97 */     return new ParsedPatternHost(this.lhs.makeAttribute(nc.lhs, p.lhs, loc.lhs, anno.lhs), this.rhs.makeAttribute(nc.rhs, p.rhs, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeChoice(List patterns, Location _loc, Annotations _anno) throws BuildException {
/* 105 */     List<ParsedPattern> lp = new ArrayList<ParsedPattern>();
/* 106 */     List<ParsedPattern> rp = new ArrayList<ParsedPattern>();
/* 107 */     for (int i = 0; i < patterns.size(); i++) {
/* 108 */       lp.add(((ParsedPatternHost)patterns.get(i)).lhs);
/* 109 */       rp.add(((ParsedPatternHost)patterns.get(i)).rhs);
/*     */     } 
/* 111 */     LocationHost loc = cast(_loc);
/* 112 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 114 */     return new ParsedPatternHost(this.lhs.makeChoice(lp, loc.lhs, anno.lhs), this.rhs.makeChoice(rp, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CommentList makeCommentList() {
/* 120 */     return new CommentListHost(this.lhs.makeCommentList(), this.rhs.makeCommentList());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DataPatternBuilder makeDataPatternBuilder(String datatypeLibrary, String type, Location _loc) throws BuildException {
/* 127 */     LocationHost loc = cast(_loc);
/*     */     
/* 129 */     return new DataPatternBuilderHost(this.lhs.makeDataPatternBuilder(datatypeLibrary, type, loc.lhs), this.rhs.makeDataPatternBuilder(datatypeLibrary, type, loc.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeElement(ParsedNameClass _nc, ParsedPattern _p, Location _loc, Annotations _anno) throws BuildException {
/* 137 */     ParsedNameClassHost nc = (ParsedNameClassHost)_nc;
/* 138 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/* 139 */     LocationHost loc = cast(_loc);
/* 140 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 142 */     return new ParsedPatternHost(this.lhs.makeElement(nc.lhs, p.lhs, loc.lhs, anno.lhs), this.rhs.makeElement(nc.rhs, p.rhs, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ElementAnnotationBuilder makeElementAnnotationBuilder(String ns, String localName, String prefix, Location _loc, CommentList _comments, Context context) {
/* 150 */     LocationHost loc = cast(_loc);
/* 151 */     CommentListHost comments = (CommentListHost)_comments;
/*     */     
/* 153 */     return new ElementAnnotationBuilderHost(this.lhs.makeElementAnnotationBuilder(ns, localName, prefix, loc.lhs, (comments == null) ? null : comments.lhs, context), this.rhs.makeElementAnnotationBuilder(ns, localName, prefix, loc.rhs, (comments == null) ? null : comments.rhs, context));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeEmpty(Location _loc, Annotations _anno) {
/* 159 */     LocationHost loc = cast(_loc);
/* 160 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 162 */     return new ParsedPatternHost(this.lhs.makeEmpty(loc.lhs, anno.lhs), this.rhs.makeEmpty(loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeErrorPattern() {
/* 168 */     return new ParsedPatternHost(this.lhs.makeErrorPattern(), this.rhs.makeErrorPattern());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeExternalRef(Parseable current, String uri, String ns, Scope _scope, Location _loc, Annotations _anno) throws BuildException, IllegalSchemaException {
/* 177 */     ScopeHost scope = (ScopeHost)_scope;
/* 178 */     LocationHost loc = cast(_loc);
/* 179 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 181 */     return new ParsedPatternHost(this.lhs.makeExternalRef(current, uri, ns, scope.lhs, loc.lhs, anno.lhs), this.rhs.makeExternalRef(current, uri, ns, scope.rhs, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Grammar makeGrammar(Scope _parent) {
/* 187 */     ScopeHost parent = (ScopeHost)_parent;
/*     */     
/* 189 */     return new GrammarHost(this.lhs.makeGrammar((parent != null) ? parent.lhs : null), this.rhs.makeGrammar((parent != null) ? parent.rhs : null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeGroup(List patterns, Location _loc, Annotations _anno) throws BuildException {
/* 197 */     List<ParsedPattern> lp = new ArrayList<ParsedPattern>();
/* 198 */     List<ParsedPattern> rp = new ArrayList<ParsedPattern>();
/* 199 */     for (int i = 0; i < patterns.size(); i++) {
/* 200 */       lp.add(((ParsedPatternHost)patterns.get(i)).lhs);
/* 201 */       rp.add(((ParsedPatternHost)patterns.get(i)).rhs);
/*     */     } 
/* 203 */     LocationHost loc = cast(_loc);
/* 204 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 206 */     return new ParsedPatternHost(this.lhs.makeGroup(lp, loc.lhs, anno.lhs), this.rhs.makeGroup(rp, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeInterleave(List patterns, Location _loc, Annotations _anno) throws BuildException {
/* 214 */     List<ParsedPattern> lp = new ArrayList<ParsedPattern>();
/* 215 */     List<ParsedPattern> rp = new ArrayList<ParsedPattern>();
/* 216 */     for (int i = 0; i < patterns.size(); i++) {
/* 217 */       lp.add(((ParsedPatternHost)patterns.get(i)).lhs);
/* 218 */       rp.add(((ParsedPatternHost)patterns.get(i)).rhs);
/*     */     } 
/* 220 */     LocationHost loc = cast(_loc);
/* 221 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 223 */     return new ParsedPatternHost(this.lhs.makeInterleave(lp, loc.lhs, anno.lhs), this.rhs.makeInterleave(rp, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeList(ParsedPattern _p, Location _loc, Annotations _anno) throws BuildException {
/* 231 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/* 232 */     LocationHost loc = cast(_loc);
/* 233 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 235 */     return new ParsedPatternHost(this.lhs.makeList(p.lhs, loc.lhs, anno.lhs), this.rhs.makeList(p.rhs, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Location makeLocation(String systemId, int lineNumber, int columnNumber) {
/* 242 */     return new LocationHost(this.lhs.makeLocation(systemId, lineNumber, columnNumber), this.rhs.makeLocation(systemId, lineNumber, columnNumber));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeMixed(ParsedPattern _p, Location _loc, Annotations _anno) throws BuildException {
/* 250 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/* 251 */     LocationHost loc = cast(_loc);
/* 252 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 254 */     return new ParsedPatternHost(this.lhs.makeMixed(p.lhs, loc.lhs, anno.lhs), this.rhs.makeMixed(p.rhs, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeNotAllowed(Location _loc, Annotations _anno) {
/* 260 */     LocationHost loc = cast(_loc);
/* 261 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 263 */     return new ParsedPatternHost(this.lhs.makeNotAllowed(loc.lhs, anno.lhs), this.rhs.makeNotAllowed(loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeOneOrMore(ParsedPattern _p, Location _loc, Annotations _anno) throws BuildException {
/* 271 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/* 272 */     LocationHost loc = cast(_loc);
/* 273 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 275 */     return new ParsedPatternHost(this.lhs.makeOneOrMore(p.lhs, loc.lhs, anno.lhs), this.rhs.makeOneOrMore(p.rhs, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeZeroOrMore(ParsedPattern _p, Location _loc, Annotations _anno) throws BuildException {
/* 283 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/* 284 */     LocationHost loc = cast(_loc);
/* 285 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 287 */     return new ParsedPatternHost(this.lhs.makeZeroOrMore(p.lhs, loc.lhs, anno.lhs), this.rhs.makeZeroOrMore(p.rhs, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeOptional(ParsedPattern _p, Location _loc, Annotations _anno) throws BuildException {
/* 295 */     ParsedPatternHost p = (ParsedPatternHost)_p;
/* 296 */     LocationHost loc = cast(_loc);
/* 297 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 299 */     return new ParsedPatternHost(this.lhs.makeOptional(p.lhs, loc.lhs, anno.lhs), this.rhs.makeOptional(p.rhs, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeText(Location _loc, Annotations _anno) {
/* 305 */     LocationHost loc = cast(_loc);
/* 306 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 308 */     return new ParsedPatternHost(this.lhs.makeText(loc.lhs, anno.lhs), this.rhs.makeText(loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeValue(String datatypeLibrary, String type, String value, Context c, String ns, Location _loc, Annotations _anno) throws BuildException {
/* 316 */     LocationHost loc = cast(_loc);
/* 317 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 319 */     return new ParsedPatternHost(this.lhs.makeValue(datatypeLibrary, type, value, c, ns, loc.lhs, anno.lhs), this.rhs.makeValue(datatypeLibrary, type, value, c, ns, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean usesComments() {
/* 325 */     return (this.lhs.usesComments() || this.rhs.usesComments());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\host\SchemaBuilderHost.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */