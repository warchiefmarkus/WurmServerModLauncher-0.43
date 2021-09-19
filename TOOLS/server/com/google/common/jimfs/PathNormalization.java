/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.Function;
/*     */ import com.ibm.icu.lang.UCharacter;
/*     */ import java.text.Normalizer;
/*     */ import java.util.regex.Pattern;
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
/*     */ public enum PathNormalization
/*     */   implements Function<String, String>
/*     */ {
/*  40 */   NONE(0)
/*     */   {
/*     */     public String apply(String string) {
/*  43 */       return string;
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   NFC(128)
/*     */   {
/*     */     public String apply(String string) {
/*  53 */       return Normalizer.normalize(string, Normalizer.Form.NFC);
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   NFD(128)
/*     */   {
/*     */     public String apply(String string) {
/*  63 */       return Normalizer.normalize(string, Normalizer.Form.NFD);
/*     */     }
/*     */   },
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
/*  82 */   CASE_FOLD_UNICODE(66)
/*     */   {
/*     */     public String apply(String string) {
/*     */       try {
/*  86 */         return UCharacter.foldCase(string, true);
/*  87 */       } catch (NoClassDefFoundError e) {
/*  88 */         NoClassDefFoundError error = new NoClassDefFoundError("PathNormalization.CASE_FOLD_UNICODE requires ICU4J. Did you forget to include it on your classpath?");
/*     */ 
/*     */ 
/*     */         
/*  92 */         error.initCause(e);
/*  93 */         throw error;
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */   },
/* 101 */   CASE_FOLD_ASCII(2)
/*     */   {
/*     */     public String apply(String string) {
/* 104 */       return Ascii.toLowerCase(string);
/*     */     }
/*     */   };
/*     */   
/*     */   private final int patternFlags;
/*     */   
/*     */   PathNormalization(int patternFlags) {
/* 111 */     this.patternFlags = patternFlags;
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
/*     */   public int patternFlags() {
/* 125 */     return this.patternFlags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String normalize(String string, Iterable<PathNormalization> normalizations) {
/* 133 */     String result = string;
/* 134 */     for (PathNormalization normalization : normalizations) {
/* 135 */       result = normalization.apply(result);
/*     */     }
/* 137 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Pattern compilePattern(String regex, Iterable<PathNormalization> normalizations) {
/* 144 */     int flags = 0;
/* 145 */     for (PathNormalization normalization : normalizations) {
/* 146 */       flags |= normalization.patternFlags();
/*     */     }
/* 148 */     return Pattern.compile(regex, flags);
/*     */   }
/*     */   
/*     */   public abstract String apply(String paramString);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\PathNormalization.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */