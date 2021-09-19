/*     */ package org.apache.http.client.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.auth.AuthProtocolState;
/*     */ import org.apache.http.auth.AuthScheme;
/*     */ import org.apache.http.auth.AuthScope;
/*     */ import org.apache.http.auth.AuthState;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.client.AuthCache;
/*     */ import org.apache.http.client.CredentialsProvider;
/*     */ import org.apache.http.conn.scheme.Scheme;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class RequestAuthCache
/*     */   implements HttpRequestInterceptor
/*     */ {
/*  61 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
/*  69 */     if (request == null) {
/*  70 */       throw new IllegalArgumentException("HTTP request may not be null");
/*     */     }
/*  72 */     if (context == null) {
/*  73 */       throw new IllegalArgumentException("HTTP context may not be null");
/*     */     }
/*     */     
/*  76 */     AuthCache authCache = (AuthCache)context.getAttribute("http.auth.auth-cache");
/*  77 */     if (authCache == null) {
/*  78 */       this.log.debug("Auth cache not set in the context");
/*     */       
/*     */       return;
/*     */     } 
/*  82 */     CredentialsProvider credsProvider = (CredentialsProvider)context.getAttribute("http.auth.credentials-provider");
/*     */     
/*  84 */     if (credsProvider == null) {
/*  85 */       this.log.debug("Credentials provider not set in the context");
/*     */       
/*     */       return;
/*     */     } 
/*  89 */     HttpHost target = (HttpHost)context.getAttribute("http.target_host");
/*  90 */     if (target.getPort() < 0) {
/*  91 */       SchemeRegistry schemeRegistry = (SchemeRegistry)context.getAttribute("http.scheme-registry");
/*     */       
/*  93 */       Scheme scheme = schemeRegistry.getScheme(target);
/*  94 */       target = new HttpHost(target.getHostName(), scheme.resolvePort(target.getPort()), target.getSchemeName());
/*     */     } 
/*     */ 
/*     */     
/*  98 */     AuthState targetState = (AuthState)context.getAttribute("http.auth.target-scope");
/*  99 */     if (target != null && targetState != null && targetState.getState() == AuthProtocolState.UNCHALLENGED) {
/* 100 */       AuthScheme authScheme = authCache.get(target);
/* 101 */       if (authScheme != null) {
/* 102 */         doPreemptiveAuth(target, authScheme, targetState, credsProvider);
/*     */       }
/*     */     } 
/*     */     
/* 106 */     HttpHost proxy = (HttpHost)context.getAttribute("http.proxy_host");
/* 107 */     AuthState proxyState = (AuthState)context.getAttribute("http.auth.proxy-scope");
/* 108 */     if (proxy != null && proxyState != null && proxyState.getState() == AuthProtocolState.UNCHALLENGED) {
/* 109 */       AuthScheme authScheme = authCache.get(proxy);
/* 110 */       if (authScheme != null) {
/* 111 */         doPreemptiveAuth(proxy, authScheme, proxyState, credsProvider);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doPreemptiveAuth(HttpHost host, AuthScheme authScheme, AuthState authState, CredentialsProvider credsProvider) {
/* 121 */     String schemeName = authScheme.getSchemeName();
/* 122 */     if (this.log.isDebugEnabled()) {
/* 123 */       this.log.debug("Re-using cached '" + schemeName + "' auth scheme for " + host);
/*     */     }
/*     */     
/* 126 */     AuthScope authScope = new AuthScope(host, AuthScope.ANY_REALM, schemeName);
/* 127 */     Credentials creds = credsProvider.getCredentials(authScope);
/*     */     
/* 129 */     if (creds != null) {
/* 130 */       if ("BASIC".equalsIgnoreCase(authScheme.getSchemeName())) {
/* 131 */         authState.setState(AuthProtocolState.CHALLENGED);
/*     */       } else {
/* 133 */         authState.setState(AuthProtocolState.SUCCESS);
/*     */       } 
/* 135 */       authState.update(authScheme, creds);
/*     */     } else {
/* 137 */       this.log.debug("No credentials for preemptive authentication");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\client\protocol\RequestAuthCache.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */