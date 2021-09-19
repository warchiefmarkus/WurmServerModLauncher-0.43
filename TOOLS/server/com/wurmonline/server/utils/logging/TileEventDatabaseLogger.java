/*    */ package com.wurmonline.server.utils.logging;
/*    */ 
/*    */ import com.wurmonline.server.Constants;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.SQLException;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TileEventDatabaseLogger
/*    */   extends DatabaseLogger<TileEvent>
/*    */ {
/* 32 */   private static final Logger logger = Logger.getLogger(TileEventDatabaseLogger.class.getName());
/* 33 */   private int numsBatched = 0;
/*    */ 
/*    */ 
/*    */   
/*    */   private static final int pruneInterval = 10000;
/*    */ 
/*    */ 
/*    */   
/*    */   public TileEventDatabaseLogger(String aLoggerDescription, int aMaxLoggablesToRemovePerCycle) {
/* 42 */     super(aLoggerDescription, TileEvent.class, aMaxLoggablesToRemovePerCycle);
/* 43 */     logger.info("Creating Tile Event logger, System useTileLog option: " + Constants.useTileEventLog);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addLoggableToBatch(PreparedStatement logsStatement, TileEvent object) throws SQLException {
/* 55 */     TileEvent tileEvent = object;
/* 56 */     logsStatement.setInt(1, tileEvent.getTileX());
/* 57 */     logsStatement.setInt(2, tileEvent.getTileY());
/* 58 */     logsStatement.setInt(3, tileEvent.getLayer());
/* 59 */     logsStatement.setLong(4, tileEvent.getPerformer());
/* 60 */     logsStatement.setInt(5, tileEvent.getAction());
/* 61 */     logsStatement.setLong(6, tileEvent.getDate());
/* 62 */     logsStatement.addBatch();
/* 63 */     this.numsBatched++;
/* 64 */     checkPruneLimit();
/*    */   }
/*    */ 
/*    */   
/*    */   private void checkPruneLimit() {
/* 69 */     if (this.numsBatched > 10000) {
/*    */       
/* 71 */       logger.log(Level.INFO, "Pruning entries");
/* 72 */       TileEvent.pruneLogEntries();
/* 73 */       this.numsBatched = 0;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\logging\TileEventDatabaseLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */