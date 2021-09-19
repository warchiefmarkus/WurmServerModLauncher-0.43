/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ 
/*    */ public class DZeroOrMorePattern
/*    */   extends DUnaryPattern
/*    */ {
/*    */   public boolean isNullable() {
/*  8 */     return true;
/*    */   }
/*    */   public Object accept(DPatternVisitor visitor) {
/* 11 */     return visitor.onZeroOrMore(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DZeroOrMorePattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */