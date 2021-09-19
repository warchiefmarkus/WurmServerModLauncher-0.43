/*     */ package org.fourthline.cling.transport.impl.jetty;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.Socket;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.servlet.Servlet;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.eclipse.jetty.server.AbstractHttpConnection;
/*     */ import org.eclipse.jetty.server.Connector;
/*     */ import org.eclipse.jetty.server.Handler;
/*     */ import org.eclipse.jetty.server.Request;
/*     */ import org.eclipse.jetty.server.Server;
/*     */ import org.eclipse.jetty.server.bio.SocketConnector;
/*     */ import org.eclipse.jetty.servlet.ServletContextHandler;
/*     */ import org.eclipse.jetty.servlet.ServletHolder;
/*     */ import org.eclipse.jetty.util.thread.ExecutorThreadPool;
/*     */ import org.eclipse.jetty.util.thread.ThreadPool;
/*     */ import org.fourthline.cling.transport.spi.ServletContainerAdapter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JettyServletContainer
/*     */   implements ServletContainerAdapter
/*     */ {
/*  52 */   private static final Logger log = Logger.getLogger(JettyServletContainer.class.getName());
/*     */ 
/*     */   
/*  55 */   public static final JettyServletContainer INSTANCE = new JettyServletContainer();
/*     */   private JettyServletContainer() {
/*  57 */     resetServer();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Server server;
/*     */   
/*     */   public synchronized void setExecutorService(ExecutorService executorService) {
/*  64 */     if (INSTANCE.server.getThreadPool() == null) {
/*  65 */       INSTANCE.server.setThreadPool((ThreadPool)new ExecutorThreadPool(executorService)
/*     */           {
/*     */             protected void doStop() throws Exception {}
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int addConnector(String host, int port) throws IOException {
/*  76 */     SocketConnector connector = new SocketConnector();
/*  77 */     connector.setHost(host);
/*  78 */     connector.setPort(port);
/*     */ 
/*     */     
/*  81 */     connector.open();
/*     */ 
/*     */     
/*  84 */     this.server.addConnector((Connector)connector);
/*     */ 
/*     */     
/*  87 */     if (this.server.isStarted()) {
/*     */       try {
/*  89 */         connector.start();
/*  90 */       } catch (Exception ex) {
/*  91 */         log.severe("Couldn't start connector: " + connector + " " + ex);
/*  92 */         throw new RuntimeException(ex);
/*     */       } 
/*     */     }
/*  95 */     return connector.getLocalPort();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void removeConnector(String host, int port) {
/* 100 */     Connector[] connectors = this.server.getConnectors();
/* 101 */     for (Connector connector : connectors) {
/* 102 */       if (connector.getHost().equals(host) && connector.getLocalPort() == port) {
/* 103 */         if (connector.isStarted() || connector.isStarting()) {
/*     */           try {
/* 105 */             connector.stop();
/* 106 */           } catch (Exception ex) {
/* 107 */             log.severe("Couldn't stop connector: " + connector + " " + ex);
/* 108 */             throw new RuntimeException(ex);
/*     */           } 
/*     */         }
/* 111 */         this.server.removeConnector(connector);
/* 112 */         if (connectors.length == 1) {
/* 113 */           log.info("No more connectors, stopping Jetty server");
/* 114 */           stopIfRunning();
/*     */         } 
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void registerServlet(String contextPath, Servlet servlet) {
/* 123 */     if (this.server.getHandler() != null) {
/*     */       return;
/*     */     }
/* 126 */     log.info("Registering UPnP servlet under context path: " + contextPath);
/* 127 */     ServletContextHandler servletHandler = new ServletContextHandler(0);
/*     */     
/* 129 */     if (contextPath != null && contextPath.length() > 0)
/* 130 */       servletHandler.setContextPath(contextPath); 
/* 131 */     ServletHolder s = new ServletHolder(servlet);
/* 132 */     servletHandler.addServlet(s, "/*");
/* 133 */     this.server.setHandler((Handler)servletHandler);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void startIfNotRunning() {
/* 138 */     if (!this.server.isStarted() && !this.server.isStarting()) {
/* 139 */       log.info("Starting Jetty server... ");
/*     */       try {
/* 141 */         this.server.start();
/* 142 */       } catch (Exception ex) {
/* 143 */         log.severe("Couldn't start Jetty server: " + ex);
/* 144 */         throw new RuntimeException(ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void stopIfRunning() {
/* 151 */     if (!this.server.isStopped() && !this.server.isStopping()) {
/* 152 */       log.info("Stopping Jetty server...");
/*     */       try {
/* 154 */         this.server.stop();
/* 155 */       } catch (Exception ex) {
/* 156 */         log.severe("Couldn't stop Jetty server: " + ex);
/* 157 */         throw new RuntimeException(ex);
/*     */       } finally {
/* 159 */         resetServer();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void resetServer() {
/* 165 */     this.server = new Server();
/* 166 */     this.server.setGracefulShutdown(1000);
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
/*     */   public static boolean isConnectionOpen(HttpServletRequest request) {
/* 178 */     return isConnectionOpen(request, " ".getBytes());
/*     */   }
/*     */   
/*     */   public static boolean isConnectionOpen(HttpServletRequest request, byte[] heartbeat) {
/* 182 */     Request jettyRequest = (Request)request;
/* 183 */     AbstractHttpConnection connection = jettyRequest.getConnection();
/* 184 */     Socket socket = (Socket)connection.getEndPoint().getTransport();
/* 185 */     if (log.isLoggable(Level.FINE))
/* 186 */       log.fine("Checking if client connection is still open: " + socket.getRemoteSocketAddress()); 
/*     */     try {
/* 188 */       socket.getOutputStream().write(heartbeat);
/* 189 */       socket.getOutputStream().flush();
/* 190 */       return true;
/* 191 */     } catch (IOException ex) {
/* 192 */       if (log.isLoggable(Level.FINE))
/* 193 */         log.fine("Client connection has been closed: " + socket.getRemoteSocketAddress()); 
/* 194 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\jetty\JettyServletContainer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */