/*     */ package org.fourthline.cling.model.types;
/*     */ 
/*     */ import java.util.logging.Logger;
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
/*     */ public abstract class UnsignedVariableInteger
/*     */ {
/*     */   protected long value;
/*  27 */   private static final Logger log = Logger.getLogger(UnsignedVariableInteger.class.getName());
/*     */   
/*     */   public enum Bits {
/*  30 */     EIGHT(255L),
/*  31 */     SIXTEEN(65535L),
/*  32 */     TWENTYFOUR(16777215L),
/*  33 */     THIRTYTWO(4294967295L);
/*     */     
/*     */     private long maxValue;
/*     */     
/*     */     Bits(long maxValue) {
/*  38 */       this.maxValue = maxValue;
/*     */     }
/*     */     
/*     */     public long getMaxValue() {
/*  42 */       return this.maxValue;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected UnsignedVariableInteger() {}
/*     */ 
/*     */   
/*     */   public UnsignedVariableInteger(long value) throws NumberFormatException {
/*  52 */     setValue(value);
/*     */   }
/*     */   
/*     */   public UnsignedVariableInteger(String s) throws NumberFormatException {
/*  56 */     if (s.startsWith("-")) {
/*     */ 
/*     */       
/*  59 */       log.warning("Invalid negative integer value '" + s + "', assuming value 0!");
/*  60 */       s = "0";
/*     */     } 
/*  62 */     setValue(Long.parseLong(s.trim()));
/*     */   }
/*     */   
/*     */   protected UnsignedVariableInteger setValue(long value) {
/*  66 */     isInRange(value);
/*  67 */     this.value = value;
/*  68 */     return this;
/*     */   }
/*     */   
/*     */   public Long getValue() {
/*  72 */     return Long.valueOf(this.value);
/*     */   }
/*     */   
/*     */   public void isInRange(long value) throws NumberFormatException {
/*  76 */     if (value < getMinValue() || value > getBits().getMaxValue()) {
/*  77 */       throw new NumberFormatException("Value must be between " + getMinValue() + " and " + getBits().getMaxValue() + ": " + value);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getMinValue() {
/*  82 */     return 0;
/*     */   }
/*     */   
/*     */   public abstract Bits getBits();
/*     */   
/*     */   public UnsignedVariableInteger increment(boolean rolloverToOne) {
/*  88 */     if (this.value + 1L > getBits().getMaxValue()) {
/*  89 */       this.value = rolloverToOne ? 1L : 0L;
/*     */     } else {
/*  91 */       this.value++;
/*     */     } 
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  98 */     if (this == o) return true; 
/*  99 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/* 101 */     UnsignedVariableInteger that = (UnsignedVariableInteger)o;
/*     */     
/* 103 */     if (this.value != that.value) return false;
/*     */     
/* 105 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 110 */     return (int)(this.value ^ this.value >>> 32L);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 115 */     return Long.toString(this.value);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\UnsignedVariableInteger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */