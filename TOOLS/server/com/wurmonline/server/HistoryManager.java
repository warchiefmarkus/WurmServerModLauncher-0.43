/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.LinkedList;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.concurrent.GuardedBy;
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
/*     */ public final class HistoryManager
/*     */ {
/*  46 */   private static final Logger logger = Logger.getLogger(HistoryManager.class.getName());
/*     */ 
/*     */   
/*     */   private static final String ADD_HISTORY = "INSERT INTO HISTORY(EVENTDATE,SERVER,PERFORMER,EVENT) VALUES (?,?,?,?)";
/*     */ 
/*     */   
/*     */   private static final String GET_HISTORY = "SELECT EVENTDATE, SERVER, PERFORMER, EVENT FROM HISTORY WHERE SERVER=? ORDER BY EVENTDATE DESC";
/*     */ 
/*     */   
/*     */   @GuardedBy("HISTORY_RW_LOCK")
/*  56 */   private static final LinkedList<HistoryEvent> HISTORY = new LinkedList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   private static final ReentrantReadWriteLock HISTORY_RW_LOCK = new ReentrantReadWriteLock();
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
/*     */   static HistoryEvent[] getHistoryEvents() {
/*  73 */     HISTORY_RW_LOCK.readLock().lock();
/*     */     
/*     */     try {
/*  76 */       return HISTORY.<HistoryEvent>toArray(new HistoryEvent[HISTORY.size()]);
/*     */     }
/*     */     finally {
/*     */       
/*  80 */       HISTORY_RW_LOCK.readLock().unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String[] getHistory(int numevents) {
/*     */     int lHistorySize;
/*  86 */     String[] hist = new String[0];
/*     */     
/*  88 */     HISTORY_RW_LOCK.readLock().lock();
/*     */     
/*     */     try {
/*  91 */       lHistorySize = HISTORY.size();
/*     */     }
/*     */     finally {
/*     */       
/*  95 */       HISTORY_RW_LOCK.readLock().unlock();
/*     */     } 
/*  97 */     if (lHistorySize > 0) {
/*     */       
/*  99 */       int numbersToFetch = Math.min(numevents, lHistorySize);
/*     */       
/* 101 */       hist = new String[numbersToFetch];
/* 102 */       HistoryEvent[] events = getHistoryEvents();
/* 103 */       for (int x = 0; x < numbersToFetch; x++)
/*     */       {
/* 105 */         hist[x] = events[x].getLongDesc();
/*     */       }
/*     */     } 
/* 108 */     return hist;
/*     */   }
/*     */ 
/*     */   
/*     */   static void loadHistory() {
/* 113 */     long start = System.nanoTime();
/* 114 */     Connection dbcon = null;
/* 115 */     PreparedStatement ps = null;
/* 116 */     ResultSet rs = null;
/* 117 */     HISTORY_RW_LOCK.writeLock().lock();
/*     */     
/*     */     try {
/* 120 */       dbcon = DbConnector.getLoginDbCon();
/* 121 */       ps = dbcon.prepareStatement("SELECT EVENTDATE, SERVER, PERFORMER, EVENT FROM HISTORY WHERE SERVER=? ORDER BY EVENTDATE DESC");
/* 122 */       ps.setInt(1, Servers.localServer.id);
/* 123 */       rs = ps.executeQuery();
/* 124 */       while (rs.next())
/*     */       {
/* 126 */         HISTORY.add(new HistoryEvent(rs.getLong("EVENTDATE"), rs.getString("PERFORMER"), rs.getString("EVENT"), rs
/* 127 */               .getInt("SERVER")));
/*     */       }
/*     */     }
/* 130 */     catch (SQLException sqx) {
/*     */       
/* 132 */       logger.log(Level.WARNING, "Problem loading History for loacl server id: " + Servers.localServer.id + " due to " + sqx
/* 133 */           .getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 137 */       HISTORY_RW_LOCK.writeLock().unlock();
/*     */       
/* 139 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 140 */       DbConnector.returnConnection(dbcon);
/*     */       
/* 142 */       float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 143 */       logger.info("Loaded " + HISTORY.size() + " HISTORY events from the database took " + lElapsedTime + " ms");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addHistory(String performerName, String event) {
/* 149 */     addHistory(performerName, event, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addHistory(String performerName, String event, boolean twit) {
/* 154 */     HISTORY_RW_LOCK.writeLock().lock();
/*     */     
/*     */     try {
/* 157 */       HISTORY.addFirst(new HistoryEvent(System.currentTimeMillis(), performerName, event, Servers.localServer.id));
/*     */     }
/*     */     finally {
/*     */       
/* 161 */       HISTORY_RW_LOCK.writeLock().unlock();
/*     */     } 
/* 163 */     Connection dbcon = null;
/* 164 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 167 */       dbcon = DbConnector.getLoginDbCon();
/* 168 */       ps = dbcon.prepareStatement("INSERT INTO HISTORY(EVENTDATE,SERVER,PERFORMER,EVENT) VALUES (?,?,?,?)");
/* 169 */       ps.setLong(1, System.currentTimeMillis());
/* 170 */       ps.setInt(2, Servers.localServer.id);
/* 171 */       ps.setString(3, performerName);
/* 172 */       ps.setString(4, event);
/* 173 */       ps.executeUpdate();
/*     */     }
/* 175 */     catch (SQLException sqx) {
/*     */       
/* 177 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 181 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 182 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 184 */     if (twit)
/* 185 */       Server.getInstance().twitLocalServer(performerName + " " + event); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\HistoryManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */