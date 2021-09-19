/*     */ package org.seamless.util.io;
/*     */ 
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Random;
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
/*     */ public class MD5Crypt
/*     */ {
/*     */   private static final String SALTCHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
/*     */   private static final String itoa64 = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
/*     */   
/*     */   private static final String to64(long v, int size) {
/*  38 */     StringBuffer result = new StringBuffer();
/*     */     
/*  40 */     while (--size >= 0) {
/*  41 */       result.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt((int)(v & 0x3FL)));
/*  42 */       v >>>= 6L;
/*     */     } 
/*     */     
/*  45 */     return result.toString();
/*     */   }
/*     */   
/*     */   private static final void clearbits(byte[] bits) {
/*  49 */     for (int i = 0; i < bits.length; i++) {
/*  50 */       bits[i] = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int bytes2u(byte inp) {
/*  59 */     return inp & 0xFF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String crypt(String password) {
/*  69 */     StringBuffer salt = new StringBuffer();
/*  70 */     Random rnd = new Random();
/*     */ 
/*     */     
/*  73 */     while (salt.length() < 8) {
/*  74 */       int index = (int)(rnd.nextFloat() * "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".length());
/*  75 */       salt.append("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".substring(index, index + 1));
/*     */     } 
/*     */ 
/*     */     
/*  79 */     return crypt(password, salt.toString(), "$1$");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String crypt(String password, String salt) {
/*  90 */     return crypt(password, salt, "$1$");
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
/*     */ 
/*     */   
/*     */   public static final String crypt(String password, String salt, String magic) {
/*     */     MessageDigest ctx, ctx1;
/*     */     try {
/* 113 */       ctx = MessageDigest.getInstance("md5");
/* 114 */       ctx1 = MessageDigest.getInstance("md5");
/*     */     }
/* 116 */     catch (NoSuchAlgorithmException ex) {
/* 117 */       System.err.println(ex);
/* 118 */       return null;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 124 */     if (salt.startsWith(magic)) {
/* 125 */       salt = salt.substring(magic.length());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 130 */     if (salt.indexOf('$') != -1) {
/* 131 */       salt = salt.substring(0, salt.indexOf('$'));
/*     */     }
/*     */     
/* 134 */     if (salt.length() > 8) {
/* 135 */       salt = salt.substring(0, 8);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     ctx.update(password.getBytes());
/* 145 */     ctx.update(magic.getBytes());
/* 146 */     ctx.update(salt.getBytes());
/*     */ 
/*     */ 
/*     */     
/* 150 */     ctx1.update(password.getBytes());
/* 151 */     ctx1.update(salt.getBytes());
/* 152 */     ctx1.update(password.getBytes());
/* 153 */     byte[] finalState = ctx1.digest();
/*     */     
/* 155 */     for (int pl = password.length(); pl > 0; pl -= 16) {
/* 156 */       ctx.update(finalState, 0, (pl > 16) ? 16 : pl);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     clearbits(finalState);
/*     */     
/*     */     int i;
/*     */     
/* 167 */     for (i = password.length(); i != 0; i >>>= 1) {
/* 168 */       if ((i & 0x1) != 0) {
/* 169 */         ctx.update(finalState, 0, 1);
/*     */       } else {
/* 171 */         ctx.update(password.getBytes(), 0, 1);
/*     */       } 
/*     */     } 
/*     */     
/* 175 */     finalState = ctx.digest();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     for (i = 0; i < 1000; i++) {
/*     */       try {
/* 185 */         ctx1 = MessageDigest.getInstance("md5");
/*     */       }
/* 187 */       catch (NoSuchAlgorithmException e0) {
/* 188 */         return null;
/*     */       } 
/*     */       
/* 191 */       if ((i & 0x1) != 0) {
/* 192 */         ctx1.update(password.getBytes());
/*     */       } else {
/* 194 */         ctx1.update(finalState, 0, 16);
/*     */       } 
/*     */       
/* 197 */       if (i % 3 != 0) {
/* 198 */         ctx1.update(salt.getBytes());
/*     */       }
/*     */       
/* 201 */       if (i % 7 != 0) {
/* 202 */         ctx1.update(password.getBytes());
/*     */       }
/*     */       
/* 205 */       if ((i & 0x1) != 0) {
/* 206 */         ctx1.update(finalState, 0, 16);
/*     */       } else {
/* 208 */         ctx1.update(password.getBytes());
/*     */       } 
/*     */       
/* 211 */       finalState = ctx1.digest();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 216 */     StringBuffer result = new StringBuffer();
/*     */     
/* 218 */     result.append(magic);
/* 219 */     result.append(salt);
/* 220 */     result.append("$");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 225 */     long l = (bytes2u(finalState[0]) << 16 | bytes2u(finalState[6]) << 8 | bytes2u(finalState[12]));
/*     */     
/* 227 */     result.append(to64(l, 4));
/*     */     
/* 229 */     l = (bytes2u(finalState[1]) << 16 | bytes2u(finalState[7]) << 8 | bytes2u(finalState[13]));
/*     */     
/* 231 */     result.append(to64(l, 4));
/*     */     
/* 233 */     l = (bytes2u(finalState[2]) << 16 | bytes2u(finalState[8]) << 8 | bytes2u(finalState[14]));
/*     */     
/* 235 */     result.append(to64(l, 4));
/*     */     
/* 237 */     l = (bytes2u(finalState[3]) << 16 | bytes2u(finalState[9]) << 8 | bytes2u(finalState[15]));
/*     */     
/* 239 */     result.append(to64(l, 4));
/*     */     
/* 241 */     l = (bytes2u(finalState[4]) << 16 | bytes2u(finalState[10]) << 8 | bytes2u(finalState[5]));
/*     */     
/* 243 */     result.append(to64(l, 4));
/*     */     
/* 245 */     l = bytes2u(finalState[11]);
/* 246 */     result.append(to64(l, 2));
/*     */ 
/*     */     
/* 249 */     clearbits(finalState);
/*     */     
/* 251 */     return result.toString();
/*     */   }
/*     */   
/*     */   public static boolean isEqual(String clear, String encrypted) {
/* 255 */     return isEqual(clear.toCharArray(), encrypted);
/*     */   }
/*     */   
/*     */   public static boolean isEqual(char[] clear, String encrypted) {
/* 259 */     String[] split = encrypted.split("\\$");
/* 260 */     if (split.length != 4)
/* 261 */       return false; 
/* 262 */     char[] a = encrypted.toCharArray();
/* 263 */     char[] b = crypt(new String(clear), split[2], "$" + split[1] + "$").toCharArray();
/* 264 */     boolean result = false;
/* 265 */     if (a == null || b == null)
/* 266 */       return (a == b); 
/* 267 */     if (a.length == b.length) {
/* 268 */       boolean equals = true;
/* 269 */       for (int i = 0; i < a.length && equals; i++)
/* 270 */         equals = (a[i] == b[i]); 
/* 271 */       result = equals;
/*     */     } 
/* 273 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\io\MD5Crypt.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */