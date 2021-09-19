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
/*    */ @Immutable
/*    */ public class BasicExpiresHandler
/*    */   extends AbstractCookieAttributeHandler
/*    */ {
/*    */   private final String[] datepatterns;
/*    */   
/*    */   public BasicExpiresHandler(String[] datepatterns) {
/* 46 */     if (datepatterns == null) {
/* 47 */       throw new IllegalArgumentException("Array of date patterns may not be null");
/*    */     }
/* 49 */     this.datepatterns = datepatterns;
/*    */   }
/*    */ 
/*    */   
/*    */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/* 54 */     if (cookie == null) {
/* 55 */       throw new IllegalArgumentException("Cookie may not be null");
/*    */     }
/* 57 */     if (value == null) {
/* 58 */       throw new MalformedCookieException("Missing value for expires attribute");
/*    */     }
/*    */     try {
/* 61 */       cookie.setExpiryDate(DateUtils.parseDate(value, this.datepatterns));
/* 62 */     } catch (DateParseException dpe) {
/* 63 */       throw new MalformedCookieException("Unable to parse expires attribute: " + value);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\BasicExpiresHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */