/*    */ package org.kohsuke.rngom.parse.host;
/*    */ 
/*    */ import org.kohsuke.rngom.ast.builder.Annotations;
/*    */ import org.kohsuke.rngom.ast.builder.BuildException;
/*    */ import org.kohsuke.rngom.ast.builder.Grammar;
/*    */ import org.kohsuke.rngom.ast.builder.Scope;
/*    */ import org.kohsuke.rngom.ast.om.Location;
/*    */ import org.kohsuke.rngom.ast.om.ParsedPattern;
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
/*    */ public class GrammarHost
/*    */   extends ScopeHost
/*    */   implements Grammar
/*    */ {
/*    */   final Grammar lhs;
/*    */   final Grammar rhs;
/*    */   
/*    */   public GrammarHost(Grammar lhs, Grammar rhs) {
/* 27 */     super((Scope)lhs, (Scope)rhs);
/* 28 */     this.lhs = lhs;
/* 29 */     this.rhs = rhs;
/*    */   }
/*    */   
/*    */   public ParsedPattern endGrammar(Location _loc, Annotations _anno) throws BuildException {
/* 33 */     LocationHost loc = cast(_loc);
/* 34 */     AnnotationsHost anno = cast(_anno);
/*    */     
/* 36 */     return new ParsedPatternHost(this.lhs.endGrammar(loc.lhs, anno.lhs), this.rhs.endGrammar(loc.rhs, anno.rhs));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\host\GrammarHost.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */