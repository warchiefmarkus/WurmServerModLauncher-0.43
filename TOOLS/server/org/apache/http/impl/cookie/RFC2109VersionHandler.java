/*    */ package org.apache.http.impl.cookie;
/*    */ 
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.cookie.Cookie;
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
/*    */ @Immutable
/*    */ public class RFC2109VersionHandler
/*    */   extends AbstractCookieAttributeHandler
/*    */ {
/*    */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/* 50 */     if (cookie == null) {
/* 51 */       throw new IllegalArgumentException("Cookie may not be null");
/*    */     }
/* 53 */     if (value == null) {
/* 54 */       throw new MalformedCookieException("Missing value for version attribute");
/*    */     }
/* 56 */     if (value.trim().length() == 0) {
/* 57 */       throw new MalformedCookieException("Blank value for version attribute");
/*    */     }
/*    */     try {
/* 60 */       cookie.setVersion(Integer.parseInt(value));
/* 61 */     } catch (NumberFormatException e) {
/* 62 */       throw new MalformedCookieException("Invalid version: " + e.getMessage());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/* 70 */     if (cookie == null) {
/* 71 */       throw new IllegalArgumentException("Cookie may not be null");
/*    */     }
/* 73 */     if (cookie.getVersion() < 0)
/* 74 */       throw new CookieRestrictionViolationException("Cookie version may not be negative"); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\RFC2109VersionHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */