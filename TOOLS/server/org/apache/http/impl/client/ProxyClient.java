/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.Socket;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.HttpClientConnection;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.auth.AuthSchemeFactory;
/*     */ import org.apache.http.auth.AuthSchemeRegistry;
/*     */ import org.apache.http.auth.AuthScope;
/*     */ import org.apache.http.auth.AuthState;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.client.params.HttpClientParams;
/*     */ import org.apache.http.client.protocol.RequestClientConnControl;
/*     */ import org.apache.http.client.protocol.RequestProxyAuthentication;
/*     */ import org.apache.http.conn.HttpRoutedConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.entity.BufferedHttpEntity;
/*     */ import org.apache.http.impl.DefaultConnectionReuseStrategy;
/*     */ import org.apache.http.impl.DefaultHttpClientConnection;
/*     */ import org.apache.http.impl.auth.BasicSchemeFactory;
/*     */ import org.apache.http.impl.auth.DigestSchemeFactory;
/*     */ import org.apache.http.impl.auth.KerberosSchemeFactory;
/*     */ import org.apache.http.impl.auth.NTLMSchemeFactory;
/*     */ import org.apache.http.impl.auth.SPNegoSchemeFactory;
/*     */ import org.apache.http.message.BasicHttpRequest;
/*     */ import org.apache.http.params.BasicHttpParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.params.HttpProtocolParams;
/*     */ import org.apache.http.protocol.BasicHttpContext;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.protocol.HttpProcessor;
/*     */ import org.apache.http.protocol.HttpRequestExecutor;
/*     */ import org.apache.http.protocol.ImmutableHttpProcessor;
/*     */ import org.apache.http.protocol.RequestContent;
/*     */ import org.apache.http.protocol.RequestTargetHost;
/*     */ import org.apache.http.protocol.RequestUserAgent;
/*     */ import org.apache.http.util.EntityUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProxyClient
/*     */ {
/*     */   private final HttpProcessor httpProcessor;
/*     */   private final HttpRequestExecutor requestExec;
/*     */   private final ProxyAuthenticationStrategy proxyAuthStrategy;
/*     */   private final HttpAuthenticator authenticator;
/*     */   private final AuthState proxyAuthState;
/*     */   private final AuthSchemeRegistry authSchemeRegistry;
/*     */   private final ConnectionReuseStrategy reuseStrategy;
/*     */   private final HttpParams params;
/*     */   
/*     */   public ProxyClient(HttpParams params) {
/*  90 */     if (params == null) {
/*  91 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*  93 */     this.httpProcessor = (HttpProcessor)new ImmutableHttpProcessor(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestContent(), (HttpRequestInterceptor)new RequestTargetHost(), (HttpRequestInterceptor)new RequestClientConnControl(), (HttpRequestInterceptor)new RequestUserAgent(), (HttpRequestInterceptor)new RequestProxyAuthentication() });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     this.requestExec = new HttpRequestExecutor();
/* 101 */     this.proxyAuthStrategy = new ProxyAuthenticationStrategy();
/* 102 */     this.authenticator = new HttpAuthenticator();
/* 103 */     this.proxyAuthState = new AuthState();
/* 104 */     this.authSchemeRegistry = new AuthSchemeRegistry();
/* 105 */     this.authSchemeRegistry.register("Basic", (AuthSchemeFactory)new BasicSchemeFactory());
/* 106 */     this.authSchemeRegistry.register("Digest", (AuthSchemeFactory)new DigestSchemeFactory());
/* 107 */     this.authSchemeRegistry.register("NTLM", (AuthSchemeFactory)new NTLMSchemeFactory());
/* 108 */     this.authSchemeRegistry.register("negotiate", (AuthSchemeFactory)new SPNegoSchemeFactory());
/* 109 */     this.authSchemeRegistry.register("Kerberos", (AuthSchemeFactory)new KerberosSchemeFactory());
/* 110 */     this.reuseStrategy = (ConnectionReuseStrategy)new DefaultConnectionReuseStrategy();
/* 111 */     this.params = params;
/*     */   }
/*     */   
/*     */   public ProxyClient() {
/* 115 */     this((HttpParams)new BasicHttpParams());
/*     */   }
/*     */   
/*     */   public HttpParams getParams() {
/* 119 */     return this.params;
/*     */   }
/*     */   
/*     */   public AuthSchemeRegistry getAuthSchemeRegistry() {
/* 123 */     return this.authSchemeRegistry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket tunnel(HttpHost proxy, HttpHost target, Credentials credentials) throws IOException, HttpException {
/* 130 */     ProxyConnection conn = new ProxyConnection(new HttpRoute(proxy));
/* 131 */     BasicHttpContext basicHttpContext = new BasicHttpContext();
/* 132 */     HttpResponse response = null;
/*     */     
/*     */     while (true) {
/* 135 */       if (!conn.isOpen()) {
/* 136 */         Socket socket = new Socket(proxy.getHostName(), proxy.getPort());
/* 137 */         conn.bind(socket, this.params);
/*     */       } 
/* 139 */       String host = target.getHostName();
/* 140 */       int port = target.getPort();
/* 141 */       if (port < 0) {
/* 142 */         port = 80;
/*     */       }
/*     */       
/* 145 */       StringBuilder buffer = new StringBuilder(host.length() + 6);
/* 146 */       buffer.append(host);
/* 147 */       buffer.append(':');
/* 148 */       buffer.append(Integer.toString(port));
/*     */       
/* 150 */       String authority = buffer.toString();
/* 151 */       ProtocolVersion ver = HttpProtocolParams.getVersion(this.params);
/* 152 */       BasicHttpRequest basicHttpRequest = new BasicHttpRequest("CONNECT", authority, ver);
/* 153 */       basicHttpRequest.setParams(this.params);
/*     */       
/* 155 */       BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
/* 156 */       credsProvider.setCredentials(new AuthScope(proxy), credentials);
/*     */ 
/*     */       
/* 159 */       basicHttpContext.setAttribute("http.target_host", target);
/* 160 */       basicHttpContext.setAttribute("http.proxy_host", proxy);
/* 161 */       basicHttpContext.setAttribute("http.connection", conn);
/* 162 */       basicHttpContext.setAttribute("http.request", basicHttpRequest);
/* 163 */       basicHttpContext.setAttribute("http.auth.proxy-scope", this.proxyAuthState);
/* 164 */       basicHttpContext.setAttribute("http.auth.credentials-provider", credsProvider);
/* 165 */       basicHttpContext.setAttribute("http.authscheme-registry", this.authSchemeRegistry);
/*     */       
/* 167 */       this.requestExec.preProcess((HttpRequest)basicHttpRequest, this.httpProcessor, (HttpContext)basicHttpContext);
/*     */       
/* 169 */       response = this.requestExec.execute((HttpRequest)basicHttpRequest, (HttpClientConnection)conn, (HttpContext)basicHttpContext);
/*     */       
/* 171 */       response.setParams(this.params);
/* 172 */       this.requestExec.postProcess(response, this.httpProcessor, (HttpContext)basicHttpContext);
/*     */       
/* 174 */       int i = response.getStatusLine().getStatusCode();
/* 175 */       if (i < 200) {
/* 176 */         throw new HttpException("Unexpected response to CONNECT request: " + response.getStatusLine());
/*     */       }
/*     */ 
/*     */       
/* 180 */       if (HttpClientParams.isAuthenticating(this.params)) {
/* 181 */         if (this.authenticator.isAuthenticationRequested(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, (HttpContext)basicHttpContext))
/*     */         {
/* 183 */           if (this.authenticator.authenticate(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, (HttpContext)basicHttpContext)) {
/*     */ 
/*     */             
/* 186 */             if (this.reuseStrategy.keepAlive(response, (HttpContext)basicHttpContext)) {
/*     */               
/* 188 */               HttpEntity entity = response.getEntity();
/* 189 */               EntityUtils.consume(entity); continue;
/*     */             } 
/* 191 */             conn.close();
/*     */ 
/*     */             
/*     */             continue;
/*     */           } 
/*     */         }
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/* 202 */     int status = response.getStatusLine().getStatusCode();
/*     */     
/* 204 */     if (status > 299) {
/*     */ 
/*     */       
/* 207 */       HttpEntity entity = response.getEntity();
/* 208 */       if (entity != null) {
/* 209 */         response.setEntity((HttpEntity)new BufferedHttpEntity(entity));
/*     */       }
/*     */       
/* 212 */       conn.close();
/* 213 */       throw new TunnelRefusedException("CONNECT refused by proxy: " + response.getStatusLine(), response);
/*     */     } 
/*     */     
/* 216 */     return conn.getSocket();
/*     */   }
/*     */   
/*     */   static class ProxyConnection
/*     */     extends DefaultHttpClientConnection
/*     */     implements HttpRoutedConnection {
/*     */     private final HttpRoute route;
/*     */     
/*     */     ProxyConnection(HttpRoute route) {
/* 225 */       this.route = route;
/*     */     }
/*     */     
/*     */     public HttpRoute getRoute() {
/* 229 */       return this.route;
/*     */     }
/*     */     
/*     */     public boolean isSecure() {
/* 233 */       return false;
/*     */     }
/*     */     
/*     */     public SSLSession getSSLSession() {
/* 237 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Socket getSocket() {
/* 242 */       return super.getSocket();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\ProxyClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */