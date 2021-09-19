/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ import org.kohsuke.rngom.ast.builder.Annotations;
/*    */ import org.kohsuke.rngom.ast.builder.BuildException;
/*    */ import org.kohsuke.rngom.ast.builder.DataPatternBuilder;
/*    */ import org.kohsuke.rngom.ast.om.Location;
/*    */ import org.kohsuke.rngom.ast.om.ParsedElementAnnotation;
/*    */ import org.kohsuke.rngom.ast.om.ParsedPattern;
/*    */ import org.kohsuke.rngom.parse.Context;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ final class DataPatternBuilderImpl
/*    */   implements DataPatternBuilder
/*    */ {
/*    */   private final DDataPattern p;
/*    */   
/*    */   public DataPatternBuilderImpl(String datatypeLibrary, String type, Location loc) {
/* 20 */     this.p = new DDataPattern();
/* 21 */     this.p.location = (Locator)loc;
/* 22 */     this.p.datatypeLibrary = datatypeLibrary;
/* 23 */     this.p.type = type;
/*    */   }
/*    */   
/*    */   public void addParam(String name, String value, Context context, String ns, Location loc, Annotations anno) throws BuildException {
/* 27 */     this.p.getClass(); this.p.params.add(new DDataPattern.Param(this.p, name, value, context.copy(), ns, loc, (Annotation)anno));
/*    */   }
/*    */ 
/*    */   
/*    */   public void annotation(ParsedElementAnnotation ea) {}
/*    */ 
/*    */   
/*    */   public ParsedPattern makePattern(Location loc, Annotations anno) throws BuildException {
/* 35 */     return makePattern(null, loc, anno);
/*    */   }
/*    */   
/*    */   public ParsedPattern makePattern(ParsedPattern except, Location loc, Annotations anno) throws BuildException {
/* 39 */     this.p.except = (DPattern)except;
/* 40 */     if (anno != null)
/* 41 */       this.p.annotation = ((Annotation)anno).getResult(); 
/* 42 */     return this.p;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DataPatternBuilderImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */