/*      */ package org.apache.http.impl.client;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.UndeclaredThrowableException;
/*      */ import java.net.URI;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.apache.http.ConnectionReuseStrategy;
/*      */ import org.apache.http.HttpEntity;
/*      */ import org.apache.http.HttpException;
/*      */ import org.apache.http.HttpHost;
/*      */ import org.apache.http.HttpRequest;
/*      */ import org.apache.http.HttpRequestInterceptor;
/*      */ import org.apache.http.HttpResponse;
/*      */ import org.apache.http.HttpResponseInterceptor;
/*      */ import org.apache.http.annotation.GuardedBy;
/*      */ import org.apache.http.annotation.ThreadSafe;
/*      */ import org.apache.http.auth.AuthSchemeFactory;
/*      */ import org.apache.http.auth.AuthSchemeRegistry;
/*      */ import org.apache.http.client.AuthenticationHandler;
/*      */ import org.apache.http.client.AuthenticationStrategy;
/*      */ import org.apache.http.client.BackoffManager;
/*      */ import org.apache.http.client.ClientProtocolException;
/*      */ import org.apache.http.client.ConnectionBackoffStrategy;
/*      */ import org.apache.http.client.CookieStore;
/*      */ import org.apache.http.client.CredentialsProvider;
/*      */ import org.apache.http.client.HttpClient;
/*      */ import org.apache.http.client.HttpRequestRetryHandler;
/*      */ import org.apache.http.client.RedirectHandler;
/*      */ import org.apache.http.client.RedirectStrategy;
/*      */ import org.apache.http.client.RequestDirector;
/*      */ import org.apache.http.client.ResponseHandler;
/*      */ import org.apache.http.client.UserTokenHandler;
/*      */ import org.apache.http.client.methods.HttpUriRequest;
/*      */ import org.apache.http.client.utils.URIUtils;
/*      */ import org.apache.http.conn.ClientConnectionManager;
/*      */ import org.apache.http.conn.ClientConnectionManagerFactory;
/*      */ import org.apache.http.conn.ConnectionKeepAliveStrategy;
/*      */ import org.apache.http.conn.routing.HttpRoute;
/*      */ import org.apache.http.conn.routing.HttpRoutePlanner;
/*      */ import org.apache.http.conn.scheme.SchemeRegistry;
/*      */ import org.apache.http.cookie.CookieSpecFactory;
/*      */ import org.apache.http.cookie.CookieSpecRegistry;
/*      */ import org.apache.http.impl.DefaultConnectionReuseStrategy;
/*      */ import org.apache.http.impl.auth.BasicSchemeFactory;
/*      */ import org.apache.http.impl.auth.DigestSchemeFactory;
/*      */ import org.apache.http.impl.auth.KerberosSchemeFactory;
/*      */ import org.apache.http.impl.auth.NTLMSchemeFactory;
/*      */ import org.apache.http.impl.auth.SPNegoSchemeFactory;
/*      */ import org.apache.http.impl.conn.BasicClientConnectionManager;
/*      */ import org.apache.http.impl.conn.DefaultHttpRoutePlanner;
/*      */ import org.apache.http.impl.conn.SchemeRegistryFactory;
/*      */ import org.apache.http.impl.cookie.BestMatchSpecFactory;
/*      */ import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
/*      */ import org.apache.http.impl.cookie.IgnoreSpecFactory;
/*      */ import org.apache.http.impl.cookie.NetscapeDraftSpecFactory;
/*      */ import org.apache.http.impl.cookie.RFC2109SpecFactory;
/*      */ import org.apache.http.impl.cookie.RFC2965SpecFactory;
/*      */ import org.apache.http.params.HttpParams;
/*      */ import org.apache.http.protocol.BasicHttpContext;
/*      */ import org.apache.http.protocol.BasicHttpProcessor;
/*      */ import org.apache.http.protocol.DefaultedHttpContext;
/*      */ import org.apache.http.protocol.HttpContext;
/*      */ import org.apache.http.protocol.HttpProcessor;
/*      */ import org.apache.http.protocol.HttpRequestExecutor;
/*      */ import org.apache.http.protocol.ImmutableHttpProcessor;
/*      */ import org.apache.http.util.EntityUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @ThreadSafe
/*      */ public abstract class AbstractHttpClient
/*      */   implements HttpClient
/*      */ {
/*  187 */   private final Log log = LogFactory.getLog(getClass());
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private HttpParams defaultParams;
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private HttpRequestExecutor requestExec;
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private ClientConnectionManager connManager;
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private ConnectionReuseStrategy reuseStrategy;
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private ConnectionKeepAliveStrategy keepAliveStrategy;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private CookieSpecRegistry supportedCookieSpecs;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private AuthSchemeRegistry supportedAuthSchemes;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private BasicHttpProcessor mutableProcessor;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private ImmutableHttpProcessor protocolProcessor;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private HttpRequestRetryHandler retryHandler;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private RedirectStrategy redirectStrategy;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private AuthenticationStrategy targetAuthStrategy;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private AuthenticationStrategy proxyAuthStrategy;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private CookieStore cookieStore;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private CredentialsProvider credsProvider;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private HttpRoutePlanner routePlanner;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private UserTokenHandler userTokenHandler;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private ConnectionBackoffStrategy connectionBackoffStrategy;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private BackoffManager backoffManager;
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractHttpClient(ClientConnectionManager conman, HttpParams params) {
/*  273 */     this.defaultParams = params;
/*  274 */     this.connManager = conman;
/*      */   }
/*      */ 
/*      */   
/*      */   protected abstract HttpParams createHttpParams();
/*      */ 
/*      */   
/*      */   protected abstract BasicHttpProcessor createHttpProcessor();
/*      */ 
/*      */   
/*      */   protected HttpContext createHttpContext() {
/*  285 */     BasicHttpContext basicHttpContext = new BasicHttpContext();
/*  286 */     basicHttpContext.setAttribute("http.scheme-registry", getConnectionManager().getSchemeRegistry());
/*      */ 
/*      */     
/*  289 */     basicHttpContext.setAttribute("http.authscheme-registry", getAuthSchemes());
/*      */ 
/*      */     
/*  292 */     basicHttpContext.setAttribute("http.cookiespec-registry", getCookieSpecs());
/*      */ 
/*      */     
/*  295 */     basicHttpContext.setAttribute("http.cookie-store", getCookieStore());
/*      */ 
/*      */     
/*  298 */     basicHttpContext.setAttribute("http.auth.credentials-provider", getCredentialsProvider());
/*      */ 
/*      */     
/*  301 */     return (HttpContext)basicHttpContext;
/*      */   }
/*      */   
/*      */   protected ClientConnectionManager createClientConnectionManager() {
/*      */     BasicClientConnectionManager basicClientConnectionManager;
/*  306 */     SchemeRegistry registry = SchemeRegistryFactory.createDefault();
/*      */     
/*  308 */     ClientConnectionManager connManager = null;
/*  309 */     HttpParams params = getParams();
/*      */     
/*  311 */     ClientConnectionManagerFactory factory = null;
/*      */     
/*  313 */     String className = (String)params.getParameter("http.connection-manager.factory-class-name");
/*      */     
/*  315 */     if (className != null) {
/*      */       try {
/*  317 */         Class<?> clazz = Class.forName(className);
/*  318 */         factory = (ClientConnectionManagerFactory)clazz.newInstance();
/*  319 */       } catch (ClassNotFoundException ex) {
/*  320 */         throw new IllegalStateException("Invalid class name: " + className);
/*  321 */       } catch (IllegalAccessException ex) {
/*  322 */         throw new IllegalAccessError(ex.getMessage());
/*  323 */       } catch (InstantiationException ex) {
/*  324 */         throw new InstantiationError(ex.getMessage());
/*      */       } 
/*      */     }
/*  327 */     if (factory != null) {
/*  328 */       connManager = factory.newInstance(params, registry);
/*      */     } else {
/*  330 */       basicClientConnectionManager = new BasicClientConnectionManager(registry);
/*      */     } 
/*      */     
/*  333 */     return (ClientConnectionManager)basicClientConnectionManager;
/*      */   }
/*      */ 
/*      */   
/*      */   protected AuthSchemeRegistry createAuthSchemeRegistry() {
/*  338 */     AuthSchemeRegistry registry = new AuthSchemeRegistry();
/*  339 */     registry.register("Basic", (AuthSchemeFactory)new BasicSchemeFactory());
/*      */ 
/*      */     
/*  342 */     registry.register("Digest", (AuthSchemeFactory)new DigestSchemeFactory());
/*      */ 
/*      */     
/*  345 */     registry.register("NTLM", (AuthSchemeFactory)new NTLMSchemeFactory());
/*      */ 
/*      */     
/*  348 */     registry.register("negotiate", (AuthSchemeFactory)new SPNegoSchemeFactory());
/*      */ 
/*      */     
/*  351 */     registry.register("Kerberos", (AuthSchemeFactory)new KerberosSchemeFactory());
/*      */ 
/*      */     
/*  354 */     return registry;
/*      */   }
/*      */ 
/*      */   
/*      */   protected CookieSpecRegistry createCookieSpecRegistry() {
/*  359 */     CookieSpecRegistry registry = new CookieSpecRegistry();
/*  360 */     registry.register("best-match", (CookieSpecFactory)new BestMatchSpecFactory());
/*      */ 
/*      */     
/*  363 */     registry.register("compatibility", (CookieSpecFactory)new BrowserCompatSpecFactory());
/*      */ 
/*      */     
/*  366 */     registry.register("netscape", (CookieSpecFactory)new NetscapeDraftSpecFactory());
/*      */ 
/*      */     
/*  369 */     registry.register("rfc2109", (CookieSpecFactory)new RFC2109SpecFactory());
/*      */ 
/*      */     
/*  372 */     registry.register("rfc2965", (CookieSpecFactory)new RFC2965SpecFactory());
/*      */ 
/*      */     
/*  375 */     registry.register("ignoreCookies", (CookieSpecFactory)new IgnoreSpecFactory());
/*      */ 
/*      */     
/*  378 */     return registry;
/*      */   }
/*      */   
/*      */   protected HttpRequestExecutor createRequestExecutor() {
/*  382 */     return new HttpRequestExecutor();
/*      */   }
/*      */   
/*      */   protected ConnectionReuseStrategy createConnectionReuseStrategy() {
/*  386 */     return (ConnectionReuseStrategy)new DefaultConnectionReuseStrategy();
/*      */   }
/*      */   
/*      */   protected ConnectionKeepAliveStrategy createConnectionKeepAliveStrategy() {
/*  390 */     return new DefaultConnectionKeepAliveStrategy();
/*      */   }
/*      */   
/*      */   protected HttpRequestRetryHandler createHttpRequestRetryHandler() {
/*  394 */     return new DefaultHttpRequestRetryHandler();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected RedirectHandler createRedirectHandler() {
/*  402 */     return new DefaultRedirectHandler();
/*      */   }
/*      */   
/*      */   protected AuthenticationStrategy createTargetAuthenticationStrategy() {
/*  406 */     return new TargetAuthenticationStrategy();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected AuthenticationHandler createTargetAuthenticationHandler() {
/*  414 */     return new DefaultTargetAuthenticationHandler();
/*      */   }
/*      */   
/*      */   protected AuthenticationStrategy createProxyAuthenticationStrategy() {
/*  418 */     return new ProxyAuthenticationStrategy();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected AuthenticationHandler createProxyAuthenticationHandler() {
/*  426 */     return new DefaultProxyAuthenticationHandler();
/*      */   }
/*      */   
/*      */   protected CookieStore createCookieStore() {
/*  430 */     return new BasicCookieStore();
/*      */   }
/*      */   
/*      */   protected CredentialsProvider createCredentialsProvider() {
/*  434 */     return new BasicCredentialsProvider();
/*      */   }
/*      */   
/*      */   protected HttpRoutePlanner createHttpRoutePlanner() {
/*  438 */     return (HttpRoutePlanner)new DefaultHttpRoutePlanner(getConnectionManager().getSchemeRegistry());
/*      */   }
/*      */   
/*      */   protected UserTokenHandler createUserTokenHandler() {
/*  442 */     return new DefaultUserTokenHandler();
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized HttpParams getParams() {
/*  447 */     if (this.defaultParams == null) {
/*  448 */       this.defaultParams = createHttpParams();
/*      */     }
/*  450 */     return this.defaultParams;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setParams(HttpParams params) {
/*  460 */     this.defaultParams = params;
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized ClientConnectionManager getConnectionManager() {
/*  465 */     if (this.connManager == null) {
/*  466 */       this.connManager = createClientConnectionManager();
/*      */     }
/*  468 */     return this.connManager;
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized HttpRequestExecutor getRequestExecutor() {
/*  473 */     if (this.requestExec == null) {
/*  474 */       this.requestExec = createRequestExecutor();
/*      */     }
/*  476 */     return this.requestExec;
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized AuthSchemeRegistry getAuthSchemes() {
/*  481 */     if (this.supportedAuthSchemes == null) {
/*  482 */       this.supportedAuthSchemes = createAuthSchemeRegistry();
/*      */     }
/*  484 */     return this.supportedAuthSchemes;
/*      */   }
/*      */   
/*      */   public synchronized void setAuthSchemes(AuthSchemeRegistry registry) {
/*  488 */     this.supportedAuthSchemes = registry;
/*      */   }
/*      */   
/*      */   public final synchronized ConnectionBackoffStrategy getConnectionBackoffStrategy() {
/*  492 */     return this.connectionBackoffStrategy;
/*      */   }
/*      */   
/*      */   public synchronized void setConnectionBackoffStrategy(ConnectionBackoffStrategy strategy) {
/*  496 */     this.connectionBackoffStrategy = strategy;
/*      */   }
/*      */   
/*      */   public final synchronized CookieSpecRegistry getCookieSpecs() {
/*  500 */     if (this.supportedCookieSpecs == null) {
/*  501 */       this.supportedCookieSpecs = createCookieSpecRegistry();
/*      */     }
/*  503 */     return this.supportedCookieSpecs;
/*      */   }
/*      */   
/*      */   public final synchronized BackoffManager getBackoffManager() {
/*  507 */     return this.backoffManager;
/*      */   }
/*      */   
/*      */   public synchronized void setBackoffManager(BackoffManager manager) {
/*  511 */     this.backoffManager = manager;
/*      */   }
/*      */   
/*      */   public synchronized void setCookieSpecs(CookieSpecRegistry registry) {
/*  515 */     this.supportedCookieSpecs = registry;
/*      */   }
/*      */   
/*      */   public final synchronized ConnectionReuseStrategy getConnectionReuseStrategy() {
/*  519 */     if (this.reuseStrategy == null) {
/*  520 */       this.reuseStrategy = createConnectionReuseStrategy();
/*      */     }
/*  522 */     return this.reuseStrategy;
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setReuseStrategy(ConnectionReuseStrategy strategy) {
/*  527 */     this.reuseStrategy = strategy;
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized ConnectionKeepAliveStrategy getConnectionKeepAliveStrategy() {
/*  532 */     if (this.keepAliveStrategy == null) {
/*  533 */       this.keepAliveStrategy = createConnectionKeepAliveStrategy();
/*      */     }
/*  535 */     return this.keepAliveStrategy;
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setKeepAliveStrategy(ConnectionKeepAliveStrategy strategy) {
/*  540 */     this.keepAliveStrategy = strategy;
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized HttpRequestRetryHandler getHttpRequestRetryHandler() {
/*  545 */     if (this.retryHandler == null) {
/*  546 */       this.retryHandler = createHttpRequestRetryHandler();
/*      */     }
/*  548 */     return this.retryHandler;
/*      */   }
/*      */   
/*      */   public synchronized void setHttpRequestRetryHandler(HttpRequestRetryHandler handler) {
/*  552 */     this.retryHandler = handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final synchronized RedirectHandler getRedirectHandler() {
/*  560 */     return createRedirectHandler();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public synchronized void setRedirectHandler(RedirectHandler handler) {
/*  568 */     this.redirectStrategy = new DefaultRedirectStrategyAdaptor(handler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized RedirectStrategy getRedirectStrategy() {
/*  575 */     if (this.redirectStrategy == null) {
/*  576 */       this.redirectStrategy = new DefaultRedirectStrategy();
/*      */     }
/*  578 */     return this.redirectStrategy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setRedirectStrategy(RedirectStrategy strategy) {
/*  585 */     this.redirectStrategy = strategy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final synchronized AuthenticationHandler getTargetAuthenticationHandler() {
/*  593 */     return createTargetAuthenticationHandler();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public synchronized void setTargetAuthenticationHandler(AuthenticationHandler handler) {
/*  601 */     this.targetAuthStrategy = new AuthenticationStrategyAdaptor(handler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized AuthenticationStrategy getTargetAuthenticationStrategy() {
/*  608 */     if (this.targetAuthStrategy == null) {
/*  609 */       this.targetAuthStrategy = createTargetAuthenticationStrategy();
/*      */     }
/*  611 */     return this.targetAuthStrategy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setTargetAuthenticationStrategy(AuthenticationStrategy strategy) {
/*  618 */     this.targetAuthStrategy = strategy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final synchronized AuthenticationHandler getProxyAuthenticationHandler() {
/*  626 */     return createProxyAuthenticationHandler();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public synchronized void setProxyAuthenticationHandler(AuthenticationHandler handler) {
/*  634 */     this.proxyAuthStrategy = new AuthenticationStrategyAdaptor(handler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized AuthenticationStrategy getProxyAuthenticationStrategy() {
/*  641 */     if (this.proxyAuthStrategy == null) {
/*  642 */       this.proxyAuthStrategy = createProxyAuthenticationStrategy();
/*      */     }
/*  644 */     return this.proxyAuthStrategy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setProxyAuthenticationStrategy(AuthenticationStrategy strategy) {
/*  651 */     this.proxyAuthStrategy = strategy;
/*      */   }
/*      */   
/*      */   public final synchronized CookieStore getCookieStore() {
/*  655 */     if (this.cookieStore == null) {
/*  656 */       this.cookieStore = createCookieStore();
/*      */     }
/*  658 */     return this.cookieStore;
/*      */   }
/*      */   
/*      */   public synchronized void setCookieStore(CookieStore cookieStore) {
/*  662 */     this.cookieStore = cookieStore;
/*      */   }
/*      */   
/*      */   public final synchronized CredentialsProvider getCredentialsProvider() {
/*  666 */     if (this.credsProvider == null) {
/*  667 */       this.credsProvider = createCredentialsProvider();
/*      */     }
/*  669 */     return this.credsProvider;
/*      */   }
/*      */   
/*      */   public synchronized void setCredentialsProvider(CredentialsProvider credsProvider) {
/*  673 */     this.credsProvider = credsProvider;
/*      */   }
/*      */   
/*      */   public final synchronized HttpRoutePlanner getRoutePlanner() {
/*  677 */     if (this.routePlanner == null) {
/*  678 */       this.routePlanner = createHttpRoutePlanner();
/*      */     }
/*  680 */     return this.routePlanner;
/*      */   }
/*      */   
/*      */   public synchronized void setRoutePlanner(HttpRoutePlanner routePlanner) {
/*  684 */     this.routePlanner = routePlanner;
/*      */   }
/*      */   
/*      */   public final synchronized UserTokenHandler getUserTokenHandler() {
/*  688 */     if (this.userTokenHandler == null) {
/*  689 */       this.userTokenHandler = createUserTokenHandler();
/*      */     }
/*  691 */     return this.userTokenHandler;
/*      */   }
/*      */   
/*      */   public synchronized void setUserTokenHandler(UserTokenHandler handler) {
/*  695 */     this.userTokenHandler = handler;
/*      */   }
/*      */   
/*      */   protected final synchronized BasicHttpProcessor getHttpProcessor() {
/*  699 */     if (this.mutableProcessor == null) {
/*  700 */       this.mutableProcessor = createHttpProcessor();
/*      */     }
/*  702 */     return this.mutableProcessor;
/*      */   }
/*      */   
/*      */   private final synchronized HttpProcessor getProtocolProcessor() {
/*  706 */     if (this.protocolProcessor == null) {
/*      */       
/*  708 */       BasicHttpProcessor proc = getHttpProcessor();
/*      */       
/*  710 */       int reqc = proc.getRequestInterceptorCount();
/*  711 */       HttpRequestInterceptor[] reqinterceptors = new HttpRequestInterceptor[reqc];
/*  712 */       for (int i = 0; i < reqc; i++) {
/*  713 */         reqinterceptors[i] = proc.getRequestInterceptor(i);
/*      */       }
/*  715 */       int resc = proc.getResponseInterceptorCount();
/*  716 */       HttpResponseInterceptor[] resinterceptors = new HttpResponseInterceptor[resc];
/*  717 */       for (int j = 0; j < resc; j++) {
/*  718 */         resinterceptors[j] = proc.getResponseInterceptor(j);
/*      */       }
/*  720 */       this.protocolProcessor = new ImmutableHttpProcessor(reqinterceptors, resinterceptors);
/*      */     } 
/*  722 */     return (HttpProcessor)this.protocolProcessor;
/*      */   }
/*      */   
/*      */   public synchronized int getResponseInterceptorCount() {
/*  726 */     return getHttpProcessor().getResponseInterceptorCount();
/*      */   }
/*      */   
/*      */   public synchronized HttpResponseInterceptor getResponseInterceptor(int index) {
/*  730 */     return getHttpProcessor().getResponseInterceptor(index);
/*      */   }
/*      */   
/*      */   public synchronized HttpRequestInterceptor getRequestInterceptor(int index) {
/*  734 */     return getHttpProcessor().getRequestInterceptor(index);
/*      */   }
/*      */   
/*      */   public synchronized int getRequestInterceptorCount() {
/*  738 */     return getHttpProcessor().getRequestInterceptorCount();
/*      */   }
/*      */   
/*      */   public synchronized void addResponseInterceptor(HttpResponseInterceptor itcp) {
/*  742 */     getHttpProcessor().addInterceptor(itcp);
/*  743 */     this.protocolProcessor = null;
/*      */   }
/*      */   
/*      */   public synchronized void addResponseInterceptor(HttpResponseInterceptor itcp, int index) {
/*  747 */     getHttpProcessor().addInterceptor(itcp, index);
/*  748 */     this.protocolProcessor = null;
/*      */   }
/*      */   
/*      */   public synchronized void clearResponseInterceptors() {
/*  752 */     getHttpProcessor().clearResponseInterceptors();
/*  753 */     this.protocolProcessor = null;
/*      */   }
/*      */   
/*      */   public synchronized void removeResponseInterceptorByClass(Class<? extends HttpResponseInterceptor> clazz) {
/*  757 */     getHttpProcessor().removeResponseInterceptorByClass(clazz);
/*  758 */     this.protocolProcessor = null;
/*      */   }
/*      */   
/*      */   public synchronized void addRequestInterceptor(HttpRequestInterceptor itcp) {
/*  762 */     getHttpProcessor().addInterceptor(itcp);
/*  763 */     this.protocolProcessor = null;
/*      */   }
/*      */   
/*      */   public synchronized void addRequestInterceptor(HttpRequestInterceptor itcp, int index) {
/*  767 */     getHttpProcessor().addInterceptor(itcp, index);
/*  768 */     this.protocolProcessor = null;
/*      */   }
/*      */   
/*      */   public synchronized void clearRequestInterceptors() {
/*  772 */     getHttpProcessor().clearRequestInterceptors();
/*  773 */     this.protocolProcessor = null;
/*      */   }
/*      */   
/*      */   public synchronized void removeRequestInterceptorByClass(Class<? extends HttpRequestInterceptor> clazz) {
/*  777 */     getHttpProcessor().removeRequestInterceptorByClass(clazz);
/*  778 */     this.protocolProcessor = null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException {
/*  784 */     return execute(request, (HttpContext)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException, ClientProtocolException {
/*  800 */     if (request == null) {
/*  801 */       throw new IllegalArgumentException("Request must not be null.");
/*      */     }
/*      */ 
/*      */     
/*  805 */     return execute(determineTarget(request), (HttpRequest)request, context);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static HttpHost determineTarget(HttpUriRequest request) throws ClientProtocolException {
/*  811 */     HttpHost target = null;
/*      */     
/*  813 */     URI requestURI = request.getURI();
/*  814 */     if (requestURI.isAbsolute()) {
/*  815 */       target = URIUtils.extractHost(requestURI);
/*  816 */       if (target == null) {
/*  817 */         throw new ClientProtocolException("URI does not specify a valid host name: " + requestURI);
/*      */       }
/*      */     } 
/*      */     
/*  821 */     return target;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpResponse execute(HttpHost target, HttpRequest request) throws IOException, ClientProtocolException {
/*  827 */     return execute(target, request, (HttpContext)null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
/*      */     DefaultedHttpContext defaultedHttpContext;
/*  834 */     if (request == null) {
/*  835 */       throw new IllegalArgumentException("Request must not be null.");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  841 */     HttpContext execContext = null;
/*  842 */     RequestDirector director = null;
/*  843 */     HttpRoutePlanner routePlanner = null;
/*  844 */     ConnectionBackoffStrategy connectionBackoffStrategy = null;
/*  845 */     BackoffManager backoffManager = null;
/*      */ 
/*      */ 
/*      */     
/*  849 */     synchronized (this) {
/*      */       
/*  851 */       HttpContext defaultContext = createHttpContext();
/*  852 */       if (context == null) {
/*  853 */         execContext = defaultContext;
/*      */       } else {
/*  855 */         defaultedHttpContext = new DefaultedHttpContext(context, defaultContext);
/*      */       } 
/*      */       
/*  858 */       director = createClientRequestDirector(getRequestExecutor(), getConnectionManager(), getConnectionReuseStrategy(), getConnectionKeepAliveStrategy(), getRoutePlanner(), getProtocolProcessor(), getHttpRequestRetryHandler(), getRedirectStrategy(), getTargetAuthenticationStrategy(), getProxyAuthenticationStrategy(), getUserTokenHandler(), determineParams(request));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  871 */       routePlanner = getRoutePlanner();
/*  872 */       connectionBackoffStrategy = getConnectionBackoffStrategy();
/*  873 */       backoffManager = getBackoffManager();
/*      */     } 
/*      */     
/*      */     try {
/*  877 */       if (connectionBackoffStrategy != null && backoffManager != null) {
/*  878 */         HttpResponse out; HttpHost targetForRoute = (target != null) ? target : (HttpHost)determineParams(request).getParameter("http.default-host");
/*      */ 
/*      */         
/*  881 */         HttpRoute route = routePlanner.determineRoute(targetForRoute, request, (HttpContext)defaultedHttpContext);
/*      */ 
/*      */         
/*      */         try {
/*  885 */           out = director.execute(target, request, (HttpContext)defaultedHttpContext);
/*  886 */         } catch (RuntimeException re) {
/*  887 */           if (connectionBackoffStrategy.shouldBackoff(re)) {
/*  888 */             backoffManager.backOff(route);
/*      */           }
/*  890 */           throw re;
/*  891 */         } catch (Exception e) {
/*  892 */           if (connectionBackoffStrategy.shouldBackoff(e)) {
/*  893 */             backoffManager.backOff(route);
/*      */           }
/*  895 */           if (e instanceof HttpException) throw (HttpException)e; 
/*  896 */           if (e instanceof IOException) throw (IOException)e; 
/*  897 */           throw new UndeclaredThrowableException(e);
/*      */         } 
/*  899 */         if (connectionBackoffStrategy.shouldBackoff(out)) {
/*  900 */           backoffManager.backOff(route);
/*      */         } else {
/*  902 */           backoffManager.probe(route);
/*      */         } 
/*  904 */         return out;
/*      */       } 
/*  906 */       return director.execute(target, request, (HttpContext)defaultedHttpContext);
/*      */     }
/*  908 */     catch (HttpException httpException) {
/*  909 */       throw new ClientProtocolException(httpException);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected RequestDirector createClientRequestDirector(HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectHandler redirectHandler, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler userTokenHandler, HttpParams params) {
/*  930 */     return new DefaultRequestDirector(requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, redirectHandler, targetAuthHandler, proxyAuthHandler, userTokenHandler, params);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected RequestDirector createClientRequestDirector(HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectStrategy redirectStrategy, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler userTokenHandler, HttpParams params) {
/*  962 */     return new DefaultRequestDirector(this.log, requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, redirectStrategy, targetAuthHandler, proxyAuthHandler, userTokenHandler, params);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected RequestDirector createClientRequestDirector(HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectStrategy redirectStrategy, AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, UserTokenHandler userTokenHandler, HttpParams params) {
/*  995 */     return new DefaultRequestDirector(this.log, requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, redirectStrategy, targetAuthStrategy, proxyAuthStrategy, userTokenHandler, params);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected HttpParams determineParams(HttpRequest req) {
/* 1027 */     return (HttpParams)new ClientParamsStack(null, getParams(), req.getParams(), null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
/* 1050 */     return execute(request, responseHandler, (HttpContext)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
/* 1075 */     HttpHost target = determineTarget(request);
/* 1076 */     return execute(target, (HttpRequest)request, responseHandler, context);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
/* 1103 */     return execute(target, request, responseHandler, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
/*      */     T result;
/* 1133 */     if (responseHandler == null) {
/* 1134 */       throw new IllegalArgumentException("Response handler must not be null.");
/*      */     }
/*      */ 
/*      */     
/* 1138 */     HttpResponse response = execute(target, request, context);
/*      */ 
/*      */     
/*      */     try {
/* 1142 */       result = (T)responseHandler.handleResponse(response);
/* 1143 */     } catch (Exception t) {
/* 1144 */       HttpEntity httpEntity = response.getEntity();
/*      */       try {
/* 1146 */         EntityUtils.consume(httpEntity);
/* 1147 */       } catch (Exception t2) {
/*      */ 
/*      */         
/* 1150 */         this.log.warn("Error consuming content after an exception.", t2);
/*      */       } 
/* 1152 */       if (t instanceof RuntimeException) {
/* 1153 */         throw (RuntimeException)t;
/*      */       }
/* 1155 */       if (t instanceof IOException) {
/* 1156 */         throw (IOException)t;
/*      */       }
/* 1158 */       throw new UndeclaredThrowableException(t);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1163 */     HttpEntity entity = response.getEntity();
/* 1164 */     EntityUtils.consume(entity);
/* 1165 */     return result;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\AbstractHttpClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */