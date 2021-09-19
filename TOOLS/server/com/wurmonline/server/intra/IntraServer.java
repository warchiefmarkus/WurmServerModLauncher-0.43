/*     */ package com.wurmonline.server.intra;
/*     */ 
/*     */ import com.wurmonline.communication.ServerListener;
/*     */ import com.wurmonline.communication.SocketConnection;
/*     */ import com.wurmonline.communication.SocketServer;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.ServerMonitoring;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
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
/*     */ 
/*     */ 
/*     */ public final class IntraServer
/*     */   implements ServerListener, CounterTypes, MiscConstants, TimeConstants
/*     */ {
/*  38 */   private static final Logger logger = Logger.getLogger(IntraServer.class.getName());
/*     */   
/*     */   public SocketServer socketServer;
/*     */   
/*     */   private final ServerMonitoring wurmserver;
/*     */   
/*     */   public IntraServer(ServerMonitoring server) throws IOException {
/*  45 */     this.wurmserver = server;
/*  46 */     this.socketServer = new SocketServer(server.getInternalIp(), server.getIntraServerPort(), server.getIntraServerPort() + 1, this);
/*     */     
/*  48 */     this.socketServer.intraServer = true;
/*  49 */     logger.log(Level.INFO, "Intraserver listening on " + 
/*     */         
/*  51 */         InetAddress.getByAddress(server.getInternalIp()) + ':' + server
/*  52 */         .getIntraServerPort());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clientConnected(SocketConnection serverConnection) {
/*     */     try {
/*  63 */       IntraServerConnection conn = new IntraServerConnection(serverConnection, this.wurmserver);
/*  64 */       serverConnection.setConnectionListener(conn);
/*  65 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/*  67 */         logger.fine("IntraServer client connected from IP " + serverConnection.getIp());
/*     */       }
/*     */     }
/*  70 */     catch (Exception ex) {
/*     */       
/*  72 */       logger.log(Level.SEVERE, "Failed to create intraserver connection: " + serverConnection + '.', ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clientException(SocketConnection conn, Exception ex) {
/*  83 */     if (logger.isLoggable(Level.FINE))
/*     */     {
/*  85 */       logger.log(Level.FINE, "Remote server lost link on connection: " + conn + " - cause:" + ex.getMessage(), ex);
/*     */     }
/*     */     
/*  88 */     if (conn != null) {
/*     */ 
/*     */       
/*     */       try {
/*  92 */         conn.flush();
/*     */       }
/*  94 */       catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */       
/*  98 */       conn.sendShutdown();
/*     */       
/*     */       try {
/* 101 */         conn.disconnect();
/*     */       }
/* 103 */       catch (Exception exception) {}
/*     */ 
/*     */       
/* 106 */       conn.closeChannel();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\intra\IntraServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */