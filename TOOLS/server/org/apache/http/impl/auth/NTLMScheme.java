/*     */ package org.apache.http.impl.auth;
/*     */ 
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.auth.AuthenticationException;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.InvalidCredentialsException;
/*     */ import org.apache.http.auth.MalformedChallengeException;
/*     */ import org.apache.http.auth.NTCredentials;
/*     */ import org.apache.http.message.BufferedHeader;
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
/*     */ @NotThreadSafe
/*     */ public class NTLMScheme
/*     */   extends AuthSchemeBase
/*     */ {
/*     */   private final NTLMEngine engine;
/*     */   private State state;
/*     */   private String challenge;
/*     */   
/*     */   enum State
/*     */   {
/*  53 */     UNINITIATED,
/*  54 */     CHALLENGE_RECEIVED,
/*  55 */     MSG_TYPE1_GENERATED,
/*  56 */     MSG_TYPE2_RECEVIED,
/*  57 */     MSG_TYPE3_GENERATED,
/*  58 */     FAILED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NTLMScheme(NTLMEngine engine) {
/*  68 */     if (engine == null) {
/*  69 */       throw new IllegalArgumentException("NTLM engine may not be null");
/*     */     }
/*  71 */     this.engine = engine;
/*  72 */     this.state = State.UNINITIATED;
/*  73 */     this.challenge = null;
/*     */   }
/*     */   
/*     */   public String getSchemeName() {
/*  77 */     return "ntlm";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getParameter(String name) {
/*  82 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRealm() {
/*  87 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isConnectionBased() {
/*  91 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parseChallenge(CharArrayBuffer buffer, int beginIndex, int endIndex) throws MalformedChallengeException {
/*  98 */     this.challenge = buffer.substringTrimmed(beginIndex, endIndex);
/*  99 */     if (this.challenge.length() == 0) {
/* 100 */       if (this.state == State.UNINITIATED) {
/* 101 */         this.state = State.CHALLENGE_RECEIVED;
/*     */       } else {
/* 103 */         this.state = State.FAILED;
/*     */       } 
/*     */     } else {
/* 106 */       if (this.state.compareTo(State.MSG_TYPE1_GENERATED) < 0) {
/* 107 */         this.state = State.FAILED;
/* 108 */         throw new MalformedChallengeException("Out of sequence NTLM response message");
/* 109 */       }  if (this.state == State.MSG_TYPE1_GENERATED) {
/* 110 */         this.state = State.MSG_TYPE2_RECEVIED;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Header authenticate(Credentials credentials, HttpRequest request) throws AuthenticationException {
/* 118 */     NTCredentials ntcredentials = null;
/*     */     try {
/* 120 */       ntcredentials = (NTCredentials)credentials;
/* 121 */     } catch (ClassCastException e) {
/* 122 */       throw new InvalidCredentialsException("Credentials cannot be used for NTLM authentication: " + credentials.getClass().getName());
/*     */     } 
/*     */ 
/*     */     
/* 126 */     String response = null;
/* 127 */     if (this.state == State.FAILED)
/* 128 */       throw new AuthenticationException("NTLM authentication failed"); 
/* 129 */     if (this.state == State.CHALLENGE_RECEIVED) {
/* 130 */       response = this.engine.generateType1Msg(ntcredentials.getDomain(), ntcredentials.getWorkstation());
/*     */ 
/*     */       
/* 133 */       this.state = State.MSG_TYPE1_GENERATED;
/* 134 */     } else if (this.state == State.MSG_TYPE2_RECEVIED) {
/* 135 */       response = this.engine.generateType3Msg(ntcredentials.getUserName(), ntcredentials.getPassword(), ntcredentials.getDomain(), ntcredentials.getWorkstation(), this.challenge);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 141 */       this.state = State.MSG_TYPE3_GENERATED;
/*     */     } else {
/* 143 */       throw new AuthenticationException("Unexpected state: " + this.state);
/*     */     } 
/* 145 */     CharArrayBuffer buffer = new CharArrayBuffer(32);
/* 146 */     if (isProxy()) {
/* 147 */       buffer.append("Proxy-Authorization");
/*     */     } else {
/* 149 */       buffer.append("Authorization");
/*     */     } 
/* 151 */     buffer.append(": NTLM ");
/* 152 */     buffer.append(response);
/* 153 */     return (Header)new BufferedHeader(buffer);
/*     */   }
/*     */   
/*     */   public boolean isComplete() {
/* 157 */     return (this.state == State.MSG_TYPE3_GENERATED || this.state == State.FAILED);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\auth\NTLMScheme.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */