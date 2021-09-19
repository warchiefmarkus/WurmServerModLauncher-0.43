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
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class SocketHttpClientConnection
/*     */   extends AbstractHttpClientConnection
/*     */   implements HttpInetConnection
/*     */ {
/*     */   private volatile boolean open;
/*  69 */   private volatile Socket socket = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void assertNotOpen() {
/*  76 */     if (this.open) {
/*  77 */       throw new IllegalStateException("Connection is already open");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void assertOpen() {
/*  83 */     if (!this.open) {
/*  84 */       throw new IllegalStateException("Connection is not open");
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
/* 107 */     return (SessionInputBuffer)new SocketInputBuffer(socket, buffersize, params);
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
/* 129 */     return (SessionOutputBuffer)new SocketOutputBuffer(socket, buffersize, params);
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
/*     */   
/*     */   protected void bind(Socket socket, HttpParams params) throws IOException {
/* 153 */     if (socket == null) {
/* 154 */       throw new IllegalArgumentException("Socket may not be null");
/*     */     }
/* 156 */     if (params == null) {
/* 157 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 159 */     this.socket = socket;
/*     */     
/* 161 */     int buffersize = HttpConnectionParams.getSocketBufferSize(params);
/*     */     
/* 163 */     init(createSessionInputBuffer(socket, buffersize, params), createSessionOutputBuffer(socket, buffersize, params), params);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 168 */     this.open = true;
/*     */   }
/*     */   
/*     */   public boolean isOpen() {
/* 172 */     return this.open;
/*     */   }
/*     */   
/*     */   protected Socket getSocket() {
/* 176 */     return this.socket;
/*     */   }
/*     */   
/*     */   public InetAddress getLocalAddress() {
/* 180 */     if (this.socket != null) {
/* 181 */       return this.socket.getLocalAddress();
/*     */     }
/* 183 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLocalPort() {
/* 188 */     if (this.socket != null) {
/* 189 */       return this.socket.getLocalPort();
/*     */     }
/* 191 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getRemoteAddress() {
/* 196 */     if (this.socket != null) {
/* 197 */       return this.socket.getInetAddress();
/*     */     }
/* 199 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRemotePort() {
/* 204 */     if (this.socket != null) {
/* 205 */       return this.socket.getPort();
/*     */     }
/* 207 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(int timeout) {
/* 212 */     assertOpen();
/* 213 */     if (this.socket != null) {
/*     */       try {
/* 215 */         this.socket.setSoTimeout(timeout);
/* 216 */       } catch (SocketException ignore) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSocketTimeout() {
/* 225 */     if (this.socket != null) {
/*     */       try {
/* 227 */         return this.socket.getSoTimeout();
/* 228 */       } catch (SocketException ignore) {
/* 229 */         return -1;
/*     */       } 
/*     */     }
/* 232 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() throws IOException {
/* 237 */     this.open = false;
/* 238 */     Socket tmpsocket = this.socket;
/* 239 */     if (tmpsocket != null) {
/* 240 */       tmpsocket.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 245 */     if (!this.open) {
/*     */       return;
/*     */     }
/* 248 */     this.open = false;
/* 249 */     Socket sock = this.socket;
/*     */     try {
/* 251 */       doFlush();
/*     */       try {
/*     */         try {
/* 254 */           sock.shutdownOutput();
/* 255 */         } catch (IOException ignore) {}
/*     */         
/*     */         try {
/* 258 */           sock.shutdownInput();
/* 259 */         } catch (IOException ignore) {}
/*     */       }
/* 261 */       catch (UnsupportedOperationException ignore) {}
/*     */     }
/*     */     finally {
/*     */       
/* 265 */       sock.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void formatAddress(StringBuilder buffer, SocketAddress socketAddress) {
/* 270 */     if (socketAddress instanceof InetSocketAddress) {
/* 271 */       InetSocketAddress addr = (InetSocketAddress)socketAddress;
/* 272 */       buffer.append((addr.getAddress() != null) ? addr.getAddress().getHostAddress() : addr.getAddress()).append(':').append(addr.getPort());
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 277 */       buffer.append(socketAddress);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 283 */     if (this.socket != null) {
/* 284 */       StringBuilder buffer = new StringBuilder();
/* 285 */       SocketAddress remoteAddress = this.socket.getRemoteSocketAddress();
/* 286 */       SocketAddress localAddress = this.socket.getLocalSocketAddress();
/* 287 */       if (remoteAddress != null && localAddress != null) {
/* 288 */         formatAddress(buffer, localAddress);
/* 289 */         buffer.append("<->");
/* 290 */         formatAddress(buffer, remoteAddress);
/*     */       } 
/* 292 */       return buffer.toString();
/*     */     } 
/* 294 */     return super.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\SocketHttpClientConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */