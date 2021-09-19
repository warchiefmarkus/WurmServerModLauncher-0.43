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
/*    */ @NotThreadSafe
/*    */ public class DefaultHttpServerConnection
/*    */   extends SocketHttpServerConnection
/*    */ {
/*    */   public void bind(Socket socket, HttpParams params) throws IOException {
/* 65 */     if (socket == null) {
/* 66 */       throw new IllegalArgumentException("Socket may not be null");
/*    */     }
/* 68 */     if (params == null) {
/* 69 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*    */     }
/* 71 */     assertNotOpen();
/* 72 */     socket.setTcpNoDelay(HttpConnectionParams.getTcpNoDelay(params));
/* 73 */     socket.setSoTimeout(HttpConnectionParams.getSoTimeout(params));
/* 74 */     socket.setKeepAlive(HttpConnectionParams.getSoKeepalive(params));
/*    */     
/* 76 */     int linger = HttpConnectionParams.getLinger(params);
/* 77 */     if (linger >= 0) {
/* 78 */       socket.setSoLinger((linger > 0), linger);
/*    */     }
/* 80 */     super.bind(socket, params);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\DefaultHttpServerConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */