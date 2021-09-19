/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DEmptyPattern
/*    */   extends DPattern
/*    */ {
/*    */   public boolean isNullable() {
/* 10 */     return true;
/*    */   }
/*    */   public Object accept(DPatternVisitor visitor) {
/* 13 */     return visitor.onEmpty(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DEmptyPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */