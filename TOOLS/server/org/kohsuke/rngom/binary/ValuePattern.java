/*    */ package org.kohsuke.rngom.binary;
/*    */ 
/*    */ import org.kohsuke.rngom.binary.visitor.PatternFunction;
/*    */ import org.kohsuke.rngom.binary.visitor.PatternVisitor;
/*    */ import org.relaxng.datatype.Datatype;
/*    */ 
/*    */ public class ValuePattern extends StringPattern {
/*    */   Object obj;
/*    */   Datatype dt;
/*    */   
/*    */   ValuePattern(Datatype dt, Object obj) {
/* 12 */     super(combineHashCode(27, obj.hashCode()));
/* 13 */     this.dt = dt;
/* 14 */     this.obj = obj;
/*    */   }
/*    */   
/*    */   boolean samePattern(Pattern other) {
/* 18 */     if (getClass() != other.getClass())
/* 19 */       return false; 
/* 20 */     if (!(other instanceof ValuePattern))
/* 21 */       return false; 
/* 22 */     return (this.dt.equals(((ValuePattern)other).dt) && this.dt.sameValue(this.obj, ((ValuePattern)other).obj));
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept(PatternVisitor visitor) {
/* 27 */     visitor.visitValue(this.dt, this.obj);
/*    */   }
/*    */   
/*    */   public Object apply(PatternFunction f) {
/* 31 */     return f.caseValue(this);
/*    */   }
/*    */ 
/*    */   
/*    */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/* 36 */     switch (context) {
/*    */       case 0:
/* 38 */         throw new RestrictionViolationException("start_contains_value");
/*    */     } 
/*    */   }
/*    */   
/*    */   Datatype getDatatype() {
/* 43 */     return this.dt;
/*    */   }
/*    */   
/*    */   Object getValue() {
/* 47 */     return this.obj;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\ValuePattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */