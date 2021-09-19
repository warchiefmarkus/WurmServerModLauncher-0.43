/*    */ package org.kohsuke.rngom.parse.host;
/*    */ 
/*    */ import org.kohsuke.rngom.ast.om.ParsedPattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParsedPatternHost
/*    */   implements ParsedPattern
/*    */ {
/*    */   public final ParsedPattern lhs;
/*    */   public final ParsedPattern rhs;
/*    */   
/*    */   ParsedPatternHost(ParsedPattern lhs, ParsedPattern rhs) {
/* 15 */     this.lhs = lhs;
/* 16 */     this.rhs = rhs;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\host\ParsedPatternHost.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */