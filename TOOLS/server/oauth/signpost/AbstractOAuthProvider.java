/*     */ package oauth.signpost;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import oauth.signpost.exception.OAuthCommunicationException;
/*     */ import oauth.signpost.exception.OAuthExpectationFailedException;
/*     */ import oauth.signpost.exception.OAuthMessageSignerException;
/*     */ import oauth.signpost.exception.OAuthNotAuthorizedException;
/*     */ import oauth.signpost.http.HttpParameters;
/*     */ import oauth.signpost.http.HttpRequest;
/*     */ import oauth.signpost.http.HttpResponse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractOAuthProvider
/*     */   implements OAuthProvider
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private String requestTokenEndpointUrl;
/*     */   private String accessTokenEndpointUrl;
/*     */   private String authorizationWebsiteUrl;
/*     */   private HttpParameters responseParameters;
/*     */   private Map<String, String> defaultHeaders;
/*     */   private boolean isOAuth10a;
/*     */   private transient OAuthProviderListener listener;
/*     */   
/*     */   public AbstractOAuthProvider(String requestTokenEndpointUrl, String accessTokenEndpointUrl, String authorizationWebsiteUrl) {
/*  53 */     this.requestTokenEndpointUrl = requestTokenEndpointUrl;
/*  54 */     this.accessTokenEndpointUrl = accessTokenEndpointUrl;
/*  55 */     this.authorizationWebsiteUrl = authorizationWebsiteUrl;
/*  56 */     this.responseParameters = new HttpParameters();
/*  57 */     this.defaultHeaders = new HashMap<String, String>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String retrieveRequestToken(OAuthConsumer consumer, String callbackUrl, String... customOAuthParams) throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException {
/*  66 */     consumer.setTokenWithSecret(null, null);
/*     */ 
/*     */ 
/*     */     
/*  70 */     HttpParameters params = new HttpParameters();
/*  71 */     params.putAll(customOAuthParams, true);
/*  72 */     params.put("oauth_callback", callbackUrl, true);
/*     */     
/*  74 */     retrieveToken(consumer, this.requestTokenEndpointUrl, params);
/*     */     
/*  76 */     String callbackConfirmed = this.responseParameters.getFirst("oauth_callback_confirmed");
/*  77 */     this.responseParameters.remove("oauth_callback_confirmed");
/*  78 */     this.isOAuth10a = Boolean.TRUE.toString().equals(callbackConfirmed);
/*     */ 
/*     */ 
/*     */     
/*  82 */     if (this.isOAuth10a) {
/*  83 */       return OAuth.addQueryParameters(this.authorizationWebsiteUrl, new String[] { "oauth_token", consumer.getToken() });
/*     */     }
/*     */     
/*  86 */     return OAuth.addQueryParameters(this.authorizationWebsiteUrl, new String[] { "oauth_token", consumer.getToken(), "oauth_callback", callbackUrl });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void retrieveAccessToken(OAuthConsumer consumer, String oauthVerifier, String... customOAuthParams) throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException {
/*  96 */     if (consumer.getToken() == null || consumer.getTokenSecret() == null) {
/*  97 */       throw new OAuthExpectationFailedException("Authorized request token or token secret not set. Did you retrieve an authorized request token before?");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 102 */     HttpParameters params = new HttpParameters();
/* 103 */     params.putAll(customOAuthParams, true);
/*     */     
/* 105 */     if (this.isOAuth10a && oauthVerifier != null) {
/* 106 */       params.put("oauth_verifier", oauthVerifier, true);
/*     */     }
/* 108 */     retrieveToken(consumer, this.accessTokenEndpointUrl, params);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void retrieveToken(OAuthConsumer consumer, String endpointUrl, HttpParameters customOAuthParams) throws OAuthMessageSignerException, OAuthCommunicationException, OAuthNotAuthorizedException, OAuthExpectationFailedException {
/* 154 */     Map<String, String> defaultHeaders = getRequestHeaders();
/*     */     
/* 156 */     if (consumer.getConsumerKey() == null || consumer.getConsumerSecret() == null) {
/* 157 */       throw new OAuthExpectationFailedException("Consumer key or secret not set");
/*     */     }
/*     */     
/* 160 */     HttpRequest request = null;
/* 161 */     HttpResponse response = null;
/*     */     try {
/* 163 */       request = createRequest(endpointUrl);
/* 164 */       for (String header : defaultHeaders.keySet()) {
/* 165 */         request.setHeader(header, defaultHeaders.get(header));
/*     */       }
/* 167 */       if (customOAuthParams != null && !customOAuthParams.isEmpty()) {
/* 168 */         consumer.setAdditionalParameters(customOAuthParams);
/*     */       }
/*     */       
/* 171 */       if (this.listener != null) {
/* 172 */         this.listener.prepareRequest(request);
/*     */       }
/*     */       
/* 175 */       consumer.sign(request);
/*     */       
/* 177 */       if (this.listener != null) {
/* 178 */         this.listener.prepareSubmission(request);
/*     */       }
/*     */       
/* 181 */       response = sendRequest(request);
/* 182 */       int statusCode = response.getStatusCode();
/*     */       
/* 184 */       boolean requestHandled = false;
/* 185 */       if (this.listener != null) {
/* 186 */         requestHandled = this.listener.onResponseReceived(request, response);
/*     */       }
/* 188 */       if (requestHandled) {
/*     */         return;
/*     */       }
/*     */       
/* 192 */       if (statusCode >= 300) {
/* 193 */         handleUnexpectedResponse(statusCode, response);
/*     */       }
/*     */       
/* 196 */       HttpParameters responseParams = OAuth.decodeForm(response.getContent());
/*     */       
/* 198 */       String token = responseParams.getFirst("oauth_token");
/* 199 */       String secret = responseParams.getFirst("oauth_token_secret");
/* 200 */       responseParams.remove("oauth_token");
/* 201 */       responseParams.remove("oauth_token_secret");
/*     */       
/* 203 */       setResponseParameters(responseParams);
/*     */       
/* 205 */       if (token == null || secret == null) {
/* 206 */         throw new OAuthExpectationFailedException("Request token or token secret not set in server reply. The service provider you use is probably buggy.");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 211 */       consumer.setTokenWithSecret(token, secret);
/*     */     }
/* 213 */     catch (OAuthNotAuthorizedException e) {
/* 214 */       throw e;
/* 215 */     } catch (OAuthExpectationFailedException e) {
/* 216 */       throw e;
/* 217 */     } catch (Exception e) {
/* 218 */       throw new OAuthCommunicationException(e);
/*     */     } finally {
/*     */       try {
/* 221 */         closeConnection(request, response);
/* 222 */       } catch (Exception e) {
/* 223 */         throw new OAuthCommunicationException(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void handleUnexpectedResponse(int statusCode, HttpResponse response) throws Exception {
/* 229 */     if (response == null) {
/*     */       return;
/*     */     }
/* 232 */     BufferedReader reader = new BufferedReader(new InputStreamReader(response.getContent()));
/* 233 */     StringBuilder responseBody = new StringBuilder();
/*     */     
/* 235 */     String line = reader.readLine();
/* 236 */     while (line != null) {
/* 237 */       responseBody.append(line);
/* 238 */       line = reader.readLine();
/*     */     } 
/*     */     
/* 241 */     switch (statusCode) {
/*     */       case 401:
/* 243 */         throw new OAuthNotAuthorizedException(responseBody.toString());
/*     */     } 
/* 245 */     throw new OAuthCommunicationException("Service provider responded in error: " + statusCode + " (" + response.getReasonPhrase() + ")", responseBody.toString());
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
/*     */   protected abstract HttpRequest createRequest(String paramString) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract HttpResponse sendRequest(HttpRequest paramHttpRequest) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeConnection(HttpRequest request, HttpResponse response) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpParameters getResponseParameters() {
/* 290 */     return this.responseParameters;
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
/*     */   protected String getResponseParameter(String key) {
/* 303 */     return this.responseParameters.getFirst(key);
/*     */   }
/*     */   
/*     */   public void setResponseParameters(HttpParameters parameters) {
/* 307 */     this.responseParameters = parameters;
/*     */   }
/*     */   
/*     */   public void setOAuth10a(boolean isOAuth10aProvider) {
/* 311 */     this.isOAuth10a = isOAuth10aProvider;
/*     */   }
/*     */   
/*     */   public boolean isOAuth10a() {
/* 315 */     return this.isOAuth10a;
/*     */   }
/*     */   
/*     */   public String getRequestTokenEndpointUrl() {
/* 319 */     return this.requestTokenEndpointUrl;
/*     */   }
/*     */   
/*     */   public String getAccessTokenEndpointUrl() {
/* 323 */     return this.accessTokenEndpointUrl;
/*     */   }
/*     */   
/*     */   public String getAuthorizationWebsiteUrl() {
/* 327 */     return this.authorizationWebsiteUrl;
/*     */   }
/*     */   
/*     */   public void setRequestHeader(String header, String value) {
/* 331 */     this.defaultHeaders.put(header, value);
/*     */   }
/*     */   
/*     */   public Map<String, String> getRequestHeaders() {
/* 335 */     return this.defaultHeaders;
/*     */   }
/*     */   
/*     */   public void setListener(OAuthProviderListener listener) {
/* 339 */     this.listener = listener;
/*     */   }
/*     */   
/*     */   public void removeListener(OAuthProviderListener listener) {
/* 343 */     this.listener = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\AbstractOAuthProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */