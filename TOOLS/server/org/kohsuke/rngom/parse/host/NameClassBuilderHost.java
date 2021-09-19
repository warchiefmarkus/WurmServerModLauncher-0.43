/*     */ package org.kohsuke.rngom.parse.host;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.kohsuke.rngom.ast.builder.Annotations;
/*     */ import org.kohsuke.rngom.ast.builder.BuildException;
/*     */ import org.kohsuke.rngom.ast.builder.CommentList;
/*     */ import org.kohsuke.rngom.ast.builder.NameClassBuilder;
/*     */ import org.kohsuke.rngom.ast.om.Location;
/*     */ import org.kohsuke.rngom.ast.om.ParsedElementAnnotation;
/*     */ import org.kohsuke.rngom.ast.om.ParsedNameClass;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class NameClassBuilderHost
/*     */   extends Base
/*     */   implements NameClassBuilder
/*     */ {
/*     */   final NameClassBuilder lhs;
/*     */   final NameClassBuilder rhs;
/*     */   
/*     */   NameClassBuilderHost(NameClassBuilder lhs, NameClassBuilder rhs) {
/*  24 */     this.lhs = lhs;
/*  25 */     this.rhs = rhs;
/*     */   }
/*     */   
/*     */   public ParsedNameClass annotate(ParsedNameClass _nc, Annotations _anno) throws BuildException {
/*  29 */     ParsedNameClassHost nc = (ParsedNameClassHost)_nc;
/*  30 */     AnnotationsHost anno = cast(_anno);
/*     */     
/*  32 */     return new ParsedNameClassHost(this.lhs.annotate(nc.lhs, anno.lhs), this.rhs.annotate(nc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedNameClass annotateAfter(ParsedNameClass _nc, ParsedElementAnnotation _e) throws BuildException {
/*  38 */     ParsedNameClassHost nc = (ParsedNameClassHost)_nc;
/*  39 */     ParsedElementAnnotationHost e = (ParsedElementAnnotationHost)_e;
/*     */     
/*  41 */     return new ParsedNameClassHost(this.lhs.annotateAfter(nc.lhs, e.lhs), this.rhs.annotateAfter(nc.rhs, e.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedNameClass commentAfter(ParsedNameClass _nc, CommentList _comments) throws BuildException {
/*  47 */     ParsedNameClassHost nc = (ParsedNameClassHost)_nc;
/*  48 */     CommentListHost comments = (CommentListHost)_comments;
/*     */     
/*  50 */     return new ParsedNameClassHost(this.lhs.commentAfter(nc.lhs, (comments == null) ? null : comments.lhs), this.rhs.commentAfter(nc.rhs, (comments == null) ? null : comments.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedNameClass makeChoice(List _nameClasses, Location _loc, Annotations _anno) {
/*  56 */     List<ParsedNameClass> lnc = new ArrayList<ParsedNameClass>();
/*  57 */     List<ParsedNameClass> rnc = new ArrayList<ParsedNameClass>();
/*  58 */     for (int i = 0; i < _nameClasses.size(); i++) {
/*  59 */       lnc.add(((ParsedNameClassHost)_nameClasses.get(i)).lhs);
/*  60 */       rnc.add(((ParsedNameClassHost)_nameClasses.get(i)).rhs);
/*     */     } 
/*  62 */     LocationHost loc = cast(_loc);
/*  63 */     AnnotationsHost anno = cast(_anno);
/*     */     
/*  65 */     return new ParsedNameClassHost(this.lhs.makeChoice(lnc, loc.lhs, anno.lhs), this.rhs.makeChoice(rnc, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedNameClass makeName(String ns, String localName, String prefix, Location _loc, Annotations _anno) {
/*  71 */     LocationHost loc = cast(_loc);
/*  72 */     AnnotationsHost anno = cast(_anno);
/*     */     
/*  74 */     return new ParsedNameClassHost(this.lhs.makeName(ns, localName, prefix, loc.lhs, anno.lhs), this.rhs.makeName(ns, localName, prefix, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedNameClass makeNsName(String ns, Location _loc, Annotations _anno) {
/*  80 */     LocationHost loc = cast(_loc);
/*  81 */     AnnotationsHost anno = cast(_anno);
/*     */     
/*  83 */     return new ParsedNameClassHost(this.lhs.makeNsName(ns, loc.lhs, anno.lhs), this.rhs.makeNsName(ns, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedNameClass makeNsName(String ns, ParsedNameClass _except, Location _loc, Annotations _anno) {
/*  89 */     ParsedNameClassHost except = (ParsedNameClassHost)_except;
/*  90 */     LocationHost loc = cast(_loc);
/*  91 */     AnnotationsHost anno = cast(_anno);
/*     */     
/*  93 */     return new ParsedNameClassHost(this.lhs.makeNsName(ns, except.lhs, loc.lhs, anno.lhs), this.rhs.makeNsName(ns, except.rhs, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedNameClass makeAnyName(Location _loc, Annotations _anno) {
/*  99 */     LocationHost loc = cast(_loc);
/* 100 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 102 */     return new ParsedNameClassHost(this.lhs.makeAnyName(loc.lhs, anno.lhs), this.rhs.makeAnyName(loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedNameClass makeAnyName(ParsedNameClass _except, Location _loc, Annotations _anno) {
/* 108 */     ParsedNameClassHost except = (ParsedNameClassHost)_except;
/* 109 */     LocationHost loc = cast(_loc);
/* 110 */     AnnotationsHost anno = cast(_anno);
/*     */     
/* 112 */     return new ParsedNameClassHost(this.lhs.makeAnyName(except.lhs, loc.lhs, anno.lhs), this.rhs.makeAnyName(except.rhs, loc.rhs, anno.rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedNameClass makeErrorNameClass() {
/* 118 */     return new ParsedNameClassHost(this.lhs.makeErrorNameClass(), this.rhs.makeErrorNameClass());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\host\NameClassBuilderHost.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */