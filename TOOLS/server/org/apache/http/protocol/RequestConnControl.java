/*    */ package org.apache.http.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.HttpRequestInterceptor;
/*    */ import org.apache.http.annotation.Immutable;
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
/*    */ public class RequestConnControl
/*    */   implements HttpRequestInterceptor
/*    */ {
/*    */   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
/* 54 */     if (request == null) {
/* 55 */       throw new IllegalArgumentException("HTTP request may not be null");
/*    */     }
/*    */     
/* 58 */     String method = request.getRequestLine().getMethod();
/* 59 */     if (method.equalsIgnoreCase("CONNECT")) {
/*    */       return;
/*    */     }
/*    */     
/* 63 */     if (!request.containsHeader("Connection"))
/*    */     {
/*    */       
/* 66 */       request.addHeader("Connection", "Keep-Alive");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\protocol\RequestConnControl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */