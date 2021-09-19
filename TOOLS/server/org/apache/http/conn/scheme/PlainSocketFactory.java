/*     */ package org.apache.http.conn.scheme;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.net.UnknownHostException;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.conn.ConnectTimeoutException;
/*     */ import org.apache.http.params.HttpConnectionParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class PlainSocketFactory
/*     */   implements SocketFactory, SchemeSocketFactory
/*     */ {
/*     */   private final HostNameResolver nameResolver;
/*     */   
/*     */   public static PlainSocketFactory getSocketFactory() {
/*  68 */     return new PlainSocketFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public PlainSocketFactory(HostNameResolver nameResolver) {
/*  77 */     this.nameResolver = nameResolver;
/*     */   }
/*     */ 
/*     */   
/*     */   public PlainSocketFactory() {
/*  82 */     this.nameResolver = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createSocket(HttpParams params) {
/*  93 */     return new Socket();
/*     */   }
/*     */   
/*     */   public Socket createSocket() {
/*  97 */     return new Socket();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket connectSocket(Socket socket, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpParams params) throws IOException, ConnectTimeoutException {
/* 108 */     if (remoteAddress == null) {
/* 109 */       throw new IllegalArgumentException("Remote address may not be null");
/*     */     }
/* 111 */     if (params == null) {
/* 112 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 114 */     Socket sock = socket;
/* 115 */     if (sock == null) {
/* 116 */       sock = createSocket();
/*     */     }
/* 118 */     if (localAddress != null) {
/* 119 */       sock.setReuseAddress(HttpConnectionParams.getSoReuseaddr(params));
/* 120 */       sock.bind(localAddress);
/*     */     } 
/* 122 */     int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
/* 123 */     int soTimeout = HttpConnectionParams.getSoTimeout(params);
/*     */     
/*     */     try {
/* 126 */       sock.setSoTimeout(soTimeout);
/* 127 */       sock.connect(remoteAddress, connTimeout);
/* 128 */     } catch (SocketTimeoutException ex) {
/* 129 */       throw new ConnectTimeoutException("Connect to " + remoteAddress + " timed out", ex);
/*     */     } 
/*     */     
/* 132 */     return sock;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isSecure(Socket sock) throws IllegalArgumentException {
/* 149 */     if (sock == null) {
/* 150 */       throw new IllegalArgumentException("Socket may not be null.");
/*     */     }
/*     */ 
/*     */     
/* 154 */     if (sock.isClosed()) {
/* 155 */       throw new IllegalArgumentException("Socket is closed.");
/*     */     }
/* 157 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Socket connectSocket(Socket socket, String host, int port, InetAddress localAddress, int localPort, HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
/*     */     InetAddress remoteAddress;
/* 169 */     InetSocketAddress local = null;
/* 170 */     if (localAddress != null || localPort > 0) {
/*     */       
/* 172 */       if (localPort < 0) {
/* 173 */         localPort = 0;
/*     */       }
/* 175 */       local = new InetSocketAddress(localAddress, localPort);
/*     */     } 
/*     */     
/* 178 */     if (this.nameResolver != null) {
/* 179 */       remoteAddress = this.nameResolver.resolve(host);
/*     */     } else {
/* 181 */       remoteAddress = InetAddress.getByName(host);
/*     */     } 
/* 183 */     InetSocketAddress remote = new InetSocketAddress(remoteAddress, port);
/* 184 */     return connectSocket(socket, remote, local, params);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\conn\scheme\PlainSocketFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */