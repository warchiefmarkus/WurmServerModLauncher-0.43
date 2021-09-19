/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
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
/*     */ final class Util
/*     */ {
/*     */   private static final int C1 = -862048943;
/*     */   private static final int C2 = 461845907;
/*     */   private static final int ARRAY_LEN = 8192;
/*     */   
/*     */   public static int nextPowerOf2(int n) {
/*  38 */     if (n == 0) {
/*  39 */       return 1;
/*     */     }
/*  41 */     int b = Integer.highestOneBit(n);
/*  42 */     return (b == n) ? n : (b << 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void checkNotNegative(long n, String description) {
/*  50 */     Preconditions.checkArgument((n >= 0L), "%s must not be negative: %s", new Object[] { description, Long.valueOf(n) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void checkNoneNull(Iterable<?> objects) {
/*  57 */     if (!(objects instanceof com.google.common.collect.ImmutableCollection)) {
/*  58 */       for (Object o : objects) {
/*  59 */         Preconditions.checkNotNull(o);
/*     */       }
/*     */     }
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
/*     */ 
/*     */ 
/*     */   
/*     */   static int smearHash(int hashCode) {
/*  76 */     return 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
/*     */   }
/*     */ 
/*     */   
/*  80 */   private static final byte[] ZERO_ARRAY = new byte[8192];
/*  81 */   private static final byte[][] NULL_ARRAY = new byte[8192][];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void zero(byte[] bytes, int off, int len) {
/*  90 */     int remaining = len;
/*  91 */     while (remaining > 8192) {
/*  92 */       System.arraycopy(ZERO_ARRAY, 0, bytes, off, 8192);
/*  93 */       off += 8192;
/*  94 */       remaining -= 8192;
/*     */     } 
/*     */     
/*  97 */     System.arraycopy(ZERO_ARRAY, 0, bytes, off, remaining);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void clear(byte[][] blocks, int off, int len) {
/* 108 */     int remaining = len;
/* 109 */     while (remaining > 8192) {
/* 110 */       System.arraycopy(NULL_ARRAY, 0, blocks, off, 8192);
/* 111 */       off += 8192;
/* 112 */       remaining -= 8192;
/*     */     } 
/*     */     
/* 115 */     System.arraycopy(NULL_ARRAY, 0, blocks, off, remaining);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\Util.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */