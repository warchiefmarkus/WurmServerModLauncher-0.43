/*     */ package com.sun.xml.bind.api.impl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
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
/*     */ class NameUtil
/*     */ {
/*     */   protected static final int UPPER_LETTER = 0;
/*     */   protected static final int LOWER_LETTER = 1;
/*     */   protected static final int OTHER_LETTER = 2;
/*     */   protected static final int DIGIT = 3;
/*     */   protected static final int OTHER = 4;
/*     */   
/*     */   protected boolean isPunct(char c) {
/*  55 */     return (c == '-' || c == '.' || c == ':' || c == '_' || c == '·' || c == '·' || c == '۝' || c == '۞');
/*     */   }
/*     */   
/*     */   protected static boolean isDigit(char c) {
/*  59 */     return ((c >= '0' && c <= '9') || Character.isDigit(c));
/*     */   }
/*     */   
/*     */   protected static boolean isUpper(char c) {
/*  63 */     return ((c >= 'A' && c <= 'Z') || Character.isUpperCase(c));
/*     */   }
/*     */   
/*     */   protected static boolean isLower(char c) {
/*  67 */     return ((c >= 'a' && c <= 'z') || Character.isLowerCase(c));
/*     */   }
/*     */   
/*     */   protected boolean isLetter(char c) {
/*  71 */     return ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || Character.isLetter(c));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String capitalize(String s) {
/*  79 */     if (!isLower(s.charAt(0)))
/*  80 */       return s; 
/*  81 */     StringBuilder sb = new StringBuilder(s.length());
/*  82 */     sb.append(Character.toUpperCase(s.charAt(0)));
/*  83 */     sb.append(s.substring(1).toLowerCase());
/*  84 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private int nextBreak(String s, int start) {
/*  89 */     int n = s.length();
/*     */     
/*  91 */     char c1 = s.charAt(start);
/*  92 */     int t1 = classify(c1);
/*     */     
/*  94 */     for (int i = start + 1; i < n; i++) {
/*     */ 
/*     */       
/*  97 */       int t0 = t1;
/*     */       
/*  99 */       c1 = s.charAt(i);
/* 100 */       t1 = classify(c1);
/*     */       
/* 102 */       switch (actionTable[t0 * 5 + t1]) {
/*     */         case 0:
/* 104 */           if (isPunct(c1)) return i; 
/*     */           break;
/*     */         case 1:
/* 107 */           if (i < n - 1) {
/* 108 */             char c2 = s.charAt(i + 1);
/* 109 */             if (isLower(c2))
/* 110 */               return i; 
/*     */           } 
/*     */           break;
/*     */         case 2:
/* 114 */           return i;
/*     */       } 
/*     */     } 
/* 117 */     return -1;
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
/* 132 */   private static final byte[] actionTable = new byte[25];
/*     */ 
/*     */   
/*     */   private static final byte ACTION_CHECK_PUNCT = 0;
/*     */ 
/*     */   
/*     */   private static final byte ACTION_CHECK_C2 = 1;
/*     */   
/*     */   private static final byte ACTION_BREAK = 2;
/*     */   
/*     */   private static final byte ACTION_NOBREAK = 3;
/*     */ 
/*     */   
/*     */   private static byte decideAction(int t0, int t1) {
/* 146 */     if (t0 == 4 && t1 == 4) return 0; 
/* 147 */     if (!xor((t0 == 3), (t1 == 3))) return 2; 
/* 148 */     if (t0 == 1 && t1 != 1) return 2; 
/* 149 */     if (!xor((t0 <= 2), (t1 <= 2))) return 2; 
/* 150 */     if (!xor((t0 == 2), (t1 == 2))) return 2;
/*     */     
/* 152 */     if (t0 == 0 && t1 == 0) return 1;
/*     */     
/* 154 */     return 3;
/*     */   }
/*     */   
/*     */   private static boolean xor(boolean x, boolean y) {
/* 158 */     return ((x && y) || (!x && !y));
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/* 163 */     for (int t0 = 0; t0 < 5; t0++) {
/* 164 */       for (int t1 = 0; t1 < 5; t1++) {
/* 165 */         actionTable[t0 * 5 + t1] = decideAction(t0, t1);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected int classify(char c0) {
/* 172 */     switch (Character.getType(c0)) { case 1:
/* 173 */         return 0;
/* 174 */       case 2: return 1;
/*     */       case 3: case 4:
/*     */       case 5:
/* 177 */         return 2;
/* 178 */       case 9: return 3; }
/* 179 */      return 4;
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
/*     */   public List<String> toWordList(String s) {
/* 194 */     ArrayList<String> ss = new ArrayList<String>();
/* 195 */     int n = s.length(); int i;
/* 196 */     for (i = 0; i < n; ) {
/*     */ 
/*     */       
/* 199 */       while (i < n && 
/* 200 */         isPunct(s.charAt(i)))
/*     */       {
/* 202 */         i++;
/*     */       }
/* 204 */       if (i >= n) {
/*     */         break;
/*     */       }
/* 207 */       int b = nextBreak(s, i);
/* 208 */       String w = (b == -1) ? s.substring(i) : s.substring(i, b);
/* 209 */       ss.add(escape(capitalize(w)));
/* 210 */       if (b == -1)
/* 211 */         break;  i = b;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 218 */     return ss;
/*     */   }
/*     */   
/*     */   protected String toMixedCaseName(List<String> ss, boolean startUpper) {
/* 222 */     StringBuilder sb = new StringBuilder();
/* 223 */     if (!ss.isEmpty()) {
/* 224 */       sb.append(startUpper ? ss.get(0) : ((String)ss.get(0)).toLowerCase());
/* 225 */       for (int i = 1; i < ss.size(); i++)
/* 226 */         sb.append(ss.get(i)); 
/*     */     } 
/* 228 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String toMixedCaseVariableName(String[] ss, boolean startUpper, boolean cdrUpper) {
/* 234 */     if (cdrUpper)
/* 235 */       for (int i = 1; i < ss.length; i++)
/* 236 */         ss[i] = capitalize(ss[i]);  
/* 237 */     StringBuilder sb = new StringBuilder();
/* 238 */     if (ss.length > 0) {
/* 239 */       sb.append(startUpper ? ss[0] : ss[0].toLowerCase());
/* 240 */       for (int i = 1; i < ss.length; i++)
/* 241 */         sb.append(ss[i]); 
/*     */     } 
/* 243 */     return sb.toString();
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
/*     */   public String toConstantName(String s) {
/* 255 */     return toConstantName(toWordList(s));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toConstantName(List<String> ss) {
/* 266 */     StringBuilder sb = new StringBuilder();
/* 267 */     if (!ss.isEmpty()) {
/* 268 */       sb.append(((String)ss.get(0)).toUpperCase());
/* 269 */       for (int i = 1; i < ss.size(); i++) {
/* 270 */         sb.append('_');
/* 271 */         sb.append(((String)ss.get(i)).toUpperCase());
/*     */       } 
/*     */     } 
/* 274 */     return sb.toString();
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
/*     */   public static void escape(StringBuilder sb, String s, int start) {
/* 293 */     int n = s.length();
/* 294 */     for (int i = start; i < n; i++) {
/* 295 */       char c = s.charAt(i);
/* 296 */       if (Character.isJavaIdentifierPart(c)) {
/* 297 */         sb.append(c);
/*     */       } else {
/* 299 */         sb.append('_');
/* 300 */         if (c <= '\017') { sb.append("000"); }
/* 301 */         else if (c <= 'ÿ') { sb.append("00"); }
/* 302 */         else if (c <= '࿿') { sb.append('0'); }
/* 303 */          sb.append(Integer.toString(c, 16));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String escape(String s) {
/* 313 */     int n = s.length();
/* 314 */     for (int i = 0; i < n; i++) {
/* 315 */       if (!Character.isJavaIdentifierPart(s.charAt(i))) {
/* 316 */         StringBuilder sb = new StringBuilder(s.substring(0, i));
/* 317 */         escape(sb, s, i);
/* 318 */         return sb.toString();
/*     */       } 
/* 320 */     }  return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isJavaIdentifier(String s) {
/* 328 */     if (s.length() == 0) return false; 
/* 329 */     if (reservedKeywords.contains(s)) return false;
/*     */     
/* 331 */     if (!Character.isJavaIdentifierStart(s.charAt(0))) return false;
/*     */     
/* 333 */     for (int i = 1; i < s.length(); i++) {
/* 334 */       if (!Character.isJavaIdentifierPart(s.charAt(i)))
/* 335 */         return false; 
/*     */     } 
/* 337 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isJavaPackageName(String s) {
/* 344 */     while (s.length() != 0) {
/* 345 */       int idx = s.indexOf('.');
/* 346 */       if (idx == -1) idx = s.length(); 
/* 347 */       if (!isJavaIdentifier(s.substring(0, idx))) {
/* 348 */         return false;
/*     */       }
/* 350 */       s = s.substring(idx);
/* 351 */       if (s.length() != 0) s = s.substring(1); 
/*     */     } 
/* 353 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 358 */   private static HashSet<String> reservedKeywords = new HashSet<String>();
/*     */ 
/*     */   
/*     */   static {
/* 362 */     String[] words = { "abstract", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "extends", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while", "true", "false", "null", "assert", "enum" };
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 423 */     for (String word : words)
/* 424 */       reservedKeywords.add(word); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\api\impl\NameUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */