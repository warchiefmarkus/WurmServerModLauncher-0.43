/*    */ package org.apache.http.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.HttpEntityEnclosingRequest;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.HttpRequestInterceptor;
/*    */ import org.apache.http.HttpVersion;
/*    */ import org.apache.http.ProtocolVersion;
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
/*    */ 
/*    */ @Immutable
/*    */ public class RequestExpectContinue
/*    */   implements HttpRequestInterceptor
/*    */ {
/*    */   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
/* 64 */     if (request == null) {
/* 65 */       throw new IllegalArgumentException("HTTP request may not be null");
/*    */     }
/* 67 */     if (request instanceof HttpEntityEnclosingRequest) {
/* 68 */       HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
/*    */       
/* 70 */       if (entity != null && entity.getContentLength() != 0L) {
/* 71 */         ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
/* 72 */         if (HttpProtocolParams.useExpectContinue(request.getParams()) && !ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0))
/*    */         {
/* 74 */           request.addHeader("Expect", "100-continue");
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\protocol\RequestExpectContinue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */