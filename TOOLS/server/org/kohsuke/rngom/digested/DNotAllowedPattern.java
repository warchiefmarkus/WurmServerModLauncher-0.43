/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DNotAllowedPattern
/*    */   extends DPattern
/*    */ {
/*    */   public boolean isNullable() {
/* 10 */     return false;
/*    */   }
/*    */   public Object accept(DPatternVisitor visitor) {
/* 13 */     return visitor.onNotAllowed(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DNotAllowedPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */