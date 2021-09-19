/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.Locale;
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
/*     */ 
/*     */ @Immutable
/*     */ public class RFC2109DomainHandler
/*     */   implements CookieAttributeHandler
/*     */ {
/*     */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/*  53 */     if (cookie == null) {
/*  54 */       throw new IllegalArgumentException("Cookie may not be null");
/*     */     }
/*  56 */     if (value == null) {
/*  57 */       throw new MalformedCookieException("Missing value for domain attribute");
/*     */     }
/*  59 */     if (value.trim().length() == 0) {
/*  60 */       throw new MalformedCookieException("Blank value for domain attribute");
/*     */     }
/*  62 */     cookie.setDomain(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/*  67 */     if (cookie == null) {
/*  68 */       throw new IllegalArgumentException("Cookie may not be null");
/*     */     }
/*  70 */     if (origin == null) {
/*  71 */       throw new IllegalArgumentException("Cookie origin may not be null");
/*     */     }
/*  73 */     String host = origin.getHost();
/*  74 */     String domain = cookie.getDomain();
/*  75 */     if (domain == null) {
/*  76 */       throw new CookieRestrictionViolationException("Cookie domain may not be null");
/*     */     }
/*  78 */     if (!domain.equals(host)) {
/*  79 */       int dotIndex = domain.indexOf('.');
/*  80 */       if (dotIndex == -1) {
/*  81 */         throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" does not match the host \"" + host + "\"");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  87 */       if (!domain.startsWith(".")) {
/*  88 */         throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" violates RFC 2109: domain must start with a dot");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  93 */       dotIndex = domain.indexOf('.', 1);
/*  94 */       if (dotIndex < 0 || dotIndex == domain.length() - 1) {
/*  95 */         throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" violates RFC 2109: domain must contain an embedded dot");
/*     */       }
/*     */ 
/*     */       
/*  99 */       host = host.toLowerCase(Locale.ENGLISH);
/* 100 */       if (!host.endsWith(domain)) {
/* 101 */         throw new CookieRestrictionViolationException("Illegal domain attribute \"" + domain + "\". Domain of origin: \"" + host + "\"");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 106 */       String hostWithoutDomain = host.substring(0, host.length() - domain.length());
/* 107 */       if (hostWithoutDomain.indexOf('.') != -1) {
/* 108 */         throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" violates RFC 2109: host minus domain may not contain any dots");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 116 */     if (cookie == null) {
/* 117 */       throw new IllegalArgumentException("Cookie may not be null");
/*     */     }
/* 119 */     if (origin == null) {
/* 120 */       throw new IllegalArgumentException("Cookie origin may not be null");
/*     */     }
/* 122 */     String host = origin.getHost();
/* 123 */     String domain = cookie.getDomain();
/* 124 */     if (domain == null) {
/* 125 */       return false;
/*     */     }
/* 127 */     return (host.equals(domain) || (domain.startsWith(".") && host.endsWith(domain)));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\RFC2109DomainHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */