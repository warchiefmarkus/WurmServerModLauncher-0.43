/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.FormattedHeader;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.auth.AuthOption;
/*     */ import org.apache.http.auth.AuthScheme;
/*     */ import org.apache.http.auth.AuthSchemeRegistry;
/*     */ import org.apache.http.auth.AuthScope;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.MalformedChallengeException;
/*     */ import org.apache.http.client.AuthCache;
/*     */ import org.apache.http.client.AuthenticationStrategy;
/*     */ import org.apache.http.client.CredentialsProvider;
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
/*     */ @Immutable
/*     */ class AuthenticationStrategyImpl
/*     */   implements AuthenticationStrategy
/*     */ {
/*  64 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*  66 */   private static final List<String> DEFAULT_SCHEME_PRIORITY = Collections.unmodifiableList(Arrays.asList(new String[] { "negotiate", "Kerberos", "NTLM", "Digest", "Basic" }));
/*     */ 
/*     */ 
/*     */   
/*     */   private final int challengeCode;
/*     */ 
/*     */   
/*     */   private final String headerName;
/*     */ 
/*     */   
/*     */   private final String prefParamName;
/*     */ 
/*     */ 
/*     */   
/*     */   AuthenticationStrategyImpl(int challengeCode, String headerName, String prefParamName) {
/*  81 */     this.challengeCode = challengeCode;
/*  82 */     this.headerName = headerName;
/*  83 */     this.prefParamName = prefParamName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAuthenticationRequested(HttpHost authhost, HttpResponse response, HttpContext context) {
/*  90 */     if (response == null) {
/*  91 */       throw new IllegalArgumentException("HTTP response may not be null");
/*     */     }
/*  93 */     int status = response.getStatusLine().getStatusCode();
/*  94 */     return (status == this.challengeCode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Header> getChallenges(HttpHost authhost, HttpResponse response, HttpContext context) throws MalformedChallengeException {
/* 101 */     if (response == null) {
/* 102 */       throw new IllegalArgumentException("HTTP response may not be null");
/*     */     }
/* 104 */     Header[] headers = response.getHeaders(this.headerName);
/* 105 */     Map<String, Header> map = new HashMap<String, Header>(headers.length);
/* 106 */     for (Header header : headers) {
/*     */       CharArrayBuffer buffer;
/*     */       int pos;
/* 109 */       if (header instanceof FormattedHeader) {
/* 110 */         buffer = ((FormattedHeader)header).getBuffer();
/* 111 */         pos = ((FormattedHeader)header).getValuePos();
/*     */       } else {
/* 113 */         String str = header.getValue();
/* 114 */         if (str == null) {
/* 115 */           throw new MalformedChallengeException("Header value is null");
/*     */         }
/* 117 */         buffer = new CharArrayBuffer(str.length());
/* 118 */         buffer.append(str);
/* 119 */         pos = 0;
/*     */       } 
/* 121 */       while (pos < buffer.length() && HTTP.isWhitespace(buffer.charAt(pos))) {
/* 122 */         pos++;
/*     */       }
/* 124 */       int beginIndex = pos;
/* 125 */       while (pos < buffer.length() && !HTTP.isWhitespace(buffer.charAt(pos))) {
/* 126 */         pos++;
/*     */       }
/* 128 */       int endIndex = pos;
/* 129 */       String s = buffer.substring(beginIndex, endIndex);
/* 130 */       map.put(s.toLowerCase(Locale.US), header);
/*     */     } 
/* 132 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Queue<AuthOption> select(Map<String, Header> challenges, HttpHost authhost, HttpResponse response, HttpContext context) throws MalformedChallengeException {
/* 140 */     if (challenges == null) {
/* 141 */       throw new IllegalArgumentException("Map of auth challenges may not be null");
/*     */     }
/* 143 */     if (authhost == null) {
/* 144 */       throw new IllegalArgumentException("Host may not be null");
/*     */     }
/* 146 */     if (response == null) {
/* 147 */       throw new IllegalArgumentException("HTTP response may not be null");
/*     */     }
/* 149 */     if (context == null) {
/* 150 */       throw new IllegalArgumentException("HTTP context may not be null");
/*     */     }
/*     */     
/* 153 */     Queue<AuthOption> options = new LinkedList<AuthOption>();
/* 154 */     AuthSchemeRegistry registry = (AuthSchemeRegistry)context.getAttribute("http.authscheme-registry");
/*     */     
/* 156 */     if (registry == null) {
/* 157 */       this.log.debug("Auth scheme registry not set in the context");
/* 158 */       return options;
/*     */     } 
/* 160 */     CredentialsProvider credsProvider = (CredentialsProvider)context.getAttribute("http.auth.credentials-provider");
/*     */     
/* 162 */     if (credsProvider == null) {
/* 163 */       this.log.debug("Credentials provider not set in the context");
/* 164 */       return options;
/*     */     } 
/*     */ 
/*     */     
/* 168 */     List<String> authPrefs = (List<String>)response.getParams().getParameter(this.prefParamName);
/* 169 */     if (authPrefs == null) {
/* 170 */       authPrefs = DEFAULT_SCHEME_PRIORITY;
/*     */     }
/* 172 */     if (this.log.isDebugEnabled()) {
/* 173 */       this.log.debug("Authentication schemes in the order of preference: " + authPrefs);
/*     */     }
/*     */     
/* 176 */     for (String id : authPrefs) {
/* 177 */       Header challenge = challenges.get(id.toLowerCase(Locale.US));
/* 178 */       if (challenge != null) {
/*     */         try {
/* 180 */           AuthScheme authScheme = registry.getAuthScheme(id, response.getParams());
/* 181 */           authScheme.processChallenge(challenge);
/*     */           
/* 183 */           AuthScope authScope = new AuthScope(authhost.getHostName(), authhost.getPort(), authScheme.getRealm(), authScheme.getSchemeName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 189 */           Credentials credentials = credsProvider.getCredentials(authScope);
/* 190 */           if (credentials != null) {
/* 191 */             options.add(new AuthOption(authScheme, credentials));
/*     */           }
/* 193 */         } catch (IllegalStateException e) {
/* 194 */           if (this.log.isWarnEnabled()) {
/* 195 */             this.log.warn("Authentication scheme " + id + " not supported");
/*     */           }
/*     */         } 
/*     */         continue;
/*     */       } 
/* 200 */       if (this.log.isDebugEnabled()) {
/* 201 */         this.log.debug("Challenge for " + id + " authentication scheme not available");
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 206 */     return options;
/*     */   }
/*     */ 
/*     */   
/*     */   public void authSucceeded(HttpHost authhost, AuthScheme authScheme, HttpContext context) {
/* 211 */     if (authhost == null) {
/* 212 */       throw new IllegalArgumentException("Host may not be null");
/*     */     }
/* 214 */     if (authScheme == null) {
/* 215 */       throw new IllegalArgumentException("Auth scheme may not be null");
/*     */     }
/* 217 */     if (context == null) {
/* 218 */       throw new IllegalArgumentException("HTTP context may not be null");
/*     */     }
/* 220 */     if (isCachable(authScheme)) {
/* 221 */       AuthCache authCache = (AuthCache)context.getAttribute("http.auth.auth-cache");
/* 222 */       if (authCache == null) {
/* 223 */         authCache = new BasicAuthCache();
/* 224 */         context.setAttribute("http.auth.auth-cache", authCache);
/*     */       } 
/* 226 */       if (this.log.isDebugEnabled()) {
/* 227 */         this.log.debug("Caching '" + authScheme.getSchemeName() + "' auth scheme for " + authhost);
/*     */       }
/*     */       
/* 230 */       authCache.put(authhost, authScheme);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean isCachable(AuthScheme authScheme) {
/* 235 */     if (authScheme == null || !authScheme.isComplete()) {
/* 236 */       return false;
/*     */     }
/* 238 */     String schemeName = authScheme.getSchemeName();
/* 239 */     return (schemeName.equalsIgnoreCase("Basic") || schemeName.equalsIgnoreCase("Digest"));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void authFailed(HttpHost authhost, AuthScheme authScheme, HttpContext context) {
/* 245 */     if (authhost == null) {
/* 246 */       throw new IllegalArgumentException("Host may not be null");
/*     */     }
/* 248 */     if (context == null) {
/* 249 */       throw new IllegalArgumentException("HTTP context may not be null");
/*     */     }
/* 251 */     AuthCache authCache = (AuthCache)context.getAttribute("http.auth.auth-cache");
/* 252 */     if (authCache != null) {
/* 253 */       if (this.log.isDebugEnabled()) {
/* 254 */         this.log.debug("Clearing cached auth scheme for " + authhost);
/*     */       }
/* 256 */       authCache.remove(authhost);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\AuthenticationStrategyImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */