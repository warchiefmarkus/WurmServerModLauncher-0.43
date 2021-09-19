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
/*     */ @Immutable
/*     */ public class NTUserPrincipal
/*     */   implements Principal, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -6870169797924406894L;
/*     */   private final String username;
/*     */   private final String domain;
/*     */   private final String ntname;
/*     */   
/*     */   public NTUserPrincipal(String domain, String username) {
/*  55 */     if (username == null) {
/*  56 */       throw new IllegalArgumentException("User name may not be null");
/*     */     }
/*  58 */     this.username = username;
/*  59 */     if (domain != null) {
/*  60 */       this.domain = domain.toUpperCase(Locale.ENGLISH);
/*     */     } else {
/*  62 */       this.domain = null;
/*     */     } 
/*  64 */     if (this.domain != null && this.domain.length() > 0) {
/*  65 */       StringBuilder buffer = new StringBuilder();
/*  66 */       buffer.append(this.domain);
/*  67 */       buffer.append('\\');
/*  68 */       buffer.append(this.username);
/*  69 */       this.ntname = buffer.toString();
/*     */     } else {
/*  71 */       this.ntname = this.username;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getName() {
/*  76 */     return this.ntname;
/*     */   }
/*     */   
/*     */   public String getDomain() {
/*  80 */     return this.domain;
/*     */   }
/*     */   
/*     */   public String getUsername() {
/*  84 */     return this.username;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  89 */     int hash = 17;
/*  90 */     hash = LangUtils.hashCode(hash, this.username);
/*  91 */     hash = LangUtils.hashCode(hash, this.domain);
/*  92 */     return hash;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  97 */     if (this == o) return true; 
/*  98 */     if (o instanceof NTUserPrincipal) {
/*  99 */       NTUserPrincipal that = (NTUserPrincipal)o;
/* 100 */       if (LangUtils.equals(this.username, that.username) && LangUtils.equals(this.domain, that.domain))
/*     */       {
/* 102 */         return true;
/*     */       }
/*     */     } 
/* 105 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 110 */     return this.ntname;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\auth\NTUserPrincipal.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */