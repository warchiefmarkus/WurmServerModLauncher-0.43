/*     */ package com.wurmonline.server.utils.logging;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
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
/*     */ public class TileLog
/*     */   implements TimeConstants, WurmLoggable
/*     */ {
/*  38 */   private static Logger logger = Logger.getLogger(TileLog.class.getName());
/*  39 */   static long lastPruned = 0L;
/*  40 */   static int numBatchEvents = 50;
/*  41 */   static long pruneInterval = 432000000L;
/*     */   
/*  43 */   private static PreparedStatement lastmPS = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   private static int lastmPSCount = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public static int overallLastmPSCount = 0;
/*     */   
/*     */   public static final String LOAD_ALL_ENTRIES = "SELECT * FROM TILE_LOG";
/*     */   
/*     */   public static final String PRUNE_ENTRIES = "DELETE FROM TILE_LOG WHERE DATE<?";
/*     */   
/*     */   static final String INSERT_TILE_LOG = "INSERT INTO TILE_LOG (TILEX,TILEY, LAYER, PERFORMER, ACTION, DATE) VALUES ( ?, ?, ?, ?, ?, ?)";
/*     */   
/*     */   int tilex;
/*     */   int tiley;
/*     */   int layer;
/*     */   long performer;
/*     */   int action;
/*     */   long date;
/*  67 */   public static final LinkedList<TileLog> logEntries = new LinkedList<>();
/*     */ 
/*     */   
/*     */   public TileLog(int _tx, int _ty, int _layer, long _performer, int _action, boolean load) {
/*  71 */     this.tilex = _tx;
/*  72 */     this.tiley = _ty;
/*  73 */     this.layer = _layer;
/*  74 */     this.performer = _performer;
/*  75 */     this.action = _action;
/*  76 */     if (!load) {
/*     */       
/*  78 */       this.date = System.currentTimeMillis();
/*  79 */       save();
/*     */     } 
/*  81 */     logEntries.add(this);
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
/*     */   public static final int getLogSize() {
/* 100 */     return logEntries.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDate(long newDate) {
/* 105 */     this.date = newDate;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearBatches() {
/*     */     try {
/* 112 */       if (lastmPS != null)
/*     */       {
/* 114 */         int[] x = lastmPS.executeBatch();
/* 115 */         logger.log(Level.INFO, "Saved tile log batch size " + x.length);
/* 116 */         lastmPS.close();
/* 117 */         lastmPS = null;
/* 118 */         lastmPSCount = 0;
/*     */       }
/*     */     
/* 121 */     } catch (SQLException iox) {
/*     */       
/* 123 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void save() {
/*     */     try {
/* 131 */       if (lastmPS == null) {
/*     */         
/* 133 */         Connection dbcon = DbConnector.getLogsDbCon();
/* 134 */         lastmPS = dbcon.prepareStatement("INSERT INTO TILE_LOG (TILEX,TILEY, LAYER, PERFORMER, ACTION, DATE) VALUES ( ?, ?, ?, ?, ?, ?)");
/*     */       } 
/* 136 */       lastmPS.setInt(1, getTileX());
/* 137 */       lastmPS.setInt(2, getTileY());
/* 138 */       lastmPS.setInt(3, getLayer());
/* 139 */       lastmPS.setLong(4, getPerformer());
/* 140 */       lastmPS.setInt(5, getAction());
/* 141 */       lastmPS.setLong(6, getDate());
/* 142 */       lastmPS.addBatch();
/* 143 */       overallLastmPSCount++;
/* 144 */       lastmPSCount++;
/* 145 */       if (lastmPSCount > numBatchEvents)
/*     */       {
/* 147 */         long checkms = System.currentTimeMillis();
/* 148 */         lastmPS.executeBatch();
/* 149 */         lastmPS.close();
/* 150 */         lastmPS = null;
/* 151 */         if (System.currentTimeMillis() - checkms > 300L || logger.isLoggable(Level.FINEST)) {
/* 152 */           logger.log(Level.WARNING, "TileLog batch took " + (System.currentTimeMillis() - checkms) + " ms for " + lastmPSCount + " updates.");
/*     */         }
/* 154 */         lastmPSCount = 0;
/*     */       }
/*     */     
/* 157 */     } catch (SQLException sqx) {
/*     */       
/* 159 */       logger.log(Level.WARNING, "Failed to save log entry.", sqx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void loadAllLogEntries() {
/*     */     try {
/* 167 */       Connection dbcon = DbConnector.getLogsDbCon();
/* 168 */       PreparedStatement ps = dbcon.prepareStatement("SELECT * FROM TILE_LOG");
/* 169 */       ResultSet rs = ps.executeQuery();
/* 170 */       while (rs.next())
/*     */       {
/*     */ 
/*     */         
/* 174 */         TileLog tl = new TileLog(rs.getInt("TILEX"), rs.getInt("TILEY"), rs.getInt("LAYER"), rs.getLong("PERFORMER"), rs.getInt("ACTION"), true);
/* 175 */         tl.setDate(rs.getLong("DATE"));
/*     */       }
/*     */     
/* 178 */     } catch (SQLException sqx) {
/*     */       
/* 180 */       logger.log(Level.WARNING, "Failed to load log entry.", sqx);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void pruneLogEntries() {
/* 186 */     if (System.currentTimeMillis() - lastPruned > pruneInterval) {
/*     */       
/* 188 */       lastPruned = System.currentTimeMillis();
/* 189 */       long cutDate = System.currentTimeMillis() - pruneInterval;
/*     */       
/*     */       try {
/* 192 */         Connection dbcon = DbConnector.getLogsDbCon();
/* 193 */         PreparedStatement ps = dbcon.prepareStatement("DELETE FROM TILE_LOG WHERE DATE<?");
/* 194 */         ps.setLong(1, cutDate);
/* 195 */         ps.execute();
/*     */       }
/* 197 */       catch (SQLException sqx) {
/*     */         
/* 199 */         logger.log(Level.WARNING, "Failed to prune log entries.", sqx);
/*     */       } 
/* 201 */       for (ListIterator<TileLog> lit = logEntries.listIterator(); lit.hasNext();) {
/*     */         
/* 203 */         if (((TileLog)lit.next()).getDate() < cutDate) {
/* 204 */           lit.remove();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final List<TileLog> getEventsFor(int tilex, int tiley, int layer) {
/* 215 */     LinkedList<TileLog> matches = new LinkedList<>();
/* 216 */     for (TileLog t : logEntries) {
/*     */       
/* 218 */       if (t.getTileX() == tilex && t.getTileY() == tiley && t.getLayer() == layer)
/* 219 */         matches.add(t); 
/*     */     } 
/* 221 */     return matches;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTileX() {
/* 226 */     return this.tilex;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTileY() {
/* 231 */     return this.tiley;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLayer() {
/* 236 */     return this.layer;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAction() {
/* 241 */     return this.action;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getPerformer() {
/* 246 */     return this.performer;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getDate() {
/* 251 */     return this.date;
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
/* 262 */     return "ItemTransfer [tilex=" + this.tilex + ", tiley=" + this.tiley + ", performer=" + this.performer + ", action=" + this.action + ", date=" + this.date + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getDatabaseInsertStatement() {
/* 269 */     return "INSERT INTO TILE_LOG (TILEX,TILEY, LAYER, PERFORMER, ACTION, DATE) VALUES ( ?, ?, ?, ?, ?, ?)";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\logging\TileLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */