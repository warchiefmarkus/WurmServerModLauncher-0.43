/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.auth.AuthOption;
/*     */ import org.apache.http.auth.AuthScheme;
/*     */ import org.apache.http.auth.AuthScope;
/*     */ import org.apache.http.auth.AuthenticationException;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.MalformedChallengeException;
/*     */ import org.apache.http.client.AuthCache;
/*     */ import org.apache.http.client.AuthenticationHandler;
/*     */ import org.apache.http.client.AuthenticationStrategy;
/*     */ import org.apache.http.client.CredentialsProvider;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @Immutable
/*     */ class AuthenticationStrategyAdaptor
/*     */   implements AuthenticationStrategy
/*     */ {
/*  62 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */   private final AuthenticationHandler handler;
/*     */ 
/*     */   
/*     */   public AuthenticationStrategyAdaptor(AuthenticationHandler handler) {
/*  68 */     this.handler = handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAuthenticationRequested(HttpHost authhost, HttpResponse response, HttpContext context) {
/*  75 */     return this.handler.isAuthenticationRequested(response, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Header> getChallenges(HttpHost authhost, HttpResponse response, HttpContext context) throws MalformedChallengeException {
/*  82 */     return this.handler.getChallenges(response, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Queue<AuthOption> select(Map<String, Header> challenges, HttpHost authhost, HttpResponse response, HttpContext context) throws MalformedChallengeException {
/*     */     AuthScheme authScheme;
/*  90 */     if (challenges == null) {
/*  91 */       throw new IllegalArgumentException("Map of auth challenges may not be null");
/*     */     }
/*  93 */     if (authhost == null) {
/*  94 */       throw new IllegalArgumentException("Host may not be null");
/*     */     }
/*  96 */     if (response == null) {
/*  97 */       throw new IllegalArgumentException("HTTP response may not be null");
/*     */     }
/*  99 */     if (context == null) {
/* 100 */       throw new IllegalArgumentException("HTTP context may not be null");
/*     */     }
/*     */     
/* 103 */     Queue<AuthOption> options = new LinkedList<AuthOption>();
/* 104 */     CredentialsProvider credsProvider = (CredentialsProvider)context.getAttribute("http.auth.credentials-provider");
/*     */     
/* 106 */     if (credsProvider == null) {
/* 107 */       this.log.debug("Credentials provider not set in the context");
/* 108 */       return options;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 113 */       authScheme = this.handler.selectScheme(challenges, response, context);
/* 114 */     } catch (AuthenticationException ex) {
/* 115 */       if (this.log.isWarnEnabled()) {
/* 116 */         this.log.warn(ex.getMessage(), (Throwable)ex);
/*     */       }
/* 118 */       return options;
/*     */     } 
/* 120 */     String id = authScheme.getSchemeName();
/* 121 */     Header challenge = challenges.get(id.toLowerCase(Locale.US));
/* 122 */     authScheme.processChallenge(challenge);
/*     */     
/* 124 */     AuthScope authScope = new AuthScope(authhost.getHostName(), authhost.getPort(), authScheme.getRealm(), authScheme.getSchemeName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     Credentials credentials = credsProvider.getCredentials(authScope);
/* 131 */     if (credentials != null) {
/* 132 */       options.add(new AuthOption(authScheme, credentials));
/*     */     }
/* 134 */     return options;
/*     */   }
/*     */ 
/*     */   
/*     */   public void authSucceeded(HttpHost authhost, AuthScheme authScheme, HttpContext context) {
/* 139 */     AuthCache authCache = (AuthCache)context.getAttribute("http.auth.auth-cache");
/* 140 */     if (isCachable(authScheme)) {
/* 141 */       if (authCache == null) {
/* 142 */         authCache = new BasicAuthCache();
/* 143 */         context.setAttribute("http.auth.auth-cache", authCache);
/*     */       } 
/* 145 */       if (this.log.isDebugEnabled()) {
/* 146 */         this.log.debug("Caching '" + authScheme.getSchemeName() + "' auth scheme for " + authhost);
/*     */       }
/*     */       
/* 149 */       authCache.put(authhost, authScheme);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void authFailed(HttpHost authhost, AuthScheme authScheme, HttpContext context) {
/* 155 */     AuthCache authCache = (AuthCache)context.getAttribute("http.auth.auth-cache");
/* 156 */     if (authCache == null) {
/*     */       return;
/*     */     }
/* 159 */     if (this.log.isDebugEnabled()) {
/* 160 */       this.log.debug("Removing from cache '" + authScheme.getSchemeName() + "' auth scheme for " + authhost);
/*     */     }
/*     */     
/* 163 */     authCache.remove(authhost);
/*     */   }
/*     */   
/*     */   private boolean isCachable(AuthScheme authScheme) {
/* 167 */     if (authScheme == null || !authScheme.isComplete()) {
/* 168 */       return false;
/*     */     }
/* 170 */     String schemeName = authScheme.getSchemeName();
/* 171 */     return (schemeName.equalsIgnoreCase("Basic") || schemeName.equalsIgnoreCase("Digest"));
/*     */   }
/*     */ 
/*     */   
/*     */   public AuthenticationHandler getHandler() {
/* 176 */     return this.handler;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\AuthenticationStrategyAdaptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */