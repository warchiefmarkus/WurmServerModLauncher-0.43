/*    */ package org.apache.http.conn.scheme;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.InetAddress;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.Socket;
/*    */ import java.net.UnknownHostException;
/*    */ import org.apache.http.conn.ConnectTimeoutException;
/*    */ import org.apache.http.params.BasicHttpParams;
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
/*    */ class SocketFactoryAdaptor
/*    */   implements SocketFactory
/*    */ {
/*    */   private final SchemeSocketFactory factory;
/*    */   
/*    */   SocketFactoryAdaptor(SchemeSocketFactory factory) {
/* 50 */     this.factory = factory;
/*    */   }
/*    */   
/*    */   public Socket createSocket() throws IOException {
/* 54 */     BasicHttpParams basicHttpParams = new BasicHttpParams();
/* 55 */     return this.factory.createSocket((HttpParams)basicHttpParams);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Socket connectSocket(Socket socket, String host, int port, InetAddress localAddress, int localPort, HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
/* 63 */     InetSocketAddress local = null;
/* 64 */     if (localAddress != null || localPort > 0) {
/*    */       
/* 66 */       if (localPort < 0) {
/* 67 */         localPort = 0;
/*    */       }
/* 69 */       local = new InetSocketAddress(localAddress, localPort);
/*    */     } 
/* 71 */     InetAddress remoteAddress = InetAddress.getByName(host);
/* 72 */     InetSocketAddress remote = new InetSocketAddress(remoteAddress, port);
/* 73 */     return this.factory.connectSocket(socket, remote, local, params);
/*    */   }
/*    */   
/*    */   public boolean isSecure(Socket socket) throws IllegalArgumentException {
/* 77 */     return this.factory.isSecure(socket);
/*    */   }
/*    */   
/*    */   public SchemeSocketFactory getFactory() {
/* 81 */     return this.factory;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 86 */     if (obj == null) return false; 
/* 87 */     if (this == obj) return true; 
/* 88 */     if (obj instanceof SocketFactoryAdaptor) {
/* 89 */       return this.factory.equals(((SocketFactoryAdaptor)obj).factory);
/*    */     }
/* 91 */     return this.factory.equals(obj);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 97 */     return this.factory.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\conn\scheme\SocketFactoryAdaptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */