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
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public final class TimeTransfer
/*     */   extends IntraCommand
/*     */   implements MiscConstants
/*     */ {
/*  49 */   private static final Logger logger = Logger.getLogger(TimeTransfer.class.getName());
/*  50 */   private static final Logger moneylogger = Logger.getLogger("Money");
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final long wurmid;
/*     */   
/*     */   private final int monthsadded;
/*     */   private final int daysadded;
/*     */   private final boolean dealItems;
/*     */   private final String detail;
/*  60 */   public static final List<TimeTransfer> transfers = new LinkedList<>();
/*     */   private boolean done = false;
/*  62 */   private IntraClient client = null;
/*     */   private boolean started = false;
/*     */   public boolean deleted = false;
/*     */   private boolean sentTransfer = false;
/*  66 */   private static final Map<Long, Set<TimeTransfer>> batchTransfers = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeTransfer(String aName, long playerId, int months, boolean _dealItems, int days, String transactionDetail, boolean load) {
/*  72 */     this.name = aName;
/*  73 */     this.wurmid = playerId;
/*  74 */     this.monthsadded = months;
/*  75 */     this.daysadded = days;
/*  76 */     this.dealItems = _dealItems;
/*  77 */     this.detail = transactionDetail.substring(0, Math.min(19, transactionDetail.length()));
/*  78 */     if (!load)
/*     */     {
/*  80 */       save();
/*     */     }
/*  82 */     transfers.add(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeTransfer(String aName, long playerId, int months, boolean _dealItems, int days, String transactionDetail) {
/*  88 */     this.name = aName;
/*  89 */     this.wurmid = playerId;
/*  90 */     this.monthsadded = months;
/*  91 */     this.daysadded = days;
/*  92 */     this.dealItems = _dealItems;
/*  93 */     this.detail = transactionDetail.substring(0, Math.min(19, transactionDetail.length()));
/*  94 */     saveProcessed();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeTransfer(long playerId, String aName, int months, boolean _dealItems, int days, String transactionDetail) {
/* 100 */     this.name = aName;
/* 101 */     this.wurmid = playerId;
/* 102 */     this.monthsadded = months;
/* 103 */     this.daysadded = days;
/* 104 */     this.dealItems = _dealItems;
/* 105 */     this.detail = transactionDetail.substring(0, Math.min(19, transactionDetail.length()));
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
/*     */   private void saveProcessed() {}
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
/*     */   private void save() {}
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
/*     */   private void process() {
/* 146 */     this.deleted = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Set<TimeTransfer> getTimeTransfersFor(long id) {
/* 151 */     return batchTransfers.get(Long.valueOf(id));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean poll() {
/* 157 */     this.pollTimes++;
/*     */     
/* 159 */     PlayerInfo info = PlayerInfoFactory.createPlayerInfo(this.name);
/*     */     
/*     */     try {
/* 162 */       info.load();
/*     */     }
/* 164 */     catch (Exception eex) {
/*     */       
/* 166 */       logger.log(Level.WARNING, "Failed to load info for wurmid " + this.wurmid + ".", eex);
/*     */     } 
/* 168 */     if (info.wurmId <= 0L) {
/*     */       
/* 170 */       logger.log(Level.WARNING, "Failed to load info for wurmid " + this.wurmid + ". No info available.");
/* 171 */       this.done = true;
/*     */     }
/* 173 */     else if (info.currentServer == Servers.localServer.id) {
/*     */       
/* 175 */       process();
/* 176 */       this.done = true;
/*     */     }
/* 178 */     else if (this.client == null && (System.currentTimeMillis() > this.timeOutAt || !this.started)) {
/*     */       
/* 180 */       ServerEntry entry = Servers.getServerWithId(info.currentServer);
/* 181 */       if (entry != null && entry.isAvailable(5, true)) {
/*     */         
/*     */         try
/*     */         {
/* 185 */           this.started = true;
/* 186 */           this.startTime = System.currentTimeMillis();
/* 187 */           this.timeOutAt = this.startTime + this.timeOutTime;
/* 188 */           this.client = new IntraClient(entry.INTRASERVERADDRESS, Integer.parseInt(entry.INTRASERVERPORT), this);
/* 189 */           this.client.login(entry.INTRASERVERPASSWORD, true);
/* 190 */           this.done = false;
/*     */         }
/* 192 */         catch (IOException iox)
/*     */         {
/* 194 */           this.done = true;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 199 */         this.timeOutAt = this.startTime + this.timeOutTime;
/* 200 */         this.done = true;
/* 201 */         if (entry == null)
/* 202 */           logger.log(Level.WARNING, "No server entry for server with id " + info.currentServer); 
/*     */       } 
/*     */     } 
/* 205 */     if (this.client != null && !this.done) {
/*     */       
/* 207 */       if (System.currentTimeMillis() > this.timeOutAt) {
/*     */         
/* 209 */         this.timeOutAt = System.currentTimeMillis() + this.timeOutTime;
/* 210 */         this.done = true;
/*     */       } 
/* 212 */       if (this.client.loggedIn && !this.done)
/*     */       {
/* 214 */         if (!this.sentTransfer) {
/*     */           
/*     */           try {
/*     */             
/* 218 */             this.timeOutAt = this.startTime + this.timeOutTime;
/* 219 */             this.client.executeExpireUpdate(this.wurmid, info.getPaymentExpire(), this.detail, this.daysadded, this.monthsadded, this.dealItems);
/* 220 */             this.sentTransfer = true;
/*     */           }
/* 222 */           catch (IOException iox) {
/*     */             
/* 224 */             logger.log(Level.WARNING, this + ", " + iox.getMessage(), iox);
/* 225 */             this.done = true;
/*     */           } 
/*     */         }
/*     */       }
/*     */       
/* 230 */       if (!this.done) {
/*     */         
/*     */         try {
/*     */           
/* 234 */           this.client.update();
/*     */         }
/* 236 */         catch (Exception ex) {
/*     */           
/* 238 */           this.done = true;
/*     */         } 
/*     */       }
/*     */     } 
/* 242 */     if (this.done && this.client != null) {
/*     */       
/* 244 */       this.sentTransfer = false;
/* 245 */       this.client.disconnect("Done");
/* 246 */       this.client = null;
/*     */     } 
/*     */ 
/*     */     
/* 250 */     return this.done;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reschedule(IntraClient aClient) {
/* 259 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(IntraClient aClient) {
/* 268 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void commandExecuted(IntraClient aClient) {
/* 274 */     process();
/* 275 */     logger2.log(Level.INFO, "TT accepted " + num);
/* 276 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void commandFailed(IntraClient aClient) {
/* 282 */     this.done = true;
/* 283 */     logger2.log(Level.INFO, "TT rejected " + num + " for " + this.wurmid + " " + this.name);
/* 284 */     this.deleted = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dataReceived(IntraClient aClient) {
/* 290 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void receivingData(ByteBuffer buffer) {
/* 299 */     this.done = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getDaysAdded() {
/* 304 */     return this.daysadded;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getMonthsAdded() {
/* 309 */     return this.monthsadded;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 320 */     return "TimeTransfer [num: " + num + ", name: " + this.name + ", ID: " + this.wurmid + ", Months Added: " + this.monthsadded + ", Days Added: " + this.daysadded + ", detail: " + this.detail + ", done: " + this.done + ", started: " + this.started + ", deleted: " + this.deleted + ", sentTransfer: " + this.sentTransfer + ", IntraClient: " + this.client + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\intra\TimeTransfer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */