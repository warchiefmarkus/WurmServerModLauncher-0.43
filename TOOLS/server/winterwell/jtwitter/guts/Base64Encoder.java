/*     */ package winterwell.jtwitter.guts;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Base64Encoder
/*     */ {
/*  40 */   static final char[] charTab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encode(String string) {
/*  47 */     return encode(string.getBytes()).toString();
/*     */   }
/*     */   
/*     */   public static String encode(byte[] data) {
/*  51 */     return encode(data, 0, data.length, null).toString();
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
/*     */   public static StringBuffer encode(byte[] data, int start, int len, StringBuffer buf) {
/*  64 */     if (buf == null) {
/*  65 */       buf = new StringBuffer(data.length * 3 / 2);
/*     */     }
/*  67 */     int end = len - 3;
/*  68 */     int i = start;
/*  69 */     int n = 0;
/*     */     
/*  71 */     while (i <= end) {
/*  72 */       int d = (data[i] & 0xFF) << 16 | (
/*  73 */         data[i + 1] & 0xFF) << 8 | 
/*  74 */         data[i + 2] & 0xFF;
/*     */       
/*  76 */       buf.append(charTab[d >> 18 & 0x3F]);
/*  77 */       buf.append(charTab[d >> 12 & 0x3F]);
/*  78 */       buf.append(charTab[d >> 6 & 0x3F]);
/*  79 */       buf.append(charTab[d & 0x3F]);
/*     */       
/*  81 */       i += 3;
/*     */       
/*  83 */       if (n++ >= 14) {
/*  84 */         n = 0;
/*  85 */         buf.append("\r\n");
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  90 */     if (i == start + len - 2) {
/*  91 */       int d = (data[i] & 0xFF) << 16 | (
/*  92 */         data[i + 1] & 0xFF) << 8;
/*     */       
/*  94 */       buf.append(charTab[d >> 18 & 0x3F]);
/*  95 */       buf.append(charTab[d >> 12 & 0x3F]);
/*  96 */       buf.append(charTab[d >> 6 & 0x3F]);
/*  97 */       buf.append("=");
/*     */     }
/*  99 */     else if (i == start + len - 1) {
/* 100 */       int d = (data[i] & 0xFF) << 16;
/*     */       
/* 102 */       buf.append(charTab[d >> 18 & 0x3F]);
/* 103 */       buf.append(charTab[d >> 12 & 0x3F]);
/* 104 */       buf.append("==");
/*     */     } 
/*     */     
/* 107 */     return buf;
/*     */   }
/*     */ 
/*     */   
/*     */   static int decode(char c) {
/* 112 */     if (c >= 'A' && c <= 'Z')
/* 113 */       return c - 65; 
/* 114 */     if (c >= 'a' && c <= 'z')
/* 115 */       return c - 97 + 26; 
/* 116 */     if (c >= '0' && c <= '9')
/* 117 */       return c - 48 + 26 + 26; 
/* 118 */     switch (c) { case '+':
/* 119 */         return 62;
/* 120 */       case '/': return 63;
/* 121 */       case '=': return 0; }
/*     */     
/* 123 */     throw new RuntimeException("unexpected code: " + c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decode(String s) {
/* 133 */     int i = 0;
/* 134 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 135 */     int len = s.length();
/*     */     
/*     */     while (true) {
/* 138 */       if (i >= len || s.charAt(i) > ' ') {
/*     */         
/* 140 */         if (i == len)
/*     */           break; 
/* 142 */         int tri = (decode(s.charAt(i)) << 18) + (
/* 143 */           decode(s.charAt(i + 1)) << 12) + (
/* 144 */           decode(s.charAt(i + 2)) << 6) + 
/* 145 */           decode(s.charAt(i + 3));
/*     */         
/* 147 */         bos.write(tri >> 16 & 0xFF);
/* 148 */         if (s.charAt(i + 2) == '=')
/* 149 */           break;  bos.write(tri >> 8 & 0xFF);
/* 150 */         if (s.charAt(i + 3) == '=')
/* 151 */           break;  bos.write(tri & 0xFF);
/*     */         
/* 153 */         i += 4; continue;
/*     */       }  i++;
/* 155 */     }  return bos.toByteArray();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\guts\Base64Encoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */