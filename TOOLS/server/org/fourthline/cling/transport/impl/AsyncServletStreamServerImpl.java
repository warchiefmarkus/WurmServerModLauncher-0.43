/*     */ package org.fourthline.cling.transport.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.servlet.AsyncContext;
/*     */ import javax.servlet.AsyncEvent;
/*     */ import javax.servlet.AsyncListener;
/*     */ import javax.servlet.Servlet;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServlet;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.fourthline.cling.model.message.Connection;
/*     */ import org.fourthline.cling.protocol.ProtocolFactory;
/*     */ import org.fourthline.cling.transport.Router;
/*     */ import org.fourthline.cling.transport.spi.InitializationException;
/*     */ import org.fourthline.cling.transport.spi.StreamServer;
/*     */ import org.fourthline.cling.transport.spi.StreamServerConfiguration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsyncServletStreamServerImpl
/*     */   implements StreamServer<AsyncServletStreamServerConfigurationImpl>
/*     */ {
/*  45 */   private static final Logger log = Logger.getLogger(StreamServer.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   protected final AsyncServletStreamServerConfigurationImpl configuration;
/*     */ 
/*     */ 
/*     */   
/*     */   protected int localPort;
/*     */ 
/*     */ 
/*     */   
/*     */   protected String hostAddress;
/*     */ 
/*     */ 
/*     */   
/*     */   private int mCounter;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncServletStreamServerConfigurationImpl getConfiguration() {
/*     */     return this.configuration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void init(InetAddress bindAddress, Router router) throws InitializationException {
/*     */     try {
/*     */       if (log.isLoggable(Level.FINE)) {
/*     */         log.fine("Setting executor service on servlet container adapter");
/*     */       }
/*     */       getConfiguration().getServletContainerAdapter().setExecutorService(router.getConfiguration().getStreamServerExecutorService());
/*     */       if (log.isLoggable(Level.FINE)) {
/*     */         log.fine("Adding connector: " + bindAddress + ":" + getConfiguration().getListenPort());
/*     */       }
/*     */       this.hostAddress = bindAddress.getHostAddress();
/*     */       this.localPort = getConfiguration().getServletContainerAdapter().addConnector(this.hostAddress, getConfiguration().getListenPort());
/*     */       String contextPath = router.getConfiguration().getNamespace().getBasePath().getPath();
/*     */       getConfiguration().getServletContainerAdapter().registerServlet(contextPath, createServlet(router));
/*     */     } catch (Exception ex) {
/*     */       throw new InitializationException("Could not initialize " + getClass().getSimpleName() + ": " + ex.toString(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncServletStreamServerImpl(AsyncServletStreamServerConfigurationImpl configuration) {
/*  95 */     this.mCounter = 0;
/*     */     this.configuration = configuration;
/*     */   } protected Servlet createServlet(final Router router) {
/*  98 */     return (Servlet)new HttpServlet()
/*     */       {
/*     */         protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
/*     */         {
/* 102 */           final long startTime = System.currentTimeMillis();
/* 103 */           final int counter = AsyncServletStreamServerImpl.this.mCounter++;
/* 104 */           if (AsyncServletStreamServerImpl.log.isLoggable(Level.FINE)) {
/* 105 */             AsyncServletStreamServerImpl.log.fine(String.format("HttpServlet.service(): id: %3d, request URI: %s", new Object[] { Integer.valueOf(counter), req.getRequestURI() }));
/*     */           }
/* 107 */           AsyncContext async = req.startAsync();
/* 108 */           async.setTimeout((AsyncServletStreamServerImpl.this.getConfiguration().getAsyncTimeoutSeconds() * 1000));
/*     */           
/* 110 */           async.addListener(new AsyncListener()
/*     */               {
/*     */                 public void onTimeout(AsyncEvent arg0) throws IOException
/*     */                 {
/* 114 */                   long duration = System.currentTimeMillis() - startTime;
/* 115 */                   if (AsyncServletStreamServerImpl.log.isLoggable(Level.FINE)) {
/* 116 */                     AsyncServletStreamServerImpl.log.fine(String.format("AsyncListener.onTimeout(): id: %3d, duration: %,4d, request: %s", new Object[] { Integer.valueOf(this.val$counter), Long.valueOf(duration), arg0.getSuppliedRequest() }));
/*     */                   }
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public void onStartAsync(AsyncEvent arg0) throws IOException {
/* 122 */                   if (AsyncServletStreamServerImpl.log.isLoggable(Level.FINE)) {
/* 123 */                     AsyncServletStreamServerImpl.log.fine(String.format("AsyncListener.onStartAsync(): id: %3d, request: %s", new Object[] { Integer.valueOf(this.val$counter), arg0.getSuppliedRequest() }));
/*     */                   }
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public void onError(AsyncEvent arg0) throws IOException {
/* 129 */                   long duration = System.currentTimeMillis() - startTime;
/* 130 */                   if (AsyncServletStreamServerImpl.log.isLoggable(Level.FINE)) {
/* 131 */                     AsyncServletStreamServerImpl.log.fine(String.format("AsyncListener.onError(): id: %3d, duration: %,4d, response: %s", new Object[] { Integer.valueOf(this.val$counter), Long.valueOf(duration), arg0.getSuppliedResponse() }));
/*     */                   }
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public void onComplete(AsyncEvent arg0) throws IOException {
/* 137 */                   long duration = System.currentTimeMillis() - startTime;
/* 138 */                   if (AsyncServletStreamServerImpl.log.isLoggable(Level.FINE)) {
/* 139 */                     AsyncServletStreamServerImpl.log.fine(String.format("AsyncListener.onComplete(): id: %3d, duration: %,4d, response: %s", new Object[] { Integer.valueOf(this.val$counter), Long.valueOf(duration), arg0.getSuppliedResponse() }));
/*     */                   }
/*     */                 }
/*     */               });
/*     */ 
/*     */           
/* 145 */           AsyncServletUpnpStream stream = new AsyncServletUpnpStream(router.getProtocolFactory(), async, req)
/*     */             {
/*     */               protected Connection createConnection() {
/* 148 */                 return new AsyncServletStreamServerImpl.AsyncServletConnection(getRequest());
/*     */               }
/*     */             };
/*     */           
/* 152 */           router.received(stream);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int getPort() {
/*     */     return this.localPort;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isConnectionOpen(HttpServletRequest request) {
/* 165 */     return true;
/*     */   } public synchronized void stop() {
/*     */     getConfiguration().getServletContainerAdapter().removeConnector(this.hostAddress, this.localPort);
/*     */   } public void run() {
/*     */     getConfiguration().getServletContainerAdapter().startIfNotRunning();
/*     */   }
/*     */   protected class AsyncServletConnection implements Connection { protected HttpServletRequest request;
/*     */     public AsyncServletConnection(HttpServletRequest request) {
/* 173 */       this.request = request;
/*     */     }
/*     */     
/*     */     public HttpServletRequest getRequest() {
/* 177 */       return this.request;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isOpen() {
/* 182 */       return AsyncServletStreamServerImpl.this.isConnectionOpen(getRequest());
/*     */     }
/*     */ 
/*     */     
/*     */     public InetAddress getRemoteAddress() {
/*     */       try {
/* 188 */         return InetAddress.getByName(getRequest().getRemoteAddr());
/* 189 */       } catch (UnknownHostException ex) {
/* 190 */         throw new RuntimeException(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public InetAddress getLocalAddress() {
/*     */       try {
/* 197 */         return InetAddress.getByName(getRequest().getLocalAddr());
/* 198 */       } catch (UnknownHostException ex) {
/* 199 */         throw new RuntimeException(ex);
/*     */       } 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\AsyncServletStreamServerImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */