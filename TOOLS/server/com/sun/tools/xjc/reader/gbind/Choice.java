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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Choice
/*    */   extends Expression
/*    */ {
/*    */   private final Expression lhs;
/*    */   private final Expression rhs;
/*    */   private final boolean isNullable;
/*    */   
/*    */   public Choice(Expression lhs, Expression rhs) {
/* 59 */     this.lhs = lhs;
/* 60 */     this.rhs = rhs;
/* 61 */     this.isNullable = (lhs.isNullable() || rhs.isNullable());
/*    */   }
/*    */   
/*    */   boolean isNullable() {
/* 65 */     return this.isNullable;
/*    */   }
/*    */   
/*    */   ElementSet lastSet() {
/* 69 */     return ElementSets.union(this.lhs.lastSet(), this.rhs.lastSet());
/*    */   }
/*    */   
/*    */   void buildDAG(ElementSet incoming) {
/* 73 */     this.lhs.buildDAG(incoming);
/* 74 */     this.rhs.buildDAG(incoming);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 78 */     return '(' + this.lhs.toString() + '|' + this.rhs.toString() + ')';
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\gbind\Choice.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */