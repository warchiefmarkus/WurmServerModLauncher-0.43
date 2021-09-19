/*     */ package 1.0.com.sun.xml.xsom.impl.util;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Uri
/*     */ {
/*  41 */   private static String utf8 = "UTF-8";
/*     */   
/*     */   public static boolean isValid(String s) {
/*  44 */     return (isValidPercent(s) && isValidFragment(s) && isValidScheme(s));
/*     */   }
/*     */   
/*     */   private static final String HEX_DIGITS = "0123456789abcdef";
/*     */   
/*     */   public static String escapeDisallowedChars(String s) {
/*  50 */     StringBuffer buf = null;
/*  51 */     int len = s.length();
/*  52 */     int done = 0; while (true) {
/*     */       byte[] bytes;
/*  54 */       int i = done;
/*     */       while (true) {
/*  56 */         if (i == len) {
/*  57 */           if (done == 0)
/*  58 */             return s; 
/*     */           break;
/*     */         } 
/*  61 */         if (isExcluded(s.charAt(i)))
/*     */           break; 
/*  63 */         i++;
/*     */       } 
/*  65 */       if (buf == null)
/*  66 */         buf = new StringBuffer(); 
/*  67 */       if (i > done) {
/*  68 */         buf.append(s.substring(done, i));
/*  69 */         done = i;
/*     */       } 
/*  71 */       if (i == len)
/*     */         break; 
/*  73 */       for (; ++i < len && isExcluded(s.charAt(i)); i++);
/*     */       
/*  75 */       String tem = s.substring(done, i);
/*     */       
/*     */       try {
/*  78 */         bytes = tem.getBytes(utf8);
/*     */       }
/*  80 */       catch (UnsupportedEncodingException e) {
/*  81 */         utf8 = "UTF8";
/*     */         try {
/*  83 */           bytes = tem.getBytes(utf8);
/*     */         }
/*  85 */         catch (UnsupportedEncodingException e2) {
/*     */           
/*  87 */           return s;
/*     */         } 
/*     */       } 
/*  90 */       for (int j = 0; j < bytes.length; j++) {
/*  91 */         buf.append('%');
/*  92 */         buf.append("0123456789abcdef".charAt((bytes[j] & 0xFF) >> 4));
/*  93 */         buf.append("0123456789abcdef".charAt(bytes[j] & 0xF));
/*     */       } 
/*  95 */       done = i;
/*     */     } 
/*  97 */     return buf.toString();
/*     */   }
/*     */   
/* 100 */   private static String excluded = "<>\"{}|\\^`";
/*     */   
/*     */   private static boolean isExcluded(char c) {
/* 103 */     return (c <= ' ' || c >= '' || excluded.indexOf(c) >= 0);
/*     */   }
/*     */   
/*     */   private static boolean isAlpha(char c) {
/* 107 */     return (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z'));
/*     */   }
/*     */   
/*     */   private static boolean isHexDigit(char c) {
/* 111 */     return (('a' <= c && c <= 'f') || ('A' <= c && c <= 'F') || isDigit(c));
/*     */   }
/*     */   
/*     */   private static boolean isDigit(char c) {
/* 115 */     return ('0' <= c && c <= '9');
/*     */   }
/*     */   
/*     */   private static boolean isSchemeChar(char c) {
/* 119 */     return (isAlpha(c) || isDigit(c) || c == '+' || c == '-' || c == '.');
/*     */   }
/*     */   
/*     */   private static boolean isValidPercent(String s) {
/* 123 */     int len = s.length();
/* 124 */     for (int i = 0; i < len; i++) {
/* 125 */       if (s.charAt(i) == '%') {
/* 126 */         if (i + 2 >= len)
/* 127 */           return false; 
/* 128 */         if (!isHexDigit(s.charAt(i + 1)) || !isHexDigit(s.charAt(i + 2)))
/*     */         {
/* 130 */           return false; } 
/*     */       } 
/* 132 */     }  return true;
/*     */   }
/*     */   
/*     */   private static boolean isValidFragment(String s) {
/* 136 */     int i = s.indexOf('#');
/* 137 */     return (i < 0 || s.indexOf('#', i + 1) < 0);
/*     */   }
/*     */   
/*     */   private static boolean isValidScheme(String s) {
/* 141 */     if (!isAbsolute(s))
/* 142 */       return true; 
/* 143 */     int i = s.indexOf(':');
/* 144 */     if (i == 0 || i + 1 == s.length() || !isAlpha(s.charAt(0)))
/*     */     {
/*     */       
/* 147 */       return false; } 
/* 148 */     while (--i > 0) {
/* 149 */       if (!isSchemeChar(s.charAt(i)))
/* 150 */         return false; 
/* 151 */     }  return true;
/*     */   }
/*     */   
/*     */   public static String resolve(String baseUri, String uriReference) {
/* 155 */     if (!isAbsolute(uriReference) && baseUri != null && isAbsolute(baseUri)) {
/*     */       try {
/* 157 */         return (new URL(new URL(baseUri), uriReference)).toString();
/*     */       }
/* 159 */       catch (MalformedURLException e) {}
/*     */     }
/* 161 */     return uriReference;
/*     */   }
/*     */   
/*     */   public static boolean hasFragmentId(String uri) {
/* 165 */     return (uri.indexOf('#') >= 0);
/*     */   }
/*     */   
/*     */   public static boolean isAbsolute(String uri) {
/* 169 */     int i = uri.indexOf(':');
/* 170 */     if (i < 0)
/* 171 */       return false; 
/* 172 */     while (--i >= 0) {
/* 173 */       switch (uri.charAt(i)) {
/*     */         case '#':
/*     */         case '/':
/*     */         case '?':
/* 177 */           return false;
/*     */       } 
/*     */     } 
/* 180 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\imp\\util\Uri.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */