/*     */ package com.sun.xml.dtdparser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlNames
/*     */ {
/*     */   public static boolean isName(String value) {
/*  27 */     if (value == null) {
/*  28 */       return false;
/*     */     }
/*  30 */     char c = value.charAt(0);
/*  31 */     if (!XmlChars.isLetter(c) && c != '_' && c != ':')
/*  32 */       return false; 
/*  33 */     for (int i = 1; i < value.length(); i++) {
/*  34 */       if (!XmlChars.isNameChar(value.charAt(i)))
/*  35 */         return false; 
/*  36 */     }  return true;
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
/*     */   public static boolean isUnqualifiedName(String value) {
/*  48 */     if (value == null || value.length() == 0) {
/*  49 */       return false;
/*     */     }
/*  51 */     char c = value.charAt(0);
/*  52 */     if (!XmlChars.isLetter(c) && c != '_')
/*  53 */       return false; 
/*  54 */     for (int i = 1; i < value.length(); i++) {
/*  55 */       if (!XmlChars.isNCNameChar(value.charAt(i)))
/*  56 */         return false; 
/*  57 */     }  return true;
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
/*     */   public static boolean isQualifiedName(String value) {
/*  72 */     if (value == null) {
/*  73 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     int first = value.indexOf(':');
/*     */ 
/*     */     
/*  82 */     if (first <= 0) {
/*  83 */       return isUnqualifiedName(value);
/*     */     }
/*     */ 
/*     */     
/*  87 */     int last = value.lastIndexOf(':');
/*  88 */     if (last != first) {
/*  89 */       return false;
/*     */     }
/*  91 */     return (isUnqualifiedName(value.substring(0, first)) && isUnqualifiedName(value.substring(first + 1)));
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
/*     */   public static boolean isNmtoken(String token) {
/* 105 */     int length = token.length();
/*     */     
/* 107 */     for (int i = 0; i < length; i++) {
/* 108 */       if (!XmlChars.isNameChar(token.charAt(i)))
/* 109 */         return false; 
/* 110 */     }  return true;
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
/*     */   public static boolean isNCNmtoken(String token) {
/* 124 */     return (isNmtoken(token) && token.indexOf(':') < 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\dtdparser\XmlNames.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */