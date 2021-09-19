/*    */ package org.kohsuke.rngom.parse.host;
/*    */ 
/*    */ import org.kohsuke.rngom.ast.builder.Annotations;
/*    */ import org.kohsuke.rngom.ast.builder.BuildException;
/*    */ import org.kohsuke.rngom.ast.builder.GrammarSection;
/*    */ import org.kohsuke.rngom.ast.builder.Include;
/*    */ import org.kohsuke.rngom.ast.om.Location;
/*    */ import org.kohsuke.rngom.parse.IllegalSchemaException;
/*    */ import org.kohsuke.rngom.parse.Parseable;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IncludeHost
/*    */   extends GrammarSectionHost
/*    */   implements Include
/*    */ {
/*    */   private final Include lhs;
/*    */   private final Include rhs;
/*    */   
/*    */   IncludeHost(Include lhs, Include rhs) {
/* 21 */     super((GrammarSection)lhs, (GrammarSection)rhs);
/* 22 */     this.lhs = lhs;
/* 23 */     this.rhs = rhs;
/*    */   }
/*    */   
/*    */   public void endInclude(Parseable current, String uri, String ns, Location _loc, Annotations _anno) throws BuildException, IllegalSchemaException {
/* 27 */     LocationHost loc = cast(_loc);
/* 28 */     AnnotationsHost anno = cast(_anno);
/*    */     
/* 30 */     this.lhs.endInclude(current, uri, ns, loc.lhs, anno.lhs);
/* 31 */     this.rhs.endInclude(current, uri, ns, loc.rhs, anno.rhs);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\host\IncludeHost.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */