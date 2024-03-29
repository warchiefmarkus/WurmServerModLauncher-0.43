/*     */ package org.apache.http.impl.auth;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.http.FormattedHeader;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.auth.AuthenticationException;
/*     */ import org.apache.http.auth.ChallengeState;
/*     */ import org.apache.http.auth.ContextAwareAuthScheme;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.MalformedChallengeException;
/*     */ import org.apache.http.protocol.HTTP;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.CharArrayBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class AuthSchemeBase
/*     */   implements ContextAwareAuthScheme
/*     */ {
/*     */   private ChallengeState challengeState;
/*     */   
/*     */   public AuthSchemeBase(ChallengeState challengeState) {
/*  69 */     this.challengeState = challengeState;
/*     */   }
/*     */   
/*     */   public AuthSchemeBase() {
/*  73 */     this((ChallengeState)null);
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
/*     */   public void processChallenge(Header header) throws MalformedChallengeException {
/*     */     CharArrayBuffer buffer;
/*     */     int pos;
/*  87 */     if (header == null) {
/*  88 */       throw new IllegalArgumentException("Header may not be null");
/*     */     }
/*  90 */     String authheader = header.getName();
/*  91 */     if (authheader.equalsIgnoreCase("WWW-Authenticate")) {
/*  92 */       this.challengeState = ChallengeState.TARGET;
/*  93 */     } else if (authheader.equalsIgnoreCase("Proxy-Authenticate")) {
/*  94 */       this.challengeState = ChallengeState.PROXY;
/*     */     } else {
/*  96 */       throw new MalformedChallengeException("Unexpected header name: " + authheader);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 101 */     if (header instanceof FormattedHeader) {
/* 102 */       buffer = ((FormattedHeader)header).getBuffer();
/* 103 */       pos = ((FormattedHeader)header).getValuePos();
/*     */     } else {
/* 105 */       String str = header.getValue();
/* 106 */       if (str == null) {
/* 107 */         throw new MalformedChallengeException("Header value is null");
/*     */       }
/* 109 */       buffer = new CharArrayBuffer(str.length());
/* 110 */       buffer.append(str);
/* 111 */       pos = 0;
/*     */     } 
/* 113 */     while (pos < buffer.length() && HTTP.isWhitespace(buffer.charAt(pos))) {
/* 114 */       pos++;
/*     */     }
/* 116 */     int beginIndex = pos;
/* 117 */     while (pos < buffer.length() && !HTTP.isWhitespace(buffer.charAt(pos))) {
/* 118 */       pos++;
/*     */     }
/* 120 */     int endIndex = pos;
/* 121 */     String s = buffer.substring(beginIndex, endIndex);
/* 122 */     if (!s.equalsIgnoreCase(getSchemeName())) {
/* 123 */       throw new MalformedChallengeException("Invalid scheme identifier: " + s);
/*     */     }
/*     */     
/* 126 */     parseChallenge(buffer, pos, buffer.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Header authenticate(Credentials credentials, HttpRequest request, HttpContext context) throws AuthenticationException {
/* 135 */     return authenticate(credentials, request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void parseChallenge(CharArrayBuffer paramCharArrayBuffer, int paramInt1, int paramInt2) throws MalformedChallengeException;
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isProxy() {
/* 146 */     return (this.challengeState != null && this.challengeState == ChallengeState.PROXY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChallengeState getChallengeState() {
/* 155 */     return this.challengeState;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 160 */     String name = getSchemeName();
/* 161 */     if (name != null) {
/* 162 */       return name.toUpperCase(Locale.US);
/*     */     }
/* 164 */     return super.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\auth\AuthSchemeBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */