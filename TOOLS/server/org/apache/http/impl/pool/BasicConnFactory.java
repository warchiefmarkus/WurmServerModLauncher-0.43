/*     */ package org.apache.http.impl.pool;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import org.apache.http.HttpClientConnection;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.impl.DefaultHttpClientConnection;
/*     */ import org.apache.http.params.HttpConnectionParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.pool.ConnFactory;
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
/*     */ public class BasicConnFactory
/*     */   implements ConnFactory<HttpHost, HttpClientConnection>
/*     */ {
/*     */   private final SSLSocketFactory sslfactory;
/*     */   private final HttpParams params;
/*     */   
/*     */   public BasicConnFactory(SSLSocketFactory sslfactory, HttpParams params) {
/*  69 */     if (params == null) {
/*  70 */       throw new IllegalArgumentException("HTTP params may not be null");
/*     */     }
/*  72 */     this.sslfactory = sslfactory;
/*  73 */     this.params = params;
/*     */   }
/*     */   
/*     */   public BasicConnFactory(HttpParams params) {
/*  77 */     this(null, params);
/*     */   }
/*     */   
/*     */   protected HttpClientConnection create(Socket socket, HttpParams params) throws IOException {
/*  81 */     DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
/*  82 */     conn.bind(socket, params);
/*  83 */     return (HttpClientConnection)conn;
/*     */   }
/*     */   
/*     */   public HttpClientConnection create(HttpHost host) throws IOException {
/*  87 */     String scheme = host.getSchemeName();
/*  88 */     Socket socket = null;
/*  89 */     if ("http".equalsIgnoreCase(scheme))
/*  90 */       socket = new Socket(); 
/*  91 */     if ("https".equalsIgnoreCase(scheme) && 
/*  92 */       this.sslfactory != null) {
/*  93 */       socket = this.sslfactory.createSocket();
/*     */     }
/*     */     
/*  96 */     if (socket == null) {
/*  97 */       throw new IOException(scheme + " scheme is not supported");
/*     */     }
/*  99 */     int connectTimeout = HttpConnectionParams.getConnectionTimeout(this.params);
/* 100 */     int soTimeout = HttpConnectionParams.getSoTimeout(this.params);
/*     */     
/* 102 */     socket.setSoTimeout(soTimeout);
/* 103 */     socket.connect(new InetSocketAddress(host.getHostName(), host.getPort()), connectTimeout);
/* 104 */     return create(socket, this.params);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\pool\BasicConnFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */