/*     */ package com.wurmonline.communication;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.nio.channels.ServerSocketChannel;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SocketServer
/*     */ {
/*     */   private final ServerSocketChannel ssc;
/*     */   private final ServerListener serverListener;
/*  39 */   private final List<SocketConnection> connections = new LinkedList<>();
/*     */   
/*  41 */   public static final ReentrantReadWriteLock CONNECTIONS_RW_LOCK = new ReentrantReadWriteLock();
/*     */   
/*     */   private final int acceptedPort;
/*     */   public boolean intraServer = false;
/*  45 */   private static final Logger logger = Logger.getLogger(SocketServer.class.getName());
/*  46 */   private static Map<String, Long> connectedIps = new HashMap<>();
/*  47 */   public static long MIN_MILLIS_BETWEEN_CONNECTIONS = 1000L;
/*     */ 
/*     */ 
/*     */   
/*     */   public SocketServer(byte[] ips, int port, int acceptedPort, ServerListener serverListener) throws IOException {
/*  52 */     this.serverListener = serverListener;
/*  53 */     this.acceptedPort = acceptedPort;
/*  54 */     InetAddress hostip = InetAddress.getByAddress(ips);
/*  55 */     logger.info("Creating Wurm SocketServer on " + hostip + ':' + port);
/*  56 */     this.ssc = ServerSocketChannel.open();
/*  57 */     this.ssc.socket().bind(new InetSocketAddress(hostip, port));
/*     */ 
/*     */     
/*  60 */     this.ssc.configureBlocking(false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() throws IOException {
/*     */     SocketChannel socketChannel;
/*  67 */     while ((socketChannel = this.ssc.accept()) != null) {
/*     */ 
/*     */       
/*     */       try {
/*  71 */         if (socketChannel.socket().getPort() != this.acceptedPort) {
/*     */           
/*  73 */           if (!this.intraServer) {
/*  74 */             logger.log(Level.INFO, "Accepted player connection: " + socketChannel.socket());
/*     */           
/*     */           }
/*     */         
/*     */         }
/*  79 */         else if (!this.intraServer) {
/*  80 */           logger.log(Level.INFO, socketChannel.socket().getRemoteSocketAddress() + " connected from the correct port");
/*     */         } 
/*     */         
/*  83 */         boolean keepGoing = true;
/*  84 */         if (!this.intraServer && MIN_MILLIS_BETWEEN_CONNECTIONS > 0L) {
/*     */ 
/*     */           
/*  87 */           String remoteIp = socketChannel.socket().getRemoteSocketAddress().toString().substring(0, socketChannel.socket().getRemoteSocketAddress().toString().indexOf(":"));
/*  88 */           Long lastConnTime = connectedIps.get(remoteIp);
/*     */           
/*  90 */           if (lastConnTime != null) {
/*     */             
/*  92 */             long lct = lastConnTime.longValue();
/*     */             
/*  94 */             if (System.currentTimeMillis() - lct < MIN_MILLIS_BETWEEN_CONNECTIONS) {
/*     */               
/*  96 */               logger.log(Level.INFO, "Disconnecting " + remoteIp + " due to too many connections.");
/*  97 */               if (socketChannel != null && socketChannel.socket() != null) {
/*     */                 
/*     */                 try {
/*     */                   
/* 101 */                   socketChannel.socket().close();
/*     */                 }
/* 103 */                 catch (IOException iox) {
/*     */                   
/* 105 */                   iox.printStackTrace();
/*     */                 } 
/*     */               }
/* 108 */               if (socketChannel != null) {
/*     */                 
/*     */                 try {
/*     */                   
/* 112 */                   socketChannel.close();
/*     */                 }
/* 114 */                 catch (IOException iox) {
/*     */                   
/* 116 */                   iox.printStackTrace();
/*     */                 } 
/*     */               }
/* 119 */               keepGoing = false;
/*     */             } 
/*     */           } else {
/*     */             
/* 123 */             connectedIps.put(remoteIp, new Long(System.currentTimeMillis()));
/*     */           } 
/* 125 */         }  if (keepGoing)
/*     */         {
/* 127 */           socketChannel.configureBlocking(false);
/* 128 */           SocketConnection socketConnection = new SocketConnection(socketChannel, true, this.intraServer);
/* 129 */           CONNECTIONS_RW_LOCK.writeLock().lock();
/*     */           
/*     */           try {
/* 132 */             this.connections.add(socketConnection);
/*     */           }
/*     */           finally {
/*     */             
/* 136 */             CONNECTIONS_RW_LOCK.writeLock().unlock();
/*     */           } 
/* 138 */           this.serverListener.clientConnected(socketConnection);
/*     */         }
/*     */       
/* 141 */       } catch (IOException e) {
/*     */ 
/*     */         
/*     */         try {
/* 145 */           socketChannel.close();
/*     */         }
/* 147 */         catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */         
/* 151 */         throw e;
/*     */       } 
/*     */     } 
/*     */     
/* 155 */     CONNECTIONS_RW_LOCK.writeLock().lock();
/*     */     
/*     */     try {
/* 158 */       for (Iterator<SocketConnection> it = this.connections.iterator(); it.hasNext(); )
/*     */       {
/* 160 */         SocketConnection socketConnection = it.next();
/*     */         
/* 162 */         if (!socketConnection.isConnected()) {
/*     */           
/* 164 */           socketConnection.disconnect();
/* 165 */           this.serverListener.clientException(socketConnection, new Exception());
/* 166 */           it.remove();
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/*     */         try {
/* 172 */           socketConnection.tick();
/*     */         }
/* 174 */         catch (Exception e) {
/*     */           
/* 176 */           socketConnection.disconnect();
/* 177 */           this.serverListener.clientException(socketConnection, e);
/* 178 */           it.remove();
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     } finally {
/*     */       
/* 185 */       CONNECTIONS_RW_LOCK.writeLock().unlock();
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
/*     */   public int getNumberOfConnections() {
/* 197 */     CONNECTIONS_RW_LOCK.readLock().lock();
/*     */     
/*     */     try {
/* 200 */       if (this.connections != null)
/*     */       {
/* 202 */         return this.connections.size();
/*     */       }
/*     */ 
/*     */       
/* 206 */       return 0;
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 211 */       CONNECTIONS_RW_LOCK.readLock().unlock();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\communication\SocketServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */