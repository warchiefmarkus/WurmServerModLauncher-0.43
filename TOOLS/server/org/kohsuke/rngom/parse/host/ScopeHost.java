/*    */ package org.kohsuke.rngom.parse.host;
/*    */ 
/*    */ import org.kohsuke.rngom.ast.builder.Annotations;
/*    */ import org.kohsuke.rngom.ast.builder.BuildException;
/*    */ import org.kohsuke.rngom.ast.builder.GrammarSection;
/*    */ import org.kohsuke.rngom.ast.builder.Scope;
/*    */ import org.kohsuke.rngom.ast.om.Location;
/*    */ import org.kohsuke.rngom.ast.om.ParsedPattern;
/*    */ 
/*    */ 
/*    */ public class ScopeHost
/*    */   extends GrammarSectionHost
/*    */   implements Scope
/*    */ {
/*    */   protected final Scope lhs;
/*    */   protected final Scope rhs;
/*    */   
/*    */   protected ScopeHost(Scope lhs, Scope rhs) {
/* 19 */     super((GrammarSection)lhs, (GrammarSection)rhs);
/* 20 */     this.lhs = lhs;
/* 21 */     this.rhs = rhs;
/*    */   }
/*    */   
/*    */   public ParsedPattern makeParentRef(String name, Location _loc, Annotations _anno) throws BuildException {
/* 25 */     LocationHost loc = cast(_loc);
/* 26 */     AnnotationsHost anno = cast(_anno);
/*    */     
/* 28 */     return new ParsedPatternHost(this.lhs.makeParentRef(name, loc.lhs, anno.lhs), this.rhs.makeParentRef(name, loc.rhs, anno.rhs));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ParsedPattern makeRef(String name, Location _loc, Annotations _anno) throws BuildException {
/* 34 */     LocationHost loc = cast(_loc);
/* 35 */     AnnotationsHost anno = cast(_anno);
/*    */     
/* 37 */     return new ParsedPatternHost(this.lhs.makeRef(name, loc.lhs, anno.lhs), this.rhs.makeRef(name, loc.rhs, anno.rhs));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\host\ScopeHost.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */