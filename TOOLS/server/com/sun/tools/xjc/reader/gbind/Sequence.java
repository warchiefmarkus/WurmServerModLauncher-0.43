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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Sequence
/*    */   extends Expression
/*    */ {
/*    */   private final Expression lhs;
/*    */   private final Expression rhs;
/*    */   private final boolean isNullable;
/*    */   private ElementSet lastSet;
/*    */   
/*    */   public Sequence(Expression lhs, Expression rhs) {
/* 66 */     this.lhs = lhs;
/* 67 */     this.rhs = rhs;
/* 68 */     this.isNullable = (lhs.isNullable() && rhs.isNullable());
/*    */   }
/*    */   
/*    */   ElementSet lastSet() {
/* 72 */     if (this.lastSet == null)
/* 73 */       if (this.rhs.isNullable()) {
/* 74 */         this.lastSet = ElementSets.union(this.lhs.lastSet(), this.rhs.lastSet());
/*    */       } else {
/* 76 */         this.lastSet = this.rhs.lastSet();
/*    */       }  
/* 78 */     return this.lastSet;
/*    */   }
/*    */   
/*    */   boolean isNullable() {
/* 82 */     return this.isNullable;
/*    */   }
/*    */   
/*    */   void buildDAG(ElementSet incoming) {
/* 86 */     this.lhs.buildDAG(incoming);
/* 87 */     if (this.lhs.isNullable()) {
/* 88 */       this.rhs.buildDAG(ElementSets.union(incoming, this.lhs.lastSet()));
/*    */     } else {
/* 90 */       this.rhs.buildDAG(this.lhs.lastSet());
/*    */     } 
/*    */   }
/*    */   public String toString() {
/* 94 */     return '(' + this.lhs.toString() + ',' + this.rhs.toString() + ')';
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\gbind\Sequence.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */