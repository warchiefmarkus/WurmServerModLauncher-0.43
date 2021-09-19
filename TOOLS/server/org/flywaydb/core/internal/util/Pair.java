/*    */ package org.flywaydb.core.internal.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Pair<L, R>
/*    */ {
/*    */   private L left;
/*    */   private R right;
/*    */   
/*    */   public static <L, R> Pair<L, R> of(L left, R right) {
/* 39 */     Pair<L, R> pair = new Pair<L, R>();
/* 40 */     pair.left = left;
/* 41 */     pair.right = right;
/* 42 */     return pair;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public L getLeft() {
/* 49 */     return this.left;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public R getRight() {
/* 56 */     return this.right;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\Pair.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */