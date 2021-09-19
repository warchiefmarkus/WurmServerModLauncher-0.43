/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.auth.AuthOption;
/*     */ import org.apache.http.auth.AuthProtocolState;
/*     */ import org.apache.http.auth.AuthScheme;
/*     */ import org.apache.http.auth.AuthState;
/*     */ import org.apache.http.auth.MalformedChallengeException;
/*     */ import org.apache.http.client.AuthenticationStrategy;
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
/*     */ public class HttpAuthenticator
/*     */ {
/*     */   private final Log log;
/*     */   
/*     */   public HttpAuthenticator(Log log) {
/*  53 */     this.log = (log != null) ? log : LogFactory.getLog(getClass());
/*     */   }
/*     */   
/*     */   public HttpAuthenticator() {
/*  57 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAuthenticationRequested(HttpHost host, HttpResponse response, AuthenticationStrategy authStrategy, AuthState authState, HttpContext context) {
/*  66 */     if (authStrategy.isAuthenticationRequested(host, response, context)) {
/*  67 */       if (authState.getState() == AuthProtocolState.SUCCESS) {
/*  68 */         authStrategy.authFailed(host, authState.getAuthScheme(), context);
/*     */       }
/*  70 */       this.log.debug("Authentication required");
/*  71 */       return true;
/*     */     } 
/*  73 */     switch (authState.getState()) {
/*     */       case CHALLENGED:
/*     */       case HANDSHAKE:
/*  76 */         this.log.debug("Authentication succeeded");
/*  77 */         authState.setState(AuthProtocolState.SUCCESS);
/*  78 */         authStrategy.authSucceeded(host, authState.getAuthScheme(), context);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case SUCCESS:
/*  85 */         return false;
/*     */     } 
/*     */     authState.setState(AuthProtocolState.UNCHALLENGED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean authenticate(HttpHost host, HttpResponse response, AuthenticationStrategy authStrategy, AuthState authState, HttpContext context) {
/*     */     try {
/*  96 */       if (this.log.isDebugEnabled()) {
/*  97 */         this.log.debug(host.toHostString() + " requested authentication");
/*     */       }
/*  99 */       Map<String, Header> challenges = authStrategy.getChallenges(host, response, context);
/* 100 */       if (challenges.isEmpty()) {
/* 101 */         this.log.debug("Response contains no authentication challenges");
/* 102 */         return false;
/*     */       } 
/*     */       
/* 105 */       AuthScheme authScheme = authState.getAuthScheme();
/* 106 */       switch (authState.getState()) {
/*     */         case FAILURE:
/* 108 */           return false;
/*     */         case SUCCESS:
/* 110 */           authState.reset();
/*     */           break;
/*     */         case CHALLENGED:
/*     */         case HANDSHAKE:
/* 114 */           if (authScheme == null) {
/* 115 */             this.log.debug("Auth scheme is null");
/* 116 */             authStrategy.authFailed(host, null, context);
/* 117 */             authState.reset();
/* 118 */             authState.setState(AuthProtocolState.FAILURE);
/* 119 */             return false;
/*     */           } 
/*     */         case UNCHALLENGED:
/* 122 */           if (authScheme != null) {
/* 123 */             String id = authScheme.getSchemeName();
/* 124 */             Header challenge = challenges.get(id.toLowerCase(Locale.US));
/* 125 */             if (challenge != null) {
/* 126 */               this.log.debug("Authorization challenge processed");
/* 127 */               authScheme.processChallenge(challenge);
/* 128 */               if (authScheme.isComplete()) {
/* 129 */                 this.log.debug("Authentication failed");
/* 130 */                 authStrategy.authFailed(host, authState.getAuthScheme(), context);
/* 131 */                 authState.reset();
/* 132 */                 authState.setState(AuthProtocolState.FAILURE);
/* 133 */                 return false;
/*     */               } 
/* 135 */               authState.setState(AuthProtocolState.HANDSHAKE);
/* 136 */               return true;
/*     */             } 
/*     */             
/* 139 */             authState.reset();
/*     */           } 
/*     */           break;
/*     */       } 
/*     */       
/* 144 */       Queue<AuthOption> authOptions = authStrategy.select(challenges, host, response, context);
/* 145 */       if (authOptions != null && !authOptions.isEmpty()) {
/* 146 */         if (this.log.isDebugEnabled()) {
/* 147 */           this.log.debug("Selected authentication options: " + authOptions);
/*     */         }
/* 149 */         authState.setState(AuthProtocolState.CHALLENGED);
/* 150 */         authState.update(authOptions);
/* 151 */         return true;
/*     */       } 
/* 153 */       return false;
/*     */     }
/* 155 */     catch (MalformedChallengeException ex) {
/* 156 */       if (this.log.isWarnEnabled()) {
/* 157 */         this.log.warn("Malformed challenge: " + ex.getMessage());
/*     */       }
/* 159 */       authState.reset();
/* 160 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\HttpAuthenticator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */