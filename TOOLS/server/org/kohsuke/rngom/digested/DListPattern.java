/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DListPattern
/*    */   extends DUnaryPattern
/*    */ {
/*    */   public boolean isNullable() {
/* 10 */     return getChild().isNullable();
/*    */   }
/*    */   public Object accept(DPatternVisitor visitor) {
/* 13 */     return visitor.onList(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DListPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */