/*     */ package com.wurmonline.server.intra;
/*     */ 
/*     */ import com.wurmonline.server.Constants;
/*     */ import com.wurmonline.server.Server;
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
/*     */ public final class TimeSync
/*     */   extends IntraCommand
/*     */ {
/*  35 */   private static final Logger logger = Logger.getLogger(TimeSync.class.getName());
/*     */ 
/*     */   
/*     */   private boolean done = false;
/*     */   
/*     */   private IntraClient client;
/*     */   
/*     */   private boolean sentCommand = false;
/*     */   
/*     */   private boolean started = false;
/*     */ 
/*     */   
/*     */   public boolean poll() {
/*  48 */     if (isThisLoginServer()) {
/*  49 */       return true;
/*     */     }
/*     */     
/*  52 */     if (this.client == null && (System.currentTimeMillis() > this.timeOutAt || !this.started)) {
/*     */       
/*     */       try {
/*     */         
/*  56 */         this.timeOutAt = System.currentTimeMillis() + this.timeOutTime;
/*  57 */         this
/*  58 */           .client = new IntraClient(getLoginServerIntraServerAddress(), Integer.parseInt(getLoginServerIntraServerPort()), this);
/*  59 */         this.client.login(getLoginServerIntraServerPassword(), true);
/*  60 */         this.started = true;
/*     */       }
/*  62 */       catch (IOException iox) {
/*     */         
/*  64 */         this.done = true;
/*     */       } 
/*     */     }
/*  67 */     if (this.client != null && !this.done) {
/*     */       
/*  69 */       if (System.currentTimeMillis() > this.timeOutAt) {
/*     */         
/*  71 */         this.done = true;
/*     */       }
/*  73 */       else if (this.client != null && this.client.loggedIn) {
/*     */         
/*  75 */         if (logger.isLoggable(Level.FINER))
/*     */         {
/*  77 */           logger.finer("3.5 sentcommand=" + this.sentCommand);
/*     */         }
/*  79 */         if (!this.sentCommand) {
/*     */           
/*     */           try {
/*     */             
/*  83 */             this.client.executeSyncCommand();
/*  84 */             this.timeOutAt = System.currentTimeMillis() + this.timeOutTime;
/*  85 */             this.sentCommand = true;
/*     */           }
/*  87 */           catch (IOException iox) {
/*     */             
/*  89 */             logger.log(Level.WARNING, iox.getMessage(), iox);
/*  90 */             this.done = true;
/*     */           } 
/*     */         }
/*     */       } 
/*  94 */       if (!this.done) {
/*     */         
/*     */         try {
/*     */           
/*  98 */           this.client.update();
/*     */         }
/* 100 */         catch (Exception ex) {
/*     */           
/* 102 */           logger.log(Level.WARNING, ex.getMessage(), ex);
/* 103 */           this.done = true;
/*     */         } 
/*     */       }
/*     */     } 
/* 107 */     if (this.client != null && this.done) {
/*     */       
/* 109 */       this.client.disconnect("Done");
/* 110 */       this.client = null;
/*     */     } 
/* 112 */     return this.done;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void commandExecuted(IntraClient aClient) {
/* 119 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void commandFailed(IntraClient aClient) {
/* 125 */     logger.warning("Command failed for Client: " + aClient);
/* 126 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dataReceived(IntraClient aClient) {
/* 132 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reschedule(IntraClient aClient) {
/* 141 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(IntraClient aClient) {
/* 150 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void receivingData(ByteBuffer buffer) {
/* 159 */     this.done = true;
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
/*     */   public static class TimeSyncSender
/*     */     implements Runnable
/*     */   {
/*     */     public TimeSyncSender() {
/* 177 */       if (Servers.isThisLoginServer())
/*     */       {
/* 179 */         throw new IllegalArgumentException("Do not send TimeSync commands from the LoginServer");
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/* 192 */       if (TimeSync.logger.isLoggable(Level.FINER))
/*     */       {
/* 194 */         TimeSync.logger.finer("Running newSingleThreadScheduledExecutor for sending TimeSync commands");
/*     */       }
/*     */       
/*     */       try {
/* 198 */         long now = System.currentTimeMillis();
/*     */         
/* 200 */         TimeSync synch = new TimeSync();
/* 201 */         Server.getInstance().addIntraCommand(synch);
/*     */         
/* 203 */         long lElapsedTime = System.currentTimeMillis() - now;
/* 204 */         if (lElapsedTime > Constants.lagThreshold)
/*     */         {
/* 206 */           TimeSync.logger.info("Finished sending TimeSync command, which took " + lElapsedTime + " millis.");
/*     */         
/*     */         }
/*     */       }
/* 210 */       catch (RuntimeException e) {
/*     */         
/* 212 */         TimeSync.logger.log(Level.WARNING, "Caught exception in ScheduledExecutorService while sending TimeSync command", e);
/* 213 */         throw e;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\intra\TimeSync.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */