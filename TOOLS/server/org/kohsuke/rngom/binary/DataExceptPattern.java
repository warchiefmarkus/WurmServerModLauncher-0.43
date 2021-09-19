/*    */ package org.kohsuke.rngom.binary;
/*    */ 
/*    */ import org.kohsuke.rngom.binary.visitor.PatternFunction;
/*    */ import org.kohsuke.rngom.binary.visitor.PatternVisitor;
/*    */ import org.relaxng.datatype.Datatype;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class DataExceptPattern extends DataPattern {
/*    */   private Pattern except;
/*    */   private Locator loc;
/*    */   
/*    */   DataExceptPattern(Datatype dt, Pattern except, Locator loc) {
/* 13 */     super(dt);
/* 14 */     this.except = except;
/* 15 */     this.loc = loc;
/*    */   }
/*    */   
/*    */   boolean samePattern(Pattern other) {
/* 19 */     if (!super.samePattern(other))
/* 20 */       return false; 
/* 21 */     return this.except.samePattern(((DataExceptPattern)other).except);
/*    */   }
/*    */   
/*    */   public void accept(PatternVisitor visitor) {
/* 25 */     visitor.visitDataExcept(getDatatype(), this.except);
/*    */   }
/*    */   
/*    */   public Object apply(PatternFunction f) {
/* 29 */     return f.caseDataExcept(this);
/*    */   }
/*    */ 
/*    */   
/*    */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/* 34 */     super.checkRestrictions(context, dad, alpha);
/*    */     try {
/* 36 */       this.except.checkRestrictions(7, null, null);
/*    */     }
/* 38 */     catch (RestrictionViolationException e) {
/* 39 */       e.maybeSetLocator(this.loc);
/* 40 */       throw e;
/*    */     } 
/*    */   }
/*    */   
/*    */   Pattern getExcept() {
/* 45 */     return this.except;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\DataExceptPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */