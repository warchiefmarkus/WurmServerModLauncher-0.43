/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.kohsuke.rngom.ast.builder.Annotations;
/*    */ import org.kohsuke.rngom.ast.builder.BuildException;
/*    */ import org.kohsuke.rngom.ast.builder.GrammarSection;
/*    */ import org.kohsuke.rngom.ast.builder.Include;
/*    */ import org.kohsuke.rngom.ast.builder.IncludedGrammar;
/*    */ import org.kohsuke.rngom.ast.builder.Scope;
/*    */ import org.kohsuke.rngom.ast.om.Location;
/*    */ import org.kohsuke.rngom.ast.om.ParsedPattern;
/*    */ import org.kohsuke.rngom.parse.IllegalSchemaException;
/*    */ import org.kohsuke.rngom.parse.Parseable;
/*    */ 
/*    */ 
/*    */ final class IncludeImpl
/*    */   extends GrammarBuilderImpl
/*    */   implements Include
/*    */ {
/* 21 */   private Set overridenPatterns = new HashSet();
/*    */   private boolean startOverriden = false;
/*    */   
/*    */   public IncludeImpl(DGrammarPattern p, Scope parent, DSchemaBuilderImpl sb) {
/* 25 */     super(p, parent, sb);
/*    */   }
/*    */   
/*    */   public void define(String name, GrammarSection.Combine combine, ParsedPattern pattern, Location loc, Annotations anno) throws BuildException {
/* 29 */     super.define(name, combine, pattern, loc, anno);
/* 30 */     if (name == "\000#start\000") {
/* 31 */       this.startOverriden = true;
/*    */     } else {
/* 33 */       this.overridenPatterns.add(name);
/*    */     } 
/*    */   }
/*    */   public void endInclude(Parseable current, String uri, String ns, Location loc, Annotations anno) throws BuildException, IllegalSchemaException {
/* 37 */     current.parseInclude(uri, this.sb, new IncludedGrammarImpl(this.grammar, this.parent, this.sb), ns);
/*    */   }
/*    */   
/*    */   private class IncludedGrammarImpl extends GrammarBuilderImpl implements IncludedGrammar {
/*    */     public IncludedGrammarImpl(DGrammarPattern p, Scope parent, DSchemaBuilderImpl sb) {
/* 42 */       super(p, parent, sb);
/*    */     }
/*    */ 
/*    */     
/*    */     public void define(String name, GrammarSection.Combine combine, ParsedPattern pattern, Location loc, Annotations anno) throws BuildException {
/* 47 */       if (name == "\000#start\000") {
/* 48 */         if (IncludeImpl.this.startOverriden) {
/*    */           return;
/*    */         }
/* 51 */       } else if (IncludeImpl.this.overridenPatterns.contains(name)) {
/*    */         return;
/*    */       } 
/*    */       
/* 55 */       super.define(name, combine, pattern, loc, anno);
/*    */     }
/*    */     
/*    */     public ParsedPattern endIncludedGrammar(Location loc, Annotations anno) throws BuildException {
/* 59 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\IncludeImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */