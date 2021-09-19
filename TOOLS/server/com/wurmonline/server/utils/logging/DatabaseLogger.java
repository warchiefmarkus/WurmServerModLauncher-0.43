/*     */ package com.wurmonline.server.utils.logging;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DatabaseLogger<T extends WurmLoggable>
/*     */   implements Runnable
/*     */ {
/*  55 */   private static final Logger logger = Logger.getLogger(DatabaseLogger.class.getName());
/*     */   
/*  57 */   private final Queue<T> queue = new ConcurrentLinkedQueue<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String iLoggerDescription;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Class<T> iLoggableClass;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int iMaxLoggablesToRemovePerCycle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DatabaseLogger(String aLoggerDescription, Class<T> aLoggableClass, int aMaxLoggablesToRemovePerCycle) {
/*  91 */     this.iLoggerDescription = aLoggerDescription;
/*  92 */     this.iLoggableClass = aLoggableClass;
/*  93 */     this.iMaxLoggablesToRemovePerCycle = aMaxLoggablesToRemovePerCycle;
/*     */     
/*  95 */     logger.info("Creating Database logger " + aLoggerDescription + " for WurmLoggable type: " + aLoggableClass.getName() + ", MaxLoggablesToRemovePerCycle: " + aMaxLoggablesToRemovePerCycle);
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
/*     */   public final void run() {
/* 107 */     Connection logsConnection = null;
/* 108 */     PreparedStatement logsStatement = null;
/*     */     
/*     */     try {
/* 111 */       if (logger.isLoggable(Level.FINEST))
/*     */       {
/* 113 */         logger.finest("Starting DatabaseLogger.run() " + this.iLoggerDescription + " for WurmLoggable type: " + this.iLoggableClass
/* 114 */             .getName());
/*     */       }
/*     */       
/* 117 */       if (!this.queue.isEmpty())
/*     */       {
/* 119 */         int objectsRemoved = 0;
/* 120 */         logsConnection = DbConnector.getLogsDbCon();
/* 121 */         while (!this.queue.isEmpty() && objectsRemoved <= this.iMaxLoggablesToRemovePerCycle) {
/*     */           
/* 123 */           WurmLoggable wurmLoggable = (WurmLoggable)this.queue.remove();
/* 124 */           objectsRemoved++;
/* 125 */           if (logger.isLoggable(Level.FINEST))
/*     */           {
/* 127 */             logger.finest("Removed from FIFO queue: " + wurmLoggable);
/*     */           }
/* 129 */           logsStatement = logsConnection.prepareStatement(wurmLoggable.getDatabaseInsertStatement());
/* 130 */           addLoggableToBatch(logsStatement, (T)wurmLoggable);
/*     */         } 
/* 132 */         logsStatement.executeBatch();
/* 133 */         if (logger.isLoggable(Level.FINER) || (!this.queue.isEmpty() && logger.isLoggable(Level.FINE)))
/*     */         {
/* 135 */           logger.fine("Removed " + this.iLoggableClass.getName() + ' ' + objectsRemoved + " objects from FIFO queue, which now contains " + this.queue
/* 136 */               .size() + " objects");
/*     */         }
/*     */       }
/*     */     
/* 140 */     } catch (SQLException e) {
/*     */       
/* 142 */       logger.log(Level.WARNING, "Problem getting WurmLogs connection due to " + e.getMessage(), e);
/*     */     }
/*     */     finally {
/*     */       
/* 146 */       if (logger.isLoggable(Level.FINEST))
/*     */       {
/* 148 */         logger.finest("Ending DatabaseLogger.run() " + this.iLoggerDescription + " for WurmLoggable type: " + this.iLoggableClass
/* 149 */             .getName());
/*     */       }
/* 151 */       DbUtilities.closeDatabaseObjects(logsStatement, null);
/* 152 */       DbConnector.returnConnection(logsConnection);
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
/*     */   abstract void addLoggableToBatch(PreparedStatement paramPreparedStatement, T paramT) throws SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void addToQueue(T loggable) {
/* 178 */     if (loggable != null) {
/*     */       
/* 180 */       if (logger.isLoggable(Level.FINEST))
/*     */       {
/* 182 */         logger.finest("Adding to database " + this.iLoggerDescription + " loggable queue: " + loggable);
/*     */       }
/* 184 */       this.queue.add(loggable);
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
/*     */   int getNumberOfLoggableObjectsInQueue() {
/* 196 */     return this.queue.size();
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
/*     */   final String getLoggerDescription() {
/* 208 */     return this.iLoggerDescription;
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
/*     */   final Class<T> getLoggableClass() {
/* 220 */     return this.iLoggableClass;
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
/*     */   final int getMaxLoggablesToRemovePerCycle() {
/* 234 */     return this.iMaxLoggablesToRemovePerCycle;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\logging\DatabaseLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */