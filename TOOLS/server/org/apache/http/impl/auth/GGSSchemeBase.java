/*     */ package org.apache.http.impl.auth;
/*     */ 
/*     */ import org.apache.commons.codec.binary.Base64;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.auth.AuthenticationException;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.InvalidCredentialsException;
/*     */ import org.apache.http.auth.MalformedChallengeException;
/*     */ import org.apache.http.message.BufferedHeader;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.CharArrayBuffer;
/*     */ import org.ietf.jgss.GSSContext;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.GSSManager;
/*     */ import org.ietf.jgss.GSSName;
/*     */ import org.ietf.jgss.Oid;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class GGSSchemeBase
/*     */   extends AuthSchemeBase
/*     */ {
/*     */   enum State
/*     */   {
/*  56 */     UNINITIATED,
/*  57 */     CHALLENGE_RECEIVED,
/*  58 */     TOKEN_GENERATED,
/*  59 */     FAILED;
/*     */   }
/*     */   
/*  62 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private final Base64 base64codec;
/*     */   
/*     */   private final boolean stripPort;
/*     */   
/*     */   private State state;
/*     */   
/*     */   private byte[] token;
/*     */ 
/*     */   
/*     */   GGSSchemeBase(boolean stripPort) {
/*  75 */     this.base64codec = new Base64(0);
/*  76 */     this.stripPort = stripPort;
/*  77 */     this.state = State.UNINITIATED;
/*     */   }
/*     */   
/*     */   GGSSchemeBase() {
/*  81 */     this(false);
/*     */   }
/*     */   
/*     */   protected GSSManager getManager() {
/*  85 */     return GSSManager.getInstance();
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] generateGSSToken(byte[] input, Oid oid, String authServer) throws GSSException {
/*  90 */     byte[] token = input;
/*  91 */     if (token == null) {
/*  92 */       token = new byte[0];
/*     */     }
/*  94 */     GSSManager manager = getManager();
/*  95 */     GSSName serverName = manager.createName("HTTP@" + authServer, GSSName.NT_HOSTBASED_SERVICE);
/*  96 */     GSSContext gssContext = manager.createContext(serverName.canonicalize(oid), oid, null, 0);
/*     */     
/*  98 */     gssContext.requestMutualAuth(true);
/*  99 */     gssContext.requestCredDeleg(true);
/* 100 */     return gssContext.initSecContext(token, 0, token.length);
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract byte[] generateToken(byte[] paramArrayOfbyte, String paramString) throws GSSException;
/*     */   
/*     */   public boolean isComplete() {
/* 107 */     return (this.state == State.TOKEN_GENERATED || this.state == State.FAILED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Header authenticate(Credentials credentials, HttpRequest request) throws AuthenticationException {
/* 117 */     return authenticate(credentials, request, (HttpContext)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Header authenticate(Credentials credentials, HttpRequest request, HttpContext context) throws AuthenticationException {
/*     */     String tokenstr;
/*     */     CharArrayBuffer buffer;
/* 125 */     if (request == null) {
/* 126 */       throw new IllegalArgumentException("HTTP request may not be null");
/*     */     }
/* 128 */     switch (this.state) {
/*     */       case UNINITIATED:
/* 130 */         throw new AuthenticationException(getSchemeName() + " authentication has not been initiated");
/*     */       case FAILED:
/* 132 */         throw new AuthenticationException(getSchemeName() + " authentication has failed");
/*     */       case CHALLENGE_RECEIVED:
/*     */         try {
/* 135 */           String authServer, key = null;
/* 136 */           if (isProxy()) {
/* 137 */             key = "http.proxy_host";
/*     */           } else {
/* 139 */             key = "http.target_host";
/*     */           } 
/* 141 */           HttpHost host = (HttpHost)context.getAttribute(key);
/* 142 */           if (host == null) {
/* 143 */             throw new AuthenticationException("Authentication host is not set in the execution context");
/*     */           }
/*     */ 
/*     */           
/* 147 */           if (!this.stripPort && host.getPort() > 0) {
/* 148 */             authServer = host.toHostString();
/*     */           } else {
/* 150 */             authServer = host.getHostName();
/*     */           } 
/*     */           
/* 153 */           if (this.log.isDebugEnabled()) {
/* 154 */             this.log.debug("init " + authServer);
/*     */           }
/* 156 */           this.token = generateToken(this.token, authServer);
/* 157 */           this.state = State.TOKEN_GENERATED;
/* 158 */         } catch (GSSException gsse) {
/* 159 */           this.state = State.FAILED;
/* 160 */           if (gsse.getMajor() == 9 || gsse.getMajor() == 8)
/*     */           {
/* 162 */             throw new InvalidCredentialsException(gsse.getMessage(), gsse); } 
/* 163 */           if (gsse.getMajor() == 13)
/* 164 */             throw new InvalidCredentialsException(gsse.getMessage(), gsse); 
/* 165 */           if (gsse.getMajor() == 10 || gsse.getMajor() == 19 || gsse.getMajor() == 20)
/*     */           {
/*     */             
/* 168 */             throw new AuthenticationException(gsse.getMessage(), gsse);
/*     */           }
/* 170 */           throw new AuthenticationException(gsse.getMessage());
/*     */         } 
/*     */       case TOKEN_GENERATED:
/* 173 */         tokenstr = new String(this.base64codec.encode(this.token));
/* 174 */         if (this.log.isDebugEnabled()) {
/* 175 */           this.log.debug("Sending response '" + tokenstr + "' back to the auth server");
/*     */         }
/* 177 */         buffer = new CharArrayBuffer(32);
/* 178 */         if (isProxy()) {
/* 179 */           buffer.append("Proxy-Authorization");
/*     */         } else {
/* 181 */           buffer.append("Authorization");
/*     */         } 
/* 183 */         buffer.append(": Negotiate ");
/* 184 */         buffer.append(tokenstr);
/* 185 */         return (Header)new BufferedHeader(buffer);
/*     */     } 
/* 187 */     throw new IllegalStateException("Illegal state: " + this.state);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parseChallenge(CharArrayBuffer buffer, int beginIndex, int endIndex) throws MalformedChallengeException {
/* 195 */     String challenge = buffer.substringTrimmed(beginIndex, endIndex);
/* 196 */     if (this.log.isDebugEnabled()) {
/* 197 */       this.log.debug("Received challenge '" + challenge + "' from the auth server");
/*     */     }
/* 199 */     if (this.state == State.UNINITIATED) {
/* 200 */       this.token = Base64.decodeBase64(challenge.getBytes());
/* 201 */       this.state = State.CHALLENGE_RECEIVED;
/*     */     } else {
/* 203 */       this.log.debug("Authentication already attempted");
/* 204 */       this.state = State.FAILED;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\auth\GGSSchemeBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */