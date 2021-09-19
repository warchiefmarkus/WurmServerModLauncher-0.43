/*     */ package org.seamless.util.io;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HexBin
/*     */ {
/*     */   private static final int BASELENGTH = 255;
/*     */   private static final int LOOKUPLENGTH = 16;
/*  31 */   private static byte[] hexNumberTable = new byte[255];
/*  32 */   private static byte[] lookUpHexAlphabet = new byte[16];
/*     */   static {
/*     */     int i;
/*  35 */     for (i = 0; i < 255; i++) {
/*  36 */       hexNumberTable[i] = -1;
/*     */     }
/*  38 */     for (i = 57; i >= 48; i--) {
/*  39 */       hexNumberTable[i] = (byte)(i - 48);
/*     */     }
/*  41 */     for (i = 70; i >= 65; i--) {
/*  42 */       hexNumberTable[i] = (byte)(i - 65 + 10);
/*     */     }
/*  44 */     for (i = 102; i >= 97; i--) {
/*  45 */       hexNumberTable[i] = (byte)(i - 97 + 10);
/*     */     }
/*     */     
/*  48 */     for (i = 0; i < 10; i++)
/*  49 */       lookUpHexAlphabet[i] = (byte)(48 + i); 
/*  50 */     for (i = 10; i <= 15; i++) {
/*  51 */       lookUpHexAlphabet[i] = (byte)(65 + i - 10);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isHex(byte octect) {
/*  61 */     return (hexNumberTable[octect] != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String bytesToString(byte[] binaryData) {
/*  68 */     if (binaryData == null)
/*  69 */       return null; 
/*  70 */     return new String(encode(binaryData));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String bytesToString(byte[] binaryData, String separator) {
/*  77 */     if (binaryData == null)
/*  78 */       return null; 
/*  79 */     String s = new String(encode(binaryData));
/*  80 */     StringBuilder sb = new StringBuilder();
/*  81 */     int i = 1;
/*  82 */     char[] chars = s.toCharArray();
/*  83 */     for (char c : chars) {
/*  84 */       sb.append(c);
/*  85 */       if (i == 2) {
/*  86 */         sb.append(separator);
/*  87 */         i = 1;
/*     */       } else {
/*  89 */         i++;
/*     */       } 
/*     */     } 
/*  92 */     sb.deleteCharAt(sb.length() - 1);
/*  93 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] stringToBytes(String hexEncoded) {
/* 100 */     return decode(hexEncoded.getBytes());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] stringToBytes(String hexEncoded, String separator) {
/* 107 */     return decode(hexEncoded.replaceAll(separator, "").getBytes());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] encode(byte[] binaryData) {
/* 117 */     if (binaryData == null)
/* 118 */       return null; 
/* 119 */     int lengthData = binaryData.length;
/* 120 */     int lengthEncode = lengthData * 2;
/* 121 */     byte[] encodedData = new byte[lengthEncode];
/* 122 */     for (int i = 0; i < lengthData; i++) {
/* 123 */       encodedData[i * 2] = lookUpHexAlphabet[binaryData[i] >> 4 & 0xF];
/* 124 */       encodedData[i * 2 + 1] = lookUpHexAlphabet[binaryData[i] & 0xF];
/*     */     } 
/* 126 */     return encodedData;
/*     */   }
/*     */   
/*     */   public static byte[] decode(byte[] binaryData) {
/* 130 */     if (binaryData == null)
/* 131 */       return null; 
/* 132 */     int lengthData = binaryData.length;
/* 133 */     if (lengthData % 2 != 0) {
/* 134 */       return null;
/*     */     }
/* 136 */     int lengthDecode = lengthData / 2;
/* 137 */     byte[] decodedData = new byte[lengthDecode];
/* 138 */     for (int i = 0; i < lengthDecode; i++) {
/* 139 */       if (!isHex(binaryData[i * 2]) || !isHex(binaryData[i * 2 + 1])) {
/* 140 */         return null;
/*     */       }
/* 142 */       decodedData[i] = (byte)(hexNumberTable[binaryData[i * 2]] << 4 | hexNumberTable[binaryData[i * 2 + 1]]);
/*     */     } 
/* 144 */     return decodedData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String decode(String binaryData) {
/* 154 */     if (binaryData == null) {
/* 155 */       return null;
/*     */     }
/* 157 */     byte[] decoded = null;
/*     */     try {
/* 159 */       decoded = decode(binaryData.getBytes("utf-8"));
/*     */     }
/* 161 */     catch (UnsupportedEncodingException e) {}
/*     */     
/* 163 */     return (decoded == null) ? null : new String(decoded);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encode(String binaryData) {
/* 173 */     if (binaryData == null) {
/* 174 */       return null;
/*     */     }
/* 176 */     byte[] encoded = null;
/*     */     try {
/* 178 */       encoded = encode(binaryData.getBytes("utf-8"));
/*     */     }
/* 180 */     catch (UnsupportedEncodingException e) {}
/*     */     
/* 182 */     return (encoded == null) ? null : new String(encoded);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\io\HexBin.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */