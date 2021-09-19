/*     */ package org.kohsuke.rngom.util;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ 
/*     */ public class Uri {
/*   8 */   private static String utf8 = "UTF-8";
/*     */   
/*     */   public static boolean isValid(String s) {
/*  11 */     return (isValidPercent(s) && isValidFragment(s) && isValidScheme(s));
/*     */   }
/*     */   private static final String HEX_DIGITS = "0123456789abcdef";
/*     */   private static final String excluded = "<>\"{}|\\^`";
/*     */   
/*     */   public static String escapeDisallowedChars(String s) {
/*  17 */     StringBuffer buf = null;
/*  18 */     int len = s.length();
/*  19 */     int done = 0; while (true) {
/*     */       byte[] arrayOfByte;
/*  21 */       int i = done;
/*     */       while (true) {
/*  23 */         if (i == len) {
/*  24 */           if (done == 0)
/*  25 */             return s; 
/*     */           break;
/*     */         } 
/*  28 */         if (isExcluded(s.charAt(i)))
/*     */           break; 
/*  30 */         i++;
/*     */       } 
/*  32 */       if (buf == null)
/*  33 */         buf = new StringBuffer(); 
/*  34 */       if (i > done) {
/*  35 */         buf.append(s.substring(done, i));
/*  36 */         done = i;
/*     */       } 
/*  38 */       if (i == len)
/*     */         break; 
/*  40 */       for (; ++i < len && isExcluded(s.charAt(i)); i++);
/*     */       
/*  42 */       String tem = s.substring(done, i);
/*     */       
/*     */       try {
/*  45 */         arrayOfByte = tem.getBytes(utf8);
/*     */       }
/*  47 */       catch (UnsupportedEncodingException e) {
/*  48 */         utf8 = "UTF8";
/*     */         try {
/*  50 */           arrayOfByte = tem.getBytes(utf8);
/*     */         }
/*  52 */         catch (UnsupportedEncodingException e2) {
/*     */           
/*  54 */           return s;
/*     */         } 
/*     */       } 
/*  57 */       for (int j = 0; j < arrayOfByte.length; j++) {
/*  58 */         buf.append('%');
/*  59 */         buf.append("0123456789abcdef".charAt((arrayOfByte[j] & 0xFF) >> 4));
/*  60 */         buf.append("0123456789abcdef".charAt(arrayOfByte[j] & 0xF));
/*     */       } 
/*  62 */       done = i;
/*     */     } 
/*  64 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isExcluded(char c) {
/*  70 */     return (c <= ' ' || c >= '' || "<>\"{}|\\^`".indexOf(c) >= 0);
/*     */   }
/*     */   
/*     */   private static boolean isAlpha(char c) {
/*  74 */     return (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z'));
/*     */   }
/*     */   
/*     */   private static boolean isHexDigit(char c) {
/*  78 */     return (('a' <= c && c <= 'f') || ('A' <= c && c <= 'F') || isDigit(c));
/*     */   }
/*     */   
/*     */   private static boolean isDigit(char c) {
/*  82 */     return ('0' <= c && c <= '9');
/*     */   }
/*     */   
/*     */   private static boolean isSchemeChar(char c) {
/*  86 */     return (isAlpha(c) || isDigit(c) || c == '+' || c == '-' || c == '.');
/*     */   }
/*     */   
/*     */   private static boolean isValidPercent(String s) {
/*  90 */     int len = s.length();
/*  91 */     for (int i = 0; i < len; i++) {
/*  92 */       if (s.charAt(i) == '%') {
/*  93 */         if (i + 2 >= len)
/*  94 */           return false; 
/*  95 */         if (!isHexDigit(s.charAt(i + 1)) || !isHexDigit(s.charAt(i + 2)))
/*     */         {
/*  97 */           return false; } 
/*     */       } 
/*  99 */     }  return true;
/*     */   }
/*     */   
/*     */   private static boolean isValidFragment(String s) {
/* 103 */     int i = s.indexOf('#');
/* 104 */     return (i < 0 || s.indexOf('#', i + 1) < 0);
/*     */   }
/*     */   
/*     */   private static boolean isValidScheme(String s) {
/* 108 */     if (!isAbsolute(s))
/* 109 */       return true; 
/* 110 */     int i = s.indexOf(':');
/* 111 */     if (i == 0 || i + 1 == s.length() || !isAlpha(s.charAt(0)))
/*     */     {
/*     */       
/* 114 */       return false; } 
/* 115 */     while (--i > 0) {
/* 116 */       if (!isSchemeChar(s.charAt(i)))
/* 117 */         return false; 
/* 118 */     }  return true;
/*     */   }
/*     */   
/*     */   public static String resolve(String baseUri, String uriReference) {
/* 122 */     if (!isAbsolute(uriReference) && baseUri != null && isAbsolute(baseUri)) {
/*     */       try {
/* 124 */         return (new URL(new URL(baseUri), uriReference)).toString();
/*     */       }
/* 126 */       catch (MalformedURLException e) {}
/*     */     }
/* 128 */     return uriReference;
/*     */   }
/*     */   
/*     */   public static boolean hasFragmentId(String uri) {
/* 132 */     return (uri.indexOf('#') >= 0);
/*     */   }
/*     */   
/*     */   public static boolean isAbsolute(String uri) {
/* 136 */     int i = uri.indexOf(':');
/* 137 */     if (i < 0)
/* 138 */       return false; 
/* 139 */     while (--i >= 0) {
/* 140 */       switch (uri.charAt(i)) {
/*     */         case '#':
/*     */         case '/':
/*     */         case '?':
/* 144 */           return false;
/*     */       } 
/*     */     } 
/* 147 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngo\\util\Uri.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */