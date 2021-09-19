/*     */ package 1.0.com.sun.codemodel;
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
/*     */ public class JJavaName
/*     */ {
/*     */   public static boolean isJavaIdentifier(String s) {
/*  20 */     if (s.length() == 0) return false; 
/*  21 */     if (reservedKeywords.contains(s)) return false;
/*     */     
/*  23 */     if (!Character.isJavaIdentifierStart(s.charAt(0))) return false;
/*     */     
/*  25 */     for (int i = 1; i < s.length(); i++) {
/*  26 */       if (!Character.isJavaIdentifierPart(s.charAt(i)))
/*  27 */         return false; 
/*     */     } 
/*  29 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isJavaPackageName(String s) {
/*  36 */     while (s.length() != 0) {
/*  37 */       int idx = s.indexOf('.');
/*  38 */       if (idx == -1) idx = s.length(); 
/*  39 */       if (!isJavaIdentifier(s.substring(0, idx))) {
/*  40 */         return false;
/*     */       }
/*  42 */       s = s.substring(idx);
/*  43 */       if (s.length() != 0) s = s.substring(1); 
/*     */     } 
/*  45 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  50 */   private static HashSet reservedKeywords = new HashSet();
/*     */ 
/*     */   
/*     */   static {
/*  54 */     String[] words = { "abstract", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "extends", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while", "true", "false", "null", "assert" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     for (int i = 0; i < words.length; i++)
/* 113 */       reservedKeywords.add(words[i]); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JJavaName.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */