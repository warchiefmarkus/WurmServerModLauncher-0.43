/*    */ package org.kohsuke.rngom.parse.host;
/*    */ 
/*    */ import org.kohsuke.rngom.ast.om.ParsedElementAnnotation;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class ParsedElementAnnotationHost
/*    */   implements ParsedElementAnnotation
/*    */ {
/*    */   final ParsedElementAnnotation lhs;
/*    */   final ParsedElementAnnotation rhs;
/*    */   
/*    */   ParsedElementAnnotationHost(ParsedElementAnnotation lhs, ParsedElementAnnotation rhs) {
/* 15 */     this.lhs = lhs;
/* 16 */     this.rhs = rhs;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\host\ParsedElementAnnotationHost.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */