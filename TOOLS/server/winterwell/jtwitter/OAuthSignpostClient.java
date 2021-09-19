/*     */ package winterwell.jtwitter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.io.StringBufferInputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import oauth.signpost.OAuthConsumer;
/*     */ import oauth.signpost.basic.DefaultOAuthProvider;
/*     */ import oauth.signpost.basic.HttpURLConnectionRequestAdapter;
/*     */ import oauth.signpost.exception.OAuthException;
/*     */ import oauth.signpost.http.HttpRequest;
/*     */ import oauth.signpost.signature.AuthorizationHeaderSigningStrategy;
/*     */ import oauth.signpost.signature.SigningStrategy;
/*     */ import winterwell.jtwitter.guts.ClientHttpRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OAuthSignpostClient
/*     */   extends URLConnectionHttpClient
/*     */   implements Twitter.IHttpClient, Serializable
/*     */ {
/*     */   public final String postMultipartForm(String url, Map<String, ?> vars) throws TwitterException {
/* 115 */     String resource = checkRateLimit(url);
/*     */     try {
/* 117 */       HttpURLConnection connection = (HttpURLConnection)(new URL(url)).openConnection();
/* 118 */       connection.setRequestMethod("POST");
/* 119 */       connection.setReadTimeout(this.timeout);
/* 120 */       connection.setConnectTimeout(this.timeout);
/*     */       
/* 122 */       Map<String, String> vars2 = new HashMap<String, String>();
/*     */       
/* 124 */       final String payload = post2_getPayload(vars2);
/*     */ 
/*     */       
/* 127 */       HttpURLConnectionRequestAdapter wrapped = new HttpURLConnectionRequestAdapter(
/* 128 */           connection)
/*     */         {
/*     */           
/*     */           public InputStream getMessagePayload() throws IOException
/*     */           {
/* 133 */             return new StringBufferInputStream(payload);
/*     */           }
/*     */         };
/*     */ 
/*     */       
/* 138 */       SimpleOAuthConsumer _consumer = new SimpleOAuthConsumer(this.consumerKey, this.consumerSecret);
/* 139 */       _consumer.setTokenWithSecret(this.accessToken, this.accessTokenSecret);
/* 140 */       AuthorizationHeaderSigningStrategy authorizationHeaderSigningStrategy = new AuthorizationHeaderSigningStrategy();
/* 141 */       _consumer.setSigningStrategy((SigningStrategy)authorizationHeaderSigningStrategy);
/* 142 */       _consumer.sign((HttpRequest)wrapped);
/*     */       
/* 144 */       ClientHttpRequest req = new ClientHttpRequest(connection);
/* 145 */       InputStream page = req.post(vars);
/*     */       
/* 147 */       processError(connection, resource);
/* 148 */       processHeaders(connection, resource);
/* 149 */       return InternalUtils.read(page);
/* 150 */     } catch (TwitterException e) {
/* 151 */       throw e;
/* 152 */     } catch (Exception e) {
/* 153 */       throw new TwitterException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   private static final DefaultOAuthProvider FOURSQUARE_PROVIDER = new DefaultOAuthProvider(
/* 162 */       "http://foursquare.com/oauth/request_token", 
/* 163 */       "http://foursquare.com/oauth/access_token", 
/* 164 */       "http://foursquare.com/oauth/authorize");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 169 */   private static final DefaultOAuthProvider LINKEDIN_PROVIDER = new DefaultOAuthProvider(
/* 170 */       "https://api.linkedin.com/uas/oauth/requestToken", 
/* 171 */       "https://api.linkedin.com/uas/oauth/accessToken", 
/* 172 */       "https://www.linkedin.com/uas/oauth/authorize");
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String JTWITTER_OAUTH_KEY = "Cz8ZLgitPR2jrQVaD6ncw";
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String JTWITTER_OAUTH_SECRET = "9FFYaWJSvQ6Yi5tctN30eN6DnXWmdw0QgJMl7V6KGI";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */   
/*     */   private String accessToken;
/*     */ 
/*     */ 
/*     */   
/*     */   private String accessTokenSecret;
/*     */ 
/*     */ 
/*     */   
/*     */   private String callbackUrl;
/*     */ 
/*     */   
/*     */   private OAuthConsumer consumer;
/*     */ 
/*     */   
/*     */   private String consumerKey;
/*     */ 
/*     */   
/*     */   private String consumerSecret;
/*     */ 
/*     */   
/*     */   private DefaultOAuthProvider provider;
/*     */ 
/*     */ 
/*     */   
/*     */   public static String askUser(String question) {
/*     */     try {
/* 214 */       Class<?> JOptionPaneClass = 
/* 215 */         Class.forName("javax.swing.JOptionPane");
/* 216 */       Method showInputDialog = JOptionPaneClass.getMethod(
/* 217 */           "showInputDialog", new Class[] { Object.class });
/* 218 */       return (String)showInputDialog.invoke(null, new Object[] { question });
/* 219 */     } catch (Exception e) {
/* 220 */       throw new RuntimeException(e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OAuthSignpostClient(String consumerKey, String consumerSecret, String callbackUrl) {
/* 242 */     assert consumerKey != null && consumerSecret != null && 
/* 243 */       callbackUrl != null;
/* 244 */     this.consumerKey = consumerKey;
/* 245 */     this.consumerSecret = consumerSecret;
/* 246 */     this.callbackUrl = callbackUrl;
/* 247 */     init();
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
/*     */   public OAuthSignpostClient(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
/* 263 */     this.consumerKey = consumerKey.toString();
/* 264 */     this.consumerSecret = consumerSecret.toString();
/* 265 */     this.accessToken = accessToken.toString();
/* 266 */     this.accessTokenSecret = accessTokenSecret.toString();
/* 267 */     init();
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
/*     */   @Deprecated
/*     */   public void authorizeDesktop() {
/* 283 */     URI uri = authorizeUrl();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 289 */       Class<?> desktopClass = Class.forName("java.awt.Desktop");
/* 290 */       Method getDesktop = desktopClass.getMethod("getDesktop", null);
/* 291 */       Object d = getDesktop.invoke(null, null);
/*     */       
/* 293 */       Method browse = desktopClass.getMethod("browse", new Class[] { URI.class });
/* 294 */       browse.invoke(d, new Object[] { uri });
/* 295 */     } catch (Exception e) {
/* 296 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI authorizeUrl() {
/*     */     try {
/* 307 */       String url = this.provider.retrieveRequestToken(this.consumer, this.callbackUrl);
/* 308 */       return new URI(url);
/* 309 */     } catch (Exception e) {
/*     */       
/* 311 */       throw new TwitterException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canAuthenticate() {
/* 317 */     return (this.consumer.getToken() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Twitter.IHttpClient copy() {
/* 322 */     return clone();
/*     */   }
/*     */ 
/*     */   
/*     */   public URLConnectionHttpClient clone() {
/* 327 */     OAuthSignpostClient c = (OAuthSignpostClient)super.clone();
/* 328 */     c.consumerKey = this.consumerKey;
/* 329 */     c.consumerSecret = this.consumerSecret;
/* 330 */     c.accessToken = this.accessToken;
/* 331 */     c.accessTokenSecret = this.accessTokenSecret;
/* 332 */     c.callbackUrl = this.callbackUrl;
/* 333 */     c.init();
/* 334 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getAccessToken() {
/* 343 */     if (this.accessToken == null)
/* 344 */       return null; 
/* 345 */     return new String[] { this.accessToken, this.accessTokenSecret };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getName() {
/* 352 */     return (this.name == null) ? "?user" : this.name;
/*     */   }
/*     */   
/*     */   private void init() {
/* 356 */     this.consumer = (OAuthConsumer)new SimpleOAuthConsumer(this.consumerKey, this.consumerSecret);
/* 357 */     if (this.accessToken != null) {
/* 358 */       this.consumer.setTokenWithSecret(this.accessToken, this.accessTokenSecret);
/*     */     }
/* 360 */     this.provider = new DefaultOAuthProvider(
/* 361 */         "https://api.twitter.com/oauth/request_token", 
/* 362 */         "https://api.twitter.com/oauth/access_token", 
/* 363 */         "https://api.twitter.com/oauth/authorize");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpURLConnection post2_connect(String uri, Map<String, String> vars) throws IOException, OAuthException {
/* 370 */     String resource = checkRateLimit(uri);
/* 371 */     HttpURLConnection connection = (HttpURLConnection)(new URL(uri))
/* 372 */       .openConnection();
/* 373 */     connection.setRequestMethod("POST");
/* 374 */     connection.setDoOutput(true);
/* 375 */     connection.setRequestProperty("Content-Type", 
/* 376 */         "application/x-www-form-urlencoded");
/* 377 */     connection.setReadTimeout(this.timeout);
/* 378 */     connection.setConnectTimeout(this.timeout);
/* 379 */     final String payload = post2_getPayload(vars);
/*     */     
/* 381 */     HttpURLConnectionRequestAdapter wrapped = new HttpURLConnectionRequestAdapter(
/* 382 */         connection)
/*     */       {
/*     */         
/*     */         public InputStream getMessagePayload() throws IOException
/*     */         {
/* 387 */           return new StringBufferInputStream(payload);
/*     */         }
/*     */       };
/*     */     
/* 391 */     this.consumer.sign((HttpRequest)wrapped);
/*     */ 
/*     */     
/* 394 */     OutputStream os = connection.getOutputStream();
/* 395 */     os.write(payload.getBytes());
/* 396 */     InternalUtils.close(os);
/*     */     
/* 398 */     processError(connection, resource);
/* 399 */     processHeaders(connection, resource);
/* 400 */     return connection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setAuthentication(URLConnection connection, String name, String password) {
/*     */     try {
/* 409 */       this.consumer.sign(connection);
/* 410 */     } catch (OAuthException e) {
/* 411 */       throw new TwitterException(e);
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
/*     */   public void setAuthorizationCode(String verifier) throws TwitterException {
/* 427 */     if (this.accessToken != null) {
/*     */       
/* 429 */       this.accessToken = null;
/* 430 */       init();
/*     */     } 
/*     */     try {
/* 433 */       this.provider.retrieveAccessToken(this.consumer, verifier);
/* 434 */       this.accessToken = this.consumer.getToken();
/* 435 */       this.accessTokenSecret = this.consumer.getTokenSecret();
/* 436 */     } catch (Exception e) {
/* 437 */       if (e.getMessage().contains("401")) {
/* 438 */         throw new TwitterException.E401(e.getMessage());
/*     */       }
/* 440 */       throw new TwitterException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setFoursquareProvider() {
/* 445 */     setProvider(FOURSQUARE_PROVIDER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLinkedInProvider() {
/* 452 */     setProvider(LINKEDIN_PROVIDER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 462 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProvider(DefaultOAuthProvider provider) {
/* 473 */     this.provider = provider;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\OAuthSignpostClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */