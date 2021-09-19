/*    */ package org.kohsuke.rngom.binary;
/*    */ 
/*    */ import org.kohsuke.rngom.binary.visitor.PatternFunction;
/*    */ import org.kohsuke.rngom.binary.visitor.PatternVisitor;
/*    */ 
/*    */ public class ChoicePattern extends BinaryPattern {
/*    */   ChoicePattern(Pattern p1, Pattern p2) {
/*  8 */     super((p1.isNullable() || p2.isNullable()), combineHashCode(11, p1.hashCode(), p2.hashCode()), p1, p2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   Pattern expand(SchemaPatternBuilder b) {
/* 14 */     Pattern ep1 = this.p1.expand(b);
/* 15 */     Pattern ep2 = this.p2.expand(b);
/* 16 */     if (ep1 != this.p1 || ep2 != this.p2) {
/* 17 */       return b.makeChoice(ep1, ep2);
/*    */     }
/* 19 */     return this;
/*    */   }
/*    */   
/*    */   boolean containsChoice(Pattern p) {
/* 23 */     return (this.p1.containsChoice(p) || this.p2.containsChoice(p));
/*    */   }
/*    */   
/*    */   public void accept(PatternVisitor visitor) {
/* 27 */     visitor.visitChoice(this.p1, this.p2);
/*    */   }
/*    */   
/*    */   public Object apply(PatternFunction f) {
/* 31 */     return f.caseChoice(this);
/*    */   }
/*    */ 
/*    */   
/*    */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/* 36 */     if (dad != null)
/* 37 */       dad.startChoice(); 
/* 38 */     this.p1.checkRestrictions(context, dad, alpha);
/* 39 */     if (dad != null)
/* 40 */       dad.alternative(); 
/* 41 */     this.p2.checkRestrictions(context, dad, alpha);
/* 42 */     if (dad != null)
/* 43 */       dad.endChoice(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\ChoicePattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */