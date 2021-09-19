/*     */ package com.sun.xml.bind.v2.util;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ public class EditDistance
/*     */ {
/*     */   private int[] cost;
/*     */   private int[] back;
/*     */   private final String a;
/*     */   private final String b;
/*     */   
/*     */   public static int editDistance(String a, String b) {
/*  64 */     return (new EditDistance(a, b)).calc();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String findNearest(String key, String[] group) {
/*  74 */     return findNearest(key, Arrays.asList(group));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String findNearest(String key, Collection<String> group) {
/*  84 */     int c = Integer.MAX_VALUE;
/*  85 */     String r = null;
/*     */     
/*  87 */     for (String s : group) {
/*  88 */       int ed = editDistance(key, s);
/*  89 */       if (c > ed) {
/*  90 */         c = ed;
/*  91 */         r = s;
/*     */       } 
/*     */     } 
/*  94 */     return r;
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
/*     */   private EditDistance(String a, String b) {
/* 106 */     this.a = a;
/* 107 */     this.b = b;
/* 108 */     this.cost = new int[a.length() + 1];
/* 109 */     this.back = new int[a.length() + 1];
/*     */     
/* 111 */     for (int i = 0; i <= a.length(); i++) {
/* 112 */       this.cost[i] = i;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void flip() {
/* 119 */     int[] t = this.cost;
/* 120 */     this.cost = this.back;
/* 121 */     this.back = t;
/*     */   }
/*     */   
/*     */   private int min(int a, int b, int c) {
/* 125 */     return Math.min(a, Math.min(b, c));
/*     */   }
/*     */   
/*     */   private int calc() {
/* 129 */     for (int j = 0; j < this.b.length(); j++) {
/* 130 */       flip();
/* 131 */       this.cost[0] = j + 1;
/* 132 */       for (int i = 0; i < this.a.length(); i++) {
/* 133 */         int match = (this.a.charAt(i) == this.b.charAt(j)) ? 0 : 1;
/* 134 */         this.cost[i + 1] = min(this.back[i] + match, this.cost[i] + 1, this.back[i + 1] + 1);
/*     */       } 
/*     */     } 
/* 137 */     return this.cost[this.a.length()];
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v\\util\EditDistance.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */