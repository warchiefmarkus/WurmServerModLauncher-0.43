/*    */ package org.apache.http.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.HttpResponseInterceptor;
/*    */ import org.apache.http.annotation.ThreadSafe;
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
/*    */ @ThreadSafe
/*    */ public class ResponseDate
/*    */   implements HttpResponseInterceptor
/*    */ {
/* 48 */   private static final HttpDateGenerator DATE_GENERATOR = new HttpDateGenerator();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
/* 56 */     if (response == null) {
/* 57 */       throw new IllegalArgumentException("HTTP response may not be null.");
/*    */     }
/*    */     
/* 60 */     int status = response.getStatusLine().getStatusCode();
/* 61 */     if (status >= 200 && !response.containsHeader("Date")) {
/*    */       
/* 63 */       String httpdate = DATE_GENERATOR.getCurrentDate();
/* 64 */       response.setHeader("Date", httpdate);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\protocol\ResponseDate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */