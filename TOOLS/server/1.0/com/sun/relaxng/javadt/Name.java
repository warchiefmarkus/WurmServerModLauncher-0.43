/*     */ package 1.0.com.sun.relaxng.javadt;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Name
/*     */ {
/*     */   public static boolean isJavaIdentifier(String token) {
/*  21 */     if (token.length() == 0) return false;
/*     */     
/*  23 */     if (reservedKeywords.contains(token)) return false;
/*     */     
/*  25 */     if (!Character.isJavaIdentifierStart(token.charAt(0))) return false;
/*     */     
/*  27 */     for (int i = 1; i < token.length(); i++) {
/*  28 */       if (!Character.isJavaIdentifierPart(token.charAt(i)))
/*  29 */         return false; 
/*     */     } 
/*  31 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isJavaPackageName(String s) {
/*  38 */     while (s.length() != 0) {
/*  39 */       int idx = s.indexOf('.');
/*  40 */       if (idx == -1) idx = s.length(); 
/*  41 */       if (!isJavaIdentifier(s.substring(0, idx))) {
/*  42 */         return false;
/*     */       }
/*  44 */       s = s.substring(idx);
/*  45 */       if (s.length() != 0) s = s.substring(1); 
/*     */     } 
/*  47 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  52 */   private static HashSet reservedKeywords = new HashSet();
/*     */ 
/*     */   
/*     */   static {
/*  56 */     String[] words = { 
/*  57 */         "abstract", 
/*  58 */         "boolean", 
/*  59 */         "break", 
/*  60 */         "byte", 
/*  61 */         "case", 
/*  62 */         "catch", 
/*  63 */         "char", 
/*  64 */         "class", 
/*  65 */         "const", 
/*  66 */         "continue", 
/*  67 */         "default", 
/*  68 */         "do", 
/*  69 */         "double", 
/*  70 */         "else", 
/*  71 */         "extends", 
/*  72 */         "final", 
/*  73 */         "finally", 
/*  74 */         "float", 
/*  75 */         "for", 
/*  76 */         "goto", 
/*  77 */         "if", 
/*  78 */         "implements", 
/*  79 */         "import", 
/*  80 */         "instanceof", 
/*  81 */         "int", 
/*  82 */         "interface", 
/*  83 */         "long", 
/*  84 */         "native", 
/*  85 */         "new", 
/*  86 */         "package", 
/*  87 */         "private", 
/*  88 */         "protected", 
/*  89 */         "public", 
/*  90 */         "return", 
/*  91 */         "short", 
/*  92 */         "static", 
/*  93 */         "strictfp", 
/*  94 */         "super", 
/*  95 */         "switch", 
/*  96 */         "synchronized", 
/*  97 */         "this", 
/*  98 */         "throw", 
/*  99 */         "throws", 
/* 100 */         "transient", 
/* 101 */         "try", 
/* 102 */         "void", 
/* 103 */         "volatile", 
/* 104 */         "while", 
/*     */ 
/*     */         
/* 107 */         "true", 
/* 108 */         "false", 
/* 109 */         "null", 
/*     */ 
/*     */         
/* 112 */         "assert" };
/*     */     
/* 114 */     for (int i = 0; i < words.length; i++)
/* 115 */       reservedKeywords.add(words[i]); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\relaxng\javadt\Name.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */