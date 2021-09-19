/*     */ package com.wurmonline.server.utils;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
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
/*     */ 
/*     */ public class CreaturePositionDatabaseUpdater
/*     */   extends DatabaseUpdater<CreaturePositionDbUpdatable>
/*     */ {
/*  42 */   private static final Logger logger = Logger.getLogger(CreaturePositionDatabaseUpdater.class.getName());
/*  43 */   private final Map<Long, CreaturePositionDbUpdatable> updatesMap = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CreaturePositionDatabaseUpdater(String aUpdaterDescription, int aMaxUpdatablesToRemovePerCycle) {
/*  55 */     super(aUpdaterDescription, CreaturePositionDbUpdatable.class, aMaxUpdatablesToRemovePerCycle);
/*  56 */     logger.info("Creating Creature Position Updater.");
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
/*     */   public void addToQueue(CreaturePositionDbUpdatable updatable) {
/*  68 */     if (updatable != null) {
/*     */       
/*  70 */       if (logger.isLoggable(Level.FINEST))
/*     */       {
/*  72 */         logger.finest("Adding to database " + updatable + " updatable queue: " + updatable);
/*     */       }
/*  74 */       CreaturePositionDbUpdatable waiting = this.updatesMap.get(Long.valueOf(updatable.getId()));
/*     */ 
/*     */       
/*  77 */       if (waiting != null)
/*  78 */         this.queue.remove(waiting); 
/*  79 */       this.updatesMap.put(Long.valueOf(updatable.getId()), updatable);
/*  80 */       this.queue.add(updatable);
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
/*     */   Connection getDatabaseConnection() throws SQLException {
/*  93 */     return DbConnector.getCreatureDbCon();
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
/*     */   void addUpdatableToBatch(PreparedStatement updateStatement, CreaturePositionDbUpdatable aDbUpdatable) throws SQLException {
/* 105 */     this.updatesMap.remove(Long.valueOf(aDbUpdatable.getId()));
/* 106 */     updateStatement.setFloat(1, aDbUpdatable.getPositionX());
/* 107 */     updateStatement.setFloat(2, aDbUpdatable.getPositionY());
/* 108 */     updateStatement.setFloat(3, aDbUpdatable.getPositionZ());
/* 109 */     float rot = Creature.normalizeAngle(aDbUpdatable.getRotation());
/* 110 */     updateStatement.setFloat(4, rot);
/* 111 */     updateStatement.setInt(5, aDbUpdatable.getZoneid());
/* 112 */     updateStatement.setInt(6, aDbUpdatable.getLayer());
/* 113 */     updateStatement.setLong(7, aDbUpdatable.getBridgeId());
/* 114 */     updateStatement.setLong(8, aDbUpdatable.getId());
/* 115 */     updateStatement.addBatch();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\CreaturePositionDatabaseUpdater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */