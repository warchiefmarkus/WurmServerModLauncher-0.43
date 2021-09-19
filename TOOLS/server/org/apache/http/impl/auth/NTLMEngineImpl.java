/*      */ package org.apache.http.impl.auth;
/*      */ 
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.security.Key;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.SecureRandom;
/*      */ import java.util.Arrays;
/*      */ import java.util.Locale;
/*      */ import javax.crypto.Cipher;
/*      */ import javax.crypto.spec.SecretKeySpec;
/*      */ import org.apache.commons.codec.binary.Base64;
/*      */ import org.apache.http.util.EncodingUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ final class NTLMEngineImpl
/*      */   implements NTLMEngine
/*      */ {
/*      */   protected static final int FLAG_REQUEST_UNICODE_ENCODING = 1;
/*      */   protected static final int FLAG_REQUEST_TARGET = 4;
/*      */   protected static final int FLAG_REQUEST_SIGN = 16;
/*      */   protected static final int FLAG_REQUEST_SEAL = 32;
/*      */   protected static final int FLAG_REQUEST_LAN_MANAGER_KEY = 128;
/*      */   protected static final int FLAG_REQUEST_NTLMv1 = 512;
/*      */   protected static final int FLAG_DOMAIN_PRESENT = 4096;
/*      */   protected static final int FLAG_WORKSTATION_PRESENT = 8192;
/*      */   protected static final int FLAG_REQUEST_ALWAYS_SIGN = 32768;
/*      */   protected static final int FLAG_REQUEST_NTLM2_SESSION = 524288;
/*      */   protected static final int FLAG_REQUEST_VERSION = 33554432;
/*      */   protected static final int FLAG_TARGETINFO_PRESENT = 8388608;
/*      */   protected static final int FLAG_REQUEST_128BIT_KEY_EXCH = 536870912;
/*      */   protected static final int FLAG_REQUEST_EXPLICIT_KEY_EXCH = 1073741824;
/*      */   protected static final int FLAG_REQUEST_56BIT_ENCRYPTION = -2147483648;
/*      */   private static final SecureRandom RND_GEN;
/*      */   static final String DEFAULT_CHARSET = "ASCII";
/*      */   
/*      */   static {
/*   73 */     SecureRandom rnd = null;
/*      */     try {
/*   75 */       rnd = SecureRandom.getInstance("SHA1PRNG");
/*   76 */     } catch (Exception e) {}
/*      */     
/*   78 */     RND_GEN = rnd;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   91 */     byte[] bytesWithoutNull = EncodingUtils.getBytes("NTLMSSP", "ASCII");
/*   92 */     SIGNATURE = new byte[bytesWithoutNull.length + 1];
/*   93 */     System.arraycopy(bytesWithoutNull, 0, SIGNATURE, 0, bytesWithoutNull.length);
/*   94 */     SIGNATURE[bytesWithoutNull.length] = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String credentialCharset = "ASCII";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] SIGNATURE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final String getResponseFor(String message, String username, String password, String host, String domain) throws NTLMEngineException {
/*      */     String response;
/*  118 */     if (message == null || message.trim().equals("")) {
/*  119 */       response = getType1Message(host, domain);
/*      */     } else {
/*  121 */       Type2Message t2m = new Type2Message(message);
/*  122 */       response = getType3Message(username, password, host, domain, t2m.getChallenge(), t2m.getFlags(), t2m.getTarget(), t2m.getTargetInfo());
/*      */     } 
/*      */     
/*  125 */     return response;
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
/*      */   String getType1Message(String host, String domain) throws NTLMEngineException {
/*  140 */     return (new Type1Message(domain, host)).getResponse();
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
/*      */   String getType3Message(String user, String password, String host, String domain, byte[] nonce, int type2Flags, String target, byte[] targetInformation) throws NTLMEngineException {
/*  166 */     return (new Type3Message(domain, host, user, password, nonce, type2Flags, target, targetInformation)).getResponse();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String getCredentialCharset() {
/*  174 */     return this.credentialCharset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setCredentialCharset(String credentialCharset) {
/*  182 */     this.credentialCharset = credentialCharset;
/*      */   }
/*      */ 
/*      */   
/*      */   private static String stripDotSuffix(String value) {
/*  187 */     if (value == null) {
/*  188 */       return null;
/*      */     }
/*  190 */     int index = value.indexOf(".");
/*  191 */     if (index != -1)
/*  192 */       return value.substring(0, index); 
/*  193 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   private static String convertHost(String host) {
/*  198 */     return stripDotSuffix(host);
/*      */   }
/*      */ 
/*      */   
/*      */   private static String convertDomain(String domain) {
/*  203 */     return stripDotSuffix(domain);
/*      */   }
/*      */   
/*      */   private static int readULong(byte[] src, int index) throws NTLMEngineException {
/*  207 */     if (src.length < index + 4)
/*  208 */       throw new NTLMEngineException("NTLM authentication - buffer too small for DWORD"); 
/*  209 */     return src[index] & 0xFF | (src[index + 1] & 0xFF) << 8 | (src[index + 2] & 0xFF) << 16 | (src[index + 3] & 0xFF) << 24;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int readUShort(byte[] src, int index) throws NTLMEngineException {
/*  214 */     if (src.length < index + 2)
/*  215 */       throw new NTLMEngineException("NTLM authentication - buffer too small for WORD"); 
/*  216 */     return src[index] & 0xFF | (src[index + 1] & 0xFF) << 8;
/*      */   }
/*      */   
/*      */   private static byte[] readSecurityBuffer(byte[] src, int index) throws NTLMEngineException {
/*  220 */     int length = readUShort(src, index);
/*  221 */     int offset = readULong(src, index + 4);
/*  222 */     if (src.length < offset + length) {
/*  223 */       throw new NTLMEngineException("NTLM authentication - buffer too small for data item");
/*      */     }
/*  225 */     byte[] buffer = new byte[length];
/*  226 */     System.arraycopy(src, offset, buffer, 0, length);
/*  227 */     return buffer;
/*      */   }
/*      */ 
/*      */   
/*      */   private static byte[] makeRandomChallenge() throws NTLMEngineException {
/*  232 */     if (RND_GEN == null) {
/*  233 */       throw new NTLMEngineException("Random generator not available");
/*      */     }
/*  235 */     byte[] rval = new byte[8];
/*  236 */     synchronized (RND_GEN) {
/*  237 */       RND_GEN.nextBytes(rval);
/*      */     } 
/*  239 */     return rval;
/*      */   }
/*      */ 
/*      */   
/*      */   private static byte[] makeSecondaryKey() throws NTLMEngineException {
/*  244 */     if (RND_GEN == null) {
/*  245 */       throw new NTLMEngineException("Random generator not available");
/*      */     }
/*  247 */     byte[] rval = new byte[16];
/*  248 */     synchronized (RND_GEN) {
/*  249 */       RND_GEN.nextBytes(rval);
/*      */     } 
/*  251 */     return rval;
/*      */   }
/*      */ 
/*      */   
/*      */   protected static class CipherGen
/*      */   {
/*      */     protected final String domain;
/*      */     
/*      */     protected final String user;
/*      */     
/*      */     protected final String password;
/*      */     
/*      */     protected final byte[] challenge;
/*      */     protected final String target;
/*      */     protected final byte[] targetInformation;
/*      */     protected byte[] clientChallenge;
/*      */     protected byte[] clientChallenge2;
/*      */     protected byte[] secondaryKey;
/*      */     protected byte[] timestamp;
/*  270 */     protected byte[] lmHash = null;
/*  271 */     protected byte[] lmResponse = null;
/*  272 */     protected byte[] ntlmHash = null;
/*  273 */     protected byte[] ntlmResponse = null;
/*  274 */     protected byte[] ntlmv2Hash = null;
/*  275 */     protected byte[] lmv2Hash = null;
/*  276 */     protected byte[] lmv2Response = null;
/*  277 */     protected byte[] ntlmv2Blob = null;
/*  278 */     protected byte[] ntlmv2Response = null;
/*  279 */     protected byte[] ntlm2SessionResponse = null;
/*  280 */     protected byte[] lm2SessionResponse = null;
/*  281 */     protected byte[] lmUserSessionKey = null;
/*  282 */     protected byte[] ntlmUserSessionKey = null;
/*  283 */     protected byte[] ntlmv2UserSessionKey = null;
/*  284 */     protected byte[] ntlm2SessionResponseUserSessionKey = null;
/*  285 */     protected byte[] lanManagerSessionKey = null;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public CipherGen(String domain, String user, String password, byte[] challenge, String target, byte[] targetInformation, byte[] clientChallenge, byte[] clientChallenge2, byte[] secondaryKey, byte[] timestamp) {
/*  291 */       this.domain = domain;
/*  292 */       this.target = target;
/*  293 */       this.user = user;
/*  294 */       this.password = password;
/*  295 */       this.challenge = challenge;
/*  296 */       this.targetInformation = targetInformation;
/*  297 */       this.clientChallenge = clientChallenge;
/*  298 */       this.clientChallenge2 = clientChallenge2;
/*  299 */       this.secondaryKey = secondaryKey;
/*  300 */       this.timestamp = timestamp;
/*      */     }
/*      */ 
/*      */     
/*      */     public CipherGen(String domain, String user, String password, byte[] challenge, String target, byte[] targetInformation) {
/*  305 */       this(domain, user, password, challenge, target, targetInformation, null, null, null, null);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getClientChallenge() throws NTLMEngineException {
/*  311 */       if (this.clientChallenge == null)
/*  312 */         this.clientChallenge = NTLMEngineImpl.makeRandomChallenge(); 
/*  313 */       return this.clientChallenge;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getClientChallenge2() throws NTLMEngineException {
/*  319 */       if (this.clientChallenge2 == null)
/*  320 */         this.clientChallenge2 = NTLMEngineImpl.makeRandomChallenge(); 
/*  321 */       return this.clientChallenge2;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getSecondaryKey() throws NTLMEngineException {
/*  327 */       if (this.secondaryKey == null)
/*  328 */         this.secondaryKey = NTLMEngineImpl.makeSecondaryKey(); 
/*  329 */       return this.secondaryKey;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getLMHash() throws NTLMEngineException {
/*  335 */       if (this.lmHash == null)
/*  336 */         this.lmHash = NTLMEngineImpl.lmHash(this.password); 
/*  337 */       return this.lmHash;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getLMResponse() throws NTLMEngineException {
/*  343 */       if (this.lmResponse == null)
/*  344 */         this.lmResponse = NTLMEngineImpl.lmResponse(getLMHash(), this.challenge); 
/*  345 */       return this.lmResponse;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLMHash() throws NTLMEngineException {
/*  351 */       if (this.ntlmHash == null)
/*  352 */         this.ntlmHash = NTLMEngineImpl.ntlmHash(this.password); 
/*  353 */       return this.ntlmHash;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLMResponse() throws NTLMEngineException {
/*  359 */       if (this.ntlmResponse == null)
/*  360 */         this.ntlmResponse = NTLMEngineImpl.lmResponse(getNTLMHash(), this.challenge); 
/*  361 */       return this.ntlmResponse;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getLMv2Hash() throws NTLMEngineException {
/*  367 */       if (this.lmv2Hash == null)
/*  368 */         this.lmv2Hash = NTLMEngineImpl.lmv2Hash(this.domain, this.user, getNTLMHash()); 
/*  369 */       return this.lmv2Hash;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLMv2Hash() throws NTLMEngineException {
/*  375 */       if (this.ntlmv2Hash == null)
/*  376 */         this.ntlmv2Hash = NTLMEngineImpl.ntlmv2Hash(this.domain, this.user, getNTLMHash()); 
/*  377 */       return this.ntlmv2Hash;
/*      */     }
/*      */ 
/*      */     
/*      */     public byte[] getTimestamp() {
/*  382 */       if (this.timestamp == null) {
/*  383 */         long time = System.currentTimeMillis();
/*  384 */         time += 11644473600000L;
/*  385 */         time *= 10000L;
/*      */         
/*  387 */         this.timestamp = new byte[8];
/*  388 */         for (int i = 0; i < 8; i++) {
/*  389 */           this.timestamp[i] = (byte)(int)time;
/*  390 */           time >>>= 8L;
/*      */         } 
/*      */       } 
/*  393 */       return this.timestamp;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLMv2Blob() throws NTLMEngineException {
/*  399 */       if (this.ntlmv2Blob == null)
/*  400 */         this.ntlmv2Blob = NTLMEngineImpl.createBlob(getClientChallenge2(), this.targetInformation, getTimestamp()); 
/*  401 */       return this.ntlmv2Blob;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLMv2Response() throws NTLMEngineException {
/*  407 */       if (this.ntlmv2Response == null)
/*  408 */         this.ntlmv2Response = NTLMEngineImpl.lmv2Response(getNTLMv2Hash(), this.challenge, getNTLMv2Blob()); 
/*  409 */       return this.ntlmv2Response;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getLMv2Response() throws NTLMEngineException {
/*  415 */       if (this.lmv2Response == null)
/*  416 */         this.lmv2Response = NTLMEngineImpl.lmv2Response(getLMv2Hash(), this.challenge, getClientChallenge()); 
/*  417 */       return this.lmv2Response;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLM2SessionResponse() throws NTLMEngineException {
/*  423 */       if (this.ntlm2SessionResponse == null)
/*  424 */         this.ntlm2SessionResponse = NTLMEngineImpl.ntlm2SessionResponse(getNTLMHash(), this.challenge, getClientChallenge()); 
/*  425 */       return this.ntlm2SessionResponse;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getLM2SessionResponse() throws NTLMEngineException {
/*  431 */       if (this.lm2SessionResponse == null) {
/*  432 */         byte[] clientChallenge = getClientChallenge();
/*  433 */         this.lm2SessionResponse = new byte[24];
/*  434 */         System.arraycopy(clientChallenge, 0, this.lm2SessionResponse, 0, clientChallenge.length);
/*  435 */         Arrays.fill(this.lm2SessionResponse, clientChallenge.length, this.lm2SessionResponse.length, (byte)0);
/*      */       } 
/*  437 */       return this.lm2SessionResponse;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getLMUserSessionKey() throws NTLMEngineException {
/*  443 */       if (this.lmUserSessionKey == null) {
/*  444 */         byte[] lmHash = getLMHash();
/*  445 */         this.lmUserSessionKey = new byte[16];
/*  446 */         System.arraycopy(lmHash, 0, this.lmUserSessionKey, 0, 8);
/*  447 */         Arrays.fill(this.lmUserSessionKey, 8, 16, (byte)0);
/*      */       } 
/*  449 */       return this.lmUserSessionKey;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLMUserSessionKey() throws NTLMEngineException {
/*  455 */       if (this.ntlmUserSessionKey == null) {
/*  456 */         byte[] ntlmHash = getNTLMHash();
/*  457 */         NTLMEngineImpl.MD4 md4 = new NTLMEngineImpl.MD4();
/*  458 */         md4.update(ntlmHash);
/*  459 */         this.ntlmUserSessionKey = md4.getOutput();
/*      */       } 
/*  461 */       return this.ntlmUserSessionKey;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLMv2UserSessionKey() throws NTLMEngineException {
/*  467 */       if (this.ntlmv2UserSessionKey == null) {
/*  468 */         byte[] ntlmv2hash = getNTLMv2Hash();
/*  469 */         byte[] truncatedResponse = new byte[16];
/*  470 */         System.arraycopy(getNTLMv2Response(), 0, truncatedResponse, 0, 16);
/*  471 */         this.ntlmv2UserSessionKey = NTLMEngineImpl.hmacMD5(truncatedResponse, ntlmv2hash);
/*      */       } 
/*  473 */       return this.ntlmv2UserSessionKey;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLM2SessionResponseUserSessionKey() throws NTLMEngineException {
/*  479 */       if (this.ntlm2SessionResponseUserSessionKey == null) {
/*  480 */         byte[] ntlmUserSessionKey = getNTLMUserSessionKey();
/*  481 */         byte[] ntlm2SessionResponseNonce = getLM2SessionResponse();
/*  482 */         byte[] sessionNonce = new byte[this.challenge.length + ntlm2SessionResponseNonce.length];
/*  483 */         System.arraycopy(this.challenge, 0, sessionNonce, 0, this.challenge.length);
/*  484 */         System.arraycopy(ntlm2SessionResponseNonce, 0, sessionNonce, this.challenge.length, ntlm2SessionResponseNonce.length);
/*  485 */         this.ntlm2SessionResponseUserSessionKey = NTLMEngineImpl.hmacMD5(sessionNonce, ntlmUserSessionKey);
/*      */       } 
/*  487 */       return this.ntlm2SessionResponseUserSessionKey;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getLanManagerSessionKey() throws NTLMEngineException {
/*  493 */       if (this.lanManagerSessionKey == null) {
/*  494 */         byte[] lmHash = getLMHash();
/*  495 */         byte[] lmResponse = getLMResponse();
/*      */         try {
/*  497 */           byte[] keyBytes = new byte[14];
/*  498 */           System.arraycopy(lmHash, 0, keyBytes, 0, 8);
/*  499 */           Arrays.fill(keyBytes, 8, keyBytes.length, (byte)-67);
/*  500 */           Key lowKey = NTLMEngineImpl.createDESKey(keyBytes, 0);
/*  501 */           Key highKey = NTLMEngineImpl.createDESKey(keyBytes, 7);
/*  502 */           byte[] truncatedResponse = new byte[8];
/*  503 */           System.arraycopy(lmResponse, 0, truncatedResponse, 0, truncatedResponse.length);
/*  504 */           Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
/*  505 */           des.init(1, lowKey);
/*  506 */           byte[] lowPart = des.doFinal(truncatedResponse);
/*  507 */           des = Cipher.getInstance("DES/ECB/NoPadding");
/*  508 */           des.init(1, highKey);
/*  509 */           byte[] highPart = des.doFinal(truncatedResponse);
/*  510 */           this.lanManagerSessionKey = new byte[16];
/*  511 */           System.arraycopy(lowPart, 0, this.lanManagerSessionKey, 0, lowPart.length);
/*  512 */           System.arraycopy(highPart, 0, this.lanManagerSessionKey, lowPart.length, highPart.length);
/*  513 */         } catch (Exception e) {
/*  514 */           throw new NTLMEngineException(e.getMessage(), e);
/*      */         } 
/*      */       } 
/*  517 */       return this.lanManagerSessionKey;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static byte[] hmacMD5(byte[] value, byte[] key) throws NTLMEngineException {
/*  524 */     HMACMD5 hmacMD5 = new HMACMD5(key);
/*  525 */     hmacMD5.update(value);
/*  526 */     return hmacMD5.getOutput();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static byte[] RC4(byte[] value, byte[] key) throws NTLMEngineException {
/*      */     try {
/*  533 */       Cipher rc4 = Cipher.getInstance("RC4");
/*  534 */       rc4.init(1, new SecretKeySpec(key, "RC4"));
/*  535 */       return rc4.doFinal(value);
/*  536 */     } catch (Exception e) {
/*  537 */       throw new NTLMEngineException(e.getMessage(), e);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static byte[] ntlm2SessionResponse(byte[] ntlmHash, byte[] challenge, byte[] clientChallenge) throws NTLMEngineException {
/*      */     try {
/*  572 */       MessageDigest md5 = MessageDigest.getInstance("MD5");
/*  573 */       md5.update(challenge);
/*  574 */       md5.update(clientChallenge);
/*  575 */       byte[] digest = md5.digest();
/*      */       
/*  577 */       byte[] sessionHash = new byte[8];
/*  578 */       System.arraycopy(digest, 0, sessionHash, 0, 8);
/*  579 */       return lmResponse(ntlmHash, sessionHash);
/*  580 */     } catch (Exception e) {
/*  581 */       if (e instanceof NTLMEngineException)
/*  582 */         throw (NTLMEngineException)e; 
/*  583 */       throw new NTLMEngineException(e.getMessage(), e);
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
/*      */   private static byte[] lmHash(String password) throws NTLMEngineException {
/*      */     try {
/*  598 */       byte[] oemPassword = password.toUpperCase(Locale.US).getBytes("US-ASCII");
/*  599 */       int length = Math.min(oemPassword.length, 14);
/*  600 */       byte[] keyBytes = new byte[14];
/*  601 */       System.arraycopy(oemPassword, 0, keyBytes, 0, length);
/*  602 */       Key lowKey = createDESKey(keyBytes, 0);
/*  603 */       Key highKey = createDESKey(keyBytes, 7);
/*  604 */       byte[] magicConstant = "KGS!@#$%".getBytes("US-ASCII");
/*  605 */       Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
/*  606 */       des.init(1, lowKey);
/*  607 */       byte[] lowHash = des.doFinal(magicConstant);
/*  608 */       des.init(1, highKey);
/*  609 */       byte[] highHash = des.doFinal(magicConstant);
/*  610 */       byte[] lmHash = new byte[16];
/*  611 */       System.arraycopy(lowHash, 0, lmHash, 0, 8);
/*  612 */       System.arraycopy(highHash, 0, lmHash, 8, 8);
/*  613 */       return lmHash;
/*  614 */     } catch (Exception e) {
/*  615 */       throw new NTLMEngineException(e.getMessage(), e);
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
/*      */   private static byte[] ntlmHash(String password) throws NTLMEngineException {
/*      */     try {
/*  630 */       byte[] unicodePassword = password.getBytes("UnicodeLittleUnmarked");
/*  631 */       MD4 md4 = new MD4();
/*  632 */       md4.update(unicodePassword);
/*  633 */       return md4.getOutput();
/*  634 */     } catch (UnsupportedEncodingException e) {
/*  635 */       throw new NTLMEngineException("Unicode not supported: " + e.getMessage(), e);
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
/*      */   private static byte[] lmv2Hash(String domain, String user, byte[] ntlmHash) throws NTLMEngineException {
/*      */     try {
/*  655 */       HMACMD5 hmacMD5 = new HMACMD5(ntlmHash);
/*      */       
/*  657 */       hmacMD5.update(user.toUpperCase(Locale.US).getBytes("UnicodeLittleUnmarked"));
/*  658 */       if (domain != null) {
/*  659 */         hmacMD5.update(domain.toUpperCase(Locale.US).getBytes("UnicodeLittleUnmarked"));
/*      */       }
/*  661 */       return hmacMD5.getOutput();
/*  662 */     } catch (UnsupportedEncodingException e) {
/*  663 */       throw new NTLMEngineException("Unicode not supported! " + e.getMessage(), e);
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
/*      */   private static byte[] ntlmv2Hash(String domain, String user, byte[] ntlmHash) throws NTLMEngineException {
/*      */     try {
/*  683 */       HMACMD5 hmacMD5 = new HMACMD5(ntlmHash);
/*      */       
/*  685 */       hmacMD5.update(user.toUpperCase(Locale.US).getBytes("UnicodeLittleUnmarked"));
/*  686 */       if (domain != null) {
/*  687 */         hmacMD5.update(domain.getBytes("UnicodeLittleUnmarked"));
/*      */       }
/*  689 */       return hmacMD5.getOutput();
/*  690 */     } catch (UnsupportedEncodingException e) {
/*  691 */       throw new NTLMEngineException("Unicode not supported! " + e.getMessage(), e);
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
/*      */   private static byte[] lmResponse(byte[] hash, byte[] challenge) throws NTLMEngineException {
/*      */     try {
/*  707 */       byte[] keyBytes = new byte[21];
/*  708 */       System.arraycopy(hash, 0, keyBytes, 0, 16);
/*  709 */       Key lowKey = createDESKey(keyBytes, 0);
/*  710 */       Key middleKey = createDESKey(keyBytes, 7);
/*  711 */       Key highKey = createDESKey(keyBytes, 14);
/*  712 */       Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
/*  713 */       des.init(1, lowKey);
/*  714 */       byte[] lowResponse = des.doFinal(challenge);
/*  715 */       des.init(1, middleKey);
/*  716 */       byte[] middleResponse = des.doFinal(challenge);
/*  717 */       des.init(1, highKey);
/*  718 */       byte[] highResponse = des.doFinal(challenge);
/*  719 */       byte[] lmResponse = new byte[24];
/*  720 */       System.arraycopy(lowResponse, 0, lmResponse, 0, 8);
/*  721 */       System.arraycopy(middleResponse, 0, lmResponse, 8, 8);
/*  722 */       System.arraycopy(highResponse, 0, lmResponse, 16, 8);
/*  723 */       return lmResponse;
/*  724 */     } catch (Exception e) {
/*  725 */       throw new NTLMEngineException(e.getMessage(), e);
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
/*      */   private static byte[] lmv2Response(byte[] hash, byte[] challenge, byte[] clientData) throws NTLMEngineException {
/*  745 */     HMACMD5 hmacMD5 = new HMACMD5(hash);
/*  746 */     hmacMD5.update(challenge);
/*  747 */     hmacMD5.update(clientData);
/*  748 */     byte[] mac = hmacMD5.getOutput();
/*  749 */     byte[] lmv2Response = new byte[mac.length + clientData.length];
/*  750 */     System.arraycopy(mac, 0, lmv2Response, 0, mac.length);
/*  751 */     System.arraycopy(clientData, 0, lmv2Response, mac.length, clientData.length);
/*  752 */     return lmv2Response;
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
/*      */   private static byte[] createBlob(byte[] clientChallenge, byte[] targetInformation, byte[] timestamp) {
/*  767 */     byte[] blobSignature = { 1, 1, 0, 0 };
/*  768 */     byte[] reserved = { 0, 0, 0, 0 };
/*  769 */     byte[] unknown1 = { 0, 0, 0, 0 };
/*  770 */     byte[] unknown2 = { 0, 0, 0, 0 };
/*  771 */     byte[] blob = new byte[blobSignature.length + reserved.length + timestamp.length + 8 + unknown1.length + targetInformation.length + unknown2.length];
/*      */     
/*  773 */     int offset = 0;
/*  774 */     System.arraycopy(blobSignature, 0, blob, offset, blobSignature.length);
/*  775 */     offset += blobSignature.length;
/*  776 */     System.arraycopy(reserved, 0, blob, offset, reserved.length);
/*  777 */     offset += reserved.length;
/*  778 */     System.arraycopy(timestamp, 0, blob, offset, timestamp.length);
/*  779 */     offset += timestamp.length;
/*  780 */     System.arraycopy(clientChallenge, 0, blob, offset, 8);
/*  781 */     offset += 8;
/*  782 */     System.arraycopy(unknown1, 0, blob, offset, unknown1.length);
/*  783 */     offset += unknown1.length;
/*  784 */     System.arraycopy(targetInformation, 0, blob, offset, targetInformation.length);
/*  785 */     offset += targetInformation.length;
/*  786 */     System.arraycopy(unknown2, 0, blob, offset, unknown2.length);
/*  787 */     offset += unknown2.length;
/*  788 */     return blob;
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
/*      */   private static Key createDESKey(byte[] bytes, int offset) {
/*  804 */     byte[] keyBytes = new byte[7];
/*  805 */     System.arraycopy(bytes, offset, keyBytes, 0, 7);
/*  806 */     byte[] material = new byte[8];
/*  807 */     material[0] = keyBytes[0];
/*  808 */     material[1] = (byte)(keyBytes[0] << 7 | (keyBytes[1] & 0xFF) >>> 1);
/*  809 */     material[2] = (byte)(keyBytes[1] << 6 | (keyBytes[2] & 0xFF) >>> 2);
/*  810 */     material[3] = (byte)(keyBytes[2] << 5 | (keyBytes[3] & 0xFF) >>> 3);
/*  811 */     material[4] = (byte)(keyBytes[3] << 4 | (keyBytes[4] & 0xFF) >>> 4);
/*  812 */     material[5] = (byte)(keyBytes[4] << 3 | (keyBytes[5] & 0xFF) >>> 5);
/*  813 */     material[6] = (byte)(keyBytes[5] << 2 | (keyBytes[6] & 0xFF) >>> 6);
/*  814 */     material[7] = (byte)(keyBytes[6] << 1);
/*  815 */     oddParity(material);
/*  816 */     return new SecretKeySpec(material, "DES");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void oddParity(byte[] bytes) {
/*  826 */     for (int i = 0; i < bytes.length; i++) {
/*  827 */       byte b = bytes[i];
/*  828 */       boolean needsParity = (((b >>> 7 ^ b >>> 6 ^ b >>> 5 ^ b >>> 4 ^ b >>> 3 ^ b >>> 2 ^ b >>> 1) & 0x1) == 0);
/*      */       
/*  830 */       if (needsParity) {
/*  831 */         bytes[i] = (byte)(bytes[i] | 0x1);
/*      */       } else {
/*  833 */         bytes[i] = (byte)(bytes[i] & 0xFFFFFFFE);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static class NTLMMessage
/*      */   {
/*  841 */     private byte[] messageContents = null;
/*      */ 
/*      */     
/*  844 */     private int currentOutputPosition = 0;
/*      */ 
/*      */ 
/*      */     
/*      */     NTLMMessage() {}
/*      */ 
/*      */     
/*      */     NTLMMessage(String messageBody, int expectedType) throws NTLMEngineException {
/*  852 */       this.messageContents = Base64.decodeBase64(EncodingUtils.getBytes(messageBody, "ASCII"));
/*      */ 
/*      */       
/*  855 */       if (this.messageContents.length < NTLMEngineImpl.SIGNATURE.length)
/*  856 */         throw new NTLMEngineException("NTLM message decoding error - packet too short"); 
/*  857 */       int i = 0;
/*  858 */       while (i < NTLMEngineImpl.SIGNATURE.length) {
/*  859 */         if (this.messageContents[i] != NTLMEngineImpl.SIGNATURE[i]) {
/*  860 */           throw new NTLMEngineException("NTLM message expected - instead got unrecognized bytes");
/*      */         }
/*  862 */         i++;
/*      */       } 
/*      */ 
/*      */       
/*  866 */       int type = readULong(NTLMEngineImpl.SIGNATURE.length);
/*  867 */       if (type != expectedType) {
/*  868 */         throw new NTLMEngineException("NTLM type " + Integer.toString(expectedType) + " message expected - instead got type " + Integer.toString(type));
/*      */       }
/*      */       
/*  871 */       this.currentOutputPosition = this.messageContents.length;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected int getPreambleLength() {
/*  879 */       return NTLMEngineImpl.SIGNATURE.length + 4;
/*      */     }
/*      */ 
/*      */     
/*      */     protected int getMessageLength() {
/*  884 */       return this.currentOutputPosition;
/*      */     }
/*      */ 
/*      */     
/*      */     protected byte readByte(int position) throws NTLMEngineException {
/*  889 */       if (this.messageContents.length < position + 1)
/*  890 */         throw new NTLMEngineException("NTLM: Message too short"); 
/*  891 */       return this.messageContents[position];
/*      */     }
/*      */ 
/*      */     
/*      */     protected void readBytes(byte[] buffer, int position) throws NTLMEngineException {
/*  896 */       if (this.messageContents.length < position + buffer.length)
/*  897 */         throw new NTLMEngineException("NTLM: Message too short"); 
/*  898 */       System.arraycopy(this.messageContents, position, buffer, 0, buffer.length);
/*      */     }
/*      */ 
/*      */     
/*      */     protected int readUShort(int position) throws NTLMEngineException {
/*  903 */       return NTLMEngineImpl.readUShort(this.messageContents, position);
/*      */     }
/*      */ 
/*      */     
/*      */     protected int readULong(int position) throws NTLMEngineException {
/*  908 */       return NTLMEngineImpl.readULong(this.messageContents, position);
/*      */     }
/*      */ 
/*      */     
/*      */     protected byte[] readSecurityBuffer(int position) throws NTLMEngineException {
/*  913 */       return NTLMEngineImpl.readSecurityBuffer(this.messageContents, position);
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
/*      */     protected void prepareResponse(int maxlength, int messageType) {
/*  925 */       this.messageContents = new byte[maxlength];
/*  926 */       this.currentOutputPosition = 0;
/*  927 */       addBytes(NTLMEngineImpl.SIGNATURE);
/*  928 */       addULong(messageType);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected void addByte(byte b) {
/*  938 */       this.messageContents[this.currentOutputPosition] = b;
/*  939 */       this.currentOutputPosition++;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected void addBytes(byte[] bytes) {
/*  949 */       if (bytes == null) {
/*      */         return;
/*      */       }
/*  952 */       for (byte b : bytes) {
/*  953 */         this.messageContents[this.currentOutputPosition] = b;
/*  954 */         this.currentOutputPosition++;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addUShort(int value) {
/*  960 */       addByte((byte)(value & 0xFF));
/*  961 */       addByte((byte)(value >> 8 & 0xFF));
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addULong(int value) {
/*  966 */       addByte((byte)(value & 0xFF));
/*  967 */       addByte((byte)(value >> 8 & 0xFF));
/*  968 */       addByte((byte)(value >> 16 & 0xFF));
/*  969 */       addByte((byte)(value >> 24 & 0xFF));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String getResponse() {
/*      */       byte[] resp;
/*  980 */       if (this.messageContents.length > this.currentOutputPosition) {
/*  981 */         byte[] tmp = new byte[this.currentOutputPosition];
/*  982 */         for (int i = 0; i < this.currentOutputPosition; i++) {
/*  983 */           tmp[i] = this.messageContents[i];
/*      */         }
/*  985 */         resp = tmp;
/*      */       } else {
/*  987 */         resp = this.messageContents;
/*      */       } 
/*  989 */       return EncodingUtils.getAsciiString(Base64.encodeBase64(resp));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class Type1Message
/*      */     extends NTLMMessage
/*      */   {
/*      */     protected byte[] hostBytes;
/*      */     
/*      */     protected byte[] domainBytes;
/*      */ 
/*      */     
/*      */     Type1Message(String domain, String host) throws NTLMEngineException {
/*      */       try {
/* 1004 */         String unqualifiedHost = NTLMEngineImpl.convertHost(host);
/*      */         
/* 1006 */         String unqualifiedDomain = NTLMEngineImpl.convertDomain(domain);
/*      */         
/* 1008 */         this.hostBytes = (unqualifiedHost != null) ? unqualifiedHost.getBytes("ASCII") : null;
/* 1009 */         this.domainBytes = (unqualifiedDomain != null) ? unqualifiedDomain.toUpperCase(Locale.US).getBytes("ASCII") : null;
/*      */       }
/* 1011 */       catch (UnsupportedEncodingException e) {
/* 1012 */         throw new NTLMEngineException("Unicode unsupported: " + e.getMessage(), e);
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
/*      */     String getResponse() {
/* 1024 */       int finalLength = 40;
/*      */ 
/*      */ 
/*      */       
/* 1028 */       prepareResponse(finalLength, 1);
/*      */ 
/*      */       
/* 1031 */       addULong(-1576500735);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1056 */       addUShort(0);
/* 1057 */       addUShort(0);
/*      */ 
/*      */       
/* 1060 */       addULong(40);
/*      */ 
/*      */       
/* 1063 */       addUShort(0);
/* 1064 */       addUShort(0);
/*      */ 
/*      */       
/* 1067 */       addULong(40);
/*      */ 
/*      */       
/* 1070 */       addUShort(261);
/*      */       
/* 1072 */       addULong(2600);
/*      */       
/* 1074 */       addUShort(3840);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1084 */       return super.getResponse();
/*      */     }
/*      */   }
/*      */   
/*      */   static class Type2Message
/*      */     extends NTLMMessage
/*      */   {
/*      */     protected byte[] challenge;
/*      */     protected String target;
/*      */     protected byte[] targetInfo;
/*      */     protected int flags;
/*      */     
/*      */     Type2Message(String message) throws NTLMEngineException {
/* 1097 */       super(message, 2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1114 */       this.challenge = new byte[8];
/* 1115 */       readBytes(this.challenge, 24);
/*      */       
/* 1117 */       this.flags = readULong(20);
/*      */       
/* 1119 */       if ((this.flags & 0x1) == 0) {
/* 1120 */         throw new NTLMEngineException("NTLM type 2 message has flags that make no sense: " + Integer.toString(this.flags));
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1125 */       this.target = null;
/*      */ 
/*      */ 
/*      */       
/* 1129 */       if (getMessageLength() >= 20) {
/* 1130 */         byte[] bytes = readSecurityBuffer(12);
/* 1131 */         if (bytes.length != 0) {
/*      */           try {
/* 1133 */             this.target = new String(bytes, "UnicodeLittleUnmarked");
/* 1134 */           } catch (UnsupportedEncodingException e) {
/* 1135 */             throw new NTLMEngineException(e.getMessage(), e);
/*      */           } 
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1141 */       this.targetInfo = null;
/*      */       
/* 1143 */       if (getMessageLength() >= 48) {
/* 1144 */         byte[] bytes = readSecurityBuffer(40);
/* 1145 */         if (bytes.length != 0) {
/* 1146 */           this.targetInfo = bytes;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     byte[] getChallenge() {
/* 1153 */       return this.challenge;
/*      */     }
/*      */ 
/*      */     
/*      */     String getTarget() {
/* 1158 */       return this.target;
/*      */     }
/*      */ 
/*      */     
/*      */     byte[] getTargetInfo() {
/* 1163 */       return this.targetInfo;
/*      */     }
/*      */ 
/*      */     
/*      */     int getFlags() {
/* 1168 */       return this.flags;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class Type3Message
/*      */     extends NTLMMessage
/*      */   {
/*      */     protected int type2Flags;
/*      */     
/*      */     protected byte[] domainBytes;
/*      */     
/*      */     protected byte[] hostBytes;
/*      */     
/*      */     protected byte[] userBytes;
/*      */     
/*      */     protected byte[] lmResp;
/*      */     
/*      */     protected byte[] ntResp;
/*      */     
/*      */     protected byte[] sessionKey;
/*      */     
/*      */     Type3Message(String domain, String host, String user, String password, byte[] nonce, int type2Flags, String target, byte[] targetInformation) throws NTLMEngineException {
/*      */       byte[] userSessionKey;
/* 1192 */       this.type2Flags = type2Flags;
/*      */ 
/*      */       
/* 1195 */       String unqualifiedHost = NTLMEngineImpl.convertHost(host);
/*      */       
/* 1197 */       String unqualifiedDomain = NTLMEngineImpl.convertDomain(domain);
/*      */ 
/*      */       
/* 1200 */       NTLMEngineImpl.CipherGen gen = new NTLMEngineImpl.CipherGen(unqualifiedDomain, user, password, nonce, target, targetInformation);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 1208 */         if ((type2Flags & 0x800000) != 0 && targetInformation != null && target != null)
/*      */         
/*      */         { 
/* 1211 */           this.ntResp = gen.getNTLMv2Response();
/* 1212 */           this.lmResp = gen.getLMv2Response();
/* 1213 */           if ((type2Flags & 0x80) != 0) {
/* 1214 */             userSessionKey = gen.getLanManagerSessionKey();
/*      */           } else {
/* 1216 */             userSessionKey = gen.getNTLMv2UserSessionKey();
/*      */           }
/*      */            }
/* 1219 */         else if ((type2Flags & 0x80000) != 0)
/*      */         
/* 1221 */         { this.ntResp = gen.getNTLM2SessionResponse();
/* 1222 */           this.lmResp = gen.getLM2SessionResponse();
/* 1223 */           if ((type2Flags & 0x80) != 0) {
/* 1224 */             userSessionKey = gen.getLanManagerSessionKey();
/*      */           } else {
/* 1226 */             userSessionKey = gen.getNTLM2SessionResponseUserSessionKey();
/*      */           }  }
/* 1228 */         else { this.ntResp = gen.getNTLMResponse();
/* 1229 */           this.lmResp = gen.getLMResponse();
/* 1230 */           if ((type2Flags & 0x80) != 0) {
/* 1231 */             userSessionKey = gen.getLanManagerSessionKey();
/*      */           } else {
/* 1233 */             userSessionKey = gen.getNTLMUserSessionKey();
/*      */           }  }
/*      */       
/* 1236 */       } catch (NTLMEngineException e) {
/*      */ 
/*      */         
/* 1239 */         this.ntResp = new byte[0];
/* 1240 */         this.lmResp = gen.getLMResponse();
/* 1241 */         if ((type2Flags & 0x80) != 0) {
/* 1242 */           userSessionKey = gen.getLanManagerSessionKey();
/*      */         } else {
/* 1244 */           userSessionKey = gen.getLMUserSessionKey();
/*      */         } 
/*      */       } 
/* 1247 */       if ((type2Flags & 0x10) != 0)
/* 1248 */       { if ((type2Flags & 0x40000000) != 0) {
/* 1249 */           this.sessionKey = NTLMEngineImpl.RC4(gen.getSecondaryKey(), userSessionKey);
/*      */         } else {
/* 1251 */           this.sessionKey = userSessionKey;
/*      */         }  }
/* 1253 */       else { this.sessionKey = null; }
/*      */ 
/*      */       
/*      */       try {
/* 1257 */         this.hostBytes = (unqualifiedHost != null) ? unqualifiedHost.getBytes("UnicodeLittleUnmarked") : null;
/*      */         
/* 1259 */         this.domainBytes = (unqualifiedDomain != null) ? unqualifiedDomain.toUpperCase(Locale.US).getBytes("UnicodeLittleUnmarked") : null;
/*      */         
/* 1261 */         this.userBytes = user.getBytes("UnicodeLittleUnmarked");
/* 1262 */       } catch (UnsupportedEncodingException e) {
/* 1263 */         throw new NTLMEngineException("Unicode not supported: " + e.getMessage(), e);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     String getResponse() {
/* 1270 */       int sessionKeyLen, ntRespLen = this.ntResp.length;
/* 1271 */       int lmRespLen = this.lmResp.length;
/*      */       
/* 1273 */       int domainLen = (this.domainBytes != null) ? this.domainBytes.length : 0;
/* 1274 */       int hostLen = (this.hostBytes != null) ? this.hostBytes.length : 0;
/* 1275 */       int userLen = this.userBytes.length;
/*      */       
/* 1277 */       if (this.sessionKey != null) {
/* 1278 */         sessionKeyLen = this.sessionKey.length;
/*      */       } else {
/* 1280 */         sessionKeyLen = 0;
/*      */       } 
/*      */       
/* 1283 */       int lmRespOffset = 72;
/* 1284 */       int ntRespOffset = lmRespOffset + lmRespLen;
/* 1285 */       int domainOffset = ntRespOffset + ntRespLen;
/* 1286 */       int userOffset = domainOffset + domainLen;
/* 1287 */       int hostOffset = userOffset + userLen;
/* 1288 */       int sessionKeyOffset = hostOffset + hostLen;
/* 1289 */       int finalLength = sessionKeyOffset + sessionKeyLen;
/*      */ 
/*      */       
/* 1292 */       prepareResponse(finalLength, 3);
/*      */ 
/*      */       
/* 1295 */       addUShort(lmRespLen);
/* 1296 */       addUShort(lmRespLen);
/*      */ 
/*      */       
/* 1299 */       addULong(lmRespOffset);
/*      */ 
/*      */       
/* 1302 */       addUShort(ntRespLen);
/* 1303 */       addUShort(ntRespLen);
/*      */ 
/*      */       
/* 1306 */       addULong(ntRespOffset);
/*      */ 
/*      */       
/* 1309 */       addUShort(domainLen);
/* 1310 */       addUShort(domainLen);
/*      */ 
/*      */       
/* 1313 */       addULong(domainOffset);
/*      */ 
/*      */       
/* 1316 */       addUShort(userLen);
/* 1317 */       addUShort(userLen);
/*      */ 
/*      */       
/* 1320 */       addULong(userOffset);
/*      */ 
/*      */       
/* 1323 */       addUShort(hostLen);
/* 1324 */       addUShort(hostLen);
/*      */ 
/*      */       
/* 1327 */       addULong(hostOffset);
/*      */ 
/*      */       
/* 1330 */       addUShort(sessionKeyLen);
/* 1331 */       addUShort(sessionKeyLen);
/*      */ 
/*      */       
/* 1334 */       addULong(sessionKeyOffset);
/*      */ 
/*      */       
/* 1337 */       addULong(this.type2Flags & 0x80 | this.type2Flags & 0x200 | this.type2Flags & 0x80000 | 0x2000000 | this.type2Flags & 0x8000 | this.type2Flags & 0x20 | this.type2Flags & 0x10 | this.type2Flags & 0x20000000 | this.type2Flags & Integer.MIN_VALUE | this.type2Flags & 0x40000000 | this.type2Flags & 0x800000 | this.type2Flags & 0x1 | this.type2Flags & 0x4);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1365 */       addUShort(261);
/*      */       
/* 1367 */       addULong(2600);
/*      */       
/* 1369 */       addUShort(3840);
/*      */ 
/*      */       
/* 1372 */       addBytes(this.lmResp);
/* 1373 */       addBytes(this.ntResp);
/* 1374 */       addBytes(this.domainBytes);
/* 1375 */       addBytes(this.userBytes);
/* 1376 */       addBytes(this.hostBytes);
/* 1377 */       if (this.sessionKey != null) {
/* 1378 */         addBytes(this.sessionKey);
/*      */       }
/* 1380 */       return super.getResponse();
/*      */     }
/*      */   }
/*      */   
/*      */   static void writeULong(byte[] buffer, int value, int offset) {
/* 1385 */     buffer[offset] = (byte)(value & 0xFF);
/* 1386 */     buffer[offset + 1] = (byte)(value >> 8 & 0xFF);
/* 1387 */     buffer[offset + 2] = (byte)(value >> 16 & 0xFF);
/* 1388 */     buffer[offset + 3] = (byte)(value >> 24 & 0xFF);
/*      */   }
/*      */   
/*      */   static int F(int x, int y, int z) {
/* 1392 */     return x & y | (x ^ 0xFFFFFFFF) & z;
/*      */   }
/*      */   
/*      */   static int G(int x, int y, int z) {
/* 1396 */     return x & y | x & z | y & z;
/*      */   }
/*      */   
/*      */   static int H(int x, int y, int z) {
/* 1400 */     return x ^ y ^ z;
/*      */   }
/*      */   
/*      */   static int rotintlft(int val, int numbits) {
/* 1404 */     return val << numbits | val >>> 32 - numbits;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class MD4
/*      */   {
/* 1415 */     protected int A = 1732584193;
/* 1416 */     protected int B = -271733879;
/* 1417 */     protected int C = -1732584194;
/* 1418 */     protected int D = 271733878;
/* 1419 */     protected long count = 0L;
/* 1420 */     protected byte[] dataBuffer = new byte[64];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void update(byte[] input) {
/* 1429 */       int curBufferPos = (int)(this.count & 0x3FL);
/* 1430 */       int inputIndex = 0;
/* 1431 */       while (input.length - inputIndex + curBufferPos >= this.dataBuffer.length) {
/*      */ 
/*      */ 
/*      */         
/* 1435 */         int transferAmt = this.dataBuffer.length - curBufferPos;
/* 1436 */         System.arraycopy(input, inputIndex, this.dataBuffer, curBufferPos, transferAmt);
/* 1437 */         this.count += transferAmt;
/* 1438 */         curBufferPos = 0;
/* 1439 */         inputIndex += transferAmt;
/* 1440 */         processBuffer();
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1445 */       if (inputIndex < input.length) {
/* 1446 */         int transferAmt = input.length - inputIndex;
/* 1447 */         System.arraycopy(input, inputIndex, this.dataBuffer, curBufferPos, transferAmt);
/* 1448 */         this.count += transferAmt;
/* 1449 */         curBufferPos += transferAmt;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     byte[] getOutput() {
/* 1456 */       int bufferIndex = (int)(this.count & 0x3FL);
/* 1457 */       int padLen = (bufferIndex < 56) ? (56 - bufferIndex) : (120 - bufferIndex);
/* 1458 */       byte[] postBytes = new byte[padLen + 8];
/*      */ 
/*      */       
/* 1461 */       postBytes[0] = Byte.MIN_VALUE;
/*      */       
/* 1463 */       for (int i = 0; i < 8; i++) {
/* 1464 */         postBytes[padLen + i] = (byte)(int)(this.count * 8L >>> 8 * i);
/*      */       }
/*      */ 
/*      */       
/* 1468 */       update(postBytes);
/*      */ 
/*      */       
/* 1471 */       byte[] result = new byte[16];
/* 1472 */       NTLMEngineImpl.writeULong(result, this.A, 0);
/* 1473 */       NTLMEngineImpl.writeULong(result, this.B, 4);
/* 1474 */       NTLMEngineImpl.writeULong(result, this.C, 8);
/* 1475 */       NTLMEngineImpl.writeULong(result, this.D, 12);
/* 1476 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void processBuffer() {
/* 1481 */       int[] d = new int[16];
/*      */       
/* 1483 */       for (int i = 0; i < 16; i++) {
/* 1484 */         d[i] = (this.dataBuffer[i * 4] & 0xFF) + ((this.dataBuffer[i * 4 + 1] & 0xFF) << 8) + ((this.dataBuffer[i * 4 + 2] & 0xFF) << 16) + ((this.dataBuffer[i * 4 + 3] & 0xFF) << 24);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1490 */       int AA = this.A;
/* 1491 */       int BB = this.B;
/* 1492 */       int CC = this.C;
/* 1493 */       int DD = this.D;
/* 1494 */       round1(d);
/* 1495 */       round2(d);
/* 1496 */       round3(d);
/* 1497 */       this.A += AA;
/* 1498 */       this.B += BB;
/* 1499 */       this.C += CC;
/* 1500 */       this.D += DD;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void round1(int[] d) {
/* 1505 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + d[0], 3);
/* 1506 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + d[1], 7);
/* 1507 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + d[2], 11);
/* 1508 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + d[3], 19);
/*      */       
/* 1510 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + d[4], 3);
/* 1511 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + d[5], 7);
/* 1512 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + d[6], 11);
/* 1513 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + d[7], 19);
/*      */       
/* 1515 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + d[8], 3);
/* 1516 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + d[9], 7);
/* 1517 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + d[10], 11);
/* 1518 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + d[11], 19);
/*      */       
/* 1520 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + d[12], 3);
/* 1521 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + d[13], 7);
/* 1522 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + d[14], 11);
/* 1523 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + d[15], 19);
/*      */     }
/*      */     
/*      */     protected void round2(int[] d) {
/* 1527 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + d[0] + 1518500249, 3);
/* 1528 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + d[4] + 1518500249, 5);
/* 1529 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + d[8] + 1518500249, 9);
/* 1530 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + d[12] + 1518500249, 13);
/*      */       
/* 1532 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + d[1] + 1518500249, 3);
/* 1533 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + d[5] + 1518500249, 5);
/* 1534 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + d[9] + 1518500249, 9);
/* 1535 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + d[13] + 1518500249, 13);
/*      */       
/* 1537 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + d[2] + 1518500249, 3);
/* 1538 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + d[6] + 1518500249, 5);
/* 1539 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + d[10] + 1518500249, 9);
/* 1540 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + d[14] + 1518500249, 13);
/*      */       
/* 1542 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + d[3] + 1518500249, 3);
/* 1543 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + d[7] + 1518500249, 5);
/* 1544 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + d[11] + 1518500249, 9);
/* 1545 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + d[15] + 1518500249, 13);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void round3(int[] d) {
/* 1550 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + d[0] + 1859775393, 3);
/* 1551 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + d[8] + 1859775393, 9);
/* 1552 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + d[4] + 1859775393, 11);
/* 1553 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + d[12] + 1859775393, 15);
/*      */       
/* 1555 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + d[2] + 1859775393, 3);
/* 1556 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + d[10] + 1859775393, 9);
/* 1557 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + d[6] + 1859775393, 11);
/* 1558 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + d[14] + 1859775393, 15);
/*      */       
/* 1560 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + d[1] + 1859775393, 3);
/* 1561 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + d[9] + 1859775393, 9);
/* 1562 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + d[5] + 1859775393, 11);
/* 1563 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + d[13] + 1859775393, 15);
/*      */       
/* 1565 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + d[3] + 1859775393, 3);
/* 1566 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + d[11] + 1859775393, 9);
/* 1567 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + d[7] + 1859775393, 11);
/* 1568 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + d[15] + 1859775393, 15);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class HMACMD5
/*      */   {
/*      */     protected byte[] ipad;
/*      */     
/*      */     protected byte[] opad;
/*      */     
/*      */     protected MessageDigest md5;
/*      */ 
/*      */     
/*      */     HMACMD5(byte[] key) throws NTLMEngineException {
/*      */       try {
/* 1585 */         this.md5 = MessageDigest.getInstance("MD5");
/* 1586 */       } catch (Exception ex) {
/*      */ 
/*      */         
/* 1589 */         throw new NTLMEngineException("Error getting md5 message digest implementation: " + ex.getMessage(), ex);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1594 */       this.ipad = new byte[64];
/* 1595 */       this.opad = new byte[64];
/*      */       
/* 1597 */       int keyLength = key.length;
/* 1598 */       if (keyLength > 64) {
/*      */         
/* 1600 */         this.md5.update(key);
/* 1601 */         key = this.md5.digest();
/* 1602 */         keyLength = key.length;
/*      */       } 
/* 1604 */       int i = 0;
/* 1605 */       while (i < keyLength) {
/* 1606 */         this.ipad[i] = (byte)(key[i] ^ 0x36);
/* 1607 */         this.opad[i] = (byte)(key[i] ^ 0x5C);
/* 1608 */         i++;
/*      */       } 
/* 1610 */       while (i < 64) {
/* 1611 */         this.ipad[i] = 54;
/* 1612 */         this.opad[i] = 92;
/* 1613 */         i++;
/*      */       } 
/*      */ 
/*      */       
/* 1617 */       this.md5.reset();
/* 1618 */       this.md5.update(this.ipad);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     byte[] getOutput() {
/* 1624 */       byte[] digest = this.md5.digest();
/* 1625 */       this.md5.update(this.opad);
/* 1626 */       return this.md5.digest(digest);
/*      */     }
/*      */ 
/*      */     
/*      */     void update(byte[] input) {
/* 1631 */       this.md5.update(input);
/*      */     }
/*      */ 
/*      */     
/*      */     void update(byte[] input, int offset, int length) {
/* 1636 */       this.md5.update(input, offset, length);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String generateType1Msg(String domain, String workstation) throws NTLMEngineException {
/* 1644 */     return getType1Message(workstation, domain);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String generateType3Msg(String username, String password, String domain, String workstation, String challenge) throws NTLMEngineException {
/* 1653 */     Type2Message t2m = new Type2Message(challenge);
/* 1654 */     return getType3Message(username, password, workstation, domain, t2m.getChallenge(), t2m.getFlags(), t2m.getTarget(), t2m.getTargetInfo());
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\auth\NTLMEngineImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */