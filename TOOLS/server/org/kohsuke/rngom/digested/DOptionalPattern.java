/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DOptionalPattern
/*    */   extends DUnaryPattern
/*    */ {
/*    */   public boolean isNullable() {
/* 10 */     return true;
/*    */   }
/*    */   public Object accept(DPatternVisitor visitor) {
/* 13 */     return visitor.onOptional(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DOptionalPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */