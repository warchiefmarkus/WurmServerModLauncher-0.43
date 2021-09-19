/*     */ package org.apache.http.auth;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.Principal;
/*     */ import java.util.Locale;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class NTCredentials
/*     */   implements Credentials, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7385699315228907265L;
/*     */   private final NTUserPrincipal principal;
/*     */   private final String password;
/*     */   private final String workstation;
/*     */   
/*     */   public NTCredentials(String usernamePassword) {
/*     */     String username;
/*  65 */     if (usernamePassword == null) {
/*  66 */       throw new IllegalArgumentException("Username:password string may not be null");
/*     */     }
/*     */     
/*  69 */     int atColon = usernamePassword.indexOf(':');
/*  70 */     if (atColon >= 0) {
/*  71 */       username = usernamePassword.substring(0, atColon);
/*  72 */       this.password = usernamePassword.substring(atColon + 1);
/*     */     } else {
/*  74 */       username = usernamePassword;
/*  75 */       this.password = null;
/*     */     } 
/*  77 */     int atSlash = username.indexOf('/');
/*  78 */     if (atSlash >= 0) {
/*  79 */       this.principal = new NTUserPrincipal(username.substring(0, atSlash).toUpperCase(Locale.ENGLISH), username.substring(atSlash + 1));
/*     */     }
/*     */     else {
/*     */       
/*  83 */       this.principal = new NTUserPrincipal(null, username.substring(atSlash + 1));
/*     */     } 
/*     */ 
/*     */     
/*  87 */     this.workstation = null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NTCredentials(String userName, String password, String workstation, String domain) {
/* 105 */     if (userName == null) {
/* 106 */       throw new IllegalArgumentException("User name may not be null");
/*     */     }
/* 108 */     this.principal = new NTUserPrincipal(domain, userName);
/* 109 */     this.password = password;
/* 110 */     if (workstation != null) {
/* 111 */       this.workstation = workstation.toUpperCase(Locale.ENGLISH);
/*     */     } else {
/* 113 */       this.workstation = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Principal getUserPrincipal() {
/* 118 */     return this.principal;
/*     */   }
/*     */   
/*     */   public String getUserName() {
/* 122 */     return this.principal.getUsername();
/*     */   }
/*     */   
/*     */   public String getPassword() {
/* 126 */     return this.password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDomain() {
/* 135 */     return this.principal.getDomain();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getWorkstation() {
/* 144 */     return this.workstation;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 149 */     int hash = 17;
/* 150 */     hash = LangUtils.hashCode(hash, this.principal);
/* 151 */     hash = LangUtils.hashCode(hash, this.workstation);
/* 152 */     return hash;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 157 */     if (this == o) return true; 
/* 158 */     if (o instanceof NTCredentials) {
/* 159 */       NTCredentials that = (NTCredentials)o;
/* 160 */       if (LangUtils.equals(this.principal, that.principal) && LangUtils.equals(this.workstation, that.workstation))
/*     */       {
/* 162 */         return true;
/*     */       }
/*     */     } 
/* 165 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 170 */     StringBuilder buffer = new StringBuilder();
/* 171 */     buffer.append("[principal: ");
/* 172 */     buffer.append(this.principal);
/* 173 */     buffer.append("][workstation: ");
/* 174 */     buffer.append(this.workstation);
/* 175 */     buffer.append("]");
/* 176 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\auth\NTCredentials.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */