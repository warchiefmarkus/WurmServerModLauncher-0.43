/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DOneOrMorePattern
/*    */   extends DUnaryPattern
/*    */ {
/*    */   public boolean isNullable() {
/* 10 */     return getChild().isNullable();
/*    */   }
/*    */   public Object accept(DPatternVisitor visitor) {
/* 13 */     return visitor.onOneOrMore(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DOneOrMorePattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */