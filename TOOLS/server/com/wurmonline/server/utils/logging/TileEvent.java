/*     */ package com.wurmonline.server.utils.logging;
/*     */ 
/*     */ import com.wurmonline.server.Constants;
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import edu.umd.cs.findbugs.annotations.NonNull;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ public final class TileEvent
/*     */   implements WurmLoggable, TimeConstants
/*     */ {
/*  43 */   private static final Logger logger = Logger.getLogger(TileEvent.class.getName());
/*  44 */   private static long lastPruned = System.currentTimeMillis() + 21600000L;
/*  45 */   private static long pruneInterval = 432000000L;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String PRUNE_ENTRIES = "DELETE FROM TILE_LOG WHERE DATE<?";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String FIND_ENTRIES_FOR_A_TILE = "SELECT * FROM TILE_LOG WHERE TILEX = ? AND TILEY = ? AND LAYER = ? ORDER BY DATE ASC";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String INSERT_TILE_LOG = "INSERT INTO TILE_LOG (TILEX,TILEY, LAYER, PERFORMER, ACTION, DATE) VALUES ( ?, ?, ?, ?, ?, ?)";
/*     */ 
/*     */ 
/*     */   
/*     */   private final int tilex;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int tiley;
/*     */ 
/*     */   
/*     */   private final int layer;
/*     */ 
/*     */   
/*     */   private final long performer;
/*     */ 
/*     */   
/*     */   private final int action;
/*     */ 
/*     */   
/*     */   private final long date;
/*     */ 
/*     */   
/*  80 */   private static final TileEventDatabaseLogger tileLogger = new TileEventDatabaseLogger("Tile logger", 500);
/*  81 */   private static final ConcurrentHashMap<Long, TileEvent> playersLog = new ConcurrentHashMap<>();
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
/*     */   public TileEvent(int _tileX, int _tileY, int _layer, long _performer, int _action) {
/* 101 */     this.tilex = _tileX;
/* 102 */     this.tiley = _tileY;
/* 103 */     this.layer = _layer;
/* 104 */     this.performer = _performer;
/* 105 */     this.action = _action;
/* 106 */     this.date = System.currentTimeMillis();
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
/*     */   public TileEvent(int _tileX, int _tileY, int _layer, long _performer, int _action, long _date) {
/* 130 */     this.tilex = _tileX;
/* 131 */     this.tiley = _tileY;
/* 132 */     this.layer = _layer;
/* 133 */     this.performer = _performer;
/* 134 */     this.action = _action;
/* 135 */     this.date = _date;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TileEventDatabaseLogger getTilelogger() {
/* 146 */     return tileLogger;
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
/*     */   public static void log(int _tileX, int _tileY, int _layer, long _performer, int _action) {
/* 172 */     if (Constants.useTileEventLog) {
/*     */       
/* 174 */       TileEvent lEvent = null;
/*     */       
/*     */       try {
/* 177 */         TileEvent oEvent = playersLog.get(Long.valueOf(_performer));
/*     */         
/* 179 */         if (oEvent != null && oEvent.tilex == _tileX && oEvent.tiley == _tileY && oEvent.layer == _layer && oEvent.action == _action && oEvent.date > 
/* 180 */           System.currentTimeMillis() - 300000L) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 185 */         lEvent = new TileEvent(_tileX, _tileY, _layer, _performer, _action);
/* 186 */         playersLog.put(Long.valueOf(_performer), lEvent);
/* 187 */         tileLogger.addToQueue(lEvent);
/*     */       }
/* 189 */       catch (Exception ex) {
/*     */         
/* 191 */         logger.log(Level.WARNING, "Could not add to queue: " + lEvent + " due to " + ex.getMessage(), ex);
/*     */       } 
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
/*     */   
/*     */   static final int getLogSize() {
/* 207 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void pruneLogEntries() {
/* 215 */     if (System.currentTimeMillis() - lastPruned > pruneInterval) {
/*     */       
/* 217 */       lastPruned = System.currentTimeMillis();
/* 218 */       long cutDate = System.currentTimeMillis() - pruneInterval;
/* 219 */       Connection dbcon = null;
/* 220 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 223 */         dbcon = DbConnector.getLogsDbCon();
/* 224 */         ps = dbcon.prepareStatement("DELETE FROM TILE_LOG WHERE DATE<?");
/* 225 */         ps.setLong(1, cutDate);
/* 226 */         ps.execute();
/*     */       }
/* 228 */       catch (SQLException sqx) {
/*     */         
/* 230 */         logger.log(Level.WARNING, "Failed to prune log entries.", sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 234 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 235 */         DbConnector.returnConnection(dbcon);
/*     */       } 
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
/*     */   @NonNull
/*     */   public static List<TileEvent> getEventsFor(int aTileX, int aTileY, int aLayer) {
/* 263 */     List<TileEvent> matches = new LinkedList<>();
/*     */     
/* 265 */     if (Constants.useTileEventLog) {
/*     */       
/* 267 */       Connection dbcon = null;
/* 268 */       PreparedStatement ps = null;
/* 269 */       ResultSet rs = null;
/*     */       
/*     */       try {
/* 272 */         dbcon = DbConnector.getLogsDbCon();
/* 273 */         ps = dbcon.prepareStatement("SELECT * FROM TILE_LOG WHERE TILEX = ? AND TILEY = ? AND LAYER = ? ORDER BY DATE ASC");
/* 274 */         ps.setInt(1, aTileX);
/* 275 */         ps.setInt(2, aTileY);
/* 276 */         ps.setInt(3, aLayer);
/* 277 */         rs = ps.executeQuery();
/* 278 */         while (rs.next())
/*     */         {
/*     */           
/* 281 */           TileEvent lEvent = new TileEvent(rs.getInt("TILEX"), rs.getInt("TILEY"), rs.getInt("LAYER"), rs.getLong("PERFORMER"), rs.getInt("ACTION"), rs.getLong("DATE"));
/* 282 */           matches.add(lEvent);
/*     */         }
/*     */       
/* 285 */       } catch (SQLException sqx) {
/*     */         
/* 287 */         logger.log(Level.WARNING, "Failed to load log entry.", sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 291 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 292 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/* 295 */     return matches;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTileX() {
/* 300 */     return this.tilex;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTileY() {
/* 305 */     return this.tiley;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLayer() {
/* 310 */     return this.layer;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAction() {
/* 315 */     return this.action;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getPerformer() {
/* 320 */     return this.performer;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getDate() {
/* 325 */     return this.date;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDatabaseInsertStatement() {
/* 336 */     return "INSERT INTO TILE_LOG (TILEX,TILEY, LAYER, PERFORMER, ACTION, DATE) VALUES ( ?, ?, ?, ?, ?, ?)";
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
/* 347 */     return "TileEvent [tilex=" + this.tilex + ", tiley=" + this.tiley + ", layer=" + this.layer + ", performer=" + this.performer + ", action=" + this.action + ", date=" + this.date + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\logging\TileEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */