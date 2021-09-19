/*    */ package org.apache.http.impl.cookie;
/*    */ 
/*    */ import org.apache.http.annotation.Immutable;
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
/*    */ @Immutable
/*    */ class BrowserCompatVersionAttributeHandler
/*    */   extends AbstractCookieAttributeHandler
/*    */ {
/*    */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/* 52 */     if (cookie == null) {
/* 53 */       throw new IllegalArgumentException("Cookie may not be null");
/*    */     }
/* 55 */     if (value == null) {
/* 56 */       throw new MalformedCookieException("Missing value for version attribute");
/*    */     }
/* 58 */     int version = 0;
/*    */     try {
/* 60 */       version = Integer.parseInt(value);
/* 61 */     } catch (NumberFormatException e) {}
/*    */ 
/*    */     
/* 64 */     cookie.setVersion(version);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\BrowserCompatVersionAttributeHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */