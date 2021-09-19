/*    */ package com.winterwell.jgeoplanet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum LengthUnit
/*    */ {
/*  9 */   METRE(1.0D),
/* 10 */   KILOMETRE(1000.0D),
/* 11 */   MILE(1609.344D);
/*    */ 
/*    */ 
/*    */   
/*    */   public final double metres;
/*    */ 
/*    */ 
/*    */   
/*    */   public final Dx dx;
/*    */ 
/*    */ 
/*    */   
/*    */   LengthUnit(double metres) {
/* 24 */     this.metres = metres;
/* 25 */     this.dx = new Dx(1.0D, this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Dx getDx() {
/* 32 */     return this.dx;
/*    */   }
/*    */ 
/*    */   
/*    */   public double getMetres() {
/* 37 */     return this.metres;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public double convert(double amount, LengthUnit otherUnit) {
/* 45 */     Dx Dx2 = new Dx(amount, otherUnit);
/* 46 */     return Dx2.convertTo(this).getValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\winterwell\jgeoplanet\LengthUnit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */