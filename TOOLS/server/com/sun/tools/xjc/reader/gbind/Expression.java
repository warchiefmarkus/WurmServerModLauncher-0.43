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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Expression
/*    */ {
/* 66 */   public static final Expression EPSILON = new Expression() {
/*    */       ElementSet lastSet() {
/* 68 */         return ElementSet.EMPTY_SET;
/*    */       }
/*    */       
/*    */       boolean isNullable() {
/* 72 */         return true;
/*    */       }
/*    */ 
/*    */       
/*    */       void buildDAG(ElementSet incoming) {}
/*    */ 
/*    */       
/*    */       public String toString() {
/* 80 */         return "-";
/*    */       }
/*    */     };
/*    */   
/*    */   abstract ElementSet lastSet();
/*    */   
/*    */   abstract boolean isNullable();
/*    */   
/*    */   abstract void buildDAG(ElementSet paramElementSet);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\gbind\Expression.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */