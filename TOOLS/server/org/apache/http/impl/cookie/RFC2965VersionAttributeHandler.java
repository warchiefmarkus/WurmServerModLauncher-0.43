/*    */ package org.apache.http.impl.cookie;
/*    */ 
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.cookie.ClientCookie;
/*    */ import org.apache.http.cookie.Cookie;
/*    */ import org.apache.http.cookie.CookieAttributeHandler;
/*    */ import org.apache.http.cookie.CookieOrigin;
/*    */ import org.apache.http.cookie.CookieRestrictionViolationException;
/*    */ import org.apache.http.cookie.MalformedCookieException;
/*    */ import org.apache.http.cookie.SetCookie;
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
/*    */ public class RFC2965VersionAttributeHandler
/*    */   implements CookieAttributeHandler
/*    */ {
/*    */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/* 58 */     if (cookie == null) {
/* 59 */       throw new IllegalArgumentException("Cookie may not be null");
/*    */     }
/* 61 */     if (value == null) {
/* 62 */       throw new MalformedCookieException("Missing value for version attribute");
/*    */     }
/*    */     
/* 65 */     int version = -1;
/*    */     try {
/* 67 */       version = Integer.parseInt(value);
/* 68 */     } catch (NumberFormatException e) {
/* 69 */       version = -1;
/*    */     } 
/* 71 */     if (version < 0) {
/* 72 */       throw new MalformedCookieException("Invalid cookie version.");
/*    */     }
/* 74 */     cookie.setVersion(version);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/* 82 */     if (cookie == null) {
/* 83 */       throw new IllegalArgumentException("Cookie may not be null");
/*    */     }
/* 85 */     if (cookie instanceof org.apache.http.cookie.SetCookie2 && 
/* 86 */       cookie instanceof ClientCookie && !((ClientCookie)cookie).containsAttribute("version"))
/*    */     {
/* 88 */       throw new CookieRestrictionViolationException("Violates RFC 2965. Version attribute is required.");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 95 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\RFC2965VersionAttributeHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */