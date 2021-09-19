/*     */ package org.kohsuke.rngom.digested;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.kohsuke.rngom.ast.builder.Annotations;
/*     */ import org.kohsuke.rngom.ast.builder.BuildException;
/*     */ import org.kohsuke.rngom.ast.builder.IncludedGrammar;
/*     */ import org.kohsuke.rngom.ast.builder.SchemaBuilder;
/*     */ import org.kohsuke.rngom.ast.builder.Scope;
/*     */ import org.kohsuke.rngom.ast.om.Location;
/*     */ import org.kohsuke.rngom.ast.om.ParsedNameClass;
/*     */ import org.kohsuke.rngom.ast.om.ParsedPattern;
/*     */ import org.kohsuke.rngom.nc.NameClass;
/*     */ import org.kohsuke.rngom.parse.Parseable;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class PatternParseable
/*     */   implements Parseable
/*     */ {
/*     */   private final DPattern pattern;
/*     */   
/*     */   public PatternParseable(DPattern p) {
/*  26 */     this.pattern = p;
/*     */   }
/*     */   
/*     */   public ParsedPattern parse(SchemaBuilder sb) throws BuildException {
/*  30 */     return this.pattern.<ParsedPattern>accept(new Parser(sb));
/*     */   }
/*     */   
/*     */   public ParsedPattern parseInclude(String uri, SchemaBuilder f, IncludedGrammar g, String inheritedNs) throws BuildException {
/*  34 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public ParsedPattern parseExternal(String uri, SchemaBuilder f, Scope s, String inheritedNs) throws BuildException {
/*  38 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   private static class Parser
/*     */     implements DPatternVisitor<ParsedPattern> {
/*     */     private final SchemaBuilder sb;
/*     */     
/*     */     public Parser(SchemaBuilder sb) {
/*  46 */       this.sb = sb;
/*     */     }
/*     */ 
/*     */     
/*     */     private Annotations parseAnnotation(DPattern p) {
/*  51 */       return null;
/*     */     }
/*     */     
/*     */     private Location parseLocation(DPattern p) {
/*  55 */       Locator l = p.getLocation();
/*  56 */       return this.sb.makeLocation(l.getSystemId(), l.getLineNumber(), l.getColumnNumber());
/*     */     }
/*     */ 
/*     */     
/*     */     private ParsedNameClass parseNameClass(NameClass name) {
/*  61 */       return (ParsedNameClass)name;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ParsedPattern onAttribute(DAttributePattern p) {
/*  67 */       return this.sb.makeAttribute(parseNameClass(p.getName()), p.getChild().<ParsedPattern>accept(this), parseLocation(p), parseAnnotation(p));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ParsedPattern onChoice(DChoicePattern p) {
/*  75 */       List<ParsedPattern> kids = new ArrayList<ParsedPattern>();
/*  76 */       for (DPattern c = p.firstChild(); c != null; c = c.next)
/*  77 */         kids.add(c.<ParsedPattern>accept(this)); 
/*  78 */       return this.sb.makeChoice(kids, parseLocation(p), null);
/*     */     }
/*     */ 
/*     */     
/*     */     public ParsedPattern onData(DDataPattern p) {
/*  83 */       return null;
/*     */     }
/*     */     
/*     */     public ParsedPattern onElement(DElementPattern p) {
/*  87 */       return this.sb.makeElement(parseNameClass(p.getName()), p.getChild().<ParsedPattern>accept(this), parseLocation(p), parseAnnotation(p));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ParsedPattern onEmpty(DEmptyPattern p) {
/*  95 */       return this.sb.makeEmpty(parseLocation(p), parseAnnotation(p));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ParsedPattern onGrammar(DGrammarPattern p) {
/* 102 */       return null;
/*     */     }
/*     */     
/*     */     public ParsedPattern onGroup(DGroupPattern p) {
/* 106 */       List<ParsedPattern> kids = new ArrayList<ParsedPattern>();
/* 107 */       for (DPattern c = p.firstChild(); c != null; c = c.next)
/* 108 */         kids.add(c.<ParsedPattern>accept(this)); 
/* 109 */       return this.sb.makeGroup(kids, parseLocation(p), null);
/*     */     }
/*     */     
/*     */     public ParsedPattern onInterleave(DInterleavePattern p) {
/* 113 */       List<ParsedPattern> kids = new ArrayList<ParsedPattern>();
/* 114 */       for (DPattern c = p.firstChild(); c != null; c = c.next)
/* 115 */         kids.add(c.<ParsedPattern>accept(this)); 
/* 116 */       return this.sb.makeInterleave(kids, parseLocation(p), null);
/*     */     }
/*     */     
/*     */     public ParsedPattern onList(DListPattern p) {
/* 120 */       return this.sb.makeList(p.getChild().<ParsedPattern>accept(this), parseLocation(p), parseAnnotation(p));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ParsedPattern onMixed(DMixedPattern p) {
/* 127 */       return this.sb.makeMixed(p.getChild().<ParsedPattern>accept(this), parseLocation(p), parseAnnotation(p));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ParsedPattern onNotAllowed(DNotAllowedPattern p) {
/* 134 */       return this.sb.makeNotAllowed(parseLocation(p), parseAnnotation(p));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ParsedPattern onOneOrMore(DOneOrMorePattern p) {
/* 140 */       return this.sb.makeOneOrMore(p.getChild().<ParsedPattern>accept(this), parseLocation(p), parseAnnotation(p));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ParsedPattern onOptional(DOptionalPattern p) {
/* 147 */       return this.sb.makeOptional(p.getChild().<ParsedPattern>accept(this), parseLocation(p), parseAnnotation(p));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ParsedPattern onRef(DRefPattern p) {
/* 155 */       return null;
/*     */     }
/*     */     
/*     */     public ParsedPattern onText(DTextPattern p) {
/* 159 */       return this.sb.makeText(parseLocation(p), parseAnnotation(p));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ParsedPattern onValue(DValuePattern p) {
/* 165 */       return this.sb.makeValue(p.getDatatypeLibrary(), p.getType(), p.getValue(), p.getContext(), p.getNs(), parseLocation(p), parseAnnotation(p));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ParsedPattern onZeroOrMore(DZeroOrMorePattern p) {
/* 176 */       return this.sb.makeZeroOrMore(p.getChild().<ParsedPattern>accept(this), parseLocation(p), parseAnnotation(p));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\PatternParseable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */