/*    */ package org.kohsuke.rngom.binary;
/*    */ import org.kohsuke.rngom.binary.visitor.PatternFunction;
/*    */ import org.kohsuke.rngom.binary.visitor.PatternVisitor;
/*    */ 
/*    */ public class GroupPattern extends BinaryPattern {
/*    */   GroupPattern(Pattern p1, Pattern p2) {
/*  7 */     super((p1.isNullable() && p2.isNullable()), combineHashCode(13, p1.hashCode(), p2.hashCode()), p1, p2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Pattern expand(SchemaPatternBuilder b) {
/* 14 */     Pattern ep1 = this.p1.expand(b);
/* 15 */     Pattern ep2 = this.p2.expand(b);
/* 16 */     if (ep1 != this.p1 || ep2 != this.p2) {
/* 17 */       return b.makeGroup(ep1, ep2);
/*    */     }
/* 19 */     return this;
/*    */   }
/*    */   
/*    */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/* 23 */     switch (context) {
/*    */       case 0:
/* 25 */         throw new RestrictionViolationException("start_contains_group");
/*    */       case 7:
/* 27 */         throw new RestrictionViolationException("data_except_contains_group");
/*    */     } 
/* 29 */     super.checkRestrictions((context == 2) ? 3 : context, dad, alpha);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 34 */     if (context != 6 && !contentTypeGroupable(this.p1.getContentType(), this.p2.getContentType()))
/*    */     {
/* 36 */       throw new RestrictionViolationException("group_string"); } 
/*    */   }
/*    */   
/*    */   public void accept(PatternVisitor visitor) {
/* 40 */     visitor.visitGroup(this.p1, this.p2);
/*    */   }
/*    */   public Object apply(PatternFunction f) {
/* 43 */     return f.caseGroup(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\GroupPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */