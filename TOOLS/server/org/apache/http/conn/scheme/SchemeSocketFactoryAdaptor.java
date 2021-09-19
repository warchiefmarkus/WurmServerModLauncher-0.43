/*    */ package org.apache.http.conn.scheme;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.InetAddress;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.Socket;
/*    */ import java.net.UnknownHostException;
/*    */ import org.apache.http.conn.ConnectTimeoutException;
/*    */ import org.apache.http.params.HttpParams;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ class SchemeSocketFactoryAdaptor
/*    */   implements SchemeSocketFactory
/*    */ {
/*    */   private final SocketFactory factory;
/*    */   
/*    */   SchemeSocketFactoryAdaptor(SocketFactory factory) {
/* 49 */     this.factory = factory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Socket connectSocket(Socket sock, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
/* 57 */     String host = remoteAddress.getHostName();
/* 58 */     int port = remoteAddress.getPort();
/* 59 */     InetAddress local = null;
/* 60 */     int localPort = 0;
/* 61 */     if (localAddress != null) {
/* 62 */       local = localAddress.getAddress();
/* 63 */       localPort = localAddress.getPort();
/*    */     } 
/* 65 */     return this.factory.connectSocket(sock, host, port, local, localPort, params);
/*    */   }
/*    */   
/*    */   public Socket createSocket(HttpParams params) throws IOException {
/* 69 */     return this.factory.createSocket();
/*    */   }
/*    */   
/*    */   public boolean isSecure(Socket sock) throws IllegalArgumentException {
/* 73 */     return this.factory.isSecure(sock);
/*    */   }
/*    */   
/*    */   public SocketFactory getFactory() {
/* 77 */     return this.factory;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 82 */     if (obj == null) return false; 
/* 83 */     if (this == obj) return true; 
/* 84 */     if (obj instanceof SchemeSocketFactoryAdaptor) {
/* 85 */       return this.factory.equals(((SchemeSocketFactoryAdaptor)obj).factory);
/*    */     }
/* 87 */     return this.factory.equals(obj);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 93 */     return this.factory.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\conn\scheme\SchemeSocketFactoryAdaptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */