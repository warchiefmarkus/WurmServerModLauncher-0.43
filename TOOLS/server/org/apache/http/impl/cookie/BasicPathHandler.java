/*    */ package org.apache.http.impl.cookie;
/*    */ 
/*    */ import org.apache.http.annotation.Immutable;
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
/*    */ @Immutable
/*    */ public class BasicPathHandler
/*    */   implements CookieAttributeHandler
/*    */ {
/*    */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/* 51 */     if (cookie == null) {
/* 52 */       throw new IllegalArgumentException("Cookie may not be null");
/*    */     }
/* 54 */     if (value == null || value.trim().length() == 0) {
/* 55 */       value = "/";
/*    */     }
/* 57 */     cookie.setPath(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/* 62 */     if (!match(cookie, origin)) {
/* 63 */       throw new CookieRestrictionViolationException("Illegal path attribute \"" + cookie.getPath() + "\". Path of origin: \"" + origin.getPath() + "\"");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 70 */     if (cookie == null) {
/* 71 */       throw new IllegalArgumentException("Cookie may not be null");
/*    */     }
/* 73 */     if (origin == null) {
/* 74 */       throw new IllegalArgumentException("Cookie origin may not be null");
/*    */     }
/* 76 */     String targetpath = origin.getPath();
/* 77 */     String topmostPath = cookie.getPath();
/* 78 */     if (topmostPath == null) {
/* 79 */       topmostPath = "/";
/*    */     }
/* 81 */     if (topmostPath.length() > 1 && topmostPath.endsWith("/")) {
/* 82 */       topmostPath = topmostPath.substring(0, topmostPath.length() - 1);
/*    */     }
/* 84 */     boolean match = targetpath.startsWith(topmostPath);
/*    */ 
/*    */     
/* 87 */     if (match && targetpath.length() != topmostPath.length() && 
/* 88 */       !topmostPath.endsWith("/")) {
/* 89 */       match = (targetpath.charAt(topmostPath.length()) == '/');
/*    */     }
/*    */     
/* 92 */     return match;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\BasicPathHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */