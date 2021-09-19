/*    */ package org.apache.http.impl.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.Socket;
/*    */ import org.apache.http.annotation.NotThreadSafe;
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
/*    */ 
/*    */ @NotThreadSafe
/*    */ public class SocketOutputBuffer
/*    */   extends AbstractSessionOutputBuffer
/*    */ {
/*    */   public SocketOutputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
/* 67 */     if (socket == null) {
/* 68 */       throw new IllegalArgumentException("Socket may not be null");
/*    */     }
/* 70 */     if (buffersize < 0) {
/* 71 */       buffersize = socket.getSendBufferSize();
/*    */     }
/* 73 */     if (buffersize < 1024) {
/* 74 */       buffersize = 1024;
/*    */     }
/* 76 */     init(socket.getOutputStream(), buffersize, params);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\io\SocketOutputBuffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */