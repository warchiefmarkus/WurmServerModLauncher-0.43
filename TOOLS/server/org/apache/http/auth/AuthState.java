/*     */ package org.apache.http.auth;
/*     */ 
/*     */ import java.util.Queue;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class AuthState
/*     */ {
/*  58 */   private AuthProtocolState state = AuthProtocolState.UNCHALLENGED;
/*     */   
/*     */   private AuthScheme authScheme;
/*     */   
/*     */   private AuthScope authScope;
/*     */   private Credentials credentials;
/*     */   private Queue<AuthOption> authOptions;
/*     */   
/*     */   public void reset() {
/*  67 */     this.state = AuthProtocolState.UNCHALLENGED;
/*  68 */     this.authOptions = null;
/*  69 */     this.authScheme = null;
/*  70 */     this.authScope = null;
/*  71 */     this.credentials = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthProtocolState getState() {
/*  78 */     return this.state;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setState(AuthProtocolState state) {
/*  85 */     this.state = (state != null) ? state : AuthProtocolState.UNCHALLENGED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthScheme getAuthScheme() {
/*  92 */     return this.authScheme;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Credentials getCredentials() {
/*  99 */     return this.credentials;
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
/*     */   public void update(AuthScheme authScheme, Credentials credentials) {
/* 111 */     if (authScheme == null) {
/* 112 */       throw new IllegalArgumentException("Auth scheme may not be null or empty");
/*     */     }
/* 114 */     if (credentials == null) {
/* 115 */       throw new IllegalArgumentException("Credentials may not be null or empty");
/*     */     }
/* 117 */     this.authScheme = authScheme;
/* 118 */     this.credentials = credentials;
/* 119 */     this.authOptions = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Queue<AuthOption> getAuthOptions() {
/* 128 */     return this.authOptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAuthOptions() {
/* 138 */     return (this.authOptions != null && !this.authOptions.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(Queue<AuthOption> authOptions) {
/* 149 */     if (authOptions == null || authOptions.isEmpty()) {
/* 150 */       throw new IllegalArgumentException("Queue of auth options may not be null or empty");
/*     */     }
/* 152 */     this.authOptions = authOptions;
/* 153 */     this.authScheme = null;
/* 154 */     this.credentials = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void invalidate() {
/* 164 */     reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean isValid() {
/* 172 */     return (this.authScheme != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setAuthScheme(AuthScheme authScheme) {
/* 184 */     if (authScheme == null) {
/* 185 */       reset();
/*     */       return;
/*     */     } 
/* 188 */     this.authScheme = authScheme;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setCredentials(Credentials credentials) {
/* 200 */     this.credentials = credentials;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public AuthScope getAuthScope() {
/* 212 */     return this.authScope;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setAuthScope(AuthScope authScope) {
/* 224 */     this.authScope = authScope;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 229 */     StringBuilder buffer = new StringBuilder();
/* 230 */     buffer.append("state:").append(this.state).append(";");
/* 231 */     if (this.authScheme != null) {
/* 232 */       buffer.append("auth scheme:").append(this.authScheme.getSchemeName()).append(";");
/*     */     }
/* 234 */     if (this.credentials != null) {
/* 235 */       buffer.append("credentials present");
/*     */     }
/* 237 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\auth\AuthState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */