/*    */ package org.kohsuke.rngom.binary;
/*    */ 
/*    */ import org.kohsuke.rngom.binary.visitor.PatternFunction;
/*    */ import org.kohsuke.rngom.binary.visitor.PatternVisitor;
/*    */ 
/*    */ public class ErrorPattern extends Pattern {
/*    */   ErrorPattern() {
/*  8 */     super(false, 0, 3);
/*    */   }
/*    */   boolean samePattern(Pattern other) {
/* 11 */     return other instanceof ErrorPattern;
/*    */   }
/*    */   public void accept(PatternVisitor visitor) {
/* 14 */     visitor.visitError();
/*    */   }
/*    */   public Object apply(PatternFunction f) {
/* 17 */     return f.caseError(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\ErrorPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */