/*    */ package org.kohsuke.rngom.binary;
/*    */ 
/*    */ import org.kohsuke.rngom.binary.visitor.PatternFunction;
/*    */ import org.kohsuke.rngom.binary.visitor.PatternVisitor;
/*    */ 
/*    */ public class InterleavePattern extends BinaryPattern {
/*    */   InterleavePattern(Pattern p1, Pattern p2) {
/*  8 */     super((p1.isNullable() && p2.isNullable()), combineHashCode(17, p1.hashCode(), p2.hashCode()), p1, p2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   Pattern expand(SchemaPatternBuilder b) {
/* 14 */     Pattern ep1 = this.p1.expand(b);
/* 15 */     Pattern ep2 = this.p2.expand(b);
/* 16 */     if (ep1 != this.p1 || ep2 != this.p2) {
/* 17 */       return b.makeInterleave(ep1, ep2);
/*    */     }
/* 19 */     return this;
/*    */   }
/*    */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/*    */     Alphabet a1;
/* 23 */     switch (context) {
/*    */       case 0:
/* 25 */         throw new RestrictionViolationException("start_contains_interleave");
/*    */       case 7:
/* 27 */         throw new RestrictionViolationException("data_except_contains_interleave");
/*    */       case 6:
/* 29 */         throw new RestrictionViolationException("list_contains_interleave");
/*    */     } 
/* 31 */     if (context == 2) {
/* 32 */       context = 4;
/*    */     }
/* 34 */     if (alpha != null && alpha.isEmpty()) {
/* 35 */       a1 = alpha;
/*    */     } else {
/* 37 */       a1 = new Alphabet();
/* 38 */     }  this.p1.checkRestrictions(context, dad, a1);
/* 39 */     if (a1.isEmpty()) {
/* 40 */       this.p2.checkRestrictions(context, dad, a1);
/*    */     } else {
/* 42 */       Alphabet a2 = new Alphabet();
/* 43 */       this.p2.checkRestrictions(context, dad, a2);
/* 44 */       a1.checkOverlap(a2);
/* 45 */       if (alpha != null) {
/* 46 */         if (alpha != a1)
/* 47 */           alpha.addAlphabet(a1); 
/* 48 */         alpha.addAlphabet(a2);
/*    */       } 
/*    */     } 
/* 51 */     if (context != 6 && !contentTypeGroupable(this.p1.getContentType(), this.p2.getContentType()))
/*    */     {
/* 53 */       throw new RestrictionViolationException("interleave_string"); } 
/* 54 */     if (this.p1.getContentType() == 2 && this.p2.getContentType() == 2)
/*    */     {
/* 56 */       throw new RestrictionViolationException("interleave_text_overlap"); } 
/*    */   }
/*    */   
/*    */   public void accept(PatternVisitor visitor) {
/* 60 */     visitor.visitInterleave(this.p1, this.p2);
/*    */   }
/*    */   public Object apply(PatternFunction f) {
/* 63 */     return f.caseInterleave(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\InterleavePattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */