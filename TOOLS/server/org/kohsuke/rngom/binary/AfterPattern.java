/*    */ package org.kohsuke.rngom.binary;
/*    */ 
/*    */ import org.kohsuke.rngom.binary.visitor.PatternFunction;
/*    */ import org.kohsuke.rngom.binary.visitor.PatternVisitor;
/*    */ 
/*    */ public class AfterPattern
/*    */   extends BinaryPattern {
/*    */   AfterPattern(Pattern p1, Pattern p2) {
/*  9 */     super(false, combineHashCode(41, p1.hashCode(), p2.hashCode()), p1, p2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   boolean isNotAllowed() {
/* 16 */     return this.p1.isNotAllowed();
/*    */   }
/*    */   
/*    */   public Object apply(PatternFunction f) {
/* 20 */     return f.caseAfter(this);
/*    */   }
/*    */   public void accept(PatternVisitor visitor) {
/* 23 */     visitor.visitAfter(this.p1, this.p2);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\AfterPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */