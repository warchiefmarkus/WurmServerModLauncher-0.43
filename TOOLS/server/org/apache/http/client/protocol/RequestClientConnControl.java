/*    */ package org.apache.http.client.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.HttpRequestInterceptor;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.conn.HttpRoutedConnection;
/*    */ import org.apache.http.conn.routing.HttpRoute;
/*    */ import org.apache.http.protocol.HttpContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/*    */ public class RequestClientConnControl
/*    */   implements HttpRequestInterceptor
/*    */ {
/* 55 */   private final Log log = LogFactory.getLog(getClass());
/*    */ 
/*    */ 
/*    */   
/*    */   private static final String PROXY_CONN_DIRECTIVE = "Proxy-Connection";
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
/* 65 */     if (request == null) {
/* 66 */       throw new IllegalArgumentException("HTTP request may not be null");
/*    */     }
/*    */     
/* 69 */     String method = request.getRequestLine().getMethod();
/* 70 */     if (method.equalsIgnoreCase("CONNECT")) {
/* 71 */       request.setHeader("Proxy-Connection", "Keep-Alive");
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 76 */     HttpRoutedConnection conn = (HttpRoutedConnection)context.getAttribute("http.connection");
/*    */     
/* 78 */     if (conn == null) {
/* 79 */       this.log.debug("HTTP connection not set in the context");
/*    */       
/*    */       return;
/*    */     } 
/* 83 */     HttpRoute route = conn.getRoute();
/*    */     
/* 85 */     if ((route.getHopCount() == 1 || route.isTunnelled()) && 
/* 86 */       !request.containsHeader("Connection")) {
/* 87 */       request.addHeader("Connection", "Keep-Alive");
/*    */     }
/*    */     
/* 90 */     if (route.getHopCount() == 2 && !route.isTunnelled() && 
/* 91 */       !request.containsHeader("Proxy-Connection"))
/* 92 */       request.addHeader("Proxy-Connection", "Keep-Alive"); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\client\protocol\RequestClientConnControl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */