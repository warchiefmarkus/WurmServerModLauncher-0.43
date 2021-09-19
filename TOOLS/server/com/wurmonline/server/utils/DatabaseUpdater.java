/*     */ package com.wurmonline.server.utils;
/*     */ 
/*     */ import com.wurmonline.server.Constants;
/*     */ import com.wurmonline.server.DbConnector;
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
/*     */ 
/*     */ public abstract class DatabaseUpdater<T extends WurmDbUpdatable>
/*     */   implements Runnable
/*     */ {
/*  56 */   private static final Logger logger = Logger.getLogger(DatabaseUpdater.class.getName());
/*     */   
/*  58 */   protected final Queue<T> queue = new ConcurrentLinkedQueue<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String iUpdaterDescription;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Class<T> iUpdatableClass;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int iMaxUpdatablesToRemovePerCycle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DatabaseUpdater(String aUpdaterDescription, Class<T> aUpdatableClass, int aMaxUpdatablesToRemovePerCycle) {
/*  91 */     this.iUpdaterDescription = aUpdaterDescription;
/*  92 */     this.iUpdatableClass = aUpdatableClass;
/*  93 */     this.iMaxUpdatablesToRemovePerCycle = aMaxUpdatablesToRemovePerCycle;
/*     */     
/*  95 */     logger.info("Creating Database updater " + aUpdaterDescription + " for WurmDbUpdatable type: " + aUpdatableClass
/*  96 */         .getName() + ", MaxUpdatablesToRemovePerCycle: " + aMaxUpdatablesToRemovePerCycle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void run() {
/* 107 */     Connection updaterConnection = null;
/* 108 */     PreparedStatement updaterStatement = null;
/*     */     
/*     */     try {
/* 111 */       if (logger.isLoggable(Level.FINEST))
/*     */       {
/* 113 */         logger.finest("Starting DatabaseUpdater.run() " + this.iUpdaterDescription + " for WurmDbUpdatable type: " + this.iUpdatableClass
/* 114 */             .getName());
/*     */       }
/*     */       
/* 117 */       if (!this.queue.isEmpty())
/*     */       {
/* 119 */         long start = System.nanoTime();
/* 120 */         int objectsRemoved = 0;
/* 121 */         updaterConnection = getDatabaseConnection();
/* 122 */         updaterStatement = null;
/* 123 */         while (!this.queue.isEmpty() && objectsRemoved <= this.iMaxUpdatablesToRemovePerCycle) {
/*     */           
/* 125 */           WurmDbUpdatable wurmDbUpdatable = (WurmDbUpdatable)this.queue.remove();
/* 126 */           objectsRemoved++;
/* 127 */           if (updaterStatement == null)
/* 128 */             updaterStatement = updaterConnection.prepareStatement(wurmDbUpdatable.getDatabaseUpdateStatement()); 
/* 129 */           addUpdatableToBatch(updaterStatement, (T)wurmDbUpdatable);
/*     */         } 
/* 131 */         if (updaterStatement != null)
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 136 */           updaterStatement.executeBatch();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 141 */         float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 142 */         if (logger.isLoggable(Level.FINER) || (this.queue
/* 143 */           .size() > this.iMaxUpdatablesToRemovePerCycle && logger.isLoggable(Level.FINE)) || lElapsedTime > (float)Constants.lagThreshold)
/*     */         {
/*     */           
/* 146 */           logger.fine("Removed " + this.iUpdatableClass.getName() + ' ' + objectsRemoved + " objects from FIFO queue, which now contains " + this.queue
/* 147 */               .size() + " objects and took " + lElapsedTime + " millis.");
/*     */         }
/*     */       }
/*     */     
/*     */     }
/* 152 */     catch (SQLException e) {
/*     */       
/* 154 */       logger.log(Level.INFO, "Error in DatabaseUpdater.run() " + this.iUpdaterDescription + " for WurmDbUpdatable type: " + this.iUpdatableClass
/* 155 */           .getName());
/* 156 */       logger.log(Level.WARNING, "Problem getting WurmLogs connection due to " + e.getMessage(), e);
/*     */     }
/*     */     finally {
/*     */       
/* 160 */       if (logger.isLoggable(Level.FINEST))
/*     */       {
/* 162 */         logger.finest("Ending DatabaseUpdater.run() " + this.iUpdaterDescription + " for WurmDbUpdatable type: " + this.iUpdatableClass
/* 163 */             .getName());
/*     */       }
/* 165 */       DbUtilities.closeDatabaseObjects(updaterStatement, null);
/* 166 */       DbConnector.returnConnection(updaterConnection);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void saveImmediately() {
/* 172 */     Connection updaterConnection = null;
/* 173 */     PreparedStatement updaterStatement = null;
/*     */     
/*     */     try {
/* 176 */       if (logger.isLoggable(Level.FINEST))
/*     */       {
/* 178 */         logger.finest("Starting DatabaseUpdater.run() " + this.iUpdaterDescription + " for WurmDbUpdatable type: " + this.iUpdatableClass
/* 179 */             .getName());
/*     */       }
/*     */       
/* 182 */       if (!this.queue.isEmpty())
/*     */       {
/* 184 */         long start = System.nanoTime();
/* 185 */         int objectsRemoved = 0;
/* 186 */         updaterConnection = getDatabaseConnection();
/* 187 */         updaterStatement = null;
/* 188 */         while (!this.queue.isEmpty()) {
/*     */           
/* 190 */           WurmDbUpdatable wurmDbUpdatable = (WurmDbUpdatable)this.queue.remove();
/* 191 */           objectsRemoved++;
/* 192 */           if (updaterStatement == null)
/* 193 */             updaterStatement = updaterConnection.prepareStatement(wurmDbUpdatable.getDatabaseUpdateStatement()); 
/* 194 */           addUpdatableToBatch(updaterStatement, (T)wurmDbUpdatable);
/*     */         } 
/* 196 */         if (updaterStatement != null)
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 201 */           updaterStatement.executeBatch();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 206 */         float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 207 */         if (logger.isLoggable(Level.FINER) || (this.queue
/* 208 */           .size() > this.iMaxUpdatablesToRemovePerCycle && logger.isLoggable(Level.FINE)) || lElapsedTime > (float)Constants.lagThreshold)
/*     */         {
/*     */           
/* 211 */           logger.fine("Removed " + this.iUpdatableClass.getName() + ' ' + objectsRemoved + " objects from FIFO queue, which now contains " + this.queue
/* 212 */               .size() + " objects and took " + lElapsedTime + " millis.");
/*     */         }
/*     */       }
/*     */     
/*     */     }
/* 217 */     catch (SQLException e) {
/*     */       
/* 219 */       logger.log(Level.WARNING, "Problem getting WurmLogs connection due to " + e.getMessage(), e);
/*     */     }
/*     */     finally {
/*     */       
/* 223 */       if (logger.isLoggable(Level.FINEST))
/*     */       {
/* 225 */         logger.finest("Ending DatabaseUpdater.run() " + this.iUpdaterDescription + " for WurmDbUpdatable type: " + this.iUpdatableClass
/* 226 */             .getName());
/*     */       }
/* 228 */       DbUtilities.closeDatabaseObjects(updaterStatement, null);
/* 229 */       DbConnector.returnConnection(updaterConnection);
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
/*     */   abstract Connection getDatabaseConnection() throws SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void addUpdatableToBatch(PreparedStatement paramPreparedStatement, T paramT) throws SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addToQueue(T updatable) {
/* 263 */     if (updatable != null) {
/*     */       
/* 265 */       if (logger.isLoggable(Level.FINEST))
/*     */       {
/* 267 */         logger.finest("Adding to database " + this.iUpdaterDescription + " updatable queue: " + updatable);
/*     */       }
/* 269 */       this.queue.add(updatable);
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
/*     */   final int getNumberOfUpdatableObjectsInQueue() {
/* 281 */     return this.queue.size();
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
/*     */   final String getUpdaterDescription() {
/* 293 */     return this.iUpdaterDescription;
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
/*     */   final Class<T> getUpdatableClass() {
/* 305 */     return this.iUpdatableClass;
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
/*     */   final int getMaxUpdatablesToRemovePerCycle() {
/* 321 */     return this.iMaxUpdatablesToRemovePerCycle;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\DatabaseUpdater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */