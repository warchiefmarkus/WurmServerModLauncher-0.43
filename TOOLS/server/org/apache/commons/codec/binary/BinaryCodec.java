/*     */ package org.apache.commons.codec.binary;
/*     */ 
/*     */ import org.apache.commons.codec.BinaryDecoder;
/*     */ import org.apache.commons.codec.BinaryEncoder;
/*     */ import org.apache.commons.codec.DecoderException;
/*     */ import org.apache.commons.codec.EncoderException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BinaryCodec
/*     */   implements BinaryDecoder, BinaryEncoder
/*     */ {
/*  41 */   private static final char[] EMPTY_CHAR_ARRAY = new char[0];
/*     */ 
/*     */   
/*  44 */   private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*     */ 
/*     */   
/*     */   private static final int BIT_0 = 1;
/*     */ 
/*     */   
/*     */   private static final int BIT_1 = 2;
/*     */ 
/*     */   
/*     */   private static final int BIT_2 = 4;
/*     */ 
/*     */   
/*     */   private static final int BIT_3 = 8;
/*     */ 
/*     */   
/*     */   private static final int BIT_4 = 16;
/*     */ 
/*     */   
/*     */   private static final int BIT_5 = 32;
/*     */ 
/*     */   
/*     */   private static final int BIT_6 = 64;
/*     */ 
/*     */   
/*     */   private static final int BIT_7 = 128;
/*     */   
/*  70 */   private static final int[] BITS = new int[] { 1, 2, 4, 8, 16, 32, 64, 128 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] encode(byte[] raw) {
/*  81 */     return toAsciiBytes(raw);
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
/*     */   public Object encode(Object raw) throws EncoderException {
/*  95 */     if (!(raw instanceof byte[])) {
/*  96 */       throw new EncoderException("argument not a byte array");
/*     */     }
/*  98 */     return toAsciiChars((byte[])raw);
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
/*     */   public Object decode(Object ascii) throws DecoderException {
/* 112 */     if (ascii == null) {
/* 113 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/* 115 */     if (ascii instanceof byte[]) {
/* 116 */       return fromAscii((byte[])ascii);
/*     */     }
/* 118 */     if (ascii instanceof char[]) {
/* 119 */       return fromAscii((char[])ascii);
/*     */     }
/* 121 */     if (ascii instanceof String) {
/* 122 */       return fromAscii(((String)ascii).toCharArray());
/*     */     }
/* 124 */     throw new DecoderException("argument not a byte array");
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
/*     */   public byte[] decode(byte[] ascii) {
/* 136 */     return fromAscii(ascii);
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
/*     */   public byte[] toByteArray(String ascii) {
/* 148 */     if (ascii == null) {
/* 149 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/* 151 */     return fromAscii(ascii.toCharArray());
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
/*     */   public static byte[] fromAscii(char[] ascii) {
/* 167 */     if (ascii == null || ascii.length == 0) {
/* 168 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/*     */     
/* 171 */     byte[] l_raw = new byte[ascii.length >> 3];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 176 */     for (int ii = 0, jj = ascii.length - 1; ii < l_raw.length; ii++, jj -= 8) {
/* 177 */       for (int bits = 0; bits < BITS.length; bits++) {
/* 178 */         if (ascii[jj - bits] == '1') {
/* 179 */           l_raw[ii] = (byte)(l_raw[ii] | BITS[bits]);
/*     */         }
/*     */       } 
/*     */     } 
/* 183 */     return l_raw;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] fromAscii(byte[] ascii) {
/* 194 */     if (isEmpty(ascii)) {
/* 195 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/*     */     
/* 198 */     byte[] l_raw = new byte[ascii.length >> 3];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 203 */     for (int ii = 0, jj = ascii.length - 1; ii < l_raw.length; ii++, jj -= 8) {
/* 204 */       for (int bits = 0; bits < BITS.length; bits++) {
/* 205 */         if (ascii[jj - bits] == 49) {
/* 206 */           l_raw[ii] = (byte)(l_raw[ii] | BITS[bits]);
/*     */         }
/*     */       } 
/*     */     } 
/* 210 */     return l_raw;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isEmpty(byte[] array) {
/* 221 */     return (array == null || array.length == 0);
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
/*     */   public static byte[] toAsciiBytes(byte[] raw) {
/* 234 */     if (isEmpty(raw)) {
/* 235 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/*     */     
/* 238 */     byte[] l_ascii = new byte[raw.length << 3];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 243 */     for (int ii = 0, jj = l_ascii.length - 1; ii < raw.length; ii++, jj -= 8) {
/* 244 */       for (int bits = 0; bits < BITS.length; bits++) {
/* 245 */         if ((raw[ii] & BITS[bits]) == 0) {
/* 246 */           l_ascii[jj - bits] = 48;
/*     */         } else {
/* 248 */           l_ascii[jj - bits] = 49;
/*     */         } 
/*     */       } 
/*     */     } 
/* 252 */     return l_ascii;
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
/*     */   public static char[] toAsciiChars(byte[] raw) {
/* 264 */     if (isEmpty(raw)) {
/* 265 */       return EMPTY_CHAR_ARRAY;
/*     */     }
/*     */     
/* 268 */     char[] l_ascii = new char[raw.length << 3];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 273 */     for (int ii = 0, jj = l_ascii.length - 1; ii < raw.length; ii++, jj -= 8) {
/* 274 */       for (int bits = 0; bits < BITS.length; bits++) {
/* 275 */         if ((raw[ii] & BITS[bits]) == 0) {
/* 276 */           l_ascii[jj - bits] = '0';
/*     */         } else {
/* 278 */           l_ascii[jj - bits] = '1';
/*     */         } 
/*     */       } 
/*     */     } 
/* 282 */     return l_ascii;
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
/*     */   public static String toAsciiString(byte[] raw) {
/* 294 */     return new String(toAsciiChars(raw));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\commons\codec\binary\BinaryCodec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */