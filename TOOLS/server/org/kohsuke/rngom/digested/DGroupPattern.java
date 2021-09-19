/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DGroupPattern
/*    */   extends DContainerPattern
/*    */ {
/*    */   public boolean isNullable() {
/* 10 */     for (DPattern p = firstChild(); p != null; p = p.next) {
/* 11 */       if (!p.isNullable())
/* 12 */         return false; 
/* 13 */     }  return true;
/*    */   }
/*    */   public <V> V accept(DPatternVisitor<V> visitor) {
/* 16 */     return visitor.onGroup(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DGroupPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */