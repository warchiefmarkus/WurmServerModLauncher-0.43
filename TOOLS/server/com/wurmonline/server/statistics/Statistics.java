/*     */ package com.wurmonline.server.statistics;
/*     */ 
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.questions.Questions;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
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
/*     */ public final class Statistics
/*     */   extends TimerTask
/*     */ {
/*  35 */   private static Logger log = null;
/*  36 */   private long totalBytesIn = 0L;
/*  37 */   private long totalBytesOut = 0L;
/*  38 */   private long currentBytesIn = 0L;
/*  39 */   private long currentBytesOut = 0L;
/*     */   
/*  41 */   private long playerCount = 0L;
/*     */   private static final long fivemin = 300000L;
/*     */   private static final long Onemeg = 1024000L;
/*  44 */   private static Statistics instance = null;
/*     */   
/*  46 */   private long creationTime = System.currentTimeMillis();
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
/*     */   public static Statistics getInstance() {
/*  59 */     if (instance == null)
/*     */     {
/*  61 */       instance = new Statistics();
/*     */     }
/*  63 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startup(Logger logger) {
/*  68 */     log = logger;
/*  69 */     Timer timer = new Timer();
/*  70 */     timer.scheduleAtFixedRate(this, 300000L, 300000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*  79 */     Runtime rt = Runtime.getRuntime();
/*  80 */     countBytes();
/*  81 */     log.info("current mem in use: " + (rt.totalMemory() / 1024000L) + "M free mem: " + (rt.freeMemory() / 1024000L) + "M Max mem: " + (rt.maxMemory() / 1024000L) + "M\nplayer count: " + this.playerCount + "\nbytes in: " + this.currentBytesIn + " bytes out: " + this.currentBytesOut + " total in: " + this.totalBytesIn + " total out: " + this.totalBytesOut + '\n' + "Server uptime: " + ((
/*     */         
/*  83 */         System.currentTimeMillis() - this.creationTime) / 1000L) + " seconds. Unanswered questions:" + 
/*  84 */         Questions.getNumUnanswered());
/*     */   }
/*     */ 
/*     */   
/*     */   private void countBytes() {
/*  89 */     long bytesIn = 0L;
/*  90 */     long bytesOut = 0L;
/*     */     
/*  92 */     Player[] players = Players.getInstance().getPlayers();
/*  93 */     this.playerCount = players.length;
/*  94 */     for (int x = 0; x != players.length; x++) {
/*     */       
/*  96 */       if (players[x].hasLink()) {
/*     */         
/*  98 */         bytesIn += players[x].getCommunicator().getConnection().getReadBytes();
/*  99 */         bytesOut += players[x].getCommunicator().getConnection().getSentBytes();
/*     */       } 
/*     */     } 
/* 102 */     this.currentBytesIn = bytesIn - this.totalBytesIn;
/* 103 */     this.currentBytesOut = bytesOut - this.totalBytesOut;
/* 104 */     if (this.currentBytesIn < 0L)
/* 105 */       this.currentBytesIn = 0L; 
/* 106 */     if (this.currentBytesOut < 0L)
/* 107 */       this.currentBytesOut = 0L; 
/* 108 */     this.totalBytesIn += this.currentBytesIn;
/* 109 */     this.totalBytesOut += this.currentBytesOut;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\statistics\Statistics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */