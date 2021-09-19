/*     */ package com.sun.tools.xjc.util;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.util.regex.Matcher;
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
/*     */ public final class StringCutter
/*     */ {
/*     */   private final String original;
/*     */   private String s;
/*     */   private boolean ignoreWhitespace;
/*     */   
/*     */   public StringCutter(String s, boolean ignoreWhitespace) {
/*  54 */     this.s = this.original = s;
/*  55 */     this.ignoreWhitespace = ignoreWhitespace;
/*     */   }
/*     */   
/*     */   public void skip(String regexp) throws ParseException {
/*  59 */     next(regexp);
/*     */   }
/*     */   
/*     */   public String next(String regexp) throws ParseException {
/*  63 */     trim();
/*  64 */     Pattern p = Pattern.compile(regexp);
/*  65 */     Matcher m = p.matcher(this.s);
/*  66 */     if (m.lookingAt()) {
/*  67 */       String r = m.group();
/*  68 */       this.s = this.s.substring(r.length());
/*  69 */       trim();
/*  70 */       return r;
/*     */     } 
/*  72 */     throw error();
/*     */   }
/*     */   
/*     */   private ParseException error() {
/*  76 */     return new ParseException(this.original, this.original.length() - this.s.length());
/*     */   }
/*     */   
/*     */   public String until(String regexp) throws ParseException {
/*  80 */     Pattern p = Pattern.compile(regexp);
/*  81 */     Matcher m = p.matcher(this.s);
/*  82 */     if (m.find()) {
/*  83 */       String str = this.s.substring(0, m.start());
/*  84 */       this.s = this.s.substring(m.start());
/*  85 */       if (this.ignoreWhitespace)
/*  86 */         str = str.trim(); 
/*  87 */       return str;
/*     */     } 
/*     */     
/*  90 */     String r = this.s;
/*  91 */     this.s = "";
/*  92 */     return r;
/*     */   }
/*     */ 
/*     */   
/*     */   public char peek() {
/*  97 */     return this.s.charAt(0);
/*     */   }
/*     */   
/*     */   private void trim() {
/* 101 */     if (this.ignoreWhitespace)
/* 102 */       this.s = this.s.trim(); 
/*     */   }
/*     */   
/*     */   public int length() {
/* 106 */     return this.s.length();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xj\\util\StringCutter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */