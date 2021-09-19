/*    */ package org.apache.http.auth;
/*    */ 
/*    */ import org.apache.http.annotation.Immutable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public final class AuthOption
/*    */ {
/*    */   private final AuthScheme authScheme;
/*    */   private final Credentials creds;
/*    */   
/*    */   public AuthOption(AuthScheme authScheme, Credentials creds) {
/* 42 */     if (authScheme == null) {
/* 43 */       throw new IllegalArgumentException("Auth scheme may not be null");
/*    */     }
/* 45 */     if (creds == null) {
/* 46 */       throw new IllegalArgumentException("User credentials may not be null");
/*    */     }
/* 48 */     this.authScheme = authScheme;
/* 49 */     this.creds = creds;
/*    */   }
/*    */   
/*    */   public AuthScheme getAuthScheme() {
/* 53 */     return this.authScheme;
/*    */   }
/*    */   
/*    */   public Credentials getCredentials() {
/* 57 */     return this.creds;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 62 */     return this.authScheme.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\auth\AuthOption.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */