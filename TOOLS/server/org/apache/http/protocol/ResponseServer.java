/*    */ package org.apache.http.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.HttpResponseInterceptor;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class ResponseServer
/*    */   implements HttpResponseInterceptor
/*    */ {
/*    */   public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
/* 59 */     if (response == null) {
/* 60 */       throw new IllegalArgumentException("HTTP request may not be null");
/*    */     }
/* 62 */     if (!response.containsHeader("Server")) {
/* 63 */       String s = (String)response.getParams().getParameter("http.origin-server");
/*    */       
/* 65 */       if (s != null)
/* 66 */         response.addHeader("Server", s); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\protocol\ResponseServer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */