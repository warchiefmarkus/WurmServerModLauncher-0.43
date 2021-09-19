/*    */ package org.kohsuke.rngom.binary;
/*    */ 
/*    */ import org.kohsuke.rngom.binary.visitor.PatternFunction;
/*    */ import org.kohsuke.rngom.binary.visitor.PatternVisitor;
/*    */ 
/*    */ public class TextPattern extends Pattern {
/*    */   TextPattern() {
/*  8 */     super(true, 2, 1);
/*    */   }
/*    */   
/*    */   boolean samePattern(Pattern other) {
/* 12 */     return other instanceof TextPattern;
/*    */   }
/*    */   
/*    */   public void accept(PatternVisitor visitor) {
/* 16 */     visitor.visitText();
/*    */   }
/*    */   
/*    */   public Object apply(PatternFunction f) {
/* 20 */     return f.caseText(this);
/*    */   }
/*    */ 
/*    */   
/*    */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/* 25 */     switch (context) {
/*    */       case 7:
/* 27 */         throw new RestrictionViolationException("data_except_contains_text");
/*    */       case 0:
/* 29 */         throw new RestrictionViolationException("start_contains_text");
/*    */       case 6:
/* 31 */         throw new RestrictionViolationException("list_contains_text");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\TextPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */