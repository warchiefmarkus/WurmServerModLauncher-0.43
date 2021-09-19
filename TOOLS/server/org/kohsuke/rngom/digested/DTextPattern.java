/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DTextPattern
/*    */   extends DPattern
/*    */ {
/*    */   public boolean isNullable() {
/* 10 */     return true;
/*    */   }
/*    */   public Object accept(DPatternVisitor visitor) {
/* 13 */     return visitor.onText(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DTextPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */