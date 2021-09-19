/*     */ package org.apache.http.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.conn.scheme.SocketFactory;
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
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public final class MultihomePlainSocketFactory
/*     */   implements SocketFactory
/*     */ {
/*  67 */   private static final MultihomePlainSocketFactory DEFAULT_FACTORY = new MultihomePlainSocketFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MultihomePlainSocketFactory getSocketFactory() {
/*  74 */     return DEFAULT_FACTORY;
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
/*     */   public Socket createSocket() {
/*  87 */     return new Socket();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket connectSocket(Socket sock, String host, int port, InetAddress localAddress, int localPort, HttpParams params) throws IOException {
/* 110 */     if (host == null) {
/* 111 */       throw new IllegalArgumentException("Target host may not be null.");
/*     */     }
/* 113 */     if (params == null) {
/* 114 */       throw new IllegalArgumentException("Parameters may not be null.");
/*     */     }
/*     */     
/* 117 */     if (sock == null) {
/* 118 */       sock = createSocket();
/*     */     }
/* 120 */     if (localAddress != null || localPort > 0) {
/*     */ 
/*     */       
/* 123 */       if (localPort < 0) {
/* 124 */         localPort = 0;
/*     */       }
/* 126 */       InetSocketAddress isa = new InetSocketAddress(localAddress, localPort);
/*     */       
/* 128 */       sock.bind(isa);
/*     */     } 
/*     */     
/* 131 */     int timeout = HttpConnectionParams.getConnectionTimeout(params);
/*     */     
/* 133 */     InetAddress[] inetadrs = InetAddress.getAllByName(host);
/* 134 */     List<InetAddress> addresses = new ArrayList<InetAddress>(inetadrs.length);
/* 135 */     addresses.addAll(Arrays.asList(inetadrs));
/* 136 */     Collections.shuffle(addresses);
/*     */     
/* 138 */     IOException lastEx = null;
/* 139 */     for (InetAddress remoteAddress : addresses) {
/*     */       try {
/* 141 */         sock.connect(new InetSocketAddress(remoteAddress, port), timeout);
/*     */         break;
/* 143 */       } catch (SocketTimeoutException ex) {
/* 144 */         throw new ConnectTimeoutException("Connect to " + remoteAddress + " timed out");
/* 145 */       } catch (IOException ex) {
/*     */         
/* 147 */         sock = new Socket();
/*     */         
/* 149 */         lastEx = ex;
/*     */       } 
/*     */     } 
/* 152 */     if (lastEx != null) {
/* 153 */       throw lastEx;
/*     */     }
/* 155 */     return sock;
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
/*     */   
/*     */   public final boolean isSecure(Socket sock) throws IllegalArgumentException {
/* 173 */     if (sock == null) {
/* 174 */       throw new IllegalArgumentException("Socket may not be null.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 179 */     if (sock.getClass() != Socket.class) {
/* 180 */       throw new IllegalArgumentException("Socket not created by this factory.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 185 */     if (sock.isClosed()) {
/* 186 */       throw new IllegalArgumentException("Socket is closed.");
/*     */     }
/*     */     
/* 189 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\conn\MultihomePlainSocketFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */