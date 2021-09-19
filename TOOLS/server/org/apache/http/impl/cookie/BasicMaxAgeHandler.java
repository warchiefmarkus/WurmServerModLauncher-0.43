/*    */ package org.apache.http.impl.cookie;
/*    */ 
/*    */ import java.util.Date;
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
/*    */ @Immutable
/*    */ public class BasicMaxAgeHandler
/*    */   extends AbstractCookieAttributeHandler
/*    */ {
/*    */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/*    */     int age;
/* 49 */     if (cookie == null) {
/* 50 */       throw new IllegalArgumentException("Cookie may not be null");
/*    */     }
/* 52 */     if (value == null) {
/* 53 */       throw new MalformedCookieException("Missing value for max-age attribute");
/*    */     }
/*    */     
/*    */     try {
/* 57 */       age = Integer.parseInt(value);
/* 58 */     } catch (NumberFormatException e) {
/* 59 */       throw new MalformedCookieException("Invalid max-age attribute: " + value);
/*    */     } 
/*    */     
/* 62 */     if (age < 0) {
/* 63 */       throw new MalformedCookieException("Negative max-age attribute: " + value);
/*    */     }
/*    */     
/* 66 */     cookie.setExpiryDate(new Date(System.currentTimeMillis() + age * 1000L));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\BasicMaxAgeHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */