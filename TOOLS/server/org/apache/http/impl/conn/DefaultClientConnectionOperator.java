/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.ConnectException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.UnknownHostException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.ConnectTimeoutException;
/*     */ import org.apache.http.conn.DnsResolver;
/*     */ import org.apache.http.conn.HttpHostConnectException;
/*     */ import org.apache.http.conn.HttpInetSocketAddress;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.conn.scheme.Scheme;
/*     */ import org.apache.http.conn.scheme.SchemeLayeredSocketFactory;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
/*     */ import org.apache.http.conn.scheme.SchemeSocketFactory;
/*     */ import org.apache.http.params.HttpConnectionParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class DefaultClientConnectionOperator
/*     */   implements ClientConnectionOperator
/*     */ {
/*  90 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final SchemeRegistry schemeRegistry;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final DnsResolver dnsResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultClientConnectionOperator(SchemeRegistry schemes) {
/* 106 */     if (schemes == null) {
/* 107 */       throw new IllegalArgumentException("Scheme registry amy not be null");
/*     */     }
/* 109 */     this.schemeRegistry = schemes;
/* 110 */     this.dnsResolver = new SystemDefaultDnsResolver();
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
/*     */   public DefaultClientConnectionOperator(SchemeRegistry schemes, DnsResolver dnsResolver) {
/* 123 */     if (schemes == null) {
/* 124 */       throw new IllegalArgumentException("Scheme registry may not be null");
/*     */     }
/*     */ 
/*     */     
/* 128 */     if (dnsResolver == null) {
/* 129 */       throw new IllegalArgumentException("DNS resolver may not be null");
/*     */     }
/*     */     
/* 132 */     this.schemeRegistry = schemes;
/* 133 */     this.dnsResolver = dnsResolver;
/*     */   }
/*     */   
/*     */   public OperatedClientConnection createConnection() {
/* 137 */     return new DefaultClientConnection();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void openConnection(OperatedClientConnection conn, HttpHost target, InetAddress local, HttpContext context, HttpParams params) throws IOException {
/* 146 */     if (conn == null) {
/* 147 */       throw new IllegalArgumentException("Connection may not be null");
/*     */     }
/* 149 */     if (target == null) {
/* 150 */       throw new IllegalArgumentException("Target host may not be null");
/*     */     }
/* 152 */     if (params == null) {
/* 153 */       throw new IllegalArgumentException("Parameters may not be null");
/*     */     }
/* 155 */     if (conn.isOpen()) {
/* 156 */       throw new IllegalStateException("Connection must not be open");
/*     */     }
/*     */     
/* 159 */     Scheme schm = this.schemeRegistry.getScheme(target.getSchemeName());
/* 160 */     SchemeSocketFactory sf = schm.getSchemeSocketFactory();
/*     */     
/* 162 */     InetAddress[] addresses = resolveHostname(target.getHostName());
/* 163 */     int port = schm.resolvePort(target.getPort());
/* 164 */     for (int i = 0; i < addresses.length; i++) {
/* 165 */       InetAddress address = addresses[i];
/* 166 */       boolean last = (i == addresses.length - 1);
/*     */       
/* 168 */       Socket sock = sf.createSocket(params);
/* 169 */       conn.opening(sock, target);
/*     */       
/* 171 */       HttpInetSocketAddress httpInetSocketAddress = new HttpInetSocketAddress(target, address, port);
/* 172 */       InetSocketAddress localAddress = null;
/* 173 */       if (local != null) {
/* 174 */         localAddress = new InetSocketAddress(local, 0);
/*     */       }
/* 176 */       if (this.log.isDebugEnabled()) {
/* 177 */         this.log.debug("Connecting to " + httpInetSocketAddress);
/*     */       }
/*     */       try {
/* 180 */         Socket connsock = sf.connectSocket(sock, (InetSocketAddress)httpInetSocketAddress, localAddress, params);
/* 181 */         if (sock != connsock) {
/* 182 */           sock = connsock;
/* 183 */           conn.opening(sock, target);
/*     */         } 
/* 185 */         prepareSocket(sock, context, params);
/* 186 */         conn.openCompleted(sf.isSecure(sock), params);
/*     */         return;
/* 188 */       } catch (ConnectException ex) {
/* 189 */         if (last) {
/* 190 */           throw new HttpHostConnectException(target, ex);
/*     */         }
/* 192 */       } catch (ConnectTimeoutException ex) {
/* 193 */         if (last) {
/* 194 */           throw ex;
/*     */         }
/*     */       } 
/* 197 */       if (this.log.isDebugEnabled()) {
/* 198 */         this.log.debug("Connect to " + httpInetSocketAddress + " timed out. " + "Connection will be retried using another IP address");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateSecureConnection(OperatedClientConnection conn, HttpHost target, HttpContext context, HttpParams params) throws IOException {
/*     */     Socket sock;
/* 209 */     if (conn == null) {
/* 210 */       throw new IllegalArgumentException("Connection may not be null");
/*     */     }
/* 212 */     if (target == null) {
/* 213 */       throw new IllegalArgumentException("Target host may not be null");
/*     */     }
/* 215 */     if (params == null) {
/* 216 */       throw new IllegalArgumentException("Parameters may not be null");
/*     */     }
/* 218 */     if (!conn.isOpen()) {
/* 219 */       throw new IllegalStateException("Connection must be open");
/*     */     }
/*     */     
/* 222 */     Scheme schm = this.schemeRegistry.getScheme(target.getSchemeName());
/* 223 */     if (!(schm.getSchemeSocketFactory() instanceof SchemeLayeredSocketFactory)) {
/* 224 */       throw new IllegalArgumentException("Target scheme (" + schm.getName() + ") must have layered socket factory.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 229 */     SchemeLayeredSocketFactory lsf = (SchemeLayeredSocketFactory)schm.getSchemeSocketFactory();
/*     */     
/*     */     try {
/* 232 */       sock = lsf.createLayeredSocket(conn.getSocket(), target.getHostName(), schm.resolvePort(target.getPort()), params);
/*     */     }
/* 234 */     catch (ConnectException ex) {
/* 235 */       throw new HttpHostConnectException(target, ex);
/*     */     } 
/* 237 */     prepareSocket(sock, context, params);
/* 238 */     conn.update(sock, target, lsf.isSecure(sock), params);
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
/*     */   protected void prepareSocket(Socket sock, HttpContext context, HttpParams params) throws IOException {
/* 254 */     sock.setTcpNoDelay(HttpConnectionParams.getTcpNoDelay(params));
/* 255 */     sock.setSoTimeout(HttpConnectionParams.getSoTimeout(params));
/*     */     
/* 257 */     int linger = HttpConnectionParams.getLinger(params);
/* 258 */     if (linger >= 0) {
/* 259 */       sock.setSoLinger((linger > 0), linger);
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
/*     */   protected InetAddress[] resolveHostname(String host) throws UnknownHostException {
/* 278 */     return this.dnsResolver.resolve(host);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\DefaultClientConnectionOperator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */