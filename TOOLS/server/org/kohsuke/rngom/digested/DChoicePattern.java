/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DChoicePattern
/*    */   extends DContainerPattern
/*    */ {
/*    */   public boolean isNullable() {
/* 10 */     for (DPattern p = firstChild(); p != null; p = p.next) {
/* 11 */       if (p.isNullable())
/* 12 */         return true; 
/* 13 */     }  return false;
/*    */   }
/*    */   public <V> V accept(DPatternVisitor<V> visitor) {
/* 16 */     return visitor.onChoice(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DChoicePattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */