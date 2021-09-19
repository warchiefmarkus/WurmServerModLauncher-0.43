/*     */ package com.sun.codemodel;
/*     */ 
/*     */ import java.util.HashSet;
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
/*     */ public class JJavaName
/*     */ {
/*     */   public static boolean isJavaIdentifier(String s) {
/*  37 */     if (s.length() == 0) return false; 
/*  38 */     if (reservedKeywords.contains(s)) return false;
/*     */     
/*  40 */     if (!Character.isJavaIdentifierStart(s.charAt(0))) return false;
/*     */     
/*  42 */     for (int i = 1; i < s.length(); i++) {
/*  43 */       if (!Character.isJavaIdentifierPart(s.charAt(i)))
/*  44 */         return false; 
/*     */     } 
/*  46 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFullyQualifiedClassName(String s) {
/*  53 */     return isJavaPackageName(s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isJavaPackageName(String s) {
/*  60 */     while (s.length() != 0) {
/*  61 */       int idx = s.indexOf('.');
/*  62 */       if (idx == -1) idx = s.length(); 
/*  63 */       if (!isJavaIdentifier(s.substring(0, idx))) {
/*  64 */         return false;
/*     */       }
/*  66 */       s = s.substring(idx);
/*  67 */       if (s.length() != 0) s = s.substring(1); 
/*     */     } 
/*  69 */     return true;
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
/*     */ 
/*     */   
/*     */   public static String getPluralForm(String word) {
/*     */     int j;
/*  94 */     boolean allUpper = true;
/*     */ 
/*     */ 
/*     */     
/*  98 */     for (int i = 0; i < word.length(); i++) {
/*  99 */       char ch = word.charAt(i);
/* 100 */       if (ch >= '') {
/* 101 */         return word;
/*     */       }
/*     */       
/* 104 */       j = allUpper & (!Character.isLowerCase(ch) ? 1 : 0);
/*     */     } 
/*     */     
/* 107 */     for (Entry e : TABLE) {
/* 108 */       String r = e.apply(word);
/* 109 */       if (r != null) {
/* 110 */         if (j != 0) r = r.toUpperCase(); 
/* 111 */         return r;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 116 */     return word;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 121 */   private static HashSet<String> reservedKeywords = new HashSet<String>();
/*     */   private static final Entry[] TABLE;
/*     */   
/*     */   static {
/* 125 */     String[] words = { "abstract", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "extends", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while", "true", "false", "null", "assert", "enum" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 186 */     for (String w : words) {
/* 187 */       reservedKeywords.add(w);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     String[] source = { "(.*)child", "$1children", "(.+)fe", "$1ves", "(.*)mouse", "$1mise", "(.+)f", "$1ves", "(.+)ch", "$1ches", "(.+)sh", "$1shes", "(.*)tooth", "$1teeth", "(.+)um", "$1a", "(.+)an", "$1en", "(.+)ato", "$1atoes", "(.*)basis", "$1bases", "(.*)axis", "$1axes", "(.+)is", "$1ises", "(.+)us", "$1uses", "(.+)s", "$1s", "(.*)foot", "$1feet", "(.+)ix", "$1ixes", "(.+)ex", "$1ices", "(.+)nx", "$1nxes", "(.+)x", "$1xes", "(.+)y", "$1ies", "(.+)", "$1s" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 240 */     TABLE = new Entry[source.length / 2];
/*     */     
/* 242 */     for (int i = 0; i < source.length; i += 2)
/* 243 */       TABLE[i / 2] = new Entry(source[i], source[i + 1]); 
/*     */   }
/*     */   
/*     */   private static class Entry {
/*     */     private final Pattern pattern;
/*     */     private final String replacement;
/*     */     
/*     */     public Entry(String pattern, String replacement) {
/*     */       this.pattern = Pattern.compile(pattern, 2);
/*     */       this.replacement = replacement;
/*     */     }
/*     */     
/*     */     String apply(String word) {
/*     */       Matcher m = this.pattern.matcher(word);
/*     */       if (m.matches()) {
/*     */         StringBuffer buf = new StringBuffer();
/*     */         m.appendReplacement(buf, this.replacement);
/*     */         return buf.toString();
/*     */       } 
/*     */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JJavaName.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */