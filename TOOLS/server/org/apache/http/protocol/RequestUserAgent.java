/*    */ package org.apache.http.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.HttpRequestInterceptor;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.params.HttpProtocolParams;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/*    */ public class RequestUserAgent
/*    */   implements HttpRequestInterceptor
/*    */ {
/*    */   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
/* 59 */     if (request == null) {
/* 60 */       throw new IllegalArgumentException("HTTP request may not be null");
/*    */     }
/* 62 */     if (!request.containsHeader("User-Agent")) {
/* 63 */       String useragent = HttpProtocolParams.getUserAgent(request.getParams());
/* 64 */       if (useragent != null)
/* 65 */         request.addHeader("User-Agent", useragent); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\protocol\RequestUserAgent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */