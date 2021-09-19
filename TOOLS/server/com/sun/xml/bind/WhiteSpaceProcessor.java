/*     */ package com.sun.xml.bind;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class WhiteSpaceProcessor
/*     */ {
/*     */   public static String replace(String text) {
/*  64 */     return replace(text).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSequence replace(CharSequence text) {
/*  71 */     int i = text.length() - 1;
/*     */ 
/*     */     
/*  74 */     while (i >= 0 && !isWhiteSpaceExceptSpace(text.charAt(i))) {
/*  75 */       i--;
/*     */     }
/*  77 */     if (i < 0)
/*     */     {
/*  79 */       return text;
/*     */     }
/*     */ 
/*     */     
/*  83 */     StringBuilder buf = new StringBuilder(text);
/*     */     
/*  85 */     buf.setCharAt(i--, ' ');
/*  86 */     for (; i >= 0; i--) {
/*  87 */       if (isWhiteSpaceExceptSpace(buf.charAt(i)))
/*  88 */         buf.setCharAt(i, ' '); 
/*     */     } 
/*  90 */     return new String(buf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSequence trim(CharSequence text) {
/*  98 */     int len = text.length();
/*  99 */     int start = 0;
/*     */     
/* 101 */     while (start < len && isWhiteSpace(text.charAt(start))) {
/* 102 */       start++;
/*     */     }
/* 104 */     int end = len - 1;
/*     */     
/* 106 */     while (end > start && isWhiteSpace(text.charAt(end))) {
/* 107 */       end--;
/*     */     }
/* 109 */     if (start == 0 && end == len - 1) {
/* 110 */       return text;
/*     */     }
/* 112 */     return text.subSequence(start, end + 1);
/*     */   }
/*     */   
/*     */   public static String collapse(String text) {
/* 116 */     return collapse(text).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSequence collapse(CharSequence text) {
/* 125 */     int len = text.length();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     int s = 0;
/* 131 */     while (s < len && 
/* 132 */       !isWhiteSpace(text.charAt(s)))
/*     */     {
/* 134 */       s++;
/*     */     }
/* 136 */     if (s == len)
/*     */     {
/* 138 */       return text;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 143 */     StringBuilder result = new StringBuilder(len);
/*     */     
/* 145 */     if (s != 0) {
/* 146 */       for (int j = 0; j < s; j++)
/* 147 */         result.append(text.charAt(j)); 
/* 148 */       result.append(' ');
/*     */     } 
/*     */     
/* 151 */     boolean inStripMode = true;
/* 152 */     for (int i = s + 1; i < len; i++) {
/* 153 */       char ch = text.charAt(i);
/* 154 */       boolean b = isWhiteSpace(ch);
/* 155 */       if (!inStripMode || !b) {
/*     */ 
/*     */         
/* 158 */         inStripMode = b;
/* 159 */         if (inStripMode) {
/* 160 */           result.append(' ');
/*     */         } else {
/* 162 */           result.append(ch);
/*     */         } 
/*     */       } 
/*     */     } 
/* 166 */     len = result.length();
/* 167 */     if (len > 0 && result.charAt(len - 1) == ' ') {
/* 168 */       result.setLength(len - 1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 173 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean isWhiteSpace(CharSequence s) {
/* 180 */     for (int i = s.length() - 1; i >= 0; i--) {
/* 181 */       if (!isWhiteSpace(s.charAt(i)))
/* 182 */         return false; 
/* 183 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean isWhiteSpace(char ch) {
/* 190 */     if (ch > ' ') return false;
/*     */ 
/*     */     
/* 193 */     return (ch == '\t' || ch == '\n' || ch == '\r' || ch == ' ');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final boolean isWhiteSpaceExceptSpace(char ch) {
/* 203 */     if (ch >= ' ') return false;
/*     */ 
/*     */     
/* 206 */     return (ch == '\t' || ch == '\n' || ch == '\r');
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\WhiteSpaceProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */