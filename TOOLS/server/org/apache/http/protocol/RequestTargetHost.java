/*    */ package org.apache.http.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.InetAddress;
/*    */ import org.apache.http.HttpConnection;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpHost;
/*    */ import org.apache.http.HttpInetConnection;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.HttpRequestInterceptor;
/*    */ import org.apache.http.HttpVersion;
/*    */ import org.apache.http.ProtocolException;
/*    */ import org.apache.http.ProtocolVersion;
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
/*    */ @Immutable
/*    */ public class RequestTargetHost
/*    */   implements HttpRequestInterceptor
/*    */ {
/*    */   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
/* 59 */     if (request == null) {
/* 60 */       throw new IllegalArgumentException("HTTP request may not be null");
/*    */     }
/* 62 */     if (context == null) {
/* 63 */       throw new IllegalArgumentException("HTTP context may not be null");
/*    */     }
/*    */     
/* 66 */     ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
/* 67 */     String method = request.getRequestLine().getMethod();
/* 68 */     if (method.equalsIgnoreCase("CONNECT") && ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0)) {
/*    */       return;
/*    */     }
/*    */     
/* 72 */     if (!request.containsHeader("Host")) {
/* 73 */       HttpHost targethost = (HttpHost)context.getAttribute("http.target_host");
/*    */       
/* 75 */       if (targethost == null) {
/* 76 */         HttpConnection conn = (HttpConnection)context.getAttribute("http.connection");
/*    */         
/* 78 */         if (conn instanceof HttpInetConnection) {
/*    */ 
/*    */           
/* 81 */           InetAddress address = ((HttpInetConnection)conn).getRemoteAddress();
/* 82 */           int port = ((HttpInetConnection)conn).getRemotePort();
/* 83 */           if (address != null) {
/* 84 */             targethost = new HttpHost(address.getHostName(), port);
/*    */           }
/*    */         } 
/* 87 */         if (targethost == null) {
/* 88 */           if (ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0)) {
/*    */             return;
/*    */           }
/* 91 */           throw new ProtocolException("Target host missing");
/*    */         } 
/*    */       } 
/*    */       
/* 95 */       request.addHeader("Host", targethost.toHostString());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\protocol\RequestTargetHost.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */