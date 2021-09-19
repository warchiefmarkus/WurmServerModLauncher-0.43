/*    */ package org.kohsuke.rngom.binary;
/*    */ 
/*    */ import org.kohsuke.rngom.binary.visitor.PatternFunction;
/*    */ import org.kohsuke.rngom.binary.visitor.PatternVisitor;
/*    */ 
/*    */ public class NotAllowedPattern extends Pattern {
/*    */   NotAllowedPattern() {
/*  8 */     super(false, 0, 7);
/*    */   }
/*    */   boolean isNotAllowed() {
/* 11 */     return true;
/*    */   }
/*    */   
/*    */   boolean samePattern(Pattern other) {
/* 15 */     return (other.getClass() == getClass());
/*    */   }
/*    */   public void accept(PatternVisitor visitor) {
/* 18 */     visitor.visitNotAllowed();
/*    */   }
/*    */   public Object apply(PatternFunction f) {
/* 21 */     return f.caseNotAllowed(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\NotAllowedPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */