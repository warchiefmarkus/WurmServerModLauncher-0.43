/*     */ package org.apache.http.client.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseInterceptor;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.auth.AuthProtocolState;
/*     */ import org.apache.http.auth.AuthScheme;
/*     */ import org.apache.http.auth.AuthState;
/*     */ import org.apache.http.client.AuthCache;
/*     */ import org.apache.http.conn.scheme.Scheme;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
/*     */ import org.apache.http.impl.client.BasicAuthCache;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public class ResponseAuthCache
/*     */   implements HttpResponseInterceptor
/*     */ {
/*  64 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
/*     */     BasicAuthCache basicAuthCache;
/*  72 */     if (response == null) {
/*  73 */       throw new IllegalArgumentException("HTTP request may not be null");
/*     */     }
/*  75 */     if (context == null) {
/*  76 */       throw new IllegalArgumentException("HTTP context may not be null");
/*     */     }
/*  78 */     AuthCache authCache = (AuthCache)context.getAttribute("http.auth.auth-cache");
/*     */     
/*  80 */     HttpHost target = (HttpHost)context.getAttribute("http.target_host");
/*  81 */     AuthState targetState = (AuthState)context.getAttribute("http.auth.target-scope");
/*  82 */     if (target != null && targetState != null) {
/*  83 */       if (this.log.isDebugEnabled()) {
/*  84 */         this.log.debug("Target auth state: " + targetState.getState());
/*     */       }
/*  86 */       if (isCachable(targetState)) {
/*  87 */         SchemeRegistry schemeRegistry = (SchemeRegistry)context.getAttribute("http.scheme-registry");
/*     */         
/*  89 */         if (target.getPort() < 0) {
/*  90 */           Scheme scheme = schemeRegistry.getScheme(target);
/*  91 */           target = new HttpHost(target.getHostName(), scheme.resolvePort(target.getPort()), target.getSchemeName());
/*     */         } 
/*     */         
/*  94 */         if (authCache == null) {
/*  95 */           basicAuthCache = new BasicAuthCache();
/*  96 */           context.setAttribute("http.auth.auth-cache", basicAuthCache);
/*     */         } 
/*  98 */         switch (targetState.getState()) {
/*     */           case CHALLENGED:
/* 100 */             cache((AuthCache)basicAuthCache, target, targetState.getAuthScheme());
/*     */             break;
/*     */           case FAILURE:
/* 103 */             uncache((AuthCache)basicAuthCache, target, targetState.getAuthScheme());
/*     */             break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 108 */     HttpHost proxy = (HttpHost)context.getAttribute("http.proxy_host");
/* 109 */     AuthState proxyState = (AuthState)context.getAttribute("http.auth.proxy-scope");
/* 110 */     if (proxy != null && proxyState != null) {
/* 111 */       if (this.log.isDebugEnabled()) {
/* 112 */         this.log.debug("Proxy auth state: " + proxyState.getState());
/*     */       }
/* 114 */       if (isCachable(proxyState)) {
/* 115 */         if (basicAuthCache == null) {
/* 116 */           basicAuthCache = new BasicAuthCache();
/* 117 */           context.setAttribute("http.auth.auth-cache", basicAuthCache);
/*     */         } 
/* 119 */         switch (proxyState.getState()) {
/*     */           case CHALLENGED:
/* 121 */             cache((AuthCache)basicAuthCache, proxy, proxyState.getAuthScheme());
/*     */             break;
/*     */           case FAILURE:
/* 124 */             uncache((AuthCache)basicAuthCache, proxy, proxyState.getAuthScheme());
/*     */             break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private boolean isCachable(AuthState authState) {
/* 131 */     AuthScheme authScheme = authState.getAuthScheme();
/* 132 */     if (authScheme == null || !authScheme.isComplete()) {
/* 133 */       return false;
/*     */     }
/* 135 */     String schemeName = authScheme.getSchemeName();
/* 136 */     return (schemeName.equalsIgnoreCase("Basic") || schemeName.equalsIgnoreCase("Digest"));
/*     */   }
/*     */ 
/*     */   
/*     */   private void cache(AuthCache authCache, HttpHost host, AuthScheme authScheme) {
/* 141 */     if (this.log.isDebugEnabled()) {
/* 142 */       this.log.debug("Caching '" + authScheme.getSchemeName() + "' auth scheme for " + host);
/*     */     }
/*     */     
/* 145 */     authCache.put(host, authScheme);
/*     */   }
/*     */   
/*     */   private void uncache(AuthCache authCache, HttpHost host, AuthScheme authScheme) {
/* 149 */     if (this.log.isDebugEnabled()) {
/* 150 */       this.log.debug("Removing from cache '" + authScheme.getSchemeName() + "' auth scheme for " + host);
/*     */     }
/*     */     
/* 153 */     authCache.remove(host);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\client\protocol\ResponseAuthCache.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */