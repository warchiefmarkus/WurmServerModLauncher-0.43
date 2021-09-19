/*     */ package org.apache.http.impl.auth;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Formatter;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.auth.AuthenticationException;
/*     */ import org.apache.http.auth.ChallengeState;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.MalformedChallengeException;
/*     */ import org.apache.http.auth.params.AuthParams;
/*     */ import org.apache.http.message.BasicHeaderValueFormatter;
/*     */ import org.apache.http.message.BasicNameValuePair;
/*     */ import org.apache.http.message.BufferedHeader;
/*     */ import org.apache.http.protocol.BasicHttpContext;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.CharArrayBuffer;
/*     */ import org.apache.http.util.EncodingUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class DigestScheme
/*     */   extends RFC2617Scheme
/*     */ {
/*  95 */   private static final char[] HEXADECIMAL = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*     */ 
/*     */   
/*     */   private boolean complete;
/*     */ 
/*     */   
/*     */   private static final int QOP_UNKNOWN = -1;
/*     */   
/*     */   private static final int QOP_MISSING = 0;
/*     */   
/*     */   private static final int QOP_AUTH_INT = 1;
/*     */   
/*     */   private static final int QOP_AUTH = 2;
/*     */   
/*     */   private String lastNonce;
/*     */   
/*     */   private long nounceCount;
/*     */   
/*     */   private String cnonce;
/*     */   
/*     */   private String a1;
/*     */   
/*     */   private String a2;
/*     */ 
/*     */   
/*     */   public DigestScheme(ChallengeState challengeState) {
/* 121 */     super(challengeState);
/* 122 */     this.complete = false;
/*     */   }
/*     */   
/*     */   public DigestScheme() {
/* 126 */     this((ChallengeState)null);
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
/*     */   public void processChallenge(Header header) throws MalformedChallengeException {
/* 140 */     super.processChallenge(header);
/* 141 */     this.complete = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isComplete() {
/* 151 */     String s = getParameter("stale");
/* 152 */     if ("true".equalsIgnoreCase(s)) {
/* 153 */       return false;
/*     */     }
/* 155 */     return this.complete;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSchemeName() {
/* 165 */     return "digest";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConnectionBased() {
/* 174 */     return false;
/*     */   }
/*     */   
/*     */   public void overrideParamter(String name, String value) {
/* 178 */     getParameters().put(name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Header authenticate(Credentials credentials, HttpRequest request) throws AuthenticationException {
/* 187 */     return authenticate(credentials, request, (HttpContext)new BasicHttpContext());
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
/*     */   public Header authenticate(Credentials credentials, HttpRequest request, HttpContext context) throws AuthenticationException {
/* 210 */     if (credentials == null) {
/* 211 */       throw new IllegalArgumentException("Credentials may not be null");
/*     */     }
/* 213 */     if (request == null) {
/* 214 */       throw new IllegalArgumentException("HTTP request may not be null");
/*     */     }
/* 216 */     if (getParameter("realm") == null) {
/* 217 */       throw new AuthenticationException("missing realm in challenge");
/*     */     }
/* 219 */     if (getParameter("nonce") == null) {
/* 220 */       throw new AuthenticationException("missing nonce in challenge");
/*     */     }
/*     */     
/* 223 */     getParameters().put("methodname", request.getRequestLine().getMethod());
/* 224 */     getParameters().put("uri", request.getRequestLine().getUri());
/* 225 */     String charset = getParameter("charset");
/* 226 */     if (charset == null) {
/* 227 */       charset = AuthParams.getCredentialCharset(request.getParams());
/* 228 */       getParameters().put("charset", charset);
/*     */     } 
/* 230 */     return createDigestHeader(credentials, request);
/*     */   }
/*     */ 
/*     */   
/*     */   private static MessageDigest createMessageDigest(String digAlg) throws UnsupportedDigestAlgorithmException {
/*     */     try {
/* 236 */       return MessageDigest.getInstance(digAlg);
/* 237 */     } catch (Exception e) {
/* 238 */       throw new UnsupportedDigestAlgorithmException("Unsupported algorithm in HTTP Digest authentication: " + digAlg);
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
/*     */   private Header createDigestHeader(Credentials credentials, HttpRequest request) throws AuthenticationException {
/*     */     MessageDigest digester;
/* 254 */     String digestValue, uri = getParameter("uri");
/* 255 */     String realm = getParameter("realm");
/* 256 */     String nonce = getParameter("nonce");
/* 257 */     String opaque = getParameter("opaque");
/* 258 */     String method = getParameter("methodname");
/* 259 */     String algorithm = getParameter("algorithm");
/*     */     
/* 261 */     Set<String> qopset = new HashSet<String>(8);
/* 262 */     int qop = -1;
/* 263 */     String qoplist = getParameter("qop");
/* 264 */     if (qoplist != null) {
/* 265 */       StringTokenizer tok = new StringTokenizer(qoplist, ",");
/* 266 */       while (tok.hasMoreTokens()) {
/* 267 */         String variant = tok.nextToken().trim();
/* 268 */         qopset.add(variant.toLowerCase(Locale.US));
/*     */       } 
/* 270 */       if (request instanceof HttpEntityEnclosingRequest && qopset.contains("auth-int")) {
/* 271 */         qop = 1;
/* 272 */       } else if (qopset.contains("auth")) {
/* 273 */         qop = 2;
/*     */       } 
/*     */     } else {
/* 276 */       qop = 0;
/*     */     } 
/*     */     
/* 279 */     if (qop == -1) {
/* 280 */       throw new AuthenticationException("None of the qop methods is supported: " + qoplist);
/*     */     }
/*     */ 
/*     */     
/* 284 */     if (algorithm == null) {
/* 285 */       algorithm = "MD5";
/*     */     }
/* 287 */     String charset = getParameter("charset");
/* 288 */     if (charset == null) {
/* 289 */       charset = "ISO-8859-1";
/*     */     }
/*     */     
/* 292 */     String digAlg = algorithm;
/* 293 */     if (digAlg.equalsIgnoreCase("MD5-sess")) {
/* 294 */       digAlg = "MD5";
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 299 */       digester = createMessageDigest(digAlg);
/* 300 */     } catch (UnsupportedDigestAlgorithmException ex) {
/* 301 */       throw new AuthenticationException("Unsuppported digest algorithm: " + digAlg);
/*     */     } 
/*     */     
/* 304 */     String uname = credentials.getUserPrincipal().getName();
/* 305 */     String pwd = credentials.getPassword();
/*     */     
/* 307 */     if (nonce.equals(this.lastNonce)) {
/* 308 */       this.nounceCount++;
/*     */     } else {
/* 310 */       this.nounceCount = 1L;
/* 311 */       this.cnonce = null;
/* 312 */       this.lastNonce = nonce;
/*     */     } 
/* 314 */     StringBuilder sb = new StringBuilder(256);
/* 315 */     Formatter formatter = new Formatter(sb, Locale.US);
/* 316 */     formatter.format("%08x", new Object[] { Long.valueOf(this.nounceCount) });
/* 317 */     String nc = sb.toString();
/*     */     
/* 319 */     if (this.cnonce == null) {
/* 320 */       this.cnonce = createCnonce();
/*     */     }
/*     */     
/* 323 */     this.a1 = null;
/* 324 */     this.a2 = null;
/*     */     
/* 326 */     if (algorithm.equalsIgnoreCase("MD5-sess")) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 332 */       sb.setLength(0);
/* 333 */       sb.append(uname).append(':').append(realm).append(':').append(pwd);
/* 334 */       String checksum = encode(digester.digest(EncodingUtils.getBytes(sb.toString(), charset)));
/* 335 */       sb.setLength(0);
/* 336 */       sb.append(checksum).append(':').append(nonce).append(':').append(this.cnonce);
/* 337 */       this.a1 = sb.toString();
/*     */     } else {
/*     */       
/* 340 */       sb.setLength(0);
/* 341 */       sb.append(uname).append(':').append(realm).append(':').append(pwd);
/* 342 */       this.a1 = sb.toString();
/*     */     } 
/*     */     
/* 345 */     String hasha1 = encode(digester.digest(EncodingUtils.getBytes(this.a1, charset)));
/*     */     
/* 347 */     if (qop == 2) {
/*     */       
/* 349 */       this.a2 = method + ':' + uri;
/* 350 */     } else if (qop == 1) {
/*     */       
/* 352 */       HttpEntity entity = null;
/* 353 */       if (request instanceof HttpEntityEnclosingRequest) {
/* 354 */         entity = ((HttpEntityEnclosingRequest)request).getEntity();
/*     */       }
/* 356 */       if (entity != null && !entity.isRepeatable()) {
/*     */         
/* 358 */         if (qopset.contains("auth")) {
/* 359 */           qop = 2;
/* 360 */           this.a2 = method + ':' + uri;
/*     */         } else {
/* 362 */           throw new AuthenticationException("Qop auth-int cannot be used with a non-repeatable entity");
/*     */         } 
/*     */       } else {
/*     */         
/* 366 */         HttpEntityDigester entityDigester = new HttpEntityDigester(digester);
/*     */         try {
/* 368 */           if (entity != null) {
/* 369 */             entity.writeTo(entityDigester);
/*     */           }
/* 371 */           entityDigester.close();
/* 372 */         } catch (IOException ex) {
/* 373 */           throw new AuthenticationException("I/O error reading entity content", ex);
/*     */         } 
/* 375 */         this.a2 = method + ':' + uri + ':' + encode(entityDigester.getDigest());
/*     */       } 
/*     */     } else {
/* 378 */       this.a2 = method + ':' + uri;
/*     */     } 
/*     */     
/* 381 */     String hasha2 = encode(digester.digest(EncodingUtils.getBytes(this.a2, charset)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 386 */     if (qop == 0) {
/* 387 */       sb.setLength(0);
/* 388 */       sb.append(hasha1).append(':').append(nonce).append(':').append(hasha2);
/* 389 */       digestValue = sb.toString();
/*     */     } else {
/* 391 */       sb.setLength(0);
/* 392 */       sb.append(hasha1).append(':').append(nonce).append(':').append(nc).append(':').append(this.cnonce).append(':').append((qop == 1) ? "auth-int" : "auth").append(':').append(hasha2);
/*     */ 
/*     */       
/* 395 */       digestValue = sb.toString();
/*     */     } 
/*     */     
/* 398 */     String digest = encode(digester.digest(EncodingUtils.getAsciiBytes(digestValue)));
/*     */     
/* 400 */     CharArrayBuffer buffer = new CharArrayBuffer(128);
/* 401 */     if (isProxy()) {
/* 402 */       buffer.append("Proxy-Authorization");
/*     */     } else {
/* 404 */       buffer.append("Authorization");
/*     */     } 
/* 406 */     buffer.append(": Digest ");
/*     */     
/* 408 */     List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(20);
/* 409 */     params.add(new BasicNameValuePair("username", uname));
/* 410 */     params.add(new BasicNameValuePair("realm", realm));
/* 411 */     params.add(new BasicNameValuePair("nonce", nonce));
/* 412 */     params.add(new BasicNameValuePair("uri", uri));
/* 413 */     params.add(new BasicNameValuePair("response", digest));
/*     */     
/* 415 */     if (qop != 0) {
/* 416 */       params.add(new BasicNameValuePair("qop", (qop == 1) ? "auth-int" : "auth"));
/* 417 */       params.add(new BasicNameValuePair("nc", nc));
/* 418 */       params.add(new BasicNameValuePair("cnonce", this.cnonce));
/*     */     } 
/* 420 */     if (algorithm != null) {
/* 421 */       params.add(new BasicNameValuePair("algorithm", algorithm));
/*     */     }
/* 423 */     if (opaque != null) {
/* 424 */       params.add(new BasicNameValuePair("opaque", opaque));
/*     */     }
/*     */     
/* 427 */     for (int i = 0; i < params.size(); i++) {
/* 428 */       BasicNameValuePair param = params.get(i);
/* 429 */       if (i > 0) {
/* 430 */         buffer.append(", ");
/*     */       }
/* 432 */       boolean noQuotes = ("nc".equals(param.getName()) || "qop".equals(param.getName()));
/* 433 */       BasicHeaderValueFormatter.DEFAULT.formatNameValuePair(buffer, (NameValuePair)param, !noQuotes);
/*     */     } 
/* 435 */     return (Header)new BufferedHeader(buffer);
/*     */   }
/*     */   
/*     */   String getCnonce() {
/* 439 */     return this.cnonce;
/*     */   }
/*     */   
/*     */   String getA1() {
/* 443 */     return this.a1;
/*     */   }
/*     */   
/*     */   String getA2() {
/* 447 */     return this.a2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String encode(byte[] binaryData) {
/* 458 */     int n = binaryData.length;
/* 459 */     char[] buffer = new char[n * 2];
/* 460 */     for (int i = 0; i < n; i++) {
/* 461 */       int low = binaryData[i] & 0xF;
/* 462 */       int high = (binaryData[i] & 0xF0) >> 4;
/* 463 */       buffer[i * 2] = HEXADECIMAL[high];
/* 464 */       buffer[i * 2 + 1] = HEXADECIMAL[low];
/*     */     } 
/*     */     
/* 467 */     return new String(buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String createCnonce() {
/* 477 */     SecureRandom rnd = new SecureRandom();
/* 478 */     byte[] tmp = new byte[8];
/* 479 */     rnd.nextBytes(tmp);
/* 480 */     return encode(tmp);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 485 */     StringBuilder builder = new StringBuilder();
/* 486 */     builder.append("DIGEST [complete=").append(this.complete).append(", nonce=").append(this.lastNonce).append(", nc=").append(this.nounceCount).append("]");
/*     */ 
/*     */ 
/*     */     
/* 490 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\auth\DigestScheme.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */