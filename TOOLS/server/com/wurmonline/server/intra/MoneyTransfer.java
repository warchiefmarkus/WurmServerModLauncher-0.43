/*     */ package com.wurmonline.server.intra;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.ServerEntry;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentLinkedDeque;
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
/*     */ 
/*     */ 
/*     */ public final class MoneyTransfer
/*     */   extends IntraCommand
/*     */   implements MiscConstants
/*     */ {
/*  49 */   private static final Logger logger = Logger.getLogger(MoneyTransfer.class.getName());
/*     */   
/*  51 */   private static final Logger moneylogger = Logger.getLogger("Money");
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final long wurmid;
/*     */   
/*     */   private final long newMoney;
/*     */   private final long moneyAdded;
/*     */   private final String detail;
/*     */   private boolean done = false;
/*  61 */   private IntraClient client = null;
/*     */   
/*     */   private boolean started = false;
/*     */   
/*     */   public boolean deleted = false;
/*     */   private boolean sentTransfer = false;
/*     */   public static final byte EXECUTOR_NONE = 0;
/*     */   public static final byte EXECUTOR_BLVDMEDIA = 1;
/*     */   public static final byte EXECUTOR_SUPERREWARDS = 2;
/*     */   public static final byte EXECUTOR_INGAME_BONUS = 3;
/*     */   public static final byte EXECUTOR_PAYPAL = 4;
/*     */   public static final byte EXECUTOR_INGAME_REFER = 5;
/*     */   public static final byte EXECUTOR_INGAME_SHOP = 6;
/*     */   public static final byte EXECUTOR_ALLOPASS = 7;
/*     */   public static final byte EXECUTOR_COINLAB = 8;
/*     */   public static final byte EXECUTOR_XSOLLA = 9;
/*     */   private final byte paymentExecutor;
/*     */   private final String campaignId;
/*  79 */   public static final ConcurrentLinkedDeque<MoneyTransfer> transfers = new ConcurrentLinkedDeque<>();
/*  80 */   private static final Map<Long, Set<MoneyTransfer>> batchTransfers = new HashMap<>();
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
/*     */   public MoneyTransfer(String aName, long playerId, long money, long _moneyAdded, String transactionDetail, byte executor, String campid, boolean load) {
/* 101 */     this.name = aName;
/* 102 */     this.wurmid = playerId;
/* 103 */     this.newMoney = money;
/* 104 */     this.detail = transactionDetail.substring(0, Math.min(39, transactionDetail.length()));
/* 105 */     this.paymentExecutor = executor;
/* 106 */     this.campaignId = campid;
/* 107 */     this.moneyAdded = _moneyAdded;
/* 108 */     if (!load)
/*     */     {
/* 110 */       save();
/*     */     }
/* 112 */     transfers.add(this);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public MoneyTransfer(long playerId, String aName, long money, long _moneyAdded, String transactionDetail, byte executor, String campid) {
/* 130 */     this.name = aName;
/* 131 */     this.wurmid = playerId;
/* 132 */     this.newMoney = money;
/* 133 */     this.detail = transactionDetail.substring(0, Math.min(39, transactionDetail.length()));
/* 134 */     this.paymentExecutor = executor;
/* 135 */     this.campaignId = campid;
/* 136 */     this.moneyAdded = _moneyAdded;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public MoneyTransfer(String aName, long playerId, long money, long _moneyAdded, String transactionDetail, byte executor, String campId) {
/* 154 */     this.name = aName;
/* 155 */     this.wurmid = playerId;
/* 156 */     this.newMoney = money;
/* 157 */     this.moneyAdded = _moneyAdded;
/* 158 */     this.detail = transactionDetail.substring(0, Math.min(39, transactionDetail.length()));
/* 159 */     this.campaignId = campId;
/* 160 */     this.paymentExecutor = executor;
/* 161 */     saveProcessed();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void saveProcessed() {
/* 172 */     this.deleted = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void save() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process() {
/* 192 */     this.deleted = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Set<MoneyTransfer> getMoneyTransfersFor(long id) {
/* 197 */     return batchTransfers.get(Long.valueOf(id));
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
/*     */   public boolean poll() {
/* 237 */     this.pollTimes++;
/* 238 */     PlayerInfo info = PlayerInfoFactory.createPlayerInfo(this.name);
/*     */     
/*     */     try {
/* 241 */       info.load();
/*     */     }
/* 243 */     catch (Exception eex) {
/*     */       
/* 245 */       logger.log(Level.WARNING, "Failed to load player info for wurmid " + this.wurmid + ".", eex);
/*     */     } 
/* 247 */     if (info.wurmId <= 0L) {
/*     */       
/* 249 */       logger.log(Level.WARNING, "Failed to load player info for wurmid " + this.wurmid + ". No info available.");
/* 250 */       this.done = true;
/*     */     }
/* 252 */     else if (info.currentServer == Servers.localServer.id) {
/*     */       
/* 254 */       logger2
/* 255 */         .log(Level.INFO, "MT Processing " + num + ", name: " + this.name + ", wurmid: " + this.wurmid + ", money: " + this.newMoney);
/*     */       
/* 257 */       process();
/* 258 */       this.done = true;
/*     */     }
/* 260 */     else if (this.client == null && (System.currentTimeMillis() > this.timeOutAt || !this.started)) {
/*     */ 
/*     */       
/* 263 */       ServerEntry entry = Servers.getServerWithId(info.currentServer);
/* 264 */       if (entry != null && entry.isAvailable(5, true)) {
/*     */         
/*     */         try
/*     */         {
/* 268 */           this.startTime = System.currentTimeMillis();
/* 269 */           this.timeOutAt = this.startTime + this.timeOutTime;
/* 270 */           this.started = true;
/* 271 */           this.client = new IntraClient(entry.INTRASERVERADDRESS, Integer.parseInt(entry.INTRASERVERPORT), this);
/* 272 */           this.client.login(entry.INTRASERVERPASSWORD, true);
/*     */           
/* 274 */           this.done = false;
/*     */         }
/* 276 */         catch (IOException iox)
/*     */         {
/* 278 */           this.done = true;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 283 */         this.timeOutAt = this.startTime + this.timeOutTime;
/* 284 */         this.done = true;
/* 285 */         if (entry == null)
/* 286 */           logger.log(Level.WARNING, "No available server entry for server with id " + info.currentServer); 
/*     */       } 
/*     */     } 
/* 289 */     if (this.client != null && !this.done) {
/*     */       
/* 291 */       if (System.currentTimeMillis() > this.timeOutAt) {
/*     */         
/* 293 */         logger2.log(Level.INFO, "MT timeout " + num + ", name: " + this.name + ", wurmid: " + this.wurmid + ", money: " + this.newMoney);
/*     */         
/* 295 */         this.done = true;
/*     */       } 
/* 297 */       if (this.client.loggedIn && !this.done)
/*     */       {
/* 299 */         if (!this.sentTransfer) {
/*     */           
/*     */           try {
/*     */             
/* 303 */             this.client.executeMoneyUpdate(this.wurmid, this.newMoney, this.moneyAdded, this.detail);
/* 304 */             this.timeOutAt = System.currentTimeMillis() + this.timeOutTime;
/* 305 */             this.sentTransfer = true;
/*     */           }
/* 307 */           catch (IOException iox) {
/*     */             
/* 309 */             logger2.log(Level.WARNING, "Problem calling IntraClient.executeMoneyUpdate() for " + this + " due to " + iox
/* 310 */                 .getMessage(), iox);
/* 311 */             this.done = true;
/*     */           } 
/*     */         }
/*     */       }
/* 315 */       if (!this.done) {
/*     */         
/*     */         try {
/*     */           
/* 319 */           this.client.update();
/*     */         }
/* 321 */         catch (Exception ex) {
/*     */           
/* 323 */           this.done = true;
/* 324 */           if (logger.isLoggable(Level.FINE))
/*     */           {
/* 326 */             logger.log(Level.FINE, "Problem calling IntraClient.update() but hopefully not serious", ex);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 331 */     if (this.done && this.client != null) {
/*     */       
/* 333 */       this.sentTransfer = false;
/* 334 */       this.client.disconnect("Done");
/* 335 */       this.client = null;
/* 336 */       logger2.log(Level.INFO, "MT Disconnected " + num + ", name: " + this.name + ", wurmid: " + this.wurmid + ", money: " + this.newMoney);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 341 */     return this.done;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getMoneyAdded() {
/* 346 */     return this.moneyAdded;
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
/*     */   public void reschedule(IntraClient aClient) {
/* 359 */     this.done = true;
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
/*     */   public void remove(IntraClient aClient) {
/* 372 */     this.done = true;
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
/*     */   public void commandExecuted(IntraClient aClient) {
/* 385 */     process();
/* 386 */     logger2.log(Level.INFO, "MT accepted " + num + ", name: " + this.name + ", wurmid: " + this.wurmid + ", money: " + this.newMoney);
/* 387 */     this.done = true;
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
/*     */   public void commandFailed(IntraClient aClient) {
/* 400 */     this.done = true;
/* 401 */     logger2.log(Level.INFO, "MT rejected " + num + ", name: " + this.name + ", wurmid: " + this.wurmid + ", money: " + this.newMoney);
/* 402 */     this.deleted = true;
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
/*     */   public void dataReceived(IntraClient aClient) {
/* 415 */     this.done = true;
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
/*     */   public void receivingData(ByteBuffer buffer) {
/* 428 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 434 */     return "MoneyTransfer [num: " + num + ", wurmid: " + this.wurmid + ", name: " + this.name + ", detail: " + this.detail + ", newMoney: " + this.newMoney + ", moneyAdded: " + this.moneyAdded + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\intra\MoneyTransfer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */