/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
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
/*     */ class Security
/*     */ {
/*     */   private static final char PVERSION41_CHAR = '*';
/*     */   private static final int SHA1_HASH_SIZE = 20;
/*     */   
/*     */   private static int charVal(char c) {
/*  47 */     return (c >= '0' && c <= '9') ? (c - 48) : ((c >= 'A' && c <= 'Z') ? (c - 65 + 10) : (c - 97 + 10));
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
/*     */   static byte[] createKeyFromOldPassword(String passwd) throws NoSuchAlgorithmException {
/*  78 */     passwd = makeScrambledPassword(passwd);
/*     */ 
/*     */     
/*  81 */     int[] salt = getSaltFromPassword(passwd);
/*     */ 
/*     */     
/*  84 */     return getBinaryPassword(salt, false);
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
/*     */   static byte[] getBinaryPassword(int[] salt, boolean usingNewPasswords) throws NoSuchAlgorithmException {
/* 102 */     int val = 0;
/*     */     
/* 104 */     byte[] binaryPassword = new byte[20];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     if (usingNewPasswords) {
/* 110 */       int pos = 0;
/*     */       
/* 112 */       for (int j = 0; j < 4; j++) {
/* 113 */         val = salt[j];
/*     */         
/* 115 */         for (int t = 3; t >= 0; t--) {
/* 116 */           binaryPassword[pos++] = (byte)(val & 0xFF);
/* 117 */           val >>= 8;
/*     */         } 
/*     */       } 
/*     */       
/* 121 */       return binaryPassword;
/*     */     } 
/*     */     
/* 124 */     int offset = 0;
/*     */     
/* 126 */     for (int i = 0; i < 2; i++) {
/* 127 */       val = salt[i];
/*     */       
/* 129 */       for (int t = 3; t >= 0; t--) {
/* 130 */         binaryPassword[t + offset] = (byte)(val % 256);
/* 131 */         val >>= 8;
/*     */       } 
/*     */       
/* 134 */       offset += 4;
/*     */     } 
/*     */     
/* 137 */     MessageDigest md = MessageDigest.getInstance("SHA-1");
/*     */     
/* 139 */     md.update(binaryPassword, 0, 8);
/*     */     
/* 141 */     return md.digest();
/*     */   }
/*     */   
/*     */   private static int[] getSaltFromPassword(String password) {
/* 145 */     int[] result = new int[6];
/*     */     
/* 147 */     if (password == null || password.length() == 0) {
/* 148 */       return result;
/*     */     }
/*     */     
/* 151 */     if (password.charAt(0) == '*') {
/*     */       
/* 153 */       String saltInHex = password.substring(1, 5);
/*     */       
/* 155 */       int val = 0;
/*     */       
/* 157 */       for (int i = 0; i < 4; i++) {
/* 158 */         val = (val << 4) + charVal(saltInHex.charAt(i));
/*     */       }
/*     */       
/* 161 */       return result;
/*     */     } 
/*     */     
/* 164 */     int resultPos = 0;
/* 165 */     int pos = 0;
/* 166 */     int length = password.length();
/*     */     
/* 168 */     while (pos < length) {
/* 169 */       int val = 0;
/*     */       
/* 171 */       for (int i = 0; i < 8; i++) {
/* 172 */         val = (val << 4) + charVal(password.charAt(pos++));
/*     */       }
/*     */       
/* 175 */       result[resultPos++] = val;
/*     */     } 
/*     */     
/* 178 */     return result;
/*     */   }
/*     */   
/*     */   private static String longToHex(long val) {
/* 182 */     String longHex = Long.toHexString(val);
/*     */     
/* 184 */     int length = longHex.length();
/*     */     
/* 186 */     if (length < 8) {
/* 187 */       int padding = 8 - length;
/* 188 */       StringBuffer buf = new StringBuffer();
/*     */       
/* 190 */       for (int i = 0; i < padding; i++) {
/* 191 */         buf.append("0");
/*     */       }
/*     */       
/* 194 */       buf.append(longHex);
/*     */       
/* 196 */       return buf.toString();
/*     */     } 
/*     */     
/* 199 */     return longHex.substring(0, 8);
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
/*     */   static String makeScrambledPassword(String password) throws NoSuchAlgorithmException {
/* 217 */     long[] passwordHash = Util.newHash(password);
/* 218 */     StringBuffer scramble = new StringBuffer();
/*     */     
/* 220 */     scramble.append(longToHex(passwordHash[0]));
/* 221 */     scramble.append(longToHex(passwordHash[1]));
/*     */     
/* 223 */     return scramble.toString();
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
/*     */   static void passwordCrypt(byte[] from, byte[] to, byte[] password, int length) {
/* 242 */     int pos = 0;
/*     */     
/* 244 */     while (pos < from.length && pos < length) {
/* 245 */       to[pos] = (byte)(from[pos] ^ password[pos]);
/* 246 */       pos++;
/*     */     } 
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
/*     */   static byte[] passwordHashStage1(String password) throws NoSuchAlgorithmException {
/* 263 */     MessageDigest md = MessageDigest.getInstance("SHA-1");
/* 264 */     StringBuffer cleansedPassword = new StringBuffer();
/*     */     
/* 266 */     int passwordLength = password.length();
/*     */     
/* 268 */     for (int i = 0; i < passwordLength; i++) {
/* 269 */       char c = password.charAt(i);
/*     */       
/* 271 */       if (c != ' ' && c != '\t')
/*     */       {
/*     */ 
/*     */         
/* 275 */         cleansedPassword.append(c);
/*     */       }
/*     */     } 
/* 278 */     return md.digest(cleansedPassword.toString().getBytes());
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
/*     */   static byte[] passwordHashStage2(byte[] hashedPassword, byte[] salt) throws NoSuchAlgorithmException {
/* 296 */     MessageDigest md = MessageDigest.getInstance("SHA-1");
/*     */ 
/*     */     
/* 299 */     md.update(salt, 0, 4);
/*     */     
/* 301 */     md.update(hashedPassword, 0, 20);
/*     */     
/* 303 */     return md.digest();
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
/*     */   
/*     */   static byte[] scramble411(String password, String seed, Connection conn) throws NoSuchAlgorithmException, UnsupportedEncodingException {
/* 325 */     MessageDigest md = MessageDigest.getInstance("SHA-1");
/* 326 */     String passwordEncoding = conn.getPasswordCharacterEncoding();
/*     */     
/* 328 */     byte[] passwordHashStage1 = md.digest((passwordEncoding == null || passwordEncoding.length() == 0) ? password.getBytes() : password.getBytes(passwordEncoding));
/*     */ 
/*     */ 
/*     */     
/* 332 */     md.reset();
/*     */     
/* 334 */     byte[] passwordHashStage2 = md.digest(passwordHashStage1);
/* 335 */     md.reset();
/*     */     
/* 337 */     byte[] seedAsBytes = seed.getBytes("ASCII");
/* 338 */     md.update(seedAsBytes);
/* 339 */     md.update(passwordHashStage2);
/*     */     
/* 341 */     byte[] toBeXord = md.digest();
/*     */     
/* 343 */     int numToXor = toBeXord.length;
/*     */     
/* 345 */     for (int i = 0; i < numToXor; i++) {
/* 346 */       toBeXord[i] = (byte)(toBeXord[i] ^ passwordHashStage1[i]);
/*     */     }
/*     */     
/* 349 */     return toBeXord;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\Security.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */