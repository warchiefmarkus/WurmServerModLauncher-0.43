/*    */ package org.kohsuke.rngom.binary;
/*    */ 
/*    */ import org.kohsuke.rngom.binary.visitor.PatternFunction;
/*    */ import org.kohsuke.rngom.binary.visitor.PatternVisitor;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class ListPattern extends Pattern {
/*    */   Pattern p;
/*    */   Locator locator;
/*    */   
/*    */   ListPattern(Pattern p, Locator locator) {
/* 13 */     super(false, 3, combineHashCode(37, p.hashCode()));
/*    */ 
/*    */     
/* 16 */     this.p = p;
/* 17 */     this.locator = locator;
/*    */   }
/*    */   
/*    */   Pattern expand(SchemaPatternBuilder b) {
/* 21 */     Pattern ep = this.p.expand(b);
/* 22 */     if (ep != this.p) {
/* 23 */       return b.makeList(ep, this.locator);
/*    */     }
/* 25 */     return this;
/*    */   }
/*    */   
/*    */   void checkRecursion(int depth) throws SAXException {
/* 29 */     this.p.checkRecursion(depth);
/*    */   }
/*    */   
/*    */   boolean samePattern(Pattern other) {
/* 33 */     return (other instanceof ListPattern && this.p == ((ListPattern)other).p);
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept(PatternVisitor visitor) {
/* 38 */     visitor.visitList(this.p);
/*    */   }
/*    */   
/*    */   public Object apply(PatternFunction f) {
/* 42 */     return f.caseList(this);
/*    */   }
/*    */ 
/*    */   
/*    */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/* 47 */     switch (context) {
/*    */       case 7:
/* 49 */         throw new RestrictionViolationException("data_except_contains_list");
/*    */       case 0:
/* 51 */         throw new RestrictionViolationException("start_contains_list");
/*    */       case 6:
/* 53 */         throw new RestrictionViolationException("list_contains_list");
/*    */     } 
/*    */     try {
/* 56 */       this.p.checkRestrictions(6, dad, null);
/*    */     }
/* 58 */     catch (RestrictionViolationException e) {
/* 59 */       e.maybeSetLocator(this.locator);
/* 60 */       throw e;
/*    */     } 
/*    */   }
/*    */   
/*    */   Pattern getOperand() {
/* 65 */     return this.p;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\ListPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */