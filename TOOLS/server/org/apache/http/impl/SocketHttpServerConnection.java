/*     */ package org.apache.http.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import org.apache.http.HttpInetConnection;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.impl.io.SocketInputBuffer;
/*     */ import org.apache.http.impl.io.SocketOutputBuffer;
/*     */ import org.apache.http.io.SessionInputBuffer;
/*     */ import org.apache.http.io.SessionOutputBuffer;
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
/*     */ @NotThreadSafe
/*     */ public class SocketHttpServerConnection
/*     */   extends AbstractHttpServerConnection
/*     */   implements HttpInetConnection
/*     */ {
/*     */   private volatile boolean open;
/*  68 */   private volatile Socket socket = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void assertNotOpen() {
/*  75 */     if (this.open) {
/*  76 */       throw new IllegalStateException("Connection is already open");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void assertOpen() {
/*  82 */     if (!this.open) {
/*  83 */       throw new IllegalStateException("Connection is not open");
/*     */     }
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
/*     */   protected SessionInputBuffer createSessionInputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
/* 106 */     return (SessionInputBuffer)new SocketInputBuffer(socket, buffersize, params);
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
/*     */   protected SessionOutputBuffer createSessionOutputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
/* 128 */     return (SessionOutputBuffer)new SocketOutputBuffer(socket, buffersize, params);
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
/*     */   protected void bind(Socket socket, HttpParams params) throws IOException {
/* 150 */     if (socket == null) {
/* 151 */       throw new IllegalArgumentException("Socket may not be null");
/*     */     }
/* 153 */     if (params == null) {
/* 154 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 156 */     this.socket = socket;
/*     */     
/* 158 */     int buffersize = HttpConnectionParams.getSocketBufferSize(params);
/*     */     
/* 160 */     init(createSessionInputBuffer(socket, buffersize, params), createSessionOutputBuffer(socket, buffersize, params), params);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 165 */     this.open = true;
/*     */   }
/*     */   
/*     */   protected Socket getSocket() {
/* 169 */     return this.socket;
/*     */   }
/*     */   
/*     */   public boolean isOpen() {
/* 173 */     return this.open;
/*     */   }
/*     */   
/*     */   public InetAddress getLocalAddress() {
/* 177 */     if (this.socket != null) {
/* 178 */       return this.socket.getLocalAddress();
/*     */     }
/* 180 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLocalPort() {
/* 185 */     if (this.socket != null) {
/* 186 */       return this.socket.getLocalPort();
/*     */     }
/* 188 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getRemoteAddress() {
/* 193 */     if (this.socket != null) {
/* 194 */       return this.socket.getInetAddress();
/*     */     }
/* 196 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRemotePort() {
/* 201 */     if (this.socket != null) {
/* 202 */       return this.socket.getPort();
/*     */     }
/* 204 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(int timeout) {
/* 209 */     assertOpen();
/* 210 */     if (this.socket != null) {
/*     */       try {
/* 212 */         this.socket.setSoTimeout(timeout);
/* 213 */       } catch (SocketException ignore) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSocketTimeout() {
/* 222 */     if (this.socket != null) {
/*     */       try {
/* 224 */         return this.socket.getSoTimeout();
/* 225 */       } catch (SocketException ignore) {
/* 226 */         return -1;
/*     */       } 
/*     */     }
/* 229 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() throws IOException {
/* 234 */     this.open = false;
/* 235 */     Socket tmpsocket = this.socket;
/* 236 */     if (tmpsocket != null) {
/* 237 */       tmpsocket.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 242 */     if (!this.open) {
/*     */       return;
/*     */     }
/* 245 */     this.open = false;
/* 246 */     this.open = false;
/* 247 */     Socket sock = this.socket;
/*     */     try {
/* 249 */       doFlush();
/*     */       try {
/*     */         try {
/* 252 */           sock.shutdownOutput();
/* 253 */         } catch (IOException ignore) {}
/*     */         
/*     */         try {
/* 256 */           sock.shutdownInput();
/* 257 */         } catch (IOException ignore) {}
/*     */       }
/* 259 */       catch (UnsupportedOperationException ignore) {}
/*     */     }
/*     */     finally {
/*     */       
/* 263 */       sock.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void formatAddress(StringBuilder buffer, SocketAddress socketAddress) {
/* 268 */     if (socketAddress instanceof InetSocketAddress) {
/* 269 */       InetSocketAddress addr = (InetSocketAddress)socketAddress;
/* 270 */       buffer.append((addr.getAddress() != null) ? addr.getAddress().getHostAddress() : addr.getAddress()).append(':').append(addr.getPort());
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 275 */       buffer.append(socketAddress);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 281 */     if (this.socket != null) {
/* 282 */       StringBuilder buffer = new StringBuilder();
/* 283 */       SocketAddress remoteAddress = this.socket.getRemoteSocketAddress();
/* 284 */       SocketAddress localAddress = this.socket.getLocalSocketAddress();
/* 285 */       if (remoteAddress != null && localAddress != null) {
/* 286 */         formatAddress(buffer, localAddress);
/* 287 */         buffer.append("<->");
/* 288 */         formatAddress(buffer, remoteAddress);
/*     */       } 
/* 290 */       return buffer.toString();
/*     */     } 
/* 292 */     return super.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\SocketHttpServerConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */