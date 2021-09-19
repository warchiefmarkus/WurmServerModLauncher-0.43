/*    */ package org.kohsuke.rngom.binary;
/*    */ 
/*    */ import org.kohsuke.rngom.binary.visitor.PatternFunction;
/*    */ import org.kohsuke.rngom.binary.visitor.PatternVisitor;
/*    */ 
/*    */ public class EmptyPattern extends Pattern {
/*    */   EmptyPattern() {
/*  8 */     super(true, 0, 5);
/*    */   }
/*    */   boolean samePattern(Pattern other) {
/* 11 */     return other instanceof EmptyPattern;
/*    */   }
/*    */   public void accept(PatternVisitor visitor) {
/* 14 */     visitor.visitEmpty();
/*    */   }
/*    */   public Object apply(PatternFunction f) {
/* 17 */     return f.caseEmpty(this);
/*    */   }
/*    */   
/*    */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/* 21 */     switch (context) {
/*    */       case 7:
/* 23 */         throw new RestrictionViolationException("data_except_contains_empty");
/*    */       case 0:
/* 25 */         throw new RestrictionViolationException("start_contains_empty");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\EmptyPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */