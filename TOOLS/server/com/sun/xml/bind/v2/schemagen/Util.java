/*     */ package com.sun.xml.bind.v2.schemagen;
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
/*     */ 
/*     */ public final class Util
/*     */ {
/*     */   public static String escapeURI(String s) {
/*  57 */     StringBuilder sb = new StringBuilder();
/*  58 */     for (int i = 0; i < s.length(); i++) {
/*  59 */       char c = s.charAt(i);
/*  60 */       if (Character.isSpaceChar(c)) {
/*  61 */         sb.append("%20");
/*     */       } else {
/*  63 */         sb.append(c);
/*     */       } 
/*     */     } 
/*  66 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getParentUriPath(String uriPath) {
/*  76 */     int idx = uriPath.lastIndexOf('/');
/*     */     
/*  78 */     if (uriPath.endsWith("/")) {
/*  79 */       uriPath = uriPath.substring(0, idx);
/*  80 */       idx = uriPath.lastIndexOf('/');
/*     */     } 
/*     */     
/*  83 */     return uriPath.substring(0, idx) + "/";
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
/*     */   public static String normalizeUriPath(String uriPath) {
/*  99 */     if (uriPath.endsWith("/")) {
/* 100 */       return uriPath;
/*     */     }
/*     */ 
/*     */     
/* 104 */     int idx = uriPath.lastIndexOf('/');
/* 105 */     return uriPath.substring(0, idx + 1);
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
/*     */   public static boolean equalsIgnoreCase(String s, String t) {
/* 117 */     if (s == t) return true; 
/* 118 */     if (s != null && t != null) {
/* 119 */       return s.equalsIgnoreCase(t);
/*     */     }
/* 121 */     return false;
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
/*     */   public static boolean equal(String s, String t) {
/* 133 */     if (s == t) return true; 
/* 134 */     if (s != null && t != null) {
/* 135 */       return s.equals(t);
/*     */     }
/* 137 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\Util.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */