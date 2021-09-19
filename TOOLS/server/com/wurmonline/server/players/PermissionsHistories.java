/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public class PermissionsHistories
/*     */ {
/*  41 */   private static final Logger logger = Logger.getLogger(PermissionsHistories.class.getName());
/*     */   
/*     */   private static final String GET_HISTORY = "SELECT * FROM PERMISSIONSHISTORY ORDER BY OBJECTID, EVENTDATE";
/*     */   private static final String ADD_HISTORY = "INSERT INTO PERMISSIONSHISTORY(OBJECTID, EVENTDATE, PLAYERID, PERFORMER, EVENT) VALUES(?,?,?,?,?)";
/*     */   private static final String DELETE_HISTORY = "DELETE FROM PERMISSIONSHISTORY WHERE OBJECTID=?";
/*     */   private static final String PURGE_HISTORY = "DELETE FROM PERMISSIONSHISTORY WHERE EVENTDATE<?";
/*  47 */   private static Map<Long, PermissionsHistory> objectHistories = new ConcurrentHashMap<>();
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
/*     */   public static void loadAll() {
/*  60 */     logger.log(Level.INFO, "Purging permissions history over 6 months old.");
/*  61 */     long start = System.nanoTime();
/*  62 */     long count = 0L;
/*  63 */     Connection dbcon = null;
/*  64 */     PreparedStatement ps = null;
/*  65 */     ResultSet rs = null;
/*     */     
/*     */     try {
/*  68 */       dbcon = DbConnector.getPlayerDbCon();
/*  69 */       ps = dbcon.prepareStatement("DELETE FROM PERMISSIONSHISTORY WHERE EVENTDATE<?");
/*  70 */       ps.setLong(1, System.currentTimeMillis() - 14515200000L);
/*  71 */       count = ps.executeUpdate();
/*     */     }
/*  73 */     catch (SQLException ex) {
/*     */       
/*  75 */       logger.log(Level.WARNING, "Failed to load history for permissions.", ex);
/*     */     }
/*     */     finally {
/*     */       
/*  79 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  80 */       DbConnector.returnConnection(dbcon);
/*  81 */       long end = System.nanoTime();
/*  82 */       logger.log(Level.INFO, "Purged " + count + " permissions history. That took " + ((float)(end - start) / 1000000.0F) + " ms.");
/*     */     } 
/*     */ 
/*     */     
/*  86 */     logger.log(Level.INFO, "Loading all permissions history.");
/*  87 */     count = 0L;
/*  88 */     start = System.nanoTime();
/*     */     
/*     */     try {
/*  91 */       dbcon = DbConnector.getPlayerDbCon();
/*  92 */       ps = dbcon.prepareStatement("SELECT * FROM PERMISSIONSHISTORY ORDER BY OBJECTID, EVENTDATE");
/*  93 */       rs = ps.executeQuery();
/*  94 */       while (rs.next())
/*     */       {
/*  96 */         long objectId = rs.getLong("OBJECTID");
/*  97 */         long eventDate = rs.getLong("EVENTDATE");
/*  98 */         long playerId = rs.getLong("PLAYERID");
/*  99 */         String performer = rs.getString("PERFORMER");
/* 100 */         String event = rs.getString("EVENT");
/* 101 */         add(objectId, eventDate, playerId, performer, event);
/* 102 */         count++;
/*     */       }
/*     */     
/* 105 */     } catch (SQLException ex) {
/*     */       
/* 107 */       logger.log(Level.WARNING, "Failed to load history for permissions.", ex);
/*     */     }
/*     */     finally {
/*     */       
/* 111 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 112 */       DbConnector.returnConnection(dbcon);
/* 113 */       long end = System.nanoTime();
/* 114 */       logger.log(Level.INFO, "Loaded " + count + " permissions history. That took " + ((float)(end - start) / 1000000.0F) + " ms.");
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
/*     */ 
/*     */   
/*     */   public static void moveHistories(long fromId, long toId) {
/* 128 */     Long id = Long.valueOf(fromId);
/* 129 */     if (objectHistories.containsKey(id)) {
/*     */       
/* 131 */       PermissionsHistory oldHistories = objectHistories.get(id);
/* 132 */       for (PermissionsHistoryEntry phe : oldHistories.getHistoryEvents())
/*     */       {
/* 134 */         addHistoryEntry(toId, phe.getEventDate(), phe.getPlayerId(), phe.getPlayerName(), phe.getEvent());
/*     */       }
/* 136 */       dbRemove(fromId);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static PermissionsHistory getPermissionsHistoryFor(long objectId) {
/* 142 */     Long id = Long.valueOf(objectId);
/* 143 */     if (objectHistories.containsKey(id))
/*     */     {
/* 145 */       return objectHistories.get(id);
/*     */     }
/*     */ 
/*     */     
/* 149 */     PermissionsHistory ph = new PermissionsHistory();
/* 150 */     objectHistories.put(id, ph);
/* 151 */     return ph;
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
/*     */   private static void add(long objectId, long eventTime, long playerId, String playerName, String event) {
/* 166 */     PermissionsHistory ph = getPermissionsHistoryFor(objectId);
/* 167 */     ph.add(eventTime, playerId, playerName, event);
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
/*     */   public static void addHistoryEntry(long objectId, long eventTime, long playerId, String playerName, String event) {
/* 182 */     add(objectId, eventTime, playerId, playerName, event);
/* 183 */     dbAddHistoryEvent(objectId, eventTime, playerId, playerName, event);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void dbAddHistoryEvent(long objectId, long eventTime, long playerId, String playerName, String event) {
/* 189 */     Connection dbcon = null;
/* 190 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 193 */       dbcon = DbConnector.getPlayerDbCon();
/* 194 */       ps = dbcon.prepareStatement("INSERT INTO PERMISSIONSHISTORY(OBJECTID, EVENTDATE, PLAYERID, PERFORMER, EVENT) VALUES(?,?,?,?,?)");
/* 195 */       String newEvent = event.replace("\"", "'");
/* 196 */       if (newEvent.length() > 255)
/*     */       {
/* 198 */         newEvent = newEvent.substring(0, 250) + "...";
/*     */       }
/* 200 */       ps.setLong(1, objectId);
/* 201 */       ps.setLong(2, eventTime);
/* 202 */       ps.setLong(3, playerId);
/* 203 */       ps.setString(4, playerName);
/* 204 */       ps.setString(5, newEvent);
/* 205 */       ps.executeUpdate();
/*     */     }
/* 207 */     catch (SQLException ex) {
/*     */       
/* 209 */       logger.log(Level.WARNING, "Failed to add permissions history for object (" + objectId + ")", ex);
/*     */     }
/*     */     finally {
/*     */       
/* 213 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 214 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void remove(long objectId) {
/* 225 */     Long id = Long.valueOf(objectId);
/* 226 */     if (objectHistories.containsKey(id)) {
/*     */       
/* 228 */       dbRemove(objectId);
/* 229 */       objectHistories.remove(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void dbRemove(long objectId) {
/* 239 */     Connection dbcon = null;
/* 240 */     PreparedStatement ps = null;
/* 241 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 244 */       dbcon = DbConnector.getPlayerDbCon();
/* 245 */       ps = dbcon.prepareStatement("DELETE FROM PERMISSIONSHISTORY WHERE OBJECTID=?");
/* 246 */       ps.setLong(1, objectId);
/* 247 */       ps.executeUpdate();
/*     */     }
/* 249 */     catch (SQLException ex) {
/*     */       
/* 251 */       logger.log(Level.WARNING, "Failed to delete permissions history for object " + objectId + ".", ex);
/*     */     }
/*     */     finally {
/*     */       
/* 255 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 256 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getHistory(long objectId, int numevents) {
/* 262 */     Long id = Long.valueOf(objectId);
/* 263 */     if (objectHistories.containsKey(id)) {
/*     */       
/* 265 */       PermissionsHistory ph = objectHistories.get(id);
/* 266 */       return ph.getHistory(numevents);
/*     */     } 
/*     */ 
/*     */     
/* 270 */     return new String[0];
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\PermissionsHistories.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */