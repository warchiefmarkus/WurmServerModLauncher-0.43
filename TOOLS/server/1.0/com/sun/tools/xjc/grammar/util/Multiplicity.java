/*     */ package 1.0.com.sun.tools.xjc.grammar.util;
/*     */ 
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionVisitor;
/*     */ import com.sun.tools.xjc.grammar.util.MultiplicityCounter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Multiplicity
/*     */ {
/*     */   public final int min;
/*     */   public final Integer max;
/*     */   
/*     */   public Multiplicity(int min, Integer max) {
/*  27 */     this.min = min; this.max = max;
/*     */   }
/*     */   public Multiplicity(int min, int max) {
/*  30 */     this.min = min; this.max = new Integer(max);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUnique() {
/*  35 */     if (this.max == null) return false; 
/*  36 */     return (this.min == 1 && this.max.intValue() == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOptional() {
/*  41 */     if (this.max == null) return false; 
/*  42 */     return (this.min == 0 && this.max.intValue() == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAtMostOnce() {
/*  47 */     if (this.max == null) return false; 
/*  48 */     return (this.max.intValue() <= 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZero() {
/*  53 */     if (this.max == null) return false; 
/*  54 */     return (this.max.intValue() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean includes(com.sun.tools.xjc.grammar.util.Multiplicity rhs) {
/*  64 */     if (rhs.min < this.min) return false; 
/*  65 */     if (this.max == null) return true; 
/*  66 */     if (rhs.max == null) return false; 
/*  67 */     return (rhs.max.intValue() <= this.max.intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMaxString() {
/*  75 */     if (this.max == null) return "unbounded"; 
/*  76 */     return this.max.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  83 */     return "(" + this.min + "," + getMaxString() + ")";
/*     */   }
/*     */ 
/*     */   
/*  87 */   public static final com.sun.tools.xjc.grammar.util.Multiplicity zero = new com.sun.tools.xjc.grammar.util.Multiplicity(0, 0);
/*     */ 
/*     */   
/*  90 */   public static final com.sun.tools.xjc.grammar.util.Multiplicity one = new com.sun.tools.xjc.grammar.util.Multiplicity(1, 1);
/*     */ 
/*     */   
/*  93 */   public static final com.sun.tools.xjc.grammar.util.Multiplicity star = new com.sun.tools.xjc.grammar.util.Multiplicity(0, null);
/*     */   
/*     */   public static com.sun.tools.xjc.grammar.util.Multiplicity calc(Expression exp, MultiplicityCounter calc) {
/*  96 */     return (com.sun.tools.xjc.grammar.util.Multiplicity)exp.visit((ExpressionVisitor)calc);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static com.sun.tools.xjc.grammar.util.Multiplicity choice(com.sun.tools.xjc.grammar.util.Multiplicity lhs, com.sun.tools.xjc.grammar.util.Multiplicity rhs) {
/* 102 */     return new com.sun.tools.xjc.grammar.util.Multiplicity(Math.min(lhs.min, rhs.min), (lhs.max == null || rhs.max == null) ? null : new Integer(Math.max(lhs.max.intValue(), rhs.max.intValue())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static com.sun.tools.xjc.grammar.util.Multiplicity group(com.sun.tools.xjc.grammar.util.Multiplicity lhs, com.sun.tools.xjc.grammar.util.Multiplicity rhs) {
/* 109 */     return new com.sun.tools.xjc.grammar.util.Multiplicity(lhs.min + rhs.min, (lhs.max == null || rhs.max == null) ? null : new Integer(lhs.max.intValue() + rhs.max.intValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static com.sun.tools.xjc.grammar.util.Multiplicity oneOrMore(com.sun.tools.xjc.grammar.util.Multiplicity c) {
/* 115 */     if (c.max == null) return c; 
/* 116 */     if (c.max.intValue() == 0) return c; 
/* 117 */     return new com.sun.tools.xjc.grammar.util.Multiplicity(c.min, null);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\gramma\\util\Multiplicity.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */