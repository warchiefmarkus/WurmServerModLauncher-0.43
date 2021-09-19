/*      */ package org.apache.http.impl.client;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InterruptedIOException;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.apache.http.ConnectionReuseStrategy;
/*      */ import org.apache.http.HttpClientConnection;
/*      */ import org.apache.http.HttpEntity;
/*      */ import org.apache.http.HttpEntityEnclosingRequest;
/*      */ import org.apache.http.HttpException;
/*      */ import org.apache.http.HttpHost;
/*      */ import org.apache.http.HttpRequest;
/*      */ import org.apache.http.HttpResponse;
/*      */ import org.apache.http.ProtocolException;
/*      */ import org.apache.http.ProtocolVersion;
/*      */ import org.apache.http.annotation.NotThreadSafe;
/*      */ import org.apache.http.auth.AuthProtocolState;
/*      */ import org.apache.http.auth.AuthScheme;
/*      */ import org.apache.http.auth.AuthState;
/*      */ import org.apache.http.auth.Credentials;
/*      */ import org.apache.http.auth.UsernamePasswordCredentials;
/*      */ import org.apache.http.client.AuthenticationHandler;
/*      */ import org.apache.http.client.AuthenticationStrategy;
/*      */ import org.apache.http.client.HttpRequestRetryHandler;
/*      */ import org.apache.http.client.NonRepeatableRequestException;
/*      */ import org.apache.http.client.RedirectException;
/*      */ import org.apache.http.client.RedirectHandler;
/*      */ import org.apache.http.client.RedirectStrategy;
/*      */ import org.apache.http.client.RequestDirector;
/*      */ import org.apache.http.client.UserTokenHandler;
/*      */ import org.apache.http.client.methods.AbortableHttpRequest;
/*      */ import org.apache.http.client.methods.HttpUriRequest;
/*      */ import org.apache.http.client.params.HttpClientParams;
/*      */ import org.apache.http.client.utils.URIUtils;
/*      */ import org.apache.http.conn.BasicManagedEntity;
/*      */ import org.apache.http.conn.ClientConnectionManager;
/*      */ import org.apache.http.conn.ClientConnectionRequest;
/*      */ import org.apache.http.conn.ConnectionKeepAliveStrategy;
/*      */ import org.apache.http.conn.ConnectionReleaseTrigger;
/*      */ import org.apache.http.conn.ManagedClientConnection;
/*      */ import org.apache.http.conn.routing.BasicRouteDirector;
/*      */ import org.apache.http.conn.routing.HttpRoute;
/*      */ import org.apache.http.conn.routing.HttpRoutePlanner;
/*      */ import org.apache.http.conn.routing.RouteInfo;
/*      */ import org.apache.http.conn.scheme.Scheme;
/*      */ import org.apache.http.entity.BufferedHttpEntity;
/*      */ import org.apache.http.impl.auth.BasicScheme;
/*      */ import org.apache.http.impl.conn.ConnectionShutdownException;
/*      */ import org.apache.http.message.BasicHttpRequest;
/*      */ import org.apache.http.params.HttpConnectionParams;
/*      */ import org.apache.http.params.HttpParams;
/*      */ import org.apache.http.params.HttpProtocolParams;
/*      */ import org.apache.http.protocol.HttpContext;
/*      */ import org.apache.http.protocol.HttpProcessor;
/*      */ import org.apache.http.protocol.HttpRequestExecutor;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @NotThreadSafe
/*      */ public class DefaultRequestDirector
/*      */   implements RequestDirector
/*      */ {
/*      */   private final Log log;
/*      */   protected final ClientConnectionManager connManager;
/*      */   protected final HttpRoutePlanner routePlanner;
/*      */   protected final ConnectionReuseStrategy reuseStrategy;
/*      */   protected final ConnectionKeepAliveStrategy keepAliveStrategy;
/*      */   protected final HttpRequestExecutor requestExec;
/*      */   protected final HttpProcessor httpProcessor;
/*      */   protected final HttpRequestRetryHandler retryHandler;
/*      */   @Deprecated
/*      */   protected final RedirectHandler redirectHandler;
/*      */   protected final RedirectStrategy redirectStrategy;
/*      */   @Deprecated
/*      */   protected final AuthenticationHandler targetAuthHandler;
/*      */   protected final AuthenticationStrategy targetAuthStrategy;
/*      */   @Deprecated
/*      */   protected final AuthenticationHandler proxyAuthHandler;
/*      */   protected final AuthenticationStrategy proxyAuthStrategy;
/*      */   protected final UserTokenHandler userTokenHandler;
/*      */   protected final HttpParams params;
/*      */   protected ManagedClientConnection managedConn;
/*      */   protected final AuthState targetAuthState;
/*      */   protected final AuthState proxyAuthState;
/*      */   private final HttpAuthenticator authenticator;
/*      */   private int execCount;
/*      */   private int redirectCount;
/*      */   private int maxRedirects;
/*      */   private HttpHost virtualHost;
/*      */   
/*      */   @Deprecated
/*      */   public DefaultRequestDirector(HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectHandler redirectHandler, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler userTokenHandler, HttpParams params) {
/*  215 */     this(LogFactory.getLog(DefaultRequestDirector.class), requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, new DefaultRedirectStrategyAdaptor(redirectHandler), new AuthenticationStrategyAdaptor(targetAuthHandler), new AuthenticationStrategyAdaptor(proxyAuthHandler), userTokenHandler, params);
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
/*      */   @Deprecated
/*      */   public DefaultRequestDirector(Log log, HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectStrategy redirectStrategy, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler userTokenHandler, HttpParams params) {
/*  240 */     this(LogFactory.getLog(DefaultRequestDirector.class), requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, redirectStrategy, new AuthenticationStrategyAdaptor(targetAuthHandler), new AuthenticationStrategyAdaptor(proxyAuthHandler), userTokenHandler, params);
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
/*      */   public DefaultRequestDirector(Log log, HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectStrategy redirectStrategy, AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, UserTokenHandler userTokenHandler, HttpParams params) {
/*  267 */     if (log == null) {
/*  268 */       throw new IllegalArgumentException("Log may not be null.");
/*      */     }
/*      */     
/*  271 */     if (requestExec == null) {
/*  272 */       throw new IllegalArgumentException("Request executor may not be null.");
/*      */     }
/*      */     
/*  275 */     if (conman == null) {
/*  276 */       throw new IllegalArgumentException("Client connection manager may not be null.");
/*      */     }
/*      */     
/*  279 */     if (reustrat == null) {
/*  280 */       throw new IllegalArgumentException("Connection reuse strategy may not be null.");
/*      */     }
/*      */     
/*  283 */     if (kastrat == null) {
/*  284 */       throw new IllegalArgumentException("Connection keep alive strategy may not be null.");
/*      */     }
/*      */     
/*  287 */     if (rouplan == null) {
/*  288 */       throw new IllegalArgumentException("Route planner may not be null.");
/*      */     }
/*      */     
/*  291 */     if (httpProcessor == null) {
/*  292 */       throw new IllegalArgumentException("HTTP protocol processor may not be null.");
/*      */     }
/*      */     
/*  295 */     if (retryHandler == null) {
/*  296 */       throw new IllegalArgumentException("HTTP request retry handler may not be null.");
/*      */     }
/*      */     
/*  299 */     if (redirectStrategy == null) {
/*  300 */       throw new IllegalArgumentException("Redirect strategy may not be null.");
/*      */     }
/*      */     
/*  303 */     if (targetAuthStrategy == null) {
/*  304 */       throw new IllegalArgumentException("Target authentication strategy may not be null.");
/*      */     }
/*      */     
/*  307 */     if (proxyAuthStrategy == null) {
/*  308 */       throw new IllegalArgumentException("Proxy authentication strategy may not be null.");
/*      */     }
/*      */     
/*  311 */     if (userTokenHandler == null) {
/*  312 */       throw new IllegalArgumentException("User token handler may not be null.");
/*      */     }
/*      */     
/*  315 */     if (params == null) {
/*  316 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*      */     }
/*      */     
/*  319 */     this.log = log;
/*  320 */     this.authenticator = new HttpAuthenticator(log);
/*  321 */     this.requestExec = requestExec;
/*  322 */     this.connManager = conman;
/*  323 */     this.reuseStrategy = reustrat;
/*  324 */     this.keepAliveStrategy = kastrat;
/*  325 */     this.routePlanner = rouplan;
/*  326 */     this.httpProcessor = httpProcessor;
/*  327 */     this.retryHandler = retryHandler;
/*  328 */     this.redirectStrategy = redirectStrategy;
/*  329 */     this.targetAuthStrategy = targetAuthStrategy;
/*  330 */     this.proxyAuthStrategy = proxyAuthStrategy;
/*  331 */     this.userTokenHandler = userTokenHandler;
/*  332 */     this.params = params;
/*      */     
/*  334 */     if (redirectStrategy instanceof DefaultRedirectStrategyAdaptor) {
/*  335 */       this.redirectHandler = ((DefaultRedirectStrategyAdaptor)redirectStrategy).getHandler();
/*      */     } else {
/*  337 */       this.redirectHandler = null;
/*      */     } 
/*  339 */     if (targetAuthStrategy instanceof AuthenticationStrategyAdaptor) {
/*  340 */       this.targetAuthHandler = ((AuthenticationStrategyAdaptor)targetAuthStrategy).getHandler();
/*      */     } else {
/*  342 */       this.targetAuthHandler = null;
/*      */     } 
/*  344 */     if (proxyAuthStrategy instanceof AuthenticationStrategyAdaptor) {
/*  345 */       this.proxyAuthHandler = ((AuthenticationStrategyAdaptor)proxyAuthStrategy).getHandler();
/*      */     } else {
/*  347 */       this.proxyAuthHandler = null;
/*      */     } 
/*      */     
/*  350 */     this.managedConn = null;
/*      */     
/*  352 */     this.execCount = 0;
/*  353 */     this.redirectCount = 0;
/*  354 */     this.targetAuthState = new AuthState();
/*  355 */     this.proxyAuthState = new AuthState();
/*  356 */     this.maxRedirects = this.params.getIntParameter("http.protocol.max-redirects", 100);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private RequestWrapper wrapRequest(HttpRequest request) throws ProtocolException {
/*  362 */     if (request instanceof HttpEntityEnclosingRequest) {
/*  363 */       return new EntityEnclosingRequestWrapper((HttpEntityEnclosingRequest)request);
/*      */     }
/*      */     
/*  366 */     return new RequestWrapper(request);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void rewriteRequestURI(RequestWrapper request, HttpRoute route) throws ProtocolException {
/*      */     try {
/*  377 */       URI uri = request.getURI();
/*  378 */       if (route.getProxyHost() != null && !route.isTunnelled()) {
/*      */         
/*  380 */         if (!uri.isAbsolute()) {
/*  381 */           HttpHost target = route.getTargetHost();
/*  382 */           uri = URIUtils.rewriteURI(uri, target, true);
/*      */         } else {
/*  384 */           uri = URIUtils.rewriteURI(uri);
/*      */         }
/*      */       
/*      */       }
/*  388 */       else if (uri.isAbsolute()) {
/*  389 */         uri = URIUtils.rewriteURI(uri, null, true);
/*      */       } else {
/*  391 */         uri = URIUtils.rewriteURI(uri);
/*      */       } 
/*      */       
/*  394 */       request.setURI(uri);
/*      */     }
/*  396 */     catch (URISyntaxException ex) {
/*  397 */       throw new ProtocolException("Invalid URI: " + request.getRequestLine().getUri(), ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException, IOException {
/*  408 */     context.setAttribute("http.auth.target-scope", this.targetAuthState);
/*  409 */     context.setAttribute("http.auth.proxy-scope", this.proxyAuthState);
/*      */     
/*  411 */     HttpRequest orig = request;
/*  412 */     RequestWrapper origWrapper = wrapRequest(orig);
/*  413 */     origWrapper.setParams(this.params);
/*  414 */     HttpRoute origRoute = determineRoute(target, (HttpRequest)origWrapper, context);
/*      */     
/*  416 */     this.virtualHost = (HttpHost)origWrapper.getParams().getParameter("http.virtual-host");
/*      */ 
/*      */     
/*  419 */     if (this.virtualHost != null && this.virtualHost.getPort() == -1) {
/*  420 */       HttpHost host = (target != null) ? target : origRoute.getTargetHost();
/*  421 */       int port = host.getPort();
/*  422 */       if (port != -1) {
/*  423 */         this.virtualHost = new HttpHost(this.virtualHost.getHostName(), port, this.virtualHost.getSchemeName());
/*      */       }
/*      */     } 
/*      */     
/*  427 */     RoutedRequest roureq = new RoutedRequest(origWrapper, origRoute);
/*      */     
/*  429 */     boolean reuse = false;
/*  430 */     boolean done = false;
/*      */     try {
/*  432 */       HttpResponse response = null;
/*  433 */       while (!done) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  439 */         RequestWrapper wrapper = roureq.getRequest();
/*  440 */         HttpRoute route = roureq.getRoute();
/*  441 */         response = null;
/*      */ 
/*      */         
/*  444 */         Object userToken = context.getAttribute("http.user-token");
/*      */ 
/*      */         
/*  447 */         if (this.managedConn == null) {
/*  448 */           ClientConnectionRequest connRequest = this.connManager.requestConnection(route, userToken);
/*      */           
/*  450 */           if (orig instanceof AbortableHttpRequest) {
/*  451 */             ((AbortableHttpRequest)orig).setConnectionRequest(connRequest);
/*      */           }
/*      */           
/*  454 */           long timeout = HttpClientParams.getConnectionManagerTimeout(this.params);
/*      */           try {
/*  456 */             this.managedConn = connRequest.getConnection(timeout, TimeUnit.MILLISECONDS);
/*  457 */           } catch (InterruptedException interrupted) {
/*  458 */             Thread.currentThread().interrupt();
/*  459 */             throw new InterruptedIOException();
/*      */           } 
/*      */           
/*  462 */           if (HttpConnectionParams.isStaleCheckingEnabled(this.params))
/*      */           {
/*  464 */             if (this.managedConn.isOpen()) {
/*  465 */               this.log.debug("Stale connection check");
/*  466 */               if (this.managedConn.isStale()) {
/*  467 */                 this.log.debug("Stale connection detected");
/*  468 */                 this.managedConn.close();
/*      */               } 
/*      */             } 
/*      */           }
/*      */         } 
/*      */         
/*  474 */         if (orig instanceof AbortableHttpRequest) {
/*  475 */           ((AbortableHttpRequest)orig).setReleaseTrigger((ConnectionReleaseTrigger)this.managedConn);
/*      */         }
/*      */         
/*      */         try {
/*  479 */           tryConnect(roureq, context);
/*  480 */         } catch (TunnelRefusedException ex) {
/*  481 */           if (this.log.isDebugEnabled()) {
/*  482 */             this.log.debug(ex.getMessage());
/*      */           }
/*  484 */           response = ex.getResponse();
/*      */           
/*      */           break;
/*      */         } 
/*  488 */         String userinfo = wrapper.getURI().getUserInfo();
/*  489 */         if (userinfo != null) {
/*  490 */           this.targetAuthState.update((AuthScheme)new BasicScheme(), (Credentials)new UsernamePasswordCredentials(userinfo));
/*      */         }
/*      */ 
/*      */         
/*  494 */         HttpHost proxy = route.getProxyHost();
/*  495 */         if (this.virtualHost != null) {
/*  496 */           target = this.virtualHost;
/*      */         } else {
/*  498 */           URI requestURI = wrapper.getURI();
/*  499 */           if (requestURI.isAbsolute()) {
/*  500 */             target = URIUtils.extractHost(requestURI);
/*      */           }
/*      */         } 
/*  503 */         if (target == null) {
/*  504 */           target = route.getTargetHost();
/*      */         }
/*      */ 
/*      */         
/*  508 */         wrapper.resetHeaders();
/*      */         
/*  510 */         rewriteRequestURI(wrapper, route);
/*      */ 
/*      */         
/*  513 */         context.setAttribute("http.target_host", target);
/*  514 */         context.setAttribute("http.proxy_host", proxy);
/*  515 */         context.setAttribute("http.connection", this.managedConn);
/*      */ 
/*      */         
/*  518 */         this.requestExec.preProcess((HttpRequest)wrapper, this.httpProcessor, context);
/*      */         
/*  520 */         response = tryExecute(roureq, context);
/*  521 */         if (response == null) {
/*      */           continue;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  527 */         response.setParams(this.params);
/*  528 */         this.requestExec.postProcess(response, this.httpProcessor, context);
/*      */ 
/*      */ 
/*      */         
/*  532 */         reuse = this.reuseStrategy.keepAlive(response, context);
/*  533 */         if (reuse) {
/*      */           
/*  535 */           long duration = this.keepAliveStrategy.getKeepAliveDuration(response, context);
/*  536 */           if (this.log.isDebugEnabled()) {
/*      */             String s;
/*  538 */             if (duration > 0L) {
/*  539 */               s = "for " + duration + " " + TimeUnit.MILLISECONDS;
/*      */             } else {
/*  541 */               s = "indefinitely";
/*      */             } 
/*  543 */             this.log.debug("Connection can be kept alive " + s);
/*      */           } 
/*  545 */           this.managedConn.setIdleDuration(duration, TimeUnit.MILLISECONDS);
/*      */         } 
/*      */         
/*  548 */         RoutedRequest followup = handleResponse(roureq, response, context);
/*  549 */         if (followup == null) {
/*  550 */           done = true;
/*      */         } else {
/*  552 */           if (reuse) {
/*      */             
/*  554 */             HttpEntity entity = response.getEntity();
/*  555 */             EntityUtils.consume(entity);
/*      */ 
/*      */             
/*  558 */             this.managedConn.markReusable();
/*      */           } else {
/*  560 */             this.managedConn.close();
/*  561 */             if (this.proxyAuthState.getState().compareTo((Enum)AuthProtocolState.CHALLENGED) > 0 && this.proxyAuthState.getAuthScheme() != null && this.proxyAuthState.getAuthScheme().isConnectionBased()) {
/*      */ 
/*      */               
/*  564 */               this.log.debug("Resetting proxy auth state");
/*  565 */               this.proxyAuthState.reset();
/*      */             } 
/*  567 */             if (this.targetAuthState.getState().compareTo((Enum)AuthProtocolState.CHALLENGED) > 0 && this.targetAuthState.getAuthScheme() != null && this.targetAuthState.getAuthScheme().isConnectionBased()) {
/*      */ 
/*      */               
/*  570 */               this.log.debug("Resetting target auth state");
/*  571 */               this.targetAuthState.reset();
/*      */             } 
/*      */           } 
/*      */           
/*  575 */           if (!followup.getRoute().equals(roureq.getRoute())) {
/*  576 */             releaseConnection();
/*      */           }
/*  578 */           roureq = followup;
/*      */         } 
/*      */         
/*  581 */         if (this.managedConn != null) {
/*  582 */           if (userToken == null) {
/*  583 */             userToken = this.userTokenHandler.getUserToken(context);
/*  584 */             context.setAttribute("http.user-token", userToken);
/*      */           } 
/*  586 */           if (userToken != null) {
/*  587 */             this.managedConn.setState(userToken);
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  595 */       if (response == null || response.getEntity() == null || !response.getEntity().isStreaming()) {
/*      */ 
/*      */         
/*  598 */         if (reuse)
/*  599 */           this.managedConn.markReusable(); 
/*  600 */         releaseConnection();
/*      */       } else {
/*      */         
/*  603 */         HttpEntity entity = response.getEntity();
/*  604 */         BasicManagedEntity basicManagedEntity = new BasicManagedEntity(entity, this.managedConn, reuse);
/*  605 */         response.setEntity((HttpEntity)basicManagedEntity);
/*      */       } 
/*      */       
/*  608 */       return response;
/*      */     }
/*  610 */     catch (ConnectionShutdownException ex) {
/*  611 */       InterruptedIOException ioex = new InterruptedIOException("Connection has been shut down");
/*      */       
/*  613 */       ioex.initCause((Throwable)ex);
/*  614 */       throw ioex;
/*  615 */     } catch (HttpException ex) {
/*  616 */       abortConnection();
/*  617 */       throw ex;
/*  618 */     } catch (IOException ex) {
/*  619 */       abortConnection();
/*  620 */       throw ex;
/*  621 */     } catch (RuntimeException ex) {
/*  622 */       abortConnection();
/*  623 */       throw ex;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void tryConnect(RoutedRequest req, HttpContext context) throws HttpException, IOException {
/*  633 */     HttpRoute route = req.getRoute();
/*  634 */     RequestWrapper requestWrapper = req.getRequest();
/*      */     
/*  636 */     int connectCount = 0;
/*      */     while (true) {
/*  638 */       context.setAttribute("http.request", requestWrapper);
/*      */       
/*  640 */       connectCount++;
/*      */       try {
/*  642 */         if (!this.managedConn.isOpen()) {
/*  643 */           this.managedConn.open(route, context, this.params);
/*      */         } else {
/*  645 */           this.managedConn.setSocketTimeout(HttpConnectionParams.getSoTimeout(this.params));
/*      */         } 
/*  647 */         establishRoute(route, context);
/*      */       }
/*  649 */       catch (IOException ex) {
/*      */         try {
/*  651 */           this.managedConn.close();
/*  652 */         } catch (IOException ignore) {}
/*      */         
/*  654 */         if (this.retryHandler.retryRequest(ex, connectCount, context)) {
/*  655 */           if (this.log.isInfoEnabled()) {
/*  656 */             this.log.info("I/O exception (" + ex.getClass().getName() + ") caught when connecting to the target host: " + ex.getMessage());
/*      */ 
/*      */             
/*  659 */             if (this.log.isDebugEnabled()) {
/*  660 */               this.log.debug(ex.getMessage(), ex);
/*      */             }
/*  662 */             this.log.info("Retrying connect");
/*      */           }  continue;
/*      */         } 
/*  665 */         throw ex;
/*      */       } 
/*      */       break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private HttpResponse tryExecute(RoutedRequest req, HttpContext context) throws HttpException, IOException {
/*  676 */     RequestWrapper wrapper = req.getRequest();
/*  677 */     HttpRoute route = req.getRoute();
/*  678 */     HttpResponse response = null;
/*      */     
/*  680 */     Exception retryReason = null;
/*      */     
/*      */     while (true) {
/*  683 */       this.execCount++;
/*      */       
/*  685 */       wrapper.incrementExecCount();
/*  686 */       if (!wrapper.isRepeatable()) {
/*  687 */         this.log.debug("Cannot retry non-repeatable request");
/*  688 */         if (retryReason != null) {
/*  689 */           throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.  The cause lists the reason the original request failed.", retryReason);
/*      */         }
/*      */ 
/*      */         
/*  693 */         throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.");
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  699 */         if (!this.managedConn.isOpen())
/*      */         {
/*      */           
/*  702 */           if (!route.isTunnelled()) {
/*  703 */             this.log.debug("Reopening the direct connection.");
/*  704 */             this.managedConn.open(route, context, this.params);
/*      */           } else {
/*      */             
/*  707 */             this.log.debug("Proxied connection. Need to start over.");
/*      */             
/*      */             break;
/*      */           } 
/*      */         }
/*  712 */         if (this.log.isDebugEnabled()) {
/*  713 */           this.log.debug("Attempt " + this.execCount + " to execute request");
/*      */         }
/*  715 */         response = this.requestExec.execute((HttpRequest)wrapper, (HttpClientConnection)this.managedConn, context);
/*      */       
/*      */       }
/*  718 */       catch (IOException ex) {
/*  719 */         this.log.debug("Closing the connection.");
/*      */         try {
/*  721 */           this.managedConn.close();
/*  722 */         } catch (IOException ignore) {}
/*      */         
/*  724 */         if (this.retryHandler.retryRequest(ex, wrapper.getExecCount(), context)) {
/*  725 */           if (this.log.isInfoEnabled()) {
/*  726 */             this.log.info("I/O exception (" + ex.getClass().getName() + ") caught when processing request: " + ex.getMessage());
/*      */           }
/*      */ 
/*      */           
/*  730 */           if (this.log.isDebugEnabled()) {
/*  731 */             this.log.debug(ex.getMessage(), ex);
/*      */           }
/*  733 */           this.log.info("Retrying request");
/*  734 */           retryReason = ex; continue;
/*      */         } 
/*  736 */         throw ex;
/*      */       } 
/*      */       break;
/*      */     } 
/*  740 */     return response;
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
/*      */   protected void releaseConnection() {
/*      */     try {
/*  753 */       this.managedConn.releaseConnection();
/*  754 */     } catch (IOException ignored) {
/*  755 */       this.log.debug("IOException releasing connection", ignored);
/*      */     } 
/*  757 */     this.managedConn = null;
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
/*      */   protected HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
/*  782 */     if (target == null) {
/*  783 */       target = (HttpHost)request.getParams().getParameter("http.default-host");
/*      */     }
/*      */     
/*  786 */     if (target == null) {
/*  787 */       throw new IllegalStateException("Target host must not be null, or set in parameters.");
/*      */     }
/*      */ 
/*      */     
/*  791 */     return this.routePlanner.determineRoute(target, request, context);
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
/*      */   protected void establishRoute(HttpRoute route, HttpContext context) throws HttpException, IOException {
/*      */     int step;
/*  807 */     BasicRouteDirector basicRouteDirector = new BasicRouteDirector(); do {
/*      */       boolean secure; int hop;
/*      */       boolean bool1;
/*  810 */       HttpRoute fact = this.managedConn.getRoute();
/*  811 */       step = basicRouteDirector.nextStep((RouteInfo)route, (RouteInfo)fact);
/*      */       
/*  813 */       switch (step) {
/*      */         
/*      */         case 1:
/*      */         case 2:
/*  817 */           this.managedConn.open(route, context, this.params);
/*      */           break;
/*      */         
/*      */         case 3:
/*  821 */           secure = createTunnelToTarget(route, context);
/*  822 */           this.log.debug("Tunnel to target created.");
/*  823 */           this.managedConn.tunnelTarget(secure, this.params);
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 4:
/*  831 */           hop = fact.getHopCount() - 1;
/*  832 */           bool1 = createTunnelToProxy(route, hop, context);
/*  833 */           this.log.debug("Tunnel to proxy created.");
/*  834 */           this.managedConn.tunnelProxy(route.getHopTarget(hop), bool1, this.params);
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 5:
/*  840 */           this.managedConn.layerProtocol(context, this.params);
/*      */           break;
/*      */         
/*      */         case -1:
/*  844 */           throw new HttpException("Unable to establish route: planned = " + route + "; current = " + fact);
/*      */         
/*      */         case 0:
/*      */           break;
/*      */         
/*      */         default:
/*  850 */           throw new IllegalStateException("Unknown step indicator " + step + " from RouteDirector.");
/*      */       } 
/*      */ 
/*      */     
/*  854 */     } while (step > 0);
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
/*      */   protected boolean createTunnelToTarget(HttpRoute route, HttpContext context) throws HttpException, IOException {
/*  882 */     HttpHost proxy = route.getProxyHost();
/*  883 */     HttpHost target = route.getTargetHost();
/*  884 */     HttpResponse response = null;
/*      */     
/*      */     while (true) {
/*  887 */       if (!this.managedConn.isOpen()) {
/*  888 */         this.managedConn.open(route, context, this.params);
/*      */       }
/*      */       
/*  891 */       HttpRequest connect = createConnectRequest(route, context);
/*  892 */       connect.setParams(this.params);
/*      */ 
/*      */       
/*  895 */       context.setAttribute("http.target_host", target);
/*  896 */       context.setAttribute("http.proxy_host", proxy);
/*  897 */       context.setAttribute("http.connection", this.managedConn);
/*  898 */       context.setAttribute("http.request", connect);
/*      */       
/*  900 */       this.requestExec.preProcess(connect, this.httpProcessor, context);
/*      */       
/*  902 */       response = this.requestExec.execute(connect, (HttpClientConnection)this.managedConn, context);
/*      */       
/*  904 */       response.setParams(this.params);
/*  905 */       this.requestExec.postProcess(response, this.httpProcessor, context);
/*      */       
/*  907 */       int i = response.getStatusLine().getStatusCode();
/*  908 */       if (i < 200) {
/*  909 */         throw new HttpException("Unexpected response to CONNECT request: " + response.getStatusLine());
/*      */       }
/*      */ 
/*      */       
/*  913 */       if (HttpClientParams.isAuthenticating(this.params)) {
/*  914 */         if (this.authenticator.isAuthenticationRequested(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, context))
/*      */         {
/*  916 */           if (this.authenticator.authenticate(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, context)) {
/*      */ 
/*      */             
/*  919 */             if (this.reuseStrategy.keepAlive(response, context)) {
/*  920 */               this.log.debug("Connection kept alive");
/*      */               
/*  922 */               HttpEntity entity = response.getEntity();
/*  923 */               EntityUtils.consume(entity); continue;
/*      */             } 
/*  925 */             this.managedConn.close();
/*      */ 
/*      */             
/*      */             continue;
/*      */           } 
/*      */         }
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/*  936 */     int status = response.getStatusLine().getStatusCode();
/*      */     
/*  938 */     if (status > 299) {
/*      */ 
/*      */       
/*  941 */       HttpEntity entity = response.getEntity();
/*  942 */       if (entity != null) {
/*  943 */         response.setEntity((HttpEntity)new BufferedHttpEntity(entity));
/*      */       }
/*      */       
/*  946 */       this.managedConn.close();
/*  947 */       throw new TunnelRefusedException("CONNECT refused by proxy: " + response.getStatusLine(), response);
/*      */     } 
/*      */ 
/*      */     
/*  951 */     this.managedConn.markReusable();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  957 */     return false;
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
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean createTunnelToProxy(HttpRoute route, int hop, HttpContext context) throws HttpException, IOException {
/*  993 */     throw new HttpException("Proxy chains are not supported.");
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
/*      */   protected HttpRequest createConnectRequest(HttpRoute route, HttpContext context) {
/* 1013 */     HttpHost target = route.getTargetHost();
/*      */     
/* 1015 */     String host = target.getHostName();
/* 1016 */     int port = target.getPort();
/* 1017 */     if (port < 0) {
/* 1018 */       Scheme scheme = this.connManager.getSchemeRegistry().getScheme(target.getSchemeName());
/*      */       
/* 1020 */       port = scheme.getDefaultPort();
/*      */     } 
/*      */     
/* 1023 */     StringBuilder buffer = new StringBuilder(host.length() + 6);
/* 1024 */     buffer.append(host);
/* 1025 */     buffer.append(':');
/* 1026 */     buffer.append(Integer.toString(port));
/*      */     
/* 1028 */     String authority = buffer.toString();
/* 1029 */     ProtocolVersion ver = HttpProtocolParams.getVersion(this.params);
/* 1030 */     return (HttpRequest)new BasicHttpRequest("CONNECT", authority, ver);
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
/*      */   protected RoutedRequest handleResponse(RoutedRequest roureq, HttpResponse response, HttpContext context) throws HttpException, IOException {
/* 1055 */     HttpRoute route = roureq.getRoute();
/* 1056 */     RequestWrapper request = roureq.getRequest();
/*      */     
/* 1058 */     HttpParams params = request.getParams();
/*      */     
/* 1060 */     if (HttpClientParams.isAuthenticating(params)) {
/* 1061 */       HttpHost target = (HttpHost)context.getAttribute("http.target_host");
/* 1062 */       if (target == null) {
/* 1063 */         target = route.getTargetHost();
/*      */       }
/* 1065 */       if (target.getPort() < 0) {
/* 1066 */         Scheme scheme = this.connManager.getSchemeRegistry().getScheme(target);
/* 1067 */         target = new HttpHost(target.getHostName(), scheme.getDefaultPort(), target.getSchemeName());
/*      */       } 
/* 1069 */       if (this.authenticator.isAuthenticationRequested(target, response, this.targetAuthStrategy, this.targetAuthState, context))
/*      */       {
/* 1071 */         if (this.authenticator.authenticate(target, response, this.targetAuthStrategy, this.targetAuthState, context))
/*      */         {
/*      */           
/* 1074 */           return roureq;
/*      */         }
/*      */       }
/*      */       
/* 1078 */       HttpHost proxy = route.getProxyHost();
/* 1079 */       if (this.authenticator.isAuthenticationRequested(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, context)) {
/*      */ 
/*      */         
/* 1082 */         if (proxy == null) {
/* 1083 */           proxy = route.getTargetHost();
/*      */         }
/* 1085 */         if (this.authenticator.authenticate(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, context))
/*      */         {
/*      */           
/* 1088 */           return roureq;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1093 */     if (HttpClientParams.isRedirecting(params) && this.redirectStrategy.isRedirected((HttpRequest)request, response, context)) {
/*      */ 
/*      */       
/* 1096 */       if (this.redirectCount >= this.maxRedirects) {
/* 1097 */         throw new RedirectException("Maximum redirects (" + this.maxRedirects + ") exceeded");
/*      */       }
/*      */       
/* 1100 */       this.redirectCount++;
/*      */ 
/*      */       
/* 1103 */       this.virtualHost = null;
/*      */       
/* 1105 */       HttpUriRequest redirect = this.redirectStrategy.getRedirect((HttpRequest)request, response, context);
/* 1106 */       HttpRequest orig = request.getOriginal();
/* 1107 */       redirect.setHeaders(orig.getAllHeaders());
/*      */       
/* 1109 */       URI uri = redirect.getURI();
/* 1110 */       HttpHost newTarget = URIUtils.extractHost(uri);
/* 1111 */       if (newTarget == null) {
/* 1112 */         throw new ProtocolException("Redirect URI does not specify a valid host name: " + uri);
/*      */       }
/*      */ 
/*      */       
/* 1116 */       if (!route.getTargetHost().equals(newTarget)) {
/* 1117 */         this.log.debug("Resetting target auth state");
/* 1118 */         this.targetAuthState.reset();
/* 1119 */         AuthScheme authScheme = this.proxyAuthState.getAuthScheme();
/* 1120 */         if (authScheme != null && authScheme.isConnectionBased()) {
/* 1121 */           this.log.debug("Resetting proxy auth state");
/* 1122 */           this.proxyAuthState.reset();
/*      */         } 
/*      */       } 
/*      */       
/* 1126 */       RequestWrapper wrapper = wrapRequest((HttpRequest)redirect);
/* 1127 */       wrapper.setParams(params);
/*      */       
/* 1129 */       HttpRoute newRoute = determineRoute(newTarget, (HttpRequest)wrapper, context);
/* 1130 */       RoutedRequest newRequest = new RoutedRequest(wrapper, newRoute);
/*      */       
/* 1132 */       if (this.log.isDebugEnabled()) {
/* 1133 */         this.log.debug("Redirecting to '" + uri + "' via " + newRoute);
/*      */       }
/*      */       
/* 1136 */       return newRequest;
/*      */     } 
/*      */     
/* 1139 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void abortConnection() {
/* 1149 */     ManagedClientConnection mcc = this.managedConn;
/* 1150 */     if (mcc != null) {
/*      */ 
/*      */       
/* 1153 */       this.managedConn = null;
/*      */       try {
/* 1155 */         mcc.abortConnection();
/* 1156 */       } catch (IOException ex) {
/* 1157 */         if (this.log.isDebugEnabled()) {
/* 1158 */           this.log.debug(ex.getMessage(), ex);
/*      */         }
/*      */       } 
/*      */       
/*      */       try {
/* 1163 */         mcc.releaseConnection();
/* 1164 */       } catch (IOException ignored) {
/* 1165 */         this.log.debug("Error releasing connection", ignored);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\DefaultRequestDirector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */