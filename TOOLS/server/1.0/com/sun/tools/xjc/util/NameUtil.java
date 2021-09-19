/*     */ package 1.0.com.sun.tools.xjc.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NameUtil
/*     */ {
/*     */   protected boolean isPunct(char c) {
/*  23 */     return (c == '-' || c == '.' || c == ':' || c == '_' || c == '·' || c == '·' || c == '۝' || c == '۞');
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isDigit(char c) {
/*  28 */     return ((c >= '0' && c <= '9') || Character.isDigit(c));
/*     */   }
/*     */   
/*     */   protected boolean isUpper(char c) {
/*  32 */     return ((c >= 'A' && c <= 'Z') || Character.isUpperCase(c));
/*     */   }
/*     */   
/*     */   protected boolean isLower(char c) {
/*  36 */     return ((c >= 'a' && c <= 'z') || Character.isLowerCase(c));
/*     */   }
/*     */   
/*     */   protected boolean isLetter(char c) {
/*  40 */     return ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || Character.isLetter(c));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String capitalize(String s) {
/*  50 */     if (!isLower(s.charAt(0)))
/*  51 */       return s; 
/*  52 */     StringBuffer sb = new StringBuffer(s.length());
/*  53 */     sb.append(Character.toUpperCase(s.charAt(0)));
/*  54 */     sb.append(s.substring(1).toLowerCase());
/*  55 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int nextBreak(String s, int start) {
/*  60 */     int n = s.length();
/*  61 */     for (int i = start; i < n; i++) {
/*  62 */       char c0 = s.charAt(i);
/*  63 */       if (i < n - 1) {
/*  64 */         char c1 = s.charAt(i + 1);
/*  65 */         if (isPunct(c1)) return i + 1; 
/*  66 */         if (isDigit(c0) && !isDigit(c1)) return i + 1; 
/*  67 */         if (!isDigit(c0) && isDigit(c1)) return i + 1; 
/*  68 */         if (isLower(c0) && !isLower(c1)) return i + 1; 
/*  69 */         if (isLetter(c0) && !isLetter(c1)) return i + 1; 
/*  70 */         if (!isLetter(c0) && isLetter(c1)) return i + 1; 
/*  71 */         if (i < n - 2) {
/*  72 */           char c2 = s.charAt(i + 2);
/*  73 */           if (isUpper(c0) && isUpper(c1) && isLower(c2))
/*  74 */             return i + 1; 
/*     */         } 
/*     */       } 
/*     */     } 
/*  78 */     return -1;
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
/*     */   public String[] toWordList(String s) {
/*  92 */     ArrayList ss = new ArrayList();
/*  93 */     int n = s.length(); int i;
/*  94 */     for (i = 0; i < n; ) {
/*     */ 
/*     */       
/*  97 */       while (i < n && 
/*  98 */         isPunct(s.charAt(i)))
/*     */       {
/* 100 */         i++;
/*     */       }
/* 102 */       if (i >= n) {
/*     */         break;
/*     */       }
/* 105 */       int b = nextBreak(s, i);
/* 106 */       String w = (b == -1) ? s.substring(i) : s.substring(i, b);
/* 107 */       ss.add(escape(capitalize(w)));
/* 108 */       if (b == -1)
/* 109 */         break;  i = b;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     return ss.<String>toArray(new String[0]);
/*     */   }
/*     */   
/*     */   protected String toMixedCaseName(String[] ss, boolean startUpper) {
/* 120 */     StringBuffer sb = new StringBuffer();
/* 121 */     if (ss.length > 0) {
/* 122 */       sb.append(startUpper ? ss[0] : ss[0].toLowerCase());
/* 123 */       for (int i = 1; i < ss.length; i++)
/* 124 */         sb.append(ss[i]); 
/*     */     } 
/* 126 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String toMixedCaseVariableName(String[] ss, boolean startUpper, boolean cdrUpper) {
/* 132 */     if (cdrUpper)
/* 133 */       for (int i = 1; i < ss.length; i++)
/* 134 */         ss[i] = capitalize(ss[i]);  
/* 135 */     StringBuffer sb = new StringBuffer();
/* 136 */     if (ss.length > 0) {
/* 137 */       sb.append(startUpper ? ss[0] : ss[0].toLowerCase());
/* 138 */       for (int i = 1; i < ss.length; i++)
/* 139 */         sb.append(ss[i]); 
/*     */     } 
/* 141 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toConstantName(String s) {
/* 146 */     return toConstantName(toWordList(s));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toConstantName(String[] ss) {
/* 157 */     StringBuffer sb = new StringBuffer();
/* 158 */     if (ss.length > 0) {
/* 159 */       sb.append(ss[0].toUpperCase());
/* 160 */       for (int i = 1; i < ss.length; i++) {
/* 161 */         sb.append('_');
/* 162 */         sb.append(ss[i].toUpperCase());
/*     */       } 
/*     */     } 
/* 165 */     return sb.toString();
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
/*     */   public static void escape(StringBuffer sb, String s, int start) {
/* 184 */     int n = s.length();
/* 185 */     for (int i = start; i < n; i++) {
/* 186 */       char c = s.charAt(i);
/* 187 */       if (Character.isJavaIdentifierPart(c)) {
/* 188 */         sb.append(c);
/*     */       } else {
/* 190 */         sb.append("_");
/* 191 */         if (c <= '\017') { sb.append("000"); }
/* 192 */         else if (c <= 'ÿ') { sb.append("00"); }
/* 193 */         else if (c <= '࿿') { sb.append("0"); }
/* 194 */          sb.append(Integer.toString(c, 16));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String escape(String s) {
/* 204 */     int n = s.length();
/* 205 */     for (int i = 0; i < n; i++) {
/* 206 */       if (!Character.isJavaIdentifierPart(s.charAt(i))) {
/* 207 */         StringBuffer sb = new StringBuffer(s.substring(0, i));
/* 208 */         escape(sb, s, i);
/* 209 */         return sb.toString();
/*     */       } 
/* 211 */     }  return s;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xj\\util\NameUtil.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */