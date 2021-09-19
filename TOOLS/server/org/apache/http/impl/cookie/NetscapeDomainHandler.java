/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.CookieRestrictionViolationException;
/*     */ import org.apache.http.cookie.MalformedCookieException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class NetscapeDomainHandler
/*     */   extends BasicDomainHandler
/*     */ {
/*     */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/*  53 */     super.validate(cookie, origin);
/*     */     
/*  55 */     String host = origin.getHost();
/*  56 */     String domain = cookie.getDomain();
/*  57 */     if (host.contains(".")) {
/*  58 */       int domainParts = (new StringTokenizer(domain, ".")).countTokens();
/*     */       
/*  60 */       if (isSpecialDomain(domain)) {
/*  61 */         if (domainParts < 2) {
/*  62 */           throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" violates the Netscape cookie specification for " + "special domains");
/*     */ 
/*     */         
/*     */         }
/*     */       
/*     */       }
/*  68 */       else if (domainParts < 3) {
/*  69 */         throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" violates the Netscape cookie specification");
/*     */       } 
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
/*     */   
/*     */   private static boolean isSpecialDomain(String domain) {
/*  84 */     String ucDomain = domain.toUpperCase(Locale.ENGLISH);
/*  85 */     return (ucDomain.endsWith(".COM") || ucDomain.endsWith(".EDU") || ucDomain.endsWith(".NET") || ucDomain.endsWith(".GOV") || ucDomain.endsWith(".MIL") || ucDomain.endsWith(".ORG") || ucDomain.endsWith(".INT"));
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
/*  96 */     if (cookie == null) {
/*  97 */       throw new IllegalArgumentException("Cookie may not be null");
/*     */     }
/*  99 */     if (origin == null) {
/* 100 */       throw new IllegalArgumentException("Cookie origin may not be null");
/*     */     }
/* 102 */     String host = origin.getHost();
/* 103 */     String domain = cookie.getDomain();
/* 104 */     if (domain == null) {
/* 105 */       return false;
/*     */     }
/* 107 */     return host.endsWith(domain);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\NetscapeDomainHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */