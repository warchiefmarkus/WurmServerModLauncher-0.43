/*     */ package org.fourthline.cling.transport.impl;
/*     */ 
/*     */ import com.sun.net.httpserver.HttpExchange;
/*     */ import com.sun.net.httpserver.HttpHandler;
/*     */ import com.sun.net.httpserver.HttpServer;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.logging.Logger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StreamServerImpl
/*     */   implements StreamServer<StreamServerConfigurationImpl>
/*     */ {
/*  50 */   private static Logger log = Logger.getLogger(StreamServer.class.getName());
/*     */   
/*     */   protected final StreamServerConfigurationImpl configuration;
/*     */   protected HttpServer server;
/*     */   
/*     */   public StreamServerImpl(StreamServerConfigurationImpl configuration) {
/*  56 */     this.configuration = configuration;
/*     */   }
/*     */   
/*     */   public synchronized void init(InetAddress bindAddress, Router router) throws InitializationException {
/*     */     try {
/*  61 */       InetSocketAddress socketAddress = new InetSocketAddress(bindAddress, this.configuration.getListenPort());
/*     */       
/*  63 */       this.server = HttpServer.create(socketAddress, this.configuration.getTcpConnectionBacklog());
/*  64 */       this.server.createContext("/", new RequestHttpHandler(router));
/*     */       
/*  66 */       log.info("Created server (for receiving TCP streams) on: " + this.server.getAddress());
/*     */     }
/*  68 */     catch (Exception ex) {
/*  69 */       throw new InitializationException("Could not initialize " + getClass().getSimpleName() + ": " + ex.toString(), ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized int getPort() {
/*  74 */     return this.server.getAddress().getPort();
/*     */   }
/*     */   
/*     */   public StreamServerConfigurationImpl getConfiguration() {
/*  78 */     return this.configuration;
/*     */   }
/*     */   
/*     */   public synchronized void run() {
/*  82 */     log.fine("Starting StreamServer...");
/*     */     
/*  84 */     this.server.start();
/*     */   }
/*     */   
/*     */   public synchronized void stop() {
/*  88 */     log.fine("Stopping StreamServer...");
/*  89 */     if (this.server != null) this.server.stop(1); 
/*     */   }
/*     */   
/*     */   protected class RequestHttpHandler
/*     */     implements HttpHandler {
/*     */     private final Router router;
/*     */     
/*     */     public RequestHttpHandler(Router router) {
/*  97 */       this.router = router;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void handle(final HttpExchange httpExchange) throws IOException {
/* 104 */       StreamServerImpl.log.fine("Received HTTP exchange: " + httpExchange.getRequestMethod() + " " + httpExchange.getRequestURI());
/* 105 */       this.router.received(new HttpExchangeUpnpStream(this.router
/* 106 */             .getProtocolFactory(), httpExchange)
/*     */           {
/*     */             protected Connection createConnection() {
/* 109 */               return new StreamServerImpl.HttpServerConnection(httpExchange);
/*     */             }
/*     */           });
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
/*     */   protected boolean isConnectionOpen(HttpExchange exchange) {
/* 123 */     log.warning("Can't check client connection, socket access impossible on JDK webserver!");
/* 124 */     return true;
/*     */   }
/*     */   
/*     */   protected class HttpServerConnection
/*     */     implements Connection {
/*     */     protected HttpExchange exchange;
/*     */     
/*     */     public HttpServerConnection(HttpExchange exchange) {
/* 132 */       this.exchange = exchange;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isOpen() {
/* 137 */       return StreamServerImpl.this.isConnectionOpen(this.exchange);
/*     */     }
/*     */ 
/*     */     
/*     */     public InetAddress getRemoteAddress() {
/* 142 */       return (this.exchange.getRemoteAddress() != null) ? this.exchange
/* 143 */         .getRemoteAddress().getAddress() : null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public InetAddress getLocalAddress() {
/* 149 */       return (this.exchange.getLocalAddress() != null) ? this.exchange
/* 150 */         .getLocalAddress().getAddress() : null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\StreamServerImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */