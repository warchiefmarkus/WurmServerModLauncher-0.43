/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.kohsuke.rngom.ast.builder.Annotations;
/*    */ import org.kohsuke.rngom.ast.builder.BuildException;
/*    */ import org.kohsuke.rngom.ast.builder.CommentList;
/*    */ import org.kohsuke.rngom.ast.builder.Div;
/*    */ import org.kohsuke.rngom.ast.builder.Grammar;
/*    */ import org.kohsuke.rngom.ast.builder.GrammarSection;
/*    */ import org.kohsuke.rngom.ast.builder.Include;
/*    */ import org.kohsuke.rngom.ast.builder.Scope;
/*    */ import org.kohsuke.rngom.ast.om.Location;
/*    */ import org.kohsuke.rngom.ast.om.ParsedElementAnnotation;
/*    */ import org.kohsuke.rngom.ast.om.ParsedPattern;
/*    */ import org.kohsuke.rngom.ast.util.LocatorImpl;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class GrammarBuilderImpl
/*    */   implements Grammar, Div
/*    */ {
/*    */   protected final DGrammarPattern grammar;
/*    */   protected final Scope parent;
/*    */   protected final DSchemaBuilderImpl sb;
/*    */   private List<Element> additionalElementAnnotations;
/*    */   
/*    */   public GrammarBuilderImpl(DGrammarPattern p, Scope parent, DSchemaBuilderImpl sb) {
/* 37 */     this.grammar = p;
/* 38 */     this.parent = parent;
/* 39 */     this.sb = sb;
/*    */   }
/*    */   
/*    */   public ParsedPattern endGrammar(Location loc, Annotations anno) throws BuildException {
/* 43 */     if (anno != null)
/* 44 */       this.grammar.annotation = ((Annotation)anno).getResult(); 
/* 45 */     if (this.additionalElementAnnotations != null) {
/* 46 */       if (this.grammar.annotation == null)
/* 47 */         this.grammar.annotation = new DAnnotation(); 
/* 48 */       this.grammar.annotation.contents.addAll(this.additionalElementAnnotations);
/*    */     } 
/* 50 */     return this.grammar;
/*    */   }
/*    */ 
/*    */   
/*    */   public void endDiv(Location loc, Annotations anno) throws BuildException {}
/*    */   
/*    */   public void define(String name, GrammarSection.Combine combine, ParsedPattern pattern, Location loc, Annotations anno) throws BuildException {
/* 57 */     if (name == "\000#start\000") {
/* 58 */       this.grammar.start = (DPattern)pattern;
/*    */     } else {
/*    */       
/* 61 */       DDefine d = this.grammar.getOrAdd(name);
/* 62 */       d.setPattern((DPattern)pattern);
/* 63 */       if (anno != null)
/* 64 */         d.annotation = ((Annotation)anno).getResult(); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void topLevelAnnotation(ParsedElementAnnotation ea) throws BuildException {
/* 69 */     if (this.additionalElementAnnotations == null)
/* 70 */       this.additionalElementAnnotations = new ArrayList<Element>(); 
/* 71 */     this.additionalElementAnnotations.add(((ElementWrapper)ea).element);
/*    */   }
/*    */ 
/*    */   
/*    */   public void topLevelComment(CommentList comments) throws BuildException {}
/*    */   
/*    */   public Div makeDiv() {
/* 78 */     return this;
/*    */   }
/*    */   
/*    */   public Include makeInclude() {
/* 82 */     return new IncludeImpl(this.grammar, this.parent, this.sb);
/*    */   }
/*    */   
/*    */   public ParsedPattern makeParentRef(String name, Location loc, Annotations anno) throws BuildException {
/* 86 */     return this.parent.makeRef(name, loc, anno);
/*    */   }
/*    */   
/*    */   public ParsedPattern makeRef(String name, Location loc, Annotations anno) throws BuildException {
/* 90 */     return DSchemaBuilderImpl.wrap(new DRefPattern(this.grammar.getOrAdd(name)), (LocatorImpl)loc, (Annotation)anno);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\GrammarBuilderImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */