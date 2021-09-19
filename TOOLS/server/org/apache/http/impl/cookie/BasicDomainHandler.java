/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieAttributeHandler;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.CookieRestrictionViolationException;
/*     */ import org.apache.http.cookie.MalformedCookieException;
/*     */ import org.apache.http.cookie.SetCookie;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class BasicDomainHandler
/*     */   implements CookieAttributeHandler
/*     */ {
/*     */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/*  51 */     if (cookie == null) {
/*  52 */       throw new IllegalArgumentException("Cookie may not be null");
/*     */     }
/*  54 */     if (value == null) {
/*  55 */       throw new MalformedCookieException("Missing value for domain attribute");
/*     */     }
/*  57 */     if (value.trim().length() == 0) {
/*  58 */       throw new MalformedCookieException("Blank value for domain attribute");
/*     */     }
/*  60 */     cookie.setDomain(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/*  65 */     if (cookie == null) {
/*  66 */       throw new IllegalArgumentException("Cookie may not be null");
/*     */     }
/*  68 */     if (origin == null) {
/*  69 */       throw new IllegalArgumentException("Cookie origin may not be null");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     String host = origin.getHost();
/*  77 */     String domain = cookie.getDomain();
/*  78 */     if (domain == null) {
/*  79 */       throw new CookieRestrictionViolationException("Cookie domain may not be null");
/*     */     }
/*  81 */     if (host.contains(".")) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  86 */       if (!host.endsWith(domain)) {
/*  87 */         if (domain.startsWith(".")) {
/*  88 */           domain = domain.substring(1, domain.length());
/*     */         }
/*  90 */         if (!host.equals(domain)) {
/*  91 */           throw new CookieRestrictionViolationException("Illegal domain attribute \"" + domain + "\". Domain of origin: \"" + host + "\"");
/*     */         
/*     */         }
/*     */       }
/*     */     
/*     */     }
/*  97 */     else if (!host.equals(domain)) {
/*  98 */       throw new CookieRestrictionViolationException("Illegal domain attribute \"" + domain + "\". Domain of origin: \"" + host + "\"");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 106 */     if (cookie == null) {
/* 107 */       throw new IllegalArgumentException("Cookie may not be null");
/*     */     }
/* 109 */     if (origin == null) {
/* 110 */       throw new IllegalArgumentException("Cookie origin may not be null");
/*     */     }
/* 112 */     String host = origin.getHost();
/* 113 */     String domain = cookie.getDomain();
/* 114 */     if (domain == null) {
/* 115 */       return false;
/*     */     }
/* 117 */     if (host.equals(domain)) {
/* 118 */       return true;
/*     */     }
/* 120 */     if (!domain.startsWith(".")) {
/* 121 */       domain = '.' + domain;
/*     */     }
/* 123 */     return (host.endsWith(domain) || host.equals(domain.substring(1)));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\BasicDomainHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */