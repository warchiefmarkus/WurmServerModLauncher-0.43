/*     */ package javax.xml.bind;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class WhiteSpaceProcessor
/*     */ {
/*     */   public static String replace(String text) {
/*  29 */     return replace(text).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSequence replace(CharSequence text) {
/*  36 */     int i = text.length() - 1;
/*     */ 
/*     */     
/*  39 */     while (i >= 0 && !isWhiteSpaceExceptSpace(text.charAt(i))) {
/*  40 */       i--;
/*     */     }
/*  42 */     if (i < 0)
/*     */     {
/*  44 */       return text;
/*     */     }
/*     */ 
/*     */     
/*  48 */     StringBuilder buf = new StringBuilder(text);
/*     */     
/*  50 */     buf.setCharAt(i--, ' ');
/*  51 */     for (; i >= 0; i--) {
/*  52 */       if (isWhiteSpaceExceptSpace(buf.charAt(i)))
/*  53 */         buf.setCharAt(i, ' '); 
/*     */     } 
/*  55 */     return new String(buf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSequence trim(CharSequence text) {
/*  63 */     int len = text.length();
/*  64 */     int start = 0;
/*     */     
/*  66 */     while (start < len && isWhiteSpace(text.charAt(start))) {
/*  67 */       start++;
/*     */     }
/*  69 */     int end = len - 1;
/*     */     
/*  71 */     while (end > start && isWhiteSpace(text.charAt(end))) {
/*  72 */       end--;
/*     */     }
/*  74 */     if (start == 0 && end == len - 1) {
/*  75 */       return text;
/*     */     }
/*  77 */     return text.subSequence(start, end + 1);
/*     */   }
/*     */   
/*     */   public static String collapse(String text) {
/*  81 */     return collapse(text).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSequence collapse(CharSequence text) {
/*  90 */     int len = text.length();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  95 */     int s = 0;
/*  96 */     while (s < len && 
/*  97 */       !isWhiteSpace(text.charAt(s)))
/*     */     {
/*  99 */       s++;
/*     */     }
/* 101 */     if (s == len)
/*     */     {
/* 103 */       return text;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 108 */     StringBuilder result = new StringBuilder(len);
/*     */     
/* 110 */     if (s != 0) {
/* 111 */       for (int j = 0; j < s; j++)
/* 112 */         result.append(text.charAt(j)); 
/* 113 */       result.append(' ');
/*     */     } 
/*     */     
/* 116 */     boolean inStripMode = true;
/* 117 */     for (int i = s + 1; i < len; i++) {
/* 118 */       char ch = text.charAt(i);
/* 119 */       boolean b = isWhiteSpace(ch);
/* 120 */       if (!inStripMode || !b) {
/*     */ 
/*     */         
/* 123 */         inStripMode = b;
/* 124 */         if (inStripMode) {
/* 125 */           result.append(' ');
/*     */         } else {
/* 127 */           result.append(ch);
/*     */         } 
/*     */       } 
/*     */     } 
/* 131 */     len = result.length();
/* 132 */     if (len > 0 && result.charAt(len - 1) == ' ') {
/* 133 */       result.setLength(len - 1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 138 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean isWhiteSpace(CharSequence s) {
/* 145 */     for (int i = s.length() - 1; i >= 0; i--) {
/* 146 */       if (!isWhiteSpace(s.charAt(i)))
/* 147 */         return false; 
/* 148 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean isWhiteSpace(char ch) {
/* 155 */     if (ch > ' ') return false;
/*     */ 
/*     */     
/* 158 */     return (ch == '\t' || ch == '\n' || ch == '\r' || ch == ' ');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final boolean isWhiteSpaceExceptSpace(char ch) {
/* 168 */     if (ch >= ' ') return false;
/*     */ 
/*     */     
/* 171 */     return (ch == '\t' || ch == '\n' || ch == '\r');
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\WhiteSpaceProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */