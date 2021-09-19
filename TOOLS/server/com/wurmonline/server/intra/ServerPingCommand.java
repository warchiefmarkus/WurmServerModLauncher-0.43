/*     */ package com.wurmonline.server.intra;
/*     */ 
/*     */ import com.wurmonline.server.ServerEntry;
/*     */ import com.wurmonline.server.Servers;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ServerPingCommand
/*     */   extends IntraCommand
/*     */ {
/*  39 */   private static final Logger logger = Logger.getLogger(ServerPingCommand.class.getName());
/*     */   
/*     */   private final ServerEntry server;
/*     */   
/*     */   private boolean done = false;
/*     */   private IntraClient client;
/*     */   private boolean pinging = false;
/*     */   
/*     */   ServerPingCommand(ServerEntry serverEntry) {
/*  48 */     this.server = serverEntry;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean poll() {
/*  54 */     if (this.server.id == Servers.localServer.id) {
/*  55 */       return true;
/*     */     }
/*     */     
/*  58 */     if (this.client == null) {
/*     */       
/*  60 */       if (logger.isLoggable(Level.FINER))
/*     */       {
/*  62 */         logger.finer("1 " + getLoginServerIntraServerAddress() + ", " + getLoginServerIntraServerPort() + ", " + 
/*  63 */             getLoginServerIntraServerPassword());
/*     */       }
/*     */       
/*     */       try {
/*  67 */         this
/*  68 */           .client = new IntraClient(getLoginServerIntraServerAddress(), Integer.parseInt(getLoginServerIntraServerPort()), this);
/*  69 */         this.client.login(this.server.INTRASERVERPASSWORD, true);
/*  70 */         logger.log(Level.INFO, "connecting to " + this.server.id);
/*     */       }
/*  72 */       catch (IOException iox) {
/*     */         
/*  74 */         this.server.setAvailable(false, false, 0, 0, 0, 10);
/*     */         
/*  76 */         this.client.disconnect("Failed.");
/*  77 */         this.client = null;
/*  78 */         logger.log(Level.INFO, "Failed");
/*  79 */         this.done = true;
/*     */       } 
/*     */     } 
/*  82 */     if (this.client != null && !this.done) {
/*     */ 
/*     */ 
/*     */       
/*  86 */       if (System.currentTimeMillis() > this.timeOutAt)
/*     */       {
/*  88 */         this.done = true;
/*     */       }
/*     */       
/*  91 */       if (!this.done) {
/*     */         
/*     */         try {
/*     */           
/*  95 */           if (this.client.loggedIn)
/*     */           {
/*  97 */             if (!this.pinging) {
/*     */               
/*  99 */               this.client.executePingCommand();
/* 100 */               this.pinging = true;
/* 101 */               this.timeOutAt = System.currentTimeMillis() + this.timeOutTime;
/*     */             } 
/*     */           }
/*     */           
/* 105 */           if (!this.done)
/*     */           {
/* 107 */             this.client.update();
/*     */           }
/*     */         }
/* 110 */         catch (IOException iox) {
/*     */           
/* 112 */           this.server.setAvailable(false, false, 0, 0, 0, 10);
/*     */           
/* 114 */           this.done = true;
/*     */         } 
/*     */       }
/* 117 */       if (this.done && this.client != null) {
/*     */ 
/*     */         
/* 120 */         this.client.disconnect("Done");
/* 121 */         this.client = null;
/*     */       } 
/*     */     } 
/* 124 */     return this.done;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void commandExecuted(IntraClient aClient) {
/* 131 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void commandFailed(IntraClient aClient) {
/* 137 */     this.server.setAvailable(false, false, 0, 0, 0, 10);
/*     */     
/* 139 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dataReceived(IntraClient aClient) {
/* 145 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reschedule(IntraClient aClient) {
/* 154 */     this.server.setAvailable(false, false, 0, 0, 0, 10);
/* 155 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(IntraClient aClient) {
/* 164 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void receivingData(ByteBuffer buffer) {
/* 173 */     boolean maintaining = ((buffer.get() & 0x1) == 1);
/* 174 */     int numsPlaying = buffer.getInt();
/* 175 */     int maxLimit = buffer.getInt();
/* 176 */     int secsToShutdown = buffer.getInt();
/* 177 */     int meshSize = buffer.getInt();
/* 178 */     this.server.setAvailable(true, maintaining, numsPlaying, maxLimit, secsToShutdown, meshSize);
/*     */     
/* 180 */     this.timeOutAt = System.currentTimeMillis() + this.timeOutTime;
/* 181 */     this.done = true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\intra\ServerPingCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */