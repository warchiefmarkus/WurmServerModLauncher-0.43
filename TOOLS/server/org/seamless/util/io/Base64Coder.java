/*      */ package org.seamless.util.io;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FilterInputStream;
/*      */ import java.io.FilterOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.ObjectStreamClass;
/*      */ import java.io.Serializable;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.CharBuffer;
/*      */ import java.util.zip.GZIPInputStream;
/*      */ import java.util.zip.GZIPOutputStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Base64Coder
/*      */ {
/*      */   public static final int NO_OPTIONS = 0;
/*      */   public static final int ENCODE = 1;
/*      */   public static final int DECODE = 0;
/*      */   public static final int GZIP = 2;
/*      */   public static final int DONT_GUNZIP = 4;
/*      */   public static final int DO_BREAK_LINES = 8;
/*      */   public static final int URL_SAFE = 16;
/*      */   public static final int ORDERED = 32;
/*      */   private static final int MAX_LINE_LENGTH = 76;
/*      */   private static final byte EQUALS_SIGN = 61;
/*      */   private static final byte NEW_LINE = 10;
/*      */   private static final String PREFERRED_ENCODING = "US-ASCII";
/*      */   private static final byte WHITE_SPACE_ENC = -5;
/*      */   private static final byte EQUALS_SIGN_ENC = -1;
/*  224 */   private static final byte[] _STANDARD_ALPHABET = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  242 */   private static final byte[] _STANDARD_DECODABET = new byte[] { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  284 */   private static final byte[] _URL_SAFE_ALPHABET = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  300 */   private static final byte[] _URL_SAFE_DECODABET = new byte[] { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, 63, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  347 */   private static final byte[] _ORDERED_ALPHABET = new byte[] { 45, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 95, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  365 */   private static final byte[] _ORDERED_DECODABET = new byte[] { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 0, -9, -9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -9, -9, -9, -1, -9, -9, -9, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, -9, -9, -9, -9, 37, -9, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final byte[] getAlphabet(int options) {
/*  415 */     if ((options & 0x10) == 16)
/*  416 */       return _URL_SAFE_ALPHABET; 
/*  417 */     if ((options & 0x20) == 32) {
/*  418 */       return _ORDERED_ALPHABET;
/*      */     }
/*  420 */     return _STANDARD_ALPHABET;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final byte[] getDecodabet(int options) {
/*  433 */     if ((options & 0x10) == 16)
/*  434 */       return _URL_SAFE_DECODABET; 
/*  435 */     if ((options & 0x20) == 32) {
/*  436 */       return _ORDERED_DECODABET;
/*      */     }
/*  438 */     return _STANDARD_DECODABET;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] encode3to4(byte[] b4, byte[] threeBytes, int numSigBytes, int options) {
/*  469 */     encode3to4(threeBytes, 0, numSigBytes, b4, 0, options);
/*  470 */     return b4;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset, int options) {
/*  501 */     byte[] ALPHABET = getAlphabet(options);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  514 */     int inBuff = ((numSigBytes > 0) ? (source[srcOffset] << 24 >>> 8) : 0) | ((numSigBytes > 1) ? (source[srcOffset + 1] << 24 >>> 16) : 0) | ((numSigBytes > 2) ? (source[srcOffset + 2] << 24 >>> 24) : 0);
/*      */ 
/*      */ 
/*      */     
/*  518 */     switch (numSigBytes) {
/*      */       
/*      */       case 3:
/*  521 */         destination[destOffset] = ALPHABET[inBuff >>> 18];
/*  522 */         destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3F];
/*  523 */         destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 0x3F];
/*  524 */         destination[destOffset + 3] = ALPHABET[inBuff & 0x3F];
/*  525 */         return destination;
/*      */       
/*      */       case 2:
/*  528 */         destination[destOffset] = ALPHABET[inBuff >>> 18];
/*  529 */         destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3F];
/*  530 */         destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 0x3F];
/*  531 */         destination[destOffset + 3] = 61;
/*  532 */         return destination;
/*      */       
/*      */       case 1:
/*  535 */         destination[destOffset] = ALPHABET[inBuff >>> 18];
/*  536 */         destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3F];
/*  537 */         destination[destOffset + 2] = 61;
/*  538 */         destination[destOffset + 3] = 61;
/*  539 */         return destination;
/*      */     } 
/*      */     
/*  542 */     return destination;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void encode(ByteBuffer raw, ByteBuffer encoded) {
/*  560 */     byte[] raw3 = new byte[3];
/*  561 */     byte[] enc4 = new byte[4];
/*      */     
/*  563 */     while (raw.hasRemaining()) {
/*  564 */       int rem = Math.min(3, raw.remaining());
/*  565 */       raw.get(raw3, 0, rem);
/*  566 */       encode3to4(enc4, raw3, rem, 0);
/*  567 */       encoded.put(enc4);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void encode(ByteBuffer raw, CharBuffer encoded) {
/*  584 */     byte[] raw3 = new byte[3];
/*  585 */     byte[] enc4 = new byte[4];
/*      */     
/*  587 */     while (raw.hasRemaining()) {
/*  588 */       int rem = Math.min(3, raw.remaining());
/*  589 */       raw.get(raw3, 0, rem);
/*  590 */       encode3to4(enc4, raw3, rem, 0);
/*  591 */       for (int i = 0; i < 4; i++) {
/*  592 */         encoded.put((char)(enc4[i] & 0xFF));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String encodeObject(Serializable serializableObject) throws IOException {
/*  620 */     return encodeObject(serializableObject, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String encodeObject(Serializable serializableObject, int options) throws IOException {
/*  657 */     if (serializableObject == null) {
/*  658 */       throw new NullPointerException("Cannot serialize a null object.");
/*      */     }
/*      */ 
/*      */     
/*  662 */     ByteArrayOutputStream baos = null;
/*  663 */     java.io.OutputStream b64os = null;
/*  664 */     GZIPOutputStream gzos = null;
/*  665 */     ObjectOutputStream oos = null;
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  670 */       baos = new ByteArrayOutputStream();
/*  671 */       b64os = new OutputStream(baos, 0x1 | options);
/*  672 */       if ((options & 0x2) != 0) {
/*      */         
/*  674 */         gzos = new GZIPOutputStream(b64os);
/*  675 */         oos = new ObjectOutputStream(gzos);
/*      */       } else {
/*      */         
/*  678 */         oos = new ObjectOutputStream(b64os);
/*      */       } 
/*  680 */       oos.writeObject(serializableObject);
/*      */     }
/*  682 */     catch (IOException e) {
/*      */ 
/*      */       
/*  685 */       throw e;
/*      */     } finally {
/*      */       
/*  688 */       try { oos.close(); } catch (Exception e) {} 
/*  689 */       try { gzos.close(); } catch (Exception e) {} 
/*  690 */       try { b64os.close(); } catch (Exception e) {} 
/*  691 */       try { baos.close(); } catch (Exception e) {}
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/*  696 */       return new String(baos.toByteArray(), "US-ASCII");
/*      */     }
/*  698 */     catch (UnsupportedEncodingException uue) {
/*      */       
/*  700 */       return new String(baos.toByteArray());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String encodeBytes(byte[] source) {
/*  720 */     String encoded = null;
/*      */     try {
/*  722 */       encoded = encodeBytes(source, 0, source.length, 0);
/*  723 */     } catch (IOException ex) {
/*  724 */       assert false : ex.getMessage();
/*      */     } 
/*  726 */     assert encoded != null;
/*  727 */     return encoded;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String encodeBytes(byte[] source, int options) throws IOException {
/*  762 */     return encodeBytes(source, 0, source.length, options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String encodeBytes(byte[] source, int off, int len) {
/*  788 */     String encoded = null;
/*      */     try {
/*  790 */       encoded = encodeBytes(source, off, len, 0);
/*  791 */     } catch (IOException ex) {
/*  792 */       assert false : ex.getMessage();
/*      */     } 
/*  794 */     assert encoded != null;
/*  795 */     return encoded;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String encodeBytes(byte[] source, int off, int len, int options) throws IOException {
/*  833 */     byte[] encoded = encodeBytesToBytes(source, off, len, options);
/*      */ 
/*      */     
/*      */     try {
/*  837 */       return new String(encoded, "US-ASCII");
/*      */     }
/*  839 */     catch (UnsupportedEncodingException uue) {
/*  840 */       return new String(encoded);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] encodeBytesToBytes(byte[] source) {
/*  860 */     byte[] encoded = null;
/*      */     try {
/*  862 */       encoded = encodeBytesToBytes(source, 0, source.length, 0);
/*  863 */     } catch (IOException ex) {
/*  864 */       assert false : "IOExceptions only come from GZipping, which is turned off: " + ex.getMessage();
/*      */     } 
/*  866 */     return encoded;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] encodeBytesToBytes(byte[] source, int off, int len, int options) throws IOException {
/*  890 */     if (source == null) {
/*  891 */       throw new NullPointerException("Cannot serialize a null array.");
/*      */     }
/*      */     
/*  894 */     if (off < 0) {
/*  895 */       throw new IllegalArgumentException("Cannot have negative offset: " + off);
/*      */     }
/*      */     
/*  898 */     if (len < 0) {
/*  899 */       throw new IllegalArgumentException("Cannot have length offset: " + len);
/*      */     }
/*      */     
/*  902 */     if (off + len > source.length) {
/*  903 */       throw new IllegalArgumentException(String.format("Cannot have offset of %d and length of %d with array of length %d", new Object[] { Integer.valueOf(off), Integer.valueOf(len), Integer.valueOf(source.length) }));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  910 */     if ((options & 0x2) != 0) {
/*  911 */       ByteArrayOutputStream baos = null;
/*  912 */       GZIPOutputStream gzos = null;
/*  913 */       OutputStream b64os = null;
/*      */ 
/*      */       
/*      */       try {
/*  917 */         baos = new ByteArrayOutputStream();
/*  918 */         b64os = new OutputStream(baos, 0x1 | options);
/*  919 */         gzos = new GZIPOutputStream(b64os);
/*      */         
/*  921 */         gzos.write(source, off, len);
/*  922 */         gzos.close();
/*      */       }
/*  924 */       catch (IOException iOException) {
/*      */ 
/*      */         
/*  927 */         throw iOException;
/*      */       } finally {
/*      */         
/*  930 */         try { gzos.close(); } catch (Exception exception) {} 
/*  931 */         try { b64os.close(); } catch (Exception exception) {} 
/*  932 */         try { baos.close(); } catch (Exception exception) {}
/*      */       } 
/*      */       
/*  935 */       return baos.toByteArray();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  940 */     boolean breakLines = ((options & 0x8) != 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  949 */     int encLen = len / 3 * 4 + ((len % 3 > 0) ? 4 : 0);
/*  950 */     if (breakLines) {
/*  951 */       encLen += encLen / 76;
/*      */     }
/*  953 */     byte[] outBuff = new byte[encLen];
/*      */ 
/*      */     
/*  956 */     int d = 0;
/*  957 */     int e = 0;
/*  958 */     int len2 = len - 2;
/*  959 */     int lineLength = 0;
/*  960 */     for (; d < len2; d += 3, e += 4) {
/*  961 */       encode3to4(source, d + off, 3, outBuff, e, options);
/*      */       
/*  963 */       lineLength += 4;
/*  964 */       if (breakLines && lineLength >= 76) {
/*      */         
/*  966 */         outBuff[e + 4] = 10;
/*  967 */         e++;
/*  968 */         lineLength = 0;
/*      */       } 
/*      */     } 
/*      */     
/*  972 */     if (d < len) {
/*  973 */       encode3to4(source, d + off, len - d, outBuff, e, options);
/*  974 */       e += 4;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  979 */     if (e <= outBuff.length - 1) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  984 */       byte[] finalOut = new byte[e];
/*  985 */       System.arraycopy(outBuff, 0, finalOut, 0, e);
/*      */       
/*  987 */       return finalOut;
/*      */     } 
/*      */     
/*  990 */     return outBuff;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset, int options) {
/* 1037 */     if (source == null) {
/* 1038 */       throw new NullPointerException("Source array was null.");
/*      */     }
/* 1040 */     if (destination == null) {
/* 1041 */       throw new NullPointerException("Destination array was null.");
/*      */     }
/* 1043 */     if (srcOffset < 0 || srcOffset + 3 >= source.length) {
/* 1044 */       throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and still process four bytes.", new Object[] { Integer.valueOf(source.length), Integer.valueOf(srcOffset) }));
/*      */     }
/*      */     
/* 1047 */     if (destOffset < 0 || destOffset + 2 >= destination.length) {
/* 1048 */       throw new IllegalArgumentException(String.format("Destination array with length %d cannot have offset of %d and still store three bytes.", new Object[] { Integer.valueOf(destination.length), Integer.valueOf(destOffset) }));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1053 */     byte[] DECODABET = getDecodabet(options);
/*      */ 
/*      */     
/* 1056 */     if (source[srcOffset + 2] == 61) {
/*      */ 
/*      */ 
/*      */       
/* 1060 */       int i = (DECODABET[source[srcOffset]] & 0xFF) << 18 | (DECODABET[source[srcOffset + 1]] & 0xFF) << 12;
/*      */ 
/*      */       
/* 1063 */       destination[destOffset] = (byte)(i >>> 16);
/* 1064 */       return 1;
/*      */     } 
/*      */ 
/*      */     
/* 1068 */     if (source[srcOffset + 3] == 61) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1073 */       int i = (DECODABET[source[srcOffset]] & 0xFF) << 18 | (DECODABET[source[srcOffset + 1]] & 0xFF) << 12 | (DECODABET[source[srcOffset + 2]] & 0xFF) << 6;
/*      */ 
/*      */ 
/*      */       
/* 1077 */       destination[destOffset] = (byte)(i >>> 16);
/* 1078 */       destination[destOffset + 1] = (byte)(i >>> 8);
/* 1079 */       return 2;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1089 */     int outBuff = (DECODABET[source[srcOffset]] & 0xFF) << 18 | (DECODABET[source[srcOffset + 1]] & 0xFF) << 12 | (DECODABET[source[srcOffset + 2]] & 0xFF) << 6 | DECODABET[source[srcOffset + 3]] & 0xFF;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1095 */     destination[destOffset] = (byte)(outBuff >> 16);
/* 1096 */     destination[destOffset + 1] = (byte)(outBuff >> 8);
/* 1097 */     destination[destOffset + 2] = (byte)outBuff;
/*      */     
/* 1099 */     return 3;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] decode(byte[] source) throws IOException {
/* 1122 */     byte[] decoded = null;
/*      */     
/* 1124 */     decoded = decode(source, 0, source.length, 0);
/*      */ 
/*      */ 
/*      */     
/* 1128 */     return decoded;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] decode(byte[] source, int off, int len, int options) throws IOException {
/* 1154 */     if (source == null) {
/* 1155 */       throw new NullPointerException("Cannot decode null source array.");
/*      */     }
/* 1157 */     if (off < 0 || off + len > source.length) {
/* 1158 */       throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and process %d bytes.", new Object[] { Integer.valueOf(source.length), Integer.valueOf(off), Integer.valueOf(len) }));
/*      */     }
/*      */ 
/*      */     
/* 1162 */     if (len == 0)
/* 1163 */       return new byte[0]; 
/* 1164 */     if (len < 4) {
/* 1165 */       throw new IllegalArgumentException("Base64-encoded string must have at least four characters, but length specified was " + len);
/*      */     }
/*      */ 
/*      */     
/* 1169 */     byte[] DECODABET = getDecodabet(options);
/*      */     
/* 1171 */     int len34 = len * 3 / 4;
/* 1172 */     byte[] outBuff = new byte[len34];
/* 1173 */     int outBuffPosn = 0;
/*      */     
/* 1175 */     byte[] b4 = new byte[4];
/* 1176 */     int b4Posn = 0;
/* 1177 */     int i = 0;
/* 1178 */     byte sbiDecode = 0;
/*      */     
/* 1180 */     for (i = off; i < off + len; i++) {
/*      */       
/* 1182 */       sbiDecode = DECODABET[source[i] & 0xFF];
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1187 */       if (sbiDecode >= -5) {
/* 1188 */         if (sbiDecode >= -1) {
/* 1189 */           b4[b4Posn++] = source[i];
/* 1190 */           if (b4Posn > 3) {
/* 1191 */             outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, options);
/* 1192 */             b4Posn = 0;
/*      */ 
/*      */             
/* 1195 */             if (source[i] == 61) {
/*      */               break;
/*      */             }
/*      */           }
/*      */         
/*      */         } 
/*      */       } else {
/*      */         
/* 1203 */         throw new IOException(String.format("Bad Base64 input character decimal %d in array position %d", new Object[] { Integer.valueOf(source[i] & 0xFF), Integer.valueOf(i) }));
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1208 */     byte[] out = new byte[outBuffPosn];
/* 1209 */     System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
/* 1210 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] decode(String s) throws IOException {
/* 1226 */     return decode(s, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] decode(String s, int options) throws IOException {
/* 1244 */     if (s == null) {
/* 1245 */       throw new NullPointerException("Input string was null.");
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/* 1250 */       arrayOfByte = s.getBytes("US-ASCII");
/*      */     }
/* 1252 */     catch (UnsupportedEncodingException uee) {
/* 1253 */       arrayOfByte = s.getBytes();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1258 */     byte[] arrayOfByte = decode(arrayOfByte, 0, arrayOfByte.length, options);
/*      */ 
/*      */ 
/*      */     
/* 1262 */     boolean dontGunzip = ((options & 0x4) != 0);
/* 1263 */     if (arrayOfByte != null && arrayOfByte.length >= 4 && !dontGunzip) {
/*      */       
/* 1265 */       int head = arrayOfByte[0] & 0xFF | arrayOfByte[1] << 8 & 0xFF00;
/* 1266 */       if (35615 == head) {
/* 1267 */         ByteArrayInputStream bais = null;
/* 1268 */         GZIPInputStream gzis = null;
/* 1269 */         ByteArrayOutputStream baos = null;
/* 1270 */         byte[] buffer = new byte[2048];
/* 1271 */         int length = 0;
/*      */         
/*      */         try {
/* 1274 */           baos = new ByteArrayOutputStream();
/* 1275 */           bais = new ByteArrayInputStream(arrayOfByte);
/* 1276 */           gzis = new GZIPInputStream(bais);
/*      */           
/* 1278 */           while ((length = gzis.read(buffer)) >= 0) {
/* 1279 */             baos.write(buffer, 0, length);
/*      */           }
/*      */ 
/*      */           
/* 1283 */           arrayOfByte = baos.toByteArray();
/*      */         
/*      */         }
/* 1286 */         catch (IOException e) {
/* 1287 */           e.printStackTrace();
/*      */         } finally {
/*      */ 
/*      */           
/* 1291 */           try { baos.close(); } catch (Exception e) {} 
/* 1292 */           try { gzis.close(); } catch (Exception e) {} 
/* 1293 */           try { bais.close(); } catch (Exception e) {}
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1299 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object decodeToObject(String encodedObject) throws IOException, ClassNotFoundException {
/* 1318 */     return decodeToObject(encodedObject, 0, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object decodeToObject(String encodedObject, int options, final ClassLoader loader) throws IOException, ClassNotFoundException {
/* 1343 */     byte[] objBytes = decode(encodedObject, options);
/*      */     
/* 1345 */     ByteArrayInputStream bais = null;
/* 1346 */     ObjectInputStream ois = null;
/* 1347 */     Object obj = null;
/*      */     
/*      */     try {
/* 1350 */       bais = new ByteArrayInputStream(objBytes);
/*      */ 
/*      */       
/* 1353 */       if (loader == null) {
/* 1354 */         ois = new ObjectInputStream(bais);
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 1360 */         ois = new ObjectInputStream(bais)
/*      */           {
/*      */             public Class<?> resolveClass(ObjectStreamClass streamClass) throws IOException, ClassNotFoundException
/*      */             {
/* 1364 */               Class<?> c = Class.forName(streamClass.getName(), false, loader);
/* 1365 */               if (c == null) {
/* 1366 */                 return super.resolveClass(streamClass);
/*      */               }
/* 1368 */               return c;
/*      */             }
/*      */           };
/*      */       } 
/*      */ 
/*      */       
/* 1374 */       obj = ois.readObject();
/*      */     }
/* 1376 */     catch (IOException e) {
/* 1377 */       throw e;
/*      */     }
/* 1379 */     catch (ClassNotFoundException e) {
/* 1380 */       throw e;
/*      */     } finally {
/*      */       
/* 1383 */       try { bais.close(); } catch (Exception e) {} 
/* 1384 */       try { ois.close(); } catch (Exception e) {}
/*      */     } 
/*      */     
/* 1387 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void encodeToFile(byte[] dataToEncode, String filename) throws IOException {
/* 1409 */     if (dataToEncode == null) {
/* 1410 */       throw new NullPointerException("Data to encode was null.");
/*      */     }
/*      */     
/* 1413 */     OutputStream bos = null;
/*      */     try {
/* 1415 */       bos = new OutputStream(new FileOutputStream(filename), 1);
/*      */       
/* 1417 */       bos.write(dataToEncode);
/*      */     }
/* 1419 */     catch (IOException e) {
/* 1420 */       throw e;
/*      */     } finally {
/*      */       
/* 1423 */       try { bos.close(); } catch (Exception e) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void decodeToFile(String dataToDecode, String filename) throws IOException {
/* 1445 */     OutputStream bos = null;
/*      */     try {
/* 1447 */       bos = new OutputStream(new FileOutputStream(filename), 0);
/*      */       
/* 1449 */       bos.write(dataToDecode.getBytes("US-ASCII"));
/*      */     }
/* 1451 */     catch (IOException e) {
/* 1452 */       throw e;
/*      */     } finally {
/*      */       
/* 1455 */       try { bos.close(); } catch (Exception e) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] decodeFromFile(String filename) throws IOException {
/* 1480 */     byte[] decodedData = null;
/* 1481 */     InputStream bis = null;
/*      */ 
/*      */     
/*      */     try {
/* 1485 */       File file = new File(filename);
/* 1486 */       byte[] buffer = null;
/* 1487 */       int length = 0;
/* 1488 */       int numBytes = 0;
/*      */ 
/*      */       
/* 1491 */       if (file.length() > 2147483647L)
/*      */       {
/* 1493 */         throw new IOException("File is too big for this convenience method (" + file.length() + " bytes).");
/*      */       }
/* 1495 */       buffer = new byte[(int)file.length()];
/*      */ 
/*      */       
/* 1498 */       bis = new InputStream(new BufferedInputStream(new FileInputStream(file)), 0);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1503 */       while ((numBytes = bis.read(buffer, length, 4096)) >= 0) {
/* 1504 */         length += numBytes;
/*      */       }
/*      */ 
/*      */       
/* 1508 */       decodedData = new byte[length];
/* 1509 */       System.arraycopy(buffer, 0, decodedData, 0, length);
/*      */     
/*      */     }
/* 1512 */     catch (IOException e) {
/* 1513 */       throw e;
/*      */     } finally {
/*      */       
/* 1516 */       try { bis.close(); } catch (Exception e) {}
/*      */     } 
/*      */     
/* 1519 */     return decodedData;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String encodeFromFile(String filename) throws IOException {
/* 1541 */     String encodedData = null;
/* 1542 */     InputStream bis = null;
/*      */ 
/*      */     
/*      */     try {
/* 1546 */       File file = new File(filename);
/* 1547 */       byte[] buffer = new byte[Math.max((int)(file.length() * 1.4D + 1.0D), 40)];
/* 1548 */       int length = 0;
/* 1549 */       int numBytes = 0;
/*      */ 
/*      */       
/* 1552 */       bis = new InputStream(new BufferedInputStream(new FileInputStream(file)), 1);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1557 */       while ((numBytes = bis.read(buffer, length, 4096)) >= 0) {
/* 1558 */         length += numBytes;
/*      */       }
/*      */ 
/*      */       
/* 1562 */       encodedData = new String(buffer, 0, length, "US-ASCII");
/*      */     
/*      */     }
/* 1565 */     catch (IOException e) {
/* 1566 */       throw e;
/*      */     } finally {
/*      */       
/* 1569 */       try { bis.close(); } catch (Exception e) {}
/*      */     } 
/*      */     
/* 1572 */     return encodedData;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void encodeFileToFile(String infile, String outfile) throws IOException {
/* 1586 */     String encoded = encodeFromFile(infile);
/* 1587 */     java.io.OutputStream out = null;
/*      */     try {
/* 1589 */       out = new BufferedOutputStream(new FileOutputStream(outfile));
/*      */       
/* 1591 */       out.write(encoded.getBytes("US-ASCII"));
/*      */     }
/* 1593 */     catch (IOException e) {
/* 1594 */       throw e;
/*      */     } finally {
/*      */       try {
/* 1597 */         out.close();
/* 1598 */       } catch (Exception ex) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void decodeFileToFile(String infile, String outfile) throws IOException {
/* 1614 */     byte[] decoded = decodeFromFile(infile);
/* 1615 */     java.io.OutputStream out = null;
/*      */     try {
/* 1617 */       out = new BufferedOutputStream(new FileOutputStream(outfile));
/*      */       
/* 1619 */       out.write(decoded);
/*      */     }
/* 1621 */     catch (IOException e) {
/* 1622 */       throw e;
/*      */     } finally {
/*      */       try {
/* 1625 */         out.close();
/* 1626 */       } catch (Exception ex) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class InputStream
/*      */     extends FilterInputStream
/*      */   {
/*      */     private boolean encode;
/*      */ 
/*      */     
/*      */     private int position;
/*      */ 
/*      */     
/*      */     private byte[] buffer;
/*      */ 
/*      */     
/*      */     private int bufferLength;
/*      */ 
/*      */     
/*      */     private int numSigBytes;
/*      */ 
/*      */     
/*      */     private int lineLength;
/*      */ 
/*      */     
/*      */     private boolean breakLines;
/*      */ 
/*      */     
/*      */     private int options;
/*      */ 
/*      */     
/*      */     private byte[] decodabet;
/*      */ 
/*      */     
/*      */     public InputStream(java.io.InputStream in) {
/* 1663 */       this(in, 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public InputStream(java.io.InputStream in, int options) {
/* 1689 */       super(in);
/* 1690 */       this.options = options;
/* 1691 */       this.breakLines = ((options & 0x8) > 0);
/* 1692 */       this.encode = ((options & 0x1) > 0);
/* 1693 */       this.bufferLength = this.encode ? 4 : 3;
/* 1694 */       this.buffer = new byte[this.bufferLength];
/* 1695 */       this.position = -1;
/* 1696 */       this.lineLength = 0;
/* 1697 */       this.decodabet = Base64Coder.getDecodabet(options);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int read() throws IOException {
/* 1711 */       if (this.position < 0) {
/* 1712 */         if (this.encode) {
/* 1713 */           byte[] b3 = new byte[3];
/* 1714 */           int numBinaryBytes = 0;
/* 1715 */           for (int i = 0; i < 3; ) {
/* 1716 */             int b = this.in.read();
/*      */ 
/*      */             
/* 1719 */             if (b >= 0) {
/* 1720 */               b3[i] = (byte)b;
/* 1721 */               numBinaryBytes++;
/*      */ 
/*      */               
/*      */               i++;
/*      */             } 
/*      */           } 
/*      */           
/* 1728 */           if (numBinaryBytes > 0) {
/* 1729 */             Base64Coder.encode3to4(b3, 0, numBinaryBytes, this.buffer, 0, this.options);
/* 1730 */             this.position = 0;
/* 1731 */             this.numSigBytes = 4;
/*      */           } else {
/*      */             
/* 1734 */             return -1;
/*      */           }
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 1740 */           byte[] b4 = new byte[4];
/* 1741 */           int i = 0;
/* 1742 */           for (i = 0; i < 4; i++) {
/*      */             
/* 1744 */             int b = 0; do {
/* 1745 */               b = this.in.read();
/* 1746 */             } while (b >= 0 && this.decodabet[b & 0x7F] <= -5);
/*      */             
/* 1748 */             if (b < 0) {
/*      */               break;
/*      */             }
/*      */             
/* 1752 */             b4[i] = (byte)b;
/*      */           } 
/*      */           
/* 1755 */           if (i == 4) {
/* 1756 */             this.numSigBytes = Base64Coder.decode4to3(b4, 0, this.buffer, 0, this.options);
/* 1757 */             this.position = 0;
/*      */           } else {
/* 1759 */             if (i == 0) {
/* 1760 */               return -1;
/*      */             }
/*      */ 
/*      */             
/* 1764 */             throw new IOException("Improperly padded Base64 input.");
/*      */           } 
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1771 */       if (this.position >= 0) {
/*      */         
/* 1773 */         if (this.position >= this.numSigBytes) {
/* 1774 */           return -1;
/*      */         }
/*      */         
/* 1777 */         if (this.encode && this.breakLines && this.lineLength >= 76) {
/* 1778 */           this.lineLength = 0;
/* 1779 */           return 10;
/*      */         } 
/*      */         
/* 1782 */         this.lineLength++;
/*      */ 
/*      */ 
/*      */         
/* 1786 */         int b = this.buffer[this.position++];
/*      */         
/* 1788 */         if (this.position >= this.bufferLength) {
/* 1789 */           this.position = -1;
/*      */         }
/*      */         
/* 1792 */         return b & 0xFF;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1799 */       throw new IOException("Error in Base64 code reading stream.");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int read(byte[] dest, int off, int len) throws IOException {
/*      */       int i;
/* 1821 */       for (i = 0; i < len; i++) {
/* 1822 */         int b = read();
/*      */         
/* 1824 */         if (b >= 0) {
/* 1825 */           dest[off + i] = (byte)b;
/*      */         } else {
/* 1827 */           if (i == 0) {
/* 1828 */             return -1;
/*      */           }
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/* 1834 */       return i;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class OutputStream
/*      */     extends FilterOutputStream
/*      */   {
/*      */     private boolean encode;
/*      */ 
/*      */     
/*      */     private int position;
/*      */ 
/*      */     
/*      */     private byte[] buffer;
/*      */ 
/*      */     
/*      */     private int bufferLength;
/*      */ 
/*      */     
/*      */     private int lineLength;
/*      */ 
/*      */     
/*      */     private boolean breakLines;
/*      */ 
/*      */     
/*      */     private byte[] b4;
/*      */ 
/*      */     
/*      */     private boolean suspendEncoding;
/*      */ 
/*      */     
/*      */     private int options;
/*      */ 
/*      */     
/*      */     private byte[] decodabet;
/*      */ 
/*      */ 
/*      */     
/*      */     public OutputStream(java.io.OutputStream out) {
/* 1876 */       this(out, 1);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public OutputStream(java.io.OutputStream out, int options) {
/* 1900 */       super(out);
/* 1901 */       this.breakLines = ((options & 0x8) != 0);
/* 1902 */       this.encode = ((options & 0x1) != 0);
/* 1903 */       this.bufferLength = this.encode ? 3 : 4;
/* 1904 */       this.buffer = new byte[this.bufferLength];
/* 1905 */       this.position = 0;
/* 1906 */       this.lineLength = 0;
/* 1907 */       this.suspendEncoding = false;
/* 1908 */       this.b4 = new byte[4];
/* 1909 */       this.options = options;
/* 1910 */       this.decodabet = Base64Coder.getDecodabet(options);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void write(int theByte) throws IOException {
/* 1930 */       if (this.suspendEncoding) {
/* 1931 */         this.out.write(theByte);
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/* 1936 */       if (this.encode) {
/* 1937 */         this.buffer[this.position++] = (byte)theByte;
/* 1938 */         if (this.position >= this.bufferLength)
/*      */         {
/* 1940 */           this.out.write(Base64Coder.encode3to4(this.b4, this.buffer, this.bufferLength, this.options));
/*      */           
/* 1942 */           this.lineLength += 4;
/* 1943 */           if (this.breakLines && this.lineLength >= 76) {
/* 1944 */             this.out.write(10);
/* 1945 */             this.lineLength = 0;
/*      */           } 
/*      */           
/* 1948 */           this.position = 0;
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/* 1955 */       else if (this.decodabet[theByte & 0x7F] > -5) {
/* 1956 */         this.buffer[this.position++] = (byte)theByte;
/* 1957 */         if (this.position >= this.bufferLength)
/*      */         {
/* 1959 */           int len = Base64Coder.decode4to3(this.buffer, 0, this.b4, 0, this.options);
/* 1960 */           this.out.write(this.b4, 0, len);
/* 1961 */           this.position = 0;
/*      */         }
/*      */       
/* 1964 */       } else if (this.decodabet[theByte & 0x7F] != -5) {
/* 1965 */         throw new IOException("Invalid character in Base64 data.");
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void write(byte[] theBytes, int off, int len) throws IOException {
/* 1985 */       if (this.suspendEncoding) {
/* 1986 */         this.out.write(theBytes, off, len);
/*      */         
/*      */         return;
/*      */       } 
/* 1990 */       for (int i = 0; i < len; i++) {
/* 1991 */         write(theBytes[off + i]);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void flushBase64() throws IOException {
/* 2004 */       if (this.position > 0) {
/* 2005 */         if (this.encode) {
/* 2006 */           this.out.write(Base64Coder.encode3to4(this.b4, this.buffer, this.position, this.options));
/* 2007 */           this.position = 0;
/*      */         } else {
/*      */           
/* 2010 */           throw new IOException("Base64 input not properly padded.");
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void close() throws IOException {
/* 2025 */       flushBase64();
/*      */ 
/*      */ 
/*      */       
/* 2029 */       super.close();
/*      */       
/* 2031 */       this.buffer = null;
/* 2032 */       this.out = null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void suspendEncoding() throws IOException {
/* 2046 */       flushBase64();
/* 2047 */       this.suspendEncoding = true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void resumeEncoding() {
/* 2059 */       this.suspendEncoding = false;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] encode(byte[] bytes) {
/* 2071 */     return encodeBytes(bytes).getBytes();
/*      */   }
/*      */   
/*      */   public static String encodeString(String s) {
/* 2075 */     return encodeBytes(s.getBytes());
/*      */   }
/*      */   
/*      */   public static String decodeString(String s) {
/*      */     try {
/* 2080 */       return new String(decode(s));
/* 2081 */     } catch (IOException ex) {
/* 2082 */       throw new RuntimeException(ex);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\io\Base64Coder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */