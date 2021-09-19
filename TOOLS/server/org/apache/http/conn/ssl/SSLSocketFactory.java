/*     */ package org.apache.http.conn.ssl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.ConnectTimeoutException;
/*     */ import org.apache.http.conn.HttpInetSocketAddress;
/*     */ import org.apache.http.conn.scheme.HostNameResolver;
/*     */ import org.apache.http.conn.scheme.LayeredSchemeSocketFactory;
/*     */ import org.apache.http.conn.scheme.LayeredSocketFactory;
/*     */ import org.apache.http.conn.scheme.SchemeLayeredSocketFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ public class SSLSocketFactory
/*     */   implements SchemeLayeredSocketFactory, LayeredSchemeSocketFactory, LayeredSocketFactory
/*     */ {
/*     */   public static final String TLS = "TLS";
/*     */   public static final String SSL = "SSL";
/*     */   public static final String SSLV2 = "SSLv2";
/* 152 */   public static final X509HostnameVerifier ALLOW_ALL_HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();
/*     */ 
/*     */   
/* 155 */   public static final X509HostnameVerifier BROWSER_COMPATIBLE_HOSTNAME_VERIFIER = new BrowserCompatHostnameVerifier();
/*     */ 
/*     */   
/* 158 */   public static final X509HostnameVerifier STRICT_HOSTNAME_VERIFIER = new StrictHostnameVerifier();
/*     */ 
/*     */   
/*     */   private final javax.net.ssl.SSLSocketFactory socketfactory;
/*     */ 
/*     */   
/*     */   private final HostNameResolver nameResolver;
/*     */   
/*     */   private volatile X509HostnameVerifier hostnameVerifier;
/*     */ 
/*     */   
/*     */   public static SSLSocketFactory getSocketFactory() throws SSLInitializationException {
/*     */     try {
/* 171 */       SSLContext sslcontext = SSLContext.getInstance("TLS");
/* 172 */       sslcontext.init(null, null, null);
/* 173 */       return new SSLSocketFactory(sslcontext, BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
/*     */     
/*     */     }
/* 176 */     catch (NoSuchAlgorithmException ex) {
/* 177 */       throw new SSLInitializationException(ex.getMessage(), ex);
/* 178 */     } catch (KeyManagementException ex) {
/* 179 */       throw new SSLInitializationException(ex.getMessage(), ex);
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
/*     */   public static SSLSocketFactory getSystemSocketFactory() throws SSLInitializationException {
/* 193 */     return new SSLSocketFactory((javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory.getDefault(), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
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
/*     */   private static SSLContext createSSLContext(String algorithm, KeyStore keystore, String keyPassword, KeyStore truststore, SecureRandom random, TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
/* 211 */     if (algorithm == null) {
/* 212 */       algorithm = "TLS";
/*     */     }
/* 214 */     KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
/*     */     
/* 216 */     kmfactory.init(keystore, (keyPassword != null) ? keyPassword.toCharArray() : null);
/* 217 */     KeyManager[] keymanagers = kmfactory.getKeyManagers();
/* 218 */     TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
/*     */     
/* 220 */     tmfactory.init(truststore);
/* 221 */     TrustManager[] trustmanagers = tmfactory.getTrustManagers();
/* 222 */     if (trustmanagers != null && trustStrategy != null) {
/* 223 */       for (int i = 0; i < trustmanagers.length; i++) {
/* 224 */         TrustManager tm = trustmanagers[i];
/* 225 */         if (tm instanceof X509TrustManager) {
/* 226 */           trustmanagers[i] = new TrustManagerDecorator((X509TrustManager)tm, trustStrategy);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 232 */     SSLContext sslcontext = SSLContext.getInstance(algorithm);
/* 233 */     sslcontext.init(keymanagers, trustmanagers, random);
/* 234 */     return sslcontext;
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
/*     */   @Deprecated
/*     */   public SSLSocketFactory(String algorithm, KeyStore keystore, String keyPassword, KeyStore truststore, SecureRandom random, HostNameResolver nameResolver) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 249 */     this(createSSLContext(algorithm, keystore, keyPassword, truststore, random, null), nameResolver);
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
/*     */   public SSLSocketFactory(String algorithm, KeyStore keystore, String keyPassword, KeyStore truststore, SecureRandom random, X509HostnameVerifier hostnameVerifier) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 265 */     this(createSSLContext(algorithm, keystore, keyPassword, truststore, random, null), hostnameVerifier);
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
/*     */   public SSLSocketFactory(String algorithm, KeyStore keystore, String keyPassword, KeyStore truststore, SecureRandom random, TrustStrategy trustStrategy, X509HostnameVerifier hostnameVerifier) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 282 */     this(createSSLContext(algorithm, keystore, keyPassword, truststore, random, trustStrategy), hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLSocketFactory(KeyStore keystore, String keystorePassword, KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 292 */     this("TLS", keystore, keystorePassword, truststore, null, null, BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLSocketFactory(KeyStore keystore, String keystorePassword) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 299 */     this("TLS", keystore, keystorePassword, null, null, null, BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 305 */     this("TLS", null, null, truststore, null, null, BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLSocketFactory(TrustStrategy trustStrategy, X509HostnameVerifier hostnameVerifier) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 315 */     this("TLS", null, null, null, null, trustStrategy, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLSocketFactory(TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 324 */     this("TLS", null, null, null, null, trustStrategy, BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
/*     */   }
/*     */   
/*     */   public SSLSocketFactory(SSLContext sslContext) {
/* 328 */     this(sslContext, BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public SSLSocketFactory(SSLContext sslContext, HostNameResolver nameResolver) {
/* 338 */     this.socketfactory = sslContext.getSocketFactory();
/* 339 */     this.hostnameVerifier = BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
/* 340 */     this.nameResolver = nameResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLSocketFactory(SSLContext sslContext, X509HostnameVerifier hostnameVerifier) {
/* 349 */     if (sslContext == null) {
/* 350 */       throw new IllegalArgumentException("SSL context may not be null");
/*     */     }
/* 352 */     this.socketfactory = sslContext.getSocketFactory();
/* 353 */     this.hostnameVerifier = hostnameVerifier;
/* 354 */     this.nameResolver = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLSocketFactory(javax.net.ssl.SSLSocketFactory socketfactory, X509HostnameVerifier hostnameVerifier) {
/* 363 */     if (socketfactory == null) {
/* 364 */       throw new IllegalArgumentException("SSL socket factory may not be null");
/*     */     }
/* 366 */     this.socketfactory = socketfactory;
/* 367 */     this.hostnameVerifier = hostnameVerifier;
/* 368 */     this.nameResolver = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createSocket(HttpParams params) throws IOException {
/* 377 */     SSLSocket sock = (SSLSocket)this.socketfactory.createSocket();
/* 378 */     prepareSocket(sock);
/* 379 */     return sock;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Socket createSocket() throws IOException {
/* 384 */     SSLSocket sock = (SSLSocket)this.socketfactory.createSocket();
/* 385 */     prepareSocket(sock);
/* 386 */     return sock;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket connectSocket(Socket socket, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
/*     */     String hostname;
/*     */     SSLSocket sslsock;
/* 397 */     if (remoteAddress == null) {
/* 398 */       throw new IllegalArgumentException("Remote address may not be null");
/*     */     }
/* 400 */     if (params == null) {
/* 401 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 403 */     Socket sock = (socket != null) ? socket : this.socketfactory.createSocket();
/* 404 */     if (localAddress != null) {
/* 405 */       sock.setReuseAddress(HttpConnectionParams.getSoReuseaddr(params));
/* 406 */       sock.bind(localAddress);
/*     */     } 
/*     */     
/* 409 */     int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
/* 410 */     int soTimeout = HttpConnectionParams.getSoTimeout(params);
/*     */     
/*     */     try {
/* 413 */       sock.setSoTimeout(soTimeout);
/* 414 */       sock.connect(remoteAddress, connTimeout);
/* 415 */     } catch (SocketTimeoutException ex) {
/* 416 */       throw new ConnectTimeoutException("Connect to " + remoteAddress + " timed out", ex);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 421 */     if (remoteAddress instanceof HttpInetSocketAddress) {
/* 422 */       hostname = ((HttpInetSocketAddress)remoteAddress).getHttpHost().getHostName();
/*     */     } else {
/* 424 */       hostname = remoteAddress.getHostName();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 429 */     if (sock instanceof SSLSocket) {
/* 430 */       sslsock = (SSLSocket)sock;
/*     */     } else {
/* 432 */       int port = remoteAddress.getPort();
/* 433 */       sslsock = (SSLSocket)this.socketfactory.createSocket(sock, hostname, port, true);
/* 434 */       prepareSocket(sslsock);
/*     */     } 
/* 436 */     sslsock.startHandshake();
/* 437 */     if (this.hostnameVerifier != null) {
/*     */       try {
/* 439 */         this.hostnameVerifier.verify(hostname, sslsock);
/*     */       }
/* 441 */       catch (IOException iox) {
/*     */         
/* 443 */         try { sslsock.close(); } catch (Exception x) {}
/* 444 */         throw iox;
/*     */       } 
/*     */     }
/* 447 */     return sslsock;
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
/*     */   public boolean isSecure(Socket sock) throws IllegalArgumentException {
/* 466 */     if (sock == null) {
/* 467 */       throw new IllegalArgumentException("Socket may not be null");
/*     */     }
/*     */     
/* 470 */     if (!(sock instanceof SSLSocket)) {
/* 471 */       throw new IllegalArgumentException("Socket not created by this factory");
/*     */     }
/*     */     
/* 474 */     if (sock.isClosed()) {
/* 475 */       throw new IllegalArgumentException("Socket is closed");
/*     */     }
/* 477 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createLayeredSocket(Socket socket, String host, int port, HttpParams params) throws IOException, UnknownHostException {
/* 488 */     SSLSocket sslSocket = (SSLSocket)this.socketfactory.createSocket(socket, host, port, true);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 493 */     prepareSocket(sslSocket);
/* 494 */     sslSocket.startHandshake();
/* 495 */     if (this.hostnameVerifier != null) {
/* 496 */       this.hostnameVerifier.verify(host, sslSocket);
/*     */     }
/*     */     
/* 499 */     return sslSocket;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createLayeredSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
/* 510 */     SSLSocket sslSocket = (SSLSocket)this.socketfactory.createSocket(socket, host, port, autoClose);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 516 */     prepareSocket(sslSocket);
/* 517 */     sslSocket.startHandshake();
/* 518 */     if (this.hostnameVerifier != null) {
/* 519 */       this.hostnameVerifier.verify(host, sslSocket);
/*     */     }
/*     */     
/* 522 */     return sslSocket;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void setHostnameVerifier(X509HostnameVerifier hostnameVerifier) {
/* 527 */     if (hostnameVerifier == null) {
/* 528 */       throw new IllegalArgumentException("Hostname verifier may not be null");
/*     */     }
/* 530 */     this.hostnameVerifier = hostnameVerifier;
/*     */   }
/*     */   
/*     */   public X509HostnameVerifier getHostnameVerifier() {
/* 534 */     return this.hostnameVerifier;
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
/* 546 */     InetSocketAddress local = null;
/* 547 */     if (localAddress != null || localPort > 0) {
/*     */       
/* 549 */       if (localPort < 0) {
/* 550 */         localPort = 0;
/*     */       }
/* 552 */       local = new InetSocketAddress(localAddress, localPort);
/*     */     } 
/*     */     
/* 555 */     if (this.nameResolver != null) {
/* 556 */       remoteAddress = this.nameResolver.resolve(host);
/*     */     } else {
/* 558 */       remoteAddress = InetAddress.getByName(host);
/*     */     } 
/* 560 */     HttpInetSocketAddress httpInetSocketAddress = new HttpInetSocketAddress(new HttpHost(host, port), remoteAddress, port);
/* 561 */     return connectSocket(socket, (InetSocketAddress)httpInetSocketAddress, local, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
/* 572 */     return createLayeredSocket(socket, host, port, autoClose);
/*     */   }
/*     */   
/*     */   protected void prepareSocket(SSLSocket socket) throws IOException {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\conn\ssl\SSLSocketFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */