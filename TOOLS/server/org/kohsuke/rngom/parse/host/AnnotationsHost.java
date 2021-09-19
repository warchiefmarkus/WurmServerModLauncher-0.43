/*    */ package org.kohsuke.rngom.parse.host;
/*    */ 
/*    */ import org.kohsuke.rngom.ast.builder.Annotations;
/*    */ import org.kohsuke.rngom.ast.builder.BuildException;
/*    */ import org.kohsuke.rngom.ast.builder.CommentList;
/*    */ import org.kohsuke.rngom.ast.om.Location;
/*    */ import org.kohsuke.rngom.ast.om.ParsedElementAnnotation;
/*    */ 
/*    */ 
/*    */ 
/*    */ class AnnotationsHost
/*    */   extends Base
/*    */   implements Annotations
/*    */ {
/*    */   final Annotations lhs;
/*    */   final Annotations rhs;
/*    */   
/*    */   AnnotationsHost(Annotations lhs, Annotations rhs) {
/* 19 */     this.lhs = lhs;
/* 20 */     this.rhs = rhs;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addAttribute(String ns, String localName, String prefix, String value, Location _loc) throws BuildException {
/* 25 */     LocationHost loc = cast(_loc);
/* 26 */     this.lhs.addAttribute(ns, localName, prefix, value, loc.lhs);
/* 27 */     this.rhs.addAttribute(ns, localName, prefix, value, loc.rhs);
/*    */   }
/*    */   
/*    */   public void addComment(CommentList _comments) throws BuildException {
/* 31 */     CommentListHost comments = (CommentListHost)_comments;
/* 32 */     this.lhs.addComment((comments == null) ? null : comments.lhs);
/* 33 */     this.rhs.addComment((comments == null) ? null : comments.rhs);
/*    */   }
/*    */   
/*    */   public void addElement(ParsedElementAnnotation _ea) throws BuildException {
/* 37 */     ParsedElementAnnotationHost ea = (ParsedElementAnnotationHost)_ea;
/* 38 */     this.lhs.addElement(ea.lhs);
/* 39 */     this.rhs.addElement(ea.rhs);
/*    */   }
/*    */   
/*    */   public void addLeadingComment(CommentList _comments) throws BuildException {
/* 43 */     CommentListHost comments = (CommentListHost)_comments;
/* 44 */     this.lhs.addLeadingComment(comments.lhs);
/* 45 */     this.rhs.addLeadingComment(comments.rhs);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\host\AnnotationsHost.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */