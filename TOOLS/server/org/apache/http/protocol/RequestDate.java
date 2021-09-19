/*    */ package org.apache.http.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.HttpRequestInterceptor;
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
/*    */ public class RequestDate
/*    */   implements HttpRequestInterceptor
/*    */ {
/* 48 */   private static final HttpDateGenerator DATE_GENERATOR = new HttpDateGenerator();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
/* 56 */     if (request == null) {
/* 57 */       throw new IllegalArgumentException("HTTP request may not be null.");
/*    */     }
/*    */     
/* 60 */     if (request instanceof org.apache.http.HttpEntityEnclosingRequest && !request.containsHeader("Date")) {
/*    */       
/* 62 */       String httpdate = DATE_GENERATOR.getCurrentDate();
/* 63 */       request.setHeader("Date", httpdate);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\protocol\RequestDate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */