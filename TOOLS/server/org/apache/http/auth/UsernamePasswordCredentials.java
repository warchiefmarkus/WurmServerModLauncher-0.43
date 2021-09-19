/*     */ package org.apache.http.auth;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.Principal;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.util.LangUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class UsernamePasswordCredentials
/*     */   implements Credentials, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 243343858802739403L;
/*     */   private final BasicUserPrincipal principal;
/*     */   private final String password;
/*     */   
/*     */   public UsernamePasswordCredentials(String usernamePassword) {
/*  58 */     if (usernamePassword == null) {
/*  59 */       throw new IllegalArgumentException("Username:password string may not be null");
/*     */     }
/*  61 */     int atColon = usernamePassword.indexOf(':');
/*  62 */     if (atColon >= 0) {
/*  63 */       this.principal = new BasicUserPrincipal(usernamePassword.substring(0, atColon));
/*  64 */       this.password = usernamePassword.substring(atColon + 1);
/*     */     } else {
/*  66 */       this.principal = new BasicUserPrincipal(usernamePassword);
/*  67 */       this.password = null;
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
/*     */   public UsernamePasswordCredentials(String userName, String password) {
/*  80 */     if (userName == null) {
/*  81 */       throw new IllegalArgumentException("Username may not be null");
/*     */     }
/*  83 */     this.principal = new BasicUserPrincipal(userName);
/*  84 */     this.password = password;
/*     */   }
/*     */   
/*     */   public Principal getUserPrincipal() {
/*  88 */     return this.principal;
/*     */   }
/*     */   
/*     */   public String getUserName() {
/*  92 */     return this.principal.getName();
/*     */   }
/*     */   
/*     */   public String getPassword() {
/*  96 */     return this.password;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 101 */     return this.principal.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 106 */     if (this == o) return true; 
/* 107 */     if (o instanceof UsernamePasswordCredentials) {
/* 108 */       UsernamePasswordCredentials that = (UsernamePasswordCredentials)o;
/* 109 */       if (LangUtils.equals(this.principal, that.principal)) {
/* 110 */         return true;
/*     */       }
/*     */     } 
/* 113 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 118 */     return this.principal.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\auth\UsernamePasswordCredentials.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */