/*    */ package com.sun.tools.xjc.reader.dtd;
/*    */ 
/*    */ import java.util.List;
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
/*    */ final class Occurence
/*    */   extends Term
/*    */ {
/*    */   final Term term;
/*    */   final boolean isOptional;
/*    */   final boolean isRepeated;
/*    */   
/*    */   Occurence(Term term, boolean optional, boolean repeated) {
/* 53 */     this.term = term;
/* 54 */     this.isOptional = optional;
/* 55 */     this.isRepeated = repeated;
/*    */   }
/*    */   
/*    */   static Term wrap(Term t, int occurence) {
/* 59 */     switch (occurence) {
/*    */       case 3:
/* 61 */         return t;
/*    */       case 1:
/* 63 */         return new Occurence(t, false, true);
/*    */       case 0:
/* 65 */         return new Occurence(t, true, true);
/*    */       case 2:
/* 67 */         return new Occurence(t, true, false);
/*    */     } 
/* 69 */     throw new IllegalArgumentException();
/*    */   }
/*    */ 
/*    */   
/*    */   void normalize(List<Block> r, boolean optional) {
/* 74 */     if (this.isRepeated) {
/* 75 */       Block b = new Block((this.isOptional || optional), true);
/* 76 */       addAllElements(b);
/* 77 */       r.add(b);
/*    */     } else {
/* 79 */       this.term.normalize(r, (optional || this.isOptional));
/*    */     } 
/*    */   }
/*    */   
/*    */   void addAllElements(Block b) {
/* 84 */     this.term.addAllElements(b);
/*    */   }
/*    */   
/*    */   boolean isOptional() {
/* 88 */     return (this.isOptional || this.term.isOptional());
/*    */   }
/*    */   
/*    */   boolean isRepeated() {
/* 92 */     return (this.isRepeated || this.term.isRepeated());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\dtd\Occurence.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */