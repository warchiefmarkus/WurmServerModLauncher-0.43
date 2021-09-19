/*     */ package oauth.signpost;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import oauth.signpost.basic.UrlStringRequestAdapter;
/*     */ import oauth.signpost.exception.OAuthCommunicationException;
/*     */ import oauth.signpost.exception.OAuthExpectationFailedException;
/*     */ import oauth.signpost.exception.OAuthMessageSignerException;
/*     */ import oauth.signpost.http.HttpParameters;
/*     */ import oauth.signpost.http.HttpRequest;
/*     */ import oauth.signpost.signature.AuthorizationHeaderSigningStrategy;
/*     */ import oauth.signpost.signature.HmacSha1MessageSigner;
/*     */ import oauth.signpost.signature.OAuthMessageSigner;
/*     */ import oauth.signpost.signature.QueryStringSigningStrategy;
/*     */ import oauth.signpost.signature.SigningStrategy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractOAuthConsumer
/*     */   implements OAuthConsumer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private String consumerKey;
/*     */   private String consumerSecret;
/*     */   private String token;
/*     */   private OAuthMessageSigner messageSigner;
/*     */   private SigningStrategy signingStrategy;
/*     */   private HttpParameters additionalParameters;
/*     */   private HttpParameters requestParameters;
/*     */   private boolean sendEmptyTokens;
/*  60 */   private final Random random = new Random(System.nanoTime());
/*     */   
/*     */   public AbstractOAuthConsumer(String consumerKey, String consumerSecret) {
/*  63 */     this.consumerKey = consumerKey;
/*  64 */     this.consumerSecret = consumerSecret;
/*  65 */     setMessageSigner((OAuthMessageSigner)new HmacSha1MessageSigner());
/*  66 */     setSigningStrategy((SigningStrategy)new AuthorizationHeaderSigningStrategy());
/*     */   }
/*     */   
/*     */   public void setMessageSigner(OAuthMessageSigner messageSigner) {
/*  70 */     this.messageSigner = messageSigner;
/*  71 */     messageSigner.setConsumerSecret(this.consumerSecret);
/*     */   }
/*     */   
/*     */   public void setSigningStrategy(SigningStrategy signingStrategy) {
/*  75 */     this.signingStrategy = signingStrategy;
/*     */   }
/*     */   
/*     */   public void setAdditionalParameters(HttpParameters additionalParameters) {
/*  79 */     this.additionalParameters = additionalParameters;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized HttpRequest sign(HttpRequest request) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
/*  84 */     if (this.consumerKey == null) {
/*  85 */       throw new OAuthExpectationFailedException("consumer key not set");
/*     */     }
/*  87 */     if (this.consumerSecret == null) {
/*  88 */       throw new OAuthExpectationFailedException("consumer secret not set");
/*     */     }
/*     */     
/*  91 */     this.requestParameters = new HttpParameters();
/*     */     try {
/*  93 */       if (this.additionalParameters != null) {
/*  94 */         this.requestParameters.putAll((Map)this.additionalParameters, false);
/*     */       }
/*  96 */       collectHeaderParameters(request, this.requestParameters);
/*  97 */       collectQueryParameters(request, this.requestParameters);
/*  98 */       collectBodyParameters(request, this.requestParameters);
/*     */ 
/*     */       
/* 101 */       completeOAuthParameters(this.requestParameters);
/*     */       
/* 103 */       this.requestParameters.remove("oauth_signature");
/*     */     }
/* 105 */     catch (IOException e) {
/* 106 */       throw new OAuthCommunicationException(e);
/*     */     } 
/*     */     
/* 109 */     String signature = this.messageSigner.sign(request, this.requestParameters);
/* 110 */     OAuth.debugOut("signature", signature);
/*     */     
/* 112 */     this.signingStrategy.writeSignature(signature, request, this.requestParameters);
/* 113 */     OAuth.debugOut("Request URL", request.getRequestUrl());
/*     */     
/* 115 */     return request;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized HttpRequest sign(Object request) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
/* 120 */     return sign(wrap(request));
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String sign(String url) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
/* 125 */     UrlStringRequestAdapter urlStringRequestAdapter = new UrlStringRequestAdapter(url);
/*     */ 
/*     */     
/* 128 */     SigningStrategy oldStrategy = this.signingStrategy;
/* 129 */     this.signingStrategy = (SigningStrategy)new QueryStringSigningStrategy();
/*     */     
/* 131 */     sign((HttpRequest)urlStringRequestAdapter);
/*     */ 
/*     */     
/* 134 */     this.signingStrategy = oldStrategy;
/*     */     
/* 136 */     return urlStringRequestAdapter.getRequestUrl();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract HttpRequest wrap(Object paramObject);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTokenWithSecret(String token, String tokenSecret) {
/* 150 */     this.token = token;
/* 151 */     this.messageSigner.setTokenSecret(tokenSecret);
/*     */   }
/*     */   
/*     */   public String getToken() {
/* 155 */     return this.token;
/*     */   }
/*     */   
/*     */   public String getTokenSecret() {
/* 159 */     return this.messageSigner.getTokenSecret();
/*     */   }
/*     */   
/*     */   public String getConsumerKey() {
/* 163 */     return this.consumerKey;
/*     */   }
/*     */   
/*     */   public String getConsumerSecret() {
/* 167 */     return this.consumerSecret;
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
/*     */   protected void completeOAuthParameters(HttpParameters out) {
/* 187 */     if (!out.containsKey("oauth_consumer_key")) {
/* 188 */       out.put("oauth_consumer_key", this.consumerKey, true);
/*     */     }
/* 190 */     if (!out.containsKey("oauth_signature_method")) {
/* 191 */       out.put("oauth_signature_method", this.messageSigner.getSignatureMethod(), true);
/*     */     }
/* 193 */     if (!out.containsKey("oauth_timestamp")) {
/* 194 */       out.put("oauth_timestamp", generateTimestamp(), true);
/*     */     }
/* 196 */     if (!out.containsKey("oauth_nonce")) {
/* 197 */       out.put("oauth_nonce", generateNonce(), true);
/*     */     }
/* 199 */     if (!out.containsKey("oauth_version")) {
/* 200 */       out.put("oauth_version", "1.0", true);
/*     */     }
/* 202 */     if (!out.containsKey("oauth_token") && ((
/* 203 */       this.token != null && !this.token.equals("")) || this.sendEmptyTokens)) {
/* 204 */       out.put("oauth_token", this.token, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpParameters getRequestParameters() {
/* 210 */     return this.requestParameters;
/*     */   }
/*     */   
/*     */   public void setSendEmptyTokens(boolean enable) {
/* 214 */     this.sendEmptyTokens = enable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void collectHeaderParameters(HttpRequest request, HttpParameters out) {
/* 222 */     HttpParameters headerParams = OAuth.oauthHeaderToParamsMap(request.getHeader("Authorization"));
/* 223 */     out.putAll((Map)headerParams, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void collectBodyParameters(HttpRequest request, HttpParameters out) throws IOException {
/* 234 */     String contentType = request.getContentType();
/* 235 */     if (contentType != null && contentType.startsWith("application/x-www-form-urlencoded")) {
/* 236 */       InputStream payload = request.getMessagePayload();
/* 237 */       out.putAll((Map)OAuth.decodeForm(payload), true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void collectQueryParameters(HttpRequest request, HttpParameters out) {
/* 247 */     String url = request.getRequestUrl();
/* 248 */     int q = url.indexOf('?');
/* 249 */     if (q >= 0)
/*     */     {
/* 251 */       out.putAll((Map)OAuth.decodeForm(url.substring(q + 1)), true);
/*     */     }
/*     */   }
/*     */   
/*     */   protected String generateTimestamp() {
/* 256 */     return Long.toString(System.currentTimeMillis() / 1000L);
/*     */   }
/*     */   
/*     */   protected String generateNonce() {
/* 260 */     return Long.toString(this.random.nextLong());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\AbstractOAuthConsumer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */