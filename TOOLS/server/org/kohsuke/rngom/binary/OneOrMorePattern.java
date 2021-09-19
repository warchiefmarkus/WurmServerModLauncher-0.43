/*    */ package org.kohsuke.rngom.binary;
/*    */ 
/*    */ import org.kohsuke.rngom.binary.visitor.PatternFunction;
/*    */ import org.kohsuke.rngom.binary.visitor.PatternVisitor;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class OneOrMorePattern extends Pattern {
/*    */   Pattern p;
/*    */   
/*    */   OneOrMorePattern(Pattern p) {
/* 11 */     super(p.isNullable(), p.getContentType(), combineHashCode(19, p.hashCode()));
/*    */ 
/*    */     
/* 14 */     this.p = p;
/*    */   }
/*    */   
/*    */   Pattern expand(SchemaPatternBuilder b) {
/* 18 */     Pattern ep = this.p.expand(b);
/* 19 */     if (ep != this.p) {
/* 20 */       return b.makeOneOrMore(ep);
/*    */     }
/* 22 */     return this;
/*    */   }
/*    */   
/*    */   void checkRecursion(int depth) throws SAXException {
/* 26 */     this.p.checkRecursion(depth);
/*    */   }
/*    */ 
/*    */   
/*    */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/* 31 */     switch (context) {
/*    */       case 0:
/* 33 */         throw new RestrictionViolationException("start_contains_one_or_more");
/*    */       case 7:
/* 35 */         throw new RestrictionViolationException("data_except_contains_one_or_more");
/*    */     } 
/*    */     
/* 38 */     this.p.checkRestrictions((context == 1) ? 2 : context, dad, alpha);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 43 */     if (context != 6 && !contentTypeGroupable(this.p.getContentType(), this.p.getContentType()))
/*    */     {
/* 45 */       throw new RestrictionViolationException("one_or_more_string"); } 
/*    */   }
/*    */   
/*    */   boolean samePattern(Pattern other) {
/* 49 */     return (other instanceof OneOrMorePattern && this.p == ((OneOrMorePattern)other).p);
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept(PatternVisitor visitor) {
/* 54 */     visitor.visitOneOrMore(this.p);
/*    */   }
/*    */   
/*    */   public Object apply(PatternFunction f) {
/* 58 */     return f.caseOneOrMore(this);
/*    */   }
/*    */   
/*    */   Pattern getOperand() {
/* 62 */     return this.p;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\OneOrMorePattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */