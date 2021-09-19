/*    */ package org.apache.http.impl;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.Socket;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.params.HttpConnectionParams;
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
/*    */ @NotThreadSafe
/*    */ public class DefaultHttpClientConnection
/*    */   extends SocketHttpClientConnection
/*    */ {
/*    */   public void bind(Socket socket, HttpParams params) throws IOException {
/* 67 */     if (socket == null) {
/* 68 */       throw new IllegalArgumentException("Socket may not be null");
/*    */     }
/* 70 */     if (params == null) {
/* 71 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*    */     }
/* 73 */     assertNotOpen();
/* 74 */     socket.setTcpNoDelay(HttpConnectionParams.getTcpNoDelay(params));
/* 75 */     socket.setSoTimeout(HttpConnectionParams.getSoTimeout(params));
/* 76 */     socket.setKeepAlive(HttpConnectionParams.getSoKeepalive(params));
/*    */     
/* 78 */     int linger = HttpConnectionParams.getLinger(params);
/* 79 */     if (linger >= 0) {
/* 80 */       socket.setSoLinger((linger > 0), linger);
/*    */     }
/* 82 */     super.bind(socket, params);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\DefaultHttpClientConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */