/*     */ package org.apache.http.client.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Queue;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.auth.AuthOption;
/*     */ import org.apache.http.auth.AuthProtocolState;
/*     */ import org.apache.http.auth.AuthScheme;
/*     */ import org.apache.http.auth.AuthState;
/*     */ import org.apache.http.auth.AuthenticationException;
/*     */ import org.apache.http.auth.ContextAwareAuthScheme;
/*     */ import org.apache.http.auth.Credentials;
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
/*     */ abstract class RequestAuthenticationBase
/*     */   implements HttpRequestInterceptor
/*     */ {
/*  49 */   final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void process(AuthState authState, HttpRequest request, HttpContext context) throws HttpException, IOException {
/*     */     Queue<AuthOption> authOptions;
/*  59 */     AuthScheme authScheme = authState.getAuthScheme();
/*  60 */     Credentials creds = authState.getCredentials();
/*  61 */     switch (authState.getState()) {
/*     */       case FAILURE:
/*     */         return;
/*     */       case SUCCESS:
/*  65 */         ensureAuthScheme(authScheme);
/*  66 */         if (authScheme.isConnectionBased()) {
/*     */           return;
/*     */         }
/*     */         break;
/*     */       case CHALLENGED:
/*  71 */         authOptions = authState.getAuthOptions();
/*  72 */         if (authOptions != null) {
/*  73 */           while (!authOptions.isEmpty()) {
/*  74 */             AuthOption authOption = authOptions.remove();
/*  75 */             authScheme = authOption.getAuthScheme();
/*  76 */             creds = authOption.getCredentials();
/*  77 */             authState.update(authScheme, creds);
/*  78 */             if (this.log.isDebugEnabled()) {
/*  79 */               this.log.debug("Generating response to an authentication challenge using " + authScheme.getSchemeName() + " scheme");
/*     */             }
/*     */             
/*     */             try {
/*  83 */               Header header = authenticate(authScheme, creds, request, context);
/*  84 */               request.addHeader(header);
/*     */               break;
/*  86 */             } catch (AuthenticationException ex) {
/*  87 */               if (this.log.isWarnEnabled()) {
/*  88 */                 this.log.warn(authScheme + " authentication error: " + ex.getMessage());
/*     */               }
/*     */             } 
/*     */           } 
/*     */           return;
/*     */         } 
/*  94 */         ensureAuthScheme(authScheme);
/*     */         break;
/*     */     } 
/*  97 */     if (authScheme != null) {
/*     */       try {
/*  99 */         Header header = authenticate(authScheme, creds, request, context);
/* 100 */         request.addHeader(header);
/* 101 */       } catch (AuthenticationException ex) {
/* 102 */         if (this.log.isErrorEnabled()) {
/* 103 */           this.log.error(authScheme + " authentication error: " + ex.getMessage());
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void ensureAuthScheme(AuthScheme authScheme) {
/* 110 */     if (authScheme == null) {
/* 111 */       throw new IllegalStateException("Auth scheme is not set");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Header authenticate(AuthScheme authScheme, Credentials creds, HttpRequest request, HttpContext context) throws AuthenticationException {
/* 121 */     if (authScheme == null) {
/* 122 */       throw new IllegalStateException("Auth state object is null");
/*     */     }
/* 124 */     if (authScheme instanceof ContextAwareAuthScheme) {
/* 125 */       return ((ContextAwareAuthScheme)authScheme).authenticate(creds, request, context);
/*     */     }
/* 127 */     return authScheme.authenticate(creds, request);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\client\protocol\RequestAuthenticationBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */