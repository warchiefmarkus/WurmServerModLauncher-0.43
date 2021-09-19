/*     */ package com.sun.tools.xjc.model;
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
/*     */   public static Multiplicity create(int min, Integer max) {
/*  58 */     if (min == 0 && max == null) return STAR; 
/*  59 */     if (min == 1 && max == null) return PLUS; 
/*  60 */     if (max != null) {
/*  61 */       if (min == 0 && max.intValue() == 0) return ZERO; 
/*  62 */       if (min == 0 && max.intValue() == 1) return OPTIONAL; 
/*  63 */       if (min == 1 && max.intValue() == 1) return ONE; 
/*     */     } 
/*  65 */     return new Multiplicity(min, max);
/*     */   }
/*     */   
/*     */   private Multiplicity(int min, Integer max) {
/*  69 */     this.min = min; this.max = max;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*  73 */     if (!(o instanceof Multiplicity)) return false;
/*     */     
/*  75 */     Multiplicity that = (Multiplicity)o;
/*     */     
/*  77 */     if (this.min != that.min) return false; 
/*  78 */     if ((this.max != null) ? !this.max.equals(that.max) : (that.max != null)) return false;
/*     */     
/*  80 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  85 */     int result = this.min;
/*  86 */     result = 29 * result + ((this.max != null) ? this.max.hashCode() : 0);
/*  87 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUnique() {
/*  92 */     if (this.max == null) return false; 
/*  93 */     return (this.min == 1 && this.max.intValue() == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOptional() {
/*  98 */     if (this.max == null) return false; 
/*  99 */     return (this.min == 0 && this.max.intValue() == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAtMostOnce() {
/* 104 */     if (this.max == null) return false; 
/* 105 */     return (this.max.intValue() <= 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZero() {
/* 110 */     if (this.max == null) return false; 
/* 111 */     return (this.max.intValue() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean includes(Multiplicity rhs) {
/* 121 */     if (rhs.min < this.min) return false; 
/* 122 */     if (this.max == null) return true; 
/* 123 */     if (rhs.max == null) return false; 
/* 124 */     return (rhs.max.intValue() <= this.max.intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMaxString() {
/* 132 */     if (this.max == null) return "unbounded"; 
/* 133 */     return this.max.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 140 */     return "(" + this.min + ',' + getMaxString() + ')';
/*     */   }
/*     */ 
/*     */   
/* 144 */   public static final Multiplicity ZERO = new Multiplicity(0, Integer.valueOf(0));
/*     */ 
/*     */   
/* 147 */   public static final Multiplicity ONE = new Multiplicity(1, Integer.valueOf(1));
/*     */ 
/*     */   
/* 150 */   public static final Multiplicity OPTIONAL = new Multiplicity(0, Integer.valueOf(1));
/*     */ 
/*     */   
/* 153 */   public static final Multiplicity STAR = new Multiplicity(0, null);
/*     */ 
/*     */   
/* 156 */   public static final Multiplicity PLUS = new Multiplicity(1, null);
/*     */ 
/*     */ 
/*     */   
/*     */   public static Multiplicity choice(Multiplicity lhs, Multiplicity rhs) {
/* 161 */     return create(Math.min(lhs.min, rhs.min), (lhs.max == null || rhs.max == null) ? null : Integer.valueOf(Math.max(lhs.max.intValue(), rhs.max.intValue())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Multiplicity group(Multiplicity lhs, Multiplicity rhs) {
/* 168 */     return create(lhs.min + rhs.min, (lhs.max == null || rhs.max == null) ? null : Integer.valueOf(lhs.max.intValue() + rhs.max.intValue()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Multiplicity multiply(Multiplicity lhs, Multiplicity rhs) {
/*     */     Integer max;
/* 174 */     int min = lhs.min * rhs.min;
/*     */     
/* 176 */     if (isZero(lhs.max) || isZero(rhs.max)) {
/* 177 */       max = Integer.valueOf(0);
/*     */     }
/* 179 */     else if (lhs.max == null || rhs.max == null) {
/* 180 */       max = null;
/*     */     } else {
/* 182 */       max = Integer.valueOf(lhs.max.intValue() * rhs.max.intValue());
/* 183 */     }  return create(min, max);
/*     */   }
/*     */   
/*     */   private static boolean isZero(Integer i) {
/* 187 */     return (i != null && i.intValue() == 0);
/*     */   }
/*     */   
/*     */   public static Multiplicity oneOrMore(Multiplicity c) {
/* 191 */     if (c.max == null) return c; 
/* 192 */     if (c.max.intValue() == 0) return c; 
/* 193 */     return create(c.min, null);
/*     */   }
/*     */   
/*     */   public Multiplicity makeOptional() {
/* 197 */     if (this.min == 0) return this; 
/* 198 */     return create(0, this.max);
/*     */   }
/*     */   
/*     */   public Multiplicity makeRepeated() {
/* 202 */     if (this.max == null || this.max.intValue() == 0) return this; 
/* 203 */     return create(this.min, null);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\Multiplicity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */