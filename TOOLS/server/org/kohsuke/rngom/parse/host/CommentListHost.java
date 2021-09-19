/*    */ package org.kohsuke.rngom.parse.host;
/*    */ 
/*    */ import org.kohsuke.rngom.ast.builder.BuildException;
/*    */ import org.kohsuke.rngom.ast.builder.CommentList;
/*    */ import org.kohsuke.rngom.ast.om.Location;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CommentListHost
/*    */   extends Base
/*    */   implements CommentList
/*    */ {
/*    */   final CommentList lhs;
/*    */   final CommentList rhs;
/*    */   
/*    */   CommentListHost(CommentList lhs, CommentList rhs) {
/* 18 */     this.lhs = lhs;
/* 19 */     this.rhs = rhs;
/*    */   }
/*    */   
/*    */   public void addComment(String value, Location _loc) throws BuildException {
/* 23 */     LocationHost loc = cast(_loc);
/* 24 */     if (this.lhs != null)
/* 25 */       this.lhs.addComment(value, loc.lhs); 
/* 26 */     if (this.rhs != null)
/* 27 */       this.rhs.addComment(value, loc.rhs); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\host\CommentListHost.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */