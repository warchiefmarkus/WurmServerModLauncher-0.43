/*    */ package org.kohsuke.rngom.parse.host;
/*    */ 
/*    */ import org.kohsuke.rngom.ast.builder.Annotations;
/*    */ import org.kohsuke.rngom.ast.builder.BuildException;
/*    */ import org.kohsuke.rngom.ast.builder.CommentList;
/*    */ import org.kohsuke.rngom.ast.builder.ElementAnnotationBuilder;
/*    */ import org.kohsuke.rngom.ast.om.Location;
/*    */ import org.kohsuke.rngom.ast.om.ParsedElementAnnotation;
/*    */ 
/*    */ 
/*    */ final class ElementAnnotationBuilderHost
/*    */   extends AnnotationsHost
/*    */   implements ElementAnnotationBuilder
/*    */ {
/*    */   final ElementAnnotationBuilder lhs;
/*    */   final ElementAnnotationBuilder rhs;
/*    */   
/*    */   ElementAnnotationBuilderHost(ElementAnnotationBuilder lhs, ElementAnnotationBuilder rhs) {
/* 19 */     super((Annotations)lhs, (Annotations)rhs);
/* 20 */     this.lhs = lhs;
/* 21 */     this.rhs = rhs;
/*    */   }
/*    */   
/*    */   public void addText(String value, Location _loc, CommentList _comments) throws BuildException {
/* 25 */     LocationHost loc = cast(_loc);
/* 26 */     CommentListHost comments = (CommentListHost)_comments;
/*    */     
/* 28 */     this.lhs.addText(value, loc.lhs, (comments == null) ? null : comments.lhs);
/* 29 */     this.rhs.addText(value, loc.rhs, (comments == null) ? null : comments.rhs);
/*    */   }
/*    */   
/*    */   public ParsedElementAnnotation makeElementAnnotation() throws BuildException {
/* 33 */     return new ParsedElementAnnotationHost(this.lhs.makeElementAnnotation(), this.rhs.makeElementAnnotation());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\host\ElementAnnotationBuilderHost.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */