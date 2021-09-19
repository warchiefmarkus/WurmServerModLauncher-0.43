/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.communication.SocketConnection;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
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
/*     */ public final class PlayerCommunicatorSender
/*     */   implements Runnable
/*     */ {
/*  34 */   private static Logger logger = Logger.getLogger(PlayerCommunicatorSender.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlayerCommunicatorSender() {
/*  41 */     logger.info("Creating");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*  52 */     logger.info("Starting on " + Thread.currentThread());
/*     */ 
/*     */     
/*     */     try {
/*  56 */       Player lPlayer = null;
/*     */ 
/*     */       
/*     */       while (true) {
/*  60 */         PlayerMessage lMessage = PlayerCommunicatorQueued.getMessageQueue().take();
/*  61 */         if (lMessage != null) {
/*     */           
/*  63 */           if (logger.isLoggable(Level.FINEST))
/*     */           {
/*  65 */             logger.finest("Removed " + lMessage);
/*     */           }
/*     */           
/*     */           try {
/*  69 */             lPlayer = Players.getInstance().getPlayer(lMessage.getPlayerId());
/*  70 */             SocketConnection lConnection = lPlayer.getCommunicator().getConnection();
/*  71 */             if (lPlayer.hasLink() && lConnection.isConnected())
/*     */             {
/*  73 */               ByteBuffer lBuffer = lConnection.getBuffer();
/*  74 */               lBuffer.put(lMessage.getMessageBytes());
/*  75 */               lConnection.flush();
/*     */ 
/*     */               
/*  78 */               if (!lConnection.tickWriting(1000000L))
/*     */               {
/*     */                 
/*  81 */                 logger.warning("Could not get a lock within 1ms to send message: " + lMessage);
/*     */ 
/*     */               
/*     */               }
/*  85 */               else if (logger.isLoggable(Level.FINEST))
/*     */               {
/*  87 */                 logger.finest("Sent message through connection: " + lMessage);
/*     */               
/*     */               }
/*     */ 
/*     */             
/*     */             }
/*  93 */             else if (logger.isLoggable(Level.FINEST))
/*     */             {
/*  95 */               logger.finest("Player is not connected so cannot send message: " + lMessage);
/*     */             }
/*     */           
/*     */           }
/*  99 */           catch (NoSuchPlayerException e) {
/*     */             
/* 101 */             logger.log(Level.WARNING, "Could not find Player for Message: " + lMessage + " - " + e.getMessage(), (Throwable)e);
/*     */           }
/* 103 */           catch (IOException e) {
/*     */             
/* 105 */             logger.log(Level.WARNING, lPlayer.getName() + ": Message: " + lMessage + " - " + e.getMessage(), e);
/* 106 */             lPlayer.setLink(false);
/*     */           } 
/*     */           
/* 109 */           Thread.yield();
/*     */           
/*     */           continue;
/*     */         } 
/* 113 */         logger.warning("Removed null message from Queue");
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 118 */     catch (RuntimeException e) {
/*     */       
/* 120 */       logger.log(Level.WARNING, "Problem running - " + e.getMessage(), e);
/*     */       
/* 122 */       Server.getInstance().initialisePlayerCommunicatorSender();
/*     */     }
/* 124 */     catch (InterruptedException e) {
/*     */ 
/*     */       
/* 127 */       logger.log(Level.WARNING, e.getMessage(), e);
/* 128 */       Server.getInstance().initialisePlayerCommunicatorSender();
/*     */     }
/*     */     finally {
/*     */       
/* 132 */       logger.info("Finished");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\PlayerCommunicatorSender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */