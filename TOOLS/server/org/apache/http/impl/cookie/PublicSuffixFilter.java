/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.http.client.utils.Punycode;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieAttributeHandler;
/*     */ import org.apache.http.cookie.CookieOrigin;
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
/*     */ public class PublicSuffixFilter
/*     */   implements CookieAttributeHandler
/*     */ {
/*     */   private final CookieAttributeHandler wrapped;
/*     */   private Set<String> exceptions;
/*     */   private Set<String> suffixes;
/*     */   
/*     */   public PublicSuffixFilter(CookieAttributeHandler wrapped) {
/*  61 */     this.wrapped = wrapped;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPublicSuffixes(Collection<String> suffixes) {
/*  71 */     this.suffixes = new HashSet<String>(suffixes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExceptions(Collection<String> exceptions) {
/*  80 */     this.exceptions = new HashSet<String>(exceptions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean match(Cookie cookie, CookieOrigin origin) {
/*  87 */     if (isForPublicSuffix(cookie)) return false; 
/*  88 */     return this.wrapped.match(cookie, origin);
/*     */   }
/*     */   
/*     */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/*  92 */     this.wrapped.parse(cookie, value);
/*     */   }
/*     */   
/*     */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/*  96 */     this.wrapped.validate(cookie, origin);
/*     */   }
/*     */   
/*     */   private boolean isForPublicSuffix(Cookie cookie) {
/* 100 */     String domain = cookie.getDomain();
/* 101 */     if (domain.startsWith(".")) domain = domain.substring(1); 
/* 102 */     domain = Punycode.toUnicode(domain);
/*     */ 
/*     */     
/* 105 */     if (this.exceptions != null && 
/* 106 */       this.exceptions.contains(domain)) return false;
/*     */ 
/*     */ 
/*     */     
/* 110 */     if (this.suffixes == null) return false;
/*     */     
/*     */     do {
/* 113 */       if (this.suffixes.contains(domain)) return true;
/*     */       
/* 115 */       if (domain.startsWith("*.")) domain = domain.substring(2); 
/* 116 */       int nextdot = domain.indexOf('.');
/* 117 */       if (nextdot == -1)
/* 118 */         break;  domain = "*" + domain.substring(nextdot);
/* 119 */     } while (domain.length() > 0);
/*     */     
/* 121 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\PublicSuffixFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */