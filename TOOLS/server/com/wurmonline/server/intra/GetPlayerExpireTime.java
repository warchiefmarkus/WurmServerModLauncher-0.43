/*     */ package com.wurmonline.server.intra;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
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
/*     */ final class GetPlayerExpireTime
/*     */   extends IntraCommand
/*     */   implements MiscConstants
/*     */ {
/*  38 */   private static final Logger logger = Logger.getLogger(GetPlayerExpireTime.class.getName());
/*     */   
/*     */   private boolean done = false;
/*     */   
/*     */   private IntraClient client;
/*     */   private final long wurmId;
/*     */   
/*     */   private GetPlayerExpireTime(long playerId) {
/*  46 */     this.wurmId = playerId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean poll() {
/*  52 */     if (Servers.isThisLoginServer()) {
/*  53 */       return true;
/*     */     }
/*     */     
/*  56 */     if (this.client == null) {
/*     */       
/*     */       try {
/*     */         
/*  60 */         this.client = new IntraClient(Servers.loginServer.INTRASERVERADDRESS, Integer.parseInt(Servers.loginServer.INTRASERVERPORT), this);
/*  61 */         this.client.login(Servers.loginServer.INTRASERVERPASSWORD, true);
/*     */       }
/*  63 */       catch (IOException iox) {
/*     */         
/*  65 */         this.done = true;
/*     */       } 
/*     */     }
/*  68 */     if (this.client != null && !this.done) {
/*     */       
/*  70 */       if (System.currentTimeMillis() > this.timeOutAt)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/*  75 */         this.done = true;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  82 */       if (this.client.loggedIn) {
/*     */         
/*     */         try {
/*     */           
/*  86 */           this.client.executeRequestPlayerPaymentExpire(this.wurmId);
/*     */         }
/*  88 */         catch (IOException iox) {
/*     */           
/*  90 */           logger.log(Level.WARNING, iox.getMessage(), iox);
/*  91 */           this.done = true;
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
/* 102 */           this.done = true;
/*     */         } 
/*     */       }
/*     */     } 
/* 106 */     if (this.done && this.client != null) {
/*     */       
/* 108 */       this.client.disconnect("Done");
/* 109 */       this.client = null;
/*     */     } 
/* 111 */     return this.done;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void commandExecuted(IntraClient aClient) {
/* 118 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void commandFailed(IntraClient aClient) {
/* 124 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dataReceived(IntraClient aClient) {
/* 130 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void reschedule(IntraClient aClient) {
/* 136 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(IntraClient aClient) {
/* 145 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void receivingData(ByteBuffer buffer) {
/* 154 */     long expireTime = buffer.getLong();
/* 155 */     PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(this.wurmId);
/* 156 */     long oldExpire = info.getPaymentExpire();
/*     */     
/*     */     try {
/* 159 */       if (expireTime > oldExpire)
/*     */       {
/* 161 */         info.setPaymentExpire(expireTime);
/*     */         
/*     */         try {
/* 164 */           Player p = Players.getInstance().getPlayer(this.wurmId);
/* 165 */           p.getCommunicator().sendNormalServerMessage("Your payment expiration date is updated to " + WurmCalendar.formatGmt(expireTime));
/*     */         }
/* 167 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 172 */     catch (IOException iox) {
/*     */       
/* 174 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/*     */     } 
/* 176 */     this.done = true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\intra\GetPlayerExpireTime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */