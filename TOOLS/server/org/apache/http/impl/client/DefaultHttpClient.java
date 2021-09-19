/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.HttpResponseInterceptor;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.client.protocol.RequestAddCookies;
/*     */ import org.apache.http.client.protocol.RequestAuthCache;
/*     */ import org.apache.http.client.protocol.RequestClientConnControl;
/*     */ import org.apache.http.client.protocol.RequestDefaultHeaders;
/*     */ import org.apache.http.client.protocol.RequestProxyAuthentication;
/*     */ import org.apache.http.client.protocol.RequestTargetAuthentication;
/*     */ import org.apache.http.client.protocol.ResponseProcessCookies;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.params.HttpConnectionParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.params.HttpProtocolParams;
/*     */ import org.apache.http.params.SyncBasicHttpParams;
/*     */ import org.apache.http.protocol.BasicHttpProcessor;
/*     */ import org.apache.http.protocol.HTTP;
/*     */ import org.apache.http.protocol.RequestContent;
/*     */ import org.apache.http.protocol.RequestExpectContinue;
/*     */ import org.apache.http.protocol.RequestTargetHost;
/*     */ import org.apache.http.protocol.RequestUserAgent;
/*     */ import org.apache.http.util.VersionInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class DefaultHttpClient
/*     */   extends AbstractHttpClient
/*     */ {
/*     */   public DefaultHttpClient(ClientConnectionManager conman, HttpParams params) {
/* 127 */     super(conman, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttpClient(ClientConnectionManager conman) {
/* 136 */     super(conman, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttpClient(HttpParams params) {
/* 141 */     super(null, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttpClient() {
/* 146 */     super(null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpParams createHttpParams() {
/* 157 */     SyncBasicHttpParams syncBasicHttpParams = new SyncBasicHttpParams();
/* 158 */     setDefaultHttpParams((HttpParams)syncBasicHttpParams);
/* 159 */     return (HttpParams)syncBasicHttpParams;
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
/*     */   public static void setDefaultHttpParams(HttpParams params) {
/* 174 */     HttpProtocolParams.setVersion(params, (ProtocolVersion)HttpVersion.HTTP_1_1);
/* 175 */     HttpProtocolParams.setContentCharset(params, HTTP.DEF_CONTENT_CHARSET.name());
/* 176 */     HttpConnectionParams.setTcpNoDelay(params, true);
/* 177 */     HttpConnectionParams.setSocketBufferSize(params, 8192);
/*     */ 
/*     */     
/* 180 */     VersionInfo vi = VersionInfo.loadVersionInfo("org.apache.http.client", DefaultHttpClient.class.getClassLoader());
/*     */     
/* 182 */     String release = (vi != null) ? vi.getRelease() : "UNAVAILABLE";
/*     */     
/* 184 */     HttpProtocolParams.setUserAgent(params, "Apache-HttpClient/" + release + " (java 1.5)");
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
/*     */   protected BasicHttpProcessor createHttpProcessor() {
/* 208 */     BasicHttpProcessor httpproc = new BasicHttpProcessor();
/* 209 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestDefaultHeaders());
/*     */     
/* 211 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestContent());
/* 212 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestTargetHost());
/*     */     
/* 214 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestClientConnControl());
/* 215 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestUserAgent());
/* 216 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestExpectContinue());
/*     */     
/* 218 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestAddCookies());
/* 219 */     httpproc.addInterceptor((HttpResponseInterceptor)new ResponseProcessCookies());
/*     */     
/* 221 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestAuthCache());
/* 222 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestTargetAuthentication());
/* 223 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestProxyAuthentication());
/* 224 */     return httpproc;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\DefaultHttpClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */