/*    */ package org.kohsuke.rngom.parse.host;
/*    */ 
/*    */ import org.kohsuke.rngom.ast.builder.Annotations;
/*    */ import org.kohsuke.rngom.ast.builder.BuildException;
/*    */ import org.kohsuke.rngom.ast.builder.CommentList;
/*    */ import org.kohsuke.rngom.ast.builder.Div;
/*    */ import org.kohsuke.rngom.ast.builder.GrammarSection;
/*    */ import org.kohsuke.rngom.ast.builder.Include;
/*    */ import org.kohsuke.rngom.ast.om.Location;
/*    */ import org.kohsuke.rngom.ast.om.ParsedElementAnnotation;
/*    */ import org.kohsuke.rngom.ast.om.ParsedPattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GrammarSectionHost
/*    */   extends Base
/*    */   implements GrammarSection
/*    */ {
/*    */   private final GrammarSection lhs;
/*    */   private final GrammarSection rhs;
/*    */   
/*    */   GrammarSectionHost(GrammarSection lhs, GrammarSection rhs) {
/* 23 */     this.lhs = lhs;
/* 24 */     this.rhs = rhs;
/* 25 */     if (lhs == null || rhs == null) {
/* 26 */       throw new IllegalArgumentException();
/*    */     }
/*    */   }
/*    */   
/*    */   public void define(String name, GrammarSection.Combine combine, ParsedPattern _pattern, Location _loc, Annotations _anno) throws BuildException {
/* 31 */     ParsedPatternHost pattern = (ParsedPatternHost)_pattern;
/* 32 */     LocationHost loc = cast(_loc);
/* 33 */     AnnotationsHost anno = cast(_anno);
/*    */     
/* 35 */     this.lhs.define(name, combine, pattern.lhs, loc.lhs, anno.lhs);
/* 36 */     this.rhs.define(name, combine, pattern.rhs, loc.rhs, anno.rhs);
/*    */   }
/*    */   
/*    */   public Div makeDiv() {
/* 40 */     return new DivHost(this.lhs.makeDiv(), this.rhs.makeDiv());
/*    */   }
/*    */   
/*    */   public Include makeInclude() {
/* 44 */     Include l = this.lhs.makeInclude();
/* 45 */     if (l == null) return null; 
/* 46 */     return new IncludeHost(l, this.rhs.makeInclude());
/*    */   }
/*    */   
/*    */   public void topLevelAnnotation(ParsedElementAnnotation _ea) throws BuildException {
/* 50 */     ParsedElementAnnotationHost ea = (ParsedElementAnnotationHost)_ea;
/* 51 */     this.lhs.topLevelAnnotation((ea == null) ? null : ea.lhs);
/* 52 */     this.rhs.topLevelAnnotation((ea == null) ? null : ea.rhs);
/*    */   }
/*    */   
/*    */   public void topLevelComment(CommentList _comments) throws BuildException {
/* 56 */     CommentListHost comments = (CommentListHost)_comments;
/*    */     
/* 58 */     this.lhs.topLevelComment((comments == null) ? null : comments.lhs);
/* 59 */     this.rhs.topLevelComment((comments == null) ? null : comments.rhs);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\host\GrammarSectionHost.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */