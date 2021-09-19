/*    */ package com.sun.tools.xjc.reader.gbind;
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
/*    */ public final class OneOrMore
/*    */   extends Expression
/*    */ {
/*    */   private final Expression child;
/*    */   
/*    */   public OneOrMore(Expression child) {
/* 51 */     this.child = child;
/*    */   }
/*    */   
/*    */   ElementSet lastSet() {
/* 55 */     return this.child.lastSet();
/*    */   }
/*    */   
/*    */   boolean isNullable() {
/* 59 */     return this.child.isNullable();
/*    */   }
/*    */   
/*    */   void buildDAG(ElementSet incoming) {
/* 63 */     this.child.buildDAG(ElementSets.union(incoming, this.child.lastSet()));
/*    */   }
/*    */   
/*    */   public String toString() {
/* 67 */     return this.child.toString() + '+';
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\gbind\OneOrMore.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */