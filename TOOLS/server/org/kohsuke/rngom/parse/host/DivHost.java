/*    */ package org.kohsuke.rngom.parse.host;
/*    */ 
/*    */ import org.kohsuke.rngom.ast.builder.Annotations;
/*    */ import org.kohsuke.rngom.ast.builder.BuildException;
/*    */ import org.kohsuke.rngom.ast.builder.Div;
/*    */ import org.kohsuke.rngom.ast.builder.GrammarSection;
/*    */ import org.kohsuke.rngom.ast.om.Location;
/*    */ 
/*    */ 
/*    */ public class DivHost
/*    */   extends GrammarSectionHost
/*    */   implements Div
/*    */ {
/*    */   private final Div lhs;
/*    */   private final Div rhs;
/*    */   
/*    */   DivHost(Div lhs, Div rhs) {
/* 18 */     super((GrammarSection)lhs, (GrammarSection)rhs);
/* 19 */     this.lhs = lhs;
/* 20 */     this.rhs = rhs;
/*    */   }
/*    */   
/*    */   public void endDiv(Location _loc, Annotations _anno) throws BuildException {
/* 24 */     LocationHost loc = cast(_loc);
/* 25 */     AnnotationsHost anno = cast(_anno);
/*    */     
/* 27 */     this.lhs.endDiv(loc.lhs, anno.lhs);
/* 28 */     this.rhs.endDiv(loc.rhs, anno.rhs);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\host\DivHost.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */