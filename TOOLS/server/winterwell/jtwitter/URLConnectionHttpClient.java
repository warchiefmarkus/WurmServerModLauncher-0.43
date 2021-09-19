/*     */ package winterwell.jtwitter;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.net.ConnectException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.SocketException;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.nio.charset.MalformedInputException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import winterwell.json.JSONArray;
/*     */ import winterwell.json.JSONObject;
/*     */ import winterwell.jtwitter.guts.Base64Encoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class URLConnectionHttpClient
/*     */   implements Twitter.IHttpClient, Serializable, Cloneable
/*     */ {
/*     */   private static final int dfltTimeOutMilliSecs = 10000;
/*     */   private static final long serialVersionUID = 1L;
/*     */   private Map<String, List<String>> headers;
/*     */   int minRateLimit;
/*     */   protected String name;
/*     */   private String password;
/*  55 */   private Map<String, RateLimit> rateLimits = Collections.synchronizedMap(new HashMap<String, RateLimit>());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean retryOnError;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRetryOnError() {
/*  67 */     return this.retryOnError;
/*     */   }
/*     */   
/*  70 */   protected int timeout = 10000;
/*     */ 
/*     */   
/*     */   private boolean htmlImpliesError = true;
/*     */ 
/*     */   
/*     */   private boolean gzip = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGzip(boolean gzip) {
/*  81 */     this.gzip = gzip;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHtmlImpliesError(boolean htmlImpliesError) {
/*  89 */     this.htmlImpliesError = htmlImpliesError;
/*     */   }
/*     */   
/*     */   public URLConnectionHttpClient() {
/*  93 */     this(null, null);
/*     */   }
/*     */   
/*     */   public URLConnectionHttpClient(String name, String password) {
/*  97 */     this.name = name;
/*  98 */     this.password = password;
/*     */     
/* 100 */     assert name == null && password == null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canAuthenticate() {
/* 105 */     return (this.name != null && this.password != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpURLConnection connect(String url, Map<String, String> vars, boolean authenticate) throws IOException {
/* 113 */     String resource = checkRateLimit(url);
/*     */     
/* 115 */     if (vars != null && vars.size() != 0) {
/*     */       
/* 117 */       StringBuilder uri = new StringBuilder(url);
/* 118 */       if (url.indexOf('?') == -1) {
/* 119 */         uri.append("?");
/* 120 */       } else if (!url.endsWith("&")) {
/* 121 */         uri.append("&");
/*     */       } 
/* 123 */       for (Map.Entry<String, String> e : vars.entrySet()) {
/* 124 */         if (e.getValue() == null) {
/*     */           continue;
/*     */         }
/* 127 */         String ek = InternalUtils.encode(e.getKey());
/* 128 */         assert !url.contains(String.valueOf(ek) + "=") : String.valueOf(url) + " " + vars;
/* 129 */         uri.append(String.valueOf(ek) + "=" + InternalUtils.encode(e.getValue()) + "&");
/*     */       } 
/* 131 */       url = uri.toString();
/*     */     } 
/*     */     
/* 134 */     HttpURLConnection connection = (HttpURLConnection)(new URL(url))
/* 135 */       .openConnection();
/*     */     
/* 137 */     if (authenticate) {
/* 138 */       setAuthentication(connection, this.name, this.password);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 143 */     connection.setRequestProperty("User-Agent", "JTwitter/2.8.7");
/* 144 */     connection.setRequestProperty("Host", "api.twitter.com");
/* 145 */     if (this.gzip) {
/* 146 */       connection.setRequestProperty("Accept-Encoding", "gzip");
/*     */     }
/* 148 */     connection.setDoInput(true);
/* 149 */     connection.setConnectTimeout(this.timeout);
/* 150 */     connection.setReadTimeout(this.timeout);
/* 151 */     connection.setConnectTimeout(this.timeout);
/*     */     
/* 153 */     processError(connection, resource);
/* 154 */     processHeaders(connection, resource);
/* 155 */     return connection;
/*     */   }
/*     */ 
/*     */   
/*     */   public Twitter.IHttpClient copy() {
/* 160 */     return clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URLConnectionHttpClient clone() {
/*     */     try {
/* 169 */       URLConnectionHttpClient c = (URLConnectionHttpClient)super.clone();
/* 170 */       c.name = this.name;
/* 171 */       c.password = this.password;
/* 172 */       c.gzip = this.gzip;
/* 173 */       c.htmlImpliesError = this.htmlImpliesError;
/* 174 */       c.setRetryOnError(this.retryOnError);
/* 175 */       c.setTimeout(this.timeout);
/* 176 */       c.setMinRateLimit(this.minRateLimit);
/* 177 */       c.rateLimits = this.rateLimits;
/*     */       
/* 179 */       return c;
/* 180 */     } catch (CloneNotSupportedException ex) {
/* 181 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final void disconnect(HttpURLConnection connection) {
/* 186 */     if (connection == null)
/*     */       return; 
/*     */     try {
/* 189 */       connection.disconnect();
/* 190 */     } catch (Throwable throwable) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String getErrorStream(HttpURLConnection connection) {
/*     */     try {
/* 197 */       return InternalUtils.read(connection.getErrorStream());
/* 198 */     } catch (NullPointerException e) {
/* 199 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getHeader(String headerName) {
/* 205 */     if (this.headers == null)
/* 206 */       return null; 
/* 207 */     List<String> vals = this.headers.get(headerName);
/* 208 */     return (vals == null || vals.isEmpty()) ? null : vals.get(0);
/*     */   }
/*     */   
/*     */   String getName() {
/* 212 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, RateLimit> getRateLimits() {
/* 217 */     return this.rateLimits;
/*     */   }
/*     */   
/*     */   public Map<String, RateLimit> updateRateLimits() {
/* 221 */     Map<String, String> vars = null;
/* 222 */     String json = getPage("https://api.twitter.com/1.1/application/rate_limit_status.json", vars, true);
/*     */     
/* 224 */     JSONObject jo = (new JSONObject(json)).getJSONObject("resources");
/*     */     
/* 226 */     Collection<JSONObject> families = jo.getMap().values();
/* 227 */     for (JSONObject family : families) {
/* 228 */       for (String res : family.getMap().keySet()) {
/*     */         
/* 230 */         JSONObject jrl = (JSONObject)family.getMap().get(res);
/* 231 */         RateLimit rl = new RateLimit(jrl);
/* 232 */         this.rateLimits.put(res, rl);
/*     */       } 
/*     */     } 
/*     */     
/* 236 */     return getRateLimits();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getPage(String url, Map<String, String> vars, boolean authenticate) throws TwitterException {
/* 244 */     assert url != null;
/* 245 */     InternalUtils.count(url);
/*     */ 
/*     */     
/*     */     try {
/* 249 */       String json = getPage2(url, vars, authenticate);
/*     */       
/* 251 */       if (this.htmlImpliesError && (
/* 252 */         json.startsWith("<!DOCTYPE html") || json.startsWith("<html")))
/*     */       {
/* 254 */         if (!url.startsWith("https://twitter.com")) {
/*     */ 
/*     */           
/* 257 */           String meat = InternalUtils.stripTags(json);
/* 258 */           throw new TwitterException.E50X(meat);
/*     */         } 
/*     */       }
/* 261 */       return json;
/* 262 */     } catch (SocketTimeoutException e) {
/* 263 */       if (!this.retryOnError) throw getPage2_ex(e, url);
/*     */       
/*     */       try {
/* 266 */         Thread.sleep(500L);
/* 267 */         return getPage2(url, vars, authenticate);
/* 268 */       } catch (Exception e2) {
/* 269 */         throw getPage2_ex(e, url);
/*     */       } 
/* 271 */     } catch (E50X e) {
/* 272 */       if (!this.retryOnError) throw getPage2_ex(e, url);
/*     */       
/*     */       try {
/* 275 */         Thread.sleep(500L);
/* 276 */         return getPage2(url, vars, authenticate);
/* 277 */       } catch (Exception e2) {
/* 278 */         throw getPage2_ex(e, url);
/*     */       } 
/* 280 */     } catch (IOException e) {
/* 281 */       throw new TwitterException.IO(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TwitterException getPage2_ex(Exception ex, String url) {
/* 289 */     if (ex instanceof TwitterException) return (TwitterException)ex; 
/* 290 */     if (ex instanceof SocketTimeoutException) {
/* 291 */       return new TwitterException.Timeout(url);
/*     */     }
/* 293 */     if (ex instanceof IOException) {
/* 294 */       return new TwitterException.IO((IOException)ex);
/*     */     }
/* 296 */     return new TwitterException(ex);
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
/*     */   private String getPage2(String url, Map<String, String> vars, boolean authenticate) throws IOException {
/* 309 */     HttpURLConnection connection = null;
/*     */     try {
/* 311 */       connection = connect(url, vars, authenticate);
/* 312 */       InputStream inStream = connection.getInputStream();
/*     */ 
/*     */       
/* 315 */       String contentEncoding = connection.getContentEncoding();
/* 316 */       if ("gzip".equals(contentEncoding)) {
/* 317 */         inStream = new GZIPInputStream(inStream);
/*     */       }
/*     */       
/* 320 */       String page = InternalUtils.read(inStream);
/*     */       
/* 322 */       return page;
/* 323 */     } catch (MalformedInputException ex) {
/*     */       
/* 325 */       throw new IOException(ex + " enc:" + connection.getContentEncoding());
/*     */     } finally {
/* 327 */       disconnect(connection);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public RateLimit getRateLimit(Twitter.KRequestType reqType) {
/* 333 */     return this.rateLimits.get(reqType.rateLimit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String post(String uri, Map<String, String> vars, boolean authenticate) throws TwitterException {
/* 344 */     InternalUtils.count(uri);
/*     */     
/*     */     try {
/* 347 */       String json = post2(uri, vars, authenticate);
/*     */       
/* 349 */       return json;
/* 350 */     } catch (E50X e) {
/* 351 */       if (!this.retryOnError) throw getPage2_ex(e, uri);
/*     */       
/*     */       try {
/* 354 */         Thread.sleep(500L);
/* 355 */         return post2(uri, vars, authenticate);
/* 356 */       } catch (Exception e2) {
/* 357 */         throw getPage2_ex(e, uri);
/*     */       } 
/* 359 */     } catch (SocketTimeoutException e) {
/* 360 */       if (!this.retryOnError) throw getPage2_ex(e, uri);
/*     */       
/*     */       try {
/* 363 */         Thread.sleep(500L);
/* 364 */         return post2(uri, vars, authenticate);
/* 365 */       } catch (Exception e2) {
/* 366 */         throw getPage2_ex(e, uri);
/*     */       } 
/* 368 */     } catch (Exception e) {
/* 369 */       throw getPage2_ex(e, uri);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String post2(String uri, Map<String, String> vars, boolean authenticate) throws Exception {
/* 376 */     HttpURLConnection connection = null;
/*     */     try {
/* 378 */       connection = post2_connect(uri, vars);
/*     */       
/* 380 */       String response = InternalUtils.read(connection
/* 381 */           .getInputStream());
/* 382 */       return response;
/*     */     } finally {
/* 384 */       disconnect(connection);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpURLConnection post2_connect(String uri, Map<String, String> vars) throws Exception {
/* 392 */     String resource = checkRateLimit(uri);
/* 393 */     InternalUtils.count(uri);
/* 394 */     HttpURLConnection connection = (HttpURLConnection)(new URL(uri))
/* 395 */       .openConnection();
/* 396 */     connection.setRequestMethod("POST");
/* 397 */     connection.setDoOutput(true);
/*     */     
/* 399 */     setAuthentication(connection, this.name, this.password);
/*     */     
/* 401 */     connection.setRequestProperty("Content-Type", 
/* 402 */         "application/x-www-form-urlencoded");
/* 403 */     connection.setReadTimeout(this.timeout);
/* 404 */     connection.setConnectTimeout(this.timeout);
/*     */     
/* 406 */     String payload = post2_getPayload(vars);
/* 407 */     connection.setRequestProperty("Content-Length", payload.length());
/* 408 */     OutputStream os = connection.getOutputStream();
/* 409 */     os.write(payload.getBytes());
/* 410 */     InternalUtils.close(os);
/*     */     
/* 412 */     processError(connection, resource);
/* 413 */     processHeaders(connection, resource);
/* 414 */     return connection;
/*     */   }
/*     */   
/*     */   protected String checkRateLimit(String url) {
/* 418 */     String resource = RateLimit.getResource(url);
/* 419 */     RateLimit limit = this.rateLimits.get(resource);
/* 420 */     if (limit != null && limit.getRemaining() <= this.minRateLimit && 
/* 421 */       !limit.isOutOfDate())
/*     */     {
/* 423 */       throw new TwitterException.RateLimit(
/* 424 */           "Pre-emptive rate-limit block for " + limit + " for " + url);
/*     */     }
/* 426 */     return resource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String post2_getPayload(Map<String, String> vars) {
/* 436 */     if (vars == null || vars.isEmpty())
/* 437 */       return ""; 
/* 438 */     StringBuilder encodedData = new StringBuilder();
/*     */ 
/*     */     
/* 441 */     if (vars.size() == 1) {
/* 442 */       String key = vars.keySet().iterator().next();
/* 443 */       if ("".equals(key)) {
/* 444 */         String val = InternalUtils.encode(vars.get(key));
/* 445 */         return val;
/*     */       } 
/*     */     } 
/*     */     
/* 449 */     for (String key : vars.keySet()) {
/* 450 */       String val = InternalUtils.encode(vars.get(key));
/* 451 */       encodedData.append(InternalUtils.encode(key));
/* 452 */       encodedData.append('=');
/* 453 */       encodedData.append(val);
/* 454 */       encodedData.append('&');
/*     */     } 
/* 456 */     encodedData.deleteCharAt(encodedData.length() - 1);
/* 457 */     return encodedData.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void processError(HttpURLConnection connection, String resource) {
/*     */     try {
/* 467 */       int code = connection.getResponseCode();
/* 468 */       if (code == 200)
/*     */         return; 
/* 470 */       URL url = connection.getURL();
/*     */       
/* 472 */       String error = processError2_reason(connection);
/*     */       
/* 474 */       if (code == 401) {
/* 475 */         if (error.contains("Basic authentication is not supported"))
/* 476 */           throw new TwitterException.UpdateToOAuth(); 
/* 477 */         throw new TwitterException.E401(String.valueOf(error) + "\n" + url + " (" + (
/* 478 */             (this.name == null) ? "anonymous" : this.name) + ")");
/*     */       } 
/* 480 */       if (code == 400 && error.startsWith("code 215"))
/*     */       {
/* 482 */         throw new TwitterException.E401(error);
/*     */       }
/* 484 */       if (code == 403)
/*     */       {
/* 486 */         processError2_403(connection, resource, url, error);
/*     */       }
/* 488 */       if (code == 404) {
/*     */         
/* 490 */         if (error != null && error.contains("deleted"))
/*     */         {
/* 492 */           throw new TwitterException.SuspendedUser(String.valueOf(error) + "\n" + url); } 
/* 493 */         throw new TwitterException.E404(String.valueOf(error) + "\n" + url);
/*     */       } 
/* 495 */       if (code == 406)
/*     */       {
/* 497 */         throw new TwitterException.E406(String.valueOf(error) + "\n" + url); } 
/* 498 */       if (code == 413)
/* 499 */         throw new TwitterException.E413(String.valueOf(error) + "\n" + url); 
/* 500 */       if (code == 416)
/* 501 */         throw new TwitterException.E416(String.valueOf(error) + "\n" + url); 
/* 502 */       if (code == 420)
/* 503 */         throw new TwitterException.TooManyLogins(String.valueOf(error) + "\n" + url); 
/* 504 */       if (code >= 500 && code < 600) {
/* 505 */         throw new TwitterException.E50X(String.valueOf(error) + "\n" + url);
/*     */       }
/*     */       
/* 508 */       processError2_rateLimit(connection, resource, code, error);
/*     */ 
/*     */       
/* 511 */       if (code > 299 && code < 400) {
/* 512 */         String locn = connection.getHeaderField("Location");
/* 513 */         throw new TwitterException(String.valueOf(code) + " " + error + " " + url + " -> " + locn);
/*     */       } 
/*     */ 
/*     */       
/* 517 */       throw new TwitterException(String.valueOf(code) + " " + error + " " + url);
/*     */     }
/* 519 */     catch (SocketTimeoutException e) {
/* 520 */       URL url = connection.getURL();
/* 521 */       throw new TwitterException.Timeout(String.valueOf(this.timeout) + "milli-secs for " + 
/* 522 */           url);
/* 523 */     } catch (ConnectException e) {
/*     */       
/* 525 */       URL url = connection.getURL();
/* 526 */       throw new TwitterException.Timeout(url.toString());
/* 527 */     } catch (SocketException e) {
/*     */ 
/*     */       
/* 530 */       throw new TwitterException.E50X(e.toString());
/* 531 */     } catch (IOException e) {
/* 532 */       throw new TwitterException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private String processError2_reason(HttpURLConnection connection) throws IOException {
/* 538 */     String errorPage = readErrorPage(connection);
/* 539 */     if (errorPage != null) {
/*     */       try {
/* 541 */         JSONObject je = new JSONObject(errorPage);
/* 542 */         Object object = je.get("errors");
/* 543 */         if (object instanceof JSONArray) {
/* 544 */           JSONObject err = ((JSONArray)object).getJSONObject(0);
/* 545 */           return "code " + err.get("code") + ": " + err.getString("message");
/* 546 */         }  if (object instanceof String) {
/* 547 */           return (String)object;
/*     */         }
/* 549 */       } catch (Exception exception) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 555 */     String error = connection.getResponseMessage();
/* 556 */     Map<String, List<String>> connHeaders = connection.getHeaderFields();
/* 557 */     List<String> errorMessage = connHeaders.get(null);
/* 558 */     if (errorMessage != null && !errorMessage.isEmpty()) {
/* 559 */       error = String.valueOf(error) + "\n" + (String)errorMessage.get(0);
/*     */     }
/* 561 */     if (errorPage != null && !errorPage.isEmpty()) {
/* 562 */       error = String.valueOf(error) + "\n" + errorPage;
/*     */     }
/* 564 */     return error;
/*     */   }
/*     */ 
/*     */   
/*     */   private void processError2_403(HttpURLConnection connection, String resource, URL url, String errorPage) {
/* 569 */     String _name = (this.name == null) ? "anon" : this.name;
/* 570 */     if (errorPage == null) {
/* 571 */       throw new TwitterException.E403(url + " (" + _name + ")");
/*     */     }
/*     */     
/* 574 */     if (errorPage.startsWith("code 185") || errorPage.contains("Wow, that's a lot of Twittering!")) {
/*     */       
/* 576 */       processHeaders(connection, resource);
/* 577 */       throw new TwitterException.RateLimit(errorPage);
/*     */     } 
/* 579 */     if (errorPage.contains("too old")) {
/* 580 */       throw new TwitterException.BadParameter(String.valueOf(errorPage) + "\n" + url);
/*     */     }
/* 582 */     if (errorPage.contains("suspended")) {
/* 583 */       throw new TwitterException.SuspendedUser(String.valueOf(errorPage) + "\n" + url);
/*     */     }
/*     */     
/* 586 */     if (errorPage.contains("Could not find"))
/* 587 */       throw new TwitterException.SuspendedUser(String.valueOf(errorPage) + "\n" + url); 
/* 588 */     if (errorPage.contains("too recent"))
/* 589 */       throw new TwitterException.TooRecent(String.valueOf(errorPage) + "\n" + url); 
/* 590 */     if (errorPage.contains("already requested to follow"))
/* 591 */       throw new TwitterException.Repetition(String.valueOf(errorPage) + "\n" + url); 
/* 592 */     if (errorPage.contains("duplicate"))
/* 593 */       throw new TwitterException.Repetition(errorPage); 
/* 594 */     if (errorPage.contains("unable to follow more people"))
/* 595 */       throw new TwitterException.FollowerLimit(String.valueOf(this.name) + " " + errorPage); 
/* 596 */     if (errorPage.contains("application is not allowed to access"))
/* 597 */       throw new TwitterException.AccessLevel(String.valueOf(this.name) + " " + errorPage); 
/* 598 */     throw new TwitterException.E403(String.valueOf(errorPage) + "\n" + url + " (" + _name + ")");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void processError2_rateLimit(HttpURLConnection connection, String resource, int code, String error) {
/* 604 */     boolean rateLimitExceeded = error.contains("Rate limit exceeded");
/* 605 */     if (rateLimitExceeded) {
/*     */       
/* 607 */       processHeaders(connection, resource);
/* 608 */       throw new TwitterException.RateLimit(String.valueOf(getName()) + ": " + error);
/*     */     } 
/*     */     
/* 611 */     if (code == 400) {
/*     */       try {
/* 613 */         String json = getPage(
/* 614 */             "http://twitter.com/account/rate_limit_status.json", 
/* 615 */             null, (this.password != null));
/* 616 */         JSONObject obj = new JSONObject(json);
/* 617 */         int hits = obj.getInt("remaining_hits");
/* 618 */         if (hits < 1)
/* 619 */           throw new TwitterException.RateLimit(error); 
/* 620 */       } catch (Exception exception) {}
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
/*     */   protected final void processHeaders(HttpURLConnection connection, String resource) {
/* 632 */     this.headers = connection.getHeaderFields();
/* 633 */     updateRateLimits(resource);
/*     */   }
/*     */   
/*     */   static String readErrorPage(HttpURLConnection connection) {
/* 637 */     InputStream stream = connection.getErrorStream();
/* 638 */     if (stream == null) {
/* 639 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 643 */       if ("gzip".equals(connection.getHeaderField("Content-Encoding"))) {
/* 644 */         stream = new GZIPInputStream(stream);
/*     */       }
/* 646 */       BufferedReader reader = new BufferedReader(new InputStreamReader(
/* 647 */             stream));
/* 648 */       int bufSize = 8192;
/*     */       
/* 650 */       StringBuilder sb = new StringBuilder(8192);
/* 651 */       char[] cbuf = new char[8192];
/*     */       try {
/*     */         while (true)
/* 654 */         { int chars = reader.read(cbuf);
/* 655 */           if (chars == -1) {
/*     */             break;
/*     */           }
/* 658 */           sb.append(cbuf, 0, chars); } 
/* 659 */       } catch (IOException e) {
/*     */         
/* 661 */         if (sb.length() == 0) {
/* 662 */           return null;
/*     */         }
/* 664 */         return sb.toString();
/*     */       } 
/*     */       
/* 667 */       return sb.toString();
/* 668 */     } catch (IOException e) {
/*     */       
/* 670 */       return null;
/*     */     } finally {
/* 672 */       InternalUtils.close(stream);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setAuthentication(URLConnection connection, String name, String password) {
/* 681 */     if (name == null || password == null)
/*     */     {
/* 683 */       throw new TwitterException.E401("Authentication requested but no authorisation details are set!");
/*     */     }
/* 685 */     String token = String.valueOf(name) + ":" + password;
/* 686 */     String encoding = Base64Encoder.encode(token);
/*     */     
/* 688 */     encoding = encoding.replace("\r\n", "");
/* 689 */     connection.setRequestProperty("Authorization", "Basic " + encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMinRateLimit(int minRateLimit) {
/* 699 */     this.minRateLimit = minRateLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRetryOnError(boolean retryOnError) {
/* 709 */     this.retryOnError = retryOnError;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTimeout(int millisecs) {
/* 714 */     this.timeout = millisecs;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 719 */     return String.valueOf(getClass().getName()) + "[name=" + this.name + ", password=" + (
/* 720 */       (this.password == null) ? "null" : "XXX") + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void updateRateLimits(String resource) {
/* 727 */     if (resource == null)
/* 728 */       return;  String limit = getHeader("X-Rate-Limit-Limit");
/* 729 */     if (limit == null) {
/*     */       return;
/*     */     }
/* 732 */     String remaining = getHeader("X-Rate-Limit-Remaining");
/* 733 */     String reset = getHeader("X-Rate-Limit-Reset");
/* 734 */     this.rateLimits.put(resource, new RateLimit(limit, remaining, reset));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\URLConnectionHttpClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */