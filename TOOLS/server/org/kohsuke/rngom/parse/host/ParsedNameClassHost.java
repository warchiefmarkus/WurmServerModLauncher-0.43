/*    */ package org.kohsuke.rngom.parse.host;
/*    */ 
/*    */ import org.kohsuke.rngom.ast.om.ParsedNameClass;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class ParsedNameClassHost
/*    */   implements ParsedNameClass
/*    */ {
/*    */   final ParsedNameClass lhs;
/*    */   final ParsedNameClass rhs;
/*    */   
/*    */   ParsedNameClassHost(ParsedNameClass lhs, ParsedNameClass rhs) {
/* 15 */     this.lhs = lhs;
/* 16 */     this.rhs = rhs;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\host\ParsedNameClassHost.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */