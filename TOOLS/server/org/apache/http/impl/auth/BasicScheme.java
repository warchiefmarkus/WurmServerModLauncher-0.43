/*     */ package org.apache.http.impl.auth;
/*     */ 
/*     */ import org.apache.commons.codec.binary.Base64;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.auth.AuthenticationException;
/*     */ import org.apache.http.auth.ChallengeState;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.MalformedChallengeException;
/*     */ import org.apache.http.auth.params.AuthParams;
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
/*     */ @NotThreadSafe
/*     */ public class BasicScheme
/*     */   extends RFC2617Scheme
/*     */ {
/*     */   private boolean complete;
/*     */   
/*     */   public BasicScheme(ChallengeState challengeState) {
/*  72 */     super(challengeState);
/*  73 */     this.complete = false;
/*     */   }
/*     */   
/*     */   public BasicScheme() {
/*  77 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSchemeName() {
/*  86 */     return "basic";
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
/* 100 */     super.processChallenge(header);
/* 101 */     this.complete = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isComplete() {
/* 111 */     return this.complete;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConnectionBased() {
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Header authenticate(Credentials credentials, HttpRequest request) throws AuthenticationException {
/* 129 */     return authenticate(credentials, request, (HttpContext)new BasicHttpContext());
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
/*     */   public Header authenticate(Credentials credentials, HttpRequest request, HttpContext context) throws AuthenticationException {
/* 150 */     if (credentials == null) {
/* 151 */       throw new IllegalArgumentException("Credentials may not be null");
/*     */     }
/* 153 */     if (request == null) {
/* 154 */       throw new IllegalArgumentException("HTTP request may not be null");
/*     */     }
/*     */     
/* 157 */     String charset = AuthParams.getCredentialCharset(request.getParams());
/* 158 */     return authenticate(credentials, charset, isProxy());
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
/*     */   public static Header authenticate(Credentials credentials, String charset, boolean proxy) {
/* 174 */     if (credentials == null) {
/* 175 */       throw new IllegalArgumentException("Credentials may not be null");
/*     */     }
/* 177 */     if (charset == null) {
/* 178 */       throw new IllegalArgumentException("charset may not be null");
/*     */     }
/*     */     
/* 181 */     StringBuilder tmp = new StringBuilder();
/* 182 */     tmp.append(credentials.getUserPrincipal().getName());
/* 183 */     tmp.append(":");
/* 184 */     tmp.append((credentials.getPassword() == null) ? "null" : credentials.getPassword());
/*     */     
/* 186 */     byte[] base64password = Base64.encodeBase64(EncodingUtils.getBytes(tmp.toString(), charset), false);
/*     */ 
/*     */     
/* 189 */     CharArrayBuffer buffer = new CharArrayBuffer(32);
/* 190 */     if (proxy) {
/* 191 */       buffer.append("Proxy-Authorization");
/*     */     } else {
/* 193 */       buffer.append("Authorization");
/*     */     } 
/* 195 */     buffer.append(": Basic ");
/* 196 */     buffer.append(base64password, 0, base64password.length);
/*     */     
/* 198 */     return (Header)new BufferedHeader(buffer);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\auth\BasicScheme.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */