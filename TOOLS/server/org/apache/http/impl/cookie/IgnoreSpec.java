/*    */ package org.apache.http.impl.cookie;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.cookie.Cookie;
/*    */ import org.apache.http.cookie.CookieOrigin;
/*    */ import org.apache.http.cookie.MalformedCookieException;
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
/*    */ @NotThreadSafe
/*    */ public class IgnoreSpec
/*    */   extends CookieSpecBase
/*    */ {
/*    */   public int getVersion() {
/* 48 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
/* 53 */     return Collections.emptyList();
/*    */   }
/*    */   
/*    */   public List<Header> formatCookies(List<Cookie> cookies) {
/* 57 */     return Collections.emptyList();
/*    */   }
/*    */   
/*    */   public Header getVersionHeader() {
/* 61 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\IgnoreSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */