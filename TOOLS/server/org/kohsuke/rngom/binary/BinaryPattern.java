/*    */ package org.kohsuke.rngom.binary;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public abstract class BinaryPattern
/*    */   extends Pattern {
/*    */   protected final Pattern p1;
/*    */   protected final Pattern p2;
/*    */   
/*    */   BinaryPattern(boolean nullable, int hc, Pattern p1, Pattern p2) {
/* 14 */     super(nullable, Math.max(p1.getContentType(), p2.getContentType()), hc);
/* 15 */     this.p1 = p1;
/* 16 */     this.p2 = p2;
/*    */   }
/*    */   
/*    */   void checkRecursion(int depth) throws SAXException {
/* 20 */     this.p1.checkRecursion(depth);
/* 21 */     this.p2.checkRecursion(depth);
/*    */   }
/*    */ 
/*    */   
/*    */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/* 26 */     this.p1.checkRestrictions(context, dad, alpha);
/* 27 */     this.p2.checkRestrictions(context, dad, alpha);
/*    */   }
/*    */   
/*    */   boolean samePattern(Pattern other) {
/* 31 */     if (getClass() != other.getClass())
/* 32 */       return false; 
/* 33 */     BinaryPattern b = (BinaryPattern)other;
/* 34 */     return (this.p1 == b.p1 && this.p2 == b.p2);
/*    */   }
/*    */   
/*    */   public final Pattern getOperand1() {
/* 38 */     return this.p1;
/*    */   }
/*    */   
/*    */   public final Pattern getOperand2() {
/* 42 */     return this.p2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void fillChildren(Collection col) {
/* 54 */     fillChildren(getClass(), this.p1, col);
/* 55 */     fillChildren(getClass(), this.p2, col);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Pattern[] getChildren() {
/* 62 */     List lst = new ArrayList();
/* 63 */     fillChildren(lst);
/* 64 */     return (Pattern[])lst.toArray((Object[])new Pattern[lst.size()]);
/*    */   }
/*    */   
/*    */   private void fillChildren(Class<?> c, Pattern p, Collection<Pattern> col) {
/* 68 */     if (p.getClass() == c) {
/* 69 */       BinaryPattern bp = (BinaryPattern)p;
/* 70 */       bp.fillChildren(c, bp.p1, col);
/* 71 */       bp.fillChildren(c, bp.p2, col);
/*    */     } else {
/* 73 */       col.add(p);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\BinaryPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */