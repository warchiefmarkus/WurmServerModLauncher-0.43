/*    */ package org.apache.http.conn;
/*    */ 
/*    */ import java.net.InetAddress;
/*    */ import java.net.InetSocketAddress;
/*    */ import org.apache.http.HttpHost;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HttpInetSocketAddress
/*    */   extends InetSocketAddress
/*    */ {
/*    */   private static final long serialVersionUID = -6650701828361907957L;
/*    */   private final HttpHost httphost;
/*    */   
/*    */   public HttpInetSocketAddress(HttpHost httphost, InetAddress addr, int port) {
/* 47 */     super(addr, port);
/* 48 */     if (httphost == null) {
/* 49 */       throw new IllegalArgumentException("HTTP host may not be null");
/*    */     }
/* 51 */     this.httphost = httphost;
/*    */   }
/*    */   
/*    */   public HttpHost getHttpHost() {
/* 55 */     return this.httphost;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 60 */     return this.httphost.getHostName() + ":" + getPort();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\conn\HttpInetSocketAddress.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */