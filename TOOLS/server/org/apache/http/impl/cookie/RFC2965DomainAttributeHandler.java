/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.cookie.ClientCookie;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class RFC2965DomainAttributeHandler
/*     */   implements CookieAttributeHandler
/*     */ {
/*     */   public void parse(SetCookie cookie, String domain) throws MalformedCookieException {
/*  60 */     if (cookie == null) {
/*  61 */       throw new IllegalArgumentException("Cookie may not be null");
/*     */     }
/*  63 */     if (domain == null) {
/*  64 */       throw new MalformedCookieException("Missing value for domain attribute");
/*     */     }
/*     */     
/*  67 */     if (domain.trim().length() == 0) {
/*  68 */       throw new MalformedCookieException("Blank value for domain attribute");
/*     */     }
/*     */     
/*  71 */     domain = domain.toLowerCase(Locale.ENGLISH);
/*  72 */     if (!domain.startsWith("."))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  78 */       domain = '.' + domain;
/*     */     }
/*  80 */     cookie.setDomain(domain);
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
/*     */   
/*     */   public boolean domainMatch(String host, String domain) {
/*  99 */     boolean match = (host.equals(domain) || (domain.startsWith(".") && host.endsWith(domain)));
/*     */ 
/*     */     
/* 102 */     return match;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/* 110 */     if (cookie == null) {
/* 111 */       throw new IllegalArgumentException("Cookie may not be null");
/*     */     }
/* 113 */     if (origin == null) {
/* 114 */       throw new IllegalArgumentException("Cookie origin may not be null");
/*     */     }
/* 116 */     String host = origin.getHost().toLowerCase(Locale.ENGLISH);
/* 117 */     if (cookie.getDomain() == null) {
/* 118 */       throw new CookieRestrictionViolationException("Invalid cookie state: domain not specified");
/*     */     }
/*     */     
/* 121 */     String cookieDomain = cookie.getDomain().toLowerCase(Locale.ENGLISH);
/*     */     
/* 123 */     if (cookie instanceof ClientCookie && ((ClientCookie)cookie).containsAttribute("domain")) {
/*     */ 
/*     */       
/* 126 */       if (!cookieDomain.startsWith(".")) {
/* 127 */         throw new CookieRestrictionViolationException("Domain attribute \"" + cookie.getDomain() + "\" violates RFC 2109: domain must start with a dot");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 133 */       int dotIndex = cookieDomain.indexOf('.', 1);
/* 134 */       if ((dotIndex < 0 || dotIndex == cookieDomain.length() - 1) && !cookieDomain.equals(".local"))
/*     */       {
/* 136 */         throw new CookieRestrictionViolationException("Domain attribute \"" + cookie.getDomain() + "\" violates RFC 2965: the value contains no embedded dots " + "and the value is not .local");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 143 */       if (!domainMatch(host, cookieDomain)) {
/* 144 */         throw new CookieRestrictionViolationException("Domain attribute \"" + cookie.getDomain() + "\" violates RFC 2965: effective host name does not " + "domain-match domain attribute.");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 151 */       String effectiveHostWithoutDomain = host.substring(0, host.length() - cookieDomain.length());
/*     */       
/* 153 */       if (effectiveHostWithoutDomain.indexOf('.') != -1) {
/* 154 */         throw new CookieRestrictionViolationException("Domain attribute \"" + cookie.getDomain() + "\" violates RFC 2965: " + "effective host minus domain may not contain any dots");
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/* 161 */     else if (!cookie.getDomain().equals(host)) {
/* 162 */       throw new CookieRestrictionViolationException("Illegal domain attribute: \"" + cookie.getDomain() + "\"." + "Domain of origin: \"" + host + "\"");
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
/*     */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 174 */     if (cookie == null) {
/* 175 */       throw new IllegalArgumentException("Cookie may not be null");
/*     */     }
/* 177 */     if (origin == null) {
/* 178 */       throw new IllegalArgumentException("Cookie origin may not be null");
/*     */     }
/* 180 */     String host = origin.getHost().toLowerCase(Locale.ENGLISH);
/* 181 */     String cookieDomain = cookie.getDomain();
/*     */ 
/*     */ 
/*     */     
/* 185 */     if (!domainMatch(host, cookieDomain)) {
/* 186 */       return false;
/*     */     }
/*     */     
/* 189 */     String effectiveHostWithoutDomain = host.substring(0, host.length() - cookieDomain.length());
/*     */     
/* 191 */     return (effectiveHostWithoutDomain.indexOf('.') == -1);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\RFC2965DomainAttributeHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */