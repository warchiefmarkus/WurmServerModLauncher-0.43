/*     */ package org.flywaydb.core.internal.util;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ public class StringUtils
/*     */ {
/*     */   public static String trimOrPad(String str, int length) {
/*  42 */     return trimOrPad(str, length, ' ');
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
/*     */   public static String trimOrPad(String str, int length, char padChar) {
/*     */     String result;
/*  55 */     if (str == null) {
/*  56 */       result = "";
/*     */     } else {
/*  58 */       result = str;
/*     */     } 
/*     */     
/*  61 */     if (result.length() > length) {
/*  62 */       return result.substring(0, length);
/*     */     }
/*     */     
/*  65 */     while (result.length() < length) {
/*  66 */       result = result + padChar;
/*     */     }
/*  68 */     return result;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNumeric(String str) {
/*  90 */     return (str != null && str.matches("\\d*"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String collapseWhitespace(String str) {
/* 100 */     return str.replaceAll("\\s+", " ");
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
/*     */   public static String left(String str, int count) {
/* 112 */     if (str == null) {
/* 113 */       return null;
/*     */     }
/*     */     
/* 116 */     if (str.length() < count) {
/* 117 */       return str;
/*     */     }
/*     */     
/* 120 */     return str.substring(0, count);
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
/*     */   public static String replaceAll(String str, String originalToken, String replacementToken) {
/* 132 */     return str.replaceAll(Pattern.quote(originalToken), Matcher.quoteReplacement(replacementToken));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasLength(String str) {
/* 142 */     return (str != null && str.length() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String arrayToCommaDelimitedString(Object[] strings) {
/* 152 */     if (strings == null) {
/* 153 */       return "";
/*     */     }
/*     */     
/* 156 */     StringBuilder builder = new StringBuilder();
/* 157 */     for (int i = 0; i < strings.length; i++) {
/* 158 */       if (i > 0) {
/* 159 */         builder.append(",");
/*     */       }
/* 161 */       builder.append(String.valueOf(strings[i]));
/*     */     } 
/* 163 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasText(String s) {
/* 173 */     return (s != null && s.trim().length() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] tokenizeToStringArray(String str, String delimiters) {
/* 184 */     if (str == null) {
/* 185 */       return null;
/*     */     }
/* 187 */     String[] tokens = str.split("[" + delimiters + "]");
/* 188 */     for (int i = 0; i < tokens.length; i++) {
/* 189 */       tokens[i] = tokens[i].trim();
/*     */     }
/* 191 */     return tokens;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int countOccurrencesOf(String str, String token) {
/* 202 */     if (str == null || token == null || str.length() == 0 || token.length() == 0) {
/* 203 */       return 0;
/*     */     }
/* 205 */     int count = 0;
/* 206 */     int pos = 0;
/*     */     int idx;
/* 208 */     while ((idx = str.indexOf(token, pos)) != -1) {
/* 209 */       count++;
/* 210 */       pos = idx + token.length();
/*     */     } 
/* 212 */     return count;
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
/*     */   public static String replace(String inString, String oldPattern, String newPattern) {
/* 225 */     if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
/* 226 */       return inString;
/*     */     }
/* 228 */     StringBuilder sb = new StringBuilder();
/* 229 */     int pos = 0;
/* 230 */     int index = inString.indexOf(oldPattern);
/*     */     
/* 232 */     int patLen = oldPattern.length();
/* 233 */     while (index >= 0) {
/* 234 */       sb.append(inString.substring(pos, index));
/* 235 */       sb.append(newPattern);
/* 236 */       pos = index + patLen;
/* 237 */       index = inString.indexOf(oldPattern, pos);
/*     */     } 
/* 239 */     sb.append(inString.substring(pos));
/*     */     
/* 241 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String collectionToCommaDelimitedString(Collection<?> collection) {
/* 252 */     return collectionToDelimitedString(collection, ", ");
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
/*     */   public static String collectionToDelimitedString(Collection<?> collection, String delimiter) {
/* 264 */     if (collection == null) {
/* 265 */       return "";
/*     */     }
/* 267 */     StringBuilder sb = new StringBuilder();
/* 268 */     Iterator<?> it = collection.iterator();
/* 269 */     while (it.hasNext()) {
/* 270 */       sb.append(it.next());
/* 271 */       if (it.hasNext()) {
/* 272 */         sb.append(delimiter);
/*     */       }
/*     */     } 
/* 275 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String trimLeadingWhitespace(String str) {
/* 286 */     if (!hasLength(str)) {
/* 287 */       return str;
/*     */     }
/* 289 */     StringBuilder buf = new StringBuilder(str);
/* 290 */     while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
/* 291 */       buf.deleteCharAt(0);
/*     */     }
/* 293 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String trimTrailingWhitespace(String str) {
/* 304 */     if (!hasLength(str)) {
/* 305 */       return str;
/*     */     }
/* 307 */     StringBuilder buf = new StringBuilder(str);
/* 308 */     while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
/* 309 */       buf.deleteCharAt(buf.length() - 1);
/*     */     }
/* 311 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\StringUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */