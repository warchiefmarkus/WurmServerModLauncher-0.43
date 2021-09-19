/*     */ package com.winterwell.jgeoplanet;
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
/*     */ public final class Dx
/*     */   implements Comparable<Dx>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final LengthUnit unit;
/*     */   private final double n;
/*     */   
/*     */   public static Dx ZERO() {
/*  23 */     return new Dx(0.0D, LengthUnit.METRE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Dx(double metres) {
/*  35 */     this(metres, LengthUnit.METRE);
/*     */   }
/*     */   
/*     */   public Dx(double n, LengthUnit unit) {
/*  39 */     this.n = n;
/*  40 */     this.unit = unit;
/*  41 */     assert unit != null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMetres() {
/*  49 */     return this.unit.metres * this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getValue() {
/*  56 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LengthUnit geKLength() {
/*  63 */     return this.unit;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  68 */     return String.valueOf((float)this.n) + " " + this.unit.toString().toLowerCase() + ((this.n != 1.0D) ? "s" : "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isShorterThan(Dx Dx2) {
/*  77 */     assert Dx2 != null;
/*  78 */     return (Math.abs(getMetres()) < Math.abs(Dx2.getMetres()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  86 */     if (obj == null) return false; 
/*  87 */     if (obj == this) return true; 
/*  88 */     if (obj.getClass() != Dx.class) return false; 
/*  89 */     Dx dx = (Dx)obj;
/*  90 */     return (getMetres() == dx.getMetres());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  95 */     return (new Double(getMetres())).hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Dx multiply(double x) {
/* 103 */     return new Dx(x * this.n, this.unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Dx Dx2) {
/* 108 */     double ms = getMetres();
/* 109 */     double ms2 = Dx2.getMetres();
/* 110 */     if (ms == ms2) return 0; 
/* 111 */     return (ms < ms2) ? -1 : 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Dx convertTo(LengthUnit unit2) {
/* 122 */     if (this.unit == unit2) {
/* 123 */       return this;
/*     */     }
/* 125 */     double n2 = divide(unit2.dx);
/* 126 */     return new Dx(n2, unit2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double divide(Dx other) {
/* 135 */     if (this.n == 0.0D) return 0.0D; 
/* 136 */     return this.n * this.unit.metres / other.n * other.unit.metres;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\winterwell\jgeoplanet\Dx.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */