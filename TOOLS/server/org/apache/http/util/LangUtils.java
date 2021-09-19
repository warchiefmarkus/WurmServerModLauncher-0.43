/*     */ package org.apache.http.util;
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
/*     */ public final class LangUtils
/*     */ {
/*     */   public static final int HASH_SEED = 17;
/*     */   public static final int HASH_OFFSET = 37;
/*     */   
/*     */   public static int hashCode(int seed, int hashcode) {
/*  47 */     return seed * 37 + hashcode;
/*     */   }
/*     */   
/*     */   public static int hashCode(int seed, boolean b) {
/*  51 */     return hashCode(seed, b ? 1 : 0);
/*     */   }
/*     */   
/*     */   public static int hashCode(int seed, Object obj) {
/*  55 */     return hashCode(seed, (obj != null) ? obj.hashCode() : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equals(Object obj1, Object obj2) {
/*  66 */     return (obj1 == null) ? ((obj2 == null)) : obj1.equals(obj2);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equals(Object[] a1, Object[] a2) {
/*  85 */     if (a1 == null) {
/*  86 */       if (a2 == null) {
/*  87 */         return true;
/*     */       }
/*  89 */       return false;
/*     */     } 
/*     */     
/*  92 */     if (a2 != null && a1.length == a2.length) {
/*  93 */       for (int i = 0; i < a1.length; i++) {
/*  94 */         if (!equals(a1[i], a2[i])) {
/*  95 */           return false;
/*     */         }
/*     */       } 
/*  98 */       return true;
/*     */     } 
/* 100 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\htt\\util\LangUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */