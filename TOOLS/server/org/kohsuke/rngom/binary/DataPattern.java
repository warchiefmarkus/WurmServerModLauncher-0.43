/*    */ package org.kohsuke.rngom.binary;
/*    */ 
/*    */ import org.kohsuke.rngom.binary.visitor.PatternFunction;
/*    */ import org.kohsuke.rngom.binary.visitor.PatternVisitor;
/*    */ import org.relaxng.datatype.Datatype;
/*    */ 
/*    */ public class DataPattern extends StringPattern {
/*    */   private Datatype dt;
/*    */   
/*    */   DataPattern(Datatype dt) {
/* 11 */     super(combineHashCode(31, dt.hashCode()));
/* 12 */     this.dt = dt;
/*    */   }
/*    */   
/*    */   boolean samePattern(Pattern other) {
/* 16 */     if (other.getClass() != getClass())
/* 17 */       return false; 
/* 18 */     return this.dt.equals(((DataPattern)other).dt);
/*    */   }
/*    */   
/*    */   public void accept(PatternVisitor visitor) {
/* 22 */     visitor.visitData(this.dt);
/*    */   }
/*    */   
/*    */   public Object apply(PatternFunction f) {
/* 26 */     return f.caseData(this);
/*    */   }
/*    */   
/*    */   Datatype getDatatype() {
/* 30 */     return this.dt;
/*    */   }
/*    */   
/*    */   boolean allowsAnyString() {
/* 34 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/* 40 */     switch (context) {
/*    */       case 0:
/* 42 */         throw new RestrictionViolationException("start_contains_data");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\DataPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */